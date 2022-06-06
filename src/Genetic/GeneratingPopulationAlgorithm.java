package Genetic;

import ProblemSolver.ProblemSolver;
import Structure.Matrix;

public class GeneratingPopulationAlgorithm {

	private static int populationSize;
	private static Genotype[][] genotypes;
	private static ProblemSolver problemSolver;
	private static int localSearchIterationsNumber;
	private static Matrix matrix;

	public static void randomIslandGeneration(int k) {
		for (int i = 0; i < populationSize * 2 ; i++) {
			problemSolver.randomPermutation();
			genotypes[k][i] = new Genotype(matrix);
			genotypes[k][i].setGenotype(problemSolver.getSolution());
		}
	}

	public static void twoOptIslandGeneration(int k){
		randomIslandGeneration(k);
		for (int i = 0; i < populationSize; i++) {
			problemSolver.setSolution(genotypes[k][i].genotype);
			for (int j = 0; j < localSearchIterationsNumber; j++) {
				problemSolver.twoOpt(false);
			}
			genotypes[k][i].setGenotype(problemSolver.getSolution());
		}

	}

	public static void swapIslandGeneration(int k) {
		randomIslandGeneration(k);
		for (int i = 0; i < populationSize; i++) {
			problemSolver.setSolution(genotypes[k][i].genotype);
			for (int j = 0; j < localSearchIterationsNumber; j++) {
				problemSolver.swap(false);
			}
			genotypes[k][i].setGenotype(problemSolver.getSolution());
		}
	}

	public static void insertIslandGeneration(int k){
		randomIslandGeneration(k);
		for (int i = 0; i < populationSize; i++) {
			problemSolver.setSolution(genotypes[k][i].genotype);
			for (int j = 0; j < localSearchIterationsNumber; j++) {
				problemSolver.insert(false);
			}
			genotypes[k][i].setGenotype(problemSolver.getSolution());
		}
	}

	public static void hybridIslandGeneration(int k) {
		randomIslandGeneration(k);
		for (int i = 0; i < populationSize / 5; i++) {
			problemSolver.setSolution(genotypes[k][i].genotype);
			problemSolver.twoOpt(true);
			genotypes[k][i].setGenotype(problemSolver.getSolution());
		}
	}

	public static void setParameters(Genotype[][] genotypes, int populationSize, ProblemSolver problemSolver, Matrix matrix) {
		GeneratingPopulationAlgorithm.genotypes = genotypes;
		GeneratingPopulationAlgorithm.populationSize = populationSize;
		GeneratingPopulationAlgorithm.problemSolver = problemSolver;
		GeneratingPopulationAlgorithm.matrix = matrix;
		GeneratingPopulationAlgorithm.localSearchIterationsNumber = matrix.getDimension() / 5;
	}
}
