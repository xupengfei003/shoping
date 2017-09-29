package so.sao.shop.supplier.pojo.job;

import java.math.BigDecimal;
import java.util.List;

public class ProInfoRespOutput {

    /**
     * 主圖連結
     */
    private String pimg_url;

    /**
     * 分類ID
     */
    private Long cat_id;

    /**
     * 供應商公司ID
     */
    private Long sup_co_id;

    /**
     * 商品名稱
     */
    private String prod_name;

    /**
     * 透雲分類ID (即後台分類設定的自訂ID)
     */
    private Long sao_cat_id;

    /**
     * 商品ID
     */
    private Long prod_id;

    /**
     * 商品市售價格
     */
    private BigDecimal comm_price;

    /**
     * 商品銷售價格
     */
    private BigDecimal price;

    /**
     * 頁籤
     */
    private List<ProInfoRespTabsOutput> tabs;

    /**
     * 分類名稱
     */
    private String cat_name;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 图片
     */
    private List<ProInfoRespImagesOutput> images;

    /**
     * 商品條碼
     */
    private String barcode;

    public String getPimg_url() {
        return pimg_url;
    }

    public void setPimg_url(String pimg_url) {
        this.pimg_url = pimg_url;
    }

    public Long getCat_id() {
        return cat_id;
    }

    public void setCat_id(Long cat_id) {
        this.cat_id = cat_id;
    }

    public Long getSup_co_id() {
        return sup_co_id;
    }

    public void setSup_co_id(Long sup_co_id) {
        this.sup_co_id = sup_co_id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public Long getSao_cat_id() {
        return sao_cat_id;
    }

    public void setSao_cat_id(Long sao_cat_id) {
        this.sao_cat_id = sao_cat_id;
    }

    public Long getProd_id() {
        return prod_id;
    }

    public void setProd_id(Long prod_id) {
        this.prod_id = prod_id;
    }

    public BigDecimal getComm_price() {
        return comm_price;
    }

    public void setComm_price(BigDecimal comm_price) {
        this.comm_price = comm_price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<ProInfoRespTabsOutput> getTabs() {
        return tabs;
    }

    public void setTabs(List<ProInfoRespTabsOutput> tabs) {
        this.tabs = tabs;
    }

    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public List<ProInfoRespImagesOutput> getImages() {
        return images;
    }

    public void setImages(List<ProInfoRespImagesOutput> images) {
        this.images = images;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
