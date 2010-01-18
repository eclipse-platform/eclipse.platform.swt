/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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

#ifndef _WIN32_WCE
#include <initguid.h>
#include <oleacc.h>
/* usp10.h has warnings at warning level 4 (64 bit) */ 
#pragma warning( disable : 4214 )
#include <usp10.h>
#pragma warning( default : 4214 )
#include <uxtheme.h>
#include <vfw.h>
#include <msctf.h>
#endif // _WIN32_WCE

#if defined(WIN32_PLATFORM_PSPC) || defined(WIN32_PLATFORM_WFSP)
#include <aygshell.h>
#endif // WIN32_PLATFORM_PSPC, WIN32_PLATFORM_WFSP

#ifdef WIN32_PLATFORM_WFSP
#include <tpcshell.h>
#endif /* WIN32_PLATFORM_WFSP */

/* Optional custom definitions to exclude some types */
#include "defines.h"

/* Structs excludes */
#ifdef _WIN32_WCE
#define NO_ACTCTX
#define NO_BLENDFUNCTION
#define NO_BP_PAINTPARAMS
#define NO_BROWSEINFO
#define NO_BUTTON_IMAGELIST
#define NO_CHOOSEFONT
#define NO_COMBOBOXINFO
#define NO_DEVMODE
#define NO_DEVMODEA
#define NO_DEVMODEW
#define NO_DOCINFO
#define NO_DWM_BLURBEHIND
#define NO_EMR
#define NO_EMREXTCREATEFONTINDIRECTW
#define NO_EXTLOGFONTW
#define NO_EXTLOGPEN
#define NO_GCP_RESULTS
#define NO_GRADIENT_RECT
#define NO_GUITHREADINFO
#define NO_HELPINFO
#define NO_HIGHCONTRAST
#define NO_LITEM
#define NO_LVINSERTMARK
#define NO_MARGINS
#define NO_MENUBARINFO
#define NO_MENUINFO
#define NO_MINMAXINFO
#define NO_NMLINK
#define NO_NMREBARCHEVRON
#define NO_NMREBARCHILDSIZE
#define NO_NMTBHOTITEM
#define NO_NMTTDISPINFO
#define NO_NMTTDISPINFOA
#define NO_NMTTDISPINFOW
#define NO_NMTVITEMCHANGE
#define NO_NONCLIENTMETRICS
#define NO_NONCLIENTMETRICSA
#define NO_NONCLIENTMETRICSW
#define NO_OSVERSIONINFOEX
#define NO_OSVERSIONINFOEXA
#define NO_OSVERSIONINFOEXW
#define NO_OUTLINETEXTMETRIC
#define NO_OUTLINETEXTMETRICA
#define NO_OUTLINETEXTMETRICW
#define NO_PANOSE
#define NO_PRINTDLG
#define NO_SCRIPT_ANALYSIS
#define NO_SCRIPT_CONTROL
#define NO_SCRIPT_DIGITSUBSTITUTE
#define NO_SCRIPT_ITEM
#define NO_SCRIPT_LOGATTR
#define NO_SCRIPT_FONTPROPERTIES
#define NO_SCRIPT_PROPERTIES
#define NO_SCRIPT_STATE
#define NO_SCRIPT_STRING_ANALYSIS
#define NO_SCROLLBARINFO
#define NO_SHDRAGIMAGE
#define NO_TF_DA_COLOR
#define NO_TF_DISPLAYATTRIBUTE
#define NO_TOOLINFO
#define NO_TRACKMOUSEEVENT
#define NO_TRIVERTEX
#define NO_TVITEMEX
#define NO_WINDOWPLACEMENT
#define NO_DROPFILES
#define NO_MONITORINFO
#define NO_OLECMD
#define NO_OLECMDTEXT

/* OLE */
#define NO_CAUUID
#define NO_CONTROLINFO
#define NO_COSERVERINFO
#define NO_DISPPARAMS
#define NO_DVTARGETDEVICE
#define NO_EXCEPINFO
#define NO_FORMATETC
#define NO_FUNCDESC1
#define NO_FUNCDESC2
#define NO_GUID
#define NO_LICINFO
#define NO_OLEINPLACEFRAMEINFO
#define NO_STATSTG
#define NO_STGMEDIUM
#define NO_TYPEATTR
#define NO_VARDESC1
#define NO_VARDESC2
#endif /* _WIN32_WCE */

#if !defined(WIN32_PLATFORM_PSPC) && !defined(WIN32_PLATFORM_WFSP)
#define NO_SHMENUBARINFO
#endif /* WIN32_PLATFORM_PSPC, WIN32_PLATFORM_WFSP */

#ifndef WIN32_PLATFORM_PSPC
#define NO_SHACTIVATEINFO
#define NO_SHRGINFO
#define NO_SIPINFO
#endif /* WIN32_PLATFORM_PSPC */

/* Functions excludes */
#ifdef _WIN32_WCE
#define NO_AbortDoc
#define NO_ActivateActCtx
#define NO_ActivateKeyboardLayout
#define NO_AddFontResourceExA
#define NO_AddFontResourceExW
#define NO_Arc
#define NO_AssocQueryStringA
#define NO_AssocQueryStringW
#define NO_AttachThreadInput
#define NO_AllowSetForegroundWindow 
#define NO_AlphaBlend
#define NO_AnimateWindow
#define NO_BeginBufferedPaint
#define NO_BeginPath
#define NO_BufferedPaintInit
#define NO_BufferedPaintSetAlpha
#define NO_BufferedPaintUnInit
#define NO_CallNextHookEx
#define NO_CallWindowProcA
#define NO_CharLowerA
#define NO_CharUpperA
#define NO_ChooseColorA
#define NO_ChooseFontA
#define NO_ChooseFontW
#define NO_CloseEnhMetaFile
#define NO_CloseThemeData
#define NO_CoCreateInstance
#define NO_CoInternetIsFeatureEnabled
#define NO_CoInternetSetFeatureEnabled
#define NO_CopyImage
#define NO_CreateAcceleratorTableA
#define NO_CreateActCtxA
#define NO_CreateActCtxW
#define NO_CreateCursor
#define NO_CreateDCA
#define NO_CreateEnhMetaFileA
#define NO_CreateEnhMetaFileW
#define NO_CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONTA_2
#define NO_CreateFontIndirectA__I
#define NO_CreatePolygonRgn
#define NO_CreateProcessA
#define NO_CreateStreamOnHGlobal
#define NO_CreateWindowExA
#define NO_DefFrameProcA
#define NO_DefFrameProcW
#define NO_DefMDIChildProcA
#define NO_DefMDIChildProcW
#define NO_DefWindowProcA
#define NO_DeleteEnhMetaFile
#define NO_DestroyCursor
#define NO_DispatchMessageA
#define NO_DPtoLP
#define NO_DragDetect
#define NO_DragFinish
#define NO_DragQueryFileA
#define NO_DragQueryFileW
#define NO_DrawAnimatedRects
#define NO_DrawStateA
#define NO_DrawStateW
#define NO_DrawTextA
#define NO_DrawThemeBackground
#define NO_DrawThemeEdge
#define NO_DrawThemeIcon
#define NO_DrawThemeParentBackground
#define NO_DrawThemeText
#define NO_DuplicateHandle
#define NO_DwmEnableBlurBehindWindow
#define NO_DwmExtendFrameIntoClientArea
#define NO_EnableScrollBar
#define NO_EndBufferedPaint
#define NO_EndDoc
#define NO_EndPage
#define NO_EndPath
#define NO_EnumDisplayMonitors
#define NO_EnumEnhMetaFile
#define NO_EnumFontFamiliesA
#define NO_EnumFontFamiliesExA
#define NO_EnumFontFamiliesExW
#define NO_EnumSystemLanguageGroupsA
#define NO_EnumSystemLanguageGroupsW
#define NO_EnumSystemLocalesA
#define NO_ExpandEnvironmentStringsW
#define NO_ExpandEnvironmentStringsA
#define NO_ExtCreatePen
#define NO_ExtCreateRegion
#define NO_ExtTextOutA
#define NO_ExtractIconExA
#define NO_FillPath
#define NO_FindWindowA
#define NO_FormatMessageA
#define NO_GdiSetBatchLimit
#define NO_GetCharABCWidthsA
#define NO_GetCharABCWidthsW
#define NO_GetCharWidthA
#define NO_GetCharWidthW
#define NO_GetCharacterPlacementA
#define NO_GetCharacterPlacementW
#define NO_GetClassInfoA
#define NO_GetClassNameA
#define NO_GetClipboardFormatNameA
#define NO_GetComboBoxInfo
#define NO_GetDateFormatA
#define NO_GetDIBColorTable
#define NO_GetDIBits
#define NO_GetFontLanguageInfo
#define NO_GetGraphicsMode
#define NO_GetGUIThreadInfo
#define NO_GetIconInfo
#define NO_GetKeyboardLayout
#define NO_GetKeyboardLayoutList
#define NO_GetKeyboardState
#define NO_GetKeyNameTextA
#define NO_GetKeyNameTextW
#define NO_GetLastActivePopup
#define NO_GetLayeredWindowAttributes
#define NO_GetLayout
#define NO_GetLocaleInfoA
#define NO_GetMapMode
#define NO_GetMenu
#define NO_GetMenuBarInfo
#define NO_GetMenuDefaultItem
#define NO_GetMenuInfo
#define NO_GetMenuItemCount
#define NO_GetMenuItemInfoA
#define NO_GetMenuItemRect
#define NO_GetMessageA
#define NO_GetMessageTime
#define NO_GetMetaRgn
#define NO_GetModuleFileNameA
#define NO_GetModuleHandleA
#define NO_GetMonitorInfoA
#define NO_GetMonitorInfoW
#define NO_GetObjectW__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2
#define NO_GetObjectA__III
#define NO_GetObjectA__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2
#define NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2
#define NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONTA_2
#define NO_GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2
#define NO_GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2
#define NO_GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2
#define NO_GetOpenFileNameA
#define NO_GetOutlineTextMetricsA
#define NO_GetOutlineTextMetricsW
#define NO_GetPath
#define NO_GetPolyFillMode
#define NO_GetProcessHeaps
#define NO_GetProfileStringA
#define NO_GetProfileStringW
#define NO_GetPropA
#define NO_GetPropW
#define NO_GetRandomRgn
#define NO_GetROP2
#define NO_GetSaveFileNameA
#define NO_GetScrollBarInfo
#define NO_GetStartupInfoA
#define NO_GetStartupInfoW
#define NO_GetSystemDefaultUILanguage
#define NO_GetTextCharset
#define NO_GetTextExtentPoint32A
#define NO_GetTextMetricsA
#define NO_GetThemeColor
#define NO_GetThemeTextExtent
#define NO_GetThemeInt
#define NO_GetThemeMargins
#define NO_GetThemeBackgroundContentRect
#define NO_GetThemeBackgroundExtent
#define NO_GetThemePartSize
#define NO_GetThemeMetric
#define NO_GetThemeRect
#define NO_GetThemeSysSize
#define NO_GetTimeFormatA
#define NO_GetVersionExA__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOA_2
#define NO_GetVersionExA__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOEXA_2
#define NO_GetVersionExW__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOEXW_2
#define NO_GetWindowLongA
#define NO_GetWindowLongPtrA
//#define NO_GetWindowLongPtrW
#define NO_GetWindowOrgEx
#define NO_GetWindowPlacement
#define NO_GetWindowTextA
#define NO_GetWindowTextLengthA
#define NO_GetWindowTheme
#define NO_GetWorldTransform
#define NO_GlobalAddAtomA
#define NO_GlobalAddAtomW
#define NO_GradientFill
#define NO_HitTestThemeBackground
#define NO_IIDFromString
#define NO_ImmDisableTextFrameService
#define NO_ImmGetCompositionFontA
#define NO_ImmGetCompositionStringA__II_3BI
#define NO_ImmGetCompositionStringA__JI_3BI
#define NO_ImmGetCompositionStringA__II_3II
#define NO_ImmGetCompositionStringA__JI_3II
#define NO_ImmSetCompositionFontA
#define NO_InsertMenuA
#define NO_InsertMenuItemA
#define NO_InsertMenuItemW
#define NO_InternetGetCookieA
#define NO_InternetGetCookieW
#define NO_InternetSetCookieA
#define NO_InternetSetCookieW
#define NO_InternetSetOption
#define NO_InvalidateRgn
#define NO_IsAppThemed
#define NO_IsHungAppWindow
#define NO_IsIconic
#define NO_IsZoomed
#define NO_LineTo
#define NO_LoadBitmapA
#define NO_LoadCursorA
#define NO_LoadIconA
#define NO_LoadImageA__IIIIII
#define NO_LoadImageA__I_3BIIII
#define NO_LoadLibraryA
#define NO_LoadStringA
#define NO_LockWindowUpdate
#define NO_LPtoDP
#define NO_MapVirtualKeyA
#define NO_MCIWndRegisterClass
#define NO_MessageBoxA
#define NO_ModifyWorldTransform
#define NO_MonitorFromWindow
#define NO_MoveMemory__ILorg_eclipse_swt_internal_win32_DEVMODEA_2I
#define NO_MoveMemory__ILorg_eclipse_swt_internal_win32_DEVMODEW_2I
#define NO_MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_EMREXTCREATEFONTINDIRECTW_2II
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_EMR_2II
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2II
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2II
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II
#define NO_MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I
#define NO_MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2II
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2II
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2II
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2II
#define NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I
#define NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I
#define NO_MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVITEMCHANGE_2I
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVITEMCHANGE_2II
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2II
#define NO_MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTA_2I
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2II
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2II
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2II
#define NO_MoveMemory__ILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2II
#define NO_MoveMemory__ILorg_eclipse_swt_internal_win32_MINMAXINFO_2I
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2II
#define NO_MoveToEx
#define NO_NotifyWinEvent
#define NO_OleInitialize
#define NO_OleUninitialize
#define NO_OpenThemeData
#define NO_PeekMessageA
#define NO_Pie
#define NO_PostMessageA
#define NO_PostThreadMessageA
#define NO_PrintDlgA
#define NO_PrintDlgW
#define NO_PrintWindow
#define NO_RedrawWindow
#define NO_RegEnumKeyExA
#define NO_RegOpenKeyExA
#define NO_RegQueryInfoKeyA
#define NO_RegQueryValueExA__I_3BI_3I_3B_3I
#define NO_RegQueryValueExA__I_3BI_3I_3I_3I
#define NO_RegisterClassA
#define NO_RegisterClipboardFormatA
#define NO_RegisterWindowMessageA
#define NO_RemoveFontResourceExW
#define NO_RemovePropA
#define NO_RemovePropW
#define NO_ReplyMessage
#define NO_SetLayeredWindowAttributes
#define NO_SetLayout
#define NO_SetProcessDPIAware
#define NO_Shell_1NotifyIconA
#define NO_SHBrowseForFolderA
#define NO_SHBrowseForFolderW
#define NO_SHGetFileInfoA
#define NO_SHGetFolderPathA
#define NO_SHGetFolderPathW
#define NO_SHGetPathFromIDListA
#define NO_SHGetPathFromIDListW
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LITEM_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TVSORTCB_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVINSERTMARK_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_MARGINS_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_MCHITTESTINFO_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_POINT_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_UDACCEL_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_HDITEM_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_SIZE_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_HDHITTESTINFO_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_SYSTEMTIME_2
#define NO_SendMessageA__IIILorg_eclipse_swt_internal_win32_TCHITTESTINFO_2
#define NO_SendMessageA__IIII
#define NO_SendMessageA__III_3C
#define NO_SendMessageA__III_3S
#define NO_SendMessageA__III_3I
#define NO_SendMessageA__III_3B
#define NO_SendMessageA__II_3II
#define NO_SendMessageA__II_3I_3I
#define NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2
#define NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LITEM_2
#define NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_MARGINS_2
#define NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2
#define NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_LVINSERTMARK_2
#define NO_SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2
#define NO_SetDIBColorTable
#define NO_SetErrorMode
#define NO_SetGraphicsMode
#define NO_SetMapMode
#define NO_SetMapperFlags
#define NO_SetMenu
#define NO_SetMenuDefaultItem
#define NO_SetMenuInfo
#define NO_SetMenuItemInfoA
#define NO_SetMetaRgn
#define NO_SetPolyFillMode
#define NO_SetPropA
#define NO_SetPropW
#define NO_SetStretchBltMode
#define NO_SetTextAlign
#define NO_SetViewportExtEx
#define NO_SetWindowExtEx
#define NO_SetWindowLongA
#define NO_SetWindowLongPtrA
//#define NO_SetWindowLongPtrW
#define NO_SetWindowOrgEx
#define NO_SetWindowPlacement
#define NO_SetWindowTextA
#define NO_SetWindowTheme
#define NO_SetWindowsHookExA
#define NO_SetWindowsHookExW
#define NO_SetWorldTransform
#define NO_ShellExecuteExA
#define NO_ShowOwnedPopups
#define NO_ShowScrollBar
#define NO_StartDocA
#define NO_StartDocW
#define NO_StartPage
#define NO_SystemParametersInfoA__II_3II
#define NO_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I
#define NO_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSA_2I
#define NO_SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSW_2I
#define NO_SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_HIGHCONTRAST_2I
#define NO_SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_HIGHCONTRAST_2I
#define NO_SCRIPT_1STRING_1ANALYSIS_1sizeof
#define NO_ScriptApplyDigitSubstitution
#define NO_ScriptBreak
#define NO_ScriptCacheGetHeight
#define NO_ScriptCPtoX
#define NO_ScriptFreeCache
#define NO_ScriptGetCMap
#define NO_ScriptGetLogicalWidths
#define NO_ScriptGetFontProperties
#define NO_ScriptGetProperties
#define NO_ScriptItemize
#define NO_ScriptJustify
#define NO_ScriptLayout
#define NO_ScriptPlace
#define NO_ScriptRecordDigitSubstitution
#define NO_ScriptShape
#define NO_ScriptStringAnalyse
#define NO_ScriptStringFree
#define NO_ScriptStringOut
#define NO_ScriptTextOut
#define NO_ScriptXtoCP
#define NO_StrokePath
#define NO_ToAscii
#define NO_ToUnicode
#define NO_TrackMouseEvent
#define NO_TranslateAcceleratorA
#define NO_TranslateMDISysAccel
#define NO_TransparentBlt
#define NO_UnhookWindowsHookEx
#define NO_UnregisterClassA
#define NO_UpdateLayeredWindow
#define NO_VkKeyScanA
#define NO_VkKeyScanW
#define NO_VtblCall__IILorg_eclipse_swt_internal_win32_TF_1DISPLAYATTRIBUTE_2
#define NO_WaitMessage
#define NO_WindowFromDC

#define GetWindowLongPtrW GetWindowLongW
#define SetWindowLongPtrW SetWindowLongW
#define ChooseColorW ChooseColor
#define CHOOSECOLORW CHOOSECOLOR
#define LPCHOOSECOLORW LPCHOOSECOLOR
#define FONTENUMPROCW FONTENUMPROC
#define NOTIFYICONDATAW NOTIFYICONDATA
#define Shell_NotifyIconW Shell_NotifyIcon
#define ShellExecuteExW ShellExecuteEx
#define SHGetFileInfoW SHGetFileInfo
#define LPSHELLEXECUTEINFOW LPSHELLEXECUTEINFO
#endif /* _WIN32_WCE */

#ifndef WIN32_PLATFORM_HPC2000
#define NO_CommandBar_1AddAdornments
#define NO_CommandBar_1Create
#define NO_CommandBar_1DrawMenuBar
#define NO_CommandBar_1Height
#define NO_CommandBar_1InsertMenubarEx
#define NO_CommandBar_1Show
#endif /* WIN32_PLATFORM_HPC2000 */

#if !defined(WIN32_PLATFORM_PSPC) && !defined(WIN32_PLATFORM_WFSP)
#define NO_SHCreateMenuBar
#define NO_SHSetAppKeyWndAssoc
#endif /* WIN32_PLATFORM_PSPC, WIN32_PLATFORM_WFSP */

#ifdef WIN32_PLATFORM_WFSP
#define NO_CommDlgExtendedError
#define NO_ChooseColorW
#endif /* WIN32_PLATFORM_WFSP */

#ifndef WIN32_PLATFORM_PSPC
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2II
#define NO_MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2JI
#define NO_NMRGINFO
#define NO_SHHandleWMSettingChange
#define NO_SHRecognizeGesture
#define NO_SHSipPreference
#define NO_SipGetInfo
#endif /* WIN32_PLATFORM_PSPC */

#ifndef WIN32_PLATFORM_WFSP
#define NO_SHSendBackToFocusWindow
#endif /* WIN32_PLATFORM_WFSP */

#ifndef _WIN32_WCE
#define NO_CommandBar_1Destroy
#define NO_TransparentImage
#endif /* _WIN32_WCE */

#define TrackMouseEvent _TrackMouseEvent
#ifdef _WIN32_WCE
#define NOTIFYICONDATAA_V2_SIZE     sizeof(NOTIFYICONDATAA)
#define NOTIFYICONDATAW_V2_SIZE     sizeof(NOTIFYICONDATAW)
#else
#undef NOTIFYICONDATAA_V2_SIZE
#undef NOTIFYICONDATAW_V2_SIZE
#define NOTIFYICONDATAA_V2_SIZE     (FIELD_OFFSET(NOTIFYICONDATAA, dwInfoFlags)+sizeof(int))
#define NOTIFYICONDATAW_V2_SIZE     (FIELD_OFFSET(NOTIFYICONDATAW, dwInfoFlags)+sizeof(int))
#endif

#ifndef _WIN32_WCE
#ifndef _BP_PAINTPARAMS
typedef HANDLE HPAINTBUFFER;
typedef struct _BP_PAINTPARAMS {
    DWORD cbSize;
    DWORD dwFlags;
    const RECT *prcExclude;
    const BLENDFUNCTION *pBlendFunction;
} BP_PAINTPARAMS, *PBP_PAINTPARAMS;
#endif

#ifndef _DWM_BLURBEHIND
typedef struct _DWM_BLURBEHIND {
    DWORD dwFlags;
    BOOL fEnable;
    HRGN hRgnBlur;
    BOOL fTransitionOnMaximized;
} DWM_BLURBEHIND, *PDWM_BLURBEHIND;
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
#endif /* _WIN32_WCE */

#ifndef GET_WHEEL_DELTA_WPARAM
#define GET_WHEEL_DELTA_WPARAM(wParam)  ((short)HIWORD(wParam))
#endif

#include "os_custom.h"

#endif /* INC_os_H */
