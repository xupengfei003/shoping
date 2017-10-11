package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.service.QualificationService;

/**
 * 供应商资质Controller
 */
@RestController
@RequestMapping("/account/qualification")
@Api(description = "供应商资质相关接口")
public class QualificationController {

    @Autowired
    private QualificationService qualificationService;
    
}
