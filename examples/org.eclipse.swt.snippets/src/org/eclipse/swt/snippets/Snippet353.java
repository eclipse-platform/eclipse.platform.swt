/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

/*
 * Gesture example
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.7
 */ 
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class Snippet353 {

	static Point origin, size;
	static double rotation, currentRotation;
	static float magnification = 1.0f, currentMagnification;

	public static void main(String [] args) {
		Display display = new Display();
		final Shell shell = new Shell(display);

		GestureListener gl = new GestureListener() {
			public void gesture(GestureEvent ge) {
				if (ge.detail == SWT.GESTURE_BEGIN) {
					currentRotation = rotation;
					currentMagnification = magnification;
				}

				if (ge.detail == SWT.GESTURE_ROTATE) {
					rotation = currentRotation - ge.rotation;
					shell.redraw();
				}
				
				if (ge.detail == SWT.GESTURE_MAGNIFY) {
					magnification = (float) (currentMagnification * ge.magnification);
					shell.redraw();
				}

				if (ge.detail == SWT.GESTURE_SWIPE) {
					// xDirection and yDirection indicate direction for GESTURE_SWIPE.
					// For this example, just move in that direction to demonstrate it's working.
					origin.x += ge.xDirection * 50;
					origin.y += ge.yDirection * 50;
					shell.redraw();
				}

				if (ge.detail == SWT.GESTURE_PAN) {
					origin.x += ge.xDirection;
					origin.y += ge.yDirection;
					shell.redraw();
				}
				
				if (ge.detail == SWT.GESTURE_END) {
					
				}		
			}
		};

		PaintListener pl = new PaintListener() {
			public void paintControl(PaintEvent e) {
				Transform t = new Transform(Display.getCurrent());
				t.translate(origin.x, origin.y);
				t.translate(size.x / 2, size.y / 2);
				t.rotate((float) rotation);
				t.translate(-size.x / 2, -size.y / 2);
				t.translate(-origin.x, -origin.y);
				e.gc.setAntialias(SWT.ON);
				e.gc.setTransform(t);
				
				// Because of bug 253670, drawRectangle is incorrect when the rotation is near 45, 135, 225 or 315 degrees.
				// Uncomment this next line and adjust the bitfield for your platform. See GC#DRAW_OFFSET.
				//e.gc.getGCData().state |= 1 << 9;
				
				e.gc.drawRectangle(origin.x, origin.y, (int)(size.x * magnification), (int)(size.y * magnification));
				t.dispose();
			}
		};
		
		shell.addPaintListener(pl);
		shell.addGestureListener(gl);
		shell.setSize(400, 400);
		size = new Point(50, 50);
		origin = new Point((shell.getSize().x - size.x) / 2, (shell.getSize().y - size.y) / 2);
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}