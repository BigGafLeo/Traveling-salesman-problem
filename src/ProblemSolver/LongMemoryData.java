package ProblemSolver;

public class LongMemoryData {

    public int[] solution;
    public int[][] tabuTable;
    public boolean[][] booleanTabuTable;
    public int tabuIterator;
    public boolean isTabuExtended;

    public LongMemoryData(int[] solution, int[][] tabuTable, boolean[][] booleanTabuTable, int tabuIterator, boolean isTabuExtended)
    {
        this.solution = solution.clone();
        this.tabuTable = new int[tabuTable.length][];
        this.booleanTabuTable = new boolean[booleanTabuTable.length][];
        for (int i = 0; i < tabuTable.length; i++) {
            this.tabuTable[i] = tabuTable[i].clone();
            this.booleanTabuTable[i] = booleanTabuTable[i].clone();
        }
        this.tabuIterator = tabuIterator;
        this.isTabuExtended = isTabuExtended;
    }

    public LongMemoryData(int[] solution, int[][] tabuTable, int tabuIterator, boolean isTabuExtended)
    {
        this.solution = solution.clone();
        this.tabuTable = new int[tabuTable.length][];
        for (int i = 0; i < tabuTable.length; i++) {
            this.tabuTable[i] = tabuTable[i].clone();
        }
        this.tabuIterator = tabuIterator;
        booleanTabuTable = null;
        this.isTabuExtended = isTabuExtended;
    }
}
