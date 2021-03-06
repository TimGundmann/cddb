package dk.gundmann.jenkins.cddbplugin.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import dk.gundmann.jenkins.cddbplugin.Command;
import dk.gundmann.jenkins.cddbplugin.Result;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameter;
import dk.gundmann.jenkins.cddbplugin.parameters.Parameters;
import dk.gundmann.jenkins.cddbplugin.utils.DatabaseAccess;

public class ResolveSqlScript implements Command {

	public static final String SCRIPT_FOLDER = "scriptFolder";
	public static final String SCRIPTS = "scripts";
	public static final String ROLLBACK = "rollback";
	public static final String DBVERSION = "dbVersion";
	public static final String KEY_VERSION = "version";

	private static final String SELECT_LAST_VERSION = "select databaseVersion from %s order by createdDate desc";
	private static final String SELECT_VERSION = "select databaseVersion from %1$s where applicationVersion = '%2$s' order by createdDate desc";
	
	@Override
	public Result execute(Parameters parameters) {
		File dir = new File(parameters.getParameter(SCRIPT_FOLDER).getValue().toString());
		Result result = validate(dir);
		if (result.isOk()) {
			findFiles(parameters, dir);
		}
		return result;
	}

	private void findFiles(Parameters parameters, File dir) {
		Integer dbVersion = findDBVersion(parameters);
		if (dbVersion == 0) {
			dbVersion = findNewestDBVersion(parameters);
			if (dbVersion == 0) {
				generateAllFileList(dir, parameters);
			} else {
				addVersionTo(parameters, dbVersion);
				generateNewFiles(dir, parameters);
			}
		} else {
			addVersionTo(parameters, dbVersion);
			generateRollBackFiles(dir, parameters);
		}
	}

	private void addVersionTo(Parameters parameters, Integer dbVersion) {
		parameters.add(Parameter.aBuilder().withKey(DBVERSION).withValue(dbVersion).build());
	}

	private void generateAllFileList(File dir, Parameters parameters) {
		parameters.add(Parameter.aBuilder().withKey(SCRIPTS).withValue(createFileList(dir)).build());
	}

	private void generateNewFiles(File dir, Parameters parameters) {
		parameters.add(Parameter.aBuilder().withKey(SCRIPTS).withValue(createFilesNewerThan(queryForLastInsertDate(parameters), dir)).build());
	}

	private void generateRollBackFiles(File dir, Parameters parameters) {
		parameters.add(Parameter.aBuilder().withKey(SCRIPTS).withValue(createFilesFromDBList(dir, parameters)).build());
		parameters.add(Parameter.aBuilder().withKey(ROLLBACK).withValue(Boolean.TRUE).build());
	}

	private Result validate(File dir) {
		if (!dir.exists()) {
			return Result.faild("The SQL update directory did not exists with path: " + dir.getAbsolutePath());
		}
		return Result.ok();
	}

	private Object createFilesFromDBList(File dir, Parameters parameters) {
		List<File> updateFiles = new ArrayList<File>();
		for (String file : getDatabaseAccess(parameters).getFileNamesFromDB(getTabelName(parameters), getDatabaseVerion(parameters))) {
			updateFiles.add(new File(file));
		}
		return updateFiles;
	}
	
	private Object createFilesNewerThan(Date lastDate, File dir) {
		System.out.println("db date: " + lastDate);
		List<File> updateFiles = new ArrayList<File>();
		if (dir.list() != null) {
			for (String fileName : dir.list()) {
				File file = new File(dir + "/" + fileName);
				try {
					BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
					System.out.print(attr.creationTime());
					System.out.println(" " + file.toPath());
					if (attr.creationTime().to(TimeUnit.MILLISECONDS) > lastDate.getTime()) {
						System.out.println("Added: " + file.toPath());
						updateFiles.add(file);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return updateFiles;
	}

	private List<File> createFileList(File dir) {
		List<File> updateFiles = new ArrayList<File>();
		if (dir.list() != null) {
			for (String file : dir.list()) {
				updateFiles.add(new File(file));
			}
		}
		return updateFiles;
	}

	private Integer findNewestDBVersion(Parameters parameters) {
		return getDatabaseAccess(parameters).getLatestDBVersion(getTabelName(parameters));
	}

	private Integer findDBVersion(Parameters parameters) {
		return getDatabaseAccess(parameters).findDBVersionBy(getRequestVersion(parameters), getTabelName(parameters));
	}

	private Date queryForLastInsertDate(Parameters parameters) {
		return getDatabaseAccess(parameters).getLatestDate(getTabelName(parameters));
	}

	private int getDatabaseVerion(Parameters parameters) {
		return parameters.valueAsType(DBVERSION, Integer.class);
	}

	private String getRequestVersion(Parameters parameters) {
		return parameters.valueAsString(KEY_VERSION);
	}

	private String getTabelName(Parameters parameters) {
		return parameters.valueAsString(TableNameResolver.KEY_VERSION_TABLE_NAME);
	}

	private DatabaseAccess getDatabaseAccess(Parameters parameters) {
		return parameters.valueAsType(DatabaseConnector.KEY_DATABASE_ACCESS, DatabaseAccess.class);
	}

}
