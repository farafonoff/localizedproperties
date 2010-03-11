package com.triadsoft.properties.editors.actions;

import java.util.Iterator;
import java.util.Locale;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Table;

import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

/**
 * Accion que permite agregar un Locale al
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * 
 */
public class AddLocaleAction extends Action {
	protected static final String MENU_MENUITEM_ADD_LOCALE = "menu.menuitem.addLocale";
	private final PropertiesEditor editor;
	private final PropertyTableViewer viewer;
	private ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(
			this.getClass(), "/icons/locale_add.png");

	private final ISelectionChangedListener listener = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent e) {
			setEnabled(!e.getSelection().isEmpty());
		}
	};

	public AddLocaleAction(PropertiesEditor editor, PropertyTableViewer viewer) {
		super(Activator.getString(MENU_MENUITEM_ADD_LOCALE));
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
		Iterator<ISelection> iter = ((IStructuredSelection) sel).iterator();
		try {
			while (iter.hasNext()) {
				// TODO:Acá hay que hacer la magia para agregar
				iter.next();
			}
		} finally {
			table.setRedraw(true);
		}
	}
}
