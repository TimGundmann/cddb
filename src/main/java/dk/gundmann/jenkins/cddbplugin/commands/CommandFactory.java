package dk.gundmann.jenkins.cddbplugin.commands;

import java.util.Arrays;
import java.util.List;

import dk.gundmann.jenkins.cddbplugin.Command;

public class CommandFactory {
	
	public List<Command> createCommands() {
    	return Arrays.asList(new Command[] {
    			new TableNameResolver(),
    			new DriverClassLoader(),
    			new DatabaseConnector(),
    			new ResolveSqlScript()
    	});
	}
	
}
