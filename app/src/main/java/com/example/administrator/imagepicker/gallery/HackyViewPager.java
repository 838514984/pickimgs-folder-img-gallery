package com.example.administrator.imagepicker.gallery;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class HackyViewPager extends ViewPager {

    private Context context;
    private float downx;
    private float position;
    private boolean scrollhint = true;

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public HackyViewPager(Context context) {
        super(context);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            downx = ev.getX();
            position = this.getCurrentItem();
        } else if (action == MotionEvent.ACTION_UP) {
            /*跟photoview冲突
            float upx = ev.getX();
            if (upx - downx > DensityUtil.dip2px(30) && position == 0) {
                if (scrollhint)
                    SysToast.show(context.getResources().getText(R.string.photo_no_more_content), 1000);
            } else if (downx - upx > DensityUtil.dip2px(30) && position == this.getAdapter().getCount() - 1) {
                if (scrollhint)
                    SysToast.show(context.getResources().getText(R.string.photo_no_more_content), 1000);
            }*/
        }

        return super.dispatchTouchEvent(ev);
    }

    public void setScrollhintEnable(boolean enable) {
        scrollhint = enable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}