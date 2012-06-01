/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

/**
 * Instances of this class represent popup windows that are used
 * to inform or warn the user.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>BALLOON, ICON_ERROR, ICON_INFORMATION, ICON_WARNING</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * </p><p>
 * Note: Only one of the styles ICON_ERROR, ICON_INFORMATION,
 * and ICON_WARNING may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#tooltips">Tool Tips snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.2
 * @noextend This class is not intended to be subclassed by clients.
 */
public class ToolTip extends Widget {
	Shell parent, tip;
	TrayItem item;
	int x, y;
	int [] borderPolygon;
	boolean spikeAbove, autohide;
	Listener listener, parentListener;
	TextLayout layoutText, layoutMessage;
	Region region;
	Font boldFont;
	Runnable runnable;
	
	static final int BORDER = 5;
	static final int PADDING = 5;
	static final int INSET = 4;
	static final int TIP_HEIGHT = 20;
	static final int IMAGE_SIZE = 16;
	static final int DELAY = 10000;

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a composite control which will be the parent of the new instance (cannot be null)
 * @param style the style of control to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 *
 * @see SWT#BALLOON
 * @see SWT#ICON_ERROR
 * @see SWT#ICON_INFORMATION
 * @see SWT#ICON_WARNING
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public ToolTip (Shell parent, int style) {
	super (parent, checkStyle (style));
	this.parent = parent;
	this.autohide = true;
	x = y = -1;	
	Display display = getDisplay ();
	tip = new Shell (parent, SWT.ON_TOP | SWT.NO_TRIM);
	Color background = display.getSystemColor (SWT.COLOR_INFO_BACKGROUND);
	tip.setBackground (background);
	listener = new Listener () {
		public void handleEvent (Event event) {
			switch (event.type) {
				case SWT.Dispose: onDispose (event); break;
				case SWT.Paint: onPaint (event); break;
				case SWT.MouseDown: onMouseDown (event); break;
			}
		}
	};
	addListener (SWT.Dispose, listener);
	tip.addListener (SWT.Paint, listener);
	tip.addListener (SWT.MouseDown, listener);
	parentListener = new Listener () {
		public void handleEvent (Event event) {
			dispose ();
		}
	};
	parent.addListener(SWT.Dispose, parentListener);
}

static int checkStyle (int style) {
	int mask = SWT.ICON_ERROR | SWT.ICON_INFORMATION | SWT.ICON_WARNING;
	if ((style & mask) == 0) return style;
	return checkBits (style, SWT.ICON_INFORMATION, SWT.ICON_WARNING, SWT.ICON_ERROR, 0, 0, 0);
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the receiver is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the receiver is selected.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 *
 * @param listener the listener which should be notified when the receiver is selected by the user
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #removeSelectionListener
 * @see SelectionEvent
 */
public void addSelectionListener (SelectionListener listener) {
	checkWidget ();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener (listener);
	addListener (SWT.Selection,typedListener);
	addListener (SWT.DefaultSelection,typedListener);
}

void configure () {
	Display display = parent.getDisplay ();
	int x = this.x;
	int y = this.y;
	if (x == -1 || y == -1) {
		Point point;
		if (item != null) {
			point = item.getLocation ();
		} else {
			point = display.getCursorLocation ();
		}
		x = point.x;
		y = point.y;
	}
	Monitor monitor = parent.getMonitor ();
	Rectangle dest = monitor.getBounds ();
	Point size = getSize (dest.width / 4);
	int w = size.x;
	int h = size.y;
	int t = (style & SWT.BALLOON) != 0 ? TIP_HEIGHT : 0;
	int i = (style & SWT.BALLOON) != 0 ? 16 : 0;
	tip.setSize (w, h + t);
	int [] polyline;
	spikeAbove = dest.height >= y + size.y + t;
	if (dest.width >= x + size.x) {
		if (dest.height >= y + size.y + t) {
			polyline = new int [] {
				0, 5+t, 1, 5+t, 1, 3+t, 3, 1+t, 5, 1+t, 5, t, 
				16, t, 16, 0, 35, t,
				w-5, t, w-5, 1+t, w-3, 1+t, w-1, 3+t, w-1, 5+t, w, 5+t,
				w, h-5+t, w-1, h-5+t, w-1, h-3+t, w-2, h-3+t, w-2, h-2+t, w-3, h-2+t, w-3, h-1+t, w-5, h-1+t, w-5, h+t,
				5, h+t, 5, h-1+t, 3, h-1+t, 3, h-2+t, 2, h-2+t, 2, h-3+t, 1, h-3+t, 1, h-5+t, 0, h-5+t, 
				0, 5+t};
			borderPolygon = new int[] {
					0, 5+t, 1, 4+t, 1, 3+t, 3, 1+t,  4, 1+t, 5, t, 
					16, t, 16, 1, 35, t,
					w-6, 0+t, w-5, 1+t, w-4, 1+t, w-2, 3+t, w-2, 4+t, w-1, 5+t,
					w-1, h-6+t, w-2, h-5+t, w-2, h-4+t, w-4, h-2+t, w-5, h-2+t, w-6, h-1+t,
					5, h-1+t, 4, h-2+t, 3, h-2+t, 1, h-4+t, 1, h-5+t, 0, h-6+t, 
					0, 5+t};
			tip.setLocation (Math.max (0, x - i), y);
		} else {
			polyline = new int [] {
				0, 5, 1, 5, 1, 3, 3, 1, 5, 1, 5, 0, 
				w-5, 0, w-5, 1, w-3, 1, w-1, 3, w-1, 5, w, 5,
				w, h-5, w-1, h-5, w-1, h-3, w-2, h-3, w-2, h-2, w-3, h-2, w-3, h-1, w-5, h-1, w-5, h,
				35, h, 16, h+t, 16, h,
				5, h, 5, h-1, 3, h-1, 3, h-2, 2, h-2, 2, h-3, 1, h-3, 1, h-5, 0, h-5, 
				0, 5};
			borderPolygon = new int[] {
					0, 5, 1, 4, 1, 3, 3, 1,  4, 1, 5, 0, 
					w-6, 0, w-5, 1, w-4, 1, w-2, 3, w-2, 4, w-1, 5,
					w-1, h-6, w-2, h-5, w-2, h-4, w-4, h-2, w-5, h-2, w-6, h-1,
					36, h-1, 16, h+t-1, 16, h-1,
					5, h-1, 4, h-2, 3, h-2, 1, h-4, 1, h-5, 0, h-6, 
					0, 5};
			tip.setLocation (Math.max (0, x - i), y - size.y - t);
		}
	} else {
		if (dest.height >= y + size.y + t) {
			polyline = new int [] {
				0, 5+t, 1, 5+t, 1, 3+t, 3, 1+t, 5, 1+t, 5, t, 
				w-35, t, w-16, 0, w-16, t,
				w-5, t, w-5, 1+t, w-3, 1+t, w-1, 3+t, w-1, 5+t, w, 5+t,
				w, h-5+t, w-1, h-5+t, w-1, h-3+t, w-2, h-3+t, w-2, h-2+t, w-3, h-2+t, w-3, h-1+t, w-5, h-1+t, w-5, h+t,
				5, h+t, 5, h-1+t, 3, h-1+t, 3, h-2+t, 2, h-2+t, 2, h-3+t, 1, h-3+t, 1, h-5+t, 0, h-5+t, 
				0, 5+t};
			borderPolygon = new int[] {
					0, 5+t, 1, 4+t, 1, 3+t, 3, 1+t,  4, 1+t, 5, t, 
					w-35, t, w-17, 2, w-17, t,
					w-6, t, w-5, 1+t, w-4, 1+t, w-2, 3+t, w-2, 4+t, w-1, 5+t,
					w-1, h-6+t, w-2, h-5+t, w-2, h-4+t, w-4, h-2+t, w-5, h-2+t, w-6, h-1+t,
					5, h-1+t, 4, h-2+t, 3, h-2+t, 1, h-4+t, 1, h-5+t, 0, h-6+t, 
					0, 5+t};
			tip.setLocation (Math.min (dest.width - size.x, x - size.x + i), y);
		} else {
			polyline = new int [] {
				0, 5, 1, 5, 1, 3, 3, 1, 5, 1, 5, 0, 
				w-5, 0, w-5, 1, w-3, 1, w-1, 3, w-1, 5, w, 5,
				w, h-5, w-1, h-5, w-1, h-3, w-2, h-3, w-2, h-2, w-3, h-2, w-3, h-1, w-5, h-1, w-5, h,
				w-16, h, w-16, h+t, w-35, h,
				5, h, 5, h-1, 3, h-1, 3, h-2, 2, h-2, 2, h-3, 1, h-3, 1, h-5, 0, h-5, 
				0, 5};
			borderPolygon = new int[] {
					0, 5, 1, 4, 1, 3, 3, 1,  4, 1, 5, 0, 
					w-6, 0, w-5, 1, w-4, 1, w-2, 3, w-2, 4, w-1, 5,
					w-1, h-6, w-2, h-5, w-2, h-4, w-4, h-2, w-5, h-2, w-6, h-1,
					w-17, h-1, w-17, h+t-2, w-36, h-1,
					5, h-1, 4, h-2, 3, h-2, 1, h-4, 1, h-5, 0, h-6, 
					0, 5};
			tip.setLocation (Math.min (dest.width - size.x, x - size.x + i), y - size.y - t);
		}
	}	
	if ((style & SWT.BALLOON) != 0) {
		if (region != null) region.dispose ();
		region = new Region (display);
		region.add (polyline);
		tip.setRegion (region);
	}
}

/**
 * Returns <code>true</code> if the receiver is automatically
 * hidden by the platform, and <code>false</code> otherwise.
 *
 * @return the receiver's auto hide state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 */
public boolean getAutoHide () {
	checkWidget ();
	return autohide;
}

Point getSize (int maxWidth) {
	int textWidth = 0, messageWidth = 0;
	if (layoutText != null) {
		layoutText.setWidth (-1);
		textWidth = layoutText.getBounds ().width;
	}
	if (layoutMessage != null) {
		layoutMessage.setWidth (-1);
		messageWidth = layoutMessage.getBounds ().width;
	}
	int messageTrim = 2 * INSET + 2 * BORDER + 2 * PADDING;
	boolean hasImage = 	layoutText != null && (style & SWT.BALLOON) != 0 && (style & (SWT.ICON_ERROR | SWT.ICON_INFORMATION | SWT.ICON_WARNING)) != 0;
	int textTrim = messageTrim + (hasImage ? IMAGE_SIZE : 0);
	int width = Math.min (maxWidth, Math.max (textWidth + textTrim, messageWidth + messageTrim));
	int textHeight = 0, messageHeight = 0;
	if (layoutText != null) {
		layoutText.setWidth (maxWidth - textTrim);	
		textHeight = layoutText.getBounds ().height;
	}
	if (layoutMessage != null) {
		layoutMessage.setWidth (maxWidth - messageTrim);
		messageHeight = layoutMessage.getBounds ().height;
	}
	int height = 2 * BORDER + 2 * PADDING + messageHeight;
	if (layoutText != null) height += Math.max (IMAGE_SIZE, textHeight) + 2 * PADDING;
	return new Point (width, height);
}

/**
 * Returns the receiver's message, which will be an empty
 * string if it has never been set.
 *
 * @return the receiver's message
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getMessage () {
	checkWidget ();
	return layoutMessage != null ? layoutMessage.getText() : "";
}

/**
 * Returns the receiver's parent, which must be a <code>Shell</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Shell getParent () {
	checkWidget ();
	return parent;
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set.
 *
 * @return the receiver's text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public String getText () {
	checkWidget ();
	return layoutText != null ? layoutText.getText() : "";
}

/**
 * Returns <code>true</code> if the receiver is visible, and
 * <code>false</code> otherwise.
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, this method
 * may still indicate that it is considered visible even though
 * it may not actually be showing.
 * </p>
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getVisible () {
	checkWidget ();
	return tip.getVisible ();
}

/**
 * Returns <code>true</code> if the receiver is visible and all
 * of the receiver's ancestors are visible and <code>false</code>
 * otherwise.
 *
 * @return the receiver's visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see #getVisible
 */
public boolean isVisible () {
	checkWidget ();
	return getVisible ();
}

void onDispose (Event event) {
	Control parent = getParent ();
	parent.removeListener (SWT.Dispose, parentListener);
	removeListener (SWT.Dispose, listener);
	notifyListeners (SWT.Dispose, event);
	event.type = SWT.None;

	if (runnable != null) {
		Display display = getDisplay ();
		display.timerExec (-1, runnable);
	}
	runnable = null;
	tip.dispose ();
	tip = null;
	if (region != null) region.dispose ();
	region = null;
	if (layoutText != null) layoutText.dispose ();
	layoutText = null;
	if (layoutMessage != null) layoutMessage.dispose ();
	layoutMessage = null;
	if (boldFont != null) boldFont.dispose ();
	boldFont = null;
	borderPolygon = null;
}

void onMouseDown (Event event) {
	sendSelectionEvent (SWT.Selection, null, true);
	setVisible (false);
}

void onPaint (Event event) {
	GC gc = event.gc;
	int x = BORDER + PADDING;
	int y = BORDER + PADDING;
	if ((style & SWT.BALLOON) != 0) {
		if (spikeAbove) y += TIP_HEIGHT;
		gc.drawPolygon (borderPolygon);
	} else {
		Rectangle rect = tip.getClientArea ();
		gc.drawRectangle(rect.x, rect.y, rect.width - 1, rect.height -1);
	} 
	if (layoutText != null) {
		int id = style & (SWT.ICON_ERROR | SWT.ICON_INFORMATION | SWT.ICON_WARNING);
		if ((style & SWT.BALLOON) != 0 && id != 0) {
			Display display = getDisplay ();
			Image image = display.getSystemImage (id);
			Rectangle rect = image.getBounds ();
			gc.drawImage (image, 0, 0, rect.width, rect.height, x, y, IMAGE_SIZE, IMAGE_SIZE);
			x += IMAGE_SIZE;
		}
		x += INSET;
		layoutText.draw (gc, x, y);
		y += 2 * PADDING + Math.max (IMAGE_SIZE, layoutText.getBounds ().height);
	}
	if (layoutMessage != null) {
		x = BORDER + PADDING + INSET;
		layoutMessage.draw (gc, x, y);
	}
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the receiver is selected by the user.
 *
 * @param listener the listener which should no longer be notified
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @see SelectionListener
 * @see #addSelectionListener
 */
public void removeSelectionListener (SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook (SWT.Selection, listener);
	eventTable.unhook (SWT.DefaultSelection,listener);	
}

/**
 * Makes the receiver hide automatically when <code>true</code>,
 * and remain visible when <code>false</code>.
 *
 * @param autoHide the auto hide state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #getVisible
 * @see #setVisible
 */
public void setAutoHide (boolean autoHide) {
	checkWidget ();
	this.autohide = autoHide;
	//TODO - update when visible
}

/**
 * Sets the location of the receiver, which must be a tooltip,
 * to the point specified by the arguments which are relative
 * to the display.
 * <p>
 * Note that this is different from most widgets where the
 * location of the widget is relative to the parent.
 * </p>
 *
 * @param x the new x coordinate for the receiver
 * @param y the new y coordinate for the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (int x, int y) {
	checkWidget ();
	if (this.x == x && this.y == y) return;
	this.x = x;
	this.y = y;
	if (tip.getVisible ()) configure ();
}

/**
 * Sets the location of the receiver, which must be a tooltip,
 * to the point specified by the argument which is relative
 * to the display.
 * <p>
 * Note that this is different from most widgets where the
 * location of the widget is relative to the parent.
 * </p><p>
 * Note that the platform window manager ultimately has control
 * over the location of tooltips.
 * </p>
 *
 * @param location the new location for the receiver
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the point is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setLocation (Point location) {
	checkWidget ();
	if (location == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	setLocation (location.x, location.y);
}

/**
 * Sets the receiver's message.
 *
 * @param string the new message
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMessage (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (layoutMessage != null) layoutMessage.dispose();
	layoutMessage = null;
	if (string.length () != 0) {
		Display display = getDisplay (); 
		layoutMessage = new TextLayout (display);
		layoutMessage.setText (string);
	}
	if (tip.getVisible ()) configure ();
}

/**
 * Sets the receiver's text.
 *
 * @param string the new text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (layoutText != null) layoutText.dispose ();
	layoutText = null;
	if (boldFont != null) boldFont.dispose ();
	boldFont = null;
	if (string.length () != 0) {
		Display display = getDisplay ();
		layoutText = new TextLayout (display);
		layoutText.setText (string);
		Font font = display.getSystemFont ();
		FontData data = font.getFontData () [0];
		boldFont = new Font (display, data.getName (), data.getHeight (), SWT.BOLD);
		TextStyle style = new TextStyle (boldFont, null, null);
		layoutText.setStyle (style, 0, string.length ());
	}
	if (tip.getVisible ()) configure ();
}

/**
 * Marks the receiver as visible if the argument is <code>true</code>,
 * and marks it invisible otherwise. 
 * <p>
 * If one of the receiver's ancestors is not visible or some
 * other condition makes the receiver not visible, marking
 * it visible may not actually cause it to be displayed.
 * </p>
 *
 * @param visible the new visibility state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setVisible (boolean visible) {
	checkWidget ();
	if (visible) configure ();
	tip.setVisible (visible);
	Display display = getDisplay ();
	if (runnable != null) display.timerExec (-1, runnable);
	runnable = null;
	if (autohide && visible) {
		runnable = new Runnable () {
			public void run () {
				if (!isDisposed ()) setVisible (false);
			}
		};
		display.timerExec(DELAY, runnable);
	}
}

}
