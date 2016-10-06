package com.example.a1521315.test02;

        import android.util.Log;

/**
 * SelectSheetListViewに必要なデータを取得するクラス
 * MyListItem
 */
public class MyListItem1 {
    protected String name;           // ID
    protected String heart_rate;   // 品名
    protected String calorie_consumption;    // 産地
    protected String weight_fluctuates;    // 個数
    protected String total_time;     // 単価
    protected String total_distance;     // 単価


    /**
     * MyListItem()
     *
     * @param name      String 名前
     * @param heart_rate  String 品名
     * @param calorie_consumption String  産地
     * @param weight_fluctuates String 個数
     * @param total_time  String 単価
     * @param total_distance  String 単価
     *
     */
    public MyListItem1(String name, String heart_rate, String calorie_consumption, String weight_fluctuates,
                       String total_time, String total_distance) {
        this.name = name;
        this.heart_rate = heart_rate;
        this.calorie_consumption = calorie_consumption;
        this.weight_fluctuates = weight_fluctuates;
        this.total_time = total_time;
        this.total_distance = total_distance;

    }

    /**
     * IDを取得
     * getName()
     *
     * @return name String 名前
     */
    public String getName() {
        Log.d("名前：", String.valueOf(name));
        return name;
    }

    /**
     * 品名を取得
     * getHeart_rate()
     *
     * @return heart_rate String 品名
     */
    public String getHeart_rate() {
        return heart_rate;
    }

    /**
     * 産地を取得
     * getMadeIn()
     *
     * @return calorie_consumption String 産地
     */
    public String getCalorie_consumption() {
        return calorie_consumption;
    }

    /**
     * 個数を取得
     * getNumber()
     *
     * @return weight_fluctuates String 個数
     */
    public String getWeight_fluctuates() {
        return weight_fluctuates;
    }

    /**
     * 単価を取得
     * getPrice()
     *
     * @return total_time String 単価
     */
    public String getTotal_time() {
        return total_time;
    }

    /**
     * 単価を取得
     * getPrice()
     *
     * @return price String 単価
     */
    public String getTotal_distance() {
        return total_distance;
    }
}