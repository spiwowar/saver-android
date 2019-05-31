package com.berlejbej.saver.constant_costs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.berlejbej.saver.R;
import com.berlejbej.saver.objects.ConstantCost;
import com.berlejbej.saver.objects.Cost;

import java.util.List;

/**
 * Created by Szymon on 2016-10-08.
 */
public class ConstantCostsAdapter extends ArrayAdapter<Cost> {

    Context context;
    int layoutResourceId;
    List<ConstantCost> constantCosts = null;

    public ConstantCostsAdapter(Context context, int layoutResourceId, List constantCosts) {
        super(context, layoutResourceId, constantCosts);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.constantCosts = constantCosts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ConstantCostsHolder holder;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ConstantCostsHolder();
            holder.setName((TextView) row.findViewById(R.id.settings_constant_costs_item_name_id));
            holder.setValue((TextView) row.findViewById(R.id.settings_constant_costs_item_value_id));
            holder.setCategory((TextView) row.findViewById(R.id.settings_constant_costs_item_category_id));

            row.setTag(R.string.holder_tag, holder);
        }
        else {
            holder = (ConstantCostsHolder)row.getTag(R.string.holder_tag);
        }

        ConstantCost constantCost = constantCosts.get(position);
        holder.getName().setText(constantCost.getName());
        holder.getValue().setText(String.valueOf(constantCost.getValue()));
        holder.getCategory().setText(constantCost.getCategory());

        row.setTag(R.string.item_id, constantCost.getID());

        return row;
    }
}
