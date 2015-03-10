package com.triadsoft.properties.preferences;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * @deprecated El enconding ahora se obtiene del encoding del archivo.
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 * @deprecated Se mantiene para no romper el paquete, pero no se usa mas
 */
public class EncodingEditor extends ListEditor {
	private Composite parent;

	public EncodingEditor(String name, String label, Composite parent) {
		super(name, label, parent);
		this.setParent(parent);
	}

	@Override
	protected String createList(String[] items) {
		// StringBuffer path = new StringBuffer();
		// for (int i = 0; i < items.length; i++) {
		// path.append(items[i]);
		// path.append(PreferenceConstants.LANGUAGE_CONTENT_TYPE_SEPARATOR);
		// }
		// return path.toString();
		return "";
	}

	@Override
	protected String getNewInputObject() {
		EncodingDialog dialog = new EncodingDialog(getShell());
		if (dialog.open() != InputDialog.OK) {
			return null;
		}
		return dialog.getLanguageEncoding();
	}

	@Override
	protected String[] parseString(String stringList) {
		// String regex = "\\"
		// + PreferenceConstants.LANGUAGE_CONTENT_TYPE_SEPARATOR;
		// String[] strings = stringList.split(regex);
		// return strings;
		return new String[0];
	}

	public void setParent(Composite parent) {
		this.parent = parent;
	}

	public Composite getParent() {
		return parent;
	}

}
