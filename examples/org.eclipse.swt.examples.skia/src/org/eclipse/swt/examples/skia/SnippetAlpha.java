package org.eclipse.swt.examples.skia;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

public class SnippetAlpha {

	final static boolean USE_SKIA = true; // Set to true to use Skia rendering, false for default SWT rendering
	
    public static void main(String[] args) {
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setText("Snippet Alpha");
        
        var style = SWT.DOUBLE_BUFFERED   | (USE_SKIA ? SWT.SKIA : SWT.NONE);
        
        final Canvas canvas = new Canvas(shell,style);
        canvas.setSize(400, 400);
        shell.setSize(420, 440);

        canvas.addListener(SWT.Paint, SnippetAlpha::onPaint);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

    private static void onPaint(Event e) {
    	
    	e.gc.setAdvanced(false);
    	e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_RED));
    	e.gc.fillRectangle(0, 0, 100, 100);

    	// on windows and linux alpha in colors is not supported at all
    	Color c = new Color(e.display, new RGBA(0, 0, 255, 50));
    	e.gc.setBackground(c);
    	e.gc.fillRectangle(200, 0, 100, 100);
    	c.dispose();
    	
    	// on windows and linux alpha colors is not supported at all
    	
    	e.gc.setAlpha(100);
    	e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_GREEN));
    	e.gc.fillRectangle(0, 200, 100, 100);
    	c.dispose();
    	
    	
    	e.gc.setLineWidth(10);
     	e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_YELLOW));
    	e.gc.drawLine(0, 0, 500, 500);
    	
    	
    	
    }
}