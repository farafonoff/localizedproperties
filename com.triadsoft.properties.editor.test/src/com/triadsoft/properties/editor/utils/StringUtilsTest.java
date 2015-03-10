package com.triadsoft.properties.editor.utils;

import java.util.Locale;

import junit.framework.TestCase;

import com.triadsoft.properties.model.utils.StringUtils;

public class StringUtilsTest extends TestCase {
	public void setUp() throws Exception {
	}

	public void tearDown() throws Exception {
	}

	public void testLocaleWithVariant() {
		String text = "en_US_WIN";
		Locale locale = StringUtils.getLocale(text);
		assertTrue("The language must be en, and was " + locale.getLanguage(),
				"en".equals(locale.getLanguage()));
		assertTrue("The country must be US, and was " + locale.getCountry(),
				"US".equals(locale.getCountry()));
		assertTrue("The variant must be WIN, and was " + locale.getVariant(),
				"WIN".equals(locale.getVariant()));
	}

	public void testLocaleWithouthVariant() {
		String text = "en_US";
		Locale locale = StringUtils.getLocale(text);
		assertTrue("The language must be en, and was " + locale.getLanguage(),
				"en".equals(locale.getLanguage()));
		assertTrue("The country must be US, and was " + locale.getCountry(),
				"US".equals(locale.getCountry()));
		assertTrue("The variant must be null, and was " + locale.getVariant(),
				locale.getVariant() == null
						|| locale.getVariant().length() == 0);
	}

	public void testLocaleWithouthVariantAndCountry() {
		String text = "en";
		Locale locale = StringUtils.getLocale(text);
		assertTrue("The language must be en, and was " + locale.getLanguage(),
				"en".equals(locale.getLanguage()));
		assertTrue("The country must be US, and was '" + locale.getCountry()
				+ "'", locale.getCountry() == null
				|| locale.getCountry().length() == 0);
		assertTrue("The variant must be null, and was " + locale.getVariant(),
				locale.getVariant() == null
						|| locale.getVariant().length() == 0);
	}
	
	public void testLocaleIfAreEquals() {
		String text = "en_US_WIN";
		String text1 = "en_US";
		Locale locale = StringUtils.getLocale(text);
		Locale locale1 = StringUtils.getLocale(text1);
		assertFalse("No son distintos!!",locale.equals(locale1));
	}
}
