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
package org.eclipse.swt.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * This class provides the layout for ScrolledComposite
 * 
 * @see ScrolledComposite
 */
class ScrolledCompositeLayout extends Layout {
	
	/* 
	 * Cache of minimum preferred size of the child control (SWT.DEFAULT, SWT.DEFAULT).
	 */
    Point preferredMinSize;

	/* 
	 * Cache the minimum size of the child control when the width is specified (wHint, SWT.DEFAULT).
	 */
    Point cachedMinWidth;

	/* 
	 * Cache the minimum size of the child control when the height is specified (SWT.DEFAULT, hHint).
	 */
    Point cachedMinHeight;

    /*
     * True iff we should recursively flush all children on the next layout
     */
    boolean flushChildren;

    boolean inLayout = false;
	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT	= 64;

/*
 * Compute the control's minimum size, and ensure that non-default hints are returned verbatim.
 * 
 * @param control
 * @param widthHint
 * @param heightHint
 * @return
 */
Point computeMinSize(Control control, int widthHint, int heightHint) {
    Point result = control.computeSize(widthHint, heightHint, flushChildren);
    flushChildren = false;
    
    if (widthHint != SWT.DEFAULT) {
        result.x = widthHint;
    }
    if (heightHint != SWT.DEFAULT) {
        result.y = heightHint;
    }
    return result;
}

protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
	if (flushCache) flushCache(composite);
	ScrolledComposite sc = (ScrolledComposite)composite;
	Point size = new Point(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	if (sc.content != null) {
		Point preferredSize = sc.content.computeSize(wHint, hHint, flushCache);
		Point currentSize = sc.content.getSize();
		size.x = sc.getExpandHorizontal() ? preferredSize.x : currentSize.x;
		size.y = sc.getExpandVertical() ? preferredSize.y : currentSize.y;
	}
	Point minSize = getMinSize(sc.content, sc.minWidth, sc.minHeight);
	size.x = Math.max(size.x, minSize.x);
	size.y = Math.max(size.y, minSize.y);
	if (wHint != SWT.DEFAULT) size.x = wHint;
	if (hHint != SWT.DEFAULT) size.y = hHint;
	return size;
}

private Point copyPoint(Point toCopy) {
    return new Point(toCopy.x, toCopy.y);
}

protected boolean flushCache(Control control) {
    preferredMinSize = null;
    cachedMinWidth = null;
    cachedMinHeight = null;
    flushChildren = true;
	return true;
}

/*
 * Computes the preferred minimum size of the control.
 *  
 * @param widthHint the known width of the control (pixels) or SWT.DEFAULT if unknown
 * @param heightHint the known height of the control (pixels) or SWT.DEFAULT if unknown
 * @return the preferred minimum size of the control
 */
Point getMinSize(Control control, int widthHint, int heightHint) {
    if (control == null) {
        return new Point(0, 0);
    }

    // If both dimensions were supplied in the input, return them verbatim
    if (widthHint != SWT.DEFAULT && heightHint != SWT.DEFAULT) {
        return new Point(widthHint, heightHint);
    }

    // No hints given -- find the preferred size
    if (widthHint == SWT.DEFAULT && heightHint == SWT.DEFAULT) {
        return copyPoint(getPreferredMinSize(control));
    }

    // If the length and width are independent, compute the preferred size
    // and adjust whatever dimension was supplied in the input
    if (isIndependentLengthAndWidth(control)) {
        Point result = copyPoint(getPreferredMinSize(control));
        if (widthHint != SWT.DEFAULT) {
            result.x = widthHint;
        }
        if (heightHint != SWT.DEFAULT) {
            result.y = heightHint;
        }
        return result;
    }

    // Computing a height
    if (heightHint == SWT.DEFAULT) {
        // If we know the control's preferred size
        if (preferredMinSize != null) {
            // If the given width is the preferred width, then return the preferred size
            if (widthHint == preferredMinSize.x) {
                return copyPoint(preferredMinSize);
            }
        }

        // If we have a cached height measurement
        if (cachedMinHeight != null) {
            // If this was measured with the same width hint
            if (cachedMinHeight.x == widthHint) {
                return copyPoint(cachedMinHeight);
            }
        }

        // Else we can't find an existing size in the cache, so recompute
        // it from scratch.
        cachedMinHeight = computeMinSize(control, widthHint, heightHint);

        return copyPoint(cachedMinHeight);
    }

    // Computing a width
    if (widthHint == SWT.DEFAULT) {
        // If we know the control's preferred size
        if (preferredMinSize != null) {
            // If the given height is the preferred height, then return the preferred size
            if (heightHint == preferredMinSize.y) {
                return copyPoint(preferredMinSize);
            }
        }

        // If we have a cached width measurement
        if (cachedMinWidth != null) {
            // If this was measured with the same height hint
            if (cachedMinWidth.y == heightHint) {
                return copyPoint(cachedMinWidth);
            }
        }

        cachedMinWidth = computeMinSize(control, widthHint, heightHint);

        return copyPoint(cachedMinWidth);
    }

    return computeMinSize(control, widthHint, heightHint);
}

Point getPreferredMinSize(Control control) {
    if (preferredMinSize == null) {
        preferredMinSize = computeMinSize(control, SWT.DEFAULT, SWT.DEFAULT);
    }
    return preferredMinSize;
}

boolean isIndependentLengthAndWidth(Control control) {
    if (control == null) {
        return true;
    }
    if (control instanceof Button || control instanceof ProgressBar
            || control instanceof Sash || control instanceof Scale
            || control instanceof Slider || control instanceof List
            || control instanceof Combo || control instanceof Tree) {
        return true;
    }
    if (control instanceof Label || control instanceof Text) {
        return (control.getStyle() & SWT.WRAP) == 0;
    }
    // Unless we're certain that the control has this property, we should
    // return false.
    return false;
}


protected void layout(Composite composite, boolean flushCache) {
	if (inLayout) return;
	if (flushCache) flushCache(composite);
	ScrolledComposite sc = (ScrolledComposite)composite;
	if (sc.content == null) return;
	ScrollBar hBar = sc.getHorizontalBar();
	ScrollBar vBar = sc.getVerticalBar();
	if (hBar != null) {
		if (hBar.getSize().y >= sc.getSize().y) {
			return;
		}
	}
	if (vBar != null) {
		if (vBar.getSize().x >= sc.getSize().x) {
			return;
		}
	}
	inLayout = true;
	Rectangle contentRect = sc.content.getBounds();
	if (!sc.alwaysShowScroll) {
		boolean hVisible = sc.needHScroll(contentRect, false);
		boolean vVisible = sc.needVScroll(contentRect, hVisible);
		if (!hVisible && vVisible) hVisible = sc.needHScroll(contentRect, vVisible);
		if (hBar != null) hBar.setVisible(hVisible);
		if (vBar != null) vBar.setVisible(vVisible);
	}
	Rectangle hostRect = sc.getClientArea();
	Point minSize = getMinSize(sc.content, sc.minWidth, sc.minHeight);
	if (sc.expandHorizontal) {
		contentRect.width = Math.max(minSize.x, hostRect.width);	
	}
	if (sc.expandVertical) {
		contentRect.height = Math.max(minSize.y, hostRect.height);
	}

	if (hBar != null) {
		hBar.setMaximum (contentRect.width);
		hBar.setThumb (Math.min (contentRect.width, hostRect.width));
		int hPage = contentRect.width - hostRect.width;
		int hSelection = hBar.getSelection ();
		if (hSelection >= hPage) {
			if (hPage <= 0) {
				hSelection = 0;
				hBar.setSelection(0);
			}
			contentRect.x = -hSelection;
		}
	}

	if (vBar != null) {
		vBar.setMaximum (contentRect.height);
		vBar.setThumb (Math.min (contentRect.height, hostRect.height));
		int vPage = contentRect.height - hostRect.height;
		int vSelection = vBar.getSelection ();
		if (vSelection >= vPage) {
			if (vPage <= 0) {
				vSelection = 0;
				vBar.setSelection(0);
			}
			contentRect.y = -vSelection;
		}
	}
	
	sc.content.setBounds (contentRect);
	inLayout = false;
}
}
