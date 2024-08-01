package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class SkijaCanvasTest {


	public static void main (String [] args) {

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("SkijaTest");
		shell.setBounds(10, 10, 200, 200);

		shell.setVisible(true);


		SkijaGC skijaGC = new SkijaGC(shell, SWT.NONE);


		skijaGC.drawText("Hello World Test", 0, 0);


		skijaGC.drawLine(0, 0,  200 , 200);



		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();


	}

}
