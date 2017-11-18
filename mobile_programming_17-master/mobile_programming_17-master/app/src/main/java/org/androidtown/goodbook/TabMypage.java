package org.androidtown.goodbook;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by sang on 2017-04-08.
 */

public class TabMypage extends Fragment {

    private MainActivity.SectionsPagerAdapter mSectionsPagerAdapter;
    Button b1;
    Button b2;

    View v1;
    View v2;

    Fragment childFragment2;
    Fragment childFragment1;


    public static TabMypage newInstance() {
        TabMypage myFragment = new TabMypage();

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View mypageView=inflater.inflate(R.layout.tab_mypage, container, false);

        b1=(Button)mypageView.findViewById(R.id.read);
        b2=(Button)mypageView.findViewById(R.id.anal);

        v1=(View)mypageView.findViewById(R.id.v1);
        v2=(View)mypageView.findViewById(R.id.v2);

        //처음부터 분석
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.mypage_fragment_container, childFragment1).commit();
        b2.setTextColor(Color.DKGRAY);
        b1.setTextColor(Color.LTGRAY);

        v1.setBackgroundColor(Color.argb(80,240,192,192));
        v2.setBackgroundColor(Color.LTGRAY);

        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                b1.setTextColor(Color.DKGRAY);
                b2.setTextColor(Color.LTGRAY);

                v2.setBackgroundColor(Color.argb(80,240,192,192));
                v1.setBackgroundColor(Color.LTGRAY);
                //읽은 책 프래그먼트 보여주기
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.mypage_fragment_container, childFragment2).commit();
            }
        });

        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                b2.setTextColor(Color.DKGRAY);
                b1.setTextColor(Color.LTGRAY);

                v1.setBackgroundColor(Color.argb(80,240,192,192));
                v2.setBackgroundColor(Color.LTGRAY);
                //분석 프래그먼트 보여주기

                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.mypage_fragment_container, childFragment1).commit();            }
        });

        return mypageView;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    //MypageAnalysis mypageAnalysis = new MypageAnalysis();
                    //return mypageAnalysis;
                case 1:
                    //   MypageReadBook readBookList = new MypageReadBook();
                    // return readBookList;

                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "analysis";
                case 1:
                    return "read book list";
            }
            return null;
        }
    }
    public void onPause() {
        super.onPause();
        System.gc();
    }
}
