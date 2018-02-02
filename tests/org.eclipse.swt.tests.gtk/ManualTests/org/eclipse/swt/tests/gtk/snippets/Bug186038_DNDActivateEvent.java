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
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


/*
 * Title: Bug 186038 - [DND] On Linux-gtk, the SWT.Activate event is not fired to the popup shell during a dragging operation
 * How to run: launch snippet, drag source button to target.
 * Bug description: When the shell appears, clicking outside of it does not dismiss it
 * Expected results: The shell should be dismissed.
 * GTK Version(s): GTK2.x
 */
public class Bug186038_DNDActivateEvent {

	public static void main(String[] args) {

		Display display = new Display();
		final Shell shell = new Shell(display);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		shell.setLayout(layout);

		// create the drop down widget.shell
		final Shell dropDownShell = new Shell(shell, SWT.ON_TOP | SWT.DROP_DOWN);
		dropDownShell.setLayout(new RowLayout());
		dropDownShell.setVisible(false);

		dropDownShell.addListener(SWT.Activate, event -> System.out.println("dropDownShell gets Activate event!"));

		dropDownShell.addListener(SWT.Deactivate, event -> {
			System.out.println(
					"dropDownShell entering Deactivate event handler and will hide the dropdown widget.shell");
			hideDropDown(dropDownShell);
		});

		dropDownShell.addListener(SWT.Close, event -> hideDropDown(dropDownShell));

		// create the button1 and when it is hovered, display the dropdown
		final Button button1 = new Button(shell, SWT.PUSH);
		button1.setText("Drop target");
		button1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!dropDownShell.isVisible()) {
					showDropDown(button1, dropDownShell);
				}
			}
		});

		int operations = DND.DROP_COPY | DND.DROP_DEFAULT;
		DropTarget target = new DropTarget(button1, operations);
		// Provide data in Text format
		Transfer[] types = new Transfer[] { TextTransfer.getInstance() };
		target.setTransfer(types);

		target.addDropListener(new DropTargetListener() {
			@Override
			public void dragEnter(DropTargetEvent event) {
				if (event.detail == DND.DROP_DEFAULT) {
					if ((event.operations & DND.DROP_COPY) != 0) {
						event.detail = DND.DROP_COPY;
					} else {
						event.detail = DND.DROP_NONE;
					}
				}
				for (int i = 0; i < event.dataTypes.length; i++) {
					if (TextTransfer.getInstance().isSupportedType(event.dataTypes[i])) {
						event.currentDataType = event.dataTypes[i];
						if (event.detail != DND.DROP_COPY) {
							event.detail = DND.DROP_NONE;
						}
						break;
					}
				}
			}

			@Override
			public void dragOver(DropTargetEvent event) {
				event.feedback = DND.FEEDBACK_SELECT;

				if (!dropDownShell.isVisible()) {
					showDropDown(button1, dropDownShell);
				}
			}

			@Override
			public void dragOperationChanged(DropTargetEvent event) {

			}

			@Override
			public void dragLeave(DropTargetEvent event) {
			}

			@Override
			public void dropAccept(DropTargetEvent event) {
			}

			@Override
			public void drop(DropTargetEvent event) {
				if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
					String text = (String) event.data;
					System.out.println(text);
				}
			}
		});

		// create the button2 as the drag source
		final Button button2 = new Button(shell, SWT.PUSH);
		button2.setText("Drag source");

		operations = DND.DROP_COPY;
		DragSource source = new DragSource(button2, operations);

		// Provide data in Text format
		source.setTransfer(types);

		source.addDragListener(new DragSourceListener() {
			@Override
			public void dragStart(DragSourceEvent event) {
				if (button2.getText().length() == 0) {
					event.doit = false;
				}
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				// Provide the data of the requested type.
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					event.data = button2.getText();
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {

			}
		});

		shell.setSize(300, 300);
		shell.addDisposeListener(e -> {
			if (dropDownShell != null && !dropDownShell.isDisposed()) {
				dropDownShell.dispose();
			}
		});
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static void showDropDown(final Button button1, final Shell dropDownShell) {
		if (dropDownShell != null && !dropDownShell.isDisposed()) {
			dropDownShell.setText("This is a drop down widget.shell");
			dropDownShell.setSize(100, 200);
			Rectangle buttonRect = button1.getBounds();
			Point p = button1.getParent().toDisplay(new Point(buttonRect.x, buttonRect.y + buttonRect.height));
			dropDownShell.setLocation(p.x, p.y);
			dropDownShell.setVisible(true);
			dropDownShell.setFocus();
		}
	}

	private static void hideDropDown(final Shell dropDownShell) {
		dropDownShell.setVisible(false);
	}
}
