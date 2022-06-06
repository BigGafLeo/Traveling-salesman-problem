package Genetic;

import Structure.Matrix;

import java.util.Arrays;
import java.util.Random;

public class SelectionAlgorithm {

	private static class TournamentDataClass {
		public int counter = 0;
		public int index;
	}
	static private final int k = 10;
	static private final double p = 0.15;
	static private Matrix matrix;

	static public int[] randomSelection(int size) {
		int[] candidates = new int[size];
		Random random = new Random();
		for (int i = 0; i < size; i++)
			candidates[i] = random.nextInt(size);
		return candidates;
	}

	static public int[] rouletteSelection(Genotype[] genotypes, int size) {
		int[] candidates = new int[size];
		Random random = new Random();

		double[] wages = new double[size];
		wages[0] = (double) 1 / matrix.distance(genotypes[0].genotype);
		for (int i = 1; i < size; i++) {
			wages[i] = wages [i - 1] + (double) 1 / matrix.distance(genotypes[i].genotype);
		}

		for (int i = 0; i < size; i++) {
			double pr = random.nextDouble() * wages[size - 1];
			candidates[i] = BinarySearch.binarySearch(wages, pr, 0, size - 1);
		}

		return candidates;
	}

	static public int[] tournamentSelection(Genotype[] genotypes, int size) {
		int range = size;
		int oldRange = range;
		int notRandomPickedIndexes = 0;
		int index;
		int secondRange;
		int[] candidates = new int[size];
		int[] distances = new int[k];
		int[] genotypesIndexes = new int[k];

		TournamentDataClass[] tournamentData = new TournamentDataClass[size];
		for (int i = 0; i < size; i++) {
			tournamentData[i] = new TournamentDataClass();
			tournamentData[i].index = i;
		}

		Random random = new Random();
		Select.setDivisor(9);

		for (int i = 0; i < size; i++) {

			for (index = 0; index < k - 1; index++) {
				if (random.nextDouble() < p)
					break;
			}

			if (size - i <= k) {
				int l = notRandomPickedIndexes;
				for (int j = notRandomPickedIndexes; j < oldRange - k + notRandomPickedIndexes; j++) {
					if (tournamentData[j].counter == k - (size - i)) {
						swap(tournamentData, l, j);
						l++;
					}
				}
			}

			for (notRandomPickedIndexes = 0; notRandomPickedIndexes < k && tournamentData[notRandomPickedIndexes].counter == k - (size - i); notRandomPickedIndexes++) {
				genotypesIndexes[notRandomPickedIndexes] = tournamentData[notRandomPickedIndexes].index;
				distances[notRandomPickedIndexes] = matrix.distance(genotypes[genotypesIndexes[notRandomPickedIndexes]].genotype);
				tournamentData[notRandomPickedIndexes].counter++;
			}

			secondRange = range - notRandomPickedIndexes;
			oldRange = range;


			for (int j = notRandomPickedIndexes; j < k; j++) {
				int l = random.nextInt(secondRange) + notRandomPickedIndexes;
				swap(tournamentData, l, secondRange + notRandomPickedIndexes - 1);
				tournamentData[secondRange + notRandomPickedIndexes - 1].counter++;
				genotypesIndexes[j] = tournamentData[secondRange + notRandomPickedIndexes - 1].index;
				distances[j] = matrix.distance(genotypes[genotypesIndexes[j]].genotype);
				if (tournamentData[secondRange + notRandomPickedIndexes - 1].counter == k) {
					swap(tournamentData, secondRange + notRandomPickedIndexes - 1, range - 1);
					range--;
				}
				secondRange--;
			}
			candidates[i] = genotypesIndexes[Select.select(distances, index)];
		}
		return candidates;
		/*int[] counters = new int[size];
		for (int i = 0; i < size; i++) {
			counters[i] = tournamentData[i].counter;
		}

		System.out.println(Arrays.toString(candidates));
		return counters;*/
	}

	static public int[] tournamentSelection2(int[][] genotypes, double p, int k) {
		int size = genotypes.length;

		int[] candidates = new int[size];
		int[] distances = new int[k];
		int[] countersPositions = new int[k];
		int[] help = new int[k];
		TournamentDataClass[] tournamentData = new TournamentDataClass[size];
		Arrays.setAll(countersPositions, e -> size - 1);
//		System.out.println(Arrays.toString(countersPositions));
		int range = size;
		for (int i = 0; i < size; i++) {
			tournamentData[i] = new TournamentDataClass();
			tournamentData[i].index = i;
		}
//		System.out.println("K = " + k);
		Random random = new Random();
		Select.setDivisor(9);
		for (int i = 0; i < size; i++) {
			int index, notRandomPickedIndexes;
			for (index = 0; index < k - 1; index++) {
				if (random.nextDouble() < p)
					break;
			}

			for (notRandomPickedIndexes = 0; notRandomPickedIndexes < k && tournamentData[notRandomPickedIndexes].counter == k - (size - i); notRandomPickedIndexes++) {
				distances[notRandomPickedIndexes] = matrix.distance(genotypes[tournamentData[notRandomPickedIndexes].index]);
				tournamentData[notRandomPickedIndexes].counter++;
			}

			int secondRange = range - notRandomPickedIndexes;
//			System.out.println("Range = " + range + ", notRandomPickedIndexes = " + notRandomPickedIndexes);
			for (int j = notRandomPickedIndexes; j < k; j++) {
				help[j] = random.nextInt(secondRange) + notRandomPickedIndexes;
//				System.out.println(tournamentData[help[j]].index);
				swap(tournamentData, help[j], secondRange - 1);
				tournamentData[secondRange - 1].counter++;
				if (tournamentData[secondRange - 1].counter == k) {
					swap(tournamentData, secondRange - 1, range - 1);
					range--;
				}
				distances[j] = matrix.distance(genotypes[tournamentData[secondRange - 1].index]);
				secondRange--;
			}
			for (int j = notRandomPickedIndexes; j < k; j++) {
				swap(tournamentData, secondRange + j - notRandomPickedIndexes, help[k - 1 - j + notRandomPickedIndexes]);
			}
//			System.out.println(Arrays.toString(countersPositions));
			for (int j = notRandomPickedIndexes; j < k; j++) {
//				System.out.println((tournamentData[help[j]].counter - 1) + " " + help[j]);
				countersPositions[tournamentData[help[j]].counter - 1]--;
				swap(tournamentData, help[j], countersPositions[tournamentData[help[j]].counter - 1] + 1);
			}
			candidates[i] = Select.select(distances, index);
		}
		for (int i = 0; i < size; i++) {
//			System.out.print(tournamentData[i].counter + " ");
		}
		int[] counters = new int[size];
		for (int i = 0; i < size; i++) {
			counters[i] = tournamentData[i].counter;
		}
		//return counters;

		return candidates;
	}

	private static void swap(TournamentDataClass[] tournamentData, int l, int i) {
		TournamentDataClass temp = tournamentData[l];
		tournamentData[l] = tournamentData[i];
		tournamentData[i] = temp;
	}

	static public void setMatrix(Matrix matrix) {
		SelectionAlgorithm.matrix = matrix;
	}
}
