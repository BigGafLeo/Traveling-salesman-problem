package Genetic;

public class SelectionAlgorithm {
	static public void randomSelection(int[][] candidates, int[][] genotypes) {

	}

	static public int[] ruletSelection(int[][] genotypes) {
		int size = genotypes[0].length;
		int[] candidates = new int[size];

		Arrays.sort(genotypes, new Comparator<int[]>() {
			@Override
			public int compare(int[] o1, int[] o2) {
				int distance1 = matrix.distance(o1);
				int distance2 = matrix.distance(o1);
				return Integer.compare(distance1, distance2);
			}
		});
		Random random = new Random();

		for (int i = 0; i < size; i++) {
			double pr = random.nextDouble();
			candidates[i] = function(pr);
		}

		return candidates;
	}

	static public int[] tournamentSelection(int[][] genotypes) {
		int size = genotypes[0].length;
		int[] candidates = new int[size];

		return candidates;
	}
}
