package com.berlejbej.saver.objects;

/**
 * Created by Szymon on 2016-10-08.
 */
public class Target {

    private Integer id = null;
    private String name;
    private double targetValue;
    private double remainingValue;
    private String startDate;
    private String endDate;

    public Target(){}

    public Target(String name, double targetValue, String startDate, String endDate){
        this.name = name;
        this.targetValue = targetValue;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Target(String name, double targetValue, double remainingValue, String startDate, String endDate){
        this.name = name;
        this.targetValue = targetValue;
        this.remainingValue = remainingValue;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setID(Integer id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setTargetValue(double targetValue){
        this.targetValue = targetValue;
    }

    public void setRemainingValue(double remainingValue){
        this.remainingValue = remainingValue;
    }

    public void setStartDate(String startDate){
        this.startDate = startDate;
    }

    public void setEndDate(String endDate){
        this.endDate = endDate;
    }

    public Integer getID(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public double getTargetValue(){
        return this.targetValue;
    }

    public double getRemainingValue(){
        return this.remainingValue;
    }

    public String getStartDate(){
        return this.startDate;
    }

    public String getEndDate(){
        return this.endDate;
    }
}
