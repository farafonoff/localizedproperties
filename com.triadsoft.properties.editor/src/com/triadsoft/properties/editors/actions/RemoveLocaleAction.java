package com.triadsoft.properties.editors.actions;

import java.util.Locale;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.utils.LocalizedPropertiesLog;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

/**
 * Accion que permite eliminar un locale
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * 
 */
public class RemoveLocaleAction extends Action {
	protected static final String MENU_MENUITEM_DELETE_LOCALE_CONFIRM_TITLE = "menu.menuitem.deleteLocale.confirm.title";
	protected static final String MENU_MENUITEM_DELETE_LOCALE_CONFIRM_MESSAGE = "menu.menuitem.deleteLocale.confirm.message";

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
		super(LocalizedPropertiesPlugin.getString(MENU_MENUITEM_DELETE_LOCALE,
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
				messageBox
						.setMessage(LocalizedPropertiesPlugin
								.getString(
										AddLocaleAction.MENU_MENUITEM_ADD_LOCALE_UNSAVEDDATA_MESSAGE,
										new String[] { locale.toString() }));
				messageBox
						.setText(LocalizedPropertiesPlugin
								.getString(
										AddLocaleAction.MENU_MENUITEM_ADD_LOCALE_UNSAVEDDATA_TITLE,
										new String[] { locale.toString() }));
				if (messageBox.open() == SWT.OK) {
					return;
				}
			}
			MessageBox messageBox = new MessageBox(editor.getEditorSite()
					.getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);
			messageBox.setMessage(LocalizedPropertiesPlugin.getString(
					MENU_MENUITEM_DELETE_LOCALE_CONFIRM_MESSAGE,
					new String[] { locale.toString() }));
			messageBox.setText(LocalizedPropertiesPlugin.getString(
					MENU_MENUITEM_DELETE_LOCALE_CONFIRM_TITLE,
					new String[] { locale.toString() }));
			if (messageBox.open() == SWT.YES) {
				editor.getResource().removeLocale(locale);
			}

		} catch (CoreException e) {
			LocalizedPropertiesLog.error(e.getLocalizedMessage());
		}
	}
}