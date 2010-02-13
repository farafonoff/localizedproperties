package com.triadsoft.properties.model.utils;

import java.io.File;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Esta clase es la encargada de encapsular todos la logica para poder parsear y
 * descubrir los datos referidos al path La clase no maneja archivos sino que
 * solamente manipula strings y obtiene los datos que ser�n utilizados por el
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
	 * Devuelve verdadero si en el wildcard path tiene establecido la carpeta
	 * root
	 * 
	 * @return
	 */
	public boolean haveRoot() {
		//return wildcardpath.matches(ROOT);
		return true;
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
	 * Este metodo se encarga de obtener del path pasado como par�metro los
	 * datos correspondientes a cada wildcard
	 */
	public Boolean parse(String filepath) {
		if (parse(filepath, true, true)) {
			return true;
		}
		if (parse(filepath, true, false)) {
			return true;
		}
		return parse(filepath, false, false);
	}

	/**
	 * 
	 * @param filepath
	 * @param withLocale
	 *            Indica si va a evaluar
	 * @return
	 */
	private Boolean parse(String filepath, boolean withLanguage,
			boolean withCoutry) {
		String escapedFilepath = escapedFilepath(filepath);
		String wildcardRegex = null;
		wildcardRegex = this.replaceToRegex(withLanguage, withCoutry).getPath();
		Pattern p = Pattern.compile(wildcardRegex);
		Matcher m = p.matcher(escapedFilepath);
		if (m.find()) {
			String discoveredPath = escapedFilepath.substring(m.start(), m
					.end());
			this.pathToRoot = escapedFilepath.substring(0, m.start());
			String wilcardpathCopy = wildcardpath;
			if (!withLanguage && !withCoutry) {
				wilcardpathCopy = wilcardpathCopy
						.replace(LANGUAGE_WILDCARD, "");
				wilcardpathCopy = wilcardpathCopy.replace(COUNTRY_WILDCARD, "");
				wilcardpathCopy = wilcardpathCopy.replace("._.", ".");
				this.resetLocale();
			} else if (withLanguage && !withCoutry) {
				wilcardpathCopy = wilcardpathCopy.replace(COUNTRY_WILDCARD, "");
				wilcardpathCopy = wilcardpathCopy.replace("._.", ".");
				wilcardpathCopy = wilcardpathCopy.replace("_.", ".");
				this.resetLocale();
			}
			String[] wildcards = wilcardpathCopy.split("\\/");
			String[] segments = discoveredPath.split("\\/");
			if (wildcards.length != segments.length) {
				throw new RuntimeException(
						"No pude separar los archivos en las mismas cantidades");
			}
			for (int i = 0; i < segments.length; i++) {
				parseWildcard(wildcards[i], segments[i]);
			}
			// if (this.language == null || this.country == null) {
			// throw new RuntimeException("Unparsable lang and country");
			// }
			return true;
		}
		return false;
	}

	private Boolean parseWildcard(String wildcard, String segment) {
		String[] segments = segment.split("\\.");
		if (segments.length > 1) {
			String[] wildcards = wildcard.split("\\.");
			if (segments.length != wildcards.length) {
				return false;
			}
			for (int i = 0; i < wildcards.length; i++) {
				parseWildcard(wildcards[i], segments[i]);
			}
			return true;
		}

		if (wildcard.equals(FILENAME_WILDCARD)) {
			fileName = segment;
			return true;
		}

		if (wildcard.equals(LANGUAGE_WILDCARD)) {
			language = segment;
			return true;
		}

		segments = segment.split("\\_");
		if (segments.length > 1) {
			String[] wildcards = wildcard.split("\\_");
			if (segments.length != wildcards.length) {
				return false;
			}
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

	public void resetLocale() {
		this.language = null;
		this.country = null;
	}

	/**
	 * Este m�todo toma el wildcard path y lo transforma en una expresion
	 * regular
	 * 
	 * @return {@link WildcardPath}
	 */
	public WildcardPath replaceToRegex() {
		return replaceToRegex(true, true);
	}

	/**
	 * Este m�todo toma el wildcard path y lo transforma en una expresion
	 * regular
	 * 
	 * @param Indica
	 *            si se usar� el lenguaje
	 * @param Indica
	 *            si se usar� el pais
	 */
	public WildcardPath replaceToRegex(boolean useLanguage, boolean useCountry) {
		resetPath();
		WildcardPath wp = new WildcardPath(getWildcardpath());
		wp.replace(ROOT_WILDCARD, TEXT_REGEX);
		wp.replace(FILENAME_WILDCARD, TEXT_REGEX);
		wp.replace(FILE_EXTENSION_WILDCARD, TEXT_REGEX);
		if (useCountry) {
			wp.replace(COUNTRY_WILDCARD, COUNTRY_REGEX);
		} else {
			wp.replace(COUNTRY_WILDCARD, "");
		}

		if (useLanguage) {
			wp.replace(LANGUAGE_WILDCARD, LANGUAGE_REGEX);
		} else {
			wp.replace(LANGUAGE_WILDCARD, "");
		}
		wp.replace("\\._", "\\.").replace("\\\\.\\\\.", "\\\\.");
		// Activator.debug(wp.getPath(), null);
		wp.replace("\\_\\\\.", "\\\\.");
		// Activator.debug(wp.getPath(), null);
		return wp;
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
	 * todos los path de la misma manera. Te�ricamente no debe haber
	 * diferencias
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
		WildcardPath newpath = path.replaceToRegex(true, true);
		Pattern p = Pattern.compile(newpath.getPath());
		Matcher m = p.matcher(filepath);
		if (m.find()) {
			return true;
		}
		newpath = path.replaceToRegex(true, false);
		p = Pattern.compile(newpath.getPath());
		m = p.matcher(filepath);
		if (m.find()) {
			return true;
		}
		newpath = path.replaceToRegex(false, false);
		p = Pattern.compile(newpath.getPath());
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
		wp.replaceToRegex(true, true);
		Pattern p = Pattern.compile(wp.getPath());
		Matcher m = p.matcher(filepath);
		if (m.find()) {
			return filepath.substring(m.start(), m.end());
		}
		wp.replaceToRegex(true, true);
		p = Pattern.compile(wp.getPath());
		m = p.matcher(filepath);
		if (m.find()) {
			return filepath.substring(m.start(), m.end());
		}
		return null;
	}
}
