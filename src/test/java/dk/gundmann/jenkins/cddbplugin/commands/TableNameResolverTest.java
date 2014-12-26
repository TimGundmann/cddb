package dk.gundmann.jenkins.cddbplugin.commands;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

import org.junit.Before;
import org.junit.Test;

import dk.gundmann.jenkins.cddbplugin.Result;
import dk.gundmann.jenkins.cddbplugin.commands.TableNameResolver;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameter;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;
import dk.gundmann.jenkins.cddbplugin.utils.StringUtil;


public class TableNameResolverTest {

	private TableNameResolver tableNameResolver = new TableNameResolver();
	
	private Parameters parameters;
	
	@Before
	public void setUp() throws Exception {
		parameters = new Parameters();
	}
	

	@Test
	public void verifyThatTheDefaultTableNameIsSetIfNonSpecified() throws Exception {
		// given when
		Result result = tableNameResolver.execute(parameters.add(
				Parameter.aBuilder()
					.withKey(TableNameResolver.KEY_VERSION_TABLE_NAME)
					.withValue(StringUtil.EMPTY)
					.build()));

		// then
		assertThat(result, equalTo(Result.ok()));
		assertThat("The default table was not added to the parameters", parameters.valueAsString(TableNameResolver.KEY_VERSION_TABLE_NAME), not(StringUtil.EMPTY));
	}
	
}
