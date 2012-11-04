package com.triadsoft.properties.editors.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import com.triadsoft.common.utils.LocalizedPropertiesMessages;
import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;
import com.triadsoft.properties.editors.PropertiesEditor;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

/**
 * Accion que permite agrandar y achicar la fuente TODO:Translate
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 */
public class IncreaseFontAction extends Action {

	public static final String PREFERENCES_FONT_SIZE_STEP = "preferences.font.size.step";

	public static final String NEW_KEY = "menu.menuitem.fontSize.increase";

	private PropertyTableViewer viewer;
	private ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(
			this.getClass(), "/icons/font_increase.png");

	public IncreaseFontAction(PropertyTableViewer viewer) {
		super(LocalizedPropertiesMessages.getString(NEW_KEY));
		this.viewer = viewer;
		super.setImageDescriptor(imageDescriptor);
		// super.setAccelerator(SWT.CTRL | 'O');
	}

	public void setEditor(PropertiesEditor editor) {
		if (editor != null) {
			this.viewer = (PropertyTableViewer) editor.getTableViewer();
		}
	}

	@Override
	public void run() {
		if (this.viewer == null) {
			return;
		}
		// float size = viewer.getFontSize();
		float size = LocalizedPropertiesPlugin.getDefault()
				.getPreferenceStore()
				.getFloat(PropertyTableViewer.PREFERENCES_FONT_SIZE);
		size += LocalizedPropertiesPlugin.getDefault().getPreferenceStore()
				.getFloat(PREFERENCES_FONT_SIZE_STEP);
		viewer.setFontSize(size);
	}
}
