package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

public class SnippetTransformAndClipping {

	final static boolean USE_SKIA = true; // Set to true to use Skia rendering, false for default SWT rendering
	
    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setText("Snippet Transform");
        
        var style = SWT.DOUBLE_BUFFERED   | (USE_SKIA ? SWT.SKIA : SWT.NONE);
        
        final Canvas canvas = new Canvas(shell,style);
        canvas.setSize(400, 400);
        shell.setSize(420, 440);

        canvas.addListener(SWT.Paint, SnippetTransformAndClipping::onPaint);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    private static void onPaint(Event e) {
    	
        e.gc.setClipping(5, 5, 20, 20); // Set clipping region to canvas size
        e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_DARK_GRAY));
        e.gc.fillRectangle(0, 0, 1000, 1000);
        
        e.gc.setClipping((Region)null); // Remove clipping region
    	
        // Draw a blue rectangle without transform
        e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_BLUE));
        e.gc.fillRectangle(50, 50, 100, 60);

        // Apply a transform: translate and rotate
        org.eclipse.swt.graphics.Transform transform = new org.eclipse.swt.graphics.Transform(e.display);
        transform.rotate(30); // rotate 30 degrees
        transform.translate(200, 200); // translate to (200, 200)
        e.gc.setTransform(transform);
        

        // Draw a red rectangle with transform
        e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_RED));
        e.gc.fillRectangle(0, 0, 100, 60); // Centered at the origin after transform

        e.gc.setClipping(50, 50, 20, 20); // Set clipping region to a small area
        e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_GREEN));
        e.gc.fillRectangle(0, 0, 1000, 1000);
        
        // Clean up
        transform.dispose();
        e.gc.setTransform(null); // Reset to default
    }
}