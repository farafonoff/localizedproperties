package com.triadsoft.properties.model.utils;

import java.io.File;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;

/**
 * Esta clase es la encargada de encapsular todos la logica para poder parsear y
 * descubrir los datos referidos al path La clase no maneja archivos sino que
 * solamente manipula strings y obtiene los datos que serï¿½n utilizados por el
 * controlador de archivos
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * @deprecated Se debe usar WilcardPath2
 */
public class WildcardPath implements IWildcardPath {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.triadsoft.properties.model.utils.IWildcardPath#getWildcardpath()
	 */
	public String getWildcardpath() {
		if (wildcardpath.indexOf("$") > -1) {
			return wildcardpath;
		}
		return wildcardpath + "$";
	}

	/**
	 * Devuelve verdadero si en el wildcard path tiene establecido la carpeta
	 * root
	 * 
	 * @return
	 */
	public boolean haveRoot() {
		// return wildcardpath.matches(ROOT);
		return true;
	}

	/**
	 * TODO: Revisar porque al traducir quedan los puntos escapeados No se debe
	 * hacer acá porque se usa internamente. Ver de mejorar que que el que lo
	 * usa de afuera no tenga que escribir código extra
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	public String getRoot() {
		return root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.triadsoft.properties.model.utils.IWildcardPath#getFileName()
	 */
	public String getFileName() {
		return fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.triadsoft.properties.model.utils.IWildcardPath#getFileExtension()
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.triadsoft.properties.model.utils.IWildcardPath#getCountry()
	 */
	public String getCountry() {
		return country;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.triadsoft.properties.model.utils.IWildcardPath#getLanguage()
	 */
	public String getLanguage() {
		return language;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.triadsoft.properties.model.utils.IWildcardPath#getLocale()
	 */
	public Locale getLocale() {
		Locale locale = null;
		if (this.language != null && this.country == null) {
			locale = new Locale(this.language);
		} else if (this.language != null && this.country != null) {
			locale = new Locale(this.language, this.country);
		}
		return locale;
	}

	protected void setParhToRoot(String pathToRoot) {
		this.pathToRoot = pathToRoot;
	}

	public String getPathToRoot() {
		return pathToRoot;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.triadsoft.properties.model.utils.IWildcardPath#parse(java.lang.String
	 * )
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
	public Boolean parse(String filepath, boolean withLanguage,
			boolean withCoutry) {
		String escapedFilepath = escapedFilepath(filepath);
		String wildcardRegex = null;
		wildcardRegex = this.replaceToRegex(withLanguage, withCoutry).getPath();
		Pattern p = Pattern.compile(wildcardRegex);
		Matcher m = p.matcher(escapedFilepath);
		if (m.find()) {
			String discoveredPath = escapedFilepath.substring(m.start(),
					m.end());
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
		if (wildcard.equals(FILE_EXTENSION_WILDCARD + "$")) {
			wildcard = FILE_EXTENSION_WILDCARD;
		}
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
		segments = segment.split("-");
		if (segments.length > 1) {
			String[] wildcards = wildcard.split("-");
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
	public IWildcardPath replace(Locale locale) {
		this.replace(LANGUAGE_WILDCARD, locale.getLanguage());
		this.replace(COUNTRY_WILDCARD, locale.getCountry());
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.triadsoft.properties.model.utils.IWildcardPath#resetPath()
	 */
	public void resetPath() {
		this.path = wildcardpath;
		this.path = wildcardpath.replaceAll("\\.", "\\\\.");
	}

	protected void resetLocale() {
		this.language = null;
		this.country = null;
	}

	/**
	 * Este mï¿½todo toma el wildcard path y lo transforma en una expresion
	 * regular
	 * 
	 * @return {@link WildcardPath}
	 */
	protected IWildcardPath replaceToRegex() {
		return replaceToRegex(true, true);
	}

	/**
	 * Este mï¿½todo toma el wildcard path y lo transforma en una expresion
	 * regular
	 * 
	 * @param Indica
	 *            si se usarï¿½ el lenguaje
	 * @param Indica
	 *            si se usarï¿½ el pais
	 */
	protected WildcardPath replaceToRegex(boolean useLanguage,
			boolean useCountry) {
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
		// Acá tengo que reemplazar el fin por el fin de linea
		return wp;
	}

	public IWildcardPath replace(String wildcard, String value) {
		return replace(wildcard, value, true);
	}

	public IWildcardPath replace(String wildcard, String value, boolean replace) {
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
		toRegex = toRegex.replaceAll(escapedWildcard(FILENAME_WILDCARD),
				this.getFileName());
		toRegex = toRegex.replaceAll(escapedWildcard(FILE_EXTENSION_WILDCARD),
				this.getFileExtension());
		toRegex = toRegex.replaceAll(escapedWildcard(ROOT_WILDCARD),
				this.getRoot());
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
	 * todos los path de la misma manera. Teï¿½ricamente no debe haber
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.triadsoft.properties.model.utils.IWildcardPath#match(java.lang.String
	 * )
	 */
	public Boolean match(String filepath) {
		return match(filepath, true, true);
	}

	/**
	 * @see com.triadsoft.properties.model.utils.IWildcardPath#match(java.lang.String,
	 *      boolean, boolean)
	 */
	public Boolean match(String filepath, boolean withLanguage,
			boolean withCountry) {
		WildcardPath path = new WildcardPath(wildcardpath);
		String escapedFilepath = escapedFilepath(filepath);
		WildcardPath newpath = path.replaceToRegex(true, true);
		Pattern p = Pattern.compile(newpath.getPath());
		Matcher m = p.matcher(escapedFilepath);
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

	public String getFilePath(IFile ifile, Locale locale) {
		WildcardPath wp = new WildcardPath(this.wildcardpath);
		wp.parse(ifile.getFullPath().toString());
		return wp.getFilePath(wp, locale);
	}

	public String getFilePath(WildcardPath wp, Locale locale) {
		wp.replace(locale);
		wp.replace(ROOT_WILDCARD, wp.getRoot());
		wp.replace(FILENAME_WILDCARD, wp.getFileName());
		wp.replace(FILE_EXTENSION_WILDCARD, wp.getFileExtension());
		wp.replace("\\\\.", "\\.");
		wp.replace("\\_", "_");
		return wp.getPathToRoot() + wp.getPath();
	}

	public Boolean parse(String filepath, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	public Boolean match(String filepath, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	public String toRegex(int offset) {
		return null;
	}

	public String toRegex() {
		return null;
	}

	public void setLanguage(String language) {
	}

	public void setCountry(String country) {
	}

	public void setFileExtension(String fileExtension) {
	}

	public void setFileName(String fileName) {
	}

	public void setRoot(String root) {
	}

	public String getPath(int offset) {
		// TODO Auto-generated method stub
		return null;
	}
}
