/*******************************************************************************
 * Copyright (c) 2023 Mat Booth and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Mat Booth - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * Description: Double-free when setting the MenuItem accelerator can result in
 * a JVM crash.
 * <p>
 * How to run:
 * <ol>
 * <li>Set SWT_GTK4=1 in the environment to run on GTK4</li>
 * <li>Run the snippet</li>
 * <li>Click the menu items a few times and it will eventually crash with the
 * following segfault:</li>
 * <p>
 *
 * <pre>
 * Native frames: (J=compiled Java code, A=aot compiled Java code, j=interpreted, Vv=VM code, C=native code)
 * C  [libgtk-4.so.1+0x213053]  gtk_shortcut_controller_remove_shortcut+0x53
 *
 * Java frames: (J=compiled Java code, j=interpreted, Vv=VM code)
 * j  org.eclipse.swt.internal.gtk4.GTK4.gtk_shortcut_controller_remove_shortcut(JJ)V+0
 * j  org.eclipse.swt.widgets.MenuItem.setAccelerator(I)V+39
 * </pre>
 * </ol>
 *
 * Tested on GTK 4.6.8 (Fedora 36)
 */
public class Issue346_JvmCrashRemovingMenuItemAccelerators {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Snippet 29");
		Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		MenuItem fileItem = new MenuItem(bar, SWT.CASCADE);
		fileItem.setText("&File");
		Menu submenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(submenu);
		MenuItem addItem = new MenuItem(submenu, SWT.PUSH);
		MenuItem removeItem = new MenuItem(submenu, SWT.PUSH);
		addItem.addListener(SWT.Selection, e -> {
			System.out.println("Adding Accelerators");
			addItem.setText("&Add Accelerators\tCtrl+A");
			addItem.setAccelerator(SWT.MOD1 | 'A');
			removeItem.setText("&Remove Accelerators\tCtrl+R");
			removeItem.setAccelerator(SWT.MOD1 | 'R');
		});
		removeItem.addListener(SWT.Selection, e -> {
			System.out.println("Removing Accelerators");
			addItem.setText("&Add Accelerators");
			addItem.setAccelerator(0);
			removeItem.setText("&Remove Accelerators");
			removeItem.setAccelerator(0);
		});
		addItem.setText("&Add Accelerators\tCtrl+A");
		addItem.setAccelerator(SWT.MOD1 | 'A');
		removeItem.setText("&Remove Accelerators\tCtrl+R");
		removeItem.setAccelerator(SWT.MOD1 | 'R');
		shell.setSize(200, 200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
