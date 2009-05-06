/*******************************************************************************
 * Copyright (c) 2003, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

import org.eclipse.swt.internal.*;

public class Cocoa extends Platform {
		
static {
	Library.loadLibrary("swt-cocoa"); //$NON-NLS-1$
	WebInitForCarbon();
}
	
/* Objective-C class ids */
public static final int C_NSHTTPCookie = Cocoa.objc_getClass("NSHTTPCookie"); //$NON-NLS-1$
public static final int C_NSHTTPCookieStorage = Cocoa.objc_getClass("NSHTTPCookieStorage"); //$NON-NLS-1$
public static final int C_NSNotificationCenter = Cocoa.objc_getClass("NSNotificationCenter"); //$NON-NLS-1$
public static final int C_NSNumber = Cocoa.objc_getClass("NSNumber"); //$NON-NLS-1$
public static final int C_NSURL = Cocoa.objc_getClass("NSURL"); //$NON-NLS-1$
public static final int C_NSURLRequest = Cocoa.objc_getClass("NSURLRequest"); //$NON-NLS-1$
public static final int C_NSURLCredential = Cocoa.objc_getClass("NSURLCredential"); //$NON-NLS-1$
public static final int C_WebKitDelegate = Cocoa.objc_getClass("WebKitDelegate"); //$NON-NLS-1$
public static final int C_WebDownload = Cocoa.objc_getClass("WebDownload"); //$NON-NLS-1$
public static final int C_WebView = Cocoa.objc_getClass("WebView"); //$NON-NLS-1$
public static final int C_NSStatusBar = Cocoa.objc_getClass("NSStatusBar"); //$NON-NLS-1$
public static final int C_NSImage = Cocoa.objc_getClass("NSImage"); //$NON-NLS-1$
public static final int C_NSGraphicsContext = Cocoa.objc_getClass("NSGraphicsContext"); //$NON-NLS-1$
public static final int C_NSStatusItemImageView = Cocoa.objc_getClass("NSStatusItemImageView"); //$NON-NLS-1$
public static final int C_NSApplication = Cocoa.objc_getClass("NSApplication"); //$NON-NLS-1$
public static final int C_NSCursor = Cocoa.objc_getClass("NSCursor"); //$NON-NLS-1$
public static final int C_NSWindow = Cocoa.objc_getClass("NSWindow"); //$NON-NLS-1$
public static final int C_NSBitmapImageRep = Cocoa.objc_getClass("NSBitmapImageRep"); //$NON-NLS-1$
public static final int C_NSImageView = Cocoa.objc_getClass("NSImageView"); //$NON-NLS-1$
public static final int C_WebPreferences = Cocoa.objc_getClass("WebPreferences"); //$NON-NLS-1$
public static final int C_NSBezierPath = Cocoa.objc_getClass("NSBezierPath"); //$NON-NLS-1$
public static final int C_NSButton = Cocoa.objc_getClass("NSButton"); //$NON-NLS-1$
public static final int C_NSObject = Cocoa.objc_getClass("NSObject"); //$NON-NLS-1$
public static final int C_NSString = Cocoa.objc_getClass("NSString"); //$NON-NLS-1$
public static final int C_NSMutableArray = Cocoa.objc_getClass("NSMutableArray"); //$NON-NLS-1$
public static final int C_NSMutableDictionary = Cocoa.objc_getClass("NSMutableDictionary"); //$NON-NLS-1$
public static final int C_WebPanelAuthenticationHandler = Cocoa.objc_getClass("WebPanelAuthenticationHandler"); //$NON-NLS-1$
public static final int C_WebScriptObject = Cocoa.objc_getClass("WebScriptObject"); //$NON-NLS-1$
public static final int C_WebUndefined = Cocoa.objc_getClass("WebUndefined"); //$NON-NLS-1$

/* Objective-C method selectors */
public static final int S_absoluteString = Cocoa.sel_registerName("absoluteString"); //$NON-NLS-1$
public static final int S_addObserver_selector_name_object = Cocoa.sel_registerName("addObserver:selector:name:object:"); //$NON-NLS-1$
public static final int S_addObject = Cocoa.sel_registerName("addObject:"); //$NON-NLS-1$
public static final int S_alloc = Cocoa.sel_registerName("alloc"); //$NON-NLS-1$
public static final int S_arrayWithCapacity = Cocoa.sel_registerName("arrayWithCapacity:"); //$NON-NLS-1$
public static final int S_autorelease = Cocoa.sel_registerName("autorelease"); //$NON-NLS-1$
public static final int S_boolValue = Cocoa.sel_registerName("boolValue"); //$NON-NLS-1$
public static final int S_cancel = Cocoa.sel_registerName("cancel"); //$NON-NLS-1$
public static final int S_cancelAuthenticationChallenge = Cocoa.sel_registerName("cancelAuthenticationChallenge:"); //$NON-NLS-1$
public static final int S_canGoBack = Cocoa.sel_registerName("canGoBack"); //$NON-NLS-1$
public static final int S_canGoForward = Cocoa.sel_registerName("canGoForward"); //$NON-NLS-1$
public static final int S_canShowMIMEType = Cocoa.sel_registerName("canShowMIMEType:"); //$NON-NLS-1$
public static final int S_chooseFilename = Cocoa.sel_registerName("chooseFilename:"); //$NON-NLS-1$
public static final int S_compare = Cocoa.sel_registerName("compare:"); //$NON-NLS-1$
public static final int S_cookies = Cocoa.sel_registerName("cookies"); //$NON-NLS-1$
public static final int S_cookiesWithResponseHeaderFields = Cocoa.sel_registerName("cookiesWithResponseHeaderFields:forURL:"); //$NON-NLS-1$
public static final int S_cookiesForURL = Cocoa.sel_registerName("cookiesForURL:"); //$NON-NLS-1$
public static final int S_copy = Cocoa.sel_registerName("copy:"); //$NON-NLS-1$
public static final int S_count = Cocoa.sel_registerName("count"); //$NON-NLS-1$
public static final int S_credentialWithUser = Cocoa.sel_registerName("credentialWithUser:password:persistence:"); //$NON-NLS-1$
public static final int S_cut = Cocoa.sel_registerName("cut:"); //$NON-NLS-1$
public static final int S_dataSource = Cocoa.sel_registerName("dataSource"); //$NON-NLS-1$
public static final int S_defaultCenter = Cocoa.sel_registerName("defaultCenter"); //$NON-NLS-1$
public static final int S_deleteCookie = Cocoa.sel_registerName("deleteCookie:"); //$NON-NLS-1$
public static final int S_dictionaryWithCapacity = Cocoa.sel_registerName("dictionaryWithCapacity:"); //$NON-NLS-1$
public static final int S_documentSource = Cocoa.sel_registerName("documentSource"); //$NON-NLS-1$
public static final int S_doubleValue = Cocoa.sel_registerName("doubleValue"); //$NON-NLS-1$
public static final int S_download = Cocoa.sel_registerName("download"); //$NON-NLS-1$
public static final int S_goBack = Cocoa.sel_registerName("goBack:"); //$NON-NLS-1$
public static final int S_goForward = Cocoa.sel_registerName("goForward:"); //$NON-NLS-1$
public static final int S_handleNotification = Cocoa.sel_registerName("handleNotification:"); //$NON-NLS-1$
public static final int S_hasPassword = Cocoa.sel_registerName("hasPassword"); //$NON-NLS-1$
public static final int S_host = Cocoa.sel_registerName("host"); //$NON-NLS-1$
public static final int S_ignore = Cocoa.sel_registerName("ignore"); //$NON-NLS-1$
public static final int S_initialRequest = Cocoa.sel_registerName("initialRequest"); //$NON-NLS-1$
public static final int S_initWithFrame_frameName_groupName = Cocoa.sel_registerName("initWithFrame:frameName:groupName:"); //$NON-NLS-1$
public static final int S_initWithProc = Cocoa.sel_registerName("initWithProc:user_data:"); //$NON-NLS-1$
public static final int S_intValue = Cocoa.sel_registerName("intValue"); //$NON-NLS-1$
public static final int S_isFileURL = Cocoa.sel_registerName("isFileURL"); //$NON-NLS-1$
public static final int S_isKindOfClass = Cocoa.sel_registerName("isKindOfClass:"); //$NON-NLS-1$
public static final int S_isSessionOnly = Cocoa.sel_registerName("isSessionOnly"); //$NON-NLS-1$
public static final int S_loadHTMLStringbaseURL = Cocoa.sel_registerName("loadHTMLString:baseURL:"); //$NON-NLS-1$
public static final int S_loadRequest = Cocoa.sel_registerName("loadRequest:"); //$NON-NLS-1$
public static final int S_length = Cocoa.sel_registerName("length"); //$NON-NLS-1$
public static final int S_mainFrame = Cocoa.sel_registerName("mainFrame"); //$NON-NLS-1$
public static final int S_mainMenu = Cocoa.sel_registerName("mainMenu"); //$NON-NLS-1$
public static final int S_mutableCopy = Cocoa.sel_registerName("mutableCopy"); //$NON-NLS-1$
public static final int S_name = Cocoa.sel_registerName("name"); //$NON-NLS-1$
public static final int S_numberWithBool = Cocoa.sel_registerName("numberWithBool:"); //$NON-NLS-1$
public static final int S_numberWithDouble = Cocoa.sel_registerName("numberWithDouble:"); //$NON-NLS-1$
public static final int S_numberWithInt = Cocoa.sel_registerName("numberWithInt:"); //$NON-NLS-1$
public static final int S_objectAtIndex = Cocoa.sel_registerName("objectAtIndex:"); //$NON-NLS-1$
public static final int S_pageTitle = Cocoa.sel_registerName("pageTitle"); //$NON-NLS-1$
public static final int S_password = Cocoa.sel_registerName("password"); //$NON-NLS-1$
public static final int S_paste = Cocoa.sel_registerName("paste:"); //$NON-NLS-1$
public static final int S_port = Cocoa.sel_registerName("port"); //$NON-NLS-1$
public static final int S_previousFailureCount = Cocoa.sel_registerName("previousFailureCount"); //$NON-NLS-1$
public static final int S_proposedCredential = Cocoa.sel_registerName("proposedCredential"); //$NON-NLS-1$
public static final int S_protectionSpace = Cocoa.sel_registerName("protectionSpace"); //$NON-NLS-1$
public static final int S_provisionalDataSource = Cocoa.sel_registerName("provisionalDataSource"); //$NON-NLS-1$
public static final int S_realm = Cocoa.sel_registerName("realm"); //$NON-NLS-1$
public static final int S_release = Cocoa.sel_registerName("release"); //$NON-NLS-1$
public static final int S_reload = Cocoa.sel_registerName("reload:"); //$NON-NLS-1$
public static final int S_retain = Cocoa.sel_registerName("retain"); //$NON-NLS-1$
public static final int S_removeObserver_name_object = Cocoa.sel_registerName("removeObserver:name:object:"); //$NON-NLS-1$
public static final int S_removeObserver = Cocoa.sel_registerName("removeObserver:"); //$NON-NLS-1$
public static final int S_representation = Cocoa.sel_registerName("representation"); //$NON-NLS-1$
public static final int S_requestWithURL = Cocoa.sel_registerName("requestWithURL:"); //$NON-NLS-1$
public static final int S_request = Cocoa.sel_registerName("request"); //$NON-NLS-1$
public static final int S_retainCount = Cocoa.sel_registerName("retainCount"); //$NON-NLS-1$
public static final int S_sender = Cocoa.sel_registerName("sender"); //$NON-NLS-1$
public static final int S_setApplicationNameForUserAgent = Cocoa.sel_registerName("setApplicationNameForUserAgent:"); //$NON-NLS-1$
public static final int S_setCachePolicy = Cocoa.sel_registerName("setCachePolicy:"); //$NON-NLS-1$
public static final int S_setCookie = Cocoa.sel_registerName("setCookie:"); //$NON-NLS-1$
public static final int S_setDestinationAllowOverwrite = Cocoa.sel_registerName("setDestination:allowOverwrite:"); //$NON-NLS-1$
public static final int S_setDownloadDelegate = Cocoa.sel_registerName("setDownloadDelegate:"); //$NON-NLS-1$
public static final int S_setFrameLoadDelegate = Cocoa.sel_registerName("setFrameLoadDelegate:"); //$NON-NLS-1$
public static final int S_setGroupName = Cocoa.sel_registerName("setGroupName:"); //$NON-NLS-1$
public static final int S_setJavaEnabled = Cocoa.sel_registerName("setJavaEnabled:"); //$NON-NLS-1$
public static final int S_setJavaScriptEnabled = Cocoa.sel_registerName("setJavaScriptEnabled:"); //$NON-NLS-1$
public static final int S_setMainMenu = Cocoa.sel_registerName("setMainMenu:"); //$NON-NLS-1$
public static final int S_setPolicyDelegate = Cocoa.sel_registerName("setPolicyDelegate:"); //$NON-NLS-1$
public static final int S_setResourceLoadDelegate = Cocoa.sel_registerName("setResourceLoadDelegate:"); //$NON-NLS-1$
public static final int S_setStatusText = Cocoa.sel_registerName("setStatusText:"); //$NON-NLS-1$
public static final int S_setUIDelegate = Cocoa.sel_registerName("setUIDelegate:"); //$NON-NLS-1$
public static final int S_sharedApplication = Cocoa.sel_registerName("sharedApplication"); //$NON-NLS-1$
public static final int S_sharedHTTPCookieStorage = Cocoa.sel_registerName("sharedHTTPCookieStorage"); //$NON-NLS-1$
public static final int S_sharedHandler = Cocoa.sel_registerName("sharedHandler"); //$NON-NLS-1$
public static final int S_standardPreferences = Cocoa.sel_registerName("standardPreferences"); //$NON-NLS-1$
public static final int S_startAuthentication = sel_registerName("startAuthentication:window:"); //$NON-NLS-1$
public static final int S_stopLoading = Cocoa.sel_registerName("stopLoading:"); //$NON-NLS-1$
public static final int S_stringByEvaluatingJavaScriptFromString = Cocoa.sel_registerName("stringByEvaluatingJavaScriptFromString:"); //$NON-NLS-1$
public static final int S_takeStringURLFrom = Cocoa.sel_registerName("takeStringURLFrom:"); //$NON-NLS-1$
public static final int S_undefined = Cocoa.sel_registerName("undefined"); //$NON-NLS-1$
public static final int S_use = Cocoa.sel_registerName("use"); //$NON-NLS-1$
public static final int S_user = Cocoa.sel_registerName("user"); //$NON-NLS-1$
public static final int S_useCredential = Cocoa.sel_registerName("useCredential:forAuthenticationChallenge:"); //$NON-NLS-1$
public static final int S_valueForKey = Cocoa.sel_registerName("valueForKey:"); //$NON-NLS-1$
public static final int S_webFrame = Cocoa.sel_registerName("webFrame"); //$NON-NLS-1$
public static final int S_URL = Cocoa.sel_registerName("URL"); //$NON-NLS-1$
public static final int S_URLWithString = Cocoa.sel_registerName("URLWithString:"); //$NON-NLS-1$
public static final int S_fileURLWithPath = Cocoa.sel_registerName("fileURLWithPath:"); //$NON-NLS-1$
public static final int S_systemStatusBar = Cocoa.sel_registerName("systemStatusBar"); //$NON-NLS-1$
public static final int S_statusItemWithLength = Cocoa.sel_registerName("statusItemWithLength:"); //$NON-NLS-1$
public static final int S_setTitle = Cocoa.sel_registerName("setTitle:"); //$NON-NLS-1$
public static final int S_setHighlightMode = Cocoa.sel_registerName("setHighlightMode:"); //$NON-NLS-1$
public static final int S_setToolTip = Cocoa.sel_registerName("setToolTip:"); //$NON-NLS-1$
public static final int S_setImage = Cocoa.sel_registerName("setImage:"); //$NON-NLS-1$
public static final int S_removeStatusItem = Cocoa.sel_registerName("removeStatusItem:"); //$NON-NLS-1$
public static final int S_initWithSize = Cocoa.sel_registerName("initWithSize:"); //$NON-NLS-1$
public static final int S_initWithFrame = Cocoa.sel_registerName("initWithFrame:"); //$NON-NLS-1$
public static final int S_initWithProc_frame_user_data = Cocoa.sel_registerName("initWithProc:frame:user_data:"); //$NON-NLS-1$
public static final int S_lockFocus = Cocoa.sel_registerName("lockFocus"); //$NON-NLS-1$
public static final int S_unlockFocus = Cocoa.sel_registerName("unlockFocus"); //$NON-NLS-1$
public static final int S_currentContext = Cocoa.sel_registerName("currentContext"); //$NON-NLS-1$
public static final int S_graphicsPort = Cocoa.sel_registerName("graphicsPort"); //$NON-NLS-1$
public static final int S_setLength = Cocoa.sel_registerName("setLength:"); //$NON-NLS-1$
public static final int S_view = Cocoa.sel_registerName("view"); //$NON-NLS-1$
public static final int S_setView = Cocoa.sel_registerName("setView:"); //$NON-NLS-1$
public static final int S_clickCount = Cocoa.sel_registerName("clickCount"); //$NON-NLS-1$
public static final int S_drawStatusBarBackgroundInRect_withHighlight = Cocoa.sel_registerName("drawStatusBarBackgroundInRect:withHighlight:"); //$NON-NLS-1$
public static final int S_drawRect = Cocoa.sel_registerName("drawRect:"); //$NON-NLS-1$
public static final int S_setNeedsDisplay = Cocoa.sel_registerName("setNeedsDisplay:"); //$NON-NLS-1$
public static final int S_initWithImage_hotSpot = Cocoa.sel_registerName("initWithImage:hotSpot:"); //$NON-NLS-1$
public static final int S_set = Cocoa.sel_registerName("set"); //$NON-NLS-1$
public static final int S_init = Cocoa.sel_registerName("init"); //$NON-NLS-1$
public static final int S_frame = Cocoa.sel_registerName("frame"); //$NON-NLS-1$
public static final int S_window = Cocoa.sel_registerName("window"); //$NON-NLS-1$
public static final int S_makeKeyWindow = Cocoa.sel_registerName("makeKeyWindow"); //$NON-NLS-1$
public static final int S_addRepresentation = Cocoa.sel_registerName("addRepresentation:"); //$NON-NLS-1$
public static final int S_initWithBitmapDataPlanes = Cocoa.sel_registerName("initWithBitmapDataPlanes:pixelsWide:pixelsHigh:bitsPerSample:samplesPerPixel:hasAlpha:isPlanar:colorSpaceName:bitmapFormat:bytesPerRow:bitsPerPixel:"); //$NON-NLS-1$
public static final int S_bitmapData = Cocoa.sel_registerName("bitmapData"); //$NON-NLS-1$
public static final int S_modifierFlags = Cocoa.sel_registerName("modifierFlags"); //$NON-NLS-1$
public static final int S_bezierPath = Cocoa.sel_registerName("bezierPath"); //$NON-NLS-1$
public static final int S_bezierPathByFlatteningPath = Cocoa.sel_registerName("bezierPathByFlatteningPath");
public static final int S_moveToPoint = Cocoa.sel_registerName("moveToPoint:"); //$NON-NLS-1$
public static final int S_lineToPoint = Cocoa.sel_registerName("lineToPoint:"); //$NON-NLS-1$
public static final int S_curveToPoint = Cocoa.sel_registerName("curveToPoint:controlPoint1:controlPoint2:"); //$NON-NLS-1$
public static final int S_closePath = Cocoa.sel_registerName("closePath"); //$NON-NLS-1$
public static final int S_elementCount = Cocoa.sel_registerName("elementCount"); //$NON-NLS-1$
public static final int S_elementAtIndex_associatedPoints = Cocoa.sel_registerName("elementAtIndex:associatedPoints:"); //$NON-NLS-1$
public static final int S_setFlatness = Cocoa.sel_registerName("setFlatness:"); //$NON-NLS-1$
public static final int S_setDefaultFlatness = Cocoa.sel_registerName("setDefaultFlatness:"); //$NON-NLS-1$
public static final int S_convertRect_toView = Cocoa.sel_registerName("convertRect:toView:"); //$NON-N/LS-1$
public static final int S_addEventListener = Cocoa.sel_registerName("addEventListener:::"); //$NON-NLS-1$
public static final int S_altKey = Cocoa.sel_registerName("altKey"); //$NON-NLS-1$
public static final int S_button = Cocoa.sel_registerName("button"); //$NON-NLS-1$
public static final int S_charCode = Cocoa.sel_registerName("charCode"); //$NON-NLS-1$
public static final int S_clientX = Cocoa.sel_registerName("clientX"); //$NON-NLS-1$
public static final int S_clientY = Cocoa.sel_registerName("clientY"); //$NON-NLS-1$
public static final int S_ctrlKey = Cocoa.sel_registerName("ctrlKey"); //$NON-NLS-1$
public static final int S_detail = Cocoa.sel_registerName("detail"); //$NON-NLS-1$
public static final int S_DOMDocument = Cocoa.sel_registerName("DOMDocument"); //$NON-NLS-1$
public static final int S_keyCode = Cocoa.sel_registerName("keyCode"); //$NON-NLS-1$
public static final int S_metaKey = Cocoa.sel_registerName("metaKey"); //$NON-NLS-1$
public static final int S_preventDefault = Cocoa.sel_registerName("preventDefault"); //$NON-NLS-1$
public static final int S_relatedTarget = Cocoa.sel_registerName("relatedTarget"); //$NON-NLS-1$;
public static final int S_shiftKey = Cocoa.sel_registerName("shiftKey"); //$NON-NLS-1$
public static final int S_type = Cocoa.sel_registerName("type"); //$NON-NLS-1$
public static final int S_wheelDelta = Cocoa.sel_registerName("wheelDelta"); //$NON-NLS-1$
public static final int S_setValue = Cocoa.sel_registerName("setValue:forKey:"); //$NON-NLS-1$
public static final int S_webScriptValueAtIndex = Cocoa.sel_registerName("webScriptValueAtIndex:"); //$NON-NLS-1$
public static final int S_getCharacters_ = Cocoa.sel_registerName("getCharacters:"); //$NON-NLS-1$
public static final int S_objCType = Cocoa.sel_registerName("objCType"); //$NON-NLS-1$
public static final int S_setPreferences = Cocoa.sel_registerName("setPreferences:"); //$NON-NLS-1$
public static final int S_value = Cocoa.sel_registerName("value"); //$NON-NLS-1$

public static final int NSAlphaFirstBitmapFormat = 1 << 0;
public static final int NSAlphaNonpremultipliedBitmapFormat = 1 << 1;
public static final int NSControlKeyMask = 1 << 18;
public static final int NSDeviceIndependentModifierFlagsMask = 0xffff0000;

public static final int NSMoveToBezierPathElement = 0;
public static final int NSLineToBezierPathElement = 1;
public static final int NSCurveToBezierPathElement = 2;
public static final int NSClosePathBezierPathElement = 3;

public static final int NSOrderedSame = 0;

public static final int NSURLCredentialPersistenceForSession = 1;
public static final int NSURLRequestReloadIgnoringLocalCacheData = 1;

public static final int NSAllDomainsMask = 0xffff;
public static final int NSDesktopDirectory = 12;
public static final int NSDocumentDirectory = 9;
public static final int NSDownloadsDirectory = 15;

/* WebKit */
/** @param outView cast=(HIViewRef *) */
public static final native int HIWebViewCreate(int[] outView);
/** @param inView cast=(HIViewRef) */
public static final native int HIWebViewGetWebView(int inView);
public static final native void WebInitForCarbon();

/* Embed NSView in HIView */
/** @method flags=no_gen */
public static final native int HIJavaViewCreateWithCocoaView(int[] hiview, int nsview);
/** @method flags=dynamic */
public static final native int HICocoaViewCreate(int nsview, int options, int[] hiview);

/* OBJ-C runtime primitives */
/**
 * @param method cast=(Method)
 * @param aClass cast=(Class)
 * @param aSelector cast=(SEL)
 */
public static final native int /*long*/ class_getClassMethod(int /*long*/ aClass, int /*long*/ aSelector);
/** @param className cast=(const char *) */
public static final native int objc_getClass(byte[] className);
/** @param className cast=(const char *) */
public static final native int objc_getMetaClass(byte[] className);
/**
 * @param object cast=(id)
 * @param selector cast=(SEL)
 */
public static final native int objc_msgSend(int object, int selector);
/**
 * @param object cast=(id)
 * @param selector cast=(SEL)
 */
public static final native int objc_msgSend(int object, int selector, int arg0);
/**
 * @method flags=cast
 * @param object cast=(id)
 * @param selector cast=(SEL)
 */
public static final native int objc_msgSend(int object, int selector, float arg0);
/**
 * @method flags=cast
 * @param object cast=(id)
 * @param selector cast=(SEL)
 */
public static final native int objc_msgSend(int object, int selector, double arg0);
/** @method flags=cast */
public static final native int objc_msgSend(int object, int selector, char[] arg0);
/**
 * @method flags=cast
 * @param object cast=(id)
 * @param selector cast=(SEL)
 */
public static final native int objc_msgSend(int object, int selector, float arg0, float arg1);
/**
 * @param object cast=(id)
 * @param selector cast=(SEL)
 * @param arg0 flags=struct
 */
public static final native int objc_msgSend(int object, int selector, NSSize arg0);
/**
 * @param object cast=(id)
 * @param selector cast=(SEL)
 * @param arg0 flags=struct
 */
public static final native int objc_msgSend(int object, int selector, NSRect arg0);
/**
 * @param object cast=(id)
 * @param selector cast=(SEL)
 * @param arg0 flags=struct
 */
public static final native int objc_msgSend(int object, int selector, NSRect arg0, int arg1, int arg2);
/**
 * @param object cast=(id)
 * @param selector cast=(SEL)
 * @param arg1 flags=struct
 */
public static final native int objc_msgSend(int object, int selector, int arg0, NSRect arg1, int arg2);
/**
 * @param object cast=(id)
 * @param selector cast=(SEL)
 * @param arg0 flags=struct
 */
public static final native int objc_msgSend(int object, int selector, NSRect arg0, int arg1);
/**
 * @param object cast=(id)
 * @param selector cast=(SEL)
 * @param arg0 flags=struct
 */
public static final native int objc_msgSend(int object, int selector, NSPoint arg0, int arg1);
/**
 * @param object cast=(id)
 * @param selector cast=(SEL)
 * @param arg1 flags=struct
 */
public static final native int objc_msgSend(int object, int selector, int arg0, NSPoint arg1);
/**
 * @param object cast=(id)
 * @param selector cast=(SEL)
 * @param arg0 flags=struct
 */
public static final native int objc_msgSend(int object, int selector, NSPoint arg0);
/**
 * @param object cast=(id)
 * @param selector cast=(SEL)
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 * @param arg2 flags=struct
 */
public static final native int objc_msgSend(int object, int selector, NSPoint arg0, NSPoint arg1, NSPoint arg2);
/**
 * @param object cast=(id)
 * @param selector cast=(SEL)
 */
public static final native int objc_msgSend(int object, int selector, int arg0, int arg1);
/**
 * @param object cast=(id)
 * @param selector cast=(SEL)
 */
public static final native int objc_msgSend(int object, int selector, int arg0, int arg1, int arg2);
/**
 * @param object cast=(id)
 * @param selector cast=(SEL)
 */
public static final native int objc_msgSend(int object, int selector, int arg0, int arg1, int arg2, int arg3);
/**
 * @param object cast=(id)
 * @param selector cast=(SEL)
 */
public static final native int objc_msgSend(int object, int selector, int[] arg0, int arg1, int arg2, int arg3, int arg4, int arg5, int arg6, int arg7, int arg8, int arg9, int arg10);
/** @method flags=cast */
public static final native double objc_msgSend_fpret(int /*long*/ id, int /*long*/ sel);
/**
 * @param result cast=(void *)
 * @param object cast=(void *)
 * @param selector cast=(SEL)
 */
public static final native void objc_msgSend_stret(NSRect result, int object, int selector);
/**
 * @param result cast=(void *)
 * @param object cast=(void *)
 * @param selector cast=(SEL)
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSRect result, int object, int selector, NSRect arg0, int arg1);
/** @param selectorName cast=(const char *) */
public static final native int sel_registerName(byte[] selectorName);

/** @method flags=const */
public static final native int NSDeviceRGBColorSpace();
/**
 * @param directory cast=(NSSearchPathDirectory)
 * @param domainMask cast=(NSSearchPathDomainMask)
 * @param expandTilde cast=(BOOL)
 */
public static final native int NSSearchPathForDirectoriesInDomains(int directory, int domainMask, boolean expandTilde);

/** @param src cast=(void *) */
public static final native void memcpy(NSRect dest, int src, int size);
/** @param src cast=(void *) */
public static final native void memmove(NSPoint dest, int src, int size);

static byte [] ascii (String name) {
	int length = name.length ();
	char [] chars = new char [length];
	name.getChars (0, length, chars, 0);
	byte [] buffer = new byte [length + 1];
	for (int i=0; i<length; i++) {
		buffer [i] = (byte) chars [i];
	}
	return buffer;
}

static int sel_registerName(String selector) {
	return Cocoa.sel_registerName(ascii(selector));
}
	
static int objc_getClass(String className) {
	return Cocoa.objc_getClass(ascii(className));
}

}
