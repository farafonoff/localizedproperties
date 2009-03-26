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
	// private Locale[] locales = new Locale[0];
	private Locale dl;
	private String filename = null;
	private List<IPropertyFileListener> listeners = new LinkedList<IPropertyFileListener>();

	public ResourceList(IFile file) {
		try {
			PathDiscovery pd = new PathDiscovery(file);
			dl = pd.getWildcardPath().getLocale();
			this.filename = pd.getWildcardPath().getFileName();
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
		return map.keySet().toArray(new Locale[map.keySet().size()]);
	}

	private void parseLocales(Map<Locale, IFile> files) throws IOException {
		for (Iterator<Locale> iterator = files.keySet().iterator(); iterator
				.hasNext();) {
			Locale locale = iterator.next();
			IFile ifile = (IFile) files.get(locale);
			PropertyFile pf = new PropertyFile(ifile);
			map.put(locale, pf);
		}
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
	public boolean changeValue(String key, String value, Locale locale) {
		PropertyFile properties = ((PropertyFile) map.get(locale));
		if (properties == null) {
			return false;
		}
		if (!properties.exist(key)) {
			addEntry(key, locale);
			// return true;
		}
		PropertyEntry entry = properties.getPropertyEntry(key);
		entry.setValue(value);
		return true;
	}

	public boolean addEntry(String key, Locale locale) {
		PropertyFile file = map.get(locale);
		PropertyEntry entry = new PropertyEntry(null, key, null);
		file.getDefaultCategory().addEntry(entry);
		for (Iterator<IPropertyFileListener> iterator = listeners.iterator(); iterator
				.hasNext();) {
			IPropertyFileListener type = (IPropertyFileListener) iterator
					.next();
			type.entryAdded(file.getDefaultCategory(), entry);
		}
		return true;
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

	public Object[] getProperties() {
		ArrayList<Property> list = new ArrayList<Property>();
		PropertyFile defaultProperties = ((PropertyFile) map.get(dl));
		String[] keys = defaultProperties.getKeys();
		for (int i = 0; i < keys.length; i++) {
			Property property = new Property(keys[i]);
			property.setValue(dl, defaultProperties.getPropertyEntry(keys[i])
					.getValue());

			for (Iterator<Locale> iter = map.keySet().iterator(); iter
					.hasNext();) {
				Locale loc = iter.next();
				if (dl.equals(loc)) {
					continue;
				}
				PropertyFile properties = ((PropertyFile) map.get(loc));
				if (!properties.exist(keys[i])) {
					property.addError(loc, new PropertyError(
							PropertyError.INVALID_KEY,
							"No se encontro la clave"));
				} else if (properties.getPropertyEntry(keys[i]).getValue() == null) {
					property.addError(loc, new PropertyError(
							PropertyError.VOID_VALUE,
							"No se encontro valor para"));
				} else {
					property.setValue(loc, properties.getPropertyEntry(keys[i])
							.getValue());
				}
			}
			list.add(property);
		}
		return list.toArray();
	}
}