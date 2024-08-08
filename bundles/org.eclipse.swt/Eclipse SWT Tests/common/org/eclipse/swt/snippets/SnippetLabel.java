package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SnippetLabel {

	public static void main (String[] args) {
		Display display = new Display();
		Image image = new Image (display, 16, 16);
		Color color = display.getSystemColor (SWT.COLOR_RED);
		GC gc = new GC (image);
		gc.setBackground (color);
		gc.fillRectangle (image.getBounds ());
		gc.dispose ();
		Shell shell = new Shell (display);
		shell.setText("SnippetLabel");

		Label label = new Label(shell, SWT.BORDER);

		label.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

		label.setText("Test...");
		Rectangle clientArea = shell.getClientArea ();
		label.setLocation (clientArea.x, clientArea.y);
		label.setImage (image);
		label.pack ();
		shell.pack ();
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		image.dispose ();
		display.dispose ();
	}

}
