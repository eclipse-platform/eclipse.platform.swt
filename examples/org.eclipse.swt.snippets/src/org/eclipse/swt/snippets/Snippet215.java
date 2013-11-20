/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.custom.*;

public class Snippet215 {

public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	Button button = new Button(shell, SWT.PUSH);
	button.setText("Capture");
	button.addListener(SWT.Selection, new Listener() {
		@Override
		public void handleEvent(Event event) {
			
			/* Take the screen shot */
			GC gc = new GC(display);
			final Image image = new Image(display, display.getBounds());
			gc.copyArea(image, 0, 0);
			gc.dispose();
			
			Shell popup = new Shell(shell, SWT.SHELL_TRIM);
			popup.setLayout(new FillLayout());
			popup.setText("Image");
			popup.setBounds(50, 50, 200, 200);
			popup.addListener(SWT.Close, new Listener() {
				@Override
				public void handleEvent(Event e) {
					image.dispose();
				}
			});
			
			ScrolledComposite sc = new ScrolledComposite (popup, SWT.V_SCROLL | SWT.H_SCROLL);
			Canvas canvas = new Canvas(sc, SWT.NONE);
			sc.setContent(canvas);
			canvas.setBounds(display.getBounds ());
			canvas.addPaintListener(new PaintListener() {
				@Override
				public void paintControl(PaintEvent e) {
					e.gc.drawImage(image, 0, 0);
				}
			});
			popup.open();
		}
	});
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}
