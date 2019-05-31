package com.berlejbej.saver.blocks.utils.targets;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.berlejbej.saver.R;
import com.berlejbej.saver.objects.Target;

import java.util.List;

/**
 * Created by Szymon on 2016-10-08.
 */
public class TargetAdapter extends ArrayAdapter<Target> {

    Context context;
    int layoutResourceId;
    List<Target> targets = null;

    public TargetAdapter(Context context, int layoutResourceId, List targets) {
        super(context, layoutResourceId, targets);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.targets = targets;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TargetHolder targetHolder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            targetHolder = new TargetHolder();
            targetHolder.setName((TextView)row.findViewById(R.id.list_view_target_name_id));
            targetHolder.setTargetValue((TextView) row.findViewById(R.id.list_view_target_remaining_id));
            targetHolder.setRemainingValue((TextView)row.findViewById(R.id.list_view_target_overall_remaining_id));
            targetHolder.setDate((TextView)row.findViewById(R.id.list_view_target_date_id));

            row.setTag(R.string.holder_tag, targetHolder);
        }
        else {
            targetHolder = (TargetHolder)row.getTag(R.string.holder_tag);
        }

        Target target = targets.get(position);
        targetHolder.name.setText(target.getName());
        targetHolder.getTargetValue().setText(String.format("%.2f", target.getRemainingValue()));
        targetHolder.getRemainingValue().setText(String.format("%.2f", target.getTargetValue()));
        targetHolder.getDate().setText(target.getEndDate());

        row.setTag(R.string.item_id, target.getID());

        return row;
    }


}
