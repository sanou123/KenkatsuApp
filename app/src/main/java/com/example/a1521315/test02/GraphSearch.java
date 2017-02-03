package com.example.a1521315.test02;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by 1521315 on 2017/01/27.
 */

public class GraphSearch extends AppCompatActivity {

    Globals globals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.graph_search);

        TextView YearSearch = (TextView) findViewById(R.id.year_search);
        TextView MonthSearch = (TextView) findViewById(R.id.month_search);
        TextView DaySearch = (TextView) findViewById(R.id.day_search);


        YearSearch.setText(globals.year);
        MonthSearch.setText(globals.year + globals.month);
        DaySearch.setText(globals.year + globals.month + globals.day);
    }
}
