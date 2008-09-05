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

#ifdef NATIVE_STATS
extern int OS_nativeFunctionCount;
extern int OS_nativeFunctionCallCount[];
extern char* OS_nativeFunctionNames[];
#define OS_NATIVE_ENTER(env, that, func) OS_nativeFunctionCallCount[func]++;
#define OS_NATIVE_EXIT(env, that, func) 
#else
#ifndef OS_NATIVE_ENTER
#define OS_NATIVE_ENTER(env, that, func) 
#endif
#ifndef OS_NATIVE_EXIT
#define OS_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	CGPoint_1sizeof_FUNC,
	CGRect_1sizeof_FUNC,
	CGSize_1sizeof_FUNC,
	CGWarpMouseCursorPosition_FUNC,
	CPSSetProcessName_FUNC,
	CloseRgn_FUNC,
	CopyRgn_FUNC,
	DeleteGlobalRef_FUNC,
	DiffRgn_FUNC,
	DisposeRgn_FUNC,
	EmptyRgn_FUNC,
	Gestalt_FUNC,
	GetCurrentProcess_FUNC,
	GetRegionBounds_FUNC,
	JNIGetObject_FUNC,
	LineTo_FUNC,
	MoveTo_FUNC,
	NSAccessibilityPositionAttribute_FUNC,
	NSAccessibilitySizeAttribute_FUNC,
	NSAffineTransformStruct_1sizeof_FUNC,
	NSBackgroundColorAttributeName_FUNC,
	NSBaselineOffsetAttributeName_FUNC,
	NSBitsPerPixelFromDepth_FUNC,
	NSCalibratedRGBColorSpace_FUNC,
	NSDefaultRunLoopMode_FUNC,
	NSDeviceRGBColorSpace_FUNC,
	NSDeviceResolution_FUNC,
	NSErrorFailingURLStringKey_FUNC,
	NSFileTypeForHFSTypeCode_FUNC,
	NSFilenamesPboardType_FUNC,
	NSFontAttributeName_FUNC,
	NSForegroundColorAttributeName_FUNC,
	NSGetSizeAndAlignment_FUNC,
	NSIntersectionRect_FUNC,
	NSLinkAttributeName_FUNC,
	NSParagraphStyleAttributeName_FUNC,
	NSPoint_1sizeof_FUNC,
	NSPrintAllPages_FUNC,
	NSPrintCopies_FUNC,
	NSPrintFirstPage_FUNC,
	NSPrintJobDisposition_FUNC,
	NSPrintLastPage_FUNC,
	NSPrintMustCollate_FUNC,
	NSPrintPreviewJob_FUNC,
	NSPrintSaveJob_FUNC,
	NSPrintSavePath_FUNC,
	NSPrintSpoolJob_FUNC,
	NSRTFPboardType_FUNC,
	NSRange_1sizeof_FUNC,
	NSRect_1sizeof_FUNC,
	NSSearchPathForDirectoriesInDomains_FUNC,
	NSSize_1sizeof_FUNC,
	NSStrikethroughColorAttributeName_FUNC,
	NSStrikethroughStyleAttributeName_FUNC,
	NSStringPboardType_FUNC,
	NSTIFFPboardType_FUNC,
	NSTemporaryDirectory_FUNC,
	NSURLPboardType_FUNC,
	NSUnderlineColorAttributeName_FUNC,
	NSUnderlineStyleAttributeName_FUNC,
	NewGlobalRef_FUNC,
	NewRgn_FUNC,
	OffsetRgn_FUNC,
	OpenRgn_FUNC,
	PtInRgn_FUNC,
	QDRegionToRects_FUNC,
	RectInRgn_FUNC,
	RectRgn_FUNC,
	SectRgn_FUNC,
	SetFrontProcess_FUNC,
	SetRect_FUNC,
	TransformProcessType_FUNC,
	UnionRgn_FUNC,
	attributedSubstringFromRange_1CALLBACK_FUNC,
	characterIndexForPoint_1CALLBACK_FUNC,
	class_1addIvar_FUNC,
	class_1addMethod_FUNC,
	class_1addProtocol_FUNC,
	drawInteriorWithFrame_1inView_1CALLBACK_FUNC,
	drawRect_1CALLBACK_FUNC,
	firstRectForCharacterRange_1CALLBACK_FUNC,
	getpid_FUNC,
	hitTest_1CALLBACK_FUNC,
	instrumentObjcMessageSends_FUNC,
	markedRange_1CALLBACK_FUNC,
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_cocoa_CGPoint_2I_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_cocoa_CGPoint_2J_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_cocoa_CGRect_2I_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_cocoa_CGRect_2J_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_cocoa_CGSize_2I_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_cocoa_CGSize_2J_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2I_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2J_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_cocoa_NSPoint_2J_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_cocoa_NSRange_2J_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_cocoa_NSRect_2J_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_cocoa_NSSize_2I_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_cocoa_NSSize_2J_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2II_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_cocoa_CGPoint_2JJ_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2II_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_cocoa_CGRect_2JJ_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2II_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_cocoa_CGSize_2JJ_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2JJ_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJ_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJ_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJ_FUNC,
#endif
	objc_1allocateClassPair_FUNC,
	objc_1getClass_FUNC,
	objc_1getProtocol_FUNC,
	objc_1lookUpClass_FUNC,
#ifndef JNI64
	objc_1msgSend__II_FUNC,
#else
	objc_1msgSend__JJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IID_FUNC,
#else
	objc_1msgSend__JJD_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIDIIIZ_FUNC,
#else
	objc_1msgSend__JJDJJJZ_FUNC,
#endif
	objc_1msgSend__IIF_FUNC,
#ifndef JNI64
	objc_1msgSend__IIFF_FUNC,
#else
	objc_1msgSend__JJDD_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIFFFF_FUNC,
#else
	objc_1msgSend__JJDDDD_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__III_FUNC,
#else
	objc_1msgSend__JJJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIIF_FUNC,
#else
	objc_1msgSend__JJJD_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIII_FUNC,
#else
	objc_1msgSend__JJJJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIIII_FUNC,
#else
	objc_1msgSend__JJJJJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIIIII_FUNC,
#else
	objc_1msgSend__JJJJJJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIIIIII_FUNC,
#else
	objc_1msgSend__JJJJJJJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIIIIIIII_FUNC,
#else
	objc_1msgSend__JJJJJJJJJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIIIIIIZZIII_FUNC,
#else
	objc_1msgSend__JJJJJJJZZJJJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIIIIIIZZIIII_FUNC,
#else
	objc_1msgSend__JJJJJJJZZJJJJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIIIIZ_FUNC,
#else
	objc_1msgSend__JJJJJZ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC,
#else
	objc_1msgSend__JJJJLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIIIZ_FUNC,
#else
	objc_1msgSend__JJJJZ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
#else
	objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSPoint_2IDIISII_FUNC,
#else
	objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSPoint_2JDJJSJJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC,
#else
	objc_1msgSend__JJJLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIIZ_FUNC,
#else
	objc_1msgSend__JJJZ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFF_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2DDD_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2FFFZ_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2DDDZ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_3F_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2J_3D_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2J_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSPoint_2Lorg_eclipse_swt_internal_cocoa_NSRect_2JD_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2J_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2F_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2D_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2FF_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2DD_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2J_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZ_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJZ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IIZI_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JJZJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2IZI_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2JZJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRange_2Lorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2IF_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Lorg_eclipse_swt_internal_cocoa_NSRect_2JD_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSRect_2Z_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSRect_2Z_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC,
#else
	objc_1msgSend__JJLorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIZ_FUNC,
#else
	objc_1msgSend__JJZ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__IIZI_FUNC,
#else
	objc_1msgSend__JJZJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__II_3B_FUNC,
#else
	objc_1msgSend__JJ_3B_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__II_3BI_FUNC,
#else
	objc_1msgSend__JJ_3BJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__II_3C_FUNC,
#else
	objc_1msgSend__JJ_3C_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__II_3CI_FUNC,
#else
	objc_1msgSend__JJ_3CJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__II_3CLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC,
#else
	objc_1msgSend__JJ_3CLorg_eclipse_swt_internal_cocoa_NSRange_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__II_3F_FUNC,
#else
	objc_1msgSend__JJ_3D_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__II_3FIF_FUNC,
#else
	objc_1msgSend__JJ_3DJD_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__II_3I_FUNC,
#else
	objc_1msgSend__JJ_3I_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend__II_3III_FUNC,
#else
	objc_1msgSend__JJ_3JJJ_FUNC,
#endif
	objc_1msgSend__JJFD_FUNC,
	objc_1msgSend__JJI_FUNC,
#ifndef JNI64
	objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2I_FUNC,
#else
	objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2J_FUNC,
#endif
#ifndef JNI64
	objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2II_FUNC,
#else
	objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2IIIIZ_FUNC,
#else
	objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JJJJZ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
#else
	objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC,
#else
	objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2ILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC,
#else
	objc_1msgSendSuper__Lorg_eclipse_swt_internal_cocoa_objc_1super_2JLorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1fpret__II_FUNC,
#else
	objc_1msgSend_1fpret__JJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1fpret__III_FUNC,
#else
	objc_1msgSend_1fpret__JJJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1fpret__IIII_FUNC,
#else
	objc_1msgSend_1fpret__JJJJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2II_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSAffineTransformStruct_2JJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2II_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2III_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJLorg_eclipse_swt_internal_cocoa_NSPoint_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2IILorg_eclipse_swt_internal_cocoa_NSPoint_2I_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSPoint_2JJLorg_eclipse_swt_internal_cocoa_NSPoint_2J_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2II_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2III_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRange_2JJJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2II_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2III_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIII_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IIIIZ_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJJJZ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRange_2I_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRange_2J_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2IILorg_eclipse_swt_internal_cocoa_NSRect_2I_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSRect_2JJLorg_eclipse_swt_internal_cocoa_NSRect_2J_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2II_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJ_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2I_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2J_FUNC,
#endif
#ifndef JNI64
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2IILorg_eclipse_swt_internal_cocoa_NSSize_2ZZI_FUNC,
#else
	objc_1msgSend_1stret__Lorg_eclipse_swt_internal_cocoa_NSSize_2JJLorg_eclipse_swt_internal_cocoa_NSSize_2ZZJ_FUNC,
#endif
	objc_1registerClassPair_FUNC,
	objc_1super_1sizeof_FUNC,
	object_1getClassName_FUNC,
	object_1getInstanceVariable_FUNC,
	object_1setClass_FUNC,
	object_1setInstanceVariable_FUNC,
	sel_1registerName_FUNC,
	selectedRange_1CALLBACK_FUNC,
	setFrameOrigin_1CALLBACK_FUNC,
	setFrameSize_1CALLBACK_FUNC,
	setFrame_1CALLBACK_FUNC,
	setMarkedText_1selectedRange_1CALLBACK_FUNC,
	textView_1willChangeSelectionFromCharacterRange_1toCharacterRange_1CALLBACK_FUNC,
	webView_1setFrame_1CALLBACK_FUNC,
} OS_FUNCS;
