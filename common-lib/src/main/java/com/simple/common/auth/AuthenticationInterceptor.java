package com.simple.common.auth;

import com.simple.common.api.BaseResponse;
import com.simple.common.error.ServiceException;
import com.simple.common.auth.token.ValidateLoginHelp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

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
        if (methodAnnotation == null) {
            return true;
        }
        try {

            //用户登录验证
            String token = request.getHeader("token");
            BaseResponse result = ValidateLoginHelp.validateToken(token);
            if (!result.success()) {
                throw new ServiceException(result.getStatus().getMessage());
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error("登录拦截器读取请求体参数信息异常!", e);
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
            if ((null == responseToken) || ("".equals(responseToken))) {
                response.setHeader("token", requestToken);
                responseToken = requestToken;
            }
            int appType = 4;
            String applicationType = getApplicationType(request, response);
            if (applicationType.toLowerCase().equals("wx")) {
                appType = 3;
            }
            ValidateLoginHelp.refreshToken(responseToken, appType);
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

    /**
     * 返回信息
     *
     * @param response
     * @param resMessage
     */
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
