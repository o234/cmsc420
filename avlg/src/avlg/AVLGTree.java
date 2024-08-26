package avlg;

import avlg.exceptions.UnimplementedMethodException;
import avlg.exceptions.EmptyTreeException;
import avlg.exceptions.InvalidBalanceException;
import java.util.LinkedList;
import java.util.Queue;


/** <p>{@link AVLGTree}  is a class representing an <a href="https://en.wikipedia.org/wiki/AVL_tree">AVL Tree</a> with
 * a relaxed balance condition. Its constructor receives a strictly  positive parameter which controls the <b>maximum</b>
 * imbalance allowed on any subtree of the tree which it creates. So, for example:</p>
 *  <ul>
 *      <li>An AVL-1 tree is a classic AVL tree, which only allows for perfectly balanced binary
 *      subtrees (imbalance of 0 everywhere), or subtrees with a maximum imbalance of 1 (somewhere). </li>
 *      <li>An AVL-2 tree relaxes the criteria of AVL-1 trees, by also allowing for subtrees
 *      that have an imbalance of 2.</li>
 *      <li>AVL-3 trees allow an imbalance of 3.</li>
 *      <li>...</li>
 *  </ul>
 *
 *  <p>The idea behind AVL-G trees is that rotations cost time, so maybe we would be willing to
 *  accept bad search performance now and then if it would mean less rotations. On the other hand, increasing
 *  the balance parameter also means that we will be making <b>insertions</b> faster.</p>
 *
 * @author Obinna Anadu
 *
 * @see EmptyTreeException
 * @see InvalidBalanceException
 * @see StudentTests
 */
public class AVLGTree<T extends Comparable<T>> {

    /* ********************************************************* *
     * Write any private data elements or private methods here...*
     * ********************************************************* */
    private int maxImbalance;
    private int size;


    private class Node {
		private T key;

         private int height;
		 private int index;
         private Node left, right;
         private int balance;

         public Node(T k) {
            key = k;
		    left = null;
            right = null;
            index = -1;
            height = -1;
            balance = 0;
         }

	}

    private Node root;


    /* ******************************************************** *
     * ************************ PUBLIC METHODS **************** *
     * ******************************************************** */

    /**
     * The class constructor provides the tree with the maximum imbalance allowed.
     * @param maxImbalance The maximum imbalance allowed by the AVL-G Tree.
     * @throws InvalidBalanceException if maxImbalance is a value smaller than 1.
     */
    public AVLGTree(int maxImbalance) throws InvalidBalanceException {
        if (maxImbalance < 1) {
            throw new InvalidBalanceException("");
        }
        this.maxImbalance = maxImbalance; 
        size = 0;
    }

    /**
     * Insert key in the tree. You will <b>not</b> be tested on
     * duplicates! This means that in a deletion test, any key that has been
     * inserted and subsequently deleted should <b>not</b> be found in the tree!
     * s
     * @param key The key to insert in the tree.
     */
    public void insert(T key) {

        
        if (root == null) {
            root = new Node(key);
            root.balance = 0;
            root.height = 0;
            root.index = 0;
        } else {
            root = insertAux(root, key);

        }
         

        root = insertAux(root, key);
        size = size + 1;
        
        indexing(root, 0);
        
    }

    private Node insertAux(Node current, T key) {
        if (current == null) {
            return new Node(key);
        }
        if (key.compareTo(current.key) < 0) {
            current.left = insertAux(current.left, key);
        }

        if (key.compareTo(current.key) > 0) {
            current.right = insertAux(current.right, key);
        }
    
        updateHeights(current);
        updateBalance(current);

        if (Math.abs(current.balance) > maxImbalance) {

            if (current.left != null  && current.balance > maxImbalance && current.left.balance > 0) {
                    current = rotateR(current);
                    
                            
                } else if (current.left != null  && current.balance > maxImbalance && current.left.balance < 0) {
                   
                    current = rotateLR(current);

                } else if (current.left != null  && current.balance > maxImbalance && current.left.balance == 0) {
                    current = rotateR(current);
                //right subtree is heavier
                } else if (current.right != null  && current.balance < maxImbalance && current.right.balance < 0) {
                    
                    current = rotateL(current);
                    
                    
                } else if (current.right != null  && current.balance < maxImbalance && current.right.balance > 0) {
                   
                    current = rotateRL(current);
                            
                } else if ((current.right != null  && current.balance < maxImbalance && current.right.balance == 0)) {
                    current = rotateL(current);
                     
                }
                
        }

        updateHeights(current);
        updateBalance(current);

        return current;
    }

    private void updateBalance(Node curr) {
        if (curr == null) {
            return;
        }
    
        Queue<Node> q = new LinkedList<>();
        q.add(curr);
    
        while (!q.isEmpty()) {
            Node node = q.poll();
    
            int left = (node.left != null) ? node.left.height : -1;
            int right = (node.right != null) ? node.right.height : -1;
            node.balance = left - right;
    
            if (node.left != null) {
                q.add(node.left);
            }
            if (node.right != null) {
                q.add(node.right);
            }
        }
    }

    
    public int updateHeights(Node curr) { 

         if (curr == null) {
            return -1;
        }

        int left = updateHeights(curr.left);
        int right = updateHeights(curr.right);
        int h = Math.max(left, right) + 1;
        curr.height = h;
        return h;
    }


    private Node rotateLR(Node curr) {
        curr.left = rotateL(curr.left);
        Node temp = curr;
        curr = rotateR(temp);
        return curr;
    }


    private Node rotateRL(Node curr) {
        curr.right = rotateR(curr.right);
        Node temp = curr;
        curr = rotateL(temp);
        return curr;
    }
    

    

    private Node rotateL(Node curr) {
        
        Node temp = curr.right;
        curr.right = temp.left;
        temp.left = curr;

        return temp;
    }


    private Node rotateR(Node curr) {
       
        Node temp = curr.left;
        curr.left = temp.right;
        temp.right = curr;

        return temp;
    }

    public Node parent(Node curr, int i, T key) {
        if (curr == null) {
            return null;
        }
        if (i == curr.index) {
            return curr;
        } else if (curr.left != null && curr.left.key == key) {
            return curr;
        } else if (curr.right != null && curr.right.key == key) {
            return curr;
        }


        Node temp = parent(curr.left, i, key);
        if (temp != null) {
            return temp;
        }
         return parent(curr.right, i, key);
    }

    private int indexing(Node current, int i) {
		if (current != null) {
			
			current.index = i++;
			i = indexing(current.left, i);
			i = indexing(current.right, i);
		}
		return i;
	}



    /**
     * Delete the key from the data structure and return it to the caller.
     * @param key The key to delete from the structure.
     * @return The key that was removed, or {@code null} if the key was not found.
     * @throws EmptyTreeException if the tree is empty.
     */
    public T delete(T key) throws EmptyTreeException {

        if (size == 0) {
            throw new EmptyTreeException("null");
        }
        
        Node parent = null;
        root = delete(root, parent, key);

       
        size = size - 1;
        updateHeights(root);

        updateBalance(root);
        indexing(root, 0);
        return key;
        
    }

    public Node delete(Node current, Node parent, T key) {
        if (current == null) {
            return current;
        }

        int compare = key.compareTo(current.key);
        if (compare > 0) {
            current.right = delete(current.right, current, key);
        } else if (compare < 0) {
            current.left = delete(current.left, current, key);
        } else {

            if (current.right != null) { //there is an inorder successor
               
                Node succ = current.right;
                while (succ.left != null) {//grabbing the inorder successor
                    succ = succ.left;
                }

                current.key = succ.key;

                current.right = delete(current.right, current, succ.key);

            } else { //no inorder successor
            
                Node kid = (current.left != null) ? current.left : null;

                if (kid == null) { //no kids
                    kid = current;
                    current = null;
                } else { //1 kid (left one)
                    current = kid;  
                }
                kid = null;

            }

        }          
        return rotateAfterDelete(current);
    }

    private Node rotateAfterDelete(Node current) {//the same stuff that was in my insert method
        if (current == null) {
            return null;
        }
        updateHeights(current);
        updateBalance(current);

        if (Math.abs(current.balance) > maxImbalance) {

            if (current.left != null  && current.balance > maxImbalance && current.left.balance > 0) {
                    current = rotateR(current);
                    
                            
                } else if (current.left != null  && current.balance > maxImbalance && current.left.balance < 0) {
                   
                    current = rotateLR(current);

                } else if (current.left != null  && current.balance > maxImbalance && current.left.balance == 0) {
                    current = rotateR(current);
                //right subtree is heavier
                } else if (current.right != null  && current.balance < maxImbalance && current.right.balance < 0) {
                    
                    current = rotateL(current);
                    
                    
                } else if (current.right != null  && current.balance < maxImbalance && current.right.balance > 0) {
                   
                    current = rotateRL(current);
                            
                } else if ((current.right != null  && current.balance < maxImbalance && current.right.balance == 0)) {
                    current = rotateL(current);
                     
                }
           
        }
        return current;
    }

    /**
     * <p>Search for key in the tree. Return a reference to it if it's in there,
     * or {@code null} otherwise.</p>
     * @param key The key to search for.
     * @return key if key is in the tree, or {@code null} otherwise.
     * @throws EmptyTreeException if the tree is empty.
     */
    public T search(T key) throws EmptyTreeException {
        if (size == 0) {
            throw new EmptyTreeException("empty");
        }
        return searchHelp(key, root);
    }

    private T searchHelp(T key, Node curr) {
        if (curr != null) {
            if (curr.key == key) {
                T temp = curr.key;
                return temp;
            }
            T left = searchHelp(key, curr.left);
            if (left != null) {
                return left;
            }
            return searchHelp(key, curr.right);
        }
        return null;
    }

    /**
     * Retrieves the maximum imbalance parameter.
     * @return The maximum imbalance parameter provided as a constructor parameter.
     */
    public int getMaxImbalance(){
        return maxImbalance;     
    }


    /**
     * <p>Return the height of the tree. The height of the tree is defined as the length of the
     * longest path between the root and the leaf level. By definition of path length, a
     * stub tree has a height of 0, and we define an empty tree to have a height of -1.</p>
     * @return The height of the tree. If the tree is empty, returns -1.
     */
    public int getHeight() {
        if (size == 0) {
            return -1;
        }
       
        return root.height;
    }

    /**
     * Query the tree for emptiness. A tree is empty iff it has zero keys stored.
     * @return {@code true} if the tree is empty, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return size == 0;      // ERASE THIS LINE AFTER YOU IMPLEMENT THIS METHOD!
    }

    /**
     * Return the key at the tree's root node.
     * @return The key at the tree's root node.
     * @throws  EmptyTreeException if the tree is empty.
     */
    public T getRoot() throws EmptyTreeException{
        if (isEmpty()) {
            throw new EmptyTreeException("empty tree");
        }
     
        return root.key;   
    }


    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the BST condition. This method is
     * <b>terrifically useful for testing!</b></p>
     * @return {@code true} if the tree satisfies the Binary Search Tree property,
     * {@code false} otherwise.
     */
    public boolean isBST() {
        Node temp = root;
        boolean b = bstAux(temp, true);
        return b;
    }

    private boolean bstAux(Node curr, boolean b) {
        if (curr != null) {
            T left = (curr.left != null) ? curr.left.key : null;
            T right = (curr.right != null) ? curr.right.key: null;
               
            if (left != null && right != null && (left.compareTo(curr.key) > 0 || right.compareTo(curr.key) < 0)) {      
                b = false;
                return b;
            } else {
                b = bstAux(curr.left, b);
                if (b != false) {
                    b = bstAux(curr.right, b);
                }
            }
        }

        return b;
    }


    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the AVL-G condition. This method is
     * <b>terrifically useful for testing!</b></p>
     * @return {@code true} if the tree satisfies the balance requirements of an AVLG tree, {@code false}
     * otherwise.
     */
    public boolean isAVLGBalanced() {
        Node temp = root;
        boolean b = balancedAux(temp, true);
        return b;
    }

    private boolean balancedAux(Node curr, boolean b) {
        if (curr != null) {
            if (Math.abs(curr.balance) > maxImbalance) {
                b = false;
                return b;
            } else {
                b = balancedAux(curr.left, b);
                b = balancedAux(curr.right, b);
            }
        }
        return b;
    }

    /**
     * <p>Empties the AVL-G Tree of all its elements. After a call to this method, the
     * tree should have <b>0</b> elements.</p>
     */
    public void clear(){
        root = null;
        size = 0;      
    }


    /**
     * <p>Return the number of elements in the tree.</p>
     * @return  The number of elements in the tree.
     */
    public int getCount(){
        return size;
    }



    //helps me visualize tree in my tests
    public void printList(){
		aux(0);
	}


	private void aux(int i){
		if (i < size) {
			Node curr = parent(root, i, null);
            if (curr != null) {
			System.out.println("Current Node: " + curr.key + " Parent: " + ((parent(root, -5, curr.key) != null ? parent(root, -5, curr.key).key : "None")) +
			" Left Child: " +(curr.left != null ? curr.left.key : "None") +
			" Right Child: " + (curr.right != null ? curr.right.key : "None")  + " Index: " + curr.index + " Height: " + curr.height +
            " Balance: " + curr.balance);
            }
			aux(i + 1);

		} else {
			return;
		}
	}
}
