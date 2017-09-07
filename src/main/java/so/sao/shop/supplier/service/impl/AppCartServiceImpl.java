package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.dao.AppCartItemDao;
import so.sao.shop.supplier.dao.SupplierCommodityDao;
import so.sao.shop.supplier.domain.AppCartItem;
import so.sao.shop.supplier.domain.SupplierCommodity;
import so.sao.shop.supplier.pojo.input.AppCartItemInput;
import so.sao.shop.supplier.service.AppCartService;

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


    public static final Integer MORE_REMAINING = -1;//大于库存
    public static final Integer EQUALS_REMAINING = 0;//等于库存



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
        List<AppCartItem> existsAppCartItemLit = cartItemDao.findExistsCartItem(appCartItem.getUserId(), appCartItem.getSupplierId(), appCartItem.getCommodityId());
        logger.debug("【查询是否存在相同的商品】" + existsAppCartItemLit.toString());
        if(existsAppCartItemLit !=null&& existsAppCartItemLit.size()>0){//如果存在，则更新商品数量

            AppCartItem existsAppCartItem = existsAppCartItemLit.get(0);
            Integer updateNumber = existsAppCartItem.getCount()+ appCartItemInput.getCount();
            logger.debug("【需要更新的数量】" + updateNumber);
            //更新记录信息并返回商品库存信息
            Integer remaining = updateCartItem(existsAppCartItem.getId(), existsAppCartItem.getCommodityId(),updateNumber);
            logger.debug("【更新后库存余量】" + remaining);
            return (remaining-updateNumber) >= 0 ? true : false;
        }else{//如果不存在相同的商品
            //查询商品库存
            SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(appCartItem.getCommodityId());
            Double inventory = supplierCommodity.getInventory();
            //判断添加的数量是否超过商品库存
            if((inventory.intValue()- appCartItem.getCount()) >= 0){
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

        PageHelper.startPage(pageNum,pageSize);
        List<AppCartItem> list = cartItemDao.findCartItemByUserId(userId);

        for (int i = 0; i < list.size(); i++) {
            AppCartItem appCartItem = list.get(i);
            Long cid =  appCartItem.getCommodityId();
            //查询商品是否还有库存
            appCartItem.setInventory(checkStore(cid).getInventory());
            logger.debug("【插入购物车信息】" + appCartItem.getInventory());
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
    public Integer updateCartItem(Long cartitemId,Long commodityId,Integer number) {

        //判断库存余量
        SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(commodityId);
        Double inventory = supplierCommodity.getInventory();
        logger.debug("【库存余量】" + inventory);
        if(number == null || number.intValue() < 1){
            return inventory.intValue();
        }

        if(inventory.doubleValue() > 0){//有库存
            Integer remaining = inventory.intValue();
            logger.debug("【更新的数量】" + number);
            logger.debug("【剩余的库存数量】" + remaining);
            if(number > remaining){//购物车更新数大于库存数
                return MORE_REMAINING;
            }
            //购物车数量小于或等于库存数量，更新购物车数量
            AppCartItem appCartItem = new AppCartItem();
            appCartItem.setId(cartitemId);
            appCartItem.setCount(number);
            appCartItem.setUpdatedAt(new Date());
            logger.debug("【更新的数据为】" + appCartItem);
            cartItemDao.updateByPrimaryKeySelective(appCartItem);
            return remaining;
        }else{
            return MORE_REMAINING;
        }
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
