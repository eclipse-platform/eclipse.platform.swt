package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class SnippetGetClippings {
	static boolean useSkia = true;
	
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("GetClipping Example");
        shell.setSize(800, 400);
        shell.setLayout(null);
        
        int style = SWT.DOUBLE_BUFFERED  | SWT.BORDER | (useSkia ? SWT.SKIA : SWT.NONE);
        

        // Rectangle clipping example
        Canvas canvasRect = new Canvas(shell, style);
        canvasRect.setBounds(20, 20, 350, 350);
        canvasRect.addPaintListener(e -> {
            GC gc = e.gc;
            // Set rectangular clipping
            Rectangle clipRect = new Rectangle(10, 10, 100, 100);
            
            gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
            gc.setClipping(clipRect);
            gc.fillRectangle(0, 0, 120, 120);
            
            int shift = 130;
            
            // Draw something
            // Get clipping rectangle
            Rectangle receivedClip = gc.getClipping();
            receivedClip.y += shift; 
            
            gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
            gc.setClipping(receivedClip);
            gc.fillRectangle(receivedClip);
            gc.drawText("getClipping()", 20, 60 +shift);
            
            Region r = new Region();
            gc.getClipping(r);
            
            r.translate(0, shift);
            
            gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
            gc.setClipping(r);
            gc.fillRectangle(new Rectangle(0, 0, 1200, 1020));
            gc.drawText("getClipping(Region)", 20, 60+ 2*shift);
            
            
        });

        // Region (circle) clipping example
        Canvas canvasRegion = new Canvas(shell, style);
        canvasRegion.setBounds(400, 20, 350, 350);
        canvasRegion.addPaintListener(e -> {
            GC gc = e.gc;
            // Set rectangular clipping
            Rectangle clipRect = new Rectangle(10, 10, 100, 100);
            Region region = new Region();
            region.add(clipRect);
            
            gc.setBackground(display.getSystemColor(SWT.COLOR_YELLOW));
            gc.setClipping(clipRect);
            gc.fillRectangle(0, 0, 120, 120);
            
            int shift = 130;
            
            // Draw something
            // Get clipping rectangle
            Region ret = new Region();
            gc.getClipping(ret);
            
            gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
            
            ret.translate(0, 130);
            
            gc.setClipping(ret);
            
            gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
            gc.setClipping(ret);
            gc.fillRectangle(new Rectangle(0, 0, 1200, 1020));
            gc.drawText("Region Clipping", 20, 60+shift);
        });

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}