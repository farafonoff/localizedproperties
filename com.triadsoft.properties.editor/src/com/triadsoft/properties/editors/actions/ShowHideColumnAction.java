package com.triadsoft.properties.editors.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

public class ShowHideColumnAction extends Action {
	private final PropertyTableViewer viewer;
	private final TableColumn column;
	private ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(
			this.getClass(), "/icons/show_column.png");

	public ShowHideColumnAction(PropertiesEditor editor,
			PropertyTableViewer viewer, TableColumn column, String text) {
		super(text);
		super.setImageDescriptor(imageDescriptor);
		this.viewer = viewer;
		this.column = column;
	}

	@Override
	public void run() {
		Table table = viewer.getTable();
		table.setRedraw(false);
		try {
			if (column.getWidth() == 0) {
				column.setWidth(200);
			} else {
				column.setWidth(0);
			}
		} finally {
			table.setRedraw(true);
		}
	}
}
