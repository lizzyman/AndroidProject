package com.ktds.mydiary;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktds.mydiary.model.Diary;

public class DetailActivity extends AppCompatActivity {

    private TextView tv_today_date;
    private ImageView img_image;
    private TextView tv_content;
    private Button btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tv_today_date = (TextView)findViewById(R.id.tv_today_date);
        img_image = (ImageView) findViewById(R.id.img_image);
        tv_content = (TextView) findViewById(R.id.tv_content);
        btn_close = (Button) findViewById(R.id.btn_close);

        Intent intent = getIntent();
        final Diary diary = (Diary) intent.getSerializableExtra("diary");

        tv_today_date.setText(diary.getWritedDate());
        img_image.setImageBitmap(BitmapFactory.decodeFile(diary.getImagePath()));
        tv_content.setText(diary.getDescript());

        img_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, ImageActivity.class);
                intent.putExtra("imagePath", diary.getImagePath());
                startActivity(intent);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            finish();
            }
        });
    }
}
