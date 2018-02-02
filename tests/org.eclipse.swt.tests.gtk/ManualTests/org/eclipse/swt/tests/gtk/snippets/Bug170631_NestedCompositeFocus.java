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
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/*
 * Title: Bug 170631 - [Widgets] First nested composite takes focus unexpectedly
 * How to run: launch snippet and hit tab
 * Bug description: Text does not get focus
 * Expected results: Text should get focus and keep it.
 * GTK Version(s): GTK2.x
 */
public class Bug170631_NestedCompositeFocus {

public static void main(String [] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setLayout(new GridLayout());
	final Canvas canvas = new Canvas(shell, SWT.NONE);
	GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
	canvas.setLayoutData(gridData);
	canvas.addPaintListener(event -> {
		Rectangle rect = canvas.getClientArea();
		event.gc.drawOval(0, 0, rect.width - 1, rect.height - 1);
	});

	final Text text = new Text(shell, SWT.BORDER);
	text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	text.setText("I want focus");

	shell.setBounds(10, 10, 200, 200);

	shell.open ();

	// With shell.setFocus(), the Canvas gets focus.  Without it, the Text does.
	shell.setFocus();

	System.out.println(shell.getDisplay().getFocusControl());

	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}