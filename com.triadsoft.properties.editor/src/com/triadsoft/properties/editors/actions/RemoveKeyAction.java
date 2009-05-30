package com.triadsoft.properties.editors.actions;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Table;

import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.ResourceList;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

/**
 * Accion que permite eliminar una clave a las properties
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 * 
 */
public class RemoveKeyAction extends Action {
	private final PropertiesEditor editor;
	private final PropertyTableViewer viewer;

	private final ImageDescriptor imageDescriptor = ImageDescriptor
			.createFromFile(this.getClass(), "/icons/key_delete.png");

	private final ISelectionChangedListener listener = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent e) {
			setEnabled(!e.getSelection().isEmpty());
		}
	};

	public RemoveKeyAction(PropertiesEditor editor, PropertyTableViewer viewer,
			String text) {
		super(text);
		super.setImageDescriptor(imageDescriptor);
		this.editor = editor;
		this.viewer = viewer;
		setEnabled(false);
		viewer.addSelectionChangedListener(listener);
	}

	@Override
	public void run() {
		ISelection sel = viewer.getSelection();
		Table table = viewer.getTable();
		table.setRedraw(false);
		Iterator iter = ((IStructuredSelection) sel).iterator();
		try {
			while (iter.hasNext()) {
				Property property = (Property) iter.next();
				editor.removeKey(property.getKey());
			}
		} finally {
			table.setRedraw(true);
		}
	}
}
