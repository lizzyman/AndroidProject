package com.ktds.mydiary.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktds.mydiary.ImageActivity;
import com.ktds.mydiary.ListDiaryActivity;
import com.ktds.mydiary.R;
import com.ktds.mydiary.model.Diary;


public class DetailFragment extends Fragment {

    private Diary diary;

    public DetailFragment() {
    }

    public static DetailFragment newInstance(Diary diary) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();

        // ~able 로 끝나는 메소드는 인스턴스이다.
        args.putSerializable("diary", diary); // putSerializable() : pubObject() 를 대신할 수 있는 것.

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle parameter = getArguments();  // Bundle을 가져올 수 있다. (args)
        this.diary = (Diary) parameter.getSerializable("diary");

    }

    private TextView tv_today_date;
    private ImageView img_image;
    private TextView tv_content;
    private Button btn_close;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        tv_today_date = (TextView) view.findViewById(R.id.tv_today_date);
        img_image = (ImageView) view.findViewById(R.id.img_image);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        btn_close = (Button) view.findViewById(R.id.btn_close);

        tv_today_date.setText(this.diary.getWritedDate());
        img_image.setImageBitmap(BitmapFactory.decodeFile(diary.getImagePath()));
        tv_content.setText(diary.getDescript());

        img_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImageActivity.class);
                intent.putExtra("imagePath", diary.getImagePath());
                startActivity(intent);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ((ListDiaryActivity)getActivity()).closeFragment(DetailFragment.this);
            }
        });

        return view;
    }
}
