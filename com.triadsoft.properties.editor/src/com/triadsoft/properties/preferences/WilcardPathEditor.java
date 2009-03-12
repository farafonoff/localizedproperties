package com.triadsoft.properties.preferences;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;

public class WilcardPathEditor extends ListEditor {

	public WilcardPathEditor(String name, String label, Composite parent) {
		super(name, label, parent);
	}

	protected String[] parseString(String stringList) {
		String regex = "\\" + PreferenceConstants.WILDCARD_PATH_SEPARATOR;
		String[] strings = stringList.split(regex);
		return strings;
	}

	@Override
	protected String createList(String[] items) {
		StringBuffer path = new StringBuffer();
		for (int i = 0; i < items.length; i++) {
			path.append(items[i]);
			path.append(PreferenceConstants.WILDCARD_PATH_SEPARATOR);
		}
		return path.toString();
	}

	@Override
	protected String getNewInputObject() {
		return null;
	}

}
