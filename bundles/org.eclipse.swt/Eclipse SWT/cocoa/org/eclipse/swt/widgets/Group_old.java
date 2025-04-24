/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class provide an etched border
 * with an optional title.
 * <p>
 * Shadow styles are hints and may not be honoured
 * by the platform.  To create a group with the
 * default shadow style for the platform, do not
 * specify a shadow style.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SHADOW_ETCHED_IN, SHADOW_ETCHED_OUT, SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the above styles may be specified.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Group_old extends Composite {
	NSView contentView;
	String text = "";
	boolean ignoreResize;
	int hMargin, vMargin;

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
 * @see SWT#SHADOW_ETCHED_IN
 * @see SWT#SHADOW_ETCHED_OUT
 * @see SWT#SHADOW_IN
 * @see SWT#SHADOW_OUT
 * @see SWT#SHADOW_NONE
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
public Group_old (Composite parent, int style) {
	super (parent, checkStyle (style));
}

static int checkStyle (int style) {
	style |= SWT.NO_FOCUS;
	/*
	* Even though it is legal to create this widget
	* with scroll bars, they serve no useful purpose
	* because they do not automatically scroll the
	* widget's client area.  The fix is to clear
	* the SWT style.
	*/
	return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
}

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

@Override
public Rectangle computeTrim (int x, int y, int width, int height) {
	checkWidget ();
	NSBox widget = (NSBox)view;
	NSRect newRect = new NSRect();
	newRect.x = x;
	newRect.y = y;
	newRect.width = width;
	newRect.height = height;
	NSRect oldRect = widget.frame();
	ignoreResize = true;
	widget.setFrameFromContentFrame(newRect);
	newRect = widget.frame();
	widget.setFrame(oldRect);
	ignoreResize = false;
	x = (int) Math.ceil(newRect.x) - hMargin;
	y = (int) Math.ceil(newRect.y) - vMargin;
	width = (int) Math.ceil(newRect.width) + (hMargin * 2);
	height = (int) Math.ceil(newRect.height) + (vMargin * 2);
	return super.computeTrim(x, y, width, height);
}

@Override
NSView contentView () {
	return contentView;
}

@Override
void createHandle () {
	state |= THEME_BACKGROUND;
	NSBox widget = (NSBox)new SWTBox().alloc();
	widget.init();
	widget.setTitlePosition(OS.NSNoTitle);
	NSSize margins = widget.contentViewMargins();
	hMargin = (int) margins.width;
	vMargin = (int) margins.height;
	widget.setContentViewMargins(new NSSize());

	NSView contentWidget = (NSView)new SWTView().alloc();
	contentWidget.init();
//	contentWidget.setDrawsBackground(false);
	widget.setContentView(contentWidget);
	contentView = contentWidget;
	view = widget;
}

@Override
NSFont defaultNSFont () {
	return display.boxFont;
}

@Override
void deregister () {
	super.deregister ();
	display.removeWidget (contentView);
	SWTBox box = (SWTBox)view;
	display.removeWidget (box.titleCell());
}

@Override
void drawBackground (long id, NSGraphicsContext context, NSRect rect) {
	if (id != view.id) return;
	fillBackground (view, context, rect, -1);
}

@Override
NSView eventView () {
	return contentView;
}

@Override
public Rectangle getClientArea () {
	checkWidget();
	NSRect rect = contentView.bounds();
	int width = Math.max (0, (int) rect.width - hMargin * 2);
	int height = Math.max (0, (int) rect.height - vMargin * 2);
	return new Rectangle((int)(rect.x) + hMargin, (int)(rect.y) + vMargin, width, height);
}

@Override
String getNameText () {
	return getText ();
}

/**
 * Returns the receiver's text, which is the string that the
 * is used as the <em>title</em>. If the text has not previously
 * been set, returns an empty string.
 *
 * @return the text
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
boolean isTransparent() {
	return true;
}

@Override
float getThemeAlpha () {
	return (background != null ? 1 : 0.25f) * parent.getThemeAlpha ();
}

@Override
void register () {
	super.register ();
	display.addWidget (contentView, this);
	SWTBox box = (SWTBox)view;
	display.addWidget (box.titleCell(), this);
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	if (contentView != null) contentView.release();
	contentView = null;
}

@Override
void resized() {
	if (!ignoreResize) super.resized();
}

@Override
void setFont(NSFont font) {
	((NSBox) view).setTitleFont(font);
}

@Override
void setForeground (double [] color) {
	NSColor nsColor;
	if (color == null) {
		nsColor = NSColor.textColor ();
	} else {
		nsColor = NSColor.colorWithDeviceRed (color[0], color[1], color[2], 1);
	}
	NSTextFieldCell cell = new NSTextFieldCell (((NSBox)view).titleCell ().id);
	cell.setTextColor (nsColor);
}

@Override
void setOrientation () {
	int direction = (style & SWT.RIGHT_TO_LEFT) != 0 ? OS.NSWritingDirectionRightToLeft : OS.NSWritingDirectionLeftToRight;
	NSTextFieldCell cell = new NSTextFieldCell (((NSBox)view).titleCell ().id);
	cell.setBaseWritingDirection(direction);
}

/**
 * Sets the receiver's text, which is the string that will
 * be displayed as the receiver's <em>title</em>, to the argument,
 * which may not be null. The string may include the mnemonic character.
 * <p>
 * Mnemonics are indicated by an '&amp;' that causes the next
 * character to be the mnemonic.  When the user presses a
 * key sequence that matches the mnemonic, focus is assigned
 * to the first child of the group. On most platforms, the
 * mnemonic appears underlined but may be emphasised in a
 * platform specific manner.  The mnemonic indicator character
 * '&amp;' can be escaped by doubling it in the string, causing
 * a single '&amp;' to be displayed.
 * </p><p>
 * Note: If control characters like '\n', '\t' etc. are used
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
	text = string;
	char [] buffer = new char [text.length ()];
	text.getChars (0, buffer.length, buffer, 0);
	int length = fixMnemonic (buffer);
	NSBox box = (NSBox)view;
	box.setTitlePosition(length == 0 ? OS.NSNoTitle : OS.NSAtTop);
	box.setTitle(NSString.stringWithCharacters(buffer, length));
}

}
