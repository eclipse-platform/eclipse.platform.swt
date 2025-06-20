package org.eclipse.swt.examples.tests;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

/**
 *
 * does not yet work: expected is that tabbing works and not arrows...
 *
 */
public class TabSnippetButton {

	final static int buttonStyle = SWT.RADIO;

	public static void main(String[] args) {
		Display display = new Display();
		Image image = display.getSystemImage(SWT.ICON_QUESTION);
		Shell shell = new Shell(display);
		shell.setText("SnippetButton_Old");
		shell.setLayout(new GridLayout());

		Group c = new Group(shell, SWT.NONE);

//		var b1 = new Button_Old(c, buttonStyle);
//		var b2 = new Button_Old(c, buttonStyle);
//		var b3 = new Button_Old(c, SWT.CHECK);

		var b1 = new Button(c, buttonStyle);
		var b2 = new Button(c, buttonStyle);
		var b3 = new Button(c, SWT.CHECK);

		b1.setText("1");
		b2.setText("2");
		b3.setText("Check");

		b1.setSize(100, 100);
		b2.setSize(100, 100);
		b3.setSize(100,100);


		b1.setLocation(200, 100);

		b2.setLocation(300, 100);
		b3.setLocation(400,100);

		c.setSize(400, 400);

		c.pack();

		c.redraw();


		shell.setSize(300, 500);


		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

}
