package com.example.a1521315.test02;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphTime extends AppCompatActivity {

    private DBAdapter dbAdapter;                // DBAdapter

    Globals globals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_linechart);

        LineChart lineChart = (LineChart) findViewById(R.id.chart);


        ArrayList<Entry> entries = new ArrayList<>();

        dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();     // DBの読み込み(読み書きの方)

        // ArrayListを生成
        String column = "name_id";          //検索対象のカラム名
        String[] name_id = {globals.name_id};            //検索対象の文字

        // DBの検索データを取得 入力した文字列を参照してDBの品名から検索
        Cursor c = dbAdapter.searchDB(null, column, name_id);

        // DBのデータを取得
        /*
        String[] xColumns = {DBAdapter.COL_DATE};     // DBのカラム：品名
        Cursor xc = dbAdapter.getDB1(xColumns);
        */

        if (c.moveToFirst()) {
            int Num = 0;
            do {
                //entries.add(c.getString(5),Num);
                Log.d("取得したCursor:", c.getString(5));
                Num = Num + 1;
            } while (c.moveToNext());
        }

        LineDataSet dataset = new LineDataSet(entries, "# of Calls");

        // ArrayListを生成
        ArrayList<String> labels = new ArrayList<String>();

        if (c.moveToFirst()) {
            do {
                labels.add(new String (c.getString(12)));
                Log.d("取得したCursor:", c.getString(12));
            } while (c.moveToNext()/* && yc_num.moveToNext()*/);
        }
        c.close();
        dbAdapter.closeDB();    // DBを閉じる



        LineData data = new LineData(labels, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);

        lineChart.setData(data);
        lineChart.animateY(5000);

    }
}
