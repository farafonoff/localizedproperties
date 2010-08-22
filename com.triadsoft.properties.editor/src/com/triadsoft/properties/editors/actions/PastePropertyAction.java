package com.triadsoft.properties.editors.actions;

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;

import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.utils.PropertyTransfer;

public class PastePropertyAction extends Action {
	private PropertiesEditor editor;
	private TableViewer viewer;
	private ViewerCell viewerCell;
	private ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(
			this.getClass(), "/icons/page_paste.png");

	private ColumnViewerEditorActivationListener listener = new ColumnViewerEditorActivationListener() {

		@Override
		public void beforeEditorDeactivated(
				ColumnViewerEditorDeactivationEvent arg0) {

		}

		@Override
		public void beforeEditorActivated(
				ColumnViewerEditorActivationEvent event) {

		}

		@Override
		public void afterEditorDeactivated(
				ColumnViewerEditorDeactivationEvent event) {
			viewerCell = null;
		}

		@Override
		public void afterEditorActivated(ColumnViewerEditorActivationEvent event) {
			viewerCell = (ViewerCell) event.getSource();
		}
	};

	public PastePropertyAction(PropertiesEditor editor) {
		super(Activator.getString("menu.menuitem.pasteProperty.label"));
		this.editor = editor;
		setEnabled(true);
		setImageDescriptor(imageDescriptor);
	}

	public void setEditor(PropertiesEditor editor) {
		this.editor = editor;
		if (this.viewer != null && this.viewer.getColumnViewerEditor() != null) {
			this.viewer.getColumnViewerEditor().removeEditorActivationListener(
					listener);
		}
		this.viewer = editor.getTableViewer();
		if (this.viewer != null && this.viewer.getColumnViewerEditor() != null) {
			this.viewer.getColumnViewerEditor().addEditorActivationListener(
					listener);
		}
	}

	public void run() {

		List<Property> properties = getProperties();
		String text = getTextValue();
		if (this.viewer != null && viewerCell != null) {
		//if (this.viewer != null && this.viewer.isCellEditorActive()) {
			if (properties != null) {
				viewerCell.setText(CopyPropertyAction.asText(properties
						.toArray(new Property[properties.size()])));
				return;
			} else if (text != null) {
				// Pego el contenido como texto
				viewerCell.setText(text);
				return;
			}
		} else if (this.viewer != null && !this.viewer.isCellEditorActive()) {
			if (properties != null) {
				for (Iterator<Property> iterator = properties.iterator(); iterator
						.hasNext();) {
					Property prop = (Property) iterator.next();
					Activator.getLogger().debug(prop.toString());
					this.editor.addProperty(prop);
				}
			} else {
				// FIXME: Que hago acá?
			}
		}

	}

	@SuppressWarnings("unchecked")
	private List<Property> getProperties() {
		PropertyTransfer transfer = PropertyTransfer.getInstance();
		final Clipboard cb = new Clipboard(editor.getSite().getShell()
				.getDisplay());
		List<Property> props = (List<Property>) cb.getContents(transfer);
		if (props == null) {
			return null;
		}
		return props;
	}

	private String getTextValue() {
		TextTransfer transfer = TextTransfer.getInstance();
		final Clipboard cb = new Clipboard(editor.getSite().getShell()
				.getDisplay());
		return (String) cb.getContents(transfer);
	}
}
