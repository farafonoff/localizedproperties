package com.triadsoft.properties.editors;

import java.util.Locale;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.MessageBox;

import com.triadsoft.common.properties.ILocalizedPropertyFileListener;
import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;
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
 * @author Triad (flores.leonardo@gmail.com)
 * @see PropertyTableViewer
 */
public class PropertyModifier implements ICellModifier {

	private static final String EDITOR_TABLE_MODIFY_KEY_NULLVALUE_TITLE = "editor.table.modifyKey.nullvalue.title";
	private static final String EDITOR_TABLE_MODIFY_KEY_NULLVALUE_MESSAGE = "editor.table.modifyKey.nullvalue.message";
	protected static final String EDITOR_TABLE_MODIFY_KEY_CONFIRM_TITLE = "editor.table.modifyKey.confirm.title";
	protected static final String EDITOR_TABLE_MODIFY_KEY_CONFIRM_MESSAGE = "editor.table.modifyKey.confirm.message";
	private ILocalizedPropertyFileListener listener = null;
	private Object item;
	private String editedProperty;

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
		item = obj;
		editedProperty = property;
		// if (property.equals(PropertiesEditor.KEY_COLUMN_ID)) {
		// return false;
		// }
		return true;
	}

	public Object getValue(Object obj, String property) {
		editedProperty = property;
		if (property.equals(PropertiesEditor.KEY_COLUMN_ID)) {
			return ((Property) obj).getKey();
		}
		Locale locale = StringUtils.getLocale(property);
		if (locale != null) {
			return ((Property) obj).getValue(locale);
		}
		return "<unknown>";
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
		Property properties = (Property) ((Item) obj).getData();
		if (PropertiesEditor.KEY_COLUMN_ID.equals(property) && value == null) {
			MessageBox messageBox = new MessageBox(editor.getEditorSite()
					.getShell(), SWT.OK | SWT.ICON_ERROR);
			messageBox.setMessage(LocalizedPropertiesPlugin
					.getString(EDITOR_TABLE_MODIFY_KEY_NULLVALUE_MESSAGE));
			messageBox.setText(LocalizedPropertiesPlugin
					.getString(EDITOR_TABLE_MODIFY_KEY_NULLVALUE_TITLE));
			messageBox.open();
			return;
		} else if (PropertiesEditor.KEY_COLUMN_ID.equals(property)
				&& !properties.getKey().equals((String) value)) {
			MessageBox messageBox = new MessageBox(editor.getEditorSite()
					.getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);
			messageBox.setMessage(LocalizedPropertiesPlugin.getString(
					EDITOR_TABLE_MODIFY_KEY_CONFIRM_MESSAGE,
					new String[] { properties.getKey().toString() }));
			messageBox.setText(LocalizedPropertiesPlugin.getString(
					EDITOR_TABLE_MODIFY_KEY_CONFIRM_TITLE,
					new String[] { properties.getKey().toString() }));
			if (messageBox.open() == SWT.YES) {
				editor.keyChanged(properties.getKey(), (String) value);
			}
			return;
		} else if (PropertiesEditor.KEY_COLUMN_ID.equals(property)) {
			return;
		}
		Locale locale = StringUtils.getLocale(property);
		if (!properties.getValue(locale).equals(value)) {
			editor.valueChanged(properties.getKey(), (String) value, locale);
		}
		item = null;
		editedProperty = null;
	}

	public void handledValue(String value) {
		if (item == null) {
			return;
		}
		if (editedProperty == null) {
			return;
		}
		modify(item, editedProperty, value);
	}
}
