package bpt;

import bpt.UnimplementedMethodException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>{@code BinaryPatriciaTrie} is a Patricia Trie over the binary alphabet &#123;	 0, 1 &#125;. By restricting themselves
 * to this small but terrifically useful alphabet, Binary Patricia Tries combine all the positive
 * aspects of Patricia Tries while shedding the storage cost typically associated with tries that
 * deal with huge alphabets.</p>
 *
 * @author Obinna Anadu!
 */
public class BinaryPatriciaTrie {

    /* We are giving you this class as an example of what your inner node might look like.
     * If you would prefer to use a size-2 array or hold other things in your nodes, please feel free
     * to do so. We can *guarantee* that a *correct* implementation exists with *exactly* this data
     * stored in the nodes.
     */
    private static class TrieNode {
        private TrieNode left, right;
        private String str;
        private boolean isKey;

        // Default constructor for your inner nodes.
        TrieNode() {
            this("", false);
        }

        // Non-default constructor.
        TrieNode(String str, boolean isKey) {
            left = right = null;
            this.str = str;
            this.isKey = isKey;
        }
    }

    private TrieNode root;
    private int size;

    /**
     * Simple constructor that will initialize the internals of {@code this}.
     */
    public BinaryPatriciaTrie() {
        root = new TrieNode();
        size = 0;
    }

    public void printTree() {
        for (int i = 1; i <= size; i++) {
          //  helpme(root, i);
        }
        helpme(root);
    }

    private void helpme(TrieNode curr) {
        if (curr != null) {
                TrieNode left = (curr.left != null) ? curr.left : null;
                TrieNode right = (curr.right != null) ? curr.right : null;
          
               
                System.out.println("Current Node: " + curr.str + " isKey? " + curr.isKey + " Left: " +
                ((left != null) ? left.str : "N/A") + " Right: " +  ((right != null) ? right.str : "N/A"));
            
                 helpme(left);
                 helpme(right);
            
           
        }
    }

    

    private TrieNode removeJunk(TrieNode curr) {
        if (curr != null) {
            if (curr.isKey) {
                curr.left = removeJunk(curr.left);
                curr.right = removeJunk(curr.right);
                return curr;
            } else {
                if (curr.left != null && curr.right != null) {
                    curr.left = removeJunk(curr.left);
                    curr.right = removeJunk(curr.right);
                    return curr;
                } else if (curr.left != null) {
                    curr.str += curr.left.str;
                    curr.isKey = true;
                    curr.right = removeJunk(curr.left.right);
                    curr.left = removeJunk(curr.left.left);
                    return curr;
                } else {
                    curr.str += curr.right.str;
                    curr.isKey = true;
                    curr.left = removeJunk(curr.right.left);
                    curr.right = removeJunk(curr.right.right);
                    return curr;
                }
            }
        }
        return null;
    }

    /**
     * Searches the trie for a given key.
     *
     * @param key The input {@link String} key.
     * @return {@code true} if and only if key is in the trie, {@code false} otherwise.
     */
    public boolean search(String key) {
        
        TrieNode curr = root;
        if (key.charAt(0) == '0') {
            curr = curr.left;
        } else {
            curr = curr.right;
        }

    
        
        return search(key, curr);
    }

    private boolean search(String key, TrieNode curr) {
        if (curr != null) {
            //if (key.equals(curr.str) && curr.isKey) {
             //   return true;
            //}

            int i = 0;
            while (i < key.length() && i < curr.str.length() && key.charAt(i) == curr.str.charAt(i)) {
                i = i + 1;
            }
            if (i < key.length() && i >= curr.str.length()) {
                key = key.substring(i);
                if (key.charAt(0) == '0') {
                    return search(key, curr.left);
                } else {
                    return search(key, curr.right);
                }
            } else if (key.equals(curr.str) && curr.isKey)  {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * Inserts key into the trie.
     *
     * @param key The input {@link String}  key.
     * @return {@code true} if and only if the key was not already in the trie, {@code false} otherwise.
     */
    public boolean insert(String key) {
        if (search(key)) {
            return false;
        }
        

        if (size == 0) {

            TrieNode t = new TrieNode(key, true);

            if (key.charAt(0) == '0') {
                root.left = t;
            } else {
                root.right = t;
            }
            size++;
            return true;

        } else {
            size +=1;
            if (key.charAt(0) == '0') {
                root.left = insert(key, root.left);
            } else {
                root.right = insert(key, root.right);
            }   
        }

        root.left = removeJunk(root.left);
        root.right = removeJunk(root.right);
        return true;   
    }


    private TrieNode insert(String key, TrieNode curr) {

        if (curr != null) {    
                int i = 0;
                while (i < key.length() && i < curr.str.length() && key.charAt(i) == curr.str.charAt(i)) {
                    i = i + 1;
                }

                if (i == key.length() && i == curr.str.length() && !curr.isKey) {
                    curr.isKey = true;
                    return curr;
                }
                 
                //this means that i can keep traversing down the tree
                if (i < key.length() && i >= curr.str.length()) {
                    key = key.substring(i);
                    if (key.charAt(0) == '0') {
                        curr.left = insert(key, curr.left);
                        return curr;
                    } else {
                        curr.right = insert(key, curr.right);
                        return curr;
                    }
                }

                //this means what im trying to insert is a prefix of current key
                //so i make a new parent
                if (i >= key.length() && i < curr.str.length()) {
                    String temp = curr.str.substring(i);
                    curr.str = temp;
                    TrieNode parent = new TrieNode(key, true);

                    if (temp.charAt(0) == '0') {
                        parent.left =  curr;
                    } else {
                        parent.right = curr;
                    }
                    return parent;
                }   
                
                //both paramter key and current key share same prefix,
                //so split the node to create two new children
                if (i < key.length() && i < curr.str.length()) {
                    String prefix = key.substring(0, i);
                    key = key.substring(i);
                    String currString = curr.str.substring(i);
                    curr.str = currString;
                    TrieNode split = new TrieNode(prefix, false);
               
                    
                    if (currString.charAt(0) == '0') {
                        split.left = curr;
                        split.right = new TrieNode(key, true);
                    } else {
                        split.right = curr;
                        split.left = new TrieNode(key, true);
                    }

                    return split;
                } 
            
        }
        return new TrieNode(key, true);
    }
    
    
    /**
     * Deletes key from the trie.
     *
     * @param key The {@link String}  key to be deleted.
     * @return {@code true} if and only if key was contained by the trie before we attempted deletion, {@code false} otherwise.
     */
    public boolean delete(String key) {
        if (!search(key)) {
            return false;
        }
      
      
        size--;
        if (key.charAt(0) == '0') {
            root.left = deleteHelp(root.left, key);
        } else {
            root.right = deleteHelp(root.right, key);
        }

        root.left = removeJunk(root.left);
        root.right = removeJunk(root.right);
        return true;
    }

    private TrieNode deleteHelp(TrieNode curr, String key) {
        if (curr != null) {
           
            if (curr.str.equals(key)) {
                if (curr.left != null && curr.right != null) {
                    curr.isKey = false;
                } else if (curr.left == null && curr.right == null) {
                    curr = null;
                } else if (curr.left != null) {
                    
                    curr.str += curr.left.str;
                    curr.left = curr.left.left;
                } else {
                    
                    curr.str += curr.right.str;
                    curr.right = curr.right.right;
                }
                return curr;
            }
        
            int i = 0;
            while (i < key.length() && i < curr.str.length() && key.charAt(i) == curr.str.charAt(i)) {
                i = i + 1;
            }

            key = key.substring(i);
            if (key.charAt(0) == '0') {
                curr.left = deleteHelp(curr.left, key);
                return curr;
            } else {
                curr.right = deleteHelp(curr.right, key);
                return curr;
            }
        }
        return null;
    }

    /**
     * Queries the trie for emptiness.
     *
     * @return {@code true} if and only if {@link #getSize()} == 0, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of keys in the tree.
     *
     * @return The number of keys in the tree.
     */
    public int getSize() {
        return size;
    }

    /**
     * <p>Performs an <i>inorder (symmetric) traversal</i> of the Binary Patricia Trie. Remember from lecture that inorder
     * traversal in tries is NOT sorted traversal, unless all the stored keys have the same length. This
     * is of course not required by your implementation, so you should make sure that in your tests you
     * are not expecting this method to return keys in lexicographic order. We put this method in the
     * interface because it helps us test your submission thoroughly and it helps you debug your code! </p>
     *
     * <p>We <b>neither require nor test </b> whether the {@link Iterator} returned by this method is fail-safe or fail-fast.
     * This means that you  do <b>not</b> need to test for thrown {@link java.util.ConcurrentModificationException}s and we do
     * <b>not</b> test your code for the possible occurrence of concurrent modifications.</p>
     *
     * <p>We also assume that the {@link Iterator} is <em>immutable</em>, i,e we do <b>not</b> test for the behavior
     * of {@link Iterator#remove()}. You can handle it any way you want for your own application, yet <b>we</b> will
     * <b>not</b> test for it.</p>
     *
     * @return An {@link Iterator} over the {@link String} keys stored in the trie, exposing the elements in <i>symmetric
     * order</i>.
     */
    public Iterator<String> inorderTraversal() {
        return new bptIterator();
    }

    class bptIterator implements Iterator<String>{
        private int count;
        ArrayList<String> arr;
        
        public bptIterator() {
            arr = new ArrayList<>();
            count = 0;
            fillArray(root, null, "");
        }

        public String fillArray(TrieNode curr, TrieNode parent, String s) {
            if (curr != null) {
            s +=  curr.str;
            fillArray(curr.left, curr, s);
            if (curr.isKey) {
                arr.add(s);
            }
            fillArray(curr.right, curr, s);
            }

            return s;
        }

        @Override
        public boolean hasNext() {
            return count < arr.size();
        }

        @Override
        public String next() {
            if (hasNext()) {
                String s = arr.get(count);
                count++;
                return s;
            } else {
                throw new NoSuchElementException();
            }
        }
        
    }

    /**
     * Finds the longest {@link String} stored in the Binary Patricia Trie.
     * @return <p>The longest {@link String} stored in this. If the trie is empty, the empty string &quot;&quot; should be
     * returned. Careful: the empty string &quot;&quot;is <b>not</b> the same string as &quot; &quot;; the latter is a string
     * consisting of a single <b>space character</b>! It is also <b>not the same as the</b> null <b>reference</b>!</p>
     *
     * <p>Ties should be broken in terms of <b>value</b> of the bit string. For example, if our trie contained
     * only the binary strings 01 and 11, <b>11</b> would be the longest string. If our trie contained
     * only 001 and 010, <b>010</b> would be the longest string.</p>
     */
    public String getLongest() {
        if (getSize() == 0) {
            return "";
        }
        TrieNode curr = root;
        String s = "";
        return helper(curr, s);
    }

    public String helper(TrieNode curr, String s) {
        if (curr != null) {
            s += curr.str;
            int compare = Integer.compare(helper(curr.left, s).length(), helper(curr.right, s).length());
            if (compare > 0) {
                s = helper(curr.left, s);
            } else if (compare < 0) {
                s = helper(curr.right, s);
            } else {
                int compare2 = Integer.compare(Integer.parseInt(helper(curr.left, s)), Integer.parseInt(helper(curr.right, s)));

                if (compare2 < 0) {
                    s = helper(curr.right, s); 
                } else {
                    s = helper(curr.left, s);
                }
            }
        }
        return s;
    }

    /**
     * Makes sure that your trie doesn't have splitter nodes with a single child. In a Patricia trie, those nodes should
     * be pruned.
     * @return {@code true} iff all nodes in the trie either denote stored strings or split into two subtrees, {@code false} otherwise.
     */
    public boolean isJunkFree(){
        return isEmpty() || (isJunkFree(root.left) && isJunkFree(root.right));
    }

    private boolean isJunkFree(TrieNode n){
        if(n == null){   // Null subtrees trivially junk-free
            return true;
        }
        if(!n.isKey){   // Non-key nodes need to be strict splitter nodes
            return ( (n.left != null) && (n.right != null) && isJunkFree(n.left) && isJunkFree(n.right) );
        } else {
            return ( isJunkFree(n.left) && isJunkFree(n.right) ); // But key-containing nodes need not.
        }
    }
}
