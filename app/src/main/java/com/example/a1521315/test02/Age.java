package com.example.a1521315.test02;

import android.net.ParseException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 1521315 on 2016/12/02.
 */

public class Age extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * 年齢を返す
     *
     * @param birth_day yyyyMMdd 形式の誕生日
     * @return 年齢
     * @throws ParseException
     */

    public static long getOld(String birth_day) throws ParseException {

        Calendar birthCal = getBirthCal(birth_day);

        Date now = new Date();

        if (birthCal.getTime().after(now)) {

            return 0; // マイナスは0

        }

        Calendar nowCal = Calendar.getInstance();

        nowCal.setTime(now);


        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

        String nowYmd = df.format(now);

        return (Long.parseLong(nowYmd) - Long.parseLong(birth_day)) / 10000L;

    }

    /**
     * 月齢を返す
     *
     * @param birth_day yyyyMMdd 形式の誕生日
     * @return 月齢
     * @throws ParseException
     */

    public static long getMonth(String birth_day) throws ParseException {

        Calendar birthCal = getBirthCal(birth_day);

        Date now = new Date();

        if (birthCal.getTime().after(now)) {

            return 0; // マイナスは0

        }

        Calendar nowCal = Calendar.getInstance();

        nowCal.setTime(now);


        return (long) ((((nowCal.get(Calendar.YEAR) * 365 + (nowCal.get(Calendar.MONTH) + 1) * 30.416666 + nowCal.get(Calendar.DAY_OF_MONTH))

                - (birthCal.get(Calendar.YEAR) * 365 + (birthCal.get(Calendar.MONTH) + 1) * 30.416666 + birthCal.get(Calendar.DAY_OF_MONTH)))

                / 30.41666) % 12);

    }


    /**
     * 日齢を返す
     *
     * @param birth_day yyyyMMdd 形式の誕生日
     * @return 日齢
     * @throws ParseException
     */

    public static long getOldDays(String birth_day) throws ParseException {

        Calendar birthCal = getBirthCal(birth_day);

        Date now = new Date();

        if (birthCal.getTime().after(now)) {

            return 0; // マイナスは0

        }

        return (now.getTime() - birthCal.getTime().getTime()) / 1000 / 60 / 60 / 24;

    }


    public static Calendar getBirthCal(String birth_day) throws ParseException {

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");


        Calendar cal = Calendar.getInstance();

        try {
            cal.setTime(df.parse(birth_day));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }


        return cal;

    }
}