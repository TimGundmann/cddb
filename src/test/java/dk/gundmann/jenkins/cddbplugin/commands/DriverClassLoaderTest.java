package dk.gundmann.jenkins.cddbplugin.commands;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.Test;

import dk.gundmann.jenkins.cddbplugin.Result;
import dk.gundmann.jenkins.cddbplugin.commands.DriverClassLoader;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameter;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;

public class DriverClassLoaderTest {
	
	private DriverClassLoader loader = new DriverClassLoader();

	@Test
	public void givenILoadJdbcDriverTheDriverClassIsFound() throws Exception {
		// given when then
		assertThat("The driver class was not found", loader.execute(new Parameters(Parameter.aBuilder()
					.withKey(DriverClassLoader.KEY_JAR_FILE_NAME)
					.withValue("./target/test-classes/ojdbc7.jar")
					.build())), equalTo(Result.ok()));
	}
	
	public void verifyThatAnExceptiIsThrownWhenNoDriverIsFound() throws Exception {
		// given when then
		assertThat(loader.execute(new Parameters(Parameter.aBuilder().build())), equalTo(Result.faild()));
	}
}
