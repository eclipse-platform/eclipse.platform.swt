/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
