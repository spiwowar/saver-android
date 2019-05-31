package com.berlejbej.saver.blocks.utils.costs;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.berlejbej.saver.R;
import com.berlejbej.saver.blocks.utils.dialogs.DialogAddCost;
import com.berlejbej.saver.objects.Cost;
import com.berlejbej.saver.utils.ClickableFrameLayout;
import com.berlejbej.saver.utils.Constants;

import java.util.List;

/**
 * Created by Szymon on 2016-10-10.
 */
public class CostsListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private String expandableListTitle;
    private List<Cost> expandableListItems;
    private View.OnClickListener costsAddClickListener = costsAddClickListener();

    private boolean addButtonClickListenerSet = false;

    public CostsListAdapter(Context context, String expandableListTitle, List<Cost> expandableListItems) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListItems = expandableListItems;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListItems.get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Cost cost = (Cost) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.costs_expandable_list_item, null);
        }
        TextView costCategory = (TextView) convertView.findViewById(R.id.cost_item_category_id);
        TextView costValue = (TextView) convertView.findViewById(R.id.cost_item_value_id);
        TextView costDate = (TextView) convertView.findViewById(R.id.cost_item_date_id);
        costCategory.setText(cost.getCategory());
        costValue.setText(String.format("%.2f", cost.getValue()));
        costDate.setText(cost.getDate());

        convertView.setTag(R.string.item_id, cost.getID());

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        if (this.expandableListItems.size() > Constants.MAX_COSTS_ITEMS){
            return Constants.MAX_COSTS_ITEMS;
        }
        return this.expandableListItems.size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.costs_expandable_list_header, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.costs_header_title_id);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

        if (!addButtonClickListenerSet) {
            ClickableFrameLayout costsAddSpace = (ClickableFrameLayout) convertView.findViewById(R.id.costs_add_button_space_id);
            costsAddSpace.setOnClickListener(costsAddClickListener);
            addButtonClickListenerSet = true;
        }
        return convertView;
    }



    public View.OnClickListener costsAddClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        };
    }

    private void createDialog() {
        final DialogAddCost dialog = new DialogAddCost(context);
        dialog.show();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}