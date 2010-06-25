/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.accessibility;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Vector;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.accessibility.*;

/**
 * Instances of this class represent a very simple accessible bar chart.
 * From an accessibility point of view, they present the data as a "list" with "list items".
 */
public class BarChart extends Canvas {
	static ResourceBundle bundle = ResourceBundle.getBundle("examples_accessibility");
	Vector data = new Vector();
	String title;
	int color = SWT.COLOR_RED;
	int selectedItem = -1;
	int valueMin = 0;
	int valueMax = 10;
	int valueIncrement = 1;
	static final int GAP = 4;
	static final int AXIS_WIDTH = 2;
	
	/**
	 * Constructs a new instance of this class given its parent
	 * and a style value describing its behavior and appearance.
	 *
	 * @param parent a composite control which will be the parent of the new instance (cannot be null)
	 * @param style the style of control to construct
	 */
	public BarChart(Composite parent, int style) {
		super (parent, style);

		addListeners();
	}

	void addListeners() {
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				Rectangle rect = getClientArea();
				Display display = getDisplay();
				int count = data.size();
				Point valueSize = gc.stringExtent (new Integer(valueMax).toString());
				int leftX = rect.x + 2 * GAP + valueSize.x;
				int bottomY = rect.y + rect.height - 2 * GAP - valueSize.y;
				int unitWidth = (rect.width - 4 * GAP - valueSize.x - AXIS_WIDTH) / count - GAP;
				int unitHeight = (rect.height - 3 * GAP - AXIS_WIDTH - 2 * valueSize.y) / ((valueMax - valueMin) / valueIncrement);
				// draw the title
				int titleWidth = gc.stringExtent (title).x;
				int center = (Math.max(titleWidth, count * (unitWidth + GAP) + GAP) - titleWidth) / 2;
				gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
				gc.drawString(title, leftX + AXIS_WIDTH + center, rect.y + GAP);
				// draw the y axis and value labels
				gc.setLineWidth(AXIS_WIDTH);
				gc.drawLine(leftX, rect.y + GAP + valueSize.y, leftX, bottomY);
				for (int i = valueMin; i <= valueMax; i+=valueIncrement) {
					int y = bottomY - i * unitHeight;
					gc.drawLine(leftX, y, leftX - GAP, y);
					gc.drawString(new Integer(i).toString(), rect.x + GAP, y - valueSize.y);
				}
				// draw the x axis and item labels
				gc.drawLine(leftX, bottomY, rect.x + rect.width - GAP, bottomY);
				for (int i = 0; i < count; i++) {
					Object [] dataItem = (Object []) data.elementAt(i);
					String itemLabel = (String)dataItem[0];
					int x = leftX + AXIS_WIDTH + GAP + i * (unitWidth + GAP);
					gc.drawString(itemLabel, x, bottomY + GAP);
				}
				// draw the bars
				gc.setBackground(display.getSystemColor(color));
				for (int i = 0; i < count; i++) {
					Object [] dataItem = (Object []) data.elementAt(i);
					int itemValue = ((Integer)dataItem[1]).intValue();
					int x = leftX + AXIS_WIDTH + GAP + i * (unitWidth + GAP);
					gc.fillRectangle(x, bottomY - AXIS_WIDTH - itemValue * unitHeight, unitWidth, itemValue * unitHeight);
				}
				if (isFocusControl()) {
					if (selectedItem == -1) {
						// draw the focus rectangle around the whole bar chart
						gc.drawFocus(rect.x, rect.y, rect.width, rect.height);
					} else {
						// draw the focus rectangle around the selected item
						Object [] dataItem = (Object []) data.elementAt(selectedItem);
						int itemValue = ((Integer)dataItem[1]).intValue();
						int x = leftX + AXIS_WIDTH + GAP + selectedItem * (unitWidth + GAP);
						gc.drawFocus(x, bottomY - itemValue * unitHeight - AXIS_WIDTH, unitWidth, itemValue * unitHeight + AXIS_WIDTH + GAP + valueSize.y);
					}
				}
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
					int item = -1;
					int count = data.size();
					for (int i = 0; i < count; i++) {
						if (itemBounds(i).contains(e.x, e.y)) {
							item = i;
							break;
						}
					}
					if (item != selectedItem) {
						selectedItem = item;
						redraw();
						getAccessible().setFocus(item);
						getAccessible().selectionChanged();
					}
				}
			}
		});
		
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				boolean change = false;
				switch (e.keyCode) {
					case SWT.ARROW_DOWN:
					case SWT.ARROW_RIGHT:
						selectedItem++;
						if (selectedItem >= data.size()) selectedItem = 0;
						change = true;
						break;
					case SWT.ARROW_UP:
					case SWT.ARROW_LEFT:
						selectedItem--;
						if (selectedItem <= -1) selectedItem = data.size() - 1;
						change = true;
						break;
					case SWT.HOME:
						selectedItem = 0;
						change = true;
						break;
					case SWT.END:
						selectedItem = data.size() - 1;
						change = true;
						break;
				}
				if (change) {
					redraw();
					getAccessible().setFocus(selectedItem);
					getAccessible().selectionChanged();
				}
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
				MessageFormat formatter = new MessageFormat("");  //$NON_NLS$
				formatter.applyPattern(bundle.getString("name"));  //$NON_NLS$
				int childID = e.childID;
				if (childID == ACC.CHILDID_SELF) {
					e.result = title;
				} else {
					Object [] item = (Object [])data.elementAt(childID);
					e.result = formatter.format(item);
				}
			}
			public void getDescription(AccessibleEvent e) {
				int childID = e.childID;
				if (childID != ACC.CHILDID_SELF) {
					Object [] item = (Object [])data.elementAt(childID);
					String value = item[1].toString();
					String colorName = bundle.getString("color" + color); //$NON_NLS$
					MessageFormat formatter = new MessageFormat("");  //$NON_NLS$
					formatter.applyPattern(bundle.getString("color_value"));  //$NON_NLS$
					e.result = formatter.format(new String [] {colorName, value});
				}
			}
		});
		
		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
			public void getRole(AccessibleControlEvent e) {
				if (e.childID == ACC.CHILDID_SELF) {
					e.detail = ACC.ROLE_LIST;
				} else {
					e.detail = ACC.ROLE_LISTITEM;
				}
			}
			public void getChildCount(AccessibleControlEvent e) {
				e.detail = data.size();
			}
			public void getChildren(AccessibleControlEvent e) {
				int count = data.size();
				Object[] children = new Object[count];
				for (int i = 0; i < count; i++) {
					children[i] = new Integer(i);
				}
				e.children = children;
			}
			public void getChildAtPoint(AccessibleControlEvent e) {
				Point testPoint = toControl(e.x, e.y);
				int childID = ACC.CHILDID_NONE;
				if (getClientArea().contains(testPoint)) {
					childID = ACC.CHILDID_SELF;
					int count = data.size();
					for (int i = 0; i < count; i++) {
						if (itemBounds(i).contains(testPoint)) {
							childID = i;
							break;
						}
					}
				}
				e.childID = childID;
			}
			public void getLocation(AccessibleControlEvent e) {
				Rectangle location = null;
				Point pt = null;
				int childID = e.childID;
				if (childID == ACC.CHILDID_SELF) {
					location = getClientArea();
					pt = getParent().toDisplay(location.x, location.y);
				} else {
					location = itemBounds(childID);
					pt = toDisplay(location.x, location.y);
				}
				e.x = pt.x;
				e.y = pt.y;
				e.width = location.width;
				e.height = location.height;
			}
			public void getFocus(AccessibleControlEvent e) {
				int childID = ACC.CHILDID_NONE;
				if (isFocusControl()) {
					if (selectedItem == -1) {
						childID = ACC.CHILDID_SELF;
					} else {
						childID = selectedItem;
					}
				}
				e.childID = childID;
			
			}
			public void getSelection(AccessibleControlEvent e) {
				e.childID = (selectedItem == -1) ? ACC.CHILDID_NONE : selectedItem;
			}
			public void getValue(AccessibleControlEvent e) {
				int childID = e.childID;
				if (childID != ACC.CHILDID_SELF) {
					Object [] dataItem = (Object []) data.elementAt(childID);
					e.result = ((Integer)dataItem[1]).toString();					
				}
			}
			public void getState(AccessibleControlEvent e) {
				int childID = e.childID;
				e.detail = ACC.STATE_FOCUSABLE;
				if (isFocusControl()) e.detail |= ACC.STATE_FOCUSED;
				if (childID != ACC.CHILDID_SELF) {
					e.detail |= ACC.STATE_SELECTABLE;
					if (childID == selectedItem) e.detail |= ACC.STATE_SELECTED;
				}
			}
		});
	}
	
	public Point computeSize (int wHint, int hHint, boolean changed) {
		checkWidget ();
		int count = data.size();
		GC gc = new GC (this);
		int titleWidth = gc.stringExtent (title).x;
		Point valueSize = gc.stringExtent (new Integer(valueMax).toString());
		int itemWidth = 0;
		for (int i = 0; i < count; i++) {
			Object [] dataItem = (Object []) data.elementAt(i);
			String itemLabel = (String)dataItem[0];
			itemWidth = Math.max(itemWidth, gc.stringExtent (itemLabel).x);
		}
		gc.dispose();
		int width = Math.max(titleWidth, count * (itemWidth + GAP) + GAP) + 3 * GAP + AXIS_WIDTH + valueSize.x;
		int height = 3 * GAP + AXIS_WIDTH + valueSize.y * ((valueMax - valueMin) / valueIncrement + 3);
		if (wHint != SWT.DEFAULT) width = wHint;
		if (hHint != SWT.DEFAULT) height = hHint;
		int border = getBorderWidth ();
		Rectangle trim = computeTrim (0, 0, width + border*2, height + border*2);
		return new Point (trim.width, trim.height);
	}

	/**
	 * Add a labeled data value to the bar chart.
	 * 
	 * @param label a string describing the value
	 * @param value the data value
	 */
	public void addData (String label, int value) {
		checkWidget ();
		data.add(new Object[] {label, new Integer(value)});
	}
	
	/**
	 * Set the title of the bar chart.
	 * 
	 * @param title a string to display as the bar chart's title.
	 */
	public void setTitle (String title) {
		checkWidget ();
		this.title = title;
	}

	/**
	 * Set the bar color to the specified color.
	 * The default color is SWT.COLOR_RED.
	 * 
	 * @param color any of the SWT.COLOR_* constants
	 */
	public void setColor (int color) {
		checkWidget ();
		this.color = color;
	}

	/**
	 * Set the minimum value for the y axis.
	 * The default minimum is 0.
	 * 
	 * @param min the minimum value
	 */
	public void setValueMin (int min) {
		checkWidget ();
		valueMin = min;
	}

	/**
	 * Set the maximum value for the y axis.
	 * The default maximum is 10.
	 * 
	 * @param max the maximum value
	 */
	public void setValueMax (int max) {
		checkWidget ();
		valueMax = max;
	}

	/**
	 * Set the increment value for the y axis.
	 * The default increment is 1.
	 * 
	 * @param increment the increment value
	 */
	public void setValueIncrement (int increment) {
		checkWidget ();
		valueIncrement = increment;
	}

	/* The bounds of the specified item in the coordinate system of the BarChart. */
	Rectangle itemBounds(int index) {
		Rectangle rect = getClientArea();
		GC gc = new GC (BarChart.this);
		Point valueSize = gc.stringExtent (new Integer(valueMax).toString());
		gc.dispose();
		int leftX = rect.x + 2 * GAP + valueSize.x;
		int bottomY = rect.y + rect.height - 2 * GAP - valueSize.y;
		int unitWidth = (rect.width - 4 * GAP - valueSize.x - AXIS_WIDTH) / data.size() - GAP;
		int unitHeight = (rect.height - 3 * GAP - AXIS_WIDTH - 2 * valueSize.y) / ((valueMax - valueMin) / valueIncrement);
		Object [] dataItem = (Object []) data.elementAt(index);
		int itemValue = ((Integer)dataItem[1]).intValue();
		int x = leftX + AXIS_WIDTH + GAP + index * (unitWidth + GAP);
		return new Rectangle(x, bottomY - itemValue * unitHeight - AXIS_WIDTH, unitWidth, itemValue * unitHeight + AXIS_WIDTH + GAP + valueSize.y);
	}
}