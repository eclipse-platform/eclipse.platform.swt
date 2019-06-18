/*******************************************************************************
 * Copyright (c) 2019 Patrick Tasse and others. All rights reserved.
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
 *     Patrick Tasse - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import java.util.Arrays;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

public class Bug547227_GroupMemoryLeak {

	private static final int NUM_ITERATIONS = 20;
	private static final int NUM_WIDGETS = 400;

	public static void main(String[] args) {
		Display display = new Display();
		System.out.println("gtk version: " + System.getProperty("org.eclipse.swt.internal.gtk.version"));
		System.out.println();

		Function<Composite, Widget> compositeCreator = parent -> {
			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout());
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
			return composite;
		};
		Function<Composite, Widget> groupCreator = parent -> {
			Group group = new Group(parent, SWT.NONE);
			group.setLayout(new GridLayout());
			group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
			return group;
		};
		for (Function<Composite, Widget> creator : Arrays.asList(compositeCreator, groupCreator)) {
			for (int i = 1; i <= NUM_ITERATIONS; i++) {
				Shell shell = new Shell(display);
				shell.setLayout(new FillLayout());
				Composite composite = new Composite(shell, SWT.NONE);
				composite.setLayout(new GridLayout(40, false));
				Widget widget = null;
				for (int k = 0; k < NUM_WIDGETS; k++) {
					widget = creator.apply(composite);
				}
				shell.pack();
				shell.open();
				while (display.readAndDispatch()) {
				}
				display.sleep();
				long t1 = System.currentTimeMillis();
				System.out.print(String.format("shell.dispose() for %s with %d %s [%d]...\t", shell, NUM_WIDGETS, widget.getClass().getSimpleName(), i));
				shell.dispose();
				long t2 = System.currentTimeMillis();
				System.out.print(String.format("%4d ms ", (t2-t1)));
				for (int n = 0; n < (t2-t1); n+=10) {
					System.out.print("=");
				}
				System.out.println();
			}
			System.out.println();
		}
		display.dispose();
	}
}