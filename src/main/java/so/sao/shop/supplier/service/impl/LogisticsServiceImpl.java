package so.sao.shop.supplier.service.impl;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.LogisticsDao;
import so.sao.shop.supplier.dao.PurchaseDao;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.vo.PurchaseInfoVo;
import so.sao.shop.supplier.service.LogisticsService;
import so.sao.shop.supplier.util.MD5Util;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wyy on 2017/8/16.
 */
@Service
public class LogisticsServiceImpl implements LogisticsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private LogisticsDao logisticsDao;
    @Resource
    private PurchaseDao purchaseDao;
    /**
     * 根据物流单好查询物流信息
     * @param num 物流单号
     * @return
     */
    @Override
    public Result<Object> findLogisticInfo(String num) {
        Result<Object> result = new Result<>();
        try {
            //查询快递100信息
            String data = kuaidi100(num);
            if(data == null){
                result.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
                result.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
                return result;
            }

            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            //转换json字符为对象
            ObjectMapper objectMapper = new ObjectMapper();
            //Object o = objectMapper.readValue(data,Object.class);
            Map<String,Object> map = objectMapper.readValue(data,Map.class);
            String comStr = map.get("com").toString();
            String companyName = logisticsDao.findCompanyNameByCom(comStr);
            if (null != companyName){
                map.put("com",companyName);
            }
            result.setData(map);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage(Constant.MessageConfig.MSG_NO_DATA);
            return result;
        }
        return result;
    }

    /**
     * 发送http请求
     * @param url 请求地址
     * @param map 参数
     * @param encoding 编码格式
     * @return
     * @throws Exception
     */
    private String postRequest(String url, Map<String, String> map, String encoding) throws Exception {
        String body = "";
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        try {
            //创建httpclient对象
            client = HttpClients.createDefault();

            //创建post方式请求对象
            HttpPost httpPost = new HttpPost(url);

            //装填参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            if (map != null) {
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }

            //设置参数到请求对象中
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));

            logger.debug("请求地址：" + url);
            logger.debug("请求参数：" + nvps.toString());

            //设置header信息
            //指定报文头【Content-type】、【User-Agent】
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

            //执行请求操作，并拿到结果（同步阻塞）
            response = client.execute(httpPost);

            //获取结果实体
            HttpEntity entity = response.getEntity();

            if (entity != null && response.getStatusLine().getStatusCode() == 200) {
                //按指定编码转换结果实体为String类型
                body = EntityUtils.toString(entity, encoding);
            }else{
                throw new Exception();
            }


            EntityUtils.consume(entity);
            //释放链接
            response.close();
        }catch (IOException e){
            e.printStackTrace();
            throw e;
        } finally {
            client.close();
            if (response != null)
                response.close();
        }
        return body;
    }


    /**
     * 向快递100发送请求
     * @param num 物流单号
     * @return
     * @throws Exception
     */
    private String kuaidi100(String num) throws Exception {

        String resp = null;
        String customer ="835162313549B821995B976FE413644C";
        String key = "rzBnXFmG1070";
        try {
            //获取公司代码
            HashMap params = new HashMap();
            params.put("key",key);
            params.put("num",num);
            String com = postRequest("http://www.kuaidi100.com/autonumber/auto",params,"utf-8");
            com = com.substring(1,com.length()-1);
            if(com == null || com.length() <= 0){
               return null;
            }
            logger.debug("【快递100返回的公司代码json字符串】 ："+com);
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,Object> maps = objectMapper.readValue(com,HashMap.class);

            //获取快递信息
            String param = "{\"com\":\""+maps.get("comCode")+"\",\"num\":\""+num+"\"}";
            String sign = MD5Util.getMD5(param+key+customer);
            sign = encode(param+key+customer);
            params = new HashMap();
            params.put("param",param);
            params.put("sign",sign);
            params.put("customer",customer);
            resp = postRequest("http://poll.kuaidi100.com/poll/query.do",params,"utf-8");
            logger.debug("【快递100返回的物流信息json字符串】 ："+resp);
        } catch (Exception e) {
            logger.debug("系统异常",e);
            throw e;
        }
        return resp;
    }

    /**
     * md5加密
     * @param pwd 被加密字符串
     * @return
     */
    private String encode(String pwd) {
        //用于加密的字符
        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            byte[] btInput = pwd.getBytes();

            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput);

            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {   //  i = 0
                byte byte0 = md[i];  //95
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5
                str[k++] = md5String[byte0 & 0xf];   //   F
            }

            //返回经过加密后的字符串
            return new String(str);

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 插入已确认收货的订单
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> insertReceivedOrder(String orderId) throws Exception {
        Map<String,Object> resultMap = new HashMap<>();
        Integer orderStatus = purchaseDao.getOrderStatus(orderId);
        if(orderStatus != 3){
            resultMap.put("flag","fail");
            resultMap.put("msg","订单状态不合法，不能收货");
            return resultMap;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("orderId",orderId);
        map.put("createTime",new Date());
        map.put("orderStatus",Constant.OrderStatusConfig.CONFIRM_RECEIVED);
        logisticsDao.updateOrderStatus(map);
        Integer count = logisticsDao.insertReceivedOrder(map);
        if(count != 0){
            resultMap.put("flag","success");
        }
        return resultMap;
    }

    /**
     * 获取已收货7天的订单ID
     *
     * @return List<String> 订单编号集合
     */
    @Override
    public List<String> findOrderIdByTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String nowTime = simpleDateFormat.format(new Date());
        List<String> orderIds = logisticsDao.findOrderIdByTime(nowTime);
        return orderIds;
    }

    /**
     * 自动确认收货
     *
     * @param orderIds 订单ID集合
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int receiveOrder(List<String> orderIds) {
        Map<String,Object> map = new HashMap<>();
        map.put("orderIds",orderIds);
        map.put("date",new Date());
        map.put("status",1);
        logisticsDao.updateQrcodesStatus(map);//更改二维码状态
        return logisticsDao.receiveOrder(map);//确认收货
    }

    /**
     * 根据订单ID删除received_purchase表已自动确认收货的订单
     *
     * @param orderIds 订单ID集合
     */
    @Override
    public void deleteReceivedOrderByOrderId(List<String> orderIds) {
        logisticsDao.deleteReceivedOrderByOrderId(orderIds);
    }

    /**
     * 根据订单状态获取订单信息（订单ID、物流单号）
     *
     * @param orderStatus 订单状态
     * @return PurchaseInfoVo 订单信息列表
     */
    @Override
    public List<PurchaseInfoVo>  findOrderInfoByOrderStatus(Integer orderStatus) {
        return logisticsDao.findOrderInfoByOrderStatus(orderStatus);
    }
}

