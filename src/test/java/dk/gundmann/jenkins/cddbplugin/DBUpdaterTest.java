package dk.gundmann.jenkins.cddbplugin;

import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;

import org.junit.Test;

import dk.gundmann.jenkins.cddbplugin.database.DatabaseException;
import dk.gundmann.jenkins.cddbplugin.utils.StringUtil;


public class DBUpdaterTest {

	private AbstractBuild<?, ?> build;
	private Launcher launcher;
	private BuildListener listener;

	private DBUpdater dbUpdater;

	@Test(expected = DatabaseException.class)
	public void givenAnEmptyJdbcDriverWillFaile() throws Exception {
		// given
		dbUpdater = new DBUpdater(StringUtil.EMPTY, StringUtil.EMPTY);

		// when then
		dbUpdater.perform(build, launcher, listener);
	}
	
	
}
