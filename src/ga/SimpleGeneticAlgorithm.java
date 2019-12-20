package ga;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Random;

// Excel feature
// import org.apache.poi.ss.usermodel.Row;
// import org.apache.poi.xssf.usermodel.XSSFSheet;
// import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import problems.Problem;

public class SimpleGeneticAlgorithm {

	private double _uniformRate;
	private double _mutationRate;
	private int _tournamentSize;
	private int _elite;
	private Problem _problem;

	private int _generations;

	public SimpleGeneticAlgorithm(Problem problem, int generations, double uniformRate, double mutationRate, int elite, int tournamentSize) {
		_problem = problem;
		_generations = generations;
		_uniformRate = uniformRate;
		_mutationRate = mutationRate;
		_elite = elite;
		_tournamentSize = tournamentSize;
	}

	public boolean runAlgorithm(int populationSize) throws IOException {
		Population myPop = new Population(populationSize, true, _problem);
		int generationCount = 1;

		// Creates excel headlines (Excel feature)
		// XSSFWorkbook workbook = new XSSFWorkbook();
		// XSSFSheet sheet = workbook.createSheet("Problem");
		// Row rowHeadlines = sheet.createRow(0);
		// rowHeadlines.createCell(0).setCellValue("Generation");
		// rowHeadlines.createCell(1).setCellValue("Individ");
		// rowHeadlines.createCell(2).setCellValue("Fitness");
		// int rowCount = 1;

		while (generationCount <= _generations) {
			System.out.println("Generation " + generationCount + ":");
			System.out.println("Best individual: " +  myPop.getFittest());
			System.out.println("Fitness: " + myPop.getFittest().getFitness());

			// Adding generation, elite and fitness of elite values to excel sheet (Excel feature)
			// Row row = sheet.createRow(rowCount);
			// row.createCell(0).setCellValue(generationCount);
			// row.createCell(1).setCellValue(myPop.getFittest().toString());
			// row.createCell(2).setCellValue(myPop.getFittest().getFitness());
			// rowCount++;

			myPop = evolvePopulation(myPop);
			generationCount++;
		}

		// Print the excel sheet (Excel feature)
		// try (FileOutputStream outputStream = new FileOutputStream("EvolutionaryAlgorithm.xlsx")) {
			// workbook.write(outputStream);
		// }
		return true;
	}

	public Population evolvePopulation(Population pop) {
		int elitismOffset = _elite;
		Population newPopulation = new Population(pop.getIndividuals().size(), false, _problem);
		Collections.sort(pop.getIndividuals());

		for (int i = 0; i < elitismOffset; i++) {
			newPopulation.getIndividuals().add(i, pop.getIndividual(i));
		}

		for (int i = elitismOffset; i < pop.getIndividuals().size(); i++) {
			Individual indiv1 = tournamentSelection(pop);
			Individual indiv2 = tournamentSelection(pop);
			Individual newIndiv = crossover(indiv1, indiv2);
			newPopulation.getIndividuals().add(i, newIndiv);
		}

		for (int i = elitismOffset; i < newPopulation.getIndividuals().size(); i++) {
			mutate(newPopulation.getIndividual(i));
		}
		return newPopulation;
	}

	private Individual crossover(Individual indiv1, Individual indiv2) {
		Individual newSol = new Individual(_problem);
		for (int i = 0; i < newSol.getDefaultGeneLength(); i++) {
			if (Math.random() <= _uniformRate) {
				double averageOfParents = (indiv1.getSingleGene(i) + indiv2.getSingleGene(i)) / 2;
				newSol.setSingleGene(i, averageOfParents);
			} else {
				Random random = new Random();
				int choseGene = random.nextInt(2);
			    if (choseGene == 0)
			    	newSol.setSingleGene(i, indiv1.getSingleGene(i));
			    else
			    	newSol.setSingleGene(i, indiv2.getSingleGene(i));
			}
		}
		return newSol;
	}

	private void mutate(Individual indiv) {
		for (int i = 0; i < indiv.getDefaultGeneLength(); i++) {
			if (Math.random() <= _mutationRate) {
				Random r = new Random();
				double gene = 0.1*r.nextGaussian() + indiv.getSingleGene(i);

				if (gene > _problem.getMaxValues().get(0))
					gene = _problem.getMaxValues().get(0);
				else if (gene < _problem.getMinValues().get(0))
					gene = _problem.getMinValues().get(0);

				indiv.setSingleGene(i, gene);
			}
		}
	}

	private Individual tournamentSelection(Population pop) {
		Population tournament = new Population(_tournamentSize, false, _problem);
		for (int i = 0; i < _tournamentSize; i++) {
			int randomId = (int) (Math.random() * pop.getIndividuals().size());
			tournament.getIndividuals().add(i, pop.getIndividual(randomId));
		}
		Individual fittest = tournament.getFittest();
		return fittest;
	}

	protected static double getFitness(Individual individual, Problem problem) {
		return problem.Eval(individual.getGenes());
	}

	private void PrintRes()  {

	}
}