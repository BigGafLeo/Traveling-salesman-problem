import Structure.AsymmetricProblemSolver;
import Structure.Matrix;
import Structure.SymmetricProblemSolver;
import org.apache.commons.math3.stat.inference.WilcoxonSignedRankTest;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class Tests {

    public static void main(String[] args) {
        File dir_atsp = new File("data/ALL_atsp");
        String[] atsp_files = dir_atsp.list();
        File dir_tsp = new File("data/ALL_tsp");
        String[] tsp_files = dir_tsp.list();


        //[0] - random
        //[1] - nearest
        //[2] - 2-opt
        assert tsp_files != null;
        double[][] tsp_distance = new double[3][tsp_files.length];
        assert atsp_files != null;
        double[][] atsp_distance = new double[3][atsp_files.length];
        Matrix matrix;

        for(int i = 0 ; i < atsp_files.length; i++)
        {
            try {
                matrix = FileManager.readFile("data/ALL_atsp/" + atsp_files[i]);
                AsymmetricProblemSolver asymmetricProblemSolver = new AsymmetricProblemSolver(matrix);
                asymmetricProblemSolver.kRandom(1000);
                atsp_distance[0][i] = asymmetricProblemSolver.getDistance();
                asymmetricProblemSolver.nearestNeighbour();
                atsp_distance[1][i] = asymmetricProblemSolver.getDistance();
                asymmetricProblemSolver.twoOpt();
                atsp_distance[2][i] = asymmetricProblemSolver.getDistance();
            } catch (FileNotFoundException | WrongFileFormatException e) {
                e.printStackTrace();
            }
            System.out.println("Przynajmniej cos sie dzieje");
        }
        for(int i = 0 ; i < tsp_files.length && i<6; i++)
        {
            try {
                matrix = FileManager.readFile("data/ALL_tsp/" + tsp_files[i]);
                SymmetricProblemSolver symmetricProblemSolver = new SymmetricProblemSolver(matrix);
                symmetricProblemSolver.kRandom(1000);
                tsp_distance[0][i] = symmetricProblemSolver.getDistance();
                symmetricProblemSolver.nearestNeighbour();
                tsp_distance[1][i] = symmetricProblemSolver.getDistance();
                symmetricProblemSolver.twoOpt();
                tsp_distance[2][i] = symmetricProblemSolver.getDistance();
            } catch (FileNotFoundException | WrongFileFormatException e) {
                e.printStackTrace();
            }
            System.out.println("Przynajmniej cos sie dzieje " + tsp_files[i]);
        }
        WilcoxonSignedRankTest wilcoxonSignedRankTest = new WilcoxonSignedRankTest();
        //[0] - 2-opt & random
        //[1] - 2-opt & nearest
        //[2] - nearest & random
        double[] tsp_results = new double[3];
        tsp_results[0] = wilcoxonSignedRankTest.wilcoxonSignedRank(tsp_distance[2], tsp_distance[0]);
        tsp_results[1] = wilcoxonSignedRankTest.wilcoxonSignedRank(tsp_distance[2], tsp_distance[1]);
        tsp_results[2] = wilcoxonSignedRankTest.wilcoxonSignedRank(tsp_distance[0], tsp_distance[1]);

        double[] atsp_results = new double[3];
        atsp_results[0] = wilcoxonSignedRankTest.wilcoxonSignedRank(atsp_distance[2], atsp_distance[0]);
        atsp_results[1] = wilcoxonSignedRankTest.wilcoxonSignedRank(atsp_distance[2], atsp_distance[1]);
        atsp_results[2] = wilcoxonSignedRankTest.wilcoxonSignedRank(atsp_distance[0], atsp_distance[1]);

        System.out.println(Arrays.toString(atsp_results) + "\n" + Arrays.toString(tsp_results));
    }
}
