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
 * example snippet: draw an X using AWT Graphics
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.0
 */
import java.awt.Frame;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Dimension;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.awt.SWT_AWT;

public class Snippet155 {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Composite composite = new Composite(shell, SWT.EMBEDDED);
		
		/* Draw an X using AWT */
		Frame frame = SWT_AWT.new_Frame(composite);
		Canvas canvas = new Canvas() {
			public void paint (Graphics g) {
				Dimension d = getSize();
				g.drawLine(0, 0, d.width, d.height);
				g.drawLine(d.width, 0, 0, d.height);
			}
		};
		frame.add(canvas);

		shell.open();
		while(!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
