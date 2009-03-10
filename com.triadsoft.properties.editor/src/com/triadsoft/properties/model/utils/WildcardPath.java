package com.triadsoft.properties.model.utils;

import java.io.File;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Esta clase es la encargada de encapsular todos la logica para poder parsear y
 * descubrir los datos referidos al path La clase no maneja archivos sino que
 * solamente manipula strings y obtiene los datos que serán utilizados por el
 * controlador de archivos
 * 
 * @author triad
 */
public class WildcardPath {
	public static final String COUNTRY_REGEX = "[A-Z]{2}";
	public static final String LANGUAGE_REGEX = "[a-z]{2}";
	public static final String TEXT_REGEX = "[a-zA-Z\\-\\_]+";
	public static final String ROOT_WILDCARD = "{root}";
	public static final String FILENAME_WILDCARD = "{filename}";
	public static final String FILE_EXTENSION_WILDCARD = "{fileextension}";
	public static final String COUNTRY_WILDCARD = "{country}";
	public static final String LANGUAGE_WILDCARD = "{lang}";

	private String root;
	private String fileName;
	private String fileExtension;
	private String country;
	private String language;
	private String pathToRoot;

	private String wildcardpath;
	private String path = "";

	public WildcardPath(String wildcardpath) {
		this.wildcardpath = wildcardpath;
		this.path = "";
	}

	public String getWildcardpath() {
		return wildcardpath;
	}

	public String getPath() {
		return path;
	}

	public String getRoot() {
		return root;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public String getCountry() {
		return country;
	}

	public String getLanguage() {
		return language;
	}

	public Locale getLocale() {
		Locale locale = null;
		if (this.language != null && this.country == null) {
			locale = new Locale(this.language);
		} else if (this.language != null && this.country != null) {
			locale = new Locale(this.language, this.country);
		}
		return locale;
	}

	public String getPathToRoot() {
		return pathToRoot;
	}

	/**
	 * Este metodo se encarga de obtener del path pasado como parámetro los
	 * datos correspondientes a cada wildcard
	 */
	public Boolean parse(String filepath) {
		String escapedFilepath = escapedFilepath(filepath);
		String wildcardRegex = this.replaceToRegex().getPath();
		Pattern p = Pattern.compile(wildcardRegex);
		Matcher m = p.matcher(escapedFilepath);
		if (m.find()) {
			String discoveredPath = escapedFilepath.substring(m.start(), m
					.end());
			this.pathToRoot = escapedFilepath.substring(0,m.start());
			String[] wildcards = wildcardpath.split("\\/");
			String[] segments = discoveredPath.split("\\/");
			for (int i = 0; i < segments.length; i++) {
				parseWildcard(wildcards[i], segments[i]);
			}
			if (this.language == null || this.country == null) {
				throw new RuntimeException("Unparsable lang and country");
			}
			return true;
		}
		return false;
	}

	private Boolean parseWildcard(String wildcard, String segment) {
		String[] segments = segment.split("\\.");
		if (segments.length > 1) {
			String[] wildcards = wildcard.split("\\.");
			for (int i = 0; i < wildcards.length; i++) {
				parseWildcard(wildcards[i], segments[i]);
			}
			return true;
		}

		segments = segment.split("\\_");
		if (segments.length > 1) {
			String[] wildcards = wildcard.split("\\_");
			for (int i = 0; i < wildcards.length; i++) {
				parseWildcard(wildcards[i], segments[i]);
			}
			return true;
		}

		// Tengo que ver si no viene algo complejo
		if (wildcard.equals(ROOT_WILDCARD)) {
			root = segment;
		} else if (wildcard.equals(FILE_EXTENSION_WILDCARD)) {
			fileExtension = segment;
		} else if (wildcard.equals(FILENAME_WILDCARD)) {
			fileName = segment;
		} else if (wildcard.equals(COUNTRY_WILDCARD)) {
			country = segment;
		} else if (wildcard.equals(LANGUAGE_WILDCARD)) {
			language = segment;
		} else {
			return false;
		}
		return true;
	}

	/**
	 * This method replace the language and country into the path string
	 * 
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
	public void resetPath() {
		this.path = wildcardpath;
		this.path = wildcardpath.replaceAll("\\.", "\\\\.");
	}

	/**
	 * This method return a wildcard path as regular expresion
	 */
	public WildcardPath replaceToRegex() {
		resetPath();
		this.replace(ROOT_WILDCARD, TEXT_REGEX);
		this.replace(FILENAME_WILDCARD, TEXT_REGEX);
		this.replace(FILE_EXTENSION_WILDCARD, TEXT_REGEX);
		this.replace(LANGUAGE_WILDCARD, LANGUAGE_REGEX);
		this.replace(COUNTRY_WILDCARD, COUNTRY_REGEX);
		return this;
	}

	public WildcardPath replace(String wildcard, String value) {
		if (path.equals("") || path.length() == 0) {
			path = wildcardpath.replaceAll("\\.", "\\\\.");
		}
		path = path.replaceAll(escapedWildcard(wildcard), value);
		return this;
	}
	
	public WildcardPath replaceDiscoveryLocale(){
		this.replace(COUNTRY_WILDCARD, COUNTRY_REGEX);
		this.replace(LANGUAGE_WILDCARD, LANGUAGE_REGEX);
		this.replace(FILENAME_WILDCARD,this.getFileName());
		this.replace(FILE_EXTENSION_WILDCARD,this.getFileExtension());
		this.replace(ROOT_WILDCARD,this.getRoot());
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
	 * Si el path es de Windows, lo convierte al de unix / Es para poder evaluar
	 * todos los path de la misma manera. Teóricamente no debe haber diferencias
	 * 
	 * @param filepath
	 * @return
	 */
	private String escapedFilepath(String filepath) {
		if (File.separator.equals("\\")) {
			return filepath.replaceAll("\\\\", "\\/");
		}
		return filepath;
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

	/**
	 * Extrae del filepath la parte que coincide con el wilcardpath declarado
	 * 
	 * @param filepath
	 *            Path Completo al archivo de recursos
	 * @return String con el la subseccion del filepath
	 */
	public String extractPath(String filepath) {
		WildcardPath wp = new WildcardPath(wildcardpath);
		wp.replaceToRegex();
		Pattern p = Pattern.compile(wp.getPath());
		Matcher m = p.matcher(filepath);
		if (m.find()) {
			return filepath.substring(m.start(), m.end());
		}
		return null;
	}

	public static void main(String[] args) {
		WildcardPath wp = new WildcardPath(
				"/{root}/{filename}.{lang}_{country}.{fileextension}");
		wp.replace(ROOT_WILDCARD, "locale").replace(FILENAME_WILDCARD, "alert")
				.replace(FILE_EXTENSION_WILDCARD, "properties");
		wp.replace(Locale.getDefault());

		WildcardPath wp1 = new WildcardPath(
				"/{root}/prueba/{filename}.{lang}_{country}.{fileextension}");
		wp1.replaceToRegex();
		System.out.println(wp1.getPath());
		System.out.println(wp1.match("src/locale/prueba/component.es_AR.properties"));
		System.out.println(wp1
				.match("src/locale/prueba/component.en_US.properties"));
		System.out.println(wp1
				.match("src/locale/prueba/component.es.properties"));
		wp1
				.parse("c:\\tempos\\TemposProject\\src\\locale\\prueba\\component.en_US.properties");
		System.out.println("Filename:" + wp1.getFileName());
		System.out.println("Extension:" + wp1.getFileExtension());
		System.out.println("Root: " + wp1.getRoot());
		System.out.println("Locale: " + wp1.getLocale());
		System.out.println("Path: " + wp1.getPathToRoot());
	}
}
