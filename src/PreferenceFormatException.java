import java.io.PrintStream;

public class PreferenceFormatException extends Exception {
	
	private final String currentLine;

	public PreferenceFormatException(String line) {
		this.currentLine = line;
	}
	
	public void printCurrentLine(PrintStream s) {
		s.print(currentLine);
	}
	
	public void printCurrentLine() {
		this.printCurrentLine(System.err);
	}

	public String getCurrentLine() {
		return currentLine;
	}

}