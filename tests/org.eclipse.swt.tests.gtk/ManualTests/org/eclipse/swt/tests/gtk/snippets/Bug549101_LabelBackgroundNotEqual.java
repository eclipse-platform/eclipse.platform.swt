/*******************************************************************************
 * Copyright (c) 2019 Simeon Andreev and others. All rights reserved.
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class Bug549101_LabelBackgroundNotEqual {

	public static void main(String[] args) {
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(500, 80);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		shell.setText("Bug");

		Composite co = new Composite(shell, SWT.NONE);
		co.setLayout(new FillLayout(SWT.HORIZONTAL));
		Label l = new Label(co, SWT.NONE);
		l.setBackground(co.getBackground());

		boolean equalColors = l.getBackground().equals(co.getBackground());
		l.setText("colors equal: " + equalColors);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
