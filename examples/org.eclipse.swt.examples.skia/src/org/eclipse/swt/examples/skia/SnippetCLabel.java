package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.graphics.Image;

/**
 * Example usage of CLabel.
 */
public class SnippetCLabel {
    public static final boolean USE_SKIA = true;

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("CLabel Example");
        shell.setLayout(new GridLayout(1, false));

        // Simple CLabel with text
        CLabel label1 = new CLabel(shell, (USE_SKIA ? SWT.SKIA : 0) | SWT.NONE);
        label1.setText("This is a CLabel with text.");
        label1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        // CLabel with image and text
        Image image = display.getSystemImage(SWT.ICON_INFORMATION);
        CLabel label2 = new CLabel(shell, (USE_SKIA ? SWT.SKIA : 0) | SWT.NONE);
        label2.setText("CLabel with image and text.");
        label2.setImage(image);
        label2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        // CLabel with alignment
        CLabel label3 = new CLabel(shell, (USE_SKIA ? SWT.SKIA : 0) | SWT.NONE);
        label3.setText("Right aligned CLabel.");
        label3.setAlignment(SWT.RIGHT);
        label3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        shell.setSize(350, 150);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}