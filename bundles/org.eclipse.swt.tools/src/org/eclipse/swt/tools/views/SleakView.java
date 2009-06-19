/*******************************************************************************
 * Copyright (c) 2004, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.views;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.tools.internal.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.internal.UIPlugin;
import org.eclipse.ui.part.*;

/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class SleakView extends ViewPart {

	Composite parent = null;
	Sleak sleak = null;

	/**
	 * The constructor.
	 */
	public SleakView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		this.parent = new Composite(parent, SWT.NONE);
		sleak = new Sleak ();
		sleak.create(this.parent);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		parent.setFocus();
	}
	
	private boolean extractOptions (String fileName) {
		FileOutputStream os = null;
		InputStream is = null;
		File file = new File(fileName);
		try {
			if (!file.exists ()) {
				is = Class.forName("org.eclipse.ui.internal.UIPlugin").getResourceAsStream ("/.options"); //$NON-NLS-1$
				if (is != null) {
					int read;
					byte [] buffer = new byte [4096];
					os = new FileOutputStream (fileName);
					while ((read = is.read (buffer)) != -1) {
						os.write(buffer, 0, read);
					}
					os.close ();
					is.close ();
				}
			}
			return true;
		} catch (Throwable e) {
		} finally {
			try {
				if (os != null)
					os.close();
			} catch (IOException e1) {
			}
			try {
				if (is != null)
					is.close();
			} catch (IOException e1) {
			}
		}
		return false;
	}

}