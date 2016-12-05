package net.args.mydailylook.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by arseon on 2016-09-13.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.top = space;
        int pos = parent.getChildLayoutPosition(view);
        if (pos > 0) {
            if (pos % 3 == 1) {
                outRect.left = space;
                outRect.right = space;
            }
        }

//        int width = ((parent.getMeasuredWidth() / 3) - (space * 2)) / 3;
//        view.setLayoutParams(new RecyclerView.LayoutParams(width, width));

//        if (pos > 0 && pos % 3 == 2) {
//            outRect.right = 0;
//        } else {
//            outRect.right = space;
//        }
    }

}
