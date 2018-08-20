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
 * GC example snippet: take a screen shot with a GC
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet215 {

public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	Button button = new Button(shell, SWT.PUSH);
	button.setText("Capture");
	button.addListener(SWT.Selection, event -> {

		/* Take the screen shot */
		GC gc = new GC(display);
		final Image image = new Image(display, display.getBounds());
		gc.copyArea(image, 0, 0);
		gc.dispose();

		Shell popup = new Shell(shell, SWT.SHELL_TRIM);
		popup.setLayout(new FillLayout());
		popup.setText("Image");
		popup.setBounds(50, 50, 200, 200);
		popup.addListener(SWT.Close, e -> image.dispose());

		ScrolledComposite sc = new ScrolledComposite (popup, SWT.V_SCROLL | SWT.H_SCROLL);
		Canvas canvas = new Canvas(sc, SWT.NONE);
		sc.setContent(canvas);
		canvas.setBounds(display.getBounds ());
		canvas.addPaintListener(e -> e.gc.drawImage(image, 0, 0));
		popup.open();
	});
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}
