package com.berlejbej.saver.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.berlejbej.saver.R;
import com.berlejbej.saver.objects.AdditionalIncome;
import com.berlejbej.saver.additional_income.AdditionalIncomeAdapter;
import com.berlejbej.saver.database.DBHandler;
import com.berlejbej.saver.notifiers.ChangeRecipient;
import com.berlejbej.saver.notifiers.NotifierManager;
import com.berlejbej.saver.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Szymon on 2016-10-07.
 */
public class AdditionalIncomeFragment extends Fragment implements ChangeRecipient {

    private CalendarView calendarView;
    private View.OnClickListener addButtonClickListener = addButtonClickListener();
    private View.OnClickListener approveButtonClickListener = approveButtonClickListener();
    private View.OnClickListener addingCancelButtonClickListener = addingCancelButtonClickListener();
    private CompoundButton.OnCheckedChangeListener todayCheckBoxChanged = todayCheckBoxChanged();

    private AdapterView.OnItemLongClickListener additionalIncomeItemLongClickListener = additionalIncomeItemLongClickListener();
    DialogInterface.OnClickListener okRemoveAdditionalIncomeButtonListener = okRemoveAdditionalIncomeButtonListener();
    DialogInterface.OnClickListener cancelRemoveAdditionalIncomeButtonListener = cancelRemoveAdditionalIncomeButtonListener();

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragments_additional_income,
                container, false);

        showAllAdditionalIncomes();

        calendarView = (CalendarView) view.findViewById(R.id.settings_additional_income_when_calendar_view_id);
        calendarView.setVisibility(View.GONE);

        Button addButton = (Button) view.findViewById(R.id.settings_additional_income_add_button_id);
        Button approveButton = (Button) view.findViewById(R.id.settings_additional_income_add_approve_button_id);
        Button addingCancelButton = (Button) view.findViewById(R.id.settings_additional_income_add_cancel_button_id);

        CheckBox todayCheckBox = (CheckBox) view.findViewById(R.id.settings_additional_income_when_today_check_box_id);
        todayCheckBox.setOnCheckedChangeListener(todayCheckBoxChanged);

        addButton.setOnClickListener(addButtonClickListener);
        approveButton.setOnClickListener(approveButtonClickListener);
        addingCancelButton.setOnClickListener(addingCancelButtonClickListener);

        NotifierManager.addRecipient(this);

        return view;
    }

    public void showAllAdditionalIncomes(){
        DBHandler dbHandler = DBHandler.getInstance(getContext());
        List<AdditionalIncome> allAdditionalIncomes = dbHandler.getAllAdditionalIncomes();

        AdditionalIncomeAdapter adapter = new AdditionalIncomeAdapter(getContext(), R.layout.fragments_additional_income_item, allAdditionalIncomes);
        ListView listView = (ListView) view.findViewById(R.id.settings_additional_income_list_view_id);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(additionalIncomeItemLongClickListener);
    }

    public void updateAdditionalIncomes(){
        showAllAdditionalIncomes();
    }

    private View.OnClickListener addButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button addButton = (Button) view.findViewById(R.id.settings_additional_income_add_button_id);
                LinearLayout addConstantCostsView = (LinearLayout) view.findViewById(R.id.settings_additional_income_add_item_id);
                addConstantCostsView.setVisibility(View.VISIBLE);
                addButton.setVisibility(View.GONE);
            }
        };
    }

    private View.OnClickListener approveButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEditText = (EditText) view.findViewById(R.id.settings_additional_income_add_income_name_id);
                EditText valueEditText = (EditText) view.findViewById(R.id.settings_additional_income_add_income_value_id);
                CheckBox todayCheckBox = (CheckBox) view.findViewById(R.id.settings_additional_income_when_today_check_box_id);
                CalendarView dateCalendarView = (CalendarView) view.findViewById(R.id.settings_additional_income_when_calendar_view_id);

                String name = nameEditText.getText().toString().trim();
                String date = null;

                if (todayCheckBox.isChecked()){
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date todayDate = new Date();
                    date = dateFormat.format(todayDate).toString();
                }
                else {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date selectedDate = new Date(dateCalendarView.getDate());
                    date = dateFormat.format(selectedDate).toString();
                }

                double value;

                if (name.equals("")) {
                    Toast.makeText(v.getContext(), getResources().getText(R.string.lack_of_additional_item_name), Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    value = Double.parseDouble(valueEditText.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(v.getContext(), getResources().getText(R.string.lack_of_additional_item_value), Toast.LENGTH_LONG).show();
                    return;
                }

                DBHandler dbHandler = DBHandler.getInstance(getContext());
                AdditionalIncome additionalIncome = new AdditionalIncome(name, value, date);
                dbHandler.addAdditionalIncome(additionalIncome);

                Button addButton = (Button) view.findViewById(R.id.settings_additional_income_add_button_id);
                LinearLayout addAdditionalIncomeView = (LinearLayout) view.findViewById(R.id.settings_additional_income_add_item_id);
                addAdditionalIncomeView.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);
                updateAdditionalIncomes();

                nameEditText.setText("");
                valueEditText.setText("");
            }
        };
    }

    private View.OnClickListener addingCancelButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nameEditText = (EditText) view.findViewById(R.id.settings_additional_income_add_income_name_id);
                EditText valueEditText = (EditText) view.findViewById(R.id.settings_additional_income_add_income_value_id);
                nameEditText.setText("");
                valueEditText.setText("");

                Button addButton = (Button) view.findViewById(R.id.settings_additional_income_add_button_id);
                LinearLayout addAdditionalIncomeView = (LinearLayout) view.findViewById(R.id.settings_additional_income_add_item_id);
                addAdditionalIncomeView.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);
            }
        };
    }

    private CompoundButton.OnCheckedChangeListener todayCheckBoxChanged() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    calendarView.setVisibility(View.GONE);
                }
                else if (!isChecked){
                    calendarView.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private View viewToRemove;

    private AdapterView.OnItemLongClickListener additionalIncomeItemLongClickListener(){
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                viewToRemove = view;
                Utils.createRemoveAlertDialog(AdditionalIncomeFragment.this.getActivity(), okRemoveAdditionalIncomeButtonListener, cancelRemoveAdditionalIncomeButtonListener);

                return true;
            }
        };
    }

    private DialogInterface.OnClickListener okRemoveAdditionalIncomeButtonListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (viewToRemove != null) {
                    DBHandler dbHandler = DBHandler.getInstance(AdditionalIncomeFragment.this.getActivity());
                    AdditionalIncome additionalIncome = new AdditionalIncome();
                    additionalIncome.setID((Integer) viewToRemove.getTag(R.string.item_id));
                    if (dbHandler.removeAdditionalIncome(additionalIncome)) {
                        NotifierManager.notifyChange();
                    }
                    else {
                        Toast.makeText(AdditionalIncomeFragment.this.getActivity(), "Could not delete the item", Toast.LENGTH_SHORT).show();
                    }
                }
                dialog.cancel();
            }
        };
    }

    private DialogInterface.OnClickListener cancelRemoveAdditionalIncomeButtonListener(){
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
            updateAdditionalIncomes();
        }
    }
}