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
 * Description: In bug 573697, we observed a native memory leak cased by {@link Decorations#accelGroup}
 * not being unreferenced.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Run the snippet.</li>
 * <li>No native memory leak should be detected by e.g. {code valgrind} or {@code jemalloc}.</li>
 * </ol>
 * </p>
 * Expected results: No native memory leak should be detected by e.g. {code valgrind} or {@code jemalloc}.
 * Actual results: {@code jemalloc} reports a native memory leak, e.g.:
 * <pre>
 * [640 bytes leaked]
 * je_prof_backtrace (.../jemalloc/src/prof.c:636 (discriminator 2))
 * je_malloc_default (.../jemalloc.c:2289)
 * g_malloc (??:?)
 * g_slice_alloc (??:?)
 * g_slice_alloc0 (??:?)
 * g_type_create_instance (??:?)
 * g_object_unref (??:?)
 * g_object_new_with_properties (??:?)
 * g_object_new (??:?)
 * Java_org_eclipse_swt_internal_gtk_GTK_gtk_1accel_1group_1new (??:?)
 * </pre>
 */
public class Bug573697_MenuBar_MenuLeak {

	public static void main(String[] args) {
		int n = 5; // repeating to show a potential native memory leak grows
		for (int i = 0; i < n; ++i) {
			Display display = new Display();
			Shell shell1 = new Shell(display);
			shell1.setText("Bug 573697 leak example");
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
			display.dispose();
		}
	}
}
