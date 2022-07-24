/** 
 *  Splay Driver Program
 *  @author Prasanth Kesava Pillai(pxk163630)
 **/


package cs6301.g60;

import java.util.Random;

public class SplayDriver {
	
public static void main(String[] args) {
		
		SplayTree<Integer> t = new SplayTree<>();
		int inputSize = 1000;
	    int initialAddSize = (int) (0.2 * inputSize); //first 20% of the input, only add function will be called
	    int add =0, remove = 0, contains = 0;
	        
	    Integer[] input = new Integer[inputSize];
	    for(int i = 0; i < inputSize; i++) {
	    	input[i] = i; 
	    }
	        
	    Shuffle.shuffle(input);
	        
	    for(int i = 0; i < inputSize; i++) {
	    	int random =  (int) (Math.random()*10);
	    	if(i < initialAddSize || random <= 5) {
	    		System.out.println("Add " + input[i]);
	    		add++;
	    		t.add(input[i]);
	    	}else if(random > 5 && random <= 7) {
	        	int index = new Random().nextInt(i);
	        	System.out.println("Remove " +input[index]);
	        	t.remove(input[index]);
	        	remove++;
	        }else if(random > 7 && random <= 10){
	        	System.out.println("Contains ");
	        	int index = (int) ((int)Math.random() * i);
	        	t.contains(input[index]);
	        	contains++;
	        }
	        t.printTree();
	     }
	     System.out.println("Add operation: "+add+"\tRemove operation: "+remove+"\tContains operation: "+contains);
}

}
