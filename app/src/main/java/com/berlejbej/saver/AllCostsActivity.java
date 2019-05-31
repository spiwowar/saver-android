package com.berlejbej.saver;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.berlejbej.saver.blocks.utils.dialogs.DialogAddCost;
import com.berlejbej.saver.objects.Cost;
import com.berlejbej.saver.costs.all_costs_list_view.AllCostsAdapter;
import com.berlejbej.saver.database.DBHandler;
import com.berlejbej.saver.notifiers.ChangeRecipient;
import com.berlejbej.saver.notifiers.NotifierManager;
import com.berlejbej.saver.utils.Utils;

import java.util.List;

/**
 * Created by Szymon on 2016-10-12.
 */
public class AllCostsActivity extends AppCompatActivity implements ChangeRecipient{

    private View.OnClickListener addCostButtonClickListener = addCostButtonClickListener();
    private AdapterView.OnItemLongClickListener costItemLongClickListener = costItemLongClickListener();

    DialogInterface.OnClickListener okRemoveCostButtonListener = okRemoveCostButtonListener();
    DialogInterface.OnClickListener cancelRemoveCostButtonListener = cancelRemoveCostButtonListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_all_costs);

        setListViewValues();

        NotifierManager.addRecipient(this);

        ImageButton addCostButton = (ImageButton) findViewById(R.id.all_costs_add_button_id);
        addCostButton.setOnClickListener(addCostButtonClickListener);
    }

    private void setListViewValues(){
        DBHandler dbHandler = DBHandler.getInstance(this);
        List<Cost> data = dbHandler.getAllCosts();

        AllCostsAdapter adapter = new AllCostsAdapter(this, R.layout.list_views_all_costs, data);
        ListView listView = (ListView)findViewById(R.id.all_costs_list_view_id);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(costItemLongClickListener);
    }

    private View.OnClickListener addCostButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddCost dialogAddCost = new DialogAddCost(AllCostsActivity.this);
                dialogAddCost.show();
            }
        };
    }

    private View viewToRemove;

    private AdapterView.OnItemLongClickListener costItemLongClickListener(){
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                viewToRemove = view;
                Utils.createRemoveAlertDialog(AllCostsActivity.this, okRemoveCostButtonListener, cancelRemoveCostButtonListener);

                return true;
            }
        };
    }

    private DialogInterface.OnClickListener okRemoveCostButtonListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (viewToRemove != null) {
                    DBHandler dbHandler = DBHandler.getInstance(AllCostsActivity.this);
                    Cost cost = new Cost();
                    cost.setID((Integer) viewToRemove.getTag(R.string.item_id));
                    if (dbHandler.removeCost(cost)) {
                        NotifierManager.notifyChange();
                    }
                    else {
                        Toast.makeText(AllCostsActivity.this, "Could not delete the item", Toast.LENGTH_SHORT).show();
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
                viewToRemove = null;
                dialog.cancel();
            }
        };
    }

    @Override
    public void changePerformed() {
        setListViewValues();
    }
}
