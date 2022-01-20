/*******************************************************************************
 * Copyright (c) 2021 Syntevo and others.
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
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public final class Bug572242_LeakInTreeDragSourceEffect {
	interface ControlType {
		Control createControl(Composite composite);
		Item    createItem(String text);
		Item    getSelectedItem();
	}

	public static void main(String[] args) {
		// Detect Resource leaks
		Resource.setNonDisposeHandler (error -> error.printStackTrace ());

		final Display display = new Display();

		final Shell shellMain = new Shell(display);
		shellMain.setLayout (new GridLayout (1, true));

		Label hint = new Label (shellMain, 0);
		hint.setText (
			"1) Use Windows / macOS / Linux\n" +
			"   on macOS, patch for Bug 577858 is needed\n" +
			"2) Click button below\n" +
			"3) Try to drag first item of Tree/Table\n" +
			"   3a) Drag will be cancelled and will not start\n" +
			"   3b) Close test Shell\n" +
			"   3c) Bug 572242: there is an Image leak (see console)\n" +
			"4) Try to drag second item of Tree/Table\n" +
			"   4a) Drag will be cancelled and will not start\n" +
			"   4b) Close test Shell\n" +
			"   4c) Bug 572242: there is an Image leak (see console)"
		);

		Button btnOpenTestShell = new Button (shellMain, SWT.PUSH);
		btnOpenTestShell.setText ("Click me");
		btnOpenTestShell.addListener (SWT.Selection, e2 -> {
			final Shell shell = new Shell(shellMain, SWT.DIALOG_TRIM);
			shell.setLayout (new GridLayout (2, true));

			// Trigger GC after DragSource is disposed to invoke leak detector
			shell.addListener (SWT.Dispose, e -> display.asyncExec (() -> System.gc()));

			new Label(shell, 0).setText ("Table");
			new Label(shell, 0).setText ("Tree");

			ControlType[] controlTypes = new ControlType[] {
				new ControlType() {
					Table control;

					@Override
					public Control createControl(Composite composite) {
						control = new Table (shell, SWT.BORDER);
						return control;
					}

					@Override
					public Item createItem(String text) {
						TableItem item = new TableItem(control, 0);
						item.setText(text);
						return item;
					}

					@Override
					public Item getSelectedItem() {
						return control.getSelection ()[0];
					}
				},

				new ControlType() {
					Tree control;

					@Override
					public Control createControl(Composite composite) {
						control = new Tree (shell, SWT.BORDER);
						return control;
					}

					@Override
					public Item createItem(String text) {
						TreeItem item = new TreeItem(control, 0);
						item.setText(text);
						return item;
					}

					@Override
					public Item getSelectedItem() {
						return control.getSelection ()[0];
					}
				},
			};

			for (ControlType controlType : controlTypes) {
				Control control = controlType.createControl(shell);
				Item itemCancelStart   = controlType.createItem("Drag me -> leak via cancel in dragStart()");
				Item itemNoAgents      = controlType.createItem("Drag me -> leak via no transfer agents");
				Item itemCancelSetData = controlType.createItem("Drag me -> cancel in dragSetData()");
				/* Item itemRegular = */ controlType.createItem("Drag me -> ok");

				DragSource dragSource = new DragSource (control, DND.DROP_MOVE | DND.DROP_COPY);
				dragSource.addDragListener (new DragSourceListener () {
					@Override
					public void dragStart(DragSourceEvent event) {
						if (itemCancelStart == controlType.getSelectedItem())
							event.doit = false;

						if (itemNoAgents == controlType.getSelectedItem())
							dragSource.setTransfer ((Transfer[])null);
						else
							dragSource.setTransfer (TextTransfer.getInstance ());
					}

					@Override
					public void dragSetData(DragSourceEvent event) {
						event.data = controlType.getSelectedItem().getText();

						if (itemCancelSetData == controlType.getSelectedItem())
							event.doit = false;
					}

					@Override
					public void dragFinished(DragSourceEvent event) {
					}
				});
			}

			Label label = new Label(shell, SWT.BORDER);
			label.setText ("\n   [ Drop dragged item here ]  \n\n");

			DropTarget dropTarget = new DropTarget(label, DND.DROP_MOVE | DND.DROP_COPY);
			dropTarget.setTransfer(TextTransfer.getInstance());
			dropTarget.addListener (DND.Drop, e -> {
				System.out.println ("Data dropped: " + e.data);
			});

			shell.pack();
			shell.open();
		});

		shellMain.pack();
		shellMain.open();

		while (!shellMain.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
