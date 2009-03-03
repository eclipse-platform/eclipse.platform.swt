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
 * Take a snapshot of a control
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.4
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

public class Snippet292 {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		final Group group = new Group(shell, SWT.NONE);
		group.setText("Group");
		group.setLayout(new GridLayout());
		final Tree tree = new Tree(group, SWT.BORDER);
		for (int i = 0; i < 5; i++) {
			TreeItem treeItem = new TreeItem (tree, SWT.NONE);
			treeItem.setText ("TreeItem " + i);
			for (int j = 0; j < 3; j++) {
				TreeItem subItem = new TreeItem(treeItem, SWT.NONE);
				subItem.setText("SubItem " + i + "-" + j);
			}
			if (i % 3 == 0) treeItem.setExpanded (true);
		}
		new Button(group, SWT.PUSH).setText("Button");
		final Label label = new Label (shell, SWT.NONE);
		label.addListener (SWT.Dispose, new Listener () {
			public void handleEvent (Event e) {
				Image image = label.getImage ();
				if (image != null) image.dispose ();
			}
		});
		Button button = new Button (shell, SWT.PUSH);
		button.setText ("Snapshot");
		button.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				Image image = label.getImage ();
				if (image != null) image.dispose ();
				image = new Image (display, group.getBounds ());
				GC gc = new GC (image);
				boolean success = group.print (gc);
				gc.dispose ();
				label.setImage (image);
				if (!success) {
					MessageBox messageBox = new MessageBox (shell, SWT.OK | SWT.PRIMARY_MODAL);
					messageBox.setMessage ("Sorry, taking a snapshot is not supported on your platform");
					messageBox.open ();
				}
			}
		});
		GridLayout layout = new GridLayout (2, true);
		shell.setLayout(layout);
		group.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		label.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		button.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true, 2, 1));
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}