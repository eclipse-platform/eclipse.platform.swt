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

/*
 * Drag and Drop example snippet: drag a URL between two labels.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.URLTransfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Bug 307441 - [DND] DnD on Linux does not behave as expected with overlapping controls
 * How to run: launch snippet and drag controls from left side to right, watch console for print statements
 * Bug description: Improper events for overlapped controls
 * Expected results: All events should be fired correctly
 * GTK Version(s): all
 */
public class Bug307441_DnDOverlappingControls {

public static void main (String [] args) {

	Display display = new Display ();
	final Shell shell = new Shell (display);
	shell.setText("URLTransfer");
	shell.setLayout(new FillLayout());
	final Label label1 = new Label (shell, SWT.BORDER);
	label1.setText ("http://www.eclipse.org");

	final Composite dropComp = new Composite(shell, SWT.BORDER);
	dropComp.setSize(600,300);
	Rectangle clientArea = dropComp.getClientArea();

	final Label label2 = new Label (dropComp, SWT.BORDER);
	label2.setBackground(new Color(255,255,0));
	label2.setText("DropLabel1");
	label2.setSize(100,100);
	label2.setBounds(clientArea.x, clientArea.y, clientArea.width, (clientArea.height*2)/3);

	final Label label3 = new Label (dropComp, SWT.BORDER);
	label3.setBackground(new Color(255, 0, 0));
	label3.setText("DropLabel2");
	label3.setSize(100,100);
	label3.setBounds(clientArea.x+10, clientArea.y+clientArea.height/3, clientArea.width-10, clientArea.height/2);
	label3.moveAbove(label2);

	setDragSource (label1);
	setDropTarget (dropComp);
	setDropTargetForLabel (label2);
	setDropTargetForLabel (label3);

	shell.setSize(900, 300);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}

public static void setDragSource (final Label label) {
	int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
	final DragSource source = new DragSource (label, operations);
	source.setTransfer(new Transfer[] {URLTransfer.getInstance()});
	source.addDragListener (new DragSourceListener () {
		@Override
		public void dragStart(DragSourceEvent e) {
		}
		@Override
		public void dragSetData(DragSourceEvent e) {
			e.data = label.getText();
		}
		@Override
		public void dragFinished(DragSourceEvent event) {
		}
	});
}

public static void setDropTargetForLabel (final Label control) {
	int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
	DropTarget target = new DropTarget(control, operations);
	target.setTransfer(new Transfer[] {URLTransfer.getInstance()});
	target.addDropListener (new DropTargetListener() {
		@Override
		public void dragEnter(DropTargetEvent e) {
			System.out.println("dragEnter="+control.getText());
			if (e.detail == DND.DROP_NONE)
				e.detail = DND.DROP_LINK;
		}
		@Override
		public void dragOperationChanged(DropTargetEvent e) {
			if (e.detail == DND.DROP_NONE)
				e.detail = DND.DROP_LINK;
		}
		@Override
		public void drop(DropTargetEvent event) {
			if (event.data == null) {
				event.detail = DND.DROP_NONE;
				return;
			}
			control.setText(((String) event.data));

		}
		@Override
		public void dragLeave(DropTargetEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("dragLeave="+control.getText());

		}
		@Override
		public void dragOver(DropTargetEvent arg0) {
			// TODO Auto-generated method stub

		}
		@Override
		public void dropAccept(DropTargetEvent arg0) {
			// TODO Auto-generated method stub

		}
	});
}
public static void setDropTarget (final Control control) {
	int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
	DropTarget target = new DropTarget(control, operations);
	target.setTransfer(new Transfer[] {URLTransfer.getInstance()});
	target.addDropListener (new DropTargetListener() {
		@Override
		public void dragEnter(DropTargetEvent e) {
			System.out.println("dragEnter=composite");

			if (e.detail == DND.DROP_NONE)
				e.detail = DND.DROP_LINK;
		}
		@Override
		public void dragOperationChanged(DropTargetEvent e) {
			if (e.detail == DND.DROP_NONE)
				e.detail = DND.DROP_LINK;
		}
		@Override
		public void drop(DropTargetEvent event) {
			if (event.data == null) {
				event.detail = DND.DROP_NONE;
				return;
			}
			control.setBackground(new Color(0,0,255));
		}
		@Override
		public void dragLeave(DropTargetEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println("dragLeave=composite");

		}
		@Override
		public void dragOver(DropTargetEvent arg0) {
			// TODO Auto-generated method stub

		}
		@Override
		public void dropAccept(DropTargetEvent arg0) {
			// TODO Auto-generated method stub

		}
	});
}
}