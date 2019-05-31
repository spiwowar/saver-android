package com.berlejbej.saver.additional_income;

import android.widget.TextView;

/**
 * Created by Szymon on 2016-10-08.
 */
public class AdditionalIncomeHolder {
    private TextView name;
    private TextView value;
    private TextView date;

    public AdditionalIncomeHolder(){}

    public AdditionalIncomeHolder(TextView name, TextView value, TextView date){
        this.name = name;
        this.value = value;
        this.date = date;
    }
    
    public void setName(TextView name){
        this.name = name;
    }

    public void setValue(TextView value){
        this.value = value;
    }

    public void setDate(TextView date){
        this.date = date;
    }

    public TextView getName(){
        return this.name;
    }

    public TextView getValue(){
        return this.value;
    }

    public TextView getDate(){
        return this.date;
    }

}