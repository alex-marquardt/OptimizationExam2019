package ga;

import problems.Problem;
import java.util.ArrayList;

public class Individual implements Comparable<Individual> {

	protected int _defaultGeneLength;
	private ArrayList<Double> _genes = new ArrayList<>();
	private double _fitness = Double.MIN_VALUE;
	private Problem _problem;

	public Individual(Problem problem) {
		_problem = problem;
		_defaultGeneLength = problem.getDimensions();
		for (int dim = 0; dim < _defaultGeneLength; dim++) {
			_genes.add(_problem.getMinValues().get(dim) + Math.random() * (_problem.getMaxValues().get(dim) - _problem.getMinValues().get(dim)));
		}
	}

	protected double getSingleGene(int index) {
		return _genes.get(index);
	}

	protected void setSingleGene(int index, double value) {
		_genes.set(index, value);
		_fitness = Double.MIN_VALUE;
	}

	public double getFitness() {
		if (_fitness == Double.MIN_VALUE) {
			_fitness = SimpleGeneticAlgorithm.getFitness(this, _problem);
		}
		return _fitness;
	}

	public int getDefaultGeneLength() {
		return _defaultGeneLength;
	}

	public ArrayList<Double> getGenes() {
		return _genes;
	}

	@Override
	public String toString() {
		String geneString = "";

		for (int i = 0; i < _genes.size(); i++) {
			geneString +=  getSingleGene(i);
			geneString += ", "; // Adding comma and space between each gene
		}
		geneString = geneString.substring(0, geneString.length() - 2); // Removing the last comma and space
		return geneString;
	}

	@Override
	public int compareTo(Individual individual) {
		if (individual.getFitness() > _fitness)
			return 1;
		else if (individual.getFitness() == _fitness)
			return 0;
		else
			return -1;
	}
}