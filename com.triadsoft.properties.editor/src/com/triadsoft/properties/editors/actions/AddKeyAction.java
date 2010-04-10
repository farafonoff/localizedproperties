package com.triadsoft.properties.editors.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

/**
 * Accion que permite agregar una clave a las properties
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * 
 */
public class AddKeyAction extends Action {
	public static final String NEW_KEY = "menu.menuitem.add";

	private final PropertiesEditor editor;
	private final PropertyTableViewer viewer;
	private ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(
			this.getClass(), "/icons/key_add.png");

	private final ISelectionChangedListener listener = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent e) {
			setEnabled(!e.getSelection().isEmpty());
		}
	};

	public AddKeyAction(PropertiesEditor editor, PropertyTableViewer viewer) {
		super(Activator.getString(NEW_KEY));
		super.setImageDescriptor(imageDescriptor);
		this.editor = editor;
		this.viewer = viewer;
		setEnabled(false);
		viewer.addSelectionChangedListener(listener);
	}

	@Override
	public void run() {
		Table table = viewer.getTable();
		table.setRedraw(false);
		try {

			AddKeyDialog dialog = new AddKeyDialog(PlatformUI.getWorkbench()
					.getDisplay().getActiveShell());
			if (dialog.open() != InputDialog.OK) {
				return;
			}
			editor.addKey(dialog.getNewKey());
		} finally {
			table.setRedraw(true);
		}
	}
}
