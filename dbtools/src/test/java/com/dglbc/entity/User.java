package com.dglbc.entity;

import com.dglbc.dbtools.annotation.FilterCondition;
import com.dglbc.dbtools.annotation.NotInclude;
import lombok.*;

/**
 * Created by LBC on 2018/3/9
 **/
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotInclude
    @FilterCondition
    private Integer sequence;
    private String name;
    @NotInclude
    private String remark;

    public User(Integer sequence, String name) {
        this.sequence = sequence;
        this.name = name;
    }
}
