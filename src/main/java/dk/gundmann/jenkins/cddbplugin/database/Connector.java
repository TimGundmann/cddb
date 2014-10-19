package dk.gundmann.jenkins.cddbplugin.database;

import java.sql.DriverManager;

import dk.gundmann.jenkins.cddbplugin.Command;
import dk.gundmann.jenkins.cddbplugin.Result;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameter;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;

public class Connector implements Command {

	public static final String KEY_CONNECTION = "connection";
	
	public static final String KEY_CONNECTION_STRING = "connectionString";
	public static final String KEY_USER = "user";
	public static final String KEY_PASSWORD = "password";
	
	@Override
	public Result execute(Parameters parameters) {
		try {
			parameters.add(Parameter.aBuilder()
					.withKey(KEY_CONNECTION)
					.withValue(
						DriverManager.getConnection(
							parameters.valueAsString(KEY_CONNECTION_STRING), 
							parameters.valueAsString(KEY_USER), 
							parameters.valueAsString(KEY_PASSWORD)))
					.build());
			return Result.ok();
		} catch (Exception e) {
			return Result.faild("Error connectin to the database, the connection string migth not be correct", e); 
		}
	}

}
