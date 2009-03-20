package com.triadsoft.properties.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import com.triadsoft.common.properties.IPropertyFileListener;
import com.triadsoft.common.properties.PropertyEntry;
import com.triadsoft.common.properties.PropertyFile;
import com.triadsoft.properties.model.utils.PathDiscovery;

/**
 * 
 * @author Leonardo Flores
 */
public class ResourceList {

	private HashMap<Locale, PropertyFile> map = new HashMap<Locale, PropertyFile>();
	private Locale[] locales = new Locale[0];
	private String filename = null;
	private List<IPropertyFileListener> listeners = new LinkedList<IPropertyFileListener>();

	public ResourceList(IFile file) {
		try {
			PathDiscovery pd = new PathDiscovery(file);
			parseLocales(pd.getResources());
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getFileName() {
		return filename;
	}

	/**
	 * Este metodo se encarga de devolver los Locales disponibles para los
	 * archivos de recursos
	 * 
	 * @return
	 */
	public Locale[] getLocales() {
		return locales;
	}

	private void parseLocales(Map<Locale, IFile> files) throws IOException {
		List<Locale> locales = new LinkedList<Locale>();
		for (Iterator<Locale> iterator = files.keySet().iterator(); iterator
				.hasNext();) {
			Locale locale = iterator.next();
			IFile ifile = (IFile) files.get(locale);
			PropertyFile pf = new PropertyFile(ifile);
			map.put(locale, pf);
			locales.add(locale);
		}
		this.locales = (Locale[]) locales.toArray(new Locale[locales.size()]);
	}

	/**
	 * Se encarga de actualizar el valor para la clave correspondiente al
	 * properties identificado por el locale
	 * 
	 * @param key
	 * @param value
	 * @param locale
	 * @return
	 */
	public boolean saveValue(String key, String value, Locale locale) {
		PropertyFile properties = ((PropertyFile) map.get(locale));
		if (properties == null) {
			return false;
		}
		if (!properties.exist(key)) {
			return false;
		}
		PropertyEntry entry = properties.getPropertyEntry(key);
		entry.setValue(value);
		for (Iterator<IPropertyFileListener> iterator = listeners.iterator(); iterator
				.hasNext();) {
			// IPropertyFileListener type = (IPropertyFileListener) iterator
			// .next();
			// type.entryModified(new Property(key, value), locale);
		}
		return true;
	}

	public void save() {
		for (int i = 0; i < locales.length; i++) {
			PropertyFile properties = (PropertyFile) map.get(locales[i]);
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

	public Object[] getProperties() {
		ArrayList<Property> list = new ArrayList<Property>();
		PropertyFile defaultProperties = ((PropertyFile) map.get(locales[0]));
		PropertyFile secondProperties = ((PropertyFile) map.get(locales[1]));
		String[] keys = defaultProperties.getKeys();
		for (int i = 0; i < keys.length; i++) {
			Property property = new Property(keys[i]);
			property.setValue(locales[0], defaultProperties.getPropertyEntry(
					keys[i]).getValue());
			if (!secondProperties.exist(keys[i])) {
				property.addError(locales[1], new PropertyError(
						PropertyError.INVALID_KEY, "No se encontro la clave"));
			} else if (secondProperties.getPropertyEntry(keys[i]).getValue() == null) {
				property.addError(locales[1], new PropertyError(
						PropertyError.VOID_VALUE, "No se encontro valor para"));
			} else {
				property.setValue(locales[1], secondProperties
						.getPropertyEntry(keys[i]).getValue());
			}
			list.add(property);
		}
		return list.toArray();
	}
}