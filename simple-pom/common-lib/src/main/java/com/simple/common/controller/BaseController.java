package com.simple.common.controller;

import com.simple.common.api.BaseResponse;
import com.simple.common.env.EnvConfig;
import com.simple.common.error.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;

//import com.simple.common.props.AppProps;

public class BaseController {

    @Autowired
    private EnvConfig envConfig;

    @Autowired
    private ServiceHelper serviceHelper;
    protected BaseResponse handleExeption(Exception ex, String msg){
        return  ServiceHelper.handleControllerException(ex, msg);
    }

}
