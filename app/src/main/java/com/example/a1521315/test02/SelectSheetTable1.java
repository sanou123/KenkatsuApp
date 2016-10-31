package com.example.a1521315.test02;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * TableLayout画面に関連するクラス
 * SelectSheetTable
 */
public class SelectSheetTable1 extends AppCompatActivity implements View.OnFocusChangeListener, SearchView.OnQueryTextListener {

    DBAdapter dbAdapter;

    private SearchView mSearchView04;           // 検索窓
    private TableLayout mTableLayout04List;     //データ表示用TableLayout

    private int colorFlg = 1;                   //背景切り替え用フラグ

    private final static int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final static int GCH = Gravity.CENTER_HORIZONTAL;
    private final static int GE = Gravity.END;         // Gravity.RIGHTでもよい

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.select_sheet_table1);

        dbAdapter = new DBAdapter(this);

        findViews();        // 各部品の結び付け

        // 検索窓を開いた状態にする(設定していない場合はアイコンをクリックしないと入力箇所が開かない)
        mSearchView04.setIconified(false);
        // 検索窓のイベント処理
        mSearchView04.setOnQueryTextListener(this);

    }

    /**
     * 各部品の結びつけ処理
     * findViews()
     */
    private void findViews() {
        mSearchView04 = (SearchView) findViewById(R.id.searchView03);               // 検索窓
        mTableLayout04List = (TableLayout) findViewById(R.id.tableLayout03List);    //データ表示用TableLayout
    }

    /**
     * SearchViewの各イベント処理
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    // 検索を始める時
    @Override
    public boolean onQueryTextSubmit(String query) {

        dbAdapter.readDB();                         // DBの読み込み(読み込みの方)

        mSearchView04.clearFocus();                 // 検索窓のフォーカスを外す(=キーボードを非表示)

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
        // ヘッダー：体重変化
        TextView headerWeight_fluctuates = setTextItem("体重変化", GCH);
        TableRow.LayoutParams paramsWeight_fluctuates = setParams(0.3f);
        // ヘッダー：総走行距離
        TextView headerTotal_time = setTextItem("総走行距離", GCH);
        TableRow.LayoutParams paramsTotal_time = setParams(0.4f);
        // ヘッダー：総走行時間
        TextView headerTotal_distance = setTextItem("総走行時間", GCH);
        TableRow.LayoutParams paramsTotal_distance = setParams(0.3f);
        // ヘッダー：総走行時間
        TextView headerCourse = setTextItem("コース名", GCH);
        TableRow.LayoutParams paramsCourse = setParams(0.3f);
        // rowHeaderにヘッダータイトルを追加
        rowHeader.addView(headerDate, paramsDate);          // ヘッダー：日時
        rowHeader.addView(headerHeart_rate, paramsHeart_rate);          // ヘッダー：最大心拍数
        rowHeader.addView(headerCalorie_consumption, paramsCalorie_consumption);          // ヘッダー：消費カロリー
        rowHeader.addView(headerWeight_fluctuates, paramsWeight_fluctuates);            // ヘッダー：体重変化
        rowHeader.addView(headerTotal_time, paramsTotal_time);          // ヘッダー：総走行距離
        rowHeader.addView(headerTotal_distance, paramsTotal_distance);            // ヘッダー：総走行時間
        rowHeader.addView(headerCourse, paramsCourse);          // ヘッダー：コース名

        rowHeader.setBackgroundResource(R.drawable.row_deco1);  // 背景

        // TableLayoutにrowHeaderを追加
        mTableLayout04List.addView(rowHeader);

        String column = "name";          //検索対象のカラム名
        String[] name = {query};            //検索対象の文字

        // DBの検索データを取得 入力した文字列を参照してDBの品名から検索
        Cursor c = dbAdapter.searchDB(null, column, name);

        if (c.moveToFirst()) {
            do {

                TableRow row = new TableRow(this);          // 行を作成
                row.setPadding(16, 12, 16, 12);             // 行のパディングを指定(左, 上, 右, 下)

                // 日時
                TextView textDate = setTextItem(c.getString(0), GCH);     // TextViewのカスタマイズ処理
                // 最大心拍数
                TextView textHeart_rate = setTextItem(c.getString(1)+" [bpm]", GCH);     // TextViewのカスタマイズ処理
                // 消費カロリー
                TextView textCalorie_consumption = setTextItem(c.getString(2)+" [cal]", GCH);      // TextViewのカスタマイズ処理
                // 体重変化
                TextView textWeight_fluctuates = setTextItem(c.getString(3)+" [kg]", GCH);      // TextViewのカスタマイズ処理
                // 総走行距離
                TextView textTotal_time = setTextItem(c.getString(4)+" [km]", GCH);      // TextViewのカスタマイズ処理
                // 総走行時間
                TextView textTotal_distance = setTextItem(c.getString(5)+" [min]", GCH);      // TextViewのカスタマイズ処理
                // コース名
                TextView textCourse = setTextItem(c.getString(0), GCH);      // TextViewのカスタマイズ処理

                // rowHeaderに各項目(DBから取得した産地,個数,単価)を追加
                row.addView(textDate, paramsDate);      // 日時
                row.addView(textHeart_rate, paramsHeart_rate);      // 最大心拍数
                row.addView(textCalorie_consumption, paramsCalorie_consumption);      // 消費カロリー
                row.addView(textWeight_fluctuates, paramsWeight_fluctuates);        // 体重変化
                row.addView(textTotal_time, paramsTotal_time);      // 総走行距離
                row.addView(textTotal_distance, paramsTotal_distance);        // 総走行時間
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
            Toast.makeText(this, "検索結果 0件", Toast.LENGTH_SHORT).show();
        }
        c.close();
        dbAdapter.closeDB();        // DBを閉じる

        return false;
    }

    // 検索窓のテキストが変わった時
    @Override
    public boolean onQueryTextChange(String newText) {

        mTableLayout04List.removeAllViews();        // TableLayoutのViewを全て消す

        return false;
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

}