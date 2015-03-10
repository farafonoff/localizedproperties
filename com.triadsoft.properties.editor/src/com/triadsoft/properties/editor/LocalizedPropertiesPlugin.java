package com.triadsoft.properties.editor;

import java.util.LinkedList;
import java.util.List;

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

	public static final String PROPERTIES_EDITOR_ID = "com.triadsoft.properties.editors.PropertiesEditor";

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
		// erase every single one wild card path
		int index = 0;
		while (store.contains(PreferenceConstants.WILDCARD_PATHS_PREFERENCES
				+ index)) {
			// Original solution, but just set default, I need to clean the
			// value
			// store.setToDefault(PreferenceConstants.WILDCARD_PATHS_PREFERENCES
			// + index);
			store.setValue(PreferenceConstants.WILDCARD_PATHS_PREFERENCES
					+ index, "");
			index++;
		}
		// persist new ones
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
				+ index)
				&& store.getString(
						PreferenceConstants.WILDCARD_PATHS_PREFERENCES + index)
						.length() > 0) {
			String value = store
					.getString(PreferenceConstants.WILDCARD_PATHS_PREFERENCES
							+ index);
			if (value != null && !value.equals("")) {
				wcs.add(store
						.getString(PreferenceConstants.WILDCARD_PATHS_PREFERENCES
								+ index));
			}
			index++;
		}
		return wcs.toArray(new String[wcs.size()]);
	}

	public static Character getDefaultSeparator() {
		return getDefault()
				.getPreferenceStore()
				.getString(
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
}