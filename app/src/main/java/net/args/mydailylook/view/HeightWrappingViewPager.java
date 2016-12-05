package net.args.mydailylook.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by arseon on 2016-09-22.
 */
public class HeightWrappingViewPager extends ViewPager {
    public HeightWrappingViewPager(Context context) {
        super(context);
    }

    public HeightWrappingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean wrapHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST;

        if (wrapHeight) {
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);

            if (getChildCount() > 0) {
                View firstChild = getChildAt(0);
                firstChild.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
                height = firstChild.getMeasuredHeight();
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        try {
//            View child = getChildAt(mCurrentPagePosition);
//            if (child != null) {
//                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//                int h = child.getMeasuredHeight();
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
