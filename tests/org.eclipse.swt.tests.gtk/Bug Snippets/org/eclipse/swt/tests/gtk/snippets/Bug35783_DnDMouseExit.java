package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/*
 * Title: Bug 35783 - [DND] Sometimes GTK sends mouse exit during native Drag [portability]
 * How to run: Run the snippet and drag quickly
 * Bug description: MouseExit events are fired when dragging
 * Expected results: No MouseExit events should be fired
 * GTK Version(s): ALL
 */
public class Bug35783_DnDMouseExit {
	public static void main(String[] args) {
		final Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.addListener(SWT.MouseExit, event -> System.out.println("exit"));
		shell.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				System.out.println("down");
			}
			@Override
			public void mouseUp(MouseEvent e) {
				System.out.println("up");
			}
		});
		DragSource ds = new DragSource(shell, DND.DROP_COPY);
		ds.setTransfer(new Transfer[] {TextTransfer.getInstance()});
		ds.addDragListener(new DragSourceAdapter() {
			@Override
			public void dragStart(DragSourceEvent event) {
				System.out.println("Drag Start");
			}
		});
		shell.open();
		while (!shell.isDisposed())
			if (!display.readAndDispatch())
				display.sleep();
	}
}
