package com.triadsoft.properties.model.utils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	/**
	 * This method replace the language and country into the path string 
	 * @param locale
	 * @return
	 */
	public WildcardPath replace(Locale locale) {
		this.replace(LANGUAGE_WILDCARD, locale.getLanguage());
		this.replace(COUNTRY_WILDCARD, locale.getCountry());
		return this;
	}
	
	/**
	 * Reset the path to initial state
	 */
	public void resetPath(){
		this.path = wildcardpath;
		this.path = wildcardpath.replaceAll("\\.", "\\\\.");
	}

	/**
	 * This method return a wildcard path as regular expresion
	 */
	public WildcardPath replaceToRegex() {
		resetPath();
		this.replace(ROOT_WILDCARD, "[a-zA-Z\\-\\_]+");
		this.replace(FILENAME_WILDCARD, "[a-zA-Z\\-\\_]+");
		this.replace(FILE_EXTENSION_WILDCARD, "[a-zA-Z\\-\\_]+");
		this.replace(LANGUAGE_WILDCARD, "[a-z]{2}");
		this.replace(COUNTRY_WILDCARD, "[A-Z]{2}");
		return this;
	}

	public WildcardPath replace(String wildcard, String value) {
		if (path.equals("") || path.length() == 0) {
			path = wildcardpath.replaceAll("\\.", "\\\\.");
		}
		path = path.replaceAll(escapedWildcard(wildcard), value);
		return this;
	}

	/**
	 * Return an string that contain the wildcard string with the curly braces
	 * chars escaped
	 * 
	 * @param wildcard
	 * @return
	 */
	private String escapedWildcard(String wildcard) {
		String rep = wildcard;
		rep = rep.replaceAll("\\{", "\\\\{");
		rep = rep.replaceAll("\\}", "\\\\}");
		return rep;
	}

	/**
	 * This method return true if the file path match with the wildcard path
	 * loaded into the WildcardPath object
	 * 
	 * @param filepath
	 *            The file path string to compare
	 * @return java.lang.Boolean
	 */
	public Boolean match(String filepath) {
		WildcardPath path = new WildcardPath(wildcardpath);
		path.replaceToRegex();
		Pattern p = Pattern.compile(path.getPath());
		Matcher m = p.matcher(filepath);
		return m.find();
	}

	public static void main(String[] args) {
		WildcardPath wp = new WildcardPath(
				"/{root}/{filename}.{lang}_{country}.{fileextension}");
		wp.replace(ROOT_WILDCARD, "locale").replace(FILENAME_WILDCARD, "alert")
				.replace(FILE_EXTENSION_WILDCARD, "properties");
		wp.replace(Locale.getDefault());

		WildcardPath wp1 = new WildcardPath(
				"/{root}/{filename}.{lang}_{country}.{fileextension}");
		wp1.replaceToRegex();
		System.out.println(wp1.getPath());
		System.out.println(wp1.match("src/locale/component.es_AR.properties"));
		System.out.println(wp1.match("src/locale/component.en_US.properties"));
		System.out.println(wp1.match("src/locale/component.es.properties"));
	}
}
