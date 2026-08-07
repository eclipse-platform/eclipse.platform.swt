package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SnippetSkiaDirectDrawing {

	public static void main(String[] arg) {


		final Display d = new Display();
		final Shell s = new Shell(d);
		s.setLayout(new FillLayout());

		final Canvas c = new Canvas(s, SWT.DOUBLE_BUFFERED | SWT.FILL | SWT.SKIA );

		c.setBackground(d.getSystemColor(SWT.COLOR_RED));

		s.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				c.setSize(s.getSize());

			}

			@Override
			public void controlMoved(ControlEvent e) {

			}
		});

		s.open();

		while (!s.isDisposed()) {
			d.readAndDispatch();
			if(!s.isDisposed())
				c.redraw();
		}

		d.close();

	}

}