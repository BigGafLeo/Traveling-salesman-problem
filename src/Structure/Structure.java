package Structure;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class Structure {

	protected String nameProblem;
	protected String type;
	protected String edge_weight_type;
	protected String format;
	protected int dimension;

	public void readFile(String dir) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(dir));
		String in = sc.nextLine();
		while (in != null)
		{
			String[] arr = in.split(" ");
			if (in.startsWith("NAME")) {
				nameProblem = arr[1];
			} else if (in.startsWith("TYPE")) {
				type =  arr[1];
				if (!Objects.equals(type, "ATSP") && !Objects.equals(type, "TSP")) {
					break;
				}
			} else if (in.startsWith("DIMENSION")) {
				dimension =  Integer.parseInt(arr[1]);
			} else if (in.startsWith("EDGE_WEIGHT_TYPE")) {
				edge_weight_type = arr[1];
			} else if (in.startsWith("NODE_COORD_SECTION")) {

			} else if (in.startsWith("EDGE_WEIGHT_SECTION")) {

			}
			in = sc.nextLine();
		}
	}
}
