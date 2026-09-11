package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SnippetCanvasCaret {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Canvas Caret Demo");
		shell.setSize(400, 300);
		shell.setLayout(new GridLayout(1, false)); // One column, vertical stack

		// Create two canvases stacked vertically
		Canvas canvas1 = new Canvas(shell, SWT.BORDER | SWT.SKIA);
		canvas1.setLayoutData(new GridData(GridData.FILL_BOTH));
		canvas1.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
		Canvas canvas2 = new Canvas(shell, SWT.BORDER);
		canvas2.setLayoutData(new GridData(GridData.FILL_BOTH));
		canvas2.setBackground(display.getSystemColor(SWT.COLOR_BLUE));

		// Initialize carets for both canvases
		Caret caret1 = new Caret(canvas1, SWT.NONE);
		caret1.setSize(2, 30);
		caret1.setVisible(true);
		Caret caret2 = new Caret(canvas2, SWT.NONE);
		caret2.setSize(2, 30);
		caret2.setVisible(true);

		// Timer logic for moving the carets
		final int steps = 20; // Number of positions
		Runnable moveCarets = new Runnable() {
			int pos = 0;

			@Override
			public void run() {
				if (canvas1.isDisposed() || caret1.isDisposed() || canvas2.isDisposed() || caret2.isDisposed())
					return;

				var ca = canvas2.getClientArea();
				var width = ca.width;
				int x = (int) ((1f * pos) / (1f * steps) * width);

				if (pos < 10) {
					caret1.setLocation(x, 15);
					canvas1.setFocus();

				} else {
					caret2.setLocation(x, 15);
					canvas2.setFocus();
				}

				pos = (pos + 1) % steps;
				display.timerExec(1000, this);
			}
		};
		display.timerExec(0, moveCarets);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}