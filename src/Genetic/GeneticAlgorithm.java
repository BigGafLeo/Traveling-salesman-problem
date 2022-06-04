package Genetic;

/*
TODO: Ewaluacja populacji  - ocena przydatności do badań
TODO: Stworzyć połączenia między oddzielnymi wyspami
TODO: Selekcja - rodzaje (losowa, ruletka (im lepszy osobnik tym większe szanse wybrania go), Turniej - najlepsi z pośród k losowych osobników),
 osobnicy w niektórych selekcjach mogą zostać wybrani więcej niż raz albo mniej
TODO: *Dołożyć elementy alg. memetycznego
TODO: dodatkowe mechanizmy śmierci jednostek + ochrona elity
TODO: Delay mutacyjny dla świeżo zmutowanych jednostek

TODO: Mechanizmy łączenia wysp oraz wyznaczania wysp do "mieszanki":
1. Wyznaczenie odchylenia dla elementów populacji - te z mniejszym będą mieszane (jeżeli jest duża różnica funkcji celu dla populacji to oznacza dużą różnicę genotypów).
2. Mechanizmy wykrywania stagnacji: odchylenie standardowe wartości funkcji celu, liczba iteracji bez zmian najlepszego fenotypu
3. Metoda łączenia wysp: zastępujemy wyspy o najgorszym fenotypie mieszanką najgorszy-najlepszy fenotyp / random,
4. Metoda ochrony elitarnych jednostek.
.
*/

import java.util.Random;

import ProblemSolver.*;
import Structure.Matrix;


public class GeneticAlgorithm {
	private static final double crossingProbability = 0.8;
	private static final double mutationProbability = 0.08;
	private static final int mutationLimit = 15;
	private static final Random random = new Random();
	private int[] bestPhenotypeOfIsland;
	private int[][][] genotypes;
	private int[][] bestGenotypeOfIsland;
	private int populationSize;
	private int islandsNumber;
	private final int dimension;
	private final Matrix matrix;
	private final ProblemSolver problemSolver;

	public GeneticAlgorithm(Matrix matrix) {
		this.matrix = matrix;
		this.dimension = matrix.getDimension();
		problemSolver = matrix.isSymmetric() ? new SymmetricProblemSolver(matrix) : new AsymmetricProblemSolver(matrix);
	}

	public void start(int populationSize, GeneratingPopulationMethod[] populationOptions) {
		this.populationSize = populationSize;
		this.islandsNumber = populationOptions.length;
		populationGeneration(populationOptions);
	}

	private void evaluate(){

	}

	private void migrate(){}

	private void setBestGenotypeOfIsland(){

	}

	private void setBestPhenotypeOfIsland(){}

	private int[] bestOfAllPopulations(){return null;}

	private void select(){}

	private void populationGeneration(GeneratingPopulationMethod[] populationOptions) {
		genotypes = new int[islandsNumber][populationSize][dimension];
		bestGenotypeOfIsland = new int[islandsNumber][dimension];
		bestPhenotypeOfIsland  = new int[islandsNumber];

		for (int i = 0; i < islandsNumber; i++) {
			switch (populationOptions[i]) {
				case HYBRID_TWO_OPT_AND_RANDOM -> GeneratingPopulationAlgorithm.hybridIslandGeneration(i);
				case TWO_OPT_GENERATION -> GeneratingPopulationAlgorithm.twoOptIslandGeneration(i);
				case SWAP_GENERATION -> GeneratingPopulationAlgorithm.swapIslandGeneration(i);
				case INSERT_GENERATION -> GeneratingPopulationAlgorithm.insertIslandGeneration(i);
				default -> GeneratingPopulationAlgorithm.randomIslandGeneration(i);
			}
		}
	}



	static public void swap(int [][]table, int i, int j){
		int[] temp = table[i];
		table [i] = table[j];
		table[j] = temp;
	}

	private void cross(CrossingOperators method) {

		for (int k = 0; k < islandsNumber; k++) {
			for (int pair = 0; pair < populationSize / 2; pair++){

				int i = random.nextInt(populationSize - 2 * pair);
				swap(genotypes[k], i, populationSize - 2 * pair - 1);
				int j = random.nextInt(populationSize - 2 * pair - 1);
				swap(genotypes[k], j, populationSize - 2 * pair - 2);

				if (random.nextDouble() <= crossingProbability)
					crossingPair(k,populationSize - 2 * pair - 1, populationSize - 2 * pair - 2, method);
			}
		}

	}

	private void crossingPair(int k, int i, int j, CrossingOperators method) {

		int[] temp;
		int[][] tempDouble;
		switch (method) {
			case OX -> {
				temp = CrossingMethods.OX(genotypes[k][i], genotypes[k][j]);
				genotypes[k][j] = CrossingMethods.OX(genotypes[k][j], genotypes[k][i]);
				genotypes[k][i] = temp;
			}
			case CX -> {
				temp = CrossingMethods.CX(genotypes[k][i], genotypes[k][j]);
				genotypes[k][j] = CrossingMethods.CX(genotypes[k][j], genotypes[k][i]);
				genotypes[k][i] = temp;
			}
			case CX2 -> {
				tempDouble = CrossingMethods.CX2(genotypes[k][i], genotypes[k][j]);
				genotypes[k][j] = tempDouble[0];
				genotypes[k][i] = tempDouble[1];
			}
			default -> { //PMX
				temp = CrossingMethods.PMX(genotypes[k][i], genotypes[k][j]);
				genotypes[k][j] = CrossingMethods.PMX(genotypes[k][j], genotypes[k][i]);
				genotypes[k][i] = temp;
			}
		}
	}

	private void mutation(MutationMethod method, int i, int j){

		MutationMethod[] methods = {MutationMethod.SWAP, MutationMethod.INSERT, MutationMethod.INVERT};

		if (method == MutationMethod.RANDOM) {
			method = methods[random.nextInt(3)];
		}

		problemSolver.setSolution(genotypes[i][j]);
		for (int iteration = random.nextInt(mutationLimit); iteration >= 0; iteration--)
			switch (method) {
				case SWAP -> problemSolver.swap(false);
				case INSERT -> problemSolver.insert(false);
				case NEAREST_NEIGHBOUR -> MutationAlgorithm.nearestNeighbourMutationAlgorithm(genotypes[i][j], matrix);
				default -> problemSolver.twoOpt(false);
			}

		genotypes[i][j] = problemSolver.getSolution();
	}

	private void mutate(MutationMethod method) {
		for (int k = 0; k < islandsNumber; k++) {
			for (int i = 0; i < populationSize; i++) {
				if (random.nextDouble() <= mutationProbability) {
					mutation(method, k, i);
				}
			}
		}
	}
}
