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
 * Tree example snippet: Images on the right side of the TreeItem
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

public class Snippet220 {	
	
public static void main(String [] args) {
	Display display = new Display();
	final Image image = display.getSystemImage(SWT.ICON_INFORMATION);
	Shell shell = new Shell(display);
	shell.setText("Images on the right side of the TreeItem");
	shell.setLayout(new FillLayout ());
	Tree tree = new Tree(shell, SWT.MULTI | SWT.FULL_SELECTION);
	tree.setHeaderVisible(true);
	tree.setLinesVisible(true);			
	int columnCount = 4;
	for(int i = 0; i < columnCount; i++) {
		TreeColumn column = new TreeColumn(tree, SWT.NONE);
		column.setText("Column " + i);	
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
					Rectangle rect = image.getBounds();
					event.width += rect.width;
					event.height = Math.max(event.height, rect.height + 2);
					break;
				}
				case SWT.PaintItem: {
					int x = event.x + event.width;
					Rectangle rect = image.getBounds();
					int offset = Math.max(0, (event.height - rect.height) / 2);
					event.gc.drawImage(image, x, event.y + offset);
					break;
				}
			}
		}
	};		
	tree.addListener(SWT.MeasureItem, paintListener);
	tree.addListener(SWT.PaintItem, paintListener);		

	for(int i = 0; i < columnCount; i++) {
		tree.getColumn(i).pack();
	}	
	shell.setSize(500, 200);
	shell.open();
	while(!shell.isDisposed ()) {
		if(!display.readAndDispatch()) display.sleep();
	}
	if(image != null) image.dispose();
	display.dispose();
}
}
