package com.triadsoft.properties.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.triadsoft.common.properties.IPropertyFile;
import com.triadsoft.common.properties.PropertyCategory;
import com.triadsoft.common.properties.PropertyEntry;
import com.triadsoft.common.properties.PropertyFile;
import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;
import com.triadsoft.properties.model.utils.LocalizedPropertiesLog;

/**
 * Esta es una clase que se encarga de controlar los recursos de propiedades y
 * se encarga del mantenimiento, eliminacion y guardado de los archivos de
 * pripiedades
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class ResourcesBag extends HashMap<Locale, PropertyFile> {

	private static final String ERROR_LOCALE_DOESNT_EXIST = "error.locale.doesnt.exist";
	private static final String ERROR_FILE_DOESN_EXIST = "error.file.doesn.exist";
	private static final String ERROR_KEY_MUST_NOT_BE_NULL = "error.key.null";
	private static final String ERROR_LOST_LOCALE = "error.locale.lost";
	private static final String ERROR_REPEATED_KEY = "error.key.repeated";

	/**
	 * 
	 */
	private static final long serialVersionUID = -8621871160250240736L;

	private HashSet<String> allKeys = new HashSet<String>();

	private Locale defaultLocale;

	public void setDefaultLocale(Locale locale) {
		this.defaultLocale = locale;
	}

	public boolean addLocalization(Locale locale, PropertyFile file) {
		if (!containsKey(locale)) {
			return false;
		}
		addKeys(file);
		put(locale, file);
		return true;
	}

	public boolean setResources(Map<Locale, IFile> resources)
			throws IOException, CoreException {
		this.clear();
		allKeys.clear();

		for (Iterator<Locale> iterator = resources.keySet().iterator(); iterator
				.hasNext();) {
			Locale locale = iterator.next();
			addResource(locale, resources.get(locale));
		}
		return true;
	}

	public boolean addResource(Locale locale, IFile file) throws IOException,
			CoreException {
		if (!file.exists()) {
			throw new IOException(LocalizedPropertiesPlugin.getString(
					ERROR_FILE_DOESN_EXIST, new String[] { file.getFullPath()
							.toString() }));
		}
		PropertyFile pf = new PropertyFile(file, LocalizedPropertiesPlugin
				.getKeyValueSeparators());
		put(locale, pf);
		return true;
	}

	public boolean addProperty(Property property) {
		String key = property.getKey();
		int index = 0;
		while (allKeys.contains(key)
				|| allKeys.contains(property.getKey() + index)) {
			key = property.getKey() + index;
			index++;
		}
		Locale[] locales = property.getLocales();
		for (int i = 0; i < locales.length; i++) {
			PropertyFile pf = get(locales[i]);
			if (pf == null) {
				continue;
			}
			String value = property.getValue(locales[i]);
			PropertyEntry entry = new PropertyEntry(null, key, value);
			pf.getDefaultCategory().addEntry(entry);
			try {
				pf.save();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean addKey(String newKey) {
		String key = newKey;
		int index = 0;
		while (allKeys.contains(key) || allKeys.contains(newKey + index)) {
			key = newKey + index;
			index++;
		}
		if (key == null) {
			throw new RuntimeException(LocalizedPropertiesPlugin
					.getString(ERROR_KEY_MUST_NOT_BE_NULL));
		}
		if (allKeys.contains(key)) {
			MessageBox messageBox = new MessageBox(LocalizedPropertiesPlugin
					.getDefault().getWorkbench().getActiveWorkbenchWindow()
					.getShell(), SWT.OK | SWT.ICON_ERROR);
			messageBox.setText(LocalizedPropertiesPlugin
					.getString(ERROR_REPEATED_KEY));
			messageBox.setMessage(LocalizedPropertiesPlugin
					.getString(ERROR_REPEATED_KEY));
			if (messageBox.open() == SWT.OK) {
				return false;
			}
			// throw new RuntimeException(LocalizedPropertiesPlugin
			// .getString(ERROR_REPEATED_KEY));
		}
		allKeys.add(key);
		for (Iterator<PropertyFile> iterator = values().iterator(); iterator
				.hasNext();) {
			PropertyFile pf = iterator.next();
			PropertyEntry entry = new PropertyEntry(null, key, "");
			pf.getDefaultCategory().addEntry(entry);
		}
		return true;
	}

	/**
	 * Metodo encargado de cargar todas las claves del property file un set que
	 * no repite las claves. Puede ocurrir que los distintos archivos tengan
	 * distintas claves, entonces de ésta manera cuando voy a armar la tabla,
	 * parto del listado de claves de la mezcla de todos los archivos
	 * 
	 * @param file
	 *            PropertyFile
	 */
	protected void addKeys(IPropertyFile file) {
		String[] keys = file.getKeys();
		for (int i = 0; i < keys.length; i++) {
			if (!allKeys.contains(keys[i])) {
				allKeys.add(keys[i]);
			}
		}
	}

	public PropertyFile put(Locale locale, PropertyFile pf) {
		addKeys(pf);
		return super.put(locale, pf);
	}

	public boolean removeLocale(Locale locale) throws CoreException {
		if (!keySet().contains(locale)) {
			throw new RuntimeException(LocalizedPropertiesPlugin.getString(
					ERROR_LOCALE_DOESNT_EXIST,
					new String[] { locale.toString() }));
		}
		PropertyFile pf = get(locale);
		remove(locale);
		if (pf != null) {
			IFile file = pf.getFile();
			if (file != null) {
				file.delete(true, null);
			}
		}
		return true;
	}

	public boolean update(Locale locale, IFile file) throws IOException,
			CoreException {
		if (get(locale) == null) {
			throw new RuntimeException(LocalizedPropertiesPlugin
					.getString(ERROR_LOST_LOCALE));
		}
		remove(locale);
		addResource(locale, file);
		return true;
	}

	public boolean removeKey(String key) {
		boolean isRemoved = false;
		if (key == null) {
			throw new RuntimeException("No se puede eliminar una clave nula");
		}
		if (!allKeys.contains(key)) {
			throw new RuntimeException("La clave no existe");
		}
		for (Iterator<PropertyFile> iterator = values().iterator(); iterator
				.hasNext();) {
			IPropertyFile pf = (IPropertyFile) iterator.next();
			PropertyEntry entry = pf.getPropertyEntry(key);
			((PropertyCategory) entry.getParent()).removeEntry(entry);
			isRemoved = true;
		}
		allKeys.remove(key);
		return isRemoved;
	}

	public void keyChanged(String oldKey, String key) {
		for (Iterator<PropertyFile> iterator = values().iterator(); iterator
				.hasNext();) {
			IPropertyFile pf = (IPropertyFile) iterator.next();
			PropertyEntry e = pf.getPropertyEntry(oldKey);
			e.setKey(key);
		}
		allKeys.remove(oldKey);
		allKeys.add(key);
	}

	public boolean changeValue(Locale locale, String key, String oldValue,
			String newValue) {
		boolean hasChanged = false;
		if (locale == null || !containsKey(locale)) {
			throw new RuntimeException("El locale no es valido");
		}
		if (newValue != null && !newValue.equals(oldValue)) {
			IPropertyFile pf = get(locale);
			PropertyEntry pe = pf.getPropertyEntry(key);
			pe.setValue(newValue);
			// try {
			// pf.getFile().getParent().refreshLocal(IFile.DEPTH_ONE, null);
			// } catch (CoreException e) {
			// LocalizedPropertiesLog.error(e.getMessage(), e);
			// }
			hasChanged = true;
		}
		return hasChanged;
	}

	public void save() {
		for (Iterator<Locale> iterator = keySet().iterator(); iterator
				.hasNext();) {
			Locale locale = iterator.next();
			IPropertyFile pf = (IPropertyFile) get(locale);
			try {
				pf.save();
			} catch (FileNotFoundException e) {
				LocalizedPropertiesLog.error(e.getLocalizedMessage(), e);
			} catch (IOException e) {
				LocalizedPropertiesLog.error(e.getLocalizedMessage(), e);
			} catch (CoreException e) {
				LocalizedPropertiesLog.error(e.getLocalizedMessage(), e);
			}
		}
	}

	public void dispose() {
		clear();
		allKeys.clear();
	}

	public Property[] getProperties() {
		ArrayList<Property> list = new ArrayList<Property>();
		PropertyFile defaultProperties = get(defaultLocale);
		for (Iterator<String> iter = allKeys.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			Property property = new Property(key);
			// Si no encuentra la entrada en el archivo por default
			// agrega la clave al archivo
			PropertyEntry defaultEntry = defaultProperties
					.getPropertyEntry(key);
			if (defaultEntry == null) {
				defaultEntry = new PropertyEntry(null, key, null);
				defaultProperties.getDefaultCategory().addEntry(defaultEntry);
			}
			property.setValue(defaultLocale, defaultEntry.getValue());

			for (Iterator<Locale> itera = keySet().iterator(); itera.hasNext();) {
				Locale loc = itera.next();
				if (defaultLocale.equals(loc)) {
					continue;
				}
				PropertyFile properties = ((PropertyFile) get(loc));
				PropertyEntry entry = properties.getPropertyEntry(key);
				if (entry == null) {
					entry = new PropertyEntry(null, key, null);
					properties.getDefaultCategory().addEntry(entry);
				}
				property.setValue(loc, entry.getValue());
			}
			list.add(property);
		}
		return list.toArray(new Property[list.size()]);
	}
}
