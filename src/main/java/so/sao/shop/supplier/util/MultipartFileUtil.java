package so.sao.shop.supplier.util;

import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bzh on 2017/9/14.
 */
public class MultipartFileUtil {
    /**
     * 将base64编码字符串转换为图片
     *
     * @param imgStr base64编码字符串
     * @return map 封装了图片相关信息
     */
    public static Map generateImage(String imgStr) throws Exception {
        String resultStr = imgStr.substring(imgStr.lastIndexOf(",")+1);
        Map result = new HashMap(); // 封装返回结果
        if (resultStr == null) {
            return result;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        //解密
        byte[] b = decoder.decodeBuffer(resultStr);
        //处理数据
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        // 生成图片路径(如果路径为空则生成)
        String path = "/image/" + System.currentTimeMillis();
        FileUtil.createDir(path);
        // 生成图片名称
        String name = NumberGenerate.generateId() + System.currentTimeMillis() + ".png";
        String img = path + "/" + name; // 图片相对地址
        OutputStream out = new FileOutputStream(new File(img));
        out.write(b);
        out.flush();
        out.close();
        result.put("path", path); // 图片路径
        result.put("name", name); // 图片名称
        result.put("img", img); // 图片路径 + 名称
        return result;
    }
}
