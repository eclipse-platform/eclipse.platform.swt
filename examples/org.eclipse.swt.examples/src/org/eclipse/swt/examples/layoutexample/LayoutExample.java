/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
package org.eclipse.swt.examples.layoutexample;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

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
		for (Tab tab : tabs) {
			TabItem item = new TabItem (tabFolder, SWT.NONE);
		    item.setText (tab.getTabText ());
		    item.setControl (tab.createTabFolderPage (tabFolder));
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
		shell.addShellListener(ShellListener.shellClosedAdapter(e -> {
			Shell[] shells = display.getShells();
			for (Shell currentShell : shells) {
				if (currentShell != shell)
					currentShell.close();
			}
		}));
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
