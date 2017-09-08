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
import so.sao.shop.supplier.dao.AccountDao;
import so.sao.shop.supplier.dao.OrderMoneyRecordDao;
import so.sao.shop.supplier.dao.PurchaseDao;
import so.sao.shop.supplier.dao.RecordPurchaseDao;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.OrderMoneyRecord;
import so.sao.shop.supplier.domain.Purchase;
import so.sao.shop.supplier.domain.RecordPurchase;
import so.sao.shop.supplier.pojo.input.OrderMoneyRecordInput;
import so.sao.shop.supplier.pojo.output.OrderMoneyRecordOutput;
import so.sao.shop.supplier.pojo.output.RecordToPurchaseOutput;
import so.sao.shop.supplier.pojo.vo.AccountOrderMoneyRecordVO;
import so.sao.shop.supplier.pojo.vo.OrderMoneyRecordVo;
import so.sao.shop.supplier.pojo.vo.PurchaseVo;
import so.sao.shop.supplier.service.OrderMoneyRecordService;
import so.sao.shop.supplier.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
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
            updateLastSettDate = getFirstDayOfMonth();//设置上一次结帐时间为当前月1号

            //a)、获取当前时间与上次结算时间相差的月数
            int months = countMonths(lastSettlementDate, currentDate);
            if (months >= 1) {
                for (int i = 1; i <= months; i++) {
                    //b)、获取上次结算时间相差i个月之后的时间
                    Date tmpLastSettlementDate = addMonths(lastSettlementDate, i);

                    //c)、获取指定时间上一个月内的订单数据
                    purchaseList = purchaseDao.findPurchaseMonth(accountId, tmpLastSettlementDate);

                    Date checkOutDate = addDays(tmpLastSettlementDate, tmpRemittanced - 1);//结账时间

                    //d)、设置结算明细、结算明细与订单关联关系、待更新的订单、待更新的账户的值，并添加到对应的list
                    setValues(purchaseList, account, currentDate, orderMoneyRecordList, recordPurchaseList,
                            purchaseUpdateList, checkOutDate);
                }
            }
        } else if ("2".equals(remittanceType)) {
            //2)、按固定时间结算
            updateLastSettDate = currentDate;//设置上一次结帐时间

            if (null == lastSettlementDate) {
                //判断上一次结算时间，如果为空的话，则设置上一次结算时间为创建时间
                lastSettlementDate = account.getCreateDate();
            }

            //e)、获取两个时间相差天数
            int days = daysOfTwoDate(lastSettlementDate, currentDate);

            //f)、相差天数与固定结算时间取整，即判断几个周期未结算
            int tmpCountRemittanced = days / tmpRemittanced;
            if (tmpCountRemittanced >= 1) {
                for (int j = 1; j <= tmpCountRemittanced; tmpCountRemittanced++) {
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
        updateAccount.setLastSettlementDate(updateLastSettDate);//上一次结帐日期
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

        String recordId = NumberGenerate.generateId();//生成结算明细id
        BigDecimal tmpOrderSettlemePrice = new BigDecimal("0.00");

        //设置结算明细与订单关联数据、订单更新数据，并添加到对应的list中
        tmpOrderSettlemePrice = setRecordPurchaseValues(purchaseList, recordPurchaseList, purchaseUpdateList, recordId);

        //设置该商户下结算明细的值，并添加到结算明细的orderMoneyRecordList
        setOMRValues(recordId, account, tmpOrderSettlemePrice, currentDate, orderMoneyRecordList, checkOutDate);

    }


    /**
     * 设置结算明细数据，并添加到orderMoneyRecordList
     * @param recordId 结算明细id
     * @param account  供应商账户
     * @param tmpOrderSettlemePrice 待结算金额
     * @param currentDate 当前时间
     * @return
     */
    public void setOMRValues(String recordId, Account account, BigDecimal tmpOrderSettlemePrice, Date currentDate,
                             List<OrderMoneyRecord> orderMoneyRecordList, Date checkOutDate) {
        OrderMoneyRecord omr = new OrderMoneyRecord();
        omr.setRecordId(recordId);
        omr.setUserId(account.getAccountId());// 账户id
        omr.setBankName(account.getBankName());// 开户行
        omr.setBankNameBranch("");// 开户支行
        omr.setBankAccount(account.getBankNum());// 银行卡号
        omr.setTotalMoney(tmpOrderSettlemePrice);// 待结算金额
        omr.setState("0");// 状态
        omr.setCreatedAt(currentDate);// 创建时间
        omr.setUpdatedAt(currentDate);// 更新时间
        omr.setSerialNumber("");//流水号
        omr.setProviderName(account.getProviderName());//供应商名称
        omr.setSettledAmount(new BigDecimal("0.00"));//已结算金额
        omr.setBankUserName(account.getBankUserName());//开户人姓名
        omr.setCheckoutAt(checkOutDate);//结账时间
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
    public BigDecimal setRecordPurchaseValues(List<Purchase> purchaseList, List<RecordPurchase> recordPurchaseList,
                                        List<Purchase> purchaseUpdateList, String recordId) {
        BigDecimal tmpOrderSettlemePrice = new BigDecimal("0.00");
        for (Purchase purchase : purchaseList) {
            RecordPurchase recordPurchase = new RecordPurchase();
            recordPurchase.setRecordId(recordId);
            recordPurchase.setOrderId(purchase.getOrderId());
            recordPurchaseList.add(recordPurchase);//结算明细与订单关联数据添加到recordPurchaseList

            BigDecimal orderSettlemePrice = purchase.getOrderSettlemePrice();
            if (null == orderSettlemePrice) {
                orderSettlemePrice = new BigDecimal("0.00");
            }
            //统计purchaseList订单列表的结算金额之和
            tmpOrderSettlemePrice = tmpOrderSettlemePrice.add(orderSettlemePrice);

            purchase.setUpdatedAt(new Date());
            purchaseUpdateList.add(purchase);//订单更新数据添加到purchaseUpdateList
        }
        return tmpOrderSettlemePrice;
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
     * 获取当前时间所在月的第一天时间
     * @return
     * @throws ParseException
     */
    public static Date getFirstDayOfMonth() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1); //设为当前月的1号
        Date date = sdf.parse(sdf.format(lastDate.getTime()) + " 00:00:00");
        return date;
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
		 * 6.新建OrderMoneyRecordVo的分页信息PageInfo<OrderMoneyRecordVo>，并设置分页信息；
		 * 7.返回结果对象output；
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
            PageInfo<OrderMoneyRecord> pageInfo = new PageInfo<>(list);

            //5.对象转换为OrderMoneyRecordVo
            List<OrderMoneyRecordVo> returnList = convertOrderMoneyRecordVo(list);

            //6.新建OrderMoneyRecordVo的分页信息PageInfo<OrderMoneyRecordVo>，并设置分页信息
            PageInfo<OrderMoneyRecordVo> pageInfoVo = new PageInfo<>();
            pageInfoVo.setPageNum(pageNum);
            pageInfoVo.setPageSize(pageSize);
            pageInfoVo.setPages(pageInfo.getPages());
            pageInfoVo.setTotal(pageInfo.getTotal());
            pageInfoVo.setSize(pageInfo.getSize());
            pageInfoVo.setList(returnList);

            output.setPageInfo(pageInfoVo);
        }

        //7.返回结果对象output
        return output;
    }

    /**
     * 结算
     *
     * @param recordId 结算明细id
     * @param state    结算状态
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrderMoneyRecordState(String recordId, String state, String serialNumber) throws Exception {

        /*
         * 1.查找该recordId对应的OrderMoneyRecord实体
         *   a)如果实体为null，则返回false；
         * 2.判断传入的state是否为"1"
         *   b)如果等于1，则继续执行；
         * 3.根据待结算金额，设置结算明细中要更新的属性值，更新结算明细数据
         *   c)获取待结算金额；
         *   d)设置要更新的属性值；
         *   e)更新结算明细数据；
         * 4.获取商户id，设置账户更新属性值，更新账户(Account)数据
         *   f)设置Account中要更新的属性值；
         *   g)更新账户(Account)数据；
         * 5.更新结算明细金额对应订单中的账户状态、更新时间；
         * 6.如果结算明细表、账户表、订单表同时更新成功，则设置flag为true；
         */
        boolean flag = false;

        //1.查找该recordId对应的OrderMoneyRecord实体
        OrderMoneyRecord orderMoneyRecord = orderMoneyRecordDao.findOne(recordId);

        //a)如果实体为null，则返回false
        if (null == orderMoneyRecord) {
            return flag;
        }

        //2.判断传入的state是否为"1"
        //b)如果等于1，则继续执行
        if ("1".equals(state)) {
            //3.根据待结算金额，设置结算明细中要更新的属性值，更新结算明细数据
            //c)获取待结算金额
            BigDecimal tmpTotalAmount = orderMoneyRecord.getTotalMoney();

            //d)设置要更新的属性值
            orderMoneyRecord.setRecordId(recordId);//结算明细id
            orderMoneyRecord.setState(state);//结算状态
            orderMoneyRecord.setUpdatedAt(new Date());//更新时间
            orderMoneyRecord.setSettledAt(new Date());//结算时间
            orderMoneyRecord.setTotalMoney(new BigDecimal("0.00"));//待结算金额
            orderMoneyRecord.setSettledAmount(tmpTotalAmount);//已结算金额
            orderMoneyRecord.setSerialNumber(serialNumber);//流水号

            //e)更新结算明细数据
            int modifyRecordNum = orderMoneyRecordDao.updateOrderMoneyRecord(orderMoneyRecord);

            //4.获取账户id，设置账户更新属性值，更新账户(Account)数据
            //f)设置Account中要更新的属性值
            Long accountId = orderMoneyRecord.getUserId();//账户id
            Account account = new Account();
            account.setBalance(new BigDecimal("0.00"));
            account.setUpdateDate(new Date());
            account.setAccountId(accountId);

            //g)更新账户(Account)数据
            int modifyAccountNum = accountDao.updateAccountByUserId(account);

            //5.更新结算明细金额对应订单中的账户状态、更新时间
            Date updateDate = new Date();
            int modifyPurchaseNum = purchaseDao.updateAccountStatus(recordId, updateDate);

            //6.如果结算明细表、账户表、订单表同时更新成功，则设置flag为true；
            if (modifyRecordNum > 0 && modifyAccountNum > 0 && modifyPurchaseNum > 0) {
                flag = true;
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); // 失败回滚
            }
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
                aormVo.setCheckoutAt(omr.getCheckoutAt() == null ? null : StringUtil.fomateData(omr.getCheckoutAt(), "yyyy-MM-dd"));//结账时间
                aormVo.setTotalMoney(NumberUtil.number2Thousand(omr.getTotalMoney()));//待结算金额
                aormVo.setSettledAmount(NumberUtil.number2Thousand(omr.getSettledAmount()));//已结算金额
                aormVo.setSettledAt(omr.getSettledAt() == null ? null : StringUtil.fomateData(omr.getSettledAt(), "yyyy-MM-dd HH:mm"));//结算时间
                aormVo.setBankAccount(omr.getBankAccount());//银行卡号
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
    public Map<String, Object> searchRecords(Long accountId, OrderMoneyRecordInput put, Integer pageNum, Integer pageSize) throws Exception {
        /*
            1.判断put中的状态码,若状态码为1,则进行已结算明细查询,若状态码为0,则进行待结算明细查询
              1).计算该用户的已结算/待结算总金额
              2).根据申请人ID查询满足结算时间条件的已结算/待结算明细,若时间为null,则查询全部明细
            2.若统计金额为null或为负，给其赋值0.00,正确数据则进行格式化
            3.判断List是否为null及是否为空,若不为null和空，进行转换封装,若List为null或为空,PageInfo为null
              a.获取OrderMoneyRecord分页的PageInfo<OrderMoneyRecord>
              b.将orderMoneyRecordList转为orderMoneyRecordVoList
              c.生成OrderMoneyRecordVo分页的PageInfo<OrderMoneyRecordVo>
         */
        // 1.判断put中的状态码,若状态码为1,则进行已结算明细查询,若状态码为0,则进行待结算明细查询
        BigDecimal money = null;     //已结算/待结算总金额
        List<OrderMoneyRecord> oList = null;  //已结算/待结算明细集合
        if ("1".equals(put.getState())) {
            // 计算该用户的已结算总金额
            money = orderMoneyRecordDao.findTotalSettledAmount(accountId);
            // 根据申请人ID查询满足结算时间条件的已结算明细,若时间为null,则查询全部已结算明细
            PageTool.startPage(pageNum, pageSize);
            oList = orderMoneyRecordDao.findSettledPage(accountId, put);
        } else if ("0".equals(put.getState())) {
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
        PageInfo<AccountOrderMoneyRecordVO> pageInfoVo = null;
        if (Ognl.CollectionIsNotEmpty(oList)) {
            // a.获取分页的PageInfo
            PageInfo<OrderMoneyRecord> pageInfo = new PageInfo<>(oList);
            // b.将orderMoneyRecordList转为orderMoneyRecordVoList
            List<AccountOrderMoneyRecordVO> voList = transformOrderMoneyRecordVo(oList);
            // c.生成Vo分页的PageInfo
            pageInfoVo = new PageInfo<>(voList);
            pageInfoVo.setPageNum(pageNum);             // 当前页
            pageInfoVo.setPageSize(pageSize);           // 每页条数
            pageInfoVo.setList(voList);                 // List<AccountOrderMoneyRecordVO>
            pageInfoVo.setTotal(pageInfo.getTotal());   // 总条数
            pageInfoVo.setPages(pageInfo.getPages());   // 总页数
            pageInfoVo.setSize(pageInfo.getSize());     // 当前页条数
        }
        map.put("pageInfo", pageInfoVo);
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
		 * 4.根据结算明细id，查询该结算明细id对应的所有订单的结算金额之和；
		 * 5.取得Purchase的分页信息；
		 * 6.对象转换为PurchaseVo；
		 * 7.新建PurchaseVo的分页信息PageInfo<PurchaseVo>，并设置分页信息；
		 * 8.返回结果对象output；
		 */

        //1.创建返回的对象output
        RecordToPurchaseOutput output = null;

        //2.分页
        //a).使用PageTool工具类开启分页
        PageTool.startPage(pageNum, pageSize);

        //3.根据条件，查询结算明细所对应的订单列表信息
        List<Purchase> purchaseList = orderMoneyRecordDao.findPageOMRPurchase(recordId, orderId);

        //4.根据结算明细id，查询该结算明细id对应的所有订单的结算金额之和
        BigDecimal tmpTotalOrderRevenue = orderMoneyRecordDao.findOMRPurchaseTotalSettlemePrice(recordId);

        if (null != purchaseList && !purchaseList.isEmpty()) {

            output = new RecordToPurchaseOutput();

            //5.取得Purchase的分页信息
            PageInfo<Purchase> pageInfo = new PageInfo<>(purchaseList);

            //6.对象转换为PurchaseVo
            List<PurchaseVo> purchaseVoList = convertPurchaseVo(purchaseList);

            //7.新建PurchaseVo的分页信息PageInfo<PurchaseVo>，并设置分页信息
            PageInfo<PurchaseVo> pageInfoVo = new PageInfo<>();
            pageInfoVo.setPageNum(pageNum);
            pageInfoVo.setPageSize(pageSize);
            pageInfoVo.setTotal(pageInfo.getTotal());
            pageInfoVo.setPages(pageInfo.getPages());
            pageInfoVo.setSize(pageInfo.getSize());
            pageInfoVo.setList(purchaseVoList);

            output.setPageInfo(pageInfoVo);

            //订单结算总额
            output.setTotalOrderRevenue(NumberUtil.number2Thousand(tmpTotalOrderRevenue));
        }

        //8.返回结果对象output
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
     * 订单对象转换成PurchasesVo
     *
     * @param list
     * @return
     */
    public List<PurchaseVo> convertPurchaseVo(List<Purchase> list) {
        //新建要返回的List<PurchaseVo>
        List<PurchaseVo> recordPurchaseVoList = new ArrayList<>();

        for (Purchase purchase : list) {
            PurchaseVo recordPurchaseVo = new PurchaseVo();
            recordPurchaseVo.setOrderId(purchase.getOrderId());//订单编号
            recordPurchaseVo.setOrderReceiverName(purchase.getOrderReceiverName());//收货人姓名
            recordPurchaseVo.setOrderReceiverMobile(purchase.getOrderReceiverMobile());//收货人电话
            recordPurchaseVo.setOrderPrice(NumberUtil.number2Thousand(purchase.getOrderPrice()));//订单金额
            recordPurchaseVo.setOrderSettlemePrice(NumberUtil.number2Thousand(purchase.getOrderSettlemePrice()));//结算金额
            recordPurchaseVo.setOrderCreateTime(purchase.getOrderCreateTime() == null ? "" : StringUtil.fomateData(purchase.getOrderCreateTime(), "yyyy-MM-dd HH:mm:ss"));//订单创建时间
            recordPurchaseVoList.add(recordPurchaseVo);
        }

        //返回转换之后的list
        return recordPurchaseVoList;
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


        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        FileInputStream fileInputStream = null;
        XSSFWorkbook workbook = null;
//        File excelFile = null;
        try {
            int startPage, endPage;
            String pageNum = request.getParameter("pageNo");
            String pageSize = request.getParameter("pageSize");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            String state = request.getParameter("state");

            logger.debug("【startTime 】" + startTime);
            logger.debug("【endTime】" + endTime);

            logger.debug("【pageNum 】" + pageNum);
            logger.debug("【pageSize】" + pageSize);
            logger.debug("【state 】" + state);


            //数据合法性验证
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
            int pageRange = endPage - startPage + 1;//页数范围
            int offset = (startPage - 1) * Integer.valueOf(pageSize).intValue();//偏移量
            int limit = pageRange * Integer.valueOf(pageSize).intValue();//总条数
            String limits = offset + "," + limit;//分页条件

            int nextMonth = month == 12 ? 1 : month + 1;
            int nextYear = month == 12 ? year + 1 : year;

            startTime = new String(year + "-" + month + "-01");
            endTime = new String(nextYear + "-" + nextMonth + "-01");
            logger.debug("【startTime 】" + startTime);
            logger.debug("【endTime】" + endTime);

            //设置响应参数
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
//			String filename = "供应商结算明细.xlsx";
            String filename = "excel.xlsx";
            filename = new String(filename.getBytes("Utf-8"), "iso-8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/octet-stream");
            ServletOutputStream out = response.getOutputStream();
            //根据条件查询数据
            List<OrderMoneyRecord> records = orderMoneyRecordDao.findRecords(startTime, endTime, state, limits);
            List<Object[]> data = new ArrayList<>();
            String[] titles = {"供应商名称", "结账时间", "待结算金额（¥）", "已结算金额（¥）", "结算时间", "结算状态", "供应商账户"};
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
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            logger.debug("【excel 输出中完毕】 ： ");
        } finally {
            if (fileInputStream != null)
                fileInputStream.close();
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
            if (workbook != null) {
                workbook.close();
            }
        }
    }

}
