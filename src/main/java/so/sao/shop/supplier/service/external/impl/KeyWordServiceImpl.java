package so.sao.shop.supplier.service.external.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.external.KeyWordDao;
import so.sao.shop.supplier.domain.external.KeyWord;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.external.KeyWordService;
import so.sao.shop.supplier.util.PageTool;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 搜素关键字Service实现类
 */
@Service
public class KeyWordServiceImpl implements KeyWordService{

    @Autowired
    private KeyWordDao keyWordDao;

    @Override
    public Result saveKeyWord(List<KeyWord> keyWords) {
     /*   //校验传入的集合是否为空
        if (keyWords.isEmpty()) {
           return Result.fail("添加热门分类为空");
        }

        //校验关键字名称是否重复
        Set<String> set = new TreeSet<String>();
        for (int i=0;i<keyWords.size();i++){
            set.add(keyWords.get(i).getKeyWordValue());
        }
        if (keyWords.size()>set.size()){
            return Result.fail("关键字重复");
        }*/

        //入参合法性校验
        Result result = checkKeyWords(keyWords);
        if (null != result){
            return result;
        }


        //清空原数据表
        keyWordDao.delete();
        //设置排序及时间
        int sort = 1;
        for(KeyWord keyWord:keyWords){
            keyWord.setSort(sort++);
            keyWord.setCreateAt(new Date());
            keyWord.setUpdateAt(new Date());
        }
        //数据保存到数据库
        keyWordDao.save(keyWords);
        return Result.success("添加关键字列表成功");
    }

    @Override
    public Result updateKeyWord(Long id, String keyWordValue) {
        int count = keyWordDao.countByKeyWordValue(keyWordValue);
        if(count>0){
            return Result.fail("关键词名称已存在！");
        } else {
            keyWordDao.update(id, keyWordValue);
            return Result.success("编辑关键词名称成功！");
        }
    }


    @Override
    public Result searchKeyWords(Integer pageNum,Integer pageSize) {

        PageTool.startPage(pageNum,pageSize);
        List<KeyWord> list = keyWordDao.findAll();
        PageInfo<KeyWord> pageInfo = new PageInfo<>(list);
        return Result.success("查询列表成功",pageInfo);
    }

    /**
     * keyWords集合合法性校验
     * @param keyWords
     * @return
     */
    public Result checkKeyWords(List<KeyWord> keyWords){
        //校验传入的集合是否为空
        if (keyWords.isEmpty()) {
            return Result.fail("添加热门分类为空");
        } else {
            //校验关键字名称是否重复
            Set<String> set = new TreeSet<String>();
            for (int i=0;i<keyWords.size();i++){
                set.add(keyWords.get(i).getKeyWordValue());
            }
            if (keyWords.size()>set.size()){
                return Result.fail("关键字重复");
            }
            return null;
        }
    }

}
