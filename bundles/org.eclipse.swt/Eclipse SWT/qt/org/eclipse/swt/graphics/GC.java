/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.graphics;

import com.trolltech.qt.core.QPointF;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.BGMode;
import com.trolltech.qt.core.Qt.FillRule;
import com.trolltech.qt.core.Qt.PenStyle;
import com.trolltech.qt.core.Qt.SizeMode;
import com.trolltech.qt.core.Qt.TextFlag;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QLinearGradient;
import com.trolltech.qt.gui.QPaintDeviceInterface;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QPolygon;
import com.trolltech.qt.gui.QRegion;
import com.trolltech.qt.gui.QStyleOptionFocusRect;
import com.trolltech.qt.gui.QStyle.PrimitiveElement;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.internal.qt.QtSWTConverter;

/**
 * Class <code>GC</code> is where all of the drawing capabilities that are
 * supported by SWT are located. Instances are used to draw on either an
 * <code>Image</code>, a <code>Control</code>, or directly on a
 * <code>Display</code>.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>LEFT_TO_RIGHT, RIGHT_TO_LEFT</dd>
 * </dl>
 * 
 * <p>
 * The SWT drawing coordinate system is the two-dimensional space with the
 * origin (0,0) at the top left corner of the drawing area and with (x,y) values
 * increasing to the right and downward respectively.
 * </p>
 * 
 * <p>
 * The result of drawing on an image that was created with an indexed palette
 * using a color that is not in the palette is platform specific. Some platforms
 * will match to the nearest color while other will draw the color itself. This
 * happens because the allocated image might use a direct palette on platforms
 * that do not support indexed palette.
 * </p>
 * 
 * <p>
 * Application code must explicitly invoke the <code>GC.dispose()</code> method
 * to release the operating system resources managed by each instance when those
 * instances are no longer required. This is <em>particularly</em> important on
 * Windows95 and Windows98 where the operating system has a limited number of
 * device contexts available.
 * </p>
 * 
 * <p>
 * Note: Only one of LEFT_TO_RIGHT and RIGHT_TO_LEFT may be specified.
 * </p>
 * 
 * @see org.eclipse.swt.events.PaintEvent
 * @see <a href="http://www.eclipse.org/swt/snippets/#gc">GC snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Examples:
 *      GraphicsExample, PaintExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 */
public final class GC extends Resource {

	/**
	 * the handle to the OS device context (Warning: This field is platform
	 * dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT public API.
	 * It is marked public only so that it can be shared within the packages
	 * provided by SWT. It is not available on all platforms and should never be
	 * accessed from application code.
	 * </p>
	 */
	private QPaintDeviceInterface paintDevice;
	private QPainter activePainter;
	private QRegion activeClipping;

	public int handle;

	private Drawable drawable;
	private GCData data;
	private FillRule fillRule;

	private int antialias = SWT.OFF; // TODO make sure this flag does not get lost, currently not interpreted

	private static final int FOREGROUND = 1 << 0;
	private static final int BACKGROUND = 1 << 1;
	private static final int FONT = 1 << 2;
	private static final int LINE_STYLE = 1 << 3;
	private static final int LINE_WIDTH = 1 << 4;
	private static final int LINE_CAP = 1 << 5;
	private static final int LINE_JOIN = 1 << 6;
	private static final int LINE_MITERLIMIT = 1 << 7;
	private static final int FOREGROUND_TEXT = 1 << 8;
	private static final int BACKGROUND_TEXT = 1 << 9;
	private static final int BRUSH = 1 << 10;
	private static final int PEN = 1 << 11;
	private static final int NULL_BRUSH = 1 << 12;
	private static final int NULL_PEN = 1 << 13;
	private static final int DRAW_OFFSET = 1 << 14;

	/**
	 * Prevents uninitialized instances from being created outside the package.
	 */
	GC() {
	}

	/**
	 * Constructs a new instance of this class which has been configured to draw
	 * on the specified drawable. Sets the foreground color, background color
	 * and font in the GC to match those in the drawable.
	 * <p>
	 * You must dispose the graphics context when it is no longer required.
	 * </p>
	 * 
	 * @param drawable
	 *            the drawable to draw on
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the drawable is null</li>
	 *                <li>ERROR_NULL_ARGUMENT - if there is no current device</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the drawable is an image
	 *                that is not a bitmap or an icon - if the drawable is an
	 *                image or printer that is already selected into another
	 *                graphics context</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NO_HANDLES if a handle could not be obtained for
	 *                GC creation</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS if not called from the
	 *                thread that created the drawable</li>
	 *                </ul>
	 */
	public GC(Drawable drawable) {
		this(drawable, SWT.NONE);
	}

	/**
	 * Constructs a new instance of this class which has been configured to draw
	 * on the specified drawable. Sets the foreground color, background color
	 * and font in the GC to match those in the drawable.
	 * <p>
	 * You must dispose the graphics context when it is no longer required.
	 * </p>
	 * 
	 * @param drawable
	 *            the drawable to draw on
	 * @param style
	 *            the style of GC to construct
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the drawable is null</li>
	 *                <li>ERROR_NULL_ARGUMENT - if there is no current device</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the drawable is an image
	 *                that is not a bitmap or an icon - if the drawable is an
	 *                image or printer that is already selected into another
	 *                graphics context</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NO_HANDLES if a handle could not be obtained for
	 *                GC creation</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS if not called from the
	 *                thread that created the drawable</li>
	 *                </ul>
	 * 
	 * @since 2.1.2
	 */
	public GC(Drawable drawable, int style) {
		if (drawable == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}

		GCData data = new GCData();
		data.style = checkStyle(style);
		QPaintDeviceInterface paintDevice = drawable.internal_new_GC(data);
		Device device = data.device;
		if (device == null) {
			device = Device.getDevice();
		}
		if (device == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.device = data.device = device;
		init(drawable, data, paintDevice);
		init();
	}

	private QPainter getActivePainter() {
		if (activePainter == null) {
			activePainter = new QPainter();
			//			if (paintDevice.paintingActive()) {
			//System.out.println("new painter for " + paintDevice + "active: " + paintDevice.paintingActive());
			//				//paintDevice.paintEngine().end();
			//			}
		}

		if (!activePainter.isActive()) {
			activePainter.begin(paintDevice);
		}

		if (!activePainter.isActive()) {
			System.out.println("inactive painter for: " + paintDevice + " painter: " //$NON-NLS-1$ //$NON-NLS-2$
					+ paintDevice.paintEngine().painter());
			//			RuntimeException re = new RuntimeException();
			//			re.printStackTrace();
		}

		if (activeClipping != null) {
			activePainter.setClipRegion(activeClipping);
		}
		return activePainter;
	}

	static int checkStyle(int style) {
		if ((style & SWT.LEFT_TO_RIGHT) != 0) {
			style &= ~SWT.RIGHT_TO_LEFT;
		}
		return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
	}

	void init(Drawable drawable, GCData data, QPaintDeviceInterface paintDevice) {
		this.paintDevice = paintDevice;

		Color foreground = data.foregroundColor;
		if (foreground != null) {
			data.state &= ~(FOREGROUND | FOREGROUND_TEXT | PEN);
		} else {
			data.foregroundColor = data.device.getSystemColor(SWT.COLOR_BLACK);
		}
		Color background = data.backgroundColor;
		if (background != null) {
			data.state &= ~(BACKGROUND | BACKGROUND_TEXT | BRUSH);
		} else {
			data.backgroundColor = data.device.getSystemColor(SWT.COLOR_WHITE);
		}
		data.state &= ~(NULL_BRUSH | NULL_PEN);
		Font font = data.font;
		if (font != null) {
			data.state &= ~FONT;
		} else {
			data.font = device.getSystemFont();
		}
		Image image = data.image;
		if (image != null) {
			image.setMemGC(this);
		}
		if ((data.style & SWT.RIGHT_TO_LEFT) != 0) {
			data.style |= SWT.MIRRORED;
		}
		this.drawable = drawable;
		this.data = data;
	}

	/**
	 * Copies a rectangular area of the receiver at the specified position into
	 * the image, which must be of type <code>SWT.BITMAP</code>.
	 * 
	 * @param image
	 *            the image to copy into
	 * @param x
	 *            the x coordinate in the receiver of the area to be copied
	 * @param y
	 *            the y coordinate in the receiver of the area to be copied
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the image is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image is not a bitmap
	 *                or has been disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void copyArea(Image image, int x, int y) {
		// TODO
		SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (image == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (!image.isBitmap() || image.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}

	/**
	 * Copies a rectangular area of the receiver at the source position onto the
	 * receiver at the destination position.
	 * 
	 * @param srcX
	 *            the x coordinate in the receiver of the area to be copied
	 * @param srcY
	 *            the y coordinate in the receiver of the area to be copied
	 * @param width
	 *            the width of the area to copy
	 * @param height
	 *            the height of the area to copy
	 * @param destX
	 *            the x coordinate in the receiver of the area to copy to
	 * @param destY
	 *            the y coordinate in the receiver of the area to copy to
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void copyArea(int srcX, int srcY, int width, int height, int destX, int destY) {
		copyArea(srcX, srcY, width, height, destX, destY, true);
	}

	/**
	 * Copies a rectangular area of the receiver at the source position onto the
	 * receiver at the destination position.
	 * 
	 * @param srcX
	 *            the x coordinate in the receiver of the area to be copied
	 * @param srcY
	 *            the y coordinate in the receiver of the area to be copied
	 * @param width
	 *            the width of the area to copy
	 * @param height
	 *            the height of the area to copy
	 * @param destX
	 *            the x coordinate in the receiver of the area to copy to
	 * @param destY
	 *            the y coordinate in the receiver of the area to copy to
	 * @param paint
	 *            if <code>true</code> paint events will be generated for old
	 *            and obscured areas
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public void copyArea(int srcX, int srcY, int width, int height, int destX, int destY, boolean paint) {
		SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		//TODO
		/*
		 * Feature in WinCE. The function WindowFromDC is not part of the WinCE
		 * SDK. The fix is to remember the HWND.
		 */
		//		int /* long */hwnd = data.hwnd;
		//		if (hwnd == 0) {
		//			OS.BitBlt(handle, destX, destY, width, height, handle, srcX, srcY, OS.SRCCOPY);
		//		} else {
		//			RECT lprcClip = null;
		//			int /* long */hrgn = OS.CreateRectRgn(0, 0, 0, 0);
		//			if (OS.GetClipRgn(handle, hrgn) == 1) {
		//				lprcClip = new RECT();
		//				OS.GetRgnBox(hrgn, lprcClip);
		//			}
		//			OS.DeleteObject(hrgn);
		//			RECT lprcScroll = new RECT();
		//			OS.SetRect(lprcScroll, srcX, srcY, srcX + width, srcY + height);
		//			int flags = paint ? OS.SW_INVALIDATE | OS.SW_ERASE : 0;
		//			int res = OS.ScrollWindowEx(hwnd, destX - srcX, destY - srcY, lprcScroll, lprcClip, 0, null, flags);
		//
		//			/*
		//			 * Feature in WinCE. ScrollWindowEx does not accept combined
		//			 * vertical and horizontal scrolling. The fix is to do a BitBlt and
		//			 * invalidate the appropriate source area.
		//			 */
		//		}
	}

	/**
	 * Disposes of the operating system resources associated with the graphics
	 * context. Applications must dispose of all GCs which they allocate.
	 * 
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS if not called from the
	 *                thread that created the drawable</li>
	 *                </ul>
	 */
	@Override
	void destroy() {
		if (activePainter != null) {
			activePainter.end();
			//System.out.println("end painter for " + paintDevice);
			activePainter = null;
		}

		Image image = data.image;
		if (image != null) {
			image.setMemGC(null);
		}

		if (drawable != null) {
			drawable.internal_dispose_GC(paintDevice, data);
		}
		drawable = null;
		data.image = null;
		data = null;
		paintDevice = null;
	}

	/**
	 * Draws the outline of a circular or elliptical arc within the specified
	 * rectangular area.
	 * <p>
	 * The resulting arc begins at <code>startAngle</code> and extends for
	 * <code>arcAngle</code> degrees, using the current color. Angles are
	 * interpreted such that 0 degrees is at the 3 o'clock position. A positive
	 * value indicates a counter-clockwise rotation while a negative value
	 * indicates a clockwise rotation.
	 * </p>
	 * <p>
	 * The center of the arc is the center of the rectangle whose origin is (
	 * <code>x</code>, <code>y</code>) and whose size is specified by the
	 * <code>width</code> and <code>height</code> arguments.
	 * </p>
	 * <p>
	 * The resulting arc covers an area <code>width + 1</code> pixels wide by
	 * <code>height + 1</code> pixels tall.
	 * </p>
	 * 
	 * @param x
	 *            the x coordinate of the upper-left corner of the arc to be
	 *            drawn
	 * @param y
	 *            the y coordinate of the upper-left corner of the arc to be
	 *            drawn
	 * @param width
	 *            the width of the arc to be drawn
	 * @param height
	 *            the height of the arc to be drawn
	 * @param startAngle
	 *            the beginning angle
	 * @param arcAngle
	 *            the angular extent of the arc, relative to the start angle
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		QPainter painter = getActivePainter();
		painter.setPen(getForeground().getColor());
		painter.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	/**
	 * Draws a rectangle, based on the specified arguments, which has the
	 * appearance of the platform's <em>focus rectangle</em> if the platform
	 * supports such a notion, and otherwise draws a simple rectangle in the
	 * receiver's foreground color.
	 * 
	 * @param x
	 *            the x coordinate of the rectangle
	 * @param y
	 *            the y coordinate of the rectangle
	 * @param width
	 *            the width of the rectangle
	 * @param height
	 *            the height of the rectangle
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #drawRectangle(int, int, int, int)
	 */
	public void drawFocus(int x, int y, int width, int height) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		QPainter painter = getActivePainter();
		painter.setPen(getForeground().getColor());
		QStyleOptionFocusRect option = new QStyleOptionFocusRect();
		option.rect().setRect(x, y, width, height);
		QApplication.style().drawPrimitive(PrimitiveElement.PE_FrameFocusRect, option, painter);
	}

	/**
	 * Draws the given image in the receiver at the specified coordinates.
	 * 
	 * @param image
	 *            the image to draw
	 * @param x
	 *            the x coordinate of where to draw
	 * @param y
	 *            the y coordinate of where to draw
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the image is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
	 *                disposed</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the given coordinates are
	 *                outside the bounds of the image</li>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NO_HANDLES - if no handles are available to
	 *                perform the operation</li>
	 *                </ul>
	 */
	public void drawImage(Image image, int x, int y) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (image == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (image.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}

		QPainter painter = getActivePainter();
		QPixmap pic = image.getQPixmap();
		painter.drawPixmap(x, y, pic);
	}

	/**
	 * Copies a rectangular area from the source image into a (potentially
	 * different sized) rectangular area in the receiver. If the source and
	 * destination areas are of differing sizes, then the source area will be
	 * stretched or shrunk to fit the destination area as it is copied. The copy
	 * fails if any part of the source rectangle lies outside the bounds of the
	 * source image, or if any of the width or height arguments are negative.
	 * 
	 * @param image
	 *            the source image
	 * @param srcX
	 *            the x coordinate in the source image to copy from
	 * @param srcY
	 *            the y coordinate in the source image to copy from
	 * @param srcWidth
	 *            the width in pixels to copy from the source
	 * @param srcHeight
	 *            the height in pixels to copy from the source
	 * @param destX
	 *            the x coordinate in the destination to copy to
	 * @param destY
	 *            the y coordinate in the destination to copy to
	 * @param destWidth
	 *            the width in pixels of the destination rectangle
	 * @param destHeight
	 *            the height in pixels of the destination rectangle
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the image is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
	 *                disposed</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if any of the width or height
	 *                arguments are negative.
	 *                <li>ERROR_INVALID_ARGUMENT - if the source rectangle is
	 *                not contained within the bounds of the source image</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTError
	 *                <ul>
	 *                <li>ERROR_NO_HANDLES - if no handles are available to
	 *                perform the operation</li>
	 *                </ul>
	 */
	public void drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY,
			int destWidth, int destHeight) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (srcWidth == 0 || srcHeight == 0 || destWidth == 0 || destHeight == 0) {
			return;
		}
		if (srcX < 0 || srcY < 0 || srcWidth < 0 || srcHeight < 0 || destWidth < 0 || destHeight < 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (image == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (image.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}

		QPainter painter = getActivePainter();
		painter.drawPixmap(new QRect(destX, destY, destWidth, destHeight), image.getQPixmap(), new QRect(srcX, srcY,
				srcWidth, srcHeight));
	}

	/**
	 * Draws a line, using the foreground color, between the points (
	 * <code>x1</code>, <code>y1</code>) and (<code>x2</code>, <code>y2</code>).
	 * 
	 * @param x1
	 *            the first point's x coordinate
	 * @param y1
	 *            the first point's y coordinate
	 * @param x2
	 *            the second point's x coordinate
	 * @param y2
	 *            the second point's y coordinate
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		QPainter painter = getActivePainter();
		painter.setPen(getForeground().getColor());
		painter.drawLine(x1, y1, x2, y2);
	}

	/**
	 * Draws the outline of an oval, using the foreground color, within the
	 * specified rectangular area.
	 * <p>
	 * The result is a circle or ellipse that fits within the rectangle
	 * specified by the <code>x</code>, <code>y</code>, <code>width</code>, and
	 * <code>height</code> arguments.
	 * </p>
	 * <p>
	 * The oval covers an area that is <code>width + 1</code> pixels wide and
	 * <code>height + 1</code> pixels tall.
	 * </p>
	 * 
	 * @param x
	 *            the x coordinate of the upper left corner of the oval to be
	 *            drawn
	 * @param y
	 *            the y coordinate of the upper left corner of the oval to be
	 *            drawn
	 * @param width
	 *            the width of the oval to be drawn
	 * @param height
	 *            the height of the oval to be drawn
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void drawOval(int x, int y, int width, int height) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		QPainter painter = getActivePainter();
		painter.setPen(getForeground().getColor());
		painter.drawEllipse(x, y, width, height);
	}

	/**
	 * Draws the path described by the parameter.
	 * <p>
	 * This operation requires the operating system's advanced graphics
	 * subsystem which may not be available on some platforms.
	 * </p>
	 * 
	 * @param path
	 *            the path to draw
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the parameter has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are
	 *                not available</li>
	 *                </ul>
	 * 
	 * @see Path
	 * 
	 * @since 3.1
	 */
	public void drawPath(Path path) {
		SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (path == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (path.handle == 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			//checkGC(DRAW);
			//TODO
			//		int /* long */gdipGraphics = data.gdipGraphics;
			//		Gdip.Graphics_TranslateTransform(gdipGraphics, data.gdipXOffset, data.gdipYOffset, Gdip.MatrixOrderPrepend);
			//		Gdip.Graphics_DrawPath(gdipGraphics, data.gdipPen, path.handle);
			//		Gdip.Graphics_TranslateTransform(gdipGraphics, -data.gdipXOffset, -data.gdipYOffset, Gdip.MatrixOrderPrepend);
		}
	}

	/**
	 * Draws a pixel, using the foreground color, at the specified point (
	 * <code>x</code>, <code>y</code>).
	 * <p>
	 * Note that the receiver's line attributes do not affect this operation.
	 * </p>
	 * 
	 * @param x
	 *            the point's x coordinate
	 * @param y
	 *            the point's y coordinate
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.0
	 */
	public void drawPoint(int x, int y) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		QPainter painter = getActivePainter();
		painter.setPen(getForeground().getColor());
		painter.drawPoint(x, y);
	}

	/**
	 * Draws the closed polygon which is defined by the specified array of
	 * integer coordinates, using the receiver's foreground color. The array
	 * contains alternating x and y values which are considered to represent
	 * points which are the vertices of the polygon. Lines are drawn between
	 * each consecutive pair, and between the first pair and last pair in the
	 * array.
	 * 
	 * @param pointArray
	 *            an array of alternating x and y values which are the vertices
	 *            of the polygon
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT if pointArray is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void drawPolygon(int[] pointArray) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		validatePointArray(pointArray);
		QPainter painter = getActivePainter();
		painter.setPen(getForeground().getColor());
		painter.drawPolygon(createPolygonFromArray(pointArray));
	}

	/**
	 * Draws the polyline which is defined by the specified array of integer
	 * coordinates, using the receiver's foreground color. The array contains
	 * alternating x and y values which are considered to represent points which
	 * are the corners of the polyline. Lines are drawn between each consecutive
	 * pair, but not between the first pair and last pair in the array.
	 * 
	 * @param pointArray
	 *            an array of alternating x and y values which are the corners
	 *            of the polyline
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the point array is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void drawPolyline(int[] pointArray) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (pointArray == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		validatePointArray(pointArray);
		QPainter painter = getActivePainter();
		painter.setPen(getForeground().getColor());
		painter.drawPolyline(createPolygonFromArray(pointArray));
	}

	/**
	 * Draws the outline of the rectangle specified by the arguments, using the
	 * receiver's foreground color. The left and right edges of the rectangle
	 * are at <code>x</code> and <code>x + width</code>. The top and bottom
	 * edges are at <code>y</code> and <code>y + height</code>.
	 * 
	 * @param x
	 *            the x coordinate of the rectangle to be drawn
	 * @param y
	 *            the y coordinate of the rectangle to be drawn
	 * @param width
	 *            the width of the rectangle to be drawn
	 * @param height
	 *            the height of the rectangle to be drawn
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void drawRectangle(int x, int y, int width, int height) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		QPainter painter = getActivePainter();
		painter.setPen(getForeground().getColor());
		painter.drawRect(x, y, width, height);
	}

	/**
	 * Draws the outline of the specified rectangle, using the receiver's
	 * foreground color. The left and right edges of the rectangle are at
	 * <code>rect.x</code> and <code>rect.x + rect.width</code>. The top and
	 * bottom edges are at <code>rect.y</code> and
	 * <code>rect.y + rect.height</code>.
	 * 
	 * @param rect
	 *            the rectangle to draw
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void drawRectangle(Rectangle rect) {
		if (rect == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		drawRectangle(rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * Draws the outline of the round-cornered rectangle specified by the
	 * arguments, using the receiver's foreground color. The left and right
	 * edges of the rectangle are at <code>x</code> and <code>x + width</code>.
	 * The top and bottom edges are at <code>y</code> and
	 * <code>y + height</code>. The <em>roundness</em> of the corners is
	 * specified by the <code>arcWidth</code> and <code>arcHeight</code>
	 * arguments, which are respectively the width and height of the ellipse
	 * used to draw the corners.
	 * 
	 * @param x
	 *            the x coordinate of the rectangle to be drawn
	 * @param y
	 *            the y coordinate of the rectangle to be drawn
	 * @param width
	 *            the width of the rectangle to be drawn
	 * @param height
	 *            the height of the rectangle to be drawn
	 * @param arcWidth
	 *            the width of the arc
	 * @param arcHeight
	 *            the height of the arc
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void drawRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		QPainter painter = getActivePainter();
		painter.setPen(getForeground().getColor());
		painter.drawRoundedRect(x, y, width, height, arcWidth, arcHeight, SizeMode.AbsoluteSize);
	}

	/**
	 * Draws the given string, using the receiver's current font and foreground
	 * color. No tab expansion or carriage return processing will be performed.
	 * The background of the rectangular area where the string is being drawn
	 * will be filled with the receiver's background color.
	 * 
	 * @param string
	 *            the string to be drawn
	 * @param x
	 *            the x coordinate of the top left corner of the rectangular
	 *            area where the string is to be drawn
	 * @param y
	 *            the y coordinate of the top left corner of the rectangular
	 *            area where the string is to be drawn
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void drawString(String string, int x, int y) {
		drawString(string, x, y, false);
	}

	/**
	 * Draws the given string, using the receiver's current font and foreground
	 * color. No tab expansion or carriage return processing will be performed.
	 * If <code>isTransparent</code> is <code>true</code>, then the background
	 * of the rectangular area where the string is being drawn will not be
	 * modified, otherwise it will be filled with the receiver's background
	 * color.
	 * 
	 * @param string
	 *            the string to be drawn
	 * @param x
	 *            the x coordinate of the top left corner of the rectangular
	 *            area where the string is to be drawn
	 * @param y
	 *            the y coordinate of the top left corner of the rectangular
	 *            area where the string is to be drawn
	 * @param isTransparent
	 *            if <code>true</code> the background will be transparent,
	 *            otherwise it will be opaque
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void drawString(String string, int x, int y, boolean isTransparent) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		QPainter painter = getActivePainter();
		if (isTransparent) {
			painter.setBackgroundMode(BGMode.TransparentMode);
		} else {
			painter.setBackgroundMode(BGMode.OpaqueMode);
			painter.setBackground(new QBrush(getBackground().getColor()));
		}
		painter.setFont(data.font.getQFont());
		painter.setPen(getForeground().getColor());
		//System.out.println("drawText(" + x + ", " + y + ", " + string + ")");

		painter.drawText(x, y + getHightCorrection(painter), string);
	}

	private int getHightCorrection(QPainter painter) {
		QFontMetrics fm = painter.fontMetrics();
		return fm.ascent();
	}

	/**
	 * Draws the given string, using the receiver's current font and foreground
	 * color. Tab expansion and carriage return processing are performed. The
	 * background of the rectangular area where the text is being drawn will be
	 * filled with the receiver's background color.
	 * 
	 * @param string
	 *            the string to be drawn
	 * @param x
	 *            the x coordinate of the top left corner of the rectangular
	 *            area where the text is to be drawn
	 * @param y
	 *            the y coordinate of the top left corner of the rectangular
	 *            area where the text is to be drawn
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void drawText(String string, int x, int y) {
		drawText(string, x, y, SWT.DRAW_DELIMITER | SWT.DRAW_TAB);
	}

	/**
	 * Draws the given string, using the receiver's current font and foreground
	 * color. Tab expansion and carriage return processing are performed. If
	 * <code>isTransparent</code> is <code>true</code>, then the background of
	 * the rectangular area where the text is being drawn will not be modified,
	 * otherwise it will be filled with the receiver's background color.
	 * 
	 * @param string
	 *            the string to be drawn
	 * @param x
	 *            the x coordinate of the top left corner of the rectangular
	 *            area where the text is to be drawn
	 * @param y
	 *            the y coordinate of the top left corner of the rectangular
	 *            area where the text is to be drawn
	 * @param isTransparent
	 *            if <code>true</code> the background will be transparent,
	 *            otherwise it will be opaque
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void drawText(String string, int x, int y, boolean isTransparent) {
		int flags = SWT.DRAW_DELIMITER | SWT.DRAW_TAB;
		if (isTransparent) {
			flags |= SWT.DRAW_TRANSPARENT;
		}
		drawText(string, x, y, flags);
	}

	/**
	 * Draws the given string, using the receiver's current font and foreground
	 * color. Tab expansion, line delimiter and mnemonic processing are
	 * performed according to the specified flags. If <code>flags</code>
	 * includes <code>DRAW_TRANSPARENT</code>, then the background of the
	 * rectangular area where the text is being drawn will not be modified,
	 * otherwise it will be filled with the receiver's background color.
	 * <p>
	 * The parameter <code>flags</code> may be a combination of:
	 * <dl>
	 * <dt><b>DRAW_DELIMITER</b></dt>
	 * <dd>draw multiple lines</dd>
	 * <dt><b>DRAW_TAB</b></dt>
	 * <dd>expand tabs</dd>
	 * <dt><b>DRAW_MNEMONIC</b></dt>
	 * <dd>underline the mnemonic character</dd>
	 * <dt><b>DRAW_TRANSPARENT</b></dt>
	 * <dd>transparent background</dd>
	 * </dl>
	 * </p>
	 * 
	 * @param string
	 *            the string to be drawn
	 * @param x
	 *            the x coordinate of the top left corner of the rectangular
	 *            area where the text is to be drawn
	 * @param y
	 *            the y coordinate of the top left corner of the rectangular
	 *            area where the text is to be drawn
	 * @param flags
	 *            the flags specifying how to process the text
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void drawText(String string, int x, int y, int flags) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		QPainter painter = getActivePainter();
		if ((flags & SWT.DRAW_TRANSPARENT) != 0) {
			painter.setBackgroundMode(BGMode.TransparentMode);
			painter.setBackground(new QBrush(getBackground().getColor()));
		}

		int translatedFlags = translateTextFlags(flags);

		painter.setPen(getForeground().getColor());

		QRect r = painter.boundingRect(0, 0, 0, 0, translatedFlags, string);

		//System.out.println("drawText(" + x + ", " + y + ", " + r.width() + ", " + r.height() + ", " + string + ")");
		painter.drawText(x, y, r.width(), r.height(), translatedFlags, string);
	}

	/**
	 * @param flags
	 * @return
	 */
	private int translateTextFlags(int flags) {
		int translatedFlags = AlignmentFlag.AlignTop.value() | AlignmentFlag.AlignLeft.value();
		if ((flags & SWT.DRAW_DELIMITER) == 0) {
			translatedFlags |= TextFlag.TextSingleLine.value();
		}
		if ((flags & SWT.DRAW_TAB) != 0) {
			translatedFlags |= TextFlag.TextExpandTabs.value();
		}
		if ((flags & SWT.DRAW_MNEMONIC) != 0) {
			translatedFlags |= TextFlag.TextShowMnemonic.value();
		}
		return translatedFlags;
	}

	/**
	 * Compares the argument to the receiver, and returns true if they represent
	 * the <em>same</em> object using a class specific comparison.
	 * 
	 * @param object
	 *            the object to compare with this object
	 * @return <code>true</code> if the object is the same as this object and
	 *         <code>false</code> otherwise
	 * 
	 * @see #hashCode
	 */
	@Override
	public boolean equals(Object object) {
		return object == this || object instanceof GC && paintDevice == ((GC) object).paintDevice;
	}

	/**
	 * Fills the interior of a circular or elliptical arc within the specified
	 * rectangular area, with the receiver's background color.
	 * <p>
	 * The resulting arc begins at <code>startAngle</code> and extends for
	 * <code>arcAngle</code> degrees, using the current color. Angles are
	 * interpreted such that 0 degrees is at the 3 o'clock position. A positive
	 * value indicates a counter-clockwise rotation while a negative value
	 * indicates a clockwise rotation.
	 * </p>
	 * <p>
	 * The center of the arc is the center of the rectangle whose origin is (
	 * <code>x</code>, <code>y</code>) and whose size is specified by the
	 * <code>width</code> and <code>height</code> arguments.
	 * </p>
	 * <p>
	 * The resulting arc covers an area <code>width + 1</code> pixels wide by
	 * <code>height + 1</code> pixels tall.
	 * </p>
	 * 
	 * @param x
	 *            the x coordinate of the upper-left corner of the arc to be
	 *            filled
	 * @param y
	 *            the y coordinate of the upper-left corner of the arc to be
	 *            filled
	 * @param width
	 *            the width of the arc to be filled
	 * @param height
	 *            the height of the arc to be filled
	 * @param startAngle
	 *            the beginning angle
	 * @param arcAngle
	 *            the angular extent of the arc, relative to the start angle
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #drawArc
	 */
	public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		QPainter painter = getActivePainter();
		painter.setBrush(getBackground().getColor());
		//painter.setPen(getBackground().getColor());
		painter.setPen(PenStyle.NoPen);
		painter.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	/**
	 * Fills the interior of the specified rectangle with a gradient sweeping
	 * from left to right or top to bottom progressing from the receiver's
	 * foreground color to its background color.
	 * 
	 * @param x
	 *            the x coordinate of the rectangle to be filled
	 * @param y
	 *            the y coordinate of the rectangle to be filled
	 * @param width
	 *            the width of the rectangle to be filled, may be negative
	 *            (inverts direction of gradient if horizontal)
	 * @param height
	 *            the height of the rectangle to be filled, may be negative
	 *            (inverts direction of gradient if vertical)
	 * @param vertical
	 *            if true sweeps from top to bottom, else sweeps from left to
	 *            right
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #drawRectangle(int, int, int, int)
	 */
	public void fillGradientRectangle(int x, int y, int width, int height, boolean vertical) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (width == 0 || height == 0) {
			return;
		}
		//		if (true) {
		//			return;
		//		}

		QColor fromColor = getForeground().getColor();
		QColor toColor = getBackground().getColor();

		boolean swapColors = false;
		if (width < 0) {
			x += width;
			width = -width;
			if (!vertical) {
				swapColors = true;
			}
		}
		if (height < 0) {
			y += height;
			height = -height;
			if (vertical) {
				swapColors = true;
			}
		}
		if (swapColors) {
			QColor tmp = fromColor;
			fromColor = toColor;
			toColor = tmp;
		}
		if (fromColor.equals(toColor)) {
			fillRectangle(x, y, width, height);
			return;
		}

		QPointF p1 = new QPointF(), p2 = new QPointF();
		p1.setX(x);
		p1.setY(y);
		if (vertical) {
			p2.setX(p1.x());
			p2.setY(p1.y() + height);
		} else {
			p2.setX(p1.x() + width);
			p2.setY(p1.y());
		}
		QPainter painter = getActivePainter();
		QLinearGradient fade = new QLinearGradient(p1, p2);
		fade.setColorAt(0.0, fromColor);
		fade.setColorAt(1.0, toColor);
		QBrush brush = new QBrush(fade);
		painter.fillRect(x, y, width, height, brush);
	}

	/**
	 * Fills the interior of an oval, within the specified rectangular area,
	 * with the receiver's background color.
	 * 
	 * @param x
	 *            the x coordinate of the upper left corner of the oval to be
	 *            filled
	 * @param y
	 *            the y coordinate of the upper left corner of the oval to be
	 *            filled
	 * @param width
	 *            the width of the oval to be filled
	 * @param height
	 *            the height of the oval to be filled
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #drawOval
	 */
	public void fillOval(int x, int y, int width, int height) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		QPainter painter = getActivePainter();
		painter.setBrush(getBackground().getColor());
		painter.setPen(getBackground().getColor());
		painter.drawEllipse(x, y, width, height);
	}

	/**
	 * Fills the path described by the parameter.
	 * <p>
	 * This operation requires the operating system's advanced graphics
	 * subsystem which may not be available on some platforms.
	 * </p>
	 * 
	 * @param path
	 *            the path to fill
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the parameter has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are
	 *                not available</li>
	 *                </ul>
	 * 
	 * @see Path
	 * 
	 * @since 3.1
	 */
	public void fillPath(Path path) {
		//TODO
		SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (path == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (path.handle == 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}

	/**
	 * Fills the interior of the closed polygon which is defined by the
	 * specified array of integer coordinates, using the receiver's background
	 * color. The array contains alternating x and y values which are considered
	 * to represent points which are the vertices of the polygon. Lines are
	 * drawn between each consecutive pair, and between the first pair and last
	 * pair in the array.
	 * 
	 * @param pointArray
	 *            an array of alternating x and y values which are the vertices
	 *            of the polygon
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT if pointArray is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #drawPolygon
	 */
	public void fillPolygon(int[] pointArray) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		validatePointArray(pointArray);
		QPainter painter = getActivePainter();
		painter.setBrush(getBackground().getColor());
		painter.setPen(getBackground().getColor());
		painter.drawPolygon(createPolygonFromArray(pointArray), FillRule.WindingFill);
	}

	private void validatePointArray(int[] pointArray) {
		if (pointArray == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (pointArray.length % 2 != 0) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}

	}

	public static QPolygon createPolygonFromArray(int[] pointArray) {
		QPolygon polygon = new QPolygon();
		for (int i = 0; i < pointArray.length; i += 2) {
			polygon.add(pointArray[i], pointArray[i + 1]);
		}
		return polygon;
	}

	/**
	 * Fills the interior of the rectangle specified by the arguments, using the
	 * receiver's background color.
	 * 
	 * @param x
	 *            the x coordinate of the rectangle to be filled
	 * @param y
	 *            the y coordinate of the rectangle to be filled
	 * @param width
	 *            the width of the rectangle to be filled
	 * @param height
	 *            the height of the rectangle to be filled
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #drawRectangle(int, int, int, int)
	 */
	public void fillRectangle(int x, int y, int width, int height) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (width < 0) {
			x = x + width;
			width = -width;
		}
		if (height < 0) {
			y = y + height;
			height = -height;
		}
		QPainter painter = getActivePainter();
		painter.fillRect(x, y, width, height, getBackground().getColor());
	}

	/**
	 * Fills the interior of the specified rectangle, using the receiver's
	 * background color.
	 * 
	 * @param rect
	 *            the rectangle to be filled
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the rectangle is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #drawRectangle(int, int, int, int)
	 */
	public void fillRectangle(Rectangle rect) {
		if (rect == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		fillRectangle(rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * Fills the interior of the round-cornered rectangle specified by the
	 * arguments, using the receiver's background color.
	 * 
	 * @param x
	 *            the x coordinate of the rectangle to be filled
	 * @param y
	 *            the y coordinate of the rectangle to be filled
	 * @param width
	 *            the width of the rectangle to be filled
	 * @param height
	 *            the height of the rectangle to be filled
	 * @param arcWidth
	 *            the width of the arc
	 * @param arcHeight
	 *            the height of the arc
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #drawRoundRectangle
	 */
	public void fillRoundRectangle(int x, int y, int width, int height, int arcWidth, int arcHeight) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		QPainter painter = getActivePainter();
		painter.setBrush(getBackground().getColor());
		painter.setPen(getBackground().getColor());
		painter.drawRoundedRect(x, y, width, height, arcWidth, arcHeight);
	}

	void flush() {
	}

	/**
	 * Returns the <em>advance width</em> of the specified character in the font
	 * which is currently selected into the receiver.
	 * <p>
	 * The advance width is defined as the horizontal distance the cursor should
	 * move after printing the character in the selected font.
	 * </p>
	 * 
	 * @param ch
	 *            the character to measure
	 * @return the distance in the x direction to move past the character before
	 *         painting the next
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public int getAdvanceWidth(char ch) {

		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		QPainter activePainter = getActivePainter();
		return activePainter.fontMetrics().width(ch);
		//		//checkGC(FONT);
		//		int tch = ch;
		//		if (ch > 0x7F) {
		//			TCHAR buffer = new TCHAR(getCodePage(), ch, false);
		//			tch = buffer.tcharAt(0);
		//		}
		//		int[] width = new int[1];
		//		OS.GetCharWidth(handle, tch, tch, width);
		//		return width[0];
	}

	/**
	 * Returns the receiver's alpha value. The alpha value is between 0
	 * (transparent) and 255 (opaque).
	 * 
	 * @return the alpha value
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public int getAlpha() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return data.alpha;
	}

	/**
	 * Returns the receiver's anti-aliasing setting value, which will be one of
	 * <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or <code>SWT.ON</code>.
	 * Note that this controls anti-aliasing for all <em>non-text drawing</em>
	 * operations.
	 * 
	 * @return the anti-aliasing setting
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #getTextAntialias
	 * 
	 * @since 3.1
	 */
	public int getAntialias() {
		return antialias;
		//		if (handle == 0) {
		//			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		//		}
		//		if (data.gdipGraphics == 0)
		//			return SWT.DEFAULT;
		//		int mode = Gdip.Graphics_GetSmoothingMode(data.gdipGraphics);
		//		switch (mode) {
		//		case Gdip.SmoothingModeDefault:
		//			return SWT.DEFAULT;
		//		case Gdip.SmoothingModeHighSpeed:
		//		case Gdip.SmoothingModeNone:
		//			return SWT.OFF;
		//		case Gdip.SmoothingModeAntiAlias:
		//		case Gdip.SmoothingModeAntiAlias8x8:
		//		case Gdip.SmoothingModeHighQuality:
		//			return SWT.ON;
		//		}
		//		return SWT.DEFAULT;
	}

	/**
	 * Returns the background color.
	 * 
	 * @return the receiver's background color
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Color getBackground() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return data.backgroundColor;
	}

	/**
	 * Returns the background pattern. The default value is <code>null</code>.
	 * 
	 * @return the receiver's background pattern
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see Pattern
	 * 
	 * @since 3.1
	 */
	public Pattern getBackgroundPattern() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return data.backgroundPattern;
	}

	/**
	 * Returns the width of the specified character in the font selected into
	 * the receiver.
	 * <p>
	 * The width is defined as the space taken up by the actual character, not
	 * including the leading and tailing whitespace or overhang.
	 * </p>
	 * 
	 * @param ch
	 *            the character to measure
	 * @return the width of the character
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public int getCharWidth(char ch) {
		SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		//checkGC(FONT);
		// TODO
		return 10;
	}

	/**
	 * Returns the bounding rectangle of the receiver's clipping region. If no
	 * clipping region is set, the return value will be a rectangle which covers
	 * the entire bounds of the object the receiver is drawing on.
	 * 
	 * @return the bounding rectangle of the clipping region
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Rectangle getClipping() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		QPainter painter = getActivePainter();
		if (painter.hasClipping()) {
			return QtSWTConverter.convert(painter.clipRegion());
		} else {
			return new Rectangle(0, 0, paintDevice.width(), paintDevice.height());
		}
	}

	/**
	 * Sets the region managed by the argument to the current clipping region of
	 * the receiver.
	 * 
	 * @param region
	 *            the region to fill with the clipping region
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the region is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the region is disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void getClipping(Region region) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (region == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (region.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}

		QPainter painter = getActivePainter();
		Rectangle rect;
		if (painter.hasClipping()) {
			rect = QtSWTConverter.convert(painter.clipRegion());
		} else {
			rect = new Rectangle(0, 0, paintDevice.width(), paintDevice.height());
		}
		region.add(rect);
	}

	/**
	 * Returns the receiver's fill rule, which will be one of
	 * <code>SWT.FILL_EVEN_ODD</code> or <code>SWT.FILL_WINDING</code>.
	 * 
	 * @return the receiver's fill rule
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public int getFillRule() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (fillRule == null || fillRule.equals(FillRule.WindingFill)) {
			return SWT.FILL_WINDING;
		}
		return SWT.FILL_EVEN_ODD;
	}

	/**
	 * Sets the receiver's fill rule to the parameter, which must be one of
	 * <code>SWT.FILL_EVEN_ODD</code> or <code>SWT.FILL_WINDING</code>.
	 * 
	 * @param rule
	 *            the new fill rule
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the rule is not one of
	 *                <code>SWT.FILL_EVEN_ODD</code> or
	 *                <code>SWT.FILL_WINDING</code></li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public void setFillRule(int rule) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		switch (rule) {
		case SWT.FILL_WINDING:
			fillRule = FillRule.WindingFill;
			break;
		case SWT.FILL_EVEN_ODD:
			fillRule = FillRule.OddEvenFill;
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
	}

	/**
	 * Returns the font currently being used by the receiver to draw and measure
	 * text.
	 * 
	 * @return the receiver's font
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Font getFont() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return data.font;
	}

	/**
	 * Returns a FontMetrics which contains information about the font currently
	 * being used by the receiver to draw and measure text.
	 * 
	 * @return font metrics for the receiver's font
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public FontMetrics getFontMetrics() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		//checkGC(FONT);
		//if (!paintDevice.paintingActive()) {
		return FontMetrics.internal_new(data.font);
		//		}
		//		QPainter painter = getActivePainter();
		//		painter.setFont(data.font.getQFont());
		//		try {
		//			return FontMetrics.internal_new(painter.fontMetrics());
		//		} finally {
		//			painter.end();
		//		}

	}

	/**
	 * Returns the receiver's foreground color.
	 * 
	 * @return the color used for drawing foreground things
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Color getForeground() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return data.foregroundColor;
	}

	/**
	 * Returns the foreground pattern. The default value is <code>null</code>.
	 * 
	 * @return the receiver's foreground pattern
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see Pattern
	 * 
	 * @since 3.1
	 */
	public Pattern getForegroundPattern() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return data.foregroundPattern;
	}

	/**
	 * Returns the GCData.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>GC</code>. It is marked public only so that it can be shared within
	 * the packages provided by SWT. It is not available on all platforms, and
	 * should never be called from application code.
	 * </p>
	 * 
	 * @return the receiver's GCData
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see GCData
	 * 
	 * @since 3.2
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public GCData getGCData() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return data;
	}

	/**
	 * Returns the receiver's interpolation setting, which will be one of
	 * <code>SWT.DEFAULT</code>, <code>SWT.NONE</code>, <code>SWT.LOW</code> or
	 * <code>SWT.HIGH</code>.
	 * 
	 * @return the receiver's interpolation setting
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public int getInterpolation() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return SWT.DEFAULT;
		//TODO
		//		int mode = Gdip.Graphics_GetInterpolationMode(data.gdipGraphics);
		//		switch (mode) {
		//		case Gdip.InterpolationModeDefault:
		//			return SWT.DEFAULT;
		//		case Gdip.InterpolationModeNearestNeighbor:
		//			return SWT.NONE;
		//		case Gdip.InterpolationModeBilinear:
		//		case Gdip.InterpolationModeLowQuality:
		//			return SWT.LOW;
		//		case Gdip.InterpolationModeBicubic:
		//		case Gdip.InterpolationModeHighQualityBilinear:
		//		case Gdip.InterpolationModeHighQualityBicubic:
		//		case Gdip.InterpolationModeHighQuality:
		//			return SWT.HIGH;
		//		}
		//		return SWT.DEFAULT;
	}

	/**
	 * Returns the receiver's line attributes.
	 * 
	 * @return the line attributes used for drawing lines
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.3
	 */
	public LineAttributes getLineAttributes() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		float[] dashes = null;
		if (data.lineDashes != null) {
			dashes = new float[data.lineDashes.length];
			System.arraycopy(data.lineDashes, 0, dashes, 0, dashes.length);
		}
		return new LineAttributes(data.lineWidth, data.lineCap, data.lineJoin, data.lineStyle, dashes,
				data.lineDashesOffset, data.lineMiterLimit);
	}

	/**
	 * Returns the receiver's line cap style, which will be one of the constants
	 * <code>SWT.CAP_FLAT</code>, <code>SWT.CAP_ROUND</code>, or
	 * <code>SWT.CAP_SQUARE</code>.
	 * 
	 * @return the cap style used for drawing lines
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public int getLineCap() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return data.lineCap;
	}

	/**
	 * Returns the receiver's line dash style. The default value is
	 * <code>null</code>.
	 * 
	 * @return the line dash style used for drawing lines
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public int[] getLineDash() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (data.lineDashes == null) {
			return null;
		}
		int[] lineDashes = new int[data.lineDashes.length];
		for (int i = 0; i < lineDashes.length; i++) {
			lineDashes[i] = (int) data.lineDashes[i];
		}
		return lineDashes;
	}

	/**
	 * Returns the receiver's line join style, which will be one of the
	 * constants <code>SWT.JOIN_MITER</code>, <code>SWT.JOIN_ROUND</code>, or
	 * <code>SWT.JOIN_BEVEL</code>.
	 * 
	 * @return the join style used for drawing lines
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public int getLineJoin() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return data.lineJoin;
	}

	/**
	 * Returns the receiver's line style, which will be one of the constants
	 * <code>SWT.LINE_SOLID</code>, <code>SWT.LINE_DASH</code>,
	 * <code>SWT.LINE_DOT</code>, <code>SWT.LINE_DASHDOT</code> or
	 * <code>SWT.LINE_DASHDOTDOT</code>.
	 * 
	 * @return the style used for drawing lines
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public int getLineStyle() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return data.lineStyle;
	}

	/**
	 * Returns the width that will be used when drawing lines for all of the
	 * figure drawing operations (that is, <code>drawLine</code>,
	 * <code>drawRectangle</code>, <code>drawPolyline</code>, and so forth.
	 * 
	 * @return the receiver's line width
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public int getLineWidth() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return (int) data.lineWidth;
	}

	/**
	 * Returns the receiver's style information.
	 * <p>
	 * Note that the value which is returned by this method <em>may
	 * not match</em> the value which was provided to the constructor when the
	 * receiver was created. This can occur when the underlying operating system
	 * does not support a particular combination of requested styles.
	 * </p>
	 * 
	 * @return the style bits
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 2.1.2
	 */
	public int getStyle() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return data.style;
	}

	/**
	 * Returns the receiver's text drawing anti-aliasing setting value, which
	 * will be one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or
	 * <code>SWT.ON</code>. Note that this controls anti-aliasing <em>only</em>
	 * for text drawing operations.
	 * 
	 * @return the anti-aliasing setting
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #getAntialias
	 * 
	 * @since 3.1
	 */
	public int getTextAntialias() {
		SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		//		if (data.gdipGraphics == 0)
		return SWT.DEFAULT;
		// TODO
		//		int mode = Gdip.Graphics_GetTextRenderingHint(data.gdipGraphics);
		//		switch (mode) {
		//		case Gdip.TextRenderingHintSystemDefault:
		//			return SWT.DEFAULT;
		//		case Gdip.TextRenderingHintSingleBitPerPixel:
		//		case Gdip.TextRenderingHintSingleBitPerPixelGridFit:
		//			return SWT.OFF;
		//		case Gdip.TextRenderingHintAntiAlias:
		//		case Gdip.TextRenderingHintAntiAliasGridFit:
		//		case Gdip.TextRenderingHintClearTypeGridFit:
		//			return SWT.ON;
		//		}
		//		return SWT.DEFAULT;
	}

	/**
	 * Sets the parameter to the transform that is currently being used by the
	 * receiver.
	 * 
	 * @param transform
	 *            the destination to copy the transform into
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parameter is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the parameter has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see Transform
	 * 
	 * @since 3.1
	 */
	public void getTransform(Transform transform) {
		//TODO
		SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (transform == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (transform.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			//		int /* long */gdipGraphics = data.gdipGraphics;
			//		if (gdipGraphics != 0) {
			//			Gdip.Graphics_GetTransform(gdipGraphics, transform.handle);
			//			int /* long */identity = identity();
			//			Gdip.Matrix_Invert(identity);
			//			Gdip.Matrix_Multiply(transform.handle, identity, Gdip.MatrixOrderAppend);
			//			Gdip.Matrix_delete(identity);
			//		} else {
			//			transform.setElements(1, 0, 0, 1, 0, 0);
			//		}
		}
	}

	/**
	 * Returns <code>true</code> if this GC is drawing in the mode where the
	 * resulting color in the destination is the <em>exclusive or</em> of the
	 * color values in the source and the destination, and <code>false</code> if
	 * it is drawing in the mode where the destination color is being replaced
	 * with the source color value.
	 * 
	 * @return <code>true</code> true if the receiver is in XOR mode, and false
	 *         otherwise
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public boolean getXORMode() {
		//TODO
		SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		//		int rop2 = 0;
		//		rop2 = OS.GetROP2(handle);
		return false; //rop2 == OS.R2_XORPEN;
	}

	/**
	 * If the argument is <code>true</code>, puts the receiver in a drawing mode
	 * where the resulting color in the destination is the <em>exclusive or</em>
	 * of the color values in the source and the destination, and if the
	 * argument is <code>false</code>, puts the receiver in a drawing mode where
	 * the destination color is replaced with the source color value.
	 * <p>
	 * Note that this mode in fundamentally unsupportable on certain platforms,
	 * notably Carbon (Mac OS X). Clients that want their code to run on all
	 * platforms need to avoid this method.
	 * </p>
	 * 
	 * @param xor
	 *            if <code>true</code>, then <em>xor</em> mode is used,
	 *            otherwise <em>source copy</em> mode is used
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @deprecated this functionality is not supported on some platforms
	 */
	@Deprecated
	public void setXORMode(boolean xor) {
		SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		//TODO
		//OS.SetROP2(handle, xor ? OS.R2_XORPEN : OS.R2_COPYPEN);
	}

	/**
	 * Returns an integer hash code for the receiver. Any two objects that
	 * return <code>true</code> when passed to <code>equals</code> must return
	 * the same value for this method.
	 * 
	 * @return the receiver's hash
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #equals
	 */
	@Override
	public int hashCode() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return paintDevice.hashCode();
	}

	/**
	 * Returns <code>true</code> if the receiver has a clipping region set into
	 * it, and <code>false</code> otherwise. If this method returns false, the
	 * receiver will draw on all available space in the destination. If it
	 * returns true, it will draw only in the area that is covered by the region
	 * that can be accessed with <code>getClipping(region)</code>.
	 * 
	 * @return <code>true</code> if the GC has a clipping region, and
	 *         <code>false</code> otherwise
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public boolean isClipped() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		return activeClipping != null;
	}

	/**
	 * Returns <code>true</code> if the GC has been disposed, and
	 * <code>false</code> otherwise.
	 * <p>
	 * This method gets the dispose state for the GC. When a GC has been
	 * disposed, it is an error to invoke any other method using the GC.
	 * 
	 * @return <code>true</code> when the GC is disposed and <code>false</code>
	 *         otherwise
	 */
	@Override
	public boolean isDisposed() {
		return paintDevice == null;
	}

	/**
	 * Sets the receiver to always use the operating system's advanced graphics
	 * subsystem for all graphics operations if the argument is
	 * <code>true</code>. If the argument is <code>false</code>, the advanced
	 * graphics subsystem is no longer used, advanced graphics state is cleared
	 * and the normal graphics subsystem is used from now on.
	 * <p>
	 * Normally, the advanced graphics subsystem is invoked automatically when
	 * any one of the alpha, antialias, patterns, interpolation, paths, clipping
	 * or transformation operations in the receiver is requested. When the
	 * receiver is switched into advanced mode, the advanced graphics subsystem
	 * performs both advanced and normal graphics operations. Because the two
	 * subsystems are different, their output may differ. Switching to advanced
	 * graphics before any graphics operations are performed ensures that the
	 * output is consistent.
	 * </p>
	 * <p>
	 * Advanced graphics may not be installed for the operating system. In this
	 * case, this operation does nothing. Some operating system have only one
	 * graphics subsystem, so switching from normal to advanced graphics does
	 * nothing. However, switching from advanced to normal graphics will always
	 * clear the advanced graphics state, even for operating systems that have
	 * only one graphics subsystem.
	 * </p>
	 * 
	 * @param advanced
	 *            the new advanced graphics state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #setAlpha
	 * @see #setAntialias
	 * @see #setBackgroundPattern
	 * @see #setClipping(Path)
	 * @see #setForegroundPattern
	 * @see #setLineAttributes
	 * @see #setInterpolation
	 * @see #setTextAntialias
	 * @see #setTransform
	 * @see #getAdvanced
	 * 
	 * @since 3.1
	 */
	public void setAdvanced(boolean advanced) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
	}

	/**
	 * Returns <code>true</code> if receiver is using the operating system's
	 * advanced graphics subsystem. Otherwise, <code>false</code> is returned to
	 * indicate that normal graphics are in use.
	 * <p>
	 * Advanced graphics may not be installed for the operating system. In this
	 * case, <code>false</code> is always returned. Some operating system have
	 * only one graphics subsystem. If this subsystem supports advanced
	 * graphics, then <code>true</code> is always returned. If any graphics
	 * operation such as alpha, antialias, patterns, interpolation, paths,
	 * clipping or transformation has caused the receiver to switch from regular
	 * to advanced graphics mode, <code>true</code> is returned. If the receiver
	 * has been explicitly switched to advanced mode and this mode is supported,
	 * <code>true</code> is returned.
	 * </p>
	 * 
	 * @return the advanced value
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @see #setAdvanced
	 * 
	 * @since 3.1
	 */
	public boolean getAdvanced() {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		// Qt is Advanced :)
		return true;
	}

	/**
	 * Sets the receiver's anti-aliasing value to the parameter, which must be
	 * one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or
	 * <code>SWT.ON</code>. Note that this controls anti-aliasing for all
	 * <em>non-text drawing</em> operations.
	 * <p>
	 * This operation requires the operating system's advanced graphics
	 * subsystem which may not be available on some platforms.
	 * </p>
	 * 
	 * @param antialias
	 *            the anti-aliasing setting
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the parameter is not one
	 *                of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or
	 *                <code>SWT.ON</code></li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are
	 *                not available</li>
	 *                </ul>
	 * 
	 * @see #getAdvanced
	 * @see #setAdvanced
	 * @see #setTextAntialias
	 * 
	 * @since 3.1
	 */
	public void setAntialias(int antialias) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		this.antialias = antialias;
		//		if (handle == 0) {
		//			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		// TODO
		//		if (data.gdipGraphics == 0 && antialias == SWT.DEFAULT)
		//			return;
		//		int mode = 0;
		//		switch (antialias) {
		//		case SWT.DEFAULT:
		//			mode = Gdip.SmoothingModeDefault;
		//			break;
		//		case SWT.OFF:
		//			mode = Gdip.SmoothingModeNone;
		//			break;
		//		case SWT.ON:
		//			mode = Gdip.SmoothingModeAntiAlias;
		//			break;
		//		default:
		//			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		//		}
		//		}
	}

	/**
	 * Sets the receiver's alpha value which must be between 0 (transparent) and
	 * 255 (opaque).
	 * <p>
	 * This operation requires the operating system's advanced graphics
	 * subsystem which may not be available on some platforms.
	 * </p>
	 * 
	 * @param alpha
	 *            the alpha value
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are
	 *                not available</li>
	 *                </ul>
	 * 
	 * @see #getAdvanced
	 * @see #setAdvanced
	 * 
	 * @since 3.1
	 */
	public void setAlpha(int alpha) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		//TODO
		data.alpha = alpha & 0xFF;
		data.state &= ~(BACKGROUND | FOREGROUND);
	}

	/**
	 * Sets the background color. The background color is used for fill
	 * operations and as the background color when text is drawn.
	 * 
	 * @param color
	 *            the new background color for the receiver
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the color is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the color has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void setBackground(Color color) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (color == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (color.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (data.backgroundPattern == null && data.backgroundColor.equals(color)) {
			return;
		}
		data.backgroundPattern = null;
		data.backgroundColor = color;
		data.state &= ~(BACKGROUND | BACKGROUND_TEXT);
	}

	/**
	 * Sets the background pattern. The default value is <code>null</code>.
	 * <p>
	 * This operation requires the operating system's advanced graphics
	 * subsystem which may not be available on some platforms.
	 * </p>
	 * 
	 * @param pattern
	 *            the new background pattern
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the parameter has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are
	 *                not available</li>
	 *                </ul>
	 * 
	 * @see Pattern
	 * @see #getAdvanced
	 * @see #setAdvanced
	 * 
	 * @since 3.1
	 */
	public void setBackgroundPattern(Pattern pattern) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (pattern != null && pattern.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (data.backgroundPattern == pattern) {
			return;
		}
		data.backgroundPattern = pattern;
		data.state &= ~BACKGROUND;
	}

	/**
	 * Sets the area of the receiver which can be changed by drawing operations
	 * to the rectangular area specified by the arguments.
	 * 
	 * @param x
	 *            the x coordinate of the clipping rectangle
	 * @param y
	 *            the y coordinate of the clipping rectangle
	 * @param width
	 *            the width of the clipping rectangle
	 * @param height
	 *            the height of the clipping rectangle
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void setClipping(int x, int y, int width, int height) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		activeClipping = new QRegion(x, y, width, height);
	}

	/**
	 * Sets the area of the receiver which can be changed by drawing operations
	 * to the path specified by the argument.
	 * <p>
	 * This operation requires the operating system's advanced graphics
	 * subsystem which may not be available on some platforms.
	 * </p>
	 * 
	 * @param path
	 *            the clipping path.
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the path has been disposed
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are
	 *                not available</li>
	 *                </ul>
	 * 
	 * @see Path
	 * @see #getAdvanced
	 * @see #setAdvanced
	 * 
	 * @since 3.1
	 */
	public void setClipping(Path path) {
		SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (path != null && path.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (path == null) {
			activeClipping = null;
		}
		//TODO
	}

	/**
	 * Sets the area of the receiver which can be changed by drawing operations
	 * to the rectangular area specified by the argument. Specifying
	 * <code>null</code> for the rectangle reverts the receiver's clipping area
	 * to its original value.
	 * 
	 * @param rect
	 *            the clipping rectangle or <code>null</code>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void setClipping(Rectangle rect) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (rect != null) {
			activeClipping = new QRegion(rect.x, rect.y, rect.width, rect.height);
		} else {
			activeClipping = null;
		}
	}

	/**
	 * Sets the area of the receiver which can be changed by drawing operations
	 * to the region specified by the argument. Specifying <code>null</code> for
	 * the region reverts the receiver's clipping area to its original value.
	 * 
	 * @param region
	 *            the clipping region or <code>null</code>
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the region has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void setClipping(Region region) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (region != null && region.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (region != null) {
			activeClipping = region.getQRegion();
		} else {
			activeClipping = null;
		}
	}

	/**
	 * Sets the font which will be used by the receiver to draw and measure text
	 * to the argument. If the argument is null, then a default font appropriate
	 * for the platform will be used instead.
	 * 
	 * @param font
	 *            the new font for the receiver, or null to indicate a default
	 *            font
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the font has been disposed
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void setFont(Font font) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (font != null && font.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		data.font = font != null ? font : data.device.getSystemFont();
		data.state &= ~FONT;
	}

	/**
	 * Sets the foreground color. The foreground color is used for drawing
	 * operations including when text is drawn.
	 * 
	 * @param color
	 *            the new foreground color for the receiver
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the color is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the color has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void setForeground(Color color) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (color == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (color.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (data.foregroundPattern == null && color.equals(data.foregroundColor)) {
			return;
		}
		data.foregroundPattern = null;
		data.foregroundColor = color;
		data.state &= ~(FOREGROUND | FOREGROUND_TEXT);
	}

	/**
	 * Sets the foreground pattern. The default value is <code>null</code>.
	 * <p>
	 * This operation requires the operating system's advanced graphics
	 * subsystem which may not be available on some platforms.
	 * </p>
	 * 
	 * @param pattern
	 *            the new foreground pattern
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the parameter has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are
	 *                not available</li>
	 *                </ul>
	 * 
	 * @see Pattern
	 * @see #getAdvanced
	 * @see #setAdvanced
	 * 
	 * @since 3.1
	 */
	public void setForegroundPattern(Pattern pattern) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (pattern != null && pattern.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		//		if (data.gdipGraphics == 0 && pattern == null)
		//			return;
		// initGdip();
		if (data.foregroundPattern == pattern) {
			return;
		}
		data.foregroundPattern = pattern;
		data.state &= ~FOREGROUND;
	}

	/**
	 * Sets the receiver's interpolation setting to the parameter, which must be
	 * one of <code>SWT.DEFAULT</code>, <code>SWT.NONE</code>,
	 * <code>SWT.LOW</code> or <code>SWT.HIGH</code>.
	 * <p>
	 * This operation requires the operating system's advanced graphics
	 * subsystem which may not be available on some platforms.
	 * </p>
	 * 
	 * @param interpolation
	 *            the new interpolation setting
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the rule is not one of
	 *                <code>SWT.DEFAULT</code>, <code>SWT.NONE</code>,
	 *                <code>SWT.LOW</code> or <code>SWT.HIGH</code>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are
	 *                not available</li>
	 *                </ul>
	 * 
	 * @see #getAdvanced
	 * @see #setAdvanced
	 * 
	 * @since 3.1
	 */
	public void setInterpolation(int interpolation) {
		SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		//TODO
		//if (data.gdipGraphics == 0 && interpolation == SWT.DEFAULT)
		//		int mode = 0;
		//		switch (interpolation) {
		//		case SWT.DEFAULT:
		//			mode = Gdip.InterpolationModeDefault;
		//			break;
		//		case SWT.NONE:
		//			mode = Gdip.InterpolationModeNearestNeighbor;
		//			break;
		//		case SWT.LOW:
		//			mode = Gdip.InterpolationModeLowQuality;
		//			break;
		//		case SWT.HIGH:
		//			mode = Gdip.InterpolationModeHighQuality;
		//			break;
		//		default:
		//			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		//		}
		// initGdip();
		//Gdip.Graphics_SetInterpolationMode(data.gdipGraphics, mode);
	}

	/**
	 * Sets the receiver's line attributes.
	 * <p>
	 * This operation requires the operating system's advanced graphics
	 * subsystem which may not be available on some platforms.
	 * </p>
	 * 
	 * @param attributes
	 *            the line attributes
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the attributes is null</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if any of the line attributes
	 *                is not valid</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are
	 *                not available</li>
	 *                </ul>
	 * 
	 * @see LineAttributes
	 * @see #getAdvanced
	 * @see #setAdvanced
	 * 
	 * @since 3.3
	 */
	public void setLineAttributes(LineAttributes attributes) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (attributes == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		int mask = 0;
		float lineWidth = attributes.width;
		if (lineWidth != data.lineWidth) {
			mask |= LINE_WIDTH | DRAW_OFFSET;
		}
		int lineStyle = attributes.style;
		if (lineStyle != data.lineStyle) {
			mask |= LINE_STYLE;
			switch (lineStyle) {
			case SWT.LINE_SOLID:
			case SWT.LINE_DASH:
			case SWT.LINE_DOT:
			case SWT.LINE_DASHDOT:
			case SWT.LINE_DASHDOTDOT:
				break;
			case SWT.LINE_CUSTOM:
				if (attributes.dash == null) {
					lineStyle = SWT.LINE_SOLID;
				}
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
		int join = attributes.join;
		if (join != data.lineJoin) {
			mask |= LINE_JOIN;
			switch (join) {
			case SWT.CAP_ROUND:
			case SWT.CAP_FLAT:
			case SWT.CAP_SQUARE:
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
		int cap = attributes.cap;
		if (cap != data.lineCap) {
			mask |= LINE_CAP;
			switch (cap) {
			case SWT.JOIN_MITER:
			case SWT.JOIN_ROUND:
			case SWT.JOIN_BEVEL:
				break;
			default:
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
		}
		float[] dashes = attributes.dash;
		float[] lineDashes = data.lineDashes;
		if (dashes != null && dashes.length > 0) {
			boolean changed = lineDashes == null || lineDashes.length != dashes.length;
			for (int i = 0; i < dashes.length; i++) {
				float dash = dashes[i];
				if (dash <= 0) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
				if (!changed && lineDashes[i] != dash) {
					changed = true;
				}
			}
			if (changed) {
				float[] newDashes = new float[dashes.length];
				System.arraycopy(dashes, 0, newDashes, 0, dashes.length);
				dashes = newDashes;
				mask |= LINE_STYLE;
			} else {
				dashes = lineDashes;
			}
		} else {
			if (lineDashes != null && lineDashes.length > 0) {
				mask |= LINE_STYLE;
			} else {
				dashes = lineDashes;
			}
		}
		float dashOffset = attributes.dashOffset;
		if (dashOffset != data.lineDashesOffset) {
			mask |= LINE_STYLE;
		}
		float miterLimit = attributes.miterLimit;
		if (miterLimit != data.lineMiterLimit) {
			mask |= LINE_MITERLIMIT;
		}
		// initGdip();
		if (mask == 0) {
			return;
		}
		data.lineWidth = lineWidth;
		data.lineStyle = lineStyle;
		data.lineCap = cap;
		data.lineJoin = join;
		data.lineDashes = dashes;
		data.lineDashesOffset = dashOffset;
		data.lineMiterLimit = miterLimit;
		data.state &= ~mask;
	}

	/**
	 * Sets the receiver's line cap style to the argument, which must be one of
	 * the constants <code>SWT.CAP_FLAT</code>, <code>SWT.CAP_ROUND</code>, or
	 * <code>SWT.CAP_SQUARE</code>.
	 * 
	 * @param cap
	 *            the cap style to be used for drawing lines
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the style is not valid</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public void setLineCap(int cap) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		if (data.lineCap == cap) {
			return;
		}
		switch (cap) {
		case SWT.CAP_ROUND:
		case SWT.CAP_FLAT:
		case SWT.CAP_SQUARE:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		data.lineCap = cap;
		data.state &= ~LINE_CAP;
	}

	/**
	 * Sets the receiver's line dash style to the argument. The default value is
	 * <code>null</code>. If the argument is not <code>null</code>, the
	 * receiver's line style is set to <code>SWT.LINE_CUSTOM</code>, otherwise
	 * it is set to <code>SWT.LINE_SOLID</code>.
	 * 
	 * @param dashes
	 *            the dash style to be used for drawing lines
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if any of the values in the
	 *                array is less than or equal 0</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public void setLineDash(int[] dashes) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		float[] lineDashes = data.lineDashes;
		if (dashes != null && dashes.length > 0) {
			boolean changed = data.lineStyle != SWT.LINE_CUSTOM || lineDashes == null
					|| lineDashes.length != dashes.length;
			for (int i = 0; i < dashes.length; i++) {
				int dash = dashes[i];
				if (dash <= 0) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
				if (!changed && lineDashes[i] != dash) {
					changed = true;
				}
			}
			if (!changed) {
				return;
			}
			data.lineDashes = new float[dashes.length];
			for (int i = 0; i < dashes.length; i++) {
				data.lineDashes[i] = dashes[i];
			}
			data.lineStyle = SWT.LINE_CUSTOM;
		} else {
			if (data.lineStyle == SWT.LINE_SOLID && (lineDashes == null || lineDashes.length == 0)) {
				return;
			}
			data.lineDashes = null;
			data.lineStyle = SWT.LINE_SOLID;
		}
		data.state &= ~LINE_STYLE;
	}

	/**
	 * Sets the receiver's line join style to the argument, which must be one of
	 * the constants <code>SWT.JOIN_MITER</code>, <code>SWT.JOIN_ROUND</code>,
	 * or <code>SWT.JOIN_BEVEL</code>.
	 * 
	 * @param join
	 *            the join style to be used for drawing lines
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the style is not valid</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 * 
	 * @since 3.1
	 */
	public void setLineJoin(int join) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (data.lineJoin == join) {
			return;
		}
		switch (join) {
		case SWT.JOIN_MITER:
		case SWT.JOIN_ROUND:
		case SWT.JOIN_BEVEL:
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		data.lineJoin = join;
		data.state &= ~LINE_JOIN;
	}

	/**
	 * Sets the receiver's line style to the argument, which must be one of the
	 * constants <code>SWT.LINE_SOLID</code>, <code>SWT.LINE_DASH</code>,
	 * <code>SWT.LINE_DOT</code>, <code>SWT.LINE_DASHDOT</code> or
	 * <code>SWT.LINE_DASHDOTDOT</code>.
	 * 
	 * @param lineStyle
	 *            the style to be used for drawing lines
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the style is not valid</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void setLineStyle(int lineStyle) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (data.lineStyle == lineStyle) {
			return;
		}
		switch (lineStyle) {
		case SWT.LINE_SOLID:
		case SWT.LINE_DASH:
		case SWT.LINE_DOT:
		case SWT.LINE_DASHDOT:
		case SWT.LINE_DASHDOTDOT:
			break;
		case SWT.LINE_CUSTOM:
			if (data.lineDashes == null) {
				lineStyle = SWT.LINE_SOLID;
			}
			break;
		default:
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		data.lineStyle = lineStyle;
		data.state &= ~LINE_STYLE;
	}

	/**
	 * Sets the width that will be used when drawing lines for all of the figure
	 * drawing operations (that is, <code>drawLine</code>,
	 * <code>drawRectangle</code>, <code>drawPolyline</code>, and so forth.
	 * <p>
	 * Note that line width of zero is used as a hint to indicate that the
	 * fastest possible line drawing algorithms should be used. This means that
	 * the output may be different from line width one.
	 * </p>
	 * 
	 * @param lineWidth
	 *            the width of a line
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public void setLineWidth(int lineWidth) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		if (data.lineWidth == lineWidth) {
			return;
		}
		data.lineWidth = lineWidth;
		data.state &= ~(LINE_WIDTH | DRAW_OFFSET);
	}

	/**
	 * Sets the receiver's text anti-aliasing value to the parameter, which must
	 * be one of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or
	 * <code>SWT.ON</code>. Note that this controls anti-aliasing only for all
	 * <em>text drawing</em> operations.
	 * <p>
	 * This operation requires the operating system's advanced graphics
	 * subsystem which may not be available on some platforms.
	 * </p>
	 * 
	 * @param antialias
	 *            the anti-aliasing setting
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the parameter is not one
	 *                of <code>SWT.DEFAULT</code>, <code>SWT.OFF</code> or
	 *                <code>SWT.ON</code></li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are
	 *                not available</li>
	 *                </ul>
	 * 
	 * @see #getAdvanced
	 * @see #setAdvanced
	 * @see #setAntialias
	 * 
	 * @since 3.1
	 */
	public void setTextAntialias(int antialias) {
		SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		//TODO
		//		if (data.gdipGraphics == 0 && antialias == SWT.DEFAULT)
		//			return;
		//		int textMode = 0;
		//		switch (antialias) {
		//		case SWT.DEFAULT:
		//			textMode = Gdip.TextRenderingHintSystemDefault;
		//			break;
		//		case SWT.OFF:
		//			textMode = Gdip.TextRenderingHintSingleBitPerPixelGridFit;
		//			break;
		//		case SWT.ON:
		//			int[] type = new int[1];
		//			OS.SystemParametersInfo(OS.SPI_GETFONTSMOOTHINGTYPE, 0, type, 0);
		//			if (type[0] == OS.FE_FONTSMOOTHINGCLEARTYPE) {
		//				textMode = Gdip.TextRenderingHintClearTypeGridFit;
		//			} else {
		//				textMode = Gdip.TextRenderingHintAntiAliasGridFit;
		//			}
		//			break;
		//		default:
		//			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		//		}
		//		// initGdip();
		//		Gdip.Graphics_SetTextRenderingHint(data.gdipGraphics, textMode);
	}

	/**
	 * Sets the transform that is currently being used by the receiver. If the
	 * argument is <code>null</code>, the current transform is set to the
	 * identity transform.
	 * <p>
	 * This operation requires the operating system's advanced graphics
	 * subsystem which may not be available on some platforms.
	 * </p>
	 * 
	 * @param transform
	 *            the transform to set
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the parameter has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_NO_GRAPHICS_LIBRARY - if advanced graphics are
	 *                not available</li>
	 *                </ul>
	 * 
	 * @see Transform
	 * @see #getAdvanced
	 * @see #setAdvanced
	 * 
	 * @since 3.1
	 */
	public void setTransform(Transform transform) {
		//TODO
		SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		if (transform != null && transform.isDisposed()) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			//		if (data.gdipGraphics == 0 && transform == null)
			//			return;
			//		// initGdip();
			//		int /* long */identity = identity();
			//		if (transform != null) {
			//			Gdip.Matrix_Multiply(identity, transform.handle, Gdip.MatrixOrderPrepend);
			//		}
			//		Gdip.Graphics_SetTransform(data.gdipGraphics, identity);
			//		Gdip.Matrix_delete(identity);
			//		data.state &= ~DRAW_OFFSET;
		}
	}

	/**
	 * Returns the extent of the given string. No tab expansion or carriage
	 * return processing will be performed.
	 * <p>
	 * The <em>extent</em> of a string is the width and height of the
	 * rectangular area it would cover if drawn in a particular font (in this
	 * case, the current font in the receiver).
	 * </p>
	 * 
	 * @param string
	 *            the string to measure
	 * @return a point containing the extent of the string
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Point stringExtent(String string) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}

		return _textExtent(getActivePainter(), string, 0);
	}

	/**
	 * Returns the extent of the given string. Tab expansion and carriage return
	 * processing are performed.
	 * <p>
	 * The <em>extent</em> of a string is the width and height of the
	 * rectangular area it would cover if drawn in a particular font (in this
	 * case, the current font in the receiver).
	 * </p>
	 * 
	 * @param string
	 *            the string to measure
	 * @return a point containing the extent of the string
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Point textExtent(String string) {
		return textExtent(string, SWT.DRAW_DELIMITER | SWT.DRAW_TAB);
	}

	/**
	 * Returns the extent of the given string. Tab expansion, line delimiter and
	 * mnemonic processing are performed according to the specified flags, which
	 * can be a combination of:
	 * <dl>
	 * <dt><b>DRAW_DELIMITER</b></dt>
	 * <dd>draw multiple lines</dd>
	 * <dt><b>DRAW_TAB</b></dt>
	 * <dd>expand tabs</dd>
	 * <dt><b>DRAW_MNEMONIC</b></dt>
	 * <dd>underline the mnemonic character</dd>
	 * <dt><b>DRAW_TRANSPARENT</b></dt>
	 * <dd>transparent background</dd>
	 * </dl>
	 * <p>
	 * The <em>extent</em> of a string is the width and height of the
	 * rectangular area it would cover if drawn in a particular font (in this
	 * case, the current font in the receiver).
	 * </p>
	 * 
	 * @param string
	 *            the string to measure
	 * @param flags
	 *            the flags specifying how to process the text
	 * @return a point containing the extent of the string
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                </ul>
	 */
	public Point textExtent(String string, int flags) {
		if (paintDevice == null) {
			SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
		}
		if (string == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		QPainter painter = getActivePainter();
		return _textExtent(painter, string, flags);
	}

	private Point _textExtent(QPainter painter, String string, int flags) {
		int qFlags = translateTextFlags(flags);
		painter.setFont(data.font.getQFont());
		QRect rect = painter.boundingRect((QRect) null, qFlags, string);
		//System.out.println("textExtent: " + string + " -> " + rect.width() + "x" + rect.height());
		return new Point(rect.width(), rect.height());
	}

	/**
	 * Invokes platform specific functionality to allocate a new graphics
	 * context.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>GC</code>. It is marked public only so that it can be shared within
	 * the packages provided by SWT. It is not available on all platforms, and
	 * should never be called from application code.
	 * </p>
	 * 
	 * @param drawable
	 *            the Drawable for the receiver.
	 * @param data
	 *            the data for the receiver.
	 * 
	 * @return a new <code>GC</code>
	 */
	public static GC qt_new(Drawable drawable, GCData data) {
		GC gc = new GC();
		QPaintDeviceInterface paintDevice = drawable.internal_new_GC(data);
		gc.device = data.device;
		gc.init(drawable, data, paintDevice);
		return gc;
	}

	/**
	 * Invokes platform specific functionality to wrap a graphics context.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>GC</code>. It is marked public only so that it can be shared within
	 * the packages provided by SWT. It is not available on all platforms, and
	 * should never be called from application code.
	 * </p>
	 * 
	 * @param hDC
	 *            the Windows HDC.
	 * @param data
	 *            the data for the receiver.
	 * 
	 * @return a new <code>GC</code>
	 */
	public static GC qt_new(Drawable drawable, QPaintDeviceInterface paintDevice, GCData data) {
		GC gc = new GC();
		gc.device = data.device;
		gc.init(drawable, data, paintDevice);
		return gc;
	}

	/**
	 * Returns a string containing a concise, human-readable description of the
	 * receiver.
	 * 
	 * @return a string representation of the receiver
	 */
	@Override
	public String toString() {
		if (isDisposed()) {
			return "GC {*DISPOSED*}"; //$NON-NLS-1$
		}
		return "GC {" + paintDevice + "}"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
