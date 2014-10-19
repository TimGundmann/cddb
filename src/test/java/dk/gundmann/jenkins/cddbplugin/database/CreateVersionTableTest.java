package dk.gundmann.jenkins.cddbplugin.database;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.Statement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import dk.gundmann.jenkins.cddbplugin.parameters.Parameter;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;

@RunWith(MockitoJUnitRunner.class)
public class CreateVersionTableTest {

	@Mock
	private Connection connnection;
	
	@Mock
	private Statement statement;
	
	private CreateVersionTable createVersionTable = new CreateVersionTable();
	
	@Test
	public void verifyThatTheTableCanBeCreated() throws Exception {
		// given
		doReturn(statement).when(connnection).createStatement();

		// when
		createVersionTable.execute(new Parameters(Parameter.aBuilder()
				.withKey(Connector.KEY_CONNECTION)
				.withValue(connnection)
				.build()));

		// then
		verify(connnection).createStatement();
		verify(statement).execute(anyString());
	}
}
