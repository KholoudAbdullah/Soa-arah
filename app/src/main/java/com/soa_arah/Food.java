package com.soa_arah;

/**
 * Created by ahlam on 2/16/2018.
 */

public class Food {
    private String Name;
    private String Image;
    private String Keyword;
    private String Calories;
    private String Garms;

    public Food(){

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

    public String getGarms() {
        return Garms;
    }

    public void setGarms(String garms) {
        Garms = garms;
    }

    public Food(String name, String image, String keyword, String calories, String garms) {
        Name = name;
        Image = image;
        Keyword = keyword;
        Calories = calories;
        Garms = garms;
    }

}