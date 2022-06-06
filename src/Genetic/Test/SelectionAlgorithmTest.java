package Genetic.Test;

import Genetic.SelectionAlgorithm;
import org.junit.jupiter.api.Test;
import Structure.Matrix;
import static org.junit.jupiter.api.Assertions.*;

class SelectionAlgorithmTest {

	int[][] genotypes = {{5, 2, 4, 1, 6, 3}, {1, 2, 4, 6, 3, 5}, {2, 6, 5, 1, 3, 4}, {1, 2, 3, 4, 5, 6}, {3, 2, 1, 5, 6, 4}};

	private Matrix matrix;

	public void setMatrix(int newDimension, int limit) {
		this.matrix = new Matrix();
		matrix.randomAsymmetricGenerateMatrix(newDimension, limit);
	}

	@Test
	void randomSelection() {
	}

	@Test
	void ruletSelection() {
	}

	@Test
	void tournamentSelection() {
		setMatrix(6, 10);
		SelectionAlgorithm.setMatrix(matrix);

		for (int k = 1; k < 6; k++) {
			//assertArrayEquals(SelectionAlgorithm.tournamentSelection(genotypes, 0.8, k), new int[] {k, k, k, k, k});
		}
	}
}