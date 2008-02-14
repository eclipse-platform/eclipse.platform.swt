/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
 * ScrolledComposite snippet: Use a ScrolledComposite to scroll a Tree vertically.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;

public class Snippet296 {
	
public static void main (String[] args) {
    final Display display = new Display ();
    Shell shell = new Shell (display);
    shell.setBounds (10, 10, 200, 300);
    final ScrolledComposite sc = new ScrolledComposite (shell, SWT.VERTICAL);
    sc.setBounds (10, 10, 180, 200);
    final int clientWidth = sc.getClientArea ().width;

    final Tree tree = new Tree (sc, SWT.NONE);
    for (int i = 0; i < 99; i++) {
        TreeItem item = new TreeItem (tree, SWT.NONE);
        item.setText ("item " + i);
        new TreeItem (item, SWT.NONE).setText ("child");
    }
    sc.setContent (tree);
    int prefHeight = tree.computeSize (SWT.DEFAULT, SWT.DEFAULT).y;
    tree.setSize (clientWidth, prefHeight);
    /*
     * The following listener ensures that the Tree is always large
     * enough to not need to show its own vertical scrollbar.
     */
    tree.addTreeListener (new TreeListener () {
        public void treeExpanded (TreeEvent e) {
            int prefHeight = tree.computeSize (SWT.DEFAULT, SWT.DEFAULT).y;
            tree.setSize (clientWidth, prefHeight);
        }
        public void treeCollapsed (TreeEvent e) {
            int prefHeight = tree.computeSize (SWT.DEFAULT, SWT.DEFAULT).y;
            tree.setSize (clientWidth, prefHeight);
        }
    });

    Button downButton = new Button (shell, SWT.PUSH);
    downButton.setBounds (10, 220, 80, 30);
    downButton.setText ("Down 10px");
    downButton.addListener (SWT.Selection, new Listener () {
        public void handleEvent (Event event) {
            sc.setOrigin (0, sc.getOrigin ().y + 10);
        }
    });
    Button upButton = new Button (shell, SWT.PUSH);
    upButton.setBounds (100, 220, 80, 30);
    upButton.setText ("Up 10px");
    upButton.addListener (SWT.Selection, new Listener () {
        public void handleEvent (Event event) {
            sc.setOrigin (0, sc.getOrigin ().y - 10);
        }
    });
    shell.open ();
    while (!shell.isDisposed ()) {
        if (!display.readAndDispatch ()) display.sleep ();
    }
    display.dispose ();
}
}
