/*******************************************************************************
 * Copyright (c) 2007, 2017 IBM Corporation and others.
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
 * Drag text selection within a StyledText widget
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.3
 */
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet257 {

	static String string1 = "A drag source is the provider of data in a Drag and Drop data transfer as well as "+
                           "the originator of the Drag and Drop operation. The data provided by the drag source "+
                           "may be transferred to another location in the same widget, to a different widget "+
                           "within the same application, or to a different application altogether. For example, "+
                           "you can drag text from your application and drop it on an email application, or you "+
                           "could drag an item in a tree and drop it below a different node in the same tree.";

	static String DRAG_START_DATA = "DRAG_START_DATA";

public static void main (String [] args) {
	final Display display = new Display ();
	Shell shell = new Shell (display);
	shell.setLayout(new FillLayout());
	shell.setSize(100, 300);
	int style = SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER;
	final StyledText text = new StyledText(shell, style);
	text.setText(string1);
	final DragSource source = new DragSource(text, DND.DROP_COPY | DND.DROP_MOVE);
	source.setDragSourceEffect(new DragSourceEffect(text) {
		@Override
		public void dragStart(DragSourceEvent event) {
			event.image = display.getSystemImage(SWT.ICON_WARNING);
		}
	});
	source.setTransfer(TextTransfer.getInstance());
	source.addDragListener(new DragSourceAdapter() {
		Point selection;
		@Override
		public void dragStart(DragSourceEvent event) {
			selection = text.getSelection();
			event.doit = selection.x != selection.y;
			text.setData(DRAG_START_DATA, selection);
		}
		@Override
		public void dragSetData(DragSourceEvent e) {
			e.data = text.getText(selection.x, selection.y-1);
		}
		@Override
		public void dragFinished(DragSourceEvent event) {
			if (event.detail == DND.DROP_MOVE) {
				Point newSelection= text.getSelection();
				int length = selection.y - selection.x;
				int delta = 0;
				if (newSelection.x < selection.x)
					delta = length;
				text.replaceTextRange(selection.x + delta, length, "");
			}
			selection = null;
			text.setData(DRAG_START_DATA, null);
		}
	});

	DropTarget target = new DropTarget(text, DND.DROP_DEFAULT | DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
	target.setTransfer(TextTransfer.getInstance());
	target.addDropListener(new DropTargetAdapter() {
		@Override
		public void dragEnter(DropTargetEvent event) {
			if (event.detail == DND.DROP_DEFAULT) {
				if (text.getData(DRAG_START_DATA) == null)
					event.detail = DND.DROP_COPY;
				else
					event.detail = DND.DROP_MOVE;
			}
		}
		@Override
		public void dragOperationChanged(DropTargetEvent event) {
			if (event.detail == DND.DROP_DEFAULT) {
				if (text.getData(DRAG_START_DATA) == null)
					event.detail = DND.DROP_COPY;
				else
					event.detail = DND.DROP_MOVE;
			}
		}
		@Override
		public void dragOver(DropTargetEvent event) {
			event.feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_SELECT;
		}
		@Override
		public void drop(DropTargetEvent event) {
			if (event.detail != DND.DROP_NONE) {
				Point selection = (Point) text.getData(DRAG_START_DATA);
				int insertPos = text.getCaretOffset();
				if (event.detail == DND.DROP_MOVE && selection != null && selection.x <= insertPos  && insertPos <= selection.y
						|| event.detail == DND.DROP_COPY && selection != null && selection.x < insertPos  && insertPos < selection.y) {
					text.setSelection(selection);
					event.detail = DND.DROP_COPY;  // prevent source from deleting selection
				} else {
					String string = (String)event.data;
					text.insert(string);
					if (selection != null)
						text.setSelectionRange(insertPos, string.length());
				}
			}
		}
	});
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

}
