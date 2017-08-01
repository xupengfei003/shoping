package so.sao.shop.supplier.util;

import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import org.apache.commons.lang3.StringUtils;
import so.sao.shop.supplier.config.StorageConfig;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.vo.BlobUpload;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.*;
import java.util.List;


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
	 * 删除文件
	 * @param filePathAndName String 文件路径及名称 如c:/fqf.txt
	 * @return boolean
	 */
	public static void delFile(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myDelFile = new File(filePath);
			myDelFile.delete();

		} catch (Exception e) {
			System.out.println("删除文件操作出错");
			e.printStackTrace();

		}

	}

	/**
	 * 读取到字节数组0
	 * @param filePath //路径
	 * @throws IOException
	 */
	public static byte[] getContent(String filePath) throws IOException {
		File file = new File(filePath);
		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			System.out.println("file too big...");
			return null;
		}
		FileInputStream fi = new FileInputStream(file);
		byte[] buffer = new byte[(int) fileSize];
		int offset = 0;
		int numRead = 0;
		while (offset < buffer.length
				&& (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
			offset += numRead;
		}
		// 确保所有数据均被读取
		if (offset != buffer.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}
		fi.close();
		return buffer;
	}

	/**
	 * 读取到字节数组1
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(String filePath) throws IOException {

		File f = new File(filePath);
		if (!f.exists()) {
			throw new FileNotFoundException(filePath);
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream((int) f.length());
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bos.close();
		}
	}

	/**
	 * 读取到字节数组2
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray2(String filePath) throws IOException {

		File f = new File(filePath);
		if (!f.exists()) {
			throw new FileNotFoundException(filePath);
		}

		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
				// do nothing
				// System.out.println("reading");
			}
			return byteBuffer.array();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	  /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
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
    public boolean deleteDirectory(String sPath) {
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
				System.out.println(sPath+"/"+tlist[i].getName());
			    boolean isOk = srcDir.renameTo(new File(sPath+"/"+fjjname)); 
			    System.out.println("==============="+sPath+"/"+fjjname);
			    getreNameFile(sPath+"/"+fjjname, fjjname);
			    System.out.println(isOk);
			}
		}
    }

	/**
	 * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray3(String filePath) throws IOException {

		FileChannel fc = null;
		RandomAccessFile rf = null;
		try {
			rf = new RandomAccessFile(filePath, "r");
			fc = rf.getChannel();
			MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,
					fc.size()).load();
			//System.out.println(byteBuffer.isLoaded());
			byte[] result = new byte[(int) fc.size()];
			if (byteBuffer.remaining() > 0) {
				// System.out.println("remain");
				byteBuffer.get(result, 0, byteBuffer.remaining());
			}
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				rf.close();
				fc.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

   /**
    * 判断文件是否存在
    */
	public static boolean checkFile(String pathfilename){
		boolean flag = false;
		File file=new File(pathfilename);    
		if(file.exists())    
		{    
		    flag = true;   
		}
		return flag;
	}

	/**
	 * 获取文件夹下的所有文件
	 */
	private static void getFile(String path){ 
		 File file = new File(path); 
		 File[] array = file.listFiles();
		 for(int i=0;i<array.length;i++){ 
			 if(array[i].isFile()){ 
				 System.out.println("^^^^^" + array[i].getName());   
			 }else if(array[i].isDirectory()){   
	                getFile(array[i].getPath());   
	         }  
		 }
	}

	/**
	 * 上传文件
	 */
	public  List<Result> UploadFiles(String  realzippath ,String [] files, StorageConfig storageConfig) {
		List<BlobUpload> blobUploadEntities = new ArrayList<BlobUpload>();
		Map<String,String> map = new HashMap<>();
		List<Result> results = new ArrayList<>();
		try {
			if (files.length !=0) {
				//获取或创建container
				CloudBlobContainer blobContainer = BlobHelper.getBlobContainer(Constant.AZURE_CONTAINER.toLowerCase(), storageConfig);
				for (int i = 0; i < files.length; i++) {
					File file = new File(realzippath+"/"+files[i].trim());
					String fileName = file.getName();
					if (file.exists()) {
						try {
							//获取上传文件的名称及文件类型
							BlobUpload blobUploadEntity = new BlobUpload();

							String extensionName = StringUtils.substringAfter(fileName, ".");

							//过滤非jpg,png,jpeg,gif格式的文件
							if (!(fileName.endsWith(".jpg")
									|| fileName.endsWith(".jpeg")
									|| fileName.endsWith(".png")
									|| fileName.endsWith(".gif"))) {
								map.put("fileName",fileName);
								Result resultMsg = new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, "上传的文件中包含非jpg/png/jpeg/gif格式",map);
								results.add(resultMsg);
								continue;
							}

							//拼装blob的名称(新的图片文件名 =UUID+"."图片扩展名)
							String newFileName = UUID.randomUUID() + "." + extensionName;
							String preName = getBlobPreName(extensionName, false).toLowerCase();
							String blobName = preName + newFileName;

							//设置文件类型，并且上传到azure blob
							CloudBlockBlob blob = blobContainer.getBlockBlobReference(blobName);
							blob.getProperties().setContentType(fileName.substring(fileName.lastIndexOf("."),fileName.length()));
							blob.upload(new FileInputStream(file), file.length());

							//生成缩略图，并上传至AzureStorage
							BufferedImage img = new BufferedImage(Constant.THUMBNAIL_DEFAULT_WIDTH, Constant.THUMBNAIL_DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
							img.createGraphics().drawImage(ImageIO.read(new FileInputStream(file)).getScaledInstance(Constant.THUMBNAIL_DEFAULT_WIDTH, Constant.THUMBNAIL_DEFAULT_HEIGHT, Image.SCALE_SMOOTH), 0, 0, null);
							ByteArrayOutputStream thumbnailStream = new ByteArrayOutputStream();
							ImageIO.write(img, "jpg", thumbnailStream);
							InputStream inputStream = new ByteArrayInputStream(thumbnailStream.toByteArray());

							String thumbnailPreName = getBlobPreName(extensionName, true).toLowerCase();
							String newThumbnailName = UUID.randomUUID().toString();
							String blobThumbnail = thumbnailPreName + newThumbnailName + ".jpg";
							CloudBlockBlob thumbnailBlob = blobContainer.getBlockBlobReference(blobThumbnail);
							thumbnailBlob.getProperties().setContentType("image/jpeg");
							thumbnailBlob.upload(inputStream, thumbnailStream.toByteArray().length);

							//将上传后的图片URL返
							blobUploadEntity.setFileName(fileName);
							blobUploadEntity.setUrl(blob.getUri().toString());
							blobUploadEntity.setMinImgUrl(thumbnailBlob.getUri().toString());
							blobUploadEntity.setType(extensionName);
							try {
								BufferedImage sourceImg = ImageIO.read(new FileInputStream(file));
								blobUploadEntity.setSize(sourceImg.getWidth() + "*" + sourceImg.getHeight());
							} catch (IOException e) {
								e.printStackTrace();
							}
							blobUploadEntities.add(blobUploadEntity);
						} catch (Exception e) {
							map.put("fileName:",fileName);
							Result resultMsg = new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, "文件上传异常",map);
							results.add(resultMsg);
							continue;
						}
					}else {
						map.put("fileName",fileName);
						Result resultMsg = new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, "上传文件为空",map);
						results.add(resultMsg);
						continue;
					}
				}
				Result result = new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS, "文件上传成功", blobUploadEntities);
				results.add(result);
			}else{
				map.put("错误原因：","未选择上传的文件");
				Result resultMsg = new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, "请选择需要上传的文件",map);
				results.add(resultMsg);
			}
		} catch (Exception e) {
			map.put("错误原因：","文件上传异常");
			Result resultMsg = new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, "文件上传异常",map);
			results.add(resultMsg);
		}
		return results;


	}

	/**
	 * 获取图片名父目录
	 * @param fileType
	 * @param thumbnail
	 * @return
	 */
	private String getBlobPreName(String fileType, Boolean thumbnail)
	{
		String afterName = "";
		if (thumbnail)
		{
			afterName = "thumbnail/";
		}
		switch (fileType)
		{
			case "jpg":
				return "jpg/" + afterName;
			case "png":
				return "png/" + afterName;
			case "jpeg":
				return "jpeg/" + afterName;
			case "gif":
				return "gif/" + afterName;
			default :
				return "";
		}
	}
}