package Genetic;

import java.util.Arrays;
import java.util.Random;

public class CrossingOperators {

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
}
