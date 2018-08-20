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
 * ToolBar example snippet: update a status line when the mouse enters a tool item
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet153 {

static String statusText = "";
public static void main(String[] args) {
    final Display display = new Display();
    Shell shell = new Shell(display);
    shell.setBounds(10, 10, 200, 200);
    final ToolBar bar = new ToolBar(shell, SWT.BORDER);
    bar.setBounds(10, 10, 150, 50);
    final Label statusLine = new Label(shell, SWT.BORDER);
    statusLine.setBounds(10, 90, 150, 30);
    new ToolItem(bar, SWT.NONE).setText("item 1");
    new ToolItem(bar, SWT.NONE).setText("item 2");
    new ToolItem(bar, SWT.NONE).setText("item 3");
    bar.addMouseMoveListener(e -> {
	    ToolItem item = bar.getItem(new Point(e.x, e.y));
	    String name = "";
	    if (item != null) {
	        name = item.getText();
	    }
	    if (!statusText.equals(name)) {
	        statusLine.setText(name);
	        statusText = name;
	    }
	});
    shell.open();
    while (!shell.isDisposed()) {
        if (!display.readAndDispatch()) display.sleep();
    }
    display.dispose();
}

}
