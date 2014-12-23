package dk.gundmann.jenkins.cddbplugin;

public class DBUpdateException extends CDDBException {

	private static final long serialVersionUID = 308123607667297354L;

	public DBUpdateException(Result result) {
		super(result.message(), result.exception());
	}

}
