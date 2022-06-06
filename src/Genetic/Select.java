package Genetic;

public class Select {
	private static int divisor;

	private static void swap(int[] A, int i, int j) {
		int tmp = A[i];
		A[i] = A[j];
		A[j] = tmp;
	}

	private static int partition(int[] A, int p, int q, int k, int i) {
		int pivot = A[k];
		swap(A, k, q);
		int firstIndex = p;
		for (int j = p; j < q; j++) {
			if (A[j] < pivot) {
				swap(A, j, firstIndex);
				firstIndex++;
			}
		}
		int secondIndex = firstIndex;
		for (int j = firstIndex; j < q; j++) {
			if (A[j] == pivot) {
				swap(A, j, secondIndex);
				secondIndex++;
			}
		}
		swap(A, q, secondIndex);
		if (i < firstIndex) {
			return firstIndex;
		}
		return Math.min(i, secondIndex);
	}

	private static int selectRec(int[] A, int p, int q, int i) {
		if (p == q) {
			return p;
		}
		int k = pivot(A, p, q);
		k = partition(A, p, q, k, i);
		if (k == i) {
			return i;
		}
		if (i < k) {
			return selectRec(A, p, k - 1, i);
		}
		return selectRec(A, k + 1, q, i);
	}

	public static int select(int[] A, int i) {
		return selectRec(A, 0, A.length - 1, i);
	}

	private static int partition5(int[] A, int p, int q) {
		for (int i = p + 1; i <= q; i++) {
			int tmp = A[i], j;
			for (j = i; j > p && A[j - 1] > A[j]; j--) {
				A[j] = A[j - 1];
			}
			A[j] = tmp;
		}
		return (p + q) / 2;
	}

	private static int pivot(int[] A, int p, int q) {
		if (q - p < divisor) {
			return partition5(A, p, q);
		}
		for (int i = p; i <= q; i += divisor) {
			int r = i + divisor - 1;
			if (r > q) {
				r = q;
			}
			int x = partition5(A, i, r);
			swap(A, x, p + (i - p) / divisor);
		}
		return selectRec(A, p, p + (q - p) / divisor, p + (q - p) / (2 * divisor));
	}

	public static void setDivisor(int divisor) {
		Select.divisor = divisor;
	}
}
