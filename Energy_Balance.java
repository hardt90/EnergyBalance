import java.util.ArrayList;

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
	public static int[] original_values = {3, 1, 3, 7, 9}; 
	
	// sets of locations with specified sums (numbered from array with upper left 0, row first)
	public static int[][] addends = {{0, 1, 2}, {0, 3, 4}}; 
	
	// desired sums for each corresponding vector in addends
	public static int[] sums = {11, 15};			  
	
	public static int iterations;
	
	
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
	
	public static void main(String[] args){
		
		
		// Strategy: Start with smaller rows/columns (just input them in order of length)
		// While currentSum < , recursively take remaining values and find a possible map to attain the desired sum
		// This ends only if it finds a solution.
		
		iterations = 0;
		
		int[] answer = FindMap(original_values, sums, addends);
		
		for (int i = 0; i < answer.length; i++){
			System.out.println("Position " + i + ": " + answer[i]  + " ");
		}
		if (answer.length == 0){
			System.out.println("No solution found!");
		}
	}
	
	// returns a map from the locations in addends to values (not their indices)
	public static int[] FindMap(int[] values, int[] sums, int[][] addends){
		iterations += 1;
		System.out.println("FindMap calls: " + iterations);
//		if (iterations > 3) System.exit(0);
//		System.out.println();
//		System.out.println("FindMap Iteration " + iterations);
		
//		System.out.println("values: ");
//		for (int i = 0; i < values.length; i++){
//			System.out.print(values[i] + " ");
//		}
//		System.out.println();
		
//		System.out.println("sums: ");
//		for (int i = 0; i < sums.length; i++){
//			System.out.print(sums[i] + " ");
//		}
//		System.out.println();
		
//		System.out.println("addends: ");
//		for (int i = 0; i < addends.length; i++){
//			for (int j = 0; j < addends[i].length; j++){
//				System.out.print(addends[i][j] + " ");
//			}
//			System.out.print(", ");
//		}
//		System.out.println();
//		System.out.println();
		
//		if (values.length == 0){
//			// check remaining sums?
//			return new int[0];
//		}
//		
//		if (sums.length == 0) return new int[0];
		
		// check all possible groups of unused values, go through all candidates with correct sum
		// generate all n-tuples without repeats from 0 to n-1, then use usedValue to turn this into a candidate
//		System.out.print("Available values: ");
//		for (int i = 0; i < values.length; i++){
//			System.out.print(values[i] + " ");
//		}
//		System.out.println();
		
		// generate unused values and a map back to the original
		
		if (addends[0].length == 0) {
			boolean good = true;
			for (int i = 0; i < sums.length; i++){
				if (sums[i] != 0){
					good = false;
				} 
			}
			if (!good) return new int[0];
			else {
				int[] answer = new int[original_values.length];
				
				// initialize to 1000 to indicate unused values
				for (int i = 0; i < answer.length; i++){
					answer[i] = 1000;
				}
//				System.out.println("Returning good values");
				return answer;
			}
		}
		
		int combinationCount = 0;
		CombinationGenerator x = new CombinationGenerator(values.length, addends[0].length);
		int[] indices = new int[addends[0].length];
		int[] indices2 = new int[indices.length];
		int sum = 0;
		while (x.hasMore()){
			combinationCount += 1;
//			System.out.println("Combination Count: " + combinationCount);
			indices = x.getNext();
			
//			if (localIteration == 1){
//				for (int i = 0; i < indices.length; i++){
//					System.out.print(indices[i] + " ");
//				}
//			}
			
			sum = 0;
			for (int j = 0; j < indices.length; j++){
				sum += values[indices[j]];
			}
			
//			System.out.print(" sum " + sum);
			
			if (sum == sums[0]){
//				System.out.println(" candidate!");
				
				// now compute all permutations of indices and iterate through them
				
				for (int i = 0; i < indices.length; i++){
						indices2[i] = indices[i];
				}
				for (int f = 0; f < factorial(indices2.length); f++){
					if (f > 0) {
						nextPermutation(indices2);
//						for (int i = 0; i < indices2.length; i++){
//							System.out.print(indices2[i] + " ");
//						}
//						System.out.println();
					}
					
					// base case
					if (sums.length == 1) {
						int[] answer = new int[original_values.length];
						
						// initialize to 1000 to indicate unused values
						for (int i = 0; i < answer.length; i++){
							answer[i] = 1000;
						}
						for(int i = 0; i < addends[0].length; i++){
							answer[addends[0][i]] = values[indices2[i]];
						}
						return answer;
					}
	
					// otherwise, update parameters before recursion
					// for newValues, skip all values we just used
					int[] newValues = new int[values.length - addends[0].length];
					int temp = 0;
					for (int i = 0; i < values.length; i++){
						boolean inside = false;
						for (int k = 0; k < indices2.length; k++){
							if (indices2[k] == i) inside = true;
						}
						if (inside) continue;
						newValues[temp] = values[i];
						temp++;
					}
					
//					System.out.println("newValues: ");
//					for (int i = 0; i < newValues.length; i++){
//						System.out.print(newValues[i] + " ");
//					}
//					System.out.println();
					
					// adjust addends and sums simultaneously to remove first entry from addends, and any other instances of its variables (and their affects on sums)
					// must adjust newAddends to reference appropriate entries of newValues?
					int[][] newAddends = new int[addends.length - 1][];
					for (int i = 1; i < addends.length; i++){
						newAddends[i - 1] = new int[addends[i].length];
						for(int j = 0; j < addends[i].length; j++){
						    newAddends[i - 1][j] = addends[i][j];
						}
					}
					
					int[] newSums = new int[sums.length - 1];
					for (int i = 1; i < sums.length; i++){
						newSums[i - 1] = sums[i];
					}
					
					boolean fail = false;
					for (int i = 0; i < addends[0].length; i++){
						int index = addends[0][i];
						for (int j = 0; j < newAddends.length; j++){
							for (int k = 0; k < newAddends[j].length; k++){
								if (newAddends[j][k] == index){
	//								System.out.println("found");
									int[] copy = new int[newAddends[j].length - 1];
									System.arraycopy(newAddends[j], 0, copy, 0, k);
						            System.arraycopy(newAddends[j], k+1, copy, k, newAddends[j].length-k-1);
									newAddends[j] = copy;
									newSums[j] -= values[indices2[i]];
									if (newAddends[j].length == 0 && newSums[j] != 0) {
										fail = true;
									}
								}
							}
							
						}
					}
//					if (newAddends[0].length == 0 && !fail) System.out.println("Success ");
					// all of these fail at some point...

					
//					System.out.println("newSums: ");
//					for (int i = 0; i < newSums.length; i++){
//						System.out.print(newSums[i] + " ");
//					}
//					System.out.println();
//					
//					System.out.println("newAddends: ");
//					for (int i = 0; i < newAddends.length; i++){
//						for (int j = 0; j < newAddends[i].length; j++){
//							System.out.print(newAddends[i][j] + " ");
//						}
//						System.out.print(", ");
//					}
//					System.out.println();
					
					if (fail) {
//						System.out.println("Some final sum failed, returning to previous case");
//						System.out.println();
						continue; // have to continue with permutations
					}
					
	//				int[] answer = new int[original_values.length];
	//				if (newAddends[0].length > 0){
	//					int[] answer = FindMap(newValues, newSums, newAddends);
	//				}
					int[] answer = FindMap(newValues, newSums, newAddends);
					if (answer.length == 0) continue;
					
	//				if (answer.length == 0 && newValues.length != 0){
	//					continue;
	//				}
					
	//				System.out.println("Subproblem answer:");
	//				for (int i = 0; i < original_values.length; i++){
	//					System.out.print(answer[i] + " ");
	//				}
	//				System.out.println();
	//				
	//				System.out.println("Addends appended:");
	//				for (int i = 0; i < addends[0].length; i++){
	//					System.out.print(addends[0][i] + " ");
	//				}
	//				System.out.println();
					
					// adjust result to compile results, return new array
	//				if (answer.length == 0) answer = new int[original_values.length];
					for (int i = 0; i < addends[0].length; i++){
	//					System.out.println(answer.length);
	//					System.out.println(addends[0][i]);
						answer[addends[0][i]] = values[indices2[i]];
					}
					
	//				System.out.println("Possible answer so far");
	//				for (int i = 0; i < values.length; i++){
	//					System.out.print(answer[i] + " ");
	//				}
	//				System.out.println();
					
					// check all sums before returning?
	//				for (int i = 0; i < sums.length; i++){
	//					sum = 0;
	//					boolean valid = true;
	//					for (int j = 0; j < addends[i].length; j++){
	//						sum += answer[addends[i][j]];
	//						if (answer[addends[i][j]] == 1000) valid = false;
	//					}
	//					if (sums[i] != sum && valid) return new int[0];
	//				}
					
					return answer;
				
				} // end of permutation loop
			}
			
//			System.out.println();
		}
		
	    // no solution found, return null array
		return new int[0];
	}
	
}

