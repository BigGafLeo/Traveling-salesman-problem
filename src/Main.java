import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    private int[][] matrix;
    private double[][] euclid;
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
                if (!Objects.equals(arr[1], "ATSP") && !Objects.equals(arr[1], "TSP")) {
                    break;
                }
            } else if (in.startsWith("DIMENSION")) {
                dimension =  Integer.parseInt(arr[1]);
            } else if (in.startsWith("EDGE_WEIGHT_TYPE")) {
                edge_weight_type = arr[1];
                if (!Objects.equals(edge_weight_type, "EUC_2D") && !Objects.equals(edge_weight_type, "EXPLICIT")) {
                    break;
                }
            } else if (in.startsWith("EDGE_WEIGHT_FORMAT") && Objects.equals(edge_weight_type, "EXPLICIT")) {
                format = arr[1];
                if (!Objects.equals(format, "FULL_MATRIX") && !Objects.equals(format, "LOWER_DIAG_ROW")) {
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
        if (matrix == null) {
            if (euclid == null) {
                throw new WrongFileFormatException();
            } else {
                euclidToMatrix();
            }
        }
    }

    public void euclidToMatrix() {
        matrix = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            matrix[i][i] = 0;
            for (int j = i + 1; j < dimension; j++) {
                matrix[i][j] = (int) Math.sqrt(Math.pow(euclid[i][0] - euclid[j][0], 2) + Math.pow(euclid[i][1] - euclid[j][1], 2));
                matrix[j][i] = matrix[i][j];
            }
        }
    }

    public void findOptimalWay() {
        solution = new Stack<>();
        //solution = new int[dimension];
        //TODO: funkcja celu (?)
    }

    public static void main(String[] args) {
        Main main = new Main();
        Scanner sc = new Scanner(System.in);
        try {
            main.readFile(sc.nextLine());
            main.findOptimalWay();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
