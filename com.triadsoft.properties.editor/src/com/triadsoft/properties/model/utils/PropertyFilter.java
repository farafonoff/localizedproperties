package com.triadsoft.properties.model.utils;

import java.util.Locale;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.triadsoft.properties.model.Property;

public class PropertyFilter extends ViewerFilter {

	private String searchText;
	private String searchString;
	private boolean matchCase = false;

	public void setSearchText(String s) {
		if (s == null) {
			this.searchString = null;
			this.searchText = null;
			return;
		}
		this.searchText = s;
		// Search must be a substring of the existing value
		this.searchString = ".*" + s + ".*";
	}

	public String getSearchText() {
		return searchText;
	}

	public void setMatchCase(boolean matchCase) {
		this.matchCase = matchCase;
	}

	public boolean getMatchCase() {
		return this.matchCase;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		}
		Property p = (Property) element;
		if (matchCase && p.getKey().matches(searchString)) {
			return true;
		} else if (!matchCase
				&& p.getKey().toUpperCase().matches(searchString.toUpperCase())) {
			return true;
		}
		// Ahora valido si el texto está en los locales
		Locale[] locales = p.getLocales();
		for (int i = 0; i < locales.length; i++) {
			String localized = p.getValue(locales[i]);
			if (matchCase && localized.matches(this.searchString)) {
				return true;
			} else if (!matchCase
					&& localized.toUpperCase().matches(
							this.searchString.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
}
