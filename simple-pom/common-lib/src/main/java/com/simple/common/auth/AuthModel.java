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
public class AuthModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String  token;
    private String  userId;
    private String  openId;
}
