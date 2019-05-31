package com.berlejbej.saver.blocks.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.berlejbej.saver.R;
import com.berlejbej.saver.objects.Cost;
import com.berlejbej.saver.database.DBHandler;
import com.berlejbej.saver.notifiers.NotifierManager;
import com.berlejbej.saver.utils.DateUtils;

/**
 * Created by Szymon on 2017-01-28.
 */
public class DialogAddCost extends Dialog {

    private Spinner spinnerCategories;
    private EditText otherCategoryET;
    private EditText costAmountET;
    private Button okButton;
    private Button cancelButton;

    private View.OnClickListener cancelButtonClickListener = cancelButtonClickListener();
    private View.OnClickListener okButtonClickListener = okButtonClickListener();
    private AdapterView.OnItemSelectedListener spinnerCategoriesItemSelected = spinnerCategoriesItemSelected();
    private boolean otherSet;

    public DialogAddCost(Context context) {
        super(context);
        init();
    }

    protected DialogAddCost(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public DialogAddCost(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init(){
        setContentView(R.layout.block_dialog_add_cost);

        okButton = (Button) findViewById(R.id.add_cost_ok_button_id);
        cancelButton = (Button) findViewById(R.id.add_cost_cancel_button_id);
        spinnerCategories = (Spinner) findViewById(R.id.add_cost_categories_spinner_id);
        otherCategoryET = (EditText) findViewById(R.id.add_cost_category_name_edit_text_id);
        costAmountET = (EditText) findViewById(R.id.add_cost_how_much_edit_text_id);

        otherCategoryET.setVisibility(View.GONE);
        okButton.setOnClickListener(okButtonClickListener);
        cancelButton.setOnClickListener(cancelButtonClickListener);
        spinnerCategories.setOnItemSelectedListener(spinnerCategoriesItemSelected);
    }

    private void updateDB(Cost cost){
        DBHandler dbHandler = DBHandler.getInstance(getContext().getApplicationContext());
        dbHandler.addCost(cost);
        NotifierManager.notifyChange();
    }

    private View.OnClickListener cancelButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }

    private View.OnClickListener okButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double costValue;
                String categoryName;
                String date = DateUtils.getTodayDateString();

                try{
                    costValue = Double.parseDouble(costAmountET.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(v.getContext(), getContext().getResources().getText(R.string.lack_of_cost_value), Toast.LENGTH_LONG).show();
                    return;
                }

                if (otherSet){
                    categoryName = otherCategoryET.getText().toString().trim();
                    if (categoryName.isEmpty()){
                        Toast.makeText(v.getContext(), getContext().getResources().getText(R.string.lack_of_cost_name), Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                else {
                    categoryName = spinnerCategories.getSelectedItem().toString();
                }

                updateDB(new Cost(categoryName, costValue, date));
                dismiss();
            }
        };
    }

    private AdapterView.OnItemSelectedListener spinnerCategoriesItemSelected() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (((TextView) view).getText().equals("Inne")){
                    otherCategoryET.setVisibility(View.VISIBLE);
                    otherSet = true;
                }
                else {
                    otherSet = false;
                    otherCategoryET.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
    }
}
