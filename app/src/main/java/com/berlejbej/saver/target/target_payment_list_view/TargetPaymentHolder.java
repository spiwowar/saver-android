package com.berlejbej.saver.target.target_payment_list_view;

import android.widget.TextView;

/**
 * Created by Szymon on 2016-10-08.
 */
public class TargetPaymentHolder {
    public TextView date;
    public TextView value;

    public TargetPaymentHolder(){}

    public TargetPaymentHolder(TextView date, TextView value){
        this.date = date;
        this.value = value;
    }

    public void setDate(TextView date){
        this.date = date;
    }

    public void setValue(TextView value){
        this.value = value;
    }

    public TextView getDate(){
        return this.date;
    }

    public TextView getValue(){
        return this.value;
    }
}