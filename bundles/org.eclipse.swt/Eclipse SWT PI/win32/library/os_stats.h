/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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
	ACCEL_1sizeof_FUNC,
	ACTCTX_1sizeof_FUNC,
	AbortDoc_FUNC,
	ActivateActCtx_FUNC,
	ActivateKeyboardLayout_FUNC,
	AddFontResourceExA_FUNC,
	AddFontResourceExW_FUNC,
	AdjustWindowRectEx_FUNC,
	AllowSetForegroundWindow_FUNC,
	AlphaBlend_FUNC,
	AnimateWindow_FUNC,
	Arc_FUNC,
	AssocQueryStringA_FUNC,
	AssocQueryStringW_FUNC,
	AttachThreadInput_FUNC,
	BITMAPINFOHEADER_1sizeof_FUNC,
	BITMAP_1sizeof_FUNC,
	BLENDFUNCTION_1sizeof_FUNC,
	BP_1PAINTPARAMS_1sizeof_FUNC,
	BROWSEINFO_1sizeof_FUNC,
	BUTTON_1IMAGELIST_1sizeof_FUNC,
	BeginBufferedPaint_FUNC,
	BeginDeferWindowPos_FUNC,
	BeginPaint_FUNC,
	BeginPath_FUNC,
	BitBlt_FUNC,
	BringWindowToTop_FUNC,
	BufferedPaintInit_FUNC,
	BufferedPaintSetAlpha_FUNC,
	BufferedPaintUnInit_FUNC,
	CANDIDATEFORM_1sizeof_FUNC,
	CERT_1CONTEXT_1sizeof_FUNC,
	CERT_1INFO_1sizeof_FUNC,
	CERT_1NAME_1BLOB_1sizeof_FUNC,
	CERT_1PUBLIC_1KEY_1INFO_1sizeof_FUNC,
	CHOOSECOLOR_1sizeof_FUNC,
	CHOOSEFONT_1sizeof_FUNC,
	COMBOBOXINFO_1sizeof_FUNC,
	COMPOSITIONFORM_1sizeof_FUNC,
	CREATESTRUCT_1sizeof_FUNC,
	CRYPT_1ALGORITHM_1IDENTIFIER_1sizeof_FUNC,
	CRYPT_1BIT_1BLOB_1sizeof_FUNC,
	CRYPT_1INTEGER_1BLOB_1sizeof_FUNC,
	CRYPT_1OBJID_1BLOB_1sizeof_FUNC,
#ifndef JNI64
	Call__I_FUNC,
#else
	Call__J_FUNC,
#endif
#ifndef JNI64
	Call__ILorg_eclipse_swt_internal_win32_DLLVERSIONINFO_2_FUNC,
#else
	Call__JLorg_eclipse_swt_internal_win32_DLLVERSIONINFO_2_FUNC,
#endif
	CallNextHookEx_FUNC,
	CallWindowProcA_FUNC,
	CallWindowProcW_FUNC,
	CertNameToStrA_FUNC,
	CertNameToStrW_FUNC,
	CharLowerA_FUNC,
	CharLowerW_FUNC,
	CharUpperA_FUNC,
	CharUpperW_FUNC,
	CheckMenuItem_FUNC,
	ChooseColorA_FUNC,
	ChooseColorW_FUNC,
	ChooseFontA_FUNC,
	ChooseFontW_FUNC,
	ClientToScreen_FUNC,
	CloseClipboard_FUNC,
	CloseEnhMetaFile_FUNC,
	CloseGestureInfoHandle_FUNC,
	CloseHandle_FUNC,
	ClosePrinter_FUNC,
	CloseThemeData_FUNC,
	CloseTouchInputHandle_FUNC,
	CoCreateInstance_FUNC,
	CoInternetIsFeatureEnabled_FUNC,
	CoInternetSetFeatureEnabled_FUNC,
	CoTaskMemAlloc_FUNC,
	CoTaskMemFree_FUNC,
	CombineRgn_FUNC,
	CommDlgExtendedError_FUNC,
	CommandBar_1AddAdornments_FUNC,
	CommandBar_1Create_FUNC,
	CommandBar_1Destroy_FUNC,
	CommandBar_1DrawMenuBar_FUNC,
	CommandBar_1Height_FUNC,
	CommandBar_1InsertMenubarEx_FUNC,
	CommandBar_1Show_FUNC,
	CopyImage_FUNC,
	CreateAcceleratorTableA_FUNC,
	CreateAcceleratorTableW_FUNC,
	CreateActCtxA_FUNC,
	CreateActCtxW_FUNC,
	CreateBitmap_FUNC,
	CreateCaret_FUNC,
	CreateCompatibleBitmap_FUNC,
	CreateCompatibleDC_FUNC,
	CreateCursor_FUNC,
	CreateDCA_FUNC,
	CreateDCW_FUNC,
#ifndef JNI64
	CreateDIBSection__III_3III_FUNC,
#else
	CreateDIBSection__JJI_3JJI_FUNC,
#endif
#ifndef JNI64
	CreateDIBSection__I_3BI_3III_FUNC,
#else
	CreateDIBSection__J_3BI_3JJI_FUNC,
#endif
	CreateEnhMetaFileA_FUNC,
	CreateEnhMetaFileW_FUNC,
#ifndef JNI64
	CreateFontIndirectA__I_FUNC,
#else
	CreateFontIndirectA__J_FUNC,
#endif
	CreateFontIndirectA__Lorg_eclipse_swt_internal_win32_LOGFONTA_2_FUNC,
#ifndef JNI64
	CreateFontIndirectW__I_FUNC,
#else
	CreateFontIndirectW__J_FUNC,
#endif
	CreateFontIndirectW__Lorg_eclipse_swt_internal_win32_LOGFONTW_2_FUNC,
	CreateIconIndirect_FUNC,
	CreateMenu_FUNC,
	CreatePalette_FUNC,
	CreatePatternBrush_FUNC,
	CreatePen_FUNC,
	CreatePolygonRgn_FUNC,
	CreatePopupMenu_FUNC,
	CreateProcessA_FUNC,
	CreateProcessW_FUNC,
	CreateRectRgn_FUNC,
	CreateSolidBrush_FUNC,
	CreateStreamOnHGlobal_FUNC,
	CreateWindowExA_FUNC,
	CreateWindowExW_FUNC,
	DEVMODEA_1sizeof_FUNC,
	DEVMODEW_1sizeof_FUNC,
	DIBSECTION_1sizeof_FUNC,
	DLLVERSIONINFO_1sizeof_FUNC,
	DOCHOSTUIINFO_1sizeof_FUNC,
	DOCINFO_1sizeof_FUNC,
	DPtoLP_FUNC,
	DRAWITEMSTRUCT_1sizeof_FUNC,
	DROPFILES_1sizeof_FUNC,
	DTTOPTS_1sizeof_FUNC,
	DWM_1BLURBEHIND_1sizeof_FUNC,
	DefFrameProcA_FUNC,
	DefFrameProcW_FUNC,
	DefMDIChildProcA_FUNC,
	DefMDIChildProcW_FUNC,
	DefWindowProcA_FUNC,
	DefWindowProcW_FUNC,
	DeferWindowPos_FUNC,
	DeleteDC_FUNC,
	DeleteEnhMetaFile_FUNC,
	DeleteMenu_FUNC,
	DeleteObject_FUNC,
	DestroyAcceleratorTable_FUNC,
	DestroyCaret_FUNC,
	DestroyCursor_FUNC,
	DestroyIcon_FUNC,
	DestroyMenu_FUNC,
	DestroyWindow_FUNC,
	DispatchMessageA_FUNC,
	DispatchMessageW_FUNC,
	DocumentPropertiesA_FUNC,
	DocumentPropertiesW_FUNC,
	DragDetect_FUNC,
	DragFinish_FUNC,
	DragQueryFileA_FUNC,
	DragQueryFileW_FUNC,
	DrawAnimatedRects_FUNC,
	DrawEdge_FUNC,
	DrawFocusRect_FUNC,
	DrawFrameControl_FUNC,
	DrawIconEx_FUNC,
	DrawMenuBar_FUNC,
	DrawStateA_FUNC,
	DrawStateW_FUNC,
	DrawTextA_FUNC,
	DrawTextW_FUNC,
	DrawThemeBackground_FUNC,
	DrawThemeEdge_FUNC,
	DrawThemeIcon_FUNC,
	DrawThemeParentBackground_FUNC,
	DrawThemeText_FUNC,
	DrawThemeTextEx_FUNC,
	DuplicateHandle_FUNC,
	DwmEnableBlurBehindWindow_FUNC,
	DwmExtendFrameIntoClientArea_FUNC,
	DwmIsCompositionEnabled_FUNC,
	EMREXTCREATEFONTINDIRECTW_1sizeof_FUNC,
	EMR_1sizeof_FUNC,
	EXTLOGFONTW_1sizeof_FUNC,
	EXTLOGPEN_1sizeof_FUNC,
	Ellipse_FUNC,
	EnableMenuItem_FUNC,
	EnableScrollBar_FUNC,
	EnableWindow_FUNC,
	EndBufferedPaint_FUNC,
	EndDeferWindowPos_FUNC,
	EndDoc_FUNC,
	EndPage_FUNC,
	EndPaint_FUNC,
	EndPath_FUNC,
	EnumDisplayMonitors_FUNC,
	EnumEnhMetaFile_FUNC,
	EnumFontFamiliesA_FUNC,
	EnumFontFamiliesExA_FUNC,
	EnumFontFamiliesExW_FUNC,
	EnumFontFamiliesW_FUNC,
	EnumSystemLanguageGroupsA_FUNC,
	EnumSystemLanguageGroupsW_FUNC,
	EnumSystemLocalesA_FUNC,
	EnumSystemLocalesW_FUNC,
	EqualRect_FUNC,
	EqualRgn_FUNC,
	ExcludeClipRect_FUNC,
	ExpandEnvironmentStringsA_FUNC,
	ExpandEnvironmentStringsW_FUNC,
	ExtCreatePen_FUNC,
	ExtCreateRegion_FUNC,
	ExtTextOutA_FUNC,
	ExtTextOutW_FUNC,
	ExtractIconExA_FUNC,
	ExtractIconExW_FUNC,
	FILETIME_1sizeof_FUNC,
	FLICK_1DATA_1sizeof_FUNC,
	FLICK_1POINT_1sizeof_FUNC,
	FileTimeToSystemTime_FUNC,
	FillPath_FUNC,
	FillRect_FUNC,
	FindWindowA_FUNC,
	FindWindowW_FUNC,
	FormatMessageA_FUNC,
	FormatMessageW_FUNC,
	FreeLibrary_FUNC,
	GCP_1RESULTS_1sizeof_FUNC,
	GESTURECONFIG_1sizeof_FUNC,
	GESTUREINFO_1sizeof_FUNC,
	GET_1WHEEL_1DELTA_1WPARAM_FUNC,
	GET_1X_1LPARAM_FUNC,
	GET_1Y_1LPARAM_FUNC,
	GID_1ROTATE_1ANGLE_1FROM_1ARGUMENT_FUNC,
	GRADIENT_1RECT_1sizeof_FUNC,
	GUITHREADINFO_1sizeof_FUNC,
	GdiSetBatchLimit_FUNC,
	GetACP_FUNC,
	GetActiveWindow_FUNC,
	GetAsyncKeyState_FUNC,
	GetBkColor_FUNC,
	GetCapture_FUNC,
	GetCaretPos_FUNC,
	GetCharABCWidthsA_FUNC,
	GetCharABCWidthsW_FUNC,
	GetCharWidthA_FUNC,
	GetCharWidthW_FUNC,
	GetCharacterPlacementA_FUNC,
	GetCharacterPlacementW_FUNC,
	GetClassInfoA_FUNC,
	GetClassInfoW_FUNC,
	GetClassNameA_FUNC,
	GetClassNameW_FUNC,
	GetClientRect_FUNC,
	GetClipBox_FUNC,
	GetClipRgn_FUNC,
	GetClipboardData_FUNC,
	GetClipboardFormatNameA_FUNC,
	GetClipboardFormatNameW_FUNC,
	GetComboBoxInfo_FUNC,
	GetCurrentObject_FUNC,
	GetCurrentProcess_FUNC,
	GetCurrentProcessExplicitAppUserModelID_FUNC,
	GetCurrentProcessId_FUNC,
	GetCurrentThreadId_FUNC,
	GetCursor_FUNC,
	GetCursorPos_FUNC,
	GetDC_FUNC,
	GetDCEx_FUNC,
	GetDIBColorTable_FUNC,
	GetDIBits_FUNC,
	GetDateFormatA_FUNC,
	GetDateFormatW_FUNC,
	GetDesktopWindow_FUNC,
	GetDeviceCaps_FUNC,
	GetDialogBaseUnits_FUNC,
	GetDlgItem_FUNC,
	GetDoubleClickTime_FUNC,
	GetFocus_FUNC,
	GetFontLanguageInfo_FUNC,
	GetForegroundWindow_FUNC,
	GetGUIThreadInfo_FUNC,
	GetGestureInfo_FUNC,
	GetGlyphIndicesW_FUNC,
	GetGraphicsMode_FUNC,
	GetIconInfo_FUNC,
	GetKeyNameTextA_FUNC,
	GetKeyNameTextW_FUNC,
	GetKeyState_FUNC,
	GetKeyboardLayout_FUNC,
	GetKeyboardLayoutList_FUNC,
	GetKeyboardState_FUNC,
	GetLastActivePopup_FUNC,
	GetLastError_FUNC,
	GetLayeredWindowAttributes_FUNC,
	GetLayout_FUNC,
	GetLibraryHandle_FUNC,
	GetLocaleInfoA_FUNC,
	GetLocaleInfoW_FUNC,
	GetMapMode_FUNC,
	GetMenu_FUNC,
	GetMenuBarInfo_FUNC,
	GetMenuDefaultItem_FUNC,
	GetMenuInfo_FUNC,
	GetMenuItemCount_FUNC,
	GetMenuItemInfoA_FUNC,
	GetMenuItemInfoW_FUNC,
	GetMenuItemRect_FUNC,
	GetMessageA_FUNC,
	GetMessagePos_FUNC,
	GetMessageTime_FUNC,
	GetMessageW_FUNC,
	GetMetaRgn_FUNC,
	GetModuleFileNameA_FUNC,
	GetModuleFileNameW_FUNC,
	GetModuleHandleA_FUNC,
	GetModuleHandleW_FUNC,
	GetMonitorInfoA_FUNC,
	GetMonitorInfoW_FUNC,
	GetNearestPaletteIndex_FUNC,
#ifndef JNI64
	GetObjectA__III_FUNC,
#else
	GetObjectA__JIJ_FUNC,
#endif
#ifndef JNI64
	GetObjectA__IILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC,
#else
	GetObjectA__JILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC,
#endif
#ifndef JNI64
	GetObjectA__IILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC,
#else
	GetObjectA__JILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC,
#endif
#ifndef JNI64
	GetObjectA__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC,
#else
	GetObjectA__JILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC,
#endif
#ifndef JNI64
	GetObjectA__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC,
#else
	GetObjectA__JILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC,
#endif
#ifndef JNI64
	GetObjectA__IILorg_eclipse_swt_internal_win32_LOGFONTA_2_FUNC,
#else
	GetObjectA__JILorg_eclipse_swt_internal_win32_LOGFONTA_2_FUNC,
#endif
#ifndef JNI64
	GetObjectA__IILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC,
#else
	GetObjectA__JILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC,
#endif
#ifndef JNI64
	GetObjectW__III_FUNC,
#else
	GetObjectW__JIJ_FUNC,
#endif
#ifndef JNI64
	GetObjectW__IILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC,
#else
	GetObjectW__JILorg_eclipse_swt_internal_win32_BITMAP_2_FUNC,
#endif
#ifndef JNI64
	GetObjectW__IILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC,
#else
	GetObjectW__JILorg_eclipse_swt_internal_win32_DIBSECTION_2_FUNC,
#endif
#ifndef JNI64
	GetObjectW__IILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC,
#else
	GetObjectW__JILorg_eclipse_swt_internal_win32_EXTLOGPEN_2_FUNC,
#endif
#ifndef JNI64
	GetObjectW__IILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC,
#else
	GetObjectW__JILorg_eclipse_swt_internal_win32_LOGBRUSH_2_FUNC,
#endif
#ifndef JNI64
	GetObjectW__IILorg_eclipse_swt_internal_win32_LOGFONTW_2_FUNC,
#else
	GetObjectW__JILorg_eclipse_swt_internal_win32_LOGFONTW_2_FUNC,
#endif
#ifndef JNI64
	GetObjectW__IILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC,
#else
	GetObjectW__JILorg_eclipse_swt_internal_win32_LOGPEN_2_FUNC,
#endif
	GetOpenFileNameA_FUNC,
	GetOpenFileNameW_FUNC,
	GetOutlineTextMetricsA_FUNC,
	GetOutlineTextMetricsW_FUNC,
	GetPaletteEntries_FUNC,
	GetParent_FUNC,
	GetPath_FUNC,
	GetPixel_FUNC,
	GetPolyFillMode_FUNC,
	GetProcAddress_FUNC,
	GetProcessHeap_FUNC,
	GetProcessHeaps_FUNC,
	GetProfileStringA_FUNC,
	GetProfileStringW_FUNC,
	GetPropA_FUNC,
	GetPropW_FUNC,
	GetROP2_FUNC,
	GetRandomRgn_FUNC,
	GetRegionData_FUNC,
	GetRgnBox_FUNC,
	GetSaveFileNameA_FUNC,
	GetSaveFileNameW_FUNC,
	GetScrollBarInfo_FUNC,
	GetScrollInfo_FUNC,
	GetStartupInfoA_FUNC,
	GetStartupInfoW_FUNC,
	GetStockObject_FUNC,
	GetSysColor_FUNC,
	GetSysColorBrush_FUNC,
	GetSystemDefaultUILanguage_FUNC,
	GetSystemMenu_FUNC,
	GetSystemMetrics_FUNC,
	GetSystemPaletteEntries_FUNC,
	GetTextCharset_FUNC,
	GetTextColor_FUNC,
	GetTextExtentPoint32A_FUNC,
	GetTextExtentPoint32W_FUNC,
	GetTextMetricsA_FUNC,
	GetTextMetricsW_FUNC,
	GetThemeBackgroundContentRect_FUNC,
	GetThemeBackgroundExtent_FUNC,
	GetThemeBitmap_FUNC,
	GetThemeColor_FUNC,
	GetThemeInt_FUNC,
	GetThemeMargins_FUNC,
	GetThemeMetric_FUNC,
	GetThemePartSize_FUNC,
	GetThemeRect_FUNC,
	GetThemeSysSize_FUNC,
	GetThemeTextExtent_FUNC,
	GetTickCount_FUNC,
	GetTimeFormatA_FUNC,
	GetTimeFormatW_FUNC,
	GetTouchInputInfo_FUNC,
	GetUpdateRect_FUNC,
	GetUpdateRgn_FUNC,
	GetVersionExA__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOA_2_FUNC,
	GetVersionExA__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOEXA_2_FUNC,
	GetVersionExW__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOEXW_2_FUNC,
	GetVersionExW__Lorg_eclipse_swt_internal_win32_OSVERSIONINFOW_2_FUNC,
	GetWindow_FUNC,
	GetWindowDC_FUNC,
	GetWindowLongA_FUNC,
	GetWindowLongPtrA_FUNC,
	GetWindowLongPtrW_FUNC,
	GetWindowLongW_FUNC,
	GetWindowOrgEx_FUNC,
	GetWindowPlacement_FUNC,
	GetWindowRect_FUNC,
	GetWindowRgn_FUNC,
	GetWindowTextA_FUNC,
	GetWindowTextLengthA_FUNC,
	GetWindowTextLengthW_FUNC,
	GetWindowTextW_FUNC,
	GetWindowTheme_FUNC,
	GetWindowThreadProcessId_FUNC,
	GetWorldTransform_FUNC,
	GlobalAddAtomA_FUNC,
	GlobalAddAtomW_FUNC,
	GlobalAlloc_FUNC,
	GlobalFree_FUNC,
	GlobalLock_FUNC,
	GlobalSize_FUNC,
	GlobalUnlock_FUNC,
	GradientFill_FUNC,
	HDHITTESTINFO_1sizeof_FUNC,
	HDITEM_1sizeof_FUNC,
	HDLAYOUT_1sizeof_FUNC,
	HELPINFO_1sizeof_FUNC,
	HIGHCONTRAST_1sizeof_FUNC,
	HIWORD_FUNC,
	HeapAlloc_FUNC,
	HeapFree_FUNC,
	HeapValidate_FUNC,
	HideCaret_FUNC,
	HitTestThemeBackground_FUNC,
	ICONINFO_1sizeof_FUNC,
	IIDFromString_FUNC,
	INITCOMMONCONTROLSEX_1sizeof_FUNC,
	INPUT_1sizeof_FUNC,
	ImageList_1Add_FUNC,
	ImageList_1AddMasked_FUNC,
	ImageList_1BeginDrag_FUNC,
	ImageList_1Create_FUNC,
	ImageList_1Destroy_FUNC,
	ImageList_1DragEnter_FUNC,
	ImageList_1DragLeave_FUNC,
	ImageList_1DragMove_FUNC,
	ImageList_1DragShowNolock_FUNC,
	ImageList_1Draw_FUNC,
	ImageList_1EndDrag_FUNC,
	ImageList_1GetDragImage_FUNC,
	ImageList_1GetIcon_FUNC,
	ImageList_1GetIconSize_FUNC,
	ImageList_1GetImageCount_FUNC,
	ImageList_1Remove_FUNC,
	ImageList_1Replace_FUNC,
	ImageList_1ReplaceIcon_FUNC,
	ImageList_1SetIconSize_FUNC,
	ImmAssociateContext_FUNC,
	ImmCreateContext_FUNC,
	ImmDestroyContext_FUNC,
	ImmDisableTextFrameService_FUNC,
	ImmGetCompositionFontA_FUNC,
	ImmGetCompositionFontW_FUNC,
#ifndef JNI64
	ImmGetCompositionStringA__II_3BI_FUNC,
#else
	ImmGetCompositionStringA__JI_3BI_FUNC,
#endif
#ifndef JNI64
	ImmGetCompositionStringA__II_3II_FUNC,
#else
	ImmGetCompositionStringA__JI_3II_FUNC,
#endif
#ifndef JNI64
	ImmGetCompositionStringW__II_3BI_FUNC,
#else
	ImmGetCompositionStringW__JI_3BI_FUNC,
#endif
#ifndef JNI64
	ImmGetCompositionStringW__II_3CI_FUNC,
#else
	ImmGetCompositionStringW__JI_3CI_FUNC,
#endif
#ifndef JNI64
	ImmGetCompositionStringW__II_3II_FUNC,
#else
	ImmGetCompositionStringW__JI_3II_FUNC,
#endif
	ImmGetContext_FUNC,
	ImmGetConversionStatus_FUNC,
	ImmGetDefaultIMEWnd_FUNC,
	ImmGetOpenStatus_FUNC,
	ImmNotifyIME_FUNC,
	ImmReleaseContext_FUNC,
	ImmSetCandidateWindow_FUNC,
	ImmSetCompositionFontA_FUNC,
	ImmSetCompositionFontW_FUNC,
	ImmSetCompositionWindow_FUNC,
	ImmSetConversionStatus_FUNC,
	ImmSetOpenStatus_FUNC,
	InSendMessage_FUNC,
	InitCommonControls_FUNC,
	InitCommonControlsEx_FUNC,
	InsertMenuA_FUNC,
	InsertMenuItemA_FUNC,
	InsertMenuItemW_FUNC,
	InsertMenuW_FUNC,
	InternetGetCookieA_FUNC,
	InternetGetCookieW_FUNC,
	InternetSetCookieA_FUNC,
	InternetSetCookieW_FUNC,
	InternetSetOption_FUNC,
	IntersectClipRect_FUNC,
	IntersectRect_FUNC,
	InvalidateRect_FUNC,
	InvalidateRgn_FUNC,
	IsAppThemed_FUNC,
	IsBadReadPtr_FUNC,
	IsBadWritePtr_FUNC,
	IsDBCSLeadByte_FUNC,
	IsHungAppWindow_FUNC,
	IsIconic_FUNC,
	IsPPC_FUNC,
	IsSP_FUNC,
	IsTouchWindow_FUNC,
	IsWindowEnabled_FUNC,
	IsWindowVisible_FUNC,
	IsZoomed_FUNC,
	KEYBDINPUT_1sizeof_FUNC,
	KillTimer_FUNC,
	LITEM_1sizeof_FUNC,
	LODWORD_FUNC,
	LOGBRUSH_1sizeof_FUNC,
	LOGFONTA_1sizeof_FUNC,
	LOGFONTW_1sizeof_FUNC,
	LOGPEN_1sizeof_FUNC,
	LOWORD_FUNC,
	LPtoDP_FUNC,
	LVCOLUMN_1sizeof_FUNC,
	LVHITTESTINFO_1sizeof_FUNC,
	LVINSERTMARK_1sizeof_FUNC,
	LVITEM_1sizeof_FUNC,
	LineTo_FUNC,
	LoadBitmapA_FUNC,
	LoadBitmapW_FUNC,
	LoadCursorA_FUNC,
	LoadCursorW_FUNC,
	LoadIconA_FUNC,
	LoadIconW_FUNC,
#ifndef JNI64
	LoadImageA__IIIIII_FUNC,
#else
	LoadImageA__JJIIII_FUNC,
#endif
#ifndef JNI64
	LoadImageA__I_3BIIII_FUNC,
#else
	LoadImageA__J_3BIIII_FUNC,
#endif
#ifndef JNI64
	LoadImageW__IIIIII_FUNC,
#else
	LoadImageW__JJIIII_FUNC,
#endif
#ifndef JNI64
	LoadImageW__I_3CIIII_FUNC,
#else
	LoadImageW__J_3CIIII_FUNC,
#endif
	LoadLibraryA_FUNC,
	LoadLibraryW_FUNC,
	LoadStringA_FUNC,
	LoadStringW_FUNC,
	LocalFree_FUNC,
	LockWindowUpdate_FUNC,
	MAKELPARAM_FUNC,
	MAKELRESULT_FUNC,
	MAKEWORD_FUNC,
	MAKEWPARAM_FUNC,
	MARGINS_1sizeof_FUNC,
	MCHITTESTINFO_1sizeof_FUNC,
	MCIWndRegisterClass_FUNC,
	MEASUREITEMSTRUCT_1sizeof_FUNC,
	MENUBARINFO_1sizeof_FUNC,
	MENUINFO_1sizeof_FUNC,
	MENUITEMINFO_1sizeof_FUNC,
	MINMAXINFO_1sizeof_FUNC,
	MONITORINFO_1sizeof_FUNC,
	MOUSEINPUT_1sizeof_FUNC,
	MSG_1sizeof_FUNC,
	MapViewOfFile_FUNC,
	MapVirtualKeyA_FUNC,
	MapVirtualKeyW_FUNC,
#ifndef JNI64
	MapWindowPoints__IILorg_eclipse_swt_internal_win32_POINT_2I_FUNC,
#else
	MapWindowPoints__JJLorg_eclipse_swt_internal_win32_POINT_2I_FUNC,
#endif
#ifndef JNI64
	MapWindowPoints__IILorg_eclipse_swt_internal_win32_RECT_2I_FUNC,
#else
	MapWindowPoints__JJLorg_eclipse_swt_internal_win32_RECT_2I_FUNC,
#endif
	MessageBeep_FUNC,
	MessageBoxA_FUNC,
	MessageBoxW_FUNC,
	ModifyWorldTransform_FUNC,
	MonitorFromWindow_FUNC,
#ifndef JNI64
	MoveMemory__III_FUNC,
#else
	MoveMemory__JJI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_DEVMODEA_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_DEVMODEA_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_DEVMODEW_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_DEVMODEW_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_DROPFILES_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_DROPFILES_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_GESTURECONFIG_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_GESTURECONFIG_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_GRADIENT_1RECT_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_KEYBDINPUT_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_KEYBDINPUT_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTA_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_LOGFONTA_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_LOGFONTW_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_LOGFONTW_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_MINMAXINFO_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_MINMAXINFO_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_MOUSEINPUT_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_MOUSEINPUT_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_MSG_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_MSG_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_NMLVDISPINFO_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_NMTVDISPINFO_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_OPENFILENAME_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_OPENFILENAME_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_RECT_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_RECT_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_SAFEARRAY_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_SAFEARRAY_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_TRIVERTEX_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_TRIVERTEX_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_UDACCEL_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_UDACCEL_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_win32_WINDOWPOS_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_win32_WINDOWPOS_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__I_3BI_FUNC,
#else
	MoveMemory__J_3BI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__I_3CI_FUNC,
#else
	MoveMemory__J_3CI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__I_3DI_FUNC,
#else
	MoveMemory__J_3DI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__I_3FI_FUNC,
#else
	MoveMemory__J_3FI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__I_3II_FUNC,
#else
	MoveMemory__J_3II_FUNC,
#endif
#ifndef JNI64
	MoveMemory__I_3JI_FUNC,
#else
	MoveMemory__J_3JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__I_3SI_FUNC,
#else
	MoveMemory__J_3SI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2JI_FUNC,
#endif
	MoveMemory__Lorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2_3BI_FUNC,
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_CERT_1CONTEXT_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_CERT_1CONTEXT_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_CERT_1INFO_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_CERT_1INFO_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEA_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEA_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEW_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_DEVMODEW_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_DOCHOSTUIINFO_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_DRAWITEMSTRUCT_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_EMREXTCREATEFONTINDIRECTW_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_EMREXTCREATEFONTINDIRECTW_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_EMR_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_EMR_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_EXTLOGPEN_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_FLICK_1DATA_2_3II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_FLICK_1DATA_2_3JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_FLICK_1POINT_2_3II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_FLICK_1POINT_2_3JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_HDITEM_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_HELPINFO_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTA_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_LOGFONTW_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_MEASUREITEMSTRUCT_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_MINMAXINFO_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_MSG_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMCUSTOMDRAW_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMHDR_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMHEADER_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMLINK_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMLISTVIEW_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVCUSTOMDRAW_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVDISPINFO_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVFINDITEM_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVODSTATECHANGE_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMLVODSTATECHANGE_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHEVRON_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMREBARCHILDSIZE_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMRGINFO_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTBHOTITEM_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTOOLBAR_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTREEVIEW_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTREEVIEW_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTCUSTOMDRAW_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOA_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTTDISPINFOW_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVCUSTOMDRAW_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVDISPINFO_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVITEMCHANGE_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMTVITEMCHANGE_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_NMUPDOWN_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_OFNOTIFY_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_OFNOTIFY_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_OPENFILENAME_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_OPENFILENAME_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2JI_FUNC,
#endif
	MoveMemory__Lorg_eclipse_swt_internal_win32_POINT_2_3JI_FUNC,
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2_3II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2_3JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_SAFEARRAY_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_SAFEARRAY_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1ITEM_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1LOGATTR_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_SCRIPT_1PROPERTIES_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICA_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_TEXTMETRICW_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_TOUCHINPUT_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_TOUCHINPUT_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_TVITEM_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_UDACCEL_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_WINDOWPOS_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory___3BII_FUNC,
#else
	MoveMemory___3BJI_FUNC,
#endif
	MoveMemory___3BLorg_eclipse_swt_internal_win32_ACCEL_2I_FUNC,
	MoveMemory___3BLorg_eclipse_swt_internal_win32_BITMAPINFOHEADER_2I_FUNC,
#ifndef JNI64
	MoveMemory___3CII_FUNC,
#else
	MoveMemory___3CJI_FUNC,
#endif
#ifndef JNI64
	MoveMemory___3DII_FUNC,
#else
	MoveMemory___3DJI_FUNC,
#endif
#ifndef JNI64
	MoveMemory___3FII_FUNC,
#else
	MoveMemory___3FJI_FUNC,
#endif
#ifndef JNI64
	MoveMemory___3III_FUNC,
#else
	MoveMemory___3IJI_FUNC,
#endif
#ifndef JNI64
	MoveMemory___3JII_FUNC,
#else
	MoveMemory___3JJI_FUNC,
#endif
#ifndef JNI64
	MoveMemory___3SII_FUNC,
#else
	MoveMemory___3SJI_FUNC,
#endif
	MoveToEx_FUNC,
	MsgWaitForMultipleObjectsEx_FUNC,
#ifndef JNI64
	MultiByteToWideChar__IIII_3CI_FUNC,
#else
	MultiByteToWideChar__IIJI_3CI_FUNC,
#endif
	MultiByteToWideChar__II_3BI_3CI_FUNC,
	NMCUSTOMDRAW_1sizeof_FUNC,
	NMHDR_1sizeof_FUNC,
	NMHEADER_1sizeof_FUNC,
	NMLINK_1sizeof_FUNC,
	NMLISTVIEW_1sizeof_FUNC,
	NMLVCUSTOMDRAW_1sizeof_FUNC,
	NMLVDISPINFO_1sizeof_FUNC,
	NMLVFINDITEM_1sizeof_FUNC,
	NMLVODSTATECHANGE_1sizeof_FUNC,
	NMREBARCHEVRON_1sizeof_FUNC,
	NMREBARCHILDSIZE_1sizeof_FUNC,
	NMRGINFO_1sizeof_FUNC,
	NMTBHOTITEM_1sizeof_FUNC,
	NMTOOLBAR_1sizeof_FUNC,
	NMTREEVIEW_1sizeof_FUNC,
	NMTTCUSTOMDRAW_1sizeof_FUNC,
	NMTTDISPINFOA_1sizeof_FUNC,
	NMTTDISPINFOW_1sizeof_FUNC,
	NMTVCUSTOMDRAW_1sizeof_FUNC,
	NMTVDISPINFO_1sizeof_FUNC,
	NMTVITEMCHANGE_1sizeof_FUNC,
	NMUPDOWN_1sizeof_FUNC,
	NONCLIENTMETRICSA_1sizeof_FUNC,
	NONCLIENTMETRICSW_1sizeof_FUNC,
	NOTIFYICONDATAA_1V2_1SIZE_FUNC,
	NOTIFYICONDATAW_1V2_1SIZE_FUNC,
	NotifyWinEvent_FUNC,
	OFNOTIFY_1sizeof_FUNC,
	OPENFILENAME_1sizeof_FUNC,
	OSVERSIONINFOA_1sizeof_FUNC,
	OSVERSIONINFOEXA_1sizeof_FUNC,
	OSVERSIONINFOEXW_1sizeof_FUNC,
	OSVERSIONINFOW_1sizeof_FUNC,
	OUTLINETEXTMETRICA_1sizeof_FUNC,
	OUTLINETEXTMETRICW_1sizeof_FUNC,
	OffsetRect_FUNC,
	OffsetRgn_FUNC,
	OleInitialize_FUNC,
	OleUninitialize_FUNC,
	OpenClipboard_FUNC,
	OpenPrinterA_FUNC,
	OpenPrinterW_FUNC,
	OpenProcess_FUNC,
	OpenThemeData_FUNC,
	PAINTSTRUCT_1sizeof_FUNC,
	PANOSE_1sizeof_FUNC,
	POINTSTOPOINT_FUNC,
	POINT_1sizeof_FUNC,
	PRIMARYLANGID_FUNC,
	PRINTDLG_1sizeof_FUNC,
	PROCESS_1INFORMATION_1sizeof_FUNC,
	PROPERTYKEY_1sizeof_FUNC,
	PROPVARIANT_1sizeof_FUNC,
	PSPropertyKeyFromString_FUNC,
	PatBlt_FUNC,
	PathIsExe_FUNC,
	PeekMessageA_FUNC,
	PeekMessageW_FUNC,
	Pie_FUNC,
	Polygon_FUNC,
	Polyline_FUNC,
	PostMessageA_FUNC,
	PostMessageW_FUNC,
	PostThreadMessageA_FUNC,
	PostThreadMessageW_FUNC,
	PrintDlgA_FUNC,
	PrintDlgW_FUNC,
	PrintWindow_FUNC,
	PtInRect_FUNC,
	PtInRegion_FUNC,
	REBARBANDINFO_1sizeof_FUNC,
	RECT_1sizeof_FUNC,
	RealizePalette_FUNC,
	RectInRegion_FUNC,
	Rectangle_FUNC,
	RedrawWindow_FUNC,
	RegCloseKey_FUNC,
	RegCreateKeyExA_FUNC,
	RegCreateKeyExW_FUNC,
	RegDeleteValueA_FUNC,
	RegDeleteValueW_FUNC,
	RegEnumKeyExA_FUNC,
	RegEnumKeyExW_FUNC,
	RegOpenKeyExA_FUNC,
	RegOpenKeyExW_FUNC,
	RegQueryInfoKeyA_FUNC,
	RegQueryInfoKeyW_FUNC,
#ifndef JNI64
	RegQueryValueExA__I_3BI_3I_3B_3I_FUNC,
#else
	RegQueryValueExA__J_3BJ_3I_3B_3I_FUNC,
#endif
#ifndef JNI64
	RegQueryValueExA__I_3BI_3I_3I_3I_FUNC,
#else
	RegQueryValueExA__J_3BJ_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	RegQueryValueExW__I_3CI_3I_3C_3I_FUNC,
#else
	RegQueryValueExW__J_3CJ_3I_3C_3I_FUNC,
#endif
#ifndef JNI64
	RegQueryValueExW__I_3CI_3I_3I_3I_FUNC,
#else
	RegQueryValueExW__J_3CJ_3I_3I_3I_FUNC,
#endif
	RegSetValueExA_FUNC,
	RegSetValueExW_FUNC,
	RegisterClassA_FUNC,
	RegisterClassW_FUNC,
	RegisterClipboardFormatA_FUNC,
	RegisterClipboardFormatW_FUNC,
	RegisterTouchWindow_FUNC,
	RegisterWindowMessageA_FUNC,
	RegisterWindowMessageW_FUNC,
	ReleaseCapture_FUNC,
	ReleaseDC_FUNC,
	RemoveMenu_FUNC,
	RemovePropA_FUNC,
	RemovePropW_FUNC,
	ReplyMessage_FUNC,
	RestoreDC_FUNC,
	RoundRect_FUNC,
	SAFEARRAYBOUND_1sizeof_FUNC,
	SAFEARRAY_1sizeof_FUNC,
	SCRIPT_1ANALYSIS_1sizeof_FUNC,
	SCRIPT_1CONTROL_1sizeof_FUNC,
	SCRIPT_1DIGITSUBSTITUTE_1sizeof_FUNC,
	SCRIPT_1FONTPROPERTIES_1sizeof_FUNC,
	SCRIPT_1ITEM_1sizeof_FUNC,
	SCRIPT_1LOGATTR_1sizeof_FUNC,
	SCRIPT_1PROPERTIES_1sizeof_FUNC,
	SCRIPT_1STATE_1sizeof_FUNC,
	SCRIPT_1STRING_1ANALYSIS_1sizeof_FUNC,
	SCROLLBARINFO_1sizeof_FUNC,
	SCROLLINFO_1sizeof_FUNC,
	SHACTIVATEINFO_1sizeof_FUNC,
	SHBrowseForFolderA_FUNC,
	SHBrowseForFolderW_FUNC,
	SHCreateItemFromRelativeName_FUNC,
	SHCreateItemInKnownFolder_FUNC,
	SHCreateMenuBar_FUNC,
	SHDRAGIMAGE_1sizeof_FUNC,
	SHELLEXECUTEINFO_1sizeof_FUNC,
	SHFILEINFOA_1sizeof_FUNC,
	SHFILEINFOW_1sizeof_FUNC,
	SHGetFileInfoA_FUNC,
	SHGetFileInfoW_FUNC,
	SHGetFolderPathA_FUNC,
	SHGetFolderPathW_FUNC,
	SHGetMalloc_FUNC,
	SHGetPathFromIDListA_FUNC,
	SHGetPathFromIDListW_FUNC,
	SHHandleWMSettingChange_FUNC,
	SHMENUBARINFO_1sizeof_FUNC,
	SHRGINFO_1sizeof_FUNC,
	SHRecognizeGesture_FUNC,
	SHSendBackToFocusWindow_FUNC,
	SHSetAppKeyWndAssoc_FUNC,
	SHSipPreference_FUNC,
	SIPINFO_1sizeof_FUNC,
	SIZE_1sizeof_FUNC,
	STARTUPINFO_1sizeof_FUNC,
	SYSTEMTIME_1sizeof_FUNC,
	SaveDC_FUNC,
	ScreenToClient_FUNC,
	ScriptApplyDigitSubstitution_FUNC,
	ScriptBreak_FUNC,
	ScriptCPtoX_FUNC,
	ScriptCacheGetHeight_FUNC,
	ScriptFreeCache_FUNC,
	ScriptGetCMap_FUNC,
	ScriptGetFontProperties_FUNC,
	ScriptGetLogicalWidths_FUNC,
	ScriptGetProperties_FUNC,
	ScriptItemize_FUNC,
	ScriptJustify_FUNC,
	ScriptLayout_FUNC,
	ScriptPlace_FUNC,
	ScriptRecordDigitSubstitution_FUNC,
	ScriptShape_FUNC,
	ScriptStringAnalyse_FUNC,
	ScriptStringFree_FUNC,
	ScriptStringOut_FUNC,
	ScriptTextOut_FUNC,
	ScriptXtoCP_FUNC,
	ScrollWindowEx_FUNC,
	SelectClipRgn_FUNC,
	SelectObject_FUNC,
	SelectPalette_FUNC,
	SendInput_FUNC,
#ifndef JNI64
	SendMessageA__IIII_FUNC,
#else
	SendMessageA__JIJJ_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_HDHITTESTINFO_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDHITTESTINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_HDITEM_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDITEM_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_LITEM_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_LITEM_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_LVINSERTMARK_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVINSERTMARK_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_LVITEM_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_LVITEM_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_MARGINS_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_MARGINS_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_MCHITTESTINFO_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_MCHITTESTINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_POINT_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_POINT_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_RECT_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_SIZE_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_SYSTEMTIME_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_SYSTEMTIME_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_TCHITTESTINFO_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_TCHITTESTINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_TCITEM_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_TCITEM_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_TVITEM_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVITEM_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_TVSORTCB_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_TVSORTCB_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__IIILorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC,
#else
	SendMessageA__JIJLorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC,
#endif
#ifndef JNI64
	SendMessageA__III_3B_FUNC,
#else
	SendMessageA__JIJ_3B_FUNC,
#endif
#ifndef JNI64
	SendMessageA__III_3C_FUNC,
#else
	SendMessageA__JIJ_3C_FUNC,
#endif
#ifndef JNI64
	SendMessageA__III_3I_FUNC,
#else
	SendMessageA__JIJ_3I_FUNC,
#endif
#ifndef JNI64
	SendMessageA__III_3S_FUNC,
#else
	SendMessageA__JIJ_3S_FUNC,
#endif
#ifndef JNI64
	SendMessageA__II_3II_FUNC,
#else
	SendMessageA__JI_3JJ_FUNC,
#endif
#ifndef JNI64
	SendMessageA__II_3I_3I_FUNC,
#else
	SendMessageA__JI_3I_3I_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIII_FUNC,
#else
	SendMessageW__JIJJ_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_BUTTON_1IMAGELIST_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_HDHITTESTINFO_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDHITTESTINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_HDITEM_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDITEM_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_HDLAYOUT_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_LITEM_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_LITEM_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVCOLUMN_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVHITTESTINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_LVINSERTMARK_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVINSERTMARK_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_LVITEM_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_LVITEM_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_MARGINS_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_MARGINS_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_MCHITTESTINFO_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_MCHITTESTINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_POINT_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_POINT_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_REBARBANDINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_RECT_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_SIZE_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_SYSTEMTIME_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_SYSTEMTIME_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_TBBUTTONINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_TBBUTTON_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_TCHITTESTINFO_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_TCHITTESTINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_TCITEM_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_TCITEM_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_TOOLINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVHITTESTINFO_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVINSERTSTRUCT_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_TVITEM_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVITEM_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_TVSORTCB_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_TVSORTCB_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__IIILorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC,
#else
	SendMessageW__JIJLorg_eclipse_swt_internal_win32_UDACCEL_2_FUNC,
#endif
#ifndef JNI64
	SendMessageW__III_3C_FUNC,
#else
	SendMessageW__JIJ_3C_FUNC,
#endif
#ifndef JNI64
	SendMessageW__III_3I_FUNC,
#else
	SendMessageW__JIJ_3I_FUNC,
#endif
#ifndef JNI64
	SendMessageW__III_3S_FUNC,
#else
	SendMessageW__JIJ_3S_FUNC,
#endif
#ifndef JNI64
	SendMessageW__II_3II_FUNC,
#else
	SendMessageW__JI_3JJ_FUNC,
#endif
#ifndef JNI64
	SendMessageW__II_3I_3I_FUNC,
#else
	SendMessageW__JI_3I_3I_FUNC,
#endif
	SetActiveWindow_FUNC,
	SetBkColor_FUNC,
	SetBkMode_FUNC,
	SetBrushOrgEx_FUNC,
	SetCapture_FUNC,
	SetCaretPos_FUNC,
	SetClipboardData_FUNC,
	SetCurrentProcessExplicitAppUserModelID_FUNC,
	SetCursor_FUNC,
	SetCursorPos_FUNC,
	SetDIBColorTable_FUNC,
	SetDllDirectoryA_FUNC,
	SetDllDirectoryW_FUNC,
	SetErrorMode_FUNC,
	SetFocus_FUNC,
	SetForegroundWindow_FUNC,
	SetGestureConfig_FUNC,
	SetGraphicsMode_FUNC,
	SetLayeredWindowAttributes_FUNC,
	SetLayout_FUNC,
	SetMapMode_FUNC,
	SetMapperFlags_FUNC,
	SetMenu_FUNC,
	SetMenuDefaultItem_FUNC,
	SetMenuInfo_FUNC,
	SetMenuItemInfoA_FUNC,
	SetMenuItemInfoW_FUNC,
	SetMetaRgn_FUNC,
	SetPaletteEntries_FUNC,
	SetParent_FUNC,
	SetPixel_FUNC,
	SetPolyFillMode_FUNC,
	SetProcessDPIAware_FUNC,
	SetPropA_FUNC,
	SetPropW_FUNC,
	SetROP2_FUNC,
	SetRect_FUNC,
	SetRectRgn_FUNC,
	SetScrollInfo_FUNC,
	SetStretchBltMode_FUNC,
	SetTextAlign_FUNC,
	SetTextColor_FUNC,
	SetTimer_FUNC,
	SetViewportExtEx_FUNC,
	SetViewportOrgEx_FUNC,
	SetWindowExtEx_FUNC,
	SetWindowLongA_FUNC,
	SetWindowLongPtrA_FUNC,
	SetWindowLongPtrW_FUNC,
	SetWindowLongW_FUNC,
	SetWindowOrgEx_FUNC,
	SetWindowPlacement_FUNC,
	SetWindowPos_FUNC,
	SetWindowRgn_FUNC,
	SetWindowTextA_FUNC,
	SetWindowTextW_FUNC,
	SetWindowTheme_FUNC,
	SetWindowsHookExA_FUNC,
	SetWindowsHookExW_FUNC,
	SetWorldTransform_FUNC,
	ShellExecuteExA_FUNC,
	ShellExecuteExW_FUNC,
	Shell_1NotifyIconA_FUNC,
	Shell_1NotifyIconW_FUNC,
	ShowCaret_FUNC,
	ShowCursor_FUNC,
	ShowOwnedPopups_FUNC,
	ShowScrollBar_FUNC,
	ShowWindow_FUNC,
	SipGetInfo_FUNC,
	StartDocA_FUNC,
	StartDocW_FUNC,
	StartPage_FUNC,
	StretchBlt_FUNC,
	StrokePath_FUNC,
	SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_HIGHCONTRAST_2I_FUNC,
	SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSA_2I_FUNC,
	SystemParametersInfoA__IILorg_eclipse_swt_internal_win32_RECT_2I_FUNC,
	SystemParametersInfoA__II_3II_FUNC,
	SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_HIGHCONTRAST_2I_FUNC,
	SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_NONCLIENTMETRICSW_2I_FUNC,
	SystemParametersInfoW__IILorg_eclipse_swt_internal_win32_RECT_2I_FUNC,
	SystemParametersInfoW__II_3II_FUNC,
	TBBUTTONINFO_1sizeof_FUNC,
	TBBUTTON_1sizeof_FUNC,
	TCHITTESTINFO_1sizeof_FUNC,
	TCITEM_1sizeof_FUNC,
	TEXTMETRICA_1sizeof_FUNC,
	TEXTMETRICW_1sizeof_FUNC,
	TF_1DA_1COLOR_1sizeof_FUNC,
	TF_1DISPLAYATTRIBUTE_1sizeof_FUNC,
	TOOLINFO_1sizeof_FUNC,
	TOUCHINPUT_1sizeof_FUNC,
	TOUCH_1COORD_1TO_1PIXEL_FUNC,
	TRACKMOUSEEVENT_1sizeof_FUNC,
	TRIVERTEX_1sizeof_FUNC,
	TVHITTESTINFO_1sizeof_FUNC,
	TVINSERTSTRUCT_1sizeof_FUNC,
	TVITEMEX_1sizeof_FUNC,
	TVITEM_1sizeof_FUNC,
	TVSORTCB_1sizeof_FUNC,
	ToAscii_FUNC,
	ToUnicode_FUNC,
	TrackMouseEvent_FUNC,
	TrackPopupMenu_FUNC,
	TranslateAcceleratorA_FUNC,
	TranslateAcceleratorW_FUNC,
	TranslateCharsetInfo_FUNC,
	TranslateMDISysAccel_FUNC,
	TranslateMessage_FUNC,
	TransparentBlt_FUNC,
	TransparentImage_FUNC,
	TreeView_1GetItemRect_FUNC,
	UDACCEL_1sizeof_FUNC,
	UnhookWindowsHookEx_FUNC,
	UnmapViewOfFile_FUNC,
	UnregisterClassA_FUNC,
	UnregisterClassW_FUNC,
	UnregisterTouchWindow_FUNC,
	UpdateLayeredWindow_FUNC,
	UpdateWindow_FUNC,
	UrlCreateFromPathA_FUNC,
	UrlCreateFromPathW_FUNC,
	ValidateRect_FUNC,
	VkKeyScanA_FUNC,
	VkKeyScanW_FUNC,
#ifndef JNI64
	VtblCall__II_FUNC,
#else
	VtblCall__IJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_FUNC,
#else
	VtblCall__IJI_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIII_FUNC,
#else
	VtblCall__IJII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIII_FUNC,
#else
	VtblCall__IJIII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIII_3I_FUNC,
#else
	VtblCall__IJIII_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIII_3C_3CI_FUNC,
#else
	VtblCall__IJII_3C_3CI_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIJ_FUNC,
#else
	VtblCall__IJIJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIJI_3J_FUNC,
#else
	VtblCall__IJIJI_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIJJ_FUNC,
#else
	VtblCall__IJIJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3I_FUNC,
#else
	VtblCall__IJI_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3I_3I_FUNC,
#else
	VtblCall__IJI_3J_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3J_FUNC,
#else
	VtblCall__IJI_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_FUNC,
#else
	VtblCall__IJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJI_FUNC,
#else
	VtblCall__IJJI_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJII_3I_FUNC,
#else
	VtblCall__IJJII_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJII_3J_FUNC,
#else
	VtblCall__IJJII_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJI_3C_3CJ_FUNC,
#else
	VtblCall__IJJI_3C_3CJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJ_FUNC,
#else
	VtblCall__IJJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJI_3J_FUNC,
#else
	VtblCall__IJJJI_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJJ_FUNC,
#else
	VtblCall__IJJJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3I_FUNC,
#else
	VtblCall__IJJ_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3I_3I_FUNC,
#else
	VtblCall__IJJ_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3J_3J_FUNC,
#else
	VtblCall__IJJ_3J_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_win32_PROPERTYKEY_2I_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_win32_PROPERTYKEY_2I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_win32_PROPERTYKEY_2J_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_win32_PROPERTYKEY_2J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_win32_TF_1DISPLAYATTRIBUTE_2_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_win32_TF_1DISPLAYATTRIBUTE_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIS_3B_3B_3B_FUNC,
#else
	VtblCall__IJS_3B_3B_3B_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3B_3I_FUNC,
#else
	VtblCall__IJ_3B_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3B_3J_FUNC,
#else
	VtblCall__IJ_3B_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3C_FUNC,
#else
	VtblCall__IJ_3C_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3CI_FUNC,
#else
	VtblCall__IJ_3CI_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3CII_3I_3I_FUNC,
#else
	VtblCall__IJ_3CII_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3CJ_FUNC,
#else
	VtblCall__IJ_3CJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3I_FUNC,
#else
	VtblCall__IJ_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3I_3B_3I_FUNC,
#else
	VtblCall__IJ_3I_3B_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3I_3B_3J_FUNC,
#else
	VtblCall__IJ_3I_3B_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3J_FUNC,
#else
	VtblCall__IJ_3J_FUNC,
#endif
	WINDOWPLACEMENT_1sizeof_FUNC,
	WINDOWPOS_1sizeof_FUNC,
	WNDCLASS_1sizeof_FUNC,
	WaitMessage_FUNC,
#ifndef JNI64
	WideCharToMultiByte__II_3CIII_3B_3Z_FUNC,
#else
	WideCharToMultiByte__II_3CIJI_3B_3Z_FUNC,
#endif
	WideCharToMultiByte__II_3CI_3BI_3B_3Z_FUNC,
	WindowFromDC_FUNC,
	WindowFromPoint_FUNC,
	wcslen_FUNC,
} OS_FUNCS;
