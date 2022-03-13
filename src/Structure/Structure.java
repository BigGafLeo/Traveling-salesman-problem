package Structure;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Structure {

	protected String nameProblem;
	protected String type;
	protected String format;
	protected int dimension;

	public void readFile(String dir) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(dir));
		String in = sc.nextLine();
		while(in!=null)
		{
			if (in.startsWith("NAME")) {
				String arr[] = in.split(" ");
				nameProblem = arr[1];
			} else if (in.startsWith("TYPE")) {
				String arr[] = in.split(" ");
				type =  arr[1];
			} else if (in.startsWith("DIMENSION")) {
				String arr[] = in.split(" ");
				dimension =  Integer.parseInt(arr[1]);
			} else
		}
	}
}
