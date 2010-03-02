/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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
import org.eclipse.swt.internal.cocoa.*;

/**
 * Instances of this class represent a taskbar item.
 * 
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * 
 * @since 3.6
 * 
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TaskItem extends Item {
	TaskBar parent;
	Shell shell;
	NSImage defaultImage;
	int progress, iProgress, progressState = SWT.DEFAULT;
	Image overlayImage;
	String overlayText = "";
	
	static final int PROGRESS_MAX = 100;
	static final int PROGRESS_TIMER = 350;
	static final int PROGRESS_BARS = 7;
	
/**
 * Constructs a new instance of this class given its parent
 * (which must be a <code>Tray</code>) and a style value
 * describing its behavior and appearance. The item is added
 * to the end of the items maintained by its parent.
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
 * @see SWT
 * @see Widget#checkSubclass
 * @see Widget#getStyle
 */
TaskItem (TaskBar parent, int style) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, -1);
	createWidget ();
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void createWidget () {
	NSApplication app = NSApplication.sharedApplication ();
	NSImage image = app.applicationIconImage ();
	defaultImage = new NSImage (image.copy ());
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

public Image getOverlayImage () {
	checkWidget ();
	return overlayImage;
}

public String getOverlayText () {
	checkWidget ();
	return overlayText;
}

/**
 * Returns the receiver's parent, which must be a <code>TaskBar</code>.
 *
 * @return the receiver's parent
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 */
public TaskBar getParent () {
	checkWidget ();
	return parent;
}

public int getProgress () {
	checkWidget ();
	return progress;
}

public int getProgressState () {
	checkWidget ();
	return progressState;
}

void releaseHandle () {
	super.releaseHandle ();
	parent = null;
	if (defaultImage != null) defaultImage.release ();
	defaultImage = null;
}

void releaseWidget () {
	super.releaseWidget ();
	overlayImage = null;
	overlayText = null;
	shell = null;
}

public void setOverlayImage (Image image) {
	checkWidget ();
	if (image != null && image.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	overlayImage = image;
	updateOverlayText (image != null ? null : overlayText);
	updateImage ();
}

public void setOverlayText (String string) {
	checkWidget ();
	if (string == null) error (SWT.ERROR_NULL_ARGUMENT);
	overlayText = string;
	updateOverlayText (string);
	updateImage ();
}

public void setProgress (int progress) {
	checkWidget ();
	progress = Math.max (0, Math.min (progress, PROGRESS_MAX));
	if (this.progress == progress) return;
	this.progress = progress;
	updateImage ();
}

public void setProgressState (int progressState) {
	checkWidget ();
	if (this.progressState == progressState) return;
	this.progressState = progressState;
	updateImage ();
}

void setShell (Shell shell) {
	this.shell = shell;
	shell.addListener (SWT.Dispose, new Listener () {
		public void handleEvent (Event event) {
			if (isDisposed ()) return;
			dispose ();
		}
	});
}

void updateImage () {
	boolean drawProgress = progress != 0 && progressState != SWT.DEFAULT;
	boolean drawIntermidiate = progressState == SWT.INDETERMINATE;
	NSApplication app = NSApplication.sharedApplication ();
	NSDockTile dock = app.dockTile ();
	boolean drawImage = overlayImage != null && dock.badgeLabel () == null;
	if (!drawImage && !drawProgress && !drawIntermidiate) {
		app.setApplicationIconImage (null);
		return;
	}
	
	NSSize size = defaultImage.size ();
	NSImage newImage = (NSImage)new NSImage().alloc ();
	newImage = newImage.initWithSize (size);
	NSBitmapImageRep rep = (NSBitmapImageRep)new NSBitmapImageRep ().alloc ();
	rep = rep.initWithBitmapDataPlanes (0, (int)size.width, (int)size.height, 8, 4, true, false, OS.NSDeviceRGBColorSpace, OS.NSAlphaFirstBitmapFormat | OS.NSAlphaNonpremultipliedBitmapFormat, (int)size.width * 4, 32);
	newImage.addRepresentation (rep);
	rep.release ();
	
	NSRect rect = new NSRect ();
	rect.height = size.height;
	rect.width = size.width;
	newImage.lockFocus ();
	defaultImage.drawInRect (rect, rect, OS.NSCompositeSourceOver, 1);
	if (drawImage) {
		NSImage badgetImage = overlayImage.handle;
		NSSize badgeSize = badgetImage.size ();
		NSRect srcRect = new NSRect ();
		srcRect.height = badgeSize.height;
		srcRect.width = badgeSize.width;
		NSRect dstRect = new NSRect ();
		dstRect.x = size.width / 2;
		dstRect.height = size.height / 2;
		dstRect.width = size.width / 2;
		badgetImage.drawInRect(dstRect, srcRect, OS.NSCompositeSourceOver, 1);
	}
	if (drawIntermidiate || drawProgress) {
		switch (progressState) {
			case SWT.ERROR:
				NSColor.colorWithDeviceRed (1, 0, 0, 0.6f).setFill ();
				break;
			case SWT.PAUSED:
				NSColor.colorWithDeviceRed (1, 1, 0, 0.6f).setFill ();
				break;
			default:
				NSColor.colorWithDeviceRed (1, 1, 1, 0.6f).setFill ();
		}
		rect.width = size.width / (PROGRESS_BARS * 2 - 1);
		rect.height = size.height / 3;
		int count;
		if (drawIntermidiate) {
			count = iProgress;
			iProgress = (iProgress + 1) % (PROGRESS_BARS + 1);
			getDisplay ().timerExec (PROGRESS_TIMER, new Runnable () {
				public void run () {
					updateImage ();
				}
			});
		} else {
			count = progress * PROGRESS_BARS / PROGRESS_MAX;
		}
		for (int i = 0; i <= count; i++) {
			rect.x = i * 2 * rect.width;
			NSBezierPath.fillRect (rect);
		}
	}
	newImage.unlockFocus ();
	app.setApplicationIconImage (newImage);
	newImage.release ();
}

void updateOverlayText (String string) {
	NSApplication app = NSApplication.sharedApplication ();
	NSDockTile dock = app.dockTile ();
	if (string != null && string.length () > 0) {
		dock.setBadgeLabel (NSString.stringWith (string));
	} else {
		dock.setBadgeLabel (null);
	}
}

}
