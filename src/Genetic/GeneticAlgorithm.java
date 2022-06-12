package Genetic;

/*
TODO: Ewaluacja populacji  - ocena przydatności do badań
TODO: Stworzyć połączenia między oddzielnymi wyspami
TODO: Delay mutacyjny dla świeżo zmutowanych jednostek
TODO: *Dołożyć elementy alg. memetycznego

TODO: Mechanizmy łączenia wysp oraz wyznaczania wysp do "mieszanki":
1. Wyznaczenie odchylenia dla elementów populacji - te z mniejszym będą mieszane (jeżeli jest duża różnica funkcji celu dla populacji to oznacza dużą różnicę genotypów).
2. Mechanizmy wykrywania stagnacji: odchylenie standardowe wartości funkcji celu, liczba iteracji bez zmian najlepszego fenotypu
3. Metoda łączenia wysp: zastępujemy wyspy o najgorszym fenotypie mieszanką najgorszy-najlepszy fenotyp / random,
4. Metoda ochrony elitarnych jednostek.
.
*/

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Random;

import FileManagerPackage.FileManager;
import FileManagerPackage.WrongFileFormatException;
import Genetic.Enums.*;
import ProblemSolver.*;
import Structure.Matrix;


public class GeneticAlgorithm {
	private static final double crossingProbability = 0.8;
	private static final double mutationProbability = 0.01;
	private static final int mutationLimit = 2;
	private static final int connectionDelay = 60;
	private static final long time = 60000;
	private static final long interval = 1000;
	private static final Random random = new Random();
	private Genotype[][] genotypes;
	private Genotype[] bestGenotypeOfIsland;
	private int [][] candidates;
	private final int [] numbersOfChildren;
	private final int populationSize;
	private final int islandsNumber;
	private final Matrix matrix;
	private final ProblemSolver problemSolver;

	public GeneticAlgorithm(Matrix matrix, int populationSize, int islandsNumber) {
		this.matrix = matrix;
		this.populationSize = populationSize;
		this.islandsNumber = islandsNumber;
		numbersOfChildren = new int[islandsNumber];
		problemSolver = matrix.isSymmetric() ? new SymmetricProblemSolver(matrix) : new AsymmetricProblemSolver(matrix);
	}

	public void start(GeneratingPopulationMethod populationOption, SelectionMethod selectionMethod,
					  CrossingMethod crossingMethod, MutationMethod mutationMethod, LifeIsBrutalMethod lifeIsBrutalMethod) {
		//System.out.println("Czas w sekundach;Wartość funkcji celu");
		populationGeneration(populationOption);
		//System.out.println(0 + ";" + findBestOfAll().phenotype);
		long startTime = System.currentTimeMillis();
		long prev = startTime;
		SelectionAlgorithm.setMatrix(matrix);
		int iterationCounter = 0;
		while (System.currentTimeMillis() <= startTime + time) {
			allMakeOlder();
			long t = System.currentTimeMillis();
			select(selectionMethod);
			cross(crossingMethod);
			mutate(mutationMethod);
			deathZone(lifeIsBrutalMethod);

			if (iterationCounter++ == connectionDelay) {
				bridge();
				iterationCounter = 0;
			}
			findBest();
			if (System.currentTimeMillis() - prev >= interval) {
				//System.out.println((System.currentTimeMillis() - startTime) / interval + ";" + findBestOfAll().phenotype);
				prev = System.currentTimeMillis();
			}
		}
		//System.out.println((System.currentTimeMillis() - startTime) / interval + ";" + findBestOfAll().phenotype);
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

	public Genotype findBestOfAll() {
		Genotype best = bestGenotypeOfIsland[0];
		for (int i = 1; i < islandsNumber; i++) {
			if (bestGenotypeOfIsland[i].phenotype < best.phenotype) {
				best = bestGenotypeOfIsland[i];
			}
		}
		return best;
	}

	private void select(SelectionMethod selectionMethod){
		for (int k = 0; k < islandsNumber; k++) {
			switch (selectionMethod){
				case ROULETTE_SELECTION -> candidates[k] = SelectionAlgorithm.rouletteSelection(genotypes[k], populationSize);
				case TOURNAMENT_SELECTION -> candidates[k] = SelectionAlgorithm.tournamentSelection(genotypes[k], populationSize);
				default -> candidates[k] = SelectionAlgorithm.randomSelection(populationSize);
			}
		}

	}

	private void populationGeneration(GeneratingPopulationMethod populationOption) {
		candidates = new int[islandsNumber][populationSize];
		genotypes = new Genotype[islandsNumber][2 * populationSize];
		bestGenotypeOfIsland = new Genotype[islandsNumber];
		for (int k = 0; k < islandsNumber; k++) {
			bestGenotypeOfIsland[k] = new Genotype(matrix);
			bestGenotypeOfIsland[k].phenotype = Integer.MAX_VALUE;
		}
		GeneratingPopulationAlgorithm.setParameters(genotypes, populationSize, problemSolver, matrix);
		for (int i = 0; i < islandsNumber; i++) {
			switch (populationOption) {
				case HYBRID_TWO_OPT_AND_RANDOM -> GeneratingPopulationAlgorithm.hybridIslandGeneration(i);
				case TWO_OPT_GENERATION -> GeneratingPopulationAlgorithm.twoOptIslandGeneration(i);
				case SWAP_GENERATION -> GeneratingPopulationAlgorithm.swapIslandGeneration(i);
				case INSERT_GENERATION -> GeneratingPopulationAlgorithm.insertIslandGeneration(i);
				default -> GeneratingPopulationAlgorithm.randomIslandGeneration(i);
			}
		}
		findBest();
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
	}

	private void interIslandConnection(int[] islandRepresentation) {
		for (int i = islandRepresentation.length - 1; i > 0; i -= 2) {
			for (int j = 0; j < populationSize; j += 4){
				Genotype tmpGenotype = genotypes[islandRepresentation[i]][j];
				genotypes[islandRepresentation[i]][j] = genotypes[islandRepresentation[i - 1]][j];
				genotypes[islandRepresentation[i - 1]][j] = tmpGenotype;
			}
		}
	}

	static public void swap(Genotype[] array, int i, int j){
		Genotype temp = array[i];
		array [i] = array[j];
		array[j] = temp;
	}

	static public void swap(int[] array, int i, int j) {
		int temp = array[i];
		array [i] = array[j];
		array[j] = temp;
	}

	private void cross(CrossingMethod method) {
		for (int k = 0; k < islandsNumber; k++) {
			numbersOfChildren[k] = 0;
			for (int pair = 0; pair < populationSize / 2; pair++) {
				int i = random.nextInt(populationSize - 2 * pair);
				swap(candidates[k], i, populationSize - 2 * pair - 1);
				int j = random.nextInt(populationSize - 2 * pair - 1);
				swap(candidates[k], j, populationSize - 2 * pair - 2);
				i = candidates[k][populationSize - 2 * pair - 1];
				j = candidates[k][populationSize - 2 * pair - 2];
				if (random.nextDouble() <= crossingProbability && i != j) {
					crossingPair(k, i, j, method, pair);
					numbersOfChildren[k]++;
				}
			}
		}
	}

	private void crossingPair(int k, int i, int j, CrossingMethod method, int pair) {

		int[] temp;
		int[][] tempDouble;
		switch (method) {
			case OX -> {
				temp = CrossingAlgorithms.OX(genotypes[k][i].genotype, genotypes[k][j].genotype);
				genotypes[k][2 * pair + populationSize].setGenotype(CrossingAlgorithms.OX(genotypes[k][j].genotype, genotypes[k][i].genotype));
				genotypes[k][2 * pair + 1 + populationSize].setGenotype(temp);
			}
			case CX -> {
				temp = CrossingAlgorithms.CX(genotypes[k][i].genotype, genotypes[k][j].genotype);
				genotypes[k][2 * pair + populationSize].setGenotype(CrossingAlgorithms.CX(genotypes[k][j].genotype, genotypes[k][i].genotype));
				genotypes[k][2 * pair + 1 + populationSize].setGenotype(temp);
			}
			case CX2 -> {
				tempDouble = CrossingAlgorithms.CX2(genotypes[k][i].genotype, genotypes[k][j].genotype);
				genotypes[k][2 * pair + populationSize].setGenotype(tempDouble[0]);
				genotypes[k][2 * pair + 1 + populationSize].setGenotype(tempDouble[1]);
			}
			default -> { //PMX
				temp = CrossingAlgorithms.PMX(genotypes[k][i].genotype, genotypes[k][j].genotype);
				genotypes[k][2 * pair + populationSize].genotype = CrossingAlgorithms.PMX(genotypes[k][j].genotype, genotypes[k][i].genotype);
				genotypes[k][2 * pair + 1 + populationSize].setGenotype(temp);
			}
		}
	}

	private void mutation(MutationMethod method, int k, int i){

		MutationMethod[] methods = {MutationMethod.RANDOM_SWAPS, MutationMethod.INSERT, MutationMethod.INVERT, MutationMethod.NEAREST_NEIGHBOUR};

		if (method == MutationMethod.RANDOM) {
			method = methods[random.nextInt(4)];
		}

		problemSolver.setSolution(genotypes[k][i].genotype);
		for (int iteration = random.nextInt(mutationLimit); iteration >= 0; iteration--)
			switch (method) {
				case RANDOM_SWAPS -> MutationAlgorithm.randomSwaps(genotypes[k][i].genotype);
				case SWAP -> problemSolver.swap(false);
				case INSERT -> problemSolver.insert(false);
				case NEAREST_NEIGHBOUR -> MutationAlgorithm.nearestNeighbourMutationAlgorithm(genotypes[k][i].genotype, matrix);
				default -> problemSolver.twoOpt(false);
			}

		genotypes[k][i].genotype = problemSolver.getSolution();
		genotypes[k][i].phenotype = matrix.distance(genotypes[k][i].genotype);
	}

	private void mutate(MutationMethod method) {
		for (int k = 0; k < islandsNumber; k++) {
			for (int i = 0; i < populationSize + numbersOfChildren[k]; i++) {
				if (random.nextDouble() <= mutationProbability) {
					mutation(method, k, i);
				}
			}
		}
	}

	private void deathZone(LifeIsBrutalMethod method) {
		for (int i = 0; i < islandsNumber; i++){
			switch (method) {
				case AGE_ELIMINATING -> LifeIsBrutalAlgorithm.ageEliminating(genotypes[i], populationSize + numbersOfChildren[i]);
				case ELITE_PROTECTION -> LifeIsBrutalAlgorithm.eliteProtection(genotypes[i], populationSize + numbersOfChildren[i]);
				default -> LifeIsBrutalAlgorithm.randomEliminating(genotypes[i], populationSize + numbersOfChildren[i]);
			}
		}
	}

	public static void main(String[] args) {
		try {
			String dir = "bestTsp/";
			String[] tspFiles = {"eil51.tsp", "ch150.tsp", /*"berlin52.tsp", "ch130.tsp" ,"kroA200.tsp", "bays29.tsp", "d198.tsp", "eil101.tsp"*/};
			String[] atspFiles = {"ft53.atsp", "ft70.atsp", "ftv33.atsp", "ftv170.atsp", "ry48p.atsp", "rbg323.atsp", "kro124p.atsp", "ftv64.atsp"};
			for (String name : tspFiles) {
				PrintStream ps = new PrintStream(new FileOutputStream(dir + name + ".csv"));
				System.setOut(ps);
				Matrix matrix = FileManager.readFile("data/ALL_tsp/" + name);
				GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(matrix, 300, 6);
//				geneticAlgorithm.start(GeneratingPopulationMethod.TWO_OPT_GENERATION, SelectionMethod.RANDOM_SELECTION, CrossingMethod.CX,MutationMethod.INVERT, LifeIsBrutalMethod.AGE_ELIMINATING);
				geneticAlgorithm.start(GeneratingPopulationMethod.RANDOM_GENERATION, SelectionMethod.TOURNAMENT_SELECTION, CrossingMethod.CX, MutationMethod.NEAREST_NEIGHBOUR, LifeIsBrutalMethod.AGE_ELIMINATING);
				ps.close();
			}
		} catch (FileNotFoundException | WrongFileFormatException e) {
			e.printStackTrace();
		}
	}
}