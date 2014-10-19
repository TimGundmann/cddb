package dk.gundmann.jenkins.cddbplugin.parameters;

public class MissingParameter extends RuntimeException {

	public MissingParameter(String key) {
		super("The property was missing: " + key);
	}

	private static final long serialVersionUID = 8737907747784831815L;

	
}
