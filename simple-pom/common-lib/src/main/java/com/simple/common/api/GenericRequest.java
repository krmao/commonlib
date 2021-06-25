package com.simple.common.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class GenericRequest extends BaseRequest {
    protected JSONObject params;

    public <T> T getObject(Class<T> clazz) {
        // TODO Auto-generated method stub
        return JSON.toJavaObject(this.params, clazz);
    }

    public <T> T get(String key, Class<T> clazz) {
        // TODO Auto-generated method stub
        return JSON.toJavaObject(this.params.getJSONObject(key), clazz);
    }

    public <T> List<T> getTList(String key, Class<T> clazz) {
        return JSON.parseArray(this.params.getString(key), clazz);
    }

    public boolean getBoolean(String key) {
        // TODO Auto-generated method stub
        return this.params.getBooleanValue(key);
    }


    public byte getByte(String key) {
        // TODO Auto-generated method stub
        return this.params.getByteValue(key);
    }



    public double getDouble(String key) {
        // TODO Auto-generated method stub
        return this.params.getDoubleValue(key);
    }


    public float getFloat(String key) {
        // TODO Auto-generated method stub
        return this.params.getFloatValue(key);
    }



    public int getInt(String key) {
        // TODO Auto-generated method stub
        return this.params.getIntValue(key);
    }

    public Integer getInteger(String key) {
        return this.params.getInteger(key);
    }



    public long getLong(String key) {
        // TODO Auto-generated method stub
        return this.params.getLongValue(key);
    }



    public String getString(String key) {
        // TODO Auto-generated method stub
        return this.params.getString(key);
    }

    public String[] getStrings(String key) {
        // TODO Auto-generated method stub
        JSONArray jsonArray = this.params.getJSONArray(key);
        String[] strings = new String[jsonArray.size()];
        for (int i = 0, n = jsonArray.size(); i < n; i++) {
            strings[i] = jsonArray.getString(i);
        }
        return strings;
    }

    public boolean containsKey(Object key) {
        return this.params.containsKey(key);
    }

}