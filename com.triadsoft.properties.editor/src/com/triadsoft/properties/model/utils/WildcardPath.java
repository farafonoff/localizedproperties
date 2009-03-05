package com.triadsoft.properties.model.utils;

import java.util.Locale;

/**
 * Esta clase es la encargada de encapsular todos la logica para poder parsear y
 * descubrir los datos referidos al path
 * 
 * @author triad
 * 
 */
public class WildcardPath {
	public static final String ROOT_WILDCARD = "{root}";
	public static final String FILENAME_WILDCARD = "{filename}";
	public static final String FILE_EXTENSION_WILDCARD = "{fileextension}";
	public static final String COUNTRY_WILDCARD = "{country}";
	public static final String LANGUAGE_WILDCARD = "{lang}";

	private String wildcardpath;
	private String path = "";

	public WildcardPath(String wildcardpath) {
		this.wildcardpath = wildcardpath;
		this.path = "";
	}

	public String getWildcardpath() {
		return wildcardpath;
	}

	public void setWildcardpath(String wildcardpath) {
		this.wildcardpath = wildcardpath;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public WildcardPath replace(Locale locale) {
		this.replace(LANGUAGE_WILDCARD, locale.getLanguage());
		this.replace(COUNTRY_WILDCARD, locale.getCountry());
		return this;
	}
	
	public WildcardPath replace() {
		this.replace(scapedWildcard(ROOT_WILDCARD),"[a-zA-Z\\-\\_]+");
		this.replace(scapedWildcard(FILENAME_WILDCARD),"[a-zA-Z\\-\\_]+");
		this.replace(scapedWildcard(FILE_EXTENSION_WILDCARD),"[a-zA-Z\\-\\_]+");
		this.replace(scapedWildcard(LANGUAGE_WILDCARD),"[a-z]{2}");
		this.replace(scapedWildcard(COUNTRY_WILDCARD),"[A-Z]{2}");
		return this;
	}

	public WildcardPath replace(String wildcard, String value) {
		if (path.equals("") || path.length() == 0) {
			path = wildcardpath.replaceAll("\\.", "\\\\.");
		}
		path = path.replaceAll(scapedWildcard(wildcard), value);
		return this;
	}
	
	private String scapedWildcard(String wildcard){
		String rep = wildcard;
		rep = rep.replaceAll("\\{", "\\\\{");
		rep = rep.replaceAll("\\}", "\\\\}");
		return rep;
	}

	public static void main(String[] args) {
		WildcardPath wp = new WildcardPath(
				"/{root}/{filename}.{lang}_{country}.{fileextension}");
		wp.replace(ROOT_WILDCARD,"locale").replace(FILENAME_WILDCARD, "alert").replace(FILE_EXTENSION_WILDCARD,"properties");
		wp.replace(Locale.getDefault());
		System.out.println(wp.getPath());
	}
}
