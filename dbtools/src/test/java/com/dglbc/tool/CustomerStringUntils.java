package com.dglbc.tool;

public class CustomerStringUntils {
    public static String firstUpper(String sourceString) {
        sourceString = sourceString.replaceFirst(sourceString.substring(0, 1), sourceString.substring(0, 1).toUpperCase());
        return sourceString;
    }

    public static String firstLow(String sourceString) {
        sourceString = sourceString.replaceFirst(sourceString.substring(0, 1), sourceString.substring(0, 1).toLowerCase());
        return sourceString;
    }
}
