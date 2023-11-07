/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
 *     Tasktop Technologies - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class represent a task item.
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
	boolean hasTaskbarButton = false;
	int progress, progressState = SWT.DEFAULT;
	Image overlayImage;
	String overlayText = "";
	boolean showingText = false;
	Menu menu;

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

@Override
protected void checkSubclass () {
	if (!isValidSubclass ()) error (SWT.ERROR_INVALID_SUBCLASS);
}

@Override
void destroyWidget () {
	if (null != parent.mTaskbarList3) { // extra safety just in case
		// MSDN for 'ITaskbarList3::SetOverlayIcon' says:
		//   Because a single overlay is applied to the taskbar button instead
		//   of to the individual window thumbnails, this is a per-group
		//   feature rather than per-window. Requests for overlay icons can
		//   be received from individual windows in a taskbar group, but they
		//   do not queue. The last overlay received is the overlay shown.
		//   If the last overlay received is removed, the overlay that it
		//   replaced is restored so long as it is still active. As an example,
		//   windows 1, 2, and 3 set, in order, overlays A, B, and C.
		//   Because overlay C was received last, it is shown on the taskbar button.
		//   Window 2 calls SetOverlayIcon with a NULL value to remove overlay B.
		//   Window 3 then does the same to remove overlay C.
		//   Because window 1's overlay A is still active, that overlay is then
		//   displayed on the taskbar button.
		// This implies that Shell should pass NULL before closing.
		//
		// Issue #603: On Win11 the lack of this actually caused wrong overlay
		// to show after closing Shell.
		parent.mTaskbarList3.SetOverlayIcon(shell.handle, 0, 0);
	}

	parent.destroyItem (this);
	releaseHandle ();
}

/**
 * Returns the receiver's pop up menu if it has one, or null
 * if it does not.
 *
 * @return the receiver's menu
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
@Override
public Menu getMenu () {
	checkWidget ();
	return menu;
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

void updateImageAndText () {
	if (showingText && (overlayText.length () != 0)) {
		updateText ();
	} else if (overlayImage != null) {
		updateImage ();
	} else {
		parent.mTaskbarList3.SetOverlayIcon(shell.handle, 0, 0);
	}
}

void updateAll () {
	updateImageAndText ();
	if (progress != 0) updateProgress ();
	if (progressState != SWT.DEFAULT) updateProgressState ();
}

void onTaskbarButtonCreated () {
	this.hasTaskbarButton = true;
	updateAll ();
}

@Override
void releaseHandle () {
	super.releaseHandle ();
	parent = null;
}

@Override
void releaseWidget () {
	super.releaseWidget ();
	overlayImage = null;
	overlayText = null;
}

/**
 * Sets the receiver's pop up menu to the argument. The way the menu is
 * shown is platform specific.
 *
 * <p>
 * This feature might not be available for the receiver on all
 * platforms. The application code can check if it is supported
 * by calling the respective get method. When the feature is not
 * available, the get method will always return the NULL.</p>
 *
 * <p>
 * For better cross platform support, the application code should
 * set this feature on the <code>TaskItem</code> for application.<br>
 * On Windows, this feature will only work on RCP applications.</p>
 *
 * <p>
 * The menu should be fully created before this method is called.
 * Dynamic changes to the menu after the method is called will not be reflected
 * in the native menu.</p>
 *
 * @param menu the new pop up menu
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_MENU_NOT_POP_UP - the menu is not a pop up menu</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the menu has been disposed</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 */
public void setMenu (Menu menu) {
	checkWidget ();
	if (menu != null) {
		if (menu.isDisposed()) error(SWT.ERROR_INVALID_ARGUMENT);
		if ((menu.style & SWT.POP_UP) == 0) {
			error (SWT.ERROR_MENU_NOT_POP_UP);
		}
	}
	if (shell != null) return;
	this.menu = menu;
	parent.setMenu (menu);
}

/**
 * Sets the receiver's overlay image, which may be null
 * indicating that no image should be displayed. The bounds
 * for the overlay image is determined by the platform and in
 * general it should be a small image.
 *
 * <p>
 * This feature might not be available for the receiver on all
 * platforms. The application code can check if it is supported
 * by calling the respective get method. When the feature is not
 * available, the get method will always return the NULL.</p>
 *
 * <p>
 * For better cross platform support, the application code should
 * first try to set this feature on the <code>TaskItem</code> for the
 * main shell then on the <code>TaskItem</code> for the application.</p>
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
	if (shell == null) return;
	this.overlayImage = overlayImage;
	this.showingText = (this.overlayImage == null);

	// MSDN for 'ITaskbarList3' says:
	// TaskbarButtonCreated ... message must be received by your application before it calls any ITaskbarList3 method
	// #updateAll() will be called later when message is received.
	if (!hasTaskbarButton) return;

	updateImageAndText ();
}

/**
 * Sets the receiver's overlay text. The space available to display the
 * overlay text is platform dependent and in general it should be no longer
 * than a few characters.
 *
 * <p>
 * This feature might not be available for the receiver on all
 * platforms. The application code can check if it is supported
 * by calling the respective get method. When the feature is not
 * available, the get method will always return an empty string.</p>
 *
 * <p>
 * For better cross platform support, the application code should
 * first try to set this feature on the <code>TaskItem</code> for the
 * main shell then on the <code>TaskItem</code> for the application.</p>
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
	if (shell == null) return;
	this.overlayText = overlayText;
	this.showingText = (this.overlayText.length() != 0);

	// MSDN for 'ITaskbarList3' says:
	// TaskbarButtonCreated ... message must be received by your application before it calls any ITaskbarList3 method
	// #updateAll() will be called later when message is received.
	if (!hasTaskbarButton) return;

	updateImageAndText ();
}

/**
 * Sets the receiver's progress, the progress represents a percentage and
 * should be in range from 0 to 100. The progress is only shown when the progress
 * state is different than <code>SWT#DEFAULT</code>.
 *
 * <p>
 * This feature might not be available for the receiver on all
 * platforms. The application code can check if it is supported
 * by calling the respective get method. When the feature is not
 * available, the get method will always return zero.</p>
 *
 * <p>
 * For better cross platform support, the application code should
 * first try to set this feature on the <code>TaskItem</code> for the
 * main shell then on the <code>TaskItem</code> for the application.</p>
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
	if (shell == null) return;
	progress = Math.max (0, Math.min (progress, PROGRESS_MAX));
	if (this.progress == progress) return;
	this.progress = progress;

	// MSDN for 'ITaskbarList3' says:
	// TaskbarButtonCreated ... message must be received by your application before it calls any ITaskbarList3 method
	// #updateAll() will be called later when message is received.
	if (!hasTaskbarButton) return;

	updateProgress ();
}

/**
 * Sets the receiver's progress state, the state can be one of
 * the following:
 * <ul>
 * <li>{@link SWT#DEFAULT}</li>
 * <li>{@link SWT#NORMAL}</li>
 * <li>{@link SWT#PAUSED}</li>
 * <li>{@link SWT#ERROR}</li>
 * <li>{@link SWT#INDETERMINATE}</li>
 * </ul>
 *
 * The percentage of progress shown by the states <code>SWT#NORMAL</code>, <code>SWT#PAUSED</code>,
 * <code>SWT#ERROR</code> is set with <code>setProgress()</code>. <br>
 * The state <code>SWT#DEFAULT</code> indicates that no progress should be shown.
 *
 * <p>
 * This feature might not be available for the receiver on all
 * platforms. The application code can check if it is supported
 * by calling the respective get method. When the feature is not
 * available, the get method will always return <code>SWT#DEFAULT</code>.</p>
 *
 * <p>
 * For better cross platform support, the application code should
 * first try to set this feature on the <code>TaskItem</code> for the
 * main shell then on the <code>TaskItem</code> for the application.</p>
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
	if (shell == null) return;
	if (this.progressState == progressState) return;
	this.progressState = progressState;

	// MSDN for 'ITaskbarList3' says:
	// TaskbarButtonCreated ... message must be received by your application before it calls any ITaskbarList3 method
	// #updateAll() will be called later when message is received.
	if (!hasTaskbarButton) return;

	updateProgressState ();
}

void setShell (Shell shell) {
	this.shell = shell;
	shell.addListener (SWT.Dispose, new Listener () {
		@Override
		public void handleEvent (Event event) {
			if (isDisposed ()) return;
			dispose ();
		}
	});
}

void updateImage () {
	Image image2 = null;
	long hIcon = 0;
	switch (overlayImage.type) {
		case SWT.BITMAP:
			image2 = Display.createIcon (overlayImage);
			hIcon = image2.handle;
			break;
		case SWT.ICON:
			hIcon = overlayImage.handle;
			break;
	}
	parent.mTaskbarList3.SetOverlayIcon(shell.handle, hIcon, 0);
	if (image2 != null) image2.dispose ();
}

void updateProgress () {
	if (progressState == SWT.INDETERMINATE) return;
	if (progressState == SWT.DEFAULT) return;
	parent.mTaskbarList3.SetProgressValue(shell.handle, progress, PROGRESS_MAX);
}

void updateProgressState () {
	int tbpFlags = OS.TBPF_NOPROGRESS;
	switch (progressState) {
		case SWT.NORMAL: tbpFlags = OS.TBPF_NORMAL; break;
		case SWT.ERROR: tbpFlags = OS.TBPF_ERROR; break;
		case SWT.PAUSED: tbpFlags = OS.TBPF_PAUSED; break;
		case SWT.INDETERMINATE: tbpFlags = OS.TBPF_INDETERMINATE; break;
	}
	parent.mTaskbarList3.SetProgressValue(shell.handle, progress, PROGRESS_MAX);
	parent.mTaskbarList3.SetProgressState(shell.handle, tbpFlags);
}

long renderTextIcon () {
	/* Create resources */
	int width = 16, height = 16;
	long hdc = OS.GetDC (0);
	BITMAPINFOHEADER bmiHeader = new BITMAPINFOHEADER ();
	bmiHeader.biSize = BITMAPINFOHEADER.sizeof;
	bmiHeader.biWidth = width;
	bmiHeader.biHeight = -height;
	bmiHeader.biPlanes = 1;
	bmiHeader.biBitCount = 32;
	bmiHeader.biCompression = OS.BI_RGB;
	byte []	bmi = new byte [BITMAPINFOHEADER.sizeof];
	OS.MoveMemory (bmi, bmiHeader, BITMAPINFOHEADER.sizeof);
	long [] pBits = new long [1];
	long hBitmap = OS.CreateDIBSection (0, bmi, OS.DIB_RGB_COLORS, pBits, 0, 0);
	if (hBitmap == 0) error (SWT.ERROR_NO_HANDLES);
	long dstHdc = OS.CreateCompatibleDC (hdc);
	long oldBitmap = OS.SelectObject (dstHdc, hBitmap);
	long hMask = OS.CreateBitmap (width, height, 1, 1, null);
	if (hMask == 0) error (SWT.ERROR_NO_HANDLES);
	long maskHdc = OS.CreateCompatibleDC (hdc);
	long oldMask = OS.SelectObject (maskHdc, hMask);

	/* Draw content */
	OS.PatBlt (maskHdc, 0, 0, width, height, OS.WHITENESS);
	long oldBrush = OS.SelectObject (maskHdc, OS.GetStockObject (OS.BLACK_BRUSH));
	OS.RoundRect (maskHdc, 0, 0, width, height, 8, 8);
	OS.SelectObject (maskHdc, oldBrush);

	long brush = OS.CreateSolidBrush (OS.GetSysColor (OS.COLOR_HIGHLIGHT));
	oldBrush = OS.SelectObject (dstHdc, brush);
	OS.RoundRect (dstHdc, 0, 0, width, height, 8, 8);
	OS.SelectObject (dstHdc, oldBrush);
	OS.DeleteObject (brush);

	int uFormat = OS.DT_LEFT | OS.DT_SINGLELINE | OS.DT_NOPREFIX;
	RECT rect = new RECT ();
	char [] buffer = overlayText.toCharArray ();
	int length = buffer.length;
	long hFont = 0, oldHFont = 0;
	NONCLIENTMETRICS info = new NONCLIENTMETRICS ();
	info.cbSize = NONCLIENTMETRICS.sizeof;
	if (OS.SystemParametersInfo (OS.SPI_GETNONCLIENTMETRICS, 0, info, 0)) {
		LOGFONT logFont = info.lfMessageFont;
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
	long hIcon = OS.CreateIconIndirect (iconInfo);
	if (hIcon == 0) error (SWT.ERROR_NO_HANDLES);
	OS.DeleteObject (hBitmap);
	OS.DeleteObject (hMask);

	return hIcon;
}

void updateText () {
	long hIcon = renderTextIcon();
	parent.mTaskbarList3.SetOverlayIcon(shell.handle, hIcon, 0);
	OS.DestroyIcon (hIcon);
}

}
