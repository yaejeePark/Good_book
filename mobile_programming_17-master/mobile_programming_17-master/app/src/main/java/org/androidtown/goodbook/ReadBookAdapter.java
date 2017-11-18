package org.androidtown.goodbook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sang on 2017-06-07.
 */

public class ReadBookAdapter extends BaseAdapter {

    //    / Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    //CustomMemoListFragment 커스텀 레이아웃
    private ArrayList<ReadBookItem> listViewItemList = new ArrayList<ReadBookItem>();
    Context mContext;

    // ListViewAdapter의 생성자
    public ReadBookAdapter(Context context) {
        mContext = context;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

//        Log.i("memoadapter>>", "getView");

        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_mypage_read_bok, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView imgPic = (ImageView) convertView.findViewById(R.id.img_book);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_bookName);
        TextView tvAuthor = (TextView) convertView.findViewById(R.id.tv_author);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ReadBookItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        imgPic.setImageBitmap(listViewItem.getImg());
        tvTitle.setText(listViewItem.getBookName());
        tvAuthor.setText(listViewItem.getAuthor());
        tvDate.setText(listViewItem.getDate());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }


//                    adapter.addItem(nTimes,strTitle,strAuthor,strDate,b_imageLink);

    public void addItem(int nTimes, String title, String author, String date, byte[] imgLink) {

        ReadBookItem item = new ReadBookItem();

//        Bitmap bmp = getAppIcon(b_img);
//
//        item.setImg(bmp);
        item.setBookName(title);
        item.setAuthor(author);
        item.setDate(date);
        item.setImg(getAppIcon(imgLink));
        item.setTimes(nTimes);

        listViewItemList.add(item);
    }

    public void clear() {
        listViewItemList.clear();
    }

    //Work: byte array -> Bitmap
    public Bitmap getAppIcon(byte[] b) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        Bitmap.createScaledBitmap(bitmap, 200, 100, false);
        return bitmap;
    }

}
