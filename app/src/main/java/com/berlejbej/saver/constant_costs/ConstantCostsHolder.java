package com.berlejbej.saver.constant_costs;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Szymon on 2016-10-08.
 */
public class ConstantCostsHolder {
    private TextView name;
    private TextView value;
    private TextView category;

    public ConstantCostsHolder(){}

    public ConstantCostsHolder(TextView name, TextView value, TextView category){
        this.name = name;
        this.value = value;
        this.category = category;
    }
    
    public void setName(TextView name){
        this.name = name;
    }

    public void setValue(TextView value){
        this.value = value;
    }

    public void setCategory(TextView category){
        this.category = category;
    }

    public TextView getName(){
        return this.name;
    }

    public TextView getValue(){
        return this.value;
    }

    public TextView getCategory(){
        return this.category;
    }

}