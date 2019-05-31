package com.berlejbej.saver.blocks.utils.costs;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ExpandableListView;

/**
 * Created by Szymon on 2016-10-10.
 */
public class CostsExpandableListView extends ExpandableListView{
    public CostsExpandableListView(Context context) {
        super(context);
    }

    public CostsExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CostsExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CostsExpandableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
