package Structure;

import java.util.Random;

final public class Euklides extends Matrix {
	private double[][] euclid;

	public Euklides(int n, double[][] euclid) {
		dimension = n;
		this.euclid = euclid;
		euclidToMatrix();
	}

	public Euklides(int n, double[][] euclid, int[][] matrix) {
		dimension = n;
		this.euclid = euclid;
		this.matrix = matrix;
	}

	public Euklides(){}

	public void randomGenerateEuklides(int newDimension, double limit)
	{
		dimension = newDimension;
		Random random = new Random();
		euclid = new double[newDimension][2];
		for(int i = 0;i<newDimension;i++) {
			euclid[i][0] = random.nextDouble() * limit;
			euclid[i][1] = random.nextDouble() * limit;
		}
		euclidToMatrix();
	}

	public void euclidToMatrix() {
		matrix = new int[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			matrix[i][i] = 0;
			for (int j = i + 1; j < dimension; j++) {
				matrix[i][j] = (int) Math.sqrt(Math.pow(euclid[i][0] - euclid[j][0], 2) + Math.pow(euclid[i][1] - euclid[j][1], 2));
				matrix[j][i] = matrix[i][j];
			}
		}
	}

	public double getX(int position){return euclid[position][0];}

	public double getY(int position){return euclid[position][1];}
}

