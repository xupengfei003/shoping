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
import so.sao.shop.supplier.pojo.input.OrderMoneyRecordRankInput;
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
     * 保存结算明细、结算明细与订单关联关系、更新Account表中的上一次结算时间字段
     *
     * @param accountList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderMoneyRecord(List<Account> accountList) throws Exception {

		/*
         * 1、判断accountList，不为空的话则循环列表
         *   a).结算明细的list
         *   b).结算明细与订单关联关系list
         *   c).更新的accountList
         *   d).获取账户id
         *   e).设置Account表中需要更新账户表中的字段
         *   f).查询该商户下已完成的且在指定结算方式内的订单
         *   g).循环订单id,设置结算明细与订单关联关系的值，并添加到结算明细与订单关联的recordPurchaseList
         *   h).设置该商户下结算明细的值，并添加到结算明细的orderMoneyRecordList
         * 2、批量更新Account表中的上一次结算时间字段
		 * 3、批量保存结算明细记录
		 * 4、批量保存结算明细与订单关联关系
		 */

        //1、判断accountList，不为空的话则循环列表
        if (null != accountList && !accountList.isEmpty()) {
            //a).结算明细的list
            List<OrderMoneyRecord> orderMoneyRecordList = new ArrayList<>();

            //b).结算明细与订单关联关系list
            List<RecordPurchase> recordPurchaseList = new ArrayList<>();

            //c).更新的accountList
            List<Account> accountUpdateList = new ArrayList<>();

            for (Account account : accountList) {
                String remittanceType = account.getRemittanceType();//结算方式

                //d).获取账户id
                Long accountId = account.getAccountId();

                Date currentDate = new Date();

                //e).设置Account表中需要更新账户表中的字段
                Account updateAccount = new Account();
                updateAccount.setAccountId(accountId);
                updateAccount.setLastSettlementDate(currentDate);//上一次结帐日期
                accountUpdateList.add(updateAccount);

                //f).查询该商户下已完成的且在指定结算方式内的订单
                List<Purchase> purchaseList = null;
                if (Ognl.isNotEmpty(remittanceType) && "1".equals(remittanceType)) {
                    //按自然月结算
                    purchaseList = purchaseDao.findPurchaseMonth(accountId, currentDate);

                } else if (Ognl.isNotEmpty(remittanceType) && "2".equals(remittanceType)) {
                    String remittanced = account.getRemittanced();//固定时间天数
                    Date lastSettlementDate = account.getLastSettlementDate();//上一次结算时间

                    //判断上一次结算时间，如果为空的话，则设置上一次结算时间为创建时间
                    if (null == lastSettlementDate) {
                        lastSettlementDate = account.getCreateDate();
                    }

                    //判断固定结算时间，如果为空，则执行下一次循环
                    if (Ognl.isEmpty(remittanced)) {
                        continue;
                    }

                    //按固定时间结算
                    purchaseList = purchaseDao.findPurchaseFixedTime(accountId, lastSettlementDate, remittanced);
                }

                if (null == purchaseList || purchaseList.isEmpty()) {
                    continue;
                }

                String recordId = NumberGenerate.generateId();//生成结算明细id

                //g).循环订单id,设置结算明细与订单关联关系的值，并添加到结算明细与订单关联的recordPurchaseList
                BigDecimal tmpOrderSettlemePrice = new BigDecimal("0.00");

                for (Purchase purchase : purchaseList) {
                    RecordPurchase recordPurchase = new RecordPurchase();
                    recordPurchase.setRecordId(recordId);
                    recordPurchase.setOrderId(purchase.getOrderId());
                    recordPurchaseList.add(recordPurchase);

                    BigDecimal orderSettlemePrice = purchase.getOrderSettlemePrice();
                    if (null == orderSettlemePrice) {
                        orderSettlemePrice = new BigDecimal("0.00");
                    }
                    tmpOrderSettlemePrice = tmpOrderSettlemePrice.add(orderSettlemePrice);
                }

                //h).设置该商户下结算明细的值，并添加到结算明细的orderMoneyRecordList
                OrderMoneyRecord omr = new OrderMoneyRecord();
                omr.setRecordId(recordId);
                omr.setUserId(accountId);// 账户id
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
                omr.setCheckoutAt(currentDate);//结账时间
                orderMoneyRecordList.add(omr);

            }

            if (!accountUpdateList.isEmpty()) {
                //2、批量更新Account表中的上一次结算时间字段
                accountDao.updateAccountLastSettlementDate(accountUpdateList);
            }

            if (orderMoneyRecordList.isEmpty() || recordPurchaseList.isEmpty()) {
                return;
            }

            //3、批量保存结算明细记录
            orderMoneyRecordDao.saveOrderMoneyRecords(orderMoneyRecordList);

            //4、批量保存结算明细与订单关联关系
            recordPurchaseDao.saveRecordPurchases(recordPurchaseList);

        }
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
    public boolean updateOrderMoneyRecordState(String recordId, String state) throws Exception {

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
