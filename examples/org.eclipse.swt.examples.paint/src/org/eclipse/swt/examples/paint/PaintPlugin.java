/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.paint;


import org.eclipse.core.runtime.*;
import org.eclipse.ui.plugin.*;

import java.text.*;
import java.util.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class PaintPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static PaintPlugin plugin;
	private static ResourceBundle resourceBundle;

	/**
	 * Constructs the Paint plugin.
	 */
	public PaintPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
		resourceBundle = descriptor.getResourceBundle();
	}
	
	/**
	 * Returns the shared instance.
	 */
	public static PaintPlugin getDefault() {
		return plugin;
	}

	/**
	 * Log an error to the ILog for this plugin
	 * 
	 * @param message the localized error message text
	 * @param exception the associated exception, or null
	 */
	public static void logError(String message, Throwable exception) {
		plugin.getLog().log(new Status(IStatus.ERROR, plugin.getDescriptor().getUniqueIdentifier(),
			0, message, exception));
	}

	/**
	 * Returns a string from the resource bundle.
	 * We don't want to crash because of a missing String.
	 * Returns the key if not found.
	 */
	public static String getResourceString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}			
	}

	/**
	 * Returns a string from the resource bundle and binds it
	 * with the given arguments. If the key is not found,
	 * return the key.
	 */
	public static String getResourceString(String key, Object[] args) {
		try {
			return MessageFormat.format(getResourceString(key), args);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}
	}
}
