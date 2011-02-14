/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
 * Link example snippet: create a link widget
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.1
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class Snippet182 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		Link link = new Link(shell, SWT.BORDER);
		link.setText("This a very simple <A>link</A> widget.");
		Rectangle clientArea = shell.getClientArea();
		link.setBounds(clientArea.x, clientArea.y, 140, 40);
		shell.pack ();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}