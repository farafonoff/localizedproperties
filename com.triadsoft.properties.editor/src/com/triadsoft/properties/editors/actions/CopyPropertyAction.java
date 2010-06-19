package com.triadsoft.properties.editors.actions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.utils.PropertyTransfer;

public class CopyPropertyAction extends Action {
	private PropertiesEditor editor;
	private TableViewer viewer;
	private ViewerCell viewerCell = null;
	private ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(
			this.getClass(), "/icons/page_copy.png");
	
	private ColumnViewerEditorActivationListener listener = new ColumnViewerEditorActivationListener() {

		@Override
		public void beforeEditorDeactivated(
				ColumnViewerEditorDeactivationEvent arg0) {
			//Nothing to do
		}

		@Override
		public void beforeEditorActivated(
				ColumnViewerEditorActivationEvent event) {
			//Nothing to do
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

	public CopyPropertyAction(PropertiesEditor editor) {
		super(Activator.getString("menu.menuitem.copyProperty.label"));
		setEditor(editor);
		setImageDescriptor(imageDescriptor);
	}

	public void setEditor(PropertiesEditor editor) {
		this.editor = editor;
		if (editor == null) {
			return;
		}
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

	@SuppressWarnings("unchecked")
	public void run() {
		final Clipboard cb = new Clipboard(editor.getSite().getShell()
				.getDisplay());

		if (editor.getTableViewer().isCellEditorActive() && viewerCell != null) {
			cb.setContents(new Object[] { viewerCell.getText() },
					new Transfer[] { TextTransfer.getInstance() });
			return;
		}
		ISelection sel = editor.getTableViewer().getSelection();
		Iterator<Property> iter = ((IStructuredSelection) sel).iterator();

		List props = new LinkedList();
		while (iter.hasNext()) {
			props.add((Property) iter.next());
		}
		if (props.size() == 0) {
			return;
		}
		Property[] properties = (Property[]) props.toArray(new Property[props
				.size()]);
		try {
			cb.setContents(new Object[] { properties },
					new Transfer[] { PropertyTransfer.getInstance()

					});
		} catch (SWTError err) {
			err.printStackTrace();
			Activator.getLogger().error("No se pudo copiar al clipboard");
		}
		Activator.getLogger().debug(
				"Se han copiado " + props.size() + " propiedad al clipboard");
	}

	public static String asText(Property[] properties) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < properties.length; i++) {
			buffer.append(properties[i].toString());
			buffer.append(System.getProperty("line.separator"));
		}
		return buffer.toString();
	}
}
