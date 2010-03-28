package com.triadsoft.properties.editors.actions;

import java.util.Locale;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

/**
 * Accion que permite eliminar un locale
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * 
 */
public class RemoveLocaleAction extends Action {
	public static final String MENU_MENUITEM_DELETE_LOCALE = "menu.menuitem.deleteLocale";
	private final PropertiesEditor editor;
	private final Locale locale;
	private ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(
			this.getClass(), "/icons/locale_delete.png");

	private final ISelectionChangedListener listener = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent e) {
			setEnabled(!e.getSelection().isEmpty());
		}
	};

	public RemoveLocaleAction(PropertiesEditor editor,
			PropertyTableViewer viewer, Locale locale) {
		super(Activator.getString(MENU_MENUITEM_DELETE_LOCALE,
				new Object[] { locale.toString() }));
		super.setImageDescriptor(imageDescriptor);
		this.editor = editor;
		this.locale = locale;
		setEnabled(false);
		viewer.addSelectionChangedListener(listener);
	}

	@Override
	public void run() {
		try {
			if (editor.isDirty()) {
				MessageBox messageBox = new MessageBox(editor.getEditorSite()
						.getShell(), SWT.OK | SWT.ICON_WARNING);
				// TODO: Localizar
				messageBox
						.setMessage("Tiene cambios sin salvar, debe guardarlos antes de continuar");
				if (messageBox.open() == SWT.OK) {
					return;
				}
			}
			editor.getResource().removeLocale(locale);
		} catch (CoreException e) {
			Activator.getLogger().error(e.getLocalizedMessage());
		}
	}
}