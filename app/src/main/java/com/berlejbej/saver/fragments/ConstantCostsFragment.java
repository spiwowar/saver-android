package com.berlejbej.saver.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.berlejbej.saver.R;
import com.berlejbej.saver.objects.ConstantCost;
import com.berlejbej.saver.constant_costs.ConstantCostsAdapter;
import com.berlejbej.saver.database.DBHandler;
import com.berlejbej.saver.notifiers.ChangeRecipient;
import com.berlejbej.saver.notifiers.NotifierManager;
import com.berlejbej.saver.utils.Utils;

import java.util.List;

/**
 * Created by Szymon on 2016-10-07.
 */
public class ConstantCostsFragment extends Fragment implements ChangeRecipient{

    private View.OnClickListener addButtonClickListener = addButtonClickListener();
    private View.OnClickListener approveButtonClickListener = approveButtonClickListener();
    private View.OnClickListener addingCancelButtonClickListener = addingCancelButtonClickListener();

    private AdapterView.OnItemLongClickListener constantCostItemLongClickListener = constantCostItemLongClickListener();
    DialogInterface.OnClickListener okRemoveConstantCostButtonListener = okRemoveConstantCostButtonListener();
    DialogInterface.OnClickListener cancelRemoveConstantCostButtonListener = cancelRemoveConstantCostButtonListener();

    ConstantCostsAdapter adapter;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragments_constant_costs,
                container, false);

        showAllConstantCosts();

        Button addButton = (Button) view.findViewById(R.id.settings_constant_costs_add_button_id);
        Button approveButton = (Button) view.findViewById(R.id.settings_constant_costs_add_approve_button_id);
        Button addingCancelButton = (Button) view.findViewById(R.id.settings_constant_costs_add_cancel_button_id);

        addButton.setOnClickListener(addButtonClickListener);
        approveButton.setOnClickListener(approveButtonClickListener);
        addingCancelButton.setOnClickListener(addingCancelButtonClickListener);

        NotifierManager.addRecipient(this);

        return view;
    }

    public void showAllConstantCosts(){
        DBHandler dbHandler = DBHandler.getInstance(getContext());
        List<ConstantCost> allConstantCosts = dbHandler.getAllConstantCosts();

        adapter = new ConstantCostsAdapter(getContext(), R.layout.fragments_constant_costs_item, allConstantCosts);
        ListView listView = (ListView) view.findViewById(R.id.settings_costs_list_view_id);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(constantCostItemLongClickListener);
    }

    public void updateConstantCosts(){
        showAllConstantCosts();
    }

    private View.OnClickListener addButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button addButton = (Button) view.findViewById(R.id.settings_constant_costs_add_button_id);
                LinearLayout addConstantCostsView = (LinearLayout) view.findViewById(R.id.settings_constant_costs_add_item_id);
                addConstantCostsView.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.GONE);
            }
        };
    }

    private View.OnClickListener approveButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEditText = (EditText) view.findViewById(R.id.settings_constant_costs_add_cost_name_id);
                EditText valueEditText = (EditText) view.findViewById(R.id.settings_constant_costs_add_cost_value_id);
                Spinner categorySpinner = (Spinner) view.findViewById(R.id.settings_constant_costs_category_spinner_id);

                String name = nameEditText.getText().toString().trim();
                String category = categorySpinner.getSelectedItem().toString();

                double value;

                if (name.equals("")) {
                    Toast.makeText(v.getContext(), getResources().getText(R.string.lack_of_cost_name), Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    value = Double.parseDouble(valueEditText.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(v.getContext(), getResources().getText(R.string.lack_of_cost_value), Toast.LENGTH_LONG).show();
                    return;
                }

                DBHandler dbHandler = DBHandler.getInstance(getContext());
                ConstantCost cost = new ConstantCost(name, value, category);
                dbHandler.addConstantCost(cost);

                Button addButton = (Button) view.findViewById(R.id.settings_constant_costs_add_button_id);
                LinearLayout addConstantCostsView = (LinearLayout) view.findViewById(R.id.settings_constant_costs_add_item_id);
                addConstantCostsView.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);
                updateConstantCosts();

                nameEditText.setText("");
                valueEditText.setText("");
            }
        };
    }

    private View.OnClickListener addingCancelButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEditText = (EditText) view.findViewById(R.id.settings_constant_costs_add_cost_name_id);
                EditText valueEditText = (EditText) view.findViewById(R.id.settings_constant_costs_add_cost_value_id);
                nameEditText.setText("");
                valueEditText.setText("");

                Button addButton = (Button) view.findViewById(R.id.settings_constant_costs_add_button_id);
                LinearLayout addConstantCostsView = (LinearLayout) view.findViewById(R.id.settings_constant_costs_add_item_id);
                addConstantCostsView.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);
            }
        };
    }

    private View viewToRemove;

    private AdapterView.OnItemLongClickListener constantCostItemLongClickListener(){
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                viewToRemove = view;
                Utils.createRemoveAlertDialog(ConstantCostsFragment.this.getActivity(), okRemoveConstantCostButtonListener, cancelRemoveConstantCostButtonListener);

                return true;
            }
        };
    }

    private DialogInterface.OnClickListener okRemoveConstantCostButtonListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (viewToRemove != null) {
                    DBHandler dbHandler = DBHandler.getInstance(ConstantCostsFragment.this.getActivity());
                    ConstantCost constantCost = new ConstantCost();
                    constantCost.setID((Integer) viewToRemove.getTag(R.string.item_id));
                    if (dbHandler.removeConstantCost(constantCost)) {
                        NotifierManager.notifyChange();
                    }
                    else {
                        Toast.makeText(ConstantCostsFragment.this.getActivity(), "Could not delete the item", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.cancel();
            }
        };
    }

    private DialogInterface.OnClickListener cancelRemoveConstantCostButtonListener() {
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
        if (isAdded()) {
            updateConstantCosts();
        }
    }
}