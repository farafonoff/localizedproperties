package com.triadsoft.properties.editors;

import java.util.Locale;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.Item;

import com.triadsoft.common.properties.ILocalizedPropertyFileListener;
import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.utils.PropertyTableViewer;

/**
 * Cell modifier para la celda de datos de la grilla PropertyTableViewer
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 * @see PropertyTableViewer
 */
public class PropertyModifier implements ICellModifier {

	private ILocalizedPropertyFileListener listener = null;

	public PropertyModifier(ILocalizedPropertyFileListener listener) {
		this.listener = listener;
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

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object,
	 *      java.lang.String, java.lang.Object)
	 */
	public void modify(Object obj, String property, Object value) {
		if (!Item.class.isInstance(obj)) {
			return;
		}
		Item item = (Item) obj;
		if (!Property.class.isInstance(item.getData())) {
			return;
		}
		PropertiesEditor editor = (PropertiesEditor) listener;
		String[] localeString = property.split("_");
		Locale locale = new Locale(localeString[0], localeString[1]);

		Property properties = (Property) ((Item) obj).getData();
		properties.setValue(locale, (String) value);

		editor.valueChanged(properties.getKey(), properties.getValue(locale),
				locale);
	}
}
