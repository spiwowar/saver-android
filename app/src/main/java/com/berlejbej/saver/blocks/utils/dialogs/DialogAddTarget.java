package com.berlejbej.saver.blocks.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.berlejbej.saver.R;
import com.berlejbej.saver.database.DBHandler;
import com.berlejbej.saver.notifiers.NotifierManager;
import com.berlejbej.saver.objects.Target;
import com.berlejbej.saver.utils.DateUtils;

import java.util.Date;

/**
 * Created by Szymon on 2017-01-28.
 */
public class DialogAddTarget extends Dialog {

    private static final String TAG = "DialogAddTarget";

    private View.OnClickListener cancelButtonClickListener = cancelButtonClickListener();
    private View.OnClickListener okButtonClickListener = okButtonClickListener();
    private View.OnClickListener openCalendarDialog = openCalendarDialog();

    private EditText targetNameET;
    private EditText targetAmountET;
    private ImageButton targetCalendarIcon;

    private EditText yearET;
    private EditText monthET;
    private EditText dayET;

    private View.OnFocusChangeListener dateOnFocusChangeListener = dateOnFocusChangeListener();

    private Button okButton;
    private Button cancelButton;

    public DialogAddTarget(Context context) {
        super(context);
        init();
    }

    protected DialogAddTarget(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public DialogAddTarget(Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    private void init(){
        setContentView(R.layout.block_dialog_add_target);

        targetNameET = (EditText) findViewById(R.id.target_name);
        targetAmountET = (EditText) findViewById(R.id.target_value);
        targetCalendarIcon = (ImageButton) findViewById(R.id.calendar_icon);

        yearET = (EditText) findViewById(R.id.year);
        monthET = (EditText) findViewById(R.id.month);
        dayET = (EditText) findViewById(R.id.day);

        // TODO: Modify textwatcher to get the right value after editing them

        dayET.setOnFocusChangeListener(dateOnFocusChangeListener);
        monthET.setOnFocusChangeListener(dateOnFocusChangeListener);
        yearET.setOnFocusChangeListener(dateOnFocusChangeListener);

        okButton = (Button) findViewById(R.id.button_ok);
        cancelButton = (Button) findViewById(R.id.button_cancel);

        okButton.setOnClickListener(okButtonClickListener);
        cancelButton.setOnClickListener(cancelButtonClickListener);

        String day = DateUtils.getCurrentDay();
        String month = DateUtils.getCurrentMonth();
        String year = DateUtils.getCurrentYear();

        dayET.setText(day);
        monthET.setText(month);
        yearET.setText(year);

        validateDate(false);

        targetCalendarIcon.setOnClickListener(openCalendarDialog);
    }

    private View.OnFocusChangeListener dateOnFocusChangeListener(){
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    validateDate(true);
                }
            }
        };
    }

    private View.OnClickListener openCalendarDialog(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // My DialogCalendar needs the month started from 0
                validateDate(true);
                DialogCalendar dialogCalendar = new DialogCalendar(getContext(),
                        yearET.getText().toString(),
                        String.valueOf(Integer.parseInt(monthET.getText().toString()) - 1),
                        dayET.getText().toString());
                dialogCalendar.setMinimumDate(DateUtils.getTodayDate());
                dialogCalendar.setOnDatePickedListener(new DialogCalendar.OnDatePickedListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        yearET.setText(year);
                        monthET.setText(String.valueOf(Integer.parseInt(month)));
                        dayET.setText(day);
                        validateDate(false);
                    }
                });
                dialogCalendar.show();
            }
        };
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

                validateDate(true);

                String name = targetNameET.getText().toString().trim();
                Double value;

                if (name.isEmpty()) {
                    Toast.makeText(v.getContext(), getContext().getResources().getText(R.string.lack_of_target_name), Toast.LENGTH_LONG).show();
                    return;
                }

                try{
                    value = Double.parseDouble(targetAmountET.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(v.getContext(), getContext().getResources().getText(R.string.lack_of_target_value), Toast.LENGTH_LONG).show();
                    return;
                }

                String startDate = DateUtils.getTodayDateString();
                String endDate = yearET.getText().toString() + "-" + monthET.getText().toString() + "-" + dayET.getText().toString();
                Target target = new Target(name, value, startDate, endDate);
                target.setRemainingValue(value);

                updateDB(target);
                dismiss();
            }
        };
    }

    private void updateDB(Target target){
        DBHandler dbHandler = DBHandler.getInstance(getContext().getApplicationContext());
        dbHandler.addTarget(target);
        NotifierManager.notifyChange();
    }

    private boolean validateDate(boolean showToasts){

        boolean dataOK = true;

        String year = yearET.getText().toString();
        String month = monthET.getText().toString();
        String day = dayET.getText().toString();


        Date todayDate = DateUtils.getTodayDate();
        String todayDateString = DateUtils.getTodayDateString();
        String validatedDate;
        String currentSelectedDateString = year + "-" + month + "-" + day;
        String[] values = new String[]{year, month, day};

        if (year.isEmpty() || month.isEmpty() || day.isEmpty()){
            if (showToasts) {
                Toast.makeText(getContext(), "Wartość daty nie może być pusta", Toast.LENGTH_SHORT).show();
                values = DateUtils.splitDate(todayDateString);
            }
            dataOK = false;
        }
        else {
            Date currentSelectedDate = DateUtils.string2Date(currentSelectedDateString);
            Log.d(TAG, "currentSelectedDate: " + currentSelectedDate);
            Log.d(TAG, "todayDate: " + todayDate.toString());
            if (todayDate.after(currentSelectedDate) && !todayDate.equals(currentSelectedDate)) {
                if (showToasts) {
                    Toast.makeText(getContext(), "Data nie może być wcześniejsza, niż dzisiejsza", Toast.LENGTH_SHORT).show();
                }
                validatedDate = DateUtils.date2String(todayDate);
                values = DateUtils.splitDate(validatedDate);
                dataOK = false;
            } else {
                validatedDate = DateUtils.date2String(currentSelectedDate);
                if (!validatedDate.equals(currentSelectedDateString)) {
                    if (showToasts) {
                        Toast.makeText(getContext(), "Nieprawidłowa data, zmiana", Toast.LENGTH_SHORT).show();
                    }
                    values = DateUtils.splitDate(validatedDate);
                    dataOK = false;
                }
            }
        }

        yearET.setText(values[0]);
        monthET.setText(values[1]);
        dayET.setText(values[2]);

        return dataOK;

    }
}
