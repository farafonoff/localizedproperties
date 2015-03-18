package com.triadsoft.properties.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.triadsoft.properties.model.utils.StringUtils;

/**
 * <p>
 * This class contains the value of property, and the translation for all
 * contained locales.
 * </p>
 * <p>
 * Is used to draw all properties in properties tables and to interchange data
 * with extensions.
 * </p>
 * <p>
 * Then we have one keyname and translations as locales have.
 * </p>
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * @see #getLocales(),#getValue(Locale)
 */
public class Property {
	public static String VALUES_SEPARATOR = "|";
	private String key;
	private Map<Locale, String> values = new HashMap<Locale, String>();
	private Map<Locale, Error> errors = new HashMap<Locale, Error>();

	/**
	 * Default constructor
	 * 
	 * @param key
	 */
	public Property(String key) {
		this.key = key;
	}

	/**
	 * Return key code value
	 * 
	 * @return String con la clave
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Return the translation for the locale passed as parameter
	 * 
	 * @param locale
	 * @return
	 */
	public String getValue(Locale locale) {
		return values.get(locale);
	}

	/**
	 * Return the contained locales.
	 * 
	 * @return
	 */
	public Locale[] getLocales() {
		return values.keySet().toArray(new Locale[values.keySet().size()]);
	}

	/**
	 * Set a translation to the locale passed as parameter
	 * 
	 * @param locale
	 * @param value
	 */
	public void setValue(Locale locale, String value) {
		this.values.put(locale, value);
	}

	/**
	 * Allow to add an error into the locale passed as parameter
	 * 
	 * @param locale
	 * @param error
	 */
	protected void addError(Locale locale, Error error) {
		errors.put(locale, error);
	}

	/**
	 * Return the map of errors, by locale
	 * 
	 * @return
	 */
	public Map<Locale, Error> getErrors() {
		return errors;
	}

	public Error getError(Locale locale) {
		return errors.get(locale);
	}

	/**
	 * It is a simple implementation
	 */
	@Override
	public String toString() {
		String ret = key;
		Iterator<Locale> iter = values.keySet().iterator();
		while (iter.hasNext()) {
			Locale loc = iter.next();
			ret += "|";
			ret += loc.toString();
			ret += "|";
			ret += values.get(loc).toString();
		}
		return ret;
	}

	public void updateError() {
		Map<String, Set<Locale>> reverseMap = new HashMap<String, Set<Locale>>();
		Map<String, Set<Locale>> sameLangLocales = new HashMap<String, Set<Locale>>();
		for (Locale locale : values.keySet()) {
			String value = values.get(locale);
			if (value == null || value.trim().length() == 0) {
				value = "";
				addError(locale, new PropertyError(PropertyError.VOID_VALUE,
						"No se encontro valor para"));
			} else {
				if (!reverseMap.containsKey(value)) {
					reverseMap.put(value, new HashSet<Locale>());
				}
				if (!sameLangLocales.containsKey(locale.getLanguage())) {
					sameLangLocales.put(locale.getLanguage(), new HashSet<Locale>());
				}
				reverseMap.get(value).add(locale);
				sameLangLocales.get(locale.getLanguage()).add(locale);
			}
		}
		for (String value:reverseMap.keySet()) {
			Set<Locale> sharingLocales = reverseMap.get(value);
			for (Locale locale : sharingLocales) {

				String lang = locale.getLanguage();
				Set<Locale> langList = sameLangLocales.get(lang);
				for (Locale loc : sharingLocales) {
					if (!langList.contains(loc)) {
						addError(loc, new PropertyError(PropertyError.UK_TEXT,
								"string shared for different languages"));
					}
				}
				for (Locale loc : langList) {
					if (!sharingLocales.contains(loc)) {
						addError(loc, new PropertyError(PropertyError.UK_TEXT,
								"string not same on all language variants"));
					}
				}
			}
		}
	}
}