package org.eclipse.swt.examples.layoutexample;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import java.util.*;
import java.text.MessageFormat;

public class LayoutExample {
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("examples_layout");

	/**
	 * Creates an instance of a LayoutExample embedded inside
	 * the supplied parent Composite.
	 * 
	 * @param parent the container of the example
	 */
	public LayoutExample(Composite parent) {
		TabFolder tabFolder = new TabFolder (parent, SWT.NULL);
		Tab [] tabs = new Tab [] {
			new FillLayoutTab (this),
			new RowLayoutTab (this),
			new GridLayoutTab (this),
			new FormLayoutTab (this),
		};
		for (int i=0; i<tabs.length; i++) {
			TabItem item = new TabItem (tabFolder, SWT.NULL);
		    item.setText (tabs [i].getTabText ());
		    item.setControl (tabs [i].createTabFolderPage (tabFolder));
		}
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
