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

import com.triadsoft.common.properties.PropertyEntry;
import com.triadsoft.common.properties.PropertyFile;
import com.triadsoft.properties.model.utils.PathDiscovery;

/**
 * 
 * @author Leonardo Flores
 */
public class ResourceList {

	// protected static final String FLEX_NATURE =
	// "com.adobe.flexbuilder.project.flexnature";
	// protected static final String AS_NATURE =
	// "com.adobe.flexbuilder.project.actionscriptnature";
	// protected static final String JAVA_NATURE =
	// "org.eclipse.jdt.core.javanature";

	private HashMap<Locale, PropertyFile> map = new HashMap<Locale, PropertyFile>();
	private Locale[] locales = new Locale[0];
	private String filename = null;
	private List<IResourceListener> listeners = new LinkedList<IResourceListener>();

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
			map.put(locale, new PropertyFile(ifile));
			locales.add(locale);
			System.out.println(ifile.getFullPath().toString() + " Locale: "
					+ locale.toString());
		}
		this.locales = (Locale[]) locales.toArray(new Locale[locales.size()]);
	}

	public void addResourceListener(IResourceListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
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
		for (Iterator<IResourceListener> iterator = listeners.iterator(); iterator
				.hasNext();) {
			IResourceListener type = (IResourceListener) iterator.next();
			type.entryModified(new Property(key, value), locale);
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
		PropertyEntry[] defaultEntries = defaultProperties.getEntries();
		for (int i = 0; i < defaultEntries.length; i++) {
			PropertyEntry secondEntry = (PropertyEntry) secondProperties
					.getPropertyEntry(defaultEntries[i].getKey());
			String value = "";
			String secondValue = "";

			if (defaultProperties != null) {
				value = defaultEntries[i].getValue();
			}
			if (secondProperties != null && secondEntry != null) {
				secondValue = secondEntry.getValue();
			}
			list.add(new Property(defaultEntries[i].getKey(), value,
					secondValue));
		}
		return list.toArray();
	}
}