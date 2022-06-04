package Genetic;

import Structure.Matrix;

import java.util.Random;

public class MutationAlgorithm {

	private static final double nearestNeighbourPercentage = 0.1;

	private static void swap(int[] genotype, int firstIndex, int secondIndex){
		int tmp = genotype[firstIndex];
		genotype[firstIndex] = genotype[secondIndex];
		genotype[secondIndex] = tmp;
	}

	public static void nearestNeighbourMutationAlgorithm(int[] genotype, Matrix matrix) {
		Random random = new Random();
		int dimension = genotype.length;
		int firstIndex = random.nextInt(genotype.length);
		int secondIndex = (int)(firstIndex + (double)dimension * nearestNeighbourPercentage);

		for (int i = firstIndex; i < secondIndex - 1; i++) {
			int minIndex = i + 1;
			for (int j = i + 2; j < secondIndex; j++) {
				if (matrix.get(genotype[i] - 1, genotype[j] - 1) < matrix.get(genotype[i] - 1, genotype[minIndex] - 1)) {
					minIndex = j;
				}
				swap(genotype, i + 1, minIndex);
			}

		}
	}


}
