/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.ole.win32.*;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.ole.win32.*;
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

	OleFrame frame;
	OleControlSite site;
	OleAutomation auto;

	boolean backwardEnabled;
	boolean forwardEnabled;

	String html;

	/* External Listener management */
	LocationListener[] locationListeners = new LocationListener[0];
	OpenWindowListener[] openWindowListeners = new OpenWindowListener[0];
	ProgressListener[] progressListeners = new ProgressListener[0];
	StatusTextListener[] statusTextListeners = new StatusTextListener[0];
	VisibilityListener[] visibilityListeners = new VisibilityListener[0];
	
	static final int BeforeNavigate2 = 0xfa;
	static final int CommandStateChange = 0x69;
	static final int DocumentComplete = 0x103;
	static final int NewWindow2 = 0xfb;
	static final int OnVisible = 0xfe;
	static final int ProgressChange = 0x6c;
	static final int StatusTextChange = 0x66;
	
	static final short CSC_UPDATECOMMANDS = -1;
	static final short CSC_NAVIGATEFORWARD = 1;
	static final short CSC_NAVIGATEBACK = 2;

	/* Package Name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.browser."; //$NON-NLS-1$

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
	frame = new OleFrame(this, SWT.NONE);
	try {
		site = new WebSite(frame, SWT.NONE, "Shell.Explorer"); //$NON-NLS-1$
	} catch (SWTException e) {
		dispose();
		SWT.error(SWT.ERROR_NO_HANDLES);
	}
	site.doVerb(OLE.OLEIVERB_INPLACEACTIVATE);
	auto = new OleAutomation(site);
	addListener(SWT.Dispose, new Listener() {
		public void handleEvent(Event e) {
			if (auto != null)
				auto.dispose();
			auto = null;
		}
	});
	addListener(SWT.Resize, new Listener() {
		public void handleEvent(Event e) {
			frame.setBounds(getClientArea());
		}
	});
	OleListener listener = new OleListener() {
		public void handleEvent(OleEvent event) {
			switch (event.type) {
				case BeforeNavigate2 : {
					Variant varResult = event.arguments[0];
					IDispatch dispatch = varResult.getDispatch();
					Variant variant = new Variant(auto);
					IDispatch top = variant.getDispatch();
					if (top.getAddress() == dispatch.getAddress()) {
						varResult = event.arguments[1];
						String url = varResult.getString();
						LocationEvent newEvent = new LocationEvent(Browser.this);
						newEvent.display = getDisplay();
						newEvent.widget = Browser.this;
						newEvent.location = url;
						for (int i = 0; i < locationListeners.length; i++)
							locationListeners[i].changing(newEvent);
						Variant cancel = event.arguments[6];
						if (cancel != null) {
							int pCancel = cancel.getByRef();
							COM.MoveMemory(pCancel, new short[]{newEvent.cancel ? COM.VARIANT_TRUE : COM.VARIANT_FALSE}, 2);
					   }
					}
					
					/*
					* This code is intentionally commented.  This IDispatch was received
					* as an argument from the OleEvent and it will be disposed along with
					* the other arguments.  
					*/
					//dispatch.Release();

					/*
					* This code is intentionally commented.  A Variant constructed from an
					* OleAutomation object does not increase its reference count.  The IDispatch
					* obtained from this Variant did not increase the reference count for the
					* OleAutomation instance either. 
					*/
					//top.Release();
					//variant.dispose();
					break;
				}
				case CommandStateChange : {
					boolean enabled = false;
					Variant varResult = event.arguments[0];
					int command = varResult.getInt();
					varResult = event.arguments[1];
					enabled = varResult.getBoolean();
					if (command == CSC_NAVIGATEBACK) backwardEnabled = enabled;
					if (command == CSC_NAVIGATEFORWARD) forwardEnabled = enabled;
					break;
				}
				case DocumentComplete: {
					Variant varResult = event.arguments[0];
					IDispatch dispatch = varResult.getDispatch();
					Variant variant = new Variant(auto);
					IDispatch top = variant.getDispatch();
					if (top.getAddress() == dispatch.getAddress()) {
						if (html != null) {
							TCHAR buffer = new TCHAR(0, html, true);
							html = null;
							int byteCount = buffer.length() * TCHAR.sizeof;
							int hGlobal = OS.GlobalAlloc(OS.GMEM_FIXED, byteCount);
							if (hGlobal != 0) {
								OS.MoveMemory(hGlobal, buffer, byteCount);
								int[] ppstm = new int[1];
								/* 
								* Note.  CreateStreamOnHGlobal is called with the flag fDeleteOnRelease.
								* If the call succeeds the buffer hGlobal is freed automatically
								* when the IStream object is released. If the call fails, free the buffer
								* hGlobal.
								*/
								if (OS.CreateStreamOnHGlobal(hGlobal, true, ppstm) == OS.S_OK) {
									int[] rgdispid = auto.getIDsOfNames(new String[] {"Document"}); //$NON-NLS-1$
									Variant pVarResult = auto.getProperty(rgdispid[0]);
									pVarResult = auto.getProperty(rgdispid[0]);
									IDispatch dispatchDocument = pVarResult.getDispatch();
									pVarResult.dispose();
									int[] ppvObject = new int[1];
									int result = dispatchDocument.QueryInterface(COM.IIDIPersistStreamInit, ppvObject);
									if (result == OS.S_OK) {
										IPersistStreamInit persistStreamInit = new IPersistStreamInit(ppvObject[0]);
										if (persistStreamInit.InitNew() == OS.S_OK) {
											persistStreamInit.Load(ppstm[0]);
										}
										persistStreamInit.Release();
									}
									dispatchDocument.Release();
									IUnknown stream = new IUnknown(ppstm[0]);
									stream.Release();
								} else {
									OS.GlobalFree(hGlobal);
								}
							}
						} else {
							varResult = event.arguments[1];
							String url = varResult.getString();
							LocationEvent locationEvent = new LocationEvent(Browser.this);
							locationEvent.location = url;
							for (int i = 0; i < locationListeners.length; i++)
								locationListeners[i].changed(locationEvent);
							ProgressEvent progressEvent = new ProgressEvent(Browser.this);
							for (int i = 0; i < progressListeners.length; i++)
								progressListeners[i].completed(progressEvent);
						}
					}
					
					/*
					* This code is intentionally commented.  A Variant constructed from an
					* OleAutomation object does not increase its reference count.  The IDispatch
					* obtained from this Variant did not increase the reference count for the
					* OleAutomation instance either. 
					*/
					//top.Release();
					//variant.dispose();
						
					/*
					* This code is intentionally commented.  This IDispatch was received
					* as an argument from the OleEvent and it will be disposed along with
					* the other arguments.  
					*/
					//dispatch.Release();
					break;
				}
				case NewWindow2 : {
					OpenWindowEvent openEvent = new OpenWindowEvent(Browser.this);
					openEvent.display = getDisplay();
					openEvent.widget = Browser.this;
					for (int i = 0; i < openWindowListeners.length; i++)
						openWindowListeners[i].open(openEvent);
					Browser browser = openEvent.browser;
					boolean doit = browser != null && !browser.isDisposed();
					if (doit) {
						Variant variant = new Variant(browser.auto);
						IDispatch iDispatch = variant.getDispatch();
						variant.dispose();
						Variant ppDisp = event.arguments[0];
						int byref = ppDisp.getByRef();
						if (byref != 0) COM.MoveMemory(byref, new int[] {iDispatch.getAddress()}, 4);
						iDispatch.Release();
					}
					break;
				}
				case OnVisible : {
					Variant arg1 = event.arguments[0];
					boolean visible = arg1.getBoolean();
					VisibilityEvent newEvent = new VisibilityEvent(Browser.this);
					newEvent.display = getDisplay();
					newEvent.widget = Browser.this;
					if (visible) {
						for (int i = 0; i < visibilityListeners.length; i++)
							visibilityListeners[i].show(newEvent);
					} else {
						for (int i = 0; i < visibilityListeners.length; i++)
							visibilityListeners[i].hide(newEvent);
					}
					break;
				}
				case ProgressChange : {
					Variant arg1 = event.arguments[0];
					int nProgress = arg1.getType() != OLE.VT_I4 ? 0 : arg1.getInt(); // may be -1
					Variant arg2 = event.arguments[1];
					int nProgressMax = arg2.getType() != OLE.VT_I4 ? 0 : arg2.getInt();
					ProgressEvent newEvent = new ProgressEvent(Browser.this);
					newEvent.display = getDisplay();
					newEvent.widget = Browser.this;
					newEvent.current = nProgress;
					newEvent.total = nProgressMax;
					if (nProgress != -1) {
						for (int i = 0; i < progressListeners.length; i++)
							progressListeners[i].changed(newEvent);
					}
					break;
				}
				case StatusTextChange : {
					Variant arg1 = event.arguments[0];
					if (arg1.getType() == OLE.VT_BSTR) {
						String text = arg1.getString();
						StatusTextEvent newEvent = new StatusTextEvent(Browser.this);
						newEvent.display = getDisplay();
						newEvent.widget = Browser.this;
						newEvent.text = text;
						for (int i = 0; i < statusTextListeners.length; i++)
							statusTextListeners[i].changed(newEvent);
					}
					break;
				}
			}
			
			/*
			* Dispose all arguments passed in the OleEvent.  This must be
			* done to properly release any IDispatch reference that was
			* automatically addRef'ed when constructing the OleEvent.  
			*/
			Variant[] arguments = event.arguments;
			for (int i = 0; i < arguments.length; i++) arguments[i].dispose();
		}
	};
	site.addEventListener(BeforeNavigate2, listener);
	site.addEventListener(CommandStateChange, listener);
	site.addEventListener(DocumentComplete, listener);
	site.addEventListener(NewWindow2, listener);
	site.addEventListener(OnVisible, listener);
	site.addEventListener(ProgressChange, listener);
	site.addEventListener(StatusTextChange, listener);
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
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);	
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
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
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
	if (listener == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
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
	if (!backwardEnabled)
		return false;
	int[] rgdispid = auto.getIDsOfNames(new String[] { "GoBack" }); //$NON-NLS-1$
	Variant pVarResult = auto.invoke(rgdispid[0]);
	return pVarResult.getType() == OLE.VT_EMPTY;
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
	if (!forwardEnabled) return false;
	int[] rgdispid = auto.getIDsOfNames(new String[] { "GoForward" }); //$NON-NLS-1$
	Variant pVarResult = auto.invoke(rgdispid[0]);
	return pVarResult.getType() == OLE.VT_EMPTY;
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
	int[] rgdispid = auto.getIDsOfNames(new String[] { "LocationURL" }); //$NON-NLS-1$
	Variant pVarResult = auto.getProperty(rgdispid[0]);
	if (pVarResult == null || pVarResult.getType() != OLE.VT_BSTR)
		return "";
	return pVarResult.getString();
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
	int[] rgdispid = auto.getIDsOfNames(new String[] { "Refresh" }); //$NON-NLS-1$
	auto.invoke(rgdispid[0]);
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
	this.html = html;
	
	/*
	* Navigate to the blank page and insert the given html when
	* receiving the next DocumentComplete notification.  See the
	* MSDN article "Loading HTML content from a Stream".
	*/
	int[] rgdispid = auto.getIDsOfNames(new String[] { "Navigate", "URL" }); //$NON-NLS-1$ //$NON-NLS-2$
	Variant[] rgvarg = new Variant[1];
	rgvarg[0] = new Variant("about:blank"); //$NON-NLS-1$
	int[] rgdispidNamedArgs = new int[1];
	rgdispidNamedArgs[0] = rgdispid[1];
	Variant pVarResult = auto.invoke(rgdispid[0], rgvarg, rgdispidNamedArgs);
	return pVarResult.getType() == OLE.VT_EMPTY;
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
	int[] rgdispid = auto.getIDsOfNames(new String[] { "Navigate", "URL" }); //$NON-NLS-1$ //$NON-NLS-2$
	Variant[] rgvarg = new Variant[1];
	rgvarg[0] = new Variant(url);
	int[] rgdispidNamedArgs = new int[1];
	rgdispidNamedArgs[0] = rgdispid[1];
	Variant pVarResult = auto.invoke(rgdispid[0], rgvarg, rgdispidNamedArgs);
	return pVarResult.getType() == OLE.VT_EMPTY;
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
	int[] rgdispid = auto.getIDsOfNames(new String[] { "Stop" }); //$NON-NLS-1$
	auto.invoke(rgdispid[0]);
}
}
