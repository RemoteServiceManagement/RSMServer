package com.rsm.util;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Dawid on 26.06.2018 at 20:18.
 */
public class TimeUtils {
    public static Instant changeOffsetToUTCWithoutChangingDateTime(Date chosenDateFrom) {
        Calendar utcTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(chosenDateFrom);
        for( int i = 0; i < Calendar.FIELD_COUNT; i++ ) {
            if( (i != Calendar.ZONE_OFFSET) && (i != Calendar.DST_OFFSET) ) {
                utcTime.set(i, calendar.get(i));
            }
        }
        return utcTime.getTime().toInstant();
    }
}
