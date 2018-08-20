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
 * Transform snippet: shear images
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet298 {

public static void main (String [] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.addListener(SWT.Paint, event -> {
		int[] icons = new int[]{SWT.ICON_ERROR, SWT.ICON_WARNING, SWT.ICON_INFORMATION, SWT.ICON_QUESTION, SWT.ICON_WORKING};
		int x = 10;
		for (int i = 0; i < icons.length; i++) {
			Image image = display.getSystemImage(icons[i]);
			if (image != null) {
				Transform t = new Transform(display);
				t.translate(x, 10);
				t.shear(1, 0);
				GC gc = event.gc;
				gc.setTransform(t);
				t.dispose();
				gc.drawImage(image, 0, 0);
				x += image.getBounds().width + 10;
			}
		}
	});
	shell.setSize(260, 100);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}
