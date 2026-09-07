/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * CTabFolder example: background of a top right control that wrapped below the
 * tab row. Widen the shell until the tool bar fits next to the tabs.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet396 {
	static final Color TAB_ROW = new Color(0x21, 0x22, 0x2C);
	static final Color BODY = new Color(0x28, 0x2A, 0x36);

	/** Paints tab row and body in two different colors. */
	static class TwoToneRenderer extends CTabFolderRenderer {
		TwoToneRenderer(CTabFolder parent) {
			super(parent);
		}

		@Override
		protected void draw(int part, int state, Rectangle bounds, GC gc) {
			if (part == PART_BACKGROUND) {
				int split = bounds.y + parent.getTabHeight();
				gc.setBackground(TAB_ROW);
				gc.fillRectangle(bounds.x, bounds.y, bounds.width, split - bounds.y);
				gc.setBackground(BODY);
				gc.fillRectangle(bounds.x, split, bounds.width, bounds.y + bounds.height - split);
				return;
			}
			super.draw(part, state, bounds, gc);
			if (part == PART_HEADER) {
				int split = bounds.y + parent.getTabHeight() + 3;
				gc.setBackground(BODY);
				gc.fillRectangle(bounds.x, split, bounds.width, bounds.y + bounds.height - split);
			}
		}
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Wrapped top right control");
		shell.setLayout(new FillLayout());

		CTabFolder folder = new CTabFolder(shell, SWT.NONE);
		folder.setRenderer(new TwoToneRenderer(folder));
		folder.setBackground(TAB_ROW);
		folder.setForeground(display.getSystemColor(SWT.COLOR_WHITE));

		for (String name : new String[] { "Variables", "Breakpoints", "Expressions" }) {
			CTabItem item = new CTabItem(folder, SWT.NONE);
			item.setText(name);
			Composite content = new Composite(folder, SWT.NONE);
			content.setBackground(BODY);
			item.setControl(content);
		}
		folder.setSelection(0);

		Composite topRight = new Composite(folder, SWT.NONE);
		topRight.setLayout(new FillLayout());
		ToolBar toolBar = new ToolBar(topRight, SWT.FLAT);
		for (String text : new String[] { "Collapse", "Expand", "Filter", "Menu" }) {
			new ToolItem(toolBar, SWT.PUSH).setText(text);
		}
		folder.setTopRight(topRight, SWT.RIGHT | SWT.WRAP);

		shell.setSize(360, 200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
