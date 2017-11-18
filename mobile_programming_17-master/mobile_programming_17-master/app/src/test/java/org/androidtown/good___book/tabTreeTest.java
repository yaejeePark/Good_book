package org.androidtown.good___book;
import junit.framework.TestCase;

import org.androidtown.goodbook.TabTree;
import org.junit.Test;

public class tabTreeTest extends TestCase {

    private TabTree tabTreeTest = new TabTree();

    @Test
    public void testUpdateTree() throws Exception{
        int level = tabTreeTest.updateTree(10000,200);
        assertEquals(level,0);
    }
}