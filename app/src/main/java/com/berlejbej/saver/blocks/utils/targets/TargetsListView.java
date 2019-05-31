package com.berlejbej.saver.blocks.utils.targets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Szymon on 2016-10-08.
 */
public class TargetsListView extends ListView {
    public TargetsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public TargetsListView(Context context) {
        super(context);
    }
    public TargetsListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
