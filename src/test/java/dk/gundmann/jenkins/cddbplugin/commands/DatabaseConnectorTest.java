package dk.gundmann.jenkins.cddbplugin.commands;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import dk.gundmann.jenkins.cddbplugin.Result;
import dk.gundmann.jenkins.cddbplugin.commands.DatabaseConnector;
import dk.gundmann.jenkins.cddbplugin.commands.DriverClassLoader;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameter;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;

public class DatabaseConnectorTest {

	@Test
	public void givenACorrectConnectionStringWillConnect() throws Exception {
		// given 
		new DriverClassLoader().execute(new Parameters(
				Parameter.aBuilder()
					.withKey(DriverClassLoader.KEY_JAR_FILE_NAME)
					.withValue("./target/jdbcjars/hsqldb.jar")
					.build()));
		
		Parameters parameters = new Parameters(
				Parameter.aBuilder()
					.withKey(DatabaseConnector.KEY_USER)
					.withValue("test")
					.build(),
				Parameter.aBuilder()
					.withKey(DatabaseConnector.KEY_PASSWORD)
					.withValue("test")
					.build(),
				Parameter.aBuilder()
					.withKey(DatabaseConnector.KEY_CONNECTION_STRING)
					.withValue("jdbc:hsqldb:file:enrolments")
					.build());
		
		// when
		DatabaseConnector connect = new DatabaseConnector();

		// then
		assertThat("The connection to the database faild!", connect.execute(parameters), equalTo(Result.ok()));
	}
	
	
	
}
