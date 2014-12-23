package dk.gundmann.jenkins.cddbplugin.database;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import java.util.Date;

import org.junit.Test;

import dk.gundmann.jenkins.cddbplugin.Result;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameter;
import dk.gundmann.jenkins.cddbplugin.utils.TestUtils;

public class ResolveSqlScriptTest {

	private static final String UPDATE_FOLDER_NOT_EXISTS = "src/test/resources/ccsdfsfd";

	private static final String UPDATES_FOLDER = "src/test/resources/updates";

	private ResolveSqlScript extractSqlScript = new ResolveSqlScript();

	private TestUtils testUtils = new TestUtils();
	
	@Test
	public void verifyThatIfNoUpdateScriptExistsNothingHappens() throws Exception {
		// given when
		Result result = extractSqlScript.execute(testUtils.getParameters().addAll(
				Parameter.aBuilder()
					.withKey(ResolveSqlScript.KEY_VERSION)
					.withValue("1.0")
					.build(),
				Parameter.aBuilder()
					.withKey(ResolveSqlScript.SCRIPT_FOLDER)
					.withValue("src/test/resources/emptyupdate")
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
		Date now = new Date();
		testUtils.createUpdateFile(UPDATES_FOLDER + "/newUpdateFile.sql");
		
		testUtils.insertRowWithApplicationVersion(1, "0.9" , "updatetabel.sql", now);
		
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
