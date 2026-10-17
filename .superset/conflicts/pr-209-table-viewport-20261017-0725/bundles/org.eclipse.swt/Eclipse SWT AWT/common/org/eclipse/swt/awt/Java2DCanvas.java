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
import org.eclipse.swt.internal.ViewportRuntime;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

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
 * Configure {@link #setLogicalContentSize(int, int)} to make scrolling part of
 * this canvas itself. Its own H_SCROLL/V_SCROLL bars, synchronous selection
 * hooks, resize/show/hide and disposal cooperate without an extra Control.
 * The widget's screen position, public event source, focus and traversal remain
 * unchanged. The canvas raster has zero pixel overscan; model providers may
 * manage their existing bounded row/column overscan independently. No native
 * handle or caller-owned item is evicted by this mediation.
 * </p><p>
 * Without that explicit configuration, the historical behavior is unchanged.
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
	/* Internal mediation flags, never public SWT style bits. */
	private static final int VIEWPORT_SYNC = 1, VIEWPORT_AGAIN = 2, VIEWPORT_CLOSED = 4, VIEWPORT_RESTORE = 8, VIEWPORT_DIRTY = 16;
	private int viewportState;
	/* Negative width keeps the historical enclosing-viewport behavior. */
	private int logicalWidth = -1, logicalHeight, originX, originY;
	private int viewportWidth, viewportHeight;
	private long viewportEpoch;
	private BarState savedHorizontal, savedVertical;

	private record BarState (int selection, int minimum, int maximum, int thumb,
			int increment, int pageIncrement, boolean visible, boolean enabled) {
		static BarState capture (ScrollBar bar) {
			return bar == null || bar.isDisposed () ? null : new BarState (
					bar.getSelection (), bar.getMinimum (), bar.getMaximum (), bar.getThumb (),
					bar.getIncrement (), bar.getPageIncrement (), bar.getVisible (), bar.getEnabled ());
		}
	}

	/**
	 * Constructs a Java2D canvas using the ordinary SWT Canvas style bits.
	 *
	 * @param parent the parent composite, not {@code null}
	 * @param style the SWT style bits
	 */
	public Java2DCanvas (Composite parent, int style) {
		super (parent, style);
		/* One listener instance per canvas; nothing per input event or logical row. */
		Listener mediation = this::mediateViewport;
		addListener (SWT.Paint, mediation);
		addListener (SWT.Resize, mediation);
		addListener (SWT.Show, mediation);
		addListener (SWT.Hide, mediation);
		addListener (SWT.Dispose, mediation);
		ScrollBar horizontal = getHorizontalBar (), vertical = getVerticalBar ();
		if (horizontal != null) horizontal.addListener (SWT.Selection, mediation);
		if (vertical != null) vertical.addListener (SWT.Selection, mediation);
	}

	private void mediateViewport (Event event) {
		switch (event.type) {
			case SWT.Paint -> paint (event);
			case SWT.Selection -> scrollViewport (event);
			case SWT.Resize -> {
				/* Release an old large raster even while hidden. */
				surface.close ();
				viewportState |= VIEWPORT_DIRTY;
				synchronizeViewport ();
				if (viewportAlive ()) super.redraw ();
			}
			case SWT.Show -> {
				if (logicalWidth >= 0) {
					viewportState |= VIEWPORT_DIRTY;
					synchronizeViewport ();
					if (viewportAlive ()) super.redraw ();
				}
			}
			case SWT.Hide -> {
				if (logicalWidth >= 0) surface.close ();
			}
			case SWT.Dispose -> {
				viewportState |= VIEWPORT_CLOSED;
				logicalWidth = -1;
				savedHorizontal = savedVertical = null;
				renderer = null;
				surface.close ();
			}
			default -> { /* No alternative public event dispatch. */ }
		}
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
	 * Returns this canvas's origin when its logical content size is configured.
	 * Otherwise returns the enclosing logical ScrolledComposite origin, or
	 * {@code (0, 0)} when this is not that parent's logical content control.
	 *
	 * @return a new point containing the current logical origin
	 */
	public Point getViewportOrigin () {
		checkWidget ();
		if (logicalWidth >= 0) {
			checkViewportOwner ();
			return new Point (originX, originY);
		}
		if (getParent () instanceof ScrolledComposite parent
				&& parent.getContent () == this && parent.getLogicalContentSize () != null) {
			return parent.getOrigin ();
		}
		return new Point (0, 0);
	}

	/**
	 * Makes this canvas the sole owner of its content extent and scroll origin.
	 * No ScrolledComposite, child control or second event loop is created. The
	 * existing H_SCROLL/V_SCROLL style bits determine which native bars exist;
	 * absent axes remain programmatically scrollable. Extents are nonnegative
	 * SWT logical pixels after the view transform, not bitmap allocation sizes.
	 *
	 * @param width logical content width
	 * @param height logical content height
	 * @exception IllegalArgumentException for negative extents or a competing
	 *             enclosing logical ScrolledComposite
	 * @since 3.137
	 */
	public void setLogicalContentSize (int width, int height) {
		checkWidget ();
		checkViewportAlive ();
		if (width < 0 || height < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		checkViewportOwner ();
		if (logicalWidth == width && logicalHeight == height) {
			viewportState |= VIEWPORT_DIRTY;
			synchronizeViewport ();
			return;
		}
		if (logicalWidth < 0) {
			/* A synchronous native Resize may re-enable us midway through restore.
			 * Keep the original pair, never snapshot a half-restored pair of bars. */
			if ((viewportState & VIEWPORT_RESTORE) == 0) {
				savedHorizontal = BarState.capture (getHorizontalBar ());
				savedVertical = BarState.capture (getVerticalBar ());
			}
			originX = originY = 0;
		}
		logicalWidth = width;
		logicalHeight = height;
		viewportState |= VIEWPORT_DIRTY;
		viewportEpoch++;
		synchronizeViewport ();
		if (viewportAlive ()) redraw ();
	}

	/**
	 * Configures a copied logical size, or restores historical parent/zero-origin
	 * behavior when null. Clearing restores this canvas's pre-configuration bar
	 * values and visibility; it never modifies the enclosing parent's bars.
	 *
	 * @param size the logical size, or null to relinquish self-owned scrolling
	 * @since 3.137
	 */
	public void setLogicalContentSize (Point size) {
		checkWidget ();
		checkViewportAlive ();
		if (size != null) {
			setLogicalContentSize (size.x, size.y);
			return;
		}
		if (logicalWidth < 0) return;
		BarState horizontal = savedHorizontal, vertical = savedVertical;
		logicalWidth = -1;
		logicalHeight = originX = originY = 0;
		long epoch = ++viewportEpoch;
		viewportState |= VIEWPORT_RESTORE;
		try {
			if (restoreBar (getHorizontalBar (), horizontal, epoch)) {
				restoreBar (getVerticalBar (), vertical, epoch);
			}
		} finally {
			viewportState &= ~VIEWPORT_RESTORE;
			if (logicalWidth < 0) savedHorizontal = savedVertical = null;
		}
		if (viewportAlive ()) redraw ();
	}

	/**
	 * @return a defensive logical size, or null when self-owned scrolling is off
	 * @since 3.137
	 */
	public Point getLogicalContentSize () {
		checkWidget ();
		return logicalWidth < 0 ? null : new Point (logicalWidth, logicalHeight);
	}

	/**
	 * Moves the self-owned logical origin, clamped to the current client extent.
	 * Programmatic movement does not synthesize Selection or control Move events.
	 * ScrollBar.setSelection alone does not send Selection; use this method for
	 * programmatic content movement. Line increments remain configurable on the
	 * existing bars; page increments follow the current client size.
	 *
	 * @param x requested horizontal origin (negative values clamp to zero)
	 * @param y requested vertical origin (negative values clamp to zero)
	 * @exception IllegalArgumentException when self-owned scrolling is off
	 * @since 3.137
	 */
	public void setViewportOrigin (int x, int y) {
		checkWidget ();
		checkViewportAlive ();
		if (logicalWidth < 0) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		checkViewportOwner ();
		synchronizeViewport ();
		if (!viewportAlive () || logicalWidth < 0) return;
		int nextX = clampOrigin (x, logicalWidth, viewportWidth);
		int nextY = clampOrigin (y, logicalHeight, viewportHeight);
		boolean changed = nextX != originX || nextY != originY;
		if (changed) {
			originX = nextX;
			originY = nextY;
			viewportEpoch++;
			/* A renderer can change the origin during its own callback. */
			surface.invalidate ();
		}
		long epoch = viewportEpoch;
		/* Movement changes positions only. Range/layout work stays in geometry
		 * reconciliation; do not allocate Points or solve bar layout per wheel. */
		ScrollBar horizontal = liveBar (getHorizontalBar ());
		ScrollBar vertical = liveBar (getVerticalBar ());
		if (horizontal != null && horizontal.getSelection () != originX) horizontal.setSelection (originX);
		if (!sameViewport (epoch)) return;
		if (vertical != null && vertical.getSelection () != originY) vertical.setSelection (originY);
		if (viewportAlive () && changed) super.redraw ();
	}

	/**
	 * Keeps the self-owned geometry coherent when an existing scrollbar mode
	 * changes. Uses the platform's supported mode; unsupported modes stay ignored
	 * as prescribed by the superclass.
	 *
	 * @param mode the ordinary SWT scrollbar mode
	 */
	@Override
	public void setScrollbarsMode (int mode) {
		super.setScrollbarsMode (mode);
		if (logicalWidth >= 0 && surface != null && viewportAlive ()) {
			viewportState |= VIEWPORT_DIRTY;
			synchronizeViewport ();
			if (viewportAlive ()) redraw ();
		}
	}

	private void scrollViewport (Event event) {
		if (!viewportAlive () || logicalWidth < 0) return;
		ScrollBar horizontal = liveBar (getHorizontalBar ());
		ScrollBar vertical = liveBar (getVerticalBar ());
		if (event.widget != horizontal && event.widget != vertical) return;
		int x = event.widget == horizontal ? horizontal.getSelection () : originX;
		int y = event.widget == vertical ? vertical.getSelection () : originY;
		setViewportOrigin (x, y);
	}

	/* Reuse Scrollable's native wheel-to-bar processing. Do not consume or
	 * redispatch public input, alter event.widget, or install a Display filter. */
	private void synchronizeViewport () {
		if (!viewportAlive () || logicalWidth < 0) return;
		if ((viewportState & VIEWPORT_SYNC) != 0) {
			viewportState |= VIEWPORT_AGAIN;
			return;
		}
		checkViewportOwner ();
		if ((viewportState & VIEWPORT_DIRTY) == 0) return;
		viewportState |= VIEWPORT_SYNC;
		try {
			/* Native bar visibility can synchronously cause Resize. Bound each
			 * reconciliation, preserving new requests instead of recursive layout. */
			for (int pass = 0; pass < 4 && viewportAlive () && logicalWidth >= 0; pass++) {
				viewportState &= ~VIEWPORT_AGAIN;
				checkViewportOwner ();
				Rectangle client = getClientArea ();
				if (!viewportAlive () || logicalWidth < 0) return;
				ScrollBar horizontal = liveBar (getHorizontalBar ());
				ScrollBar vertical = liveBar (getVerticalBar ());
				boolean overlay = (getScrollbarsMode () & SWT.SCROLLBAR_OVERLAY) != 0;
				int barHeight = horizontal == null || overlay ? 0 : horizontal.getSize ().y;
				int barWidth = vertical == null || overlay ? 0 : vertical.getSize ().x;
				int outerWidth = saturatedExtent (client.width,
						vertical != null && vertical.getVisible () ? barWidth : 0);
				int outerHeight = saturatedExtent (client.height,
						horizontal != null && horizontal.getVisible () ? barHeight : 0);
				/* A zero client dimension has lost the hidden negative space occupied
				 * by native trim. Do not invent extra room by adding an entire bar. */
				Point bounds = getSize ();
				long border = 2L * Math.max (0, getBorderWidth ());
				outerWidth = Math.min (outerWidth, (int)Math.max (0L, bounds.x - border));
				outerHeight = Math.min (outerHeight, (int)Math.max (0L, bounds.y - border));
				ViewportRuntime.PixelLayout plan = ViewportRuntime.solvePixels (
						outerWidth, outerHeight, logicalWidth, logicalHeight, barHeight, barWidth,
						horizontal == null ? ViewportRuntime.NEVER : ViewportRuntime.AUTO,
						vertical == null ? ViewportRuntime.NEVER : ViewportRuntime.AUTO);
				viewportWidth = plan.bodyWidth ();
				viewportHeight = plan.bodyHeight ();
				int x = clampOrigin (originX, logicalWidth, viewportWidth);
				int y = clampOrigin (originY, logicalHeight, plan.bodyHeight ());
				if (x != originX || y != originY) {
					originX = x;
					originY = y;
					viewportEpoch++;
					surface.invalidate ();
				}
				long epoch = viewportEpoch;
				if (!configureBar (horizontal, logicalWidth, plan.bodyWidth (), x,
						plan.horizontalVisible (), epoch)) continue;
				if (!configureBar (vertical, logicalHeight, plan.bodyHeight (), y,
						plan.verticalVisible (), epoch)) continue;
				if (!sameViewport (epoch)) continue;
				Rectangle actual = getClientArea ();
				if (!sameViewport (epoch)) continue;
				if (actual.width == plan.bodyWidth () && actual.height == plan.bodyHeight ()
						&& (viewportState & VIEWPORT_AGAIN) == 0) {
					viewportState &= ~VIEWPORT_DIRTY;
					return;
				}
			}
		} finally {
			viewportState &= ~VIEWPORT_SYNC;
		}
		/* Retry only through the existing paint loop, never a new timer/queue. */
		if (viewportAlive () && logicalWidth >= 0) super.redraw ();
	}

	private boolean configureBar (ScrollBar bar, int extent, int visible, int selection,
			boolean shown, long epoch) {
		if (bar == null) return sameViewport (epoch);
		int maximum = Math.max (1, extent);
		int thumb = Math.min (maximum, Math.max (1, visible));
		int page = Math.max (1, visible);
		if (bar.getMinimum () != 0 || bar.getMaximum () != maximum || bar.getThumb () != thumb
				|| bar.getSelection () != selection || bar.getPageIncrement () != page) {
			bar.setValues (selection, 0, maximum, thumb, bar.getIncrement (), page);
		}
		if (!sameViewport (epoch) || bar.isDisposed ()) return false;
		if (bar.getVisible () != shown) bar.setVisible (shown);
		return sameViewport (epoch) && !bar.isDisposed ();
	}

	private boolean restoreBar (ScrollBar bar, BarState saved, long epoch) {
		if (!restoringViewport (epoch)) return false;
		if (bar != null && !bar.isDisposed () && saved != null) {
			bar.setValues (saved.selection, saved.minimum, saved.maximum, saved.thumb,
					saved.increment, saved.pageIncrement);
			if (!restoringViewport (epoch) || bar.isDisposed ()) return false;
			bar.setVisible (saved.visible);
			if (!restoringViewport (epoch) || bar.isDisposed ()) return false;
			bar.setEnabled (saved.enabled);
		}
		return restoringViewport (epoch);
	}

	private boolean restoringViewport (long epoch) {
		return viewportAlive () && logicalWidth < 0 && viewportEpoch == epoch;
	}

	private boolean sameViewport (long epoch) {
		return viewportAlive () && logicalWidth >= 0 && viewportEpoch == epoch;
	}

	private boolean viewportAlive () {
		return !isDisposed () && (viewportState & VIEWPORT_CLOSED) == 0;
	}

	private void checkViewportAlive () {
		if (!viewportAlive ()) SWT.error (SWT.ERROR_WIDGET_DISPOSED);
	}

	private void checkViewportOwner () {
		if (getParent () instanceof ScrolledComposite parent && parent.getContent () == this
				&& parent.getLogicalContentSize () != null) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	}

	private static ScrollBar liveBar (ScrollBar bar) {
		return bar == null || bar.isDisposed () ? null : bar;
	}

	private static int clampOrigin (int requested, int extent, int visible) {
		return Math.max (0, Math.min (requested, extent - Math.min (extent, Math.max (1, visible))));
	}

	private static int saturatedExtent (int body, int trim) {
		return (int)Math.min (Integer.MAX_VALUE, Math.max (0L, body) + Math.max (0L, trim));
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
		if (logicalWidth >= 0 && !isVisible ()) return;
		if (painting) {
			/* Do not recursively invoke an application renderer from update(). */
			super.redraw ();
			return;
		}
		painting = true;
		try {
			synchronizeViewport ();
			if (!viewportAlive () || event.gc.isDisposed ()) return;
			Rectangle area = getClientArea ();
			if (logicalWidth >= 0 && (area.width != viewportWidth || area.height != viewportHeight)) {
				viewportState |= VIEWPORT_DIRTY;
				synchronizeViewport ();
				if (!viewportAlive () || event.gc.isDisposed ()) return;
				area = getClientArea ();
			}
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
