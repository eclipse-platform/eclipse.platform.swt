/*******************************************************************************
 * Copyright (c) 2019 Oleksandr Zakusylo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Oleksandr Zakusylo - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import java.awt.Frame;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JButton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Bug 548661 - [GTK] DND from SWT to embbedded AWT frame is broken. Verify that
 * How to run: launch snippet, drag the button "Drag Source" onto the button "Swing Target"
 * Bug description: no drag events are reported in the console.
 * Expected results: drag enter and drop/drag exit are reported according to user actions.
 */
public class Bug548661_AWTDragDrop {

	public static void main(String[] args) {
		Shell shell = new Shell(SWT.DIALOG_TRIM);
		shell.setLayout(new GridLayout(3, true));

		Button sourceButton = new Button(shell, SWT.PUSH);
		sourceButton.setText("Drag Source");

		Button targetButton = new Button(shell, SWT.PUSH);
		targetButton.setText("SWT Target");

		Composite swingContainer = new Composite(shell, SWT.EMBEDDED);
		Frame frame = SWT_AWT.new_Frame(swingContainer);

		JButton swingButton = new JButton("Swing target");
		frame.add(swingButton);

		shell.open();
		shell.pack();

		DragSource ds = new DragSource(sourceButton, DND.DROP_MOVE);
		ds.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		ds.addDragListener(new DragSourceAdapter() {
			@Override
			public void dragSetData(DragSourceEvent event) {
				event.data = "TRANSFERRED DATA STRING";
			}
		});

		DropTarget dt = new DropTarget(targetButton, DND.DROP_MOVE);
		dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dt.addDropListener(new DropTargetAdapter() {
			@Override
			public void dragEnter(DropTargetEvent event) {
				System.err.println("SWT drag enter");
			}

			@Override
			public void dragLeave(DropTargetEvent event) {
				System.err.println("SWT drag exit");
			}

			@Override
			public void drop(DropTargetEvent event) {
				System.err.println("SWT drop " + event.data);
			}
		});

		new java.awt.dnd.DropTarget(swingButton, new java.awt.dnd.DropTargetAdapter() {
			@Override
			public void dragEnter(java.awt.dnd.DropTargetDragEvent dtde) {
				System.err.println("AWT drag enter");
			}

			@Override
			public void dragExit(java.awt.dnd.DropTargetEvent dte) {
				System.err.println("AWT drag exit");
			}

			@Override
			public void drop(java.awt.dnd.DropTargetDropEvent dtde) {
				dtde.acceptDrop(java.awt.dnd.DnDConstants.ACTION_MOVE);
				try {
					Object data = dtde.getTransferable().getTransferData(DataFlavor.stringFlavor);
					System.err.println("AWT drop " + data);
				} catch (UnsupportedFlavorException | IOException e) {
					e.printStackTrace();
				}
			}
		});

		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
