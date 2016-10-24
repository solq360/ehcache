package org.solq.ehcache.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player implements Serializable {

    private long id;
    private String name;

    private List<String> items;
    private Map<Integer, Long> records;

    public static Player of(long id, String name) {
	int count = 200;
	Player ret = new Player();
	ret.id = id;
	ret.name = name;

	ret.items = new ArrayList<String>(count);
	for (int i = 0; i < count; i++) {
	    ret.items.add(i + "");
	}

	ret.records = new HashMap<>(count);
	for (int i = 0; i < count; i++) {
	    ret.records.put(i, 1L);
	}

	return ret;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (int) (id ^ (id >>> 32));
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Player other = (Player) obj;
	if (id != other.id)
	    return false;
	return true;
    }

    // get and setter
    public void setId(long id) {
	this.id = id;
    }

    public void setItems(List<String> items) {
	this.items = items;
    }

    public void setRecords(Map<Integer, Long> records) {
	this.records = records;
    }

    public long getId() {
	return id;
    }

    public List<String> getItems() {
	return items;
    }

    public Map<Integer, Long> getRecords() {
	return records;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

}
