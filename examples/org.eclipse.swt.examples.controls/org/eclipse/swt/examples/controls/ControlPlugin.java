package org.eclipse.swt.examples.controls;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.text.*;import java.util.*;import org.eclipse.core.runtime.*;import org.eclipse.swt.graphics.*;import org.eclipse.ui.plugin.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class ControlPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static ControlPlugin plugin;
	private static ResourceBundle resourceBundle;

	static final int
		ciClosedFolder = 0,
		ciOpenFolder = 1,
		ciTarget = 2;
	static final String[] imageLocations = {
		"closedFolder.gif",
		"openFolder.gif",
		"target.gif" };
	static Image images[];

	/**
	 * The constructor.
	 */
	public ControlPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		plugin = this;
		resourceBundle = descriptor.getResourceBundle();
	}
	
	/**
	 * Clean up
	 */
	public void shutdown() throws CoreException {
		super.shutdown();
		freeResources();
	}

	/**
	 * Returns the shared instance.
	 */
	public static ControlPlugin getDefault() {
		return plugin;
	}
	
	/**
	 * Gets a string from the resource bundle.
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
	 * Gets a string from the resource bundle and binds it
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
	 * Loads the resources
	 */
	public static void initResources() {
		Class clazz = ControlPlugin.class;
		try {
			if (images == null) {
				images = new Image[imageLocations.length];
				
				for (int i = 0; i < imageLocations.length; ++i) {
					ImageData source = new ImageData(clazz.getResourceAsStream(
						imageLocations[i]));
					ImageData mask = source.getTransparencyMask();
					images[i] = new Image(null, source, mask);
				}
			}	
		} catch (Throwable ex) {
			freeResources();
			logError(getResourceString("error.CouldNotLoadResources"), ex);
			throw new IllegalStateException();
		}
	}

	/**
	 * Frees the resources
	 */
	public static void freeResources() {
		if (images != null) {
			for (int i = 0; i < images.length; ++i) {
				final Image image = images[i];
				if (image != null) image.dispose();
			}
			images = null;
		}
	}
}
