package com.triadsoft.common.properties;

import java.util.LinkedList;
import java.util.List;

public class PropertyCategory1 {
	private List<PropertyEntry1> entries = new LinkedList<PropertyEntry1>();
	private String name = null;

	public PropertyCategory1() {
	}

	public List<PropertyEntry1> getEntries() {
		return entries;
	}

	public void setEntries(List<PropertyEntry1> entries) {
		this.entries = entries;
	}

	public void addEntry(PropertyEntry1 entry) {
		if (this.entries == null) {
			this.entries = new LinkedList<PropertyEntry1>();
		}
		this.entries.add(entry);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
