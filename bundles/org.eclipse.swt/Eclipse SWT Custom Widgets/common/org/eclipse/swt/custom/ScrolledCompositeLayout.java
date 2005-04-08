/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
	
	boolean inLayout = false;
	static final int DEFAULT_WIDTH	= 64;
	static final int DEFAULT_HEIGHT	= 64;
	
protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
	ScrolledComposite sc = (ScrolledComposite)composite;
	if (sc.content == null) {
		int w = (wHint != SWT.DEFAULT) ? wHint : DEFAULT_WIDTH;
		int h = (hHint != SWT.DEFAULT) ? hHint : DEFAULT_HEIGHT;
		return new Point(w, h);
	}
	Point size = sc.content.computeSize (wHint, hHint, flushCache);
	if (sc.alwaysShowScroll) {
		ScrollBar hBar = sc.getHorizontalBar();
		ScrollBar vBar = sc.getVerticalBar();
		if (hBar != null) size.y += hBar.getSize().y;
		if (vBar != null) size.x += vBar.getSize().x;
	}
	return size;
}

protected boolean flushCache(Control control) {
	return true;
}

protected void layout(Composite composite, boolean flushCache) {
	if (inLayout) return;
	ScrolledComposite sc = (ScrolledComposite)composite;
	if (sc.content == null) return;
	inLayout = true;
	Rectangle contentRect = sc.content.getBounds();
	ScrollBar hBar = sc.getHorizontalBar();
	ScrollBar vBar = sc.getVerticalBar();
	if (!sc.alwaysShowScroll) {
		boolean hVisible = sc.needHScroll(contentRect, false);
		boolean vVisible = sc.needVScroll(contentRect, hVisible);
		if (!hVisible && vVisible) hVisible = sc.needHScroll(contentRect, vVisible);
		if (hBar != null) hBar.setVisible(hVisible);
		if (vBar != null) vBar.setVisible(vVisible);
	}

	Rectangle hostRect = sc.getClientArea();
	if (sc.expandHorizontal) {
		contentRect.width = Math.max(sc.minWidth, hostRect.width);	
	}
	if (sc.expandVertical) {
		contentRect.height = Math.max(sc.minHeight, hostRect.height);
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
