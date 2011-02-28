/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
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
 * Touch example: create a shell and listen for TouchEvents. Paint
 * circles where the user touched.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 *
 * @since 3.7
 */
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet352 {
	
	private static class CircleInfo {
		
		public CircleInfo(Point inCenter, Color inColor) {
			this.center = inCenter;
			this.color = inColor;
		}
		
		Point center;
		Color color;
	}
	
	static Map<Long, CircleInfo> touchLocations = new HashMap<Long, CircleInfo>();
	static int colorIndex = 0;
	static final int PAINTABLE_COLORS = 15;
	static final int CIRCLE_RADIUS = 40;
	
	public static void main (String [] args) {
		final Display display = new Display ();
		final Shell shell = new Shell (display);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));
		shell.setText ("Touch demonstration");
		
		TouchListener tl = new TouchListener() {
			public void touch(TouchEvent e) {
				
				Touch touches[] = e.touches;

				for (int i = 0; i < touches.length; i++) {
					Touch currTouch = touches[i];

					if ((currTouch.state & (SWT.TOUCHSTATE_UP)) != 0) {
						touchLocations.remove(currTouch.id);
					} else {
						CircleInfo info = touchLocations.get(currTouch.id);
						Point newPoint = Display.getCurrent().map(null, (Control)e.widget, new Point((int)currTouch.x, (int)currTouch.y));

						if (info == null) {
							info = new CircleInfo(newPoint, display.getSystemColor((colorIndex + 2) % PAINTABLE_COLORS));
							colorIndex++;
						}

						info.center = newPoint;
						touchLocations.put(currTouch.id, info);
					}
				}

				Control c = (Control)e.widget;
				c.redraw();
			}

		};

		PaintListener pl = new PaintListener() {
			public void paintControl(PaintEvent e) {
				Iterator<Map.Entry<Long, CircleInfo>> iter = touchLocations.entrySet().iterator();
				while (iter.hasNext()) {
					CircleInfo ci = iter.next().getValue();
					e.gc.setBackground(ci.color);
					e.gc.fillOval(ci.center.x - CIRCLE_RADIUS, ci.center.y - CIRCLE_RADIUS, CIRCLE_RADIUS * 2, CIRCLE_RADIUS * 2);
				}
			}
		};
		
		Canvas c = new Canvas(shell, SWT.NONE);
		c.setTouchEnabled(true);
		c.setSize(800, 800);
		c.addTouchListener(tl);
		c.addPaintListener(pl);

		shell.setSize (800, 800);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}