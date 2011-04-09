package com.triadsoft.properties.editors.actions;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;

import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

/**
 * Accion que permite eliminar una clave a las properties
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * 
 */
public class RemovePropertyAction extends Action {
	protected static final String MENU_MENUITEM_DELETE_KEY_CONFIRM_TITLE = "menu.menuitem.deleteKey.confirm.title";
	protected static final String MENU_MENUITEM_DELETE_KEY_CONFIRM_MESSAGE = "menu.menuitem.deleteKey.confirm.message";
	protected static final String MENU_MENUITEM_DELETE_KEY = "menu.menuitem.deleteKey";
	private PropertiesEditor editor;
	private PropertyTableViewer viewer;

	private final ImageDescriptor imageDescriptor = ImageDescriptor
			.createFromFile(this.getClass(), "/icons/key_delete.png");

	private final ISelectionChangedListener listener = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent e) {
			setEnabled(!e.getSelection().isEmpty());
		}
	};

	public RemovePropertyAction(PropertiesEditor editor,
			PropertyTableViewer viewer) {
		super(LocalizedPropertiesPlugin.getString(MENU_MENUITEM_DELETE_KEY));
		super.setImageDescriptor(imageDescriptor);
		this.editor = editor;
		this.viewer = viewer;
		viewer.addSelectionChangedListener(listener);
		setEnabled(false);
	}

	public void setEditor(PropertiesEditor editor) {
		this.editor = editor;
		if (this.viewer != null) {
			this.viewer.removeSelectionChangedListener(listener);
		}
		if (this.editor == null) {
			this.viewer = null;
			return;
		}
		this.viewer.addSelectionChangedListener(listener);
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
				MessageBox mb = new MessageBox(editor.getEditorSite()
						.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);
				mb.setMessage(LocalizedPropertiesPlugin.getString(
						MENU_MENUITEM_DELETE_KEY_CONFIRM_MESSAGE,
						new String[] { property.getKey() }));
				mb.setText(LocalizedPropertiesPlugin.getString(
						MENU_MENUITEM_DELETE_KEY_CONFIRM_TITLE,
						new String[] { property.getKey() }));
				if (mb.open() == SWT.YES) {
					editor.removeKey(property.getKey());
				}
			}
		} finally {
			table.setRedraw(true);
		}
	}
}
