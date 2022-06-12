package Genetic;

import FileManagerPackage.FileManager;
import FileManagerPackage.WrongFileFormatException;
import Genetic.Enums.*;
import Structure.Matrix;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;

import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Experiment {

	public static void testAllParametersAlone(String name) {

		try {
			String[] arr = name.split("\\.");
			Matrix matrix = FileManager.readFile("data/ALL_" + arr[1] + "/" + arr[0] + "." + arr[1]);;
			int [] populationSizes = {100/*,200,300*/};
			int [] islandsNumbers = {/*2,4,*/6};
			PrintStream stdout = System.out;
			PrintStream ps;
			String dir = arr[1].equals("tsp") ? "TSP_solo/" : "ATSP_solo/";
			for (int populationSize : populationSizes) {
				for (int islandsNumber : islandsNumbers) {
					GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(matrix, populationSize, islandsNumber);
					ps = new PrintStream(new FileOutputStream(dir + name + "RANDOM.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.RANDOM_GENERATION, SelectionMethod.RANDOM_SELECTION,
							CrossingMethod.CX, MutationMethod.INVERT, LifeIsBrutalMethod.RANDOM_ELIMINATING);
					ps.close();
					ps = new PrintStream(new FileOutputStream(dir + name + "INSERT_GENERATION.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.INSERT_GENERATION, SelectionMethod.RANDOM_SELECTION,
							CrossingMethod.CX, MutationMethod.INVERT, LifeIsBrutalMethod.RANDOM_ELIMINATING);
					ps.close();
					ps = new PrintStream(new FileOutputStream(dir + name + "TWO_OPT_GENERATION.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.TWO_OPT_GENERATION, SelectionMethod.RANDOM_SELECTION,
							CrossingMethod.CX, MutationMethod.INVERT, LifeIsBrutalMethod.RANDOM_ELIMINATING);
					ps.close();
					ps = new PrintStream(new FileOutputStream(dir + name + "HYBRID_TWO_OPT_AND_RANDOM.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.HYBRID_TWO_OPT_AND_RANDOM, SelectionMethod.RANDOM_SELECTION,
							CrossingMethod.CX, MutationMethod.INVERT, LifeIsBrutalMethod.RANDOM_ELIMINATING);
					ps.close();
					ps = new PrintStream(new FileOutputStream(dir + name + "TOURNAMENT_SELECTION.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.RANDOM_GENERATION, SelectionMethod.TOURNAMENT_SELECTION,
							CrossingMethod.CX, MutationMethod.INVERT, LifeIsBrutalMethod.RANDOM_ELIMINATING);
					ps.close();
					ps = new PrintStream(new FileOutputStream(dir + name + "ROULETTE_SELECTION.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.RANDOM_GENERATION, SelectionMethod.ROULETTE_SELECTION,
							CrossingMethod.CX, MutationMethod.INVERT, LifeIsBrutalMethod.RANDOM_ELIMINATING);
					ps.close();
					ps = new PrintStream(new FileOutputStream(dir + name + "OX.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.RANDOM_GENERATION, SelectionMethod.RANDOM_SELECTION,
							CrossingMethod.OX, MutationMethod.INVERT, LifeIsBrutalMethod.RANDOM_ELIMINATING);
					ps.close();
					ps = new PrintStream(new FileOutputStream(dir + name + "PMX.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.RANDOM_GENERATION, SelectionMethod.RANDOM_SELECTION,
							CrossingMethod.PMX, MutationMethod.INVERT, LifeIsBrutalMethod.RANDOM_ELIMINATING);
					ps.close();
					ps = new PrintStream(new FileOutputStream(dir + name + "CX2.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.RANDOM_GENERATION, SelectionMethod.RANDOM_SELECTION,
							CrossingMethod.CX2, MutationMethod.INVERT, LifeIsBrutalMethod.RANDOM_ELIMINATING);
					ps.close();
					ps = new PrintStream(new FileOutputStream(dir + name + ".NEAREST_NEIGHBOUR_CROSS.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.RANDOM_GENERATION, SelectionMethod.RANDOM_SELECTION,
							CrossingMethod.NEAREST_NEIGHBOUR, MutationMethod.INVERT, LifeIsBrutalMethod.RANDOM_ELIMINATING);
					ps.close();
					ps = new PrintStream(new FileOutputStream(dir + name + "RANDOM_SWAPS_MUTATION.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.RANDOM_GENERATION, SelectionMethod.RANDOM_SELECTION,
							CrossingMethod.CX, MutationMethod.RANDOM_SWAPS, LifeIsBrutalMethod.RANDOM_ELIMINATING);
					ps.close();
					ps = new PrintStream(new FileOutputStream(dir + name + "RANDOM_MUTATION.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.RANDOM_GENERATION, SelectionMethod.RANDOM_SELECTION,
							CrossingMethod.CX, MutationMethod.RANDOM, LifeIsBrutalMethod.RANDOM_ELIMINATING);
					ps.close();
					ps = new PrintStream(new FileOutputStream(dir + name + "NEAREST_NEIGHBOUR_MUTATION.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.RANDOM_GENERATION, SelectionMethod.RANDOM_SELECTION,
							CrossingMethod.CX, MutationMethod.NEAREST_NEIGHBOUR, LifeIsBrutalMethod.RANDOM_ELIMINATING);
					ps.close();
					ps = new PrintStream(new FileOutputStream(dir + name + "INSERT_MUTATION.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.RANDOM_GENERATION, SelectionMethod.RANDOM_SELECTION,
							CrossingMethod.CX, MutationMethod.INSERT, LifeIsBrutalMethod.RANDOM_ELIMINATING);
					ps.close();
					ps = new PrintStream(new FileOutputStream(dir + name + "ELITE_PROTECTION.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.RANDOM_GENERATION, SelectionMethod.RANDOM_SELECTION,
							CrossingMethod.CX, MutationMethod.INVERT, LifeIsBrutalMethod.ELITE_PROTECTION);
					ps.close();
					ps = new PrintStream(new FileOutputStream(dir + name + "AGE_ELIMINATING.csv"));
					System.setOut(ps);
					geneticAlgorithm.start(GeneratingPopulationMethod.RANDOM_GENERATION, SelectionMethod.RANDOM_SELECTION,
							CrossingMethod.CX, MutationMethod.INVERT, LifeIsBrutalMethod.AGE_ELIMINATING);
					ps.close();
					System.setOut(stdout);
				}
			}
		} catch (FileNotFoundException | WrongFileFormatException e) {
			e.printStackTrace();
		}
	}

	public static void deleteUnnecessary(String[] names) {
		String dir = "RandomGeneration/";
		for (String name : names) {
			try {
				Scanner scanner = new Scanner(new File(dir + name + ".csv"));
				String[] arr = new String[100];
				int counter = 0;
				while (scanner.hasNextLine()) {
					String s = scanner.nextLine();
					if (s.charAt(0) > '9') {
						arr[counter++] = s;
					}
				}
				scanner.close();
				PrintWriter printWriter = new PrintWriter(dir + name + ".csv");
				for (int i = 0; i < 100; i++) {
					printWriter.println(arr[i]);
				}
				printWriter.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

	}

	public static void testRandomParameters(String[] names) throws FileNotFoundException {
		Random random = new Random();
		int[] values = {4, 3, 5, 4, 3};
		int[][] parameters = new int[100][5];
//		for (int i = 0; i < 100; i++) {
//			int k;
//			do {
//				for (int j = 0; j < 5; j++)
//					parameters[i][j] = random.nextInt(values[j]);
//				for (k = 0; k < i; k++) {
//					int j;
//					for (j = 0; j < 5; j++) {
//						if (parameters[k][j] != parameters[i][j])
//							break;
//					}
//					if (j == 5)
//						break;
//				}
//			} while (k < i);
//		}
//		PrintStream psc = new PrintStream(new FileOutputStream( "parameters.csv"));
//		psc.println(Arrays.deepToString(parameters));
//		psc.close();

		try (FileReader fr = new FileReader("parameters.csv"))
		{
			int content;
			int counter = 0;
			while ((content = fr.read()) != -1) {
				if (content >= '0' && content <='9') {
					parameters[counter / 5][counter % 5] = content - 48;
					counter++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String name : names) {
			try {
				String[] arr = name.split("\\.");
				Matrix matrix = FileManager.readFile("data/ALL_" + arr[1] + "/" + arr[0] + "." + arr[1]);
				GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(matrix, 100, 6);

				MutationMethod[] mutationMethods = {MutationMethod.NEAREST_NEIGHBOUR,MutationMethod.INVERT,MutationMethod.INSERT,MutationMethod.RANDOM};
				CrossingMethod[] crossingMethods = {CrossingMethod.PMX,CrossingMethod.CX2,CrossingMethod.CX,CrossingMethod.OX,CrossingMethod.NEAREST_NEIGHBOUR};
				GeneratingPopulationMethod[] generatingPopulationMethods = {GeneratingPopulationMethod.HYBRID_TWO_OPT_AND_RANDOM,GeneratingPopulationMethod.RANDOM_GENERATION,GeneratingPopulationMethod.TWO_OPT_GENERATION,GeneratingPopulationMethod.INSERT_GENERATION};
				LifeIsBrutalMethod[] lifeIsBrutalMethods = {LifeIsBrutalMethod.AGE_ELIMINATING,LifeIsBrutalMethod.RANDOM_ELIMINATING,LifeIsBrutalMethod.ELITE_PROTECTION};
				SelectionMethod[] selectionMethods = {SelectionMethod.TOURNAMENT_SELECTION,SelectionMethod.RANDOM_SELECTION,SelectionMethod.ROULETTE_SELECTION};

				String[] mutationNames = {"NEAREST_NEIGHBOUR ","INVERT ","INSERT ","RANDOM "};
				String[] crossingNames = {"PMX ","CX2 ","CX ","OX ","NEAREST_NEIGHBOUR "};
				String[] generatingPopulationNames = {"HYBRID_TWO_OPT_AND_RANDOM ","RANDOM_GENERATION ","TWO_OPT_GENERATION ","INSERT_GENERATION "};
				String[] lifeIsBrutalNames = {"AGE_ELIMINATING ","RANDOM_ELIMINATING ","ELITE_PROTECTION "};
				String[] selectionNames = {"TOURNAMENT_SELECTION ","RANDOM_SELECTION ","ROULETTE_SELECTION "};

				String dir = "RandomGeneration/";
				PrintStream stdout = System.out;
				PrintStream ps = new PrintStream(new FileOutputStream(dir + name + ".csv"));
				System.setOut(ps);
				int results;
				for (int i = 0; i < 100; i++) {
					geneticAlgorithm.start(generatingPopulationMethods[parameters[i][0]], selectionMethods[parameters[i][1]],
							crossingMethods[parameters[i][2]], mutationMethods[parameters[i][3]], lifeIsBrutalMethods[parameters[i][4]]);

					results = geneticAlgorithm.findBestOfAll().phenotype;

					System.out.println(generatingPopulationNames[parameters[i][0]] + selectionNames[parameters[i][1]]
							+ crossingNames[parameters[i][2]] + mutationNames[parameters[i][3]] + lifeIsBrutalNames[parameters[i][4]] + ";" + results);
				}
				ps.close();
				System.setOut(stdout);
			} catch (WrongFileFormatException | FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	static public void refactorCSV(String[] names, int[] optimalValues, boolean isSymmetric) throws FileNotFoundException {
		String dir = "RandomGeneration/";
		String[] values;
		String[] configuration = new String[100];
		String line;
		double [] statistics = new double[100];

		for (int j = 0; j < names.length; j++) {
			Scanner scanner = new Scanner(new File(dir + names[j] + ".csv"));
			for (int i = 0; i < statistics.length; i++) {
				line = scanner.nextLine();
				values = line.split(";");
				if(configuration[i] == null)
					configuration[i] = values[0];
				statistics[i] += Double.parseDouble(values[1]) / optimalValues[j];
			}
			scanner.close();
		}
		for (int i = 0; i < statistics.length; i++)
			statistics[i] = statistics[i] / names.length;
		PrintWriter printWriter = new PrintWriter(dir + (isSymmetric ? "tsp" : "atsp") + "results.csv");
		for (int i = 0; i < 100; i++) {
			printWriter.println(configuration[i] + ";" + statistics[i]);
		}
		printWriter.close();
	}

	static public void soloParametersAnalizys(String[] names, int[] optimaValues, boolean isSymetric) throws FileNotFoundException {
		String[] files;
		String fileName;
		String regex;
		String[] separated;
		String dir;
		if (isSymetric) {
			File dir_tsp = new File("TSP_solo");
			files = dir_tsp.list();
			fileName = "arraysTSP.csv";
			regex = ".tsp";
			dir = "TSP_solo/";
		} else {
			File dir_tsp = new File("ATSP_solo");
			files = dir_tsp.list();
			fileName = "arraysATSP.csv";
			regex = ".atsp";
			dir = "ATSP_solo/";
		}

		if (files != null) {
			PrintWriter printWriter = new PrintWriter(fileName);
			for (String file : files) {
				String line = "";
				String[] temp;
				separated = file.split(regex);
				if (separated[1].charAt(0) == '.')
					separated[1] = separated[1].substring(1);
				separated[1] = separated[1].substring(0, separated[1].length() - 4);
				Scanner scanner = new Scanner(new File(dir + file));

				while (scanner.hasNextLine())
					line = scanner.nextLine();

				temp = line.split(";");

				double value = Double.parseDouble(temp[1]);
				int instanceNumberRepresentation = switch (separated[0]) {
					case "ch150", "ft70" -> 1;
					case "berlin52", "ftv33" -> 2;
					case "ch130", "ftv170" -> 3;
					case "kroA200", "ry48p" -> 4;
					case "bays29", "rbg323" -> 5;
					case "d198", "kro124p" -> 6;
					case "eil101", "ftv64" -> 7;
					default -> 0;
				};

				System.out.println(separated[0] + ";" + separated[1] + ";" + value / optimaValues[instanceNumberRepresentation]);
				printWriter.println(separated[0] + ";" + separated[1] + ";" + value / optimaValues[instanceNumberRepresentation]);
			}
			printWriter.close();

		}
	}

	public static void WilcoxonTestForCrossingMethods() {
		WilcoxonSignedRankTest wilcoxonSignedRankTest = new WilcoxonSignedRankTest();
		double[] tsp_results = new double[10];
		double[] tsp_cx = {1.0069, 1.0595, 1.1326, 1.0752, 1.0425, 1.0763, 1.0352, 1.1364};
		double[] tsp_ox = {1.0510, 1.3360, 2.1003, 2.7529, 2.5924, 2.0238, 1.3897, 2.5180};
		double[] tsp_pmx = {1.3960, 2.1851, 5.9056, 6.4203, 8.9786, 3.8903, 2.3380, 9.3788};
		double[] tsp_NN = {1.4347, 2.2381, 5.8108, 6.5892, 8.9596, 3.8983, 2.1784, 9.2129};
		double[] tsp_cx2 = {1.3881, 2.1583, 5.9555, 6.5334, 9.2257, 4.0795, 2.3685, 9.2350};
		double[][] tsp_data = {tsp_cx, tsp_ox, tsp_pmx, tsp_NN, tsp_cx2};
		int counter = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = i + 1; j < 5; j++) {
				tsp_results[counter++] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(tsp_data[i], tsp_data[j], true);
			}
		}

		double[] atsp_results = new double[10];
		double[] atsp_cx = {2.28993483, 1.514002017, 7.753901996, 1.804821151, 3.032082654, 3.6230748, 4.047511312, 1.841769519};
		double[] atsp_ox = {1.621288921, 1.310009567, 4.480580762, 1.227838258, 1.763458401, 2.472812586, 3.426093514, 1.513243656};
		double[] atsp_pmx = {2.365097755, 1.5144416, 7.701270417, 1.793934681, 3.10331702, 3.766050235, 4.083710407, 2.069615865};
		double[] atsp_NN = {2.304272266, 1.506425672, 7.885662432, 1.687402799, 3.232735182, 3.771956942, 4.049019608, 1.962834558};
		double[] atsp_cx2 = {2.402751629, 1.519432162, 7.866061706, 1.8281493, 3.346927678, 3.921611924, 4.08974359, 2.155179587};
		double[][] atsp_data = {atsp_cx, atsp_ox, atsp_pmx, atsp_NN, atsp_cx2};
		counter = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = i + 1; j < 5; j++) {
				atsp_results[counter++] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(atsp_data[i], atsp_data[j], true);
			}
		}
		System.out.println("\nWilcoxon test results:\n" + Arrays.toString(atsp_results) + "\n" + Arrays.toString(tsp_results));
	}


	public static void main(String[] args) {
		String[] tspFiles = {"eil51.tsp", "ch150.tsp", "berlin52.tsp", "ch130.tsp" ,"kroA200.tsp", "bays29.tsp", "d198.tsp", "eil101.tsp"};
		String[] atspFiles = {"ft53.atsp", "ft70.atsp", "ftv33.atsp", "ftv170.atsp", "ry48p.atsp", "rbg323.atsp", "kro124p.atsp", "ftv64.atsp"};

		int [] atspOptimalSolutions = {6905, 38673, 1286, 2755, 14422, 1326, 36230, 1839};
		int [] tspOptimalSolutions = {426, 6528, 7542, 6110, 29368, 2020, 15780, 629};

//		for (String s: tspFiles)
//			testAllParametersAlone(s);
//		for (String s: atspFiles)
//			testAllParametersAlone(s);
		try {
			testRandomParameters(atspFiles);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
//		testRandomParameters(atspFiles);
		/*try {
//			deleteNotNecessary(atspFiles);
			refactorCSV(tspFiles, tspOptimalSolutions,true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
		/*try {
			soloParametersAnalizys(tspFiles, tspOptimalSolutions, true);
			soloParametersAnalizys(atspFiles, atspOptimalSolutions, false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}*/
//		WilcoxonTestForCrossingMethods();
	}
}
