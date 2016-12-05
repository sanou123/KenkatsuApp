package com.example.a1521315.test02;

/**
 * Created by 1521315 on 2016/11/17.
 */


import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class GraphMile extends AppCompatActivity {

    private DBAdapter dbAdapter;                // DBAdapter

    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distance_barchart);
        //横画面に固定

        createBarChart();

    }

    private void createBarChart() {
        BarChart barChart = (BarChart) findViewById(R.id.bar_chart);
        barChart.setDescription("");

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(true);
        barChart.setDrawGridBackground(true);
        barChart.setDrawBarShadow(false);
        barChart.setEnabled(true);

        barChart.setTouchEnabled(true);
        barChart.setPinchZoom(true);
        barChart.setDoubleTapToZoomEnabled(true);

        barChart.setHighlightEnabled(true);
        barChart.setDrawHighlightArrow(true);
        barChart.setHighlightEnabled(true);

        barChart.setScaleEnabled(true);

        barChart.getLegend().setEnabled(true);

        //X軸周り
        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setSpaceBetweenLabels(0);

        barChart.setData(createBarChartData());

        barChart.invalidate();
        // アニメーション
        barChart.animateY(2000, Easing.EasingOption.EaseInBack);
    }

    // BarChartの設定
    private BarData createBarChartData() {
        ArrayList<BarDataSet> barDataSets = new ArrayList<>();

        // X軸
        /*ArrayList<String> xValues = new ArrayList<>();
        xValues.add(globals.date);*/

        //////////////////////////////////////////////////////////////////////////////
        dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();     // DBの読み込み(読み書きの方)

        // ArrayListを生成
        //items = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();

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
            do {
                xValues.add(c.getString(3)+c.getString(4)+c.getString(5)+'\n'
                        +c.getString(6));
                Log.d("取得したCursor:", c.getString(3)+c.getString(4)+c.getString(5)+'\n'
                        +c.getString(6));
            } while (c.moveToNext());
        }

        //////////////////////////////////////////////////////////////////////////////


        // valueA
        /*ArrayList<BarEntry> valuesA = new ArrayList<>();
        valuesA.add(new BarEntry(100, 0));*/

        //////////////////////////////////////////////////////////////////////////////


        // ArrayListを生成
        ArrayList<BarEntry> valuesA = new ArrayList<>();

        // DBのデータを取得
        /*String[] yColumns = {DBAdapter.COL_DISTANCE};     // DBのカラム：品名
        Cursor yc = dbAdapter.getDB1(yColumns);

        String[] yColumns_Num = {DBAdapter.COL_ID_DATA};     // DBのカラム：品名
        Cursor yc_num = dbAdapter.getDB1(yColumns_Num);*/

        if (c.moveToFirst()) {
            int Num = 0;
            do {
                valuesA.add(new BarEntry(Float.parseFloat(c.getString(15)), Num));
                Log.d("取得したCursor:", c.getString(15));
                Num = Num +1;
            } while (c.moveToNext()/* && yc_num.moveToNext()*/);
        }
        c.close();
        dbAdapter.closeDB();    // DBを閉じる

        //////////////////////////////////////////////////////////////////////////////


        BarDataSet valuesADataSet = new BarDataSet(valuesA, "走行距離");
        valuesADataSet.setColor(ColorTemplate.COLORFUL_COLORS[3]);

        barDataSets.add(valuesADataSet);



        BarData barData = new BarData(xValues, barDataSets);
        return barData;
    }
}