/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Canvas snippet: paint a circle in a canvas
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet245 {

public static void main(String [] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.addPaintListener(event -> {
		Rectangle rect = shell.getClientArea();
		event.gc.drawOval(0, 0, rect.width - 1, rect.height - 1);
	});
	Rectangle clientArea = shell.getClientArea();
	shell.setBounds(clientArea.x + 10, clientArea.y + 10, 200, 200);
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}
