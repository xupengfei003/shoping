package so.sao.shop.supplier.web.external;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.domain.external.KeyWord;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.external.KeyWordService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 搜素关键字配置controller
 */
@RestController
@RequestMapping("/external/keyWords")
@Api(description = "运维-搜索关键字配置接口【责任人：陈沙】")
public class KeyWordController {

    @Autowired
    private KeyWordService keyWordService;

    @ApiOperation(value="添加关键字配置", notes="添加关键字配置")
    @PostMapping(value="/create")
    public Result create(@RequestBody List<KeyWord> keyWords) {

        return keyWordService.saveKeyWord(keyWords);
    }
    @ApiOperation(value="编辑关键字名称", notes="编辑关键字名称")
    @PutMapping(value="/update")
    public Result update(@RequestParam Long id, @RequestParam String keyWordValue) {

        return keyWordService.updateKeyWord(id,keyWordValue);
    }
    @ApiOperation(value="查询所有关键字", notes="查询所有关键字")
    @GetMapping(value="/searchAll")
    public Result searchAll(@RequestParam(required = false) Integer pageNum,@RequestParam(required = false) Integer pageSize) {

        return keyWordService.searchKeyWords(pageNum,pageSize);
    }
}
