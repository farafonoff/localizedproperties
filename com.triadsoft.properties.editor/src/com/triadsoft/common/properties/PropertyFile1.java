package com.triadsoft.common.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class PropertyFile1 extends Properties {
	private List<PropertyCategory1> categories = new LinkedList<PropertyCategory1>();

	private PropertyCategory1 defaultCategory;

	public PropertyCategory1 getDefaultCategory() {
		return defaultCategory;
	}

	public void setDefaultCategory(PropertyCategory1 defaultCategory) {
		this.defaultCategory = defaultCategory;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -8571609999901052828L;

	public PropertyFile1(File file) {
		super();
		try {
			FileInputStream fis = new FileInputStream(file);
			load(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PropertyFile1(InputStream stream) {
		super();
		try {
			load(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void load(InputStream inStream) throws IOException {
		super.load(inStream);
		defaultCategory = new PropertyCategory1();
		defaultCategory.setName("Default");
		categories.add(defaultCategory);
		Enumeration enumeration = keys();
		while (enumeration.hasMoreElements()) {
			String key = (String) enumeration.nextElement();
			PropertyEntry1 entry = new PropertyEntry1();
			entry.setKey(key);
			entry.setValue(getProperty(key));
			defaultCategory.addEntry(entry);
		}
	}

	public List<PropertyCategory1> getCategories() {
		return categories;
	}
}
