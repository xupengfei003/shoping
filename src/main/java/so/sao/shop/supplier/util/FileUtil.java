package so.sao.shop.supplier.util;

import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.apache.commons.lang3.StringUtils;
import so.sao.shop.supplier.config.CommConstant;
import so.sao.shop.supplier.config.StorageConfig;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.vo.CommBlobUpload;
import so.sao.shop.supplier.web.UploadController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.*;


public class FileUtil {

	/**
	 * 创建目录
	 * @param destDirName  目标目录
	 * @return 目录创建成功返回true，否则返回false
	 */
	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		// 创建单个目录
		if (dir.mkdirs()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除单个文件
	 * @param   sPath    被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * @param   sPath 被删除目录的文件路径
	 * @return  目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		//如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		//如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean  flag = true;
		//删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			//删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) break;
			} //删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag) break;
			}
		}
		if (!flag) return false;
		//删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 递归查询文件夹
	 * sPath
	 */
	public static void getreNameFile(String sPath,String fjjname) {
		File f=new File(sPath);
		File[] tlist = f.listFiles();
		for (int i = 0; i < tlist.length; i++) {
			if(tlist[i].isDirectory()){//如果是文件夹，则修改文件夹名称
				File srcDir = new File(sPath+"/"+tlist[i].getName());
				boolean isOk = srcDir.renameTo(new File(sPath+"/"+fjjname));
				getreNameFile(sPath+"/"+fjjname, fjjname);
			}
		}
	}

	/**
	 * 将二维码图片上传至云端
	 *
	 * @param path 图片路径
	 * @param qrcodeName 图片名称
	 * @param storageConfig 配置
	 * @return
	 */
	public Result uploadQrcode(String  path , String qrcodeName, StorageConfig storageConfig) {
		Map<String, String> map = new HashMap<>();

		if (Ognl.isEmpty(qrcodeName)) {
			map.put("错误原因：", "未选择上传的文件");
			Result resultMsg = new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, "请选择需要上传的文件", map);
			return resultMsg;
		}

		CommBlobUpload blobUploadEntity = null;
		try {
			//获取或创建container
			CloudBlobContainer blobContainer = BlobHelper.getBlobContainer(CommConstant.AZURE_CONTAINER.toLowerCase(), storageConfig);
			File file = new File(path + "/" + qrcodeName.trim());
			String fileName = file.getName();

			if (!file.exists()) {
				map.put("fileName", fileName);
				Result resultMsg = new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, "上传文件为空", map);
				return resultMsg;
			}

			try {
				//获取上传文件的名称及文件类型
				blobUploadEntity = new CommBlobUpload();

				String extensionName = StringUtils.substringAfter(fileName, ".");

				//过滤非jpg,png,jpeg,gif格式的文件
				if (!(fileName.endsWith(".jpg")
						|| fileName.endsWith(".jpeg")
						|| fileName.endsWith(".png")
						|| fileName.endsWith(".gif"))) {
					map.put("fileName", fileName);
					Result resultMsg = new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, "上传的文件中包含非jpg/png/jpeg/gif格式", map);
					return resultMsg;
				}

				//拼装blob的名称(新的图片文件名 =UUID+"."图片扩展名)
				String newFileName = UUID.randomUUID() + "." + extensionName;
				String preName = UploadController.getBlobPreName(extensionName, false).toLowerCase();
				String blobName = preName + newFileName;

				//设置文件类型，并且上传到azure blob
				CloudBlockBlob blob = blobContainer.getBlockBlobReference(blobName);
				blob.getProperties().setContentType(fileName.substring(fileName.lastIndexOf("."), fileName.length()));
				blob.upload(new FileInputStream(file), file.length());

				//将上传后的图片URL返
				blobUploadEntity.setFileName(fileName);
				blobUploadEntity.setUrl(blob.getUri().toString());
				blobUploadEntity.setType(extensionName);
				try {
					BufferedImage sourceImg = ImageIO.read(new FileInputStream(file));
					blobUploadEntity.setSize(sourceImg.getWidth() + "*" + sourceImg.getHeight());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				map.put("fileName:", fileName);
				Result resultMsg = new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, "文件上传异常", map);
				return resultMsg;
			}
		} catch (Exception e) {
			map.put("错误原因：", "文件上传异常");
			Result resultMsg = new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, "文件上传异常", map);
			return resultMsg;
		}

		Result result = new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS, "文件上传成功", blobUploadEntity);

		return result;
	}




}