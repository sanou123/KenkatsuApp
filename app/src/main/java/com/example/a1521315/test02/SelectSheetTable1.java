package com.example.a1521315.test02;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * TableLayout画面に関連するクラス
 * SelectSheetTable1
 */
public class SelectSheetTable1 extends AppCompatActivity {

    Globals globals;
    private DBAdapter dbAdapter;                // DBAdapter
    private ArrayAdapter<String> adapter;       // ArrayAdapter
    private ArrayList<String> items;            // ArrayList

    private Button mButton03AllDelete;          // 全削除ボタン
    private TableLayout mTableLayout04List;     //データ表示用TableLayout

    private int colorFlg = 1;                   //背景切り替え用フラグ

    private final static int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final static int GCH = Gravity.CENTER_HORIZONTAL;
    //private final static int GE = Gravity.END;         // Gravity.RIGHTでもよい

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.select_sheet_table1);

        dbAdapter = new DBAdapter(this);

        findViews();        // 各部品の結び付け

        init();         //データの検索を始める時

        delete();

    }

    /**
     * 各部品の結びつけ処理
     * findViews()
     */
    private void findViews() {
        mTableLayout04List = (TableLayout) findViewById(R.id.tableLayout03List);    //データ表示用TableLayout
        mButton03AllDelete = (Button) findViewById(R.id.button02AllDelete);         // 全削除ボタン
    }


    // 検索を始める時
    private void init() {

        dbAdapter.readDB();                         // DBの読み込み(読み込みの方)

        TableRow rowHeader = new TableRow(this);    // 行を作成
        rowHeader.setPadding(16, 12, 16, 12);       // 行のパディングを指定(左, 上, 右, 下)

        // ヘッダー：日時
        TextView headerDate = setTextItem("日時", GCH);            // TextViewのカスタマイズ処理
        TableRow.LayoutParams paramsDate = setParams(0.4f);       // LayoutParamsのカスタマイズ処理
        // ヘッダー：最大心拍数
        TextView headerHeart_rate = setTextItem("最大心拍数", GCH);
        TableRow.LayoutParams paramsHeart_rate = setParams(0.4f);
        // ヘッダー：消費カロリー
        TextView headerCalorie_consumption = setTextItem("消費カロリー", GCH);
        TableRow.LayoutParams paramsCalorie_consumption = setParams(0.3f);
        // ヘッダー：総走行時間
        TextView headerTotal_time = setTextItem("走行時間", GCH);
        TableRow.LayoutParams paramsTotal_time = setParams(0.4f);
        // ヘッダー：総走行距離
        TextView headerTotal_distance = setTextItem("走行距離", GCH);
        TableRow.LayoutParams paramsTotal_distance = setParams(0.3f);
        // ヘッダー：コース名
        TextView headerCourse = setTextItem("コース名", GCH);
        TableRow.LayoutParams paramsCourse = setParams(0.3f);
        // rowHeaderにヘッダータイトルを追加
        rowHeader.addView(headerDate, paramsDate);          // ヘッダー：日時
        rowHeader.addView(headerHeart_rate, paramsHeart_rate);          // ヘッダー：最大心拍数
        rowHeader.addView(headerCalorie_consumption, paramsCalorie_consumption);          // ヘッダー：消費カロリー
        rowHeader.addView(headerTotal_time, paramsTotal_time);          // ヘッダー：総走行距離
        rowHeader.addView(headerTotal_distance, paramsTotal_distance);            // ヘッダー：総走行時間
        rowHeader.addView(headerCourse, paramsCourse);          // ヘッダー：コース名

        rowHeader.setBackgroundResource(R.drawable.row_deco1);  // 背景

        // TableLayoutにrowHeaderを追加
        mTableLayout04List.addView(rowHeader);

        String column = "name";          //検索対象のカラム名
        String[] name = {globals.now_user};            //検索対象の文字

        // DBの検索データを取得 入力した文字列を参照してDBの品名から検索
        Cursor c = dbAdapter.searchDB(null, column, name);

        if (c.moveToFirst()) {
            do {

                TableRow row = new TableRow(this);          // 行を作成
                row.setPadding(16, 12, 16, 12);             // 行のパディングを指定(左, 上, 右, 下)

                // 日時
                TextView textDate = setTextItem(c.getString(3), GCH);     // TextViewのカスタマイズ処理
                // 最大心拍数
                TextView textHeart_rate = setTextItem(c.getString(4)+" [bpm]", GCH);     // TextViewのカスタマイズ処理
                // 消費カロリー
                TextView textCalorie_consumption = setTextItem(c.getString(5)+" [cal]", GCH);      // TextViewのカスタマイズ処理
                // 総走行時間
                TextView textTotal_time = setTextItem(c.getString(9)+" [分]", GCH);      // TextViewのカスタマイズ処理
                // 総走行距離
                TextView textTotal_distance = setTextItem(c.getString(12)+" [km]", GCH);      // TextViewのカスタマイズ処理
                // コース名
                TextView textCourse = setTextItem(c.getString(8), GCH);      // TextViewのカスタマイズ処理

                /* getString()参考
                Log.d("取得したCursor(DATA_ID):", String.valueOf(c.getString(0)));
                Log.d("取得したCursor(名前に対するID):", String.valueOf(c.getString(1)));
                Log.d("取得したCursor(名前):", String.valueOf(c.getString(2)));
                Log.d("取得したCursor(日時):", c.getString(3));
                Log.d("取得したCursor(心拍数):", c.getString(4));
                Log.d("取得したCursor(消費カロリー):", c.getString(5));
                Log.d("取得したCursor(総走行時間):", c.getString(6));
                Log.d("取得したCursor(総走行距離):", c.getString(7));
                Log.d("取得したCursor(コース名):", c.getString(8));
                Log.d("取得したCursor(タイム):", c.getString(9));
                Log.d("取得したCursor(平均速度):", c.getString(10));
                Log.d("取得したCursor(最高速度):", c.getString(11));
                Log.d("取得したCursor(走行距離):", c.getString(12));
                */


                // rowHeaderに各項目(DBから取得した産地,個数,単価)を追加
                row.addView(textDate, paramsDate);      // 日時
                row.addView(textHeart_rate, paramsHeart_rate);      // 最大心拍数
                row.addView(textCalorie_consumption, paramsCalorie_consumption);      // 消費カロリー
                row.addView(textTotal_time, paramsTotal_time);      // 総走行時間
                row.addView(textTotal_distance, paramsTotal_distance);        // 総走行距離
                row.addView(textCourse, paramsCourse);          // コース名

                mTableLayout04List.addView(row);            // TableLayoutにrowHeaderを追加

                // 交互に行の背景を変える
                if (colorFlg % 2 != 0) {
                    row.setBackgroundResource(R.drawable.row_deco2);
                } else {
                    row.setBackgroundResource(R.drawable.row_deco1);
                }
                colorFlg++;

            } while (c.moveToNext());
        } else {
            Toast.makeText(this, "登録されているデータがありません。", Toast.LENGTH_SHORT).show();
        }
        c.close();
        dbAdapter.closeDB();        // DBを閉じる

    }


    /**
     * 行の各項目のTextViewカスタマイズ処理
     * setTextItem()
     *
     * @param str     String
     * @param gravity int
     * @return title TextView タイトル
     */
    private TextView setTextItem(String str, int gravity) {
        TextView title = new TextView(this);
        title.setTextSize(16.0f);           // テキストサイズ
        title.setTextColor(Color.BLACK);    // テキストカラー
        title.setGravity(gravity);          // テキストのGravity
        title.setText(str);                 // テキストのセット

        return title;
    }

    /**
     * 行の各項目のLayoutParamsカスタマイズ処理
     * setParams()
     */
    private TableRow.LayoutParams setParams(float f) {
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, WC);
        params.weight = f;      //weight(行内でのテキストごとの比率)

        return params;
    }

    /**
     * トレーニング情報の削除
     * */
    private void delete(){
     /*   dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();     // DBの読み込み(読み書きの方)

        findViews();            // 各部品の結び付け

        // ArrayListを生成
        items = new ArrayList<>();

        // DBのデータを取得
        String[] columns = {DBAdapter.COL_NAME};     // DBのカラム：品名
        Cursor c = dbAdapter.getDB1(columns);

        c.close();
        dbAdapter.closeDB();    // DBを閉じる

        // 全削除ボタン押下時処理
        mButton03AllDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!items.isEmpty()) {
                    dbAdapter.openDB();     // DBの読み込み(読み書きの方)
                    dbAdapter.allDelete();  // DBのレコードを全削除
                    dbAdapter.closeDB();    // DBを閉じる

                }else {
                    Toast.makeText(SelectSheetTable1.this, "登録されているデータがありません。", Toast.LENGTH_SHORT).show();
                }

            }
        });*/
    }
}