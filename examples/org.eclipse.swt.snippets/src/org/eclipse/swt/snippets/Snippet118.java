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
 * Cursor example snippet: create a color cursor from an image file
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.0
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet118 {

public static void main (String [] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setSize(150, 150);
	final Cursor[] cursor = new Cursor[1];
	Button button = new Button(shell, SWT.PUSH);
	button.setText("Change cursor");
	Point size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	button.setSize(size);
	button.addListener(SWT.Selection, e -> {
		FileDialog dialog = new FileDialog(shell);
		dialog.setFilterExtensions(new String[] {"*.ico", "*.gif", "*.*"});
		String name = dialog.open();
		if (name == null) return;
		ImageData image = new ImageData(name);
		Cursor oldCursor = cursor[0];
		cursor[0] = new Cursor(display, image, 0, 0);
		shell.setCursor(cursor[0]);
		if (oldCursor != null) oldCursor.dispose();
	});
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	if (cursor[0] != null) cursor[0].dispose();
	display.dispose();
}
}
