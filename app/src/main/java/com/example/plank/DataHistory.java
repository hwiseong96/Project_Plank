package com.example.plank;

public class DataHistory {
    int image;
    String month, day, nDay, plank1, plank2, memo;

    DataHistory(String month, String day, String nDay, String plank1, String plank2, String memo, int image) {
        this.day = day;
        this.month = month;
        this.nDay = nDay;
        this.plank1 = plank1;
        this.plank2 = plank2;
        this.memo = memo;
        this.image = image;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getnDay() {
        return nDay;
    }

    public void setnDay(String nDay) {
        this.nDay = nDay;
    }

    public String getPlank1() {
        return plank1;
    }

    public void setPlank1(String plank1) {
        this.plank1 = plank1;
    }

    public String getPlank2() {
        return plank2;
    }

    public void setPlank2(String plank2) {
        this.plank2 = plank2;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


}
