package com.simple.common.api;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class GenericResponse extends BaseResponse {

    //@Builder.Default
    private JSONObject data = new JSONObject();
    public GenericResponse addKey$Value(String key, Object value) {
        data.put(key, value);
        return this;
    }
    protected void setData(JSONObject data){
        this.data = data;
    }
    public GenericResponse setDataObject(Object value) {
        String jsonString = JSON.toJSONString(value);
        this.data = this.data.parseObject(jsonString);
        return this;
    }
    public GenericResponse(Object value){
        this.setDataObject(value);
    }
    public static GenericResponse buildWithObject(Object value){
       return  new GenericResponse(value);
    }
    public static GenericResponse build(){
        return  new GenericResponse();
    }
    public static GenericResponse buildFailure(String msg){
        GenericResponse ret = new GenericResponse();
        ret.code(ResultCode.FAILURE);
        ret.message(msg);
        return ret;
    }
    public static GenericResponse error(String msg){
        GenericResponse ret = GenericResponse.buildFailure(msg);
        return ret;
    }
    public static GenericResponse ok(String msg){
        GenericResponse ret = GenericResponse.buildSuccess(msg);
        return ret;
    }
    public static GenericResponse ok(){
        GenericResponse ret = GenericResponse.buildSuccess();
        return ret;
    }
    public static GenericResponse buildSuccess(){
        GenericResponse ret = GenericResponse.build();
        ret.code(ResultCode.SUCCESS);
        return ret;
    }

    public static GenericResponse buildSuccess(String message){
        GenericResponse ret =  GenericResponse.buildSuccess();
        ret.message(message);
        return ret;
    }

}
