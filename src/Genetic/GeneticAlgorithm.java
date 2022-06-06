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

import Genetic.Enums.CrossingOperators;
import Genetic.Enums.GeneratingPopulationMethod;
import Genetic.Enums.MutationMethod;
import ProblemSolver.*;
import Structure.Matrix;


public class GeneticAlgorithm {
	private static final double crossingProbability = 0.8;
	private static final double mutationProbability = 0.01;
	private static final int mutationLimit = 1;
	private static final int connectionDelay = 30;
	private static final long time = 60000;
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

	public void start(GeneratingPopulationMethod populationOption, SelectionMethod selectionMethod,
					  CrossingMethod crossingMethod, MutationMethod mutationMethod, LifeIsBrutalMethod lifeIsBrutalMethod) {

		populationGeneration(populationOption);
		System.out.println(Arrays.toString(bestGenotypeOfIsland));
		long startTime = System.currentTimeMillis();
		SelectionAlgorithm.setMatrix(matrix);
		int iterationCounter = 0;
		while (System.currentTimeMillis() <= startTime + time) {
			allMakeOlder();
			long t = System.currentTimeMillis();
			select(selectionMethod);
			System.out.print("Select: " + (System.currentTimeMillis() - t));
			t = System.currentTimeMillis();
			cross(crossingMethod);
			System.out.print(" Cross: " + (System.currentTimeMillis() - t));
			t = System.currentTimeMillis();
			mutate(mutationMethod);
			System.out.print(" Mutate: " + (System.currentTimeMillis() - t));
			t = System.currentTimeMillis();
			deathZone(lifeIsBrutalMethod);
			System.out.print(" Deatch: " + (System.currentTimeMillis() - t));

			t = System.currentTimeMillis();
			findBest();
			System.out.println(" " + Arrays.toString(bestGenotypeOfIsland));
			if (iterationCounter++ == connectionDelay) {
				bridge();
				iterationCounter = 0;
			}
		}
		int min = bestGenotypeOfIsland[0].phenotype;
		for (int k = 1; k < islandsNumber; k++) {
			if (bestGenotypeOfIsland[k].phenotype < min) {
				min = bestGenotypeOfIsland[k].phenotype;
			}
		}
		System.out.println(min);
	}

	private void allMakeOlder(){
		for (int k = 0; k < islandsNumber; k++)
			for (int i = 0; i < populationSize; i++)
				genotypes[k][i].makeOlder();
	}

	private void findBest() {
		for (int k = 0; k < islandsNumber; k++) {
			int minIndex = -1;
			for (int i = 0; i < populationSize; i++) {
				if (genotypes[k][i].phenotype < bestGenotypeOfIsland[k].phenotype) {
					bestGenotypeOfIsland[k].phenotype = genotypes[k][i].phenotype;
					minIndex = i;
				}
			}
			if (minIndex > -1) {
				bestGenotypeOfIsland[k].genotype = genotypes[k][minIndex].genotype.clone();
				bestGenotypeOfIsland[k].age = genotypes[k][minIndex].age;
			}
		}
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
		findBest();
		for (int i = 0; i < islandsNumber; i++) {
			boolean[] arr = new boolean[genotypes[i][0].genotype.length];
			System.out.println(Arrays.toString(bestGenotypeOfIsland[i].genotype));
			System.out.println(matrix.distance(bestGenotypeOfIsland[i].genotype));
			for (int k = 0; k < bestGenotypeOfIsland[i].genotype.length; k++) {
				arr[bestGenotypeOfIsland[i].genotype[k] - 1] = true;
			}
			for (int k = 0; k < bestGenotypeOfIsland[i].genotype.length; k++) {
				if (!arr[k]) {
					for (int l = 0; l < 50; l++) {
						System.out.println("dupa\n");
					}
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
	}

	private void bridge() {

		Random random = new Random();
		int[] islandRepresentation = new int[islandsNumber];
		for (int i = 0; i < islandsNumber; i++) {
			islandRepresentation[i] = i;
		}
		for (int pair = 0; pair < islandsNumber / 2; pair++) {
			int i = random.nextInt(islandsNumber - pair * 2);
			swap(islandRepresentation, islandsNumber - 2 * pair - 1, i);
			int j = random.nextInt(islandsNumber - pair * 2 - 1);
			swap(islandRepresentation, islandsNumber - 2 * pair - 2, j);
		}
		interIslandConnection(islandRepresentation);
		System.out.println("Bridge");
	}

	private void interIslandConnection(int[] islandRepresentation) {
		for (int i = islandRepresentation.length - 1; i > 0; i -= 2) {
			for (int j = 0; j < populationSize; j += 2){
				Genotype tmpGenotype = genotypes[islandRepresentation[i]][j];
				genotypes[islandRepresentation[i]][j] = genotypes[islandRepresentation[i - 1]][j];
				genotypes[islandRepresentation[i - 1]][j] = tmpGenotype;
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

				if (random.nextDouble() <= crossingProbability)		//TODO: Upewnić się że krzyżowane elementy są parami różne
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

		genotypes[k][i].genotype = problemSolver.getSolution();
		genotypes[k][i].phenotype = matrix.distance(genotypes[k][i].genotype);
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

	public static void main(String[] args) {
		try {
			Matrix matrix = FileManager.readFile("data/ALL_tsp/berlin52.tsp");
			GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(matrix, 300, 6);
			geneticAlgorithm.start(GeneratingPopulationMethod.SWAP_GENERATION, SelectionMethod.TOURNAMENT_SELECTION,CrossingMethod.PMX,MutationMethod.SWAP,LifeIsBrutalMethod.RANDOM_ELIMINATING);
		} catch (FileNotFoundException | WrongFileFormatException e) {
			e.printStackTrace();
		}
	}
}