package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.AccountDao;
import so.sao.shop.supplier.dao.OrderMoneyRecordDao;
import so.sao.shop.supplier.dao.PurchaseDao;
import so.sao.shop.supplier.dao.RecordPurchaseDao;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.OrderMoneyRecord;
import so.sao.shop.supplier.domain.Purchase;
import so.sao.shop.supplier.domain.RecordPurchase;
import so.sao.shop.supplier.pojo.input.OrderMoneyRecordInput;
import so.sao.shop.supplier.pojo.input.OrderMoneyRecordRankInput;
import so.sao.shop.supplier.pojo.output.OrderMoneyRecordOutput;
import so.sao.shop.supplier.pojo.output.RecordToPurchaseOutput;
import so.sao.shop.supplier.pojo.vo.AccountOrderMoneyRecordVO;
import so.sao.shop.supplier.pojo.vo.AccountPurchaseVo;
import so.sao.shop.supplier.pojo.vo.OrderMoneyRecordVo;
import so.sao.shop.supplier.pojo.vo.PurchaseVo;
import so.sao.shop.supplier.service.OrderMoneyRecordService;
import so.sao.shop.supplier.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by niewenchao on 2017/7/19.
 */
@Service
public class OrderMoneyRecordServiceImpl implements OrderMoneyRecordService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderMoneyRecordDao orderMoneyRecordDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private PurchaseDao purchaseDao;
    @Autowired
    private RecordPurchaseDao recordPurchaseDao;

    /**
     * 保存结算明细、结算明细与订单关联关系、更新账户的上一次结算时间、更新订单中的账户状态
     *
     * @param accountList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderMoneyRecord(List<Account> accountList) throws Exception {

		/*
         * 1、判断accountList，不为空的话则循环列表
         * 2、新建插入数据、更新数据list
         *      a)、结算明细的列表orderMoneyRecordList
         *      b)、结算明细与订单关联关系列表recordPurchaseList
         *      c)、账户更新的数据列表accountList
         *      d)、订单更新的数据列表purchaseUpdateList
		 * 3、批量插入数据、批量更新数据
		 *      e)、批量更新供应商账户表中的上一次结算时间
		 *      f)、批量保存结算明细记录
		 *      g)、批量保存结算明细与订单关联关系
		 *      h)、批量更新订单中的账户状态
		 */

        //1、判断accountList，不为空的话则循环列表
        if (null != accountList && !accountList.isEmpty()) {

            //2、新建插入数据、更新数据list
            //a)、结算明细的列表orderMoneyRecordList
            List<OrderMoneyRecord> orderMoneyRecordList = new ArrayList<>();

            //b)、结算明细与订单关联关系列表recordPurchaseList
            List<RecordPurchase> recordPurchaseList = new ArrayList<>();

            //c)、账户更新的数据列表accountList
            List<Account> accountUpdateList = new ArrayList<>();

            //d)、订单更新的数据列表purchaseUpdateList
            List<Purchase> purchaseUpdateList = new ArrayList<>();

            for (Account account : accountList) {

                //设置新增、更新数据
                purchaseListByRemittanceType(account, orderMoneyRecordList, recordPurchaseList, purchaseUpdateList, accountUpdateList);
            }
            //3、批量插入数据、批量更新数据

            if (!accountUpdateList.isEmpty()) {
                //e)、批量更新供应商账户表中的上一次结算时间
                accountDao.updateAccountLastSettlementDate(accountUpdateList);
            }

            if (orderMoneyRecordList.isEmpty() || recordPurchaseList.isEmpty() || purchaseUpdateList.isEmpty()) {
                return;
            }

            //f)、批量保存结算明细记录
            orderMoneyRecordDao.saveOrderMoneyRecords(orderMoneyRecordList);

            //g)、批量保存结算明细与订单关联关系
            recordPurchaseDao.saveRecordPurchases(recordPurchaseList);

            //h)、批量更新订单中的账户状态
            purchaseDao.updatePurchasesAccountStatusById(purchaseUpdateList);

        }
    }

    /**
     * 根据不同结算方式，获取不同方式下的订单列表数据
     *
     * @param account              供应商账户
     * @param orderMoneyRecordList 结算明细list
     * @param recordPurchaseList   结算明细与订单关联关系list
     * @param purchaseUpdateList   待更新的订单list
     * @param accountUpdateList    待更新的账户list
     * @throws Exception
     */
    public void purchaseListByRemittanceType(Account account, List<OrderMoneyRecord> orderMoneyRecordList,
                                             List<RecordPurchase> recordPurchaseList, List<Purchase> purchaseUpdateList,
                                             List<Account> accountUpdateList) throws Exception {

        String remittanceType = account.getRemittanceType();//结算方式
        if (Ognl.isEmpty(remittanceType)) { //如果结算方式为空，则返回
            return;
        }

        String remittanced = account.getRemittanced();//每个月的几号或固定天数
        //如果为空，则返回
        if (Ognl.isEmpty(remittanced)) {
            return;
        }
        int tmpRemittanced = Integer.valueOf(remittanced);

        Date lastSettlementDate = account.getLastSettlementDate();//上一次结账时间
        //获取账户id
        Long accountId = account.getAccountId();
        Date currentDate = new Date();

        Date updateLastSettDate = null;

        /* 1、获取不同结算方式下，对应时间段内订单列表
         *    1)、按自然月结算
         *      a)、获取当前时间与上次结算时间相差的月数
         *      b)、获取上次结算时间相差i个月之后的时间
         *      c)、获取指定时间上一个月的内的订单数据
         *      d)、设置结算明细、结算明细与订单关联关系、待更新的订单、待更新的账户的值，并添加到对应的list
         *    2)、按固定时间结算
         *      e)、获取两个时间相差天数
         *      f)、相差天数与固定结算时间取整，即判断几个周期未结算
         *      g)、获取上次结算时间加上固定结算时间j个周期之后的时间
         *      h)、获取指定时间段内的订单数据
         *      i)、设置结算明细、结算明细与订单关联关系、待更新的订单、待更新的账户的值，并添加到对应的list
         * 2、设置更新供应商账户数据，并添加到对应的列表
         *
         */

        //1、获取不同结算方式下，对应时间段内订单列表
        List<Purchase> purchaseList = null;
        //1)、按自然月结算
        if ("1".equals(remittanceType)) {
            updateLastSettDate = firstDayOfMonth(currentDate);//设置上一次结帐时间为当前月1号

            //a)、获取当前时间与上次结算时间相差的月数
            int months = countMonths(lastSettlementDate, currentDate);
            if (months >= 1) {
                for (int i = 1; i <= months; i++) {
                    //b)、获取上次结算时间相差i个月之后的时间
                    Date tmpLastSettlementDate = addMonths(lastSettlementDate, i);

                    Date tmpFirstDayOfMonth = firstDayOfMonth(tmpLastSettlementDate);

                    //c)、获取指定时间上一个月内的订单数据
                    purchaseList = purchaseDao.findPurchaseMonth(accountId, tmpFirstDayOfMonth);

                    Date checkOutDate = addDays(tmpFirstDayOfMonth, tmpRemittanced - 1);//结账时间

                    //d)、设置结算明细、结算明细与订单关联关系、待更新的订单、待更新的账户的值，并添加到对应的list
                    setValues(purchaseList, account, currentDate, orderMoneyRecordList, recordPurchaseList,
                            purchaseUpdateList, checkOutDate);
                }
            }
        } else if ("2".equals(remittanceType)) {
            //2)、按固定时间结算
            //设置上一次结帐时间
            updateLastSettDate = currentDate;

            if (null == lastSettlementDate) {
                //判断上一次结算时间，如果为空的话，则设置上一次结算时间为创建时间
                lastSettlementDate = account.getCreateDate();
            }

            //e)、获取两个时间相差天数
            int days = daysOfTwoDate(lastSettlementDate, currentDate);

            //f)、相差天数与固定结算时间取整，即判断几个周期未结算
            int tmpCountRemittanced = days / tmpRemittanced;
            if (tmpCountRemittanced >= 1) {
                for (int j = 1; j <= tmpCountRemittanced; j++) {
                    //g)、获取上次结算时间加上固定结算时间j个周期之后的时间
                    Date tmpLastSettlementDate = addDays(lastSettlementDate, tmpRemittanced * j);

                    //h)、获取指定时间段内的订单数据
                    purchaseList = purchaseDao.findPurchaseFixedTime(accountId, tmpLastSettlementDate, remittanced);

                    //i)、设置结算明细、结算明细与订单关联关系、待更新的订单、待更新的账户的值，并添加到对应的list
                    setValues(purchaseList, account, currentDate, orderMoneyRecordList, recordPurchaseList,
                            purchaseUpdateList, tmpLastSettlementDate);
                }
            }
        }

        //2、设置更新供应商账户数据，并添加到对应的列表
        Account updateAccount = new Account();
        updateAccount.setAccountId(accountId);
        //上一次结帐日期
        updateAccount.setLastSettlementDate(updateLastSettDate);
        updateAccount.setUpdateDate(currentDate);
        accountUpdateList.add(updateAccount);
    }


    /**
     * 设置结算明细及结算明细与订单关联、订单更新数据
     * @param purchaseList
     * @param account
     * @param currentDate
     * @param orderMoneyRecordList
     * @param recordPurchaseList
     * @param purchaseUpdateList
     */
    public void setValues(List<Purchase> purchaseList, Account account, Date currentDate, List<OrderMoneyRecord> orderMoneyRecordList,
                          List<RecordPurchase> recordPurchaseList, List<Purchase> purchaseUpdateList, Date checkOutDate) {
        if (null == purchaseList || purchaseList.isEmpty()) {
            return;
        }

        //生成结算明细id
        String recordId = NumberGenerate.generateId();

        //设置结算明细与订单关联数据、订单更新数据，并添加到对应的list中
        Map<String, BigDecimal> tmpMap = setRecordPurchaseValues(purchaseList, recordPurchaseList, purchaseUpdateList, recordId);

        //设置该商户下结算明细的值，并添加到结算明细的orderMoneyRecordList
        setOMRValues(recordId, account, tmpMap, currentDate, orderMoneyRecordList, checkOutDate);

    }


    /**
     * 设置结算明细数据，并添加到orderMoneyRecordList
     * @param recordId 结算明细id
     * @param account  供应商账户
     * @param map 订单总金额、运费总金额 键值对
     * @param currentDate 当前时间
     * @return
     */
    public void setOMRValues(String recordId, Account account, Map<String, BigDecimal> map, Date currentDate,
                             List<OrderMoneyRecord> orderMoneyRecordList, Date checkOutDate) {

        BigDecimal tmpOrderSettlemePrice = map.get("orderSettlemePrice");
        BigDecimal tmpOrderPostage = map.get("orderPostage");
        OrderMoneyRecord omr = new OrderMoneyRecord();
        omr.setRecordId(recordId);
        // 账户id
        omr.setUserId(account.getAccountId());
        // 开户行
        omr.setBankName(account.getBankName());
        // 开户支行
        omr.setBankNameBranch("");
        // 银行卡号
        omr.setBankAccount(account.getBankNum());
        // 待结算金额
        omr.setTotalMoney(tmpOrderSettlemePrice.add(tmpOrderPostage));
        // 结算状态
        omr.setState("0");
        // 创建时间
        omr.setCreatedAt(currentDate);
        // 更新时间
        omr.setUpdatedAt(currentDate);
        //流水号
        omr.setSerialNumber("");
        //供应商名称
        omr.setProviderName(account.getProviderName());
        //已结算金额
        omr.setSettledAmount(new BigDecimal("0.00"));
        //开户人姓名
        omr.setBankUserName(account.getBankUserName());
        //结账时间
        omr.setCheckoutAt(checkOutDate);
        //运费总金额
        omr.setPostageTotalAmount(tmpOrderPostage);
        //订单成本金额
        omr.setOrderTotalAmount(tmpOrderSettlemePrice);
        orderMoneyRecordList.add(omr);
    }

    /**
     * 设置结算明细与订单关联表数据，以及要更新的订单数据
     * @param purchaseList
     * @param recordPurchaseList
     * @param purchaseUpdateList
     * @param recordId
     * @return 返回待结算金额
     */
    public Map<String, BigDecimal> setRecordPurchaseValues(List<Purchase> purchaseList, List<RecordPurchase> recordPurchaseList,
                                        List<Purchase> purchaseUpdateList, String recordId) {
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal tmpOrderSettlemePrice = new BigDecimal("0.00");
        BigDecimal tmpOrderPostage = new BigDecimal("0.00");
        for (Purchase purchase : purchaseList) {
            RecordPurchase recordPurchase = new RecordPurchase();
            recordPurchase.setRecordId(recordId);
            recordPurchase.setOrderId(purchase.getOrderId());
            //结算明细与订单关联数据添加到recordPurchaseList
            recordPurchaseList.add(recordPurchase);

            Integer orderStatus = purchase.getOrderStatus();
            if (Ognl.isNotNull(orderStatus) && orderStatus.equals(Constant.OrderStatusConfig.RECEIVED)) {
                //订单状态为 4已完成 的结算成本金额
                BigDecimal orderSettlemePrice = purchase.getOrderSettlemePrice();
                if (null == orderSettlemePrice) {
                    orderSettlemePrice = new BigDecimal("0.00");
                }
                //统计purchaseList订单列表的成本金额
                tmpOrderSettlemePrice = tmpOrderSettlemePrice.add(orderSettlemePrice);
            }

            //运费
            BigDecimal orderPostage = purchase.getOrderPostage();
            if (null == orderPostage) {
                orderPostage = new BigDecimal("0.00");
            }
            //统计purchaseList订单列表的运费金额
            tmpOrderPostage = tmpOrderPostage.add(orderPostage);

            purchase.setUpdatedAt(new Date());
            //订单更新数据添加到purchaseUpdateList
            purchaseUpdateList.add(purchase);
        }
        map.put("orderSettlemePrice", tmpOrderSettlemePrice);
        map.put("orderPostage", tmpOrderPostage);
        return map;
    }


    /**
     * 获取两个时间之间相差几个月
     * @param lastDate  上次结算时间
     * @param currentDate   当前时间
     * @return
     * @throws ParseException
     */
    public static int countMonths(Date lastDate, Date currentDate) throws ParseException {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(lastDate);
        c2.setTime(currentDate);
        int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        //开始日期若小月结束日期
        if(year < 0){
            year = -year;
            return year*12 + c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        }
        return year*12 + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
    }

    /**
     * 获取指定时间，加上n个月之后的时间
     * @param date 当前时间
     * @param n
     * @return
     */
    public static Date addMonths(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, n);
        return calendar.getTime();
    }

    /**
     * 判断两个时间相差天数
     *   例如：2017-09-06 23:59:59 和 2017-09-07 00:01:00 相差1天
     * @param fDate 起始时间
     * @param oDate 结束时间
     * @return
     */
    public static int daysOfTwoDate(Date fDate, Date oDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
    }

    /**
     * 获取指定时间加上固定天数之后的时间
     * @param curDate
     * @param num
     * @return
     * @throws ParseException
     */
    public static Date addDays(Date curDate, int num) throws ParseException {
        Calendar ca = Calendar.getInstance();
        ca.setTime(curDate);
        ca.add(Calendar.DATE, num);// num为增加的天数
        Date endDate = ca.getTime();
        return endDate;
    }

    /**
     * 把指定时间设置为这个月的1号
     *  如：2017-08-05 00:00:00  -> 2017-08-01 00:00:00
     * @param date 指定时间
     * @return
     */
    public static Date firstDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }


    /**
     * 查询结算明细列表
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param input    入参
     * @return
     */
    @Override
    public OrderMoneyRecordOutput searchOrderMoneyRecords(Integer pageNum, Integer pageSize,
                                                          OrderMoneyRecordInput input) throws Exception {

		/*
         * 1.创建返回的对象output；
		 * 2.分页
		 *   a).使用PageTool工具类开启分页；
		 * 3.根据入参条件，查询结算明细列表；
		 * 4.取得OrderMoneyRecord的分页信息；
		 * 5.对象转换为OrderMoneyRecordVo；
		 * 6.返回结果对象output；
		 */

        //1.创建返回的对象output
        OrderMoneyRecordOutput output = null;

        //2.分页
        //a).使用PageTool工具类开启分页
        PageTool.startPage(pageNum, pageSize);

        //3.根据入参条件，查询结算明细列表
        List<OrderMoneyRecord> list = orderMoneyRecordDao.findPageByState(input);
        if (null != list && !list.isEmpty()) {

            output = new OrderMoneyRecordOutput();

            //4.取得OrderMoneyRecord的分页信息
            PageInfo pageInfo = new PageInfo<>(list);

            //5.对象转换为OrderMoneyRecordVo
            List<OrderMoneyRecordVo> returnList = convertOrderMoneyRecordVo(list);

            pageInfo.setList(returnList);

            output.setPageInfo(pageInfo);
        }

        //6.返回结果对象output
        return output;
    }

    /**
     * 结算
     *
     * @param recordId 结算明细id
     * @param serialNumber  流水号
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderMoneyRecordState(String recordId, String serialNumber) throws Exception {

        /*
         * 1.查找该recordId对应的OrderMoneyRecord实体
         *   a)如果实体为null，则返回false；
         * 2.根据待结算金额，设置结算明细中要更新的属性值，更新结算明细数据
         *   b)获取待结算金额；
         *   c)设置要更新的属性值；
         *   d)更新结算明细数据；
         * 3.获取商户id，设置账户更新属性值，更新账户(Account)数据
         *   e)设置Account中要更新的属性值；
         *   f)更新账户(Account)数据；
         * 4.更新结算明细金额对应订单中的账户状态、更新时间；
         * 5.如果结算明细表、账户表、订单表同时更新成功，则设置flag为true；
         */
        boolean flag = false;

        //1.查找该recordId对应的OrderMoneyRecord实体
        OrderMoneyRecord orderMoneyRecord = orderMoneyRecordDao.findOne(recordId);

        //a)如果实体为null，则返回false
        if (null == orderMoneyRecord) {
            return flag;
        }

        //2.根据待结算金额，设置结算明细中要更新的属性值，更新结算明细数据
        //b)获取待结算金额
        BigDecimal tmpTotalAmount = orderMoneyRecord.getTotalMoney();

        //c)设置要更新的属性值
        orderMoneyRecord.setRecordId(recordId);//结算明细id
        orderMoneyRecord.setUpdatedAt(new Date());//更新时间
        orderMoneyRecord.setSettledAt(new Date());//结算时间
        orderMoneyRecord.setTotalMoney(new BigDecimal("0.00"));//待结算金额
        orderMoneyRecord.setSettledAmount(tmpTotalAmount);//已结算金额
        orderMoneyRecord.setSerialNumber(serialNumber);//流水号

        //d)更新结算明细数据
        int modifyRecordNum = orderMoneyRecordDao.updateOrderMoneyRecord(orderMoneyRecord);

        //3.获取账户id，设置账户更新属性值，更新账户(Account)数据
        //e)设置Account中要更新的属性值
        Long accountId = orderMoneyRecord.getUserId();//账户id
        Account account = new Account();
        account.setBalance(new BigDecimal("0.00"));
        account.setUpdateDate(new Date());
        account.setAccountId(accountId);

        //f)更新账户(Account)数据
        int modifyAccountNum = accountDao.updateAccountByUserId(account);

        //4.更新结算明细金额对应订单中的账户状态、更新时间
        Date updateDate = new Date();
        int modifyPurchaseNum = purchaseDao.updateAccountStatus(recordId, updateDate);

        //5.如果结算明细表、账户表、订单表同时更新成功，则设置flag为true；
        if (modifyRecordNum > 0 && modifyAccountNum > 0 && modifyPurchaseNum > 0) {
            flag = true;
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 失败回滚
        }
        return flag;
    }

    /**
     * 结算明细对象转换为OrderMoneyRecordVo对象
     *
     * @param list 要转换的list
     * @return
     */
    private List<OrderMoneyRecordVo> convertOrderMoneyRecordVo(List<OrderMoneyRecord> list) throws Exception {
        //新建要返回的List<OrderMoneyRecordVo>
        List<OrderMoneyRecordVo> tmpList = new ArrayList<>();
        if (null != list && !list.isEmpty()) {
            for (OrderMoneyRecord omr : list) {
                OrderMoneyRecordVo ormVo = new OrderMoneyRecordVo();
                ormVo.setRecordId(omr.getRecordId());//结算明细id
                ormVo.setProviderName(omr.getProviderName());//供应商名称
                ormVo.setCheckoutAt(omr.getCheckoutAt() == null ? "" : StringUtil.fomateData(omr.getCheckoutAt(), "yyyy-MM-dd"));//结账时间
                ormVo.setTotalMoney(NumberUtil.number2Thousand(omr.getTotalMoney()));//待结算金额
                ormVo.setSettledAmount(NumberUtil.number2Thousand(omr.getSettledAmount()));//已结算金额
                ormVo.setSettledAt(omr.getSettledAt() == null ? "" : StringUtil.fomateData(omr.getSettledAt(), "yyyy-MM-dd HH:mm"));//结算时间
                ormVo.setState(omr.getState());//结算状态
                ormVo.setBankAccount(omr.getBankAccount());//供应商账户
                ormVo.setSerialNumber(omr.getSerialNumber());//银行流水号
                ormVo.setPostageTotalAmount(NumberUtil.number2Thousand(omr.getPostageTotalAmount()));//运费总金额
                ormVo.setOrderTotalAmount(NumberUtil.number2Thousand(omr.getOrderTotalAmount()));//订单结算总金额
                tmpList.add(ormVo);
            }
        }
        //返回转换之后的list
        return tmpList;
    }


    /**
     * 结算明细对象转换为OrderMoneyRecordVo对象
     *
     * @param list 要转换的list
     * @return
     */
    private List<AccountOrderMoneyRecordVO> transformOrderMoneyRecordVo(List<OrderMoneyRecord> list) throws Exception {
        //新建要返回的List<OrderMoneyRecordVo>
        List<AccountOrderMoneyRecordVO> tmpList = new ArrayList<>();
        if (null != list && !list.isEmpty()) {
            for (OrderMoneyRecord omr : list) {
                AccountOrderMoneyRecordVO aormVo = new AccountOrderMoneyRecordVO();
                aormVo.setRecordId(omr.getRecordId());//结算明细id
                aormVo.setBankUserName(omr.getBankUserName()); //开户人姓名
                aormVo.setBankName(omr.getBankName());  //开户行
                aormVo.setBankNameBranch(omr.getBankNameBranch());  //开户支行
                aormVo.setSerialNumber(omr.getSerialNumber());  //银行流水号
                aormVo.setCheckoutAt(omr.getCheckoutAt() == null ? null : StringUtil.fomateData(omr.getCheckoutAt(), "yyyy-MM-dd"));//结账时间
                aormVo.setTotalMoney(NumberUtil.number2Thousand(omr.getTotalMoney()));//待结算金额
                aormVo.setSettledAmount(NumberUtil.number2Thousand(omr.getSettledAmount()));//已结算金额
                aormVo.setSettledAt(omr.getSettledAt() == null ? null : StringUtil.fomateData(omr.getSettledAt(), "yyyy-MM-dd HH:mm"));//结算时间
                aormVo.setBankAccount(omr.getBankAccount());//银行卡号
                aormVo.setPostageTotalAmount(NumberUtil.number2Thousand(omr.getPostageTotalAmount()));//运费总金额
                aormVo.setOrderTotalAmount(NumberUtil.number2Thousand(omr.getOrderTotalAmount()));//订单总金额
                tmpList.add(aormVo);
            }
        }
        //返回转换之后的list
        return tmpList;
    }

    /**
     * 根据根据申请人账户ID查询满足结算时间条件的已结算/待结算明细，并根据pageNum和pageSize进行分页
     *
     * @param accountId 申请人ID
     * @param put       多条件查询入参
     * @param pageNum   当前页数
     * @param pageSize  每页条数
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> searchRecords(Long accountId, OrderMoneyRecordRankInput put, Integer pageNum, Integer pageSize) throws Exception {
        /*
            1.确认排序字段及排序次序
            2.判断put中的状态码,若状态码为1,则进行已结算明细查询,若状态码为0,则进行待结算明细查询
              1).计算该用户的已结算/待结算总金额
              2).根据申请人ID查询满足结算时间条件的已结算/待结算明细,若时间为null,则查询全部明细
            3.若统计金额为null或为负，给其赋值0.00,正确数据则进行格式化
            4.判断List是否为null及是否为空,若不为null和空，进行转换封装,若List为null或为空,PageInfo为null
              a.获取OrderMoneyRecord分页的PageInfo<OrderMoneyRecord>
              b.将orderMoneyRecordList转为orderMoneyRecordVoList
              c.生成OrderMoneyRecordVo分页的PageInfo<OrderMoneyRecordVo>
         */
        // 1.确认排序字段及排序次序
        //确认排序字段（0 结账时间; 1 待结算金额; 2 结算时间; 3 结算金额）
        switch (put.getSortName()) {
            case "0":
                put.setSortName("checkout_at"); //结账时间
                break;
            case "1":
                put.setSortName("total_money"); //待结算金额
                break;
            case "2":
                put.setSortName("settled_at");  //结算时间
                break;
            case "3":
                put.setSortName("settled_amount"); //结算金额
                break;
        }
        //确定排序次序（0 正序; 1 倒序）
        switch (put.getSortType()) {
            case "0":
                put.setSortType("ASC");
                break;
            case "1":
                put.setSortType("DESC");
                break;
        }
        // 2.判断put中的状态码,若状态码为1,则进行已结算明细查询,若状态码为0,则进行待结算明细查询
        BigDecimal money = null;     //已结算/待结算总金额
        List<OrderMoneyRecord> oList = null;  //已结算/待结算明细集合
        if ("1".equals(put.getState())) {
            // 计算该用户的已结算总金额
            money = orderMoneyRecordDao.findTotalSettledAmount(accountId);
            // 根据申请人ID查询满足结算时间条件的已结算明细,若时间为null,则查询全部已结算明细
            PageTool.startPage(pageNum, pageSize);
            oList = orderMoneyRecordDao.findSettledPage(accountId, put);
        }
        if ("0".equals(put.getState())) {
            // 计算该用户的待结算总金额
            money = orderMoneyRecordDao.findTotalUnpaidMoney(accountId);
            // 根据申请人ID查询满足结算时间条件的待结算明细,若时间为null,则查询全部待结算明细
            PageTool.startPage(pageNum, pageSize);
            oList = orderMoneyRecordDao.findUnpaidPage(accountId, put);
        }
        // 3.若统计金额为null给其赋值0.00,正确数据则进行格式化
        String moneyFormat = NumberUtil.number2Thousand(money);
        // 定义map存放data
        Map<String, Object> map = new HashMap();
        map.put("money", moneyFormat);
        // 4.判断List是否为null及是否为空,若不为null和空，进行转换封装,若List为null或为空,PageInfo为null
        PageInfo pageInfo = null;
        if (Ognl.CollectionIsNotEmpty(oList)) {
            // a.获取分页的PageInfo
            pageInfo = new PageInfo<>(oList);
            // b.将orderMoneyRecordList转为orderMoneyRecordVoList
            List<AccountOrderMoneyRecordVO> voList = transformOrderMoneyRecordVo(oList);
            // c.生成Vo分页的PageInfo
            pageInfo.setList(voList);
        }
        map.put("pageInfo", pageInfo);
        return map;
    }

    /**
     * 根据结算明细id、订单id查询该明细所对应的订单列表，并根据pageNum和pageSize进行分页展示
     *
     * @param recordId 结算明细id
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param orderId  订单id
     * @return
     */
    @Override
    public RecordToPurchaseOutput searchOMRPurchaseDetails(String recordId, Integer pageNum, Integer pageSize, String orderId) {

        /*
		 * 1.创建返回的对象output；
		 * 2.分页
		 *   a).使用PageTool工具类开启分页；
		 * 3.根据条件，查询结算明细所对应的订单列表信息；
		 * 4.结算明细列表页面总计金额；
		 * 5.根据结算明细id，查询该结算明细实体，根据结算状态获取待结算金额或结算金额；
		 * 6.取得AccountPurchaseVo的分页信息；
		 * 7.返回结果对象output；
		 */

        //1.创建返回的对象output
        RecordToPurchaseOutput output = new RecordToPurchaseOutput();

        //2.分页
        //a).使用PageTool工具类开启分页
        PageTool.startPage(pageNum, pageSize);

        //3.根据条件，查询结算明细所对应的订单列表信息
        List<AccountPurchaseVo> purchaseList = orderMoneyRecordDao.findPageOMRPurchase(recordId, orderId);

        //4.结算明细列表页面总计金额
        BigDecimal tmpTotalOrderTotalPrice = orderMoneyRecordDao.countOrderTotalPrice(recordId);

        //5.根据结算明细id，查询该结算明细实体，根据结算状态获取待结算金额或结算金额
        BigDecimal tmpTotalOrderRevenue = new BigDecimal("0.00");
        OrderMoneyRecord orderMoneyRecord = orderMoneyRecordDao.findOne(recordId);
        if (null != orderMoneyRecord) {
            String state = orderMoneyRecord.getState();
            if ("0".equals(state)) {
                tmpTotalOrderRevenue = orderMoneyRecord.getTotalMoney();
            } else if ("1".equals(state)) {
                tmpTotalOrderRevenue = orderMoneyRecord.getSettledAmount();
            }
        }

        PageInfo pageInfo = null;
        if (null != purchaseList && !purchaseList.isEmpty()) {
            //6.取得Purchase的分页信息
            pageInfo = new PageInfo<>(purchaseList);
        }

        output.setPageInfo(pageInfo);
        //订单结算总额
        output.setTotalOrderRevenue(NumberUtil.number2Thousand(tmpTotalOrderRevenue));
        //订单运费总额
        output.setTotalOrderTotalPrice(NumberUtil.number2Thousand(tmpTotalOrderTotalPrice));

        //7.返回结果对象output
        return output;
    }

    /**
     * 财务管理-供应商结算-已结算
     *
     * @param startTime 开始时间
     * @param endTime   解释时间
     * @return 已结算金额
     */
    @Override
    public String settlementMoney(String startTime, String endTime) {
        //根据条件，查询已结算的金额
        BigDecimal settlemenMoney = orderMoneyRecordDao.settledAmount(startTime, endTime);
        //将金额格式转化为千分位
        return NumberUtil.number2Thousand(settlemenMoney);
    }

    /**
     * 财务管理-供应商结算-未结算
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 未结算金额
     */
    @Override
    public String unsettled(String startTime, String endTime) {
        //根据条件，查询未结算的金额
        BigDecimal unsettled = orderMoneyRecordDao.totalMoney(startTime, endTime);
        //将金额格式转化为千分位
        return NumberUtil.number2Thousand(unsettled);
    }

    /**
     * 结算明细列表 导出excel
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {

        XSSFWorkbook workbook = null;
        try {

            String pageNum = request.getParameter("pageNum");
            String pageSize = request.getParameter("pageSize");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            String state = request.getParameter("state");

            logger.debug("【startTime 】" + startTime);
            logger.debug("【endTime】" + endTime);

            logger.debug("【pageNum 】" + pageNum);
            logger.debug("【pageSize】" + pageSize);
            logger.debug("【state 】" + state);
            //验证分页参数合法性
            String limits = checkPageNumber(pageNum, pageSize);


            if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime) || StringUtils.isEmpty(state)) {
                throw new Exception();
            }

            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startTime);
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
            //验证：起始日期不能大于大于终止日期
            if (!endDate.after(startDate)) {
                throw new Exception();
            }

            //整理传参数据
            int year = Integer.valueOf(startTime.split("-")[0]).intValue();
            int month = Integer.valueOf(startTime.split("-")[1]).intValue();


            int nextMonth = month == 12 ? 1 : month + 1;
            int nextYear = month == 12 ? year + 1 : year;

            startTime = new String(year + "-" + month + "-01");
            endTime = new String(nextYear + "-" + nextMonth + "-01");
            logger.debug("【startTime 】" + startTime);
            logger.debug("【endTime】" + endTime);

            //设置响应参数
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            String filename = "结算明细"+ StringUtil.fomateData(new Date(), "yyyyMMddHHmmss") +".xlsx";
            filename = new String(filename.getBytes("Utf-8"), "iso-8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/octet-stream");
            ServletOutputStream out = response.getOutputStream();
            //根据条件查询数据
            List<OrderMoneyRecord> records = orderMoneyRecordDao.findRecords(startTime, endTime, state, limits);
            List<Object[]> data = new ArrayList<>();
            String[] titles = {"供应商名称", "结账时间", "待结算金额（¥）", "已结算金额（¥）", "结算时间", "结算状态", "供应商账户", "银行流水号"};
            for (int i = 0; i < records.size(); i++) {//转换数据格式
                Object[] o = OrderMoneyRecord.converData(records.get(i));
                logger.debug("【数据 为】 ： " + Arrays.toString(o));
                data.add(o);
            }
            //生成excel文件
            logger.debug("【excel 输出中。。】 ： ");
            workbook = ExcelExportHelper.exportExcel(data, titles);
//          输出excel
            workbook.write(out);
            logger.debug("【excel 输出中完毕】 ： ");
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

    /**
     * 供应商订单金额统计
     * @param timeType 统计时间类型--时间类型（1.本周，2.当月，3.近三个月）
     * @return
     */
    @Override
    public Map<String, Object> countOrderMoneyRecords(Integer timeType,Long accountId) {
        Map<String,String> timeMap = this.getelectTime(timeType);
        String startTime = timeMap.get("startTime");
        String endTime = timeMap.get("endTime");
        Map<Object, Object> dataMap = orderMoneyRecordDao.countOrderMoneyRecords(startTime,endTime,accountId);
        Map<String,Object> resultMap =  this.transformOfMap(dataMap);
        resultMap.put("orderTotalMoney",NumberUtil.number2Thousand(purchaseDao.getTotalMoneyByAccountId(startTime,endTime,accountId)));
        return resultMap;
    }

    /**
     * 根据时间类型获取具体的检索时间段
     * @param timeType
     * @return
     */
    private Map<String,String> getelectTime(Integer timeType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,String> timeMap = new HashMap<>();
        Date date = new Date();
        switch (timeType){
            case 1:
                timeMap.put("startTime",sdf.format(DateUtil.getTimesWeekmorning()));//本周第一天
                timeMap.put("endTime",sdf.format(DateUtil.getTimesWeeknight()));//本周最后一天
                break;
            case 2:
                timeMap.put("startTime",sdf.format(DateUtil.firstDayOfMonth(date)));//获取指定时间所在月的第一天
                timeMap.put("endTime",sdf.format(DateUtil.lastDayOfMonth(date)));//获取指定时间所在月的最后一天
                break;
            case 3:
                timeMap.put("startTime",sdf.format(DateUtil.subtractMonths(date,3)));//3个月前的第一天
                timeMap.put("endTime",sdf.format(date));//目前日期
                break;
        }
        return timeMap;
    }

    /**
     * map中key值和value转化
     *  key:结算状态对应的字符
     *  value:结算状态对应的统计数据
     * @param dataMap
     * @return
     */
    private Map<String,Object> transformOfMap(Map<Object, Object> dataMap) {
        Map<String ,Object> resultMap = new HashMap<>();//返回参数
        Map<String,Object> valueMap = (Map<String,Object>)dataMap.get("1");//已结算
        BigDecimal settledMoney = this.getValueByMap(valueMap,"settledMoney");//已结算金额
        resultMap.put("settledMoney",NumberUtil.number2Thousand(settledMoney));

        valueMap = (Map<String,Object>)dataMap.get("0");//未结算
        BigDecimal pendingSettlementMoney = this.getValueByMap(valueMap,"pendingSettlementMoney");//未结算
        // 金额
        resultMap.put("pendingSettlementMoney",NumberUtil.number2Thousand(pendingSettlementMoney));
        return resultMap;
    }

    /**
     * 根据字符串获取相对应的value值
     * @param valueMap
     * @param str
     * @return
     */
    private BigDecimal getValueByMap(Map<String, Object> valueMap, String str) {
        BigDecimal money = new BigDecimal(0);
        if (null == valueMap || valueMap.isEmpty() || null == valueMap.get(str)){//若该供应商没有已结算的统计数据
            return money;
        }else {
            return (BigDecimal)valueMap.get(str);
        }
    }

    /**
     * 验证分页参数合法性
     * @param pageNum
     * @param pageSize
     * @return
     * @throws Exception
     */
    private String checkPageNumber(String pageNum, String pageSize) throws Exception {

        if(StringUtils.isEmpty(pageNum)){
            return null;
        }
        //默认前10条
        if(StringUtils.isEmpty(pageSize)){
            pageSize = "10";
        }

        int startPage;
        int endPage;//数据合法性验证

        if (pageNum.split(",").length > 1) {
            startPage = Integer.valueOf(pageNum.split(",")[0]).intValue();
            endPage = Integer.valueOf(pageNum.split(",")[1]).intValue();
            //参数不合法，抛出异常
            if (startPage < 1 || endPage < 1 || (endPage - startPage) < 0) {
                throw new Exception();
            }
        } else {
            startPage = Integer.valueOf(pageNum).intValue();
            endPage = Integer.valueOf(pageNum).intValue();
        }
        int pageRange = endPage - startPage + 1;//页数范围
        int offset = (startPage - 1) * Integer.valueOf(pageSize).intValue();//偏移量
        int limit = pageRange * Integer.valueOf(pageSize).intValue();//总条数
        return offset + "," + limit;
    }

    /**
     * 导出结算明细对应的订单列表
     * @param recordId  结算明细id
     * @param orderId   订单id
     * @param pageNum   页码
     * @param pageSize  每页条数
     * @param response
     * @throws Exception
     */
    @Override
    public void exportRecordToPurchasesExcel(String recordId, String orderId, String pageNum, String pageSize, HttpServletResponse response) throws Exception {
        XSSFWorkbook workbook = null;
        try {

            //判断recordId不为空
            if (Ognl.isEmpty(recordId)) {
                return ;
            }

            //验证分页参数合法性
            String limits = checkPageNumber(pageNum, pageSize);

            //设置响应参数
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            String filename = "账单明细"+ StringUtil.fomateData(new Date(), "yyyyMMddHHmmss") +".xlsx";
            filename = new String(filename.getBytes("Utf-8"), "iso-8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/octet-stream");
            ServletOutputStream out = response.getOutputStream();
            //根据条件查询数据
            List<AccountPurchaseVo> recordToPurchase = orderMoneyRecordDao.findPurchasesByRecordId(recordId, orderId, limits);
            List<Object[]> data = new ArrayList<>();
            String[] titles = {"订单编号", "订单状态", "商品金额小计（¥）", "运费金额（¥）", "折扣优惠（¥）", "总计金额（¥）", "实付金额（¥）",
                               "透云进货价小计（¥）", "结算金额（¥）", "付款时间", "支付类型", "付款流水号"};
            if (null != recordToPurchase && !recordToPurchase.isEmpty()) {
                for (int i = 0; i < recordToPurchase.size(); i++) {//转换数据格式
                    Object[] o = AccountPurchaseVo.converData(recordToPurchase.get(i));
                    data.add(o);
                }
            }
            //生成excel文件
            workbook = ExcelExportHelper.exportExcel(data, titles);
            //输出excel
            workbook.write(out);
        } finally {
            if (workbook != null) {
                workbook.close();
            }
        }
    }

}
