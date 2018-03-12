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

    String Rname;

    public String getCalGoal() {
        return calGoal;
    }

    public void setCalGoal(String calGoal) {
        this.calGoal = calGoal;
    }

    String calGoal;

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    String min;

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    String max;

    public String getNewcalFood() {
        return NewcalFood;
    }

    public void setNewcalFood(String newcalFood) {
        NewcalFood = newcalFood;
    }

    String NewcalFood;

    public String getDailyCal() {
        return dailyCal;
    }

    public void setDailyCal(String dailyCal) {
        this.dailyCal = dailyCal;
    }

    String dailyCal;

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    String water;
}
