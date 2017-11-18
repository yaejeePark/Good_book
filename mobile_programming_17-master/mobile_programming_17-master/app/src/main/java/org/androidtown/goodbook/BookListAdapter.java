package org.androidtown.goodbook;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 지영 on 2017-04-09.
 */

public class BookListAdapter extends BaseAdapter {
    List<BookItem> items = new ArrayList<BookItem>();

    public void addAll(List<BookItem> items) {
        if(!(this.items.isEmpty()))
            removeAll();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void removeAll(){

        int j=0;
        for(int i=0;!(this.items.isEmpty());i++)
            this.items.remove(j);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemView view;

        checkTitle(items.get(position));

        if(convertView == null){
            view = new ItemView(parent.getContext());
        }else {
            view = (ItemView)convertView;
        }
        view.setBookItem(items.get(position), position);

        getImageList.setName(items.get(position),position);

        return view;
    }

    public void checkTitle(BookItem item){

        item.title=item.title.replace("</b>","");
        item.title=item.title.replace("<b>","");
        item.author=item.author.replace("</b>","");
        item.author=item.author.replace("<b>","");

    }

    //DB ->
    public String getTitle(int position) {
        return items.get(position).getTitle();
    }
    public String getImage(int position) {

        return items.get(position).getImage();
    }
    public String getAuthor(int position) {

        return items.get(position).getAuthor();
    }
    //<-
}


