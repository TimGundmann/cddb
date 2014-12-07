package dk.gundmann.jenkins.cddbplugin.database;

import dk.gundmann.jenkins.cddbplugin.Command;
import dk.gundmann.jenkins.cddbplugin.Result;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;

public class ResolveSqlScript implements Command {

	public static final String SCRIPT_FOLDER = "scriptFolder";
	public static final String SCRIPTS = "scripts";

	private static final String SELECT_LAST_VERSION = "select * from %s where version = :version order by version";

	@Override
	public Result execute(Parameters parameters) {
		parameters.getParameter(SCRIPT_FOLDER);
		return Result.ok();
	}

	
}
