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
package org.eclipse.swt.examples.controlexample;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


public class CustomControlExample extends ControlExample {

	/**
	 * Creates an instance of a CustomControlExample embedded
	 * inside the supplied parent Composite.
	 *
	 * @param parent the container of the example
	 */
	public CustomControlExample(Composite parent) {
		super (parent);
	}

	/**
	 * Answers the set of example Tabs
	 */
	@Override
	Tab[] createTabs() {
		return new Tab [] {
			new CComboTab (this),
			new CLabelTab (this),
			new CTabFolderTab (this),
			new SashFormTab (this),
			new StyledTextTab (this),
		};
	}

	/**
	 * Invokes as a standalone program.
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		CustomControlExample instance = new CustomControlExample(shell);
		shell.setText(getResourceString("custom.window.title"));
		setShellSize(instance, shell);
		shell.open();
		while (! shell.isDisposed()) {
			if (! display.readAndDispatch()) display.sleep();
		}
		instance.dispose();
	}
}
