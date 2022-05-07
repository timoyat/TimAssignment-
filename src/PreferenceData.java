import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//create new class to store preferencesdata for students
//from the looks of it, this matches a student to a preference
public class PreferenceData {

	// creates a list of Student objects
	private final List<Student> students;
	// creates a list of Project objects
	private final List<Project> projects;
	// create 2d array, why are we doing this
	private int[][] preferences;

	private static enum ReadState {
		STUDENT_MODE, PROJECT_MODE, PREFERENCE_MODE, UNKNOWN;
	};


	// create ArrayLists with type Student and Project
	// we use array lists as they do not have a set size thus we can populate an ArrayList
	// without knowing the number of items in the student and project files.
	public PreferenceData() {
		//refers to the super class of students. WHY
		super();
		this.students = new ArrayList<Student>();
		this.projects = new ArrayList<Project>();
	}

	// add a Student "s" to the array list, 
	// the function takes in a Student object 
	// 
	public void addStudent(Student s) {
		this.students.add(s);
	}

	// add a student s to the Student ArrayList.
	// this method first creates a student object using the createStudent method in Student 
	// once the student has been created we use the addStudent method above to add the student to the list.
	public void addStudent(String s) {
		this.addStudent(Student.createStudent(s));
	}

	// add project object to Project arrayList
	public void addProject(Project p) {
		this.projects.add(p);
	}
	
	//create project object from a string using createProject method ETC ETC TIM FINISH THIS
	public void addProject(String p) {
		this.addProject(Project.createProject(p));
	}

	// create a 2d Array or a matrix. The array is fixed with the sizes of our arraylist
	public void createPreferenceMatrix() {
		this.preferences = new int[this.students.size()][this.projects.size()];
	}

	// assigns a students preference for a student through the matrix we created
	// takes in a student and project object and then find where their index is in teh matrix
	// we can then assign Student S's preference for Project p
	public void setPreference(Student s, Project p, int preference) {
		this.preferences[this.students.indexOf(s)][this.projects.indexOf(p)] = preference;
	}

	// sets the preference at the given row and colomn, we do not need to 
	// reference back to the indexes of student and project
	// WHY TIM?
	public void setPreference(int row, int column, int preference) {
		this.preferences[row][column] = preference;
	}

	// sets the preference of all projects at student index row.
	// row is the student at index row
	// we get the preferences from prefvalues, which is declared as a string array
	// the user inputs for student[row] as string of integers "123 "
	// we then convert the string to an int, and then interate over ther string
	// ie prefValues = "213", prefValues[1] = 1

	// we only iterate throught the indexes for project, "j", aslong as 
	// j is less than len(prefValues), this is to avoid out of bounds errors,
	// the method sets the preference for the student at index row for the project at index j to prefValues[j]
	public void setPreferenceRow(int row, String[] prefValues) {
		for (int j = 0; j < prefValues.length; j++) {
			this.preferences[row][j] = Integer.parseInt(prefValues[j]);
		}
	}

	// returns a list of all students
	public List<Student> getStudents() {
		return students;
	}

	// returns a list of all projects
	public List<Project> getProjects() {
		return projects;
	}

	// returns a matrix of all the prefences students have for projects
	public int[][] getPreferences() {
		return preferences;
	}

	//override because we have other toString methods in other classes, when PreferenceData.toString is called 
	// the program doesnt mistakenly reference to string from somewhere else
	@Override
	// returns a string with students, projects and students preferences for each project
	public String toString() {
		return "PreferenceData [students=" + students + ", projects=" + projects + ", preferences="
				// convert array to string format 
				+ Arrays.toString(preferences) + "]";
	}

	//reading data from file 
	static PreferenceData readData(String inputFile) {
		//new preference data object
		PreferenceData prefs = new PreferenceData();
		//try to read file 
		try(BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
			
			ReadState state = ReadState.UNKNOWN;
			int row = 0;
			//while file is able to be read
			while (reader.ready()) {
				// read a line, goes to next everytime it runs
				String line = reader.readLine();
				switch (line.trim()) {
				//if we are reading students we use Student mode
				case "Students:":
					state = ReadState.STUDENT_MODE;
					break;
				case "Projects:":
					state = ReadState.PROJECT_MODE;
					break;
				case "Preferences:":
					prefs.createPreferenceMatrix();
					state = ReadState.PREFERENCE_MODE;
					break;
				default:
					switch (state) {
					// if none of teh above cases are met it means we are actually reading data
					// ie student names, not just the category

					//if mode is set to student mode, add the student to the ArrayList
					case STUDENT_MODE:
						prefs.addStudent(line);
						break;
					//TIM DO THIS
					case PROJECT_MODE:
						prefs.addProject(line);
						break;
					//TIM DO THIS
					case PREFERENCE_MODE:
						//regex splits string by ,
						prefs.setPreferenceRow(row, line.split(","));
						row++;
						break;
					default:
						throw new PreferenceFormatException(line);
					}
				}

			}

			reader.close();
		//catch errors wit file

		} catch (FileNotFoundException e) {
			System.err.println("Error opening preferences file. File does not exist as specified.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error reading from file.");
			e.printStackTrace();
		} catch (PreferenceFormatException e) {
			System.err.println("Preference file in incorrect format. I can't tell which section I'm in.");
			System.err.println("Line being read: " + e.getCurrentLine());
			e.printStackTrace();
		}

		return prefs;
	}

	// return preference that student i has for project j
	public int getPreference(int i, int j) {
		return this.preferences[i][j];
	}
	
	// time do this
	public int numStudents() {
		return this.students.size();
	}
	
	// tim do this
	public int numProjects() {
		return this.projects.size();
	}

}