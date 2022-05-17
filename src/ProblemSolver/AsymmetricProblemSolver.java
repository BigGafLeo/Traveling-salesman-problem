package ProblemSolver;

import ProblemSolver.ProblemSolver;
import Structure.Matrix;

public class AsymmetricProblemSolver extends ProblemSolver {

    public AsymmetricProblemSolver(Matrix matrix) {
        super(matrix);
    }

    @Override
    protected boolean cutEdges(int[] edgesToCut) {
        isInitiated = false;
        helpDistance = firstDistance = distance;
        for (int i = 2; i < dimension - 1; i++) {
            int tmpDistance = helpDistance;
            tmpDistance -= matrix.get(solution[0] - 1, solution[i - 1] - 1)
                    + matrix.get(solution[1] - 1, solution[i] - 1)
                    + matrix.get(solution[i] - 1, solution[i + 1] - 1);
            tmpDistance += matrix.get(solution[0] - 1, solution[i] - 1)
                    + matrix.get(solution[1] - 1, solution[i + 1] - 1)
                    + matrix.get(solution[i] - 1, solution[i - 1] - 1);
            if ((tmpDistance < distance || !isInitiated) && (tabuTable == null || aspirationCriterion
                    && helpDistance < bestDistance || isAcceptable(solution[0], solution[i]))) {
                isInitiated = true;
                distance = tmpDistance;
                edgesToCut[0] = 0;
                edgesToCut[1] = i;
            }
            helpDistance = tmpDistance;
            for (int j = 1; j < dimension; j++) {
                tmpDistance -= matrix.get(solution[(j + 1) % dimension] - 1, solution[j] - 1)
                        + matrix.get(solution[j - 1] - 1, solution[(i + j - 1) % dimension] - 1)
                        + matrix.get(solution[(j + i) % dimension] - 1, solution[(j + i + 1) % dimension] - 1);
                tmpDistance += matrix.get(solution[(j + 1) % dimension] - 1, solution[(i + j + 1) % dimension] - 1)
                        + matrix.get(solution[(j + i) % dimension] - 1, solution[(i + j - 1) % dimension] - 1)
                        + matrix.get(solution[j - 1] - 1, solution[j] - 1);
                if ((tmpDistance < distance || !isInitiated) && (tabuTable == null || aspirationCriterion
                        && helpDistance < bestDistance || isAcceptable(solution[j], solution[(j + i) % dimension]))) {
                    isInitiated = true;
                    distance = tmpDistance;
                    edgesToCut[0] = j;
                    edgesToCut[1] = j + i;
                }
            }
        }
        return distance < firstDistance;
    }
}
