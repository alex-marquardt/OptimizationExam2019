package ga;

import java.io.IOException;

import problems.P1;
import problems.P2;
import problems.RevAckley;
import problems.RevSphere;
import problems.RevRosenbrock;
import problems.Problem;

public class MainApp {

	public static void main(String[] args) throws IOException {
		Problem problem = new RevAckley();

		SimpleGeneticAlgorithm ga = new SimpleGeneticAlgorithm(problem, 40, 0.5, 0.025, 5, 5);
		ga.runAlgorithm(200);
	}
}