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
 * @author Triad (flores.leonardo@gmail.com)
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

	/**
	 * FIXME: Revisar porque al traducir quedan los puntos escapeados
	 * 
	 * @return
	 */
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
		boolean parsed = parse(filepath, Boolean.TRUE);
		if (parsed) {
			return true;
		}
		return parse(filepath, Boolean.FALSE);
	}

	private Boolean parse(String filepath, Boolean withLocale) {
		String escapedFilepath = escapedFilepath(filepath);
		String wildcardRegex = this.replaceToRegex(withLocale).getPath();
		Pattern p = Pattern.compile(wildcardRegex);
		Matcher m = p.matcher(escapedFilepath);
		if (m.find()) {
			String discoveredPath = escapedFilepath.substring(m.start(), m
					.end());
			this.pathToRoot = escapedFilepath.substring(0, m.start());
			if (!withLocale) {
				wildcardpath = wildcardpath.replace(LANGUAGE_WILDCARD, "");
				wildcardpath = wildcardpath.replace(COUNTRY_WILDCARD, "");
				wildcardpath = wildcardpath.replace("._.", ".");
			}
			String[] wildcards = wildcardpath.split("\\/");
			String[] segments = discoveredPath.split("\\/");
			for (int i = 0; i < segments.length; i++) {
				parseWildcard(wildcards[i], segments[i]);
			}
//			if (this.language == null || this.country == null) {
//				throw new RuntimeException("Unparsable lang and country");
//			}
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

		if (wildcard.equals(FILENAME_WILDCARD)) {
			fileName = segment;
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
	 * @return Devuelve el WilcardPath a la cual reemplazo el pais y el lenguaje
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
	public WildcardPath replaceToRegex(Boolean useLocale) {
		resetPath();
		this.replace(ROOT_WILDCARD, TEXT_REGEX);
		this.replace(FILENAME_WILDCARD, TEXT_REGEX);
		this.replace(FILE_EXTENSION_WILDCARD, TEXT_REGEX);
		if (useLocale != null && useLocale.equals(Boolean.TRUE)) {
			this.replace(LANGUAGE_WILDCARD, LANGUAGE_REGEX);
			this.replace(COUNTRY_WILDCARD, COUNTRY_REGEX);
		} else {
			this.replace(LANGUAGE_WILDCARD, "");
			this.replace(COUNTRY_WILDCARD, "");
			this.replace("\\._", "\\.");
			this.replace("\\\\.\\\\.", "\\\\.");
		}
		return this;
	}

	public WildcardPath replace(String wildcard, String value) {
		return replace(wildcard, value, true);
	}

	public WildcardPath replace(String wildcard, String value, boolean replace) {
		if (path.equals("") || path.length() == 0) {
			resetPath();
		}
		path = path.replaceAll(escapedWildcard(wildcard), value);
		return this;
	}

	/**
	 * Devuelve la expresion regular necesaria para obtener el locale a partir
	 * del nombre del archivo
	 * 
	 * @return String con la expresion regular resultante
	 */
	public String getLocaleRegex() {
		String toRegex = this.wildcardpath;
		toRegex = toRegex.replaceAll(escapedWildcard(COUNTRY_WILDCARD),
				COUNTRY_REGEX);
		toRegex = toRegex.replaceAll(escapedWildcard(LANGUAGE_WILDCARD),
				LANGUAGE_REGEX);
		toRegex = toRegex.replaceAll(escapedWildcard(FILENAME_WILDCARD), this
				.getFileName());
		toRegex = toRegex.replaceAll(escapedWildcard(FILE_EXTENSION_WILDCARD),
				this.getFileExtension());
		toRegex = toRegex.replaceAll(escapedWildcard(ROOT_WILDCARD), this
				.getRoot());
		return toRegex;
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
		path.replaceToRegex(Boolean.TRUE);
		Pattern p = Pattern.compile(path.getPath());
		Matcher m = p.matcher(filepath);
		boolean matched = m.find();
		if (matched) {
			return true;
		}
		path.replaceToRegex(Boolean.FALSE);
		p = Pattern.compile(path.getPath());
		m = p.matcher(filepath);
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
		wp.replaceToRegex(Boolean.TRUE);
		Pattern p = Pattern.compile(wp.getPath());
		Matcher m = p.matcher(filepath);
		if (m.find()) {
			return filepath.substring(m.start(), m.end());
		}
		wp.replaceToRegex(Boolean.TRUE);
		p = Pattern.compile(wp.getPath());
		m = p.matcher(filepath);
		if (m.find()) {
			return filepath.substring(m.start(), m.end());
		}
		return null;
	}
}
