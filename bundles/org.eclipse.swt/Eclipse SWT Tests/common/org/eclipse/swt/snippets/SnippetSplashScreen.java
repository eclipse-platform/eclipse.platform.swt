package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class SnippetSplashScreen {

	public static void main(String[] args) {
		Display display = new Display();
		Shell splash = new Shell(display, SWT.ON_TOP);
		splash.setText("Splash Screen");

		// Set the size of the splash screen
		Rectangle screenSize = display.getPrimaryMonitor().getBounds();
		splash.setSize(400, 200);
		splash.setLocation((screenSize.width - 400) / 2, (screenSize.height - 200) / 2);

		// Create a progress bar
//		progressBar.setBounds((screenSize.width - 400) / 2, (screenSize.height - 200) / 2, 300, 30);
//		progressBar.setMaximum(100);

		int downShift = 50;

		var ca = splash.getClientArea();
		System.out.println("ClientArea: " + ca);
		var progressBar = new ProgressBar(splash, SWT.SMOOTH);
		progressBar.setMinimum(50);
		progressBar.setMaximum(100);
		progressBar.setBounds(ca.x + 50, ca.y + downShift, 200, 32);

		Label b = new Label(splash, SWT.NONE);
		b.setText("Text");
		b.setBounds(ca.x + 50, ca.y + 40 + downShift, 200, 30);

		splash.open();

		countUpProgressBar(progressBar, /* closeAfterCount */ true);

		// Event loop
		while (!splash.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static void countUpProgressBar(ProgressBar progressBar, boolean close) {

		var display = progressBar.getDisplay();

		new Thread(() -> {

			// Simulate some work with progress
			for (int i = 0; i <= 100; i++) {
				final int progress = i;
				progressBar.getDisplay().asyncExec(() -> {
					if (progressBar.isDisposed())
						return;
					progressBar.setSelection(progress);
					System.out.println("Set Progress: " + progress);
				});

				try {
					Thread.sleep(20); // Simulate work
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// Close the splash screen after work is done
			if (close)
				display.asyncExec(() -> {
					if (!progressBar.getShell().isDisposed()) {
						progressBar.getShell().close();
					}
				});
		}).start();

	}
}