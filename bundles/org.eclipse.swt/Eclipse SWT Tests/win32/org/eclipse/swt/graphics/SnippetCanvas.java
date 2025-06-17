package org.eclipse.swt.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetCanvas {

	public static void main (String [] args) {
		Display display = new Display ();
		Shell shell = new Shell(display);
		shell.setText("Snippet 1");

		shell.setLayout(new FillLayout());

		Canvas c = new Canvas(shell, SWT.FILL);

		c.setSize(100,100);
		c.setBackground(c.getDisplay().getSystemColor(SWT.COLOR_CYAN));
		c.addListener(SWT.Paint, e -> onPaint( e));

		shell.addListener(SWT.Resize, e -> onResize( e, c));



		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

	private static void onResize(Event e,Canvas c) {


		var ca = c.getShell().getClientArea();

		c.setSize(ca.width, ca.height);


	}

	private static void onPaint(Event e) {



		e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_DARK_GREEN));

		e.gc.fillRectangle(50,50,400,400 );

	}

}
