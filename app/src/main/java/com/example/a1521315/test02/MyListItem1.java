package com.example.a1521315.test02;

        import android.util.Log;

/**
 * SelectSheetListViewに必要なデータを取得するクラス
 * MyListItem
 */
public class MyListItem1 {
    protected String name;           // 名前
    protected String heart_rate;   // 心拍数
    protected String calorie_consumption;    // 消費カロリー
    protected String weight_fluctuates;    // 体重変化
    protected String total_time;     // 総走行時間
    protected String total_distance;     // 総走行距離


    /**
     * MyListItem()
     *
     * @param name      String 名前
     * @param heart_rate  String 心拍数
     * @param calorie_consumption String  消費カロリー
     * @param weight_fluctuates String 体重変化
     * @param total_time  String 総走行時間
     * @param total_distance  String 総走行距離
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
     * 名前を取得
     * getName()
     *
     * @return name String 名前
     */
    public String getName() {
        Log.d("名前：", String.valueOf(name));
        return name;
    }

    /**
     * 心拍数を取得
     * getHeart_rate()
     *
     * @return heart_rate String 心拍数
     */
    public String getHeart_rate() {
        return heart_rate;
    }

    /**
     * 消費カロリーを取得
     * getCalorie_consumption()
     *
     * @return calorie_consumption String 消費カロリー
     */
    public String getCalorie_consumption() {
        return calorie_consumption;
    }

    /**
     * 体重変化を取得
     * getWeight_fluctuates()
     *
     * @return weight_fluctuates String 体重変化
     */
    public String getWeight_fluctuates() {
        return weight_fluctuates;
    }

    /**
     * 総走行時間を取得
     * getTotal_time()
     *
     * @return total_time String 総走行時間
     */
    public String getTotal_time() {
        return total_time;
    }

    /**
     * 総走行距離を取得
     * getTotal_distance()
     *
     * @return price String 総走行距離
     */
    public String getTotal_distance() {
        return total_distance;
    }
}