/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * GridLayout example snippet: insert widgets into a grid layout
 *
 * For a list of all SWT example snippets see
 * http://dev.eclipse.org/viewcvs/index.cgi/%7Echeckout%7E/platform-swt-home/dev.html#snippets
 */
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet6 {

public static void main (String [] args) {
    Display display = new Display ();
    final Shell shell = new Shell (display);
    shell.setLayout(new GridLayout());
    final Composite c = new Composite(shell, SWT.NONE);
    GridLayout layout = new GridLayout();
    layout.numColumns = 3;
    c.setLayout(layout);
    for (int i = 0; i < 10; i++) {
        Button b = new Button(c, SWT.PUSH);
        b.setText("Button "+i);
    }

    Button b = new Button(shell, SWT.PUSH);
    b.setText("add a new button at row 2 column 1");
    final int[] index = new int[1];
    b.addListener(SWT.Selection, new Listener() {
        public void handleEvent(Event e) {
            Button b = new Button(c, SWT.PUSH);
            b.setText("Special "+index[0]);
            index[0]++;
            Control[] children = c.getChildren();
            b.moveAbove(children[3]);
            shell.layout(true);
        }
    });

    shell.open ();
    while (!shell.isDisposed ()) {
        if (!display.readAndDispatch ()) display.sleep ();
    }
    display.dispose ();
}

}
