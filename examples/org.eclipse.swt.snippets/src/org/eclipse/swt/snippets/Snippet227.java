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
 * Tree example snippet: draw multiple lines in a tree item
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

public class Snippet227 {	
public static void main(String [] args) {
	Display display = new Display();
	Shell shell = new Shell (display);
	shell.setText("Multiple lines in a TreeItem");
	shell.setLayout (new FillLayout());
	final Tree tree = new Tree(shell, SWT.MULTI | SWT.FULL_SELECTION);
	tree.setHeaderVisible(true);
	tree.setLinesVisible(true);
	int columnCount = 4;
	for (int i=0; i<columnCount; i++) {
		TreeColumn column = new TreeColumn(tree, SWT.NONE);
		column.setText("Column " + i);
		column.setWidth(100);
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

	/*
	 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
	 * Therefore, it is critical for performance that these methods be
	 * as efficient as possible.
	 */
	Listener paintListener = new Listener() {
		public void handleEvent(Event event) {
			switch(event.type) {		
				case SWT.MeasureItem: {
					TreeItem item = (TreeItem)event.item;
					String text = getText(item, event.index);
					Point size = event.gc.textExtent(text);
					event.width = size.x;
					event.height = Math.max(event.height, size.y);
					break;
				}
				case SWT.PaintItem: {
					TreeItem item = (TreeItem)event.item;
					String text = getText(item, event.index);
					Point size = event.gc.textExtent(text);					
					int offset2 = event.index == 0 ? Math.max(0, (event.height - size.y) / 2) : 0;
					event.gc.drawText(text, event.x, event.y + offset2, true);
					break;
				}
				case SWT.EraseItem: {	
					event.detail &= ~SWT.FOREGROUND;
					break;
				}
			}
		}
		String getText(TreeItem item, int column) {
			String text = item.getText(column);
			if (column != 0) {
				TreeItem parent = item.getParentItem();
				int index = parent == null ? tree.indexOf(item) : parent.indexOf(item);
				if ((index+column) % 3 == 1){
					text +="\nnew line";
				}
				if ((index+column) % 3 == 2) {
					text +="\nnew line\nnew line";
				}
			}
			return text;
		}
	};
	tree.addListener(SWT.MeasureItem, paintListener);
	tree.addListener(SWT.PaintItem, paintListener);
	tree.addListener(SWT.EraseItem, paintListener);

	shell.setSize(600, 400);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}