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
 * TODO:Translate
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class WildCardPath2 implements IWildcardPath {

	private String root;
	private String fileName;
	private String fileExtension;
	private String country;
	private String language;
	private String variant;
	private String pathToRoot;

	private String wildcardpath;

	public WildCardPath2(String wildcardpath) {
		this.wildcardpath = wildcardpath;
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
		// String virtualLanguage = language == null ?
		// StringUtils.getKeyLocale()
		// .getLanguage() : this.language;
		// String virtualCountry = country == null ? StringUtils.getKeyLocale()
		// .getCountry() : this.country;
		//
		Locale locale = null;
		// if (variant == null) {
		// locale = new Locale(virtualLanguage, virtualCountry);
		// } else {
		// locale = new Locale(virtualLanguage, virtualCountry, variant);
		// }
		if (this.language != null && this.country != null
				&& this.variant != null) {
			locale = new Locale(this.language, this.country, this.variant);
		} else if (this.language != null && this.country != null) {
			locale = new Locale(this.language, this.country);	
		} else if (this.language != null && this.country == null) {
			locale = new Locale(this.language);
		} else if (this.language == null && this.country == null) {
			locale = StringUtils.getKeyLocale();
		} else if (this.language == null && this.country != null) {
			locale = new Locale(StringUtils.getKeyLocale().getLanguage(),
					this.country);
		}
		return locale;
	}

	public String getWildcardpath() {
		return this.wildcardpath;
	}

	/**
	 * Set a text to distinguishe
	 * 
	 * @param variant
	 * @see http://download.oracle.com/javase/6/docs/api/java/util/Locale.html
	 */
	public void setVariant(String variant) {
		this.variant = variant;
	}

	public String getVariant() {
		return variant;
	}

	public Boolean parse(String filepath) {
		int index = 0;
		while (!match(filepath, index)
				&& index < IWildcardPath.MAXIMUM_OPTIONALS) {
			index++;
		}
		if (index == IWildcardPath.MAXIMUM_OPTIONALS) {
			return false;
		}
		return parse(filepath, index);
	}

	/**
	 * @deprecated
	 */
	public void resetPath() {
		// this.path = null;
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
			String cleanString = wrappedString.substring(1, wrappedString
					.length() - 2);
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
		asRegex = asRegex.replace(VARIANT_WILDCARD, "(" + VARIANT_REGEX + ")");
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
		int index = 0;
		while (!match(filepath, index)
				&& index < IWildcardPath.MAXIMUM_OPTIONALS) {
			index++;
		}
		return index != IWildcardPath.MAXIMUM_OPTIONALS;
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
		// If root is null,it returns the replacement of root by regular
		// expression
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
		// if variant is null, it returns the regular expresion
		regex = variant != null ? regex.replace(VARIANT_WILDCARD, variant)
				: regex.replace(VARIANT_WILDCARD, VARIANT_REGEX);
		return regex;
	}

	public String getPathToRoot() {
		return pathToRoot;
	}

	public String toString() {
		return wildcardpath;
	}

	public String getFilePath(IFile ifile, Locale locale) {
		try {
			WildCardPath2 wp2 = (WildCardPath2) clone();
			// FIXME: coregir filepath
			int index = 0;
			String fullpath = ifile.getFullPath().toFile().getAbsolutePath();
			while (!wp2.match(fullpath, index)
					&& index < IWildcardPath.MAXIMUM_OPTIONALS) {
				index++;
			}
			wp2.parse(fullpath, index);
			wp2.setFileName(fileName);
			wp2.setFileExtension(fileExtension);
			wp2.setRoot(root);
			wp2.setLanguage(locale.getLanguage() != null
					&& locale.getLanguage().length() > 0 ? locale.getLanguage()
					: null);
			wp2.setCountry(locale.getCountry() != null
					&& locale.getCountry().length() > 0 ? locale.getCountry()
					: null);

			wp2.setVariant(locale.getVariant() != null
					&& locale.getVariant().length() > 0 ? locale.getVariant()
					: null);

			// Ahora tengo que buscar el path, que no me devuelva ninguna
			// expresion regular
			index = 0;
			boolean hasRegex = true;
			while (hasRegex && index < IWildcardPath.MAXIMUM_OPTIONALS) {
				String newPath = wp2.getPath(index);
				if (!newPath.contains(IWildcardPath.COUNTRY_REGEX)
						&& !newPath.contains(IWildcardPath.LANGUAGE_REGEX)
						&& !newPath.contains(IWildcardPath.TEXT_REGEX)) {
					hasRegex = false;
				} else {
					index++;
				}
			}
			return wp2.getPathToRoot() + "/" + wp2.getPath(index);
		} catch (CloneNotSupportedException e) {
			LocalizedPropertiesLog.error("Error clonando en getFilePath", e);
		}
		return null;
	}

	public Boolean parse(String filepath, int offset) {
		fileName = null;
		fileExtension = null;
		language = null;
		country = null;
		root = null;
		variant = null;
		return match(filepath, offset);
	}

	public Boolean match(String filepath, int offset) {
		String transformedPath = filepath.replace(File.separator, "/");
		Pattern patt = Pattern.compile(this.getWildcardPathAsRegex(offset));
		Matcher m = patt.matcher(transformedPath);
		String[] loadedWilcards = this.getLoadedWildcards(offset);
		if (m.find()) {
			pathToRoot = transformedPath.substring(0, m.start());
			for (int i = 0; i < loadedWilcards.length; i++) {
				String wc = loadedWilcards[i];
				if (wc.equals(ROOT_WILDCARD)) {
					root = m.group(i + 1);
				} else if (wc.equals(LANGUAGE_WILDCARD)) {
					language = m.group(i + 1);
				} else if (wc.equals(COUNTRY_WILDCARD)) {
					country = m.group(i + 1);
				} else if (wc.equals(VARIANT_WILDCARD)) {
					variant = m.group(i + 1);
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
		wp2.setCountry(this.getCountry());
		wp2.setLanguage(this.getLanguage());
		wp2.setFileName(this.getFileName());
		wp2.setFileExtension(this.getFileExtension());
		wp2.setRoot(this.getRoot());
		wp2.setVariant(this.getVariant());
		return wp2;
	}

	public String getPath() {
		return getPath(0);
	}

	/**
	 * Gets path for the {@link LocalizedPropertiesPage}. If some
	 * wildcards do not exist in the path, they will be removed ALONG with
	 * the character, that PRECEEDS it.
	 * @return Modified path for a file created by the wizard.
     */
    public String getWizardPath() {
            String path = this.getExcludedOptional(0);

            //FIXME there is a problem commented in the issue 84, but it's Windows related.

            if (root != null && !("".equals(root))) {
                    path = path.replace(ROOT_WILDCARD, root);
            }
            else {
                    path = removeWildcard(ROOT_WILDCARD, path);
            }
            if (fileName != null && !("".equals(fileName))) {
                    path = path.replace(FILENAME_WILDCARD, fileName);
            }
            else {
                    path = removeWildcard(FILENAME_WILDCARD, path);
            }
            if (fileExtension != null && !("".equals(fileExtension))) {
                    path = path.replace(FILE_EXTENSION_WILDCARD, fileExtension);
            }
            else {
                    path = removeWildcard(FILE_EXTENSION_WILDCARD, path);
            }
            if (language != null && !("".equals(language))) {
                    path = path.replace(LANGUAGE_WILDCARD, language);
            }
            else {
                    path = removeWildcard(LANGUAGE_WILDCARD, path);
            }
            if (country != null && !("".equals(country))) {
                    path = path.replace(COUNTRY_WILDCARD, country);
            }
            else {
                    path = removeWildcard(COUNTRY_WILDCARD, path);
            }

            return path;
    }

    /**
     * Removes wildcard occurances from path
     * @param wildcard A wildcard, which will be removed from path.
     * @param path A path in which wildcard occurances will be searched.
     * @return Modified path without wildcard.
     */
    private String removeWildcard(String wildcard, String path) {
            Pattern pattern = Pattern.compile(String.format(IWildcardPath.REMOVAL_REGEX, wildcard));
            Matcher matcher = pattern.matcher(path);
            return matcher.replaceAll("");
    }


	public String getPath(int offset) {
		String path = this.getExcludedOptional(offset);
		path = path.replace(ROOT_WILDCARD, root == null ? TEXT_REGEX : root);
		path = path.replace(FILENAME_WILDCARD, fileName == null ? TEXT_REGEX
				: fileName);
		path = path.replace(FILE_EXTENSION_WILDCARD,
				fileExtension == null ? TEXT_REGEX : fileExtension);
		path = path.replace(LANGUAGE_WILDCARD,
				language == null ? LANGUAGE_REGEX : language);
		path = path.replace(COUNTRY_WILDCARD, country == null ? COUNTRY_REGEX
				: country);
		path = path.replace(VARIANT_WILDCARD, variant == null ? VARIANT_REGEX
				: variant);
		return path;
	}
}