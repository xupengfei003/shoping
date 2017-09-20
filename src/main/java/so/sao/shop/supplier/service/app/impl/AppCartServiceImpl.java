package so.sao.shop.supplier.service.app.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.dao.*;
import so.sao.shop.supplier.dao.app.AppCartItemDao;
import so.sao.shop.supplier.domain.*;
import so.sao.shop.supplier.pojo.input.AppCartItemInput;
import so.sao.shop.supplier.pojo.output.AppCartItemOut;
import so.sao.shop.supplier.pojo.vo.AppCartItemVo;
import so.sao.shop.supplier.service.app.AppCartService;
import so.sao.shop.supplier.util.Ognl;

import java.util.*;

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
     * 将购物车记录转为购物车记录VO
     * @param cartItem
     * @return
     * @throws Exception
     */
    private AppCartItemVo transformAppCartItemVo(AppCartItem cartItem,
                                                 Date createTime,
                                                 Date updateTime,
                                                 Long userId,
                                                 Long id,
                                                 Integer count) throws Exception {
        if (Ognl.isNull(cartItem)) {
            return null;
        }
        //新建要返回的List<AppCartItemVo>
        AppCartItemVo cartItemVo = new AppCartItemVo();
        cartItemVo.setId(id);
        cartItemVo.setSupplierId(cartItem.getSupplierId());
        cartItemVo.setSupplierName(cartItem.getSupplierName());
        cartItemVo.setCommodityId(cartItem.getCommodityId());
        cartItemVo.setCommodityName(cartItem.getCommodityName());
        cartItemVo.setCommodityPrice(cartItem.getCommodityPrice());
        cartItemVo.setCommodityPic(cartItem.getCommodityPic());
        cartItemVo.setMeasureSpecId(cartItem.getMeasureSpecId());
        cartItemVo.setMeasureSpecName(cartItem.getMeasureSpecName());
        cartItemVo.setRuleVal(cartItem.getRuleVal());
        cartItemVo.setUnitId(cartItem.getUnitId());
        cartItemVo.setUnitName(cartItem.getUnitName());
        cartItemVo.setCommodityProperties(cartItem.getCommodityProperties());
        cartItemVo.setCount(count);
        cartItemVo.setCreatedAt(createTime);
        cartItemVo.setUpdatedAt(updateTime);
        cartItemVo.setUserId(userId);
        //返回转换之后的list
        return cartItemVo;
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
            1.根据购物车记录ID和userId查询购物车记录中的商品Id,若查询结果为null,则返回失败
            2.根据商品的Id查询商品的相信信息,同时校验商品及供货商是否合法,返回符合要求的购物车信息
            3.若返回的code为0,则返回失败
            4.校验是否有库存,若有库存,进行更新,成功的话返回购物车记录
            5.若库存不足，返回失败
         */
        Map<String,Object> map = new HashMap<>(); // 定义Map,用于保存提示信息及更新后的购物车记录
        // 1.根据购物车记录ID和userId查询购物车记录中的商品Id,若查询结果为null,则返回失败
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
        // 3.若返回的code为0,则返回失败
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
            // 5.若库存不足，返回失败
            map.put("code","0");
            map.put("msg","库存不足");
            map.put("appCartItem",appCartItem);
        }
        return map;
    }

    /**
     * 批量修改修改购物车内商品的数量
     * @param inputList
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> updateCartItemBatch(List<AppCartItemInput> inputList) throws Exception {
        /*
            1.根据购物车记录ID和userId查询购物车记录中的商品Id,若查询结果为null,则返回失败
            2.若查询出集合的size和入参集合的size不等,说明更新数据有误,更新失败
            3.根据商品的Id查询商品的相信信息,同时校验商品及供货商是否合法,返回符合要求的购物车信息
            4.若返回的code为0,则返回失败
            5.校验是否有库存,若有库存,进行更新
            6.若库存不足，返回失败
         */
        Map<String,Object> map = new HashMap<>(); // 定义Map,用于保存提示信息及更新后的购物车记录
        // 更新数据集合不能为空
        if (!Ognl.CollectionIsNotEmpty(inputList)){
            map.put("code","0");
            map.put("msg","更新数据集合不能为空");
            map.put("collection",null);
            return map;
        }
        // 1.根据购物车记录ID和userId查询购物车记录中的商品Id,若查询结果为null,则返回失败
        List<AppCartItem> cartItemList = cartItemDao.findByIdAndUserIdBatch(inputList);
        // 若集合为null,说明没购物车记录,更新失败
        if (!Ognl.CollectionIsNotEmpty(cartItemList)){
            map.put("code","0");
            map.put("msg","购物车记录不存在");
            map.put("collection",null);
            return map;
        }
        // 2.若查询出集合的size和入参集合的size不等,说明更新数据有误,更新失败
        if (inputList.size() != cartItemList.size()){
            map.put("code","0");
            map.put("msg","更新数据有误");
            map.put("collection",null);
            return map;
        }
        List<AppCartItem> updateList = new ArrayList<>(); // 用于记录批量更新的完整购物车记录数据
        // 3.根据商品的Id查询商品的相信信息,同时校验商品及供货商是否合法,返回符合要求的购物车信息
        for (int i = 0; i < cartItemList.size(); i++) {
            AppCartItem appCartItems = cartItemList.get(i);
            map = validateCommodity(appCartItems.getCommodityId());
            String code = (String)map.get("code");
            // 4.若返回的code为0,则返回失败
            if ("0".equals(code)){
                map.put("appCartItem",null);
                return map;
            }
            // 5.校验是否有库存,若有库存,进行更新
            AppCartItem appCartItem = (AppCartItem)map.get("appCartItem");
            int number = inputList.get(i).getNumber();
            if(appCartItem.getInventory() - number >= 0){
                appCartItem.setCount(number);                     // 商品数量
                appCartItem.setUpdatedAt(new Date());             // 更新时间
                appCartItem.setUserId(appCartItems.getUserId());  // 用户ID
                appCartItem.setId(appCartItems.getId());          // 记录ID
                updateList.add(appCartItem);
            }else{
                // 6.若库存不足，返回失败
                map.put("code","0");
                map.put("msg","库存不足");
                map.put("collection",null);
                return map;
            }
        }
        // 更新数据,成功的话返回购物车记录
        int num = cartItemDao.updateByIdBatch(updateList);
        if (num > 0){
            map.put("code","1");
            map.put("msg","更新成功");
            map.put("appCartItem",updateList);
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
              b.若返回的code为0,则返回失败
              c.校验是否有库存,若有库存,进行更新
              d.若库存不足，返回失败
         */
        Map<String,Object> map = new HashMap<>(); // 定义Map,用于保存提示信息及要新增的购物车记录
        // 1.判断是否存在相同商品的购物车,若存在,进行更新
        AppCartItem appCartItem = cartItemDao.selectByCommodityId(commodityId,userId);
        if (Ognl.isNotNull(appCartItem)){
            // 更新的数量等于固有数量加新添加数量
            Integer TotalNumber = appCartItem.getCount() + number;
            // 调用更新接口
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
            // b.若返回的code为0,则返回失败
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
                // d.若库存不足，返回失败
                map.put("code","0");
                map.put("msg","库存不足");
                map.put("appCartItem",appCartItem);
            }
        }
        return map;
    }

    /**
     * 查询该用户下的所有购物车记录信息
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> findCartItemsByUserId(Long userId) throws Exception {
        /*
            1.查询个人购物车记录结合,若记录为null,返回暂无购物车记录
            2.校验商品是否可继续出售并取出商品信息
            3.校验库存
            4.校验商品是否失效
         */
        Map<String,Object> map = new HashMap<>();           // 定义Map,用于保存提示信息及数据
        List<AppCartItemOut> outList = new ArrayList<>();   // 要求格式的数据集合
        // 1.查询个人购物车记录结合,若记录为null,返回暂无购物车记录
        List<AppCartItem> list = cartItemDao.selectByUserId(userId);
        if (!Ognl.CollectionIsNotEmpty(list)){
            map.put("code","1");
            map.put("msg","暂无购物车记录");
            map.put("collection",null);
            return map;
        }
        List<AppCartItemVo> voList = new ArrayList<>();     // 购物车转化成Vo的集合
        for (AppCartItem appCartItems:list) {
            // 取出查询的数据
            int oldNumber = appCartItems.getCount();         // 购物车商品数量
            Date creatTime = appCartItems.getCreatedAt();    // 购物车创建时间
            Date uapdateTime = appCartItems.getUpdatedAt();  // 购物车修改时间
            Long id = appCartItems.getId();                  // ID
            // 2.校验商品是否可继续出售并取出商品信息
            map = validateCommodity(appCartItems.getCommodityId());
            // 获取信息码和购物车记录
            String code = (String)map.get("code");
            AppCartItem appCartItem = (AppCartItem)map.get("appCartItem");
            if (Ognl.isNull(appCartItem)){
                map.put("code","0");
                map.put("msg","商品信息已经被删除");
                map.put("collection",null);
                return map;
            }
            // 转化为VO
            AppCartItemVo cartItemVo = transformAppCartItemVo(appCartItem,creatTime,uapdateTime,userId,id,oldNumber);
            // 3.校验库存
            if(appCartItem.getInventory() - oldNumber >= 0) {
                cartItemVo.setRemaining("1");       // 有库存
            } else {
                cartItemVo.setRemaining("0");       // 无库存
            }
            // 4.校验商品是否失效
            if ("0".equals(code)){
                cartItemVo.setIsUsable("0");        // 商品已经失效
            } else {
                cartItemVo.setIsUsable("1");        // 商品未失效
            }
            voList.add(cartItemVo);
        }
        // 5.转化格式输出
        outList = transformAppCartItemOut(voList);
        map.put("code","1");
        map.put("msg","成功");
        map.put("collection",outList);
        return map;
    }


    /**
     * 将List<AppCartItemVo>转为List<AppCartItemOut>
     * @param voList
     * @return
     */
    private List<AppCartItemOut> transformAppCartItemOut (List<AppCartItemVo> voList){
        if (Ognl.isNull(voList)){
            return null;
        }
        // 新建输出对象的List
        List<AppCartItemOut> outList = new ArrayList<>();
        // 循环数据
        for (AppCartItemVo vo : voList) {
            // 当outList为null时
            if (Ognl.CollectionIsNotEmpty(outList)) {
                // 当outList不为null时
                for (int i = 0 ,len = outList.size(); i < len; i++) {
                    // 判断List<AppCartItemOut>中是否有supplierId,若有supplierId,返回这条记录,若无返回null
                    AppCartItemOut ap = exsitSupplierId(vo.getSupplierId(), outList);
                    if (Ognl.isNotNull(ap)) {
                        ap.getAppCartItems().add(vo);
                    } else {
                        AppCartItemOut appCartItemOut = transformOut(vo);
                        outList.add(appCartItemOut);
                    }
                }
            } else {
                AppCartItemOut appCartItemOut = transformOut(vo);
                outList.add(appCartItemOut);
            }
        }
        return outList;
    }

    /**
     * 将AppCartItemVo对象转为AppCartItemOut对象
     * @param vo
     * @return
     */
    private AppCartItemOut transformOut (AppCartItemVo vo){
        if (Ognl.isNull(vo)){
            return null;
        }
        // 新建输出对象
        AppCartItemOut appCartItemOut = new AppCartItemOut();
        appCartItemOut.setSupplierId(vo.getSupplierId());
        appCartItemOut.setSupplierName(vo.getSupplierName());
        // 新建Vo的list
        List<AppCartItemVo> list = new ArrayList<>();
        list.add(vo);
        appCartItemOut.setAppCartItems(list);
        appCartItemOut.setList(new String[0]);
        appCartItemOut.setIsSelectShop(false);
        return appCartItemOut;
    }

    /**
     * 判断List<AppCartItemOut>中是否有supplierId,若有supplierId,返回这条记录,若无返回null
     * @param supplierId
     * @param outList
     * @return
     */
    private AppCartItemOut exsitSupplierId(Long supplierId, List<AppCartItemOut> outList) {
        if (Ognl.isNull(outList)){
            return null;
        }
        for (int i = 0; i < outList.size() ; i++) {
            if(outList.get(i).getSupplierId().equals(supplierId)){
                return outList.get(i);
            }
        }
        return null;
    }

}
