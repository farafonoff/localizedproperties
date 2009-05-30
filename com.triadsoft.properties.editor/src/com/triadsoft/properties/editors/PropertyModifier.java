package com.triadsoft.properties.editors;

import java.util.Locale;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.Item;

import com.triadsoft.common.properties.ILocalizedPropertyFileListener;
import com.triadsoft.properties.model.Property;
import com.triadsoft.properties.model.utils.PropertyTableViewer;
import com.triadsoft.properties.model.utils.StringUtils;

/**
 * <p>
 * Cell modifier para la celda de datos de la grilla PropertyTableViewer.
 * </p>
 * <p>
 * Permite controlar los valores de la celda que se va a editar.
 * </p>
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 * @see PropertyTableViewer
 */
public class PropertyModifier implements ICellModifier {

	private ILocalizedPropertyFileListener listener = null;

	public PropertyModifier(ILocalizedPropertyFileListener listener) {
		this.listener = listener;
	}

	/**
	 * Permite saber si la columna es modificable, para la columna 0 devuelve
	 * false, que es la columna de la clave. Para los demas devuelve true
	 * 
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object,
	 *      java.lang.String)
	 */
	public boolean canModify(Object obj, String property) {
		if (property.equals(PropertiesEditor.KEY_COLUMN_ID)) {
			return false;
		}
		return true;
	}

	public Object getValue(Object obj, String property) {
		String[] loc = property.split("_");
		if (loc[0] != null && loc[0].length() > 0) {
			Locale locale = new Locale(loc[0], loc[1]);
			return ((Property) obj).getValue(locale);
		}
		return "";
	}

	/**
	 * Metodo encargado de hacer la modificacion real del valor
	 * 
	 * @param obj
	 *            Objeto que se esta modificando
	 * @param property
	 *            Indica la propiedad del objeto que se está modificando
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
		Locale locale = StringUtils.getLocale(property);

		Property properties = (Property) ((Item) obj).getData();
		properties.setValue(locale, (String) value);

		editor.valueChanged(properties.getKey(), properties.getValue(locale),
				locale);
	}
}
