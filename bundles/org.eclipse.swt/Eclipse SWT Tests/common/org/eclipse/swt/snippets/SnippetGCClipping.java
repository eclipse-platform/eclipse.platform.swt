package org.eclipse.swt.snippets;

import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetGCClipping {

	static int ZOOM = 200;

	public static void main(String[] args) {
		Display display = new Display();
		Image image1 = display.getSystemImage(SWT.ICON_QUESTION);

		ImageData img = image1.getImageData();

		Shell shell = new Shell(display);
		shell.setText("SnippetGCClipping");
		shell.setLayout(new GridLayout());

		var canvas = new Canvas(shell, SWT.FILL);
		canvas.setSize(shell.getSize());


		canvas.addListener(SWT.Paint, event ->{

			var gc = SkijaGC.createDefaultInstance((NativeGC) event.gc.innerGC);

//			var gc = event.gc;

			var ca = canvas.getClientArea();

			gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_GREEN));

			gc.fillRectangle(ca);

			var clipped = new Rectangle(ca.x, ca.y, ca.width / 2, ca.height / 2);

			gc.setClipping(clipped);
			gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_RED));
			gc.fillRectangle(ca);

			gc.setClipping((Rectangle) null);

			ca.y = ca.y + ca.height;
			gc.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_BLUE));

			gc.fillRectangle(new Rectangle(clipped.x + clipped.width, clipped.y + clipped.height, clipped.width,
					clipped.height));

			gc.commit();

		});

		shell.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {

				canvas.setSize(shell.getSize());

			}

			@Override
			public void controlMoved(ControlEvent e) {

			}
		});

		shell.setSize(300, 500);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}

}
