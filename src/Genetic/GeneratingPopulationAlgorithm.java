package Genetic;

import ProblemSolver.ProblemSolver;

public class GeneratingPopulationAlgorithm {

	private static int populationSize;
	private static int[][][] genotypes;
	private static ProblemSolver problemSolver;

	public static void randomIslandGeneration(int k) {
		for (int i = 0; i < populationSize; i++) {
			problemSolver.randomPermutation();
			genotypes[k][i] = problemSolver.getSolution();
		}
	}

	public static void twoOptIslandGeneration(int k){
		randomIslandGeneration(k);
		for (int i = 0; i < populationSize; i++) {
			problemSolver.setSolution(genotypes[k][i]);
			problemSolver.twoOpt(true);
			genotypes[k][i] = problemSolver.getSolution();
		}

	}

	public static void swapIslandGeneration(int k) {
		randomIslandGeneration(k);
		for (int i = 0; i < populationSize; i++) {
			problemSolver.setSolution(genotypes[k][i]);
			problemSolver.swap(true);
			genotypes[k][i] = problemSolver.getSolution();
		}
	}

	public static void insertIslandGeneration(int k){
		randomIslandGeneration(k);
		for (int i = 0; i < populationSize; i++) {
			problemSolver.setSolution(genotypes[k][i]);
			problemSolver.insert(true);
			genotypes[k][i] = problemSolver.getSolution();
		}
	}

	public static void hybridIslandGeneration(int k) {
		randomIslandGeneration(k);
		for (int i = 0; i < populationSize / 5; i++) {
			problemSolver.setSolution(genotypes[k][i]);
			problemSolver.twoOpt(true);
			genotypes[k][i] = problemSolver.getSolution();
		}
	}

	public static void setParameters(int[][][] genotypes, int populationSize, ProblemSolver problemSolver) {
		GeneratingPopulationAlgorithm.genotypes = genotypes;
		GeneratingPopulationAlgorithm.populationSize = populationSize;
		GeneratingPopulationAlgorithm.problemSolver = problemSolver;
	}
}
