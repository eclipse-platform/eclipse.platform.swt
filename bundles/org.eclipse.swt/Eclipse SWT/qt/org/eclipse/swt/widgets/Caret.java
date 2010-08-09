/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
package org.eclipse.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Instances of this class provide an i-beam that is typically used as the
 * insertion point for text.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#caret">Caret snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample, Canvas tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class Caret extends Widget {
	private static final int DEFAULT_WIDTH = 1;
	private Canvas parent;
	private int x, y;
	private int width = DEFAULT_WIDTH;
	private int height;
	private boolean isVisible;
	private Image image;
	private Font font;
	private Color carentBG;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 * 
	 * @param parent
	 *            a composite control which will be the parent of the new
	 *            instance (cannot be null)
	 * @param style
	 *            the style of control to construct
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * 
	 * @see SWT
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Caret(Canvas parent, int style) {
		super(parent, style);
		this.parent = parent;
		createWidget();
	}

	void createWidget() {
		isVisible = true;
		carentBG = new Color(display, 0, 0, 0);
		if (parent.getCaret() == null) {
			parent.setCaret(this);
		}
	}

	/**
	 * Returns a rectangle describing the receiver's size and location relative
	 * to its parent (or its display if its parent is null).
	 * 
	 * @return the receiver's bounding rectangle
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Rectangle getBounds() {
		checkWidget();
		if (image != null) {
			Rectangle rect = image.getBounds();
			return new Rectangle(x, y, rect.width, rect.height);
		}
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Returns the font that the receiver will use to paint textual information.
	 * 
	 * @return the receiver's font
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Font getFont() {
		checkWidget();
		if (font != null) {
			return font;
		}
		return parent.getFont();
	}

	/**
	 * Returns the image that the receiver will use to paint the caret.
	 * 
	 * @return the receiver's image
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Image getImage() {
		checkWidget();
		return image;
	}

	/**
	 * Returns a point describing the receiver's location relative to its parent
	 * (or its display if its parent is null).
	 * 
	 * @return the receiver's location
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Point getLocation() {
		checkWidget();
		return new Point(x, y);
	}

	/**
	 * Returns the receiver's parent, which must be a <code>Canvas</code>.
	 * 
	 * @return the receiver's parent
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Canvas getParent() {
		checkWidget();
		return parent;
	}

	/**
	 * Returns a point describing the receiver's size.
	 * 
	 * @return the receiver's size
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Point getSize() {
		checkWidget();
		if (image != null) {
			Rectangle rect = image.getBounds();
			return new Point(rect.width, rect.height);
		}
		return new Point(width, height);
	}

	/**
	 * Returns <code>true</code> if the receiver is visible, and
	 * <code>false</code> otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition
	 * makes the receiver not visible, this method may still indicate that it is
	 * considered visible even though it may not actually be showing.
	 * </p>
	 * 
	 * @return the receiver's visibility state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getVisible() {
		checkWidget();
		return isVisible;
	}

	/**
	 * Returns <code>true</code> if the receiver is visible and all of the
	 * receiver's ancestors are visible and <code>false</code> otherwise.
	 * 
	 * @return the receiver's visibility state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #getVisible
	 */
	public boolean isVisible() {
		checkWidget();
		return isVisible && parent.isVisible() && parent.hasFocus();
	}

	/**
	 * Marks the receiver as visible if the argument is <code>true</code>, and
	 * marks it invisible otherwise.
	 * <p>
	 * If one of the receiver's ancestors is not visible or some other condition
	 * makes the receiver not visible, marking it visible may not actually cause
	 * it to be displayed.
	 * </p>
	 * 
	 * @param visible
	 *            the new visibility state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setVisible(boolean visible) {
		checkWidget();
		if (visible == isVisible) {
			return;
		}
		isVisible = visible;
	}

	@Override
	void releaseParent() {
		super.releaseParent();
		if (this == parent.getCaret()) {
			parent.setCaret(null);
		}
	}

	@Override
	void releaseWidget() {
		parent = null;
		image = null;
		font = null;
		carentBG.dispose();
		carentBG = null;
	}

	/**
	 * Sets the receiver's size and location to the rectangular area specified
	 * by the arguments. The <code>x</code> and <code>y</code> arguments are
	 * relative to the receiver's parent (or its display if its parent is null).
	 * 
	 * @param x
	 *            the new x coordinate for the receiver
	 * @param y
	 *            the new y coordinate for the receiver
	 * @param width
	 *            the new width for the receiver
	 * @param height
	 *            the new height for the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setBounds(int x, int y, int width, int height) {
		checkWidget();
		//System.out.println("Caret.setBounds(" + x + ", " + y + ", " + width + ", " + height + ")");
		boolean moved = this.x != x || this.y != y;
		boolean resized = this.width != width || this.height != height;
		if (!(moved || resized)) {
			return;
		}

		// invalidate old location
		parent.getQWidget().update(this.x, this.y, this.width, this.height);

		this.x = x;
		this.y = y;
		this.width = Math.max(width, DEFAULT_WIDTH);
		this.height = height;

		// invalidate new location
		parent.getQWidget().update(this.x, this.y, this.width, this.height);
	}

	/**
	 * Sets the receiver's size and location to the rectangular area specified
	 * by the argument. The <code>x</code> and <code>y</code> fields of the
	 * rectangle are relative to the receiver's parent (or its display if its
	 * parent is null).
	 * 
	 * @param rect
	 *            the new bounds for the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setBounds(Rectangle rect) {
		if (rect == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		setBounds(rect.x, rect.y, rect.width, rect.height);
	}

	void killFocus() {
		//focus = false;
	}

	void setFocus() {
		//focus = true;
	}

	/**
	 * Sets the font that the receiver will use to paint textual information to
	 * the font specified by the argument, or to the default font for that kind
	 * of control if the argument is null.
	 * 
	 * @param font
	 *            the new font (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the font has been disposed
	 *                </li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setFont(Font font) {
		checkWidget();
		if (font != null && font.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.font = font;
	}

	/**
	 * Sets the image that the receiver will use to paint the caret to the image
	 * specified by the argument, or to the default which is a filled rectangle
	 * if the argument is null
	 * 
	 * @param image
	 *            the new image (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setImage(Image image) {
		checkWidget();
		if (image != null && image.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		this.image = image;
		//	resize();
	}

	/**
	 * Sets the receiver's location to the point specified by the arguments
	 * which are relative to the receiver's parent (or its display if its parent
	 * is null).
	 * 
	 * @param x
	 *            the new x coordinate for the receiver
	 * @param y
	 *            the new y coordinate for the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setLocation(int x, int y) {
		checkWidget();
		if (this.x == x && this.y == y) {
			return;
		}
		setBounds(x, y, width, height);
	}

	/**
	 * Sets the receiver's location to the point specified by the argument which
	 * is relative to the receiver's parent (or its display if its parent is
	 * null).
	 * 
	 * @param location
	 *            the new location for the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setLocation(Point location) {
		checkWidget();
		if (location == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		setLocation(location.x, location.y);
	}

	/**
	 * Sets the receiver's size to the point specified by the arguments.
	 * 
	 * @param width
	 *            the new width for the receiver
	 * @param height
	 *            the new height for the receiver
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSize(int width, int height) {
		checkWidget();
		if (this.width == width && this.height == height) {
			return;
		}
		setBounds(x, y, width, height);
	}

	/**
	 * Sets the receiver's size to the point specified by the argument.
	 * 
	 * @param size
	 *            the new extent for the receiver
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the point is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSize(Point size) {
		checkWidget();
		if (size == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		setSize(size.x, size.y);
	}

	void draw() {
		if (!isVisible || parent == null || parent.isDisposed()) {
			return;
		}
		//System.out.println("caret paint: " + this + " @ " + getLocation());
		GCData data = new GCData();
		data.device = display;
		GC gc = GC.qt_new(parent, parent.getQWidget(), data);
		//		GdkColor color = new GdkColor ();
		//		color.red = (short) 0xffff;
		//		color.green = (short) 0xffff;
		//		color.blue = (short) 0xffff;
		//		int /*long*/ colormap = OS.gdk_colormap_get_system ();
		//		OS.gdk_colormap_alloc_color (colormap, color, true, true);
		//		OS.gdk_gc_set_foreground (gc, color);
		//		OS.gdk_gc_set_function (gc, OS.GDK_XOR);
		//		if (image != null && !image.isDisposed() && image.mask == 0) {
		//			int[] width = new int[1];
		//			int[] height = new int[1];
		//			OS.gdk_drawable_get_size(image.pixmap, width, height);
		//			int nX = x;
		//			if ((parent.style & SWT.MIRRORED) != 0) {
		//				nX = parent.getClientWidth() - width[0] - nX;
		//			}
		//			OS.gdk_draw_drawable(window, gc, image.pixmap, 0, 0, nX, y, width[0], height[0]);
		//		} else {
		int nWidth = width, nHeight = height;
		if (nWidth <= 0) {
			nWidth = DEFAULT_WIDTH;
		}
		int nX = x;
		if ((parent.style & SWT.MIRRORED) != 0) {
			nX = parent.getClientWidth() - nWidth - nX;
		}
		data.backgroundColor = carentBG;
		gc.fillRectangle(nX, y, nWidth, nHeight);
		gc.dispose();
		//}
	}
}
