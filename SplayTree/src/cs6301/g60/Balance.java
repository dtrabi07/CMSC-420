/** 
 *  Rotation Algorithm 
 *  @author Prasanth Kesava Pillai(pxk163630)
 **/


package cs6301.g60;
import cs6301.g60.BST.Entry;

public class Balance {
	
	public static <T extends Comparable<? super T>> Entry<T> singleLeftRotate(Entry<T> p) {
		if(p == null){
			return null;
		}
		Entry<T> Q = p.right;
		if(Q.left != null){
			p.right = Q.left;
		}else{
			p.right = null;
		}
		Q.left = p;
		return Q;
	}
	
	public static <T extends Comparable<? super T>> Entry<T> singleRightRotate(Entry<T> p) {
		if(p == null){
			return null;
		}
		Entry<T> Q = p.left;
		if(Q.right != null){
			p.left = Q.right;
		}else{
			p.left = null;
		}
		Q.right = p;
		return Q;
	}
	
	public static <T extends Comparable<? super T>> Entry<T> doubleLeftRotate(Entry<T> p) {
		if(p == null){
			return null;
		}
		Entry<T> Q = singleLeftRotate(p);
		return singleLeftRotate(Q);
	}

	public static <T extends Comparable<? super T>> Entry<T> doubleRightRotate(Entry<T> p) {
		if(p == null){
			return null;
		}
		Entry<T> Q = singleRightRotate(p);
		return singleRightRotate(Q);
	}

	public static <T extends Comparable<? super T>> Entry<T> doubleRightLeftRotate(Entry<T> p) {
		if(p == null){
			return null;
		}
		p.right = singleRightRotate(p.right);
		return singleLeftRotate(p);
	}
	
	public static <T extends Comparable<? super T>> Entry<T> doubleLeftRightRotate(Entry<T> p) {
		if(p == null){
			return null;
		}
		p.left = singleLeftRotate(p.left);
		return singleRightRotate(p);
	}

}
