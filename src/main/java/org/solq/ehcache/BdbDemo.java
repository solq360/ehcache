package org.solq.ehcache;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.junit.Test;
import org.solq.ehcache.model.Player;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class BdbDemo {
    static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    String envHomePath = "D:/test/berkeleydata";
    String databaseName = "micmiu-demo";

    @Test
    public void save_byte() throws JsonProcessingException {
	Player data = Player.of(1L, "name");
	byte[] objValues = OBJECT_MAPPER.writeValueAsBytes(data);
	System.out.println(objValues.length);
    }

    @Test
    public void test() {

	BdbManager db = BdbManager.of(envHomePath, databaseName);
	try {
	    db.open();
	    int count = 1000000;
	    long start = System.currentTimeMillis();
	    long end = System.currentTimeMillis();

	    while (count-- > 0) {
		Player data = Player.of(count, "name");
		byte[] keyValues = String.valueOf(count).getBytes();
		byte[] objValues = OBJECT_MAPPER.writeValueAsBytes(data);
		db.put(keyValues, objValues);
	    }
	    end = System.currentTimeMillis();
	    System.out.println(end - start);
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    db.close();
	}

	System.out.println(" ====  Demo Test End  ====");

    }

    @Test
    public void test_all() {

	BdbManager db = BdbManager.of(envHomePath, databaseName);
	try {
	    db.open();
	    int count = 1000000;
	    long start = System.currentTimeMillis();
	    long end = System.currentTimeMillis();

	    while (count-- > 0) {
		byte[] keyValues = String.valueOf(count).getBytes();
		byte[] bytes = db.get(keyValues);
		if (bytes.length > 0) {
		    Player p = OBJECT_MAPPER.readValue(bytes, Player.class);
		}
	    }
	    end = System.currentTimeMillis();
	    System.out.println(end - start);
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    db.close();
	}

	System.out.println(" ====  Demo Test End  ====");

    }

}