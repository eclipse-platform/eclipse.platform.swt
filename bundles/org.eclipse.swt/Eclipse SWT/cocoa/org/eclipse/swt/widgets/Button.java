/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class represent a selectable user interface object that
 * issues notification when pressed and released.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ARROW, CHECK, PUSH, RADIO, TOGGLE, FLAT, WRAP</dd>
 * <dd>UP, DOWN, LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles ARROW, CHECK, PUSH, RADIO, and TOGGLE
 * may be specified.
 * </p><p>
 * Note: Only one of the styles LEFT, RIGHT, and CENTER may be specified.
 * </p><p>
 * Note: Only one of the styles UP, DOWN, LEFT, and RIGHT may be specified
 * when the ARROW style is specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#button">Button snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Button extends Control {
	String text;
	Image image;
	boolean grayed;

	/**
	 * On MacOS 10.8 and later Radiobuttons with the same parent view and action are automatically in group,
	 * which is avoided by wrapping them individually in an extra parent view.
	 */
	private SWTView radioParent;

	private static final double [] DEFAULT_DISABLED_FOREGROUND = new double [] { 0.6745f, 0.6745f, 0.6745f, 1.0f };

	static final int EXTRA_HEIGHT = 2;
	static final int EXTRA_WIDTH = 6;
	static final int IMAGE_GAP = 2;
	static final int SMALL_BUTTON_HEIGHT = 28;
	static final int REGULAR_BUTTON_HEIGHT = 32;
	static final int MAX_SIZE = 40000;

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
 * @see SWT#ARROW
 * @see SWT#CHECK
 * @see SWT#PUSH
 * @see SWT#RADIO
 * @see SWT#TOGGLE
 * @see SWT#FLAT
 * @see SWT#UP
 * @see SWT#DOWN
 * @see SWT#LEFT
 * @see SWT#RIGHT
 * @see SWT#CENTER
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Button (Composite parent, int style) {
	super (parent, checkStyle (style));
}

/**
 * Adds the listener to the collection of listeners who will
 * be notified when the control is selected by the user, by sending
 * it one of the messages defined in the <code>SelectionListener</code>
 * interface.
 * <p>
 * <code>widgetSelected</code> is called when the control is selected by the user.
 * <code>widgetDefaultSelected</code> is not called.
 * </p>
 * <p>
 * When the <code>SWT.RADIO</code> style bit is set, the <code>widgetSelected</code> method is
 * also called when the receiver loses selection because another item in the same radio group
 * was selected by the user. During <code>widgetSelected</code> the application can use
 * <code>getSelection()</code> to determine the current selected state of the receiver.
 * </p>
 *
 * @param listener the listener which should be notified
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
public void addSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	TypedListener typedListener = new TypedListener(listener);
	addListener(SWT.Selection,typedListener);
	addListener(SWT.DefaultSelection,typedListener);
}

@Override
NSSize cellSizeForBounds (long id, long sel, NSRect cellFrame) {
	NSSize size = super.cellSizeForBounds(id, sel, cellFrame);
	if (image != null && ((style & (SWT.CHECK|SWT.RADIO)) !=0)) {
		NSSize imageSize = image.handle.size();
		size.width += imageSize.width + IMAGE_GAP;
		size.height = Math.max(size.height, imageSize.height);
	}

	if (((style & (SWT.PUSH|SWT.TOGGLE)) !=0) && (style & (SWT.FLAT|SWT.WRAP)) == 0) {
		if (image != null) {
			NSCell cell = new NSCell(id);
			if (cell.controlSize() == OS.NSSmallControlSize) size.height += EXTRA_HEIGHT;
		}
		// TODO: Why is this necessary?
		size.width += EXTRA_WIDTH;
	}

	if ((style & SWT.WRAP) != 0 && text.length() != 0 && cellFrame.width < MAX_SIZE) {
		NSCell cell = new NSCell (id);
		NSRect titleRect = cell.titleRectForBounds(cellFrame);
		NSSize wrapSize = new NSSize();
		wrapSize.width = titleRect.width;
		wrapSize.height = MAX_SIZE;
		NSAttributedString attribStr = createString(text, null, foreground, style, true, true, true);
		NSRect rect = attribStr.boundingRectWithSize(wrapSize, OS.NSStringDrawingUsesLineFragmentOrigin);
		attribStr.release();
		// Avoid trim height to be set to a negative value
		double trimHeight = Math.max(size.height - titleRect.height, 0);
		size.height = rect.height;
		if (image != null && ((style & (SWT.CHECK|SWT.RADIO)) !=0)) {
			NSSize imageSize = image.handle.size();
			size.height = Math.max(size.height, imageSize.height);
		}
		size.height += trimHeight;
	}
	return size;
}

static int checkStyle (int style) {
	style = checkBits (style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO, SWT.TOGGLE, 0);
	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
		return checkBits (style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
	}
	if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
		return checkBits (style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
	}
	if ((style & SWT.ARROW) != 0) {
		style |= SWT.NO_FOCUS;
		return checkBits (style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0, 0);
	}
	return style;
}

void click () {
	sendSelectionEvent (SWT.Selection);
}

@Override
public Point computeSize (int wHint, int hHint, boolean changed) {
	checkWidget();
	if ((style & SWT.ARROW) != 0) {
		// TODO use some OS metric instead of hardcoded values
		int width = wHint != SWT.DEFAULT ? wHint : 14;
		int height = hHint != SWT.DEFAULT ? hHint : 14;
		return new Point (width, height);
	}
	NSSize size = null;
	NSCell cell = ((NSButton)view).cell ();
	if ((style & SWT.WRAP) != 0 && wHint != SWT.DEFAULT) {
		NSRect rect = new NSRect ();
		rect.width = wHint;
		rect.height = hHint != SWT.DEFAULT ? hHint : MAX_SIZE;
		size = cell.cellSizeForBounds (rect);
	} else {
		size = cell.cellSize ();
	}
	int width = (int)Math.ceil (size.width);
	int height = (int)Math.ceil (size.height);
	if (wHint != SWT.DEFAULT) width = wHint;
	if (hHint != SWT.DEFAULT) height = hHint;
	return new Point (width, height);
}

NSAttributedString createString() {
	NSAttributedString attribStr = createString(text, null, foreground, style, false, true, true);
	attribStr.autorelease();
	return attribStr;
}

@Override
void createHandle () {
	if ((style & SWT.PUSH) == 0) state |= THEME_BACKGROUND;
	NSButton widget = (NSButton)new SWTButton().alloc();
	widget.init();
	NSButtonCell cell = (NSButtonCell)new SWTButtonCell ().alloc ().init ();
	widget.setCell (cell);
	cell.release ();
	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0 && (style & SWT.FLAT) == 0) {
		NSView superview = parent.view;
		while (superview != null) {
			if (superview.isKindOfClass(OS.class_NSTableView)) {
				style |= SWT.FLAT;
				break;
			}
			superview = superview.superview();
		}
	}
	int type = OS.NSMomentaryLightButton;
	if ((style & SWT.PUSH) != 0) {
		if ((style & SWT.FLAT) != 0) {
			widget.setBezelStyle(OS.NSShadowlessSquareBezelStyle);
		} else {
			widget.setBezelStyle((style & SWT.WRAP) != 0 ? OS.NSRegularSquareBezelStyle : OS.NSRoundedBezelStyle);
		}
	} else if ((style & SWT.CHECK) != 0) {
		type = OS.NSSwitchButton;
	} else if ((style & SWT.RADIO) != 0) {
		type = OS.NSRadioButton;
		radioParent = (SWTView) new SWTView().alloc().init();
	} else if ((style & SWT.TOGGLE) != 0) {
		type = OS.NSPushOnPushOffButton;
		if ((style & SWT.FLAT) != 0) {
			widget.setBezelStyle(OS.NSShadowlessSquareBezelStyle);
		} else {
			widget.setBezelStyle((style & SWT.WRAP) != 0 ? OS.NSRegularSquareBezelStyle : OS.NSRoundedBezelStyle);
		}
	} else if ((style & SWT.ARROW) != 0) {
		widget.setBezelStyle(OS.NSShadowlessSquareBezelStyle);
	}
	widget.setButtonType(type);
	widget.setTitle(NSString.string());
	widget.setImagePosition(OS.NSImageLeft);
	widget.setTarget(widget);
	widget.setAction(OS.sel_sendSelection);
	view = widget;
	_setAlignment(style);
}

@Override
void createWidget() {
	text = "";
	super.createWidget ();
}

@Override
Font defaultFont () {
	return Font.cocoa_new (display, defaultNSFont ());
}

@Override
NSFont defaultNSFont() {
	NSCell cell = ((NSControl)view).cell();
	return NSFont.systemFontOfSize (NSFont.systemFontSizeForControlSize (cell.controlSize()));
}

@Override
void deregister () {
	super.deregister ();
	display.removeWidget(((NSControl)view).cell());
	if (radioParent != null) display.removeWidget(radioParent);
}

@Override
boolean dragDetect(int x, int y, boolean filter, boolean[] consume) {
	boolean dragging = super.dragDetect(x, y, filter, consume);
	consume[0] = dragging;
	return dragging;
}

private static NSRect smallerRect (NSRect cellFrame, double dx, double dy1, double dy2, float lineWidth) {
	if (lineWidth == 2) {
		dx -= 0.5;
		dy1 -= 0.5;
		dy2 -= 0.5;
	} else if (lineWidth == 0.5) {
		dx += 0.25;
		dy1 += 0.25;
		dy2 += 0.25;
	} else if (lineWidth == 0.75) {
		dx += 0.125;
		dy1 += 0.125;
		dy2 += 0.125;
	}
	NSRect result = new NSRect();
	result.x = cellFrame.x + dx;
	result.y = cellFrame.y + dy1;
	result.width = cellFrame.width - 2 * dx;
	result.height = cellFrame.height - dy1 - dy2;
	return result;
}

@Override
void drawBezelWithFrame_inView (long id, long sel, NSRect cellFrame, long viewid) {
	if (this.background != null) {
		NSButton button = (NSButton) view;

		final boolean isHighlighted = (style & SWT.TOGGLE) == 0 ? button.isHighlighted() : ((NSButton) view).state() == OS.NSOnState;

		final NSWindow window = button.window();
		final NSButtonCell defaultButtonCell = window == null ? null : window.defaultButtonCell();
		final boolean isDefault = defaultButtonCell != null && defaultButtonCell.id == id;

		double [] borderRGB = getLighterOrDarkerColor(this.background, 0.3, luma(background) >= 0.5);
		if (isHighlighted && (style & SWT.FLAT) != 0) {
			borderRGB = getLighterOrDarkerColor(borderRGB, 0.2, true);
		}

		NSGraphicsContext gc = NSGraphicsContext.currentContext();
		gc.saveGraphicsState();

		NSBezierPath path;
		final float lineWidth;
		if (isDefault) {
			lineWidth = 2f;
		} else if ((style & SWT.FLAT) == 0) {
			lineWidth = 0.75f;
		} else {
			lineWidth = 1f;
		}
		final long bezelStyle = button.bezelStyle();
		if (bezelStyle == OS.NSRoundedBezelStyle) {
			NSRect rect2 = smallerRect(cellFrame, 6.5f, 4.5f, 7.5f, lineWidth);
			path = NSBezierPath.bezierPathWithRoundedRect(rect2, 3, 3);
		} else if (bezelStyle == OS.NSRegularSquareBezelStyle) {
			NSRect rect2 = smallerRect(cellFrame, 2.5f, 2.5f, 3.5f, lineWidth);
			path = NSBezierPath.bezierPathWithRoundedRect(rect2, 3, 3);
		} else {
			NSRect rect2 = smallerRect(cellFrame, 0.5f, 0.5f, 0.5f, lineWidth);
			path = NSBezierPath.bezierPathWithRect(rect2);
		}
		if (!isHighlighted) {
			double [] backgroundRGB = this.background;
			NSColor backgroundNSColor = NSColor.colorWithDeviceRed(backgroundRGB[0], backgroundRGB[1], backgroundRGB[2], 1f);

			if((style & SWT.FLAT) == 0) {
				double [] topRGB = getLighterOrDarkerColor(this.background, 0.2, false);
				NSColor topColor = NSColor.colorWithDeviceRed(topRGB[0], topRGB[1], topRGB[2], 1f);
				double [] bottomRGB = getLighterOrDarkerColor(this.background, 0.1, true);
				NSColor bottomColor = NSColor.colorWithDeviceRed(bottomRGB[0], bottomRGB[1], bottomRGB[2], 1f);

				NSMutableArray ma = NSMutableArray.arrayWithCapacity(4);
				ma.addObject(topColor);
				ma.addObject(backgroundNSColor);
				ma.addObject(backgroundNSColor);
				ma.addObject(bottomColor);
				NSGradient gradient = ((NSGradient) new NSGradient().alloc()).initWithColors(ma);
				gradient.drawInBezierPath(path, 90);
				gradient.release();
			} else {
				backgroundNSColor.setFill();
				path.fill();
			}
		} else {
			double [] colorRGB0 = getLighterOrDarkerColor(this.background, 0.1f, true);
			NSColor color0 = NSColor.colorWithDeviceRed(colorRGB0[0], colorRGB0[1], colorRGB0[2], 1f);
			color0.setFill();
			path.fill();
		}

		// draw border
		path.setLineWidth(lineWidth);
		NSColor borderNSColor = NSColor.colorWithDeviceRed(borderRGB[0], borderRGB[1], borderRGB[2], 1f);
		borderNSColor.setStroke();
		path.stroke();

		gc.restoreGraphicsState();
		return;
	}
	super.drawBezelWithFrame_inView(id, sel, cellFrame, viewid);
}

@Override
void drawInteriorWithFrame_inView (long id, long sel, NSRect cellRect, long viewid) {
	if ((style & (SWT.CHECK|SWT.RADIO)) != 0 && backgroundImage != null) {
		fillBackground (new NSView(viewid), NSGraphicsContext.currentContext(), cellRect, -1);
	}
	super.drawInteriorWithFrame_inView(id, sel, cellRect, viewid);
	if (image != null && ((style & (SWT.CHECK|SWT.RADIO)) !=0)) {
		NSSize imageSize = image.handle.size();
		NSCell nsCell = new NSCell(id);
		double x = 0;
		double y = (imageSize.height - cellRect.height)/2f;
		NSRect imageRect = nsCell.imageRectForBounds(cellRect);
		NSSize stringSize = ((NSButton)view).attributedTitle().size();
		switch (style & (SWT.LEFT|SWT.RIGHT|SWT.CENTER)) {
			case SWT.LEFT:
				x = imageRect.x + imageRect.width + IMAGE_GAP;
				break;
			case SWT.CENTER:
				x = cellRect.x + imageRect.x + imageRect.width + ((cellRect.width-stringSize.width)/2f) - imageSize.width - IMAGE_GAP;
				break;
			case SWT.RIGHT:
				x = cellRect.x + cellRect.width - stringSize.width - imageSize.width - IMAGE_GAP;
				break;
		}
		NSRect destRect = new NSRect();
		destRect.x = x;
		destRect.y = y;
		destRect.width = imageSize.width;
		destRect.height = imageSize.height;
		NSGraphicsContext.static_saveGraphicsState();
		NSAffineTransform transform = NSAffineTransform.transform();
		transform.scaleXBy(1, -1);
		transform.translateXBy(0, -imageSize.height);
		transform.concat();
		image.handle.drawInRect(destRect, new NSRect(), OS.NSCompositeSourceOver, 1);
		NSGraphicsContext.static_restoreGraphicsState();
	}

}

@Override
NSRect drawTitleWithFrameInView (long id, long sel, long title, NSRect titleRect, long view) {
	boolean wrap = (style & SWT.WRAP) != 0 && text.length() != 0;
	boolean isEnabled = isEnabled();
	if (wrap) {
		NSSize wrapSize = new NSSize();
		wrapSize.width = titleRect.width;
		wrapSize.height = MAX_SIZE;
		final double[] foreground2;
		if (isEnabled) {
			foreground2 = foreground == null ? display.getNSColorRGB (NSColor.textColor()) : foreground;
		} else if (foreground == null) {
			foreground2 = DEFAULT_DISABLED_FOREGROUND;
		} else {
			foreground2 = getLighterOrDarkerColor(foreground, 0.5, luma(foreground) >= 0.5);
		}
		NSAttributedString attribStr = createString(text, null, foreground2, style, true, true, true);
		NSRect rect = attribStr.boundingRectWithSize(wrapSize, OS.NSStringDrawingUsesLineFragmentOrigin);
		switch (style & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) {
			case SWT.LEFT:
				rect.x = titleRect.x;
				break;
			case SWT.CENTER:
				rect.x = titleRect.x + (titleRect.width - rect.width) / 2f;
				break;
			case SWT.RIGHT:
				rect.x = titleRect.x + titleRect.width - rect.width;
				break;
		}
		rect.y = titleRect.y + (titleRect.height - rect.height) / 2;
		attribStr.drawInRect(rect);
		attribStr.release ();
		return rect;
	}
	if (!isEnabled && foreground != null) {
		NSAttributedString attribStr = createString(text, null, getLighterOrDarkerColor(foreground, 0.5, luma(foreground) >= 0.5), style, false, true, true);
		final NSRect result = super.drawTitleWithFrameInView(id, sel, attribStr.id, titleRect, view);
		attribStr.release();
		return result;
	}
	return super.drawTitleWithFrameInView(id, sel, title, titleRect, view);
}

@Override
boolean drawsBackground() {
	return background != null || backgroundImage != null;
}

@Override
void drawWidget (long id, NSGraphicsContext context, NSRect rect) {
	if ((style & SWT.ARROW) != 0) {
		NSRect frame = view.frame();
		int arrowSize = Math.min((int)frame.height, (int)frame.width) / 2;
		context.saveGraphicsState();
		NSPoint p1 = new NSPoint();
		p1.x = -arrowSize / 2;
		p1.y = -arrowSize / 2;
		NSPoint p2 = new NSPoint();
		p2.x = arrowSize / 2;
		p2.y = p1.y;
		NSPoint p3 = new NSPoint();
		p3.y = arrowSize / 2;

		NSBezierPath path = NSBezierPath.bezierPath();
		path.moveToPoint(p1);
		path.lineToPoint(p2);
		path.lineToPoint(p3);
		path.closePath();

		NSAffineTransform transform = NSAffineTransform.transform();
		if ((style & SWT.LEFT) != 0) {
			transform.rotateByDegrees(90);
		} else if ((style & SWT.UP) != 0) {
			transform.rotateByDegrees(180);
		} else if ((style & SWT.RIGHT) != 0) {
			transform.rotateByDegrees(-90);
		}
		path.transformUsingAffineTransform(transform);
		transform = NSAffineTransform.transform();
		transform.translateXBy(frame.width / 2, frame.height / 2);
		path.transformUsingAffineTransform(transform);

		NSColor color = isEnabled() ? NSColor.blackColor() : NSColor.disabledControlTextColor();
		color.set();
		path.fill();
		context.restoreGraphicsState();
	}
	super.drawWidget (id, context, rect);
}

@Override
NSRect focusRingMaskBoundsForFrame (long id, long sel, NSRect cellFrame, long view) {
	return cellFrame;
}

/**
 * Returns a value which describes the position of the
 * text or image in the receiver. The value will be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is an <code>ARROW</code> button, in
 * which case, the alignment will indicate the direction of
 * the arrow (one of <code>LEFT</code>, <code>RIGHT</code>,
 * <code>UP</code> or <code>DOWN</code>).
 *
 * @return the alignment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getAlignment () {
	checkWidget ();
	if ((style & SWT.ARROW) != 0) {
		if ((style & SWT.UP) != 0) return SWT.UP;
		if ((style & SWT.DOWN) != 0) return SWT.DOWN;
		if ((style & SWT.LEFT) != 0) return SWT.LEFT;
		if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
		return SWT.UP;
	}
	if ((style & SWT.LEFT) != 0) return SWT.LEFT;
	if ((style & SWT.CENTER) != 0) return SWT.CENTER;
	if ((style & SWT.RIGHT) != 0) return SWT.RIGHT;
	return SWT.LEFT;
}

/**
 * Returns <code>true</code> if the receiver is grayed,
 * and false otherwise. When the widget does not have
 * the <code>CHECK</code> style, return false.
 *
 * @return the grayed state of the checkbox
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public boolean getGrayed() {
	checkWidget ();
	if ((style & SWT.CHECK) == 0) return false;
	return grayed;
}

/**
 * Returns the receiver's image if it has one, or null
 * if it does not.
 *
 * @return the receiver's image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getImage () {
	checkWidget();
	return image;
}

@Override
String getNameText () {
	return getText ();
}

/**
 * Returns <code>true</code> if the receiver is selected,
 * and false otherwise.
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed in. If the receiver is of any other type,
 * this method returns false.
 *
 * @return the selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public boolean getSelection () {
	checkWidget ();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return false;
	if ((style & SWT.CHECK) != 0 && grayed) return ((NSButton)view).state() == OS.NSMixedState;
	return ((NSButton)view).state() == OS.NSOnState;
}

/**
 * Returns the receiver's text, which will be an empty
 * string if it has never been set or if the receiver is
 * an <code>ARROW</code> button.
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
	return text;
}

@Override
boolean isDescribedByLabel () {
	return false;
}

/*
 * Feature in Cocoa.  If a checkbox is in multi-state mode, nextState cycles from off to mixed to on and back to off again.
 * This will cause the on state to momentarily appear while clicking on the checkbox. To avoid this, we override [NSCell nextState]
 * to go directly to the desired state if we have a grayed checkbox.
 */
@Override
long nextState(long id, long sel) {
	if ((style & SWT.CHECK) != 0 && grayed) {
		return ((NSButton)view).state() == OS.NSMixedState ? OS.NSOffState : OS.NSMixedState;
	}

	return super.nextState(id, sel);
}

@Override
void register() {
	super.register();
	display.addWidget(((NSControl)view).cell(), this);
	if (radioParent != null) display.addWidget(radioParent, this);
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	image = null;
	text = null;
}

/**
 * Removes the listener from the collection of listeners who will
 * be notified when the control is selected by the user.
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
public void removeSelectionListener(SelectionListener listener) {
	checkWidget();
	if (listener == null) error (SWT.ERROR_NULL_ARGUMENT);
	if (eventTable == null) return;
	eventTable.unhook(SWT.Selection, listener);
	eventTable.unhook(SWT.DefaultSelection,listener);
}

void selectRadio () {
	/*
	* This code is intentionally commented.  When two groups
	* of radio buttons with the same parent are separated by
	* another control, the correct behavior should be that
	* the two groups act independently.  This is consistent
	* with radio tool and menu items.  The commented code
	* implements this behavior.
	*/
//	int index = 0;
//	Control [] children = parent._getChildren ();
//	while (index < children.length && children [index] != this) index++;
//	int i = index - 1;
//	while (i >= 0 && children [i].setRadioSelection (false)) --i;
//	int j = index + 1;
//	while (j < children.length && children [j].setRadioSelection (false)) j++;
//	setSelection (true);
	Control [] children = parent._getChildren ();
	for (int i=0; i<children.length; i++) {
		Control child = children [i];
		if (this != child) child.setRadioSelection (false);
	}
	setSelection (true);
}

@Override
void sendSelection () {
	if ((style & SWT.RADIO) != 0) {
		if ((parent.getStyle () & SWT.NO_RADIO_GROUP) == 0) {
			selectRadio ();
		}
	}
	if ((style & SWT.CHECK) != 0) {
		if (grayed && ((NSButton)view).state() == OS.NSOnState) {
			((NSButton)view).setState(OS.NSOffState);
		}
		if (!grayed && ((NSButton)view).state() == OS.NSMixedState) {
			((NSButton)view).setState(OS.NSOnState);
		}
	}
	sendSelectionEvent (SWT.Selection);
}


/**
 * Controls how text, images and arrows will be displayed
 * in the receiver. The argument should be one of
 * <code>LEFT</code>, <code>RIGHT</code> or <code>CENTER</code>
 * unless the receiver is an <code>ARROW</code> button, in
 * which case, the argument indicates the direction of
 * the arrow (one of <code>LEFT</code>, <code>RIGHT</code>,
 * <code>UP</code> or <code>DOWN</code>).
 *
 * @param alignment the new alignment
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setAlignment (int alignment) {
	checkWidget ();
	_setAlignment (alignment);
	redraw ();
}

void _setAlignment (int alignment) {
	if ((style & SWT.ARROW) != 0) {
		if ((style & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT)) == 0) return;
		style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		style |= alignment & (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.RIGHT);
		return;
	}
	if ((alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER)) == 0) return;
	style &= ~(SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	style |= alignment & (SWT.LEFT | SWT.RIGHT | SWT.CENTER);
	/* text is still null when this is called from createHandle() */
	if (text != null) {
		((NSButton)view).setAttributedTitle(createString());
	}
}

@Override
void setBackgroundColor(NSColor nsColor) {
	Control control = findBackgroundControl();
	if (control == null || control.backgroundImage == null) {
		NSButtonCell cell = new NSButtonCell(((NSButton)view).cell());
		cell.setBackgroundColor(nsColor);
	}
}

@Override
void setBackgroundImage(NSImage image) {
	if (image != null) {
		NSButtonCell cell = new NSButtonCell(((NSButton)view).cell());
		cell.setBackgroundColor(null);
	}
}

@Override
void setBounds (int x, int y, int width, int height, boolean move, boolean resize) {
	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0 && (style & (SWT.FLAT | SWT.WRAP)) == 0) {
		int heightThreshold = REGULAR_BUTTON_HEIGHT;

		NSCell cell = ((NSControl)view).cell();
		if (cell != null && cell.controlSize() == OS.NSSmallControlSize) {
			heightThreshold = SMALL_BUTTON_HEIGHT;
		}

		NSButton button = (NSButton)view;
		if (height > heightThreshold) {
			button.setBezelStyle(OS.NSRegularSquareBezelStyle);
		} else {
			button.setBezelStyle(OS.NSRoundedBezelStyle);
		}
	}
	super.setBounds(x, y, width, height, move, resize);
	if (radioParent != null && resize) {
		NSSize size = new NSSize();
		size.width = width;
		size.height = height;
		view.setFrameSize(size);
	}
}

@Override
void setFont (NSFont font) {
	if (text != null) {
		((NSButton)view).setAttributedTitle(createString());
	}
}

@Override
void setForeground (double [] color) {
	((NSButton)view).setAttributedTitle(createString());
}

/**
 * Sets the grayed state of the receiver.  This state change
 * only applies if the control was created with the SWT.CHECK
 * style.
 *
 * @param grayed the new grayed state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setGrayed(boolean grayed) {
	checkWidget ();
	if ((style & SWT.CHECK) == 0) return;
	boolean checked = getSelection ();
	this.grayed = grayed;
	((NSButton) view).setAllowsMixedState(grayed);

	if (checked) {
		if (grayed) {
			((NSButton) view).setState (OS.NSMixedState);
		} else {
			((NSButton) view).setState (OS.NSOnState);
		}
	}
}

/**
 * Sets the receiver's image to the argument, which may be
 * <code>null</code> indicating that no image should be displayed.
 * <p>
 * Note that a Button can display an image and text simultaneously.
 * </p>
 * @param image the image to display on the receiver (may be <code>null</code>)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setImage (Image image) {
	checkWidget();
	if (image != null && image.isDisposed ()) {
		error (SWT.ERROR_INVALID_ARGUMENT);
	}
	if ((style & SWT.ARROW) != 0) return;
	this.image = image;
	if ((style & (SWT.RADIO|SWT.CHECK)) == 0) {
		/*
		 * Feature in Cocoa.  If the NSImage object being set into the button is
		 * the same NSImage object that is already there then the button does not
		 * redraw itself.  This results in the button's image not visually updating
		 * if the NSImage object's content has changed since it was last set
		 * into the button.  The workaround is to explicitly redraw the button.
		 */
		((NSButton)view).setImage(image != null ? image.handle : null);
		view.setNeedsDisplay(true);
	} else {
		((NSButton)view).setAttributedTitle(createString());
	}

	if (image != null) {
		if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0 && (style & (SWT.FLAT | SWT.WRAP)) == 0) {
			NSCell cell = ((NSButton)view).cell();
			NSSize size = cell.cellSize ();
			int height = (int)Math.ceil(size.height);
			if (height > SMALL_BUTTON_HEIGHT) {
				cell.setControlSize(OS.NSRegularControlSize);
			} else if (display.smallFonts){
				cell.setControlSize(OS.NSSmallControlSize);
			}
			setFont(getFont());
		}
	}

	updateAlignment ();
}

@Override
boolean setRadioSelection (boolean value){
	if ((style & SWT.RADIO) == 0) return false;
	if (getSelection() != value) {
		setSelection (value);
		sendSelectionEvent (SWT.Selection);
	}
	return true;
}

/**
 * Sets the selection state of the receiver, if it is of type <code>CHECK</code>,
 * <code>RADIO</code>, or <code>TOGGLE</code>.
 *
 * <p>
 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>,
 * it is selected when it is checked. When it is of type <code>TOGGLE</code>,
 * it is selected when it is pushed in.
 *
 * @param selected the new selection state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setSelection (boolean selected) {
	checkWidget();
	if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) return;
	if (grayed) {
		((NSButton)view).setState (selected ? OS.NSMixedState : OS.NSOffState);
	} else {
		((NSButton)view).setState (selected ? OS.NSOnState : OS.NSOffState);
	}
}

/**
 * Sets the receiver's text.
 * <p>
 * This method sets the button label.  The label may include
 * the mnemonic character but must not contain line delimiters.
 * </p>
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, a selection
 * event occurs. On most platforms, the mnemonic appears
 * underlined but may be emphasized in a platform specific
 * manner.  The mnemonic indicator character '&amp;' can be
 * escaped by doubling it in the string, causing a single
 * '&amp;' to be displayed.
 * </p><p>
 * Note that a Button can display an image and text simultaneously
 * on Windows (starting with XP), GTK+ and OSX.  On other platforms,
 * a Button that has an image and text set into it will display the
 * image or text that was set most recently.
 * </p><p>
 * Also note, if control characters like '\n', '\t' etc. are used
 * in the string, then the behavior is platform dependent.
 * </p>
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
	checkWidget();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	if ((style & SWT.ARROW) != 0) return;
	text = string;
	((NSButton)view).setAttributedTitle(createString());
	updateAlignment ();
}

@Override
NSRect titleRectForBounds (long id, long sel, NSRect cellFrame) {
	NSRect rect = super.titleRectForBounds(id, sel, cellFrame);
	if (image != null && ((style & (SWT.CHECK|SWT.RADIO)) !=0)) {
		NSSize imageSize = image.handle.size();
		rect.x += imageSize.width + IMAGE_GAP;
		rect.width -= (imageSize.width + IMAGE_GAP);
		rect.width = Math.max(0f, rect.width);
	}
	return rect;
}

@Override
int traversalCode (int key, NSEvent theEvent) {
	int code = super.traversalCode (key, theEvent);
	if ((style & SWT.ARROW) != 0) code &= ~(SWT.TRAVERSE_TAB_NEXT | SWT.TRAVERSE_TAB_PREVIOUS);
	if ((style & SWT.RADIO) != 0) code |= SWT.TRAVERSE_ARROW_NEXT | SWT.TRAVERSE_ARROW_PREVIOUS;
	return code;
}

void updateAlignment () {
	NSButton widget = (NSButton)view;
	if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
		if (text.length() != 0 && image != null) {
			widget.setImagePosition(OS.NSImageLeft);
		} else {
			widget.setImagePosition(text.length() != 0 ? OS.NSNoImage : OS.NSImageOnly);
		}
	}
}

@Override
NSView topView() {
	if (radioParent != null) return radioParent;
	return super.topView();
}

@Override
void setZOrder() {
	super.setZOrder();
	if (radioParent != null) radioParent.addSubview(view);
}
}
