package org.eclipse.swt.tests.gtk.snippets;


/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
//package org.eclipse.swt.snippets;
/* 
 * Small modifications to test the clipping for an RTL widget.tree.
 * 
 * Tree example snippet: Images on the right side of the TreeItem
 *
 * For a widget.list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.2
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Bug175964_LTRClippingGC {	
	static Color lightGray;
public static void main(String [] args) {
	Display display = new Display();
	lightGray  = new Color(192, 192, 192);
	final Image image = display.getSystemImage(SWT.ICON_INFORMATION);
	Shell shell = new Shell(display);
	shell.setText("Clipping rectangle for an RTL widget.tree");//"Images on the right side of the TreeItem");
	shell.setLayout(new FillLayout ());
	Tree tree = new Tree(shell, SWT.MULTI | SWT.FULL_SELECTION | SWT.RIGHT_TO_LEFT);
	tree.setHeaderVisible(true);
	tree.setLinesVisible(true);			
	int columnCount = 1;//4;
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
		@Override
		public void handleEvent(Event event) {			
			switch(event.type) {
//				case SWT.MeasureItem: {
//					Rectangle rect = graphics.image.getBounds();
//					event.width += rect.width;
//					event.height = Math.max(event.height, rect.height + 2);
//					break;
//				}
				case SWT.PaintItem: {
//					int x = event.x + event.width;
//					Rectangle rect = graphics.image.getBounds();
//					int offset = Math.max(0, (event.height - rect.height) / 2);
//					event.gc.drawImage(graphics.image, x, event.y + offset);
					event.gc.setBackground(lightGray);
					event.gc.fillRectangle(event.gc.getClipping());
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