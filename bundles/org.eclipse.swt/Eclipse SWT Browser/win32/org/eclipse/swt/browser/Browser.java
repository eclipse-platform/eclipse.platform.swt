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
import org.eclipse.swt.internal.ole.win32.COM;
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

	/* External Listener management */
	LocationListener[] locationListeners = new LocationListener[0];
	ProgressListener[] progressListeners = new ProgressListener[0];
	StatusTextListener[] statusTextListeners = new StatusTextListener[0];
	
	static final int BeforeNavigate2 = 250;
	static final int CommandStateChange = 105;
	static final int NavigateComplete2 = 259;
	static final int ProgressChange = 108;
	static final int StatusTextChange = 102;
	
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
 *
 * @see #getStyle
 * 
 * @since 3.0
 */
public Browser(Composite parent, int style) {
	super(parent, style);
	frame = new OleFrame(this, SWT.NONE);
	site = new OleControlSite(frame, SWT.NONE, "Shell.Explorer"); //$NON-NLS-1$
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
					Variant varResult = event.arguments[1];
					if (varResult != null) {
						String url = varResult.getString();
						LocationEvent newEvent = new LocationEvent(Browser.this);
						newEvent.location = url;
						if (locationListeners != null) {
							for (int i = 0; i < locationListeners.length; i++)
								locationListeners[i].changing(newEvent);
						}						
						Variant cancel = event.arguments[6];
						if (cancel != null){
							int pCancel = cancel.getByRef();
							COM.MoveMemory(pCancel, new short[]{newEvent.cancel ? COM.VARIANT_TRUE : COM.VARIANT_FALSE}, 2);
					   }
					}
					return;
				}
				case CommandStateChange : {
					int command = 0;
					boolean enabled = false;
					Variant varResult = event.arguments[0];
					if (varResult != null) {
						command = varResult.getInt();
					}
					varResult = event.arguments[1];
					if (varResult != null) {
						enabled = varResult.getBoolean();
					}
					if (command == CSC_NAVIGATEBACK) backwardEnabled = enabled;
					if (command == CSC_NAVIGATEFORWARD) forwardEnabled = enabled;
					return;
				}
				case NavigateComplete2: {
					Variant varResult = event.arguments[1];
					if (varResult != null) {
						String url = varResult.getString();
						LocationEvent newEvent = new LocationEvent(Browser.this);
						newEvent.location = url;
						if (locationListeners != null) {
							for (int i = 0; i < locationListeners.length; i++)
								locationListeners[i].changed(newEvent);
						}						
					}
					return;
				}
				case ProgressChange : {
					Variant arg1 = event.arguments[0];
					int nProgress = (arg1 == null || arg1.getType() != OLE.VT_I4) ? 0 : arg1.getInt(); // may be -1
					Variant arg2 = event.arguments[1];
					int nProgressMax = (arg2 == null || arg2.getType() != OLE.VT_I4) ? 0 : arg2.getInt();
					ProgressEvent newEvent = new ProgressEvent(Browser.this);
					newEvent.current = nProgress;
					newEvent.total = nProgressMax;
					if (progressListeners != null) {
						if (nProgress != -1) {
							for (int i = 0; i < progressListeners.length; i++)
								progressListeners[i].changed(newEvent);
						} else {
							for (int i = 0; i < progressListeners.length; i++)
								progressListeners[i].completed(newEvent);
						}
					}
					return;
				}
				case StatusTextChange : {
					Variant arg1 = event.arguments[0];
					if (arg1 == null || arg1.getType() != OLE.VT_BSTR)	return;
					String text = arg1.getString();
					StatusTextEvent newEvent = new StatusTextEvent(Browser.this);
					newEvent.text = text;
					if (statusTextListeners != null) {
						for (int i = 0; i < statusTextListeners.length; i++)
							statusTextListeners[i].changed(newEvent);
					}
					return;
				}
			}
		}
	};
	site.addEventListener(BeforeNavigate2, listener);
	site.addEventListener(CommandStateChange, listener);
	site.addEventListener(NavigateComplete2, listener);
	site.addEventListener(ProgressChange, listener);
	site.addEventListener(StatusTextChange, listener);
}

/**	 
 * Adds the listener to receive events.
 * <p>
 *
 * @param listener the listener
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * 		<li>ERROR_NULL_ARGUMENT when listener is null</li>
 *	</ul>
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
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * 		<li>ERROR_NULL_ARGUMENT when listener is null</li>
 *	</ul>
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
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * 		<li>ERROR_NULL_ARGUMENT when listener is null</li>
 *	</ul>
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
 * Navigate to the previous session history item.
 *
 * @return <code>true</code> if the operation was successful and <code>false</code> otherwise
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
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
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
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
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
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
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
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
 * @exception SWTError
 *	<ul><li>ERROR_THREAD_INVALID_ACCESS	when called from the wrong thread</li>
 * 		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * 		<li>ERROR_NULL_ARGUMENT when listener is null</li></ul>
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
 * @exception SWTError
 *	<ul><li>ERROR_THREAD_INVALID_ACCESS	when called from the wrong thread</li>
 * 		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * 		<li>ERROR_NULL_ARGUMENT when listener is null</li></ul>
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
 * @exception SWTError
 *	<ul><li>ERROR_THREAD_INVALID_ACCESS	when called from the wrong thread</li>
 * 		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 * 		<li>ERROR_NULL_ARGUMENT when listener is null</li></ul>
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
 * Loads a URL.
 * 
 * @param url the URL to be loaded
 *
 * @return true if the operation was successfull and false otherwise.
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the text is null</li>
 * </ul>
 * 
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
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
	Variant pVarResult =
		auto.invoke(rgdispid[0], rgvarg, rgdispidNamedArgs);
	return pVarResult.getType() == OLE.VT_EMPTY;
}

/**
 * Stop any loading and rendering activity.
 *
 * @exception SWTError <ul>
 *		<li>ERROR_THREAD_INVALID_ACCESS when called from the wrong thread</li>
 *		<li>ERROR_WIDGET_DISPOSED when the widget has been disposed</li>
 *	</ul>
 *
 * @since 3.0
 */
public void stop() {
	checkWidget();
	int[] rgdispid = auto.getIDsOfNames(new String[] { "Stop" }); //$NON-NLS-1$
	auto.invoke(rgdispid[0]);
}
}
