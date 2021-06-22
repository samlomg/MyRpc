package com.dglbc.dbassistant.check;


import com.dglbc.dbassistant.annotation.MyColumn;
import com.dglbc.dbassistant.annotation.MyTable;

@MyTable(tableName = "T0027",alias = "t0027")
public class T0027 {
    private Integer sequence;
    @MyColumn(columnName = "begtime",as = "begTime")
    private String begTime;
    @MyColumn(columnName = "endtime",as = "endTime")
    private String endTime;
    private Integer flag;
    private String goodCode;
    private String mcu;
    private String zheKou;

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getBegTime() {
        return begTime;
    }

    public void setBegTime(String begTime) {
        this.begTime = begTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getGoodCode() {
        return goodCode;
    }

    public void setGoodCode(String goodCode) {
        this.goodCode = goodCode;
    }

    public String getMcu() {
        return mcu;
    }

    public void setMcu(String mcu) {
        this.mcu = mcu;
    }

    public String getZheKou() {
        return zheKou;
    }

    public void setZheKou(String zheKou) {
        this.zheKou = zheKou;
    }
}
