package dk.gundmann.jenkins.cddbplugin.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

public class StringUtilTest {

	private static final String NOT_EMPTY = "123";

	@Test
	public void givenAnEmptyStringWillSucceed() throws Exception {
		// given when then
		assertThat(StringUtil.isEmpty(StringUtil.EMPTY), is(Boolean.TRUE));
	}
	
	@Test
	public void givenAnNullStringWillSucceed() throws Exception {
		// given when then
		assertThat(StringUtil.isEmpty(StringUtil.NULL), is(Boolean.TRUE));
	}

	@Test
	public void givenAnNonEmptyStringWillFail() throws Exception {
		// given when then
		assertThat(StringUtil.isEmpty(NOT_EMPTY), is(Boolean.FALSE));
	}
	
	@Test
	public void verifyThatStringIsNotEmpty() throws Exception {
		// given when then
		assertThat(StringUtil.isNotEmpty(NOT_EMPTY), is(Boolean.TRUE));
	}
}
