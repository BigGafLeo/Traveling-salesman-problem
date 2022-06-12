package Genetic;

import Structure.Matrix;

import java.util.Arrays;
import java.util.Random;

public class CrossingAlgorithms {

	private static int [] randomCuts(int dimension){
		int [] cutPoints = new int[2];

		Random random = new Random();
		cutPoints[0] = random.nextInt(dimension - 1);
		cutPoints[1] = random.nextInt(dimension - 2);
		if (cutPoints[1] >= cutPoints[0])
			cutPoints[1]++;
		else {
			int tmp = cutPoints[0];
			cutPoints[0] = cutPoints[1];
			cutPoints[1] = tmp;
		}

		return cutPoints;
	}

	public static int[] PMX(int[] firstParent, int[] secondParent) {
		int dimension = firstParent.length;
		int[] help = new int[dimension];
		int[] cutPoints = randomCuts(dimension);
		int[] child = new int[dimension];

		for (int i = cutPoints[0] + 1; i <= cutPoints[1]; i++) {
			child[i] = secondParent[i];
			help[secondParent[i] - 1] = firstParent[i];
		}
		for (int i = 0; i <= cutPoints[0]; i++) {
			int j = firstParent[i];
			while (help[j - 1] > 0) {
				j = help[j - 1];
			}
			child[i] = j;
		}
		for (int i = cutPoints[1] + 1; i < dimension; i++) {
			int j = firstParent[i];
			while (help[j - 1] > 0) {
				j = help[j - 1];
			}
			child[i] = j;
		}
		return child;
	}

	public static int [] OX(int[] firstParent, int[] secondParent){
		int dimension = firstParent.length;
		boolean[] help = new boolean[dimension];
//		int[] cutPoints = randomCuts(dimension);
		int[] child = new int[dimension];
		int[] cutPoints = {2,5};

		for (int i = cutPoints[0] + 1; i <= cutPoints[1]; i++) {
			child[i] = firstParent[i];
			help[firstParent[i] - 1] = true;
		}
		int j = cutPoints[1] + 1;
		for (int i = cutPoints[1] + 1; i <= cutPoints[0] + dimension; i++) {
			while (help[secondParent[j % dimension] - 1])
				j++;
			child[i % dimension] = secondParent[j++ % dimension];
		}
		return child;
	}

	public static int [] CX(int[] firstParent, int[] secondParent){
		int dimension = firstParent.length;
		int[] help = new int[dimension];
		int[] child = new int[dimension];
		for (int i = 0; i < dimension; i++) {
			help[firstParent[i] - 1] = i;
		}
		int i = 0;
		do {
			child[i] = firstParent[i];
			i = help[secondParent[i] - 1];
		} while (i != 0);
		for (int j = 0; j < dimension; j++) {
			if (child[j] == 0)
				child[j] = secondParent[j];
		}
		return child;
	}

	public static int [][] CX2(int[] firstParent, int[] secondParent){
		int dimension = firstParent.length;
		int [][] children = new int[2][dimension];
		int [] help = new int [dimension];
		boolean [] hasOccurred = new boolean[dimension];
		for (int i = 0; i < dimension; i++) {
			help[firstParent[i] - 1] = i;
		}
		int j = 0, k = -1;
		do {
			do {
				k++;
			} while (hasOccurred[k]);
			int i = k;
			do {
				hasOccurred[i] = true;
				children[0][j] = secondParent[i];
				i = help[secondParent[i] - 1];
				i = help[secondParent[i] - 1];
				children[1][j] = secondParent[i];
				i = help[secondParent[i] - 1];
				j++;
			} while (i != k);
		} while (j < dimension);

		return children;
	}

	public static int[][] nearestNeighbourCross(int[] firstParent, int[] secondParent, Matrix matrix) {
		Random random = new Random();
		int i = random.nextInt(matrix.getDimension());
		int j = random.nextInt(matrix.getDimension() - 1);
		if (j >= i)
			j++;
		int [][] children = new int [2][];
		children[0] = nearestNeighbourCross(firstParent, secondParent, matrix, i);
		children[1] = nearestNeighbourCross(secondParent, firstParent, matrix, j);
		return children;
	}

	public static int[] nearestNeighbourCross(int[] firstParent, int[] secondParent, Matrix matrix, int index) {
		int dimension = matrix.getDimension();
		int [] child = new int [dimension];
		int [][] help = new int [2][dimension];
		int firstOrSecond = 0; // 0 - first, 1 - second
		boolean[] alreadyUsed = new boolean[dimension];

		for (int i = 0; i < dimension; i++) {
			help[0][firstParent[i] - 1] = i;
			help[1][secondParent[i] - 1] = i;
		}

		for (int i = 0; i < dimension - 1; i++) {
			child[i] = (firstOrSecond == 0 ? firstParent[index] : secondParent[index]);
			int secondIndex = help[1 - firstOrSecond][child[i] - 1];
			if (firstOrSecond == 1) {
				int tmp = index;
				index = secondIndex;
				secondIndex = tmp;
			}
			alreadyUsed[child[i] - 1] = true;
			int minDistance = Integer.MAX_VALUE;
			int actDistance;
			int tmpIndex = 0;

			int city = firstParent[(index - 1 + dimension) % dimension];
			if (!alreadyUsed[city - 1] && (actDistance = matrix.get(city - 1, firstParent[index] - 1)) < minDistance) {
				firstOrSecond = 0;
				minDistance = actDistance;
				tmpIndex = (index - 1 + dimension) % dimension;
			}
			city = firstParent[(index + 1) % dimension];
			if (!alreadyUsed[city - 1] && (actDistance = matrix.get(city - 1, firstParent[index] - 1)) < minDistance) {
				firstOrSecond = 0;
				minDistance = actDistance;
				tmpIndex = (index + 1) % dimension;
			}
			city = secondParent[(secondIndex - 1 + dimension) % dimension];
			if (!alreadyUsed[city - 1] && (actDistance = matrix.get(city - 1, secondParent[secondIndex] - 1)) < minDistance) {
				firstOrSecond = 1;
				minDistance = actDistance;
				tmpIndex = (secondIndex - 1 + dimension) % dimension;
			}
			city = secondParent[(secondIndex + 1) % dimension];
			if (!alreadyUsed[city - 1] && (actDistance = matrix.get(city - 1, secondParent[secondIndex] - 1)) < minDistance) {
				firstOrSecond = 1;
				minDistance = actDistance;
				tmpIndex = (secondIndex + 1) % dimension;
			}

			if (minDistance == Integer.MAX_VALUE) {

				firstOrSecond = 0;
				for (int j = 0; j < dimension; j++) {
					city = firstParent[j];
					if (!alreadyUsed[city - 1] && (actDistance = matrix.get(city - 1, firstParent[index] - 1)) < minDistance) {
						minDistance = actDistance;
						tmpIndex = j;
					}
				}
			}
			index = tmpIndex;
		}
		child[dimension - 1] = (firstOrSecond == 0 ? firstParent[index] : secondParent[index]);
		return child;
	}
}
