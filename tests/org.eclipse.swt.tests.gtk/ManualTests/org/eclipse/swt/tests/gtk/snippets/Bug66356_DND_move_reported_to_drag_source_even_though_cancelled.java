/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Bug 66356 - DND - move reported to drag source even though cancelled
 * How to run: launch snippet, click and drag from the source button back onto it
 * Bug description: Move = true is printed
 * Expected results: Move = false should be printed if not dragging beyond the source button
 * GTK Version(s): 2.x
 */
public class Bug66356_DND_move_reported_to_drag_source_even_though_cancelled {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Button source = new Button(shell, SWT.PUSH);
		source.setText("Source");
		DragSource dragSource = new DragSource(source, DND.DROP_MOVE);
		dragSource.setTransfer(new Transfer[] {TextTransfer.getInstance()});
		dragSource.addDragListener(new DragSourceAdapter() {
			@Override
			public void dragSetData(DragSourceEvent event) {
				event.data = "hello";
			}
			@Override
			public void dragFinished(DragSourceEvent event) {
				System.out.println("Move = "+(event.detail == DND.DROP_MOVE));
			}
		});
		Button dest = new Button(shell, SWT.PUSH);
		dest.setText("Dest");
		DropTarget dropTarget = new DropTarget(dest, DND.DROP_MOVE);
		dropTarget.setTransfer(new Transfer[] {TextTransfer.getInstance()});
//		dropTarget.addDropListener(new DropTargetAdapter() {
//			public void drop(DropTargetEvent event) {
//				event.detail = DND.DROP_NONE;
//			}
//		});
//		dropTarget.addDropListener(new DropTargetListener() {
//
//			@Override
//			public void dropAccept(DropTargetEvent event) {
//				// TD Auto-generated method stub
//				event.detail = DND.DROP_NONE;
//
//			}
//
//			@Override
//			public void drop(DropTargetEvent event) {
//				// TD Auto-generated method stub
//
//			}
//
//			@Override
//			public void dragOver(DropTargetEvent event) {
//				// TD Auto-generated method stub
//
//			}
//
//			@Override
//			public void dragOperationChanged(DropTargetEvent event) {
//				// TD Auto-generated method stub
//
//			}
//
//			@Override
//			public void dragLeave(DropTargetEvent event) {
//				// TD Auto-generated method stub
//
//			}
//
//			@Override
//			public void dragEnter(DropTargetEvent event) {
//				// TD Auto-generated method stub
//
//			}
//		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
