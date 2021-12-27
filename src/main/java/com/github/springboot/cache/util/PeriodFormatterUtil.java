package com.github.springboot.cache.util;

import com.github.springboot.cache.properties.SpringCacheRedisTtlProperties;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.time.Duration;

/**
 * @author WangChen
 * @since 2021-12-25 16:08
 **/
public final class PeriodFormatterUtil {

    /**
     * period param
     */
    private static final String DAY = "d";
    private static final String HOUR = "h";
    private static final String MINUTES = "m";
    private static final String SECOND = "s";
    private static final PeriodFormatter FORMATTER;


    /**
     * parse period string
     *
     * @param periodStr periodStr
     * @return long second
     */
    public static long covertToSecond(String periodStr) {
        Period period = FORMATTER.parsePeriod(periodStr);
        int days = period.getDays();
        if (days > 0) {
            return Duration.ofDays(days).getSeconds();
        }
        int hours = period.getHours();
        if (hours > 0) {
            return Duration.ofHours(hours).getSeconds();
        }
        int minutes = period.getMinutes();
        if (minutes > 0) {
            return Duration.ofMinutes(minutes).getSeconds();
        }
        int seconds = period.getSeconds();
        if (seconds > 0) {
            return seconds;
        }
        return SpringCacheRedisTtlProperties.DEFAULT.getSeconds();
    }

    static {
        FORMATTER = new PeriodFormatterBuilder()
                .appendDays().appendSuffix(DAY)
                .appendHours().appendSuffix(HOUR)
                .appendMinutes().appendSuffix(MINUTES)
                .appendSeconds().appendSuffix(SECOND)
                .toFormatter();
    }
}
