package Genetic;

/*
TODO: Ewaluacja populacji  - ocena przydatności do badań
TODO: Stworzyć połączenia między oddzielnymi wyspami
TODO: Selekcja - rodzaje (losowa, ruletka (im lepszy osobnik tym większe szanse wybrania go), Turniej - najlepsi z pośród k losowych osobników),
 osobnicy w niektórych selekcjach mogą zostać wybrani więcej niż raz albo mniej
TODO: Metoda krzyżowania której jednym z argumentów jest wybrany operator krzyżowania zaś drugim prawdopodobieństwo wystąpenia mutacji
TODO: Stworzyć operatory krzyżowania
TODO: Stworzyć algorytm mutacyjny (swap, insert, invert, k-opt, NN)
TODO: *Dołożyć elementy alg. memetycznego
*/

import java.util.Random;

import ProblemSolver.*;
import Structure.Matrix;


public class GeneticAlgorithm {
	private double crossingProbability = 0.8;

	private int[] bestPhenotype;
	private int[][][] genotypes;
	private int[][] bestGenotype;
	private int populationSize;
	private int islandsNumber;
	private int dimension;
	private Matrix matrix;
	private ProblemSolver problemSolver;

	public GeneticAlgorithm(Matrix matrix) {
		this.matrix = matrix;
		this.dimension = matrix.getDimension();
		problemSolver = matrix.isSymmetric() ? new SymmetricProblemSolver(matrix) : new AsymmetricProblemSolver(matrix);
	}

	public void start(int populationSize, int [] populationOptions) {
		this.populationSize = populationSize;
		this.islandsNumber = populationOptions.length;
		populationGeneration(populationOptions);
	}

	private void evaluation(){

	}

	private void populationGeneration(int [] populationOptions) {
		genotypes = new int[islandsNumber][populationSize][dimension];
		bestGenotype = new int[islandsNumber][dimension];
		bestPhenotype  = new int[islandsNumber];

		for (int i = 0; i < islandsNumber; i++) {
			switch (populationOptions[i]){
				case 0:
					randomIslandGeneration(i);
					break;

				case 1:
					twoOptIslandGeneration(i);
					break;

				case 2:
					swapIslandGeneration(i);
					break;

				case 3:
					insertIslandGeneration(i);
					break;

				default:
					System.exit(666);
					break;
			}
		}
	}

	private void randomIslandGeneration(int k){
		for (int i = 0; i < populationSize; i++) {
			problemSolver.randomPermutation();
			genotypes[k][i] = problemSolver.getSolution();
		}
	}

	private void twoOptIslandGeneration(int k){
		randomIslandGeneration(k);
		for (int i = 0; i < populationSize; i++) {
			problemSolver.twoOpt(false);
			genotypes[k][i] = problemSolver.getSolution();
		}

	}

	private void swapIslandGeneration(int k) {
		randomIslandGeneration(k);
		for (int i = 0; i < populationSize; i++) {
			problemSolver.swap(false);
			genotypes[k][i] = problemSolver.getSolution();
		}
	}

	private void insertIslandGeneration(int k){
		randomIslandGeneration(k);
		for (int i = 0; i < populationSize; i++) {
			problemSolver.insert(false);
			genotypes[k][i] = problemSolver.getSolution();
		}
	}

	static public void swap(int [][]table, int i, int j){
		int[] temp = table[i];
		table [i] = table[j];
		table[j] = temp;
	}

	private void crossingPermutation(int k) {
		Random random = new Random();

		for (int pair = 0; pair < populationSize / 2; pair++){

			int i = random.nextInt(populationSize - 2 * pair);
			swap(genotypes[k], i, populationSize - 2 * pair - 1);
			int j = random.nextInt(populationSize - 2 * pair - 1);
			swap(genotypes[k], j, populationSize - 2 * pair - 2);

			if (random.nextDouble() <= crossingProbability)
				crossingPair(k,populationSize - 2 * pair - 1, populationSize - 2 * pair - 2);
		}
	}

	private void crossingPair(int k, int i, int j) {
		int[] temp = CrossingOperators.PMX(genotypes[k][i], genotypes[k][j]).clone();
		genotypes[k][j] = CrossingOperators.PMX(genotypes[k][j], genotypes[k][i]);
		genotypes[k][i] = temp;
	}


}
