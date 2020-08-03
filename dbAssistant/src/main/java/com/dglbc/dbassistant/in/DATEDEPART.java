package com.dglbc.dbassistant.in;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
public enum DATEDEPART {
    year("year"),
    quarter("quarter"),
    month("month"),
    dayofyear("dayofyear"),
    day("day"),
    week("week"),
    hour("hour"),
    minute("minute"),
    second("second"),
    millisecond("millisecond"),
    microsecond("microsecond"),
    nanosecond("nanosecond"),
    ISO_WEEK("ISO_WEEK");

    private String c;

    DATEDEPART(String string) {
        this.c = string;
    }
}
