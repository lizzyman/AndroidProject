package com.naver.blog.hiddenplaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.naver.blog.hiddenplaces.common.ListViewSimpleAdapter;
import com.naver.blog.hiddenplaces.db.MockData;

import java.util.List;

public class HiddenPlaceActivity extends AppCompatActivity {

    private Button btn_location;
    private Button btn_region;
    private Button btn_back;

    private ListView lv_placeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_place);

        btn_location = (Button) findViewById(R.id.btn_location);
        btn_region = (Button) findViewById(R.id.btn_region);
        btn_back = (Button) findViewById(R.id.btn_back);
        lv_placeList = (ListView) findViewById(R.id.lv_placeList);

        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HiddenPlaceActivity.this, SearchWithLocationActivity.class);
                startActivity(intent);
            }
        });

        btn_region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HiddenPlaceActivity.this, CityListActivity.class);
                startActivity(intent);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String downtown = intent.getStringExtra("downtown");

        MockData mmockData = new MockData();
        List<String> place = mmockData.place.get(downtown);

        getSupportActionBar().setTitle(downtown);

        lv_placeList.setAdapter(new ListViewSimpleAdapter(HiddenPlaceActivity.this, R.layout.item_city, place) {
            @Override
            public Holder createHolder(View view) {
                ItemHolder itemHolder = new ItemHolder(view);

                return itemHolder;
            }

            @Override
            public void setContent(Holder holder, int position) {
                String place = (String)getItem(position);
                ItemHolder itemHolder = (ItemHolder) holder;
                itemHolder.tv_cityName.setText(place);
            }

            class ItemHolder extends Holder {
                public TextView tv_cityName;

                public ItemHolder(View v) {
                    super(v);

                    tv_cityName = (TextView) v.findViewById(R.id.tv_cityName);
                }
            }
        });


    }
}
