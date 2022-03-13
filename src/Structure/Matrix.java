package Structure;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

final public class Matrix extends Structure {


	private int[][] matrix;


	public Matrix(int n) {
		this.dimension = n;
		matrix = new int[n][n];
	}


}
