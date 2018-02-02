/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
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
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


/**
 *
 To get this guy to work,
 1) Uncomment line with "UNCOMMENT THIS".
 2) Get hold of nebula rich text project. (found inside org.eclipse.nebula)
   edit .classpath and add:
 <classpathentry kind="src" path="/org.eclipse.nebula.widgets.richtext"/>
 (This can be done via auto fix.

 Before Fix: When running, error messages thrown into console.
 After fix: Snippet runs as expected.
 */
public class Bug518961_RTTest extends org.eclipse.swt.widgets.Shell {

	public Bug518961_RTTest(Display display) {
		super(display, SWT.SHELL_TRIM);
		createContents();
	}

	private void createContents() {
//		new RichTextEditor(this);                                         /// UNCOMMENT THIS.
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Bug518961_RTTest(display);
		shell.setSize(800, 800);
		shell.setLayout(new FillLayout());

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	@Override
	protected void checkSubclass() {
	}

}