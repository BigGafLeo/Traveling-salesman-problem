package Genetic;

public class BinarySearch {
	public static int binarySearch(double[] arr, double value, int left, int right) {
		if (left == right)
			return left;
		int middle = (left + right) / 2;

		if (value < arr[middle]) {
			return binarySearch(arr, value, left, middle);
		}
		return binarySearch(arr, value, middle + 1, right);
	}
}
