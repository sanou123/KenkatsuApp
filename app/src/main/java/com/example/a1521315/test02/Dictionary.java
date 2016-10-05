package com.example.a1521315.test02;

/**
 * Created by Administrator on 2016/09/29.
 */

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dictionary extends AppCompatActivity {
    int PARENT_DATA = 20;
    int CHILD_DATA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary);
        // 親ノードのリスト
        List<Map<String, String>> parentList = new ArrayList<Map<String, String>>();
        // 全体の子ノードのリスト
        List<List<Map<String, String>>> allChildList = new ArrayList<List<Map<String, String>>>();

        // 親ノードに表示する内容を生成
        for (int i = 0; i < PARENT_DATA; i++) {
            Map<String, String> parentData = new HashMap<String, String>();
            //parentData.put("title", "title" + Integer.toString(i));
            if(i == 0) {
                parentData.put("title", "健康（けんこう）");
            }
            if(i == 1) {
                parentData.put("title", "健康寿命（けんこうじゅみょう）");
            }
            if(i == 2) {
                parentData.put("title", "心拍数（しんぱくすう）");
            }
            if(i == 3) {
                parentData.put("title", "除脈（じょみゃく）");
            }
            if(i == 4) {
                parentData.put("title", "頻脈（ひんみゃく）");
            }
            if(i == 5) {
                parentData.put("title", "運動の目安（うんどうのめやす）");
            }
            if(i == 6) {
                parentData.put("title", "METs（めっつ）");
            }
            // 親ノードのリストに内容を格納
            parentList.add(parentData);
        }

        // 子ノードに表示する文字を生成
        for (int i = 0; i < PARENT_DATA; i++) {
            // 子ノード全体用のリスト
            List<Map<String, String>> childList = new ArrayList<Map<String, String>>();

            // 各子ノード用データ格納
            for (int j = 0; j < CHILD_DATA; j++) {
                Map<String, String> childData = new HashMap<String, String>();
                //childData.put("TITLE", "child" + Integer.toString(j));
                //childData.put("SUMMARY", "summary" + Integer.toString(j));
                if(i == 0) {
                    childData.put("TITLE", "健康とは、病気でないとか、弱っていないということではなく、肉体的にも、精神的にも、そして社会的にも、すべてが 満たされた状態にあることをいいます。（日本WHO協会訳）");
                }
                if(i == 1) {
                    childData.put("TITLE", "介護など人の手を借りずに自立した生活を送れる期間のことをいいます。");
                }
                if(i == 2) {
                    childData.put("TITLE", "心臓が全身に血液を送り出す拍動数のことです。高い値になると心臓に負荷がかかっていますが、繰り返しトレーニングすることで値も下がります。60～100bpmが正常値です。");
                }
                if(i == 3) {
                    childData.put("TITLE", "脈拍が正常値の下限値である60bpmより下である状態のことです。脳に必要な血液を十分に送れないため、めまいや失神、ふらつきなどを引き起こす場合があります。");
                }
                if(i == 4) {
                    childData.put("TITLE", "脈拍が正常値の上限値である100bpmより上である状態のことです。おもに運動で起こります。繰り返しのトレーニングで解消されます。");
                }
                if(i == 5) {
                    childData.put("TITLE", "運動後心拍によって運動の種類が変わります。\n軽い運動は心拍数が110bpmほど、110～149bpmほどでは有酸素運動、150以上で無酸素運動と言われています。");
                }
                if(i == 6) {
                    childData.put("TITLE", "MITS");
                }
                // 子ノードのリストに文字を格納
                childList.add(childData);
            }
            // 全体の子ノードリストに各小ノードリストのデータを格納
            allChildList.add(childList);
        }

        // アダプタを作る
        SimpleExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                this, parentList,
                android.R.layout.simple_expandable_list_item_1,
                new String[] { "title" }, new int[] { android.R.id.text1 },
                allChildList, android.R.layout.simple_expandable_list_item_2,
                new String[] { "TITLE" ,"SUMMARY" }, new int[] {
                android.R.id.text1, android.R.id.text2 });

        ExpandableListView lv = (ExpandableListView) findViewById(R.id.expandableListView1);
        //生成した情報をセット
        lv.setAdapter(adapter);

        // リスト項目がクリックされた時の処理
        lv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view,
                                        int groupPosition, int childPosition, long id) {
                ExpandableListAdapter adapter = parent
                        .getExpandableListAdapter();

                // クリックされた場所の内容情報を取得
               /* Map<String, String> item = (Map<String, String>) adapter
                        .getChild(groupPosition, childPosition);

                // トーストとして表示
                Toast.makeText(
                        getApplicationContext(),
                        "child clicked " + item.get("TITLE") + " "
                                + item.get("SUMMARY"), Toast.LENGTH_LONG)
                        .show();*/
                return false;
            }
        });

        // グループの親項目がクリックされた時の処理
        lv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View view,
                                        int groupPosition, long id) {

                ExpandableListAdapter adapter = parent
                        .getExpandableListAdapter();

                // クリックされた場所の内容情報を取得
                /*Map<String, String> item = (Map<String, String>) adapter
                        .getGroup(groupPosition);
                // トーストとして表示
                Toast.makeText(getApplicationContext(),
                        "parent clicked " + item.get("title"),
                        Toast.LENGTH_LONG).show();*/
                return false;
            }
        });

    }
}