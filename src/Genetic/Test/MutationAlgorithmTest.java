package Genetic.Test;

import Genetic.MutationAlgorithm;
import Structure.Matrix;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MutationAlgorithmTest {

	@Test
	public void nearestNeighbourMutationAlgorithmTest(){
		int[][] tMatrix = {
				{0, 5, 2, 4, 1, 3, 7, 8},
				{5, 0, 3, 1, 1, 2, 6, 4},
				{2, 3, 0, 4, 2, 1, 5, 7},
				{4, 1, 4, 0, 5, 6, 2, 1},
				{1, 1, 2, 5, 0, 4, 2, 3},
				{3, 2, 1, 6, 4, 0, 5, 1},
				{7, 6, 5, 2, 2, 5, 0, 1},
				{8, 4, 7, 1, 3, 1, 1, 0}
		};
		Matrix matrix = new Matrix(8,tMatrix,true);
		int [] genotype = {1,4,7,2,6,8,3,5};
		MutationAlgorithm.nearestNeighbourMutationAlgorithm(genotype, matrix);
		System.out.println(Arrays.toString(genotype));
	}

}