/*******************************************************************************
 * Copyright (c) 2023 Syntevo and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Syntevo - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.tests.manual;

import org.eclipse.swt.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public final class Issue0400_WaylandDifferentDndMouseEvents {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout (new GridLayout (1, true));

		TabFolder tabFolder = new TabFolder(shell, 0);

		// Problem #1
		{
			TabItem tabItem = new TabItem(tabFolder, 0);
			tabItem.setText("Problem #1");
			Composite tab = new Composite(tabFolder, 0);
			tab.setLayout (new GridLayout (1, true));
			tabItem.setControl(tab);

			Label hint = new Label(tab, 0);
			hint.setText(
				"1. Drag&drop between two rectangles below\n" +
				"2. On Windows, macOS and GTK X11, drag&&drop events are:\n" +
				"   MouseDown, dragStart, dragEnter, dragOver ... dragOver, dragLeave, dropAccept, dragSetData, drop, dragFinished\n" +
				"3. Issue 400: On Linux Wayland, initial `MouseDown` is missing\n" +
				"4. Issue 400: On Linux Wayland, there is an extra `MouseUp` at the end\n"
			);

			for (int iText = 0; iText < 2; iText++) {
				Composite control = new Composite(tab, SWT.BORDER);
				control.addListener(SWT.MouseDown, event -> {
					System.out.println("MouseDown:                              " + event);
				});
				control.addListener(SWT.MouseUp, event -> {
					System.out.println("MouseUp:                                " + event);
				});
				control.addListener(SWT.MenuDetect, event -> {
					System.out.println("MenuDetect:                             " + event);
				});

				final DragSource dragSource = new DragSource(control, DND.DROP_MOVE);
				dragSource.setTransfer(TextTransfer.getInstance());
				dragSource.addDragListener(new DragSourceAdapter() {
					@Override
					public void dragStart(DragSourceEvent event) {
						System.out.println("DragSourceListener.dragStart            " + event);
					}

					@Override
					public void dragFinished(DragSourceEvent event) {
						System.out.println("DragSourceListener.dragFinished         " + event);
					}

					@Override
					public void dragSetData(DragSourceEvent event) {
						System.out.println("DragSourceListener.dragSetData          " + event);
						event.data = "hello world";
					}
				});

				final DropTarget target = new DropTarget(control, DND.DROP_MOVE);
				target.setTransfer(TextTransfer.getInstance());

				target.addDropListener(new DropTargetListener() {
					@Override
					public void dragEnter(DropTargetEvent event) {
						System.out.println("DropTargetListener.dragEnter            " + event);
					}

					@Override
					public void dragLeave(DropTargetEvent event) {
						System.out.println("DropTargetListener.dragLeave            " + event);
					}

					@Override
					public void dragOperationChanged(DropTargetEvent event) {
						System.out.println("DropTargetListener.dragOperationChanged " + event);
					}

					@Override
					public void dragOver(DropTargetEvent event) {
						System.out.println("DropTargetListener.dragOver             " + event);
					}

					@Override
					public void drop(DropTargetEvent event) {
						System.out.println("DropTargetListener.drop                 " + event);
					}

					@Override
					public void dropAccept(DropTargetEvent event) {
						System.out.println("DropTargetListener.dropAccept           " + event);
					}
				});
			}
		}

		// Problem #2
		{
			TabItem tabItem = new TabItem(tabFolder, 0);
			tabItem.setText("Problem #2");
			Composite tab = new Composite(tabFolder, 0);
			tab.setLayout (new GridLayout (1, true));
			tabItem.setControl(tab);

			Label hint = new Label(tab, 0);
			hint.setText(
				"1. Press left mouse button in rectangle below\n" +
				"2. Without releasing the mouse button, move mouse\n" +
				"3. On Windows, macOS and GTK X11, events are:\n" +
				"   MouseDown, MouseMove ... MouseMove, MouseUp\n" +
				"4. Issue 400: On Linux Wayland, mouse events only arrive when mouse button is released\n" +
				"5. Issue 400: On Linux Wayland, many `MouseMove` events are lost when they finally arrive\n"
			);

			Composite composite = new Composite(tab, SWT.BORDER);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			boolean[] isMouseDown = new boolean[1];
			composite.addListener(SWT.MouseDown,  event -> {
				System.out.println("MouseDown:                              " + event);
				isMouseDown[0] = true;
			});
			composite.addListener(SWT.MouseMove,  event -> {
				if (!isMouseDown[0]) return;
				System.out.println("MouseMove with button down:             " + event);
			});
			composite.addListener(SWT.MouseUp,    event -> {
				System.out.println("MouseUp:                                " + event);
				isMouseDown[0] = false;
			});

			// 'DragDetect' listener is necessary to trigger the problem
			composite.addListener(SWT.DragDetect, event -> {
			});
		}

		// Problem #3
		{
			TabItem tabItem = new TabItem(tabFolder, 0);
			tabItem.setText("Problem #3");
			Composite tab = new Composite(tabFolder, 0);
			tab.setLayout (new GridLayout (1, true));
			tabItem.setControl(tab);

			Label hint = new Label(tab, 0);
			hint.setText(
				"1. Press left mouse button in rectangle below\n" +
				"2. Without releasing the mouse button, move mouse *slowly*\n" +
				"3. On Windows, macOS and GTK X11, events arrive immediately.\n" +
				"4. Issue 400: On Linux Wayland, first events arrive with delay,\n" +
				"   because SWT tries to detect drag&drop, even though noone is interested\n" +
				"   in such events"
			);

			Composite composite = new Composite(tab, SWT.BORDER);
			composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

			boolean[] isMouseDown = new boolean[1];
			composite.addListener(SWT.MouseDown,  event -> {
				System.out.println("MouseDown:                              " + event);
				isMouseDown[0] = true;
			});
			composite.addListener(SWT.MouseMove,  event -> {
				if (!isMouseDown[0]) return;
				System.out.println("MouseMove with button down:             " + event);
			});
			composite.addListener(SWT.MouseUp,    event -> {
				System.out.println("MouseUp:                                " + event);
				isMouseDown[0] = false;
			});
		}

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
