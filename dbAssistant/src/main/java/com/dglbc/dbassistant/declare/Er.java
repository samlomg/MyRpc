package com.dglbc.dbassistant.declare;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Er<T,S> {
    private T first;
    private S second;
}
