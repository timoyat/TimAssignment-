public class Student {

	private final String email;
	private final String name;
	private final String studentNumber;
	private final String session;
	
	private Student(String email, String name, String studentNumber, String session) {
		super();
		this.email = email;
		this.name = name;
		this.studentNumber = studentNumber;
		this.session = session;
	}
	
	public static Student createStudent(String email, String name, String studentNumber, String session) {
		return new Student(email, name, studentNumber, session);
	}
	
	public static Student createStudent(String desc) {
		return createStudent(desc.split(","));
	}
	
	public static Student createStudent(String[] parts) {
		String email = parts[0].trim();
		String name = parts[1].trim();
		String studentNumber = parts[2].trim();
		String session = parts[3].trim();
		
		return createStudent(email, name, studentNumber, session);
	}

	@Override
	public String toString() {
		return "Student [email=" + email + ", name=" + name + ", studentNumber=" + studentNumber + ", session="
				+ session + "]";
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public String getSession() {
		return session;
	}
	
	
	
}