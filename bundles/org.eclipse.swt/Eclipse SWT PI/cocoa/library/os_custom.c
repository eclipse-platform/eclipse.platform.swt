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
	(JNIEnv *env, jclass that, jintLong arg0)
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

#ifndef NO_CGDisplayBounds
JNIEXPORT void JNICALL OS_NATIVE(CGDisplayBounds)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	CGRect _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, CGDisplayBounds_FUNC);
	if (arg1) if ((lparg1 = getCGRectFields(env, arg1, &_arg1)) == NULL) goto fail;
	*lparg1 = CGDisplayBounds((CGDirectDisplayID)arg0);
fail:
	if (arg1 && lparg1) setCGRectFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, CGDisplayBounds_FUNC);
}
#endif

#ifndef NO__1_1BIG_1ENDIAN_1_1
JNIEXPORT jboolean JNICALL OS_NATIVE(_1_1BIG_1ENDIAN_1_1)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	OS_NATIVE_ENTER(env, that, _1_1BIG_1ENDIAN_1_1_FUNC)
#ifdef __BIG_ENDIAN__
	rc = (jboolean)TRUE;
#else
	rc = (jboolean)FALSE;
#endif
	OS_NATIVE_EXIT(env, that, _1_1BIG_1ENDIAN_1_1_FUNC)
	return rc;
}
#endif

#ifndef NO_drawRect_1CALLBACK
static jintLong drawRect_1CALLBACK;
static void drawRect(id obj, SEL sel, NSRect rect)
{
	return ((void (*)(id, SEL, NSRect*))drawRect_1CALLBACK)(obj, sel, &rect);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(drawRect_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	drawRect_1CALLBACK = func;
	return (jintLong)drawRect;
}
#endif

#ifndef NO_draggedImage_1beganAt_1CALLBACK
static jintLong draggedImage_1beganAt_1CALLBACK;
static BOOL draggedImage_1beganAt_1(id obj, SEL sel, NSImage *image, NSPoint point)
{
	return ((BOOL (*)(id, SEL, NSImage*, NSPoint*))draggedImage_1beganAt_1CALLBACK)(obj, sel, image, &point);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(draggedImage_1beganAt_1CALLBACK)
(JNIEnv *env, jclass that, jintLong func)
{
	draggedImage_1beganAt_1CALLBACK = func;
	return (jintLong)draggedImage_1beganAt_1;
}
#endif

#ifndef NO_draggedImage_1endedAt_1operation_1CALLBACK
static jintLong draggedImage_1endedAt_1operation_1CALLBACK;
static void draggedImage_1endedAt_1operation(id obj, SEL sel, NSImage *image, NSPoint point, NSDragOperation op)
{
	return ((void (*)(id, SEL, NSImage *, NSPoint *, NSDragOperation))draggedImage_1endedAt_1operation_1CALLBACK)(obj, sel, image, &point, op);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(draggedImage_1endedAt_1operation_1CALLBACK)
(JNIEnv *env, jclass that, jintLong func)
{
	draggedImage_1endedAt_1operation_1CALLBACK = func;
	return (jintLong)draggedImage_1endedAt_1operation;
}
#endif

#ifndef NO_drawInteriorWithFrame_1inView_1CALLBACK
static jintLong drawInteriorWithFrame_1inView_1CALLBACK;
static void drawInteriorWithFrame_1inView(id obj, SEL sel, NSRect rect, NSView* view)
{
	return ((void (*)(id, SEL, NSRect*, NSView*))drawInteriorWithFrame_1inView_1CALLBACK)(obj, sel, &rect, view);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(drawInteriorWithFrame_1inView_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	drawInteriorWithFrame_1inView_1CALLBACK = func;
	return (jintLong)drawInteriorWithFrame_1inView;
}
#endif

#ifndef NO_drawImage_1withFrame_1inView_1CALLBACK
static jintLong drawImage_1withFrame_1inView_1CALLBACK;
static void drawImage_1withFrame_1inView(id obj, SEL sel, NSImage* image, NSRect frame, NSView* view)
{
	return ((void (*)(id, SEL, NSImage*, NSRect*, NSView*))drawImage_1withFrame_1inView_1CALLBACK)(obj, sel, image, &frame, view);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(drawImage_1withFrame_1inView_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	drawImage_1withFrame_1inView_1CALLBACK = func;
	return (jintLong)drawImage_1withFrame_1inView;
}
#endif

#ifndef NO_class_1getName
JNIEXPORT jstring JNICALL OS_NATIVE(class_1getName)
(JNIEnv *env, jclass that, jintLong arg0)
{
	jstring rc = 0;
	OS_NATIVE_ENTER(env, that, class_1getName_FUNC);
	const char *className = class_getName((Class)arg0);
	if (className != NULL) rc = (*env)->NewStringUTF(env, className);
	OS_NATIVE_EXIT(env, that, class_1getName_FUNC);
	return rc;
}
#endif


#ifndef NO_setFrame_1CALLBACK
static jintLong setFrame_1CALLBACK;
static void setFrame(id obj, SEL sel, NSRect rect)
{
	return ((void (*)(id, SEL, NSRect*))setFrame_1CALLBACK)(obj, sel, &rect);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(setFrame_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	setFrame_1CALLBACK = func;
	return (jintLong)setFrame;
}
#endif

#ifndef NO_setFrameOrigin_1CALLBACK
static jintLong setFrameOrigin_1CALLBACK;
static void setFrameOrigin(id obj, SEL sel, NSPoint point)
{
	return ((void (*)(id, SEL, NSPoint*))setFrameOrigin_1CALLBACK)(obj, sel, &point);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(setFrameOrigin_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	setFrameOrigin_1CALLBACK = func;
	return (jintLong)setFrameOrigin;
}
#endif

#ifndef NO_setFrameSize_1CALLBACK
static jintLong setFrameSize_1CALLBACK;
static void setFrameSize(id obj, SEL sel, NSSize size)
{
	return ((void (*)(id, SEL, NSSize*))setFrameSize_1CALLBACK)(obj, sel, &size);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(setFrameSize_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	setFrameSize_1CALLBACK = func;
	return (jintLong)setFrameSize;
}
#endif

#ifndef NO_hitTest_1CALLBACK
static jintLong hitTest_1CALLBACK;
static void hitTest(id obj, SEL sel, NSPoint point)
{
	return ((void (*)(id, SEL, NSPoint*))hitTest_1CALLBACK)(obj, sel, &point);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(hitTest_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	hitTest_1CALLBACK = func;
	return (jintLong)hitTest;
}
#endif

#ifndef NO_highlightSelectionInClipRect_1CALLBACK
static jintLong highlightSelectionInClipRect_1CALLBACK;
static void highlightSelectionInClipRect(id obj, SEL sel, NSRect rect)
{
	return ((void (*)(id, SEL, NSRect*))highlightSelectionInClipRect_1CALLBACK)(obj, sel, &rect);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(highlightSelectionInClipRect_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	highlightSelectionInClipRect_1CALLBACK = func;
	return (jintLong)highlightSelectionInClipRect;
}
#endif

#ifndef NO_webView_1setFrame_1CALLBACK
static jintLong webView_1setFrame_1CALLBACK;
static void webView_1setFrame(id obj, SEL sel, WebView *sender, NSRect rect)
{
	return ((void (*)(id, SEL, WebView*, NSRect*))webView_1setFrame_1CALLBACK)(obj, sel, sender, &rect);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(webView_1setFrame_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	webView_1setFrame_1CALLBACK = func;
	return (jintLong)webView_1setFrame;
}
#endif

#ifndef NO_setMarkedText_1selectedRange_1CALLBACK
static jintLong setMarkedText_1selectedRange_1CALLBACK;
static void setMarkedText_1selectedRange(id obj, SEL sel, id *arg0, NSRange arg1)
{
	((void (*)(id, SEL, id*, NSRange*))setMarkedText_1selectedRange_1CALLBACK)(obj, sel, arg0, &arg1);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(setMarkedText_1selectedRange_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	setMarkedText_1selectedRange_1CALLBACK = func;
	return (jintLong)setMarkedText_1selectedRange;
}
#endif

#ifndef NO_selectedRange_1CALLBACK
static jintLong selectedRange_1CALLBACK;
static NSRange selectedRangeProc(id obj, SEL sel)
{
	NSRange* ptr = ((NSRange* (*)(id, SEL))selectedRange_1CALLBACK)(obj, sel);
	NSRange range = *ptr;
	free(ptr);
	return range;
}
JNIEXPORT jintLong JNICALL OS_NATIVE(selectedRange_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	selectedRange_1CALLBACK = func;
	return (jintLong)selectedRangeProc;
}
#endif

#ifndef NO_markedRange_1CALLBACK
static jintLong markedRange_1CALLBACK;
static NSRange markedRangeProc(id obj, SEL sel)
{
	NSRange* ptr = ((NSRange* (*)(id, SEL))markedRange_1CALLBACK)(obj, sel);
	NSRange range = *ptr;
	free(ptr);
	return range;
}
JNIEXPORT jintLong JNICALL OS_NATIVE(markedRange_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	markedRange_1CALLBACK = func;
	return (jintLong)markedRangeProc;
}
#endif

#ifndef NO_characterIndexForPoint_1CALLBACK
static jintLong characterIndexForPoint_1CALLBACK;
static int characterIndexForPoint(id obj, SEL sel, NSPoint point)
{
	return ((int (*)(id, SEL, NSPoint*))characterIndexForPoint_1CALLBACK)(obj, sel, &point);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(characterIndexForPoint_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	characterIndexForPoint_1CALLBACK = func;
	return (jintLong)characterIndexForPoint;
}
#endif

#ifndef NO_firstRectForCharacterRange_1CALLBACK
static jintLong firstRectForCharacterRange_1CALLBACK;
static NSRect firstRectForCharacterRangeProc(id obj, SEL sel, NSRange arg0)
{
	NSRect* ptr = ((NSRect* (*)(id, SEL, NSRange*))firstRectForCharacterRange_1CALLBACK)(obj, sel, &arg0);
	NSRect result = *ptr;
	free(ptr);
	return result;
}
JNIEXPORT jintLong JNICALL OS_NATIVE(firstRectForCharacterRange_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	firstRectForCharacterRange_1CALLBACK = func;
	return (jintLong)firstRectForCharacterRangeProc;
}
#endif

#ifndef NO_attributedSubstringFromRange_1CALLBACK
static jintLong attributedSubstringFromRange_1CALLBACK;
static id attributedSubstringFromRangeProc(id obj, SEL sel, NSRange arg0)
{
	return ((id (*)(id, SEL, NSRange*))attributedSubstringFromRange_1CALLBACK)(obj, sel, &arg0);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(attributedSubstringFromRange_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	attributedSubstringFromRange_1CALLBACK = func;
	return (jintLong)attributedSubstringFromRangeProc;
}
#endif

#ifndef NO_textView_1willChangeSelectionFromCharacterRange_1toCharacterRange_1CALLBACK
static jintLong textView_1willChangeSelectionFromCharacterRange_1toCharacterRange_1CALLBACK;
static NSRange textView_1willChangeSelectionFromCharacterRange_1toCharacterRange(id obj, SEL sel, NSTextView *aTextView, NSRange oldSelectedCharRange, NSRange newSelectedCharRange)
{
	NSRange* ptr = ((NSRange* (*)(id, SEL, NSTextView*, NSRange*, NSRange*))textView_1willChangeSelectionFromCharacterRange_1toCharacterRange_1CALLBACK)(obj, sel, aTextView, &oldSelectedCharRange, &newSelectedCharRange);
	NSRange result = *ptr;
	free(ptr);
	return result;
}
JNIEXPORT jintLong JNICALL OS_NATIVE(textView_1willChangeSelectionFromCharacterRange_1toCharacterRange_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	textView_1willChangeSelectionFromCharacterRange_1toCharacterRange_1CALLBACK = func;
	return (jintLong)textView_1willChangeSelectionFromCharacterRange_1toCharacterRange;
}
#endif

#ifndef NO_accessibilityHitTest_1CALLBACK
static jintLong accessibilityHitTest_1CALLBACK;
static void accessibilityHitTest(id obj, SEL sel, NSPoint point)
{
	return ((void (*)(id, SEL, NSPoint *))accessibilityHitTest_1CALLBACK)(obj, sel, &point);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(accessibilityHitTest_1CALLBACK)
(JNIEnv *env, jclass that, jintLong func)
{
	accessibilityHitTest_1CALLBACK = func;
	return (jintLong)accessibilityHitTest;
}
#endif

#ifndef NO_isFlipped_1CALLBACK
static BOOL isFlippedProc(id obj, SEL sel)
{
	return YES;
}
JNIEXPORT jintLong JNICALL OS_NATIVE(isFlipped_1CALLBACK)
(JNIEnv *env, jclass that)
{
	return (jintLong)isFlippedProc;
}
#endif

#ifndef NO_kTISPropertyUnicodeKeyLayoutData
JNIEXPORT jintLong JNICALL OS_NATIVE(kTISPropertyUnicodeKeyLayoutData)
(JNIEnv *env, jclass that)
{
	// Technically this CFStringRef should be CFRetain'ed but we have no opportunity to release it.
	// The pointer won't disappear unless the Carbon framework bundle is somehow unloaded, which is unlikely to happen.
	static int initialized = 0;
	static CFStringRef *var = NULL;
	if (!initialized) {
		CFBundleRef bundle = CFBundleGetBundleWithIdentifier(CFSTR("com.apple.Carbon"));
		if (bundle) var = (CFStringRef *)CFBundleGetDataPointerForName(bundle, CFSTR("kTISPropertyUnicodeKeyLayoutData"));
		initialized = 1;
	} 
	
	return (jintLong)(*var);
}
#endif

#ifndef NO_shouldChangeTextInRange_1replacementString_1CALLBACK
static jintLong shouldChangeTextInRange_1replacementString_1CALLBACK;
static BOOL shouldChangeTextInRange_1replacementString(id obj, SEL sel, NSRange affectedCharRange, NSString *replacementString)
{
	return ((BOOL (*)(id, SEL, NSRange*, NSString*))shouldChangeTextInRange_1replacementString_1CALLBACK)(obj, sel, &affectedCharRange, replacementString);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(shouldChangeTextInRange_1replacementString_1CALLBACK)
(JNIEnv *env, jclass that, jintLong func)
{
	shouldChangeTextInRange_1replacementString_1CALLBACK = func;
	return (jintLong)shouldChangeTextInRange_1replacementString;
}
#endif

#ifndef NO_view_1stringForToolTip_1point_1userData_1CALLBACK
static jintLong view_1stringForToolTip_1point_1userData_1CALLBACK;
static NSString * view_1stringForToolTip_1point_1userDataProc(id obj, SEL sel, NSView* view, NSToolTipTag tag, NSPoint point, void *userData)
{
	return ((NSString* (*)(id, SEL, NSView*, NSToolTipTag, NSPoint*, void *))view_1stringForToolTip_1point_1userData_1CALLBACK)(obj, sel, view, tag, &point, userData);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(view_1stringForToolTip_1point_1userData_1CALLBACK)
(JNIEnv *env, jclass that, jintLong func)
{
	view_1stringForToolTip_1point_1userData_1CALLBACK = func;
	return (jintLong)view_1stringForToolTip_1point_1userDataProc;
}
#endif

#ifndef NO_canDragRowsWithIndexes_1atPoint_1CALLBACK
static jintLong canDragRowsWithIndexes_1atPoint_1CALLBACK;
static BOOL canDragRowsWithIndexes_1atPoint_1Proc(id obj, SEL sel, NSIndexSet *indexes, NSPoint point)
{
	return ((BOOL (*)(id, SEL, NSIndexSet*, NSPoint*))canDragRowsWithIndexes_1atPoint_1CALLBACK)(obj, sel, indexes, &point);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(canDragRowsWithIndexes_1atPoint_1CALLBACK)
(JNIEnv *env, jclass that, jintLong func)
{
	canDragRowsWithIndexes_1atPoint_1CALLBACK = func;
	return (jintLong)canDragRowsWithIndexes_1atPoint_1Proc;
}
#endif


#ifndef NO_setNeedsDisplayInRect_1CALLBACK
static jintLong setNeedsDisplayInRect_1CALLBACK;
static void setNeedsDisplayInRectProc(id obj, SEL sel, NSRect rect)
{
	return ((void (*)(id, SEL, NSRect*))setNeedsDisplayInRect_1CALLBACK)(obj, sel, &rect);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(setNeedsDisplayInRect_1CALLBACK)
(JNIEnv *env, jclass that, jintLong func)
{
	setNeedsDisplayInRect_1CALLBACK = func;
	return (jintLong)setNeedsDisplayInRectProc;
}
#endif

#ifndef NO_drawWithFrame_1inView_1CALLBACK
static jintLong drawWithFrame_1inView_1CALLBACK;
static void drawWithFrame_1inView(id obj, SEL sel, NSRect rect, NSView* view)
{
	return ((void (*)(id, SEL, NSRect*, NSView*))drawWithFrame_1inView_1CALLBACK)(obj, sel, &rect, view);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(drawWithFrame_1inView_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	drawWithFrame_1inView_1CALLBACK = func;
	return (jintLong)drawWithFrame_1inView;
}
#endif

#ifndef NO_imageRectForBounds_1CALLBACK
static jintLong imageRectForBounds_1CALLBACK;
static NSRect imageRectForBoundsProc(id obj, SEL sel, NSRect cellFrame)
{
	NSRect* ptr = ((NSRect* (*)(id, SEL, NSRect*))imageRectForBounds_1CALLBACK)(obj, sel, &cellFrame);
	NSRect result = *ptr;
	free(ptr);
	return result;
}
JNIEXPORT jintLong JNICALL OS_NATIVE(imageRectForBounds_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	imageRectForBounds_1CALLBACK = func;
	return (jintLong)imageRectForBoundsProc;
}
#endif

#ifndef NO_titleRectForBounds_1CALLBACK
static jintLong titleRectForBounds_1CALLBACK;
static NSRect titleRectForBoundsProc(id obj, SEL sel, NSRect cellFrame)
{
	NSRect* ptr = ((NSRect* (*)(id, SEL, NSRect*))titleRectForBounds_1CALLBACK)(obj, sel, &cellFrame);
	NSRect result = *ptr;
	free(ptr);
	return result;
}
JNIEXPORT jintLong JNICALL OS_NATIVE(titleRectForBounds_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	titleRectForBounds_1CALLBACK = func;
	return (jintLong)titleRectForBoundsProc;
}
#endif

#ifndef NO_cellSize_1CALLBACK
static jintLong cellSize_1CALLBACK;
static NSSize cellSizeProc(id obj, SEL sel)
{
	NSSize* ptr = ((NSSize* (*)(id, SEL))cellSize_1CALLBACK)(obj, sel);
	NSSize range = *ptr;
	free(ptr);
	return range;
}
JNIEXPORT jintLong JNICALL OS_NATIVE(cellSize_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	cellSize_1CALLBACK = func;
	return (jintLong)cellSizeProc;
}
#endif

#ifndef NO_hitTestForEvent_1inRect_1ofView_1CALLBACK
static jintLong hitTestForEvent_1inRect_1ofView_1CALLBACK;
static NSUInteger hitTestForEvent_1inRect_1ofViewProc(id obj, SEL sel, NSEvent* event, NSRect frame, NSView* view)
{
	return ((NSUInteger (*)(id, SEL, NSEvent*, NSRect*, NSView*))hitTestForEvent_1inRect_1ofView_1CALLBACK)(obj, sel, event, &frame, view);
}
JNIEXPORT jintLong JNICALL OS_NATIVE(hitTestForEvent_1inRect_1ofView_1CALLBACK)
	(JNIEnv *env, jclass that, jintLong func)
{
	hitTestForEvent_1inRect_1ofView_1CALLBACK = func;
	return (jintLong)hitTestForEvent_1inRect_1ofViewProc;
}
#endif
