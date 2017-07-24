package com.sao.so.supplier.service.impl;

import com.sao.so.supplier.config.Constant;
import com.sao.so.supplier.dao.CommCategoryDao;
import com.sao.so.supplier.domain.CommCategory;
import com.sao.so.supplier.pojo.BaseResult;
import com.sao.so.supplier.pojo.input.CommCategoryInput;
import com.sao.so.supplier.pojo.input.CommCategoryUpdateInput;
import com.sao.so.supplier.pojo.output.CommCategorySelectOutput;
import com.sao.so.supplier.service.CommCategoryService;
import com.sao.so.supplier.util.BaseResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
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
     * 新增加商品品类--ServiceImpl
     * @param commCategoryInput
     * @return
     */
    public BaseResult saveCommCategory(CommCategoryInput commCategoryInput){
        BaseResult result = new BaseResult();
        CommCategory commCategory =commCategoryDao.findCommCategoryByName(commCategoryInput.getName());
        //判断需要新增的类型名称是否已经存在
        if(null == commCategory){
            CommCategory commCate = new CommCategory();
            //如果新增的类型没有PID,那就让PID==0
            if(null == commCategoryInput.getPid()){
                commCate.setPid(0L);
            }else {
                commCate.setPid(commCategoryInput.getPid());
            }
            commCate.setName(commCategoryInput.getName());
            commCate.setSort(commCategoryInput.getSort());
            commCate.setRemark(commCategoryInput.getRemark());
            if(null == commCategoryInput.getPid() || 0==commCategoryInput.getPid()){
                commCate.setLevel(1);
            }else {
                //需要新增的PID有数值时候，需要查询id = Pid的商品类型的level是多少，最后返回level+1
                Integer level = commCategoryDao.findCommCategoryLevelByPid(commCategoryInput.getPid());
                commCate.setLevel(level+1);
            }
            commCate.setDeleted(0);
            commCate.setCreatedAt(new Date().getTime());
            commCate.setUpdatedAt(new Date().getTime());
            boolean flag = commCategoryDao.save(commCate);
            return BaseResultUtil.transTo(flag,"添加商品类型成功","添加商品类型失败");
        }else {
            result.setCode(com.sao.so.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("你输入的商品类型名称已经存在了，请重新定义！");
            return result;
        }
    }
    @Override
    public BaseResult deleteCommCategory(Long id) {
        boolean flag = commCategoryDao.deleteById(id);
        return BaseResultUtil.transTo(flag,"删除商品类型成功","删除商品类型失败");
    }

    /**
     * 根据ID修改商品类型信息
     * @param commCategoryUpdateInput 商品类型对象入参
     * @return
     */
 
	@Override
    public BaseResult updateCommCategory(CommCategoryUpdateInput commCategoryUpdateInput) {
	    BaseResult result = new BaseResult();
        CommCategory one = commCategoryDao.findOne(commCategoryUpdateInput.getId());
        //修改前再次验证ID是否存在
        if(null != one){
            CommCategory commCategory = new CommCategory();
            commCategory.setId(commCategoryUpdateInput.getId());
            commCategory.setName(commCategoryUpdateInput.getName());
            commCategory.setRemark(commCategoryUpdateInput.getRemark());
            commCategory.setSort(commCategoryUpdateInput.getSort());
            commCategory.setUpdatedAt(new Date().getTime());
            boolean flag = commCategoryDao.update(commCategory);
            return BaseResultUtil.transTo(flag,"更新商品类型成功","更新商品类型失败");
        }else {
            result.setCode(Constant.CodeConfig.CODE_NOT_FOUND_RESULT);
            result.setMessage("你选择修改的商品类型不存在，请重新选择");
            return result;
        }
    }

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
        for (CommCategory commCategory : tempList) {
            CommCategorySelectOutput commCategorySelectOutput = new CommCategorySelectOutput();
            commCategorySelectOutput.setId(commCategory.getId());
            commCategorySelectOutput.setName(commCategory.getName());
            list.add(commCategorySelectOutput);
        }
        return list;

    }
}
