package org.androidtown.goodbook;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

/**
 * Created by 지영 on 2017-05-04.
 */

public class ImageRequest extends NetworkRequest<Bitmap>{
    String url;
    public ImageRequest(String url) {
        this.url = url;
    }
    @Override
    public URL getURL() throws MalformedURLException {
        return new URL(url);
    }

    @Override
    protected Bitmap parse(InputStream is) throws ParseException {
        Bitmap bm = BitmapFactory.decodeStream(is);
        return bm;
    }
}
