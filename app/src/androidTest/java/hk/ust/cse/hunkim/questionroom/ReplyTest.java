package hk.ust.cse.hunkim.questionroom;

import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import hk.ust.cse.hunkim.questionroom.question.Reply;

/**
 * Created by DangerHen on 15/11/2.
 */
public class ReplyTest extends TestCase {
    Reply r;


    protected void setUp() throws Exception {
        super.setUp();
        r = new Reply("Hello? This is very nice");
    }

    @SmallTest
    public void testGetcontent(){
        assertEquals(r.getContent(),"Hello? This is very nice");
    }

    @SmallTest
    public void testGetTimestamp(){
        assertNotNull(r.getTimestamp());
    }

}
