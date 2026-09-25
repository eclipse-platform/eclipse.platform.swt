package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.AnimatedProgress;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Example usage of AnimatedProgress (deprecated).
 */
public class SnippetAnimatedProgress {
	
	final static boolean USE_SKIA = true; // Set to true to use Skia rendering, false for default SWT rendering

	
    @SuppressWarnings("deprecation")
	public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("AnimatedProgress Example");
        shell.setLayout(new GridLayout(1, false));

        Composite composite = new Composite(shell, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        composite.setLayout(new GridLayout(1, false));

		AnimatedProgress progress = new AnimatedProgress(composite, SWT.HORIZONTAL | SWT.BORDER | (USE_SKIA ? SWT.SKIA : SWT.NONE));
        progress.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        Button startButton = new Button(composite, SWT.PUSH);
        startButton.setText("Start Animation");
        startButton.addListener(SWT.Selection, e -> progress.start());

        Button stopButton = new Button(composite, SWT.PUSH);
        stopButton.setText("Stop Animation");
        stopButton.addListener(SWT.Selection, e -> progress.stop());

        shell.setSize(300, 120);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
