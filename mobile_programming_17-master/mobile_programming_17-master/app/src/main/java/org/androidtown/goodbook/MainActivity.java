package org.androidtown.goodbook;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private Animation fab_open, fab_close;

    Uri mImage;
    String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    Button btnHeart;

    /* DB(report) */
    SQLiteDatabase db;
    BookListDBHelper helper;

    TextView nameView;
    EditText pageView;
    String strGoal;
    int nGoal;

    Fragment readBookFragment;
    Fragment analysisFragment;
    Fragment photoFragment;
    Fragment reportFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

// Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        /* Tutorial */


        //DB->
        helper = new BookListDBHelper(this, // 현재 화면의 context
                "book.db",
                null, // 커서 팩토리
                1); // 버전 번호

        //tutorial table 검사
        int nCount = 0;
        db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM tutorial", null);
        c.moveToFirst();

        while (c.moveToNext()) {
            SharedPreferences preference = getSharedPreferences("first", MODE_PRIVATE);//"원래는 first"
            int firstviewshow = preference.getInt("first", 0);//만약 이게 0이라면

            nCount = nCount + 1;
            Log.d("nCount", Integer.toString(nCount));
        }
        Log.d("nCount", Integer.toString(nCount));

        if (nCount == 0) {
            db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("pass", "yes");
            db.insert("tutorial", null, values);
            mSectionsPagerAdapter.notifyDataSetChanged();
            mViewPager.setAdapter(mSectionsPagerAdapter);

            startActivity(new Intent(this, Tutorial.class));
        }


        startActivity(new Intent(this, SplashActivity.class));


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        btnHeart = (Button) findViewById(R.id.btnHeart);


        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);


        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivityFunc", "mViewPager");
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                animateFAB();

            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                animateFAB();
                Intent i = new Intent(getApplicationContext(), BookListActivity.class);
                startActivity(i);

            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                animateFAB();
                Intent i = new Intent(getApplicationContext(), WriteReport.class);
                startActivity(i);
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                animateFAB();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivity(intent);
            }
        });
        fab4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                animateFAB();
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                Cursor cursor;
                db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
                cursor = db.query("goal", null, null, null, null, null, null);
                int nGoalPages = 0;
                int nSum = 0;
                String strGoalDate = "";
                int nCur;
                String strReadDate;
                Date dateRead;
                Date dateGoal;

                //date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                while (cursor.moveToNext()) {
                    nGoalPages = cursor.getInt(cursor.getColumnIndex("page_amount"));
                }

                db = helper.getReadableDatabase(); // db객체를 얻어온다. 읽기 전용
                cursor = db.query("goal", null, null, null, null, null, null);

                //date
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                while (cursor.moveToNext()) {
                    nGoalPages = cursor.getInt(cursor.getColumnIndex("page_amount"));
                    strGoalDate = cursor.getString(cursor.getColumnIndex("date"));
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


                if (nGoalPages > nSum && nGoalPages > 0) {
                    Toast.makeText(MainActivity.this, "목표를 달성한 후 \n새로운 목표를 지정해주세요.", Toast.LENGTH_SHORT).show();


                } else {
                    final View dialogView = inflater.inflate(R.layout.set_purpose, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);     // 여기서 this는 Activity의 this
                    builder.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)
                    builder.setTitle("나의 목표");       // 제목 설정
                    final AlertDialog dialog = builder.create();    // 알림창 객체 생성


                    final EditText goalView = (EditText) dialogView.findViewById(R.id.numberOfPage);
                    Button b1 = (Button) dialogView.findViewById(R.id.b1);
                    Button b2 = (Button) dialogView.findViewById(R.id.b2);


                    final Spinner spTreeType = (Spinner) dialogView.findViewById(R.id.goalSpinner);
                    ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(MainActivity.this, R.array.tree_types, R.layout.spinner_style);
                    spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_style);
                    spTreeType.setAdapter(spinnerAdapter);


                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String page = goalView.getText().toString();

                            if (page.isEmpty())
                                Toast.makeText(getApplicationContext(), "목표를 입력해 주세요", Toast.LENGTH_SHORT).show();
                            else {

                                OutputStream outStream = null;
                                String extStroageDirectory =
                                        Environment.getExternalStorageDirectory().toString();

                                //DB에 추가
                                db = helper.getWritableDatabase(); // db 객체를 얻어온다. 쓰기 가능
                                ContentValues values = new ContentValues();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String date = sdf.format(new Date());
                                values.put("date", date);
                                values.put("page_amount", Integer.parseInt(page));
                                values.put("tree_type", spTreeType.getSelectedItem().toString());

                                Log.d("DB Goal>>", date + ", " + spTreeType.getSelectedItem().toString());
                                try {
                                    db.beginTransaction();
                                    db.insert("goal", null, values);
                                    db.setTransactionSuccessful();
                                } catch (SQLException e) {
                                } finally {
                                    db.endTransaction();
                                }
                                mSectionsPagerAdapter.notifyDataSetChanged();
                                mViewPager.setAdapter(mSectionsPagerAdapter);
                                dialog.dismiss();

                            }
                        }

                    });


                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Toast.makeText(getApplicationContext(), "목표 입력을 취소합니다.", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                    });
                    dialog.show();
                }

            }
        });

        btnHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSectionsPagerAdapter.notifyDataSetChanged();
                mViewPager.setAdapter(mSectionsPagerAdapter);
            }
        });

        backPressCloseHandler = new BackPressCloseHandler(this);
//        readBookFragment = new MypageReadBook();
//        analysisFragment = new MypageAnalysis();
//        photoFragment= new PhotoFragment();
//        reportFragment= new MemoFragment();

    }


    public void animateFAB() {

        if (isFabOpen) {

            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab4.startAnimation(fab_close);

            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fab4.setClickable(false);

            isFabOpen = false;

            Log.d("Raj", "close");

        } else {

            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab4.startAnimation(fab_open);

            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            fab4.setClickable(true);

            isFabOpen = true;
            fab1.setBackgroundColor(Color.TRANSPARENT);
            fab2.setBackgroundColor(Color.TRANSPARENT);
            fab3.setBackgroundColor(Color.TRANSPARENT);
            fab4.setBackgroundColor(Color.TRANSPARENT);
            Log.d("Raj", "open");

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //찍은 사진 가져오기
        if (requestCode == 1) {

            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContentResolver(), mImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //deleted placeholderfragment class from here

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
//    public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        TabTree tab1 = TabTree.newInstance();
        TabBooklist tab2 = TabBooklist.newInstance();
        TabReview tab3 = TabReview.newInstance();
        TabMypage tab4 = TabMypage.newInstance();


        public void init() {
            readBookFragment = new MypageReadBook();
            analysisFragment = new MypageAnalysis();
            photoFragment = new PhotoFragment();
            reportFragment = new MemoFragment();

            tab3.childFragment1 = reportFragment;
            tab3.childFragment2 = photoFragment;
            tab4.childFragment1 = analysisFragment;
            tab4.childFragment2 = readBookFragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Log.d("sequence", "instantiateItem");


            return super.instantiateItem(container, position);
        }


        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);

            Log.d("sequence", "restoreState");

        }

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            Log.d("sequence", "SectionsPagerAdapter");
            init();


        }

        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("sequence", "getitem");

            switch (position) {
                case 0:
//                    TabTree tabtree = fragments.get(0);
//                    TabTree tab1tree = new TabTree();
//                    return tab1tree

                    return tab1;

                case 1:
//                    TabBooklist tab2booklist = new TabBooklist();
//                    return tab2booklist;
                    return tab2;

                case 2:
//                    TabReview tab3review = new TabReview();
//                    return tab3review;
//                    tab3.childFragment1=reportFragment;
//                    tab3.childFragment2=photoFragment;
                    return tab3;

                case 3:
//                    TabMypage tab4mypage = new TabMypage();
//                    return tab4mypage;
//                    tab4.childFragment1=analysisFragment;
//                    tab4.childFragment2=readBookFragment;
                    return tab4;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {

            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Log.d("sequence", "getPageTitle");

            switch (position) {
                case 0:
                    return "tree";

                case 1:
                    return "book list";
                case 2:
                    return "report";
                case 3:
                    return "my page";

            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

}