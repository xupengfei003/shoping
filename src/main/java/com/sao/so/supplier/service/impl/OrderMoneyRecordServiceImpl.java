package com.sao.so.supplier.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sao.so.supplier.config.Constant;
import com.sao.so.supplier.dao.AccountDao;
import com.sao.so.supplier.dao.OrderMoneyRecordDao;
import com.sao.so.supplier.dao.PurchaseDao;
import com.sao.so.supplier.domain.Account;
import com.sao.so.supplier.domain.OrderMoneyRecord;
import com.sao.so.supplier.pojo.output.OrderMoneyRecordOutput;
import com.sao.so.supplier.pojo.vo.OrderMoneyRecordVo;
import com.sao.so.supplier.service.OrderMoneyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by niewenchao on 2017/7/19.
 */
@Service
public class OrderMoneyRecordServiceImpl implements OrderMoneyRecordService {

    @Autowired
    private OrderMoneyRecordDao orderMoneyRecordDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private PurchaseDao purchaseDao;

    /**
     * 保存提现申请记录
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveOrderMoneyRecord(Long userId) {
        Map<String, Object> output = new HashMap<>();

        /**
         * 1.判断当前系统时间是否在提现日期内
         */
        Calendar cal = Calendar.getInstance();
        int currentNum = cal.get(Calendar.DAY_OF_MONTH);
        if(Constant.OMRTimePeriodConfig.START_DATE <= currentNum && currentNum <= Constant.OMRTimePeriodConfig.END_DATE){

        }else{
            output.put("status", Constant.OMRCodeConfig.OMR_CODE_NOT_TIME_PERIOD);
            return output;
        }

        /**
         * 2.判断提现金额 >= 最小提现金额
         */
        Account account = accountDao.findByUserId(userId);
        if(null == account){
            output.put("status", Constant.OMRCodeConfig.OMR_CODE_ACCOUNT_NOT_EXIST);
        } else {
            BigDecimal totalMoney = account.getBalance();
            BigDecimal minWithdraw = Constant.OMRMinWithdrawConfig.MIN_TOTAL_MONEY;
            if(null != minWithdraw && null != totalMoney) {
                if(totalMoney.compareTo(minWithdraw) == -1) {
                    output.put("status", Constant.OMRCodeConfig.OMR_CODE_MIN_WITHDRAW);
                    return output;
                }
            }

            /**
             * 获取入参中的值
             */
            String bankName = account.getBankName();
            String bankNameBranch = "";
            String bankAccount = account.getBankNum();
            String serialNumber = "";

            /**
             * 保存提现申请
             */
            OrderMoneyRecord omr = new OrderMoneyRecord();
            omr.setUserId(userId);
            omr.setBankName(bankName);
            omr.setBankNameBranch(bankNameBranch);
            omr.setBankAccount(bankAccount);
            omr.setTotalMoney(totalMoney);
            omr.setState("0");
            omr.setCreatedAt(System.currentTimeMillis());
            omr.setUpdatedAt(System.currentTimeMillis());
            omr.setSerialNumber(serialNumber);
            orderMoneyRecordDao.save(omr);
            Long recordId = omr.getRecordId();
            if(null != recordId) {
                output.put("status", Constant.CodeConfig.CODE_SUCCESS);
                output.put("recordId", recordId);
            } else {
                output.put("status", Constant.CodeConfig.CODE_FAILURE);
            }
        }
        return output;
    }

    /**
     * 查询提现申请记录
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return
     */
    @Override
    public OrderMoneyRecordOutput searchOrderMoneyRecords(Integer pageNum, Integer pageSize) {
        OrderMoneyRecordOutput output = new OrderMoneyRecordOutput();
        if(null == pageNum) {
            pageNum = 1;
        }
        if(null == pageSize) {
            pageSize = 10;
        }

        /**
         * 查询提现申请总条数
         */
        int totalNum = orderMoneyRecordDao.findCountByState();

        /**
         * 设置分页
         */
        PageHelper.startPage(pageNum, pageSize);

        /**
         * 查询提现申请记录列表
         */
        List<OrderMoneyRecord> list = orderMoneyRecordDao.findPageByState();

        /**
         * 对象转换
         */
        List<OrderMoneyRecordVo> returnList = convertOrderMoneyRecordVo(list);

        /**
         * 返回分页数据
         */
        PageInfo<OrderMoneyRecordVo> pageInfo = new PageInfo<>();
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(totalNum);
        pageInfo.setPages(this.totalPageNum(pageSize, totalNum));
        pageInfo.setList(returnList);
        output.setPageInfo(pageInfo);
        output.setCode(Constant.CodeConfig.CODE_SUCCESS);
        output.setMessage(Constant.MessageConfig.MSG_SUCCESS);
        return output;
    }


    /**
     * 审核/提现操作
     * @param recordId 提现记录id
     * @param state 提现记录状态
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public  Map<String, Object> updateOrderMoneyRecordState(Long recordId, String state) {
        Map<String, Object> output = new HashMap<>();
        /**
         * 1.查找该recordId对应的OrderMoneyRecord实体
         * 2.修改OrderMoneyRecord的state状态从 申请中改为审核通过
         * 3.修改OrderMoneyRecord的state状态从 审核通过改为已提现
         * 4.更新Account中balance(余额)字段
         * 5.更新提现金额对应订单中的账户状态
         */
        if(null != recordId){
            OrderMoneyRecord orderMoneyRecord = orderMoneyRecordDao.findOne(recordId);
            if(null != orderMoneyRecord){
                if(!StringUtils.isEmpty(state) && "1".equals(state)){
                    /**
                     * 提现申请状态修改：申请中改为审核通过
                     */
                    orderMoneyRecord.setRecordId(recordId);
                    orderMoneyRecord.setState(state);
                    orderMoneyRecord.setUpdatedAt(System.currentTimeMillis());
                    int modifyNum = orderMoneyRecordDao.updateOrderMoneyRecord(orderMoneyRecord);
                    if(modifyNum > 0){
                        output.put("status", 1);
                    }
                    return output;
                } else if(!StringUtils.isEmpty(state) && "2".equals(state)){
                    String tmpState = orderMoneyRecord.getState();
                    if(!StringUtils.isEmpty(tmpState) && "1".equals(tmpState)) {
                        /**
                         * 提现申请状态修改：审核通过改为已提现
                         */
                        orderMoneyRecord.setRecordId(recordId);
                        orderMoneyRecord.setState(state);
                        orderMoneyRecord.setUpdatedAt(System.currentTimeMillis());
                        int doneNum = orderMoneyRecordDao.updateOrderMoneyRecord(orderMoneyRecord);
                        Long tmpUserId = orderMoneyRecord.getUserId();
                        Account account = accountDao.findByUserId(tmpUserId);
                        if(null != account){
                            BigDecimal tmpBalance = account.getBalance().subtract(orderMoneyRecord.getTotalMoney());
                            /**
                             * 更新Account中balance(余额)字段
                             */
                            account.setBalance(tmpBalance);
                            account.setUpdateDate(System.currentTimeMillis());
                            account.setUserId(tmpUserId);
                            accountDao.updateAccountByUserId(account);
                            /**
                             * 更新提现金额对应订单中的账户状态
                             */
                            purchaseDao.updateAccountStatus(tmpUserId);
                            if(doneNum > 0){
                                output.put("status", 2);
                            }
                            return output;
                        }
                    }else{
                        output.put("status", 3);
                        return output;
                    }
                } else {
                    output.put("status", 4);
                }
            }
        }
        output.put("status", 5);
        return output;
    }

    /**
     * 对象转换
     * @param list 要转换的list
     * @return
     */
    private List<OrderMoneyRecordVo> convertOrderMoneyRecordVo(List<OrderMoneyRecord> list){
        List<OrderMoneyRecordVo> tmpList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(null != list && !list.isEmpty()){
            for(OrderMoneyRecord omr : list){
                OrderMoneyRecordVo ormVo = new OrderMoneyRecordVo();
                ormVo.setRecordId(omr.getRecordId());
                ormVo.setUserId(omr.getUserId());
                ormVo.setBankName(omr.getBankName());
                ormVo.setBankNameBranch(omr.getBankNameBranch());
                ormVo.setBankAccount(omr.getBankAccount());
                ormVo.setTotalMoney(omr.getTotalMoney());
                ormVo.setState(omr.getState());
                ormVo.setCreatedAt(sdf.format(omr.getCreatedAt()));
                ormVo.setUpdatedAt(sdf.format(omr.getUpdatedAt()));
                ormVo.setSerialNumber(omr.getSerialNumber());
                tmpList.add(ormVo);
            }
        }
        return tmpList;
    }


    /**
     * 计算总页数
     * @param pageSize
     * @param totalNum
     * @return
     */
    private int totalPageNum(int pageSize, int totalNum){
        return totalNum%pageSize == 0 ? totalNum/pageSize : totalNum/pageSize + 1;
    }


    /**
     * 1.根据提现申请表中的申请人ID查询申请该ID下所有的申请记录，并根据pageNum和pageSize进行分页展示
     *   返回开发规定的提现申请记录的Output对象
     *   ①、利用PageHelper开启分页；
     *   ②、根据分页信息查询提现申请的 List<OrderMoneyRecord>,并取得OrderMoneyRecord的分页信息；
     *   ③、若查询的List<OrderMoneyRecord>非空，则将其转为List<OrderMoneyRecordVo>以便按开发要求进行封装输出；
     *   ④、新建OrderMoneyRecordVo的分页信息PageInfo<OrderMoneyRecordVo>，并将分页信息Set其中；
     *   ⑤、将PageInfo<OrderMoneyRecordVo>、状态码及message封装进提现申请记录的Output对象（OrderMoneyRecordOutput）；
     *   ⑥、若查询结果为空，则提null、状态码及message封装进提现申请记录的Output对象（OrderMoneyRecordOutput）；
     *   ⑦、返回提现申请记录的Output对象（OrderMoneyRecordOutput）。
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public OrderMoneyRecordOutput searchOrderMoneyRecords(Long userId,Integer pageNum,Integer pageSize) {
        //创建提现申请记录的Output对象；
        OrderMoneyRecordOutput orderMoneyRecordOutput = new OrderMoneyRecordOutput();
        /**
         * ①、利用PageHelper开启分页；
         */
        Integer PageHelperPageNum =pageNum==null ? 1: pageNum;//默认第一页
        Integer PageHelperPageSize = pageSize==null ? 5: pageSize;//默认每页5条
        PageHelper.startPage(PageHelperPageNum,PageHelperPageSize);

        /**
         *  ②、根据分页信息查询提现申请的 List<OrderMoneyRecord>,并取得OrderMoneyRecord的分页信息；
         */
        List<OrderMoneyRecord> orderMoneyRecordList = orderMoneyRecordDao.findPage(userId);
        if (orderMoneyRecordList!=null){//判断取得的List集合是否为空
            //取得OrderMoneyRecord的分页信息
            PageInfo<OrderMoneyRecord> pageInfo = new PageInfo<>(orderMoneyRecordList);

            /**
             * ③、若查询的List<OrderMoneyRecord>非空，则将其转为List<OrderMoneyRecordVo>以便按开发要求进行封装输出；
             */
            List<OrderMoneyRecordVo> orderMoneyRecordVoList = convertOrderMoneyRecordVo(orderMoneyRecordList);//新建OrderMoneyRecordVo的List

            /**
             * ④、新建OrderMoneyRecordVo的分页信息PageInfo<OrderMoneyRecordVo>，并将分页信息Set其中；
             */
            PageInfo<OrderMoneyRecordVo> pageInfoVo = new PageInfo<>();//新建OrderMoneyRecordVo分页信息
            pageInfoVo.setPageNum(PageHelperPageNum);//当前页
            pageInfoVo.setPageSize(PageHelperPageSize);//每页条数
            pageInfoVo.setList(orderMoneyRecordVoList);//List<OrderMoneyRecordVo>
            pageInfoVo.setTotal(pageInfo.getTotal());//总条数
            pageInfoVo.setPages(pageInfo.getPages());//总页数
            pageInfoVo.setSize(pageInfo.getSize());//当前页条数

            /**
             * ⑤、将PageInfo<OrderMoneyRecordVo>、状态码及message封装进提现申请记录的Output对象；
             */
            orderMoneyRecordOutput.setPageInfo(pageInfoVo);//OrderMoneyRecordVo的分页信息
            orderMoneyRecordOutput.setCode(Constant.CodeConfig.CODE_SUCCESS);//成功状态码
            orderMoneyRecordOutput.setMessage(Constant.MessageConfig.MSG_SUCCESS);//成功信息
        }else {
            /**
             * ⑥、若查询结果为空，则提null、状态码及message封装进提现申请记录的Output对象（OrderMoneyRecordOutput）；
             */
            orderMoneyRecordOutput.setPageInfo(null);//分页信息
            orderMoneyRecordOutput.setCode(Constant.CodeConfig.CODE_SUCCESS);//成功状态码
            orderMoneyRecordOutput.setMessage(Constant.MessageConfig.MSG_SUCCESS);//成功信息
        }
        /**
         * ⑦、返回提现申请记录的Output对象（OrderMoneyRecordOutput）。
         */
        return orderMoneyRecordOutput;
    }

}
