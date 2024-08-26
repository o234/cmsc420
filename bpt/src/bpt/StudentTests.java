package bpt;
import org.junit.Test;

import static org.junit.Assert.*;

//import javax.swing.text.html.HTMLDocument.Iterator;
import java.util.Iterator;

/**
 * A jUnit test suite for {@link BinaryPatriciaTrie}.
 *
 * @author --- YOUR NAME HERE! ----.
 */
public class StudentTests {


    @Test public void testEmptyTrie() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        assertTrue("Trie should be empty",trie.isEmpty());
        assertEquals("Trie size should be 0", 0, trie.getSize());

        assertFalse("No string inserted so search should fail", trie.search("0101"));

    }

    @Test public void testFewInsertionsWithSearch() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();
                BinaryPatriciaTrie tt = new BinaryPatriciaTrie();

              //  tt.insert("100");
             //   tt.insert("101");
              //  tt.delete("100");
        /* 
        trie.insert("1010");
        trie.insert("10");
        trie.insert("1");
        trie.insert("11");
        trie.insert("11000");
        trie.insert("1100011");
        trie.insert("11000101");
        trie.insert("1111");
        trie.insert("110");
        */
       // trie.insert("001");
      //  trie.insert("00100");
       // trie.insert("111100110001");
       // trie.insert("1111000");
       // trie.insert("1101");
        
       //trie.delete("111100110001");
       // trie.insert("1");

        trie.insert("100");
      //  trie.insert("101");
       trie.insert("0");
       trie.insert("10111");
            trie.insert("10110");
       trie.insert("1011");

      //trie.insert("1010");
      //trie.insert("1");
      // trie.insert("111");
      // trie.insert("11");
      //trie.insert("110");
  

      //trie.delete("1011");

     
        System.out.println(trie.getSize());
       System.out.println("search: " + trie.search("1011"));
       Iterator<String> i = trie.inorderTraversal();
        trie.printTree();
      System.out.println("junkfree?: " + trie.isJunkFree());
      //while (i.hasNext()) {
      //    System.out.println(i.next());
       //}
    }


    //testing isEmpty function
    @Test public void testFewInsertionsWithDeletion() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        trie.insert("000");
        trie.insert("001");
        trie.insert("011");
        trie.insert("1001");
        trie.insert("1");

        assertFalse("After inserting five strings, the trie should not be considered empty!", trie.isEmpty());
        assertEquals("After inserting five strings, the trie should report five strings stored.", 5, trie.getSize());

        trie.delete("0"); // Failed deletion; should affect exactly nothing.
        assertEquals("After inserting five strings and requesting the deletion of one not in the trie, the trie " +
                "should report five strings stored.", 5, trie.getSize());
        assertTrue("After inserting five strings and requesting the deletion of one not in the trie, the trie had some junk in it!",
                trie.isJunkFree());

        trie.delete("011"); // Successful deletion
        assertEquals("After inserting five strings and deleting one of them, the trie should report 4 strings.", 4, trie.getSize());
        assertTrue("After inserting five strings and deleting one of them, the trie had some junk in it!",
                trie.isJunkFree());
    }
}