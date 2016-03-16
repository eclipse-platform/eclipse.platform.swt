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
 * Link example snippet: create a link widget and 
 * provide buttons to set the link color.
 * 
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.1
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet182 {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new RowLayout());
		
		Link link = new Link(shell, SWT.BORDER);
		link.setText("This a very simple <A>link</A> widget.");
		
		Button setButton = new Button(shell, SWT.PUSH);
		setButton.setText("Choose link color");
		setButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("default link color " + link.getLinkForeground());
				ColorDialog colorDialog = new ColorDialog(shell);
				RGB color = colorDialog.open();
				link.setLinkForeground(new Color(display, color));
				System.out.println("user selected link color " + link.getLinkForeground());
			}
		});
		
		Button resetButton = new Button(shell, SWT.PUSH);
		resetButton.setText("Reset link color");
		resetButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("link color reset to system default");
				link.setLinkForeground(null);
			}
		});
		
		shell.pack ();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}