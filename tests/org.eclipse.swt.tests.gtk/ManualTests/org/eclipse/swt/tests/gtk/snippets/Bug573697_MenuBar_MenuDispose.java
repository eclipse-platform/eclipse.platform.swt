/*******************************************************************************
 * Copyright (c) 2021 Simeon Andreev and others. All rights reserved.
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
 *     Simeon Andreev - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * Description: After fixing the memory leak in bug 573697, we observed further menu problems
 * In particular, {@link Shell#setMenuBar(Menu)} called with a {@code null}
 * and {@link Menu#dispose()} would cause a crash or {@code GLib CRITICAL} errors on
 * standard error stream.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Run the snippet.</li>
 * <li>No JDK crash should occur, no {@code GLib CRITICAL} errors should be printed on standard error stream.</li>
 * </ol>
 * </p>
 * Expected results: Standard error has no {@code GLib CRITICAL} errors, the JDK did not crash.
 * Actual results: The JDK either crashed or {@code GLib CRITICAL} errors are printed on standard error stream.
 */
public class Bug573697_MenuBar_MenuDispose {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Bug 573697 critical errors example");
		shell.setSize(300, 200);

		Menu menu = new Menu(shell, SWT.BAR);
		MenuItem item1 = new MenuItem(menu, SWT.PUSH);
		item1.setText("&One\tCtrl+A");
		item1.setAccelerator(SWT.MOD1 + 'A');
		MenuItem item2 = new MenuItem(menu, SWT.PUSH);
		item2.setText("&Two\tCtrl+C");
		item2.setAccelerator(SWT.MOD1 + 'C');
		shell.setMenuBar(menu);


		shell.open();
		shell.setMenuBar(null);
		menu.dispose();

		while (display.readAndDispatch()) {
			// process UI events before disposing
		}

		System.out.println("No GLib CRITICAL errors should have been printed.");
		display.dispose();
	}
}
