/*******************************************************************************
 * Copyright (c) 2008, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.accessibility;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Instances of this class represent a very simple accessible bar chart.
 * From an accessibility point of view, they present the data as a "list" with "list items".
 */
public class BarChart extends Canvas {
	static ResourceBundle bundle = ResourceBundle.getBundle("examples_accessibility");
	List<Object[]> data = new ArrayList<>();
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
		addPaintListener(e -> {
			GC gc = e.gc;
			Rectangle rect = getClientArea();
			Display display = getDisplay();
			int count = data.size();
			Point valueSize = gc.stringExtent (Integer.valueOf(valueMax).toString());
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
			for (int i1 = valueMin; i1 <= valueMax; i1+=valueIncrement) {
				int y = bottomY - i1 * unitHeight;
				gc.drawLine(leftX, y, leftX - GAP, y);
				gc.drawString(Integer.valueOf(i1).toString(), rect.x + GAP, y - valueSize.y);
			}
			// draw the x axis and item labels
			gc.drawLine(leftX, bottomY, rect.x + rect.width - GAP, bottomY);
			for (int i2 = 0; i2 < count; i2++) {
				Object [] dataItem1 = data.get(i2);
				String itemLabel = (String)dataItem1[0];
				int x1 = leftX + AXIS_WIDTH + GAP + i2 * (unitWidth + GAP);
				gc.drawString(itemLabel, x1, bottomY + GAP);
			}
			// draw the bars
			gc.setBackground(display.getSystemColor(color));
			for (int i3 = 0; i3 < count; i3++) {
				Object [] dataItem2 = data.get(i3);
				int itemValue1 = ((Integer)dataItem2[1]).intValue();
				int x2 = leftX + AXIS_WIDTH + GAP + i3 * (unitWidth + GAP);
				gc.fillRectangle(x2, bottomY - AXIS_WIDTH - itemValue1 * unitHeight, unitWidth, itemValue1 * unitHeight);
			}
			if (isFocusControl()) {
				if (selectedItem == -1) {
					// draw the focus rectangle around the whole bar chart
					gc.drawFocus(rect.x, rect.y, rect.width, rect.height);
				} else {
					// draw the focus rectangle around the selected item
					Object [] dataItem3 = data.get(selectedItem);
					int itemValue2 = ((Integer)dataItem3[1]).intValue();
					int x3 = leftX + AXIS_WIDTH + GAP + selectedItem * (unitWidth + GAP);
					gc.drawFocus(x3, bottomY - itemValue2 * unitHeight - AXIS_WIDTH, unitWidth, itemValue2 * unitHeight + AXIS_WIDTH + GAP + valueSize.y);
				}
			}
		});

		addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				redraw();
			}
			@Override
			public void focusLost(FocusEvent e) {
				redraw();
			}
		});

		addMouseListener(MouseListener.mouseDownAdapter(e -> {
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
		}));

		addKeyListener(new KeyAdapter() {
			@Override
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

		addTraverseListener(e -> {
			switch (e.detail) {
				case SWT.TRAVERSE_TAB_NEXT:
				case SWT.TRAVERSE_TAB_PREVIOUS:
					e.doit = true;
					break;
			}
		});

		getAccessible().addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				MessageFormat formatter = new MessageFormat("");  //$NON_NLS$
				formatter.applyPattern(bundle.getString("name"));  //$NON_NLS$
				int childID = e.childID;
				if (childID == ACC.CHILDID_SELF) {
					e.result = title;
				} else {
					Object [] item = data.get(childID);
					e.result = formatter.format(item);
				}
			}
			@Override
			public void getDescription(AccessibleEvent e) {
				int childID = e.childID;
				if (childID != ACC.CHILDID_SELF) {
					Object [] item = data.get(childID);
					String value = item[1].toString();
					String colorName = bundle.getString("color" + color); //$NON_NLS$
					MessageFormat formatter = new MessageFormat("");  //$NON_NLS$
					formatter.applyPattern(bundle.getString("color_value"));  //$NON_NLS$
					e.result = formatter.format(new String [] {colorName, value});
				}
			}
		});

		getAccessible().addAccessibleControlListener(new AccessibleControlAdapter() {
			@Override
			public void getRole(AccessibleControlEvent e) {
				if (e.childID == ACC.CHILDID_SELF) {
					e.detail = ACC.ROLE_LIST;
				} else {
					e.detail = ACC.ROLE_LISTITEM;
				}
			}
			@Override
			public void getChildCount(AccessibleControlEvent e) {
				e.detail = data.size();
			}
			@Override
			public void getChildren(AccessibleControlEvent e) {
				int count = data.size();
				Object[] children = new Object[count];
				for (int i = 0; i < count; i++) {
					children[i] = Integer.valueOf(i);
				}
				e.children = children;
			}
			@Override
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
			@Override
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
			@Override
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
			@Override
			public void getSelection(AccessibleControlEvent e) {
				e.childID = (selectedItem == -1) ? ACC.CHILDID_NONE : selectedItem;
			}
			@Override
			public void getValue(AccessibleControlEvent e) {
				int childID = e.childID;
				if (childID != ACC.CHILDID_SELF) {
					Object [] dataItem = data.get(childID);
					e.result = ((Integer)dataItem[1]).toString();
				}
			}
			@Override
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

	@Override
	public Point computeSize (int wHint, int hHint, boolean changed) {
		checkWidget ();
		int count = data.size();
		GC gc = new GC (this);
		int titleWidth = gc.stringExtent (title).x;
		Point valueSize = gc.stringExtent (Integer.valueOf(valueMax).toString());
		int itemWidth = 0;
		for (int i = 0; i < count; i++) {
			Object [] dataItem = data.get(i);
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
		data.add(new Object[] {label, Integer.valueOf(value)});
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
		Point valueSize = gc.stringExtent (Integer.valueOf(valueMax).toString());
		gc.dispose();
		int leftX = rect.x + 2 * GAP + valueSize.x;
		int bottomY = rect.y + rect.height - 2 * GAP - valueSize.y;
		int unitWidth = (rect.width - 4 * GAP - valueSize.x - AXIS_WIDTH) / data.size() - GAP;
		int unitHeight = (rect.height - 3 * GAP - AXIS_WIDTH - 2 * valueSize.y) / ((valueMax - valueMin) / valueIncrement);
		Object [] dataItem = data.get(index);
		int itemValue = ((Integer)dataItem[1]).intValue();
		int x = leftX + AXIS_WIDTH + GAP + index * (unitWidth + GAP);
		return new Rectangle(x, bottomY - itemValue * unitHeight - AXIS_WIDTH, unitWidth, itemValue * unitHeight + AXIS_WIDTH + GAP + valueSize.y);
	}
}