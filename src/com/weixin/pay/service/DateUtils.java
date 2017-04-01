package com.weixin.pay.service;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by luc on 16/12/23.
 */
public class DateUtils {
    public static void printTimeStamp(){
        long timeStamp = System.currentTimeMillis();
        DateFormat format = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        System.out.println(format.format(timeStamp));
    }

    public static String getToday(String timeStamp){
        long time = Long.parseLong(timeStamp + "000");
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return format.format(time);
    }

    public static String changeDateFormatWithTimeEnd(long timeStamp){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return format.format(timeStamp);
    }

    /**
     * 将字符串的金额,转成 &yen; x.xx 格式
     * @param amout
     * @return
     */
    public static String change2CNY(String amout){
        DecimalFormat df = new DecimalFormat("0.00");
        NumberFormat CHN = NumberFormat.getCurrencyInstance(Locale.CHINA);
        double amountDouble = 0;
        try {
            amountDouble = Double.parseDouble(amout);
            amountDouble = Double.parseDouble(df.format(amountDouble));
        }catch (NumberFormatException e){
            throw new NumberFormatException();
        }
        return CHN.format(amountDouble);
    }

    /**
     * 时间戳转成 2016-10-20 10:43:12
     * @return
     */
    public static String changeDataFormat(long timeStamp){
        String time_end = String.valueOf(timeStamp);
        String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
        return time_end = time_end.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
    }

    /**
     * 以分为整数的订单总额 转换成以元为单位
     * @param fee
     * @return
     */
    public static String change2yuan(String fee){
        DecimalFormat df = new DecimalFormat("0.00");
        double feeDouble = Double.parseDouble(fee);
        feeDouble = feeDouble / 100;
        return df.format(feeDouble);
    }
}
