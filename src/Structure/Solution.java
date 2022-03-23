package Structure;

import java.util.Arrays;
import java.util.Random;

public class Solution {
	private int dimension;
	private int[] solutionQue;
	private Matrix matrix;
	private int distance;
	
	public Solution(Matrix matrix)
	{	
		this.matrix=matrix;
		this.dimension = matrix.getDimension();
		solutionQue = new int[dimension];
	}
	public void kRandom()
	{
		Random random = new Random();
		for(int i =0;i<dimension;i++)
			solutionQue[i]=i;

		for (int a=10;a>0;a--) {
			for (int i = dimension - 1; i > 0; i--) {
				int j = random.nextInt(i + 1);
				int temp = solutionQue[i];
				solutionQue[i] = solutionQue[j];
				solutionQue[j] = temp;
			}
		}
		distance = matrix.distance(solutionQue);
	}

	@Override
	public String toString() {
		return "Solution{" +
				"solutionQue=" + Arrays.toString(solutionQue) +
				", distance=" + distance +
				'}';
	}
}
