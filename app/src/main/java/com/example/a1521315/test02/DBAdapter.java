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

    private final static String DB_NAME = "asdfgh.db";      // DB名
    private final static String DB_TABLE_USER = "user";       // DBのテーブル名
    private final static String DB_TABLE_DATA = "data";       // DBのテーブル名
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

    public final static String COL_ID_DATA = "data_id";             // id
    public final static String COL_NAME_ID = "name_id";         //名前に対するID
    public final static String COL_DATE = "date";                //日時
    public final static String COL_HEART_RATE = "heart_rate";    // 心拍数
    public final static String COL_CALORIE_CONSUMPTION = "calorie_consumption";      // 消費カロリー
    public final static String COL_TOTAL_TIME = "total_time";        // 総走行時間
    public final static String COL_TOTAL_DISTANCE = "total_distance";        // 総走行距離
    public final static String COL_COURSE_NAME = "course_name";        //コース名
    public final static String COL_TIME = "time";                   //タイム
    public final static String COL_AVG_SPEED = "avg_speed";         //平均速度
    public final static String COL_MAX_SPEED = "max_speed";         //最高速度
    public final static String COL_DISTANCE = "distance";           //走行距離
    public final static String COL_BMI = "bmi";           //BMI指数


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
     */
    public void saveDB(String name, int age, String sex, int height, int weight) {

        db.beginTransaction();          // トランザクション開始

        try {
            ContentValues values = new ContentValues();     // ContentValuesでデータを設定していく
            values.put(COL_NAME, name);
            values.put(COL_AGE, age);
            values.put(COL_SEX, sex);
            values.put(COL_HEIGHT, height);
            values.put(COL_WEIGHT, weight);


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


    public void saveDB_DATA(int name_id, String name ,String date, String heart_rate, String calorie_consumption,
                            String total_time, String total_distance, String course_name, String time,
                            String avg_speed, String max_speed, String distance, String bmi) {

        db.beginTransaction();          // トランザクション開始

        try {
            ContentValues values = new ContentValues();     // ContentValuesでデータを設定していく
            values.put(COL_NAME_ID, name_id);
            values.put(COL_NAME, name);
            values.put(COL_DATE, date);
            values.put(COL_HEART_RATE, heart_rate);
            values.put(COL_CALORIE_CONSUMPTION, calorie_consumption);
            values.put(COL_TOTAL_TIME, total_time);
            values.put(COL_TOTAL_DISTANCE, total_distance);
            values.put(COL_COURSE_NAME, course_name);
            values.put(COL_TIME, time);
            values.put(COL_AVG_SPEED, avg_speed);
            values.put(COL_MAX_SPEED, max_speed);
            values.put(COL_DISTANCE, distance);
            values.put(COL_BMI, bmi);



            // insertメソッド データ登録
            // 第1引数：DBのテーブル名
            // 第2引数：更新する条件式
            // 第3引数：ContentValues
            db.insert(DB_TABLE_DATA, null, values);      // レコードへ登録

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
    public Cursor getDB1(String[] columns) {

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
     * DBの検索したデータを取得
     * searchDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @param column  String 選択条件に使うカラム名
     * @param name    String[]
     * @return DBの検索したデータ
     */
    public Cursor searchDB(String[] columns, String column, String[] name) {
        return db.query(DB_TABLE_DATA, columns, column + " like ?", name, null, null, null,null);
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

    public void updateDB(String strName, int strAge,String strSex, int strHeight, int strWeight) {
        db.beginTransaction();          // トランザクション開始

        try {
            ContentValues values = new ContentValues();     // ContentValuesでデータを設定していく
            values.put(COL_NAME, strName);
            values.put(COL_AGE, strAge);
            values.put(COL_SEX, strSex);
            values.put(COL_HEIGHT, strHeight);
            values.put(COL_WEIGHT, strWeight);

            // insertメソッド データ登録
            // 第1引数：DBのテーブル名
            // 第2引数：更新する条件式
            // 第3引数：ContentValues

            String whereClause = "name = ?";
            String whereArgs[] = new String[]{strName};
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
                    + COL_AGE + " INTEGER NOT NULL,"
                    + COL_SEX + " TEXT NOT NULL,"
                    + COL_HEIGHT + " INTEGER NOT NULL,"
                    + COL_WEIGHT + " INTEGER NOT NULL"
                    + ");";

            //テーブルを作成するSQL文の定義 ※スペースに気を付ける
            String createTbl_data = "CREATE TABLE " + DB_TABLE_DATA + " ("
                    + COL_ID_DATA + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_NAME_ID + " INTEGER NOT NULL,"
                    + COL_NAME + " TEXT NOT NULL ,"
                    + COL_DATE + " TEXT NOT NULL ,"
                    + COL_HEART_RATE + " INTEGER NOT NULL,"
                    + COL_CALORIE_CONSUMPTION + " INTEGER NOT NULL,"
                    + COL_TOTAL_TIME + " INTEGER NOT NULL,"
                    + COL_TOTAL_DISTANCE + " INTEGER NOT NULL,"
                    + COL_COURSE_NAME + " TEXT NOT NULL ,"
                    + COL_TIME + " INTEGER NOT NULL ,"
                    + COL_AVG_SPEED + " INTEGER NOT NULL ,"
                    + COL_MAX_SPEED + " INTEGER NOT NULL ,"
                    + COL_DISTANCE + " INTEGER NOT NULL ,"
                    + COL_BMI +" INTEGER NOT NULL,"
                    + "FOREIGN KEY(name_id) REFERENCES DB_TABLE_USER(COL_ID_USER) ON DELETE CASCADE"
                    + ");";
            db.execSQL(createTbl_user);      //SQL文の実行

            db.execSQL(createTbl_data);      //SQL文の実行

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
            // テーブル生成
            onCreate(db);
        }
    }
}