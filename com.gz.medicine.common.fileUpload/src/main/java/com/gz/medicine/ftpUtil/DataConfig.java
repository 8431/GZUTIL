package com.gz.medicine.ftpUtil;



/**
 * 
 * 
 * @author        jiangzhenjian  2016-5-6 上午10:33:16
 * @see           //配置文件dataConfig对象
 * @version       v1.0.0.1
 */
public class DataConfig {
	 private String pdfWsdl;
	 private String updateWsdl;
	 private String ftpFileIP;
	 private Integer ftpPort;
	 private String ftpLoginName;
	 private String ftpLoginPwd;
	public String getPdfWsdl() {
		return pdfWsdl;
	}
	public void setPdfWsdl(String pdfWsdl) {
		this.pdfWsdl = pdfWsdl;
	}
	public String getUpdateWsdl() {
		return updateWsdl;
	}
	public void setUpdateWsdl(String updateWsdl) {
		this.updateWsdl = updateWsdl;
	}
	public String getFtpFileIP() {
		return ftpFileIP;
	}
	public void setFtpFileIP(String ftpFileIP) {
		this.ftpFileIP = ftpFileIP;
	}
	public Integer getFtpPort() {
		return ftpPort;
	}
	public void setFtpPort(Integer ftpPort) {
		this.ftpPort = ftpPort;
	}
	public String getFtpLoginName() {
		return ftpLoginName;
	}
	public void setFtpLoginName(String ftpLoginName) {
		this.ftpLoginName = ftpLoginName;
	}
	public String getFtpLoginPwd() {
		return ftpLoginPwd;
	}
	public void setFtpLoginPwd(String ftpLoginPwd) {
		this.ftpLoginPwd = ftpLoginPwd;
	}


}
