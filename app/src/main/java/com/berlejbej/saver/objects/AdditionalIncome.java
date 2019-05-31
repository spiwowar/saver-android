package com.berlejbej.saver.objects;

/**
 * Created by Szymon on 2016-10-22.
 */
public class AdditionalIncome {

    private Integer id = null;
    private String name;
    private double value;
    private String date;

    public AdditionalIncome(){}

    public AdditionalIncome(String name, double value, String date){
        this.name = name;
        this.value = value;
        this.date = date;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setID(Integer id){
        this.id = id;
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

    public String getName(){
        return this.name;
    }

    public double getValue(){
        return this.value;
    }

    public String getDate(){
        return this.date;
    }
}
