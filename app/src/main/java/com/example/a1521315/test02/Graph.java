package com.example.a1521315.test02;

/**
 * Created by 1521315 on 2016/11/17.
 */


        import android.content.pm.ActivityInfo;
        import android.database.Cursor;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import android.widget.Toast;

        import com.github.mikephil.charting.animation.Easing;
        import com.github.mikephil.charting.charts.BarChart;
        import com.github.mikephil.charting.components.XAxis;
        import com.github.mikephil.charting.data.BarData;
        import com.github.mikephil.charting.data.BarDataSet;
        import com.github.mikephil.charting.data.BarEntry;
        import com.github.mikephil.charting.utils.ColorTemplate;

        import java.util.ArrayList;


public class Graph extends AppCompatActivity {

    private DBAdapter dbAdapter;                // DBAdapter
    private ArrayAdapter<String> adapter;       // ArrayAdapter
    private ArrayList<String> items;            // ArrayList
    private ListView mListView03Date;        // ListView


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        createBarChart();
    }

    private void createBarChart() {
        BarChart barChart = (BarChart) findViewById(R.id.bar_chart);

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
        ArrayList<String> xValues = new ArrayList<>();

        dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();     // DBの読み込み(読み書きの方)

        // DBのデータを取得
        String[] columns = {DBAdapter.COL_DATE};     // DBのカラム：日付
        Cursor c = dbAdapter.getDB1(columns);

    /*    if (c.moveToFirst()) {
            do {
                xValues.add(c.getString(3));
                Log.d("取得したCursor:", c.getString(3));
            } while (c.moveToNext());
        }else {
            Toast.makeText(this, "登録されているデータがありません。", Toast.LENGTH_SHORT).show();
        }
        */
        c.moveToFirst();


        if (!xValues.isEmpty()) {
            xValues.add(c.getString(3));
            Log.d("取得したCursor:", c.getString(3));
        } else {
            Toast.makeText(this, "登録されているデータがありません。", Toast.LENGTH_SHORT).show();
        }



        // valueA
        ArrayList<BarEntry> valuesA = new ArrayList<>();

        if (!xValues.isEmpty()) {
            valuesA.add(new BarEntry(1,2));
            Log.d("取得したCursor:", c.getString(12));
        }


        c.close();
        dbAdapter.closeDB();    // DBを閉じる

        BarDataSet valuesADataSet = new BarDataSet(valuesA, "走行距離");
        valuesADataSet.setColor(ColorTemplate.COLORFUL_COLORS[3]);

        barDataSets.add(valuesADataSet);

        // valueB
     /*   ArrayList<BarEntry> valuesB = new ArrayList<>();
        valuesB.add(new BarEntry(200, 0));
        valuesB.add(new BarEntry(300, 1));
        valuesB.add(new BarEntry(400, 2));
        valuesB.add(new BarEntry(500, 3));
        valuesB.add(new BarEntry(600, 4));
        valuesB.add(new BarEntry(700, 5));

        BarDataSet valuesBDataSet = new BarDataSet(valuesB, "B");
        valuesBDataSet.setColor(ColorTemplate.COLORFUL_COLORS[4]);

        barDataSets.add(valuesBDataSet);
        */

        BarData barData = new BarData(xValues, barDataSets);
        return barData;
    }
}
