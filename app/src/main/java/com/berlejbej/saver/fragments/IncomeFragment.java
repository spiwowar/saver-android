package com.berlejbej.saver.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.berlejbej.saver.R;
import com.berlejbej.saver.database.DBHandler;
import com.berlejbej.saver.objects.Income;
import com.berlejbej.saver.notifiers.NotifierManager;

/**
 * Created by Szymon on 2016-10-07.
 */
public class IncomeFragment extends Fragment {

    private Income income;
    private View.OnClickListener okButtonClickListener = okButtonClickListener();
    private View.OnClickListener cancelButtonClickListener = cancelButtonClickListener();
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragments_income,
                container, false);
        EditText incomeEditText = (EditText) view.findViewById(R.id.settings_income_value_id);
        Button okButton = (Button) view.findViewById(R.id.settings_income_ok_button_id);
        Button cancelButton = (Button) view.findViewById(R.id.settings_income_cancel_button_id);

        DBHandler dbHandler = DBHandler.getInstance(getContext());
        income = dbHandler.getIncome();

        incomeEditText.setText(String.valueOf(income.getIncome()));
        okButton.setOnClickListener(okButtonClickListener);
        cancelButton.setOnClickListener(cancelButtonClickListener);
        return view;
    }

    private View.OnClickListener okButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText incomeEditText = (EditText) view.findViewById(R.id.settings_income_value_id);
                double incomeValue;
                try{
                    incomeValue = Double.parseDouble(incomeEditText.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(v.getContext(), getResources().getText(R.string.wrong_monthly_income), Toast.LENGTH_LONG).show();
                    return;
                }

                DBHandler dbHandler = DBHandler.getInstance(getContext());
                Income income = new Income(incomeValue);
                dbHandler.setIncome(income);
                NotifierManager.notifyChange();
            }
        };
    }

    private View.OnClickListener cancelButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack();
                }
            }
        };
    }

}