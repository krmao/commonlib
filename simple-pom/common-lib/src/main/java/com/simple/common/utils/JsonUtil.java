package com.simple.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import org.apache.commons.beanutils.BeanUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * json工具类
 * @author hejinguo
 * @version $Id: JsonUtil.java, v 0.1 2019年11月17日 下午4:47:32
 */
public class JsonUtil {
    private static SerializeConfig mapping = new SerializeConfig();

    static {
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        mapping.put(Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 对象转换成JSON字符串
     * @author  hejinguo
     */
    public static String writeObjectJSON(Object obj) {
        return JSON.toJSONString(obj, mapping);
    }

    /**
     * json转换成Object
     * @param <T>
     * @param jsonData
     * @param cla
     * @return
     */
    public static final <T> T jsonToObject(String jsonData, Class<T> cla) {
        return JSON.parseObject(jsonData, cla);
    }

    /**
     * JSON字符创转成json
     * @param jsonStr
     * @return
     */
    public static Map parseJSON2Map(String jsonStr) {
        Map mapEs = JSONObject.parseObject(jsonStr, Map.class);
        return mapEs;
    }

    /**
     * map转object 对象
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;
        Object obj = beanClass.newInstance();
        BeanUtils.populate(obj, map);
        return obj;
    }

    /**
     * JSON转 List<T>
     */
    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
        return ts;
    }

    public static String getJsonValue(String rescontent, String key) {
        JSONObject jsonObject;
        String v = null;
        try {
            jsonObject = JSONObject.parseObject(rescontent);
            v = jsonObject.getString(key);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return v;
    }

    /**
     * 返回操作成功消息
     * @param messages
     * @return
     */
    public static String returnJsonSuccess(String messages) {
        return "{\"status\":1,\"message\":\"" + messages + "\"}";
    }

    /**
     * 返回失败的消息
     * @param messages
     * @return
     */
    public static String returnJsonUnSuccess(String messages) {
        return "{\"status\":0,\"message\":\"" + messages + "\"}";
    }

    public static boolean isJson(String string){
        try {
            JSONObject jsonStr= JSONObject.parseObject(string);
            return  true;
        } catch (Exception e) {
            return false;
        }
    }
}
