package com.example.a1521315.test02;

/**
 * Created by 1521315 on 2016/10/04.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Databaseに関連するクラス
 * DBAdapter
 */
public class DBAdapter {

    private final static String DB_NAME = "kenkatsu_APP.db";      // DB名

    private final static String DB_TABLE_USER = "user";       // DBのテーブル名
    private final static String DB_TABLE_DATA = "data";       // DBのテーブル名
    private final static String DB_TABLE_DATA_TABLE = "data_table";       // DBのテーブル名
    private final static String DB_TABLE_GHOST = "ghost";       // DBのテーブル名


    private final static int DB_VERSION = 1;                // DBのバージョン

    /**
     * DBのカラム名
     */
    public final static String COL_ID_USER = "user_id";             // id
    public final static String COL_NAME = "name";    // 名前
    public final static String COL_AGE = "age";      // 年齢
    public final static String COL_SEX = "sex";      // 性別
    public final static String COL_HEIGHT = "height";      // 身長
    public final static String COL_WEIGHT = "weight";        // 体重
    public final static String COL_BMI = "bmi";           //BMI指数
    public final static String COL_IDEAL_WEIGHT = "ideal_weight";           //理想体重
    public final static String COL_LOGIN = "login";           //ログイン
    public final static String COL_AGE_YEAR = "age_year";                //日時
    public final static String COL_AGE_MONTH = "age_month";                //日時
    public final static String COL_AGE_DAY = "age_day";                //日時




    public final static String COL_ID_DATA = "data_id";             // id
    public final static String COL_NAME_ID = "name_id";         //名前に対するID
    public final static String COL_YEAR = "year";                //日時
    public final static String COL_MONTH = "month";                //日時
    public final static String COL_DAY = "day";                //日時
    public final static String COL_TIMES_OF_DAY = "times_of_day";                //日時

    public final static String COL_HEART_RATE = "heart_rate";    // 心拍数
    public final static String COL_CALORIE_CONSUMPTION = "calorie_consumption";      // 消費カロリー
    public final static String COL_TOTAL_TIME = "total_time";        // 総走行時間
    public final static String COL_TOTAL_DISTANCE = "total_distance";        // 総走行距離
    public final static String COL_COURSE_NAME = "course_name";        //コース名
    public final static String COL_TIME = "time";                   //タイム
    public final static String COL_AVG_SPEED = "avg_speed";         //平均速度
    public final static String COL_MAX_SPEED = "max_speed";         //最高速度
    public final static String COL_DISTANCE = "distance";           //走行距離
    public final static String COL_TRAINING_NAME = "training_name";           //走行距離
    public final static String COL_GRAPH_TIME = "graph_time";       //  グラフ用時間(変換)

    public final static String COL_COURSE_ID = "course_id";         //名前に対するID
    public final static String COL_GHOST_TIME = "ghost_time";



    private SQLiteDatabase db = null;           // SQLiteDatabase
    private DBHelper dbHelper = null;           // DBHelper
    protected Context context;                  // Context

    // コンストラクタ
    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DBHelper(this.context);
    }

    /**
     * DBの読み書き
     * openDB()
     *
     * @return this 自身のオブジェクト
     */
    public DBAdapter openDB() {
        db = dbHelper.getWritableDatabase();        // DBの読み書き
        return this;
    }

    /**
     * DBの読み込み 今回は未使用
     * readDB()
     *
     * @return this 自身のオブジェクト
     */
    public DBAdapter readDB() {
        db = dbHelper.getReadableDatabase();        // DBの読み込み
        return this;
    }

    public void onOpen(SQLiteDatabase db) {
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    /**
     * DBを閉じる
     * closeDB()
     */
    public void closeDB() {
        db.close();     // DBを閉じる
        db = null;
    }

    /**
     * DBのレコードへ登録
     * saveDB()
     *
     * @param name 名前
     * @param age  年齢
     * @param sex   性別
     * @param height  身長
     * @param weight   体重
     * @param bmi   BMI
     *
     */
    public void saveDB(String name, long age, String sex, int height, int weight, double bmi,
                       double ideal_weight, String login, int age_year, int age_month, int age_day) {

        db.beginTransaction();          // トランザクション開始

        try {
            ContentValues values = new ContentValues();     // ContentValuesでデータを設定していく
            values.put(COL_NAME, name);
            values.put(COL_AGE, age);
            values.put(COL_SEX, sex);
            values.put(COL_HEIGHT, height);
            values.put(COL_WEIGHT, weight);
            values.put(COL_BMI, bmi);
            values.put(COL_IDEAL_WEIGHT, ideal_weight);
            values.put(COL_LOGIN, login);
            values.put(COL_AGE_YEAR, age_year);
            values.put(COL_AGE_MONTH, age_month);
            values.put(COL_AGE_DAY, age_day);



            // insertメソッド データ登録
            // 第1引数：DBのテーブル名
            // 第2引数：更新する条件式
            // 第3引数：ContentValues
            db.insert(DB_TABLE_USER, null, values);      // レコードへ登録

            db.setTransactionSuccessful();      // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();                // トランザクションの終了
        }
    }


    public void saveDB_DATA(int name_id, String name ,String year, String month, String day,
                            String times_of_day, String heart_rate, double calorie_consumption,
                            String total_time, String total_distance, String course_name, String time,
                            String avg_speed, String max_speed, String distance,String training_name,
                            long graph_time) {

        db.beginTransaction();          // トランザクション開始

        try {
            ContentValues values = new ContentValues();     // ContentValuesでデータを設定していく
            values.put(COL_NAME_ID, name_id);
            values.put(COL_NAME, name);
            values.put(COL_YEAR, year);
            values.put(COL_MONTH, month);
            values.put(COL_DAY, day);
            values.put(COL_TIMES_OF_DAY, times_of_day);
            values.put(COL_HEART_RATE, heart_rate);
            values.put(COL_CALORIE_CONSUMPTION, calorie_consumption);
            values.put(COL_TOTAL_TIME, total_time);
            values.put(COL_TOTAL_DISTANCE, total_distance);
            values.put(COL_COURSE_NAME, course_name);
            values.put(COL_TIME, time);
            values.put(COL_AVG_SPEED, avg_speed);
            values.put(COL_MAX_SPEED, max_speed);
            values.put(COL_DISTANCE, distance);
            values.put(COL_TRAINING_NAME, training_name);
            values.put(COL_GRAPH_TIME, graph_time);




            // insertメソッド データ登録
            // 第1引数：DBのテーブル名
            // 第2引数：更新する条件式
            // 第3引数：ContentValues
            db.insert(DB_TABLE_DATA, null, values);      // レコードへ登録
            db.insert(DB_TABLE_DATA_TABLE, null, values);      // レコードへ登録

            db.setTransactionSuccessful();      // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();                // トランザクションの終了
        }
    }

    public void saveDB_GHOST(String name, String course_name, double ghost_time) {

        db.beginTransaction();          // トランザクション開始

        try {
            ContentValues values = new ContentValues();     // ContentValuesでデータを設定していく
            values.put(COL_NAME, name);
            values.put(COL_COURSE_NAME, course_name);
            values.put(COL_GHOST_TIME, ghost_time);

            // insertメソッド データ登録
            // 第1引数：DBのテーブル名
            // 第2引数：更新する条件式
            // 第3引数：ContentValues
            db.insert(DB_TABLE_GHOST, null, values);      // レコードへ登録

            db.setTransactionSuccessful();      // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();                // トランザクションの終了
        }
    }

    /**
     * DBのデータを取得
     * getDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @return DBのデータ
     */
    public Cursor getDB(String[] columns) {

        // queryメソッド DBのデータを取得
        // 第1引数：DBのテーブル名
        // 第2引数：取得するカラム名
        // 第3引数：選択条件(WHERE句)
        // 第4引数：第3引数のWHERE句において?を使用した場合に使用
        // 第5引数：集計条件(GROUP BY句)
        // 第6引数：選択条件(HAVING句)
        // 第7引数：ソート条件(ODERBY句)
        return db.query(DB_TABLE_USER, columns, null, null, null, null, null);

    }

    /**
     * DBのデータを取得
     * getDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @return DBのデータ
     */
    public Cursor getDB_DATA(String[] columns) {

        // queryメソッド DBのデータを取得
        // 第1引数：DBのテーブル名
        // 第2引数：取得するカラム名
        // 第3引数：選択条件(WHERE句)
        // 第4引数：第3引数のWHERE句において?を使用した場合に使用
        // 第5引数：集計条件(GROUP BY句)
        // 第6引数：選択条件(HAVING句)
        // 第7引数：ソート条件(ODERBY句)
        return db.query(DB_TABLE_DATA, columns, null, null, null, null, null);

    }

    /**
     * DBのデータを取得
     * sortDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @return DBのデータ
     */
    public Cursor sortDB(String[] columns, String column,String time,String column_user, String course, String user) {

        // queryメソッド DBのデータを取得
        // 第1引数：DBのテーブル名
        // 第2引数：取得するカラム名
        // 第3引数：選択条件(WHERE句)
        // 第4引数：第3引数のWHERE句において?を使用した場合に使用
        // 第5引数：集計条件(GROUP BY句)
        // 第6引数：選択条件(HAVING句)
        // 第7引数：ソート条件(ODERBY句)
        //return db.query(DB_TABLE_GHOST, columns, " MAX("+ column +")",name, null,  null, null);
        return db.query(DB_TABLE_GHOST, columns, column + " like ? and " + time + " = " + "(SELECT MIN(" + time + ") FROM " + DB_TABLE_GHOST + ")"
                + "and "+ column_user + " like ?", new String[]{course,user}, null, null, null);
    }


    /**
     * DBの検索したデータを取得
     * searchDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @param column  String 選択条件に使うカラム名
     * @param name    String[]
     * @return DBの検索したデータ
     */
    public Cursor searchDB(String[] columns, String column, String[] name) {
        return db.query(DB_TABLE_DATA, columns, column + " like ?", name, null, null, null);
    }

    /**
     * DBの検索したデータを取得
     * searchDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @param column  String 選択条件に使うカラム名
     * @param name_id    String[]
     * @return DBの検索したデータ
     */
    public Cursor search_tableDB(String[] columns, String column, String column1, String name_id,String course) {
        return db.query(DB_TABLE_DATA_TABLE, columns, column + " like ? and "+ column1 + " like ?",
                new String[]{name_id, course}, null, null, null);
    }

    /**
     * DBのレコードを全削除
     * allDelete()
     */
    public void allDelete() {

        db.beginTransaction();                      // トランザクション開始
        try {
            // deleteメソッド DBのレコードを削除
            // 第1引数：テーブル名
            // 第2引数：削除する条件式 nullの場合は全レコードを削除
            // 第3引数：第2引数で?を使用した場合に使用
            db.delete(DB_TABLE_DATA, null, null);        // DBのレコードを全削除
            db.setTransactionSuccessful();          // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();                    // トランザクションの終了
        }
    }

    /**
     * DBのレコードの単一削除
     * selectDelete()
     *
     * @param position String
     */
    public void selectDelete(String position) {

        db.beginTransaction();                      // トランザクション開始
        try {
            db.delete(DB_TABLE_USER, COL_ID_USER + "=?", new String[]{position});
            db.setTransactionSuccessful();          // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();                    // トランザクションの終了
        }
    }

    public void updateDB(String name, long age, String sex, int height, int weight, double bmi,
                         double ideal_weight, String login, int age_year, int age_month, int age_day) {
        db.beginTransaction();          // トランザクション開始

        try {
            ContentValues values = new ContentValues();     // ContentValuesでデータを設定していく
            values.put(COL_NAME, name);
            values.put(COL_AGE, age);
            values.put(COL_SEX, sex);
            values.put(COL_HEIGHT, height);
            values.put(COL_WEIGHT, weight);
            values.put(COL_BMI, bmi);
            values.put(COL_IDEAL_WEIGHT, ideal_weight);
            values.put(COL_LOGIN, login);
            values.put(COL_AGE_YEAR, age_year);
            values.put(COL_AGE_MONTH, age_month);
            values.put(COL_AGE_DAY, age_day);

            // insertメソッド データ登録
            // 第1引数：DBのテーブル名
            // 第2引数：更新する条件式
            // 第3引数：ContentValues

            String whereClause = "name = ?";
            String whereArgs[] = new String[]{name};
            db.update(DB_TABLE_USER, values, whereClause, whereArgs);

            db.setTransactionSuccessful();      // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();                // トランザクションの終了
        }
    }


    /**
     * データベースの生成やアップグレードを管理するSQLiteOpenHelperを継承したクラス
     * DBHelper
     */
    private static class DBHelper extends SQLiteOpenHelper {

        // コンストラクタ
        public DBHelper(Context context) {
            //第1引数：コンテキスト
            //第2引数：DB名
            //第3引数：factory nullでよい
            //第4引数：DBのバージョン
            super(context, DB_NAME, null, DB_VERSION);
        }

        /**
         * DB生成時に呼ばれる
         * onCreate()
         *
         * @param db SQLiteDatabase
         */
        @Override
        public void onCreate(SQLiteDatabase db) {

            //テーブルを作成するSQL文の定義 ※スペースに気を付ける
            String createTbl_user = "CREATE TABLE " + DB_TABLE_USER + " ("
                    + COL_ID_USER + " INTEGER PRIMARY KEY ,"
                    + COL_NAME + " TEXT NOT NULL ,"
                    + COL_AGE + " INTEGER NOT NULL ,"
                    + COL_SEX + " TEXT NOT NULL ,"
                    + COL_HEIGHT + " INTEGER NOT NULL ,"
                    + COL_WEIGHT + " INTEGER NOT NULL ,"
                    + COL_BMI +" INTEGER NOT NULL ,"
                    + COL_IDEAL_WEIGHT +" INTEGER NOT NULL ,"
                    + COL_LOGIN +" TEXT NOT NULL,"
                    + COL_AGE_YEAR + " INTEGER NOT NULL ,"
                    + COL_AGE_MONTH + " INTEGER NOT NULL ,"
                    + COL_AGE_DAY + " INTEGER NOT NULL "
                    + ");";

            //テーブルを作成するSQL文の定義 ※スペースに気を付ける
            String createTbl_data = "CREATE TABLE " + DB_TABLE_DATA + " ("
                    + COL_ID_DATA + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + COL_NAME_ID + " INTEGER NOT NULL ,"
                    + COL_NAME + " TEXT NOT NULL ,"
                    + COL_YEAR + " TEXT NOT NULL ,"
                    + COL_MONTH + " TEXT NOT NULL ,"
                    + COL_DAY + " TEXT NOT NULL ,"
                    + COL_TIMES_OF_DAY + " TEXT NOT NULL ,"

                    + COL_HEART_RATE + " INTEGER NOT NULL ,"
                    + COL_CALORIE_CONSUMPTION + " INTEGER NOT NULL ,"
                    + COL_TOTAL_TIME + " INTEGER NOT NULL ,"
                    + COL_TOTAL_DISTANCE + " INTEGER NOT NULL ,"
                    + COL_COURSE_NAME + " TEXT NOT NULL ,"
                    + COL_TIME + " INTEGER NOT NULL ,"
                    + COL_AVG_SPEED + " INTEGER NOT NULL ,"
                    + COL_MAX_SPEED + " INTEGER NOT NULL ,"
                    + COL_DISTANCE + " INTEGER NOT NULL ,"
                    + COL_TRAINING_NAME + " TEXT NOT NULL ,"
                    + COL_GRAPH_TIME +" TEXT NOT NULL ,"
                    + "FOREIGN KEY(name_id) REFERENCES DB_TABLE_USER(COL_ID_USER) ON DELETE CASCADE"
                    + ");";

            //テーブルを作成するSQL文の定義 ※スペースに気を付ける
            String createTbl_data_table = "CREATE TABLE " + DB_TABLE_DATA_TABLE + " ("
                    + COL_ID_DATA + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + COL_NAME_ID + " INTEGER NOT NULL ,"
                    + COL_NAME + " TEXT NOT NULL ,"
                    + COL_YEAR + " TEXT NOT NULL ,"
                    + COL_MONTH + " TEXT NOT NULL ,"
                    + COL_DAY + " TEXT NOT NULL ,"
                    + COL_TIMES_OF_DAY + " TEXT NOT NULL ,"

                    + COL_HEART_RATE + " INTEGER NOT NULL ,"
                    + COL_CALORIE_CONSUMPTION + " INTEGER NOT NULL ,"
                    + COL_TOTAL_TIME + " INTEGER NOT NULL ,"
                    + COL_TOTAL_DISTANCE + " INTEGER NOT NULL ,"
                    + COL_COURSE_NAME + " TEXT NOT NULL ,"
                    + COL_TIME + " INTEGER NOT NULL ,"
                    + COL_AVG_SPEED + " INTEGER NOT NULL ,"
                    + COL_MAX_SPEED + " INTEGER NOT NULL ,"
                    + COL_DISTANCE + " INTEGER NOT NULL ,"
                    + COL_TRAINING_NAME + " TEXT NOT NULL ,"
                    + COL_GRAPH_TIME +" TEXT NOT NULL ,"
                    + "FOREIGN KEY(name_id) REFERENCES DB_TABLE_USER(COL_ID_USER) ON DELETE CASCADE"
                    + ");";

            //テーブルを作成するSQL文の定義 ※スペースに気を付ける
            String createTbl_data_ghost = "CREATE TABLE " + DB_TABLE_GHOST + " ("
                    + COL_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + COL_NAME + " TEXT NOT NULL ,"
                    + COL_COURSE_NAME + " TEXT NOT NULL ,"
                    + COL_GHOST_TIME + " DOUBLE NOT NULL "
                    + ");";

            db.execSQL(createTbl_user);      //SQL文の実行
            db.execSQL(createTbl_data);      //SQL文の実行
            db.execSQL(createTbl_data_table);      //SQL文の実行
            db.execSQL(createTbl_data_ghost);      //SQL文の実行


        }

        /**
         * DBアップグレード(バージョンアップ)時に呼ばれる
         *
         * @param db         SQLiteDatabase
         * @param oldVersion int 古いバージョン
         * @param newVersion int 新しいバージョン
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // DBからテーブル削除
            db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE_USER);
            // DBからテーブル削除
            db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE_DATA);
            // DBからテーブル削除
            db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE_DATA_TABLE);
            // DBからテーブル削除
            db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE_GHOST);
            // テーブル生成
            onCreate(db);
        }
    }
}