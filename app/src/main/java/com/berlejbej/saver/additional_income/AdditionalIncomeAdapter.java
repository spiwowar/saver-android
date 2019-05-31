package com.berlejbej.saver.additional_income;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.berlejbej.saver.R;
import com.berlejbej.saver.objects.AdditionalIncome;
import com.berlejbej.saver.objects.Cost;

import java.util.List;

/**
 * Created by Szymon on 2016-10-08.
 */
public class AdditionalIncomeAdapter extends ArrayAdapter<Cost> {

    Context context;
    int layoutResourceId;
    List<AdditionalIncome> additionalIncomes = null;

    public AdditionalIncomeAdapter(Context context, int layoutResourceId, List additionalIncomes) {
        super(context, layoutResourceId, additionalIncomes);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.additionalIncomes = additionalIncomes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AdditionalIncomeHolder holder;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new AdditionalIncomeHolder();
            holder.setName((TextView) row.findViewById(R.id.settings_additional_income_item_name_id));
            holder.setValue((TextView) row.findViewById(R.id.settings_additional_income_item_value_id));
            holder.setDate((TextView) row.findViewById(R.id.settings_additional_income_item_date_id));

            row.setTag(R.string.holder_tag, holder);
        }
        else {
            holder = (AdditionalIncomeHolder)row.getTag(R.string.holder_tag);
        }

        AdditionalIncome additionalIncome = additionalIncomes.get(position);
        holder.getName().setText(additionalIncome.getName());
        holder.getValue().setText(String.valueOf(additionalIncome.getValue()));
        holder.getDate().setText(additionalIncome.getDate());

        row.setTag(R.string.item_id, additionalIncome.getID());

        return row;
    }
}
