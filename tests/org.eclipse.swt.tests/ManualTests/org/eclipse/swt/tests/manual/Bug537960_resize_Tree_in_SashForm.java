/*******************************************************************************
 * Copyright (c) 2018 Simeon Andreev and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Simeon Andreev - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.manual;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Description: Resizing part stack of Package Explorer doesn't always show bottom rows.
 *
 * <ol>
 * <li>Open Package Explorer.</li>
 * <li>Move another view in a part stack below the Package Explorer part stack.</li>
 * <li>Move the sash between the two part stacks, so that the Package Explorer view has an increased height.</li>
 * </ol>
 * Expected results: Package Explorer tree is resized and all rows are visible.
 * Actual results: Often, the text and icons of a few of the bottom rows are not painted.
 */
public class Bug537960_resize_Tree_in_SashForm {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.setSize(450, 300);
		shell.setText("Bug 537960: Tree rows not painted on resize");

		SashForm sashForm = new SashForm(shell, SWT.VERTICAL);

		tree(sashForm);

		Label label = new Label(sashForm, SWT.BORDER);
		label.setText("move sash down");

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void tree(Composite parent) {
		Display display = parent.getDisplay();
		final Color darkGreen = display.getSystemColor(SWT.COLOR_DARK_GREEN);

		Composite composite = new Composite(parent, SWT.BORDER);
		composite.setLayout(new FillLayout(SWT.VERTICAL));

		Tree tree = new Tree(composite, SWT.NONE);
		for (int i = 0; i < 20; ++i) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText("item " + i);
		}

		class PaintListener implements Listener {
			@Override
			public void handleEvent(Event event) {
				if (event.type == SWT.PaintItem) {
					TreeItem item = (TreeItem) event.item;
					GC gc = event.gc;
					gc.setBackground(darkGreen);
					gc.setAlpha(55);
					gc.fillRectangle(item.getBounds());
				}
			}
		}

		class MeasureListener implements Listener {
			@Override
			public void handleEvent(Event event) {
				/*
				 * An item measure listener needs to be registered, for the problem to be seen.
				 * The listener doesn't need to actually do something.
				 */
			}
		}

		tree.addListener(SWT.PaintItem, new PaintListener());

		boolean addMeasureListener = true; // if we don't add a measure listener, the bug is not seen
		if (addMeasureListener) {
			tree.addListener(SWT.MeasureItem, new MeasureListener());
		}
	}
}
