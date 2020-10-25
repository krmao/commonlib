package com.simple.common.error;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.simple.common.api.BaseResponse;
import com.simple.common.api.GenericResponse;
import com.simple.common.api.ResultCode;
import com.simple.common.auth.Sessions;
import com.simple.common.env.EnvConfig;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.nio.charset.Charset;


@RequiredArgsConstructor
@Component
public class ServiceHelper {
    static final ILogger logger = SLoggerFactory.getLogger(ServiceHelper.class);

    private final SentryClient sentryClient;
    private final EnvConfig envConfig;

    public void handleError(ILogger log, String errMsg) {
        log.error(errMsg);
        if (!envConfig.isDebug()) {
            sentryClient.sendMessage(errMsg);
        }
    }

    public void handleError(String errMsg) {
        logger.error(errMsg);
        if (!envConfig.isDebug()) {
            sentryClient.sendMessage(errMsg);
        }
    }

    public void handleException(ILogger log, Exception ex, String errMsg) {
        log.error(errMsg, ex);
        if (!envConfig.isDebug()) {
            sentryClient.sendException(ex);
        }
    }

    public void handleException(Exception ex, String errMsg) {
        logger.error(errMsg, ex);
        if (!envConfig.isDebug()) {
            sentryClient.sendException(ex);
        }
    }

    public static GenericResponse handleControllerException(Exception ex, String msg) {
        try {
            String requestURL = Sessions.getCurrentRequest().getRequestURI();
            InputStream inputStream = Sessions.getCurrentRequest().getInputStream();
            String requestData = StreamUtils.copyToString(inputStream,
                    Charset.forName("UTF-8"));
            String messages = errorMessages(requestURL, requestData, msg, ex);
            if (EnvConfig.env().isDebug()) {
                logger.error(messages);
                ex.printStackTrace();
            } else {
                SentryClientFactory.sentryClient().sendException(ex);
            }

        } catch (Exception e) {
            if (EnvConfig.env().isDebug()) {
                ex.printStackTrace();
            }
        }
        GenericResponse res = GenericResponse.build();
        res.code(ResultCode.FAILURE).message(msg);
        return res;
    }

    public static GenericResponse handleControllerException(HttpServletRequest request, Exception ex, String msg) {
        try {
            String requestURL = request.getRequestURI();
            InputStream inputStream = request.getInputStream();
            String requestData = StreamUtils.copyToString(inputStream,
                    Charset.forName("UTF-8"));
            String messages = errorMessages(requestURL, requestData, msg, ex);

            if (EnvConfig.env().isDebug()) {
                logger.error(messages);
                ex.printStackTrace();
            } else {
                SentryClientFactory.sentryClient().sendException(ex);
            }
        } catch (Exception e) {
            if (EnvConfig.env().isDebug()) {
                ex.printStackTrace();
            }
        }
        GenericResponse res = GenericResponse.build();
        res.code(ResultCode.FAILURE).message(msg);
        return res;
    }

    public static void handleServiceException(Exception ex, String msg) {
        if (EnvConfig.env().isDebug()) {
            logger.error(msg);
            ex.printStackTrace();
        } else {
            SentryClientFactory.sentryClient().sendException(ex);
        }


    }
    public static void handleServiceException(String msg) {
        if (EnvConfig.env().isDebug()) {
            logger.error(msg);
        } else {
            SentryClientFactory.sentryClient().sendMessage(msg);
        }
    }


    public static String errorMessages(String uri, String params, String msg, Exception ex) {

        StringBuffer buffer = new StringBuffer();
        buffer.append(System.getProperty("line.separator"));
        buffer.append("---->请求方法：" + uri);
        buffer.append(System.getProperty("line.separator"));
        buffer.append("---->请求参数：" + params);
        buffer.append(System.getProperty("line.separator"));
        buffer.append("---->异常信息：" + msg);
        buffer.append(System.getProperty("line.separator"));

        return buffer.toString();
    }
}
