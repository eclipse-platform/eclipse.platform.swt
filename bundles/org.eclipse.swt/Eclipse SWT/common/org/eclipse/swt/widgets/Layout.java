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
package org.eclipse.swt.widgets;


import org.eclipse.swt.graphics.*;

/**
 * A layout controls the position and size
 * of the children of a composite widget.
 * This class is the abstract base class for
 * layouts.
 * 
 *  @see Composite#setLayout(Layout)
 */
public abstract class Layout {

/**
 * Computes and returns the size of the specified
 * composite's client area according to this layout.
 * <p>
 * This method computes the size that the client area 
 * of the composite must be in order to position all 
 * children at their preferred size inside the
 * composite according to the layout algorithm
 * encoded by this layout.
 * </p>
 * <p>
 * When a width or height hint is supplied, it is
 * used to constrain the result. For example, if a
 * width hint is provided that is less than the
 * width of the client area, the layout may choose
 * to wrap and increase height, clip, overlap, or
 * otherwise constrain the children.
 * </p>
 *
 * @param composite a composite widget using this layout
 * @param wHint width (<code>SWT.DEFAULT</code> for preferred size)
 * @param hHint height (<code>SWT.DEFAULT</code> for preferred size)
 * @param flushCache <code>true</code> means flush cached layout values
 * @return a point containing the computed size (width, height)
 * 
 * @see #layout
 * @see Control#getBorderWidth
 * @see Control#getBounds
 * @see Control#getSize
 * @see Control#pack(boolean)
 * @see "computeTrim, getClientArea for controls that implement them"
 */
protected abstract Point computeSize (Composite composite, int wHint, int hHint, boolean flushCache);

/**
 * Instruct the layout to flush any cached values
 * associated with the control specified in the argument 
 * <code>control</code>.
 * 
 * @param control a control managed by this layout
 * @return true if the Layout has flushed all cached information associated with control
 * 
 * @since 3.1
 */
protected boolean flushCache (Control control) {
	return false;
}

/**
 * Lays out the children of the specified composite
 * according to this layout.
 * <p>
 * This method positions and sizes the children of a
 * composite using the layout algorithm encoded by this
 * layout. Children of the composite are positioned in
 * the client area of the composite. The position of
 * the composite is not altered by this method.
 * </p>
 * <p>
 * When the flush cache hint is true, the layout is
 * instructed to flush any cached values associated
 * with the children. Typically, a layout will cache
 * the preferred sizes of the children to avoid the
 * expense of computing these values each time the
 * widget is laid out.
 * </p>
 * <p>
 * When layout is triggered explicitly by the programmer
 * the flush cache hint is true. When layout is triggered
 * by a resize, either caused by the programmer or by the
 * user, the hint is false.
 * </p>
 *
 * @param composite a composite widget using this layout
 * @param flushCache <code>true</code> means flush cached layout values
 */
protected abstract void layout (Composite composite, boolean flushCache);
}
