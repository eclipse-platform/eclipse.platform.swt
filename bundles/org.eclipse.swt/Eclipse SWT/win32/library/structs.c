/*
 * (c) Copyright IBM Corp., 2000, 2001
 * All Rights Reserved.
 */

/**
 * JNI SWT object field getters and setters declarations for Windows structs
 */

#include <jni.h>
#include "structs.h"

/* ----------- fid and class caches  ----------- */
/*
 * Used for Java objects passed into JNI that are
 * declared like:
 *
 * 	nativeFunction (Rectangle p1, Rectangle p2, Rectangle p3)
 *
 * and not like this
 *
 * 	nativeFunction (Object p1, Object p2, Object p3)
 *
 *
 */
 
/* ============================ BEGIN WIN32 ONLY ============================= */
#ifndef _WIN32_WCE

void getPrintdlgFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpPrintdlg, PRINTDLG_FID_CACHE *lpPrintdlgFc)
{
    lpPrintdlg->lStructSize = (*env)->GetIntField(env,lpObject,lpPrintdlgFc->lStructSize);
    lpPrintdlg->hwndOwner = (HWND)(*env)->GetIntField(env,lpObject,lpPrintdlgFc->hwndOwner);
    lpPrintdlg->hDevMode = (HANDLE)(*env)->GetIntField(env,lpObject,lpPrintdlgFc->hDevMode);
    lpPrintdlg->hDevNames = (HANDLE)(*env)->GetIntField(env,lpObject,lpPrintdlgFc->hDevNames);
    lpPrintdlg->hDC = (HDC)(*env)->GetIntField(env,lpObject,lpPrintdlgFc->hDC);
    lpPrintdlg->Flags = (*env)->GetIntField(env,lpObject,lpPrintdlgFc->Flags);
    lpPrintdlg->nFromPage = (*env)->GetShortField(env,lpObject,lpPrintdlgFc->nFromPage);
    lpPrintdlg->nToPage = (*env)->GetShortField(env,lpObject,lpPrintdlgFc->nToPage);
    lpPrintdlg->nMinPage = (*env)->GetShortField(env,lpObject,lpPrintdlgFc->nMinPage);
    lpPrintdlg->nMaxPage = (*env)->GetShortField(env,lpObject,lpPrintdlgFc->nMaxPage);
    lpPrintdlg->nCopies = (*env)->GetShortField(env,lpObject,lpPrintdlgFc->nCopies);
    lpPrintdlg->hInstance = (HINSTANCE)(*env)->GetIntField(env,lpObject,lpPrintdlgFc->hInstance);
    lpPrintdlg->lCustData = (*env)->GetIntField(env,lpObject,lpPrintdlgFc->lCustData);
    lpPrintdlg->lpfnPrintHook = (LPPRINTHOOKPROC)(*env)->GetIntField(env,lpObject,lpPrintdlgFc->lpfnPrintHook);
    lpPrintdlg->lpfnSetupHook = (LPSETUPHOOKPROC)(*env)->GetIntField(env,lpObject,lpPrintdlgFc->lpfnSetupHook);
    lpPrintdlg->lpPrintTemplateName = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpPrintdlgFc->lpPrintTemplateName);
    lpPrintdlg->lpSetupTemplateName = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpPrintdlgFc->lpSetupTemplateName);
    lpPrintdlg->hPrintTemplate = (HANDLE)(*env)->GetIntField(env,lpObject,lpPrintdlgFc->hPrintTemplate);
    lpPrintdlg->hSetupTemplate = (HANDLE)(*env)->GetIntField(env,lpObject,lpPrintdlgFc->hSetupTemplate);
}

void setPrintdlgFields(JNIEnv *env, jobject lpObject, PRINTDLG *lpPrintdlg, PRINTDLG_FID_CACHE *lpPrintdlgFc)
{
    (*env)->SetIntField(env,lpObject,lpPrintdlgFc->lStructSize, lpPrintdlg->lStructSize);
    (*env)->SetIntField(env,lpObject,lpPrintdlgFc->hwndOwner, (jint)lpPrintdlg->hwndOwner);
    (*env)->SetIntField(env,lpObject,lpPrintdlgFc->hDevMode, (jint)lpPrintdlg->hDevMode);
    (*env)->SetIntField(env,lpObject,lpPrintdlgFc->hDevNames, (jint)lpPrintdlg->hDevNames);
    (*env)->SetIntField(env,lpObject,lpPrintdlgFc->hDC, (jint)lpPrintdlg->hDC);
    (*env)->SetIntField(env,lpObject,lpPrintdlgFc->Flags, lpPrintdlg->Flags);
    (*env)->SetShortField(env,lpObject,lpPrintdlgFc->nFromPage, lpPrintdlg->nFromPage);
    (*env)->SetShortField(env,lpObject,lpPrintdlgFc->nToPage, lpPrintdlg->nToPage);
    (*env)->SetShortField(env,lpObject,lpPrintdlgFc->nMinPage, lpPrintdlg->nMinPage);
    (*env)->SetShortField(env,lpObject,lpPrintdlgFc->nMaxPage, lpPrintdlg->nMaxPage);
    (*env)->SetShortField(env,lpObject,lpPrintdlgFc->nCopies, lpPrintdlg->nCopies);
    (*env)->SetIntField(env,lpObject,lpPrintdlgFc->hInstance, (jint)lpPrintdlg->hInstance);
    (*env)->SetIntField(env,lpObject,lpPrintdlgFc->lCustData, lpPrintdlg->lCustData);
    (*env)->SetIntField(env,lpObject,lpPrintdlgFc->lpfnPrintHook, (jint)lpPrintdlg->lpfnPrintHook);
    (*env)->SetIntField(env,lpObject,lpPrintdlgFc->lpfnSetupHook, (jint)lpPrintdlg->lpfnSetupHook);
    (*env)->SetIntField(env,lpObject,lpPrintdlgFc->lpPrintTemplateName, (jint)lpPrintdlg->lpPrintTemplateName);
    (*env)->SetIntField(env,lpObject,lpPrintdlgFc->lpSetupTemplateName, (jint)lpPrintdlg->lpSetupTemplateName);
    (*env)->SetIntField(env,lpObject,lpPrintdlgFc->hPrintTemplate, (jint)lpPrintdlg->hPrintTemplate);
    (*env)->SetIntField(env,lpObject,lpPrintdlgFc->hSetupTemplate, (jint)lpPrintdlg->hSetupTemplate);
}

void setNmttdispinfoFieldsA(JNIEnv *env, jobject lpObject, NMTTDISPINFO *lpNmttdispinfo, NMTTDISPINFO_FID_CACHE *lpNmttdispinfoFc)
{
    jint *lpInt;

    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->hwndFrom, (jint)lpNmttdispinfo->hdr.hwndFrom);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->idFrom, lpNmttdispinfo->hdr.idFrom);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->code, lpNmttdispinfo->hdr.code);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->lpszText, (jint)lpNmttdispinfo->lpszText);
    lpInt = (jint *)lpNmttdispinfo->szText;
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad0, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad1, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad2, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad3, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad4, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad5, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad6, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad7, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad8, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad9, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad10, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad11, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad12, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad13, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad14, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad15, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad16, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad17, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad18, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad19, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->hinst, (jint)lpNmttdispinfo->hinst);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->uFlags, lpNmttdispinfo->uFlags);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->lParam, lpNmttdispinfo->lParam);
}

void setNmttdispinfoFieldsW(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpNmttdispinfo, NMTTDISPINFO_FID_CACHE *lpNmttdispinfoFc)
{
    jint *lpInt;

    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->hwndFrom, (jint)lpNmttdispinfo->hdr.hwndFrom);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->idFrom, lpNmttdispinfo->hdr.idFrom);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->code, lpNmttdispinfo->hdr.code);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->lpszText, (jint)lpNmttdispinfo->lpszText);
    lpInt = (jint *)lpNmttdispinfo->szText;
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad0, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad1, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad2, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad3, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad4, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad5, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad6, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad7, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad8, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad9, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad10, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad11, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad12, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad13, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad14, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad15, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad16, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad17, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad18, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->pad19, *lpInt++);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->hinst, (jint)lpNmttdispinfo->hinst);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->uFlags, lpNmttdispinfo->uFlags);
    (*env)->SetIntField(env,lpObject,lpNmttdispinfoFc->lParam, lpNmttdispinfo->lParam);
}

void cacheTrackmouseeventFids(JNIEnv *env, jobject lpTrackmouseevent, PTRACKMOUSEEVENT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->trackmouseeventClass = (*env)->GetObjectClass(env,lpTrackmouseevent);
    lpCache->cbSize = (*env)->GetFieldID(env,lpCache->trackmouseeventClass,"cbSize","I");
    lpCache->dwFlags = (*env)->GetFieldID(env,lpCache->trackmouseeventClass,"dwFlags","I");
    lpCache->hwndTrack = (*env)->GetFieldID(env,lpCache->trackmouseeventClass,"hwndTrack","I");
    lpCache->dwHoverTime = (*env)->GetFieldID(env,lpCache->trackmouseeventClass,"dwHoverTime","I");
    lpCache->cached = 1;
}

void getTrackmouseeventFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpTrackmouseevent, TRACKMOUSEEVENT_FID_CACHE *lpTrackmouseeventFc)
{
    lpTrackmouseevent->cbSize = (*env)->GetIntField(env,lpObject,lpTrackmouseeventFc->cbSize);
    lpTrackmouseevent->dwFlags = (*env)->GetIntField(env,lpObject,lpTrackmouseeventFc->dwFlags);
    lpTrackmouseevent->hwndTrack = (HWND)(*env)->GetIntField(env,lpObject,lpTrackmouseeventFc->hwndTrack);
    lpTrackmouseevent->dwHoverTime = (*env)->GetIntField(env,lpObject,lpTrackmouseeventFc->dwHoverTime);
}

void setTrackmouseeventFields(JNIEnv *env, jobject lpObject, TRACKMOUSEEVENT *lpTrackmouseevent, TRACKMOUSEEVENT_FID_CACHE *lpTrackmouseeventFc)
{
    (*env)->SetIntField(env,lpObject,lpTrackmouseeventFc->cbSize, lpTrackmouseevent->cbSize);
    (*env)->SetIntField(env,lpObject,lpTrackmouseeventFc->dwFlags, lpTrackmouseevent->dwFlags);
    (*env)->SetIntField(env,lpObject,lpTrackmouseeventFc->hwndTrack, (jint)lpTrackmouseevent->hwndTrack);
    (*env)->SetIntField(env,lpObject,lpTrackmouseeventFc->dwHoverTime, lpTrackmouseevent->dwHoverTime);
}

void cacheDocinfoFids(JNIEnv *env, jobject lpDocinfo, PDOCINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->docinfoClass = (*env)->GetObjectClass(env,lpDocinfo);
    lpCache->cbSize = (*env)->GetFieldID(env,lpCache->docinfoClass,"cbSize","I");
    lpCache->lpszDocName = (*env)->GetFieldID(env,lpCache->docinfoClass,"lpszDocName","I");
    lpCache->lpszOutput = (*env)->GetFieldID(env,lpCache->docinfoClass,"lpszOutput","I");
    lpCache->lpszDatatype = (*env)->GetFieldID(env,lpCache->docinfoClass,"lpszDatatype","I");
    lpCache->fwType = (*env)->GetFieldID(env,lpCache->docinfoClass,"fwType","I");
    lpCache->cached = 1;
}

void getDocinfoFields(JNIEnv *env, jobject lpObject, DOCINFO *lpDocinfo, DOCINFO_FID_CACHE *lpDocinfoFc)
{
    lpDocinfo->cbSize = (*env)->GetIntField(env,lpObject,lpDocinfoFc->cbSize);
    lpDocinfo->lpszDocName = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpDocinfoFc->lpszDocName);
    lpDocinfo->lpszOutput = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpDocinfoFc->lpszOutput);
    lpDocinfo->lpszDatatype = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpDocinfoFc->lpszDatatype);
    lpDocinfo->fwType = (*env)->GetIntField(env,lpObject,lpDocinfoFc->fwType);
}

void setDocinfoFields(JNIEnv *env, jobject lpObject, DOCINFO *lpDocinfo, DOCINFO_FID_CACHE *lpDocinfoFc)
{
    (*env)->SetIntField(env,lpObject,lpDocinfoFc->cbSize, (jint)lpDocinfo->cbSize);
    (*env)->SetIntField(env,lpObject,lpDocinfoFc->lpszDocName, (jint)lpDocinfo->lpszDocName);
    (*env)->SetIntField(env,lpObject,lpDocinfoFc->lpszOutput, (jint)lpDocinfo->lpszOutput);
    (*env)->SetIntField(env,lpObject,lpDocinfoFc->lpszDatatype, (jint)lpDocinfo->lpszDatatype);
    (*env)->SetIntField(env,lpObject,lpDocinfoFc->fwType, lpDocinfo->fwType);
}

void cacheGradientrectFids(JNIEnv *env, jobject lpGradientrect, PGRADIENT_RECT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->gradientrectClass = (*env)->GetObjectClass(env,lpGradientrect);
    lpCache->UpperLeft = (*env)->GetFieldID(env,lpCache->gradientrectClass,"UpperLeft","I");
    lpCache->LowerRight = (*env)->GetFieldID(env,lpCache->gradientrectClass,"LowerRight","I");
    lpCache->cached = 1;
}

void getGradientrectFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpGradientrect, GRADIENT_RECT_FID_CACHE *lpGradientrectFc)
{
	lpGradientrect->UpperLeft = (*env)->GetIntField(env,lpObject,lpGradientrectFc->UpperLeft);
	lpGradientrect->LowerRight = (*env)->GetIntField(env,lpObject,lpGradientrectFc->LowerRight);
}

void setGradientrectFields(JNIEnv *env, jobject lpObject, GRADIENT_RECT *lpGradientrect, GRADIENT_RECT_FID_CACHE *lpGradientrectFc)
{
    (*env)->SetIntField(env,lpObject,lpGradientrectFc->UpperLeft, (jint)lpGradientrect->UpperLeft);
    (*env)->SetIntField(env,lpObject,lpGradientrectFc->LowerRight, (jint)lpGradientrect->LowerRight);
}

void cacheHelpinfoFids(JNIEnv *env, jobject lpHelpinfo, PHELPINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->helpinfoClass = (*env)->GetObjectClass(env,lpHelpinfo);
    lpCache->cbSize = (*env)->GetFieldID(env,lpCache->helpinfoClass,"cbSize","I");
    lpCache->iContextType = (*env)->GetFieldID(env,lpCache->helpinfoClass,"iContextType","I");
    lpCache->iCtrlId = (*env)->GetFieldID(env,lpCache->helpinfoClass,"iCtrlId","I");
    lpCache->hItemHandle = (*env)->GetFieldID(env,lpCache->helpinfoClass,"hItemHandle","I");
    lpCache->dwContextId = (*env)->GetFieldID(env,lpCache->helpinfoClass,"dwContextId","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->helpinfoClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->helpinfoClass,"y","I");
    lpCache->cached = 1;
}

void getHelpinfoFields(JNIEnv *env, jobject lpObject, HELPINFO *lpHelpinfo, HELPINFO_FID_CACHE *lpHelpinfoFc)
{
    lpHelpinfo->cbSize = (*env)->GetIntField(env,lpObject,lpHelpinfoFc->cbSize);
    lpHelpinfo->iContextType = (*env)->GetIntField(env,lpObject,lpHelpinfoFc->iContextType);
    lpHelpinfo->iCtrlId = (*env)->GetIntField(env,lpObject,lpHelpinfoFc->iCtrlId);
    lpHelpinfo->hItemHandle = (HANDLE)(*env)->GetIntField(env,lpObject,lpHelpinfoFc->hItemHandle);
    lpHelpinfo->dwContextId = (*env)->GetIntField(env,lpObject,lpHelpinfoFc->dwContextId);
    lpHelpinfo->MousePos.x = (*env)->GetIntField(env,lpObject,lpHelpinfoFc->x);
    lpHelpinfo->MousePos.y = (*env)->GetIntField(env,lpObject,lpHelpinfoFc->y);
}

void setHelpinfoFields(JNIEnv *env, jobject lpObject, HELPINFO *lpHelpinfo, HELPINFO_FID_CACHE *lpHelpinfoFc)
{
    (*env)->SetIntField(env,lpObject,lpHelpinfoFc->cbSize, lpHelpinfo->cbSize);
    (*env)->SetIntField(env,lpObject,lpHelpinfoFc->iContextType, lpHelpinfo->iContextType);
    (*env)->SetIntField(env,lpObject,lpHelpinfoFc->iCtrlId, lpHelpinfo->iCtrlId);
    (*env)->SetIntField(env,lpObject,lpHelpinfoFc->hItemHandle, (jint)lpHelpinfo->hItemHandle);
    (*env)->SetIntField(env,lpObject,lpHelpinfoFc->dwContextId, lpHelpinfo->dwContextId);
    (*env)->SetIntField(env,lpObject,lpHelpinfoFc->x, lpHelpinfo->MousePos.x);
    (*env)->SetIntField(env,lpObject,lpHelpinfoFc->y, lpHelpinfo->MousePos.y);
}

#ifdef USE_2000_CALLS
void cacheMenuinfoFids(JNIEnv *env, jobject lpMenuinfo, PMENUINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->menuinfoClass = (*env)->GetObjectClass(env,lpMenuinfo);
    lpCache->cbSize = (*env)->GetFieldID(env,lpCache->menuinfoClass,"cbSize","I");
    lpCache->fMask = (*env)->GetFieldID(env,lpCache->menuinfoClass,"fMask","I");
    lpCache->dwStyle = (*env)->GetFieldID(env,lpCache->menuinfoClass,"dwStyle","I");
    lpCache->cyMax = (*env)->GetFieldID(env,lpCache->menuinfoClass,"cyMax","I");
    lpCache->hbrBack = (*env)->GetFieldID(env,lpCache->menuinfoClass,"hbrBack","I");
    lpCache->dwContextHelpID = (*env)->GetFieldID(env,lpCache->menuinfoClass,"dwContextHelpID","I");
    lpCache->dwMenuData = (*env)->GetFieldID(env,lpCache->menuinfoClass,"dwMenuData","I");
    lpCache->cached = 1;
}

void getMenuinfoFields(JNIEnv *env, jobject lpObject, MENUINFO *lpMenuinfo, MENUINFO_FID_CACHE *lpMenuinfoFc)
{
    lpMenuinfo->cbSize = (*env)->GetIntField(env,lpObject,lpMenuinfoFc->cbSize);
    lpMenuinfo->fMask = (*env)->GetIntField(env,lpObject,lpMenuinfoFc->fMask);
    lpMenuinfo->dwStyle = (*env)->GetIntField(env,lpObject,lpMenuinfoFc->dwStyle);
    lpMenuinfo->cyMax = (*env)->GetIntField(env,lpObject,lpMenuinfoFc->cyMax);
    lpMenuinfo->hbrBack = (HBRUSH)(*env)->GetIntField(env,lpObject,lpMenuinfoFc->hbrBack);
    lpMenuinfo->dwContextHelpID = (*env)->GetIntField(env,lpObject,lpMenuinfoFc->dwContextHelpID);
    lpMenuinfo->dwMenuData = (*env)->GetIntField(env,lpObject,lpMenuinfoFc->dwMenuData);
}

void setMenuinfoFields(JNIEnv *env, jobject lpObject, MENUINFO *lpMenuinfo, MENUINFO_FID_CACHE *lpMenuinfoFc)
{
    (*env)->SetIntField(env,lpObject,lpMenuinfoFc->cbSize, lpMenuinfo->cbSize);
    (*env)->SetIntField(env,lpObject,lpMenuinfoFc->fMask, lpMenuinfo->fMask);
    (*env)->SetIntField(env,lpObject,lpMenuinfoFc->dwStyle, lpMenuinfo->dwStyle);
    (*env)->SetIntField(env,lpObject,lpMenuinfoFc->cyMax, lpMenuinfo->cyMax);
    (*env)->SetIntField(env,lpObject,lpMenuinfoFc->hbrBack, (jint)lpMenuinfo->hbrBack);
    (*env)->SetIntField(env,lpObject,lpMenuinfoFc->dwContextHelpID, lpMenuinfo->dwContextHelpID);
    (*env)->SetIntField(env,lpObject,lpMenuinfoFc->dwMenuData, lpMenuinfo->dwMenuData);
}
#endif

void cacheNmttdispinfoFids(JNIEnv *env, jobject lpNmttdispinfo, PNMTTDISPINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->nmttdispinfoClass = (*env)->GetObjectClass(env,lpNmttdispinfo);
    lpCache->hwndFrom = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"hwndFrom","I");
    lpCache->idFrom = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"idFrom","I");
    lpCache->lpszText = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"lpszText","I");
    lpCache->code = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"code","I");
    lpCache->pad0 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad0","I");
    lpCache->pad1 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad1","I");
    lpCache->pad2 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad2","I");
    lpCache->pad3 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad3","I");
    lpCache->pad4 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad4","I");
    lpCache->pad5 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad5","I");
    lpCache->pad6 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad6","I");
    lpCache->pad7 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad7","I");
    lpCache->pad8 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad8","I");
    lpCache->pad9 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad9","I");
    lpCache->pad10 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad10","I");
    lpCache->pad11 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad11","I");
    lpCache->pad12 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad12","I");
    lpCache->pad13 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad13","I");
    lpCache->pad14 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad14","I");
    lpCache->pad15 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad15","I");
    lpCache->pad16 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad16","I");
    lpCache->pad17 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad17","I");
    lpCache->pad18 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad18","I");
    lpCache->pad19 = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"pad19","I");
    lpCache->hinst = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"hinst","I");
    lpCache->uFlags = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"uFlags","I");
#ifndef _WIN32_WCE
    lpCache->lParam = (*env)->GetFieldID(env,lpCache->nmttdispinfoClass,"lParam","I");
#endif
    lpCache->cached = 1;
}

void getNmttdispinfoFieldsA(JNIEnv *env, jobject lpObject, NMTTDISPINFO *lpNmttdispinfo, NMTTDISPINFO_FID_CACHE *lpNmttdispinfoFc)
{
    jint *lpInt;

    lpNmttdispinfo->hdr.hwndFrom = (HWND)(*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->hwndFrom);
    lpNmttdispinfo->hdr.idFrom = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->idFrom);
    lpNmttdispinfo->hdr.code = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->code);
    lpNmttdispinfo->lpszText = (LPTSTR)(*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->lpszText);
    lpInt = (jint *)lpNmttdispinfo->szText;
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad0);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad1);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad2);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad3);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad4);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad5);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad6);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad7);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad8);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad9);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad10);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad11);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad12);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad13);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad14);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad15);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad16);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad17);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad18);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad19);
    lpNmttdispinfo->hinst = (HINSTANCE)(*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->hinst);
    lpNmttdispinfo->uFlags = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->uFlags);
#ifndef _WIN32_WCE
    lpNmttdispinfo->lParam = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->lParam);
#endif
}
void getNmttdispinfoFieldsW(JNIEnv *env, jobject lpObject, NMTTDISPINFOW *lpNmttdispinfo, NMTTDISPINFO_FID_CACHE *lpNmttdispinfoFc)
{
    jint *lpInt;

    lpNmttdispinfo->hdr.hwndFrom = (HWND)(*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->hwndFrom);
    lpNmttdispinfo->hdr.idFrom = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->idFrom);
    lpNmttdispinfo->hdr.code = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->code);
    lpNmttdispinfo->lpszText = (LPTSTR)(*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->lpszText);
    lpInt = (jint *)lpNmttdispinfo->szText;
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad0);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad1);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad2);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad3);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad4);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad5);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad6);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad7);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad8);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad9);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad10);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad11);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad12);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad13);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad14);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad15);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad16);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad17);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad18);
    *lpInt++ = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->pad19);
    lpNmttdispinfo->hinst = (HINSTANCE)(*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->hinst);
    lpNmttdispinfo->uFlags = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->uFlags);
#ifndef _WIN32_WCE
    lpNmttdispinfo->lParam = (*env)->GetIntField(env,lpObject,lpNmttdispinfoFc->lParam);
#endif
}

void cachePagesetupdlgFids(JNIEnv *env, jobject lpPagesetupdlg, PPAGESETUPDLG_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->pagesetupdlgClass = (*env)->GetObjectClass(env,lpPagesetupdlg);
    lpCache->lStructSize = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"lStructSize","I");
    lpCache->hwndOwner = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"hwndOwner","I");
    lpCache->hDevMode = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"hDevMode","I");
    lpCache->hDevNames = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"hDevNames","I");
    lpCache->Flags = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"Flags","I");
    lpCache->ptPaperSize_x = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"ptPaperSize_x","I");
    lpCache->ptPaperSize_y = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"ptPaperSize_y","I");
    lpCache->rtMinMargin_left = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"rtMinMargin_left","I");
    lpCache->rtMinMargin_top = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"rtMinMargin_top","I");
    lpCache->rtMinMargin_right = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"rtMinMargin_right","I");
    lpCache->rtMinMargin_bottom = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"rtMinMargin_bottom","I");
    lpCache->rtMargin_left = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"rtMargin_left","I");
    lpCache->rtMargin_top = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"rtMargin_top","I");
    lpCache->rtMargin_right = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"rtMargin_right","I");
    lpCache->rtMargin_bottom = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"rtMargin_bottom","I");
    lpCache->hInstance = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"hInstance","I");
    lpCache->lCustData = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"lCustData","I");
    lpCache->lpfnPageSetupHook = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"lpfnPageSetupHook","I");
    lpCache->lpfnPagePaintHook = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"lpfnPagePaintHook","I");
    lpCache->lpPageSetupTemplateName = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"lpPageSetupTemplateName","I");
    lpCache->hPageSetupTemplate = (*env)->GetFieldID(env,lpCache->pagesetupdlgClass,"hPageSetupTemplate","I");
    lpCache->cached = 1;
}

void getPagesetupdlgFields(JNIEnv *env, jobject lpObject, PAGESETUPDLG *lpPagesetupdlg, PAGESETUPDLG_FID_CACHE *lpPagesetupdlgFc)
{
    lpPagesetupdlg->lStructSize = (*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->lStructSize);
    lpPagesetupdlg->hwndOwner = (HWND)(*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->hwndOwner);
    lpPagesetupdlg->hDevMode = (HGLOBAL)(*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->hDevMode);
    lpPagesetupdlg->hDevNames = (HGLOBAL)(*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->hDevNames);
    lpPagesetupdlg->Flags = (*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->Flags);
    lpPagesetupdlg->ptPaperSize.x = (*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->ptPaperSize_x);
    lpPagesetupdlg->ptPaperSize.y = (*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->ptPaperSize_y);
    lpPagesetupdlg->rtMinMargin.left = (*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->rtMinMargin_left);
    lpPagesetupdlg->rtMinMargin.top = (*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->rtMinMargin_top);
    lpPagesetupdlg->rtMinMargin.right = (*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->rtMinMargin_right);
    lpPagesetupdlg->rtMinMargin.bottom = (*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->rtMinMargin_bottom);
    lpPagesetupdlg->rtMargin.left = (*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->rtMargin_left);
    lpPagesetupdlg->rtMargin.top = (*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->rtMargin_top);
    lpPagesetupdlg->rtMargin.right = (*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->rtMargin_right);
    lpPagesetupdlg->rtMargin.bottom = (*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->rtMargin_bottom);
    lpPagesetupdlg->hInstance = (HINSTANCE)(*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->hInstance);
    lpPagesetupdlg->lCustData = (*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->lCustData);
    lpPagesetupdlg->lpfnPageSetupHook = (LPPAGESETUPHOOK)(*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->lpfnPageSetupHook);
    lpPagesetupdlg->lpfnPagePaintHook = (LPPAGEPAINTHOOK)(*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->lpfnPagePaintHook);
    lpPagesetupdlg->lpPageSetupTemplateName = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->lpPageSetupTemplateName);
    lpPagesetupdlg->hPageSetupTemplate = (HGLOBAL)(*env)->GetIntField(env,lpObject,lpPagesetupdlgFc->hPageSetupTemplate);
}

void setPagesetupdlgFields(JNIEnv *env, jobject lpObject, PAGESETUPDLG *lpPagesetupdlg, PAGESETUPDLG_FID_CACHE *lpPagesetupdlgFc)
{
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->lStructSize, lpPagesetupdlg->lStructSize);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->hwndOwner, (jint)lpPagesetupdlg->hwndOwner);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->hDevMode, (jint)lpPagesetupdlg->hDevMode);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->hDevNames, (jint)lpPagesetupdlg->hDevNames);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->Flags, lpPagesetupdlg->Flags);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->ptPaperSize_x, lpPagesetupdlg->ptPaperSize.x);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->ptPaperSize_y, lpPagesetupdlg->ptPaperSize.y);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->rtMinMargin_left, lpPagesetupdlg->rtMinMargin.left);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->rtMinMargin_top, lpPagesetupdlg->rtMinMargin.top);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->rtMinMargin_right, lpPagesetupdlg->rtMinMargin.right);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->rtMinMargin_bottom, lpPagesetupdlg->rtMinMargin.bottom);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->rtMargin_left, lpPagesetupdlg->rtMargin.left);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->rtMargin_top, lpPagesetupdlg->rtMargin.top);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->rtMargin_right, lpPagesetupdlg->rtMargin.right);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->rtMargin_bottom, lpPagesetupdlg->rtMargin.bottom);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->hInstance, (jint)lpPagesetupdlg->hInstance);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->lCustData, lpPagesetupdlg->lCustData);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->lpfnPageSetupHook, (jint)lpPagesetupdlg->lpfnPageSetupHook);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->lpfnPagePaintHook, (jint)lpPagesetupdlg->lpfnPagePaintHook);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->lpPageSetupTemplateName, (jint)lpPagesetupdlg->lpPageSetupTemplateName);
    (*env)->SetIntField(env,lpObject,lpPagesetupdlgFc->hPageSetupTemplate, (jint)lpPagesetupdlg->hPageSetupTemplate);
}

void cacheToolinfoFids(JNIEnv *env, jobject lpToolinfo, PTOOLINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->toolinfoClass = (*env)->GetObjectClass(env,lpToolinfo);
    lpCache->cbSize = (*env)->GetFieldID(env,lpCache->toolinfoClass,"cbSize","I");
    lpCache->uFlags = (*env)->GetFieldID(env,lpCache->toolinfoClass,"uFlags","I");
    lpCache->hwnd = (*env)->GetFieldID(env,lpCache->toolinfoClass,"hwnd","I");
    lpCache->uId = (*env)->GetFieldID(env,lpCache->toolinfoClass,"uId","I");
    lpCache->left = (*env)->GetFieldID(env,lpCache->toolinfoClass,"left","I");
    lpCache->top = (*env)->GetFieldID(env,lpCache->toolinfoClass,"top","I");
    lpCache->right = (*env)->GetFieldID(env,lpCache->toolinfoClass,"right","I");
    lpCache->bottom = (*env)->GetFieldID(env,lpCache->toolinfoClass,"bottom","I");
    lpCache->hinst = (*env)->GetFieldID(env,lpCache->toolinfoClass,"hinst","I");
    lpCache->lpszText = (*env)->GetFieldID(env,lpCache->toolinfoClass,"lpszText","I");
    lpCache->lParam = (*env)->GetFieldID(env,lpCache->toolinfoClass,"lParam","I");
    lpCache->cached = 1;
}

void getToolinfoFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpToolinfo, TOOLINFO_FID_CACHE *lpToolinfoFc)
{
    lpToolinfo->cbSize = (*env)->GetIntField(env,lpObject,lpToolinfoFc->cbSize);
    lpToolinfo->uFlags = (*env)->GetIntField(env,lpObject,lpToolinfoFc->uFlags);
    lpToolinfo->hwnd = (HWND)(*env)->GetIntField(env,lpObject,lpToolinfoFc->hwnd);
    lpToolinfo->uId = (*env)->GetIntField(env,lpObject,lpToolinfoFc->uId);
    lpToolinfo->rect.left = (*env)->GetIntField(env,lpObject,lpToolinfoFc->left);
    lpToolinfo->rect.top = (*env)->GetIntField(env,lpObject,lpToolinfoFc->top);
    lpToolinfo->rect.right = (*env)->GetIntField(env,lpObject,lpToolinfoFc->right);
    lpToolinfo->rect.bottom = (*env)->GetIntField(env,lpObject,lpToolinfoFc->bottom);
    lpToolinfo->hinst = (HINSTANCE)(*env)->GetIntField(env,lpObject,lpToolinfoFc->hinst);
    lpToolinfo->lpszText = (LPTSTR)(*env)->GetIntField(env,lpObject,lpToolinfoFc->lpszText);
    lpToolinfo->lParam = (*env)->GetIntField(env,lpObject,lpToolinfoFc->lParam);
}

void setToolinfoFields(JNIEnv *env, jobject lpObject, TOOLINFO *lpToolinfo, TOOLINFO_FID_CACHE *lpToolinfoFc)
{
    (*env)->SetIntField(env,lpObject,lpToolinfoFc->cbSize, lpToolinfo->cbSize);
    (*env)->SetIntField(env,lpObject,lpToolinfoFc->uFlags, lpToolinfo->uFlags);
    (*env)->SetIntField(env,lpObject,lpToolinfoFc->hwnd, (jint)lpToolinfo->hwnd);
    (*env)->SetIntField(env,lpObject,lpToolinfoFc->uId, lpToolinfo->uId);
    (*env)->SetIntField(env,lpObject,lpToolinfoFc->left, lpToolinfo->rect.left);
    (*env)->SetIntField(env,lpObject,lpToolinfoFc->top, lpToolinfo->rect.top);
    (*env)->SetIntField(env,lpObject,lpToolinfoFc->right, lpToolinfo->rect.right);
    (*env)->SetIntField(env,lpObject,lpToolinfoFc->bottom, lpToolinfo->rect.bottom);
    (*env)->SetIntField(env,lpObject,lpToolinfoFc->hinst, (jint)lpToolinfo->hinst);
    (*env)->SetIntField(env,lpObject,lpToolinfoFc->lpszText, (jint)lpToolinfo->lpszText);
    (*env)->SetIntField(env,lpObject,lpToolinfoFc->lParam, lpToolinfo->lParam);
}

void cacheTrivertexFids(JNIEnv *env, jobject lpTrivertex, PTRIVERTEX_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->trivertexClass = (*env)->GetObjectClass(env,lpTrivertex);
    lpCache->x = (*env)->GetFieldID(env,lpCache->trivertexClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->trivertexClass,"y","I");
    lpCache->Red = (*env)->GetFieldID(env,lpCache->trivertexClass,"Red","S");
    lpCache->Green = (*env)->GetFieldID(env,lpCache->trivertexClass,"Green","S");
    lpCache->Blue = (*env)->GetFieldID(env,lpCache->trivertexClass,"Blue","S");
    lpCache->Alpha = (*env)->GetFieldID(env,lpCache->trivertexClass,"Alpha","S");
    lpCache->cached = 1;
}

void getTrivertexFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpTrivertex, TRIVERTEX_FID_CACHE *lpTrivertexFc)
{
	lpTrivertex->x = (*env)->GetIntField(env,lpObject,lpTrivertexFc->x);
	lpTrivertex->y = (*env)->GetIntField(env,lpObject,lpTrivertexFc->y);
	lpTrivertex->Red = (*env)->GetShortField(env,lpObject,lpTrivertexFc->Red);
	lpTrivertex->Green = (*env)->GetShortField(env,lpObject,lpTrivertexFc->Green);
	lpTrivertex->Blue = (*env)->GetShortField(env,lpObject,lpTrivertexFc->Blue);
	lpTrivertex->Alpha = (*env)->GetShortField(env,lpObject,lpTrivertexFc->Alpha);
}

void setTrivertexFields(JNIEnv *env, jobject lpObject, TRIVERTEX *lpTrivertex, TRIVERTEX_FID_CACHE *lpTrivertexFc)
{
    (*env)->SetIntField(env,lpObject,lpTrivertexFc->x, (jint)lpTrivertex->x);
    (*env)->SetIntField(env,lpObject,lpTrivertexFc->y, (jint)lpTrivertex->y);
    (*env)->SetShortField(env,lpObject,lpTrivertexFc->Red, (jshort)lpTrivertex->Red);
    (*env)->SetShortField(env,lpObject,lpTrivertexFc->Green, (jshort)lpTrivertex->Green);
    (*env)->SetShortField(env,lpObject,lpTrivertexFc->Blue, (jshort)lpTrivertex->Blue);
    (*env)->SetShortField(env,lpObject,lpTrivertexFc->Alpha, (jshort)lpTrivertex->Alpha);
}

void cacheWindowplacementFids(JNIEnv *env, jobject lpWindowplacement, PWINDOWPLACEMENT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->windowplacementClass = (*env)->GetObjectClass(env,lpWindowplacement);
    lpCache->length = (*env)->GetFieldID(env,lpCache->windowplacementClass,"length","I");
    lpCache->flags = (*env)->GetFieldID(env,lpCache->windowplacementClass,"flags","I");
    lpCache->showCmd = (*env)->GetFieldID(env,lpCache->windowplacementClass,"showCmd","I");
    lpCache->ptMinPosition_x = (*env)->GetFieldID(env,lpCache->windowplacementClass,"ptMinPosition_x","I");
    lpCache->ptMinPosition_y = (*env)->GetFieldID(env,lpCache->windowplacementClass,"ptMinPosition_y","I");
    lpCache->ptMaxPosition_x = (*env)->GetFieldID(env,lpCache->windowplacementClass,"ptMaxPosition_x","I");
    lpCache->ptMaxPosition_y = (*env)->GetFieldID(env,lpCache->windowplacementClass,"ptMaxPosition_y","I");
    lpCache->left = (*env)->GetFieldID(env,lpCache->windowplacementClass,"left","I");
    lpCache->top = (*env)->GetFieldID(env,lpCache->windowplacementClass,"top","I");
    lpCache->right = (*env)->GetFieldID(env,lpCache->windowplacementClass,"right","I");
    lpCache->bottom = (*env)->GetFieldID(env,lpCache->windowplacementClass,"bottom","I");
    lpCache->cached = 1;
}

void getWindowplacementFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpWindowplacement, WINDOWPLACEMENT_FID_CACHE *lpWindowplacementFc)
{
    lpWindowplacement->length = (*env)->GetIntField(env,lpObject,lpWindowplacementFc->length);
    lpWindowplacement->flags = (*env)->GetIntField(env,lpObject,lpWindowplacementFc->flags);
    lpWindowplacement->showCmd = (*env)->GetIntField(env,lpObject,lpWindowplacementFc->showCmd);
    lpWindowplacement->ptMinPosition.x = (*env)->GetIntField(env,lpObject,lpWindowplacementFc->ptMinPosition_x);
    lpWindowplacement->ptMinPosition.y = (*env)->GetIntField(env,lpObject,lpWindowplacementFc->ptMinPosition_y);
    lpWindowplacement->ptMaxPosition.x = (*env)->GetIntField(env,lpObject,lpWindowplacementFc->ptMaxPosition_x);
    lpWindowplacement->ptMaxPosition.y = (*env)->GetIntField(env,lpObject,lpWindowplacementFc->ptMaxPosition_y);
    lpWindowplacement->rcNormalPosition.left = (*env)->GetIntField(env,lpObject,lpWindowplacementFc->left);
    lpWindowplacement->rcNormalPosition.top = (*env)->GetIntField(env,lpObject,lpWindowplacementFc->top);
    lpWindowplacement->rcNormalPosition.right = (*env)->GetIntField(env,lpObject,lpWindowplacementFc->right);
    lpWindowplacement->rcNormalPosition.bottom = (*env)->GetIntField(env,lpObject,lpWindowplacementFc->bottom);
}

void setWindowplacementFields(JNIEnv *env, jobject lpObject, WINDOWPLACEMENT *lpWindowplacement, WINDOWPLACEMENT_FID_CACHE *lpWindowplacementFc)
{
    (*env)->SetIntField(env,lpObject,lpWindowplacementFc->length, lpWindowplacement->length);
    (*env)->SetIntField(env,lpObject,lpWindowplacementFc->flags, lpWindowplacement->flags);
    (*env)->SetIntField(env,lpObject,lpWindowplacementFc->showCmd, lpWindowplacement->showCmd);
    (*env)->SetIntField(env,lpObject,lpWindowplacementFc->ptMinPosition_x, lpWindowplacement->ptMinPosition.x);
    (*env)->SetIntField(env,lpObject,lpWindowplacementFc->ptMinPosition_y, lpWindowplacement->ptMinPosition.y);
    (*env)->SetIntField(env,lpObject,lpWindowplacementFc->ptMaxPosition_x, lpWindowplacement->ptMaxPosition.x);
    (*env)->SetIntField(env,lpObject,lpWindowplacementFc->ptMaxPosition_y, lpWindowplacement->ptMaxPosition.y);
    (*env)->SetIntField(env,lpObject,lpWindowplacementFc->left, lpWindowplacement->rcNormalPosition.left);
    (*env)->SetIntField(env,lpObject,lpWindowplacementFc->top, lpWindowplacement->rcNormalPosition.top);
    (*env)->SetIntField(env,lpObject,lpWindowplacementFc->right, lpWindowplacement->rcNormalPosition.right);
    (*env)->SetIntField(env,lpObject,lpWindowplacementFc->bottom, lpWindowplacement->rcNormalPosition.bottom);
}

void cacheDropfilesFids(JNIEnv *env, jobject lpDropfiles, PDROPFILES_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->dropfilesClass = (*env)->GetObjectClass(env,lpDropfiles);
    lpCache->pFiles = (*env)->GetFieldID(env,lpCache->dropfilesClass,"pFiles","I");
    lpCache->pt_x = (*env)->GetFieldID(env,lpCache->dropfilesClass,"pt_x","I");
    lpCache->pt_y = (*env)->GetFieldID(env,lpCache->dropfilesClass,"pt_y","I");
    lpCache->fNC = (*env)->GetFieldID(env,lpCache->dropfilesClass,"fNC","I");
    lpCache->fWide = (*env)->GetFieldID(env,lpCache->dropfilesClass,"fWide","I");
    lpCache->cached = 1;
}

void getDropfilesFields(JNIEnv *env, jobject lpObject, DROPFILES *lpDropfiles, DROPFILES_FID_CACHE *lpDropfilesFc)
{
    lpDropfiles->pFiles = (*env)->GetIntField(env,lpObject,lpDropfilesFc->pFiles);
    lpDropfiles->pt.x = (*env)->GetIntField(env,lpObject,lpDropfilesFc->pt_x);
    lpDropfiles->pt.y = (*env)->GetIntField(env,lpObject,lpDropfilesFc->pt_y);
    lpDropfiles->fNC = (*env)->GetIntField(env,lpObject,lpDropfilesFc->fNC);
    lpDropfiles->fWide = (*env)->GetIntField(env,lpObject,lpDropfilesFc->fWide);
}

void setDropfilesFields(JNIEnv *env, jobject lpObject, DROPFILES *lpDropfiles, DROPFILES_FID_CACHE *lpDropfilesFc)
{
    (*env)->SetIntField(env,lpObject,lpDropfilesFc->pFiles, (jint)lpDropfiles->pFiles);
    (*env)->SetIntField(env,lpObject,lpDropfilesFc->pt_x, lpDropfiles->pt.x);
    (*env)->SetIntField(env,lpObject,lpDropfilesFc->pt_y, lpDropfiles->pt.y);
    (*env)->SetIntField(env,lpObject,lpDropfilesFc->fNC, lpDropfiles->fNC);
    (*env)->SetIntField(env,lpObject,lpDropfilesFc->fWide, lpDropfiles->fWide);
}

void cacheOlecmdtextFids(JNIEnv *env, jobject lpOlecmdtext, POLECMDTEXT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->olecmdtextClass = (*env)->GetObjectClass(env,lpOlecmdtext);
    lpCache->cmdtextf = (*env)->GetFieldID(env,lpCache->olecmdtextClass,"cmdtextf","I");
    lpCache->cwActual = (*env)->GetFieldID(env,lpCache->olecmdtextClass,"cwActual","I");
    lpCache->cwBuf = (*env)->GetFieldID(env,lpCache->olecmdtextClass,"cwBuf","I");
    lpCache->rgwz = (*env)->GetFieldID(env,lpCache->olecmdtextClass,"rgwz","S");
    lpCache->cached = 1;
}

void getOlecmdtextFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpOlecmdtext, OLECMDTEXT_FID_CACHE *lpOlecmdtextFc)
{
    
    lpOlecmdtext->cmdtextf = (*env)->GetIntField(env,lpObject,lpOlecmdtextFc->cmdtextf);
    lpOlecmdtext->cwActual = (*env)->GetIntField(env,lpObject,lpOlecmdtextFc->cwActual);
    lpOlecmdtext->cwBuf = (*env)->GetIntField(env,lpObject,lpOlecmdtextFc->cwBuf);
    lpOlecmdtext->rgwz[0] = (*env)->GetShortField(env,lpObject,lpOlecmdtextFc->rgwz);;
}

void setOlecmdtextFields(JNIEnv *env, jobject lpObject, OLECMDTEXT *lpOlecmdtext, OLECMDTEXT_FID_CACHE *lpOlecmdtextFc)
{
    (*env)->SetIntField(env,lpObject,lpOlecmdtextFc->cmdtextf, lpOlecmdtext->cmdtextf);
    (*env)->SetIntField(env,lpObject,lpOlecmdtextFc->cwActual, lpOlecmdtext->cwActual);
    (*env)->SetIntField(env,lpObject,lpOlecmdtextFc->cwBuf, lpOlecmdtext->cwBuf);
    (*env)->SetShortField(env,lpObject,lpOlecmdtextFc->rgwz, lpOlecmdtext->rgwz[0]);
}

void cacheChoosefontFids(JNIEnv *env, jobject lpChoosefont, PCHOOSEFONT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->choosefontClass = (*env)->GetObjectClass(env,lpChoosefont);
    lpCache->lStructSize = (*env)->GetFieldID(env,lpCache->choosefontClass,"lStructSize","I");
    lpCache->hwndOwner = (*env)->GetFieldID(env,lpCache->choosefontClass,"hwndOwner","I");
    lpCache->hDC = (*env)->GetFieldID(env,lpCache->choosefontClass,"hDC","I");
    lpCache->lpLogFont = (*env)->GetFieldID(env,lpCache->choosefontClass,"lpLogFont","I");
    lpCache->iPointSize = (*env)->GetFieldID(env,lpCache->choosefontClass,"iPointSize","I");
    lpCache->Flags = (*env)->GetFieldID(env,lpCache->choosefontClass,"Flags","I");
    lpCache->rgbColors = (*env)->GetFieldID(env,lpCache->choosefontClass,"rgbColors","I");
    lpCache->lCustData = (*env)->GetFieldID(env,lpCache->choosefontClass,"lCustData","I");
    lpCache->lpfnHook = (*env)->GetFieldID(env,lpCache->choosefontClass,"lpfnHook","I");
    lpCache->lpTemplateName = (*env)->GetFieldID(env,lpCache->choosefontClass,"lpTemplateName","I");
    lpCache->hInstance = (*env)->GetFieldID(env,lpCache->choosefontClass,"hInstance","I");
    lpCache->lpszStyle = (*env)->GetFieldID(env,lpCache->choosefontClass,"lpszStyle","I");
    lpCache->nFontType = (*env)->GetFieldID(env,lpCache->choosefontClass,"nFontType","S");
    lpCache->___MISSING_ALIGNMENT__ = (*env)->GetFieldID(env,lpCache->choosefontClass,"___MISSING_ALIGNMENT__","S");
    lpCache->nSizeMin = (*env)->GetFieldID(env,lpCache->choosefontClass,"nSizeMin","I");
    lpCache->nSizeMax = (*env)->GetFieldID(env,lpCache->choosefontClass,"nSizeMax","I");
    lpCache->cached = 1;
}
void getChoosefontFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpChoosefont, CHOOSEFONT_FID_CACHE *lpChoosefontFc)
{
    lpChoosefont->lStructSize = (*env)->GetIntField(env,lpObject,lpChoosefontFc->lStructSize);
    lpChoosefont->hwndOwner = (HWND)(*env)->GetIntField(env,lpObject,lpChoosefontFc->hwndOwner);
    lpChoosefont->hDC = (HDC)(*env)->GetIntField(env,lpObject,lpChoosefontFc->hDC);
    lpChoosefont->lpLogFont = (LPLOGFONT)(*env)->GetIntField(env,lpObject,lpChoosefontFc->lpLogFont);
    lpChoosefont->iPointSize = (*env)->GetIntField(env,lpObject,lpChoosefontFc->iPointSize);
    lpChoosefont->Flags = (*env)->GetIntField(env,lpObject,lpChoosefontFc->Flags);
    lpChoosefont->rgbColors = (*env)->GetIntField(env,lpObject,lpChoosefontFc->rgbColors);
    lpChoosefont->lCustData = (*env)->GetIntField(env,lpObject,lpChoosefontFc->lCustData);
    lpChoosefont->lpfnHook = (LPCFHOOKPROC)(*env)->GetIntField(env,lpObject,lpChoosefontFc->lpfnHook);
    lpChoosefont->lpTemplateName = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpChoosefontFc->lpTemplateName);
    lpChoosefont->hInstance = (HINSTANCE)(*env)->GetIntField(env,lpObject,lpChoosefontFc->hInstance);
    lpChoosefont->lpszStyle = (LPTSTR)(*env)->GetIntField(env,lpObject,lpChoosefontFc->lpszStyle);
    lpChoosefont->nFontType = (*env)->GetShortField(env,lpObject,lpChoosefontFc->nFontType);
    lpChoosefont->___MISSING_ALIGNMENT__ = (*env)->GetShortField(env,lpObject,lpChoosefontFc->___MISSING_ALIGNMENT__);
    lpChoosefont->nSizeMin = (*env)->GetIntField(env,lpObject,lpChoosefontFc->nSizeMin);
    lpChoosefont->nSizeMax = (*env)->GetIntField(env,lpObject,lpChoosefontFc->nSizeMax);
}

void setChoosefontFields(JNIEnv *env, jobject lpObject, CHOOSEFONT *lpChoosefont, CHOOSEFONT_FID_CACHE *lpChoosefontFc)
{
    (*env)->SetIntField(env,lpObject,lpChoosefontFc->lStructSize, lpChoosefont->lStructSize);
    (*env)->SetIntField(env,lpObject,lpChoosefontFc->hwndOwner, (jint)lpChoosefont->hwndOwner);
    (*env)->SetIntField(env,lpObject,lpChoosefontFc->hDC, (jint)lpChoosefont->hDC);
    (*env)->SetIntField(env,lpObject,lpChoosefontFc->lpLogFont, (jint)lpChoosefont->lpLogFont);
    (*env)->SetIntField(env,lpObject,lpChoosefontFc->iPointSize, lpChoosefont->iPointSize);
    (*env)->SetIntField(env,lpObject,lpChoosefontFc->Flags, lpChoosefont->Flags);
    (*env)->SetIntField(env,lpObject,lpChoosefontFc->rgbColors, lpChoosefont->rgbColors);
    (*env)->SetIntField(env,lpObject,lpChoosefontFc->lCustData, lpChoosefont->lCustData);
    (*env)->SetIntField(env,lpObject,lpChoosefontFc->lpfnHook, (jint)lpChoosefont->lpfnHook);
    (*env)->SetIntField(env,lpObject,lpChoosefontFc->lpTemplateName, (jint)lpChoosefont->lpTemplateName);
    (*env)->SetIntField(env,lpObject,lpChoosefontFc->hInstance, (jint)lpChoosefont->hInstance);
    (*env)->SetIntField(env,lpObject,lpChoosefontFc->lpszStyle, (jint)lpChoosefont->lpszStyle);
    (*env)->SetShortField(env,lpObject,lpChoosefontFc->nFontType, lpChoosefont->nFontType);
    (*env)->SetShortField(env,lpObject,lpChoosefontFc->___MISSING_ALIGNMENT__, lpChoosefont->___MISSING_ALIGNMENT__);
    (*env)->SetIntField(env,lpObject,lpChoosefontFc->nSizeMin, lpChoosefont->nSizeMin);
    (*env)->SetIntField(env,lpObject,lpChoosefontFc->nSizeMax, lpChoosefont->nSizeMax);
}

void cacheOlecmdFids(JNIEnv *env, jobject lpOlecmd, POLECMD_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->olecmdClass = (*env)->GetObjectClass(env,lpOlecmd);
    lpCache->cmdID = (*env)->GetFieldID(env,lpCache->olecmdClass,"cmdID","I");
    lpCache->cmdf = (*env)->GetFieldID(env,lpCache->olecmdClass,"cmdf","I");
    lpCache->cached = 1;
}

void getOlecmdFields(JNIEnv *env, jobject lpObject, OLECMD *lpOlecmd, OLECMD_FID_CACHE *lpOlecmdFc)
{
    lpOlecmd->cmdID = (*env)->GetIntField(env,lpObject,lpOlecmdFc->cmdID);
    lpOlecmd->cmdf = (*env)->GetIntField(env,lpObject,lpOlecmdFc->cmdf);
}

void setOlecmdFields(JNIEnv *env, jobject lpObject, OLECMD *lpOlecmd, OLECMD_FID_CACHE *lpOlecmdFc)
{
    (*env)->SetIntField(env,lpObject,lpOlecmdFc->cmdID, lpOlecmd->cmdID);
    (*env)->SetIntField(env,lpObject,lpOlecmdFc->cmdf, lpOlecmd->cmdf);
}

void cacheGCP_RESULTSFids(JNIEnv *env, jobject lpObject, PGCP_RESULTS_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->nMaxFit = (*env)->GetFieldID(env, lpCache->clazz, "nMaxFit", "I");
	lpCache->nGlyphs = (*env)->GetFieldID(env, lpCache->clazz, "nGlyphs", "I");
	lpCache->lpGlyphs = (*env)->GetFieldID(env, lpCache->clazz, "lpGlyphs", "I");
	lpCache->lpClass = (*env)->GetFieldID(env, lpCache->clazz, "lpClass", "I");
	lpCache->lpCaretPos = (*env)->GetFieldID(env, lpCache->clazz, "lpCaretPos", "I");
	lpCache->lpDx = (*env)->GetFieldID(env, lpCache->clazz, "lpDx", "I");
	lpCache->lpOrder = (*env)->GetFieldID(env, lpCache->clazz, "lpOrder", "I");
	lpCache->lpOutString = (*env)->GetFieldID(env, lpCache->clazz, "lpOutString", "I");
	lpCache->lStructSize = (*env)->GetFieldID(env, lpCache->clazz, "lStructSize", "I");
	lpCache->cached = 1;
}

void getGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct, PGCP_RESULTS_FID_CACHE lpCache)
{
	lpStruct->nMaxFit = (*env)->GetIntField(env, lpObject, lpCache->nMaxFit);
	lpStruct->nGlyphs = (*env)->GetIntField(env, lpObject, lpCache->nGlyphs);
	lpStruct->lpGlyphs = (LPWSTR)(*env)->GetIntField(env, lpObject, lpCache->lpGlyphs);
	lpStruct->lpClass = (LPSTR)(*env)->GetIntField(env, lpObject, lpCache->lpClass);
	lpStruct->lpCaretPos = (int  *)(*env)->GetIntField(env, lpObject, lpCache->lpCaretPos);
	lpStruct->lpDx = (int  *)(*env)->GetIntField(env, lpObject, lpCache->lpDx);
	lpStruct->lpOrder = (UINT  *)(*env)->GetIntField(env, lpObject, lpCache->lpOrder);
	lpStruct->lpOutString = (LPTSTR)(*env)->GetIntField(env, lpObject, lpCache->lpOutString);
	lpStruct->lStructSize = (*env)->GetIntField(env, lpObject, lpCache->lStructSize);
}

void setGCP_RESULTSFields(JNIEnv *env, jobject lpObject, GCP_RESULTS *lpStruct, PGCP_RESULTS_FID_CACHE lpCache)
{
	(*env)->SetIntField(env, lpObject, lpCache->nMaxFit, lpStruct->nMaxFit);
	(*env)->SetIntField(env, lpObject, lpCache->nGlyphs, lpStruct->nGlyphs);
	(*env)->SetIntField(env, lpObject, lpCache->lpGlyphs, (int)lpStruct->lpGlyphs);
	(*env)->SetIntField(env, lpObject, lpCache->lpClass, (int)lpStruct->lpClass);
	(*env)->SetIntField(env, lpObject, lpCache->lpCaretPos, (int)lpStruct->lpCaretPos);
	(*env)->SetIntField(env, lpObject, lpCache->lpDx, (int)lpStruct->lpDx);
	(*env)->SetIntField(env, lpObject, lpCache->lpOrder, (int)lpStruct->lpOrder);
	(*env)->SetIntField(env, lpObject, lpCache->lpOutString, (int)lpStruct->lpOutString);
	(*env)->SetIntField(env, lpObject, lpCache->lStructSize, lpStruct->lStructSize);
}
#endif

void cacheAccelFids(JNIEnv *env, jobject lpAccel, PACCEL_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->accelClass = (*env)->GetObjectClass(env,lpAccel);
    lpCache->fVirt = (*env)->GetFieldID(env,lpCache->accelClass,"fVirt","B");
    lpCache->key = (*env)->GetFieldID(env,lpCache->accelClass,"key","S");
    lpCache->cmd = (*env)->GetFieldID(env,lpCache->accelClass,"cmd","S");
    lpCache->cached = 1;
}

void cacheBitmapFids(JNIEnv *env, jobject lpBitmap, PBITMAP_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->bitmapClass = (*env)->GetObjectClass(env,lpBitmap);
    lpCache->bmType = (*env)->GetFieldID(env,lpCache->bitmapClass,"bmType","I");
    lpCache->bmWidth = (*env)->GetFieldID(env,lpCache->bitmapClass,"bmWidth","I");
    lpCache->bmHeight = (*env)->GetFieldID(env,lpCache->bitmapClass,"bmHeight","I");
    lpCache->bmWidthBytes = (*env)->GetFieldID(env,lpCache->bitmapClass,"bmWidthBytes","I");
    lpCache->bmPlanes = (*env)->GetFieldID(env,lpCache->bitmapClass,"bmPlanes","S");
    lpCache->bmBitsPixel = (*env)->GetFieldID(env,lpCache->bitmapClass,"bmBitsPixel","S");
    lpCache->bmBits = (*env)->GetFieldID(env,lpCache->bitmapClass,"bmBits","I");
    lpCache->cached = 1;
}

void cacheBrowseinfoFids(JNIEnv *env, jobject lpBrowseinfo, PBROWSEINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->browseinfoClass = (*env)->GetObjectClass(env,lpBrowseinfo);
    lpCache->hwndOwner = (*env)->GetFieldID(env,lpCache->browseinfoClass,"hwndOwner","I");
    lpCache->pidlRoot = (*env)->GetFieldID(env,lpCache->browseinfoClass,"pidlRoot","I");
    lpCache->pszDisplayName = (*env)->GetFieldID(env,lpCache->browseinfoClass,"pszDisplayName","I");
    lpCache->lpszTitle = (*env)->GetFieldID(env,lpCache->browseinfoClass,"lpszTitle","I");
    lpCache->ulFlags = (*env)->GetFieldID(env,lpCache->browseinfoClass,"ulFlags","I");
    lpCache->lpfn = (*env)->GetFieldID(env,lpCache->browseinfoClass,"lpfn","I");
    lpCache->lParam = (*env)->GetFieldID(env,lpCache->browseinfoClass,"lParam","I");
    lpCache->iImage = (*env)->GetFieldID(env,lpCache->browseinfoClass,"iImage","I");
    lpCache->cached = 1;
}
/*
void cacheCharformatFids(JNIEnv *env, jobject lpCharformat, PCHARFORMAT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->charformatClass = (*env)->GetObjectClass(env,lpCharformat);
    lpCache->cbSize = (*env)->GetFieldID(env,lpCache->charformatClass,"cbSize","I");
    lpCache->dwMask = (*env)->GetFieldID(env,lpCache->charformatClass,"dwMask","I");
    lpCache->dwEffects = (*env)->GetFieldID(env,lpCache->charformatClass,"dwEffects","I");
    lpCache->yHeight = (*env)->GetFieldID(env,lpCache->charformatClass,"yHeight","I");
    lpCache->yOffset = (*env)->GetFieldID(env,lpCache->charformatClass,"yOffset","I");
    lpCache->crTextColor = (*env)->GetFieldID(env,lpCache->charformatClass,"crTextColor","I");
    lpCache->bCharSet = (*env)->GetFieldID(env,lpCache->charformatClass,"bCharSet","B");
    lpCache->bPitchAndFamily = (*env)->GetFieldID(env,lpCache->charformatClass,"bPitchAndFamily","B");
    lpCache->szFaceName0 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName0","B");
    lpCache->szFaceName1 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName1","B");
    lpCache->szFaceName2 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName2","B");
    lpCache->szFaceName3 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName3","B");
    lpCache->szFaceName4 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName4","B");
    lpCache->szFaceName5 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName5","B");
    lpCache->szFaceName6 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName6","B");
    lpCache->szFaceName7 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName7","B");
    lpCache->szFaceName8 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName8","B");
    lpCache->szFaceName9 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName9","B");
    lpCache->szFaceName10 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName10","B");
    lpCache->szFaceName11 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName11","B");
    lpCache->szFaceName12 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName12","B");
    lpCache->szFaceName13 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName13","B");
    lpCache->szFaceName14 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName14","B");
    lpCache->szFaceName15 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName15","B");
    lpCache->szFaceName16 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName16","B");
    lpCache->szFaceName17 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName17","B");
    lpCache->szFaceName18 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName18","B");
    lpCache->szFaceName19 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName19","B");
    lpCache->szFaceName20 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName20","B");
    lpCache->szFaceName21 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName21","B");
    lpCache->szFaceName22 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName22","B");
    lpCache->szFaceName23 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName23","B");
    lpCache->szFaceName24 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName24","B");
    lpCache->szFaceName25 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName25","B");
    lpCache->szFaceName26 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName26","B");
    lpCache->szFaceName27 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName27","B");
    lpCache->szFaceName28 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName28","B");
    lpCache->szFaceName29 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName29","B");
    lpCache->szFaceName30 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName30","B");
    lpCache->szFaceName31 = (*env)->GetFieldID(env,lpCache->charformatClass,"szFaceName31","B");
    lpCache->cached = 1;
}

void cacheCharformat2Fids(JNIEnv *env, jobject lpCharformat2, PCHARFORMAT2_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->charformat2Class = (*env)->GetObjectClass(env,lpCharformat2);
    lpCache->cbSize = (*env)->GetFieldID(env,lpCache->charformat2Class,"cbSize","I");
    lpCache->dwMask = (*env)->GetFieldID(env,lpCache->charformat2Class,"dwMask","I");
    lpCache->dwEffects = (*env)->GetFieldID(env,lpCache->charformat2Class,"dwEffects","I");
    lpCache->yHeight = (*env)->GetFieldID(env,lpCache->charformat2Class,"yHeight","I");
    lpCache->yOffset = (*env)->GetFieldID(env,lpCache->charformat2Class,"yOffset","I");
    lpCache->crTextColor = (*env)->GetFieldID(env,lpCache->charformat2Class,"crTextColor","I");
    lpCache->bCharSet = (*env)->GetFieldID(env,lpCache->charformat2Class,"bCharSet","B");
    lpCache->bPitchAndFamily = (*env)->GetFieldID(env,lpCache->charformat2Class,"bPitchAndFamily","B");
    lpCache->szFaceName0 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName0","B");
    lpCache->szFaceName1 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName1","B");
    lpCache->szFaceName2 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName2","B");
    lpCache->szFaceName3 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName3","B");
    lpCache->szFaceName4 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName4","B");
    lpCache->szFaceName5 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName5","B");
    lpCache->szFaceName6 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName6","B");
    lpCache->szFaceName7 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName7","B");
    lpCache->szFaceName8 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName8","B");
    lpCache->szFaceName9 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName9","B");
    lpCache->szFaceName10 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName10","B");
    lpCache->szFaceName11 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName11","B");
    lpCache->szFaceName12 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName12","B");
    lpCache->szFaceName13 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName13","B");
    lpCache->szFaceName14 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName14","B");
    lpCache->szFaceName15 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName15","B");
    lpCache->szFaceName16 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName16","B");
    lpCache->szFaceName17 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName17","B");
    lpCache->szFaceName18 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName18","B");
    lpCache->szFaceName19 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName19","B");
    lpCache->szFaceName20 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName20","B");
    lpCache->szFaceName21 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName21","B");
    lpCache->szFaceName22 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName22","B");
    lpCache->szFaceName23 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName23","B");
    lpCache->szFaceName24 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName24","B");
    lpCache->szFaceName25 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName25","B");
    lpCache->szFaceName26 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName26","B");
    lpCache->szFaceName27 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName27","B");
    lpCache->szFaceName28 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName28","B");
    lpCache->szFaceName29 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName29","B");
    lpCache->szFaceName30 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName30","B");
    lpCache->szFaceName31 = (*env)->GetFieldID(env,lpCache->charformat2Class,"szFaceName31","B");
    lpCache->wWeight = (*env)->GetFieldID(env,lpCache->charformat2Class,"wWeight","S");
    lpCache->sSpacing = (*env)->GetFieldID(env,lpCache->charformat2Class,"sSpacing","S");
    lpCache->crBackColor = (*env)->GetFieldID(env,lpCache->charformat2Class,"crBackColor","I");
    lpCache->lcid = (*env)->GetFieldID(env,lpCache->charformat2Class,"lcid","I");
    lpCache->dwReserved = (*env)->GetFieldID(env,lpCache->charformat2Class,"dwReserved","I");
    lpCache->sStyle = (*env)->GetFieldID(env,lpCache->charformat2Class,"sStyle","S");
    lpCache->wKerning = (*env)->GetFieldID(env,lpCache->charformat2Class,"wKerning","S");
    lpCache->bUnderlineType = (*env)->GetFieldID(env,lpCache->charformat2Class,"bUnderlineType","B");
    lpCache->bAnimation = (*env)->GetFieldID(env,lpCache->charformat2Class,"bAnimation","B");
    lpCache->bRevAuthor = (*env)->GetFieldID(env,lpCache->charformat2Class,"bRevAuthor","B");
    lpCache->bReserved1 = (*env)->GetFieldID(env,lpCache->charformat2Class,"bReserved1","B");
    lpCache->cached = 1;
}
*/
void cacheChoosecolorFids(JNIEnv *env, jobject lpChoosecolor, PCHOOSECOLOR_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->choosecolorClass = (*env)->GetObjectClass(env,lpChoosecolor);
    lpCache->lStructSize = (*env)->GetFieldID(env,lpCache->choosecolorClass,"lStructSize","I");
    lpCache->hwndOwner = (*env)->GetFieldID(env,lpCache->choosecolorClass,"hwndOwner","I");
    lpCache->hInstance = (*env)->GetFieldID(env,lpCache->choosecolorClass,"hInstance","I");
    lpCache->rgbResult = (*env)->GetFieldID(env,lpCache->choosecolorClass,"rgbResult","I");
    lpCache->lpCustColors = (*env)->GetFieldID(env,lpCache->choosecolorClass,"lpCustColors","I");
    lpCache->Flags = (*env)->GetFieldID(env,lpCache->choosecolorClass,"Flags","I");
    lpCache->lCustData = (*env)->GetFieldID(env,lpCache->choosecolorClass,"lCustData","I");
    lpCache->lpfnHook = (*env)->GetFieldID(env,lpCache->choosecolorClass,"lpfnHook","I");
    lpCache->lpTemplateName = (*env)->GetFieldID(env,lpCache->choosecolorClass,"lpTemplateName","I");
    lpCache->cached = 1;
}

void cacheCompositionformFids(JNIEnv *env, jobject lpCompositionform, PCOMPOSITIONFORM_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->compositionformClass = (*env)->GetObjectClass(env,lpCompositionform);
    lpCache->dwStyle = (*env)->GetFieldID(env,lpCache->compositionformClass,"dwStyle","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->compositionformClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->compositionformClass,"y","I");
    lpCache->left = (*env)->GetFieldID(env,lpCache->compositionformClass,"left","I");
    lpCache->top = (*env)->GetFieldID(env,lpCache->compositionformClass,"top","I");
    lpCache->right = (*env)->GetFieldID(env,lpCache->compositionformClass,"right","I");
    lpCache->bottom = (*env)->GetFieldID(env,lpCache->compositionformClass,"bottom","I");
    lpCache->cached = 1;
}

void cacheCreatestructFids(JNIEnv *env, jobject lpCreatestruct, PCREATESTRUCT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->createstructClass = (*env)->GetObjectClass(env,lpCreatestruct);
    lpCache->lpCreateParams = (*env)->GetFieldID(env,lpCache->createstructClass,"lpCreateParams","I");
    lpCache->hInstance = (*env)->GetFieldID(env,lpCache->createstructClass,"hInstance","I");
    lpCache->hMenu = (*env)->GetFieldID(env,lpCache->createstructClass,"hMenu","I");
    lpCache->hwndParent = (*env)->GetFieldID(env,lpCache->createstructClass,"hwndParent","I");
    lpCache->cx = (*env)->GetFieldID(env,lpCache->createstructClass,"cx","I");
    lpCache->cy = (*env)->GetFieldID(env,lpCache->createstructClass,"cy","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->createstructClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->createstructClass,"y","I");
    lpCache->style = (*env)->GetFieldID(env,lpCache->createstructClass,"style","I");
    lpCache->lpszName = (*env)->GetFieldID(env,lpCache->createstructClass,"lpszName","I");
    lpCache->lpszClass = (*env)->GetFieldID(env,lpCache->createstructClass,"lpszClass","I");
    lpCache->dwExStyle = (*env)->GetFieldID(env,lpCache->createstructClass,"dwExStyle","I");
    lpCache->cached = 1;
}

void cacheDibsectionFids(JNIEnv *env, jobject lpDibsection, PDIBSECTION_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->dibsectionClass = (*env)->GetObjectClass(env,lpDibsection);
    lpCache->bmType = (*env)->GetFieldID(env,lpCache->dibsectionClass,"bmType","I");
    lpCache->bmWidth = (*env)->GetFieldID(env,lpCache->dibsectionClass,"bmWidth","I");
    lpCache->bmHeight = (*env)->GetFieldID(env,lpCache->dibsectionClass,"bmHeight","I");
    lpCache->bmWidthBytes = (*env)->GetFieldID(env,lpCache->dibsectionClass,"bmWidthBytes","I");
    lpCache->bmPlanes = (*env)->GetFieldID(env,lpCache->dibsectionClass,"bmPlanes","S");
    lpCache->bmBitsPixel = (*env)->GetFieldID(env,lpCache->dibsectionClass,"bmBitsPixel","S");
    lpCache->bmBits = (*env)->GetFieldID(env,lpCache->dibsectionClass,"bmBits","I");
    lpCache->biSize = (*env)->GetFieldID(env,lpCache->dibsectionClass,"biSize","I");
    lpCache->biWidth = (*env)->GetFieldID(env,lpCache->dibsectionClass,"biWidth","I");
    lpCache->biHeight = (*env)->GetFieldID(env,lpCache->dibsectionClass,"biHeight","I");
    lpCache->biPlanes = (*env)->GetFieldID(env,lpCache->dibsectionClass,"biPlanes","S");
    lpCache->biBitCount = (*env)->GetFieldID(env,lpCache->dibsectionClass,"biBitCount","S");
    lpCache->biCompression = (*env)->GetFieldID(env,lpCache->dibsectionClass,"biCompression","I");
    lpCache->biSizeImage = (*env)->GetFieldID(env,lpCache->dibsectionClass,"biSizeImage","I");
    lpCache->biXPelsPerMeter = (*env)->GetFieldID(env,lpCache->dibsectionClass,"biXPelsPerMeter","I");
    lpCache->biYPelsPerMeter = (*env)->GetFieldID(env,lpCache->dibsectionClass,"biYPelsPerMeter","I");
    lpCache->biClrUsed = (*env)->GetFieldID(env,lpCache->dibsectionClass,"biClrUsed","I");
    lpCache->biClrImportant = (*env)->GetFieldID(env,lpCache->dibsectionClass,"biClrImportant","I");
    lpCache->dsBitfields0 = (*env)->GetFieldID(env,lpCache->dibsectionClass,"dsBitfields0","I");
    lpCache->dsBitfields1 = (*env)->GetFieldID(env,lpCache->dibsectionClass,"dsBitfields1","I");
    lpCache->dsBitfields2 = (*env)->GetFieldID(env,lpCache->dibsectionClass,"dsBitfields2","I");
    lpCache->dshSection = (*env)->GetFieldID(env,lpCache->dibsectionClass,"dshSection","I");
    lpCache->dsOffset = (*env)->GetFieldID(env,lpCache->dibsectionClass,"dsOffset","I");
    lpCache->cached = 1;
}

void cacheDllversioninfoFids(JNIEnv *env, jobject lpDllversioninfo, PDLLVERSIONINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->dllversioninfoClass = (*env)->GetObjectClass(env,lpDllversioninfo);
    lpCache->cbSize = (*env)->GetFieldID(env,lpCache->dllversioninfoClass,"cbSize","I");
    lpCache->dwMajorVersion = (*env)->GetFieldID(env,lpCache->dllversioninfoClass,"dwMajorVersion","I");
    lpCache->dwMinorVersion = (*env)->GetFieldID(env,lpCache->dllversioninfoClass,"dwMinorVersion","I");
    lpCache->dwBuildNumber = (*env)->GetFieldID(env,lpCache->dllversioninfoClass,"dwBuildNumber","I");
    lpCache->dwPlatformID = (*env)->GetFieldID(env,lpCache->dllversioninfoClass,"dwPlatformID","I");
    lpCache->cached = 1;
}

void cacheDrawitemstructFids(JNIEnv *env, jobject lpDrawitemstruct, PDRAWITEMSTRUCT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->drawitemstructClass = (*env)->GetObjectClass(env,lpDrawitemstruct);
    lpCache->CtlType = (*env)->GetFieldID(env,lpCache->drawitemstructClass,"CtlType","I");
    lpCache->CtlID = (*env)->GetFieldID(env,lpCache->drawitemstructClass,"CtlID","I");
    lpCache->itemID = (*env)->GetFieldID(env,lpCache->drawitemstructClass,"itemID","I");
    lpCache->itemAction = (*env)->GetFieldID(env,lpCache->drawitemstructClass,"itemAction","I");
    lpCache->itemState = (*env)->GetFieldID(env,lpCache->drawitemstructClass,"itemState","I");
    lpCache->hwndItem = (*env)->GetFieldID(env,lpCache->drawitemstructClass,"hwndItem","I");
    lpCache->hDC = (*env)->GetFieldID(env,lpCache->drawitemstructClass,"hDC","I");
    lpCache->left = (*env)->GetFieldID(env,lpCache->drawitemstructClass,"left","I");
    lpCache->top = (*env)->GetFieldID(env,lpCache->drawitemstructClass,"top","I");
    lpCache->right = (*env)->GetFieldID(env,lpCache->drawitemstructClass,"right","I");
    lpCache->bottom = (*env)->GetFieldID(env,lpCache->drawitemstructClass,"bottom","I");
    lpCache->itemData = (*env)->GetFieldID(env,lpCache->drawitemstructClass,"itemData","I");
    lpCache->cached = 1;
}

void cacheHditemFids(JNIEnv *env, jobject lpHditem, PHDITEM_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->hditemClass = (*env)->GetObjectClass(env,lpHditem);
    lpCache->mask = (*env)->GetFieldID(env,lpCache->hditemClass,"mask","I");
    lpCache->cxy = (*env)->GetFieldID(env,lpCache->hditemClass,"cxy","I");
    lpCache->pszText = (*env)->GetFieldID(env,lpCache->hditemClass,"pszText","I");
    lpCache->hbm = (*env)->GetFieldID(env,lpCache->hditemClass,"hbm","I");
    lpCache->cchTextMax = (*env)->GetFieldID(env,lpCache->hditemClass,"cchTextMax","I");
    lpCache->fmt = (*env)->GetFieldID(env,lpCache->hditemClass,"fmt","I");
    lpCache->lParam = (*env)->GetFieldID(env,lpCache->hditemClass,"lParam","I");
    lpCache->iImage = (*env)->GetFieldID(env,lpCache->hditemClass,"iImage","I");
    lpCache->iOrder = (*env)->GetFieldID(env,lpCache->hditemClass,"iOrder","I");
    lpCache->cached = 1;
}

void cacheHdlayoutFids(JNIEnv *env, jobject lpHdlayout, PHDLAYOUT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->hdlayoutClass = (*env)->GetObjectClass(env,lpHdlayout);
    lpCache->prc = (*env)->GetFieldID(env,lpCache->hdlayoutClass,"prc","I");
    lpCache->pwpos = (*env)->GetFieldID(env,lpCache->hdlayoutClass,"pwpos","I");
    lpCache->cached = 1;
}

void cacheIconinfoFids(JNIEnv *env, jobject lpIconinfo, PICONINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->iconinfoClass = (*env)->GetObjectClass(env,lpIconinfo);
    lpCache->fIcon = (*env)->GetFieldID(env,lpCache->iconinfoClass,"fIcon","Z");
    lpCache->xHotspot = (*env)->GetFieldID(env,lpCache->iconinfoClass,"xHotspot","I");
    lpCache->yHotspot = (*env)->GetFieldID(env,lpCache->iconinfoClass,"yHotspot","I");
    lpCache->hbmMask = (*env)->GetFieldID(env,lpCache->iconinfoClass,"hbmMask","I");
    lpCache->hbmColor = (*env)->GetFieldID(env,lpCache->iconinfoClass,"hbmColor","I");
    lpCache->cached = 1;
}

void cacheInitcommoncontrolsexFids(JNIEnv *env, jobject lpInitcommoncontrolsex, PINITCOMMONCONTROLSEX_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->initcommoncontrolsexClass = (*env)->GetObjectClass(env,lpInitcommoncontrolsex);
    lpCache->dwSize = (*env)->GetFieldID(env,lpCache->initcommoncontrolsexClass,"dwSize","I");
    lpCache->dwICC = (*env)->GetFieldID(env,lpCache->initcommoncontrolsexClass,"dwICC","I");
    lpCache->cached = 1;
}

void cacheLogbrushFids(JNIEnv *env, jobject lpLogbrush, PLOGBRUSH_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->logbrushClass = (*env)->GetObjectClass(env,lpLogbrush);
    lpCache->lbStyle = (*env)->GetFieldID(env,lpCache->logbrushClass,"lbStyle","I");
    lpCache->lbColor = (*env)->GetFieldID(env,lpCache->logbrushClass,"lbColor","I");
    lpCache->lbHatch = (*env)->GetFieldID(env,lpCache->logbrushClass,"lbHatch","I");
    lpCache->cached = 1;
}

void cacheLogfontFids(JNIEnv *env, jobject lpLogfont, PLOGFONT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->logfontClass = (*env)->GetObjectClass(env,lpLogfont);
    lpCache->lfHeight = (*env)->GetFieldID(env,lpCache->logfontClass,"lfHeight","I");
    lpCache->lfWidth = (*env)->GetFieldID(env,lpCache->logfontClass,"lfWidth","I");
    lpCache->lfEscapement = (*env)->GetFieldID(env,lpCache->logfontClass,"lfEscapement","I");
    lpCache->lfOrientation = (*env)->GetFieldID(env,lpCache->logfontClass,"lfOrientation","I");
    lpCache->lfWeight = (*env)->GetFieldID(env,lpCache->logfontClass,"lfWeight","I");
    lpCache->lfItalic = (*env)->GetFieldID(env,lpCache->logfontClass,"lfItalic","B");
    lpCache->lfUnderline = (*env)->GetFieldID(env,lpCache->logfontClass,"lfUnderline","B");
    lpCache->lfStrikeOut = (*env)->GetFieldID(env,lpCache->logfontClass,"lfStrikeOut","B");
    lpCache->lfCharSet = (*env)->GetFieldID(env,lpCache->logfontClass,"lfCharSet","B");
    lpCache->lfOutPrecision = (*env)->GetFieldID(env,lpCache->logfontClass,"lfOutPrecision","B");
    lpCache->lfClipPrecision = (*env)->GetFieldID(env,lpCache->logfontClass,"lfClipPrecision","B");
    lpCache->lfQuality = (*env)->GetFieldID(env,lpCache->logfontClass,"lfQuality","B");
    lpCache->lfPitchAndFamily = (*env)->GetFieldID(env,lpCache->logfontClass,"lfPitchAndFamily","B");
    /* the calling function must get these byte array elements */
    lpCache->lfFaceName0 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName0","C");
    lpCache->lfFaceName1 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName1","C");
    lpCache->lfFaceName2 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName2","C");
    lpCache->lfFaceName3 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName3","C");
    lpCache->lfFaceName4 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName4","C");
    lpCache->lfFaceName5 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName5","C");
    lpCache->lfFaceName6 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName6","C");
    lpCache->lfFaceName7 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName7","C");
    lpCache->lfFaceName8 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName8","C");
    lpCache->lfFaceName9 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName9","C");
    lpCache->lfFaceName10 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName10","C");
    lpCache->lfFaceName11 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName11","C");
    lpCache->lfFaceName12 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName12","C");
    lpCache->lfFaceName13 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName13","C");
    lpCache->lfFaceName14 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName14","C");
    lpCache->lfFaceName15 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName15","C");
    lpCache->lfFaceName16 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName16","C");
    lpCache->lfFaceName17 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName17","C");
    lpCache->lfFaceName18 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName18","C");
    lpCache->lfFaceName19 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName19","C");
    lpCache->lfFaceName20 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName20","C");
    lpCache->lfFaceName21 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName21","C");
    lpCache->lfFaceName22 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName22","C");
    lpCache->lfFaceName23 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName23","C");
    lpCache->lfFaceName24 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName24","C");
    lpCache->lfFaceName25 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName25","C");
    lpCache->lfFaceName26 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName26","C");
    lpCache->lfFaceName27 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName27","C");
    lpCache->lfFaceName28 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName28","C");
    lpCache->lfFaceName29 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName29","C");
    lpCache->lfFaceName30 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName30","C");
    lpCache->lfFaceName31 = (*env)->GetFieldID(env,lpCache->logfontClass,"lfFaceName31","C");
    lpCache->cached = 1;
}

void cacheLogpenFids(JNIEnv *env, jobject lpLogpen, PLOGPEN_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->logpenClass = (*env)->GetObjectClass(env,lpLogpen);
    lpCache->lopnStyle = (*env)->GetFieldID(env,lpCache->logpenClass,"lopnStyle","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->logpenClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->logpenClass,"y","I");
    lpCache->lopnColor = (*env)->GetFieldID(env,lpCache->logpenClass,"lopnColor","I");
    lpCache->cached = 1;
}
/*
void cacheExtlogpenFids(JNIEnv *env, jobject lpExtlogpen, PEXTLOGPEN_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->extlogpenClass = (*env)->GetObjectClass(env,lpExtlogpen);
    lpCache->elpPenStyle = (*env)->GetFieldID(env,lpCache->extlogpenClass,"elpPenStyle","I");
    lpCache->elpWidth = (*env)->GetFieldID(env,lpCache->extlogpenClass,"elpWidth","I");
    lpCache->elpBrushStyle = (*env)->GetFieldID(env,lpCache->extlogpenClass,"elpBrushStyle","I");
    lpCache->elpColor = (*env)->GetFieldID(env,lpCache->extlogpenClass,"elpColor","I");
    lpCache->elpHatch = (*env)->GetFieldID(env,lpCache->extlogpenClass,"elpHatch","I");
    lpCache->elpNumEntries = (*env)->GetFieldID(env,lpCache->extlogpenClass,"elpNumEntries","I"); 
	lpCache->cached = 1;
}
*/
void cacheLvcolumnFids(JNIEnv *env, jobject lpLvcolumn, PLVCOLUMN_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->lvcolumnClass = (*env)->GetObjectClass(env,lpLvcolumn);
    lpCache->mask = (*env)->GetFieldID(env,lpCache->lvcolumnClass,"mask","I");
    lpCache->fmt = (*env)->GetFieldID(env,lpCache->lvcolumnClass,"fmt","I");
    lpCache->cx = (*env)->GetFieldID(env,lpCache->lvcolumnClass,"cx","I");
    lpCache->pszText = (*env)->GetFieldID(env,lpCache->lvcolumnClass,"pszText","I");
    lpCache->cchTextMax = (*env)->GetFieldID(env,lpCache->lvcolumnClass,"cchTextMax","I");
    lpCache->iSubItem = (*env)->GetFieldID(env,lpCache->lvcolumnClass,"iSubItem","I");
    lpCache->iImage = (*env)->GetFieldID(env,lpCache->lvcolumnClass,"iImage","I");
    lpCache->iOrder = (*env)->GetFieldID(env,lpCache->lvcolumnClass,"iOrder","I");
    lpCache->cached = 1;
}

void cacheLvhittestinfoFids(JNIEnv *env, jobject lpLvhittestinfo, PLVHITTESTINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->lvhittestinfoClass = (*env)->GetObjectClass(env,lpLvhittestinfo);
    lpCache->x = (*env)->GetFieldID(env,lpCache->lvhittestinfoClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->lvhittestinfoClass,"y","I");
    lpCache->flags = (*env)->GetFieldID(env,lpCache->lvhittestinfoClass,"flags","I");
    lpCache->iItem = (*env)->GetFieldID(env,lpCache->lvhittestinfoClass,"iItem","I");
    lpCache->iSubItem = (*env)->GetFieldID(env,lpCache->lvhittestinfoClass,"iSubItem","I");
    lpCache->cached = 1;
}

void cacheLvitemFids(JNIEnv *env, jobject lpLvitem, PLVITEM_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->lvitemClass = (*env)->GetObjectClass(env,lpLvitem);
    lpCache->mask = (*env)->GetFieldID(env,lpCache->lvitemClass,"mask","I");
    lpCache->iItem = (*env)->GetFieldID(env,lpCache->lvitemClass,"iItem","I");
    lpCache->iSubItem = (*env)->GetFieldID(env,lpCache->lvitemClass,"iSubItem","I");
    lpCache->state = (*env)->GetFieldID(env,lpCache->lvitemClass,"state","I");
    lpCache->stateMask = (*env)->GetFieldID(env,lpCache->lvitemClass,"stateMask","I");
    lpCache->pszText = (*env)->GetFieldID(env,lpCache->lvitemClass,"pszText","I");
    lpCache->cchTextMax = (*env)->GetFieldID(env,lpCache->lvitemClass,"cchTextMax","I");
    lpCache->iImage = (*env)->GetFieldID(env,lpCache->lvitemClass,"iImage","I");
    lpCache->lParam = (*env)->GetFieldID(env,lpCache->lvitemClass,"lParam","I");
    lpCache->iIndent = (*env)->GetFieldID(env,lpCache->lvitemClass,"iIndent","I");
    lpCache->cached = 1;
}

void cacheMeasureitemstructFids(JNIEnv *env, jobject lpMeasureitemstruct, PMEASUREITEMSTRUCT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->measureitemstructClass = (*env)->GetObjectClass(env,lpMeasureitemstruct);
    lpCache->CtlType = (*env)->GetFieldID(env,lpCache->measureitemstructClass,"CtlType","I");
    lpCache->CtlID = (*env)->GetFieldID(env,lpCache->measureitemstructClass,"CtlID","I");
    lpCache->itemID = (*env)->GetFieldID(env,lpCache->measureitemstructClass,"itemID","I");
    lpCache->itemWidth = (*env)->GetFieldID(env,lpCache->measureitemstructClass,"itemWidth","I");
    lpCache->itemHeight = (*env)->GetFieldID(env,lpCache->measureitemstructClass,"itemHeight","I");
    lpCache->itemData = (*env)->GetFieldID(env,lpCache->measureitemstructClass,"itemData","I");
    lpCache->cached = 1;
}

void cacheMenuiteminfoFids(JNIEnv *env, jobject lpMenuiteminfo, PMENUITEMINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->menuiteminfoClass = (*env)->GetObjectClass(env,lpMenuiteminfo);
    lpCache->cbSize = (*env)->GetFieldID(env,lpCache->menuiteminfoClass,"cbSize","I");
    lpCache->fMask = (*env)->GetFieldID(env,lpCache->menuiteminfoClass,"fMask","I");
    lpCache->fType = (*env)->GetFieldID(env,lpCache->menuiteminfoClass,"fType","I");
    lpCache->fState = (*env)->GetFieldID(env,lpCache->menuiteminfoClass,"fState","I");
    lpCache->wID = (*env)->GetFieldID(env,lpCache->menuiteminfoClass,"wID","I");
    lpCache->hSubMenu = (*env)->GetFieldID(env,lpCache->menuiteminfoClass,"hSubMenu","I");
    lpCache->hbmpChecked = (*env)->GetFieldID(env,lpCache->menuiteminfoClass,"hbmpChecked","I");
    lpCache->hbmpUnchecked = (*env)->GetFieldID(env,lpCache->menuiteminfoClass,"hbmpUnchecked","I");
    lpCache->dwItemData = (*env)->GetFieldID(env,lpCache->menuiteminfoClass,"dwItemData","I");
    lpCache->dwTypeData = (*env)->GetFieldID(env,lpCache->menuiteminfoClass,"dwTypeData","I");
    lpCache->cch = (*env)->GetFieldID(env,lpCache->menuiteminfoClass,"cch", "I");
#ifdef USE_2000_CALLS
#ifndef _WIN32_WCE
    lpCache->hbmpItem = (*env)->GetFieldID(env,lpCache->menuiteminfoClass,"hbmpItem", "I");
#endif
#endif
    lpCache->cached = 1;
}

void cacheMsgFids(JNIEnv *env, jobject lpMsg, PMSG_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->msgClass = (*env)->GetObjectClass(env,lpMsg);
    lpCache->hwnd = (*env)->GetFieldID(env,lpCache->msgClass,"hwnd","I");
    lpCache->message = (*env)->GetFieldID(env,lpCache->msgClass,"message","I");
    lpCache->wParam = (*env)->GetFieldID(env,lpCache->msgClass,"wParam","I");
    lpCache->lParam = (*env)->GetFieldID(env,lpCache->msgClass,"lParam","I");
    lpCache->time = (*env)->GetFieldID(env,lpCache->msgClass,"time","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->msgClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->msgClass,"y","I");
    lpCache->cached = 1;
}

void cacheNmhdrFids(JNIEnv *env, jobject lpNmhdr, PNMHDR_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->nmhdrClass = (*env)->GetObjectClass(env,lpNmhdr);
    lpCache->hwndFrom = (*env)->GetFieldID(env,lpCache->nmhdrClass,"hwndFrom","I");
    lpCache->idFrom = (*env)->GetFieldID(env,lpCache->nmhdrClass,"idFrom","I");
    lpCache->code = (*env)->GetFieldID(env,lpCache->nmhdrClass,"code","I");
    lpCache->cached = 1;
}

void cacheNmheaderFids(JNIEnv *env, jobject lpNmheader, PNMHEADER_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->nmheaderClass = (*env)->GetObjectClass(env,lpNmheader);
    lpCache->hwndFrom = (*env)->GetFieldID(env,lpCache->nmheaderClass,"hwndFrom","I");
    lpCache->idFrom = (*env)->GetFieldID(env,lpCache->nmheaderClass,"idFrom","I");
    lpCache->code = (*env)->GetFieldID(env,lpCache->nmheaderClass,"code","I");
    lpCache->iItem = (*env)->GetFieldID(env,lpCache->nmheaderClass,"iItem","I");
    lpCache->iButton = (*env)->GetFieldID(env,lpCache->nmheaderClass,"iButton","I");
    lpCache->pitem = (*env)->GetFieldID(env,lpCache->nmheaderClass,"pitem","I");
    lpCache->cached = 1;
}

void cacheNmlistviewFids(JNIEnv *env, jobject lpNmlistview, PNMLISTVIEW_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->nmlistviewClass = (*env)->GetObjectClass(env,lpNmlistview);
    lpCache->hwndFrom = (*env)->GetFieldID(env,lpCache->nmlistviewClass,"hwndFrom","I");
    lpCache->idFrom = (*env)->GetFieldID(env,lpCache->nmlistviewClass,"idFrom","I");
    lpCache->code = (*env)->GetFieldID(env,lpCache->nmlistviewClass,"code","I");
    lpCache->iItem = (*env)->GetFieldID(env,lpCache->nmlistviewClass,"iItem","I");
    lpCache->iSubItem = (*env)->GetFieldID(env,lpCache->nmlistviewClass,"iSubItem","I");
    lpCache->uNewState = (*env)->GetFieldID(env,lpCache->nmlistviewClass,"uNewState","I");
    lpCache->uOldState = (*env)->GetFieldID(env,lpCache->nmlistviewClass,"uOldState","I");
    lpCache->uChanged = (*env)->GetFieldID(env,lpCache->nmlistviewClass,"uChanged","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->nmlistviewClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->nmlistviewClass,"y","I");
    lpCache->lParam = (*env)->GetFieldID(env,lpCache->nmlistviewClass,"lParam","I");
    lpCache->cached = 1;
}

void cacheNmtoolbarFids(JNIEnv *env, jobject lpNmtoolbar, PNMTOOLBAR_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->nmtoolbarClass = (*env)->GetObjectClass(env,lpNmtoolbar);
    lpCache->hwndFrom = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"hwndFrom","I");
    lpCache->idFrom = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"idFrom","I");
    lpCache->code = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"code","I");
    lpCache->iItem = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"iItem","I");
    lpCache->iBitmap = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"iBitmap","I");
    lpCache->idCommand = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"idCommand","I");
    lpCache->fsState = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"fsState","B");
    lpCache->fsStyle = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"fsStyle","B");
    lpCache->___MISSING_ALIGNMENT__ = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"___MISSING_ALIGNMENT__","S");
    lpCache->dwData = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"dwData","I");
    lpCache->iString = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"iString","I");
    lpCache->cchText = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"cchText","I");
    lpCache->pszText = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"pszText","I");
    lpCache->left = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"left","I");
    lpCache->top = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"top","I");
    lpCache->right = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"right","I");
    lpCache->bottom = (*env)->GetFieldID(env,lpCache->nmtoolbarClass,"bottom","I");
    lpCache->cached = 1;
}

void cacheNmtvcustomdrawFids(JNIEnv *env, jobject lpNmtvcustomdraw, PNMTVCUSTOMDRAW_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->nmtvcustomdrawClass = (*env)->GetObjectClass(env,lpNmtvcustomdraw);
    lpCache->hwndFrom = (*env)->GetFieldID(env,lpCache->nmtvcustomdrawClass,"hwndFrom","I");
    lpCache->idFrom = (*env)->GetFieldID(env,lpCache->nmtvcustomdrawClass,"idFrom","I");
    lpCache->code = (*env)->GetFieldID(env,lpCache->nmtvcustomdrawClass,"code","I");
    lpCache->dwDrawStage = (*env)->GetFieldID(env,lpCache->nmtvcustomdrawClass,"dwDrawStage","I");
    lpCache->hdc = (*env)->GetFieldID(env,lpCache->nmtvcustomdrawClass,"hdc","I");
    lpCache->left = (*env)->GetFieldID(env,lpCache->nmtvcustomdrawClass,"left","I");
    lpCache->top = (*env)->GetFieldID(env,lpCache->nmtvcustomdrawClass,"top","I");
    lpCache->right = (*env)->GetFieldID(env,lpCache->nmtvcustomdrawClass,"right","I");
    lpCache->bottom = (*env)->GetFieldID(env,lpCache->nmtvcustomdrawClass,"bottom","I");
    lpCache->dwItemSpec = (*env)->GetFieldID(env,lpCache->nmtvcustomdrawClass,"dwItemSpec","I");
    lpCache->uItemState = (*env)->GetFieldID(env,lpCache->nmtvcustomdrawClass,"uItemState","I");
    lpCache->lItemlParam = (*env)->GetFieldID(env,lpCache->nmtvcustomdrawClass,"lItemlParam","I");
    lpCache->clrText = (*env)->GetFieldID(env,lpCache->nmtvcustomdrawClass,"clrText","I");
    lpCache->clrTextBk = (*env)->GetFieldID(env,lpCache->nmtvcustomdrawClass,"clrTextBk","I");
    lpCache->iLevel = (*env)->GetFieldID(env,lpCache->nmtvcustomdrawClass,"iLevel","I");
    lpCache->cached = 1;
}

void cacheOpenfilenameFids(JNIEnv *env, jobject lpOpenfilename, POPENFILENAME_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->openfilenameClass = (*env)->GetObjectClass(env,lpOpenfilename);
    lpCache->lStructSize = (*env)->GetFieldID(env,lpCache->openfilenameClass,"lStructSize","I");
    lpCache->hwndOwner = (*env)->GetFieldID(env,lpCache->openfilenameClass,"hwndOwner","I");
    lpCache->hInstance = (*env)->GetFieldID(env,lpCache->openfilenameClass,"hInstance","I");
    lpCache->lpstrFilter = (*env)->GetFieldID(env,lpCache->openfilenameClass,"lpstrFilter","I");
    lpCache->lpstrCustomFilter = (*env)->GetFieldID(env,lpCache->openfilenameClass,"lpstrCustomFilter","I");
    lpCache->nMaxCustFilter = (*env)->GetFieldID(env,lpCache->openfilenameClass,"nMaxCustFilter","I");
    lpCache->nFilterIndex = (*env)->GetFieldID(env,lpCache->openfilenameClass,"nFilterIndex","I");
    lpCache->lpstrFile = (*env)->GetFieldID(env,lpCache->openfilenameClass,"lpstrFile","I");
    lpCache->nMaxFile = (*env)->GetFieldID(env,lpCache->openfilenameClass,"nMaxFile","I");
    lpCache->lpstrFileTitle = (*env)->GetFieldID(env,lpCache->openfilenameClass,"lpstrFileTitle","I");
    lpCache->nMaxFileTitle = (*env)->GetFieldID(env,lpCache->openfilenameClass,"nMaxFileTitle","I");
    lpCache->lpstrInitialDir = (*env)->GetFieldID(env,lpCache->openfilenameClass,"lpstrInitialDir","I");
    lpCache->lpstrTitle = (*env)->GetFieldID(env,lpCache->openfilenameClass,"lpstrTitle","I");
    lpCache->Flags = (*env)->GetFieldID(env,lpCache->openfilenameClass,"Flags","I");
    lpCache->nFileOffset = (*env)->GetFieldID(env,lpCache->openfilenameClass,"nFileOffset","S");
    lpCache->nFileExtension = (*env)->GetFieldID(env,lpCache->openfilenameClass,"nFileExtension","S");
    lpCache->lpstrDefExt = (*env)->GetFieldID(env,lpCache->openfilenameClass,"lpstrDefExt","I");
    lpCache->lCustData = (*env)->GetFieldID(env,lpCache->openfilenameClass,"lCustData","I");
    lpCache->lpfnHook = (*env)->GetFieldID(env,lpCache->openfilenameClass,"lpfnHook","I");
    lpCache->lpTemplateName = (*env)->GetFieldID(env,lpCache->openfilenameClass,"lpTemplateName","I");
    lpCache->cached = 1;
}

void cachePaintstructFids(JNIEnv *env, jobject lpPaint, PPAINTSTRUCT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->paintClass = (*env)->GetObjectClass(env,lpPaint);
    lpCache->hdc = (*env)->GetFieldID(env,lpCache->paintClass,"hdc","I");
    lpCache->fErase = (*env)->GetFieldID(env,lpCache->paintClass,"fErase","Z");
    lpCache->left = (*env)->GetFieldID(env,lpCache->paintClass,"left","I");
    lpCache->top = (*env)->GetFieldID(env,lpCache->paintClass,"top","I");
    lpCache->right = (*env)->GetFieldID(env,lpCache->paintClass,"right","I");
    lpCache->bottom = (*env)->GetFieldID(env,lpCache->paintClass,"bottom","I");
    lpCache->fRestore = (*env)->GetFieldID(env,lpCache->paintClass,"fRestore","Z");
    lpCache->fIncUpdate = (*env)->GetFieldID(env,lpCache->paintClass,"fIncUpdate","Z");
    lpCache->cached = 1;
}

/*
void cacheParaformatFids(JNIEnv *env, jobject lpParaformat, PPARAFORMAT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->paraformatClass = (*env)->GetObjectClass(env,lpParaformat);
    lpCache->cbSize = (*env)->GetFieldID(env,lpCache->paraformatClass,"cbSize","I");
    lpCache->dwMask = (*env)->GetFieldID(env,lpCache->paraformatClass,"dwMask","I");
    lpCache->wNumbering  = (*env)->GetFieldID(env,lpCache->paraformatClass,"wNumbering","S");
    lpCache->wEffects  = (*env)->GetFieldID(env,lpCache->paraformatClass,"wEffects","S");
    lpCache->dxStartIndent = (*env)->GetFieldID(env,lpCache->paraformatClass,"dxStartIndent","I");
    lpCache->dxRightIndent = (*env)->GetFieldID(env,lpCache->paraformatClass,"dxRightIndent","I");
    lpCache->dxOffset = (*env)->GetFieldID(env,lpCache->paraformatClass,"dxOffset","I");
    lpCache->wAlignment  = (*env)->GetFieldID(env,lpCache->paraformatClass,"wAlignment","S");
    lpCache->cTabCount  = (*env)->GetFieldID(env,lpCache->paraformatClass,"cTabCount","S");
    lpCache->rgxTabs0 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs0","I");
    lpCache->rgxTabs1 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs1","I");
    lpCache->rgxTabs2 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs2","I");
    lpCache->rgxTabs3 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs3","I");
    lpCache->rgxTabs4 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs4","I");
    lpCache->rgxTabs5 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs5","I");
    lpCache->rgxTabs6 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs6","I");
    lpCache->rgxTabs7 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs7","I");
    lpCache->rgxTabs8 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs8","I");
    lpCache->rgxTabs9 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs9","I");
    lpCache->rgxTabs10 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs10","I");
    lpCache->rgxTabs11 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs11","I");
    lpCache->rgxTabs12 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs12","I");
    lpCache->rgxTabs13 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs13","I");
    lpCache->rgxTabs14 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs14","I");
    lpCache->rgxTabs15 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs15","I");
    lpCache->rgxTabs16 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs16","I");
    lpCache->rgxTabs17 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs17","I");
    lpCache->rgxTabs18 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs18","I");
    lpCache->rgxTabs19 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs19","I");
    lpCache->rgxTabs20 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs20","I");
    lpCache->rgxTabs21 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs21","I");
    lpCache->rgxTabs22 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs22","I");
    lpCache->rgxTabs23 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs23","I");
    lpCache->rgxTabs24 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs24","I");
    lpCache->rgxTabs25 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs25","I");
    lpCache->rgxTabs26 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs26","I");
    lpCache->rgxTabs27 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs27","I");
    lpCache->rgxTabs28 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs28","I");
    lpCache->rgxTabs29 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs29","I");
    lpCache->rgxTabs30 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs30","I");
    lpCache->rgxTabs31 = (*env)->GetFieldID(env,lpCache->paraformatClass,"rgxTabs31","I");
    lpCache->cached = 1;
}
*/
void cachePointFids(JNIEnv *env, jobject lpPoint, PPOINT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->pointClass = (*env)->GetObjectClass(env, lpPoint);
    lpCache->x = (*env)->GetFieldID(env,lpCache->pointClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->pointClass,"y","I");
    lpCache->cached = 1;
}

void cachePrintdlgFids(JNIEnv *env, jobject lpPrintdlg, PPRINTDLG_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->printdlgClass = (*env)->GetObjectClass(env, lpPrintdlg);
    lpCache->lStructSize = (*env)->GetFieldID(env,lpCache->printdlgClass,"lStructSize","I");
    lpCache->hwndOwner = (*env)->GetFieldID(env,lpCache->printdlgClass,"hwndOwner","I");
    lpCache->hDevMode = (*env)->GetFieldID(env,lpCache->printdlgClass,"hDevMode","I");
    lpCache->hDevNames = (*env)->GetFieldID(env,lpCache->printdlgClass,"hDevNames","I");
    lpCache->hDC = (*env)->GetFieldID(env,lpCache->printdlgClass,"hDC","I");
    lpCache->Flags = (*env)->GetFieldID(env,lpCache->printdlgClass,"Flags","I");
    lpCache->nFromPage = (*env)->GetFieldID(env,lpCache->printdlgClass,"nFromPage","S");
    lpCache->nToPage = (*env)->GetFieldID(env,lpCache->printdlgClass,"nToPage","S");
    lpCache->nMinPage = (*env)->GetFieldID(env,lpCache->printdlgClass,"nMinPage","S");
    lpCache->nMaxPage = (*env)->GetFieldID(env,lpCache->printdlgClass,"nMaxPage","S");
    lpCache->nCopies = (*env)->GetFieldID(env,lpCache->printdlgClass,"nCopies","S");
    lpCache->hInstance = (*env)->GetFieldID(env,lpCache->printdlgClass,"hInstance","I");
    lpCache->lCustData = (*env)->GetFieldID(env,lpCache->printdlgClass,"lCustData","I");
    lpCache->lpfnPrintHook = (*env)->GetFieldID(env,lpCache->printdlgClass,"lpfnPrintHook","I");
    lpCache->lpfnSetupHook = (*env)->GetFieldID(env,lpCache->printdlgClass,"lpfnSetupHook","I");
    lpCache->lpPrintTemplateName = (*env)->GetFieldID(env,lpCache->printdlgClass,"lpPrintTemplateName","I");
    lpCache->lpSetupTemplateName = (*env)->GetFieldID(env,lpCache->printdlgClass,"lpSetupTemplateName","I");
    lpCache->hPrintTemplate = (*env)->GetFieldID(env,lpCache->printdlgClass,"hPrintTemplate","I");
    lpCache->hSetupTemplate = (*env)->GetFieldID(env,lpCache->printdlgClass,"hSetupTemplate","I");
    lpCache->cached = 1;
}

void cacheRebarbandinfoFids(JNIEnv *env, jobject lpRebarbandinfo, PREBARBANDINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->rebarbandinfoClass = (*env)->GetObjectClass(env, lpRebarbandinfo);
    lpCache->cbSize = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"cbSize","I");
    lpCache->fMask = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"fMask","I");
    lpCache->fStyle = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"fStyle","I");
    lpCache->clrFore = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"clrFore","I");
    lpCache->clrBack = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"clrBack","I");
    lpCache->lpText = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"lpText","I");
    lpCache->cch = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"cch","I");
    lpCache->iImage = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"iImage","I");
    lpCache->hwndChild = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"hwndChild","I");
    lpCache->cxMinChild = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"cxMinChild","I");
    lpCache->cyMinChild = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"cyMinChild","I");
    lpCache->cx = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"cx","I");
    lpCache->hbmBack = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"hbmBack","I");
    lpCache->wID = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"wID","I");
#ifndef _WIN32_WCE
    lpCache->cyChild = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"cyChild","I");
    lpCache->cyMaxChild = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"cyMaxChild","I");
    lpCache->cyIntegral = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"cyIntegral","I");
    lpCache->cxIdeal = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"cxIdeal","I");
    lpCache->lParam = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"lParam","I");
    lpCache->cxHeader = (*env)->GetFieldID(env,lpCache->rebarbandinfoClass,"cxHeader","I");
#endif
    lpCache->cached = 1;
}

void cacheRectFids(JNIEnv *env, jobject lpRect, PRECT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->rectClass = (*env)->GetObjectClass(env,lpRect);
    lpCache->left = (*env)->GetFieldID(env,lpCache->rectClass,"left","I");
    lpCache->top = (*env)->GetFieldID(env,lpCache->rectClass,"top","I");
    lpCache->right = (*env)->GetFieldID(env,lpCache->rectClass,"right","I");
    lpCache->bottom = (*env)->GetFieldID(env,lpCache->rectClass,"bottom","I");
    lpCache->cached = 1;
}

void cacheScrollinfoFids(JNIEnv *env, jobject lpScrollinfo, PSCROLLINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->scrollinfoClass = (*env)->GetObjectClass(env,lpScrollinfo);
    lpCache->cbSize = (*env)->GetFieldID(env,lpCache->scrollinfoClass,"cbSize","I");
    lpCache->fMask = (*env)->GetFieldID(env,lpCache->scrollinfoClass,"fMask","I");
    lpCache->nMin = (*env)->GetFieldID(env,lpCache->scrollinfoClass,"nMin","I");
    lpCache->nMax = (*env)->GetFieldID(env,lpCache->scrollinfoClass,"nMax","I");
    lpCache->nPage = (*env)->GetFieldID(env,lpCache->scrollinfoClass,"nPage","I");
    lpCache->nPos = (*env)->GetFieldID(env,lpCache->scrollinfoClass,"nPos","I");
    lpCache->nTrackPos = (*env)->GetFieldID(env,lpCache->scrollinfoClass,"nTrackPos","I");
    lpCache->cached = 1;
}

void cacheSizeFids(JNIEnv *env, jobject lpSize, PSIZE_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->sizeClass = (*env)->GetObjectClass(env,lpSize);
    lpCache->cx = (*env)->GetFieldID(env,lpCache->sizeClass,"cx","I");
    lpCache->cy = (*env)->GetFieldID(env,lpCache->sizeClass,"cy","I");
    lpCache->cached = 1;
}

void cacheTbbuttonFids(JNIEnv *env, jobject lpTbbutton, PTBBUTTON_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->tbbuttonClass = (*env)->GetObjectClass(env,lpTbbutton);
    lpCache->iBitmap = (*env)->GetFieldID(env,lpCache->tbbuttonClass,"iBitmap","I");
    lpCache->idCommand = (*env)->GetFieldID(env,lpCache->tbbuttonClass,"idCommand","I");
    lpCache->fsState = (*env)->GetFieldID(env,lpCache->tbbuttonClass,"fsState","B");
    lpCache->fsStyle = (*env)->GetFieldID(env,lpCache->tbbuttonClass,"fsStyle","B");
    lpCache->dwData = (*env)->GetFieldID(env,lpCache->tbbuttonClass,"dwData","I");
    lpCache->iString = (*env)->GetFieldID(env,lpCache->tbbuttonClass,"iString","I");
    lpCache->cached = 1;
}

void cacheTbbuttoninfoFids(JNIEnv *env, jobject lpTbbuttoninfo, PTBBUTTONINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->tbbuttoninfoClass = (*env)->GetObjectClass(env,lpTbbuttoninfo);
    lpCache->cbSize = (*env)->GetFieldID(env,lpCache->tbbuttoninfoClass,"cbSize","I");
    lpCache->dwMask = (*env)->GetFieldID(env,lpCache->tbbuttoninfoClass,"dwMask","I");
    lpCache->idCommand = (*env)->GetFieldID(env,lpCache->tbbuttoninfoClass,"idCommand","I");
    lpCache->iImage = (*env)->GetFieldID(env,lpCache->tbbuttoninfoClass,"iImage","I");
    lpCache->fsState = (*env)->GetFieldID(env,lpCache->tbbuttoninfoClass,"fsState","B");
    lpCache->fsStyle = (*env)->GetFieldID(env,lpCache->tbbuttoninfoClass,"fsStyle","B");
    lpCache->cx = (*env)->GetFieldID(env,lpCache->tbbuttoninfoClass,"cx","S");
    lpCache->lParam = (*env)->GetFieldID(env,lpCache->tbbuttoninfoClass,"lParam","I");
    lpCache->pszText = (*env)->GetFieldID(env,lpCache->tbbuttoninfoClass,"pszText","I");
    lpCache->cchText = (*env)->GetFieldID(env,lpCache->tbbuttoninfoClass,"cchText","I");
    lpCache->cached = 1;
}

void cacheTcitemFids(JNIEnv *env, jobject lpTcitem, PTCITEM_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->tcitemClass = (*env)->GetObjectClass(env,lpTcitem);
    lpCache->mask = (*env)->GetFieldID(env,lpCache->tcitemClass,"mask","I");
    lpCache->dwState = (*env)->GetFieldID(env,lpCache->tcitemClass,"dwState","I");
    lpCache->dwStateMask = (*env)->GetFieldID(env,lpCache->tcitemClass,"dwStateMask","I");
    lpCache->pszText = (*env)->GetFieldID(env,lpCache->tcitemClass,"pszText","I");
    lpCache->cchTextMax = (*env)->GetFieldID(env,lpCache->tcitemClass,"cchTextMax","I");
    lpCache->iImage = (*env)->GetFieldID(env,lpCache->tcitemClass,"iImage","I");
    lpCache->lParam = (*env)->GetFieldID(env,lpCache->tcitemClass,"lParam","I");
    lpCache->cached = 1;
}

void cacheTextmetricFids(JNIEnv *env, jobject lpTextmetric, PTEXTMETRIC_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->textmetricClass = (*env)->GetObjectClass(env,lpTextmetric);
    lpCache->tmHeight = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmHeight","I");
    lpCache->tmAscent = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmAscent","I");
    lpCache->tmDescent = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmDescent","I");
    lpCache->tmInternalLeading = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmInternalLeading","I");
    lpCache->tmExternalLeading = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmExternalLeading","I");
    lpCache->tmAveCharWidth = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmAveCharWidth","I");
    lpCache->tmMaxCharWidth = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmMaxCharWidth","I");
    lpCache->tmWeight = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmWeight","I");
    lpCache->tmOverhang = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmOverhang","I");
    lpCache->tmDigitizedAspectX = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmDigitizedAspectX","I");
    lpCache->tmDigitizedAspectY = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmDigitizedAspectY","I");
    lpCache->tmFirstChar = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmFirstChar","C");
    lpCache->tmLastChar = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmLastChar","C");
    lpCache->tmDefaultChar = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmDefaultChar","C");
    lpCache->tmBreakChar = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmBreakChar","C");
    lpCache->tmItalic = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmItalic","B");
    lpCache->tmUnderlined = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmUnderlined","B");
    lpCache->tmStruckOut = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmStruckOut","B");
    lpCache->tmPitchAndFamily = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmPitchAndFamily","B");
    lpCache->tmCharSet = (*env)->GetFieldID(env,lpCache->textmetricClass,"tmCharSet","B");
    lpCache->cached = 1;
}

void cacheTvhittestinfoFids(JNIEnv *env, jobject lpTvhittestinfo, PTVHITTESTINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->tvhittestinfoClass = (*env)->GetObjectClass(env,lpTvhittestinfo);
    lpCache->x = (*env)->GetFieldID(env,lpCache->tvhittestinfoClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->tvhittestinfoClass,"y","I");
    lpCache->flags = (*env)->GetFieldID(env,lpCache->tvhittestinfoClass,"flags","I");
    lpCache->hItem = (*env)->GetFieldID(env,lpCache->tvhittestinfoClass,"hItem","I");
    lpCache->cached = 1;
}

void cacheTvinsertstructFids(JNIEnv *env, jobject lpTvinsertstruct, PTVINSERTSTRUCT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->tvinsertstructClass = (*env)->GetObjectClass(env,lpTvinsertstruct);
    lpCache->hParent = (*env)->GetFieldID(env,lpCache->tvinsertstructClass,"hParent","I");
    lpCache->hInsertAfter = (*env)->GetFieldID(env,lpCache->tvinsertstructClass,"hInsertAfter","I");
    lpCache->mask = (*env)->GetFieldID(env,lpCache->tvinsertstructClass,"mask","I");
    lpCache->hItem = (*env)->GetFieldID(env,lpCache->tvinsertstructClass,"hItem","I");
    lpCache->state = (*env)->GetFieldID(env,lpCache->tvinsertstructClass,"state","I");
    lpCache->stateMask = (*env)->GetFieldID(env,lpCache->tvinsertstructClass,"stateMask","I");
    lpCache->pszText = (*env)->GetFieldID(env,lpCache->tvinsertstructClass,"pszText","I");
    lpCache->cchTextMax = (*env)->GetFieldID(env,lpCache->tvinsertstructClass,"cchTextMax","I");
    lpCache->iImage = (*env)->GetFieldID(env,lpCache->tvinsertstructClass,"iImage","I");
    lpCache->iSelectedImage = (*env)->GetFieldID(env,lpCache->tvinsertstructClass,"iSelectedImage","I");
    lpCache->cChildren = (*env)->GetFieldID(env,lpCache->tvinsertstructClass,"cChildren","I");
    lpCache->lParam = (*env)->GetFieldID(env,lpCache->tvinsertstructClass,"lParam","I");
    lpCache->cached = 1;
}

void cacheTvitemFids(JNIEnv *env, jobject lpTvitem, PTVITEM_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->tvitemClass = (*env)->GetObjectClass(env,lpTvitem);
    lpCache->mask = (*env)->GetFieldID(env,lpCache->tvitemClass,"mask","I");
    lpCache->hItem = (*env)->GetFieldID(env,lpCache->tvitemClass,"hItem","I");
    lpCache->state = (*env)->GetFieldID(env,lpCache->tvitemClass,"state","I");
    lpCache->stateMask = (*env)->GetFieldID(env,lpCache->tvitemClass,"stateMask","I");
    lpCache->pszText = (*env)->GetFieldID(env,lpCache->tvitemClass,"pszText","I");
    lpCache->cchTextMax = (*env)->GetFieldID(env,lpCache->tvitemClass,"cchTextMax","I");
    lpCache->iImage = (*env)->GetFieldID(env,lpCache->tvitemClass,"iImage","I");
    lpCache->iSelectedImage = (*env)->GetFieldID(env,lpCache->tvitemClass,"iSelectedImage","I");
    lpCache->cChildren = (*env)->GetFieldID(env,lpCache->tvitemClass,"cChildren","I");
    lpCache->lParam = (*env)->GetFieldID(env,lpCache->tvitemClass,"lParam","I");
    lpCache->cached = 1;
}

void cacheWindowposFids(JNIEnv *env, jobject lpWindowpos, PWINDOWPOS_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->windowposClass = (*env)->GetObjectClass(env,lpWindowpos);
    lpCache->hwnd = (*env)->GetFieldID(env,lpCache->windowposClass,"hwnd","I");
    lpCache->hwndInsertAfter = (*env)->GetFieldID(env,lpCache->windowposClass,"hwndInsertAfter","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->windowposClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->windowposClass,"y","I");
    lpCache->cx = (*env)->GetFieldID(env,lpCache->windowposClass,"cx","I");
    lpCache->cy = (*env)->GetFieldID(env,lpCache->windowposClass,"cy","I");
    lpCache->flags = (*env)->GetFieldID(env,lpCache->windowposClass,"flags","I");
    lpCache->cached = 1;
}

void cacheWndclassFids(JNIEnv *env, jobject lpWndclass, PWNDCLASS_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->wndclassClass = (*env)->GetObjectClass(env,lpWndclass);
    lpCache->style = (*env)->GetFieldID(env,lpCache->wndclassClass,"style","I");
    lpCache->lpfnWndProc = (*env)->GetFieldID(env,lpCache->wndclassClass,"lpfnWndProc","I");
    lpCache->cbClsExtra = (*env)->GetFieldID(env,lpCache->wndclassClass,"cbClsExtra","I");
    lpCache->cbWndExtra = (*env)->GetFieldID(env,lpCache->wndclassClass,"cbWndExtra","I");
    lpCache->hInstance = (*env)->GetFieldID(env,lpCache->wndclassClass,"hInstance","I");
    lpCache->hIcon = (*env)->GetFieldID(env,lpCache->wndclassClass,"hIcon","I");
    lpCache->hCursor = (*env)->GetFieldID(env,lpCache->wndclassClass,"hCursor","I");
    lpCache->hbrBackground = (*env)->GetFieldID(env,lpCache->wndclassClass,"hbrBackground","I");
    lpCache->lpszMenuName = (*env)->GetFieldID(env,lpCache->wndclassClass,"lpszMenuName","I");
    lpCache->lpszClassName = (*env)->GetFieldID(env,lpCache->wndclassClass,"lpszClassName","I");
    lpCache->cached = 1;
}

/*
* NOTE: Untested - szDisplayName and szTypeName don't work
*/
/*
void cacheShfileinfoFids(JNIEnv *env, jobject lpShfileinfo, PSHFILEINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->shfileinfoClass = (*env)->GetObjectClass(env,lpShfileinfo);
    lpCache->hIcon = (*env)->GetFieldID(env,lpCache->shfileinfoClass,"hIcon","I");
    lpCache->iIcon = (*env)->GetFieldID(env,lpCache->shfileinfoClass,"iIcon","I");
    lpCache->dwAttributes = (*env)->GetFieldID(env,lpCache->shfileinfoClass,"dwAttributes","I");
    lpCache->szDisplayName = (*env)->GetFieldID(env,lpCache->shfileinfoClass,"szDisplayName","[B");
    lpCache->szTypeName = (*env)->GetFieldID(env,lpCache->shfileinfoClass,"szTypeName","[B");
    lpCache->cached = 1;
}
*/
  
/* ----------- ole fid and class caches  ----------- */

void cacheCauuidFids(JNIEnv *env, jobject lpCauuid, PCAUUID_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->cauuidClass = (*env)->GetObjectClass(env,lpCauuid);
    lpCache->cElems = (*env)->GetFieldID(env,lpCache->cauuidClass,"cElems","I");
    lpCache->pElems = (*env)->GetFieldID(env,lpCache->cauuidClass,"pElems","I");
    lpCache->cached = 1;
}

void cacheCoserverinfoFids(JNIEnv *env, jobject lpCoservinfo, PCOSERVERINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->coserverinfoClass = (*env)->GetObjectClass(env,lpCoservinfo);
    lpCache->dwReserved1 = (*env)->GetFieldID(env,lpCache->coserverinfoClass,"dwReserved1","I");
    lpCache->pwszName = (*env)->GetFieldID(env,lpCache->coserverinfoClass,"pwszName","I");
    lpCache->pAuthInfo = (*env)->GetFieldID(env,lpCache->coserverinfoClass,"pAuthInfo","I");
    lpCache->dwReserved2 = (*env)->GetFieldID(env,lpCache->coserverinfoClass,"dwReserved2","I");
    lpCache->cached = 1;
}

void cacheControlinfoFids(JNIEnv *env, jobject lpControlinfo, PCONTROLINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->controlinfoClass = (*env)->GetObjectClass(env,lpControlinfo);
    lpCache->cb = (*env)->GetFieldID(env,lpCache->controlinfoClass,"cb","I");
    lpCache->hAccel = (*env)->GetFieldID(env,lpCache->controlinfoClass,"hAccel","I");
    lpCache->cAccel = (*env)->GetFieldID(env,lpCache->controlinfoClass,"cAccel","S");
    lpCache->filler = (*env)->GetFieldID(env,lpCache->controlinfoClass,"filler","S");
    lpCache->dwFlags = (*env)->GetFieldID(env,lpCache->controlinfoClass,"dwFlags","I");
    lpCache->cached = 1;
}

void cacheDispparamsFids(JNIEnv *env, jobject lpDispparams, PDISPPARAMS_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->dispparamsClass = (*env)->GetObjectClass(env,lpDispparams);
    lpCache->rgvarg = (*env)->GetFieldID(env,lpCache->dispparamsClass,"rgvarg","I");
    lpCache->rgdispidNamedArgs = (*env)->GetFieldID(env,lpCache->dispparamsClass,"rgdispidNamedArgs","I");
    lpCache->cArgs = (*env)->GetFieldID(env,lpCache->dispparamsClass,"cArgs","I");
    lpCache->cNamedArgs = (*env)->GetFieldID(env,lpCache->dispparamsClass,"cNamedArgs","I");
    lpCache->cached = 1;
}

void cacheDvaspectinfoFids(JNIEnv *env, jobject lpDvaspectinfo, PDVASPECTINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->dvaspectinfoClass = (*env)->GetObjectClass(env,lpDvaspectinfo);
    lpCache->cb = (*env)->GetFieldID(env,lpCache->dvaspectinfoClass,"cb","I");
    lpCache->dwFlags = (*env)->GetFieldID(env,lpCache->dvaspectinfoClass,"dwFlags","I");
    lpCache->cached = 1;
}

void cacheDvtargetdeviceFids(JNIEnv *env, jobject lpDvtargetdevice, PDVTARGETDEVICE_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->dvtargetdeviceClass = (*env)->GetObjectClass(env,lpDvtargetdevice);
    lpCache->tdSize = (*env)->GetFieldID(env,lpCache->dvtargetdeviceClass,"tdSize","I");
    lpCache->tdDriverNameOffset = (*env)->GetFieldID(env,lpCache->dvtargetdeviceClass,"tdDriverNameOffset","S");
    lpCache->tdDeviceNameOffset = (*env)->GetFieldID(env,lpCache->dvtargetdeviceClass,"tdDeviceNameOffset","S");
    lpCache->tdPortNameOffset = (*env)->GetFieldID(env,lpCache->dvtargetdeviceClass,"tdPortNameOffset","S");
    lpCache->tdExtDevmodeOffset = (*env)->GetFieldID(env,lpCache->dvtargetdeviceClass,"tdExtDevmodeOffset","S");
    lpCache->tdData = (*env)->GetFieldID(env,lpCache->dvtargetdeviceClass,"tdData","B");
    lpCache->cached = 1;
}

void cacheExcepinfoFids(JNIEnv *env, jobject lpExcepinfo, PEXCEPINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->excepinfoClass = (*env)->GetObjectClass(env,lpExcepinfo);
    lpCache->wCode = (*env)->GetFieldID(env,lpCache->excepinfoClass,"wCode","S");
    lpCache->wReserved = (*env)->GetFieldID(env,lpCache->excepinfoClass,"wReserved","S");
    lpCache->bstrSource = (*env)->GetFieldID(env,lpCache->excepinfoClass,"bstrSource","I");
    lpCache->bstrDescription = (*env)->GetFieldID(env,lpCache->excepinfoClass,"bstrDescription","I");
    lpCache->bstrHelpFile = (*env)->GetFieldID(env,lpCache->excepinfoClass,"bstrHelpFile","I");
    lpCache->dwHelpContext = (*env)->GetFieldID(env,lpCache->excepinfoClass,"dwHelpContext","I");
    lpCache->pvReserved = (*env)->GetFieldID(env,lpCache->excepinfoClass,"pvReserved","I");
    lpCache->pfnDeferredFillIn = (*env)->GetFieldID(env,lpCache->excepinfoClass,"pfnDeferredFillIn","I");
    lpCache->scode = (*env)->GetFieldID(env,lpCache->excepinfoClass,"scode","I");
    lpCache->cached = 1;
}

void cacheLicinfoFids(JNIEnv *env, jobject lpLicinfo, PLICINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->licinfoClass = (*env)->GetObjectClass(env,lpLicinfo);
    lpCache->cbLicInfo = (*env)->GetFieldID(env,lpCache->licinfoClass,"cbLicInfo","I");
    lpCache->fRuntimeKeyAvail = (*env)->GetFieldID(env,lpCache->licinfoClass,"fRuntimeKeyAvail","I");
    lpCache->fLicVerified = (*env)->GetFieldID(env,lpCache->licinfoClass,"fLicVerified","I");
    lpCache->cached = 1;
}

void cacheFiletimeFids(JNIEnv *env, jobject lpFiletime, PFILETIME_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->filetimeClass = (*env)->GetObjectClass(env,lpFiletime);
    lpCache->dwLowDateTime = (*env)->GetFieldID(env,lpCache->filetimeClass,"dwLowDateTime","I");
    lpCache->dwHighDateTime = (*env)->GetFieldID(env,lpCache->filetimeClass,"dwHighDateTime","I");
    lpCache->cached = 1;
}

void cacheFormatetcFids(JNIEnv *env, jobject lpFormatetc, PFORMATETC_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->formatetcClass = (*env)->GetObjectClass(env,lpFormatetc);
    lpCache->cfFormat = (*env)->GetFieldID(env,lpCache->formatetcClass,"cfFormat","I");
    lpCache->ptd = (*env)->GetFieldID(env,lpCache->formatetcClass,"ptd","I");
    lpCache->dwAspect = (*env)->GetFieldID(env,lpCache->formatetcClass,"dwAspect","I");
    lpCache->lindex = (*env)->GetFieldID(env,lpCache->formatetcClass,"lindex","I");
    lpCache->tymed = (*env)->GetFieldID(env,lpCache->formatetcClass,"tymed","I");
    lpCache->cached = 1;
}

void cacheGuidFids(JNIEnv *env, jobject lpGuid, PGUID_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->guidClass = (*env)->GetObjectClass(env,lpGuid);
    lpCache->data1 = (*env)->GetFieldID(env,lpCache->guidClass,"data1","I");
    lpCache->data2 = (*env)->GetFieldID(env,lpCache->guidClass,"data2","S");
    lpCache->data3 = (*env)->GetFieldID(env,lpCache->guidClass,"data3","S");
    lpCache->b0 = (*env)->GetFieldID(env,lpCache->guidClass,"b0","B");
    lpCache->b1 = (*env)->GetFieldID(env,lpCache->guidClass,"b1","B");
    lpCache->b2 = (*env)->GetFieldID(env,lpCache->guidClass,"b2","B");
    lpCache->b3 = (*env)->GetFieldID(env,lpCache->guidClass,"b3","B");
    lpCache->b4 = (*env)->GetFieldID(env,lpCache->guidClass,"b4","B");
    lpCache->b5 = (*env)->GetFieldID(env,lpCache->guidClass,"b5","B");
    lpCache->b6 = (*env)->GetFieldID(env,lpCache->guidClass,"b6","B");
    lpCache->b7 = (*env)->GetFieldID(env,lpCache->guidClass,"b7","B");
    lpCache->cached = 1;
}

void cacheIdldescFids(JNIEnv *env, jobject lpIdldesc, PIDLDESC_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->idldescClass = (*env)->GetObjectClass(env,lpIdldesc);
    lpCache->dwReserved = (*env)->GetFieldID(env,lpCache->idldescClass,"dwReserved","I");
    lpCache->wIDLFlags = (*env)->GetFieldID(env,lpCache->idldescClass,"wIDLFlags","S");
    lpCache->cached = 1;
}

void cacheMulti_qiFids(JNIEnv *env, jobject lpMulti_qi, PMULTI_QI_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->multi_qiClass = (*env)->GetObjectClass(env,lpMulti_qi);
    lpCache->pIID = (*env)->GetFieldID(env,lpCache->multi_qiClass,"pIID","I");
    lpCache->pItf = (*env)->GetFieldID(env,lpCache->multi_qiClass,"pItf","I");
    lpCache->hr = (*env)->GetFieldID(env,lpCache->multi_qiClass,"hr","I");
    lpCache->cached = 1;
}

void cacheOleinplaceframeinfoFids(JNIEnv *env, jobject lpOleinplaceframeinfo, POLEINPLACEFRAMEINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->oleinplaceframeinfoClass = (*env)->GetObjectClass(env,lpOleinplaceframeinfo);
    lpCache->cb = (*env)->GetFieldID(env,lpCache->oleinplaceframeinfoClass,"cb","I");
    lpCache->fMDIApp = (*env)->GetFieldID(env,lpCache->oleinplaceframeinfoClass,"fMDIApp","I");
    lpCache->hwndFrame = (*env)->GetFieldID(env,lpCache->oleinplaceframeinfoClass,"hwndFrame","I");
    lpCache->haccel = (*env)->GetFieldID(env,lpCache->oleinplaceframeinfoClass,"haccel","I");
    lpCache->cAccelEntries = (*env)->GetFieldID(env,lpCache->oleinplaceframeinfoClass,"cAccelEntries","I");
    lpCache->cached = 1;
}

void cacheOleverbFids(JNIEnv *env, jobject lpOleverb, POLEVERB_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->oleverbClass = (*env)->GetObjectClass(env,lpOleverb);
    lpCache->lVerb = (*env)->GetFieldID(env,lpCache->oleverbClass,"lVerb","I");
    lpCache->lpszVerbName = (*env)->GetFieldID(env,lpCache->oleverbClass,"lpszVerbName","I");
    lpCache->fuFlags = (*env)->GetFieldID(env,lpCache->oleverbClass,"fuFlags","I");
    lpCache->grfAttribs = (*env)->GetFieldID(env,lpCache->oleverbClass,"grfAttribs","I");
    lpCache->cached = 1;
}

void cacheStgmediumFids(JNIEnv *env, jobject lpStgmedium, PSTGMEDIUM_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->stgmediumClass = (*env)->GetObjectClass(env,lpStgmedium);
    lpCache->tymed = (*env)->GetFieldID(env,lpCache->stgmediumClass,"tymed","I");
    lpCache->unionField = (*env)->GetFieldID(env,lpCache->stgmediumClass,"unionField","I");
    lpCache->pUnkForRelease = (*env)->GetFieldID(env,lpCache->stgmediumClass,"pUnkForRelease","I");
    lpCache->cached = 1;
}

void cacheStatstgFids(JNIEnv *env, jobject lpStatstg, PSTATSTG_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->statstgClass = (*env)->GetObjectClass(env,lpStatstg);
    lpCache->pwcsName = (*env)->GetFieldID(env,lpCache->statstgClass,"pwcsName","I");
    lpCache->type = (*env)->GetFieldID(env,lpCache->statstgClass,"type","I");
    lpCache->cbSize = (*env)->GetFieldID(env,lpCache->statstgClass,"cbSize","L");
    lpCache->mtime_dwLowDateTime = (*env)->GetFieldID(env,lpCache->statstgClass,"mtime_dwLowDateTime","I");
    lpCache->mtime_dwHighDateTime = (*env)->GetFieldID(env,lpCache->statstgClass,"mtime_dwHighDateTime","I");
    lpCache->ctime_dwLowDateTime = (*env)->GetFieldID(env,lpCache->statstgClass,"ctime_dwLowDateTime","I");
    lpCache->ctime_dwHighDateTime = (*env)->GetFieldID(env,lpCache->statstgClass,"ctime_dwHighDateTime","I");
    lpCache->atime_dwLowDateTime = (*env)->GetFieldID(env,lpCache->statstgClass,"atime_dwLowDateTime","I");
    lpCache->atime_dwHighDateTime = (*env)->GetFieldID(env,lpCache->statstgClass,"atime_dwHighDateTime","I");
    lpCache->grfMode = (*env)->GetFieldID(env,lpCache->statstgClass,"grfMode","I");
    lpCache->grfLocksSupported = (*env)->GetFieldID(env,lpCache->statstgClass,"grfLocksSupported","I");
    lpCache->clsid_data1 = (*env)->GetFieldID(env,lpCache->statstgClass,"clsid_data1","I");
    lpCache->clsid_data2 = (*env)->GetFieldID(env,lpCache->statstgClass,"clsid_data2","S");
    lpCache->clsid_data3 = (*env)->GetFieldID(env,lpCache->statstgClass,"clsid_data3","S");
    lpCache->clsid_b0 = (*env)->GetFieldID(env,lpCache->statstgClass,"clsid_b0","B");
    lpCache->clsid_b1 = (*env)->GetFieldID(env,lpCache->statstgClass,"clsid_b1","B");
    lpCache->clsid_b2 = (*env)->GetFieldID(env,lpCache->statstgClass,"clsid_b2","B");
    lpCache->clsid_b3 = (*env)->GetFieldID(env,lpCache->statstgClass,"clsid_b3","B");
    lpCache->clsid_b4 = (*env)->GetFieldID(env,lpCache->statstgClass,"clsid_b4","B");
    lpCache->clsid_b5 = (*env)->GetFieldID(env,lpCache->statstgClass,"clsid_b5","B");
    lpCache->clsid_b6 = (*env)->GetFieldID(env,lpCache->statstgClass,"clsid_b6","B");
    lpCache->clsid_b7 = (*env)->GetFieldID(env,lpCache->statstgClass,"clsid_b7","B");
    lpCache->grfStateBits = (*env)->GetFieldID(env,lpCache->statstgClass,"grfStateBits","I");
    lpCache->reserved = (*env)->GetFieldID(env,lpCache->statstgClass,"reserved","I");
    lpCache->cached = 1;
}

void cacheTypeattrFids(JNIEnv *env, jobject lpTypeattr, PTYPEATTR_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->typeattrClass = (*env)->GetObjectClass(env,lpTypeattr);
    lpCache->guid_data1 = (*env)->GetFieldID(env,lpCache->typeattrClass,"guid_data1","I");
    lpCache->guid_data2 = (*env)->GetFieldID(env,lpCache->typeattrClass,"guid_data2","S");
    lpCache->guid_data3 = (*env)->GetFieldID(env,lpCache->typeattrClass,"guid_data3","S");
    lpCache->guid_b0 = (*env)->GetFieldID(env,lpCache->typeattrClass,"guid_b0","B");
    lpCache->guid_b1 = (*env)->GetFieldID(env,lpCache->typeattrClass,"guid_b1","B");
    lpCache->guid_b2 = (*env)->GetFieldID(env,lpCache->typeattrClass,"guid_b2","B");
    lpCache->guid_b3 = (*env)->GetFieldID(env,lpCache->typeattrClass,"guid_b3","B");
    lpCache->guid_b4 = (*env)->GetFieldID(env,lpCache->typeattrClass,"guid_b4","B");
    lpCache->guid_b5 = (*env)->GetFieldID(env,lpCache->typeattrClass,"guid_b5","B");
    lpCache->guid_b6 = (*env)->GetFieldID(env,lpCache->typeattrClass,"guid_b6","B");
    lpCache->guid_b7 = (*env)->GetFieldID(env,lpCache->typeattrClass,"guid_b7","B");
    lpCache->lcid = (*env)->GetFieldID(env,lpCache->typeattrClass,"lcid","I");
    lpCache->dwReserved = (*env)->GetFieldID(env,lpCache->typeattrClass,"dwReserved","I");
    lpCache->memidConstructor = (*env)->GetFieldID(env,lpCache->typeattrClass,"memidConstructor","I");
    lpCache->memidDestructor = (*env)->GetFieldID(env,lpCache->typeattrClass,"memidDestructor","I");
    lpCache->lpstrSchema = (*env)->GetFieldID(env,lpCache->typeattrClass,"lpstrSchema","I");
    lpCache->cbSizeInstance = (*env)->GetFieldID(env,lpCache->typeattrClass,"cbSizeInstance","I");
    lpCache->typekind = (*env)->GetFieldID(env,lpCache->typeattrClass,"typekind","I");
    lpCache->cFuncs = (*env)->GetFieldID(env,lpCache->typeattrClass,"cFuncs","S");
    lpCache->cVars = (*env)->GetFieldID(env,lpCache->typeattrClass,"cVars","S");
    lpCache->cImplTypes = (*env)->GetFieldID(env,lpCache->typeattrClass,"cImplTypes","S");
    lpCache->cbSizeVft = (*env)->GetFieldID(env,lpCache->typeattrClass,"cbSizeVft","S");
    lpCache->cbAlignment = (*env)->GetFieldID(env,lpCache->typeattrClass,"cbAlignment","S");
    lpCache->wTypeFlags = (*env)->GetFieldID(env,lpCache->typeattrClass,"wTypeFlags","S");
    lpCache->wMajorVerNum = (*env)->GetFieldID(env,lpCache->typeattrClass,"wMajorVerNum","S");
    lpCache->wMinorVerNum = (*env)->GetFieldID(env,lpCache->typeattrClass,"wMinorVerNum","S");
    lpCache->tdescAlias_unionField = (*env)->GetFieldID(env,lpCache->typeattrClass,"tdescAlias_unionField","I");
    lpCache->tdescAlias_vt = (*env)->GetFieldID(env,lpCache->typeattrClass,"tdescAlias_vt","S");
    lpCache->idldescType_dwReserved = (*env)->GetFieldID(env,lpCache->typeattrClass,"idldescType_dwReserved","I");
    lpCache->idldescType_wIDLFlags = (*env)->GetFieldID(env,lpCache->typeattrClass,"idldescType_wIDLFlags","S");
    lpCache->cached = 1;
}

void cacheTypedescFids(JNIEnv *env, jobject lpTypedesc, PTYPEDESC_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->typedescClass = (*env)->GetObjectClass(env,lpTypedesc);
    lpCache->typedesc_union = (*env)->GetFieldID(env,lpCache->typedescClass,"typedesc_union","I");
    lpCache->vt = (*env)->GetFieldID(env,lpCache->typedescClass,"vt","S");
    lpCache->cached = 1;
}

void cacheFuncdesc1Fids(JNIEnv *env, jobject lpFuncdesc, PFUNCDESC1_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->funcdescClass = (*env)->GetObjectClass(env,lpFuncdesc);
    lpCache->memid = (*env)->GetFieldID(env,lpCache->funcdescClass,"memid","I");
    lpCache->lprgscode = (*env)->GetFieldID(env,lpCache->funcdescClass,"lprgscode","I");
    lpCache->lprgelemdescParam = (*env)->GetFieldID(env,lpCache->funcdescClass,"lprgelemdescParam","I");
    lpCache->funckind = (*env)->GetFieldID(env,lpCache->funcdescClass,"funckind","I");
    lpCache->invkind = (*env)->GetFieldID(env,lpCache->funcdescClass,"invkind","I");
    lpCache->callconv = (*env)->GetFieldID(env,lpCache->funcdescClass,"callconv","I");
    lpCache->cParams = (*env)->GetFieldID(env,lpCache->funcdescClass,"cParams","S");
    lpCache->cParamsOpt = (*env)->GetFieldID(env,lpCache->funcdescClass,"cParamsOpt","S");
    lpCache->oVft = (*env)->GetFieldID(env,lpCache->funcdescClass,"oVft","S");
    lpCache->cScodes = (*env)->GetFieldID(env,lpCache->funcdescClass,"cScodes","S");
    lpCache->elemdescFunc_tdesc_union = (*env)->GetFieldID(env,lpCache->funcdescClass,"elemdescFunc_tdesc_union","I");
    lpCache->elemdescFunc_tdesc_vt = (*env)->GetFieldID(env,lpCache->funcdescClass,"elemdescFunc_tdesc_vt","S");
    lpCache->elemdescFunc_tdesc_filler = (*env)->GetFieldID(env,lpCache->funcdescClass,"elemdescFunc_tdesc_filler","S");
    lpCache->elemdescFunc_paramdesc_pparamdescex = (*env)->GetFieldID(env,lpCache->funcdescClass,"elemdescFunc_paramdesc_pparamdescex","I");
    lpCache->elemdescFunc_paramdesc_wParamFlags = (*env)->GetFieldID(env,lpCache->funcdescClass,"elemdescFunc_paramdesc_wParamFlags","S");
    lpCache->elemdescFunc_paramdesc_filler = (*env)->GetFieldID(env,lpCache->funcdescClass,"elemdescFunc_paramdesc_filler","S");
    lpCache->wFuncFlags = (*env)->GetFieldID(env,lpCache->funcdescClass,"wFuncFlags","S");
    lpCache->cached = 1;
 }

void cacheFuncdesc2Fids(JNIEnv *env, jobject lpFuncdesc, PFUNCDESC2_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->funcdescClass = (*env)->GetObjectClass(env,lpFuncdesc);
    lpCache->memid = (*env)->GetFieldID(env,lpCache->funcdescClass,"memid","I");
    lpCache->lprgscode = (*env)->GetFieldID(env,lpCache->funcdescClass,"lprgscode","I");
    lpCache->lprgelemdescParam = (*env)->GetFieldID(env,lpCache->funcdescClass,"lprgelemdescParam","I");
    lpCache->funckind = (*env)->GetFieldID(env,lpCache->funcdescClass,"funckind","I");
    lpCache->invkind = (*env)->GetFieldID(env,lpCache->funcdescClass,"invkind","I");
    lpCache->callconv = (*env)->GetFieldID(env,lpCache->funcdescClass,"callconv","I");
    lpCache->cParams = (*env)->GetFieldID(env,lpCache->funcdescClass,"cParams","S");
    lpCache->cParamsOpt = (*env)->GetFieldID(env,lpCache->funcdescClass,"cParamsOpt","S");
    lpCache->oVft = (*env)->GetFieldID(env,lpCache->funcdescClass,"oVft","S");
    lpCache->cScodes = (*env)->GetFieldID(env,lpCache->funcdescClass,"cScodes","S");
    lpCache->elemdescFunc_tdesc_union = (*env)->GetFieldID(env,lpCache->funcdescClass,"elemdescFunc_tdesc_union","I");
    lpCache->elemdescFunc_tdesc_vt = (*env)->GetFieldID(env,lpCache->funcdescClass,"elemdescFunc_tdesc_vt","S");
    lpCache->elemdescFunc_tdesc_filler = (*env)->GetFieldID(env,lpCache->funcdescClass,"elemdescFunc_tdesc_filler","S");
    lpCache->elemdescFunc_idldesc_dwReserved = (*env)->GetFieldID(env,lpCache->funcdescClass,"elemdescFunc_idldesc_dwReserved","I");
    lpCache->elemdescFunc_idldesc_wIDLFlags = (*env)->GetFieldID(env,lpCache->funcdescClass,"elemdescFunc_idldesc_wIDLFlags","S");
    lpCache->elemdescFunc_idldesc_filler = (*env)->GetFieldID(env,lpCache->funcdescClass,"elemdescFunc_idldesc_filler","S");
    lpCache->wFuncFlags = (*env)->GetFieldID(env,lpCache->funcdescClass,"wFuncFlags","S");
    lpCache->cached = 1;
 }  
void cacheVardesc1Fids(JNIEnv *env, jobject lpVardesc, PVARDESC1_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->vardescClass = (*env)->GetObjectClass(env,lpVardesc);
    lpCache->memid = (*env)->GetFieldID(env,lpCache->vardescClass,"memid","I");
    lpCache->lpstrSchema = (*env)->GetFieldID(env,lpCache->vardescClass,"lpstrSchema","I");
    lpCache->unionField = (*env)->GetFieldID(env,lpCache->vardescClass,"unionField","I");
    lpCache->elemdescVar_tdesc_union = (*env)->GetFieldID(env,lpCache->vardescClass,"elemdescVar_tdesc_union","I");
    lpCache->elemdescVar_tdesc_vt = (*env)->GetFieldID(env,lpCache->vardescClass,"elemdescVar_tdesc_vt","S");
    lpCache->elemdescVar_tdesc_filler = (*env)->GetFieldID(env,lpCache->vardescClass,"elemdescVar_tdesc_filler","S");
    lpCache->elemdescVar_paramdesc_pparamdescex = (*env)->GetFieldID(env,lpCache->vardescClass,"elemdescVar_paramdesc_pparamdescex","I");
    lpCache->elemdescVar_paramdesc_wParamFlags = (*env)->GetFieldID(env,lpCache->vardescClass,"elemdescVar_paramdesc_wParamFlags","S");
    lpCache->elemdescVar_paramdesc_filler = (*env)->GetFieldID(env,lpCache->vardescClass,"elemdescVar_paramdesc_filler","S");
    lpCache->wVarFlags = (*env)->GetFieldID(env,lpCache->vardescClass,"wVarFlags","S");
    lpCache->filler = (*env)->GetFieldID(env,lpCache->vardescClass,"filler","S");
    lpCache->varkind = (*env)->GetFieldID(env,lpCache->vardescClass,"varkind","I");
    lpCache->cached = 1;
}

void cacheVardesc2Fids(JNIEnv *env, jobject lpVardesc, PVARDESC2_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->vardescClass = (*env)->GetObjectClass(env,lpVardesc);
    lpCache->memid = (*env)->GetFieldID(env,lpCache->vardescClass,"memid","I");
    lpCache->lpstrSchema = (*env)->GetFieldID(env,lpCache->vardescClass,"lpstrSchema","I");
    lpCache->unionField = (*env)->GetFieldID(env,lpCache->vardescClass,"unionField","I");
    lpCache->elemdescVar_tdesc_union = (*env)->GetFieldID(env,lpCache->vardescClass,"elemdescVar_tdesc_union","I");
    lpCache->elemdescVar_tdesc_vt = (*env)->GetFieldID(env,lpCache->vardescClass,"elemdescVar_tdesc_vt","S");
    lpCache->elemdescVar_tdesc_filler = (*env)->GetFieldID(env,lpCache->vardescClass,"elemdescVar_tdesc_filler","S");
    lpCache->elemdescVar_idldesc_dwReserved = (*env)->GetFieldID(env,lpCache->vardescClass,"elemdescVar_idldesc_dwReserved","I");
    lpCache->elemdescVar_idldesc_wIDLFlags = (*env)->GetFieldID(env,lpCache->vardescClass,"elemdescVar_idldesc_wIDLFlags","S");
    lpCache->elemdescVar_idldesc_filler = (*env)->GetFieldID(env,lpCache->vardescClass,"elemdescVar_idldesc_filler","S");
    lpCache->wVarFlags = (*env)->GetFieldID(env,lpCache->vardescClass,"wVarFlags","S");
    lpCache->filler = (*env)->GetFieldID(env,lpCache->vardescClass,"filler","S");
    lpCache->varkind = (*env)->GetFieldID(env,lpCache->vardescClass,"varkind","I");
    lpCache->cached = 1;
}
    
/* ----------- swt getters and setters  ----------- */
/**
 * These functions get or set object field ids assuming that the
 * fids for these objects have already been cached.
 *
 */
void getAccelFields(JNIEnv *env, jobject lpObject, ACCEL *lpAccel, ACCEL_FID_CACHE *lpAccelFc)
{
    lpAccel->fVirt = (*env)->GetByteField(env,lpObject,lpAccelFc->fVirt);
    lpAccel->key = (*env)->GetShortField(env,lpObject,lpAccelFc->key);
    lpAccel->cmd = (*env)->GetShortField(env,lpObject,lpAccelFc->cmd);
}

void setAccelFields(JNIEnv *env, jobject lpObject, ACCEL *lpAccel, ACCEL_FID_CACHE *lpAccelFc)
{
    (*env)->SetByteField(env,lpObject,lpAccelFc->fVirt, lpAccel->fVirt);
    (*env)->SetShortField(env,lpObject,lpAccelFc->key, lpAccel->key);
    (*env)->SetShortField(env,lpObject,lpAccelFc->cmd, lpAccel->cmd);
}

void getBitmapFields(JNIEnv *env, jobject lpObject, BITMAP *lpBitmap, BITMAP_FID_CACHE *lpBitmapFc)
{
    lpBitmap->bmType = (*env)->GetIntField(env,lpObject,lpBitmapFc->bmType);
    lpBitmap->bmWidth = (*env)->GetIntField(env,lpObject,lpBitmapFc->bmWidth);
    lpBitmap->bmHeight = (*env)->GetIntField(env,lpObject,lpBitmapFc->bmHeight);
    lpBitmap->bmWidthBytes = (*env)->GetIntField(env,lpObject,lpBitmapFc->bmWidthBytes);
    lpBitmap->bmPlanes = (*env)->GetShortField(env,lpObject,lpBitmapFc->bmPlanes);
    lpBitmap->bmBitsPixel = (*env)->GetShortField(env,lpObject,lpBitmapFc->bmBitsPixel);
    lpBitmap->bmBits = (void *)(*env)->GetIntField(env,lpObject,lpBitmapFc->bmBits);
}

void setBitmapFields(JNIEnv *env, jobject lpObject, BITMAP *lpBitmap, BITMAP_FID_CACHE *lpBitmapFc)
{
    (*env)->SetIntField(env,lpObject,lpBitmapFc->bmType, lpBitmap->bmType);
    (*env)->SetIntField(env,lpObject,lpBitmapFc->bmWidth, lpBitmap->bmWidth);
    (*env)->SetIntField(env,lpObject,lpBitmapFc->bmHeight, lpBitmap->bmHeight);
    (*env)->SetIntField(env,lpObject,lpBitmapFc->bmWidthBytes, lpBitmap->bmWidthBytes);
    (*env)->SetShortField(env,lpObject,lpBitmapFc->bmPlanes, lpBitmap->bmPlanes);
    (*env)->SetShortField(env,lpObject,lpBitmapFc->bmBitsPixel, lpBitmap->bmBitsPixel);
    (*env)->SetIntField(env,lpObject,lpBitmapFc->bmBits, (jint)lpBitmap->bmBits);
}

void getBrowseinfoFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpBrowseinfo, BROWSEINFO_FID_CACHE *lpBrowseinfoFc)
{
    lpBrowseinfo->hwndOwner = (HWND)(*env)->GetIntField(env,lpObject,lpBrowseinfoFc->hwndOwner);
    lpBrowseinfo->pidlRoot = (LPCITEMIDLIST)(*env)->GetIntField(env,lpObject,lpBrowseinfoFc->pidlRoot);
    lpBrowseinfo->pszDisplayName = (LPSTR)(*env)->GetIntField(env,lpObject,lpBrowseinfoFc->pszDisplayName);
    lpBrowseinfo->lpszTitle = (LPCSTR)(*env)->GetIntField(env,lpObject,lpBrowseinfoFc->lpszTitle);
    lpBrowseinfo->ulFlags = (*env)->GetIntField(env,lpObject,lpBrowseinfoFc->ulFlags);
    lpBrowseinfo->lpfn = (BFFCALLBACK)(*env)->GetIntField(env,lpObject,lpBrowseinfoFc->lpfn);
    lpBrowseinfo->lParam = (*env)->GetIntField(env,lpObject,lpBrowseinfoFc->lParam);
    lpBrowseinfo->iImage = (*env)->GetIntField(env,lpObject,lpBrowseinfoFc->iImage);
    
}

void setBrowseinfoFields(JNIEnv *env, jobject lpObject, BROWSEINFO *lpBrowseinfo, BROWSEINFO_FID_CACHE *lpBrowseinfoFc)
{
    (*env)->SetIntField(env,lpObject,lpBrowseinfoFc->hwndOwner, (jint)lpBrowseinfo->hwndOwner);
    (*env)->SetIntField(env,lpObject,lpBrowseinfoFc->pidlRoot, (jint)lpBrowseinfo->pidlRoot);
    (*env)->SetIntField(env,lpObject,lpBrowseinfoFc->pszDisplayName, (jint)lpBrowseinfo->pszDisplayName);
    (*env)->SetIntField(env,lpObject,lpBrowseinfoFc->lpszTitle, (jint)lpBrowseinfo->lpszTitle);
    (*env)->SetIntField(env,lpObject,lpBrowseinfoFc->ulFlags, lpBrowseinfo->ulFlags);
    (*env)->SetIntField(env,lpObject,lpBrowseinfoFc->lpfn, (jint)lpBrowseinfo->lpfn);
    (*env)->SetIntField(env,lpObject,lpBrowseinfoFc->lParam, lpBrowseinfo->lParam);
    (*env)->SetIntField(env,lpObject,lpBrowseinfoFc->iImage, lpBrowseinfo->iImage);
}
/*
void getCharformatFields(JNIEnv *env, jobject lpObject, CHARFORMAT *lpCharformat, CHARFORMAT_FID_CACHE *lpCharformatFc)
{
    lpCharformat->cbSize = (*env)->GetIntField(env,lpObject,lpCharformatFc->cbSize);
    lpCharformat->dwMask = (*env)->GetIntField(env,lpObject,lpCharformatFc->dwMask);
    lpCharformat->dwEffects = (*env)->GetIntField(env,lpObject,lpCharformatFc->dwEffects);
    lpCharformat->yHeight = (*env)->GetIntField(env,lpObject,lpCharformatFc->yHeight);
    lpCharformat->yOffset = (*env)->GetIntField(env,lpObject,lpCharformatFc->yOffset);
    lpCharformat->crTextColor = (*env)->GetIntField(env,lpObject,lpCharformatFc->crTextColor);
    lpCharformat->bCharSet = (*env)->GetByteField(env,lpObject,lpCharformatFc->bCharSet);
    lpCharformat->bPitchAndFamily = (*env)->GetByteField(env,lpObject,lpCharformatFc->bPitchAndFamily);
    lpCharformat->szFaceName[0] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName0);
    lpCharformat->szFaceName[1] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName1);
    lpCharformat->szFaceName[2] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName2);
    lpCharformat->szFaceName[3] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName3);
    lpCharformat->szFaceName[4] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName4);
    lpCharformat->szFaceName[5] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName5);
    lpCharformat->szFaceName[6] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName6);
    lpCharformat->szFaceName[7] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName7);
    lpCharformat->szFaceName[8] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName8);
    lpCharformat->szFaceName[9] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName9);
    lpCharformat->szFaceName[10] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName10);
    lpCharformat->szFaceName[11] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName11);
    lpCharformat->szFaceName[12] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName12);
    lpCharformat->szFaceName[13] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName13);
    lpCharformat->szFaceName[14] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName14);
    lpCharformat->szFaceName[15] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName15);
    lpCharformat->szFaceName[16] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName16);
    lpCharformat->szFaceName[17] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName17);
    lpCharformat->szFaceName[18] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName18);
    lpCharformat->szFaceName[19] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName19);
    lpCharformat->szFaceName[20] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName20);
    lpCharformat->szFaceName[21] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName21);
    lpCharformat->szFaceName[22] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName22);
    lpCharformat->szFaceName[23] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName23);
    lpCharformat->szFaceName[24] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName24);
    lpCharformat->szFaceName[25] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName25);
    lpCharformat->szFaceName[26] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName26);
    lpCharformat->szFaceName[27] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName27);
    lpCharformat->szFaceName[28] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName28);
    lpCharformat->szFaceName[29] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName29);
    lpCharformat->szFaceName[30] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName30);
    lpCharformat->szFaceName[31] = (*env)->GetByteField(env,lpObject,lpCharformatFc->szFaceName31);
}

void setCharformatFields(JNIEnv *env, jobject lpObject, CHARFORMAT *lpCharformat, CHARFORMAT_FID_CACHE *lpCharformatFc)
{
    (*env)->SetIntField(env,lpObject,lpCharformatFc->cbSize, lpCharformat->cbSize);
    (*env)->SetIntField(env,lpObject,lpCharformatFc->dwMask, lpCharformat->dwMask);
    (*env)->SetIntField(env,lpObject,lpCharformatFc->dwEffects, lpCharformat->dwEffects);
    (*env)->SetIntField(env,lpObject,lpCharformatFc->yHeight, lpCharformat->yHeight);
    (*env)->SetIntField(env,lpObject,lpCharformatFc->yOffset, lpCharformat->yOffset);
    (*env)->SetIntField(env,lpObject,lpCharformatFc->crTextColor, lpCharformat->crTextColor);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->bCharSet, lpCharformat->bCharSet);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->bPitchAndFamily, lpCharformat->bPitchAndFamily);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName0, lpCharformat->szFaceName[0]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName1, lpCharformat->szFaceName[1]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName2, lpCharformat->szFaceName[2]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName3, lpCharformat->szFaceName[3]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName4, lpCharformat->szFaceName[4]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName5, lpCharformat->szFaceName[5]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName6, lpCharformat->szFaceName[6]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName7, lpCharformat->szFaceName[7]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName8, lpCharformat->szFaceName[8]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName9, lpCharformat->szFaceName[9]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName10, lpCharformat->szFaceName[10]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName11, lpCharformat->szFaceName[11]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName12, lpCharformat->szFaceName[12]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName13, lpCharformat->szFaceName[13]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName14, lpCharformat->szFaceName[14]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName15, lpCharformat->szFaceName[15]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName16, lpCharformat->szFaceName[16]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName17, lpCharformat->szFaceName[17]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName18, lpCharformat->szFaceName[18]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName19, lpCharformat->szFaceName[19]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName20, lpCharformat->szFaceName[20]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName21, lpCharformat->szFaceName[21]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName22, lpCharformat->szFaceName[22]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName23, lpCharformat->szFaceName[23]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName24, lpCharformat->szFaceName[24]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName25, lpCharformat->szFaceName[25]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName26, lpCharformat->szFaceName[26]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName27, lpCharformat->szFaceName[27]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName28, lpCharformat->szFaceName[28]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName29, lpCharformat->szFaceName[29]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName30, lpCharformat->szFaceName[30]);
    (*env)->SetByteField(env,lpObject,lpCharformatFc->szFaceName31, lpCharformat->szFaceName[31]);
}
*/
/*
void getCharformat2Fields(JNIEnv *env, jobject lpObject, CHARFORMAT2 *lpCharformat2, CHARFORMAT2_FID_CACHE *lpCharformat2Fc)
{
    lpCharformat2->cbSize = (*env)->GetIntField(env,lpObject,lpCharformat2Fc->cbSize);
    lpCharformat2->dwMask = (*env)->GetIntField(env,lpObject,lpCharformat2Fc->dwMask);
    lpCharformat2->dwEffects = (*env)->GetIntField(env,lpObject,lpCharformat2Fc->dwEffects);
    lpCharformat2->yHeight = (*env)->GetIntField(env,lpObject,lpCharformat2Fc->yHeight);
    lpCharformat2->yOffset = (*env)->GetIntField(env,lpObject,lpCharformat2Fc->yOffset);
    lpCharformat2->crTextColor = (*env)->GetIntField(env,lpObject,lpCharformat2Fc->crTextColor);
    lpCharformat2->bCharSet = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->bCharSet);
    lpCharformat2->bPitchAndFamily = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->bPitchAndFamily);
    lpCharformat2->szFaceName[0] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName0);
    lpCharformat2->szFaceName[1] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName1);
    lpCharformat2->szFaceName[2] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName2);
    lpCharformat2->szFaceName[3] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName3);
    lpCharformat2->szFaceName[4] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName4);
    lpCharformat2->szFaceName[5] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName5);
    lpCharformat2->szFaceName[6] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName6);
    lpCharformat2->szFaceName[7] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName7);
    lpCharformat2->szFaceName[8] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName8);
    lpCharformat2->szFaceName[9] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName9);
    lpCharformat2->szFaceName[10] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName10);
    lpCharformat2->szFaceName[11] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName11);
    lpCharformat2->szFaceName[12] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName12);
    lpCharformat2->szFaceName[13] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName13);
    lpCharformat2->szFaceName[14] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName14);
    lpCharformat2->szFaceName[15] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName15);
    lpCharformat2->szFaceName[16] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName16);
    lpCharformat2->szFaceName[17] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName17);
    lpCharformat2->szFaceName[18] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName18);
    lpCharformat2->szFaceName[19] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName19);
    lpCharformat2->szFaceName[20] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName20);
    lpCharformat2->szFaceName[21] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName21);
    lpCharformat2->szFaceName[22] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName22);
    lpCharformat2->szFaceName[23] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName23);
    lpCharformat2->szFaceName[24] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName24);
    lpCharformat2->szFaceName[25] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName25);
    lpCharformat2->szFaceName[26] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName26);
    lpCharformat2->szFaceName[27] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName27);
    lpCharformat2->szFaceName[28] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName28);
    lpCharformat2->szFaceName[29] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName29);
    lpCharformat2->szFaceName[30] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName30);
    lpCharformat2->szFaceName[31] = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->szFaceName31);
    lpCharformat2->wWeight = (*env)->GetShortField(env,lpObject,lpCharformat2Fc->wWeight);
    lpCharformat2->sSpacing = (*env)->GetShortField(env,lpObject,lpCharformat2Fc->sSpacing);
    lpCharformat2->crBackColor = (*env)->GetIntField(env,lpObject,lpCharformat2Fc->crBackColor);
    lpCharformat2->lcid = (*env)->GetIntField(env,lpObject,lpCharformat2Fc->lcid);
    lpCharformat2->dwReserved = (*env)->GetIntField(env,lpObject,lpCharformat2Fc->dwReserved);
    lpCharformat2->sStyle = (*env)->GetShortField(env,lpObject,lpCharformat2Fc->sStyle);
    lpCharformat2->wKerning = (*env)->GetShortField(env,lpObject,lpCharformat2Fc->wKerning);
    lpCharformat2->bUnderlineType = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->bUnderlineType);
    lpCharformat2->bAnimation = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->bAnimation);
    lpCharformat2->bRevAuthor = (*env)->GetByteField(env,lpObject,lpCharformat2Fc->bRevAuthor);
}

void setCharformat2Fields(JNIEnv *env, jobject lpObject, CHARFORMAT2 *lpCharformat2, CHARFORMAT2_FID_CACHE *lpCharformat2Fc)
{
    (*env)->SetIntField(env,lpObject,lpCharformat2Fc->cbSize, lpCharformat2->cbSize);
    (*env)->SetIntField(env,lpObject,lpCharformat2Fc->dwMask, lpCharformat2->dwMask);
    (*env)->SetIntField(env,lpObject,lpCharformat2Fc->dwEffects, lpCharformat2->dwEffects);
    (*env)->SetIntField(env,lpObject,lpCharformat2Fc->yHeight, lpCharformat2->yHeight);
    (*env)->SetIntField(env,lpObject,lpCharformat2Fc->yOffset, lpCharformat2->yOffset);
    (*env)->SetIntField(env,lpObject,lpCharformat2Fc->crTextColor, lpCharformat2->crTextColor);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->bCharSet, lpCharformat2->bCharSet);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->bPitchAndFamily, lpCharformat2->bPitchAndFamily);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName0, lpCharformat2->szFaceName[0]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName1, lpCharformat2->szFaceName[1]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName2, lpCharformat2->szFaceName[2]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName3, lpCharformat2->szFaceName[3]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName4, lpCharformat2->szFaceName[4]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName5, lpCharformat2->szFaceName[5]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName6, lpCharformat2->szFaceName[6]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName7, lpCharformat2->szFaceName[7]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName8, lpCharformat2->szFaceName[8]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName9, lpCharformat2->szFaceName[9]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName10, lpCharformat2->szFaceName[10]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName11, lpCharformat2->szFaceName[11]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName12, lpCharformat2->szFaceName[12]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName13, lpCharformat2->szFaceName[13]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName14, lpCharformat2->szFaceName[14]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName15, lpCharformat2->szFaceName[15]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName16, lpCharformat2->szFaceName[16]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName17, lpCharformat2->szFaceName[17]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName18, lpCharformat2->szFaceName[18]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName19, lpCharformat2->szFaceName[19]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName20, lpCharformat2->szFaceName[20]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName21, lpCharformat2->szFaceName[21]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName22, lpCharformat2->szFaceName[22]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName23, lpCharformat2->szFaceName[23]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName24, lpCharformat2->szFaceName[24]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName25, lpCharformat2->szFaceName[25]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName26, lpCharformat2->szFaceName[26]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName27, lpCharformat2->szFaceName[27]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName28, lpCharformat2->szFaceName[28]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName29, lpCharformat2->szFaceName[29]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName30, lpCharformat2->szFaceName[30]);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->szFaceName31, lpCharformat2->szFaceName[31]);
    (*env)->SetShortField(env,lpObject,lpCharformat2Fc->wWeight, lpCharformat2->wWeight);
    (*env)->SetShortField(env,lpObject,lpCharformat2Fc->sSpacing, lpCharformat2->sSpacing);
    (*env)->SetIntField(env,lpObject,lpCharformat2Fc->crBackColor, lpCharformat2->crBackColor);
    (*env)->SetIntField(env,lpObject,lpCharformat2Fc->lcid, lpCharformat2->lcid);
    (*env)->SetIntField(env,lpObject,lpCharformat2Fc->dwReserved, lpCharformat2->dwReserved);
    (*env)->SetShortField(env,lpObject,lpCharformat2Fc->sStyle, lpCharformat2->sStyle);
    (*env)->SetShortField(env,lpObject,lpCharformat2Fc->wKerning, lpCharformat2->wKerning);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->bUnderlineType, lpCharformat2->bUnderlineType);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->bAnimation, lpCharformat2->bAnimation);
    (*env)->SetByteField(env,lpObject,lpCharformat2Fc->bRevAuthor, lpCharformat2->bRevAuthor);
}
*/
void getChoosecolorFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpChoosecolor, CHOOSECOLOR_FID_CACHE *lpChoosecolorFc)
{
    lpChoosecolor->lStructSize = (*env)->GetIntField(env,lpObject,lpChoosecolorFc->lStructSize);
    lpChoosecolor->hwndOwner = (HWND)(*env)->GetIntField(env,lpObject,lpChoosecolorFc->hwndOwner);
    lpChoosecolor->hInstance = (HWND)(*env)->GetIntField(env,lpObject,lpChoosecolorFc->hInstance);
    lpChoosecolor->rgbResult = (*env)->GetIntField(env,lpObject,lpChoosecolorFc->rgbResult);
    lpChoosecolor->lpCustColors = (COLORREF *)(*env)->GetIntField(env,lpObject,lpChoosecolorFc->lpCustColors);
    lpChoosecolor->Flags = (*env)->GetIntField(env,lpObject,lpChoosecolorFc->Flags);
    lpChoosecolor->lCustData = (*env)->GetIntField(env,lpObject,lpChoosecolorFc->lCustData);
    lpChoosecolor->lpfnHook = (LPCCHOOKPROC)(*env)->GetIntField(env,lpObject,lpChoosecolorFc->lpfnHook);
    lpChoosecolor->lpTemplateName = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpChoosecolorFc->lpTemplateName);
}

void setChoosecolorFields(JNIEnv *env, jobject lpObject, CHOOSECOLOR *lpChoosecolor, CHOOSECOLOR_FID_CACHE *lpChoosecolorFc)
{
    (*env)->SetIntField(env,lpObject,lpChoosecolorFc->lStructSize, lpChoosecolor->lStructSize);
    (*env)->SetIntField(env,lpObject,lpChoosecolorFc->hwndOwner, (jint)lpChoosecolor->hwndOwner);
    (*env)->SetIntField(env,lpObject,lpChoosecolorFc->hInstance, (jint)lpChoosecolor->hInstance);
    (*env)->SetIntField(env,lpObject,lpChoosecolorFc->rgbResult, lpChoosecolor->rgbResult);
    (*env)->SetIntField(env,lpObject,lpChoosecolorFc->lpCustColors, (jint)lpChoosecolor->lpCustColors);
    (*env)->SetIntField(env,lpObject,lpChoosecolorFc->Flags, lpChoosecolor->Flags);
    (*env)->SetIntField(env,lpObject,lpChoosecolorFc->lCustData, lpChoosecolor->lCustData);
    (*env)->SetIntField(env,lpObject,lpChoosecolorFc->lpfnHook, (jint)lpChoosecolor->lpfnHook);
    (*env)->SetIntField(env,lpObject,lpChoosecolorFc->lpTemplateName, (jint)lpChoosecolor->lpTemplateName);
}

void getCompositionformFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpCompositionform, COMPOSITIONFORM_FID_CACHE *lpCompositionformFc)
{
    lpCompositionform->dwStyle = (*env)->GetIntField(env,lpObject,lpCompositionformFc->dwStyle);
    lpCompositionform->ptCurrentPos.x = (*env)->GetIntField(env,lpObject,lpCompositionformFc->x);
    lpCompositionform->ptCurrentPos.y = (*env)->GetIntField(env,lpObject,lpCompositionformFc->y);
    lpCompositionform->rcArea.left = (*env)->GetIntField(env,lpObject,lpCompositionformFc->left);
    lpCompositionform->rcArea.top = (*env)->GetIntField(env,lpObject,lpCompositionformFc->top);
    lpCompositionform->rcArea.right = (*env)->GetIntField(env,lpObject,lpCompositionformFc->right);
    lpCompositionform->rcArea.bottom = (*env)->GetIntField(env,lpObject,lpCompositionformFc->bottom);
}

void setCompositionformFields(JNIEnv *env, jobject lpObject, COMPOSITIONFORM *lpCompositionform, COMPOSITIONFORM_FID_CACHE *lpCompositionformFc)
{
    (*env)->SetIntField(env,lpObject,lpCompositionformFc->dwStyle, lpCompositionform->dwStyle);
    (*env)->SetIntField(env,lpObject,lpCompositionformFc->x, lpCompositionform->ptCurrentPos.x);
    (*env)->SetIntField(env,lpObject,lpCompositionformFc->y, lpCompositionform->ptCurrentPos.y);
    (*env)->SetIntField(env,lpObject,lpCompositionformFc->left, lpCompositionform->rcArea.left);
    (*env)->SetIntField(env,lpObject,lpCompositionformFc->top, lpCompositionform->rcArea.top);
    (*env)->SetIntField(env,lpObject,lpCompositionformFc->right, lpCompositionform->rcArea.right);
    (*env)->SetIntField(env,lpObject,lpCompositionformFc->bottom, lpCompositionform->rcArea.bottom);
}

void getCreatestructFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpCreatestruct, CREATESTRUCT_FID_CACHE *lpCreatestructFc)
{
    lpCreatestruct->lpCreateParams = (void *)(*env)->GetIntField(env,lpObject,lpCreatestructFc->lpCreateParams);
    lpCreatestruct->hInstance = (HINSTANCE)(*env)->GetIntField(env,lpObject,lpCreatestructFc->hInstance);
    lpCreatestruct->hMenu = (HMENU)(*env)->GetIntField(env,lpObject,lpCreatestructFc->hMenu);
    lpCreatestruct->hwndParent = (HWND)(*env)->GetIntField(env,lpObject,lpCreatestructFc->hwndParent);
    lpCreatestruct->cx = (*env)->GetIntField(env,lpObject,lpCreatestructFc->cx);    	
    lpCreatestruct->cy = (*env)->GetIntField(env,lpObject,lpCreatestructFc->cy);
    lpCreatestruct->x = (*env)->GetIntField(env,lpObject,lpCreatestructFc->x);
    lpCreatestruct->y = (*env)->GetIntField(env,lpObject,lpCreatestructFc->y);
    lpCreatestruct->style = (*env)->GetIntField(env,lpObject,lpCreatestructFc->style);    	
    lpCreatestruct->lpszName = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpCreatestructFc->lpszName);
    lpCreatestruct->lpszClass = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpCreatestructFc->lpszClass);
    lpCreatestruct->dwExStyle = (*env)->GetIntField(env,lpObject,lpCreatestructFc->dwExStyle);
}

void setCreatestructFields(JNIEnv *env, jobject lpObject, CREATESTRUCT *lpCreatestruct, CREATESTRUCT_FID_CACHE *lpCreatestructFc)
{
    (*env)->SetIntField(env,lpObject,lpCreatestructFc->lpCreateParams, (jint)lpCreatestruct->lpCreateParams);
    (*env)->SetIntField(env,lpObject,lpCreatestructFc->hInstance, (jint)lpCreatestruct->hInstance);
    (*env)->SetIntField(env,lpObject,lpCreatestructFc->hMenu, (jint)lpCreatestruct->hMenu);
    (*env)->SetIntField(env,lpObject,lpCreatestructFc->hwndParent, (jint)lpCreatestruct->hwndParent);
    (*env)->SetIntField(env,lpObject,lpCreatestructFc->cx, lpCreatestruct->cx);
    (*env)->SetIntField(env,lpObject,lpCreatestructFc->cy, lpCreatestruct->cy);
    (*env)->SetIntField(env,lpObject,lpCreatestructFc->x, lpCreatestruct->x);
    (*env)->SetIntField(env,lpObject,lpCreatestructFc->y, lpCreatestruct->y);
    (*env)->SetIntField(env,lpObject,lpCreatestructFc->style, lpCreatestruct->style);
    (*env)->SetIntField(env,lpObject,lpCreatestructFc->lpszName, (jint)lpCreatestruct->lpszName);
    (*env)->SetIntField(env,lpObject,lpCreatestructFc->lpszClass, (jint)lpCreatestruct->lpszClass);
    (*env)->SetIntField(env,lpObject,lpCreatestructFc->dwExStyle, lpCreatestruct->dwExStyle);
}

void getDibsectionFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpDibsection, DIBSECTION_FID_CACHE *lpDibsectionFc)
{
    lpDibsection->dsBm.bmType = (*env)->GetIntField(env,lpObject,lpDibsectionFc->bmType);
    lpDibsection->dsBm.bmWidth = (*env)->GetIntField(env,lpObject,lpDibsectionFc->bmWidth);
    lpDibsection->dsBm.bmHeight = (*env)->GetIntField(env,lpObject,lpDibsectionFc->bmHeight);
    lpDibsection->dsBm.bmWidthBytes = (*env)->GetIntField(env,lpObject,lpDibsectionFc->bmWidthBytes);
    lpDibsection->dsBm.bmPlanes = (*env)->GetShortField(env,lpObject,lpDibsectionFc->bmPlanes);
    lpDibsection->dsBm.bmBitsPixel = (*env)->GetShortField(env,lpObject,lpDibsectionFc->bmBitsPixel);
    lpDibsection->dsBm.bmBits = (void *)(*env)->GetIntField(env,lpObject,lpDibsectionFc->bmBits);

    lpDibsection->dsBmih.biSize = (*env)->GetIntField(env,lpObject,lpDibsectionFc->biSize);
    lpDibsection->dsBmih.biWidth = (*env)->GetIntField(env,lpObject,lpDibsectionFc->biWidth);
    lpDibsection->dsBmih.biHeight = (*env)->GetIntField(env,lpObject,lpDibsectionFc->biHeight);
    lpDibsection->dsBmih.biPlanes = (*env)->GetShortField(env,lpObject,lpDibsectionFc->biPlanes);
    lpDibsection->dsBmih.biBitCount = (*env)->GetShortField(env,lpObject,lpDibsectionFc->biBitCount);
    lpDibsection->dsBmih.biCompression = (*env)->GetIntField(env,lpObject,lpDibsectionFc->biCompression);
    lpDibsection->dsBmih.biSizeImage = (*env)->GetIntField(env,lpObject,lpDibsectionFc->biSizeImage);
    lpDibsection->dsBmih.biXPelsPerMeter = (*env)->GetIntField(env,lpObject,lpDibsectionFc->biXPelsPerMeter);
    lpDibsection->dsBmih.biYPelsPerMeter = (*env)->GetIntField(env,lpObject,lpDibsectionFc->biYPelsPerMeter);
    lpDibsection->dsBmih.biClrUsed = (*env)->GetIntField(env,lpObject,lpDibsectionFc->biClrUsed);
    lpDibsection->dsBmih.biClrImportant = (*env)->GetIntField(env,lpObject,lpDibsectionFc->biClrImportant);

    lpDibsection->dsBitfields[0] = (*env)->GetIntField(env,lpObject,lpDibsectionFc->dsBitfields0);
    lpDibsection->dsBitfields[1] = (*env)->GetIntField(env,lpObject,lpDibsectionFc->dsBitfields1);
    lpDibsection->dsBitfields[2] = (*env)->GetIntField(env,lpObject,lpDibsectionFc->dsBitfields2);
    lpDibsection->dshSection = (HANDLE)(*env)->GetIntField(env,lpObject,lpDibsectionFc->dshSection);
    lpDibsection->dsOffset = (*env)->GetIntField(env,lpObject,lpDibsectionFc->dsOffset);
}
	
void setDibsectionFields(JNIEnv *env, jobject lpObject, DIBSECTION *lpDibsection, DIBSECTION_FID_CACHE *lpDibsectionFc)
{
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->bmType, lpDibsection->dsBm.bmType);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->bmWidth, lpDibsection->dsBm.bmWidth);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->bmHeight, lpDibsection->dsBm.bmHeight);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->bmWidthBytes, lpDibsection->dsBm.bmWidthBytes);
    (*env)->SetShortField(env,lpObject,lpDibsectionFc->bmPlanes, lpDibsection->dsBm.bmPlanes);
    (*env)->SetShortField(env,lpObject,lpDibsectionFc->bmBitsPixel, lpDibsection->dsBm.bmBitsPixel);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->bmBits, (jint)lpDibsection->dsBm.bmBits);

    (*env)->SetIntField(env,lpObject,lpDibsectionFc->biSize, lpDibsection->dsBmih.biSize);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->biWidth, lpDibsection->dsBmih.biWidth);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->biHeight, lpDibsection->dsBmih.biHeight);
    (*env)->SetShortField(env,lpObject,lpDibsectionFc->biPlanes, lpDibsection->dsBmih.biPlanes);
    (*env)->SetShortField(env,lpObject,lpDibsectionFc->biBitCount, lpDibsection->dsBmih.biBitCount);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->biCompression, lpDibsection->dsBmih.biCompression);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->biSizeImage, lpDibsection->dsBmih.biSizeImage);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->biXPelsPerMeter, lpDibsection->dsBmih.biXPelsPerMeter);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->biYPelsPerMeter, lpDibsection->dsBmih.biYPelsPerMeter);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->biClrUsed, lpDibsection->dsBmih.biClrUsed);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->biClrImportant, lpDibsection->dsBmih.biClrImportant);

    (*env)->SetIntField(env,lpObject,lpDibsectionFc->dsBitfields0, lpDibsection->dsBitfields[0]);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->dsBitfields1, lpDibsection->dsBitfields[1]);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->dsBitfields2, lpDibsection->dsBitfields[2]);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->dshSection, (jint)lpDibsection->dshSection);
    (*env)->SetIntField(env,lpObject,lpDibsectionFc->dsOffset, lpDibsection->dsOffset);
}

void getDllversioninfoFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpDllversioninfo, DLLVERSIONINFO_FID_CACHE *lpDllversioninfoFc)
{
    lpDllversioninfo->cbSize = (*env)->GetIntField(env,lpObject,lpDllversioninfoFc->cbSize);
    lpDllversioninfo->dwMajorVersion = (*env)->GetIntField(env,lpObject,lpDllversioninfoFc->dwMajorVersion);
    lpDllversioninfo->dwMinorVersion = (*env)->GetIntField(env,lpObject,lpDllversioninfoFc->dwMinorVersion);
    lpDllversioninfo->dwBuildNumber = (*env)->GetIntField(env,lpObject,lpDllversioninfoFc->dwBuildNumber);
    lpDllversioninfo->dwPlatformID = (*env)->GetIntField(env,lpObject,lpDllversioninfoFc->dwPlatformID);
}

void setDllversioninfoFields(JNIEnv *env, jobject lpObject, DLLVERSIONINFO *lpDllversioninfo, DLLVERSIONINFO_FID_CACHE *lpDllversioninfoFc)
{
    (*env)->SetIntField(env,lpObject,lpDllversioninfoFc->cbSize, lpDllversioninfo->cbSize);
    (*env)->SetIntField(env,lpObject,lpDllversioninfoFc->dwMajorVersion, lpDllversioninfo->dwMajorVersion);
    (*env)->SetIntField(env,lpObject,lpDllversioninfoFc->dwMinorVersion, lpDllversioninfo->dwMinorVersion);
    (*env)->SetIntField(env,lpObject,lpDllversioninfoFc->dwBuildNumber, lpDllversioninfo->dwBuildNumber);
    (*env)->SetIntField(env,lpObject,lpDllversioninfoFc->dwPlatformID, lpDllversioninfo->dwPlatformID);
}

void getDrawitemstructFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpDrawitemstruct, DRAWITEMSTRUCT_FID_CACHE *lpDrawitemstructFc)
{
    lpDrawitemstruct->CtlType = (*env)->GetIntField(env,lpObject,lpDrawitemstructFc->CtlType);
    lpDrawitemstruct->CtlID = (*env)->GetIntField(env,lpObject,lpDrawitemstructFc->CtlID);
    lpDrawitemstruct->itemID = (*env)->GetIntField(env,lpObject,lpDrawitemstructFc->itemID);
    lpDrawitemstruct->itemAction = (*env)->GetIntField(env,lpObject,lpDrawitemstructFc->itemAction);
    lpDrawitemstruct->itemState = (*env)->GetIntField(env,lpObject,lpDrawitemstructFc->itemState);
    lpDrawitemstruct->hwndItem = (HWND)(*env)->GetIntField(env,lpObject,lpDrawitemstructFc->hwndItem);
    lpDrawitemstruct->hDC = (HDC)(*env)->GetIntField(env,lpObject,lpDrawitemstructFc->hDC);
    lpDrawitemstruct->rcItem.left = (*env)->GetIntField(env,lpObject,lpDrawitemstructFc->left);
    lpDrawitemstruct->rcItem.top = (*env)->GetIntField(env,lpObject,lpDrawitemstructFc->top);
    lpDrawitemstruct->rcItem.right = (*env)->GetIntField(env,lpObject,lpDrawitemstructFc->right);
    lpDrawitemstruct->rcItem.bottom = (*env)->GetIntField(env,lpObject,lpDrawitemstructFc->bottom);
    lpDrawitemstruct->itemData = (*env)->GetIntField(env,lpObject,lpDrawitemstructFc->itemData);
}

void setDrawitemstructFields(JNIEnv *env, jobject lpObject, DRAWITEMSTRUCT *lpDrawitemstruct, DRAWITEMSTRUCT_FID_CACHE *lpDrawitemstructFc)
{
    (*env)->SetIntField(env,lpObject,lpDrawitemstructFc->CtlType, lpDrawitemstruct->CtlType);
    (*env)->SetIntField(env,lpObject,lpDrawitemstructFc->CtlID, lpDrawitemstruct->CtlID);
    (*env)->SetIntField(env,lpObject,lpDrawitemstructFc->itemID, lpDrawitemstruct->itemID);
    (*env)->SetIntField(env,lpObject,lpDrawitemstructFc->itemAction, lpDrawitemstruct->itemAction);
    (*env)->SetIntField(env,lpObject,lpDrawitemstructFc->itemState, lpDrawitemstruct->itemState);
    (*env)->SetIntField(env,lpObject,lpDrawitemstructFc->hwndItem, (jint)lpDrawitemstruct->hwndItem);
    (*env)->SetIntField(env,lpObject,lpDrawitemstructFc->hDC, (jint)lpDrawitemstruct->hDC);
    (*env)->SetIntField(env,lpObject,lpDrawitemstructFc->left, lpDrawitemstruct->rcItem.left);
    (*env)->SetIntField(env,lpObject,lpDrawitemstructFc->top, lpDrawitemstruct->rcItem.top);
    (*env)->SetIntField(env,lpObject,lpDrawitemstructFc->right, lpDrawitemstruct->rcItem.right);
    (*env)->SetIntField(env,lpObject,lpDrawitemstructFc->bottom, lpDrawitemstruct->rcItem.bottom);
    (*env)->SetIntField(env,lpObject,lpDrawitemstructFc->itemData, lpDrawitemstruct->itemData);
}

void getHditemFields(JNIEnv *env, jobject lpObject, HDITEM *lpHditem, HDITEM_FID_CACHE *lpHditemFc)
{
    lpHditem->mask = (*env)->GetIntField(env,lpObject,lpHditemFc->mask);
    lpHditem->cxy = (*env)->GetIntField(env,lpObject,lpHditemFc->cxy);
    lpHditem->pszText = (LPTSTR)(*env)->GetIntField(env,lpObject,lpHditemFc->pszText);
    lpHditem->hbm = (HBITMAP)(*env)->GetIntField(env,lpObject,lpHditemFc->hbm);
    lpHditem->cchTextMax = (*env)->GetIntField(env,lpObject,lpHditemFc->cchTextMax);
    lpHditem->fmt = (*env)->GetIntField(env,lpObject,lpHditemFc->fmt);
    lpHditem->lParam = (*env)->GetIntField(env,lpObject,lpHditemFc->lParam);
    lpHditem->iImage = (*env)->GetIntField(env,lpObject,lpHditemFc->iImage);
    lpHditem->iOrder = (*env)->GetIntField(env,lpObject,lpHditemFc->iOrder);
}

void setHditemFields(JNIEnv *env, jobject lpObject, HDITEM *lpHditem, HDITEM_FID_CACHE *lpHditemFc)
{
    (*env)->SetIntField(env,lpObject,lpHditemFc->mask, lpHditem->mask);
    (*env)->SetIntField(env,lpObject,lpHditemFc->cxy, lpHditem->cxy);
    (*env)->SetIntField(env,lpObject,lpHditemFc->pszText, (jint)lpHditem->pszText);
    (*env)->SetIntField(env,lpObject,lpHditemFc->hbm, (jint)lpHditem->hbm);
    (*env)->SetIntField(env,lpObject,lpHditemFc->cchTextMax, lpHditem->cchTextMax);
    (*env)->SetIntField(env,lpObject,lpHditemFc->fmt, lpHditem->fmt);
    (*env)->SetIntField(env,lpObject,lpHditemFc->lParam, lpHditem->lParam);
    (*env)->SetIntField(env,lpObject,lpHditemFc->iImage, lpHditem->iImage);
    (*env)->SetIntField(env,lpObject,lpHditemFc->iOrder, lpHditem->iOrder);
}

void getHdlayoutFields(JNIEnv *env, jobject lpObject, HDLAYOUT *lpHdlayout, HDLAYOUT_FID_CACHE *lpHdlayoutFc)
{
    lpHdlayout->prc = (RECT FAR *)(*env)->GetIntField(env,lpObject,lpHdlayoutFc->prc);
    lpHdlayout->pwpos = (WINDOWPOS FAR *)(*env)->GetIntField(env,lpObject,lpHdlayoutFc->pwpos);
}

void setHdlayoutFields(JNIEnv *env, jobject lpObject, HDLAYOUT *lpHdlayout, HDLAYOUT_FID_CACHE *lpHdlayoutFc)
{
    (*env)->SetIntField(env,lpObject,lpHdlayoutFc->prc, (jint)lpHdlayout->prc);
    (*env)->SetIntField(env,lpObject,lpHdlayoutFc->pwpos, (jint)lpHdlayout->pwpos);
}

void getIconinfoFields(JNIEnv *env, jobject lpObject, ICONINFO *lpIconinfo, ICONINFO_FID_CACHE *lpIconinfoFc)
{
    lpIconinfo->fIcon = (*env)->GetBooleanField(env,lpObject,lpIconinfoFc->fIcon);
    lpIconinfo->xHotspot = (*env)->GetIntField(env,lpObject,lpIconinfoFc->xHotspot);
    lpIconinfo->yHotspot = (*env)->GetIntField(env,lpObject,lpIconinfoFc->yHotspot);
    lpIconinfo->hbmMask = (HBITMAP)(*env)->GetIntField(env,lpObject,lpIconinfoFc->hbmMask);
    lpIconinfo->hbmColor = (HBITMAP)(*env)->GetIntField(env,lpObject,lpIconinfoFc->hbmColor);
}

void setIconinfoFields(JNIEnv *env, jobject lpObject, ICONINFO *lpIconinfo, ICONINFO_FID_CACHE *lpIconinfoFc)
{
    (*env)->SetBooleanField(env,lpObject,lpIconinfoFc->fIcon, (jboolean)lpIconinfo->fIcon);
    (*env)->SetIntField(env,lpObject,lpIconinfoFc->xHotspot, lpIconinfo->xHotspot);
    (*env)->SetIntField(env,lpObject,lpIconinfoFc->yHotspot, lpIconinfo->yHotspot);
    (*env)->SetIntField(env,lpObject,lpIconinfoFc->hbmMask, (jint)lpIconinfo->hbmMask);
    (*env)->SetIntField(env,lpObject,lpIconinfoFc->hbmColor, (jint)lpIconinfo->hbmColor);
}

void getInitcommoncontrolsexFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpInitcommoncontrolsex, INITCOMMONCONTROLSEX_FID_CACHE *lpInitcommoncontrolsexFc)
{
    lpInitcommoncontrolsex->dwSize = (*env)->GetIntField(env,lpObject,lpInitcommoncontrolsexFc->dwSize);
    lpInitcommoncontrolsex->dwICC = (*env)->GetIntField(env,lpObject,lpInitcommoncontrolsexFc->dwICC);
}

void setInitcommoncontrolsexFields(JNIEnv *env, jobject lpObject, INITCOMMONCONTROLSEX *lpInitcommoncontrolsex, INITCOMMONCONTROLSEX_FID_CACHE *lpInitcommoncontrolsexFc)
{
    (*env)->SetIntField(env,lpObject,lpInitcommoncontrolsexFc->dwSize, lpInitcommoncontrolsex->dwSize);
    (*env)->SetIntField(env,lpObject,lpInitcommoncontrolsexFc->dwICC, lpInitcommoncontrolsex->dwICC);
}

void getLogbrushFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpLogbrush, LOGBRUSH_FID_CACHE *lpLogbrushFc)
{
    lpLogbrush->lbStyle = (*env)->GetIntField(env,lpObject,lpLogbrushFc->lbStyle);
    lpLogbrush->lbColor = (*env)->GetIntField(env,lpObject,lpLogbrushFc->lbColor);
    lpLogbrush->lbHatch = (*env)->GetIntField(env,lpObject,lpLogbrushFc->lbHatch);
}

void setLogbrushFields(JNIEnv *env, jobject lpObject, LOGBRUSH *lpLogbrush, LOGBRUSH_FID_CACHE *lpLogbrushFc)
{
    (*env)->SetIntField(env,lpObject,lpLogbrushFc->lbStyle, lpLogbrush->lbStyle);
    (*env)->SetIntField(env,lpObject,lpLogbrushFc->lbColor, lpLogbrush->lbColor);
    (*env)->SetIntField(env,lpObject,lpLogbrushFc->lbHatch, lpLogbrush->lbHatch);
}

void getLogfontFieldsA(JNIEnv *env, jobject lpObject, LOGFONT *lpLogfont, LOGFONT_FID_CACHE *lpLogfontFc)
{
    lpLogfont->lfHeight = (*env)->GetIntField(env,lpObject,lpLogfontFc->lfHeight);
    lpLogfont->lfWidth = (*env)->GetIntField(env,lpObject,lpLogfontFc->lfWidth);
    lpLogfont->lfEscapement = (*env)->GetIntField(env,lpObject,lpLogfontFc->lfEscapement);
    lpLogfont->lfOrientation = (*env)->GetIntField(env,lpObject,lpLogfontFc->lfOrientation);
    lpLogfont->lfWeight = (*env)->GetIntField(env,lpObject,lpLogfontFc->lfWeight);
    lpLogfont->lfItalic = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfItalic);
    lpLogfont->lfUnderline = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfUnderline);
    lpLogfont->lfStrikeOut = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfStrikeOut);
    lpLogfont->lfCharSet = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfCharSet);
    lpLogfont->lfOutPrecision = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfOutPrecision);
    lpLogfont->lfClipPrecision = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfClipPrecision);
    lpLogfont->lfQuality = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfQuality);
    lpLogfont->lfPitchAndFamily = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfPitchAndFamily);
    lpLogfont->lfFaceName[0] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName0);
    lpLogfont->lfFaceName[1] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName1);
    lpLogfont->lfFaceName[2] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName2);
    lpLogfont->lfFaceName[3] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName3);
    lpLogfont->lfFaceName[4] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName4);
    lpLogfont->lfFaceName[5] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName5);
    lpLogfont->lfFaceName[6] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName6);
    lpLogfont->lfFaceName[7] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName7);
    lpLogfont->lfFaceName[8] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName8);
    lpLogfont->lfFaceName[9] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName9);
    lpLogfont->lfFaceName[10] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName10);
    lpLogfont->lfFaceName[11] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName11);
    lpLogfont->lfFaceName[12] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName12);
    lpLogfont->lfFaceName[13] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName13);
    lpLogfont->lfFaceName[14] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName14);
    lpLogfont->lfFaceName[15] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName15);
    lpLogfont->lfFaceName[16] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName16);
    lpLogfont->lfFaceName[17] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName17);
    lpLogfont->lfFaceName[18] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName18);
    lpLogfont->lfFaceName[19] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName19);
    lpLogfont->lfFaceName[20] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName20);
    lpLogfont->lfFaceName[21] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName21);
    lpLogfont->lfFaceName[22] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName22);
    lpLogfont->lfFaceName[23] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName23);
    lpLogfont->lfFaceName[24] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName24);
    lpLogfont->lfFaceName[25] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName25);
    lpLogfont->lfFaceName[26] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName26);
    lpLogfont->lfFaceName[27] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName27);
    lpLogfont->lfFaceName[28] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName28);
    lpLogfont->lfFaceName[29] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName29);
    lpLogfont->lfFaceName[30] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName30);
    lpLogfont->lfFaceName[31] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName31);
}
void getLogfontFieldsW(JNIEnv *env, jobject lpObject, LOGFONTW *lpLogfont, LOGFONT_FID_CACHE *lpLogfontFc)
{
    lpLogfont->lfHeight = (*env)->GetIntField(env,lpObject,lpLogfontFc->lfHeight);
    lpLogfont->lfWidth = (*env)->GetIntField(env,lpObject,lpLogfontFc->lfWidth);
    lpLogfont->lfEscapement = (*env)->GetIntField(env,lpObject,lpLogfontFc->lfEscapement);
    lpLogfont->lfOrientation = (*env)->GetIntField(env,lpObject,lpLogfontFc->lfOrientation);
    lpLogfont->lfWeight = (*env)->GetIntField(env,lpObject,lpLogfontFc->lfWeight);
    lpLogfont->lfItalic = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfItalic);
    lpLogfont->lfUnderline = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfUnderline);
    lpLogfont->lfStrikeOut = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfStrikeOut);
    lpLogfont->lfCharSet = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfCharSet);
    lpLogfont->lfOutPrecision = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfOutPrecision);
    lpLogfont->lfClipPrecision = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfClipPrecision);
    lpLogfont->lfQuality = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfQuality);
    lpLogfont->lfPitchAndFamily = (*env)->GetByteField(env,lpObject,lpLogfontFc->lfPitchAndFamily);
    lpLogfont->lfFaceName[0] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName0);
    lpLogfont->lfFaceName[1] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName1);
    lpLogfont->lfFaceName[2] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName2);
    lpLogfont->lfFaceName[3] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName3);
    lpLogfont->lfFaceName[4] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName4);
    lpLogfont->lfFaceName[5] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName5);
    lpLogfont->lfFaceName[6] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName6);
    lpLogfont->lfFaceName[7] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName7);
    lpLogfont->lfFaceName[8] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName8);
    lpLogfont->lfFaceName[9] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName9);
    lpLogfont->lfFaceName[10] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName10);
    lpLogfont->lfFaceName[11] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName11);
    lpLogfont->lfFaceName[12] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName12);
    lpLogfont->lfFaceName[13] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName13);
    lpLogfont->lfFaceName[14] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName14);
    lpLogfont->lfFaceName[15] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName15);
    lpLogfont->lfFaceName[16] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName16);
    lpLogfont->lfFaceName[17] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName17);
    lpLogfont->lfFaceName[18] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName18);
    lpLogfont->lfFaceName[19] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName19);
    lpLogfont->lfFaceName[20] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName20);
    lpLogfont->lfFaceName[21] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName21);
    lpLogfont->lfFaceName[22] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName22);
    lpLogfont->lfFaceName[23] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName23);
    lpLogfont->lfFaceName[24] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName24);
    lpLogfont->lfFaceName[25] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName25);
    lpLogfont->lfFaceName[26] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName26);
    lpLogfont->lfFaceName[27] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName27);
    lpLogfont->lfFaceName[28] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName28);
    lpLogfont->lfFaceName[29] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName29);
    lpLogfont->lfFaceName[30] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName30);
    lpLogfont->lfFaceName[31] = (*env)->GetCharField(env,lpObject,lpLogfontFc->lfFaceName31);
}

void setLogfontFieldsA(JNIEnv *env, jobject lpObject, LOGFONT *lpLogfont, LOGFONT_FID_CACHE *lpLogfontFc)
{
    (*env)->SetIntField(env,lpObject,lpLogfontFc->lfHeight, lpLogfont->lfHeight);
    (*env)->SetIntField(env,lpObject,lpLogfontFc->lfWidth, lpLogfont->lfWidth);
    (*env)->SetIntField(env,lpObject,lpLogfontFc->lfEscapement, lpLogfont->lfEscapement);
    (*env)->SetIntField(env,lpObject,lpLogfontFc->lfOrientation, lpLogfont->lfOrientation);
    (*env)->SetIntField(env,lpObject,lpLogfontFc->lfWeight, lpLogfont->lfWeight);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfItalic, lpLogfont->lfItalic);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfUnderline, lpLogfont->lfUnderline);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfStrikeOut, lpLogfont->lfStrikeOut);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfCharSet, lpLogfont->lfCharSet);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfOutPrecision, lpLogfont->lfOutPrecision);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfClipPrecision, lpLogfont->lfClipPrecision);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfQuality, lpLogfont->lfQuality);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfPitchAndFamily, lpLogfont->lfPitchAndFamily);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName0, lpLogfont->lfFaceName[0]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName1, lpLogfont->lfFaceName[1]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName2, lpLogfont->lfFaceName[2]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName3, lpLogfont->lfFaceName[3]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName4, lpLogfont->lfFaceName[4]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName5, lpLogfont->lfFaceName[5]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName6, lpLogfont->lfFaceName[6]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName7, lpLogfont->lfFaceName[7]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName8, lpLogfont->lfFaceName[8]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName9, lpLogfont->lfFaceName[9]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName10, lpLogfont->lfFaceName[10]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName11, lpLogfont->lfFaceName[11]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName12, lpLogfont->lfFaceName[12]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName13, lpLogfont->lfFaceName[13]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName14, lpLogfont->lfFaceName[14]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName15, lpLogfont->lfFaceName[15]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName16, lpLogfont->lfFaceName[16]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName17, lpLogfont->lfFaceName[17]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName18, lpLogfont->lfFaceName[18]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName19, lpLogfont->lfFaceName[19]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName20, lpLogfont->lfFaceName[20]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName21, lpLogfont->lfFaceName[21]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName22, lpLogfont->lfFaceName[22]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName23, lpLogfont->lfFaceName[23]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName24, lpLogfont->lfFaceName[24]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName25, lpLogfont->lfFaceName[25]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName26, lpLogfont->lfFaceName[26]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName27, lpLogfont->lfFaceName[27]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName28, lpLogfont->lfFaceName[28]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName29, lpLogfont->lfFaceName[29]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName30, lpLogfont->lfFaceName[30]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName31, lpLogfont->lfFaceName[31]);
}
void setLogfontFieldsW(JNIEnv *env, jobject lpObject, LOGFONTW *lpLogfont, LOGFONT_FID_CACHE *lpLogfontFc)
{
    (*env)->SetIntField(env,lpObject,lpLogfontFc->lfHeight, lpLogfont->lfHeight);
    (*env)->SetIntField(env,lpObject,lpLogfontFc->lfWidth, lpLogfont->lfWidth);
    (*env)->SetIntField(env,lpObject,lpLogfontFc->lfEscapement, lpLogfont->lfEscapement);
    (*env)->SetIntField(env,lpObject,lpLogfontFc->lfOrientation, lpLogfont->lfOrientation);
    (*env)->SetIntField(env,lpObject,lpLogfontFc->lfWeight, lpLogfont->lfWeight);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfItalic, lpLogfont->lfItalic);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfUnderline, lpLogfont->lfUnderline);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfStrikeOut, lpLogfont->lfStrikeOut);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfCharSet, lpLogfont->lfCharSet);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfOutPrecision, lpLogfont->lfOutPrecision);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfClipPrecision, lpLogfont->lfClipPrecision);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfQuality, lpLogfont->lfQuality);
    (*env)->SetByteField(env,lpObject,lpLogfontFc->lfPitchAndFamily, lpLogfont->lfPitchAndFamily);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName0, lpLogfont->lfFaceName[0]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName1, lpLogfont->lfFaceName[1]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName2, lpLogfont->lfFaceName[2]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName3, lpLogfont->lfFaceName[3]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName4, lpLogfont->lfFaceName[4]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName5, lpLogfont->lfFaceName[5]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName6, lpLogfont->lfFaceName[6]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName7, lpLogfont->lfFaceName[7]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName8, lpLogfont->lfFaceName[8]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName9, lpLogfont->lfFaceName[9]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName10, lpLogfont->lfFaceName[10]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName11, lpLogfont->lfFaceName[11]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName12, lpLogfont->lfFaceName[12]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName13, lpLogfont->lfFaceName[13]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName14, lpLogfont->lfFaceName[14]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName15, lpLogfont->lfFaceName[15]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName16, lpLogfont->lfFaceName[16]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName17, lpLogfont->lfFaceName[17]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName18, lpLogfont->lfFaceName[18]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName19, lpLogfont->lfFaceName[19]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName20, lpLogfont->lfFaceName[20]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName21, lpLogfont->lfFaceName[21]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName22, lpLogfont->lfFaceName[22]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName23, lpLogfont->lfFaceName[23]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName24, lpLogfont->lfFaceName[24]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName25, lpLogfont->lfFaceName[25]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName26, lpLogfont->lfFaceName[26]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName27, lpLogfont->lfFaceName[27]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName28, lpLogfont->lfFaceName[28]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName29, lpLogfont->lfFaceName[29]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName30, lpLogfont->lfFaceName[30]);
    (*env)->SetCharField(env,lpObject,lpLogfontFc->lfFaceName31, lpLogfont->lfFaceName[31]);
}


void getLogpenFields(JNIEnv *env, jobject lpObject, LOGPEN *lpLogpen, LOGPEN_FID_CACHE *lpLogpenFc)
{
    lpLogpen->lopnStyle = (*env)->GetIntField(env,lpObject,lpLogpenFc->lopnStyle);
    lpLogpen->lopnWidth.x = (*env)->GetIntField(env,lpObject,lpLogpenFc->x);
    lpLogpen->lopnWidth.y = (*env)->GetIntField(env,lpObject,lpLogpenFc->y);
    lpLogpen->lopnColor = (*env)->GetIntField(env,lpObject,lpLogpenFc->lopnColor);
}


void setLogpenFields(JNIEnv *env, jobject lpObject, LOGPEN *lpLogpen, LOGPEN_FID_CACHE *lpLogpenFc)
{
    (*env)->SetIntField(env,lpObject,lpLogpenFc->lopnStyle, lpLogpen->lopnStyle);
    (*env)->SetIntField(env,lpObject,lpLogpenFc->x, lpLogpen->lopnWidth.x);
    (*env)->SetIntField(env,lpObject,lpLogpenFc->y, lpLogpen->lopnWidth.y);
    (*env)->SetIntField(env,lpObject,lpLogpenFc->lopnColor, lpLogpen->lopnColor);
}
/*
void getExtlogpenFields(JNIEnv *env, jobject lpObject, EXTLOGPEN *lpExtlogpen, EXTLOGPEN_FID_CACHE *lpExtlogpenFc)
{
    lpExtlogpen->elpPenStyle = (*env)->GetIntField(env,lpObject,lpExtlogpenFc->elpPenStyle);
    lpExtlogpen->elpWidth = (*env)->GetIntField(env,lpObject,lpExtlogpenFc->elpWidth);
    lpExtlogpen->elpBrushStyle = (*env)->GetIntField(env,lpObject,lpExtlogpenFc->elpBrushStyle);
    lpExtlogpen->elpColor = (*env)->GetIntField(env,lpObject,lpExtlogpenFc->elpColor);
    lpExtlogpen->elpHatch = (*env)->GetIntField(env,lpObject,lpExtlogpenFc->elpHatch);
	lpExtlogpen->elpNumEntries = (*env)->GetIntField(env,lpObject,lpExtlogpenFc->elpNumEntries);    
}

void setExtlogpenFields(JNIEnv *env, jobject lpObject, EXTLOGPEN *lpExtlogpen, EXTLOGPEN_FID_CACHE *lpExtlogpenFc)
{
    (*env)->SetIntField(env,lpObject,lpExtlogpenFc->elpPenStyle, lpExtlogpen->elpPenStyle);
    (*env)->SetIntField(env,lpObject,lpExtlogpenFc->elpWidth, lpExtlogpen->elpWidth);
    (*env)->SetIntField(env,lpObject,lpExtlogpenFc->elpBrushStyle, lpExtlogpen->elpBrushStyle);
    (*env)->SetIntField(env,lpObject,lpExtlogpenFc->elpColor, lpExtlogpen->elpColor);
    (*env)->SetIntField(env,lpObject,lpExtlogpenFc->elpHatch, lpExtlogpen->elpHatch);
	(*env)->SetIntField(env,lpObject,lpExtlogpenFc->elpNumEntries, lpExtlogpen->elpNumEntries);
}
*/
void getLvcolumnFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpLvcolumn, PLVCOLUMN_FID_CACHE lpLvcolumnFc)
{
    lpLvcolumn->mask = (*env)->GetIntField(env,lpObject,lpLvcolumnFc->mask);
    lpLvcolumn->fmt = (*env)->GetIntField(env,lpObject,lpLvcolumnFc->fmt);
    lpLvcolumn->cx = (*env)->GetIntField(env,lpObject,lpLvcolumnFc->cx);
    lpLvcolumn->pszText = (LPTSTR)(*env)->GetIntField(env,lpObject,lpLvcolumnFc->pszText);
    lpLvcolumn->cchTextMax = (*env)->GetIntField(env,lpObject,lpLvcolumnFc->cchTextMax);
    lpLvcolumn->iSubItem = (*env)->GetIntField(env,lpObject,lpLvcolumnFc->iSubItem);
    lpLvcolumn->iImage = (*env)->GetIntField(env,lpObject,lpLvcolumnFc->iImage);
    lpLvcolumn->iOrder = (*env)->GetIntField(env,lpObject,lpLvcolumnFc->iOrder);
}

void setLvcolumnFields(JNIEnv *env, jobject lpObject, LVCOLUMN *lpLvcolumn, PLVCOLUMN_FID_CACHE lpLvcolumnFc)
{
    (*env)->SetIntField(env,lpObject,lpLvcolumnFc->mask, lpLvcolumn->mask);
    (*env)->SetIntField(env,lpObject,lpLvcolumnFc->fmt, lpLvcolumn->fmt);
    (*env)->SetIntField(env,lpObject,lpLvcolumnFc->cx, lpLvcolumn->cx);
    (*env)->SetIntField(env,lpObject,lpLvcolumnFc->pszText, (jint)lpLvcolumn->pszText);
    (*env)->SetIntField(env,lpObject,lpLvcolumnFc->cchTextMax, lpLvcolumn->cchTextMax);
    (*env)->SetIntField(env,lpObject,lpLvcolumnFc->iSubItem, lpLvcolumn->iSubItem);
    (*env)->SetIntField(env,lpObject,lpLvcolumnFc->iImage, lpLvcolumn->iImage);
    (*env)->SetIntField(env,lpObject,lpLvcolumnFc->iOrder, lpLvcolumn->iOrder);
}

void getLvhittestinfoFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpLvhittestinfo, PLVHITTESTINFO_FID_CACHE lpLvhittestinfoFc)
{
    lpLvhittestinfo->pt.x = (*env)->GetIntField(env,lpObject,lpLvhittestinfoFc->x);
    lpLvhittestinfo->pt.y = (*env)->GetIntField(env,lpObject,lpLvhittestinfoFc->y);
    lpLvhittestinfo->flags = (*env)->GetIntField(env,lpObject,lpLvhittestinfoFc->flags);
    lpLvhittestinfo->iItem = (*env)->GetIntField(env,lpObject,lpLvhittestinfoFc->iItem);
    lpLvhittestinfo->iSubItem = (*env)->GetIntField(env,lpObject,lpLvhittestinfoFc->iSubItem);
}

void setLvhittestinfoFields(JNIEnv *env, jobject lpObject, LVHITTESTINFO *lpLvhittestinfo, PLVHITTESTINFO_FID_CACHE lpLvhittestinfoFc)
{
    (*env)->SetIntField(env,lpObject,lpLvhittestinfoFc->x, lpLvhittestinfo->pt.x);
    (*env)->SetIntField(env,lpObject,lpLvhittestinfoFc->y, lpLvhittestinfo->pt.y);
    (*env)->SetIntField(env,lpObject,lpLvhittestinfoFc->flags, lpLvhittestinfo->flags);
    (*env)->SetIntField(env,lpObject,lpLvhittestinfoFc->iItem, lpLvhittestinfo->iItem);
    (*env)->SetIntField(env,lpObject,lpLvhittestinfoFc->iSubItem, lpLvhittestinfo->iSubItem);
}

void getLvitemFields(JNIEnv *env, jobject lpObject, LVITEM *lpLvitem, LVITEM_FID_CACHE *lpLvitemFc)
{
    lpLvitem->mask = (*env)->GetIntField(env,lpObject,lpLvitemFc->mask);
    lpLvitem->iItem = (*env)->GetIntField(env,lpObject,lpLvitemFc->iItem);
    lpLvitem->iSubItem = (*env)->GetIntField(env,lpObject,lpLvitemFc->iSubItem);
    lpLvitem->state = (*env)->GetIntField(env,lpObject,lpLvitemFc->state);
    lpLvitem->stateMask = (*env)->GetIntField(env,lpObject,lpLvitemFc->stateMask);
    lpLvitem->pszText = (LPTSTR)(*env)->GetIntField(env,lpObject,lpLvitemFc->pszText);
    lpLvitem->cchTextMax = (*env)->GetIntField(env,lpObject,lpLvitemFc->cchTextMax);
    lpLvitem->iImage = (*env)->GetIntField(env,lpObject,lpLvitemFc->iImage);
    lpLvitem->lParam = (*env)->GetIntField(env,lpObject,lpLvitemFc->lParam);
    lpLvitem->iIndent = (*env)->GetIntField(env,lpObject,lpLvitemFc->iIndent);
}

void setLvitemFields(JNIEnv *env, jobject lpObject, LVITEM *lpLvitem, LVITEM_FID_CACHE *lpLvitemFc)
{
    (*env)->SetIntField(env,lpObject,lpLvitemFc->mask, lpLvitem->mask);
    (*env)->SetIntField(env,lpObject,lpLvitemFc->iItem, lpLvitem->iItem);
    (*env)->SetIntField(env,lpObject,lpLvitemFc->iSubItem, lpLvitem->iSubItem);
    (*env)->SetIntField(env,lpObject,lpLvitemFc->state, lpLvitem->state);
    (*env)->SetIntField(env,lpObject,lpLvitemFc->stateMask, lpLvitem->stateMask);
    (*env)->SetIntField(env,lpObject,lpLvitemFc->pszText, (jint)lpLvitem->pszText);
    (*env)->SetIntField(env,lpObject,lpLvitemFc->cchTextMax, lpLvitem->cchTextMax);
    (*env)->SetIntField(env,lpObject,lpLvitemFc->iImage, lpLvitem->iImage);
    (*env)->SetIntField(env,lpObject,lpLvitemFc->lParam, lpLvitem->lParam);
    (*env)->SetIntField(env,lpObject,lpLvitemFc->iIndent, lpLvitem->iIndent);
}

void getMeasureitemstructFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpMeasureitemstruct, MEASUREITEMSTRUCT_FID_CACHE *lpMeasureitemstructFc)
{
    lpMeasureitemstruct->CtlType = (*env)->GetIntField(env,lpObject,lpMeasureitemstructFc->CtlType);
    lpMeasureitemstruct->CtlID = (*env)->GetIntField(env,lpObject,lpMeasureitemstructFc->CtlID);
    lpMeasureitemstruct->itemID = (*env)->GetIntField(env,lpObject,lpMeasureitemstructFc->itemID);
    lpMeasureitemstruct->itemWidth = (*env)->GetIntField(env,lpObject,lpMeasureitemstructFc->itemWidth);
    lpMeasureitemstruct->itemHeight = (*env)->GetIntField(env,lpObject,lpMeasureitemstructFc->itemHeight);
    lpMeasureitemstruct->itemData = (*env)->GetIntField(env,lpObject,lpMeasureitemstructFc->itemData);
}

void setMeasureitemstructFields(JNIEnv *env, jobject lpObject, MEASUREITEMSTRUCT *lpMeasureitemstruct, MEASUREITEMSTRUCT_FID_CACHE *lpMeasureitemstructFc)
{
    (*env)->SetIntField(env,lpObject,lpMeasureitemstructFc->CtlType, lpMeasureitemstruct->CtlType);
    (*env)->SetIntField(env,lpObject,lpMeasureitemstructFc->CtlID, lpMeasureitemstruct->CtlID);
    (*env)->SetIntField(env,lpObject,lpMeasureitemstructFc->itemID, lpMeasureitemstruct->itemID);
    (*env)->SetIntField(env,lpObject,lpMeasureitemstructFc->itemWidth, lpMeasureitemstruct->itemWidth);
    (*env)->SetIntField(env,lpObject,lpMeasureitemstructFc->itemHeight, lpMeasureitemstruct->itemHeight);
    (*env)->SetIntField(env,lpObject,lpMeasureitemstructFc->itemData, lpMeasureitemstruct->itemData);
}

void getMenuiteminfoFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpMenuiteminfo, MENUITEMINFO_FID_CACHE *lpMenuiteminfoFc)
{
    lpMenuiteminfo->cbSize = (*env)->GetIntField(env,lpObject,lpMenuiteminfoFc->cbSize);
    lpMenuiteminfo->fMask = (*env)->GetIntField(env,lpObject,lpMenuiteminfoFc->fMask);
    lpMenuiteminfo->fType = (*env)->GetIntField(env,lpObject,lpMenuiteminfoFc->fType);
    lpMenuiteminfo->fState = (*env)->GetIntField(env,lpObject,lpMenuiteminfoFc->fState);
    lpMenuiteminfo->wID = (*env)->GetIntField(env,lpObject,lpMenuiteminfoFc->wID);
    lpMenuiteminfo->hSubMenu = (HMENU)(*env)->GetIntField(env,lpObject,lpMenuiteminfoFc->hSubMenu);
    lpMenuiteminfo->hbmpChecked = (HBITMAP)(*env)->GetIntField(env,lpObject,lpMenuiteminfoFc->hbmpChecked);
    lpMenuiteminfo->hbmpUnchecked = (HBITMAP)(*env)->GetIntField(env,lpObject,lpMenuiteminfoFc->hbmpUnchecked);
    lpMenuiteminfo->dwItemData = (*env)->GetIntField(env,lpObject,lpMenuiteminfoFc->dwItemData);
    lpMenuiteminfo->dwTypeData = (LPTSTR)(*env)->GetIntField(env,lpObject,lpMenuiteminfoFc->dwTypeData);
    lpMenuiteminfo->cch = (*env)->GetIntField(env,lpObject,lpMenuiteminfoFc->cch);
#ifdef USE_2000_CALLS
#ifndef _WIN32_WCE
    lpMenuiteminfo->hbmpItem = (HBITMAP)(*env)->GetIntField(env,lpObject,lpMenuiteminfoFc->hbmpItem);
#endif
#endif
}

void setMenuiteminfoFields(JNIEnv *env, jobject lpObject, MENUITEMINFO *lpMenuiteminfo, MENUITEMINFO_FID_CACHE *lpMenuiteminfoFc)
{
    (*env)->SetIntField(env,lpObject,lpMenuiteminfoFc->cbSize, lpMenuiteminfo->cbSize);
    (*env)->SetIntField(env,lpObject,lpMenuiteminfoFc->fMask, lpMenuiteminfo->fMask);
    (*env)->SetIntField(env,lpObject,lpMenuiteminfoFc->fType, lpMenuiteminfo->fType);
    (*env)->SetIntField(env,lpObject,lpMenuiteminfoFc->fState, lpMenuiteminfo->fState);
    (*env)->SetIntField(env,lpObject,lpMenuiteminfoFc->wID, lpMenuiteminfo->wID);
    (*env)->SetIntField(env,lpObject,lpMenuiteminfoFc->hSubMenu, (jint)lpMenuiteminfo->hSubMenu);
    (*env)->SetIntField(env,lpObject,lpMenuiteminfoFc->hbmpChecked, (jint)lpMenuiteminfo->hbmpChecked);
    (*env)->SetIntField(env,lpObject,lpMenuiteminfoFc->hbmpUnchecked, (jint)lpMenuiteminfo->hbmpUnchecked);
    (*env)->SetIntField(env,lpObject,lpMenuiteminfoFc->dwItemData, lpMenuiteminfo->dwItemData);
    (*env)->SetIntField(env,lpObject,lpMenuiteminfoFc->dwTypeData, (jint)lpMenuiteminfo->dwTypeData);
    (*env)->SetIntField(env,lpObject,lpMenuiteminfoFc->cch, lpMenuiteminfo->cch);
#ifdef USE_2000_CALLS
#ifndef _WIN32_WCE
    (*env)->SetIntField(env,lpObject,lpMenuiteminfoFc->hbmpItem, (jint)lpMenuiteminfo->hbmpItem);
#endif
#endif
}

void getMsgFields(JNIEnv *env, jobject lpObject, MSG *lpMsg, MSG_FID_CACHE *lpMsgFc)
{
    lpMsg->hwnd = (HWND)(*env)->GetIntField(env,lpObject,lpMsgFc->hwnd);
    lpMsg->message = (*env)->GetIntField(env,lpObject,lpMsgFc->message);
    lpMsg->wParam = (*env)->GetIntField(env,lpObject,lpMsgFc->wParam);
    lpMsg->lParam = (*env)->GetIntField(env,lpObject,lpMsgFc->lParam);
    lpMsg->time = (*env)->GetIntField(env,lpObject,lpMsgFc->time);
    lpMsg->pt.x = (*env)->GetIntField(env,lpObject,lpMsgFc->x);
    lpMsg->pt.y = (*env)->GetIntField(env,lpObject,lpMsgFc->y);
}

void setMsgFields(JNIEnv *env, jobject lpObject, MSG *lpMsg, MSG_FID_CACHE *lpMsgFc)
{
    (*env)->SetIntField(env,lpObject,lpMsgFc->hwnd, (jint)lpMsg->hwnd);
    (*env)->SetIntField(env,lpObject,lpMsgFc->message, lpMsg->message);
    (*env)->SetIntField(env,lpObject,lpMsgFc->wParam, lpMsg->wParam);
    (*env)->SetIntField(env,lpObject,lpMsgFc->lParam, lpMsg->lParam);
    (*env)->SetIntField(env,lpObject,lpMsgFc->time, lpMsg->time);
    (*env)->SetIntField(env,lpObject,lpMsgFc->x, lpMsg->pt.x);
    (*env)->SetIntField(env,lpObject,lpMsgFc->y, lpMsg->pt.y);
}

void getNmhdrFields(JNIEnv *env, jobject lpObject, NMHDR *lpNmhdr, NMHDR_FID_CACHE *lpNmhdrFc)
{
    lpNmhdr->hwndFrom = (HWND)(*env)->GetIntField(env,lpObject,lpNmhdrFc->hwndFrom);
    lpNmhdr->idFrom = (*env)->GetIntField(env,lpObject,lpNmhdrFc->idFrom);
    lpNmhdr->code = (*env)->GetIntField(env,lpObject,lpNmhdrFc->code);
}

void setNmhdrFields(JNIEnv *env, jobject lpObject, NMHDR *lpNmhdr, NMHDR_FID_CACHE *lpNmhdrFc)
{
    (*env)->SetIntField(env,lpObject,lpNmhdrFc->hwndFrom, (jint)lpNmhdr->hwndFrom);
    (*env)->SetIntField(env,lpObject,lpNmhdrFc->idFrom, lpNmhdr->idFrom);
    (*env)->SetIntField(env,lpObject,lpNmhdrFc->code, lpNmhdr->code);
}

void getNmheaderFields(JNIEnv *env, jobject lpObject, NMHEADER *lpNmheader, NMHEADER_FID_CACHE *lpNmheaderFc)
{
    lpNmheader->hdr.hwndFrom = (HWND)(*env)->GetIntField(env,lpObject,lpNmheaderFc->hwndFrom);
    lpNmheader->hdr.idFrom = (*env)->GetIntField(env,lpObject,lpNmheaderFc->idFrom);
    lpNmheader->hdr.code = (*env)->GetIntField(env,lpObject,lpNmheaderFc->code);
    lpNmheader->iItem = (*env)->GetIntField(env,lpObject,lpNmheaderFc->iItem);
    lpNmheader->iButton = (*env)->GetIntField(env,lpObject,lpNmheaderFc->iButton);
    lpNmheader->pitem = (HDITEM FAR *)(*env)->GetIntField(env,lpObject,lpNmheaderFc->pitem);
}

void setNmheaderFields(JNIEnv *env, jobject lpObject, NMHEADER *lpNmheader, NMHEADER_FID_CACHE *lpNmheaderFc)
{
    (*env)->SetIntField(env,lpObject,lpNmheaderFc->hwndFrom, (jint)lpNmheader->hdr.hwndFrom);
    (*env)->SetIntField(env,lpObject,lpNmheaderFc->idFrom, lpNmheader->hdr.idFrom);
    (*env)->SetIntField(env,lpObject,lpNmheaderFc->code, lpNmheader->hdr.code);
    (*env)->SetIntField(env,lpObject,lpNmheaderFc->iItem, (jint)lpNmheader->iItem);
    (*env)->SetIntField(env,lpObject,lpNmheaderFc->iButton, (jint)lpNmheader->iButton);
    (*env)->SetIntField(env,lpObject,lpNmheaderFc->pitem, (jint)lpNmheader->pitem);
}

void getNmlistviewFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpNmlistview, NMLISTVIEW_FID_CACHE *lpNmlistviewFc)
{
    lpNmlistview->hdr.hwndFrom = (HWND)(*env)->GetIntField(env,lpObject,lpNmlistviewFc->hwndFrom);
    lpNmlistview->hdr.idFrom = (*env)->GetIntField(env,lpObject,lpNmlistviewFc->idFrom);
    lpNmlistview->hdr.code = (*env)->GetIntField(env,lpObject,lpNmlistviewFc->code);
    lpNmlistview->iItem = (*env)->GetIntField(env,lpObject,lpNmlistviewFc->iItem);
    lpNmlistview->iSubItem = (*env)->GetIntField(env,lpObject,lpNmlistviewFc->iSubItem);
    lpNmlistview->uNewState = (*env)->GetIntField(env,lpObject,lpNmlistviewFc->uNewState);
    lpNmlistview->uOldState = (*env)->GetIntField(env,lpObject,lpNmlistviewFc->uOldState);
    lpNmlistview->uChanged = (*env)->GetIntField(env,lpObject,lpNmlistviewFc->uChanged);
    lpNmlistview->ptAction.x = (*env)->GetIntField(env,lpObject,lpNmlistviewFc->x);
    lpNmlistview->ptAction.y = (*env)->GetIntField(env,lpObject,lpNmlistviewFc->y);
    lpNmlistview->lParam = (*env)->GetIntField(env,lpObject,lpNmlistviewFc->lParam);
}

void setNmlistviewFields(JNIEnv *env, jobject lpObject, NMLISTVIEW *lpNmlistview, NMLISTVIEW_FID_CACHE *lpNmlistviewFc)
{
    (*env)->SetIntField(env,lpObject,lpNmlistviewFc->hwndFrom, (jint)lpNmlistview->hdr.hwndFrom);
    (*env)->SetIntField(env,lpObject,lpNmlistviewFc->idFrom, lpNmlistview->hdr.idFrom);
    (*env)->SetIntField(env,lpObject,lpNmlistviewFc->code, lpNmlistview->hdr.code);
    (*env)->SetIntField(env,lpObject,lpNmlistviewFc->iItem, (jint)lpNmlistview->iItem);
    (*env)->SetIntField(env,lpObject,lpNmlistviewFc->iSubItem, (jint)lpNmlistview->iSubItem);
    (*env)->SetIntField(env,lpObject,lpNmlistviewFc->uNewState, (jint)lpNmlistview->uNewState);
    (*env)->SetIntField(env,lpObject,lpNmlistviewFc->uOldState, (jint)lpNmlistview->uOldState);
    (*env)->SetIntField(env,lpObject,lpNmlistviewFc->uChanged, (jint)lpNmlistview->uChanged);
    (*env)->SetIntField(env,lpObject,lpNmlistviewFc->x, (jint)lpNmlistview->ptAction.x);
    (*env)->SetIntField(env,lpObject,lpNmlistviewFc->y, (jint)lpNmlistview->ptAction.y);
    (*env)->SetIntField(env,lpObject,lpNmlistviewFc->lParam, (jint)lpNmlistview->lParam);
}

void getNmtoolbarFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpNmtoolbar, NMTOOLBAR_FID_CACHE *lpNmtoolbarFc)
{
    lpNmtoolbar->hdr.hwndFrom = (HWND)(*env)->GetIntField(env,lpObject,lpNmtoolbarFc->hwndFrom);
    lpNmtoolbar->hdr.idFrom = (*env)->GetIntField(env,lpObject,lpNmtoolbarFc->idFrom);
    lpNmtoolbar->hdr.code = (*env)->GetIntField(env,lpObject,lpNmtoolbarFc->code);
    lpNmtoolbar->iItem = (*env)->GetIntField(env,lpObject,lpNmtoolbarFc->iItem);
    lpNmtoolbar->tbButton.iBitmap = (*env)->GetIntField(env,lpObject,lpNmtoolbarFc->iBitmap);
    lpNmtoolbar->tbButton.idCommand = (*env)->GetIntField(env,lpObject,lpNmtoolbarFc->idCommand);
    lpNmtoolbar->tbButton.fsState = (*env)->GetByteField(env,lpObject,lpNmtoolbarFc->fsState);
    lpNmtoolbar->tbButton.fsStyle = (*env)->GetByteField(env,lpObject,lpNmtoolbarFc->fsStyle);
    lpNmtoolbar->tbButton.dwData = (*env)->GetIntField(env,lpObject,lpNmtoolbarFc->dwData);
    lpNmtoolbar->tbButton.iString = (*env)->GetIntField(env,lpObject,lpNmtoolbarFc->iString);
    lpNmtoolbar->cchText = (*env)->GetIntField(env,lpObject,lpNmtoolbarFc->cchText);
    lpNmtoolbar->pszText = (LPTSTR)(*env)->GetIntField(env,lpObject,lpNmtoolbarFc->pszText);
#ifndef _WIN32_WCE
    lpNmtoolbar->rcButton.left = (*env)->GetIntField(env,lpObject,lpNmtoolbarFc->left);
    lpNmtoolbar->rcButton.top = (*env)->GetIntField(env,lpObject,lpNmtoolbarFc->top);
    lpNmtoolbar->rcButton.right = (*env)->GetIntField(env,lpObject,lpNmtoolbarFc->right);
    lpNmtoolbar->rcButton.bottom = (*env)->GetIntField(env,lpObject,lpNmtoolbarFc->bottom);
#endif
}

void setNmtoolbarFields(JNIEnv *env, jobject lpObject, NMTOOLBAR *lpNmtoolbar, NMTOOLBAR_FID_CACHE *lpNmtoolbarFc)
{
    (*env)->SetIntField(env,lpObject,lpNmtoolbarFc->hwndFrom, (jint)lpNmtoolbar->hdr.hwndFrom);
    (*env)->SetIntField(env,lpObject,lpNmtoolbarFc->idFrom, lpNmtoolbar->hdr.idFrom);
    (*env)->SetIntField(env,lpObject,lpNmtoolbarFc->code, lpNmtoolbar->hdr.code);
    (*env)->SetIntField(env,lpObject,lpNmtoolbarFc->iItem, lpNmtoolbar->iItem);
    (*env)->SetIntField(env,lpObject,lpNmtoolbarFc->iBitmap, lpNmtoolbar->tbButton.iBitmap);
    (*env)->SetIntField(env,lpObject,lpNmtoolbarFc->idCommand, lpNmtoolbar->tbButton.idCommand);
    (*env)->SetByteField(env,lpObject,lpNmtoolbarFc->fsState, lpNmtoolbar->tbButton.fsState);
    (*env)->SetByteField(env,lpObject,lpNmtoolbarFc->fsStyle, lpNmtoolbar->tbButton.fsStyle);
    (*env)->SetIntField(env,lpObject,lpNmtoolbarFc->dwData, lpNmtoolbar->tbButton.dwData);
    (*env)->SetIntField(env,lpObject,lpNmtoolbarFc->iString, lpNmtoolbar->tbButton.iString);
    (*env)->SetIntField(env,lpObject,lpNmtoolbarFc->cchText, lpNmtoolbar->cchText);
    (*env)->SetIntField(env,lpObject,lpNmtoolbarFc->pszText, (jint)lpNmtoolbar->pszText);
#ifndef _WIN32_WCE
    (*env)->SetIntField(env,lpObject,lpNmtoolbarFc->left, lpNmtoolbar->rcButton.left);
    (*env)->SetIntField(env,lpObject,lpNmtoolbarFc->top, lpNmtoolbar->rcButton.top);
    (*env)->SetIntField(env,lpObject,lpNmtoolbarFc->right, lpNmtoolbar->rcButton.right);
    (*env)->SetIntField(env,lpObject,lpNmtoolbarFc->bottom, lpNmtoolbar->rcButton.bottom);
#endif
}


void getNmtvcustomdrawFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpNmtvcustomdraw, NMTVCUSTOMDRAW_FID_CACHE *lpNmtvcustomdrawFc)
{
    lpNmtvcustomdraw->nmcd.hdr.hwndFrom = (HWND)(*env)->GetIntField(env,lpObject,lpNmtvcustomdrawFc->hwndFrom);
    lpNmtvcustomdraw->nmcd.hdr.idFrom = (*env)->GetIntField(env,lpObject,lpNmtvcustomdrawFc->idFrom);
    lpNmtvcustomdraw->nmcd.hdr.code = (*env)->GetIntField(env,lpObject,lpNmtvcustomdrawFc->code);
    lpNmtvcustomdraw->nmcd.dwDrawStage = (*env)->GetIntField(env,lpObject,lpNmtvcustomdrawFc->dwDrawStage);
    lpNmtvcustomdraw->nmcd.hdc = (HDC)(*env)->GetIntField(env,lpObject,lpNmtvcustomdrawFc->hdc);
    lpNmtvcustomdraw->nmcd.rc.left = (*env)->GetIntField(env,lpObject,lpNmtvcustomdrawFc->left);
    lpNmtvcustomdraw->nmcd.rc.top = (*env)->GetIntField(env,lpObject,lpNmtvcustomdrawFc->top);
    lpNmtvcustomdraw->nmcd.rc.right = (*env)->GetIntField(env,lpObject,lpNmtvcustomdrawFc->right);
    lpNmtvcustomdraw->nmcd.rc.bottom = (*env)->GetIntField(env,lpObject,lpNmtvcustomdrawFc->bottom);
    lpNmtvcustomdraw->nmcd.dwItemSpec = (*env)->GetIntField(env,lpObject,lpNmtvcustomdrawFc->dwItemSpec);
    lpNmtvcustomdraw->nmcd.uItemState = (*env)->GetIntField(env,lpObject,lpNmtvcustomdrawFc->uItemState);
    lpNmtvcustomdraw->nmcd.lItemlParam = (*env)->GetIntField(env,lpObject,lpNmtvcustomdrawFc->lItemlParam);
    lpNmtvcustomdraw->clrText = (*env)->GetIntField(env,lpObject,lpNmtvcustomdrawFc->clrText);
    lpNmtvcustomdraw->clrTextBk = (*env)->GetIntField(env,lpObject,lpNmtvcustomdrawFc->clrTextBk);
#ifndef _WIN32_WCE
    lpNmtvcustomdraw->iLevel = (*env)->GetIntField(env,lpObject,lpNmtvcustomdrawFc->iLevel);
#endif
}

void setNmtvcustomdrawFields(JNIEnv *env, jobject lpObject, NMTVCUSTOMDRAW *lpNmtvcustomdraw, NMTVCUSTOMDRAW_FID_CACHE *lpNmtvcustomdrawFc)
{
    (*env)->SetIntField(env,lpObject,lpNmtvcustomdrawFc->hwndFrom, (jint)lpNmtvcustomdraw->nmcd.hdr.hwndFrom);
    (*env)->SetIntField(env,lpObject,lpNmtvcustomdrawFc->idFrom, lpNmtvcustomdraw->nmcd.hdr.idFrom);
    (*env)->SetIntField(env,lpObject,lpNmtvcustomdrawFc->code, lpNmtvcustomdraw->nmcd.hdr.code);
    (*env)->SetIntField(env,lpObject,lpNmtvcustomdrawFc->dwDrawStage, lpNmtvcustomdraw->nmcd.dwDrawStage);
    (*env)->SetIntField(env,lpObject,lpNmtvcustomdrawFc->hdc, (jint)lpNmtvcustomdraw->nmcd.hdc);
    (*env)->SetIntField(env,lpObject,lpNmtvcustomdrawFc->left, lpNmtvcustomdraw->nmcd.rc.left);
    (*env)->SetIntField(env,lpObject,lpNmtvcustomdrawFc->top, lpNmtvcustomdraw->nmcd.rc.top);
    (*env)->SetIntField(env,lpObject,lpNmtvcustomdrawFc->right, lpNmtvcustomdraw->nmcd.rc.right);
    (*env)->SetIntField(env,lpObject,lpNmtvcustomdrawFc->bottom, lpNmtvcustomdraw->nmcd.rc.bottom);
    (*env)->SetIntField(env,lpObject,lpNmtvcustomdrawFc->dwItemSpec, lpNmtvcustomdraw->nmcd.dwItemSpec);
    (*env)->SetIntField(env,lpObject,lpNmtvcustomdrawFc->uItemState, lpNmtvcustomdraw->nmcd.uItemState);
    (*env)->SetIntField(env,lpObject,lpNmtvcustomdrawFc->lItemlParam, lpNmtvcustomdraw->nmcd.lItemlParam);
    (*env)->SetIntField(env,lpObject,lpNmtvcustomdrawFc->clrText, lpNmtvcustomdraw->clrText);
    (*env)->SetIntField(env,lpObject,lpNmtvcustomdrawFc->clrTextBk, lpNmtvcustomdraw->clrTextBk);
#ifndef _WIN32_WCE
    (*env)->SetIntField(env,lpObject,lpNmtvcustomdrawFc->iLevel, lpNmtvcustomdraw->iLevel);
#endif
}

void getOpenfilenameFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpOpenfilename, OPENFILENAME_FID_CACHE *lpOpenfilenameFc)
{
    lpOpenfilename->lStructSize = (*env)->GetIntField(env,lpObject,lpOpenfilenameFc->lStructSize);
    lpOpenfilename->hwndOwner = (HWND)(*env)->GetIntField(env,lpObject,lpOpenfilenameFc->hwndOwner);
    lpOpenfilename->hInstance = (HINSTANCE)(*env)->GetIntField(env,lpObject,lpOpenfilenameFc->hInstance);
    lpOpenfilename->lpstrFilter = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpOpenfilenameFc->lpstrFilter);
    lpOpenfilename->lpstrCustomFilter = (LPTSTR)(*env)->GetIntField(env,lpObject,lpOpenfilenameFc->lpstrCustomFilter);
    lpOpenfilename->nMaxCustFilter = (*env)->GetIntField(env,lpObject,lpOpenfilenameFc->nMaxCustFilter);
    lpOpenfilename->nFilterIndex = (*env)->GetIntField(env,lpObject,lpOpenfilenameFc->nFilterIndex);
    lpOpenfilename->lpstrFile = (LPTSTR)(*env)->GetIntField(env,lpObject,lpOpenfilenameFc->lpstrFile);
    lpOpenfilename->nMaxFile = (*env)->GetIntField(env,lpObject,lpOpenfilenameFc->nMaxFile);
    lpOpenfilename->lpstrFileTitle = (LPTSTR)(*env)->GetIntField(env,lpObject,lpOpenfilenameFc->lpstrFileTitle);
    lpOpenfilename->nMaxFileTitle = (*env)->GetIntField(env,lpObject,lpOpenfilenameFc->nMaxFileTitle);
    lpOpenfilename->lpstrInitialDir = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpOpenfilenameFc->lpstrInitialDir);
    lpOpenfilename->lpstrTitle = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpOpenfilenameFc->lpstrTitle);
    lpOpenfilename->Flags = (*env)->GetIntField(env,lpObject,lpOpenfilenameFc->Flags);
    lpOpenfilename->nFileOffset = (*env)->GetShortField(env,lpObject,lpOpenfilenameFc->nFileOffset);
    lpOpenfilename->nFileExtension = (*env)->GetShortField(env,lpObject,lpOpenfilenameFc->nFileExtension);
    lpOpenfilename->lpstrDefExt = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpOpenfilenameFc->lpstrDefExt);
    lpOpenfilename->lCustData = (*env)->GetIntField(env,lpObject,lpOpenfilenameFc->lCustData);
    lpOpenfilename->lpfnHook = (LPOFNHOOKPROC)(*env)->GetIntField(env,lpObject,lpOpenfilenameFc->lpfnHook);
    lpOpenfilename->lpTemplateName = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpOpenfilenameFc->lpTemplateName);
}

void setOpenfilenameFields(JNIEnv *env, jobject lpObject, OPENFILENAME *lpOpenfilename, OPENFILENAME_FID_CACHE *lpOpenfilenameFc)
{
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->lStructSize, lpOpenfilename->lStructSize);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->hwndOwner, (jint)lpOpenfilename->hwndOwner);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->hInstance, (jint)lpOpenfilename->hInstance);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->lpstrFilter, (jint)lpOpenfilename->lpstrFilter);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->lpstrCustomFilter, (jint)lpOpenfilename->lpstrCustomFilter);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->nMaxCustFilter, lpOpenfilename->nMaxCustFilter);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->nFilterIndex, lpOpenfilename->nFilterIndex);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->lpstrFile, (jint)lpOpenfilename->lpstrFile);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->nMaxFile, lpOpenfilename->nMaxFile);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->lpstrFileTitle, (jint)lpOpenfilename->lpstrFileTitle);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->nMaxFileTitle, lpOpenfilename->nMaxFileTitle);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->lpstrInitialDir, (jint)lpOpenfilename->lpstrInitialDir);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->lpstrTitle, (jint)lpOpenfilename->lpstrTitle);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->Flags, lpOpenfilename->Flags);
    (*env)->SetShortField(env,lpObject,lpOpenfilenameFc->nFileOffset, lpOpenfilename->nFileOffset);
    (*env)->SetShortField(env,lpObject,lpOpenfilenameFc->nFileExtension, lpOpenfilename->nFileExtension);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->lpstrDefExt, (jint)lpOpenfilename->lpstrDefExt);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->lCustData, lpOpenfilename->lCustData);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->lpfnHook, (jint)lpOpenfilename->lpfnHook);
    (*env)->SetIntField(env,lpObject,lpOpenfilenameFc->lpTemplateName, (jint)lpOpenfilename->lpTemplateName);
}

void getPaintstructFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpPaint, PAINTSTRUCT_FID_CACHE *lpPaintstructFc)
{
    lpPaint->hdc = (HDC)(*env)->GetIntField(env,lpObject,lpPaintstructFc->hdc);
    lpPaint->fErase = (*env)->GetBooleanField(env,lpObject,lpPaintstructFc->fErase);
    lpPaint->rcPaint.left = (*env)->GetIntField(env,lpObject,lpPaintstructFc->left);
    lpPaint->rcPaint.top = (*env)->GetIntField(env,lpObject,lpPaintstructFc->top);
    lpPaint->rcPaint.right = (*env)->GetIntField(env,lpObject,lpPaintstructFc->right);
    lpPaint->rcPaint.bottom = (*env)->GetIntField(env,lpObject,lpPaintstructFc->bottom);
    lpPaint->fRestore = (*env)->GetBooleanField(env,lpObject,lpPaintstructFc->fRestore);
    lpPaint->fIncUpdate = (*env)->GetBooleanField(env,lpObject,lpPaintstructFc->fIncUpdate);
}

void setPaintstructFields(JNIEnv *env, jobject lpObject, PAINTSTRUCT *lpPaint, PAINTSTRUCT_FID_CACHE *lpPaintstructFc)
{
    (*env)->SetIntField(env,lpObject,lpPaintstructFc->hdc, (jint)lpPaint->hdc);
    (*env)->SetBooleanField(env,lpObject,lpPaintstructFc->fErase, (jboolean)lpPaint->fErase);
    (*env)->SetIntField(env,lpObject,lpPaintstructFc->left, lpPaint->rcPaint.left);
    (*env)->SetIntField(env,lpObject,lpPaintstructFc->top, lpPaint->rcPaint.top);
    (*env)->SetIntField(env,lpObject,lpPaintstructFc->right, lpPaint->rcPaint.right);
    (*env)->SetIntField(env,lpObject,lpPaintstructFc->bottom, lpPaint->rcPaint.bottom);
    (*env)->SetBooleanField(env,lpObject,lpPaintstructFc->fRestore, (jboolean)lpPaint->fRestore);
    (*env)->SetBooleanField(env,lpObject,lpPaintstructFc->fIncUpdate, (jboolean)lpPaint->fIncUpdate);
}
/*
void getParaformatFields(JNIEnv *env, jobject lpObject, PARAFORMAT *lpParaformat, PARAFORMAT_FID_CACHE *lpParaformatFc)
{
    lpParaformat->cbSize = (*env)->GetIntField(env,lpObject,lpParaformatFc->cbSize);
    lpParaformat->dwMask = (*env)->GetIntField(env,lpObject,lpParaformatFc->dwMask);
    lpParaformat->wNumbering = (*env)->GetShortField(env,lpObject,lpParaformatFc->wNumbering);
    lpParaformat->wEffects = (*env)->GetShortField(env,lpObject,lpParaformatFc->wEffects);
    lpParaformat->dxStartIndent = (*env)->GetIntField(env,lpObject,lpParaformatFc->dxStartIndent);
    lpParaformat->dxRightIndent = (*env)->GetIntField(env,lpObject,lpParaformatFc->dxRightIndent);
    lpParaformat->dxOffset = (*env)->GetIntField(env,lpObject,lpParaformatFc->dxOffset);
    lpParaformat->wAlignment = (*env)->GetShortField(env,lpObject,lpParaformatFc->wAlignment);
    lpParaformat->cTabCount = (*env)->GetShortField(env,lpObject,lpParaformatFc->cTabCount);
    lpParaformat->rgxTabs[0] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs0);
    lpParaformat->rgxTabs[1] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs1);
    lpParaformat->rgxTabs[2] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs2);
    lpParaformat->rgxTabs[3] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs3);
    lpParaformat->rgxTabs[4] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs4);
    lpParaformat->rgxTabs[5] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs5);
    lpParaformat->rgxTabs[6] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs6);
    lpParaformat->rgxTabs[7] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs7);
    lpParaformat->rgxTabs[8] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs8);
    lpParaformat->rgxTabs[9] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs9);
    lpParaformat->rgxTabs[10] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs10);
    lpParaformat->rgxTabs[11] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs11);
    lpParaformat->rgxTabs[12] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs12);
    lpParaformat->rgxTabs[13] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs13);
    lpParaformat->rgxTabs[14] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs14);
    lpParaformat->rgxTabs[15] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs15);
    lpParaformat->rgxTabs[16] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs16);
    lpParaformat->rgxTabs[17] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs17);
    lpParaformat->rgxTabs[18] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs18);
    lpParaformat->rgxTabs[19] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs19);
    lpParaformat->rgxTabs[20] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs20);
    lpParaformat->rgxTabs[21] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs21);
    lpParaformat->rgxTabs[22] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs22);
    lpParaformat->rgxTabs[23] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs23);
    lpParaformat->rgxTabs[24] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs24);
    lpParaformat->rgxTabs[25] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs25);
    lpParaformat->rgxTabs[26] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs26);
    lpParaformat->rgxTabs[27] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs27);
    lpParaformat->rgxTabs[28] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs28);
    lpParaformat->rgxTabs[29] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs29);
    lpParaformat->rgxTabs[30] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs30);
    lpParaformat->rgxTabs[31] = (*env)->GetIntField(env,lpObject,lpParaformatFc->rgxTabs31);
}

void setParaformatFields(JNIEnv *env, jobject lpObject, PARAFORMAT *lpParaformat, PARAFORMAT_FID_CACHE *lpParaformatFc)
{
    (*env)->SetIntField(env,lpObject,lpParaformatFc->cbSize, lpParaformat->cbSize);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->dwMask, lpParaformat->dwMask);
    (*env)->SetShortField(env,lpObject,lpParaformatFc->wNumbering, lpParaformat->wNumbering);
    (*env)->SetShortField(env,lpObject,lpParaformatFc->wEffects, lpParaformat->wEffects);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->dxStartIndent, lpParaformat->dxStartIndent);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->dxRightIndent, lpParaformat->dxRightIndent);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->dxOffset, lpParaformat->dxOffset);
    (*env)->SetShortField(env,lpObject,lpParaformatFc->wAlignment, lpParaformat->wAlignment);
    (*env)->SetShortField(env,lpObject,lpParaformatFc->cTabCount, lpParaformat->cTabCount);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs0, lpParaformat->rgxTabs[0]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs1, lpParaformat->rgxTabs[1]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs2, lpParaformat->rgxTabs[2]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs3, lpParaformat->rgxTabs[3]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs4, lpParaformat->rgxTabs[4]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs5, lpParaformat->rgxTabs[5]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs6, lpParaformat->rgxTabs[6]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs7, lpParaformat->rgxTabs[7]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs8, lpParaformat->rgxTabs[8]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs9, lpParaformat->rgxTabs[9]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs10, lpParaformat->rgxTabs[10]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs11, lpParaformat->rgxTabs[11]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs12, lpParaformat->rgxTabs[12]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs13, lpParaformat->rgxTabs[13]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs14, lpParaformat->rgxTabs[14]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs15, lpParaformat->rgxTabs[15]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs16, lpParaformat->rgxTabs[16]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs17, lpParaformat->rgxTabs[17]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs18, lpParaformat->rgxTabs[18]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs19, lpParaformat->rgxTabs[19]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs20, lpParaformat->rgxTabs[20]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs21, lpParaformat->rgxTabs[21]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs22, lpParaformat->rgxTabs[22]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs23, lpParaformat->rgxTabs[23]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs24, lpParaformat->rgxTabs[24]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs25, lpParaformat->rgxTabs[25]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs26, lpParaformat->rgxTabs[26]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs27, lpParaformat->rgxTabs[27]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs28, lpParaformat->rgxTabs[28]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs29, lpParaformat->rgxTabs[29]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs30, lpParaformat->rgxTabs[30]);
    (*env)->SetIntField(env,lpObject,lpParaformatFc->rgxTabs31, lpParaformat->rgxTabs[31]);
}
*/
void getPointFields(JNIEnv *env, jobject lpObject, POINT *lpPoint, POINT_FID_CACHE *lpPointFc)
{
    lpPoint->x = (*env)->GetIntField(env,lpObject,lpPointFc->x);
    lpPoint->y = (*env)->GetIntField(env,lpObject,lpPointFc->y);
}

void setPointFields(JNIEnv *env, jobject lpObject, POINT *lpPoint, POINT_FID_CACHE *lpPointFc)
{
    (*env)->SetIntField(env,lpObject,lpPointFc->x, lpPoint->x);
    (*env)->SetIntField(env,lpObject,lpPointFc->y, lpPoint->y);
}

void getRebarbandinfoFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpRebarbandinfo, REBARBANDINFO_FID_CACHE *lpRebarbandinfoFc)
{
    lpRebarbandinfo->cbSize = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->cbSize);
    lpRebarbandinfo->fMask = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->fMask);
    lpRebarbandinfo->fStyle = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->fStyle);
    lpRebarbandinfo->clrFore = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->clrFore);
    lpRebarbandinfo->clrBack = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->clrBack);
    lpRebarbandinfo->lpText = (LPTSTR)(*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->lpText);
    lpRebarbandinfo->cch = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->cch);
    lpRebarbandinfo->iImage = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->iImage);
    lpRebarbandinfo->hwndChild = (HWND)(*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->hwndChild);
    lpRebarbandinfo->cxMinChild = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->cxMinChild);
    lpRebarbandinfo->cyMinChild = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->cyMinChild);
    lpRebarbandinfo->cx = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->cx);
    lpRebarbandinfo->hbmBack = (HBITMAP)(*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->hbmBack);
    lpRebarbandinfo->wID = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->wID);
#ifndef _WIN32_WCE
    lpRebarbandinfo->cyChild = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->cyChild);
    lpRebarbandinfo->cyMaxChild = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->cyMaxChild);
    lpRebarbandinfo->cyIntegral = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->cyIntegral);
    lpRebarbandinfo->cxIdeal = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->cxIdeal);
    lpRebarbandinfo->lParam = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->lParam);
    lpRebarbandinfo->cxHeader = (*env)->GetIntField(env,lpObject,lpRebarbandinfoFc->cxHeader);
#endif
}

void setRebarbandinfoFields(JNIEnv *env, jobject lpObject, REBARBANDINFO *lpRebarbandinfo, REBARBANDINFO_FID_CACHE *lpRebarbandinfoFc)
{
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->cbSize, lpRebarbandinfo->cbSize);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->fMask, lpRebarbandinfo->fMask);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->fStyle, lpRebarbandinfo->fStyle);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->clrFore, lpRebarbandinfo->clrFore);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->clrBack, lpRebarbandinfo->clrBack);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->lpText, (jint)lpRebarbandinfo->lpText);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->cch, lpRebarbandinfo->cch);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->iImage, lpRebarbandinfo->iImage);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->hwndChild, (jint)lpRebarbandinfo->hwndChild);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->cxMinChild, lpRebarbandinfo->cxMinChild);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->cyMinChild, lpRebarbandinfo->cyMinChild);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->cx, lpRebarbandinfo->cx);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->hbmBack, (jint)lpRebarbandinfo->hbmBack);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->wID, lpRebarbandinfo->wID);
#ifndef _WIN32_WCE
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->cyChild, lpRebarbandinfo->cyChild);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->cyMaxChild, lpRebarbandinfo->cyMaxChild);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->cyIntegral, lpRebarbandinfo->cyIntegral);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->cxIdeal, lpRebarbandinfo->cxIdeal);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->lParam, lpRebarbandinfo->lParam);
    (*env)->SetIntField(env,lpObject,lpRebarbandinfoFc->cxHeader, lpRebarbandinfo->cxHeader);
#endif
}

void getRectFields(JNIEnv *env, jobject lpObject, RECT *lpRect, RECT_FID_CACHE *lpRectFc)
{
    lpRect->left = (*env)->GetIntField(env,lpObject,lpRectFc->left);
    lpRect->top = (*env)->GetIntField(env,lpObject,lpRectFc->top);
    lpRect->right = (*env)->GetIntField(env,lpObject,lpRectFc->right);
    lpRect->bottom = (*env)->GetIntField(env,lpObject,lpRectFc->bottom);
}

void setRectFields(JNIEnv *env, jobject lpObject, RECT *lpRect, RECT_FID_CACHE *lpRectFc)
{
    (*env)->SetIntField(env,lpObject,lpRectFc->left,lpRect->left);
    (*env)->SetIntField(env,lpObject,lpRectFc->top,lpRect->top);
    (*env)->SetIntField(env,lpObject,lpRectFc->right,lpRect->right);
    (*env)->SetIntField(env,lpObject,lpRectFc->bottom,lpRect->bottom);
}

void getScrollinfoFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpScrollinfo, SCROLLINFO_FID_CACHE *lpScrollinfoFc)
{
    lpScrollinfo->cbSize = (*env)->GetIntField(env,lpObject,lpScrollinfoFc->cbSize);
    lpScrollinfo->fMask = (*env)->GetIntField(env,lpObject,lpScrollinfoFc->fMask);
    lpScrollinfo->nMin = (*env)->GetIntField(env,lpObject,lpScrollinfoFc->nMin);
    lpScrollinfo->nMax = (*env)->GetIntField(env,lpObject,lpScrollinfoFc->nMax);
    lpScrollinfo->nPage = (*env)->GetIntField(env,lpObject,lpScrollinfoFc->nPage);
    lpScrollinfo->nPos = (*env)->GetIntField(env,lpObject,lpScrollinfoFc->nPos);
    lpScrollinfo->nTrackPos = (*env)->GetIntField(env,lpObject,lpScrollinfoFc->nTrackPos);
}

void setScrollinfoFields(JNIEnv *env, jobject lpObject, SCROLLINFO *lpScrollinfo, SCROLLINFO_FID_CACHE *lpScrollinfoFc)
{
    (*env)->SetIntField(env,lpObject,lpScrollinfoFc->cbSize, lpScrollinfo->cbSize);
    (*env)->SetIntField(env,lpObject,lpScrollinfoFc->fMask, lpScrollinfo->fMask);
    (*env)->SetIntField(env,lpObject,lpScrollinfoFc->nMin, lpScrollinfo->nMin);
    (*env)->SetIntField(env,lpObject,lpScrollinfoFc->nMax, lpScrollinfo->nMax);
    (*env)->SetIntField(env,lpObject,lpScrollinfoFc->nPage, lpScrollinfo->nPage);
    (*env)->SetIntField(env,lpObject,lpScrollinfoFc->nPos, lpScrollinfo->nPos);
    (*env)->SetIntField(env,lpObject,lpScrollinfoFc->nTrackPos, lpScrollinfo->nTrackPos);
}

void getSizeFields(JNIEnv *env, jobject lpObject, SIZE *lpSize, SIZE_FID_CACHE *lpSizeFc)
{
    lpSize->cx = (*env)->GetIntField(env,lpObject,lpSizeFc->cx);
    lpSize->cy = (*env)->GetIntField(env,lpObject,lpSizeFc->cy);
}

void setSizeFields(JNIEnv *env, jobject lpObject, SIZE *lpSize, SIZE_FID_CACHE *lpSizeFc)
{
    (*env)->SetIntField(env,lpObject,lpSizeFc->cx, lpSize->cx);
    (*env)->SetIntField(env,lpObject,lpSizeFc->cy, lpSize->cy);
}

void getTbbuttonFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpTbbutton, TBBUTTON_FID_CACHE *lpTbbuttonFc)
{
    lpTbbutton->iBitmap = (*env)->GetIntField(env,lpObject,lpTbbuttonFc->iBitmap);
    lpTbbutton->idCommand = (*env)->GetIntField(env,lpObject,lpTbbuttonFc->idCommand);
    lpTbbutton->fsState = (*env)->GetByteField(env,lpObject,lpTbbuttonFc->fsState);
    lpTbbutton->fsStyle = (*env)->GetByteField(env,lpObject,lpTbbuttonFc->fsStyle);
    lpTbbutton->dwData = (*env)->GetIntField(env,lpObject,lpTbbuttonFc->dwData);
    lpTbbutton->iString = (*env)->GetIntField(env,lpObject,lpTbbuttonFc->iString);
}

void setTbbuttonFields(JNIEnv *env, jobject lpObject, TBBUTTON *lpTbbutton, TBBUTTON_FID_CACHE *lpTbbuttonFc)
{
    (*env)->SetIntField(env,lpObject,lpTbbuttonFc->iBitmap, lpTbbutton->iBitmap);
    (*env)->SetIntField(env,lpObject,lpTbbuttonFc->idCommand, lpTbbutton->idCommand);
    (*env)->SetByteField(env,lpObject,lpTbbuttonFc->fsState, lpTbbutton->fsState);
    (*env)->SetByteField(env,lpObject,lpTbbuttonFc->fsStyle, lpTbbutton->fsStyle);
    (*env)->SetIntField(env,lpObject,lpTbbuttonFc->dwData, lpTbbutton->dwData);
    (*env)->SetIntField(env,lpObject,lpTbbuttonFc->iString, lpTbbutton->iString);
}

void getTbbuttoninfoFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpTbbuttoninfo, TBBUTTONINFO_FID_CACHE *lpTbbuttoninfoFc)
{
    lpTbbuttoninfo->cbSize = (*env)->GetIntField(env,lpObject,lpTbbuttoninfoFc->cbSize);
    lpTbbuttoninfo->dwMask = (*env)->GetIntField(env,lpObject,lpTbbuttoninfoFc->dwMask);
    lpTbbuttoninfo->idCommand = (*env)->GetIntField(env,lpObject,lpTbbuttoninfoFc->idCommand);
    lpTbbuttoninfo->iImage = (*env)->GetIntField(env,lpObject,lpTbbuttoninfoFc->iImage);
    lpTbbuttoninfo->fsState = (*env)->GetByteField(env,lpObject,lpTbbuttoninfoFc->fsState);
    lpTbbuttoninfo->fsStyle = (*env)->GetByteField(env,lpObject,lpTbbuttoninfoFc->fsStyle);
    lpTbbuttoninfo->cx = (*env)->GetShortField(env,lpObject,lpTbbuttoninfoFc->cx);
    lpTbbuttoninfo->lParam = (*env)->GetIntField(env,lpObject,lpTbbuttoninfoFc->lParam);
    lpTbbuttoninfo->pszText = (LPTSTR)(*env)->GetIntField(env,lpObject,lpTbbuttoninfoFc->pszText);
    lpTbbuttoninfo->cchText = (*env)->GetIntField(env,lpObject,lpTbbuttoninfoFc->cchText);
}

void setTbbuttoninfoFields(JNIEnv *env, jobject lpObject, TBBUTTONINFO *lpTbbuttoninfo, TBBUTTONINFO_FID_CACHE *lpTbbuttoninfoFc)
{
    (*env)->SetIntField(env,lpObject,lpTbbuttoninfoFc->cbSize, lpTbbuttoninfo->cbSize);
    (*env)->SetIntField(env,lpObject,lpTbbuttoninfoFc->dwMask, lpTbbuttoninfo->dwMask);
    (*env)->SetIntField(env,lpObject,lpTbbuttoninfoFc->idCommand, lpTbbuttoninfo->idCommand);
    (*env)->SetIntField(env,lpObject,lpTbbuttoninfoFc->iImage, lpTbbuttoninfo->iImage);
    (*env)->SetByteField(env,lpObject,lpTbbuttoninfoFc->fsState, lpTbbuttoninfo->fsState);
    (*env)->SetByteField(env,lpObject,lpTbbuttoninfoFc->fsStyle, lpTbbuttoninfo->fsStyle);
    (*env)->SetShortField(env,lpObject,lpTbbuttoninfoFc->cx, lpTbbuttoninfo->cx);
    (*env)->SetIntField(env,lpObject,lpTbbuttoninfoFc->lParam, lpTbbuttoninfo->lParam);
    (*env)->SetIntField(env,lpObject,lpTbbuttoninfoFc->pszText, (jint)lpTbbuttoninfo->pszText);
    (*env)->SetIntField(env,lpObject,lpTbbuttoninfoFc->cchText, lpTbbuttoninfo->cchText);
}

void getTcitemFields(JNIEnv *env, jobject lpObject, TCITEM *lpTcitem, TCITEM_FID_CACHE *lpTcitemFc)
{
    lpTcitem->mask = (*env)->GetIntField(env,lpObject,lpTcitemFc->mask);
    lpTcitem->dwState = (*env)->GetIntField(env,lpObject,lpTcitemFc->dwState);
    lpTcitem->dwStateMask = (*env)->GetIntField(env,lpObject,lpTcitemFc->dwStateMask);
    lpTcitem->pszText = (LPTSTR)(*env)->GetIntField(env,lpObject,lpTcitemFc->pszText);
    lpTcitem->cchTextMax = (*env)->GetIntField(env,lpObject,lpTcitemFc->cchTextMax);
    lpTcitem->iImage = (*env)->GetIntField(env,lpObject,lpTcitemFc->iImage);
    lpTcitem->lParam = (*env)->GetIntField(env,lpObject,lpTcitemFc->lParam);
}

void setTcitemFields(JNIEnv *env, jobject lpObject, TCITEM *lpTcitem, TCITEM_FID_CACHE *lpTcitemFc)
{
    (*env)->SetIntField(env,lpObject,lpTcitemFc->mask, lpTcitem->mask);
    (*env)->SetIntField(env,lpObject,lpTcitemFc->dwState, lpTcitem->dwState);
    (*env)->SetIntField(env,lpObject,lpTcitemFc->dwStateMask, lpTcitem->dwStateMask);
    (*env)->SetIntField(env,lpObject,lpTcitemFc->pszText, (jint)lpTcitem->pszText);
    (*env)->SetIntField(env,lpObject,lpTcitemFc->cchTextMax, lpTcitem->cchTextMax);
    (*env)->SetIntField(env,lpObject,lpTcitemFc->iImage, lpTcitem->iImage);
    (*env)->SetIntField(env,lpObject,lpTcitemFc->lParam, lpTcitem->lParam);
}

void getTextmetricFieldsA(JNIEnv *env, jobject lpObject, TEXTMETRIC *lpTextmetric, TEXTMETRIC_FID_CACHE *lpTextmetricFc)
{
    lpTextmetric->tmHeight = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmHeight);
    lpTextmetric->tmAscent = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmAscent);
    lpTextmetric->tmDescent = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmDescent);
    lpTextmetric->tmInternalLeading = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmInternalLeading);
    lpTextmetric->tmExternalLeading = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmExternalLeading);
    lpTextmetric->tmAveCharWidth = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmAveCharWidth);
    lpTextmetric->tmMaxCharWidth = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmMaxCharWidth);
    lpTextmetric->tmWeight = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmWeight);
    lpTextmetric->tmOverhang = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmOverhang);
    lpTextmetric->tmDigitizedAspectX = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmDigitizedAspectX);
    lpTextmetric->tmDigitizedAspectY = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmDigitizedAspectY);
    lpTextmetric->tmFirstChar = (*env)->GetByteField(env,lpObject,lpTextmetricFc->tmFirstChar);
    lpTextmetric->tmLastChar = (*env)->GetByteField(env,lpObject,lpTextmetricFc->tmLastChar);
    lpTextmetric->tmDefaultChar = (*env)->GetByteField(env,lpObject,lpTextmetricFc->tmDefaultChar);
    lpTextmetric->tmBreakChar = (*env)->GetByteField(env,lpObject,lpTextmetricFc->tmBreakChar);
    lpTextmetric->tmItalic = (*env)->GetByteField(env,lpObject,lpTextmetricFc->tmItalic);
    lpTextmetric->tmUnderlined = (*env)->GetByteField(env,lpObject,lpTextmetricFc->tmUnderlined);
    lpTextmetric->tmStruckOut = (*env)->GetByteField(env,lpObject,lpTextmetricFc->tmStruckOut);
    lpTextmetric->tmPitchAndFamily = (*env)->GetByteField(env,lpObject,lpTextmetricFc->tmPitchAndFamily);
    lpTextmetric->tmCharSet = (*env)->GetByteField(env,lpObject,lpTextmetricFc->tmCharSet);
}
void getTextmetricFieldsW(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpTextmetric, TEXTMETRIC_FID_CACHE *lpTextmetricFc)
{
    lpTextmetric->tmHeight = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmHeight);
    lpTextmetric->tmAscent = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmAscent);
    lpTextmetric->tmDescent = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmDescent);
    lpTextmetric->tmInternalLeading = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmInternalLeading);
    lpTextmetric->tmExternalLeading = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmExternalLeading);
    lpTextmetric->tmAveCharWidth = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmAveCharWidth);
    lpTextmetric->tmMaxCharWidth = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmMaxCharWidth);
    lpTextmetric->tmWeight = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmWeight);
    lpTextmetric->tmOverhang = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmOverhang);
    lpTextmetric->tmDigitizedAspectX = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmDigitizedAspectX);
    lpTextmetric->tmDigitizedAspectY = (*env)->GetIntField(env,lpObject,lpTextmetricFc->tmDigitizedAspectY);
    lpTextmetric->tmFirstChar = (*env)->GetCharField(env,lpObject,lpTextmetricFc->tmFirstChar);
    lpTextmetric->tmLastChar = (*env)->GetCharField(env,lpObject,lpTextmetricFc->tmLastChar);
    lpTextmetric->tmDefaultChar = (*env)->GetCharField(env,lpObject,lpTextmetricFc->tmDefaultChar);
    lpTextmetric->tmBreakChar = (*env)->GetCharField(env,lpObject,lpTextmetricFc->tmBreakChar);
    lpTextmetric->tmItalic = (*env)->GetByteField(env,lpObject,lpTextmetricFc->tmItalic);
    lpTextmetric->tmUnderlined = (*env)->GetByteField(env,lpObject,lpTextmetricFc->tmUnderlined);
    lpTextmetric->tmStruckOut = (*env)->GetByteField(env,lpObject,lpTextmetricFc->tmStruckOut);
    lpTextmetric->tmPitchAndFamily = (*env)->GetByteField(env,lpObject,lpTextmetricFc->tmPitchAndFamily);
    lpTextmetric->tmCharSet = (*env)->GetByteField(env,lpObject,lpTextmetricFc->tmCharSet);
}

void setTextmetricFieldsA(JNIEnv *env, jobject lpObject, TEXTMETRIC *lpTextmetric, TEXTMETRIC_FID_CACHE *lpTextmetricFc)
{
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmHeight, lpTextmetric->tmHeight);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmAscent, lpTextmetric->tmAscent);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmDescent, lpTextmetric->tmDescent);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmInternalLeading, lpTextmetric->tmInternalLeading);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmExternalLeading, lpTextmetric->tmExternalLeading);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmAveCharWidth, lpTextmetric->tmAveCharWidth);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmMaxCharWidth, lpTextmetric->tmMaxCharWidth);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmWeight, lpTextmetric->tmWeight);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmOverhang, lpTextmetric->tmOverhang);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmDigitizedAspectX, lpTextmetric->tmDigitizedAspectX);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmDigitizedAspectY, lpTextmetric->tmDigitizedAspectY);
    (*env)->SetByteField(env,lpObject,lpTextmetricFc->tmFirstChar, lpTextmetric->tmFirstChar);
    (*env)->SetByteField(env,lpObject,lpTextmetricFc->tmLastChar, lpTextmetric->tmLastChar);
    (*env)->SetByteField(env,lpObject,lpTextmetricFc->tmDefaultChar, lpTextmetric->tmDefaultChar);
    (*env)->SetByteField(env,lpObject,lpTextmetricFc->tmBreakChar, lpTextmetric->tmBreakChar);
    (*env)->SetByteField(env,lpObject,lpTextmetricFc->tmItalic, lpTextmetric->tmItalic);
    (*env)->SetByteField(env,lpObject,lpTextmetricFc->tmUnderlined, lpTextmetric->tmUnderlined);
    (*env)->SetByteField(env,lpObject,lpTextmetricFc->tmStruckOut, lpTextmetric->tmStruckOut);
    (*env)->SetByteField(env,lpObject,lpTextmetricFc->tmPitchAndFamily, lpTextmetric->tmPitchAndFamily);
    (*env)->SetByteField(env,lpObject,lpTextmetricFc->tmCharSet, lpTextmetric->tmCharSet);
}
void setTextmetricFieldsW(JNIEnv *env, jobject lpObject, TEXTMETRICW *lpTextmetric, TEXTMETRIC_FID_CACHE *lpTextmetricFc)
{
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmHeight, lpTextmetric->tmHeight);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmAscent, lpTextmetric->tmAscent);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmDescent, lpTextmetric->tmDescent);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmInternalLeading, lpTextmetric->tmInternalLeading);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmExternalLeading, lpTextmetric->tmExternalLeading);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmAveCharWidth, lpTextmetric->tmAveCharWidth);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmMaxCharWidth, lpTextmetric->tmMaxCharWidth);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmWeight, lpTextmetric->tmWeight);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmOverhang, lpTextmetric->tmOverhang);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmDigitizedAspectX, lpTextmetric->tmDigitizedAspectX);
    (*env)->SetIntField(env,lpObject,lpTextmetricFc->tmDigitizedAspectY, lpTextmetric->tmDigitizedAspectY);
    (*env)->SetCharField(env,lpObject,lpTextmetricFc->tmFirstChar, lpTextmetric->tmFirstChar);
    (*env)->SetCharField(env,lpObject,lpTextmetricFc->tmLastChar, lpTextmetric->tmLastChar);
    (*env)->SetCharField(env,lpObject,lpTextmetricFc->tmDefaultChar, lpTextmetric->tmDefaultChar);
    (*env)->SetCharField(env,lpObject,lpTextmetricFc->tmBreakChar, lpTextmetric->tmBreakChar);
    (*env)->SetByteField(env,lpObject,lpTextmetricFc->tmItalic, lpTextmetric->tmItalic);
    (*env)->SetByteField(env,lpObject,lpTextmetricFc->tmUnderlined, lpTextmetric->tmUnderlined);
    (*env)->SetByteField(env,lpObject,lpTextmetricFc->tmStruckOut, lpTextmetric->tmStruckOut);
    (*env)->SetByteField(env,lpObject,lpTextmetricFc->tmPitchAndFamily, lpTextmetric->tmPitchAndFamily);
    (*env)->SetByteField(env,lpObject,lpTextmetricFc->tmCharSet, lpTextmetric->tmCharSet);
}

void getTvhittestinfoFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpTvhittestinfo, TVHITTESTINFO_FID_CACHE *lpTvhittestinfoFc)
{
    lpTvhittestinfo->pt.x = (*env)->GetIntField(env,lpObject,lpTvhittestinfoFc->x);
    lpTvhittestinfo->pt.y = (*env)->GetIntField(env,lpObject,lpTvhittestinfoFc->y);
    lpTvhittestinfo->flags = (*env)->GetIntField(env,lpObject,lpTvhittestinfoFc->flags);
    lpTvhittestinfo->hItem = (HTREEITEM)(*env)->GetIntField(env,lpObject,lpTvhittestinfoFc->hItem);
}

void setTvhittestinfoFields(JNIEnv *env, jobject lpObject, TVHITTESTINFO *lpTvhittestinfo, TVHITTESTINFO_FID_CACHE *lpTvhittestinfoFc)
{
    (*env)->SetIntField(env,lpObject,lpTvhittestinfoFc->x, lpTvhittestinfo->pt.x);
    (*env)->SetIntField(env,lpObject,lpTvhittestinfoFc->y, lpTvhittestinfo->pt.y);
    (*env)->SetIntField(env,lpObject,lpTvhittestinfoFc->flags, lpTvhittestinfo->flags);
    (*env)->SetIntField(env,lpObject,lpTvhittestinfoFc->hItem, (jint)lpTvhittestinfo->hItem);
}

void getTvinsertstructFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpTvinsertstruct, TVINSERTSTRUCT_FID_CACHE *lpTvinsertstructFc)
{
    lpTvinsertstruct->hParent = (HTREEITEM)(*env)->GetIntField(env,lpObject,lpTvinsertstructFc->hParent);
    lpTvinsertstruct->hInsertAfter = (HTREEITEM)(*env)->GetIntField(env,lpObject,lpTvinsertstructFc->hInsertAfter);

    lpTvinsertstruct->item.mask = (*env)->GetIntField(env,lpObject,lpTvinsertstructFc->mask);
    lpTvinsertstruct->item.hItem = (HTREEITEM)(*env)->GetIntField(env,lpObject,lpTvinsertstructFc->hItem);
    lpTvinsertstruct->item.state = (*env)->GetIntField(env,lpObject,lpTvinsertstructFc->state);
    lpTvinsertstruct->item.stateMask = (*env)->GetIntField(env,lpObject,lpTvinsertstructFc->stateMask);
    lpTvinsertstruct->item.pszText = (LPTSTR)(*env)->GetIntField(env,lpObject,lpTvinsertstructFc->pszText);
    lpTvinsertstruct->item.cchTextMax = (*env)->GetIntField(env,lpObject,lpTvinsertstructFc->cchTextMax);
    lpTvinsertstruct->item.iImage = (*env)->GetIntField(env,lpObject,lpTvinsertstructFc->iImage);
    lpTvinsertstruct->item.iSelectedImage = (*env)->GetIntField(env,lpObject,lpTvinsertstructFc->iSelectedImage);
    lpTvinsertstruct->item.cChildren = (*env)->GetIntField(env,lpObject,lpTvinsertstructFc->cChildren);
    lpTvinsertstruct->item.lParam = (*env)->GetIntField(env,lpObject,lpTvinsertstructFc->lParam);
}

void setTvinsertstructFields(JNIEnv *env, jobject lpObject, TVINSERTSTRUCT *lpTvinsertstruct, TVINSERTSTRUCT_FID_CACHE *lpTvinsertstructFc)
{
    (*env)->SetIntField(env,lpObject,lpTvinsertstructFc->hParent, (jint)lpTvinsertstruct->hParent);
    (*env)->SetIntField(env,lpObject,lpTvinsertstructFc->hInsertAfter, (jint)lpTvinsertstruct->hInsertAfter);

    (*env)->SetIntField(env,lpObject,lpTvinsertstructFc->mask, lpTvinsertstruct->item.mask);
    (*env)->SetIntField(env,lpObject,lpTvinsertstructFc->hItem, (jint)lpTvinsertstruct->item.hItem);
    (*env)->SetIntField(env,lpObject,lpTvinsertstructFc->state, lpTvinsertstruct->item.state);
    (*env)->SetIntField(env,lpObject,lpTvinsertstructFc->stateMask, lpTvinsertstruct->item.stateMask);
    (*env)->SetIntField(env,lpObject,lpTvinsertstructFc->pszText, (jint)lpTvinsertstruct->item.pszText);
    (*env)->SetIntField(env,lpObject,lpTvinsertstructFc->cchTextMax, lpTvinsertstruct->item.cchTextMax);
    (*env)->SetIntField(env,lpObject,lpTvinsertstructFc->iImage, lpTvinsertstruct->item.iImage);
    (*env)->SetIntField(env,lpObject,lpTvinsertstructFc->iSelectedImage, lpTvinsertstruct->item.iSelectedImage);
    (*env)->SetIntField(env,lpObject,lpTvinsertstructFc->cChildren, lpTvinsertstruct->item.cChildren);
    (*env)->SetIntField(env,lpObject,lpTvinsertstructFc->lParam, lpTvinsertstruct->item.lParam);
}

void getTvitemFields(JNIEnv *env, jobject lpObject, TVITEM *lpTvitem, TVITEM_FID_CACHE *lpTvitemFc)
{
    lpTvitem->mask = (*env)->GetIntField(env,lpObject,lpTvitemFc->mask);
    lpTvitem->hItem = (HTREEITEM)(*env)->GetIntField(env,lpObject,lpTvitemFc->hItem);
    lpTvitem->state = (*env)->GetIntField(env,lpObject,lpTvitemFc->state);
    lpTvitem->stateMask = (*env)->GetIntField(env,lpObject,lpTvitemFc->stateMask);
    lpTvitem->pszText = (LPTSTR)(*env)->GetIntField(env,lpObject,lpTvitemFc->pszText);
    lpTvitem->cchTextMax = (*env)->GetIntField(env,lpObject,lpTvitemFc->cchTextMax);
    lpTvitem->iImage = (*env)->GetIntField(env,lpObject,lpTvitemFc->iImage);
    lpTvitem->iSelectedImage = (*env)->GetIntField(env,lpObject,lpTvitemFc->iSelectedImage);
    lpTvitem->cChildren = (*env)->GetIntField(env,lpObject,lpTvitemFc->cChildren);
    lpTvitem->lParam = (*env)->GetIntField(env,lpObject,lpTvitemFc->lParam);
}

void setTvitemFields(JNIEnv *env, jobject lpObject, TVITEM *lpTvitem, TVITEM_FID_CACHE *lpTvitemFc)
{
    (*env)->SetIntField(env,lpObject,lpTvitemFc->mask, lpTvitem->mask);
    (*env)->SetIntField(env,lpObject,lpTvitemFc->hItem, (jint)lpTvitem->hItem);
    (*env)->SetIntField(env,lpObject,lpTvitemFc->state, lpTvitem->state);
    (*env)->SetIntField(env,lpObject,lpTvitemFc->stateMask, lpTvitem->stateMask);
    (*env)->SetIntField(env,lpObject,lpTvitemFc->pszText, (jint)lpTvitem->pszText);
    (*env)->SetIntField(env,lpObject,lpTvitemFc->cchTextMax, lpTvitem->cchTextMax);
    (*env)->SetIntField(env,lpObject,lpTvitemFc->iImage, lpTvitem->iImage);
    (*env)->SetIntField(env,lpObject,lpTvitemFc->iSelectedImage, lpTvitem->iSelectedImage);
    (*env)->SetIntField(env,lpObject,lpTvitemFc->cChildren, lpTvitem->cChildren);
    (*env)->SetIntField(env,lpObject,lpTvitemFc->lParam, lpTvitem->lParam);
}

void getWindowposFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpWindowpos, WINDOWPOS_FID_CACHE *lpWindowposFc)
{
    lpWindowpos->hwnd = (HWND)(*env)->GetIntField(env,lpObject,lpWindowposFc->hwnd);
    lpWindowpos->hwndInsertAfter = (HWND)(*env)->GetIntField(env,lpObject,lpWindowposFc->hwndInsertAfter);
    lpWindowpos->x = (*env)->GetIntField(env,lpObject,lpWindowposFc->x);
    lpWindowpos->y = (*env)->GetIntField(env,lpObject,lpWindowposFc->y);
    lpWindowpos->cx = (*env)->GetIntField(env,lpObject,lpWindowposFc->cx);
    lpWindowpos->cy = (*env)->GetIntField(env,lpObject,lpWindowposFc->cy);
    lpWindowpos->flags = (*env)->GetIntField(env,lpObject,lpWindowposFc->flags);
}

void setWindowposFields(JNIEnv *env, jobject lpObject, WINDOWPOS *lpWindowpos, WINDOWPOS_FID_CACHE *lpWindowposFc)
{
    (*env)->SetIntField(env,lpObject,lpWindowposFc->hwnd, (jint)lpWindowpos->hwnd);
    (*env)->SetIntField(env,lpObject,lpWindowposFc->hwndInsertAfter, (jint)lpWindowpos->hwndInsertAfter);
    (*env)->SetIntField(env,lpObject,lpWindowposFc->x, lpWindowpos->x);
    (*env)->SetIntField(env,lpObject,lpWindowposFc->y, lpWindowpos->y);
    (*env)->SetIntField(env,lpObject,lpWindowposFc->cx, lpWindowpos->cx);
    (*env)->SetIntField(env,lpObject,lpWindowposFc->cy, lpWindowpos->cy);
    (*env)->SetIntField(env,lpObject,lpWindowposFc->flags, lpWindowpos->flags);
}

void getWndclassFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpWndclass, WNDCLASS_FID_CACHE *lpWndclassFc)
{
    lpWndclass->style = (*env)->GetIntField(env,lpObject,lpWndclassFc->style);
    lpWndclass->lpfnWndProc = (WNDPROC)(*env)->GetIntField(env,lpObject,lpWndclassFc->lpfnWndProc);
    lpWndclass->cbClsExtra = (*env)->GetIntField(env,lpObject,lpWndclassFc->cbClsExtra);
    lpWndclass->cbWndExtra = (*env)->GetIntField(env,lpObject,lpWndclassFc->cbWndExtra);
    lpWndclass->hInstance = (HINSTANCE)(*env)->GetIntField(env,lpObject,lpWndclassFc->hInstance);
    lpWndclass->hIcon = (HICON)(*env)->GetIntField(env,lpObject,lpWndclassFc->hIcon);
    lpWndclass->hCursor = (HICON)(*env)->GetIntField(env,lpObject,lpWndclassFc->hCursor);
    lpWndclass->hbrBackground = (HBRUSH)(*env)->GetIntField(env,lpObject,lpWndclassFc->hbrBackground);
    lpWndclass->lpszMenuName = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpWndclassFc->lpszMenuName);
    lpWndclass->lpszClassName = (LPCTSTR)(*env)->GetIntField(env,lpObject,lpWndclassFc->lpszClassName);
}

void setWndclassFields(JNIEnv *env, jobject lpObject, WNDCLASS *lpWndclass, WNDCLASS_FID_CACHE *lpWndclassFc)
{
    (*env)->SetIntField(env,lpObject,lpWndclassFc->style, lpWndclass->style);
    (*env)->SetIntField(env,lpObject,lpWndclassFc->lpfnWndProc, (jint)lpWndclass->lpfnWndProc);
    (*env)->SetIntField(env,lpObject,lpWndclassFc->cbClsExtra, lpWndclass->cbClsExtra);
    (*env)->SetIntField(env,lpObject,lpWndclassFc->cbWndExtra, lpWndclass->cbWndExtra);
    (*env)->SetIntField(env,lpObject,lpWndclassFc->hInstance, (jint)lpWndclass->hInstance);
    (*env)->SetIntField(env,lpObject,lpWndclassFc->hIcon, (jint)lpWndclass->hIcon);
    (*env)->SetIntField(env,lpObject,lpWndclassFc->hCursor, (jint)lpWndclass->hCursor);
    (*env)->SetIntField(env,lpObject,lpWndclassFc->hbrBackground, (jint)lpWndclass->hbrBackground);
    (*env)->SetIntField(env,lpObject,lpWndclassFc->lpszMenuName, (jint)lpWndclass->lpszMenuName);
    (*env)->SetIntField(env,lpObject,lpWndclassFc->lpszClassName, (jint)lpWndclass->lpszClassName);
}
    
/*
* NOTE: Untested - szDisplayName and szTypeName don't work
*/
/*
void getShfileinfoFields(JNIEnv *env, jobject lpObject, SHFILEINFO *lpShfileinfo, SHFILEINFO_FID_CACHE *lpShfileinfoFc)
{
    lpShfileinfo->hIcon = (HICON) (*env)->GetIntField(env,lpObject,lpShfileinfoFc->hIcon);
    lpShfileinfo->iIcon = (int) (*env)->GetIntField(env,lpObject,lpShfileinfoFc->iIcon);
    lpShfileinfo->dwAttributes = (DWORD)(*env)->GetIntField(env,lpObject,lpShfileinfoFc->dwAttributes);
    {
    jbyteArray szDisplayName = (*env)->GetObjectField(env,lpObject,lpShfileinfoFc->szDisplayName);
	if (szDisplayName) {
    	jbyte * szDisplayName1 = (*env)->GetByteArrayElements(env, szDisplayName,NULL);
    	memcpy (lpShfileinfo->szDisplayName, szDisplayName1, sizeof (lpShfileinfo->szDisplayName));
    	(*env)->ReleaseByteArrayElements(env,szDisplayName, szDisplayName1, NULL);
    }
    }
    {
    jbyteArray szTypeName = (*env)->GetObjectField(env,lpObject,lpShfileinfoFc->szTypeName);
	if (szTypeName) {
    	jbyte * szTypeName1 = (*env)->GetByteArrayElements(env, szTypeName,NULL);
    	memcpy (lpShfileinfo->szTypeName, szTypeName1, sizeof (lpShfileinfo->szTypeName));
    	(*env)->ReleaseByteArrayElements(env, szTypeName, szTypeName1, NULL);
    }
    }
}

void setShfileinfoFields(JNIEnv *env, jobject lpObject, SHFILEINFO *lpShfileinfo, SHFILEINFO_FID_CACHE *lpShfileinfoFc)
{
    (*env)->SetIntField(env,lpObject,lpShfileinfoFc->hIcon, lpShfileinfo->hIcon);
    (*env)->SetIntField(env,lpObject,lpShfileinfoFc->iIcon, lpShfileinfo->iIcon);
    (*env)->SetIntField(env,lpObject,lpShfileinfoFc->dwAttributes, lpShfileinfo->dwAttributes);
    {
    jbyteArray szDisplayName = (*env)->GetObjectField(env,lpObject,lpShfileinfoFc->szDisplayName);
	if (szDisplayName) {
    	jbyte * szDisplayName1 = (*env)->GetByteArrayElements(env,szDisplayName,NULL);
    	memcpy (szDisplayName1, lpShfileinfo->szDisplayName, sizeof (lpShfileinfo->szDisplayName));
    	(*env)->ReleaseByteArrayElements(env,szDisplayName,szDisplayName1, NULL);
    }
    }
    {
    jbyteArray szTypeName = (*env)->GetObjectField(env,lpObject,lpShfileinfoFc->szTypeName);
	if (szTypeName) {
    	jbyte * szTypeName1 = (*env)->GetByteArrayElements(env,szTypeName,NULL);
    	memcpy (szTypeName1, lpShfileinfo->szTypeName, sizeof (lpShfileinfo->szTypeName));
    	(*env)->ReleaseByteArrayElements(env,szTypeName,szTypeName1, NULL);
    }
    }
}
*/
    	
/* ----------- activex/ole getters and setters  ----------- */

void getCauuidFields(JNIEnv *env, jobject lpObject, CAUUID *lpCauuid, CAUUID_FID_CACHE *lpCauuidFc)
{
    lpCauuid->cElems = (*env)->GetIntField(env,lpObject,lpCauuidFc->cElems);
    lpCauuid->pElems = (GUID FAR*)(*env)->GetIntField(env,lpObject,lpCauuidFc->pElems);
}

void setCauuidFields(JNIEnv *env, jobject lpObject, CAUUID *lpCauuid, CAUUID_FID_CACHE *lpCauuidFc)
{
    (*env)->SetIntField(env,lpObject,lpCauuidFc->cElems, lpCauuid->cElems);
    (*env)->SetIntField(env,lpObject,lpCauuidFc->pElems, (jint)lpCauuid->pElems);
}

void getCoserverinfoFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpCoserverinfo, COSERVERINFO_FID_CACHE *lpCoserverinfoFc)
{
    lpCoserverinfo->dwReserved1 = (*env)->GetIntField(env,lpObject,lpCoserverinfoFc->dwReserved1);
    lpCoserverinfo->pwszName = (LPWSTR)(*env)->GetIntField(env,lpObject,lpCoserverinfoFc->pwszName);
    lpCoserverinfo->pAuthInfo = (COAUTHINFO *)(*env)->GetIntField(env,lpObject,lpCoserverinfoFc->pAuthInfo);
    lpCoserverinfo->dwReserved2 = (*env)->GetIntField(env,lpObject,lpCoserverinfoFc->dwReserved2);
}

void setCoserverinfoFields(JNIEnv *env, jobject lpObject, COSERVERINFO *lpCoserverinfo, COSERVERINFO_FID_CACHE *lpCoserverinfoFc)
{
    (*env)->SetIntField(env,lpObject,lpCoserverinfoFc->dwReserved1, lpCoserverinfo->dwReserved1);
    (*env)->SetIntField(env,lpObject,lpCoserverinfoFc->pwszName, (jint)lpCoserverinfo->pwszName);
    (*env)->SetIntField(env,lpObject,lpCoserverinfoFc->pAuthInfo, (jint)lpCoserverinfo->pAuthInfo);
    (*env)->SetIntField(env,lpObject,lpCoserverinfoFc->dwReserved2, lpCoserverinfo->dwReserved2);
}

void getControlinfoFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpControlinfo, CONTROLINFO_FID_CACHE *lpControlinfoFc)
{
    lpControlinfo->cb = (*env)->GetIntField(env,lpObject,lpControlinfoFc->cb);
    lpControlinfo->hAccel = (HACCEL)(*env)->GetIntField(env,lpObject,lpControlinfoFc->hAccel);
    lpControlinfo->cAccel = (*env)->GetShortField(env,lpObject,lpControlinfoFc->cAccel);
    lpControlinfo->dwFlags = (*env)->GetIntField(env,lpObject,lpControlinfoFc->dwFlags);
}

void setControlinfoFields(JNIEnv *env, jobject lpObject, CONTROLINFO *lpControlinfo, CONTROLINFO_FID_CACHE *lpControlinfoFc)
{
    (*env)->SetIntField(env,lpObject,lpControlinfoFc->cb, lpControlinfo->cb);
    (*env)->SetIntField(env,lpObject,lpControlinfoFc->hAccel, (jint)lpControlinfo->hAccel);
    (*env)->SetShortField(env,lpObject,lpControlinfoFc->cAccel, lpControlinfo->cAccel);
    (*env)->SetIntField(env,lpObject,lpControlinfoFc->dwFlags, lpControlinfo->dwFlags);
}

void getDvtargetdeviceFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpDvtargetdevice, DVTARGETDEVICE_FID_CACHE *lpDvtargetdeviceFc)
{
    lpDvtargetdevice->tdSize = (*env)->GetIntField(env,lpObject,lpDvtargetdeviceFc->tdSize);
    lpDvtargetdevice->tdDriverNameOffset = (*env)->GetShortField(env,lpObject,lpDvtargetdeviceFc->tdDriverNameOffset);
    lpDvtargetdevice->tdDeviceNameOffset = (*env)->GetShortField(env,lpObject,lpDvtargetdeviceFc->tdDeviceNameOffset);
    lpDvtargetdevice->tdPortNameOffset = (*env)->GetShortField(env,lpObject,lpDvtargetdeviceFc->tdPortNameOffset);
    lpDvtargetdevice->tdExtDevmodeOffset = (*env)->GetShortField(env,lpObject,lpDvtargetdeviceFc->tdExtDevmodeOffset);
    *lpDvtargetdevice->tdData = (*env)->GetByteField(env,lpObject,lpDvtargetdeviceFc->tdData);
}

void setDvtargetdeviceFields(JNIEnv *env, jobject lpObject, DVTARGETDEVICE *lpDvtargetdevice, DVTARGETDEVICE_FID_CACHE *lpDvtargetdeviceFc)
{
    (*env)->SetIntField(env,lpObject,lpDvtargetdeviceFc->tdSize, lpDvtargetdevice->tdSize);
    (*env)->SetShortField(env,lpObject,lpDvtargetdeviceFc->tdDriverNameOffset, lpDvtargetdevice->tdDriverNameOffset);
    (*env)->SetShortField(env,lpObject,lpDvtargetdeviceFc->tdDeviceNameOffset, lpDvtargetdevice->tdDeviceNameOffset);
    (*env)->SetShortField(env,lpObject,lpDvtargetdeviceFc->tdPortNameOffset, lpDvtargetdevice->tdPortNameOffset);
    (*env)->SetShortField(env,lpObject,lpDvtargetdeviceFc->tdExtDevmodeOffset, lpDvtargetdevice->tdExtDevmodeOffset);
    (*env)->SetByteField(env,lpObject,lpDvtargetdeviceFc->tdData, *lpDvtargetdevice->tdData);
}

void getFormatetcFields(JNIEnv *env, jobject lpObject, FORMATETC *lpFormatetc, FORMATETC_FID_CACHE *lpFormatetcFc)
{
    lpFormatetc->cfFormat = (CLIPFORMAT)(*env)->GetIntField(env,lpObject,lpFormatetcFc->cfFormat);
    lpFormatetc->ptd = (DVTARGETDEVICE *)(*env)->GetIntField(env,lpObject,lpFormatetcFc->ptd);
    lpFormatetc->dwAspect = (*env)->GetIntField(env,lpObject,lpFormatetcFc->dwAspect);
    lpFormatetc->lindex = (*env)->GetIntField(env,lpObject,lpFormatetcFc->lindex);
    lpFormatetc->tymed = (*env)->GetIntField(env,lpObject,lpFormatetcFc->tymed);
}

void setFormatetcFields(JNIEnv *env, jobject lpObject, FORMATETC *lpFormatetc, FORMATETC_FID_CACHE *lpFormatetcFc)
{
    (*env)->SetIntField(env,lpObject,lpFormatetcFc->cfFormat, (jint)lpFormatetc->cfFormat);
    (*env)->SetIntField(env,lpObject,lpFormatetcFc->ptd, (jint)lpFormatetc->ptd);
    (*env)->SetIntField(env,lpObject,lpFormatetcFc->dwAspect, lpFormatetc->dwAspect);
    (*env)->SetIntField(env,lpObject,lpFormatetcFc->lindex, lpFormatetc->lindex);
    (*env)->SetIntField(env,lpObject,lpFormatetcFc->tymed, lpFormatetc->tymed);
}

void getDispparamsFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpDispparams, DISPPARAMS_FID_CACHE *lpDispparamsFc)
{
    lpDispparams->rgvarg = (VARIANTARG FAR* )(*env)->GetIntField(env,lpObject,lpDispparamsFc->rgvarg);
    lpDispparams->rgdispidNamedArgs = (DISPID FAR* )(*env)->GetIntField(env,lpObject,lpDispparamsFc->rgdispidNamedArgs);
    lpDispparams->cArgs = (*env)->GetIntField(env,lpObject,lpDispparamsFc->cArgs);
    lpDispparams->cNamedArgs = (*env)->GetIntField(env,lpObject,lpDispparamsFc->cNamedArgs);
}

void setDispparamsFields(JNIEnv *env, jobject lpObject, DISPPARAMS *lpDispparams, DISPPARAMS_FID_CACHE *lpDispparamsFc)
{
    (*env)->SetIntField(env,lpObject,lpDispparamsFc->rgvarg, (jint)lpDispparams->rgvarg);
    (*env)->SetIntField(env,lpObject,lpDispparamsFc->rgdispidNamedArgs, (jint)lpDispparams->rgdispidNamedArgs);
    (*env)->SetIntField(env,lpObject,lpDispparamsFc->cArgs, lpDispparams->cArgs);
    (*env)->SetIntField(env,lpObject,lpDispparamsFc->cNamedArgs, lpDispparams->cNamedArgs);
}

void getDvaspectinfoFields(JNIEnv *env, jobject lpObject, DVASPECTINFO *lpDvaspectinfo, DVASPECTINFO_FID_CACHE *lpDvaspectinfoFc)
{
    lpDvaspectinfo->cb = (*env)->GetIntField(env,lpObject,lpDvaspectinfoFc->cb);
    lpDvaspectinfo->dwFlags = (*env)->GetIntField(env,lpObject,lpDvaspectinfoFc->dwFlags);
}

void setDvaspectinfoFields(JNIEnv *env, jobject lpObject, DVASPECTINFO *lpDvaspectinfo, DVASPECTINFO_FID_CACHE *lpDvaspectinfoFc)
{
    (*env)->SetIntField(env,lpObject,lpDvaspectinfoFc->cb, lpDvaspectinfo->cb);
    (*env)->SetIntField(env,lpObject,lpDvaspectinfoFc->dwFlags, lpDvaspectinfo->dwFlags);
}

void getExcepinfoFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpExcepinfo, EXCEPINFO_FID_CACHE *lpExcepinfoFc)
{
    lpExcepinfo->wCode = (*env)->GetShortField(env,lpObject,lpExcepinfoFc->wCode);
    lpExcepinfo->wReserved = (*env)->GetShortField(env,lpObject,lpExcepinfoFc->wReserved);
    lpExcepinfo->bstrSource = (BSTR)(*env)->GetIntField(env,lpObject,lpExcepinfoFc->bstrSource);
    lpExcepinfo->bstrDescription = (BSTR)(*env)->GetIntField(env,lpObject,lpExcepinfoFc->bstrDescription);
    lpExcepinfo->bstrHelpFile = (BSTR)(*env)->GetIntField(env,lpObject,lpExcepinfoFc->bstrHelpFile);
    lpExcepinfo->dwHelpContext = (*env)->GetIntField(env,lpObject,lpExcepinfoFc->dwHelpContext);
    lpExcepinfo->pvReserved = (void FAR* )(*env)->GetIntField(env,lpObject,lpExcepinfoFc->pvReserved);
    lpExcepinfo->pfnDeferredFillIn = (HRESULT (STDAPICALLTYPE FAR* )(struct tagEXCEPINFO FAR*))(*env)->GetIntField(env,lpObject,lpExcepinfoFc->pfnDeferredFillIn);
    lpExcepinfo->scode = (*env)->GetIntField(env,lpObject,lpExcepinfoFc->scode);
}

void setExcepinfoFields(JNIEnv *env, jobject lpObject, EXCEPINFO *lpExcepinfo, EXCEPINFO_FID_CACHE *lpExcepinfoFc)
{
    (*env)->SetShortField(env,lpObject,lpExcepinfoFc->wCode, lpExcepinfo->wCode);
    (*env)->SetShortField(env,lpObject,lpExcepinfoFc->wReserved, lpExcepinfo->wReserved);
    (*env)->SetIntField(env,lpObject,lpExcepinfoFc->bstrSource, (jint)lpExcepinfo->bstrSource);
    (*env)->SetIntField(env,lpObject,lpExcepinfoFc->bstrDescription, (jint)lpExcepinfo->bstrDescription);
    (*env)->SetIntField(env,lpObject,lpExcepinfoFc->bstrHelpFile, (jint)lpExcepinfo->bstrHelpFile);
    (*env)->SetIntField(env,lpObject,lpExcepinfoFc->dwHelpContext, lpExcepinfo->dwHelpContext);
    (*env)->SetIntField(env,lpObject,lpExcepinfoFc->pvReserved, (jint)lpExcepinfo->pvReserved);
    (*env)->SetIntField(env,lpObject,lpExcepinfoFc->pfnDeferredFillIn, (jint)lpExcepinfo->pfnDeferredFillIn);
    (*env)->SetIntField(env,lpObject,lpExcepinfoFc->scode, lpExcepinfo->scode);
}

void getFiletimeFields(JNIEnv *env, jobject lpObject, FILETIME *lpFiletime, FILETIME_FID_CACHE *lpFiletimeFc)
{
    lpFiletime->dwLowDateTime = (*env)->GetIntField(env,lpObject,lpFiletimeFc->dwLowDateTime);
    lpFiletime->dwHighDateTime = (*env)->GetIntField(env,lpObject,lpFiletimeFc->dwHighDateTime);
}

void setFiletimeFields(JNIEnv *env, jobject lpObject, FILETIME *lpFiletime, FILETIME_FID_CACHE *lpFiletimeFc)
{
    (*env)->SetIntField(env,lpObject,lpFiletimeFc->dwLowDateTime, lpFiletime->dwLowDateTime);
    (*env)->SetIntField(env,lpObject,lpFiletimeFc->dwHighDateTime, lpFiletime->dwHighDateTime);
}

void getGuidFields(JNIEnv *env, jobject lpObject, GUID *lpGuid, GUID_FID_CACHE *lpGuidFc)
{
    lpGuid->Data1 = (*env)->GetIntField(env,lpObject,lpGuidFc->data1);
    lpGuid->Data2 = (*env)->GetShortField(env,lpObject,lpGuidFc->data2);
    lpGuid->Data3 = (*env)->GetShortField(env,lpObject,lpGuidFc->data3);
    lpGuid->Data4[0] = (*env)->GetByteField(env,lpObject,lpGuidFc->b0);
    lpGuid->Data4[1] = (*env)->GetByteField(env,lpObject,lpGuidFc->b1);
    lpGuid->Data4[2] = (*env)->GetByteField(env,lpObject,lpGuidFc->b2);
    lpGuid->Data4[3] = (*env)->GetByteField(env,lpObject,lpGuidFc->b3);
    lpGuid->Data4[4] = (*env)->GetByteField(env,lpObject,lpGuidFc->b4);
    lpGuid->Data4[5] = (*env)->GetByteField(env,lpObject,lpGuidFc->b5);
    lpGuid->Data4[6] = (*env)->GetByteField(env,lpObject,lpGuidFc->b6);
    lpGuid->Data4[7] = (*env)->GetByteField(env,lpObject,lpGuidFc->b7);
}

void setGuidFields(JNIEnv *env, jobject lpObject, GUID *lpGuid, GUID_FID_CACHE *lpGuidFc)
{
    (*env)->SetIntField(env,lpObject,lpGuidFc->data1, lpGuid->Data1);
    (*env)->SetShortField(env,lpObject,lpGuidFc->data2, lpGuid->Data2);
    (*env)->SetShortField(env,lpObject,lpGuidFc->data3, lpGuid->Data3);
    (*env)->SetByteField(env,lpObject,lpGuidFc->b0, lpGuid->Data4[0]);
    (*env)->SetByteField(env,lpObject,lpGuidFc->b1, lpGuid->Data4[1]);
    (*env)->SetByteField(env,lpObject,lpGuidFc->b2, lpGuid->Data4[2]);
    (*env)->SetByteField(env,lpObject,lpGuidFc->b3, lpGuid->Data4[3]);
    (*env)->SetByteField(env,lpObject,lpGuidFc->b4, lpGuid->Data4[4]);
    (*env)->SetByteField(env,lpObject,lpGuidFc->b5, lpGuid->Data4[5]);
    (*env)->SetByteField(env,lpObject,lpGuidFc->b6, lpGuid->Data4[6]);
    (*env)->SetByteField(env,lpObject,lpGuidFc->b7, lpGuid->Data4[7]);
}

void getIdldescFields(JNIEnv *env, jobject lpObject, IDLDESC *lpIdldesc, IDLDESC_FID_CACHE *lpIdldescFc)
{
    lpIdldesc->dwReserved = (*env)->GetIntField(env,lpObject,lpIdldescFc->dwReserved);
    lpIdldesc->wIDLFlags = (*env)->GetShortField(env,lpObject,lpIdldescFc->wIDLFlags);
}

void setIdldescFields(JNIEnv *env, jobject lpObject, IDLDESC *lpIdldesc, IDLDESC_FID_CACHE *lpIdldescFc)
{
    (*env)->SetIntField(env,lpObject,lpIdldescFc->dwReserved, lpIdldesc->dwReserved);
    (*env)->SetShortField(env,lpObject,lpIdldescFc->wIDLFlags, lpIdldesc->wIDLFlags);
}

void getLicinfoFields(JNIEnv *env, jobject lpObject, LICINFO *lpLicinfo, LICINFO_FID_CACHE *lpLicinfoFc)
{
    lpLicinfo->cbLicInfo = (*env)->GetIntField(env,lpObject,lpLicinfoFc->cbLicInfo);
    lpLicinfo->fRuntimeKeyAvail = (*env)->GetIntField(env,lpObject,lpLicinfoFc->fRuntimeKeyAvail);
    lpLicinfo->fLicVerified = (*env)->GetIntField(env,lpObject,lpLicinfoFc->fLicVerified);
}

void setLicinfoFields(JNIEnv *env, jobject lpObject, LICINFO *lpLicinfo, LICINFO_FID_CACHE *lpLicinfoFc)
{
    (*env)->SetIntField(env,lpObject,lpLicinfoFc->cbLicInfo, lpLicinfo->cbLicInfo);
    (*env)->SetIntField(env,lpObject,lpLicinfoFc->fRuntimeKeyAvail, lpLicinfo->fRuntimeKeyAvail);
    (*env)->SetIntField(env,lpObject,lpLicinfoFc->fLicVerified, lpLicinfo->fLicVerified);
}

void getMulti_qiFields(JNIEnv *env, jobject lpObject, MULTI_QI *lpMulti_qi, MULTI_QI_FID_CACHE *lpMulti_qiFc)
{
    lpMulti_qi->pIID = (const IID*)(*env)->GetIntField(env,lpObject,lpMulti_qiFc->pIID);
    lpMulti_qi->pItf = (IUnknown *)(*env)->GetIntField(env,lpObject,lpMulti_qiFc->pItf);
    lpMulti_qi->hr = (HRESULT)(*env)->GetIntField(env,lpObject,lpMulti_qiFc->hr);
}

void setMulti_qiFields(JNIEnv *env, jobject lpObject, MULTI_QI *lpMulti_qi, MULTI_QI_FID_CACHE *lpMulti_qiFc)
{
    (*env)->SetIntField(env,lpObject,lpMulti_qiFc->pIID, (jint)lpMulti_qi->pIID);
    (*env)->SetIntField(env,lpObject,lpMulti_qiFc->pItf, (jint)lpMulti_qi->pItf);
    (*env)->SetIntField(env,lpObject,lpMulti_qiFc->hr, (jint)lpMulti_qi->hr);
}

void getOleinplaceframeinfoFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpOleinplaceframeinfo, OLEINPLACEFRAMEINFO_FID_CACHE *lpOleinplaceframeinfoFc)
{
    lpOleinplaceframeinfo->cb = (*env)->GetIntField(env,lpObject,lpOleinplaceframeinfoFc->cb);
    lpOleinplaceframeinfo->fMDIApp = (*env)->GetIntField(env,lpObject,lpOleinplaceframeinfoFc->fMDIApp);
    lpOleinplaceframeinfo->hwndFrame = (HWND)(*env)->GetIntField(env,lpObject,lpOleinplaceframeinfoFc->hwndFrame);
    lpOleinplaceframeinfo->haccel = (HACCEL)(*env)->GetIntField(env,lpObject,lpOleinplaceframeinfoFc->haccel);
    lpOleinplaceframeinfo->cAccelEntries = (*env)->GetIntField(env,lpObject,lpOleinplaceframeinfoFc->cAccelEntries);
}

void setOleinplaceframeinfoFields(JNIEnv *env, jobject lpObject, OLEINPLACEFRAMEINFO *lpOleinplaceframeinfo, OLEINPLACEFRAMEINFO_FID_CACHE *lpOleinplaceframeinfoFc)
{
    (*env)->SetIntField(env,lpObject,lpOleinplaceframeinfoFc->cb, lpOleinplaceframeinfo->cb);
    (*env)->SetIntField(env,lpObject,lpOleinplaceframeinfoFc->fMDIApp, lpOleinplaceframeinfo->fMDIApp);
    (*env)->SetIntField(env,lpObject,lpOleinplaceframeinfoFc->hwndFrame, (jint)lpOleinplaceframeinfo->hwndFrame);
    (*env)->SetIntField(env,lpObject,lpOleinplaceframeinfoFc->haccel, (jint)lpOleinplaceframeinfo->haccel);
    (*env)->SetIntField(env,lpObject,lpOleinplaceframeinfoFc->cAccelEntries, lpOleinplaceframeinfo->cAccelEntries);
}

void getOleverbFields(JNIEnv *env, jobject lpObject, OLEVERB *lpOleverb, OLEVERB_FID_CACHE *lpOleverbFc)
{
    lpOleverb->lVerb = (*env)->GetIntField(env,lpObject,lpOleverbFc->lVerb);
    lpOleverb->lpszVerbName = (LPWSTR)(*env)->GetIntField(env,lpObject,lpOleverbFc->lpszVerbName);
    lpOleverb->fuFlags = (*env)->GetIntField(env,lpObject,lpOleverbFc->fuFlags);
    lpOleverb->grfAttribs = (*env)->GetIntField(env,lpObject,lpOleverbFc->grfAttribs);
}

void setOleverbFields(JNIEnv *env, jobject lpObject, OLEVERB *lpOleverb, OLEVERB_FID_CACHE *lpOleverbFc)
{
    (*env)->SetIntField(env,lpObject,lpOleverbFc->lVerb, lpOleverb->lVerb);
    (*env)->SetIntField(env,lpObject,lpOleverbFc->lpszVerbName, (jint)lpOleverb->lpszVerbName);
    (*env)->SetIntField(env,lpObject,lpOleverbFc->fuFlags, lpOleverb->fuFlags);
    (*env)->SetIntField(env,lpObject,lpOleverbFc->grfAttribs, lpOleverb->grfAttribs);
}

void getStgmediumFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStgmedium, STGMEDIUM_FID_CACHE *lpStgmediumFc)
{
    lpStgmedium->tymed = (*env)->GetIntField(env,lpObject,lpStgmediumFc->tymed);
    lpStgmedium->hGlobal = (HGLOBAL)(*env)->GetIntField(env,lpObject,lpStgmediumFc->unionField);
    lpStgmedium->pUnkForRelease = (IUnknown *)(*env)->GetIntField(env,lpObject,lpStgmediumFc->pUnkForRelease);
}

void setStgmediumFields(JNIEnv *env, jobject lpObject, STGMEDIUM *lpStgmedium, STGMEDIUM_FID_CACHE *lpStgmediumFc)
{
    (*env)->SetIntField(env,lpObject,lpStgmediumFc->tymed, lpStgmedium->tymed);
    (*env)->SetIntField(env,lpObject,lpStgmediumFc->unionField, (jint)lpStgmedium->hGlobal);
    (*env)->SetIntField(env,lpObject,lpStgmediumFc->pUnkForRelease, (jint)lpStgmedium->pUnkForRelease);
}

void getStatstgFields(JNIEnv *env, jobject lpObject, STATSTG *lpStatstg, STATSTG_FID_CACHE *lpStatstgFc)
{
    lpStatstg->pwcsName = (LPWSTR)(*env)->GetIntField(env,lpObject,lpStatstgFc->pwcsName);
    lpStatstg->type = (*env)->GetIntField(env,lpObject,lpStatstgFc->type);
    lpStatstg->cbSize.QuadPart = (*env)->GetLongField(env,lpObject,lpStatstgFc->cbSize);
    lpStatstg->mtime.dwLowDateTime = (*env)->GetIntField(env,lpObject,lpStatstgFc->mtime_dwLowDateTime);
    lpStatstg->mtime.dwHighDateTime = (*env)->GetIntField(env,lpObject,lpStatstgFc->mtime_dwHighDateTime);
    lpStatstg->ctime.dwLowDateTime = (*env)->GetIntField(env,lpObject,lpStatstgFc->ctime_dwLowDateTime);
    lpStatstg->ctime.dwHighDateTime = (*env)->GetIntField(env,lpObject,lpStatstgFc->ctime_dwHighDateTime);
    lpStatstg->atime.dwLowDateTime = (*env)->GetIntField(env,lpObject,lpStatstgFc->atime_dwLowDateTime);
    lpStatstg->atime.dwHighDateTime = (*env)->GetIntField(env,lpObject,lpStatstgFc->atime_dwHighDateTime);
    lpStatstg->grfMode = (*env)->GetIntField(env,lpObject,lpStatstgFc->grfMode);
    lpStatstg->grfLocksSupported = (*env)->GetIntField(env,lpObject,lpStatstgFc->grfLocksSupported);
    lpStatstg->clsid.Data1 = (*env)->GetIntField(env,lpObject,lpStatstgFc->clsid_data1);
    lpStatstg->clsid.Data2 = (*env)->GetShortField(env,lpObject,lpStatstgFc->clsid_data2);
    lpStatstg->clsid.Data3 = (*env)->GetShortField(env,lpObject,lpStatstgFc->clsid_data3);
    lpStatstg->clsid.Data4[0] = (*env)->GetByteField(env,lpObject,lpStatstgFc->clsid_b0);
    lpStatstg->clsid.Data4[1] = (*env)->GetByteField(env,lpObject,lpStatstgFc->clsid_b1);
    lpStatstg->clsid.Data4[2] = (*env)->GetByteField(env,lpObject,lpStatstgFc->clsid_b2);
    lpStatstg->clsid.Data4[3] = (*env)->GetByteField(env,lpObject,lpStatstgFc->clsid_b3);
    lpStatstg->clsid.Data4[4] = (*env)->GetByteField(env,lpObject,lpStatstgFc->clsid_b4);
    lpStatstg->clsid.Data4[5] = (*env)->GetByteField(env,lpObject,lpStatstgFc->clsid_b5);
    lpStatstg->clsid.Data4[6] = (*env)->GetByteField(env,lpObject,lpStatstgFc->clsid_b6);
    lpStatstg->clsid.Data4[7] = (*env)->GetByteField(env,lpObject,lpStatstgFc->clsid_b7);
    lpStatstg->grfStateBits = (*env)->GetIntField(env,lpObject,lpStatstgFc->grfStateBits);
    lpStatstg->reserved = (*env)->GetIntField(env,lpObject,lpStatstgFc->reserved);
}

void setStatstgFields(JNIEnv *env, jobject lpObject, STATSTG *lpStatstg, STATSTG_FID_CACHE *lpStatstgFc)
{
    (*env)->SetIntField(env,lpObject,lpStatstgFc->pwcsName, (jint)lpStatstg->pwcsName);
    (*env)->SetIntField(env,lpObject,lpStatstgFc->type, lpStatstg->type);
    (*env)->SetLongField(env,lpObject,lpStatstgFc->cbSize, lpStatstg->cbSize.QuadPart);
    (*env)->SetIntField(env,lpObject,lpStatstgFc->mtime_dwLowDateTime, lpStatstg->mtime.dwLowDateTime);
    (*env)->SetIntField(env,lpObject,lpStatstgFc->mtime_dwHighDateTime, lpStatstg->mtime.dwHighDateTime);
    (*env)->SetIntField(env,lpObject,lpStatstgFc->ctime_dwLowDateTime, lpStatstg->ctime.dwLowDateTime);
    (*env)->SetIntField(env,lpObject,lpStatstgFc->ctime_dwHighDateTime, lpStatstg->ctime.dwHighDateTime);
    (*env)->SetIntField(env,lpObject,lpStatstgFc->atime_dwLowDateTime, lpStatstg->atime.dwLowDateTime);
    (*env)->SetIntField(env,lpObject,lpStatstgFc->atime_dwHighDateTime, lpStatstg->atime.dwHighDateTime);
    (*env)->SetIntField(env,lpObject,lpStatstgFc->grfMode, lpStatstg->grfMode);
    (*env)->SetIntField(env,lpObject,lpStatstgFc->grfLocksSupported, lpStatstg->grfLocksSupported);
    (*env)->SetIntField(env,lpObject,lpStatstgFc->clsid_data1, lpStatstg->clsid.Data1);
    (*env)->SetShortField(env,lpObject,lpStatstgFc->clsid_data2, lpStatstg->clsid.Data2);
    (*env)->SetShortField(env,lpObject,lpStatstgFc->clsid_data3, lpStatstg->clsid.Data3);
    (*env)->SetByteField(env,lpObject,lpStatstgFc->clsid_b0, lpStatstg->clsid.Data4[0]);
    (*env)->SetByteField(env,lpObject,lpStatstgFc->clsid_b1, lpStatstg->clsid.Data4[1]);
    (*env)->SetByteField(env,lpObject,lpStatstgFc->clsid_b2, lpStatstg->clsid.Data4[2]);
    (*env)->SetByteField(env,lpObject,lpStatstgFc->clsid_b3, lpStatstg->clsid.Data4[3]);
    (*env)->SetByteField(env,lpObject,lpStatstgFc->clsid_b4, lpStatstg->clsid.Data4[4]);
    (*env)->SetByteField(env,lpObject,lpStatstgFc->clsid_b5, lpStatstg->clsid.Data4[5]);
    (*env)->SetByteField(env,lpObject,lpStatstgFc->clsid_b6, lpStatstg->clsid.Data4[6]);
    (*env)->SetByteField(env,lpObject,lpStatstgFc->clsid_b7, lpStatstg->clsid.Data4[7]);
    (*env)->SetIntField(env,lpObject,lpStatstgFc->grfStateBits, lpStatstg->grfStateBits);
    (*env)->SetIntField(env,lpObject,lpStatstgFc->reserved, lpStatstg->reserved);
}

void getTypeattrFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpTypeattr, TYPEATTR_FID_CACHE *lpTypeattrFc)
{
    lpTypeattr->guid.Data1 = (*env)->GetIntField(env,lpObject,lpTypeattrFc->guid_data1);
    lpTypeattr->guid.Data2 = (*env)->GetShortField(env,lpObject,lpTypeattrFc->guid_data2);
    lpTypeattr->guid.Data3 = (*env)->GetShortField(env,lpObject,lpTypeattrFc->guid_data3);
    lpTypeattr->guid.Data4[0] = (*env)->GetByteField(env,lpObject,lpTypeattrFc->guid_b0);
    lpTypeattr->guid.Data4[1] = (*env)->GetByteField(env,lpObject,lpTypeattrFc->guid_b1);
    lpTypeattr->guid.Data4[2] = (*env)->GetByteField(env,lpObject,lpTypeattrFc->guid_b2);
    lpTypeattr->guid.Data4[3] = (*env)->GetByteField(env,lpObject,lpTypeattrFc->guid_b3);
    lpTypeattr->guid.Data4[4] = (*env)->GetByteField(env,lpObject,lpTypeattrFc->guid_b4);
    lpTypeattr->guid.Data4[5] = (*env)->GetByteField(env,lpObject,lpTypeattrFc->guid_b5);
    lpTypeattr->guid.Data4[6] = (*env)->GetByteField(env,lpObject,lpTypeattrFc->guid_b6);
    lpTypeattr->guid.Data4[7] = (*env)->GetByteField(env,lpObject,lpTypeattrFc->guid_b7);
    lpTypeattr->lcid = (*env)->GetIntField(env,lpObject,lpTypeattrFc->lcid);
    lpTypeattr->dwReserved = (*env)->GetIntField(env,lpObject,lpTypeattrFc->dwReserved);
    lpTypeattr->memidConstructor = (*env)->GetIntField(env,lpObject,lpTypeattrFc->memidConstructor);
    lpTypeattr->memidDestructor = (*env)->GetIntField(env,lpObject,lpTypeattrFc->memidDestructor);
    lpTypeattr->lpstrSchema = (OLECHAR FAR* )(*env)->GetIntField(env,lpObject,lpTypeattrFc->lpstrSchema);
    lpTypeattr->cbSizeInstance = (*env)->GetIntField(env,lpObject,lpTypeattrFc->cbSizeInstance);
    lpTypeattr->typekind = (*env)->GetIntField(env,lpObject,lpTypeattrFc->typekind);
    lpTypeattr->cFuncs = (*env)->GetShortField(env,lpObject,lpTypeattrFc->cFuncs);
    lpTypeattr->cVars = (*env)->GetShortField(env,lpObject,lpTypeattrFc->cVars);
    lpTypeattr->cImplTypes = (*env)->GetShortField(env,lpObject,lpTypeattrFc->cImplTypes);
    lpTypeattr->cbSizeVft = (*env)->GetShortField(env,lpObject,lpTypeattrFc->cbSizeVft);
    lpTypeattr->cbAlignment = (*env)->GetShortField(env,lpObject,lpTypeattrFc->cbAlignment);
    lpTypeattr->wTypeFlags = (*env)->GetShortField(env,lpObject,lpTypeattrFc->wTypeFlags);
    lpTypeattr->wMajorVerNum = (*env)->GetShortField(env,lpObject,lpTypeattrFc->wMajorVerNum);
    lpTypeattr->wMinorVerNum = (*env)->GetShortField(env,lpObject,lpTypeattrFc->wMinorVerNum);
    lpTypeattr->tdescAlias.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR* )(*env)->GetIntField(env,lpObject,lpTypeattrFc->tdescAlias_unionField);
    lpTypeattr->tdescAlias.vt = (*env)->GetShortField(env,lpObject,lpTypeattrFc->tdescAlias_vt);
    lpTypeattr->idldescType.dwReserved = (*env)->GetIntField(env,lpObject,lpTypeattrFc->idldescType_dwReserved);
    lpTypeattr->idldescType.wIDLFlags = (*env)->GetShortField(env,lpObject,lpTypeattrFc->idldescType_wIDLFlags);
}

void setTypeattrFields(JNIEnv *env, jobject lpObject, TYPEATTR *lpTypeattr, TYPEATTR_FID_CACHE *lpTypeattrFc)
{
    (*env)->SetIntField(env,lpObject,lpTypeattrFc->guid_data1, lpTypeattr->guid.Data1);
    (*env)->SetShortField(env,lpObject,lpTypeattrFc->guid_data2, lpTypeattr->guid.Data2);
    (*env)->SetShortField(env,lpObject,lpTypeattrFc->guid_data3, lpTypeattr->guid.Data3);
    (*env)->SetByteField(env,lpObject,lpTypeattrFc->guid_b0, lpTypeattr->guid.Data4[0]);
    (*env)->SetByteField(env,lpObject,lpTypeattrFc->guid_b1, lpTypeattr->guid.Data4[1]);
    (*env)->SetByteField(env,lpObject,lpTypeattrFc->guid_b2, lpTypeattr->guid.Data4[2]);
    (*env)->SetByteField(env,lpObject,lpTypeattrFc->guid_b3, lpTypeattr->guid.Data4[3]);
    (*env)->SetByteField(env,lpObject,lpTypeattrFc->guid_b4, lpTypeattr->guid.Data4[4]);
    (*env)->SetByteField(env,lpObject,lpTypeattrFc->guid_b5, lpTypeattr->guid.Data4[5]);
    (*env)->SetByteField(env,lpObject,lpTypeattrFc->guid_b6, lpTypeattr->guid.Data4[6]);
    (*env)->SetByteField(env,lpObject,lpTypeattrFc->guid_b7, lpTypeattr->guid.Data4[7]);
    (*env)->SetIntField(env,lpObject,lpTypeattrFc->lcid, lpTypeattr->lcid);
    (*env)->SetIntField(env,lpObject,lpTypeattrFc->dwReserved, lpTypeattr->dwReserved);
    (*env)->SetIntField(env,lpObject,lpTypeattrFc->memidConstructor, lpTypeattr->memidConstructor);
    (*env)->SetIntField(env,lpObject,lpTypeattrFc->memidDestructor, lpTypeattr->memidDestructor);
    (*env)->SetIntField(env,lpObject,lpTypeattrFc->lpstrSchema, (jint)lpTypeattr->lpstrSchema);
    (*env)->SetIntField(env,lpObject,lpTypeattrFc->cbSizeInstance, lpTypeattr->cbSizeInstance);
    (*env)->SetIntField(env,lpObject,lpTypeattrFc->typekind, lpTypeattr->typekind);
    (*env)->SetShortField(env,lpObject,lpTypeattrFc->cFuncs, lpTypeattr->cFuncs);
    (*env)->SetShortField(env,lpObject,lpTypeattrFc->cVars, lpTypeattr->cVars);
    (*env)->SetShortField(env,lpObject,lpTypeattrFc->cImplTypes, lpTypeattr->cImplTypes);
    (*env)->SetShortField(env,lpObject,lpTypeattrFc->cbSizeVft, lpTypeattr->cbSizeVft);
    (*env)->SetShortField(env,lpObject,lpTypeattrFc->cbAlignment, lpTypeattr->cbAlignment);
    (*env)->SetShortField(env,lpObject,lpTypeattrFc->wTypeFlags, lpTypeattr->wTypeFlags);
    (*env)->SetShortField(env,lpObject,lpTypeattrFc->wMajorVerNum, lpTypeattr->wMajorVerNum);
    (*env)->SetShortField(env,lpObject,lpTypeattrFc->wMinorVerNum, lpTypeattr->wMinorVerNum);

    (*env)->SetIntField(env,lpObject,lpTypeattrFc->tdescAlias_unionField, (jint)lpTypeattr->tdescAlias.lptdesc);
    (*env)->SetShortField(env,lpObject,lpTypeattrFc->tdescAlias_vt, lpTypeattr->tdescAlias.vt);

    (*env)->SetIntField(env,lpObject,lpTypeattrFc->idldescType_dwReserved, lpTypeattr->idldescType.dwReserved);
    (*env)->SetShortField(env,lpObject,lpTypeattrFc->idldescType_wIDLFlags, lpTypeattr->idldescType.wIDLFlags);
}

void getTypedescFields(JNIEnv *env, jobject lpObject, TYPEDESC *lpTypedesc, TYPEDESC_FID_CACHE *lpTypedescFc)
{
    lpTypedesc->lptdesc = (struct FARSTRUCT tagTYPEDESC FAR* )(*env)->GetIntField(env,lpObject,lpTypedescFc->typedesc_union);
    lpTypedesc->vt = (*env)->GetShortField(env,lpObject,lpTypedescFc->vt);
}

void setTypedescFields(JNIEnv *env, jobject lpObject, TYPEDESC *lpTypedesc, TYPEDESC_FID_CACHE *lpTypedescFc)
{
    (*env)->SetIntField(env,lpObject,lpTypedescFc->typedesc_union, (jint)lpTypedesc->lptdesc);
    (*env)->SetShortField(env,lpObject,lpTypedescFc->vt, lpTypedesc->vt);
}

void getFuncdesc1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpFuncdesc, FUNCDESC1_FID_CACHE *lpFuncdescFc)
{
    lpFuncdesc->memid= (*env)->GetIntField(env,lpObject,lpFuncdescFc->memid);
    lpFuncdesc->lprgscode = (SCODE FAR *)(*env)->GetIntField(env,lpObject,lpFuncdescFc->lprgscode);
    lpFuncdesc->lprgelemdescParam = (ELEMDESC FAR *)(*env)->GetIntField(env,lpObject,lpFuncdescFc->lprgelemdescParam);
    lpFuncdesc->funckind = (*env)->GetIntField(env,lpObject,lpFuncdescFc->funckind);
    lpFuncdesc->invkind = (*env)->GetIntField(env,lpObject,lpFuncdescFc->invkind);
    lpFuncdesc->callconv = (*env)->GetIntField(env,lpObject,lpFuncdescFc->callconv);
    lpFuncdesc->cParams = (*env)->GetShortField(env,lpObject,lpFuncdescFc->cParams);
    lpFuncdesc->cParamsOpt = (*env)->GetShortField(env,lpObject,lpFuncdescFc->cParamsOpt);
    lpFuncdesc->oVft = (*env)->GetShortField(env,lpObject,lpFuncdescFc->oVft);
    lpFuncdesc->cScodes = (*env)->GetShortField(env,lpObject,lpFuncdescFc->cScodes);
    lpFuncdesc->elemdescFunc.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR* )(*env)->GetIntField(env,lpObject,lpFuncdescFc->elemdescFunc_tdesc_union);
    lpFuncdesc->elemdescFunc.tdesc.vt = (*env)->GetShortField(env,lpObject,lpFuncdescFc->elemdescFunc_tdesc_vt);
    lpFuncdesc->elemdescFunc.paramdesc.pparamdescex = (LPPARAMDESCEX)(*env)->GetIntField(env,lpObject,lpFuncdescFc->elemdescFunc_paramdesc_pparamdescex);
    lpFuncdesc->elemdescFunc.paramdesc.wParamFlags = (*env)->GetShortField(env,lpObject,lpFuncdescFc->elemdescFunc_paramdesc_wParamFlags);  
    lpFuncdesc->wFuncFlags = (*env)->GetShortField(env,lpObject,lpFuncdescFc->wFuncFlags);    
}

void setFuncdesc1Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpFuncdesc, FUNCDESC1_FID_CACHE *lpFuncdescFc)
{
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->memid, (jint)lpFuncdesc->memid);
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->lprgscode, (jint)lpFuncdesc->lprgscode);
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->lprgelemdescParam, (jint)lpFuncdesc->lprgelemdescParam);
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->funckind, (jint)lpFuncdesc->funckind);
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->invkind, (jint)lpFuncdesc->invkind);
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->callconv, (jint)lpFuncdesc->callconv);
    (*env)->SetShortField(env,lpObject,lpFuncdescFc->cParams, (jshort)lpFuncdesc->cParams);
    (*env)->SetShortField(env,lpObject,lpFuncdescFc->cParamsOpt, (jshort)lpFuncdesc->cParamsOpt);
    (*env)->SetShortField(env,lpObject,lpFuncdescFc->oVft, (jshort)lpFuncdesc->oVft);
    (*env)->SetShortField(env,lpObject,lpFuncdescFc->cScodes, (jshort)lpFuncdesc->cScodes);
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->elemdescFunc_tdesc_union, (jint)lpFuncdesc->elemdescFunc.tdesc.lptdesc);
    (*env)->SetShortField(env,lpObject,lpFuncdescFc->elemdescFunc_tdesc_vt, (jshort)lpFuncdesc->elemdescFunc.tdesc.vt);
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->elemdescFunc_paramdesc_pparamdescex, (jint)lpFuncdesc->elemdescFunc.paramdesc.pparamdescex);
    (*env)->SetShortField(env,lpObject,lpFuncdescFc->elemdescFunc_paramdesc_wParamFlags, (jshort)lpFuncdesc->elemdescFunc.paramdesc.wParamFlags);
    (*env)->SetShortField(env,lpObject,lpFuncdescFc->wFuncFlags, (jshort)lpFuncdesc->wFuncFlags);
}
void getFuncdesc2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpFuncdesc, FUNCDESC2_FID_CACHE *lpFuncdescFc)
{
    lpFuncdesc->memid= (*env)->GetIntField(env,lpObject,lpFuncdescFc->memid);
    lpFuncdesc->lprgscode = (SCODE FAR *)(*env)->GetIntField(env,lpObject,lpFuncdescFc->lprgscode);
    lpFuncdesc->lprgelemdescParam = (ELEMDESC FAR *)(*env)->GetIntField(env,lpObject,lpFuncdescFc->lprgelemdescParam);
    lpFuncdesc->funckind = (*env)->GetIntField(env,lpObject,lpFuncdescFc->funckind);
    lpFuncdesc->invkind = (*env)->GetIntField(env,lpObject,lpFuncdescFc->invkind);
    lpFuncdesc->callconv = (*env)->GetIntField(env,lpObject,lpFuncdescFc->callconv);
    lpFuncdesc->cParams = (*env)->GetShortField(env,lpObject,lpFuncdescFc->cParams);
    lpFuncdesc->cParamsOpt = (*env)->GetShortField(env,lpObject,lpFuncdescFc->cParamsOpt);
    lpFuncdesc->oVft = (*env)->GetShortField(env,lpObject,lpFuncdescFc->oVft);
    lpFuncdesc->cScodes = (*env)->GetShortField(env,lpObject,lpFuncdescFc->cScodes);
    lpFuncdesc->elemdescFunc.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR* )(*env)->GetIntField(env,lpObject,lpFuncdescFc->elemdescFunc_tdesc_union);
    lpFuncdesc->elemdescFunc.tdesc.vt = (*env)->GetShortField(env,lpObject,lpFuncdescFc->elemdescFunc_tdesc_vt);
    lpFuncdesc->elemdescFunc.idldesc.dwReserved = (*env)->GetIntField(env,lpObject,lpFuncdescFc->elemdescFunc_idldesc_dwReserved);
    lpFuncdesc->elemdescFunc.idldesc.wIDLFlags = (*env)->GetShortField(env,lpObject,lpFuncdescFc->elemdescFunc_idldesc_wIDLFlags);  
    lpFuncdesc->wFuncFlags = (*env)->GetShortField(env,lpObject,lpFuncdescFc->wFuncFlags);    
}

void setFuncdesc2Fields(JNIEnv *env, jobject lpObject, FUNCDESC *lpFuncdesc, FUNCDESC2_FID_CACHE *lpFuncdescFc)
{
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->memid, (jint)lpFuncdesc->memid);
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->lprgscode, (jint)lpFuncdesc->lprgscode);
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->lprgelemdescParam, (jint)lpFuncdesc->lprgelemdescParam);
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->funckind, (jint)lpFuncdesc->funckind);
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->invkind, (jint)lpFuncdesc->invkind);
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->callconv, (jint)lpFuncdesc->callconv);
    (*env)->SetShortField(env,lpObject,lpFuncdescFc->cParams, (jshort)lpFuncdesc->cParams);
    (*env)->SetShortField(env,lpObject,lpFuncdescFc->cParamsOpt, (jshort)lpFuncdesc->cParamsOpt);
    (*env)->SetShortField(env,lpObject,lpFuncdescFc->oVft, (jshort)lpFuncdesc->oVft);
    (*env)->SetShortField(env,lpObject,lpFuncdescFc->cScodes, (jshort)lpFuncdesc->cScodes);
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->elemdescFunc_tdesc_union, (jint)lpFuncdesc->elemdescFunc.tdesc.lptdesc);
    (*env)->SetShortField(env,lpObject,lpFuncdescFc->elemdescFunc_tdesc_vt, (jshort)lpFuncdesc->elemdescFunc.tdesc.vt);
    (*env)->SetIntField(env,lpObject,lpFuncdescFc->elemdescFunc_idldesc_dwReserved, (jint)lpFuncdesc->elemdescFunc.idldesc.dwReserved);
    (*env)->SetShortField(env,lpObject,lpFuncdescFc->elemdescFunc_idldesc_wIDLFlags, (jshort)lpFuncdesc->elemdescFunc.idldesc.wIDLFlags);
    (*env)->SetShortField(env,lpObject,lpFuncdescFc->wFuncFlags, (jshort)lpFuncdesc->wFuncFlags);
}
void getVardesc1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpVardesc, VARDESC1_FID_CACHE *lpVardescFc)
{
    lpVardesc->memid = (*env)->GetIntField(env,lpObject,lpVardescFc->memid);
    lpVardesc->lpstrSchema = (OLECHAR FAR* )(*env)->GetIntField(env,lpObject,lpVardescFc->lpstrSchema);
    lpVardesc->oInst = (*env)->GetIntField(env,lpObject,lpVardescFc->unionField);
    lpVardesc->elemdescVar.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR* )(*env)->GetIntField(env,lpObject,lpVardescFc->elemdescVar_tdesc_union);
    lpVardesc->elemdescVar.tdesc.vt = (*env)->GetShortField(env,lpObject,lpVardescFc->elemdescVar_tdesc_vt);
    lpVardesc->elemdescVar.paramdesc.pparamdescex= (LPPARAMDESCEX)(*env)->GetIntField(env,lpObject,lpVardescFc->elemdescVar_paramdesc_pparamdescex);
    lpVardesc->elemdescVar.paramdesc.wParamFlags = (*env)->GetShortField(env,lpObject,lpVardescFc->elemdescVar_paramdesc_wParamFlags);
    lpVardesc->wVarFlags = (*env)->GetShortField(env,lpObject,lpVardescFc->wVarFlags);
    lpVardesc->varkind = (*env)->GetIntField(env,lpObject,lpVardescFc->varkind);
}

void setVardesc1Fields(JNIEnv *env, jobject lpObject, VARDESC *lpVardesc, VARDESC1_FID_CACHE *lpVardescFc)
{
    (*env)->SetIntField(env,lpObject,lpVardescFc->memid, lpVardesc->memid);
    (*env)->SetIntField(env,lpObject,lpVardescFc->lpstrSchema, (jint)lpVardesc->lpstrSchema);
    (*env)->SetIntField(env,lpObject,lpVardescFc->unionField, lpVardesc->oInst);
    (*env)->SetIntField(env,lpObject,lpVardescFc->elemdescVar_tdesc_union, (jint)lpVardesc->elemdescVar.tdesc.lptdesc);
    (*env)->SetShortField(env,lpObject,lpVardescFc->elemdescVar_tdesc_vt, (jshort)lpVardesc->elemdescVar.tdesc.vt);
    (*env)->SetIntField(env,lpObject,lpVardescFc->elemdescVar_paramdesc_pparamdescex, (jint)lpVardesc->elemdescVar.paramdesc.pparamdescex);
    (*env)->SetShortField(env,lpObject,lpVardescFc->elemdescVar_paramdesc_wParamFlags, (jshort)lpVardesc->elemdescVar.paramdesc.wParamFlags);
    (*env)->SetShortField(env,lpObject,lpVardescFc->wVarFlags, (jshort)lpVardesc->wVarFlags);
    (*env)->SetIntField(env,lpObject,lpVardescFc->varkind, (jint)lpVardesc->varkind);
}
void getVardesc2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpVardesc, VARDESC2_FID_CACHE *lpVardescFc)
{
    lpVardesc->memid = (*env)->GetIntField(env,lpObject,lpVardescFc->memid);
    lpVardesc->lpstrSchema = (OLECHAR FAR* )(*env)->GetIntField(env,lpObject,lpVardescFc->lpstrSchema);
    lpVardesc->oInst = (*env)->GetIntField(env,lpObject,lpVardescFc->unionField);
    lpVardesc->elemdescVar.tdesc.lptdesc = (struct FARSTRUCT tagTYPEDESC FAR* )(*env)->GetIntField(env,lpObject,lpVardescFc->elemdescVar_tdesc_union);
    lpVardesc->elemdescVar.tdesc.vt = (*env)->GetShortField(env,lpObject,lpVardescFc->elemdescVar_tdesc_vt);
    lpVardesc->elemdescVar.idldesc.dwReserved = (*env)->GetIntField(env,lpObject,lpVardescFc->elemdescVar_idldesc_dwReserved);
    lpVardesc->elemdescVar.idldesc.wIDLFlags = (*env)->GetShortField(env,lpObject,lpVardescFc->elemdescVar_idldesc_wIDLFlags);
    lpVardesc->wVarFlags = (*env)->GetShortField(env,lpObject,lpVardescFc->wVarFlags);
    lpVardesc->varkind = (*env)->GetIntField(env,lpObject,lpVardescFc->varkind);
}

void setVardesc2Fields(JNIEnv *env, jobject lpObject, VARDESC *lpVardesc, VARDESC2_FID_CACHE *lpVardescFc)
{
    (*env)->SetIntField(env,lpObject,lpVardescFc->memid, lpVardesc->memid);
    (*env)->SetIntField(env,lpObject,lpVardescFc->lpstrSchema, (jint)lpVardesc->lpstrSchema);
    (*env)->SetIntField(env,lpObject,lpVardescFc->unionField, (jint)lpVardesc->oInst);
    (*env)->SetIntField(env,lpObject,lpVardescFc->elemdescVar_tdesc_union, (jint)lpVardesc->elemdescVar.tdesc.lptdesc);
    (*env)->SetShortField(env,lpObject,lpVardescFc->elemdescVar_tdesc_vt, (jshort)lpVardesc->elemdescVar.tdesc.vt);
    (*env)->SetIntField(env,lpObject,lpVardescFc->elemdescVar_idldesc_dwReserved, (jint)lpVardesc->elemdescVar.idldesc.dwReserved);
    (*env)->SetShortField(env,lpObject,lpVardescFc->elemdescVar_idldesc_wIDLFlags, (jshort)lpVardesc->elemdescVar.idldesc.wIDLFlags);
    (*env)->SetShortField(env,lpObject,lpVardescFc->wVarFlags, (jshort)lpVardesc->wVarFlags);
    (*env)->SetIntField(env,lpObject,lpVardescFc->varkind, (jint)lpVardesc->varkind);    
}

void cacheOsversioninfoFids(JNIEnv *env, jobject lpOsversioninfo, POSVERSIONINFO_FID_CACHE lpCache)
{
    if (lpCache->cached) return;
    lpCache->osversioninfoClass = (*env)->GetObjectClass(env,lpOsversioninfo);
    lpCache->dwOSVersionInfoSize = (*env)->GetFieldID(env,lpCache->osversioninfoClass,"dwOSVersionInfoSize","I");
    lpCache->dwMajorVersion = (*env)->GetFieldID(env,lpCache->osversioninfoClass,"dwMajorVersion","I");
    lpCache->dwMinorVersion = (*env)->GetFieldID(env,lpCache->osversioninfoClass,"dwMinorVersion","I");
    lpCache->dwBuildNumber = (*env)->GetFieldID(env,lpCache->osversioninfoClass,"dwBuildNumber","I");
    lpCache->dwPlatformId = (*env)->GetFieldID(env,lpCache->osversioninfoClass,"dwPlatformId","I");
//	lpCache->szCSDVersion = (*env)->GetFieldID(env,lpCache->osversioninfoClass,"szCSDVersion","[C");
    lpCache->cached = 1;
}

void getOsversioninfoFields(JNIEnv *env, jobject lpObject, OSVERSIONINFO *lpOsversioninfo, OSVERSIONINFO_FID_CACHE *lpOsversioninfoFc)
{
    lpOsversioninfo->dwOSVersionInfoSize = (*env)->GetIntField(env,lpObject,lpOsversioninfoFc->dwOSVersionInfoSize);
    lpOsversioninfo->dwMajorVersion = (*env)->GetIntField(env,lpObject,lpOsversioninfoFc->dwMajorVersion);
    lpOsversioninfo->dwMinorVersion = (*env)->GetIntField(env,lpObject,lpOsversioninfoFc->dwMinorVersion);
    lpOsversioninfo->dwBuildNumber = (*env)->GetIntField(env,lpObject,lpOsversioninfoFc->dwBuildNumber);
    lpOsversioninfo->dwPlatformId = (*env)->GetIntField(env,lpObject,lpOsversioninfoFc->dwPlatformId);
/*
	{
	jcharArray szCSDVersion = (*env)->GetObjectField(env, lpObject, lpOsversioninfoFc->szCSDVersion);
    if (szCSDVersion) {
        jchar *szCSDVersion1 = (*env)->GetCharArrayElements(env, szCSDVersion, NULL);
        memcpy(lpOsversioninfo->szCSDVersion, szCSDVersion1, 128 * ??);
        (*env)->ReleaseCharArrayElements(env, szCSDVersion, szCSDVersion1, JNI_ABORT);
	}
	}
*/
//	lpOsversioninfo->szCSDVersion = (*env)->GetIntField(env,lpObject,lpOsversioninfoFc->szCSDVersion);
}

void setOsversioninfoFields(JNIEnv *env, jobject lpObject, OSVERSIONINFO *lpOsversioninfo, OSVERSIONINFO_FID_CACHE *lpOsversioninfoFc)
{
    (*env)->SetIntField(env,lpObject,lpOsversioninfoFc->dwOSVersionInfoSize, lpOsversioninfo->dwOSVersionInfoSize);
    (*env)->SetIntField(env,lpObject,lpOsversioninfoFc->dwMajorVersion, lpOsversioninfo->dwMajorVersion);
    (*env)->SetIntField(env,lpObject,lpOsversioninfoFc->dwMinorVersion, lpOsversioninfo->dwMinorVersion);
    (*env)->SetIntField(env,lpObject,lpOsversioninfoFc->dwBuildNumber, lpOsversioninfo->dwBuildNumber);
    (*env)->SetIntField(env,lpObject,lpOsversioninfoFc->dwPlatformId, lpOsversioninfo->dwPlatformId); 
/*
	{
	jcharArray szCSDVersion = (*env)->GetObjectField(env, lpObject, lpOsversioninfoFc->szCSDVersion);
    if (szCSDVersion) {
        jchar *szCSDVersion1 = (*env)->GetCharArrayElements(env, szCSDVersion, NULL);
        memcpy(szCSDVersion1, lpOsversioninfo->szCSDVersion, 128 * ??);
        (*env)->ReleaseCharArrayElements(env, szCSDVersion, szCSDVersion1, JNI_ABORT);
	}
	}
*/
//	(*env)->SetIntField(env,lpObject,lpOsversioninfoFc->szCSDVersion, lpOsversioninfo->szCSDVersion);
}

void cacheShellexecuteinfoFids(JNIEnv *env, jobject lpObject, PSHELLEXECUTEINFO_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->hProcess = (*env)->GetFieldID(env, lpCache->clazz, "hProcess", "I");
	lpCache->hIcon = (*env)->GetFieldID(env, lpCache->clazz, "hIcon", "I");
	lpCache->dwHotKey = (*env)->GetFieldID(env, lpCache->clazz, "dwHotKey", "I");
	lpCache->hkeyClass = (*env)->GetFieldID(env, lpCache->clazz, "hkeyClass", "I");
	lpCache->lpClass = (*env)->GetFieldID(env, lpCache->clazz, "lpClass", "I");
	lpCache->lpIDList = (*env)->GetFieldID(env, lpCache->clazz, "lpIDList", "I");
	lpCache->hInstApp = (*env)->GetFieldID(env, lpCache->clazz, "hInstApp", "I");
	lpCache->nShow = (*env)->GetFieldID(env, lpCache->clazz, "nShow", "I");
	lpCache->lpDirectory = (*env)->GetFieldID(env, lpCache->clazz, "lpDirectory", "I");
	lpCache->lpParameters = (*env)->GetFieldID(env, lpCache->clazz, "lpParameters", "I");
	lpCache->lpFile = (*env)->GetFieldID(env, lpCache->clazz, "lpFile", "I");
	lpCache->lpVerb = (*env)->GetFieldID(env, lpCache->clazz, "lpVerb", "I");
	lpCache->hwnd = (*env)->GetFieldID(env, lpCache->clazz, "hwnd", "I");
	lpCache->fMask = (*env)->GetFieldID(env, lpCache->clazz, "fMask", "I");
	lpCache->cbSize = (*env)->GetFieldID(env, lpCache->clazz, "cbSize", "I");
	lpCache->cached = 1;
}

void getShellexecuteinfoFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct, PSHELLEXECUTEINFO_FID_CACHE lpCache)
{
	lpStruct->hProcess = (*env)->GetIntField(env, lpObject, lpCache->hProcess);
	lpStruct->hIcon = (*env)->GetIntField(env, lpObject, lpCache->hIcon);
	lpStruct->dwHotKey = (*env)->GetIntField(env, lpObject, lpCache->dwHotKey);
	lpStruct->hkeyClass = (*env)->GetIntField(env, lpObject, lpCache->hkeyClass);
	lpStruct->lpClass = (*env)->GetIntField(env, lpObject, lpCache->lpClass);
	lpStruct->lpIDList = (*env)->GetIntField(env, lpObject, lpCache->lpIDList);
	lpStruct->hInstApp = (*env)->GetIntField(env, lpObject, lpCache->hInstApp);
	lpStruct->nShow = (*env)->GetIntField(env, lpObject, lpCache->nShow);
	lpStruct->lpDirectory = (*env)->GetIntField(env, lpObject, lpCache->lpDirectory);
	lpStruct->lpParameters = (*env)->GetIntField(env, lpObject, lpCache->lpParameters);
	lpStruct->lpFile = (*env)->GetIntField(env, lpObject, lpCache->lpFile);
	lpStruct->lpVerb = (*env)->GetIntField(env, lpObject, lpCache->lpVerb);
	lpStruct->hwnd = (*env)->GetIntField(env, lpObject, lpCache->hwnd);
	lpStruct->fMask = (*env)->GetIntField(env, lpObject, lpCache->fMask);
	lpStruct->cbSize = (*env)->GetIntField(env, lpObject, lpCache->cbSize);
}

void setShellexecuteinfoFields(JNIEnv *env, jobject lpObject, SHELLEXECUTEINFO *lpStruct, PSHELLEXECUTEINFO_FID_CACHE lpCache)
{
	(*env)->SetIntField(env, lpObject, lpCache->hProcess, lpStruct->hProcess);
	(*env)->SetIntField(env, lpObject, lpCache->hIcon, lpStruct->hIcon);
	(*env)->SetIntField(env, lpObject, lpCache->dwHotKey, lpStruct->dwHotKey);
	(*env)->SetIntField(env, lpObject, lpCache->hkeyClass, lpStruct->hkeyClass);
	(*env)->SetIntField(env, lpObject, lpCache->lpClass, lpStruct->lpClass);
	(*env)->SetIntField(env, lpObject, lpCache->lpIDList, lpStruct->lpIDList);
	(*env)->SetIntField(env, lpObject, lpCache->hInstApp, lpStruct->hInstApp);
	(*env)->SetIntField(env, lpObject, lpCache->nShow, lpStruct->nShow);
	(*env)->SetIntField(env, lpObject, lpCache->lpDirectory, lpStruct->lpDirectory);
	(*env)->SetIntField(env, lpObject, lpCache->lpParameters, lpStruct->lpParameters);
	(*env)->SetIntField(env, lpObject, lpCache->lpFile, lpStruct->lpFile);
	(*env)->SetIntField(env, lpObject, lpCache->lpVerb, lpStruct->lpVerb);
	(*env)->SetIntField(env, lpObject, lpCache->hwnd, lpStruct->hwnd);
	(*env)->SetIntField(env, lpObject, lpCache->fMask, lpStruct->fMask);
	(*env)->SetIntField(env, lpObject, lpCache->cbSize, lpStruct->cbSize);
}


