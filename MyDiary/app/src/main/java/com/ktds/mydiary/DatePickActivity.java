package com.ktds.mydiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickActivity extends AppCompatActivity {

    private DatePicker dp_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_pick);

        // java.Util 에 있는 Calendar를 사용.
        Calendar calendar = Calendar.getInstance();

        dp_date = (DatePicker) findViewById(R.id.dp_date);
        dp_date.init(calendar.get(Calendar.YEAR)
                        , calendar.get(Calendar.MONTH)
                        , calendar.get(Calendar.DAY_OF_MONTH)
                        , new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String date = String.format("Year: %d, Month: %d, Day: %d", year, monthOfYear, dayOfMonth);
                                Log.d("DAY", date);

                                monthOfYear++; // 안드로이드에서 Calendar는 0부터 시작하므로 1을 더해주고 (1월부터 시작하도록 설정)
                                // 월의 숫자가 10보다 크면 그대로 사용하고, 작으면 앞에 0을 붙여준다.
                                String month = monthOfYear > 10 ? monthOfYear + "" : "0" + monthOfYear; // 06
                                // 일의 숫자가 10보다 크면 그대로 사용하고, 작으면 앞에 0을 붙여준다.
                                String day = dayOfMonth > 10 ? dayOfMonth + "" : "0" + dayOfMonth; // 01

                                Intent intent = new Intent();
                                intent.putExtra("DATE", String.format("%d.%s.%s", year, month, day)); // 앞이 %d 인 이유는 년도가 숫자이기 때문에

                                setResult(RESULT_OK, intent); // 나를 호출한 액티비티에게 결과를 돌려준다.
                                finish();
                            }
                });



        dp_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = dp_date.getYear();
                int month = dp_date.getMonth();
                int dayOfMonth = dp_date.getDayOfMonth();

                Log.d("DAY", year + "");
                Log.d("DAY", month + "");
                Log.d("DAY", dayOfMonth + "");

            }
        });
    }

}
