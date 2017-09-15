package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.dao.AccountDao;
import so.sao.shop.supplier.dao.AppCartItemDao;
import so.sao.shop.supplier.dao.SupplierCommodityDao;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.AppCartItem;
import so.sao.shop.supplier.domain.SupplierCommodity;
import so.sao.shop.supplier.pojo.input.AppCartItemInput;
import so.sao.shop.supplier.pojo.output.AppCartItemOut;
import so.sao.shop.supplier.pojo.output.AppCartItemOutSub;
import so.sao.shop.supplier.pojo.output.CommodityOutput;
import so.sao.shop.supplier.service.AppCartService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wyy on 2017/7/19.
 */
@Transactional
@Service
public class AppCartServiceImpl implements AppCartService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AppCartItemDao cartItemDao;

    @Autowired
    private SupplierCommodityDao supplierCommodityDao;

    @Autowired
    private AccountDao accountDao;

    @Override
    public boolean saveCartItem(AppCartItemInput appCartItemInput) {

        if(appCartItemInput == null){
            return false;
        }

        if (!(checkStore(appCartItemInput.getCommodityId()).getInventory() > 0)){//没库存
            logger.debug("【没库存】");
            return false;
        }

        AppCartItem appCartItem = new AppCartItem();
        appCartItem = convertToCartItem(appCartItemInput);
        appCartItem.setCreatedAt(new Date());
        //查询是否存在相同的商品
        List<AppCartItem> existsAppCartItemLit = cartItemDao.findExistsCartItem(appCartItem.getUserId(),appCartItem.getCommodityId());
        logger.debug("【查询是否存在相同的商品】"+(existsAppCartItemLit.size()>0?true:false) +"  "+ existsAppCartItemLit.size());
        if(existsAppCartItemLit !=null&& existsAppCartItemLit.size()>0){//如果存在，则更新商品数量

            AppCartItem existsAppCartItem = existsAppCartItemLit.get(0);
            Integer updateNumber = existsAppCartItem.getCount()+ appCartItemInput.getCount();//需要更新的库存数量
            logger.debug("【需要更新的数量】" + updateNumber);
            //更新记录信息并返回商品库存信息
            AppCartItem updatedAppCartItem = updateCartItem(existsAppCartItem.getId(), existsAppCartItem.getCommodityId(),updateNumber);
            logger.debug("【更新后商品信息】" + updatedAppCartItem);
            logger.debug("【更新后库存余量】" + updatedAppCartItem.getInventory());
            return updatedAppCartItem.getRemaining();
        }else{//如果不存在相同的商品
            //查询商品库存
            SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(appCartItem.getCommodityId());
            CommodityOutput commodityOutput = supplierCommodityDao.findDetail(appCartItem.getCommodityId());
            Account account = accountDao.findAccountByUserId(appCartItem.getUserId());
            appCartItem.setSupplierName(account.getProviderName());
            Double inventory = supplierCommodity.getInventory();
            appCartItem.copySupplierCommodity(supplierCommodity);
            appCartItem.copyCommodityOutput(commodityOutput);
            computeRemainig(appCartItem);
            //判断是否还有库存
            if(appCartItem.getRemaining()){
                logger.debug("【插入购物车信息】" + appCartItem);
                return cartItemDao.insertSelective(appCartItem)>0?true:false;
            }else{
                return false;
            }
        }
    }

    @Override
    public boolean deleteCartItemById(Long id) {
        return cartItemDao.deleteByPrimaryKey(id)>0?true:false;
    }

    @Override
    public PageInfo<AppCartItem> findCartItemByUserId(Long userId, int pageNum, int pageSize) {

        /**
         * 业务逻辑：
         * 1. 根据用户id查询出购物车表中的信息
         * 2. 根据商品id去商品表中查询相应的商品信息
         * 3. 如果购物车中的数量大于商品信息中的库存数量则
         * 4. 若商品表链接获取失败，取购物车表中的商品缓存信息
         */

        PageHelper.startPage(pageNum,pageSize);
        List<AppCartItem> list = cartItemDao.findCartItemByUserId(userId);

        for (int i = 0; i < list.size(); i++) {
            AppCartItem appCartItem = list.get(i);
            //supplierCommodityDao没有提供批量查询的方法，这里后期会影响性能
            SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(appCartItem.getCommodityId());//
            CommodityOutput commodityOutput = supplierCommodityDao.findDetail(appCartItem.getCommodityId());
            Account account = accountDao.findAccountByUserId(appCartItem.getUserId());
            appCartItem.setSupplierName(account.getProviderName());
            appCartItem.copySupplierCommodity(supplierCommodity);
            appCartItem.copyCommodityOutput(commodityOutput);
            computeRemainig(appCartItem);
            logger.debug("【购物车信息】" +appCartItem);
        }
        return new PageInfo<AppCartItem>(list);
    }

    @Override
    public AppCartItem findOne(Long id) {
        return cartItemDao.findCartItemById(id);
    }


    @Override
    public boolean deleteCartItemsByIds(List<Long> cartitemIds) {
        return cartItemDao.deleteBatchByPrimaryKey(cartitemIds)>0?true:false;
    }

    @Override
    public AppCartItem updateCartItem(Long cartitemId,Long commodityId,Integer number) {
        /**
         * 业务逻辑
         * 1. 根据商品id更新商品数量，同时更新购物车缓存信息
         * 2. 返回更新后的信息
         */

        //根据商品id更新商品数量，同时更新购物车缓存信息
        SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(commodityId);
        CommodityOutput commodityOutput = supplierCommodityDao.findDetail(commodityId);
        AppCartItem appCartItem = cartItemDao.selectByPrimaryKey(cartitemId);
        if(!appCartItem.getCommodityId().equals(commodityId)){
            return null;
        }
        Account account = accountDao.findAccountByUserId(appCartItem.getUserId());
        appCartItem.setSupplierName(account.getProviderName());
        appCartItem.copySupplierCommodity(supplierCommodity);
        appCartItem.copyCommodityOutput(commodityOutput);

        //返回更新后的信息
        if(appCartItem.getInventory() - number >= 0){//有库存
            appCartItem.setCount(number);
            appCartItem.setUpdatedAt(new Date());
            cartItemDao.updateByPrimaryKeySelective(appCartItem);
            computeRemainig(appCartItem);//计算是否有库存字段
        }else{
            appCartItem.setRemaining(false);
        }
        return appCartItem;
    }


    @Override
    public List<AppCartItemOut> findCartItemsByUserId(Long userId) {
        /**
         * 业务逻辑：
         * 1. 根据用户id查询出购物车表中的信息
         * 2. 根据商品id去商品表中查询相应的商品信息
         */
        List<AppCartItem> list = cartItemDao.findCartItemByUserId(userId);
        List<AppCartItemOut> appCartItemOuts = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            AppCartItem appCartItem = list.get(i);
            //supplierCommodityDao没有提供批量查询的方法，这里后期会影响性能
            SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(appCartItem.getCommodityId());//
            CommodityOutput commodityOutput = supplierCommodityDao.findDetail(appCartItem.getCommodityId());
            Account account = accountDao.findAccountByUserId(appCartItem.getUserId());
            appCartItem.setSupplierName(account.getProviderName());
            appCartItem.copySupplierCommodity(supplierCommodity);
            appCartItem.copyCommodityOutput(commodityOutput);
            computeRemainig(appCartItem);
            logger.debug("【购物车信息1】" +appCartItem);

            //重新组装数据格式
            AppCartItemOutSub appCartItemOutSub = new AppCartItemOutSub();
            appCartItemOutSub.copyAppCartItem(appCartItem);
            String[] pros =  new String[0];
            if(appCartItemOuts.size()>0){
                for (int j = 0; j < appCartItemOuts.size(); j++) {
                    AppCartItemOut aci = appCartItemOuts.get(j);
                    if(appCartItem.getSupplierId().equals(aci.getSupplierId())){
                        aci.getAppCartItems().add(appCartItemOutSub);
                    }else{
                        AppCartItemOut appCartItemOut = new AppCartItemOut();
                        appCartItemOut.setSupplierId(appCartItem.getSupplierId());
                        appCartItemOut.setSupplierName(appCartItem.getSupplierName());
                        appCartItemOut.getAppCartItems().add(appCartItemOutSub);
                        appCartItemOut.setList(pros);
                        appCartItemOut.setIsSelectShop(false);
                        appCartItemOuts.add(appCartItemOut);

                    }

                }
            }else{
                AppCartItemOut appCartItemOut = new AppCartItemOut();
                appCartItemOut.setSupplierId(appCartItem.getSupplierId());
                appCartItemOut.setSupplierName(appCartItem.getSupplierName());
                appCartItemOut.getAppCartItems().add(appCartItemOutSub);
                appCartItemOut.setList(pros);
                appCartItemOut.setIsSelectShop(false);
                appCartItemOuts.add(appCartItemOut);
            }

            logger.debug("【购物车信息2】" +appCartItemOuts);
        }



        return appCartItemOuts;
    }

    /**
     * 得到商品信息
     * @return
     * @param cid 商品id
     */
    private SupplierCommodity checkStore(Long cid) {
        return supplierCommodityDao.findOne(cid);
    }

    /**
     * 计算库存判断字段
     * @param appCartItem
     */
    private void computeRemainig(AppCartItem appCartItem){
        if(appCartItem.getInventory() == null){
            appCartItem.setRemaining(false);
            return;
        }

        if(appCartItem.getCount()>appCartItem.getInventory()){
            appCartItem.setRemaining(false);
        }else{
            appCartItem.setRemaining(true);
        }
    }

    /**
     * 转换对象
     * @param appCartItemInput
     * @return
     */
    private AppCartItem convertToCartItem(AppCartItemInput appCartItemInput) {
        if(appCartItemInput ==null){
            return null;
        }
        AppCartItem appCartItem = new AppCartItem();

        appCartItem.setUserId(appCartItemInput.getUserId());
        appCartItem.setSupplierId(appCartItemInput.getSupplierId());
        appCartItem.setSupplierName(appCartItemInput.getSupplierName());

        appCartItem.setCommodityId(appCartItemInput.getCommodityId());
        appCartItem.setCommodityPrice(appCartItemInput.getCommodityPrice());
        appCartItem.setCommodityName(appCartItemInput.getCommodityName());
        appCartItem.setCommodityPic(appCartItemInput.getCommodityPic());
        appCartItem.setCommodityProperties(appCartItemInput.getCommodityProperties());
        appCartItem.setCount(appCartItemInput.getCount());
        return appCartItem;
    }
}
