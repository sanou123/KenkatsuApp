package com.example.a1521315.test02;

/**
 * Created by Administrator on 2016/09/29.
 */

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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
        //横画面に固定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
            if(i == 7) {
                parentData.put("title", "最大心拍（さいだいしんぱく）");
            }
            if(i == 8) {
                parentData.put("title", "BMI指数（びーえむあいしすう）");
            }
            if(i == 9) {
                parentData.put("title", "メタボリックシンドローム");
            }
            if(i == 10) {
                parentData.put("title", "ロコモティブシンドローム");
            }
            if(i == 11) {
                parentData.put("title", "中性脂肪（ちゅうせいしぼう）");
            }
            if(i == 12) {
                parentData.put("title", "生活習慣病（生活習慣病）");
            }
            if(i == 13) {
                parentData.put("title", "高トリグリセライド血症（こうとりぐりせらいどけっしょう）");
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
                    childData.put("TITLE", "「Metabolic equivalents」の略で、活動・運動を行った時に安静状態の何倍の代謝（カロリー消費）をしているかを表します。");
                }
                if(i == 7) {
                    childData.put("TITLE", "最大心拍数の上限のことです。年齢ごとにおおよその上限があり220-年齢で求めることがでできます。最大値の6割ほどの心拍数がダイエットに効果的です。");
                }
                if(i == 8) {
                    childData.put("TITLE", "[体重(kg)]÷[身長(m)の2乗]で算出される値です。肥満や低体重（やせ）の判定に用いられます。正常値は18.5以上25未満です。");
                }
                if(i == 9) {
                    childData.put("TITLE", "内臓脂肪型肥満（内臓肥満・腹部肥満）に高血糖・高血圧・脂質異常症のうち2つ以上の症状が一度に出ている状態です。");
                }
                if(i == 10) {
                    childData.put("TITLE", "骨、関節、筋肉などの運動器の働きが衰えることで自立した生活を送れなくなる状態です。");
                }
                if(i == 11) {
                    childData.put("TITLE", "肉や魚、食用油など食品中の脂質や、体脂肪の大部分を占める物質です。エネルギー源になりますが摂りすぎると生活習慣病を引き起こします。");
                }
                if(i == 12) {
                    childData.put("TITLE", "食事や運動・喫煙・飲酒・ストレスなどの生活習慣が深く関与し、発症の原因となる疾患の総称です。");
                }
                if(i == 13) {
                    childData.put("TITLE", "血液中の中性脂肪の値が150mg/dl以上の状態のことです。メタボリックシンドロームの診断基準でもあります。");
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

    @Override
    //戻るキーを無効にする
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setClassName("com.example.a1521315.test02",
                    "com.example.a1521315.test02.MenuSelect");
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}