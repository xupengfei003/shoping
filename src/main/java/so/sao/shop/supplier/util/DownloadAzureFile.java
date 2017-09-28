package so.sao.shop.supplier.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import so.sao.shop.supplier.pojo.Result;
/**
 * <p>Title: 云端文件下载转换</p>
 * <p>Description: 云端链接不能直接下载txt、pdf、wps</p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author clown
 * @date 2017年8月28日
 */
public class DownloadAzureFile {
	
	/**
	 * 初始化日志
	 */
	private final static Logger logger = LoggerFactory.getLogger(DownloadAzureFile.class);
	
	/**
	 * 云端文件下载转换
	 * @param downloadUrl 云端地址
	 * @param realFileName 文件名称
	 * @param request 
	 * @param response
	 * @return
	 */
	public static Result downloadFile(String downloadUrl, String realFileName, HttpServletRequest request, HttpServletResponse response) throws Exception{
		response.setContentType("text/html;charset=UTF-8");  
        try {  
        	request.setCharacterEncoding("UTF-8");  
        	  
            BufferedInputStream bis = null;  
            BufferedOutputStream bos = null;  
            //此处使用的配置文件里面取出的文件服务器地址,拼凑成完整的文件服务器上的文件路径  
                        //写demo时，可以直接写成http://xxx/xx/xx.txt.这种形式  
  
            response.setContentType("application/octet-stream");  
            response.reset();//清除response中的缓存  
            //根据网络文件地址创建URL  
            URL url = new URL(downloadUrl);  
            //获取此路径的连接  
            URLConnection conn = url.openConnection();  
  
            Long fileLength = conn.getContentLengthLong();//获取文件大小  
            //设置reponse响应头，真实文件名重命名，就是在这里设置，设置编码
            String new_filename = URLEncoder.encode(realFileName, "UTF8");
            //response.setHeader("Content-disposition", "attachment; filename=" + new String(realFileName.split("-_-")[0].getBytes("utf-8"), "ISO8859-1"));  
            response.setHeader("Content-disposition", "attachment; filename=" + new String(new_filename.getBytes("utf-8"),"ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));  
  
            bis = new BufferedInputStream(conn.getInputStream());//构造读取流  
            bos = new BufferedOutputStream(response.getOutputStream());//构造输出流  
            byte[] buff = new byte[1024];  
            int bytesRead;  
            //每次读取缓存大小的流，写到输出流  
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
                bos.write(buff, 0, bytesRead);  
            }  
            response.flushBuffer();//将所有的读取的流返回给客户端  
            bis.close();  
            bos.close();    
        } catch (IOException e) {  
        	logger.error(e.getMessage(), e);  
        	throw new Exception(e.getMessage());  
        }  
        return Result.success("文件下载成功");
    }  
}
