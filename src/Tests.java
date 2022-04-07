import Structure.AsymmetricProblemSolver;
import Structure.Matrix;
import Structure.SymmetricProblemSolver;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class Tests {

    static public void TestKRandom_UponK(String dir, int optimal)
    {
        Matrix matrix;
        AsymmetricProblemSolver problemSolver;
        try {
            matrix = FileManager.readFile(dir);
            PrintWriter writer = new PrintWriter(dir+"TestKRandom_UponK_.csv");
            problemSolver = new AsymmetricProblemSolver(matrix);
            for(int k = 1; k <= 1000; k+=10) {
                problemSolver.kRandom(k);
                writer.println(k + ";" + problemSolver.getDistance() / optimal);
            }
            writer.close();
        } catch (WrongFileFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    static public void TestKOpt_UponK(String dir, int optimal)
    {
        Matrix matrix;
        SymmetricProblemSolver problemSolver;
        try {
            matrix = FileManager.readFile(dir);
            PrintWriter writer = new PrintWriter(dir+"TestKOpt_UponK_.csv");
            problemSolver = new SymmetricProblemSolver(matrix);
            problemSolver.randomPermutation();
            int[] initialSolution = problemSolver.getSolution();
            for(int k = 2; k <= 3; k++) {
                problemSolver.kOpt(k);
                writer.println(k + ";" + problemSolver.getDistance() / optimal);
                problemSolver.setSolution(initialSolution);
            }
            writer.close();
        } catch (WrongFileFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    static public void TestAlgorithms(String dir, int optimal)
    {
        Matrix matrix;
        AsymmetricProblemSolver problemSolver;
        try {
            matrix = FileManager.readFile(dir);
            PrintWriter writer = new PrintWriter(dir+"TestAlgorithms_.csv");
            problemSolver = new AsymmetricProblemSolver(matrix);
            /*problemSolver.kRandom(50000);
            System.out.println("jakosc: " + problemSolver.getDistance() / optimal);
            System.out.println("odleglosc: " + problemSolver.getDistance());
            problemSolver.nearestNeighbour();
            System.out.println("jakosc: " + problemSolver.getDistance() / optimal);
            System.out.println("odleglosc: " + problemSolver.getDistance());*/
            //problemSolver.randomPermutation();
            problemSolver.twoOpt();
            System.out.println("jakosc: " + problemSolver.getDistance() / optimal);
            System.out.println("odleglosc: " + problemSolver.getDistance());
        } catch (WrongFileFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    static public void compare() {
        File dir_atsp = new File("data/ALL_atsp");
        String[] atsp_files = dir_atsp.list();
        File dir_tsp = new File("data/ALL_tsp");
        String[] tsp_files = dir_tsp.list();


        //[0] - random
        //[1] - nearest
        //[2] - 2-opt
        assert tsp_files != null;
        double[][] tsp_distance = new double[3][10];
        assert atsp_files != null;
        double[][] atsp_distance = new double[3][18];
        Matrix matrix;
        AsymmetricProblemSolver asymmetricProblemSolver;
        SymmetricProblemSolver symmetricProblemSolver;

        System.out.println("\nAsymmetric problem:");
        for(int i = 0 ; i < atsp_distance[1].length; i++)
        {
            try {
                matrix = FileManager.readFile("data/ALL_atsp/" + atsp_files[i]);
                asymmetricProblemSolver = new AsymmetricProblemSolver(matrix);
                asymmetricProblemSolver.kRandom(1000);
                atsp_distance[0][i] = asymmetricProblemSolver.getDistance();
                asymmetricProblemSolver.nearestNeighbour();
                atsp_distance[1][i] = asymmetricProblemSolver.getDistance();
                asymmetricProblemSolver.twoOpt();
                atsp_distance[2][i] = asymmetricProblemSolver.getDistance();
                System.out.println(atsp_distance[0][i] + " " + atsp_distance[1][i] + " " + atsp_distance[2][i]);
            } catch (FileNotFoundException | WrongFileFormatException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\nSymmetric problem:");
        for(int i = 0 ; i < tsp_distance[1].length; i++)
        {
            try {
                matrix = FileManager.readFile("data/ALL_tsp/" + tsp_files[i]);
                symmetricProblemSolver = new SymmetricProblemSolver(matrix);
                symmetricProblemSolver.kRandom(1000);
                tsp_distance[0][i] = symmetricProblemSolver.getDistance();
                symmetricProblemSolver.nearestNeighbour();
                tsp_distance[1][i] = symmetricProblemSolver.getDistance();
                symmetricProblemSolver.twoOpt();
                tsp_distance[2][i] = symmetricProblemSolver.getDistance();
                System.out.println(tsp_distance[0][i] + " " + tsp_distance[1][i] + " " + tsp_distance[2][i]);
            } catch (FileNotFoundException | WrongFileFormatException e) {
                e.printStackTrace();
            }
        }
        WilcoxonSignedRankTest wilcoxonSignedRankTest = new WilcoxonSignedRankTest();
        //[0] - 2-opt & random
        //[1] - 2-opt & nearest
        //[2] - nearest & random
        double[] tsp_results = new double[3];
        tsp_results[0] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(tsp_distance[2], tsp_distance[0], true);
        tsp_results[1] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(tsp_distance[2], tsp_distance[1], true);
        tsp_results[2] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(tsp_distance[0], tsp_distance[1], true);

        double[] atsp_results = new double[3];
        atsp_results[0] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(atsp_distance[2], atsp_distance[0], true);
        atsp_results[1] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(atsp_distance[2], atsp_distance[1], true);
        atsp_results[2] = wilcoxonSignedRankTest.wilcoxonSignedRankTest(atsp_distance[0], atsp_distance[1], true);

        System.out.println("\nWilcoxon test results:\n" + Arrays.toString(atsp_results) + "\n" + Arrays.toString(tsp_results));
    }

    public static void main(String[] args) {
        /*try {
            matrix = FileManager.readFile("data/ALL_tsp/ch150.tsp");
            symmetricProblemSolver = new SymmetricProblemSolver(matrix);
            symmetricProblemSolver.kRandom(10000);
            System.out.println(symmetricProblemSolver.getDistance());
            symmetricProblemSolver.twoOpt();
            System.out.println(symmetricProblemSolver.getDistance());
        } catch (FileNotFoundException | WrongFileFormatException e) {
            e.printStackTrace();
        }*/
     //   TestKOpt_UponK("data/ALL_tsp/Ch130.tsp", 6110);
     //   TestKRandom_UponK( "data/ALL_atsp/p43.atsp", 5620);

     //   compare();
        TestAlgorithms("data/ALL_atsp/rbg443.atsp", 2720);
    }
}
