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
 * use a menu item's armListener to update a status line.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet286 {

	public static void main(java.lang.String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());
		
		Canvas blankCanvas = new Canvas(shell, SWT.BORDER);
		blankCanvas.setLayoutData(new GridData(200, 200));
		final Label statusLine = new Label(shell, SWT.NONE);
		statusLine.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		Menu bar = new Menu (shell, SWT.BAR);
		shell.setMenuBar (bar);
		
		MenuItem menuItem = new MenuItem (bar, SWT.CASCADE);
		menuItem.setText ("Test");
		Menu menu = new Menu(bar);
		menuItem.setMenu (menu);
		
		for (int i = 0; i < 5; i++) {
			MenuItem item = new MenuItem (menu, SWT.PUSH);
			item.setText ("Item " + i);
			item.addArmListener(new ArmListener() {
				public void widgetArmed(ArmEvent e) {
					statusLine.setText(((MenuItem)e.getSource()).getText());
				}
			});
		}
		
		shell.pack();
		shell.open();
		
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}