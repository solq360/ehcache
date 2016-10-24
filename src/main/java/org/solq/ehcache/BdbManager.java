package org.solq.ehcache;

import java.io.File;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class BdbManager {
    private String envHomePath;
    private String databaseName;
    private Environment dbEnv;
    private Database database;

    public void open() {
	EnvironmentConfig envCfg = new EnvironmentConfig();
	envCfg.setAllowCreate(true);
	envCfg.setTransactional(true);
	File file = new File(envHomePath);
	if (!file.canExecute()) {
	    file.mkdirs();
	}
	DatabaseConfig dbCfg = new DatabaseConfig();
	dbCfg.setAllowCreate(true);
	dbCfg.setTransactional(false);

	try {
	    dbEnv = new Environment(file, envCfg);
	    database = dbEnv.openDatabase(null, databaseName, dbCfg);
	} catch (DatabaseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void close() {
	if (null != database) {
	    try {
		database.close();
	    } catch (DatabaseException e) {
		e.printStackTrace();
	    }
	}
	if (null != dbEnv) {
	    // 在关闭环境前清理下日志
	    try {
		dbEnv.cleanLog();
		dbEnv.close();
		dbEnv = null;
	    } catch (DatabaseException e) {
		e.printStackTrace();
	    }

	}
    }

    public OperationStatus put(byte[] keyValues, byte[] objValues) {
	DatabaseEntry beanKeyEntry = new DatabaseEntry(keyValues);
	DatabaseEntry beanValEntry = new DatabaseEntry(objValues);
	OperationStatus status = null;
	try {
	    status = database.put(null, beanKeyEntry, beanValEntry);
	} catch (DatabaseException e) {
	    e.printStackTrace();
	}
	return status;
    }

    public byte[] get(byte[] keyValues) {
	DatabaseEntry beanKeyEntry = new DatabaseEntry(keyValues);
	DatabaseEntry valEntryGet = new DatabaseEntry();
	try {
	    OperationStatus status = database.get(null, beanKeyEntry, valEntryGet, LockMode.DEFAULT);
	} catch (DatabaseException e) {
 	    e.printStackTrace();
	}
	return valEntryGet.getData();
    }

    public static BdbManager of(String envHomePath, String databaseName) {
	BdbManager ret = new BdbManager();
	ret.databaseName = databaseName;
	ret.envHomePath = envHomePath;
	return ret;
    }

    public String getEnvHomePath() {
	return envHomePath;
    }

    public String getDatabaseName() {
	return databaseName;
    }

    public Environment getDbEnv() {
	return dbEnv;
    }

    public Database getDatabase() {
	return database;
    }

}
