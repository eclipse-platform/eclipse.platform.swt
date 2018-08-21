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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug258196_VirtualTreeResizing {
	public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setLayout (new FillLayout());
        final Tree tree = new Tree(shell, SWT.VIRTUAL | SWT.BORDER);
        tree.addListener(SWT.SetData, event -> {
		    final TreeItem item = (TreeItem)event.item;
		    TreeItem parentItem = item.getParentItem();
		    String text = null;
		    if (parentItem == null) {
		        text = "node "+tree.indexOf(item);
		    } else {
		        text = parentItem.getText()+" - "+parentItem.indexOf(item);
		    }
		    item.setData(text);
		    item.setItemCount(10);
		    display.asyncExec(() -> {
			    if (!item.isDisposed()) {
			        item.setText(item.getData().toString());
			    }
			});
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
