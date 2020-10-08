package com.simple.core.redis;

/**
 * redis数据库枚举
 * @author hejinguo
 * @version $Id: JedisDBEnum.java, v 0.1 2019年11月17日 下午5:33:40
 */
public enum JedisDBEnum {
    PC(0), WECHAT(1);

    private int value;

    JedisDBEnum(int type) {
        this.value = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
