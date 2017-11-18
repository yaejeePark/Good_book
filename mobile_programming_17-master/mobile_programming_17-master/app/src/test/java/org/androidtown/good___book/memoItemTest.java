package org.androidtown.good___book;


import org.androidtown.goodbook.MemoItem;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by user1 on 2017-06-09.
 */

public class memoItemTest {

    private MemoItem memoItemTest = new MemoItem();

    @Test
    public void testGetTitle() throws Exception{
        String title = memoItemTest.getTitle();
        assertEquals(title,"책은 도끼다");
    }

    @Test
    public void testGetID() throws Exception{
        int id = memoItemTest.getID();
        assertEquals(id,3);
    }

    @Test
    public void testGetContext() throws Exception{
        String context = memoItemTest.getContext();
        assertNotNull(context);
    }

}
