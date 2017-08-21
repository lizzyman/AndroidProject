package com.naver.blog.hiddenplaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DownTownListActivity extends AppCompatActivity {

    private Button btn_search_with_location;
    private Button btn_gangnam;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_town_list);

        btn_search_with_location = (Button) findViewById(R.id.btn_search_location);
        btn_gangnam = (Button) findViewById(R.id.btn_gangnam);
        btn_back = (Button) findViewById(R.id.btn_back);

        btn_search_with_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DownTownListActivity.this, SearchWithLocationActivity.class);
                startActivity(intent);
            }
        });

        btn_gangnam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DownTownListActivity.this, HiddenPlaceActivity.class);
                startActivity(intent);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DownTownListActivity.this, CityListActivity.class);
                startActivity(intent);
            }
        });

    }
}
