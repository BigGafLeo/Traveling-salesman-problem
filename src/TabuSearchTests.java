import ProblemSolver.AsymmetricProblemSolver;
import Structure.Matrix;
import ProblemSolver.ProblemSolver;
import ProblemSolver.SymmetricProblemSolver;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Objects;

public class TabuSearchTests {

    public static void testTabuSearch(String name, int startingOption) {
        PrintStream ps;
        try {
            String[] arr = name.split("\\.");
            Matrix structure = FileManager.readFile("data/ALL_" + arr[1] + "/" + arr[0] + "." + arr[1]);
            int [][] results = new int[8][];
            ProblemSolver problemSolver = Objects.equals(arr[1], "tsp") ? new SymmetricProblemSolver(structure)
                    : new AsymmetricProblemSolver(structure);
            if (startingOption == 0) {
                problemSolver.randomPermutation();
            } else {
                problemSolver.nearestNeighbour();
            }
            int[] initialSolution = problemSolver.getSolution();
            PrintStream stdout = System.out;
            int counter = 0;
            for (boolean b : new boolean[]{true/*, false*/}) {
                for (int i = 0; i < (structure.getDimension() < 2000 ? 4 : 3); i++) {
                    results[counter++] = problemSolver.tabuSearch(i == 2 ? -1 : i, b);
                    problemSolver.setSolution(initialSolution);
                }
            }
            ps = new PrintStream(new FileOutputStream(name + (startingOption == 0 ? "" : "NN") + ".csv"));
            System.setOut(ps);
            System.out.println("second;swapAC;insertAC;invertAC;" + (structure.getDimension() < 2000 ? "3optAC;" : "")
                    /*+ "swap;insert;invert" + (structure.getDimension() < 2000 ? ";3opt" : "")*/);
            for (int i = 0; i < results[0].length; i++) {
                for (int j = 0; j < (structure.getDimension() < 2000 ? /*8 : 6*/ 4 : 3); j++) {
                    if (results[j][i] == 0) {
                        if (i > 0)
                            results[j][i] = results[j][i - 1];
                        else
                            results[j][0] = problemSolver.getDistance();
                    }
                }
                System.out.print((i + 1));
                for (int j = 0; j < (structure.getDimension() < 2000 ? /*8 : 6*/ 4 : 3); j++) {
                    System.out.print(";" + results[j][i]);
                }
                System.out.println();
            }
            System.setOut(stdout);
        } catch (FileNotFoundException | WrongFileFormatException e) {
            e.printStackTrace();
        }
    }

    public static void compare() {
//        File dir_atsp = new File("data/ALL_atsp");
//        String[] atsp_files = dir_atsp.list();
//        File dir_tsp = new File("data/ALL_tsp");
//        String[] tsp_files = dir_tsp.list();
//
//
//        //[0] - swap
//        //[1] - insert
//        //[2] - 2-opt
//        //[3] - 3-opt
//        assert tsp_files != null;
//        double[][] tsp_distance = new double[4][10];
//        assert atsp_files != null;
//        double[][] atsp_distance = new double[4][18];
//        Matrix matrix;
//        AsymmetricProblemSolver asymmetricProblemSolver;
//        SymmetricProblemSolver symmetricProblemSolver;
//
//        System.out.println("\nAsymmetric problem:");
//        for (int i = 0 ; i < atsp_distance[1].length; i++) {
//            try {
//                matrix = FileManager.readFile("data/ALL_atsp/" + atsp_files[i]);
//                asymmetricProblemSolver = new AsymmetricProblemSolver(matrix);
//                asymmetricProblemSolver.tabuSearch(0, true);
//                atsp_distance[0][i] = asymmetricProblemSolver.getDistance();
//                asymmetricProblemSolver.tabuSearch(1, true);
//                atsp_distance[1][i] = asymmetricProblemSolver.getDistance();
//                asymmetricProblemSolver.tabuSearch(-1, true);
//                atsp_distance[2][i] = asymmetricProblemSolver.getDistance();
//                asymmetricProblemSolver.tabuSearch(3, true);
//                atsp_distance[3][i] = asymmetricProblemSolver.getDistance();
//                System.out.println(atsp_distance[0][i] + " " + atsp_distance[1][i] + " " + atsp_distance[2][i] + " " + atsp_distance[3][i]);
//            } catch (FileNotFoundException | WrongFileFormatException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println("\nSymmetric problem:");
//        for (int i = 0 ; i < tsp_distance[1].length; i++) {
//            try {
//                matrix = FileManager.readFile("data/ALL_tsp/" + tsp_files[i]);
//                symmetricProblemSolver = new SymmetricProblemSolver(matrix);
//                symmetricProblemSolver.tabuSearch(0, true);
//                tsp_distance[0][i] = symmetricProblemSolver.getDistance();
//                symmetricProblemSolver.tabuSearch(1, true);
//                tsp_distance[1][i] = symmetricProblemSolver.getDistance();
//                symmetricProblemSolver.tabuSearch(-1, true);
//                tsp_distance[2][i] = symmetricProblemSolver.getDistance();
//                symmetricProblemSolver.tabuSearch(3, true);
//                tsp_distance[3][i] = symmetricProblemSolver.getDistance();
//                System.out.println(tsp_distance[0][i] + " " + tsp_distance[1][i] + " " + tsp_distance[2][i] + " " + tsp_distance[3][i]);
//            } catch (FileNotFoundException | WrongFileFormatException e) {
//                e.printStackTrace();
//            }
//        }
        WilcoxonSignedRankTest wilcoxonSignedRankTest = new WilcoxonSignedRankTest();
        //[0] - swap
        //[1] - insert
        //[2] - 2-opt
        //[3] - 3-opt
        double tsp_swap[] = {7492,120323,414,7526,7009,73631,635,339075,121660,813778};
        double tsp_insert[] = {6848,73083,412,7526,6854,49466,619,615641,69346,1664693};
        double tsp_invert[] = {6721,52154,417,7526,6206,36664,632,24140,45499,171399};
        double tsp_opt[] = {6597,842060,418,7526,6246,401517,632,1329976,850785,3912840};

        double atsp_swap[] = {1286,1990,3835,1632,39,1618,1840,36500,2952,14518};
        double atsp_insert[] = {1286,1959,3474,1534,39,1609,1850,36714,2767,14429};
        double atsp_invert[] = {1433,3933,9305,3756,39,2751,3347,46039,5561,14776};
        double atsp_opt[] = {1376,2090,3702,4704,39,1710,1937,39069,7679,14466};

        double[] tsp_results = new double[6];
        tsp_results[0] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(tsp_swap, tsp_insert, true);
        tsp_results[1] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(tsp_swap, tsp_invert, true);
        tsp_results[2] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(tsp_swap, tsp_opt, true);
        tsp_results[3] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(tsp_insert, tsp_invert, true);
        tsp_results[4] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(tsp_insert, tsp_opt, true);
        tsp_results[5] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(tsp_invert, tsp_opt, true);

        double[] atsp_results = new double[6];
        atsp_results[0] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(atsp_swap, atsp_insert, true);
        atsp_results[1] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(atsp_swap, atsp_invert, true);
        atsp_results[2] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(atsp_swap, atsp_opt, true);
        atsp_results[3] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(atsp_insert, atsp_invert, true);
        atsp_results[4] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(atsp_insert, atsp_opt, true);
        atsp_results[5] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(atsp_invert, atsp_opt, true);

        System.out.println("\nWilcoxon test results:\n" + Arrays.toString(atsp_results) + "\n" + Arrays.toString(tsp_results));
    }

    public static void main(String[] args) {
        String[] files = {/*"eil51.tsp", "ch150.tsp", "d657.tsp", "u2152.tsp", "ftv33.atsp", "ftv70.atsp", "ftv170.atsp", */"rbg323.atsp"};
//        String[] files = {"berlin52.tsp", "ch130.tsp", "d493.tsp", "eil101.tsp", "fl1577.tsp","u1432.tsp", "u724.tsp", "ftv33.atsp", "ftv70.atsp", "ftv170.atsp", "rbg323.atsp", "br17.atsp", "ftv55.atsp", "ftv64.atsp", "ry48p.atsp", "kro124p.atsp", "rbg443.atsp"};
        for (String file : files) {
            testTabuSearch(file, 0);
            //testTabuSearch(file, 1);
        }
        //compare();
    }
}
