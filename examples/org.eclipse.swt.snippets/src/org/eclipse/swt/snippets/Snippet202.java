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
 * Virtual Tree example snippet: create a virtual tree (lazy)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet202 {

public static void main(String[] args) {
	Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setLayout (new FillLayout());
	final Tree tree = new Tree(shell, SWT.VIRTUAL | SWT.BORDER);
	tree.addListener(SWT.SetData, event -> {
		TreeItem item = (TreeItem)event.item;
		TreeItem parentItem = item.getParentItem();
		String text = null;
		if (parentItem == null) {
			text = "node "+tree.indexOf(item);
		} else {
			text = parentItem.getText()+" - "+parentItem.indexOf(item);
		}
		item.setText(text);
		item.setItemCount(10);
	});
	tree.setItemCount(20);
	shell.setSize(400, 300);
	shell.open();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}