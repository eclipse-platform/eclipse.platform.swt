package org.eclipse.swt.tests.manual;

/*
 * ToolBar example snippet: create a flat tool bar (images)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Snippet36 {

public static void main (String [] args) {
	Display display = new Display();
	Image image1 = new Image (display, 16, 16);
	Color color = display.getSystemColor (SWT.COLOR_RED);
	GC gc = new GC (image1);
	gc.setBackground (color);
	gc.fillRectangle (image1.getBounds ());
	gc.dispose ();
	Image image2 = new Image (display, 200, 200);
	gc = new GC (image2);
	gc.setBackground (color);
	gc.fillRectangle (image2.getBounds ());
	gc.dispose ();
	Shell shell = new Shell (display);
	shell.setText("Snippet 36");
	ToolBar toolBar = new ToolBar (shell, SWT.FLAT | SWT.BORDER);
	Rectangle clientArea = shell.getClientArea ();
	toolBar.setLocation (clientArea.x, clientArea.y);
	for (int i=0; i<12; i++) {
		int style = SWT.PUSH;
		ToolItem item = new ToolItem (toolBar, style);
		if (i%2 == 0)
			item.setImage (image2);
		else
			item.setImage (image1);
	}
	toolBar.pack ();
	shell.open ();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	image1.dispose ();
	display.dispose ();
}

}
