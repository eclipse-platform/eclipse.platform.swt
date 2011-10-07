/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.launcher;

 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * A Layout class that automatically switches from a horizontal split to a vertical
 * split layout to accomodate changing size conditions.
 * 
 * Later on we might improve this class to take into account the "preferred" size of
 * the widgets.
 */
public class SplitLayout extends Layout {
	private static final int
		splitHorizontally = 0,
		splitVertically = 1;
	private int splitDirection = splitHorizontally;

	public int spacing = 3;
	public int marginTop = 3;
	public int marginLeft = 3;
	public int marginRight = 3;
	public int marginBottom = 3;

	/**
	 * Creates a new layout
	 */
	public SplitLayout() {
	}

	/**
	 * @see Layout#computeSize(Composite, int, int, boolean)
	 */
	protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		if (wHint == SWT.DEFAULT) {
			if (hHint == SWT.DEFAULT) {
				Point hSplitSize = computeHSplitSize(composite, wHint, hHint, flushCache);
				Point vSplitSize = computeVSplitSize(composite, wHint, hHint, false);
				int hSplitArea = hSplitSize.x * hSplitSize.y;
				int vSplitArea = vSplitSize.x * vSplitSize.y;
				// Choose direction consuming least area
				if (hSplitArea < vSplitArea) {
					splitDirection = splitHorizontally;
					return hSplitSize;
				} else {
					splitDirection = splitVertically;
					return vSplitSize;
				}
			} else {
				// Constrained in height: split vertically
				splitDirection = splitVertically;
				return computeVSplitSize(composite, wHint, hHint, flushCache);
			}
		} else {
			if (hHint == SWT.DEFAULT) {
				// Constrained in width: split horizontally
				splitDirection = splitHorizontally;
				return computeHSplitSize(composite, wHint, hHint, flushCache);
			} else {
				if (hHint < wHint) {
					splitDirection = splitVertically;
					return computeVSplitSize(composite, wHint, hHint, flushCache);
				} else {
					splitDirection = splitHorizontally;
					return computeHSplitSize(composite, wHint, hHint, flushCache);
				}					
			}
		}
	}
	/**
	 * @see Layout#layout(Composite, boolean)
	 */
	protected void layout(Composite composite, boolean flushCache) {
		Rectangle clientArea = composite.getClientArea();
		computeSize(composite, clientArea.width, clientArea.height, false);
		
		Control[] children = composite.getChildren();
		clientArea.x += marginLeft;
		clientArea.y += marginTop;
		clientArea.width -= marginRight + marginLeft;
		clientArea.height -= marginBottom + marginTop;
		Point position = new Point(clientArea.x, clientArea.y);

		for (int i = 0; i < children.length; ++i) {
			final Control child = children[i];
			final Rectangle bounds;
			if (splitDirection == splitHorizontally) {
				int height = clientArea.height / children.length;
				bounds = new Rectangle(position.x, position.y, clientArea.width, height);
				position.y += height + spacing;
			} else {
				int width = clientArea.width / children.length;
				bounds = new Rectangle(position.x, position.y, width, clientArea.height);
				position.x += width + spacing;
			}
			bounds.width = Math.max(bounds.width, 0);
			bounds.height = Math.max(bounds.height, 0);
			child.setBounds(bounds);
		}
	}

	private Point computeHSplitSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		Point size = new Point(marginLeft + marginRight, marginTop + marginBottom);
		Control[] children = composite.getChildren();
		for (int i = 0; i < children.length; ++i) {
			final Control child = children[i];

			Point childSize = child.computeSize(wHint, hHint, flushCache);
			size.x = Math.max(size.x, childSize.x);
			size.y += childSize.y + spacing;
		}
		return size;
	}

	private Point computeVSplitSize(Composite composite, int wHint, int hHint, boolean flushCache) {
		Point size = new Point(marginLeft + marginRight, marginTop + marginBottom);
		Control[] children = composite.getChildren();
		for (int i = 0; i < children.length; ++i) {
			final Control child = children[i];

			Point childSize = child.computeSize(wHint, hHint, flushCache);
			size.x += childSize.x + spacing;
			size.y = Math.max(size.y, childSize.y);
		}
		return size;
	}
}
