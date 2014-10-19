package dk.gundmann.jenkins.cddbplugin.database;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import dk.gundmann.jenkins.cddbplugin.Result;
import dk.gundmann.jenkins.cddbplugin.annotations.IntegrationTest;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameter;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;

@Ignore
@Category(IntegrationTest.class)
public class ConnectorTest {

	@Test
	public void givenACorrectConnectionStringWillConnect() throws Exception {
		// given 
		new DriverClassLoader().execute(new Parameters(
				Parameter.aBuilder()
					.withKey(DriverClassLoader.KEY_JAR_FILE_NAME)
					.withValue("./target/test-classes/ojdbc7.jar")
					.build()));
		
		Parameters parameters = new Parameters(
				Parameter.aBuilder()
					.withKey(Connector.KEY_USER)
					.withValue("test")
					.build(),
				Parameter.aBuilder()
					.withKey(Connector.KEY_PASSWORD)
					.withValue("test")
					.build(),
				Parameter.aBuilder()
					.withKey(Connector.KEY_CONNECTION_STRING)
					.withValue("jdbc:oracle:thin:@localhost:1521:xe")
					.build());
		
		// when
		Connector connect = new Connector();

		// then
		assertThat("The connection to the database faild!", connect.execute(parameters), equalTo(Result.ok()));
	}
}
