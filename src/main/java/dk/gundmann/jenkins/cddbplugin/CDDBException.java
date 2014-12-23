package dk.gundmann.jenkins.cddbplugin;

public class CDDBException extends RuntimeException {

	public CDDBException(String message, Exception exception) {
		super(message, exception);
	}

	public CDDBException(String message) {
		super(message);
	}

	private static final long serialVersionUID = -1007706683439023396L;

}
