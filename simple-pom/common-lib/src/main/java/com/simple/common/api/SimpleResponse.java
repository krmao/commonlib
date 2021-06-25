package com.simple.common.api;


import lombok.AllArgsConstructor;
import lombok.Builder;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleResponse<T> extends BaseResponse {
    private T data;

    public SimpleResponse<T> failure(String msg){
        this.code(ResultCode.FAILURE);
        if(null != msg){
            this.message(msg);
        }
        return this;
    }
    public SimpleResponse<T> failure(){
        this.failure("a failure");
        return this;
    }
    public SimpleResponse<T> success(T result){
        this.setData(result);
        return this;
    }

}
