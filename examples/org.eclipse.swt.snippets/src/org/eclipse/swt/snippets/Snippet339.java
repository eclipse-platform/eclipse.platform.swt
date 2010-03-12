/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
 * CTabFolder example snippet: set a gradient on unselected tabs
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.6
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet339 {
	
public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	FillLayout fillLayout = new FillLayout ();
	fillLayout.marginWidth = 20;
	fillLayout.marginHeight = 20;
	shell.setLayout(fillLayout);
	
	CTabFolder folder = new CTabFolder(shell, SWT.BORDER);
	folder.setBackground(new Color[]{display.getSystemColor(SWT.COLOR_YELLOW), display.getSystemColor(SWT.COLOR_RED)}, new int[]{100}, true);
	
	for (int i = 0; i < 6; i++) {
		CTabItem item = new CTabItem(folder, SWT.CLOSE);
		item.setText("Item "+i);
		Text text = new Text(folder, SWT.MULTI);
		text.setText("Content for Item "+i +"\n\n\n\n");
		item.setControl(text);
	}
	
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
}
}
