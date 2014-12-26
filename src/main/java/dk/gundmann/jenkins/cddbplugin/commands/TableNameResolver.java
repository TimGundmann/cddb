package dk.gundmann.jenkins.cddbplugin.commands;

import dk.gundmann.jenkins.cddbplugin.Command;
import dk.gundmann.jenkins.cddbplugin.Result;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameter;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;
import dk.gundmann.jenkins.cddbplugin.utils.StringUtil;

public class TableNameResolver implements Command {

	public static final String DEFAULT_TABLE_NAME = "dbupdate_version";
	public static final String KEY_VERSION_TABLE_NAME = "versionTableName";

	@Override
	public Result execute(Parameters parameters) {
		String tableName = parameters.valueAsString(KEY_VERSION_TABLE_NAME);
		if (StringUtil.isEmpty(tableName)) {
			parameters.add(Parameter.aBuilder()
					.withKey(KEY_VERSION_TABLE_NAME)
					.withValue(DEFAULT_TABLE_NAME)
					.build());
		}
		return Result.ok();
	}

}
