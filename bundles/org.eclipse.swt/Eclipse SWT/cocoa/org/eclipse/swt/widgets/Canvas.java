/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.cocoa.*;
import org.eclipse.swt.internal.graphics.*;

/**
 * Instances of this class provide a surface for drawing
 * arbitrary graphics.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * This class may be subclassed by custom control implementors
 * who are building controls that are <em>not</em> constructed
 * from aggregates of other controls. That is, they are either
 * painted using SWT graphics calls or are handled by native
 * methods.
 * </p>
 *
 * @see Composite
 * @see <a href="http://www.eclipse.org/swt/snippets/#canvas">Canvas snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public class Canvas extends Composite {
	Caret caret;
	IME ime;
	NSOpenGLContext glcontext;
	NSBezierPath visiblePath;

	static NSMutableArray supportedPboardTypes;

	static {
		// This array is leaked.
		supportedPboardTypes = NSMutableArray.arrayWithCapacity(1);
		supportedPboardTypes.retain();
		supportedPboardTypes.addObject(OS.NSPasteboardTypeString);
		//supportedPboardTypes.addObject(OS.NSRTFPboardType);
	};

Canvas () {
	/* Do nothing */
}

@Override
long attributedSubstringFromRange (long id, long sel, long range) {
	if (ime != null) return ime.attributedSubstringFromRange (id, sel, range);
	return super.attributedSubstringFromRange(id, sel, range);
}

@Override
void sendFocusEvent(int type) {
	if (caret != null) {
		if (type == SWT.FocusIn) {
			caret.setFocus();
		} else {
			caret.killFocus();
		}
	}
	super.sendFocusEvent(type);
}


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
 * </ul>
 *
 * @see SWT
 * @see Widget#getStyle
 */
public Canvas (Composite parent, int style) {
	super (parent, style);
}

@Override
long characterIndexForPoint (long id, long sel, long point) {
	if (ime != null) return ime.characterIndexForPoint (id, sel, point);
	return super.characterIndexForPoint (id, sel, point);
}

/**
 * Fills the interior of the rectangle specified by the arguments,
 * with the receiver's background.
 *
 * @param gc the gc where the rectangle is to be filled
 * @param x the x coordinate of the rectangle to be filled
 * @param y the y coordinate of the rectangle to be filled
 * @param width the width of the rectangle to be filled
 * @param height the height of the rectangle to be filled
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the gc is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the gc has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.2
 */
public void drawBackground (GC gc, int x, int y, int width, int height) {
	drawBackground(gc, x, y, width, height, 0, 0);
}

@Override
void drawBackground (long id, NSGraphicsContext context, NSRect rect) {
	super.drawBackground(id, context, rect);
	if (glcontext != null) {
		if (isObscured()) {
			glcontext.setValues(new int[]{-1}, OS.NSOpenGLCPSurfaceOrder);
			context.saveGraphicsState();
			context.setCompositingOperation(OS.NSCompositeClear);
			if (visiblePath == null) {
				long visibleRegion = getVisibleRegion();
				visiblePath = getPath(visibleRegion);
				OS.DisposeRgn(visibleRegion);
			}
			visiblePath.addClip();
			NSBezierPath.fillRect(rect);
			context.restoreGraphicsState();
		} else {
			glcontext.setValues(new int[]{1}, OS.NSOpenGLCPSurfaceOrder);
		}
	}
}

@Override
void drawRect (long id, long sel, NSRect rect) {
	if (glcontext != null && glcontext.view() == null) glcontext.setView(view);
	super.drawRect(id, sel, rect);
}

@Override
void drawWidget (long id, NSGraphicsContext context, NSRect rect) {
	if (id != view.id) return;
	super.drawWidget (id, context, rect);
	if (caret == null) return;
	if (caret.isShowing) {
		long ctx = context.graphicsPort();
		OS.CGContextSaveGState (ctx);
		OS.CGContextSetBlendMode (ctx, OS.kCGBlendModeDifference);
		Image image = caret.image;
		if (image != null) {
			NSImage imageHandle = image.handle;
			NSSize size = imageHandle.size();
			NSImageRep imageRep = ImageUtil.createImageRep(image, size);
			if (!imageRep.isKindOfClass(OS.class_NSBitmapImageRep)) return;
			NSBitmapImageRep rep = new NSBitmapImageRep(imageRep);
			CGRect destRect = new CGRect ();
			destRect.origin.x = caret.x;
			destRect.origin.y = caret.y;
			destRect.size.width = size.width;
			destRect.size.height = size.height;
			long data = rep.bitmapData();
			long format = rep.bitmapFormat();
			long bpr = rep.bytesPerRow();
			int alphaInfo;
			if (rep.hasAlpha()) {
				alphaInfo = (format & OS.NSAlphaFirstBitmapFormat) != 0 ? OS.kCGImageAlphaFirst : OS.kCGImageAlphaLast;
			} else {
				alphaInfo = (format & OS.NSAlphaFirstBitmapFormat) != 0 ? OS.kCGImageAlphaNoneSkipFirst : OS.kCGImageAlphaNoneSkipLast;
			}
			long provider = OS.CGDataProviderCreateWithData(0, data, bpr * (int)size.height, 0);
			long colorspace = OS.CGColorSpaceCreateDeviceRGB();
			long cgImage = OS.CGImageCreate((int)size.width, (int)size.height, rep.bitsPerSample(), rep.bitsPerPixel(), bpr, colorspace, alphaInfo, provider, 0, true, 0);
			OS.CGColorSpaceRelease(colorspace);
			OS.CGDataProviderRelease(provider);
			OS.CGContextScaleCTM (ctx, 1, -1);
			OS.CGContextTranslateCTM (ctx, 0, -(size.height + 2 * destRect.origin.y));
			OS.CGContextSetBlendMode (ctx, OS.kCGBlendModeDifference);
			OS.CGContextDrawImage (ctx, destRect, cgImage);
			OS.CGImageRelease(cgImage);
		} else {
			CGRect drawRect = new CGRect();
			drawRect.origin.x = caret.x;
			drawRect.origin.y = caret.y;
			drawRect.size.width = caret.width != 0 ? caret.width : Caret.DEFAULT_WIDTH;
			drawRect.size.height = caret.height;
			long colorspace = OS.CGColorSpaceCreateDeviceRGB();
			OS.CGContextSetFillColorSpace(ctx, colorspace);
			OS.CGColorSpaceRelease(colorspace);
			OS.CGContextSetFillColor(ctx, new double [] {1, 1, 1, 1});
			OS.CGContextFillRect(ctx, drawRect);
		}
		OS.CGContextRestoreGState(ctx);
	}
}

@Override
NSRect firstRectForCharacterRange (long id, long sel, long range) {
	if (ime != null) return ime.firstRectForCharacterRange (id, sel, range);
	return super.firstRectForCharacterRange (id, sel, range);
}

/**
 * Returns the caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 *
 * @return the caret for the receiver, may be null
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Caret getCaret () {
	checkWidget();
	return caret;
}

/**
 * Returns the IME.
 *
 * @return the IME
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public IME getIME () {
	checkWidget();
	return ime;
}

@Override
boolean hasMarkedText (long id, long sel) {
	if (ime != null) return ime.hasMarkedText (id, sel);
	return super.hasMarkedText (id, sel);
}

@Override
boolean imeInComposition () {
	return ime != null && ime.isInlineEnabled () && ime.startOffset != -1;
}

@Override
boolean insertText (long id, long sel, long string) {
	if (ime != null) {
		if (!ime.insertText (id, sel, string)) return false;
	}
	return super.insertText (id, sel, string);
}

@Override
boolean isOpaque (long id, long sel) {
	if (glcontext != null) return true;
	return super.isOpaque(id, sel);
}

@Override
NSRange markedRange (long id, long sel) {
	if (ime != null) return ime.markedRange (id, sel);
	return super.markedRange (id, sel);
}

@Override
boolean readSelectionFromPasteboard(long id, long sel, long pasteboard) {
	boolean result = false;
	NSPasteboard pboard = new NSPasteboard(pasteboard);
	NSArray availableTypes = pboard.types();
	NSString type;

	for (long i = 0; i < supportedPboardTypes.count(); i++) {
		if (result) break;
		type = new NSString(supportedPboardTypes.objectAtIndex(i));
		if (availableTypes.containsObject(type)) {
			result = readSelectionFromPasteboard(pboard, type);
		}
	}
	return result;
}

boolean readSelectionFromPasteboard(NSPasteboard pboard, NSString type) {
	boolean result = false;
	NSString newSelection = null;
	if (type.isEqualToString(OS.NSPasteboardTypeString)) {
		NSString string = pboard.stringForType(OS.NSPasteboardTypeString);
		if (string != null && string.length() > 0) {
			newSelection = string;
		}
	}

	if (newSelection != null) {
		Accessible acc = getAccessible();
		acc.internal_accessibilitySetValue_forAttribute(newSelection, OS.NSAccessibilitySelectedTextAttribute, ACC.CHILDID_SELF);
		result = true;
	}

	return result;
}

@Override
void releaseChildren (boolean destroy) {
	if (caret != null) {
		caret.release (false);
		caret = null;
	}
	if (ime != null) {
		ime.release (false);
		ime = null;
	}
	super.releaseChildren (destroy);
}

@Override
void reskinChildren (int flags) {
	if (caret != null) caret.reskin (flags);
	if (ime != null)  ime.reskin (flags);
	super.reskinChildren (flags);
}

@Override
void releaseWidget () {
	super.releaseWidget();
	if (visiblePath != null) visiblePath.release();
	visiblePath = null;
}

@Override
void resetVisibleRegion () {
	super.resetVisibleRegion ();
	if (visiblePath != null) visiblePath.release();
	visiblePath = null;
}

/**
 * Scrolls a rectangular area of the receiver by first copying
 * the source area to the destination and then causing the area
 * of the source which is not covered by the destination to
 * be repainted. Children that intersect the rectangle are
 * optionally moved during the operation. In addition, all outstanding
 * paint events are flushed before the source area is copied to
 * ensure that the contents of the canvas are drawn correctly.
 *
 * @param destX the x coordinate of the destination
 * @param destY the y coordinate of the destination
 * @param x the x coordinate of the source
 * @param y the y coordinate of the source
 * @param width the width of the area
 * @param height the height of the area
 * @param all <code>true</code>if children should be scrolled, and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void scroll (int destX, int destY, int x, int y, int width, int height, boolean all) {
	checkWidget();
	if (width <= 0 || height <= 0) return;
	int deltaX = destX - x, deltaY = destY - y;
	if (deltaX == 0 && deltaY == 0) return;
	if (!isDrawing ()) return;
	NSRect visibleRect = view.visibleRect();
	if (visibleRect.width <= 0 || visibleRect.height <= 0) return;
	boolean isFocus = caret != null && caret.isFocusCaret ();
	if (isFocus) caret.killFocus ();
	Rectangle clientRect = getClientArea ();
	Rectangle sourceRect = new Rectangle (x, y, width, height);
	Control control = findBackgroundControl ();
	boolean redraw = control != null && control.backgroundImage != null;
	if (!redraw) redraw = hasRegion ();
	if (!redraw) redraw = isObscured ();
	if (!redraw && sourceRect.intersects (clientRect)) {
		getShell().setScrolling();
		redraw = !update(all);
	}
	if (redraw) {
		redrawWidget (view, x, y, width, height, false);
		redrawWidget (view, destX, destY, width, height, false);
	} else {
		NSRect damage = new NSRect();
		damage.x = x;
		damage.y = y;
		damage.width = width;
		damage.height = height;
		NSPoint dest = new NSPoint();
		dest.x = destX;
		dest.y = destY;

		view.lockFocus();
		NSSize delta = new NSSize();
		delta.width = deltaX;
		delta.height = deltaY;
		view.scrollRect(damage, delta);
		view.unlockFocus();

		boolean disjoint = (destX + width < x) || (x + width < destX) || (destY + height < y) || (y + height < destY);
		if (disjoint) {
			view.setNeedsDisplayInRect(damage);
		} else {
			if (deltaX != 0) {
				int newX = destX - deltaX;
				if (deltaX < 0) newX = destX + width;
				damage.x = newX;
				damage.width = Math.abs(deltaX);
				view.setNeedsDisplayInRect(damage);
			}
			if (deltaY != 0) {
				int newY = destY - deltaY;
				if (deltaY < 0) newY = destY + height;
				damage.x = x;
				damage.y = newY;
				damage.width = width;
				damage.height =  Math.abs (deltaY);
				view.setNeedsDisplayInRect(damage);
			}
		}

		NSRect srcRect = new NSRect();
		srcRect.x = sourceRect.x;
		srcRect.y = sourceRect.y;
		srcRect.width = sourceRect.width;
		srcRect.height = sourceRect.height;
		OS.NSIntersectionRect(visibleRect, visibleRect, srcRect);

		if (!OS.NSEqualRects(visibleRect, srcRect)) {
			if (srcRect.x != visibleRect.x) {
				damage.x = srcRect.x + deltaX;
				damage.y = srcRect.y + deltaY;
				damage.width = visibleRect.x - srcRect.x;
				damage.height = srcRect.height;
				view.setNeedsDisplayInRect(damage);
			}
			if (visibleRect.x + visibleRect.width != srcRect.x + srcRect.width) {
				damage.x = srcRect.x + visibleRect.width + deltaX;
				damage.y = srcRect.y + deltaY;
				damage.width = srcRect.width - visibleRect.width;
				damage.height = srcRect.height;
				view.setNeedsDisplayInRect(damage);
			}
			if (visibleRect.y != srcRect.y) {
				damage.x = visibleRect.x + deltaX;
				damage.y = srcRect.y + deltaY;
				damage.width = visibleRect.width;
				damage.height = visibleRect.y - srcRect.y;
				view.setNeedsDisplayInRect(damage);
			}
			if (visibleRect.y + visibleRect.height != srcRect.y + srcRect.height) {
				damage.x = visibleRect.x + deltaX;
				damage.y = visibleRect.y + visibleRect.height + deltaY;
				damage.width = visibleRect.width;
				damage.height = srcRect.y + srcRect.height - (visibleRect.y + visibleRect.height);
				view.setNeedsDisplayInRect(damage);
			}
		}
	}

	if (all) {
		Control [] children = _getChildren ();
		for (int i=0; i<children.length; i++) {
			Control child = children [i];
			Rectangle rect = child.getBounds ();
			if (Math.min(x + width, rect.x + rect.width) >= Math.max (x, rect.x) &&
				Math.min(y + height, rect.y + rect.height) >= Math.max (y, rect.y)) {
					child.setLocation (rect.x + deltaX, rect.y + deltaY);
			}
		}
	}
	if (isFocus) caret.setFocus ();
}

@Override
NSRange selectedRange (long id, long sel) {
	if (ime != null) return ime.selectedRange (id, sel);
	return super.selectedRange (id, sel);
}

@Override
boolean sendKeyEvent (NSEvent nsEvent, int type) {
	if (caret != null) NSCursor.setHiddenUntilMouseMoves (true);
	return super.sendKeyEvent (nsEvent, type);
}

/**
 * Sets the receiver's caret.
 * <p>
 * The caret for the control is automatically hidden
 * and shown when the control is painted or resized,
 * when focus is gained or lost and when an the control
 * is scrolled.  To avoid drawing on top of the caret,
 * the programmer must hide and show the caret when
 * drawing in the window any other time.
 * </p>
 * @param caret the new caret for the receiver, may be null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the caret has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setCaret (Caret caret) {
	checkWidget();
	Caret newCaret = caret;
	Caret oldCaret = this.caret;
	this.caret = newCaret;
	if (hasFocus ()) {
		if (oldCaret != null) oldCaret.killFocus ();
		if (newCaret != null) {
			if (newCaret.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
			newCaret.setFocus ();
		}
	}
}

@Override
public void setFont (Font font) {
	checkWidget ();
	if (caret != null) caret.setFont (font);
	super.setFont (font);
}

@Override
void setOpenGLContext(Object value) {
	glcontext = (NSOpenGLContext)value;
	Shell shell = getShell ();
	if (glcontext != null) {
		shell.glContextCount++;
	} else {
		shell.glContextCount--;
	}
	shell.updateOpaque ();
}

/**
 * Sets the receiver's IME.
 *
 * @param ime the new IME for the receiver, may be null
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the IME has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 *
 * @since 3.4
 */
public void setIME (IME ime) {
	checkWidget ();
	if (ime != null && ime.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
	this.ime = ime;
}

@Override
boolean setMarkedText_selectedRange (long id, long sel, long string, long range) {
	if (ime != null) {
		if (!ime.setMarkedText_selectedRange (id, sel, string, range)) return false;
	}
	return super.setMarkedText_selectedRange (id, sel, string, range);
}

@Override
long validAttributesForMarkedText (long id, long sel) {
	if (ime != null) return ime.validAttributesForMarkedText (id, sel);
	return super.validAttributesForMarkedText(id, sel);
}

@Override
long validRequestorForSendType(long id, long sel, long sendType, long returnType) {
	if (id == view.id) {
		Accessible acc = getAccessible();
		if (acc != null) {
			// This returns null if there are no additional overrides. Since this is only checked to see if there is
			// a StyledText or other control that supports reading and writing of the selection there's no need to bother
			// with checking the default values. They will be picked up in the default implementation.
			NSArray attributes = acc.internal_accessibilityAttributeNames(ACC.CHILDID_SELF);
			if (attributes != null) {
				boolean canReturn = attributes.containsObject(OS.NSAccessibilitySelectedTextAttribute);
				boolean canSend = acc.internal_accessibilityIsAttributeSettable(OS.NSAccessibilitySelectedTextAttribute, ACC.CHILDID_SELF);
				boolean canHandlePBoardType = supportedPboardTypes.containsObject(new id(sendType)) && supportedPboardTypes.containsObject(new id(returnType));
				if (canReturn && canSend && canHandlePBoardType) {
					id selection = acc.internal_accessibilityAttributeValue(OS.NSAccessibilitySelectedTextAttribute, ACC.CHILDID_SELF);
					if (selection != null) {
						NSString selectionString = new NSString(selection);
						if (selectionString.length() > 0) return view.id;
					}
				}
			}
		}
	}

	return super.validRequestorForSendType(id, sel, sendType, returnType);
}

@Override
void updateOpenGLContext(long id, long sel, long notification) {
	if (glcontext != null) ((NSOpenGLContext)glcontext).update();
}

@Override
void viewWillMoveToWindow(long id, long sel, long arg0) {
	super.viewWillMoveToWindow(id, sel, arg0);
	if (glcontext != null && id == view.id && arg0 != 0) {
		Widget newShell = display.getWidget(new NSWindow(arg0).contentView());
		if (newShell instanceof Shell) {
			((Shell) newShell).glContextCount++;
			((Shell) newShell).updateOpaque();
		}
		Shell shell = getShell();
		shell.glContextCount--;
		shell.updateOpaque();
	}
}

@Override
boolean writeSelectionToPasteboard(long id, long sel, long pasteboardObj, long typesObj) {
	boolean result = false;
	NSPasteboard pboard = new NSPasteboard(pasteboardObj);
	NSArray types = new NSArray(typesObj);
	NSMutableArray typesToDeclare = NSMutableArray.arrayWithCapacity(2);
	NSString type;

	for (long i = 0; i < supportedPboardTypes.count(); i++) {
		type = new NSString(supportedPboardTypes.objectAtIndex(i));
		if (types.containsObject(type)) typesToDeclare.addObject(type);
	}

	if (typesToDeclare.count() > 0) {
		pboard.declareTypes(typesToDeclare, view);
		for (long i = 0; i < typesToDeclare.count(); i++) {
			type = new NSString(typesToDeclare.objectAtIndex(i));
			if (writeSelectionToPasteboard(pboard, type)) result = true;
		}
	}

	return result;
}

boolean writeSelectionToPasteboard(NSPasteboard pboard, NSString type) {
	boolean result = false;

	if (type.isEqualToString(OS.NSPasteboardTypeString)) {
		Accessible acc = getAccessible();
		id selection = acc.internal_accessibilityAttributeValue(OS.NSAccessibilitySelectedTextAttribute, ACC.CHILDID_SELF);
		if (selection != null) {
			NSString selectionString = new NSString(selection);
			if (selectionString.length() > 0) result = pboard.setString(selectionString, OS.NSPasteboardTypeString);
		}
	}

	return result;
}

}
