package dk.gundmann.jenkins.cddbplugin.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dk.gundmann.jenkins.cddbplugin.database.Connector;
import dk.gundmann.jenkins.cddbplugin.database.CreateVersionTable;
import dk.gundmann.jenkins.cddbplugin.database.DriverClassLoader;
import dk.gundmann.jenkins.cddbplugin.database.TableNameResolver;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameter;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;

public class TestUtils {

	private final Parameters parameters;

	public TestUtils() {
		parameters = setUpParameters();
		clearDB();
	}

	private Parameters setUpParameters() {
		Parameters result = new Parameters(Parameter.aBuilder().withKey(TableNameResolver.KEY_VERSION_TABLE_NAME)
				.withValue(StringUtil.EMPTY).build(), Parameter.aBuilder().withKey(DriverClassLoader.KEY_JAR_FILE_NAME)
				.withValue("./target/jdbcjars/hsqldb.jar").build(), Parameter.aBuilder().withKey(Connector.KEY_USER)
				.withValue("sa").build(), Parameter.aBuilder().withKey(Connector.KEY_PASSWORD).withValue("sa").build(),
				Parameter.aBuilder().withKey(Connector.KEY_CONNECTION_STRING).withValue("jdbc:hsqldb:file:mydb")
						.build());
		new TableNameResolver().execute(result);
		new DriverClassLoader().execute(result);
		new Connector().execute(result);
		new CreateVersionTable().execute(result);
		return result;
	}

	private void clearDB() {
		try {
			getConnection().createStatement().execute("delete from " + TableNameResolver.DEFAULT_TABLE_NAME);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		return getParameters().valueAsType(Connector.KEY_CONNECTION, Connection.class);
	}

	public Parameters getParameters() {
		return parameters;
	}

	public void insertRowWithApplicationVersion(int dbVersion, String version, String fileName, Date createdDate) {
		try {
			getConnection().createStatement().execute(
					"insert into " + TableNameResolver.DEFAULT_TABLE_NAME
							+ "(databaseVersion, applicationVersion, fileName, createdDate) values(" + dbVersion + ", "
							+ version + ", '" + fileName + "', '" + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(createdDate) +"')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertRowWithApplicationVersion(int dbVersion, String version, String fileName) {
		insertRowWithApplicationVersion(dbVersion, version, fileName, new Date());
	}

	public void createUpdateFile(String fileName) {
		try {
			new File(fileName).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
