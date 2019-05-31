package com.berlejbej.saver.objects;

/**
 * Created by Szymon on 2016-10-22.
 */
public class ConstantCost {

    private Integer id = null;
    private String name;
    private double value;
    private String category;

    public ConstantCost(){}

    public ConstantCost(String name, double value, String category){
        this.name = name;
        this.value = value;
        this.category = category;
    }

    public void setID(Integer id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setValue(double value){
        this.value = value;
    }

    public void setCategory(String category){
        this.category = category;
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

    public String getCategory(){
        return this.category;
    }
}
