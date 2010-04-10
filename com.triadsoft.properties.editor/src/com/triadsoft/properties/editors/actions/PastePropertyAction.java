package com.triadsoft.properties.editors.actions;

import java.util.Locale;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;

import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.utils.PropertyTableViewer;
import com.triadsoft.properties.model.utils.StringUtils;

public class PastePropertyAction extends Action {
	private PropertiesEditor editor;

	private final ISelectionChangedListener listener = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent e) {
//			if (PropertyTableViewer.class.isInstance(e.getSelectionProvider())) {
//				PastePropertyAction.this.viewer = (PropertyTableViewer) e
//						.getSource();
//			}
		}
	};

	public PastePropertyAction() {
		super("PasteProperty");
		setEnabled(true);
	}
	
	public void setEditor(PropertiesEditor editor){
		this.editor = editor;
	}

	public void run() {
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
		String[] properties = props.split(System.getProperty("line.separator"));
		for (int i = 0; i < properties.length; i++) {
			String[] values = properties[i].split("\\"
					+ Property.VALUES_SEPARATOR);
			Property prop = new Property(values[0]);
			this.parseLocales(prop, values);
			this.editor.addProperty(prop);
			Activator.getLogger().debug(prop.toString());
		}
		return true;
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
