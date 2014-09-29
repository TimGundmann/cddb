package dk.gundmann.jenkins.cddbplugin.driver;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.Test;

public class DriverClassLoaderTest {
	
	private DriverClassLoader loader = new DriverClassLoader();

	@Test
	public void givenILoadJdbcDriverTheDriverClassIsFound() throws Exception {
		// given 
//		File f = new File("./target/test-classes/ojdbc7.jar");
//		f.exists();
//	    URLClassLoader urlCl = new URLClassLoader(new URL[] { f.toURL()},System.class.getClassLoader());
//	    Class testClass = urlCl.loadClass("oracle.net.ano.Ano");
//	    testClass.newInstance();
	    
	    
		loader.updateJdbcDriver("./target/test-classes/ojdbc7.jar");
//		
//		// when then
//		assertThat("The driver class was not found", locator.findDriverLClass("target/test-classes/ojdbc7.jar"), is(notNullValue()));
	}
	
}
