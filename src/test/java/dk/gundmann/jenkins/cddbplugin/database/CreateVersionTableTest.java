package dk.gundmann.jenkins.cddbplugin.database;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import dk.gundmann.jenkins.cddbplugin.Result;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameter;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;
import dk.gundmann.jenkins.cddbplugin.utils.StringUtil;

@RunWith(MockitoJUnitRunner.class)
public class CreateVersionTableTest {

	private static final String GIVEN_TABLE_NAME = "given table name";

	@Mock
	private Connection connection;
	
	@Mock
	private Statement statement;
	
	private CreateVersionTable createVersionTable = new CreateVersionTable();
	
	private Parameters parameters;
	
	@Before
	public void setUp() throws Exception {
		doReturn(statement).when(connection).createStatement();
		parameters = new Parameters(Parameter.aBuilder()
				.withKey(Connector.KEY_CONNECTION)
				.withValue(connection)
				.build());
	}
	
	@Test
	public void verifyThatTheTableCanBeCreated() throws Exception {
		// given 
		setUpTableExists(false);
		
		// when then
		assertThat(createVersionTable.execute(parameters), equalTo(Result.ok()));
		verify(connection).createStatement();
		verify(statement).execute(contains("create table"));
	}
	
	@Test
	public void verifyThatIfATableExistsThenItIsNotCreated() throws Exception {
		// given
		setUpTableExists(true);

		// when then
		assertThat(createVersionTable.execute(parameters), equalTo(Result.ok()));
		verifyZeroInteractions(statement);
	}
	
	@Test
	public void verifyThatTheDefaultTableNameIsSetIfNonSpecified() throws Exception {
		// given
		setUpTableExists(false);

		// when
		Result result = createVersionTable.execute(parameters.add(
				Parameter.aBuilder()
					.withKey(CreateVersionTable.KEY_VERSION_TABLE_NAME)
					.withValue(StringUtil.EMPTY)
					.build()));

		// then
		assertThat(result, equalTo(Result.ok()));
		verify(statement).execute(contains(CreateVersionTable.DEFAULT_TABLE_NAME));
	}
	
	@Test
	public void givenATableNameWillUseIt() throws Exception {
		// given
		setUpTableExists(false);

		// when
		Result result = createVersionTable.execute(parameters.add(
				Parameter.aBuilder()
					.withKey(CreateVersionTable.KEY_VERSION_TABLE_NAME)
					.withValue(GIVEN_TABLE_NAME)
					.build()));

		// then
		assertThat(result, equalTo(Result.ok()));
		verify(statement).execute(contains(GIVEN_TABLE_NAME));
	}

	@Test
	public void givenAnExceptionWillFail() throws Exception {
		// given when then
		assertThat(createVersionTable.execute(new Parameters()), equalTo(Result.faild()));
	}
	
	private void setUpTableExists(boolean exists) throws SQLException {
		DatabaseMetaData metaData = mock(DatabaseMetaData.class);
		ResultSet resultSet = mock(ResultSet.class); 

		when(connection.getMetaData()).thenReturn(metaData);
		when(metaData.getTables(anyString(), anyString(), anyString(), any(String[].class))).thenReturn(resultSet);
		when(resultSet.first()).thenReturn(exists);
	}
}
