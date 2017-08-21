package com.ktds.mydiary;

import android.app.Application;

import com.ktds.mydiary.model.Member;

/**
 * Created by Admin on 2017-06-29.
 */

// Application 전체에서 공유를 할 수 있는 클래스가 됨.
public class MyDiaryApplication extends Application {

    public static Member member;

}
