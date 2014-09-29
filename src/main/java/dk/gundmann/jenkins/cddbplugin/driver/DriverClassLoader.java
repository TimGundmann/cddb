package dk.gundmann.jenkins.cddbplugin.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class DriverClassLoader {

	public void updateJdbcDriver(String jarFileName) {
		try {
			DriverManager.registerDriver(findDriverLClass(jarFileName, createClassLoader(jarFileName)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private URLClassLoader createClassLoader(String jarFileName) throws MalformedURLException {
		return new URLClassLoader(new URL[] {createURLFrom(jarFileName)}, System.class.getClassLoader());
	}

	private URL createURLFrom(String jarFileName) throws MalformedURLException {
		return new File(jarFileName).toURL();
	}
	
	private Driver findDriverLClass(String jarFile, URLClassLoader classLoader) {
		JarInputStream is = null;
		try {
			is = new JarInputStream(new FileInputStream(jarFile));
			JarEntry entry;
			while ((entry = is.getNextJarEntry()) != null) {
				if (isADriverClass(entry, classLoader)) {
					return (Driver)classLoader.loadClass(convertToClassName(entry)).newInstance();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		throw new DriverClassNotFoundException(jarFile);
	}

	private String convertToClassName(JarEntry entry) {
		return entry.getName().replace("/", ".").replace(".class", "");
	}

	private boolean isADriverClass(JarEntry entry, URLClassLoader classLoader) {
		if (entry.getName().endsWith(".class")) {
			try {
				return Driver.class.isAssignableFrom(classLoader.loadClass(convertToClassName(entry)));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return false; 
	}
	
}
