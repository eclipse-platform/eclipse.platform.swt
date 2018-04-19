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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Description: Drawing an image with a paint listener of a {@link Group} yields wrong results,
 *              namely the drawing is done in the group area and not in client area of the group.
 *              This causes problems e.g. with JFace ControlDecoration.
 * Steps to reproduce:
 * <ol>
 * <li>Run the snippet.</li>
 * <li>Resize the shell to its minimum height.</li>
 * </ol>
 * Expected results: The shell contains a barely visible table, the standard out and error are empty.
 * Actual results: The standard error contains messages that indicate "invalid rectangle passed".
 */
public class Bug533799_shrinking_table_too_much_causes_pixman_errors_on_standard_error {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.setSize(300, 100);
		shell.setText("Bug 533799 errors on standard error");

		Table table = new Table(shell, SWT.BORDER);
		table.setHeaderVisible(true);

		for (int i = 0; i < 2; ++i) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText("column " + i);
			column.setWidth(150);
		}

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
