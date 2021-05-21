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
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * Description: After fixing the memory leak in bug 573697, we observed crashes
 * on shell re-parenting. In particular, {@link Decorations#fixAccelGroup()}
 * destroyed {@link Decorations#accelGroup} while still in use by GTK.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Run the snippet.</li>
 * <li>No JDK crash should occur.</li>
 * </ol>
 * </p>
 * Expected results: Standard out has text "No crash occurred.", the JDK did not crash.
 * Actual results: The JDK crashed in GTK+ method {@code g_type_check_instance()}.
 */
public class Bug573697_MenuBar_CrashShellReparent {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell1 = new Shell(display);
		shell1.setText("Bug 573697 crash example");
		shell1.setSize(300, 200);

		Label label1 = new Label(shell1, SWT.CENTER);
		label1.setBounds(shell1.getClientArea());

		Menu menu1 = new Menu(shell1, SWT.BAR);
		MenuItem item11 = new MenuItem(menu1, SWT.PUSH);
		item11.setText("&One\tCtrl+A");
		item11.setAccelerator(SWT.MOD1 + 'A');
		MenuItem item12 = new MenuItem(menu1, SWT.PUSH);
		item12.setText("&Two\tCtrl+C");
		item12.setAccelerator(SWT.MOD1 + 'C');
		shell1.setMenuBar(menu1);

		Shell shell2 = new Shell(display);
		shell2.setText("Shell 2");
		shell2.setSize(300, 200);

		Label label2 = new Label(shell2, SWT.CENTER);
		label2.setBounds(shell2.getClientArea());

		Menu menu2 = new Menu(shell2, SWT.BAR);
		MenuItem item21 = new MenuItem(menu2, SWT.PUSH);
		item21.setText("&Four\tCtrl+A");
		item21.setAccelerator(SWT.MOD1 + 'A');

		shell1.open();
		shell2.open();

		label1.setParent(shell2);

		while (display.readAndDispatch()) {
			// process UI events before disposing a shell
		}

		shell2.dispose();
		System.out.println("No crash occurred.");
		display.dispose();
	}
}
