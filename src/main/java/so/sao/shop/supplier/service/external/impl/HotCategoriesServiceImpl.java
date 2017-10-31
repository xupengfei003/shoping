package so.sao.shop.supplier.service.external.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.config.azure.AzureBlobService;
import so.sao.shop.supplier.dao.external.HotCategoriesDao;
import so.sao.shop.supplier.domain.external.HotCategories;
import so.sao.shop.supplier.exception.BusinessException;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.vo.CommBlobUpload;
import so.sao.shop.supplier.service.external.HotCategoriesService;
import so.sao.shop.supplier.util.Ognl;
import so.sao.shop.supplier.util.PageTool;

import java.util.*;

/**
 * 热门分类配置service
 * Created by LiuGang at 2017/9/18
 */
@Service
public class HotCategoriesServiceImpl implements HotCategoriesService {

    @Autowired
    private HotCategoriesDao hotCategoriesDao;
    @Autowired
    private AzureBlobService azureBlobService;
    /**
     * 查询热门分类集合
     *
     * @param pageNum  当前页号
     * @param pageSize 页面大小/记录条数
     * @return Result
     */
    @Override
    public Result searchHotCategories(Integer pageNum, Integer pageSize) {
        //开始分页
        PageTool.startPage(pageNum, pageSize);
        List<HotCategories> hotCategories = hotCategoriesDao.find();
        //将查询列表放置到分页对象中
        PageInfo pageInfo = new PageInfo(hotCategories);
        if (hotCategories != null && hotCategories.size() > 0)
        {
            return Result.success("查询成功",pageInfo);
        }else{
            return Result.fail("查无数据");
        }
    }

    /**
     * 更换热门分类图片
     *
     * @param file 分类图片
     * @return Result
     */
    @Override
    public Result updateIcon(MultipartFile file) {

        //文件校验
        if (file == null) {
            return Result.fail("上传文件为空");
        }

        MultipartFile[] files = {file};
        List<CommBlobUpload> blobUploadList = new ArrayList<CommBlobUpload>();
        //调用上传图片接口
        Result result = azureBlobService.uploadFilesComm(files, blobUploadList);
        if(blobUploadList.isEmpty()){
            return result;
        }else{
            return Result.success("更换icon成功", blobUploadList.get(0));
        }
    }

    /**
     * 批量添加热门分类
     *
     * @param hotCategories 热门分类集合
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveHotCategories(List<HotCategories> hotCategories) {

        //校验传入的集合是否为空
        if (hotCategories.isEmpty()) {
            return Result.fail("添加热门分类为空");
        }
        //校验必填字段
        checkHotCategories(hotCategories);
        //重复热门分类校验
        checkHotCategoriesId(hotCategories);
        //清空原有热门分类数据表数据
        hotCategoriesDao.deleteHotCategoriesData();
        int sort = 1;
        for (HotCategories hotCategory : hotCategories){
            hotCategory.setSort(sort++);
            hotCategory.setCreateAt(new Date());
            hotCategory.setUpdateAt(new Date());
        }

        //将数据保存到数据表中
        hotCategoriesDao.saveBatch(hotCategories);
        return Result.success("添加热门分类成功");
    }

    /**
     * 校验热门分类列表必填字段
     * @param hotCategories
     */
    private void checkHotCategories(List<HotCategories> hotCategories) {

        //除更多分类以外热门分类必填校验
        for (int i = 0; i < hotCategories.size() - 1; i++) {
            //一级分类不能为空
            if (hotCategories.get(i).getCategoryOneId() == null ||hotCategories.get(i).getCategoryOneId().equals(0)){
                throw new BusinessException("一级分类不能为空");
            }else{
                if (Ognl.isEmpty(hotCategories.get(i).getUrl()) || Ognl.isEmpty(hotCategories.get(i).getMinImg())){
                    throw  new BusinessException("icon图片不能为空");
                }
            }
        }
        //获取更多分类项的url和minImg,检验是否为空
        String url = hotCategories.get(hotCategories.size()-1).getUrl();
        String minImg = hotCategories.get(hotCategories.size()-1).getMinImg();
        if (Ognl.isEmpty(url) || Ognl.isEmpty(minImg)){
            throw new BusinessException("更多分类型的icon图片url不能为空");
        }
    }

    /**
     * 校验传入的集合中是否有重复的分类
     * @param hotCategories
     */
    private void checkHotCategoriesId(List<HotCategories> hotCategories){
        //treeSet集合用于存放分类id(一级/二级/三级分类的id)
        Set<Long> categoryIds = new TreeSet<>();
        //除更多分类外，若三级分类为空（id==null/0,name为空/空串，判断二级分类是否为空，为空则id为一级分类id
        for (int i = 0 ; i < hotCategories.size() - 1 ; i++){
            if (hotCategories.get(i).getCategoryThreeId()==null||hotCategories.get(i).getCategoryThreeId().equals(0L)){
                if (hotCategories.get(i).getCategoryTwoId()==null||hotCategories.get(i).getCategoryTwoId().equals(0L)){
                    categoryIds.add(hotCategories.get(i).getCategoryOneId());
                }else {
                    categoryIds.add(hotCategories.get(i).getCategoryTwoId());
                }
            } else {
                categoryIds.add(hotCategories.get(i).getCategoryThreeId());
            }
        }
        if (categoryIds.size() < (hotCategories.size()-1)){
            throw new BusinessException("存在重复分类，请检查修改后添加！");
        }
    }
}
