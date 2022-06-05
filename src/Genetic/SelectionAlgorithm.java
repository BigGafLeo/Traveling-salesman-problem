package Genetic;


import Structure.Matrix;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class SelectionAlgorithm {

	static private Matrix matrix;

	static public int[] randomSelection(int size) {
		int[] candidates = new int[size];
		Random random = new Random();
		for (int i = 0; i < size; i++)
			candidates[i] = random.nextInt(size);
		return candidates;
	}

	static public int[] ruletSelection(int[][] genotypes) {
		int size = genotypes[0].length;
		int[] candidates = new int[size];

		Arrays.sort(genotypes, new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				int distance1 = matrix.distance(o1);
				int distance2 = matrix.distance(o1);
				return Integer.compare(distance1, distance2);
			}
		});
		Random random = new Random();

		for (int i = 0; i < size; i++) {
			double pr = random.nextDouble();
			candidates[i] = function(pr);
		}

		return candidates;
	}

	static public int[] tournamentSelection(int[][] genotypes) {
		int size = genotypes[0].length;
		int[] candidates = new int[size];

		return candidates;
	}

	static public void setMatrix(Matrix matrix) {
		SelectionAlgorithm.matrix = matrix;
	}
}
