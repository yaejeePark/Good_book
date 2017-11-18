package org.androidtown.goodbook;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sang on 2017-04-08.
 */

public class TabTree extends Fragment {

    int mDegree = 0;
    ImageButton seedView;
    TextView goal;
    ImageView background;
    BitmapDrawable dr;
    //    int currentPage = 500;
//    int goalPage = 1000;
    int stateIndex;
    String tree = "";
    Handler handler = new Handler();
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    int widthSmallTree = 100;
    int heightSmallTree = 130;
    TextView tvPurposeState;

    //progress bar
    ProgressBar progressBar;
    static SQLiteDatabase db;
    static BookListDBHelper helper;

    int sub_purpose = 0;
    int seed_half_process = 0;
    int nGoalPages = 0;
    int nSum = 0;
    int nCur = 0;
    String strGoalDate = "";
    Date dateGoal;
    String strReadDate = "";
    Date dateRead;
    String strGoalState;

//    ArrayList<Integer> images1 = new ArrayList<>();//각 단계 이미지 저장
//    ArrayList<Integer> images2 = new ArrayList<>();//각 단계에 따른 애니메이션 이미지 저장

    ArrayList<Bitmap> images1 = new ArrayList<>();//각 단계 이미지 저장
    ArrayList<Bitmap> images2 = new ArrayList<>();//각 단계에 따른 애니메이션 이미지 저장


    public static TabTree newInstance() {
        TabTree myFragment = new TabTree();

        Log.d("CalledSequence", "(TabTreeFunc)init");

        return myFragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("CalledSequence", "(TabTreeFunc)onCreateView");

        nSum = 0;
        nCur = 0;
        strGoalDate = "";
        mDegree = 0;

        View rootView = inflater.inflate(R.layout.tab_tree, container, false);
        long time = System.currentTimeMillis();
//실시간으로 받아와서 뒤에 imageView띄워주기
        background = (ImageView) rootView.findViewById(R.id.imageView2);
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd kk:mm:ss");
        String str = dayTime.format(new Date(time));
        String str_sub = str.substring(11, 13);//11,13 14이 나오겠지
        dr = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.drawable.background_night);
        if (Integer.parseInt(str_sub) < 8 || 17 < Integer.parseInt(str_sub))
            background.setImageDrawable(dr);
        seedView = (ImageButton) rootView.findViewById(R.id.TreeView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        imageView3 = (ImageView) rootView.findViewById(R.id.imageView3);
        imageView4 = (ImageView) rootView.findViewById(R.id.imageView4);
        imageView5 = (ImageView) rootView.findViewById(R.id.imageView5);
        tvPurposeState = (TextView) rootView.findViewById(R.id.tvPurposeState);

        //이미지 뷰 회전
        //drawable에 다가 최근에 만든 나무 이미지 넣어주면 됨
        //if(이미지가 있으면 1개)

        //if문써서 만약에 가장 최근에 만든 나무가 있다면 출력
        //BG tree1
        mDegree = mDegree - 15;
        BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.drawable.grape_tree5_3);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap src = drawable.getBitmap();
        Bitmap resized = Bitmap.createScaledBitmap(src, widthSmallTree, heightSmallTree, true);
        imageView3.setImageBitmap(rotateImage(resized, mDegree));

        //if문써서 만약에 가장 최근에 만든 나무가 있다면 출력
        //BG tree2
        drawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.drawable.orange_tree5_3);
        options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        src = drawable.getBitmap();
        resized = Bitmap.createScaledBitmap(src, widthSmallTree, heightSmallTree, true);
        imageView4.setImageBitmap(resized);

        //if문써서 만약에 가장 최근에 만든 나무가 있다면 출력
        //BG tree3
        mDegree = mDegree + 18;
        drawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), R.drawable.tree5_2);
        options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        src = drawable.getBitmap();
        resized = Bitmap.createScaledBitmap(src, widthSmallTree, heightSmallTree, true);
        imageView5.setImageBitmap(rotateImage(resized, mDegree));
        //<-

        //DB->
        helper = new BookListDBHelper(getContext(), // 현재 화면의 context
                "book.db",
                null, // 커서 팩토리
                1); // 버전 번호
        //<-

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor cursor;
        SimpleDateFormat sdf;

//
//        //현재 레벨 계산
//        int sub_purpose = 0;
//        int seed_half_process = 0;
//        sub_purpose = (int) (nGoalPages / 10);
//        seed_half_process = nGoalPages - (sub_purpose * 9);
////        sub_purpose=(int)(goalPage/10);
////        seed_half_process=goalPage-(sub_purpose*9);
//
//        //currentPage <- 사용자가 읽은 양
//        //goalPage <- 사용자가 설정한 목표
//
//        if ((0 <= nSum) && (nSum <= seed_half_process + sub_purpose))
//            stateIndex = 0;
//        else {
//            for (int i = sub_purpose * 3; i <= nGoalPages; i = i + sub_purpose) {
//                if (nSum <= i) {
//                    stateIndex = (i / sub_purpose) - 2;
//                    break;
//                }
//            }
//        }
//
//        setImages(stateIndex, tree);
//        seedView.setImageBitmap(images1.get(0));

        seedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seedView.setClickable(false);


                MyThread treeThread = new MyThread(stateIndex);//임의로 지정
                treeThread.start();
            }
        });

        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = "";

                if(nGoalPages != 0) {
                    str = strGoalDate + " 에 설정한 \n목표 입니다";

                } else {
                    progressBar.setProgress(0);
                    str = "새로운 목표를 설정해주세요";
                }

                if(nSum <= nGoalPages) {
                    if(nGoalPages == 0) {
                        str = "새로운 목표를 설정해주세요";
                    }
                }
                if(nSum > nGoalPages && nGoalPages != 0)
                    str = "목표를 달성하였습니다. 새로운 목표를 설정해주세요.";


                Toast.makeText(getContext(), str, Toast.LENGTH_LONG).show();

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("CalledSequence", "(TabTreeFunc)onResume");

        updateData();
        updateTree();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        Log.d("CalledSequence", "(TabTreeFunc)setUserVisibleHint");
        if(isVisibleToUser) {
            Log.d("CalledSequence", "(TabTreeFunc)isVisibleToUser");

        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        Log.d("CalledSequence", "(TabTreeFunc)setMenuVisibility");

        if(menuVisible) {
            Log.d("CalledSequence", "(TabTreeFunc)menuVisible");
        }
    }

    public void updateTree() {

        //현재 레벨 계산
        sub_purpose = (int) (nGoalPages / 10);
        seed_half_process = nGoalPages - (sub_purpose * 9);
//        sub_purpose=(int)(goalPage/10);
//        seed_half_process=goalPage-(sub_purpose*9);

        //currentPage <- 사용자가 읽은 양
        //goalPage <- 사용자가 설정한 목표

        if ((0 <= nSum) && (nSum <= seed_half_process + sub_purpose))
            stateIndex = 0;
        else {
            for (int i = sub_purpose * 3; i <= nGoalPages; i = i + sub_purpose) {
                if (nSum <= i) {
                    stateIndex = (i / sub_purpose) - 2;
                    break;
                }
            }
        }

        setImages(stateIndex, tree);
        seedView.setImageBitmap(images1.get(0));
    }

    public void updateData() {
        /* progressbar update */
        //reamount에서 모든 페이지수의 합  /  가장 최신의 goal (맨 위에 있는거)의 목표 페이지 수, 날짜
        Cursor cursor;

        helper = new BookListDBHelper(getContext(), // 현재 화면의 context
                "book.db",
                null, // 커서 팩토리
                1); // 버전 번호

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        cursor = db.query("goal", null, null, null, null, null, null);

        //date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (cursor.moveToNext()) {
            nGoalPages = cursor.getInt(cursor.getColumnIndex("page_amount"));
            strGoalDate = cursor.getString(cursor.getColumnIndex("date"));
            tree = cursor.getString(cursor.getColumnIndex("tree_type"));
        }

        cursor = db.query("readAmount", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            nCur = cursor.getInt(cursor.getColumnIndex("page_amount"));
            strReadDate = cursor.getString(cursor.getColumnIndex("date"));
            try {
                dateRead = sdf.parse(strReadDate);
                dateGoal = sdf.parse(strGoalDate);

                if ((dateRead.getTime() - dateGoal.getTime()) > 0) {
                    nSum = nSum + nCur;
                }

            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }

        if(nSum < 0) {
            tvPurposeState.setText( "0 페이지 / "+
                    Integer.toString(nGoalPages)+ " 페이지");
        } else {
            tvPurposeState.setText( Integer.toString(nSum) +" 페이지 / "+
                    Integer.toString(nGoalPages)+ " 페이지");
        }


        if(nGoalPages != 0) {
            progressBar.setProgress((nSum*progressBar.getMax()/nGoalPages));

        } else {
            progressBar.setProgress(0);
            tvPurposeState.setText("목표가 없어요. 새로운 목표를 설정해주세요.");

        }

        if(nSum >= nGoalPages ) {

            if(nSum == 0 && nGoalPages == 0) {
                progressBar.setProgress(0);
                tvPurposeState.setText("목표가 없어요. 새로운 목표를 설정해주세요.");
            } else {
                progressBar.setProgress(progressBar.getMax());
                tvPurposeState.setText("목표를 달성하였습니다. 새로운 목표를 설정해주세요.");
            }
        }
    }

    public Bitmap reduceImg(int idDrawable) {
        BitmapDrawable drawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), idDrawable);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 4;
        Bitmap src = drawable.getBitmap();
        Bitmap resized = Bitmap.createScaledBitmap(src, widthSmallTree, heightSmallTree, true);
        return resized;
    }


    // 이미지 회전 함수
    public Bitmap rotateImage(Bitmap src, float degree) {

        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }





    public void setImages(int stateIndex, String tree) {

        if(!(images1.isEmpty())){
            images1.clear();
        }

        if(!(images2.isEmpty())){
            images2.clear();
        }

        if (stateIndex == 0) {


//            images1.add(R.drawable.seed1);//0레벨  --> 씨앗
            images1.add(reduceImg(R.drawable.seed1));
            images1.add(reduceImg(R.drawable.seed2));
            images1.add(reduceImg(R.drawable.seed3));
            images1.add(reduceImg(R.drawable.seed4));
            images1.add(reduceImg(R.drawable.seed5));
            images1.add(reduceImg(R.drawable.seed6));

        } else if (0 < stateIndex && stateIndex < 4) {//1,2,3레벨 --> 이파리

            if (stateIndex == 1) {
                images1.add(reduceImg(R.drawable.leaf1));
                images2.add(reduceImg(R.drawable.leaf1_1));
                images2.add(reduceImg(R.drawable.leaf1_2));
                images2.add(reduceImg(R.drawable.leaf1_3));

//                images1.add(R.drawable.leaf1);//1
//                images2.add(R.drawable.leaf1_1);//1
//                images2.add(R.drawable.leaf1_2);//1
//                images2.add(R.drawable.leaf1_3);//1

            } else if (stateIndex == 2) {
                images1.add(reduceImg(R.drawable.leaf2));
                images2.add(reduceImg(R.drawable.leaf2_1));
                images2.add(reduceImg(R.drawable.leaf2_2));
                images2.add(reduceImg(R.drawable.leaf2_3));

//                images1.add(R.drawable.leaf2);//2
//                images2.add(R.drawable.leaf2_1);//2
//                images2.add(R.drawable.leaf2_2);//2
//                images2.add(R.drawable.leaf2_3);//2

            } else {
                images1.add(reduceImg(R.drawable.leaf3));
                images2.add(reduceImg(R.drawable.leaf3_1));
                images2.add(reduceImg(R.drawable.leaf3_2));
                images2.add(reduceImg(R.drawable.leaf3_3));

//                images1.add(R.drawable.leaf3);//3
//                images2.add(R.drawable.leaf3_1);//3
//                images2.add(R.drawable.leaf3_2);//3
//                images2.add(R.drawable.leaf3_3);//3


            }
        } else {
            if (tree.equals("사과나무")) { //4,5,6,7,8 레벨 --> 나무
                switch (stateIndex) {
                    case 4: {
                        images1.add(reduceImg(R.drawable.apple_tree1));
                        images2.add(reduceImg(R.drawable.apple_tree1_1));
                        images2.add(reduceImg(R.drawable.apple_tree1_2));
                        images2.add(reduceImg(R.drawable.apple_tree1_3));

//                        images1.add(R.drawable.apple_tree1);//4
//                        images2.add(R.drawable.apple_tree1_1);
//                        images2.add(R.drawable.apple_tree1_2);
//                        images2.add(R.drawable.apple_tree1_3);
                        break;
                    }
                    case 5: {
                        images1.add(reduceImg(R.drawable.apple_tree2));
                        images2.add(reduceImg(R.drawable.apple_tree2_1));
                        images2.add(reduceImg(R.drawable.apple_tree2_2));
                        images2.add(reduceImg(R.drawable.apple_tree2_3));

//                        images1.add(R.drawable.apple_tree2);
//                        images2.add(R.drawable.apple_tree2_1);
//                        images2.add(R.drawable.apple_tree2_2);
//                        images2.add(R.drawable.apple_tree2_3);
                        break;
                    }
                    case 6: {
                        images1.add(reduceImg(R.drawable.apple_tree3));
                        images2.add(reduceImg(R.drawable.apple_tree3_1));
                        images2.add(reduceImg(R.drawable.apple_tree3_2));
                        images2.add(reduceImg(R.drawable.apple_tree3_3));

//                        images1.add(R.drawable.apple_tree3);
//                        images2.add(R.drawable.apple_tree3_1);
//                        images2.add(R.drawable.apple_tree3_2);
//                        images2.add(R.drawable.apple_tree3_3);
                        break;
                    }
                    case 7: {
                        images1.add(reduceImg(R.drawable.apple_tree4));
                        images2.add(reduceImg(R.drawable.apple_tree4_1));
                        images2.add(reduceImg(R.drawable.apple_tree4_2));
                        images2.add(reduceImg(R.drawable.apple_tree4_3));

//                        images1.add(R.drawable.apple_tree4);
//                        images2.add(R.drawable.apple_tree4_1);
//                        images2.add(R.drawable.apple_tree4_2);
//                        images2.add(R.drawable.apple_tree4_3);
                        break;
                    }
                    case 8: {
                        images1.add(reduceImg(R.drawable.apple_tree5));
                        images2.add(reduceImg(R.drawable.apple_tree5_1));
                        images2.add(reduceImg(R.drawable.apple_tree5_2));
                        images2.add(reduceImg(R.drawable.apple_tree5_3));

//                        images1.add(R.drawable.apple_tree5);
//                        images2.add(R.drawable.apple_tree5_1);
//                        images2.add(R.drawable.apple_tree5_2);
//                        images2.add(R.drawable.apple_tree5_3);
                        break;
                    }
                }
            } else if (tree.equals("오렌지나무")) {
                switch (stateIndex) {
                    case 4: {
                        images1.add(reduceImg(R.drawable.orange_tree1));
                        images2.add(reduceImg(R.drawable.orange_tree1_1));
                        images2.add(reduceImg(R.drawable.orange_tree1_2));
                        images2.add(reduceImg(R.drawable.orange_tree1_3));

//                        images1.add(R.drawable.orange_tree1);//9
//                        images2.add(R.drawable.orange_tree1_1);
//                        images2.add(R.drawable.orange_tree1_2);
//                        images2.add(R.drawable.orange_tree1_3);
                        break;
                    }
                    case 5: {
                        images1.add(reduceImg(R.drawable.orange_tree2));
                        images2.add(reduceImg(R.drawable.orange_tree2_1));
                        images2.add(reduceImg(R.drawable.orange_tree2_2));
                        images2.add(reduceImg(R.drawable.orange_tree2_3));

//                        images1.add(R.drawable.orange_tree2);
//                        images2.add(R.drawable.orange_tree2_1);
//                        images2.add(R.drawable.orange_tree2_2);
//                        images2.add(R.drawable.orange_tree2_3);
                        break;
                    }
                    case 6: {
                        images1.add(reduceImg(R.drawable.orange_tree3));
                        images2.add(reduceImg(R.drawable.orange_tree3_1));
                        images2.add(reduceImg(R.drawable.orange_tree3_2));
                        images2.add(reduceImg(R.drawable.orange_tree3_3));

                        break;
                    }
                    case 7: {
                        images1.add(reduceImg(R.drawable.orange_tree4));
                        images2.add(reduceImg(R.drawable.orange_tree4_1));
                        images2.add(reduceImg(R.drawable.orange_tree4_2));
                        images2.add(reduceImg(R.drawable.orange_tree4_3));

                        break;
                    }
                    case 8: {
                        images1.add(reduceImg(R.drawable.orange_tree5));
                        images2.add(reduceImg(R.drawable.orange_tree5_1));
                        images2.add(reduceImg(R.drawable.orange_tree5_2));
                        images2.add(reduceImg(R.drawable.orange_tree5_3));

                        break;
                    }
                }
            } else if (tree.equals("포도나무")) {
                switch (stateIndex) {
                    case 4: {
                        images1.add(reduceImg(R.drawable.grape_tree1));
                        images2.add(reduceImg(R.drawable.grape_tree1_1));
                        images2.add(reduceImg(R.drawable.grape_tree1_2));
                        images2.add(reduceImg(R.drawable.grape_tree1_3));

                        break;
                    }
                    case 5: {
                        images1.add(reduceImg(R.drawable.grape_tree2));
                        images2.add(reduceImg(R.drawable.grape_tree2_1));
                        images2.add(reduceImg(R.drawable.grape_tree2_2));
                        images2.add(reduceImg(R.drawable.grape_tree2_3));

                        break;
                    }
                    case 6: {
                        images1.add(reduceImg(R.drawable.grape_tree3));
                        images2.add(reduceImg(R.drawable.grape_tree3_1));
                        images2.add(reduceImg(R.drawable.grape_tree3_2));
                        images2.add(reduceImg(R.drawable.grape_tree3_3));

                        break;
                    }
                    case 7: {
                        images1.add(reduceImg(R.drawable.grape_tree4));
                        images2.add(reduceImg(R.drawable.grape_tree4_1));
                        images2.add(reduceImg(R.drawable.grape_tree4_2));
                        images2.add(reduceImg(R.drawable.grape_tree4_3));

                        break;
                    }
                    case 8: {
                        images1.add(reduceImg(R.drawable.grape_tree5));
                        images2.add(reduceImg(R.drawable.grape_tree5_1));
                        images2.add(reduceImg(R.drawable.grape_tree5_2));
                        images2.add(reduceImg(R.drawable.grape_tree5_3));

                        break;
                    }
                }
            } else if (tree.equals("기본나무")) {
                switch (stateIndex) {
                    case 4: {
                        images1.add(reduceImg(R.drawable.tree1));
                        images2.add(reduceImg(R.drawable.tree1_1));
                        images2.add(reduceImg(R.drawable.tree1_2));
                        images2.add(reduceImg(R.drawable.tree1_3));

                        break;
                    }
                    case 5: {
                        images1.add(reduceImg(R.drawable.tree2));
                        images2.add(reduceImg(R.drawable.tree2_1));
                        images2.add(reduceImg(R.drawable.tree2_2));
                        images2.add(reduceImg(R.drawable.tree2_3));

                        break;
                    }
                    case 6: {
                        images1.add(reduceImg(R.drawable.tree3));
                        images2.add(reduceImg(R.drawable.tree3_1));
                        images2.add(reduceImg(R.drawable.tree3_2));
                        images2.add(reduceImg(R.drawable.tree3_3));

                        break;
                    }
                    case 7: {
                        images1.add(reduceImg(R.drawable.tree4));
                        images2.add(reduceImg(R.drawable.tree4_1));
                        images2.add(reduceImg(R.drawable.tree4_2));
                        images2.add(reduceImg(R.drawable.tree4_3));

                        break;
                    }
                    case 8: {
                        images1.add(reduceImg(R.drawable.tree5));
                        images2.add(reduceImg(R.drawable.tree5_1));
                        images2.add(reduceImg(R.drawable.tree5_2));
                        images2.add(reduceImg(R.drawable.tree5_3));

                        break;
                    }
                }
            } else if (tree.equals("단풍나무")) {
                switch (stateIndex) {
                    case 4: {
                        images1.add(reduceImg(R.drawable.maple_tree1));
                        images2.add(reduceImg(R.drawable.maple_tree1_1));
                        images2.add(reduceImg(R.drawable.maple_tree1_2));
                        images2.add(reduceImg(R.drawable.maple_tree1_3));

                        break;
                    }
                    case 5: {
                        images1.add(reduceImg(R.drawable.maple_tree2));
                        images2.add(reduceImg(R.drawable.maple_tree2_1));
                        images2.add(reduceImg(R.drawable.maple_tree2_2));
                        images2.add(reduceImg(R.drawable.maple_tree2_3));

                        break;
                    }
                    case 6: {
                        images1.add(reduceImg(R.drawable.maple_tree3));
                        images2.add(reduceImg(R.drawable.maple_tree3_1));
                        images2.add(reduceImg(R.drawable.maple_tree3_2));
                        images2.add(reduceImg(R.drawable.maple_tree3_3));

                        break;
                    }
                    case 7: {
                        images1.add(reduceImg(R.drawable.maple_tree4));
                        images2.add(reduceImg(R.drawable.maple_tree4_1));
                        images2.add(reduceImg(R.drawable.maple_tree4_2));
                        images2.add(reduceImg(R.drawable.maple_tree4_3));

                        break;
                    }
                    case 8: {
                        images1.add(reduceImg(R.drawable.maple_tree5));
                        images2.add(reduceImg(R.drawable.maple_tree5_1));
                        images2.add(reduceImg(R.drawable.maple_tree5_2));
                        images2.add(reduceImg(R.drawable.maple_tree5_3));

                        break;
                    }
                }

            }
        }
    }


    class MyThread extends Thread {
        int stateIndex;
        int getIndex = 0;


        public MyThread(int state) {

            this.stateIndex = state;

        }

        public void run() {


            if (stateIndex == 0) {//씨앗일때
                for (int i = 0; i < 3; i++) {
                    for (getIndex = 0; getIndex < 6; getIndex++) {


                        handler.post(new Runnable() {
                            public void run() {
                                // seedView.setImageResource(images1.get(getIndex));
                                try {
                                    seedView.setImageBitmap(images1.get(getIndex));
                                }catch(Exception e ){
                                    }

                            }

                        });
                        try {
                            int sleepTime = 500;
                            Thread.sleep(sleepTime);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {//그 외 일때
                //인덱스 따져서 구현

                for (int i = 0; i < 3; i++) {
                    for (getIndex = 0; getIndex < images2.size(); getIndex++) {//나무 애니메이션
                        handler.post(new Runnable() {
                            public void run() {
                                seedView.setImageBitmap(images2.get(getIndex));
                            }
                        });
                        try {
                            int sleepTime = 500;
                            Thread.sleep(sleepTime);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }

            seedView.setClickable(true);
        }
    }
    public void onPause() {

        super.onPause();
        System.gc();
    }
}