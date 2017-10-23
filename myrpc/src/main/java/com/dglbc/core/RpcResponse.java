package com.dglbc.core;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse {
    private String requestId;
    private Throwable error;
    private Object result;
}
