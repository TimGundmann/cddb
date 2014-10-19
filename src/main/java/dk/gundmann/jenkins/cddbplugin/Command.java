package dk.gundmann.jenkins.cddbplugin;

import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;

public interface Command {

	Result execute(Parameters parameters);
	
}
