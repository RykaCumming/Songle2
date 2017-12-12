package uk.ac.ed.inf.songle2;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by s1540547 on 10/12/17.
 */
public class ScrollingActivityTest {
    ScrollingActivity s = new ScrollingActivity();
//    s = Robolectric.setupActivity(ScrollingActivity.class);
    @Test
    public void spaces() throws Exception {
        String correct_result = "*** **** ***** ***** * ";
 //       assertTrue(correct_result,s.spaces("red blue green black p "));
    }

}