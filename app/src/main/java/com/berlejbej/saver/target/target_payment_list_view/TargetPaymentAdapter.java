package com.berlejbej.saver.target.target_payment_list_view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.berlejbej.saver.PaymentMonth;
import com.berlejbej.saver.R;
import com.berlejbej.saver.objects.Target;
import com.berlejbej.saver.utils.Constants;

import java.util.List;

/**
 * Created by Szymon on 2016-11-07.
 */
public class TargetPaymentAdapter extends ArrayAdapter<Target> {

    Context context;
    int layoutResourceId;
    List<PaymentMonth> paymentMonths = null;
    private boolean seeAll = false;

    public TargetPaymentAdapter(Context context, int layoutResourceId, List paymentMonths) {
        super(context, layoutResourceId, paymentMonths);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.paymentMonths = paymentMonths;
    }

    public void seeAll(boolean seeAll){
        this.seeAll = seeAll;
    }

    public boolean isExpanded(){
        return this.seeAll;
    }

    @Override
    public int getCount() {
        if (!seeAll && paymentMonths.size() > Constants.MAX_PAYMENT_ITEMS){
            return Constants.MAX_PAYMENT_ITEMS;
        }
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TargetPaymentHolder targetPaymentHolder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            targetPaymentHolder = new TargetPaymentHolder();
            targetPaymentHolder.setDate((TextView) row.findViewById(R.id.target_payment_list_view_date_id));
            targetPaymentHolder.setValue((TextView) row.findViewById(R.id.target_payment_list_view_value_id));

            row.setTag(R.string.holder_tag, targetPaymentHolder);
        }
        else {
            targetPaymentHolder = (TargetPaymentHolder)row.getTag(R.string.holder_tag);
        }

        PaymentMonth paymentMonth = paymentMonths.get(position);
        targetPaymentHolder.getDate().setText(paymentMonth.getDate());
        targetPaymentHolder.getValue().setText(String.valueOf(paymentMonth.getValue()));

        return row;
    }
}
