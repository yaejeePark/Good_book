package org.androidtown.goodbook;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sang on 2017-04-08.
 */

public class TabBooklist extends Fragment {

    /* <DB> (booklist) */
    //variable use in DB->
    int nCount = 1;
    SQLiteDatabase db;
    BookListDBHelper helper;
    int int_tree_start = 0;
    BookListActivity blist = new BookListActivity();

    ArrayList<String> items;
    // ArrayAdapter 생성. 아이템 View를 선택(single choice)가능하도록 만듦.
//    ArrayAdapter<String> adapter;

    ListView listview;
    BookListShowAdapter adapter;
    //<-

    public static TabBooklist newInstance() {
        TabBooklist myFragment = new TabBooklist();

        Log.d("MainActivityFunc", "TabBooklistInitCalled");

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_booklist, container, false);

        /* <DB> */
        //get db and helper ->
        db = blist.getDb();
        helper = blist.getHelper();
        //<-

        // initialize db->
        //booklist
        helper = new BookListDBHelper(this.getContext(),
                "book.db",
                null, // 커서 팩토리
                1); // 버전 번호

        db = helper.getReadableDatabase();
        Cursor c = db.query("booklist", null, null, null, null, null, null);
        int_tree_start = c.getCount();

        /* (XML) <Book list> */
        // Adapter 생성
        adapter = new BookListShowAdapter(getContext());

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) rootView.findViewById(R.id.lv_book_list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new CustomOnClickListener());


        return rootView;
    }


    //getCount
    public int getCount() {
        return items.size();
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.clear();
        adapter.notifyDataSetChanged();

        select();

        showAmountRead();
    }


    //select
    public void select() {
        nCount = 1;
        // 1) db의 데이터를 읽어와서, 2) 결과 저장, 3)해당 데이터를 꺼내 사용

        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.query("booklist", null, null, null, null, null, null);

        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            String str_title = c.getString(c.getColumnIndex("title"));
            String str_author = c.getString(c.getColumnIndex("author"));
            byte[] b_imageLink = c.getBlob(c.getColumnIndex("image_link"));
            int n_pageCur = c.getInt(c.getColumnIndex("page_cur"));
            int n_pageTotal = c.getInt(c.getColumnIndex("page_total"));

            //add
            adapter.addItem(b_imageLink, str_title, str_author, n_pageCur, n_pageTotal);

            adapter.notifyDataSetChanged();

            nCount++;
        }
    }

    private class CustomOnClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String str = ((booklistLayoutItem) adapter.getItem(i)).getBookName();

            showPopup(getView(), str);


        }

    }

    public void showPopup(View anchorView, final String strTitle) {
        final View popupView = getActivity().getLayoutInflater().inflate(R.layout.pop_up_booklist_eachbook, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                RelativeLayout.LayoutParams.WRAP_CONTENT, ViewPager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, -100);

        /* 선언 */
        final EditText etTimes, etCurPages, etTotPages;
        final EditText etDelete;
        TextView tvGenreShow;
        final Spinner spGenreList;
        final ArrayList<String> strListGenre = new ArrayList<String>();
        Button btnSave;
        final String strGetTitle = strTitle;

        final SQLiteDatabase db2_read;
        final SQLiteDatabase db2_write;
        final BookListDBHelper helper2;

        final int curPageFromDB;
        final int nTimesFromDB;

        //횟수, 페이지 수, 이전에 설정햇던 장르
        int nTimes, nCurPages, nTotPages;
        final String originStrGenre;

        /* 대입 */
        etTimes = (EditText) popupView.findViewById(R.id.etTimes); //읽은 횟수
        etCurPages = (EditText) popupView.findViewById(R.id.etCurPages); //읽은 페이지 수
        etTotPages = (EditText) popupView.findViewById(R.id.etTotPages); //토탈 페이지 수
        spGenreList = (Spinner) popupView.findViewById(R.id.spGenreList); //장르 리스트
        tvGenreShow = (TextView) popupView.findViewById(R.id.tvGenreShow); //이전에 설정했던 장르 보여줌
        etDelete = (EditText) popupView.findViewById(R.id.etDelete); //'삭제' 입력 edit text
        btnSave = (Button) popupView.findViewById(R.id.btnSave2); //저장 버튼

        helper2 = new BookListDBHelper(this.getContext(),
                "book.db",
                null, // 커서 팩토리
                1); // 버전 번호

        db2_write = helper2.getWritableDatabase();
        db2_read = helper2.getReadableDatabase();

        /* spGenreList 세팅 */
        Cursor c2 = db2_read.query("genreCount", null, null, null, null, null, null);
        String strGenreRead;

        while (c2.moveToNext()) {
            strGenreRead = c2.getString(c2.getColumnIndex("genre"));
            strListGenre.add(strGenreRead);
        }


        ArrayAdapter<String> adpGenre = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, strListGenre);
        adpGenre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenreList.setAdapter(adpGenre);



        /* 팝업창에 db에서 데이터 갖고와서 보여주기 */
        //책 이름으로 찾는다
        c2 = db2_read.rawQuery("SELECT * FROM booklist WHERE title=?", new String[]{strTitle + ""});
        c2.moveToFirst();


        nTimes = c2.getInt(c2.getColumnIndex("read_times"));
        nCurPages = c2.getInt(c2.getColumnIndex("page_cur"));
        nTotPages = c2.getInt(c2.getColumnIndex("page_total"));
        curPageFromDB = nCurPages; //page_cur을 DB에서 불러옴
        nTimesFromDB = nTimes;

        originStrGenre = c2.getString(c2.getColumnIndex("genre"));

        //여기
        spGenreList.setSelection(getIndex(spGenreList, originStrGenre));

        Log.i("TabBooklist>>", nCurPages + ", " + originStrGenre);

        etTimes.setText(Integer.toString(nTimes));
        etTotPages.setText(Integer.toString(nTotPages));
        etCurPages.setText(Integer.toString(nCurPages));
        tvGenreShow.setText(originStrGenre);

        /* 변경 사항 저장 */
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etDelete.getText().toString().isEmpty()) {
                    //update
                    Cursor c2 = db2_write.rawQuery("SELECT * FROM booklist WHERE title=?", new String[]{strGetTitle + ""});
                    c2.moveToFirst();

                    //횟수, 페이지 수, 장르(스피너에서 받음) 업데이트
                    int nTimes, nCurPages, nTotPages;
                    String strGenre;
                    nTimes = Integer.parseInt(etTimes.getText().toString());
                    nCurPages = Integer.parseInt(etCurPages.getText().toString());
                    nTotPages = Integer.parseInt(etTotPages.getText().toString());
                    strGenre = spGenreList.getSelectedItem().toString();

                    Date cDate = new Date();
                    String strDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);

                    int exceedTime = 1; //time 초과할 때

                    //DB(booklist)에 업데이트
                    ContentValues values2 = new ContentValues();

                    values2.put("genre", strGenre); //장르

                    int curPageFromEt = nCurPages; //editText에서 가져옴
                    int curPageSave = curPageFromEt - curPageFromDB;
                    boolean isChanged = false;

                    if (nTimes != nTimesFromDB) {
                        isChanged = true;
                    }

                    if (nCurPages >= nTotPages) {//여기 원래 ==이거 임
                        values2.put("read_times", nTimes + 1);   //횟수
                        exceedTime++;
                        values2.put("page_cur", 0);   //지금 페이지 수
                        values2.put("date_finish", strDate);
                    } else if (nCurPages == nTotPages) {//여기가 원래 >==이거였음.
                        values2.put("read_times", nTimes + 1);   //횟수
                        exceedTime++;
                        values2.put("page_cur", 0);   //지금 페이지 수
                        values2.put("date_finish", strDate);
                        curPageSave = 0;
                    } else if (nCurPages < nTotPages) { //nCurPages < nTotPages
                        values2.put("read_times", nTimes);   //횟수
                        values2.put("page_cur", nCurPages);   //지금 페이지 수
                    }
                    values2.put("page_total", nTotPages); //전체 페이지 수

                    if (isChanged)
                        values2.put("read_times", nTimes);   //횟수


                    db2_write.update("booklist", values2, "title=?", new String[]{strGetTitle});

                    //DB(genreCount) 수정
                    //genre table 하나 줄이기

                    if (!originStrGenre.equalsIgnoreCase(strGenre)) {
                        //장르가 바뀌었다면
                        //이전 장르는 하나 감소
                        c2 = db2_write.rawQuery("SELECT * FROM genreCount WHERE genre=?", new String[]{originStrGenre + ""});
                        c2.moveToFirst();

                        int n = c2.getInt(c2.getColumnIndex("count"));
                        n = n - 1;

                        ContentValues values1 = new ContentValues();
                        values1.put("count", n);

                        db2_write.update("genreCount", values1, "genre=?", new String[]{originStrGenre});

                        //새로운 장르는 추가
                        c2 = db2_write.rawQuery("SELECT * FROM genreCount WHERE genre=?", new String[]{strGenre + ""});
                        c2.moveToFirst();

                        n = c2.getInt(c2.getColumnIndex("count"));
                        n = n + 1;

                        values1 = new ContentValues();
                        values1.put("count", n);

                        db2_write.update("genreCount", values1, "genre=?", new String[]{strGenre});
                    } else {

                    }

                    //DB(readAmount)에 추가
//                    int curPageFromEt = nCurPages; //editText에서 가져옴
//                    curPageSave = curPageFromEt - curPageFromDB;

                    Log.d("PageCount>>", "DB: " + Integer.toString(curPageFromDB) + "- EditText: " + Integer.toString(curPageFromEt));

                    //date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = sdf.format(new Date());

                    values2 = new ContentValues();
                    values2.put("title", strGetTitle);
                    values2.put("genre", strGenre);
                    values2.put("date", date);
                    values2.put("page_amount", curPageSave);//*exceedTime
                    try {
                        db2_write.beginTransaction();
                        db2_write.insert("readAmount", null, values2);
                        db2_write.setTransactionSuccessful();
                    } catch (SQLException e) {
                    } finally {
                        db2_write.endTransaction();
                    }

//여기
                    Toast.makeText(getContext(), "내용이 변경되었습니다", Toast.LENGTH_SHORT).show();
                    adapter.clear();
                    select();
                    adapter.notifyDataSetChanged();

                    popupWindow.dismiss();
                    //창닫기
                } else {
                    if (etDelete.getText().toString().equals("삭제")) {
                        //booklist table 삭제하기
                        db2_write.delete("booklist", "title=?", new String[]{strGetTitle});

                        Toast.makeText(getContext(), "삭제되었습니다", Toast.LENGTH_SHORT).show();

                        //genre table 하나 줄이기
                        Cursor c2 = db2_write.rawQuery("SELECT * FROM genreCount WHERE genre=?", new String[]{originStrGenre + ""});
                        c2.moveToFirst();

                        int n = c2.getInt(c2.getColumnIndex("count"));
                        n = n - 1;

                        ContentValues values1 = new ContentValues();
                        values1.put("count", n);

                        db2_write.update("genreCount", values1, "genre=?", new String[]{originStrGenre});
                        //listview update
                        adapter.clear();
                        select();
                        adapter.notifyDataSetChanged();

                        popupWindow.dismiss();
                        //창닫기

                    } else {
                        Toast.makeText(getContext(), "잘못입력하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }


    private int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    public void showAmountRead() {
        db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
        Cursor c = db.query("readAmount", null, null, null, null, null, null);

//        "title text," +
//                "genre text," +
//                "date text," +
//                "page_amount integer" +

        while (c.moveToNext()) {
            // c의 int가져와라 ( c의 컬럼 중 id) 인 것의 형태이다.
            String str_title = c.getString(c.getColumnIndex("title"));
            String str_genre = c.getString(c.getColumnIndex("genre"));
            String str_date = c.getString(c.getColumnIndex("date"));
            int int_pageAmount = c.getInt(c.getColumnIndex("page_amount"));

            Log.d("AmountRead>>", str_title + ", " + str_genre + ", " + str_date + ", " + Integer.toString(int_pageAmount));
        }
    }

    public void onPause() {

        super.onPause();
        System.gc();
    }
}
