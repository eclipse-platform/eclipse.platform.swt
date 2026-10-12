/*******************************************************************************
 * Copyright 2026 Hitesh Soliwal and contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 *******************************************************************************/
package org.eclipse.swt.awt;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.ViewportGcProxy;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

/**
 * An SWT canvas that paints Java2D content through a visible-sized raster.
 * <p>
 * The renderer receives a real {@link Graphics2D} synchronously on the SWT
 * Display thread. Its clip identifies the logical content requiring repaint.
 * The renderer must not retain that graphics object; the canvas disposes it when
 * the callback returns. Java2D paths, paints, images, fonts and composites keep
 * their ordinary Java2D behavior. Presentation uses the existing SWT image/GC
 * native path and does not create an embedded component or another event loop.
 * </p><p>
 * When this canvas is the content of a {@link ScrolledComposite} with an
 * explicit logical content size, that parent owns the extent and scroll origin.
 * Painting composes {@code translate(-origin.x, -origin.y) * viewTransform}.
 * The origin is measured in the parent's logical pixels after the view
 * transform. In other parents the origin is zero. Configuring a view transform
 * does not change the parent's logical extent.
 * </p><p>
 * The raster contains one pixel per SWT logical client pixel. It and its pixel
 * validity mask are bounded by the current client size, not the logical content
 * size. Native image transfers contain only the requested dirty rectangle.
 * Exposures reuse valid raster pixels; callers must use {@link #redraw()} or
 * {@link #redraw(int, int, int, int, boolean)} when their model changes. The
 * rectangle overload uses canvas coordinates, as it does for other SWT controls.
 * Integer translations support partial raster rendering. Other view transforms
 * regenerate the visible raster when pixels are invalid, preserving the edge
 * coverage of a fresh Java2D frame; native transfers still use only the requested
 * dirty rectangle.
 * A renderer must reconstruct its requested region from model state rather than
 * depend on paint-call counts or pixels from an earlier callback.
 * </p><p>
 * Before each rendering callback the dirty raster is filled with the canvas
 * background color. Java2D initially uses black paint and its default font and
 * rendering hints. The renderer may select other state, including alpha
 * compositing. SWT background painting also runs before presentation, so newly
 * transparent source pixels cannot leave old content on screen.
 * </p>
 *
 * @since 3.137
 */
public final class Java2DCanvas extends Canvas {

	private final Java2DSurface surface = new Java2DSurface ();
	private Consumer<Graphics2D> renderer;
	private AffineTransform viewTransform = new AffineTransform ();
	private boolean painting;

	/**
	 * Constructs a Java2D canvas using the ordinary SWT Canvas style bits.
	 *
	 * @param parent the parent composite, not {@code null}
	 * @param style the SWT style bits
	 */
	public Java2DCanvas (Composite parent, int style) {
		super (parent, style);
		addListener (SWT.Paint, this::paint);
		addListener (SWT.Resize, event -> {
			/* Release an old, possibly much larger raster even while hidden. */
			surface.close ();
			super.redraw ();
		});
		addListener (SWT.Dispose, event -> {
			renderer = null;
			surface.close ();
		});
	}

	/**
	 * Returns the current synchronous renderer, or {@code null}.
	 *
	 * @return the renderer
	 */
	public Consumer<Graphics2D> getRenderer () {
		checkWidget ();
		return renderer;
	}

	/**
	 * Sets the renderer and invalidates the raster. A {@code null} renderer paints
	 * only the background. Callbacks execute on the Display thread and must finish
	 * before event processing can continue.
	 *
	 * @param renderer the renderer, or {@code null} to clear it
	 */
	public void setRenderer (Consumer<Graphics2D> renderer) {
		checkWidget ();
		this.renderer = renderer;
		redraw ();
	}

	/**
	 * Returns a defensive copy of the transform from content coordinates to the
	 * parent's logical pixel coordinates before scrolling.
	 *
	 * @return the current view transform
	 */
	public AffineTransform getViewTransform () {
		checkWidget ();
		return new AffineTransform (viewTransform);
	}

	/**
	 * Sets a copied content transform and invalidates the raster. A {@code null}
	 * transform restores the identity. Finite singular transforms are supported,
	 * as in Java2D; a renderer may receive no invertible user-space clip for them.
	 *
	 * @param transform the new transform, or {@code null}
	 * @exception IllegalArgumentException if any transform element is not finite
	 */
	public void setViewTransform (AffineTransform transform) {
		checkWidget ();
		AffineTransform next = transform == null ? new AffineTransform () : new AffineTransform (transform);
		double [] elements = new double [6];
		next.getMatrix (elements);
		for (double element : elements) {
			if (!Double.isFinite (element)) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		}
		if (viewTransform.equals (next)) return;
		viewTransform = next;
		redraw ();
	}

	/**
	 * Returns the logical origin owned by the enclosing logical ScrolledComposite,
	 * or {@code (0, 0)} when this is not that parent's logical content control.
	 *
	 * @return a new point containing the current logical origin
	 */
	public Point getViewportOrigin () {
		checkWidget ();
		if (getParent () instanceof ScrolledComposite parent
				&& parent.getContent () == this && parent.getLogicalContentSize () != null) {
			return parent.getOrigin ();
		}
		return new Point (0, 0);
	}

	/**
	 * Returns the dimensions of the currently allocated raster. The result is
	 * {@code (0, 0)} before the first paint and after a resize until painting
	 * allocates the new visible-sized surface. No raster is allocated for an empty
	 * client area.
	 *
	 * @return a new point containing the raster width and height
	 */
	public Point getRasterSize () {
		checkWidget ();
		return new Point (surface.width (), surface.height ());
	}

	/** Invalidates all cached pixels and schedules ordinary SWT painting. */
	@Override
	public void redraw () {
		checkWidget ();
		/* A superclass constructor may call this before field initialization. */
		if (surface != null) surface.invalidate ();
		super.redraw ();
	}

	/**
	 * Invalidates a rectangle in canvas coordinates and schedules SWT painting.
	 *
	 * @param x the canvas x coordinate
	 * @param y the canvas y coordinate
	 * @param width the rectangle width
	 * @param height the rectangle height
	 * @param all whether intersecting child controls also need repainting
	 */
	@Override
	public void redraw (int x, int y, int width, int height, boolean all) {
		checkWidget ();
		if (surface != null) {
			Rectangle area = getClientArea ();
			surface.invalidate (x - area.x, y - area.y, width, height);
		}
		super.redraw (x, y, width, height, all);
	}

	private void paint (Event event) {
		if (event.gc == null || event.gc.isDisposed ()) return;
		if (painting) {
			/* Do not recursively invoke an application renderer from update(). */
			super.redraw ();
			return;
		}
		painting = true;
		try {
			Rectangle area = getClientArea ();
			Rectangle dirty = paintBounds (event, area);
			configureSurface (area);
			if (dirty.isEmpty ()) return;
			int x = dirty.x - area.x, y = dirty.y - area.y;
			if (!surface.render (x, y, dirty.width, dirty.height, renderer)) {
				if (!isDisposed ()) super.redraw ();
				return;
			}
			if (isDisposed () || event.gc.isDisposed ()) return;
			Image transfer = new Image (getDisplay (), surface.copyImageData (x, y, dirty.width, dirty.height));
			try {
				try (ViewportGcProxy scope = ViewportGcProxy.wrap (event.gc)) {
					scope.clip (dirty);
					GC gc = scope.gc ();
					gc.setAlpha (255);
					gc.setXORMode (false);
					gc.setInterpolation (SWT.NONE);
					drawBackground (gc, dirty.x, dirty.y, dirty.width, dirty.height);
					gc.drawImage (transfer, dirty.x, dirty.y);
				}
			} finally {
				transfer.dispose ();
			}
		} finally {
			painting = false;
		}
	}

	private void configureSurface (Rectangle area) {
		Point origin = getViewportOrigin ();
		AffineTransform transform = AffineTransform.getTranslateInstance (
				-(double)origin.x - area.x, -(double)origin.y - area.y);
		transform.concatenate (viewTransform);
		Color background = getBackground ();
		int argb = background.getAlpha () << 24 | background.getRed () << 16
				| background.getGreen () << 8 | background.getBlue ();
		surface.configure (area.width, area.height, transform, argb);
	}

	private static Rectangle paintBounds (Event event, Rectangle area) {
		Rectangle clip = event.gc.getClipping ();
		if (event.width <= 0 || event.height <= 0 || clip.isEmpty () || area.isEmpty ()) {
			return new Rectangle (0, 0, 0, 0);
		}
		long left = Math.max (area.x, Math.max (event.x, clip.x));
		long top = Math.max (area.y, Math.max (event.y, clip.y));
		long right = Math.min ((long)area.x + area.width,
				Math.min ((long)event.x + event.width, (long)clip.x + clip.width));
		long bottom = Math.min ((long)area.y + area.height,
				Math.min ((long)event.y + event.height, (long)clip.y + clip.height));
		if (right <= left || bottom <= top) return new Rectangle (0, 0, 0, 0);
		return new Rectangle ((int)left, (int)top, (int)(right - left), (int)(bottom - top));
	}
}
