package Genetic;

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
		System.out.println(Arrays.toString(CrossingMethods.OX(firstParent, secondParent)));
		assertArrayEquals(result, CrossingMethods.OX(firstParent, secondParent));
	}

	@Test
	public void crossingTestCX() {
		int [] firstParent = {1,2,3,4,5,6,7,8};
		int [] secondParent = {8,5,2,1,3,6,4,7};
		int [] result = {1,5,2,4,3,6,7,8};
		System.out.println(Arrays.toString(result));
		System.out.println(Arrays.toString(CrossingMethods.CX(firstParent, secondParent)));
		assertArrayEquals(result, CrossingMethods.CX(firstParent, secondParent));
	}

	@Test
	public void crossingTestCX2() {
		int [] firstParent = {1,2,3,4,5,6,7,8};
		int [] secondParent = {2,7,5,8,4,1,6,3};
		int [][] result = {{2,1,6,7,5,3,8,4}, {6,7,2,1,8,4,5,3}};

//		int [] firstParent = {3,4,8,2,7,1,6,5};
//		int [] secondParent = {4,2,5,1,6,8,3,7};
//		int [][] result = {{4,8,6,2,5,3,1,7,}, {1,7,4,8,6,2,5,3}};

		int [][] generated = CrossingMethods.CX2(firstParent, secondParent);
		System.out.println(Arrays.toString(result[1]));
		System.out.println(Arrays.toString(result[0]));
		System.out.println(Arrays.toString(generated[0])+ "\n" +Arrays.toString(generated[1]));
		assertArrayEquals(result, CrossingMethods.CX2(firstParent, secondParent));
	}
}