/*******************************************************************************
 * Copyright (c) 2025 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.internal.skia;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GCExtension;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.canvasext.DpiScaler;
import org.eclipse.swt.internal.canvasext.IExternalCanvasHandler;
import org.eclipse.swt.internal.graphics.RectangleConverter;
import org.eclipse.swt.internal.graphics.SkiaColorConverter;
import org.eclipse.swt.internal.graphics.SkiaGC;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.opengl.GLPaintEventInvoker;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Event;

import io.github.humbleui.skija.BackendRenderTarget;
import io.github.humbleui.skija.ColorAlphaType;
import io.github.humbleui.skija.ColorInfo;
import io.github.humbleui.skija.ColorSpace;
import io.github.humbleui.skija.ColorType;
import io.github.humbleui.skija.DirectContext;
import io.github.humbleui.skija.FramebufferFormat;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.ImageInfo;
import io.github.humbleui.skija.PixelGeometry;
import io.github.humbleui.skija.Surface;
import io.github.humbleui.skija.SurfaceOrigin;
import io.github.humbleui.skija.SurfaceProps;
import io.github.humbleui.types.Rect;

public final class SkiaGlCanvasExtension extends GLPaintEventInvoker
implements ISkiaCanvasExtension, IExternalCanvasHandler {

	private final DirectContext skijaContext;
	private BackendRenderTarget renderTarget;
	private Surface surface;
	private final Canvas canvas;
	private final SkiaResources resources;
	private final List<RedrawCommand> redrawCommands = new ArrayList<>();

	private final DpiScalerUtil scaler;
	private Image lastImage;

	private static final int SAMPLES = 0;

	private record RedrawCommand(Rectangle area) {
	}

	public SkiaGlCanvasExtension(Canvas canvas) {
		super(canvas, createGLData());
		this.resources = new SkiaResources(canvas, this);
		this.scaler = new DpiScalerUtil( new DpiScaler(canvas));
		setCurrent();
		skijaContext = DirectContext.makeGL();
		this.canvas = canvas;
		this.canvas.addListener(SWT.Resize, this::onResize);
		this.canvas.addListener(SWT.Dispose, e -> onDispose());
	}

	private static GLData createGLData() {
		final GLData data = new GLData();
		data.doubleBuffer = true;
		data.samples = SAMPLES;
		return data;
	}

	private void onDispose() {

		if (lastImage != null && !lastImage.isClosed()) {
			lastImage.close();
		}

		if (surface != null && !surface.isClosed()) {
			surface.close();
		}
		if (renderTarget != null && !renderTarget.isClosed()) {
			renderTarget.close();
		}

		// do not close the skijaContext, this freezes the app.

	}

	private void onResize(Event e) {

		final Rectangle rect = this.canvas.getClientArea();

		if (surface != null && !surface.isClosed()) {
			surface.close();
			surface = null;
		}

		final DpiScalerUtil util = new DpiScalerUtil(resources.getScaler());

		final var scaled = util.scaleSurfaceSize(rect.width, rect.height);
		if (renderTarget != null && !renderTarget.isClosed()) {
			renderTarget.close();
		}

		renderTarget = BackendRenderTarget.makeGL(scaled.x, scaled.y, /* samples */SAMPLES, /* stencil */0, /* fbid */0,
				FramebufferFormat.GR_GL_RGBA8);
		surface = Surface.wrapBackendRenderTarget(skijaContext, renderTarget, SurfaceOrigin.BOTTOM_LEFT,
				ColorType.RGBA_8888, ColorSpace.getSRGB(),
				new SurfaceProps(PixelGeometry.RGB_H).withDeviceIndependentFonts(false));
		if (surface != null) {
			surface.getCanvas().clear(getBackgroundForSkia());
		}
		if (this.lastImage != null && !this.lastImage.isClosed()) {
			this.lastImage.close();
			this.lastImage = null;
		}

	}

	@Override
	public Surface getSurface() {
		return surface;
	}

	@Override
	public void redrawTriggered(int x, int y, int width, int height, boolean all) {

		this.redrawCommands.add(new RedrawCommand(new Rectangle(x, y, width, height)));
		super.redrawTriggered();

	}

	@Override
	public void redrawTriggered() {
		this.redrawCommands.add(new RedrawCommand(null));
		super.redrawTriggered();
	}

	@Override
	public void doPaint(Consumer<Event> paintEventSender) {

		if (surface == null) {
			return;
		}

		final int saveCount = surface.getCanvas().getSaveCount();

		try {

			setCurrent();

			Rectangle bounds = null;

			final Rectangle ca = canvas.getClientArea();

			// canvas not visible, do nothing...
			if (ca.isEmpty()) {
				skijaContext.flush();
				return;
			}

			// for which area do we need to execute the paint events?
			// If there are redraw commands, we can limit the area to the union of the
			// specified redraw areas.
			// If there are no redraw commands, we need to execute the paint events for the
			// whole client area.
			bounds = extractRedrawArea(ca);

			// if the bounds are not equal to the client area, then the bounds area is
			// smaller, which means there is an unchanged area
			// which has to stay the same.
			// if if we don't even have an image for drawing to the area, we need to execute
			// the paint events for the whole client area,
			// otherwise we would have an unchanged area which is not drawn at all.
			if (!bounds.equals(ca)) {
				final boolean imageDrawn = drawImageToSurface();
				if (!imageDrawn) {
					bounds = ca;
				}
			}

			// if the bounds are empty, which means there is no area to draw, then we don't
			// draw at all.
			if (bounds.isEmpty()) {
				skijaContext.flush();
				return;
			}

			final DpiScalerUtil util = new DpiScalerUtil(resources.getScaler());

			// scale the bounds and clip the surface.
			final var scaledBounds = RectangleConverter.scaleUpRectangle(util,  bounds);
			// new save count for the clip, so we can restore to this point in order to stay
			// consistent for the future.
			this.surface.getCanvas().save();
			this.surface.getCanvas().clipRect(new Rect(scaledBounds.x, scaledBounds.y,
					scaledBounds.x + scaledBounds.width, scaledBounds.y + scaledBounds.height));
			this.surface.getCanvas().clear(getBackgroundForSkia());

			executePaintEvents(paintEventSender, bounds);
			// saving the drawn image for the next redraw event.
			createLastImageSnapshot();
			// carets will be drawn after the image snapshot. Carets do not belong to the
			// redraw logic.
			// A blinking caret could be implemented by redrawing the last image and
			// then draw the caret on top of it,
			// without executing the paint events again.
			// Caret support needs more modifications in canvas and caret. Not yet
			// supported.

			skijaContext.flush();

		} finally {
			if (surface != null && !surface.getCanvas().isClosed()) {
				surface.getCanvas().restoreToCount(saveCount);
			}
		}

	}

	private void executePaintEvents(Consumer<Event> consumer, Rectangle bounds) {
		final Event event = new Event();
		event.count = 1;
		event.setBounds(bounds);
		final SkiaGC gc = new SkiaGC(canvas, this, SWT.None);
		event.gc = new GCExtension(gc);
		event.display = this.canvas.getDisplay();
		try {
			consumer.accept(event);
		} finally {
			gc.dispose();
			event.gc = null;
		}
	}

	private void createLastImageSnapshot() {
		if (this.lastImage != null && !this.lastImage.isClosed()) {
			this.lastImage.close();
			this.lastImage = null;
		}
		if (surface != null && surface.getCanvas() != null && !surface.getCanvas().isClosed()) {
			this.lastImage = surface.makeImageSnapshot();
		} else {
			this.lastImage = null;
		}

	}

	private boolean drawImageToSurface() {

		if (this.lastImage != null && !this.lastImage.isClosed()) {
			this.surface.getCanvas().drawImage(this.lastImage, 0, 0);
			return true;
		}

		return false;
	}

	private Rectangle extractRedrawArea(Rectangle ca) {

		try {
			if (this.redrawCommands.isEmpty()) {
				return ca;
			}

			var area = this.redrawCommands.get(0).area;

			if (area == null) {
				this.redrawCommands.clear();
				return ca;
			}

			if (this.redrawCommands.size() > 1) {

				for (int i = 1; i < this.redrawCommands.size(); i++) {
					final var a = this.redrawCommands.get(i).area;
					if (a == null) {
						return ca;
					}
					area = area.union(a);
				}

			}
			return area.intersection(ca);

		} finally {
			this.redrawCommands.clear();
		}

	}

	private int getBackgroundForSkia() {
		return SkiaColorConverter.convertSWTColorToSkijaColor(canvas.getBackground());
	}

	@Override
	public SkiaResources getResources() {
		return this.resources;
	}

	@Override
	public Surface createSupportSurface(int width, int height) {
		final ImageInfo i = new ImageInfo(new ColorInfo(ColorType.RGBA_8888, ColorAlphaType.PREMUL, null), width,
				height);
		return Surface.makeRenderTarget(skijaContext, true, i);
	}

	@Override
	public DpiScalerUtil getScaler() {
		return scaler;
	}

}
