package com.dglbc.data.jdbc.exception;

@SuppressWarnings("serial")
public class AppExp extends RuntimeException {

	public AppExp(String message) {
		super(message);
	}

	public static void error(String desc) {
		throw new AppExp(desc);
	}
}
