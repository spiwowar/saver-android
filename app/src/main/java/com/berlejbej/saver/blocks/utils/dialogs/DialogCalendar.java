package com.berlejbej.saver.blocks.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.berlejbej.saver.R;
import com.berlejbej.saver.database.DBHandler;
import com.berlejbej.saver.notifiers.NotifierManager;
import com.berlejbej.saver.objects.Target;
import com.berlejbej.saver.utils.DateUtils;

import java.util.Date;

/**
 * Created by Szymon on 2017-01-28.
 */
public class DialogCalendar extends Dialog {

    private View.OnClickListener cancelButtonClickListener = cancelButtonClickListener();
    private View.OnClickListener okButtonClickListener = okButtonClickListener();

    private Button okButton;
    private Button cancelButton;
    private CalendarView calendarView;

    private String selectedYear;
    private String selectedMonth;
    private String selectedDay;

    public DialogCalendar(Context context) {
        super(context);
        this.selectedYear = DateUtils.getCurrentYear();
        this.selectedMonth = DateUtils.getCurrentMonth();
        this.selectedDay = DateUtils.getCurrentDay();
        init();
    }

    public DialogCalendar(Context context, String year, String month, String day) {
        super(context);
        this.selectedYear = year;
        this.selectedMonth = month;
        this.selectedDay = day;
        init();
    }

    protected DialogCalendar(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.selectedYear = DateUtils.getCurrentYear();
        this.selectedMonth = DateUtils.getCurrentMonth();
        this.selectedDay = DateUtils.getCurrentDay();
        init();
    }

    public DialogCalendar(Context context, int themeResId) {
        super(context, themeResId);
        this.selectedYear = DateUtils.getCurrentYear();
        this.selectedMonth = DateUtils.getCurrentMonth();
        this.selectedDay = DateUtils.getCurrentDay();
        init();
    }

    private void init(){
        setContentView(R.layout.block_dialog_calendar);

        calendarView = (CalendarView) findViewById(R.id.calendar_view);
        okButton = (Button) findViewById(R.id.ok_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);

        calendarView.setDate(DateUtils.getMilliseconds(this.selectedYear, this.selectedMonth, this.selectedDay));
        okButton.setOnClickListener(okButtonClickListener);
        cancelButton.setOnClickListener(cancelButtonClickListener);
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

                long date = calendarView.getDate();
                String year = DateUtils.getYear(date);
                String month = DateUtils.getMonth(date);
                String day = DateUtils.getDay(date);
                if (DialogCalendar.this.onDatePickedListener != null){
                    DialogCalendar.this.onDatePickedListener.onDatePicked(year, month, day);
                }
                dismiss();
            }
        };
    }

    private void updateDB(Target target){
        DBHandler dbHandler = DBHandler.getInstance(getContext().getApplicationContext());
        dbHandler.addTarget(target);
        NotifierManager.notifyChange();
    }

    interface OnDatePickedListener {
        void onDatePicked(String year, String month, String day);
    }

    private OnDatePickedListener onDatePickedListener = null;

    public void setOnDatePickedListener(OnDatePickedListener onDatePickedListener){
        this.onDatePickedListener = onDatePickedListener;
    }

    public void setMinimumDate(Date date){
        calendarView.setMinDate(DateUtils.getMillisecondsFromDate(DateUtils.date2String(date)));
    }
}
