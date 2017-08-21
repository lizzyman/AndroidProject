package com.ktds.mydiary.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.ktds.mydiary.model.Diary;
import com.ktds.mydiary.model.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2017-06-29.
 */

public class DiaryDB extends SQLiteOpenHelper {

    // 생성자에서 변수를 써야 하므로 final 앞에 static을 해준다.
    private static final String DB_NAME = "DIARY";
    private static final int DB_VERSION = 4;

    public DiaryDB(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    /**
     *
     * @param context : Activity
     * @param name : DB 이름
     * @param factory : null
     * @param version : DB 버전 --> 기능이 업데이트 되면서 테이블/컬럼 등이 추가/삭제 될 경우 버전을 올려줌.
     */
    public DiaryDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 애플리케이션이 처음 실행될 때만 onCreate() 가 실행된다.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Member 테이블 생성.
        // 안드로이드에서는 테이블을 만드는 방법이 없기 때문에 직접 코드로 작성해야 한다.
        StringBuffer query = new StringBuffer();
        query.append(" CREATE TABLE MEMBER ( ");
        query.append(" ID TEXT PRIMARY KEY NOT NULL, ");
        query.append(" PASSWORD TEXT NOT NULL, ");
        query.append(" EMAIL TEXT NOT NULL, ");
        query.append(" NAME TEXT NOT NULL, ");
        query.append(" AGE INTEGER ");
        query.append(" ) ");

        db.execSQL(query.toString());

        onUpgrade(db, 1, DB_VERSION);  // 설치된 애플리케이션을 지우고 다시 설치시 에러 발생하는데, 이를 추가하면 해결된다.

        Log.d("DB", "TABLE 만들기 완료!");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                StringBuffer query = new StringBuffer();
                query.append(" CREATE TABLE IF NOT EXISTS DIARY ( ");  // DIARY 테이블이 없으면 만들어라.
                query.append(" ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
                query.append(" WRITED_DATE TEXT NOT NULL, ");
                query.append(" DESCRIPT TEXT NOT NULL, ");
                query.append(" IMAGE_PATH TEXT NOT NULL ");
                query.append(" ) ");

                db.execSQL(query.toString());
            case 2:
                query = new StringBuffer();
                query.append(" ALTER TABLE DIARY ");
                query.append(" ADD THMB_IMAGE_PATH TEXT NULL ");

                db.execSQL(query.toString());
        }
    }

    public void insertMember(Member member) {

        StringBuffer query = new StringBuffer();

        // 안드로이드는 경량 DB 이기 때문에 고성능을 요구하는 query는 작성하지 못한다.
        query.append(" INSERT INTO MEMBER ( ");
        query.append(" ID, EMAIL, AGE, NAME, PASSWORD ) ");
        query.append(" VALUES ( ?, ?, ?, ?, ? ) ");

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query.toString(), new Object[]{ member.getId(), member.getEmail(), member.getAge(), member.getName(), member.getPassword() }); // 파라미터가 존재하는 것은 두번째 execSQL() 을 사용한다.
        db.close();
    }

    // SELECT query일 때만 execSQL() 이 아닌 rowQuery( ) 를 사용한다.
    public Member selectUser(String id, String password) {

        StringBuffer query = new StringBuffer();
        query.append(" SELECT   ID ");
        query.append("          , EMAIL ");
        query.append("          , PASSWORD ");
        query.append("          , NAME ");
        query.append("          , AGE ");
        query.append(" FROM     MEMBER ");
        query.append(" WHERE    ID = ? ");
        query.append(" AND      PASSWORD = ? ");

        Member member = null;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query.toString(), new String[]{id, password}); // Cursor : java에 있는 resultSet 과 같은 것이다.

        // query 의 결과가 있을 때 데이터가 채워져라.
        if ( cursor.moveToNext() ) {
            member = new Member();
            member.setId( cursor.getString( cursor.getColumnIndex("ID" ) ) );
            member.setPassword( cursor.getString( cursor.getColumnIndex("PASSWORD") ) );
            member.setName( cursor.getString( cursor.getColumnIndex("NAME") ) );
            member.setEmail( cursor.getString( cursor.getColumnIndex("EMAIL") ) );
            member.setAge( cursor.getInt( cursor.getColumnIndex("AGE") ) );
        }

        cursor.close();
        return member;
    }

    public void insertDiary(Diary diary) {
        StringBuffer query = new StringBuffer();
        query.append(" INSERT   INTO DIARY ( ");
        query.append(" WRITED_DATE, DESCRIPT, IMAGE_PATH, THMB_IMAGE_PATH ");
        query.append(" ) VALUES ( ?, ?, ?, ? ) ");

        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(query.toString(), new Object[] {diary.getWritedDate(), diary.getDescript(), diary.getImagePath(), diary.getThumbnailImagePath()});
        db.close();
    }

    public List<Diary> selectDiary() {
        StringBuffer query = new StringBuffer();
        query.append(" SELECT   ID ");
        query.append("          , WRITED_DATE ");
        query.append("          , DESCRIPT ");
        query.append("          , IMAGE_PATH ");
        query.append("          , THMB_IMAGE_PATH ");
        query.append(" FROM     DIARY ");
        query.append(" ORDER    BY WRITED_DATE DESC ");

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(query.toString(), null);

        List<Diary> diaryList = new ArrayList<>();
        Diary diary = null;

        while(cursor.moveToNext()) {
            diary = new Diary();
            diary.setId( cursor.getString( cursor.getColumnIndex("ID") ) );
            diary.setWritedDate( cursor.getString( cursor.getColumnIndex("WRITED_DATE") ) );
            diary.setDescript( cursor.getString( cursor.getColumnIndex("DESCRIPT") ) );
            diary.setImagePath( cursor.getString( cursor.getColumnIndex("IMAGE_PATH") ) );
            diary.setThumbnailImagePath( cursor.getString( cursor.getColumnIndex("THMB_IMAGE_PATH") ) );

            diaryList.add(diary);
        }

        cursor.close();
        db.close();  // close() 를 안해주면 메모리 누수가 발생한다.

        return diaryList;
    }
}
