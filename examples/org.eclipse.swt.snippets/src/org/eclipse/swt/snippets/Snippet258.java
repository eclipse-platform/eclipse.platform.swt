/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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
 * Create a search text control
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.3
 */ 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

public class Snippet258 {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(2, false));
		
		final Text text = new Text(shell, SWT.SEARCH | SWT.ICON_CANCEL);
		Image image = null;
		if ((text.getStyle() & SWT.ICON_CANCEL) == 0) {
			image = display.getSystemImage(SWT.ICON_ERROR);
			ToolBar toolBar = new ToolBar (shell, SWT.FLAT);
			ToolItem item = new ToolItem (toolBar, SWT.PUSH);
			item.setImage (image);
			item.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					text.setText("");
					System.out.println("Search cancelled");
				}
			});
		}
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setText("Search text");
		text.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				if (e.detail == SWT.CANCEL) {
					System.out.println("Search cancelled");
				} else {
					System.out.println("Searching for: " + text.getText() + "...");
				}
			}
		});

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		if (image != null) image.dispose();
		display.dispose();
	}
}