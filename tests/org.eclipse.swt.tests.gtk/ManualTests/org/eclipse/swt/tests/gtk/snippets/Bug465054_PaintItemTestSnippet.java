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
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug465054_PaintItemTestSnippet {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shellMain = new Shell(display, SWT.SHELL_TRIM);
		shellMain.setLayout(new FillLayout(SWT.VERTICAL));

		Tree tree = new Tree(shellMain, SWT.BORDER);
		new TreeItem(tree, SWT.NONE).setText("setText");
		for (int i = 0; i < 100; i++) {
			new TreeItem(tree, SWT.NONE).setText("setText " + i);
		}

		tree.addListener(SWT.PaintItem, new Listener() {
			@Override
			public void handleEvent(Event event) {
				System.out.println("PaintItem");
				Rectangle bounds = ((TreeItem) event.item).getBounds(event.index);
				event.gc.fillRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
				event.gc.drawText(((TreeItem) event.item).getText(), bounds.x, bounds.y, true);
			}
		});
		

		shellMain.open();

		while (!shellMain.isDisposed()) {
			try {
				if (!display.readAndDispatch())
					display.sleep();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		display.dispose();
	}
}
