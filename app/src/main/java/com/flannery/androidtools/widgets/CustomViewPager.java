package com.flannery.androidtools.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * VelocityTracker is a concept commonly used in computer graphics, physics simulations, and user interface frameworks to calculate the velocity of an object or pointer based on its position over time. It's particularly useful for creating responsive and realistic interactions in applications like games or touch-based user interfaces.
 * In the context of Android development, VelocityTracker refers to a class provided by the Android framework. It's used to track the velocity of motion events, such as touch events, on the screen. This can be useful for implementing various gestures and animations that require knowledge of how quickly a user is moving their finger across the screen.
 * Here's a basic overview of how VelocityTracker works in Android:
 * Initialization: To use VelocityTracker, you need to create an instance of it and associate it with a specific motion event, typically the ACTION_MOVE events in the case of touch gestures.
 * Tracking: As the user interacts with the screen, you feed the VelocityTracker instance with the motion events, which contain the current position of the pointer. The VelocityTracker class calculates the velocity based on the change in position over time.
 * Velocity Retrieval: After you've collected enough motion events, you can retrieve the calculated velocity using the computeCurrentVelocity(int units) method. The units parameter allows you to specify the desired units for the velocity, such as pixels per second.
 *
 * https://www.nhooo.com/note/qadf7m.html
 */
public class CustomViewPager extends ViewGroup {
    private static final String TAG = CustomViewPager.class.getSimpleName();
    private int screenWidth;
    private int screenHeight;
    private int lastMoveX = 0;
    private VelocityTracker velocityTracker;
    private int MAX_VELOCITY = 600;
    private int curScreen = 0;
    private Scroller scroller;

    public CustomViewPager(Context context) {
        super(context);
        init(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        scroller = new Scroller(context);

        LinearLayout layout1 = new LinearLayout(context);
        layout1.setBackgroundColor(Color.RED);
        addView(layout1);
        LinearLayout layout2 = new LinearLayout(context);
        layout2.setBackgroundColor(Color.GREEN);
        addView(layout2);
        LinearLayout layout3 = new LinearLayout(context);
        layout3.setBackgroundColor(Color.BLUE);
        addView(layout3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: onTouchEvent=" + event);
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        int x = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastMoveX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                int dis = lastMoveX - x;
                Log.i(TAG, "onTouchEvent: dis=" + dis);
                scrollBy(dis, 0);
                lastMoveX = x;
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) velocityTracker.getXVelocity();
                if (velocityX > MAX_VELOCITY && curScreen > 0) {
                    jump2Screen(curScreen - 1);
                } else if (velocityX < -MAX_VELOCITY && curScreen < getChildCount() - 1) {
                    jump2Screen(curScreen + 1);
                } else {
                    int screen = (getScrollX() + screenWidth / 2) / screenWidth;
                    jump2Screen(screen);
                }
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void jump2Screen(int screen) {
        curScreen = screen;
        if (curScreen > getChildCount() - 1) {
            curScreen = getChildCount() - 1;
        }
        int dis = curScreen * screenWidth - getScrollX();
        scroller.startScroll(getScrollX(), 0, dis, 0);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), 0);
            postInvalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenWidth = MeasureSpec.getSize(widthMeasureSpec);
        screenHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(screenWidth, screenHeight);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.measure(screenWidth, screenHeight);
        }
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int leftWidth = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout(leftWidth, 0, leftWidth + screenWidth, screenHeight);
            leftWidth = leftWidth + screenWidth;
        }
    }

}
