/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

/*
 * Tree example snippet: draw images on right side of tree item
 *
 * For more info on custom-drawing TreeItem and TreeItem content see
 * http://www.eclipse.org/articles/article.php?file=Article-CustomDrawingTableAndTreeItems/index.html
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

public class Bug531882_TreeColIconPacking {

public static void main(String [] args) {
	Display display = new Display();
	final Image image = display.getSystemImage(SWT.ICON_INFORMATION);
	Shell shell = new Shell(display);

	shell.setText("Images on the right side of the TreeItem");
	shell.setLayout(new FillLayout ());

	Tree tree = new Tree(shell, SWT.MULTI | SWT.FULL_SELECTION);
	tree.setHeaderVisible(true);
	tree.setLinesVisible(true);

	int columnCount = 3;
	for (int i=0; i<columnCount; i++) {
		TreeColumn column = new TreeColumn(tree, SWT.NONE);
		column.setText("Column " + i);
	}

	int itemCount = 4;
	for(int i = 0; i < itemCount; i++) {
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText(new String[] {"item "+i+" a", "item "+i+" b", "item "+i+" c"});
		for (int j = 0; j < itemCount; j++) {
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setText(new String[] {"sub-item " + j + " a", "sub-item " + j + " b", "sub-item " + j + " c"});
			for (int k = 0; k < itemCount; k++) {
				TreeItem kItem = new TreeItem(subItem, SWT.NONE);
				kItem.setText(new String[] {"sub-sub-item "+k+" a", "sub-sub-item "+k+" b", "sub-sub-item "+k+" c"});
			}
		}
	}

	/*
	 * NOTE: MeasureItem, PaintItem and EraseItem are called repeatedly.
	 * Therefore, it is critical for performance that these methods be
	 * as efficient as possible.
	 */
	Listener paintListener = event -> {
		switch(event.type) {
			case SWT.MeasureItem: {
				Rectangle rect1 = image.getBounds();
				event.width += rect1.width;
				event.height = Math.max(event.height, rect1.height + 2);
				break;
			}
			case SWT.PaintItem: {
				int x = event.x + event.width;
				Rectangle rect2 = image.getBounds();
				int offset = Math.max(0, (event.height - rect2.height) / 2);
				event.gc.drawImage(image, x, event.y + offset);
				break;
			}
		}
	};
	tree.addListener(SWT.MeasureItem, paintListener);
	tree.addListener(SWT.PaintItem, paintListener);

	tree.setVisible(true);

	for(int i = 0; i < columnCount; i++) {
		tree.getColumn(i).pack();
	}

//	for(int i = 0; i < columnCount; i++) {
//		tree.getColumn(i).pack();
//	}

	shell.setSize(500, 200);
	shell.open();
	while(!shell.isDisposed ()) {
		if(!display.readAndDispatch()) display.sleep();
	}
	if(image != null) image.dispose();
	display.dispose();
}
}
