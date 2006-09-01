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

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * This is another tab that demonstrates the use of a path clipping.
 */
public class PathClippingAnimTab extends AnimatedGraphicsTab {

	private Button colorButton;
	private GraphicsBackground background;
	private Menu menu;
	private int rectWidth = 300;
	private int rectHeight = 300;
	private int incWidth = 5;
	private int incHeight = 5;
	private boolean vertical = false;
	private int angle;

	public PathClippingAnimTab(GraphicsExample example) {
		super(example);
	}

	public String getCategory() {
		return GraphicsExample.getResourceString("Clipping"); //$NON-NLS-1$
	}

	public String getText() {
		return GraphicsExample.getResourceString("AnimPathClipping"); //$NON-NLS-1$
	}
	
	public String getDescription() {
		return GraphicsExample.getResourceString("AnimPathClippingDesc"); //$NON-NLS-1$
	}

	public void dispose() {
		if (menu != null) {
			menu.dispose();
			menu = null;
		}
	}

	/**
	 * Creates the widgets used to control the drawing.
	 */
	public void createControlPanel(Composite parent) {
		super.createControlPanel(parent);

		// color menu
		ColorMenu cm = new ColorMenu();
		cm.setPatternItems(example.checkAdvancedGraphics());
		menu = cm.createMenu(parent.getParent(), new ColorListener() {
			public void setColor(GraphicsBackground gb) {
				background = gb;
				colorButton.setImage(gb.getThumbNail());
				example.redraw();
			}
		});

		// initialize the background to the 5th item in the menu (blue)
		background = (GraphicsBackground) menu.getItem(4).getData();

		// color button
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));

		colorButton = new Button(comp, SWT.PUSH);
		colorButton.setText(GraphicsExample.getResourceString("Color")); //$NON-NLS-1$
		colorButton.setImage(background.getThumbNail());
		colorButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				final Button button = (Button) event.widget;
				final Composite parent = button.getParent();
				Rectangle bounds = button.getBounds();
				Point point = parent.toDisplay(new Point(bounds.x, bounds.y));
				menu.setLocation(point.x, point.y + bounds.height);
				menu.setVisible(true);
			}
		});
	}

	public void next(int width, int height) {
		angle = (angle + 5)%360;
		if (vertical) {
			if (rectHeight <= 0) {
				incHeight = -incHeight;
			}
			if (rectHeight >= height) {
				incHeight = -incHeight;
				vertical = false;
			}
			rectHeight = rectHeight + incHeight;
		} else {
			if (rectWidth <= 0) {
				incWidth = -incWidth;
			}
			if (rectWidth >= width) {
				incWidth = -incWidth;
				vertical = true;
			}
			rectWidth = rectWidth + incWidth;
		}
	}

	public void paint(GC gc, int width, int height) {
		if (!example.checkAdvancedGraphics()) return;
		Device device = gc.getDevice();
		
		// top triangle
		Path path = new Path(device);
		path.moveTo(width/2, 0);
		path.lineTo(width/2+100, 173);
		path.lineTo(width/2-100, 173);
		path.lineTo(width/2, 0);
		
		// bottom triangle
		Path path2 = new Path(device);
		path2.moveTo(width/2, height);
		path2.lineTo(width/2+100, height-173);
		path2.lineTo(width/2-100, height-173);
		path2.lineTo(width/2, height);
		
		// left triangle
		Path path3 = new Path(device);
		path3.moveTo(0, height/2);
		path3.lineTo(173, height/2-100);
		path3.lineTo(173, height/2+100);
		path3.lineTo(0, height/2);
		
		// right triangle
		Path path4 = new Path(device);
		path4.moveTo(width, height/2);
		path4.lineTo(width-173, height/2-100);
		path4.lineTo(width-173, height/2+100);
		path4.lineTo(width, height/2);
		
		// circle
		Path path5 = new Path(device);
		path5.moveTo((width-200)/2, (height-200)/2);
		path5.addArc((width-200)/2, (height-200)/2, 200, 200, 0, 360);
		
		// top rectangle
		Path path6 = new Path(device);
		path6.addRectangle((width-40)/2, 175, 40, ((height-200)/2)-177);
		
		// bottom rectangle
		Path path7 = new Path(device);
		path7.addRectangle((width-40)/2, ((height-200)/2)+202, 40, (height-175)-(((height-200)/2)+202));
		
		// left rectangle
		Path path8 = new Path(device);
		path8.addRectangle(175, (height-40)/2, ((width-200)/2)-177, 40);
		
		// right rectangle
		Path path9 = new Path(device);
		path9.addRectangle((width-200)/2+202, (height-40)/2, (width-175)-((width-200)/2+202), 40);
		
		path.addPath(path2);
		path.addPath(path3);
		path.addPath(path4);
		path.addPath(path5);
		path.addPath(path6);
		path.addPath(path7);
		path.addPath(path8);
		path.addPath(path9);
		gc.setClipping(path);

		Pattern pattern = null;
		if (background.getBgColor1() != null) {
			gc.setBackground(background.getBgColor1());
		} else if (background.getBgImage() != null) {
			pattern = new Pattern(device, background.getBgImage());
			gc.setBackgroundPattern(pattern);
		}

		gc.setLineWidth(2);
		gc.fillRectangle((width-rectWidth)/2, (height-rectHeight)/2, rectWidth, rectHeight);
		gc.drawPath(path);
		
		if (pattern != null)
			pattern.dispose();
		
		path9.dispose();
		path8.dispose();
		path7.dispose();
		path6.dispose();
		path5.dispose();
		path4.dispose();
		path3.dispose();
		path2.dispose();
		path.dispose();
	}
}
