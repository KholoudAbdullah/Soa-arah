package com.soa_arah;

/**
 * Created by ahlam on 2/16/2018.
 */

public class Food {
    private String Name;
    private String Image;
    private String Keyword;
    private String Calories;
    private String Quantity;
    private String standard;
    private String BarcodN;
    private String ImageTable;

    public Food(){}

    public Food(String name, String image, String keyword, String calories, String standard,String quantity) {
        Name = name;
        Image = image;
        Keyword = keyword;
        Calories = calories;
        Quantity = quantity;
        this.standard=standard;
    }


    public void setBarcodN(String barcodN) {
        BarcodN = barcodN;
    }

    public void setImageTable(String imageTable) {
        ImageTable = imageTable;
    }

    public String getBarcodN() {
        return BarcodN;
    }

    public String getImageTable() {
        return ImageTable;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getStandard() {
        return standard;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getKeyword() {
        return Keyword;
    }

    public void setKeyword(String keyword) {
        Keyword = keyword;
    }

    public String getCalories() {
        return Calories;
    }

    public void setCalories(String calories) {
        Calories = calories;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }



}