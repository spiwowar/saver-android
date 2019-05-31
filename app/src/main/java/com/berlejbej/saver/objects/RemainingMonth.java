package com.berlejbej.saver.objects;

/**
 * Created by Szymon on 2016-10-25.
 */
public class RemainingMonth {

    private double remainingValue;

    public RemainingMonth() {}

    public RemainingMonth(double remainingValue){
        this.remainingValue = remainingValue;
    }

    public void setValue(double remainingValue){
        this.remainingValue = remainingValue;
    }

    public double getValue(){
        return this.remainingValue;
    }
}
