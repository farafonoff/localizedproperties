package com.triadsoft.properties.editors;

import java.util.Locale;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Image;

import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

/**
 * Provider para las columnas de PropertyTableViewer
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 * @see PropertyTableViewer
 * 
 */
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
		Property property = (Property) obj;
		if (index == 0) {
			return property.getKey();
		} else {
			return property.getValue(getLocale((String) viewer
					.getColumnProperties()[index]));
		}
	}

	private Locale getLocale(String localeString) {
		String[] loc = localeString.split("_");
		if (loc.length == 1) {
			return new Locale(loc[0]);
		}
		return new Locale(loc[0], loc[1]);
	}

	public void addListener(ILabelProviderListener arg0) {

	}

	public void dispose() {
	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
	 *      java.lang.String)
	 */
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	/**
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener arg0) {

	}
}
