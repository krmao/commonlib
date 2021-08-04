package com.simple.common.auth;

import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class AuthorizeInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

         if(Sessions.checkPermissions(request)){
             return true;
         }else{
             response.setStatus(HttpServletResponse.SC_FORBIDDEN);
             return false;
         }

//        HandlerMethod handlerMethod = (HandlerMethod) handler;
//        Authorize authorize = handlerMethod.getMethod().getAnnotation(Authorize.class);
//        if (authorize == null) {
//            return true; // no need to authorize
//        }
//
//        String[] allowedHeaders = authorize.value();
//        String authzHeader = request.getHeader(AuthConstant.AUTHORIZATION_HEADER);
//        //System.out.println("get the roles from toke is*************" +  authzHeader);
//        String authHeader = Sessions.getAuthorizationHeader(request);
//
//        if (StringUtils.isEmpty(authHeader)) {
//            throw new PermissionDeniedException(AuthConstant.ERROR_MSG_MISSING_AUTH_HEADER);
//        }
//
//        if (!Arrays.asList(allowedHeaders).contains(authHeader)) {
//            throw new PermissionDeniedException(AuthConstant.ERROR_MSG_DO_NOT_HAVE_ACCESS);
//        }
//
//        return true;
    }
}
