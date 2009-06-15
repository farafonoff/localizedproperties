package com.triadsoft.properties.editors.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import com.triadsoft.properties.editors.AddKeyDialog;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

/**
 * Accion que permite agregar una clave a las properties
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 * 
 */
public class AddKeyAction extends Action {
	public static final String NEW_KEY = "new.key";

	private final PropertiesEditor editor;
	private final PropertyTableViewer viewer;
	private ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(
			this.getClass(), "/icons/key_add.png");

	private final ISelectionChangedListener listener = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent e) {
			// System.out.println(e.getSelection());
			setEnabled(!e.getSelection().isEmpty());
		}
	};

	public AddKeyAction(PropertiesEditor editor, PropertyTableViewer viewer,
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
