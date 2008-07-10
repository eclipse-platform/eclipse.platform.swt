/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "os_structs.h"
#include "os_stats.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_cocoa_OS_##func

#ifndef NO_JNIGetObject
JNIEXPORT jobject JNICALL OS_NATIVE(JNIGetObject)
	(JNIEnv *env, jclass that, jint arg0)
{
	jobject rc = 0;
	OS_NATIVE_ENTER(env, that, JNIGetObject_FUNC);
	rc = (jobject)arg0;
	OS_NATIVE_EXIT(env, that, JNIGetObject_FUNC);
	return rc;
}
#endif

#ifndef NO_NSIntersectionRect
JNIEXPORT void JNICALL OS_NATIVE(NSIntersectionRect)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jobject arg2)
{
	NSRect _arg0, *lparg0=NULL;
	NSRect _arg1, *lparg1=NULL;
	NSRect _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, NSIntersectionRect_FUNC);
	if (arg0) if ((lparg0 = getNSRectFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getNSRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getNSRectFields(env, arg2, &_arg2)) == NULL) goto fail;
	*lparg0 = NSIntersectionRect(*lparg1, *lparg2);
fail:
	if (arg2 && lparg2) setNSRectFields(env, arg2, lparg2);
	if (arg1 && lparg1) setNSRectFields(env, arg1, lparg1);
	if (arg0 && lparg0) setNSRectFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, NSIntersectionRect_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2
static SEL cascadeTopLeftFromPoint;
static SEL convertScreenToBase;
static SEL convertBaseToScreen;
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3)
{
	NSPoint _arg0, *lparg0=NULL;
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	
	//rc = (jint)objc_msgSend_struct(lparg0, arg1, arg2, lparg3);
	
	if (cascadeTopLeftFromPoint == NULL) cascadeTopLeftFromPoint = sel_registerName("cascadeTopLeftFromPoint:");
	if ((SEL)arg2 == cascadeTopLeftFromPoint) {
		*lparg0 = [(NSWindow *)arg1 cascadeTopLeftFromPoint: *lparg3];
	} else {
		if (convertScreenToBase == 0) convertScreenToBase = sel_registerName("convertScreenToBase:");
		if ((SEL)arg2 == convertScreenToBase) {
			*lparg0 = [(id)arg1 convertScreenToBase: *lparg3];
		} else {
			if (convertBaseToScreen == 0) convertBaseToScreen = sel_registerName("convertBaseToScreen:");
			if ((SEL)arg2 == convertBaseToScreen) {
				*lparg0 = [(id)arg1 convertBaseToScreen: *lparg3];
			}
		}
	}
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC);
	return rc;
}
#endif


#ifndef NO_objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2III
static SEL locationForGlyphAtIndex;
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2III)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3)
{
	NSPoint _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2III_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	
	//rc = (jint)objc_msgSend_struct(lparg0, arg1, arg2);
	
	if (locationForGlyphAtIndex == 0) locationForGlyphAtIndex = sel_registerName("locationForGlyphAtIndex:");
	if ((SEL)arg2 == locationForGlyphAtIndex) {
		*lparg0 = [(NSLayoutManager *)arg1 locationForGlyphAtIndex: arg3];
	}
	
fail:
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2III_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II
static SEL mouseLocationOutsideOfEventStream;
static SEL locationInWindow;
static SEL mouseLocation;
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSPoint _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	
	//rc = (jint)objc_msgSend_struct(lparg0, arg1, arg2);
	
	if (mouseLocationOutsideOfEventStream == 0) mouseLocationOutsideOfEventStream = sel_registerName("mouseLocationOutsideOfEventStream");
	if ((SEL)arg2 == mouseLocationOutsideOfEventStream) {
		*lparg0 = [(NSWindow *)arg1 mouseLocationOutsideOfEventStream];
	} else {
		if (locationInWindow == 0) locationInWindow = sel_registerName("locationInWindow");
		if ((SEL)arg2 == locationInWindow) {
			*lparg0 = [(NSEvent *)arg1 locationInWindow];
		} else {
			if (mouseLocation == 0) mouseLocation = sel_registerName("mouseLocation");
			if ((SEL)arg2 == mouseLocation) {
				*lparg0 = [(id)arg1 mouseLocation];
			}
		}
	}
	
fail:
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I
static SEL glyphRangeForCharacterRange;
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jint arg4)
{
	NSRange _arg0, *lparg0=NULL;
	NSRange _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	//objc_msgSend_struct(lparg0, arg1, arg2, lparg3, arg4);
	if (glyphRangeForCharacterRange == 0) glyphRangeForCharacterRange = sel_registerName("glyphRangeForCharacterRange:actualCharacterRange::");
	if ((SEL)arg2 == glyphRangeForCharacterRange) {
		*lparg0 = [(id)arg1 glyphRangeForCharacterRange: *lparg3 actualCharacterRange: (NSRange *)arg4];
	}
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2ZZI
static SEL frameSizeForContentSize;
JNIEXPORT void JNICALL OS_NATIVE(objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2ZZI)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jboolean arg4, jboolean arg5, jint arg6)
{
	NSSize _arg0, *lparg0=NULL;
	NSSize _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2ZZI_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSSizeFields(env, arg3, &_arg3)) == NULL) goto fail;
	//objc_msgSend_struct(lparg0, arg1, arg2, lparg3, arg4, arg5, arg6);
	if (frameSizeForContentSize == 0) frameSizeForContentSize = sel_registerName("frameSizeForContentSize:hasHorizontalScroller:hasVerticalScroller:borderType:");
	if ((SEL)arg2 == frameSizeForContentSize) {
		*lparg0 = [(id)arg1 frameSizeForContentSize: *lparg3 hasHorizontalScroller: arg4 hasVerticalScroller: arg5 borderType: arg6];
	}
fail:
	if (arg3 && lparg3) setNSSizeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2ZZI_FUNC);
}
#endif

#ifndef NO_objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2II
static SEL selectedRange;
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSRange _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	
	//rc = (jint)objc_msgSend_struct(lparg0, arg1, arg2);
	if (selectedRange == 0) selectedRange = sel_registerName("selectedRange");
	if ((SEL)arg2 == selectedRange) {
		*lparg0 = [(NSText *)arg1 selectedRange];
	}
		
fail:
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC);
	return rc;
}
#endif


#ifndef NO_objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2III
static SEL doubleClickAtIndex;
static SEL glyphRangeForTextContainer;
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2III)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jint arg3)
{
	NSRange _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2III_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	
	//rc = (jint)objc_msgSend_struct(lparg0, arg1, arg2, arg3);
	if (doubleClickAtIndex == 0) doubleClickAtIndex = sel_registerName("doubleClickAtIndex:");
	if ((SEL)arg2 == doubleClickAtIndex) {
		*lparg0 = [(NSAttributedString *)arg1 doubleClickAtIndex: arg3];
	} else {
		if (glyphRangeForTextContainer == 0) glyphRangeForTextContainer = sel_registerName("glyphRangeForTextContainer:");
		if ((SEL)arg2 == glyphRangeForTextContainer) {
			*lparg0 = [(NSLayoutManager *)arg1 glyphRangeForTextContainer: (NSTextContainer *)arg3];
		}
	}
		
fail:
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2III_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSSize_2II
static SEL size;
static SEL minimumSize;
static SEL contentSize;
static SEL containerSize;
static SEL cellSize;
static SEL sizeValue;
static SEL contentViewMargins;
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSSize_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	NSSize _arg0, *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
	if (arg0) if ((lparg0 = getNSSizeFields(env, arg0, &_arg0)) == NULL) goto fail;
	
	//rc = (jint)objc_msgSend_struct(lparg0, arg1, arg2);
	if (size == 0) size = sel_registerName("size");
	if ((SEL)arg2 == size) {
		*lparg0 = [(NSAttributedString *)arg1 size];
	} else {
		if (minimumSize == 0) minimumSize = sel_registerName("minimumSize");
		if ((SEL)arg2 == minimumSize) {
			*lparg0 = [(NSTabView *)arg1 minimumSize];
		} else {
			if (contentSize == 0) contentSize = sel_registerName("contentSize");
			if ((SEL)arg2 == contentSize) {
				*lparg0 = [(NSScrollView *)arg1 contentSize];
			} else {
				if (cellSize == 0) cellSize = sel_registerName("cellSize");
				if ((SEL)arg2 == contentSize) {
					*lparg0 = [(NSCell *)arg1 cellSize];
				} else {
					if (containerSize == 0) containerSize = sel_registerName("containerSize");
					if ((SEL)arg2 == containerSize) {
						*lparg0 = [(id)arg1 containerSize];
					} else {
						if (sizeValue == 0) sizeValue = sel_registerName("sizeValue");
						if ((SEL)arg2 == sizeValue) {
							*lparg0 = [(id)arg1 sizeValue];
						} else {
							if (contentViewMargins == 0) contentViewMargins = sel_registerName("contentViewMargins");
							if ((SEL)arg2 == contentViewMargins) {
								*lparg0 = [(id)arg1 contentViewMargins];
							}
						}
					}
				}
			}
		}
	}
	
fail:
	if (arg0 && lparg0) setNSSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I
static SEL convertPointfromView;
static SEL convertPoint_toView_;
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jint arg4)
{
	NSPoint _arg0, *lparg0=NULL;
	NSPoint _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	if (arg0) if ((lparg0 = getNSPointFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSPointFields(env, arg3, &_arg3)) == NULL) goto fail;
	
	//rc = (jint)objc_msgSend_struct(lparg0, arg1, arg2, lparg3, arg4);
	if (convertPointfromView == 0) convertPointfromView = sel_registerName("convertPoint:fromView:");
	if ((SEL)arg2 == convertPointfromView) {
		*lparg0 = [(NSView *)arg1 convertPoint: *lparg3 fromView: (NSView *)arg4];
	} else {
		if (convertPoint_toView_ == 0) convertPoint_toView_ = sel_registerName("convertPoint:toView:");
		if ((SEL)arg2 == convertPoint_toView_) {
			*lparg0 = [(id)arg1 convertPoint: *lparg3 toView: (id)arg4];
		}
	}
	
fail:
	if (arg3 && lparg3) setNSPointFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSPointFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC);
	return rc;
}
#endif

#ifndef NO_objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2
static SEL lineRangeForRange;
JNIEXPORT jint JNICALL OS_NATIVE(objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3)
{
	NSRange _arg0, *lparg0=NULL;
	NSRange _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	if (arg0) if ((lparg0 = getNSRangeFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getNSRangeFields(env, arg3, &_arg3)) == NULL) goto fail;
	//rc = (jint)objc_msgSend_struct(lparg0, arg1, arg2, lparg3);
	if (lineRangeForRange == 0) lineRangeForRange = sel_registerName("lineRangeForRange:");
	if ((SEL)arg2 == lineRangeForRange) {
		*lparg0 = [(NSString *)arg1 lineRangeForRange: *lparg3];
	}
fail:
	if (arg3 && lparg3) setNSRangeFields(env, arg3, lparg3);
	if (arg0 && lparg0) setNSRangeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, objc_1msgSend_1struct__Lorg_eclipse_swt_internal_cocoa_NSRange_2IILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC);
	return rc;
}
#endif

#ifndef NO_drawRect_1CALLBACK
static SWT_PTR drawRect_1CALLBACK;
static void drawRect(id obj, SEL sel, NSRect rect)
{
	return ((void (*)(id, SEL, NSRect*))drawRect_1CALLBACK)(obj, sel, &rect);
}
JNIEXPORT SWT_PTR JNICALL OS_NATIVE(drawRect_1CALLBACK)
	(JNIEnv *env, jclass that, SWT_PTR func)
{
	drawRect_1CALLBACK = func;
	return (SWT_PTR)drawRect;
}
#endif

#ifndef NO_setFrame_1CALLBACK
static SWT_PTR setFrame_1CALLBACK;
static void setFrame(id obj, SEL sel, NSRect rect)
{
	return ((void (*)(id, SEL, NSRect*))setFrame_1CALLBACK)(obj, sel, &rect);
}
JNIEXPORT SWT_PTR JNICALL OS_NATIVE(setFrame_1CALLBACK)
	(JNIEnv *env, jclass that, SWT_PTR func)
{
	setFrame_1CALLBACK = func;
	return (SWT_PTR)setFrame;
}
#endif

#ifndef NO_setFrameOrigin_1CALLBACK
static SWT_PTR setFrameOrigin_1CALLBACK;
static void setFrameOrigin(id obj, SEL sel, NSPoint point)
{
	return ((void (*)(id, SEL, NSPoint*))setFrameOrigin_1CALLBACK)(obj, sel, &point);
}
JNIEXPORT SWT_PTR JNICALL OS_NATIVE(setFrameOrigin_1CALLBACK)
	(JNIEnv *env, jclass that, SWT_PTR func)
{
	setFrameOrigin_1CALLBACK = func;
	return (SWT_PTR)setFrameOrigin;
}
#endif

#ifndef NO_setFrameSize_1CALLBACK
static SWT_PTR setFrameSize_1CALLBACK;
static void setFrameSize(id obj, SEL sel, NSSize size)
{
	return ((void (*)(id, SEL, NSSize*))setFrameSize_1CALLBACK)(obj, sel, &size);
}
JNIEXPORT SWT_PTR JNICALL OS_NATIVE(setFrameSize_1CALLBACK)
	(JNIEnv *env, jclass that, SWT_PTR func)
{
	setFrameSize_1CALLBACK = func;
	return (SWT_PTR)setFrameSize;
}
#endif

#ifndef NO_hitTest_1CALLBACK
static SWT_PTR hitTest_1CALLBACK;
static void hitTest(id obj, SEL sel, NSPoint point)
{
	return ((void (*)(id, SEL, NSPoint*))hitTest_1CALLBACK)(obj, sel, &point);
}
JNIEXPORT SWT_PTR JNICALL OS_NATIVE(hitTest_1CALLBACK)
	(JNIEnv *env, jclass that, SWT_PTR func)
{
	hitTest_1CALLBACK = func;
	return (SWT_PTR)hitTest;
}
#endif

#ifndef NO_webView_1setFrame_1CALLBACK
static SWT_PTR webView_1setFrame_1CALLBACK;
static void webView_1setFrame(id obj, SEL sel, WebView *sender, NSRect rect)
{
	return ((void (*)(id, SEL, WebView*, NSRect*))webView_1setFrame_1CALLBACK)(obj, sel, sender, &rect);
}
JNIEXPORT SWT_PTR JNICALL OS_NATIVE(webView_1setFrame_1CALLBACK)
	(JNIEnv *env, jclass that, SWT_PTR func)
{
	webView_1setFrame_1CALLBACK = func;
	return (SWT_PTR)webView_1setFrame;
}
#endif

#ifndef NO_setMarkedText_1selectedRange_1CALLBACK
static SWT_PTR setMarkedText_1selectedRange_1CALLBACK;
static void setMarkedText_1selectedRange(id obj, SEL sel, id *arg0, NSRange arg1)
{
	((void (*)(id, SEL, id*, NSRange*))setMarkedText_1selectedRange_1CALLBACK)(obj, sel, arg0, &arg1);
}
JNIEXPORT SWT_PTR JNICALL OS_NATIVE(setMarkedText_1selectedRange_1CALLBACK)
	(JNIEnv *env, jclass that, SWT_PTR func)
{
	setMarkedText_1selectedRange_1CALLBACK = func;
	return (SWT_PTR)setMarkedText_1selectedRange;
}
#endif

#ifndef NO_selectedRange_1CALLBACK
static SWT_PTR selectedRange_1CALLBACK;
static NSRange selectedRangeProc(id obj, SEL sel)
{
	NSRange* ptr = ((NSRange* (*)(id, SEL))selectedRange_1CALLBACK)(obj, sel);
	NSRange range = *ptr;
	free(ptr);
	return range;
}
JNIEXPORT SWT_PTR JNICALL OS_NATIVE(selectedRange_1CALLBACK)
	(JNIEnv *env, jclass that, SWT_PTR func)
{
	selectedRange_1CALLBACK = func;
	return (SWT_PTR)selectedRangeProc;
}
#endif

#ifndef NO_markedRange_1CALLBACK
static SWT_PTR markedRange_1CALLBACK;
static NSRange markedRangeProc(id obj, SEL sel)
{
	NSRange* ptr = ((NSRange* (*)(id, SEL))markedRange_1CALLBACK)(obj, sel);
	NSRange range = *ptr;
	free(ptr);
	return range;
}
JNIEXPORT SWT_PTR JNICALL OS_NATIVE(markedRange_1CALLBACK)
	(JNIEnv *env, jclass that, SWT_PTR func)
{
	markedRange_1CALLBACK = func;
	return (SWT_PTR)markedRangeProc;
}
#endif

#ifndef NO_characterIndexForPoint_1CALLBACK
static SWT_PTR characterIndexForPoint_1CALLBACK;
static int characterIndexForPoint(id obj, SEL sel, NSPoint point)
{
	return ((int (*)(id, SEL, NSPoint*))characterIndexForPoint_1CALLBACK)(obj, sel, &point);
}
JNIEXPORT SWT_PTR JNICALL OS_NATIVE(characterIndexForPoint_1CALLBACK)
	(JNIEnv *env, jclass that, SWT_PTR func)
{
	characterIndexForPoint_1CALLBACK = func;
	return (SWT_PTR)characterIndexForPoint;
}
#endif

#ifndef NO_firstRectForCharacterRange_1CALLBACK
static SWT_PTR firstRectForCharacterRange_1CALLBACK;
static NSRect firstRectForCharacterRangeProc(id obj, SEL sel, NSRange arg0)
{
	NSRect* ptr = ((NSRect* (*)(id, SEL, NSRange*))firstRectForCharacterRange_1CALLBACK)(obj, sel, &arg0);
	NSRect result = *ptr;
	free(ptr);
	return result;
}
JNIEXPORT SWT_PTR JNICALL OS_NATIVE(firstRectForCharacterRange_1CALLBACK)
	(JNIEnv *env, jclass that, SWT_PTR func)
{
	firstRectForCharacterRange_1CALLBACK = func;
	return (SWT_PTR)firstRectForCharacterRangeProc;
}
#endif

#ifndef NO_attributedSubstringFromRange_1CALLBACK
static SWT_PTR attributedSubstringFromRange_1CALLBACK;
static id attributedSubstringFromRangeProc(id obj, SEL sel, NSRange arg0)
{
	return ((id (*)(id, SEL, NSRange*))attributedSubstringFromRange_1CALLBACK)(obj, sel, &arg0);
}
JNIEXPORT SWT_PTR JNICALL OS_NATIVE(attributedSubstringFromRange_1CALLBACK)
	(JNIEnv *env, jclass that, SWT_PTR func)
{
	attributedSubstringFromRange_1CALLBACK = func;
	return (SWT_PTR)attributedSubstringFromRangeProc;
}
#endif
