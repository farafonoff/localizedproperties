package com.triadsoft.properties.editors;

import java.util.Locale;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Item;

import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.ResourceList;

public class PropertyModifier implements ICellModifier {

	private Viewer viewer = null;

	public PropertyModifier(Viewer viewer) {
		this.viewer = viewer;
	}

	public boolean canModify(Object obj, String property) {
		if (property.equals(PropertiesEditor.KEY_COLUMN_ID)) {
			return false;
		}
		return true;
	}

	public Object getValue(Object obj, String property) {
		String[] loc = property.split("_");
		Locale locale = new Locale(loc[0], loc[1]);
		return ((Property) obj).getValue(locale);
	}

	public void modify(Object obj, String property, Object value) {
		if (!Item.class.isInstance(obj)) {
			return;
		}
		Item item = (Item) obj;
		if (!Property.class.isInstance(item.getData())) {
			return;
		}
		ResourceList resources = (ResourceList) viewer.getInput();
		String[] localeString = property.split("_");
		Locale locale = new Locale(localeString[0], localeString[1]);

		Property properties = (Property) ((Item) obj).getData();
		properties.setValue(locale, (String) value);
		resources.changeValue(properties.getKey(), properties.getValue(locale),
				locale);
		viewer.refresh();
	}
}
