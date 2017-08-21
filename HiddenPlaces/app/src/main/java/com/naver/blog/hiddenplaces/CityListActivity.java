package com.naver.blog.hiddenplaces;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.naver.blog.hiddenplaces.db.MockData;

import java.util.List;

public class CityListActivity extends AppCompatActivity {

    private Button btn_search_location;
    private RecyclerView rv_cityList;
    private Button btn_back;

    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_list);

        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();

        final MockData mockData = new MockData();
        data = mockData.city.get("");

        btn_search_location = (Button) findViewById(R.id.btn_search_location);
        btn_back = (Button) findViewById(R.id.btn_back);

        btn_search_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CityListActivity.this, SearchWithLocationActivity.class);
                startActivity(intent);
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (getSupportActionBar().isShowing()) {
                data = mockData.city.get("");
                rv_cityList.getAdapter().notifyDataSetChanged();

                getSupportActionBar().setTitle("");
                getSupportActionBar().hide();
            }
            else {
                finish();
            }
            }
        });
        rv_cityList = (RecyclerView) findViewById(R.id.rv_cityList);
        rv_cityList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv_cityList.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                LayoutInflater inflater = CityListActivity.this.getLayoutInflater();
                View view = inflater.inflate(R.layout.item_city,parent,false);

                ItemHolder holder = new ItemHolder(view);
                return holder;
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ItemHolder itemHolder = (ItemHolder) holder;
                String city = getItem(position);

                itemHolder.tv_cityName.setText(city);

                itemHolder.tv_cityName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = ((TextView) v).getText().toString();

                        if (!CityListActivity.this.getSupportActionBar().isShowing()) {
                            data = mockData.downtown.get(text);

                            rv_cityList.getAdapter().notifyDataSetChanged();

                            CityListActivity.this.getSupportActionBar().setTitle(text);
                            CityListActivity.this.getSupportActionBar().show();
                        }
                        else {
                            Intent intent = new Intent(CityListActivity.this, HiddenPlaceActivity.class);
                            intent.putExtra("downtown", text);
                            startActivity(intent);
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return data.size();
            }

            public String getItem(int position) {
                return data.get(position);
            }

            class ItemHolder extends RecyclerView.ViewHolder{
                public TextView tv_cityName;

                public ItemHolder (View itemView){
                    super(itemView);
                    tv_cityName = (TextView) itemView.findViewById(R.id.tv_cityName);
                }
            }
        });
    }
}