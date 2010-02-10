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
public class TaskBarItem extends Item {
	TaskBar parent;
	Shell shell;
	int progress, progressState = SWT.DEFAULT;
	Image overlayImage;
	String overlayText = "";

	static final int PROGRESS_MAX = 100;
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
TaskBarItem (TaskBar parent, int style) {
	super (parent, style);
	this.parent = parent;
	parent.createItem (this, -1);
}

protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

void destroyWidget () {
	parent.destroyItem (this);
	releaseHandle ();
}

public Image getOverlayImage () {
	checkWidget();
	return overlayImage;
}

public String getOverlayText () {
	checkWidget();
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
}

void releaseWidget () {
	super.releaseWidget ();
	overlayImage = null;
	overlayText = null;
}

/**
 * Sets the receiver's overlay image.
 *
 * @param overlayImage the new overlay image
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the image has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setOverlayImage (Image overlayImage) {
	checkWidget ();
	if (overlayImage != null && overlayImage.isDisposed ()) error (SWT.ERROR_INVALID_ARGUMENT);
	this.overlayImage = overlayImage;
}

public void setOverlayText (String overlayText) {
	checkWidget ();
	if (overlayText == null) error (SWT.ERROR_NULL_ARGUMENT);
	this.overlayText = overlayText;
}

public void setProgressState (int progressState) {
	checkWidget ();
	if (this.progressState == progressState) return;
	this.progressState = progressState;
}

public void setProgress (int progress) {
	checkWidget ();
	progress = Math.max(0, Math.min(progress, PROGRESS_MAX));
	if (this.progress == progress) return;
	this.progress = progress;
}

}
