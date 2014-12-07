package dk.gundmann.jenkins.cddbplugin.utils;

import java.sql.Connection;

import dk.gundmann.jenkins.cddbplugin.database.Connector;
import dk.gundmann.jenkins.cddbplugin.database.DriverClassLoader;
import dk.gundmann.jenkins.cddbplugin.database.TableNameResolver;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameter;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;

public class TestUtils {

	private final Parameters parameters;
	
	public TestUtils() {
		parameters = setUpParameters();
	}
	
	private Parameters setUpParameters() {
		Parameters result = new Parameters(
				Parameter.aBuilder()
					.withKey(TableNameResolver.KEY_VERSION_TABLE_NAME)
					.withValue(StringUtil.EMPTY)
					.build(),
				Parameter.aBuilder()
					.withKey(DriverClassLoader.KEY_JAR_FILE_NAME)
					.withValue("./target/jdbcjars/hsqldb.jar")
					.build(),				
				Parameter.aBuilder()
					.withKey(Connector.KEY_USER)
					.withValue("sa")
					.build(),
					Parameter.aBuilder()
					.withKey(Connector.KEY_PASSWORD)
					.withValue("sa")
					.build(),
				Parameter.aBuilder()
					.withKey(Connector.KEY_CONNECTION_STRING)
					.withValue("jdbc:hsqldb:file:mydb")
					.build());
		new TableNameResolver().execute(result);
		new DriverClassLoader().execute(result);
		new Connector().execute(result);
		return result;
	}

	public Connection getConnection() {
		return getParameters().valueAsType(Connector.KEY_CONNECTION, Connection.class);
	}

	public Parameters getParameters() {
		return parameters;
	}

}
