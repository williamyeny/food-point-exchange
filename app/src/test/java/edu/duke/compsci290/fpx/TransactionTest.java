package edu.duke.compsci290.fpx;
import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Serena on 4/29/18.
 */

public class TransactionTest {
        Transaction myTrans= new Transaction();
        Transaction secTrans=new Transaction("12","13",90);


        @Test
        public void originalAmount() {
                assertTrue(myTrans.getmReceiverID()=="");
        }
        @Test
        public void testDateAdded(){
                assertFalse(secTrans.getmDate()=="");

        }
        @Test
        public void noDuplicateTrans(){
                assertFalse(myTrans==secTrans);
        }

}
