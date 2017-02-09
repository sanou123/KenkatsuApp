package com.example.a1521315.test02;

import android.util.Log;

/**
 * SelectSheetListViewに必要なデータを取得するクラス
 * MyListItem
 */
public class MyListItem {
    protected int user_id;           // ID
    protected String name;   // 名前
    protected String age;    // 年齢
    protected String sex;    // 性別
    protected String height;    // 身長
    protected String weight;     // 体重
    protected double bmi;       //BMI
    protected double ideal_weight;  //理想体重
    protected String  login;  //最終ログイン
    protected String  user_year;  //
    protected String  user_month;  //
    protected String  user_day;  //

    /**
     * MyListItem()
     *
     * @param user_id      int USER_ID
     * @param name String 名前
     * @param age  String 年齢
     * @param sex  String 性別
     * @param height  String 身長
     * @param weight   String 体重
     * @param bmi       double BMI
     * @param ideal_weight double 理想体重
     * @param login         String 最終ログイン
     * @param user_year         String 生年月日
     * @param user_month         String 生年月日
     * @param user_day         String 生年月日
     */
    public MyListItem(int user_id, String name, String age, String sex,String height,
                      String weight, double bmi, double ideal_weight, String login,
                      String user_year, String user_month, String user_day) {
        this.user_id = user_id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.ideal_weight = ideal_weight;
        this.login = login;
        this.user_year = user_year;
        this.user_month = user_month;
        this.user_day = user_day;


    }

    /**
     * IDを取得
     * getUser_id()
     *
     * @return user_id int USER_ID
     */
    public int getUser_id() {
        Log.d("取得したID：", String.valueOf(user_id));
        return user_id;
    }

    /**
     * 名前を取得
     * getName()
     *
     * @return name String 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 年齢を取得
     * getAge()
     *
     * @return age String 年齢
     */
    public String getAge() {
        return age;
    }

    /**
     * 性別を取得
     * getSex()
     *
     * @return age String 性別
     */
    public String getSex() {
        return sex;
    }

    /**
     * 身長を取得
     * getHeight()
     *
     * @return height String 身長
     */
    public String getHeight() {
        return height;
    }

    /**
     * 体重を取得
     * getWeight()
     *
     * @return weight String 体重
     */
    public String getWeight() {
        return weight;
    }

    /**
     * BMIを取得
     * getBmi()
     *
     * @return bmi String BMI
     */
    public double getBmi() {
        return bmi;
    }

    /**
     * 理想体重を取得
     * getIdeal_weight()
     *
     * @return ideal_weight String 理想体重
     */
    public double getIdeal_weight() {
        return ideal_weight;
    }

    /**
     * 最終ログインを取得
     * getLogin()
     *
     * @return login String 最終ログイン
     */
    public String getLogin() {
        return login;
    }

    /**
     * 生年月日を取得
     * getUser_Year()
     *
     * @return login String 生年月日
     */
    public String getUser_Year() {
        return user_year;
    }

    /**
     * 生年月日を取得
     * getUser_Month()
     *
     * @return login String 生年月日
     */
    public String getUser_Month() {
        return user_month;
    }

    /**
     * 生年月日を取得
     * getUser_Day()
     *
     * @return login String 生年月日
     */
    public String getUser_Day() {
        return user_day;
    }
}