/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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

int OS_nativeFunctionCount = 1064;
int OS_nativeFunctionCallCount[1064];
char * OS_nativeFunctionNames[] = {
	"ACCEL_1sizeof",
	"ACTCTX_1sizeof",
	"AbortDoc",
	"ActivateActCtx",
	"ActivateKeyboardLayout",
	"AddFontResourceExA",
	"AddFontResourceExW",
	"AdjustWindowRectEx",
	"AllowSetForegroundWindow",
	"AlphaBlend",
	"AnimateWindow",
	"Arc",
	"AssocQueryStringA",
	"AssocQueryStringW",
	"AttachThreadInput",
	"BITMAPINFOHEADER_1sizeof",
	"BITMAP_1sizeof",
	"BLENDFUNCTION_1sizeof",
	"BP_1PAINTPARAMS_1sizeof",
	"BROWSEINFO_1sizeof",
	"BUTTON_1IMAGELIST_1sizeof",
	"BeginBufferedPaint",
	"BeginDeferWindowPos",
	"BeginPaint",
	"BeginPath",
	"BitBlt",
	"BringWindowToTop",
	"BufferedPaintInit",
	"BufferedPaintSetAlpha",
	"BufferedPaintUnInit",
	"CANDIDATEFORM_1sizeof",
	"CERT_1CONTEXT_1sizeof",
	"CERT_1INFO_1sizeof",
	"CERT_1NAME_1BLOB_1sizeof",
	"CERT_1PUBLIC_1KEY_1INFO_1sizeof",
	"CHOOSECOLOR_1sizeof",
	"CHOOSEFONT_1sizeof",
	"COMBOBOXINFO_1sizeof",
	"COMPOSITIONFORM_1sizeof",
	"CREATESTRUCT_1sizeof",
	"CRYPT_1ALGORITHM_1IDENTIFIER_1sizeof",
	"CRYPT_1BIT_1BLOB_1sizeof",
	"CRYPT_1INTEGER_1BLOB_1sizeof",
	"CRYPT_1OBJID_1BLOB_1sizeof",
#ifndef JNI64
	"Call__I",
#else
	"Call__J",
#endif
#ifndef JNI64
	"Call__ILorg_eclipse_swt_internal_win32_DLLVERSIONINFO_2",
#else
	"Call__JLorg_eclipse_swt_internal_win32_DLLVERSIONINFO_2",
#endif
	"CallNextHookEx",
	"CallWindowProcA",
	"CallWindowProcW",
	"CertNameToStrA",
	"CertNameToStrW",
	"CharLowerA",
	"CharLowerW",
	"CharUpperA",
	"CharUpperW",
	"CheckMenuItem",
	"ChooseColorA",
	"ChooseColorW",
	"ChooseFontA",
	"ChooseFontW",
	"ClientToScreen",
	"CloseClipboard",
	"CloseEnhMetaFile",
	"CloseGestureInfoHandle",
	"CloseHandle",
	"ClosePrinter",
	"CloseThemeData",
	"CloseTouchInputHandle",
	"CoCreateInstance",
	"CoInternetIsFeatureEnabled",
	"CoInternetSetFeatureEnabled",
	"CoTaskMemAlloc",
	"CoTaskMemFree",
	"CombineRgn",
	"CommDlgExtendedError",
	"CommandBar_1AddAdornments",
	"CommandBar_1Create",
	"CommandBar_1Destroy",
	"CommandBar_1DrawMenuBar",
	"CommandBar_1Height",
	"CommandBar_1InsertMenubarEx",
	"CommandBar_1Show",
	"CopyImage",
	"CreateAcceleratorTableA",
	"CreateAcceleratorTableW",
	"CreateActCtxA",
	"CreateActCtxW",
	"CreateBitmap",
	"CreateCaret",
	"CreateCompatibleBitmap",
	"CreateCompatibleDC",
	"CreateCursor",
	"CreateDCA",
	"CreateDCW",
#ifndef JNI64
	"CreateDIBSection__III_3III",
#else
	"CreateDIBSection__JJI_3JJI",
#endif
#ifndef JNI64
	"CreateDIBSection__I_3BI_3III",
#else
	"CreateDIBSection__J_3BI_3JJI",
#endif
	"CreateEnhMetaFileA",
	"CreateEnhMetaFileW",
#ifndef JNI64
	"CreateFontIndirectA__I",
#else
	"CreateFontIndirectA__J",
#endif
	"CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONTA_2",
#ifndef JNI64
	"CreateFontIndirectW__I",
#else
	"CreateFontIndirectW__J",
#endif
	"CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONTW_2",
	"CreateIconIndirect",
	"CreateMenu",
	"CreatePalette",
	"CreatePatternBrush",
	"CreatePen",
	"CreatePolygonRgn",
	"CreatePopupMenu",
	"CreateProcessA",
	"CreateProcessW",
	"CreateRectRgn",
	"CreateSolidBrush",
	"CreateStreamOnHGlobal",
	"CreateWindowExA",
	"CreateWindowExW",
	"DEVMODEA_1sizeof",
	"DEVMODEW_1sizeof",
	"DIBSECTION_1sizeof",
	"DLLVERSIONINFO_1sizeof",
	"DOCHOSTUIINFO_1sizeof",
	"DOCINFO_1sizeof",
	"DPtoLP",
	"DRAWITEMSTRUCT_1sizeof",
	"DROPFILES_1sizeof",
	"DWM_1BLURBEHIND_1sizeof",
	"DefFrameProcA",
	"DefFrameProcW",
	"DefMDIChildProcA",
	"DefMDIChildProcW",
	"DefWindowProcA",
	"DefWindowProcW",
	"DeferWindowPos",
	"DeleteDC",
	"DeleteEnhMetaFile",
	"DeleteMenu",
	"DeleteObject",
	"DestroyAcceleratorTable",
	"DestroyCaret",
	"DestroyCursor",
	"DestroyIcon",
	"DestroyMenu",
	"DestroyWindow",
	"DispatchMessageA",
	"DispatchMessageW",
	"DocumentPropertiesA",
	"DocumentPropertiesW",
	"DragDetect",
	"DragFinish",
	"DragQueryFileA",
	"DragQueryFileW",
	"DrawAnimatedRects",
	"DrawEdge",
	"DrawFocusRect",
	"DrawFrameControl",
	"DrawIconEx",
	"DrawMenuBar",
	"DrawStateA",
	"DrawStateW",
	"DrawTextA",
	"DrawTextW",
	"DrawThemeBackground",
	"DrawThemeEdge",
	"DrawThemeIcon",
	"DrawThemeParentBackground",
	"DrawThemeText",
	"DuplicateHandle",
	"DwmEnableBlurBehindWindow",
	"DwmExtendFrameIntoClientArea",
	"EMREXTCREATEFONTINDIRECTW_1sizeof",
	"EMR_1sizeof",
	"EXTLOGFONTW_1sizeof",
	"EXTLOGPEN_1sizeof",
	"Ellipse",
	"EnableMenuItem",
	"EnableScrollBar",
	"EnableWindow",
	"EndBufferedPaint",
	"EndDeferWindowPos",
	"EndDoc",
	"EndPage",
	"EndPaint",
	"EndPath",
	"EnumDisplayMonitors",
	"EnumEnhMetaFile",
	"EnumFontFamiliesA",
	"EnumFontFamiliesExA",
	"EnumFontFamiliesExW",
	"EnumFontFamiliesW",
	"EnumSystemLanguageGroupsA",
	"EnumSystemLanguageGroupsW",
	"EnumSystemLocalesA",
	"EnumSystemLocalesW",
	"EqualRect",
	"EqualRgn",
	"ExcludeClipRect",
	"ExpandEnvironmentStringsA",
	"ExpandEnvironmentStringsW",
	"ExtCreatePen",
	"ExtCreateRegion",
	"ExtTextOutA",
	"ExtTextOutW",
	"ExtractIconExA",
	"ExtractIconExW",
	"FILETIME_1sizeof",
	"FLICK_1DATA_1sizeof",
	"FLICK_1POINT_1sizeof",
	"FileTimeToSystemTime",
	"FillPath",
	"FillRect",
	"FindWindowA",
	"FindWindowW",
	"FormatMessageA",
	"FormatMessageW",
	"FreeLibrary",
	"GCP_1RESULTS_1sizeof",
	"GESTURECONFIG_1sizeof",
	"GESTUREINFO_1sizeof",
	"GET_1WHEEL_1DELTA_1WPARAM",
	"GET_1X_1LPARAM",
	"GET_1Y_1LPARAM",
	"GID_1ROTATE_1ANGLE_1FROM_1ARGUMENT",
	"GRADIENT_1RECT_1sizeof",
	"GUITHREADINFO_1sizeof",
	"GdiSetBatchLimit",
	"GetACP",
	"GetActiveWindow",
	"GetAsyncKeyState",
	"GetBkColor",
	"GetCapture",
	"GetCaretPos",
	"GetCharABCWidthsA",
	"GetCharABCWidthsW",
	"GetCharWidthA",
	"GetCharWidthW",
	"GetCharacterPlacementA",
	"GetCharacterPlacementW",
	"GetClassInfoA",
	"GetClassInfoW",
	"GetClassNameA",
	"GetClassNameW",
	"GetClientRect",
	"GetClipBox",
	"GetClipRgn",
	"GetClipboardData",
	"GetClipboardFormatNameA",
	"GetClipboardFormatNameW",
	"GetComboBoxInfo",
	"GetCurrentObject",
	"GetCurrentProcess",
	"GetCurrentProcessId",
	"GetCurrentThreadId",
	"GetCursor",
	"GetCursorPos",
	"GetDC",
	"GetDCEx",
	"GetDIBColorTable",
	"GetDIBits",
	"GetDateFormatA",
	"GetDateFormatW",
	"GetDesktopWindow",
	"GetDeviceCaps",
	"GetDialogBaseUnits",
	"GetDlgItem",
	"GetDoubleClickTime",
	"GetFocus",
	"GetFontLanguageInfo",
	"GetForegroundWindow",
	"GetGUIThreadInfo",
	"GetGestureInfo",
	"GetGlyphIndicesW",
	"GetGraphicsMode",
	"GetIconInfo",
	"GetKeyNameTextA",
	"GetKeyNameTextW",
	"GetKeyState",
	"GetKeyboardLayout",
	"GetKeyboardLayoutList",
	"GetKeyboardState",
	"GetLastActivePopup",
	"GetLastError",
	"GetLayeredWindowAttributes",
	"GetLayout",
	"GetLibraryHandle",
	"GetLocaleInfoA",
	"GetLocaleInfoW",
	"GetMapMode",
	"GetMenu",
	"GetMenuBarInfo",
	"GetMenuDefaultItem",
	"GetMenuInfo",
	"GetMenuItemCount",
	"GetMenuItemInfoA",
	"GetMenuItemInfoW",
	"GetMenuItemRect",
	"GetMessageA",
	"GetMessagePos",
	"GetMessageTime",
	"GetMessageW",
	"GetMetaRgn",
	"GetModuleFileNameA",
	"GetModuleFileNameW",
	"GetModuleHandleA",
	"GetModuleHandleW",
	"GetMonitorInfoA",
	"GetMonitorInfoW",
	"GetNearestPaletteIndex",
#ifndef JNI64
	"GetObjectA__III",
#else
	"GetObjectA__JIJ",
#endif
#ifndef JNI64
	"GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2",
#else
	"GetObjectA__JILorg_eclipse_swt_internal_win32_BITMAP_2",
#endif
#ifndef JNI64
	"GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2",
#else
	"GetObjectA__JILorg_eclipse_swt_internal_win32_DIBSECTION_2",
#endif
#ifndef JNI64
	"GetObjectA__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2",
#else
	"GetObjectA__JILorg_eclipse_swt_internal_win32_EXTLOGPEN_2",
#endif
#ifndef JNI64
	"GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2",
#else
	"GetObjectA__JILorg_eclipse_swt_internal_win32_LOGBRUSH_2",
#endif
#ifndef JNI64
	"GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONTA_2",
#else
	"GetObjectA__JILorg_eclipse_swt_internal_win32_LOGFONTA_2",
#endif
#ifndef JNI64
	"GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2",
#else
	"GetObjectA__JILorg_eclipse_swt_internal_win32_LOGPEN_2",
#endif
#ifndef JNI64
	"GetObjectW__III",
#else
	"GetObjectW__JIJ",
#endif
#ifndef JNI64
	"GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2",
#else
	"GetObjectW__JILorg_eclipse_swt_internal_win32_BITMAP_2",
#endif
#ifndef JNI64
	"GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2",
#else
	"GetObjectW__JILorg_eclipse_swt_internal_win32_DIBSECTION_2",
#endif
#ifndef JNI64
	"GetObjectW__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2",
#else
	"GetObjectW__JILorg_eclipse_swt_internal_win32_EXTLOGPEN_2",
#endif
#ifndef JNI64
	"GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2",
#else
	"GetObjectW__JILorg_eclipse_swt_internal_win32_LOGBRUSH_2",
#endif
#ifndef JNI64
	"GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONTW_2",
#else
	"GetObjectW__JILorg_eclipse_swt_internal_win32_LOGFONTW_2",
#endif
#ifndef JNI64
	"GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2",
#else
	"GetObjectW__JILorg_eclipse_swt_internal_win32_LOGPEN_2",
#endif
	"GetOpenFileNameA",
	"GetOpenFileNameW",
	"GetOutlineTextMetricsA",
	"GetOutlineTextMetricsW",
	"GetPaletteEntries",
	"GetParent",
	"GetPath",
	"GetPixel",
	"GetPolyFillMode",
	"GetProcAddress",
	"GetProcessHeap",
	"GetProcessHeaps",
	"GetProfileStringA",
	"GetProfileStringW",
	"GetPropA",
	"GetPropW",
	"GetROP2",
	"GetRandomRgn",
	"GetRegionData",
	"GetRgnBox",
	"GetSaveFileNameA",
	"GetSaveFileNameW",
	"GetScrollBarInfo",
	"GetScrollInfo",
	"GetStartupInfoA",
	"GetStartupInfoW",
	"GetStockObject",
	"GetSysColor",
	"GetSysColorBrush",
	"GetSystemDefaultUILanguage",
	"GetSystemMenu",
	"GetSystemMetrics",
	"GetSystemPaletteEntries",
	"GetTextCharset",
	"GetTextColor",
	"GetTextExtentPoint32A",
	"GetTextExtentPoint32W",
	"GetTextMetricsA",
	"GetTextMetricsW",
	"GetThemeBackgroundContentRect",
	"GetThemeBackgroundExtent",
	"GetThemeColor",
	"GetThemeInt",
	"GetThemeMargins",
	"GetThemeMetric",
	"GetThemePartSize",
	"GetThemeRect",
	"GetThemeSysSize",
	"GetThemeTextExtent",
	"GetTickCount",
	"GetTimeFormatA",
	"GetTimeFormatW",
	"GetTouchInputInfo",
	"GetUpdateRect",
	"GetUpdateRgn",
	"GetVersionExA__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOA_2",
	"GetVersionExA__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOEXA_2",
	"GetVersionExW__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOEXW_2",
	"GetVersionExW__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOW_2",
	"GetWindow",
	"GetWindowDC",
	"GetWindowLongA",
	"GetWindowLongPtrA",
	"GetWindowLongPtrW",
	"GetWindowLongW",
	"GetWindowOrgEx",
	"GetWindowPlacement",
	"GetWindowRect",
	"GetWindowRgn",
	"GetWindowTextA",
	"GetWindowTextLengthA",
	"GetWindowTextLengthW",
	"GetWindowTextW",
	"GetWindowTheme",
	"GetWindowThreadProcessId",
	"GetWorldTransform",
	"GlobalAddAtomA",
	"GlobalAddAtomW",
	"GlobalAlloc",
	"GlobalFree",
	"GlobalLock",
	"GlobalSize",
	"GlobalUnlock",
	"GradientFill",
	"HDHITTESTINFO_1sizeof",
	"HDITEM_1sizeof",
	"HDLAYOUT_1sizeof",
	"HELPINFO_1sizeof",
	"HIGHCONTRAST_1sizeof",
	"HIWORD",
	"HeapAlloc",
	"HeapFree",
	"HeapValidate",
	"HideCaret",
	"HitTestThemeBackground",
	"ICONINFO_1sizeof",
	"IIDFromString",
	"INITCOMMONCONTROLSEX_1sizeof",
	"INPUT_1sizeof",
	"ImageList_1Add",
	"ImageList_1AddMasked",
	"ImageList_1BeginDrag",
	"ImageList_1Create",
	"ImageList_1Destroy",
	"ImageList_1DragEnter",
	"ImageList_1DragLeave",
	"ImageList_1DragMove",
	"ImageList_1DragShowNolock",
	"ImageList_1Draw",
	"ImageList_1EndDrag",
	"ImageList_1GetDragImage",
	"ImageList_1GetIcon",
	"ImageList_1GetIconSize",
	"ImageList_1GetImageCount",
	"ImageList_1Remove",
	"ImageList_1Replace",
	"ImageList_1ReplaceIcon",
	"ImageList_1SetIconSize",
	"ImmAssociateContext",
	"ImmCreateContext",
	"ImmDestroyContext",
	"ImmDisableTextFrameService",
	"ImmGetCompositionFontA",
	"ImmGetCompositionFontW",
#ifndef JNI64
	"ImmGetCompositionStringA__II_3BI",
#else
	"ImmGetCompositionStringA__JI_3BI",
#endif
#ifndef JNI64
	"ImmGetCompositionStringA__II_3II",
#else
	"ImmGetCompositionStringA__JI_3II",
#endif
#ifndef JNI64
	"ImmGetCompositionStringW__II_3BI",
#else
	"ImmGetCompositionStringW__JI_3BI",
#endif
#ifndef JNI64
	"ImmGetCompositionStringW__II_3CI",
#else
	"ImmGetCompositionStringW__JI_3CI",
#endif
#ifndef JNI64
	"ImmGetCompositionStringW__II_3II",
#else
	"ImmGetCompositionStringW__JI_3II",
#endif
	"ImmGetContext",
	"ImmGetConversionStatus",
	"ImmGetDefaultIMEWnd",
	"ImmGetOpenStatus",
	"ImmNotifyIME",
	"ImmReleaseContext",
	"ImmSetCandidateWindow",
	"ImmSetCompositionFontA",
	"ImmSetCompositionFontW",
	"ImmSetCompositionWindow",
	"ImmSetConversionStatus",
	"ImmSetOpenStatus",
	"InSendMessage",
	"InitCommonControls",
	"InitCommonControlsEx",
	"InsertMenuA",
	"InsertMenuItemA",
	"InsertMenuItemW",
	"InsertMenuW",
	"InternetGetCookieA",
	"InternetGetCookieW",
	"InternetSetCookieA",
	"InternetSetCookieW",
	"InternetSetOption",
	"IntersectClipRect",
	"IntersectRect",
	"InvalidateRect",
	"InvalidateRgn",
	"IsAppThemed",
	"IsBadReadPtr",
	"IsBadWritePtr",
	"IsDBCSLeadByte",
	"IsHungAppWindow",
	"IsIconic",
	"IsPPC",
	"IsSP",
	"IsTouchWindow",
	"IsWindowEnabled",
	"IsWindowVisible",
	"IsZoomed",
	"KEYBDINPUT_1sizeof",
	"KillTimer",
	"LITEM_1sizeof",
	"LODWORD",
	"LOGBRUSH_1sizeof",
	"LOGFONTA_1sizeof",
	"LOGFONTW_1sizeof",
	"LOGPEN_1sizeof",
	"LOWORD",
	"LPtoDP",
	"LVCOLUMN_1sizeof",
	"LVHITTESTINFO_1sizeof",
	"LVINSERTMARK_1sizeof",
	"LVITEM_1sizeof",
	"LineTo",
	"LoadBitmapA",
	"LoadBitmapW",
	"LoadCursorA",
	"LoadCursorW",
	"LoadIconA",
	"LoadIconW",
#ifndef JNI64
	"LoadImageA__IIIIII",
#else
	"LoadImageA__JJIIII",
#endif
#ifndef JNI64
	"LoadImageA__I_3BIIII",
#else
	"LoadImageA__J_3BIIII",
#endif
#ifndef JNI64
	"LoadImageW__IIIIII",
#else
	"LoadImageW__JJIIII",
#endif
#ifndef JNI64
	"LoadImageW__I_3CIIII",
#else
	"LoadImageW__J_3CIIII",
#endif
	"LoadLibraryA",
	"LoadLibraryW",
	"LoadStringA",
	"LoadStringW",
	"LocalFree",
	"LockWindowUpdate",
	"MAKELPARAM",
	"MAKELRESULT",
	"MAKEWORD",
	"MAKEWPARAM",
	"MARGINS_1sizeof",
	"MCHITTESTINFO_1sizeof",
	"MCIWndRegisterClass",
	"MEASUREITEMSTRUCT_1sizeof",
	"MENUBARINFO_1sizeof",
	"MENUINFO_1sizeof",
	"MENUITEMINFO_1sizeof",
	"MINMAXINFO_1sizeof",
	"MONITORINFO_1sizeof",
	"MOUSEINPUT_1sizeof",
	"MSG_1sizeof",
	"MapViewOfFile",
	"MapVirtualKeyA",
	"MapVirtualKeyW",
#ifndef JNI64
	"MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I",
#else
	"MapWindowPoints__JJLorg_eclipse_swt_internal_win32_POINT_2I",
#endif
#ifndef JNI64
	"MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I",
#else
	"MapWindowPoints__JJLorg_eclipse_swt_internal_win32_RECT_2I",
#endif
	"MessageBeep",
	"MessageBoxA",
	"MessageBoxW",
	"ModifyWorldTransform",
	"MonitorFromWindow",
#ifndef JNI64
	"MoveMemory__III",
#else
	"MoveMemory__JJI",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_DEVMODEA_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_DEVMODEA_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_DEVMODEW_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_DEVMODEW_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_DROPFILES_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_GESTURECONFIG_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_GESTURECONFIG_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_KEYBDINPUT_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_KEYBDINPUT_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTA_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_LOGFONTA_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTW_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_LOGFONTW_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_MINMAXINFO_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_MINMAXINFO_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_MOUSEINPUT_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_MOUSEINPUT_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_MSG_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_MSG_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_OPENFILENAME_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_OPENFILENAME_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_RECT_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_SAFEARRAY_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_SAFEARRAY_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_TRIVERTEX_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_UDACCEL_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_UDACCEL_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_win32_WINDOWPOS_2I",
#endif
#ifndef JNI64
	"MoveMemory__I_3BI",
#else
	"MoveMemory__J_3BI",
#endif
#ifndef JNI64
	"MoveMemory__I_3CI",
#else
	"MoveMemory__J_3CI",
#endif
#ifndef JNI64
	"MoveMemory__I_3DI",
#else
	"MoveMemory__J_3DI",
#endif
#ifndef JNI64
	"MoveMemory__I_3FI",
#else
	"MoveMemory__J_3FI",
#endif
#ifndef JNI64
	"MoveMemory__I_3II",
#else
	"MoveMemory__J_3II",
#endif
#ifndef JNI64
	"MoveMemory__I_3JI",
#else
	"MoveMemory__J_3JI",
#endif
#ifndef JNI64
	"MoveMemory__I_3SI",
#else
	"MoveMemory__J_3SI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2JI",
#endif
	"MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2_3BI",
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_CERT_1CONTEXT_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_CERT_1CONTEXT_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_CERT_1INFO_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_CERT_1INFO_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEA_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEA_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEW_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEW_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_EMREXTCREATEFONTINDIRECTW_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_EMREXTCREATEFONTINDIRECTW_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_EMR_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_EMR_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_FLICK_1DATA_2_3II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_FLICK_1DATA_2_3JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_FLICK_1POINT_2_3II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_FLICK_1POINT_2_3JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVODSTATECHANGE_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVODSTATECHANGE_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTREEVIEW_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTREEVIEW_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVITEMCHANGE_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVITEMCHANGE_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_OFNOTIFY_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_OFNOTIFY_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_OPENFILENAME_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_OPENFILENAME_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2JI",
#endif
	"MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2_3JI",
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2_3II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2_3JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_TOUCHINPUT_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_TOUCHINPUT_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2JI",
#endif
#ifndef JNI64
	"MoveMemory___3BII",
#else
	"MoveMemory___3BJI",
#endif
	"MoveMemory___3BLorg_eclipse_swt_internal_win32_ACCEL_2I",
	"MoveMemory___3BLorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2I",
#ifndef JNI64
	"MoveMemory___3CII",
#else
	"MoveMemory___3CJI",
#endif
#ifndef JNI64
	"MoveMemory___3DII",
#else
	"MoveMemory___3DJI",
#endif
#ifndef JNI64
	"MoveMemory___3FII",
#else
	"MoveMemory___3FJI",
#endif
#ifndef JNI64
	"MoveMemory___3III",
#else
	"MoveMemory___3IJI",
#endif
#ifndef JNI64
	"MoveMemory___3JII",
#else
	"MoveMemory___3JJI",
#endif
#ifndef JNI64
	"MoveMemory___3SII",
#else
	"MoveMemory___3SJI",
#endif
	"MoveToEx",
	"MsgWaitForMultipleObjectsEx",
#ifndef JNI64
	"MultiByteToWideChar__IIII_3CI",
#else
	"MultiByteToWideChar__IIJI_3CI",
#endif
	"MultiByteToWideChar__II_3BI_3CI",
	"NMCUSTOMDRAW_1sizeof",
	"NMHDR_1sizeof",
	"NMHEADER_1sizeof",
	"NMLINK_1sizeof",
	"NMLISTVIEW_1sizeof",
	"NMLVCUSTOMDRAW_1sizeof",
	"NMLVDISPINFO_1sizeof",
	"NMLVFINDITEM_1sizeof",
	"NMLVODSTATECHANGE_1sizeof",
	"NMREBARCHEVRON_1sizeof",
	"NMREBARCHILDSIZE_1sizeof",
	"NMRGINFO_1sizeof",
	"NMTBHOTITEM_1sizeof",
	"NMTOOLBAR_1sizeof",
	"NMTREEVIEW_1sizeof",
	"NMTTCUSTOMDRAW_1sizeof",
	"NMTTDISPINFOA_1sizeof",
	"NMTTDISPINFOW_1sizeof",
	"NMTVCUSTOMDRAW_1sizeof",
	"NMTVDISPINFO_1sizeof",
	"NMTVITEMCHANGE_1sizeof",
	"NMUPDOWN_1sizeof",
	"NONCLIENTMETRICSA_1sizeof",
	"NONCLIENTMETRICSW_1sizeof",
	"NOTIFYICONDATAA_1V2_1SIZE",
	"NOTIFYICONDATAW_1V2_1SIZE",
	"NotifyWinEvent",
	"OFNOTIFY_1sizeof",
	"OPENFILENAME_1sizeof",
	"OSVERSIONINFOA_1sizeof",
	"OSVERSIONINFOEXA_1sizeof",
	"OSVERSIONINFOEXW_1sizeof",
	"OSVERSIONINFOW_1sizeof",
	"OUTLINETEXTMETRICA_1sizeof",
	"OUTLINETEXTMETRICW_1sizeof",
	"OffsetRect",
	"OffsetRgn",
	"OleInitialize",
	"OleUninitialize",
	"OpenClipboard",
	"OpenPrinterA",
	"OpenPrinterW",
	"OpenProcess",
	"OpenThemeData",
	"PAINTSTRUCT_1sizeof",
	"PANOSE_1sizeof",
	"POINTSTOPOINT",
	"POINT_1sizeof",
	"PRIMARYLANGID",
	"PRINTDLG_1sizeof",
	"PROCESS_1INFORMATION_1sizeof",
	"PROPERTYKEY_1sizeof",
	"PROPVARIANT_1sizeof",
	"PSPropertyKeyFromString",
	"PatBlt",
	"PathIsExe",
	"PeekMessageA",
	"PeekMessageW",
	"Pie",
	"Polygon",
	"Polyline",
	"PostMessageA",
	"PostMessageW",
	"PostThreadMessageA",
	"PostThreadMessageW",
	"PrintDlgA",
	"PrintDlgW",
	"PrintWindow",
	"PtInRect",
	"PtInRegion",
	"REBARBANDINFO_1sizeof",
	"RECT_1sizeof",
	"RealizePalette",
	"RectInRegion",
	"Rectangle",
	"RedrawWindow",
	"RegCloseKey",
	"RegCreateKeyExA",
	"RegCreateKeyExW",
	"RegDeleteValueA",
	"RegDeleteValueW",
	"RegEnumKeyExA",
	"RegEnumKeyExW",
	"RegOpenKeyExA",
	"RegOpenKeyExW",
	"RegQueryInfoKeyA",
	"RegQueryInfoKeyW",
#ifndef JNI64
	"RegQueryValueExA__I_3BI_3I_3B_3I",
#else
	"RegQueryValueExA__J_3BJ_3I_3B_3I",
#endif
#ifndef JNI64
	"RegQueryValueExA__I_3BI_3I_3I_3I",
#else
	"RegQueryValueExA__J_3BJ_3I_3I_3I",
#endif
#ifndef JNI64
	"RegQueryValueExW__I_3CI_3I_3C_3I",
#else
	"RegQueryValueExW__J_3CJ_3I_3C_3I",
#endif
#ifndef JNI64
	"RegQueryValueExW__I_3CI_3I_3I_3I",
#else
	"RegQueryValueExW__J_3CJ_3I_3I_3I",
#endif
	"RegSetValueExA",
	"RegSetValueExW",
	"RegisterClassA",
	"RegisterClassW",
	"RegisterClipboardFormatA",
	"RegisterClipboardFormatW",
	"RegisterTouchWindow",
	"RegisterWindowMessageA",
	"RegisterWindowMessageW",
	"ReleaseCapture",
	"ReleaseDC",
	"RemoveMenu",
	"RemovePropA",
	"RemovePropW",
	"ReplyMessage",
	"RestoreDC",
	"RoundRect",
	"SAFEARRAYBOUND_1sizeof",
	"SAFEARRAY_1sizeof",
	"SCRIPT_1ANALYSIS_1sizeof",
	"SCRIPT_1CONTROL_1sizeof",
	"SCRIPT_1DIGITSUBSTITUTE_1sizeof",
	"SCRIPT_1FONTPROPERTIES_1sizeof",
	"SCRIPT_1ITEM_1sizeof",
	"SCRIPT_1LOGATTR_1sizeof",
	"SCRIPT_1PROPERTIES_1sizeof",
	"SCRIPT_1STATE_1sizeof",
	"SCRIPT_1STRING_1ANALYSIS_1sizeof",
	"SCROLLBARINFO_1sizeof",
	"SCROLLINFO_1sizeof",
	"SHACTIVATEINFO_1sizeof",
	"SHBrowseForFolderA",
	"SHBrowseForFolderW",
	"SHCreateItemFromRelativeName",
	"SHCreateItemInKnownFolder",
	"SHCreateMenuBar",
	"SHDRAGIMAGE_1sizeof",
	"SHELLEXECUTEINFO_1sizeof",
	"SHFILEINFOA_1sizeof",
	"SHFILEINFOW_1sizeof",
	"SHGetFileInfoA",
	"SHGetFileInfoW",
	"SHGetFolderPathA",
	"SHGetFolderPathW",
	"SHGetMalloc",
	"SHGetPathFromIDListA",
	"SHGetPathFromIDListW",
	"SHHandleWMSettingChange",
	"SHMENUBARINFO_1sizeof",
	"SHRGINFO_1sizeof",
	"SHRecognizeGesture",
	"SHSendBackToFocusWindow",
	"SHSetAppKeyWndAssoc",
	"SHSipPreference",
	"SIPINFO_1sizeof",
	"SIZE_1sizeof",
	"STARTUPINFO_1sizeof",
	"SYSTEMTIME_1sizeof",
	"SaveDC",
	"ScreenToClient",
	"ScriptApplyDigitSubstitution",
	"ScriptBreak",
	"ScriptCPtoX",
	"ScriptCacheGetHeight",
	"ScriptFreeCache",
	"ScriptGetCMap",
	"ScriptGetFontProperties",
	"ScriptGetLogicalWidths",
	"ScriptGetProperties",
	"ScriptItemize",
	"ScriptJustify",
	"ScriptLayout",
	"ScriptPlace",
	"ScriptRecordDigitSubstitution",
	"ScriptShape",
	"ScriptStringAnalyse",
	"ScriptStringFree",
	"ScriptStringOut",
	"ScriptTextOut",
	"ScriptXtoCP",
	"ScrollWindowEx",
	"SelectClipRgn",
	"SelectObject",
	"SelectPalette",
	"SendInput",
#ifndef JNI64
	"SendMessageA__IIII",
#else
	"SendMessageA__JIJJ",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_HDHITTESTINFO_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDHITTESTINFO_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_HDITEM_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDITEM_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDLAYOUT_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_LITEM_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_LITEM_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVCOLUMN_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVHITTESTINFO_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_LVINSERTMARK_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVINSERTMARK_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVITEM_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_MARGINS_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_MARGINS_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_MCHITTESTINFO_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_MCHITTESTINFO_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_POINT_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_POINT_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_REBARBANDINFO_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_RECT_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_SIZE_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_SIZE_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_SYSTEMTIME_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_SYSTEMTIME_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_TBBUTTONINFO_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_TBBUTTON_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_TCHITTESTINFO_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_TCHITTESTINFO_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_TCITEM_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_TOOLINFO_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVHITTESTINFO_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVITEM_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_TVSORTCB_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVSORTCB_2",
#endif
#ifndef JNI64
	"SendMessageA__IIILorg_eclipse_swt_internal_win32_UDACCEL_2",
#else
	"SendMessageA__JIJLorg_eclipse_swt_internal_win32_UDACCEL_2",
#endif
#ifndef JNI64
	"SendMessageA__III_3B",
#else
	"SendMessageA__JIJ_3B",
#endif
#ifndef JNI64
	"SendMessageA__III_3C",
#else
	"SendMessageA__JIJ_3C",
#endif
#ifndef JNI64
	"SendMessageA__III_3I",
#else
	"SendMessageA__JIJ_3I",
#endif
#ifndef JNI64
	"SendMessageA__III_3S",
#else
	"SendMessageA__JIJ_3S",
#endif
#ifndef JNI64
	"SendMessageA__II_3II",
#else
	"SendMessageA__JI_3JJ",
#endif
#ifndef JNI64
	"SendMessageA__II_3I_3I",
#else
	"SendMessageA__JI_3I_3I",
#endif
#ifndef JNI64
	"SendMessageW__IIII",
#else
	"SendMessageW__JIJJ",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_HDHITTESTINFO_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDHITTESTINFO_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_HDITEM_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDITEM_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDLAYOUT_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_LITEM_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_LITEM_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVCOLUMN_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVHITTESTINFO_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_LVINSERTMARK_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVINSERTMARK_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVITEM_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_MARGINS_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_MARGINS_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_MCHITTESTINFO_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_MCHITTESTINFO_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_POINT_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_POINT_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_REBARBANDINFO_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_RECT_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_SIZE_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_SIZE_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_SYSTEMTIME_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_SYSTEMTIME_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_TBBUTTONINFO_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_TBBUTTON_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_TCHITTESTINFO_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_TCHITTESTINFO_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_TCITEM_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_TOOLINFO_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVHITTESTINFO_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVITEM_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_TVSORTCB_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVSORTCB_2",
#endif
#ifndef JNI64
	"SendMessageW__IIILorg_eclipse_swt_internal_win32_UDACCEL_2",
#else
	"SendMessageW__JIJLorg_eclipse_swt_internal_win32_UDACCEL_2",
#endif
#ifndef JNI64
	"SendMessageW__III_3C",
#else
	"SendMessageW__JIJ_3C",
#endif
#ifndef JNI64
	"SendMessageW__III_3I",
#else
	"SendMessageW__JIJ_3I",
#endif
#ifndef JNI64
	"SendMessageW__III_3S",
#else
	"SendMessageW__JIJ_3S",
#endif
#ifndef JNI64
	"SendMessageW__II_3II",
#else
	"SendMessageW__JI_3JJ",
#endif
#ifndef JNI64
	"SendMessageW__II_3I_3I",
#else
	"SendMessageW__JI_3I_3I",
#endif
	"SetActiveWindow",
	"SetBkColor",
	"SetBkMode",
	"SetBrushOrgEx",
	"SetCapture",
	"SetCaretPos",
	"SetClipboardData",
	"SetCurrentProcessExplicitAppUserModelID",
	"SetCursor",
	"SetCursorPos",
	"SetDIBColorTable",
	"SetDllDirectoryA",
	"SetDllDirectoryW",
	"SetErrorMode",
	"SetFocus",
	"SetForegroundWindow",
	"SetGestureConfig",
	"SetGraphicsMode",
	"SetLayeredWindowAttributes",
	"SetLayout",
	"SetMapMode",
	"SetMapperFlags",
	"SetMenu",
	"SetMenuDefaultItem",
	"SetMenuInfo",
	"SetMenuItemInfoA",
	"SetMenuItemInfoW",
	"SetMetaRgn",
	"SetPaletteEntries",
	"SetParent",
	"SetPixel",
	"SetPolyFillMode",
	"SetProcessDPIAware",
	"SetPropA",
	"SetPropW",
	"SetROP2",
	"SetRect",
	"SetRectRgn",
	"SetScrollInfo",
	"SetStretchBltMode",
	"SetTextAlign",
	"SetTextColor",
	"SetTimer",
	"SetViewportExtEx",
	"SetViewportOrgEx",
	"SetWindowExtEx",
	"SetWindowLongA",
	"SetWindowLongPtrA",
	"SetWindowLongPtrW",
	"SetWindowLongW",
	"SetWindowOrgEx",
	"SetWindowPlacement",
	"SetWindowPos",
	"SetWindowRgn",
	"SetWindowTextA",
	"SetWindowTextW",
	"SetWindowTheme",
	"SetWindowsHookExA",
	"SetWindowsHookExW",
	"SetWorldTransform",
	"ShellExecuteExA",
	"ShellExecuteExW",
	"Shell_1NotifyIconA",
	"Shell_1NotifyIconW",
	"ShowCaret",
	"ShowCursor",
	"ShowOwnedPopups",
	"ShowScrollBar",
	"ShowWindow",
	"SipGetInfo",
	"StartDocA",
	"StartDocW",
	"StartPage",
	"StretchBlt",
	"StrokePath",
	"SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_HIGHCONTRAST_2I",
	"SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSA_2I",
	"SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I",
	"SystemParametersInfoA__II_3II",
	"SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_HIGHCONTRAST_2I",
	"SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSW_2I",
	"SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I",
	"SystemParametersInfoW__II_3II",
	"TBBUTTONINFO_1sizeof",
	"TBBUTTON_1sizeof",
	"TCHITTESTINFO_1sizeof",
	"TCITEM_1sizeof",
	"TEXTMETRICA_1sizeof",
	"TEXTMETRICW_1sizeof",
	"TF_1DA_1COLOR_1sizeof",
	"TF_1DISPLAYATTRIBUTE_1sizeof",
	"TOOLINFO_1sizeof",
	"TOUCHINPUT_1sizeof",
	"TOUCH_1COORD_1TO_1PIXEL",
	"TRACKMOUSEEVENT_1sizeof",
	"TRIVERTEX_1sizeof",
	"TVHITTESTINFO_1sizeof",
	"TVINSERTSTRUCT_1sizeof",
	"TVITEMEX_1sizeof",
	"TVITEM_1sizeof",
	"TVSORTCB_1sizeof",
	"ToAscii",
	"ToUnicode",
	"TrackMouseEvent",
	"TrackPopupMenu",
	"TranslateAcceleratorA",
	"TranslateAcceleratorW",
	"TranslateCharsetInfo",
	"TranslateMDISysAccel",
	"TranslateMessage",
	"TransparentBlt",
	"TransparentImage",
	"TreeView_1GetItemRect",
	"UDACCEL_1sizeof",
	"UnhookWindowsHookEx",
	"UnmapViewOfFile",
	"UnregisterClassA",
	"UnregisterClassW",
	"UnregisterTouchWindow",
	"UpdateLayeredWindow",
	"UpdateWindow",
	"ValidateRect",
	"VkKeyScanA",
	"VkKeyScanW",
#ifndef JNI64
	"VtblCall__II",
#else
	"VtblCall__IJ",
#endif
#ifndef JNI64
	"VtblCall__III",
#else
	"VtblCall__IJI",
#endif
#ifndef JNI64
	"VtblCall__IIII",
#else
	"VtblCall__IJII",
#endif
#ifndef JNI64
	"VtblCall__IIIII",
#else
	"VtblCall__IJIII",
#endif
#ifndef JNI64
	"VtblCall__IIIII_3I",
#else
	"VtblCall__IJIII_3I",
#endif
#ifndef JNI64
	"VtblCall__IIII_3C_3CI",
#else
	"VtblCall__IJII_3C_3CI",
#endif
#ifndef JNI64
	"VtblCall__IIIJ",
#else
	"VtblCall__IJIJ",
#endif
#ifndef JNI64
	"VtblCall__IIIJI_3J",
#else
	"VtblCall__IJIJI_3J",
#endif
#ifndef JNI64
	"VtblCall__IIIJJ",
#else
	"VtblCall__IJIJJ",
#endif
#ifndef JNI64
	"VtblCall__III_3I",
#else
	"VtblCall__IJI_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3I_3I",
#else
	"VtblCall__IJI_3J_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3J",
#else
	"VtblCall__IJI_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJ",
#else
	"VtblCall__IJJ",
#endif
#ifndef JNI64
	"VtblCall__IIJI",
#else
	"VtblCall__IJJI",
#endif
#ifndef JNI64
	"VtblCall__IIJII_3I",
#else
	"VtblCall__IJJII_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJII_3J",
#else
	"VtblCall__IJJII_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJI_3C_3CJ",
#else
	"VtblCall__IJJI_3C_3CJ",
#endif
#ifndef JNI64
	"VtblCall__IIJJ",
#else
	"VtblCall__IJJJ",
#endif
#ifndef JNI64
	"VtblCall__IIJJI_3J",
#else
	"VtblCall__IJJJI_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJJJ",
#else
	"VtblCall__IJJJJ",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3I",
#else
	"VtblCall__IJJ_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3I_3I",
#else
	"VtblCall__IJJ_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3J_3J",
#else
	"VtblCall__IJJ_3J_3J",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_win32_PROPERTYKEY_2I",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_win32_PROPERTYKEY_2I",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_win32_PROPERTYKEY_2J",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_win32_PROPERTYKEY_2J",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_win32_TF_1DISPLAYATTRIBUTE_2",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_win32_TF_1DISPLAYATTRIBUTE_2",
#endif
#ifndef JNI64
	"VtblCall__IIS_3B_3B_3B",
#else
	"VtblCall__IJS_3B_3B_3B",
#endif
#ifndef JNI64
	"VtblCall__II_3B_3I",
#else
	"VtblCall__IJ_3B_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3B_3J",
#else
	"VtblCall__IJ_3B_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3C",
#else
	"VtblCall__IJ_3C",
#endif
#ifndef JNI64
	"VtblCall__II_3CI",
#else
	"VtblCall__IJ_3CI",
#endif
#ifndef JNI64
	"VtblCall__II_3CII_3I_3I",
#else
	"VtblCall__IJ_3CII_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3CJ",
#else
	"VtblCall__IJ_3CJ",
#endif
#ifndef JNI64
	"VtblCall__II_3I",
#else
	"VtblCall__IJ_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3I_3B_3I",
#else
	"VtblCall__IJ_3I_3B_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3I_3B_3J",
#else
	"VtblCall__IJ_3I_3B_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3J",
#else
	"VtblCall__IJ_3J",
#endif
	"WINDOWPLACEMENT_1sizeof",
	"WINDOWPOS_1sizeof",
	"WNDCLASS_1sizeof",
	"WaitMessage",
#ifndef JNI64
	"WideCharToMultiByte__II_3CIII_3B_3Z",
#else
	"WideCharToMultiByte__II_3CIJI_3B_3Z",
#endif
	"WideCharToMultiByte__II_3CI_3BI_3B_3Z",
	"WindowFromDC",
	"WindowFromPoint",
	"wcslen",
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
