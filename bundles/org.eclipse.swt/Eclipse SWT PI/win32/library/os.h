/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#ifndef INC_os_H
#define INC_os_H

#include <windows.h>
#include <WindowsX.h>
#include <winuser.h>
#include <commctrl.h>
#include <commdlg.h>
#include <oaidl.h>
#include <shlobj.h>
#include <ole2.h>
#include <olectl.h>
#include <objbase.h>
#include <shlwapi.h>
#include <shellapi.h>
#include <wininet.h>
#include <mshtmhst.h>
#include <Tabflicks.h>
#include <initguid.h>
#include <oleacc.h>
/* usp10.h has warnings at warning level 4 (64 bit) */ 
#pragma warning( disable : 4214 )
#include <usp10.h>
#pragma warning( default : 4214 )
#include <uxtheme.h>
#include <vfw.h>
#include <msctf.h>

/* Optional custom definitions to exclude some types */
#include "defines.h"

#define OS_LOAD_FUNCTION LOAD_FUNCTION

/* Structs excludes */

/* Functions excludes */

#define TrackMouseEvent _TrackMouseEvent
#undef NOTIFYICONDATAW_V2_SIZE
#define NOTIFYICONDATAW_V2_SIZE     (FIELD_OFFSET(NOTIFYICONDATAW, dwInfoFlags)+sizeof(int))

#ifndef _BP_PAINTPARAMS
typedef HANDLE HPAINTBUFFER;
#endif

#ifndef PROPERTYKEY_DEFINED
#define PROPERTYKEY_DEFINED
typedef struct _tagpropertykey {
    GUID fmtid;
    DWORD pid;
} PROPERTYKEY;
#endif

#ifndef _DWM_BLURBEHIND
typedef struct _DWM_BLURBEHIND {
    DWORD dwFlags;
    BOOL fEnable;
    HRGN hRgnBlur;
    BOOL fTransitionOnMaximized;
} DWM_BLURBEHIND, *PDWM_BLURBEHIND;
#endif

#ifndef DTT_CALLBACK_PROC
typedef 
int
(WINAPI *DTT_CALLBACK_PROC)
(
    __in HDC hdc,
    __inout_ecount(cchText) LPWSTR pszText,
    __in int cchText,
    __inout LPRECT prc,
    __in UINT dwFlags,
    __in LPARAM lParam);
#endif

#if (_WIN32_IE <= 0x0600)
typedef struct tagTVITEMCHANGE {
    NMHDR hdr;
    UINT uChanged;
    HTREEITEM hItem;
    UINT uStateNew;
    UINT uStateOld;
    LPARAM lParam;
} NMTVITEMCHANGE;
#endif /* _WIN32_IE <= 0x0600 */

#if (WINVER < 0x0601)
typedef struct FLICK_POINT
{
    INT x:16;
    INT y:16;
}FLICK_POINT;

typedef struct FLICK_DATA
{
/*
	Avoid warnings in Windows 64 bit.
    FLICKACTION_COMMANDCODE iFlickActionCommandCode:5;
    FLICKDIRECTION iFlickDirection:3;
*/
    INT iFlickActionCommandCode:5;
    UINT iFlickDirection:3;
    BOOL fControlModifier:1;
    BOOL fMenuModifier:1;
    BOOL fAltGRModifier:1;
    BOOL fWinModifier:1;
    BOOL fShiftModifier:1;
    INT  iReserved:2;
    BOOL fOnInkingSurface:1;
    INT  iActionArgument:16;
}FLICK_DATA;

/*
 * Touch input handle
 */
DECLARE_HANDLE(HTOUCHINPUT);

typedef struct tagTOUCHINPUT {
    LONG x;
    LONG y;
    HANDLE hSource;
    DWORD dwID;
    DWORD dwFlags;
    DWORD dwMask;
    DWORD dwTime;
    ULONG_PTR dwExtraInfo;
    DWORD cxContact;
    DWORD cyContact;
} TOUCHINPUT, *PTOUCHINPUT;
typedef TOUCHINPUT const * PCTOUCHINPUT;

/*
 * Conversion of touch input coordinates to pixels
 */
#define TOUCH_COORD_TO_PIXEL(l)         ((l) / 100)

/*
 * Gesture information handle
 */
DECLARE_HANDLE(HGESTUREINFO);

typedef struct tagGESTUREINFO {
    UINT cbSize;                    // size, in bytes, of this structure (including variable length Args field)
    DWORD dwFlags;                  // see GF_* flags
    DWORD dwID;                     // gesture ID, see GID_* defines
    HWND hwndTarget;                // handle to window targeted by this gesture
    POINTS ptsLocation;             // current location of this gesture
    DWORD dwInstanceID;             // internally used
    DWORD dwSequenceID;             // internally used
    ULONGLONG ullArguments;         // arguments for gestures whose arguments fit in 8 BYTES
    UINT cbExtraArgs;               // size, in bytes, of extra arguments, if any, that accompany this gesture
} GESTUREINFO, *PGESTUREINFO;
typedef GESTUREINFO const * PCGESTUREINFO;

typedef struct tagGESTURENOTIFYSTRUCT {
    UINT cbSize;                    // size, in bytes, of this structure
    DWORD dwFlags;                  // unused
    HWND hwndTarget;                // handle to window targeted by the gesture
    POINTS ptsLocation;             // starting location
    DWORD dwInstanceID;             // internally used
} GESTURENOTIFYSTRUCT, *PGESTURENOTIFYSTRUCT;

/*
 * Gesture argument helpers
 *   - Angle should be a double in the range of -2pi to +2pi
 *   - Argument should be an unsigned 16-bit value
 */
#define GID_ROTATE_ANGLE_TO_ARGUMENT(_arg_)     ((USHORT)((((_arg_) + 2.0 * 3.14159265) / (4.0 * 3.14159265)) * 65535.0))
#define GID_ROTATE_ANGLE_FROM_ARGUMENT(_arg_)   ((((double)(_arg_) / 65535.0) * 4.0 * 3.14159265) - 2.0 * 3.14159265)

typedef struct tagGESTURECONFIG {
    DWORD dwID;                     // gesture ID
    DWORD dwWant;                   // settings related to gesture ID that are to be turned on
    DWORD dwBlock;                  // settings related to gesture ID that are to be turned off
} GESTURECONFIG, *PGESTURECONFIG;
#endif /* WINVER >= 0x0601 */

#ifndef GET_WHEEL_DELTA_WPARAM
#define GET_WHEEL_DELTA_WPARAM(wParam)  ((short)HIWORD(wParam))
#endif

#ifndef LODWORD
#define LODWORD(l) ((DWORD)((DWORDLONG)(l)))
#endif

#include "os_custom.h"

#if defined (_WIN64) || defined (_WIN32_WCE)
#define PRINT_CODE(buf, size, format, code) sprintf(buf, format, code);
#else
#define PRINT_CODE(buf, size, format, code) sprintf_s(buf, size, format, code);
#endif
#define NATIVE_TRY(env, that, func) \
	__try {
#define NATIVE_CATCH(env, that, func) \
	} __except(EXCEPTION_EXECUTE_HANDLER) { \
		jclass expClass = (*env)->FindClass(env, "org/eclipse/swt/SWTError");  \
		if (expClass) { \
			char buffer[256]; \
			PRINT_CODE(buffer, 256, "cought native exception: 0x%x", GetExceptionCode()) \
			(*env)->ThrowNew(env, expClass, buffer); \
		} \
	}

#define OS_NATIVE_ENTER_TRY(env, that, func) \
	OS_NATIVE_ENTER(env, that, func); \
	NATIVE_TRY(env, that, func);
#define OS_NATIVE_EXIT_CATCH(env, that, func) \
	NATIVE_CATCH(env, that, func); \
	OS_NATIVE_EXIT(env, that, func);

#endif /* INC_os_H */
