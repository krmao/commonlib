package com.simple.common.auth;

import com.simple.common.error.ServiceException;
import com.simple.common.token.JwtUtils;
import com.simple.common.redis.SimpleRedisClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Sessions {
    public static final String DEFAULT_ROLE = "guest";
    private static final Logger logger = LoggerFactory.getLogger(Sessions.class);
    public static final long SHORT_SESSION = TimeUnit.HOURS.toMillis(12);
    public static final long LONG_SESSION = TimeUnit.HOURS.toMillis(30 * 24);

    public static String login(String userId, String roles, String openId, String unionId) {
        return Sessions.createTokenWithUserInfo(userId,roles, openId, unionId);
    }
    public static String login(String userId, String roles) {
        return Sessions.createTokenWithUserInfo(userId,roles, "", "");
    }
    public static String login(String userId) {
        return Sessions.createTokenWithUserInfo(userId,Sessions.DEFAULT_ROLE, "", "");
    }
    public static void loginUser(Integer id, String userId, String roles, String openId,String unionId, String domain,boolean rememberMe, HttpServletResponse response) {
        String token = Sessions.createTokenWithUserInfo(userId, roles, openId, unionId);
        Sessions.writeToken(token,domain,rememberMe,response);
    }
    public static void logout(String domain, HttpServletRequest request, HttpServletResponse response) {
        //EnvConfig.env().get
        String token = Sessions.getAuthToken(request);
        Sessions.logout(domain,token,response);
    }
    public static void logout(String domain, String token, HttpServletResponse response) {
        SimpleRedisClient.templateInstance.delete(token);
        Cookie cookie = new Cookie(AuthConstant.COOKIE_NAME, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setDomain(domain);
        response.addCookie(cookie);
    }



    public static void writeToken(String token, String domainName,boolean rememberMe, HttpServletResponse response) {
        long duration;

        if (rememberMe) {
            // "Remember me"
            duration = LONG_SESSION;
        } else {
            duration = SHORT_SESSION;
        }
        int maxAge = (int) (duration / 1000);

        Cookie cookie = new Cookie(AuthConstant.COOKIE_NAME, token);
        cookie.setPath("/");
        cookie.setDomain(domainName);
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        response.setHeader(AuthConstant.AUTHENTICATION_HEADER, token);
    }

    public static String createTokenWithUserInfo(String userId, String roles, String openId, String unionId){
        Map<String, Object> jwtPayload =  new HashMap<String, Object>();
        jwtPayload.put(AuthConstant.AUTHORIZATION_HEADER,roles);
        String newToken = JwtUtils.createToken(jwtPayload);
        if (null == newToken){
            throw new ServiceException("failed to create token");
        }
        AuthModel userInfoOld = (AuthModel)SimpleRedisClient.operatorInstance.get(userId);
        if ((null != userInfoOld) && (StringUtils.isNotBlank(userInfoOld.getToken()))){
            SimpleRedisClient.templateInstance.delete(userId);
            SimpleRedisClient.templateInstance.delete(userInfoOld.getToken());
        }
        AuthModel userInfo =  AuthModel.builder().token(newToken).userId(userId).openId(openId).build();
        SimpleRedisClient.operatorInstance.set(newToken, userInfo,1L, TimeUnit.DAYS);
        SimpleRedisClient.operatorInstance.set(userId, userInfo,1L, TimeUnit.DAYS);
        return newToken;
    }

    public static String getCookieToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) return null;
        Cookie tokenCookie = Arrays.stream(cookies)
                .filter(cookie -> AuthConstant.COOKIE_NAME.equals(cookie.getName()))
                .findAny().orElse(null);
        if (tokenCookie == null) return null;
        return tokenCookie.getValue();
    }

    public static String getAuthToken(HttpServletRequest request) {
        String token = request.getHeader(AuthConstant.AUTHENTICATION_HEADER);
        return token;
    }
    public static String getAuthorizationHeader(HttpServletRequest request) {
        String authorization = request.getHeader(AuthConstant.AUTHORIZATION_HEADER);
        return authorization;
    }

    public static String getAuthorizationRole(HttpServletRequest request) {
        String token = request.getHeader(AuthConstant.AUTHENTICATION_HEADER);
        String roles =  JwtUtils.decodeToken(token);
        return roles;
    }




    public static void saveSessionUserInfo(String token, Integer id,  String userId, String openId){
        AuthModel userInfo =  AuthModel.builder().token(token).userId(userId).openId(openId).build();
        SimpleRedisClient.operatorInstance.set(token, userInfo,1L, TimeUnit.DAYS);
    }
    public static AuthModel getSessionUserInfo(String token){
        AuthModel userInfo = (AuthModel) SimpleRedisClient.operatorInstance.get(token);
        return userInfo;
    }
    public static AuthModel getSessionUserInfo(HttpServletRequest request){
        String token = Sessions.getAuthToken(request);
        AuthModel userInfo = (AuthModel) SimpleRedisClient.operatorInstance.get(token);
        return userInfo;
    }
    public static String getSessionUserId(HttpServletRequest request){

        AuthModel userInfo = Sessions.getSessionUserInfo(request);
        return userInfo.getUserId();
    }
    public static AuthModel getSessionUserStatusByUserId(String userId){
        AuthModel userStatus = (AuthModel) SimpleRedisClient.operatorInstance.get(userId);
        return userStatus;

    }

    public static boolean validateAuthentication(HttpServletRequest request ) {
        String token = Sessions.getAuthToken(request);
        return Sessions.validateToken(token);
    }


    public static boolean validateToken(String token ) {

        if (StringUtils.isBlank(token)) {
            logger.error("请求参数TokenId不能为空!");
            throw new PermissionDeniedException("未登录，请登录");
            //return BaseResponse.build().code(ResultCode.FAILURE).message("未登录，请登录");
        }
        if (!JwtUtils.verifyToken(token)){
            throw new PermissionDeniedException("Token无效");
        }
        //step2:根据token获取userId
        AuthModel authModel = (AuthModel) SimpleRedisClient.operatorInstance.get(token);
        if (null== authModel){
            logger.error("登录过期，请重新登录");
            throw new PermissionDeniedException("登录过期，请重新登录");
        }

        return true;
    }


    public static boolean refreshToken(String token) {
        int expTimeSeconds = 60 * 60 * 24 * 3;
        if (StringUtils.isBlank(token)){
            return false;
        }
        AuthModel authModel = (AuthModel) SimpleRedisClient.operatorInstance.get(token);
        SimpleRedisClient.operatorInstance.set(token, authModel, 1L, TimeUnit.DAYS);
        SimpleRedisClient.operatorInstance.set(authModel.getUserId(), authModel, 1L, TimeUnit.DAYS);
        return true;
    }

    public static HttpServletRequest  getCurrentRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            return request;
        }
        return null;
    }
    public static HttpServletResponse  getCurrentResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletResponse response = ((ServletRequestAttributes)requestAttributes).getResponse();
            return response;
        }
        return null;
    }


}
