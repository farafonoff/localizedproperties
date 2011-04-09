package com.triadsoft.properties.model.utils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
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
 */
public class WildCardPath2 implements IWildcardPath {

	private String root;

	private String fileName;
	private String fileExtension;
	private String country;
	private String language;
	private String pathToRoot;

	private String wildcardpath;
	private String path = "";

	public WildCardPath2(String wildcardpath) {
		this.wildcardpath = wildcardpath;
		this.path = "";
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

	public void setRoot(String root) {
		this.root = root;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	public String getWildcardpath() {
		return this.wildcardpath;
	}

	public Boolean parse(String filepath) {
		return parse(filepath, true, true);
	}

	/**
	 * @deprecated Se debe reemplazar por parse(String filepath,int offset)
	 */
	public Boolean parse(String filepath, boolean withLanguage,
			boolean withCountry) {
		String transformedPath = filepath.replace(File.separator, "/");
		String[] loadedWilcards = this.getLoadedWildcards(withLanguage,
				withCountry);

		Pattern patt = Pattern.compile(getWildcardPathAsRegex(withLanguage,
				withCountry));
		Matcher m = patt.matcher(transformedPath);
		if (m.find()) {
			for (int i = 0; i < loadedWilcards.length; i++) {
				String wc = loadedWilcards[i];
				if (wc.equals(ROOT_WILDCARD)) {
					root = m.group(i + 1);
				} else if (wc.equals(LANGUAGE_WILDCARD)) {
					language = m.group(i + 1);
				} else if (wc.equals(COUNTRY_WILDCARD)) {
					country = m.group(i + 1);
				} else if (wc.equals(FILENAME_WILDCARD)) {
					fileName = m.group(i + 1);
				} else if (wc.equals(FILE_EXTENSION_WILDCARD)) {
					fileExtension = m.group(i + 1);
				}
			}
			return true;
		}
		// patt = Pattern.compile(getWildcardPathAsRegex(true, false));
		// m = patt.matcher(transformedPath);
		// if (m.find()) {
		// if (haveRoot) {
		// root = m.group(1);
		// }
		// fileName = m.group(haveRoot ? 2 : 1);
		// language = m.group(haveRoot ? 3 : 2);
		// fileExtension = m.group(haveRoot ? 4 : 3);
		// return true;
		// }
		// patt = Pattern.compile(getWildcardPathAsRegex(false, false));
		// m = patt.matcher(transformedPath);
		// if (m.find()) {
		// if (haveRoot) {
		// root = m.group(1);
		// }
		// fileName = m.group(haveRoot ? 2 : 1);
		// fileExtension = m.group(haveRoot ? 3 : 2);
		// return true;
		// }
		return false;
	}

	public void resetPath() {
		this.path = null;
	}

	private String[] getLoadedWildcards(boolean withLanguage,
			boolean withCountry) {
		String wps = this.wildcardpath;
		List<String> wpsList = new LinkedList<String>();
		Pattern p = Pattern.compile("\\{\\w+\\}");
		Matcher m = p.matcher(wps);
		while (m.find()) {
			String wc = wps.substring(m.start(), m.end());
			if (!withLanguage && wc.equals(LANGUAGE_WILDCARD)) {
				continue;
			} else if (!withCountry && wc.equals(COUNTRY_WILDCARD)) {
				continue;
			} else {
				wpsList.add(wc);
			}
		}
		return wpsList.toArray(new String[wpsList.size()]);
	}

	/**
	 * Obtiene los wildacards usados en la expresion regular usando el offset
	 * para eliminar los opcionales
	 * 
	 * @param offset
	 * @return
	 */
	private String[] getLoadedWildcards(int offset) {
		String wps = this.getExcludedOptional(offset);
		List<String> wpsList = new LinkedList<String>();
		Pattern p = Pattern.compile("\\{\\w+\\}");
		Matcher m = p.matcher(wps);
		while (m.find()) {
			String wc = wps.substring(m.start(), m.end());
			wpsList.add(wc);
		}
		return wpsList.toArray(new String[wpsList.size()]);
	}

	private String getWildcardPathAsRegex(boolean withLanguage,
			boolean withCountry) {
		String asRegex = this.wildcardpath;
		if (withLanguage) {
			asRegex = asRegex.replace(LANGUAGE_WILDCARD, "(" + LANGUAGE_REGEX
					+ ")");
		} else {
			asRegex = asRegex.replace("." + LANGUAGE_WILDCARD, "");
			asRegex = asRegex.replace("_" + LANGUAGE_WILDCARD, "");
			asRegex = asRegex.replace(LANGUAGE_WILDCARD, "");
		}
		if (withCountry) {
			asRegex = asRegex.replace(COUNTRY_WILDCARD, "(" + COUNTRY_REGEX
					+ ")");
		} else {
			asRegex = asRegex.replace("." + COUNTRY_WILDCARD, "");
			asRegex = asRegex.replace("_" + COUNTRY_WILDCARD, "");
			asRegex = asRegex.replace(COUNTRY_WILDCARD, "");
		}
		asRegex = asRegex.replace(".", "\\.");
		asRegex = asRegex.replace(ROOT_WILDCARD, "(" + TEXT_REGEX + ")");
		asRegex = asRegex.replace(ROOT_WILDCARD, "(" + TEXT_REGEX + ")");
		asRegex = asRegex.replace(FILENAME_WILDCARD, "(" + TEXT_REGEX + ")");
		asRegex = asRegex.replace(FILE_EXTENSION_WILDCARD, "(" + TEXT_REGEX
				+ ")");
		return asRegex;
	}

	private String getExcludedOptional(int offset) {
		String asRegex = this.wildcardpath;
		Pattern p = Pattern.compile(OPTIONAL_PARAMETERS);
		Matcher m = p.matcher(asRegex);
		while (m.find()) {
			String currentOpt = asRegex.substring(m.start(), m.end());
			try {
				String number = currentOpt.substring(currentOpt.length() - 1,
						currentOpt.length());
				int order = Integer.parseInt(number);
				if (offset >= order) {
					asRegex = asRegex.replace(m.group(), "");
					// actualizo el string
					m = p.matcher(asRegex);
				}
			} catch (NumberFormatException ex) {
				// Nada por hacer por ahora.
			}
		}
		// Ahora que hice la limpieza según el nivel
		// Tengo que sacar los opcionales que quedaron.
		m = p.matcher(asRegex);
		while (m.find()) {
			String wrappedString = m.group();
			String cleanString = wrappedString.substring(1,
					wrappedString.length() - 2);
			asRegex = asRegex.replace(wrappedString, cleanString);
		}
		asRegex = asRegex.replaceAll("\\.", ".");
		asRegex = asRegex.replaceAll("\\_", "_");
		return asRegex;
	}

	private String getWildcardPathAsRegex(int offset) {
		String asRegex = this.getExcludedOptional(offset);
		asRegex = asRegex
				.replace(LANGUAGE_WILDCARD, "(" + LANGUAGE_REGEX + ")");
		asRegex = asRegex.replace(COUNTRY_WILDCARD, "(" + COUNTRY_REGEX + ")");
		asRegex = asRegex.replace(".", "\\.");
		asRegex = asRegex.replace(ROOT_WILDCARD, "(" + TEXT_REGEX + ")");
		asRegex = asRegex.replace(ROOT_WILDCARD, "(" + TEXT_REGEX + ")");
		asRegex = asRegex.replace(FILENAME_WILDCARD, "(" + TEXT_REGEX + ")");
		asRegex = asRegex.replace(FILE_EXTENSION_WILDCARD, "(" + TEXT_REGEX
				+ ")");
		return asRegex;
	}

	/**
	 * @see com.triadsoft.properties.model.utils.IWildcardPath#match(java.lang.String,
	 *      boolean, boolean)
	 */
	public Boolean match(String filepath) {
		return match(filepath, true, true);
	}

	/**
	 * @see com.triadsoft.properties.model.utils.IWildcardPath#match(java.lang.String,
	 *      boolean, boolean)
	 * @deprecated
	 */
	public Boolean match(String filepath, boolean withLanguage,
			boolean withCountry) {
		String transformedPath = filepath.replace(File.separator, "/");
		Pattern patt = Pattern.compile(this.getWildcardPathAsRegex(
				withLanguage, withCountry));
		Matcher m = patt.matcher(transformedPath);
		if (m.find()) {
			return true;
		}
		patt = Pattern.compile(this.getWildcardPathAsRegex(true, false));
		m = patt.matcher(transformedPath);
		// Si coincide es porque tiene el lenguaje pero no el pais
		if (m.find()) {
			return true;
		}
		patt = Pattern.compile(this.getWildcardPathAsRegex(false, false));
		m = patt.matcher(transformedPath);
		// Si coincide es porque tiene el lenguaje pero no el pais
		if (m.find()) {
			return true;
		}
		return false;
	}

	public IWildcardPath replace(String wildcard, String value, boolean replace) {
		if (path.equals("") || path.length() == 0) {
			resetPath();
		}
		path = path.replaceAll(escapedWildcard(wildcard), value);
		return this;
	}

	public String toRegex() {
		return toRegex(0);
	}

	public String toRegex(int offset) {
		String regex = this.getExcludedOptional(offset);
		// Primero reemplazo los valores que se deben escapear
		regex = regex.replace(".", "\\.");
		regex = regex.replace("-", "\\-");
		regex = regex.replace("_", "\\_");
		// Si root es null, devuelve a root reemplazado por la expresion regular
		regex = root != null ? regex.replace(ROOT_WILDCARD, root) : regex
				.replace(ROOT_WILDCARD, TEXT_REGEX);
		// Si el nombre del archivo es null, entonces devuelve la expresion
		// regular que lo reemplaza
		regex = fileName != null ? regex.replace(FILENAME_WILDCARD, fileName)
				: regex.replace(FILENAME_WILDCARD, TEXT_REGEX);
		// Si el la extension del archivo es null, entonces devuelve la
		// expresion regular que lo reemplaza
		regex = fileExtension != null ? regex.replace(FILE_EXTENSION_WILDCARD,
				fileExtension) : regex.replace(FILE_EXTENSION_WILDCARD,
				TEXT_REGEX);
		// Si el country es null devuelve la expresion regular que lo repesenta
		regex = country != null ? regex.replace(COUNTRY_WILDCARD, country)
				: regex.replace(COUNTRY_WILDCARD, COUNTRY_REGEX);
		// Si el lenguage es null devuelve la expresion regular que lo repesenta
		regex = language != null ? regex.replace(LANGUAGE_WILDCARD, language)
				: regex.replace(LANGUAGE_WILDCARD, LANGUAGE_REGEX);
		return regex;
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

	public IWildcardPath replace(String wildcard, String value) {
		// TODO To implement
		return null;
	}

	public IWildcardPath replace(Locale locale) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPathToRoot() {
		return pathToRoot;
	}

	public String toString() {
		return wildcardpath;
	}

	public String getFilePath(IFile ifile, Locale locale) {
		return null;
	}

	public Boolean parse(String filepath, int offset) {
		return match(filepath, offset);
	}

	public Boolean match(String filepath, int offset) {
		String transformedPath = filepath.replace(File.separator, "/");
		Pattern patt = Pattern.compile(this.getWildcardPathAsRegex(offset));
		Matcher m = patt.matcher(transformedPath);
		String[] loadedWilcards = this.getLoadedWildcards(offset);
		if (m.find()) {
			for (int i = 0; i < loadedWilcards.length; i++) {
				String wc = loadedWilcards[i];
				if (wc.equals(ROOT_WILDCARD)) {
					root = m.group(i + 1);
				} else if (wc.equals(LANGUAGE_WILDCARD)) {
					language = m.group(i + 1);
				} else if (wc.equals(COUNTRY_WILDCARD)) {
					country = m.group(i + 1);
				} else if (wc.equals(FILENAME_WILDCARD)) {
					fileName = m.group(i + 1);
				} else if (wc.equals(FILE_EXTENSION_WILDCARD)) {
					fileExtension = m.group(i + 1);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		WildCardPath2 wp2 = new WildCardPath2(this.wildcardpath);
		return wp2;
	}
}
