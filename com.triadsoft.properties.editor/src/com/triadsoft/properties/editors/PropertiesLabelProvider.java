package com.triadsoft.properties.editors;

import java.util.Locale;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.triadsoft.properties.model.Property;

public class PropertiesLabelProvider implements ITableLabelProvider {

	ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(this
			.getClass(), "/icons/8x8/warning.png");

	public Image getColumnImage(Object obj, int index) {
		Property property = (Property) obj;
		if (imageDescriptor != null && index == 0) {
			if (property.getErrors().size() > 0) {
				return imageDescriptor.createImage();
			}
		}
		return null;
	}

	public String getColumnText(Object obj, int index) {
		Property property = (Property) obj;
		if (index == 0) {
			return property.getKey();
		} else if (index == 1) {
			return property.getValue(new Locale("en", "US"));
		} else if (index == 2) {
			return property.getValue(new Locale("es", "AR"));
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
