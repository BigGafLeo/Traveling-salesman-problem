package Genetic;

import Structure.Matrix;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static Genetic.GeneticAlgorithm.swap;


public class LifeIsBrutalAlgorithm {

	private static final double percentOfElite = 0.2;
	private static final Random random = new Random();

	private static void elite(Genotype[] genotypes, int numberOfGenotypes) {
		Arrays.sort(genotypes, 0, numberOfGenotypes, Comparator.comparingInt(o -> o.phenotype));
	}

	public static void eliteProtection(Genotype[] genotypes, int numberOfGenotypes){

		int chosenOne;
		int sizeOfElite = (int)(numberOfGenotypes * percentOfElite) + 1;

		elite(genotypes, numberOfGenotypes);

		for (int i = sizeOfElite; i < numberOfGenotypes; i++){
			chosenOne = random.nextInt(numberOfGenotypes - i) + sizeOfElite;
			swap(genotypes, chosenOne,numberOfGenotypes - 1 - i + sizeOfElite);
		}
	}

	public static void randomEliminating(Genotype[] genotypes, int numberOfGenotypes){
		int chosenOne;
		for (int i = 0; i < numberOfGenotypes - 1; i++){
			chosenOne = random.nextInt(numberOfGenotypes - i);
			swap(genotypes, chosenOne, numberOfGenotypes - 1 - i);
		}
	}

	public static void ageEliminating(Genotype[] genotypes, int numberOfGenotypes) {
		int populationSize = genotypes.length / 2;
		int sizeOfElite = (int)(numberOfGenotypes * percentOfElite);
		double[] sumsOfAges = new double [numberOfGenotypes - sizeOfElite];
		sumsOfAges[0] = genotypes[sizeOfElite].age;
		for (int i = sizeOfElite + 1; i < numberOfGenotypes; i++) {
			sumsOfAges[i - sizeOfElite] = sumsOfAges[i - sizeOfElite - 1] + genotypes[i].age;
		}
		for (int i = numberOfGenotypes - 1; i >= populationSize; i--) {
			double value = random.nextDouble() * sumsOfAges[numberOfGenotypes - sizeOfElite - 1];
			int index = BinarySearch.binarySearch(sumsOfAges, value, 0, i - sizeOfElite);
			swap(genotypes, index + sizeOfElite, i);
		}
		/*int populationSize = genotypes.length / 2;
		double[] sumsOfAges = new double [numberOfGenotypes];
		sumsOfAges[0] = genotypes[0].age;
		for (int i = 1; i < numberOfGenotypes; i++) {
			sumsOfAges[i] = sumsOfAges[i - 1] + genotypes[i].age;
		}
		for (int i = numberOfGenotypes - 1; i >= populationSize; i--) {
			double value = random.nextDouble() * sumsOfAges[numberOfGenotypes - 1];
			int index = BinarySearch.binarySearch(sumsOfAges, value, 0, i);
			swap(genotypes, index, i);
		}*/
	}
}
