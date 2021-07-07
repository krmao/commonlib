package com.simple.common.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class SessionUser {
    private boolean   isLoginUser;
    private String    userId;
    private String    openId;
    private String[]  roles;
    private boolean   isWechatUser;
    public  boolean isLoginUser(){
        return this.isLoginUser;
    }
    public  boolean isWechatUser(){
        return this.isWechatUser;
    }
}
