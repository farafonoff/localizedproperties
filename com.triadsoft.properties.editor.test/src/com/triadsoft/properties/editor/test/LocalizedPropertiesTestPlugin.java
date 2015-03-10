package com.triadsoft.properties.editor.test;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class LocalizedPropertiesTestPlugin extends AbstractUIPlugin {

	private static BundleContext context;
	private static LocalizedPropertiesTestPlugin plugin;
	public static final String PLUGIN_ID = "com.triadsoft.properties.test";

	static BundleContext getContext() {
		return context;
	}

	public static LocalizedPropertiesTestPlugin getDefault() {
		return plugin;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		LocalizedPropertiesTestPlugin.context = bundleContext;
		plugin = this;
		super.start(bundleContext);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		LocalizedPropertiesTestPlugin.context = null;
		plugin = null;
		super.stop(bundleContext);
	}
}
