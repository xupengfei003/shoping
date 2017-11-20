package so.sao.shop.supplier.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.domain.DictItem;
import so.sao.shop.supplier.service.DictService;

import java.util.List;

/**
 * Created by xujc on 2017/7/18.
 */
@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    /**
     * 获取快递列表
     *
     * @return
     */
    @GetMapping("/searchExpress")
    public List<DictItem> searchExpress() throws Exception {
        return dictService.selectExpress();
    }
}
