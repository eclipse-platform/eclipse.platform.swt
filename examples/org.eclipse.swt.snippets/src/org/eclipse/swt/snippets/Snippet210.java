/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
 * Dragging text in a StyledText widget
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
	source.setTransfer(new Transfer[] {TextTransfer.getInstance()});
	source.addDragListener(new DragSourceAdapter() {
		public void dragStart(DragSourceEvent e) {
			Point selection = text1.getSelection();
			try {
				int offset = text1.getOffsetAtLocation(new Point(e.x, e.y));
				e.doit = offset > selection.x && offset < selection.y;
			} catch (IllegalArgumentException ex) {
			}
		}
		public void dragSetData(DragSourceEvent e) {
			Point selection = text1.getSelection();
			if (selection.x != selection.y) {
				e.data = text1.getText(selection.x, selection.y-1);
			}
		}
		public void dragFinished(DragSourceEvent e) {
			if (e.detail == DND.DROP_MOVE) {
				text1.insert("");
			}
		}
	});
	
	final StyledText text2 = new StyledText(shell, style);
	text2.setText(string2);
	DropTarget target = new DropTarget(text2, DND.DROP_DEFAULT | DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
	target.setTransfer(new Transfer[] {TextTransfer.getInstance()});
	target.addDropListener(new DropTargetAdapter() {
		public void dragEnter(DropTargetEvent e) {
			if (e.detail == DND.DROP_DEFAULT)
				e.detail = DND.DROP_COPY;
		}
		public void dragOperationChanged(DropTargetEvent e) {
			if (e.detail == DND.DROP_DEFAULT)
				e.detail = DND.DROP_COPY;
		}
		public void dragOver(DropTargetEvent e) {
			text2.setFocus();
			Point location = display.map(null, text2, e.x, e.y);
			location.x = Math.max(0, location.x);
			location.y = Math.max(0, location.y);
			try {
				int offset = text2.getOffsetAtLocation(new Point(location.x, location.y));
				text2.setCaretOffset(offset);
			} catch (IllegalArgumentException ex) {
				int maxOffset = text2.getCharCount();
				Point maxLocation = text2.getLocationAtOffset(maxOffset);
				if (location.y >= maxLocation.y) {
					if (location.x >= maxLocation.x) {
						text2.setCaretOffset(maxOffset);
					} else {
						int offset = text2.getOffsetAtLocation(new Point(location.x, maxLocation.y));
						text2.setCaretOffset(offset);
					}
				} else {
					text2.setCaretOffset(maxOffset);
				}
			}
			
		}
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