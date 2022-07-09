/*******************************************************************************
 * Copyright (c) 2022 Syntevo and others.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Issue0247_WaylandDragDropGetsStuck {
	public static void main(String[] args) {
		final Display display = new Display ();

		final Shell shell = new Shell (display);
		shell.setLayout (new GridLayout (1, true));

		Label hint = new Label (shell, 0);
		hint.setText (
			"1) Use Linux with Wayland\n" +
			"2) Drag TreeItem\n" +
			"3) Issue 0247: Dragging begins immediately when mouse is moved without any move threshold\n" +
			"4) Move mouse over TreeItem slowly and double-click once in a while\n" +
			"5) Issue 0247: Sometimes, counter will grow, indicating that there's an unfinished drag.\n" +
			"   You will also see it by DRAGGING indicator being stuck.\n"
		);

		Tree control = new Tree (shell, SWT.BORDER);
		for (int i = 0; i < 5; i++) {
			new TreeItem(control, 0).setText("TreeItem #" + i);
		}

		Label labelIsDrag = new Label(shell, 0);
		labelIsDrag.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		final int[] numActiveDrags = new int[1];
		DragSource dragSource = new DragSource (control, DND.DROP_MOVE | DND.DROP_COPY);
		dragSource.setTransfer (TextTransfer.getInstance ());
		dragSource.addDragListener (new DragSourceListener() {
			@Override
			public void dragStart(DragSourceEvent event) {
				labelIsDrag.setText("DRAGGING");
				numActiveDrags[0]++;
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				event.data = "Data";
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				numActiveDrags[0]--;
				labelIsDrag.setText("" + numActiveDrags[0]);
			}
		});

		shell.pack ();
		shell.open ();

		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) {
				display.sleep ();
			}
		}

		display.dispose ();
	}
}