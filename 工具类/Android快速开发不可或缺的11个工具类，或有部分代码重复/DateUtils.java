package shixiu.mitao.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ShiShow_xk on 2016/8/22.
 */
public class DateUtils {
    /**
     * GuiYI 诡异的需求 转为 n秒前 n分钟前 n小时前 日期
     *
     * @param date
     * @param *    format 超过昨天，要显示的日期格式如 2016-08-22 11:08:56 就应该传入 yyyy-MM-dd
     *             hh:mm:ss
     * @return
     */
    public static String dataLongToSNS(String date, String format) {

        date = date.replace("/Date(","");
        date = date.replace(")/","");

        long time = Long.parseLong(date);

        long now = System.currentTimeMillis();

        long diff = now - time;
        diff = diff / 1000;// 秒

        if (diff < 0) {
            return dateLongToString(time, format);
        }

        if (diff < 30) { // 30秒
            return "刚刚";
        }

        if (diff < 60) {
            return String.format("%s秒前", diff);
        }

        if (diff < 3600) {
            return String.format("%s分钟前", diff / 60);
        }
        // 获取今天凌晨时间
        long todayStart = getMoring(new Date()).getTime();

        if (time >= todayStart) {// 今天
            return String.format("%s小时前", diff / 3600);
        }

        if (time < todayStart && time >= todayStart - 86400000) {
            return "昨天 " + dateLongToString(time, "HH:mm");
        }

        return dateLongToString(time, format);
    }

    // 获取今天凌晨的时间
    private static Date getMoring(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    private static String dateLongToString(long time, String format) {
        if (time <= 0) {
            return "Empty";
        }
        DateFormat format2 = new SimpleDateFormat(format);
        String dateString = format2.format(new Date(time));
        return dateString;
    }
}
