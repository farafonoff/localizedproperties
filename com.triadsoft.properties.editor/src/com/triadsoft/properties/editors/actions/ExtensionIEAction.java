package com.triadsoft.properties.editors.actions;

import org.eclipse.jface.action.Action;

import com.triadsoft.properties.editor.extensions.IPropertiesExport;
import com.triadsoft.properties.editor.extensions.IPropertiesIE;
import com.triadsoft.properties.editor.extensions.IPropertiesImport;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.Property;

/**
 * This action is to handle import/export extensions Each instance handle each
 * reference to action
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * @since 0.8.0
 */
public class ExtensionIEAction extends Action {
	private IPropertiesIE extension;
	private final PropertiesEditor editor;

	public ExtensionIEAction(PropertiesEditor editor, IPropertiesIE extension) {
		super(extension.getName());
		this.extension = extension;
		this.editor = editor;
		setDescription(extension.getDescription());
	}

	@Override
	public void run() {
		Object[] properties = editor.getResource().getProperties();
		if (extension instanceof IPropertiesImport) {
			Property[] props = ((IPropertiesImport) extension)
					.importProperties();
		} else if (extension instanceof IPropertiesExport) {
			((IPropertiesExport) extension).exportProperties(new Property[0]);
		}
	}
}
