package com.simple.common.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseStatus {
    public String trace_id = "";
    private ResultCode code = ResultCode.SUCCESS;
    private String message = "SUCCESS";
}
