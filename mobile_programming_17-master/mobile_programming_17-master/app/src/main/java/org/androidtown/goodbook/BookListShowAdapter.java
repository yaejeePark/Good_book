package org.androidtown.goodbook;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sang on 2017-05-09.
 */

public class BookListShowAdapter extends BaseAdapter {
    Context mContext;
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<booklistLayoutItem> listViewItemList = new ArrayList<booklistLayoutItem>() ;


    public BookListShowAdapter(Context context) {
        mContext = context;
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.booklist_layout, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView img_book = (ImageView) convertView.findViewById(R.id.img_book) ;
        TextView tv_bookName = (TextView) convertView.findViewById(R.id.tv_bookName) ;
        TextView tv_author = (TextView) convertView.findViewById(R.id.tv_author) ;
        ProgressBar prog_read = (ProgressBar) convertView.findViewById(R.id.prog_read);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        booklistLayoutItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
//        img_book.setImageDrawable(listViewItem.getImg());
        img_book.setImageBitmap(listViewItem.getImg());
        tv_bookName.setText(listViewItem.getBookName());
        tv_author.setText(listViewItem.getAuthor());
        prog_read.setProgress(listViewItem.getProg());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(byte[] b_img, String title, String author, int cur, int tot) {
//    public void addItem(Drawable bmp, String title, String author, int cur, int tot) {
        booklistLayoutItem item = new booklistLayoutItem();

        Bitmap bmp = getAppIcon(b_img);

        item.setImg(bmp);
        item.setBookName(title);
        item.setAuthor(author);
        item.setProg(cur, tot);

        listViewItemList.add(item);
    }

    public void clear() {
        listViewItemList.clear();
    }

    //Work: byte array -> Bitmap
    public Bitmap getAppIcon(byte[] b) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        Bitmap.createScaledBitmap(bitmap, 1000, 1000, false);
        return bitmap;
    }
}
