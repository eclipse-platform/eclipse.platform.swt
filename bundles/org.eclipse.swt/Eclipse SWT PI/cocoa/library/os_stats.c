/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "os_stats.h"

#ifdef NATIVE_STATS

int OS_nativeFunctionCount = 458;
int OS_nativeFunctionCallCount[458];
char * OS_nativeFunctionNames[] = {
	"ATSFontActivateFromFileReference",
	"AcquireRootMenu",
	"CFDataGetBytePtr",
	"CFDataGetLength",
	"CFRelease",
	"CFRunLoopAddObserver",
	"CFRunLoopGetCurrent",
	"CFRunLoopObserverCreate",
	"CFRunLoopObserverInvalidate",
	"CFURLCreateStringByAddingPercentEscapes",
	"CGBitmapContextCreate",
	"CGBitmapContextCreateImage",
	"CGBitmapContextGetData",
	"CGColorSpaceCreateDeviceRGB",
	"CGColorSpaceRelease",
	"CGContextAddPath",
	"CGContextCopyPath",
	"CGContextCopyWindowContentsToRect",
	"CGContextDrawImage",
	"CGContextRelease",
	"CGContextReplacePathWithStrokedPath",
	"CGContextRestoreGState",
	"CGContextSaveGState",
	"CGContextSetLineCap",
	"CGContextSetLineDash",
	"CGContextSetLineJoin",
	"CGContextSetLineWidth",
	"CGContextSetMiterLimit",
	"CGContextTranslateCTM",
	"CGDataProviderCreateWithData",
	"CGDataProviderRelease",
	"CGDisplayBaseAddress",
	"CGDisplayBitsPerPixel",
	"CGDisplayBitsPerSample",
	"CGDisplayBounds",
	"CGDisplayBytesPerRow",
	"CGDisplayPixelsHigh",
	"CGDisplayPixelsWide",
	"CGEnableEventStateCombining",
	"CGEventCreateKeyboardEvent",
	"CGEventGetIntegerValueField",
	"CGEventKeyboardSetUnicodeString",
	"CGEventPost",
	"CGGetDisplaysWithRect",
	"CGImageCreate",
	"CGImageGetHeight",
	"CGImageGetWidth",
	"CGImageRelease",
	"CGPathAddCurveToPoint",
	"CGPathAddLineToPoint",
	"CGPathApply",
	"CGPathCloseSubpath",
	"CGPathCreateCopy",
	"CGPathCreateMutable",
	"CGPathElement_1sizeof",
	"CGPathMoveToPoint",
	"CGPathRelease",
	"CGPoint_1sizeof",
	"CGPostMouseEvent",
	"CGPostScrollWheelEvent",
	"CGRect_1sizeof",
	"CGSetLocalEventsFilterDuringSuppressionState",
	"CGSetLocalEventsSuppressionInterval",
	"CGSize_1sizeof",
	"CGWarpMouseCursorPosition",
	"CPSSetProcessName",
	"CancelMenuTracking",
	"CloseRgn",
	"CopyRgn",
	"DeleteGlobalRef",
	"DiffRgn",
	"DisposeRgn",
	"EmptyRgn",
	"FSPathMakeRef",
	"Gestalt",
	"GetCurrentButtonState",
	"GetCurrentProcess",
	"GetDblTime",
	"GetRegionBounds",
	"JNIGetObject",
	"LMGetKbdType",
	"LineTo",
	"MoveTo",
	"NSAccessibilityActionDescription",
	"NSAccessibilityButtonRole",
	"NSAccessibilityCheckBoxRole",
	"NSAccessibilityChildrenAttribute",
	"NSAccessibilityColumnRole",
	"NSAccessibilityComboBoxRole",
	"NSAccessibilityConfirmAction",
	"NSAccessibilityContentsAttribute",
	"NSAccessibilityDescriptionAttribute",
	"NSAccessibilityDialogSubrole",
	"NSAccessibilityEnabledAttribute",
	"NSAccessibilityExpandedAttribute",
	"NSAccessibilityFloatingWindowSubrole",
	"NSAccessibilityFocusedAttribute",
	"NSAccessibilityFocusedUIElementChangedNotification",
	"NSAccessibilityGridRole",
	"NSAccessibilityGroupRole",
	"NSAccessibilityHelpAttribute",
	"NSAccessibilityHelpTagRole",
	"NSAccessibilityHorizontalOrientationValue",
	"NSAccessibilityHorizontalScrollBarAttribute",
	"NSAccessibilityImageRole",
	"NSAccessibilityIncrementorRole",
	"NSAccessibilityInsertionPointLineNumberAttribute",
	"NSAccessibilityLabelValueAttribute",
	"NSAccessibilityLineForIndexParameterizedAttribute",
	"NSAccessibilityLinkRole",
	"NSAccessibilityLinkTextAttribute",
	"NSAccessibilityListRole",
	"NSAccessibilityMaxValueAttribute",
	"NSAccessibilityMenuBarRole",
	"NSAccessibilityMenuButtonRole",
	"NSAccessibilityMenuItemRole",
	"NSAccessibilityMenuRole",
	"NSAccessibilityMinValueAttribute",
	"NSAccessibilityNextContentsAttribute",
	"NSAccessibilityNumberOfCharactersAttribute",
	"NSAccessibilityOrientationAttribute",
	"NSAccessibilityOutlineRole",
	"NSAccessibilityOutlineRowSubrole",
	"NSAccessibilityParentAttribute",
	"NSAccessibilityPopUpButtonRole",
	"NSAccessibilityPositionAttribute",
	"NSAccessibilityPostNotification",
	"NSAccessibilityPressAction",
	"NSAccessibilityPreviousContentsAttribute",
	"NSAccessibilityProgressIndicatorRole",
	"NSAccessibilityRTFForRangeParameterizedAttribute",
	"NSAccessibilityRadioButtonRole",
	"NSAccessibilityRadioGroupRole",
	"NSAccessibilityRaiseBadArgumentException",
	"NSAccessibilityRangeForIndexParameterizedAttribute",
	"NSAccessibilityRangeForLineParameterizedAttribute",
	"NSAccessibilityRangeForPositionParameterizedAttribute",
	"NSAccessibilityRoleAttribute",
	"NSAccessibilityRoleDescription",
	"NSAccessibilityRoleDescriptionAttribute",
	"NSAccessibilityRoleDescriptionForUIElement",
	"NSAccessibilityRowRole",
	"NSAccessibilityScrollAreaRole",
	"NSAccessibilityScrollBarRole",
	"NSAccessibilitySelectedAttribute",
	"NSAccessibilitySelectedChildrenAttribute",
	"NSAccessibilitySelectedChildrenChangedNotification",
	"NSAccessibilitySelectedTextAttribute",
	"NSAccessibilitySelectedTextChangedNotification",
	"NSAccessibilitySelectedTextRangeAttribute",
	"NSAccessibilitySelectedTextRangesAttribute",
	"NSAccessibilityServesAsTitleForUIElementsAttribute",
	"NSAccessibilitySizeAttribute",
	"NSAccessibilitySliderRole",
	"NSAccessibilitySortButtonRole",
	"NSAccessibilitySplitterRole",
	"NSAccessibilityStandardWindowSubrole",
	"NSAccessibilityStaticTextRole",
	"NSAccessibilityStringForRangeParameterizedAttribute",
	"NSAccessibilityStyleRangeForIndexParameterizedAttribute",
	"NSAccessibilitySubroleAttribute",
	"NSAccessibilitySystemDialogSubrole",
	"NSAccessibilityTabGroupRole",
	"NSAccessibilityTableRole",
	"NSAccessibilityTableRowSubrole",
	"NSAccessibilityTabsAttribute",
	"NSAccessibilityTextAreaRole",
	"NSAccessibilityTextFieldRole",
	"NSAccessibilityTextLinkSubrole",
	"NSAccessibilityTitleAttribute",
	"NSAccessibilityTitleUIElementAttribute",
	"NSAccessibilityToolbarRole",
	"NSAccessibilityTopLevelUIElementAttribute",
	"NSAccessibilityUnignoredAncestor",
	"NSAccessibilityUnignoredChildren",
	"NSAccessibilityUnignoredChildrenForOnlyChild",
	"NSAccessibilityUnignoredDescendant",
	"NSAccessibilityUnknownRole",
	"NSAccessibilityUnknownSubrole",
	"NSAccessibilityValueAttribute",
	"NSAccessibilityValueChangedNotification",
	"NSAccessibilityValueDescriptionAttribute",
	"NSAccessibilityValueIndicatorRole",
	"NSAccessibilityVerticalOrientationValue",
	"NSAccessibilityVerticalScrollBarAttribute",
	"NSAccessibilityVisibleCharacterRangeAttribute",
	"NSAccessibilityVisibleChildrenAttribute",
	"NSAccessibilityWindowAttribute",
	"NSAccessibilityWindowRole",
	"NSAffineTransformStruct_1sizeof",
	"NSBackgroundColorAttributeName",
	"NSBaselineOffsetAttributeName",
	"NSBeep",
	"NSBitsPerPixelFromDepth",
	"NSCalibratedRGBColorSpace",
	"NSCopyBits",
	"NSDefaultRunLoopMode",
	"NSDeviceRGBColorSpace",
	"NSDeviceResolution",
	"NSDragPboard",
	"NSEqualRects",
	"NSErrorFailingURLStringKey",
	"NSEventTrackingRunLoopMode",
	"NSFileTypeForHFSTypeCode",
	"NSFilenamesPboardType",
	"NSFontAttributeName",
	"NSForegroundColorAttributeName",
	"NSGetSizeAndAlignment",
	"NSHTMLPboardType",
	"NSIntersectionRect",
	"NSLinkAttributeName",
	"NSNumberOfColorComponents",
	"NSObliquenessAttributeName",
	"NSParagraphStyleAttributeName",
	"NSPointInRect",
	"NSPoint_1sizeof",
	"NSPrintAllPages",
	"NSPrintCopies",
	"NSPrintFirstPage",
	"NSPrintJobDisposition",
	"NSPrintLastPage",
	"NSPrintMustCollate",
	"NSPrintPreviewJob",
	"NSPrintSaveJob",
	"NSPrintSavePath",
	"NSPrintScalingFactor",
	"NSPrintSpoolJob",
	"NSRTFPboardType",
	"NSRange_1sizeof",
	"NSRect_1sizeof",
	"NSSearchPathForDirectoriesInDomains",
	"NSSize_1sizeof",
	"NSStrikethroughColorAttributeName",
	"NSStrikethroughStyleAttributeName",
	"NSStringPboardType",
	"NSStrokeWidthAttributeName",
	"NSTIFFPboardType",
	"NSTemporaryDirectory",
	"NSToolbarCustomizeToolbarItemIdentifier",
	"NSToolbarDidRemoveItemNotification",
	"NSToolbarFlexibleSpaceItemIdentifier",
	"NSToolbarPrintItemIdentifier",
	"NSToolbarSeparatorItemIdentifier",
	"NSToolbarShowColorsItemIdentifier",
	"NSToolbarShowFontsItemIdentifier",
	"NSToolbarSpaceItemIdentifier",
	"NSToolbarWillAddItemNotification",
	"NSURLPboardType",
	"NSUnderlineColorAttributeName",
	"NSUnderlineStyleAttributeName",
	"NewGlobalRef",
	"NewRgn",
	"OffsetRgn",
	"OpenRgn",
	"PtInRgn",
	"QDRegionToRects",
	"RectInRgn",
	"RectRgn",
	"SectRgn",
	"SetFrontProcess",
	"SetRect",
	"SetThemeCursor",
	"TISCopyCurrentKeyboardInputSource",
	"TISGetInputSourceProperty",
	"TransformProcessType",
	"UCKeyTranslate",
	"UnionRgn",
	"_1_1BIG_1ENDIAN_1_1",
	"accessibilityHitTest_1CALLBACK",
	"attributedSubstringFromRange_1CALLBACK",
	"call",
	"cellSize_1CALLBACK",
	"characterIndexForPoint_1CALLBACK",
	"class_1addIvar",
	"class_1addMethod",
	"class_1addProtocol",
	"class_1createInstance",
	"class_1getClassMethod",
	"class_1getInstanceMethod",
	"class_1getMethodImplementation",
	"class_1getName",
	"class_1getSuperclass",
	"dragSelectionWithEvent_1offset_1slideBack_1CALLBACK",
	"draggedImage_1beganAt_1CALLBACK",
	"draggedImage_1endedAt_1operation_1CALLBACK",
	"draggedImage_1movedTo_1CALLBACK",
	"drawImage_1withFrame_1inView_1CALLBACK",
	"drawInteriorWithFrame_1inView_1CALLBACK",
	"drawRect_1CALLBACK",
	"drawWithFrame_1inView_1CALLBACK",
	"firstRectForCharacterRange_1CALLBACK",
	"getpid",
	"highlightSelectionInClipRect_1CALLBACK",
	"hitTestForEvent_1inRect_1ofView_1CALLBACK",
	"hitTest_1CALLBACK",
	"imageRectForBounds_1CALLBACK",
	"instrumentObjcMessageSends",
	"isFlipped_1CALLBACK",
	"kCFRunLoopCommonModes",
	"kTISPropertyUnicodeKeyLayoutData",
	"markedRange_1CALLBACK",
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_cocoa_CGPathElement_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_cocoa_CGPathElement_2J",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_cocoa_CGPoint_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_cocoa_CGPoint_2J",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_cocoa_CGRect_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_cocoa_CGRect_2J",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_cocoa_CGSize_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_cocoa_CGSize_2J",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2J",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_cocoa_NSPoint_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_cocoa_NSPoint_2J",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_cocoa_NSRange_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_cocoa_NSRange_2J",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_cocoa_NSRect_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_cocoa_NSRect_2J",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_cocoa_NSSize_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_cocoa_NSSize_2J",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_cocoa_CGPathElement_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_cocoa_CGPathElement_2JJ",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2JJ",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2JJ",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2JJ",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2JJ",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJ",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJ",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJ",
#endif
	"method_1setImplementation",
	"objc_1allocateClassPair",
	"objc_1getClass",
	"objc_1getMetaClass",
	"objc_1getProtocol",
	"objc_1lookUpClass",
#ifndef JNI64
	"objc_1msgSend__II",
#else
	"objc_1msgSend__JJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IID",
#else
	"objc_1msgSend__JJD",
#endif
#ifndef JNI64
	"objc_1msgSend__IIDIIIZ",
#else
	"objc_1msgSend__JJDJJJZ",
#endif
	"objc_1msgSend__IIF",
#ifndef JNI64
	"objc_1msgSend__IIFF",
#else
	"objc_1msgSend__JJDD",
#endif
#ifndef JNI64
	"objc_1msgSend__IIFFFF",
#else
	"objc_1msgSend__JJDDDD",
#endif
#ifndef JNI64
	"objc_1msgSend__IIFI",
#else
	"objc_1msgSend__JJDJ",
#endif
#ifndef JNI64
	"objc_1msgSend__III",
#else
	"objc_1msgSend__JJJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IIIF",
#else
	"objc_1msgSend__JJJD",
#endif
#ifndef JNI64
	"objc_1msgSend__IIII",
#else
	"objc_1msgSend__JJJJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IIIII",
#else
	"objc_1msgSend__JJJJJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IIIIIF",
#else
	"objc_1msgSend__JJJJJD",
#endif
#ifndef JNI64
	"objc_1msgSend__IIIIII",
#else
	"objc_1msgSend__JJJJJJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IIIIIII",
#else
	"objc_1msgSend__JJJJJJJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IIIIIIIII",
#else
	"objc_1msgSend__JJJJJJJJJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IIIIIIIZZIII",
#else
	"objc_1msgSend__JJJJJJJZZJJJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IIIIIIIZZIIII",
#else
	"objc_1msgSend__JJJJJJJZZJJJJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IIIIIZ",
#else
	"objc_1msgSend__JJJJJZ",
#endif
#ifndef JNI64
	"objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2",
#else
	"objc_1msgSend__JJJJLorg_eclipse_swt_internal_cocoa_NSRange_2",
#endif
#ifndef JNI64
	"objc_1msgSend__IIIIZ",
#else
	"objc_1msgSend__JJJJZ",
#endif
#ifndef JNI64
	"objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2",
#else
	"objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2",
#endif
#ifndef JNI64
	"objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIIIII",
#else
	"objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2JDJJJJJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIISII",
#else
	"objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2JDJJSJJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSSize_2IIIZ",
#else
	"objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSSize_2JJJZ",
#endif
#ifndef JNI64
	"objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2",
#else
	"objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSRange_2",
#endif
#ifndef JNI64
	"objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRect_2I",
#else
	"objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSRect_2J",
#endif
#ifndef JNI64
	"objc_1msgSend__IIIZ",
#else
	"objc_1msgSend__JJJZ",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFF",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2DDD",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFFZ",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2DDDZ",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_3F",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2J_3D",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2J",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2JD",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2I",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2J",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IIIII",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2JJJJJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2II",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2JJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2F",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2D",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2FF",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2DD",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2J",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2III",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJZ",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZI",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJZJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IZI",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JZJ",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2JD",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Z",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Z",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSSize_2",
#endif
#ifndef JNI64
	"objc_1msgSend__IIZ",
#else
	"objc_1msgSend__JJZ",
#endif
#ifndef JNI64
	"objc_1msgSend__IIZI",
#else
	"objc_1msgSend__JJZJ",
#endif
#ifndef JNI64
	"objc_1msgSend__II_3B",
#else
	"objc_1msgSend__JJ_3B",
#endif
#ifndef JNI64
	"objc_1msgSend__II_3BI",
#else
	"objc_1msgSend__JJ_3BJ",
#endif
#ifndef JNI64
	"objc_1msgSend__II_3C",
#else
	"objc_1msgSend__JJ_3C",
#endif
#ifndef JNI64
	"objc_1msgSend__II_3CI",
#else
	"objc_1msgSend__JJ_3CJ",
#endif
#ifndef JNI64
	"objc_1msgSend__II_3CLorg_eclipse_swt_internal_cocoa_NSRange_2",
#else
	"objc_1msgSend__JJ_3CLorg_eclipse_swt_internal_cocoa_NSRange_2",
#endif
#ifndef JNI64
	"objc_1msgSend__II_3F",
#else
	"objc_1msgSend__JJ_3D",
#endif
#ifndef JNI64
	"objc_1msgSend__II_3FIF",
#else
	"objc_1msgSend__JJ_3DJD",
#endif
#ifndef JNI64
	"objc_1msgSend__II_3I",
#else
	"objc_1msgSend__JJ_3J",
#endif
#ifndef JNI64
	"objc_1msgSend__II_3III",
#else
	"objc_1msgSend__JJ_3JII",
#endif
	"objc_1msgSend__JJFD",
	"objc_1msgSend__JJI",
	"objc_1msgSend__JJ_3I",
	"objc_1msgSend__JJ_3JJJ",
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2I",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2J",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2II",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJ",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2III",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJJ",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIIZ",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJJJZ",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2J",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSPoint_2",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSPoint_2",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRange_2I",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSRange_2J",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSRect_2",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2I",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSRect_2J",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSSize_2",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSSize_2",
#endif
	"objc_1msgSendSuper_1stret",
#ifndef JNI64
	"objc_1msgSend_1bool__II",
#else
	"objc_1msgSend_1bool__JJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1bool__III",
#else
	"objc_1msgSend_1bool__JJJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1bool__IIII",
#else
	"objc_1msgSend_1bool__JJJJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1bool__IIIII",
#else
	"objc_1msgSend_1bool__JJJJJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1bool__IIIIIII",
#else
	"objc_1msgSend_1bool__JJJJJJJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1bool__IIILorg_eclipse_swt_internal_cocoa_NSSize_2Z",
#else
	"objc_1msgSend_1bool__JJJLorg_eclipse_swt_internal_cocoa_NSSize_2Z",
#endif
#ifndef JNI64
	"objc_1msgSend_1bool__IILorg_eclipse_swt_internal_cocoa_NSPoint_2",
#else
	"objc_1msgSend_1bool__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2",
#endif
#ifndef JNI64
	"objc_1msgSend_1bool__IILorg_eclipse_swt_internal_cocoa_NSRange_2I",
#else
	"objc_1msgSend_1bool__JJLorg_eclipse_swt_internal_cocoa_NSRange_2J",
#endif
#ifndef JNI64
	"objc_1msgSend_1bool__IILorg_eclipse_swt_internal_cocoa_NSRect_2",
#else
	"objc_1msgSend_1bool__JJLorg_eclipse_swt_internal_cocoa_NSRect_2",
#endif
#ifndef JNI64
	"objc_1msgSend_1bool__IIS",
#else
	"objc_1msgSend_1bool__JJS",
#endif
#ifndef JNI64
	"objc_1msgSend_1fpret__II",
#else
	"objc_1msgSend_1fpret__JJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1fpret__III",
#else
	"objc_1msgSend_1fpret__JJJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1fpret__IIII",
#else
	"objc_1msgSend_1fpret__JJJJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2JJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2III",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJLorg_eclipse_swt_internal_cocoa_NSPoint_2",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJLorg_eclipse_swt_internal_cocoa_NSPoint_2J",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2II",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2III",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJLorg_eclipse_swt_internal_cocoa_NSRange_2J",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRect_2",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2III",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIII",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIIZ",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJJZ",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRange_2J",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2J",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2II",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJ",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSRect_2",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2I",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2J",
#endif
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2ZZI",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2ZZJ",
#endif
	"objc_1registerClassPair",
	"objc_1super_1sizeof",
	"object_1getClass",
	"object_1getClassName",
	"object_1getInstanceVariable",
	"object_1setClass",
	"object_1setInstanceVariable",
	"sel_1registerName",
	"selectedRange_1CALLBACK",
	"setFrameOrigin_1CALLBACK",
	"setFrameSize_1CALLBACK",
	"setFrame_1CALLBACK",
	"setMarkedText_1selectedRange_1CALLBACK",
	"setNeedsDisplayInRect_1CALLBACK",
	"shouldChangeTextInRange_1replacementString_1CALLBACK",
	"textView_1willChangeSelectionFromCharacterRange_1toCharacterRange_1CALLBACK",
	"titleRectForBounds_1CALLBACK",
	"view_1stringForToolTip_1point_1userData_1CALLBACK",
	"webView_1setFrame_1CALLBACK",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(OS_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return OS_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(OS_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, OS_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(OS_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return OS_nativeFunctionCallCount[index];
}

#endif
