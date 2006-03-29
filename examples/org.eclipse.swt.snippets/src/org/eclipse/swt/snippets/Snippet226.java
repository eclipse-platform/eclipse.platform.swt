/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
 * Tree example snippet: Draw a custom gradient selection
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.2
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.*;

public class Snippet226 {
	
public static void main(String [] args) {
	final Display display = new Display();
	Shell shell = new Shell(display);
	shell.setText("Custom Selection - Gradient selection");
	shell.setLayout(new FillLayout());
	final Tree tree = new Tree(shell, SWT.MULTI | SWT.FULL_SELECTION);
	tree.setHeaderVisible(true);
	tree.setLinesVisible(true);
	int columnCount = 4;
	for (int i=0; i<columnCount; i++) {
		TreeColumn column = new TreeColumn(tree, SWT.NONE);
		column.setText("Col: " + i);	
	}
	int itemCount = 3;
	for (int i=0; i<itemCount; i++) {
		TreeItem item1 = new TreeItem(tree, SWT.NONE);
		item1.setText("item "+i);
		for (int c=1; c < columnCount; c++) {
			item1.setText(c, "item ["+i+"-"+c+"]");
		}
		for (int j=0; j<itemCount; j++) {
			TreeItem item2 = new TreeItem(item1, SWT.NONE);
			item2.setText("item ["+i+" "+j+"]");
			for (int c=1; c<columnCount; c++) {
				item2.setText(c, "item ["+i+" "+j+"-"+c+"]");
			}
			for (int k=0; k<itemCount; k++) {
				TreeItem item3 = new TreeItem(item2, SWT.NONE);
				item3.setText("item ["+i+" "+j+" "+k+"]");
				for (int c=1; c<columnCount; c++) {
					item3.setText(c, "item ["+i+" "+j+" "+k+"-"+c+"]");
				}
			}
		}
	}

	tree.addListener(SWT.EraseItem, new Listener() {
		public void handleEvent(Event event) {			
			if(tree.getEnabled() && (event.detail & SWT.SELECTED) != 0) {
				GC gc = event.gc;
				gc.setAdvanced(true);
				if (gc.getAdvanced()) gc.setAlpha(127);								
				Rectangle rect = event.getBounds();
				gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
				gc.setBackground(display.getSystemColor(SWT.COLOR_LIST_BACKGROUND));
				gc.fillGradientRectangle(0, rect.y, 500, rect.height, false);
				event.detail &= ~SWT.SELECTED;					
			}						
		}
	});		
	for (int i=0; i<columnCount; i++) {
		tree.getColumn(i).pack();
	}	
	tree.setSelection(tree.getItem(0));
	shell.setSize(500, 200);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();	
}
}