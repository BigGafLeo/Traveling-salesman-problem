package Structure;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Scanner;

public class Structure {

	protected int dimension;

//	public void readFile(String dir) throws FileNotFoundException {
//		Scanner sc = new Scanner(new File(dir));
//		String in = sc.nextLine();
//		while (in != null)
//		{
//			String[] arr = in.split(" ");
//			if (in.startsWith("NAME")) {
//				nameProblem = arr[1];
//			} else if (in.startsWith("TYPE")) {
//				type =  arr[1];
//				if (!Objects.equals(type, "ATSP") && !Objects.equals(type, "TSP")) {
//					break;
//				}
//			} else if (in.startsWith("DIMENSION")) {
//				dimension =  Integer.parseInt(arr[1]);
//			} else if (in.startsWith("EDGE_WEIGHT_TYPE")) {
//				edge_weight_type = arr[1];
//			} else if (in.startsWith("NODE_COORD_SECTION")) {
//
//			} else if (in.startsWith("EDGE_WEIGHT_SECTION")) {
//
//			}
//			in = sc.nextLine();
//		}
//	}
	static public void writeData(Structure structure, String dir) throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(dir);

		if(structure instanceof Euklides) {
			writer.println("TYPE : TSP");
			writer.println("DIMENSION : "+structure.dimension);
			writer.println("EDGE_WEIGHT_TYPE : EUC_2D");
			writer.println("NODE_COORD_SECTION");
			for(int i = 0;i < structure.dimension;i++)
				writer.println((i+1)+" "+((Euklides) structure).getX(i)+" "+ ((Euklides) structure).getX(i));
		} else {
			writer.println("TYPE : " + (((Matrix) structure).isSymmetric() ? "TSP" : "ATSP"));
			writer.println("DIMENSION : "+structure.dimension);
			writer.println("EDGE_WEIGHT_TYPE : EXPLICIT");
			writer.println("EDGE_WEIGHT_FORMAT : FULL_MATRIX");
			writer.println("EDGE_WEIGHT_SECTION");
			for (int i = 0; i < structure.dimension; i++) {
				for (int j = 0; j < structure.dimension; j++) {
					writer.print(((Matrix) structure).get(i, j) + " ");
				}
				writer.println();
			}
		}
		writer.close();
	}
}
