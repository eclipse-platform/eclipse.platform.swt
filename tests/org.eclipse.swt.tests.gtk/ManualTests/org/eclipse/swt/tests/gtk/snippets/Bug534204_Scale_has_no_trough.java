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
 * <li>Reduce the height of the snippet to its minimum by resizing. </li>
 * <li>Observe that the horizontal scales have no trough. </li>
 * <li>Resize the snippet back to its original state. </li>
 * <li>Reduce the width of the snippet to its minimum by resizing.</li>
 * <li>Observe that the vertical scales have no trough. </li>
 * </ol>
 * Expected results: The shell contains scales, each with a slider and a trough.
 * Actual results: After shrinking, the shell contains scales with no trough.
 */
public class Bug534204_Scale_has_no_trough {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(300, 300);
		shell.setText("Bug 534204 scale has no trough");
		shell.setLayout(new FillLayout(SWT.VERTICAL));

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout());

		Composite horizontal = new Composite(composite, SWT.NONE);
		horizontal.setLayout(new FillLayout(SWT.VERTICAL));
		new Scale(horizontal, SWT.HORIZONTAL);
		new Scale(horizontal, SWT.HORIZONTAL).setSelection(100);

		Composite vertical = new Composite(composite, SWT.NONE);
		vertical.setLayout(new FillLayout());
		new Scale(vertical, SWT.VERTICAL);
		new Scale(vertical, SWT.VERTICAL).setSelection(100);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
