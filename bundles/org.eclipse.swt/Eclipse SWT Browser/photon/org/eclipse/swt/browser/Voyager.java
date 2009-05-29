/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Chris McKillop (QNX Software Systems) - initial implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import java.io.File;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.widgets.*;

class Voyager extends WebBrowser {	
	int webHandle;
	String url = ""; //$NON-NLS-1$
	String text = ""; //$NON-NLS-1$
	int textOffset;
	int currentProgress;
	int totalProgress = 25;
	/* browser to redirect content to */
	Browser redirectBrowser;
	static int instanceCount = 0;
	
	/* Package Name */
	static Callback callback;

public void create(Composite parent, int style) {
	/* use Photon's built-in anchoring for resizing */
	int[] args = {
			OS.Pt_ARG_ANCHOR_FLAGS,
			OS.Pt_TOP_ANCHORED_TOP | OS.Pt_BOTTOM_ANCHORED_BOTTOM | OS.Pt_LEFT_ANCHORED_LEFT | OS.Pt_RIGHT_ANCHORED_RIGHT,
			OS.Pt_TOP_ANCHORED_TOP | OS.Pt_BOTTOM_ANCHORED_BOTTOM | OS.Pt_LEFT_ANCHORED_LEFT | OS.Pt_RIGHT_ANCHORED_RIGHT,
			OS.Pt_ARG_FILL_COLOR,
			0xFFFFFF,
			0 };
	webHandle = OS.PtCreateWidget(OS.PtWebClient(), browser.handle, args.length / 3, args);
	if (webHandle == 0) {
		browser.dispose();
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
	
	/**
	 * Feature in Photon PtWebClient.  If you give a server name
	 * when the widget is created it will attempt to start a new server
	 * rather then attaching a new window context to the existing server.
	 * If you don't connect to the existing one then javascript window
	 * creation will fail.
	 */
	if (instanceCount == 0) {
		/* select server */
		byte[] serverBuffer = Converter.wcsToMbcs(null, server, true);
		int serverPtr = OS.malloc(serverBuffer.length);
		OS.memmove(serverPtr, serverBuffer, serverBuffer.length);
		OS.PtSetResource(webHandle, OS.Pt_ARG_WEB_SERVER, serverPtr, 0);
		OS.free(serverPtr);
	} 
	instanceCount++;
	
	if (callback == null) callback = new Callback(this.getClass(), "webProc", 3, false); //$NON-NLS-1$
	int webProc = callback.getAddress();
	if (webProc == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	OS.PtAddCallback(webHandle,OS.Pt_CB_WEB_CLOSE_WINDOW, webProc, OS.Pt_CB_WEB_CLOSE_WINDOW);
	OS.PtAddCallback(webHandle,OS.Pt_CB_WEB_COMPLETE, webProc, OS.Pt_CB_WEB_COMPLETE);
	OS.PtAddCallback(webHandle,OS.Pt_CB_WEB_DATA_REQ, webProc, OS.Pt_CB_WEB_DATA_REQ);
	OS.PtAddCallback(webHandle,OS.Pt_CB_WEB_METADATA, webProc, OS.Pt_CB_WEB_METADATA);	
	OS.PtAddCallback(webHandle,OS.Pt_CB_WEB_NEW_WINDOW, webProc, OS.Pt_CB_WEB_NEW_WINDOW);
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
		browser.addListener(folderEvents[i], listener);
	}
	OS.PtRealizeWidget(webHandle);
}

static int webProc(int handle, int data, int info) {
	Display display = Display.getCurrent();
	int parent = OS.PtWidgetParent (handle);
	Widget widget = display.findWidget(parent);
	if (widget != null && widget instanceof Browser) {
		Browser browser = (Browser)widget;
		return ((Voyager)browser.webBrowser).webProc(data, info);
	}
	return OS.Pt_CONTINUE;		
}

public boolean back() {
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
		case OS.Pt_CB_WEB_CLOSE_WINDOW: return Pt_CB_WEB_CLOSE_WINDOW(info);
		case OS.Pt_CB_WEB_COMPLETE:	    return Pt_CB_WEB_COMPLETE(info);
		case OS.Pt_CB_WEB_DATA_REQ:     return Pt_CB_WEB_DATA_REQ(info);
		case OS.Pt_CB_WEB_METADATA:		return Pt_CB_WEB_METADATA(info);
		case OS.Pt_CB_WEB_NEW_WINDOW:   return Pt_CB_WEB_NEW_WINDOW(info);
		case OS.Pt_CB_WEB_START:	    return Pt_CB_WEB_START(info);
		case OS.Pt_CB_WEB_STATUS:	    return Pt_CB_WEB_STATUS(info);
		case OS.Pt_CB_WEB_URL:		    return Pt_CB_WEB_URL(info);
	}
	return OS.Pt_CONTINUE;
}

public boolean execute(String script) {
	return false;
}

public boolean forward() {
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

public String getBrowserType () {
	return "voyager"; //$NON-NLS-1$
}

public String getText () {
	// TODO
	return ""; //$NON-NLS-1$
}

public String getUrl() {
	return url;
}

public boolean isBackEnabled() {
	int ptr = OS.malloc(4);
	int[] args = new int[] {OS.Pt_ARG_WEB_NAVIGATE_PAGE, ptr, 0};
	OS.PtGetResources(webHandle, args.length / 3, args);
	int[] result = new int[1];
	OS.memmove(result, ptr, 4);
	OS.memmove(result, result[0], 4);
	OS.free(ptr);
	return (result[0] & (1 << OS.Pt_WEB_DIRECTION_BACK)) != 0;
}

public boolean isForwardEnabled() {
	int ptr = OS.malloc(4);
	int[] args = new int[]{OS.Pt_ARG_WEB_NAVIGATE_PAGE, ptr, 0};
	OS.PtGetResources(webHandle, args.length / 3, args);
	int[] result = new int[1];
	OS.memmove(result, ptr, 4);
	OS.memmove(result, result[0], 4);
	OS.free(ptr);
	return (result[0] & (1 << OS.Pt_WEB_DIRECTION_FWD)) != 0;
}

void onDispose() {
	OS.PtDestroyWidget(webHandle);
	webHandle = 0;
	instanceCount--;
}

void onFocusGained(Event e) {
	OS.PtContainerGiveFocus(webHandle, null);
}

int Pt_CB_WEB_CLOSE_WINDOW(int info) {
	WindowEvent event = new WindowEvent(browser);
	event.display = browser.getDisplay();
	event.widget = browser;
	for(int i = 0; i < closeWindowListeners.length; i++ )
		closeWindowListeners[i].close(event);
	browser.dispose();
	return OS.Pt_CONTINUE;
}

int Pt_CB_WEB_COMPLETE(int info) {
	Display display = browser.getDisplay();
	LocationEvent event = new LocationEvent(browser);
	event.display = display;
	event.widget = browser;
	event.location = url;
	event.top = true;
	for (int i = 0; i < locationListeners.length; i++)
		locationListeners[i].changed(event);
	ProgressEvent progress = new ProgressEvent(browser);
	progress.display = display;
	progress.widget = browser;
	progress.current = totalProgress;
	progress.total = totalProgress;
	for (int i = 0; i < progressListeners.length; i++)
		progressListeners[i].completed(progress);
	StatusTextEvent statusevent = new StatusTextEvent(browser);
	statusevent.display = display;
	statusevent.widget = browser;
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
	PtWebClient2Data_t clientData = new PtWebClient2Data_t();
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
			/*
			* Feature on Photon. The PtSetResource() call for PtWebClient data imposes
			* a limit on the size of the text buffer being passed. The workaround is
			* to break the text into 1KB chunks.
			*/
			if (text.length() - textOffset > 1024) {
				data = text.substring(textOffset, textOffset + 1024);
				textOffset += 1024;
			} else {
				data = text.substring(textOffset);
			}
			break;
		case OS.Pt_WEB_DATA_CLOSE:
			text = ""; //$NON-NLS-1$
			break;
	}
	if (data != null) {
		byte[] buffer = Converter.wcsToMbcs(null, data, true);
		clientData.data = OS.malloc(buffer.length);
		OS.memmove(clientData.data, buffer, buffer.length);
		clientData.length = buffer.length - 1;
	}
	dataReq.url = clientData.url;
	int ptr = OS.malloc(PtWebClient2Data_t.sizeof);
	OS.memmove(ptr, clientData, PtWebClient2Data_t.sizeof);
	OS.PtSetResource(webHandle, OS.Pt_ARG_WEB_DATA, clientData.data, ptr);
	OS.free(ptr);
	if (clientData.data != 0) OS.free(clientData.data);
	return OS.Pt_CONTINUE;
}

int Pt_CB_WEB_METADATA(int info) {
	PtCallbackInfo_t cbinfo_t = new PtCallbackInfo_t();
	OS.memmove(cbinfo_t, info, PtCallbackInfo_t.sizeof);
	final PtWebMetaDataCallback_t webmeta_t = new PtWebMetaDataCallback_t();
	OS.memmove(webmeta_t, cbinfo_t.cbdata, PtWebMetaDataCallback_t.sizeof);
	String name = new String(webmeta_t.name, 0, OS.strlen(cbinfo_t.cbdata));
	if (name.equals("title")) { //$NON-NLS-1$
		String title = new String(webmeta_t.value, 0, OS.strlen(cbinfo_t.cbdata + webmeta_t.name.length));
		TitleEvent newEvent = new TitleEvent(browser);
		newEvent.display = browser.getDisplay();
		newEvent.widget = browser;
		newEvent.title = title;
		/*
		* Feature on Photon.  The Voyager Browser updates the title section
		* in the window decoration even if the title refers to an inner frame.
		* Browsers on other platforms only update the title that refers to
		* the top frame.  As a result, the title event on Photon is sent for 
		* both top and inner frames.
		*/
		for (int i = 0; i < titleListeners.length; i++)
			titleListeners[i].changed(newEvent);
	}	
	return OS.Pt_CONTINUE;
}

int Pt_CB_WEB_NEW_WINDOW(int info) {
	PtCallbackInfo_t cbinfo_t = new PtCallbackInfo_t();
	OS.memmove(cbinfo_t, info, PtCallbackInfo_t.sizeof);
	final PtWebWindowCallback_t webwin_t = new PtWebWindowCallback_t();
	OS.memmove(webwin_t,cbinfo_t.cbdata,PtWebWindowCallback_t.sizeof);
	/*
	* Feature on Photon.  The server will use the first PtWebClient
	* widget created from within the CB_WEB_NEW_WINDOW callback to
	* host the new window.  The workaround is to create a temporary
	* PtWebClient widget everytime the notification is received. 
	* When its location is known, the Browser provided by the 
	* application is then redirected.
	*/ 
	final Browser hidden = new Browser(browser.getParent(), SWT.NONE);
	hidden.addLocationListener(new LocationListener() {
		public void changed(org.eclipse.swt.browser.LocationEvent event) {
			/*
			* Bug on Voyager.  The first PtWebClient widget created
			* from within the CB_WEB_NEW_WINDOW callback is the one
			* hosting the new window.  For some reason, this PtWebClient
			* widget may or may not receive a Pt_CB_WEB_URL
			* notification. It receives a Pt_CB_WEB_COMPLETE in all cases.
			* The workaround is to reload the content when this occurs.
			* This request causes the Pt_CB_WEB_URL to be correctly sent, 
			* providing the information required to redirect the browser
			* provided by the application.
			*/
			if (event.location.length() == 0) {
				hidden.refresh();
				return;
			}
			hidden.dispose();
		}
		public void changing(final org.eclipse.swt.browser.LocationEvent event) {
			Browser redirect = ((Voyager)hidden.webBrowser).redirectBrowser;
			/* Forward the link to the Browser actually provided by the user */
			if (redirect != null && !redirect.isDisposed()) {
				WindowEvent newEvent = new WindowEvent(redirect);
				newEvent.display = browser.getDisplay();
				newEvent.widget = redirect;
				newEvent.location = null;
				/* Photon sets the size to 0,0 when it isn't specified. */
				newEvent.size = webwin_t.size_w == 0 && webwin_t.size_h == 0 ? null : new Point(webwin_t.size_w, webwin_t.size_h);
				for (int i = 0; i < redirect.webBrowser.visibilityWindowListeners.length; i++)
					redirect.webBrowser.visibilityWindowListeners[i].show(newEvent);
				redirect.setUrl(event.location);
			}
		}
	});
	WindowEvent event = new WindowEvent(browser);
	event.display = browser.getDisplay();
	event.widget = browser;
	event.required = true;
	for (int i = 0; i < openWindowListeners.length; i++)
		openWindowListeners[i].open(event);
	if (event.browser != null && !event.browser.isDisposed()) ((Voyager)hidden.webBrowser).redirectBrowser = event.browser;
	return OS.Pt_CONTINUE;
}

int Pt_CB_WEB_START(int info) {
	currentProgress = 1;
	ProgressEvent progress = new ProgressEvent(browser);
	progress.display = browser.getDisplay();
	progress.widget = browser;
	progress.current = currentProgress;
	progress.total = totalProgress;
	for (int i = 0; i < progressListeners.length; i++)
		progressListeners[i].changed(progress);
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
			StatusTextEvent statusevent = new StatusTextEvent(browser);
			statusevent.display = browser.getDisplay();
			statusevent.widget = browser;
			statusevent.text = new String(webstatus.desc, 0, OS.strlen(cbinfo_t.cbdata));
			for (int i = 0; i < statusTextListeners.length; i++)
				statusTextListeners[i].changed(statusevent);
			if (webstatus.type == OS.Pt_WEB_STATUS_PROGRESS) {
				currentProgress++;
				if (currentProgress >= totalProgress) currentProgress = 1;
				ProgressEvent progress = new ProgressEvent(browser);
				progress.display = browser.getDisplay();
				progress.widget = browser;
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
	LocationEvent event = new LocationEvent(browser);
	event.display = browser.getDisplay();
	event.widget = browser;
	event.location = url;
	event.doit = true;
	for (int i = 0; i < locationListeners.length; i++)
		locationListeners[i].changing(event);
	/* Widget could have been disposed */
	if (browser.isDisposed()) return OS.Pt_CONTINUE;
	if (!event.doit) stop();
	return OS.Pt_CONTINUE;
}

public void refresh() {
	OS.PtSetResource(webHandle, OS.Pt_ARG_WEB_RELOAD, 1, 0);
}

public boolean setText(String html) {
	text = html;
	textOffset = 0;
	byte[] buffer = Converter.wcsToMbcs(null, "client:", true); //$NON-NLS-1$
	int ptr = OS.malloc(buffer.length);
	OS.memmove(ptr, buffer, buffer.length);
	OS.PtSetResource(webHandle, OS.Pt_ARG_WEB_GET_URL, ptr, OS.Pt_WEB_ACTION_DISPLAY);
	OS.free(ptr);
	return true;
}

public boolean setUrl(String url) {
	byte[] buffer = Converter.wcsToMbcs(null, url, true);
	int ptr = OS.malloc(buffer.length);
	OS.memmove(ptr, buffer, buffer.length);
	OS.PtSetResource(webHandle, OS.Pt_ARG_WEB_GET_URL, ptr, OS.Pt_WEB_ACTION_DISPLAY);
	OS.free(ptr);
	return true;
}

public void stop() {
	OS.PtSetResource(webHandle, OS.Pt_ARG_WEB_STOP, 1, 0);
}
}
