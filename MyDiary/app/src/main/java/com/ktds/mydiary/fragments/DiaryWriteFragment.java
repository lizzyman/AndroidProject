package com.ktds.mydiary.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ktds.mydiary.DatePickActivity;
import com.ktds.mydiary.ListDiaryActivity;
import com.ktds.mydiary.R;
import com.ktds.mydiary.db.DiaryDB;
import com.ktds.mydiary.model.Diary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DiaryWriteFragment extends Fragment {

    public DiaryWriteFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static DiaryWriteFragment newInstance() {
        DiaryWriteFragment fragment = new DiaryWriteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private Button btn_add_image;
    private EditText ed_today_date;
    private ImageView img_image;
    private EditText ed_content;
    private Button btn_close;
    private Button btn_save;

    private  String imagePath;
    private String thumnailImagePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requestPermission();

        // Fragment는 Activity 가 아니기 때문에 layout이 없다 그래서 아래처럼 view를 사용하여 layout을 설정해줘야 한다.
        View view = inflater.inflate(R.layout.fragment_diary_write, container, false);

        btn_add_image = (Button) view.findViewById(R.id.btn_add_image);
        ed_today_date = (EditText) view.findViewById(R.id.ed_today_date);
        img_image = (ImageView) view.findViewById(R.id.img_image);
        ed_content = (EditText) view.findViewById(R.id.ed_content);
        btn_close = (Button) view.findViewById(R.id.btn_close);
        btn_save = (Button) view.findViewById(R.id.btn_save);


        ed_today_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DatePickActivity.class);
                startActivityForResult(intent, 2000);
            }
        });

        // 앨범에서 이미지 가져오기
        // 앨범에서 어떤 이미지를 가져올래?
        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK); // 암묵적 인텐트 (무언가를 가져올 때 사용.) <-> 명시적 인텐트
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 1000);  // 액티비티를 띄워서 결과를 받아오겠다.
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ListDiaryActivity) getActivity()).moveFragment(1,0);
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Diary diary = new Diary();

                diary.setWritedDate( ed_today_date.getText().toString());
                diary.setDescript( ed_content.getText().toString());
                diary.setImagePath( imagePath );
                diary.setThumbnailImagePath(thumnailImagePath);

                DiaryDB diaryDB = new DiaryDB(getActivity());
                diaryDB.insertDiary(diary);

                ((ListDiaryActivity) getActivity()).moveFragment(1,0);
            }
        });

        return view;
    }

    //글을 작성하고나서 writeFragment의 data를 초기화 시켜주는 함수
    public void initiateForm(){
        ed_today_date.setText("");
        ed_content.setText("");
        img_image.setImageBitmap(null);
    }



    // 어떤 이미지를 선택했는지 결과를 가져옴.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ( requestCode == 1000 && data != null ) {
            Uri imageUri = data.getData();

            if ( imageUri != null ) {
                /**********************************
                 * 이미지 파일의 경로 얻어오기 시작
                 **********************************/

                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor = getActivity().managedQuery(imageUri, proj, null, null, null);
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();
                imagePath = cursor.getString(columnIndex);
                Toast.makeText(getActivity(), imagePath, Toast.LENGTH_SHORT).show();

                /********************************
                 * 이미지 파일의 경로 얻어오기 끝
                 ********************************/

                Bitmap photo = null;

//                try {
//                    photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    photo = getThumbnail(getActivity().getContentResolver(), imagePath); // 썸네일 받아오기 (아주 작은 용량의 이미지를 가져와서 보여준다.)

                thumnailImagePath = getDownloadDirectory().getAbsolutePath();
                thumnailImagePath += File.separator;
                thumnailImagePath += "thumb" + splitFileName(imagePath);

                Log.d("THMB", thumnailImagePath);

                saveBitmapToFileCache(photo, thumnailImagePath);

//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                img_image.setImageBitmap(photo);
            }
        }
        else if ( requestCode == 2000 ) {
            String date = data.getStringExtra("DATE");  // DatePickActivity 가 전달한 DATE 를 가져올 수 있다.
            ed_today_date.setText(date);
        }
    }

    // 썸네일 가져오기
    public Bitmap getThumbnail(ContentResolver cr, String path) {

        Cursor ca = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.MediaColumns._ID },
                MediaStore.MediaColumns.DATA + "=?",
                new String[] {path}, null);

        if (ca != null && ca.moveToFirst()) {
            int id = ca.getInt(ca.getColumnIndex(MediaStore.MediaColumns._ID));
            ca.close();

            return MediaStore.Images.Thumbnails.getThumbnail(
                    cr, id, MediaStore.Images.Thumbnails.MINI_KIND, null );
        }

        ca.close();
        return null;
    }

    private String splitFileName(String filePath) {

        //  /user/photo/diary/a.jpeg (가장 마지막에 있는 경로구분자가 몇 번째에 있느냐?)
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.length()); // a.jpeg 가 나옴. (lastIndexOf : a 가 나옴.)

    }

    // Device 에 저장하기
    private File getDownloadDirectory() {
        // 이 핸드폰에 외장메모리가 있는가?(있으면 외장메모리에, 없으면 내장메모리에 저장)
        String externalStorageState = Environment.getExternalStorageState();
        if ( externalStorageState.equals(Environment.MEDIA_MOUNTED) ) { // 외장메모리가 꽂혀있다면
            String externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath(); // 외장메모리의 절대주소 받아오기

            externalStoragePath += File.separator + "diary" + File.separator + "photo"; // 외장 메모리의 경로에서 diary를 만들고 photo를 만들겠다.

            File externalStorage = new File(externalStoragePath);

            if ( !externalStorage.exists() ) { //  이 디렉토리가 없다면
                externalStorage.mkdirs();  // 이 파일 경로를 만들어라
            }
            return externalStorage;
        }
        else {  // 외장메모리가 없다면
            String dataDirectoryPath = Environment.getDataDirectory().getAbsolutePath();

            dataDirectoryPath += File.separator + "diary" + File.separator + "photo";

            File dataDirectory = new File(dataDirectoryPath);

            if ( !dataDirectory.exists() ) {
                dataDirectory.mkdirs();
            }
            return dataDirectory;
        }
    }

    // 썸네일 저장하기
    private void saveBitmapToFileCache(Bitmap bitmap, String strFilePath) {

        File fileCacheItem = new File(strFilePath);
        OutputStream out = null;

        try
        {
            fileCacheItem.createNewFile();
            out = new FileOutputStream(fileCacheItem);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {

            // 사용자의 동의가 필요한 권한.
            int permission = getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);

            if ( permission == PackageManager.PERMISSION_DENIED ) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1001);
            }

            permission = getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if ( permission == PackageManager.PERMISSION_DENIED ) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
            }
        }
    }

}
