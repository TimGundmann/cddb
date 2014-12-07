package dk.gundmann.jenkins.cddbplugin.database;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.sql.Connection;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import dk.gundmann.jenkins.cddbplugin.Result;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameter;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class ResolveSqlScriptTest {

	@Mock
	private Connection connection;	

	@Mock
	private Statement statement;
	
	private ResolveSqlScript extractSqlScript = new ResolveSqlScript();

	private Parameters parameters;
	
	@Before
	public void setUp() throws Exception {
		parameters = new Parameters(Parameter.aBuilder()
				.withKey(Connector.KEY_CONNECTION)
				.withValue(connection)
				.build());
	}

	@Test
	public void verifyThatIfNoUpdateScriptExistsNothingHappens() throws Exception {
		// given when
		Result result = extractSqlScript.execute(parameters.addAll(
				Parameter.aBuilder()
					.withKey(ResolveSqlScript.SCRIPT_FOLDER)
					.build()));

		// then
		assertThat(result, equalTo(Result.ok()));
		assertThat(parameters.getParameter(ResolveSqlScript.SCRIPTS), nullValue());
	}
	
	@Test
	public void givenAnUpdateFileThatIsNotUpdatedWillReturnIt() throws Exception {
		// given 
		doReturn(statement).when(connection).createStatement();
		
		// when
		Result result = extractSqlScript.execute(parameters.addAll(
				Parameter.aBuilder()
					.withKey(ResolveSqlScript.SCRIPT_FOLDER)
					.withValue("Folder")
					.build()));

		// then
		assertThat(result, equalTo(Result.ok()));
		verify(statement).execute(anyString());
	}
	
}
