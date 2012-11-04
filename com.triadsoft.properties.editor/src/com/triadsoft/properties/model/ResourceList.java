package com.triadsoft.properties.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;

import com.triadsoft.common.properties.IPropertyFile;
import com.triadsoft.common.utils.LocalizedPropertiesLog;
import com.triadsoft.common.utils.LocalizedPropertiesMessages;
import com.triadsoft.properties.model.utils.IFilesDiscoverer;
import com.triadsoft.properties.model.utils.IWildcardPath;
import com.triadsoft.properties.model.utils.NewPathDiscovery;
import com.triadsoft.properties.model.utils.WildCardPath2;
import com.triadsoft.properties.wizards.LocalizedPropertiesWizard;

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
 * @see WildCardPath2
 */
public class ResourceList {

	protected static final String PREFERENCES_ADD_LOCALE_ACTION_ERROR = "preferences.add.locale.action.error";

	private String filename = null;

	private IFilesDiscoverer pd;

	private ResourcesBag propertyFiles;

	public ResourceList(IFile file) {
		try {
			pd = new NewPathDiscovery(file);
			this.filename = pd.getWildcardPath().getFileName();

			propertyFiles = new ResourcesBag();
			propertyFiles.setDefaultLocale(pd.getDefaultLocale());
			propertyFiles.setResources(pd.getResources());
		} catch (NullPointerException e) {
			LocalizedPropertiesLog.error(e.getMessage(), e);
		} catch (IOException e) {
			LocalizedPropertiesLog.error(e.getMessage(), e);
		} catch (CoreException e) {
			LocalizedPropertiesLog.error(e.getMessage(), e);
		}
	}

	public String getFileName() {
		return filename;
	}

	public Locale getDefaultLocale() {
		return pd.getDefaultLocale();
	}

	/**
	 * Este metodo se encarga de devolver los Locales disponibles para los
	 * archivos de recursos
	 * 
	 * @return Array de Locale
	 */
	public Locale[] getLocales() {
		return propertyFiles.keySet().toArray(
				new Locale[propertyFiles.keySet().size()]);
	}

	public IPropertyFile getPropertyFile(Locale locale) {
		return propertyFiles.get(locale);
	}

	public void setPropertyFile(IPropertyFile pf, Locale locale) {
		propertyFiles.put(locale, pf);
	}

	public boolean existKey(String key) {
		return propertyFiles.containsKey(key);
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
		return propertyFiles.changeValue(locale, key, null, value);
	}

	/**
	 * <p>
	 * Permite agregar una clave nueva, haciendolo para todos los resources
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public String addKey(String key) {
		return propertyFiles.addKey(key);
	}

	public boolean removeKey(String key) {
		return propertyFiles.removeKey(key);
	}

	public boolean addProperty(Property property) {
		return propertyFiles.addProperty(property);
	}

	public String keyChanged(String oldKey, String key) {
		return propertyFiles.keyChanged(oldKey, key);
	}

	public void saveAsUnescapedUnicode() {
		propertyFiles.saveAsUnescapedUnicode();
	}

	public void saveAsEscapedUnicode() {
		propertyFiles.saveAsEscapedUnicode();
	}

	public void save() {
		propertyFiles.save();
		refreshSpace();
	}

	private void refreshSpace() {
		IContainer container = ((PropertiesFile) propertyFiles.get(pd
				.getDefaultLocale())).getIFile().getParent();
		if (pd.getWildcardPath().getRoot() == null) {
			container = ((PropertiesFile) propertyFiles.get(pd
					.getDefaultLocale())).getIFile().getProject();
		} else {
			while (!container.getName().equals(pd.getWildcardPath().getRoot())) {
				container = container.getParent();
			}
		}

		try {
			container.refreshLocal(IFile.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			LocalizedPropertiesLog.error(e.getMessage(), e);
		}
	}

	public void dispose() {
		propertyFiles.dispose();
	}

	public void removeLocale(Locale locale) throws CoreException {
		propertyFiles.removeLocale(locale);
	}

	public void addLocale(Locale locale) {
		IFile file = ((PropertiesFile) propertyFiles.get(this
				.getDefaultLocale())).getIFile();
		String newFilePath = pd.getWildcardPath().getFilePath(file, locale);

		final IFile newFile = file.getWorkspace().getRoot()
				.getFile(new Path(newFilePath));
		if (newFile.exists()) {
			MessageDialog.openError(PlatformUI.getWorkbench().getDisplay()
					.getActiveShell(), "Error", LocalizedPropertiesMessages
					.getString(PREFERENCES_ADD_LOCALE_ACTION_ERROR,
							new Object[] { locale }));
			return;
		}
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor)
					throws InvocationTargetException {
				try {
					LocalizedPropertiesWizard.createFullFilePath(newFile
							.getFullPath().removeLastSegments(1));
					newFile.create(openContentStream(), true, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};

		try {
			new ProgressMonitorDialog(PlatformUI.getWorkbench().getDisplay()
					.getActiveShell()).run(true, true, op);
		} catch (InvocationTargetException e) {
			LocalizedPropertiesLog.error(e.getLocalizedMessage(), e);
		} catch (InterruptedException e) {
			LocalizedPropertiesLog.error(e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Metodo que devuelve el listado de propiedades para
	 * 
	 * @return
	 */
	public Object[] getProperties() {
		return propertyFiles.getProperties();
	}

	private InputStream openContentStream() {
		String contents = "#Created By Localized Properties Plugin\n";
		return new ByteArrayInputStream(contents.getBytes());
	}

	public boolean resourceChanged(IResourceChangeEvent event) {
		IResourceDelta rootDelta = event.getDelta();
		// get the delta, if any, for the documentation directory
		if (rootDelta == null) {
			return false;
		}
		final Map<Locale, IFile> added = new HashMap<Locale, IFile>();
		final Map<Locale, IFile> deleted = new HashMap<Locale, IFile>();
		final Map<Locale, IFile> changed = new HashMap<Locale, IFile>();
		// changed.putAll(resources);
		IWildcardPath path = null;
		try {
			path = (IWildcardPath) pd.getWildcardPath().clone();
			path.setCountry(null);
			path.setLanguage(null);
			IResourceDeltaVisitor visitor = new ResourceChangeDeltaVisitor(
					added, deleted, changed, path, pd.getWildcardPath()
							.getFileName(), pd.getWildcardPath()
							.getFileExtension());
			rootDelta.accept(visitor);
		} catch (CoreException e) {
			LocalizedPropertiesLog.error("Error finding resources", e);
		} catch (CloneNotSupportedException e) {
			LocalizedPropertiesLog.error("Error cloning wildcardpath", e);
		}
		boolean returnValue = false;

		if (added.size() > 0) {
			for (Iterator<Locale> iterator = added.keySet().iterator(); iterator
					.hasNext();) {
				Locale locale = (Locale) iterator.next();
				try {
					propertyFiles.addResource(locale, added.get(locale));
				} catch (IOException e) {
					LocalizedPropertiesLog.error(e.getMessage(), e);
				} catch (CoreException e) {
					LocalizedPropertiesLog.error(e.getMessage(), e);
				}
			}
			returnValue = true;
		}
		if (deleted.size() > 0) {
			for (Iterator<Locale> iterator = deleted.keySet().iterator(); iterator
					.hasNext();) {
				Locale locale = (Locale) iterator.next();
				propertyFiles.remove(locale);
			}
			returnValue = true;
		}
		if (changed.size() > 0) {
			for (Iterator<Locale> iterator = changed.keySet().iterator(); iterator
					.hasNext();) {
				Locale locale = (Locale) iterator.next();
				try {
					propertyFiles.update(locale, changed.get(locale));
				} catch (CoreException e) {
					LocalizedPropertiesLog.error(e.getMessage(), e);
				} catch (IOException e) {
					LocalizedPropertiesLog.error(e.getMessage(), e);
				}
			}
			returnValue = true;
		}
		return returnValue;
	}
}