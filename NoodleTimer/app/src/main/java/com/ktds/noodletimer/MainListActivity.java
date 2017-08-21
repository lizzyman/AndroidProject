package com.ktds.noodletimer;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ktds.noodletimer.common.ListViewSimpleAdapter;
import com.ktds.noodletimer.model.Noodle;

import java.util.ArrayList;
import java.util.List;

public class MainListActivity extends AppCompatActivity {

    private ListView lv_noodle_list;
    private List<Noodle> noodleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        getMockData();

        lv_noodle_list = (ListView) findViewById(R.id.lv_noodle_list);
        lv_noodle_list.setAdapter(new ListViewSimpleAdapter(MainListActivity.this, R.layout.item_noodle, noodleList) {
            @Override
            public Holder createHolder(View view) {
                ItemHolder itemHolder = new ItemHolder(view);

                return itemHolder;
            }

            @Override
            public void setContent(Holder holder, int position) {
                final Noodle noodle = (Noodle) getItem(position);

                ItemHolder itemHolder = (ItemHolder) holder;
                itemHolder.tv_noodleName.setText(noodle.getNoodleName());
                itemHolder.iv_noodleImage.setImageDrawable(noodle.getImage());

                itemHolder.tv_noodleName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainListActivity.this, TimerActivity.class);
                        intent.putExtra("time", noodle.getCookingTime());
                        startActivity(intent);
                    }
                });
            }

            class ItemHolder extends Holder {
                public TextView tv_noodleName;
                public ImageView iv_noodleImage;

                public ItemHolder(View v) {
                    super(v);

                    tv_noodleName = (TextView) v.findViewById(R.id.tv_noodleName);
                    iv_noodleImage = (ImageView) v.findViewById(R.id.iv_noodleImage);
                }
            }
        });

    }

    private void getMockData() {
        Drawable drawable1 = getResources().getDrawable(R.drawable.nuguri);
        Noodle noodle1 = new Noodle();
        noodle1.setNoodleName("너구리 라면");
        noodle1.setImage(drawable1);
        noodle1.setCookingTime(180);
        noodleList.add(noodle1);

        Drawable drawable2 = getResources().getDrawable(R.drawable.jin);
        Noodle noodle2 = new Noodle();
        noodle2.setNoodleName("진라면");
        noodle2.setImage(drawable2);
        noodle2.setCookingTime(170);
        noodleList.add(noodle2);

        Drawable drawable3 = getResources().getDrawable(R.drawable.sin);
        Noodle noodle3 = new Noodle();
        noodle3.setNoodleName("신라면");
        noodle3.setImage(drawable3);
        noodle3.setCookingTime(215);
        noodleList.add(noodle3);

    }
}
