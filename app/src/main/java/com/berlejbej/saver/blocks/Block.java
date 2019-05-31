package com.berlejbej.saver.blocks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by Szymon on 2017-01-25.
 */
public abstract class Block extends ViewGroup {

    public Block(Context context) {
        super(context);
    }

    public Block(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Block(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Block(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    public abstract void update();

    public abstract void create(ViewGroup viewGroup);

    /* TODO: to handle at the end, blocks moving
    private long startClickTime = 0;
    private float lastTouchX = 0.0f;
    private float lastTouchY = 0.0f;
    private final int LONG_CLICK_ACTIVATION_TIME = 1000;
    private float dx = 0.0f;
    private float dy = 0.0f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        final int action = MotionEventCompat.getActionMasked(event);
        switch (action){
            case MotionEvent.ACTION_DOWN: {
                final float x = event.getX();
                final float y = event.getY();

                startClickTime = Calendar.getInstance().getTimeInMillis();

                // Remember where we started (for dragging)
                lastTouchX = x;
                lastTouchY = y;
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;

                final float x = event.getX();
                final float y = event.getY();

                // Calculate the distance moved
                if (clickDuration > LONG_CLICK_ACTIVATION_TIME) {
                    dx = x - lastTouchX;
                    dy = y - lastTouchY;

                    setY(getY() + dy);

                    // Remember this touch position for the next move event
                    lastTouchX = x;
                    lastTouchY = y;
                }

                break;
            }
        }
        return true;
    }
    */
}
