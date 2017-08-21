package com.ktds.mydiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ktds.mydiary.db.DiaryDB;

public class MyDiaryActivity extends AppCompatActivity {

    private Button btn_signup;
    private Button btn_login;

    // onCreate() 는 애플리케이션이 처음 실행될 때만 동작하기 때문에 여기서 DB를 생성해준다.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // DB 생성
        // DB 파일이 만들어져 있자 않다면 테이블을 생성(onCreate)
        // DB 파일이 만들어져 있다면, 테이블 생성을 무시함.
        DiaryDB db = new DiaryDB(this);

        setContentView(R.layout.activity_my_diary);

        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDiaryActivity.this, SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyDiaryActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);  // history(스택)를 쌓지 말아라.
                startActivity(intent);
            }
        });
    }
}
