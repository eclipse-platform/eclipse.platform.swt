/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
package org.eclipse.swt.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;

/**
 * This class provides the layout for ScrolledComposite
 *
 * @see ScrolledComposite
 */
class ScrolledCompositeLayout extends Layout {

	boolean inLayout = false;
	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT	= 64;

@Override
protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
	ScrolledComposite sc = (ScrolledComposite)composite;
	Point size = new Point(DEFAULT_WIDTH, DEFAULT_HEIGHT);
	if (sc.content != null) {
		Point preferredSize = sc.content.computeSize(wHint, hHint, flushCache);
		Point currentSize = sc.content.getSize();
		size.x = sc.getExpandHorizontal() ? preferredSize.x : currentSize.x;
		size.y = sc.getExpandVertical() ? preferredSize.y : currentSize.y;
	}
	size.x = Math.max(size.x, sc.minWidth);
	size.y = Math.max(size.y, sc.minHeight);
    if (wHint != SWT.DEFAULT) {
        size.x = wHint;
    }
    if (hHint != SWT.DEFAULT) {
        size.y = hHint;
    }
	return size;
}

@Override
protected boolean flushCache(Control control) {
	return true;
}

@Override
protected void layout(Composite composite, boolean flushCache) {
    if (inLayout) {
        return;
    }
	ScrolledComposite sc = (ScrolledComposite)composite;
    if (sc.content == null) {
        return;
    }
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
	ViewportRuntime.PixelLayout viewport = sc.solveViewportLayout (contentRect);
    if (hBar != null) {
        hBar.setVisible(viewport.horizontalVisible());
    }
    if (vBar != null) {
        vBar.setVisible(viewport.verticalVisible());
    }
	Rectangle hostRect = sc.getClientArea();
	GC gc = new GC (sc);
	if (sc.logicalContentSize != null) {
		int logicalWidth = sc.logicalContentSize.x;
		int logicalHeight = sc.logicalContentSize.y;
		int hSelection = 0;
		int vSelection = 0;
		if (hBar != null) {
			hBar.setMaximum (logicalWidth);
			hBar.setThumb (Math.min (logicalWidth, hostRect.width));
			hBar.setIncrement ((int) gc.getFontMetrics ().getAverageCharacterWidth ());
			hBar.setPageIncrement (hostRect.width);
			hSelection = Math.min (hBar.getSelection (), Math.max (0, logicalWidth - hostRect.width));
			hBar.setSelection (hSelection);
		}
		if (vBar != null) {
			vBar.setMaximum (logicalHeight);
			vBar.setThumb (Math.min (logicalHeight, hostRect.height));
			vBar.setIncrement (gc.getFontMetrics ().getHeight ());
			vBar.setPageIncrement (hostRect.height);
			vSelection = Math.min (vBar.getSelection (), Math.max (0, logicalHeight - hostRect.height));
			vBar.setSelection (vSelection);
		}
		gc.dispose ();
		contentRect.x = 0;
		contentRect.y = 0;
		contentRect.width = hostRect.width;
		contentRect.height = hostRect.height;
		sc.content.setBounds (contentRect);
		sc.viewportRuntime.scrollTo (hSelection, vSelection);
		inLayout = false;
		return;
	}
	if (sc.expandHorizontal) {
		contentRect.width = Math.max(sc.minWidth, hostRect.width);
	}
	if (sc.expandVertical) {
		contentRect.height = Math.max(sc.minHeight, hostRect.height);
	}

	if (hBar != null) {
		hBar.setMaximum (contentRect.width);
		hBar.setThumb (Math.min (contentRect.width, hostRect.width));
		hBar.setIncrement ((int) gc.getFontMetrics ().getAverageCharacterWidth ());
		hBar.setPageIncrement (hostRect.width);
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
		vBar.setIncrement (gc.getFontMetrics ().getHeight ());
		vBar.setPageIncrement (hostRect.height);
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
	gc.dispose ();

	sc.content.setBounds (contentRect);
	sc.syncViewportOriginFromContent ();
	inLayout = false;
}
}
