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
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.mozilla.*;

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
	
	nsIWebBrowser webBrowser;
	int gtkHandle;

	/* Interfaces for this Mozilla embedding notification */
	XPCOMObject supports;
	XPCOMObject weakReference;
	XPCOMObject webProgressListener;
	XPCOMObject	webBrowserChrome;
	XPCOMObject webBrowserChromeFocus;
	XPCOMObject embeddingSiteWindow;
	XPCOMObject interfaceRequestor;
	XPCOMObject supportsWeakReference;
	XPCOMObject contextMenuListener;	
	XPCOMObject uriContentListener;
	int chromeFlags = nsIWebBrowserChrome.CHROME_DEFAULT;
	int refCount = 0;
	int request;
	String html;
	Point location;
	Point size;

	/* External Listener management */
	CloseWindowListener[] closeWindowListeners = new CloseWindowListener[0];
	LocationListener[] locationListeners = new LocationListener[0];
	OpenWindowListener[] openWindowListeners = new OpenWindowListener[0];
	ProgressListener[] progressListeners = new ProgressListener[0];
	StatusTextListener[] statusTextListeners = new StatusTextListener[0];
	VisibilityWindowListener[] visibilityWindowListeners = new VisibilityWindowListener[0];

	static nsIAppShell AppShell;
	static AppFileLocProvider LocProvider; 
	static WindowCreator WindowCreator;
	static int BrowserCount;
	static boolean mozilla;
	static boolean IsLinux;

	/* Package Name */
	static final String PACKAGE_PREFIX = "org.eclipse.swt.browser."; //$NON-NLS-1$
	
static {
	String osName = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
	IsLinux = osName.startsWith("linux");
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
	super(parent,style | SWT.EMBEDDED);
	
	if (!IsLinux) {
		dispose();
		SWT.error(SWT.ERROR_NO_HANDLES);		
	}

	int[] result = new int[1];
	if (!mozilla) {
		try {
			Library.loadLibrary("swt-gtk"); //$NON-NLS-1$
			Library.loadLibrary ("swt-mozilla"); //$NON-NLS-1$
		} catch (UnsatisfiedLinkError e) {
			dispose();
			SWT.error(SWT.ERROR_NO_HANDLES);
		}
		
		String mozillaPath = GRE.mozillaPath;
		if (mozillaPath == null) {
			dispose();
			SWT.error(SWT.ERROR_NO_HANDLES);
		}

		LocProvider = new AppFileLocProvider();
		LocProvider.AddRef();

		int[] retVal = new int[1];
		nsString path = new nsString(mozillaPath);
		int rc = XPCOM.NS_NewLocalFile(path.getAddress(), true, retVal);
		path.dispose();
		if (rc != XPCOM.NS_OK) error(rc);
		if (retVal[0] == 0) error(XPCOM.NS_ERROR_NULL_POINTER);
			
		nsILocalFile localFile = new nsILocalFile(retVal[0]);
		rc = XPCOM.NS_InitEmbedding(localFile.getAddress(), LocProvider.getAddress());
		localFile.Release();
		if (rc != XPCOM.NS_OK) {
			LocProvider.Release();
			LocProvider = null;
			dispose();
			SWT.error(SWT.ERROR_NO_HANDLES);
		}

		rc = XPCOM.NS_GetComponentManager(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		
		nsIComponentManager componentManager = new nsIComponentManager(result[0]);
		result[0] = 0;
		rc = componentManager.CreateInstance(XPCOM.NS_APPSHELL_CID, 0, nsIAppShell.NS_IAPPSHELL_IID, result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);		
		componentManager.Release();
		
		AppShell = new nsIAppShell(result[0]);
		rc = AppShell.Create(null, null);
		if (rc != XPCOM.NS_OK) error(rc);
		rc = AppShell.Spinup();
		if (rc != XPCOM.NS_OK) error(rc);
		
		WindowCreator = new WindowCreator();
		WindowCreator.AddRef();
		
		rc = XPCOM.NS_GetServiceManager(result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		
		nsIServiceManager serviceManager = new nsIServiceManager(result[0]);
		result[0] = 0;
		byte[] buffer = XPCOM.NS_WINDOWWATCHER_CONTRACTID.getBytes();
		byte[] aContractID = new byte[buffer.length + 1];
		System.arraycopy(buffer, 0, aContractID, 0, buffer.length);
		rc = serviceManager.GetServiceByContractID(aContractID, nsIWindowWatcher.NS_IWINDOWWATCHER_IID, result);
		if (rc != XPCOM.NS_OK) error(rc);
		if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);		
		serviceManager.Release();
		
		nsIWindowWatcher windowWatcher = new nsIWindowWatcher(result[0]);
		result[0] = 0;
		rc = windowWatcher.SetWindowCreator(WindowCreator.getAddress());
		if (rc != XPCOM.NS_OK) error(rc);
		windowWatcher.Release();
		
		mozilla = true;
	}
	BrowserCount++;
	if (BrowserCount == 1) {
		GTK.gtk_init_check(new int[1], null);
		final Display display = getDisplay();
		display.asyncExec(new Runnable() {
			public void run() {
				if (BrowserCount == 0) return;
				while (GTK.gtk_events_pending() != 0) {
					GTK.gtk_main_iteration();
				}
				display.timerExec(25, this);		
			}
		});
	}
	parent.getShell().setFocus();
	gtkHandle = GTK.gtk_plug_new(embeddedHandle);

	int rc = XPCOM.NS_GetComponentManager(result);
	if (rc != XPCOM.NS_OK) error(rc);
	if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
	
	nsIComponentManager componentManager = new nsIComponentManager(result[0]);
	result[0] = 0;
	nsID NS_IWEBBROWSER_CID = new nsID("F1EAC761-87E9-11d3-AF80-00A024FFC08C"); //$NON-NLS-1$
	rc = componentManager.CreateInstance(NS_IWEBBROWSER_CID, 0, nsIWebBrowser.NS_IWEBBROWSER_IID, result);
	if (rc != XPCOM.NS_OK) error(rc);
	if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);	
	componentManager.Release();
	
	webBrowser = new nsIWebBrowser(result[0]); 

	createCOMInterfaces();
	AddRef();

	rc = webBrowser.SetContainerWindow(webBrowserChrome.getAddress());
	if (rc != XPCOM.NS_OK) error(rc);
			
	rc = webBrowser.QueryInterface(nsIBaseWindow.NS_IBASEWINDOW_IID, result);
	if (rc != XPCOM.NS_OK) error(rc);
	if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
	
	nsIBaseWindow baseWindow = new nsIBaseWindow(result[0]);	
	rc = baseWindow.InitWindow(gtkHandle, 0, 0, 0, 2, 2);
	if (rc != XPCOM.NS_OK) error(XPCOM.NS_ERROR_FAILURE);
	rc = baseWindow.Create();
	if (rc != XPCOM.NS_OK) error(XPCOM.NS_ERROR_FAILURE);
	rc = baseWindow.SetVisibility(true);
	if (rc != XPCOM.NS_OK) error(XPCOM.NS_ERROR_FAILURE);
	baseWindow.Release();

	rc = webBrowser.AddWebBrowserListener(weakReference.getAddress(), nsIWebProgressListener.NS_IWEBPROGRESSLISTENER_IID);
	if (rc != XPCOM.NS_OK) error(rc);

	rc = webBrowser.SetParentURIContentListener(uriContentListener.getAddress());
	if (rc != XPCOM.NS_OK) error(rc);

	Listener listener = new Listener() {
		public void handleEvent(Event event) {
			switch (event.type) {
				case SWT.Dispose: onDispose(); break;
				case SWT.Resize: onResize(); break;
				case SWT.FocusIn: onFocusGained(event);	break;
				case SWT.FocusOut: onFocusLost(event); break;
			}
		}
	};	
	int[] folderEvents = new int[]{
		SWT.Dispose,
		SWT.Resize,  
		SWT.FocusIn, 
		SWT.FocusOut, 
	};
	for (int i = 0; i < folderEvents.length; i++) {
		addListener(folderEvents[i], listener);
	}

	GTK.gtk_widget_show(gtkHandle);
}

static Browser findBrowser(Control control, int gtkHandle) {
	if (control instanceof Browser) {
		Browser browser = (Browser)control;
		if (browser.gtkHandle == gtkHandle) return browser;
	}
	if (control instanceof Composite) {
		Composite composite = (Composite)control;
		Control[] children = composite.getChildren();
		for (int i = 0; i < children.length; i++) {
			Browser browser = findBrowser(children[i], gtkHandle);
			if (browser != null) return browser;
		}
	}
	return null;
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
 * @exception SWTError <ul>
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
 * @exception SWTError <ul>
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
 * @exception SWTError <ul>
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
	if (!IsLinux) return false;
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID, result);
	if (rc != XPCOM.NS_OK) error(rc);
	if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
	
	nsIWebNavigation webNavigation = new nsIWebNavigation(result[0]);		 	
	rc = webNavigation.GoBack();	
	webNavigation.Release();
	
	return rc == XPCOM.NS_OK;
}

protected void checkSubclass() {
	String name = getClass().getName();
	int index = name.lastIndexOf('.');
	if (!name.substring(0, index + 1).equals(PACKAGE_PREFIX)) {
		SWT.error(SWT.ERROR_INVALID_SUBCLASS);
	}
}

void createCOMInterfaces() {
	// Create each of the interfaces that this object implements
	supports = new XPCOMObject(new int[]{2, 0, 0}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
	};
	
	weakReference = new XPCOMObject(new int[]{2, 0, 0, 2}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return QueryReferent(args[0], args[1]);}
	};

	webProgressListener = new XPCOMObject(new int[]{2, 0, 0, 4, 6, 3, 4, 3}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return OnStateChange(args[0], args[1], args[2],args[3]);}
		public int method4(int[] args) {return OnProgressChange(args[0], args[1], args[2],args[3],args[4],args[5]);}
		public int method5(int[] args) {return OnLocationChange(args[0], args[1], args[2]);}
		public int method6(int[] args) {return OnStatusChange(args[0], args[1], args[2],args[3]);}
		public int method7(int[] args) {return OnSecurityChange(args[0], args[1], args[2]);}
	};
	
	webBrowserChrome = new XPCOMObject(new int[]{2, 0, 0, 2, 1, 1, 1, 1, 0, 2, 0, 1, 1}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return SetStatus(args[0], args[1]);}
		public int method4(int[] args) {return GetWebBrowser(args[0]);}
		public int method5(int[] args) {return SetWebBrowser(args[0]);}
		public int method6(int[] args) {return GetChromeFlags(args[0]);}
		public int method7(int[] args) {return SetChromeFlags(args[0]);}
		public int method8(int[] args) {return DestroyBrowserWindow();}
		public int method9(int[] args) {return SizeBrowserTo(args[0], args[1]);}
		public int method10(int[] args) {return ShowAsModal();}
		public int method11(int[] args) {return IsWindowModal(args[0]);}
		public int method12(int[] args) {return ExitModalEventLoop(args[0]);}
	};
	
	webBrowserChromeFocus = new XPCOMObject(new int[]{2, 0, 0, 0, 0}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return FocusNextElement();}
		public int method4(int[] args) {return FocusPrevElement();}
	};
		
	embeddingSiteWindow = new XPCOMObject(new int[]{2, 0, 0, 5, 5, 0, 1, 1, 1, 1, 1}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return SetDimensions(args[0], args[1], args[2], args[3], args[4]);}
		public int method4(int[] args) {return GetDimensions(args[0], args[1], args[2], args[3], args[4]);}
		public int method5(int[] args) {return SetFocus();}
		public int method6(int[] args) {return GetVisibility(args[0]);}
		public int method7(int[] args) {return SetVisibility(args[0]);}
		public int method8(int[] args) {return GetTitle(args[0]);}
		public int method9(int[] args) {return SetTitle(args[0]);}
		public int method10(int[] args) {return GetSiteWindow(args[0]);}
	};
	
	interfaceRequestor = new XPCOMObject(new int[]{2, 0, 0, 2}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return GetInterface(args[0], args[1]);}
	};
		
	supportsWeakReference = new XPCOMObject(new int[]{2, 0, 0, 1}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return GetWeakReference(args[0]);}
	};
	
	contextMenuListener = new XPCOMObject(new int[]{2, 0, 0, 3}){
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return OnShowContextMenu(args[0],args[1],args[2]);}
	};
	
	uriContentListener = new XPCOMObject(new int[]{2, 0, 0, 2, 5, 3, 4, 1, 1, 1, 1}) {
		public int method0(int[] args) {return QueryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return OnStartURIOpen(args[0], args[1]);}
		public int method4(int[] args) {return DoContent(args[0], args[1], args[2], args[3], args[4]);}
		public int method5(int[] args) {return IsPreferred(args[0], args[1], args[2]);}
		public int method6(int[] args) {return CanHandleContent(args[0], args[1], args[2], args[3]);}
		public int method7(int[] args) {return GetLoadCookie(args[0]);}
		public int method8(int[] args) {return SetLoadCookie(args[0]);}
		public int method9(int[] args) {return GetParentContentListener(args[0]);}
		public int method10(int[] args) {return SetParentContentListener(args[0]);}		
	};
}

void disposeCOMInterfaces() {
	if (supports != null) {
		supports.dispose();
		supports = null;
	}	
	if (weakReference != null) {
		weakReference.dispose();
		weakReference = null;	
	}
	if (webProgressListener != null) {
		webProgressListener.dispose();
		webProgressListener = null;
	}
	if (webBrowserChrome != null) {
		webBrowserChrome.dispose();
		webBrowserChrome = null;
	}
	if (webBrowserChromeFocus != null) {
		webBrowserChromeFocus.dispose();
		webBrowserChromeFocus = null;
	}
	if (embeddingSiteWindow != null) {
		embeddingSiteWindow.dispose();
		embeddingSiteWindow = null;
	}
	if (interfaceRequestor != null) {
		interfaceRequestor.dispose();
		interfaceRequestor = null;
	}		
	if (supportsWeakReference != null) {
		supportsWeakReference.dispose();
		supportsWeakReference = null;
	}	
	if (contextMenuListener != null) {
		contextMenuListener.dispose();
		contextMenuListener = null;
	}
	if (uriContentListener != null) {
		uriContentListener.dispose();
		uriContentListener = null;
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
	if (!IsLinux) return false;
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID, result);
	if (rc != XPCOM.NS_OK) error(rc);
	if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
	
	nsIWebNavigation webNavigation = new nsIWebNavigation(result[0]);
	rc = webNavigation.GoForward();
	webNavigation.Release();

	return rc == XPCOM.NS_OK;
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
	if (!IsLinux) return "";
	int[] aContentDOMWindow = new int[1];
	int rc = webBrowser.GetContentDOMWindow(aContentDOMWindow);
	if (rc != XPCOM.NS_OK) error(rc);
	if (aContentDOMWindow[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
			
	nsIDOMWindow domWindow = new nsIDOMWindow(aContentDOMWindow[0]);           
	int[] result = new int[1];
	rc = domWindow.QueryInterface(nsIDOMWindowInternal.NS_IDOMWINDOWINTERNAL_IID, result);
	if (rc != XPCOM.NS_OK) error(rc);
	if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
	domWindow.Release();
         
	nsIDOMWindowInternal domWindowInternal = new nsIDOMWindowInternal(result[0]);
	int[] aLocation = new int[1];  
	rc = domWindowInternal.GetLocation(aLocation);
	if (rc != XPCOM.NS_OK) error(rc);
	if (aLocation[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
	domWindowInternal.Release();

	nsIDOMLocation domLocation = new nsIDOMLocation(aLocation[0]); 
    nsString _retval = new nsString();
	rc = domLocation.ToString(_retval.getAddress());
	if (rc != XPCOM.NS_OK) error(rc);
	domLocation.Release();          
	String url = _retval.toString();
	_retval.dispose();
                
	return url;
}

static String error(int code) {
	throw new SWTError("XPCOM error "+code); //$NON-NLS-1$
}

void onDispose() {
	int rc = webBrowser.RemoveWebBrowserListener(weakReference.getAddress(), nsIWebProgressListener.NS_IWEBPROGRESSLISTENER_IID);
	if (rc != XPCOM.NS_OK) error(rc);

	rc = webBrowser.SetParentURIContentListener(0);
	if (rc != XPCOM.NS_OK) error(rc);
	
	int[] result = new int[1];
	rc = webBrowser.QueryInterface(nsIBaseWindow.NS_IBASEWINDOW_IID, result);
	if (rc != XPCOM.NS_OK) error(rc);
	if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
	
	nsIBaseWindow baseWindow = new nsIBaseWindow(result[0]);
	rc = baseWindow.Destroy();
	if (rc != XPCOM.NS_OK) error(rc);
	baseWindow.Release();
	
	Release();
	webBrowser.Release();
	
	GTK.gtk_widget_destroy(gtkHandle);
	while (GTK.gtk_events_pending() != 0) {
		GTK.gtk_main_iteration();
	}
	gtkHandle = 0;
	
	BrowserCount--;
	/*
	* This code is intentionally commented.  It is not possible to reinitialize
	* Mozilla once it has been terminated.  NS_InitEmbedding always fails after
	* NS_TermEmbedding has been called.  The workaround is to call NS_InitEmbedding
	* once and never call NS_TermEmbedding.
	*/
//	if (BrowserCount == 0) {
//		if (AppShell != null) {
//			// Shutdown the appshell service.
//			rc = AppShell.Spindown();
//			if (rc != XPCOM.NS_OK) error(rc);
//			AppShell.Release();
//			AppShell = null;
//		}
//		LocProvider.Release();
//		LocProvider = null;
//		WindowCreator.Release();
//		WindowCreator = null;
//		XPCOM.NS_TermEmbedding();
//		mozilla = false;
//	}
}

void onFocusGained(Event e) {
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIWebBrowserFocus.NS_IWEBBROWSERFOCUS_IID, result);
	if (rc != XPCOM.NS_OK) error(rc);
	if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
	
	nsIWebBrowserFocus webBrowserFocus = new nsIWebBrowserFocus(result[0]);
	rc = webBrowserFocus.Activate();
	if (rc != XPCOM.NS_OK) error(rc);
	webBrowserFocus.Release();
}
	
void onFocusLost(Event e) {
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIWebBrowserFocus.NS_IWEBBROWSERFOCUS_IID, result);
	if (rc != XPCOM.NS_OK) error(rc);
	if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
	
	nsIWebBrowserFocus webBrowserFocus = new nsIWebBrowserFocus(result[0]);
	rc = webBrowserFocus.Deactivate();
	if (rc != XPCOM.NS_OK) error(rc);
	webBrowserFocus.Release();
}

void onResize() {
	Rectangle rect = getClientArea();
	int width = Math.max(2, rect.width);
	int height = Math.max(2, rect.height);

	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIBaseWindow.NS_IBASEWINDOW_IID, result);
	if (rc != XPCOM.NS_OK) error(rc);
	if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
	
	nsIBaseWindow baseWindow = new nsIBaseWindow(result[0]);
	rc = baseWindow.SetPositionAndSize(0, 0, width,  height, true);
	if (rc != XPCOM.NS_OK) error(rc);
	baseWindow.Release();
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
	if (!IsLinux) return;
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID, result);
	if (rc != XPCOM.NS_OK) error(rc);
	if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
	
	nsIWebNavigation webNavigation = new nsIWebNavigation(result[0]);		 	
	rc = webNavigation.Reload(nsIWebNavigation.LOAD_FLAGS_NONE);
	/*
	* Feature in Mozilla.  Reload returns an error code NS_ERROR_INVALID_POINTER
	* when it is called immediately after a request to load a new document using
	* LoadURI.  The workaround is to ignore this error code. 
	*/
	if (rc != XPCOM.NS_OK && rc != XPCOM.NS_ERROR_INVALID_POINTER) error(rc);	
	webNavigation.Release();
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
	if (!IsLinux) return false;
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID, result);
	if (rc != XPCOM.NS_OK) error(rc);
	if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);

	nsIWebNavigation webNavigation = new nsIWebNavigation(result[0]);
	/*
	* Note.  Stop any pending request that uses the html content.  This is required
	* to avoid displaying a blank page as a result of consecutive calls to
	* setText.  The previous request would otherwise render the new html content
	* and reset the html field before the browser actually navigates to the blank
	* page as requested below.
	*/
	if (this.html != null) {
		rc = webNavigation.Stop(nsIWebNavigation.STOP_ALL);
		if (rc != XPCOM.NS_OK) error(rc);
	}
	this.html = html;
	char[] arg = "about:blank".toCharArray(); //$NON-NLS-1$
	char[] c = new char[arg.length+1];
	System.arraycopy(arg,0,c,0,arg.length);
	rc = webNavigation.LoadURI(c, nsIWebNavigation.LOAD_FLAGS_NONE, 0, 0, 0);
	onFocusGained(null); // Fixes the keyboard input Tag issues
	webNavigation.Release();
	return rc == XPCOM.NS_OK;
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
	if (!IsLinux) return false;
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID, result);
	if (rc != XPCOM.NS_OK) error(rc);
	if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);

	nsIWebNavigation webNavigation = new nsIWebNavigation(result[0]);
    char[] arg = url.toCharArray(); 
    char[] c = new char[arg.length+1];
    System.arraycopy(arg,0,c,0,arg.length);
	rc = webNavigation.LoadURI(c, nsIWebNavigation.LOAD_FLAGS_NONE, 0, 0, 0);
	onFocusGained(null); // Fixes the keyboard input Tag issues
	webNavigation.Release();
	return rc == XPCOM.NS_OK;
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
	if (!IsLinux) return;
	int[] result = new int[1];
	int rc = webBrowser.QueryInterface(nsIWebNavigation.NS_IWEBNAVIGATION_IID, result);
	if (rc != XPCOM.NS_OK) error(rc);
	if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
	
	nsIWebNavigation webNavigation = new nsIWebNavigation(result[0]);	 	
	rc = webNavigation.Stop(nsIWebNavigation.STOP_ALL);
	if (rc != XPCOM.NS_OK) error(rc);
	webNavigation.Release();
}

/* nsISupports */

int QueryInterface(int riid, int ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;

	nsID guid = new nsID();
	XPCOM.memmove(guid, riid, nsID.sizeof);

	if (guid.Equals(nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove(ppvObject, new int[] {supports.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIWeakReference.NS_IWEAKREFERENCE_IID)) {
		XPCOM.memmove(ppvObject, new int[] {weakReference.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIWebProgressListener.NS_IWEBPROGRESSLISTENER_IID)) {
		XPCOM.memmove(ppvObject, new int[] {webProgressListener.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIWebBrowserChrome.NS_IWEBBROWSERCHROME_IID)) {
		XPCOM.memmove(ppvObject, new int[] {webBrowserChrome.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIWebBrowserChromeFocus.NS_IWEBBROWSERCHROMEFOCUS_IID)) {
		XPCOM.memmove(ppvObject, new int[] {webBrowserChromeFocus.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIEmbeddingSiteWindow.NS_IEMBEDDINGSITEWINDOW_IID)) {
		XPCOM.memmove(ppvObject, new int[] {embeddingSiteWindow.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIInterfaceRequestor.NS_IINTERFACEREQUESTOR_IID)) {
		XPCOM.memmove(ppvObject, new int[] {interfaceRequestor.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsISupportsWeakReference.NS_ISUPPORTSWEAKREFERENCE_IID)) {
		XPCOM.memmove(ppvObject, new int[] {supportsWeakReference.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIContextMenuListener.NS_ICONTEXTMENULISTENER_IID)) {
		XPCOM.memmove(ppvObject, new int[] {contextMenuListener.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIURIContentListener.NS_IURICONTENTLISTENER_IID)) {
		XPCOM.memmove(ppvObject, new int[] {uriContentListener.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	XPCOM.memmove(ppvObject, new int[] {0}, 4);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}

int AddRef() {
	refCount++;
	return refCount;
}

int Release() {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces();
	return refCount;
}

/* nsIWeakReference */	
	
int QueryReferent(int riid, int ppvObject) {
	return QueryInterface(riid,ppvObject);
}

/* nsIInterfaceRequestor */

int GetInterface(int riid,int ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID();
	XPCOM.memmove(guid, riid, nsID.sizeof);
	if (guid.Equals(nsIDOMWindow.NS_IDOMWINDOW_IID)) {
		int[] aContentDOMWindow = new int[1];
		int rc = webBrowser.GetContentDOMWindow(aContentDOMWindow);
		if (rc != XPCOM.NS_OK) error(rc);
		if (aContentDOMWindow[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
		XPCOM.memmove(ppvObject, aContentDOMWindow, 4);
		return rc;
	}
	return QueryInterface(riid,ppvObject);
}

int GetWeakReference(int ppvObject) {
	XPCOM.memmove(ppvObject, new int[] {weakReference.getAddress()}, 4);
	AddRef();
	return XPCOM.NS_OK;
}

/* nsIWebProgressListener */

int OnStateChange(int aWebProgress, int aRequest, int aStateFlags, int aStatus) {
	if ((aStateFlags & nsIWebProgressListener.STATE_IS_DOCUMENT) == 0) return XPCOM.NS_OK;
	if ((aStateFlags & nsIWebProgressListener.STATE_START) != 0) {
		if (request == 0) request = aRequest;
	} else if ((aStateFlags & nsIWebProgressListener.STATE_REDIRECTING) != 0) {
		if (request == aRequest) request = 0;
	} else if ((aStateFlags & nsIWebProgressListener.STATE_STOP) != 0) {
		if (html != null) {
			byte[] data = html.getBytes();
			html = null;

			/* render HTML in memory */
			String contentType = "text/html"; //$NON-NLS-1$
			
			InputStream inputStream = new InputStream(data);
			inputStream.AddRef();

			int[] result = new int[1];
			int rc = webBrowser.QueryInterface(nsIInterfaceRequestor.NS_IINTERFACEREQUESTOR_IID, result);
			if (rc != XPCOM.NS_OK) error(rc);
			if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
	
			nsIInterfaceRequestor interfaceRequestor = new nsIInterfaceRequestor(result[0]);
			result[0] = 0;
			rc = interfaceRequestor.GetInterface(nsIContentViewerContainer.NS_ICONTENTVIEWERCONTAINER_IID, result);
			if (rc != XPCOM.NS_OK) error(rc);
			if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
			interfaceRequestor.Release();
	
			nsIContentViewerContainer contentViewerContainer = new nsIContentViewerContainer(result[0]);
			result[0] = 0;
	
			rc = XPCOM.NS_GetServiceManager(result);
			if (rc != XPCOM.NS_OK) error(rc);
			if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
		
			nsIServiceManager serviceManager = new nsIServiceManager(result[0]);
			result[0] = 0;
			rc = serviceManager.GetService(XPCOM.NS_IOSERVICE_CID, nsIIOService.NS_IIOSERVICE_IID, result);
			if (rc != XPCOM.NS_OK) error(rc);
			if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);		
	
			nsIIOService ioService = new nsIIOService(result[0]);
			result[0] = 0;
			byte[] aString = "about:blank".getBytes(); //$NON-NLS-1$
			int aSpec = XPCOM.nsCString_new(aString, aString.length);
			rc = ioService.NewURI(aSpec, null, 0, result);
			XPCOM.nsCString_delete(aSpec);
			if (rc != XPCOM.NS_OK) error(rc);
			if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
			ioService.Release();
	
			nsIURI uri = new nsIURI(result[0]);
			result[0] = 0;
			rc = XPCOM.NS_GetComponentManager(result);
			if (rc != XPCOM.NS_OK) error(rc);
			if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
	
			nsIComponentManager componentManager = new nsIComponentManager(result[0]);
			result[0] = 0;
			rc = componentManager.CreateInstance(XPCOM.NS_LOADGROUP_CID, 0, nsILoadGroup.NS_ILOADGROUP_IID, result);
			if (rc != XPCOM.NS_OK) error(rc);
			if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
			nsILoadGroup loadGroup = new nsILoadGroup(result[0]);
			result[0] = 0;
			rc = componentManager.CreateInstance(XPCOM.NS_INPUTSTREAMCHANNEL_CID, 0, nsIInputStreamChannel.NS_IINPUTSTREAMCHANNEL_IID, result);
			if (rc != XPCOM.NS_OK) error(rc);
			if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);	
			nsIInputStreamChannel inputStreamChannel = new nsIInputStreamChannel(result[0]);
			result[0] = 0;
			componentManager.Release();	
			
			rc = inputStreamChannel.SetURI(uri.getAddress());
			if (rc != XPCOM.NS_OK) error(rc);
			rc = inputStreamChannel.SetContentStream(inputStream.getAddress());
			if (rc != XPCOM.NS_OK) error(rc);
			byte[] buffer = contentType.getBytes();
			byte[] contentTypeBuffer = new byte[buffer.length + 1];
			System.arraycopy(buffer, 0, contentTypeBuffer, 0, buffer.length);
			int aContentType = XPCOM.nsCString_new(contentTypeBuffer, contentTypeBuffer.length);
			rc = inputStreamChannel.SetContentType(aContentType);
			XPCOM.nsCString_delete(aContentType);
			if (rc != XPCOM.NS_OK) error(rc);
			byte[] contentCharsetBuffer = new byte[1];
			int aContentCharset = XPCOM.nsCString_new(contentCharsetBuffer, contentCharsetBuffer.length);
			rc = inputStreamChannel.SetContentCharset(aContentCharset);
			XPCOM.nsCString_delete(aContentCharset);
			if (rc != XPCOM.NS_OK) error(rc);
			rc = inputStreamChannel.SetLoadGroup(loadGroup.getAddress());
			if (rc != XPCOM.NS_OK) error(rc);
	
			buffer = XPCOM.NS_CATEGORYMANAGER_CONTRACTID.getBytes();
			byte[] aContractID = new byte[buffer.length + 1];
			System.arraycopy(buffer, 0, aContractID, 0, buffer.length);
			rc = serviceManager.GetServiceByContractID(aContractID, nsICategoryManager.NS_ICATEGORYMANAGER_IID, result);
			if (rc != XPCOM.NS_OK) error(rc);
			if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);		
	
			nsICategoryManager categoryManager = new nsICategoryManager(result[0]);
			result[0] = 0;
			buffer = "Gecko-Content-Viewers".getBytes(); //$NON-NLS-1$
			byte[] aCategory = new byte[buffer.length + 1];
			System.arraycopy(buffer, 0, aCategory, 0, buffer.length);
			rc = categoryManager.GetCategoryEntry(aCategory, contentTypeBuffer, result);
			if (rc != XPCOM.NS_OK) error(rc);
			if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
			categoryManager.Release();
	
			int length = XPCOM.strlen(result[0]);
			aContractID = new byte[length + 1];
			XPCOM.memmove(aContractID, result[0], length);
			rc = serviceManager.GetServiceByContractID(aContractID, nsIDocumentLoaderFactory.NS_IDOCUMENTLOADERFACTORY_IID, result);
			if (rc != XPCOM.NS_OK) error(rc);
			if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);	
	
			nsIDocumentLoaderFactory documentLoaderFactory = new nsIDocumentLoaderFactory(result[0]);
			result[0] = 0;
			buffer = "view".getBytes(); //$NON-NLS-1$
			byte[] aCommand = new byte[buffer.length + 1];
			System.arraycopy(buffer, 0, aCommand, 0, buffer.length);
			int[] aDocListenerResult = new int[1];
			rc = documentLoaderFactory.CreateInstance(aCommand, inputStreamChannel.getAddress(), loadGroup.getAddress(),
					contentTypeBuffer, contentViewerContainer.getAddress(), 0, aDocListenerResult, result);
			if (rc != XPCOM.NS_OK) error(rc);
			if (aDocListenerResult[0] == 0) error(XPCOM.NS_NOINTERFACE);
			if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
			documentLoaderFactory.Release();
	
			nsIContentViewer contentViewer = new nsIContentViewer(result[0]);
			nsIStreamListener streamListener = new nsIStreamListener(aDocListenerResult[0]);
			result[0] = 0;
			rc = contentViewer.SetContainer(contentViewerContainer.getAddress());
			if (rc != XPCOM.NS_OK) error(rc);
			rc = contentViewerContainer.Embed(contentViewer.getAddress(), aCommand, 0);
			if (rc != XPCOM.NS_OK) error(rc);
			contentViewer.Release();
	
			rc = inputStreamChannel.QueryInterface(nsIRequest.NS_IREQUEST_IID, result);
			if (rc != XPCOM.NS_OK) error(rc);
			if (result[0] == 0) error(XPCOM.NS_NOINTERFACE);
	
			nsIRequest request = new nsIRequest(result[0]);
			result[0] = 0;
			rc = streamListener.OnStartRequest(request.getAddress(), 0);
			if (rc != XPCOM.NS_OK) error(rc);
	
			/* append */
			rc = streamListener.OnDataAvailable(request.getAddress(), 0, inputStream.getAddress(), 0, data.length);
			
			/*
			* Note.   Mozilla returns a NS_ERROR_HTMLPARSER_UNRESOLVEDDTD if the given content
			* cannot be rendered as HTML.  Silently ignore this error. 
			*/
			if (rc != XPCOM.NS_ERROR_HTMLPARSER_UNRESOLVEDDTD && rc != XPCOM.NS_OK) error(rc);
	
			/* close */
			rc = streamListener.OnStopRequest(request.getAddress(), 0, XPCOM.NS_OK);
			if (rc != XPCOM.NS_ERROR_HTMLPARSER_UNRESOLVEDDTD && rc != XPCOM.NS_OK) error(rc);
	
			request.Release();
			streamListener.Release();
			serviceManager.Release();
			inputStreamChannel.Release();
			loadGroup.Release();
			uri.Release();
			contentViewerContainer.Release();
			inputStream.Release();
		}

		/*
		* Feature on Mozilla.  When a request is redirected (STATE_REDIRECTING),
		* it never reaches the state STATE_STOP and it is replaced with a new request.
		* The new request is received when it is in the state STATE_STOP.
		* To handle this case,  the variable request is set to 0 when the corresponding
		* request is redirected. The following request received with the state STATE_STOP
		* - the new request resulting from the redirection - is used to send
		* the ProgressListener.completed event.
		*/
		if (request == aRequest || request == 0) {
			request = 0;
			StatusTextEvent event = new StatusTextEvent(this);
			event.display = getDisplay();
			event.widget = this;
			event.text = ""; //$NON-NLS-1$
			for (int i = 0; i < statusTextListeners.length; i++)
				statusTextListeners[i].changed(event);
			
			ProgressEvent event2 = new ProgressEvent(this);
			event2.display = getDisplay();
			event2.widget = this;
			for (int i = 0; i < progressListeners.length; i++)
				progressListeners[i].completed(event2);
		}
	}
	return XPCOM.NS_OK;
}	

int OnProgressChange(int aWebProgress, int aRequest, int aCurSelfProgress, int aMaxSelfProgress, int aCurTotalProgress, int aMaxTotalProgress) {
	if (progressListeners.length == 0) return XPCOM.NS_OK;
	
	int total = aMaxTotalProgress;
	if (total <= 0) total = Integer.MAX_VALUE;
	ProgressEvent event = new ProgressEvent(this);
	event.display = getDisplay();
	event.widget = this;
	event.current = aCurTotalProgress;
	event.total = aMaxTotalProgress;
	for (int i = 0; i < progressListeners.length; i++)
		progressListeners[i].changed(event);			
	return XPCOM.NS_OK;
}		

int OnLocationChange(int aWebProgress, int aRequest, int aLocation) {
	if (locationListeners.length == 0) return XPCOM.NS_OK;
	/*
	* Bug in Mozilla.  The argument aRequest is always null.  The fix is
	* to compare the URI aLocation with the current URI.
	*/
	nsIURI location = new nsIURI(aLocation);
	int aSpec = XPCOM.nsCString_new();
	location.GetSpec(aSpec);
	int length = XPCOM.nsCString_Length(aSpec);
	int buffer = XPCOM.nsCString_get(aSpec);
	buffer = XPCOM.nsCString_get(aSpec);
	byte[] dest = new byte[length + 1];
	XPCOM.memmove(dest, buffer, length);
	XPCOM.nsCString_delete(aSpec);

	int[] aContentDOMWindow = new int[1];
	int rc = webBrowser.GetContentDOMWindow(aContentDOMWindow);
	if (rc != XPCOM.NS_OK) error(rc);
	if (aContentDOMWindow[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);

	nsIDOMWindow domWindow = new nsIDOMWindow(aContentDOMWindow[0]);
	int[] result = new int[1];
	rc = domWindow.QueryInterface(nsIDOMWindowInternal.NS_IDOMWINDOWINTERNAL_IID, result);
	if (rc != XPCOM.NS_OK) error(rc);
	if (result[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
	domWindow.Release();
         
	nsIDOMWindowInternal domWindowInternal = new nsIDOMWindowInternal(result[0]);
	int[] aCurrentLocation = new int[1];  
	rc = domWindowInternal.GetLocation(aCurrentLocation);
	if (rc != XPCOM.NS_OK) error(rc);
	if (aCurrentLocation[0] == 0) error(XPCOM.NS_ERROR_NO_INTERFACE);
	domWindowInternal.Release();

	nsIDOMLocation domLocation = new nsIDOMLocation(aCurrentLocation[0]); 
	nsString _retval = new nsString();
	rc = domLocation.ToString(_retval.getAddress());
	if (rc != XPCOM.NS_OK) error(rc);
	domLocation.Release();  
	
	int nsString = XPCOM.nsString_new();
	XPCOM.nsString_AssignWithConversion(nsString, dest);
	boolean send = XPCOM.nsString_Equals(_retval.getAddress(), nsString);
	XPCOM.nsString_delete(nsString);
	_retval.dispose();
		
	if (send) {
		LocationEvent event = new LocationEvent(this);
		event.display = getDisplay();
		event.widget = this;
		event.location = new String(dest);
		for (int i = 0; i < locationListeners.length; i++)
			locationListeners[i].changed(event);
	}
	return XPCOM.NS_OK;
}
  
int OnStatusChange(int aWebProgress, int aRequest, int aStatus, int aMessage) {
	if (statusTextListeners.length == 0) return XPCOM.NS_OK;
	
	StatusTextEvent event = new StatusTextEvent(this);
	event.display = getDisplay();
	event.widget = this;
	int length = XPCOM.nsCRT_strlen_PRUnichar(aMessage);
	char[] dest = new char[length];
	XPCOM.memmove(dest, aMessage, length * 2);
	event.text = new String(dest);
	for (int i = 0; i < statusTextListeners.length; i++)
		statusTextListeners[i].changed(event);

	return XPCOM.NS_OK;
}		

int OnSecurityChange(int aWebProgress, int aRequest, int state) {
	return XPCOM.NS_OK;
}

/* nsIWebBrowserChrome */

int SetStatus(int statusType, int status) {
	StatusTextEvent event = new StatusTextEvent(this);
	event.display = getDisplay();
	event.widget = this;
	int length = XPCOM.nsCRT_strlen_PRUnichar(status);
	char[] dest = new char[length];
	XPCOM.memmove(dest, status, length * 2);
	String string = new String(dest);
	if (string == null) string = ""; //$NON-NLS-1$
	event.text = string;
	for (int i = 0; i < statusTextListeners.length; i++)
		statusTextListeners[i].changed(event);	
	return XPCOM.NS_OK;
}		

int GetWebBrowser(int aWebBrowser) {
	int[] ret = new int[1];	
	if (webBrowser != null) {
		webBrowser.AddRef();
		ret[0] = webBrowser.getAddress();	
	}
	XPCOM.memmove(aWebBrowser, ret, 4);
	return XPCOM.NS_OK;
}

int SetWebBrowser(int aWebBrowser) {
	if (webBrowser != null) webBrowser.Release();
	webBrowser = aWebBrowser != 0 ? new nsIWebBrowser(aWebBrowser) : null;  				
	return XPCOM.NS_OK;
}
   
int GetChromeFlags(int aChromeFlags) {
	int[] ret = new int[1];
	ret[0] = chromeFlags;
	XPCOM.memmove(aChromeFlags, ret, 4);
	return XPCOM.NS_OK;
}

int SetChromeFlags(int aChromeFlags) {
	chromeFlags = aChromeFlags;
	return XPCOM.NS_OK;
}
   
int DestroyBrowserWindow() {
	WindowEvent newEvent = new WindowEvent(this);
	newEvent.display = getDisplay();
	newEvent.widget = this;
	for (int i = 0; i < closeWindowListeners.length; i++)
		closeWindowListeners[i].close(newEvent);
	/*
	* Note on Mozilla.  The DestroyBrowserWindow notification cannot be cancelled.
	* The browser widget cannot be used after this notification has been received.
	* The application is advised to close the window hosting the browser widget.
	* The browser widget must be disposed in all cases.
	*/
	dispose();
	return XPCOM.NS_OK;
}
   	
int SizeBrowserTo(int aCX, int aCY) {
	size = new Point(aCX, aCY);
	return XPCOM.NS_OK;
}

int ShowAsModal() {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
   
int IsWindowModal(int retval) {
	// no modal loop
	XPCOM.memmove(retval, new int[] {0}, 4);
	return XPCOM.NS_OK;
}
   
int ExitModalEventLoop(int aStatus) {
	return XPCOM.NS_OK;
}

/* nsIEmbeddingSiteWindow */ 
   
int SetDimensions(int flags, int x, int y, int cx, int cy) {
	if (flags == nsIEmbeddingSiteWindow.DIM_FLAGS_POSITION) location = new Point(x, y);
	return XPCOM.NS_OK;   	
}	

int GetDimensions(int flags, int x, int y, int cx, int cy) {
	return XPCOM.NS_OK;     	
}	

int SetFocus() {
	return XPCOM.NS_OK;     	
}	

int GetVisibility(int value) {
	return XPCOM.NS_OK;     	
}
   
int SetVisibility(int value) {
	WindowEvent event = new WindowEvent(this);
	event.display = getDisplay();
	event.widget = this;
	if (value == 1) {
		event.location = location;
		event.size = size;
		for (int i = 0; i < visibilityWindowListeners.length; i++)
			visibilityWindowListeners[i].show(event);
		location = null;
		size = null;
	} else {
		for (int i = 0; i < visibilityWindowListeners.length; i++)
			visibilityWindowListeners[i].hide(event);
	}
	return XPCOM.NS_OK;     	
}

int GetTitle(int value) {
	return XPCOM.NS_OK;     	
}
 
int SetTitle(int value) {
	return XPCOM.NS_OK;     	
}

int GetSiteWindow(int siteWindow) {
	return XPCOM.NS_OK;     	
}  
 
/* nsIWebBrowserChromeFocus */

int FocusNextElement() {
	traverse(SWT.TRAVERSE_TAB_NEXT);
	return XPCOM.NS_OK;  
}

int FocusPrevElement() {
	traverse(SWT.TRAVERSE_TAB_PREVIOUS);
	return XPCOM.NS_OK;     	
}

/* nsIContextMenuListener */

int OnShowContextMenu(int aContextFlags, int aEvent, int aNode) {
	return XPCOM.NS_OK;     	
}

/* nsIURIContentListener */

int OnStartURIOpen(int aURI, int retval) {
	if (locationListeners.length == 0) return XPCOM.NS_OK;
	
	nsIURI location = new nsIURI(aURI);
	int aSpec = XPCOM.nsCString_new();
	location.GetSpec(aSpec);
	int length = XPCOM.nsCString_Length(aSpec);
	int buffer = XPCOM.nsCString_get(aSpec);
	buffer = XPCOM.nsCString_get(aSpec);
	byte[] dest = new byte[length + 1];
	XPCOM.memmove(dest, buffer, length);
	XPCOM.nsCString_delete(aSpec);
	
	boolean cancel = false;
	if (request == 0) {
		LocationEvent event = new LocationEvent(this);
		event.location = new String(dest);
		for (int i = 0; i < locationListeners.length; i++)
			locationListeners[i].changing(event);
		cancel = event.cancel;
	}
	XPCOM.memmove(retval, new int[] {cancel ? 1 : 0}, 4);
	return XPCOM.NS_OK;
}

int DoContent(int aContentType, int aIsContentPreferred, int aRequest, int aContentHandler, int retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int IsPreferred(int aContentType, int aDesiredContentType, int retval) {
	XPCOM.memmove(retval, new int[] {1}, 4);
	return XPCOM.NS_OK;
}

int CanHandleContent(int aContentType, int aIsContentPreferred, int aDesiredContentType, int retval) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetLoadCookie(int aLoadCookie) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int SetLoadCookie(int aLoadCookie) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}

int GetParentContentListener(int aParentContentListener) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
	
int SetParentContentListener(int aParentContentListener) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
}