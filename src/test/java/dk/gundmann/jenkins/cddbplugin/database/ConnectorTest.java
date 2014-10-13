package dk.gundmann.jenkins.cddbplugin.database;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import dk.gundmann.jenkins.cddbplugin.annotations.IntegrationTest;

@Ignore
@Category(IntegrationTest.class)
public class ConnectorTest {

	@Test
	public void givenACorrectConnectionStringWillConnect() throws Exception {
		// given 
		new DriverClassLoader().registerJdbcDriver("./target/test-classes/ojdbc7.jar");
		
		// when
		Connector connect = new Connector()
				.withUser("test")
				.withPassword("test")
				.withConnectionString("jdbc:oracle:thin:@localhost:1521:xe");

		// then
		assertThat("The connection to the database faild!", connect.connect(), notNullValue());
	}
}
