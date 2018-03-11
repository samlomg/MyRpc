package com.dglbc.lam;

import java.util.function.Predicate;

public class SortApple {

    public boolean sourtByLambda(int a,Predicate<Integer> in){
        return in.test(a);
    }
}
