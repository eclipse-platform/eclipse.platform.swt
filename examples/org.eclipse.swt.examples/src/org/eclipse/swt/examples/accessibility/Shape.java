/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.accessibility;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.accessibility.*;

/**
 * Instances of this class represent an accessible, focusable, user interface object
 * that has a shape and a color. From an accessibility point of view, they imitate a
 * "label" or "static text" whose name is the shape, and value is the color.
 */
public class Shape extends Canvas {
	String color = "Blue", shape = "Square";
	
	/**
	 * Constructs a new instance of this class given its parent
	 * and a style value describing its behavior and appearance.
	 *
	 * @param parent a composite control which will be the parent of the new instance (cannot be null)
	 * @param style the style of control to construct
	 */
	public Shape(Composite parent, int style) {
		super (parent, style);

		addListeners();
	}

	void addListeners() {
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				Display display = getDisplay();
				Color c = display.getSystemColor(SWT.COLOR_BLUE);
				if (color.equals("Red")) c = display.getSystemColor(SWT.COLOR_RED);
				else if (color.equals("Green")) c = display.getSystemColor(SWT.COLOR_GREEN);
				gc.setBackground(c);
				Rectangle rect = getClientArea();
				int length = Math.min(rect.width, rect.height);
				if (shape.equals("Circle")) {
					gc.fillOval(0, 0, length, length);
				} else {
					gc.fillRectangle(0, 0, length, length);
				}
				if (isFocusControl()) gc.drawFocus(rect.x, rect.y, rect.width, rect.height);
			}
		});
		
		addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				redraw();
			}
			public void focusLost(FocusEvent e) {
				redraw();
			}
		});
		
		addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				if (getClientArea().contains(e.x, e.y)) {
					setFocus();
				}
			}
		});
		
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				// key listener enables traversal out
			}
		});
		
		addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				switch (e.detail) {
					case SWT.TRAVERSE_TAB_NEXT:
					case SWT.TRAVERSE_TAB_PREVIOUS:
						e.doit = true;
						break;
				}
			}
		});

		getAccessible().addAccessibleListener(new AccessibleAdapter() {
			public void getName(AccessibleEvent e) {
				e.result = shape;
			}
		});
		
		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_LABEL;
			}
			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_FOCUSABLE;
				if (isFocusControl()) e.detail |= ACC.STATE_FOCUSED;
			}
			public void getValue(AccessibleControlEvent e) {
				e.result = color;
			}
		});
	}
	
	/**
	 * Return the receiver's color.
	 * The default color is "Blue".
	 * 
	 * @return the color name string, which may be either "Red", "Green" or "Blue"
	 */
	public String getColor () {
		return this.color;
	}

	/**
	 * Return the receiver's shape.
	 * The default shape is "Square".
	 * 
	 * @return the shape name string, which may be either "Circle" or "Square"
	 */
	public String getShape () {
		return this.shape;
	}

	/**
	 * Set the receiver's color to the specified color name string.
	 * The default color is "Blue".
	 * 
	 * @param color a string that can be either "Red", "Green" or "Blue"
	 */
	public void setColor (String color) {
		this.color = color;
	}

	/**
	 * Set the receiver's shape to the specified shape name string.
	 * The default shape is "Square".
	 * 
	 * @param shape a string that can be either "Circle" or "Square"
	 */
	public void setShape (String shape) {
		this.shape = shape;
	}
}
