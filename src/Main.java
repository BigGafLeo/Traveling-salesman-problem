import Structure.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

public class Main {

    private Matrix structure;
    private ProblemSolver problemSolver;

    public static void main(String[] args) {
        Main main = new Main();
//        Scanner sc = new Scanner(System.in);
//        try {
//            Structure.readFile(sc.nextLine());
//            for (int i = 0; i < main.structure.getDimension(); i++) {
//                for (int j = 0; j < main.structure.getDimension(); j++) {
//                    System.out.print(main.structure.get(i, j) + " ");
//                }
//                System.out.println();
//            }
//            System.out.println("\n\n\n\n");
//            if (main.structure instanceof Euklides)
//                for(int i = 0; i < main.structure.getDimension();i++)
//                    System.out.println(i + " " + ((Euklides) main.structure).getX(i) + " " + ((Euklides) main.structure).getY(i));
//
//        } catch (Exception ex) {
//            System.out.println(ex);
//            ex.printStackTrace();
//        }
        main.structure = new Matrix();
        main.structure.randomAsymmetricGenerateMatrix(10, 10);
        for (int i = 0; i < main.structure.getDimension(); i++) {
            for (int j = 0; j < main.structure.getDimension(); j++) {
                System.out.print(main.structure.get(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println("\n\n\n\n");
        /*if (main.structure instanceof Euklides)
            for(int i =0; i < main.structure.getDimension(); i++)
                System.out.println(i+" "+((Euklides)main.structure).getX(i)+" "+((Euklides)main.structure).getY(i));
        main.problemSolver = main.structure.isSymmetric() ? new SymmetricProblemSolver(main.structure)
                : new AsymmetricProblemSolver(main.structure);
        main.problemSolver.randomPermutation();*/
        /*PrintStream ps;
        try {
            String name = "ch130";
            main.structure = FileManager.readFile("data/ALL_tsp/" + name + ".tsp");
            main.problemSolver = new SymmetricProblemSolver(main.structure);
            main.problemSolver.randomPermutation();
            main.problemSolver.nearestNeighbour();
            int[] initialSolution = main.problemSolver.getSolution();
            ps = new PrintStream(new FileOutputStream(name + "swap.csv"));
            System.setOut(ps);
            main.problemSolver.tabuSearch(0, true);
            main.problemSolver.setSolution(initialSolution);
            ps = new PrintStream(new FileOutputStream(name + "insert.csv"));
            System.setOut(ps);
            main.problemSolver.tabuSearch(1, true);
            main.problemSolver.setSolution(initialSolution);
            ps = new PrintStream(new FileOutputStream(name + "invert.csv"));
            System.setOut(ps);
            main.problemSolver.tabuSearch(-1, true);
            main.problemSolver.setSolution(initialSolution);
            ps = new PrintStream(new FileOutputStream(name + "k0pt.csv"));
            System.setOut(ps);
            main.problemSolver.tabuSearch(2, true);
            main.problemSolver.setSolution(initialSolution);
            main.problemSolver.kOpt(2, true);
        } catch (FileNotFoundException | WrongFileFormatException e) {
            e.printStackTrace();
        }*/
        String name = "berlin52";
        try {
            main.structure = FileManager.readFile("data/ALL_tsp/" + name + ".tsp");
            ProblemSolverMultiThread problemSolverMultiThread = new SymmetricProblemSolverMultiThread(main.structure);
            problemSolverMultiThread.randomPermutation();
            int[] initialSolution = problemSolverMultiThread.getSolution();
            System.out.println(problemSolverMultiThread);
            long start = System.currentTimeMillis();
            problemSolverMultiThread.kOpt(3, true);
            System.out.println(System.currentTimeMillis() - start);
            System.out.println(problemSolverMultiThread);
            main.problemSolver = new SymmetricProblemSolver(main.structure);
            main.problemSolver.setSolution(initialSolution);
            start = System.currentTimeMillis();
            main.problemSolver.kOpt(3, true);
            System.out.println(System.currentTimeMillis() - start);
            System.out.println(main.problemSolver);
        } catch (FileNotFoundException | WrongFileFormatException e) {
            e.printStackTrace();
        }
    }
}
