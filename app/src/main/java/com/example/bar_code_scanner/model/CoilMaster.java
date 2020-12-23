package com.example.bar_code_scanner.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "COIL_MASTER_20200813")
public class CoilMaster {
    public String getCOILID() {
        return COILID;
    }

    public void setCOILID(String COILID) {
        this.COILID = COILID;
    }

    @ColumnInfo(name = "COILID")
    @PrimaryKey
    @NonNull
    private String COILID;
    @ColumnInfo(name = "WEIGHT")
    private int WEIGHT;
    @ColumnInfo(name = "GAUGE")
    private String GAUGE;
    @ColumnInfo(name = "WIDTH")
    private int WIDTH;
    @ColumnInfo(name = "COLOR")
    private String COLOR;
    @ColumnInfo(name = "STATUS")
    private String STATUS;
    @ColumnInfo(name = "TYPE")
    private String TYPE;



    public int getWEIGHT() {
        return WEIGHT;
    }

    public void setWEIGHT(int WEIGHT) {
        this.WEIGHT = WEIGHT;
    }

    public String getGAUGE() {
        return GAUGE;
    }

    public void setGAUGE(String GAUGE) {
        this.GAUGE = GAUGE;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public void setWIDTH(int WIDTH) {
        this.WIDTH = WIDTH;
    }

    public String getCOLOR() {
        return COLOR;
    }

    public CoilMaster(@NonNull String COILID, int WEIGHT, String GAUGE, int WIDTH, String COLOR, String STATUS, String TYPE) {
        this.COILID = COILID;
        this.WEIGHT = WEIGHT;
        this.GAUGE = GAUGE;
        this.WIDTH = WIDTH;
        this.COLOR = COLOR;
        this.STATUS = STATUS;
        this.TYPE = TYPE;
    }

    public void setCOLOR(String COLOR) {
        this.COLOR = COLOR;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }
}