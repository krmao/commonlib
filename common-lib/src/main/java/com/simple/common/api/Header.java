package com.simple.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Header{
    private String version = "1.0";
    private String systemCode = "1";
    private String deviceType = "1";
    private String cid = "0000";

}