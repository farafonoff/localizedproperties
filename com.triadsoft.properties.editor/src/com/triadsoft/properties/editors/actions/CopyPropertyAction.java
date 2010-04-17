package com.triadsoft.properties.editors.actions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.Property;

public class CopyPropertyAction extends Action {
	private PropertiesEditor editor;

	public CopyPropertyAction(PropertiesEditor editor) {
		super("CopyProperty");
		setEditor(editor);
	}

	public void setEditor(PropertiesEditor editor) {
		this.editor = editor;
		if (editor == null) {
			return;
		}
	}

	@SuppressWarnings("unchecked")
	public void run() {
		// Si el editor está en modo edicion
		if (editor != null && editor.getTableViewer().isCellEditorActive()) {
			return;
		}
		ISelection sel = editor.getTableViewer().getSelection();
		Iterator<Property> iter = ((IStructuredSelection) sel).iterator();

		List props = new LinkedList();
		while (iter.hasNext()) {
			props.add((Property) iter.next());
		}
		if (props.size() == 0) {
			return;
		}
		Property[] properties = (Property[]) props.toArray(new Property[props
				.size()]);
		final Clipboard cb = new Clipboard(editor.getSite().getShell()
				.getDisplay());
		try {
			cb.setContents(new Object[] { asText(properties) },
					new Transfer[] { TextTransfer.getInstance()

					});
		} catch (SWTError err) {
			err.printStackTrace();
			Activator.getLogger().error("No se pudo copiar al clipboard");
		}
		Activator.getLogger().debug(
				"Se han copiado " + props.size() + " propiedad al clipboard");
	}

	private static String[] asTextArray(Property[] properties) {
		List<String> props = new LinkedList<String>();
		for (int i = 0; i < properties.length; i++) {
			props.add(properties[i].toString());
		}
		return props.toArray(new String[props.size()]);
	}

	private static String asText(Property[] properties) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < properties.length; i++) {
			buffer.append(properties[i].toString());
			buffer.append(System.getProperty("line.separator"));
		}
		return buffer.toString();
	}
}
