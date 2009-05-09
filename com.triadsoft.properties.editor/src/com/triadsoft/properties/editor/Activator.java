package com.triadsoft.properties.editor;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.triadsoft.properties.preferences.PreferenceConstants;

/**
 * El Activator class controla el ciclo de vida del plugin
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 */
public class Activator extends AbstractUIPlugin {

	private static final String BUNDLE_NAME = "com.triadsoft.properties.editor.localizedProperties";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	// The plug-in ID
	public static final String PLUGIN_ID = "com.triadsoft.properties";

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
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
	public static Activator getDefault() {
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

	public static String[] getWildcardPaths() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		return store.getString(PreferenceConstants.WILDCARD_PATHS_PREFERENCES)
				.split("\\|");
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
