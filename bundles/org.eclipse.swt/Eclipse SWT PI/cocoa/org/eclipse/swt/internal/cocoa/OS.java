/*******************************************************************************
 * Copyright (c) 2007, 2017 IBM Corporation and others.
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
 *     Karsten Thoms <karsten.thoms@itemis.de> - Bug 522349
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

import org.eclipse.swt.internal.*;

public class OS extends C {
	static {
		Library.loadLibrary("swt-pi"); //$NON-NLS-1$
	}

	/**
	 * NOTE: For new code, use {@link #VERSION_MMB} and {@link #VERSION_MMB(int, int, int)}
	 */
	public static final int VERSION;
	public static final int VERSION_MMB;
	static {
		int [] response = new int [1];
		OS.Gestalt (OS.gestaltSystemVersion, response);
		VERSION = response [0] & 0xffff;
		OS.Gestalt (OS.gestaltSystemVersionMajor, response);
		int major = response [0];
		OS.Gestalt (OS.gestaltSystemVersionMinor, response);
		int minor = response [0];
		OS.Gestalt (OS.gestaltSystemVersionBugFix, response);
		int bugFix = response [0];
		VERSION_MMB = VERSION_MMB (major, minor, bugFix);
	}

	public static int VERSION_MMB (int major, int minor, int bugFix) {
		return (major << 16) + (minor << 8) + bugFix;
	}

	/*
	 *  Magic number explanation, from Cocoa's TextSizingExample:
	 *
	 *  "The first is called LargeNumberForText [1.0e7] and it was not arbitrarily chosen.
	 *  The actual value was chosen to be around the largest floating point value possible that can preserve at least pixel precision. [...]
	 *  It is not wise to use bigger dimensions for text system objects because, even if you ever fill all that space,
	 *  by the time you get to the far reaches, the letters won't have the precision necessary to look and act correctly.
	 *  [...] Because the Cocoa text system respects this limit in various ways, a second constant, NotQuiteAsLargeNumberForText, is used for the
	 *  field-like text views created by the FieldAspect class. This is simply half of LargeNumberForText; at sizes as large as LargeNumberForText,
	 *  the text system stops aligning text, for various reasons."
	 */
	public static final double /*float*/ MAX_TEXT_CONTAINER_SIZE = 0.5e7f;

	public static final int gestaltSystemVersion = ('s'<<24) + ('y'<<16) + ('s'<<8) + 'v';
	public static final int gestaltSystemVersionMajor = ('s'<<24) + ('y'<<16) + ('s'<<8) + '1';
	public static final int gestaltSystemVersionMinor = ('s'<<24) + ('y'<<16) + ('s'<<8) + '2';
	public static final int gestaltSystemVersionBugFix = ('s'<<24) + ('y'<<16) + ('s'<<8) + '3';
	public static final int noErr = 0;
	public static final int kProcessTransformToForegroundApplication = 1;
	public static final int kSystemIconsCreator = ('m' << 24) + ('a' << 16) + ('c' << 8) + 's';
	public static final int kAlertCautionIcon = ('c'<<24) + ('a'<<16) + ('u'<<8) + 't';
	public static final int kAlertNoteIcon = ('n'<<24) + ('o'<<16) + ('t'<<8) + 'e';
	public static final int kAlertStopIcon = ('s'<<24) + ('t'<<16) + ('o'<<8) + 'p';
	public static final int kHICommandHide = ('h'<<24) + ('i'<<16) + ('d'<<8) + 'e';
	public static final int kHICommandHideOthers = ('h'<<24) + ('i'<<16) + ('d'<<8) + 'o';
	public static final int kHICommandShowAll = ('s'<<24) + ('h'<<16) + ('a'<<8) + 'l';
	public static final int kHICommandQuit = ('q'<<24) + ('u'<<16) + ('i'<<8) + 't';
	public static final int kHICommandServices = ('s'<<24) + ('e'<<16) + ('r'<<8) + 'v';
	public static final int shiftKey = 1 << 9;
	public static final int kThemeMetricFocusRectOutset = 7;
	public static final int kHIThemeOrientationNormal = 0;
	public static final int kUIModeNormal = 0;
	public static final int kUIModeContentHidden = 2;
	public static final int kUIModeAllHidden = 3;
	public static final int kLSUnknownType = 0;
	public static final int kLSUnknownCreator = 0;
	public static final int kLSRolesAll = 0xFFFFFFFF;
	public static final int kAXUnderlineStyleNone = 0x0;
	public static final int kAXUnderlineStyleSingle = 0x1;
	public static final int kAXUnderlineStyleThick = 0x2;
	public static final int kAXUnderlineStyleDouble = 0x9;
	public static final int kPMDestinationPrinter = 1;
	public static final int kPMDuplexNone = 0x0001;
	public static final int kPMDuplexNoTumble = 0x0002;
	public static final int kPMDuplexTumble = 0x0003;

	public static final long /*int*/ sel_identity = Selector.sel_identity.value;
	public static final long /*int*/ sel_sendSearchSelection = Selector.sel_sendSearchSelection.value;
	public static final long /*int*/ sel_sendCancelSelection = Selector.sel_sendCancelSelection.value;
	public static final long /*int*/ sel_sendSelection = Selector.sel_sendSelection.value;
	public static final long /*int*/ sel_sendSelection_ = Selector.sel_sendSelection_.value;
	public static final long /*int*/ sel_sendDoubleSelection = Selector.sel_sendDoubleSelection.value;
	public static final long /*int*/ sel_sendVerticalSelection = Selector.sel_sendVerticalSelection.value;
	public static final long /*int*/ sel_sendHorizontalSelection = Selector.sel_sendHorizontalSelection.value;
	public static final long /*int*/ sel_timerProc_ = Selector.sel_timerProc_.value;
	public static final long /*int*/ sel_handleNotification_ = Selector.sel_handleNotification_.value;
	public static final long /*int*/ sel_callJava = Selector.sel_callJava.value;
	public static final long /*int*/ sel_callRunBeforeUnloadConfirmPanelWithMessage = Selector.sel_callRunBeforeUnloadConfirmPanelWithMessage.value;
	public static final long /*int*/ sel_createPanelDidEnd = Selector.sel_createPanelDidEnd.value;
	public static final long /*int*/ sel_systemColorSettingsChanged_ = Selector.sel_systemColorSettingsChanged_.value;
	public static final long /*int*/ sel_screenParametersChanged_ = Selector.sel_screenParametersChanged_.value;
	public static final long /*int*/ sel_panelDidEnd_returnCode_contextInfo_ = Selector.sel_panelDidEnd_returnCode_contextInfo_.value;
	public static final long /*int*/ sel_updateOpenGLContext_ = Selector.sel_updateOpenGLContext_.value;

	public static final long /*int*/ sel_overwriteExistingFileCheck = Selector.sel_overwriteExistingFileCheck.value;
	public static final long /*int*/ sel_setShowsHiddenFiles_ = Selector.sel_setShowsHiddenFiles_.value;

	public static final long /*int*/ sel_contextID = Selector.sel_contextID.value;

	public static final long /*int*/ sel__drawThemeProgressArea_ = Selector.sel__drawThemeProgressArea_.value;

	public static final long /*int*/ sel__setDashboardBehavior = Selector.sel__setDashboardBehavior.value;

	public static final long /*int*/ sel__setNeedsToUseHeartBeatWindow_ = Selector.sel__setNeedsToUseHeartBeatWindow_.value;

	public static final long /*int*/ class_WebPanelAuthenticationHandler = OS.objc_getClass("WebPanelAuthenticationHandler");
	public static final long /*int*/ sel_sharedHandler = Selector.sel_sharedHandler.value;
	public static final long /*int*/ sel_startAuthentication = Selector.sel_startAuthentication.value;
	public static final long /*int*/ sel_setAllowsAnyHTTPSCertificate = Selector.sel_setAllowsAnyHTTPSCertificate.value;

	public static final long /*int*/ sel_accessibleHandle = Selector.sel_accessibleHandle.value;
	public static final long /*int*/ sel_getImageView = Selector.sel_getImageView.value;

	public static final long /*int*/ sel_clearDeferFlushing = Selector.sel_clearDeferFlushing.value;

	public static final long /*int*/ sel_setShouldExpandItem_ = Selector.sel_setShouldExpandItem_.value;
	public static final long /*int*/ sel_setShouldScrollClipView_ = Selector.sel_setShouldScrollClipView_.value;

	public static final long /*int*/ sel_setQuota = Selector.sel_setQuota.value;
	public static final long /*int*/ sel_webView_frame_exceededDatabaseQuotaForSecurityOrigin_database_ = Selector.sel_webView_frame_exceededDatabaseQuotaForSecurityOrigin_database.value;

	public static final long /*int*/ sel_beginSheetModalForWindow_completionHandler_ = Selector.sel_beginSheetModalForWindow_completionHandler_.value;

	/** custom selector for SWT.OpenUrl event, also used in the launcher **/
	public static final long /*int*/ sel_application_openUrls_ = Selector.sel_application_openUrls_.value;

	/** non-API selector for NSCursor **/
	public static final long /*int*/ sel_busyButClickableCursor = Selector.sel_busyButClickableCursor.value;

	/* These are not generated in order to avoid creating static methods on all classes */
	public static final long /*int*/ sel_isSelectorExcludedFromWebScript_ = Selector.sel_isSelectorExcludedFromWebScript_.value;
	public static final long /*int*/ sel_webScriptNameForSelector_ = Selector.sel_webScriptNameForSelector_.value;

	public static final long /*int*/ sel_setColor_forAttribute_ = Selector.sel_setColor_forAttribute_.value;

	public static final long /*int*/ sel_javaRunLoopMode = Selector.sel_javaRunLoopMode.value;

	/* These are not generated in order to avoid attempting to create a java method called "null" */
	public static final long /*int*/ class_NSNull = objc_getClass("NSNull");
	public static final long /*int*/ sel_null = Selector.sel_null.value;

	/* NSTextAttachmentCell */
	/** @method callback_types=NSPoint;id;SEL;,callback_flags=struct;none;none; */
	public static final native long /*int*/ CALLBACK_cellBaselineOffset(long /*int*/ func);
	/** @method callback_types=NSSize;id;SEL;,callback_flags=struct;none;none; */
	public static final native long /*int*/ CALLBACK_NSTextAttachmentCell_cellSize(long /*int*/ func);
	/** @method callback_types=id;id;SEL;,callback_flags=struct;none;none; */
	public static final native long /*int*/ CALLBACK_NSTextAttachmentCell_attachment(long /*int*/ func);
	public static final long /*int*/ sel_cellBaselineOffset = Selector.sel_cellBaselineOffset.value;

	/*10.6 Accessibility Strings*/
	/** @method flags=const dynamic no_gen*/
	public static final native long /*int*/ NSAccessibilityRowIndexRangeAttribute();
	public static final NSString NSAccessibilityRowIndexRangeAttribute = new NSString(NSAccessibilityRowIndexRangeAttribute());
	/** @method flags=const dynamic no_gen*/
	public static final native long /*int*/ NSAccessibilityColumnIndexRangeAttribute();
	public static final NSString NSAccessibilityColumnIndexRangeAttribute = new NSString(NSAccessibilityColumnIndexRangeAttribute());
	/** @method flags=const dynamic no_gen*/
	public static final native long /*int*/ NSAccessibilityCellForColumnAndRowParameterizedAttribute();
	public static final NSString NSAccessibilityCellForColumnAndRowParameterizedAttribute = new NSString(NSAccessibilityCellForColumnAndRowParameterizedAttribute());
	/** @method flags=const dynamic no_gen*/
	public static final native long /*int*/ NSAccessibilityCellRole();
	public static final NSString NSAccessibilityCellRole = new NSString(NSAccessibilityCellRole());

	/** 10.7 selectors and constants */
	public static final long /*int*/ sel_isCompatibleWithOverlayScrollers = Selector.sel_isCompatibleWithOverlayScrollers.value;
	public static final long /*int*/ sel_flashScrollers = Selector.sel_flashScrollers.value;
	public static final long /*int*/ sel_frameSizeForContentSize_horizontalScrollerClass_verticalScrollerClass_borderType_controlSize_scrollerStyle_ = Selector.sel_frameSizeForContentSize_horizontalScrollerClass_verticalScrollerClass_borderType_controlSize_scrollerStyle_.value;
	public static final long /*int*/ sel_scrollerStyle = Selector.sel_scrollerStyle.value;
	public static final long /*int*/ sel_toggleFullScreen_ = Selector.sel_toggleFullScreen_.value;

	public static final int NSScrollerStyleLegacy = 0;
	public static final int NSScrollerStyleOverlay = 1;
	public static final int NSWindowFullScreenButton = 7;
	public static final int NSFullScreenWindowMask = 1 << 14;
	public static final int NSWindowCollectionBehaviorFullScreenPrimary = 1 << 7;
	public static final int NSWindowCollectionBehaviorFullScreenAuxiliary = 1 << 8;

	/** 10.12 selector */
	public static final long /*int*/ sel_setAllowsAutomaticWindowTabbing_ = Selector.sel_setAllowsAutomaticWindowTabbing.value;

	/* AWT application delegate. Remove these when JavaRuntimeSupport.framework has bridgesupport generated for it. */
	public static final long /*int*/ class_JRSAppKitAWT = objc_getClass("JRSAppKitAWT");
	public static final long /*int*/ sel_awtAppDelegate = Selector.sel_awtAppDelegate.value;

	public static final long /*int*/ class_NSToolbarView = objc_getClass("NSToolbarView");

	/*
	 * Wrapper function which will call NSSavePanel.beginSheetModalForWindow. This
	 * implementation allows passing of objective-C block from Java to C code, and
	 * receives a callback from the block to a Java function. Here, handler is a
	 * the function pointer of the function that will be called by the objective-C
	 * block.
	 */
	/** @method flags=no_gen*/
	public static native void beginSheetModalForWindow(long id, long sel, long window, long handler);
	public static void beginSheetModalForWindow(NSPanel id, NSWindow window, long handler) {
		OS.beginSheetModalForWindow(id.id, OS.sel_beginSheetModalForWindow_completionHandler_, window != null ? window.id : 0, handler);
	}

	/*
	 * Custom message that will be sent when setTheme is called for example from Platform UI code.
	 */
	public static final long /*int*/ sel_appAppearanceChanged = OS.sel_registerName("appAppearanceChanged");
	public static void setTheme(boolean isDarkTheme) {
		OS.objc_msgSend(NSApplication.sharedApplication().id, sel_appAppearanceChanged, isDarkTheme ? 1 : 0);
	}

/** JNI natives */

/** @method flags=jni */
public static final native long /*int*/ NewGlobalRef(Object object);
/**
 * @method flags=jni
 * @param globalRef cast=(jobject)
 */
public static final native void DeleteGlobalRef(long /*int*/ globalRef);
/** @method flags=no_gen */
public static final native Object JNIGetObject(long /*int*/ globalRef);

/** Carbon calls */

public static final native int Gestalt(int selector, int[] response);
/** @param psn cast=(ProcessSerialNumber *) */
public static final native int GetCurrentProcess(int[] psn);
/** @param psn cast=(ProcessSerialNumber *) */
public static final native int TransformProcessType(int[] psn, int transformState);
public static final native int CPSSetProcessName(int[] psn, long /*int*/ name);
/** @method flags=dynamic */
public static final native int SetThemeCursor(int themeCursor);
/** @method flags=dynamic */
public static final native int GetCurrentEventButtonState();
/** @method flags=dynamic */
public static final native int GetDblTime();
/** @method flags=dynamic
	@param inCreator cast=(OSType)
	@param inType cast=(OSType)
	@param inExtension cast=(CFStringRef)
	@param inMIMEType cast=(CFStringRef)
	@param inUsageFlags cast=(IconServicesUsageFlags)
	@param outIconRef cast=(IconRef *) */
public static final native int GetIconRefFromTypeInfo(int inCreator, int inType, long /*int*/ inExtension, long /*int*/ inMIMEType, int inUsageFlags, long /*int*/ outIconRef[]);
/** @method flags=dynamic
    @param context cast=(CGContextRef) */
public static final native long /*int*/ CGContextCopyPath(long /*int*/ context);
/** @method flags=dynamic */
public static final native long /*int*/ TISCopyCurrentKeyboardInputSource();
/** @method flags=dynamic
    @param inputSource cast=(TISInputSourceRef)
    @param propertyKey cast=(CFStringRef) */
public static final native long /*int*/ TISGetInputSourceProperty (long /*int*/ inputSource, long /*int*/ propertyKey);
/** @method flags=no_gen */
public static final native long /*int*/ kTISPropertyUnicodeKeyLayoutData();
/**
 * @method flags=dynamic
 * @param inMode cast=(UInt32)
 * @param inOptions cast=(UInt32)
 */
public static final native int SetSystemUIMode(int inMode, int inOptions);
/**
 * @method flags=dynamic
 * @param outMode cast=(UInt32*)
 * @param outOptions cast=(UInt32*)
 */
public static final native int GetSystemUIMode(int[] outMode, int[] outOptions);
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
public static final native int UCKeyTranslate (long /*int*/ keyLayoutPtr, short virtualKeyCode, short keyAction, int modifierKeyState, int keyboardType, int keyTranslateOptions, int[] deadKeyState, int maxStringLength, int[] actualStringLength, char[] unicodeString);
/**
 * @param inUTI1 cast=(CFStringRef)
 * @param inUTI2 cast=(CFStringRef)
 */
public static final native boolean UTTypeEqual(long /*int*/ inUTI1, long /*int*/ inUTI2);

/**
 * @method flags=dynamic
 * @param metric cast=(SInt32 *)
*/
public static final native void GetThemeMetric(int themeConstant, int[] metric);
/**
 * @method flags=dynamic
 * @param inContext cast=(CGContextRef)
*/
public static final native int HIThemeDrawFocusRect(CGRect inRect, boolean inHasFocus, long /*int*/ inContext, int inOrientation);

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
public static final native int ATSFontActivateFromFileReference(byte[] iFile, int iContext, int iFormat, long /*int*/ iReserved, int iOptions, long /*int*/ [] oContainer);

public static final int kATSFontContextLocal = 2;
public static final int kATSOptionFlagsDefault = 0;
public static final int kATSFontFormatUnspecified = 0;

/** @method flags=dynamic
 * @param path cast=(const UInt8 *)
 * @param ref cast=(FSRef *)
 * @param isDirectory cast=(Boolean *)
 */
public static final native int FSPathMakeRef (long /*int*/ path, byte[] ref, boolean[] isDirectory);

/** @method flags=dynamic */
public static final native byte LMGetKbdType();

/** @method flags=dynamic */
public static final native long /*int*/ AcquireRootMenu ();
/** @method flags=dynamic */
public static final native int CancelMenuTracking (long /*int*/ inRootMenu, boolean inImmediate, int inDismissalReason);
/**
 * @param inType cast=(OSType)
 * @param inCreator cast=(OSType)
 * @param inExtension cast=(CFStringRef)
 * @param inRoleMask cast=(LSRolesMask)
 * @param outAppRef cast=(FSRef *)
 * @param outAppURL cast=(CFURLRef *)
 */
public static final native long /*int*/ LSGetApplicationForInfo(int inType, int inCreator,long /*int*/ inExtension, int inRoleMask, byte[] outAppRef, int[] outAppURL);
/** @method flags=dynamic
 * @param mHandle cast=(MenuRef)
 * @param commandId cast=(MenuCommand)
 * @param index cast=(UInt32)
 * @param outMenu cast=(MenuRef *)
 * @param outIndex cast=(MenuItemIndex *)
 */
public static final native int GetIndMenuItemWithCommandID(long /*int*/ mHandle, int commandId, int index, long /*int*/ [] outMenu, short[] outIndex);
/** @method flags=dynamic
 * @param mHandle cast=(MenuRef)
 * @param index cast=(short)
 */
public static final native void DeleteMenuItem(long /*int*/ mHandle, short index);
/** @method flags=dynamic
 * @param pmSessionInfo cast=(PMPrintSession)
 * @param outPMPrinter cast=(PMPrinter *)
 */
public static final native long /*int*/ PMSessionGetCurrentPrinter(long /*int*/ pmSessionInfo, long /*int*/[] outPMPrinter);
/** @method flags=dynamic
 * @param pmSessionInfo cast=(PMPrintSession)
 * @param pmPrintSettings cast=(PMPrintSettings)
 */
public static final native long /*int*/ PMSessionGetDestinationType(long /*int*/ pmSessionInfo, long /*int*/ pmPrintSettings, short[] outDestinationType);
/** @method flags=dynamic
 * @param printSettings cast=(PMPrintSettings)
 * @param outDuplexSetting cast=(PMDuplexMode *)
 */
public static final native long /*int*/ PMGetDuplex(long /*int*/ printSettings, int[] outDuplexSetting);
/** @method flags=dynamic
 * @param printSettings cast=(PMPrintSettings)
 * @param duplexSetting cast=(PMDuplexMode)
 */
public static final native long /*int*/ PMSetDuplex(long /*int*/ printSettings, int duplexSetting);
/** @method flags=dynamic
 * @param pmPrinter cast=(PMPrinter)
 * @param outNumResolutions cast=(UInt32 *)
 */
public static final native long /*int*/ PMPrinterGetPrinterResolutionCount(long /*int*/ pmPrinter, int[] outNumResolutions);
/** @method flags=dynamic
 * @param pmPrinter cast=(PMPrinter)
 * @param pmPrintSettings cast=(PMPrintSettings)
 * @param outResolution cast=(PMResolution *)
 */
public static final native long /*int*/ PMPrinterGetOutputResolution(long /*int*/ pmPrinter, long /*int*/ pmPrintSettings, PMResolution outResolution);
/** @method flags=dynamic
 * @param pmPrinter cast=(PMPrinter)
 * @param outResolution cast=(PMResolution *)
 */
public static final native long /*int*/ PMPrinterGetIndexedPrinterResolution(long /*int*/ pmPrinter, int index, PMResolution outResolution);

// Custom FindWindow implementation to avoid namespace collisions with Point.
/**
 * @method flags=no_gen
 * @param wHandle cast=(WindowRef *)
 */
public static final native long /*int*/ FindWindow (long /*int*/ h, long /*int*/ v, long /*int*/ [] wHandle);

/**
 * @method flags=dynamic
 * @param display cast=(CGDirectDisplayID)
 */
public static final native long /*int*/ CGDisplayBaseAddress(int display);
/**
 * @method flags=dynamic
 * @param display cast=(CGDirectDisplayID)
 */
public static final native long /*int*/ CGDisplayBitsPerPixel(int display);
/**
 * @method flags=dynamic
 * @param display cast=(CGDirectDisplayID)
 */
public static final native long /*int*/ CGDisplayBitsPerSample(int display);
/**
 * @method flags=dynamic
 * @param display cast=(CGDirectDisplayID)
 */
public static final native long /*int*/ CGDisplayBytesPerRow(int display);


/** C calls */

public static final native int getpid();

public static final native void call(long /*int*/ proc, long /*int*/ id, long /*int*/ sel);

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
public static final native void CGContextCopyWindowContentsToRect(long /*int*/ context, CGRect destRect, long /*int*/ contextID, long /*int*/ windowNumber, CGRect srcRect);

/**
 * @method flags=dynamic
 * @param displayID cast=(CGDirectDisplayID)
 */
public static final native long /*int*/ CGDisplayCreateImage(int displayID);

/** QuickDraw calls */

/** @method flags=dynamic */
public static final native long /*int*/ NewRgn();
/** @method flags=dynamic */
public static final native void RectRgn(long /*int*/ rgnHandle, short[] rect);
/** @method flags=dynamic */
public static final native void OpenRgn();
/** @method flags=dynamic */
public static final native void OffsetRgn(long /*int*/ rgnHandle, short dh, short dv);
/** @method flags=dynamic */
public static final native void MoveTo(short h, short v);
/** @method flags=dynamic */
public static final native void LineTo(short h, short v);
/** @method flags=dynamic */
public static final native void UnionRgn(long /*int*/ srcRgnA, long /*int*/ srcRgnB, long /*int*/ dstRgn);
/** @method flags=dynamic */
public static final native void CloseRgn(long /*int*/ dstRgn);
/** @method flags=dynamic */
public static final native void DisposeRgn(long /*int*/ rgnHandle);
/**
 * @method flags=dynamic
 * @param pt flags=struct,cast=(Point *)
 */
public static final native boolean PtInRgn(short[] pt, long /*int*/ rgnHandle);
/** @method flags=dynamic */
public static final native void GetRegionBounds(long /*int*/ rgnHandle, short[] bounds);
/** @method flags=dynamic */
public static final native void SectRgn(long /*int*/ srcRgnA, long /*int*/ srcRgnB, long /*int*/ dstRgn);
/** @method flags=dynamic */
public static final native boolean EmptyRgn(long /*int*/ rgnHandle);
/** @method flags=dynamic */
public static final native void DiffRgn(long /*int*/ srcRgnA, long /*int*/ srcRgnB, long /*int*/ dstRgn);
/** @method flags=dynamic */
public static final native boolean RectInRgn(short[] rect, long /*int*/ rgnHandle);
/** @method flags=dynamic */
public static final native int QDRegionToRects(long /*int*/ rgn, int dir, long /*int*/ proc, long /*int*/ userData);
/** @method flags=dynamic */
public static final native void CopyRgn(long /*int*/ srcRgnHandle, long /*int*/ dstRgnHandle);
/** @method flags=dynamic */
public static final native void SetRect(short[] r, short left, short top, short right, short bottom);
public static final int kQDParseRegionFromTop = (1 << 0);
public static final int kQDParseRegionFromBottom = (1 << 1);
public static final int kQDParseRegionFromLeft = (1 << 2);
public static final int kQDParseRegionFromRight = (1 << 3);
public static final int kQDParseRegionFromTopLeft = kQDParseRegionFromTop | kQDParseRegionFromLeft;
public static final int kQDRegionToRectsMsgParse = 2;

/**
 * @method flags=dynamic
 * @param inWindow cast=(WindowRef)
 */
public static final native int HIWindowGetCGWindowID(long /*int*/ inWindow);

/** JavaScriptCore calls */

/**
 * @param ctx cast=(JSContextRef)
 * @param script cast=(JSStringRef)
 * @param thisObject cast=(JSObjectRef)
 * @param sourceURL cast=(JSStringRef)
 * @param exception cast=(JSValueRef *)
 */
public static final native long /*int*/ JSEvaluateScript (long /*int*/ ctx, long /*int*/ script, long /*int*/ thisObject, long /*int*/ sourceURL, int startingLineNumber, long /*int*/[] exception);

/**
 * @param string cast=(const char *)
 */
public static final native long /*int*/ JSStringCreateWithUTF8CString (byte[] string);

/**
 * @param string cast=(JSStringRef)
 */
public static final native void JSStringRelease (long /*int*/ string);


/** Certificate Security */

/**
 * @param certType cast=(CSSM_CERT_TYPE)
 * @param policyOID cast=(CSSM_OID *)
 * @param value cast=(CSSM_DATA *)
 * @param policySearch cast=(SecPolicySearchRef *)
 */
public static final native int SecPolicySearchCreate(long /*int*/ certType, long /*int*/ policyOID, long /*int*/ value, long /*int*/ [] policySearch);

/**
 * @param searchRef cast=(SecPolicySearchRef)
 * @param policyRef cast=(SecPolicyRef *)
 */
public static final native int SecPolicySearchCopyNext(long /*int*/ searchRef, long /*int*/ [] policyRef);

/**
 * @param certificates cast=(CFArrayRef)
 * @param policies cast=(CFTypeRef)
 * @param trustRef cast=(SecTrustRef *)
 */
public static final native int SecTrustCreateWithCertificates(long /*int*/ certificates, long /*int*/ policies, long /*int*/ [] trustRef);

public static final int CSSM_CERT_X_509v3 = 0x3;


/** Custom callbacks */

/** @method flags=no_gen */
public static final native long /*int*/ isFlipped_CALLBACK();

/** Custom structure return */

/** @method flags=no_gen */
public static final native void NSIntersectionRect (NSRect result, NSRect aRect, NSRect bRect);
/**
 * @method flags=no_gen
 * @param display cast=(CGDirectDisplayID)
 */
public static final native void CGDisplayBounds(int display, CGRect rect);

/** @method flags=const address*/
public static final native long /*int*/ kCFTypeDictionaryKeyCallBacks();
/** @method flags=const address*/
public static final native long /*int*/ kCFTypeDictionaryValueCallBacks();

/** @method flags=const */
public static final native long /*int*/ kUTTypeFileURL();
public static final NSString kUTTypeFileURL = new NSString(kUTTypeFileURL());
/** @method flags=const */
public static final native long /*int*/ kUTTypeURL();
public static final NSString kUTTypeURL = new NSString(kUTTypeURL());

/** Objective-C runtime */

/**
 * @param cls cast=(Class)
 * @param name cast=(const char *),flags=critical
 * @param types cast=(const char *),flags=critical
 */
public static final native boolean class_addIvar(long /*int*/ cls, byte[] name, long /*int*/ size, byte alignment, byte[] types);
/**
 * @param cls cast=(Class)
 * @param name cast=(SEL)
 * @param imp cast=(IMP)
 */
public static final native boolean class_addMethod(long /*int*/ cls, long /*int*/ name, long /*int*/ imp, String types);
/**
 * @param cls cast=(Class)
 * @param protocol cast=(Protocol *)
 */
public static final native boolean class_addProtocol(long /*int*/ cls, long /*int*/ protocol);
/**
 * @param aClass cast=(Class)
 * @param aSelector cast=(SEL)
 */
public static final native long /*int*/ class_getClassMethod(long /*int*/ aClass, long /*int*/ aSelector);
/**
 * @param cls cast=(Class)
 * @param name cast=(SEL)
 */
public static final native long /*int*/ class_getMethodImplementation(long /*int*/ cls, long /*int*/ name);
/**
 * @param cls cast=(Class)
 * @param name cast=(SEL)
 */
public static final native long /*int*/ class_getInstanceMethod(long /*int*/ cls, long /*int*/ name);
/** @param cls cast=(Class) */
public static final native long /*int*/ class_getSuperclass(long /*int*/ cls);
/**
 * @param method cast=(Method)
 * @param imp cast=(IMP)
 */
public static final native long /*int*/ method_setImplementation(long /*int*/ method, long /*int*/ imp);
/**
 * @param sel cast=(SEL)
 */
public static final native long /*int*/ sel_getName(long /*int*/ sel);
/**
 * @param cls cast=(Class)
 * @param extraBytes cast=(size_t)
 */
public static final native long /*int*/ class_createInstance(long /*int*/ cls, long /*int*/ extraBytes);

/** @method flags=no_gen */
public static final native String class_getName(long /*int*/ cls);
/** @method flags=dynamic */
public static final native void instrumentObjcMessageSends(boolean val);
/** @param superclass cast=(Class) */
public static final native long /*int*/ objc_allocateClassPair(long /*int*/ superclass, String name, long /*int*/ extraBytes);
/** @param klass cast=(Class) */
public static final native void objc_disposeClassPair(long /*int*/ klass);
public static final native long /*int*/ objc_getClass(String className);
public static final native long /*int*/ objc_getMetaClass(String name);
public static final native long /*int*/ objc_getProtocol(String name);
public static final native long /*int*/ objc_lookUpClass(String className);
/** @param cls cast=(Class) */
public static final native void objc_registerClassPair(long /*int*/ cls);
/** @param obj cast=(id) */
public static final native long /*int*/ object_getClassName(long /*int*/ obj);
/** @param obj cast=(id) */
public static final native long /*int*/ object_getClass(long /*int*/ obj);

/**
 * @param obj cast=(id)
 * @param name cast=(const char*),flags=critical
 * @param outValue cast=(void **),flags=critical
 */
public static final native long /*int*/ object_getInstanceVariable(long /*int*/ obj, byte[] name, long /*int*/ [] outValue);
/**
 * @param obj cast=(id)
 * @param name cast=(const char*),flags=critical
 * @param value cast=(void *),flags=critical
 */
public static final native long /*int*/ object_setInstanceVariable(long /*int*/ obj, byte[] name, long /*int*/ value);
/**
 * @param obj cast=(id)
 * @param clazz cast=(Class)
 */
public static final native long /*int*/ object_setClass(long /*int*/ obj, long /*int*/ clazz);
public static final native long /*int*/ sel_registerName(String selectorName);
public static final native int objc_super_sizeof();

/**
 * @method flags=cast
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSSize result, long /*int*/ id, long /*int*/ sel, NSSize arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3, long /*int*/ arg4, long /*int*/ arg5);

/** This section is auto generated */

/** Custom callbacks */
/** @method callback_types=id;id;SEL;NSPoint;,callback_flags=none;none;none;struct; */
public static final native long /*int*/ CALLBACK_accessibilityHitTest_(long /*int*/ func);
/** @method callback_types=NSAttributedString*;id;SEL;NSRange;,callback_flags=none;none;none;struct; */
public static final native long /*int*/ CALLBACK_attributedSubstringFromRange_(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSRect;NSBitmapImageRep*;,callback_flags=none;none;none;struct;none; */
public static final native long /*int*/ CALLBACK_cacheDisplayInRect_toBitmapImageRep_(long /*int*/ func);
/** @method callback_types=BOOL;id;SEL;NSIndexSet*;NSPoint;,callback_flags=none;none;none;none;struct; */
public static final native long /*int*/ CALLBACK_canDragRowsWithIndexes_atPoint_(long /*int*/ func);
/** @method callback_types=NSSize;id;SEL;,callback_flags=struct;none;none; */
public static final native long /*int*/ CALLBACK_cellSize(long /*int*/ func);
/** @method callback_types=NSSize;id;SEL;NSRect;,callback_flags=struct;none;none;struct; */
public static final native long /*int*/ CALLBACK_cellSizeForBounds_(long /*int*/ func);
/** @method callback_types=NSUInteger;id;SEL;NSPoint;,callback_flags=none;none;none;struct; */
public static final native long /*int*/ CALLBACK_characterIndexForPoint_(long /*int*/ func);
/** @method callback_types=NSInteger;id;SEL;NSPoint;,callback_flags=none;none;none;struct; */
public static final native long /*int*/ CALLBACK_columnAtPoint_(long /*int*/ func);
/** @method callback_types=BOOL;id;SEL;NSEvent*;NSSize;BOOL;,callback_flags=none;none;none;none;struct;none; */
public static final native long /*int*/ CALLBACK_dragSelectionWithEvent_offset_slideBack_(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSImage*;NSPoint;,callback_flags=none;none;none;none;struct; */
public static final native long /*int*/ CALLBACK_draggedImage_beganAt_(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSImage*;NSPoint;NSDragOperation;,callback_flags=none;none;none;none;struct;none; */
public static final native long /*int*/ CALLBACK_draggedImage_endedAt_operation_(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSRect;,callback_flags=none;none;none;struct; */
public static final native long /*int*/ CALLBACK_drawBackgroundInClipRect_(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSRect;NSView*;,callback_flags=none;none;none;struct;none; */
public static final native long /*int*/ CALLBACK_drawBezelWithFrame_inView_(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSImage*;NSRect;NSView*;,callback_flags=none;none;none;none;struct;none; */
public static final native long /*int*/ CALLBACK_drawImage_withFrame_inView_(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSRect;NSView*;,callback_flags=none;none;none;struct;none; */
public static final native long /*int*/ CALLBACK_drawInteriorWithFrame_inView_(long /*int*/ func);
/** @method callback_types=void;id;SEL;BOOL;NSRect;,callback_flags=none;none;none;none;struct; */
public static final native long /*int*/ CALLBACK_drawLabel_inRect_(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSRect;,callback_flags=none;none;none;struct; */
public static final native long /*int*/ CALLBACK_drawRect_(long /*int*/ func);
/** @method callback_types=NSRect;id;SEL;NSAttributedString*;NSRect;NSView*;,callback_flags=struct;none;none;none;struct;none; */
public static final native long /*int*/ CALLBACK_drawTitle_withFrame_inView_(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSRect;,callback_flags=none;none;none;struct; */
public static final native long /*int*/ CALLBACK_drawViewBackgroundInRect_(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSRect;NSView*;,callback_flags=none;none;none;struct;none; */
public static final native long /*int*/ CALLBACK_drawWithExpansionFrame_inView_(long /*int*/ func);
/** @method callback_types=NSRect;id;SEL;NSRect;NSView*;,callback_flags=struct;none;none;struct;none; */
public static final native long /*int*/ CALLBACK_expansionFrameWithFrame_inView_(long /*int*/ func);
/** @method callback_types=NSRect;id;SEL;NSRange;,callback_flags=struct;none;none;struct; */
public static final native long /*int*/ CALLBACK_firstRectForCharacterRange_(long /*int*/ func);
/** @method callback_types=NSRect;id;SEL;NSRect;NSView*;,callback_flags=struct;none;none;struct;none; */
public static final native long /*int*/ CALLBACK_focusRingMaskBoundsForFrame_inView_(long /*int*/ func);
/** @method callback_types=NSRect;id;SEL;NSInteger;,callback_flags=struct;none;none;none; */
public static final native long /*int*/ CALLBACK_headerRectOfColumn_(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSRect;,callback_flags=none;none;none;struct; */
public static final native long /*int*/ CALLBACK_highlightSelectionInClipRect_(long /*int*/ func);
/** @method callback_types=NSView*;id;SEL;NSPoint;,callback_flags=none;none;none;struct; */
public static final native long /*int*/ CALLBACK_hitTest_(long /*int*/ func);
/** @method callback_types=NSCellHitResult;id;SEL;NSEvent*;NSRect;NSView*;,callback_flags=none;none;none;none;struct;none; */
public static final native long /*int*/ CALLBACK_hitTestForEvent_inRect_ofView_(long /*int*/ func);
/** @method callback_types=NSRect;id;SEL;NSRect;,callback_flags=struct;none;none;struct; */
public static final native long /*int*/ CALLBACK_imageRectForBounds_(long /*int*/ func);
/** @method callback_types=NSRange;id;SEL;,callback_flags=struct;none;none; */
public static final native long /*int*/ CALLBACK_markedRange(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSClipView*;NSPoint;,callback_flags=none;none;none;none;struct; */
public static final native long /*int*/ CALLBACK_scrollClipView_toPoint_(long /*int*/ func);
/** @method callback_types=NSRange;id;SEL;,callback_flags=struct;none;none; */
public static final native long /*int*/ CALLBACK_selectedRange(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSRect;,callback_flags=none;none;none;struct; */
public static final native long /*int*/ CALLBACK_setFrame_(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSPoint;,callback_flags=none;none;none;struct; */
public static final native long /*int*/ CALLBACK_setFrameOrigin_(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSSize;,callback_flags=none;none;none;struct; */
public static final native long /*int*/ CALLBACK_setFrameSize_(long /*int*/ func);
/** @method callback_types=void;id;SEL;id;NSRange;,callback_flags=none;none;none;none;struct; */
public static final native long /*int*/ CALLBACK_setMarkedText_selectedRange_(long /*int*/ func);
/** @method callback_types=void;id;SEL;NSRect;,callback_flags=none;none;none;struct; */
public static final native long /*int*/ CALLBACK_setNeedsDisplayInRect_(long /*int*/ func);
/** @method callback_types=BOOL;id;SEL;NSRange;NSString*;,callback_flags=none;none;none;struct;none; */
public static final native long /*int*/ CALLBACK_shouldChangeTextInRange_replacementString_(long /*int*/ func);
/** @method callback_types=NSSize;id;SEL;BOOL;,callback_flags=struct;none;none;none; */
public static final native long /*int*/ CALLBACK_sizeOfLabel_(long /*int*/ func);
/** @method callback_types=NSRange;id;SEL;NSTextView*;NSRange;NSRange;,callback_flags=struct;none;none;none;struct;struct; */
public static final native long /*int*/ CALLBACK_textView_willChangeSelectionFromCharacterRange_toCharacterRange_(long /*int*/ func);
/** @method callback_types=NSRect;id;SEL;NSRect;,callback_flags=struct;none;none;struct; */
public static final native long /*int*/ CALLBACK_titleRectForBounds_(long /*int*/ func);
/** @method callback_types=NSString*;id;SEL;NSView*;NSToolTipTag;NSPoint;void*;,callback_flags=none;none;none;none;none;struct;none; */
public static final native long /*int*/ CALLBACK_view_stringForToolTip_point_userData_(long /*int*/ func);
/** @method callback_types=void;id;SEL;WebView*;NSRect;,callback_flags=none;none;none;none;struct; */
public static final native long /*int*/ CALLBACK_webView_setFrame_(long /*int*/ func);

/** Classes */
public static final long /*int*/ class_DOMDocument = objc_getClass("DOMDocument");
public static final long /*int*/ class_DOMEvent = objc_getClass("DOMEvent");
public static final long /*int*/ class_DOMKeyboardEvent = objc_getClass("DOMKeyboardEvent");
public static final long /*int*/ class_DOMMouseEvent = objc_getClass("DOMMouseEvent");
public static final long /*int*/ class_DOMUIEvent = objc_getClass("DOMUIEvent");
public static final long /*int*/ class_DOMWheelEvent = objc_getClass("DOMWheelEvent");
public static final long /*int*/ class_NSActionCell = objc_getClass("NSActionCell");
public static final long /*int*/ class_NSAffineTransform = objc_getClass("NSAffineTransform");
public static final long /*int*/ class_NSAlert = objc_getClass("NSAlert");
public static final long /*int*/ class_NSAppearance = objc_getClass("NSAppearance");
public static final long /*int*/ class_NSAppleEventDescriptor = objc_getClass("NSAppleEventDescriptor");
public static final long /*int*/ class_NSApplication = objc_getClass("NSApplication");
public static final long /*int*/ class_NSArray = objc_getClass("NSArray");
public static final long /*int*/ class_NSAssertionHandler = objc_getClass("NSAssertionHandler");
public static final long /*int*/ class_NSAttributedString = objc_getClass("NSAttributedString");
public static final long /*int*/ class_NSAutoreleasePool = objc_getClass("NSAutoreleasePool");
public static final long /*int*/ class_NSBezierPath = objc_getClass("NSBezierPath");
public static final long /*int*/ class_NSBitmapImageRep = objc_getClass("NSBitmapImageRep");
public static final long /*int*/ class_NSBox = objc_getClass("NSBox");
public static final long /*int*/ class_NSBrowserCell = objc_getClass("NSBrowserCell");
public static final long /*int*/ class_NSBundle = objc_getClass("NSBundle");
public static final long /*int*/ class_NSButton = objc_getClass("NSButton");
public static final long /*int*/ class_NSButtonCell = objc_getClass("NSButtonCell");
public static final long /*int*/ class_NSCalendarDate = objc_getClass("NSCalendarDate");
public static final long /*int*/ class_NSCell = objc_getClass("NSCell");
public static final long /*int*/ class_NSCharacterSet = objc_getClass("NSCharacterSet");
public static final long /*int*/ class_NSClipView = objc_getClass("NSClipView");
public static final long /*int*/ class_NSCoder = objc_getClass("NSCoder");
public static final long /*int*/ class_NSColor = objc_getClass("NSColor");
public static final long /*int*/ class_NSColorList = objc_getClass("NSColorList");
public static final long /*int*/ class_NSColorPanel = objc_getClass("NSColorPanel");
public static final long /*int*/ class_NSColorSpace = objc_getClass("NSColorSpace");
public static final long /*int*/ class_NSComboBox = objc_getClass("NSComboBox");
public static final long /*int*/ class_NSComboBoxCell = objc_getClass("NSComboBoxCell");
public static final long /*int*/ class_NSControl = objc_getClass("NSControl");
public static final long /*int*/ class_NSCursor = objc_getClass("NSCursor");
public static final long /*int*/ class_NSData = objc_getClass("NSData");
public static final long /*int*/ class_NSDate = objc_getClass("NSDate");
public static final long /*int*/ class_NSDatePicker = objc_getClass("NSDatePicker");
public static final long /*int*/ class_NSDictionary = objc_getClass("NSDictionary");
public static final long /*int*/ class_NSDirectoryEnumerator = objc_getClass("NSDirectoryEnumerator");
public static final long /*int*/ class_NSDockTile = objc_getClass("NSDockTile");
public static final long /*int*/ class_NSEnumerator = objc_getClass("NSEnumerator");
public static final long /*int*/ class_NSError = objc_getClass("NSError");
public static final long /*int*/ class_NSEvent = objc_getClass("NSEvent");
public static final long /*int*/ class_NSFileManager = objc_getClass("NSFileManager");
public static final long /*int*/ class_NSFileWrapper = objc_getClass("NSFileWrapper");
public static final long /*int*/ class_NSFont = objc_getClass("NSFont");
public static final long /*int*/ class_NSFontManager = objc_getClass("NSFontManager");
public static final long /*int*/ class_NSFontPanel = objc_getClass("NSFontPanel");
public static final long /*int*/ class_NSFormatter = objc_getClass("NSFormatter");
public static final long /*int*/ class_NSGradient = objc_getClass("NSGradient");
public static final long /*int*/ class_NSGraphicsContext = objc_getClass("NSGraphicsContext");
public static final long /*int*/ class_NSHTTPCookie = objc_getClass("NSHTTPCookie");
public static final long /*int*/ class_NSHTTPCookieStorage = objc_getClass("NSHTTPCookieStorage");
public static final long /*int*/ class_NSImage = objc_getClass("NSImage");
public static final long /*int*/ class_NSImageRep = objc_getClass("NSImageRep");
public static final long /*int*/ class_NSImageView = objc_getClass("NSImageView");
public static final long /*int*/ class_NSIndexSet = objc_getClass("NSIndexSet");
public static final long /*int*/ class_NSInputManager = objc_getClass("NSInputManager");
public static final long /*int*/ class_NSKeyedArchiver = objc_getClass("NSKeyedArchiver");
public static final long /*int*/ class_NSKeyedUnarchiver = objc_getClass("NSKeyedUnarchiver");
public static final long /*int*/ class_NSLayoutManager = objc_getClass("NSLayoutManager");
public static final long /*int*/ class_NSLocale = objc_getClass("NSLocale");
public static final long /*int*/ class_NSMenu = objc_getClass("NSMenu");
public static final long /*int*/ class_NSMenuItem = objc_getClass("NSMenuItem");
public static final long /*int*/ class_NSMutableArray = objc_getClass("NSMutableArray");
public static final long /*int*/ class_NSMutableAttributedString = objc_getClass("NSMutableAttributedString");
public static final long /*int*/ class_NSMutableDictionary = objc_getClass("NSMutableDictionary");
public static final long /*int*/ class_NSMutableIndexSet = objc_getClass("NSMutableIndexSet");
public static final long /*int*/ class_NSMutableParagraphStyle = objc_getClass("NSMutableParagraphStyle");
public static final long /*int*/ class_NSMutableSet = objc_getClass("NSMutableSet");
public static final long /*int*/ class_NSMutableString = objc_getClass("NSMutableString");
public static final long /*int*/ class_NSMutableURLRequest = objc_getClass("NSMutableURLRequest");
public static final long /*int*/ class_NSNotification = objc_getClass("NSNotification");
public static final long /*int*/ class_NSNotificationCenter = objc_getClass("NSNotificationCenter");
public static final long /*int*/ class_NSNumber = objc_getClass("NSNumber");
public static final long /*int*/ class_NSNumberFormatter = objc_getClass("NSNumberFormatter");
public static final long /*int*/ class_NSObject = objc_getClass("NSObject");
public static final long /*int*/ class_NSOpenGLContext = objc_getClass("NSOpenGLContext");
public static final long /*int*/ class_NSOpenGLPixelFormat = objc_getClass("NSOpenGLPixelFormat");
public static final long /*int*/ class_NSOpenPanel = objc_getClass("NSOpenPanel");
public static final long /*int*/ class_NSOutlineView = objc_getClass("NSOutlineView");
public static final long /*int*/ class_NSPanel = objc_getClass("NSPanel");
public static final long /*int*/ class_NSParagraphStyle = objc_getClass("NSParagraphStyle");
public static final long /*int*/ class_NSPasteboard = objc_getClass("NSPasteboard");
public static final long /*int*/ class_NSPopUpButton = objc_getClass("NSPopUpButton");
public static final long /*int*/ class_NSPrintInfo = objc_getClass("NSPrintInfo");
public static final long /*int*/ class_NSPrintOperation = objc_getClass("NSPrintOperation");
public static final long /*int*/ class_NSPrintPanel = objc_getClass("NSPrintPanel");
public static final long /*int*/ class_NSPrinter = objc_getClass("NSPrinter");
public static final long /*int*/ class_NSProgressIndicator = objc_getClass("NSProgressIndicator");
public static final long /*int*/ class_NSResponder = objc_getClass("NSResponder");
public static final long /*int*/ class_NSRunLoop = objc_getClass("NSRunLoop");
public static final long /*int*/ class_NSRunningApplication = objc_getClass("NSRunningApplication");
public static final long /*int*/ class_NSSavePanel = objc_getClass("NSSavePanel");
public static final long /*int*/ class_NSScreen = objc_getClass("NSScreen");
public static final long /*int*/ class_NSScrollView = objc_getClass("NSScrollView");
public static final long /*int*/ class_NSScroller = objc_getClass("NSScroller");
public static final long /*int*/ class_NSSearchField = objc_getClass("NSSearchField");
public static final long /*int*/ class_NSSearchFieldCell = objc_getClass("NSSearchFieldCell");
public static final long /*int*/ class_NSSecureTextField = objc_getClass("NSSecureTextField");
public static final long /*int*/ class_NSSegmentedCell = objc_getClass("NSSegmentedCell");
public static final long /*int*/ class_NSSet = objc_getClass("NSSet");
public static final long /*int*/ class_NSSlider = objc_getClass("NSSlider");
public static final long /*int*/ class_NSStatusBar = objc_getClass("NSStatusBar");
public static final long /*int*/ class_NSStatusItem = objc_getClass("NSStatusItem");
public static final long /*int*/ class_NSStepper = objc_getClass("NSStepper");
public static final long /*int*/ class_NSString = objc_getClass("NSString");
public static final long /*int*/ class_NSTabView = objc_getClass("NSTabView");
public static final long /*int*/ class_NSTabViewItem = objc_getClass("NSTabViewItem");
public static final long /*int*/ class_NSTableColumn = objc_getClass("NSTableColumn");
public static final long /*int*/ class_NSTableHeaderCell = objc_getClass("NSTableHeaderCell");
public static final long /*int*/ class_NSTableHeaderView = objc_getClass("NSTableHeaderView");
public static final long /*int*/ class_NSTableView = objc_getClass("NSTableView");
public static final long /*int*/ class_NSText = objc_getClass("NSText");
public static final long /*int*/ class_NSTextAttachment = objc_getClass("NSTextAttachment");
public static final long /*int*/ class_NSTextContainer = objc_getClass("NSTextContainer");
public static final long /*int*/ class_NSTextField = objc_getClass("NSTextField");
public static final long /*int*/ class_NSTextFieldCell = objc_getClass("NSTextFieldCell");
public static final long /*int*/ class_NSTextStorage = objc_getClass("NSTextStorage");
public static final long /*int*/ class_NSTextTab = objc_getClass("NSTextTab");
public static final long /*int*/ class_NSTextView = objc_getClass("NSTextView");
public static final long /*int*/ class_NSThread = objc_getClass("NSThread");
public static final long /*int*/ class_NSTimeZone = objc_getClass("NSTimeZone");
public static final long /*int*/ class_NSTimer = objc_getClass("NSTimer");
public static final long /*int*/ class_NSToolbar = objc_getClass("NSToolbar");
public static final long /*int*/ class_NSToolbarItem = objc_getClass("NSToolbarItem");
public static final long /*int*/ class_NSTouch = objc_getClass("NSTouch");
public static final long /*int*/ class_NSTrackingArea = objc_getClass("NSTrackingArea");
public static final long /*int*/ class_NSTypesetter = objc_getClass("NSTypesetter");
public static final long /*int*/ class_NSURL = objc_getClass("NSURL");
public static final long /*int*/ class_NSURLAuthenticationChallenge = objc_getClass("NSURLAuthenticationChallenge");
public static final long /*int*/ class_NSURLCredential = objc_getClass("NSURLCredential");
public static final long /*int*/ class_NSURLDownload = objc_getClass("NSURLDownload");
public static final long /*int*/ class_NSURLProtectionSpace = objc_getClass("NSURLProtectionSpace");
public static final long /*int*/ class_NSURLRequest = objc_getClass("NSURLRequest");
public static final long /*int*/ class_NSUndoManager = objc_getClass("NSUndoManager");
public static final long /*int*/ class_NSUserDefaults = objc_getClass("NSUserDefaults");
public static final long /*int*/ class_NSValue = objc_getClass("NSValue");
public static final long /*int*/ class_NSView = objc_getClass("NSView");
public static final long /*int*/ class_NSWindow = objc_getClass("NSWindow");
public static final long /*int*/ class_NSWorkspace = objc_getClass("NSWorkspace");
public static final long /*int*/ class_SFCertificatePanel = objc_getClass("SFCertificatePanel");
public static final long /*int*/ class_SFCertificateTrustPanel = objc_getClass("SFCertificateTrustPanel");
public static final long /*int*/ class_WebDataSource = objc_getClass("WebDataSource");
public static final long /*int*/ class_WebFrame = objc_getClass("WebFrame");
public static final long /*int*/ class_WebFrameView = objc_getClass("WebFrameView");
public static final long /*int*/ class_WebPreferences = objc_getClass("WebPreferences");
public static final long /*int*/ class_WebScriptObject = objc_getClass("WebScriptObject");
public static final long /*int*/ class_WebUndefined = objc_getClass("WebUndefined");
public static final long /*int*/ class_WebView = objc_getClass("WebView");

/** Protocols */
public static final long /*int*/ protocol_NSAccessibility = objc_getProtocol("NSAccessibility");
public static final long /*int*/ protocol_NSAccessibilityAdditions = objc_getProtocol("NSAccessibilityAdditions");
public static final long /*int*/ protocol_NSAppearanceCustomization = objc_getProtocol("NSAppearanceCustomization");
public static final long /*int*/ protocol_NSApplicationDelegate = objc_getProtocol("NSApplicationDelegate");
public static final long /*int*/ protocol_NSApplicationNotifications = objc_getProtocol("NSApplicationNotifications");
public static final long /*int*/ protocol_NSColorPanelResponderMethod = objc_getProtocol("NSColorPanelResponderMethod");
public static final long /*int*/ protocol_NSComboBoxNotifications = objc_getProtocol("NSComboBoxNotifications");
public static final long /*int*/ protocol_NSDraggingDestination = objc_getProtocol("NSDraggingDestination");
public static final long /*int*/ protocol_NSDraggingSource = objc_getProtocol("NSDraggingSource");
public static final long /*int*/ protocol_NSFontManagerResponderMethod = objc_getProtocol("NSFontManagerResponderMethod");
public static final long /*int*/ protocol_NSFontPanelValidationAdditions = objc_getProtocol("NSFontPanelValidationAdditions");
public static final long /*int*/ protocol_NSMenuDelegate = objc_getProtocol("NSMenuDelegate");
public static final long /*int*/ protocol_NSMenuValidation = objc_getProtocol("NSMenuValidation");
public static final long /*int*/ protocol_NSOutlineViewDataSource = objc_getProtocol("NSOutlineViewDataSource");
public static final long /*int*/ protocol_NSOutlineViewDelegate = objc_getProtocol("NSOutlineViewDelegate");
public static final long /*int*/ protocol_NSOutlineViewNotifications = objc_getProtocol("NSOutlineViewNotifications");
public static final long /*int*/ protocol_NSPasteboardOwner = objc_getProtocol("NSPasteboardOwner");
public static final long /*int*/ protocol_NSSavePanelDelegate = objc_getProtocol("NSSavePanelDelegate");
public static final long /*int*/ protocol_NSTabViewDelegate = objc_getProtocol("NSTabViewDelegate");
public static final long /*int*/ protocol_NSTableDataSource = objc_getProtocol("NSTableDataSource");
public static final long /*int*/ protocol_NSTableViewDelegate = objc_getProtocol("NSTableViewDelegate");
public static final long /*int*/ protocol_NSTableViewNotifications = objc_getProtocol("NSTableViewNotifications");
public static final long /*int*/ protocol_NSTextAttachmentCell = objc_getProtocol("NSTextAttachmentCell");
public static final long /*int*/ protocol_NSTextDelegate = objc_getProtocol("NSTextDelegate");
public static final long /*int*/ protocol_NSTextInput = objc_getProtocol("NSTextInput");
public static final long /*int*/ protocol_NSTextViewDelegate = objc_getProtocol("NSTextViewDelegate");
public static final long /*int*/ protocol_NSToolTipOwner = objc_getProtocol("NSToolTipOwner");
public static final long /*int*/ protocol_NSToolbarDelegate = objc_getProtocol("NSToolbarDelegate");
public static final long /*int*/ protocol_NSToolbarNotifications = objc_getProtocol("NSToolbarNotifications");
public static final long /*int*/ protocol_NSURLDownloadDelegate = objc_getProtocol("NSURLDownloadDelegate");
public static final long /*int*/ protocol_NSWindowDelegate = objc_getProtocol("NSWindowDelegate");
public static final long /*int*/ protocol_NSWindowNotifications = objc_getProtocol("NSWindowNotifications");
public static final long /*int*/ protocol_WebDocumentRepresentation = objc_getProtocol("WebDocumentRepresentation");
public static final long /*int*/ protocol_WebFrameLoadDelegate = objc_getProtocol("WebFrameLoadDelegate");
public static final long /*int*/ protocol_WebOpenPanelResultListener = objc_getProtocol("WebOpenPanelResultListener");
public static final long /*int*/ protocol_WebPolicyDecisionListener = objc_getProtocol("WebPolicyDecisionListener");
public static final long /*int*/ protocol_WebPolicyDelegate = objc_getProtocol("WebPolicyDelegate");
public static final long /*int*/ protocol_WebResourceLoadDelegate = objc_getProtocol("WebResourceLoadDelegate");
public static final long /*int*/ protocol_WebUIDelegate = objc_getProtocol("WebUIDelegate");

/** Selectors */
private static java.util.Map<Long,Selector> SELECTORS;
public static void registerSelector (Long value, Selector selector) {
	if (SELECTORS == null) {
		SELECTORS = new java.util.HashMap<>();
	}
	SELECTORS.put(value, selector);
}
public static Selector getSelector (long value) {
	return SELECTORS.get(value);
}
public static final long /*int*/ sel_CGEvent = Selector.sel_CGEvent.value;
public static final long /*int*/ sel_DOMDocument = Selector.sel_DOMDocument.value;
public static final long /*int*/ sel_IBeamCursor = Selector.sel_IBeamCursor.value;
public static final long /*int*/ sel_PMPrintSession = Selector.sel_PMPrintSession.value;
public static final long /*int*/ sel_PMPrintSettings = Selector.sel_PMPrintSettings.value;
public static final long /*int*/ sel_TIFFRepresentation = Selector.sel_TIFFRepresentation.value;
public static final long /*int*/ sel_URL = Selector.sel_URL.value;
public static final long /*int*/ sel_URLFromPasteboard_ = Selector.sel_URLFromPasteboard_.value;
public static final long /*int*/ sel_URLWithString_ = Selector.sel_URLWithString_.value;
public static final long /*int*/ sel_URLsForDirectory_inDomains_ = Selector.sel_URLsForDirectory_inDomains_.value;
public static final long /*int*/ sel_UTF8String = Selector.sel_UTF8String.value;
public static final long /*int*/ sel_abortEditing = Selector.sel_abortEditing.value;
public static final long /*int*/ sel_absoluteString = Selector.sel_absoluteString.value;
public static final long /*int*/ sel_acceptsFirstMouse_ = Selector.sel_acceptsFirstMouse_.value;
public static final long /*int*/ sel_acceptsFirstResponder = Selector.sel_acceptsFirstResponder.value;
public static final long /*int*/ sel_accessibilityActionDescription_ = Selector.sel_accessibilityActionDescription_.value;
public static final long /*int*/ sel_accessibilityActionNames = Selector.sel_accessibilityActionNames.value;
public static final long /*int*/ sel_accessibilityAttributeNames = Selector.sel_accessibilityAttributeNames.value;
public static final long /*int*/ sel_accessibilityAttributeValue_ = Selector.sel_accessibilityAttributeValue_.value;
public static final long /*int*/ sel_accessibilityAttributeValue_forParameter_ = Selector.sel_accessibilityAttributeValue_forParameter_.value;
public static final long /*int*/ sel_accessibilityFocusedUIElement = Selector.sel_accessibilityFocusedUIElement.value;
public static final long /*int*/ sel_accessibilityHitTest_ = Selector.sel_accessibilityHitTest_.value;
public static final long /*int*/ sel_accessibilityIsAttributeSettable_ = Selector.sel_accessibilityIsAttributeSettable_.value;
public static final long /*int*/ sel_accessibilityIsIgnored = Selector.sel_accessibilityIsIgnored.value;
public static final long /*int*/ sel_accessibilityParameterizedAttributeNames = Selector.sel_accessibilityParameterizedAttributeNames.value;
public static final long /*int*/ sel_accessibilityPerformAction_ = Selector.sel_accessibilityPerformAction_.value;
public static final long /*int*/ sel_accessibilitySetOverrideValue_forAttribute_ = Selector.sel_accessibilitySetOverrideValue_forAttribute_.value;
public static final long /*int*/ sel_accessibilitySetValue_forAttribute_ = Selector.sel_accessibilitySetValue_forAttribute_.value;
public static final long /*int*/ sel_action = Selector.sel_action.value;
public static final long /*int*/ sel_activateIgnoringOtherApps_ = Selector.sel_activateIgnoringOtherApps_.value;
public static final long /*int*/ sel_activateWithOptions_ = Selector.sel_activateWithOptions_.value;
public static final long /*int*/ sel_addAttribute_value_range_ = Selector.sel_addAttribute_value_range_.value;
public static final long /*int*/ sel_addButtonWithTitle_ = Selector.sel_addButtonWithTitle_.value;
public static final long /*int*/ sel_addChildWindow_ordered_ = Selector.sel_addChildWindow_ordered_.value;
public static final long /*int*/ sel_addClip = Selector.sel_addClip.value;
public static final long /*int*/ sel_addEventListener_listener_useCapture_ = Selector.sel_addEventListener_listener_useCapture_.value;
public static final long /*int*/ sel_addIndex_ = Selector.sel_addIndex_.value;
public static final long /*int*/ sel_addItem_ = Selector.sel_addItem_.value;
public static final long /*int*/ sel_addItemWithObjectValue_ = Selector.sel_addItemWithObjectValue_.value;
public static final long /*int*/ sel_addItemWithTitle_action_keyEquivalent_ = Selector.sel_addItemWithTitle_action_keyEquivalent_.value;
public static final long /*int*/ sel_addLayoutManager_ = Selector.sel_addLayoutManager_.value;
public static final long /*int*/ sel_addObject_ = Selector.sel_addObject_.value;
public static final long /*int*/ sel_addObjectsFromArray_ = Selector.sel_addObjectsFromArray_.value;
public static final long /*int*/ sel_addObserver_selector_name_object_ = Selector.sel_addObserver_selector_name_object_.value;
public static final long /*int*/ sel_addRepresentation_ = Selector.sel_addRepresentation_.value;
public static final long /*int*/ sel_addSubview_ = Selector.sel_addSubview_.value;
public static final long /*int*/ sel_addSubview_positioned_relativeTo_ = Selector.sel_addSubview_positioned_relativeTo_.value;
public static final long /*int*/ sel_addTabStop_ = Selector.sel_addTabStop_.value;
public static final long /*int*/ sel_addTabViewItem_ = Selector.sel_addTabViewItem_.value;
public static final long /*int*/ sel_addTableColumn_ = Selector.sel_addTableColumn_.value;
public static final long /*int*/ sel_addTemporaryAttribute_value_forCharacterRange_ = Selector.sel_addTemporaryAttribute_value_forCharacterRange_.value;
public static final long /*int*/ sel_addTextContainer_ = Selector.sel_addTextContainer_.value;
public static final long /*int*/ sel_addTimer_forMode_ = Selector.sel_addTimer_forMode_.value;
public static final long /*int*/ sel_addToolTipRect_owner_userData_ = Selector.sel_addToolTipRect_owner_userData_.value;
public static final long /*int*/ sel_addTypes_owner_ = Selector.sel_addTypes_owner_.value;
public static final long /*int*/ sel_alignment = Selector.sel_alignment.value;
public static final long /*int*/ sel_allKeys = Selector.sel_allKeys.value;
public static final long /*int*/ sel_allObjects = Selector.sel_allObjects.value;
public static final long /*int*/ sel_alloc = Selector.sel_alloc.value;
public static final long /*int*/ sel_allowsColumnReordering = Selector.sel_allowsColumnReordering.value;
public static final long /*int*/ sel_allowsFloats = Selector.sel_allowsFloats.value;
public static final long /*int*/ sel_alphaComponent = Selector.sel_alphaComponent.value;
public static final long /*int*/ sel_alphaValue = Selector.sel_alphaValue.value;
public static final long /*int*/ sel_altKey = Selector.sel_altKey.value;
public static final long /*int*/ sel_alternateSelectedControlColor = Selector.sel_alternateSelectedControlColor.value;
public static final long /*int*/ sel_alternateSelectedControlTextColor = Selector.sel_alternateSelectedControlTextColor.value;
public static final long /*int*/ sel_alwaysShowsDecimalSeparator = Selector.sel_alwaysShowsDecimalSeparator.value;
public static final long /*int*/ sel_appearanceNamed_ = Selector.sel_appearanceNamed_.value;
public static final long /*int*/ sel_appendAttributedString_ = Selector.sel_appendAttributedString_.value;
public static final long /*int*/ sel_appendBezierPath_ = Selector.sel_appendBezierPath_.value;
public static final long /*int*/ sel_appendBezierPathWithArcWithCenter_radius_startAngle_endAngle_ = Selector.sel_appendBezierPathWithArcWithCenter_radius_startAngle_endAngle_.value;
public static final long /*int*/ sel_appendBezierPathWithArcWithCenter_radius_startAngle_endAngle_clockwise_ = Selector.sel_appendBezierPathWithArcWithCenter_radius_startAngle_endAngle_clockwise_.value;
public static final long /*int*/ sel_appendBezierPathWithGlyphs_count_inFont_ = Selector.sel_appendBezierPathWithGlyphs_count_inFont_.value;
public static final long /*int*/ sel_appendBezierPathWithOvalInRect_ = Selector.sel_appendBezierPathWithOvalInRect_.value;
public static final long /*int*/ sel_appendBezierPathWithRect_ = Selector.sel_appendBezierPathWithRect_.value;
public static final long /*int*/ sel_appendBezierPathWithRoundedRect_xRadius_yRadius_ = Selector.sel_appendBezierPathWithRoundedRect_xRadius_yRadius_.value;
public static final long /*int*/ sel_appendString_ = Selector.sel_appendString_.value;
public static final long /*int*/ sel_application_openFile_ = Selector.sel_application_openFile_.value;
public static final long /*int*/ sel_application_openFiles_ = Selector.sel_application_openFiles_.value;
public static final long /*int*/ sel_applicationDidBecomeActive_ = Selector.sel_applicationDidBecomeActive_.value;
public static final long /*int*/ sel_applicationDidFinishLaunching_ = Selector.sel_applicationDidFinishLaunching_.value;
public static final long /*int*/ sel_applicationDidResignActive_ = Selector.sel_applicationDidResignActive_.value;
public static final long /*int*/ sel_applicationDockMenu_ = Selector.sel_applicationDockMenu_.value;
public static final long /*int*/ sel_applicationIconImage = Selector.sel_applicationIconImage.value;
public static final long /*int*/ sel_applicationShouldHandleReopen_hasVisibleWindows_ = Selector.sel_applicationShouldHandleReopen_hasVisibleWindows_.value;
public static final long /*int*/ sel_applicationShouldTerminate_ = Selector.sel_applicationShouldTerminate_.value;
public static final long /*int*/ sel_applicationWillFinishLaunching_ = Selector.sel_applicationWillFinishLaunching_.value;
public static final long /*int*/ sel_applicationWillTerminate_ = Selector.sel_applicationWillTerminate_.value;
public static final long /*int*/ sel_archivedDataWithRootObject_ = Selector.sel_archivedDataWithRootObject_.value;
public static final long /*int*/ sel_areCursorRectsEnabled = Selector.sel_areCursorRectsEnabled.value;
public static final long /*int*/ sel_arrangeInFront_ = Selector.sel_arrangeInFront_.value;
public static final long /*int*/ sel_array = Selector.sel_array.value;
public static final long /*int*/ sel_arrayWithCapacity_ = Selector.sel_arrayWithCapacity_.value;
public static final long /*int*/ sel_arrayWithObject_ = Selector.sel_arrayWithObject_.value;
public static final long /*int*/ sel_arrowCursor = Selector.sel_arrowCursor.value;
public static final long /*int*/ sel_ascender = Selector.sel_ascender.value;
public static final long /*int*/ sel_attachColorList_ = Selector.sel_attachColorList_.value;
public static final long /*int*/ sel_attachment = Selector.sel_attachment.value;
public static final long /*int*/ sel_attribute_atIndex_effectiveRange_ = Selector.sel_attribute_atIndex_effectiveRange_.value;
public static final long /*int*/ sel_attributedStringValue = Selector.sel_attributedStringValue.value;
public static final long /*int*/ sel_attributedStringWithAttachment_ = Selector.sel_attributedStringWithAttachment_.value;
public static final long /*int*/ sel_attributedSubstringFromRange_ = Selector.sel_attributedSubstringFromRange_.value;
public static final long /*int*/ sel_attributedTitle = Selector.sel_attributedTitle.value;
public static final long /*int*/ sel_attributesAtIndex_longestEffectiveRange_inRange_ = Selector.sel_attributesAtIndex_longestEffectiveRange_inRange_.value;
public static final long /*int*/ sel_autorelease = Selector.sel_autorelease.value;
public static final long /*int*/ sel_availableFontFamilies = Selector.sel_availableFontFamilies.value;
public static final long /*int*/ sel_availableFonts = Selector.sel_availableFonts.value;
public static final long /*int*/ sel_availableMembersOfFontFamily_ = Selector.sel_availableMembersOfFontFamily_.value;
public static final long /*int*/ sel_availableTypeFromArray_ = Selector.sel_availableTypeFromArray_.value;
public static final long /*int*/ sel_backgroundColor = Selector.sel_backgroundColor.value;
public static final long /*int*/ sel_backingScaleFactor = Selector.sel_backingScaleFactor.value;
public static final long /*int*/ sel_badgeLabel = Selector.sel_badgeLabel.value;
public static final long /*int*/ sel_baselineOffsetInLayoutManager_glyphIndex_ = Selector.sel_baselineOffsetInLayoutManager_glyphIndex_.value;
public static final long /*int*/ sel_becomeFirstResponder = Selector.sel_becomeFirstResponder.value;
public static final long /*int*/ sel_becomeKeyWindow = Selector.sel_becomeKeyWindow.value;
public static final long /*int*/ sel_beginDocument = Selector.sel_beginDocument.value;
public static final long /*int*/ sel_beginEditing = Selector.sel_beginEditing.value;
public static final long /*int*/ sel_beginGestureWithEvent_ = Selector.sel_beginGestureWithEvent_.value;
public static final long /*int*/ sel_beginPageInRect_atPlacement_ = Selector.sel_beginPageInRect_atPlacement_.value;
public static final long /*int*/ sel_beginSheet_modalForWindow_modalDelegate_didEndSelector_contextInfo_ = Selector.sel_beginSheet_modalForWindow_modalDelegate_didEndSelector_contextInfo_.value;
public static final long /*int*/ sel_beginSheetForWindow_modalDelegate_didEndSelector_contextInfo_trust_message_ = Selector.sel_beginSheetForWindow_modalDelegate_didEndSelector_contextInfo_trust_message_.value;
public static final long /*int*/ sel_beginSheetModalForWindow_modalDelegate_didEndSelector_contextInfo_ = Selector.sel_beginSheetModalForWindow_modalDelegate_didEndSelector_contextInfo_.value;
public static final long /*int*/ sel_beginSheetWithPrintInfo_modalForWindow_delegate_didEndSelector_contextInfo_ = Selector.sel_beginSheetWithPrintInfo_modalForWindow_delegate_didEndSelector_contextInfo_.value;
public static final long /*int*/ sel_bestRepresentationForDevice_ = Selector.sel_bestRepresentationForDevice_.value;
public static final long /*int*/ sel_bezelStyle = Selector.sel_bezelStyle.value;
public static final long /*int*/ sel_bezierPath = Selector.sel_bezierPath.value;
public static final long /*int*/ sel_bezierPathByFlatteningPath = Selector.sel_bezierPathByFlatteningPath.value;
public static final long /*int*/ sel_bezierPathWithRect_ = Selector.sel_bezierPathWithRect_.value;
public static final long /*int*/ sel_bezierPathWithRoundedRect_xRadius_yRadius_ = Selector.sel_bezierPathWithRoundedRect_xRadius_yRadius_.value;
public static final long /*int*/ sel_bitmapData = Selector.sel_bitmapData.value;
public static final long /*int*/ sel_bitmapFormat = Selector.sel_bitmapFormat.value;
public static final long /*int*/ sel_bitmapImageRepForCachingDisplayInRect_ = Selector.sel_bitmapImageRepForCachingDisplayInRect_.value;
public static final long /*int*/ sel_bitsPerPixel = Selector.sel_bitsPerPixel.value;
public static final long /*int*/ sel_bitsPerSample = Selector.sel_bitsPerSample.value;
public static final long /*int*/ sel_blackColor = Selector.sel_blackColor.value;
public static final long /*int*/ sel_blueComponent = Selector.sel_blueComponent.value;
public static final long /*int*/ sel_boldSystemFontOfSize_ = Selector.sel_boldSystemFontOfSize_.value;
public static final long /*int*/ sel_boolValue = Selector.sel_boolValue.value;
public static final long /*int*/ sel_borderWidth = Selector.sel_borderWidth.value;
public static final long /*int*/ sel_boundingRectForGlyphRange_inTextContainer_ = Selector.sel_boundingRectForGlyphRange_inTextContainer_.value;
public static final long /*int*/ sel_boundingRectWithSize_options_ = Selector.sel_boundingRectWithSize_options_.value;
public static final long /*int*/ sel_bounds = Selector.sel_bounds.value;
public static final long /*int*/ sel_bundleIdentifier = Selector.sel_bundleIdentifier.value;
public static final long /*int*/ sel_bundlePath = Selector.sel_bundlePath.value;
public static final long /*int*/ sel_bundleWithIdentifier_ = Selector.sel_bundleWithIdentifier_.value;
public static final long /*int*/ sel_bundleWithPath_ = Selector.sel_bundleWithPath_.value;
public static final long /*int*/ sel_button = Selector.sel_button.value;
public static final long /*int*/ sel_buttonNumber = Selector.sel_buttonNumber.value;
public static final long /*int*/ sel_bytes = Selector.sel_bytes.value;
public static final long /*int*/ sel_bytesPerPlane = Selector.sel_bytesPerPlane.value;
public static final long /*int*/ sel_bytesPerRow = Selector.sel_bytesPerRow.value;
public static final long /*int*/ sel_cacheDisplayInRect_toBitmapImageRep_ = Selector.sel_cacheDisplayInRect_toBitmapImageRep_.value;
public static final long /*int*/ sel_calendarDate = Selector.sel_calendarDate.value;
public static final long /*int*/ sel_canBecomeKeyView = Selector.sel_canBecomeKeyView.value;
public static final long /*int*/ sel_canBecomeKeyWindow = Selector.sel_canBecomeKeyWindow.value;
public static final long /*int*/ sel_canDragRowsWithIndexes_atPoint_ = Selector.sel_canDragRowsWithIndexes_atPoint_.value;
public static final long /*int*/ sel_canGoBack = Selector.sel_canGoBack.value;
public static final long /*int*/ sel_canGoForward = Selector.sel_canGoForward.value;
public static final long /*int*/ sel_canRedo = Selector.sel_canRedo.value;
public static final long /*int*/ sel_canShowMIMEType_ = Selector.sel_canShowMIMEType_.value;
public static final long /*int*/ sel_canUndo = Selector.sel_canUndo.value;
public static final long /*int*/ sel_cancel = Selector.sel_cancel.value;
public static final long /*int*/ sel_cancelAuthenticationChallenge_ = Selector.sel_cancelAuthenticationChallenge_.value;
public static final long /*int*/ sel_cancelButtonCell = Selector.sel_cancelButtonCell.value;
public static final long /*int*/ sel_cancelButtonRectForBounds_ = Selector.sel_cancelButtonRectForBounds_.value;
public static final long /*int*/ sel_cancelOperation_ = Selector.sel_cancelOperation_.value;
public static final long /*int*/ sel_cancelTracking = Selector.sel_cancelTracking.value;
public static final long /*int*/ sel_cascadeTopLeftFromPoint_ = Selector.sel_cascadeTopLeftFromPoint_.value;
public static final long /*int*/ sel_cell = Selector.sel_cell.value;
public static final long /*int*/ sel_cellClass = Selector.sel_cellClass.value;
public static final long /*int*/ sel_cellSize = Selector.sel_cellSize.value;
public static final long /*int*/ sel_cellSizeForBounds_ = Selector.sel_cellSizeForBounds_.value;
public static final long /*int*/ sel_changeColor_ = Selector.sel_changeColor_.value;
public static final long /*int*/ sel_changeFont_ = Selector.sel_changeFont_.value;
public static final long /*int*/ sel_charCode = Selector.sel_charCode.value;
public static final long /*int*/ sel_characterAtIndex_ = Selector.sel_characterAtIndex_.value;
public static final long /*int*/ sel_characterIndexForGlyphAtIndex_ = Selector.sel_characterIndexForGlyphAtIndex_.value;
public static final long /*int*/ sel_characterIndexForInsertionAtPoint_ = Selector.sel_characterIndexForInsertionAtPoint_.value;
public static final long /*int*/ sel_characterIndexForPoint_ = Selector.sel_characterIndexForPoint_.value;
public static final long /*int*/ sel_characterIsMember_ = Selector.sel_characterIsMember_.value;
public static final long /*int*/ sel_characters = Selector.sel_characters.value;
public static final long /*int*/ sel_charactersIgnoringModifiers = Selector.sel_charactersIgnoringModifiers.value;
public static final long /*int*/ sel_chooseFilename_ = Selector.sel_chooseFilename_.value;
public static final long /*int*/ sel_className = Selector.sel_className.value;
public static final long /*int*/ sel_cleanUpOperation = Selector.sel_cleanUpOperation.value;
public static final long /*int*/ sel_clearColor = Selector.sel_clearColor.value;
public static final long /*int*/ sel_clearCurrentContext = Selector.sel_clearCurrentContext.value;
public static final long /*int*/ sel_clearDrawable = Selector.sel_clearDrawable.value;
public static final long /*int*/ sel_clickCount = Selector.sel_clickCount.value;
public static final long /*int*/ sel_clickedColumn = Selector.sel_clickedColumn.value;
public static final long /*int*/ sel_clickedRow = Selector.sel_clickedRow.value;
public static final long /*int*/ sel_close = Selector.sel_close.value;
public static final long /*int*/ sel_closePath = Selector.sel_closePath.value;
public static final long /*int*/ sel_code = Selector.sel_code.value;
public static final long /*int*/ sel_collapseItem_ = Selector.sel_collapseItem_.value;
public static final long /*int*/ sel_collapseItem_collapseChildren_ = Selector.sel_collapseItem_collapseChildren_.value;
public static final long /*int*/ sel_collectionBehavior = Selector.sel_collectionBehavior.value;
public static final long /*int*/ sel_color = Selector.sel_color.value;
public static final long /*int*/ sel_colorAtX_y_ = Selector.sel_colorAtX_y_.value;
public static final long /*int*/ sel_colorListNamed_ = Selector.sel_colorListNamed_.value;
public static final long /*int*/ sel_colorSpace = Selector.sel_colorSpace.value;
public static final long /*int*/ sel_colorSpaceModel = Selector.sel_colorSpaceModel.value;
public static final long /*int*/ sel_colorSpaceName = Selector.sel_colorSpaceName.value;
public static final long /*int*/ sel_colorUsingColorSpaceName_ = Selector.sel_colorUsingColorSpaceName_.value;
public static final long /*int*/ sel_colorWithCalibratedRed_green_blue_alpha_ = Selector.sel_colorWithCalibratedRed_green_blue_alpha_.value;
public static final long /*int*/ sel_colorWithDeviceRed_green_blue_alpha_ = Selector.sel_colorWithDeviceRed_green_blue_alpha_.value;
public static final long /*int*/ sel_colorWithKey_ = Selector.sel_colorWithKey_.value;
public static final long /*int*/ sel_colorWithPatternImage_ = Selector.sel_colorWithPatternImage_.value;
public static final long /*int*/ sel_columnAtPoint_ = Selector.sel_columnAtPoint_.value;
public static final long /*int*/ sel_columnIndexesInRect_ = Selector.sel_columnIndexesInRect_.value;
public static final long /*int*/ sel_columnWithIdentifier_ = Selector.sel_columnWithIdentifier_.value;
public static final long /*int*/ sel_comboBoxSelectionDidChange_ = Selector.sel_comboBoxSelectionDidChange_.value;
public static final long /*int*/ sel_comboBoxWillDismiss_ = Selector.sel_comboBoxWillDismiss_.value;
public static final long /*int*/ sel_comboBoxWillPopUp_ = Selector.sel_comboBoxWillPopUp_.value;
public static final long /*int*/ sel_compare_ = Selector.sel_compare_.value;
public static final long /*int*/ sel_concat = Selector.sel_concat.value;
public static final long /*int*/ sel_conformsToProtocol_ = Selector.sel_conformsToProtocol_.value;
public static final long /*int*/ sel_containerSize = Selector.sel_containerSize.value;
public static final long /*int*/ sel_containsIndex_ = Selector.sel_containsIndex_.value;
public static final long /*int*/ sel_containsObject_ = Selector.sel_containsObject_.value;
public static final long /*int*/ sel_containsPoint_ = Selector.sel_containsPoint_.value;
public static final long /*int*/ sel_contentRect = Selector.sel_contentRect.value;
public static final long /*int*/ sel_contentSize = Selector.sel_contentSize.value;
public static final long /*int*/ sel_contentSizeForFrameSize_hasHorizontalScroller_hasVerticalScroller_borderType_ = Selector.sel_contentSizeForFrameSize_hasHorizontalScroller_hasVerticalScroller_borderType_.value;
public static final long /*int*/ sel_contentView = Selector.sel_contentView.value;
public static final long /*int*/ sel_contentViewMargins = Selector.sel_contentViewMargins.value;
public static final long /*int*/ sel_context = Selector.sel_context.value;
public static final long /*int*/ sel_controlBackgroundColor = Selector.sel_controlBackgroundColor.value;
public static final long /*int*/ sel_controlContentFontOfSize_ = Selector.sel_controlContentFontOfSize_.value;
public static final long /*int*/ sel_controlDarkShadowColor = Selector.sel_controlDarkShadowColor.value;
public static final long /*int*/ sel_controlHighlightColor = Selector.sel_controlHighlightColor.value;
public static final long /*int*/ sel_controlLightHighlightColor = Selector.sel_controlLightHighlightColor.value;
public static final long /*int*/ sel_controlPointBounds = Selector.sel_controlPointBounds.value;
public static final long /*int*/ sel_controlShadowColor = Selector.sel_controlShadowColor.value;
public static final long /*int*/ sel_controlSize = Selector.sel_controlSize.value;
public static final long /*int*/ sel_controlTextColor = Selector.sel_controlTextColor.value;
public static final long /*int*/ sel_convertBaseToScreen_ = Selector.sel_convertBaseToScreen_.value;
public static final long /*int*/ sel_convertFont_toHaveTrait_ = Selector.sel_convertFont_toHaveTrait_.value;
public static final long /*int*/ sel_convertPoint_fromView_ = Selector.sel_convertPoint_fromView_.value;
public static final long /*int*/ sel_convertPoint_toView_ = Selector.sel_convertPoint_toView_.value;
public static final long /*int*/ sel_convertPointFromBase_ = Selector.sel_convertPointFromBase_.value;
public static final long /*int*/ sel_convertPointToBase_ = Selector.sel_convertPointToBase_.value;
public static final long /*int*/ sel_convertRect_fromView_ = Selector.sel_convertRect_fromView_.value;
public static final long /*int*/ sel_convertRect_toView_ = Selector.sel_convertRect_toView_.value;
public static final long /*int*/ sel_convertRectFromBase_ = Selector.sel_convertRectFromBase_.value;
public static final long /*int*/ sel_convertRectToBase_ = Selector.sel_convertRectToBase_.value;
public static final long /*int*/ sel_convertScreenToBase_ = Selector.sel_convertScreenToBase_.value;
public static final long /*int*/ sel_convertSize_fromView_ = Selector.sel_convertSize_fromView_.value;
public static final long /*int*/ sel_convertSize_toView_ = Selector.sel_convertSize_toView_.value;
public static final long /*int*/ sel_convertSizeFromBase_ = Selector.sel_convertSizeFromBase_.value;
public static final long /*int*/ sel_convertSizeToBase_ = Selector.sel_convertSizeToBase_.value;
public static final long /*int*/ sel_cookies = Selector.sel_cookies.value;
public static final long /*int*/ sel_cookiesForURL_ = Selector.sel_cookiesForURL_.value;
public static final long /*int*/ sel_cookiesWithResponseHeaderFields_forURL_ = Selector.sel_cookiesWithResponseHeaderFields_forURL_.value;
public static final long /*int*/ sel_copiesOnScroll = Selector.sel_copiesOnScroll.value;
public static final long /*int*/ sel_copy = Selector.sel_copy.value;
public static final long /*int*/ sel_copy_ = Selector.sel_copy_.value;
public static final long /*int*/ sel_count = Selector.sel_count.value;
public static final long /*int*/ sel_createContext = Selector.sel_createContext.value;
public static final long /*int*/ sel_createFileAtPath_contents_attributes_ = Selector.sel_createFileAtPath_contents_attributes_.value;
public static final long /*int*/ sel_credentialWithUser_password_persistence_ = Selector.sel_credentialWithUser_password_persistence_.value;
public static final long /*int*/ sel_crosshairCursor = Selector.sel_crosshairCursor.value;
public static final long /*int*/ sel_ctrlKey = Selector.sel_ctrlKey.value;
public static final long /*int*/ sel_currentApplication = Selector.sel_currentApplication.value;
public static final long /*int*/ sel_currentContext = Selector.sel_currentContext.value;
public static final long /*int*/ sel_currentCursor = Selector.sel_currentCursor.value;
public static final long /*int*/ sel_currentEditor = Selector.sel_currentEditor.value;
public static final long /*int*/ sel_currentEvent = Selector.sel_currentEvent.value;
public static final long /*int*/ sel_currentHandler = Selector.sel_currentHandler.value;
public static final long /*int*/ sel_currentInputManager = Selector.sel_currentInputManager.value;
public static final long /*int*/ sel_currentPoint = Selector.sel_currentPoint.value;
public static final long /*int*/ sel_currentRunLoop = Selector.sel_currentRunLoop.value;
public static final long /*int*/ sel_currentThread = Selector.sel_currentThread.value;
public static final long /*int*/ sel_cursorUpdate_ = Selector.sel_cursorUpdate_.value;
public static final long /*int*/ sel_curveToPoint_controlPoint1_controlPoint2_ = Selector.sel_curveToPoint_controlPoint1_controlPoint2_.value;
public static final long /*int*/ sel_cut_ = Selector.sel_cut_.value;
public static final long /*int*/ sel_dataCell = Selector.sel_dataCell.value;
public static final long /*int*/ sel_dataForType_ = Selector.sel_dataForType_.value;
public static final long /*int*/ sel_dataSource = Selector.sel_dataSource.value;
public static final long /*int*/ sel_dataWithBytes_length_ = Selector.sel_dataWithBytes_length_.value;
public static final long /*int*/ sel_dateValue = Selector.sel_dateValue.value;
public static final long /*int*/ sel_dateWithCalendarFormat_timeZone_ = Selector.sel_dateWithCalendarFormat_timeZone_.value;
public static final long /*int*/ sel_dateWithTimeIntervalSinceNow_ = Selector.sel_dateWithTimeIntervalSinceNow_.value;
public static final long /*int*/ sel_dateWithYear_month_day_hour_minute_second_timeZone_ = Selector.sel_dateWithYear_month_day_hour_minute_second_timeZone_.value;
public static final long /*int*/ sel_dayOfMonth = Selector.sel_dayOfMonth.value;
public static final long /*int*/ sel_dealloc = Selector.sel_dealloc.value;
public static final long /*int*/ sel_decimalDigitCharacterSet = Selector.sel_decimalDigitCharacterSet.value;
public static final long /*int*/ sel_decimalSeparator = Selector.sel_decimalSeparator.value;
public static final long /*int*/ sel_declareTypes_owner_ = Selector.sel_declareTypes_owner_.value;
public static final long /*int*/ sel_defaultBaselineOffsetForFont_ = Selector.sel_defaultBaselineOffsetForFont_.value;
public static final long /*int*/ sel_defaultButtonCell = Selector.sel_defaultButtonCell.value;
public static final long /*int*/ sel_defaultCenter = Selector.sel_defaultCenter.value;
public static final long /*int*/ sel_defaultFlatness = Selector.sel_defaultFlatness.value;
public static final long /*int*/ sel_defaultLineHeightForFont_ = Selector.sel_defaultLineHeightForFont_.value;
public static final long /*int*/ sel_defaultManager = Selector.sel_defaultManager.value;
public static final long /*int*/ sel_defaultParagraphStyle = Selector.sel_defaultParagraphStyle.value;
public static final long /*int*/ sel_defaultPrinter = Selector.sel_defaultPrinter.value;
public static final long /*int*/ sel_defaultTimeZone = Selector.sel_defaultTimeZone.value;
public static final long /*int*/ sel_delegate = Selector.sel_delegate.value;
public static final long /*int*/ sel_deleteCookie_ = Selector.sel_deleteCookie_.value;
public static final long /*int*/ sel_deliverResult = Selector.sel_deliverResult.value;
public static final long /*int*/ sel_deltaX = Selector.sel_deltaX.value;
public static final long /*int*/ sel_deltaY = Selector.sel_deltaY.value;
public static final long /*int*/ sel_deminiaturize_ = Selector.sel_deminiaturize_.value;
public static final long /*int*/ sel_depth = Selector.sel_depth.value;
public static final long /*int*/ sel_descender = Selector.sel_descender.value;
public static final long /*int*/ sel_description = Selector.sel_description.value;
public static final long /*int*/ sel_deselectAll_ = Selector.sel_deselectAll_.value;
public static final long /*int*/ sel_deselectItemAtIndex_ = Selector.sel_deselectItemAtIndex_.value;
public static final long /*int*/ sel_deselectRow_ = Selector.sel_deselectRow_.value;
public static final long /*int*/ sel_destroyContext = Selector.sel_destroyContext.value;
public static final long /*int*/ sel_detail = Selector.sel_detail.value;
public static final long /*int*/ sel_device = Selector.sel_device.value;
public static final long /*int*/ sel_deviceDescription = Selector.sel_deviceDescription.value;
public static final long /*int*/ sel_deviceSize = Selector.sel_deviceSize.value;
public static final long /*int*/ sel_dictionary = Selector.sel_dictionary.value;
public static final long /*int*/ sel_dictionaryWithCapacity_ = Selector.sel_dictionaryWithCapacity_.value;
public static final long /*int*/ sel_dictionaryWithObject_forKey_ = Selector.sel_dictionaryWithObject_forKey_.value;
public static final long /*int*/ sel_disableCursorRects = Selector.sel_disableCursorRects.value;
public static final long /*int*/ sel_disableFlushWindow = Selector.sel_disableFlushWindow.value;
public static final long /*int*/ sel_disabledControlTextColor = Selector.sel_disabledControlTextColor.value;
public static final long /*int*/ sel_discardCursorRects = Selector.sel_discardCursorRects.value;
public static final long /*int*/ sel_display = Selector.sel_display.value;
public static final long /*int*/ sel_displayIfNeeded = Selector.sel_displayIfNeeded.value;
public static final long /*int*/ sel_displayName = Selector.sel_displayName.value;
public static final long /*int*/ sel_displayNameForKey_value_ = Selector.sel_displayNameForKey_value_.value;
public static final long /*int*/ sel_displayRectIgnoringOpacity_inContext_ = Selector.sel_displayRectIgnoringOpacity_inContext_.value;
public static final long /*int*/ sel_distantFuture = Selector.sel_distantFuture.value;
public static final long /*int*/ sel_doCommandBySelector_ = Selector.sel_doCommandBySelector_.value;
public static final long /*int*/ sel_dockTile = Selector.sel_dockTile.value;
public static final long /*int*/ sel_documentCursor = Selector.sel_documentCursor.value;
public static final long /*int*/ sel_documentSource = Selector.sel_documentSource.value;
public static final long /*int*/ sel_documentView = Selector.sel_documentView.value;
public static final long /*int*/ sel_documentViewShouldHandlePrint = Selector.sel_documentViewShouldHandlePrint.value;
public static final long /*int*/ sel_documentVisibleRect = Selector.sel_documentVisibleRect.value;
public static final long /*int*/ sel_doubleClickAtIndex_ = Selector.sel_doubleClickAtIndex_.value;
public static final long /*int*/ sel_doubleValue = Selector.sel_doubleValue.value;
public static final long /*int*/ sel_download = Selector.sel_download.value;
public static final long /*int*/ sel_download_decideDestinationWithSuggestedFilename_ = Selector.sel_download_decideDestinationWithSuggestedFilename_.value;
public static final long /*int*/ sel_dragImage_at_offset_event_pasteboard_source_slideBack_ = Selector.sel_dragImage_at_offset_event_pasteboard_source_slideBack_.value;
public static final long /*int*/ sel_dragImageForRowsWithIndexes_tableColumns_event_offset_ = Selector.sel_dragImageForRowsWithIndexes_tableColumns_event_offset_.value;
public static final long /*int*/ sel_dragSelectionWithEvent_offset_slideBack_ = Selector.sel_dragSelectionWithEvent_offset_slideBack_.value;
public static final long /*int*/ sel_draggedImage_beganAt_ = Selector.sel_draggedImage_beganAt_.value;
public static final long /*int*/ sel_draggedImage_endedAt_operation_ = Selector.sel_draggedImage_endedAt_operation_.value;
public static final long /*int*/ sel_draggingDestinationWindow = Selector.sel_draggingDestinationWindow.value;
public static final long /*int*/ sel_draggingEnded_ = Selector.sel_draggingEnded_.value;
public static final long /*int*/ sel_draggingEntered_ = Selector.sel_draggingEntered_.value;
public static final long /*int*/ sel_draggingExited_ = Selector.sel_draggingExited_.value;
public static final long /*int*/ sel_draggingLocation = Selector.sel_draggingLocation.value;
public static final long /*int*/ sel_draggingPasteboard = Selector.sel_draggingPasteboard.value;
public static final long /*int*/ sel_draggingSourceOperationMask = Selector.sel_draggingSourceOperationMask.value;
public static final long /*int*/ sel_draggingSourceOperationMaskForLocal_ = Selector.sel_draggingSourceOperationMaskForLocal_.value;
public static final long /*int*/ sel_draggingUpdated_ = Selector.sel_draggingUpdated_.value;
public static final long /*int*/ sel_drawAtPoint_ = Selector.sel_drawAtPoint_.value;
public static final long /*int*/ sel_drawAtPoint_fromRect_operation_fraction_ = Selector.sel_drawAtPoint_fromRect_operation_fraction_.value;
public static final long /*int*/ sel_drawBackgroundForGlyphRange_atPoint_ = Selector.sel_drawBackgroundForGlyphRange_atPoint_.value;
public static final long /*int*/ sel_drawBackgroundInClipRect_ = Selector.sel_drawBackgroundInClipRect_.value;
public static final long /*int*/ sel_drawBezelWithFrame_inView_ = Selector.sel_drawBezelWithFrame_inView_.value;
public static final long /*int*/ sel_drawFromPoint_toPoint_options_ = Selector.sel_drawFromPoint_toPoint_options_.value;
public static final long /*int*/ sel_drawGlyphsForGlyphRange_atPoint_ = Selector.sel_drawGlyphsForGlyphRange_atPoint_.value;
public static final long /*int*/ sel_drawImage_withFrame_inView_ = Selector.sel_drawImage_withFrame_inView_.value;
public static final long /*int*/ sel_drawInBezierPath_angle_ = Selector.sel_drawInBezierPath_angle_.value;
public static final long /*int*/ sel_drawInRect_ = Selector.sel_drawInRect_.value;
public static final long /*int*/ sel_drawInRect_angle_ = Selector.sel_drawInRect_angle_.value;
public static final long /*int*/ sel_drawInRect_fromRect_operation_fraction_ = Selector.sel_drawInRect_fromRect_operation_fraction_.value;
public static final long /*int*/ sel_drawInteriorWithFrame_inView_ = Selector.sel_drawInteriorWithFrame_inView_.value;
public static final long /*int*/ sel_drawLabel_inRect_ = Selector.sel_drawLabel_inRect_.value;
public static final long /*int*/ sel_drawRect_ = Selector.sel_drawRect_.value;
public static final long /*int*/ sel_drawSortIndicatorWithFrame_inView_ascending_priority_ = Selector.sel_drawSortIndicatorWithFrame_inView_ascending_priority_.value;
public static final long /*int*/ sel_drawStatusBarBackgroundInRect_withHighlight_ = Selector.sel_drawStatusBarBackgroundInRect_withHighlight_.value;
public static final long /*int*/ sel_drawTitle_withFrame_inView_ = Selector.sel_drawTitle_withFrame_inView_.value;
public static final long /*int*/ sel_drawViewBackgroundInRect_ = Selector.sel_drawViewBackgroundInRect_.value;
public static final long /*int*/ sel_drawWithExpansionFrame_inView_ = Selector.sel_drawWithExpansionFrame_inView_.value;
public static final long /*int*/ sel_drawingRectForBounds_ = Selector.sel_drawingRectForBounds_.value;
public static final long /*int*/ sel_elementAtIndex_associatedPoints_ = Selector.sel_elementAtIndex_associatedPoints_.value;
public static final long /*int*/ sel_elementCount = Selector.sel_elementCount.value;
public static final long /*int*/ sel_enableCursorRects = Selector.sel_enableCursorRects.value;
public static final long /*int*/ sel_enableFlushWindow = Selector.sel_enableFlushWindow.value;
public static final long /*int*/ sel_endDocument = Selector.sel_endDocument.value;
public static final long /*int*/ sel_endEditing = Selector.sel_endEditing.value;
public static final long /*int*/ sel_endEditingFor_ = Selector.sel_endEditingFor_.value;
public static final long /*int*/ sel_endGestureWithEvent_ = Selector.sel_endGestureWithEvent_.value;
public static final long /*int*/ sel_endPage = Selector.sel_endPage.value;
public static final long /*int*/ sel_endSheet_returnCode_ = Selector.sel_endSheet_returnCode_.value;
public static final long /*int*/ sel_enterExitEventWithType_location_modifierFlags_timestamp_windowNumber_context_eventNumber_trackingNumber_userData_ = Selector.sel_enterExitEventWithType_location_modifierFlags_timestamp_windowNumber_context_eventNumber_trackingNumber_userData_.value;
public static final long /*int*/ sel_enumeratorAtPath_ = Selector.sel_enumeratorAtPath_.value;
public static final long /*int*/ sel_expandItem_ = Selector.sel_expandItem_.value;
public static final long /*int*/ sel_expandItem_expandChildren_ = Selector.sel_expandItem_expandChildren_.value;
public static final long /*int*/ sel_expansionFrameWithFrame_inView_ = Selector.sel_expansionFrameWithFrame_inView_.value;
public static final long /*int*/ sel_familyName = Selector.sel_familyName.value;
public static final long /*int*/ sel_fieldEditor_forObject_ = Selector.sel_fieldEditor_forObject_.value;
public static final long /*int*/ sel_fileExistsAtPath_ = Selector.sel_fileExistsAtPath_.value;
public static final long /*int*/ sel_fileExistsAtPath_isDirectory_ = Selector.sel_fileExistsAtPath_isDirectory_.value;
public static final long /*int*/ sel_fileSystemRepresentation = Selector.sel_fileSystemRepresentation.value;
public static final long /*int*/ sel_fileURLWithPath_ = Selector.sel_fileURLWithPath_.value;
public static final long /*int*/ sel_filename = Selector.sel_filename.value;
public static final long /*int*/ sel_filenames = Selector.sel_filenames.value;
public static final long /*int*/ sel_fill = Selector.sel_fill.value;
public static final long /*int*/ sel_fillRect_ = Selector.sel_fillRect_.value;
public static final long /*int*/ sel_finishLaunching = Selector.sel_finishLaunching.value;
public static final long /*int*/ sel_firstIndex = Selector.sel_firstIndex.value;
public static final long /*int*/ sel_firstRectForCharacterRange_ = Selector.sel_firstRectForCharacterRange_.value;
public static final long /*int*/ sel_firstResponder = Selector.sel_firstResponder.value;
public static final long /*int*/ sel_flagsChanged_ = Selector.sel_flagsChanged_.value;
public static final long /*int*/ sel_floatValue = Selector.sel_floatValue.value;
public static final long /*int*/ sel_flushBuffer = Selector.sel_flushBuffer.value;
public static final long /*int*/ sel_flushGraphics = Selector.sel_flushGraphics.value;
public static final long /*int*/ sel_flushWindowIfNeeded = Selector.sel_flushWindowIfNeeded.value;
public static final long /*int*/ sel_focusRingMaskBoundsForFrame_inView_ = Selector.sel_focusRingMaskBoundsForFrame_inView_.value;
public static final long /*int*/ sel_font = Selector.sel_font.value;
public static final long /*int*/ sel_fontName = Selector.sel_fontName.value;
public static final long /*int*/ sel_fontWithFamily_traits_weight_size_ = Selector.sel_fontWithFamily_traits_weight_size_.value;
public static final long /*int*/ sel_fontWithName_size_ = Selector.sel_fontWithName_size_.value;
public static final long /*int*/ sel_frame = Selector.sel_frame.value;
public static final long /*int*/ sel_frameOfCellAtColumn_row_ = Selector.sel_frameOfCellAtColumn_row_.value;
public static final long /*int*/ sel_frameOfOutlineCellAtRow_ = Selector.sel_frameOfOutlineCellAtRow_.value;
public static final long /*int*/ sel_frameRectForContentRect_ = Selector.sel_frameRectForContentRect_.value;
public static final long /*int*/ sel_frameSizeForContentSize_hasHorizontalScroller_hasVerticalScroller_borderType_ = Selector.sel_frameSizeForContentSize_hasHorizontalScroller_hasVerticalScroller_borderType_.value;
public static final long /*int*/ sel_fullPathForApplication_ = Selector.sel_fullPathForApplication_.value;
public static final long /*int*/ sel_generalPasteboard = Selector.sel_generalPasteboard.value;
public static final long /*int*/ sel_genericRGBColorSpace = Selector.sel_genericRGBColorSpace.value;
public static final long /*int*/ sel_getBitmapDataPlanes_ = Selector.sel_getBitmapDataPlanes_.value;
public static final long /*int*/ sel_getBytes_ = Selector.sel_getBytes_.value;
public static final long /*int*/ sel_getBytes_length_ = Selector.sel_getBytes_length_.value;
public static final long /*int*/ sel_getCharacters_ = Selector.sel_getCharacters_.value;
public static final long /*int*/ sel_getCharacters_range_ = Selector.sel_getCharacters_range_.value;
public static final long /*int*/ sel_getComponents_ = Selector.sel_getComponents_.value;
public static final long /*int*/ sel_getGlyphs_range_ = Selector.sel_getGlyphs_range_.value;
public static final long /*int*/ sel_getGlyphsInRange_glyphs_characterIndexes_glyphInscriptions_elasticBits_bidiLevels_ = Selector.sel_getGlyphsInRange_glyphs_characterIndexes_glyphInscriptions_elasticBits_bidiLevels_.value;
public static final long /*int*/ sel_getIndexes_maxCount_inIndexRange_ = Selector.sel_getIndexes_maxCount_inIndexRange_.value;
public static final long /*int*/ sel_getInfoForFile_application_type_ = Selector.sel_getInfoForFile_application_type_.value;
public static final long /*int*/ sel_getValues_forAttribute_forVirtualScreen_ = Selector.sel_getValues_forAttribute_forVirtualScreen_.value;
public static final long /*int*/ sel_globalContext = Selector.sel_globalContext.value;
public static final long /*int*/ sel_glyphIndexForCharacterAtIndex_ = Selector.sel_glyphIndexForCharacterAtIndex_.value;
public static final long /*int*/ sel_glyphIndexForPoint_inTextContainer_fractionOfDistanceThroughGlyph_ = Selector.sel_glyphIndexForPoint_inTextContainer_fractionOfDistanceThroughGlyph_.value;
public static final long /*int*/ sel_glyphRangeForCharacterRange_actualCharacterRange_ = Selector.sel_glyphRangeForCharacterRange_actualCharacterRange_.value;
public static final long /*int*/ sel_glyphRangeForTextContainer_ = Selector.sel_glyphRangeForTextContainer_.value;
public static final long /*int*/ sel_goBack = Selector.sel_goBack.value;
public static final long /*int*/ sel_goForward = Selector.sel_goForward.value;
public static final long /*int*/ sel_graphicsContext = Selector.sel_graphicsContext.value;
public static final long /*int*/ sel_graphicsContextWithBitmapImageRep_ = Selector.sel_graphicsContextWithBitmapImageRep_.value;
public static final long /*int*/ sel_graphicsContextWithGraphicsPort_flipped_ = Selector.sel_graphicsContextWithGraphicsPort_flipped_.value;
public static final long /*int*/ sel_graphicsContextWithWindow_ = Selector.sel_graphicsContextWithWindow_.value;
public static final long /*int*/ sel_graphicsPort = Selector.sel_graphicsPort.value;
public static final long /*int*/ sel_greenComponent = Selector.sel_greenComponent.value;
public static final long /*int*/ sel_handleEvent_ = Selector.sel_handleEvent_.value;
public static final long /*int*/ sel_handleFailureInFunction_file_lineNumber_description_ = Selector.sel_handleFailureInFunction_file_lineNumber_description_.value;
public static final long /*int*/ sel_handleFailureInMethod_object_file_lineNumber_description_ = Selector.sel_handleFailureInMethod_object_file_lineNumber_description_.value;
public static final long /*int*/ sel_handleMouseEvent_ = Selector.sel_handleMouseEvent_.value;
public static final long /*int*/ sel_hasAlpha = Selector.sel_hasAlpha.value;
public static final long /*int*/ sel_hasMarkedText = Selector.sel_hasMarkedText.value;
public static final long /*int*/ sel_hasPassword = Selector.sel_hasPassword.value;
public static final long /*int*/ sel_hasShadow = Selector.sel_hasShadow.value;
public static final long /*int*/ sel_headerCell = Selector.sel_headerCell.value;
public static final long /*int*/ sel_headerRectOfColumn_ = Selector.sel_headerRectOfColumn_.value;
public static final long /*int*/ sel_headerView = Selector.sel_headerView.value;
public static final long /*int*/ sel_helpRequested_ = Selector.sel_helpRequested_.value;
public static final long /*int*/ sel_hide_ = Selector.sel_hide_.value;
public static final long /*int*/ sel_hideOtherApplications_ = Selector.sel_hideOtherApplications_.value;
public static final long /*int*/ sel_highlightColorInView_ = Selector.sel_highlightColorInView_.value;
public static final long /*int*/ sel_highlightColorWithFrame_inView_ = Selector.sel_highlightColorWithFrame_inView_.value;
public static final long /*int*/ sel_highlightSelectionInClipRect_ = Selector.sel_highlightSelectionInClipRect_.value;
public static final long /*int*/ sel_hitPart = Selector.sel_hitPart.value;
public static final long /*int*/ sel_hitTest_ = Selector.sel_hitTest_.value;
public static final long /*int*/ sel_hitTestForEvent_inRect_ofView_ = Selector.sel_hitTestForEvent_inRect_ofView_.value;
public static final long /*int*/ sel_host = Selector.sel_host.value;
public static final long /*int*/ sel_hotSpot = Selector.sel_hotSpot.value;
public static final long /*int*/ sel_hourOfDay = Selector.sel_hourOfDay.value;
public static final long /*int*/ sel_iconForFile_ = Selector.sel_iconForFile_.value;
public static final long /*int*/ sel_iconForFileType_ = Selector.sel_iconForFileType_.value;
public static final long /*int*/ sel_ignore = Selector.sel_ignore.value;
public static final long /*int*/ sel_ignoreModifierKeysWhileDragging = Selector.sel_ignoreModifierKeysWhileDragging.value;
public static final long /*int*/ sel_image = Selector.sel_image.value;
public static final long /*int*/ sel_imageInterpolation = Selector.sel_imageInterpolation.value;
public static final long /*int*/ sel_imageNamed_ = Selector.sel_imageNamed_.value;
public static final long /*int*/ sel_imageRectForBounds_ = Selector.sel_imageRectForBounds_.value;
public static final long /*int*/ sel_imageRepWithContentsOfFile_ = Selector.sel_imageRepWithContentsOfFile_.value;
public static final long /*int*/ sel_imageRepWithData_ = Selector.sel_imageRepWithData_.value;
public static final long /*int*/ sel_imageablePageBounds = Selector.sel_imageablePageBounds.value;
public static final long /*int*/ sel_increment = Selector.sel_increment.value;
public static final long /*int*/ sel_indentationPerLevel = Selector.sel_indentationPerLevel.value;
public static final long /*int*/ sel_indexOfItemWithTarget_andAction_ = Selector.sel_indexOfItemWithTarget_andAction_.value;
public static final long /*int*/ sel_indexOfObjectIdenticalTo_ = Selector.sel_indexOfObjectIdenticalTo_.value;
public static final long /*int*/ sel_indexOfSelectedItem = Selector.sel_indexOfSelectedItem.value;
public static final long /*int*/ sel_indexSetWithIndex_ = Selector.sel_indexSetWithIndex_.value;
public static final long /*int*/ sel_infoDictionary = Selector.sel_infoDictionary.value;
public static final long /*int*/ sel_init = Selector.sel_init.value;
public static final long /*int*/ sel_initByReferencingFile_ = Selector.sel_initByReferencingFile_.value;
public static final long /*int*/ sel_initListDescriptor = Selector.sel_initListDescriptor.value;
public static final long /*int*/ sel_initWithAttributes_ = Selector.sel_initWithAttributes_.value;
public static final long /*int*/ sel_initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bitmapFormat_bytesPerRow_bitsPerPixel_ = Selector.sel_initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bitmapFormat_bytesPerRow_bitsPerPixel_.value;
public static final long /*int*/ sel_initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bytesPerRow_bitsPerPixel_ = Selector.sel_initWithBitmapDataPlanes_pixelsWide_pixelsHigh_bitsPerSample_samplesPerPixel_hasAlpha_isPlanar_colorSpaceName_bytesPerRow_bitsPerPixel_.value;
public static final long /*int*/ sel_initWithCapacity_ = Selector.sel_initWithCapacity_.value;
public static final long /*int*/ sel_initWithCharacters_length_ = Selector.sel_initWithCharacters_length_.value;
public static final long /*int*/ sel_initWithColors_ = Selector.sel_initWithColors_.value;
public static final long /*int*/ sel_initWithContainerSize_ = Selector.sel_initWithContainerSize_.value;
public static final long /*int*/ sel_initWithContentRect_styleMask_backing_defer_ = Selector.sel_initWithContentRect_styleMask_backing_defer_.value;
public static final long /*int*/ sel_initWithContentRect_styleMask_backing_defer_screen_ = Selector.sel_initWithContentRect_styleMask_backing_defer_screen_.value;
public static final long /*int*/ sel_initWithContentsOfFile_ = Selector.sel_initWithContentsOfFile_.value;
public static final long /*int*/ sel_initWithData_ = Selector.sel_initWithData_.value;
public static final long /*int*/ sel_initWithDictionary_ = Selector.sel_initWithDictionary_.value;
public static final long /*int*/ sel_initWithFileWrapper_ = Selector.sel_initWithFileWrapper_.value;
public static final long /*int*/ sel_initWithFocusedViewRect_ = Selector.sel_initWithFocusedViewRect_.value;
public static final long /*int*/ sel_initWithFormat_shareContext_ = Selector.sel_initWithFormat_shareContext_.value;
public static final long /*int*/ sel_initWithFrame_ = Selector.sel_initWithFrame_.value;
public static final long /*int*/ sel_initWithFrame_frameName_groupName_ = Selector.sel_initWithFrame_frameName_groupName_.value;
public static final long /*int*/ sel_initWithFrame_pullsDown_ = Selector.sel_initWithFrame_pullsDown_.value;
public static final long /*int*/ sel_initWithIconRef_ = Selector.sel_initWithIconRef_.value;
public static final long /*int*/ sel_initWithIdentifier_ = Selector.sel_initWithIdentifier_.value;
public static final long /*int*/ sel_initWithImage_hotSpot_ = Selector.sel_initWithImage_hotSpot_.value;
public static final long /*int*/ sel_initWithIndex_ = Selector.sel_initWithIndex_.value;
public static final long /*int*/ sel_initWithIndexSet_ = Selector.sel_initWithIndexSet_.value;
public static final long /*int*/ sel_initWithIndexesInRange_ = Selector.sel_initWithIndexesInRange_.value;
public static final long /*int*/ sel_initWithItemIdentifier_ = Selector.sel_initWithItemIdentifier_.value;
public static final long /*int*/ sel_initWithLocaleIdentifier_ = Selector.sel_initWithLocaleIdentifier_.value;
public static final long /*int*/ sel_initWithName_ = Selector.sel_initWithName_.value;
public static final long /*int*/ sel_initWithRect_options_owner_userInfo_ = Selector.sel_initWithRect_options_owner_userInfo_.value;
public static final long /*int*/ sel_initWithSize_ = Selector.sel_initWithSize_.value;
public static final long /*int*/ sel_initWithStartingColor_endingColor_ = Selector.sel_initWithStartingColor_endingColor_.value;
public static final long /*int*/ sel_initWithString_ = Selector.sel_initWithString_.value;
public static final long /*int*/ sel_initWithString_attributes_ = Selector.sel_initWithString_attributes_.value;
public static final long /*int*/ sel_initWithTitle_ = Selector.sel_initWithTitle_.value;
public static final long /*int*/ sel_initWithTitle_action_keyEquivalent_ = Selector.sel_initWithTitle_action_keyEquivalent_.value;
public static final long /*int*/ sel_initWithTransform_ = Selector.sel_initWithTransform_.value;
public static final long /*int*/ sel_initWithType_location_ = Selector.sel_initWithType_location_.value;
public static final long /*int*/ sel_initWithURL_ = Selector.sel_initWithURL_.value;
public static final long /*int*/ sel_insertColor_key_atIndex_ = Selector.sel_insertColor_key_atIndex_.value;
public static final long /*int*/ sel_insertItem_atIndex_ = Selector.sel_insertItem_atIndex_.value;
public static final long /*int*/ sel_insertItemWithItemIdentifier_atIndex_ = Selector.sel_insertItemWithItemIdentifier_atIndex_.value;
public static final long /*int*/ sel_insertItemWithObjectValue_atIndex_ = Selector.sel_insertItemWithObjectValue_atIndex_.value;
public static final long /*int*/ sel_insertTabViewItem_atIndex_ = Selector.sel_insertTabViewItem_atIndex_.value;
public static final long /*int*/ sel_insertText_ = Selector.sel_insertText_.value;
public static final long /*int*/ sel_intValue = Selector.sel_intValue.value;
public static final long /*int*/ sel_integerValue = Selector.sel_integerValue.value;
public static final long /*int*/ sel_intercellSpacing = Selector.sel_intercellSpacing.value;
public static final long /*int*/ sel_interpretKeyEvents_ = Selector.sel_interpretKeyEvents_.value;
public static final long /*int*/ sel_invalidate = Selector.sel_invalidate.value;
public static final long /*int*/ sel_invalidateShadow = Selector.sel_invalidateShadow.value;
public static final long /*int*/ sel_invert = Selector.sel_invert.value;
public static final long /*int*/ sel_isActive = Selector.sel_isActive.value;
public static final long /*int*/ sel_isDescendantOf_ = Selector.sel_isDescendantOf_.value;
public static final long /*int*/ sel_isDocumentEdited = Selector.sel_isDocumentEdited.value;
public static final long /*int*/ sel_isDrawingToScreen = Selector.sel_isDrawingToScreen.value;
public static final long /*int*/ sel_isEmpty = Selector.sel_isEmpty.value;
public static final long /*int*/ sel_isEnabled = Selector.sel_isEnabled.value;
public static final long /*int*/ sel_isEqual_ = Selector.sel_isEqual_.value;
public static final long /*int*/ sel_isEqualTo_ = Selector.sel_isEqualTo_.value;
public static final long /*int*/ sel_isEqualToString_ = Selector.sel_isEqualToString_.value;
public static final long /*int*/ sel_isExecutableFileAtPath_ = Selector.sel_isExecutableFileAtPath_.value;
public static final long /*int*/ sel_isFieldEditor = Selector.sel_isFieldEditor.value;
public static final long /*int*/ sel_isFilePackageAtPath_ = Selector.sel_isFilePackageAtPath_.value;
public static final long /*int*/ sel_isFileURL = Selector.sel_isFileURL.value;
public static final long /*int*/ sel_isFlipped = Selector.sel_isFlipped.value;
public static final long /*int*/ sel_isHidden = Selector.sel_isHidden.value;
public static final long /*int*/ sel_isHiddenOrHasHiddenAncestor = Selector.sel_isHiddenOrHasHiddenAncestor.value;
public static final long /*int*/ sel_isHighlighted = Selector.sel_isHighlighted.value;
public static final long /*int*/ sel_isItemExpanded_ = Selector.sel_isItemExpanded_.value;
public static final long /*int*/ sel_isKeyWindow = Selector.sel_isKeyWindow.value;
public static final long /*int*/ sel_isKindOfClass_ = Selector.sel_isKindOfClass_.value;
public static final long /*int*/ sel_isMainThread = Selector.sel_isMainThread.value;
public static final long /*int*/ sel_isMainWindow = Selector.sel_isMainWindow.value;
public static final long /*int*/ sel_isMiniaturized = Selector.sel_isMiniaturized.value;
public static final long /*int*/ sel_isOpaque = Selector.sel_isOpaque.value;
public static final long /*int*/ sel_isPlanar = Selector.sel_isPlanar.value;
public static final long /*int*/ sel_isResting = Selector.sel_isResting.value;
public static final long /*int*/ sel_isRowSelected_ = Selector.sel_isRowSelected_.value;
public static final long /*int*/ sel_isRunning = Selector.sel_isRunning.value;
public static final long /*int*/ sel_isSelectionOnly = Selector.sel_isSelectionOnly.value;
public static final long /*int*/ sel_isSeparatorItem = Selector.sel_isSeparatorItem.value;
public static final long /*int*/ sel_isSessionOnly = Selector.sel_isSessionOnly.value;
public static final long /*int*/ sel_isSheet = Selector.sel_isSheet.value;
public static final long /*int*/ sel_isVisible = Selector.sel_isVisible.value;
public static final long /*int*/ sel_isZoomed = Selector.sel_isZoomed.value;
public static final long /*int*/ sel_itemArray = Selector.sel_itemArray.value;
public static final long /*int*/ sel_itemAtIndex_ = Selector.sel_itemAtIndex_.value;
public static final long /*int*/ sel_itemAtRow_ = Selector.sel_itemAtRow_.value;
public static final long /*int*/ sel_itemHeight = Selector.sel_itemHeight.value;
public static final long /*int*/ sel_itemIdentifier = Selector.sel_itemIdentifier.value;
public static final long /*int*/ sel_itemObjectValueAtIndex_ = Selector.sel_itemObjectValueAtIndex_.value;
public static final long /*int*/ sel_itemTitleAtIndex_ = Selector.sel_itemTitleAtIndex_.value;
public static final long /*int*/ sel_itemWithTag_ = Selector.sel_itemWithTag_.value;
public static final long /*int*/ sel_jobDisposition = Selector.sel_jobDisposition.value;
public static final long /*int*/ sel_keyCode = Selector.sel_keyCode.value;
public static final long /*int*/ sel_keyDown_ = Selector.sel_keyDown_.value;
public static final long /*int*/ sel_keyEquivalent = Selector.sel_keyEquivalent.value;
public static final long /*int*/ sel_keyEquivalentModifierMask = Selector.sel_keyEquivalentModifierMask.value;
public static final long /*int*/ sel_keyUp_ = Selector.sel_keyUp_.value;
public static final long /*int*/ sel_keyWindow = Selector.sel_keyWindow.value;
public static final long /*int*/ sel_knobProportion = Selector.sel_knobProportion.value;
public static final long /*int*/ sel_knobThickness = Selector.sel_knobThickness.value;
public static final long /*int*/ sel_lastPathComponent = Selector.sel_lastPathComponent.value;
public static final long /*int*/ sel_layoutManager = Selector.sel_layoutManager.value;
public static final long /*int*/ sel_leading = Selector.sel_leading.value;
public static final long /*int*/ sel_length = Selector.sel_length.value;
public static final long /*int*/ sel_level = Selector.sel_level.value;
public static final long /*int*/ sel_levelForItem_ = Selector.sel_levelForItem_.value;
public static final long /*int*/ sel_lineFragmentUsedRectForGlyphAtIndex_effectiveRange_ = Selector.sel_lineFragmentUsedRectForGlyphAtIndex_effectiveRange_.value;
public static final long /*int*/ sel_lineFragmentUsedRectForGlyphAtIndex_effectiveRange_withoutAdditionalLayout_ = Selector.sel_lineFragmentUsedRectForGlyphAtIndex_effectiveRange_withoutAdditionalLayout_.value;
public static final long /*int*/ sel_lineToPoint_ = Selector.sel_lineToPoint_.value;
public static final long /*int*/ sel_linkTextAttributes = Selector.sel_linkTextAttributes.value;
public static final long /*int*/ sel_loadHTMLString_baseURL_ = Selector.sel_loadHTMLString_baseURL_.value;
public static final long /*int*/ sel_loadNibFile_externalNameTable_withZone_ = Selector.sel_loadNibFile_externalNameTable_withZone_.value;
public static final long /*int*/ sel_loadRequest_ = Selector.sel_loadRequest_.value;
public static final long /*int*/ sel_localizedDescription = Selector.sel_localizedDescription.value;
public static final long /*int*/ sel_location = Selector.sel_location.value;
public static final long /*int*/ sel_locationForGlyphAtIndex_ = Selector.sel_locationForGlyphAtIndex_.value;
public static final long /*int*/ sel_locationInWindow = Selector.sel_locationInWindow.value;
public static final long /*int*/ sel_lockFocus = Selector.sel_lockFocus.value;
public static final long /*int*/ sel_lowercaseString = Selector.sel_lowercaseString.value;
public static final long /*int*/ sel_magnification = Selector.sel_magnification.value;
public static final long /*int*/ sel_magnifyWithEvent_ = Selector.sel_magnifyWithEvent_.value;
public static final long /*int*/ sel_mainBundle = Selector.sel_mainBundle.value;
public static final long /*int*/ sel_mainFrame = Selector.sel_mainFrame.value;
public static final long /*int*/ sel_mainMenu = Selector.sel_mainMenu.value;
public static final long /*int*/ sel_mainRunLoop = Selector.sel_mainRunLoop.value;
public static final long /*int*/ sel_mainScreen = Selector.sel_mainScreen.value;
public static final long /*int*/ sel_mainWindow = Selector.sel_mainWindow.value;
public static final long /*int*/ sel_makeCurrentContext = Selector.sel_makeCurrentContext.value;
public static final long /*int*/ sel_makeFirstResponder_ = Selector.sel_makeFirstResponder_.value;
public static final long /*int*/ sel_makeKeyAndOrderFront_ = Selector.sel_makeKeyAndOrderFront_.value;
public static final long /*int*/ sel_markedRange = Selector.sel_markedRange.value;
public static final long /*int*/ sel_markedTextAttributes = Selector.sel_markedTextAttributes.value;
public static final long /*int*/ sel_maxValue = Selector.sel_maxValue.value;
public static final long /*int*/ sel_maximum = Selector.sel_maximum.value;
public static final long /*int*/ sel_maximumFractionDigits = Selector.sel_maximumFractionDigits.value;
public static final long /*int*/ sel_maximumIntegerDigits = Selector.sel_maximumIntegerDigits.value;
public static final long /*int*/ sel_menu = Selector.sel_menu.value;
public static final long /*int*/ sel_menu_willHighlightItem_ = Selector.sel_menu_willHighlightItem_.value;
public static final long /*int*/ sel_menuBarFontOfSize_ = Selector.sel_menuBarFontOfSize_.value;
public static final long /*int*/ sel_menuDidClose_ = Selector.sel_menuDidClose_.value;
public static final long /*int*/ sel_menuFontOfSize_ = Selector.sel_menuFontOfSize_.value;
public static final long /*int*/ sel_menuForEvent_ = Selector.sel_menuForEvent_.value;
public static final long /*int*/ sel_menuNeedsUpdate_ = Selector.sel_menuNeedsUpdate_.value;
public static final long /*int*/ sel_menuWillOpen_ = Selector.sel_menuWillOpen_.value;
public static final long /*int*/ sel_metaKey = Selector.sel_metaKey.value;
public static final long /*int*/ sel_minFrameWidthWithTitle_styleMask_ = Selector.sel_minFrameWidthWithTitle_styleMask_.value;
public static final long /*int*/ sel_minSize = Selector.sel_minSize.value;
public static final long /*int*/ sel_minValue = Selector.sel_minValue.value;
public static final long /*int*/ sel_miniaturize_ = Selector.sel_miniaturize_.value;
public static final long /*int*/ sel_minimum = Selector.sel_minimum.value;
public static final long /*int*/ sel_minimumSize = Selector.sel_minimumSize.value;
public static final long /*int*/ sel_minuteOfHour = Selector.sel_minuteOfHour.value;
public static final long /*int*/ sel_modifierFlags = Selector.sel_modifierFlags.value;
public static final long /*int*/ sel_monthOfYear = Selector.sel_monthOfYear.value;
public static final long /*int*/ sel_mouse_inRect_ = Selector.sel_mouse_inRect_.value;
public static final long /*int*/ sel_mouseDown_ = Selector.sel_mouseDown_.value;
public static final long /*int*/ sel_mouseDownCanMoveWindow = Selector.sel_mouseDownCanMoveWindow.value;
public static final long /*int*/ sel_mouseDragged_ = Selector.sel_mouseDragged_.value;
public static final long /*int*/ sel_mouseEntered_ = Selector.sel_mouseEntered_.value;
public static final long /*int*/ sel_mouseExited_ = Selector.sel_mouseExited_.value;
public static final long /*int*/ sel_mouseLocation = Selector.sel_mouseLocation.value;
public static final long /*int*/ sel_mouseLocationOutsideOfEventStream = Selector.sel_mouseLocationOutsideOfEventStream.value;
public static final long /*int*/ sel_mouseMoved_ = Selector.sel_mouseMoved_.value;
public static final long /*int*/ sel_mouseUp_ = Selector.sel_mouseUp_.value;
public static final long /*int*/ sel_moveColumn_toColumn_ = Selector.sel_moveColumn_toColumn_.value;
public static final long /*int*/ sel_moveToBeginningOfParagraph_ = Selector.sel_moveToBeginningOfParagraph_.value;
public static final long /*int*/ sel_moveToEndOfParagraph_ = Selector.sel_moveToEndOfParagraph_.value;
public static final long /*int*/ sel_moveToPoint_ = Selector.sel_moveToPoint_.value;
public static final long /*int*/ sel_moveUp_ = Selector.sel_moveUp_.value;
public static final long /*int*/ sel_mutableCopy = Selector.sel_mutableCopy.value;
public static final long /*int*/ sel_mutableString = Selector.sel_mutableString.value;
public static final long /*int*/ sel_name = Selector.sel_name.value;
public static final long /*int*/ sel_needsPanelToBecomeKey = Selector.sel_needsPanelToBecomeKey.value;
public static final long /*int*/ sel_nextEventMatchingMask_untilDate_inMode_dequeue_ = Selector.sel_nextEventMatchingMask_untilDate_inMode_dequeue_.value;
public static final long /*int*/ sel_nextObject = Selector.sel_nextObject.value;
public static final long /*int*/ sel_nextState = Selector.sel_nextState.value;
public static final long /*int*/ sel_nextWordFromIndex_forward_ = Selector.sel_nextWordFromIndex_forward_.value;
public static final long /*int*/ sel_noResponderFor_ = Selector.sel_noResponderFor_.value;
public static final long /*int*/ sel_normalizedPosition = Selector.sel_normalizedPosition.value;
public static final long /*int*/ sel_noteNumberOfRowsChanged = Selector.sel_noteNumberOfRowsChanged.value;
public static final long /*int*/ sel_numberOfColumns = Selector.sel_numberOfColumns.value;
public static final long /*int*/ sel_numberOfComponents = Selector.sel_numberOfComponents.value;
public static final long /*int*/ sel_numberOfGlyphs = Selector.sel_numberOfGlyphs.value;
public static final long /*int*/ sel_numberOfItems = Selector.sel_numberOfItems.value;
public static final long /*int*/ sel_numberOfPlanes = Selector.sel_numberOfPlanes.value;
public static final long /*int*/ sel_numberOfRows = Selector.sel_numberOfRows.value;
public static final long /*int*/ sel_numberOfRowsInTableView_ = Selector.sel_numberOfRowsInTableView_.value;
public static final long /*int*/ sel_numberOfSelectedRows = Selector.sel_numberOfSelectedRows.value;
public static final long /*int*/ sel_numberOfVisibleItems = Selector.sel_numberOfVisibleItems.value;
public static final long /*int*/ sel_numberWithBool_ = Selector.sel_numberWithBool_.value;
public static final long /*int*/ sel_numberWithDouble_ = Selector.sel_numberWithDouble_.value;
public static final long /*int*/ sel_numberWithInt_ = Selector.sel_numberWithInt_.value;
public static final long /*int*/ sel_numberWithInteger_ = Selector.sel_numberWithInteger_.value;
public static final long /*int*/ sel_objCType = Selector.sel_objCType.value;
public static final long /*int*/ sel_object = Selector.sel_object.value;
public static final long /*int*/ sel_objectAtIndex_ = Selector.sel_objectAtIndex_.value;
public static final long /*int*/ sel_objectEnumerator = Selector.sel_objectEnumerator.value;
public static final long /*int*/ sel_objectForInfoDictionaryKey_ = Selector.sel_objectForInfoDictionaryKey_.value;
public static final long /*int*/ sel_objectForKey_ = Selector.sel_objectForKey_.value;
public static final long /*int*/ sel_objectValues = Selector.sel_objectValues.value;
public static final long /*int*/ sel_openFile_withApplication_ = Selector.sel_openFile_withApplication_.value;
public static final long /*int*/ sel_openPanel = Selector.sel_openPanel.value;
public static final long /*int*/ sel_openURL_ = Selector.sel_openURL_.value;
public static final long /*int*/ sel_openURLs_withAppBundleIdentifier_options_additionalEventParamDescriptor_launchIdentifiers_ = Selector.sel_openURLs_withAppBundleIdentifier_options_additionalEventParamDescriptor_launchIdentifiers_.value;
public static final long /*int*/ sel_operationNotAllowedCursor = Selector.sel_operationNotAllowedCursor.value;
public static final long /*int*/ sel_options = Selector.sel_options.value;
public static final long /*int*/ sel_orderBack_ = Selector.sel_orderBack_.value;
public static final long /*int*/ sel_orderFront_ = Selector.sel_orderFront_.value;
public static final long /*int*/ sel_orderFrontRegardless = Selector.sel_orderFrontRegardless.value;
public static final long /*int*/ sel_orderFrontStandardAboutPanel_ = Selector.sel_orderFrontStandardAboutPanel_.value;
public static final long /*int*/ sel_orderOut_ = Selector.sel_orderOut_.value;
public static final long /*int*/ sel_orderWindow_relativeTo_ = Selector.sel_orderWindow_relativeTo_.value;
public static final long /*int*/ sel_orderedWindows = Selector.sel_orderedWindows.value;
public static final long /*int*/ sel_otherEventWithType_location_modifierFlags_timestamp_windowNumber_context_subtype_data1_data2_ = Selector.sel_otherEventWithType_location_modifierFlags_timestamp_windowNumber_context_subtype_data1_data2_.value;
public static final long /*int*/ sel_otherMouseDown_ = Selector.sel_otherMouseDown_.value;
public static final long /*int*/ sel_otherMouseDragged_ = Selector.sel_otherMouseDragged_.value;
public static final long /*int*/ sel_otherMouseUp_ = Selector.sel_otherMouseUp_.value;
public static final long /*int*/ sel_outlineTableColumn = Selector.sel_outlineTableColumn.value;
public static final long /*int*/ sel_outlineView_acceptDrop_item_childIndex_ = Selector.sel_outlineView_acceptDrop_item_childIndex_.value;
public static final long /*int*/ sel_outlineView_child_ofItem_ = Selector.sel_outlineView_child_ofItem_.value;
public static final long /*int*/ sel_outlineView_didClickTableColumn_ = Selector.sel_outlineView_didClickTableColumn_.value;
public static final long /*int*/ sel_outlineView_isItemExpandable_ = Selector.sel_outlineView_isItemExpandable_.value;
public static final long /*int*/ sel_outlineView_numberOfChildrenOfItem_ = Selector.sel_outlineView_numberOfChildrenOfItem_.value;
public static final long /*int*/ sel_outlineView_objectValueForTableColumn_byItem_ = Selector.sel_outlineView_objectValueForTableColumn_byItem_.value;
public static final long /*int*/ sel_outlineView_setObjectValue_forTableColumn_byItem_ = Selector.sel_outlineView_setObjectValue_forTableColumn_byItem_.value;
public static final long /*int*/ sel_outlineView_shouldCollapseItem_ = Selector.sel_outlineView_shouldCollapseItem_.value;
public static final long /*int*/ sel_outlineView_shouldEditTableColumn_item_ = Selector.sel_outlineView_shouldEditTableColumn_item_.value;
public static final long /*int*/ sel_outlineView_shouldExpandItem_ = Selector.sel_outlineView_shouldExpandItem_.value;
public static final long /*int*/ sel_outlineView_shouldReorderColumn_toColumn_ = Selector.sel_outlineView_shouldReorderColumn_toColumn_.value;
public static final long /*int*/ sel_outlineView_shouldSelectItem_ = Selector.sel_outlineView_shouldSelectItem_.value;
public static final long /*int*/ sel_outlineView_shouldTrackCell_forTableColumn_item_ = Selector.sel_outlineView_shouldTrackCell_forTableColumn_item_.value;
public static final long /*int*/ sel_outlineView_validateDrop_proposedItem_proposedChildIndex_ = Selector.sel_outlineView_validateDrop_proposedItem_proposedChildIndex_.value;
public static final long /*int*/ sel_outlineView_willDisplayCell_forTableColumn_item_ = Selector.sel_outlineView_willDisplayCell_forTableColumn_item_.value;
public static final long /*int*/ sel_outlineView_writeItems_toPasteboard_ = Selector.sel_outlineView_writeItems_toPasteboard_.value;
public static final long /*int*/ sel_outlineViewColumnDidMove_ = Selector.sel_outlineViewColumnDidMove_.value;
public static final long /*int*/ sel_outlineViewColumnDidResize_ = Selector.sel_outlineViewColumnDidResize_.value;
public static final long /*int*/ sel_outlineViewItemDidExpand_ = Selector.sel_outlineViewItemDidExpand_.value;
public static final long /*int*/ sel_outlineViewSelectionDidChange_ = Selector.sel_outlineViewSelectionDidChange_.value;
public static final long /*int*/ sel_outlineViewSelectionIsChanging_ = Selector.sel_outlineViewSelectionIsChanging_.value;
public static final long /*int*/ sel_owner = Selector.sel_owner.value;
public static final long /*int*/ sel_pageDown_ = Selector.sel_pageDown_.value;
public static final long /*int*/ sel_pageTitle = Selector.sel_pageTitle.value;
public static final long /*int*/ sel_pageUp_ = Selector.sel_pageUp_.value;
public static final long /*int*/ sel_panel_shouldShowFilename_ = Selector.sel_panel_shouldShowFilename_.value;
public static final long /*int*/ sel_panelConvertFont_ = Selector.sel_panelConvertFont_.value;
public static final long /*int*/ sel_paperSize = Selector.sel_paperSize.value;
public static final long /*int*/ sel_paragraphs = Selector.sel_paragraphs.value;
public static final long /*int*/ sel_parentWindow = Selector.sel_parentWindow.value;
public static final long /*int*/ sel_password = Selector.sel_password.value;
public static final long /*int*/ sel_paste_ = Selector.sel_paste_.value;
public static final long /*int*/ sel_pasteboard_provideDataForType_ = Selector.sel_pasteboard_provideDataForType_.value;
public static final long /*int*/ sel_pasteboardWithName_ = Selector.sel_pasteboardWithName_.value;
public static final long /*int*/ sel_path = Selector.sel_path.value;
public static final long /*int*/ sel_pathExtension = Selector.sel_pathExtension.value;
public static final long /*int*/ sel_pathForResource_ofType_ = Selector.sel_pathForResource_ofType_.value;
public static final long /*int*/ sel_pathForResource_ofType_inDirectory_forLocalization_ = Selector.sel_pathForResource_ofType_inDirectory_forLocalization_.value;
public static final long /*int*/ sel_performDragOperation_ = Selector.sel_performDragOperation_.value;
public static final long /*int*/ sel_performKeyEquivalent_ = Selector.sel_performKeyEquivalent_.value;
public static final long /*int*/ sel_performSelector_withObject_afterDelay_inModes_ = Selector.sel_performSelector_withObject_afterDelay_inModes_.value;
public static final long /*int*/ sel_performSelectorOnMainThread_withObject_waitUntilDone_ = Selector.sel_performSelectorOnMainThread_withObject_waitUntilDone_.value;
public static final long /*int*/ sel_phase = Selector.sel_phase.value;
public static final long /*int*/ sel_pixelsHigh = Selector.sel_pixelsHigh.value;
public static final long /*int*/ sel_pixelsWide = Selector.sel_pixelsWide.value;
public static final long /*int*/ sel_pointSize = Selector.sel_pointSize.value;
public static final long /*int*/ sel_pointValue = Selector.sel_pointValue.value;
public static final long /*int*/ sel_pointingHandCursor = Selector.sel_pointingHandCursor.value;
public static final long /*int*/ sel_pop = Selector.sel_pop.value;
public static final long /*int*/ sel_popUpContextMenu_withEvent_forView_ = Selector.sel_popUpContextMenu_withEvent_forView_.value;
public static final long /*int*/ sel_popUpStatusItemMenu_ = Selector.sel_popUpStatusItemMenu_.value;
public static final long /*int*/ sel_port = Selector.sel_port.value;
public static final long /*int*/ sel_postEvent_atStart_ = Selector.sel_postEvent_atStart_.value;
public static final long /*int*/ sel_preparedCellAtColumn_row_ = Selector.sel_preparedCellAtColumn_row_.value;
public static final long /*int*/ sel_prependTransform_ = Selector.sel_prependTransform_.value;
public static final long /*int*/ sel_preventDefault = Selector.sel_preventDefault.value;
public static final long /*int*/ sel_previousFailureCount = Selector.sel_previousFailureCount.value;
public static final long /*int*/ sel_printDocumentView = Selector.sel_printDocumentView.value;
public static final long /*int*/ sel_printOperationWithPrintInfo_ = Selector.sel_printOperationWithPrintInfo_.value;
public static final long /*int*/ sel_printOperationWithView_printInfo_ = Selector.sel_printOperationWithView_printInfo_.value;
public static final long /*int*/ sel_printPanel = Selector.sel_printPanel.value;
public static final long /*int*/ sel_printSettings = Selector.sel_printSettings.value;
public static final long /*int*/ sel_printer = Selector.sel_printer.value;
public static final long /*int*/ sel_printerNames = Selector.sel_printerNames.value;
public static final long /*int*/ sel_printerWithName_ = Selector.sel_printerWithName_.value;
public static final long /*int*/ sel_propertyListForType_ = Selector.sel_propertyListForType_.value;
public static final long /*int*/ sel_proposedCredential = Selector.sel_proposedCredential.value;
public static final long /*int*/ sel_protectionSpace = Selector.sel_protectionSpace.value;
public static final long /*int*/ sel_push = Selector.sel_push.value;
public static final long /*int*/ sel_rangeValue = Selector.sel_rangeValue.value;
public static final long /*int*/ sel_readSelectionFromPasteboard_ = Selector.sel_readSelectionFromPasteboard_.value;
public static final long /*int*/ sel_realm = Selector.sel_realm.value;
public static final long /*int*/ sel_recentSearches = Selector.sel_recentSearches.value;
public static final long /*int*/ sel_rectArrayForCharacterRange_withinSelectedCharacterRange_inTextContainer_rectCount_ = Selector.sel_rectArrayForCharacterRange_withinSelectedCharacterRange_inTextContainer_rectCount_.value;
public static final long /*int*/ sel_rectArrayForGlyphRange_withinSelectedGlyphRange_inTextContainer_rectCount_ = Selector.sel_rectArrayForGlyphRange_withinSelectedGlyphRange_inTextContainer_rectCount_.value;
public static final long /*int*/ sel_rectForPart_ = Selector.sel_rectForPart_.value;
public static final long /*int*/ sel_rectOfColumn_ = Selector.sel_rectOfColumn_.value;
public static final long /*int*/ sel_rectOfRow_ = Selector.sel_rectOfRow_.value;
public static final long /*int*/ sel_rectValue = Selector.sel_rectValue.value;
public static final long /*int*/ sel_redComponent = Selector.sel_redComponent.value;
public static final long /*int*/ sel_redo = Selector.sel_redo.value;
public static final long /*int*/ sel_reflectScrolledClipView_ = Selector.sel_reflectScrolledClipView_.value;
public static final long /*int*/ sel_registerForDraggedTypes_ = Selector.sel_registerForDraggedTypes_.value;
public static final long /*int*/ sel_release = Selector.sel_release.value;
public static final long /*int*/ sel_reload_ = Selector.sel_reload_.value;
public static final long /*int*/ sel_reloadData = Selector.sel_reloadData.value;
public static final long /*int*/ sel_reloadItem_reloadChildren_ = Selector.sel_reloadItem_reloadChildren_.value;
public static final long /*int*/ sel_removeAllItems = Selector.sel_removeAllItems.value;
public static final long /*int*/ sel_removeAllPoints = Selector.sel_removeAllPoints.value;
public static final long /*int*/ sel_removeAttribute_range_ = Selector.sel_removeAttribute_range_.value;
public static final long /*int*/ sel_removeChildWindow_ = Selector.sel_removeChildWindow_.value;
public static final long /*int*/ sel_removeColorWithKey_ = Selector.sel_removeColorWithKey_.value;
public static final long /*int*/ sel_removeFromSuperview = Selector.sel_removeFromSuperview.value;
public static final long /*int*/ sel_removeIndex_ = Selector.sel_removeIndex_.value;
public static final long /*int*/ sel_removeItem_ = Selector.sel_removeItem_.value;
public static final long /*int*/ sel_removeItemAtIndex_ = Selector.sel_removeItemAtIndex_.value;
public static final long /*int*/ sel_removeItemAtPath_error_ = Selector.sel_removeItemAtPath_error_.value;
public static final long /*int*/ sel_removeLastObject = Selector.sel_removeLastObject.value;
public static final long /*int*/ sel_removeObject_ = Selector.sel_removeObject_.value;
public static final long /*int*/ sel_removeObjectAtIndex_ = Selector.sel_removeObjectAtIndex_.value;
public static final long /*int*/ sel_removeObjectForKey_ = Selector.sel_removeObjectForKey_.value;
public static final long /*int*/ sel_removeObjectIdenticalTo_ = Selector.sel_removeObjectIdenticalTo_.value;
public static final long /*int*/ sel_removeObserver_ = Selector.sel_removeObserver_.value;
public static final long /*int*/ sel_removeObserver_name_object_ = Selector.sel_removeObserver_name_object_.value;
public static final long /*int*/ sel_removeRepresentation_ = Selector.sel_removeRepresentation_.value;
public static final long /*int*/ sel_removeStatusItem_ = Selector.sel_removeStatusItem_.value;
public static final long /*int*/ sel_removeTabViewItem_ = Selector.sel_removeTabViewItem_.value;
public static final long /*int*/ sel_removeTableColumn_ = Selector.sel_removeTableColumn_.value;
public static final long /*int*/ sel_removeTemporaryAttribute_forCharacterRange_ = Selector.sel_removeTemporaryAttribute_forCharacterRange_.value;
public static final long /*int*/ sel_removeToolTip_ = Selector.sel_removeToolTip_.value;
public static final long /*int*/ sel_removeTrackingArea_ = Selector.sel_removeTrackingArea_.value;
public static final long /*int*/ sel_replaceCharactersInRange_withString_ = Selector.sel_replaceCharactersInRange_withString_.value;
public static final long /*int*/ sel_replyToOpenOrPrint_ = Selector.sel_replyToOpenOrPrint_.value;
public static final long /*int*/ sel_representation = Selector.sel_representation.value;
public static final long /*int*/ sel_representations = Selector.sel_representations.value;
public static final long /*int*/ sel_request = Selector.sel_request.value;
public static final long /*int*/ sel_requestWithURL_ = Selector.sel_requestWithURL_.value;
public static final long /*int*/ sel_resetCursorRects = Selector.sel_resetCursorRects.value;
public static final long /*int*/ sel_resignFirstResponder = Selector.sel_resignFirstResponder.value;
public static final long /*int*/ sel_resizeDownCursor = Selector.sel_resizeDownCursor.value;
public static final long /*int*/ sel_resizeLeftCursor = Selector.sel_resizeLeftCursor.value;
public static final long /*int*/ sel_resizeLeftRightCursor = Selector.sel_resizeLeftRightCursor.value;
public static final long /*int*/ sel_resizeRightCursor = Selector.sel_resizeRightCursor.value;
public static final long /*int*/ sel_resizeUpCursor = Selector.sel_resizeUpCursor.value;
public static final long /*int*/ sel_resizeUpDownCursor = Selector.sel_resizeUpDownCursor.value;
public static final long /*int*/ sel_resizingMask = Selector.sel_resizingMask.value;
public static final long /*int*/ sel_resourcePath = Selector.sel_resourcePath.value;
public static final long /*int*/ sel_respondsToSelector_ = Selector.sel_respondsToSelector_.value;
public static final long /*int*/ sel_restoreGraphicsState = Selector.sel_restoreGraphicsState.value;
public static final long /*int*/ sel_retain = Selector.sel_retain.value;
public static final long /*int*/ sel_retainCount = Selector.sel_retainCount.value;
public static final long /*int*/ sel_rightMouseDown_ = Selector.sel_rightMouseDown_.value;
public static final long /*int*/ sel_rightMouseDragged_ = Selector.sel_rightMouseDragged_.value;
public static final long /*int*/ sel_rightMouseUp_ = Selector.sel_rightMouseUp_.value;
public static final long /*int*/ sel_rotateByDegrees_ = Selector.sel_rotateByDegrees_.value;
public static final long /*int*/ sel_rotateWithEvent_ = Selector.sel_rotateWithEvent_.value;
public static final long /*int*/ sel_rotation = Selector.sel_rotation.value;
public static final long /*int*/ sel_rowAtPoint_ = Selector.sel_rowAtPoint_.value;
public static final long /*int*/ sel_rowForItem_ = Selector.sel_rowForItem_.value;
public static final long /*int*/ sel_rowHeight = Selector.sel_rowHeight.value;
public static final long /*int*/ sel_rowsInRect_ = Selector.sel_rowsInRect_.value;
public static final long /*int*/ sel_run = Selector.sel_run.value;
public static final long /*int*/ sel_runModal = Selector.sel_runModal.value;
public static final long /*int*/ sel_runModalForDirectory_file_ = Selector.sel_runModalForDirectory_file_.value;
public static final long /*int*/ sel_runModalForWindow_ = Selector.sel_runModalForWindow_.value;
public static final long /*int*/ sel_runModalWithPrintInfo_ = Selector.sel_runModalWithPrintInfo_.value;
public static final long /*int*/ sel_runMode_beforeDate_ = Selector.sel_runMode_beforeDate_.value;
public static final long /*int*/ sel_runOperation = Selector.sel_runOperation.value;
public static final long /*int*/ sel_samplesPerPixel = Selector.sel_samplesPerPixel.value;
public static final long /*int*/ sel_saveGraphicsState = Selector.sel_saveGraphicsState.value;
public static final long /*int*/ sel_savePanel = Selector.sel_savePanel.value;
public static final long /*int*/ sel_scaleXBy_yBy_ = Selector.sel_scaleXBy_yBy_.value;
public static final long /*int*/ sel_scheduledTimerWithTimeInterval_target_selector_userInfo_repeats_ = Selector.sel_scheduledTimerWithTimeInterval_target_selector_userInfo_repeats_.value;
public static final long /*int*/ sel_screen = Selector.sel_screen.value;
public static final long /*int*/ sel_screenX = Selector.sel_screenX.value;
public static final long /*int*/ sel_screenY = Selector.sel_screenY.value;
public static final long /*int*/ sel_screens = Selector.sel_screens.value;
public static final long /*int*/ sel_scrollClipView_toPoint_ = Selector.sel_scrollClipView_toPoint_.value;
public static final long /*int*/ sel_scrollColumnToVisible_ = Selector.sel_scrollColumnToVisible_.value;
public static final long /*int*/ sel_scrollPoint_ = Selector.sel_scrollPoint_.value;
public static final long /*int*/ sel_scrollRangeToVisible_ = Selector.sel_scrollRangeToVisible_.value;
public static final long /*int*/ sel_scrollRect_by_ = Selector.sel_scrollRect_by_.value;
public static final long /*int*/ sel_scrollRectToVisible_ = Selector.sel_scrollRectToVisible_.value;
public static final long /*int*/ sel_scrollRowToVisible_ = Selector.sel_scrollRowToVisible_.value;
public static final long /*int*/ sel_scrollToPoint_ = Selector.sel_scrollToPoint_.value;
public static final long /*int*/ sel_scrollWheel_ = Selector.sel_scrollWheel_.value;
public static final long /*int*/ sel_scrollerWidth = Selector.sel_scrollerWidth.value;
public static final long /*int*/ sel_scrollerWidthForControlSize_ = Selector.sel_scrollerWidthForControlSize_.value;
public static final long /*int*/ sel_searchButtonCell = Selector.sel_searchButtonCell.value;
public static final long /*int*/ sel_searchButtonRectForBounds_ = Selector.sel_searchButtonRectForBounds_.value;
public static final long /*int*/ sel_searchTextRectForBounds_ = Selector.sel_searchTextRectForBounds_.value;
public static final long /*int*/ sel_secondOfMinute = Selector.sel_secondOfMinute.value;
public static final long /*int*/ sel_secondarySelectedControlColor = Selector.sel_secondarySelectedControlColor.value;
public static final long /*int*/ sel_selectAll_ = Selector.sel_selectAll_.value;
public static final long /*int*/ sel_selectItem_ = Selector.sel_selectItem_.value;
public static final long /*int*/ sel_selectItemAtIndex_ = Selector.sel_selectItemAtIndex_.value;
public static final long /*int*/ sel_selectRowIndexes_byExtendingSelection_ = Selector.sel_selectRowIndexes_byExtendingSelection_.value;
public static final long /*int*/ sel_selectTabViewItemAtIndex_ = Selector.sel_selectTabViewItemAtIndex_.value;
public static final long /*int*/ sel_selectText_ = Selector.sel_selectText_.value;
public static final long /*int*/ sel_selectedControlColor = Selector.sel_selectedControlColor.value;
public static final long /*int*/ sel_selectedControlTextColor = Selector.sel_selectedControlTextColor.value;
public static final long /*int*/ sel_selectedRange = Selector.sel_selectedRange.value;
public static final long /*int*/ sel_selectedRow = Selector.sel_selectedRow.value;
public static final long /*int*/ sel_selectedRowIndexes = Selector.sel_selectedRowIndexes.value;
public static final long /*int*/ sel_selectedTabViewItem = Selector.sel_selectedTabViewItem.value;
public static final long /*int*/ sel_selectedTextAttributes = Selector.sel_selectedTextAttributes.value;
public static final long /*int*/ sel_selectedTextBackgroundColor = Selector.sel_selectedTextBackgroundColor.value;
public static final long /*int*/ sel_selectedTextColor = Selector.sel_selectedTextColor.value;
public static final long /*int*/ sel_sendAction_to_ = Selector.sel_sendAction_to_.value;
public static final long /*int*/ sel_sendAction_to_from_ = Selector.sel_sendAction_to_from_.value;
public static final long /*int*/ sel_sendEvent_ = Selector.sel_sendEvent_.value;
public static final long /*int*/ sel_sender = Selector.sel_sender.value;
public static final long /*int*/ sel_separatorItem = Selector.sel_separatorItem.value;
public static final long /*int*/ sel_set = Selector.sel_set.value;
public static final long /*int*/ sel_setAcceptsMouseMovedEvents_ = Selector.sel_setAcceptsMouseMovedEvents_.value;
public static final long /*int*/ sel_setAcceptsTouchEvents_ = Selector.sel_setAcceptsTouchEvents_.value;
public static final long /*int*/ sel_setAccessoryView_ = Selector.sel_setAccessoryView_.value;
public static final long /*int*/ sel_setAction_ = Selector.sel_setAction_.value;
public static final long /*int*/ sel_setActivationPolicy_ = Selector.sel_setActivationPolicy_.value;
public static final long /*int*/ sel_setAlertStyle_ = Selector.sel_setAlertStyle_.value;
public static final long /*int*/ sel_setAlignment_ = Selector.sel_setAlignment_.value;
public static final long /*int*/ sel_setAllowedFileTypes_ = Selector.sel_setAllowedFileTypes_.value;
public static final long /*int*/ sel_setAllowsColumnReordering_ = Selector.sel_setAllowsColumnReordering_.value;
public static final long /*int*/ sel_setAllowsFloats_ = Selector.sel_setAllowsFloats_.value;
public static final long /*int*/ sel_setAllowsMixedState_ = Selector.sel_setAllowsMixedState_.value;
public static final long /*int*/ sel_setAllowsMultipleSelection_ = Selector.sel_setAllowsMultipleSelection_.value;
public static final long /*int*/ sel_setAllowsOtherFileTypes_ = Selector.sel_setAllowsOtherFileTypes_.value;
public static final long /*int*/ sel_setAllowsUndo_ = Selector.sel_setAllowsUndo_.value;
public static final long /*int*/ sel_setAllowsUserCustomization_ = Selector.sel_setAllowsUserCustomization_.value;
public static final long /*int*/ sel_setAlpha_ = Selector.sel_setAlpha_.value;
public static final long /*int*/ sel_setAlphaValue_ = Selector.sel_setAlphaValue_.value;
public static final long /*int*/ sel_setAlternateButtonTitle_ = Selector.sel_setAlternateButtonTitle_.value;
public static final long /*int*/ sel_setAppearance_ = Selector.sel_setAppearance_.value;
public static final long /*int*/ sel_setApplicationIconImage_ = Selector.sel_setApplicationIconImage_.value;
public static final long /*int*/ sel_setApplicationNameForUserAgent_ = Selector.sel_setApplicationNameForUserAgent_.value;
public static final long /*int*/ sel_setAttachmentCell_ = Selector.sel_setAttachmentCell_.value;
public static final long /*int*/ sel_setAttributedString_ = Selector.sel_setAttributedString_.value;
public static final long /*int*/ sel_setAttributedStringValue_ = Selector.sel_setAttributedStringValue_.value;
public static final long /*int*/ sel_setAttributedTitle_ = Selector.sel_setAttributedTitle_.value;
public static final long /*int*/ sel_setAutoenablesItems_ = Selector.sel_setAutoenablesItems_.value;
public static final long /*int*/ sel_setAutohidesScrollers_ = Selector.sel_setAutohidesScrollers_.value;
public static final long /*int*/ sel_setAutoresizesOutlineColumn_ = Selector.sel_setAutoresizesOutlineColumn_.value;
public static final long /*int*/ sel_setAutoresizesSubviews_ = Selector.sel_setAutoresizesSubviews_.value;
public static final long /*int*/ sel_setAutoresizingMask_ = Selector.sel_setAutoresizingMask_.value;
public static final long /*int*/ sel_setAutosaveExpandedItems_ = Selector.sel_setAutosaveExpandedItems_.value;
public static final long /*int*/ sel_setBackgroundColor_ = Selector.sel_setBackgroundColor_.value;
public static final long /*int*/ sel_setBackgroundLayoutEnabled_ = Selector.sel_setBackgroundLayoutEnabled_.value;
public static final long /*int*/ sel_setBackgroundStyle_ = Selector.sel_setBackgroundStyle_.value;
public static final long /*int*/ sel_setBadgeLabel_ = Selector.sel_setBadgeLabel_.value;
public static final long /*int*/ sel_setBaseWritingDirection_ = Selector.sel_setBaseWritingDirection_.value;
public static final long /*int*/ sel_setBaseWritingDirection_range_ = Selector.sel_setBaseWritingDirection_range_.value;
public static final long /*int*/ sel_setBecomesKeyOnlyIfNeeded_ = Selector.sel_setBecomesKeyOnlyIfNeeded_.value;
public static final long /*int*/ sel_setBezelStyle_ = Selector.sel_setBezelStyle_.value;
public static final long /*int*/ sel_setBezeled_ = Selector.sel_setBezeled_.value;
public static final long /*int*/ sel_setBorderType_ = Selector.sel_setBorderType_.value;
public static final long /*int*/ sel_setBorderWidth_ = Selector.sel_setBorderWidth_.value;
public static final long /*int*/ sel_setBordered_ = Selector.sel_setBordered_.value;
public static final long /*int*/ sel_setBoundsRotation_ = Selector.sel_setBoundsRotation_.value;
public static final long /*int*/ sel_setBoxType_ = Selector.sel_setBoxType_.value;
public static final long /*int*/ sel_setButtonType_ = Selector.sel_setButtonType_.value;
public static final long /*int*/ sel_setCacheMode_ = Selector.sel_setCacheMode_.value;
public static final long /*int*/ sel_setCachePolicy_ = Selector.sel_setCachePolicy_.value;
public static final long /*int*/ sel_setCanChooseDirectories_ = Selector.sel_setCanChooseDirectories_.value;
public static final long /*int*/ sel_setCanChooseFiles_ = Selector.sel_setCanChooseFiles_.value;
public static final long /*int*/ sel_setCanCreateDirectories_ = Selector.sel_setCanCreateDirectories_.value;
public static final long /*int*/ sel_setCancelButtonCell_ = Selector.sel_setCancelButtonCell_.value;
public static final long /*int*/ sel_setCell_ = Selector.sel_setCell_.value;
public static final long /*int*/ sel_setCellClass_ = Selector.sel_setCellClass_.value;
public static final long /*int*/ sel_setClip = Selector.sel_setClip.value;
public static final long /*int*/ sel_setCollectionBehavior_ = Selector.sel_setCollectionBehavior_.value;
public static final long /*int*/ sel_setColor_ = Selector.sel_setColor_.value;
public static final long /*int*/ sel_setColumnAutoresizingStyle_ = Selector.sel_setColumnAutoresizingStyle_.value;
public static final long /*int*/ sel_setCompositingOperation_ = Selector.sel_setCompositingOperation_.value;
public static final long /*int*/ sel_setContainerSize_ = Selector.sel_setContainerSize_.value;
public static final long /*int*/ sel_setContentView_ = Selector.sel_setContentView_.value;
public static final long /*int*/ sel_setContentViewMargins_ = Selector.sel_setContentViewMargins_.value;
public static final long /*int*/ sel_setControlSize_ = Selector.sel_setControlSize_.value;
public static final long /*int*/ sel_setCookie_ = Selector.sel_setCookie_.value;
public static final long /*int*/ sel_setCopiesOnScroll_ = Selector.sel_setCopiesOnScroll_.value;
public static final long /*int*/ sel_setCurrentContext_ = Selector.sel_setCurrentContext_.value;
public static final long /*int*/ sel_setCurrentOperation_ = Selector.sel_setCurrentOperation_.value;
public static final long /*int*/ sel_setCustomUserAgent_ = Selector.sel_setCustomUserAgent_.value;
public static final long /*int*/ sel_setData_forType_ = Selector.sel_setData_forType_.value;
public static final long /*int*/ sel_setDataCell_ = Selector.sel_setDataCell_.value;
public static final long /*int*/ sel_setDataSource_ = Selector.sel_setDataSource_.value;
public static final long /*int*/ sel_setDatePickerElements_ = Selector.sel_setDatePickerElements_.value;
public static final long /*int*/ sel_setDatePickerStyle_ = Selector.sel_setDatePickerStyle_.value;
public static final long /*int*/ sel_setDateValue_ = Selector.sel_setDateValue_.value;
public static final long /*int*/ sel_setDefaultButtonCell_ = Selector.sel_setDefaultButtonCell_.value;
public static final long /*int*/ sel_setDefaultFlatness_ = Selector.sel_setDefaultFlatness_.value;
public static final long /*int*/ sel_setDefaultParagraphStyle_ = Selector.sel_setDefaultParagraphStyle_.value;
public static final long /*int*/ sel_setDefaultTabInterval_ = Selector.sel_setDefaultTabInterval_.value;
public static final long /*int*/ sel_setDelegate_ = Selector.sel_setDelegate_.value;
public static final long /*int*/ sel_setDestination_allowOverwrite_ = Selector.sel_setDestination_allowOverwrite_.value;
public static final long /*int*/ sel_setDictionary_ = Selector.sel_setDictionary_.value;
public static final long /*int*/ sel_setDirectory_ = Selector.sel_setDirectory_.value;
public static final long /*int*/ sel_setDirectoryURL_ = Selector.sel_setDirectoryURL_.value;
public static final long /*int*/ sel_setDisplayMode_ = Selector.sel_setDisplayMode_.value;
public static final long /*int*/ sel_setDisplaysLinkToolTips_ = Selector.sel_setDisplaysLinkToolTips_.value;
public static final long /*int*/ sel_setDocumentCursor_ = Selector.sel_setDocumentCursor_.value;
public static final long /*int*/ sel_setDocumentEdited_ = Selector.sel_setDocumentEdited_.value;
public static final long /*int*/ sel_setDocumentView_ = Selector.sel_setDocumentView_.value;
public static final long /*int*/ sel_setDoubleAction_ = Selector.sel_setDoubleAction_.value;
public static final long /*int*/ sel_setDoubleValue_ = Selector.sel_setDoubleValue_.value;
public static final long /*int*/ sel_setDownloadDelegate_ = Selector.sel_setDownloadDelegate_.value;
public static final long /*int*/ sel_setDrawsBackground_ = Selector.sel_setDrawsBackground_.value;
public static final long /*int*/ sel_setDropItem_dropChildIndex_ = Selector.sel_setDropItem_dropChildIndex_.value;
public static final long /*int*/ sel_setDropRow_dropOperation_ = Selector.sel_setDropRow_dropOperation_.value;
public static final long /*int*/ sel_setEditable_ = Selector.sel_setEditable_.value;
public static final long /*int*/ sel_setEnabled_ = Selector.sel_setEnabled_.value;
public static final long /*int*/ sel_setEnabled_forSegment_ = Selector.sel_setEnabled_forSegment_.value;
public static final long /*int*/ sel_setFill = Selector.sel_setFill.value;
public static final long /*int*/ sel_setFillColor_ = Selector.sel_setFillColor_.value;
public static final long /*int*/ sel_setFireDate_ = Selector.sel_setFireDate_.value;
public static final long /*int*/ sel_setFirstLineHeadIndent_ = Selector.sel_setFirstLineHeadIndent_.value;
public static final long /*int*/ sel_setFloatingPanel_ = Selector.sel_setFloatingPanel_.value;
public static final long /*int*/ sel_setFocusRingType_ = Selector.sel_setFocusRingType_.value;
public static final long /*int*/ sel_setFont_ = Selector.sel_setFont_.value;
public static final long /*int*/ sel_setFormatter_ = Selector.sel_setFormatter_.value;
public static final long /*int*/ sel_setFrame_ = Selector.sel_setFrame_.value;
public static final long /*int*/ sel_setFrame_display_ = Selector.sel_setFrame_display_.value;
public static final long /*int*/ sel_setFrame_display_animate_ = Selector.sel_setFrame_display_animate_.value;
public static final long /*int*/ sel_setFrameFromContentFrame_ = Selector.sel_setFrameFromContentFrame_.value;
public static final long /*int*/ sel_setFrameLoadDelegate_ = Selector.sel_setFrameLoadDelegate_.value;
public static final long /*int*/ sel_setFrameOrigin_ = Selector.sel_setFrameOrigin_.value;
public static final long /*int*/ sel_setFrameSize_ = Selector.sel_setFrameSize_.value;
public static final long /*int*/ sel_setGridStyleMask_ = Selector.sel_setGridStyleMask_.value;
public static final long /*int*/ sel_setHTTPBody_ = Selector.sel_setHTTPBody_.value;
public static final long /*int*/ sel_setHTTPMethod_ = Selector.sel_setHTTPMethod_.value;
public static final long /*int*/ sel_setHasHorizontalScroller_ = Selector.sel_setHasHorizontalScroller_.value;
public static final long /*int*/ sel_setHasShadow_ = Selector.sel_setHasShadow_.value;
public static final long /*int*/ sel_setHasVerticalScroller_ = Selector.sel_setHasVerticalScroller_.value;
public static final long /*int*/ sel_setHeadIndent_ = Selector.sel_setHeadIndent_.value;
public static final long /*int*/ sel_setHeaderCell_ = Selector.sel_setHeaderCell_.value;
public static final long /*int*/ sel_setHeaderView_ = Selector.sel_setHeaderView_.value;
public static final long /*int*/ sel_setHelpMenu_ = Selector.sel_setHelpMenu_.value;
public static final long /*int*/ sel_setHidden_ = Selector.sel_setHidden_.value;
public static final long /*int*/ sel_setHiddenUntilMouseMoves_ = Selector.sel_setHiddenUntilMouseMoves_.value;
public static final long /*int*/ sel_setHidesOnDeactivate_ = Selector.sel_setHidesOnDeactivate_.value;
public static final long /*int*/ sel_setHighlightMode_ = Selector.sel_setHighlightMode_.value;
public static final long /*int*/ sel_setHighlighted_ = Selector.sel_setHighlighted_.value;
public static final long /*int*/ sel_setHighlightedTableColumn_ = Selector.sel_setHighlightedTableColumn_.value;
public static final long /*int*/ sel_setHighlightsBy_ = Selector.sel_setHighlightsBy_.value;
public static final long /*int*/ sel_setHorizontalScroller_ = Selector.sel_setHorizontalScroller_.value;
public static final long /*int*/ sel_setHorizontallyResizable_ = Selector.sel_setHorizontallyResizable_.value;
public static final long /*int*/ sel_setIcon_ = Selector.sel_setIcon_.value;
public static final long /*int*/ sel_setIdentifier_ = Selector.sel_setIdentifier_.value;
public static final long /*int*/ sel_setImage_ = Selector.sel_setImage_.value;
public static final long /*int*/ sel_setImage_forSegment_ = Selector.sel_setImage_forSegment_.value;
public static final long /*int*/ sel_setImageAlignment_ = Selector.sel_setImageAlignment_.value;
public static final long /*int*/ sel_setImageInterpolation_ = Selector.sel_setImageInterpolation_.value;
public static final long /*int*/ sel_setImagePosition_ = Selector.sel_setImagePosition_.value;
public static final long /*int*/ sel_setImageScaling_ = Selector.sel_setImageScaling_.value;
public static final long /*int*/ sel_setIncrement_ = Selector.sel_setIncrement_.value;
public static final long /*int*/ sel_setIndeterminate_ = Selector.sel_setIndeterminate_.value;
public static final long /*int*/ sel_setIndicatorImage_inTableColumn_ = Selector.sel_setIndicatorImage_inTableColumn_.value;
public static final long /*int*/ sel_setInteger_forKey_ = Selector.sel_setInteger_forKey_.value;
public static final long /*int*/ sel_setIntercellSpacing_ = Selector.sel_setIntercellSpacing_.value;
public static final long /*int*/ sel_setJavaEnabled_ = Selector.sel_setJavaEnabled_.value;
public static final long /*int*/ sel_setJavaScriptEnabled_ = Selector.sel_setJavaScriptEnabled_.value;
public static final long /*int*/ sel_setJobDisposition_ = Selector.sel_setJobDisposition_.value;
public static final long /*int*/ sel_setJobTitle_ = Selector.sel_setJobTitle_.value;
public static final long /*int*/ sel_setKeyEquivalent_ = Selector.sel_setKeyEquivalent_.value;
public static final long /*int*/ sel_setKeyEquivalentModifierMask_ = Selector.sel_setKeyEquivalentModifierMask_.value;
public static final long /*int*/ sel_setKnobProportion_ = Selector.sel_setKnobProportion_.value;
public static final long /*int*/ sel_setLabel_ = Selector.sel_setLabel_.value;
public static final long /*int*/ sel_setLabel_forSegment_ = Selector.sel_setLabel_forSegment_.value;
public static final long /*int*/ sel_setLeaf_ = Selector.sel_setLeaf_.value;
public static final long /*int*/ sel_setLength_ = Selector.sel_setLength_.value;
public static final long /*int*/ sel_setLevel_ = Selector.sel_setLevel_.value;
public static final long /*int*/ sel_setLineBreakMode_ = Selector.sel_setLineBreakMode_.value;
public static final long /*int*/ sel_setLineCapStyle_ = Selector.sel_setLineCapStyle_.value;
public static final long /*int*/ sel_setLineDash_count_phase_ = Selector.sel_setLineDash_count_phase_.value;
public static final long /*int*/ sel_setLineFragmentPadding_ = Selector.sel_setLineFragmentPadding_.value;
public static final long /*int*/ sel_setLineFragmentRect_forGlyphRange_usedRect_ = Selector.sel_setLineFragmentRect_forGlyphRange_usedRect_.value;
public static final long /*int*/ sel_setLineJoinStyle_ = Selector.sel_setLineJoinStyle_.value;
public static final long /*int*/ sel_setLineSpacing_ = Selector.sel_setLineSpacing_.value;
public static final long /*int*/ sel_setLineWidth_ = Selector.sel_setLineWidth_.value;
public static final long /*int*/ sel_setLinkTextAttributes_ = Selector.sel_setLinkTextAttributes_.value;
public static final long /*int*/ sel_setMainMenu_ = Selector.sel_setMainMenu_.value;
public static final long /*int*/ sel_setMarkedText_selectedRange_ = Selector.sel_setMarkedText_selectedRange_.value;
public static final long /*int*/ sel_setMaxSize_ = Selector.sel_setMaxSize_.value;
public static final long /*int*/ sel_setMaxValue_ = Selector.sel_setMaxValue_.value;
public static final long /*int*/ sel_setMaximum_ = Selector.sel_setMaximum_.value;
public static final long /*int*/ sel_setMaximumFractionDigits_ = Selector.sel_setMaximumFractionDigits_.value;
public static final long /*int*/ sel_setMaximumIntegerDigits_ = Selector.sel_setMaximumIntegerDigits_.value;
public static final long /*int*/ sel_setMenu_ = Selector.sel_setMenu_.value;
public static final long /*int*/ sel_setMenu_forSegment_ = Selector.sel_setMenu_forSegment_.value;
public static final long /*int*/ sel_setMenuFormRepresentation_ = Selector.sel_setMenuFormRepresentation_.value;
public static final long /*int*/ sel_setMessage_ = Selector.sel_setMessage_.value;
public static final long /*int*/ sel_setMessageText_ = Selector.sel_setMessageText_.value;
public static final long /*int*/ sel_setMinSize_ = Selector.sel_setMinSize_.value;
public static final long /*int*/ sel_setMinValue_ = Selector.sel_setMinValue_.value;
public static final long /*int*/ sel_setMinWidth_ = Selector.sel_setMinWidth_.value;
public static final long /*int*/ sel_setMinimum_ = Selector.sel_setMinimum_.value;
public static final long /*int*/ sel_setMinimumFractionDigits_ = Selector.sel_setMinimumFractionDigits_.value;
public static final long /*int*/ sel_setMinimumIntegerDigits_ = Selector.sel_setMinimumIntegerDigits_.value;
public static final long /*int*/ sel_setMiterLimit_ = Selector.sel_setMiterLimit_.value;
public static final long /*int*/ sel_setMovable_ = Selector.sel_setMovable_.value;
public static final long /*int*/ sel_setNameFieldStringValue_ = Selector.sel_setNameFieldStringValue_.value;
public static final long /*int*/ sel_setNeedsDisplay_ = Selector.sel_setNeedsDisplay_.value;
public static final long /*int*/ sel_setNeedsDisplayInRect_ = Selector.sel_setNeedsDisplayInRect_.value;
public static final long /*int*/ sel_setNumberOfVisibleItems_ = Selector.sel_setNumberOfVisibleItems_.value;
public static final long /*int*/ sel_setNumberStyle_ = Selector.sel_setNumberStyle_.value;
public static final long /*int*/ sel_setObject_forKey_ = Selector.sel_setObject_forKey_.value;
public static final long /*int*/ sel_setObjectValue_ = Selector.sel_setObjectValue_.value;
public static final long /*int*/ sel_setOnMouseEntered_ = Selector.sel_setOnMouseEntered_.value;
public static final long /*int*/ sel_setOpaque_ = Selector.sel_setOpaque_.value;
public static final long /*int*/ sel_setOptions_ = Selector.sel_setOptions_.value;
public static final long /*int*/ sel_setOutlineTableColumn_ = Selector.sel_setOutlineTableColumn_.value;
public static final long /*int*/ sel_setPaletteLabel_ = Selector.sel_setPaletteLabel_.value;
public static final long /*int*/ sel_setPanelFont_isMultiple_ = Selector.sel_setPanelFont_isMultiple_.value;
public static final long /*int*/ sel_setPartialStringValidationEnabled_ = Selector.sel_setPartialStringValidationEnabled_.value;
public static final long /*int*/ sel_setPatternPhase_ = Selector.sel_setPatternPhase_.value;
public static final long /*int*/ sel_setPlaceholderString_ = Selector.sel_setPlaceholderString_.value;
public static final long /*int*/ sel_setPolicyDelegate_ = Selector.sel_setPolicyDelegate_.value;
public static final long /*int*/ sel_setPreferences_ = Selector.sel_setPreferences_.value;
public static final long /*int*/ sel_setPrinter_ = Selector.sel_setPrinter_.value;
public static final long /*int*/ sel_setPropertyList_forType_ = Selector.sel_setPropertyList_forType_.value;
public static final long /*int*/ sel_setPullsDown_ = Selector.sel_setPullsDown_.value;
public static final long /*int*/ sel_setReleasedWhenClosed_ = Selector.sel_setReleasedWhenClosed_.value;
public static final long /*int*/ sel_setRepresentedFilename_ = Selector.sel_setRepresentedFilename_.value;
public static final long /*int*/ sel_setRepresentedURL_ = Selector.sel_setRepresentedURL_.value;
public static final long /*int*/ sel_setResizingMask_ = Selector.sel_setResizingMask_.value;
public static final long /*int*/ sel_setResourceLoadDelegate_ = Selector.sel_setResourceLoadDelegate_.value;
public static final long /*int*/ sel_setRichText_ = Selector.sel_setRichText_.value;
public static final long /*int*/ sel_setRowHeight_ = Selector.sel_setRowHeight_.value;
public static final long /*int*/ sel_setScalesWhenResized_ = Selector.sel_setScalesWhenResized_.value;
public static final long /*int*/ sel_setScrollable_ = Selector.sel_setScrollable_.value;
public static final long /*int*/ sel_setSearchButtonCell_ = Selector.sel_setSearchButtonCell_.value;
public static final long /*int*/ sel_setSegmentCount_ = Selector.sel_setSegmentCount_.value;
public static final long /*int*/ sel_setSegmentStyle_ = Selector.sel_setSegmentStyle_.value;
public static final long /*int*/ sel_setSelectable_ = Selector.sel_setSelectable_.value;
public static final long /*int*/ sel_setSelected_forSegment_ = Selector.sel_setSelected_forSegment_.value;
public static final long /*int*/ sel_setSelectedItemIdentifier_ = Selector.sel_setSelectedItemIdentifier_.value;
public static final long /*int*/ sel_setSelectedRange_ = Selector.sel_setSelectedRange_.value;
public static final long /*int*/ sel_setSelectedSegment_ = Selector.sel_setSelectedSegment_.value;
public static final long /*int*/ sel_setSelectedTextAttributes_ = Selector.sel_setSelectedTextAttributes_.value;
public static final long /*int*/ sel_setSelectionOnly_ = Selector.sel_setSelectionOnly_.value;
public static final long /*int*/ sel_setServicesMenu_ = Selector.sel_setServicesMenu_.value;
public static final long /*int*/ sel_setShouldAntialias_ = Selector.sel_setShouldAntialias_.value;
public static final long /*int*/ sel_setShowsHelp_ = Selector.sel_setShowsHelp_.value;
public static final long /*int*/ sel_setShowsPrintPanel_ = Selector.sel_setShowsPrintPanel_.value;
public static final long /*int*/ sel_setShowsProgressPanel_ = Selector.sel_setShowsProgressPanel_.value;
public static final long /*int*/ sel_setShowsResizeIndicator_ = Selector.sel_setShowsResizeIndicator_.value;
public static final long /*int*/ sel_setShowsToolbarButton_ = Selector.sel_setShowsToolbarButton_.value;
public static final long /*int*/ sel_setSize_ = Selector.sel_setSize_.value;
public static final long /*int*/ sel_setState_ = Selector.sel_setState_.value;
public static final long /*int*/ sel_setString_ = Selector.sel_setString_.value;
public static final long /*int*/ sel_setString_forType_ = Selector.sel_setString_forType_.value;
public static final long /*int*/ sel_setStringValue_ = Selector.sel_setStringValue_.value;
public static final long /*int*/ sel_setStroke = Selector.sel_setStroke.value;
public static final long /*int*/ sel_setSubmenu_ = Selector.sel_setSubmenu_.value;
public static final long /*int*/ sel_setSubmenu_forItem_ = Selector.sel_setSubmenu_forItem_.value;
public static final long /*int*/ sel_setTabStops_ = Selector.sel_setTabStops_.value;
public static final long /*int*/ sel_setTabViewType_ = Selector.sel_setTabViewType_.value;
public static final long /*int*/ sel_setTag_ = Selector.sel_setTag_.value;
public static final long /*int*/ sel_setTag_forSegment_ = Selector.sel_setTag_forSegment_.value;
public static final long /*int*/ sel_setTarget_ = Selector.sel_setTarget_.value;
public static final long /*int*/ sel_setTextColor_ = Selector.sel_setTextColor_.value;
public static final long /*int*/ sel_setTextStorage_ = Selector.sel_setTextStorage_.value;
public static final long /*int*/ sel_setTitle_ = Selector.sel_setTitle_.value;
public static final long /*int*/ sel_setTitleFont_ = Selector.sel_setTitleFont_.value;
public static final long /*int*/ sel_setTitlePosition_ = Selector.sel_setTitlePosition_.value;
public static final long /*int*/ sel_setToolTip_ = Selector.sel_setToolTip_.value;
public static final long /*int*/ sel_setToolTip_forSegment_ = Selector.sel_setToolTip_forSegment_.value;
public static final long /*int*/ sel_setToolbar_ = Selector.sel_setToolbar_.value;
public static final long /*int*/ sel_setTrackingMode_ = Selector.sel_setTrackingMode_.value;
public static final long /*int*/ sel_setTransformStruct_ = Selector.sel_setTransformStruct_.value;
public static final long /*int*/ sel_setTreatsFilePackagesAsDirectories_ = Selector.sel_setTreatsFilePackagesAsDirectories_.value;
public static final long /*int*/ sel_setUIDelegate_ = Selector.sel_setUIDelegate_.value;
public static final long /*int*/ sel_setURL_ = Selector.sel_setURL_.value;
public static final long /*int*/ sel_setUpPrintOperationDefaultValues = Selector.sel_setUpPrintOperationDefaultValues.value;
public static final long /*int*/ sel_setUsesAlternatingRowBackgroundColors_ = Selector.sel_setUsesAlternatingRowBackgroundColors_.value;
public static final long /*int*/ sel_setUsesFontPanel_ = Selector.sel_setUsesFontPanel_.value;
public static final long /*int*/ sel_setUsesScreenFonts_ = Selector.sel_setUsesScreenFonts_.value;
public static final long /*int*/ sel_setUsesSingleLineMode_ = Selector.sel_setUsesSingleLineMode_.value;
public static final long /*int*/ sel_setUsesThreadedAnimation_ = Selector.sel_setUsesThreadedAnimation_.value;
public static final long /*int*/ sel_setValue_forHTTPHeaderField_ = Selector.sel_setValue_forHTTPHeaderField_.value;
public static final long /*int*/ sel_setValue_forKey_ = Selector.sel_setValue_forKey_.value;
public static final long /*int*/ sel_setValueWraps_ = Selector.sel_setValueWraps_.value;
public static final long /*int*/ sel_setValues_forParameter_ = Selector.sel_setValues_forParameter_.value;
public static final long /*int*/ sel_setVerticalScrollElasticity_ = Selector.sel_setVerticalScrollElasticity_.value;
public static final long /*int*/ sel_setVerticalScroller_ = Selector.sel_setVerticalScroller_.value;
public static final long /*int*/ sel_setView_ = Selector.sel_setView_.value;
public static final long /*int*/ sel_setVisible_ = Selector.sel_setVisible_.value;
public static final long /*int*/ sel_setWantsRestingTouches_ = Selector.sel_setWantsRestingTouches_.value;
public static final long /*int*/ sel_setWidth_ = Selector.sel_setWidth_.value;
public static final long /*int*/ sel_setWidth_forSegment_ = Selector.sel_setWidth_forSegment_.value;
public static final long /*int*/ sel_setWidthTracksTextView_ = Selector.sel_setWidthTracksTextView_.value;
public static final long /*int*/ sel_setWindingRule_ = Selector.sel_setWindingRule_.value;
public static final long /*int*/ sel_setWorksWhenModal_ = Selector.sel_setWorksWhenModal_.value;
public static final long /*int*/ sel_setWraps_ = Selector.sel_setWraps_.value;
public static final long /*int*/ sel_sharedApplication = Selector.sel_sharedApplication.value;
public static final long /*int*/ sel_sharedCertificateTrustPanel = Selector.sel_sharedCertificateTrustPanel.value;
public static final long /*int*/ sel_sharedColorPanel = Selector.sel_sharedColorPanel.value;
public static final long /*int*/ sel_sharedFontManager = Selector.sel_sharedFontManager.value;
public static final long /*int*/ sel_sharedFontPanel = Selector.sel_sharedFontPanel.value;
public static final long /*int*/ sel_sharedHTTPCookieStorage = Selector.sel_sharedHTTPCookieStorage.value;
public static final long /*int*/ sel_sharedPrintInfo = Selector.sel_sharedPrintInfo.value;
public static final long /*int*/ sel_sharedWorkspace = Selector.sel_sharedWorkspace.value;
public static final long /*int*/ sel_shiftKey = Selector.sel_shiftKey.value;
public static final long /*int*/ sel_shouldAntialias = Selector.sel_shouldAntialias.value;
public static final long /*int*/ sel_shouldChangeTextInRange_replacementString_ = Selector.sel_shouldChangeTextInRange_replacementString_.value;
public static final long /*int*/ sel_shouldDelayWindowOrderingForEvent_ = Selector.sel_shouldDelayWindowOrderingForEvent_.value;
public static final long /*int*/ sel_shouldDrawInsertionPoint = Selector.sel_shouldDrawInsertionPoint.value;
public static final long /*int*/ sel_size = Selector.sel_size.value;
public static final long /*int*/ sel_sizeOfLabel_ = Selector.sel_sizeOfLabel_.value;
public static final long /*int*/ sel_sizeToFit = Selector.sel_sizeToFit.value;
public static final long /*int*/ sel_sizeValue = Selector.sel_sizeValue.value;
public static final long /*int*/ sel_skipDescendents = Selector.sel_skipDescendents.value;
public static final long /*int*/ sel_smallSystemFontSize = Selector.sel_smallSystemFontSize.value;
public static final long /*int*/ sel_sortIndicatorRectForBounds_ = Selector.sel_sortIndicatorRectForBounds_.value;
public static final long /*int*/ sel_standardPreferences = Selector.sel_standardPreferences.value;
public static final long /*int*/ sel_standardUserDefaults = Selector.sel_standardUserDefaults.value;
public static final long /*int*/ sel_standardWindowButton_ = Selector.sel_standardWindowButton_.value;
public static final long /*int*/ sel_startAnimation_ = Selector.sel_startAnimation_.value;
public static final long /*int*/ sel_state = Selector.sel_state.value;
public static final long /*int*/ sel_statusItemWithLength_ = Selector.sel_statusItemWithLength_.value;
public static final long /*int*/ sel_stop_ = Selector.sel_stop_.value;
public static final long /*int*/ sel_stopAnimation_ = Selector.sel_stopAnimation_.value;
public static final long /*int*/ sel_stopLoading_ = Selector.sel_stopLoading_.value;
public static final long /*int*/ sel_stopModal = Selector.sel_stopModal.value;
public static final long /*int*/ sel_string = Selector.sel_string.value;
public static final long /*int*/ sel_stringByAddingPercentEscapesUsingEncoding_ = Selector.sel_stringByAddingPercentEscapesUsingEncoding_.value;
public static final long /*int*/ sel_stringByAppendingPathComponent_ = Selector.sel_stringByAppendingPathComponent_.value;
public static final long /*int*/ sel_stringByAppendingPathExtension_ = Selector.sel_stringByAppendingPathExtension_.value;
public static final long /*int*/ sel_stringByAppendingString_ = Selector.sel_stringByAppendingString_.value;
public static final long /*int*/ sel_stringByDeletingLastPathComponent = Selector.sel_stringByDeletingLastPathComponent.value;
public static final long /*int*/ sel_stringByDeletingPathExtension = Selector.sel_stringByDeletingPathExtension.value;
public static final long /*int*/ sel_stringByReplacingOccurrencesOfString_withString_ = Selector.sel_stringByReplacingOccurrencesOfString_withString_.value;
public static final long /*int*/ sel_stringByReplacingPercentEscapesUsingEncoding_ = Selector.sel_stringByReplacingPercentEscapesUsingEncoding_.value;
public static final long /*int*/ sel_stringForKey_ = Selector.sel_stringForKey_.value;
public static final long /*int*/ sel_stringForObjectValue_ = Selector.sel_stringForObjectValue_.value;
public static final long /*int*/ sel_stringForType_ = Selector.sel_stringForType_.value;
public static final long /*int*/ sel_stringValue = Selector.sel_stringValue.value;
public static final long /*int*/ sel_stringWithCharacters_length_ = Selector.sel_stringWithCharacters_length_.value;
public static final long /*int*/ sel_stringWithFormat_ = Selector.sel_stringWithFormat_.value;
public static final long /*int*/ sel_stringWithUTF8String_ = Selector.sel_stringWithUTF8String_.value;
public static final long /*int*/ sel_stroke = Selector.sel_stroke.value;
public static final long /*int*/ sel_strokeRect_ = Selector.sel_strokeRect_.value;
public static final long /*int*/ sel_styleMask = Selector.sel_styleMask.value;
public static final long /*int*/ sel_submenu = Selector.sel_submenu.value;
public static final long /*int*/ sel_subviews = Selector.sel_subviews.value;
public static final long /*int*/ sel_superclass = Selector.sel_superclass.value;
public static final long /*int*/ sel_superview = Selector.sel_superview.value;
public static final long /*int*/ sel_swipeWithEvent_ = Selector.sel_swipeWithEvent_.value;
public static final long /*int*/ sel_systemFontOfSize_ = Selector.sel_systemFontOfSize_.value;
public static final long /*int*/ sel_systemFontSize = Selector.sel_systemFontSize.value;
public static final long /*int*/ sel_systemFontSizeForControlSize_ = Selector.sel_systemFontSizeForControlSize_.value;
public static final long /*int*/ sel_systemStatusBar = Selector.sel_systemStatusBar.value;
public static final long /*int*/ sel_systemVersion = Selector.sel_systemVersion.value;
public static final long /*int*/ sel_tabStopType = Selector.sel_tabStopType.value;
public static final long /*int*/ sel_tabStops = Selector.sel_tabStops.value;
public static final long /*int*/ sel_tabView_didSelectTabViewItem_ = Selector.sel_tabView_didSelectTabViewItem_.value;
public static final long /*int*/ sel_tabView_shouldSelectTabViewItem_ = Selector.sel_tabView_shouldSelectTabViewItem_.value;
public static final long /*int*/ sel_tabView_willSelectTabViewItem_ = Selector.sel_tabView_willSelectTabViewItem_.value;
public static final long /*int*/ sel_tabViewItemAtPoint_ = Selector.sel_tabViewItemAtPoint_.value;
public static final long /*int*/ sel_tableColumns = Selector.sel_tableColumns.value;
public static final long /*int*/ sel_tableView_acceptDrop_row_dropOperation_ = Selector.sel_tableView_acceptDrop_row_dropOperation_.value;
public static final long /*int*/ sel_tableView_didClickTableColumn_ = Selector.sel_tableView_didClickTableColumn_.value;
public static final long /*int*/ sel_tableView_objectValueForTableColumn_row_ = Selector.sel_tableView_objectValueForTableColumn_row_.value;
public static final long /*int*/ sel_tableView_setObjectValue_forTableColumn_row_ = Selector.sel_tableView_setObjectValue_forTableColumn_row_.value;
public static final long /*int*/ sel_tableView_shouldEditTableColumn_row_ = Selector.sel_tableView_shouldEditTableColumn_row_.value;
public static final long /*int*/ sel_tableView_shouldReorderColumn_toColumn_ = Selector.sel_tableView_shouldReorderColumn_toColumn_.value;
public static final long /*int*/ sel_tableView_shouldSelectRow_ = Selector.sel_tableView_shouldSelectRow_.value;
public static final long /*int*/ sel_tableView_shouldTrackCell_forTableColumn_row_ = Selector.sel_tableView_shouldTrackCell_forTableColumn_row_.value;
public static final long /*int*/ sel_tableView_validateDrop_proposedRow_proposedDropOperation_ = Selector.sel_tableView_validateDrop_proposedRow_proposedDropOperation_.value;
public static final long /*int*/ sel_tableView_willDisplayCell_forTableColumn_row_ = Selector.sel_tableView_willDisplayCell_forTableColumn_row_.value;
public static final long /*int*/ sel_tableView_writeRowsWithIndexes_toPasteboard_ = Selector.sel_tableView_writeRowsWithIndexes_toPasteboard_.value;
public static final long /*int*/ sel_tableViewColumnDidMove_ = Selector.sel_tableViewColumnDidMove_.value;
public static final long /*int*/ sel_tableViewColumnDidResize_ = Selector.sel_tableViewColumnDidResize_.value;
public static final long /*int*/ sel_tableViewSelectionDidChange_ = Selector.sel_tableViewSelectionDidChange_.value;
public static final long /*int*/ sel_tableViewSelectionIsChanging_ = Selector.sel_tableViewSelectionIsChanging_.value;
public static final long /*int*/ sel_tag = Selector.sel_tag.value;
public static final long /*int*/ sel_target = Selector.sel_target.value;
public static final long /*int*/ sel_terminate_ = Selector.sel_terminate_.value;
public static final long /*int*/ sel_testPart_ = Selector.sel_testPart_.value;
public static final long /*int*/ sel_textBackgroundColor = Selector.sel_textBackgroundColor.value;
public static final long /*int*/ sel_textColor = Selector.sel_textColor.value;
public static final long /*int*/ sel_textContainer = Selector.sel_textContainer.value;
public static final long /*int*/ sel_textDidChange_ = Selector.sel_textDidChange_.value;
public static final long /*int*/ sel_textDidEndEditing_ = Selector.sel_textDidEndEditing_.value;
public static final long /*int*/ sel_textStorage = Selector.sel_textStorage.value;
public static final long /*int*/ sel_textView_clickedOnLink_atIndex_ = Selector.sel_textView_clickedOnLink_atIndex_.value;
public static final long /*int*/ sel_textView_willChangeSelectionFromCharacterRange_toCharacterRange_ = Selector.sel_textView_willChangeSelectionFromCharacterRange_toCharacterRange_.value;
public static final long /*int*/ sel_textViewDidChangeSelection_ = Selector.sel_textViewDidChangeSelection_.value;
public static final long /*int*/ sel_thickness = Selector.sel_thickness.value;
public static final long /*int*/ sel_threadDictionary = Selector.sel_threadDictionary.value;
public static final long /*int*/ sel_tile = Selector.sel_tile.value;
public static final long /*int*/ sel_timeZone = Selector.sel_timeZone.value;
public static final long /*int*/ sel_timestamp = Selector.sel_timestamp.value;
public static final long /*int*/ sel_title = Selector.sel_title.value;
public static final long /*int*/ sel_titleCell = Selector.sel_titleCell.value;
public static final long /*int*/ sel_titleFont = Selector.sel_titleFont.value;
public static final long /*int*/ sel_titleOfSelectedItem = Selector.sel_titleOfSelectedItem.value;
public static final long /*int*/ sel_titleRectForBounds_ = Selector.sel_titleRectForBounds_.value;
public static final long /*int*/ sel_toggleToolbarShown_ = Selector.sel_toggleToolbarShown_.value;
public static final long /*int*/ sel_toolbar = Selector.sel_toolbar.value;
public static final long /*int*/ sel_toolbar_itemForItemIdentifier_willBeInsertedIntoToolbar_ = Selector.sel_toolbar_itemForItemIdentifier_willBeInsertedIntoToolbar_.value;
public static final long /*int*/ sel_toolbarAllowedItemIdentifiers_ = Selector.sel_toolbarAllowedItemIdentifiers_.value;
public static final long /*int*/ sel_toolbarDefaultItemIdentifiers_ = Selector.sel_toolbarDefaultItemIdentifiers_.value;
public static final long /*int*/ sel_toolbarDidRemoveItem_ = Selector.sel_toolbarDidRemoveItem_.value;
public static final long /*int*/ sel_toolbarSelectableItemIdentifiers_ = Selector.sel_toolbarSelectableItemIdentifiers_.value;
public static final long /*int*/ sel_toolbarWillAddItem_ = Selector.sel_toolbarWillAddItem_.value;
public static final long /*int*/ sel_touchesBeganWithEvent_ = Selector.sel_touchesBeganWithEvent_.value;
public static final long /*int*/ sel_touchesCancelledWithEvent_ = Selector.sel_touchesCancelledWithEvent_.value;
public static final long /*int*/ sel_touchesEndedWithEvent_ = Selector.sel_touchesEndedWithEvent_.value;
public static final long /*int*/ sel_touchesMatchingPhase_inView_ = Selector.sel_touchesMatchingPhase_inView_.value;
public static final long /*int*/ sel_touchesMovedWithEvent_ = Selector.sel_touchesMovedWithEvent_.value;
public static final long /*int*/ sel_trackingAreas = Selector.sel_trackingAreas.value;
public static final long /*int*/ sel_traitsOfFont_ = Selector.sel_traitsOfFont_.value;
public static final long /*int*/ sel_transform = Selector.sel_transform.value;
public static final long /*int*/ sel_transformPoint_ = Selector.sel_transformPoint_.value;
public static final long /*int*/ sel_transformSize_ = Selector.sel_transformSize_.value;
public static final long /*int*/ sel_transformStruct = Selector.sel_transformStruct.value;
public static final long /*int*/ sel_transformUsingAffineTransform_ = Selector.sel_transformUsingAffineTransform_.value;
public static final long /*int*/ sel_translateXBy_yBy_ = Selector.sel_translateXBy_yBy_.value;
public static final long /*int*/ sel_type = Selector.sel_type.value;
public static final long /*int*/ sel_type_conformsToType_ = Selector.sel_type_conformsToType_.value;
public static final long /*int*/ sel_typeOfFile_error_ = Selector.sel_typeOfFile_error_.value;
public static final long /*int*/ sel_types = Selector.sel_types.value;
public static final long /*int*/ sel_typesetter = Selector.sel_typesetter.value;
public static final long /*int*/ sel_unarchiveObjectWithData_ = Selector.sel_unarchiveObjectWithData_.value;
public static final long /*int*/ sel_undefined = Selector.sel_undefined.value;
public static final long /*int*/ sel_undo = Selector.sel_undo.value;
public static final long /*int*/ sel_undoManager = Selector.sel_undoManager.value;
public static final long /*int*/ sel_unhideAllApplications_ = Selector.sel_unhideAllApplications_.value;
public static final long /*int*/ sel_unlockFocus = Selector.sel_unlockFocus.value;
public static final long /*int*/ sel_unmarkText = Selector.sel_unmarkText.value;
public static final long /*int*/ sel_unregisterDraggedTypes = Selector.sel_unregisterDraggedTypes.value;
public static final long /*int*/ sel_update = Selector.sel_update.value;
public static final long /*int*/ sel_updateFromPMPrintSettings = Selector.sel_updateFromPMPrintSettings.value;
public static final long /*int*/ sel_updateTrackingAreas = Selector.sel_updateTrackingAreas.value;
public static final long /*int*/ sel_use = Selector.sel_use.value;
public static final long /*int*/ sel_useCredential_forAuthenticationChallenge_ = Selector.sel_useCredential_forAuthenticationChallenge_.value;
public static final long /*int*/ sel_usedRectForTextContainer_ = Selector.sel_usedRectForTextContainer_.value;
public static final long /*int*/ sel_user = Selector.sel_user.value;
public static final long /*int*/ sel_userInfo = Selector.sel_userInfo.value;
public static final long /*int*/ sel_usesAlternatingRowBackgroundColors = Selector.sel_usesAlternatingRowBackgroundColors.value;
public static final long /*int*/ sel_validAttributesForMarkedText = Selector.sel_validAttributesForMarkedText.value;
public static final long /*int*/ sel_validModesForFontPanel_ = Selector.sel_validModesForFontPanel_.value;
public static final long /*int*/ sel_validRequestorForSendType_returnType_ = Selector.sel_validRequestorForSendType_returnType_.value;
public static final long /*int*/ sel_validateMenuItem_ = Selector.sel_validateMenuItem_.value;
public static final long /*int*/ sel_validateVisibleColumns = Selector.sel_validateVisibleColumns.value;
public static final long /*int*/ sel_value = Selector.sel_value.value;
public static final long /*int*/ sel_valueForKey_ = Selector.sel_valueForKey_.value;
public static final long /*int*/ sel_valueWithPoint_ = Selector.sel_valueWithPoint_.value;
public static final long /*int*/ sel_valueWithRange_ = Selector.sel_valueWithRange_.value;
public static final long /*int*/ sel_valueWithRect_ = Selector.sel_valueWithRect_.value;
public static final long /*int*/ sel_valueWithSize_ = Selector.sel_valueWithSize_.value;
public static final long /*int*/ sel_view = Selector.sel_view.value;
public static final long /*int*/ sel_view_stringForToolTip_point_userData_ = Selector.sel_view_stringForToolTip_point_userData_.value;
public static final long /*int*/ sel_viewDidMoveToWindow = Selector.sel_viewDidMoveToWindow.value;
public static final long /*int*/ sel_viewWillMoveToWindow_ = Selector.sel_viewWillMoveToWindow_.value;
public static final long /*int*/ sel_visibleFrame = Selector.sel_visibleFrame.value;
public static final long /*int*/ sel_visibleRect = Selector.sel_visibleRect.value;
public static final long /*int*/ sel_wantsPeriodicDraggingUpdates = Selector.sel_wantsPeriodicDraggingUpdates.value;
public static final long /*int*/ sel_wantsToHandleMouseEvents = Selector.sel_wantsToHandleMouseEvents.value;
public static final long /*int*/ sel_webFrame = Selector.sel_webFrame.value;
public static final long /*int*/ sel_webScriptValueAtIndex_ = Selector.sel_webScriptValueAtIndex_.value;
public static final long /*int*/ sel_webView_contextMenuItemsForElement_defaultMenuItems_ = Selector.sel_webView_contextMenuItemsForElement_defaultMenuItems_.value;
public static final long /*int*/ sel_webView_createWebViewWithRequest_ = Selector.sel_webView_createWebViewWithRequest_.value;
public static final long /*int*/ sel_webView_decidePolicyForMIMEType_request_frame_decisionListener_ = Selector.sel_webView_decidePolicyForMIMEType_request_frame_decisionListener_.value;
public static final long /*int*/ sel_webView_decidePolicyForNavigationAction_request_frame_decisionListener_ = Selector.sel_webView_decidePolicyForNavigationAction_request_frame_decisionListener_.value;
public static final long /*int*/ sel_webView_decidePolicyForNewWindowAction_request_newFrameName_decisionListener_ = Selector.sel_webView_decidePolicyForNewWindowAction_request_newFrameName_decisionListener_.value;
public static final long /*int*/ sel_webView_didChangeLocationWithinPageForFrame_ = Selector.sel_webView_didChangeLocationWithinPageForFrame_.value;
public static final long /*int*/ sel_webView_didCommitLoadForFrame_ = Selector.sel_webView_didCommitLoadForFrame_.value;
public static final long /*int*/ sel_webView_didFailProvisionalLoadWithError_forFrame_ = Selector.sel_webView_didFailProvisionalLoadWithError_forFrame_.value;
public static final long /*int*/ sel_webView_didFinishLoadForFrame_ = Selector.sel_webView_didFinishLoadForFrame_.value;
public static final long /*int*/ sel_webView_didReceiveTitle_forFrame_ = Selector.sel_webView_didReceiveTitle_forFrame_.value;
public static final long /*int*/ sel_webView_didStartProvisionalLoadForFrame_ = Selector.sel_webView_didStartProvisionalLoadForFrame_.value;
public static final long /*int*/ sel_webView_identifierForInitialRequest_fromDataSource_ = Selector.sel_webView_identifierForInitialRequest_fromDataSource_.value;
public static final long /*int*/ sel_webView_mouseDidMoveOverElement_modifierFlags_ = Selector.sel_webView_mouseDidMoveOverElement_modifierFlags_.value;
public static final long /*int*/ sel_webView_printFrameView_ = Selector.sel_webView_printFrameView_.value;
public static final long /*int*/ sel_webView_resource_didFailLoadingWithError_fromDataSource_ = Selector.sel_webView_resource_didFailLoadingWithError_fromDataSource_.value;
public static final long /*int*/ sel_webView_resource_didFinishLoadingFromDataSource_ = Selector.sel_webView_resource_didFinishLoadingFromDataSource_.value;
public static final long /*int*/ sel_webView_resource_didReceiveAuthenticationChallenge_fromDataSource_ = Selector.sel_webView_resource_didReceiveAuthenticationChallenge_fromDataSource_.value;
public static final long /*int*/ sel_webView_resource_willSendRequest_redirectResponse_fromDataSource_ = Selector.sel_webView_resource_willSendRequest_redirectResponse_fromDataSource_.value;
public static final long /*int*/ sel_webView_runBeforeUnloadConfirmPanelWithMessage_initiatedByFrame_ = Selector.sel_webView_runBeforeUnloadConfirmPanelWithMessage_initiatedByFrame_.value;
public static final long /*int*/ sel_webView_runJavaScriptAlertPanelWithMessage_ = Selector.sel_webView_runJavaScriptAlertPanelWithMessage_.value;
public static final long /*int*/ sel_webView_runJavaScriptAlertPanelWithMessage_initiatedByFrame_ = Selector.sel_webView_runJavaScriptAlertPanelWithMessage_initiatedByFrame_.value;
public static final long /*int*/ sel_webView_runJavaScriptConfirmPanelWithMessage_ = Selector.sel_webView_runJavaScriptConfirmPanelWithMessage_.value;
public static final long /*int*/ sel_webView_runJavaScriptConfirmPanelWithMessage_initiatedByFrame_ = Selector.sel_webView_runJavaScriptConfirmPanelWithMessage_initiatedByFrame_.value;
public static final long /*int*/ sel_webView_runOpenPanelForFileButtonWithResultListener_ = Selector.sel_webView_runOpenPanelForFileButtonWithResultListener_.value;
public static final long /*int*/ sel_webView_setFrame_ = Selector.sel_webView_setFrame_.value;
public static final long /*int*/ sel_webView_setResizable_ = Selector.sel_webView_setResizable_.value;
public static final long /*int*/ sel_webView_setStatusBarVisible_ = Selector.sel_webView_setStatusBarVisible_.value;
public static final long /*int*/ sel_webView_setStatusText_ = Selector.sel_webView_setStatusText_.value;
public static final long /*int*/ sel_webView_setToolbarsVisible_ = Selector.sel_webView_setToolbarsVisible_.value;
public static final long /*int*/ sel_webView_unableToImplementPolicyWithError_frame_ = Selector.sel_webView_unableToImplementPolicyWithError_frame_.value;
public static final long /*int*/ sel_webView_windowScriptObjectAvailable_ = Selector.sel_webView_windowScriptObjectAvailable_.value;
public static final long /*int*/ sel_webViewClose_ = Selector.sel_webViewClose_.value;
public static final long /*int*/ sel_webViewFocus_ = Selector.sel_webViewFocus_.value;
public static final long /*int*/ sel_webViewShow_ = Selector.sel_webViewShow_.value;
public static final long /*int*/ sel_webViewUnfocus_ = Selector.sel_webViewUnfocus_.value;
public static final long /*int*/ sel_weightOfFont_ = Selector.sel_weightOfFont_.value;
public static final long /*int*/ sel_wheelDelta = Selector.sel_wheelDelta.value;
public static final long /*int*/ sel_wheelDeltaX = Selector.sel_wheelDeltaX.value;
public static final long /*int*/ sel_wheelDeltaY = Selector.sel_wheelDeltaY.value;
public static final long /*int*/ sel_width = Selector.sel_width.value;
public static final long /*int*/ sel_window = Selector.sel_window.value;
public static final long /*int*/ sel_windowBackgroundColor = Selector.sel_windowBackgroundColor.value;
public static final long /*int*/ sel_windowDidBecomeKey_ = Selector.sel_windowDidBecomeKey_.value;
public static final long /*int*/ sel_windowDidDeminiaturize_ = Selector.sel_windowDidDeminiaturize_.value;
public static final long /*int*/ sel_windowDidMiniaturize_ = Selector.sel_windowDidMiniaturize_.value;
public static final long /*int*/ sel_windowDidMove_ = Selector.sel_windowDidMove_.value;
public static final long /*int*/ sel_windowDidResignKey_ = Selector.sel_windowDidResignKey_.value;
public static final long /*int*/ sel_windowDidResize_ = Selector.sel_windowDidResize_.value;
public static final long /*int*/ sel_windowFrameColor = Selector.sel_windowFrameColor.value;
public static final long /*int*/ sel_windowFrameTextColor = Selector.sel_windowFrameTextColor.value;
public static final long /*int*/ sel_windowNumber = Selector.sel_windowNumber.value;
public static final long /*int*/ sel_windowNumberAtPoint_belowWindowWithWindowNumber_ = Selector.sel_windowNumberAtPoint_belowWindowWithWindowNumber_.value;
public static final long /*int*/ sel_windowRef = Selector.sel_windowRef.value;
public static final long /*int*/ sel_windowShouldClose_ = Selector.sel_windowShouldClose_.value;
public static final long /*int*/ sel_windowWillClose_ = Selector.sel_windowWillClose_.value;
public static final long /*int*/ sel_windowWithWindowNumber_ = Selector.sel_windowWithWindowNumber_.value;
public static final long /*int*/ sel_windows = Selector.sel_windows.value;
public static final long /*int*/ sel_worksWhenModal = Selector.sel_worksWhenModal.value;
public static final long /*int*/ sel_wraps = Selector.sel_wraps.value;
public static final long /*int*/ sel_writeObjects_ = Selector.sel_writeObjects_.value;
public static final long /*int*/ sel_writeSelectionToPasteboard_types_ = Selector.sel_writeSelectionToPasteboard_types_.value;
public static final long /*int*/ sel_yearOfCommonEra = Selector.sel_yearOfCommonEra.value;
public static final long /*int*/ sel_zoom_ = Selector.sel_zoom_.value;

/** Constants */
public static final int NSAlertFirstButtonReturn = 1000;
public static final int NSAlertSecondButtonReturn = 1001;
public static final int NSAlertThirdButtonReturn = 1002;
public static final int NSAlphaFirstBitmapFormat = 1;
public static final int NSAlphaNonpremultipliedBitmapFormat = 2;
public static final int NSAlternateKeyMask = 524288;
public static final int NSAnyEventMask = -1;
public static final int NSAppKitDefined = 13;
public static final int NSApplicationActivateIgnoringOtherApps = 2;
public static final int NSApplicationActivationPolicyRegular = 0;
public static final int NSApplicationDefined = 15;
public static final int NSApplicationDelegateReplySuccess = 0;
public static final int NSAtTop = 2;
public static final int NSBackgroundStyleRaised = 2;
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
public static final int NSClockAndCalendarDatePickerStyle = 1;
public static final int NSClosableWindowMask = 2;
public static final int NSClosePathBezierPathElement = 3;
public static final int NSCommandKeyMask = 1048576;
public static final int NSCompositeClear = 0;
public static final int NSCompositeCopy = 1;
public static final int NSCompositeSourceAtop = 5;
public static final int NSCompositeSourceOver = 2;
public static final int NSCompositeXOR = 10;
public static final int NSContentsCellMask = 1;
public static final int NSControlKeyMask = 262144;
public static final int NSCriticalAlertStyle = 2;
public static final int NSCurveToBezierPathElement = 2;
public static final int NSDeleteCharacter = 127;
public static final long NSDeviceIndependentModifierFlagsMask = 4294901760L;
public static final int NSDocModalWindowMask = 64;
public static final int NSDragOperationCopy = 1;
public static final int NSDragOperationDelete = 32;
public static final int NSDragOperationEvery = -1;
public static final int NSDragOperationLink = 2;
public static final int NSDragOperationMove = 16;
public static final int NSDragOperationNone = 0;
public static final int NSEnterCharacter = 3;
public static final int NSEvenOddWindingRule = 1;
public static final int NSEventTypeBeginGesture = 19;
public static final int NSEventTypeEndGesture = 20;
public static final int NSEventTypeGesture = 29;
public static final int NSEventTypeMagnify = 30;
public static final int NSEventTypeRotate = 18;
public static final int NSEventTypeSwipe = 31;
public static final int NSFileHandlingPanelOKButton = 1;
public static final int NSFlagsChanged = 12;
public static final int NSFocusRingTypeNone = 1;
public static final int NSFontPanelAllEffectsModeMask = 1048320;
public static final int NSFontPanelAllModesMask = -1;
public static final int NSFontPanelCollectionModeMask = 4;
public static final int NSFontPanelDocumentColorEffectModeMask = 2048;
public static final int NSFontPanelFaceModeMask = 1;
public static final int NSFontPanelShadowEffectModeMask = 4096;
public static final int NSFontPanelSizeModeMask = 2;
public static final int NSFontPanelStandardModesMask = 65535;
public static final int NSFontPanelStrikethroughEffectModeMask = 512;
public static final int NSFontPanelTextColorEffectModeMask = 1024;
public static final int NSFontPanelUnderlineEffectModeMask = 256;
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
public static final int NSLineBreakByClipping = 2;
public static final int NSLineBreakByTruncatingMiddle = 5;
public static final int NSLineBreakByTruncatingTail = 4;
public static final int NSLineBreakByWordWrapping = 0;
public static final int NSLineToBezierPathElement = 1;
public static final int NSMiniControlSize = 2;
public static final int NSMiniaturizableWindowMask = 4;
public static final int NSMiterLineJoinStyle = 0;
public static final int NSMixedState = -1;
public static final int NSModalPanelWindowLevel = 8;
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
public static final int NSNonactivatingPanelMask = 128;
public static final int NSNormalWindowLevel = 0;
public static final int NSOffState = 0;
public static final int NSOnState = 1;
public static final int NSOpenGLCPSurfaceOrder = 235;
public static final int NSOpenGLCPSwapInterval = 222;
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
public static final int NSPrintPanelShowsCopies = 1;
public static final int NSPrintPanelShowsOrientation = 8;
public static final int NSPrintPanelShowsPageRange = 2;
public static final int NSPrintPanelShowsPageSetupAccessory = 256;
public static final int NSPrintPanelShowsPrintSelection = 32;
public static final int NSProgressIndicatorPreferredThickness = 14;
public static final int NSPushOnPushOffButton = 1;
public static final int NSRGBColorSpaceModel = 1;
public static final int NSRadioButton = 4;
public static final int NSRegularControlSize = 0;
public static final int NSRegularSquareBezelStyle = 2;
public static final int NSResizableWindowMask = 8;
public static final int NSRightMouseDown = 3;
public static final int NSRightMouseDragged = 7;
public static final int NSRightMouseUp = 4;
public static final int NSRoundLineCapStyle = 1;
public static final int NSRoundLineJoinStyle = 1;
public static final int NSRoundedBezelStyle = 1;
public static final int NSRoundedDisclosureBezelStyle = 14;
public static final int NSScaleNone = 2;
public static final int NSScrollElasticityNone = 1;
public static final int NSScrollWheel = 22;
public static final int NSScrollerDecrementLine = 4;
public static final int NSScrollerDecrementPage = 1;
public static final int NSScrollerIncrementLine = 5;
public static final int NSScrollerIncrementPage = 3;
public static final int NSScrollerKnob = 2;
public static final int NSScrollerKnobSlot = 6;
public static final int NSShadowlessSquareBezelStyle = 6;
public static final int NSShiftKeyMask = 131072;
public static final int NSSmallControlSize = 1;
public static final int NSSquareLineCapStyle = 2;
public static final int NSStatusWindowLevel = 25;
public static final int NSStringDrawingUsesLineFragmentOrigin = 1;
public static final int NSSubmenuWindowLevel = 3;
public static final int NSSwitchButton = 3;
public static final int NSSystemDefined = 14;
public static final int NSTabCharacter = 9;
public static final int NSTableColumnNoResizing = 0;
public static final int NSTableColumnUserResizingMask = 2;
public static final int NSTableViewDropAbove = 1;
public static final int NSTableViewDropOn = 0;
public static final int NSTableViewGridNone = 0;
public static final int NSTableViewNoColumnAutoresizing = 0;
public static final int NSTableViewSolidVerticalGridLineMask = 1;
public static final int NSTerminateCancel = 0;
public static final int NSTerminateNow = 1;
public static final int NSTextAlignmentCenter = 2;
public static final int NSTextAlignmentJustified = 3;
public static final int NSTextAlignmentLeft = 0;
public static final int NSTextAlignmentNatural = 4;
public static final int NSTextAlignmentRight = 1;
public static final int NSTextFieldAndStepperDatePickerStyle = 0;
public static final int NSTextFieldDatePickerStyle = 2;
public static final int NSTitledWindowMask = 1;
public static final int NSToolbarDisplayModeIconOnly = 2;
public static final int NSTouchEventSubtype = 3;
public static final int NSTouchPhaseAny = -1;
public static final int NSTouchPhaseBegan = 1;
public static final int NSTouchPhaseCancelled = 16;
public static final int NSTouchPhaseEnded = 8;
public static final int NSTouchPhaseMoved = 2;
public static final int NSTouchPhaseStationary = 4;
public static final int NSTouchPhaseTouching = 7;
public static final int NSUnderlineStyleDouble = 9;
public static final int NSUnderlineStyleNone = 0;
public static final int NSUnderlineStyleSingle = 1;
public static final int NSUnderlineStyleThick = 2;
public static final int NSUtilityWindowMask = 16;
public static final int NSViewHeightSizable = 16;
public static final int NSViewMaxXMargin = 4;
public static final int NSViewMaxYMargin = 32;
public static final int NSViewMinXMargin = 1;
public static final int NSViewMinYMargin = 8;
public static final int NSViewWidthSizable = 2;
public static final int NSWarningAlertStyle = 0;
public static final int NSWindowAbove = 1;
public static final int NSWindowBelow = -1;
public static final int NSWindowCollectionBehaviorCanJoinAllSpaces = 1;
public static final int NSWindowCollectionBehaviorDefault = 0;
public static final int NSWindowCollectionBehaviorMoveToActiveSpace = 2;
public static final int NSWritingDirectionLeftToRight = 0;
public static final int NSWritingDirectionNatural = -1;
public static final int NSWritingDirectionRightToLeft = 1;
public static final int NSYearMonthDatePickerElementFlag = 192;
public static final int NSYearMonthDayDatePickerElementFlag = 224;
public static final int kCFRunLoopBeforeWaiting = 32;
public static final int kCFStringEncodingUTF8 = 134217984;
public static final int kCGBlendModeDifference = 10;
public static final int kCGBlendModeNormal = 0;
public static final int kCGEventFilterMaskPermitLocalKeyboardEvents = 2;
public static final int kCGEventFilterMaskPermitLocalMouseEvents = 1;
public static final int kCGEventFilterMaskPermitSystemDefinedEvents = 4;
public static final int kCGEventKeyDown = 10;
public static final int kCGEventKeyUp = 11;
public static final int kCGEventLeftMouseDown = 1;
public static final int kCGEventLeftMouseUp = 2;
public static final int kCGEventMouseMoved = 5;
public static final int kCGEventOtherMouseDown = 25;
public static final int kCGEventOtherMouseUp = 26;
public static final int kCGEventRightMouseDown = 3;
public static final int kCGEventRightMouseUp = 4;
public static final int kCGEventSourceStateHIDSystemState = 1;
public static final int kCGEventSuppressionStateRemoteMouseDrag = 1;
public static final int kCGEventSuppressionStateSuppressionInterval = 0;
public static final int kCGHIDEventTap = 0;
public static final int kCGImageAlphaFirst = 4;
public static final int kCGImageAlphaLast = 3;
public static final int kCGImageAlphaNoneSkipFirst = 6;
public static final int kCGImageAlphaNoneSkipLast = 5;
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
public static final int kCGScrollEventUnitLine = 1;
public static final int kCGScrollEventUnitPixel = 0;
public static final int kCGTextFillStroke = 2;
public static final int kCTParagraphStyleSpecifierBaseWritingDirection = 13;
public static final int kCTWritingDirectionLeftToRight = 0;
public static final int kCTWritingDirectionNatural = -1;
public static final int kCTWritingDirectionRightToLeft = 1;
public static final int NSAllApplicationsDirectory = 100;
public static final int NSAllDomainsMask = 65535;
public static final int NSCachesDirectory = 13;
public static final int NSOrderedSame = 0;
public static final int NSURLCredentialPersistenceForSession = 1;
public static final int NSURLErrorBadURL = -1000;
public static final int NSURLErrorSecureConnectionFailed = -1200;
public static final int NSURLErrorServerCertificateNotYetValid = -1204;
public static final int NSURLRequestReloadIgnoringLocalCacheData = 1;
public static final int NSUTF8StringEncoding = 4;
public static final int NSUserDomainMask = 1;

/** Globals */
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityAttributedStringForRangeParameterizedAttribute();
public static final NSString NSAccessibilityAttributedStringForRangeParameterizedAttribute = new NSString(NSAccessibilityAttributedStringForRangeParameterizedAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityBackgroundColorTextAttribute();
public static final NSString NSAccessibilityBackgroundColorTextAttribute = new NSString(NSAccessibilityBackgroundColorTextAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityBoundsForRangeParameterizedAttribute();
public static final NSString NSAccessibilityBoundsForRangeParameterizedAttribute = new NSString(NSAccessibilityBoundsForRangeParameterizedAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityButtonRole();
public static final NSString NSAccessibilityButtonRole = new NSString(NSAccessibilityButtonRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityCheckBoxRole();
public static final NSString NSAccessibilityCheckBoxRole = new NSString(NSAccessibilityCheckBoxRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityChildrenAttribute();
public static final NSString NSAccessibilityChildrenAttribute = new NSString(NSAccessibilityChildrenAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityColorWellRole();
public static final NSString NSAccessibilityColorWellRole = new NSString(NSAccessibilityColorWellRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityColumnRole();
public static final NSString NSAccessibilityColumnRole = new NSString(NSAccessibilityColumnRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityColumnsAttribute();
public static final NSString NSAccessibilityColumnsAttribute = new NSString(NSAccessibilityColumnsAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityComboBoxRole();
public static final NSString NSAccessibilityComboBoxRole = new NSString(NSAccessibilityComboBoxRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityConfirmAction();
public static final NSString NSAccessibilityConfirmAction = new NSString(NSAccessibilityConfirmAction());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityContentsAttribute();
public static final NSString NSAccessibilityContentsAttribute = new NSString(NSAccessibilityContentsAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityDescriptionAttribute();
public static final NSString NSAccessibilityDescriptionAttribute = new NSString(NSAccessibilityDescriptionAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityDialogSubrole();
public static final NSString NSAccessibilityDialogSubrole = new NSString(NSAccessibilityDialogSubrole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityEnabledAttribute();
public static final NSString NSAccessibilityEnabledAttribute = new NSString(NSAccessibilityEnabledAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityExpandedAttribute();
public static final NSString NSAccessibilityExpandedAttribute = new NSString(NSAccessibilityExpandedAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityFloatingWindowSubrole();
public static final NSString NSAccessibilityFloatingWindowSubrole = new NSString(NSAccessibilityFloatingWindowSubrole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityFocusedAttribute();
public static final NSString NSAccessibilityFocusedAttribute = new NSString(NSAccessibilityFocusedAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityFocusedUIElementChangedNotification();
public static final NSString NSAccessibilityFocusedUIElementChangedNotification = new NSString(NSAccessibilityFocusedUIElementChangedNotification());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityFocusedWindowChangedNotification();
public static final NSString NSAccessibilityFocusedWindowChangedNotification = new NSString(NSAccessibilityFocusedWindowChangedNotification());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityFontFamilyKey();
public static final NSString NSAccessibilityFontFamilyKey = new NSString(NSAccessibilityFontFamilyKey());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityFontNameKey();
public static final NSString NSAccessibilityFontNameKey = new NSString(NSAccessibilityFontNameKey());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityFontSizeKey();
public static final NSString NSAccessibilityFontSizeKey = new NSString(NSAccessibilityFontSizeKey());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityFontTextAttribute();
public static final NSString NSAccessibilityFontTextAttribute = new NSString(NSAccessibilityFontTextAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityForegroundColorTextAttribute();
public static final NSString NSAccessibilityForegroundColorTextAttribute = new NSString(NSAccessibilityForegroundColorTextAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityGridRole();
public static final NSString NSAccessibilityGridRole = new NSString(NSAccessibilityGridRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityGroupRole();
public static final NSString NSAccessibilityGroupRole = new NSString(NSAccessibilityGroupRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityHeaderAttribute();
public static final NSString NSAccessibilityHeaderAttribute = new NSString(NSAccessibilityHeaderAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityHelpAttribute();
public static final NSString NSAccessibilityHelpAttribute = new NSString(NSAccessibilityHelpAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityHelpTagRole();
public static final NSString NSAccessibilityHelpTagRole = new NSString(NSAccessibilityHelpTagRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityHorizontalOrientationValue();
public static final NSString NSAccessibilityHorizontalOrientationValue = new NSString(NSAccessibilityHorizontalOrientationValue());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityHorizontalScrollBarAttribute();
public static final NSString NSAccessibilityHorizontalScrollBarAttribute = new NSString(NSAccessibilityHorizontalScrollBarAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityImageRole();
public static final NSString NSAccessibilityImageRole = new NSString(NSAccessibilityImageRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityIncrementorRole();
public static final NSString NSAccessibilityIncrementorRole = new NSString(NSAccessibilityIncrementorRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityIndexAttribute();
public static final NSString NSAccessibilityIndexAttribute = new NSString(NSAccessibilityIndexAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityInsertionPointLineNumberAttribute();
public static final NSString NSAccessibilityInsertionPointLineNumberAttribute = new NSString(NSAccessibilityInsertionPointLineNumberAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityLabelValueAttribute();
public static final NSString NSAccessibilityLabelValueAttribute = new NSString(NSAccessibilityLabelValueAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityLineForIndexParameterizedAttribute();
public static final NSString NSAccessibilityLineForIndexParameterizedAttribute = new NSString(NSAccessibilityLineForIndexParameterizedAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityLinkRole();
public static final NSString NSAccessibilityLinkRole = new NSString(NSAccessibilityLinkRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityLinkTextAttribute();
public static final NSString NSAccessibilityLinkTextAttribute = new NSString(NSAccessibilityLinkTextAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityLinkedUIElementsAttribute();
public static final NSString NSAccessibilityLinkedUIElementsAttribute = new NSString(NSAccessibilityLinkedUIElementsAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityListRole();
public static final NSString NSAccessibilityListRole = new NSString(NSAccessibilityListRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityMaxValueAttribute();
public static final NSString NSAccessibilityMaxValueAttribute = new NSString(NSAccessibilityMaxValueAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityMenuBarRole();
public static final NSString NSAccessibilityMenuBarRole = new NSString(NSAccessibilityMenuBarRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityMenuButtonRole();
public static final NSString NSAccessibilityMenuButtonRole = new NSString(NSAccessibilityMenuButtonRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityMenuItemRole();
public static final NSString NSAccessibilityMenuItemRole = new NSString(NSAccessibilityMenuItemRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityMenuRole();
public static final NSString NSAccessibilityMenuRole = new NSString(NSAccessibilityMenuRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityMinValueAttribute();
public static final NSString NSAccessibilityMinValueAttribute = new NSString(NSAccessibilityMinValueAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityMisspelledTextAttribute();
public static final NSString NSAccessibilityMisspelledTextAttribute = new NSString(NSAccessibilityMisspelledTextAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityMovedNotification();
public static final NSString NSAccessibilityMovedNotification = new NSString(NSAccessibilityMovedNotification());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityNextContentsAttribute();
public static final NSString NSAccessibilityNextContentsAttribute = new NSString(NSAccessibilityNextContentsAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityNumberOfCharactersAttribute();
public static final NSString NSAccessibilityNumberOfCharactersAttribute = new NSString(NSAccessibilityNumberOfCharactersAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityOrientationAttribute();
public static final NSString NSAccessibilityOrientationAttribute = new NSString(NSAccessibilityOrientationAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityOutlineRole();
public static final NSString NSAccessibilityOutlineRole = new NSString(NSAccessibilityOutlineRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityOutlineRowSubrole();
public static final NSString NSAccessibilityOutlineRowSubrole = new NSString(NSAccessibilityOutlineRowSubrole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityParentAttribute();
public static final NSString NSAccessibilityParentAttribute = new NSString(NSAccessibilityParentAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityPopUpButtonRole();
public static final NSString NSAccessibilityPopUpButtonRole = new NSString(NSAccessibilityPopUpButtonRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityPositionAttribute();
public static final NSString NSAccessibilityPositionAttribute = new NSString(NSAccessibilityPositionAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityPressAction();
public static final NSString NSAccessibilityPressAction = new NSString(NSAccessibilityPressAction());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityPreviousContentsAttribute();
public static final NSString NSAccessibilityPreviousContentsAttribute = new NSString(NSAccessibilityPreviousContentsAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityProgressIndicatorRole();
public static final NSString NSAccessibilityProgressIndicatorRole = new NSString(NSAccessibilityProgressIndicatorRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityRTFForRangeParameterizedAttribute();
public static final NSString NSAccessibilityRTFForRangeParameterizedAttribute = new NSString(NSAccessibilityRTFForRangeParameterizedAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityRadioButtonRole();
public static final NSString NSAccessibilityRadioButtonRole = new NSString(NSAccessibilityRadioButtonRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityRadioGroupRole();
public static final NSString NSAccessibilityRadioGroupRole = new NSString(NSAccessibilityRadioGroupRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityRangeForIndexParameterizedAttribute();
public static final NSString NSAccessibilityRangeForIndexParameterizedAttribute = new NSString(NSAccessibilityRangeForIndexParameterizedAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityRangeForLineParameterizedAttribute();
public static final NSString NSAccessibilityRangeForLineParameterizedAttribute = new NSString(NSAccessibilityRangeForLineParameterizedAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityRangeForPositionParameterizedAttribute();
public static final NSString NSAccessibilityRangeForPositionParameterizedAttribute = new NSString(NSAccessibilityRangeForPositionParameterizedAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityResizedNotification();
public static final NSString NSAccessibilityResizedNotification = new NSString(NSAccessibilityResizedNotification());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityRoleAttribute();
public static final NSString NSAccessibilityRoleAttribute = new NSString(NSAccessibilityRoleAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityRoleDescriptionAttribute();
public static final NSString NSAccessibilityRoleDescriptionAttribute = new NSString(NSAccessibilityRoleDescriptionAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityRowCountChangedNotification();
public static final NSString NSAccessibilityRowCountChangedNotification = new NSString(NSAccessibilityRowCountChangedNotification());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityRowRole();
public static final NSString NSAccessibilityRowRole = new NSString(NSAccessibilityRowRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityRowsAttribute();
public static final NSString NSAccessibilityRowsAttribute = new NSString(NSAccessibilityRowsAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityScrollAreaRole();
public static final NSString NSAccessibilityScrollAreaRole = new NSString(NSAccessibilityScrollAreaRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityScrollBarRole();
public static final NSString NSAccessibilityScrollBarRole = new NSString(NSAccessibilityScrollBarRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySelectedAttribute();
public static final NSString NSAccessibilitySelectedAttribute = new NSString(NSAccessibilitySelectedAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySelectedChildrenAttribute();
public static final NSString NSAccessibilitySelectedChildrenAttribute = new NSString(NSAccessibilitySelectedChildrenAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySelectedChildrenChangedNotification();
public static final NSString NSAccessibilitySelectedChildrenChangedNotification = new NSString(NSAccessibilitySelectedChildrenChangedNotification());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySelectedColumnsAttribute();
public static final NSString NSAccessibilitySelectedColumnsAttribute = new NSString(NSAccessibilitySelectedColumnsAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySelectedRowsAttribute();
public static final NSString NSAccessibilitySelectedRowsAttribute = new NSString(NSAccessibilitySelectedRowsAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySelectedRowsChangedNotification();
public static final NSString NSAccessibilitySelectedRowsChangedNotification = new NSString(NSAccessibilitySelectedRowsChangedNotification());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySelectedTextAttribute();
public static final NSString NSAccessibilitySelectedTextAttribute = new NSString(NSAccessibilitySelectedTextAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySelectedTextChangedNotification();
public static final NSString NSAccessibilitySelectedTextChangedNotification = new NSString(NSAccessibilitySelectedTextChangedNotification());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySelectedTextRangeAttribute();
public static final NSString NSAccessibilitySelectedTextRangeAttribute = new NSString(NSAccessibilitySelectedTextRangeAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySelectedTextRangesAttribute();
public static final NSString NSAccessibilitySelectedTextRangesAttribute = new NSString(NSAccessibilitySelectedTextRangesAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityServesAsTitleForUIElementsAttribute();
public static final NSString NSAccessibilityServesAsTitleForUIElementsAttribute = new NSString(NSAccessibilityServesAsTitleForUIElementsAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityShowMenuAction();
public static final NSString NSAccessibilityShowMenuAction = new NSString(NSAccessibilityShowMenuAction());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySizeAttribute();
public static final NSString NSAccessibilitySizeAttribute = new NSString(NSAccessibilitySizeAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySliderRole();
public static final NSString NSAccessibilitySliderRole = new NSString(NSAccessibilitySliderRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySortButtonRole();
public static final NSString NSAccessibilitySortButtonRole = new NSString(NSAccessibilitySortButtonRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySplitterRole();
public static final NSString NSAccessibilitySplitterRole = new NSString(NSAccessibilitySplitterRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityStandardWindowSubrole();
public static final NSString NSAccessibilityStandardWindowSubrole = new NSString(NSAccessibilityStandardWindowSubrole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityStaticTextRole();
public static final NSString NSAccessibilityStaticTextRole = new NSString(NSAccessibilityStaticTextRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityStrikethroughColorTextAttribute();
public static final NSString NSAccessibilityStrikethroughColorTextAttribute = new NSString(NSAccessibilityStrikethroughColorTextAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityStrikethroughTextAttribute();
public static final NSString NSAccessibilityStrikethroughTextAttribute = new NSString(NSAccessibilityStrikethroughTextAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityStringForRangeParameterizedAttribute();
public static final NSString NSAccessibilityStringForRangeParameterizedAttribute = new NSString(NSAccessibilityStringForRangeParameterizedAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityStyleRangeForIndexParameterizedAttribute();
public static final NSString NSAccessibilityStyleRangeForIndexParameterizedAttribute = new NSString(NSAccessibilityStyleRangeForIndexParameterizedAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySubroleAttribute();
public static final NSString NSAccessibilitySubroleAttribute = new NSString(NSAccessibilitySubroleAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySuperscriptTextAttribute();
public static final NSString NSAccessibilitySuperscriptTextAttribute = new NSString(NSAccessibilitySuperscriptTextAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilitySystemDialogSubrole();
public static final NSString NSAccessibilitySystemDialogSubrole = new NSString(NSAccessibilitySystemDialogSubrole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityTabGroupRole();
public static final NSString NSAccessibilityTabGroupRole = new NSString(NSAccessibilityTabGroupRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityTableRole();
public static final NSString NSAccessibilityTableRole = new NSString(NSAccessibilityTableRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityTableRowSubrole();
public static final NSString NSAccessibilityTableRowSubrole = new NSString(NSAccessibilityTableRowSubrole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityTabsAttribute();
public static final NSString NSAccessibilityTabsAttribute = new NSString(NSAccessibilityTabsAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityTextAreaRole();
public static final NSString NSAccessibilityTextAreaRole = new NSString(NSAccessibilityTextAreaRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityTextFieldRole();
public static final NSString NSAccessibilityTextFieldRole = new NSString(NSAccessibilityTextFieldRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityTextLinkSubrole();
public static final NSString NSAccessibilityTextLinkSubrole = new NSString(NSAccessibilityTextLinkSubrole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityTitleAttribute();
public static final NSString NSAccessibilityTitleAttribute = new NSString(NSAccessibilityTitleAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityTitleChangedNotification();
public static final NSString NSAccessibilityTitleChangedNotification = new NSString(NSAccessibilityTitleChangedNotification());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityTitleUIElementAttribute();
public static final NSString NSAccessibilityTitleUIElementAttribute = new NSString(NSAccessibilityTitleUIElementAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityToolbarRole();
public static final NSString NSAccessibilityToolbarRole = new NSString(NSAccessibilityToolbarRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityTopLevelUIElementAttribute();
public static final NSString NSAccessibilityTopLevelUIElementAttribute = new NSString(NSAccessibilityTopLevelUIElementAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityURLAttribute();
public static final NSString NSAccessibilityURLAttribute = new NSString(NSAccessibilityURLAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityUnderlineColorTextAttribute();
public static final NSString NSAccessibilityUnderlineColorTextAttribute = new NSString(NSAccessibilityUnderlineColorTextAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityUnderlineTextAttribute();
public static final NSString NSAccessibilityUnderlineTextAttribute = new NSString(NSAccessibilityUnderlineTextAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityUnknownRole();
public static final NSString NSAccessibilityUnknownRole = new NSString(NSAccessibilityUnknownRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityUnknownSubrole();
public static final NSString NSAccessibilityUnknownSubrole = new NSString(NSAccessibilityUnknownSubrole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityValueAttribute();
public static final NSString NSAccessibilityValueAttribute = new NSString(NSAccessibilityValueAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityValueChangedNotification();
public static final NSString NSAccessibilityValueChangedNotification = new NSString(NSAccessibilityValueChangedNotification());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityValueDescriptionAttribute();
public static final NSString NSAccessibilityValueDescriptionAttribute = new NSString(NSAccessibilityValueDescriptionAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityValueIndicatorRole();
public static final NSString NSAccessibilityValueIndicatorRole = new NSString(NSAccessibilityValueIndicatorRole());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityVerticalOrientationValue();
public static final NSString NSAccessibilityVerticalOrientationValue = new NSString(NSAccessibilityVerticalOrientationValue());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityVerticalScrollBarAttribute();
public static final NSString NSAccessibilityVerticalScrollBarAttribute = new NSString(NSAccessibilityVerticalScrollBarAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityVisibleCharacterRangeAttribute();
public static final NSString NSAccessibilityVisibleCharacterRangeAttribute = new NSString(NSAccessibilityVisibleCharacterRangeAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityVisibleChildrenAttribute();
public static final NSString NSAccessibilityVisibleChildrenAttribute = new NSString(NSAccessibilityVisibleChildrenAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityVisibleColumnsAttribute();
public static final NSString NSAccessibilityVisibleColumnsAttribute = new NSString(NSAccessibilityVisibleColumnsAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityVisibleNameKey();
public static final NSString NSAccessibilityVisibleNameKey = new NSString(NSAccessibilityVisibleNameKey());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityVisibleRowsAttribute();
public static final NSString NSAccessibilityVisibleRowsAttribute = new NSString(NSAccessibilityVisibleRowsAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityWindowAttribute();
public static final NSString NSAccessibilityWindowAttribute = new NSString(NSAccessibilityWindowAttribute());
/** @method flags=const */
public static final native long /*int*/ NSAccessibilityWindowRole();
public static final NSString NSAccessibilityWindowRole = new NSString(NSAccessibilityWindowRole());
/** @method flags=const */
public static final native long /*int*/ NSApplicationDidChangeScreenParametersNotification();
public static final NSString NSApplicationDidChangeScreenParametersNotification = new NSString(NSApplicationDidChangeScreenParametersNotification());
/** @method flags=const */
public static final native long /*int*/ NSAttachmentAttributeName();
public static final NSString NSAttachmentAttributeName = new NSString(NSAttachmentAttributeName());
/** @method flags=const */
public static final native long /*int*/ NSBackgroundColorAttributeName();
public static final NSString NSBackgroundColorAttributeName = new NSString(NSBackgroundColorAttributeName());
/** @method flags=const */
public static final native long /*int*/ NSBaselineOffsetAttributeName();
public static final NSString NSBaselineOffsetAttributeName = new NSString(NSBaselineOffsetAttributeName());
/** @method flags=const */
public static final native long /*int*/ NSCalibratedRGBColorSpace();
public static final NSString NSCalibratedRGBColorSpace = new NSString(NSCalibratedRGBColorSpace());
/** @method flags=const */
public static final native long /*int*/ NSCursorAttributeName();
public static final NSString NSCursorAttributeName = new NSString(NSCursorAttributeName());
/** @method flags=const */
public static final native long /*int*/ NSDeviceRGBColorSpace();
public static final NSString NSDeviceRGBColorSpace = new NSString(NSDeviceRGBColorSpace());
/** @method flags=const */
public static final native long /*int*/ NSDeviceResolution();
public static final NSString NSDeviceResolution = new NSString(NSDeviceResolution());
/** @method flags=const */
public static final native long /*int*/ NSDragPboard();
public static final NSString NSDragPboard = new NSString(NSDragPboard());
/** @method flags=const */
public static final native long /*int*/ NSEventTrackingRunLoopMode();
public static final NSString NSEventTrackingRunLoopMode = new NSString(NSEventTrackingRunLoopMode());
/** @method flags=const */
public static final native long /*int*/ NSFilenamesPboardType();
public static final NSString NSFilenamesPboardType = new NSString(NSFilenamesPboardType());
/** @method flags=const */
public static final native long /*int*/ NSFontAttributeName();
public static final NSString NSFontAttributeName = new NSString(NSFontAttributeName());
/** @method flags=const */
public static final native long /*int*/ NSForegroundColorAttributeName();
public static final NSString NSForegroundColorAttributeName = new NSString(NSForegroundColorAttributeName());
/** @method flags=const */
public static final native long /*int*/ NSLigatureAttributeName();
public static final NSString NSLigatureAttributeName = new NSString(NSLigatureAttributeName());
/** @method flags=const */
public static final native long /*int*/ NSLinkAttributeName();
public static final NSString NSLinkAttributeName = new NSString(NSLinkAttributeName());
/** @method flags=const */
public static final native long /*int*/ NSModalPanelRunLoopMode();
public static final NSString NSModalPanelRunLoopMode = new NSString(NSModalPanelRunLoopMode());
/** @method flags=const */
public static final native long /*int*/ NSObliquenessAttributeName();
public static final NSString NSObliquenessAttributeName = new NSString(NSObliquenessAttributeName());
/** @method flags=const */
public static final native long /*int*/ NSOutlineViewColumnDidMoveNotification();
public static final NSString NSOutlineViewColumnDidMoveNotification = new NSString(NSOutlineViewColumnDidMoveNotification());
/** @method flags=const */
public static final native long /*int*/ NSParagraphStyleAttributeName();
public static final NSString NSParagraphStyleAttributeName = new NSString(NSParagraphStyleAttributeName());
/** @method flags=const */
public static final native long /*int*/ NSPasteboardTypeHTML();
public static final NSString NSPasteboardTypeHTML = new NSString(NSPasteboardTypeHTML());
/** @method flags=const */
public static final native long /*int*/ NSPasteboardTypeRTF();
public static final NSString NSPasteboardTypeRTF = new NSString(NSPasteboardTypeRTF());
/** @method flags=const */
public static final native long /*int*/ NSPasteboardTypeString();
public static final NSString NSPasteboardTypeString = new NSString(NSPasteboardTypeString());
/** @method flags=const */
public static final native long /*int*/ NSPrintAllPages();
public static final NSString NSPrintAllPages = new NSString(NSPrintAllPages());
/** @method flags=const */
public static final native long /*int*/ NSPrintCopies();
public static final NSString NSPrintCopies = new NSString(NSPrintCopies());
/** @method flags=const */
public static final native long /*int*/ NSPrintFirstPage();
public static final NSString NSPrintFirstPage = new NSString(NSPrintFirstPage());
/** @method flags=const */
public static final native long /*int*/ NSPrintJobDisposition();
public static final NSString NSPrintJobDisposition = new NSString(NSPrintJobDisposition());
/** @method flags=const */
public static final native long /*int*/ NSPrintLastPage();
public static final NSString NSPrintLastPage = new NSString(NSPrintLastPage());
/** @method flags=const */
public static final native long /*int*/ NSPrintMustCollate();
public static final NSString NSPrintMustCollate = new NSString(NSPrintMustCollate());
/** @method flags=const */
public static final native long /*int*/ NSPrintOrientation();
public static final NSString NSPrintOrientation = new NSString(NSPrintOrientation());
/** @method flags=const */
public static final native long /*int*/ NSPrintPreviewJob();
public static final NSString NSPrintPreviewJob = new NSString(NSPrintPreviewJob());
/** @method flags=const */
public static final native long /*int*/ NSPrintSaveJob();
public static final NSString NSPrintSaveJob = new NSString(NSPrintSaveJob());
/** @method flags=const */
public static final native long /*int*/ NSPrintSavePath();
public static final NSString NSPrintSavePath = new NSString(NSPrintSavePath());
/** @method flags=const */
public static final native long /*int*/ NSPrintScalingFactor();
public static final NSString NSPrintScalingFactor = new NSString(NSPrintScalingFactor());
/** @method flags=const */
public static final native long /*int*/ NSPrintSpoolJob();
public static final NSString NSPrintSpoolJob = new NSString(NSPrintSpoolJob());
/** @method flags=const */
public static final native long /*int*/ NSSpellingStateAttributeName();
public static final NSString NSSpellingStateAttributeName = new NSString(NSSpellingStateAttributeName());
/** @method flags=const */
public static final native double /*float*/ NSSquareStatusItemLength();
/** @method flags=const */
public static final native long /*int*/ NSStrikethroughColorAttributeName();
public static final NSString NSStrikethroughColorAttributeName = new NSString(NSStrikethroughColorAttributeName());
/** @method flags=const */
public static final native long /*int*/ NSStrikethroughStyleAttributeName();
public static final NSString NSStrikethroughStyleAttributeName = new NSString(NSStrikethroughStyleAttributeName());
/** @method flags=const */
public static final native long /*int*/ NSStrokeWidthAttributeName();
public static final NSString NSStrokeWidthAttributeName = new NSString(NSStrokeWidthAttributeName());
/** @method flags=const */
public static final native long /*int*/ NSSystemColorsDidChangeNotification();
public static final NSString NSSystemColorsDidChangeNotification = new NSString(NSSystemColorsDidChangeNotification());
/** @method flags=const */
public static final native long /*int*/ NSTIFFPboardType();
public static final NSString NSTIFFPboardType = new NSString(NSTIFFPboardType());
/** @method flags=const */
public static final native long /*int*/ NSTableViewColumnDidMoveNotification();
public static final NSString NSTableViewColumnDidMoveNotification = new NSString(NSTableViewColumnDidMoveNotification());
/** @method flags=const */
public static final native long /*int*/ NSToolbarCustomizeToolbarItemIdentifier();
public static final NSString NSToolbarCustomizeToolbarItemIdentifier = new NSString(NSToolbarCustomizeToolbarItemIdentifier());
/** @method flags=const */
public static final native long /*int*/ NSToolbarDidRemoveItemNotification();
public static final NSString NSToolbarDidRemoveItemNotification = new NSString(NSToolbarDidRemoveItemNotification());
/** @method flags=const */
public static final native long /*int*/ NSToolbarFlexibleSpaceItemIdentifier();
public static final NSString NSToolbarFlexibleSpaceItemIdentifier = new NSString(NSToolbarFlexibleSpaceItemIdentifier());
/** @method flags=const */
public static final native long /*int*/ NSToolbarPrintItemIdentifier();
public static final NSString NSToolbarPrintItemIdentifier = new NSString(NSToolbarPrintItemIdentifier());
/** @method flags=const */
public static final native long /*int*/ NSToolbarSeparatorItemIdentifier();
public static final NSString NSToolbarSeparatorItemIdentifier = new NSString(NSToolbarSeparatorItemIdentifier());
/** @method flags=const */
public static final native long /*int*/ NSToolbarShowColorsItemIdentifier();
public static final NSString NSToolbarShowColorsItemIdentifier = new NSString(NSToolbarShowColorsItemIdentifier());
/** @method flags=const */
public static final native long /*int*/ NSToolbarShowFontsItemIdentifier();
public static final NSString NSToolbarShowFontsItemIdentifier = new NSString(NSToolbarShowFontsItemIdentifier());
/** @method flags=const */
public static final native long /*int*/ NSToolbarSpaceItemIdentifier();
public static final NSString NSToolbarSpaceItemIdentifier = new NSString(NSToolbarSpaceItemIdentifier());
/** @method flags=const */
public static final native long /*int*/ NSToolbarWillAddItemNotification();
public static final NSString NSToolbarWillAddItemNotification = new NSString(NSToolbarWillAddItemNotification());
/** @method flags=const */
public static final native long /*int*/ NSURLPboardType();
public static final NSString NSURLPboardType = new NSString(NSURLPboardType());
/** @method flags=const */
public static final native long /*int*/ NSUnderlineColorAttributeName();
public static final NSString NSUnderlineColorAttributeName = new NSString(NSUnderlineColorAttributeName());
/** @method flags=const */
public static final native long /*int*/ NSUnderlineStyleAttributeName();
public static final NSString NSUnderlineStyleAttributeName = new NSString(NSUnderlineStyleAttributeName());
/** @method flags=const */
public static final native long /*int*/ NSViewGlobalFrameDidChangeNotification();
public static final NSString NSViewGlobalFrameDidChangeNotification = new NSString(NSViewGlobalFrameDidChangeNotification());
/** @method flags=const */
public static final native long /*int*/ NSWindowDidBecomeKeyNotification();
public static final NSString NSWindowDidBecomeKeyNotification = new NSString(NSWindowDidBecomeKeyNotification());
/** @method flags=const */
public static final native long /*int*/ NSWindowDidDeminiaturizeNotification();
public static final NSString NSWindowDidDeminiaturizeNotification = new NSString(NSWindowDidDeminiaturizeNotification());
/** @method flags=const */
public static final native long /*int*/ NSWindowDidMiniaturizeNotification();
public static final NSString NSWindowDidMiniaturizeNotification = new NSString(NSWindowDidMiniaturizeNotification());
/** @method flags=const */
public static final native long /*int*/ NSWindowDidMoveNotification();
public static final NSString NSWindowDidMoveNotification = new NSString(NSWindowDidMoveNotification());
/** @method flags=const */
public static final native long /*int*/ NSWindowDidResignKeyNotification();
public static final NSString NSWindowDidResignKeyNotification = new NSString(NSWindowDidResignKeyNotification());
/** @method flags=const */
public static final native long /*int*/ NSWindowDidResizeNotification();
public static final NSString NSWindowDidResizeNotification = new NSString(NSWindowDidResizeNotification());
/** @method flags=const */
public static final native long /*int*/ NSWindowWillCloseNotification();
public static final NSString NSWindowWillCloseNotification = new NSString(NSWindowWillCloseNotification());
/** @method flags=const */
public static final native long /*int*/ kCFAllocatorDefault();
/** @method flags=const */
public static final native long /*int*/ kCFRunLoopCommonModes();
/** @method flags=const */
public static final native long /*int*/ kCTFontAttributeName();
/** @method flags=const */
public static final native long /*int*/ kCTForegroundColorAttributeName();
/** @method flags=const */
public static final native long /*int*/ kCTParagraphStyleAttributeName();
/** @method flags=const */
public static final native long /*int*/ NSDefaultRunLoopMode();
public static final NSString NSDefaultRunLoopMode = new NSString(NSDefaultRunLoopMode());
/** @method flags=const */
public static final native long /*int*/ NSErrorFailingURLStringKey();
public static final NSString NSErrorFailingURLStringKey = new NSString(NSErrorFailingURLStringKey());
/** @method flags=const */
public static final native long /*int*/ NSLocaleLanguageCode();
public static final NSString NSLocaleLanguageCode = new NSString(NSLocaleLanguageCode());
/** @method flags=const */
public static final native long /*int*/ NSNotFound();

/** Functions */

/**
 * @param action cast=(NSString*)
 */
public static final native long /*int*/ NSAccessibilityActionDescription(long /*int*/ action);
/**
 * @param element cast=(id)
 * @param notification cast=(NSString*)
 */
public static final native void NSAccessibilityPostNotification(long /*int*/ element, long /*int*/ notification);
/**
 * @param element cast=(id)
 * @param attribute cast=(NSString*)
 * @param value cast=(id)
 */
public static final native void NSAccessibilityRaiseBadArgumentException(long /*int*/ element, long /*int*/ attribute, long /*int*/ value);
/**
 * @param role cast=(NSString*)
 * @param subrole cast=(NSString*)
 */
public static final native long /*int*/ NSAccessibilityRoleDescription(long /*int*/ role, long /*int*/ subrole);
/**
 * @param element cast=(id)
 */
public static final native long /*int*/ NSAccessibilityRoleDescriptionForUIElement(long /*int*/ element);
/**
 * @param element cast=(id)
 */
public static final native long /*int*/ NSAccessibilityUnignoredAncestor(long /*int*/ element);
/**
 * @param originalChildren cast=(NSArray*)
 */
public static final native long /*int*/ NSAccessibilityUnignoredChildren(long /*int*/ originalChildren);
/**
 * @param originalChild cast=(id)
 */
public static final native long /*int*/ NSAccessibilityUnignoredChildrenForOnlyChild(long /*int*/ originalChild);
/**
 * @param element cast=(id)
 */
public static final native long /*int*/ NSAccessibilityUnignoredDescendant(long /*int*/ element);
public static final native void NSBeep();
/**
 * @param depth cast=(NSWindowDepth)
 */
public static final native long /*int*/ NSBitsPerPixelFromDepth(int depth);
/**
 * @param count cast=(NSInteger*)
 */
public static final native void NSCountWindows(long[] /*int[]*/ count);
/**
 * @param colorSpaceName cast=(NSString*)
 */
public static final native long /*int*/ NSNumberOfColorComponents(long /*int*/ colorSpaceName);
/**
 * @param aRect flags=struct
 * @param op cast=(NSCompositingOperation)
 */
public static final native void NSRectFillUsingOperation(NSRect aRect, long /*int*/ op);
/**
 * @param alloc cast=(CFAllocatorRef)
 * @param str cast=(CFStringRef)
 * @param attributes cast=(CFDictionaryRef)
 */
public static final native long /*int*/ CFAttributedStringCreate(long /*int*/ alloc, long /*int*/ str, long /*int*/ attributes);
/**
 * @param theData cast=(CFDataRef)
 */
public static final native long /*int*/ CFDataGetBytePtr(long /*int*/ theData);
/**
 * @param theData cast=(CFDataRef)
 */
public static final native long /*int*/ CFDataGetLength(long /*int*/ theData);
/**
 * @param theDict cast=(CFMutableDictionaryRef)
 * @param key cast=(void*)
 * @param value cast=(void*)
 */
public static final native void CFDictionaryAddValue(long /*int*/ theDict, long /*int*/ key, long /*int*/ value);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param capacity cast=(CFIndex)
 * @param keyCallBacks cast=(CFDictionaryKeyCallBacks*)
 * @param valueCallBacks cast=(CFDictionaryValueCallBacks*)
 */
public static final native long /*int*/ CFDictionaryCreateMutable(long /*int*/ allocator, long /*int*/ capacity, long /*int*/ keyCallBacks, long /*int*/ valueCallBacks);
/**
 * @param cf cast=(CFTypeRef)
 */
public static final native void CFRelease(long /*int*/ cf);
/**
 * @param rl cast=(CFRunLoopRef)
 * @param observer cast=(CFRunLoopObserverRef)
 * @param mode cast=(CFStringRef)
 */
public static final native void CFRunLoopAddObserver(long /*int*/ rl, long /*int*/ observer, long /*int*/ mode);
public static final native long /*int*/ CFRunLoopGetCurrent();
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param activities cast=(CFOptionFlags)
 * @param repeats cast=(Boolean)
 * @param order cast=(CFIndex)
 * @param callout cast=(CFRunLoopObserverCallBack)
 * @param context cast=(CFRunLoopObserverContext*)
 */
public static final native long /*int*/ CFRunLoopObserverCreate(long /*int*/ allocator, long /*int*/ activities, boolean repeats, long /*int*/ order, long /*int*/ callout, long /*int*/ context);
/**
 * @param observer cast=(CFRunLoopObserverRef)
 */
public static final native void CFRunLoopObserverInvalidate(long /*int*/ observer);
/**
 * @param mode cast=(CFStringRef)
 * @param seconds cast=(CFTimeInterval)
 * @param returnAfterSourceHandled cast=(Boolean)
 */
public static final native int CFRunLoopRunInMode(long /*int*/ mode, double seconds, boolean returnAfterSourceHandled);
/**
 * @param rl cast=(CFRunLoopRef)
 */
public static final native void CFRunLoopStop(long /*int*/ rl);
/**
 * @param alloc cast=(CFAllocatorRef)
 * @param chars cast=(UniChar*)
 * @param numChars cast=(CFIndex)
 */
public static final native long /*int*/ CFStringCreateWithCharacters(long /*int*/ alloc, char[] chars, long /*int*/ numChars);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param fsRef cast=(FSRef*)
 */
public static final native long /*int*/ CFURLCreateFromFSRef(long /*int*/ allocator, byte[] fsRef);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param originalString cast=(CFStringRef)
 * @param charactersToLeaveUnescaped cast=(CFStringRef)
 * @param legalURLCharactersToBeEscaped cast=(CFStringRef)
 * @param encoding cast=(CFStringEncoding)
 */
public static final native long /*int*/ CFURLCreateStringByAddingPercentEscapes(long /*int*/ allocator, long /*int*/ originalString, long /*int*/ charactersToLeaveUnescaped, long /*int*/ legalURLCharactersToBeEscaped, int encoding);
/**
 * @param data cast=(void*)
 * @param width cast=(size_t)
 * @param height cast=(size_t)
 * @param bitsPerComponent cast=(size_t)
 * @param bytesPerRow cast=(size_t)
 * @param colorspace cast=(CGColorSpaceRef)
 * @param bitmapInfo cast=(CGBitmapInfo)
 */
public static final native long /*int*/ CGBitmapContextCreate(long /*int*/ data, long /*int*/ width, long /*int*/ height, long /*int*/ bitsPerComponent, long /*int*/ bytesPerRow, long /*int*/ colorspace, int bitmapInfo);
/**
 * @param c cast=(CGContextRef)
 */
public static final native long /*int*/ CGBitmapContextCreateImage(long /*int*/ c);
/**
 * @param c cast=(CGContextRef)
 */
public static final native long /*int*/ CGBitmapContextGetData(long /*int*/ c);
/**
 * @param space cast=(CGColorSpaceRef)
 * @param components cast=(CGFloat*)
 */
public static final native long /*int*/ CGColorCreate(long /*int*/ space, double[] /*float[]*/ components);
/**
 * @param color cast=(CGColorRef)
 */
public static final native void CGColorRelease(long /*int*/ color);
public static final native long /*int*/ CGColorSpaceCreateDeviceRGB();
/**
 * @param space cast=(CGColorSpaceRef)
 */
public static final native void CGColorSpaceRelease(long /*int*/ space);
/**
 * @param context cast=(CGContextRef)
 * @param path cast=(CGPathRef)
 */
public static final native void CGContextAddPath(long /*int*/ context, long /*int*/ path);
/**
 * @param context cast=(CGContextRef)
 * @param rect flags=struct
 * @param auxiliaryInfo cast=(CFDictionaryRef)
 */
public static final native void CGContextBeginTransparencyLayerWithRect(long /*int*/ context, CGRect rect, long /*int*/ auxiliaryInfo);
/**
 * @param c cast=(CGContextRef)
 * @param rect flags=struct
 * @param image cast=(CGImageRef)
 */
public static final native void CGContextDrawImage(long /*int*/ c, CGRect rect, long /*int*/ image);
/**
 * @param context cast=(CGContextRef)
 */
public static final native void CGContextEndTransparencyLayer(long /*int*/ context);
/**
 * @param c cast=(CGContextRef)
 * @param rect flags=struct
 */
public static final native void CGContextFillRect(long /*int*/ c, CGRect rect);
/**
 * @param c cast=(CGContextRef)
 */
public static final native void CGContextRelease(long /*int*/ c);
/**
 * @param c cast=(CGContextRef)
 */
public static final native void CGContextReplacePathWithStrokedPath(long /*int*/ c);
/**
 * @param c cast=(CGContextRef)
 */
public static final native void CGContextRestoreGState(long /*int*/ c);
/**
 * @param c cast=(CGContextRef)
 */
public static final native void CGContextSaveGState(long /*int*/ c);
/**
 * @param c cast=(CGContextRef)
 * @param sx cast=(CGFloat)
 * @param sy cast=(CGFloat)
 */
public static final native void CGContextScaleCTM(long /*int*/ c, double /*float*/ sx, double /*float*/ sy);
/**
 * @param context cast=(CGContextRef)
 * @param mode cast=(CGBlendMode)
 */
public static final native void CGContextSetBlendMode(long /*int*/ context, int mode);
/**
 * @param c cast=(CGContextRef)
 * @param components cast=(CGFloat*)
 */
public static final native void CGContextSetFillColor(long /*int*/ c, double[] /*float[]*/ components);
/**
 * @param c cast=(CGContextRef)
 * @param colorspace cast=(CGColorSpaceRef)
 */
public static final native void CGContextSetFillColorSpace(long /*int*/ c, long /*int*/ colorspace);
/**
 * @param c cast=(CGContextRef)
 * @param cap cast=(CGLineCap)
 */
public static final native void CGContextSetLineCap(long /*int*/ c, int cap);
/**
 * @param c cast=(CGContextRef)
 * @param phase cast=(CGFloat)
 * @param lengths cast=(CGFloat*)
 * @param count cast=(size_t)
 */
public static final native void CGContextSetLineDash(long /*int*/ c, double /*float*/ phase, float[] lengths, long /*int*/ count);
/**
 * @param c cast=(CGContextRef)
 * @param join cast=(CGLineJoin)
 */
public static final native void CGContextSetLineJoin(long /*int*/ c, int join);
/**
 * @param c cast=(CGContextRef)
 * @param width cast=(CGFloat)
 */
public static final native void CGContextSetLineWidth(long /*int*/ c, double /*float*/ width);
/**
 * @param c cast=(CGContextRef)
 * @param limit cast=(CGFloat)
 */
public static final native void CGContextSetMiterLimit(long /*int*/ c, double /*float*/ limit);
/**
 * @param c cast=(CGContextRef)
 * @param shouldAntialias cast=(_Bool)
 */
public static final native void CGContextSetShouldAntialias(long /*int*/ c, boolean shouldAntialias);
/**
 * @param c cast=(CGContextRef)
 * @param mode cast=(CGTextDrawingMode)
 */
public static final native void CGContextSetTextDrawingMode(long /*int*/ c, int mode);
/**
 * @param c cast=(CGContextRef)
 * @param t flags=struct
 */
public static final native void CGContextSetTextMatrix(long /*int*/ c, CGAffineTransform t);
/**
 * @param c cast=(CGContextRef)
 * @param x cast=(CGFloat)
 * @param y cast=(CGFloat)
 */
public static final native void CGContextSetTextPosition(long /*int*/ c, double /*float*/ x, double /*float*/ y);
/**
 * @param c cast=(CGContextRef)
 */
public static final native void CGContextStrokePath(long /*int*/ c);
/**
 * @param c cast=(CGContextRef)
 * @param tx cast=(CGFloat)
 * @param ty cast=(CGFloat)
 */
public static final native void CGContextTranslateCTM(long /*int*/ c, double /*float*/ tx, double /*float*/ ty);
/**
 * @param info cast=(void*)
 * @param data cast=(void*)
 * @param size cast=(size_t)
 * @param releaseData cast=(CGDataProviderReleaseDataCallback)
 */
public static final native long /*int*/ CGDataProviderCreateWithData(long /*int*/ info, long /*int*/ data, long /*int*/ size, long /*int*/ releaseData);
/**
 * @param provider cast=(CGDataProviderRef)
 */
public static final native void CGDataProviderRelease(long /*int*/ provider);
/**
 * @param display cast=(CGDirectDisplayID)
 */
public static final native long /*int*/ CGDisplayPixelsHigh(int display);
/**
 * @param display cast=(CGDirectDisplayID)
 */
public static final native long /*int*/ CGDisplayPixelsWide(int display);
/**
 * @param source cast=(CGEventSourceRef)
 * @param virtualKey cast=(CGKeyCode)
 * @param keyDown cast=(_Bool)
 */
public static final native long /*int*/ CGEventCreateKeyboardEvent(long /*int*/ source, short virtualKey, boolean keyDown);
/**
 * @param source cast=(CGEventSourceRef)
 * @param mouseType cast=(CGEventType)
 * @param mouseCursorPosition flags=struct
 * @param mouseButton cast=(CGMouseButton)
 */
public static final native long /*int*/ CGEventCreateMouseEvent(long /*int*/ source, int mouseType, CGPoint mouseCursorPosition, int mouseButton);
/**
 * @param source cast=(CGEventSourceRef)
 * @param units cast=(CGScrollEventUnit)
 * @param wheelCount cast=(CGWheelCount)
 * @param wheel1 cast=(int32_t)
 */
public static final native long /*int*/ CGEventCreateScrollWheelEvent(long /*int*/ source, int units, int wheelCount, int wheel1);
/**
 * @param event cast=(CGEventRef)
 * @param field cast=(CGEventField)
 */
public static final native long CGEventGetIntegerValueField(long /*int*/ event, int field);
/**
 * @param tap cast=(CGEventTapLocation)
 * @param event cast=(CGEventRef)
 */
public static final native void CGEventPost(int tap, long /*int*/ event);
/**
 * @param sourceState cast=(CGEventSourceStateID)
 */
public static final native long /*int*/ CGEventSourceCreate(int sourceState);
/**
 * @param rect flags=struct
 * @param maxDisplays cast=(CGDisplayCount)
 * @param dspys cast=(CGDirectDisplayID*)
 * @param dspyCnt cast=(CGDisplayCount*)
 */
public static final native int CGGetDisplaysWithRect(CGRect rect, int maxDisplays, long /*int*/ dspys, long /*int*/ dspyCnt);
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
public static final native long /*int*/ CGImageCreate(long /*int*/ width, long /*int*/ height, long /*int*/ bitsPerComponent, long /*int*/ bitsPerPixel, long /*int*/ bytesPerRow, long /*int*/ colorspace, int bitmapInfo, long /*int*/ provider, long /*int*/ decode, boolean shouldInterpolate, int intent);
/**
 * @param image cast=(CGImageRef)
 */
public static final native long /*int*/ CGImageGetHeight(long /*int*/ image);
/**
 * @param image cast=(CGImageRef)
 */
public static final native long /*int*/ CGImageGetWidth(long /*int*/ image);
/**
 * @param image cast=(CGImageRef)
 */
public static final native void CGImageRelease(long /*int*/ image);
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
public static final native void CGPathAddCurveToPoint(long /*int*/ path, long /*int*/ m, double /*float*/ cp1x, double /*float*/ cp1y, double /*float*/ cp2x, double /*float*/ cp2y, double /*float*/ x, double /*float*/ y);
/**
 * @param path cast=(CGMutablePathRef)
 * @param m cast=(CGAffineTransform*)
 * @param x cast=(CGFloat)
 * @param y cast=(CGFloat)
 */
public static final native void CGPathAddLineToPoint(long /*int*/ path, long /*int*/ m, double /*float*/ x, double /*float*/ y);
/**
 * @param path cast=(CGMutablePathRef)
 * @param m cast=(CGAffineTransform*)
 * @param rect flags=struct
 */
public static final native void CGPathAddRect(long /*int*/ path, long /*int*/ m, CGRect rect);
/**
 * @param path cast=(CGPathRef)
 * @param info cast=(void*)
 * @param function cast=(CGPathApplierFunction)
 */
public static final native void CGPathApply(long /*int*/ path, long /*int*/ info, long /*int*/ function);
/**
 * @param path cast=(CGMutablePathRef)
 */
public static final native void CGPathCloseSubpath(long /*int*/ path);
/**
 * @param path cast=(CGPathRef)
 */
public static final native long /*int*/ CGPathCreateCopy(long /*int*/ path);
public static final native long /*int*/ CGPathCreateMutable();
/**
 * @param path cast=(CGMutablePathRef)
 * @param m cast=(CGAffineTransform*)
 * @param x cast=(CGFloat)
 * @param y cast=(CGFloat)
 */
public static final native void CGPathMoveToPoint(long /*int*/ path, long /*int*/ m, double /*float*/ x, double /*float*/ y);
/**
 * @param path cast=(CGPathRef)
 */
public static final native void CGPathRelease(long /*int*/ path);
/**
 * @param keyChar cast=(CGCharCode)
 * @param virtualKey cast=(CGKeyCode)
 * @param keyDown cast=(boolean_t)
 */
public static final native int CGPostKeyboardEvent(short keyChar, short virtualKey, boolean keyDown);
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
 * @param font cast=(CTFontRef)
 */
public static final native double /*float*/ CTFontGetAscent(long /*int*/ font);
/**
 * @param font cast=(CTFontRef)
 */
public static final native double /*float*/ CTFontGetDescent(long /*int*/ font);
/**
 * @param font cast=(CTFontRef)
 */
public static final native double /*float*/ CTFontGetLeading(long /*int*/ font);
/**
 * @param attrString cast=(CFAttributedStringRef)
 */
public static final native long /*int*/ CTLineCreateWithAttributedString(long /*int*/ attrString);
/**
 * @param line cast=(CTLineRef)
 * @param context cast=(CGContextRef)
 */
public static final native void CTLineDraw(long /*int*/ line, long /*int*/ context);
/**
 * @param line cast=(CTLineRef)
 * @param ascent cast=(CGFloat*)
 * @param descent cast=(CGFloat*)
 * @param leading cast=(CGFloat*)
 */
public static final native double CTLineGetTypographicBounds(long /*int*/ line, double[] /*float[]*/ ascent, double[] /*float[]*/ descent, double[] /*float[]*/ leading);
/**
 * @param settings cast=(CTParagraphStyleSetting*)
 * @param settingCount cast=(size_t)
 */
public static final native long /*int*/ CTParagraphStyleCreate(long /*int*/ settings, long /*int*/ settingCount);
/**
 * @param typesetter cast=(CTTypesetterRef)
 * @param stringRange flags=struct
 */
public static final native long /*int*/ CTTypesetterCreateLine(long /*int*/ typesetter, CFRange stringRange);
/**
 * @param string cast=(CFAttributedStringRef)
 */
public static final native long /*int*/ CTTypesetterCreateWithAttributedString(long /*int*/ string);
/**
 * @param typesetter cast=(CTTypesetterRef)
 * @param startIndex cast=(CFIndex)
 * @param width cast=(double)
 */
public static final native long /*int*/ CTTypesetterSuggestLineBreak(long /*int*/ typesetter, long /*int*/ startIndex, double width);
/**
 * @param aRect flags=struct
 * @param bRect flags=struct
 */
public static final native boolean NSEqualRects(NSRect aRect, NSRect bRect);
/**
 * @param hfsFileTypeCode cast=(OSType)
 */
public static final native long /*int*/ NSFileTypeForHFSTypeCode(int hfsFileTypeCode);
/**
 * @param typePtr cast=(char*)
 * @param sizep cast=(NSUInteger*)
 * @param alignp cast=(NSUInteger*)
 */
public static final native long /*int*/ NSGetSizeAndAlignment(long /*int*/ typePtr, long[] /*int[]*/ sizep, long[] /*int[]*/ alignp);
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
public static final native long /*int*/ NSSearchPathForDirectoriesInDomains(long /*int*/ directory, long /*int*/ domainMask, boolean expandTilde);
public static final native long /*int*/ NSTemporaryDirectory();

/** Super Sends */

/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native boolean objc_msgSendSuper_bool(objc_super superId, long /*int*/ sel, NSRange arg0, long /*int*/ arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native boolean objc_msgSendSuper_bool(objc_super superId, long /*int*/ sel, long /*int*/ arg0, NSPoint arg1);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSendSuper(objc_super superId, long /*int*/ sel);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSendSuper(objc_super superId, long /*int*/ sel, NSPoint arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSendSuper(objc_super superId, long /*int*/ sel, NSRect arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSendSuper(objc_super superId, long /*int*/ sel, NSRect arg0, long /*int*/ arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSendSuper(objc_super superId, long /*int*/ sel, NSSize arg0);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSendSuper(objc_super superId, long /*int*/ sel, boolean arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSendSuper(objc_super superId, long /*int*/ sel, boolean arg0, NSRect arg1);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSendSuper(objc_super superId, long /*int*/ sel, long /*int*/ arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSendSuper(objc_super superId, long /*int*/ sel, long /*int*/ arg0, NSPoint arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSendSuper(objc_super superId, long /*int*/ sel, long /*int*/ arg0, NSPoint arg1, long /*int*/ arg2);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSendSuper(objc_super superId, long /*int*/ sel, long /*int*/ arg0, NSRect arg1, long /*int*/ arg2);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSendSuper(objc_super superId, long /*int*/ sel, long /*int*/ arg0, boolean arg1);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSendSuper(objc_super superId, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSendSuper(objc_super superId, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, boolean arg3);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSendSuper(objc_super superId, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native void objc_msgSendSuper_stret(NSRect result, objc_super superId, long /*int*/ sel, NSRect arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native void objc_msgSendSuper_stret(NSRect result, objc_super superId, long /*int*/ sel, NSRect arg0, long /*int*/ arg1);
/** @method flags=cast,fixedargs=2 */
public static final native void objc_msgSendSuper_stret(NSRect result, objc_super superId, long /*int*/ sel, long /*int*/ arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native void objc_msgSendSuper_stret(NSRect result, objc_super superId, long /*int*/ sel, long /*int*/ arg0, NSRect arg1, long /*int*/ arg2);
/** @method flags=cast,fixedargs=2 */
public static final native void objc_msgSendSuper_stret(NSSize result, objc_super superId, long /*int*/ sel);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native void objc_msgSendSuper_stret(NSSize result, objc_super superId, long /*int*/ sel, NSRect arg0);
/** @method flags=cast,fixedargs=2 */
public static final native void objc_msgSendSuper_stret(NSSize result, objc_super superId, long /*int*/ sel, boolean arg0);

/** Sends */

/** @method flags=cast,fixedargs=2 */
public static final native boolean objc_msgSend_bool(long /*int*/ id, long /*int*/ sel);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native boolean objc_msgSend_bool(long /*int*/ id, long /*int*/ sel, NSPoint arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 */
public static final native boolean objc_msgSend_bool(long /*int*/ id, long /*int*/ sel, NSPoint arg0, NSRect arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native boolean objc_msgSend_bool(long /*int*/ id, long /*int*/ sel, NSRange arg0, long /*int*/ arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native boolean objc_msgSend_bool(long /*int*/ id, long /*int*/ sel, NSRect arg0);
/** @method flags=cast,fixedargs=2 */
public static final native boolean objc_msgSend_bool(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native boolean objc_msgSend_bool(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, NSPoint arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native boolean objc_msgSend_bool(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, NSSize arg1, boolean arg2);
/** @method flags=cast,fixedargs=2 */
public static final native boolean objc_msgSend_bool(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1);
/** @method flags=cast,fixedargs=2 */
public static final native boolean objc_msgSend_bool(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2);
/** @method flags=cast,fixedargs=2 */
public static final native boolean objc_msgSend_bool(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3);
/** @method flags=cast,fixedargs=2 */
public static final native boolean objc_msgSend_bool(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3, long /*int*/ arg4);
/** @method flags=cast,fixedargs=2 */
public static final native boolean objc_msgSend_bool(long /*int*/ id, long /*int*/ sel, short arg0);
/** @method flags=cast,fixedargs=2 */
public static final native double objc_msgSend_fpret(long /*int*/ id, long /*int*/ sel);
/** @method flags=cast,fixedargs=2 */
public static final native double objc_msgSend_fpret(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0);
/** @method flags=cast,fixedargs=2 */
public static final native double objc_msgSend_fpret(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1);
/** @method flags=cast,fixedargs=2 */
public static final native float objc_msgSend_floatret(long /*int*/ id, long /*int*/ sel);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSAffineTransformStruct arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSPoint arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 * @param arg2 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSPoint arg0, NSPoint arg1, NSPoint arg2);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSPoint arg0, NSPoint arg1, long /*int*/ arg2);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSPoint arg0, NSRect arg1, long /*int*/ arg2, double /*float*/ arg3);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSPoint arg0, double /*float*/ arg1, double /*float*/ arg2, double /*float*/ arg3);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSPoint arg0, double /*float*/ arg1, double /*float*/ arg2, double /*float*/ arg3, boolean arg4);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSPoint arg0, long /*int*/ arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSPoint arg0, long /*int*/ arg1, double[] /*float[]*/ arg2);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRange arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRange arg0, NSPoint arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRange arg0, NSRange arg1, long /*int*/ arg2, long[] /*int[]*/ arg3);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRange arg0, long /*int*/ arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRange arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3, long /*int*/ arg4, byte[] arg5);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRect arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRect arg0, NSPoint arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 * @param arg2 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRect arg0, NSRange arg1, NSRect arg2);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRect arg0, NSRect arg1, long /*int*/ arg2, double /*float*/ arg3);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRect arg0, NSSize arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRect arg0, boolean arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRect arg0, boolean arg1, boolean arg2);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRect arg0, double /*float*/ arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRect arg0, double /*float*/ arg1, double /*float*/ arg2);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRect arg0, long /*int*/ arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRect arg0, long /*int*/ arg1, boolean arg2, long /*int*/ arg3);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRect arg0, long /*int*/ arg1, long /*int*/ arg2);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRect arg0, long /*int*/ arg1, long /*int*/ arg2, boolean arg3);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRect arg0, long /*int*/ arg1, long /*int*/ arg2, boolean arg3, long /*int*/ arg4);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSRect arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, NSSize arg0);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, boolean arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, boolean arg0, NSRect arg1);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, boolean arg0, long /*int*/ arg1);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, byte[] arg0);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, byte[] arg0, long /*int*/ arg1);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, char[] arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, char[] arg0, NSRange arg1);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, char[] arg0, long /*int*/ arg1);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, double arg0);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, double arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3, boolean arg4);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, double /*float*/ arg0, double /*float*/ arg1);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, double /*float*/ arg0, double /*float*/ arg1, double /*float*/ arg2, double /*float*/ arg3);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, double /*float*/ arg0, long /*int*/ arg1);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, double[] /*float[]*/ arg0);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, double[] /*float[]*/ arg0, long /*int*/ arg1, double /*float*/ arg2);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, NSPoint arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 * @param arg2 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, NSPoint arg1, NSSize arg2, long /*int*/ arg3, long /*int*/ arg4, long /*int*/ arg5, boolean arg6);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, NSPoint arg1, long /*int*/ arg2);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, NSPoint arg1, long /*int*/ arg2, double arg3, long /*int*/ arg4, long /*int*/ arg5, long /*int*/ arg6, long /*int*/ arg7, long /*int*/ arg8);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, NSPoint arg1, long /*int*/ arg2, double arg3, long /*int*/ arg4, long /*int*/ arg5, short arg6, long /*int*/ arg7, long /*int*/ arg8);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, NSRange arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, NSRect arg1, long /*int*/ arg2);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, boolean arg1);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, double /*float*/ arg1);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg2 flags=struct
 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, NSRange arg2);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, boolean arg2);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, double arg2, long /*int*/ arg3);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, boolean arg3);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, double /*float*/ arg3);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3, long /*int*/ arg4);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3, long /*int*/ arg4, boolean arg5, boolean arg6, long /*int*/ arg7, long /*int*/ arg8, long /*int*/ arg9);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3, long /*int*/ arg4, boolean arg5, boolean arg6, long /*int*/ arg7, long /*int*/ arg8, long /*int*/ arg9, long /*int*/ arg10);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3, long /*int*/ arg4, long /*int*/ arg5);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, long /*int*/ arg2, long /*int*/ arg3, long /*int*/ arg4, long /*int*/ arg5, long /*int*/ arg6);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long[] /*int[]*/ arg0);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, long[] /*int[]*/ arg0, int arg1, int arg2);
/** @method flags=cast,fixedargs=2 */
public static final native long /*int*/ objc_msgSend(long /*int*/ id, long /*int*/ sel, int[] arg0, long /*int*/ arg1);
/** @method flags=cast,fixedargs=2 */
public static final native int objc_msgSend(int id, int sel, float arg0);
/** @method flags=cast,fixedargs=2 */
public static final native long objc_msgSend(long id, long sel, int arg0);
/** @method flags=cast,fixedargs=2 */
public static final native long objc_msgSend(long id, long sel, int[] arg0);
/** @method flags=cast,fixedargs=2 */
public static final native long objc_msgSend(long id, long sel, long[] arg0, long arg1, long arg2);
/** @method flags=cast,fixedargs=2 */
public static final native void objc_msgSend_stret(NSAffineTransformStruct result, long /*int*/ id, long /*int*/ sel);
/** @method flags=cast,fixedargs=2 */
public static final native void objc_msgSend_stret(NSPoint result, long /*int*/ id, long /*int*/ sel);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSPoint result, long /*int*/ id, long /*int*/ sel, NSPoint arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSPoint result, long /*int*/ id, long /*int*/ sel, NSPoint arg0, long /*int*/ arg1);
/** @method flags=cast,fixedargs=2 */
public static final native void objc_msgSend_stret(NSPoint result, long /*int*/ id, long /*int*/ sel, long /*int*/ arg0);
/** @method flags=cast,fixedargs=2 */
public static final native void objc_msgSend_stret(NSRange result, long /*int*/ id, long /*int*/ sel);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSRange result, long /*int*/ id, long /*int*/ sel, NSRange arg0, long /*int*/ arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSRange result, long /*int*/ id, long /*int*/ sel, NSRect arg0);
/** @method flags=cast,fixedargs=2 */
public static final native void objc_msgSend_stret(NSRange result, long /*int*/ id, long /*int*/ sel, long /*int*/ arg0);
/** @method flags=cast,fixedargs=2 */
public static final native void objc_msgSend_stret(NSRect result, long /*int*/ id, long /*int*/ sel);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSRect result, long /*int*/ id, long /*int*/ sel, NSRange arg0, long /*int*/ arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSRect result, long /*int*/ id, long /*int*/ sel, NSRect arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSRect result, long /*int*/ id, long /*int*/ sel, NSRect arg0, long /*int*/ arg1);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSRect result, long /*int*/ id, long /*int*/ sel, NSSize arg0, long /*int*/ arg1);
/** @method flags=cast,fixedargs=2 */
public static final native void objc_msgSend_stret(NSRect result, long /*int*/ id, long /*int*/ sel, long /*int*/ arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg1 flags=struct
 */
public static final native void objc_msgSend_stret(NSRect result, long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, NSRect arg1, long /*int*/ arg2);
/** @method flags=cast,fixedargs=2 */
public static final native void objc_msgSend_stret(NSRect result, long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1);
/** @method flags=cast,fixedargs=2 */
public static final native void objc_msgSend_stret(NSRect result, long /*int*/ id, long /*int*/ sel, long /*int*/ arg0, long /*int*/ arg1, boolean arg2);
/** @method flags=cast,fixedargs=2 */
public static final native void objc_msgSend_stret(NSSize result, long /*int*/ id, long /*int*/ sel);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSSize result, long /*int*/ id, long /*int*/ sel, NSRect arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSSize result, long /*int*/ id, long /*int*/ sel, NSSize arg0);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSSize result, long /*int*/ id, long /*int*/ sel, NSSize arg0, boolean arg1, boolean arg2, long /*int*/ arg3);
/**
 * @method flags=cast,fixedargs=2
 * @param arg0 flags=struct
 */
public static final native void objc_msgSend_stret(NSSize result, long /*int*/ id, long /*int*/ sel, NSSize arg0, long /*int*/ arg1);
/** @method flags=cast,fixedargs=2 */
public static final native void objc_msgSend_stret(NSSize result, long /*int*/ id, long /*int*/ sel, boolean arg0);

/** Sizeof natives */
public static final native int CFRange_sizeof();
public static final native int CGAffineTransform_sizeof();
public static final native int CGPathElement_sizeof();
public static final native int CGPoint_sizeof();
public static final native int CGRect_sizeof();
public static final native int CGSize_sizeof();
public static final native int CTParagraphStyleSetting_sizeof();
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
public static final native void memmove(long /*int*/ dest, CFRange src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(CFRange dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(long /*int*/ dest, CGAffineTransform src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(CGAffineTransform dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(long /*int*/ dest, CGPathElement src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(CGPathElement dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(long /*int*/ dest, CGPoint src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(CGPoint dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(long /*int*/ dest, CGRect src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(CGRect dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(long /*int*/ dest, CGSize src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(CGSize dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(long /*int*/ dest, CTParagraphStyleSetting src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(CTParagraphStyleSetting dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(long /*int*/ dest, NSAffineTransformStruct src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(NSAffineTransformStruct dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(long /*int*/ dest, NSPoint src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(NSPoint dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(long /*int*/ dest, NSRange src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(NSRange dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(long /*int*/ dest, NSRect src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(NSRect dest, long /*int*/ src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(long /*int*/ dest, NSSize src, long /*int*/ size);
/**
 * @param dest cast=(void *),flags=no_in critical
 * @param src cast=(void *),flags=critical
 */
public static final native void memmove(NSSize dest, long /*int*/ src, long /*int*/ size);

/** This section is auto generated */

}
