package com.simple.common.auth;

import com.simple.common.api.BaseResponse;
import com.simple.common.error.ServiceException;


import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.MimeHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 拦截器,用于控制是否登录
 *
 * @author hejinguo
 * @version $Id: HandlerAuthLoginContextInterceptor.java, v 0.1 2019年11月17日 下午5:37:11
 */
@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    private static final Logger logger = LoggerFactory
            .getLogger(AuthenticationInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        //判断在方法上是否有添加需登录注解(若添加则验证,否则相反)
        LoginRequired methodAnnotation = method.getAnnotation(LoginRequired.class);
        Authorize methodAnnotationz = method.getAnnotation(Authorize.class);
        if ((methodAnnotation == null) && (null == methodAnnotationz)) {
            return true;
        }


        //用户登录验证

        BaseResponse result = Sessions.validateAuthentication(request);
        if (!result.success()) {
            throw new ServiceException(result.getStatus().getMessage());
        }

        if (null != methodAnnotationz) {
            //解析出token中的Authorization中的权限信息放到请求头中方便后续的认证
            String roles = Sessions.getAuthorizationRole(request);
            //System.out.println("decode the roles from toke is*************" +  roles);
            if (null != roles) {
                Map<String, String> map = new HashMap<>();
                map.put(AuthConstant.AUTHORIZATION_HEADER, roles);
                addHeader(request, map);

            }
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {

    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object obj,
                           ModelAndView modelandview) throws Exception {
        try {


            String responseToken = response.getHeader("token");
            String requestToken = request.getHeader("token");
            if (StringUtils.isBlank(responseToken) && StringUtils.isNotBlank(requestToken)) {
                response.setHeader("token", requestToken);
                responseToken = requestToken;
            }
            if (StringUtils.isNotBlank(responseToken)) {
                Sessions.refreshToken(responseToken);
            }
            //ValidateLoginHelp.refreshToken(responseToken, appType);
        } catch (Exception ex) {
            logger.error("请求刷新token失败", ex);
        }

    }

    private static String getApplicationType(HttpServletRequest request, HttpServletResponse response) {
        String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.indexOf("micromessenger") > -1) { //微信客户端
            return "wx";
        } else {
            return "app";
        }
    }

    private void addHeader(HttpServletRequest request, Map<String, String> headerMap) {
        if (headerMap == null || headerMap.isEmpty()) {
            return;
        }

        Class<? extends HttpServletRequest> c = request.getClass();
        System.out.println(c.getName());

        try {
            Field requestField = c.getDeclaredField("request");
            requestField.setAccessible(true);

            Object o = requestField.get(request);
            Field coyoteRequest = o.getClass().getDeclaredField("coyoteRequest");
            coyoteRequest.setAccessible(true);

            Object o2 = coyoteRequest.get(o);
            Field headers = o2.getClass().getDeclaredField("headers");
            headers.setAccessible(true);

            MimeHeaders mimeHeaders = (MimeHeaders) headers.get(o2);
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                mimeHeaders.removeHeader(entry.getKey());
                mimeHeaders.addValue(entry.getKey()).setString(entry.getValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    private void returnJsonMessage(HttpServletResponse response, ResponseMessage resMessage) {
//        PrintWriter writer = null;
//        response.setCharacterEncoding(DataEncode.UTF8);
//        response.setContentType("application/json;charset=UTF-8");
//        try {
//            writer = response.getWriter();
//            writer.print(JsonUtil.writeObjectJSON(resMessage));
//        } catch (IOException e) {
//            logger.error("登录拦截器中输入返回信息异常!", e);
//        } finally {
//            if (writer != null)
//                writer.close();
//        }
//    }
}
