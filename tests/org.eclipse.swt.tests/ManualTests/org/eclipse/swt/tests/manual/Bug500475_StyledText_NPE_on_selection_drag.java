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

package org.eclipse.swt.tests.manual;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Description: StyledText NPE on selection drag.
 * Steps to reproduce: Double click in the text but do not release the mouse button after second click. Drag around
 *     the text such that scrolling of the text widget does happen.
 * Expected results: No exceptions should be raised.
 * Actual results: Application dies because of NPE.
 */
public class Bug500475_StyledText_NPE_on_selection_drag {

	public static void main(String[] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setSize(200, 200);
		shell.setLayout(new FillLayout());

		StyledText text = new StyledText(shell, SWT.BORDER);
		text.setDoubleClickEnabled(false); // exception does happen only if this flag is 'false'
		text.setText("This is some dummy text. We need enough text to have the scroll bars appear.");

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
