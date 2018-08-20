/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
package org.eclipse.swt.snippets;

/*
 * TreeEditor example snippet: edit the text of a tree item (in place, fancy)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet111 {

public static void main (String [] args) {
	final Display display = new Display ();
	final Color black = display.getSystemColor (SWT.COLOR_BLACK);
	Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());
	final Tree tree = new Tree (shell, SWT.BORDER);
	for (int i=0; i<16; i++) {
		TreeItem itemI = new TreeItem (tree, SWT.NONE);
		itemI.setText ("Item " + i);
		for (int j=0; j<16; j++) {
			TreeItem itemJ = new TreeItem (itemI, SWT.NONE);
			itemJ.setText ("Item " + j);
		}
	}
	final TreeItem [] lastItem = new TreeItem [1];
	final TreeEditor editor = new TreeEditor (tree);
	tree.addListener (SWT.Selection, event -> {
		final TreeItem item = (TreeItem) event.item;
		if (item != null && item == lastItem [0]) {
			boolean showBorder = true;
			final Composite composite = new Composite (tree, SWT.NONE);
			if (showBorder) composite.setBackground (black);
			final Text text = new Text (composite, SWT.NONE);
			final int inset = showBorder ? 1 : 0;
			composite.addListener (SWT.Resize, e1 -> {
				Rectangle rect1 = composite.getClientArea ();
				text.setBounds (rect1.x + inset, rect1.y + inset, rect1.width - inset * 2, rect1.height - inset * 2);
			});
			Listener textListener = e2 -> {
				switch (e2.type) {
					case SWT.FocusOut:
						item.setText (text.getText ());
						composite.dispose ();
						break;
					case SWT.Verify:
						String newText = text.getText ();
						String leftText = newText.substring (0, e2.start);
						String rightText = newText.substring (e2.end, newText.length ());
						GC gc = new GC (text);
						Point size = gc.textExtent (leftText + e2.text + rightText);
						gc.dispose ();
						size = text.computeSize (size.x, SWT.DEFAULT);
						editor.horizontalAlignment = SWT.LEFT;
						Rectangle itemRect = item.getBounds (), rect2 = tree.getClientArea ();
						editor.minimumWidth = Math.max (size.x, itemRect.width) + inset * 2;
						int left = itemRect.x, right = rect2.x + rect2.width;
						editor.minimumWidth = Math.min (editor.minimumWidth, right - left);
						editor.minimumHeight = size.y + inset * 2;
						editor.layout ();
						break;
					case SWT.Traverse:
						switch (e2.detail) {
							case SWT.TRAVERSE_RETURN:
								item.setText (text.getText ());
								//FALL THROUGH
							case SWT.TRAVERSE_ESCAPE:
								composite.dispose ();
								e2.doit = false;
						}
						break;
				}
			};
			text.addListener (SWT.FocusOut, textListener);
			text.addListener (SWT.Traverse, textListener);
			text.addListener (SWT.Verify, textListener);
			editor.setEditor (composite, item);
			text.setText (item.getText ());
			text.selectAll ();
			text.setFocus ();
		}
		lastItem [0] = item;
	});
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
