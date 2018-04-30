/*******************************************************************************
 * Copyright (c) 2018 Simeon Andreev and others. All rights reserved.
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;

/**
 * Description: Specifying a small height hint via {@link GridData} for a {@link Scale},
 *              or shrinking the parent of a {@link Scale} too much can result
 *              in a missing line of the scale.
 * Steps to reproduce:
 * <ol>
 * <li>Run the snippet.</li>
 * </ol>
 * Expected results: The shell contains a scale with a line and a trough.
 * Actual results: The shell contains a scale with a tough but without a line.
 */
public class Bug534204_Scale_has_no_line {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(300, 100);
		shell.setText("Bug 534204 scale has only trough and no line");
		shell.setLayout(new FillLayout(SWT.VERTICAL));

		Composite main = new Composite(shell, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		main.setLayout(gridLayout);

		Scale scale = new Scale(main, SWT.NONE);
		GridData gd = new GridData();
		gd.widthHint = 200;
		gd.heightHint = 18;
		scale.setLayoutData(gd);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
