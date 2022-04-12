import Structure.*;

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
        if (main.structure instanceof Euklides)
            for(int i =0; i < main.structure.getDimension(); i++)
                System.out.println(i+" "+((Euklides)main.structure).getX(i)+" "+((Euklides)main.structure).getY(i));
        main.problemSolver = main.structure.isSymmetric() ? new SymmetricProblemSolver(main.structure)
                : new AsymmetricProblemSolver(main.structure);
        main.problemSolver.randomPermutation();
        main.problemSolver.tabuSearch(2);
        System.out.println(main.problemSolver.toString());
    }
}
