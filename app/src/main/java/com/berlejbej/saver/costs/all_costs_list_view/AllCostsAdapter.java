package com.berlejbej.saver.costs.all_costs_list_view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.berlejbej.saver.R;
import com.berlejbej.saver.objects.Cost;

import java.util.List;

/**
 * Created by Szymon on 2016-10-08.
 */
public class AllCostsAdapter extends ArrayAdapter<Cost> {

    Context context;
    int layoutResourceId;
    List<Cost> costs = null;

    public AllCostsAdapter(Context context, int layoutResourceId, List costs) {
        super(context, layoutResourceId, costs);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.costs = costs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AllCostsHolder holder;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new AllCostsHolder();
            holder.setCategory((TextView) row.findViewById(R.id.list_view_cost_category_id));
            holder.setValue((TextView) row.findViewById(R.id.list_view_cost_value_id));
            holder.setDate((TextView) row.findViewById(R.id.list_view_cost_date_id));

            row.setTag(R.string.holder_tag, holder);
        }
        else {
            holder = (AllCostsHolder)row.getTag(R.string.holder_tag);
        }

        Cost cost = costs.get(position);
        holder.getCategory().setText(cost.getCategory());
        holder.getValue().setText(String.format("%.2f", cost.getValue()));
        holder.getDate().setText(cost.getDate());

        row.setTag(R.string.item_id, cost.getID());

        return row;
    }


}
