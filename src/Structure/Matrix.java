package Structure;

import java.util.Random;

public class Matrix {

	protected int dimension;
	protected int[][] matrix;
	private boolean symmetric;

	public Matrix(int n, int[][] matrix) {
		this.dimension = n;
		this.matrix = matrix;
	}

	public Matrix(){}

	public void randomSymmetricGenerateMatrix(int newDimension, int limit) {
		dimension = newDimension;
		symmetric = true;
		Random random = new Random();
		matrix = new int[newDimension][newDimension];
		for (int i = 0; i < newDimension; i++) {
			matrix[i][i] = 0;
			for (int j = i + 1; j < newDimension; j++) {
				matrix[i][j] = random.nextInt(limit);
				matrix[j][i] = matrix[i][j];
			}
		}
	}

	public void randomAsymmetricGenerateMatrix(int newDimension, int limit) {
		dimension = newDimension;
		symmetric = false;
		Random random = new Random();
		matrix = new int[newDimension][newDimension];
		for (int i = 0; i < newDimension; i++) {
			matrix[i][i] = 0;
			for (int j = i + 1; j < newDimension; j++) {
				matrix[i][j] = random.nextInt(limit);
				matrix[j][i] = random.nextInt(limit);
			}
		}
	}

	public int distance(int[] tab) {
		int distance = 0;
		for (int i = 1; i < dimension; i++) {
			distance += matrix[tab[i-1]][tab[i]];
		}
		distance += matrix[tab[dimension - 1]][tab[0]];
		return distance;
	}

	public int get(int i, int j) {
		return matrix[i][j];
	}

	public boolean isSymmetric() {
		return symmetric;
	}

	public int getDimension() {
		return dimension;
	}
}
