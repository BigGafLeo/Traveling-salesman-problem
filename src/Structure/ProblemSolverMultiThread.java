
package Structure;

		import java.util.*;
		import java.util.Arrays;

public abstract class ProblemSolverMultiThread extends Thread {
	protected final int dimension;

	protected int[] solution;
	protected int[] tmpSolution;
	/**
	 * Najlepsze z dotychczas napotkanych rozwiązań w tabuSearch.
	 */
	protected int[] bestSolution;
	protected int[][] goodSolutions;

	protected final Matrix matrix;
	/**
	 * Flaga sprawdzająca, czy tablica tabuTable osiągneła maksymalny rozmiar.
	 */
	protected boolean isTabuExtended;
	/**
	 * Tablica tabu.
	 */
	protected int[][] tabuTable;
	/**
	 *
	 */
	protected boolean[][] booleanTabuTable;
	protected int[] firstPermutation;
	/**
	 * Iterator idący po tablicy tabu w algorytmie tabuSearch.
	 */
	int tabuTableSize = 100;
	protected int tabuIterator;
	/**
	 * Flaga sprawdzająca, czy rozwiązanie zostało zainicjowane
	 * (używana w algorytmach kOpt i nearestNeighbours).
	 */
	protected boolean isInitiated;

	/**
	 * Długość możliwie najkrótszej drogi w tabuSearch.
	 */
	protected int bestDistance;
	/**
	 * Długość możliwie najkrótszej drogi (w przypadku tabuSearch najkrótszej drogi otoczenia).
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

	public ProblemSolverMultiThread(Matrix matrix) {
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
		//long start = System.currentTimeMillis(), end = start + 10000;
		randomPermutation();
		int[] bestSolution;
		int bestDistance = distance;
		bestSolution = solution.clone();

		for (int i = 1; i < k/* && System.currentTimeMillis() < end*/; i++) {
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
		//System.out.println("czas: " + (System.currentTimeMillis() - start));
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
		//long start = System.currentTimeMillis();
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
		//System.out.println("czas: " + (System.currentTimeMillis() - start));
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
			if (a <= min) {
				if (a < min) {
					min = a;
				}
			}
		}
		distance += min;
		for (int i = k; i < dimension; i++) {
			if (matrix.get(tmpSolution[k - 1] - 1, tmpSolution[i] - 1) == min) {
				int tmp = tmpSolution[k];
				tmpSolution[k] = tmpSolution[i];
				tmpSolution[i] = tmp;
				nearestNeighboursRec(k + 1, distance);
				tmpSolution[i] = tmpSolution[k];
				tmpSolution[k] = tmp;
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

	private boolean twoOpt() {
		int[] edgesToCut = new int[2];
		boolean changes = cutEdges(edgesToCut);
		if (changes || tabuTable != null) {
			for (int i = edgesToCut[0] + 1; i < (edgesToCut[1] + edgesToCut[0] + 3) / 2; i++) {
				int tmp = solution[i % dimension];
				int index = (edgesToCut[1] - i + edgesToCut[0] + 1) % dimension;
				solution[i % dimension] = solution[index];
				solution[index] = tmp;
			}
			if (tabuTable != null) {
				tabuTable[tabuIterator][0] = edgesToCut[0];
				tabuTable[tabuIterator][1] = edgesToCut[1];
				tabuIterator = (tabuIterator + 1) % tabuTableSize;
				tabuTable[tabuIterator][0] = edgesToCut[1];
				tabuTable[tabuIterator][1] = edgesToCut[0];
				tabuIterator = (tabuIterator + 1) % tabuTableSize;
			}
		}
		return changes;
	}

	public void twoOpt(boolean multiCheck) {
		//long start = System.currentTimeMillis(), end = start + 10000;
		//randomPermutation();
		tabuTable = null;
		boolean changes;
		do {
			changes = twoOpt();
		} while (multiCheck && changes/* && System.currentTimeMillis() < end*/);
		//System.out.println("czas: " + (System.currentTimeMillis() - start));
	}

//public void tabuSearch(int k) {
//	//randomPermutation();
//	System.out.println(this);
//	tabuIterator = 0;
//	long start = System.currentTimeMillis(), end = start + 60000, time = start + 1000;
//	tabuTable = new int[tabuTableSize][dimension];
//	bestDistance = distance;
//	bestSolution = solution.clone();
//	isTabuExtended = false;
//	if (k >= 2) {
//		initKOpt(k);
//	}
//	do {
//		switch (k) {
//			case 0 -> swap();
//			case 1 -> insert();
//			default -> kOpt(k);
//		}
//		tabuTable[tabuIterator] = solution.clone();
//		if (distance < bestDistance) {
//			bestDistance = distance;
//			bestSolution = solution.clone();
//		}
//		if (tabuIterator == tabuTableSize - 1)
//			isTabuExtended = true;
//		tabuIterator = (tabuIterator + 1) % tabuTableSize;
//		System.out.println(this);
//		if (System.currentTimeMillis() >= time) {
//			System.out.println((System.currentTimeMillis() - start) / 1000 + ";" + bestDistance + ";" + this);
//			time += 1000;
//		}
//	} while (System.currentTimeMillis() < end);
//	solution = bestSolution.clone();
//	distance = bestDistance;
//}

	public void tabuSearch(int k) {
		//randomPermutation();
		tabuIterator = 0;
		long start = System.currentTimeMillis(), end = start + 30000, time = start + 1000;
		if (k < 2) {
			tabuTable = new int[tabuTableSize][2];
		} else {
			tabuTable = new int[tabuTableSize][k];
			booleanTabuTable = new boolean[tabuTableSize][k];
		}
		bestDistance = distance;
		bestSolution = solution.clone();
		isTabuExtended = false;
		if (k >= 2) {
			initKOpt(k);
		}
		int worseCounter = 0, kickFrequency = 100;
		Integer lastDistance = null;
		do {
			switch (k) {
				case -1 -> twoOpt();
				case 0 -> swap();
				case 1 -> insert();
				default -> kOpt(k);
			}
			if (lastDistance != null && distance >= lastDistance) {
				worseCounter++;
				if (worseCounter == kickFrequency) {
					// TODO: kick

					randomPermutation(); // zaczynamy od nowa

					isTabuExtended = false;
					tabuIterator = 0;
					lastDistance = null;
					worseCounter = 0;
					kickFrequency *= 1.1;
					continue;
				}
			} else if (distance < bestDistance) {
				bestDistance = distance;
				bestSolution = solution.clone();
			}
			lastDistance = distance;
			if (tabuIterator == tabuTableSize - 1)
				isTabuExtended = true;
			if (System.currentTimeMillis() >= time) {
				System.out.println((System.currentTimeMillis() - start) / 1000 + ";" + bestDistance);
				time += 1000;
			}
		} while (System.currentTimeMillis() < end);
		solution = bestSolution.clone();
		distance = bestDistance;
	}

	protected int[] recreateSolution(int k, int[] permutation, int[] corrCity, boolean[] booleanArray) {
		tmpSolution = new int[dimension];
		int j = 0;
		int a = permutation[0];
		int b = corrCity[permutation[0]];
		if (booleanArray[0]) {
			for (int l = b + a; l >= b; l--) {
				tmpSolution[j++] = solution[l % dimension];
			}
		} else {
			for (int l = 0; l <= a; l++) {
				tmpSolution[j++] = solution[l];
			}
		}
		for (int i = 1; i < k; i++) {
			a = permutation[i];
			b = corrCity[permutation[i]];
			if (booleanArray[i]) {
				for (int l = a; l >= b; l--) {
					tmpSolution[j++] = solution[l];
				}
			} else {
				for (int l = b; l <= a; l++) {
					tmpSolution[j++] = solution[l];
				}
			}
		}
		a = permutation[0];
		b = corrCity[permutation[0]];
		b += (b == 0 ? dimension : 0);
		if (booleanArray[0]) {
			for (int l = a + dimension; l >= a + 1 + b; l--) {
				tmpSolution[j++] = solution[l % dimension];
			}
		} else {
			for (int l = b; l <= dimension - 1; l++) {
				tmpSolution[j++] = solution[l];
			}
		}
		return tmpSolution;
	}

	protected boolean isAcceptable(int first, int second) {
		int iterator = (isTabuExtended ? tabuTableSize : tabuIterator) - 1;
		int i = tabuIterator + tabuTableSize - 1;
		while (iterator >= 0) {
			if (tabuTable[i % tabuTableSize][0] == first && tabuTable[i % tabuTableSize][1] == second) {
				return false;
			}
			i--;
			iterator--;
		}
		return true;
	}

	protected boolean isAcceptable(int[] permutation, boolean[] booleanArray){
		int iterator = (isTabuExtended ? tabuTableSize : tabuIterator) - 1;
		int i = tabuIterator + tabuTableSize - 1;
		while (iterator >= 0) {
			if (Arrays.equals(tabuTable[i % tabuTableSize], permutation) && Arrays.equals(booleanTabuTable[i % tabuTableSize], booleanArray)) {
				return false;
			}
			i--;
			iterator--;
		}
		return true;
	}

	public void swap(boolean multiCheck) {
		tabuTable = null;
		int tmpDistance;
		do {
			tmpDistance = distance;
			swap();
		} while (multiCheck && tmpDistance > distance);
	}

	private int[] swap(int a, int b) {
		tmpSolution = solution.clone();
		int tmp = tmpSolution[a];
		tmpSolution[a] = tmpSolution[b];
		tmpSolution[b] = tmp;
		return tmpSolution;
	}

	public void swap() {
		int a = 0, b = 0;
		isInitiated = false;
		firstDistance = distance;
		for (int i = 0; i < dimension; i++) {
			helpDistance = firstDistance;
			helpDistance -= matrix.get(solution[(i - 1 + dimension) % dimension] - 1, solution[i] - 1)
					+ matrix.get(solution[(i + 1) % dimension] - 1, solution[(i + 2) % dimension] - 1);
			helpDistance += matrix.get(solution[(i - 1 + dimension) % dimension] - 1, solution[(i + 1) % dimension] - 1)
					+ matrix.get(solution[i] - 1, solution[(i + 2) % dimension] - 1);
			if (this instanceof AsymmetricProblemSolverMultiThread) {
				helpDistance += matrix.get(solution[(i + 1) % dimension] - 1, solution[i] - 1)
						- matrix.get(solution[i] - 1, solution[(i + 1) % dimension] - 1);
			}
			if (helpDistance < bestDistance || (helpDistance < distance || !isInitiated) && (tabuTable == null || isAcceptable(solution[i], solution[(i + 1) % dimension]))) {
				isInitiated = true;
				distance = helpDistance;
				a = i;
				b = (i + 1) % dimension;
			}
			for (int j = i + 2; j < dimension - 1; j++) {
				helpDistance = firstDistance;
				helpDistance -= matrix.get(solution[(i - 1 + dimension) % dimension] - 1, solution[i] - 1)
						+ matrix.get(solution[i] - 1, solution[i + 1] - 1)
						+ matrix.get(solution[j - 1] - 1, solution[j] - 1)
						+ matrix.get(solution[j] - 1, solution[j + 1] - 1);
				helpDistance += matrix.get(solution[(i - 1 + dimension) % dimension] - 1, solution[j] - 1)
						+ matrix.get(solution[j] - 1, solution[i + 1] - 1)
						+ matrix.get(solution[j - 1] - 1, solution[i] - 1)
						+ matrix.get(solution[i] - 1, solution[j + 1] - 1);
				if (helpDistance < bestDistance || (helpDistance < distance || !isInitiated) && (tabuTable == null || isAcceptable(solution[i], solution[j]))) {
					isInitiated = true;
					distance = helpDistance;
					a = i;
					b = j;
				}
			}
		}
		if (distance < firstDistance || tabuTable != null)  {
			solution = swap(a, b).clone();
			if (tabuTable != null) {
				tabuTable[tabuIterator][0] = solution[a];
				tabuTable[tabuIterator][1] = solution[b];
				tabuIterator = (tabuIterator + 1) % tabuTableSize;
				tabuTable[tabuIterator][0] = solution[b];
				tabuTable[tabuIterator][1] = solution[a];
				tabuIterator = (tabuIterator + 1) % tabuTableSize;
			}
		}
	}

	public void insert(boolean multiCheck) {
		tabuTable = null;
		int tmpDistance;
		do {
			tmpDistance = distance;
			insert();
		} while (multiCheck && tmpDistance > distance);
	}

	private int[] insert(int a, int b) {
		tmpSolution = solution.clone();
		int tmp = tmpSolution[a];
		for (int i = a; i < b; i++) {
			tmpSolution[i % dimension] = tmpSolution[(i + 1) % dimension];
		}
		tmpSolution[b % dimension] = tmp;
		return tmpSolution;
	}

	protected void insert() {
		int a = 0, b = 0;
		firstDistance = distance;
		isInitiated = false;
		for (int i = 0; i < dimension; i++) {
			helpDistance = firstDistance;
			helpDistance -= matrix.get(solution[(i - 1 + dimension) % dimension] - 1, solution[i] - 1)
					+ matrix.get(solution[(i + 1) % dimension] - 1, solution[(i + 2) % dimension] - 1);
			helpDistance += matrix.get(solution[(i - 1 + dimension) % dimension] - 1, solution[(i + 1) % dimension] - 1)
					+ matrix.get(solution[i] - 1, solution[(i + 2) % dimension] - 1);
			if (this instanceof AsymmetricProblemSolverMultiThread) {
				helpDistance += matrix.get(solution[(i + 1) % dimension] - 1, solution[i] - 1)
						- matrix.get(solution[i] - 1, solution[(i + 1) % dimension] - 1);
			}
			if (helpDistance < bestDistance || (helpDistance < distance || !isInitiated) && (tabuTable == null || isAcceptable(solution[i], solution[(i + 1) % dimension]))) {
				isInitiated = true;
				distance = helpDistance;
				a = i;
				b = i + 1;
			}

			for (int j = i + 2; j < i + dimension - 1; j++) {
				helpDistance -= matrix.get(solution[(j - 1 + dimension) % dimension] - 1, solution[i] - 1)
						+ matrix.get(solution[j % dimension] - 1,solution[(j + 1) % dimension] - 1);
				helpDistance += matrix.get(solution[(j - 1 + dimension) % dimension] - 1,solution[j % dimension] - 1)
						+ matrix.get(solution[i] - 1,solution[(j + 1) % dimension] - 1);
				if (this instanceof AsymmetricProblemSolverMultiThread) {
					helpDistance += matrix.get(solution[j % dimension] - 1, solution[i] - 1)
							- matrix.get(solution[i] - 1, solution[j % dimension] - 1);
				}
				if (helpDistance < bestDistance || (helpDistance < distance || !isInitiated) && (tabuTable == null || isAcceptable(solution[i], solution[j % dimension]))) {
					isInitiated = true;
					distance = helpDistance;
					a = i;
					b = j;
				}
			}
		}
		if (distance < firstDistance || tabuTable != null) {
			solution = insert(a, b).clone();
			if (tabuTable != null) {
				tabuTable[tabuIterator][0] = solution[a];
				tabuTable[tabuIterator][1] = solution[b % dimension];
				tabuIterator = (tabuIterator + 1) % tabuTableSize;
				tabuTable[tabuIterator][0] = solution[a];
				tabuTable[tabuIterator][1] = solution[(a + dimension - 1) % dimension];
				tabuIterator = (tabuIterator + 1) % tabuTableSize;
			}
		}
	}

	public void kOpt(int k, boolean multiCheck) {
		//long start = System.currentTimeMillis();
		initKOpt(k);
		tabuTable = null;
		int tmpDistance;
		do {
			tmpDistance = distance;
			kOpt(k);
		} while (multiCheck && tmpDistance > distance);
		//System.out.println(System.currentTimeMillis() - start);
	}

	private void initKOpt(int k) {
		corrCity = new int[dimension];
		finalCorrCity = new int[dimension];
		permutation = new int[k];
		booleanArray = new boolean[k];
	}

	private void kOpt(int k) {
		firstDistance = distance;
		if (this instanceof SymmetricProblemSolverMultiThread) {
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
		int tmpDistance = distance;
		isInitiated = false;
		initPermutation(k, 0);
		if (tmpDistance > distance || tabuTable != null) {
			recreateSolution(k, finalPermutation, finalCorrCity, finalBooleanArray);
			solution = tmpSolution.clone();
			if (tabuTable != null) {
				for (int i = 0; i < k; i++) {
					tabuTable[tabuIterator][i] = solution[finalPermutation[i]];
				}
				booleanTabuTable[tabuIterator] = finalBooleanArray.clone();
				tabuIterator = (tabuIterator + 1) % tabuTableSize;
				for (int i = 0; i < k; i++) {
					if (finalBooleanArray[i]) {
						tabuTable[tabuIterator][i] = solution[corrCity[firstPermutation[i]]];
					}
					else
						tabuTable[tabuIterator][i] = solution[firstPermutation[i]];
				}
				booleanTabuTable[tabuIterator] = finalBooleanArray.clone();
				tabuIterator = (tabuIterator + 1) % tabuTableSize;
			}
		}
	}

	private void initPermutation(int k, int l) {
		if (l == k) {
			int tmpDistance = distance;
			boolean tmpIsInitiated = isInitiated;
			initBooleanArray(k, (this instanceof SymmetricProblemSolverMultiThread ? 1 : 0), firstDistance);
			if (!tmpIsInitiated || tmpDistance > distance) {
				firstPermutation = permutation.clone();
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
			if (helpDistance < bestDistance || (!isInitiated || distance < this.distance)
					&& (tabuTable == null || isAcceptable(permutation, booleanArray))) {
				isInitiated = true;
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
		int m4 = solution[l < k - 1 ? (permutation[l] + 1) % dimension : (booleanArray[0] ? permutation[0] : corrCity[permutation[0]])];
		distance -= matrix.get(m1 - 1, m2 - 1) + matrix.get(m3 - 1, m4 - 1);
		distance += matrix.get(m1 - 1, m3 - 1) + matrix.get(m2 - 1, m4 - 1);
		if (this instanceof AsymmetricProblemSolverMultiThread) {
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
			if (this instanceof AsymmetricProblemSolverMultiThread) {
				int a, b, c, d;
				if (helpBooleanArray[i + j]) {
					c = helpPermutation[i + j];
					a = corrCity[c];
					d = 1;
				} else {
					a = helpPermutation[i + j];
					c = corrCity[a];
					d = 0;
				}
				helpDistance += distances[1 - d][c] - distances[1 - d][a] - distances[d][a] + distances[d][c];
				if (helpBooleanArray[i + j + 1]) {
					b = helpPermutation[i + j + 1];
					c = corrCity[b];
					d = 1;
				} else {
					c = helpPermutation[i + j + 1];
					b = corrCity[c];
					d = 0;
				}
				helpDistance += distances[1 - d][b] - distances[1 - d][c] - distances[d][c] + distances[d][b];
				helpDistance += matrix.get(solution[b] - 1, solution[a] - 1)
						- matrix.get(solution[a] - 1, solution[b] - 1);
			}

			int tmp = helpPermutation[i + j];
			helpPermutation[i + j] = helpPermutation[i + j + 1];
			helpPermutation[i + j + 1] = tmp;
			boolean tmpBool = !helpBooleanArray[i + j];
			helpBooleanArray[i + j] = !helpBooleanArray[i + j + 1];
			helpBooleanArray[i + j + 1] = tmpBool;

			if (helpDistance < bestDistance || (!isInitiated || helpDistance < distance)
					&& (tabuTable == null || isAcceptable(helpPermutation, helpBooleanArray))) {
				isInitiated = true;
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

	public void run(int k){
		for (int i = 0; i < 1; i++)
			switch (k){
				case -1:
					twoOpt();
					break;
				case 0:
					swap();
					break;
				case 1:
					insert();
					break;
				default:
					kOpt(k);
					break;
			}
	}
}
