package com.triadsoft.properties.utils;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.triadsoft.properties.editor.test.LocalizedPropertiesTestPlugin;

/**
 * Clase que encapsula la funcionalidad de log de datos
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 * 
 */
public class LocalizedPropertiesLog {
	public static void debug(String message, Throwable th) {
		IStatus status = new Status(IStatus.OK,
				LocalizedPropertiesTestPlugin.PLUGIN_ID, message, th);
		log(status);
	}

	public static void debug(String message) {
		IStatus status = new Status(IStatus.OK,
				LocalizedPropertiesTestPlugin.PLUGIN_ID, message, null);
		log(status);
	}

	public static void info(String message, Throwable th) {
		IStatus status = new Status(IStatus.INFO,
				LocalizedPropertiesTestPlugin.PLUGIN_ID, message, th);
		log(status);
	}

	public static void info(String message) {
		IStatus status = new Status(IStatus.INFO,
				LocalizedPropertiesTestPlugin.PLUGIN_ID, message, null);
		log(status);
	}

	public static void warning(String message, Throwable th) {
		IStatus status = new Status(IStatus.WARNING,
				LocalizedPropertiesTestPlugin.PLUGIN_ID, message, th);
		log(status);
	}

	public static void warning(String message) {
		IStatus status = new Status(IStatus.WARNING,
				LocalizedPropertiesTestPlugin.PLUGIN_ID, message, null);
		log(status);
	}

	public static void error(String message, Throwable th) {
		IStatus status = new Status(IStatus.ERROR,
				LocalizedPropertiesTestPlugin.PLUGIN_ID, message, th);
		log(status);
	}

	public static void error(String message) {
		IStatus status = new Status(IStatus.ERROR,
				LocalizedPropertiesTestPlugin.PLUGIN_ID, message, null);
		log(status);
	}

	public static void log(IStatus status) {
		LocalizedPropertiesTestPlugin.getDefault().getLog().log(status);
	}
}
