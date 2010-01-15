package com.triadsoft.properties.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import com.triadsoft.common.properties.IPropertyFileListener;
import com.triadsoft.common.properties.PropertyCategory;
import com.triadsoft.common.properties.PropertyEntry;
import com.triadsoft.common.properties.PropertyFile;
import com.triadsoft.properties.editor.Activator;
import com.triadsoft.properties.model.utils.PathDiscovery;
import com.triadsoft.properties.model.utils.WildcardPath;

/**
 * Esta clase es la encargada de manejar el archivo que se intenta abrir desde
 * el editor. A partir del nombre del archivo abierto, ésta clase intenta
 * descubrir a partir de los WildcardPath mostrados en los defaults, la
 * ubicacion de los demas archivos de recursos parseando el path del archivo y
 * descubre el locale que contiene el path del archivo. Ésta clase basicamente
 * sirve de conexion entre el editor y el manejo de los archivos de propiedades
 * descubiertos en el path, los mantiene sincronizados, y cuando recibe un
 * cambio desde el editor, se ancarga de actualizar el archivo que corresponde
 * al idioma que se está modificando
 * 
 * @author Triad (flores.leonardo@gmail.com)
 * @see WildcardPath
 */
public class ResourceList {

	private HashMap<Locale, PropertyFile> map = new HashMap<Locale, PropertyFile>();
	private Locale defaultLocale;

	private String filename = null;

	private List<IPropertyFileListener> listeners = new LinkedList<IPropertyFileListener>();

	private HashSet<String> allKeys = new HashSet<String>();

	public ResourceList(IFile file) {
		try {
			PathDiscovery pd = new PathDiscovery(file);
			defaultLocale = pd.getDefaultLocale();
			this.filename = pd.getWildcardPath().getFileName();
			parseLocales(pd.getResources());
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public String getFileName() {
		return filename;
	}

	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	/**
	 * Este metodo se encarga de devolver los Locales disponibles para los
	 * archivos de recursos
	 * 
	 * @return Array de Locale
	 */
	public Locale[] getLocales() {
		return map.keySet().toArray(new Locale[map.keySet().size()]);
	}

	public void addListener(IPropertyFileListener listener) {
		listeners.add(listener);
	}

	private void parseLocales(Map<Locale, IFile> files) throws IOException,
			CoreException {
		Character separator = null;
		for (Iterator<Locale> iterator = files.keySet().iterator(); iterator
				.hasNext();) {
			Locale locale = iterator.next();
			IFile ifile = (IFile) files.get(locale);
			if (!ifile.exists()) {
				throw new IOException("No encontré el archivo "
						+ ifile.getFullPath());
			}
			PropertyFile pf;
			if (separator == null) {
				pf = new PropertyFile(ifile, Activator.getKeyValueSeparators());
				separator = pf.getSeparator();
			} else {
				pf = new PropertyFile(ifile, separator);
			}
			addKeys(pf);
			map.put(locale, pf);
		}
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
	private void addKeys(PropertyFile file) {
		String[] keys = file.getKeys();
		for (int i = 0; i < keys.length; i++) {
			allKeys.add(keys[i]);
		}
	}

	/**
	 * Se encarga de actualizar el valor para la clave correspondiente al
	 * properties identificado por el locale
	 * 
	 * @param key
	 * @param value
	 * @param locale
	 * @return Boolean que indica si se pudo cambiar el valor
	 */
	public boolean changeValue(String key, String value, Locale locale) {
		PropertyFile properties = ((PropertyFile) map.get(locale));
		if (properties == null) {
			return false;
		}
		PropertyEntry entry = properties.getPropertyEntry(key);
		entry.setValue(value);
		return true;
	}

	/**
	 * <p>
	 * Permite agregar una clave nueva, haciendolo para todos los resources
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public boolean addKey(String key) {
		allKeys.add(key);
		for (Iterator<PropertyFile> iterator = map.values().iterator(); iterator
				.hasNext();) {
			PropertyFile myFile = iterator.next();
			PropertyEntry entry = new PropertyEntry(null, key, "");
			myFile.getDefaultCategory().addEntry(entry);
		}
		return true;
	}

	public boolean removeKey(String key) {
		boolean isRemoved = false;

		for (Iterator<PropertyFile> iterator = map.values().iterator(); iterator
				.hasNext();) {
			PropertyFile file = (PropertyFile) iterator.next();
			PropertyEntry entry = file.getPropertyEntry(key);
			if (entry != null) {
				((PropertyCategory) entry.getParent()).removeEntry(entry);
				isRemoved = true;
			}
		}
		allKeys.remove(key);
		return isRemoved;
	}

	public void keyChanged(String oldKey, String key) {
		PropertyFile pf = map.get(defaultLocale);
		PropertyEntry e = pf.getPropertyEntry(oldKey);
		e.setKey(key);
		allKeys.remove(oldKey);
		allKeys.add(key);
	}

	public void save() {
		for (int i = 0; i < getLocales().length; i++) {
			PropertyFile properties = (PropertyFile) map.get(getLocales()[i]);
			try {
				properties.save();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Metodo que devuelve el listado de propiedades para
	 * 
	 * @return
	 */
	public Object[] getProperties() {
		ArrayList<Property> list = new ArrayList<Property>();
		PropertyFile defaultProperties = ((PropertyFile) map.get(defaultLocale));
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

			for (Iterator<Locale> itera = map.keySet().iterator(); itera
					.hasNext();) {
				Locale loc = itera.next();
				if (defaultLocale.equals(loc)) {
					continue;
				}
				PropertyFile properties = ((PropertyFile) map.get(loc));
				PropertyEntry entry = properties.getPropertyEntry(key);
				if (entry == null) {
					entry = new PropertyEntry(null, key, null);
					properties.getDefaultCategory().addEntry(entry);
				}
				property.setValue(loc, entry.getValue());
			}
			list.add(property);
		}
		return list.toArray();
	}
}