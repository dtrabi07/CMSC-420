/** 
 *  Implementation of Splay Tree 
 *  @author Prasanth Kesava Pillai(pxk163630)
 **/
 
package cs6301.g60;

import java.util.Scanner;

public class SplayTree<T extends Comparable<? super T>> extends BST<T> {

	Entry<T> temp;
	SplayTree() {
		super();
	}
	
	/**
	 * Creating Splay Nodes
	 */
	static class Entry<T> extends BST.Entry<T> {
		Entry<T> parentNode;
		Entry(T x, Entry<T> left, Entry<T> right) {
            super(x, left, right);
            parentNode = null;
        }
	}
	
	/**
	 * Add operation on Splay Tree
	 * If the element is already present, 
	 * 		Splay the tree to make the element that is being added as the root 
	 * Else 
	 * 		Add the element in the tree and splay the tree
	 */
	@Override
	public boolean add(T x) {
		temp = null;
		boolean addElement = super.add(new Entry<>(x, null, null));
	    if(addElement == false){
	    	splay(x);
	    	return false;
	    }
	    if(this.size == 1){
	    	return true;
	    }
	    splay(x);
	    return true;
	}
	
	/**
	 * Splay Tree : Remove Operation
	 * If the element is not present, 
	 * 		Splay the tree to make the last element in search of the element to be
	 * 		removed as the root 
	 * Else 
	 * 		Remove the element by splaying the tree
	 * 		If the element removed has right and left subtree
	 * 			Make the max element on the left subtree as the root
	 * 		Else it has only right subtree
	 * 			Make the root of the right subtree as the splay tree's root
	 */
	@Override
	public T remove(T x) {
		temp = null;
		if(this.root == null)
			return null;
		
		//If the root element is not the element to be removed, splay the tree
		if(this.root.element != x) {
			Entry<T> entry = (Entry<T>) find(x); 
			splay(entry.element);
		}
		//If left of root is null and right of root is not null --> return root.right
		if(this.root.element == x && this.root.left == null && this.root.right != null) {
			Entry<T> entry = (Entry<T>) this.root.right;
			this.root.right = null;
			this.root = entry;
			this.size--;
			return root.element;
		}
		
		//If there are 2 roots after removing the element x : findMax of left tree and make it the root
		if(this.root.element == x && this.root.left != null && this.root.right != null) {
			Entry<T> rightRoot = (Entry<T>) this.root.right;
			Entry<T> leftRoot = (Entry<T>) this.root.left;
			Entry<T> newRoot = null;
			//findMax on left root
			Entry<T> maxNode = (Entry<T>) findMax(this.root.left);
			this.root = leftRoot;
			find(maxNode.element); 
			if(this.stack.isEmpty() || this.stack.peek().element == null) {
				newRoot = leftRoot;
			}
			if(!this.stack.isEmpty() && this.stack.peek().element != null) {
				temp = null;
				splay(maxNode.element);
			}else{
				this.root = newRoot;
			}	
			this.root.right = rightRoot;
			this.size--;
		}	
		
		//If there are 2 roots after removing the element x : findMax of left tree and make it the root
		else if(this.root.element == x && this.root.left != null && this.root.right == null) {
			
			Entry<T> rightRoot = (Entry<T>) this.root.right;
			Entry<T> leftRoot = (Entry<T>) this.root.left;
			Entry<T> newRoot = null;
			//findMax on left root
			Entry<T> maxNode = (Entry<T>) findMax(this.root.left);
			this.root = leftRoot;
			find(maxNode.element); 
			if(this.stack.isEmpty() || this.stack.peek().element == null) {
				newRoot = leftRoot;	
			}
			if(!this.stack.isEmpty() && this.stack.peek().element != null) {
				temp = null;
				splay(maxNode.element);
				this.root = maxNode;
			}else{
				this.root = newRoot;
			}
			this.root.right = rightRoot;	
			this.size--;
		}	
		return x;
	}
	
	
	/**
	 * Splay Tree Contains Operation :
	 * 	1. If the element is present, splay the tree and make it the root
	 *  2. Else, make the last element accessed in search of the conatins element as the root
	 */
	@Override
	public boolean contains(T x){
		
		temp = null;
		if(this.root == null)
			return false;
		
		//If the root element is not the element to be removed, splay the tree
		if(this.root.element != x) {
			T y = find(x).element;
			splay(y);
			if(x == y) {
				return true;
			}else{
				return false;
			}
		}
		return true;
	}	
	
	public void splay(T x)  {
		Entry<T> parent = null;
		Entry<T> e = null;
		if(this.stack.size() > 1){
    		e = (Entry<T>) this.stack.pop();
    	}else{
    		return;
    	}
		while(!this.stack.isEmpty() && this.stack.peek().element != null){
    		parent = (Entry<T>) this.stack.pop();
    		splayOperation(e, parent, x);
    		if(this.stack.isEmpty()|| this.stack.peek().element == null) {
    			break;
    		}
    		e = (Entry<T>) this.stack.pop();
    		parent = null;
    	}
    	if(this.root.element != x) {
    		splayOperation(e, parent, x);
    	}
	}
	
	/**
	 * Splay Operation on the tree
	 */
	public Entry<T> splayOperation(Entry<T> e, Entry<T> parent, T x) {
		
		//Root element : Do nothing
		if(e == null){
			return null;
		}
		SplayTree.Entry<T> newNode = e;
		
		if(e != null) {
			if(parent == null && x.compareTo(e.element) <= 0) {
				if(temp != null){
					e.left = temp;
					temp = null;
				}
				//System.out.println("Zig");
				newNode = (Entry<T>) Balance.singleRightRotate(e);
			}else if(parent == null && x.compareTo(e.element) > 0) {
				if(temp != null){
					e.right = temp;
					temp = null;
				}
				//System.out.println("Zag");
				newNode = (Entry<T>) Balance.singleLeftRotate(e);
			}else if(parent != null && x.compareTo(e.element) <= 0 && e.element.compareTo(parent.element)<= 0) {
				//System.out.println("Zig-Zig Right");
				if(temp != null){
					e.left = temp;
					temp = null;
				}
				newNode = (Entry<T>) Balance.doubleRightRotate(parent);
			}else if(parent != null && x.compareTo(e.element) > 0 && e.element.compareTo(parent.element) > 0) {
				//System.out.println("Zag-Zag rotation");
				if(temp != null){
					e.right = temp;
					temp = null;
				}
				newNode = (Entry<T>) Balance.doubleLeftRotate(parent);
			}else if(parent != null && x.compareTo(e.element)> 0 && e.element.compareTo(parent.element)<= 0) {
				//System.out.println("Zag-Zig rotation");
				if(temp != null){
					e.right = temp;
					temp = null;
				}
				newNode = (Entry<T>)Balance.doubleLeftRightRotate(parent);
			}else{
				//System.out.println("Zig-Zag rotation");
				if(temp != null){
					e.left = temp;
					temp = null;
				}
				newNode = (Entry<T>)Balance.doubleRightLeftRotate(parent);
			}
		}
		
		if(parent == null || parent == root){
			this.root = newNode;
			return null;
		}
		this.temp = newNode;
		return newNode;
	}
	
    public static void main(String[] st) {
        SplayTree<Integer> t = new SplayTree<>();
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            int x = in.nextInt();
            if(x > 0) {
                System.out.print("Add " + x + " : ");
                t.add(x);
                t.printTree();
            } else if(x < 0) {
                System.out.print("Remove " + x + " : ");
                //t.contains(-x);
                t.remove(-x);
                t.printTree();
            } else {

                for(Integer node: t){
                    System.out.println(node);
                }
                System.out.println();
                in.close();
                return;
            }
        }
        in.close();
        
    }
}

