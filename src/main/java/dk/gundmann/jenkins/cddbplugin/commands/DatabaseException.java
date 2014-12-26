package dk.gundmann.jenkins.cddbplugin.commands;

public class DatabaseException extends RuntimeException {
	
	public static final String MISSING_DRIVER = "The jdbc driver was not found: \"%s\"";

	private static final long serialVersionUID = 664646717914950968L;

	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(String message, Throwable t) {
		super(message, t);
	}

}
