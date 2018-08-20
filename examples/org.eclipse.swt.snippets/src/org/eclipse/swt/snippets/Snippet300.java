/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
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
 * example snippet: drag and drop text between SWT and Swing
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.0
 */
import javax.swing.*;

import org.eclipse.swt.*;
import org.eclipse.swt.awt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet300 {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("SWT and Swing DND Example");
		GridLayout layout = new GridLayout(1, false);
		shell.setLayout(layout);

		Text swtText = new Text(shell, SWT.BORDER);
		swtText.setText("SWT Text");
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		swtText.setLayoutData(data);
		setDragDrop(swtText);

		Composite comp = new Composite(shell, SWT.EMBEDDED);
		java.awt.Frame frame = SWT_AWT.new_Frame(comp);
		JTextField swingText = new JTextField(40);
		swingText.setText("Swing Text");
		swingText.setDragEnabled(true);
		frame.add(swingText);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.heightHint = swingText.getPreferredSize().height;
		comp.setLayoutData(data);

		shell.setSize(400, 200);
		shell.open();
		while(!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
	public static void setDragDrop (final Text text) {
		Transfer[] types = new Transfer[] {TextTransfer.getInstance()};
		int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;

		final DragSource source = new DragSource (text, operations);
		source.setTransfer(types);
		source.addDragListener (new DragSourceListener () {
			Point selection;
			@Override
			public void dragStart(DragSourceEvent e) {
				selection = text.getSelection();
				e.doit = selection.x != selection.y;
			}
			@Override
			public void dragSetData(DragSourceEvent e) {
				e.data = text.getText(selection.x, selection.y-1);
			}
			@Override
			public void dragFinished(DragSourceEvent e) {
				if (e.detail == DND.DROP_MOVE) {
					text.setSelection(selection);
					text.insert("");
				}
				selection = null;
			}
		});

		DropTarget target = new DropTarget(text, operations);
		target.setTransfer(types);
		target.addDropListener (new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event) {
				if (event.data == null) {
					event.detail = DND.DROP_NONE;
					return;
				}
				text.append((String) event.data);
			}
		});
	}
}
