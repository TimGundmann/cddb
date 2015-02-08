package dk.gundmann.jenkins.cddbplugin.commands;

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
import dk.gundmann.jenkins.cddbplugin.utils.DatabaseAccess;

@RunWith(MockitoJUnitRunner.class)
public class CreateVersionTableTest {

	private static final String GIVEN_TABLE_NAME = "given table name";

	@Mock
	private DatabaseAccess databaseAccess;
	
	@Mock
	private Statement statement;
	
	private CreateVersionTable createVersionTable = new CreateVersionTable();
	
	private Parameters parameters;
	
	@Before
	public void setUp() throws Exception {
		parameters = new Parameters(Parameter.aBuilder()
				.withKey(DatabaseConnector.KEY_DATABASE_ACCESS)
				.withValue(databaseAccess)
				.build());
	}
	
	@Test
	public void verifyThatTheTablesIsCreated() throws Exception {
		// given when then
		assertThat(createVersionTable.execute(parameters), equalTo(Result.ok()));
	}
	
	@Test
	public void givenATableNameWillUseIt() throws Exception {
		// given when
		Result result = createVersionTable.execute(parameters.add(
				Parameter.aBuilder()
					.withKey(TableNameResolver.KEY_VERSION_TABLE_NAME)
					.withValue(GIVEN_TABLE_NAME)
					.build()));

		// then
		assertThat(result, equalTo(Result.ok()));
		verify(databaseAccess).createTable(contains(GIVEN_TABLE_NAME));
	}

	@Test
	public void givenAnExceptionWillFail() throws Exception {
		// given when then
		assertThat(createVersionTable.execute(new Parameters()), equalTo(Result.faild()));
	}
	
}
