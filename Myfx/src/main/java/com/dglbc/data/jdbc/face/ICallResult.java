package com.dglbc.data.jdbc.face;

import java.sql.CallableStatement;

public interface ICallResult<T> {
    public T callResult(CallableStatement cst) throws Exception;
}
