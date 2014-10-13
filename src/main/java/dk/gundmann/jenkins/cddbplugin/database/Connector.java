package dk.gundmann.jenkins.cddbplugin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

	private String connectionString;
	private String password;
	private String user;

	public Connector withConnectionString(String connectionString) {
		this.connectionString = connectionString;
		return this;
	}

	public Connector withUser(String user) {
		this.user = user;
		return this;
	}

	public Connector withPassword(String password) {
		this.password = password;
		return this;
	}

	public Connection connect() {
		try {
			return DriverManager.getConnection(connectionString, user, password);
		} catch (SQLException e) {
			throw new DatabaseException("Error connectin to the database, the connection string migth not be correct", e); 
		}
	}

}
