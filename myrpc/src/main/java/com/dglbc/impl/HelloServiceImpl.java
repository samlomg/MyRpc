package com.dglbc.impl;

import com.dglbc.api.HelloService;

public class HelloServiceImpl implements HelloService {

    public String hello(String name) {
        return "Hello! " + name;
    }
}
