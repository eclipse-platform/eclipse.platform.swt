package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Bug563253_GraphicsSnippet {

    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);

        // Create image and GC for drawing
        Image image = new Image(display, 400, 400);
        GC gc = new GC(image);

        // Paint shell
        shell.addListener(SWT.Paint, event -> {
            // Draw on GC
            gc.fillRectangle(0, 0, 400, 400);

            // Draw on event GC
            event.gc.drawImage(image, 0, 0);
        });

        shell.setSize(400, 400);
        shell.open();

        while(!shell.isDisposed()) {
            if(!display.readAndDispatch()) display.sleep();
        }

        display.dispose();
        gc.dispose();
        image.dispose();
    }
}