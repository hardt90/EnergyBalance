import java.util.ArrayList;
class Permutation{
    
    public static boolean nextPermutation(int[] array) {
	    // Find longest non-increasing suffix
	    int i = array.length - 1;
	    while (i > 0 && array[i - 1] >= array[i])
	        i--;
	    // Now i is the head index of the suffix
	    
	    // Are we at the last permutation already?
	    if (i <= 0)
	        return false;
	    
	    // Let array[i - 1] be the pivot
	    // Find rightmost element that exceeds the pivot
	    int j = array.length - 1;
	    while (array[j] <= array[i - 1])
	        j--;
	    // Now the value array[j] will become the new pivot
	    // Assertion: j >= i
	    
	    // Swap the pivot with j
	    int temp = array[i - 1];
	    array[i - 1] = array[j];
	    array[j] = temp;
	    
	    // Reverse the suffix
	    j = array.length - 1;
	    while (i < j) {
	        temp = array[i];
	        array[i] = array[j];
	        array[j] = temp;
	        i++;
	        j--;
	    }
	    
	    // Successfully computed the next permutation
	    return true;
	}
	
	public static int factorial(int n) {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
    
    
}
public class Energy_Balance {
	
//	 Example: ......................................
//			  .......313.(7/11).....................
//			  .......7..............................
//			  .......9..............................
//			  ....(18/15)...........................
//		      ......................................
	
//  First assign distinct indices (0 - (n - 1)) to all the number locations, like
//    012 (7/11)
//    3
//    4
// (18/15)
//	
//  Then the parameters are:
//  original_values = {3, 1, 3, 7, 9}
//  addends = {{0, 1, 2}, {0, 3, 4}} (These indicate which indices add to column/row sums.  Note 0 is shared in this example.)
//  sums = {11, 15}  (Make sure the order matches the order of your entries of addends).
//  
//  The output is 
//	Position 0: 3 
//	Position 1: 1 
//	Position 2: 7 
//	Position 3: 3 
//	Position 4: 9 
//  So one solution is
//	  317
//	  3
//	  9
//
//  For faster runtime on larger problems, it's best to put shorter rows/columns first and overlap as many entries as possible between the rows and columns as you proceed.  Square configurations should alternate row and column sums.
	
	
	// given numbers to place in rows and columns
	public static int[] original_values = {
        -17, -18, -12,  27, -34,        /*  0 -  4 */
         10,  -4, -23,   0,  25,   6,   /*  5 - 10 */
         26, -14,                       /* 11 - 12 */
        -12,   1,   4, -24,  -3, -12,   /* 13 - 18 */
         15,  -3,                       /* 19 - 20 */
         -2,  15,   0,   7,  -9,   6};  /* 21 - 26 */
	
	// sets of locations with specified sums (numbered from array with upper left 0, row first)
	public static int[][] addends = {
        {0, 1, 2, 3, 4},           // -54 
        {5, 6, 7},                 // -17
        {8, 9, 10},                //  33
        {14, 15, 16, 17, 18},      // -36
        {21, 22, 23},              //  13
        {24, 25, 26},              //   4
        {0, 7},                    // -40
        {6, 11, 13, 19, 22},       //  40
        {4, 9, 12, 17, 20, 25},    // -38
    }; 
	
	// desired sums for each corresponding vector in addends
	public static int[] sums = {-54, -17, 33, -36, 13, 4, -40, 40, -38};			  
	
	public static int iterations = 0;
	
	

	static int EMPTY = 1 << 10;
    
	public static void main(String[] args){
				
		iterations = 0;
		
		int[] answer = FindMap(original_values, sums, addends);
		
		for (int i = 0; i < answer.length; i++){
			System.out.println("Position " + i + ": " + 
                               (answer[i] == EMPTY ? "ANY" : Integer.toString(answer[i]))+ " ");
		}
		if (answer.length == 0){
			System.out.println("No solution found!");
		}
	}
	
	// returns a map from the locations in addends to values (not their indices)
	public static int[] FindMap(int[] values, int[] sums, int[][] addends){
		iterations += 1;
				
        /*
        if (addends[0].length == 0) {
            boolean passed = true;
            for (int i = 0; i < sums.length; i++){
                if (sums[i] != 0){
                    passed = false;
                } 
            }
            if (!passed) {
                return new int[0];
            } else {
                int[] answer = new int[original_values.length];

                for (int i = 0; i < answer.length; i++){
                    answer[i] = EMPTY;
                }
                return answer;//premature, non-solution
            }
            //this check should omitted and pass on to the next iteration of solution, if any remaining items in sums exists.
        }*/

		int combinationCount = 0;
		CombinationGenerator cg = new CombinationGenerator(values.length, addends[0].length);
		int[] addIndices     = new int[addends[0].length];
		int[] addIndicesCopy = new int[addends[0].length];
        
		while (cg.hasMore()){
			combinationCount += 1;
			addIndices = cg.getNext();
						
			int sum = 0;
			for (int j = 0; j < addIndices.length; j++){
				sum += values[addIndices[j]];
			}
						
			if (sum == sums[0]){
				
				// now compute all permutations of indices and iterate through them
				System.arraycopy(addIndices, 0, addIndicesCopy, 0, addIndices.length);

                for (int f = 0; f < Permutation.factorial(addIndices.length); f++){
					if (f > 0) {
						Permutation.nextPermutation(addIndicesCopy);
					}
					
					// base case
					if (sums.length == 1) {
						int[] answer = new int[original_values.length];
						for (int i = 0; i < answer.length; i++){
							answer[i] = EMPTY;
						}
						for(int i = 0; i < addends[0].length; i++){
							answer[addends[0][i]] = values[addIndicesCopy[i]];
						}
						return answer;
					}
	
					// otherwise, update parameters before recursion
					// for newValues, skip all values we just used

					int[] newValues = new int[values.length - addends[0].length];
					int temp = 0;
					for (int i = 0; i < values.length; i++){
						boolean inside = false;
						for (int k = 0; k < addIndicesCopy.length; k++){
							if (addIndicesCopy[k] == i) {
                                inside = true;
                                break;
                            }
						}
						if (inside) continue;
						newValues[temp++] = values[i];
					}

                    int[][] newAddends = new int[addends.length - 1][];
					for (int i = 1; i < addends.length; i++){
						newAddends[i - 1] = new int[addends[i].length];
						System.arraycopy(addends[i], 0, newAddends[i-1], 0, addends[i].length);
					}
					
					int[] newSums = new int[sums.length - 1];
                    System.arraycopy(sums, 1, newSums, 0, sums.length - 1);
					
					boolean failed = false;
					for (int i = 0; i < addends[0].length; i++){
						int index = addends[0][i];
						for (int j = 0; j < newAddends.length; j++){
							for (int k = 0; k < newAddends[j].length; k++){
								if (newAddends[j][k] == index){
									int[] copy = new int[newAddends[j].length - 1];
									System.arraycopy(newAddends[j], 0, copy, 0, k);
						            System.arraycopy(newAddends[j], k+1, copy, k, newAddends[j].length-k-1);
									newAddends[j] = copy;
									newSums[j] -= values[addIndicesCopy[i]];
									if (newAddends[j].length == 0 && newSums[j] != 0) {
										failed = true;
									}
								}
							}
							
						}
					}
					if (failed) {
						continue; // have to continue with permutations
					}
					
					int[] answer = FindMap(newValues, newSums, newAddends);
					if (answer.length == 0) continue;
					
					for (int i = 0; i < addends[0].length; i++){
						answer[addends[0][i]] = values[addIndicesCopy[i]];
					}
					
					return answer;
				
				} // end of permutation loop
			} //end if sum == sum[0]
		} //end of all combination groups
		
	    // no solution found, return null array
		return new int[0];
	}
	
}

