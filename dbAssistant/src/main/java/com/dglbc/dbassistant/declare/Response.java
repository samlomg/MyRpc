package com.dglbc.dbassistant.declare;

import lombok.*;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Response<T> {
    private int code;
    private String status;
    private T data;

    public Response(int code, String status) {
        this.code = code;
        this.status = status;
    }
}
