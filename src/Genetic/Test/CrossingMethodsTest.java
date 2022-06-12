package Genetic.Test;

import Genetic.CrossingAlgorithms;
import Structure.Matrix;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CrossingMethodsTest {

	@Test
	public void crossingTestOX() {
		int [] firstParent = {3,4,8,2,7,1,6,5};
		int [] secondParent = {4,2,5,1,6,8,3,7};
		int [] result = {5,6,8,2,7,1,3,4};
		System.out.println(Arrays.toString(result));
		System.out.println(Arrays.toString(CrossingAlgorithms.OX(firstParent, secondParent)));
		assertArrayEquals(result, CrossingAlgorithms.OX(firstParent, secondParent));
	}

	@Test
	public void crossingTestCX() {
		int [] firstParent = {1,2,3,4,5,6,7,8};
		int [] secondParent = {8,5,2,1,3,6,4,7};
		int [] result = {1,5,2,4,3,6,7,8};
		System.out.println(Arrays.toString(result));
		System.out.println(Arrays.toString(CrossingAlgorithms.CX(firstParent, secondParent)));
		assertArrayEquals(result, CrossingAlgorithms.CX(firstParent, secondParent));
	}

	@Test
	public void crossingTestCX2() {
		int [] firstParent = {1,2,3,4,5,6,7,8};
		int [] secondParent = {2,7,5,8,4,1,6,3};
		int [][] result = {{2,1,6,7,5,3,8,4}, {6,7,2,1,8,4,5,3}};

//		int [] firstParent = {3,4,8,2,7,1,6,5};
//		int [] secondParent = {4,2,5,1,6,8,3,7};
//		int [][] result = {{4,8,6,2,5,3,1,7,}, {1,7,4,8,6,2,5,3}};

		int [][] generated = CrossingAlgorithms.CX2(firstParent, secondParent);
		System.out.println(Arrays.toString(result[1]));
		System.out.println(Arrays.toString(result[0]));
		System.out.println(Arrays.toString(generated[0])+ "\n" +Arrays.toString(generated[1]));
		assertArrayEquals(result, CrossingAlgorithms.CX2(firstParent, secondParent));
	}

	@Test
	public void nearestNeighbourCrossTest() {
		int[][] tMatrix = {
				{0, 5, 2, 4, 1},
				{5, 0, 3, 1, 1},
				{2, 3, 0, 4, 2},
				{4, 1, 4, 0, 5},
				{1, 1, 2, 5, 0}
		};
		Matrix matrix = new Matrix(5,tMatrix,true);
		int [] firstParent = {1,2,3,4,5};
		int [] secondParent = {2,5,4,1,3};
		int [] iWant = {1,5,2,3,4};
		int [] result = CrossingAlgorithms.nearestNeighbourCross(firstParent, secondParent, matrix, 0);
		System.out.println(Arrays.toString(iWant));
		System.out.println(Arrays.toString(result));
		assertArrayEquals(iWant,result);
	}
}