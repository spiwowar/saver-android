package com.berlejbej.saver.blocks;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.berlejbej.saver.AllCostsActivity;
import com.berlejbej.saver.R;
import com.berlejbej.saver.blocks.utils.costs.CostsExpandableListView;
import com.berlejbej.saver.blocks.utils.costs.CostsListAdapter;
import com.berlejbej.saver.objects.Cost;
import com.berlejbej.saver.database.DBHandler;
import com.berlejbej.saver.notifiers.NotifierManager;
import com.berlejbej.saver.utils.Utils;

import java.util.List;

/**
 * Created by Szymon on 2016-10-07.
 */
public class CostsBlock extends Block {

    private Context context;
    private View block;

    private boolean isListExpanded = false;

    private View costToRemove = null;

    private CostsExpandableListView costsExpandableListView;
    private CostsListAdapter costsListAdapter;
    private String costsListTitle;
    private List<Cost> costsListItems;

    private TextView seeAllTextView;
    private TextView lackOfCostsTextView;

    private View.OnClickListener seeAllCostsClicked = seeAllCostsClicked();
    private ExpandableListView.OnGroupExpandListener costsExpandListener = costsExpandListener();
    private ExpandableListView.OnGroupCollapseListener costsCollapseListener = costsCollapseListener();
    private AdapterView.OnItemLongClickListener costItemLongClickListener = costItemLongClickListener();

    private DialogInterface.OnClickListener okRemoveCostButtonListener = okRemoveCostButtonListener();
    private DialogInterface.OnClickListener cancelRemoveCostButtonListener = cancelRemoveCostButtonListener();

    public CostsBlock(Context context) {
        super(context);
        this.context = context;
    }

    public CostsBlock(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CostsBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public CostsBlock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    @Override
    public void create(ViewGroup viewGroup){
        block = ((Activity) context).getLayoutInflater().inflate(R.layout.block_costs, viewGroup);

        costsExpandableListView = (CostsExpandableListView) block.findViewById(R.id.block_costs_expandable_list_view_id);
        seeAllTextView = (TextView) block.findViewById(R.id.block_costs_see_all_costs_id);
        lackOfCostsTextView = (TextView) block.findViewById(R.id.main_lack_of_costs_text_view_id);

    }

    @Override
    public void update() {

        costsExpandableListView.setOnGroupExpandListener(costsExpandListener);
        costsExpandableListView.setOnGroupCollapseListener(costsCollapseListener);
        costsExpandableListView.setOnItemLongClickListener(costItemLongClickListener);
        costsListTitle = getResources().getString(R.string.costs);

        costsListItems = DBHandler.getInstance(getContext()).getAllCosts();
        costsListAdapter = new CostsListAdapter(block.getContext(), costsListTitle, costsListItems);
        costsExpandableListView.setAdapter(costsListAdapter);

        if (isListExpanded){
            costsExpandableListView.expandGroup(0);
        }
        else {
            costsExpandableListView.collapseGroup(0);
        }

        //set see all onclicklistener
        seeAllTextView.setOnClickListener(seeAllCostsClicked);
    }


    public ExpandableListView.OnGroupExpandListener costsExpandListener(){
        return new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                /* There are no costs defined */
                if (costsListAdapter.getChildrenCount(groupPosition) == 0){
                    lackOfCostsTextView.setVisibility(View.VISIBLE);
                    seeAllTextView.setVisibility(View.GONE);
                }
                else {
                    seeAllTextView.setVisibility(View.VISIBLE);
                    lackOfCostsTextView.setVisibility(View.GONE);
                }

                isListExpanded = true;
            }
        };
    }

    public ExpandableListView.OnGroupCollapseListener costsCollapseListener(){
        return new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                seeAllTextView.setVisibility(View.GONE);
                lackOfCostsTextView.setVisibility(View.GONE);

                isListExpanded = false;
            }
        };
    }

    public AdapterView.OnItemLongClickListener costItemLongClickListener(){
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                costToRemove = view;
                Utils.createRemoveAlertDialog(getContext(),
                        okRemoveCostButtonListener, cancelRemoveCostButtonListener);
                return true;
            }
        };
    }

    private DialogInterface.OnClickListener okRemoveCostButtonListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (costToRemove != null) {
                    DBHandler dbHandler = DBHandler.getInstance(getContext());
                    Cost cost = new Cost();
                    cost.setID((Integer) costToRemove.getTag(R.string.item_id));
                    if (dbHandler.removeCost(cost)) {
                        NotifierManager.notifyChange();
                    }
                    else {
                        Toast.makeText(getContext(), "Could not remove the item", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.cancel();
            }
        };
    }

    private DialogInterface.OnClickListener cancelRemoveCostButtonListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                costToRemove = null;
                dialog.cancel();
            }
        };
    }

    public View.OnClickListener seeAllCostsClicked(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllCostsActivity.class);
                getContext().startActivity(intent);
            }
        };
    }

    /*
 * TODO: Make this to remember in sharedpreferences whether it is expanded or not, and do it on resume
 */
    /*boolean groupExpanded;

    public void onPause(){
        super.onPause();
        groupExpanded = costsExpandableListView.isGroupExpanded(0);
    }

    public void onResume(){
        super.onResume();
        if (groupExpanded){
            costsExpandableListView.expandGroup(0);
        }
        else {
            costsExpandableListView.collapseGroup(0);
        }
    }*/


}

