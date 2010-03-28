package com.triadsoft.properties.model.utils;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.triadsoft.properties.editor.Activator;

/**
 * Clase que encapsula la funcionalidad de log de datos
 * 
 * @author Triad (flores.leonardo@triadsoft.com.ar)
 * 
 */
public class LocalizedPropertiesLog {
	private static ILog logger = Activator.getDefault().getLog();

	public void debug(String message, Throwable th) {
		IStatus status = new Status(IStatus.OK, Activator.PLUGIN_ID, message,
				th);
		logger.log(status);
	}

	public void debug(String message) {
		IStatus status = new Status(IStatus.OK, Activator.PLUGIN_ID, message,
				null);
		logger.log(status);
	}

	public void info(String message, Throwable th) {
		IStatus status = new Status(IStatus.INFO, Activator.PLUGIN_ID, message,
				th);
		logger.log(status);
	}

	public void info(String message) {
		IStatus status = new Status(IStatus.INFO, Activator.PLUGIN_ID, message,
				null);
		logger.log(status);
	}

	public void warning(String message, Throwable th) {
		IStatus status = new Status(IStatus.WARNING, Activator.PLUGIN_ID,
				message, th);
		logger.log(status);
	}

	public void warning(String message) {
		IStatus status = new Status(IStatus.WARNING, Activator.PLUGIN_ID,
				message, null);
		logger.log(status);
	}

	public void error(String message, Throwable th) {
		IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
				message, th);
		logger.log(status);
	}

	public void error(String message) {
		IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID,
				message, null);
		logger.log(status);
	}
}
