package com.triadsoft.properties.editor;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.triadsoft.properties.preferences.PreferenceConstants;

/**
 * El Activator ha sido cambiado por esta que controla el ciclo de vida del
 * plugin
 * 
 * @author Triad (flores.leonardo@gmail.com)
 */
public class LocalizedPropertiesPlugin extends AbstractUIPlugin {

	private static final String BUNDLE_NAME = "com.triadsoft.properties.editor.localizedProperties";
	public static final String PROPERTIES_EDITOR_ID = "com.triadsoft.properties.editors.PropertiesEditor";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	public static final String PLUGIN_ID = "com.triadsoft.properties";

	private static LocalizedPropertiesPlugin plugin;

	/**
	 * The constructor
	 */
	public LocalizedPropertiesPlugin() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 *      )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static LocalizedPropertiesPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static void setWildcardPaths(String[] wps) {
		IPreferenceStore store = LocalizedPropertiesPlugin.getDefault()
				.getPreferenceStore();
		for (int i = 0; i < wps.length; i++) {
			store.setValue(PreferenceConstants.WILDCARD_PATHS_PREFERENCES + i,
					wps[i]);
		}
	}

	public static String getDefaultWildcardPath() {
		String[] wps = getWildcardPaths();
		IPreferenceStore store = LocalizedPropertiesPlugin.getDefault()
				.getPreferenceStore();
		int defaultIndex = store
				.getInt(PreferenceConstants.WILDCARD_PATH_DEFAULT_INDEX_PREFERENCES);
		if (defaultIndex < wps.length) {
			return wps[defaultIndex];
		}
		return wps[0];
	}

	public static String[] getWildcardPaths() {
		IPreferenceStore store = LocalizedPropertiesPlugin.getDefault()
				.getPreferenceStore();
		int index = 0;
		List<String> wcs = new LinkedList<String>();
		while (store.contains(PreferenceConstants.WILDCARD_PATHS_PREFERENCES
				+ index)) {
			wcs.add(store
					.getString(PreferenceConstants.WILDCARD_PATHS_PREFERENCES
							+ index));
			index++;
		}
		return wcs.toArray(new String[wcs.size()]);
	}

	public static Character getDefaultSeparator() {
		return getDefault().getPreferenceStore().getString(
				PreferenceConstants.KEY_VALUE_DEFAULT_SEPARATOR_PREFERENCES)
				.charAt(0);
	}

	public static String[] getKeyValueSeparators() {
		String separators = getDefault().getPreferenceStore().getString(
				PreferenceConstants.KEY_VALUE_SEPARATORS_PREFERENCES);
		List<String> sepList = new LinkedList<String>();
		for (int i = 0; i < separators.length(); i++) {
			sepList.add(separators.substring(i, i + 1));
		}
		return (String[]) sepList.toArray(new String[sepList.size()]);

	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	public static String getString(String key, Object[] params) {
		String resource = getString(key);
		String parametrized = resource;
		if (key.equals(resource)) {
			return key;
		}
		for (int i = 0; i < params.length; i++) {
			parametrized = resource.replaceAll("\\{" + i + "\\}", params[i]
					.toString());
		}
		return parametrized;
	}
}