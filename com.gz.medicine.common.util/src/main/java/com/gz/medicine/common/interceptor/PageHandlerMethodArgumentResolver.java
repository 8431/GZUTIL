package com.gz.medicine.common.interceptor;

import com.gz.medicine.common.mybatisPageVo.Page;
import com.gz.medicine.common.mybatisPageVo.PageModel;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;

/**
 * Created by Administrator on 2017/8/3 0003.
 */
public class PageHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    public boolean supportsParameter(MethodParameter methodParameter) {
        //参数类型使用PageModel则判断为使用分页
           return  methodParameter.getParameterType().equals(PageModel.class);//直接判断 类型方式  ;
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        PageModel pageModel = (PageModel) BeanUtils.instantiate(parameter.getParameterType());
        Page p= new Page();

        pageModel.setPage(p);
        Map<String,String[]> mp=webRequest.getParameterMap();
        if(mp!=null&&mp.size()>0){
            for (Map.Entry<String, String[]> entry : mp.entrySet()) {
                if(!StringUtils.isEmpty(entry.getKey())){
                    p.put(entry.getKey(),entry.getValue()[0]);
                }
            }
        }

        String pageNo= (String) p.get("pageNo");
        String pageSize= (String) p.get("pageSize");
       if(pageNo!=null){
           p.setPageNo(Integer.parseInt(pageNo));
       }
        if(pageSize!=null){
            p.setPageSize(Integer.parseInt(pageSize));
       }


        return pageModel;
    }
}
