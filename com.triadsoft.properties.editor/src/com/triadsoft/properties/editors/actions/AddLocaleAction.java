package com.triadsoft.properties.editors.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.editors.AddLocaleDialog;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

/**
 * Accion que permite agregar un Locale al
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * 
 */
public class AddLocaleAction extends Action {
	public static final String MENU_MENUITEM_ADD_LOCALE_UNSAVEDDATA_TITLE = "menu.menuitem.locale.unsaveddata.title";
	public static final String MENU_MENUITEM_ADD_LOCALE_UNSAVEDDATA_MESSAGE = "menu.menuitem.locale.unsaveddata.message";
	protected static final String MENU_MENUITEM_ADD_LOCALE = "menu.menuitem.addLocale";
	private final PropertiesEditor editor;
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
		setEnabled(false);
		viewer.addSelectionChangedListener(listener);
	}

	@Override
	public void run() {
		try {
			if (editor.isDirty()) {
				MessageBox messageBox = new MessageBox(editor.getEditorSite()
						.getShell(), SWT.OK | SWT.ICON_WARNING);
				messageBox
						.setMessage(Activator
								.getString(MENU_MENUITEM_ADD_LOCALE_UNSAVEDDATA_MESSAGE));
				messageBox.setText(Activator
						.getString(MENU_MENUITEM_ADD_LOCALE_UNSAVEDDATA_TITLE));
				if (messageBox.open() == SWT.OK) {
					return;
				}
			}
			AddLocaleDialog dialog = new AddLocaleDialog(PlatformUI
					.getWorkbench().getDisplay().getActiveShell());
			if (dialog.open() != InputDialog.OK) {
				return;
			}
			editor.getResource().addLocale(dialog.getNewLocale());
		} finally {

		}
	}
}
