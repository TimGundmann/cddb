package dk.gundmann.jenkins.cddbplugin.database;

public class DriverClassNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 664646717914950968L;

	public DriverClassNotFoundException(String jarFileName) {
		super("The jdbc driver was not found: \"" + jarFileName + "\"");
	}

	public DriverClassNotFoundException(String jarFileName, Throwable t) {
		super("The jdbc driver was not found: \"" + jarFileName + "\"", t);
	}

}
