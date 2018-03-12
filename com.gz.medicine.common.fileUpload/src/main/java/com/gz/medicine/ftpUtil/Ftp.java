package com.gz.medicine.ftpUtil;

/**
 * ftp下载类
 * Created by dlf on 2016/3/25.
 */
import java.io.*;
import java.util.TimeZone;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import org.apache.log4j.Logger;
public class Ftp {
    private FTPClient ftpClient;
    private String strIp;
    private int intPort;
    private String user;
    private String password;

    private static Logger logger = Logger.getLogger(Ftp.class.getName());

    /* *
     * Ftp构造函数
     */
    public Ftp(String strIp, int intPort, String user, String Password) {
        this.strIp = strIp;
        this.intPort = intPort;
        this.user = user;
        this.password = Password;
        this.ftpClient = new FTPClient();
    }
    public Ftp(DataConfig dataConfig) {
    	 this.strIp = dataConfig.getFtpFileIP();
         this.intPort = dataConfig.getFtpPort();
         this.user = dataConfig.getFtpLoginName();
         this.password = dataConfig.getFtpLoginPwd();
         this.ftpClient = new FTPClient();
	}
	/**
     * @return 判断是否登入成功
     * */
    public boolean ftpLogin() {
        boolean isLogin = false;
        try {
            FTPClientConfig ftpClientConfig = new FTPClientConfig();
            ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
            this.ftpClient.setControlEncoding("GBK");
            this.ftpClient.configure(ftpClientConfig);
            if (this.intPort > 0) {
                this.ftpClient.connect(this.strIp, this.intPort);
            } else {
                this.ftpClient.connect(this.strIp);
            }
            // FTP服务器连接回答
            int reply = this.ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                this.ftpClient.disconnect();
                logger.error("登录FTP服务失败！");
                return isLogin;
            }
            this.ftpClient.login(this.user, this.password);
            // 设置传输协议
            this.ftpClient.enterLocalPassiveMode();
            this.ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            logger.info("恭喜" + this.user + "成功登陆FTP服务器");
            isLogin = true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(this.user + "登录FTP服务失败！" + e.getMessage());
        }
        this.ftpClient.setBufferSize(1024 * 2);
        this.ftpClient.setDataTimeout(30 * 1000);
        return isLogin;
    }

    /**
     * @退出关闭服务器链接
     * */
        public void getpdf(){
            boolean loginflag=true;
            try {
                this.ftpClient.connect("168.160.76.102", 21);
                loginflag = this.ftpClient.login("168.160.76.102\\lisftp","lisftp");
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (loginflag && FTPReply.isPositiveCompletion(ftpClient.getReplyCode()))
            {
                InputStream inputStream =null;
                try {
                    inputStream=ftpClient.retrieveFileStream("loom.pdf");//返回为空
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
     public void ftpLogOut() {
        if (null != this.ftpClient && this.ftpClient.isConnected()) {
            try {
                boolean reuslt = this.ftpClient.logout();// 退出FTP服务器
                if (reuslt) {
                    logger.info("成功退出服务器");
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.warn("退出FTP服务器异常！" + e.getMessage());
            } finally {
                try {
                    this.ftpClient.disconnect();// 关闭FTP服务器的连接
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.warn("关闭FTP服务器的连接异常！");
                }
            }
        }
    }

    /***
     * 上传Ftp文件
     * @param localFile 当地文件

     * */
    public boolean uploadFile(File localFile, String romotUpLoadePath) {
        BufferedInputStream inStream = null;
        boolean success = false;
        try {
            this.ftpClient.changeWorkingDirectory(romotUpLoadePath);// 改变工作路径
            inStream = new BufferedInputStream(new FileInputStream(localFile));
            logger.info(localFile.getName() + "开始上传.....");
            success = this.ftpClient.storeFile(localFile.getName(), inStream);
            if (success == true) {
                logger.info(localFile.getName() + "上传成功");
                return success;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(localFile + "未找到");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }
    /***
     * 上传Ftp文件
     * @param localFile 当地文件

     * */
    public boolean uploadFile(InputStream localFile,String name, String romotUpLoadePath) {
        boolean success = false;
        try {
            this.ftpClient.changeWorkingDirectory(romotUpLoadePath);// 改变工作路径
            success = this.ftpClient.storeFile(name, localFile);
            if (success == true) {
                logger.info(name + "上传成功");
                return success;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error(localFile + "未找到");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    /***
     * 下载文件
     * @param remoteFileName   待下载文件名称
     * @param localDires 下载到当地那个路径下
     * @param remoteDownLoadPath remoteFileName所在的路径
     * */

    public boolean downloadFile(String remoteFileName, String localDires,
                                String remoteDownLoadPath) {
        String strFilePath = localDires+"/" + remoteFileName;
        remoteDownLoadPath="/"+remoteDownLoadPath;
        BufferedOutputStream outStream = null;
        boolean success = false;
        try {
            boolean isChangeSuccessed=this.ftpClient.changeWorkingDirectory(remoteDownLoadPath);
            System.out.println("isChangeSuccessed:"+isChangeSuccessed);
            outStream = new BufferedOutputStream(new FileOutputStream(
                    strFilePath));
            logger.info(remoteFileName + "开始下载....");
            success = this.ftpClient.retrieveFile(remoteFileName, outStream);
            InputStream inputStream =null;
            try {
                inputStream=ftpClient.retrieveFileStream(remoteFileName);//返回为空
                System.out.println("流大小："+inputStream.read());
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                ftpLogOut();
            }
            if (success) {
                logger.info(remoteFileName + "成功下载到" + strFilePath);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(remoteFileName + "下载失败");
        } finally {
            if (null != outStream) {
                try {
                    outStream.flush();
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (success == false) {
            logger.error(remoteFileName + "下载失败!!!");
        }
        return success;
    }




    public boolean downloadFile(String sourcePdfPath, String targetPdfPath )  throws Exception{
        //jdk1.7以上implements java.io.Closeable的类不需要手动关闭流
        OutputStream outStream = null;
        boolean success = false;
            outStream =new FileOutputStream( targetPdfPath);
            logger.info(sourcePdfPath + "开始下载....");
            success = this.ftpClient.retrieveFile(sourcePdfPath, outStream);
            outStream.flush();
            
            if (success == true) {
                logger.info(sourcePdfPath + "成功下载到" + targetPdfPath);
                return success;
            }
            //IOUtils.closeQuietly(outStream);
        if (success == false) {
            logger.error(sourcePdfPath + "下载失败!!!");
        }
        return success;
    }

    /***
     * @上传文件夹
     * @param localDirectory
     *            当地文件夹
     * @param remoteDirectoryPath
     *            Ftp 服务器路径 以目录"/"结束
     * */
    public boolean uploadDirectory(String localDirectory,
                                   String remoteDirectoryPath) {
        File src = new File(localDirectory);
        try {
            remoteDirectoryPath = remoteDirectoryPath + src.getName() + "/";
            this.ftpClient.makeDirectory(remoteDirectoryPath);
            // ftpClient.listDirectories();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info(remoteDirectoryPath + "目录创建失败");
        }
        File[] allFile = src.listFiles();
        for (File anAllFile : allFile) {
            if (!anAllFile.isDirectory()) {
                String srcName = anAllFile.getPath();
                uploadFile(new File(srcName), remoteDirectoryPath);
            }
        }
        for (File anAllFile : allFile) {
            if (anAllFile.isDirectory()) {
                // 递归
                uploadDirectory(anAllFile.getPath(),
                        remoteDirectoryPath);
            }
        }
        return true;
    }

    /***
     * @下载文件夹

     * @param remoteDirectory 远程文件夹
     * */
    public boolean downLoadDirectory(String localDirectoryPath,String remoteDirectory) {
        try {
            String fileName = new File(remoteDirectory).getName();
            localDirectoryPath = localDirectoryPath + fileName + "//";
            new File(localDirectoryPath).mkdirs();
            FTPFile[] allFile = this.ftpClient.listFiles(remoteDirectory);
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
                if (!allFile[currentFile].isDirectory()) {
                    downloadFile(allFile[currentFile].getName(),localDirectoryPath, remoteDirectory);
                }
            }
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {
                if (allFile[currentFile].isDirectory()) {
                    String strremoteDirectoryPath = remoteDirectory + "/"+ allFile[currentFile].getName();
                    downLoadDirectory(localDirectoryPath,strremoteDirectoryPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("下载文件夹失败");
            return false;
        }
        return true;
    }
    // FtpClient的Set 和 Get 函数
    public FTPClient getFtpClient() {
        return ftpClient;
    }
    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    public static void main(String[] args) throws Exception {
        Ftp ftp=new Ftp("39.108.53.12",21,"administrator","Qq16899199");
        ftp.ftpLogin();
ftp.uploadDirectory("D:\\ftp","/doc");
ftp.ftpLogOut();


    }



}
