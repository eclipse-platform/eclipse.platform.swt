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
 * example snippet: Multiple lines per TableItem
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

public class Snippet231 {
	
public static void main(String [] args) {
	Display display = new Display();
	Shell shell = new Shell (display);
	shell.setText("Multiple lines in a TableItem");
	shell.setLayout (new FillLayout());
	final Table table = new Table(shell, SWT.MULTI | SWT.FULL_SELECTION);
	table.setHeaderVisible(true);
	table.setLinesVisible(true);
	int columnCount = 4;
	for (int i=0; i<columnCount; i++) {
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Column " + i);	
	}
	int itemCount = 8;
	for(int i = 0; i < itemCount; i++) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(new String[] {"item "+i+" a", "item "+i+" b", "item "+i+" c", "item "+i+" d"});
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
					TableItem item = (TableItem)event.item;
					String text = getText(item, event.index);
					Point size = event.gc.textExtent(text);
					event.width = size.x;
					event.height = Math.max(event.height, size.y);
					break;
				}
				case SWT.PaintItem: {
					TableItem item = (TableItem)event.item;
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
		String getText(TableItem item, int column) {
			String text = item.getText(column);
			if (column != 0) {
				int index = table.indexOf(item);
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
	table.addListener(SWT.MeasureItem, paintListener);
	table.addListener(SWT.PaintItem, paintListener);
	table.addListener(SWT.EraseItem, paintListener);
	for (int i = 0; i < columnCount; i++) {
		table.getColumn(i).pack();
	}
	shell.pack();
	shell.open();
	while(!shell.isDisposed()) {
		if(!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}