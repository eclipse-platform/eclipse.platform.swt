/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
 * Drag text between two StyledText widgets
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.2
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet210 {
	static String string1 = "A drag source is the provider of data in a Drag and Drop data transfer as well as "+
                           "the originator of the Drag and Drop operation. The data provided by the drag source "+
                           "may be transferred to another location in the same widget, to a different widget "+
                           "within the same application, or to a different application altogether. For example, "+
                           "you can drag text from your application and drop it on an email application, or you "+
                           "could drag an item in a tree and drop it below a different node in the same tree.";

	static String string2 = "A drop target receives data in a Drag and Drop operation. The data received by "+
	                        "the drop target may have come from the same widget, from a different widget within "+
	                        "the same application, or from a different application altogether. For example, you "+
	                        "can drag text from an email application and drop it on your application, or you could "+
	                        "drag an item in a tree and drop it below a different node in the same tree.";

public static void main (String [] args) {
	final Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());
	int style = SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER;
	final StyledText text1 = new StyledText(shell, style);
	text1.setText(string1);
	DragSource source = new DragSource(text1, DND.DROP_COPY | DND.DROP_MOVE);
	source.setTransfer(TextTransfer.getInstance());
	source.addDragListener(new DragSourceAdapter() {
		Point selection;
		@Override
		public void dragStart(DragSourceEvent e) {
			selection = text1.getSelection();
			e.doit = selection.x != selection.y;
		}
		@Override
		public void dragSetData(DragSourceEvent e) {
			e.data = text1.getText(selection.x, selection.y-1);
		}
		@Override
		public void dragFinished(DragSourceEvent e) {
			if (e.detail == DND.DROP_MOVE) {
				text1.replaceTextRange(selection.x, selection.y - selection.x, "");
			}
			selection = null;
		}
	});

	final StyledText text2 = new StyledText(shell, style);
	text2.setText(string2);
	DropTarget target = new DropTarget(text2, DND.DROP_DEFAULT | DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
	target.setTransfer(TextTransfer.getInstance());
	target.addDropListener(new DropTargetAdapter() {
		@Override
		public void dragEnter(DropTargetEvent e) {
			if (e.detail == DND.DROP_DEFAULT)
				e.detail = DND.DROP_COPY;
		}
		@Override
		public void dragOperationChanged(DropTargetEvent e) {
			if (e.detail == DND.DROP_DEFAULT)
				e.detail = DND.DROP_COPY;
		}
		@Override
		public void drop(DropTargetEvent e) {
			text2.insert((String)e.data);
		}
	});
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}