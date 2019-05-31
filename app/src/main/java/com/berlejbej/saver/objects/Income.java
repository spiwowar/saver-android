package com.berlejbej.saver.objects;

/**
 * Created by Szymon on 2016-10-22.
 */
public class Income {

    public final static int MONTHLY = 0;
    public final static int QUARTERLY = 1;
    public final static int YEARLY = 2;
    public final static int TWO_WEEKS = 3;
    public final static int WEEKLY = 4;
    public final static int DAILY = 5;
    public final static int NON_STANDARD = 6;

    private double income;
    private int frequency;

    public Income(){}

    public Income(double income){
        this.income = income;
    }

    public Income setIncome(double income){
        this.income = income;
        return this;
    }

    public double getIncome(){
        return this.income;
    }

    public Income setFrequency(int frequency){
        this.frequency = frequency;
        return this;
    }

    public int getFrequency(){
        return this.frequency;
    }
}
