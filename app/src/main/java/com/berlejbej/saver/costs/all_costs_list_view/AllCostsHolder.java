package com.berlejbej.saver.costs.all_costs_list_view;

import android.widget.TextView;

/**
 * Created by Szymon on 2016-10-08.
 */
public class AllCostsHolder {
    private TextView category;
    private TextView value;
    private TextView date;

    public AllCostsHolder(){}

    public AllCostsHolder(TextView category, TextView value, TextView date){
        this.category = category;
        this.value = value;
        this.date = date;
    }

    public void setCategory(TextView category){
        this.category = category;
    }

    public void setValue(TextView value){
        this.value = value;
    }

    public void setDate(TextView date){
        this.date = date;
    }

    public TextView getCategory(){
        return this.category;
    }

    public TextView getValue(){
        return this.value;
    }

    public TextView getDate(){
        return this.date;
    }
}