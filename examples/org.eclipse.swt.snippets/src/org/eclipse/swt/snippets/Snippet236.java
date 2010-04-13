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
 * Table example snippet: draw different foreground colors for text in a TableItem.
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

public class Snippet236 {

public static void main(String [] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setText("Table: Change style multiple times in cell");
	shell.setLayout(new FillLayout());
	Table table = new Table(shell, SWT.MULTI | SWT.FULL_SELECTION);
	table.setLinesVisible(true);
	for(int i = 0; i < 10; i++) {
		new TableItem(table, SWT.NONE);			
	}
	final TextLayout textLayout = new TextLayout(display);
	textLayout.setText("SWT: Standard Widget Toolkit");
	Font font1 = new Font(display, "Tahoma", 14, SWT.BOLD);
	Font font2 = new Font(display, "Tahoma", 10, SWT.NORMAL);
	Font font3 = new Font(display, "Tahoma", 14, SWT.ITALIC);
	TextStyle style1 = new TextStyle(font1, display.getSystemColor(SWT.COLOR_BLUE), null);
	TextStyle style2 = new TextStyle(font2, display.getSystemColor(SWT.COLOR_MAGENTA), null);
	TextStyle style3 = new TextStyle(font3, display.getSystemColor(SWT.COLOR_RED), null);
	textLayout.setStyle(style1, 0, 0); textLayout.setStyle(style1, 5, 12);
	textLayout.setStyle(style2, 1, 1); textLayout.setStyle(style2, 14, 19);
	textLayout.setStyle(style3, 2, 2); textLayout.setStyle(style3, 21, 27);

	/*
	 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
	 * Therefore, it is critical for performance that these methods be
	 * as efficient as possible.
	 */
	table.addListener(SWT.PaintItem, new Listener() {
		public void handleEvent(Event event) {
			textLayout.draw(event.gc, event.x, event.y);
		}
	});
	final Rectangle textLayoutBounds = textLayout.getBounds();
	table.addListener(SWT.MeasureItem, new Listener() {
		public void handleEvent(Event e) {
			e.width = textLayoutBounds.width + 2;
			e.height = textLayoutBounds.height + 2;
		}
	});
	shell.setSize(400, 200);
	shell.open();
	while(!shell.isDisposed()) {
		if(!display.readAndDispatch()) display.sleep();
	}
	font1.dispose();
	font2.dispose();
	font3.dispose();
	textLayout.dispose();
	display.dispose();
}
}
