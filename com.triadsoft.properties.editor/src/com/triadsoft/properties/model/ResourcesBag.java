package com.triadsoft.properties.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.triadsoft.common.properties.IPropertyFile;
import com.triadsoft.common.utils.LocalizedPropertiesLog;
import com.triadsoft.common.utils.LocalizedPropertiesMessages;
import com.triadsoft.properties.editor.LocalizedPropertiesPlugin;

/**
 * Esta es una clase que se encarga de controlar los recursos de propiedades y
 * se encarga del mantenimiento, eliminacion y guardado de los archivos de
 * propiedades.
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class ResourcesBag extends HashMap<Locale, IPropertyFile> {

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

	public boolean addLocalization(Locale locale, PropertiesFile file) {
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
			throw new IOException(LocalizedPropertiesMessages.getString(
					ERROR_FILE_DOESN_EXIST, new String[] { file.getFullPath()
							.toString() }));
		}
		PropertiesFile pf = new PropertiesFile(file);
		put(locale, pf);
		return true;
	}

	/**
	 * Receive an property object and add the key to all locales
	 * 
	 * @param property
	 * @return
	 */
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
			IPropertyFile pf = get(locales[i]);
			if (pf == null) {
				continue;
			}
			String value = property.getValue(locales[i]);
			pf.setProperty(key, value);
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

	public String addKey(String key) {
		String newKey = getNewKey(key);
		if (newKey == null) {
			throw new RuntimeException(
					LocalizedPropertiesMessages
							.getString(ERROR_KEY_MUST_NOT_BE_NULL));
		}
		if (allKeys.contains(newKey)) {
			MessageBox messageBox = new MessageBox(LocalizedPropertiesPlugin
					.getDefault().getWorkbench().getActiveWorkbenchWindow()
					.getShell(), SWT.OK | SWT.ICON_ERROR);
			messageBox.setText(LocalizedPropertiesMessages
					.getString(ERROR_REPEATED_KEY));
			messageBox.setMessage(LocalizedPropertiesMessages
					.getString(ERROR_REPEATED_KEY));
			if (messageBox.open() == SWT.OK) {
				return key;
			}
			// throw new RuntimeException(LocalizedPropertiesPlugin
			// .getString(ERROR_REPEATED_KEY));
		}
		allKeys.add(newKey);
		for (Iterator<IPropertyFile> iterator = values().iterator(); iterator
				.hasNext();) {
			IPropertyFile pf = iterator.next();
			pf.setProperty(newKey, "");
		}
		return newKey;
	}

	/**
	 * The method receives a key and checks if the key exist. If the key exist,
	 * appends an incremental number until the key doesn't exists
	 * 
	 * @param key
	 * @return
	 */
	private String getNewKey(String key) {
		Pattern p = Pattern.compile("\\d{1,}");
		Matcher m = p.matcher(key);
		String num = "";
		String newKey = key;

		if (m.find()) {
			num = key.substring(m.start(), m.end());
			key = key.substring(0, m.start());
		}
		int index = num.length() > 0 ? new Integer(num) : 0;

		while (allKeys.contains(newKey) || allKeys.contains(newKey + index)) {
			newKey = key + index;
			index++;
		}
		return newKey;
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

	public IPropertyFile put(Locale locale, PropertiesFile pf) {
		addKeys(pf);
		return super.put(locale, pf);
	}

	public boolean removeLocale(Locale locale) throws CoreException {
		if (!keySet().contains(locale)) {
			throw new RuntimeException(LocalizedPropertiesMessages.getString(
					ERROR_LOCALE_DOESNT_EXIST,
					new String[] { locale.toString() }));
		}
		IPropertyFile pf = get(locale);
		remove(locale);
		if (pf != null) {
			IFile file = ((PropertiesFile) pf).getIFile();
			if (file != null) {
				file.delete(true, null);
			}
		}
		return true;
	}

	public boolean update(Locale locale, IFile file) throws IOException,
			CoreException {
		if (get(locale) == null) {
			throw new RuntimeException(
					LocalizedPropertiesMessages.getString(ERROR_LOST_LOCALE));
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
		for (Iterator<IPropertyFile> iterator = values().iterator(); iterator
				.hasNext();) {
			PropertiesFile pf = (PropertiesFile) iterator.next();
			pf.remove(key);
			isRemoved = true;
		}
		allKeys.remove(key);
		return isRemoved;
	}

	public String keyChanged(String oldKey, String key) {
		String newKey = getNewKey(key);
		for (Iterator<IPropertyFile> iterator = values().iterator(); iterator
				.hasNext();) {
			PropertiesFile pf = (PropertiesFile) iterator.next();
			String value = pf.getProperty(oldKey);
			pf.remove(oldKey);
			// addKey(newKey);
			pf.setProperty(newKey, value);
		}
		allKeys.remove(oldKey);
		allKeys.add(newKey);
		return newKey;
	}

	public boolean changeValue(Locale locale, String key, String oldValue,
			String newValue) {
		boolean hasChanged = false;
		if (locale == null || !containsKey(locale)) {
			throw new RuntimeException("El locale no es valido");
		}
		if (newValue != null && !newValue.equals(oldValue)) {
			PropertiesFile pf = (PropertiesFile) get(locale);
			pf.setProperty(key, newValue);
			// try {
			// pf.getIFile().getParent().refreshLocal(IFile.DEPTH_ONE, null);
			hasChanged = true;
			// } catch (CoreException e) {
			// LocalizedPropertiesLog.error(e.getMessage(), e);
			// }
		}
		return hasChanged;
	}

	/**
	 * Force to save files as unescaped code
	 */
	public void saveAsUnescapedUnicode() {
		this.save(false);
	}

	/**
	 * Force to save files as escaped code
	 */
	public void saveAsEscapedUnicode() {
		this.save(true);
	}

	/**
	 * This method keep the file style that was loaded from file
	 */
	public void save() {
		for (Iterator<Locale> iterator = keySet().iterator(); iterator
				.hasNext();) {
			Locale locale = iterator.next();
			PropertiesFile pf = (PropertiesFile) get(locale);
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
		refreshWorkspace();
	}

	private void save(boolean escapedUnicode) {
		for (Iterator<Locale> iterator = keySet().iterator(); iterator
				.hasNext();) {
			Locale locale = iterator.next();
			PropertiesFile pf = (PropertiesFile) get(locale);
			try {
				if (escapedUnicode) {
					pf.saveAsEscapedUnicode();
				} else {
					pf.saveAsUnescapedUnicode();
				}
			} catch (FileNotFoundException e) {
				LocalizedPropertiesLog.error(e.getLocalizedMessage(), e);
			} catch (IOException e) {
				LocalizedPropertiesLog.error(e.getLocalizedMessage(), e);
			} catch (CoreException e) {
				LocalizedPropertiesLog.error(e.getLocalizedMessage(), e);
			}
		}
		refreshWorkspace();
	}

	private void refreshWorkspace() {
		PropertiesFile pf = (PropertiesFile) get(defaultLocale);
		try {
			pf.getIFile().getParent().refreshLocal(IFile.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			LocalizedPropertiesLog.error(
					"Error actualizando el arbol de archivos", e);
		}
	}

	public void dispose() {
		clear();
		allKeys.clear();
	}

	public Property[] getProperties() {
		ArrayList<Property> list = new ArrayList<Property>();
		PropertiesFile defaultProperties = (PropertiesFile) get(defaultLocale);
		for (Iterator<String> iter = allKeys.iterator(); iter.hasNext();) {
			String key = (String) iter.next();
			Property property = new Property(key);
			// Si no encuentra la entrada en el archivo por default
			// agrega la clave al archivo
			if (defaultProperties.getProperty(key) == null) {
				defaultProperties.setProperty(key, "");
			}
			property.setValue(defaultLocale, defaultProperties.getProperty(key));

			for (Iterator<Locale> itera = keySet().iterator(); itera.hasNext();) {
				Locale loc = itera.next();
				if (defaultLocale.equals(loc)) {
					continue;
				}
				PropertiesFile properties = ((PropertiesFile) get(loc));
				if (properties.getProperty(key) == null) {
					properties.setProperty(key, "");
				}
				property.setValue(loc, properties.getProperty(key));
			}
			property.updateError();
			list.add(property);
		}
		return list.toArray(new Property[list.size()]);
	}
}
