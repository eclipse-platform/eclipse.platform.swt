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
 * Drag and Drop example snippet: define a default operation (in this example, Copy)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet84 {
	
public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	
	final Label label = new Label(shell, SWT.BORDER);
	label.setText("Drag Source");
	DragSource source = new DragSource(label, DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK);
	source.setTransfer(new Transfer[] {TextTransfer.getInstance()});
	source.addDragListener(new DragSourceAdapter() {
		public void dragSetData(DragSourceEvent event) {
			event.data = "Text Transferred";
		}
		public void dragFinished(DragSourceEvent event) {
			if (event.doit) {
				String operation = null;
				switch (event.detail) {
				case DND.DROP_MOVE:
					operation = "moved"; break;
				case DND.DROP_COPY:
					operation = "copied"; break;
				case DND.DROP_LINK:
					operation = "linked"; break;
				case DND.DROP_NONE:
					operation = "disallowed"; break;
				default:
					operation = "unknown"; break;
				}
				label.setText("Drag Source (data "+operation+")");
			} else {
				label.setText("Drag Source (drag cancelled)");
			}
		}
	});

	final Text text = new Text(shell, SWT.BORDER | SWT.MULTI);
	text.setText("Drop Target");
	DropTarget target = new DropTarget(text, DND.DROP_DEFAULT | DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK);
	target.setTransfer(new Transfer[] {TextTransfer.getInstance()});
	target.addDropListener(new DropTargetAdapter() {
		public void dragEnter(DropTargetEvent event) {
			if (event.detail == DND.DROP_DEFAULT) event.detail = DND.DROP_COPY;
		}
		public void dragOperationChanged(DropTargetEvent event) {
			if (event.detail == DND.DROP_DEFAULT) event.detail = DND.DROP_COPY;
		}
		public void drop(DropTargetEvent event) {
			String operation = null;
			switch (event.detail) {
			case DND.DROP_MOVE:
				operation = "moved"; break;
			case DND.DROP_COPY:
				operation = "copied"; break;
			case DND.DROP_LINK:
				operation = "linked"; break;
			case DND.DROP_NONE:
				operation = "disallowed"; break;
			default:
				operation = "unknown"; break;
			}
			text.append("\n"+operation+(String)event.data);
		}
	});
	
	shell.setSize(400, 400);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
}
