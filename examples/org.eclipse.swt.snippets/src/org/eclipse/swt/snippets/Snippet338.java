/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 508155
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Control example snippet: perform custom traversals
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.6
 */

import static org.eclipse.swt.events.FocusListener.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet338 {

public static void main(String[] args) {
	Display display = new Display();
	Shell parentShell = new Shell(display);
	parentShell.setBounds(10,10,100,100);
	parentShell.open();

	Shell childShell = new Shell(parentShell);
	childShell.setLayout(new GridLayout());
	TabFolder folder = new TabFolder(childShell, SWT.NONE);
	folder.setLayout(new FillLayout());
	TabItem tab1 = new TabItem(folder, SWT.NONE);
	tab1.setText("Tab &1");
	new TabItem(folder, SWT.NONE).setText("Tab &2");
	Composite composite = new Composite(folder, SWT.NONE);
	composite.setLayout(new GridLayout());
	tab1.setControl(composite);
	Text text1 = new Text(composite, SWT.SINGLE);

	/* canvas represents a custom control */
	final Canvas canvas = new Canvas(composite, SWT.BORDER);
	canvas.setLayoutData(new GridData(300,200));
	canvas.addListener(SWT.Paint, event -> {
		if (canvas.isFocusControl()) {
			event.gc.drawText("focus is here, custom traverse keys:\n\tN: Tab next\n\tP: Tab previous\n\tR: Return\n\tE: Esc\n\tT: Tab Folder next page", 0, 0);
		} else {
			event.gc.drawString("focus is not in this control", 0, 0);
		}
	});
	canvas.addListener(SWT.KeyDown, event -> {
		int traversal = SWT.NONE;
		switch (event.keyCode) {
			case 'n': traversal = SWT.TRAVERSE_TAB_NEXT; break;
			case 'p': traversal = SWT.TRAVERSE_TAB_PREVIOUS; break;
			case 'r': traversal = SWT.TRAVERSE_RETURN; break;
			case 'e': traversal = SWT.TRAVERSE_ESCAPE; break;
			case 't': traversal = SWT.TRAVERSE_PAGE_NEXT; break;
		}
		if (traversal != SWT.NONE) {
			event.doit = true; /* this will be the Traverse event's initial doit value */
			canvas.traverse(traversal, event);
		}
	});
	canvas.addFocusListener(focusLostAdapter(e->canvas.redraw()));
	canvas.addFocusListener(focusGainedAdapter(e->canvas.redraw()));


	Text text2 = new Text(composite, SWT.SINGLE);
	Button button = new Button(childShell, SWT.PUSH);
	button.setText("Default &Button");
	button.addListener(SWT.Selection, event -> System.out.println("Default button selected"));
	childShell.setDefaultButton(button);

	Listener printTraverseListener = event -> {
		StringBuilder buffer = new StringBuilder("Traverse ");
		buffer.append(event.widget);
		buffer.append(" type=");
		switch (event.detail) {
			case SWT.TRAVERSE_ARROW_NEXT: buffer.append("TRAVERSE_ARROW_NEXT"); break;
			case SWT.TRAVERSE_ARROW_PREVIOUS: buffer.append("TRAVERSE_ARROW_NEXT"); break;
			case SWT.TRAVERSE_ESCAPE: buffer.append("TRAVERSE_ESCAPE"); break;
			case SWT.TRAVERSE_MNEMONIC: buffer.append("TRAVERSE_MNEMONIC"); break;
			case SWT.TRAVERSE_PAGE_NEXT: buffer.append("TRAVERSE_PAGE_NEXT"); break;
			case SWT.TRAVERSE_PAGE_PREVIOUS: buffer.append("TRAVERSE_PAGE_PREVIOUS"); break;
			case SWT.TRAVERSE_RETURN: buffer.append("TRAVERSE_RETURN"); break;
			case SWT.TRAVERSE_TAB_NEXT: buffer.append("TRAVERSE_TAB_NEXT"); break;
			case SWT.TRAVERSE_TAB_PREVIOUS: buffer.append("TRAVERSE_TAB_PREVIOUS"); break;
		}
		buffer.append(" doit=" + event.doit);
		buffer.append(" keycode=" + event.keyCode);
		buffer.append(" char=" + event.character);
		buffer.append(" stateMask=" + event.stateMask);
		System.out.println(buffer.toString());
	};
	childShell.addListener(SWT.Traverse, printTraverseListener);
	folder.addListener(SWT.Traverse, printTraverseListener);
	composite.addListener(SWT.Traverse, printTraverseListener);
	canvas.addListener(SWT.Traverse, printTraverseListener);
	button.addListener(SWT.Traverse, printTraverseListener);
	text1.addListener(SWT.Traverse, printTraverseListener);
	text2.addListener(SWT.Traverse, printTraverseListener);

	childShell.pack();
	childShell.open();
	text1.setFocus();
	while (!parentShell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}

}
