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

import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

/**
 * Accion que permite eliminar una clave a las properties
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * 
 */
public class RemoveKeyAction extends Action {
	protected static final String MENU_MENUITEM_DELETE_KEY_CONFIRM_TITLE = "menu.menuitem.deleteKey.confirm.title";
	protected static final String MENU_MENUITEM_DELETE_KEY_CONFIRM_MESSAGE = "menu.menuitem.deleteKey.confirm.message";
	protected static final String MENU_MENUITEM_DELETE_KEY = "menu.menuitem.deleteKey";
	private final PropertiesEditor editor;
	private final PropertyTableViewer viewer;

	private final ImageDescriptor imageDescriptor = ImageDescriptor
			.createFromFile(this.getClass(), "/icons/key_delete.png");

	private final ISelectionChangedListener listener = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent e) {
			setEnabled(!e.getSelection().isEmpty());
		}
	};

	public RemoveKeyAction(PropertiesEditor editor, PropertyTableViewer viewer) {
		super(Activator.getString(MENU_MENUITEM_DELETE_KEY));
		super.setImageDescriptor(imageDescriptor);
		this.editor = editor;
		this.viewer = viewer;
		viewer.addSelectionChangedListener(listener);
		setEnabled(false);
	}

	@SuppressWarnings("unchecked")
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
				mb.setMessage(Activator.getString(
						MENU_MENUITEM_DELETE_KEY_CONFIRM_MESSAGE,
						new String[] { property.getKey() }));
				mb.setText(Activator.getString(
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
