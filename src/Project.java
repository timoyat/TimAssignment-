
public class Project {
	
	private final String supervisor;
	private final int capacity;
	
	private Project(String supervisor, int capacity) {
		super();
		this.supervisor = supervisor;
		this.capacity = capacity;
	}
	
	public static Project createProject(String supervisor, int capacity) {
		return new Project(supervisor, capacity);
	}
	
	public static Project createProject(String[] parts) {
		return createProject(parts[0].trim(), Integer.parseInt(parts[1].trim()));
	}
	
	public static Project createProject(String desc) {
		return createProject(desc.split(","));
	}

	public String getSupervisor() {
		return supervisor;
	}

	public int getCapacity() {
		return capacity;
	}

	@Override
	public String toString() {
		return "Project [supervisor=" + supervisor + ", capacity=" + capacity + "]";
	}
	
}
