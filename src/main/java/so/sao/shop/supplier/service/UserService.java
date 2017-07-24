package so.sao.shop.supplier.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.UserDao;

/**
 * 业务层
 *
 * @author
 * @create 2017-07-07 20:26
 **/
@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public PageInfo searchSysUser1(Integer pageNum, Integer pageSize) {
        pageNum = pageNum == null ? 1 : pageNum;
        pageSize = pageSize == null ? 10 : pageSize;
        Page page = PageHelper.startPage(pageNum, pageSize);
        PageInfo pageInfo = new PageInfo(userDao.findPage(page),userDao.countPage(page));
        return pageInfo;
    }
}
