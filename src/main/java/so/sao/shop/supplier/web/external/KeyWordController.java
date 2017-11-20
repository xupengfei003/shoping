package so.sao.shop.supplier.web.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.domain.external.KeyWord;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.external.KeyWordService;

import java.util.List;

/**
 * 搜素关键字配置controller
 */
@RestController
@RequestMapping("/external/keyWords")
public class KeyWordController {

    @Autowired
    private KeyWordService keyWordService;

    @PostMapping(value="/create")
    public Result create(@RequestBody List<KeyWord> keyWords) {

        return keyWordService.saveKeyWord(keyWords);
    }
    @PutMapping(value="/update")
    public Result update(@RequestBody KeyWord keyWord) {

        return keyWordService.updateKeyWord(keyWord);
    }
    @GetMapping(value="/searchAll")
    public Result searchAll(@RequestParam(required = false) Integer pageNum,@RequestParam(required = false) Integer pageSize) {

        return keyWordService.searchKeyWords(pageNum,pageSize);
    }
}
