/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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

int OS_nativeFunctionCount = 589;
int OS_nativeFunctionCallCount[589];
char * OS_nativeFunctionNames[] = {
	"ATSFontActivateFromFileReference",
	"AcquireRootMenu",
	"CALLBACK_1NSTextAttachmentCell_1cellSize",
	"CALLBACK_1accessibilityHitTest_1",
	"CALLBACK_1attributedSubstringFromRange_1",
	"CALLBACK_1canDragRowsWithIndexes_1atPoint_1",
	"CALLBACK_1cellBaselineOffset",
	"CALLBACK_1cellSize",
	"CALLBACK_1characterIndexForPoint_1",
	"CALLBACK_1dragSelectionWithEvent_1offset_1slideBack_1",
	"CALLBACK_1draggedImage_1beganAt_1",
	"CALLBACK_1draggedImage_1endedAt_1operation_1",
	"CALLBACK_1drawBackgroundInClipRect_1",
	"CALLBACK_1drawImage_1withFrame_1inView_1",
	"CALLBACK_1drawInteriorWithFrame_1inView_1",
	"CALLBACK_1drawLabel_1inRect_1",
	"CALLBACK_1drawRect_1",
	"CALLBACK_1drawViewBackgroundInRect_1",
	"CALLBACK_1drawWithExpansionFrame_1inView_1",
	"CALLBACK_1expansionFrameWithFrame_1inView_1",
	"CALLBACK_1firstRectForCharacterRange_1",
	"CALLBACK_1highlightSelectionInClipRect_1",
	"CALLBACK_1hitTestForEvent_1inRect_1ofView_1",
	"CALLBACK_1hitTest_1",
	"CALLBACK_1imageRectForBounds_1",
	"CALLBACK_1markedRange",
	"CALLBACK_1scrollClipView_1toPoint_1",
	"CALLBACK_1selectedRange",
	"CALLBACK_1setFrameOrigin_1",
	"CALLBACK_1setFrameSize_1",
	"CALLBACK_1setFrame_1",
	"CALLBACK_1setMarkedText_1selectedRange_1",
	"CALLBACK_1setNeedsDisplayInRect_1",
	"CALLBACK_1shouldChangeTextInRange_1replacementString_1",
	"CALLBACK_1sizeOfLabel_1",
	"CALLBACK_1textView_1willChangeSelectionFromCharacterRange_1toCharacterRange_1",
	"CALLBACK_1titleRectForBounds_1",
	"CALLBACK_1view_1stringForToolTip_1point_1userData_1",
	"CALLBACK_1webView_1setFrame_1",
	"CFAttributedStringCreate",
	"CFDataGetBytePtr",
	"CFDataGetLength",
	"CFDictionaryAddValue",
	"CFDictionaryCreateMutable",
	"CFRange_1sizeof",
	"CFRelease",
	"CFRunLoopAddObserver",
	"CFRunLoopGetCurrent",
	"CFRunLoopObserverCreate",
	"CFRunLoopObserverInvalidate",
	"CFRunLoopRunInMode",
	"CFRunLoopStop",
	"CFStringCreateWithCharacters",
	"CFURLCreateFromFSRef",
	"CFURLCreateStringByAddingPercentEscapes",
	"CGAffineTransform_1sizeof",
	"CGBitmapContextCreate",
	"CGBitmapContextCreateImage",
	"CGBitmapContextGetData",
	"CGColorCreate",
	"CGColorRelease",
	"CGColorSpaceCreateDeviceRGB",
	"CGColorSpaceRelease",
	"CGContextAddPath",
	"CGContextCopyPath",
	"CGContextCopyWindowContentsToRect",
	"CGContextDrawImage",
	"CGContextFillRect",
	"CGContextRelease",
	"CGContextReplacePathWithStrokedPath",
	"CGContextRestoreGState",
	"CGContextSaveGState",
	"CGContextScaleCTM",
	"CGContextSetBlendMode",
	"CGContextSetFillColor",
	"CGContextSetFillColorSpace",
	"CGContextSetLineCap",
	"CGContextSetLineDash",
	"CGContextSetLineJoin",
	"CGContextSetLineWidth",
	"CGContextSetMiterLimit",
	"CGContextSetShouldAntialias",
	"CGContextSetTextDrawingMode",
	"CGContextSetTextMatrix",
	"CGContextSetTextPosition",
	"CGContextStrokePath",
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
	"CGPathAddRect",
	"CGPathApply",
	"CGPathCloseSubpath",
	"CGPathCreateCopy",
	"CGPathCreateMutable",
	"CGPathElement_1sizeof",
	"CGPathMoveToPoint",
	"CGPathRelease",
	"CGPoint_1sizeof",
	"CGPostKeyboardEvent",
	"CGPostMouseEvent",
	"CGPostScrollWheelEvent",
	"CGRect_1sizeof",
	"CGSetLocalEventsFilterDuringSuppressionState",
	"CGSetLocalEventsSuppressionInterval",
	"CGSize_1sizeof",
	"CGWarpMouseCursorPosition",
	"CPSSetProcessName",
	"CTFontGetAscent",
	"CTFontGetDescent",
	"CTFontGetLeading",
	"CTLineCreateWithAttributedString",
	"CTLineDraw",
	"CTLineGetTypographicBounds",
	"CTParagraphStyleCreate",
	"CTParagraphStyleSetting_1sizeof",
	"CTTypesetterCreateLine",
	"CTTypesetterCreateWithAttributedString",
	"CTTypesetterSuggestLineBreak",
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
	"GetSystemUIMode",
	"GetThemeMetric",
	"HIThemeDrawFocusRect",
	"JNIGetObject",
	"JSEvaluateScript",
	"JSStringCreateWithUTF8CString",
	"JSStringRelease",
	"LMGetKbdType",
	"LSGetApplicationForInfo",
	"LineTo",
	"MoveTo",
	"NSAccessibilityActionDescription",
	"NSAccessibilityAttributedStringForRangeParameterizedAttribute",
	"NSAccessibilityBackgroundColorTextAttribute",
	"NSAccessibilityBoundsForRangeParameterizedAttribute",
	"NSAccessibilityButtonRole",
	"NSAccessibilityCellForColumnAndRowParameterizedAttribute",
	"NSAccessibilityCellRole",
	"NSAccessibilityCheckBoxRole",
	"NSAccessibilityChildrenAttribute",
	"NSAccessibilityColorWellRole",
	"NSAccessibilityColumnIndexRangeAttribute",
	"NSAccessibilityColumnRole",
	"NSAccessibilityColumnsAttribute",
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
	"NSAccessibilityFocusedWindowChangedNotification",
	"NSAccessibilityFontFamilyKey",
	"NSAccessibilityFontNameKey",
	"NSAccessibilityFontSizeKey",
	"NSAccessibilityFontTextAttribute",
	"NSAccessibilityForegroundColorTextAttribute",
	"NSAccessibilityGridRole",
	"NSAccessibilityGroupRole",
	"NSAccessibilityHeaderAttribute",
	"NSAccessibilityHelpAttribute",
	"NSAccessibilityHelpTagRole",
	"NSAccessibilityHorizontalOrientationValue",
	"NSAccessibilityHorizontalScrollBarAttribute",
	"NSAccessibilityImageRole",
	"NSAccessibilityIncrementorRole",
	"NSAccessibilityIndexAttribute",
	"NSAccessibilityInsertionPointLineNumberAttribute",
	"NSAccessibilityLabelValueAttribute",
	"NSAccessibilityLineForIndexParameterizedAttribute",
	"NSAccessibilityLinkRole",
	"NSAccessibilityLinkTextAttribute",
	"NSAccessibilityLinkedUIElementsAttribute",
	"NSAccessibilityListRole",
	"NSAccessibilityMaxValueAttribute",
	"NSAccessibilityMenuBarRole",
	"NSAccessibilityMenuButtonRole",
	"NSAccessibilityMenuItemRole",
	"NSAccessibilityMenuRole",
	"NSAccessibilityMinValueAttribute",
	"NSAccessibilityMisspelledTextAttribute",
	"NSAccessibilityMovedNotification",
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
	"NSAccessibilityResizedNotification",
	"NSAccessibilityRoleAttribute",
	"NSAccessibilityRoleDescription",
	"NSAccessibilityRoleDescriptionAttribute",
	"NSAccessibilityRoleDescriptionForUIElement",
	"NSAccessibilityRowCountChangedNotification",
	"NSAccessibilityRowIndexRangeAttribute",
	"NSAccessibilityRowRole",
	"NSAccessibilityRowsAttribute",
	"NSAccessibilityScrollAreaRole",
	"NSAccessibilityScrollBarRole",
	"NSAccessibilitySelectedAttribute",
	"NSAccessibilitySelectedChildrenAttribute",
	"NSAccessibilitySelectedChildrenChangedNotification",
	"NSAccessibilitySelectedColumnsAttribute",
	"NSAccessibilitySelectedRowsAttribute",
	"NSAccessibilitySelectedRowsChangedNotification",
	"NSAccessibilitySelectedTextAttribute",
	"NSAccessibilitySelectedTextChangedNotification",
	"NSAccessibilitySelectedTextRangeAttribute",
	"NSAccessibilitySelectedTextRangesAttribute",
	"NSAccessibilityServesAsTitleForUIElementsAttribute",
	"NSAccessibilityShowMenuAction",
	"NSAccessibilitySizeAttribute",
	"NSAccessibilitySliderRole",
	"NSAccessibilitySortButtonRole",
	"NSAccessibilitySplitterRole",
	"NSAccessibilityStandardWindowSubrole",
	"NSAccessibilityStaticTextRole",
	"NSAccessibilityStrikethroughColorTextAttribute",
	"NSAccessibilityStrikethroughTextAttribute",
	"NSAccessibilityStringForRangeParameterizedAttribute",
	"NSAccessibilityStyleRangeForIndexParameterizedAttribute",
	"NSAccessibilitySubroleAttribute",
	"NSAccessibilitySuperscriptTextAttribute",
	"NSAccessibilitySystemDialogSubrole",
	"NSAccessibilityTabGroupRole",
	"NSAccessibilityTableRole",
	"NSAccessibilityTableRowSubrole",
	"NSAccessibilityTabsAttribute",
	"NSAccessibilityTextAreaRole",
	"NSAccessibilityTextFieldRole",
	"NSAccessibilityTextLinkSubrole",
	"NSAccessibilityTitleAttribute",
	"NSAccessibilityTitleChangedNotification",
	"NSAccessibilityTitleUIElementAttribute",
	"NSAccessibilityToolbarRole",
	"NSAccessibilityTopLevelUIElementAttribute",
	"NSAccessibilityURLAttribute",
	"NSAccessibilityUnderlineColorTextAttribute",
	"NSAccessibilityUnderlineTextAttribute",
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
	"NSAccessibilityVisibleColumnsAttribute",
	"NSAccessibilityVisibleNameKey",
	"NSAccessibilityVisibleRowsAttribute",
	"NSAccessibilityWindowAttribute",
	"NSAccessibilityWindowRole",
	"NSAffineTransformStruct_1sizeof",
	"NSApplicationDidChangeScreenParametersNotification",
	"NSAttachmentAttributeName",
	"NSBackgroundColorAttributeName",
	"NSBaselineOffsetAttributeName",
	"NSBeep",
	"NSBitsPerPixelFromDepth",
	"NSCalibratedRGBColorSpace",
	"NSCopyBits",
	"NSCountWindows",
	"NSCursorAttributeName",
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
	"NSModalPanelRunLoopMode",
	"NSNumberOfColorComponents",
	"NSObliquenessAttributeName",
	"NSOutlineViewColumnDidMoveNotification",
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
	"NSSpellingStateAttributeName",
	"NSStrikethroughColorAttributeName",
	"NSStrikethroughStyleAttributeName",
	"NSStringPboardType",
	"NSStrokeWidthAttributeName",
	"NSSystemColorsDidChangeNotification",
	"NSTIFFPboardType",
	"NSTableViewColumnDidMoveNotification",
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
	"NSViewGlobalFrameDidChangeNotification",
	"NSWindowList",
	"NewGlobalRef",
	"NewRgn",
	"OffsetRgn",
	"OpenRgn",
	"PtInRgn",
	"QDRegionToRects",
	"RectInRgn",
	"RectRgn",
	"SecPolicySearchCopyNext",
	"SecPolicySearchCreate",
	"SecTrustCreateWithCertificates",
	"SectRgn",
	"SetFrontProcess",
	"SetRect",
	"SetSystemUIMode",
	"SetThemeCursor",
	"TISCopyCurrentKeyboardInputSource",
	"TISGetInputSourceProperty",
	"TransformProcessType",
	"UCKeyTranslate",
	"UTTypeEqual",
	"UnionRgn",
	"_1_1BIG_1ENDIAN_1_1",
	"call",
	"class_1addIvar",
	"class_1addMethod",
	"class_1addProtocol",
	"class_1createInstance",
	"class_1getClassMethod",
	"class_1getInstanceMethod",
	"class_1getMethodImplementation",
	"class_1getName",
	"class_1getSuperclass",
	"getpid",
	"instrumentObjcMessageSends",
	"isFlipped_1CALLBACK",
	"kCFAllocatorDefault",
	"kCFRunLoopCommonModes",
	"kCFTypeDictionaryKeyCallBacks",
	"kCFTypeDictionaryValueCallBacks",
	"kCTFontAttributeName",
	"kCTForegroundColorAttributeName",
	"kCTParagraphStyleAttributeName",
	"kTISPropertyUnicodeKeyLayoutData",
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_cocoa_CFRange_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_cocoa_CFRange_2J",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_cocoa_CGAffineTransform_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_cocoa_CGAffineTransform_2J",
#endif
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
	"memmove__ILorg_eclipse_swt_internal_cocoa_CTParagraphStyleSetting_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_cocoa_CTParagraphStyleSetting_2J",
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
	"memmove__Lorg_eclipse_swt_internal_cocoa_CFRange_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_cocoa_CFRange_2JJ",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_cocoa_CGAffineTransform_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_cocoa_CGAffineTransform_2JJ",
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
	"memmove__Lorg_eclipse_swt_internal_cocoa_CTParagraphStyleSetting_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_cocoa_CTParagraphStyleSetting_2JJ",
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
	"objc_1msgSend__IIIIDI",
#else
	"objc_1msgSend__JJJJDJ",
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
	"objc_1msgSend__IIIIIIII",
#else
	"objc_1msgSend__JJJJJJJJ",
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
	"objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2I",
#else
	"objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2J",
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
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2IIII_3B",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2JJJJ_3B",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2",
#endif
#ifndef JNI64
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2I_3I",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRange_2J_3J",
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
	"objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2ZZ",
#else
	"objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2ZZ",
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
	"objc_1msgSend__IIZLorg_eclipse_swt_internal_cocoa_NSRect_2",
#else
	"objc_1msgSend__JJZLorg_eclipse_swt_internal_cocoa_NSRect_2",
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
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIII",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJJJJ",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIIZ",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJJJZ",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJLorg_eclipse_swt_internal_cocoa_NSPoint_2",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJLorg_eclipse_swt_internal_cocoa_NSPoint_2J",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2J",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIZ",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJZ",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSPoint_2",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSPoint_2",
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
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IZ",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JZ",
#endif
#ifndef JNI64
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IZLorg_eclipse_swt_internal_cocoa_NSRect_2",
#else
	"objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JZLorg_eclipse_swt_internal_cocoa_NSRect_2",
#endif
	"objc_1msgSendSuper_1bool",
#ifndef JNI64
	"objc_1msgSendSuper_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2",
#else
	"objc_1msgSendSuper_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSRect_2",
#endif
#ifndef JNI64
	"objc_1msgSendSuper_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2I",
#else
	"objc_1msgSendSuper_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSRect_2J",
#endif
#ifndef JNI64
	"objc_1msgSendSuper_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2Lorg_eclipse_swt_internal_cocoa_objc_1super_2I",
#else
	"objc_1msgSendSuper_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2Lorg_eclipse_swt_internal_cocoa_objc_1super_2J",
#endif
#ifndef JNI64
	"objc_1msgSendSuper_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2Lorg_eclipse_swt_internal_cocoa_objc_1super_2IZ",
#else
	"objc_1msgSendSuper_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2Lorg_eclipse_swt_internal_cocoa_objc_1super_2JZ",
#endif
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
	"objc_1msgSend_1bool__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2",
#else
	"objc_1msgSend_1bool__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2",
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
#ifndef JNI64
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IIZ",
#else
	"objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJZ",
#endif
	"objc_1registerClassPair",
	"objc_1super_1sizeof",
	"object_1getClass",
	"object_1getClassName",
	"object_1getInstanceVariable",
	"object_1setClass",
	"object_1setInstanceVariable",
	"sel_1getName",
	"sel_1registerName",
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
