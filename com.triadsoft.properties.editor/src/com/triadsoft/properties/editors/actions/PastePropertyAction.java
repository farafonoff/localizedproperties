package com.triadsoft.properties.editors.actions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;

import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.utils.StringUtils;

public class PastePropertyAction extends Action {
	private PropertiesEditor editor;

	public PastePropertyAction(PropertiesEditor editor) {
		super("PasteProperty");
		this.editor = editor;
		setEnabled(true);
	}

	public void setEditor(PropertiesEditor editor) {
		this.editor = editor;
	}

	public void run() {
		if (editor != null && editor.getTableViewer().isCellEditorActive()) {
			return;
		}
		if (pasteResources()) {
			return;
		}
	}

	private boolean pasteResources() {
		TextTransfer transfer = TextTransfer.getInstance();
		final Clipboard cb = new Clipboard(editor.getSite().getShell()
				.getDisplay());
		String props = (String) cb.getContents(transfer);
		if (props == null) {
			return false;
		}
		List<Property> properties = getProperties(props);
		for (Iterator<Property> iterator = properties.iterator(); iterator
				.hasNext();) {
			Property prop = (Property) iterator.next();
			Activator.getLogger().debug(prop.toString());
			this.editor.addProperty(prop);
		}
		return true;
	}

	private List<Property> getProperties(String props) {
		List<Property> p = new LinkedList<Property>();
		String[] properties = props.split(System.getProperty("line.separator"));
		for (int i = 0; i < properties.length; i++) {
			String[] values = properties[i].split("\\"
					+ Property.VALUES_SEPARATOR);
			Property prop = new Property(values[0]);
			this.parseLocales(prop, values);
			p.add(prop);
		}
		return p;
	}

	private void parseLocales(Property prop, String[] values) {
		for (int i = 1; i < values.length; i++) {
			Locale loc = StringUtils.getLocale(values[i]);
			if ((i + 1) >= values.length) {
				prop.setValue(loc, "");
			} else {
				prop.setValue(loc, values[i + 1]);
			}
			i++;
		}
	}
}
