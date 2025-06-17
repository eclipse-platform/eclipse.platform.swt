package org.eclipse.swt.graphics;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;

import io.github.humbleui.skija.*;
import io.github.humbleui.types.*;

public final class SkijaGC extends GC {


	SkijaGC() {
	}

	@Override
	public void drawRectangle (Rectangle rect) {
		if (rect == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
		rect = DPIUtil.scaleUp(drawable, rect, getZoom());
		drawRectangleInPixels(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	void init(Drawable drawable, GCData data, long hDC) {
		super.init(drawable, data, hDC);

		glPrepareSurface();


	}


	protected void glFinishDrawing() {
		var s =  ( SkiaCanvas )drawable;
		s.skijaContext.flush();
		s.swapBuffers();
	}

	protected void glPrepareSurface() {
		var s =  ( SkiaCanvas )drawable;
		io.github.humbleui.skija.Canvas canvas = s.surface.getCanvas();
		canvas.clear(0xFFFFFFFF);
		s.redraw();
	}

	@Override
	public void dispose() {

		glFinishDrawing();
		super.dispose();

	}


	@Override
	void drawRectangleInPixels (int x, int y, int width, int height) {

		SkiaCanvas c =  ( SkiaCanvas )drawable;


		Paint p = new Paint();

		p.setColor(0xFF0000FF);

		c.surface.getCanvas().drawRect(createScaledRectangle(x,y,width ,height  ), p   );

		p.close();

	}

	private Rect createScaledRectangle(int x, int y, int width, int height) {
		return new Rect(DPIUtil.autoScaleUp(x), DPIUtil.autoScaleUp(y), DPIUtil.autoScaleUp(x + width),
				DPIUtil.autoScaleUp(y + height));
	}


}
