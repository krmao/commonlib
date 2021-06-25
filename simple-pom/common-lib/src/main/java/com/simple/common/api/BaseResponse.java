package com.simple.common.api;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {
    private ResponseStatus status = new ResponseStatus();
    public static BaseResponse buildFailure(String msg){
        BaseResponse ret = new BaseResponse();
        return ret.code(ResultCode.FAILURE).message(msg);
    }
    public static BaseResponse buildSuccess(){
        BaseResponse ret = new BaseResponse();
        return ret.code(ResultCode.SUCCESS);
    }
    public static BaseResponse build(){
        return new BaseResponse();
    }

    public BaseResponse code(ResultCode code){
        this.status.setCode(code);
        return this;
    }
    @JSONField(serialize = false)
    public boolean success(){
        return (this.status.getCode().equals(ResultCode.SUCCESS));
    }
    public BaseResponse message(String message){
        this.status.setMessage(message);
        return this;
    }

}
