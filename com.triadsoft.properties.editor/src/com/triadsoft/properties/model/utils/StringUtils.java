package com.triadsoft.properties.model.utils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	protected static final String FULL_LOCALE = "([a-z]{2})_([A-Z]{2})_([\\w]+)";
	protected static final String LANGUAGE_COUNTRY_LOCALE = "([a-z]{2})_([A-Z]{2})";
	protected static final String LANGUAGE_LOCALE = "([a-z]{2})";

	public static Locale getLocale(String languageCountry) {
		// String language = extractValue(languageCountry,
		// IWildcardPath.LANGUAGE_REGEX);
		// String country = extractValue(languageCountry,
		// IWildcardPath.COUNTRY_REGEX);
		// String variant = extractValue(languageCountry,
		// IWildcardPath.VARIANT_REGEX);
		String language;
		String country;
		String variant;

		if (languageCountry.matches(FULL_LOCALE)) {
			Pattern p = Pattern.compile(FULL_LOCALE);
			Matcher m = p.matcher(languageCountry);
			if (m.find()) {
				language = m.group(1);
				country = m.group(2);
				variant = m.group(3);
				return new Locale(language, country, variant);
			}
		} else if (languageCountry.matches(LANGUAGE_COUNTRY_LOCALE)) {
			Pattern p = Pattern.compile(LANGUAGE_COUNTRY_LOCALE);
			Matcher m = p.matcher(languageCountry);
			if (m.find()) {
				language = m.group(1);
				country = m.group(2);
				return new Locale(language, country);
			}
		} else if (languageCountry.matches(LANGUAGE_LOCALE)) {
			Pattern p = Pattern.compile(LANGUAGE_LOCALE);
			Matcher m = p.matcher(languageCountry);
			if (m.find()) {
				language = m.group(1);
				return new Locale(language);
			}
		}

		// if (language != null && country != null && variant != null) {
		// return new Locale(language, country, variant);
		// } else if (language != null && country != null) {
		// return new Locale(language, country);
		// } else if (language != null && country == null) {
		// return new Locale(language);
		// }
		return null;
	}

	public static Locale getKeyLocale() {
		return new Locale("xx", "XX", "WIN");
	}

	/**
	 * It extract a text from segment that matches with regular expresion
	 * 
	 * @param searchText
	 * @param regex
	 * @return
	 */
	private static String extractValue(String searchText, String regex) {
		String searched = null;
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(searchText);

		if (m.find()) {
			searched = searchText.substring(m.start(), m.end());
		}
		return searched;
	}

	public static String join(String[] values, Character separator) {
		if (values == null) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < values.length; i++) {
			buffer.append(values[i]);
			buffer.append(separator);
		}
		if (buffer.length() > 1) {
			buffer.delete(buffer.length() - 1, buffer.length());
		}
		return buffer.toString();
	}
}
