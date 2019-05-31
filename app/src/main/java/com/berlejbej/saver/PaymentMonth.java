package com.berlejbej.saver;

/**
 * Created by Szymon on 2016-11-07.
 */
public class PaymentMonth {

    private String date;
    private double value;

    public PaymentMonth(){}

    public PaymentMonth(String date, double value){
        this.date = date;
        this.value = value;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setValue(double value){
        this.value = value;
    }

    public String getDate(){
        return this.date;
    }

    public double getValue(){
        return this.value;
    }
}
