package no.hib.Richard;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Random ran = new Random();
		
		int numberRandom = 1000000;
		
		
		
		int[] ints = new int[numberRandom];
		
		for(int i = 0; i < ints.length; i ++){
			ints[i] = ran.nextInt(10000);
		}
		
		List<Integer> array = new ArrayList<Integer>();
	
		    for (int index = 0; index < ints.length; index++)
		    {
		        array.add(ints[index]);
		    }
		
		  System.out.println("Tall ferdig generert");
		 double start = System.currentTimeMillis(); 
		 System.out.println("Starter sortering");
		 List<Integer> sorted =  mergeSort(array);
		double end = System.currentTimeMillis();
		 
		 //System.out.println(sorted.toString());
		System.out.println(end-start + " ms");
		 
	}
	
	
	public static List<Integer> mergeSort(List<Integer> array){
		
		  // Base case. A list of zero or one elements is sorted, by definition.
	    if(array.size() <= 1){
	    	return array;
	    }
	     

	    // Recursive case. First, *divide* the list into equal-sized sublists.
	    List<Integer> left = new ArrayList<Integer>();
	    List<Integer> right = new ArrayList<Integer>();
	    int middle = array.size()/2;
	    for(int i = 0; i < middle; i++){
	    	left.add(array.get(i));
	    }
	    for(int i = middle; i < array.size(); i++){
	    	right.add(array.get(i));
	    }
	   

	    // Recursively sort both sublists.
	    left = mergeSort(left);
	    right = mergeSort(right);
	    // *Conquer*: merge the now-sorted sublists.
	    return merge(left, right);
		

	}
	
	 
	public static List<Integer> merge(List<Integer> left, List<Integer> right){
    // receive the left and right sublist as arguments.
    // 'result' variable for the merged result of two sublists.
    List<Integer> result = new ArrayList<Integer>();
  // assign the element of the sublists to 'result' variable until there is no element to merge. 
    while(left.size() > 0 || right.size() > 0){
        if( left.size() > 0 && right.size() > 0){
           // compare the first two element, which is the small one, of each two sublists.
            if(left.get(0) <= right.get(0)){
                // the small element is copied to 'result' variable.
                // delete the copied one(a first element) in the sublist.
                result.add(left.get(0));
            	left.remove(0);
            }
            else{
                // same operation as the above(in the right sublist).
            	result.add(right.get(0));
            	right.remove(0);
            }
        }
        else if (left.size() > 0){
            // copy all of remaining elements from the sublist to 'result' variable, 
            // when there is no more element to compare with.
        result.add(left.get(0));
    	left.remove(0);
        }
        else if (right.size() > 0){
            // same operation as the above(in the right sublist).
        result.add(right.get(0));
    	right.remove(0);
        }
    }
    // return the result of the merged sublists(or completed one, finally).
    // the length of the left and right sublists will grow bigger and bigger, after the next call of this function.
    return result;
	}
 
    
}

	
