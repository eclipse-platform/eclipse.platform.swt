package org.eclipse.swt.custom;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*; 
import org.eclipse.swt.events.*;

/**
 * A control for showing progress feedback for a long running operation.
 */
public class AnimatedProgress extends Canvas {

	private static final int SLEEP = 70;
	private static final int DEFAULT_WIDTH = 160;
	private static final int DEFAULT_HEIGHT = 18;
	private boolean active = false;
	private boolean showStripes = false;
	private int value;
	private int orientation = SWT.HORIZONTAL;
	private boolean showBorder = false;

public AnimatedProgress(Composite parent, int style) {
	super(parent, checkStyle(style));
	
	if ((style & SWT.VERTICAL) != 0) {
		orientation = SWT.VERTICAL;
	}
	showBorder = (style & SWT.BORDER) != 0;
	
	addControlListener(new ControlAdapter() {
		public void controlResized(ControlEvent e) {
			redraw();
		}
	});	
	addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
			paint(e);
		}
	});
	addDisposeListener(new DisposeListener() {
		public void widgetDisposed(DisposeEvent e){
			stop();
		}
	});
}
private static int checkStyle (int style) {
	int mask = SWT.NONE;
	return style & mask;
}
/**
 * Stop the animation if it is not already stopped and 
 * reset the presentation to a blank appearance.
 */
public synchronized void clear(){
	checkWidget();
	if (active) stop();
	showStripes = false;
	redraw();
}
public Point computeSize(int wHint, int hHint, boolean changed) {
	checkWidget();
	Point size = null;
	if (orientation == SWT.HORIZONTAL) {
		size = new Point(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	} else {
		size = new Point(DEFAULT_HEIGHT, DEFAULT_WIDTH);
	}
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	
	return size;
}
private void drawBevelRect(GC gc, int x, int y, int w, int h, Color topleft, Color bottomright) {
	gc.setForeground(topleft);
	gc.drawLine(x, y, x+w-1, y);
	gc.drawLine(x, y, x, y+h-1);
		
	gc.setForeground(bottomright);
	gc.drawLine(x+w, y, x+w, y+h);
	gc.drawLine(x, y+h, x+w, y+h);
}
private void paint(PaintEvent event) {
	GC gc = event.gc;
	Display disp= getDisplay();
			
	Rectangle rect= getClientArea();
	gc.fillRectangle(rect);
	if (showBorder) {
		drawBevelRect(gc, rect.x, rect.y, rect.width-1, rect.height-1,
			disp.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW),
			disp.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
	}
	
	paintStripes(gc);
}	
private void paintStripes(GC gc) {
	
	if (!showStripes) return;
	
	Rectangle rect= getClientArea();
	// Subtracted border painted by paint.
	rect = new Rectangle(rect.x+2, rect.y+2, rect.width-4, rect.height-4);

	gc.setLineWidth(2);
	gc.setClipping(rect);
	Color color = getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION);
	gc.setBackground(color);
	gc.fillRectangle(rect);
	gc.setForeground(this.getBackground());
	int step = 12;
	int foregroundValue = value == 0 ? step - 2 : value - 2;
	if (orientation == SWT.HORIZONTAL) {
		int y = rect.y - 1;
		int w = rect.width;
		int h = rect.height + 2;
		for (int i= 0; i < w; i+= step) {
			int x = i + foregroundValue;
			gc.drawLine(x, y, x, h);
		}
	} else {
		int x = rect.x - 1;
		int w = rect.width + 2;
		int h = rect.height;

		for (int i= 0; i < h; i+= step) {
			int y = i + foregroundValue;
			gc.drawLine(x, y, w, y);
		}
	}
	
	if (active) {
		value = (value + 2) % step;
	}
}
/**
* Start the animation.
*/
public synchronized void start() {
	checkWidget();
	if (active) return;

	active = true;
	showStripes = true;
	
	final Display display = getDisplay();
	final Runnable [] timer = new Runnable [1];
	timer [0] = new Runnable () {
		public void run () {
			if (!active) return;
			GC gc = new GC(AnimatedProgress.this);			
			paintStripes(gc);
			gc.dispose();
			display.timerExec (SLEEP, timer [0]);
		}
	};
	display.timerExec (SLEEP, timer [0]);
}
/**
* Stop the animation.   Freeze the presentation at its current appearance.
*/
public synchronized void stop() {
	checkWidget();
	active = false;
}
}
