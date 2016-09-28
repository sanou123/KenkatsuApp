package com.example.a1521315.test02;

import android.util.Log;

/**
 * SelectSheetListViewに必要なデータを取得するクラス
 * MyListItem
 */
public class MyListItem {
    protected int id;           // ID
    protected String name;   // 名前
    protected String age;    // 年齢
    protected String sex;    // 性別
    protected String height;    // 身長
    protected String weight;     // 体重

    /**
     * MyListItem()
     *
     * @param id      int ID
     * @param name String 名前
     * @param age  String 年齢
     * @param sex  String 性別
     * @param height  String 身長
     * @param weight   String 体重
     *
     */
    public MyListItem(int id, String name, String age, String sex,String height,
                      String weight) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.height = height;
        this.weight = weight;

    }

    /**
     * IDを取得
     * getId()
     *
     * @return id int ID
     */
    public int getId() {
        Log.d("取得したID：", String.valueOf(id));
        return id;
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
}