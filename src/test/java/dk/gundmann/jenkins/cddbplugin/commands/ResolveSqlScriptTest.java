package dk.gundmann.jenkins.cddbplugin.commands;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Test;

import dk.gundmann.jenkins.cddbplugin.Result;
import dk.gundmann.jenkins.cddbplugin.commands.ResolveSqlScript;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameter;
import dk.gundmann.jenkins.cddbplugin.utils.TestUtils;

public class ResolveSqlScriptTest {

	private static final String UPDATE_FOLDER_EMPTY = "src/test/resources/emptyupdate";

	private static final String UPDATE_FOLDER_NOT_EXISTS = "src/test/resources/ccsdfsfd";

	private static final String UPDATES_FOLDER = "src/test/resources/updates";

	private static final String NEW_UPDATE_FILE_NAME = UPDATES_FOLDER + "/newUpdateFile.sql";

	private ResolveSqlScript extractSqlScript = new ResolveSqlScript();

	private TestUtils testUtils = new TestUtils();

	@After
	public void tearDown() {
		testUtils.removeFile(NEW_UPDATE_FILE_NAME);
	}
	
	@Test
	public void verifyThatIfNoUpdateScriptExistsNothingHappens() throws Exception {
		// given
		testUtils.makeDir(UPDATE_FOLDER_EMPTY);
		
		// when
		Result result = extractSqlScript.execute(testUtils.getParameters().addAll(
				Parameter.aBuilder()
					.withKey(ResolveSqlScript.KEY_VERSION)
					.withValue("1.0")
					.build(),
				Parameter.aBuilder()
					.withKey(ResolveSqlScript.SCRIPT_FOLDER)
					.withValue(UPDATE_FOLDER_EMPTY)
					.build()));

		// then
		assertThat(result, equalTo(Result.ok()));
		assertThat(testUtils.getParameters().valueAsFiles(ResolveSqlScript.SCRIPTS).size(), equalTo(0));
	}
	
	@Test
	public void verifyThatAnErrorOccurredWhenNoFolderExists() throws Exception {
		// given when
		Result result = extractSqlScript.execute(testUtils.getParameters().addAll(
				Parameter.aBuilder()
					.withKey(ResolveSqlScript.SCRIPT_FOLDER)
					.withValue(UPDATE_FOLDER_NOT_EXISTS)
					.build()));

		// then
		assertThat(result, equalTo(Result.faild()));
	}
	
	@Test
	public void givenNoVersionInTheDatabaseWillReturnAllUpdateFiles() throws Exception {
		// given when
		Result result = extractSqlScript.execute(testUtils.getParameters().addAll(
				Parameter.aBuilder()
					.withKey(ResolveSqlScript.KEY_VERSION)
					.withValue("1.0")
					.build(),
				Parameter.aBuilder()
					.withKey(ResolveSqlScript.SCRIPT_FOLDER)
					.withValue(UPDATES_FOLDER)
					.build()));

		// then
		assertThat(result, equalTo(Result.ok()));
		assertThat(testUtils.getParameters().valueAsFiles(ResolveSqlScript.SCRIPTS).size(), greaterThan(1));
	}
	
	@Test
	public void givenAnSqlFileThatIsNotUpdatedAndANewVersionWillReturnIt() throws Exception {
		// given
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -2);
		testUtils.createUpdateFile(NEW_UPDATE_FILE_NAME);
		
		testUtils.insertRowWithApplicationVersion(1, "0.9" , "updatetabel.sql", calendar.getTime());
		
		// when
		Result result = extractSqlScript.execute(testUtils.getParameters().addAll(
				Parameter.aBuilder()
					.withKey(ResolveSqlScript.KEY_VERSION)
					.withValue("1.0")
					.build(),
				Parameter.aBuilder()
					.withKey(ResolveSqlScript.SCRIPT_FOLDER)
					.withValue(UPDATES_FOLDER)
					.build()));

		// then
		assertThat(result, equalTo(Result.ok()));
		assertThat(testUtils.getParameters().valueAsFiles(ResolveSqlScript.SCRIPTS).size(), equalTo(1));
	}

	@Test
	public void verifyThatGivenAnExistingVersionWillReturnFiles() throws Exception {
		// given
		testUtils.insertRowWithApplicationVersion(1, "1.0", "updatetabel.sql");
		testUtils.insertRowWithApplicationVersion(2, "1.1", "createTable.sql");

		// when
		Result result = extractSqlScript.execute(testUtils.getParameters().addAll(
				Parameter.aBuilder()
					.withKey(ResolveSqlScript.KEY_VERSION)
					.withValue("1.0")
					.build(),
				Parameter.aBuilder()
					.withKey(ResolveSqlScript.SCRIPT_FOLDER)
					.withValue(UPDATES_FOLDER)
					.build()));

		// then
		assertThat(result, equalTo(Result.ok()));
		assertThat(testUtils.getParameters().valueAsFiles(ResolveSqlScript.SCRIPTS).size(), equalTo(2));
	}
}
