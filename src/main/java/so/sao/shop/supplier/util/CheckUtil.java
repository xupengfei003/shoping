package so.sao.shop.supplier.util;

import so.sao.shop.supplier.domain.User;

import javax.servlet.http.HttpServletRequest;

public class CheckUtil {
    public static Long supplierIdCheck(HttpServletRequest request, Long supplierId) throws Exception {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(supplierId == null||supplierId == 0){
            supplierId = user.getAccountId();
        }else if (supplierId.equals(user.getAccountId())){
            return supplierId;
        }else {
            if (!(Constant.IS_ADMIN.equals(user.getIsAdmin()))){
                throw  new RuntimeException("非管理员无权进行操作");
            }
        }
        return supplierId;
    }

}
