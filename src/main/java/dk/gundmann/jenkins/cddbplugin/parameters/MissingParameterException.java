package dk.gundmann.jenkins.cddbplugin.parameters;

import dk.gundmann.jenkins.cddbplugin.CDDBException;

public class MissingParameterException extends CDDBException {

	public MissingParameterException(String key) {
		super("The property was missing: " + key);
	}

	private static final long serialVersionUID = 8737907747784831815L;

	
}
