package com.berlejbej.saver.blocks;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.berlejbej.saver.R;
import com.berlejbej.saver.database.DBHandler;
import com.berlejbej.saver.objects.Income;
import com.berlejbej.saver.utils.MoneyCalculations;

import java.text.NumberFormat;

/**
 * Created by Szymon on 2016-10-07.
 */
public class BalanceBlock extends Block{

    private Context context;
    private View block;

    private TextView balanceTextView;

    public BalanceBlock(Context context) {
        super(context);
        this.context = context;
    }

    public BalanceBlock(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public BalanceBlock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public BalanceBlock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    @Override
    public void create(ViewGroup viewGroup){
        block = ((Activity) context).getLayoutInflater().inflate(R.layout.block_balance, viewGroup);
        balanceTextView = (TextView) block.findViewById(R.id.main_balance_view_value_id);
    }

    @Override
    public void update(){

        double balance = MoneyCalculations.getDailyBalance(getContext());
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        String balanceFormatted = numberFormat.format(balance);

        balanceTextView.setText(balanceFormatted + "/" + getResources().getString(R.string.day));
    }
}
