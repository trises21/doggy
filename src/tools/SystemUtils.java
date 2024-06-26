/**
 *
 */
package tools;

import constants.ServerConstants;
import java.util.Calendar;
import server.Timer;

/**
 * @author Eternal
 *
 */
public class SystemUtils {

    public static long getUsedMemoryMB() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime()
                .freeMemory()) / 1024L / 1024L;
    }

    public static long getTimeMillisByDay(int year, int month, int day, int hour, int min, int sec) {           
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR, hour - (hour >= 12 ? 12 : 0));
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);
        return cal.getTimeInMillis();
    }
    
    public static long getTimeMillisByTime(int year, int month, int day, int hourofday, int min, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hourofday);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);
        return cal.getTimeInMillis();
    }

    public static void setScheduleAtTime(int year, int month, int day, int hour, int min, int sec, Runnable r) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.AM_PM, hour >= 12 ? Calendar.PM : Calendar.AM);
        cal.set(Calendar.HOUR, hour - (hour >= 12 ? 12 : 0));
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);
        long t = cal.getTimeInMillis() - System.currentTimeMillis();
        if (t < 0) {
            System.out.println("이벤트 일정을 찾고 있습니다.");
            r.run();
        } else {
            System.out.println("진행중인 이벤트가 존재하며 " + (StringUtil.getReadableMillis(0, t)) + "후에 종료합니다.");
            Timer.EventTimer.getInstance().schedule(r, t);
        }
    }

    public static double getHpModByDay() {
        double mod = 1.0;
        //if (SystemUtils.getTimeMillisByDay(2013, 5, 30) > System.currentTimeMillis()) {
        //    return 0.23;
        //} else if (SystemUtils.getTimeMillisByDay(2013, 7, 30) > System.currentTimeMillis()) {
        //    return 0.35;
        //} else if (SystemUtils.getTimeMillisByDay(2013, 8, 30) > System.currentTimeMillis()) {
        //    return 0.45;
        //}
        return mod;
    }
}
