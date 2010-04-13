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
 * Table example snippet: show results as a bar chart in table
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

public class Snippet228 {
	
public static void main(String [] args) {
	final Display display = new Display();		
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	shell.setText("Show results as a bar chart in Table");
	final Table table = new Table(shell, SWT.BORDER);
	table.setHeaderVisible(true);
	table.setLinesVisible(true);
	TableColumn column1 = new TableColumn(table, SWT.NONE);
	column1.setText("Bug Status");
	column1.setWidth(100);
	final TableColumn column2 = new TableColumn(table, SWT.NONE);
	column2.setText("Percent");
	column2.setWidth(200);
	String[] labels = new String[]{"Resolved", "New", "Won't Fix", "Invalid"};
	for (int i=0; i<labels.length; i++) {
		 TableItem item = new TableItem(table, SWT.NONE);
		 item.setText(labels[i]);
	}

	/*
	 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
	 * Therefore, it is critical for performance that these methods be
	 * as efficient as possible.
	 */
	table.addListener(SWT.PaintItem, new Listener() {
		int[] percents = new int[] {50, 30, 5, 15};
		public void handleEvent(Event event) {
			if (event.index == 1) {
				GC gc = event.gc;
				TableItem item = (TableItem)event.item;
				int index = table.indexOf(item);
				int percent = percents[index];
				Color foreground = gc.getForeground();
				Color background = gc.getBackground();
				gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
				gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
				int width = (column2.getWidth() - 1) * percent / 100;
				gc.fillGradientRectangle(event.x, event.y, width, event.height, true);					
				Rectangle rect2 = new Rectangle(event.x, event.y, width-1, event.height-1);
				gc.drawRectangle(rect2);
				gc.setForeground(display.getSystemColor(SWT.COLOR_LIST_FOREGROUND));
				String text = percent+"%";
				Point size = event.gc.textExtent(text);					
				int offset = Math.max(0, (event.height - size.y) / 2);
				gc.drawText(text, event.x+2, event.y+offset, true);
				gc.setForeground(background);
				gc.setBackground(foreground);
			}
		}
	});		
			
	shell.pack();
	shell.open();
	while(!shell.isDisposed()) {
		if(!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}