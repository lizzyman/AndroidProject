package com.ktds.mydiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ktds.mydiary.db.DiaryDB;
import com.ktds.mydiary.model.Member;

public class SignUpActivity extends AppCompatActivity {

    private EditText ed_email;
    private EditText ed_name;
    private EditText ed_age;
    private EditText ed_id;
    private EditText ed_password;
    private Button btn_login;
    private Button btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ed_id = (EditText) findViewById(R.id.ed_id);
        ed_password = (EditText) findViewById(R.id.ed_password);
        ed_email = (EditText) findViewById(R.id.ed_email);
        ed_age = (EditText) findViewById(R.id.ed_age);
        ed_name = (EditText) findViewById(R.id.ed_name);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Member member = new Member();
                member.setId( ed_id.getText().toString() );
                member.setPassword( ed_password.getText().toString() );
                member.setEmail( ed_email.getText().toString() );
                member.setName( ed_name.getText().toString() );
                member.setAge( Integer.parseInt(ed_age.getText().toString()) );

                String message = String.format("Email : %s\nName: %s\nAge: %d\nId: %s\nPassword: %s",
                        member.getEmail(), member.getName(), member.getAge(), member.getId(), member.getPassword());

                // 사용자에게 묻는 작업
                AlertDialog.Builder dialog = new AlertDialog.Builder(SignUpActivity.this);
                dialog.setTitle("아래의 정보로 가입하시겠습니까?")
                        .setMessage(message)
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(SignUpActivity.this, "회원가입을 취소했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DiaryDB diaryDB = new DiaryDB(SignUpActivity.this);
                                diaryDB.insertMember(member);

                                Toast.makeText(SignUpActivity.this, "회원가입이 완료되었습니다. 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();

                                String id = ed_id.getText().toString();
                                String password = ed_password.getText().toString();

                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                intent.putExtra("id", id);
                                intent.putExtra("password", password);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent);
                            }
                        }).create().show();
            }
        });

        btn_close = (Button) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
