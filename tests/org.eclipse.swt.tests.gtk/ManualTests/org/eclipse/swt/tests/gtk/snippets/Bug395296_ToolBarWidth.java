/*******************************************************************************
 * Copyright (c) 2019 Red Hat and others. All rights reserved.
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
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public final class Bug395296_ToolBarWidth {

	private static ToolBar tb;
	private static ToolItem item4;

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout());

		Composite buttonComp = new Composite(shell, SWT.NONE);
		buttonComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		buttonComp.setLayout(new RowLayout());
		addButton(buttonComp, "Item 4");
		addButton(buttonComp, "Rebase HEAD to");
		addButton(buttonComp, "Save Stash");

		tb = new ToolBar(shell, SWT.FLAT);
		tb.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		tb.setBackground(tb.getDisplay().getSystemColor(SWT.COLOR_YELLOW));

		for (int i = 0; i < 3; i++) {
			new ToolItem(tb, SWT.NONE).setText("Item " + i);
		}

		final Composite comp = new Composite(shell, SWT.BORDER);
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		comp.addPaintListener(e -> {
			Point size = comp.getSize();
			int d = 50;
			for (int i = d; i < size.x; i += d) {
				e.gc.drawLine(i, 0, i, size.y);
			}
		});

		shell.setSize(600, 300);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void addButton(Composite parent, final String label) {
		Button bt = new Button(parent, SWT.PUSH);
		bt.setText(label);
		bt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (item4 != null) {
					item4.dispose();
				}
				item4 = new ToolItem(tb, SWT.NONE);
				item4.setText(label);
			}
		});
	}

}