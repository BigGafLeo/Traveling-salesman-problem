import Structure.AsymmetricProblemSolver;
import Structure.Matrix;
import Structure.SymmetricProblemSolver;

import java.io.File;
import java.io.FileNotFoundException;

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
                matrix = FileManager.readFile(atsp_files[i]);
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

        }
        for(int i = 0 ; i < tsp_files.length; i++)
        {
            try {
                matrix = FileManager.readFile(atsp_files[i]);
                SymmetricProblemSolver symmetricProblemSolver = new SymmetricProblemSolver(matrix);
            } catch (FileNotFoundException | WrongFileFormatException e) {
                e.printStackTrace();
            }

        }
    }
}
