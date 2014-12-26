package dk.gundmann.jenkins.cddbplugin.commands;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import dk.gundmann.jenkins.cddbplugin.Command;
import dk.gundmann.jenkins.cddbplugin.Result;
import dk.gundmann.jenkins.cddbplugin.parameters.MissingParameterException;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;
import dk.gundmann.jenkins.cddbplugin.utils.StringUtil;

public class CreateVersionTable implements Command {
	
	private static final String CREATE_VERSION_TABLE = "create table %s(databaseVersion INTEGER not NULL, applicationVersion varchar(255) not NULL, fileName varchar(1000), createdDate TIMESTAMP)";
	private static final String NO_CATALOG = null;
	private static final String NO_SCHEMA_PATTERN = null;
	private static final String[] NO_TYPES = new String[0];

	@Override
	public Result execute(Parameters parameters) {
		try	{
			createTable(resolvedConnection(parameters), resolveTableName(parameters));
		} catch (Exception e) {
			return Result.faild("Error while createing version table", e);
		}
		return Result.ok();
	}

	private String resolveTableName(Parameters parameters) {
		try {
			return parameters.valueAsString(TableNameResolver.KEY_VERSION_TABLE_NAME);
		} catch (MissingParameterException e) {
			return StringUtil.EMPTY;
		}
	}

	private Connection resolvedConnection(Parameters parameters) {
		return parameters.valueAsType(Connector.KEY_CONNECTION, Connection.class);
	}

	private void createTable(Connection connection, String tableName) throws Exception {
		if (tableNotExists(connection, tableName)) {
			Statement statement = connection.createStatement();
			statement.execute(String.format(CREATE_VERSION_TABLE, tableName));
		}
	}

	private boolean tableNotExists(Connection connection, String tableName) throws Exception {
		ResultSet tables = connection.getMetaData().getTables(NO_CATALOG, NO_SCHEMA_PATTERN, tableName, NO_TYPES);
		return !tables.first();
	}
	
}
