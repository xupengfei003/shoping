package so.sao.shop.supplier.alipay;

import net.sf.json.JSONObject;

/**
 * @author gxy on 2017/9/15.
 */
public class JsonUtils {

    public static String convertToString(AlipayRefundInfo alidata) {

        JSONObject json = JSONObject.fromObject(alidata);
        return json.toString();
    }
}
