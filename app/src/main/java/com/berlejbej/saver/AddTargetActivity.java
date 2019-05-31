package com.berlejbej.saver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.berlejbej.saver.database.DBHandler;
import com.berlejbej.saver.notifiers.NotifierManager;
import com.berlejbej.saver.objects.Target;
import com.berlejbej.saver.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Szymon on 2016-10-07.
 */
public class AddTargetActivity extends AppCompatActivity {

    private View.OnClickListener cancelButtonClickListener = cancelButtonClickListener();
    private View.OnClickListener okButtonClickListener = okButtonClickListener();
    private CalendarView.OnDateChangeListener dateChangedListener = dateChangedListener();
    private CalendarView targetEndDate;
    String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_add_target);

        targetEndDate = (CalendarView) findViewById(R.id.add_target_calendar_id);
        targetEndDate.setOnDateChangeListener(dateChangedListener);
        endDate = DateUtils.getTodayDateString();

        Button cancelButton = (Button) findViewById(R.id.button_cancel);
        Button okButton = (Button) findViewById(R.id.button_ok);
        cancelButton.setOnClickListener(cancelButtonClickListener);
        okButton.setOnClickListener(okButtonClickListener);
    }

    private CalendarView.OnDateChangeListener dateChangedListener(){
        return new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                Date date = calendar.getTime();
                endDate = DateUtils.date2String(date);
            }
        };
    }

    private View.OnClickListener cancelButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
    }

    private View.OnClickListener okButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //remove and add in the begin of class
                EditText targetName = (EditText) findViewById(R.id.target_name);
                EditText targetTargetValue = (EditText) findViewById(R.id.target_value);

                String name = targetName.getText().toString().trim();
                double value;

                if (name.equals("")) {
                    Toast.makeText(v.getContext(), getResources().getText(R.string.lack_of_target_name), Toast.LENGTH_LONG).show();
                    return;
                }

                try{
                    value = Double.parseDouble(targetTargetValue.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(v.getContext(), getResources().getText(R.string.lack_of_target_value), Toast.LENGTH_LONG).show();
                    return;
                }

                DBHandler dbHandler = DBHandler.getInstance(getApplicationContext());
                String startDate = DateUtils.getTodayDateString();

                Target target = new Target(name, value, startDate, endDate);
                target.setRemainingValue(value);
                dbHandler.addTarget(target);
                NotifierManager.notifyChange();
                finish();
            }
        };
    }
}
