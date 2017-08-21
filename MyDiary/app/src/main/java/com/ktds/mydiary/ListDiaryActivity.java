package com.ktds.mydiary;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.ktds.mydiary.fragments.DetailFragment;
import com.ktds.mydiary.fragments.DiaryListFragment;
import com.ktds.mydiary.fragments.DiaryWriteFragment;
import com.ktds.mydiary.model.Diary;

public class ListDiaryActivity extends AppCompatActivity {

    private Button btn_go_main;
    private Button btn_go_list;
    private Button btn_go_write;

    private PagerSlidingTabStrip tabStrip;
    private ViewPager pager;

    //Android.v4.Fragment 사용하기
    private Fragment diaryFragment;
    private Fragment writeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_diary);

//        btn_go_main = (Button) findViewById(R.id.btn_go_main);
//        btn_go_list = (Button) findViewById(R.id.btn_go_list);
//        btn_go_write = (Button) findViewById(R.id.btn_go_write);

        diaryFragment = DiaryListFragment.newInstance();
        writeFragment = DiaryWriteFragment.newInstance();

        tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabStrip);
        pager = (ViewPager) findViewById(R.id.pager);

        final String[] title = {"DIARY", "WRITE"};

        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            //CharSequence = String
            //ViewPager는 메모리관리를 알아서 처리해준다.
            //필요할때마다 fragment를 그때그때 만들어서 사용할 것이라서, 스택관리할 필요 x
            @Override
            public CharSequence getPageTitle(int position) {
                //position 이란? :
                return title[position];
            }

            @Override
            public Fragment getItem(int position) {
                //0을 클릭하면 diaryFragment가 나오고
                if(position == 0){
                    return diaryFragment;
                }else if(position == 1){
                    return writeFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                //탭 2개를 return 2 하는 것 보다, String 배열 만들어서 그 length 가져오게 코딩하기
                return title.length;
            }
        });

        //원래 Fragment는 상단에 쌓는것인데, ViewPager는 옆으로 쌓기 때문에 스택을 생각 안해도되는거고 closeFragment를 사용하지 말아야한다.
        tabStrip.setViewPager(pager);
        //write가 보이겠지~
        pager.setCurrentItem(1);

    }



    //fragment가 fragment를 띄울 수 있지만, 특정 버전에서만 그러하기 때문에, 이것을 어떻게 처리할 것인지를 생각해보아야한다.
    //액티비티를 새롭게 띄우기로한다.
    public void openDetailFragment(Diary diary) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("diary", diary);
        startActivity(intent);
    }

    public void moveFragment(int from , int to){
        pager.setCurrentItem(to);

        //List 갱신
        if(to == 0){
            ((DiaryListFragment)diaryFragment).refreshData();
        }
        if(from == 1){
            //일기 작성 폼 초기화
            ((DiaryWriteFragment)writeFragment).initiateForm();
        }
    }



//    public void closeFragment(Fragment fragment) {
//
//        // 전달 받은 Fragment 를 제거
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.remove(fragment);
//        transaction.commit();
//
//        // 이전 Fragment 를 보여줌
//        FragmentManager manager = getSupportFragmentManager();
//        manager.popBackStack();
//
//    }
}
