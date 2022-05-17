package ProblemSolver;

import Structure.Matrix;

public class SymmetricProblemSolver extends ProblemSolver {

    public SymmetricProblemSolver(Matrix matrix) {
        super(matrix);
    }

    @Override
    protected boolean cutEdges(int[] edgesToCut) {
        isInitiated = false;
        firstDistance = distance;
        for (int i = 0; i < dimension - 2; i++) {
            for (int j = i + 2; j < dimension - (i == 0 ? 1 : 0); j++) {
                int tmpDistance = firstDistance;
                tmpDistance -= matrix.get(solution[i] - 1, solution[i + 1] - 1) +
                        matrix.get(solution[j % dimension] - 1, solution[(j + 1) % dimension] - 1);
                tmpDistance += matrix.get(solution[i] - 1, solution[j % dimension] - 1) +
                        matrix.get(solution[i + 1] - 1, solution[(j + 1) % dimension] - 1);
                if ((tmpDistance < distance || !isInitiated) && (tabuTable == null || aspirationCriterion
                        && helpDistance < bestDistance || isAcceptable(solution[i], solution[j]))) {
                    isInitiated = true;
                    distance = tmpDistance;
                    edgesToCut[0] = i;
                    edgesToCut[1] = j;
                }
            }
        }
        return distance < firstDistance;
    }
}
