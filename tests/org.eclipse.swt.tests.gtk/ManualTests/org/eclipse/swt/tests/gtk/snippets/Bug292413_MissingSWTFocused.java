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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug292413_MissingSWTFocused {

	public static void main(String[] args) {
	    final Display display = new Display();
	    final Shell shell = new Shell(display);
	    shell.setBounds(10,10,200,200);
	    shell.setLayout(new FillLayout());
	    final Tree tree = new Tree(shell, SWT.MULTI);
	    for (int i = 0; i < 9; i++) {
	        new TreeItem(tree, SWT.NONE).setText("item " + i);
	    }
	    tree.addListener(SWT.EraseItem, new Listener() {
	        @Override
			public void handleEvent(Event event) {
	            System.out.println("EraseItem event");
	            if ((event.detail & SWT.FOCUSED) != 0) {
	                System.out.println("item has focus: " + ((TreeItem)event.item).getText());
	            }
	        }
	    });
	    tree.addListener(SWT.PaintItem, new Listener() {
	        @Override
			public void handleEvent(Event event) {
	            System.out.println("PaintItem event");
	            if ((event.detail & SWT.FOCUSED) != 0) {
	                System.out.println("item has focus: " + ((TreeItem)event.item).getText());
	            }
	        }
	    });
	    shell.open();
	    while (!shell.isDisposed()) {
	        if (!display.readAndDispatch()) display.sleep();
	    }
	    display.dispose();
	}
}
