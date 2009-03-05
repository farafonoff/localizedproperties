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
		if (property.equals(PropertiesEditor.EN_US_COLUMN_ID)) {
			return ((Property) obj).getValue();
		} else if (property.equals(PropertiesEditor.ES_AR_COLUMN_ID)) {
			return ((Property) obj).getSecondValue();
		}
		return null;
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
		if (property.equals(PropertiesEditor.EN_US_COLUMN_ID)) {
			properties.setValue((String) value);
			resources.saveValue(properties.getKey(), properties.getValue(),
					locale);
		} else if (property.equals(PropertiesEditor.ES_AR_COLUMN_ID)) {
			properties.setSecondValue((String) value);
			resources.saveValue(properties.getKey(), properties
					.getSecondValue(), locale);
		}
		viewer.refresh();
	}
}
