package com.ktds.mydiary.fragments;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ktds.mydiary.ListDiaryActivity;
import com.ktds.mydiary.R;
import com.ktds.mydiary.db.DiaryDB;
import com.ktds.mydiary.model.Diary;

import java.util.List;

public class DiaryListFragment extends Fragment {

    public DiaryListFragment() {
    }

    public static DiaryListFragment newInstance() {
        DiaryListFragment fragment = new DiaryListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private GridView gv_diary_list;

    private DiaryDB diaryDB;
    private List<Diary> diaryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary_list, container, false);

        diaryDB = new DiaryDB(getActivity());
        diaryList = diaryDB.selectDiary();

        gv_diary_list = (GridView) view.findViewById(R.id.gv_diary_list);
        gv_diary_list.setAdapter(new ArrayAdapter<Diary>(getActivity(), 0, diaryList) {

            ItemHolder holder = null;

            @NonNull
            @Override
            public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater itemInflater = getActivity().getLayoutInflater();
                    convertView = itemInflater.inflate(R.layout.item_diary, parent, false);

                    holder = new ItemHolder();
                    holder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
                    holder.tv_writedDate = (TextView) convertView.findViewById(R.id.tv_writedDate);

                    convertView.setTag(holder);
                } else {
                    holder = (ItemHolder) convertView.getTag();
                }

                final Diary diary = getItem(position);
                holder.tv_writedDate.setText(diary.getWritedDate());

                if ( diary.getThumbnailImagePath() != null && diary.getThumbnailImagePath().length() > 0 ) { // 데이터가 있다면,
                    Bitmap photo = BitmapFactory.decodeFile(diary.getThumbnailImagePath());
                    holder.iv_photo.setImageBitmap(photo);
                }
                else {
                    holder.iv_photo.setImageResource(R.drawable.shin); // 없으면 신민아 Image를 보여줘라.
                }

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ( (ListDiaryActivity) getActivity()).openDetailFragment(diary);
                    }
                });

                return convertView;
            }

            class ItemHolder {
                public ImageView iv_photo;
                public TextView tv_writedDate;
            }
        });
        return view;
    }

    //data 갱신 코드
    public void refreshData(){
        //DB를 가져오고, diaryList를 갱신시켜서 Adapter에게 알려줄 것이다.
        //dairyDB, diaryList, gv_diary_list 이 3가지에 접근해야한다 : 즉, 인스턴스화 시켜야한다.
        //"인스턴스는 메모리를 가진다" 이것에대해 완벽하게 이해해야한다.


        //아래 코드로 했을 때 왜 갱신이 안되냐면? gv_diary_list는 메모리를 참조하고있는거라서 Data를 갱신시킬 수 없어.
//        diaryList = diaryDB.selectDiary();
//        //BaseAdapter 로 바꾼다 : 갱신은 안될 것이다.
//        ((BaseAdapter) gv_diary_list.getAdapter()).notifyDataSetChanged();

        //메모리는 바뀌지않고 데이터만 바뀔 수 있도록 해주어야한다.
        diaryList.clear();
        diaryList.addAll(diaryDB.selectDiary());
        ((BaseAdapter) gv_diary_list.getAdapter()).notifyDataSetChanged();

    }
}
