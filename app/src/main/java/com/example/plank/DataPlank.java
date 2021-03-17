package com.example.plank;

public class DataPlank {

    int image;
    String day;

    public DataPlank(int image, String day){

        this.day = day;
        this.image = image;

    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


}
