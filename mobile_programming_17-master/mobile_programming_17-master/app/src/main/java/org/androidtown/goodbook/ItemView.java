package org.androidtown.goodbook;

import android.content.Context;
import android.text.Html;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by 지영 on 2017-05-04.
 */

public class ItemView extends FrameLayout {
    public ItemView(Context context) {
        super(context);
        init();
    }

    URLImageView iconView;
    TextView titleView, authorView,d_catgView;
    private void init(){
        inflate(getContext(), R.layout.view_item, this);
        iconView = (URLImageView)findViewById(R.id.image_icon);
        titleView = (TextView)findViewById(R.id.text_title);
        authorView = (TextView)findViewById(R.id.text_author);
        d_catgView = (TextView)findViewById(R.id.text_d_catg);

    }

    BookItem item;
    //    ImageRequest mRequest;
    //
    public void setBookItem(BookItem item, int position) {
        this.item = item;
        titleView.setText(Html.fromHtml(item.title));
        authorView.setText(item.author);
        d_catgView.setText(item.d_catg);

        // iconView item.image....
        iconView.setImageURL(item.image, position);

//        getImageList.setName(item.title);
    }
}

