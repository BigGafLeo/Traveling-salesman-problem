package Structure;

import java.util.*;
import java.util.Arrays;

public abstract class ProblemSolver {
	protected final int dimension;
	protected int[] solution;
	protected int[] tmpSolution;
	protected final Matrix matrix;

	protected boolean isInitiated;
	/**
	 * Długość możliwie najktótszej drogi.
	 */
	protected int distance;
	/**
	 * Długość drogi używana w funkcji permutations.
	 */
	protected int helpDistance;
	/**
	 * Długość początkowo wygenerowanej drogi.
	 */
	protected int firstDistance;
	/**
	 * Długość odwróceonej początkowo wygenerowanej drogi.
	 */
	protected int revFirstDistance;
	/**
	 * distances[0] - tablica długości początkowej drogi prowadzącej od pierwszego miasta
	 * distances[1] - tablica długości odwróconej początkowej drogi prowadzącej od ostatniego miasta
	 */
	protected int[][] distances;

	/**
	 * Permutacja miast używana w funkcji initPermutation.
	 */
	protected int[] permutation;
	/**
	 * Ostateczna permutacja miast.
	 */
	protected int[] finalPermutation;
	/**
	 * Permutacja miast używana w funkcji permutations.
	 */
	protected int[] helpPermutation;

	/**
	 * Tablica używana do przechowania chwilowego stanu kolejności,
	 * w jakich odwiedzane są miasta, które łączy nieprzerwana droga,
	 * używana w funkcji initBooleanArray.
	 */
	protected boolean[] booleanArray;
	/**
	 * Tablica używana do przechowania chwilowego stanu kolejności,
	 * w jakich odwiedzane są miasta, które łączy nieprzerwana droga,
	 * używana w funkcji permutations.
	 */
	protected boolean[] helpBooleanArray;
	/**
	 * Tablica używana do przechowania ostatecznego stanu kolejności,
	 * w jakich odwiedzane są miasta, które łączy nieprzerwana droga.
	 */
	protected boolean[] finalBooleanArray;

	/**
	 * Tablica przyporządkowująca jednemu z krańcowych miast drogi
	 * drugie krańcowe miasto tej drogi.
	 */
	protected int[] corrCity;
	/**
	 * Ostateczna tablica przyporządkowująca jednemu z krańcowych miast drogi
	 * drugie krańcowe miasto tej drogi.
	 */
	protected int[] finalCorrCity;

	public ProblemSolver(Matrix matrix) {
		this.matrix=matrix;
		this.dimension = matrix.getDimension();
		solution = new int[dimension];
	}

	public void randomPermutation() {
		Random random = new Random();
		for(int i = 0; i < dimension; i++)
			solution[i] = i + 1;
		for (int i = dimension - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			int temp = solution[i];
			solution[i] = solution[j];
			solution[j] = temp;
		}
		distance = matrix.distance(solution);
	}

	public void kRandom(int k) {
		long end = System.currentTimeMillis() + 10000;
		randomPermutation();
		int[] bestSolution;
		int bestDistance = distance;
		bestSolution = solution.clone();

		for (int i = 1; i < k  && System.currentTimeMillis() < end; i++) {
			randomPermutation();
			if(bestDistance > distance) {
				bestDistance = distance;
				bestSolution = solution.clone();
			} else {
				distance = bestDistance;
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
		int bestDistance = distance;
		bestSolution = solution.clone();
		for (int i = 2; i <= dimension; i++) {
			nearestSingleNeighbour(i);
			if(bestDistance > distance) {
				bestDistance = distance;
				bestSolution = solution.clone();
			}
		}
		solution = bestSolution.clone();
		distance = bestDistance;
	}

	/*public void nearestNeighboursRec(int k, int distance) {
		if (k == dimension) {
			distance += matrix.get(tmpSolution[dimension - 1] - 1, tmpSolution[0] - 1);
			if (distance < this.distance || !isInitiated) {
				solution = tmpSolution.clone();
				this.distance = distance;
				isInitiated = true;
			}
			return;
		}
		LinkedList<Integer> help = new LinkedList<>();
		int min = matrix.get(tmpSolution[k - 1] - 1, tmpSolution[k] - 1);
		help.push(k);
		for (int i = k + 1; i < dimension; i++) {
			int a = matrix.get(tmpSolution[k - 1] - 1, tmpSolution[i] - 1);
			if (a <= min) {
				if (a < min) {
					min = a;
					help.clear();
				}
				help.push(i);
			}
		}
		distance += min;
		while (!help.isEmpty()) {
			int i = help.pop();
			int tmp = tmpSolution[k];
			tmpSolution[k] = tmpSolution[i];
			tmpSolution[i] = tmp;
			nearestNeighboursRec(k + 1, distance);
			tmpSolution[i] = tmpSolution[k];
			tmpSolution[k] = tmp;
		}
	}*/

	public void nearestNeighboursRec(int k, int distance) {
		if (k == dimension) {
			distance += matrix.get(tmpSolution[dimension - 1] - 1, tmpSolution[0] - 1);
			if (distance < this.distance || !isInitiated) {
				solution = tmpSolution.clone();
				this.distance = distance;
				isInitiated = true;
			}
			return;
		}
		int min = matrix.get(tmpSolution[k - 1] - 1, tmpSolution[k] - 1);
		for (int i = k + 1; i < dimension; i++) {
			int a = matrix.get(tmpSolution[k - 1] - 1, tmpSolution[i] - 1);
			if (a < min) {
				min = a;
			}
		}
		distance += min;
		for (int i = k; i < dimension; i++) {
			if (matrix.get(tmpSolution[k - 1] - 1, tmpSolution[i] - 1) == min) {
				int tmp = tmpSolution[k];
				tmpSolution[k] = tmpSolution[i];
				tmpSolution[i] = tmp;
				nearestNeighboursRec(k + 1, distance);
				//tmpSolution[i] = tmpSolution[k];
				//tmpSolution[k] = tmp;
			}
		}
	}

	public void nearestNeighbours() {
		isInitiated = false;
		tmpSolution = new int[dimension];
		for (int i = 1; i <= dimension; i++) {
			tmpSolution[i - 1] = i;
		}
		for (int i = 0; i < dimension; i++) {
			int tmp = tmpSolution[0];
			tmpSolution[0] = tmpSolution[i];
			tmpSolution[i] = tmp;
			nearestNeighboursRec(1, 0);
			tmpSolution[i] = tmpSolution[0];
			tmpSolution[0] = tmp;
		}
	}

	protected abstract boolean cutEdges(int[] edgesToCut);

	public void twoOpt() {
		long start = System.currentTimeMillis(), end = start + 10000;
		//randomPermutation();
		//nearestNeighbour();
		int[] edgesToCut = new int[2];
		boolean changes;

		do {
			System.out.println(this);
			changes = cutEdges(edgesToCut);
			if (changes) {
				for (int i = edgesToCut[0] + 1; i < (edgesToCut[1] + edgesToCut[0] + 3) / 2; i++) {
					int tmp = solution[i % dimension];
					int index = (edgesToCut[1] - i + edgesToCut[0] + 1) % dimension;
					solution[i % dimension] = solution[index];
					solution[index] = tmp;
				}
			}
		} while (changes && System.currentTimeMillis() < end);
		//System.out.println(System.currentTimeMillis() - start);
	}

	public void kOpt(int k) {
		//randomPermutation();
		long start = System.currentTimeMillis();
		//System.out.println();
		corrCity = new int[dimension];
		finalCorrCity = new int[dimension];
		permutation = new int[k];
		booleanArray = new boolean[k];
		do {
			firstDistance = distance;
			if (this instanceof SymmetricProblemSolver) {
				booleanArray[0] = false;
			} else {
				distances = new int[2][dimension];
				distances[0][0] = 0;
				distances[0][dimension - 1] = 0;
				for (int i = 1; i < dimension; i++) {
					distances[0][i] = distances[0][i - 1] + matrix.get(solution[i - 1] - 1, solution[i] - 1);
					distances[1][dimension - 1 - i] = distances[1][dimension - i]
							+ matrix.get(solution[dimension - i] - 1, solution[dimension - 1 - i] - 1);
				}
				revFirstDistance = distances[1][0] + matrix.get(solution[0] - 1, solution[dimension - 1] - 1);
			}
			System.out.println("\n\n" + this + "\n\n");
			finalPermutation = null;
			initPermutation(k, 0);
			if (finalPermutation != null) {
				int[] tmpSolution = new int[dimension];
				int j = 0;
				for (int i = 0; i < k; i++) {
					int a = finalPermutation[i];
					int b = finalCorrCity[finalPermutation[i]];
					a += (a < b ? dimension : 0);
					if (finalBooleanArray[i]) {
						for (int l = a; l >= b; l--) {
							tmpSolution[j++] = solution[l % dimension];
						}
					} else {
						for (int l = b; l <= a; l++) {
							tmpSolution[j++] = solution[l % dimension];
						}
					}
				}
				solution = tmpSolution.clone();
			}
		} while (finalPermutation != null);
		System.out.println("\n" + (System.currentTimeMillis() - start) + "\n");
	}

	private void initPermutation(int k, int l) {
		if (l == k) {
			int tmpDistance = distance;
			initBooleanArray(k, (this instanceof SymmetricProblemSolver ? 1 : 0), firstDistance);
			if (tmpDistance > distance) {
				for (int i = 0; i < k; i++) {
					finalCorrCity[permutation[i]] = corrCity[permutation[i]];
				}
			}
			return;
		}
		for (int i = (l == 0 ? 0 : permutation[l - 1] + 2); i < dimension - (l > 0 && permutation[0] == 0 ? 1 : 0) - 2 * (k - l - 1); i++) {
			permutation[l] = i;
			if (l > 0) {
				corrCity[permutation[l]] = (permutation[l - 1] + 1) % dimension;
				if (l == k - 1) {
					corrCity[permutation[0]] = (permutation[k - 1] + 1) % dimension;
				}
			}
			initPermutation(k, l + 1);
		}
	}

	private void initBooleanArray(int k, int l, int distance) {
		if (l == k) {
			if (distance < this.distance) {
				this.distance = distance;
				finalPermutation = permutation.clone();
				finalBooleanArray = booleanArray.clone();
			}
			helpDistance = distance;
			helpPermutation = permutation.clone();
			helpBooleanArray = booleanArray.clone();
			permutations(2, true, k);
			return;
		}
		booleanArray[l] = false;
		initBooleanArray(k, l + 1, distance);
		int m1 = solution[l > 0 ? (booleanArray[l - 1] ? corrCity[permutation[l - 1]]: permutation[l - 1]) : permutation[k - 1]];
		int m2 = solution[corrCity[permutation[l]]];
		int m3 = solution[permutation[l]];
		int m4 = solution[(permutation[l] + 1) % dimension];
		distance -= matrix.get(m1 - 1, m2 - 1) + matrix.get(m3 - 1, m4 - 1);
		distance += matrix.get(m1 - 1, m3 - 1) + matrix.get(m2 - 1, m4 - 1);
		if (this instanceof AsymmetricProblemSolver) {
			distance += distances[1][corrCity[permutation[l]]] - distances[1][permutation[l]]
					- distances[0][permutation[l]] + distances[0][corrCity[permutation[l]]];
			if (permutation[l] < corrCity[permutation[l]]) {
				distance += revFirstDistance - firstDistance;
			}
		}
		booleanArray[l] = true;
		initBooleanArray(k, l + 1, distance);
	}

	public int permutations(int l, boolean isEven, int k) {
		if (l == k) {
			return 0;
		}
		int j, it = (isEven ? -1 : 1);
		int start = (isEven ? l - 1 : 1);
		for (int i = start; i * it <= (l - start) * it; i += it) {
			j = permutations(l + 1, isEven, k);
			isEven = !isEven;

			int m1 = solution[helpBooleanArray[i + j - 1] ? corrCity[helpPermutation[i + j - 1]] : helpPermutation[i + j - 1]];
			int m2 = solution[helpBooleanArray[i + j] ? helpPermutation[i + j] : corrCity[helpPermutation[i + j]]];
			int m3 = solution[helpBooleanArray[i + j + 1] ? corrCity[helpPermutation[i + j + 1]] : helpPermutation[i + j + 1]];
			int m4 = solution[helpBooleanArray[(i + j + 2) % k] ? helpPermutation[(i + j + 2) % k] : corrCity[helpPermutation[(i + j + 2) % k]]];
			helpDistance -= matrix.get(m1 - 1, m2 - 1) + matrix.get(m3 - 1, m4 - 1);
			helpDistance += matrix.get(m1 - 1, m3 - 1) + matrix.get(m2 - 1, m4 - 1);
			System.out.println(i + " " + j);
			System.out.println(Arrays.toString(helpPermutation));
			if (this instanceof AsymmetricProblemSolver) {
				int a, b, c, d;
				if (helpBooleanArray[i + j]) {
					c = helpPermutation[i + j];
					a = corrCity[c];
					d = 1;
					if (c < a) {
						helpDistance += firstDistance - revFirstDistance;
					}
				} else {
					a = helpPermutation[i + j];
					c = corrCity[a];
					d = 0;
					if (a < c) {
						helpDistance += revFirstDistance - firstDistance;
					}
				}
				helpDistance += distances[1 - d][a] - distances[1 - d][c] - distances[d][c] + distances[d][a];
				if (helpBooleanArray[i + j + 1]) {
					b = helpPermutation[i + j + 1];
					c = corrCity[b];
					d = 1;
					if (b < c) {
						helpDistance += firstDistance - revFirstDistance;
					}
				} else {
					c = helpPermutation[i + j + 1];
					b = corrCity[c];
					d = 0;
					if (c < b) {
						helpDistance += revFirstDistance - firstDistance;
					}
				}
				System.out.println(a + " " + b);
				helpDistance += distances[1 - d][c] - distances[1 - d][b] - distances[d][b] + distances[d][c];
				helpDistance += matrix.get(solution[b] - 1, solution[a] - 1)
						- matrix.get(solution[a] - 1, solution[b] - 1);
			}

			int tmp = helpPermutation[i + j];
			helpPermutation[i + j] = helpPermutation[i + j + 1];
			helpPermutation[i + j + 1] = tmp;
			boolean tmpBool = !helpBooleanArray[i + j];
			helpBooleanArray[i + j] = !helpBooleanArray[i + j + 1];
			helpBooleanArray[i + j + 1] = tmpBool;

			if (helpDistance < distance) {
				distance = helpDistance;
				finalPermutation = helpPermutation.clone();
				finalBooleanArray = helpBooleanArray.clone();
			}
		}
		j = permutations(l + 1, isEven, k);
		return j + (isEven ? 1 : 0);
	}

	public double getDistance() {
		return distance;
	}

	public int[] getSolution() {
		return solution;
	}

	public void setSolution(int[] solution) {
		this.solution = solution;
		distance = matrix.distance(solution);
	}
}
