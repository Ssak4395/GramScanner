package com.example.bar_code_scanner.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Scans")
public class Scan {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "COILid")
    private String COILDID;



    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public double getWEIGHT() {
        return WEIGHT;
    }

    public void setWEIGHT(double WEIGHT) {
        this.WEIGHT = WEIGHT;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getCOLOR() {
        return COLOR;
    }

    public void setCOLOR(String COLOR) {
        this.COLOR = COLOR;
    }

    @ColumnInfo(name = "Type")
    private String TYPE;
    @ColumnInfo(name = "Weight")
    private double WEIGHT;
    @ColumnInfo(name = "Status")
    private String STATUS;
    @ColumnInfo(name= "Date")
    private Date timeScanned;

    public Date getTimeScanned() {
        return timeScanned;
    }

    public void setTimeScanned(Date timeScanned) {
        this.timeScanned = timeScanned;
    }
//    @ColumnInfo(name = "Date_Input")
//    private Date DATE_IN;


    public Scan(@NonNull String COILDID, String TYPE, double WEIGHT, String STATUS, String COLOR) {
        this.COILDID = COILDID;
        this.TYPE = TYPE;
        this.WEIGHT = WEIGHT;
        this.STATUS = STATUS;
        this.COLOR = COLOR;
    }

    @NonNull
    public String getCOILDID() {
        return COILDID;
    }

    public void setCOILDID(@NonNull String COILDID) {
        this.COILDID = COILDID;
    }

    @ColumnInfo(name = "Colours")
    private String COLOR;


}
