package com.berlejbej.saver.objects;

/**
 * Created by Szymon on 2016-10-08.
 */
public class Cost {

    public Integer id = null;
    public String category;
    public double value;
    public String date;

    public Cost(){}

    public Cost(String category, double cost, String date){
        this.category = category;
        this.value = cost;
        this.date = date;
    }

    public void setID(Integer id){
        this.id = id;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public void setValue(double value){
        this.value = value;
    }

    public void setDate(String date){
        this.date = date;
    }

    public Integer getID(){
        return this.id;
    }

    public String getCategory(){
        return this.category;
    }

    public double getValue(){
        return this.value;
    }

    public String getDate() {
        return this.date;
    }
}
