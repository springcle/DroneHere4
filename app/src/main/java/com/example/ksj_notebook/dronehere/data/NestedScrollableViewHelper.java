/*
package com.example.ksj_notebook.dronehere.data;

*/
/**
 * Created by gta2v on 2017-03-05.
 *//*


import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.sothree.slidinguppanel.ScrollableViewHelper;

public class NestedScrollableViewHelper extends ScrollableViewHelper {
    View mScrollableView =null;
    public int getScrollableViewScrollPosition(View scrollableView, boolean isSlidingUp) {

        if (mScrollableView instanceof NestedScrollView) {
            if(isSlidingUp){
                return mScrollableView.getScrollY();
            } else {
                NestedScrollView nsv = ((NestedScrollView) mScrollableView);
                View child = nsv.getChildAt(0);
                return (child.getBottom() - (nsv.getHeight() + nsv.getScrollY()));
            }
        } else {
            return 0;
        }
    }
}*/
