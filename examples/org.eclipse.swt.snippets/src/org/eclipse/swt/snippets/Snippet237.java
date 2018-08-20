/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
 * Composite Snippet: inherit a background color or image
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet237 {

public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(display);
	shell.setText("Composite.setBackgroundMode()");
	shell.setLayout(new RowLayout(SWT.VERTICAL));

	Color color = display.getSystemColor(SWT.COLOR_CYAN);

	Group group = new Group(shell, SWT.NONE);
	group.setText("SWT.INHERIT_NONE");
	group.setBackground(color);
	group.setBackgroundMode(SWT.INHERIT_NONE);
	createChildren(group);

	group = new Group(shell, SWT.NONE);
	group.setBackground(color);
	group.setText("SWT.INHERIT_DEFAULT");
	group.setBackgroundMode(SWT.INHERIT_DEFAULT);
	createChildren(group);

	group = new Group(shell, SWT.NONE);
	group.setBackground(color);
	group.setText("SWT.INHERIT_FORCE");
	group.setBackgroundMode(SWT.INHERIT_FORCE);
	createChildren(group);

	shell.pack();
	shell.open();
	while(!shell.isDisposed()) {
		if(!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
static void createChildren(Composite parent) {
	parent.setLayout(new RowLayout());
	List list = new List(parent, SWT.BORDER | SWT.MULTI);
	list.add("List item 1");
	list.add("List item 2");
	Label label = new Label(parent, SWT.NONE);
	label.setText("Label");
	Button button = new Button(parent, SWT.RADIO);
	button.setText("Radio Button");
	button = new Button(parent, SWT.CHECK);
	button.setText("Check box Button");
	button = new Button(parent, SWT.PUSH);
	button.setText("Push Button");
	Text text = new Text(parent, SWT.BORDER);
	text.setText("Text");
}
}
