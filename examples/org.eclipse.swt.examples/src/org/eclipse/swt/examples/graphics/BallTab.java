/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.swt.examples.graphics;

import java.util.LinkedList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Transform;

/**
 * This tab uses alpha blending to simulate "ghosting" of a ball in movement.
 */
public class BallTab extends AnimatedGraphicsTab {

	BallCollection[] bc;

	/**
	 * This inner class serves as a container for the data needed to display a
	 * collection of balls.
	 */
	static class BallCollection {
		float x, y; // position of ball
		float incX, incY; // values by which to move the ball
		int ball_size; // size (diameter) of the ball
		int capacity; // number of balls in the collection
		LinkedList prevx, prevy; // collection of previous x and y positions
								 // of ball
		Color[] colors; // colors used for this ball collection

		public BallCollection(float x, float y, float incX, float incY,
				int ball_size, int capacity, Color[] colors) {
			this.x = x;
			this.y = y;
			this.incX = incX;
			this.incY = incY;
			this.ball_size = ball_size;
			this.capacity = capacity;
			prevx = new LinkedList();
			prevy = new LinkedList();
			this.colors = colors;
		}
	}

	public void dispose() {
		bc[0] = bc[1] = bc[2] = bc[3] = bc[4] = null;
	}

	public BallTab(GraphicsExample example) {
		super(example);
		bc = new BallCollection[5];
	}

	public String getCategory() {
		return GraphicsExample.getResourceString("Alpha"); //$NON-NLS-1$
	}

	public String getText() {
		return GraphicsExample.getResourceString("Ball"); //$NON-NLS-1$
	}

	public String getDescription() {
		return GraphicsExample.getResourceString("BallDescription"); //$NON-NLS-1$
	}
	
	public int getInitialAnimationTime() {
		return 10;
	}

	public void next(int width, int height) {
		for (int i = 0; i < bc.length; i++) {
			if (bc[i] == null) return;
			if (bc[i].prevx.size() == 0) {
				bc[i].prevx.addLast(new Float(bc[i].x));
				bc[i].prevy.addLast(new Float(bc[i].y));
			} else if (bc[i].prevx.size() == bc[i].capacity) {
				bc[i].prevx.removeFirst();
				bc[i].prevy.removeFirst();
			}

			bc[i].x += bc[i].incX;
			bc[i].y += bc[i].incY;

			float random = (float) Math.random();

			// right
			if (bc[i].x + bc[i].ball_size > width) {
				bc[i].x = width - bc[i].ball_size;
				bc[i].incX = random * -width / 16 - 1;
			}
			// left
			if (bc[i].x < 0) {
				bc[i].x = 0;
				bc[i].incX = random * width / 16 + 1;
			}
			// bottom
			if (bc[i].y + bc[i].ball_size > height) {
				bc[i].y = (height - bc[i].ball_size) - 2;
				bc[i].incY = random * -height / 16 - 1;
			}
			// top
			if (bc[i].y < 0) {
				bc[i].y = 0;
				bc[i].incY = random * height / 16 + 1;
			}
			bc[i].prevx.addLast(new Float(bc[i].x));
			bc[i].prevy.addLast(new Float(bc[i].y));
		}
	}

	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;
		Device device = gc.getDevice();

		if (bc[0] == null) {
			bc[0] = new BallCollection(0, 0, 5, 5, 20, 20, new Color[] { device
					.getSystemColor(SWT.COLOR_GREEN) });
			bc[1] = new BallCollection(50, 300, 10, -5, 50, 10,
					new Color[] { device.getSystemColor(SWT.COLOR_BLUE) });
			bc[2] = new BallCollection(250, 100, -5, 8, 25, 12,
					new Color[] { device.getSystemColor(SWT.COLOR_RED) });
			bc[3] = new BallCollection(150, 400, 5, 8, 35, 14,
					new Color[] { device.getSystemColor(SWT.COLOR_BLACK) });
			bc[4] = new BallCollection(100, 250, -5, -18, 100, 5,
					new Color[] { device.getSystemColor(SWT.COLOR_MAGENTA) });
		}

		for (int j = 0; j < bc.length; j++) {
			for (int i = 0; i < bc[j].prevx.size(); i++) {
				Transform transform = new Transform(device);
				transform.translate(((Float) bc[j].prevx.get(bc[j].prevx.size()
						- (i + 1))).floatValue(), ((Float) bc[j].prevy
						.get(bc[j].prevy.size() - (i + 1))).floatValue());
				gc.setTransform(transform);
				transform.dispose();

				Path path = new Path(device);
				path.addArc(0, 0, bc[j].ball_size, bc[j].ball_size, 0, 360);
				gc.setAlpha(255 - i * (255 / bc[j].capacity));
				gc.setBackground(bc[j].colors[0]);
				gc.fillPath(path);
				gc.drawPath(path);
				path.dispose();
			}
		}
	}
}
