/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import java.util.Hashtable;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class implement the browser user interface
 * metaphor.  It allows the user to visualize and navigate through
 * HTML documents.
 * <p>
 * Note that although this class is a subclass of <code>Composite</code>,
 * it does not make sense to set a layout on it.
 * </p><p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @since 3.0
 */
public class Browser extends Composite {
	
	/* Package Name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.browser."; //$NON-NLS-1$
	
	static final int MAX_PROGRESS = 100;
	
	/* External Listener management */
	CloseWindowListener[] closeWindowListeners = new CloseWindowListener[0];
	LocationListener[] locationListeners = new LocationListener[0];
	OpenWindowListener[] openWindowListeners = new OpenWindowListener[0];
	ProgressListener[] progressListeners = new ProgressListener[0];
	StatusTextListener[] statusTextListeners = new StatusTextListener[0];
	TitleListener[] titleListeners = new TitleListener[0];
	VisibilityWindowListener[] visibilityWindowListeners = new VisibilityWindowListener[0];
	
	/* Objective-C WebView delegate */
	static int Delegate;
	
	static Hashtable Map = new Hashtable();
	static Callback callback;

	/* Carbon HIView handle */
	int webViewHandle;
	
	int identifier;
	int resourceCount;
	String url = "";
	Point location;
	Point size;
	boolean statusBar = true, toolBar = true;
	boolean added;

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
 * @param parent a widget which will be the parent of the new instance (cannot be null)
 * @param style the style of widget to construct
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES if a handle could not be obtained for browser creation</li>
 * </ul>
 * 
 * @see #getStyle
 * 
 * @since 3.0
 */
public Browser(Composite parent, int style) {
	super(parent, style);

	/*
	* Note.  Loading the webkit bundle on Jaguar causes a crash.
	* The workaround is to detect any OS prior to 10.30 and fail
	* without crashing.
	*/
	int[] response = new int[1];
	int err = OS.Gestalt(OS.gestaltSystemVersion, response);
	if (err != OS.noErr || ((response[0] & 0xffff) < 0x1030)) {
		dispose();
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	int outControl[] = new int[1];
	try {
		WebKit.HIWebViewCreate(outControl);
	} catch (UnsatisfiedLinkError e) {
		dispose();
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	webViewHandle = outControl[0];
	if (webViewHandle == 0) {
		dispose();
		SWT.error(SWT.ERROR_NO_HANDLES);		
	}
	/*
	* Bug in Safari. The WebView must be added after the top window is visible
	* or it eats mouse events from the top window. A second issue is that the WebView 
	* does not receive mouse and key events when it is added to a visible top window. 
	* It is assumed that Safari hooks its own event listener when the top window 
	* emits the kEventWindowShown event. The workaround to the first problem is to add
	* the WebView to the HIView after the top window is visible. The workaround to the second
	* problem is to send a fake kEventWindowShown event to the top window after the WebView
	* has been added to the HIView (after the top window is visible) to give Safari a chance
	* to hook events.
	*/
	if (getShell().isVisible()) {
		added = true;
		OS.HIViewAddSubview(handle, webViewHandle);
		OS.HIViewSetVisible(webViewHandle, true);
		
		int[] showEvent = new int[1];
		OS.CreateEvent(0, OS.kEventClassWindow, OS.kEventWindowShown, 0.0, OS.kEventAttributeUserEvent, showEvent);
		OS.SetEventParameter(showEvent[0], OS.kEventParamDirectObject, OS.typeWindowRef, 4, new int[] {OS.GetControlOwner(handle)});
		OS.SendEventToEventTarget(showEvent[0], OS.GetWindowEventTarget(OS.GetControlOwner(handle)));
		if (showEvent[0] != 0) OS.ReleaseEvent(showEvent[0]);
	}

	final int webView = WebKit.HIWebViewGetWebView(webViewHandle);
	/*
	* This code is intentionally commented. Setting a group name is the right thing
	* to do in order to avoid multiple open window requests. For some reason, Safari
	* crashes when requested to reopen the same window if that window was previously
	* closed. This may be because that window was not correctly closed. 
	*/	
//	String groupName = "MyDocument"; //$NON-NLS-1$
//	int length = groupName.length();
//	char[] buffer = new char[length];
//	groupName.getChars(0, length, buffer, 0);
//	int groupNameString = OS.CFStringCreateWithCharacters(0, buffer, length);
//	// [webView setGroupName:@"MyDocument"];
//	WebKit.objc_msgSend(webView, WebKit.S_setGroupName, groupNameString);
//	OS.CFRelease(groupNameString);
	
	final int notificationCenter = WebKit.objc_msgSend(WebKit.C_NSNotificationCenter, WebKit.S_defaultCenter);

	Listener listener = new Listener() {
		public void handleEvent(Event e) {
			switch (e.type) {
				case SWT.Dispose: {
					Shell shell = getShell();
					shell.removeListener(SWT.Resize, this);
					shell.removeListener(SWT.Show, this);
					shell.removeListener(SWT.Hide, this);
					Control c = Browser.this;
					do {
						c.removeListener(SWT.Show, this);
						c.removeListener(SWT.Hide, this);
						c = c.getParent();
					} while (c != shell);

					Map.remove(new Integer(webView));
					WebKit.objc_msgSend(notificationCenter, WebKit.S_removeObserver_name_object, Delegate, 0, webView);
					break;
				}
				case SWT.Hide: {
					/*
					* Bug on Safari. The web view cannot be obscured by other views above it.
					* This problem is specified in the apple documentation for HiWebViewCreate.
					* The workaround is to hook Hide and Show events on the browser's parents
					* and set its size to 0 in Hide and to restore its size in Show.
					*/
					CGRect bounds = new CGRect();
					OS.HIViewSetFrame(webViewHandle, bounds);
					break;
				}
				case SWT.Show: {
					if (!added && e.widget == getShell()) {
						/*
						* Bug in Safari. The WebView must be added after the top window is visible
						* or it eats mouse events from the top window. A second issue is that the WebView 
						* does not receive mouse and key events when it is added to a visible top window. 
						* It is assumed that Safari hooks its own event listener when the top window 
						* emits the kEventWindowShown event. The workaround to the first problem is to add
						* the WebView to the HIView after the top window is visible. The workaround to the second
						* problem is to send a fake kEventWindowShown event to the top window after the WebView
						* has been added to the HIView (after the top window is visible) to give Safari a chance
						* to hook events.
						* 
						* Note. SWT.Show is sent before the Shell is actually visible. The workaround is to
						* add the WebView to the HiView from an asyncExec, after this notification has returned and
						* made the Shell visible.
						* Note. Because the Shell is already visible when WebView is added to the HiView and the real
						* kEventWindowShow event has therefore been sent already, we need to send a fake kEventWindowShown
						* event so that Safari can hook its events.
						*/
						final Shell shell = getShell();
						getDisplay().asyncExec(new Runnable() { 
							public void run() {
								if (isDisposed() || shell.isDisposed()) return;
								if (added || !shell.isVisible()) return;
								added = true;
								OS.HIViewAddSubview(handle, webViewHandle);
								OS.HIViewSetVisible(webViewHandle, true);

								int[] showEvent = new int[1];
								OS.CreateEvent(0, OS.kEventClassWindow, OS.kEventWindowShown, 0.0, OS.kEventAttributeUserEvent, showEvent);
								OS.SetEventParameter(showEvent[0], OS.kEventParamDirectObject, OS.typeWindowRef, 4, new int[] {OS.GetControlOwner(handle)});
								OS.SendEventToEventTarget(showEvent[0], OS.GetWindowEventTarget(OS.GetControlOwner(handle)));
								if (showEvent[0] != 0) OS.ReleaseEvent(showEvent[0]);
							}
						});
					}
					/*
					* Bug on Safari. The web view cannot be obscured by other views above it.
					* This problem is specified in the apple documentation for HiWebViewCreate.
					* The workaround is to hook Hide and Show events on the browser's parents
					* and set its size to 0 in Hide and to restore its size in Show.
					*/
					CGRect bounds = new CGRect();
					OS.HIViewGetFrame(handle, bounds);
					OS.HIViewSetFrame(webViewHandle, bounds);
					break;
				}
				case SWT.Resize: {
					/*
					* Bug on Safari. Resizing the height of a Shell containing a Browser at
					* a fixed location causes the Browser to redraw at a wrong location.
					* The web view is a HIView container that internally hosts
					* a Cocoa NSView that uses a coordinates system with the origin at the
					* bottom left corner of a window instead of the coordinates system used
					* in Carbon that starts at the top left corner. The workaround is to
					* reposition the web view every time the Shell of the Browser is resized.
					* 
					* Feature on Safari. The HIView ignores the call to update its position
					* because it believes it has not changed. The workaround is to force
					* it to reposition by changing its size and setting it back to the
					* original value.
					* 
					*/
					/* If the widget is hidden, leave its size to 0,0 as set in the SWT.Hide callback */
					if (!isVisible()) break;
					CGRect bounds = new CGRect();
					OS.HIViewGetFrame(handle, bounds);
					/* 
					* Note.  Setting negative width or height causes Safari to always
					* display incorrectly even if further resize events are correct.
					*/
					if (bounds.width < 0) bounds.width = 0;
					if (bounds.height < 0) bounds.height = 0;
					OS.HIViewSetFrame(webViewHandle, bounds);
					/*
					* Bug on Webkit. Resizing a Cocoa WebView in a Carbon window does not
					* redraw the new area of the WebView. The workaround is to call Display.update.
					* That call invokes UpdateControls on the Carbon window and that causes the
					* Cocoa view to redraw correctly.
					*/
					e.display.update();
					break;
				}
			}
		}
	};
	addListener(SWT.Dispose, listener);
	addListener(SWT.Resize, listener);
	Shell shell = getShell();
	shell.addListener(SWT.Resize, listener);
	shell.addListener(SWT.Show, listener);
	shell.addListener(SWT.Hide, listener);
	Control c = this;
	do {
		c.addListener(SWT.Show, listener);
		c.addListener(SWT.Hide, listener);
		c = c.getParent();
	} while (c != shell);
	
	if (callback == null) {
		callback = new Callback(this.getClass(), "eventProc", 6); //$NON-NLS-1$
		int eventProc = callback.getAddress();
		// Delegate = [[WebResourceLoadDelegate alloc] init eventProc];
		Delegate = WebKit.objc_msgSend(WebKit.C_WebKitDelegate, WebKit.S_alloc);
		Delegate = WebKit.objc_msgSend(Delegate, WebKit.S_initWithProc, eventProc);
	}

	Map.put(new Integer(webView), this);
				
	// [webView setFrameLoadDelegate:delegate];
	WebKit.objc_msgSend(webView, WebKit.S_setFrameLoadDelegate, Delegate);
		
	// [webView setResourceLoadDelegate:delegate];
	WebKit.objc_msgSend(webView, WebKit.S_setResourceLoadDelegate, Delegate);

	// [webView setUIDelegate:delegate];
	WebKit.objc_msgSend(webView, WebKit.S_setUIDelegate, Delegate);
	
	/* register delegate for all notifications send out from webview */
	WebKit.objc_msgSend(notificationCenter, WebKit.S_addObserver_selector_name_object, Delegate, WebKit.S_handleNotification, 0, webView);
	
	// [webView setPolicyDelegate:delegate];
	WebKit.objc_msgSend(webView, WebKit.S_setPolicyDelegate, Delegate);
}

static int eventProc(int webview, int selector, int arg0, int arg1, int arg2, int arg3) {
	Object o = Map.get(new Integer(webview));
	if (o instanceof Browser)
		return ((Browser)o).handleCallback(selector, arg0, arg1, arg2, arg3);
	return 0;
}

/**	 
 * Adds the listener to receive events.
 * <p>
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addCloseWindowListener(CloseWindowListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);	
	CloseWindowListener[] newCloseWindowListeners = new CloseWindowListener[closeWindowListeners.length + 1];
	System.arraycopy(closeWindowListeners, 0, newCloseWindowListeners, 0, closeWindowListeners.length);
	closeWindowListeners = newCloseWindowListeners;
	closeWindowListeners[closeWindowListeners.length - 1] = listener;
}

/**	 
 * Adds the listener to receive events.
 * <p>
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addLocationListener(LocationListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	LocationListener[] newLocationListeners = new LocationListener[locationListeners.length + 1];
	System.arraycopy(locationListeners, 0, newLocationListeners, 0, locationListeners.length);
	locationListeners = newLocationListeners;
	locationListeners[locationListeners.length - 1] = listener;
}

/**	 
 * Adds the listener to receive events.
 * <p>
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addOpenWindowListener(OpenWindowListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	OpenWindowListener[] newOpenWindowListeners = new OpenWindowListener[openWindowListeners.length + 1];
	System.arraycopy(openWindowListeners, 0, newOpenWindowListeners, 0, openWindowListeners.length);
	openWindowListeners = newOpenWindowListeners;
	openWindowListeners[openWindowListeners.length - 1] = listener;
}

/**	 
 * Adds the listener to receive events.
 * <p>
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addProgressListener(ProgressListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	ProgressListener[] newProgressListeners = new ProgressListener[progressListeners.length + 1];
	System.arraycopy(progressListeners, 0, newProgressListeners, 0, progressListeners.length);
	progressListeners = newProgressListeners;
	progressListeners[progressListeners.length - 1] = listener;
}

/**	 
 * Adds the listener to receive events.
 * <p>
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addStatusTextListener(StatusTextListener listener) {
	checkWidget();
	if (listener == null) SWT.error (SWT.ERROR_NULL_ARGUMENT);
	StatusTextListener[] newStatusTextListeners = new StatusTextListener[statusTextListeners.length + 1];
	System.arraycopy(statusTextListeners, 0, newStatusTextListeners, 0, statusTextListeners.length);
	statusTextListeners = newStatusTextListeners;
	statusTextListeners[statusTextListeners.length - 1] = listener;
}

/**	 
 * Adds the listener to receive events.
 * <p>
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addTitleListener(TitleListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	TitleListener[] newTitleListeners = new TitleListener[titleListeners.length + 1];
	System.arraycopy(titleListeners, 0, newTitleListeners, 0, titleListeners.length);
	titleListeners = newTitleListeners;
	titleListeners[titleListeners.length - 1] = listener;
}

/**	 
 * Adds the listener to receive events.
 * <p>
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addVisibilityWindowListener(VisibilityWindowListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	VisibilityWindowListener[] newVisibilityWindowListeners = new VisibilityWindowListener[visibilityWindowListeners.length + 1];
	System.arraycopy(visibilityWindowListeners, 0, newVisibilityWindowListeners, 0, visibilityWindowListeners.length);
	visibilityWindowListeners = newVisibilityWindowListeners;
	visibilityWindowListeners[visibilityWindowListeners.length - 1] = listener;
}

/**
 * Navigate to the previous session history item.
 *
 * @return <code>true</code> if the operation was successful and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @see #forward
 * 
 * @since 3.0
 */
public boolean back() {
	checkWidget();
	int webView = WebKit.HIWebViewGetWebView(webViewHandle);
	return WebKit.objc_msgSend(webView, WebKit.S_goBack) != 0;
}

protected void checkSubclass () {
	String name = getClass().getName();
	int index = name.lastIndexOf('.');
	if (!name.substring(0, index + 1).equals(PACKAGE_PREFIX)) {
		SWT.error(SWT.ERROR_INVALID_SUBCLASS);
	}
}

/**
 * Navigate to the next session history item.
 *
 * @return <code>true</code> if the operation was successful and <code>false</code> otherwise
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @see #back
 * 
 * @since 3.0
 */
public boolean forward() {
	checkWidget();
	int webView = WebKit.HIWebViewGetWebView(webViewHandle);
	return WebKit.objc_msgSend(webView, WebKit.S_goForward) != 0;
}

/**
 * Returns the current URL.
 *
 * @return the current URL or an empty <code>String</code> if there is no current URL
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @see #setUrl
 * 
 * @since 3.0
 */
public String getUrl() {
	checkWidget();
	return url;
}

/* Here we dispatch all WebView upcalls. */
int handleCallback(int selector, int arg0, int arg1, int arg2, int arg3) {
	int ret = 0;
	// for meaning of selector see WebKitDelegate methods in webkit.c
	switch (selector) {
		case 1: didFailProvisionalLoadWithError(arg0, arg1); break;
		case 2: didFinishLoadForFrame(arg0); break;
		case 3: didReceiveTitle(arg0, arg1); break;
		case 4: didStartProvisionalLoadForFrame(arg0); break;
		case 5: didFinishLoadingFromDataSource(arg0, arg1); break;
		case 6: didFailLoadingWithError(arg0, arg1, arg2); break;
		case 7: ret = identifierForInitialRequest(arg0, arg1); break;
		case 8: ret = willSendRequest(arg0, arg1, arg2, arg3); break;
		case 9: handleNotification(arg0); break;
		case 10: didCommitLoadForFrame(arg0); break;
		case 11: ret = createWebViewWithRequest(arg0); break;
		case 12: webViewShow(arg0); break;
		case 13: setFrame(arg0); break;
		case 14: webViewClose(); break;
		case 15: ret = contextMenuItemsForElement(arg0, arg1); break;
		case 16: setStatusBarVisible(arg0); break;
		case 17: setResizable(arg0); break;
		case 18: setToolbarsVisible(arg0); break;
		case 19: decidePolicyForMIMEType(arg0, arg1, arg2, arg3); break;
		case 20: decidePolicyForNavigationAction(arg0, arg1, arg2, arg3); break;
		case 21: decidePolicyForNewWindowAction(arg0, arg1, arg2, arg3); break;
		case 22: unableToImplementPolicyWithError(arg0, arg1); break;
	}
	return ret;
}

/**
 * Returns <code>true</code> if the receiver can navigate to the 
 * previous session history item, and <code>false</code> otherwise.
 *
 * @return the receiver's back command enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #back
 */
public boolean isBackEnabled() {
	checkWidget();
	int webView = WebKit.HIWebViewGetWebView(webViewHandle);
	return WebKit.objc_msgSend(webView, WebKit.S_canGoBack) != 0;
}

/**
 * Returns <code>true</code> if the receiver can navigate to the 
 * next session history item, and <code>false</code> otherwise.
 *
 * @return the receiver's forward command enabled state
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
 * </ul>
 * 
 * @see #forward
 */
public boolean isForwardEnabled() {
	checkWidget();
	int webView = WebKit.HIWebViewGetWebView(webViewHandle);
	return WebKit.objc_msgSend(webView, WebKit.S_canGoForward) != 0;
}

/**
 * Refresh the current page.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void refresh() {
	checkWidget();
	int webView = WebKit.HIWebViewGetWebView(webViewHandle);
	WebKit.objc_msgSend(webView, WebKit.S_reload, 0);
}

/**	 
 * Removes the listener.
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void removeCloseWindowListener(CloseWindowListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (closeWindowListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < closeWindowListeners.length; i++) {
		if (listener == closeWindowListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (closeWindowListeners.length == 1) {
		closeWindowListeners = new CloseWindowListener[0];
		return;
	}
	CloseWindowListener[] newCloseWindowListeners = new CloseWindowListener[closeWindowListeners.length - 1];
	System.arraycopy(closeWindowListeners, 0, newCloseWindowListeners, 0, index);
	System.arraycopy(closeWindowListeners, index + 1, newCloseWindowListeners, index, closeWindowListeners.length - index - 1);
	closeWindowListeners = newCloseWindowListeners;
}

/**	 
 * Removes the listener.
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void removeLocationListener(LocationListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (locationListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < locationListeners.length; i++) {
		if (listener == locationListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (locationListeners.length == 1) {
		locationListeners = new LocationListener[0];
		return;
	}
	LocationListener[] newLocationListeners = new LocationListener[locationListeners.length - 1];
	System.arraycopy(locationListeners, 0, newLocationListeners, 0, index);
	System.arraycopy(locationListeners, index + 1, newLocationListeners, index, locationListeners.length - index - 1);
	locationListeners = newLocationListeners;
}

/**	 
 * Removes the listener.
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void removeOpenWindowListener(OpenWindowListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (openWindowListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < openWindowListeners.length; i++) {
		if (listener == openWindowListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (openWindowListeners.length == 1) {
		openWindowListeners = new OpenWindowListener[0];
		return;
	}
	OpenWindowListener[] newOpenWindowListeners = new OpenWindowListener[openWindowListeners.length - 1];
	System.arraycopy(openWindowListeners, 0, newOpenWindowListeners, 0, index);
	System.arraycopy(openWindowListeners, index + 1, newOpenWindowListeners, index, openWindowListeners.length - index - 1);
	openWindowListeners = newOpenWindowListeners;
}

/**	 
 * Removes the listener.
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void removeProgressListener(ProgressListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (progressListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < progressListeners.length; i++) {
		if (listener == progressListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (progressListeners.length == 1) {
		progressListeners = new ProgressListener[0];
		return;
	}
	ProgressListener[] newProgressListeners = new ProgressListener[progressListeners.length - 1];
	System.arraycopy(progressListeners, 0, newProgressListeners, 0, index);
	System.arraycopy(progressListeners, index + 1, newProgressListeners, index, progressListeners.length - index - 1);
	progressListeners = newProgressListeners;
}

/**	 
 * Removes the listener.
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void removeStatusTextListener(StatusTextListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (statusTextListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < statusTextListeners.length; i++) {
		if (listener == statusTextListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (statusTextListeners.length == 1) {
		statusTextListeners = new StatusTextListener[0];
		return;
	}
	StatusTextListener[] newStatusTextListeners = new StatusTextListener[statusTextListeners.length - 1];
	System.arraycopy(statusTextListeners, 0, newStatusTextListeners, 0, index);
	System.arraycopy(statusTextListeners, index + 1, newStatusTextListeners, index, statusTextListeners.length - index - 1);
	statusTextListeners = newStatusTextListeners;
}

/**	 
 * Removes the listener.
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void removeTitleListener(TitleListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (titleListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < titleListeners.length; i++) {
		if (listener == titleListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (titleListeners.length == 1) {
		titleListeners = new TitleListener[0];
		return;
	}
	TitleListener[] newTitleListeners = new TitleListener[titleListeners.length - 1];
	System.arraycopy(titleListeners, 0, newTitleListeners, 0, index);
	System.arraycopy(titleListeners, index + 1, newTitleListeners, index, titleListeners.length - index - 1);
	titleListeners = newTitleListeners;
}

/**	 
 * Removes the listener.
 *
 * @param listener the listener
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void removeVisibilityWindowListener(VisibilityWindowListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (visibilityWindowListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < visibilityWindowListeners.length; i++) {
		if (listener == visibilityWindowListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (visibilityWindowListeners.length == 1) {
		visibilityWindowListeners = new VisibilityWindowListener[0];
		return;
	}
	VisibilityWindowListener[] newVisibilityWindowListeners = new VisibilityWindowListener[visibilityWindowListeners.length - 1];
	System.arraycopy(visibilityWindowListeners, 0, newVisibilityWindowListeners, 0, index);
	System.arraycopy(visibilityWindowListeners, index + 1, newVisibilityWindowListeners, index, visibilityWindowListeners.length - index - 1);
	visibilityWindowListeners = newVisibilityWindowListeners;
}

/**
 * Renders HTML.
 * 
 * @param html the HTML content to be rendered
 *
 * @return true if the operation was successful and false otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the html is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *  
 * @see #setUrl
 * 
 * @since 3.0
 */
public boolean setText(String html) {
	checkWidget();
	if (html == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	
	int length = html.length();
	char[] buffer = new char[length];
	html.getChars(0, length, buffer, 0);
	int string = OS.CFStringCreateWithCharacters(0, buffer, length);

	String baseURL = "about:blank"; //$NON-NLS-1$
	length = baseURL.length();
	buffer = new char[length];
	baseURL.getChars(0, length, buffer, 0);
	int URLString = OS.CFStringCreateWithCharacters(0, buffer, length);
	
	/*
	* Note.  URLWithString uses autorelease.  The resulting URL
	* does not need to be released.
	* URL = [NSURL URLWithString:(NSString *)URLString]
	*/	
	int URL = WebKit.objc_msgSend(WebKit.C_NSURL, WebKit.S_URLWithString, URLString);
	OS.CFRelease(URLString);

	int webView = WebKit.HIWebViewGetWebView(webViewHandle);
	
	//mainFrame = [webView mainFrame];
	int mainFrame = WebKit.objc_msgSend(webView, WebKit.S_mainFrame);
	
	//[mainFrame loadHTMLString:(NSString *) string baseURL:(NSURL *)URL];
	WebKit.objc_msgSend(mainFrame, WebKit.S_loadHTMLStringbaseURL, string, URL);
	OS.CFRelease(string);
	
	return true;
}

/**
 * Loads a URL.
 * 
 * @param url the URL to be loaded
 *
 * @return true if the operation was successful and false otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the url is null</li>
 * </ul>
 * 
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *  
 * @see #getUrl
 * 
 * @since 3.0
 */
public boolean setUrl(String url) {
	checkWidget();
	if (url == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	
	StringBuffer buffer = new StringBuffer();
	for (int i = 0; i < url.length(); i++) {
		char c = url.charAt(i);
		if (c == ' ') buffer.append("%20");
		else buffer.append(c);
	}
	
	int length = buffer.length();
	char[] chars = new char[length];
	buffer.getChars(0, length, chars, 0);
	int sHandle = OS.CFStringCreateWithCharacters(0, chars, length);

	/*
	* Note.  URLWithString uses autorelease.  The resulting URL
	* does not need to be released.
	* inURL = [NSURL URLWithString:(NSString *)sHandle]
	*/	
	int inURL= WebKit.objc_msgSend(WebKit.C_NSURL, WebKit.S_URLWithString, sHandle);
	OS.CFRelease(sHandle);
		
	//request = [NSURLRequest requestWithURL:(NSURL*)inURL];
	int request= WebKit.objc_msgSend(WebKit.C_NSURLRequest, WebKit.S_requestWithURL, inURL);
	
	int webView = WebKit.HIWebViewGetWebView(webViewHandle);
	
	//mainFrame = [webView mainFrame];
	int mainFrame= WebKit.objc_msgSend(webView, WebKit.S_mainFrame);

	//[mainFrame loadRequest:request];
	WebKit.objc_msgSend(mainFrame, WebKit.S_loadRequest, request);

	return true;
}

/**
 * Stop any loading and rendering activity.
 *
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void stop() {
	checkWidget();
	int webView = WebKit.HIWebViewGetWebView(webViewHandle);
	WebKit.objc_msgSend(webView, WebKit.S_stopLoading, 0);
}

/* WebFrameLoadDelegate */
  
void didFailProvisionalLoadWithError(int error, int frame) {
	int webView = WebKit.HIWebViewGetWebView(webViewHandle);
	if (frame == WebKit.objc_msgSend(webView, WebKit.S_mainFrame)) {
		/*
		* Feature on Safari.  The identifier is used here as a marker for the events 
		* related to the top frame and the URL changes related to that top frame as 
		* they should appear on the location bar of a browser.  It is expected to reset
		* the identifier to 0 when the event didFinishLoadingFromDataSource related to 
		* the identifierForInitialRequest event is received.  Howeever, Safari fires
		* the didFinishLoadingFromDataSource event before the entire content of the
		* top frame is loaded.  It is possible to receive multiple willSendRequest 
		* events in this interval, causing the Browser widget to send unwanted
		* Location.changing events.  For this reason, the identifier is reset to 0
		* when the top frame has either finished loading (didFinishLoadForFrame
		* event) or failed (didFailProvisionalLoadWithError).
		*/
		identifier = 0;
	}
}

void didFinishLoadForFrame(int frame) {
	int webView = WebKit.HIWebViewGetWebView(webViewHandle);
	if (frame == WebKit.objc_msgSend(webView, WebKit.S_mainFrame)) {
		final Display display= getDisplay();
		final ProgressEvent progress = new ProgressEvent(this);
		progress.display = getDisplay();
		progress.widget = this;
		progress.current = MAX_PROGRESS;
		progress.total = MAX_PROGRESS;
		for (int i = 0; i < progressListeners.length; i++) {
			final ProgressListener listener = progressListeners[i];
			/*
			* Note on WebKit.  Running the event loop from a Browser
			* delegate callback breaks the WebKit (stop loading or
			* crash).  The widget ProgressBar currently touches the
			* event loop every time the method setSelection is called.  
			* The workaround is to invoke Display.asyncexec so that
			* the Browser does not crash when the user updates the 
			* selection of the ProgressBar.
			*/
			display.asyncExec(
				new Runnable() {
					public void run() {
						if (!display.isDisposed() && !isDisposed())
							listener.completed(progress);
					}
				}
			);
		}
		/*
		* Feature on Safari.  The identifier is used here as a marker for the events 
		* related to the top frame and the URL changes related to that top frame as 
		* they should appear on the location bar of a browser.  It is expected to reset
		* the identifier to 0 when the event didFinishLoadingFromDataSource related to 
		* the identifierForInitialRequest event is received.  Howeever, Safari fires
		* the didFinishLoadingFromDataSource event before the entire content of the
		* top frame is loaded.  It is possible to receive multiple willSendRequest 
		* events in this interval, causing the Browser widget to send unwanted
		* Location.changing events.  For this reason, the identifier is reset to 0
		* when the top frame has either finished loading (didFinishLoadForFrame
		* event) or failed (didFailProvisionalLoadWithError).
		*/
		identifier = 0;
	}
}

void didReceiveTitle(int title, int frame) {
	int webView = WebKit.HIWebViewGetWebView(webViewHandle);
	if (frame == WebKit.objc_msgSend(webView, WebKit.S_mainFrame)) {
		int length = OS.CFStringGetLength(title);
		char[] buffer = new char[length];
		CFRange range = new CFRange();
		range.length = length;
		OS.CFStringGetCharacters(title, range, buffer);
		String newTitle = new String(buffer);
		TitleEvent newEvent = new TitleEvent(Browser.this);
		newEvent.display = getDisplay();
		newEvent.widget = this;
		newEvent.title = newTitle;
		for (int i = 0; i < titleListeners.length; i++)
			titleListeners[i].changed(newEvent);
	}
}

void didStartProvisionalLoadForFrame(int frame) {
	/* 
	* This code is intentionally commented.  WebFrameLoadDelegate:didStartProvisionalLoadForFrame is
	* called before WebResourceLoadDelegate:willSendRequest and
	* WebFrameLoadDelegate:didCommitLoadForFrame.  The resource count is reset when didCommitLoadForFrame
	* is received for the top frame.
	*/
//	int webView = WebKit.HIWebViewGetWebView(webViewHandle);
//	if (frame == WebKit.objc_msgSend(webView, WebKit.S_mainFrame)) {
//		/* reset resource status variables */
//		resourceCount= 0;
//	}
}

void didCommitLoadForFrame(int frame) {
	int webView = WebKit.HIWebViewGetWebView(webViewHandle);
	//id url= [[[[frame provisionalDataSource] request] URL] absoluteString];
	int dataSource = WebKit.objc_msgSend(frame, WebKit.S_dataSource);
	int request = WebKit.objc_msgSend(dataSource, WebKit.S_request);
	int url = WebKit.objc_msgSend(request, WebKit.S_URL);
	int s = WebKit.objc_msgSend(url, WebKit.S_absoluteString);	
	int length = OS.CFStringGetLength(s);
	if (length == 0) return;
	char[] buffer = new char[length];
	CFRange range = new CFRange();
	range.length = length;
	OS.CFStringGetCharacters(s, range, buffer);
	String url2 = new String(buffer);
	final Display display = getDisplay();

	boolean top = frame == WebKit.objc_msgSend(webView, WebKit.S_mainFrame);
	if (top) {
		/* reset resource status variables */
		resourceCount = 0;		
		this.url = url2;
		
		final ProgressEvent progress = new ProgressEvent(this);
		progress.display = display;
		progress.widget = this;
		progress.current = 1;
		progress.total = MAX_PROGRESS;
		for (int i = 0; i < progressListeners.length; i++) {
			final ProgressListener listener = progressListeners[i];
			/*
			* Note on WebKit.  Running the event loop from a Browser
			* delegate callback breaks the WebKit (stop loading or
			* crash).  The widget ProgressBar currently touches the
			* event loop every time the method setSelection is called.  
			* The workaround is to invoke Display.asyncexec so that
			* the Browser does not crash when the user updates the 
			* selection of the ProgressBar.
			*/
			display.asyncExec(
				new Runnable() {
					public void run() {
						if (!display.isDisposed() && !isDisposed())
							listener.changed(progress);
					}
				}
			);
		}
		
		StatusTextEvent statusText = new StatusTextEvent(this);
		statusText.display = display;
		statusText.widget = this;
		statusText.text = url2;
		for (int i = 0; i < statusTextListeners.length; i++)
			statusTextListeners[i].changed(statusText);
	}
	LocationEvent location = new LocationEvent(Browser.this);
	location.display = display;
	location.widget = this;
	location.location = url2;
	location.top = top;
	for (int i = 0; i < locationListeners.length; i++)
		locationListeners[i].changed(location);
}

/* WebResourceLoadDelegate */

void didFinishLoadingFromDataSource(int identifier, int dataSource) {
	/*
	* Feature on Safari.  The identifier is used here as a marker for the events 
	* related to the top frame and the URL changes related to that top frame as 
	* they should appear on the location bar of a browser.  It is expected to reset
	* the identifier to 0 when the event didFinishLoadingFromDataSource related to 
	* the identifierForInitialRequest event is received.  Howeever, Safari fires
	* the didFinishLoadingFromDataSource event before the entire content of the
	* top frame is loaded.  It is possible to receive multiple willSendRequest 
	* events in this interval, causing the Browser widget to send unwanted
	* Location.changing events.  For this reason, the identifier is reset to 0
	* when the top frame has either finished loading (didFinishLoadForFrame
	* event) or failed (didFailProvisionalLoadWithError).
	*/
	// this code is intentionally commented
	//if (this.identifier == identifier) this.identifier = 0;
}

void didFailLoadingWithError(int identifier, int error, int dataSource) {
	/*
	* Feature on Safari.  The identifier is used here as a marker for the events 
	* related to the top frame and the URL changes related to that top frame as 
	* they should appear on the location bar of a browser.  It is expected to reset
	* the identifier to 0 when the event didFinishLoadingFromDataSource related to 
	* the identifierForInitialRequest event is received.  Howeever, Safari fires
	* the didFinishLoadingFromDataSource event before the entire content of the
	* top frame is loaded.  It is possible to receive multiple willSendRequest 
	* events in this interval, causing the Browser widget to send unwanted
	* Location.changing events.  For this reason, the identifier is reset to 0
	* when the top frame has either finished loading (didFinishLoadForFrame
	* event) or failed (didFailProvisionalLoadWithError).
	*/
	// this code is intentionally commented
	//if (this.identifier == identifier) this.identifier = 0;
}

int identifierForInitialRequest(int request, int dataSource) {
	final Display display = getDisplay();
	final ProgressEvent progress = new ProgressEvent(this);
	progress.display = display;
	progress.widget = this;
	progress.current = resourceCount;
	progress.total = Math.max(resourceCount, MAX_PROGRESS);
	for (int i = 0; i < progressListeners.length; i++) {
		final ProgressListener listener = progressListeners[i];
		/*
		* Note on WebKit.  Running the event loop from a Browser
		* delegate callback breaks the WebKit (stop loading or
		* crash).  The widget ProgressBar currently touches the
		* event loop every time the method setSelection is called.  
		* The workaround is to invoke Display.asyncexec so that
		* the Browser does not crash when the user updates the 
		* selection of the ProgressBar.
		*/
		display.asyncExec(
			new Runnable() {
				public void run() {
					if (!display.isDisposed() && !isDisposed())
						listener.changed(progress);
				}
			}
		);
	}

	/*
	* Note.  numberWithInt uses autorelease.  The resulting object
	* does not need to be released.
	* identifier = [NSNumber numberWithInt: resourceCount++]
	*/	
	int identifier = WebKit.objc_msgSend(WebKit.C_NSNumber, WebKit.S_numberWithInt, resourceCount++);
		
	if (this.identifier == 0) {
		int webView = WebKit.HIWebViewGetWebView(webViewHandle);
		int frame = WebKit.objc_msgSend(dataSource, WebKit.S_webFrame);
		if (frame == WebKit.objc_msgSend(webView, WebKit.S_mainFrame)) this.identifier = identifier;
	}
	return identifier;
		
}

int willSendRequest(int identifier, int request, int redirectResponse, int dataSource) {
	return request;
}

/* handleNotification */

void handleNotification(int notification) {	
}

/* UIDelegate */
int createWebViewWithRequest(int request) {
	WindowEvent newEvent = new WindowEvent(Browser.this);
	newEvent.display = getDisplay();
	newEvent.widget = this;
	if (openWindowListeners != null) {
		for (int i = 0; i < openWindowListeners.length; i++)
			openWindowListeners[i].open(newEvent);
	}
	int webView = 0;
	Browser browser = newEvent.browser;
	if (browser != null && !browser.isDisposed()) {
		webView = WebKit.HIWebViewGetWebView(browser.webViewHandle);
		
		if (request != 0) {
			//mainFrame = [webView mainFrame];
			int mainFrame= WebKit.objc_msgSend(webView, WebKit.S_mainFrame);

			//[mainFrame loadRequest:request];
			WebKit.objc_msgSend(mainFrame, WebKit.S_loadRequest, request);
		}
	}
	return webView;
}

void webViewShow(int sender) {
	/*
	* Feature on WebKit.  The Safari WebKit expects the application
	* to create a new Window using the Objective C Cocoa API in response
	* to UIDelegate.createWebViewWithRequest. The application is then
	* expected to use Objective C Cocoa API to make this window visible
	* when receiving the UIDelegate.webViewShow message.  For some reason,
	* a window created with the Carbon API hosting the new browser instance
	* does not redraw until it has been resized.  The fix is to increase the
	* size of the Shell and restore it to its initial size.
	*/
	Shell parent = getShell();
	Point pt = parent.getSize();
	parent.setSize(pt.x+1, pt.y);
	parent.setSize(pt.x, pt.y);
	WindowEvent newEvent = new WindowEvent(this);
	newEvent.display = getDisplay();
	newEvent.widget = this;
	if (location != null) newEvent.location = location;
	if (size != null) newEvent.size = size;
	/*
	* Feature in Safari.  Safari's tool bar contains
	* the address bar.  The address bar is displayed
	* if the tool bar is displayed. There is no separate
	* notification for the address bar.
	* Feature in Safari.  The menu bar is always
	* displayed. There is no notification to hide
	* the menu bar.
	*/
	newEvent.addressBar = toolBar;
	newEvent.menuBar = true;
	newEvent.statusBar = statusBar;
	newEvent.toolBar = toolBar;
	for (int i = 0; i < visibilityWindowListeners.length; i++)
		visibilityWindowListeners[i].show(newEvent);
	location = null;
	size = null;
}

void setFrame(int frame) {
	float[] dest = new float[4];
	OS.memcpy(dest, frame, 16);
	/* convert to SWT system coordinates */
	Rectangle bounds = getDisplay().getBounds();
	location = new Point((int)dest[0], bounds.height - (int)dest[1] - (int)dest[3]);
	size = new Point((int)dest[2], (int)dest[3]);
}

void webViewClose() {
	Shell parent = getShell();
	WindowEvent newEvent = new WindowEvent(this);
	newEvent.display = getDisplay();
	newEvent.widget = this;
	for (int i = 0; i < closeWindowListeners.length; i++)
		closeWindowListeners[i].close(newEvent);	
	dispose();
	if (parent.isDisposed()) return;
	/*
	* Feature on WebKit.  The Safari WebKit expects the application
	* to create a new Window using the Objective C Cocoa API in response
	* to UIDelegate.createWebViewWithRequest. The application is then
	* expected to use Objective C Cocoa API to make this window visible
	* when receiving the UIDelegate.webViewShow message.  For some reason,
	* a window created with the Carbon API hosting the new browser instance
	* does not redraw until it has been resized.  The fix is to increase the
	* size of the Shell and restore it to its initial size.
	*/
	Point pt = parent.getSize();
	parent.setSize(pt.x+1, pt.y);
	parent.setSize(pt.x, pt.y);
}

int contextMenuItemsForElement(int element, int defaultMenuItems) {
	org.eclipse.swt.internal.carbon.Point pt = new org.eclipse.swt.internal.carbon.Point();
	OS.GetGlobalMouse(pt);
	Event event = new Event();
	event.x = pt.h;
	event.y = pt.v;
	notifyListeners(SWT.MenuDetect, event);
	Menu menu = getMenu();
	if (!event.doit) return 0;
	if (menu != null && !menu.isDisposed()) {
		if (event.x != pt.h || event.y != pt.v) {
			menu.setLocation(event.x, event.y);
		}
		menu.setVisible(true);
		return 0;
	}
	return defaultMenuItems;
}

void setStatusBarVisible(int visible) {
	/* Note.  Webkit only emits the notification when the status bar should be hidden. */
	statusBar = visible != 0;
}

void setResizable(int visible) {
}

void setToolbarsVisible(int visible) {
	/* Note.  Webkit only emits the notification when the tool bar should be hidden. */
	toolBar = visible != 0;
}

/* PolicyDelegate */

void decidePolicyForMIMEType(int type, int request, int frame, int listener) {
	WebKit.objc_msgSend(listener, WebKit.S_use);
}

void decidePolicyForNavigationAction(int actionInformation, int request, int frame, int listener) {
	int url = WebKit.objc_msgSend(request, WebKit.S_URL);
	int s = WebKit.objc_msgSend(url, WebKit.S_absoluteString);
	int length = OS.CFStringGetLength(s);
	char[] buffer = new char[length];
	CFRange range = new CFRange();
	range.length = length;
	OS.CFStringGetCharacters(s, range, buffer);
	String url2 = new String(buffer);
	
	LocationEvent newEvent = new LocationEvent(this);
	newEvent.display = getDisplay();
	newEvent.widget = this;
	newEvent.location = url2;
	newEvent.doit = true;
	if (locationListeners != null) {
		for (int i = 0; i < locationListeners.length; i++) 
			locationListeners[i].changing(newEvent);
	}

	WebKit.objc_msgSend(listener, newEvent.doit ? WebKit.S_use : WebKit.S_ignore);
}

void decidePolicyForNewWindowAction(int actionInformation, int request, int frameName, int listener) {
}

void unableToImplementPolicyWithError(int error, int frame) {
}

}
