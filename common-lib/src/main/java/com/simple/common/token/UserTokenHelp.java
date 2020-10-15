package com.simple.common.token;

import com.simple.core.redis.JedisDBEnum;
import com.simple.core.redis.JedisHelper;
import org.apache.commons.lang.StringUtils;

/**
 * 用户Token信息
 * @author hejinguo
 * @version $Id: UserTokenUtil.java, v 0.1 2019年11月19日 下午7:52:08
 */
public class UserTokenHelp {
    /**
     * 获取wechat用户userId
     * @param token
     * @return
     */
    public static Integer getWechatUserId(String token) {
        if(StringUtils.isBlank(token)){
            return  null;
        }
        String tokenValue = JedisHelper.getInstance().get(token, JedisDBEnum.WECHAT);
        if(StringUtils.isBlank(tokenValue)){
            return  null;
        }
        String[] tokenValueAry= DesTokenUtil.decrypt(tokenValue).split(",");
        return  Integer.valueOf(tokenValueAry[0]);
    }

    /**
     * 获取PC用户信息
     * @param token
     * @return
     */
    public static Integer getPcUserId(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String tokenValue = JedisHelper.getInstance().get(token, JedisDBEnum.PC);
        if (StringUtils.isBlank(tokenValue)) {
            return null;
        }
        String[] tokenValueAry = DesPcTokenUtil.decrypt(tokenValue).split(",");
        return Integer.valueOf(tokenValueAry[0]);
    }

}
