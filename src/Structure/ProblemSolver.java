package Structure;

import java.util.*;
import java.util.Arrays;

public abstract class ProblemSolver {
	protected final int dimension;
	protected int[] solution;
	protected final Matrix matrix;
	protected int distance;
	protected int[] tmpPermutation;
	protected int[] permutation;
	protected int[] finalPermutation;
	protected int helpDistance;
	protected int firstDistance;
	protected int[] nextCity;
	
	public ProblemSolver(Matrix matrix) {
		this.matrix=matrix;
		this.dimension = matrix.getDimension();
		solution = new int[dimension];
	}

	public void randomPermutation() {
		Random random = new Random();

		for (int i = dimension - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			int temp = solution[i];
			solution[i] = solution[j];
			solution[j] = temp;
		}
		distance = matrix.distance(solution);
	}

	public void kRandom(int k) {
		for(int i = 0; i < dimension; i++)
			solution[i] = i + 1;
		randomPermutation();
		int[] bestSolution;
		int bestDistance = distance;
		bestSolution = solution.clone();
		for (int i = 1; i < k; i++) {
			randomPermutation();
			System.out.println(this);
			if(bestDistance > distance) {
				bestDistance = distance;
				bestSolution = solution.clone();
			}
		}
		solution = bestSolution.clone();
		distance = bestDistance;
	}

	@Override
	public String toString() {
		return "Solution{" +
				"solutionQue=" + Arrays.toString(solution) +
				", distance=" + distance +
				'}';
	}

	public void nearestSingleNeighbour(int first) {
		ArrayList<Integer> cities = new ArrayList<>();
		for (int i = 1; i <= dimension; i++) {
			cities.add(i);
		}
		solution[0] = first;
		distance = 0;
		cities.remove(first - 1);
		for (int i = 1; i < dimension; i++) {
			solution[i] = cities.get(0);
			for (int j = 1; j < dimension - i; j++) {
				if (matrix.get(solution[i - 1] - 1, cities.get(j) - 1) < matrix.get(solution[i - 1] - 1, solution[i] - 1)) {
					solution[i] = cities.get(j);
				}
			}
			distance += matrix.get(solution[i - 1] - 1, solution[i] - 1);
			cities.remove((Integer) solution[i]);
		}
		distance += matrix.get(solution[dimension - 1] - 1, solution[0] - 1);
	}

	public void nearestNeighbour() {
		int[] bestSolution;
		nearestSingleNeighbour(1);
		System.out.println(this);
		int bestDistance = distance;
		bestSolution = solution.clone();
		for (int i = 2; i <= dimension; i++) {
			nearestSingleNeighbour(i);
			System.out.println(this);
			if(bestDistance > distance) {
				bestDistance = distance;
				bestSolution = solution.clone();
			}
		}
		solution = bestSolution.clone();
		distance = bestDistance;
	}

	protected abstract boolean cutEdges(int[] edgesToCut);

	public void twoOpt() {
		randomPermutation();
		int[] edgesToCut = new int[2];
		boolean changes;
		do {
			changes = cutEdges(edgesToCut);
			if (changes) {
				for (int i = edgesToCut[0] + 1; i < (edgesToCut[1] + edgesToCut[0] + 3) / 2; i++) {
					int tmp = solution[i % dimension];
					int index = (edgesToCut[1] - i + edgesToCut[0] + 1) % dimension;
					solution[i % dimension] = solution[index];
					solution[index] = tmp;
				}
			}
		} while (changes);
	}

	/*public void threeOpt() {
		randomPermutation();
		int tmpDistance, firstDistance;
		int[] edgesToCut = new int[3];
		boolean[] firstOrSecond = new boolean[2];
		boolean changes;
		do {
			changes = cutEdges(edgesToCut);
			firstDistance = distance;
			for (int i = 0; i < dimension; i++) {
				for (int j = i + 2; j < dimension + i - 4; j++) {
					for (int k = j + 2; k < dimension + j - 2; k++) {
						int helpDistance = firstDistance - matrix.get(solution[i], solution[(i + 1) % dimension])
								- matrix.get(solution[j], solution[(j + 1) % dimension])
								- matrix.get(solution[k], solution[(k + 1) % dimension]);
						for (int l = 1; l < 8; l++) {
							int a = (l % 2 == 0 ? j : k);
							int b = ()
							tmpDistance = helpDistance + matrix.get(solution[i], solution[])
									+ matrix.get(solution[], solution[])
									+ matrix.get(solution[], solution[])
						}
					}
				}
			}
			if (changes) {
				for (int i = edgesToCut[0] + 1; i < (edgesToCut[1] + edgesToCut[0] + 3) / 2; i++) {
					int tmp = solution[i % dimension];
					int index = (edgesToCut[1] - i + edgesToCut[0] + 1) % dimension;
					solution[i % dimension] = solution[index];
					solution[index] = tmp;
				}
			}
		} while (changes);
	}*/

	public void kOpt(int k) {
		randomPermutation();
		firstDistance = distance;
		finalPermutation = null;
		nextCity = new int[dimension];
		permutation = new int[k];
		permutation[0] = 1;
		recKOpt(k, 0);
		if (finalPermutation != null) {
			//zmiany w solution
		}
	}

	private void recKOpt(int k, int l) {
		if (l == k) {
			helpDistance = firstDistance;
			tmpPermutation = permutation.clone();
			permutations(k, true);
			return;
		}
		for (int i = (l == 0 ? 0 : permutation[l - 1] + 2); i < dimension - (permutation[0] == 0 ? 1 : 0) - 2 * (k - l - 1); i++) {
			if (l > 0) {
				nextCity[permutation[l - 1]] = permutation[l];
				if (l == k - 1) {
					nextCity[permutation[0]] = permutation[k - 1];
				}
			}
			permutation[l] = i;
			recKOpt(k, l + 1);
		}
	}

	public int permutations(int l, boolean isEven) {
		if (l == dimension) {
			return 0;
		}
		int j = 0;
		if (isEven) {
			for (int i = l; i > 0; i--) {
				j = permutations(l + 1, isEven);
				isEven = !isEven;
				int tmp = tmpPermutation[i + j];
				tmpPermutation[i + j] = tmpPermutation[i + j - 1];
				tmpPermutation[i + j - 1] = tmp;
				// coś z dodawaniem i usuwaniem krawędzi (helpDistance)
				if (helpDistance < distance) {
					distance = helpDistance;
					finalPermutation = tmpPermutation.clone();
				}
			}
			j = permutations(l + 1, isEven);
			return j + 1;
		} else {
			if (l % 2 == 1) {
				isEven = true;
			}
			for (int i = 0; i < l; i++) {
				j = permutations(l + 1, isEven);
				isEven = !isEven;
				System.out.println(this);
				int tmp = tmpPermutation[i + j];
				tmpPermutation[i + j] = tmpPermutation[i + j + 1];
				tmpPermutation[i + j + 1] = tmp;
				int tmpDistance = helpDistance;
				// coś z dodawaniem i usuwaniem krawędzi (helpDistance)
				helpDistance = 1;
				if (helpDistance < distance) {
					distance = helpDistance;
					finalPermutation = tmpPermutation.clone();
				}
			}
			j = permutations(l + 1, isEven);
			return j;
		}

	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
}
