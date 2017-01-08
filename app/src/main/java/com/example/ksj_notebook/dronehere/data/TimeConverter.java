package com.example.ksj_notebook.dronehere.data;

import java.text.DecimalFormat;
import java.util.TimeZone;

/**
 * Created by NAKNAK on 2017-01-06.
 */
public class TimeConverter
    {
        // 밀리세컨드로부터 시, 분, 초를 얻기위한 상수 선언
        private static final long ONE_SECOND = 1000L;
        private static final long ONE_MINUTE = ONE_SECOND * 60L;
        private static final long ONE_HOUR = ONE_MINUTE * 60L;

        // GMT(또는 UTC)로부터의 시간차
        // 서울은 GMT+9
        private static final int GMT_OFFSET_HOUR = (int)(TimeZone.getDefault().getRawOffset() / ONE_HOUR);

        private long time;

        public TimeConverter(String time)
        {
            this.time = Long.parseLong(time);
        }

   /* public static TimeConverter getInstance()
    {
        return new TimeConverter();
    }*/

    public int getGMTHour()
    {
        return (int)(time / ONE_HOUR % 24L);
    }

    public int getHour()
    {
        return (getGMTHour() + GMT_OFFSET_HOUR) % 24;
    }

    public int getMinute()
    {
        return (int)(time / ONE_MINUTE % 60L);
    }

    public int getSecond()
    {
        return (int)(time / ONE_SECOND % 60L);
    }

    public int getMillisecond()
    {
        return (int)(time  % 1000L);
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        DecimalFormat formatter2= new DecimalFormat("00");
        DecimalFormat formatter3 = new DecimalFormat("000");

        sb.append(formatter2.format(getHour()))
                .append("시 ")
                .append(formatter2.format(getMinute()))
                .append("분");

        return sb.toString();
    }
}
