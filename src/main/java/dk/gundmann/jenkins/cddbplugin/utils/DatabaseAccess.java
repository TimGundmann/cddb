package dk.gundmann.jenkins.cddbplugin.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;

public class DatabaseAccess {

	private static final String SELECT_LAST_DATE = "select createdDate from %s order by createdDate desc";
	private static final String SELECT_LAST_VERSION = "select databaseVersion from %s order by createdDate desc";
	private static final String SELECT_VERSION = "select databaseVersion from %1$s where applicationVersion = '%2$s' order by createdDate desc";
	private static final String SELEC_FILE_NAMES = "select fileName from %1$s where databaseVersion >= %2$s";
	private static final String CREATE_VERSION_TABLE = "create table %s(databaseVersion INTEGER not NULL, applicationVersion varchar(255) not NULL, fileName varchar(1000), createdDate TIMESTAMP)";

	private static final String NO_CATALOG = null;
	private static final String NO_SCHEMA_PATTERN = null;
	private static final String[] NO_TYPES = new String[0];

	private final Connection connection;

	public DatabaseAccess(Connection connection) {
		this.connection = connection;
	}
	
	public Connection getConnection() {
		return connection;
	}

	public List<String> getFileNamesFromDB(String tableName, int databaseVerison) {
		List<String> result = new ArrayList<String>();
		try {
			ResultSet fileNameList = getConnection()
					.createStatement()
					.executeQuery(String.format(SELEC_FILE_NAMES, tableName, databaseVerison));
			while (fileNameList.next()) {
				result.add(fileNameList.getString("fileName"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
 		return result;
	}

	public void createTable(String tableName) throws Exception {
		if (tableNotExists(getConnection(), tableName)) {
			Statement statement = getConnection().createStatement();
			statement.execute(String.format(CREATE_VERSION_TABLE, tableName));
		}
	}

	private boolean tableNotExists(Connection connection, String tableName) throws Exception {
		ResultSet tables = connection.getMetaData().getTables(NO_CATALOG, NO_SCHEMA_PATTERN, tableName, NO_TYPES);
		return !tables.first();
	}

	public Date getLatestDate(String tableName) {
		return executeQuery(String.format(SELECT_LAST_DATE, tableName), Date.class);
	}

	public Integer getLatestDBVersion(String tableName) {
		Integer result = executeQuery(String.format(SELECT_LAST_VERSION, tableName), Integer.class);
		if (result == null) {
			return 0;
		}
		return result;
	}

	public Integer findDBVersionBy(String appVersion, String tableName) {
		Integer result = executeQuery(String.format(SELECT_VERSION, appVersion, tableName), Integer.class);
		if (result == null) {
			return 0;
		}
		return result;
	}
	
	public <T> T executeQuery(String queryString, Class<T> returnType) {
		try {
			ResultSet queryResult = connection.createStatement().executeQuery(queryString);
			if (queryResult.next()) {
				return returnType.cast(queryResult.getInt("databaseVersion"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
