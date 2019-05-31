package com.berlejbej.saver.blocks;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.berlejbej.saver.R;
import com.berlejbej.saver.utils.MoneyCalculations;

import java.text.NumberFormat;

/**
 * Created by Szymon on 2016-10-07.
 */
public class RemainingBlock extends Block {

    private Context context;
    private View block;

    private TextView todayRemainingTextView;
    private TextView monthRemainingTextView;

    public RemainingBlock(Context context) {
        super(context);
        this.context = context;
    }

    public RemainingBlock(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public RemainingBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public RemainingBlock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    @Override
    public void create(ViewGroup viewGroup){
        block = ((Activity) context).getLayoutInflater().inflate(R.layout.block_remaining, viewGroup);

        todayRemainingTextView = (TextView) block.findViewById(R.id.main_remaining_view_value_id);
        monthRemainingTextView = (TextView) block.findViewById(R.id.main_remaining_view_overall_value_id);
    }

    @Override
    public void update() {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        double todayRemaining = MoneyCalculations.getRemainingTodayValue(getContext());
        double monthRemaining = MoneyCalculations.getRemainingMonthlyValue(getContext());

        String todayRemainingFormatted = numberFormat.format(todayRemaining);
        String monthRemainingFormatted = numberFormat.format(monthRemaining);

        todayRemainingTextView.setText(todayRemainingFormatted);
        monthRemainingTextView.setText(monthRemainingFormatted);
    }
}
