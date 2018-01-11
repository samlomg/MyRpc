package com.dglbc.jdbc.face;

import java.sql.ResultSet;

public interface IVo<T> {
	public T row(ResultSet rs, int rowNum) throws Exception;
}
