package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SkijaDrawImage {

	public static void main (String [] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SkijaTest");
		shell.setBounds(10, 10, 200, 200);
		shell.setVisible(true);

		Image image = new Image (display, 16, 16);
		Color color = display.getSystemColor (SWT.COLOR_RED);
		GC gc = new GC (image);
		gc.setBackground (color);
		gc.fillRectangle (image.getBounds ());
		gc.dispose ();




		SkijaGC skijaGC = new SkijaGC(shell, SWT.NONE);

		skijaGC.drawImage(image, 0, 0);
		skijaGC.drawText("Test", 0, 0);

		skijaGC.commit();


		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();


	}
}
