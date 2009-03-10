package com.triadsoft.properties.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.triadsoft.common.properties.PropertyEntry;
import com.triadsoft.common.properties.PropertyFile;
import com.triadsoft.properties.model.utils.PathDiscovery;

/**
 * Natures para Java org.eclipse.jdt.core.javanature
 * 
 * Natures java web org.eclipse.wst.common.project.facet.core.nature
 * org.eclipse.wst.common.modulecore.ModuleCoreNature
 * 
 * Nature java plugin org.eclipse.pde.PluginNature
 * 
 * Natures para flex com.adobe.flexbuilder.project.flexnature
 * com.adobe.flexbuilder.project.actionscriptnature
 * 
 * @author Leonardo Flores
 * 
 */
public class ResourceList {

	protected static final String FLEX_NATURE = "com.adobe.flexbuilder.project.flexnature";
	protected static final String AS_NATURE = "com.adobe.flexbuilder.project.actionscriptnature";
	protected static final String JAVA_NATURE = "org.eclipse.jdt.core.javanature";

	private HashMap<Locale, PropertyFile> map = new HashMap<Locale, PropertyFile>();
	private IPath localePath;
	private IProject project = null;
	private Locale[] locales = new Locale[0];
	private String filename = null;
	private IResourceLocator locator = null;
	private List<IResourceListener> listeners = new LinkedList<IResourceListener>();

	public ResourceList(IFile file) {
		try {
			project = file.getProject();
			PathDiscovery pd = new PathDiscovery(file);
			locator = getResourceLocatorByNature(project);
			localePath = locator.getLocalePath(file);
			filename = locator.getFileName(file, new Locale("en", "US"));
			parseLocales(file);
			Locale[] locales = getLocales();
			for (int i = 0; i < locales.length; i++) {
				loadByLocale(locator.getFileName(file, locales[i]), locales[i]);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public String getFileName() {
		return filename;
	}

	/**
	 * Este metodo se encarga de cargar los archivos de recursos segun su
	 * locale.
	 * 
	 * @param filename
	 *            Nombre del archivo, se debe usar el devuelto por el locator
	 *            para abstraer el nombre del archivo de la forma en que se
	 *            almacenan los recursos
	 * @param locale
	 *            Locale para el cual se buscará el archivo
	 */
	private void loadByLocale(String filename, Locale locale) {
		try {
			String localeDir = locator.getLocaleDir(locale);
			IPath filePath = localePath.append(new Path("/" + localeDir + "/"
					+ filename));
			IFile file = project.getFile(filePath.removeFirstSegments(1));
			if (!file.isSynchronized(IFile.DEPTH_ONE)) {
				file.refreshLocal(IFile.DEPTH_ONE, null);
			}
			map.put(locale, new PropertyFile(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
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

	/**
	 * Metodo que a partir del IFile y del locator "descubre" cuales son los
	 * locales disponibles.Por el momento estan hardcore los locales de en_US y
	 * es_AR, pero la idea es que se obtengan los locales disponibles en el path
	 * de los archivos de recursos
	 * 
	 * @param file
	 */
	private void parseLocales(IFile file) {
		LinkedList<Locale> locales = new LinkedList<Locale>();
		locales.add(new Locale("en", "US"));
		locales.add(new Locale("es", "AR"));
		this.locales = (Locale[]) locales.toArray(new Locale[locales.size()]);
	}

	private static IResourceLocator getResourceLocatorByNature(IProject project)
			throws CoreException {
		if (project.getNature(FLEX_NATURE) != null) {
			return new FlexLocator();
		} else if (project.getNature(JAVA_NATURE) != null) {
			return new JavaLocator();
		}
		return new FlexLocator();
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
			if (secondProperties != null) {
				secondValue = secondEntry.getValue();
			}
			list.add(new Property(defaultEntries[i].getKey(), value,
					secondValue));
		}
		return list.toArray();
	}
}