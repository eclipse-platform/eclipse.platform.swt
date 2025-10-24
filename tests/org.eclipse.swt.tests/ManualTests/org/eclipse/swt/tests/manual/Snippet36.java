package org.eclipse.swt.tests.manual;

/*
 * ToolBar example snippet: create a flat tool bar (images)
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageGcDrawer;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Snippet36 {

public static void main (String [] args) {
	Display display = new Display();

	Color color = display.getSystemColor (SWT.COLOR_RED);
	ImageGcDrawer imageGcDrawer = (gc, imageWidth, imageHeight) -> {
		gc.setBackground (color);
		gc.fillRectangle (new Rectangle(0,0,imageWidth, imageHeight));
	};
	Image image1 = new Image (display, imageGcDrawer, 16, 16);

	imageGcDrawer = (gc, imageWidth, imageHeight) -> {
		gc.setBackground (color);
		gc.fillRectangle (new Rectangle(0,0,imageWidth, imageHeight));
	};
	Image image2 = new Image (display, imageGcDrawer, 200, 200);
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
