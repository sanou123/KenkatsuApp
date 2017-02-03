package com.example.a1521315.test02;

        import android.util.Log;

/**
 * SelectSheetListViewに必要なデータを取得するクラス
 * MyListItem
 */
public class MyListItemData {
    protected int data_id;           // ID
    protected int name_id;           // 名前に対するID
    protected String name;           // 名前
    protected String date;          //日時
    protected String heart_rate;   // 心拍数
    protected String calorie_consumption;    // 消費カロリー
    protected String total_time;     // 総走行時間
    protected String total_distance;     // 総走行距離
    protected String course_name;        //コース名
    protected String time;          //タイム
    protected String avg_speed;          //平均速度
    protected String max_speed;          //最高速度
    protected String distance;          //走行距離
    protected String training_name;          //トレーニング名
    protected String year;
    protected String month;
    protected String day;
    protected long graph_time;




    /**
     * MyListItemData()
     *
     * @param data_id      int DATA_ID
     * @param name_id      int 名前に対するID
     * @param name      String 名前
     * @param date      String 日時
     * @param heart_rate  String 心拍数
     * @param calorie_consumption String  消費カロリー
     * @param total_time  String 総走行時間
     * @param total_distance  String 総走行距離
     * @param course_name String コース名
     * @param time       String タイム
     * @param avg_speed  String 平均速度
     * @param max_speed  String 最高速度
     * @param distance   String 走行距離
     * @param training_name　Stringトレーニング名
     * @param year String 年
     * @param month String 月
     * @param day String 日
     * @param graph_time long グラフ用時間
     */
    public MyListItemData(int data_id, int name_id, String name,String date, String heart_rate,
                       String calorie_consumption, String total_time, String total_distance,
                       String course_name, String time,String avg_speed, String max_speed,
                       String distance, String training_name,String year,String month,
                       String day, long graph_time) {

        this.data_id = data_id;
        this.name_id = name_id;
        this.name = name;
        this.date = date;
        this.heart_rate = heart_rate;
        this.calorie_consumption = calorie_consumption;
        this.total_time = total_time;
        this.total_distance = total_distance;
        this.course_name = course_name;
        this.time = time;
        this.avg_speed = avg_speed;
        this.max_speed = max_speed;
        this.distance = distance;
        this.training_name = training_name;
        this.year = year;
        this.month = month;
        this.day = day;
        this.graph_time = graph_time;

    }

    /**
     * DATA_IDを取得
     * getData_id()
     *
     * @return name String DATA_ID
     */
    public int getData_id() {
        Log.d("名前：", String.valueOf(data_id));
        return data_id;
    }

    /**
     * 名前に対するIDを取得
     * getName_id()
     *
     * @return name String 名前に対するID
     */
    public int getName_id() {
        Log.d("名前：", String.valueOf(name_id));
        return name_id;
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
     * 日時を取得
     * getDate()
     *
     * @return date String 日時
     */
    public String getDate() {
        return date;
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
     * @return total_distance String 総走行距離
     */
    public String getTotal_distance() {
        return total_distance;
    }



    /**
     * コース名を取得
     * getCourse_name()
     *
     * @return course_name String コース名
     */
    public String getCourse_name() { return course_name;  }

    /**
     * タイムを取得
     * getTime()
     *
     * @return time String タイム
     */
    public String getTime() { return time;  }

    /**
     * 平均速度を取得
     * getAvg_speed()
     *
     * @return avg_speed String 平均速度
     */
    public String getAvg_speed() { return avg_speed;  }

    /**
     * 最高速度を取得
     * getMax_speed()
     *
     * @return max_speed String 最高速度
     */
    public String getMax_speed() { return max_speed;  }

    /**
     * 走行距離を取得
     * getDistance()
     *
     * @return distance String 走行距離
     */
    public String getDistance() { return distance;  }

    /**
     * トレーニング名を取得
     * getTraining_name()
     *
     * @return training_name String トレーニング名
     */
    public String getTraining_name() { return training_name;  }

    /**
     * 年を取得
     * getYear()
     *
     * @return year String 年
     */
    public String getYear() { return year;  }

    /**
     * 月を取得
     * getMonth()
     *
     * @return month String 月
     */
    public String getMonth() { return month;  }

    /**
     * 日を取得
     * getDay()
     *
     * @return day String 日
     */
    public String getDay() { return day;  }

    /**
     * グラフ用時間を取得
     * getGraph_time()
     *
     * @return graph_time long グラフ用時間
     */
    public long getGraph_time() { return graph_time;  }
}