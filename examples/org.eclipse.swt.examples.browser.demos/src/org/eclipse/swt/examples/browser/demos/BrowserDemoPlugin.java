/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.browser.demos;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class BrowserDemoPlugin extends AbstractUIPlugin {

	public static String PLUGIN_PATH = null;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		PLUGIN_PATH = FileLocator.toFileURL(context.getBundle().getEntry(".")).toString();
	}
}
