package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SnippetCopyArea {
	
	final static boolean useSkia = true;
	
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("Skija CopyArea paint=true Example");
        shell.setSize(400, 300);

        Canvas canvas = new Canvas(shell, useSkia ? SWT.SKIA : SWT.NONE);
        canvas.setBounds(10, 10, 380, 280);

        // Create an image and draw something on it
        Image image = new Image(display, 100, 100);
        GC gcImage = new GC(image);
        gcImage.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
        gcImage.fillRectangle(0, 0, 100, 100);
        gcImage.setForeground(display.getSystemColor(SWT.COLOR_RED));
        gcImage.drawLine(0, 0, 100, 100);
        gcImage.dispose();

        canvas.addPaintListener(e -> {
        	
        	if(e.x == 20 && e.y == 20) {
        		return;
        	}
        	
            // Draw the original image
            e.gc.drawImage(image, 20, 20);
            // Use copyArea to move a part of the image
            // Rectangle: srcX, srcY, width, height, destX, destY
            e.gc.copyArea(20, 20, 50, 50, 120, 20, true); // paint=true
            // Draw a border around the copied area
            e.gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
            e.gc.drawRectangle(120, 20, 50, 50);
        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        image.dispose();
        display.dispose();
    }
}