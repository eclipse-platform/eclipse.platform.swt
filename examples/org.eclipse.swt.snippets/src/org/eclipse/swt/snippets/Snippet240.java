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
 * Tree snippet: Text that spans multiple columns
 *
 * For more info on custom-drawing TableItem and TreeItem content see 
 * http://www.eclipse.org/articles/article.php?file=Article-CustomDrawingTableAndTreeItems/index.html
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.2
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet240 {

public static void main(String [] args) {
	final Display display = new Display();
	Shell shell = new Shell (display);
	shell.setText("Text spans two columns in a TreeItem");
	shell.setLayout (new FillLayout());
	final Tree tree = new Tree(shell, SWT.MULTI | SWT.FULL_SELECTION);
	tree.setHeaderVisible(true);
	int columnCount = 4;
	for (int i=0; i<columnCount; i++) {
		TreeColumn column = new TreeColumn(tree, SWT.NONE);
		column.setText("Column " + i);	
	}
	int itemCount = 8;
	for (int i = 0; i < itemCount; i++) {
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText(0, "item "+i+" a");
		item.setText(3, "item "+i+" d");
		for (int j = 0; j < 3; j++) {
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setText(0, "subItem "+i+"-"+j+" a");
			subItem.setText(3, "subItem "+i+"-"+j+" d");
		}
	}	
	/*
	 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
	 * Therefore, it is critical for performance that these methods be
	 * as efficient as possible.
	 */
	final String string = "text that spans two columns";
	GC gc = new GC(tree);
	final Point extent = gc.stringExtent(string);
	gc.dispose();
	final Color red = display.getSystemColor(SWT.COLOR_RED);
	Listener paintListener = new Listener() {
		public void handleEvent(Event event) {
			switch(event.type) {		
				case SWT.MeasureItem: {
					if (event.index == 1 || event.index == 2) {
						event.width = extent.x/2;
						event.height = Math.max(event.height, extent.y + 2);
					}
					break;
				}
				case SWT.PaintItem: {
					if (event.index == 1 || event.index == 2) {
						int offset = 0;
						if (event.index == 2) {
							TreeColumn column1 = tree.getColumn(1);
							offset = column1.getWidth();
						}
						event.gc.setForeground(red);
						int y = event.y + (event.height - extent.y)/2;
						event.gc.drawString(string, event.x - offset, y, true);
					}
					break;
				}
			}
		}
	};
	tree.addListener(SWT.MeasureItem, paintListener);
	tree.addListener(SWT.PaintItem, paintListener);
	for (int i = 0; i < columnCount; i++) {
		tree.getColumn(i).pack();
	}
	shell.pack();
	shell.open();
	while(!shell.isDisposed()) {
		if(!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}