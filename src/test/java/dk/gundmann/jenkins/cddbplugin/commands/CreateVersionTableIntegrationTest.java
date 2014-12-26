package dk.gundmann.jenkins.cddbplugin.commands;

import static org.hamcrest.MatcherAssert.assertThat;

import java.sql.Connection;

import org.junit.Test;

import dk.gundmann.jenkins.cddbplugin.commands.CreateVersionTable;
import dk.gundmann.jenkins.cddbplugin.commands.TableNameResolver;
import dk.gundmann.jenkins.cddbplugin.utils.TestUtils;

public class CreateVersionTableIntegrationTest {

	private TestUtils testUtils = new TestUtils();
	
	@Test
	public void verifyThatTheTabelIsConnected() throws Exception {
		// given when
		new CreateVersionTable().execute(testUtils.getParameters());

		// then
		Connection connection = testUtils.getConnection();
		assertThat("The tabel was not created", connection.createStatement().execute("select * from " + TableNameResolver.DEFAULT_TABLE_NAME));
	}
}
