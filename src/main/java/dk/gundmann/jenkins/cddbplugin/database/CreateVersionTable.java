package dk.gundmann.jenkins.cddbplugin.database;

import java.sql.Connection;
import java.sql.Statement;

import dk.gundmann.jenkins.cddbplugin.Command;
import dk.gundmann.jenkins.cddbplugin.Result;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;

public class CreateVersionTable implements Command {
	
	private static final String CREATE_VERSION_TABLE = "create table dbupdate_version(version INTEGER not NULL)";

	@Override
	public Result execute(Parameters parameters) {
		try	{
			createTable(parameters.valueAsType("connection", Connection.class));
		} catch (Exception e) {
			return Result.faild("Error while createing version table", e);
		}
		return Result.ok();
	}

	private void createTable(Connection connection) throws Exception {
		Statement statement = connection.createStatement();
		statement.execute(CREATE_VERSION_TABLE);
	}

}
