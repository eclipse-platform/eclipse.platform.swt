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
 * ToolBar example snippet: item colors and status text
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet153 {

public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setText("Snippet 153");
	RowLayout layout = new RowLayout(SWT.VERTICAL);
	layout.fill = true;
	shell.setLayout(layout);
	ToolBar bar = new ToolBar(shell, SWT.BORDER);
	Label statusLine = new Label(shell, SWT.BORDER);
	ToolItem toggle = new ToolItem(bar, SWT.CHECK);
	toggle.setText("Toggle Bar Colors");
	new ToolItem(bar, SWT.SEPARATOR);
	new ToolItem(bar, SWT.PUSH).setText("Push Button");
	new ToolItem(bar, SWT.CHECK).setText("Check Button");
	new ToolItem(bar, SWT.DROP_DOWN).setText("Drop Down");

	toggle.addListener(SWT.Selection, event -> {
		if (toggle.getSelection()) {
			bar.setForeground(display.getSystemColor(SWT.COLOR_GREEN));
			bar.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
		} else {
			bar.setForeground(null);
			bar.setBackground(null);
		}
	});

	Listener barListener = new Listener() {
		ToolItem item;
		String statusText = "";

		@Override
		public void handleEvent(Event event) {
			if (item != null) {
				item.setForeground(null);
				item.setBackground(null);
			}
			if (event.type == SWT.MouseMove) {
				item = bar.getItem(new Point(event.x, event.y));
			} else {
				item = null;
			}
			if (item != null) {
				item.setForeground(display.getSystemColor(SWT.COLOR_RED));
				item.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
			}
			String name = (item != null) ? item.getText() : "";
			if (!statusText.equals(name)) {
				statusLine.setText(name);
				statusText = name;
			}
		}
	};
	bar.addListener(SWT.MouseMove, barListener);
	bar.addListener(SWT.MouseExit, barListener);

	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

}
