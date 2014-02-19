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
package org.eclipse.swt.examples.browserexample;

import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.*;

/**
 * <code>BrowserView</code> is a simple demonstration
 * of the SWT Browser widget.  It consists of a workbench
 * view and tab folder where each tab in the folder allows the
 * user to interact with a control.
 * 
 * @see ViewPart
 */
public class BrowserView extends ViewPart {
	BrowserExample instance = null;
	
	/**
	 * Create the example
	 * 
	 * @see ViewPart#createPartControl
	 */
	@Override
	public void createPartControl(Composite frame) {
		instance = new BrowserExample(frame, true);
	}

	/**
	 * Called when we must grab focus.
	 * 
	 * @see org.eclipse.ui.part.ViewPart#setFocus
	 */
	@Override
	public void setFocus() {
		instance.focus();
	}

	/**
	 * Called when the View is to be disposed
	 */	
	@Override
	public void dispose() {
		instance.dispose();
		instance = null;
		super.dispose();
	}
}
