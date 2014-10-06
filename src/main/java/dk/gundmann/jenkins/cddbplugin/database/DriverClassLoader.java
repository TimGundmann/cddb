package dk.gundmann.jenkins.cddbplugin.database;

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

	public Driver registerJdbcDriver(String jarFileName) {
		try {
			Driver driver = findDriverLClass(jarFileName, createClassLoader(jarFileName));
			DriverManager.registerDriver(driver);
			return driver; 
		} catch (Exception e) {
			throw new DriverClassNotFoundException(jarFileName, e);
		}
	}

	private Driver findDriverLClass(String jarFile, URLClassLoader classLoader) throws Exception {
		JarInputStream is = null;
		try {
			is = new JarInputStream(new FileInputStream(jarFile));
			JarEntry entry;
			while ((entry = is.getNextJarEntry()) != null) {
				if (isADriverClass(entry, classLoader)) {
					return (Driver) classLoader.loadClass(convertToClassName(entry)).newInstance();
				}
			}
		} finally {
			is.close();
		}
		throw new DriverClassNotFoundException(jarFile);
	}

	private URLClassLoader createClassLoader(String jarFileName) throws MalformedURLException {
		return new URLClassLoader(new URL[] { createURLFrom(jarFileName) }, System.class.getClassLoader());
	}

	@SuppressWarnings("deprecation")
	private URL createURLFrom(String jarFileName) throws MalformedURLException {
		return new File(jarFileName).toURL();
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
