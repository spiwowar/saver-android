package com.berlejbej.saver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.berlejbej.saver.database.DBHandler;
import com.berlejbej.saver.objects.Target;
import com.berlejbej.saver.target.target_payment_list_view.TargetPaymentAdapter;
import com.berlejbej.saver.target.target_payment_list_view.TargetPaymentListView;
import com.berlejbej.saver.utils.Constants;
import com.berlejbej.saver.utils.DateUtils;
import com.berlejbej.saver.utils.MoneyCalculations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Szymon on 2016-11-06.
 */
public class TargetDetailsActivity extends AppCompatActivity {

    private Target target;
    private TargetPaymentListView targetPaymentListView;
    private TargetPaymentAdapter targetPaymentAdapter;
    private View.OnClickListener expandButtonClickListener = expandButtonClickListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.target_layout);

        Intent intent = getIntent();
        int itemId = intent.getIntExtra("item_id", -1);

        DBHandler dbHandler = DBHandler.getInstance(getApplicationContext());
        target = dbHandler.getTarget(itemId);

        TextView targetName = (TextView) findViewById(R.id.target_name_id);
        TextView targetValue = (TextView) findViewById(R.id.target_target_id);
        TextView targetRemaining = (TextView) findViewById(R.id.target_remaining_id);
        TextView targetDate = (TextView) findViewById(R.id.target_date_id);

        RelativeLayout expandPaymentList = (RelativeLayout) findViewById(R.id.target_payment_expand_button_id);
        expandPaymentList.setOnClickListener(expandButtonClickListener);

        targetName.setText(target.getName());
        targetValue.setText(String.valueOf(target.getTargetValue()));
        targetRemaining.setText(String.valueOf(target.getRemainingValue()));
        targetDate.setText(target.getEndDate());

        setListViewValues(itemId);
    }


    private void setListViewValues(Integer itemId){
        DBHandler dbHandler = DBHandler.getInstance(this);
        Target target = dbHandler.getTarget(itemId);

        List<PaymentMonth> paymentMonths = getPaymentMonthsList(target);

        targetPaymentAdapter = new TargetPaymentAdapter(this, R.layout.target_payment_list_view, paymentMonths);
        targetPaymentListView = (TargetPaymentListView)findViewById(R.id.target_payment_list_view_id);
        targetPaymentListView.setAdapter(targetPaymentAdapter);
    }

    private List<PaymentMonth> getPaymentMonthsList(Target target) {

        List<PaymentMonth> paymentMonths = new ArrayList<>();

        final String NEW_FORMAT = "MM-yyyy";
        final String OLD_FORMAT = Constants.dateFormat;
        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);

        Date startDate = null;
        Date endDate = null;

        String newStartDateFormat;
        String newEndDateFormat;
        try {
            startDate = sdf.parse(target.getStartDate());
            endDate = sdf.parse(target.getEndDate());

            sdf.applyPattern(NEW_FORMAT);

            newStartDateFormat = sdf.format(startDate);
            newEndDateFormat = sdf.format(endDate);

            startDate = sdf.parse(newStartDateFormat);
            endDate = sdf.parse(newEndDateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        endCalendar.setTime(endDate);

        int monthsBetween = DateUtils.getMonthsCount(DateUtils.string2Date(target.getStartDate()), DateUtils.string2Date(target.getEndDate()));
        PaymentMonth paymentMonth;

        double monthlyValue = target.getTargetValue()/monthsBetween;

        List <Double> targetPayments = MoneyCalculations.getPaymentsFromToday(getApplicationContext(), target);

        for (int i=0; i<targetPayments.size(); i++){
            paymentMonth = new PaymentMonth();
            startCalendar.add(Calendar.MONTH, 1);
            paymentMonth.setDate(sdf.format(startCalendar.getTime()));
            paymentMonth.setValue(targetPayments.get(i));
            paymentMonths.add(paymentMonth);
        }

        return paymentMonths;
    }

    public View.OnClickListener expandButtonClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (targetPaymentAdapter.isExpanded()){
                    targetPaymentAdapter.seeAll(false);
                }
                else {
                    targetPaymentAdapter.seeAll(true);
                }
                targetPaymentAdapter.notifyDataSetChanged();
            }
        };
    }
}
