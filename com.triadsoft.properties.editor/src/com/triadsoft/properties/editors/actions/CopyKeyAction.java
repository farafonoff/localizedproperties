package com.triadsoft.properties.editors.actions;

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Table;

import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

/**
 * Accion que permite agregar una clave a las properties
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * 
 */
public class CopyKeyAction extends Action {
	protected static final String MENU_MENUITEM_COPY = "menu.menuitem.copy";

	public static final String NEW_KEY = "new.key";

	private final PropertiesEditor editor;
	private final PropertyTableViewer viewer;
	private ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(
			this.getClass(), "/icons/key.png");

	private final ISelectionChangedListener listener = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent e) {
			setEnabled(!e.getSelection().isEmpty());
		}
	};

	public CopyKeyAction(PropertiesEditor editor, PropertyTableViewer viewer) {
		super(Activator.getString(MENU_MENUITEM_COPY));
		super.setImageDescriptor(imageDescriptor);
		this.editor = editor;
		this.viewer = viewer;
		setEnabled(true);
		viewer.addSelectionChangedListener(listener);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void run() {
		Table table = viewer.getTable();
		table.setRedraw(false);
		ISelection sel = viewer.getSelection();
		Iterator<Property> iter = ((IStructuredSelection) sel).iterator();
		try {
			while (iter.hasNext()) {
				Property property = (Property) iter.next();
				final Clipboard cb = new Clipboard(editor.getSite().getShell()
						.getDisplay());
				TextTransfer textTransfer = TextTransfer.getInstance();
				Transfer[] transfers = new Transfer[] { textTransfer };
				Object[] data = new Object[] { property.getKey() };
				cb.setContents(data, transfers);
				System.out.println("Copiando la propiedad.."
						+ property.getKey());
			}
		} finally {
			table.setRedraw(true);
		}
	}
}
