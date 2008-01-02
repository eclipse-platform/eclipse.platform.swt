/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.layoutexample;


import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

import java.text.*;
import java.util.*;

public class LayoutExample {
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("examples_layout");
	private TabFolder tabFolder;
	
	/**
	 * Creates an instance of a LayoutExample embedded inside
	 * the supplied parent Composite.
	 * 
	 * @param parent the container of the example
	 */
	public LayoutExample(Composite parent) {
		tabFolder = new TabFolder (parent, SWT.NONE);
		Tab [] tabs = new Tab [] {
			new FillLayoutTab (this),
			new RowLayoutTab (this),
			new GridLayoutTab (this),
			new FormLayoutTab (this),
			new StackLayoutTab (this)
		};
		for (int i=0; i<tabs.length; i++) {
			TabItem item = new TabItem (tabFolder, SWT.NONE);
		    item.setText (tabs [i].getTabText ());
		    item.setControl (tabs [i].createTabFolderPage (tabFolder));
		}
	}
	
	/**
	 * Grabs input focus.
	 */
	public void setFocus() {
		tabFolder.setFocus();
	}
	
	/**
	 * Disposes of all resources associated with a particular
	 * instance of the LayoutExample.
	 */	
	public void dispose() {
		tabFolder = null;
	}
	
	/**
	 * Invokes as a standalone program.
	 */
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		new LayoutExample(shell);
		shell.setText(getResourceString("window.title"));
		shell.addShellListener (new ShellAdapter () {
			public void shellClosed(ShellEvent e) {
				Shell [] shells = display.getShells();
				for (int i = 0; i < shells.length; i++) {
					if (shells [i] != shell) shells [i].close ();
				}
			}
		});
		shell.open();
		while (! shell.isDisposed()) {
			if (! display.readAndDispatch()) display.sleep();
		}
	}

	/**
	 * Gets a string from the resource bundle.
	 * We don't want to crash because of a missing String.
	 * Returns the key if not found.
	 */
	static String getResourceString(String key) {
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
	static String getResourceString(String key, Object[] args) {
		try {
			return MessageFormat.format(getResourceString(key), args);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}
	}
}
