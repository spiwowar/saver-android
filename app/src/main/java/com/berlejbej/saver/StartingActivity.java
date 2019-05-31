package com.berlejbej.saver;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.berlejbej.saver.database.DBHandler;
import com.berlejbej.saver.objects.Income;
import com.berlejbej.saver.objects.RemainingMonth;
import com.berlejbej.saver.utils.DateUtils;

public class StartingActivity extends AppCompatActivity {

    private static final String TAG = "StartingActivity";

    private EditText incomeEditText;
    private EditText availableMoneyEditText;
    private Button okButton;
    private Spinner constantIncomeFrequencySpinner;
    private ImageView spinnerArrow;
    private View.OnClickListener okButtonClickListener = okButtonClickListener();
    private AdapterView.OnItemSelectedListener onSpinnerItemClickListener = onSpinnerItemClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isFirstRun()) {
            startMainIntent();
            finish();
        }
        setContentView(R.layout.activities_starting);

        incomeEditText = (EditText) findViewById(R.id.starting_income_edit_text_id);
        availableMoneyEditText = (EditText) findViewById(R.id.starting_remaining_edit_text_id);
        constantIncomeFrequencySpinner = (Spinner) findViewById(R.id.contant_income_frequency_spinner_id);
        spinnerArrow = (ImageView) findViewById(R.id.constant_income_frequency_spinner_arrow_id);

        okButton = (Button) findViewById(R.id.starting_income_ok_button_id);
        okButton.setOnClickListener(okButtonClickListener);

        setConstantIncomeSpinner(constantIncomeFrequencySpinner, spinnerArrow);

    }

    private void setConstantIncomeSpinner(final Spinner spinner, ImageView spinnerArrow){
        spinner.setSelection(2); // selected is monthly
        spinner.setOnItemSelectedListener(onSpinnerItemClickListener);

        spinnerArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CLICKED!!");
                spinner.performClick();
            }
        });


    }

    private AdapterView.OnItemSelectedListener onSpinnerItemClickListener(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 5){ // selected is not unusual
                    showNotStandardSpinnerDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private void showNotStandardSpinnerDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.income_frequency_spinner_dialog);
        dialog.setTitle("Nonstandard income frequency");
        dialog.show();
    }

    public View.OnClickListener okButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String incomeString = incomeEditText.getText().toString();
                String availableMoneyString = availableMoneyEditText.getText().toString();
                float income;
                float availableMoney;

                try {
                    income = Float.parseFloat(incomeString);
                    Log.d(TAG, "Set income: " + income);
                }
                catch (NumberFormatException e) {
                    Toast.makeText(v.getContext(), getResources().getText(R.string.lack_of_income_toast), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    availableMoney = Float.parseFloat(availableMoneyString);
                }
                catch (NumberFormatException e) {
                    Toast.makeText(v.getContext(), getResources().getText(R.string.lack_of_remaining_toast), Toast.LENGTH_LONG).show();
                    return;
                }

                DBHandler dbHandler = DBHandler.getInstance(getApplicationContext());
                RemainingMonth available = new RemainingMonth(availableMoney);
                dbHandler.addRemainingMonth(available);
                dbHandler.setStartDate(DateUtils.getTodayDateString());

                Income incomeToAdd = new Income();
                incomeToAdd.setIncome(income).setFrequency(Income.MONTHLY);

                dbHandler.setIncome(incomeToAdd);

                setFirstRun();
                startMainIntent();
            }
        };
    }

    private boolean isFirstRun() {
        SharedPreferences mPref = getApplicationContext().getSharedPreferences("com.berlejbej", Context.MODE_PRIVATE);
        boolean firstRun = mPref.getBoolean("firstRun", true);
        return firstRun;
    }

    private void setFirstRun() {
        SharedPreferences mPref = getApplicationContext().getSharedPreferences("com.berlejbej", Context.MODE_PRIVATE);
        mPref.edit().putBoolean("firstRun", false).commit();
    }

    public void startMainIntent(){
        Intent mainAppIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainAppIntent);
        finish();
    }
}
