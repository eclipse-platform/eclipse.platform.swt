/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
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

public class OS extends C {
	static {
		Library.loadLibrary("swt-pi"); //$NON-NLS-1$
	}
	
	public static final int VERSION;
	static {
		int [] response = new int [1];
		OS.Gestalt (OS.gestaltSystemVersion, response);
		VERSION = response [0] & 0xffff;		
	}
	
	public static final int gestaltSystemVersion = ('s'<<24) + ('y'<<16) + ('s'<<8) + 'v';
	public static final int noErr = 0;
	public static final int kProcessTransformToForegroundApplication = 1;
	public static final int kAlertCautionIcon = ('c'<<24) + ('a'<<16) + ('u'<<8) + 't';
	public static final int kAlertNoteIcon = ('n'<<24) + ('o'<<16) + ('t'<<8) + 'e';
	public static final int kAlertStopIcon = ('s'<<24) + ('t'<<16) + ('o'<<8) + 'p';
	public static final int shiftKey = 1 << 9;

	public static final int /*long*/ sel_sendSearchSelection = sel_registerName("sendSearchSelection");
	public static final int /*long*/ sel_sendCancelSelection = sel_registerName("sendCancelSelection");
	public static final int /*long*/ sel_sendSelection = sel_registerName("sendSelection");
	public static final int /*long*/ sel_sendSelection_ = sel_registerName("sendSelection:");
	public static final int /*long*/ sel_sendDoubleSelection = sel_registerName("sendDoubleSelection");
	public static final int /*long*/ sel_sendVerticalSelection = sel_registerName("sendVerticalSelection");
	public static final int /*long*/ sel_sendHorizontalSelection = sel_registerName("sendHorizontalSelection");
	public static final int /*long*/ sel_timerProc_ = sel_registerName("timerProc:");
	public static final int /*long*/ sel_handleNotification_ = sel_registerName("handleNotification:");
	public static final int /*long*/ sel_callJava = sel_registerName("callJava:index:arg:");
	public static final int /*long*/ sel_quitRequested_ = sel_registerName("quitRequested:");
	public static final int /*long*/ sel_systemSettingsChanged_ = sel_registerName("systemSettingsChanged:");
	public static final int /*long*/ sel_panelDidEnd_returnCode_contextInfo_ = sel_registerName("panelDidEnd:returnCode:contextInfo:");
	
	public static final int /*long*/ sel_overwriteExistingFileCheck = sel_registerName("_overwriteExistingFileCheck:");

	public static final int /*long*/ sel_setMovable_ = OS.sel_registerName("setMovable:");

	public static final int /*long*/ sel_contextID = OS.sel_registerName("contextID");

	public static final int /*long*/ sel__drawThemeProgressArea_ = OS.sel_registerName("_drawThemeProgressArea:");
	
	public static final int /*long*/ sel__setNeedsToUseHeartBeatWindow_ = OS.sel_registerName("_setNeedsToUseHeartBeatWindow:");

	public static final int /*long*/ class_WebPanelAuthenticationHandler = OS.objc_getClass("WebPanelAuthenticationHandler");
	public static final int /*long*/ sel_sharedHandler = sel_registerName("sharedHandler");
	public static final int /*long*/ sel_startAuthentication = sel_registerName("startAuthentication:window:");

	/* These are not generated in order to avoid creating static methods on all classes */
	public static final int /*long*/ sel_isSelectorExcludedFromWebScript_ = sel_registerName("isSelectorExcludedFromWebScript:");
	public static final int /*long*/ sel_webScriptNameForSelector_ = sel_registerName("webScriptNameForSelector:");

/** JNI natives */

/** @method flags=jni */
public static final native int /*long*/ NewGlobalRef(Object object);
/**
 * @method flags=jni
 * @param globalRef cast=(jobject)
 */
public static final native void DeleteGlobalRef(int /*long*/ globalRef);
/** @method flags=no_gen */ 
public static final native Object JNIGetObject(int /*long*/ globalRef);

/** Carbon calls */

public static final native int Gestalt(int selector, int[] response);
/** @param psn cast=(ProcessSerialNumber *) */
public static final native int GetCurrentProcess(int[] psn);
/** @param psn cast=(ProcessSerialNumber *) */
public static final native int SetFrontProcess(int[] psn);
/** @param psn cast=(ProcessSerialNumber *) */
public static final native int TransformProcessType(int[] psn, int transformState);
public static final native int CPSSetProcessName(int[] psn, int /*long*/ name);
/** @method flags=dynamic */
public static final native int SetThemeCursor(int themeCursor);
/** @method flags=dynamic */
public static final native int GetCurrentButtonState();
/** @method flags=dynamic */
public static final native int GetDblTime();
/** @method flags=dynamic 
    @param  cast=(CGContextRef) */
public static final native int /*long*/ CGContextCopyPath(int /*long*/ context);
/** @method flags=dynamic */
public static final native int /*long*/ TISCopyCurrentKeyboardInputSource();
/** @method flags=dynamic 
    @param  cast=(TISInputSourceRef) 
    @param  cast=(CFStringRef) */
public static final native int /*long*/ TISGetInputSourceProperty (int /*long*/ inputSource, int /*long*/ propertyKey);
/** @method flags=no_gen */
public static final native int /*long*/ kTISPropertyUnicodeKeyLayoutData();
/**
 * @method flags=dynamic
 * @param keyLayoutPtr cast=(const UCKeyboardLayout *)
 * @param virtualKeyCode cast=(UInt16)
 * @param keyAction cast=(UInt16)
 * @param modifierKeyState cast=(UInt32)
 * @param keyboardType cast=(UInt32)
 * @param keyTranslateOptions cast=(OptionBits)
 * @param deadKeyState cast=(UInt32 *)
 * @param maxStringLength cast=(UniCharCount)
 * @param actualStringLength cast=(UniCharCount *)
 * @param unicodeString cast=(UniChar *)
 */
public static final native int UCKeyTranslate (int /*long*/ keyLayoutPtr, short virtualKeyCode, short keyAction, int modifierKeyState, int keyboardType, int keyTranslateOptions, int[] deadKeyState, int maxStringLength, int[] actualStringLength, char[] unicodeString);

public static final int kUCKeyActionDown = 0;
public static final int kUCKeyActionUp = 1;

public static final int kThemeCopyArrowCursor = 1;
public static final int kThemeNotAllowedCursor = 18;
public static final int kThemeAliasArrowCursor = 2;

/** @method flags=dynamic 
 * @param iFile cast=(const FSRef *)
 * @param iContext cast=(ATSFontContext)
 * @param iFormat cast=(ATSFontFormat)
 * @param iReserved cast=(void *)
 * @param iOptions cast=(ATSOptionFlags)
 * @param oContainer cast=(ATSFontContainerRef *)
 */
public static final native int ATSFontActivateFromFileReference(byte[] iFile, int iContext, int iFormat, int /*long*/ iReserved, int iOptions, int /*long*/ [] oContainer);

public static final int kATSFontContextLocal = 2;
public static final int kATSOptionFlagsDefault = 0;
public static final int kATSFontFormatUnspecified = 0;

/** @method flags=dynamic 
 * @param path cast=(const UInt8 *)
 * @param ref cast=(FSRef *)
 * @param isDirectory cast=(Boolean *)
 */
public static final native int FSPathMakeRef (int /*long*/ path, byte[] ref, boolean[] isDirectory);

/** @method flags=dynamic */
public static final native byte LMGetKbdType();

/** @method flags=dynamic */
public static final native int /*long*/ AcquireRootMenu ();
/** @method flags=dynamic */
public static final native int CancelMenuTracking (int /*long*/ inRootMenu, boolean inImmediate, int inDismissalReason);

/** C calls */

public static final native int getpid();

public static final native void call(int /*long*/ proc, int /*long*/ id, int /*long*/ sel);

/** @method flags=no_gen */
public static final native boolean __BIG_ENDIAN__();
public static final int kCGBitmapByteOrderDefault = 0 << 12;
public static final int kCGBitmapByteOrder16Little = 1 << 12;
public static final int kCGBitmapByteOrder32Little = 2 << 12;
public static final int kCGBitmapByteOrder16Big = 3 << 12;
public static final int kCGBitmapByteOrder32Big = 4 << 12;
public static final int kCGBitmapByteOrder16Host = __BIG_ENDIAN__() ? kCGBitmapByteOrder16Big : kCGBitmapByteOrder16Little;
public static final int kCGBitmapByteOrder32Host = __BIG_ENDIAN__() ? kCGBitmapByteOrder32Big : kCGBitmapByteOrder32Little;

/**
 * @method flags=dynamic
 * @param destRect flags=struct
 * @param srcRect flags=struct
 */
public static final native void CGContextCopyWindowContentsToRect(int /*long*/ context, CGRect destRect, int /*long*/ contextID, int /*long*/ windowNumber, CGRect srcRect);

/** QuickDraw calls */

/** @method flags=dynamic */
public static final native int /*long*/ NewRgn();
/** @method flags=dynamic */
public static final native void RectRgn(int /*long*/ rgnHandle, short[] rect);
/** @method flags=dynamic */
public static final native void OpenRgn();
/** @method flags=dynamic */
public static final native void OffsetRgn(int /*long*/ rgnHandle, short dh, short dv);
/** @method flags=dynamic */
public static final native void MoveTo(short h, short v);
/** @method flags=dynamic */
public static final native void LineTo(short h, short v);
/** @method flags=dynamic */
public static final native void UnionRgn(int /*long*/ srcRgnA, int /*long*/ srcRgnB, int /*long*/ dstRgn);
/** @method flags=dynamic */
public static final native void CloseRgn(int /*long*/ dstRgn);
/** @method flags=dynamic */
public static final native void DisposeRgn(int /*long*/ rgnHandle);
/**
 * @method flags=dynamic
 * @param pt flags=struct,cast=(Point *)
 */
public static final native boolean PtInRgn(short[] pt, int /*long*/ rgnHandle);
/** @method flags=dynamic */
public static final native void GetRegionBounds(int /*long*/ rgnHandle, short[] bounds);
/** @method flags=dynamic */
public static final native void SectRgn(int /*long*/ srcRgnA, int /*long*/ srcRgnB, int /*long*/ dstRgn);
/** @method flags=dynamic */
public static final native boolean EmptyRgn(int /*long*/ rgnHandle);
/** @method flags=dynamic */
public static final native void DiffRgn(int /*long*/ srcRgnA, int /*long*/ srcRgnB, int /*long*/ dstRgn);
/** @method flags=dynamic */
public static final native boolean RectInRgn(short[] rect, int /*long*/ rgnHandle);
/** @method flags=dynamic */
public static final native int QDRegionToRects(int /*long*/ rgn, int dir, int /*long*/ proc, int /*long*/ userData);
/** @method flags=dynamic */
public static final native void CopyRgn(int /*long*/ srcRgnHandle, int /*long*/ dstRgnHandle);
/** @method flags=dynamic */
public static final native void SetRect(short[] r, short left, short top, short right, short bottom);
public static final int kQDParseRegionFromTop = (1 << 0);
public static final int kQDParseRegionFromBottom = (1 << 1);
public static final int kQDParseRegionFromLeft = (1 << 2);
public static final int kQDParseRegionFromRight = (1 << 3);
public static final int kQDParseRegionFromTopLeft = kQDParseRegionFromTop | kQDParseRegionFromLeft;
public static final int kQDRegionToRectsMsgParse = 2;

/** Custom callbacks */

/** @method flags=no_gen */
public static final native int /*long*/ isFlipped_CALLBACK();
/** @method flags=no_gen */
public static final native int /*long*/ drawRect_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ drawImage_withFrame_inView_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ drawInteriorWithFrame_inView_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ drawWithFrame_inView_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ imageRectForBounds_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ titleRectForBounds_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ hitTestForEvent_inRect_ofView_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ cellSize_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ setFrame_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ setFrameOrigin_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ setFrameSize_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ hitTest_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ webView_setFrame_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ markedRange_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ selectedRange_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ highlightSelectionInClipRect_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ attributedSubstringFromRange_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ setMarkedText_selectedRange_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ characterIndexForPoint_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ firstRectForCharacterRange_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ textView_willChangeSelectionFromCharacterRange_toCharacterRange_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ draggedImage_beganAt_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ draggedImage_endedAt_operation_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ accessibilityHitTest_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ dragSelectionWithEvent_offset_slideBack_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ shouldChangeTextInRange_replacementString_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ view_stringForToolTip_point_userData_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ canDragRowsWithIndexes_atPoint_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ setNeedsDisplayInRect_CALLBACK(int /*long*/ func);
/** @method flags=no_gen */
public static final native int /*long*/ expansionFrameWithFrameProc_CALLBACK(int /*long*/ func);

/** Custom structure return */

/** @method flags=no_gen */
public static final native void NSIntersectionRect (NSRect result, NSRect aRect, NSRect bRect);
/**
 * @method flags=no_gen
 * @param display cast=(CGDirectDisplayID)
 */
public static final native void CGDisplayBounds(int display, CGRect rect);

/** Objective-C runtime */

/**
 * @param cls cast=(Class)
 * @param name cast=(const char *),flags=critical
 * @param types cast=(const char *),flags=critical
 */
public static final native boolean class_addIvar(int /*long*/ cls, byte[] name, int /*long*/ size, byte alignment, byte[] types);
/**
 * @param cls cast=(Class)
 * @param name cast=(SEL)
 * @param imp cast=(IMP)
 */
public static final native boolean class_addMethod(int /*long*/ cls, int /*long*/ name, int /*long*/ imp, String types);
/**
 * @param cls cast=(Class)
 * @param protocol cast=(Protocol *)
 */
public static final native boolean class_addProtocol(int /*long*/ cls, int /*long*/ protocol);
/**
 * @param method cast=(Method)
 * @param aClass cast=(Class)
 * @param aSelector cast=(SEL)
 */
public static final native int /*long*/ class_getClassMethod(int /*long*/ aClass, int /*long*/ aSelector);
/**
 * @param cls cast=(Class)
 * @param name cast=(SEL)
 */
public static final native int /*long*/ class_getMethodImplementation(int /*long*/ cls, int /*long*/ name);
/**
 * @param cls cast=(Class)
 * @param name cast=(SEL)
 */
public static final native int /*long*/ class_getInstanceMethod(int /*long*/ cls, int /*long*/ name);
/** @param cls cast=(Class) */
public static final native int /*long*/ class_getSuperclass(int /*long*/ cls);
/**
 * @param method cast=(Method)
 * @param imp cast=(IMP)
 */
public static final native int /*long*/ method_setImplementation(int /*long*/ method, int /*long*/ imp);
/**
 * @param cls cast=(Class)
 * @param extraBytes cast=(size_t)
 */
public static final native int /*long*/ class_createInstance(int /*long*/ cls, int /*long*/ extraBytes);

/** @method flags=no_gen */
public static final native String class_getName(int /*long*/ cls);
/** @method flags=dynamic */
public static final native void instrumentObjcMessageSends(boolean val);
/** @param superclass cast=(Class) */
public static final native int /*long*/ objc_allocateClassPair(int /*long*/ superclass, String name, int /*long*/ extraBytes);
public static final native int /*long*/ objc_getClass(String className);
public static final native int /*long*/ objc_getMetaClass(String name);
public static final native int /*long*/ objc_getProtocol(String name);
public static final native int /*long*/ objc_lookUpClass(String className);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSendSuper(objc_super superId, int /*long*/ sel);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSendSuper(objc_super superId, int /*long*/ sel, NSRect arg0);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSendSuper(objc_super superId, int /*long*/ sel, NSPoint arg0);
/**
 * @method flags=cast
 * @param arg1 flags=struct
 */
public static final native int /*long*/ objc_msgSendSuper(objc_super superId, int /*long*/ sel, int /*long*/ arg0, NSPoint arg1);
 /**
  * @method flags=cast
  * @param arg0 flags=struct
  */
public static final native int /*long*/ objc_msgSendSuper(objc_super superId, int /*long*/ sel, NSSize arg0);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSendSuper(objc_super superId, int /*long*/ sel, int /*long*/ arg0);
/**
 *  @method flags=cast
 *  @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSendSuper(objc_super superId, int /*long*/ sel, NSRect arg0, int /*long*/ arg1);
/**
 *  @method flags=cast
 *  @param arg1 flags=struct
 */
public static final native int /*long*/ objc_msgSendSuper(objc_super superId, int /*long*/ sel, int /*long*/ arg0, NSPoint arg1, int /*long*/ arg2);
/**
 *  @method flags=cast
 *  @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSendSuper(objc_super superId, int /*long*/ sel, NSRange arg0, int /*long*/ arg1);
/**
 *  @method flags=cast
 *  @param arg1 flags=struct
 */
public static final native int /*long*/ objc_msgSendSuper(objc_super superId, int /*long*/ sel, int /*long*/ arg0, NSRect arg1, int /*long*/ arg2);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSendSuper(objc_super superId, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSendSuper(objc_super superId, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, boolean arg3);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSendSuper(objc_super superId, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3);

/** @method flags=cast */
public static final native void objc_msgSendSuper_stret(NSSize result, objc_super superId, int /*long*/ sel);
/** @param cls cast=(Class) */
public static final native void objc_registerClassPair(int /*long*/ cls);
/** @param obj cast=(id) */
public static final native int /*long*/ object_getClassName(int /*long*/ obj);
/** @param obj cast=(id) */
public static final native int /*long*/ object_getClass(int /*long*/ obj);

/**
 * @param obj cast=(id)
 * @param name cast=(const char*),flags=critical
 * @param outValue cast=(void **),flags=critical
 */
public static final native int /*long*/ object_getInstanceVariable(int /*long*/ obj, byte[] name, int /*long*/ [] outValue);
/**
 * @param obj cast=(id)
 * @param name cast=(const char*),flags=critical
 * @param value cast=(void *),flags=critical
 */
public static final native int /*long*/ object_setInstanceVariable(int /*long*/ obj, byte[] name, int /*long*/ value);
/**
 * @param obj cast=(id)
 * @param clazz cast=(Class) 
 */
public static final native int /*long*/ object_setClass(int /*long*/ obj, int /*long*/ clazz);
public static final native int /*long*/ sel_registerName(String selectorName);
public static final native int objc_super_sizeof();


/** This section is auto generated */

/** Classes */
public static final int /*long*/ class_DOMDocument = objc_getClass("DOMDocument");
public static final int /*long*/ class_DOMEvent = objc_getClass("DOMEvent");
public static final int /*long*/ class_DOMKeyboardEvent = objc_getClass("DOMKeyboardEvent");
public static final int /*long*/ class_DOMMouseEvent = objc_getClass("DOMMouseEvent");
public static final int /*long*/ class_DOMUIEvent = objc_getClass("DOMUIEvent");
public static final int /*long*/ class_DOMWheelEvent = objc_getClass("DOMWheelEvent");
public static final int /*long*/ class_NSActionCell = objc_getClass("NSActionCell");
public static final int /*long*/ class_NSAffineTransform = objc_getClass("NSAffineTransform");
public static final int /*long*/ class_NSAlert = objc_getClass("NSAlert");
public static final int /*long*/ class_NSAppleEventDescriptor = objc_getClass("NSAppleEventDescriptor");
public static final int /*long*/ class_NSApplication = objc_getClass("NSApplication");
public static final int /*long*/ class_NSArray = objc_getClass("NSArray");
public static final int /*long*/ class_NSAttributedString = objc_getClass("NSAttributedString");
public static final int /*long*/ class_NSAutoreleasePool = objc_getClass("NSAutoreleasePool");
public static final int /*long*/ class_NSBezierPath = objc_getClass("NSBezierPath");
public static final int /*long*/ class_NSBitmapImageRep = objc_getClass("NSBitmapImageRep");
public static final int /*long*/ class_NSBox = objc_getClass("NSBox");
public static final int /*long*/ class_NSBrowserCell = objc_getClass("NSBrowserCell");
public static final int /*long*/ class_NSBundle = objc_getClass("NSBundle");
public static final int /*long*/ class_NSButton = objc_getClass("NSButton");
public static final int /*long*/ class_NSButtonCell = objc_getClass("NSButtonCell");
public static final int /*long*/ class_NSCalendarDate = objc_getClass("NSCalendarDate");
public static final int /*long*/ class_NSCell = objc_getClass("NSCell");
public static final int /*long*/ class_NSCharacterSet = objc_getClass("NSCharacterSet");
public static final int /*long*/ class_NSClipView = objc_getClass("NSClipView");
public static final int /*long*/ class_NSCoder = objc_getClass("NSCoder");
public static final int /*long*/ class_NSColor = objc_getClass("NSColor");
public static final int /*long*/ class_NSColorPanel = objc_getClass("NSColorPanel");
public static final int /*long*/ class_NSColorSpace = objc_getClass("NSColorSpace");
public static final int /*long*/ class_NSComboBox = objc_getClass("NSComboBox");
public static final int /*long*/ class_NSComboBoxCell = objc_getClass("NSComboBoxCell");
public static final int /*long*/ class_NSControl = objc_getClass("NSControl");
public static final int /*long*/ class_NSCursor = objc_getClass("NSCursor");
public static final int /*long*/ class_NSData = objc_getClass("NSData");
public static final int /*long*/ class_NSDate = objc_getClass("NSDate");
public static final int /*long*/ class_NSDatePicker = objc_getClass("NSDatePicker");
public static final int /*long*/ class_NSDictionary = objc_getClass("NSDictionary");
public static final int /*long*/ class_NSDirectoryEnumerator = objc_getClass("NSDirectoryEnumerator");
public static final int /*long*/ class_NSEnumerator = objc_getClass("NSEnumerator");
public static final int /*long*/ class_NSError = objc_getClass("NSError");
public static final int /*long*/ class_NSEvent = objc_getClass("NSEvent");
public static final int /*long*/ class_NSFileManager = objc_getClass("NSFileManager");
public static final int /*long*/ class_NSFileWrapper = objc_getClass("NSFileWrapper");
public static final int /*long*/ class_NSFont = objc_getClass("NSFont");
public static final int /*long*/ class_NSFontManager = objc_getClass("NSFontManager");
public static final int /*long*/ class_NSFontPanel = objc_getClass("NSFontPanel");
public static final int /*long*/ class_NSFormatter = objc_getClass("NSFormatter");
public static final int /*long*/ class_NSGradient = objc_getClass("NSGradient");
public static final int /*long*/ class_NSGraphicsContext = objc_getClass("NSGraphicsContext");
public static final int /*long*/ class_NSHTTPCookie = objc_getClass("NSHTTPCookie");
public static final int /*long*/ class_NSHTTPCookieStorage = objc_getClass("NSHTTPCookieStorage");
public static final int /*long*/ class_NSImage = objc_getClass("NSImage");
public static final int /*long*/ class_NSImageRep = objc_getClass("NSImageRep");
public static final int /*long*/ class_NSImageView = objc_getClass("NSImageView");
public static final int /*long*/ class_NSIndexSet = objc_getClass("NSIndexSet");
public static final int /*long*/ class_NSInputManager = objc_getClass("NSInputManager");
public static final int /*long*/ class_NSKeyedArchiver = objc_getClass("NSKeyedArchiver");
public static final int /*long*/ class_NSKeyedUnarchiver = objc_getClass("NSKeyedUnarchiver");
public static final int /*long*/ class_NSLayoutManager = objc_getClass("NSLayoutManager");
public static final int /*long*/ class_NSMenu = objc_getClass("NSMenu");
public static final int /*long*/ class_NSMenuItem = objc_getClass("NSMenuItem");
public static final int /*long*/ class_NSMutableArray = objc_getClass("NSMutableArray");
public static final int /*long*/ class_NSMutableAttributedString = objc_getClass("NSMutableAttributedString");
public static final int /*long*/ class_NSMutableDictionary = objc_getClass("NSMutableDictionary");
public static final int /*long*/ class_NSMutableIndexSet = objc_getClass("NSMutableIndexSet");
public static final int /*long*/ class_NSMutableParagraphStyle = objc_getClass("NSMutableParagraphStyle");
public static final int /*long*/ class_NSMutableSet = objc_getClass("NSMutableSet");
public static final int /*long*/ class_NSMutableString = objc_getClass("NSMutableString");
public static final int /*long*/ class_NSMutableURLRequest = objc_getClass("NSMutableURLRequest");
public static final int /*long*/ class_NSNotification = objc_getClass("NSNotification");
public static final int /*long*/ class_NSNotificationCenter = objc_getClass("NSNotificationCenter");
public static final int /*long*/ class_NSNumber = objc_getClass("NSNumber");
public static final int /*long*/ class_NSNumberFormatter = objc_getClass("NSNumberFormatter");
public static final int /*long*/ class_NSObject = objc_getClass("NSObject");
public static final int /*long*/ class_NSOpenGLContext = objc_getClass("NSOpenGLContext");
public static final int /*long*/ class_NSOpenGLPixelFormat = objc_getClass("NSOpenGLPixelFormat");
public static final int /*long*/ class_NSOpenGLView = objc_getClass("NSOpenGLView");
public static final int /*long*/ class_NSOpenPanel = objc_getClass("NSOpenPanel");
public static final int /*long*/ class_NSOutlineView = objc_getClass("NSOutlineView");
public static final int /*long*/ class_NSPanel = objc_getClass("NSPanel");
public static final int /*long*/ class_NSParagraphStyle = objc_getClass("NSParagraphStyle");
public static final int /*long*/ class_NSPasteboard = objc_getClass("NSPasteboard");
public static final int /*long*/ class_NSPopUpButton = objc_getClass("NSPopUpButton");
public static final int /*long*/ class_NSPrintInfo = objc_getClass("NSPrintInfo");
public static final int /*long*/ class_NSPrintOperation = objc_getClass("NSPrintOperation");
public static final int /*long*/ class_NSPrintPanel = objc_getClass("NSPrintPanel");
public static final int /*long*/ class_NSPrinter = objc_getClass("NSPrinter");
public static final int /*long*/ class_NSProgressIndicator = objc_getClass("NSProgressIndicator");
public static final int /*long*/ class_NSResponder = objc_getClass("NSResponder");
public static final int /*long*/ class_NSRunLoop = objc_getClass("NSRunLoop");
public static final int /*long*/ class_NSSavePanel = objc_getClass("NSSavePanel");
public static final int /*long*/ class_NSScreen = objc_getClass("NSScreen");
public static final int /*long*/ class_NSScrollView = objc_getClass("NSScrollView");
public static final int /*long*/ class_NSScroller = objc_getClass("NSScroller");
public static final int /*long*/ class_NSSearchField = objc_getClass("NSSearchField");
public static final int /*long*/ class_NSSearchFieldCell = objc_getClass("NSSearchFieldCell");
public static final int /*long*/ class_NSSecureTextField = objc_getClass("NSSecureTextField");
public static final int /*long*/ class_NSSegmentedCell = objc_getClass("NSSegmentedCell");
public static final int /*long*/ class_NSSet = objc_getClass("NSSet");
public static final int /*long*/ class_NSSlider = objc_getClass("NSSlider");
public static final int /*long*/ class_NSStatusBar = objc_getClass("NSStatusBar");
public static final int /*long*/ class_NSStatusItem = objc_getClass("NSStatusItem");
public static final int /*long*/ class_NSStepper = objc_getClass("NSStepper");
public static final int /*long*/ class_NSString = objc_getClass("NSString");
public static final int /*long*/ class_NSTabView = objc_getClass("NSTabView");
public static final int /*long*/ class_NSTabViewItem = objc_getClass("NSTabViewItem");
public static final int /*long*/ class_NSTableColumn = objc_getClass("NSTableColumn");
public static final int /*long*/ class_NSTableHeaderCell = objc_getClass("NSTableHeaderCell");
public static final int /*long*/ class_NSTableHeaderView = objc_getClass("NSTableHeaderView");
public static final int /*long*/ class_NSTableView = objc_getClass("NSTableView");
public static final int /*long*/ class_NSText = objc_getClass("NSText");
public static final int /*long*/ class_NSTextAttachment = objc_getClass("NSTextAttachment");
public static final int /*long*/ class_NSTextContainer = objc_getClass("NSTextContainer");
public static final int /*long*/ class_NSTextField = objc_getClass("NSTextField");
public static final int /*long*/ class_NSTextFieldCell = objc_getClass("NSTextFieldCell");
public static final int /*long*/ class_NSTextStorage = objc_getClass("NSTextStorage");
public static final int /*long*/ class_NSTextTab = objc_getClass("NSTextTab");
public static final int /*long*/ class_NSTextView = objc_getClass("NSTextView");
public static final int /*long*/ class_NSThread = objc_getClass("NSThread");
public static final int /*long*/ class_NSTimeZone = objc_getClass("NSTimeZone");
public static final int /*long*/ class_NSTimer = objc_getClass("NSTimer");
public static final int /*long*/ class_NSToolbar = objc_getClass("NSToolbar");
public static final int /*long*/ class_NSToolbarItem = objc_getClass("NSToolbarItem");
public static final int /*long*/ class_NSTrackingArea = objc_getClass("NSTrackingArea");
public static final int /*long*/ class_NSTypesetter = objc_getClass("NSTypesetter");
public static final int /*long*/ class_NSURL = objc_getClass("NSURL");
public static final int /*long*/ class_NSURLAuthenticationChallenge = objc_getClass("NSURLAuthenticationChallenge");
public static final int /*long*/ class_NSURLCredential = objc_getClass("NSURLCredential");
public static final int /*long*/ class_NSURLDownload = objc_getClass("NSURLDownload");
public static final int /*long*/ class_NSURLProtectionSpace = objc_getClass("NSURLProtectionSpace");
public static final int /*long*/ class_NSURLRequest = objc_getClass("NSURLRequest");
public static final int /*long*/ class_NSValue = objc_getClass("NSValue");
public static final int /*long*/ class_NSView = objc_getClass("NSView");
public static final int /*long*/ class_NSWindow = objc_getClass("NSWindow");
public static final int /*long*/ class_NSWorkspace = objc_getClass("NSWorkspace");
public static final int /*long*/ class_WebDataSource = objc_getClass("WebDataSource");
public static final int /*long*/ class_WebFrame = objc_getClass("WebFrame");
public static final int /*long*/ class_WebFrameView = objc_getClass("WebFrameView");
public static final int /*long*/ class_WebPreferences = objc_getClass("WebPreferences");
public static final int /*long*/ class_WebScriptObject = objc_getClass("WebScriptObject");
public static final int /*long*/ class_WebUndefined = objc_getClass("WebUndefined");
public static final int /*long*/ class_WebView = objc_getClass("WebView");

/** Protocols */
public static final int /*long*/ protocol_NSAccessibility = objc_getProtocol("NSAccessibility");
public static final int /*long*/ protocol_NSAccessibilityAdditions = objc_getProtocol("NSAccessibilityAdditions");
public static final int /*long*/ protocol_NSApplicationDelegate = objc_getProtocol("NSApplicationDelegate");
public static final int /*long*/ protocol_NSApplicationNotifications = objc_getProtocol("NSApplicationNotifications");
public static final int /*long*/ protocol_NSColorPanelResponderMethod = objc_getProtocol("NSColorPanelResponderMethod");
public static final int /*long*/ protocol_NSComboBoxNotifications = objc_getProtocol("NSComboBoxNotifications");
public static final int /*long*/ protocol_NSDraggingDestination = objc_getProtocol("NSDraggingDestination");
public static final int /*long*/ protocol_NSDraggingSource = objc_getProtocol("NSDraggingSource");
public static final int /*long*/ protocol_NSFontManagerResponderMethod = objc_getProtocol("NSFontManagerResponderMethod");
public static final int /*long*/ protocol_NSMenuDelegate = objc_getProtocol("NSMenuDelegate");
public static final int /*long*/ protocol_NSOutlineViewDataSource = objc_getProtocol("NSOutlineViewDataSource");
public static final int /*long*/ protocol_NSOutlineViewDelegate = objc_getProtocol("NSOutlineViewDelegate");
public static final int /*long*/ protocol_NSOutlineViewNotifications = objc_getProtocol("NSOutlineViewNotifications");
public static final int /*long*/ protocol_NSPasteboardOwner = objc_getProtocol("NSPasteboardOwner");
public static final int /*long*/ protocol_NSSavePanelDelegate = objc_getProtocol("NSSavePanelDelegate");
public static final int /*long*/ protocol_NSTabViewDelegate = objc_getProtocol("NSTabViewDelegate");
public static final int /*long*/ protocol_NSTableDataSource = objc_getProtocol("NSTableDataSource");
public static final int /*long*/ protocol_NSTableViewDelegate = objc_getProtocol("NSTableViewDelegate");
public static final int /*long*/ protocol_NSTableViewNotifications = objc_getProtocol("NSTableViewNotifications");
public static final int /*long*/ protocol_NSTextDelegate = objc_getProtocol("NSTextDelegate");
public static final int /*long*/ protocol_NSTextInput = objc_getProtocol("NSTextInput");
public static final int /*long*/ protocol_NSTextViewDelegate = objc_getProtocol("NSTextViewDelegate");
public static final int /*long*/ protocol_NSToolTipOwner = objc_getProtocol("NSToolTipOwner");
public static final int /*long*/ protocol_NSToolbarDelegate = objc_getProtocol("NSToolbarDelegate");
public static final int /*long*/ protocol_NSToolbarNotifications = objc_getProtocol("NSToolbarNotifications");
public static final int /*long*/ protocol_NSURLDownloadDelegate = objc_getProtocol("NSURLDownloadDelegate");
public static final int /*long*/ protocol_NSWindowDelegate = objc_getProtocol("NSWindowDelegate");
public static final int /*long*/ protocol_NSWindowNotifications = objc_getProtocol("NSWindowNotifications");
public static final int /*long*/ protocol_WebDocumentRepresentation = objc_getProtocol("WebDocumentRepresentation");
public static final int /*long*/ protocol_WebFrameLoadDelegate = objc_getProtocol("WebFrameLoadDelegate");
public static final int /*long*/ protocol_WebOpenPanelResultListener = objc_getProtocol("WebOpenPanelResultListener");
public static final int /*long*/ protocol_WebPolicyDecisionListener = objc_getProtocol("WebPolicyDecisionListener");
public static final int /*long*/ protocol_WebPolicyDelegate = objc_getProtocol("WebPolicyDelegate");
public static final int /*long*/ protocol_WebResourceLoadDelegate = objc_getProtocol("WebResourceLoadDelegate");
public static final int /*long*/ protocol_WebUIDelegate = objc_getProtocol("WebUIDelegate");

/** Selectors */
public static final int /*long*/ sel_CGEvent = sel_registerName("CGEvent");
public static final int /*long*/ sel_DOMDocument = sel_registerName("DOMDocument");
public static final int /*long*/ sel_IBeamCursor = sel_registerName("IBeamCursor");
public static final int /*long*/ sel_TIFFRepresentation = sel_registerName("TIFFRepresentation");
public static final int /*long*/ sel_URL = sel_registerName("URL");
public static final int /*long*/ sel_URLFromPasteboard_ = sel_registerName("URLFromPasteboard:");
public static final int /*long*/ sel_URLWithString_ = sel_registerName("URLWithString:");
public static final int /*long*/ sel_UTF8String = sel_registerName("UTF8String");
public static final int /*long*/ sel_abortEditing = sel_registerName("abortEditing");
public static final int /*long*/ sel_absoluteString = sel_registerName("absoluteString");
public static final int /*long*/ sel_acceptsFirstMouse_ = sel_registerName("acceptsFirstMouse:");
public static final int /*long*/ sel_acceptsFirstResponder = sel_registerName("acceptsFirstResponder");
public static final int /*long*/ sel_accessibilityActionDescription_ = sel_registerName("accessibilityActionDescription:");
public static final int /*long*/ sel_accessibilityActionNames = sel_registerName("accessibilityActionNames");
public static final int /*long*/ sel_accessibilityAttributeNames = sel_registerName("accessibilityAttributeNames");
public static final int /*long*/ sel_accessibilityAttributeValue_ = sel_registerName("accessibilityAttributeValue:");
public static final int /*long*/ sel_accessibilityAttributeValue_forParameter_ = sel_registerName("accessibilityAttributeValue:forParameter:");
public static final int /*long*/ sel_accessibilityFocusedUIElement = sel_registerName("accessibilityFocusedUIElement");
public static final int /*long*/ sel_accessibilityHitTest_ = sel_registerName("accessibilityHitTest:");
public static final int /*long*/ sel_accessibilityIsAttributeSettable_ = sel_registerName("accessibilityIsAttributeSettable:");
public static final int /*long*/ sel_accessibilityIsIgnored = sel_registerName("accessibilityIsIgnored");
public static final int /*long*/ sel_accessibilityParameterizedAttributeNames = sel_registerName("accessibilityParameterizedAttributeNames");
public static final int /*long*/ sel_accessibilityPerformAction_ = sel_registerName("accessibilityPerformAction:");
public static final int /*long*/ sel_accessibilitySetOverrideValue_forAttribute_ = sel_registerName("accessibilitySetOverrideValue:forAttribute:");
public static final int /*long*/ sel_accessibilitySetValue_forAttribute_ = sel_registerName("accessibilitySetValue:forAttribute:");
public static final int /*long*/ sel_action = sel_registerName("action");
public static final int /*long*/ sel_activateIgnoringOtherApps_ = sel_registerName("activateIgnoringOtherApps:");
public static final int /*long*/ sel_addAttribute_value_range_ = sel_registerName("addAttribute:value:range:");
public static final int /*long*/ sel_addButtonWithTitle_ = sel_registerName("addButtonWithTitle:");
public static final int /*long*/ sel_addChildWindow_ordered_ = sel_registerName("addChildWindow:ordered:");
public static final int /*long*/ sel_addClip = sel_registerName("addClip");
public static final int /*long*/ sel_addEventListener_listener_useCapture_ = sel_registerName("addEventListener:listener:useCapture:");
public static final int /*long*/ sel_addIndex_ = sel_registerName("addIndex:");
public static final int /*long*/ sel_addItem_ = sel_registerName("addItem:");
public static final int /*long*/ sel_addItemWithObjectValue_ = sel_registerName("addItemWithObjectValue:");
public static final int /*long*/ sel_addItemWithTitle_action_keyEquivalent_ = sel_registerName("addItemWithTitle:action:keyEquivalent:");
public static final int /*long*/ sel_addLayoutManager_ = sel_registerName("addLayoutManager:");
public static final int /*long*/ sel_addObject_ = sel_registerName("addObject:");
public static final int /*long*/ sel_addObjectsFromArray_ = sel_registerName("addObjectsFromArray:");
public static final int /*long*/ sel_addObserver_selector_name_object_ = sel_registerName("addObserver:selector:name:object:");
public static final int /*long*/ sel_addRepresentation_ = sel_registerName("addRepresentation:");
public static final int /*long*/ sel_addSubview_ = sel_registerName("addSubview:");
public static final int /*long*/ sel_addSubview_positioned_relativeTo_ = sel_registerName("addSubview:positioned:relativeTo:");
public static final int /*long*/ sel_addTabStop_ = sel_registerName("addTabStop:");
public static final int /*long*/ sel_addTabViewItem_ = sel_registerName("addTabViewItem:");
public static final int /*long*/ sel_addTableColumn_ = sel_registerName("addTableColumn:");
public static final int /*long*/ sel_addTemporaryAttribute_value_forCharacterRange_ = sel_registerName("addTemporaryAttribute:value:forCharacterRange:");
public static final int /*long*/ sel_addTextContainer_ = sel_registerName("addTextContainer:");
public static final int /*long*/ sel_addTimer_forMode_ = sel_registerName("addTimer:forMode:");
public static final int /*long*/ sel_addToolTipRect_owner_userData_ = sel_registerName("addToolTipRect:owner:userData:");
public static final int /*long*/ sel_addTypes_owner_ = sel_registerName("addTypes:owner:");
public static final int /*long*/ sel_alignment = sel_registerName("alignment");
public static final int /*long*/ sel_allKeys = sel_registerName("allKeys");
public static final int /*long*/ sel_alloc = sel_registerName("alloc");
public static final int /*long*/ sel_allowsColumnReordering = sel_registerName("allowsColumnReordering");
public static final int /*long*/ sel_allowsFloats = sel_registerName("allowsFloats");
public static final int /*long*/ sel_alphaComponent = sel_registerName("alphaComponent");
public static final int /*long*/ sel_alphaValue = sel_registerName("alphaValue");
public static final int /*long*/ sel_altKey = sel_registerName("altKey");
public static final int /*long*/ sel_alternateSelectedControlColor = sel_registerName("alternateSelectedControlColor");
public static final int /*long*/ sel_alternateSelectedControlTextColor = sel_registerName("alternateSelectedControlTextColor");
public static final int /*long*/ sel_alwaysShowsDecimalSeparator = sel_registerName("alwaysShowsDecimalSeparator");
public static final int /*long*/ sel_appendAttributedString_ = sel_registerName("appendAttributedString:");
public static final int /*long*/ sel_appendBezierPath_ = sel_registerName("appendBezierPath:");
public static final int /*long*/ sel_appendBezierPathWithArcWithCenter_radius_startAngle_endAngle_ = sel_registerName("appendBezierPathWithArcWithCenter:radius:startAngle:endAngle:");
public static final int /*long*/ sel_appendBezierPathWithArcWithCenter_radius_startAngle_endAngle_clockwise_ = sel_registerName("appendBezierPathWithArcWithCenter:radius:startAngle:endAngle:clockwise:");
public static final int /*long*/ sel_appendBezierPathWithGlyphs_count_inFont_ = sel_registerName("appendBezierPathWithGlyphs:count:inFont:");
public static final int /*long*/ sel_appendBezierPathWithOvalInRect_ = sel_registerName("appendBezierPathWithOvalInRect:");
public static final int /*long*/ sel_appendBezierPathWithRect_ = sel_registerName("appendBezierPathWithRect:");
public static final int /*long*/ sel_appendBezierPathWithRoundedRect_xRadius_yRadius_ = sel_registerName("appendBezierPathWithRoundedRect:xRadius:yRadius:");
public static final int /*long*/ sel_appendString_ = sel_registerName("appendString:");
public static final int /*long*/ sel_applicationDidBecomeActive_ = sel_registerName("applicationDidBecomeActive:");
public static final int /*long*/ sel_applicationDidFinishLaunching_ = sel_registerName("applicationDidFinishLaunching:");
public static final int /*long*/ sel_applicationDidResignActive_ = sel_registerName("applicationDidResignActive:");
public static final int /*long*/ sel_applicationShouldTerminate_ = sel_registerName("applicationShouldTerminate:");
public static final int /*long*/ sel_applicationWillFinishLaunching_ = sel_registerName("applicationWillFinishLaunching:");
public static final int /*long*/ sel_applicationWillTerminate_ = sel_registerName("applicationWillTerminate:");
public static final int /*long*/ sel_archivedDataWithRootObject_ = sel_registerName("archivedDataWithRootObject:");
public static final int /*long*/ sel_areCursorRectsEnabled = sel_registerName("areCursorRectsEnabled");
public static final int /*long*/ sel_array = sel_registerName("array");
public static final int /*long*/ sel_arrayWithCapacity_ = sel_registerName("arrayWithCapacity:");
public static final int /*long*/ sel_arrayWithObject_ = sel_registerName("arrayWithObject:");
public static final int /*long*/ sel_arrowCursor = sel_registerName("arrowCursor");
public static final int /*long*/ sel_ascender = sel_registerName("ascender");
public static final int /*long*/ sel_attributedStringValue = sel_registerName("attributedStringValue");
public static final int /*long*/ sel_attributedStringWithAttachment_ = sel_registerName("attributedStringWithAttachment:");
public static final int /*long*/ sel_attributedSubstringFromRange_ = sel_registerName("attributedSubstringFromRange:");
public static final int /*long*/ sel_attributesAtIndex_longestEffectiveRange_inRange_ = sel_registerName("attributesAtIndex:longestEffectiveRange:inRange:");
public static final int /*long*/ sel_autorelease = sel_registerName("autorelease");
public static final int /*long*/ sel_availableFontFamilies = sel_registerName("availableFontFamilies");
public static final int /*long*/ sel_availableFonts = sel_registerName("availableFonts");
public static final int /*long*/ sel_availableMembersOfFontFamily_ = sel_registerName("availableMembersOfFontFamily:");
public static final int /*long*/ sel_availableTypeFromArray_ = sel_registerName("availableTypeFromArray:");
public static final int /*long*/ sel_baselineOffsetInLayoutManager_glyphIndex_ = sel_registerName("baselineOffsetInLayoutManager:glyphIndex:");
public static final int /*long*/ sel_becomeFirstResponder = sel_registerName("becomeFirstResponder");
public static final int /*long*/ sel_beginDocument = sel_registerName("beginDocument");
public static final int /*long*/ sel_beginEditing = sel_registerName("beginEditing");
public static final int /*long*/ sel_beginPageInRect_atPlacement_ = sel_registerName("beginPageInRect:atPlacement:");
public static final int /*long*/ sel_beginSheet_modalForWindow_modalDelegate_didEndSelector_contextInfo_ = sel_registerName("beginSheet:modalForWindow:modalDelegate:didEndSelector:contextInfo:");
public static final int /*long*/ sel_beginSheetModalForWindow_modalDelegate_didEndSelector_contextInfo_ = sel_registerName("beginSheetModalForWindow:modalDelegate:didEndSelector:contextInfo:");
public static final int /*long*/ sel_beginSheetWithPrintInfo_modalForWindow_delegate_didEndSelector_contextInfo_ = sel_registerName("beginSheetWithPrintInfo:modalForWindow:delegate:didEndSelector:contextInfo:");
public static final int /*long*/ sel_bestRepresentationForDevice_ = sel_registerName("bestRepresentationForDevice:");
public static final int /*long*/ sel_bezierPath = sel_registerName("bezierPath");
public static final int /*long*/ sel_bezierPathByFlatteningPath = sel_registerName("bezierPathByFlatteningPath");
public static final int /*long*/ sel_bezierPathWithRect_ = sel_registerName("bezierPathWithRect:");
public static final int /*long*/ sel_bitmapData = sel_registerName("bitmapData");
public static final int /*long*/ sel_bitmapFormat = sel_registerName("bitmapFormat");
public static final int /*long*/ sel_bitsPerPixel = sel_registerName("bitsPerPixel");
public static final int /*long*/ sel_bitsPerSample = sel_registerName("bitsPerSample");
public static final int /*long*/ sel_blackColor = sel_registerName("blackColor");
public static final int /*long*/ sel_blueComponent = sel_registerName("blueComponent");
public static final int /*long*/ sel_boolValue = sel_registerName("boolValue");
public static final int /*long*/ sel_borderWidth = sel_registerName("borderWidth");
public static final int /*long*/ sel_boundingRectForGlyphRange_inTextContainer_ = sel_registerName("boundingRectForGlyphRange:inTextContainer:");
public static final int /*long*/ sel_bounds = sel_registerName("bounds");
public static final int /*long*/ sel_bundleIdentifier = sel_registerName("bundleIdentifier");
public static final int /*long*/ sel_bundlePath = sel_registerName("bundlePath");
public static final int /*long*/ sel_bundleWithIdentifier_ = sel_registerName("bundleWithIdentifier:");
public static final int /*long*/ sel_bundleWithPath_ = sel_registerName("bundleWithPath:");
public static final int /*long*/ sel_button = sel_registerName("button");
public static final int /*long*/ sel_buttonNumber = sel_registerName("buttonNumber");
public static final int /*long*/ sel_bytes = sel_registerName("bytes");
public static final int /*long*/ sel_bytesPerPlane = sel_registerName("bytesPerPlane");
public static final int /*long*/ sel_bytesPerRow = sel_registerName("bytesPerRow");
public static final int /*long*/ sel_calendarDate = sel_registerName("calendarDate");
public static final int /*long*/ sel_canBecomeKeyWindow = sel_registerName("canBecomeKeyWindow");
public static final int /*long*/ sel_canDragRowsWithIndexes_atPoint_ = sel_registerName("canDragRowsWithIndexes:atPoint:");
public static final int /*long*/ sel_canGoBack = sel_registerName("canGoBack");
public static final int /*long*/ sel_canGoForward = sel_registerName("canGoForward");
public static final int /*long*/ sel_canShowMIMEType_ = sel_registerName("canShowMIMEType:");
public static final int /*long*/ sel_cancel = sel_registerName("cancel");
public static final int /*long*/ sel_cancelAuthenticationChallenge_ = sel_registerName("cancelAuthenticationChallenge:");
public static final int /*long*/ sel_cancelButtonCell = sel_registerName("cancelButtonCell");
public static final int /*long*/ sel_cancelTracking = sel_registerName("cancelTracking");
public static final int /*long*/ sel_cascadeTopLeftFromPoint_ = sel_registerName("cascadeTopLeftFromPoint:");
public static final int /*long*/ sel_cell = sel_registerName("cell");
public static final int /*long*/ sel_cellClass = sel_registerName("cellClass");
public static final int /*long*/ sel_cellSize = sel_registerName("cellSize");
public static final int /*long*/ sel_cellSizeForBounds_ = sel_registerName("cellSizeForBounds:");
public static final int /*long*/ sel_changeColor_ = sel_registerName("changeColor:");
public static final int /*long*/ sel_changeFont_ = sel_registerName("changeFont:");
public static final int /*long*/ sel_charCode = sel_registerName("charCode");
public static final int /*long*/ sel_characterAtIndex_ = sel_registerName("characterAtIndex:");
public static final int /*long*/ sel_characterIndexForGlyphAtIndex_ = sel_registerName("characterIndexForGlyphAtIndex:");
public static final int /*long*/ sel_characterIndexForInsertionAtPoint_ = sel_registerName("characterIndexForInsertionAtPoint:");
public static final int /*long*/ sel_characterIndexForPoint_ = sel_registerName("characterIndexForPoint:");
public static final int /*long*/ sel_characterIsMember_ = sel_registerName("characterIsMember:");
public static final int /*long*/ sel_characters = sel_registerName("characters");
public static final int /*long*/ sel_charactersIgnoringModifiers = sel_registerName("charactersIgnoringModifiers");
public static final int /*long*/ sel_chooseFilename_ = sel_registerName("chooseFilename:");
public static final int /*long*/ sel_className = sel_registerName("className");
public static final int /*long*/ sel_cleanUpOperation = sel_registerName("cleanUpOperation");
public static final int /*long*/ sel_clearColor = sel_registerName("clearColor");
public static final int /*long*/ sel_clearCurrentContext = sel_registerName("clearCurrentContext");
public static final int /*long*/ sel_clearGLContext = sel_registerName("clearGLContext");
public static final int /*long*/ sel_clickCount = sel_registerName("clickCount");
public static final int /*long*/ sel_clickedColumn = sel_registerName("clickedColumn");
public static final int /*long*/ sel_clickedRow = sel_registerName("clickedRow");
public static final int /*long*/ sel_clientX = sel_registerName("clientX");
public static final int /*long*/ sel_clientY = sel_registerName("clientY");
public static final int /*long*/ sel_close = sel_registerName("close");
public static final int /*long*/ sel_closePath = sel_registerName("closePath");
public static final int /*long*/ sel_code = sel_registerName("code");
public static final int /*long*/ sel_collapseItem_ = sel_registerName("collapseItem:");
public static final int /*long*/ sel_collapseItem_collapseChildren_ = sel_registerName("collapseItem:collapseChildren:");
public static final int /*long*/ sel_color = sel_registerName("color");
public static final int /*long*/ sel_colorAtX_y_ = sel_registerName("colorAtX:y:");
public static final int /*long*/ sel_colorSpaceName = sel_registerName("colorSpaceName");
public static final int /*long*/ sel_colorUsingColorSpace_ = sel_registerName("colorUsingColorSpace:");
public static final int /*long*/ sel_colorUsingColorSpaceName_ = sel_registerName("colorUsingColorSpaceName:");
public static final int /*long*/ sel_colorWithDeviceRed_green_blue_alpha_ = sel_registerName("colorWithDeviceRed:green:blue:alpha:");
public static final int /*long*/ sel_colorWithPatternImage_ = sel_registerName("colorWithPatternImage:");
public static final int /*long*/ sel_columnAtPoint_ = sel_registerName("columnAtPoint:");
public static final int /*long*/ sel_columnIndexesInRect_ = sel_registerName("columnIndexesInRect:");
public static final int /*long*/ sel_columnWithIdentifier_ = sel_registerName("columnWithIdentifier:");
public static final int /*long*/ sel_comboBoxSelectionDidChange_ = sel_registerName("comboBoxSelectionDidChange:");
public static final int /*long*/ sel_comboBoxWillDismiss_ = sel_registerName("comboBoxWillDismiss:");
public static final int /*long*/ sel_compare_ = sel_registerName("compare:");
public static final int /*long*/ sel_concat = sel_registerName("concat");
public static final int /*long*/ sel_conformsToProtocol_ = sel_registerName("conformsToProtocol:");
public static final int /*long*/ sel_containerSize = sel_registerName("containerSize");
public static final int /*long*/ sel_containsIndex_ = sel_registerName("containsIndex:");
public static final int /*long*/ sel_containsObject_ = sel_registerName("containsObject:");
public static final int /*long*/ sel_containsPoint_ = sel_registerName("containsPoint:");
public static final int /*long*/ sel_contentRect = sel_registerName("contentRect");
public static final int /*long*/ sel_contentRectForFrameRect_ = sel_registerName("contentRectForFrameRect:");
public static final int /*long*/ sel_contentSize = sel_registerName("contentSize");
public static final int /*long*/ sel_contentSizeForFrameSize_hasHorizontalScroller_hasVerticalScroller_borderType_ = sel_registerName("contentSizeForFrameSize:hasHorizontalScroller:hasVerticalScroller:borderType:");
public static final int /*long*/ sel_contentView = sel_registerName("contentView");
public static final int /*long*/ sel_contentViewMargins = sel_registerName("contentViewMargins");
public static final int /*long*/ sel_context = sel_registerName("context");
public static final int /*long*/ sel_controlBackgroundColor = sel_registerName("controlBackgroundColor");
public static final int /*long*/ sel_controlContentFontOfSize_ = sel_registerName("controlContentFontOfSize:");
public static final int /*long*/ sel_controlDarkShadowColor = sel_registerName("controlDarkShadowColor");
public static final int /*long*/ sel_controlHighlightColor = sel_registerName("controlHighlightColor");
public static final int /*long*/ sel_controlLightHighlightColor = sel_registerName("controlLightHighlightColor");
public static final int /*long*/ sel_controlPointBounds = sel_registerName("controlPointBounds");
public static final int /*long*/ sel_controlShadowColor = sel_registerName("controlShadowColor");
public static final int /*long*/ sel_controlSize = sel_registerName("controlSize");
public static final int /*long*/ sel_controlTextColor = sel_registerName("controlTextColor");
public static final int /*long*/ sel_convertBaseToScreen_ = sel_registerName("convertBaseToScreen:");
public static final int /*long*/ sel_convertPoint_fromView_ = sel_registerName("convertPoint:fromView:");
public static final int /*long*/ sel_convertPoint_toView_ = sel_registerName("convertPoint:toView:");
public static final int /*long*/ sel_convertPointFromBase_ = sel_registerName("convertPointFromBase:");
public static final int /*long*/ sel_convertPointToBase_ = sel_registerName("convertPointToBase:");
public static final int /*long*/ sel_convertRect_fromView_ = sel_registerName("convertRect:fromView:");
public static final int /*long*/ sel_convertRect_toView_ = sel_registerName("convertRect:toView:");
public static final int /*long*/ sel_convertRectFromBase_ = sel_registerName("convertRectFromBase:");
public static final int /*long*/ sel_convertRectToBase_ = sel_registerName("convertRectToBase:");
public static final int /*long*/ sel_convertScreenToBase_ = sel_registerName("convertScreenToBase:");
public static final int /*long*/ sel_convertSize_fromView_ = sel_registerName("convertSize:fromView:");
public static final int /*long*/ sel_convertSize_toView_ = sel_registerName("convertSize:toView:");
public static final int /*long*/ sel_convertSizeFromBase_ = sel_registerName("convertSizeFromBase:");
public static final int /*long*/ sel_convertSizeToBase_ = sel_registerName("convertSizeToBase:");
public static final int /*long*/ sel_cookies = sel_registerName("cookies");
public static final int /*long*/ sel_cookiesForURL_ = sel_registerName("cookiesForURL:");
public static final int /*long*/ sel_cookiesWithResponseHeaderFields_forURL_ = sel_registerName("cookiesWithResponseHeaderFields:forURL:");
public static final int /*long*/ sel_copiesOnScroll = sel_registerName("copiesOnScroll");
public static final int /*long*/ sel_copy = sel_registerName("copy");
public static final int /*long*/ sel_copy_ = sel_registerName("copy:");
public static final int /*long*/ sel_count = sel_registerName("count");
public static final int /*long*/ sel_createContext = sel_registerName("createContext");
public static final int /*long*/ sel_createFileAtPath_contents_attributes_ = sel_registerName("createFileAtPath:contents:attributes:");
public static final int /*long*/ sel_credentialWithUser_password_persistence_ = sel_registerName("credentialWithUser:password:persistence:");
public static final int /*long*/ sel_crosshairCursor = sel_registerName("crosshairCursor");
public static final int /*long*/ sel_ctrlKey = sel_registerName("ctrlKey");
public static final int /*long*/ sel_currentContext = sel_registerName("currentContext");
public static final int /*long*/ sel_currentCursor = sel_registerName("currentCursor");
public static final int /*long*/ sel_currentEditor = sel_registerName("currentEditor");
public static final int /*long*/ sel_currentEvent = sel_registerName("currentEvent");
public static final int /*long*/ sel_currentInputManager = sel_registerName("currentInputManager");
public static final int /*long*/ sel_currentPoint = sel_registerName("currentPoint");
public static final int /*long*/ sel_currentRunLoop = sel_registerName("currentRunLoop");
public static final int /*long*/ sel_currentThread = sel_registerName("currentThread");
public static final int /*long*/ sel_cursorUpdate_ = sel_registerName("cursorUpdate:");
public static final int /*long*/ sel_curveToPoint_controlPoint1_controlPoint2_ = sel_registerName("curveToPoint:controlPoint1:controlPoint2:");
public static final int /*long*/ sel_cut_ = sel_registerName("cut:");
public static final int /*long*/ sel_dataCell = sel_registerName("dataCell");
public static final int /*long*/ sel_dataForType_ = sel_registerName("dataForType:");
public static final int /*long*/ sel_dataSource = sel_registerName("dataSource");
public static final int /*long*/ sel_dataWithBytes_length_ = sel_registerName("dataWithBytes:length:");
public static final int /*long*/ sel_dateValue = sel_registerName("dateValue");
public static final int /*long*/ sel_dateWithCalendarFormat_timeZone_ = sel_registerName("dateWithCalendarFormat:timeZone:");
public static final int /*long*/ sel_dateWithTimeIntervalSinceNow_ = sel_registerName("dateWithTimeIntervalSinceNow:");
public static final int /*long*/ sel_dateWithYear_month_day_hour_minute_second_timeZone_ = sel_registerName("dateWithYear:month:day:hour:minute:second:timeZone:");
public static final int /*long*/ sel_dayOfMonth = sel_registerName("dayOfMonth");
public static final int /*long*/ sel_decimalDigitCharacterSet = sel_registerName("decimalDigitCharacterSet");
public static final int /*long*/ sel_decimalSeparator = sel_registerName("decimalSeparator");
public static final int /*long*/ sel_declareTypes_owner_ = sel_registerName("declareTypes:owner:");
public static final int /*long*/ sel_defaultBaselineOffsetForFont_ = sel_registerName("defaultBaselineOffsetForFont:");
public static final int /*long*/ sel_defaultButtonCell = sel_registerName("defaultButtonCell");
public static final int /*long*/ sel_defaultCenter = sel_registerName("defaultCenter");
public static final int /*long*/ sel_defaultFlatness = sel_registerName("defaultFlatness");
public static final int /*long*/ sel_defaultLineHeightForFont_ = sel_registerName("defaultLineHeightForFont:");
public static final int /*long*/ sel_defaultManager = sel_registerName("defaultManager");
public static final int /*long*/ sel_defaultParagraphStyle = sel_registerName("defaultParagraphStyle");
public static final int /*long*/ sel_defaultPrinter = sel_registerName("defaultPrinter");
public static final int /*long*/ sel_defaultTimeZone = sel_registerName("defaultTimeZone");
public static final int /*long*/ sel_delegate = sel_registerName("delegate");
public static final int /*long*/ sel_deleteCookie_ = sel_registerName("deleteCookie:");
public static final int /*long*/ sel_deliverResult = sel_registerName("deliverResult");
public static final int /*long*/ sel_deltaX = sel_registerName("deltaX");
public static final int /*long*/ sel_deltaY = sel_registerName("deltaY");
public static final int /*long*/ sel_deminiaturize_ = sel_registerName("deminiaturize:");
public static final int /*long*/ sel_depth = sel_registerName("depth");
public static final int /*long*/ sel_descender = sel_registerName("descender");
public static final int /*long*/ sel_description = sel_registerName("description");
public static final int /*long*/ sel_deselectAll_ = sel_registerName("deselectAll:");
public static final int /*long*/ sel_deselectItemAtIndex_ = sel_registerName("deselectItemAtIndex:");
public static final int /*long*/ sel_deselectRow_ = sel_registerName("deselectRow:");
public static final int /*long*/ sel_destroyContext = sel_registerName("destroyContext");
public static final int /*long*/ sel_detail = sel_registerName("detail");
public static final int /*long*/ sel_deviceDescription = sel_registerName("deviceDescription");
public static final int /*long*/ sel_deviceRGBColorSpace = sel_registerName("deviceRGBColorSpace");
public static final int /*long*/ sel_dictionary = sel_registerName("dictionary");
public static final int /*long*/ sel_dictionaryWithCapacity_ = sel_registerName("dictionaryWithCapacity:");
public static final int /*long*/ sel_dictionaryWithObject_forKey_ = sel_registerName("dictionaryWithObject:forKey:");
public static final int /*long*/ sel_disableCursorRects = sel_registerName("disableCursorRects");
public static final int /*long*/ sel_disabledControlTextColor = sel_registerName("disabledControlTextColor");
public static final int /*long*/ sel_discardCursorRects = sel_registerName("discardCursorRects");
public static final int /*long*/ sel_display = sel_registerName("display");
public static final int /*long*/ sel_displayIfNeeded = sel_registerName("displayIfNeeded");
public static final int /*long*/ sel_displayRectIgnoringOpacity_inContext_ = sel_registerName("displayRectIgnoringOpacity:inContext:");
public static final int /*long*/ sel_distantFuture = sel_registerName("distantFuture");
public static final int /*long*/ sel_doCommandBySelector_ = sel_registerName("doCommandBySelector:");
public static final int /*long*/ sel_documentCursor = sel_registerName("documentCursor");
public static final int /*long*/ sel_documentSource = sel_registerName("documentSource");
public static final int /*long*/ sel_documentView = sel_registerName("documentView");
public static final int /*long*/ sel_documentViewShouldHandlePrint = sel_registerName("documentViewShouldHandlePrint");
public static final int /*long*/ sel_documentVisibleRect = sel_registerName("documentVisibleRect");
public static final int /*long*/ sel_doubleClickAtIndex_ = sel_registerName("doubleClickAtIndex:");
public static final int /*long*/ sel_doubleValue = sel_registerName("doubleValue");
public static final int /*long*/ sel_download = sel_registerName("download");
public static final int /*long*/ sel_download_decideDestinationWithSuggestedFilename_ = sel_registerName("download:decideDestinationWithSuggestedFilename:");
public static final int /*long*/ sel_dragImage_at_offset_event_pasteboard_source_slideBack_ = sel_registerName("dragImage:at:offset:event:pasteboard:source:slideBack:");
public static final int /*long*/ sel_dragImageForRowsWithIndexes_tableColumns_event_offset_ = sel_registerName("dragImageForRowsWithIndexes:tableColumns:event:offset:");
public static final int /*long*/ sel_dragSelectionWithEvent_offset_slideBack_ = sel_registerName("dragSelectionWithEvent:offset:slideBack:");
public static final int /*long*/ sel_draggedImage_beganAt_ = sel_registerName("draggedImage:beganAt:");
public static final int /*long*/ sel_draggedImage_endedAt_operation_ = sel_registerName("draggedImage:endedAt:operation:");
public static final int /*long*/ sel_draggingDestinationWindow = sel_registerName("draggingDestinationWindow");
public static final int /*long*/ sel_draggingEnded_ = sel_registerName("draggingEnded:");
public static final int /*long*/ sel_draggingEntered_ = sel_registerName("draggingEntered:");
public static final int /*long*/ sel_draggingExited_ = sel_registerName("draggingExited:");
public static final int /*long*/ sel_draggingLocation = sel_registerName("draggingLocation");
public static final int /*long*/ sel_draggingPasteboard = sel_registerName("draggingPasteboard");
public static final int /*long*/ sel_draggingSourceOperationMask = sel_registerName("draggingSourceOperationMask");
public static final int /*long*/ sel_draggingSourceOperationMaskForLocal_ = sel_registerName("draggingSourceOperationMaskForLocal:");
public static final int /*long*/ sel_draggingUpdated_ = sel_registerName("draggingUpdated:");
public static final int /*long*/ sel_drawAtPoint_ = sel_registerName("drawAtPoint:");
public static final int /*long*/ sel_drawAtPoint_fromRect_operation_fraction_ = sel_registerName("drawAtPoint:fromRect:operation:fraction:");
public static final int /*long*/ sel_drawBackgroundForGlyphRange_atPoint_ = sel_registerName("drawBackgroundForGlyphRange:atPoint:");
public static final int /*long*/ sel_drawFromPoint_toPoint_options_ = sel_registerName("drawFromPoint:toPoint:options:");
public static final int /*long*/ sel_drawGlyphsForGlyphRange_atPoint_ = sel_registerName("drawGlyphsForGlyphRange:atPoint:");
public static final int /*long*/ sel_drawImage_withFrame_inView_ = sel_registerName("drawImage:withFrame:inView:");
public static final int /*long*/ sel_drawInRect_ = sel_registerName("drawInRect:");
public static final int /*long*/ sel_drawInRect_angle_ = sel_registerName("drawInRect:angle:");
public static final int /*long*/ sel_drawInRect_fromRect_operation_fraction_ = sel_registerName("drawInRect:fromRect:operation:fraction:");
public static final int /*long*/ sel_drawInteriorWithFrame_inView_ = sel_registerName("drawInteriorWithFrame:inView:");
public static final int /*long*/ sel_drawRect_ = sel_registerName("drawRect:");
public static final int /*long*/ sel_drawSortIndicatorWithFrame_inView_ascending_priority_ = sel_registerName("drawSortIndicatorWithFrame:inView:ascending:priority:");
public static final int /*long*/ sel_drawStatusBarBackgroundInRect_withHighlight_ = sel_registerName("drawStatusBarBackgroundInRect:withHighlight:");
public static final int /*long*/ sel_drawWithFrame_inView_ = sel_registerName("drawWithFrame:inView:");
public static final int /*long*/ sel_drawingRectForBounds_ = sel_registerName("drawingRectForBounds:");
public static final int /*long*/ sel_elementAtIndex_associatedPoints_ = sel_registerName("elementAtIndex:associatedPoints:");
public static final int /*long*/ sel_elementCount = sel_registerName("elementCount");
public static final int /*long*/ sel_enableCursorRects = sel_registerName("enableCursorRects");
public static final int /*long*/ sel_enableFreedObjectCheck_ = sel_registerName("enableFreedObjectCheck:");
public static final int /*long*/ sel_endDocument = sel_registerName("endDocument");
public static final int /*long*/ sel_endEditing = sel_registerName("endEditing");
public static final int /*long*/ sel_endPage = sel_registerName("endPage");
public static final int /*long*/ sel_endSheet_returnCode_ = sel_registerName("endSheet:returnCode:");
public static final int /*long*/ sel_enterExitEventWithType_location_modifierFlags_timestamp_windowNumber_context_eventNumber_trackingNumber_userData_ = sel_registerName("enterExitEventWithType:location:modifierFlags:timestamp:windowNumber:context:eventNumber:trackingNumber:userData:");
public static final int /*long*/ sel_enumeratorAtPath_ = sel_registerName("enumeratorAtPath:");
public static final int /*long*/ sel_expandItem_ = sel_registerName("expandItem:");
public static final int /*long*/ sel_expandItem_expandChildren_ = sel_registerName("expandItem:expandChildren:");
public static final int /*long*/ sel_expansionFrameWithFrame_inView_ = sel_registerName("expansionFrameWithFrame:inView:");
public static final int /*long*/ sel_familyName = sel_registerName("familyName");
public static final int /*long*/ sel_fieldEditor_forObject_ = sel_registerName("fieldEditor:forObject:");
public static final int /*long*/ sel_fileExistsAtPath_isDirectory_ = sel_registerName("fileExistsAtPath:isDirectory:");
public static final int /*long*/ sel_fileSystemRepresentation = sel_registerName("fileSystemRepresentation");
public static final int /*long*/ sel_fileURLWithPath_ = sel_registerName("fileURLWithPath:");
public static final int /*long*/ sel_filename = sel_registerName("filename");
public static final int /*long*/ sel_filenames = sel_registerName("filenames");
public static final int /*long*/ sel_fill = sel_registerName("fill");
public static final int /*long*/ sel_fillRect_ = sel_registerName("fillRect:");
public static final int /*long*/ sel_finishLaunching = sel_registerName("finishLaunching");
public static final int /*long*/ sel_firstIndex = sel_registerName("firstIndex");
public static final int /*long*/ sel_firstRectForCharacterRange_ = sel_registerName("firstRectForCharacterRange:");
public static final int /*long*/ sel_firstResponder = sel_registerName("firstResponder");
public static final int /*long*/ sel_flagsChanged_ = sel_registerName("flagsChanged:");
public static final int /*long*/ sel_floatValue = sel_registerName("floatValue");
public static final int /*long*/ sel_flushBuffer = sel_registerName("flushBuffer");
public static final int /*long*/ sel_flushGraphics = sel_registerName("flushGraphics");
public static final int /*long*/ sel_font = sel_registerName("font");
public static final int /*long*/ sel_fontName = sel_registerName("fontName");
public static final int /*long*/ sel_fontWithFamily_traits_weight_size_ = sel_registerName("fontWithFamily:traits:weight:size:");
public static final int /*long*/ sel_fontWithName_size_ = sel_registerName("fontWithName:size:");
public static final int /*long*/ sel_frame = sel_registerName("frame");
public static final int /*long*/ sel_frameOfCellAtColumn_row_ = sel_registerName("frameOfCellAtColumn:row:");
public static final int /*long*/ sel_frameOfOutlineCellAtRow_ = sel_registerName("frameOfOutlineCellAtRow:");
public static final int /*long*/ sel_frameRectForContentRect_ = sel_registerName("frameRectForContentRect:");
public static final int /*long*/ sel_frameSizeForContentSize_hasHorizontalScroller_hasVerticalScroller_borderType_ = sel_registerName("frameSizeForContentSize:hasHorizontalScroller:hasVerticalScroller:borderType:");
public static final int /*long*/ sel_fullPathForApplication_ = sel_registerName("fullPathForApplication:");
public static final int /*long*/ sel_generalPasteboard = sel_registerName("generalPasteboard");
public static final int /*long*/ sel_getBitmapDataPlanes_ = sel_registerName("getBitmapDataPlanes:");
public static final int /*long*/ sel_getBytes_ = sel_registerName("getBytes:");
public static final int /*long*/ sel_getBytes_length_ = sel_registerName("getBytes:length:");
public static final int /*long*/ sel_getCharacters_ = sel_registerName("getCharacters:");
public static final int /*long*/ sel_getCharacters_range_ = sel_registerName("getCharacters:range:");
public static final int /*long*/ sel_getComponents_ = sel_registerName("getComponents:");
public static final int /*long*/ sel_getGlyphs_range_ = sel_registerName("getGlyphs:range:");
public static final int /*long*/ sel_getGlyphsInRange_glyphs_characterIndexes_glyphInscriptions_elasticBits_bidiLevels_ = sel_registerName("getGlyphsInRange:glyphs:characterIndexes:glyphInscriptions:elasticBits:bidiLevels:");
public static final int /*long*/ sel_getIndexes_maxCount_inIndexRange_ = sel_registerName("getIndexes:maxCount:inIndexRange:");
public static final int /*long*/ sel_getInfoForFile_application_type_ = sel_registerName("getInfoForFile:application:type:");
public static final int /*long*/ sel_getValues_forAttribute_forVirtualScreen_ = sel_registerName("getValues:forAttribute:forVirtualScreen:");
public static final int /*long*/ sel_glyphIndexForCharacterAtIndex_ = sel_registerName("glyphIndexForCharacterAtIndex:");
public static final int /*long*/ sel_glyphIndexForPoint_inTextContainer_fractionOfDistanceThroughGlyph_ = sel_registerName("glyphIndexForPoint:inTextContainer:fractionOfDistanceThroughGlyph:");
public static final int /*long*/ sel_glyphRangeForCharacterRange_actualCharacterRange_ = sel_registerName("glyphRangeForCharacterRange:actualCharacterRange:");
public static final int /*long*/ sel_glyphRangeForTextContainer_ = sel_registerName("glyphRangeForTextContainer:");
public static final int /*long*/ sel_goBack = sel_registerName("goBack");
public static final int /*long*/ sel_goForward = sel_registerName("goForward");
public static final int /*long*/ sel_graphicsContext = sel_registerName("graphicsContext");
public static final int /*long*/ sel_graphicsContextWithBitmapImageRep_ = sel_registerName("graphicsContextWithBitmapImageRep:");
public static final int /*long*/ sel_graphicsContextWithGraphicsPort_flipped_ = sel_registerName("graphicsContextWithGraphicsPort:flipped:");
public static final int /*long*/ sel_graphicsContextWithWindow_ = sel_registerName("graphicsContextWithWindow:");
public static final int /*long*/ sel_graphicsPort = sel_registerName("graphicsPort");
public static final int /*long*/ sel_greenComponent = sel_registerName("greenComponent");
public static final int /*long*/ sel_handleEvent_ = sel_registerName("handleEvent:");
public static final int /*long*/ sel_handleMouseEvent_ = sel_registerName("handleMouseEvent:");
public static final int /*long*/ sel_hasAlpha = sel_registerName("hasAlpha");
public static final int /*long*/ sel_hasMarkedText = sel_registerName("hasMarkedText");
public static final int /*long*/ sel_hasPassword = sel_registerName("hasPassword");
public static final int /*long*/ sel_hasShadow = sel_registerName("hasShadow");
public static final int /*long*/ sel_headerCell = sel_registerName("headerCell");
public static final int /*long*/ sel_headerRectOfColumn_ = sel_registerName("headerRectOfColumn:");
public static final int /*long*/ sel_headerView = sel_registerName("headerView");
public static final int /*long*/ sel_helpRequested_ = sel_registerName("helpRequested:");
public static final int /*long*/ sel_hide_ = sel_registerName("hide:");
public static final int /*long*/ sel_hideOtherApplications_ = sel_registerName("hideOtherApplications:");
public static final int /*long*/ sel_highlightColorInView_ = sel_registerName("highlightColorInView:");
public static final int /*long*/ sel_highlightColorWithFrame_inView_ = sel_registerName("highlightColorWithFrame:inView:");
public static final int /*long*/ sel_highlightSelectionInClipRect_ = sel_registerName("highlightSelectionInClipRect:");
public static final int /*long*/ sel_hitPart = sel_registerName("hitPart");
public static final int /*long*/ sel_hitTest_ = sel_registerName("hitTest:");
public static final int /*long*/ sel_hitTestForEvent_inRect_ofView_ = sel_registerName("hitTestForEvent:inRect:ofView:");
public static final int /*long*/ sel_host = sel_registerName("host");
public static final int /*long*/ sel_hourOfDay = sel_registerName("hourOfDay");
public static final int /*long*/ sel_iconForFile_ = sel_registerName("iconForFile:");
public static final int /*long*/ sel_iconForFileType_ = sel_registerName("iconForFileType:");
public static final int /*long*/ sel_ignore = sel_registerName("ignore");
public static final int /*long*/ sel_ignoreModifierKeysWhileDragging = sel_registerName("ignoreModifierKeysWhileDragging");
public static final int /*long*/ sel_image = sel_registerName("image");
public static final int /*long*/ sel_imageInterpolation = sel_registerName("imageInterpolation");
public static final int /*long*/ sel_imageNamed_ = sel_registerName("imageNamed:");
public static final int /*long*/ sel_imageRectForBounds_ = sel_registerName("imageRectForBounds:");
public static final int /*long*/ sel_imageRepWithData_ = sel_registerName("imageRepWithData:");
public static final int /*long*/ sel_imageablePageBounds = sel_registerName("imageablePageBounds");
public static final int /*long*/ sel_increment = sel_registerName("increment");
public static final int /*long*/ sel_indentationPerLevel = sel_registerName("indentationPerLevel");
public static final int /*long*/ sel_indexOfItemWithTarget_andAction_ = sel_registerName("indexOfItemWithTarget:andAction:");
public static final int /*long*/ sel_indexOfObjectIdenticalTo_ = sel_registerName("indexOfObjectIdenticalTo:");
public static final int /*long*/ sel_indexOfSelectedItem = sel_registerName("indexOfSelectedItem");
public static final int /*long*/ sel_infoDictionary = sel_registerName("infoDictionary");
public static final int /*long*/ sel_init = sel_registerName("init");
public static final int /*long*/ sel_initByReferencingFile_ = sel_registerName("initByReferencingFile:");
public static final int /*long*/ sel_initListDescriptor = sel_registerName("initListDescriptor");
public static final int /*long*/ sel_initWithAttributes_ = sel_registerName("initWithAttributes:");
public static final int /*long*/ sel_initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bitmapFormat_bytesPerRow_bitsPerPixel_ = sel_registerName("initWithBitmapDataPlanes:pixelsWide:pixelsHigh:bitsPerSample:samplesPerPixel:hasAlpha:isPlanar:colorSpaceName:bitmapFormat:bytesPerRow:bitsPerPixel:");
public static final int /*long*/ sel_initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bytesPerRow_bitsPerPixel_ = sel_registerName("initWithBitmapDataPlanes:pixelsWide:pixelsHigh:bitsPerSample:samplesPerPixel:hasAlpha:isPlanar:colorSpaceName:bytesPerRow:bitsPerPixel:");
public static final int /*long*/ sel_initWithCapacity_ = sel_registerName("initWithCapacity:");
public static final int /*long*/ sel_initWithCharacters_length_ = sel_registerName("initWithCharacters:length:");
public static final int /*long*/ sel_initWithContainerSize_ = sel_registerName("initWithContainerSize:");
public static final int /*long*/ sel_initWithContentRect_styleMask_backing_defer_ = sel_registerName("initWithContentRect:styleMask:backing:defer:");
public static final int /*long*/ sel_initWithContentRect_styleMask_backing_defer_screen_ = sel_registerName("initWithContentRect:styleMask:backing:defer:screen:");
public static final int /*long*/ sel_initWithContentsOfFile_ = sel_registerName("initWithContentsOfFile:");
public static final int /*long*/ sel_initWithData_ = sel_registerName("initWithData:");
public static final int /*long*/ sel_initWithDictionary_ = sel_registerName("initWithDictionary:");
public static final int /*long*/ sel_initWithFileWrapper_ = sel_registerName("initWithFileWrapper:");
public static final int /*long*/ sel_initWithFocusedViewRect_ = sel_registerName("initWithFocusedViewRect:");
public static final int /*long*/ sel_initWithFormat_shareContext_ = sel_registerName("initWithFormat:shareContext:");
public static final int /*long*/ sel_initWithFrame_ = sel_registerName("initWithFrame:");
public static final int /*long*/ sel_initWithFrame_frameName_groupName_ = sel_registerName("initWithFrame:frameName:groupName:");
public static final int /*long*/ sel_initWithFrame_pixelFormat_ = sel_registerName("initWithFrame:pixelFormat:");
public static final int /*long*/ sel_initWithFrame_pullsDown_ = sel_registerName("initWithFrame:pullsDown:");
public static final int /*long*/ sel_initWithIdentifier_ = sel_registerName("initWithIdentifier:");
public static final int /*long*/ sel_initWithImage_hotSpot_ = sel_registerName("initWithImage:hotSpot:");
public static final int /*long*/ sel_initWithIndex_ = sel_registerName("initWithIndex:");
public static final int /*long*/ sel_initWithIndexesInRange_ = sel_registerName("initWithIndexesInRange:");
public static final int /*long*/ sel_initWithItemIdentifier_ = sel_registerName("initWithItemIdentifier:");
public static final int /*long*/ sel_initWithRect_options_owner_userInfo_ = sel_registerName("initWithRect:options:owner:userInfo:");
public static final int /*long*/ sel_initWithSize_ = sel_registerName("initWithSize:");
public static final int /*long*/ sel_initWithStartingColor_endingColor_ = sel_registerName("initWithStartingColor:endingColor:");
public static final int /*long*/ sel_initWithString_ = sel_registerName("initWithString:");
public static final int /*long*/ sel_initWithString_attributes_ = sel_registerName("initWithString:attributes:");
public static final int /*long*/ sel_initWithTitle_ = sel_registerName("initWithTitle:");
public static final int /*long*/ sel_initWithTitle_action_keyEquivalent_ = sel_registerName("initWithTitle:action:keyEquivalent:");
public static final int /*long*/ sel_initWithTransform_ = sel_registerName("initWithTransform:");
public static final int /*long*/ sel_initWithType_location_ = sel_registerName("initWithType:location:");
public static final int /*long*/ sel_initWithURL_ = sel_registerName("initWithURL:");
public static final int /*long*/ sel_insertItem_atIndex_ = sel_registerName("insertItem:atIndex:");
public static final int /*long*/ sel_insertItemWithItemIdentifier_atIndex_ = sel_registerName("insertItemWithItemIdentifier:atIndex:");
public static final int /*long*/ sel_insertItemWithObjectValue_atIndex_ = sel_registerName("insertItemWithObjectValue:atIndex:");
public static final int /*long*/ sel_insertTabViewItem_atIndex_ = sel_registerName("insertTabViewItem:atIndex:");
public static final int /*long*/ sel_insertText_ = sel_registerName("insertText:");
public static final int /*long*/ sel_intValue = sel_registerName("intValue");
public static final int /*long*/ sel_integerValue = sel_registerName("integerValue");
public static final int /*long*/ sel_intercellSpacing = sel_registerName("intercellSpacing");
public static final int /*long*/ sel_interpretKeyEvents_ = sel_registerName("interpretKeyEvents:");
public static final int /*long*/ sel_invalidate = sel_registerName("invalidate");
public static final int /*long*/ sel_invalidateShadow = sel_registerName("invalidateShadow");
public static final int /*long*/ sel_invert = sel_registerName("invert");
public static final int /*long*/ sel_isActive = sel_registerName("isActive");
public static final int /*long*/ sel_isDocumentEdited = sel_registerName("isDocumentEdited");
public static final int /*long*/ sel_isDrawingToScreen = sel_registerName("isDrawingToScreen");
public static final int /*long*/ sel_isEmpty = sel_registerName("isEmpty");
public static final int /*long*/ sel_isEqual_ = sel_registerName("isEqual:");
public static final int /*long*/ sel_isEqualTo_ = sel_registerName("isEqualTo:");
public static final int /*long*/ sel_isEqualToString_ = sel_registerName("isEqualToString:");
public static final int /*long*/ sel_isFilePackageAtPath_ = sel_registerName("isFilePackageAtPath:");
public static final int /*long*/ sel_isFlipped = sel_registerName("isFlipped");
public static final int /*long*/ sel_isHidden = sel_registerName("isHidden");
public static final int /*long*/ sel_isHiddenOrHasHiddenAncestor = sel_registerName("isHiddenOrHasHiddenAncestor");
public static final int /*long*/ sel_isHighlighted = sel_registerName("isHighlighted");
public static final int /*long*/ sel_isItemExpanded_ = sel_registerName("isItemExpanded:");
public static final int /*long*/ sel_isKeyWindow = sel_registerName("isKeyWindow");
public static final int /*long*/ sel_isKindOfClass_ = sel_registerName("isKindOfClass:");
public static final int /*long*/ sel_isMainThread = sel_registerName("isMainThread");
public static final int /*long*/ sel_isMiniaturized = sel_registerName("isMiniaturized");
public static final int /*long*/ sel_isOpaque = sel_registerName("isOpaque");
public static final int /*long*/ sel_isPlanar = sel_registerName("isPlanar");
public static final int /*long*/ sel_isRowSelected_ = sel_registerName("isRowSelected:");
public static final int /*long*/ sel_isRunning = sel_registerName("isRunning");
public static final int /*long*/ sel_isSessionOnly = sel_registerName("isSessionOnly");
public static final int /*long*/ sel_isVisible = sel_registerName("isVisible");
public static final int /*long*/ sel_isZoomed = sel_registerName("isZoomed");
public static final int /*long*/ sel_itemArray = sel_registerName("itemArray");
public static final int /*long*/ sel_itemAtIndex_ = sel_registerName("itemAtIndex:");
public static final int /*long*/ sel_itemAtRow_ = sel_registerName("itemAtRow:");
public static final int /*long*/ sel_itemIdentifier = sel_registerName("itemIdentifier");
public static final int /*long*/ sel_itemObjectValueAtIndex_ = sel_registerName("itemObjectValueAtIndex:");
public static final int /*long*/ sel_itemTitleAtIndex_ = sel_registerName("itemTitleAtIndex:");
public static final int /*long*/ sel_jobDisposition = sel_registerName("jobDisposition");
public static final int /*long*/ sel_keyCode = sel_registerName("keyCode");
public static final int /*long*/ sel_keyDown_ = sel_registerName("keyDown:");
public static final int /*long*/ sel_keyEquivalent = sel_registerName("keyEquivalent");
public static final int /*long*/ sel_keyEquivalentModifierMask = sel_registerName("keyEquivalentModifierMask");
public static final int /*long*/ sel_keyUp_ = sel_registerName("keyUp:");
public static final int /*long*/ sel_keyWindow = sel_registerName("keyWindow");
public static final int /*long*/ sel_knobThickness = sel_registerName("knobThickness");
public static final int /*long*/ sel_lastPathComponent = sel_registerName("lastPathComponent");
public static final int /*long*/ sel_layoutManager = sel_registerName("layoutManager");
public static final int /*long*/ sel_leading = sel_registerName("leading");
public static final int /*long*/ sel_length = sel_registerName("length");
public static final int /*long*/ sel_levelForItem_ = sel_registerName("levelForItem:");
public static final int /*long*/ sel_lineFragmentUsedRectForGlyphAtIndex_effectiveRange_ = sel_registerName("lineFragmentUsedRectForGlyphAtIndex:effectiveRange:");
public static final int /*long*/ sel_lineFragmentUsedRectForGlyphAtIndex_effectiveRange_withoutAdditionalLayout_ = sel_registerName("lineFragmentUsedRectForGlyphAtIndex:effectiveRange:withoutAdditionalLayout:");
public static final int /*long*/ sel_lineToPoint_ = sel_registerName("lineToPoint:");
public static final int /*long*/ sel_linkTextAttributes = sel_registerName("linkTextAttributes");
public static final int /*long*/ sel_loadHTMLString_baseURL_ = sel_registerName("loadHTMLString:baseURL:");
public static final int /*long*/ sel_loadNibFile_externalNameTable_withZone_ = sel_registerName("loadNibFile:externalNameTable:withZone:");
public static final int /*long*/ sel_loadRequest_ = sel_registerName("loadRequest:");
public static final int /*long*/ sel_localizedDescription = sel_registerName("localizedDescription");
public static final int /*long*/ sel_location = sel_registerName("location");
public static final int /*long*/ sel_locationForGlyphAtIndex_ = sel_registerName("locationForGlyphAtIndex:");
public static final int /*long*/ sel_locationInWindow = sel_registerName("locationInWindow");
public static final int /*long*/ sel_lockFocus = sel_registerName("lockFocus");
public static final int /*long*/ sel_lowercaseString = sel_registerName("lowercaseString");
public static final int /*long*/ sel_mainBundle = sel_registerName("mainBundle");
public static final int /*long*/ sel_mainFrame = sel_registerName("mainFrame");
public static final int /*long*/ sel_mainMenu = sel_registerName("mainMenu");
public static final int /*long*/ sel_mainRunLoop = sel_registerName("mainRunLoop");
public static final int /*long*/ sel_mainScreen = sel_registerName("mainScreen");
public static final int /*long*/ sel_makeCurrentContext = sel_registerName("makeCurrentContext");
public static final int /*long*/ sel_makeFirstResponder_ = sel_registerName("makeFirstResponder:");
public static final int /*long*/ sel_makeKeyAndOrderFront_ = sel_registerName("makeKeyAndOrderFront:");
public static final int /*long*/ sel_markedRange = sel_registerName("markedRange");
public static final int /*long*/ sel_markedTextAttributes = sel_registerName("markedTextAttributes");
public static final int /*long*/ sel_maxValue = sel_registerName("maxValue");
public static final int /*long*/ sel_maximum = sel_registerName("maximum");
public static final int /*long*/ sel_maximumFractionDigits = sel_registerName("maximumFractionDigits");
public static final int /*long*/ sel_maximumIntegerDigits = sel_registerName("maximumIntegerDigits");
public static final int /*long*/ sel_menu = sel_registerName("menu");
public static final int /*long*/ sel_menu_willHighlightItem_ = sel_registerName("menu:willHighlightItem:");
public static final int /*long*/ sel_menuDidClose_ = sel_registerName("menuDidClose:");
public static final int /*long*/ sel_menuForEvent_ = sel_registerName("menuForEvent:");
public static final int /*long*/ sel_menuNeedsUpdate_ = sel_registerName("menuNeedsUpdate:");
public static final int /*long*/ sel_menuWillOpen_ = sel_registerName("menuWillOpen:");
public static final int /*long*/ sel_metaKey = sel_registerName("metaKey");
public static final int /*long*/ sel_minFrameWidthWithTitle_styleMask_ = sel_registerName("minFrameWidthWithTitle:styleMask:");
public static final int /*long*/ sel_minSize = sel_registerName("minSize");
public static final int /*long*/ sel_minValue = sel_registerName("minValue");
public static final int /*long*/ sel_miniaturize_ = sel_registerName("miniaturize:");
public static final int /*long*/ sel_minimum = sel_registerName("minimum");
public static final int /*long*/ sel_minimumSize = sel_registerName("minimumSize");
public static final int /*long*/ sel_minuteOfHour = sel_registerName("minuteOfHour");
public static final int /*long*/ sel_modifierFlags = sel_registerName("modifierFlags");
public static final int /*long*/ sel_monthOfYear = sel_registerName("monthOfYear");
public static final int /*long*/ sel_mouseDown_ = sel_registerName("mouseDown:");
public static final int /*long*/ sel_mouseDragged_ = sel_registerName("mouseDragged:");
public static final int /*long*/ sel_mouseEntered_ = sel_registerName("mouseEntered:");
public static final int /*long*/ sel_mouseExited_ = sel_registerName("mouseExited:");
public static final int /*long*/ sel_mouseLocation = sel_registerName("mouseLocation");
public static final int /*long*/ sel_mouseLocationOutsideOfEventStream = sel_registerName("mouseLocationOutsideOfEventStream");
public static final int /*long*/ sel_mouseMoved_ = sel_registerName("mouseMoved:");
public static final int /*long*/ sel_mouseUp_ = sel_registerName("mouseUp:");
public static final int /*long*/ sel_moveColumn_toColumn_ = sel_registerName("moveColumn:toColumn:");
public static final int /*long*/ sel_moveToBeginningOfParagraph_ = sel_registerName("moveToBeginningOfParagraph:");
public static final int /*long*/ sel_moveToEndOfParagraph_ = sel_registerName("moveToEndOfParagraph:");
public static final int /*long*/ sel_moveToPoint_ = sel_registerName("moveToPoint:");
public static final int /*long*/ sel_moveUp_ = sel_registerName("moveUp:");
public static final int /*long*/ sel_mutableCopy = sel_registerName("mutableCopy");
public static final int /*long*/ sel_mutableString = sel_registerName("mutableString");
public static final int /*long*/ sel_name = sel_registerName("name");
public static final int /*long*/ sel_nextEventMatchingMask_untilDate_inMode_dequeue_ = sel_registerName("nextEventMatchingMask:untilDate:inMode:dequeue:");
public static final int /*long*/ sel_nextObject = sel_registerName("nextObject");
public static final int /*long*/ sel_nextState = sel_registerName("nextState");
public static final int /*long*/ sel_nextWordFromIndex_forward_ = sel_registerName("nextWordFromIndex:forward:");
public static final int /*long*/ sel_noResponderFor_ = sel_registerName("noResponderFor:");
public static final int /*long*/ sel_noteNumberOfRowsChanged = sel_registerName("noteNumberOfRowsChanged");
public static final int /*long*/ sel_numberOfColumns = sel_registerName("numberOfColumns");
public static final int /*long*/ sel_numberOfComponents = sel_registerName("numberOfComponents");
public static final int /*long*/ sel_numberOfGlyphs = sel_registerName("numberOfGlyphs");
public static final int /*long*/ sel_numberOfItems = sel_registerName("numberOfItems");
public static final int /*long*/ sel_numberOfPlanes = sel_registerName("numberOfPlanes");
public static final int /*long*/ sel_numberOfRows = sel_registerName("numberOfRows");
public static final int /*long*/ sel_numberOfRowsInTableView_ = sel_registerName("numberOfRowsInTableView:");
public static final int /*long*/ sel_numberOfSelectedRows = sel_registerName("numberOfSelectedRows");
public static final int /*long*/ sel_numberOfVisibleItems = sel_registerName("numberOfVisibleItems");
public static final int /*long*/ sel_numberWithBool_ = sel_registerName("numberWithBool:");
public static final int /*long*/ sel_numberWithDouble_ = sel_registerName("numberWithDouble:");
public static final int /*long*/ sel_numberWithInt_ = sel_registerName("numberWithInt:");
public static final int /*long*/ sel_numberWithInteger_ = sel_registerName("numberWithInteger:");
public static final int /*long*/ sel_objCType = sel_registerName("objCType");
public static final int /*long*/ sel_object = sel_registerName("object");
public static final int /*long*/ sel_objectAtIndex_ = sel_registerName("objectAtIndex:");
public static final int /*long*/ sel_objectEnumerator = sel_registerName("objectEnumerator");
public static final int /*long*/ sel_objectForInfoDictionaryKey_ = sel_registerName("objectForInfoDictionaryKey:");
public static final int /*long*/ sel_objectForKey_ = sel_registerName("objectForKey:");
public static final int /*long*/ sel_objectValues = sel_registerName("objectValues");
public static final int /*long*/ sel_openFile_withApplication_ = sel_registerName("openFile:withApplication:");
public static final int /*long*/ sel_openGLContext = sel_registerName("openGLContext");
public static final int /*long*/ sel_openPanel = sel_registerName("openPanel");
public static final int /*long*/ sel_openURL_ = sel_registerName("openURL:");
public static final int /*long*/ sel_openURLs_withAppBundleIdentifier_options_additionalEventParamDescriptor_launchIdentifiers_ = sel_registerName("openURLs:withAppBundleIdentifier:options:additionalEventParamDescriptor:launchIdentifiers:");
public static final int /*long*/ sel_options = sel_registerName("options");
public static final int /*long*/ sel_orderBack_ = sel_registerName("orderBack:");
public static final int /*long*/ sel_orderFront_ = sel_registerName("orderFront:");
public static final int /*long*/ sel_orderFrontRegardless = sel_registerName("orderFrontRegardless");
public static final int /*long*/ sel_orderFrontStandardAboutPanel_ = sel_registerName("orderFrontStandardAboutPanel:");
public static final int /*long*/ sel_orderOut_ = sel_registerName("orderOut:");
public static final int /*long*/ sel_orderWindow_relativeTo_ = sel_registerName("orderWindow:relativeTo:");
public static final int /*long*/ sel_orderedWindows = sel_registerName("orderedWindows");
public static final int /*long*/ sel_orientation = sel_registerName("orientation");
public static final int /*long*/ sel_otherEventWithType_location_modifierFlags_timestamp_windowNumber_context_subtype_data1_data2_ = sel_registerName("otherEventWithType:location:modifierFlags:timestamp:windowNumber:context:subtype:data1:data2:");
public static final int /*long*/ sel_otherMouseDown_ = sel_registerName("otherMouseDown:");
public static final int /*long*/ sel_otherMouseDragged_ = sel_registerName("otherMouseDragged:");
public static final int /*long*/ sel_otherMouseUp_ = sel_registerName("otherMouseUp:");
public static final int /*long*/ sel_outlineTableColumn = sel_registerName("outlineTableColumn");
public static final int /*long*/ sel_outlineView_acceptDrop_item_childIndex_ = sel_registerName("outlineView:acceptDrop:item:childIndex:");
public static final int /*long*/ sel_outlineView_child_ofItem_ = sel_registerName("outlineView:child:ofItem:");
public static final int /*long*/ sel_outlineView_didClickTableColumn_ = sel_registerName("outlineView:didClickTableColumn:");
public static final int /*long*/ sel_outlineView_isItemExpandable_ = sel_registerName("outlineView:isItemExpandable:");
public static final int /*long*/ sel_outlineView_numberOfChildrenOfItem_ = sel_registerName("outlineView:numberOfChildrenOfItem:");
public static final int /*long*/ sel_outlineView_objectValueForTableColumn_byItem_ = sel_registerName("outlineView:objectValueForTableColumn:byItem:");
public static final int /*long*/ sel_outlineView_setObjectValue_forTableColumn_byItem_ = sel_registerName("outlineView:setObjectValue:forTableColumn:byItem:");
public static final int /*long*/ sel_outlineView_shouldCollapseItem_ = sel_registerName("outlineView:shouldCollapseItem:");
public static final int /*long*/ sel_outlineView_shouldExpandItem_ = sel_registerName("outlineView:shouldExpandItem:");
public static final int /*long*/ sel_outlineView_validateDrop_proposedItem_proposedChildIndex_ = sel_registerName("outlineView:validateDrop:proposedItem:proposedChildIndex:");
public static final int /*long*/ sel_outlineView_willDisplayCell_forTableColumn_item_ = sel_registerName("outlineView:willDisplayCell:forTableColumn:item:");
public static final int /*long*/ sel_outlineView_writeItems_toPasteboard_ = sel_registerName("outlineView:writeItems:toPasteboard:");
public static final int /*long*/ sel_outlineViewColumnDidMove_ = sel_registerName("outlineViewColumnDidMove:");
public static final int /*long*/ sel_outlineViewColumnDidResize_ = sel_registerName("outlineViewColumnDidResize:");
public static final int /*long*/ sel_outlineViewItemDidExpand_ = sel_registerName("outlineViewItemDidExpand:");
public static final int /*long*/ sel_outlineViewSelectionDidChange_ = sel_registerName("outlineViewSelectionDidChange:");
public static final int /*long*/ sel_owner = sel_registerName("owner");
public static final int /*long*/ sel_pageDown_ = sel_registerName("pageDown:");
public static final int /*long*/ sel_pageTitle = sel_registerName("pageTitle");
public static final int /*long*/ sel_pageUp_ = sel_registerName("pageUp:");
public static final int /*long*/ sel_panel_shouldShowFilename_ = sel_registerName("panel:shouldShowFilename:");
public static final int /*long*/ sel_panelConvertFont_ = sel_registerName("panelConvertFont:");
public static final int /*long*/ sel_paperSize = sel_registerName("paperSize");
public static final int /*long*/ sel_paragraphs = sel_registerName("paragraphs");
public static final int /*long*/ sel_parentWindow = sel_registerName("parentWindow");
public static final int /*long*/ sel_password = sel_registerName("password");
public static final int /*long*/ sel_paste_ = sel_registerName("paste:");
public static final int /*long*/ sel_pasteboard_provideDataForType_ = sel_registerName("pasteboard:provideDataForType:");
public static final int /*long*/ sel_pasteboardWithName_ = sel_registerName("pasteboardWithName:");
public static final int /*long*/ sel_pathExtension = sel_registerName("pathExtension");
public static final int /*long*/ sel_pathForResource_ofType_ = sel_registerName("pathForResource:ofType:");
public static final int /*long*/ sel_performDragOperation_ = sel_registerName("performDragOperation:");
public static final int /*long*/ sel_performSelectorOnMainThread_withObject_waitUntilDone_ = sel_registerName("performSelectorOnMainThread:withObject:waitUntilDone:");
public static final int /*long*/ sel_pixelsHigh = sel_registerName("pixelsHigh");
public static final int /*long*/ sel_pixelsWide = sel_registerName("pixelsWide");
public static final int /*long*/ sel_pointSize = sel_registerName("pointSize");
public static final int /*long*/ sel_pointValue = sel_registerName("pointValue");
public static final int /*long*/ sel_pointingHandCursor = sel_registerName("pointingHandCursor");
public static final int /*long*/ sel_pop = sel_registerName("pop");
public static final int /*long*/ sel_popUpContextMenu_withEvent_forView_ = sel_registerName("popUpContextMenu:withEvent:forView:");
public static final int /*long*/ sel_popUpStatusItemMenu_ = sel_registerName("popUpStatusItemMenu:");
public static final int /*long*/ sel_port = sel_registerName("port");
public static final int /*long*/ sel_postEvent_atStart_ = sel_registerName("postEvent:atStart:");
public static final int /*long*/ sel_prependTransform_ = sel_registerName("prependTransform:");
public static final int /*long*/ sel_preventDefault = sel_registerName("preventDefault");
public static final int /*long*/ sel_previousFailureCount = sel_registerName("previousFailureCount");
public static final int /*long*/ sel_printDocumentView = sel_registerName("printDocumentView");
public static final int /*long*/ sel_printOperationWithPrintInfo_ = sel_registerName("printOperationWithPrintInfo:");
public static final int /*long*/ sel_printOperationWithView_printInfo_ = sel_registerName("printOperationWithView:printInfo:");
public static final int /*long*/ sel_printPanel = sel_registerName("printPanel");
public static final int /*long*/ sel_printer = sel_registerName("printer");
public static final int /*long*/ sel_printerNames = sel_registerName("printerNames");
public static final int /*long*/ sel_printerWithName_ = sel_registerName("printerWithName:");
public static final int /*long*/ sel_propertyListForType_ = sel_registerName("propertyListForType:");
public static final int /*long*/ sel_proposedCredential = sel_registerName("proposedCredential");
public static final int /*long*/ sel_protectionSpace = sel_registerName("protectionSpace");
public static final int /*long*/ sel_push = sel_registerName("push");
public static final int /*long*/ sel_rangeValue = sel_registerName("rangeValue");
public static final int /*long*/ sel_realm = sel_registerName("realm");
public static final int /*long*/ sel_recentSearches = sel_registerName("recentSearches");
public static final int /*long*/ sel_rectArrayForCharacterRange_withinSelectedCharacterRange_inTextContainer_rectCount_ = sel_registerName("rectArrayForCharacterRange:withinSelectedCharacterRange:inTextContainer:rectCount:");
public static final int /*long*/ sel_rectOfColumn_ = sel_registerName("rectOfColumn:");
public static final int /*long*/ sel_rectOfRow_ = sel_registerName("rectOfRow:");
public static final int /*long*/ sel_rectValue = sel_registerName("rectValue");
public static final int /*long*/ sel_redComponent = sel_registerName("redComponent");
public static final int /*long*/ sel_reflectScrolledClipView_ = sel_registerName("reflectScrolledClipView:");
public static final int /*long*/ sel_registerForDraggedTypes_ = sel_registerName("registerForDraggedTypes:");
public static final int /*long*/ sel_release = sel_registerName("release");
public static final int /*long*/ sel_reload_ = sel_registerName("reload:");
public static final int /*long*/ sel_reloadData = sel_registerName("reloadData");
public static final int /*long*/ sel_reloadItem_reloadChildren_ = sel_registerName("reloadItem:reloadChildren:");
public static final int /*long*/ sel_removeAllItems = sel_registerName("removeAllItems");
public static final int /*long*/ sel_removeAllPoints = sel_registerName("removeAllPoints");
public static final int /*long*/ sel_removeAttribute_range_ = sel_registerName("removeAttribute:range:");
public static final int /*long*/ sel_removeChildWindow_ = sel_registerName("removeChildWindow:");
public static final int /*long*/ sel_removeFromSuperview = sel_registerName("removeFromSuperview");
public static final int /*long*/ sel_removeItem_ = sel_registerName("removeItem:");
public static final int /*long*/ sel_removeItemAtIndex_ = sel_registerName("removeItemAtIndex:");
public static final int /*long*/ sel_removeItemAtPath_error_ = sel_registerName("removeItemAtPath:error:");
public static final int /*long*/ sel_removeLastObject = sel_registerName("removeLastObject");
public static final int /*long*/ sel_removeObject_ = sel_registerName("removeObject:");
public static final int /*long*/ sel_removeObjectAtIndex_ = sel_registerName("removeObjectAtIndex:");
public static final int /*long*/ sel_removeObjectForKey_ = sel_registerName("removeObjectForKey:");
public static final int /*long*/ sel_removeObjectIdenticalTo_ = sel_registerName("removeObjectIdenticalTo:");
public static final int /*long*/ sel_removeObserver_ = sel_registerName("removeObserver:");
public static final int /*long*/ sel_removeRepresentation_ = sel_registerName("removeRepresentation:");
public static final int /*long*/ sel_removeStatusItem_ = sel_registerName("removeStatusItem:");
public static final int /*long*/ sel_removeTabViewItem_ = sel_registerName("removeTabViewItem:");
public static final int /*long*/ sel_removeTableColumn_ = sel_registerName("removeTableColumn:");
public static final int /*long*/ sel_removeTemporaryAttribute_forCharacterRange_ = sel_registerName("removeTemporaryAttribute:forCharacterRange:");
public static final int /*long*/ sel_removeTrackingArea_ = sel_registerName("removeTrackingArea:");
public static final int /*long*/ sel_replaceCharactersInRange_withString_ = sel_registerName("replaceCharactersInRange:withString:");
public static final int /*long*/ sel_representation = sel_registerName("representation");
public static final int /*long*/ sel_representations = sel_registerName("representations");
public static final int /*long*/ sel_request = sel_registerName("request");
public static final int /*long*/ sel_requestWithURL_ = sel_registerName("requestWithURL:");
public static final int /*long*/ sel_resetCursorRects = sel_registerName("resetCursorRects");
public static final int /*long*/ sel_resignFirstResponder = sel_registerName("resignFirstResponder");
public static final int /*long*/ sel_resizeDownCursor = sel_registerName("resizeDownCursor");
public static final int /*long*/ sel_resizeLeftCursor = sel_registerName("resizeLeftCursor");
public static final int /*long*/ sel_resizeLeftRightCursor = sel_registerName("resizeLeftRightCursor");
public static final int /*long*/ sel_resizeRightCursor = sel_registerName("resizeRightCursor");
public static final int /*long*/ sel_resizeUpCursor = sel_registerName("resizeUpCursor");
public static final int /*long*/ sel_resizeUpDownCursor = sel_registerName("resizeUpDownCursor");
public static final int /*long*/ sel_resizingMask = sel_registerName("resizingMask");
public static final int /*long*/ sel_resourcePath = sel_registerName("resourcePath");
public static final int /*long*/ sel_respondsToSelector_ = sel_registerName("respondsToSelector:");
public static final int /*long*/ sel_restoreGraphicsState = sel_registerName("restoreGraphicsState");
public static final int /*long*/ sel_retain = sel_registerName("retain");
public static final int /*long*/ sel_retainCount = sel_registerName("retainCount");
public static final int /*long*/ sel_rightMouseDown_ = sel_registerName("rightMouseDown:");
public static final int /*long*/ sel_rightMouseDragged_ = sel_registerName("rightMouseDragged:");
public static final int /*long*/ sel_rightMouseUp_ = sel_registerName("rightMouseUp:");
public static final int /*long*/ sel_rotateByDegrees_ = sel_registerName("rotateByDegrees:");
public static final int /*long*/ sel_rowAtPoint_ = sel_registerName("rowAtPoint:");
public static final int /*long*/ sel_rowForItem_ = sel_registerName("rowForItem:");
public static final int /*long*/ sel_rowHeight = sel_registerName("rowHeight");
public static final int /*long*/ sel_rowsInRect_ = sel_registerName("rowsInRect:");
public static final int /*long*/ sel_run = sel_registerName("run");
public static final int /*long*/ sel_runModal = sel_registerName("runModal");
public static final int /*long*/ sel_runModalForWindow_ = sel_registerName("runModalForWindow:");
public static final int /*long*/ sel_runModalWithPrintInfo_ = sel_registerName("runModalWithPrintInfo:");
public static final int /*long*/ sel_runMode_beforeDate_ = sel_registerName("runMode:beforeDate:");
public static final int /*long*/ sel_runOperation = sel_registerName("runOperation");
public static final int /*long*/ sel_samplesPerPixel = sel_registerName("samplesPerPixel");
public static final int /*long*/ sel_saveGraphicsState = sel_registerName("saveGraphicsState");
public static final int /*long*/ sel_savePanel = sel_registerName("savePanel");
public static final int /*long*/ sel_scaleXBy_yBy_ = sel_registerName("scaleXBy:yBy:");
public static final int /*long*/ sel_scheduledTimerWithTimeInterval_target_selector_userInfo_repeats_ = sel_registerName("scheduledTimerWithTimeInterval:target:selector:userInfo:repeats:");
public static final int /*long*/ sel_screen = sel_registerName("screen");
public static final int /*long*/ sel_screens = sel_registerName("screens");
public static final int /*long*/ sel_scrollColumnToVisible_ = sel_registerName("scrollColumnToVisible:");
public static final int /*long*/ sel_scrollPoint_ = sel_registerName("scrollPoint:");
public static final int /*long*/ sel_scrollRangeToVisible_ = sel_registerName("scrollRangeToVisible:");
public static final int /*long*/ sel_scrollRectToVisible_ = sel_registerName("scrollRectToVisible:");
public static final int /*long*/ sel_scrollRowToVisible_ = sel_registerName("scrollRowToVisible:");
public static final int /*long*/ sel_scrollToPoint_ = sel_registerName("scrollToPoint:");
public static final int /*long*/ sel_scrollWheel_ = sel_registerName("scrollWheel:");
public static final int /*long*/ sel_scrollerWidth = sel_registerName("scrollerWidth");
public static final int /*long*/ sel_scrollerWidthForControlSize_ = sel_registerName("scrollerWidthForControlSize:");
public static final int /*long*/ sel_searchButtonCell = sel_registerName("searchButtonCell");
public static final int /*long*/ sel_searchTextRectForBounds_ = sel_registerName("searchTextRectForBounds:");
public static final int /*long*/ sel_secondOfMinute = sel_registerName("secondOfMinute");
public static final int /*long*/ sel_secondarySelectedControlColor = sel_registerName("secondarySelectedControlColor");
public static final int /*long*/ sel_selectAll_ = sel_registerName("selectAll:");
public static final int /*long*/ sel_selectItem_ = sel_registerName("selectItem:");
public static final int /*long*/ sel_selectItemAtIndex_ = sel_registerName("selectItemAtIndex:");
public static final int /*long*/ sel_selectRowIndexes_byExtendingSelection_ = sel_registerName("selectRowIndexes:byExtendingSelection:");
public static final int /*long*/ sel_selectTabViewItemAtIndex_ = sel_registerName("selectTabViewItemAtIndex:");
public static final int /*long*/ sel_selectText_ = sel_registerName("selectText:");
public static final int /*long*/ sel_selectedControlColor = sel_registerName("selectedControlColor");
public static final int /*long*/ sel_selectedControlTextColor = sel_registerName("selectedControlTextColor");
public static final int /*long*/ sel_selectedRange = sel_registerName("selectedRange");
public static final int /*long*/ sel_selectedRow = sel_registerName("selectedRow");
public static final int /*long*/ sel_selectedRowIndexes = sel_registerName("selectedRowIndexes");
public static final int /*long*/ sel_selectedTabViewItem = sel_registerName("selectedTabViewItem");
public static final int /*long*/ sel_selectedTextBackgroundColor = sel_registerName("selectedTextBackgroundColor");
public static final int /*long*/ sel_selectedTextColor = sel_registerName("selectedTextColor");
public static final int /*long*/ sel_sendAction_to_ = sel_registerName("sendAction:to:");
public static final int /*long*/ sel_sendEvent_ = sel_registerName("sendEvent:");
public static final int /*long*/ sel_sender = sel_registerName("sender");
public static final int /*long*/ sel_separatorItem = sel_registerName("separatorItem");
public static final int /*long*/ sel_set = sel_registerName("set");
public static final int /*long*/ sel_setAcceptsMouseMovedEvents_ = sel_registerName("setAcceptsMouseMovedEvents:");
public static final int /*long*/ sel_setAccessoryView_ = sel_registerName("setAccessoryView:");
public static final int /*long*/ sel_setAction_ = sel_registerName("setAction:");
public static final int /*long*/ sel_setAlertStyle_ = sel_registerName("setAlertStyle:");
public static final int /*long*/ sel_setAlignment_ = sel_registerName("setAlignment:");
public static final int /*long*/ sel_setAllowsColumnReordering_ = sel_registerName("setAllowsColumnReordering:");
public static final int /*long*/ sel_setAllowsFloats_ = sel_registerName("setAllowsFloats:");
public static final int /*long*/ sel_setAllowsMixedState_ = sel_registerName("setAllowsMixedState:");
public static final int /*long*/ sel_setAllowsMultipleSelection_ = sel_registerName("setAllowsMultipleSelection:");
public static final int /*long*/ sel_setAllowsUserCustomization_ = sel_registerName("setAllowsUserCustomization:");
public static final int /*long*/ sel_setAlpha_ = sel_registerName("setAlpha:");
public static final int /*long*/ sel_setAlphaValue_ = sel_registerName("setAlphaValue:");
public static final int /*long*/ sel_setApplicationIconImage_ = sel_registerName("setApplicationIconImage:");
public static final int /*long*/ sel_setApplicationNameForUserAgent_ = sel_registerName("setApplicationNameForUserAgent:");
public static final int /*long*/ sel_setAttributedString_ = sel_registerName("setAttributedString:");
public static final int /*long*/ sel_setAttributedStringValue_ = sel_registerName("setAttributedStringValue:");
public static final int /*long*/ sel_setAttributedTitle_ = sel_registerName("setAttributedTitle:");
public static final int /*long*/ sel_setAutoenablesItems_ = sel_registerName("setAutoenablesItems:");
public static final int /*long*/ sel_setAutohidesScrollers_ = sel_registerName("setAutohidesScrollers:");
public static final int /*long*/ sel_setAutoresizesOutlineColumn_ = sel_registerName("setAutoresizesOutlineColumn:");
public static final int /*long*/ sel_setAutoresizesSubviews_ = sel_registerName("setAutoresizesSubviews:");
public static final int /*long*/ sel_setAutoresizingMask_ = sel_registerName("setAutoresizingMask:");
public static final int /*long*/ sel_setAutosaveExpandedItems_ = sel_registerName("setAutosaveExpandedItems:");
public static final int /*long*/ sel_setBackgroundColor_ = sel_registerName("setBackgroundColor:");
public static final int /*long*/ sel_setBackgroundLayoutEnabled_ = sel_registerName("setBackgroundLayoutEnabled:");
public static final int /*long*/ sel_setBezelStyle_ = sel_registerName("setBezelStyle:");
public static final int /*long*/ sel_setBorderType_ = sel_registerName("setBorderType:");
public static final int /*long*/ sel_setBorderWidth_ = sel_registerName("setBorderWidth:");
public static final int /*long*/ sel_setBordered_ = sel_registerName("setBordered:");
public static final int /*long*/ sel_setBoxType_ = sel_registerName("setBoxType:");
public static final int /*long*/ sel_setButtonType_ = sel_registerName("setButtonType:");
public static final int /*long*/ sel_setCacheMode_ = sel_registerName("setCacheMode:");
public static final int /*long*/ sel_setCanChooseDirectories_ = sel_registerName("setCanChooseDirectories:");
public static final int /*long*/ sel_setCanChooseFiles_ = sel_registerName("setCanChooseFiles:");
public static final int /*long*/ sel_setCanCreateDirectories_ = sel_registerName("setCanCreateDirectories:");
public static final int /*long*/ sel_setCancelButtonCell_ = sel_registerName("setCancelButtonCell:");
public static final int /*long*/ sel_setCell_ = sel_registerName("setCell:");
public static final int /*long*/ sel_setCellClass_ = sel_registerName("setCellClass:");
public static final int /*long*/ sel_setClip = sel_registerName("setClip");
public static final int /*long*/ sel_setColor_ = sel_registerName("setColor:");
public static final int /*long*/ sel_setColumnAutoresizingStyle_ = sel_registerName("setColumnAutoresizingStyle:");
public static final int /*long*/ sel_setCompositingOperation_ = sel_registerName("setCompositingOperation:");
public static final int /*long*/ sel_setContainerSize_ = sel_registerName("setContainerSize:");
public static final int /*long*/ sel_setContentView_ = sel_registerName("setContentView:");
public static final int /*long*/ sel_setContentViewMargins_ = sel_registerName("setContentViewMargins:");
public static final int /*long*/ sel_setControlSize_ = sel_registerName("setControlSize:");
public static final int /*long*/ sel_setCookie_ = sel_registerName("setCookie:");
public static final int /*long*/ sel_setCopiesOnScroll_ = sel_registerName("setCopiesOnScroll:");
public static final int /*long*/ sel_setCurrentContext_ = sel_registerName("setCurrentContext:");
public static final int /*long*/ sel_setCurrentOperation_ = sel_registerName("setCurrentOperation:");
public static final int /*long*/ sel_setData_forType_ = sel_registerName("setData:forType:");
public static final int /*long*/ sel_setDataCell_ = sel_registerName("setDataCell:");
public static final int /*long*/ sel_setDataSource_ = sel_registerName("setDataSource:");
public static final int /*long*/ sel_setDatePickerElements_ = sel_registerName("setDatePickerElements:");
public static final int /*long*/ sel_setDatePickerStyle_ = sel_registerName("setDatePickerStyle:");
public static final int /*long*/ sel_setDateValue_ = sel_registerName("setDateValue:");
public static final int /*long*/ sel_setDefaultButtonCell_ = sel_registerName("setDefaultButtonCell:");
public static final int /*long*/ sel_setDefaultFlatness_ = sel_registerName("setDefaultFlatness:");
public static final int /*long*/ sel_setDefaultParagraphStyle_ = sel_registerName("setDefaultParagraphStyle:");
public static final int /*long*/ sel_setDefaultTabInterval_ = sel_registerName("setDefaultTabInterval:");
public static final int /*long*/ sel_setDelegate_ = sel_registerName("setDelegate:");
public static final int /*long*/ sel_setDestination_allowOverwrite_ = sel_registerName("setDestination:allowOverwrite:");
public static final int /*long*/ sel_setDictionary_ = sel_registerName("setDictionary:");
public static final int /*long*/ sel_setDirectory_ = sel_registerName("setDirectory:");
public static final int /*long*/ sel_setDisplayMode_ = sel_registerName("setDisplayMode:");
public static final int /*long*/ sel_setDocumentCursor_ = sel_registerName("setDocumentCursor:");
public static final int /*long*/ sel_setDocumentEdited_ = sel_registerName("setDocumentEdited:");
public static final int /*long*/ sel_setDocumentView_ = sel_registerName("setDocumentView:");
public static final int /*long*/ sel_setDoubleAction_ = sel_registerName("setDoubleAction:");
public static final int /*long*/ sel_setDoubleValue_ = sel_registerName("setDoubleValue:");
public static final int /*long*/ sel_setDownloadDelegate_ = sel_registerName("setDownloadDelegate:");
public static final int /*long*/ sel_setDrawsBackground_ = sel_registerName("setDrawsBackground:");
public static final int /*long*/ sel_setDropItem_dropChildIndex_ = sel_registerName("setDropItem:dropChildIndex:");
public static final int /*long*/ sel_setDropRow_dropOperation_ = sel_registerName("setDropRow:dropOperation:");
public static final int /*long*/ sel_setEditable_ = sel_registerName("setEditable:");
public static final int /*long*/ sel_setEnabled_ = sel_registerName("setEnabled:");
public static final int /*long*/ sel_setEnabled_forSegment_ = sel_registerName("setEnabled:forSegment:");
public static final int /*long*/ sel_setFill = sel_registerName("setFill");
public static final int /*long*/ sel_setFillColor_ = sel_registerName("setFillColor:");
public static final int /*long*/ sel_setFireDate_ = sel_registerName("setFireDate:");
public static final int /*long*/ sel_setFirstLineHeadIndent_ = sel_registerName("setFirstLineHeadIndent:");
public static final int /*long*/ sel_setFloatValue_knobProportion_ = sel_registerName("setFloatValue:knobProportion:");
public static final int /*long*/ sel_setFocusRingType_ = sel_registerName("setFocusRingType:");
public static final int /*long*/ sel_setFont_ = sel_registerName("setFont:");
public static final int /*long*/ sel_setFormatter_ = sel_registerName("setFormatter:");
public static final int /*long*/ sel_setFrame_ = sel_registerName("setFrame:");
public static final int /*long*/ sel_setFrame_display_ = sel_registerName("setFrame:display:");
public static final int /*long*/ sel_setFrameLoadDelegate_ = sel_registerName("setFrameLoadDelegate:");
public static final int /*long*/ sel_setFrameOrigin_ = sel_registerName("setFrameOrigin:");
public static final int /*long*/ sel_setFrameSize_ = sel_registerName("setFrameSize:");
public static final int /*long*/ sel_setHasHorizontalScroller_ = sel_registerName("setHasHorizontalScroller:");
public static final int /*long*/ sel_setHasShadow_ = sel_registerName("setHasShadow:");
public static final int /*long*/ sel_setHasVerticalScroller_ = sel_registerName("setHasVerticalScroller:");
public static final int /*long*/ sel_setHeaderCell_ = sel_registerName("setHeaderCell:");
public static final int /*long*/ sel_setHeaderView_ = sel_registerName("setHeaderView:");
public static final int /*long*/ sel_setHidden_ = sel_registerName("setHidden:");
public static final int /*long*/ sel_setHiddenUntilMouseMoves_ = sel_registerName("setHiddenUntilMouseMoves:");
public static final int /*long*/ sel_setHighlightMode_ = sel_registerName("setHighlightMode:");
public static final int /*long*/ sel_setHighlighted_ = sel_registerName("setHighlighted:");
public static final int /*long*/ sel_setHighlightedTableColumn_ = sel_registerName("setHighlightedTableColumn:");
public static final int /*long*/ sel_setHorizontalScroller_ = sel_registerName("setHorizontalScroller:");
public static final int /*long*/ sel_setHorizontallyResizable_ = sel_registerName("setHorizontallyResizable:");
public static final int /*long*/ sel_setIcon_ = sel_registerName("setIcon:");
public static final int /*long*/ sel_setIdentifier_ = sel_registerName("setIdentifier:");
public static final int /*long*/ sel_setImage_ = sel_registerName("setImage:");
public static final int /*long*/ sel_setImage_forSegment_ = sel_registerName("setImage:forSegment:");
public static final int /*long*/ sel_setImageAlignment_ = sel_registerName("setImageAlignment:");
public static final int /*long*/ sel_setImageInterpolation_ = sel_registerName("setImageInterpolation:");
public static final int /*long*/ sel_setImagePosition_ = sel_registerName("setImagePosition:");
public static final int /*long*/ sel_setImageScaling_ = sel_registerName("setImageScaling:");
public static final int /*long*/ sel_setIncrement_ = sel_registerName("setIncrement:");
public static final int /*long*/ sel_setIndeterminate_ = sel_registerName("setIndeterminate:");
public static final int /*long*/ sel_setIndicatorImage_inTableColumn_ = sel_registerName("setIndicatorImage:inTableColumn:");
public static final int /*long*/ sel_setIntercellSpacing_ = sel_registerName("setIntercellSpacing:");
public static final int /*long*/ sel_setJavaEnabled_ = sel_registerName("setJavaEnabled:");
public static final int /*long*/ sel_setJavaScriptEnabled_ = sel_registerName("setJavaScriptEnabled:");
public static final int /*long*/ sel_setJobDisposition_ = sel_registerName("setJobDisposition:");
public static final int /*long*/ sel_setJobTitle_ = sel_registerName("setJobTitle:");
public static final int /*long*/ sel_setKeyEquivalent_ = sel_registerName("setKeyEquivalent:");
public static final int /*long*/ sel_setKeyEquivalentModifierMask_ = sel_registerName("setKeyEquivalentModifierMask:");
public static final int /*long*/ sel_setLabel_ = sel_registerName("setLabel:");
public static final int /*long*/ sel_setLabel_forSegment_ = sel_registerName("setLabel:forSegment:");
public static final int /*long*/ sel_setLeaf_ = sel_registerName("setLeaf:");
public static final int /*long*/ sel_setLength_ = sel_registerName("setLength:");
public static final int /*long*/ sel_setLevel_ = sel_registerName("setLevel:");
public static final int /*long*/ sel_setLineBreakMode_ = sel_registerName("setLineBreakMode:");
public static final int /*long*/ sel_setLineCapStyle_ = sel_registerName("setLineCapStyle:");
public static final int /*long*/ sel_setLineDash_count_phase_ = sel_registerName("setLineDash:count:phase:");
public static final int /*long*/ sel_setLineFragmentPadding_ = sel_registerName("setLineFragmentPadding:");
public static final int /*long*/ sel_setLineFragmentRect_forGlyphRange_usedRect_ = sel_registerName("setLineFragmentRect:forGlyphRange:usedRect:");
public static final int /*long*/ sel_setLineJoinStyle_ = sel_registerName("setLineJoinStyle:");
public static final int /*long*/ sel_setLineSpacing_ = sel_registerName("setLineSpacing:");
public static final int /*long*/ sel_setLineWidth_ = sel_registerName("setLineWidth:");
public static final int /*long*/ sel_setLinkTextAttributes_ = sel_registerName("setLinkTextAttributes:");
public static final int /*long*/ sel_setMainMenu_ = sel_registerName("setMainMenu:");
public static final int /*long*/ sel_setMarkedText_selectedRange_ = sel_registerName("setMarkedText:selectedRange:");
public static final int /*long*/ sel_setMaxSize_ = sel_registerName("setMaxSize:");
public static final int /*long*/ sel_setMaxValue_ = sel_registerName("setMaxValue:");
public static final int /*long*/ sel_setMaximum_ = sel_registerName("setMaximum:");
public static final int /*long*/ sel_setMaximumFractionDigits_ = sel_registerName("setMaximumFractionDigits:");
public static final int /*long*/ sel_setMaximumIntegerDigits_ = sel_registerName("setMaximumIntegerDigits:");
public static final int /*long*/ sel_setMenu_ = sel_registerName("setMenu:");
public static final int /*long*/ sel_setMenu_forSegment_ = sel_registerName("setMenu:forSegment:");
public static final int /*long*/ sel_setMessageText_ = sel_registerName("setMessageText:");
public static final int /*long*/ sel_setMinSize_ = sel_registerName("setMinSize:");
public static final int /*long*/ sel_setMinValue_ = sel_registerName("setMinValue:");
public static final int /*long*/ sel_setMinWidth_ = sel_registerName("setMinWidth:");
public static final int /*long*/ sel_setMinimum_ = sel_registerName("setMinimum:");
public static final int /*long*/ sel_setMinimumFractionDigits_ = sel_registerName("setMinimumFractionDigits:");
public static final int /*long*/ sel_setMinimumIntegerDigits_ = sel_registerName("setMinimumIntegerDigits:");
public static final int /*long*/ sel_setMiterLimit_ = sel_registerName("setMiterLimit:");
public static final int /*long*/ sel_setNeedsDisplay_ = sel_registerName("setNeedsDisplay:");
public static final int /*long*/ sel_setNeedsDisplayInRect_ = sel_registerName("setNeedsDisplayInRect:");
public static final int /*long*/ sel_setNumberOfVisibleItems_ = sel_registerName("setNumberOfVisibleItems:");
public static final int /*long*/ sel_setNumberStyle_ = sel_registerName("setNumberStyle:");
public static final int /*long*/ sel_setObject_forKey_ = sel_registerName("setObject:forKey:");
public static final int /*long*/ sel_setObjectValue_ = sel_registerName("setObjectValue:");
public static final int /*long*/ sel_setOnMouseEntered_ = sel_registerName("setOnMouseEntered:");
public static final int /*long*/ sel_setOpaque_ = sel_registerName("setOpaque:");
public static final int /*long*/ sel_setOpenGLContext_ = sel_registerName("setOpenGLContext:");
public static final int /*long*/ sel_setOptions_ = sel_registerName("setOptions:");
public static final int /*long*/ sel_setOrientation_ = sel_registerName("setOrientation:");
public static final int /*long*/ sel_setOutlineTableColumn_ = sel_registerName("setOutlineTableColumn:");
public static final int /*long*/ sel_setPaletteLabel_ = sel_registerName("setPaletteLabel:");
public static final int /*long*/ sel_setPanelFont_isMultiple_ = sel_registerName("setPanelFont:isMultiple:");
public static final int /*long*/ sel_setPartialStringValidationEnabled_ = sel_registerName("setPartialStringValidationEnabled:");
public static final int /*long*/ sel_setPatternPhase_ = sel_registerName("setPatternPhase:");
public static final int /*long*/ sel_setPixelFormat_ = sel_registerName("setPixelFormat:");
public static final int /*long*/ sel_setPlaceholderString_ = sel_registerName("setPlaceholderString:");
public static final int /*long*/ sel_setPolicyDelegate_ = sel_registerName("setPolicyDelegate:");
public static final int /*long*/ sel_setPreferences_ = sel_registerName("setPreferences:");
public static final int /*long*/ sel_setPrinter_ = sel_registerName("setPrinter:");
public static final int /*long*/ sel_setPropertyList_forType_ = sel_registerName("setPropertyList:forType:");
public static final int /*long*/ sel_setPullsDown_ = sel_registerName("setPullsDown:");
public static final int /*long*/ sel_setReleasedWhenClosed_ = sel_registerName("setReleasedWhenClosed:");
public static final int /*long*/ sel_setResizingMask_ = sel_registerName("setResizingMask:");
public static final int /*long*/ sel_setResourceLoadDelegate_ = sel_registerName("setResourceLoadDelegate:");
public static final int /*long*/ sel_setRichText_ = sel_registerName("setRichText:");
public static final int /*long*/ sel_setRowHeight_ = sel_registerName("setRowHeight:");
public static final int /*long*/ sel_setScrollable_ = sel_registerName("setScrollable:");
public static final int /*long*/ sel_setSearchButtonCell_ = sel_registerName("setSearchButtonCell:");
public static final int /*long*/ sel_setSegmentCount_ = sel_registerName("setSegmentCount:");
public static final int /*long*/ sel_setSegmentStyle_ = sel_registerName("setSegmentStyle:");
public static final int /*long*/ sel_setSelectable_ = sel_registerName("setSelectable:");
public static final int /*long*/ sel_setSelected_forSegment_ = sel_registerName("setSelected:forSegment:");
public static final int /*long*/ sel_setSelectedRange_ = sel_registerName("setSelectedRange:");
public static final int /*long*/ sel_setSelectedSegment_ = sel_registerName("setSelectedSegment:");
public static final int /*long*/ sel_setServicesMenu_ = sel_registerName("setServicesMenu:");
public static final int /*long*/ sel_setShouldAntialias_ = sel_registerName("setShouldAntialias:");
public static final int /*long*/ sel_setShowsPrintPanel_ = sel_registerName("setShowsPrintPanel:");
public static final int /*long*/ sel_setShowsProgressPanel_ = sel_registerName("setShowsProgressPanel:");
public static final int /*long*/ sel_setShowsToolbarButton_ = sel_registerName("setShowsToolbarButton:");
public static final int /*long*/ sel_setSize_ = sel_registerName("setSize:");
public static final int /*long*/ sel_setState_ = sel_registerName("setState:");
public static final int /*long*/ sel_setString_ = sel_registerName("setString:");
public static final int /*long*/ sel_setString_forType_ = sel_registerName("setString:forType:");
public static final int /*long*/ sel_setStringValue_ = sel_registerName("setStringValue:");
public static final int /*long*/ sel_setStroke = sel_registerName("setStroke");
public static final int /*long*/ sel_setSubmenu_ = sel_registerName("setSubmenu:");
public static final int /*long*/ sel_setSubmenu_forItem_ = sel_registerName("setSubmenu:forItem:");
public static final int /*long*/ sel_setTabStops_ = sel_registerName("setTabStops:");
public static final int /*long*/ sel_setTabViewType_ = sel_registerName("setTabViewType:");
public static final int /*long*/ sel_setTag_forSegment_ = sel_registerName("setTag:forSegment:");
public static final int /*long*/ sel_setTarget_ = sel_registerName("setTarget:");
public static final int /*long*/ sel_setTextColor_ = sel_registerName("setTextColor:");
public static final int /*long*/ sel_setTextStorage_ = sel_registerName("setTextStorage:");
public static final int /*long*/ sel_setTitle_ = sel_registerName("setTitle:");
public static final int /*long*/ sel_setTitleFont_ = sel_registerName("setTitleFont:");
public static final int /*long*/ sel_setTitlePosition_ = sel_registerName("setTitlePosition:");
public static final int /*long*/ sel_setToolTip_ = sel_registerName("setToolTip:");
public static final int /*long*/ sel_setToolTip_forSegment_ = sel_registerName("setToolTip:forSegment:");
public static final int /*long*/ sel_setToolbar_ = sel_registerName("setToolbar:");
public static final int /*long*/ sel_setTrackingMode_ = sel_registerName("setTrackingMode:");
public static final int /*long*/ sel_setTransformStruct_ = sel_registerName("setTransformStruct:");
public static final int /*long*/ sel_setUIDelegate_ = sel_registerName("setUIDelegate:");
public static final int /*long*/ sel_setURL_ = sel_registerName("setURL:");
public static final int /*long*/ sel_setUpPrintOperationDefaultValues = sel_registerName("setUpPrintOperationDefaultValues");
public static final int /*long*/ sel_setUsesAlternatingRowBackgroundColors_ = sel_registerName("setUsesAlternatingRowBackgroundColors:");
public static final int /*long*/ sel_setUsesThreadedAnimation_ = sel_registerName("setUsesThreadedAnimation:");
public static final int /*long*/ sel_setValue_forKey_ = sel_registerName("setValue:forKey:");
public static final int /*long*/ sel_setValueWraps_ = sel_registerName("setValueWraps:");
public static final int /*long*/ sel_setVerticalScroller_ = sel_registerName("setVerticalScroller:");
public static final int /*long*/ sel_setView_ = sel_registerName("setView:");
public static final int /*long*/ sel_setVisible_ = sel_registerName("setVisible:");
public static final int /*long*/ sel_setWidth_ = sel_registerName("setWidth:");
public static final int /*long*/ sel_setWidth_forSegment_ = sel_registerName("setWidth:forSegment:");
public static final int /*long*/ sel_setWidthTracksTextView_ = sel_registerName("setWidthTracksTextView:");
public static final int /*long*/ sel_setWindingRule_ = sel_registerName("setWindingRule:");
public static final int /*long*/ sel_setWorksWhenModal_ = sel_registerName("setWorksWhenModal:");
public static final int /*long*/ sel_setWraps_ = sel_registerName("setWraps:");
public static final int /*long*/ sel_sharedApplication = sel_registerName("sharedApplication");
public static final int /*long*/ sel_sharedColorPanel = sel_registerName("sharedColorPanel");
public static final int /*long*/ sel_sharedFontManager = sel_registerName("sharedFontManager");
public static final int /*long*/ sel_sharedFontPanel = sel_registerName("sharedFontPanel");
public static final int /*long*/ sel_sharedHTTPCookieStorage = sel_registerName("sharedHTTPCookieStorage");
public static final int /*long*/ sel_sharedPrintInfo = sel_registerName("sharedPrintInfo");
public static final int /*long*/ sel_sharedWorkspace = sel_registerName("sharedWorkspace");
public static final int /*long*/ sel_shiftKey = sel_registerName("shiftKey");
public static final int /*long*/ sel_shouldAntialias = sel_registerName("shouldAntialias");
public static final int /*long*/ sel_shouldChangeTextInRange_replacementString_ = sel_registerName("shouldChangeTextInRange:replacementString:");
public static final int /*long*/ sel_shouldDelayWindowOrderingForEvent_ = sel_registerName("shouldDelayWindowOrderingForEvent:");
public static final int /*long*/ sel_size = sel_registerName("size");
public static final int /*long*/ sel_sizeToFit = sel_registerName("sizeToFit");
public static final int /*long*/ sel_sizeValue = sel_registerName("sizeValue");
public static final int /*long*/ sel_skipDescendents = sel_registerName("skipDescendents");
public static final int /*long*/ sel_smallSystemFontSize = sel_registerName("smallSystemFontSize");
public static final int /*long*/ sel_sortIndicatorRectForBounds_ = sel_registerName("sortIndicatorRectForBounds:");
public static final int /*long*/ sel_standardPreferences = sel_registerName("standardPreferences");
public static final int /*long*/ sel_standardWindowButton_ = sel_registerName("standardWindowButton:");
public static final int /*long*/ sel_startAnimation_ = sel_registerName("startAnimation:");
public static final int /*long*/ sel_state = sel_registerName("state");
public static final int /*long*/ sel_statusItemWithLength_ = sel_registerName("statusItemWithLength:");
public static final int /*long*/ sel_stop_ = sel_registerName("stop:");
public static final int /*long*/ sel_stopAnimation_ = sel_registerName("stopAnimation:");
public static final int /*long*/ sel_stopLoading_ = sel_registerName("stopLoading:");
public static final int /*long*/ sel_string = sel_registerName("string");
public static final int /*long*/ sel_stringByAddingPercentEscapesUsingEncoding_ = sel_registerName("stringByAddingPercentEscapesUsingEncoding:");
public static final int /*long*/ sel_stringByAppendingPathComponent_ = sel_registerName("stringByAppendingPathComponent:");
public static final int /*long*/ sel_stringByAppendingString_ = sel_registerName("stringByAppendingString:");
public static final int /*long*/ sel_stringByDeletingLastPathComponent = sel_registerName("stringByDeletingLastPathComponent");
public static final int /*long*/ sel_stringByDeletingPathExtension = sel_registerName("stringByDeletingPathExtension");
public static final int /*long*/ sel_stringByEvaluatingJavaScriptFromString_ = sel_registerName("stringByEvaluatingJavaScriptFromString:");
public static final int /*long*/ sel_stringByReplacingOccurrencesOfString_withString_ = sel_registerName("stringByReplacingOccurrencesOfString:withString:");
public static final int /*long*/ sel_stringForObjectValue_ = sel_registerName("stringForObjectValue:");
public static final int /*long*/ sel_stringForType_ = sel_registerName("stringForType:");
public static final int /*long*/ sel_stringValue = sel_registerName("stringValue");
public static final int /*long*/ sel_stringWithCharacters_length_ = sel_registerName("stringWithCharacters:length:");
public static final int /*long*/ sel_stringWithFormat_ = sel_registerName("stringWithFormat:");
public static final int /*long*/ sel_stringWithUTF8String_ = sel_registerName("stringWithUTF8String:");
public static final int /*long*/ sel_stroke = sel_registerName("stroke");
public static final int /*long*/ sel_strokeRect_ = sel_registerName("strokeRect:");
public static final int /*long*/ sel_styleMask = sel_registerName("styleMask");
public static final int /*long*/ sel_submenu = sel_registerName("submenu");
public static final int /*long*/ sel_subviews = sel_registerName("subviews");
public static final int /*long*/ sel_superclass = sel_registerName("superclass");
public static final int /*long*/ sel_superview = sel_registerName("superview");
public static final int /*long*/ sel_systemFontOfSize_ = sel_registerName("systemFontOfSize:");
public static final int /*long*/ sel_systemFontSize = sel_registerName("systemFontSize");
public static final int /*long*/ sel_systemFontSizeForControlSize_ = sel_registerName("systemFontSizeForControlSize:");
public static final int /*long*/ sel_systemStatusBar = sel_registerName("systemStatusBar");
public static final int /*long*/ sel_systemVersion = sel_registerName("systemVersion");
public static final int /*long*/ sel_tabStopType = sel_registerName("tabStopType");
public static final int /*long*/ sel_tabStops = sel_registerName("tabStops");
public static final int /*long*/ sel_tabView_didSelectTabViewItem_ = sel_registerName("tabView:didSelectTabViewItem:");
public static final int /*long*/ sel_tabView_willSelectTabViewItem_ = sel_registerName("tabView:willSelectTabViewItem:");
public static final int /*long*/ sel_tabViewItemAtPoint_ = sel_registerName("tabViewItemAtPoint:");
public static final int /*long*/ sel_tableColumns = sel_registerName("tableColumns");
public static final int /*long*/ sel_tableView_acceptDrop_row_dropOperation_ = sel_registerName("tableView:acceptDrop:row:dropOperation:");
public static final int /*long*/ sel_tableView_didClickTableColumn_ = sel_registerName("tableView:didClickTableColumn:");
public static final int /*long*/ sel_tableView_objectValueForTableColumn_row_ = sel_registerName("tableView:objectValueForTableColumn:row:");
public static final int /*long*/ sel_tableView_setObjectValue_forTableColumn_row_ = sel_registerName("tableView:setObjectValue:forTableColumn:row:");
public static final int /*long*/ sel_tableView_shouldEditTableColumn_row_ = sel_registerName("tableView:shouldEditTableColumn:row:");
public static final int /*long*/ sel_tableView_validateDrop_proposedRow_proposedDropOperation_ = sel_registerName("tableView:validateDrop:proposedRow:proposedDropOperation:");
public static final int /*long*/ sel_tableView_willDisplayCell_forTableColumn_row_ = sel_registerName("tableView:willDisplayCell:forTableColumn:row:");
public static final int /*long*/ sel_tableView_writeRowsWithIndexes_toPasteboard_ = sel_registerName("tableView:writeRowsWithIndexes:toPasteboard:");
public static final int /*long*/ sel_tableViewColumnDidMove_ = sel_registerName("tableViewColumnDidMove:");
public static final int /*long*/ sel_tableViewColumnDidResize_ = sel_registerName("tableViewColumnDidResize:");
public static final int /*long*/ sel_tableViewSelectionDidChange_ = sel_registerName("tableViewSelectionDidChange:");
public static final int /*long*/ sel_target = sel_registerName("target");
public static final int /*long*/ sel_terminate_ = sel_registerName("terminate:");
public static final int /*long*/ sel_textBackgroundColor = sel_registerName("textBackgroundColor");
public static final int /*long*/ sel_textColor = sel_registerName("textColor");
public static final int /*long*/ sel_textContainer = sel_registerName("textContainer");
public static final int /*long*/ sel_textDidChange_ = sel_registerName("textDidChange:");
public static final int /*long*/ sel_textDidEndEditing_ = sel_registerName("textDidEndEditing:");
public static final int /*long*/ sel_textStorage = sel_registerName("textStorage");
public static final int /*long*/ sel_textView_clickedOnLink_atIndex_ = sel_registerName("textView:clickedOnLink:atIndex:");
public static final int /*long*/ sel_textView_willChangeSelectionFromCharacterRange_toCharacterRange_ = sel_registerName("textView:willChangeSelectionFromCharacterRange:toCharacterRange:");
public static final int /*long*/ sel_textViewDidChangeSelection_ = sel_registerName("textViewDidChangeSelection:");
public static final int /*long*/ sel_threadDictionary = sel_registerName("threadDictionary");
public static final int /*long*/ sel_tile = sel_registerName("tile");
public static final int /*long*/ sel_timeZone = sel_registerName("timeZone");
public static final int /*long*/ sel_timestamp = sel_registerName("timestamp");
public static final int /*long*/ sel_title = sel_registerName("title");
public static final int /*long*/ sel_titleCell = sel_registerName("titleCell");
public static final int /*long*/ sel_titleFont = sel_registerName("titleFont");
public static final int /*long*/ sel_titleOfSelectedItem = sel_registerName("titleOfSelectedItem");
public static final int /*long*/ sel_titleRectForBounds_ = sel_registerName("titleRectForBounds:");
public static final int /*long*/ sel_toggleToolbarShown_ = sel_registerName("toggleToolbarShown:");
public static final int /*long*/ sel_toolbar = sel_registerName("toolbar");
public static final int /*long*/ sel_toolbar_itemForItemIdentifier_willBeInsertedIntoToolbar_ = sel_registerName("toolbar:itemForItemIdentifier:willBeInsertedIntoToolbar:");
public static final int /*long*/ sel_toolbarAllowedItemIdentifiers_ = sel_registerName("toolbarAllowedItemIdentifiers:");
public static final int /*long*/ sel_toolbarDefaultItemIdentifiers_ = sel_registerName("toolbarDefaultItemIdentifiers:");
public static final int /*long*/ sel_toolbarDidRemoveItem_ = sel_registerName("toolbarDidRemoveItem:");
public static final int /*long*/ sel_toolbarSelectableItemIdentifiers_ = sel_registerName("toolbarSelectableItemIdentifiers:");
public static final int /*long*/ sel_toolbarWillAddItem_ = sel_registerName("toolbarWillAddItem:");
public static final int /*long*/ sel_trackingAreas = sel_registerName("trackingAreas");
public static final int /*long*/ sel_traitsOfFont_ = sel_registerName("traitsOfFont:");
public static final int /*long*/ sel_transform = sel_registerName("transform");
public static final int /*long*/ sel_transformPoint_ = sel_registerName("transformPoint:");
public static final int /*long*/ sel_transformSize_ = sel_registerName("transformSize:");
public static final int /*long*/ sel_transformStruct = sel_registerName("transformStruct");
public static final int /*long*/ sel_transformUsingAffineTransform_ = sel_registerName("transformUsingAffineTransform:");
public static final int /*long*/ sel_translateXBy_yBy_ = sel_registerName("translateXBy:yBy:");
public static final int /*long*/ sel_type = sel_registerName("type");
public static final int /*long*/ sel_types = sel_registerName("types");
public static final int /*long*/ sel_typesetter = sel_registerName("typesetter");
public static final int /*long*/ sel_unarchiveObjectWithData_ = sel_registerName("unarchiveObjectWithData:");
public static final int /*long*/ sel_undefined = sel_registerName("undefined");
public static final int /*long*/ sel_unhideAllApplications_ = sel_registerName("unhideAllApplications:");
public static final int /*long*/ sel_unlockFocus = sel_registerName("unlockFocus");
public static final int /*long*/ sel_unmarkText = sel_registerName("unmarkText");
public static final int /*long*/ sel_unregisterDraggedTypes = sel_registerName("unregisterDraggedTypes");
public static final int /*long*/ sel_updateTrackingAreas = sel_registerName("updateTrackingAreas");
public static final int /*long*/ sel_use = sel_registerName("use");
public static final int /*long*/ sel_useCredential_forAuthenticationChallenge_ = sel_registerName("useCredential:forAuthenticationChallenge:");
public static final int /*long*/ sel_usedRectForTextContainer_ = sel_registerName("usedRectForTextContainer:");
public static final int /*long*/ sel_user = sel_registerName("user");
public static final int /*long*/ sel_userInfo = sel_registerName("userInfo");
public static final int /*long*/ sel_usesAlternatingRowBackgroundColors = sel_registerName("usesAlternatingRowBackgroundColors");
public static final int /*long*/ sel_validAttributesForMarkedText = sel_registerName("validAttributesForMarkedText");
public static final int /*long*/ sel_validateVisibleColumns = sel_registerName("validateVisibleColumns");
public static final int /*long*/ sel_value = sel_registerName("value");
public static final int /*long*/ sel_valueForKey_ = sel_registerName("valueForKey:");
public static final int /*long*/ sel_valueWithPoint_ = sel_registerName("valueWithPoint:");
public static final int /*long*/ sel_valueWithRange_ = sel_registerName("valueWithRange:");
public static final int /*long*/ sel_valueWithRect_ = sel_registerName("valueWithRect:");
public static final int /*long*/ sel_valueWithSize_ = sel_registerName("valueWithSize:");
public static final int /*long*/ sel_view_stringForToolTip_point_userData_ = sel_registerName("view:stringForToolTip:point:userData:");
public static final int /*long*/ sel_viewDidMoveToWindow = sel_registerName("viewDidMoveToWindow");
public static final int /*long*/ sel_visibleFrame = sel_registerName("visibleFrame");
public static final int /*long*/ sel_visibleRect = sel_registerName("visibleRect");
public static final int /*long*/ sel_wantsPeriodicDraggingUpdates = sel_registerName("wantsPeriodicDraggingUpdates");
public static final int /*long*/ sel_wantsToHandleMouseEvents = sel_registerName("wantsToHandleMouseEvents");
public static final int /*long*/ sel_webFrame = sel_registerName("webFrame");
public static final int /*long*/ sel_webScriptValueAtIndex_ = sel_registerName("webScriptValueAtIndex:");
public static final int /*long*/ sel_webView_contextMenuItemsForElement_defaultMenuItems_ = sel_registerName("webView:contextMenuItemsForElement:defaultMenuItems:");
public static final int /*long*/ sel_webView_createWebViewWithRequest_ = sel_registerName("webView:createWebViewWithRequest:");
public static final int /*long*/ sel_webView_decidePolicyForMIMEType_request_frame_decisionListener_ = sel_registerName("webView:decidePolicyForMIMEType:request:frame:decisionListener:");
public static final int /*long*/ sel_webView_decidePolicyForNavigationAction_request_frame_decisionListener_ = sel_registerName("webView:decidePolicyForNavigationAction:request:frame:decisionListener:");
public static final int /*long*/ sel_webView_decidePolicyForNewWindowAction_request_newFrameName_decisionListener_ = sel_registerName("webView:decidePolicyForNewWindowAction:request:newFrameName:decisionListener:");
public static final int /*long*/ sel_webView_didChangeLocationWithinPageForFrame_ = sel_registerName("webView:didChangeLocationWithinPageForFrame:");
public static final int /*long*/ sel_webView_didCommitLoadForFrame_ = sel_registerName("webView:didCommitLoadForFrame:");
public static final int /*long*/ sel_webView_didFailProvisionalLoadWithError_forFrame_ = sel_registerName("webView:didFailProvisionalLoadWithError:forFrame:");
public static final int /*long*/ sel_webView_didFinishLoadForFrame_ = sel_registerName("webView:didFinishLoadForFrame:");
public static final int /*long*/ sel_webView_didReceiveTitle_forFrame_ = sel_registerName("webView:didReceiveTitle:forFrame:");
public static final int /*long*/ sel_webView_didStartProvisionalLoadForFrame_ = sel_registerName("webView:didStartProvisionalLoadForFrame:");
public static final int /*long*/ sel_webView_identifierForInitialRequest_fromDataSource_ = sel_registerName("webView:identifierForInitialRequest:fromDataSource:");
public static final int /*long*/ sel_webView_mouseDidMoveOverElement_modifierFlags_ = sel_registerName("webView:mouseDidMoveOverElement:modifierFlags:");
public static final int /*long*/ sel_webView_printFrameView_ = sel_registerName("webView:printFrameView:");
public static final int /*long*/ sel_webView_resource_didFailLoadingWithError_fromDataSource_ = sel_registerName("webView:resource:didFailLoadingWithError:fromDataSource:");
public static final int /*long*/ sel_webView_resource_didFinishLoadingFromDataSource_ = sel_registerName("webView:resource:didFinishLoadingFromDataSource:");
public static final int /*long*/ sel_webView_resource_didReceiveAuthenticationChallenge_fromDataSource_ = sel_registerName("webView:resource:didReceiveAuthenticationChallenge:fromDataSource:");
public static final int /*long*/ sel_webView_resource_willSendRequest_redirectResponse_fromDataSource_ = sel_registerName("webView:resource:willSendRequest:redirectResponse:fromDataSource:");
public static final int /*long*/ sel_webView_runJavaScriptAlertPanelWithMessage_ = sel_registerName("webView:runJavaScriptAlertPanelWithMessage:");
public static final int /*long*/ sel_webView_runJavaScriptConfirmPanelWithMessage_ = sel_registerName("webView:runJavaScriptConfirmPanelWithMessage:");
public static final int /*long*/ sel_webView_runOpenPanelForFileButtonWithResultListener_ = sel_registerName("webView:runOpenPanelForFileButtonWithResultListener:");
public static final int /*long*/ sel_webView_setFrame_ = sel_registerName("webView:setFrame:");
public static final int /*long*/ sel_webView_setResizable_ = sel_registerName("webView:setResizable:");
public static final int /*long*/ sel_webView_setStatusBarVisible_ = sel_registerName("webView:setStatusBarVisible:");
public static final int /*long*/ sel_webView_setStatusText_ = sel_registerName("webView:setStatusText:");
public static final int /*long*/ sel_webView_setToolbarsVisible_ = sel_registerName("webView:setToolbarsVisible:");
public static final int /*long*/ sel_webView_unableToImplementPolicyWithError_frame_ = sel_registerName("webView:unableToImplementPolicyWithError:frame:");
public static final int /*long*/ sel_webView_windowScriptObjectAvailable_ = sel_registerName("webView:windowScriptObjectAvailable:");
public static final int /*long*/ sel_webViewClose_ = sel_registerName("webViewClose:");
public static final int /*long*/ sel_webViewFocus_ = sel_registerName("webViewFocus:");
public static final int /*long*/ sel_webViewShow_ = sel_registerName("webViewShow:");
public static final int /*long*/ sel_webViewUnfocus_ = sel_registerName("webViewUnfocus:");
public static final int /*long*/ sel_weightOfFont_ = sel_registerName("weightOfFont:");
public static final int /*long*/ sel_wheelDelta = sel_registerName("wheelDelta");
public static final int /*long*/ sel_width = sel_registerName("width");
public static final int /*long*/ sel_window = sel_registerName("window");
public static final int /*long*/ sel_windowBackgroundColor = sel_registerName("windowBackgroundColor");
public static final int /*long*/ sel_windowDidBecomeKey_ = sel_registerName("windowDidBecomeKey:");
public static final int /*long*/ sel_windowDidMove_ = sel_registerName("windowDidMove:");
public static final int /*long*/ sel_windowDidResignKey_ = sel_registerName("windowDidResignKey:");
public static final int /*long*/ sel_windowDidResize_ = sel_registerName("windowDidResize:");
public static final int /*long*/ sel_windowFrameColor = sel_registerName("windowFrameColor");
public static final int /*long*/ sel_windowFrameTextColor = sel_registerName("windowFrameTextColor");
public static final int /*long*/ sel_windowNumber = sel_registerName("windowNumber");
public static final int /*long*/ sel_windowShouldClose_ = sel_registerName("windowShouldClose:");
public static final int /*long*/ sel_windowWillClose_ = sel_registerName("windowWillClose:");
public static final int /*long*/ sel_windows = sel_registerName("windows");
public static final int /*long*/ sel_worksWhenModal = sel_registerName("worksWhenModal");
public static final int /*long*/ sel_wraps = sel_registerName("wraps");
public static final int /*long*/ sel_writeToPasteboard_ = sel_registerName("writeToPasteboard:");
public static final int /*long*/ sel_yearOfCommonEra = sel_registerName("yearOfCommonEra");
public static final int /*long*/ sel_zoom_ = sel_registerName("zoom:");

/** Constants */
public static final int NSAlertFirstButtonReturn = 1000;
public static final int NSAlertSecondButtonReturn = 1001;
public static final int NSAlertThirdButtonReturn = 1002;
public static final int NSAlphaFirstBitmapFormat = 1;
public static final int NSAlphaNonpremultipliedBitmapFormat = 2;
public static final int NSAlternateKeyMask = 524288;
public static final int NSApplicationDefined = 15;
public static final int NSAtTop = 2;
public static final int NSBackingStoreBuffered = 2;
public static final int NSBackspaceCharacter = 8;
public static final int NSBevelLineJoinStyle = 2;
public static final int NSBezelBorder = 2;
public static final int NSBoldFontMask = 2;
public static final int NSBorderlessWindowMask = 0;
public static final int NSBottomTabsBezelBorder = 2;
public static final int NSBoxCustom = 4;
public static final int NSBoxSeparator = 2;
public static final int NSButtLineCapStyle = 0;
public static final int NSCancelButton = 0;
public static final int NSCarriageReturnCharacter = 13;
public static final int NSCenterTextAlignment = 2;
public static final int NSClockAndCalendarDatePickerStyle = 1;
public static final int NSClosableWindowMask = 2;
public static final int NSClosePathBezierPathElement = 3;
public static final int NSCommandKeyMask = 1048576;
public static final int NSCompositeClear = 0;
public static final int NSCompositeCopy = 1;
public static final int NSCompositeSourceOver = 2;
public static final int NSCompositeXOR = 10;
public static final int NSControlKeyMask = 262144;
public static final int NSCriticalAlertStyle = 2;
public static final int NSCurveToBezierPathElement = 2;
public static final int NSDeleteCharacter = 127;
public static final int NSDeviceIndependentModifierFlagsMask = -65536;
public static final int NSDragOperationCopy = 1;
public static final int NSDragOperationDelete = 32;
public static final int NSDragOperationEvery = -1;
public static final int NSDragOperationLink = 2;
public static final int NSDragOperationMove = 16;
public static final int NSDragOperationNone = 0;
public static final int NSEnterCharacter = 3;
public static final int NSEvenOddWindingRule = 1;
public static final int NSFileHandlingPanelOKButton = 1;
public static final int NSFlagsChanged = 12;
public static final int NSFocusRingTypeNone = 1;
public static final int NSHelpFunctionKey = 63302;
public static final int NSHelpKeyMask = 4194304;
public static final int NSHourMinuteDatePickerElementFlag = 12;
public static final int NSHourMinuteSecondDatePickerElementFlag = 14;
public static final int NSImageAbove = 5;
public static final int NSImageAlignCenter = 0;
public static final int NSImageAlignLeft = 4;
public static final int NSImageAlignRight = 8;
public static final int NSImageCacheNever = 3;
public static final int NSImageInterpolationDefault = 0;
public static final int NSImageInterpolationHigh = 3;
public static final int NSImageInterpolationLow = 2;
public static final int NSImageInterpolationNone = 1;
public static final int NSImageLeft = 2;
public static final int NSImageOnly = 1;
public static final int NSImageOverlaps = 6;
public static final int NSInformationalAlertStyle = 1;
public static final int NSItalicFontMask = 1;
public static final int NSJustifiedTextAlignment = 3;
public static final int NSKeyDown = 10;
public static final int NSKeyUp = 11;
public static final int NSLandscapeOrientation = 1;
public static final int NSLeftMouseDown = 1;
public static final int NSLeftMouseDownMask = 2;
public static final int NSLeftMouseDragged = 6;
public static final int NSLeftMouseDraggedMask = 64;
public static final int NSLeftMouseUp = 2;
public static final int NSLeftMouseUpMask = 4;
public static final int NSLeftTabStopType = 0;
public static final int NSLeftTextAlignment = 0;
public static final int NSLineBreakByClipping = 2;
public static final int NSLineBreakByWordWrapping = 0;
public static final int NSLineToBezierPathElement = 1;
public static final int NSMiniaturizableWindowMask = 4;
public static final int NSMiterLineJoinStyle = 0;
public static final int NSMixedState = -1;
public static final int NSMomentaryLightButton = 0;
public static final int NSMouseEntered = 8;
public static final int NSMouseExited = 9;
public static final int NSMouseMoved = 5;
public static final int NSMoveToBezierPathElement = 0;
public static final int NSNewlineCharacter = 10;
public static final int NSNoBorder = 0;
public static final int NSNoImage = 0;
public static final int NSNoTitle = 0;
public static final int NSNonZeroWindingRule = 0;
public static final int NSOffState = 0;
public static final int NSOnState = 1;
public static final int NSOpenGLPFAAccumSize = 14;
public static final int NSOpenGLPFAAlphaSize = 11;
public static final int NSOpenGLPFAColorSize = 8;
public static final int NSOpenGLPFADepthSize = 12;
public static final int NSOpenGLPFADoubleBuffer = 5;
public static final int NSOpenGLPFASampleBuffers = 55;
public static final int NSOpenGLPFASamples = 56;
public static final int NSOpenGLPFAStencilSize = 13;
public static final int NSOpenGLPFAStereo = 6;
public static final int NSOtherMouseDown = 25;
public static final int NSOtherMouseDragged = 27;
public static final int NSOtherMouseUp = 26;
public static final int NSOutlineViewDropOnItemIndex = -1;
public static final int NSPageDownFunctionKey = 63277;
public static final int NSPageUpFunctionKey = 63276;
public static final int NSPortraitOrientation = 0;
public static final int NSPrintPanelShowsPageSetupAccessory = 256;
public static final int NSProgressIndicatorPreferredThickness = 14;
public static final int NSPushOnPushOffButton = 1;
public static final int NSRadioButton = 4;
public static final int NSRegularControlSize = 0;
public static final int NSResizableWindowMask = 8;
public static final int NSRightMouseDown = 3;
public static final int NSRightMouseDragged = 7;
public static final int NSRightMouseUp = 4;
public static final int NSRightTextAlignment = 1;
public static final int NSRoundLineCapStyle = 1;
public static final int NSRoundLineJoinStyle = 1;
public static final int NSRoundedBezelStyle = 1;
public static final int NSScaleNone = 2;
public static final int NSScrollWheel = 22;
public static final int NSScrollerDecrementLine = 4;
public static final int NSScrollerDecrementPage = 1;
public static final int NSScrollerIncrementLine = 5;
public static final int NSScrollerIncrementPage = 3;
public static final int NSScrollerKnob = 2;
public static final int NSShadowlessSquareBezelStyle = 6;
public static final int NSShiftKeyMask = 131072;
public static final int NSSmallControlSize = 1;
public static final int NSSquareLineCapStyle = 2;
public static final int NSStatusWindowLevel = 25;
public static final int NSSwitchButton = 3;
public static final int NSSystemDefined = 14;
public static final int NSTabCharacter = 9;
public static final int NSTableColumnNoResizing = 0;
public static final int NSTableColumnUserResizingMask = 2;
public static final int NSTableViewDropAbove = 1;
public static final int NSTableViewDropOn = 0;
public static final int NSTableViewNoColumnAutoresizing = 0;
public static final int NSTextFieldAndStepperDatePickerStyle = 0;
public static final int NSTitledWindowMask = 1;
public static final int NSUnderlineStyleDouble = 9;
public static final int NSUnderlineStyleNone = 0;
public static final int NSUnderlineStyleSingle = 1;
public static final int NSUnderlineStyleThick = 2;
public static final int NSViewHeightSizable = 16;
public static final int NSViewWidthSizable = 2;
public static final int NSWarningAlertStyle = 0;
public static final int NSWindowAbove = 1;
public static final int NSWindowBelow = -1;
public static final int NSYearMonthDatePickerElementFlag = 192;
public static final int NSYearMonthDayDatePickerElementFlag = 224;
public static final int kCFRunLoopBeforeWaiting = 32;
public static final int kCFStringEncodingUTF8 = 134217984;
public static final int kCGEventFilterMaskPermitLocalKeyboardEvents = 2;
public static final int kCGEventFilterMaskPermitLocalMouseEvents = 1;
public static final int kCGEventFilterMaskPermitSystemDefinedEvents = 4;
public static final int kCGEventSuppressionStateRemoteMouseDrag = 1;
public static final int kCGEventSuppressionStateSuppressionInterval = 0;
public static final int kCGImageAlphaFirst = 4;
public static final int kCGImageAlphaNoneSkipFirst = 6;
public static final int kCGImageAlphaOnly = 7;
public static final int kCGKeyboardEventKeyboardType = 10;
public static final int kCGLineCapButt = 0;
public static final int kCGLineCapRound = 1;
public static final int kCGLineCapSquare = 2;
public static final int kCGLineJoinBevel = 2;
public static final int kCGLineJoinMiter = 0;
public static final int kCGLineJoinRound = 1;
public static final int kCGPathElementAddCurveToPoint = 3;
public static final int kCGPathElementAddLineToPoint = 1;
public static final int kCGPathElementAddQuadCurveToPoint = 2;
public static final int kCGPathElementCloseSubpath = 4;
public static final int kCGPathElementMoveToPoint = 0;
public static final int kCGPathStroke = 2;
public static final int kCGSessionEventTap = 1;
public static final int NSAllApplicationsDirectory = 100;
public static final int NSAllDomainsMask = 65535;
public static final int NSNotFound = 2147483647;
public static final int NSOrderedSame = 0;
public static final int NSURLCredentialPersistenceForSession = 1;
public static final int NSURLErrorBadURL = -1000;
public static final int NSUTF8StringEncoding = 4;

/** Globals */
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityButtonRole();
public static final NSString NSAccessibilityButtonRole = new NSString(NSAccessibilityButtonRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityCheckBoxRole();
public static final NSString NSAccessibilityCheckBoxRole = new NSString(NSAccessibilityCheckBoxRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityChildrenAttribute();
public static final NSString NSAccessibilityChildrenAttribute = new NSString(NSAccessibilityChildrenAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityColumnRole();
public static final NSString NSAccessibilityColumnRole = new NSString(NSAccessibilityColumnRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityComboBoxRole();
public static final NSString NSAccessibilityComboBoxRole = new NSString(NSAccessibilityComboBoxRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityConfirmAction();
public static final NSString NSAccessibilityConfirmAction = new NSString(NSAccessibilityConfirmAction());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityContentsAttribute();
public static final NSString NSAccessibilityContentsAttribute = new NSString(NSAccessibilityContentsAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityDescriptionAttribute();
public static final NSString NSAccessibilityDescriptionAttribute = new NSString(NSAccessibilityDescriptionAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityDialogSubrole();
public static final NSString NSAccessibilityDialogSubrole = new NSString(NSAccessibilityDialogSubrole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityEnabledAttribute();
public static final NSString NSAccessibilityEnabledAttribute = new NSString(NSAccessibilityEnabledAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityExpandedAttribute();
public static final NSString NSAccessibilityExpandedAttribute = new NSString(NSAccessibilityExpandedAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityFloatingWindowSubrole();
public static final NSString NSAccessibilityFloatingWindowSubrole = new NSString(NSAccessibilityFloatingWindowSubrole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityFocusedAttribute();
public static final NSString NSAccessibilityFocusedAttribute = new NSString(NSAccessibilityFocusedAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityFocusedUIElementChangedNotification();
public static final NSString NSAccessibilityFocusedUIElementChangedNotification = new NSString(NSAccessibilityFocusedUIElementChangedNotification());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityGridRole();
public static final NSString NSAccessibilityGridRole = new NSString(NSAccessibilityGridRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityGroupRole();
public static final NSString NSAccessibilityGroupRole = new NSString(NSAccessibilityGroupRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityHelpAttribute();
public static final NSString NSAccessibilityHelpAttribute = new NSString(NSAccessibilityHelpAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityHelpTagRole();
public static final NSString NSAccessibilityHelpTagRole = new NSString(NSAccessibilityHelpTagRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityHorizontalOrientationValue();
public static final NSString NSAccessibilityHorizontalOrientationValue = new NSString(NSAccessibilityHorizontalOrientationValue());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityHorizontalScrollBarAttribute();
public static final NSString NSAccessibilityHorizontalScrollBarAttribute = new NSString(NSAccessibilityHorizontalScrollBarAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityImageRole();
public static final NSString NSAccessibilityImageRole = new NSString(NSAccessibilityImageRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityIncrementorRole();
public static final NSString NSAccessibilityIncrementorRole = new NSString(NSAccessibilityIncrementorRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityInsertionPointLineNumberAttribute();
public static final NSString NSAccessibilityInsertionPointLineNumberAttribute = new NSString(NSAccessibilityInsertionPointLineNumberAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityLabelValueAttribute();
public static final NSString NSAccessibilityLabelValueAttribute = new NSString(NSAccessibilityLabelValueAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityLineForIndexParameterizedAttribute();
public static final NSString NSAccessibilityLineForIndexParameterizedAttribute = new NSString(NSAccessibilityLineForIndexParameterizedAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityLinkRole();
public static final NSString NSAccessibilityLinkRole = new NSString(NSAccessibilityLinkRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityLinkTextAttribute();
public static final NSString NSAccessibilityLinkTextAttribute = new NSString(NSAccessibilityLinkTextAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityListRole();
public static final NSString NSAccessibilityListRole = new NSString(NSAccessibilityListRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityMaxValueAttribute();
public static final NSString NSAccessibilityMaxValueAttribute = new NSString(NSAccessibilityMaxValueAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityMenuBarRole();
public static final NSString NSAccessibilityMenuBarRole = new NSString(NSAccessibilityMenuBarRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityMenuButtonRole();
public static final NSString NSAccessibilityMenuButtonRole = new NSString(NSAccessibilityMenuButtonRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityMenuItemRole();
public static final NSString NSAccessibilityMenuItemRole = new NSString(NSAccessibilityMenuItemRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityMenuRole();
public static final NSString NSAccessibilityMenuRole = new NSString(NSAccessibilityMenuRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityMinValueAttribute();
public static final NSString NSAccessibilityMinValueAttribute = new NSString(NSAccessibilityMinValueAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityNextContentsAttribute();
public static final NSString NSAccessibilityNextContentsAttribute = new NSString(NSAccessibilityNextContentsAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityNumberOfCharactersAttribute();
public static final NSString NSAccessibilityNumberOfCharactersAttribute = new NSString(NSAccessibilityNumberOfCharactersAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityOrientationAttribute();
public static final NSString NSAccessibilityOrientationAttribute = new NSString(NSAccessibilityOrientationAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityOutlineRole();
public static final NSString NSAccessibilityOutlineRole = new NSString(NSAccessibilityOutlineRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityOutlineRowSubrole();
public static final NSString NSAccessibilityOutlineRowSubrole = new NSString(NSAccessibilityOutlineRowSubrole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityParentAttribute();
public static final NSString NSAccessibilityParentAttribute = new NSString(NSAccessibilityParentAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityPopUpButtonRole();
public static final NSString NSAccessibilityPopUpButtonRole = new NSString(NSAccessibilityPopUpButtonRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityPositionAttribute();
public static final NSString NSAccessibilityPositionAttribute = new NSString(NSAccessibilityPositionAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityPressAction();
public static final NSString NSAccessibilityPressAction = new NSString(NSAccessibilityPressAction());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityPreviousContentsAttribute();
public static final NSString NSAccessibilityPreviousContentsAttribute = new NSString(NSAccessibilityPreviousContentsAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityProgressIndicatorRole();
public static final NSString NSAccessibilityProgressIndicatorRole = new NSString(NSAccessibilityProgressIndicatorRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityRTFForRangeParameterizedAttribute();
public static final NSString NSAccessibilityRTFForRangeParameterizedAttribute = new NSString(NSAccessibilityRTFForRangeParameterizedAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityRadioButtonRole();
public static final NSString NSAccessibilityRadioButtonRole = new NSString(NSAccessibilityRadioButtonRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityRadioGroupRole();
public static final NSString NSAccessibilityRadioGroupRole = new NSString(NSAccessibilityRadioGroupRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityRangeForIndexParameterizedAttribute();
public static final NSString NSAccessibilityRangeForIndexParameterizedAttribute = new NSString(NSAccessibilityRangeForIndexParameterizedAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityRangeForLineParameterizedAttribute();
public static final NSString NSAccessibilityRangeForLineParameterizedAttribute = new NSString(NSAccessibilityRangeForLineParameterizedAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityRangeForPositionParameterizedAttribute();
public static final NSString NSAccessibilityRangeForPositionParameterizedAttribute = new NSString(NSAccessibilityRangeForPositionParameterizedAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityRoleAttribute();
public static final NSString NSAccessibilityRoleAttribute = new NSString(NSAccessibilityRoleAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityRoleDescriptionAttribute();
public static final NSString NSAccessibilityRoleDescriptionAttribute = new NSString(NSAccessibilityRoleDescriptionAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityRowRole();
public static final NSString NSAccessibilityRowRole = new NSString(NSAccessibilityRowRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityScrollAreaRole();
public static final NSString NSAccessibilityScrollAreaRole = new NSString(NSAccessibilityScrollAreaRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityScrollBarRole();
public static final NSString NSAccessibilityScrollBarRole = new NSString(NSAccessibilityScrollBarRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilitySelectedAttribute();
public static final NSString NSAccessibilitySelectedAttribute = new NSString(NSAccessibilitySelectedAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilitySelectedChildrenAttribute();
public static final NSString NSAccessibilitySelectedChildrenAttribute = new NSString(NSAccessibilitySelectedChildrenAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilitySelectedChildrenChangedNotification();
public static final NSString NSAccessibilitySelectedChildrenChangedNotification = new NSString(NSAccessibilitySelectedChildrenChangedNotification());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilitySelectedTextAttribute();
public static final NSString NSAccessibilitySelectedTextAttribute = new NSString(NSAccessibilitySelectedTextAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilitySelectedTextChangedNotification();
public static final NSString NSAccessibilitySelectedTextChangedNotification = new NSString(NSAccessibilitySelectedTextChangedNotification());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilitySelectedTextRangeAttribute();
public static final NSString NSAccessibilitySelectedTextRangeAttribute = new NSString(NSAccessibilitySelectedTextRangeAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilitySelectedTextRangesAttribute();
public static final NSString NSAccessibilitySelectedTextRangesAttribute = new NSString(NSAccessibilitySelectedTextRangesAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityServesAsTitleForUIElementsAttribute();
public static final NSString NSAccessibilityServesAsTitleForUIElementsAttribute = new NSString(NSAccessibilityServesAsTitleForUIElementsAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilitySizeAttribute();
public static final NSString NSAccessibilitySizeAttribute = new NSString(NSAccessibilitySizeAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilitySliderRole();
public static final NSString NSAccessibilitySliderRole = new NSString(NSAccessibilitySliderRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilitySortButtonRole();
public static final NSString NSAccessibilitySortButtonRole = new NSString(NSAccessibilitySortButtonRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilitySplitterRole();
public static final NSString NSAccessibilitySplitterRole = new NSString(NSAccessibilitySplitterRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityStandardWindowSubrole();
public static final NSString NSAccessibilityStandardWindowSubrole = new NSString(NSAccessibilityStandardWindowSubrole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityStaticTextRole();
public static final NSString NSAccessibilityStaticTextRole = new NSString(NSAccessibilityStaticTextRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityStringForRangeParameterizedAttribute();
public static final NSString NSAccessibilityStringForRangeParameterizedAttribute = new NSString(NSAccessibilityStringForRangeParameterizedAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityStyleRangeForIndexParameterizedAttribute();
public static final NSString NSAccessibilityStyleRangeForIndexParameterizedAttribute = new NSString(NSAccessibilityStyleRangeForIndexParameterizedAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilitySubroleAttribute();
public static final NSString NSAccessibilitySubroleAttribute = new NSString(NSAccessibilitySubroleAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilitySystemDialogSubrole();
public static final NSString NSAccessibilitySystemDialogSubrole = new NSString(NSAccessibilitySystemDialogSubrole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityTabGroupRole();
public static final NSString NSAccessibilityTabGroupRole = new NSString(NSAccessibilityTabGroupRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityTableRole();
public static final NSString NSAccessibilityTableRole = new NSString(NSAccessibilityTableRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityTableRowSubrole();
public static final NSString NSAccessibilityTableRowSubrole = new NSString(NSAccessibilityTableRowSubrole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityTabsAttribute();
public static final NSString NSAccessibilityTabsAttribute = new NSString(NSAccessibilityTabsAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityTextAreaRole();
public static final NSString NSAccessibilityTextAreaRole = new NSString(NSAccessibilityTextAreaRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityTextFieldRole();
public static final NSString NSAccessibilityTextFieldRole = new NSString(NSAccessibilityTextFieldRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityTextLinkSubrole();
public static final NSString NSAccessibilityTextLinkSubrole = new NSString(NSAccessibilityTextLinkSubrole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityTitleAttribute();
public static final NSString NSAccessibilityTitleAttribute = new NSString(NSAccessibilityTitleAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityTitleUIElementAttribute();
public static final NSString NSAccessibilityTitleUIElementAttribute = new NSString(NSAccessibilityTitleUIElementAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityToolbarRole();
public static final NSString NSAccessibilityToolbarRole = new NSString(NSAccessibilityToolbarRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityTopLevelUIElementAttribute();
public static final NSString NSAccessibilityTopLevelUIElementAttribute = new NSString(NSAccessibilityTopLevelUIElementAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityUnknownRole();
public static final NSString NSAccessibilityUnknownRole = new NSString(NSAccessibilityUnknownRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityUnknownSubrole();
public static final NSString NSAccessibilityUnknownSubrole = new NSString(NSAccessibilityUnknownSubrole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityValueAttribute();
public static final NSString NSAccessibilityValueAttribute = new NSString(NSAccessibilityValueAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityValueChangedNotification();
public static final NSString NSAccessibilityValueChangedNotification = new NSString(NSAccessibilityValueChangedNotification());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityValueDescriptionAttribute();
public static final NSString NSAccessibilityValueDescriptionAttribute = new NSString(NSAccessibilityValueDescriptionAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityValueIndicatorRole();
public static final NSString NSAccessibilityValueIndicatorRole = new NSString(NSAccessibilityValueIndicatorRole());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityVerticalOrientationValue();
public static final NSString NSAccessibilityVerticalOrientationValue = new NSString(NSAccessibilityVerticalOrientationValue());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityVerticalScrollBarAttribute();
public static final NSString NSAccessibilityVerticalScrollBarAttribute = new NSString(NSAccessibilityVerticalScrollBarAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityVisibleCharacterRangeAttribute();
public static final NSString NSAccessibilityVisibleCharacterRangeAttribute = new NSString(NSAccessibilityVisibleCharacterRangeAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityVisibleChildrenAttribute();
public static final NSString NSAccessibilityVisibleChildrenAttribute = new NSString(NSAccessibilityVisibleChildrenAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityWindowAttribute();
public static final NSString NSAccessibilityWindowAttribute = new NSString(NSAccessibilityWindowAttribute());
/** @method flags=const */
public static final native int /*long*/ NSAccessibilityWindowRole();
public static final NSString NSAccessibilityWindowRole = new NSString(NSAccessibilityWindowRole());
/** @method flags=const */
public static final native int /*long*/ NSApplicationDidChangeScreenParametersNotification();
public static final NSString NSApplicationDidChangeScreenParametersNotification = new NSString(NSApplicationDidChangeScreenParametersNotification());
/** @method flags=const */
public static final native int /*long*/ NSBackgroundColorAttributeName();
public static final NSString NSBackgroundColorAttributeName = new NSString(NSBackgroundColorAttributeName());
/** @method flags=const */
public static final native int /*long*/ NSBaselineOffsetAttributeName();
public static final NSString NSBaselineOffsetAttributeName = new NSString(NSBaselineOffsetAttributeName());
/** @method flags=const */
public static final native int /*long*/ NSCalibratedRGBColorSpace();
public static final NSString NSCalibratedRGBColorSpace = new NSString(NSCalibratedRGBColorSpace());
/** @method flags=const */
public static final native int /*long*/ NSDeviceRGBColorSpace();
public static final NSString NSDeviceRGBColorSpace = new NSString(NSDeviceRGBColorSpace());
/** @method flags=const */
public static final native int /*long*/ NSDeviceResolution();
public static final NSString NSDeviceResolution = new NSString(NSDeviceResolution());
/** @method flags=const */
public static final native int /*long*/ NSDragPboard();
public static final NSString NSDragPboard = new NSString(NSDragPboard());
/** @method flags=const */
public static final native int /*long*/ NSEventTrackingRunLoopMode();
public static final NSString NSEventTrackingRunLoopMode = new NSString(NSEventTrackingRunLoopMode());
/** @method flags=const */
public static final native int /*long*/ NSFilenamesPboardType();
public static final NSString NSFilenamesPboardType = new NSString(NSFilenamesPboardType());
/** @method flags=const */
public static final native int /*long*/ NSFontAttributeName();
public static final NSString NSFontAttributeName = new NSString(NSFontAttributeName());
/** @method flags=const */
public static final native int /*long*/ NSForegroundColorAttributeName();
public static final NSString NSForegroundColorAttributeName = new NSString(NSForegroundColorAttributeName());
/** @method flags=const */
public static final native int /*long*/ NSHTMLPboardType();
public static final NSString NSHTMLPboardType = new NSString(NSHTMLPboardType());
/** @method flags=const */
public static final native int /*long*/ NSLinkAttributeName();
public static final NSString NSLinkAttributeName = new NSString(NSLinkAttributeName());
/** @method flags=const */
public static final native int /*long*/ NSObliquenessAttributeName();
public static final NSString NSObliquenessAttributeName = new NSString(NSObliquenessAttributeName());
/** @method flags=const */
public static final native int /*long*/ NSParagraphStyleAttributeName();
public static final NSString NSParagraphStyleAttributeName = new NSString(NSParagraphStyleAttributeName());
/** @method flags=const */
public static final native int /*long*/ NSPrintAllPages();
public static final NSString NSPrintAllPages = new NSString(NSPrintAllPages());
/** @method flags=const */
public static final native int /*long*/ NSPrintCopies();
public static final NSString NSPrintCopies = new NSString(NSPrintCopies());
/** @method flags=const */
public static final native int /*long*/ NSPrintFirstPage();
public static final NSString NSPrintFirstPage = new NSString(NSPrintFirstPage());
/** @method flags=const */
public static final native int /*long*/ NSPrintJobDisposition();
public static final NSString NSPrintJobDisposition = new NSString(NSPrintJobDisposition());
/** @method flags=const */
public static final native int /*long*/ NSPrintLastPage();
public static final NSString NSPrintLastPage = new NSString(NSPrintLastPage());
/** @method flags=const */
public static final native int /*long*/ NSPrintMustCollate();
public static final NSString NSPrintMustCollate = new NSString(NSPrintMustCollate());
/** @method flags=const */
public static final native int /*long*/ NSPrintPreviewJob();
public static final NSString NSPrintPreviewJob = new NSString(NSPrintPreviewJob());
/** @method flags=const */
public static final native int /*long*/ NSPrintSaveJob();
public static final NSString NSPrintSaveJob = new NSString(NSPrintSaveJob());
/** @method flags=const */
public static final native int /*long*/ NSPrintSavePath();
public static final NSString NSPrintSavePath = new NSString(NSPrintSavePath());
/** @method flags=const */
public static final native int /*long*/ NSPrintScalingFactor();
public static final NSString NSPrintScalingFactor = new NSString(NSPrintScalingFactor());
/** @method flags=const */
public static final native int /*long*/ NSPrintSpoolJob();
public static final NSString NSPrintSpoolJob = new NSString(NSPrintSpoolJob());
/** @method flags=const */
public static final native int /*long*/ NSRTFPboardType();
public static final NSString NSRTFPboardType = new NSString(NSRTFPboardType());
/** @method flags=const */
public static final native int /*long*/ NSStrikethroughColorAttributeName();
public static final NSString NSStrikethroughColorAttributeName = new NSString(NSStrikethroughColorAttributeName());
/** @method flags=const */
public static final native int /*long*/ NSStrikethroughStyleAttributeName();
public static final NSString NSStrikethroughStyleAttributeName = new NSString(NSStrikethroughStyleAttributeName());
/** @method flags=const */
public static final native int /*long*/ NSStringPboardType();
public static final NSString NSStringPboardType = new NSString(NSStringPboardType());
/** @method flags=const */
public static final native int /*long*/ NSStrokeWidthAttributeName();
public static final NSString NSStrokeWidthAttributeName = new NSString(NSStrokeWidthAttributeName());
/** @method flags=const */
public static final native int /*long*/ NSSystemColorsDidChangeNotification();
public static final NSString NSSystemColorsDidChangeNotification = new NSString(NSSystemColorsDidChangeNotification());
/** @method flags=const */
public static final native int /*long*/ NSTIFFPboardType();
public static final NSString NSTIFFPboardType = new NSString(NSTIFFPboardType());
/** @method flags=const */
public static final native int /*long*/ NSToolbarCustomizeToolbarItemIdentifier();
public static final NSString NSToolbarCustomizeToolbarItemIdentifier = new NSString(NSToolbarCustomizeToolbarItemIdentifier());
/** @method flags=const */
public static final native int /*long*/ NSToolbarDidRemoveItemNotification();
public static final NSString NSToolbarDidRemoveItemNotification = new NSString(NSToolbarDidRemoveItemNotification());
/** @method flags=const */
public static final native int /*long*/ NSToolbarFlexibleSpaceItemIdentifier();
public static final NSString NSToolbarFlexibleSpaceItemIdentifier = new NSString(NSToolbarFlexibleSpaceItemIdentifier());
/** @method flags=const */
public static final native int /*long*/ NSToolbarPrintItemIdentifier();
public static final NSString NSToolbarPrintItemIdentifier = new NSString(NSToolbarPrintItemIdentifier());
/** @method flags=const */
public static final native int /*long*/ NSToolbarSeparatorItemIdentifier();
public static final NSString NSToolbarSeparatorItemIdentifier = new NSString(NSToolbarSeparatorItemIdentifier());
/** @method flags=const */
public static final native int /*long*/ NSToolbarShowColorsItemIdentifier();
public static final NSString NSToolbarShowColorsItemIdentifier = new NSString(NSToolbarShowColorsItemIdentifier());
/** @method flags=const */
public static final native int /*long*/ NSToolbarShowFontsItemIdentifier();
public static final NSString NSToolbarShowFontsItemIdentifier = new NSString(NSToolbarShowFontsItemIdentifier());
/** @method flags=const */
public static final native int /*long*/ NSToolbarSpaceItemIdentifier();
public static final NSString NSToolbarSpaceItemIdentifier = new NSString(NSToolbarSpaceItemIdentifier());
/** @method flags=const */
public static final native int /*long*/ NSToolbarWillAddItemNotification();
public static final NSString NSToolbarWillAddItemNotification = new NSString(NSToolbarWillAddItemNotification());
/** @method flags=const */
public static final native int /*long*/ NSURLPboardType();
public static final NSString NSURLPboardType = new NSString(NSURLPboardType());
/** @method flags=const */
public static final native int /*long*/ NSUnderlineColorAttributeName();
public static final NSString NSUnderlineColorAttributeName = new NSString(NSUnderlineColorAttributeName());
/** @method flags=const */
public static final native int /*long*/ NSUnderlineStyleAttributeName();
public static final NSString NSUnderlineStyleAttributeName = new NSString(NSUnderlineStyleAttributeName());
/** @method flags=const */
public static final native int /*long*/ kCFRunLoopCommonModes();
/** @method flags=const */
public static final native int /*long*/ NSDefaultRunLoopMode();
public static final NSString NSDefaultRunLoopMode = new NSString(NSDefaultRunLoopMode());
/** @method flags=const */
public static final native int /*long*/ NSErrorFailingURLStringKey();
public static final NSString NSErrorFailingURLStringKey = new NSString(NSErrorFailingURLStringKey());

/** Functions */

/**
 * @param action cast=(NSString*)
 */
public static final native int /*long*/ NSAccessibilityActionDescription(int /*long*/ action);
/**
 * @param element cast=(id)
 * @param notification cast=(NSString*)
 */
public static final native void NSAccessibilityPostNotification(int /*long*/ element, int /*long*/ notification);
/**
 * @param element cast=(id)
 * @param attribute cast=(NSString*)
 * @param value cast=(id)
 */
public static final native void NSAccessibilityRaiseBadArgumentException(int /*long*/ element, int /*long*/ attribute, int /*long*/ value);
/**
 * @param role cast=(NSString*)
 * @param subrole cast=(NSString*)
 */
public static final native int /*long*/ NSAccessibilityRoleDescription(int /*long*/ role, int /*long*/ subrole);
/**
 * @param element cast=(id)
 */
public static final native int /*long*/ NSAccessibilityRoleDescriptionForUIElement(int /*long*/ element);
/**
 * @param element cast=(id)
 */
public static final native int /*long*/ NSAccessibilityUnignoredAncestor(int /*long*/ element);
/**
 * @param originalChildren cast=(NSArray*)
 */
public static final native int /*long*/ NSAccessibilityUnignoredChildren(int /*long*/ originalChildren);
/**
 * @param originalChild cast=(id)
 */
public static final native int /*long*/ NSAccessibilityUnignoredChildrenForOnlyChild(int /*long*/ originalChild);
/**
 * @param element cast=(id)
 */
public static final native int /*long*/ NSAccessibilityUnignoredDescendant(int /*long*/ element);
public static final native void NSBeep();
/**
 * @param depth cast=(NSWindowDepth)
 */
public static final native int /*long*/ NSBitsPerPixelFromDepth(int depth);
/**
 * @param srcGState cast=(NSInteger)
 * @param srcRect flags=struct
 * @param destPoint flags=struct
 */
public static final native void NSCopyBits(int /*long*/ srcGState, NSRect srcRect, NSPoint destPoint);
/**
 * @param colorSpaceName cast=(NSString*)
 */
public static final native int /*long*/ NSNumberOfColorComponents(int /*long*/ colorSpaceName);
/**
 * @param theData cast=(CFDataRef)
 */
public static final native int /*long*/ CFDataGetBytePtr(int /*long*/ theData);
/**
 * @param theData cast=(CFDataRef)
 */
public static final native int /*long*/ CFDataGetLength(int /*long*/ theData);
/**
 * @param cf cast=(CFTypeRef)
 */
public static final native void CFRelease(int /*long*/ cf);
/**
 * @param rl cast=(CFRunLoopRef)
 * @param observer cast=(CFRunLoopObserverRef)
 * @param mode cast=(CFStringRef)
 */
public static final native void CFRunLoopAddObserver(int /*long*/ rl, int /*long*/ observer, int /*long*/ mode);
public static final native int /*long*/ CFRunLoopGetCurrent();
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param activities cast=(CFOptionFlags)
 * @param repeats cast=(Boolean)
 * @param order cast=(CFIndex)
 * @param callout cast=(CFRunLoopObserverCallBack)
 * @param context cast=(CFRunLoopObserverContext*)
 */
public static final native int /*long*/ CFRunLoopObserverCreate(int /*long*/ allocator, int /*long*/ activities, boolean repeats, int /*long*/ order, int /*long*/ callout, int /*long*/ context);
/**
 * @param observer cast=(CFRunLoopObserverRef)
 */
public static final native void CFRunLoopObserverInvalidate(int /*long*/ observer);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param originalString cast=(CFStringRef)
 * @param charactersToLeaveUnescaped cast=(CFStringRef)
 * @param legalURLCharactersToBeEscaped cast=(CFStringRef)
 * @param encoding cast=(CFStringEncoding)
 */
public static final native int /*long*/ CFURLCreateStringByAddingPercentEscapes(int /*long*/ allocator, int /*long*/ originalString, int /*long*/ charactersToLeaveUnescaped, int /*long*/ legalURLCharactersToBeEscaped, int encoding);
/**
 * @param data cast=(void*)
 * @param width cast=(size_t)
 * @param height cast=(size_t)
 * @param bitsPerComponent cast=(size_t)
 * @param bytesPerRow cast=(size_t)
 * @param colorspace cast=(CGColorSpaceRef)
 * @param bitmapInfo cast=(CGBitmapInfo)
 */
public static final native int /*long*/ CGBitmapContextCreate(int /*long*/ data, int /*long*/ width, int /*long*/ height, int /*long*/ bitsPerComponent, int /*long*/ bytesPerRow, int /*long*/ colorspace, int bitmapInfo);
/**
 * @param c cast=(CGContextRef)
 */
public static final native int /*long*/ CGBitmapContextCreateImage(int /*long*/ c);
/**
 * @param c cast=(CGContextRef)
 */
public static final native int /*long*/ CGBitmapContextGetData(int /*long*/ c);
public static final native int /*long*/ CGColorSpaceCreateDeviceRGB();
/**
 * @param space cast=(CGColorSpaceRef)
 */
public static final native void CGColorSpaceRelease(int /*long*/ space);
/**
 * @param context cast=(CGContextRef)
 * @param path cast=(CGPathRef)
 */
public static final native void CGContextAddPath(int /*long*/ context, int /*long*/ path);
/**
 * @param c cast=(CGContextRef)
 * @param rect flags=struct
 * @param image cast=(CGImageRef)
 */
public static final native void CGContextDrawImage(int /*long*/ c, CGRect rect, int /*long*/ image);
/**
 * @param c cast=(CGContextRef)
 */
public static final native void CGContextRelease(int /*long*/ c);
/**
 * @param c cast=(CGContextRef)
 */
public static final native void CGContextReplacePathWithStrokedPath(int /*long*/ c);
/**
 * @param c cast=(CGContextRef)
 */
public static final native void CGContextRestoreGState(int /*long*/ c);
/**
 * @param c cast=(CGContextRef)
 */
public static final native void CGContextSaveGState(int /*long*/ c);
/**
 * @param c cast=(CGContextRef)
 * @param cap cast=(CGLineCap)
 */
public static final native void CGContextSetLineCap(int /*long*/ c, int cap);
/**
 * @param c cast=(CGContextRef)
 * @param phase cast=(CGFloat)
 * @param lengths cast=(CGFloat*)
 * @param count cast=(size_t)
 */
public static final native void CGContextSetLineDash(int /*long*/ c, float /*double*/ phase, float[] lengths, int /*long*/ count);
/**
 * @param c cast=(CGContextRef)
 * @param join cast=(CGLineJoin)
 */
public static final native void CGContextSetLineJoin(int /*long*/ c, int join);
/**
 * @param c cast=(CGContextRef)
 * @param width cast=(CGFloat)
 */
public static final native void CGContextSetLineWidth(int /*long*/ c, float /*double*/ width);
/**
 * @param c cast=(CGContextRef)
 * @param limit cast=(CGFloat)
 */
public static final native void CGContextSetMiterLimit(int /*long*/ c, float /*double*/ limit);
/**
 * @param c cast=(CGContextRef)
 * @param tx cast=(CGFloat)
 * @param ty cast=(CGFloat)
 */
public static final native void CGContextTranslateCTM(int /*long*/ c, float /*double*/ tx, float /*double*/ ty);
/**
 * @param info cast=(void*)
 * @param data cast=(void*)
 * @param size cast=(size_t)
 * @param releaseData cast=(CGDataProviderReleaseDataCallback)
 */
public static final native int /*long*/ CGDataProviderCreateWithData(int /*long*/ info, int /*long*/ data, int /*long*/ size, int /*long*/ releaseData);
/**
 * @param provider cast=(CGDataProviderRef)
 */
public static final native void CGDataProviderRelease(int /*long*/ provider);
/**
 * @param display cast=(CGDirectDisplayID)
 */
public static final native int /*long*/ CGDisplayBaseAddress(int display);
/**
 * @param display cast=(CGDirectDisplayID)
 */
public static final native int /*long*/ CGDisplayBitsPerPixel(int display);
/**
 * @param display cast=(CGDirectDisplayID)
 */
public static final native int /*long*/ CGDisplayBitsPerSample(int display);
/**
 * @param display cast=(CGDirectDisplayID)
 */
public static final native int /*long*/ CGDisplayBytesPerRow(int display);
/**
 * @param display cast=(CGDirectDisplayID)
 */
public static final native int /*long*/ CGDisplayPixelsHigh(int display);
/**
 * @param display cast=(CGDirectDisplayID)
 */
public static final native int /*long*/ CGDisplayPixelsWide(int display);
/**
 * @param doCombineState cast=(boolean_t)
 */
public static final native int CGEnableEventStateCombining(int doCombineState);
/**
 * @param source cast=(CGEventSourceRef)
 * @param virtualKey cast=(CGKeyCode)
 * @param keyDown cast=(_Bool)
 */
public static final native int /*long*/ CGEventCreateKeyboardEvent(int /*long*/ source, short virtualKey, boolean keyDown);
/**
 * @param event cast=(CGEventRef)
 * @param field cast=(CGEventField)
 */
public static final native long CGEventGetIntegerValueField(int /*long*/ event, int field);
/**
 * @param event cast=(CGEventRef)
 * @param stringLength cast=(UniCharCount)
 * @param unicodeString cast=(UniChar*)
 */
public static final native void CGEventKeyboardSetUnicodeString(int /*long*/ event, int /*long*/ stringLength, char[] unicodeString);
/**
 * @param tap cast=(CGEventTapLocation)
 * @param event cast=(CGEventRef)
 */
public static final native void CGEventPost(int tap, int /*long*/ event);
/**
 * @param rect flags=struct
 * @param maxDisplays cast=(CGDisplayCount)
 * @param dspys cast=(CGDirectDisplayID*)
 * @param dspyCnt cast=(CGDisplayCount*)
 */
public static final native int CGGetDisplaysWithRect(CGRect rect, int maxDisplays, int /*long*/ dspys, int /*long*/ dspyCnt);
/**
 * @param width cast=(size_t)
 * @param height cast=(size_t)
 * @param bitsPerComponent cast=(size_t)
 * @param bitsPerPixel cast=(size_t)
 * @param bytesPerRow cast=(size_t)
 * @param colorspace cast=(CGColorSpaceRef)
 * @param bitmapInfo cast=(CGBitmapInfo)
 * @param provider cast=(CGDataProviderRef)
 * @param decode cast=(CGFloat*)
 * @param shouldInterpolate cast=(_Bool)
 * @param intent cast=(CGColorRenderingIntent)
 */
public static final native int /*long*/ CGImageCreate(int /*long*/ width, int /*long*/ height, int /*long*/ bitsPerComponent, int /*long*/ bitsPerPixel, int /*long*/ bytesPerRow, int /*long*/ colorspace, int bitmapInfo, int /*long*/ provider, int /*long*/ decode, boolean shouldInterpolate, int intent);
/**
 * @param image cast=(CGImageRef)
 */
public static final native int /*long*/ CGImageGetHeight(int /*long*/ image);
/**
 * @param image cast=(CGImageRef)
 */
public static final native int /*long*/ CGImageGetWidth(int /*long*/ image);
/**
 * @param image cast=(CGImageRef)
 */
public static final native void CGImageRelease(int /*long*/ image);
/**
 * @param path cast=(CGMutablePathRef)
 * @param m cast=(CGAffineTransform*)
 * @param cp1x cast=(CGFloat)
 * @param cp1y cast=(CGFloat)
 * @param cp2x cast=(CGFloat)
 * @param cp2y cast=(CGFloat)
 * @param x cast=(CGFloat)
 * @param y cast=(CGFloat)
 */
public static final native void CGPathAddCurveToPoint(int /*long*/ path, int /*long*/ m, float /*double*/ cp1x, float /*double*/ cp1y, float /*double*/ cp2x, float /*double*/ cp2y, float /*double*/ x, float /*double*/ y);
/**
 * @param path cast=(CGMutablePathRef)
 * @param m cast=(CGAffineTransform*)
 * @param x cast=(CGFloat)
 * @param y cast=(CGFloat)
 */
public static final native void CGPathAddLineToPoint(int /*long*/ path, int /*long*/ m, float /*double*/ x, float /*double*/ y);
/**
 * @param path cast=(CGPathRef)
 * @param info cast=(void*)
 * @param function cast=(CGPathApplierFunction)
 */
public static final native void CGPathApply(int /*long*/ path, int /*long*/ info, int /*long*/ function);
/**
 * @param path cast=(CGMutablePathRef)
 */
public static final native void CGPathCloseSubpath(int /*long*/ path);
/**
 * @param path cast=(CGPathRef)
 */
public static final native int /*long*/ CGPathCreateCopy(int /*long*/ path);
public static final native int /*long*/ CGPathCreateMutable();
/**
 * @param path cast=(CGMutablePathRef)
 * @param m cast=(CGAffineTransform*)
 * @param x cast=(CGFloat)
 * @param y cast=(CGFloat)
 */
public static final native void CGPathMoveToPoint(int /*long*/ path, int /*long*/ m, float /*double*/ x, float /*double*/ y);
/**
 * @param path cast=(CGPathRef)
 */
public static final native void CGPathRelease(int /*long*/ path);
/**
 * @param mouseCursorPosition flags=struct
 * @param updateMouseCursorPosition cast=(boolean_t)
 * @param buttonCount cast=(CGButtonCount)
 * @param mouseButtonDown cast=(boolean_t)
 */
public static final native int CGPostMouseEvent(CGPoint mouseCursorPosition, boolean updateMouseCursorPosition, int buttonCount, boolean mouseButtonDown, boolean varArg0, boolean varArg1, boolean varArg2, boolean varArg3);
/**
 * @param wheelCount cast=(CGWheelCount)
 * @param wheel1 cast=(int32_t)
 */
public static final native int CGPostScrollWheelEvent(int wheelCount, int wheel1);
/**
 * @param filter cast=(CGEventFilterMask)
 * @param state cast=(CGEventSuppressionState)
 */
public static final native int CGSetLocalEventsFilterDuringSuppressionState(int filter, int state);
/**
 * @param seconds cast=(CFTimeInterval)
 */
public static final native int CGSetLocalEventsSuppressionInterval(double seconds);
/**
 * @param newCursorPosition flags=struct
 */
public static final native int CGWarpMouseCursorPosition(CGPoint newCursorPosition);
/**
 * @param aRect flags=struct
 * @param bRect flags=struct
 */
public static final native boolean NSEqualRects(NSRect aRect, NSRect bRect);
/**
 * @param hfsFileTypeCode cast=(OSType)
 */
public static final native int /*long*/ NSFileTypeForHFSTypeCode(int hfsFileTypeCode);
/**
 * @param typePtr cast=(char*)
 * @param sizep cast=(NSUInteger*)
 * @param alignp cast=(NSUInteger*)
 */
public static final native int /*long*/ NSGetSizeAndAlignment(int /*long*/ typePtr, int[] /*long[]*/ sizep, int[] /*long[]*/ alignp);
/**
 * @param aPoint flags=struct
 * @param aRect flags=struct
 */
public static final native boolean NSPointInRect(NSPoint aPoint, NSRect aRect);
/**
 * @param directory cast=(NSSearchPathDirectory)
 * @param domainMask cast=(NSSearchPathDomainMask)
 * @param expandTilde cast=(BOOL)
 */
public static final native int /*long*/ NSSearchPathForDirectoriesInDomains(int /*long*/ directory, int /*long*/ domainMask, boolean expandTilde);
public static final native int /*long*/ NSTemporaryDirectory();

/** Sends */

/** @method flags=cast */
public static final native boolean objc_msgSend_bool(int /*long*/ id, int /*long*/ sel);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native boolean objc_msgSend_bool(int /*long*/ id, int /*long*/ sel, NSPoint arg0);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native boolean objc_msgSend_bool(int /*long*/ id, int /*long*/ sel, NSRange arg0, int /*long*/ arg1);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native boolean objc_msgSend_bool(int /*long*/ id, int /*long*/ sel, NSRect arg0);
/** @method flags=cast */
public static final native boolean objc_msgSend_bool(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0);
/**
 * @method flags=cast
 * @param arg1 flags=struct
 */
public static final native boolean objc_msgSend_bool(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, NSPoint arg1);
/**
 * @method flags=cast
 * @param arg1 flags=struct
 */
public static final native boolean objc_msgSend_bool(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, NSSize arg1, boolean arg2);
/** @method flags=cast */
public static final native boolean objc_msgSend_bool(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1);
/** @method flags=cast */
public static final native boolean objc_msgSend_bool(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2);
/** @method flags=cast */
public static final native boolean objc_msgSend_bool(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3, int /*long*/ arg4);
/** @method flags=cast */
public static final native boolean objc_msgSend_bool(int /*long*/ id, int /*long*/ sel, short arg0);
/** @method flags=cast */
public static final native double objc_msgSend_fpret(int /*long*/ id, int /*long*/ sel);
/** @method flags=cast */
public static final native double objc_msgSend_fpret(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0);
/** @method flags=cast */
public static final native double objc_msgSend_fpret(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSAffineTransformStruct arg0);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSPoint arg0);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 * @param arg2 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSPoint arg0, NSPoint arg1, NSPoint arg2);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSPoint arg0, NSPoint arg1, int /*long*/ arg2);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSPoint arg0, NSRect arg1, int /*long*/ arg2, float /*double*/ arg3);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSPoint arg0, float /*double*/ arg1, float /*double*/ arg2, float /*double*/ arg3);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSPoint arg0, float /*double*/ arg1, float /*double*/ arg2, float /*double*/ arg3, boolean arg4);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSPoint arg0, int /*long*/ arg1, float[] /*double[]*/ arg2);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRange arg0);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRange arg0, NSPoint arg1);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRange arg0, NSRange arg1, int /*long*/ arg2, int /*long*/ arg3);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRange arg0, int /*long*/ arg1);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRange arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3, int /*long*/ arg4, int /*long*/ arg5);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRect arg0);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRect arg0, NSPoint arg1);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 * @param arg2 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRect arg0, NSRange arg1, NSRect arg2);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRect arg0, NSRect arg1, int /*long*/ arg2, float /*double*/ arg3);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRect arg0, boolean arg1);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRect arg0, float /*double*/ arg1);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRect arg0, float /*double*/ arg1, float /*double*/ arg2);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRect arg0, int /*long*/ arg1);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRect arg0, int /*long*/ arg1, boolean arg2, int /*long*/ arg3);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRect arg0, int /*long*/ arg1, int /*long*/ arg2);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRect arg0, int /*long*/ arg1, int /*long*/ arg2, boolean arg3);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRect arg0, int /*long*/ arg1, int /*long*/ arg2, boolean arg3, int /*long*/ arg4);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSRect arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, NSSize arg0);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, boolean arg0);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, boolean arg0, int /*long*/ arg1);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, byte[] arg0);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, byte[] arg0, int /*long*/ arg1);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, char[] arg0);
/**
 * @method flags=cast
 * @param arg1 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, char[] arg0, NSRange arg1);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, char[] arg0, int /*long*/ arg1);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, double arg0);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, double arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3, boolean arg4);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, float /*double*/ arg0, float /*double*/ arg1);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, float /*double*/ arg0, float /*double*/ arg1, float /*double*/ arg2, float /*double*/ arg3);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, float /*double*/ arg0, int /*long*/ arg1);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, float[] /*double[]*/ arg0);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, float[] /*double[]*/ arg0, int /*long*/ arg1, float /*double*/ arg2);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0);
/**
 * @method flags=cast
 * @param arg1 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, NSPoint arg1);
/**
 * @method flags=cast
 * @param arg1 flags=struct
 * @param arg2 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, NSPoint arg1, NSSize arg2, int /*long*/ arg3, int /*long*/ arg4, int /*long*/ arg5, boolean arg6);
/**
 * @method flags=cast
 * @param arg1 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, NSPoint arg1, int /*long*/ arg2, double arg3, int /*long*/ arg4, int /*long*/ arg5, int /*long*/ arg6, int /*long*/ arg7, int /*long*/ arg8);
/**
 * @method flags=cast
 * @param arg1 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, NSPoint arg1, int /*long*/ arg2, double arg3, int /*long*/ arg4, int /*long*/ arg5, short arg6, int /*long*/ arg7, int /*long*/ arg8);
/**
 * @method flags=cast
 * @param arg1 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, NSRange arg1);
/**
 * @method flags=cast
 * @param arg1 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, NSRect arg1, int /*long*/ arg2);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, boolean arg1);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, float /*double*/ arg1);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1);
/**
 * @method flags=cast
 * @param arg2 flags=struct
 */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, NSRange arg2);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, boolean arg2);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, boolean arg3);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, float /*double*/ arg3);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3, int /*long*/ arg4);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3, int /*long*/ arg4, boolean arg5, boolean arg6, int /*long*/ arg7, int /*long*/ arg8, int /*long*/ arg9);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3, int /*long*/ arg4, boolean arg5, boolean arg6, int /*long*/ arg7, int /*long*/ arg8, int /*long*/ arg9, int /*long*/ arg10);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, int /*long*/ arg2, int /*long*/ arg3, int /*long*/ arg4, int /*long*/ arg5, int /*long*/ arg6);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int[] /*long[]*/ arg0);
/** @method flags=cast */
public static final native int /*long*/ objc_msgSend(int /*long*/ id, int /*long*/ sel, int[] /*long[]*/ arg0, int arg1, int arg2);
/** @method flags=cast */
public static final native int objc_msgSend(int id, int sel, float arg0);
/** @method flags=cast */
public static final native long objc_msgSend(long id, long sel, float arg0, double arg1);
/** @method flags=cast */
public static final native long objc_msgSend(long id, long sel, int arg0);
/** @method flags=cast */
public static final native long objc_msgSend(long id, long sel, int[] arg0);
/** @method flags=cast */
public static final native long objc_msgSend(long id, long sel, long[] arg0, long arg1, long arg2);
/** @method flags=cast */
public static final native void objc_msgSend_stret(NSAffineTransformStruct result, int /*long*/ id, int /*long*/ sel);
/** @method flags=cast */
public static final native void objc_msgSend_stret(NSPoint result, int /*long*/ id, int /*long*/ sel);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSPoint result, int /*long*/ id, int /*long*/ sel, NSPoint arg0);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSPoint result, int /*long*/ id, int /*long*/ sel, NSPoint arg0, int /*long*/ arg1);
/** @method flags=cast */
public static final native void objc_msgSend_stret(NSPoint result, int /*long*/ id, int /*long*/ sel, int /*long*/ arg0);
/** @method flags=cast */
public static final native void objc_msgSend_stret(NSRange result, int /*long*/ id, int /*long*/ sel);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSRange result, int /*long*/ id, int /*long*/ sel, NSRange arg0, int /*long*/ arg1);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSRange result, int /*long*/ id, int /*long*/ sel, NSRect arg0);
/** @method flags=cast */
public static final native void objc_msgSend_stret(NSRange result, int /*long*/ id, int /*long*/ sel, int /*long*/ arg0);
/** @method flags=cast */
public static final native void objc_msgSend_stret(NSRect result, int /*long*/ id, int /*long*/ sel);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSRect result, int /*long*/ id, int /*long*/ sel, NSRange arg0, int /*long*/ arg1);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSRect result, int /*long*/ id, int /*long*/ sel, NSRect arg0);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSRect result, int /*long*/ id, int /*long*/ sel, NSRect arg0, int /*long*/ arg1);
/** @method flags=cast */
public static final native void objc_msgSend_stret(NSRect result, int /*long*/ id, int /*long*/ sel, int /*long*/ arg0);
/** @method flags=cast */
public static final native void objc_msgSend_stret(NSRect result, int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1);
/** @method flags=cast */
public static final native void objc_msgSend_stret(NSRect result, int /*long*/ id, int /*long*/ sel, int /*long*/ arg0, int /*long*/ arg1, boolean arg2);
/** @method flags=cast */
public static final native void objc_msgSend_stret(NSSize result, int /*long*/ id, int /*long*/ sel);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSSize result, int /*long*/ id, int /*long*/ sel, NSRect arg0);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSSize result, int /*long*/ id, int /*long*/ sel, NSSize arg0);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSSize result, int /*long*/ id, int /*long*/ sel, NSSize arg0, boolean arg1, boolean arg2, int /*long*/ arg3);
/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSSize result, int /*long*/ id, int /*long*/ sel, NSSize arg0, int /*long*/ arg1);

/** Sizeof natives */
public static final native int CGPathElement_sizeof();
public static final native int CGPoint_sizeof();
public static final native int CGRect_sizeof();
public static final native int CGSize_sizeof();
public static final native int NSAffineTransformStruct_sizeof();
public static final native int NSPoint_sizeof();
public static final native int NSRange_sizeof();
public static final native int NSRect_sizeof();
public static final native int NSSize_sizeof();

/** Memmove natives */

/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(int /*long*/ dest, CGPathElement src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(CGPathElement dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(int /*long*/ dest, CGPoint src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(CGPoint dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(int /*long*/ dest, CGRect src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(CGRect dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(int /*long*/ dest, CGSize src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(CGSize dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(int /*long*/ dest, NSAffineTransformStruct src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(NSAffineTransformStruct dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(int /*long*/ dest, NSPoint src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(NSPoint dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(int /*long*/ dest, NSRange src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(NSRange dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(int /*long*/ dest, NSRect src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(NSRect dest, int /*long*/ src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(int /*long*/ dest, NSSize src, int /*long*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(NSSize dest, int /*long*/ src, int /*long*/ size);

/** This section is auto generated */
}
