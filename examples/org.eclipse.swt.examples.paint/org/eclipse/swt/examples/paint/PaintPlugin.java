package org.eclipse.swt.examples.paint;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.core.runtime.IPluginDescriptor;import org.eclipse.ui.plugin.AbstractUIPlugin;import java.text.MessageFormat;import java.util.*;

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
