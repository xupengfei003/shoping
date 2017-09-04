package so.sao.shop.supplier.service;

import com.google.zxing.Result;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 二维码相关API
 *
 * @author hengle.yang
 * @since 2017/8/11 17:27
 */
public interface QrcodeService {

    /**
     * 生成包含字符串信息的二维码图片
     *
     * @param outputStream 文件输出流路径
     * @param content      二维码携带信息
     * @param qrCodeSize   二维码图片大小
     * @param imageFormat  二维码的格式
     * @throws IOException io异常
     * @throws WriterException 编码二维码时的异常
     * @return 成功返回true，失败返回false
     */
    boolean createQrCode(OutputStream outputStream, String content, int qrCodeSize, String imageFormat) throws WriterException, IOException;

    /**
     * 读二维码并输出携带的信息
     *
     * @param inputStream 二维码文件输入流
     * @return 返回Result对象，封装了二维码携带的信息
     * @throws IOException io异常
     */
    Result readQrCode(InputStream inputStream)throws IOException;
}
