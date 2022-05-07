import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

public class Runner {

	private static final String INPUT_FILE_NAME = "prefs.csv";
	private static final String OUTPUT_FILE_NAME = "assignment.txt";

	public static void main(String[] args) {

		PreferenceData data = PreferenceData.readData(INPUT_FILE_NAME);

		try(BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME))) {

			IloCplex cplex = new IloCplex();
			cplex.setOut(null);

			List<String> variableNames = new ArrayList<String>();

			for (Student s : data.getStudents()) {
				for (Project p : data.getProjects()) {
					variableNames.add(s.getName() + ", " + s.getStudentNumber() + ", " + s.getEmail() + ", " + p.getSupervisor());
				}
			}
			List<IloIntVar> variables = new ArrayList<IloIntVar>(variableNames.size());

			for (String name : variableNames)
				variables.add(cplex.intVar(0, 1, name));

			IloIntVar[] varArray = variables.toArray(new IloIntVar[variables.size()]);
			
			IloRange[] constraints = new IloRange[data.numStudents() + data.numProjects()];
			
			for (int k = 0; k < data.numStudents(); k++) {
				IloIntVar[] studentVars = Arrays.copyOfRange(varArray, k * data.numProjects(), (k+1)*data.numProjects());
				constraints[k] = cplex.addEq(1, cplex.sum(studentVars));
			}
			
			for (int k =  data.numStudents(); k < data.numStudents() + data.numProjects(); k++) {
				IloIntVar[] projectVars = new IloIntVar[data.numStudents()];
				int column = k - data.numStudents();
				for (int i = 0; i < data.numStudents(); i++) {
					projectVars[i] = varArray[i*data.numProjects() + column];
				}
				constraints[k] = cplex.addLe(cplex.sum(projectVars), data.getProjects().get(column).getCapacity());
			}
			
			int[] coefficients = new int[data.numStudents() * data.numProjects()];
			
			for (int i = 0; i < data.numStudents(); i++) {
				for (int j = 0; j < data.numProjects(); j++) {
					coefficients[i * data.numProjects() + j] = data.getPreference(i, j);
				}
			}
			
			cplex.addMinimize(cplex.scalProd(varArray, coefficients));
			
			List<String> solution = null;

			if (cplex.solve()) {

				double[] values = cplex.getValues(varArray);

				solution = new ArrayList<String>();

				for (int i = 0; i < varArray.length; i++) {

					if (1.0 - values[i] < 0.00000001) {

						solution.add(variableNames.get(i));

					}

				}

			}
			
			
			for (String s : solution) {
				writer.write(s + "\n");
			}

			cplex.clearModel();

		} catch (IloException e) { //is this mean to be IOException??
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Failed attempt to write to file: " + OUTPUT_FILE_NAME);
			e.printStackTrace();
		}

	}

}