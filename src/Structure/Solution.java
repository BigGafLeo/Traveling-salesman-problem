package Structure;

import java.util.*;
import java.util.Arrays;
public class Solution {
	private final int dimension;
	private int[] solutionQue;
	private final Matrix matrix;
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
		for(int i = 0; i < dimension; i++)
			solutionQue[i] = i + 1;

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

	public void nearestSingleNeighbour(int first) {
		ArrayList<Integer> cities = new ArrayList<>();
		for (int i = 1; i <= dimension; i++) {
			cities.add(i);
		}
		solutionQue[0] = first;
		distance = 0;
		cities.remove(first - 1);
		for (int i = 1; i < dimension; i++) {
			solutionQue[i] = cities.get(0);
			for (int j = 1; j < dimension - i; j++) {
				if (matrix.get(solutionQue[i - 1] - 1, cities.get(j) - 1) < matrix.get(solutionQue[i - 1] - 1, solutionQue[i] - 1)) {
					solutionQue[i] = cities.get(j);
				}
			}
			distance += matrix.get(solutionQue[i - 1] - 1, solutionQue[i] - 1);
			cities.remove((Integer) solutionQue[i]);
		}
		distance += matrix.get(solutionQue[dimension - 1] - 1, solutionQue[0] - 1);
		/* for (int i = 1; i < dimension; i++) {
			solutionQue[i] = 0;
			for (int j = 1; j < dimension - i; j++) {
				if (matrix.get(solutionQue[i - 1], cities.get(solutionQue[i])) < matrix.get(solutionQue[i - 1], solutionQue[i])) {
					solutionQue[i] = j;
				}
			}
			distance += matrix.get(solutionQue[i - 1], solutionQue[i]);
			cities.remove(solutionQue[i]);
			solutionQue[i] = cities.get(solutionQueue[i]);
		}
		distance += matrix.get(solutionQue[dimension - 1], solutionQue[0]); */
	}

	public void nearestNeighbour() {
		int[] bestSolution;
		nearestSingleNeighbour(1);
		System.out.println(this);
		int bestDistance = distance;
		bestSolution = solutionQue.clone();
		for (int i = 2; i <= dimension; i++) {
			nearestSingleNeighbour(i);
			System.out.println(this);
			if(bestDistance > distance) {
				bestDistance = distance;
				bestSolution = solutionQue.clone();
			}
		}
		solutionQue = bestSolution.clone();

		distance = bestDistance;
	}

	public void symmetricTwoOpt() {
		kRandom();
		System.out.println(this);

		int tmpDistance, firstDistance;
		int[] curvesToCut = new int[2];
		boolean changes = true;

		while (changes) {
			changes = false;
			firstDistance = distance;
			for (int i = 0; i < dimension - 2; i++) {
				for (int j = i + 2; j < dimension + i - 2; j++) {
					tmpDistance = firstDistance;
					tmpDistance -= matrix.get(solutionQue[i] - 1, solutionQue[(i + 1) % dimension] - 1) +
							matrix.get(solutionQue[j % dimension] - 1, solutionQue[(j + 1) % dimension] - 1);
					tmpDistance += matrix.get(solutionQue[i] - 1, solutionQue[j % dimension] - 1) +
							matrix.get(solutionQue[(i + 1) % dimension] - 1, solutionQue[(j + 1) % dimension] - 1);
					if (tmpDistance < distance) {
						distance = tmpDistance;
						curvesToCut[0] = i;
						curvesToCut[1] = j;
						changes = true;
					}
				}
			}
			if (changes) {
				for (int i = curvesToCut[0] + 1; i < (curvesToCut[1] + curvesToCut[0] + 3) / 2; i++) {
					int tmp = solutionQue[i % dimension];
					int index = (curvesToCut[1] - i + curvesToCut[0] + 1) % dimension;
					solutionQue[i % dimension] = solutionQue[index];
					solutionQue[index] = tmp;
				}
				System.out.println(this);
			}
		}
	}

	public void twoOpt() {
		kRandom();
		System.out.println(this);

		int tmpDistance, firstDistance;
		int[] curvesToCut = new int[2];
		boolean changes = true;

		while (changes) {
			changes = false;
			firstDistance = distance;
			for (int i = 2; i < dimension - 1; i++) {
				tmpDistance = firstDistance;
				tmpDistance -= matrix.get(solutionQue[0] - 1, solutionQue[i - 1] - 1)
						+ matrix.get(solutionQue[1] - 1, solutionQue[i] - 1)
						+ matrix.get(solutionQue[i] - 1, solutionQue[i + 1] - 1);
				tmpDistance += matrix.get(solutionQue[0] - 1, solutionQue[i] - 1)
						+ matrix.get(solutionQue[1] - 1, solutionQue[i + 1] - 1)
						+ matrix.get(solutionQue[i] - 1, solutionQue[i - 1] - 1);
				if (tmpDistance < distance) {
					distance = tmpDistance;
					curvesToCut[0] = 0;
					curvesToCut[1] = i;
					changes = true;
				}
				firstDistance = tmpDistance;
				for (int j = 1; j < dimension; j++) {
					tmpDistance -= matrix.get(solutionQue[(j + 1) % dimension] - 1, solutionQue[j] - 1)
							+ matrix.get(solutionQue[j - 1] - 1, solutionQue[(i + j - 1) % dimension] - 1)
							+ matrix.get(solutionQue[(j + i) % dimension] - 1, solutionQue[(j + i + 1) % dimension] - 1);
					tmpDistance += matrix.get(solutionQue[(j + 1) % dimension] - 1, solutionQue[(i + j + 1) % dimension] - 1)
							+ matrix.get(solutionQue[(j + i) % dimension] - 1, solutionQue[(i + j - 1) % dimension] - 1)
							+ matrix.get(solutionQue[j - 1] - 1, solutionQue[j] - 1);
					if (tmpDistance < distance) {
						distance = tmpDistance;
						curvesToCut[0] = j;
						curvesToCut[1] = j + i;
						changes = true;
					}
				}
			}
			if (changes) {
				for (int i = curvesToCut[0] + 1; i < (curvesToCut[1] + curvesToCut[0] + 3) / 2; i++) {
					int tmp = solutionQue[i % dimension];
					int index = (curvesToCut[1] - i + curvesToCut[0] + 1) % dimension;
					solutionQue[i % dimension] = solutionQue[index];
					solutionQue[index] = tmp;
				}
			}
		}
	}
}
