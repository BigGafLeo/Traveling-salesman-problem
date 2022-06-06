package FileManagerPackage;

import Structure.Euklides;
import Structure.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Scanner;

public class FileManager {

	static public Matrix readFile(String dir) throws FileNotFoundException, WrongFileFormatException {
		int[][] matrix = null;
		double[][] euclid = null;
		int dimension = 0;
		Scanner sc = new Scanner(new File(dir));
		String format = "", edge_weight_type = "", type = "";
		while (sc.hasNextLine()) {
			String in = sc.nextLine();
			String[] arr = in.split(" ");
			if (in.startsWith("TYPE")) {
				type = arr[arr.length - 1];
				if (!Objects.equals(type, "ATSP") && !Objects.equals(type, "TSP")) {
					break;
				}
			} else if (in.startsWith("DIMENSION")) {
				dimension =  Integer.parseInt(arr[arr.length - 1]);
			} else if (in.startsWith("EDGE_WEIGHT_TYPE")) {
				edge_weight_type = arr[arr.length-1];
				if (!Objects.equals(edge_weight_type, "EUC_2D") && !Objects.equals(edge_weight_type, "EXPLICIT")) {
					break;
				}
			} else if (in.startsWith("EDGE_WEIGHT_FORMAT") && Objects.equals(edge_weight_type, "EXPLICIT")) {
				format = arr[arr.length-1];
				if (!Objects.equals(format, "FULL_MATRIX") && !Objects.equals(format, "LOWER_DIAG_ROW")) {
					break;
				}
			} else if ((in.startsWith("NODE_COORD_SECTION") || in.startsWith("DISPLAY_DATA_SECTION")) && dimension > 0) {
				euclid = new double[dimension][2];
				for (int i = 0; i < dimension; i++) {
					sc.nextInt();
					euclid[i][0] = Double.parseDouble(sc.next());//sc.nextDouble();
					euclid[i][1] = Double.parseDouble(sc.next());//sc.nextDouble();
				}
			} else if (in.startsWith("EDGE_WEIGHT_SECTION") && dimension > 0) {
				if (Objects.equals(format, "FULL_MATRIX")) {
					matrix = new int[dimension][dimension];
					for (int i = 0; i < dimension; i++) {
						for (int j = 0; j < dimension; j++) {
							matrix[i][j] = sc.nextInt();
						}
					}
				} else if (Objects.equals(format, "LOWER_DIAG_ROW")) {
					matrix = new int[dimension][dimension];
					for (int i = 0; i < dimension; i++) {
						for (int j = 0; j <= i; j++) {
							matrix[i][j] = sc.nextInt();
							matrix[j][i] = matrix[i][j];
						}
					}
				}
			}
		}
		if (matrix == null && euclid == null) {
			throw new WrongFileFormatException();
		}
		if (euclid != null) {
			if (matrix == null) {
				return new Euklides(dimension, euclid);
			}
			return new Euklides(dimension, euclid, matrix);
		}
		return new Matrix(dimension, matrix, type.equals("TSP"));
	}

	static public void writeData(Matrix structure, String dir) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(dir);

		int dimension = structure.getDimension();
		if(structure instanceof Euklides) {
			writer.println("TYPE : TSP");
			writer.println("DIMENSION : " + dimension);
			writer.println("EDGE_WEIGHT_TYPE : EUC_2D");
			writer.println("NODE_COORD_SECTION");
			for(int i = 0; i < dimension; i++)
				writer.println((i+1)+" "+((Euklides) structure).getX(i)+" "+ ((Euklides) structure).getX(i));
		} else {
			writer.println("TYPE : " + (structure.isSymmetric() ? "TSP" : "ATSP"));
			writer.println("DIMENSION : " + dimension);
			writer.println("EDGE_WEIGHT_TYPE : EXPLICIT");
			writer.println("EDGE_WEIGHT_FORMAT : FULL_MATRIX");
			writer.println("EDGE_WEIGHT_SECTION");
			for (int i = 0; i < dimension; i++) {
				for (int j = 0; j < dimension; j++) {
					writer.print(structure.get(i, j) + " ");
				}
				writer.println();
			}
		}
		writer.close();
	}

}
