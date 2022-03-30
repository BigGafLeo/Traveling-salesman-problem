import Structure.AsymmetricProblemSolver;
import Structure.Matrix;
import Structure.ProblemSolver;
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
        double[][] tsp_distance = new double[3][tsp_files.length];
        double[][] atsp_distance = new double[3][atsp_files.length];
        Matrix matrix;



        for(int i = 0 ; i < atsp_files.length; i++)
        {
            try {
                matrix = FileManager.readFile(atsp_files[i]);
                AsymmetricProblemSolver asymmetricProblemSolver = new AsymmetricProblemSolver(matrix);
                asymmetricProblemSolver.kRandom(1000);
                atsp_distance[0][i] = asymmetricProblemSolver.getDistance();
                asymmetricProblemSolver.setDistance(0);
                asymmetricProblemSolver.nearestNeighbour();
                atsp_distance[1][i] = asymmetricProblemSolver.getDistance();
                asymmetricProblemSolver.setDistance(0);
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
