package Genetic;

import Structure.Matrix;

import java.util.Arrays;

public class Genotype {
	public int[] genotype;
	public int age;
	public int phenotype;
	static public Matrix matrix;

	public void makeOlder(){
		age++;
	}

	public Genotype(Matrix matrix) {Genotype.matrix = matrix;}

	public void setGenotype(int[] genotype) {
		this.genotype = genotype;
		phenotype = matrix.distance(genotype);
		age = 1;
	}

	@Override
	public String toString() {
//		return "Genotype: " + Arrays.toString(genotype) + "\nAge: " + age + ", phenotype: " + phenotype;
		return "phenotype: " + phenotype;
	}
}
