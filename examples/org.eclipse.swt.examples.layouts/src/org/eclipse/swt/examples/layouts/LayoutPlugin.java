package org.eclipse.swt.examples.layouts;

/*
 * (c) Copyright IBM Corp. 2000, 2002.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.core.runtime.*;
import org.eclipse.ui.plugin.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class LayoutPlugin extends AbstractUIPlugin {
	/**
	 * The constructor.
	 */
	public LayoutPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
	}
	
	/**
	 * Clean up
	 */
	public void shutdown() throws CoreException {
		super.shutdown();
	}
}
