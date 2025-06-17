package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.opengl.*;

import io.github.humbleui.skija.*;

public class SkiaCanvas extends GLCanvas {

	public DirectContext skijaContext;
	private BackendRenderTarget renderTarget;
	public Surface surface;
	private boolean redrawTriggered;


	public SkiaCanvas(Composite parent, int style) {
		this(parent,style,createGLData());
	}

	private static GLData createGLData() {
		GLData data = new GLData();
		data.doubleBuffer = true;
		return data;
	}

//	@Override
//	protected LRESULT WM_PAINT(long wParam, long lParam) {
//		/* Process WM_PAINT */
//
//		LRESULT re = null;
//
////		glPrepareSurface();
//		re = super.WM_PAINT(wParam, lParam);
////		glFinishDrawing();
//
//			return re;
//	}
//
//		// This is a complete override of the paint logic
//
//		if (isDisposed())
//			return null;
//
//		if (!redrawTriggered) {
//			redraw();
//			return new LRESULT(OS.DefWindowProc(handle, (int) OS.WM_PAINT, wParam, lParam));
//		}
//
//		doPaint();
//
//
//		var r = OS.DefWindowProc(handle, (int) OS.WM_PAINT, wParam, lParam);
//
//		LRESULT l = new LRESULT(r);
//		return l;
//	}

//	@Override
//	public void redraw() {
//		redrawTriggered = true;
//		super.redraw();
//	}


//	private void doPaint() {
//
//		var e = EventTool.createEvent(this, surface, SWT.Paint);
//
//		onPaint(e);
//
//		redraw();
//
//
//	}


	public SkiaCanvas(Composite parent, int style, GLData data) {
		super(parent, style, data);

		state |= FOREIGN_HANDLE;

		setCurrent();
		skijaContext = DirectContext.makeGL();


		addListener(SWT.Resize, e -> onResize(e));
//		addListener(SWT.Paint, e -> onPaint(e));

	}

	private void onResize(Event e) {
		Rectangle rect = getClientArea();

		renderTarget = BackendRenderTarget.makeGL(rect.width, rect.height, /* samples */0, /* stencil */8,
				/* fbid */0, FramebufferFormat.GR_GL_RGBA8);

		System.out.println("CreateOpenGLRenderTarget");

		if (surface != null && !surface.isClosed()) {
			surface.close();
		}

		surface = Surface.makeFromBackendRenderTarget(skijaContext, renderTarget, SurfaceOrigin.BOTTOM_LEFT,
				SurfaceColorFormat.RGBA_8888, ColorSpace.getDisplayP3(), new SurfaceProps(PixelGeometry.RGB_H));
		surface.getCanvas().clear(0xFFFFFFFF);

		skijaContext.flush();
		swapBuffers();
		redraw();
	}

//	private void onPaint(Event e) {
//		glPrepareSurface();
//		doPaint();
//		glFinishDrawing();
//	}


	long startTime = System.currentTimeMillis();

	private void drawSurface() {

			surface.getCanvas().clear(0xFFFFFFFF);

			Point size = getSize();

			long currentPosTime = System.currentTimeMillis() - startTime;

			currentPosTime = currentPosTime % 10000;

			double position = (double) currentPosTime / (double) 10000;

			int colorAsRGB = 0xFF42FFF4;
			int colorRed = 0xFFFF0000;
			int colorGreen = 0xFF00FF00;
			int colorBlue = 0xFF0000FF;

			try (var paint = new Paint()) {
				paint.setColor(colorBlue);
				surface.getCanvas().drawCircle(  (int) (position * size.x), 100, 100, paint);
			}
		}


	@Override
	GC new_GC (GCData data) {
		return GC.skiagc_new( this, data) ;
	}

}
