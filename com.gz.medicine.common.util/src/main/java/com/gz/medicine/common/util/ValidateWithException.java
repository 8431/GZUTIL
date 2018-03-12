package com.gz.medicine.common.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
public  abstract class  ValidateWithException {
	/** 
     * 服务端参数有效性验证 
     * @param object 验证的实体对象 
     * @param groups 验证组 
     * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中 
     */  
    @SuppressWarnings("rawtypes")   
	public static  String validates(Validator validator, Object object, Class<?>... groups) {  
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);  
        if (!constraintViolations.isEmpty()) {  
            StringBuffer sb=new StringBuffer();  
            for (ConstraintViolation constraintViolation : constraintViolations) {  
                sb.append(constraintViolation.getMessage());  
            }  
            return sb.toString();
        }
		return null;
    }
}
