package com.santamaria.dronehere.Here;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by gta2v on 2017-03-07.
 */

public class ViewPagerOverride extends ViewPager{


    /**
     * Created by woosuk on 2016-12-10.
     *
     * 커스텀 뷰페이저. Swipe로 page 이동 메소드를 오버라이드해서 커스텀 사용.
     * 이전에 사다조 했던거에서 다시 퍼옴
     */

        private boolean enabled;

        public ViewPagerOverride(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.enabled = true;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if (this.enabled) {
                return super.onInterceptTouchEvent(ev);
            }
            return false;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            if (this.enabled) {
                return super.onTouchEvent(ev);
            }
            return false;
        }

        public void setPagingEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }



