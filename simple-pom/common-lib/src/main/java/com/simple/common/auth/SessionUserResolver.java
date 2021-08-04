package com.simple.common.auth;

import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;

import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;


public class SessionUserResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(LoginUser.class);
    }


    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) {
        SessionUser user = SessionUser.builder().isLoginUser(false).isWechatUser(false).build();
        try {
            HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
            String token = request.getHeader(AuthConstant.AUTHENTICATION_HEADER);
            String roles = request.getHeader(AuthConstant.AUTHORIZATION_HEADER);
            String[] roleArray = {roles};
            user.setRoles(roleArray);

            if (StringUtils.isBlank(token)) {
                user.setLoginUser(false);
            } else {
                user.setLoginUser(true);
            }

            AuthModel model = Sessions.getSessionUserInfo(token);

            if ((null == model) || (StringUtils.isBlank(model.getUserId()))){
                user.setLoginUser(false);
            }else{
                user.setLoginUser(true);
            }
            user.setUserId(model.getUserId());
            if (StringUtils.isNotBlank(model.getOpenId())) {
                user.setWechatUser(true);
                user.setOpenId(model.getOpenId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
}