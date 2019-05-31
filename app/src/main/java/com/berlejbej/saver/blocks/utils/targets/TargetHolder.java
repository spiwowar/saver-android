package com.berlejbej.saver.blocks.utils.targets;

import android.widget.TextView;

/**
 * Created by Szymon on 2016-10-08.
 */
public class TargetHolder {
    public TextView name;
    public TextView targetValue;
    public TextView remainingValue;
    public TextView date;

    public TargetHolder(){}

    public TargetHolder(TextView name, TextView targetValue, TextView remainingValue, TextView date){
        this.name = name;
        this.targetValue = targetValue;
        this.remainingValue = remainingValue;
        this.date = date;
    }

    public void setName(TextView name){
        this.name = name;
    }

    public void setTargetValue(TextView targetValue){
        this.targetValue = targetValue;
    }

    public void setRemainingValue(TextView remainingValue){
        this.remainingValue = remainingValue;
    }

    public void setDate(TextView date){
        this.date = date;
    }

    public TextView getName(){
        return this.name;
    }

    public TextView getTargetValue(){
        return this.targetValue;
    }

    public TextView getRemainingValue(){
        return this.remainingValue;
    }

    public TextView getDate(){
        return this.date;
    }
}