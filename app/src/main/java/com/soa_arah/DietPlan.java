package com.soa_arah;

/**
 * Created by HP on 3/7/2018.
 */


public class DietPlan {
    public String getRname() {
        return Rname;
    }

    public void setRname(String rname) {
        Rname = rname;
    }

    private String Rname;

    public String getCalGoal() {
        return calGoal;
    }

    public void setCalGoal(String calGoal) {
        this.calGoal = calGoal;
    }

    private String calGoal;

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    private String min;

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    private String max;

    public String getBMI() {
        return BMI;
    }

    public void setBMI(String BMI) {
        this.BMI = BMI;
    }

    private String BMI;

    public String getDailyCal() {
        return dailyCal;
    }

    public void setDailyCal(String dailyCal) {
        this.dailyCal = dailyCal;
    }

    private String dailyCal;

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    private String water;

    public DietPlan(String rname, String calGoal, String min, String max, String BMI, String dailyCal, String water) {
        Rname = rname;
        this.calGoal = calGoal;
        this.min = min;
        this.max = max;
        this.BMI = BMI;
        this.dailyCal = dailyCal;
        this.water = water;
    }
    public DietPlan(){

    }
}
