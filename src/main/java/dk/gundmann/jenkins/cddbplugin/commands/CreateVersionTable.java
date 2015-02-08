package dk.gundmann.jenkins.cddbplugin.commands;

import dk.gundmann.jenkins.cddbplugin.Command;
import dk.gundmann.jenkins.cddbplugin.Result;
import dk.gundmann.jenkins.cddbplugin.parameters.MissingParameterException;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;
import dk.gundmann.jenkins.cddbplugin.utils.DatabaseAccess;
import dk.gundmann.jenkins.cddbplugin.utils.StringUtil;

public class CreateVersionTable implements Command {
	
	@Override
	public Result execute(Parameters parameters) {
		try	{
			resolveDatabaseAccess(parameters).createTable(resolveTableName(parameters));
		} catch (Exception e) {
			return Result.faild("Error while createing version table", e);
		}
		return Result.ok();
	}

	private DatabaseAccess resolveDatabaseAccess(Parameters parameters) {
		return parameters.valueAsType(DatabaseConnector.KEY_DATABASE_ACCESS, DatabaseAccess.class);
	}

	private String resolveTableName(Parameters parameters) {
		try {
			return parameters.valueAsString(TableNameResolver.KEY_VERSION_TABLE_NAME);
		} catch (MissingParameterException e) {
			return StringUtil.EMPTY;
		}
	}

}
