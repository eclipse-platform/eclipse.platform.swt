/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.browser.demos;

import org.eclipse.ui.plugin.*;
import org.osgi.framework.BundleContext;
import java.util.*;
import org.eclipse.core.runtime.*;

public class BrowserDemoPlugin extends AbstractUIPlugin {

	public static BrowserDemoPlugin plugin;
	ResourceBundle resourceBundle;
	public static String PLUGIN_PATH = null;
	
	public BrowserDemoPlugin() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("org.eclipse.swt.examples.browser.demos.BrowserDemoPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		PLUGIN_PATH = Platform.resolve(plugin.getBundle().getEntry(".")).toString();
	}

	public void stop(BundleContext context) throws Exception {
		super.stop(context);
	}

	public static BrowserDemoPlugin getDefault() {
		return plugin;
	}

	public static String getResourceString(String key) {
		ResourceBundle bundle = BrowserDemoPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}	
}
