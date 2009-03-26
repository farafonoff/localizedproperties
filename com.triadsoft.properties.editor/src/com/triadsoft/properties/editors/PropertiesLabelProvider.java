package com.triadsoft.properties.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;

import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.ResourceList;

public class PropertiesLabelProvider implements ITableLabelProvider {

	ImageDescriptor imageDescriptor = ImageDescriptor.createFromFile(this
			.getClass(), "/icons/8x8/warning.png");

	private TableViewer viewer;

	public PropertiesLabelProvider(TableViewer viewer) {
		this.viewer = viewer;
	}

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
		ResourceList rl = (ResourceList) viewer.getInput();
		Property property = (Property) obj;
		if (index == 0) {
			return property.getKey();
		} else {
			return property.getValue(rl.getLocales()[index-1]);
		}
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
