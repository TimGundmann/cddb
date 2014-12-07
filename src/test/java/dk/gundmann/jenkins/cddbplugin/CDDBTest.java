package dk.gundmann.jenkins.cddbplugin;

import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;

import org.junit.Test;

import dk.gundmann.jenkins.cddbplugin.utils.StringUtil;


public class CDDBTest {

	private AbstractBuild<?, ?> build;
	private Launcher launcher;
	private BuildListener listener;

	private CDDB dbUpdater;

	@Test(expected = DBUpdateException.class)
	public void givenAnEmptyJdbcDriverWillFaile() throws Exception {
		// given
		dbUpdater = new CDDB(StringUtil.EMPTY, StringUtil.EMPTY, StringUtil.EMPTY);

		// when then
		dbUpdater.perform(build, launcher, listener);
	}
	
	
}
