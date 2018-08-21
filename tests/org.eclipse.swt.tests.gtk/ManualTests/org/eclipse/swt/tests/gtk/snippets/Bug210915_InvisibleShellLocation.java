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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug210915_InvisibleShellLocation {

public static void main(String[] args) {
    Display display = new Display();
    final Shell shell = new Shell(display);
    shell.setText("Main");
    shell.setLayout(new RowLayout());

    final Button button = new Button(shell, SWT.PUSH);
    button.setText("Show");
    button.addSelectionListener(new SelectionAdapter() {
        @Override
		public void widgetSelected(SelectionEvent e) {
            showHover(shell, button);
        }
    });
    shell.setBounds(100, 100, 300, 200);

    shell.open();
    while (!shell.isDisposed()) {
        if (!display.readAndDispatch())
            display.sleep();
    }
    display.dispose();
}

static void showHover(final Shell shell, final Button button) {
    final Display display = shell.getDisplay();
    Rectangle bb = button.getBounds();
    final Point pos = button.toDisplay(bb.x, bb.y + bb.height + 5);

    final Shell hover = new Shell(shell, SWT.ON_TOP | SWT.NO_TRIM);
    hover.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
    hover.setBounds(-30000, 0, 100, 50);
    hover.setVisible(true);

    display.asyncExec(() -> {
	    System.out.println("before setLocation:" + hover.getLocation());
	    hover.setLocation(pos);
	    System.out.println("after setLocation:" + hover.getLocation());
	});

    display.timerExec(2000, () -> {
	    Point loc = hover.getLocation();
	    System.out.println("before setVisible(false): " + loc);
	    hover.setVisible(false);
	    Point loc2 = hover.getLocation();
	    System.out.println("after setVisible(false): " + loc2);
	});

    display.timerExec(4000, () -> {
	    Point loc = hover.getLocation();
	    System.out.println("before setVisible(true): " + loc);
	    hover.setVisible(true);
	    Point loc2 = hover.getLocation();
	    System.out.println("after setVisible(true): " + loc2);
	});
}
}