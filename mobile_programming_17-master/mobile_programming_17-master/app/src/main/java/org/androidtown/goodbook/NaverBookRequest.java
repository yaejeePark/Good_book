package org.androidtown.goodbook;

import com.begentgroup.xmlparser.XMLParser;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;



/**
 * Created by 지영 on 2017-05-04.
 */

public class NaverBookRequest extends NetworkRequest<NaverBooks> {

    String keyword;
    int start, display;

    public NaverBookRequest(String keyword) {
        this(keyword, 1, 10);
    }

    public NaverBookRequest(String keyword, int start) {
        this(keyword, start, 10);
    }

    public NaverBookRequest(String keyword, int start, int display) {

        try {
            this.keyword = URLEncoder.encode(keyword,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        this.start =start;
        this.display = display;
    }


    private static final String URL_FORMAT = "https://openapi.naver.com/v1/search/book.xml?target=book&query=%s&start=1&display=10";
    @Override
    public URL getURL() throws MalformedURLException {
        String url = String.format(URL_FORMAT, keyword, start, display);
        return new URL(url);
    }

    @Override
    public void setRequestHeader(HttpURLConnection conn) {
        super.setRequestHeader(conn);
        conn.setRequestProperty("X-Naver-Client-Id", "HEnFs7Bh9TKDBtIsuLdo");
        conn.setRequestProperty("X-Naver-Client-Secret", "1Z_p2tQdf1");
    }

    @Override
    protected NaverBooks parse(InputStream is) throws ParseException {
        XMLParser parser = new XMLParser();
        NaverBooks books = parser.fromXml(is, "channel", NaverBooks.class);
        return books;
    }
}

