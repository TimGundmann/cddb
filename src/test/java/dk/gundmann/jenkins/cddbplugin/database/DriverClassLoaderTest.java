package dk.gundmann.jenkins.cddbplugin.database;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Test;

import dk.gundmann.jenkins.cddbplugin.database.DriverClassLoader;
import dk.gundmann.jenkins.cddbplugin.database.DriverClassNotFoundException;

public class DriverClassLoaderTest {
	
	private DriverClassLoader loader = new DriverClassLoader();

	@Test
	public void givenILoadJdbcDriverTheDriverClassIsFound() throws Exception {
		// given when then
		assertThat("The driver class was not found", loader.registerJdbcDriver("./target/test-classes/ojdbc7.jar"), is(notNullValue()));
	}
	
	@Test(expected=DriverClassNotFoundException.class)
	public void verifyThatAnExceptiIsThrownWhenNoDriverIsFound() throws Exception {
		// given when then
		loader.registerJdbcDriver("");
	}
}
