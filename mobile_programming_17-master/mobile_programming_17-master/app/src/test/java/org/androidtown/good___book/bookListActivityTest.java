package org.androidtown.good___book;
import android.graphics.Bitmap;

import junit.framework.TestCase;

import org.androidtown.goodbook.BookListActivity;
import org.junit.Test;

public class bookListActivityTest extends TestCase {

    private BookListActivity bookListActivityTest = new BookListActivity();
    Bitmap savebit;
    @Test
    public void testGetGenre() throws Exception{
        String genre = bookListActivityTest.getGenre("인문");
        assertEquals(genre,"인문");
    }



}