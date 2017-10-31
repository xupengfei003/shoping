package so.sao.shop.supplier.service.external.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.external.KeyWordDao;
import so.sao.shop.supplier.domain.external.KeyWord;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.external.KeyWordService;
import so.sao.shop.supplier.util.PageTool;

import java.util.*;

/**
 * <p>Version: 运维平台 V0.9.0 </p>
 * <p>Title: InvoiceContent</p>
 * <p>Description:运维平台-关键字配置serviceImpl</p>
 *
 * @author: sha.chen
 * @Date: Created in 2017/10/30 15:00
 */
@Service
public class KeyWordServiceImpl implements KeyWordService {

    @Autowired
    private KeyWordDao keyWordDao;

    /**
     * 添加关键字列表到数据库
     * @param keyWords KeyWord对象集合
     * @return
     */
    @Override
    public Result saveKeyWord(List<KeyWord> keyWords) {
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

    /**
     * 编辑关键字名称
     * @param keyWord 关键字
     * @return
     */
    @Override
    public Result updateKeyWord(KeyWord keyWord) {
        int count = keyWordDao.countByKeyWordValue(keyWord.getKeyWordValue());
        if(count>0){
            return Result.fail("关键词名称已存在！");
        } else {
            keyWord.setUpdateAt(new Date());
            keyWordDao.update(keyWord);
            return Result.success("编辑关键词名称成功！");
        }
    }


    /**
     * 查看关键字列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Result searchKeyWords(Integer pageNum, Integer pageSize) {

        PageTool.startPage(pageNum,pageSize);
        List<KeyWord> list = keyWordDao.findAll();
        PageInfo<KeyWord> pageInfo = new PageInfo<>(list);
        return Result.success("查询列表成功",pageInfo);
    }

    /**
     * 传入的keyWords集合合法性校验
     * @param keyWords
     * @return
     */
    public Result checkKeyWords(List<KeyWord> keyWords){
        //校验传入的集合是否为空
        if (keyWords.isEmpty()) {
            return Result.fail("关键字列表为空");
        } else {
            List<String> keyVuls = new ArrayList<>();
            for(KeyWord k : keyWords){
                if(k.getKeyWordValue()!=null){
                    keyVuls.add(k.getKeyWordValue());
                }
            }
            //校验关键字名称是否重复
            Set<String> set = new TreeSet<String>();
            for (int i=0;i<keyVuls.size();i++){
                set.add(keyVuls.get(i));
            }
            if (keyVuls.size()>set.size()){
                return Result.fail("关键字重复");
            }
        }
        return null;
    }

}
