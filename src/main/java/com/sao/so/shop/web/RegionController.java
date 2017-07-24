package com.sao.so.shop.web;


import com.sao.so.shop.pojo.output.RegionOutput;
import com.sao.so.shop.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 行政区字典表 前端控制器
 * </p>
 *
 * @author zhangruibing
 * @since 2017-07-17
 */
@Api(value = "RegionController", tags = "行政区域")
@RestController
public class RegionController {

    @Autowired
    private RegionService regionService;

    /**
     * 获取行政区域列表
     * @param parentId 父id
     * @param level 级别
     * @return
     */
    @ApiOperation(value = "获取行政区域列表", notes = "获取行政区域列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "parentId", name = "parentId", required = true, paramType = "query"),
            @ApiImplicitParam(value = "level", name = "level", paramType = "query")
    })
    @GetMapping(value = "/regions")
    public RegionOutput getRegion(Integer parentId, Integer level) {
        return regionService.getListForRegion(parentId, level);
    }
}
