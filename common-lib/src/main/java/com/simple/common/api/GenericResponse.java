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

}
