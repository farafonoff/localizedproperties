package com.triadsoft.properties.editors.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.utils.PropertyFilter;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

public class RemoveSearchTextAction extends Action {
	protected static final String ACTION_REMOVE_SEARCH_DIALOG_TITLE = "action.remove.search.dialog.title";
	private PropertiesEditor editor;

	private ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(
			this.getClass(), "/icons/find_cancel.png");

	public RemoveSearchTextAction(PropertiesEditor editor) {
		super(LocalizedPropertiesPlugin.getString(ACTION_REMOVE_SEARCH_DIALOG_TITLE));
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
			PropertyFilter filter = (PropertyFilter) ((PropertyTableViewer) editor
					.getTableViewer()).getFilters()[0];
			filter.setMatchCase(false);
			filter.setSearchText(null);
			editor.getTableViewer().refresh(true);
		} finally {

		}
	}
}