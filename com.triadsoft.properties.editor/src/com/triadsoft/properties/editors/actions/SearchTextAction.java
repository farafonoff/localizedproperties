package com.triadsoft.properties.editors.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;

import com.triadsoft.common.utils.LocalizedPropertiesMessages;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.editors.PropertiesLabelProvider;
import com.triadsoft.properties.model.utils.PropertyFilter;
import com.triadsoft.properties.model.utils.PropertyTableViewer;
import com.triadsoft.properties.preferences.SearchTextDialog;

/**
 * Accion que permite agregar un Locale al
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * 
 */
public class SearchTextAction extends Action {
	protected static final String ACTION_ADD_SEARCH_DIALOG_TITLE = "action.add.search.dialog.title";
	private PropertiesEditor editor;

	private ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(
			this.getClass(), "/icons/find.png");

	public SearchTextAction(PropertiesEditor editor) {
		super(LocalizedPropertiesMessages.getString(LocalizedPropertiesMessages
				.getString(ACTION_ADD_SEARCH_DIALOG_TITLE)));
		super.setImageDescriptor(imageDescriptor);
		setEditor(editor);
	}

	public void setEditor(PropertiesEditor editor) {
		if (editor != null) {
			this.editor = editor;
		}
	}

	@Override
	public void run() {
		try {
			SearchTextDialog dialog = new SearchTextDialog(PlatformUI
					.getWorkbench().getDisplay().getActiveShell());
			PropertyFilter filter = (PropertyFilter) ((PropertyTableViewer) editor
					.getTableViewer()).getFilters()[0];
			dialog.setSearchText(filter.getSearchText());
			dialog.setMatchCase(filter.getMatchCase());

			if (dialog.open() == InputDialog.OK) {
				filter.setSearchText(dialog.getSearchText());
				filter.setMatchCase(dialog.getMatchCase());
				((PropertiesLabelProvider) ((PropertyTableViewer) editor
						.getTableViewer()).getLabelProvider())
						.setSearchText(dialog.getSearchText());
			}
			editor.getTableViewer().refresh(true);
		} finally {

		}
	}
}
