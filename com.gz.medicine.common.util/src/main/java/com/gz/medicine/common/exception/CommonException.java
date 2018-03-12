package com.gz.medicine.common.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.bind.annotation.XmlAttribute;

@SuppressWarnings("serial")
public class CommonException extends Exception {
	String code ="COM0000.INTERNAL.EXCEPTION";
	String desc ="Internal Exception";

	public CommonException() {
		super();
	}

	public CommonException(Throwable cause) {
		super(cause);
	}

	public CommonException(String msg) {
		super(msg);
	}

	public CommonException(String code, String desc) {
		super();
		this.code=code;
		this.desc=desc;
	}

	public CommonException(Enum e,Throwable cause) {
		super(cause);
		try {
			Class clazz = e.getClass();
			Method getValue = null;
			getValue = clazz.getDeclaredMethod("getValue");
			Method getName = clazz.getDeclaredMethod("getName");
			String value= (String) getValue.invoke(e);
			String name= (String) getName.invoke(e);
			this.code=name;
			this.desc=value;
		} catch (NoSuchMethodException e1) {
			this.code="CM001";
			this.desc="CommonException构造器异常:"+e1.getMessage();
		} catch (IllegalAccessException e1) {
			this.code="CM001";
			this.desc="CommonException构造器异常:"+e1.getMessage();
        } catch (InvocationTargetException e1) {
			this.code="CM001";
			this.desc="CommonException构造器异常:"+e1.getMessage();
		}

	}
	public CommonException(Enum e,String msg,Throwable cause) {
		super(cause);
		try {
			Class clazz = e.getClass();
			Method getName = clazz.getDeclaredMethod("getName");
			String name= (String) getName.invoke(e);
			this.code=name;
			this.desc=msg;
		} catch (NoSuchMethodException e1) {
			this.code="CM001";
			this.desc="CommonException构造器异常:"+e1.getMessage();
		} catch (IllegalAccessException e1) {
			this.code="CM001";
			this.desc="CommonException构造器异常:"+e1.getMessage();
		} catch (InvocationTargetException e1) {
			this.code="CM001";
			this.desc="CommonException构造器异常:"+e1.getMessage();
		}

	}

	public CommonException(String code, String desc, Throwable cause) {
		super(cause);
		this.code=code;
		this.desc=desc;
	}

	public CommonException(String code, String desc, String message, Throwable cause) {
		super(message, cause);
		this.code=code;
		this.desc=desc;
	}

    @XmlAttribute
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

    @XmlAttribute
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 打印Trace信息
	 *
	 * @param e
	 * @return
	 */
	public static String getTrace(Exception e) {
		if (null == e)
			return null;
		ByteArrayOutputStream out = null;
		PrintStream print = null;
		try {
			out = new ByteArrayOutputStream();
			print = new PrintStream(out);
			e.printStackTrace(print);
			print.flush();
			out.flush();
			return out.toString();
		} catch (Exception e1) {
			return null;
		}finally{
			try{ if(out!=null) out.close(); }catch(Exception ignore){}
			if(print!=null) print.close();
		}
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("CommonException{");
		if(code!=null && !"".equals(code)) sb.append("code={").append(code).append("},");
		if(desc!=null && !"".equals(desc)) sb.append("desc={").append(desc).append("},");
		if(this.getMessage()!=null && !"".equals(this.getMessage())) sb.append("message={").append(this.getMessage()).append("},");
		if(this.getCause()!=null && !"".equals(this.getCause())) sb.append("cause={").append(this.getCause().getMessage()).append("},");
		return sb.toString();
	}
}
