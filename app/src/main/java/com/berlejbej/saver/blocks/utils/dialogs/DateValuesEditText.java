package com.berlejbej.saver.blocks.utils.dialogs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by Szymon on 2017-01-31.
 */
public class DateValuesEditText extends EditText {
    public DateValuesEditText(Context context) {
        super(context);
    }

    public DateValuesEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateValuesEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DateValuesEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onKeyPreIme( int key_code, KeyEvent event )
    {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
            this.clearFocus();

        return super.onKeyPreIme( key_code, event );
    }
}
