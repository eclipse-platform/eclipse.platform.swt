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

#include "os.h"

#ifndef NO_CFRange
void cacheCFRangeFields(JNIEnv *env, jobject lpObject);
CFRange *getCFRangeFields(JNIEnv *env, jobject lpObject, CFRange *lpStruct);
void setCFRangeFields(JNIEnv *env, jobject lpObject, CFRange *lpStruct);
#define CFRange_sizeof() sizeof(CFRange)
#else
#define cacheCFRangeFields(a,b)
#define getCFRangeFields(a,b,c) NULL
#define setCFRangeFields(a,b,c)
#define CFRange_sizeof() 0
#endif

#ifndef NO_CGAffineTransform
void cacheCGAffineTransformFields(JNIEnv *env, jobject lpObject);
CGAffineTransform *getCGAffineTransformFields(JNIEnv *env, jobject lpObject, CGAffineTransform *lpStruct);
void setCGAffineTransformFields(JNIEnv *env, jobject lpObject, CGAffineTransform *lpStruct);
#define CGAffineTransform_sizeof() sizeof(CGAffineTransform)
#else
#define cacheCGAffineTransformFields(a,b)
#define getCGAffineTransformFields(a,b,c) NULL
#define setCGAffineTransformFields(a,b,c)
#define CGAffineTransform_sizeof() 0
#endif

#ifndef NO_CGPathElement
void cacheCGPathElementFields(JNIEnv *env, jobject lpObject);
CGPathElement *getCGPathElementFields(JNIEnv *env, jobject lpObject, CGPathElement *lpStruct);
void setCGPathElementFields(JNIEnv *env, jobject lpObject, CGPathElement *lpStruct);
#define CGPathElement_sizeof() sizeof(CGPathElement)
#else
#define cacheCGPathElementFields(a,b)
#define getCGPathElementFields(a,b,c) NULL
#define setCGPathElementFields(a,b,c)
#define CGPathElement_sizeof() 0
#endif

#ifndef NO_CGPoint
void cacheCGPointFields(JNIEnv *env, jobject lpObject);
CGPoint *getCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct);
void setCGPointFields(JNIEnv *env, jobject lpObject, CGPoint *lpStruct);
#define CGPoint_sizeof() sizeof(CGPoint)
#else
#define cacheCGPointFields(a,b)
#define getCGPointFields(a,b,c) NULL
#define setCGPointFields(a,b,c)
#define CGPoint_sizeof() 0
#endif

#ifndef NO_CGRect
void cacheCGRectFields(JNIEnv *env, jobject lpObject);
CGRect *getCGRectFields(JNIEnv *env, jobject lpObject, CGRect *lpStruct);
void setCGRectFields(JNIEnv *env, jobject lpObject, CGRect *lpStruct);
#define CGRect_sizeof() sizeof(CGRect)
#else
#define cacheCGRectFields(a,b)
#define getCGRectFields(a,b,c) NULL
#define setCGRectFields(a,b,c)
#define CGRect_sizeof() 0
#endif

#ifndef NO_CGSize
void cacheCGSizeFields(JNIEnv *env, jobject lpObject);
CGSize *getCGSizeFields(JNIEnv *env, jobject lpObject, CGSize *lpStruct);
void setCGSizeFields(JNIEnv *env, jobject lpObject, CGSize *lpStruct);
#define CGSize_sizeof() sizeof(CGSize)
#else
#define cacheCGSizeFields(a,b)
#define getCGSizeFields(a,b,c) NULL
#define setCGSizeFields(a,b,c)
#define CGSize_sizeof() 0
#endif

#ifndef NO_CTParagraphStyleSetting
void cacheCTParagraphStyleSettingFields(JNIEnv *env, jobject lpObject);
CTParagraphStyleSetting *getCTParagraphStyleSettingFields(JNIEnv *env, jobject lpObject, CTParagraphStyleSetting *lpStruct);
void setCTParagraphStyleSettingFields(JNIEnv *env, jobject lpObject, CTParagraphStyleSetting *lpStruct);
#define CTParagraphStyleSetting_sizeof() sizeof(CTParagraphStyleSetting)
#else
#define cacheCTParagraphStyleSettingFields(a,b)
#define getCTParagraphStyleSettingFields(a,b,c) NULL
#define setCTParagraphStyleSettingFields(a,b,c)
#define CTParagraphStyleSetting_sizeof() 0
#endif

#ifndef NO_NSAffineTransformStruct
void cacheNSAffineTransformStructFields(JNIEnv *env, jobject lpObject);
NSAffineTransformStruct *getNSAffineTransformStructFields(JNIEnv *env, jobject lpObject, NSAffineTransformStruct *lpStruct);
void setNSAffineTransformStructFields(JNIEnv *env, jobject lpObject, NSAffineTransformStruct *lpStruct);
#define NSAffineTransformStruct_sizeof() sizeof(NSAffineTransformStruct)
#else
#define cacheNSAffineTransformStructFields(a,b)
#define getNSAffineTransformStructFields(a,b,c) NULL
#define setNSAffineTransformStructFields(a,b,c)
#define NSAffineTransformStruct_sizeof() 0
#endif

#ifndef NO_NSPoint
void cacheNSPointFields(JNIEnv *env, jobject lpObject);
NSPoint *getNSPointFields(JNIEnv *env, jobject lpObject, NSPoint *lpStruct);
void setNSPointFields(JNIEnv *env, jobject lpObject, NSPoint *lpStruct);
#define NSPoint_sizeof() sizeof(NSPoint)
#else
#define cacheNSPointFields(a,b)
#define getNSPointFields(a,b,c) NULL
#define setNSPointFields(a,b,c)
#define NSPoint_sizeof() 0
#endif

#ifndef NO_NSRange
void cacheNSRangeFields(JNIEnv *env, jobject lpObject);
NSRange *getNSRangeFields(JNIEnv *env, jobject lpObject, NSRange *lpStruct);
void setNSRangeFields(JNIEnv *env, jobject lpObject, NSRange *lpStruct);
#define NSRange_sizeof() sizeof(NSRange)
#else
#define cacheNSRangeFields(a,b)
#define getNSRangeFields(a,b,c) NULL
#define setNSRangeFields(a,b,c)
#define NSRange_sizeof() 0
#endif

#ifndef NO_NSRect
void cacheNSRectFields(JNIEnv *env, jobject lpObject);
NSRect *getNSRectFields(JNIEnv *env, jobject lpObject, NSRect *lpStruct);
void setNSRectFields(JNIEnv *env, jobject lpObject, NSRect *lpStruct);
#define NSRect_sizeof() sizeof(NSRect)
#else
#define cacheNSRectFields(a,b)
#define getNSRectFields(a,b,c) NULL
#define setNSRectFields(a,b,c)
#define NSRect_sizeof() 0
#endif

#ifndef NO_NSSize
void cacheNSSizeFields(JNIEnv *env, jobject lpObject);
NSSize *getNSSizeFields(JNIEnv *env, jobject lpObject, NSSize *lpStruct);
void setNSSizeFields(JNIEnv *env, jobject lpObject, NSSize *lpStruct);
#define NSSize_sizeof() sizeof(NSSize)
#else
#define cacheNSSizeFields(a,b)
#define getNSSizeFields(a,b,c) NULL
#define setNSSizeFields(a,b,c)
#define NSSize_sizeof() 0
#endif

#ifndef NO_PMResolution
void cachePMResolutionFields(JNIEnv *env, jobject lpObject);
PMResolution *getPMResolutionFields(JNIEnv *env, jobject lpObject, PMResolution *lpStruct);
void setPMResolutionFields(JNIEnv *env, jobject lpObject, PMResolution *lpStruct);
#define PMResolution_sizeof() sizeof(PMResolution)
#else
#define cachePMResolutionFields(a,b)
#define getPMResolutionFields(a,b,c) NULL
#define setPMResolutionFields(a,b,c)
#define PMResolution_sizeof() 0
#endif

#ifndef NO_objc_super
void cacheobjc_superFields(JNIEnv *env, jobject lpObject);
struct objc_super *getobjc_superFields(JNIEnv *env, jobject lpObject, struct objc_super *lpStruct);
void setobjc_superFields(JNIEnv *env, jobject lpObject, struct objc_super *lpStruct);
#define objc_super_sizeof() sizeof(struct objc_super)
#else
#define cacheobjc_superFields(a,b)
#define getobjc_superFields(a,b,c) NULL
#define setobjc_superFields(a,b,c)
#define objc_super_sizeof() 0
#endif

