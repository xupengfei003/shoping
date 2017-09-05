package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.CommCategoryDao;
import so.sao.shop.supplier.domain.CommCategory;
import so.sao.shop.supplier.pojo.output.CommCategorySelectOutput;
import so.sao.shop.supplier.service.CommCategoryService;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品品类维护Service
 * Created by QuJunLong on 2017/7/18.
 */
@Service
public class CommCategoryServiceImpl implements CommCategoryService {

    @Autowired
    private CommCategoryDao commCategoryDao;

    /**
     * 查询商品品类列表；
     * @param pid
     * @return CommCategorySelectOutput
     */
    @Override
    public List<CommCategorySelectOutput> searchCommCategory(Long pid) {
        List<CommCategorySelectOutput> list = new ArrayList<>();
        //将CommCategory遍历添加到一个新数组里面，新数组存放需要的对象属性值
        List<CommCategory> tempList = commCategoryDao.find(pid);
        tempList.forEach( commCategory ->{
            CommCategorySelectOutput commCategorySelectOutput = new CommCategorySelectOutput();
            commCategorySelectOutput.setId(commCategory.getId());
            commCategorySelectOutput.setName(commCategory.getName());
            list.add(commCategorySelectOutput);
        });
        return list;

    }
}
