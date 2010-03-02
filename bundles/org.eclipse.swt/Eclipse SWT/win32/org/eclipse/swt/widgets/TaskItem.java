/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent a task bar item.
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
TaskItem (TaskBar parent, int style) {
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

/**
 * Returns the receiver's overlay image if it has one, or null
 * if it does not.
 *
 * @return the receiver's overlay image
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public Image getOverlayImage () {
	checkWidget ();
	return overlayImage;
}

/**
 * Returns the receiver's overlay text, which will be an empty
 * string if it has never been set.
 *
 * @return the receiver's overlay text
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
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

/**
 * Returns the receiver's progress.
 *
 * @return the receiver's progress
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public int getProgress () {
	checkWidget ();
	return progress;
}

/**
 * Returns the receiver's progress state.
 *
 * @return the receiver's progress state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
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
 * Sets the receiver's overlay image, which may be null
 * indicating that no image should be displayed. The bounds
 * for the overlay image is determined by the platform and in 
 * general it should be a small image.
 *
 * @param overlayImage the new overlay image (may be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the overlayImage has been disposed</li> 
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
	if (overlayImage != null) {
		updateImage ();
	} else {
		if (overlayText.length () != 0) {
			updateText ();
		} else {
			int /*long*/ mTaskbarList3 = parent.mTaskbarList3;
			int /*long*/ hwnd = shell.handle;
			/* ITaskbarList3::SetOverlayIcon */
			OS.VtblCall (18, mTaskbarList3, hwnd, 0, 0);
		}
	}
}

/**
 * Sets the receiver's overlay text. The space available to display the
 * overlay text is platform dependent and in general it should be no longer
 * than a few characters.
 *
 * @param overlayText the new overlay text
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the overlayText is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setOverlayText (String overlayText) {
	checkWidget ();
	if (overlayText == null) error (SWT.ERROR_NULL_ARGUMENT);
	this.overlayText = overlayText;
	if (overlayText.length () != 0) {
		updateText ();
	} else {
		if (overlayImage != null) {
			updateImage ();
		} else {
			int /*long*/ mTaskbarList3 = parent.mTaskbarList3;
			int /*long*/ hwnd = shell.handle;
			/* ITaskbarList3::SetOverlayIcon */
			OS.VtblCall (18, mTaskbarList3, hwnd, 0, 0);
		}
	}
}

/**
 * Sets the receiver's progress, the progress represents a percentage and
 * should be in range from 0 to 100. The progress is only shown when the progress
 * state is different than <code>SWT#DEFAULT</code>.
 *
 * @param progress the new progress
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * #see {@link #setProgressState(int)}
 */
public void setProgress (int progress) {
	checkWidget ();
	progress = Math.max (0, Math.min (progress, PROGRESS_MAX));
	if (this.progress == progress) return;
	this.progress = progress;
	if (progressState == SWT.INDETERMINATE) return;
	if (progressState == SWT.DEFAULT) return;
	int /*long*/ mTaskbarList3 = parent.mTaskbarList3;
	int /*long*/ hwnd = shell.handle;
	/* ITaskbarList3::SetProgressValue */
	OS.VtblCall (9, mTaskbarList3, hwnd, (long)progress, (long)PROGRESS_MAX);
}

/**
 * Sets the receiver's progress state, the state can be one of
 * the following:
 * <p><ul>
 * <li>{@link SWT#DEFAULT}</li>
 * <li>{@link SWT#NORMAL}</li>
 * <li>{@link SWT#PAUSED}</li>
 * <li>{@link SWT#ERROR}</li>
 * <li>{@link SWT#INDETERMINATE}</li>
 * </ul></p>
 * 
 * The percentage of progress shown by the states <code>SWT#NORMAL</code>, <code>SWT#PAUSED</code>, 
 * <code>SWT#ERROR</code> is set with <code>setProgress()</code>. <br>
 * The state <code>SWT#DEFAULT</code> indicates that no progress should be shown.
 *
 * @param progressState the new progress state 
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * #see {@link #setProgress(int)}
 */
public void setProgressState (int progressState) {
	checkWidget ();
	if (this.progressState == progressState) return;
	this.progressState = progressState;
	int tbpFlags = OS.TBPF_NOPROGRESS;
	switch (progressState) {
		case SWT.NORMAL: tbpFlags = OS.TBPF_NORMAL; break;
		case SWT.ERROR: tbpFlags = OS.TBPF_ERROR; break;
		case SWT.PAUSED: tbpFlags = OS.TBPF_PAUSED; break;
		case SWT.INDETERMINATE: tbpFlags = OS.TBPF_INDETERMINATE; break;
	}
	int /*long*/ mTaskbarList3 = parent.mTaskbarList3;
	int /*long*/ hwnd = shell.handle;
	/* ITaskbarList3::SetProgressValue */
	OS.VtblCall (9, mTaskbarList3, hwnd, (long)progress, (long)PROGRESS_MAX);
	/* ITaskbarList3::SetProgressState */
	OS.VtblCall (10, mTaskbarList3, hwnd, tbpFlags);
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
	Image image2 = null;
	int /*long*/ hIcon = 0;
	switch (overlayImage.type) {
		case SWT.BITMAP:
			image2 = Display.createIcon (overlayImage);
			hIcon = image2.handle;
			break;
		case SWT.ICON:
			hIcon = overlayImage.handle;
			break;
	}
	int /*long*/ mTaskbarList3 = parent.mTaskbarList3;
	int /*long*/ hwnd = shell.handle;
	/* ITaskbarList3::SetOverlayIcon */
	OS.VtblCall (18, mTaskbarList3, hwnd, hIcon, 0);
	if (image2 != null) image2.dispose ();
}

void updateText () {
	/* Create resources */
	int width = 16, height = 16;
	int /*long*/ hdc = OS.GetDC (0);
	BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER ();
	bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
	bmiHeader.biWidth = width;
	bmiHeader.biHeight = -height;
	bmiHeader.biPlanes = 1;
	bmiHeader.biBitCount = 32;
	bmiHeader.biCompression = OS.BI_RGB;
	byte []	bmi = new byte [BITMAPINFOHEADER.sizeof];
	OS.MoveMemory (bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
	int /*long*/ [] pBits = new int /*long*/ [1];
	int /*long*/ hBitmap = OS.CreateDIBSection (0, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
	if (hBitmap == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	int /*long*/ dstHdc = OS.CreateCompatibleDC (hdc);
	int /*long*/ oldBitmap = OS.SelectObject (dstHdc, hBitmap);
	int /*long*/ hMask = OS.CreateBitmap (width, height, 1, 1, null);
	if (hMask == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	int /*long*/ maskHdc = OS.CreateCompatibleDC (hdc);
	int /*long*/ oldMask = OS.SelectObject (maskHdc, hMask);
	
	/* Draw content */
	OS.PatBlt (maskHdc, 0, 0, width, height, OS.WHITENESS);
	int /*long*/ oldBrush = OS.SelectObject (maskHdc, OS.GetStockObject (OS.BLACK_BRUSH));
	OS.RoundRect (maskHdc, 0, 0, width, height, 8, 8);
	OS.SelectObject (maskHdc, oldBrush);
	
	int /*long*/ brush = OS.CreateSolidBrush (OS.GetSysColor (OS.COLOR_HIGHLIGHT));
	oldBrush = OS.SelectObject (dstHdc, brush);
	OS.RoundRect (dstHdc, 0, 0, width, height, 8, 8);
	OS.SelectObject (dstHdc, oldBrush);
	OS.DeleteObject (brush);

	int uFormat = OS.DT_LEFT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
	RECT rect = new RECT ();
	TCHAR buffer = new TCHAR (shell.getCodePage (), overlayText, false);
	int length = buffer.length();
	int /*long*/ hFont = 0, oldHFont = 0;
	NONCLIENTMETRICS info = OS.IsUnicode ? (NONCLIENTMETRICS) new NONCLIENTMETRICSW () : new NONCLIENTMETRICSA ();
	info.cbSize = NONCLIENTMETRICS.sizeof;
	if (OS.SystemParametersInfo (OS.SPI_GETNONCLIENTMETRICS, 0, info, 0)) {
		LOGFONT logFont = OS.IsUnicode ? (LOGFONT) ((NONCLIENTMETRICSW)info).lfMessageFont : ((NONCLIENTMETRICSA)info).lfMessageFont;
		logFont.lfHeight = -10;
		hFont = OS.CreateFontIndirect (logFont);
		oldHFont = OS.SelectObject (dstHdc, hFont);
		OS.DrawText (dstHdc, buffer, length, rect, uFormat | OS.DT_CALCRECT);
		if (rect.right > width - 2) {
			OS.SelectObject (dstHdc, oldHFont);
			OS.DeleteObject (hFont);
			logFont.lfHeight = -8;
			hFont = OS.CreateFontIndirect (logFont);
			OS.SelectObject (dstHdc, hFont);
		}
	}
	OS.DrawText (dstHdc, buffer, length, rect, uFormat | OS.DT_CALCRECT);
	OS.OffsetRect (rect, (width - rect.right) / 2, (height - rect.bottom) / 2);
	int oldBkMode = OS.SetBkMode (dstHdc, OS.TRANSPARENT);
	OS.SetTextColor (dstHdc, OS.GetSysColor (OS.COLOR_HIGHLIGHTTEXT));
	OS.DrawText (dstHdc, buffer, length, rect, uFormat);
	if (hFont != 0) {
		OS.SelectObject (dstHdc, oldHFont);
		OS.DeleteObject (hFont);
	}
	OS.SetBkMode(dstHdc, oldBkMode);
	
	/* Release resources */
	OS.SelectObject (dstHdc, oldBitmap);
	OS.DeleteDC (dstHdc);
	OS.SelectObject (maskHdc, oldMask);
	OS.DeleteDC (maskHdc);
	OS.ReleaseDC (0, hdc);
	
	ICONINFO iconInfo = new ICONINFO ();
	iconInfo.fIcon = true;
	iconInfo.hbmColor = hBitmap;
	iconInfo.hbmMask = hMask;
	int /*long*/ hIcon = OS.CreateIconIndirect (iconInfo);
	if (hIcon == 0) SWT.error (SWT.ERROR_NO_HANDLES);
	OS.DeleteObject (hBitmap);
	OS.DeleteObject (hMask);
	
	int /*long*/ mTaskbarList3 = parent.mTaskbarList3;
	int /*long*/ hwnd = shell.handle;
	/* ITaskbarList3::SetOverlayIcon */
	OS.VtblCall (18, mTaskbarList3, hwnd, hIcon, 0);
	OS.DestroyIcon (hIcon);
}

}
