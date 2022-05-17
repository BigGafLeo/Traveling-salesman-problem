package Genetic;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CrossingOperatorsTest {


	@Test
	public void crossingTestOX() {
		int [] firstParent = {3,4,8,2,7,1,6,5};
		int [] secondParent = {4,2,5,1,6,8,3,7};
		int [] result = {5,6,8,2,7,1,3,4};
		System.out.println(Arrays.toString(result));
		System.out.println(Arrays.toString(CrossingOperators.OX(firstParent, secondParent)));
		assertArrayEquals(result, CrossingOperators.OX(firstParent, secondParent));
	}

	@Test
	public void crossingTestCX() {
		int [] firstParent = {1,2,3,4,5,6,7,8};
		int [] secondParent = {8,5,2,1,3,6,4,7};
		int [] result = {1,5,2,4,3,6,7,8};
		System.out.println(Arrays.toString(result));
		System.out.println(Arrays.toString(CrossingOperators.CX(firstParent, secondParent)));
		assertArrayEquals(result, CrossingOperators.CX(firstParent, secondParent));
	}
}