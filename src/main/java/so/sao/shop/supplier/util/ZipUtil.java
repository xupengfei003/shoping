package so.sao.shop.supplier.util;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;




public class ZipUtil {
	private static final int BUFFEREDSIZE = 1024;



	 /**
	  * 解压zip或者rar包的内容到指定的目录下，可以处理其文件夹下包含子文件夹的情况
	  * 
	  * @param zipFilename
	  *            要解压的zip或者rar包文件
	  * @param outputDirectory
	  *            解压后存放的目录
	  * 
	  */
	 public static void unzipZipRar(String zipFilename, String outputDirectory)
	   throws Exception {
	  File outFile = new File(outputDirectory);
	  if (!outFile.exists()) {
	   outFile.mkdirs();
	  }

	  ZipFile zipFile = new ZipFile(zipFilename);
	  @SuppressWarnings("rawtypes")
	  Enumeration en = zipFile.getEntries();
	  ZipEntry zipEntry = null;
	  while (en.hasMoreElements()) {
	   zipEntry = (ZipEntry) en.nextElement();
	   if (zipEntry.isDirectory()) {
	    // mkdir directory
	    String dirName = zipEntry.getName();
	    // System.out.println("=dirName is:=" + dirName + "=end=");
	    dirName = dirName.substring(0, dirName.length() - 1);
	    File f = new File(outFile.getPath() + File.separator + dirName);
	    f.mkdirs();
	   } else {
	    // unzip file
	    String strFilePath = outFile.getPath() + File.separator
	      + zipEntry.getName();
	    File f = new File(strFilePath);

	    // 判断文件不存在的话，就创建该文件所在文件夹的目录
	    if (!f.exists()) {
	     String[] arrFolderName = zipEntry.getName().split("/");
	     String strRealFolder = "";
	     for (int i = 0; i < (arrFolderName.length - 1); i++) {
	      strRealFolder += arrFolderName[i] + File.separator;
	     }
	     strRealFolder = outFile.getPath() + File.separator
	       + strRealFolder;
	     File tempDir = new File(strRealFolder);
	     // 此处使用.mkdirs()方法，而不能用.mkdir()
	     tempDir.mkdirs();
	    }
	    // the codes remedified by can_do on 2010-07-02 =end=
	    f.createNewFile();
	    InputStream in = zipFile.getInputStream(zipEntry);
	    FileOutputStream out = new FileOutputStream(f);
	    try {
	     int c;
	     byte[] by = new byte[BUFFEREDSIZE];
	     while ((c = in.read(by)) != -1) {
	      out.write(by, 0, c);
	     }
	     // out.flush();
	    } catch (IOException e) {
	     throw e;
	    } finally {
	     out.close();
	     in.close();
	    }
	   }
	  }
	 }

	 /**
	 /**
	  * 解压zip格式压缩包 对应的是ant.jar
	  */
	 private static void unzip(String sourceZip, String destDir)
	   throws Exception {
	  try {
	   Project p = new Project();
	   Expand e = new Expand();
	   e.setProject(p);
	   e.setSrc(new File(sourceZip));
	   e.setOverwrite(false);
	   e.setDest(new File(destDir));
	   /*
	    * ant下的zip工具默认压缩编码为UTF-8编码， 而winRAR软件压缩是用的windows默认的GBK或者GB2312编码
	    * 所以解压缩时要制定编码格式
	    */
	   e.setEncoding("gbk");
	   e.execute();
	  } catch (Exception e) {
	   throw e;
	  }
	 }

	 /**
	  * 解压rar格式压缩包。
	  * 对应的是java-unrar-0.3.jar，但是java-unrar-0.3.jar又会用到commons-logging-1.1.1.jar
	  */
	 private static void unrar(String sourceRar, String destDir ,Boolean isDelete)
	   throws Exception {
	  Archive a = null;
	  FileOutputStream fos = null;
	  try {
	   a = new Archive(new File(sourceRar));
	   FileHeader fh = a.nextFileHeader();
	   while (fh != null) {
	    if (!fh.isDirectory()) {
	     // 1 根据不同的操作系统拿到相应的 destDirName 和 destFileName
	     String compressFileName = fh.getFileNameString().trim();
	     String destFileName = "";
	     String destDirName = "";
	     // 非windows系统
	     if (File.separator.equals("/")) {
	      destFileName = destDir
	        + compressFileName.replaceAll("\\\\", "/");
	      destDirName = destFileName.substring(0,
	        destFileName.lastIndexOf("/"));
	      // windows系统
	     } else {
	      destFileName = destDir
	        + compressFileName.replaceAll("/", "\\\\");
	      destDirName = destFileName.substring(0,
	        destFileName.lastIndexOf("\\"));
	     }
	     // 2创建文件夹
	     File dir = new File(destDirName);
	     if (!dir.exists() || !dir.isDirectory()) {
	      dir.mkdirs();
	     }
	     // 3解压缩文件
	     fos = new FileOutputStream(new File(destFileName));
	     a.extractFile(fh, fos);
	     fos.close();
	     fos = null;
	    }
	    fh = a.nextFileHeader();
	   }
	   a.close();
	   a = null;
	  } catch (Exception e) {
	   throw e;
	  } finally {
	   if (fos != null) {
	    try {
	     fos.close();
	     fos = null;
	    } catch (Exception e) {
	     e.printStackTrace();
	    }
	   }
	   if (a != null) {
	    try {
	     a.close();
	     a = null;
	    } catch (Exception e) {
	     e.printStackTrace();
	    }
	   }
	  }
	  if (isDelete) {  
          new File(sourceRar).delete();  
      }
	 }

	 /**
	  * 解压缩
	  */
	 public static void deCompress(String sourceFile, String destDir ,Boolean isDelete)
	   throws Exception {
	  // 保证文件夹路径最后是"/"或者"\"
	  char lastChar = destDir.charAt(destDir.length() - 1);
	  if (lastChar != '/' && lastChar != '\\') {
	   destDir += File.separator;
	  }
	  // 根据类型，进行相应的解压缩
	  String type = sourceFile.substring(sourceFile.lastIndexOf(".") + 1);
	  if (type.equals("zip")) {
	   ZipUtil.unzip(sourceFile, destDir);
	  } else if (type.equals("rar")) {
	   ZipUtil.unrar(sourceFile, destDir,isDelete);
	  } else {
	   throw new Exception("只支持zip和rar格式的压缩包！");
	  }
	  if (isDelete) {  
          new File(sourceFile).delete();  
      }
	 }
    
	public static void main(String[] args) {
		ZipUtil cpr = new ZipUtil();
		try {
			//cpr.zip("d:/333.zip", "d:/333", false);
			cpr.deCompress("d:/99999999999.rar", "d:/cheziliang",false);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
