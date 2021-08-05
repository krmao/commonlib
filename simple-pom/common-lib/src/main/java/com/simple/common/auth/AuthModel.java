package com.simple.common.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class AuthModel  {
    private String  token;
    private String  userId;
    //private Integer id;
    private String  openId;
    private String  roles;
}
