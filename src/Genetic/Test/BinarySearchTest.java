package Genetic.Test;

import Genetic.BinarySearch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTest {


	@Test
	public void testBinarySearch(){
		double[] testing = {0.15,0.20,0.30,0.45678,0.9,1.0};
		Assertions.assertEquals(BinarySearch.binarySearch(testing, 0, 0, 5), 0);

	}
}