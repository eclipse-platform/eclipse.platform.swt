package org.eclipse.swt.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import io.github.humbleui.skija.BackendRenderTarget;
import io.github.humbleui.skija.Canvas;
import io.github.humbleui.skija.ColorSpace;
import io.github.humbleui.skija.DirectContext;
import io.github.humbleui.skija.FramebufferFormat;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.PixelGeometry;
import io.github.humbleui.skija.Surface;
import io.github.humbleui.skija.SurfaceColorFormat;
import io.github.humbleui.skija.SurfaceOrigin;
import io.github.humbleui.skija.SurfaceProps;

// should work
public class GLCanvasSmallTest {
	public static void main(String[] args) throws Exception {
		new GLCanvasSmallTest().run();
	}

	private Display display;
	private GLCanvas glCanvas;
	private DirectContext context;
	private Surface surface;
	private BackendRenderTarget renderTarget;

	protected void run() throws Exception {
		display = new Display();

		Shell shell = new Shell(display);
		shell.setText("Red Circle: Skija SWT Demo with OpenGL");
		shell.setLayout(new FillLayout());

		shell.setSize(new Point(400, 400));

		GLData data = new GLData();
		data.doubleBuffer = true;

		glCanvas = new GLCanvas(shell, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE,
				data);
		glCanvas.setCurrent();
		context = DirectContext.makeGL();

		Listener listener = event -> {
			switch (event.type) {
				case SWT.Paint :
					onPaint(event);
					break;
				case SWT.Resize :
					onResize(event);
					break;
				case SWT.Dispose :
					onDispose();
					break;
			}
		};
		glCanvas.addListener(SWT.Paint, listener);
		glCanvas.addListener(SWT.Resize, listener);
		shell.addListener(SWT.Dispose, listener);

		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		display.dispose();
	}

	protected void release() {
		if (surface != null) {
			surface.close();
			surface = null;
		}
		if (renderTarget != null) {
			renderTarget.close();
			renderTarget = null;
		}
	}

	protected void onResize(Event event) {
		release();
		Rectangle rect = glCanvas.getClientArea();
		renderTarget = BackendRenderTarget.makeGL(rect.width, rect.height,
				/* samples */0, /* stencil */8, /* fbid */0,
				FramebufferFormat.GR_GL_RGBA8);
		surface = Surface.makeFromBackendRenderTarget(context, renderTarget,
				SurfaceOrigin.BOTTOM_LEFT, SurfaceColorFormat.RGBA_8888,
				ColorSpace.getDisplayP3(),
				new SurfaceProps(PixelGeometry.RGB_H));

		onPaint(event);

	}

	protected void onPaint(Event event) {
		if (surface == null)
			return;
		Canvas canvas = surface.getCanvas();
		paint(canvas);
		context.flush();
		glCanvas.swapBuffers();
	}

	protected void onDispose() {
		release();
		context.close();
	}

	protected void paint(Canvas canvas) {
		// canvas.clear(0xFFFFFFFF);
		// canvas.save();
		// canvas.translate(100, 100);
		// canvas.rotate(System.currentTimeMillis() % 1000 / 1000f * 360f);
		// canvas.drawRect(Rect.makeLTRB(-100, -100, 100, 100),
		// new Paint().setColor(0xFFCC3333));
		// canvas.restore();

		canvas.clear(0xFFFFFFFF);

		int width = surface.getWidth();
		int height = surface.getHeight();

		try (Paint paint = new Paint().setColor(0xFFFF0000)) {
			canvas.drawCircle(width / 2, height / 2, 100, paint);
		}

	}
}