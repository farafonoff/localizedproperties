package com.triadsoft.properties.editors.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;

import com.triadsoft.common.utils.LocalizedPropertiesMessages;
import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

/**
 * Accion que permite agrandar y achicar la fuente
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 */
public class DecreaseFontAction extends Action {
	public static final String NEW_KEY = "menu.menuitem.fontSize.decrease";

	private PropertyTableViewer viewer;
	private ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(
			this.getClass(), "/icons/font_decrease.png");

	public DecreaseFontAction(PropertyTableViewer viewer) {
		super(LocalizedPropertiesMessages.getString(NEW_KEY));
		this.viewer = viewer;
		super.setImageDescriptor(imageDescriptor);
		super.setAccelerator(SWT.CTRL | '-');
	}

	public void setEditor(PropertiesEditor editor) {
		if (editor != null) {
			this.viewer = (PropertyTableViewer) editor.getTableViewer();
		}
	}

	@Override
	public void run() {
		// float size =
		// viewer.getTable().getFont().getFontData()[0].getHeight();
		float size = LocalizedPropertiesPlugin.getDefault()
				.getPreferenceStore()
				.getFloat(PropertyTableViewer.PREFERENCES_FONT_SIZE);
		float step = LocalizedPropertiesPlugin.getDefault()
				.getPreferenceStore()
				.getFloat(IncreaseFontAction.PREFERENCES_FONT_SIZE_STEP);
		size -= step;
		viewer.setFontSize(size);
	}
}
