package com.triadsoft.properties.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.triadsoft.properties.model.Property;

public class PropertiesLabelProvider implements ITableLabelProvider {

	ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(this
			.getClass(), "/icons/cancel.png");

	public Image getColumnImage(Object obj, int index) {
		if (imageDescriptor != null && index == 0) {
			//return imageDescriptor.createImage();
			return null;
		}
		return null;
	}

	public String getColumnText(Object obj, int index) {
		Property property = (Property) obj;
		if (index == 0) {
			return property.getKey();
		} else if (index == 1) {
			return property.getValue();
		} else if (index == 2) {
			return property.getSecondValue();
		}
		return "";
	}

	public void addListener(ILabelProviderListener arg0) {

	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	public void removeListener(ILabelProviderListener arg0) {

	}
}
