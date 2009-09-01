/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
 * ScrolledComposite snippet: use a ScrolledComposite to scroll a disabled
 * control.  Note that this goes against conventional UI behaviour, where
 * disabling a control typically disables its scrollbars as well.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet322 {
	
public static void main (String[] args) {
	final Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setBounds (10, 10, 300, 300);
	final ScrolledComposite sc = new ScrolledComposite (shell, SWT.VERTICAL);
	sc.setBounds (10, 10, 280, 200);
	final int clientWidth = sc.getClientArea ().width;

	final Tree tree = new Tree (sc, SWT.NONE);
	for (int i = 0; i < 99; i++) {
		TreeItem item = new TreeItem (tree, SWT.NONE);
		item.setText ("item " + i);
		new TreeItem (item, SWT.NONE).setText ("child");
	}
	sc.setContent (tree);
	int prefHeight = tree.computeSize (SWT.DEFAULT, SWT.DEFAULT).y;
	tree.setSize (clientWidth, prefHeight);
	/*
	 * The following listener ensures that the Tree is always large
	 * enough to not need to show its own vertical scrollbar.
	 */
	tree.addTreeListener (new TreeListener () {
		public void treeExpanded (TreeEvent e) {
			int prefHeight = tree.computeSize (SWT.DEFAULT, SWT.DEFAULT).y;
			tree.setSize (clientWidth, prefHeight);
		}
		public void treeCollapsed (TreeEvent e) {
			int prefHeight = tree.computeSize (SWT.DEFAULT, SWT.DEFAULT).y;
			tree.setSize (clientWidth, prefHeight);
		}
	});
	/*
	 * The following listener ensures that a newly-selected item
	 * in the Tree is always visible.
	 */
	tree.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			TreeItem [] selectedItems = tree.getSelection();
			if (selectedItems.length > 0) {
				Rectangle itemRect = selectedItems[0].getBounds();
				Rectangle area = sc.getClientArea();
				Point origin = sc.getOrigin();
				if (itemRect.x < origin.x || itemRect.y < origin.y
						|| itemRect.x + itemRect.width > origin.x + area.width
						|| itemRect.y + itemRect.height > origin.y + area.height) {
					sc.setOrigin(itemRect.x, itemRect.y);
				}
			}
		}
	});
	/*
	 * The following listener scrolls the Tree one item at a time
	 * in response to MouseWheel events.
	 */
	tree.addListener(SWT.MouseWheel, new Listener() {
		public void handleEvent(Event event) {
			Point origin = sc.getOrigin();
			if (event.count < 0) {
				origin.y = Math.min(origin.y + tree.getItemHeight(), tree.getSize().y);
			} else {
				origin.y = Math.max(origin.y - tree.getItemHeight(), 0);
			}
			sc.setOrigin(origin);
		}
	});

	Button disableButton = new Button (shell, SWT.PUSH);
	disableButton.setBounds (10, 220, 120, 30);
	disableButton.setText ("Disable");
	disableButton.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event event) {
			tree.setEnabled(false);
		}
	});
	Button enableButton = new Button (shell, SWT.PUSH);
	enableButton.setBounds (140, 220, 120, 30);
	enableButton.setText ("Enable");
	enableButton.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event event) {
			tree.setEnabled(true);
		}
	});

	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
