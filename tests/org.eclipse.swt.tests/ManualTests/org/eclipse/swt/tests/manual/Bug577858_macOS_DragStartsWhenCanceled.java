/*******************************************************************************
 * Copyright (c) 2021, 2022 Syntevo and others.
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
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public final class Bug577858_macOS_DragStartsWhenCanceled {
	public static void main(String[] args) {
		final Display display = new Display ();

		final Shell shell = new Shell (display);
		shell.setLayout (new GridLayout (1, true));

		Label hint = new Label (shell, 0);
		hint.setText (
			"1) Use macOS\n" +
			"2) Try to drag first item of Tree/Table\n" +
			"3) Bug 577858: Snippet cancels drag, but drag starts anyway.\n" +
			"   This causes unexpected DND.DragEnd event.\n" +
			"   Also, drag&&drop fails when dropped.\n" +
			"See console for debug output."
		);

		Composite composite = new Composite (shell, 0);
		composite.setLayout (new GridLayout (2, true));

		new Label (composite, 0).setText ("Table");
		new Label (composite, 0).setText ("Tree");

		// Table
		{
			Table control = new Table (composite, SWT.BORDER);
			new TableItem (control, 0).setText ("I'm canceling drag&drop");
			new TableItem (control, 0).setText ("I'm a good item #2");
			new TableItem (control, 0).setText ("I'm a good item #3");

			DragSource dragSource = new DragSource (control, DND.DROP_MOVE | DND.DROP_COPY);
			dragSource.setTransfer (TextTransfer.getInstance ());
			dragSource.addDragListener (new DragSourceListener () {
				TableItem getSelectedItem() {
					return control.getSelection ()[0];
				}

				boolean isCancel() {
					return 0 == control.indexOf (getSelectedItem ());
				}

				@Override
				public void dragStart(DragSourceEvent event) {
					if (isCancel ())
						event.doit = false;
				}

				@Override
				public void dragSetData(DragSourceEvent event) {
					if (isCancel ())
						System.out.println ("ERROR: Unexpected DND.DragSetData event!");

					event.data = getSelectedItem ().getText ();
				}

				@Override
				public void dragFinished(DragSourceEvent event) {
					if (isCancel ())
						System.out.println ("ERROR: Unexpected DND.DragEnd event!");
				}
			});
		}

		// Tree
		{
			Tree control = new Tree (composite, SWT.BORDER);
			new TreeItem (control, 0).setText ("I'm canceling drag&drop");
			new TreeItem (control, 0).setText ("I'm a good item #2");
			new TreeItem (control, 0).setText ("I'm a good item #3");

			DragSource dragSource = new DragSource (control, DND.DROP_MOVE | DND.DROP_COPY);
			dragSource.setTransfer (TextTransfer.getInstance ());
			dragSource.addDragListener (new DragSourceListener () {
				TreeItem getSelectedItem() {
					return control.getSelection ()[0];
				}

				boolean isCancel() {
					return 0 == control.indexOf (getSelectedItem ());
				}

				@Override
				public void dragStart(DragSourceEvent event) {
					if (isCancel ())
						event.doit = false;
				}

				@Override
				public void dragSetData(DragSourceEvent event) {
					if (isCancel ())
						System.out.println ("ERROR: Unexpected DND.DragSetData event!");

					event.data = getSelectedItem ().getText ();
				}

				@Override
				public void dragFinished(DragSourceEvent event) {
					if (isCancel ())
						System.out.println ("ERROR: Unexpected DND.DragEnd event!");
				}
			});
		}

		Label label = new Label (shell, SWT.BORDER);
		label.setText ("\n   [ Drop dragged item here ]  \n\n");

		DropTarget dropTarget = new DropTarget (label, DND.DROP_MOVE | DND.DROP_COPY);
		dropTarget.setTransfer (TextTransfer.getInstance ());
		dropTarget.addListener (DND.Drop, e -> {
			System.out.println ("Data dropped: " + e.data);
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
