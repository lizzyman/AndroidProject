package com.ktds.mydiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ktds.mydiary.db.DiaryDB;
import com.ktds.mydiary.model.Member;

public class LoginActivity extends AppCompatActivity {

    private EditText ed_id;
    private EditText ed_password;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 로그인을 이미 했다면, List 목록 화면으로 이동.
        if ( MyDiaryApplication.member != null ) {
            Intent intent = new Intent(LoginActivity.this, ListDiaryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);

        ed_id = (EditText) findViewById(R.id.ed_id);
        ed_password = (EditText) findViewById(R.id.ed_password);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 로그인 여부 판단
                String id = ed_id.getText().toString();
                String password = ed_password.getText().toString();

                DiaryDB diaryDB = new DiaryDB(LoginActivity.this);
                MyDiaryApplication.member = diaryDB.selectUser(id, password);

                if ( MyDiaryApplication.member != null ) {
                    // 매끄러운 페이지 이동을 위해 작성 페이지가 아닌 목록 페이지를 먼저 보여준다.
                    Intent intent = new Intent(LoginActivity.this, ListDiaryActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "아이디/비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
