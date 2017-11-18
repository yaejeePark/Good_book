package org.androidtown.good___book;
import junit.framework.TestCase;

import org.androidtown.goodbook.MypageAnalysis;
import org.junit.Test;

public class myPageAnalysisTest extends TestCase {

    private MypageAnalysis mypageAnalysisTest = new MypageAnalysis();

    @Test
    public void testGetSentence() throws Exception{
        String str = mypageAnalysisTest.getNicName(3,4);
        assertEquals(str,"건강한 사랑꾼");
    }

    public void testGetIdxOfSecondValues() throws Exception {
        int indexOfSecondValues = mypageAnalysisTest.getIdxOfSecondValues();
        assertNotNull(indexOfSecondValues);
    }

}