package com.ktds.mydiary;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ImageActivity extends AppCompatActivity {

    private ImageView img_big;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        img_big = (ImageView) findViewById(R.id.img_big);

        Intent intent = getIntent();
        String imagePath = intent.getStringExtra("imagePath");

        img_big.setImageBitmap(BitmapFactory.decodeFile(imagePath));

        img_big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
