package com.triadsoft.properties.editors.actions;

import java.util.Iterator;
import java.util.Locale;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import com.triadsoft.common.utils.LocalizedPropertiesMessages;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

public class SetDefaultTextAction extends Action {
	public static final String FILL_VALUES = "menu.menuitem.fillvalues";
	private static final String PREFERENCES_FILL_DIALOG_TITLE = "preferences.paste.dialog.title";
	private static final String PREFERENCES_FILL_KEY_DIALOG_LABEL = "preferences.paste.dialog.label";
	private static final String PREFERENCES_FILL_KEY_DIALOG_DESCRIPTION = "preferences.paste.dialog.description";

	private final PropertiesEditor editor;
	private final PropertyTableViewer viewer;
	private Locale selectedLocale;
	private ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(
			this.getClass(), "/icons/page_copy.png");

	private final ISelectionChangedListener listener = new ISelectionChangedListener() {
		public void selectionChanged(SelectionChangedEvent e) {
			setEnabled(!e.getSelection().isEmpty());
		}
	};
	

	public Locale getSelectedLocale() {
		return selectedLocale;
	}

	public void setSelectedLocale(Locale value) {
		this.selectedLocale = value;
	}	
	
	public SetDefaultTextAction(PropertiesEditor editor,
			PropertyTableViewer viewer) {
		super(LocalizedPropertiesMessages.getString(FILL_VALUES));
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
				String defaultText = null;
				if (selectedLocale != null) {
					defaultText = property.getValue(selectedLocale);
				}
				GetStringDialog dialog = new GetStringDialog(PlatformUI
						.getWorkbench().getDisplay().getActiveShell(),
						PREFERENCES_FILL_DIALOG_TITLE,
						PREFERENCES_FILL_KEY_DIALOG_LABEL,
						PREFERENCES_FILL_KEY_DIALOG_DESCRIPTION, defaultText);
				if (dialog.open() != InputDialog.OK) {
					return;
				}
				String text = dialog.getText();
				if (text==null) {
					text = "";
				}
				editor.bulkValueChangeStart();
				for (Locale locale : property.getLocales()) {
					editor.bulkValueChanged(property.getKey(), text, locale);
				}
				editor.bulkValueChangeEnd();
			}
		} finally {
			table.setRedraw(true);
		}
	}
}
