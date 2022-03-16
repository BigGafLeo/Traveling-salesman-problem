import Structure.Euklides;
import Structure.Matrix;
import Structure.Structure;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;
import java.util.Random;

public class Main {
    private int[][] matrix;
    private double[][] euclid;
    private Structure structure;
    private int dimension = 0;
    private Stack<Integer> solution;
    //private int solution[];

    public void readFile(String dir) throws FileNotFoundException, WrongFileFormatException {
        Scanner sc = new Scanner(new File(dir));
        String format = "", edge_weight_type = "";
        while (sc.hasNextLine()) {
            String in = sc.nextLine();
            String[] arr = in.split(" ");
            if (in.startsWith("TYPE")) {
                if (!Objects.equals(arr[arr.length-1], "ATSP") && !Objects.equals(arr[arr.length - 1], "TSP")) {
                    System.out.println("type");
                    break;
                }
            } else if (in.startsWith("DIMENSION")) {
                dimension =  Integer.parseInt(arr[1]);
            } else if (in.startsWith("EDGE_WEIGHT_TYPE")) {
                edge_weight_type = arr[arr.length-1];
                if (!Objects.equals(edge_weight_type, "EUC_2D") && !Objects.equals(edge_weight_type, "EXPLICIT")) {
                    System.out.println("edge_weight_type");
                    break;
                }
            } else if (in.startsWith("EDGE_WEIGHT_FORMAT") && Objects.equals(edge_weight_type, "EXPLICIT")) {
                format = arr[arr.length-1];
                if (!Objects.equals(format, "FULL_MATRIX") && !Objects.equals(format, "LOWER_DIAG_ROW")) {
                    System.out.println("format");
                    break;
                }
            } else if ((in.startsWith("NODE_COORD_SECTION") || in.startsWith("DISPLAY_DATA_SECTION")) && dimension > 0) {
                euclid = new double[dimension][2];
                for (int i = 0; i < dimension; i++) {
                    sc.nextInt();
                    euclid[i][0] = sc.nextDouble();
                    euclid[i][1] = sc.nextDouble();
                }
            } else if (in.startsWith("EDGE_WEIGHT_SECTION") && dimension > 0) {
                if (Objects.equals(format, "FULL_MATRIX")) {
                    matrix = new int[dimension][dimension];
                    for (int i = 0; i < dimension; i++) {
                        for (int j = 0; j < dimension; j++) {
                            matrix[i][j] = sc.nextInt();
                        }
                    }
                } else if (Objects.equals(format, "LOWER_DIAG_ROW")) {
                    matrix = new int[dimension][dimension];
                    for (int i = 0; i < dimension; i++) {
                        for (int j = 0; j <= i; j++) {
                            matrix[i][j] = sc.nextInt();
                            matrix[j][i] = matrix[i][j];
                        }
                    }
                }
            }
        }
        if (matrix == null && euclid == null) {
            throw new WrongFileFormatException();
        }
        if(euclid != null) {
            structure = new Euklides(dimension, euclid);
        }
        else {
            structure = new Matrix(dimension, matrix);
        }

    }

//    public void findOptimalWay() {
//        solution = new int[dimension];
//        //TODO: funkcja celu (?)
//    }

    public static void main(String[] args) {
        Main main = new Main();
//        Scanner sc = new Scanner(System.in);
//        Scanner sc = new Scanner("kroB150.tsp");
//        try {
//            main.readFile(sc.nextLine());
//            for (int i = 0; i < main.dimension; i++) {
//                for (int j = 0; j < main.dimension; j++) {
//                    System.out.print(((Matrix) main.structure).get(i, j) + " ");
//                }
//                System.out.println();
//            }
//            System.out.println("\n\n\n\n");
//            if (main.euclid != null)
//                for(int i =0;i<main.euclid.length;i++)
//                    System.out.println(i+" "+main.euclid[i][0]+" "+main.euclid[i][1]);
//
//        } catch (Exception ex) {
//            System.out.println(ex);
//            ex.printStackTrace();
//        }
        main.dimension = 30;
        main.structure = new Euklides();
        ((Euklides)main.structure).randomGenerateEuklides(main.dimension, 1000);
        for (int i = 0; i < main.dimension; i++) {
                for (int j = 0; j < main.dimension; j++) {
                    System.out.print(((Euklides) main.structure).get(i, j) + " ");
                }
                System.out.println();
            }
            System.out.println("\n\n\n\n");
            if (main.structure instanceof Euklides)
                for(int i =0;i<main.dimension; i++)
                    System.out.println(i+" "+((Euklides)main.structure).getX(i)+" "+((Euklides)main.structure).getY(i));

    }
}
