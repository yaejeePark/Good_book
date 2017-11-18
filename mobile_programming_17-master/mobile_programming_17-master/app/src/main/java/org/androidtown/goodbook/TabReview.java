package org.androidtown.goodbook;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by sang on 2017-04-08.
 */

public class TabReview extends Fragment {

    Button b1;
    Button b2;
    View v1;
    View v2;

    MemoAdapter adapter;
    ListView listview ;
    Fragment childFragment1;
    Fragment childFragment2;


    public static TabReview newInstance() {
        TabReview myFragment = new TabReview();

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View reportView = inflater.inflate(R.layout.tab_review, container, false);

        b1 = (Button) reportView.findViewById(R.id.report);
        b2 = (Button) reportView.findViewById(R.id.photo);

        v1=(View)reportView.findViewById(R.id.v1);
        v2=(View)reportView.findViewById(R.id.v2);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.child_fragment_container, childFragment1).commit();
        b1.setTextColor(Color.DKGRAY);
        b2.setTextColor(Color.LTGRAY);

        v2.setBackgroundColor(Color.LTGRAY);
        v1.setBackgroundColor(Color.argb(80,240,192,192));
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b1.setTextColor(Color.DKGRAY);
                b2.setTextColor(Color.LTGRAY);

                v2.setBackgroundColor(Color.LTGRAY);
                v1.setBackgroundColor(Color.argb(80,240,192,192));
                //메모 프래그먼트 보여주기
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.child_fragment_container, childFragment1).commit();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b2.setTextColor(Color.DKGRAY);
                b1.setTextColor(Color.LTGRAY);

                v1.setBackgroundColor(Color.LTGRAY);
                v2.setBackgroundColor(Color.argb(80,240,192,192));
                //사진 프래그먼트 보여주기
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.child_fragment_container, childFragment2).commit();
            }
        });

        return reportView;

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
    public void onPause() {

        super.onPause();
        System.gc();
    }

}