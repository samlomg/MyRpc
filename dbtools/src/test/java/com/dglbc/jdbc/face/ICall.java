package com.dglbc.jdbc.face;

import java.sql.CallableStatement;

public interface ICall {
	//设置参数
	public void callParamed(CallableStatement cst) throws Exception;
	//获取结果
	public boolean callResult(CallableStatement cst) throws Exception;		
}
