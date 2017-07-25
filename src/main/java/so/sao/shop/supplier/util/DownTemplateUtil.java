package so.sao.shop.supplier.util;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

/**
 * Created by acer on 2017/7/25.
 */
public class DownTemplateUtil {

    public static void downLoadExcel(HttpServletRequest request, HttpServletResponse response, String templateName, String  fileName) throws IOException {
        URL save = Thread.currentThread().getContextClassLoader().getResource("");
        String str = save.toString();
        str = str.substring(6, str.length());
        str = str.replaceAll("%20", " ");
        int num = str.lastIndexOf("shop");//supplier 为项目名，应用到不同的项目中，这个需要修改！
        str = str.substring(0, num + "shop".length());
        str = str + "/file/"+templateName;//Excel模板所在的路径。
        File f = new File(str);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
       // String filename = "供应商信息.xlsx";
        fileName = new String(fileName.getBytes("Utf-8"), "iso-8859-1");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/octet-stream");
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(f));
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }

}


