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
 *     Thomas Singer
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Bug536934_TreeFullItem {

	public static void main(String[] args) {
		final Display display = new Display();

		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final Tree tree = new Tree(shell, SWT.BORDER);

		final TreeItem item1 = new TreeItem(tree, SWT.None);
		new TreeItem(item1, SWT.NONE);
		final TreeItem item2 = new TreeItem(tree, SWT.None);
		new TreeItem(item2, SWT.NONE);

		final Listener listener = new Listener() {
			private final String text = "item fkds fjsd fd  dsf d fd sfkl dsl fsld fl sfl sd f dflk sd flds fds END";

			@Override
			public void handleEvent(Event event) {
				if (event.type == SWT.MeasureItem) {
					final Point size = event.gc.textExtent(text);
					event.width = size.x;
					event.height = size.y;
				}
				else if (event.type == SWT.EraseItem) {
					if ((event.detail & SWT.SELECTED) > 0) {
						event.gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
						event.gc.setForeground(display.getSystemColor(SWT.COLOR_YELLOW));
					}
					else {
						event.gc.setBackground(display.getSystemColor(SWT.COLOR_MAGENTA));
						event.gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
					}
					event.gc.fillRectangle(event.x, event.y, event.width, event.height);
					event.detail &= ~SWT.SELECTED;
				}
				else if (event.type == SWT.PaintItem) {
					event.gc.drawText(text, event.x, event.y, true);
				}
			}
		};
		tree.addListener(SWT.MeasureItem, listener);
		tree.addListener(SWT.EraseItem, listener);
		tree.addListener(SWT.PaintItem, listener);

		shell.setSize(300, 300);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}