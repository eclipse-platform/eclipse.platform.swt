/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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
package org.eclipse.swt.examples.layouts;

 
import org.eclipse.swt.examples.layoutexample.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.part.*;

/**
 * <code>Layouts</code> is a simple demonstration
 * of the layouts defined by SWT.  It consists of a workbench
 * view and tab folder where each tab in the folder allows the
 * user to interact with a layout.
 * 
 * @see ViewPart
 */
public class LayoutView extends ViewPart {
	LayoutExample instance = null;
	
	/**
	 * Create the example
	 * 
	 * @see ViewPart#createPartControl
	 */
	@Override
	public void createPartControl(Composite frame) {
		instance = new LayoutExample(frame);
	}

	/**
	 * Called when we must grab focus.
	 * 
	 * @see org.eclipse.ui.part.ViewPart#setFocus
	 */
	@Override
	public void setFocus() {
		instance.setFocus();
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
