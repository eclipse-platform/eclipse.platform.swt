/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Chris McKillop (QNX Software Systems) - initial implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import java.io.File;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
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
 * </p><p>
 * NOTE: The API in the browser package is NOT finalized.
 * Use at your own risk, because it will most certainly change.
 * The only reason this API is being released at this time is so that 
 * other teams can try it out.
 * </p>
 * 
 * @since 3.0
 */
public class Browser extends Composite {	
	int webHandle;
	String url = ""; //$NON-NLS-1$
	String text = ""; //$NON-NLS-1$
	int currentProgress;
	int totalProgress = 25;
	
	/* External Listener management */
	LocationListener[] locationListeners = new LocationListener[0];
	OpenWindowListener[] openWindowListeners = new OpenWindowListener[0];
	ProgressListener[] progressListeners = new ProgressListener[0];
	StatusTextListener[] statusTextListeners = new StatusTextListener[0];
	VisibilityListener[] visibilityListeners = new VisibilityListener[0];
	
	/* Package Name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.browser."; //$NON-NLS-1$
	static Callback callback;

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

	/* use Photon's built-in anchoring for resizing */
	int[] args = {
			OS.Pt_ARG_ANCHOR_FLAGS,
			OS.Pt_TOP_ANCHORED_TOP | OS.Pt_BOTTOM_ANCHORED_BOTTOM | OS.Pt_LEFT_ANCHORED_LEFT | OS.Pt_RIGHT_ANCHORED_RIGHT,
			OS.Pt_TOP_ANCHORED_TOP | OS.Pt_BOTTOM_ANCHORED_BOTTOM | OS.Pt_LEFT_ANCHORED_LEFT | OS.Pt_RIGHT_ANCHORED_RIGHT,
			OS.Pt_ARG_FILL_COLOR,
			0xFFFFFF,
			0 };
	webHandle = OS.PtCreateWidget(OS.PtWebClient(), handle, args.length / 3, args);
	if (webHandle == 0) {
		dispose();
		SWT.error (SWT.ERROR_NO_HANDLES);
	}

	/* configure the widget with a specific server */
	File netfront = new File("/usr/photon/bin/netfront"); //$NON-NLS-1$
	String name, server;
	if (netfront.exists() || (OS.QNX_MAJOR >= 6 && OS.QNX_MINOR >= 3 && OS.QNX_MICRO >= 0)) {
		name = "NetfrontServer"; //$NON-NLS-1$
		server = "netfront"; //$NON-NLS-1$
	} else {
		name = "VoyagerServer-2"; //$NON-NLS-1$
		server = "vserver"; //$NON-NLS-1$
	}
	/* set client name */
	byte[] nameBuffer = Converter.wcsToMbcs(null, name, true);
	int namePtr = OS.malloc(nameBuffer.length);
	OS.memmove(namePtr, nameBuffer, nameBuffer.length);
	OS.PtSetResource(webHandle, OS.Pt_ARG_CLIENT_NAME, namePtr, 0);
	OS.free(namePtr);
	
	/* select server */
	byte[] serverBuffer = Converter.wcsToMbcs(null, server, true);
	int serverPtr = OS.malloc(serverBuffer.length);
	OS.memmove(serverPtr, serverBuffer, serverBuffer.length);
	OS.PtSetResource(webHandle, OS.Pt_ARG_WEB_SERVER, serverPtr, 0);
	OS.free(serverPtr);
	
	if (callback == null) callback = new Callback(this.getClass(), "webProc", 3, false); //$NON-NLS-1$
	int webProc = callback.getAddress();
	OS.PtAddCallback(webHandle,OS.Pt_CB_WEB_COMPLETE, webProc, OS.Pt_CB_WEB_COMPLETE);
	OS.PtAddCallback(webHandle,OS.Pt_CB_WEB_DATA_REQ, webProc, OS.Pt_CB_WEB_DATA_REQ);
	OS.PtAddCallback(webHandle,OS.Pt_CB_WEB_START, webProc, OS.Pt_CB_WEB_START);
	OS.PtAddCallback(webHandle,OS.Pt_CB_WEB_STATUS, webProc, OS.Pt_CB_WEB_STATUS);
	OS.PtAddCallback(webHandle,OS.Pt_CB_WEB_URL, webProc, OS.Pt_CB_WEB_URL);
	
	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.Dispose: onDispose(); break;
				case SWT.FocusIn: onFocusGained(event);	break;
			}
		}
	};	
	int[] folderEvents = new int[]{
		SWT.Dispose,
		SWT.FocusIn,  
	};
	for (int i = 0; i < folderEvents.length; i++) {
		addListener(folderEvents[i], listener);
	}
	OS.PtRealizeWidget(webHandle);
}

static int webProc(int handle, int data, int info) {
	Display display = Display.getCurrent();
	int parent = OS.PtWidgetParent (handle);
	Widget widget = display.findWidget(parent);
	if (widget != null && widget instanceof Browser) {
		Browser browser = (Browser)widget;
		return browser.webProc(data, info);
	}
	return OS.Pt_CONTINUE;		
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
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addLocationListener(LocationListener listener) {
	checkWidget();
	if (listener == null)
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
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
 * @exception SWTError <ul>
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
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addProgressListener(ProgressListener listener) {
	checkWidget();
	if (listener == null)
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
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
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addStatusTextListener(StatusTextListener listener) {
	checkWidget();
	if (listener == null)
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
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
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void addVisibilityListener(VisibilityListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	VisibilityListener[] newVisibilityListeners = new VisibilityListener[visibilityListeners.length + 1];
	System.arraycopy(visibilityListeners, 0, newVisibilityListeners, 0, visibilityListeners.length);
	visibilityListeners = newVisibilityListeners;
	visibilityListeners[visibilityListeners.length - 1] = listener;
}

/**
 * Navigate to the previous session history item.
 *
 * @return <code>true</code> if the operation was successful and <code>false</code> otherwise
 *
 * @exception SWTError <ul>
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
	int ptr = OS.malloc(4);
	int[] args = new int[]{OS.Pt_ARG_WEB_NAVIGATE_PAGE, ptr, 0};
	OS.PtGetResources(webHandle, args.length / 3, args);
	int[] result = new int[1];
	OS.memmove(result, ptr, 4);
	OS.memmove(result, result[0], 4);
	OS.free(ptr);
	if ((result[0] & (1 << OS.Pt_WEB_DIRECTION_BACK)) == 0) return false;
	OS.PtSetResource(webHandle, OS.Pt_ARG_WEB_NAVIGATE_PAGE, OS.Pt_WEB_DIRECTION_BACK, 0);
	return true;
}

int webProc(int data, int info) {
	switch (data) {
		case OS.Pt_CB_WEB_COMPLETE:	return Pt_CB_WEB_COMPLETE(info);
		case OS.Pt_CB_WEB_START:	return Pt_CB_WEB_START(info);
		case OS.Pt_CB_WEB_STATUS:	return Pt_CB_WEB_STATUS(info);
		case OS.Pt_CB_WEB_URL:		return Pt_CB_WEB_URL(info);
		case OS.Pt_CB_WEB_DATA_REQ: return Pt_CB_WEB_DATA_REQ(info);
	}
	return OS.Pt_CONTINUE;
}

protected void checkSubclass() {
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
 * @exception SWTError <ul>
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
	int ptr = OS.malloc(4);
	int[] args = new int[]{OS.Pt_ARG_WEB_NAVIGATE_PAGE, ptr, 0};
	OS.PtGetResources(webHandle, args.length / 3, args);
	int[] result = new int[1];
	OS.memmove(result, ptr, 4);
	OS.memmove(result, result[0], 4);
	OS.free(ptr);
	if ((result[0] & (1 << OS.Pt_WEB_DIRECTION_FWD)) == 0) return false;
	OS.PtSetResource(webHandle, OS.Pt_ARG_WEB_NAVIGATE_PAGE, OS.Pt_WEB_DIRECTION_FWD, 0);
	return true;
}

/**
 * Returns the current URL.
 *
 * @return the current URL or an empty <code>String</code> if there is no current URL
 *
 * @exception SWTError <ul>
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

void onDispose() {
	OS.PtDestroyWidget(webHandle);
	webHandle = 0;
}

void onFocusGained(Event e) {
	OS.PtContainerGiveFocus(webHandle, null);
}

int Pt_CB_WEB_COMPLETE(int info) {
	Display display = getDisplay();
	LocationEvent event = new LocationEvent(this);
	event.display = display;
	event.widget = this;
	event.location = url;
	for (int i = 0; i < locationListeners.length; i++)
		locationListeners[i].changed(event);
	ProgressEvent progress = new ProgressEvent(this);
	progress.display = display;
	progress.widget = this;
	progress.current = totalProgress;
	progress.total = totalProgress;
	for (int i = 0; i < progressListeners.length; i++)
		progressListeners[i].completed(progress);
	StatusTextEvent statusevent = new StatusTextEvent(this);
	statusevent.display = display;
	statusevent.widget = this;
	statusevent.text = ""; //$NON-NLS-1$
	for (int i = 0; i < statusTextListeners.length; i++)
		statusTextListeners[i].changed(statusevent);
	return OS.Pt_CONTINUE;
}

int Pt_CB_WEB_DATA_REQ(int info) {
	PtCallbackInfo_t cbinfo_t = new PtCallbackInfo_t();
	OS.memmove(cbinfo_t, info, PtCallbackInfo_t.sizeof);
	PtWebDataReqCallback_t dataReq = new PtWebDataReqCallback_t();
	OS.memmove(dataReq, cbinfo_t.cbdata, PtWebDataReqCallback_t.sizeof);
	PtWebClientData_t clientData = new PtWebClientData_t();
	clientData.type = dataReq.type;
	clientData.data = 0;
	String data = null;
	switch (clientData.type) {
		case OS.Pt_WEB_DATA_HEADER:
			StringBuffer sb = new StringBuffer("Content-Type: text/html\n"); //$NON-NLS-1$
			sb.append("Content-Length: "); //$NON-NLS-1$
			sb.append(text.length());
			sb.append("\n"); //$NON-NLS-1$
			data = sb.toString();
			break;
		case OS.Pt_WEB_DATA_BODY:
			data = text;
			break;
		case OS.Pt_WEB_DATA_CLOSE:
			text = ""; //$NON-NLS-1$
			break;
	}
	if (data != null ) {
		byte[] buffer = Converter.wcsToMbcs(null, data, true);
		clientData.data = OS.malloc(buffer.length);
		OS.memmove(clientData.data, buffer, buffer.length);
		clientData.length = buffer.length - 1;
	}
	System.arraycopy(dataReq.url, 0, clientData.url, 0, dataReq.url.length);
	int ptr = OS.malloc(PtWebClientData_t.sizeof);
	OS.memmove(ptr, clientData, PtWebClientData_t.sizeof);
	OS.PtSetResource(webHandle, OS.Pt_ARG_WEB_DATA, clientData.data, ptr);
	OS.free(ptr);
	if( clientData.data != 0 ) OS.free(clientData.data);
	return OS.Pt_CONTINUE;
}

int Pt_CB_WEB_START(int info) {
	LocationEvent event = new LocationEvent(this);
	event.display = getDisplay();
	event.widget = this;
	event.location = url;
	for (int i = 0; i < locationListeners.length; i++)
		locationListeners[i].changing(event);
	if (event.cancel) {
		stop();
	} else {
		currentProgress = 1;
		ProgressEvent progress = new ProgressEvent(this);
		progress.display = getDisplay();
		progress.widget = this;
		progress.current = currentProgress;
		progress.total = totalProgress;
		for (int i = 0; i < progressListeners.length; i++)
			progressListeners[i].changed(progress);
	}
	return OS.Pt_CONTINUE;
}

int Pt_CB_WEB_STATUS(int info) {
	PtCallbackInfo_t cbinfo_t = new PtCallbackInfo_t();
	PtWebStatusCallback_t webstatus = new PtWebStatusCallback_t();
	OS.memmove(cbinfo_t, info, PtCallbackInfo_t.sizeof);
	OS.memmove(webstatus, cbinfo_t.cbdata, PtWebStatusCallback_t.sizeof);
	switch (webstatus.type) {
		case OS.Pt_WEB_STATUS_MOUSE :
		case OS.Pt_WEB_STATUS_PROGRESS :
			StatusTextEvent statusevent = new StatusTextEvent(this);
			statusevent.display = getDisplay();
			statusevent.widget = this;
			statusevent.text = new String(webstatus.desc, 0, OS.strlen(cbinfo_t.cbdata));
			for (int i = 0; i < statusTextListeners.length; i++)
				statusTextListeners[i].changed(statusevent);
			if (webstatus.type == OS.Pt_WEB_STATUS_PROGRESS) {
				currentProgress++;
				if (currentProgress >= totalProgress) currentProgress = 1;
				ProgressEvent progress = new ProgressEvent(this);
				progress.display = getDisplay();
				progress.widget = this;
				progress.current = currentProgress;
				progress.total = totalProgress;
				for (int i = 0; i < progressListeners.length; i++)
					progressListeners[i].changed(progress);
			}
			break;
	}
	return OS.Pt_CONTINUE;
}

int Pt_CB_WEB_URL(int info) {
	PtCallbackInfo_t cbinfo_t = new PtCallbackInfo_t();
	OS.memmove(cbinfo_t, info, PtCallbackInfo_t.sizeof);
	byte[] buffer = new byte[OS.strlen(cbinfo_t.cbdata) + 1];
	OS.memmove(buffer, cbinfo_t.cbdata, buffer.length);
	url = new String(Converter.mbcsToWcs(null, buffer));
	return OS.Pt_CONTINUE;
}

/**
 * Refresh the current page.
 *
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void refresh() {
	checkWidget();
	OS.PtSetResource(webHandle, OS.Pt_ARG_WEB_RELOAD, 1, 0);
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
 * @exception SWTError <ul>
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
		if (listener == locationListeners[i]) {
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
 * @exception SWTError <ul>
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
 * @exception SWTError <ul>
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
		if (listener == progressListeners[i]) {
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
 * @exception SWTError <ul>
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
		if (listener == statusTextListeners[i]) {
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
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 * 
 * @since 3.0
 */
public void removeVisibilityListener(VisibilityListener listener) {
	checkWidget();
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (visibilityListeners.length == 0) return;
	int index = -1;
	for (int i = 0; i < visibilityListeners.length; i++) {
		if (listener == visibilityListeners[i]){
			index = i;
			break;
		}
	}
	if (index == -1) return;
	if (visibilityListeners.length == 1) {
		visibilityListeners = new VisibilityListener[0];
		return;
	}
	VisibilityListener[] newVisibilityListeners = new VisibilityListener[visibilityListeners.length - 1];
	System.arraycopy(visibilityListeners, 0, newVisibilityListeners, 0, index);
	System.arraycopy(visibilityListeners, index + 1, newVisibilityListeners, index, visibilityListeners.length - index - 1);
	visibilityListeners = newVisibilityListeners;
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
 * @exception SWTError <ul>
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
	text = html;
	byte[] buffer = Converter.wcsToMbcs(null, "client:", true); //$NON-NLS-1$
	int ptr = OS.malloc(buffer.length);
	OS.memmove(ptr, buffer, buffer.length);
	OS.PtSetResource(webHandle, OS.Pt_ARG_WEB_GET_URL, ptr, OS.Pt_WEB_ACTION_DISPLAY);
	OS.free(ptr);
	return true;
}

/**
 * Loads a URL.
 * 
 * @param url the URL to be loaded
 *
 * @return true if the operation was successfull and false otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the url is null</li>
 * </ul>
 * 
 * @exception SWTError <ul>
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
	byte[] buffer = Converter.wcsToMbcs(null, url, true);
	int ptr = OS.malloc(buffer.length);
	OS.memmove(ptr, buffer, buffer.length);
	OS.PtSetResource(webHandle, OS.Pt_ARG_WEB_GET_URL, ptr, OS.Pt_WEB_ACTION_DISPLAY);
	OS.free(ptr);
	return true;
}

/**
 * Stop any loading and rendering activity.
 *
 * @exception SWTError <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *    <li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * </ul>
 *
 * @since 3.0
 */
public void stop() {
	checkWidget();
	OS.PtSetResource(webHandle, OS.Pt_ARG_WEB_STOP, 1, 0);
}
}
