package org.eclipse.swt.examples.controls;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.core.runtime.*;
import org.eclipse.ui.plugin.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class ControlPlugin extends AbstractUIPlugin {
	/**
	 * The constructor.
	 */
	public ControlPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
	}
	
	/**
	 * Clean up
	 */
	public void shutdown() throws CoreException {
		super.shutdown();
	}
}
