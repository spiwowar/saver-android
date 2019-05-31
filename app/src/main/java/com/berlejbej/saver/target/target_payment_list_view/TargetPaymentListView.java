package com.berlejbej.saver.target.target_payment_list_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Szymon on 2016-11-06.
 */
public class TargetPaymentListView extends ListView {
    public TargetPaymentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public TargetPaymentListView(Context context) {
        super(context);
    }
    public TargetPaymentListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
