package com.triadsoft.properties.preferences;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;

import com.triadsoft.properties.editor.Activator;

public class WilcardPathEditor extends ListEditor {

	protected Composite parent;

	public WilcardPathEditor(String name, String label, Composite parent) {
		super(name, label, parent);
		this.parent = parent;
	}

	protected String[] parseString(String stringList) {
		String regex = "\\" + getSeparator();
		String[] strings = stringList.split(regex);
		return strings;
	}

	@Override
	protected String createList(String[] items) {
		StringBuffer path = new StringBuffer();
		String separator = getSeparator();
		for (int i = 0; i < items.length; i++) {
			path.append(items[i]);
			path.append(separator);
		}
		return path.toString();
	}

	@Override
	protected String getNewInputObject() {
		WilcardPathDialog dialog = new WilcardPathDialog(getShell());
		if (dialog.open() != InputDialog.OK) {
			return null;
		}
		return dialog.getWildcardPath();
	}

	private String getSeparator() {
		String separator = Activator
				.getDefault()
				.getPluginPreferences()
				.getString(
						PreferenceConstants.WILDCARD_PATH_SEPARATOR_PREFERENCES);
		return separator == null ? "|" : separator;
	}
}
