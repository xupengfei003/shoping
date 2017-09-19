package so.sao.shop.supplier.service.app.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.dao.*;
import so.sao.shop.supplier.dao.app.AppCartItemDao;
import so.sao.shop.supplier.domain.*;
import so.sao.shop.supplier.service.app.AppCartService;
import so.sao.shop.supplier.util.Ognl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wyy on 2017/7/19.
 */
@Service
public class AppCartServiceImpl implements AppCartService {

    /**
     * 初始化日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 购物车表对应dao
     */
    @Autowired
    AppCartItemDao cartItemDao;

    /**
     * 供应商商品关系表对应dao
     */
    @Autowired
    private SupplierCommodityDao supplierCommodityDao;

    /**
     * 计量规格表对应的dao
     */
    @Autowired
    private CommUnitDao commUnitDao;

    /**
     * 计量单位表对应的dao
     */
    @Autowired
    private CommMeasureSpecDao commMeasureSpecDao;

    /**
     * 商品表对应的dao
     */
    @Autowired
    private CommodityDao commodityDao;

    /**
     * 账户表对应dao
     */
    @Autowired
    private AccountDao accountDao;

    /**
     * 根据商品的Id查询商品的相信信息，同时校验商品及供货商是否合法，返回符合要求的购物车信息
     * @param id
     * @return
     * @throws Exception
     */
    private Map<String,Object> validateCommodity (long id) throws Exception{
        // 1.定义Map,用于保存提示信息及商品信息
        Map<String,Object> map = new HashMap<>();
        if (Ognl.isNull(id)){
            map.put("msg","商品ID有误");
            map.put("appCartItem",null);
            return map;
        }
        /*
            2.根据商品Id,查询该商品的详细信息
              a.根据商品ID查询商品信息
              b.根据查询出商品信息中的计量单位ID查询计量规格名称
              c.根据查询出商品信息中的计量规格Id查询计量单位名称
              d.根据查询出商品信息中的供应商id查询供应商名称与供应商状态
              e.根据查询出商品信息中的code69查询商品名称
         */
        SupplierCommodity supplierCommodity = supplierCommodityDao.findOneToCartItem(id);
        if (Ognl.isNull(supplierCommodity)){
            map.put("code","0");
            map.put("msg","该商品已被删除");
            map.put("appCartItem",null);
            return map;
        }
        CommUnit commUnit = commUnitDao.findOne(supplierCommodity.getUnitId());
        if (Ognl.isNull(commUnit)){
            map.put("code","0");
            map.put("msg","计量单位有误");
            map.put("appCartItem",null);
            return map;
        }
        CommMeasureSpec commMeasureSpec = commMeasureSpecDao.findOne(supplierCommodity.getMeasureSpecId());
        if (Ognl.isNull(commMeasureSpec)){
            map.put("code","0");
            map.put("msg","计量规格有误");
            map.put("appCartItem",null);
            return map;
        }
        Account account = accountDao.findNameAndStatus(supplierCommodity.getSupplierId());
        if (Ognl.isNull(account)){
            map.put("code","0");
            map.put("msg","供应商不存在");
            map.put("appCartItem",null);
            return map;
        }
        Commodity commodity = commodityDao.findNameByCode69(supplierCommodity.getCode69());
        // 3.生成购物车数据
        AppCartItem appCartItem = new AppCartItem();
        appCartItem.setSupplierId(supplierCommodity.getSupplierId()); //供应商ID
        appCartItem.setSupplierName(account.getProviderName());       //供应商名称
        appCartItem.setCommodityId(id);                               //商品ID
        appCartItem.setCommodityName(commodity.getName());            //商品名称
        appCartItem.setCommodityPrice(supplierCommodity.getPrice());  //商品价格
        appCartItem.setCommodityPic(supplierCommodity.getMinImg());   //商品图片路径
        appCartItem.setMeasureSpecId(supplierCommodity.getMeasureSpecId()); //计量规格ID
        appCartItem.setMeasureSpecName(commMeasureSpec.getName());    //计量规格名称
        appCartItem.setRuleVal(supplierCommodity.getRuleVal());       //规格值
        appCartItem.setUnitId(supplierCommodity.getUnitId());         //计量单位ID
        appCartItem.setUnitName(commUnit.getName());                  //计量单位名称
        appCartItem.setCommodityProperties(supplierCommodity.getSku());//sku
        appCartItem.setInventory(supplierCommodity.getInventory());    //库存数
        // 4.校验供应商状态与商品状态
        if (1 != account.getAccountStatus()){
            map.put("code","0");
            map.put("msg","供应商已被停用");
            map.put("appCartItem",appCartItem);
            return map;
        }
        if (2 != supplierCommodity.getStatus()){
            map.put("code","0");
            map.put("msg","商品已下架");
            map.put("appCartItem",appCartItem);
            return map;
        }
        if (1 != supplierCommodity.getInvalidStatus()){
            map.put("code","0");
            map.put("msg","商品已失效");
            map.put("appCartItem",appCartItem);
            return map;
        }
        // 5.返回
        map.put("code","1");
        map.put("msg",null);
        map.put("appCartItem",appCartItem);
        return map;
    }

    /**
     * 根据购物车记录ID从购物车删除商品
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCartItemById(Long id,Long userId) throws Exception{
        // 若删除时受影响的行数大于0为成功，其余为删除失败
        return cartItemDao.deleteById(id,userId)>0?true:false;
    }

    /**
     * 修改购物车内商品的数量
     * @param cartitemId
     * @param number
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> updateCartItem(Long cartitemId, Integer number, Long userId) throws Exception{
        /*
            1.根据购物车记录ID和userId查询购物车记录中的商品Id,若查询结果为null,则返回null
            2.根据商品的Id查询商品的相信信息,同时校验商品及供货商是否合法,返回符合要求的购物车信息
            3.若返回的购物车信息为null,则返回null
            4.校验是否有库存,若有库存,进行更新,成功的话返回购物车记录
            5.若库存不足，返回null
         */
        Map<String,Object> map = new HashMap<>(); // 定义Map,用于保存提示信息及更新后的购物车记录
        // 1.根据购物车记录ID和userId查询购物车记录中的商品Id,若查询结果为null,则返回null
        AppCartItem appCartItem = cartItemDao.selectByIdAndUserId(cartitemId,userId);
        if (Ognl.isNull(appCartItem)){
            map.put("code","0");
            map.put("msg","该购物车记录不存在");
            map.put("appCartItem",null);
            return map;
        }
        // 2.根据商品的Id查询商品的相信信息,同时校验商品及供货商是否合法,返回符合要求的购物车信息
        map = validateCommodity(appCartItem.getCommodityId());
        String code = (String)map.get("code");
        // 3.若返回的code为0,则返回null
        if ("0".equals(code)){
            return map;
        }
        // 4.校验是否有库存,若有库存,进行更新
        appCartItem = (AppCartItem)map.get("appCartItem");
        if(appCartItem.getInventory() - number >= 0){
            appCartItem.setCount(number);           // 商品数量
            appCartItem.setUpdatedAt(new Date());   // 更新时间
            appCartItem.setUserId(userId);          // 用户ID
            appCartItem.setId(cartitemId);          // 记录ID
            // 更新数据,成功的话返回购物车记录
            int num = cartItemDao.updateById(appCartItem);
            if (num > 0){
                map.put("code","1");
                map.put("msg","更新成功");
                map.put("appCartItem",appCartItem);
            }
        }else{
            // 5.若库存不足，返回null
            map.put("code","0");
            map.put("msg","库存不足");
            map.put("appCartItem",appCartItem);
        }
        return map;
    }

    /**
     * 添加商品到购物车
     * @param commodityId
     * @param number
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> saveCartItem(Long commodityId, Integer number, Long userId) throws Exception{
        /*
            1.判断是否存在相同商品的购物车,若存在,进行更新
            2.若不存在,进行添加
              a.根据商品的Id查询商品的相信信息,同时校验商品及供货商是否合法,返回符合要求的购物车信息
              b.若返回的购物车信息为null,则返回null
              c.校验是否有库存,若有库存,进行更新
              d.若库存不足，返回null
         */
        Map<String,Object> map = new HashMap<>(); // 定义Map,用于保存提示信息及要新增的购物车记录
        // 1.判断是否存在相同商品的购物车,若存在,进行更新
        AppCartItem appCartItem = cartItemDao.selectByCommodityId(commodityId,userId);
        if (Ognl.isNotNull(appCartItem)){
            Integer TotalNumber = appCartItem.getCount() + number;
            map = updateCartItem(appCartItem.getId(), TotalNumber, userId);
            // 获取信息码
            String code = (String)map.get("code");
            if ("1".equals(code)){
                map.put("msg","添加购物车成功");
            }
        } else {
            // 2.若不存在,进行添加
            // a.根据商品的Id查询商品的相信信息,同时校验商品及供货商是否合法,返回符合要求的购物车信息
            map = validateCommodity(commodityId);
            String code = (String)map.get("code");
            // b.若返回的code为0,则返回null
            if ("0".equals(code)){
                return map;
            }
            // c.校验是否有库存,若有库存,进行更新
            appCartItem = (AppCartItem)map.get("appCartItem");
            if(appCartItem.getInventory() - number >= 0){
                appCartItem.setCount(number);           // 商品数量
                appCartItem.setCreatedAt(new Date());   // 创建时间
                appCartItem.setUserId(userId);          // 用户ID
                // 插入数据
                int num = cartItemDao.insertOne(appCartItem);
                if (num > 0){
                    map.put("code","1");
                    map.put("msg","添加购物车成功");
                    map.put("appCartItem",appCartItem);
                }
            }else{
                // d.若库存不足，返回null
                map.put("code","0");
                map.put("msg","库存不足");
                map.put("appCartItem",appCartItem);
            }
        }
        return map;
    }






//
//    @Override
//    public PageInfo<AppCartItem> findCartItemByUserId(Long userId, int pageNum, int pageSize) {
//
//        /**
//         * 业务逻辑：
//         * 1. 根据用户id查询出购物车表中的信息
//         * 2. 根据商品id去商品表中查询相应的商品信息
//         * 3. 如果购物车中的数量大于商品信息中的库存数量则
//         * 4. 若商品表链接获取失败，取购物车表中的商品缓存信息
//         */
//
//        PageHelper.startPage(pageNum,pageSize);
//        List<AppCartItem> list = cartItemDao.findCartItemByUserId(userId);
//
//        for (int i = 0; i < list.size(); i++) {
//            AppCartItem appCartItem = list.get(i);
//            //supplierCommodityDao没有提供批量查询的方法，这里后期会影响性能
//            SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(appCartItem.getCommodityId());//
//            CommodityOutput commodityOutput = supplierCommodityDao.findDetail(appCartItem.getCommodityId());
//            Account account = accountDao.findAccountByUserId(appCartItem.getUserId());
//            appCartItem.setSupplierName(account.getProviderName());
//            appCartItem.copySupplierCommodity(supplierCommodity);
//            appCartItem.copyCommodityOutput(commodityOutput);
//            computeRemainig(appCartItem);
//            logger.debug("【购物车信息】" +appCartItem);
//        }
//        return new PageInfo<AppCartItem>(list);
//    }
//
//    @Override
//    public AppCartItem findOne(Long id) {
//        return cartItemDao.findCartItemById(id);
//    }
//
//
//    @Override
//    public boolean deleteCartItemsByIds(List<Long> cartitemIds) {
//        return cartItemDao.deleteBatchByPrimaryKey(cartitemIds)>0?true:false;
//    }
//

//
//    @Override
//    public List<AppCartItemOut> findCartItemsByUserId(Long userId) {
//        /**
//         * 业务逻辑：
//         * 1. 根据用户id查询出购物车表中的信息
//         * 2. 根据商品id去商品表中查询相应的商品信息
//         */
//        List<AppCartItem> list = cartItemDao.findCartItemByUserId(userId);
//        List<AppCartItemOut> appCartItemOuts = new ArrayList<>();
//        for (int i = 0; i < list.size(); i++) {
//            AppCartItem appCartItem = list.get(i);
//            //supplierCommodityDao没有提供批量查询的方法，这里后期会影响性能
//            SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(appCartItem.getCommodityId());//
//            CommodityOutput commodityOutput = supplierCommodityDao.findDetail(appCartItem.getCommodityId());
//            Account account = accountDao.findAccountByUserId(appCartItem.getUserId());
//            appCartItem.setSupplierName(account.getProviderName());
//            appCartItem.copySupplierCommodity(supplierCommodity);
//            appCartItem.copyCommodityOutput(commodityOutput);
//            computeRemainig(appCartItem);
//            logger.debug("【购物车信息1】" +appCartItem);
//
//            //重新组装数据格式
//            AppCartItemOutSub appCartItemOutSub = new AppCartItemOutSub();
//            appCartItemOutSub.copyAppCartItem(appCartItem);
//            String[] pros =  new String[0];
//            if(appCartItemOuts.size()>0){
//                for (int j = 0; j < appCartItemOuts.size(); j++) {
//                    AppCartItemOut aci = appCartItemOuts.get(j);
//                    if(appCartItem.getSupplierId().equals(aci.getSupplierId())){
//                        aci.getAppCartItems().add(appCartItemOutSub);
//                    }else{
//                        AppCartItemOut appCartItemOut = new AppCartItemOut();
//                        appCartItemOut.setSupplierId(appCartItem.getSupplierId());
//                        appCartItemOut.setSupplierName(appCartItem.getSupplierName());
//                        appCartItemOut.getAppCartItems().add(appCartItemOutSub);
//                        appCartItemOut.setList(pros);
//                        appCartItemOut.setIsSelectShop(false);
//                        appCartItemOuts.add(appCartItemOut);
//
//                    }
//
//                }
//            }else{
//                AppCartItemOut appCartItemOut = new AppCartItemOut();
//                appCartItemOut.setSupplierId(appCartItem.getSupplierId());
//                appCartItemOut.setSupplierName(appCartItem.getSupplierName());
//                appCartItemOut.getAppCartItems().add(appCartItemOutSub);
//                appCartItemOut.setList(pros);
//                appCartItemOut.setIsSelectShop(false);
//                appCartItemOuts.add(appCartItemOut);
//            }
//
//            logger.debug("【购物车信息2】" +appCartItemOuts);
//        }
//
//
//
//        return appCartItemOuts;
//    }
//
//    /**
//     * 得到商品信息
//     * @return
//     * @param cid 商品id
//     */
//    private SupplierCommodity checkStore(Long cid) {
//        return supplierCommodityDao.findOne(cid);
//    }
//
//    /**
//     * 计算库存判断字段
//     * @param appCartItem
//     */
//    private void computeRemainig(AppCartItem appCartItem){
//        if(appCartItem.getInventory() == null){
//            appCartItem.setRemaining(false);
//            return;
//        }
//
//        if(appCartItem.getCount()>appCartItem.getInventory()){
//            appCartItem.setRemaining(false);
//        }else{
//            appCartItem.setRemaining(true);
//        }
//    }
//
//    /**
//     * 转换对象
//     * @param appCartItemInput
//     * @return
//     */
//    private AppCartItem convertToCartItem(AppCartItemInput appCartItemInput) {
//        if(appCartItemInput ==null){
//            return null;
//        }
//        AppCartItem appCartItem = new AppCartItem();
//
//        appCartItem.setUserId(appCartItemInput.getUserId());
//        appCartItem.setSupplierId(appCartItemInput.getSupplierId());
//        appCartItem.setSupplierName(appCartItemInput.getSupplierName());
//
//        appCartItem.setCommodityId(appCartItemInput.getCommodityId());
//        appCartItem.setCommodityPrice(appCartItemInput.getCommodityPrice());
//        appCartItem.setCommodityName(appCartItemInput.getCommodityName());
//        appCartItem.setCommodityPic(appCartItemInput.getCommodityPic());
//        appCartItem.setCommodityProperties(appCartItemInput.getCommodityProperties());
//        appCartItem.setCount(appCartItemInput.getCount());
//        return appCartItem;
//    }
}
