/** 
 *  Binary search tree 
 *  @author Prasanth Kesava Pillai(pxk163630)
 **/

package cs6301.g60;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeMap;

import cs6301.g60.Balance;

public class BST<T extends Comparable<? super T>> implements Iterable<T> {
    static class Entry<T> {
        T element;
        Entry<T> left, right;

        Entry(T x, Entry<T> left, Entry<T> right) {
            this.element = x;
            this.left = left;
            this.right = right;
        }
    }
    
    Entry<T> root;
    int size;
    ArrayDeque<Entry<T>> stack;


    public BST() {
        root = null;
        size = 0;
    }

    public boolean contains(T x) {
        Entry<T> result = find(root, x);

        if(result==null || !result.element.equals(x)){
            return false;
        }

        return true;
    }

    /*
     * Is there an element that is equal to x in the tree.
     *  Element in tree that is equal to x is returned, null otherwise.
     */
    public T get(T x) {
        return find(x).element.equals(x)?x:null;
    }

    public Entry<T> find(T x) {
        stack = new ArrayDeque<>();
        stack.push(new Entry<>(null, null, null));
        return find(root, x);
    }

    public Entry<T> find(Entry<T> root ,T x) {
        Entry<T> node = root;
        if(x.equals(node.element)){
            return node;
        }

        while(true) {
            if (node.element.compareTo(x) > 0) {
                if (node.left == null) {
                    break;
                }else{
                    stack.push(node);
                    node = node.left;
                }
            } else if(node.element.equals(x)){
                return node;
            }else{
                // node.element is smaller than x
                if (node.right == null) {
                    break;
                }else{
                    stack.push(node);
                    node = node.right;
                }
            }
        }
        return node;
    }
    
    /**
     * Return the maximum element in the tree
     */
    public Entry<T> findMax(Entry<T> node) {
    	
    	if(node.right == null) {
    		return node;
    	}    	
    	return findMax(node.right);
    }

    /**
     *  Adds x to tree.
     *  If tree contains a node with same key, replace element by x.
     *  Returns true if x is a new element added to tree.
     */
    public boolean add(T x){
    	return add(new Entry<>(x, null, null));
    }
    
    public boolean add(Entry<T> x) {
        if(root==null){
            root = x;
            size++;
            return true;
        }


        Entry<T> result = find(x.element);
        if(result.element.compareTo(x.element) == 0) {
            result.element = x.element;
            return false;
        }else{
            if(result.element.compareTo(x.element) > 0){
                result.left = x;
            }else if(result.element.compareTo(x.element) < 0){
                result.right = x;
            }
            stack.push(result);
            size++;
            return true;
        }
    }

    /**
     *  Remove x from tree.
     *  Return x if found, otherwise return null
     */
    public T remove(T x) {
	    if(root==null){
	        return null;
        }
        Entry<T> result = find(x);
	    T ans = result.element;
	    if(!result.element.equals(x)){
	        return null;
        } else {
	        if(result.left == null || result.right == null){
	            bypass( result );
            } else {
	            // find the leftmost element in right subtree
                stack.push(result);
                // this sets up the parent pointer for the leftmost node in right subtree.
                Entry<T> leftmost = find( result.right, result.element);
                result.element = leftmost.element;
                bypass(leftmost);
            }
        }
        size--;
	    return ans;

    }

    public void bypass(Entry<T> x){
        Entry<T> parent = stack.peek();
        Entry<T> child = x.left==null?x.right:x.left;
        if(parent.element == null){
            root = child;
        }else if(parent.left!=null && parent.left.equals(x)){
            parent.left = child;
        }else if(parent.right!=null && parent.right.equals(x)){
            parent.right = child;
        }
    }

    public Iterator<T> iterator() {
        return new BSTIterator(this);
    }

    class BSTIterator implements Iterator<T>{
        ArrayDeque<Entry<T>> stack;
        BST<T> bst;
        Entry<T> node;
        public BSTIterator(BST<T> bst){
            stack = new ArrayDeque<>();
            this.bst = bst;
            node = bst.root;
            populateStack(node);
        }


        public boolean hasNext() {
            return !stack.isEmpty();
        }

        public T next() {
            Entry<T> result = stack.pop();
            if(result.element!=null) {
                if (result.right != null) {
                    populateStack(result.right);
                }

                return result.element;
            }else{
                return stack.pop().element;
            }
        }

        public void remove() {
            bst.remove(node.element);
        }

        private void populateStack(Entry<T> node){
            while(node!=null && node.element!=null){
                stack.push(node);
                node = node.left;
            }
        }
    }

    public static void main(String[] args) {
    	System.out.println();
	    BST<Integer> t = new BST<>();
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            int x = in.nextInt();
            if(x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printTree();
            } else if(x < 0) {
                System.out.print("Remove " + x + " : ");
                System.out.println(t.remove(-x));
                t.printTree();
            } else {

                Comparable[] arr = t.toArray();
                System.out.print("Final: ");
                for(int i=0; i<t.size; i++) {
                    System.out.print(arr[i] + " ");
                }
                System.out.println();


                in.close();
                return;
            }
        }
        in.close();
        
    }


    public Comparable[] toArray() {
        Comparable[] arr = new Comparable[size];
        inorder(root, arr);
        return arr;
    }

    public void printTree() {
        System.out.print("[" + size + "]");
        printTree(root);
        System.out.println();
    }

    void printTree(Entry<T> node) {
        if(node != null) {
            printTree(node.left);
            if(node.element!=null)
                System.out.print(" " + node.element);
            printTree(node.right);
        }
    }
    int i = 0;
    void inorder(Entry<T> node, Comparable[] arr) {
        if(node != null) {
            inorder(node.left, arr);
            arr[i++] = node.element;
            inorder(node.right, arr);
        }
    }
}
/*
Sample input:
1 3 5 7 9 2 4 6 8 10 -3 -6 -3 0

Output:
Add 1 : [1] 1
Add 3 : [2] 1 3
Add 5 : [3] 1 3 5
Add 7 : [4] 1 3 5 7
Add 9 : [5] 1 3 5 7 9
Add 2 : [6] 1 2 3 5 7 9
Add 4 : [7] 1 2 3 4 5 7 9
Add 6 : [8] 1 2 3 4 5 6 7 9
Add 8 : [9] 1 2 3 4 5 6 7 8 9
Add 10 : [10] 1 2 3 4 5 6 7 8 9 10
Remove -3 : [9] 1 2 4 5 6 7 8 9 10
Remove -6 : [8] 1 2 4 5 7 8 9 10
Remove -3 : [8] 1 2 4 5 7 8 9 10
Final: 1 2 4 5 7 8 9 10 

*/
