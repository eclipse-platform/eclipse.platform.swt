/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;


import org.eclipse.swt.internal.*;

public class OS extends C {
	static {
		Library.loadLibrary ("swt"); //$NON-NLS-1$
	}
	
	/*
	* SWT Windows flags
	*/
	public static final boolean IsWin32s;
	public static final boolean IsWin95;
	public static final boolean IsWinNT;
	public static final boolean IsWinCE;
	public static final boolean IsPPC;
	public static final boolean IsHPC;
	public static final boolean IsSP;
	public static final boolean IsDBLocale;
	public static final boolean IsUnicode;
	public static final int WIN32_MAJOR, WIN32_MINOR, WIN32_VERSION;
	public static final int COMCTL32_MAJOR, COMCTL32_MINOR, COMCTL32_VERSION;
	public static final int SHELL32_MAJOR, SHELL32_MINOR, SHELL32_VERSION;

	public static final String NO_MANIFEST = "org.eclipse.swt.internal.win32.OS.NO_MANIFEST";

	/*
	* Flags for Window API GetVersionEx()
	*/
	public static final int VER_PLATFORM_WIN32s = 0;
	public static final int VER_PLATFORM_WIN32_WINDOWS = 1;
	public static final int VER_PLATFORM_WIN32_NT = 2;
	public static final int VER_PLATFORM_WIN32_CE = 3;
	
	/* Forward references */
	public static final int HEAP_ZERO_MEMORY = 0x8;
	public static final int ACTCTX_FLAG_RESOURCE_NAME_VALID = 0x00000008;
	public static final int ACTCTX_FLAG_SET_PROCESS_DEFAULT = 0x00000010;
	public static final int MANIFEST_RESOURCE_ID = 2;
	public static final int SM_DBCSENABLED = 0x2A;
	public static final int SM_IMMENABLED = 0x52;
	public static final int LANG_KOREAN = 0x12;
	public static final int LANG_JAPANESE = 0x11;
	public static final int MAX_PATH = 260;
	
	/* Get the Windows version and the flags */
	static {
		/*
		* Try the UNICODE version of GetVersionEx first
		* and then the ANSI version.  The UNICODE version
		* is present on all versions of Windows but is not
		* implemented on Win95/98/ME.
		* 
		* NOTE: The value of OSVERSIONINFO.sizeof cannot
		* be static final because it relies on the Windows
		* platform version to be initialized and IsUnicode
		* has not been calculated.  It must be initialized
		* here, after the platform is determined in order
		* for the value to be correct.
		*/
		OSVERSIONINFO info = new OSVERSIONINFOW ();
		info.dwOSVersionInfoSize = OSVERSIONINFOW.sizeof;
		if (!OS.GetVersionExW ((OSVERSIONINFOW)info)) {
			info = new OSVERSIONINFOA ();
			info.dwOSVersionInfoSize = OSVERSIONINFOA.sizeof;
			OS.GetVersionExA ((OSVERSIONINFOA)info);
		}
		OSVERSIONINFO.sizeof = info.dwOSVersionInfoSize;
		
		IsWin32s = info.dwPlatformId == VER_PLATFORM_WIN32s;
		IsWin95 = info.dwPlatformId == VER_PLATFORM_WIN32_WINDOWS;
		IsWinNT = info.dwPlatformId == VER_PLATFORM_WIN32_NT;
		IsWinCE = info.dwPlatformId == VER_PLATFORM_WIN32_CE;
		IsSP = IsSP();
		IsPPC = IsPPC();
		IsHPC = IsWinCE && !IsPPC && !IsSP;	
		WIN32_MAJOR = info.dwMajorVersion;
		WIN32_MINOR = info.dwMinorVersion;
		WIN32_VERSION = VERSION (WIN32_MAJOR, WIN32_MINOR);
		IsUnicode = !IsWin32s && !IsWin95;

		/* Load the manifest to force the XP Theme */
		if (System.getProperty (NO_MANIFEST) == null) {
			if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1)) {
				TCHAR buffer = new TCHAR (0, MAX_PATH);
				long /*int*/ hModule = OS.GetLibraryHandle ();
				while (OS.GetModuleFileName (hModule, buffer, buffer.length ()) == buffer.length ()) {
					buffer = new TCHAR (0, buffer.length () + MAX_PATH);
				}
				long /*int*/ hHeap = OS.GetProcessHeap ();
				int byteCount = buffer.length () * (OS.IsUnicode ? 2 : 1);
				long /*int*/ pszText = OS.HeapAlloc (hHeap, HEAP_ZERO_MEMORY, byteCount);
				OS.MoveMemory (pszText, buffer, byteCount);	
				ACTCTX pActCtx = new ACTCTX ();
				pActCtx.cbSize = ACTCTX.sizeof;
				pActCtx.dwFlags = ACTCTX_FLAG_RESOURCE_NAME_VALID | ACTCTX_FLAG_SET_PROCESS_DEFAULT;
				pActCtx.lpSource = pszText;
				pActCtx.lpResourceName = MANIFEST_RESOURCE_ID;
				long /*int*/ hActCtx = OS.CreateActCtx (pActCtx);
				if (pszText != 0) OS.HeapFree (hHeap, 0, pszText);
				long /*int*/ [] lpCookie = new long /*int*/ [1];
				OS.ActivateActCtx (hActCtx, lpCookie);
				/*
				* NOTE:  A single activation context is created and activated
				* for the entire lifetime of the program.  It is deactivated
				* and released by Windows when the program exits.
				*/
			}
		}
		
		/* Make the process DPI aware for Windows Vista */
		if (!OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (6, 0)) OS.SetProcessDPIAware ();

		/* Get the DBCS flag */
		boolean dbcsEnabled = OS.GetSystemMetrics (SM_DBCSENABLED) != 0;
		boolean immEnabled = OS.GetSystemMetrics (SM_IMMENABLED) != 0;
		IsDBLocale = dbcsEnabled || immEnabled;
		
		/*
		* Bug in Windows.  On Korean Windows XP when the Text
		* Services Framework support for legacy applications
		* is enabled, certain legacy calls segment fault.
		* For example, when ImmSetCompositionWindow() is used
		* to move the composition window outside of the client
		* area, Windows crashes.  The fix is to disable legacy
		* support.
		* 
		* Note: The bug is fixed in Service Pack 2.
		*/
		if (!OS.IsWinCE && OS.WIN32_VERSION == OS.VERSION (5, 1)) {
			short langID = OS.GetSystemDefaultUILanguage ();
			short primaryLang = OS.PRIMARYLANGID (langID);
			if (primaryLang == LANG_KOREAN) {
				OSVERSIONINFOEX infoex = IsUnicode ? (OSVERSIONINFOEX)new OSVERSIONINFOEXW () : (OSVERSIONINFOEX)new OSVERSIONINFOEXA ();
				infoex.dwOSVersionInfoSize = OSVERSIONINFOEX.sizeof;
				GetVersionEx (infoex);
				if (infoex.wServicePackMajor < 2) {
					OS.ImmDisableTextFrameService (0);
				}
			}
		}
	}
	
	/* Get the COMCTL32.DLL version */
	static {
		DLLVERSIONINFO dvi = new DLLVERSIONINFO ();
		dvi.cbSize = DLLVERSIONINFO.sizeof;
		dvi.dwMajorVersion = 4;
		dvi.dwMinorVersion = 0;
		TCHAR lpLibFileName = new TCHAR (0, "comctl32.dll", true); //$NON-NLS-1$
		long /*int*/ hModule = OS.LoadLibrary (lpLibFileName);
		if (hModule != 0) {
			String name = "DllGetVersion\0"; //$NON-NLS-1$
			byte [] lpProcName = new byte [name.length ()];
			for (int i=0; i<lpProcName.length; i++) {
				lpProcName [i] = (byte) name.charAt (i);
			}
			long /*int*/ DllGetVersion = OS.GetProcAddress (hModule, lpProcName);
			if (DllGetVersion != 0) OS.Call (DllGetVersion, dvi);
			OS.FreeLibrary (hModule);
		}
		COMCTL32_MAJOR = dvi.dwMajorVersion;
		COMCTL32_MINOR = dvi.dwMinorVersion;
		COMCTL32_VERSION = VERSION (COMCTL32_MAJOR, COMCTL32_MINOR);
	}
	
	/* Get the Shell32.DLL version */
	static {
		DLLVERSIONINFO dvi = new DLLVERSIONINFO ();
		dvi.cbSize = DLLVERSIONINFO.sizeof;
		dvi.dwMajorVersion = 4;
		TCHAR lpLibFileName = new TCHAR (0, "Shell32.dll", true); //$NON-NLS-1$
		long /*int*/ hModule = OS.LoadLibrary (lpLibFileName);
		if (hModule != 0) {
			String name = "DllGetVersion\0"; //$NON-NLS-1$
			byte [] lpProcName = new byte [name.length ()];
			for (int i=0; i<lpProcName.length; i++) {
				lpProcName [i] = (byte) name.charAt (i);
			}
			long /*int*/ DllGetVersion = OS.GetProcAddress (hModule, lpProcName);
			if (DllGetVersion != 0) OS.Call (DllGetVersion, dvi);
			OS.FreeLibrary (hModule);
		}
		SHELL32_MAJOR = dvi.dwMajorVersion;
		SHELL32_MINOR = dvi.dwMinorVersion;
		SHELL32_VERSION = VERSION (SHELL32_MAJOR, SHELL32_MINOR);
	}

	/* Flag used on WinCE */
	static final int SYS_COLOR_INDEX_FLAG = OS.IsWinCE ? 0x40000000 : 0x0;

	/*
	* NOTE:  There is a bug in JVM 1.2 where loading 
	* a class with a large number of constants causes
	* a segment fault to occur sometime later after
	* the class is loaded.  The fix is to break the
	* class up into a hierarchy of classes that each
	* contain a smaller number of constants.  This
	* fix is not necessary at this time but is required
	* when all constants are uncommented.  We have not
	* done the research to determine the limit.
	*/

	/* Constants */
	public static final int ABS_DOWNDISABLED = 8;
	public static final int ABS_DOWNHOT = 6;
	public static final int ABS_DOWNNORMAL = 5;
	public static final int ABS_DOWNPRESSED = 7;
	public static final int ABS_LEFTDISABLED = 12;
	public static final int ABS_LEFTHOT = 10;
	public static final int ABS_LEFTNORMAL = 9;
	public static final int ABS_LEFTPRESSED = 11;
	public static final int ABS_RIGHTDISABLED = 16;
	public static final int ABS_RIGHTHOT = 14;
	public static final int	ABS_RIGHTNORMAL = 13;
	public static final int	ABS_RIGHTPRESSED = 15;
	public static final int ABS_UPDISABLED = 4;
	public static final int ABS_UPHOT = 2;
	public static final int ABS_UPNORMAL = 1;
	public static final int ABS_UPPRESSED = 3;
	public static final int AC_SRC_OVER = 0;
	public static final int AC_SRC_ALPHA = 1;
//	public static final int ACTCTX_FLAG_RESOURCE_NAME_VALID = 0x00000008;
//	public static final int ACTCTX_FLAG_SET_PROCESS_DEFAULT = 0x00000010;
	public static final int ALTERNATE = 1;
	public static final int ASSOCF_NOTRUNCATE = 0x00000020;
	public static final int ASSOCF_INIT_IGNOREUNKNOWN = 0x400;
	public static final int ASSOCSTR_COMMAND = 1;
	public static final int ASSOCSTR_DEFAULTICON = 15;
	public static final int ASSOCSTR_FRIENDLYAPPNAME = 4;
	public static final int ASSOCSTR_FRIENDLYDOCNAME = 3;
	public static final int AW_SLIDE = 0x00040000;
	public static final int AW_ACTIVATE = 0x00020000;
	public static final int AW_BLEND = 0x00080000;
	public static final int AW_HIDE = 0x00010000;
	public static final int AW_CENTER = 0x00000010;
	public static final int AW_HOR_POSITIVE = 0x00000001;
	public static final int AW_HOR_NEGATIVE = 0x00000002;
	public static final int AW_VER_POSITIVE = 0x00000004;
	public static final int AW_VER_NEGATIVE = 0x00000008;
	public static final int ATTR_INPUT = 0x00;
	public static final int ATTR_TARGET_CONVERTED = 0x01;
	public static final int ATTR_CONVERTED = 0x02;
	public static final int ATTR_TARGET_NOTCONVERTED = 0x03;
	public static final int ATTR_INPUT_ERROR = 0x04;
	public static final int ATTR_FIXEDCONVERTED = 0x05;
	public static final int BCM_FIRST = 0x1600;
	public static final int BCM_GETIDEALSIZE = BCM_FIRST + 0x1;
	public static final int BCM_GETIMAGELIST = BCM_FIRST + 0x3;
	public static final int BCM_GETNOTE = BCM_FIRST + 0xa;
	public static final int BCM_GETNOTELENGTH = BCM_FIRST + 0xb;
	public static final int BCM_SETIMAGELIST = BCM_FIRST + 0x2;
	public static final int BCM_SETNOTE = BCM_FIRST + 0x9;
	public static final int BDR_RAISEDOUTER = 0x0001;
	public static final int BDR_SUNKENOUTER = 0x0002;
	public static final int BDR_RAISEDINNER = 0x0004;
	public static final int BDR_SUNKENINNER = 0x0008;
	public static final int BDR_OUTER = 0x0003;
	public static final int BDR_INNER = 0x000c;
	public static final int BDR_RAISED = 0x0005;
	public static final int BDR_SUNKEN = 0x000a;
	public static final int BFFM_INITIALIZED = 0x1;
	public static final int BFFM_SETSELECTION = IsUnicode ? 0x467 : 0x466;
	public static final int BFFM_VALIDATEFAILED = IsUnicode ? 0x4 : 0x3;
	public static final int BFFM_VALIDATEFAILEDW = 0x4;
	public static final int BFFM_VALIDATEFAILEDA = 0x3;
	public static final int BF_ADJUST = 0x2000; 
	public static final int BF_LEFT = 0x0001;
	public static final int BF_TOP = 0x0002;
	public static final int BF_RIGHT = 0x0004;
	public static final int BF_BOTTOM = 0x0008;
	public static final int BF_RECT = (BF_LEFT | BF_TOP | BF_RIGHT | BF_BOTTOM);
	public static final int BIF_EDITBOX = 0x10;
	public static final int BIF_NEWDIALOGSTYLE = 0x40;
	public static final int BIF_RETURNONLYFSDIRS = 0x1;
	public static final int BIF_VALIDATE = 0x20;
	public static final int BITSPIXEL = 0xc;
	public static final int BI_BITFIELDS = 3;
	public static final int BI_RGB = 0;
	public static final int BLACKNESS = 0x42;
	public static final int BLACK_BRUSH = 4;
	public static final int BUTTON_IMAGELIST_ALIGN_LEFT = 0;
	public static final int BUTTON_IMAGELIST_ALIGN_RIGHT = 1;
	public static final int BUTTON_IMAGELIST_ALIGN_CENTER = 4;
	public static final int BM_CLICK = 0xf5;
	public static final int BM_GETCHECK = 0xf0;
	public static final int BM_SETCHECK = 0xf1;
	public static final int BM_SETIMAGE = 0xf7;
	public static final int BM_SETSTYLE = 0xf4;
	public static final int BN_CLICKED = 0x0;
	public static final int BN_DOUBLECLICKED = 0x5;
	public static final int BPBF_COMPATIBLEBITMAP = 0;
	public static final int BPBF_DIB = 1;
	public static final int BPBF_TOPDOWNDIB = 2;
	public static final int BPBF_TOPDOWNMONODIB = 3;
	public static final int BPPF_ERASE = 0x0001;
	public static final int BPPF_NOCLIP = 0x0002;
	public static final int BPPF_NONCLIENT = 0x0004;
	public static final int BP_PUSHBUTTON = 1;
	public static final int BP_RADIOBUTTON = 2;
	public static final int BP_CHECKBOX = 3;
	public static final int BP_GROUPBOX = 4;
	public static final int BST_CHECKED = 0x1;
	public static final int BST_INDETERMINATE = 0x2;
	public static final int BST_UNCHECKED = 0x0;
	public static final int BS_3STATE = 0x5;
	public static final int BS_BITMAP = 0x80;
	public static final int BS_CENTER = 0x300;
	public static final int BS_CHECKBOX = 0x2;
	public static final int BS_COMMANDLINK =  0xe;
	public static final int BS_DEFPUSHBUTTON = 0x1;
	public static final int BS_FLAT = 0x8000;
	public static final int BS_GROUPBOX = 0x7;
	public static final int BS_ICON = 0x40;
	public static final int BS_LEFT = 0x100;
	public static final int BS_MULTILINE = 0x2000;
	public static final int BS_NOTIFY = 0x4000;
	public static final int BS_OWNERDRAW = 0xb;
	public static final int BS_PATTERN = 0x3;
	public static final int BS_PUSHBUTTON = 0x0;
	public static final int BS_PUSHLIKE = 0x1000;
	public static final int BS_RADIOBUTTON = 0x4;
	public static final int BS_RIGHT = 0x200;
	public static final int BS_SOLID = 0x0;
	public static final int BTNS_AUTOSIZE = 0x10;
	public static final int BTNS_BUTTON = 0x0;
	public static final int BTNS_CHECK = 0x2;
	public static final int BTNS_CHECKGROUP = 0x6;
	public static final int BTNS_DROPDOWN = 0x8;
	public static final int BTNS_GROUP = 0x4;
	public static final int BTNS_SEP = 0x1;
	public static final int BTNS_SHOWTEXT = 0x40;
	public static final int CBN_DROPDOWN = 0x7;
	public static final int CBN_EDITCHANGE = 0x5;
	public static final int CBN_KILLFOCUS = 0x4;
	public static final int CBN_SELCHANGE = 0x1;
	public static final int CBN_SETFOCUS = 0x3;
	public static final int CBS_AUTOHSCROLL = 0x40;
	public static final int CBS_DROPDOWN = 0x2;
	public static final int CBS_DROPDOWNLIST = 0x3;
	public static final int CBS_CHECKEDNORMAL = 5;
	public static final int CBS_MIXEDNORMAL = 9;
	public static final int CBS_NOINTEGRALHEIGHT = 0x400;
	public static final int CBS_SIMPLE = 0x1;
	public static final int CBS_UNCHECKEDNORMAL = 1;
	public static final int CBS_CHECKEDDISABLED = 8;
	public static final int CBS_CHECKEDHOT = 6;
	public static final int CBS_CHECKEDPRESSED = 7;
	public static final int CBS_MIXEDDISABLED = 12;
	public static final int CBS_MIXEDHOT = 10;
	public static final int CBS_MIXEDPRESSED = 11;
	public static final int CBS_UNCHECKEDDISABLED = 4;
	public static final int CBS_UNCHECKEDHOT = 2;
	public static final int CBS_UNCHECKEDPRESSED = 3;
	public static final int CB_ADDSTRING = 0x143;
	public static final int CB_DELETESTRING = 0x144;
	public static final int CB_ERR = 0xffffffff;
	public static final int CB_ERRSPACE = 0xfffffffe;
	public static final int CB_FINDSTRINGEXACT = 0x158;
	public static final int CB_GETCOUNT = 0x146;
	public static final int CB_GETCURSEL = 0x147;
	public static final int CB_GETDROPPEDCONTROLRECT = 0x152;
	public static final int CB_GETDROPPEDSTATE = 0x157;
	public static final int CB_GETDROPPEDWIDTH = 0x015f;
	public static final int CB_GETEDITSEL = 0x140;
	public static final int CB_GETHORIZONTALEXTENT = 0x015d;
	public static final int CB_GETITEMHEIGHT = 0x154;
	public static final int CB_GETLBTEXT = 0x148;
	public static final int CB_GETLBTEXTLEN = 0x149;
	public static final int CB_INSERTSTRING = 0x14a;
	public static final int CB_LIMITTEXT = 0x141;
	public static final int CB_RESETCONTENT = 0x14b;
	public static final int CB_SELECTSTRING = 0x14d;
	public static final int CB_SETCURSEL = 0x14e;
	public static final int CB_SETDROPPEDWIDTH= 0x0160;
	public static final int CB_SETEDITSEL = 0x142;
	public static final int CB_SETHORIZONTALEXTENT = 0x015e;
	public static final int CB_SETITEMHEIGHT = 0x0153;
	public static final int CB_SHOWDROPDOWN = 0x14f;
	public static final int CBXS_NORMAL = 1;
	public static final int CBXS_HOT = 2;
	public static final int CBXS_PRESSED = 3;
	public static final int CBXS_DISABLED = 4;
	public static final int CCHDEVICENAME = 32;
	public static final int CCHFORMNAME = 32;
	public static final int CCHILDREN_SCROLLBAR = 5;
	public static final int CCM_FIRST = 0x2000;
	public static final int CCM_SETBKCOLOR = 0x2001;
	public static final int CCM_SETVERSION = 0x2007;
	public static final int CCS_NODIVIDER = 0x40;
	public static final int CCS_NORESIZE = 0x4;
	public static final int CCS_VERT = 0x80;
	public static final int CC_ANYCOLOR = 0x100;
	public static final int CC_ENABLEHOOK = 0x10;
	public static final int CC_FULLOPEN = 0x2;
	public static final int CC_RGBINIT = 0x1;
	public static final int CDDS_POSTERASE = 0x00000004;
	public static final int CDDS_POSTPAINT = 0x00000002;
	public static final int CDDS_PREERASE = 0x00000003;
	public static final int CDDS_PREPAINT = 0x00000001;
	public static final int CDDS_ITEM = 0x00010000;
	public static final int CDDS_ITEMPOSTPAINT = CDDS_ITEM | CDDS_POSTPAINT;
	public static final int CDDS_ITEMPREPAINT = CDDS_ITEM | CDDS_PREPAINT;
	public static final int CDDS_SUBITEM = 0x00020000;
	public static final int CDDS_SUBITEMPOSTPAINT = CDDS_ITEMPOSTPAINT | CDDS_SUBITEM;
	public static final int CDDS_SUBITEMPREPAINT = CDDS_ITEMPREPAINT | CDDS_SUBITEM;
	public static final int CDIS_SELECTED = 0x0001;
	public static final int CDIS_GRAYED = 0x0002;
	public static final int CDIS_DISABLED = 0x0004;
	public static final int CDIS_CHECKED = 0x0008;
	public static final int CDIS_FOCUS = 0x0010;
	public static final int CDIS_DEFAULT = 0x0020;
	public static final int CDIS_DROPHILITED = 0x1000;
	public static final int CDIS_HOT = 0x0040;
	public static final int CDIS_MARKED = 0x0080;
	public static final int CDIS_INDETERMINATE = 0x0100;
	public static final int CDM_FIRST = 0x0400 + 100;
	public static final int CDM_GETSPEC = CDM_FIRST;
	public static final int CDN_FIRST = -601;
	public static final int CDN_SELCHANGE = CDN_FIRST - 1;
	public static final int CDRF_DODEFAULT = 0x00000000;
	public static final int CDRF_DOERASE = 0x00000008;
	public static final int CDRF_NEWFONT = 0x00000002;
	public static final int CDRF_NOTIFYITEMDRAW = 0x00000020;
	public static final int CDRF_NOTIFYPOSTERASE = 0x00000040;
	public static final int CDRF_NOTIFYPOSTPAINT = 0x00000010;
	public static final int CDRF_NOTIFYSUBITEMDRAW = 0x00000020;
	public static final int CDRF_SKIPDEFAULT = 0x04;
	public static final int CDRF_SKIPPOSTPAINT = 0x00000100;
	public static final int CERT_SIMPLE_NAME_STR = 1;
	public static final int CFE_AUTOCOLOR = 0x40000000;
	public static final int CFE_ITALIC = 0x2;
	public static final int CFE_STRIKEOUT = 0x8;
	public static final int CFE_UNDERLINE = 0x4;
	public static final int CFM_BOLD = 0x1;
	public static final int CFM_CHARSET = 0x8000000;
	public static final int CFM_COLOR = 0x40000000;
	public static final int CFM_FACE = 0x20000000;
	public static final int CFM_ITALIC = 0x2;
	public static final int CFM_SIZE = 0x80000000;
	public static final int CFM_STRIKEOUT = 0x8;
	public static final int CFM_UNDERLINE = 0x4;
	public static final int CFM_WEIGHT = 0x400000;
	public static final int CFS_POINT = 0x2;
	public static final int CFS_RECT = 0x1;
	public static final int CFS_CANDIDATEPOS = 0x0040;
	public static final int CFS_EXCLUDE = 0x0080;
	public static final int CF_EFFECTS = 0x100;
	public static final int CF_INITTOLOGFONTSTRUCT = 0x40;
	public static final int CF_SCREENFONTS = 0x1;
	public static final int CF_TEXT = 0x1;
	public static final int CF_UNICODETEXT = 13;
	public static final int CF_USESTYLE = 0x80;
	public static final int CLR_DEFAULT = 0xff000000;
	public static final int CLR_INVALID = 0xffffffff;
	public static final int CLR_NONE = 0xffffffff;
	public static final int CLSCTX_INPROC_SERVER = 1;
	public static final int CSIDL_APPDATA = 0x1a;
	public static final int CSIDL_LOCAL_APPDATA = 0x1c;
	public static final int COLORONCOLOR = 0x3;
	public static final int COLOR_3DDKSHADOW = 0x15 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_3DFACE = 0xf | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_3DHIGHLIGHT = 0x14 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_3DHILIGHT = 0x14 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_3DLIGHT = 0x16 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_3DSHADOW = 0x10 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_ACTIVECAPTION = 0x2 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_BTNFACE = 0xf | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_BTNHIGHLIGHT = 0x14 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_BTNSHADOW = 0x10 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_BTNTEXT = 0x12 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_CAPTIONTEXT = 0x9 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_GRADIENTACTIVECAPTION = 0x1b | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_GRADIENTINACTIVECAPTION = 0x1c | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_GRAYTEXT = 0x11 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_HIGHLIGHT = 0xd | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_HIGHLIGHTTEXT = 0xe | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_HOTLIGHT = 26 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_INACTIVECAPTION = 0x3 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_INACTIVECAPTIONTEXT = 0x13 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_INFOBK = 0x18 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_INFOTEXT = 0x17 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_MENU = 0x4 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_MENUTEXT = 0x7 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_SCROLLBAR = 0x0 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_WINDOW = 0x5 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_WINDOWFRAME = 0x6 | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_WINDOWTEXT = 0x8 | SYS_COLOR_INDEX_FLAG;
	public static final int COMPLEXREGION = 0x3;
	public static final int CP_ACP = 0x0;
	public static final int CP_UTF8 = 65001;
	public static final int CP_DROPDOWNBUTTON = 1;
	public static final int CP_INSTALLED = 0x1;
	public static final int CPS_COMPLETE = 0x1;
	public static final int CS_BYTEALIGNWINDOW = 0x2000;
	public static final int CS_DBLCLKS = 0x8;
	public static final int CS_DROPSHADOW = 0x20000;
	public static final int CS_GLOBALCLASS = 0x4000;
	public static final int CS_HREDRAW = 0x2;
	public static final int CS_VREDRAW = 0x1;
	public static final int CS_OWNDC = 0x20;
	public static final int CW_USEDEFAULT = 0x80000000;
	public static final String DATETIMEPICK_CLASS = "SysDateTimePick32"; //$NON-NLS-1$
	public static final int DATE_LONGDATE = 0x00000002;
	public static final int DATE_SHORTDATE = 0x00000001;
	public static final int DATE_YEARMONTH = 0x00000008; //#if(WINVER >= 0x0500)
	public static final int DCX_CACHE = 0x2;
	public static final int DCX_CLIPCHILDREN = 0x8;
	public static final int DCX_CLIPSIBLINGS = 0x10;
	public static final int DCX_INTERSECTRGN = 0x80;
	public static final int DCX_WINDOW = 0x1;
	public static final int DEFAULT_CHARSET = 0x1;
	public static final int DEFAULT_GUI_FONT = 0x11;
	public static final int DFCS_BUTTONCHECK = 0x0;
	public static final int DFCS_CHECKED = 0x400;
	public static final int DFCS_FLAT = 0x4000;
	public static final int DFCS_INACTIVE = 0x100;
	public static final int DFCS_PUSHED = 0x200;
	public static final int DFCS_SCROLLDOWN = 0x1;
	public static final int DFCS_SCROLLLEFT = 0x2;
	public static final int DFCS_SCROLLRIGHT = 0x3;
	public static final int DFCS_SCROLLUP = 0x0;
	public static final int DFC_BUTTON = 0x4;
	public static final int DFC_SCROLL = 0x3;
	public static final int DIB_RGB_COLORS = 0x0;
	public static final int DISP_E_EXCEPTION = 0x80020009;
	public static final int DI_NORMAL = 0x3;
	public static final int DI_NOMIRROR = 0x10;
	public static final int DLGC_BUTTON = 0x2000;
	public static final int DLGC_HASSETSEL = 0x8;
	public static final int DLGC_STATIC = 0x100;
	public static final int DLGC_WANTALLKEYS = 0x4;
	public static final int DLGC_WANTARROWS = 0x1;
	public static final int DLGC_WANTCHARS = 0x80;
	public static final int DLGC_WANTTAB = 0x2;
	public static final short DMCOLLATE_FALSE = 0;
	public static final short DMCOLLATE_TRUE = 1;
	public static final int DM_SETDEFID = 0x401;
	public static final int DM_COLLATE = 0x00008000;
	public static final int DM_COPIES = 0x00000100;
	public static final int DM_DUPLEX = 0x00001000;
	public static final int DM_ORIENTATION = 0x00000001;
	public static final int DM_OUT_BUFFER = 2;
	public static final short DMORIENT_PORTRAIT = 1;
	public static final short DMORIENT_LANDSCAPE = 2;
	public static final short DMDUP_SIMPLEX = 1;
	public static final short DMDUP_VERTICAL = 2;
	public static final short DMDUP_HORIZONTAL = 3;
	public static final int DSS_DISABLED = 0x20;
	public static final int DSTINVERT = 0x550009;
	public static final int DST_BITMAP = 0x4;
	public static final int DST_ICON = 0x3;
	public static final int DT_BOTTOM = 0x8;
	public static final int DT_CALCRECT = 0x400;
	public static final int DT_CENTER = 0x1;
	public static final int DT_EDITCONTROL = 0x2000;
	public static final int DT_EXPANDTABS = 0x40;
	public static final int DT_ENDELLIPSIS = 32768;
	public static final int DT_HIDEPREFIX = 0x100000;
	public static final int DT_LEFT = 0x0;
	public static final int DT_NOPREFIX = 0x800;
	public static final int DT_RASPRINTER = 0x2;
	public static final int DT_RIGHT = 0x2;
	public static final int DT_RTLREADING = 0x00020000;
	public static final int DT_SINGLELINE = 0x20;
	public static final int DT_TOP = 0;
	public static final int DT_VCENTER = 4;
	public static final int DT_WORDBREAK = 0x10;
	public static final int DTM_FIRST = 0x1000;
	public static final int DTM_GETSYSTEMTIME = DTM_FIRST + 1; 
	public static final int DTM_GETIDEALSIZE = DTM_FIRST + 15;
	public static final int DTM_SETFORMAT = IsUnicode ? DTM_FIRST + 50 : DTM_FIRST + 5;
	public static final int DTM_SETSYSTEMTIME = DTM_FIRST + 2;
	public static final int DTN_FIRST = 0xFFFFFD08;
	public static final int DTN_DATETIMECHANGE = DTN_FIRST + 1;
	public static final int DTN_CLOSEUP = DTN_FIRST + 7;
	public static final int DTN_DROPDOWN = DTN_FIRST + 6;
	public static final int DTS_LONGDATEFORMAT = 0x0004;
	public static final int DTS_SHORTDATECENTURYFORMAT = 0x000C;
	public static final int DTS_SHORTDATEFORMAT = 0x0000;
	public static final int DTS_TIMEFORMAT = 0x0009;
	public static final int DTS_UPDOWN = 0x0001;
	public static final int DWM_BB_ENABLE = 0x1;
	public static final int DWM_BB_BLURREGION = 0x2; 
	public static final int DWM_BB_TRANSITIONONMAXIMIZED = 0x4;
	public static final int E_POINTER = 0x80004003;
	public static final int EBP_NORMALGROUPBACKGROUND = 5;
	public static final int EBP_NORMALGROUPCOLLAPSE = 6;
	public static final int EBP_NORMALGROUPEXPAND = 7;
	public static final int EBP_NORMALGROUPHEAD = 8;
	public static final int EBNGC_NORMAL = 1;
	public static final int EBNGC_HOT = 2;
	public static final int EBNGC_PRESSED = 3;
	public static final int EBP_HEADERBACKGROUND = 1;
	public static final int EC_LEFTMARGIN = 0x1;
	public static final int EC_RIGHTMARGIN = 0x2;
	public static final int ECOOP_AND = 0x3;
	public static final int ECOOP_OR = 0x2;
	public static final int ECO_AUTOHSCROLL = 0x80;
	public static final int EDGE_RAISED = (BDR_RAISEDOUTER | BDR_RAISEDINNER);
	public static final int EDGE_SUNKEN = (BDR_SUNKENOUTER | BDR_SUNKENINNER);
	public static final int EDGE_ETCHED = (BDR_SUNKENOUTER | BDR_RAISEDINNER);
	public static final int EDGE_BUMP = (BDR_RAISEDOUTER | BDR_SUNKENINNER);
	public static final int ELF_VENDOR_SIZE = 4;
	public static final int EM_CANUNDO = 0xc6;
	public static final int EM_CHARFROMPOS = 0xd7;
	public static final int EM_DISPLAYBAND = 0x433;
	public static final int EM_GETFIRSTVISIBLELINE = 0xce;
	public static final int EM_GETLIMITTEXT = 0xd5;
	public static final int EM_GETLINE = 0xc4;
	public static final int EM_GETLINECOUNT = 0xba;
	public static final int EM_GETMARGINS = 0xd4;
	public static final int EM_GETPASSWORDCHAR = 0xd2;
	public static final int EM_GETSCROLLPOS = 0x4dd;
	public static final int EM_GETSEL = 0xb0;
	public static final int EM_LIMITTEXT = 0xc5;
	public static final int EM_LINEFROMCHAR = 0xc9;
	public static final int EM_LINEINDEX = 0xbb;
	public static final int EM_LINELENGTH = 0xc1;
	public static final int EM_LINESCROLL = 0xb6;
	public static final int EM_POSFROMCHAR = 0xd6;
	public static final int EM_REPLACESEL = 0xc2;
	public static final int EM_SCROLLCARET = 0xb7;
	public static final int EM_SETBKGNDCOLOR = 0x443;
	public static final int EM_SETLIMITTEXT = 0xc5;
	public static final int EM_SETMARGINS = 211;
	public static final int EM_SETOPTIONS = 0x44d;
	public static final int EM_SETPARAFORMAT = 0x447;
	public static final int EM_SETPASSWORDCHAR = 0xcc;
	public static final int EM_SETCUEBANNER = 0x1500 + 1;
	public static final int EM_SETREADONLY = 0xcf;
	public static final int EM_SETRECT = 0xb3;
	public static final int EM_SETSEL = 0xb1;
	public static final int EM_SETTABSTOPS = 0xcb;
	public static final int EM_UNDO = 199;
	public static final int EMR_EXTCREATEFONTINDIRECTW = 82;
	public static final int EMR_EXTTEXTOUTW = 84;
	public static final int EN_ALIGN_LTR_EC = 0x0700;
	public static final int EN_ALIGN_RTL_EC = 0x0701;
	public static final int EN_CHANGE = 0x300;
	public static final int EP_EDITTEXT = 1;
	public static final int ERROR_FILE_NOT_FOUND = 0x2;
	public static final int ERROR_NO_MORE_ITEMS = 0x103;
	public static final int ESB_DISABLE_BOTH = 0x3;
	public static final int ESB_ENABLE_BOTH = 0x0;
	public static final int ES_AUTOHSCROLL = 0x80;
	public static final int ES_AUTOVSCROLL = 0x40;
	public static final int ES_CENTER = 0x1;
	public static final int ES_MULTILINE = 0x4;
	public static final int ES_NOHIDESEL = 0x100;
	public static final int ES_PASSWORD = 0x20;
	public static final int ES_READONLY = 0x800;
	public static final int ES_RIGHT = 0x2;
	public static final int ETO_CLIPPED = 0x4;
	public static final int ETS_NORMAL = 1;
	public static final int ETS_HOT = 2;
	public static final int ETS_SELECTED = 3;
	public static final int ETS_DISABLED = 4;
	public static final int ETS_FOCUSED = 5;
	public static final int ETS_READONLY = 6;
	public static final int EVENT_OBJECT_FOCUS = 0x8005;
	public static final int EVENT_OBJECT_LOCATIONCHANGE = 0x800B;
	public static final int EVENT_OBJECT_SELECTIONWITHIN = 0x8009;
	public static final int EVENT_OBJECT_VALUECHANGE = 0x800E;
	public static final short FADF_FIXEDSIZE = 0x10;
	public static final short FADF_HAVEVARTYPE = 0x80;
	public static final int FALT = 0x10;
	public static final int FCONTROL = 0x8;
	public static final int FE_FONTSMOOTHINGCLEARTYPE = 0x0002;
	public static final int FEATURE_DISABLE_NAVIGATION_SOUNDS = 21;
	public static final int FILE_ATTRIBUTE_DIRECTORY = 0x00000010;
	public static final int FILE_ATTRIBUTE_NORMAL = 0x00000080;
	public static final int FILE_MAP_READ = 4;
	public static final int FLICKDIRECTION_RIGHT = 0;
	public static final int FLICKDIRECTION_UPRIGHT = 1;
	public static final int FLICKDIRECTION_UP = 2;
	public static final int FLICKDIRECTION_UPLEFT = 3;
	public static final int FLICKDIRECTION_LEFT = 4;
	public static final int FLICKDIRECTION_DOWNLEFT = 5;
	public static final int FLICKDIRECTION_DOWN = 6;
	public static final int FLICKDIRECTION_DOWNRIGHT = 7;
	public static final int FLICKDIRECTION_INVALID = 8;
	public static final int FNERR_INVALIDFILENAME = 0x3002;
	public static final int FNERR_BUFFERTOOSMALL = 0x3003;
	public static final int FOF_SILENT = 0x0004;
	public static final int FOF_NOCONFIRMATION = 0x0010;
	public static final int FOF_NOCONFIRMMKDIR = 0x0200;
	public static final int FOF_NOERRORUI = 0x0400;
	public static final int FOF_NO_UI = (FOF_SILENT | FOF_NOCONFIRMATION | FOF_NOERRORUI | FOF_NOCONFIRMMKDIR); 
	public static final int FORMAT_MESSAGE_ALLOCATE_BUFFER = 0x00000100;
	public static final int FORMAT_MESSAGE_FROM_SYSTEM = 0x00001000;
	public static final int FORMAT_MESSAGE_IGNORE_INSERTS = 0x00000200;
	public static final int FR_PRIVATE = 0x10;
	public static final int FSHIFT = 0x4;
	public static final int FVIRTKEY = 0x1;
	public static final int GBS_NORMAL = 1;
	public static final int GBS_DISABLED = 2;
	public static final int GBF_DIRECT = 0x00000001;
	public static final int GBF_COPY = 0x00000002;
	public static final int GBF_VALIDBITS = 0x00000003;
	public static final int GCP_REORDER = 0x0002;
	public static final int GCP_GLYPHSHAPE = 0x0010;
	public static final int GCP_CLASSIN = 0x00080000;
	public static final int GCP_LIGATE = 0x0020;
	public static final int GCS_COMPSTR = 0x8;
	public static final int GCS_RESULTSTR = 0x800;
	public static final int GCS_COMPATTR = 0x0010;
	public static final int GCS_COMPCLAUSE = 0x0020;
	public static final int GCS_CURSORPOS = 0x0080;
	public static final int GDT_VALID = 0;
	public static final int GET_FEATURE_FROM_PROCESS = 0x2;
	public static final int GF_BEGIN = 1;
	public static final int GF_INERTIA = 2;
	public static final int GF_END = 4;
	public static final int GGI_MARK_NONEXISTING_GLYPHS = 1;
	public static final int GID_BEGIN = 1;
	public static final int GID_END = 2;
	public static final int GID_ZOOM = 3;
	public static final int GID_PAN = 4;
	public static final int GID_ROTATE = 5;
	public static final int GID_TWOFINGERTAP = 6;
	public static final int GID_PRESSANDTAP = 7;
	public static final int GLPS_CLOSED = 1;
	public static final int GLPS_OPENED = 2;
	public static final int GM_ADVANCED = 2;
	public static final int GMDI_USEDISABLED = 0x1;
	public static final int GMEM_FIXED = 0x0;
	public static final int GMEM_MOVEABLE = 0x2;
	public static final int GMEM_ZEROINIT = 0x40;
	public static final int GN_CONTEXTMENU = 1000;
	public static final int GPTR = 0x40;
	public static final int GRADIENT_FILL_RECT_H = 0x0;
	public static final int GRADIENT_FILL_RECT_V = 0x1;
	public static final int GTL_NUMBYTES = 0x10;
	public static final int GTL_NUMCHARS = 0x8;
	public static final int GTL_PRECISE = 0x2;
	public static final int GT_DEFAULT = 0x0;
	public static final int GUI_16BITTASK = 0x20;
	public static final int GUI_CARETBLINKING = 0x1;
	public static final int GUI_INMENUMODE = 0x4;
	public static final int GUI_INMOVESIZE = 0x2;
	public static final int GUI_POPUPMENUMODE = 0x10;
	public static final int GUI_SYSTEMMENUMODE = 0x8;
	public static final int GWL_EXSTYLE = 0xffffffec;
	public static final int GWL_ID = -12;
	public static final int GWL_HWNDPARENT = -8;
	public static final int GWL_STYLE = 0xfffffff0;
	public static final int GWL_USERDATA = 0xffffffeb;
	public static final int GWL_WNDPROC = 0xfffffffc;
	public static final int GWLP_ID = -12;
	public static final int GWLP_HWNDPARENT = -8;
	public static final int GWLP_USERDATA = 0xffffffeb;
	public static final int GWLP_WNDPROC = 0xfffffffc;
	public static final int GW_CHILD = 0x5;
	public static final int GW_HWNDFIRST = 0x0;
	public static final int GW_HWNDLAST = 0x1;
	public static final int GW_HWNDNEXT = 0x2;
	public static final int GW_HWNDPREV = 0x3;
	public static final int GW_OWNER = 0x4;
	public static final int HBMMENU_CALLBACK = 0xffffffff;
	public static final int HCBT_CREATEWND = 3;
	public static final int HCF_HIGHCONTRASTON = 0x1;
	public static final int HDF_BITMAP = 0x2000;
	public static final int HDF_BITMAP_ON_RIGHT = 0x1000;
	public static final int HDF_CENTER = 2;
	public static final int HDF_JUSTIFYMASK = 0x3;
	public static final int HDF_IMAGE = 0x0800;
	public static final int HDF_LEFT = 0;
	public static final int HDF_RIGHT = 1;
	public static final int HDF_SORTUP = 0x0400;
	public static final int HDF_SORTDOWN = 0x0200;
	public static final int HDI_BITMAP = 0x0010;
	public static final int HDI_IMAGE = 32;
	public static final int HDI_ORDER = 0x80;
	public static final int HDI_TEXT = 0x2;
	public static final int HDI_WIDTH = 0x1;
	public static final int HDI_FORMAT = 0x4;
	public static final int HDM_FIRST = 0x1200;
	public static final int HDM_DELETEITEM = HDM_FIRST + 2;
	public static final int HDM_GETBITMAPMARGIN = HDM_FIRST + 21;
	public static final int HDM_GETITEMCOUNT = 0x1200;
	public static final int HDM_GETITEMA = HDM_FIRST + 3;
	public static final int HDM_GETITEMW = HDM_FIRST + 11;
	public static final int HDM_GETITEM = IsUnicode ? HDM_GETITEMW : HDM_GETITEMA;
	public static final int HDM_GETITEMRECT = HDM_FIRST + 7;
	public static final int HDM_GETORDERARRAY = HDM_FIRST + 17;
	public static final int HDM_HITTEST = HDM_FIRST + 6;
	public static final int HDM_INSERTITEMA = HDM_FIRST + 1;
	public static final int HDM_INSERTITEMW = HDM_FIRST + 10;
	public static final int HDM_INSERTITEM = IsUnicode ? HDM_INSERTITEMW : HDM_INSERTITEMA;
	public static final int HDM_LAYOUT = HDM_FIRST + 5;
	public static final int HDM_ORDERTOINDEX = HDM_FIRST + 15;
	public static final int HDM_SETIMAGELIST = HDM_FIRST + 8;
	public static final int HDM_SETITEMA = HDM_FIRST + 4;
	public static final int HDM_SETITEMW = HDM_FIRST + 12;
	public static final int HDM_SETITEM = IsUnicode ? HDM_SETITEMW : HDM_SETITEMA;
	public static final int HDM_SETORDERARRAY = HDM_FIRST + 18;
	public static final int HDN_FIRST = 0xfffffed4;
	public static final int HDN_BEGINDRAG = HDN_FIRST - 10;
	public static final int HDN_BEGINTRACK = IsUnicode ? 0xfffffeba : 0xfffffece;
	public static final int HDN_BEGINTRACKW = 0xfffffeba;
	public static final int HDN_BEGINTRACKA = 0xfffffece;
	public static final int HDN_DIVIDERDBLCLICKA = HDN_FIRST - 5;
	public static final int HDN_DIVIDERDBLCLICKW = HDN_FIRST - 25;
	public static final int HDN_DIVIDERDBLCLICK = IsUnicode ? HDN_DIVIDERDBLCLICKW : HDN_DIVIDERDBLCLICKA;
	public static final int HDN_ENDDRAG = HDN_FIRST - 11;
	public static final int HDN_ITEMCHANGED = IsUnicode ? 0xfffffebf : 0xfffffed3;
	public static final int HDN_ITEMCHANGEDW = 0xfffffebf;
	public static final int HDN_ITEMCHANGEDA = 0xfffffed3;
	public static final int HDN_ITEMCHANGINGW = HDN_FIRST - 20;
	public static final int HDN_ITEMCHANGINGA = HDN_FIRST;
	public static final int HDN_ITEMCLICKW = HDN_FIRST - 22;
	public static final int HDN_ITEMCLICKA = HDN_FIRST - 2;
	public static final int HDN_ITEMDBLCLICKW = HDN_FIRST - 23;
	public static final int HDN_ITEMDBLCLICKA = HDN_FIRST - 3;
	public static final int HDN_ITEMDBLCLICK = IsUnicode ? HDN_ITEMDBLCLICKW : HDN_ITEMDBLCLICKA;
	public static final int HDS_BUTTONS = 0x2;
	public static final int HDS_DRAGDROP = 0x0040;
	public static final int HDS_FULLDRAG = 0x80;
	public static final int HDS_HIDDEN = 0x8;
//	public static final int HEAP_ZERO_MEMORY = 0x8;
	public static final int HELPINFO_MENUITEM = 0x2;
	public static final int HHT_ONDIVIDER = 0x4;
	public static final int HHT_ONDIVOPEN = 0x8;
	public static final int HICF_ARROWKEYS = 0x2;
	public static final int HICF_LEAVING = 0x20;
	public static final int HICF_MOUSE = 0x1;
	public static final int HINST_COMMCTRL = 0xffffffff;
	public static final int HKEY_CLASSES_ROOT = 0x80000000;
	public static final int HKEY_CURRENT_USER = 0x80000001;
	public static final int HKEY_LOCAL_MACHINE = 0x80000002;
	public static final int HORZRES = 0x8;
	public static final int HTBORDER = 0x12;
	public static final int HTCAPTION = 0x2;
	public static final int HTCLIENT = 0x1;
	public static final int HTERROR = -2;
	public static final int HTHSCROLL = 0x6;
	public static final int HTMENU = 0x5;
	public static final int HTNOWHERE = 0x0;
	public static final int HTSYSMENU = 0x3;
	public static final int HTTRANSPARENT = 0xffffffff;
	public static final int HTVSCROLL = 0x7;
	public static final int HWND_BOTTOM = 0x1;
	public static final int HWND_TOP = 0x0;
	public static final int HWND_TOPMOST = 0xffffffff;
	public static final int HWND_NOTOPMOST = -2;
	public static final int ICC_COOL_CLASSES = 0x400;
	public static final int ICC_DATE_CLASSES = 0x100;
	public static final int ICM_NOTOPEN = 0x0;
	public static final int ICON_BIG = 0x1;
	public static final int ICON_SMALL = 0x0;
	public static final int I_IMAGECALLBACK = -1;
	public static final int I_IMAGENONE = -2;
	public static final int IDABORT = 0x3;
	public static final int IDANI_CAPTION = 3;
	public static final int IDB_STD_SMALL_COLOR = 0x0;
	public static final int IDC_APPSTARTING = 0x7f8a;
	public static final int IDC_ARROW = 0x7f00;
	public static final int IDC_CROSS = 0x7f03;
	public static final int IDC_HAND = 0x7f89;
	public static final int IDC_HELP = 0x7f8b;
	public static final int IDC_IBEAM = 0x7f01;
	public static final int IDC_NO = 0x7f88;
	public static final int IDC_SIZE = 0x7f80;
	public static final int IDC_SIZEALL = 0x7f86;
	public static final int IDC_SIZENESW = 0x7f83;
	public static final int IDC_SIZENS = 0x7f85;
	public static final int IDC_SIZENWSE = 0x7f82;
	public static final int IDC_SIZEWE = 0x7f84;
	public static final int IDC_UPARROW = 0x7f04;
	public static final int IDC_WAIT = 0x7f02;
	public static final int IDI_APPLICATION = 32512;
	public static final int IDNO = 0x7;
	public static final int IDOK = 0x1;
	public static final int IDRETRY = 0x4;
	public static final int IDYES = 0x6;
	public static final int ILC_COLOR = 0x0;
	public static final int ILC_COLOR16 = 0x10;
	public static final int ILC_COLOR24 = 0x18;
	public static final int ILC_COLOR32 = 0x20;
	public static final int ILC_COLOR4 = 0x4;
	public static final int ILC_COLOR8 = 0x8;
	public static final int ILC_MASK = 0x1;
	public static final int ILC_MIRROR = 0x2000;
	public static final int ILD_NORMAL = 0x0;
	public static final int ILD_SELECTED = 0x4;
	public static final int IMAGE_BITMAP = 0x0;
	public static final int IMAGE_CURSOR = 0x2;
	public static final int IMAGE_ICON = 0x1;
	public static final int IME_CMODE_FULLSHAPE = 0x8;
	public static final int IME_CMODE_KATAKANA = 0x2;
	public static final int IME_CMODE_NATIVE = 0x1;
	public static final int IME_CMODE_ROMAN = 0x10;
	public static final int IMEMOUSE_LDOWN = 1;
	public static final int INFINITE = 0xffffffff;
	public static final int INPUT_KEYBOARD = 1;
	public static final int INPUT_MOUSE = 0;
	public static final int INTERNET_MAX_URL_LENGTH = 2084;
	public static final int INTERNET_OPTION_END_BROWSER_SESSION = 42;
	public static final int KEY_ENUMERATE_SUB_KEYS = 0x8;
	public static final int KEY_NOTIFY = 0x10;
	public static final int KEY_QUERY_VALUE = 0x1;
	public static final int KEY_READ = 0x20019;
	public static final int KEY_WRITE = 0x20006;
	public static final int KEYEVENTF_EXTENDEDKEY = 0x0001;
	public static final int KEYEVENTF_KEYUP = 0x0002;
	public static final int L_MAX_URL_LENGTH = 2084;
//	public static final int LANG_KOREAN = 0x12;
	public static final int LANG_NEUTRAL = 0x0;
	public static final int LANG_USER_DEFAULT = 1 << 10;
	public static final int LAYOUT_RTL = 0x1;
	public static final int LAYOUT_BITMAPORIENTATIONPRESERVED = 0x8;
	public static final int LBN_DBLCLK = 0x2;
	public static final int LBN_SELCHANGE = 0x1;
	public static final int LBS_EXTENDEDSEL = 0x800;
	public static final int LBS_MULTIPLESEL = 0x8;
	public static final int LBS_NOINTEGRALHEIGHT = 0x100;
	public static final int LBS_NOTIFY = 0x1;
	public static final int LB_ADDSTRING = 0x180;
	public static final int LB_DELETESTRING = 0x182;
	public static final int LB_ERR = 0xffffffff;
	public static final int LB_ERRSPACE = 0xfffffffe;
	public static final int LB_FINDSTRINGEXACT = 0x1a2;
	public static final int LB_GETCARETINDEX = 0x19f;
	public static final int LB_GETCOUNT = 0x18b;
	public static final int LB_GETCURSEL = 0x188;
	public static final int LB_GETHORIZONTALEXTENT = 0x193;
	public static final int LB_GETITEMHEIGHT = 0x1a1;
	public static final int LB_GETITEMRECT = 0x198;
	public static final int LB_GETSEL = 0x187;
	public static final int LB_GETSELCOUNT = 0x190;
	public static final int LB_GETSELITEMS = 0x191;
	public static final int LB_GETTEXT = 0x189;
	public static final int LB_GETTEXTLEN = 0x18a;
	public static final int LB_GETTOPINDEX = 0x18e;
	public static final int LB_INITSTORAGE = 0x1a8;
	public static final int LB_INSERTSTRING = 0x181;
	public static final int LB_RESETCONTENT = 0x184;
	public static final int LB_SELITEMRANGE = 0x19b;
	public static final int LB_SELITEMRANGEEX = 0x183;
	public static final int LB_SETANCHORINDEX = 0xf19c;
	public static final int LB_SETCARETINDEX = 0x19e;
	public static final int LB_SETCURSEL = 0x186;
	public static final int LB_SETHORIZONTALEXTENT = 0x194;
	public static final int LB_SETSEL = 0x185;
	public static final int LB_SETTOPINDEX = 0x197;
	public static final int LF_FULLFACESIZE = 64;
	public static final int LF_FACESIZE = 32;
	public static final int LGRPID_ARABIC = 0xd;
	public static final int LGRPID_HEBREW = 0xc;
	public static final int LGRPID_INSTALLED = 1;
	public static final int LIF_ITEMINDEX = 0x1;
	public static final int LIF_STATE = 0x2;
	public static final int LIS_FOCUSED = 0x1;
	public static final int LIS_ENABLED = 0x2;
	public static final int LISS_HOT = 0x2;
	public static final int LISS_SELECTED = 0x3;
	public static final int LISS_SELECTEDNOTFOCUS = 0x5;
	public static final int LM_GETIDEALHEIGHT = 0x701;
	public static final int LM_SETITEM = 0x702;
	public static final int LM_GETITEM = 0x703;
	public static final int LCID_SUPPORTED = 0x2;
	public static final int LOCALE_IDEFAULTANSICODEPAGE = 0x1004;
	public static final int LOCALE_IDATE = 0x00000021;
	public static final int LOCALE_ITIME = 0x00000023;
	public static final int LOCALE_RETURN_NUMBER = 0x20000000; // #if(WINVER >= 0x0400)
	public static final int LOCALE_S1159 = 0x00000028;	
	public static final int LOCALE_S2359 = 0x00000029;	
	public static final int LOCALE_SDECIMAL = 14;	
	public static final int LOCALE_SISO3166CTRYNAME = 0x5a;
	public static final int LOCALE_SISO639LANGNAME = 0x59;
	public static final int LOCALE_SLONGDATE = 0x00000020;
	public static final int LOCALE_SSHORTDATE = 0x0000001F;
	public static final int LOCALE_STIMEFORMAT = 0x00001003;
	public static final int LOCALE_SYEARMONTH = 0x00001006;  // #if(WINVER >= 0x0500)
	public static final int LOCALE_SDAYNAME1    = 0x0000002A;   // long name for Monday
	public static final int LOCALE_SDAYNAME2    = 0x0000002B;   // long name for Tuesday
	public static final int LOCALE_SDAYNAME3    = 0x0000002C;   // long name for Wednesday
	public static final int LOCALE_SDAYNAME4    = 0x0000002D;   // long name for Thursday
	public static final int LOCALE_SDAYNAME5    = 0x0000002E;   // long name for Friday
	public static final int LOCALE_SDAYNAME6    = 0x0000002F;   // long name for Saturday
	public static final int LOCALE_SDAYNAME7    = 0x00000030;   // long name for Sunday
	public static final int LOCALE_SMONTHNAME1  = 0x00000038;   // long name for January
	public static final int LOCALE_SMONTHNAME2  = 0x00000039;   // long name for February
	public static final int LOCALE_SMONTHNAME3  = 0x0000003A;   // long name for March
	public static final int LOCALE_SMONTHNAME4  = 0x0000003B;   // long name for April
	public static final int LOCALE_SMONTHNAME5  = 0x0000003C;   // long name for May
	public static final int LOCALE_SMONTHNAME6  = 0x0000003D;   // long name for June
	public static final int LOCALE_SMONTHNAME7  = 0x0000003E;   // long name for July
	public static final int LOCALE_SMONTHNAME8  = 0x0000003F;   // long name for August
	public static final int LOCALE_SMONTHNAME9  = 0x00000040;   // long name for September
	public static final int LOCALE_SMONTHNAME10 = 0x00000041;   // long name for October
	public static final int LOCALE_SMONTHNAME11 = 0x00000042;   // long name for November
	public static final int LOCALE_SMONTHNAME12 = 0x00000043;   // long name for December
	public static final int LOCALE_USER_DEFAULT = 1024;
	public static final int LOGPIXELSX = 0x58;
	public static final int LOGPIXELSY = 0x5a;
	public static final int LPSTR_TEXTCALLBACK = 0xffffffff;
	public static final int LR_DEFAULTCOLOR = 0x0;
	public static final int LR_SHARED = 0x8000;
	public static final int LVCFMT_BITMAP_ON_RIGHT = 0x1000;
	public static final int LVCFMT_CENTER = 0x2;
	public static final int LVCFMT_IMAGE = 0x800;
	public static final int LVCFMT_LEFT = 0x0;
	public static final int LVCFMT_RIGHT = 0x1;
	public static final int LVCF_FMT = 0x1;
	public static final int LVCF_IMAGE = 0x10;
	public static final int LVCFMT_JUSTIFYMASK = 0x3;
	public static final int LVCF_TEXT = 0x4;
	public static final int LVCF_WIDTH = 0x2;
	public static final int LVHT_ONITEM = 0xe;
	public static final int LVHT_ONITEMICON = 0x2;
	public static final int LVHT_ONITEMLABEL = 0x4;
	public static final int LVHT_ONITEMSTATEICON = 0x8;
	public static final int LVIF_IMAGE = 0x2;
	public static final int LVIF_INDENT = 0x10;
	public static final int LVIF_STATE = 0x8;
	public static final int LVIF_TEXT = 0x1;
	public static final int LVIM_AFTER = 0x00000001; 
	public static final int LVIR_BOUNDS = 0x0;
	public static final int LVIR_ICON = 0x1;
	public static final int LVIR_LABEL = 0x2;
	public static final int LVIR_SELECTBOUNDS = 0x3;
	public static final int LVIS_DROPHILITED = 0x8;
	public static final int LVIS_FOCUSED = 0x1;
	public static final int LVIS_SELECTED = 0x2;
	public static final int LVIS_STATEIMAGEMASK = 0xf000;
	public static final int LVM_FIRST = 0x1000;
	public static final int LVM_APPROXIMATEVIEWRECT = 0x1040;
	public static final int LVM_CREATEDRAGIMAGE = LVM_FIRST + 33;
	public static final int LVM_DELETEALLITEMS = 0x1009;
	public static final int LVM_DELETECOLUMN = 0x101c;
	public static final int LVM_DELETEITEM = 0x1008;
	public static final int LVM_ENSUREVISIBLE = 0x1013;
	public static final int LVM_GETBKCOLOR = 0x1000;
	public static final int LVM_GETCOLUMN = IsUnicode ? 0x105f : 0x1019;
	public static final int LVM_GETCOLUMNORDERARRAY = LVM_FIRST + 59;
	public static final int LVM_GETCOLUMNWIDTH = 0x101d;
	public static final int LVM_GETCOUNTPERPAGE = 0x1028;
	public static final int LVM_GETEXTENDEDLISTVIEWSTYLE = 0x1037;
	public static final int LVM_GETHEADER = 0x101f;
	public static final int LVM_GETIMAGELIST = 0x1002;
	public static final int LVM_GETITEM = IsUnicode ? 0x104b : 0x1005;
	public static final int LVM_GETITEMW = 0x104b;
	public static final int LVM_GETITEMA = 0x1005;
	public static final int LVM_GETITEMCOUNT = 0x1004;
	public static final int LVM_GETITEMRECT = 0x100e;
	public static final int LVM_GETITEMSTATE = 0x102c;
	public static final int LVM_GETNEXTITEM = 0x100c;
	public static final int LVM_GETSELECTEDCOLUMN = LVM_FIRST + 174;
	public static final int LVM_GETSELECTEDCOUNT = 0x1032;
	public static final int LVM_GETSTRINGWIDTH = IsUnicode ? 0x1057 : 0x1011;
	public static final int LVM_GETSUBITEMRECT = 0x1038;
	public static final int LVM_GETTEXTCOLOR = 0x1023;
	public static final int LVM_GETTOOLTIPS = 0x104e;
	public static final int LVM_GETTOPINDEX = 0x1027;
	public static final int LVM_HITTEST = 0x1012;
	public static final int LVM_INSERTCOLUMN = IsUnicode ? 0x1061 : 0x101b;
	public static final int LVM_INSERTITEM = IsUnicode ? 0x104d : 0x1007;
	public static final int LVM_REDRAWITEMS = LVM_FIRST + 21;
	public static final int LVM_SCROLL = 0x1014;
	public static final int LVM_SETBKCOLOR = 0x1001;
	public static final int LVM_SETCALLBACKMASK = LVM_FIRST + 11;
	public static final int LVM_SETCOLUMN = IsUnicode ? 0x1060 : 0x101a;
	public static final int LVM_SETCOLUMNORDERARRAY = LVM_FIRST + 58;
	public static final int LVM_SETCOLUMNWIDTH = 0x101e;
	public static final int LVM_SETEXTENDEDLISTVIEWSTYLE = 0x1036;
	public static final int LVM_SETIMAGELIST = 0x1003;
	public static final int LVM_SETINSERTMARK = LVM_FIRST + 166;
	public static final int LVM_SETITEM = IsUnicode ? 0x104c : 0x1006;
	public static final int LVM_SETITEMCOUNT = LVM_FIRST + 47;
	public static final int LVM_SETITEMSTATE = 0x102b;
	public static final int LVM_SETSELECTIONMARK = LVM_FIRST + 67;
	public static final int LVM_SETSELECTEDCOLUMN = LVM_FIRST + 140;
	public static final int LVM_SETTEXTBKCOLOR = 0x1026;
	public static final int LVM_SETTEXTCOLOR = 0x1024;
	public static final int LVM_SETTOOLTIPS = LVM_FIRST + 74;
	public static final int LVM_SUBITEMHITTEST = LVM_FIRST + 57;
	public static final int LVNI_FOCUSED = 0x1;
	public static final int LVNI_SELECTED = 0x2;
	public static final int LVN_BEGINDRAG = 0xffffff93;
	public static final int LVN_BEGINRDRAG = 0xffffff91;
	public static final int LVN_COLUMNCLICK = 0xffffff94;
	public static final int LVN_FIRST = 0xffffff9c;
	public static final int LVN_GETDISPINFOA = LVN_FIRST - 50;
	public static final int LVN_GETDISPINFOW = LVN_FIRST - 77;
	public static final int LVN_ITEMACTIVATE = 0xffffff8e;
	public static final int LVN_ITEMCHANGED = 0xffffff9b;
	public static final int LVN_MARQUEEBEGIN = 0xffffff64;
	public static final int LVN_ODFINDITEMA = LVN_FIRST - 52;
	public static final int LVN_ODFINDITEMW = LVN_FIRST - 79;
	public static final int LVN_ODSTATECHANGED = LVN_FIRST - 15;
	public static final int LVP_LISTITEM = 1;
	public static final int LVSCW_AUTOSIZE = 0xffffffff;
	public static final int LVSCW_AUTOSIZE_USEHEADER = 0xfffffffe;
	public static final int LVSICF_NOINVALIDATEALL = 0x1;
	public static final int LVSICF_NOSCROLL = 0x2;
	public static final int LVSIL_SMALL = 0x1;
	public static final int LVSIL_STATE = 0x2;
	public static final int LVS_EX_DOUBLEBUFFER = 0x10000;
	public static final int LVS_EX_FULLROWSELECT = 0x20;
	public static final int LVS_EX_GRIDLINES = 0x1;
	public static final int LVS_EX_HEADERDRAGDROP = 0x10;
	public static final int LVS_EX_LABELTIP = 0x4000;
	public static final int LVS_EX_ONECLICKACTIVATE = 0x40;
	public static final int LVS_EX_SUBITEMIMAGES = 0x2;
	public static final int LVS_EX_TRACKSELECT = 0x8;
	public static final int LVS_EX_TRANSPARENTBKGND = 0x800000;
	public static final int LVS_EX_TWOCLICKACTIVATE = 0x80;
	public static final int LVS_LIST = 0x3;
	public static final int LVS_NOCOLUMNHEADER = 0x4000;
	public static final int LVS_NOSCROLL = 0x2000;
	public static final int LVS_OWNERDATA = 0x1000;
	public static final int LVS_OWNERDRAWFIXED = 0x400;
	public static final int LVS_REPORT = 0x1;
	public static final int LVS_SHAREIMAGELISTS = 0x40;
	public static final int LVS_SHOWSELALWAYS = 0x8;
	public static final int LVS_SINGLESEL = 0x4;
	public static final int LWA_COLORKEY = 0x00000001;
	public static final int LWA_ALPHA = 0x00000002;
	public static final int MAX_LINKID_TEXT = 48;
//	public static final int MAX_PATH = 260;
	public static final int MA_NOACTIVATE = 0x3;
//	public static final int MANIFEST_RESOURCE_ID = 2;
	public static final int MB_ABORTRETRYIGNORE = 0x2;
	public static final int MB_APPLMODAL = 0x0;
	public static final int MB_ICONERROR = 0x10;
	public static final int MB_ICONINFORMATION = 0x40;
	public static final int MB_ICONQUESTION = 0x20;
	public static final int MB_ICONWARNING = 0x30;
	public static final int MB_OK = 0x0;
	public static final int MB_OKCANCEL = 0x1;
	public static final int MB_PRECOMPOSED = 0x1;
	public static final int MB_RETRYCANCEL = 0x5;
	public static final int MB_RIGHT = 0x00080000;
	public static final int MB_RTLREADING = 0x100000;
	public static final int MB_SYSTEMMODAL = 0x1000;
	public static final int MB_TASKMODAL = 0x2000;
	public static final int MB_TOPMOST = 0x00040000;
	public static final int MB_YESNO = 0x4;
	public static final int MB_YESNOCANCEL = 0x3;
	public static final int MCHT_CALENDAR = 0x20000;
	public static final int MCHT_CALENDARDATE = MCHT_CALENDAR | 0x0001;
	public static final int MCM_FIRST = 0x1000;
	public static final int MCM_GETCURSEL = MCM_FIRST + 1;
	public static final int MCM_GETMINREQRECT = MCM_FIRST + 9;
	public static final int MCM_HITTEST = MCM_FIRST + 14;
	public static final int MCM_SETCURSEL = MCM_FIRST + 2;
	public static final int MCN_FIRST = 0xFFFFFD12;
	public static final int MCN_SELCHANGE = MCN_FIRST + 1;
	public static final int MCN_SELECT = MCN_FIRST + 4;
	public static final int MCS_NOTODAY = 0x0010;
	public static final int MDIS_ALLCHILDSTYLES = 0x0001;
	public static final int MFS_CHECKED = 0x8;
	public static final int MFS_DISABLED = 0x3;
	public static final int MFS_GRAYED = 0x3;
	public static final int MFT_RADIOCHECK = 0x200;
	public static final int MFT_RIGHTJUSTIFY = 0x4000;
	public static final int MFT_RIGHTORDER = 0x2000; 
	public static final int MFT_SEPARATOR = 0x800;
	public static final int MFT_STRING = 0x0;
	public static final int MF_BYCOMMAND = 0x0;
	public static final int MF_BYPOSITION = 0x400;
	public static final int MF_CHECKED = 0x8;
	public static final int MF_DISABLED = 0x2;
	public static final int MF_ENABLED = 0x0;
	public static final int MF_GRAYED = 0x1;
	public static final int MF_HILITE = 0x80;
	public static final int MF_POPUP = 0x10;
	public static final int MF_SEPARATOR = 0x800;
	public static final int MF_SYSMENU = 0x2000;
	public static final int MF_UNCHECKED = 0x0;
	public static final int MIIM_BITMAP = 0x80;
	public static final int MIIM_DATA = 0x20;
	public static final int MIIM_FTYPE = 0x100;
	public static final int MIIM_ID = 0x2;
	public static final int MIIM_STATE = 0x1;
	public static final int MIIM_STRING = 0x40;
	public static final int MIIM_SUBMENU = 0x4;
	public static final int MIIM_TYPE = 0x10;
	public static final int MIM_BACKGROUND = 0x2;
	public static final int MIM_STYLE = 0x10;
	public static final int MK_ALT = 0x20;
	public static final int MK_CONTROL = 0x8;
	public static final int MK_LBUTTON = 0x1;
	public static final int MK_MBUTTON = 0x10;
	public static final int MK_RBUTTON = 0x2;
	public static final int MK_SHIFT = 0x4;
	public static final int MK_XBUTTON1 = 0x20;
	public static final int MK_XBUTTON2 = 0x40;
	public static final int MM_TEXT = 0x1;
	public static final int MNC_CLOSE = 0x1;
	public static final int MNS_CHECKORBMP = 0x4000000;
	public static final int MONITOR_DEFAULTTONEAREST = 0x2;
	public static final int MONITORINFOF_PRIMARY = 0x1;
	public static final String MONTHCAL_CLASS = "SysMonthCal32"; //$NON-NLS-1$
	public static final int MOUSEEVENTF_ABSOLUTE = 0x8000;
	public static final int MOUSEEVENTF_LEFTDOWN = 0x0002; 
	public static final int MOUSEEVENTF_LEFTUP = 0x0004; 
	public static final int MOUSEEVENTF_MIDDLEDOWN = 0x0020; 
	public static final int MOUSEEVENTF_MIDDLEUP = 0x0040; 
	public static final int MOUSEEVENTF_MOVE = 0x0001;
	public static final int MOUSEEVENTF_RIGHTDOWN = 0x0008; 
	public static final int MOUSEEVENTF_RIGHTUP = 0x0010;
	public static final int MOUSEEVENTF_VIRTUALDESK = 0x4000;
	public static final int MOUSEEVENTF_WHEEL = 0x0800;
	public static final int MOUSEEVENTF_XDOWN = 0x0080;
	public static final int MOUSEEVENTF_XUP = 0x0100;
	public static final int MSGF_DIALOGBOX = 0;
	public static final int MSGF_COMMCTRL_BEGINDRAG = 0x4200;
	public static final int MSGF_COMMCTRL_SIZEHEADER = 0x4201;
	public static final int MSGF_COMMCTRL_DRAGSELECT = 0x4202;
	public static final int MSGF_COMMCTRL_TOOLBARCUST = 0x4203;
	public static final int MSGF_MAINLOOP = 8;
	public static final int MSGF_MENU = 2;
	public static final int MSGF_MOVE = 3;
	public static final int MSGF_MESSAGEBOX = 1;
	public static final int MSGF_NEXTWINDOW = 6;
	public static final int MSGF_SCROLLBAR = 5;
	public static final int MSGF_SIZE = 4;
	public static final int MSGF_USER = 4096;
	public static final int MWMO_INPUTAVAILABLE = 0x4;
	public static final int MWT_LEFTMULTIPLY = 2;
	public static final int NI_COMPOSITIONSTR = 0x15;
	public static final int NID_READY = 0x80;
	public static final int NID_MULTI_INPUT = 0x40;	
	public static final int NIF_ICON = 0x00000002;
	public static final int NIF_INFO = 0x00000010;
	public static final int NIF_MESSAGE = 0x00000001;
	public static final int NIF_STATE = 0x00000008;
	public static final int NIF_TIP = 0x00000004;
	public static final int NIIF_ERROR = 0x00000003;
	public static final int NIIF_INFO = 0x00000001;
	public static final int NIIF_NONE = 0x00000000;
	public static final int NIIF_WARNING = 0x00000002;
	public static final int NIM_ADD = 0x00000000;
	public static final int NIM_DELETE = 0x00000002;
	public static final int NIM_MODIFY = 0x00000001;
	public static final int NIN_SELECT = 0x400 + 0;
	public static final int NINF_KEY = 0x1;
	public static final int NIN_KEYSELECT = NIN_SELECT | NINF_KEY;
	public static final int NIN_BALLOONSHOW = 0x400 + 2;
	public static final int NIN_BALLOONHIDE = 0x400 + 3;
	public static final int NIN_BALLOONTIMEOUT = 0x400 + 4;
	public static final int NIN_BALLOONUSERCLICK = 0x400 + 5;
	public static final int NIS_HIDDEN = 0x00000001;
	public static final int NM_FIRST = 0x0;
	public static final int NM_CLICK = 0xfffffffe;
	public static final int NM_CUSTOMDRAW = NM_FIRST - 12;
	public static final int NM_DBLCLK = 0xfffffffd;
	public static final int NM_RECOGNIZEGESTURE = NM_FIRST - 16;
	public static final int NM_RELEASEDCAPTURE = NM_FIRST - 16;
	public static final int NM_RETURN = 0xfffffffc;
	public static final int NOTIFYICONDATAA_V2_SIZE = NOTIFYICONDATAA_V2_SIZE ();
	public static final int NOTIFYICONDATAW_V2_SIZE = NOTIFYICONDATAW_V2_SIZE ();
	public static final int NOTIFYICONDATA_V2_SIZE = IsUnicode ? NOTIFYICONDATAW_V2_SIZE : NOTIFYICONDATAA_V2_SIZE;
	public static final int NOTSRCCOPY = 0x330008;
	public static final int NULLREGION = 0x1;
	public static final int NULL_BRUSH = 0x5;
	public static final int NULL_PEN = 0x8;
	public static final int NUMRESERVED = 106;
	public static final int OBJID_WINDOW = 0x00000000;
	public static final int OBJID_SYSMENU = 0xFFFFFFFF;
	public static final int OBJID_TITLEBAR = 0xFFFFFFFE;
	public static final int OBJID_MENU = 0xFFFFFFFD;
	public static final int OBJID_CLIENT = 0xFFFFFFFC;
	public static final int OBJID_VSCROLL = 0xFFFFFFFB;
	public static final int OBJID_HSCROLL = 0xFFFFFFFA;
	public static final int OBJID_SIZEGRIP = 0xFFFFFFF9;
	public static final int OBJID_CARET = 0xFFFFFFF8;
	public static final int OBJID_CURSOR = 0xFFFFFFF7;
	public static final int OBJID_ALERT = 0xFFFFFFF6;
	public static final int OBJID_SOUND = 0xFFFFFFF5;
	public static final int OBJID_QUERYCLASSNAMEIDX = 0xFFFFFFF4;
	public static final int OBJID_NATIVEOM = 0xFFFFFFF0;
	public static final int OBJ_BITMAP = 0x7;
	public static final int OBJ_FONT = 0x6;
	public static final int OBJ_PEN = 0x1;
	public static final int OBM_CHECKBOXES = 0x7ff7;
	public static final int ODS_SELECTED = 0x1;
	public static final int ODT_MENU = 0x1;
	public static final int OFN_ALLOWMULTISELECT = 0x200;
	public static final int OFN_EXPLORER = 0x80000;
	public static final int OFN_ENABLEHOOK = 0x20;
	public static final int OFN_ENABLESIZING = 0x800000;
	public static final int OFN_HIDEREADONLY = 0x4;
	public static final int OFN_NOCHANGEDIR = 0x8;
	public static final int OFN_OVERWRITEPROMPT = 0x2;
	public static final int OIC_BANG = 0x7F03;
	public static final int OIC_HAND = 0x7F01;
	public static final int OIC_INFORMATION = 0x7F04;
	public static final int OIC_QUES = 0x7F02;
	public static final int OIC_WINLOGO = 0x7F05;
	public static final int OPAQUE = 0x2;
	public static final int PATCOPY = 0xf00021;
	public static final int PATINVERT = 0x5a0049;
	public static final int PBM_GETPOS = 0x408;
	public static final int PBM_GETRANGE = 0x407;
	public static final int PBM_GETSTATE = 0x400 + 17;
	public static final int PBM_SETBARCOLOR = 0x409;
	public static final int PBM_SETBKCOLOR = 0x2001;
	public static final int PBM_SETMARQUEE = 0x400 + 10;
	public static final int PBM_SETPOS = 0x402;
	public static final int PBM_SETRANGE32 = 0x406;
	public static final int PBM_SETSTATE = 0x400 + 16;
	public static final int PBM_STEPIT = 0x405;
	public static final int PBS_MARQUEE = 0x08;
	public static final int PBS_SMOOTH = 0x1;
	public static final int PBS_VERTICAL = 0x4;
	public static final int PBS_NORMAL = 1;
	public static final int PBS_HOT = 2;
	public static final int PBS_PRESSED = 3; 
	public static final int PBS_DISABLED = 4;
	public static final int PBS_DEFAULTED = 5;
	public static final int PBST_NORMAL = 0x0001;
	public static final int PBST_ERROR = 0x0002;
	public static final int PBST_PAUSED = 0x0003;
	public static final int PD_ALLPAGES = 0x0;
	public static final int PD_COLLATE = 0x10;
	public static final int PD_PAGENUMS = 0x2;
	public static final int PD_PRINTTOFILE = 0x20;
	public static final int PD_RETURNDC = 0x100;
	public static final int PD_RETURNDEFAULT = 0x00000400;
	public static final int PD_SELECTION = 0x1;
	public static final int PD_USEDEVMODECOPIESANDCOLLATE = 0x40000;
	public static final int PT_CLOSEFIGURE = 1;
	public static final int PT_LINETO = 2;
	public static final int PT_BEZIERTO = 4;
	public static final int PT_MOVETO = 6;
	public static final int PFM_TABSTOPS = 0x10;
	public static final int PHYSICALHEIGHT = 0x6f;
	public static final int PHYSICALOFFSETX = 0x70;
	public static final int PHYSICALOFFSETY = 0x71;
	public static final int PHYSICALWIDTH = 0x6e;
	public static final int PLANES = 0xe;
	public static final int PM_NOREMOVE = 0x0;
	public static final int PM_NOYIELD = 0x2;
	public static final int QS_HOTKEY = 0x0080;
	public static final int QS_KEY = 0x0001;
	public static final int QS_MOUSEMOVE = 0x0002;
	public static final int QS_MOUSEBUTTON = 0x0004;
	public static final int QS_MOUSE = QS_MOUSEMOVE | QS_MOUSEBUTTON;
	public static final int QS_INPUT = QS_KEY | QS_MOUSE;
	public static final int QS_POSTMESSAGE = 0x0008;
	public static final int QS_TIMER = 0x0010;
	public static final int QS_PAINT = 0x0020;
	public static final int QS_SENDMESSAGE = 0x0040;
	public static final int QS_ALLINPUT = QS_MOUSEMOVE | QS_MOUSEBUTTON | QS_KEY | QS_POSTMESSAGE | QS_TIMER | QS_PAINT | QS_SENDMESSAGE;
	public static final int PM_QS_INPUT = QS_INPUT << 16;
	public static final int PM_QS_POSTMESSAGE = (QS_POSTMESSAGE | QS_HOTKEY | QS_TIMER) << 16;
	public static final int PM_QS_PAINT = QS_PAINT << 16;
	public static final int PM_QS_SENDMESSAGE = QS_SENDMESSAGE << 16;
	public static final int PM_REMOVE = 0x1;
	public static final String PROGRESS_CLASS = "msctls_progress32"; //$NON-NLS-1$
	public static final int PP_BAR = 1;
	public static final int PP_BARVERT = 2;
	public static final int PP_CHUNK = 3;
	public static final int PP_CHUNKVERT = 4;
	public static final int PRF_CHILDREN = 16;
	public static final int PRF_CLIENT = 0x4;
	public static final int PRF_ERASEBKGND = 0x8;
	public static final int PRF_NONCLIENT = 0x2;
	public static final int PROGRESSCHUNKSIZE = 2411;
	public static final int PROGRESSSPACESIZE = 2412;
	public static final int PS_DASH = 0x1;
	public static final int PS_DASHDOT = 0x3;
	public static final int PS_DASHDOTDOT = 0x4;
	public static final int PS_DOT = 0x2;
	public static final int PS_ENDCAP_FLAT = 0x200;
	public static final int PS_ENDCAP_SQUARE = 0x100;
	public static final int PS_ENDCAP_ROUND = 0x000;
	public static final int PS_ENDCAP_MASK = 0xF00;
	public static final int PS_GEOMETRIC = 0x10000;
	public static final int PS_JOIN_BEVEL = 0x1000;
	public static final int PS_JOIN_MASK = 0xF000;
	public static final int PS_JOIN_MITER = 0x2000;
	public static final int PS_JOIN_ROUND = 0x0000;
	public static final int PS_SOLID = 0x0;
	public static final int PS_STYLE_MASK = 0xf;
	public static final int PS_TYPE_MASK = 0x000f0000;
	public static final int PS_USERSTYLE = 0x7;
	public static final int R2_COPYPEN = 0xd;
	public static final int R2_XORPEN = 0x7;
	public static final int RASTERCAPS = 0x26;
	public static final int RASTER_FONTTYPE = 0x1;
	public static final int RBBIM_CHILD = 0x10;
	public static final int RBBIM_CHILDSIZE = 0x20;
	public static final int RBBIM_COLORS = 0x2;
	public static final int RBBIM_HEADERSIZE = 0x800;
	public static final int RBBIM_ID = 0x100;
	public static final int RBBIM_IDEALSIZE = 0x200;
	public static final int RBBIM_SIZE = 0x40;
	public static final int RBBIM_STYLE = 0x1;
	public static final int RBBIM_TEXT = 0x4;
	public static final int RBBS_BREAK = 0x1;
	public static final int RBBS_GRIPPERALWAYS = 0x80;
	public static final int RBBS_NOGRIPPER = 0x00000100;
	public static final int RBBS_USECHEVRON = 0x00000200;
	public static final int RBBS_VARIABLEHEIGHT = 0x40;
	public static final int RBN_FIRST = 0xfffffcc1;
	public static final int RBN_BEGINDRAG = RBN_FIRST - 4;
	public static final int RBN_CHILDSIZE = RBN_FIRST - 8;
	public static final int RBN_CHEVRONPUSHED = RBN_FIRST - 10;
	public static final int RBN_HEIGHTCHANGE = 0xfffffcc1;
	public static final int RBS_UNCHECKEDNORMAL = 1;
	public static final int RBS_UNCHECKEDHOT = 2;
	public static final int RBS_UNCHECKEDPRESSED = 3;
	public static final int RBS_UNCHECKEDDISABLED = 4;
	public static final int RBS_CHECKEDNORMAL = 5;
	public static final int RBS_CHECKEDHOT = 6;
	public static final int RBS_CHECKEDPRESSED = 7;
	public static final int RBS_CHECKEDDISABLED = 8;
	public static final int RBS_DBLCLKTOGGLE = 0x8000;
	public static final int RBS_BANDBORDERS = 0x400;
	public static final int RBS_VARHEIGHT = 0x200;
	public static final int RB_DELETEBAND = 0x402;
	public static final int RB_GETBANDBORDERS = 0x422;
	public static final int RB_GETBANDCOUNT = 0x40c;
	public static final int RB_GETBANDINFO = IsUnicode ? 0x41c : 0x41d;
	public static final int RB_GETBANDMARGINS = 0x428;
	public static final int RB_GETBARHEIGHT = 0x41b;
	public static final int RB_GETBKCOLOR = 0x414;
	public static final int RB_GETRECT = 0x409;
	public static final int RB_GETTEXTCOLOR = 0x416;
	public static final int RB_IDTOINDEX = 0x410;
	public static final int RB_INSERTBAND = IsUnicode ? 0x40a : 0x401;
	public static final int RB_MOVEBAND = 0x427;
	public static final int RB_SETBANDINFO = IsUnicode ? 0x40b : 0x406;
	public static final int RB_SETBKCOLOR = 0x413;
	public static final int RB_SETTEXTCOLOR = 0x415;
	public static final int RC_BITBLT = 0x1;
	public static final int RC_PALETTE = 0x100;
	public static final int RDW_ALLCHILDREN = 0x80;
	public static final int RDW_ERASE = 0x4;
	public static final int RDW_FRAME = 0x400;
	public static final int RDW_INVALIDATE = 0x1;
	public static final int RDW_UPDATENOW = 0x100;
	public static final int READ_CONTROL = 0x20000;
	public static final String REBARCLASSNAME = "ReBarWindow32"; //$NON-NLS-1$
	public static final int REG_DWORD = 4;
	public static final int REG_OPTION_VOLATILE = 0x1;
	public static final int RGN_AND = 0x1;
	public static final int RGN_COPY = 5;
	public static final int RGN_DIFF = 0x4;
	public static final int RGN_ERROR = 0;
	public static final int RGN_OR = 0x2;
	public static final int RP_BAND = 3;
	public static final int SBP_ARROWBTN = 0x1;
	public static final int SBP_THUMBBTNHORZ = 2;
	public static final int SBP_THUMBBTNVERT = 3;
	public static final int SBP_LOWERTRACKHORZ = 4;
	public static final int SBP_UPPERTRACKHORZ = 5;
	public static final int SBP_LOWERTRACKVERT = 6;
	public static final int SBP_UPPERTRACKVERT = 7;
	public static final int SBP_GRIPPERHORZ = 8;
	public static final int SBP_GRIPPERVERT = 9;
	public static final int SBP_SIZEBOX = 10;
	public static final int SBS_HORZ = 0x0;
	public static final int SBS_VERT = 0x1;
	public static final int SB_BOTH = 0x3;
	public static final int SB_BOTTOM = 0x7;
	public static final int SB_NONE = 0;
	public static final int SB_CONST_ALPHA = 0x00000001;
	public static final int SB_PIXEL_ALPHA = 0x00000002;
	public static final int SB_PREMULT_ALPHA = 0x00000004;
	public static final int SB_CTL = 0x2;
	public static final int SB_ENDSCROLL = 0x8;
	public static final int SB_HORZ = 0x0;
	public static final int SB_LINEDOWN = 0x1;
	public static final int SB_LINEUP = 0x0;
	public static final int SB_PAGEDOWN = 0x3;
	public static final int SB_PAGEUP = 0x2;
	public static final int SB_THUMBPOSITION = 0x4;
	public static final int SB_THUMBTRACK = 0x5;
	public static final int SB_TOP = 0x6;
	public static final int SB_VERT = 0x1;
	public static final int SCF_ALL = 0x4;
	public static final int SCF_DEFAULT = 0x0;
	public static final int SCF_SELECTION = 0x1;
	public static final int SC_CLOSE = 0xf060;
	public static final int SC_HSCROLL = 0xf080;
	public static final int SC_KEYMENU = 0xf100;
	public static final int SC_MAXIMIZE = 0xf030;
	public static final int SC_MINIMIZE = 0xf020;
	public static final int SC_NEXTWINDOW = 0xF040;
	public static final int SC_RESTORE = 0xf120;
	public static final int SC_SIZE = 0xf000;
	public static final int SC_TASKLIST = 0xf130;
	public static final int SC_VSCROLL = 0xf070;
	public static final int SCRBS_NORMAL = 1;
	public static final int SCRBS_HOT = 2;
	public static final int SCRBS_PRESSED = 3;
	public static final int SCRBS_DISABLED = 4;
	public static final int SEM_FAILCRITICALERRORS = 0x1;
	public static final int SET_FEATURE_ON_PROCESS = 0x2;
	public static final int SF_RTF = 0x2;
	public static final int SHADEBLENDCAPS = 120;
	public static final int SHCMBF_HIDDEN = 0x2;
	public static final int SHCMBM_OVERRIDEKEY = 0x400 + 403;
	public static final int SHCMBM_SETSUBMENU = 0x590;
	public static final int SHGFP_TYPE_CURRENT = 0;
	public static final int SHCMBM_GETSUBMENU = 0x591;
	public static final int SHGFI_ICON = 0x000000100;
	public static final int SHGFI_SMALLICON= 0x1;
	public static final int SHGFI_USEFILEATTRIBUTES = 0x000000010;
	public static final int SHMBOF_NODEFAULT = 0x1;
	public static final int SHMBOF_NOTIFY = 0x2;
	public static final int SHRG_RETURNCMD = 0x1;
	public static final int SIGDN_FILESYSPATH = 0x80058000;
	public static final int SIF_ALL = 0x17;
	public static final int SIF_DISABLENOSCROLL = 0x8;
	public static final int SIF_PAGE = 0x2;
	public static final int SIF_POS = 0x4;
	public static final int SIF_RANGE = 0x1;
	public static final int SIF_TRACKPOS = 0x10;
	public static final int SIP_DOWN = 1;
	public static final int SIP_UP = 0;
	public static final int SIPF_ON = 0x1;
	public static final int SIZE_RESTORED = 0;
	public static final int SIZE_MINIMIZED = 1;
	public static final int SIZE_MAXIMIZED = 2;
	public static final int SIZEPALETTE = 104;
	public static final int SM_CMONITORS = 80;
	public static final int SM_CXBORDER = 0x5;
	public static final int SM_CXCURSOR = 0xd;
	public static final int SM_CXDOUBLECLK = 36;
	public static final int SM_CYDOUBLECLK = 37;
	public static final int SM_CXEDGE = 0x2d;
	public static final int SM_CXFOCUSBORDER = 83;
	public static final int SM_CXHSCROLL = 0x15;
	public static final int SM_CXICON = 0x0b;
	public static final int SM_CYICON = 0x0c;
	public static final int SM_CXVIRTUALSCREEN = 78;
	public static final int SM_CYVIRTUALSCREEN = 79;
	public static final int SM_CXSMICON = 49;
	public static final int SM_CYSMICON = 50;
	public static final int SM_CXSCREEN = 0x0;
	public static final int SM_XVIRTUALSCREEN = 76;
	public static final int SM_YVIRTUALSCREEN = 77;
	public static final int SM_CXVSCROLL = 0x2;
	public static final int SM_CYBORDER = 0x6;
	public static final int SM_CYCURSOR = 0xe;
	public static final int SM_CYFOCUSBORDER = 84;
	public static final int SM_CYHSCROLL = 0x3;
	public static final int SM_CYMENU = 0xf;
	public static final int SM_CXMINTRACK = 34;
	public static final int SM_CYMINTRACK = 35;
	public static final int SM_CMOUSEBUTTONS = 43;
	public static final int SM_CYSCREEN = 0x1;
	public static final int SM_CYVSCROLL = 0x14;
	public static final int SM_DIGITIZER = 94;
	public static final int SM_MAXIMUMTOUCHES= 95;	
//	public static final int SM_DBCSENABLED = 0x2A;
//	public static final int SM_IMMENABLED = 0x52;
	public static final int SPI_GETFONTSMOOTHINGTYPE = 0x200A;
	public static final int SPI_GETHIGHCONTRAST = 66;
	public static final int SPI_GETWORKAREA = 0x30;
	public static final int SPI_GETMOUSEVANISH = 0x1020;
	public static final int SPI_GETNONCLIENTMETRICS = 41;
	public static final int SPI_GETWHEELSCROLLLINES = 104;
	public static final int SPI_GETCARETWIDTH = 0x2006;
	public static final int SPI_SETSIPINFO = 224;
	public static final int SPI_SETHIGHCONTRAST = 67;
	public static final int SRCAND = 0x8800c6;
	public static final int SRCCOPY = 0xcc0020;
	public static final int SRCINVERT = 0x660046;
	public static final int SRCPAINT = 0xee0086;
	public static final int SS_BITMAP = 0xe;
	public static final int SS_CENTER = 0x1;
	public static final int SS_CENTERIMAGE = 0x200;
	public static final int SS_EDITCONTROL = 0x2000;
	public static final int SS_ICON = 0x3;
	public static final int SS_LEFT = 0x0;
	public static final int SS_LEFTNOWORDWRAP = 0xc;
	public static final int SS_NOTIFY = 0x100;
	public static final int SS_OWNERDRAW = 0xd;
	public static final int SS_REALSIZEIMAGE = 0x800;
	public static final int SS_RIGHT = 0x2;
	public static final int SSA_FALLBACK = 0x00000020;
	public static final int SSA_GLYPHS = 0x00000080;
	public static final int SSA_METAFILE = 0x00000800;
	public static final int SSA_LINK = 0x00001000;
	public static final int STANDARD_RIGHTS_READ = 0x20000;
	public static final int STARTF_USESHOWWINDOW = 0x1;
	public static final int STATE_SYSTEM_INVISIBLE = 0x00008000;
	public static final int STATE_SYSTEM_OFFSCREEN = 0x00010000;
	public static final int STATE_SYSTEM_UNAVAILABLE = 0x00000001;
	public static final int STD_COPY = 0x1;
	public static final int STD_CUT = 0x0;
	public static final int STD_FILENEW = 0x6;
	public static final int STD_FILEOPEN = 0x7;
	public static final int STD_FILESAVE = 0x8;
	public static final int STD_PASTE = 0x2;
	public static final int STM_GETIMAGE = 0x173;
	public static final int STM_SETIMAGE = 0x172;
	public static final int SWP_ASYNCWINDOWPOS = 0x4000;
	public static final int SWP_DRAWFRAME = 0x20;
	public static final int SWP_NOACTIVATE = 0x10;
	public static final int SWP_NOCOPYBITS = 0x100;
	public static final int SWP_NOMOVE = 0x2;
	public static final int SWP_NOREDRAW = 0x8;
	public static final int SWP_NOSIZE = 0x1;
	public static final int SWP_NOZORDER = 0x4;
	public static final int SW_ERASE = 0x4;
	public static final int SW_HIDE = 0x0;
	public static final int SW_INVALIDATE = 0x2;
	public static final int SW_MINIMIZE = 0x6;
	public static final int SW_PARENTOPENING = 0x3;
	public static final int SW_RESTORE = IsWinCE ? 0xd : 0x9;
	public static final int SW_SCROLLCHILDREN = 0x1;
	public static final int SW_SHOW = 0x5;
	public static final int SW_SHOWMAXIMIZED = IsWinCE ? 0xb : 0x3;
	public static final int SW_SHOWMINIMIZED = 0x2;
	public static final int SW_SHOWMINNOACTIVE = 0x7;
	public static final int SW_SHOWNA = 0x8;
	public static final int SW_SHOWNOACTIVATE = 0x4;
	public static final int SYNCHRONIZE = 0x100000;
	public static final int SYSRGN = 0x4;
	public static final int SYSTEM_FONT = 0xd;
	public static final int S_OK = 0x0;
	public static final int TABP_TABITEM = 1;
	public static final int TABP_TABITEMLEFTEDGE = 2;
	public static final int TABP_TABITEMRIGHTEDGE = 3;
	public static final int TABP_TABITEMBOTHEDGE = 4;
	public static final int TABP_TOPTABITEM = 5;
	public static final int TABP_TOPTABITEMLEFTEDGE = 6;
	public static final int TABP_TOPTABITEMRIGHTEDGE = 7;
	public static final int TABP_TOPTABITEMBOTHEDGE = 8;
	public static final int TABP_PANE = 9;
	public static final int TABP_BODY = 10;
	public static final int TBIF_COMMAND = 0x20;
	public static final int TBIF_STATE = 0x4;
	public static final int TBIF_IMAGE = 0x1;
	public static final int TBIF_LPARAM = 0x10;
	public static final int TBIF_SIZE = 0x40;
	public static final int TBIF_STYLE = 0x8;
	public static final int TBIF_TEXT = 0x2;
	public static final int TB_GETEXTENDEDSTYLE = 0x400 + 85;
	public static final int TBM_GETLINESIZE = 0x418;
	public static final int TBM_GETPAGESIZE = 0x416;
	public static final int TBM_GETPOS = 0x400;
	public static final int TBM_GETRANGEMAX = 0x402;
	public static final int TBM_GETRANGEMIN = 0x401;
	public static final int TBM_GETTHUMBRECT = 0x419;
	public static final int TBM_SETLINESIZE = 0x417;
	public static final int TBM_SETPAGESIZE = 0x415;
	public static final int TBM_SETPOS = 0x405;
	public static final int TBM_SETRANGEMAX = 0x408;
	public static final int TBM_SETRANGEMIN = 0x407;
	public static final int TBM_SETTICFREQ = 0x414;
	public static final int TBN_DROPDOWN = 0xfffffd3a;
	public static final int TBN_FIRST = 0xfffffd44;
	public static final int TBN_HOTITEMCHANGE = 0xFFFFFD37;
	public static final int TBSTATE_CHECKED = 0x1;
	public static final int TBSTATE_PRESSED = 0x02;
	public static final int TBSTYLE_CUSTOMERASE = 0x2000;
	public static final int TBSTYLE_DROPDOWN = 0x8;
	public static final int TBSTATE_ENABLED = 0x4;
	public static final int TBSTYLE_AUTOSIZE = 0x10;
	public static final int TBSTYLE_EX_DOUBLEBUFFER = 0x80; 
	public static final int TBSTYLE_EX_DRAWDDARROWS = 0x1;
	public static final int TBSTYLE_EX_HIDECLIPPEDBUTTONS = 0x10;
	public static final int TBSTYLE_EX_MIXEDBUTTONS = 0x8;
	public static final int TBSTYLE_FLAT = 0x800;
	public static final int TBSTYLE_LIST = 0x1000;
	public static final int TBSTYLE_TOOLTIPS = 0x100;
	public static final int TBSTYLE_TRANSPARENT = 0x8000;
	public static final int TBSTYLE_WRAPABLE = 0x200;
	public static final int TBS_AUTOTICKS = 0x1;
	public static final int TBS_BOTH = 0x8;
	public static final int TBS_DOWNISLEFT = 0x0400;
	public static final int TBS_HORZ = 0x0;
	public static final int TBS_VERT = 0x2;
	public static final int TB_ADDSTRING = IsUnicode ? 0x44d : 0x41c;
	public static final int TB_AUTOSIZE = 0x421;
	public static final int TB_BUTTONCOUNT = 0x418;
	public static final int TB_BUTTONSTRUCTSIZE = 0x41e;
	public static final int TB_COMMANDTOINDEX = 0x419;
	public static final int TB_DELETEBUTTON = 0x416;
	public static final int TB_ENDTRACK = 0x8;
	public static final int TB_GETBUTTON = 0x417;
	public static final int TB_GETBUTTONINFO = IsUnicode ? 0x43f : 0x441;
	public static final int TB_GETBUTTONSIZE = 0x43a;
	public static final int TB_GETBUTTONTEXT = IsUnicode ? 0x44b : 0x42d;
	public static final int TB_GETDISABLEDIMAGELIST = 0x437;
	public static final int TB_GETHOTIMAGELIST = 0x435;
	public static final int TB_GETHOTITEM = 0x0400 + 71;
	public static final int TB_GETIMAGELIST = 0x431;
	public static final int TB_GETITEMRECT = 0x41d;
	public static final int TB_GETPADDING = 0x0400 + 86;
	public static final int TB_GETROWS = 0x428;
	public static final int TB_GETSTATE = 0x412;
	public static final int TB_GETTOOLTIPS = 0x423;
	public static final int TB_INSERTBUTTON = IsUnicode ? 0x443 : 0x415;
	public static final int TB_LOADIMAGES = 0x432;
	public static final int TB_MAPACCELERATOR = 0x0400 + (IsUnicode ? 90 : 78);
	public static final int TB_SETBITMAPSIZE = 0x420;
	public static final int TB_SETBUTTONINFO = IsUnicode ? 0x440 : 0x442;
	public static final int TB_SETBUTTONSIZE = 0x41f;
	public static final int TB_SETDISABLEDIMAGELIST = 0x436;
	public static final int TB_SETEXTENDEDSTYLE = 0x454;
	public static final int TB_SETHOTIMAGELIST = 0x434;
	public static final int TB_SETHOTITEM =  0x0400 + 72;
	public static final int TB_SETIMAGELIST = 0x430;
	public static final int TB_SETPARENT = 0x400 + 37;
	public static final int TB_SETROWS = 0x427;
	public static final int TB_SETSTATE = 0x411;
	public static final int TB_THUMBPOSITION = 0x4;
	public static final int TBPF_NOPROGRESS = 0x0;
	public static final int TBPF_INDETERMINATE = 0x1;
	public static final int TBPF_NORMAL = 0x2;
	public static final int TBPF_ERROR = 0x4;
	public static final int TBPF_PAUSED = 0x8;
	public static final int TCIF_IMAGE = 0x2;
	public static final int TCIF_TEXT = 0x1;
	public static final int TCI_SRCCHARSET = 0x1;
	public static final int TCI_SRCCODEPAGE = 0x2;
	public static final int TCM_ADJUSTRECT = 0x1328;
	public static final int TCM_DELETEITEM = 0x1308;
	public static final int TCM_GETCURSEL = 0x130b;
	public static final int TCM_GETITEMCOUNT = 0x1304;
	public static final int TCM_GETITEMRECT = 0x130a;
	public static final int TCM_GETTOOLTIPS = 0x132d;
	public static final int TCM_HITTEST = 0x130d;
	public static final int TCM_INSERTITEM = IsUnicode ? 0x133e : 0x1307;
	public static final int TCM_SETCURSEL = 0x130c;
	public static final int TCM_SETIMAGELIST = 0x1303;
	public static final int TCM_SETITEM = IsUnicode ? 0x133d : 0x1306;
	public static final int TCN_SELCHANGE = 0xfffffdd9;
	public static final int TCN_SELCHANGING = 0xfffffdd8;
	public static final int TCS_BOTTOM = 0x0002;
	public static final int TCS_FOCUSNEVER = 0x8000;
	public static final int TCS_MULTILINE = 0x200;
	public static final int TCS_TABS = 0x0;
	public static final int TCS_TOOLTIPS = 0x4000;
	public static final int TECHNOLOGY = 0x2;
	public static final int TF_ATTR_INPUT = 0;
	public static final int TF_ATTR_TARGET_CONVERTED = 1;
	public static final int TF_ATTR_CONVERTED = 2;
	public static final int TF_ATTR_TARGET_NOTCONVERTED = 3;
	public static final int TF_ATTR_INPUT_ERROR = 4;
	public static final int TF_ATTR_FIXEDCONVERTED = 5;
	public static final int TF_ATTR_OTHER = -1;
	public static final int TF_CT_NONE = 0;
	public static final int TF_CT_SYSCOLOR = 1;
	public static final int TF_CT_COLORREF = 2;
	public static final int TF_LS_NONE = 0;
	public static final int TF_LS_SOLID = 1;
	public static final int TF_LS_DOT = 2;
	public static final int TF_LS_DASH = 3;
	public static final int TF_LS_SQUIGGLE = 4;
	public static final int TIME_NOSECONDS = 0x2;
	public static final int TIS_NORMAL = 1;
	public static final int TIS_HOT = 2;
	public static final int TIS_SELECTED = 3;
	public static final int TIS_DISABLED = 4;
	public static final int TIS_FOCUSED = 5;
	public static final int TKP_TRACK = 1;
	public static final int TKP_TRACKVERT = 2;
	public static final int TKP_THUMB = 3;
	public static final int TKP_THUMBBOTTOM = 4;
	public static final int TKP_THUMBTOP = 5;
	public static final int TKP_THUMBVERT = 6;
	public static final int TKP_THUMBLEFT = 7;
	public static final int TKP_THUMBRIGHT = 8;
	public static final int TKP_TICS = 9;
	public static final int TKP_TICSVERT = 10;
	public static final int TME_HOVER = 0x1;
	public static final int TME_LEAVE = 0x2;
	public static final int TME_QUERY = 0x40000000;
	public static final int TMPF_VECTOR = 0x2;
	public static final int TMT_CONTENTMARGINS = 3602;
	public static final int TOUCHEVENTF_MOVE = 0x0001;
	public static final int TOUCHEVENTF_DOWN = 0x0002;
	public static final int TOUCHEVENTF_UP = 0x0004;
	public static final int TOUCHEVENTF_INRANGE = 0x0008;
	public static final int TOUCHEVENTF_PRIMARY = 0x0010;
	public static final int TOUCHEVENTF_NOCOALESCE = 0x0020;
	public static final int TOUCHEVENTF_PALM = 0x0080;	
	public static final String TOOLBARCLASSNAME = "ToolbarWindow32"; //$NON-NLS-1$
	public static final String TOOLTIPS_CLASS = "tooltips_class32"; //$NON-NLS-1$
	public static final int TP_BUTTON = 1;
	public static final int TP_DROPDOWNBUTTON = 2;
	public static final int TP_SPLITBUTTON = 3;
	public static final int TP_SPLITBUTTONDROPDOWN = 4;
	public static final int TP_SEPARATOR = 5;
	public static final int TP_SEPARATORVERT = 6;
	public static final int TPM_LEFTALIGN = 0x0;
	public static final int TPM_LEFTBUTTON = 0x0;
	public static final int TPM_RIGHTBUTTON = 0x2;
	public static final int TPM_RIGHTALIGN = 0x8;
	public static final String TRACKBAR_CLASS = "msctls_trackbar32"; //$NON-NLS-1$
	public static final int TRANSPARENT = 0x1;
	public static final int TREIS_DISABLED = 4;
	public static final int TREIS_HOT = 2;
	public static final int TREIS_NORMAL = 1;
	public static final int TREIS_SELECTED = 3;
	public static final int TREIS_SELECTEDNOTFOCUS = 5;
	public static final int TS_MIN = 0;
	public static final int TS_TRUE = 1;
	public static final int TS_DRAW = 2;
	public static final int TS_NORMAL = 1;
	public static final int TS_HOT = 2;
	public static final int TS_PRESSED = 3;
	public static final int TS_DISABLED = 4;
	public static final int TS_CHECKED = 5;
	public static final int TS_HOTCHECKED = 6;
	public static final int TTDT_AUTOMATIC = 0;
	public static final int TTDT_RESHOW = 1;
	public static final int TTDT_AUTOPOP = 2;
	public static final int TTDT_INITIAL = 3;
	public static final int TTF_ABSOLUTE = 0x80;
	public static final int TTF_IDISHWND = 0x1;
	public static final int TTF_SUBCLASS = 0x10;
	public static final int TTF_RTLREADING = 0x4;
	public static final int TTF_TRACK = 0x20;
	public static final int TTF_TRANSPARENT = 0x100;
	public static final int TTI_NONE = 0;
	public static final int TTI_INFO = 1;
	public static final int TTI_WARNING = 2;
	public static final int TTI_ERROR= 3;
	public static final int TTM_ACTIVATE = 0x400 + 1;
	public static final int TTM_ADDTOOL = IsUnicode ? 0x432 : 0x404;
	public static final int TTM_ADJUSTRECT = 0x400 + 31;
	public static final int TTM_GETCURRENTTOOLA = 0x400 + 15;
	public static final int TTM_GETCURRENTTOOLW = 0x400 + 59;
	public static final int TTM_GETCURRENTTOOL = 0x400 + (IsUnicode ? 59 : 15);
	public static final int TTM_GETDELAYTIME = 0x400 + 21;
	public static final int TTM_DELTOOL = IsUnicode ? 0x433 : 0x405;
	public static final int TTM_GETTOOLINFO = 0x400 + (IsUnicode ? 53 : 8);
	public static final int TTM_NEWTOOLRECT = 0x400 + (IsUnicode ? 52 : 6);
	public static final int TTM_POP = 0x400 + 28; 
	public static final int TTM_SETDELAYTIME = 0x400 + 3;
	public static final int TTM_SETMAXTIPWIDTH = 0x418;
	public static final int TTM_SETTITLEA = 0x400 + 32;
	public static final int TTM_SETTITLEW = 0x400 + 33;
	public static final int TTM_SETTITLE = 0x400 + (IsUnicode ? 33 : 32);
	public static final int TTM_TRACKPOSITION = 1042;
	public static final int TTM_TRACKACTIVATE = 1041;
	public static final int TTM_UPDATE = 0x41D;
	public static final int TTN_FIRST = 0xfffffdf8;
	public static final int TTN_GETDISPINFO = IsUnicode ? 0xfffffdee : 0xfffffdf8;
	public static final int TTN_GETDISPINFOW = 0xfffffdee;
	public static final int TTN_GETDISPINFOA = 0xfffffdf8;
	public static final int TTN_POP = TTN_FIRST - 2;
	public static final int TTN_SHOW = TTN_FIRST - 1;
	public static final int TTS_ALWAYSTIP = 0x1;
	public static final int TTS_BALLOON = 0x40;
	public static final int TTS_NOANIMATE = 0x10;
	public static final int TTS_NOFADE = 0x20;
	public static final int TTS_NOPREFIX = 0x02;
	public static final int TV_FIRST = 0x1100;
	public static final int TVE_COLLAPSE = 0x1;
	public static final int TVE_COLLAPSERESET = 0x8000;
	public static final int TVE_EXPAND = 0x2;
	public static final int TVGN_CARET = 0x9;
	public static final int TVGN_CHILD = 0x4;
	public static final int TVGN_DROPHILITED = 0x8;
	public static final int TVGN_FIRSTVISIBLE = 0x5;
	public static final int TVGN_LASTVISIBLE = 0xa;
	public static final int TVGN_NEXT = 0x1;
	public static final int TVGN_NEXTVISIBLE = 0x6;
	public static final int TVGN_PARENT = 0x3;
	public static final int TVGN_PREVIOUS = 0x2;
	public static final int TVGN_PREVIOUSVISIBLE = 0x7;
	public static final int TVGN_ROOT = 0x0;
	public static final int TVHT_ONITEM = 0x46;
	public static final int TVHT_ONITEMBUTTON = 16;
	public static final int TVHT_ONITEMICON = 0x2;
	public static final int TVHT_ONITEMINDENT = 0x8;
	public static final int TVHT_ONITEMRIGHT = 0x20;
	public static final int TVHT_ONITEMLABEL = 0x4;
	public static final int TVHT_ONITEMSTATEICON = 0x40;
	public static final int TVIF_HANDLE = 0x10;
	public static final int TVIF_IMAGE = 0x2;
	public static final int TVIF_INTEGRAL = 0x0080;
	public static final int TVIF_PARAM = 0x4;
	public static final int TVIF_SELECTEDIMAGE = 0x20;
	public static final int TVIF_STATE = 0x8;
	public static final int TVIF_TEXT = 0x1;
	public static final int TVIS_DROPHILITED = 0x8;
	public static final int TVIS_EXPANDED = 0x20;
	public static final int TVIS_SELECTED = 0x2;
	public static final int TVIS_STATEIMAGEMASK = 0xf000;
	public static final long /*int*/ TVI_FIRST = -0x0FFFF;
	public static final long /*int*/ TVI_LAST = -0x0FFFE;
	public static final long /*int*/ TVI_ROOT = -0x10000;
	public static final long /*int*/ TVI_SORT = -0x0FFFD;
	public static final int TVM_CREATEDRAGIMAGE = TV_FIRST + 18;
	public static final int TVM_DELETEITEM = 0x1101;
	public static final int TVM_ENSUREVISIBLE = 0x1114;
	public static final int TVM_EXPAND = 0x1102;
	public static final int TVM_GETBKCOLOR = 0x111f;
	public static final int TVM_GETCOUNT = 0x1105;
	public static final int TVM_GETEXTENDEDSTYLE = TV_FIRST + 45;
	public static final int TVM_GETIMAGELIST = 0x1108;
	public static final int TVM_GETITEM = IsUnicode ? 0x113e : 0x110c;
	public static final int TVM_GETITEMHEIGHT = 0x111c;
	public static final int TVM_GETITEMRECT = 0x1104;
	public static final int TVM_GETITEMSTATE = TV_FIRST + 39;
	public static final int TVM_GETNEXTITEM = 0x110a;
	public static final int TVM_GETTEXTCOLOR = 0x1120;
	public static final int TVM_GETTOOLTIPS = TV_FIRST + 25;
	public static final int TVM_GETVISIBLECOUNT = TV_FIRST + 16;
	public static final int TVM_HITTEST = 0x1111;
	public static final int TVM_INSERTITEM = IsUnicode ? 0x1132 : 0x1100;
	public static final int TVM_MAPACCIDTOHTREEITEM = TV_FIRST + 42;
	public static final int TVM_MAPHTREEITEMTOACCID = TV_FIRST + 43;
	public static final int TVM_SELECTITEM = 0x110b;
	public static final int TVM_SETBKCOLOR = 0x111d;
	public static final int TVM_SETEXTENDEDSTYLE = TV_FIRST + 44;
	public static final int TVM_SETIMAGELIST = 0x1109;
	public static final int TVM_SETINSERTMARK = 0x111a;
	public static final int TVM_SETITEM = IsUnicode ? 0x113f : 0x110d;
	public static final int TVM_SETITEMHEIGHT = TV_FIRST + 27;
	public static final int TVM_SETSCROLLTIME = TV_FIRST + 33;
	public static final int TVM_SETTEXTCOLOR = 0x111e;
	public static final int TVM_SORTCHILDREN = TV_FIRST + 19;
	public static final int TVM_SORTCHILDRENCB = TV_FIRST + 21;
	public static final int TVN_BEGINDRAGW = 0xfffffe38;
	public static final int TVN_BEGINDRAGA = 0xfffffe69;
	public static final int TVN_BEGINRDRAGW = 0xfffffe37;
	public static final int TVN_BEGINRDRAGA = 0xfffffe68;
	public static final int TVN_FIRST = 0xfffffe70;
	public static final int TVN_GETDISPINFOA = TVN_FIRST - 3;
	public static final int TVN_GETDISPINFOW = TVN_FIRST - 52;
	public static final int TVN_ITEMCHANGINGW = TVN_FIRST - 17;
	public static final int TVN_ITEMCHANGINGA = TVN_FIRST - 16;
	public static final int TVN_ITEMEXPANDEDA = TVN_FIRST -6;
	public static final int TVN_ITEMEXPANDEDW = TVN_FIRST - 55;
	public static final int TVN_ITEMEXPANDINGW = 0xfffffe3a;
	public static final int TVN_ITEMEXPANDINGA = 0xfffffe6b;
	public static final int TVN_SELCHANGEDW = 0xfffffe3d;
	public static final int TVN_SELCHANGEDA = 0xfffffe6e;
	public static final int TVN_SELCHANGINGW = 0xfffffe3e;
	public static final int TVN_SELCHANGINGA = 0xfffffe6f;
	public static final int TVP_GLYPH = 2;
	public static final int TVP_TREEITEM = 1;
	public static final int TVSIL_NORMAL = 0x0;
	public static final int TVSIL_STATE = 0x2;
	public static final int TVS_DISABLEDRAGDROP = 0x10;
	public static final int TVS_EX_AUTOHSCROLL = 0x0020;
	public static final int TVS_EX_DOUBLEBUFFER = 0x0004;
	public static final int TVS_EX_DIMMEDCHECKBOXES = 0x0200;
	public static final int TVS_EX_DRAWIMAGEASYNC = 0x0400;
	public static final int TVS_EX_EXCLUSIONCHECKBOXES = 0x0100;
	public static final int TVS_EX_FADEINOUTEXPANDOS = 0x0040;
	public static final int TVS_EX_MULTISELECT = 0x0002;
	public static final int TVS_EX_NOINDENTSTATE = 0x0008;
	public static final int TVS_EX_PARTIALCHECKBOXES = 0x0080;
	public static final int TVS_EX_RICHTOOLTIP = 0x0010;
	public static final int TVS_FULLROWSELECT = 0x1000;
	public static final int TVS_HASBUTTONS = 0x1;
	public static final int TVS_HASLINES = 0x2;
	public static final int TVS_LINESATROOT = 0x4;
	public static final int TVS_NOHSCROLL = 0x8000;
	public static final int TVS_NONEVENHEIGHT = 0x4000;
	public static final int TVS_NOSCROLL = 0x2000;
	public static final int TVS_NOTOOLTIPS = 0x80;
	public static final int TVS_SHOWSELALWAYS = 0x20;
	public static final int TVS_TRACKSELECT = 0x200;
	public static final int UDM_GETACCEL = 0x046C;
	public static final int UDM_GETRANGE32 = 0x0470;
	public static final int UDM_GETPOS = 0x468;
	public static final int UDM_GETPOS32 = 0x0472;
	public static final int UDM_SETACCEL = 0x046B;
	public static final int UDM_SETRANGE32 = 0x046f;
	public static final int UDM_SETPOS = 0x467;
	public static final int UDM_SETPOS32 = 0x0471;
	public static final int UDN_DELTAPOS = -722;
	public static final int UDS_ALIGNLEFT = 0x008;
	public static final int UDS_ALIGNRIGHT = 0x004;
	public static final int UDS_AUTOBUDDY = 0x0010;
	public static final int UDS_WRAP = 0x0001;
	public static final int UIS_CLEAR = 2;
	public static final int UIS_INITIALIZE = 3;
	public static final int UIS_SET = 1;
	public static final int UISF_HIDEACCEL = 0x2;
	public static final int UISF_HIDEFOCUS = 0x1;
	public static final String UPDOWN_CLASS = "msctls_updown32"; //$NON-NLS-1$
	public static final int USP_E_SCRIPT_NOT_IN_FONT = 0x80040200;
	public static final int VERTRES = 0xa;
	public static final int VK_BACK = 0x8;
	public static final int VK_CANCEL = 0x3;
	public static final int VK_CAPITAL = 0x14;
	public static final int VK_CONTROL = 0x11;
	public static final int VK_DECIMAL = 0x6E;
	public static final int VK_DELETE = 0x2e;
	public static final int VK_DIVIDE = 0x6f;
	public static final int VK_DOWN = 0x28;
	public static final int VK_END = 0x23;
	public static final int VK_ESCAPE = 0x1b;
	public static final int VK_F1 = 0x70;
	public static final int VK_F10 = 0x79;
	public static final int VK_F11 = 0x7a;
	public static final int VK_F12 = 0x7b;
	public static final int VK_F13 = 0x7c;
	public static final int VK_F14 = 0x7d;
	public static final int VK_F15 = 0x7e;
	public static final int VK_F16 = 0x7F;
	public static final int VK_F17 = 0x80;
	public static final int VK_F18 = 0x81;
	public static final int VK_F19 = 0x82;
	public static final int VK_F20 = 0x83;
	public static final int VK_F2 = 0x71;
	public static final int VK_F3 = 0x72;
	public static final int VK_F4 = 0x73;
	public static final int VK_F5 = 0x74;
	public static final int VK_F6 = 0x75;
	public static final int VK_F7 = 0x76;
	public static final int VK_F8 = 0x77;
	public static final int VK_F9 = 0x78;
	public static final int VK_HOME = 0x24;
	public static final int VK_INSERT = 0x2d;
	public static final int VK_L = 0x4c;
	public static final int VK_LBUTTON = 0x1;
	public static final int VK_LEFT = 0x25;
	public static final int VK_LCONTROL = 0xA2;
	public static final int VK_LMENU = 0xA4;
	public static final int VK_LSHIFT = 0xA0;
	public static final int VK_MBUTTON = 0x4;
	public static final int VK_MENU = 0x12;
	public static final int VK_MULTIPLY = 0x6A;
	public static final int VK_N = 0x4e;
	public static final int VK_O = 0x4f;
	public static final int VK_NEXT = 0x22;
	public static final int VK_NUMLOCK = 0x90;
	public static final int VK_NUMPAD0 = 0x60;
	public static final int VK_NUMPAD1 = 0x61;
	public static final int VK_NUMPAD2 = 0x62;
	public static final int VK_NUMPAD3 = 0x63;
	public static final int VK_NUMPAD4 = 0x64;
	public static final int VK_NUMPAD5 = 0x65;
	public static final int VK_NUMPAD6 = 0x66;
	public static final int VK_NUMPAD7 = 0x67;
	public static final int VK_NUMPAD8 = 0x68;
	public static final int VK_NUMPAD9 = 0x69;
	public static final int VK_PAUSE = 0x13;
	public static final int VK_PRIOR = 0x21;
	public static final int VK_RBUTTON = 0x2;
	public static final int VK_RETURN = 0xd;
	public static final int VK_RIGHT = 0x27;
	public static final int VK_RCONTROL = 0xA3;
	public static final int VK_RMENU = 0xA5;
	public static final int VK_RSHIFT = 0xA1;
	public static final int VK_SCROLL = 0x91;
	public static final int VK_SEPARATOR = 0x6C;
	public static final int VK_SHIFT = 0x10;
	public static final int VK_SNAPSHOT = 0x2C;
	public static final int VK_SPACE = 0x20;
	public static final int VK_SUBTRACT = 0x6D;
	public static final int VK_TAB = 0x9;
	public static final int VK_UP = 0x26;
	public static final int VK_XBUTTON1 = 0x05;
	public static final int VK_XBUTTON2 = 0x06;
	public static final int VK_ADD = 0x6B;
	public static final int VK_APP1 = 0xc1;
	public static final int VK_APP2 = 0xc2;
	public static final int VK_APP3 = 0xc3;
	public static final int VK_APP4 = 0xc4;
	public static final int VK_APP5 = 0xc5;
	public static final int VK_APP6 = 0xc6;
	public static final int VT_BOOL = 11;
	public static final int VT_LPWSTR = 31;
	public static final short VARIANT_TRUE = -1;
	public static final short VARIANT_FALSE = 0;
	public static final String WC_HEADER = "SysHeader32"; //$NON-NLS-1$
	public static final String WC_LINK = "SysLink"; //$NON-NLS-1$
	public static final String WC_LISTVIEW = "SysListView32"; //$NON-NLS-1$
	public static final String WC_TABCONTROL = "SysTabControl32"; //$NON-NLS-1$
	public static final String WC_TREEVIEW = "SysTreeView32"; //$NON-NLS-1$
	public static final int WINDING = 2;
	public static final int WH_CBT = 5;
	public static final int WH_GETMESSAGE = 0x3;
	public static final int WH_MSGFILTER = 0xFFFFFFFF;
	public static final int WH_FOREGROUNDIDLE = 11;
	public static final int WHEEL_DELTA = 120;
	public static final int WHEEL_PAGESCROLL = 0xFFFFFFFF;
	public static final int WHITE_BRUSH = 0;
	public static final int WHITENESS = 0x00FF0062;
	public static final int WM_ACTIVATE = 0x6;
	public static final int WM_ACTIVATEAPP = 0x1c;
	public static final int WM_APP = 0x8000;
	public static final int WM_DWMCOLORIZATIONCOLORCHANGED = 0x320;
	public static final int WM_CANCELMODE = 0x1f;
	public static final int WM_CAPTURECHANGED = 0x0215;
	public static final int WM_CHANGEUISTATE = 0x0127;
	public static final int WM_CHAR = 0x102;
	public static final int WM_CLEAR = 0x303;
	public static final int WM_CLOSE = 0x10;
	public static final int WM_COMMAND = 0x111;
	public static final int WM_CONTEXTMENU = 0x7b;
	public static final int WM_COPY = 0x301;
	public static final int WM_CREATE = 0x0001;	
	public static final int WM_CTLCOLORBTN = 0x135;
	public static final int WM_CTLCOLORDLG = 0x136;
	public static final int WM_CTLCOLOREDIT = 0x133;
	public static final int WM_CTLCOLORLISTBOX = 0x134;
	public static final int WM_CTLCOLORMSGBOX = 0x132;
	public static final int WM_CTLCOLORSCROLLBAR = 0x137;
	public static final int WM_CTLCOLORSTATIC = 0x138;
	public static final int WM_CUT = 0x300;
	public static final int WM_DEADCHAR = 0x103;
	public static final int WM_DESTROY = 0x2;
	public static final int WM_DRAWITEM = 0x2b;
	public static final int WM_ENDSESSION = 0x16;
	public static final int WM_ENTERIDLE = 0x121;
	public static final int WM_ERASEBKGND = 0x14;
	public static final int WM_GESTURE = 0x0119;
	public static final int WM_GETDLGCODE = 0x87;
	public static final int WM_GETFONT = 0x31;
//	public static final int WM_GETICON = 0x7f;
	public static final int WM_GETOBJECT = 0x003D;
	public static final int WM_GETMINMAXINFO = 0x0024;
	public static final int WM_HELP = 0x53;
	public static final int WM_HOTKEY = 0x0312;
	public static final int WM_HSCROLL = 0x114;
	public static final int WM_IME_CHAR = 0x286;
	public static final int WM_IME_COMPOSITION = 0x10f;
	public static final int WM_IME_COMPOSITION_START = 0x010D;
	public static final int WM_IME_ENDCOMPOSITION = 0x010E;
	public static final int WM_INITDIALOG = 0x110;
	public static final int WM_INITMENUPOPUP = 0x117;
	public static final int WM_INPUTLANGCHANGE = 0x51;
	public static final int WM_KEYDOWN = 0x100;
	public static final int WM_KEYFIRST = 0x100;
	public static final int WM_KEYLAST = 0x108;
	public static final int WM_KEYUP = 0x101;
	public static final int WM_KILLFOCUS = 0x8;
	public static final int WM_LBUTTONDBLCLK = 0x203;
	public static final int WM_LBUTTONDOWN = 0x201;
	public static final int WM_LBUTTONUP = 0x202;
	public static final int WM_MBUTTONDBLCLK = 0x209;
	public static final int WM_MBUTTONDOWN = 0x207;
	public static final int WM_MBUTTONUP = 0x208;
	public static final int WM_MEASUREITEM = 0x2c;
	public static final int WM_MENUCHAR = 0x120;
	public static final int WM_MENUSELECT = 0x11f;
	public static final int WM_MOUSEACTIVATE = 0x21;
	public static final int WM_MOUSEFIRST = 0x200;
	public static final int WM_MOUSEHOVER = 0x2a1;
	public static final int WM_MOUSELEAVE = 0x2a3;
	public static final int WM_MOUSEMOVE = 0x200;
	public static final int WM_MOUSEWHEEL = 0x20a;
	public static final int WM_MOUSEHWHEEL = 0x20e;
	public static final int WM_MOUSELAST = 0x20d;
	public static final int WM_MOVE = 0x3;
	public static final int WM_NCACTIVATE = 0x86;
	public static final int WM_NCCALCSIZE = 0x83;
	public static final int WM_NCHITTEST = 0x84;
	public static final int WM_NCLBUTTONDOWN = 0x00A1;
	public static final int WM_NCPAINT = 0x85;
	public static final int WM_NOTIFY = 0x4e;
	public static final int WM_NULL = 0x0;
	public static final int WM_PAINT = 0xf;
	public static final int WM_PALETTECHANGED = 0x311;
	public static final int WM_PARENTNOTIFY = 0x0210;
	public static final int WM_PASTE = 0x302;
	public static final int WM_PRINT = 0x0317;
	public static final int WM_PRINTCLIENT = 0x0318;
	public static final int WM_QUERYENDSESSION = 0x11;
	public static final int WM_QUERYNEWPALETTE = 0x30f;
	public static final int WM_QUERYOPEN = 0x13;
	public static final int WM_QUERYUISTATE = 0x129;
	public static final int WM_RBUTTONDBLCLK = 0x206;
	public static final int WM_RBUTTONDOWN = 0x204;
	public static final int WM_RBUTTONUP = 0x205;
	public static final int WM_SETCURSOR = 0x20;
	public static final int WM_SETFOCUS = 0x7;
	public static final int WM_SETFONT = 0x30;
	public static final int WM_SETICON = 0x80;
	public static final int WM_SETREDRAW = 0xb;
	public static final int WM_SETTEXT = 12;
	public static final int WM_SETTINGCHANGE = 0x1A;
	public static final int WM_SHOWWINDOW = 0x18;
	public static final int WM_SIZE = 0x5;
	public static final int WM_SYSCHAR = 0x106;
	public static final int WM_SYSCOLORCHANGE = 0x15;
	public static final int WM_SYSCOMMAND = 0x112;
	public static final int WM_SYSKEYDOWN = 0x104;
	public static final int WM_SYSKEYUP = 0x105;
	public static final int WM_TABLET_FLICK = 0x02C0 + 11;	
	public static final int WM_TIMER = 0x113;
	public static final int WM_THEMECHANGED = 0x031a;
	public static final int WM_TOUCH = 0x240;
	public static final int WM_UNDO = 0x304;
	public static final int WM_UNINITMENUPOPUP = 0x0125;
	public static final int WM_UPDATEUISTATE = 0x0128;
	public static final int WM_USER = 0x400;
	public static final int WM_VSCROLL = 0x115;
	public static final int WM_WINDOWPOSCHANGED = 0x47;
	public static final int WM_WINDOWPOSCHANGING = 0x46;
	public static final int WPF_RESTORETOMAXIMIZED = 0x0002;
	public static final int WS_BORDER = 0x800000;
	public static final int WS_CAPTION = 0xc00000;
	public static final int WS_CHILD = 0x40000000;
	public static final int WS_CLIPCHILDREN = 0x2000000;
	public static final int WS_CLIPSIBLINGS = 0x4000000;
	public static final int WS_DISABLED = 0x4000000;
	public static final int WS_EX_APPWINDOW = 0x40000;
	public static final int WS_EX_CAPTIONOKBTN = 0x80000000;
	public static final int WS_EX_CLIENTEDGE = 0x200;
	public static final int WS_EX_COMPOSITED = 0x2000000;
	public static final int WS_EX_DLGMODALFRAME = 0x1;
	public static final int WS_EX_LAYERED = 0x00080000;
	public static final int WS_EX_LAYOUTRTL = 0x00400000;
	public static final int WS_EX_LEFTSCROLLBAR = 0x00004000;
	public static final int WS_EX_MDICHILD = 0x00000040;
	public static final int WS_EX_NOINHERITLAYOUT = 0x00100000;
	public static final int WS_EX_NOACTIVATE = 0x08000000;
	public static final int WS_EX_RIGHT = 0x00001000;
	public static final int WS_EX_RTLREADING = 0x00002000;
	public static final int WS_EX_STATICEDGE = 0x20000;
	public static final int WS_EX_TOOLWINDOW = 0x80;
	public static final int WS_EX_TOPMOST = 0x8;
	public static final int WS_EX_TRANSPARENT = 0x20;
	public static final int WS_HSCROLL = 0x100000;
	public static final int WS_MAXIMIZEBOX = IsWinCE ? 0x20000 : 0x10000;
	public static final int WS_MINIMIZEBOX = IsWinCE ? 0x10000 : 0x20000;
	public static final int WS_OVERLAPPED = IsWinCE ? WS_BORDER | WS_CAPTION : 0x0;
	public static final int WS_OVERLAPPEDWINDOW = 0xcf0000;
	public static final int WS_POPUP = 0x80000000;
	public static final int WS_SYSMENU = 0x80000;
	public static final int WS_TABSTOP = 0x10000;
	public static final int WS_THICKFRAME = 0x40000;
	public static final int WS_VISIBLE = 0x10000000;
	public static final int WS_VSCROLL = 0x200000;
	public static final int WM_XBUTTONDOWN = 0x020B;
	public static final int WM_XBUTTONUP = 0x020C;
	public static final int WM_XBUTTONDBLCLK = 0x020D;
	public static final int XBUTTON1 = 0x1;
	public static final int XBUTTON2 = 0x2;
	public static final int X509_ASN_ENCODING = 1;
	
public static int VERSION (int major, int minor) {
	return major << 16 | minor;
}

/** 64 bit */
public static final native int ACCEL_sizeof ();
public static final native int ACTCTX_sizeof ();
public static final native int BITMAP_sizeof ();
public static final native int BITMAPINFOHEADER_sizeof ();
public static final native int BLENDFUNCTION_sizeof ();
public static final native int BP_PAINTPARAMS_sizeof ();
public static final native int BROWSEINFO_sizeof ();
public static final native int BUTTON_IMAGELIST_sizeof ();
public static final native int CANDIDATEFORM_sizeof ();
public static final native int CERT_CONTEXT_sizeof ();
public static final native int CERT_INFO_sizeof ();
public static final native int CERT_NAME_BLOB_sizeof ();
public static final native int CERT_PUBLIC_KEY_INFO_sizeof ();
public static final native int CHOOSECOLOR_sizeof ();
public static final native int CHOOSEFONT_sizeof ();
public static final native int COMBOBOXINFO_sizeof ();
public static final native int COMPOSITIONFORM_sizeof ();
public static final native int CREATESTRUCT_sizeof ();
public static final native int CRYPT_ALGORITHM_IDENTIFIER_sizeof ();
public static final native int CRYPT_BIT_BLOB_sizeof ();
public static final native int CRYPT_INTEGER_BLOB_sizeof ();
public static final native int CRYPT_OBJID_BLOB_sizeof ();
public static final native int DEVMODEA_sizeof ();
public static final native int DEVMODEW_sizeof ();
public static final native int DIBSECTION_sizeof ();
public static final native int DLLVERSIONINFO_sizeof ();
public static final native int DOCHOSTUIINFO_sizeof ();
public static final native int DOCINFO_sizeof ();
public static final native int DRAWITEMSTRUCT_sizeof ();
public static final native int DROPFILES_sizeof ();
public static final native int DTTOPTS_sizeof ();
public static final native int DWM_BLURBEHIND_sizeof ();
public static final native int EMR_sizeof ();
public static final native int EMREXTCREATEFONTINDIRECTW_sizeof ();
public static final native int EXTLOGFONTW_sizeof ();
public static final native int EXTLOGPEN_sizeof ();
public static final native int FILETIME_sizeof ();
public static final native int FLICK_DATA_sizeof ();
public static final native int FLICK_POINT_sizeof ();
public static final native int GCP_RESULTS_sizeof ();
public static final native int GESTURECONFIG_sizeof ();
public static final native int GESTUREINFO_sizeof ();
public static final native int GRADIENT_RECT_sizeof ();
public static final native int GUITHREADINFO_sizeof ();
public static final native int HDITEM_sizeof ();
public static final native int HDLAYOUT_sizeof ();
public static final native int HDHITTESTINFO_sizeof ();
public static final native int HELPINFO_sizeof ();
public static final native int HIGHCONTRAST_sizeof ();
public static final native int ICONINFO_sizeof ();
public static final native int INITCOMMONCONTROLSEX_sizeof ();
public static final native int INPUT_sizeof ();
public static final native int KEYBDINPUT_sizeof ();
public static final native int LITEM_sizeof ();
public static final native int LOGBRUSH_sizeof ();
public static final native int LOGFONTA_sizeof ();
public static final native int LOGFONTW_sizeof ();
public static final native int LOGPEN_sizeof ();
public static final native int LVCOLUMN_sizeof ();
public static final native int LVHITTESTINFO_sizeof ();
public static final native int LVITEM_sizeof ();
public static final native int LVINSERTMARK_sizeof ();
public static final native int MARGINS_sizeof ();
public static final native int MCHITTESTINFO_sizeof ();
public static final native int MEASUREITEMSTRUCT_sizeof ();
public static final native int MENUBARINFO_sizeof ();
public static final native int MENUINFO_sizeof ();
public static final native int MENUITEMINFO_sizeof ();
public static final native int MINMAXINFO_sizeof ();
public static final native int MOUSEINPUT_sizeof ();
public static final native int MONITORINFO_sizeof ();
public static final native int MSG_sizeof ();
public static final native int NMCUSTOMDRAW_sizeof ();
public static final native int NMHDR_sizeof ();
public static final native int NMHEADER_sizeof ();
public static final native int NMLINK_sizeof ();
public static final native int NMLISTVIEW_sizeof ();
public static final native int NMLVCUSTOMDRAW_sizeof ();
public static final native int NMLVDISPINFO_sizeof ();
public static final native int NMLVFINDITEM_sizeof ();
public static final native int NMLVODSTATECHANGE_sizeof ();
public static final native int NMREBARCHEVRON_sizeof ();
public static final native int NMREBARCHILDSIZE_sizeof ();
public static final native int NMRGINFO_sizeof ();
public static final native int NMTBHOTITEM_sizeof ();
public static final native int NMTREEVIEW_sizeof ();
public static final native int NMTOOLBAR_sizeof ();
public static final native int NMTTDISPINFOA_sizeof ();
public static final native int NMTTDISPINFOW_sizeof ();
public static final native int NMTTCUSTOMDRAW_sizeof ();
public static final native int NMTVCUSTOMDRAW_sizeof ();
public static final native int NMTVDISPINFO_sizeof ();
public static final native int NMTVITEMCHANGE_sizeof ();
public static final native int NMUPDOWN_sizeof ();
public static final native int NONCLIENTMETRICSA_sizeof ();
public static final native int NONCLIENTMETRICSW_sizeof ();
/** @method flags=const */
public static final native int NOTIFYICONDATAA_V2_SIZE ();
/** @method flags=const */
public static final native int NOTIFYICONDATAW_V2_SIZE ();
public static final native int OFNOTIFY_sizeof ();
public static final native int OPENFILENAME_sizeof ();
public static final native int OSVERSIONINFOA_sizeof ();
public static final native int OSVERSIONINFOW_sizeof ();
public static final native int OSVERSIONINFOEXA_sizeof ();
public static final native int OSVERSIONINFOEXW_sizeof ();
public static final native int OUTLINETEXTMETRICA_sizeof ();
public static final native int OUTLINETEXTMETRICW_sizeof ();
public static final native int PAINTSTRUCT_sizeof ();
public static final native int PANOSE_sizeof ();
public static final native int POINT_sizeof ();
public static final native int PRINTDLG_sizeof ();
public static final native int PROCESS_INFORMATION_sizeof ();
public static final native int PROPVARIANT_sizeof ();
public static final native int PROPERTYKEY_sizeof ();
public static final native int REBARBANDINFO_sizeof ();
public static final native int RECT_sizeof ();
public static final native int SAFEARRAY_sizeof ();
public static final native int SAFEARRAYBOUND_sizeof ();
public static final native int SCRIPT_ANALYSIS_sizeof ();
public static final native int SCRIPT_CONTROL_sizeof ();
public static final native int SCRIPT_DIGITSUBSTITUTE_sizeof ();
public static final native int SCRIPT_FONTPROPERTIES_sizeof ();
public static final native int SCRIPT_ITEM_sizeof ();
public static final native int SCRIPT_LOGATTR_sizeof ();
public static final native int SCRIPT_PROPERTIES_sizeof ();
public static final native int SCRIPT_STATE_sizeof ();
public static final native int SCRIPT_STRING_ANALYSIS_sizeof ();
public static final native int SCROLLBARINFO_sizeof ();
public static final native int SCROLLINFO_sizeof ();
public static final native int SHACTIVATEINFO_sizeof ();
public static final native int SHDRAGIMAGE_sizeof();
public static final native int SHELLEXECUTEINFO_sizeof ();
public static final native int SHFILEINFOA_sizeof ();
public static final native int SHFILEINFOW_sizeof ();
public static final native int SHMENUBARINFO_sizeof ();
public static final native int SHRGINFO_sizeof ();
public static final native int SIPINFO_sizeof ();
public static final native int SIZE_sizeof ();
public static final native int STARTUPINFO_sizeof ();
public static final native int SYSTEMTIME_sizeof ();
public static final native int TBBUTTON_sizeof ();
public static final native int TBBUTTONINFO_sizeof ();
public static final native int TCITEM_sizeof ();
public static final native int TCHITTESTINFO_sizeof ();
public static final native int TEXTMETRICA_sizeof ();
public static final native int TEXTMETRICW_sizeof ();
public static final native int TF_DA_COLOR_sizeof ();
public static final native int TF_DISPLAYATTRIBUTE_sizeof ();
public static final native int TOOLINFO_sizeof ();
public static final native int TOUCHINPUT_sizeof();
public static final native int TRACKMOUSEEVENT_sizeof ();
public static final native int TRIVERTEX_sizeof ();
public static final native int TVHITTESTINFO_sizeof ();
public static final native int TVINSERTSTRUCT_sizeof ();
public static final native int TVITEM_sizeof ();
public static final native int TVITEMEX_sizeof ();
public static final native int TVSORTCB_sizeof ();
public static final native int UDACCEL_sizeof ();
public static final native int WINDOWPLACEMENT_sizeof ();
public static final native int WINDOWPOS_sizeof ();
public static final native int WNDCLASS_sizeof ();

/** Ansi/Unicode wrappers */

public static final long /*int*/ AddFontResourceEx (TCHAR lpszFilename, int fl, long /*int*/ pdv) {
	if (IsUnicode) {
		char [] lpszFilename1 = lpszFilename == null ? null : lpszFilename.chars;
		return AddFontResourceExW (lpszFilename1, fl, pdv);
	}
	byte [] lpszFilename1 = lpszFilename == null ? null : lpszFilename.bytes;
	return AddFontResourceExA (lpszFilename1, fl, pdv);
}

public static final int AssocQueryString(int flags, int str, TCHAR pszAssoc, TCHAR pszExtra, TCHAR pszOut, int[] pcchOut) {
	if (IsUnicode) {
		char [] pszAssoc1 = pszAssoc == null ? null : pszAssoc.chars;
		char [] pszExtra1 = pszExtra == null ? null : pszExtra.chars;
		char [] pszOut1 = pszOut == null ? null : pszOut.chars;
		return AssocQueryStringW (flags, str, pszAssoc1, pszExtra1, pszOut1, pcchOut);
	}
	byte [] pszAssoc1 = pszAssoc == null ? null : pszAssoc.bytes;
	byte [] pszExtra1 = pszExtra == null ? null : pszExtra.bytes;
	byte [] pszOut1 = pszOut == null ? null : pszOut.bytes;
	return AssocQueryStringA (flags, str, pszAssoc1, pszExtra1, pszOut1, pcchOut);
}

public static final long /*int*/ CallWindowProc (long /*int*/ lpPrevWndFunc, long /*int*/ hWnd, int Msg, long /*int*/ wParam, long /*int*/ lParam) {
	if (IsUnicode) return CallWindowProcW (lpPrevWndFunc, hWnd, Msg, wParam, lParam);
	return CallWindowProcA (lpPrevWndFunc, hWnd, Msg, wParam, lParam);
}

public static final int CertNameToStr (int dwCertEncodingType, CERT_NAME_BLOB pName, int dwStrType, TCHAR psz, int csz) {
	if (IsUnicode) {
		char [] psz1 = psz == null ? null : psz.chars;
		return CertNameToStrW (dwCertEncodingType, pName, dwStrType, psz1, csz);
	}
	byte [] psz1 = psz == null ? null : psz.bytes;
	return CertNameToStrA (dwCertEncodingType, pName, dwStrType, psz1, csz);
}

public static final long /*int*/ CharUpper (long /*int*/ ch) {
	if (IsUnicode) return CharUpperW (ch);
	return CharUpperA (ch);
}

public static final long /*int*/ CharLower (long /*int*/ ch) {
	if (IsUnicode) return CharLowerW (ch);
	return CharLowerA (ch);
}

public static final boolean ChooseColor (CHOOSECOLOR lpcc) {
	if (IsUnicode) return ChooseColorW (lpcc);
	return ChooseColorA (lpcc);
}

public static final boolean ChooseFont (CHOOSEFONT chooseFont) {
	if (IsUnicode) return ChooseFontW (chooseFont);
	return ChooseFontA (chooseFont);
}

public static final long /*int*/ CreateActCtx (ACTCTX pActCtx) {
	if (IsUnicode) return CreateActCtxW (pActCtx);
	return CreateActCtxA (pActCtx);
}

public static final long /*int*/ CreateAcceleratorTable (byte [] lpaccl, int cEntries) {
	if (IsUnicode) return CreateAcceleratorTableW (lpaccl, cEntries);
	return CreateAcceleratorTableA (lpaccl, cEntries);
}

public static final long /*int*/ CreateDC (TCHAR lpszDriver, TCHAR lpszDevice, long /*int*/ lpszOutput, long /*int*/ lpInitData) {
	if (IsUnicode) {
		char [] lpszDriver1 = lpszDriver == null ? null : lpszDriver.chars;
		char [] lpszDevice1 = lpszDevice == null ? null : lpszDevice.chars;
		return CreateDCW (lpszDriver1, lpszDevice1, lpszOutput, lpInitData);
	}
	byte [] lpszDriver1 = lpszDriver == null ? null : lpszDriver.bytes;
	byte [] lpszDevice1 = lpszDevice == null ? null : lpszDevice.bytes;
	return CreateDCA (lpszDriver1, lpszDevice1, lpszOutput, lpInitData);
}

public static final long /*int*/ CreateEnhMetaFile (long /*int*/ hdcRef, TCHAR lpFilename, RECT lpRect, TCHAR lpDescription) {
	if (IsUnicode) {
		char [] lpFilename1 = lpFilename == null ? null : lpFilename.chars;
		char [] lpDescription1 = lpDescription == null ? null : lpDescription.chars;
		return CreateEnhMetaFileW (hdcRef, lpFilename1, lpRect, lpDescription1);
	}
	byte [] lpFilename1 = lpFilename == null ? null : lpFilename.bytes;
	byte [] lpDescription1 = lpDescription == null ? null : lpDescription.bytes;
	return CreateEnhMetaFileA (hdcRef, lpFilename1, lpRect, lpDescription1);
}

public static final long /*int*/ CreateFontIndirect (long /*int*/ lplf) {
	if (IsUnicode) return CreateFontIndirectW (lplf);
	return CreateFontIndirectA (lplf);
}

public static final long /*int*/ CreateFontIndirect (LOGFONT lplf) {
	if (IsUnicode) return CreateFontIndirectW ((LOGFONTW)lplf);
	return CreateFontIndirectA ((LOGFONTA)lplf);
}

public static final boolean CreateProcess (long /*int*/ lpApplicationName, long /*int*/ lpCommandLine, long /*int*/ lpProcessAttributes, long /*int*/ lpThreadAttributes, boolean bInheritHandles, int dwCreationFlags, long /*int*/ lpEnvironment, long /*int*/ lpCurrentDirectory, STARTUPINFO lpStartupInfo, PROCESS_INFORMATION lpProcessInformation) {
	if (IsUnicode) return CreateProcessW (lpApplicationName, lpCommandLine, lpProcessAttributes, lpThreadAttributes, bInheritHandles, dwCreationFlags, lpEnvironment, lpCurrentDirectory, lpStartupInfo, lpProcessInformation);
	return CreateProcessA (lpApplicationName, lpCommandLine, lpProcessAttributes, lpThreadAttributes, bInheritHandles, dwCreationFlags, lpEnvironment, lpCurrentDirectory, lpStartupInfo, lpProcessInformation);
}

public static final long /*int*/ CreateWindowEx (int dwExStyle, TCHAR lpClassName, TCHAR lpWindowName, int dwStyle, int X, int Y, int nWidth, int nHeight, long /*int*/ hWndParent, long /*int*/ hMenu, long /*int*/ hInstance, CREATESTRUCT lpParam) {
	if (IsUnicode) {
		char [] lpClassName1 = lpClassName == null ? null : lpClassName.chars;
		char [] lpWindowName1 = lpWindowName == null ? null : lpWindowName.chars;
		return CreateWindowExW (dwExStyle, lpClassName1, lpWindowName1, dwStyle, X, Y, nWidth, nHeight, hWndParent, hMenu, hInstance, lpParam);
	}
	byte [] lpClassName1 = lpClassName == null ? null : lpClassName.bytes;
	byte [] lpWindowName1 = lpWindowName == null ? null : lpWindowName.bytes;
	return CreateWindowExA (dwExStyle, lpClassName1, lpWindowName1, dwStyle, X, Y, nWidth, nHeight, hWndParent, hMenu, hInstance, lpParam);
}

public static final long /*int*/ DefMDIChildProc (long /*int*/ hWnd, int Msg, long /*int*/ wParam, long /*int*/ lParam) {
	if (IsUnicode) return DefMDIChildProcW (hWnd, Msg, wParam, lParam);
	return DefMDIChildProcA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ DefFrameProc (long /*int*/ hWnd, long /*int*/ hWndMDIClient, int Msg, long /*int*/ wParam, long /*int*/ lParam) {
	if (IsUnicode) return DefFrameProcW (hWnd, hWndMDIClient, Msg, wParam, lParam);
	return DefFrameProcA (hWnd, hWndMDIClient, Msg, wParam, lParam);
}
public static final long /*int*/ DefWindowProc (long /*int*/ hWnd, int Msg, long /*int*/ wParam, long /*int*/ lParam) {
	if (IsUnicode) return DefWindowProcW (hWnd, Msg, wParam, lParam);
	return DefWindowProcA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ DispatchMessage (MSG lpmsg) {
	if (IsUnicode) return DispatchMessageW (lpmsg);
	return DispatchMessageA (lpmsg);
}

public static final int DocumentProperties (long /*int*/ hWnd, long /*int*/ hPrinter, TCHAR pDeviceName, long /*int*/ pDevModeOutput, long /*int*/ pDevModeInput, int fMode) {
	if (IsUnicode) {
		char [] pDeviceName1 = pDeviceName == null ? null : pDeviceName.chars;
		return DocumentPropertiesW (hWnd, hPrinter, pDeviceName1, pDevModeOutput, pDevModeInput, fMode);
	}
	byte [] pDeviceName1 = pDeviceName == null ? null : pDeviceName.bytes;
	return DocumentPropertiesA (hWnd, hPrinter, pDeviceName1, pDevModeOutput, pDevModeInput, fMode);
}

public static final int DragQueryFile (long /*int*/ hDrop, int iFile, TCHAR lpszFile, int cch) {
	if (IsUnicode) {
		char [] lpszFile1 = lpszFile == null ? null : lpszFile.chars;
		return DragQueryFileW (hDrop, iFile, lpszFile1, cch);
	}
	byte [] lpszFile1 = lpszFile == null ? null : lpszFile.bytes;
	return DragQueryFileA (hDrop, iFile, lpszFile1, cch);
}

public static final boolean DrawState (long /*int*/ hdc, long /*int*/ hbr, long /*int*/ lpOutputFunc, long /*int*/ lData, long /*int*/ wData, int x, int y, int cx, int cy, int fuFlags) {
	if (IsUnicode) return DrawStateW (hdc, hbr, lpOutputFunc, lData, wData, x, y, cx, cy, fuFlags);
	return DrawStateA (hdc, hbr, lpOutputFunc, lData, wData, x, y, cx, cy, fuFlags);
}

public static final int DrawText (long /*int*/ hDC, TCHAR lpString, int nCount, RECT lpRect, int uFormat) {
	if (IsUnicode) {
		char [] lpString1 = lpString == null ? null : lpString.chars;
		return DrawTextW (hDC, lpString1, nCount, lpRect, uFormat);
	}
	byte [] lpString1 = lpString == null ? null : lpString.bytes;
	return DrawTextA (hDC, lpString1, nCount, lpRect, uFormat);
}

public static final int EnumFontFamilies (long /*int*/ hdc, TCHAR lpszFamily, long /*int*/ lpEnumFontFamProc, long /*int*/ lParam) {
	if (IsUnicode) {
		char [] lpszFamily1 = lpszFamily == null ? null : lpszFamily.chars;
		return EnumFontFamiliesW (hdc, lpszFamily1, lpEnumFontFamProc, lParam);
	}
	byte [] lpszFamily1 = lpszFamily == null ? null : lpszFamily.bytes;
	return EnumFontFamiliesA (hdc, lpszFamily1, lpEnumFontFamProc, lParam);
}

public static final int EnumFontFamiliesEx (long /*int*/ hdc, LOGFONT lpLogfont, long /*int*/ lpEnumFontFamExProc, long /*int*/ lParam, int dwFlags) {
	if (IsUnicode) return EnumFontFamiliesExW (hdc, (LOGFONTW)lpLogfont, lpEnumFontFamExProc, lParam, dwFlags);
	return EnumFontFamiliesExA (hdc, (LOGFONTA)lpLogfont, lpEnumFontFamExProc, lParam, dwFlags);
}

public static final boolean EnumSystemLocales (long /*int*/ lpLocaleEnumProc, int dwFlags) {
	if (IsUnicode) return EnumSystemLocalesW (lpLocaleEnumProc, dwFlags);
	return EnumSystemLocalesA (lpLocaleEnumProc, dwFlags);
}

public static final boolean EnumSystemLanguageGroups (long /*int*/ pLangGroupEnumProc, int dwFlags, long /*int*/ lParam) {
	if (IsUnicode) return EnumSystemLanguageGroupsW (pLangGroupEnumProc, dwFlags, lParam);
	return EnumSystemLanguageGroupsA (pLangGroupEnumProc, dwFlags, lParam);
}

public static final int ExpandEnvironmentStrings (TCHAR lpSrc, TCHAR lpDst, int nSize) {
	if (IsUnicode) {
		char [] lpSrc1 = lpSrc == null ? null : lpSrc.chars;
		char [] lpDst1 = lpDst == null ? null : lpDst.chars;
		return ExpandEnvironmentStringsW (lpSrc1, lpDst1, nSize);
	}
	byte [] lpSrc1 = lpSrc == null ? null : lpSrc.bytes;
	byte [] lpDst1 = lpDst == null ? null : lpDst.bytes;
	return ExpandEnvironmentStringsA (lpSrc1, lpDst1, nSize);
}

public static final int ExtractIconEx (TCHAR lpszFile, int nIconIndex, long /*int*/ [] phiconLarge, long /*int*/ [] phiconSmall, int nIcons) {
	if (IsUnicode) {
		char [] lpszFile1 = lpszFile == null ? null : lpszFile.chars;
		return ExtractIconExW (lpszFile1, nIconIndex, phiconLarge, phiconSmall, nIcons);
	}
	byte [] lpszFile1 = lpszFile == null ? null : lpszFile.bytes;
	return ExtractIconExA (lpszFile1, nIconIndex, phiconLarge, phiconSmall, nIcons);
}

public static final boolean ExtTextOut(long /*int*/ hdc, int X, int Y, int fuOptions, RECT lprc, TCHAR lpString, int cbCount, int[] lpDx) {
	if (IsUnicode) {
		char [] lpString1 = lpString == null ? null : lpString.chars;
		return ExtTextOutW (hdc, X, Y, fuOptions, lprc, lpString1, cbCount, lpDx);
	}
	byte [] lpString1 = lpString == null ? null : lpString.bytes;
	return ExtTextOutA (hdc, X, Y, fuOptions, lprc, lpString1, cbCount, lpDx);
}

public static final long /*int*/ FindWindow (TCHAR lpClassName, TCHAR lpWindowName) {
	if (IsUnicode) {
		char [] lpClassName1 = lpClassName == null ? null : lpClassName.chars;
		char [] lpWindowName1 = lpWindowName == null ? null : lpWindowName.chars;
		return FindWindowW (lpClassName1, lpWindowName1); 
	}
	byte [] lpClassName1 = lpClassName == null ? null : lpClassName.bytes;
	byte [] lpWindowName1 = lpWindowName == null ? null : lpWindowName.bytes;
	return FindWindowA (lpClassName1, lpWindowName1);
}

public static final int FormatMessage (int dwFlags, long /*int*/ lpSource, int dwMessageId, int dwLanguageId, long /*int*/ [] lpBuffer, int nSize, long /*int*/ Arguments) {
	if (IsUnicode) {
		return FormatMessageW (dwFlags, lpSource, dwMessageId, dwLanguageId, lpBuffer, nSize, Arguments); 
	}
	return FormatMessageA (dwFlags, lpSource, dwMessageId, dwLanguageId, lpBuffer, nSize, Arguments);
}

public static final boolean GetCharABCWidths (long /*int*/ hdc, int iFirstChar, int iLastChar, int [] lpabc) {
	if (IsUnicode) return GetCharABCWidthsW (hdc,iFirstChar, iLastChar, lpabc);
	return GetCharABCWidthsA (hdc,iFirstChar, iLastChar, lpabc);
}

public static final int GetCharacterPlacement (long /*int*/ hdc, TCHAR lpString, int nCount, int nMaxExtent, GCP_RESULTS lpResults, int dwFlags) {
	if (IsUnicode) {
		char [] lpString1 = lpString == null ? null : lpString.chars;
		return GetCharacterPlacementW (hdc, lpString1, nCount, nMaxExtent, lpResults, dwFlags);
	}
	byte [] lpString1 = lpString == null ? null : lpString.bytes;
	return GetCharacterPlacementA (hdc, lpString1, nCount, nMaxExtent, lpResults, dwFlags);	
}

public static final boolean GetCharWidth (long /*int*/ hdc, int iFirstChar, int iLastChar, int [] lpabc) {
	if (IsUnicode) return GetCharWidthW (hdc,iFirstChar, iLastChar, lpabc);
	return GetCharWidthA (hdc,iFirstChar, iLastChar, lpabc);
}

public static final boolean GetClassInfo (long /*int*/ hInstance, TCHAR lpClassName, WNDCLASS lpWndClass) {
	if (IsUnicode) {
		char [] lpClassName1 = lpClassName == null ? null : lpClassName.chars;
		return GetClassInfoW (hInstance, lpClassName1, lpWndClass);
	}
	byte [] lpClassName1 = lpClassName == null ? null : lpClassName.bytes;
	return GetClassInfoA (hInstance, lpClassName1, lpWndClass);
}

public static final int GetClassName (long /*int*/ hWnd, TCHAR lpClassName, int nMaxCount) {
	if (IsUnicode) {
		char [] lpClassName1 = lpClassName == null ? null : lpClassName.chars;
		return GetClassNameW (hWnd, lpClassName1, nMaxCount);
	}
	byte [] lpClassName1 = lpClassName == null ? null : lpClassName.bytes;
	return GetClassNameA (hWnd, lpClassName1, nMaxCount);
}

public static final int GetClipboardFormatName (int format, TCHAR lpszFormatName, int cchMaxCount) {
	if (IsUnicode) {
		char [] lpszFormatName1 = lpszFormatName == null ? null : lpszFormatName.chars;
		return GetClipboardFormatNameW (format, lpszFormatName1, cchMaxCount);
	}
	byte [] lpszFormatName1 = lpszFormatName == null ? null : lpszFormatName.bytes;
	return GetClipboardFormatNameA (format, lpszFormatName1, cchMaxCount);
}

public static final int GetDateFormat (int Locale, int dwFlags, SYSTEMTIME lpDate, TCHAR lpFormat, TCHAR lpDateStr, int cchDate) {
	if (IsUnicode) {
		char [] lpString1 = lpFormat == null ? null : lpFormat.chars;
		char [] lpString2 = lpDateStr == null ? null : lpDateStr.chars;
		return GetDateFormatW (Locale, dwFlags, lpDate, lpString1, lpString2, cchDate);
	}
	byte [] lpString1 = lpFormat == null ? null : lpFormat.bytes;
	byte [] lpString2 = lpDateStr == null ? null : lpDateStr.bytes;
	return GetDateFormatA (Locale, dwFlags, lpDate, lpString1, lpString2, cchDate);
}

public static final int GetKeyNameText (int lParam, TCHAR lpString, int nSize) {
	if (IsUnicode) {
		char [] lpString1 = lpString == null ? null : lpString.chars;
		return GetKeyNameTextW (lParam, lpString1, nSize);
	}
	byte [] lpString1 = lpString == null ? null : lpString.bytes;
	return GetKeyNameTextA (lParam, lpString1, nSize);
}

public static final int GetLocaleInfo (int Locale, int LCType, TCHAR lpLCData, int cchData) {
	if (IsUnicode) {
		char [] lpLCData1 = lpLCData == null ? null : lpLCData.chars;
		return GetLocaleInfoW (Locale, LCType, lpLCData1, cchData);
	}
	byte [] lpLCData1 = lpLCData == null ? null : lpLCData.bytes;
	return GetLocaleInfoA (Locale, LCType, lpLCData1, cchData);
}

public static final boolean GetMenuItemInfo (long /*int*/ hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii) {
	if (IsUnicode) return GetMenuItemInfoW (hMenu, uItem, fByPosition, lpmii);
	return GetMenuItemInfoA (hMenu, uItem, fByPosition, lpmii);
}

public static final boolean GetMessage (MSG lpMsg, long /*int*/ hWnd, int wMsgFilterMin, int wMsgFilterMax) {
	if (IsUnicode) return GetMessageW (lpMsg, hWnd, wMsgFilterMin, wMsgFilterMax);
	return GetMessageA (lpMsg, hWnd, wMsgFilterMin, wMsgFilterMax);
}

public static final int GetModuleFileName (long /*int*/ hModule, TCHAR lpFilename, int inSize) {
	if (IsUnicode) {
		char [] lpFilename1 = lpFilename == null ? null : lpFilename.chars;
		return GetModuleFileNameW (hModule, lpFilename1, inSize);
	}
	byte [] lpFilename1 = lpFilename == null ? null : lpFilename.bytes;
	return GetModuleFileNameA (hModule, lpFilename1, inSize);
}

public static final long /*int*/ GetModuleHandle (TCHAR lpModuleName) {
	if (IsUnicode) {
		char [] lpModuleName1 = lpModuleName == null ? null : lpModuleName.chars;
		return GetModuleHandleW (lpModuleName1);
	}
	byte [] lpModuleName1 = lpModuleName == null ? null : lpModuleName.bytes;
	return GetModuleHandleA (lpModuleName1);
}

public static final boolean GetMonitorInfo (long /*int*/ hmonitor, MONITORINFO lpmi) {
	if (IsUnicode) return GetMonitorInfoW (hmonitor, lpmi);
	return GetMonitorInfoA (hmonitor, lpmi);
}

public static final int GetObject (long /*int*/ hgdiobj, int cbBuffer, BITMAP lpvObject) {
	if (IsUnicode) return GetObjectW (hgdiobj, cbBuffer, lpvObject);
	return GetObjectA (hgdiobj, cbBuffer, lpvObject);
}

public static final int GetObject (long /*int*/ hgdiobj, int cbBuffer, DIBSECTION lpvObject) {
	if (IsUnicode) return GetObjectW (hgdiobj, cbBuffer, lpvObject);
	return GetObjectA (hgdiobj, cbBuffer, lpvObject);
}

public static final int GetObject (long /*int*/ hgdiobj, int cbBuffer, EXTLOGPEN lpvObject) {
	if (IsUnicode) return GetObjectW (hgdiobj, cbBuffer, lpvObject);
	return GetObjectA (hgdiobj, cbBuffer, lpvObject);
}

public static final int GetObject (long /*int*/ hgdiobj, int cbBuffer, LOGBRUSH lpvObject) {
	if (IsUnicode) return GetObjectW (hgdiobj, cbBuffer, lpvObject);
	return GetObjectA (hgdiobj, cbBuffer, lpvObject);
}

public static final int GetObject (long /*int*/ hgdiobj, int cbBuffer, LOGFONT lpvObject) {
	if (IsUnicode) return GetObjectW (hgdiobj, cbBuffer, (LOGFONTW)lpvObject);
	return GetObjectA (hgdiobj, cbBuffer, (LOGFONTA)lpvObject);
}

public static final int GetObject (long /*int*/ hgdiobj, int cbBuffer, LOGPEN lpvObject) {
	if (IsUnicode) return GetObjectW (hgdiobj, cbBuffer, lpvObject);
	return GetObjectA (hgdiobj, cbBuffer, lpvObject);
}

public static final int GetObject (long /*int*/ hgdiobj, int cbBuffer, long /*int*/ lpvObject) {
	if (IsUnicode) return GetObjectW (hgdiobj, cbBuffer, lpvObject);
	return GetObjectA (hgdiobj, cbBuffer, lpvObject);
}

public static final boolean GetOpenFileName (OPENFILENAME lpofn) {
	if (IsUnicode) return GetOpenFileNameW (lpofn);
	return GetOpenFileNameA (lpofn);
}

public static final int GetOutlineTextMetrics (long /*int*/ hdc, int cbData,  OUTLINETEXTMETRIC lpOTM) {
	if (IsUnicode) return GetOutlineTextMetricsW (hdc, cbData, (OUTLINETEXTMETRICW)lpOTM);
	return GetOutlineTextMetricsA (hdc, cbData, (OUTLINETEXTMETRICA)lpOTM);
}

public static final int GetProfileString (TCHAR lpAppName, TCHAR lpKeyName, TCHAR lpDefault, TCHAR lpReturnedString, int nSize) {
	if (IsUnicode) {
		char [] lpAppName1 = lpAppName == null ? null : lpAppName.chars;
		char [] lpKeyName1 = lpKeyName == null ? null : lpKeyName.chars;
		char [] lpDefault1 = lpDefault == null ? null : lpDefault.chars;
		char [] lpReturnedString1 = lpReturnedString == null ? null : lpReturnedString.chars;
		return GetProfileStringW (lpAppName1, lpKeyName1, lpDefault1, lpReturnedString1, nSize);
	}
	byte [] lpAppName1 = lpAppName == null ? null : lpAppName.bytes;
	byte [] lpKeyName1 = lpKeyName == null ? null : lpKeyName.bytes;
	byte [] lpDefault1 = lpDefault == null ? null : lpDefault.bytes;
	byte [] lpReturnedString1 = lpReturnedString == null ? null : lpReturnedString.bytes;
	return GetProfileStringA (lpAppName1, lpKeyName1, lpDefault1, lpReturnedString1, nSize);
}

public static long /*int*/ GetProp (long /*int*/ hWnd, long /*int*/ lpString) {
	if (IsUnicode) return GetPropW (hWnd, lpString);
	return GetPropA (hWnd, lpString);
}

public static final boolean GetSaveFileName (OPENFILENAME lpofn) {
	if (IsUnicode) return GetSaveFileNameW (lpofn);
	return GetSaveFileNameA (lpofn);
}

public static final void GetStartupInfo (STARTUPINFO lpStartupInfo) {
	if (IsUnicode) {
		GetStartupInfoW (lpStartupInfo);
	} else {
		GetStartupInfoA (lpStartupInfo);
	}
}

public static final boolean GetTextExtentPoint32 (long /*int*/ hdc, TCHAR lpString, int cbString, SIZE lpSize) {
	if (IsUnicode) {
		char [] lpString1 = lpString == null ? null : lpString.chars;
		return GetTextExtentPoint32W (hdc, lpString1, cbString, lpSize);
	}
	byte [] lpString1 = lpString == null ? null : lpString.bytes;
	return GetTextExtentPoint32A (hdc, lpString1, cbString, lpSize);	
}

public static final boolean GetTextMetrics (long /*int*/ hdc, TEXTMETRIC lptm) {
	if (IsUnicode) return GetTextMetricsW (hdc, (TEXTMETRICW)lptm);
	return GetTextMetricsA (hdc, (TEXTMETRICA)lptm);
}

public static final int GetTimeFormat (int Locale, int dwFlags, SYSTEMTIME lpTime, TCHAR lpFormat, TCHAR lpTimeStr, int cchTime) {
	if (IsUnicode) {
		char [] lpString1 = lpFormat == null ? null : lpFormat.chars;
		char [] lpString2 = lpTimeStr == null ? null : lpTimeStr.chars;
		return GetTimeFormatW (Locale, dwFlags, lpTime, lpString1, lpString2, cchTime);
	}
	byte [] lpString1 = lpFormat == null ? null : lpFormat.bytes;
	byte [] lpString2 = lpTimeStr == null ? null : lpTimeStr.bytes;
	return GetTimeFormatA (Locale, dwFlags, lpTime, lpString1, lpString2, cchTime);
}

public static final boolean GetVersionEx (OSVERSIONINFO lpVersionInfo) {
	if (IsUnicode) return GetVersionExW ((OSVERSIONINFOW)lpVersionInfo);
	return GetVersionExA ((OSVERSIONINFOA)lpVersionInfo);
}

public static final boolean GetVersionEx (OSVERSIONINFOEX lpVersionInfo) {
	if (IsUnicode) return GetVersionExW ((OSVERSIONINFOEXW)lpVersionInfo);
	return GetVersionExA ((OSVERSIONINFOEXA)lpVersionInfo);
}

public static final int GetWindowLong (long /*int*/ hWnd, int nIndex) {
	if (IsUnicode) return GetWindowLongW (hWnd, nIndex);
	return GetWindowLongA (hWnd, nIndex);
}

public static final long /*int*/ GetWindowLongPtr (long /*int*/ hWnd, int nIndex) {
	if (IsUnicode) return GetWindowLongPtrW (hWnd, nIndex);
	return GetWindowLongPtrA (hWnd, nIndex);
}

public static final int GetWindowText (long /*int*/ hWnd, TCHAR lpString, int nMaxCount) {
	if (IsUnicode) {
		char [] lpString1 = lpString == null ? null : lpString.chars;
		return GetWindowTextW (hWnd, lpString1, nMaxCount);
	}
	byte [] lpString1 = lpString == null ? null : lpString.bytes;
	return GetWindowTextA (hWnd, lpString1, nMaxCount);
}

public static final int GetWindowTextLength (long /*int*/ hWnd) {
	if (IsUnicode) return GetWindowTextLengthW (hWnd);
	return GetWindowTextLengthA (hWnd);
}

public static final int GlobalAddAtom (TCHAR lpString) {
	if (IsUnicode) {
		char [] lpString1 = lpString == null ? null : lpString.chars;
		return GlobalAddAtomW (lpString1);
	}
	byte [] lpString1 = lpString == null ? null : lpString.bytes;
	return GlobalAddAtomA (lpString1);
}

public static final boolean ImmGetCompositionFont (long /*int*/ hIMC, LOGFONT lplf) {
	if (IsUnicode) return ImmGetCompositionFontW (hIMC, (LOGFONTW)lplf);
	return ImmGetCompositionFontA (hIMC, (LOGFONTA)lplf);
}

public static final boolean ImmSetCompositionFont (long /*int*/ hIMC, LOGFONT lplf) {
	if (IsUnicode) return ImmSetCompositionFontW (hIMC, (LOGFONTW)lplf);
	return ImmSetCompositionFontA (hIMC, (LOGFONTA)lplf);
}

public static final int ImmGetCompositionString (long /*int*/ hIMC, int dwIndex, byte [] lpBuf, int dwBufLen) {
	if (IsUnicode) {
		return ImmGetCompositionStringW (hIMC, dwIndex, lpBuf, dwBufLen);
	}
	return ImmGetCompositionStringA (hIMC, dwIndex, lpBuf, dwBufLen);
}

public static final int ImmGetCompositionString (long /*int*/ hIMC, int dwIndex, int [] lpBuf, int dwBufLen) {
	if (IsUnicode) {
		return ImmGetCompositionStringW (hIMC, dwIndex, lpBuf, dwBufLen);
	}
	return ImmGetCompositionStringA (hIMC, dwIndex, lpBuf, dwBufLen);
}

public static final int ImmGetCompositionString (long /*int*/ hIMC, int dwIndex, TCHAR lpBuf, int dwBufLen) {
	if (IsUnicode) {
		char [] lpBuf1 = lpBuf == null ? null : lpBuf.chars;
		return ImmGetCompositionStringW (hIMC, dwIndex, lpBuf1, dwBufLen);
	}
	byte [] lpBuf1 = lpBuf == null ? null : lpBuf.bytes;
	return ImmGetCompositionStringA (hIMC, dwIndex, lpBuf1, dwBufLen);
}

public static final boolean InternetGetCookie (TCHAR lpszUrl, TCHAR lpszCookieName, TCHAR lpszCookieData, int[] lpdwSize) {
	if (IsUnicode) {
		char [] url = lpszUrl == null ? null : lpszUrl.chars;
		char [] cookieName = lpszCookieName == null ? null : lpszCookieName.chars;
		char [] cookieData = lpszCookieData == null ? null : lpszCookieData.chars;
		return InternetGetCookieW (url, cookieName, cookieData, lpdwSize);
	}
	byte [] url = lpszUrl == null ? null : lpszUrl.bytes;
	byte [] cookieName = lpszCookieName == null ? null : lpszCookieName.bytes;
	byte [] cookieData = lpszCookieData == null ? null : lpszCookieData.bytes;
	return InternetGetCookieA (url, cookieName, cookieData, lpdwSize);
}

public static final boolean InternetSetCookie (TCHAR lpszUrl, TCHAR lpszCookieName, TCHAR lpszCookieData) {
	if (IsUnicode) {
		char [] url = lpszUrl == null ? null : lpszUrl.chars;
		char [] cookieName = lpszCookieName == null ? null : lpszCookieName.chars;
		char [] cookieData = lpszCookieData == null ? null : lpszCookieData.chars;
		return InternetSetCookieW (url, cookieName, cookieData);
	}
	byte [] url = lpszUrl == null ? null : lpszUrl.bytes;
	byte [] cookieName = lpszCookieName == null ? null : lpszCookieName.bytes;
	byte [] cookieData = lpszCookieData == null ? null : lpszCookieData.bytes;
	return InternetSetCookieA (url, cookieName, cookieData);
}

public static final boolean InsertMenu (long /*int*/ hMenu, int uPosition, int uFlags, long /*int*/ uIDNewItem, TCHAR lpNewItem) {
	if (IsUnicode) {
		char [] lpNewItem1 = lpNewItem == null ? null : lpNewItem.chars;
		return InsertMenuW (hMenu, uPosition, uFlags, uIDNewItem, lpNewItem1);
	}
	byte [] lpNewItem1 = lpNewItem == null ? null : lpNewItem.bytes;
	return InsertMenuA (hMenu, uPosition, uFlags, uIDNewItem, lpNewItem1);	
}

public static final boolean InsertMenuItem (long /*int*/ hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii) {
	if (IsUnicode) return InsertMenuItemW (hMenu, uItem, fByPosition, lpmii);
	return InsertMenuItemA (hMenu, uItem, fByPosition, lpmii);
}

public static final long /*int*/ LoadBitmap (long /*int*/ hInstance, long /*int*/ lpBitmapName) {
	if (IsUnicode) return LoadBitmapW (hInstance, lpBitmapName);
	return LoadBitmapA (hInstance, lpBitmapName);
}

public static final long /*int*/ LoadCursor (long /*int*/ hInstance, long /*int*/ lpCursorName) {
	if (IsUnicode) return LoadCursorW (hInstance, lpCursorName);
	return LoadCursorA (hInstance, lpCursorName);
}

public static final long /*int*/ LoadIcon (long /*int*/ hInstance, long /*int*/ lpIconName) {
	if (IsUnicode) return LoadIconW (hInstance, lpIconName);
	return LoadIconA (hInstance, lpIconName);
}

public static final long /*int*/ LoadImage (long /*int*/ hinst, TCHAR lpszName, int uType, int cxDesired, int cyDesired, int fuLoad) {
	if (IsUnicode) {
		char [] lpszName1 = lpszName == null ? null : lpszName.chars;
		return LoadImageW (hinst, lpszName1, uType, cxDesired, cyDesired, fuLoad);
	}
	byte [] lpszName1 = lpszName == null ? null : lpszName.bytes;
	return LoadImageA (hinst, lpszName1, uType, cxDesired, cyDesired, fuLoad);
}

public static final long /*int*/ LoadImage (long /*int*/ hinst, long /*int*/ lpszName, int uType, int cxDesired, int cyDesired, int fuLoad) {
	if (IsUnicode) return LoadImageW (hinst, lpszName, uType, cxDesired, cyDesired, fuLoad);
	return LoadImageA (hinst, lpszName, uType, cxDesired, cyDesired, fuLoad);
}

public static final long /*int*/ LoadLibrary (TCHAR lpLibFileName) {
	if (IsUnicode) {
		char [] lpLibFileName1 = lpLibFileName == null ? null : lpLibFileName.chars;
		return LoadLibraryW (lpLibFileName1);
	}
	byte [] lpLibFileName1 = lpLibFileName == null ? null : lpLibFileName.bytes;
	return LoadLibraryA (lpLibFileName1);
}

public static final int LoadString (long /*int*/ hinst, int uID, TCHAR lpBuffer, int nBufferMax) {
	if (IsUnicode) {
		char [] lpBuffer1 = lpBuffer == null ? null : lpBuffer.chars;
		return LoadStringW (hinst, uID, lpBuffer1, nBufferMax);
	}
	byte [] lpBuffer1 = lpBuffer == null ? null : lpBuffer.bytes;
	return LoadStringA (hinst, uID, lpBuffer1, nBufferMax);
}

public static final int MapVirtualKey (int uCode, int uMapType) {
	if (IsUnicode) return MapVirtualKeyW (uCode, uMapType);
	return MapVirtualKeyA (uCode, uMapType);
}

public static final int MessageBox (long /*int*/ hWnd, TCHAR lpText, TCHAR lpCaption, int uType) {
	if (IsUnicode) {
		char [] lpText1 = lpText == null ? null : lpText.chars;
		char [] lpCaption1 = lpCaption == null ? null : lpCaption.chars;
		return MessageBoxW (hWnd, lpText1, lpCaption1, uType);
	}
	byte [] lpText1 = lpText == null ? null : lpText.bytes;
	byte [] lpCaption1 = lpCaption == null ? null : lpCaption.bytes;
	return MessageBoxA (hWnd, lpText1, lpCaption1, uType);
}

public static final void MoveMemory (long /*int*/ Destination, TCHAR Source, int Length) {
	if (IsUnicode) {
		char [] Source1 = Source == null ? null : Source.chars;
		MoveMemory (Destination, Source1, Length);
	} else {
		byte [] Source1 = Source == null ? null : Source.bytes;
		MoveMemory (Destination, Source1, Length);
	}
}

public static final void MoveMemory (TCHAR Destination, long /*int*/ Source, int Length) {
	if (IsUnicode) {
		char [] Destination1 = Destination == null ? null : Destination.chars;
		MoveMemory (Destination1, Source, Length);
	} else {
		byte [] Destination1 = Destination == null ? null : Destination.bytes;
		MoveMemory (Destination1, Source, Length);
	}
}

public static final void MoveMemory (long /*int*/ Destination, DEVMODE Source, int Length) {
	if (IsUnicode) {
		MoveMemory (Destination, (DEVMODEW)Source, Length);
	} else {
		MoveMemory (Destination, (DEVMODEA)Source, Length);
	}
}

public static final void MoveMemory (DEVMODE Destination, long /*int*/ Source, int Length) {
	if (IsUnicode) {
		MoveMemory ((DEVMODEW)Destination, Source, Length);
	} else {
		MoveMemory ((DEVMODEA)Destination, Source, Length);
	}
}

public static final void MoveMemory (long /*int*/ Destination, LOGFONT Source, int Length) {
	if (IsUnicode) {
		MoveMemory (Destination, (LOGFONTW)Source, Length);
	} else {
		MoveMemory (Destination, (LOGFONTA)Source, Length);
	}
}

public static final void MoveMemory (LOGFONT Destination, long /*int*/ Source, int Length) {
	if (IsUnicode) {
		MoveMemory ((LOGFONTW)Destination, Source, Length);
	} else {
		MoveMemory ((LOGFONTA)Destination, Source, Length);
	}
}

public static final void MoveMemory (long /*int*/ Destination, NMTTDISPINFO Source, int Length) {
	if (IsUnicode) {
		MoveMemory (Destination, (NMTTDISPINFOW)Source, Length);
	} else {
		MoveMemory (Destination, (NMTTDISPINFOA)Source, Length);
	}
}

public static final void MoveMemory (NMTTDISPINFO Destination, long /*int*/ Source, int Length) {
	if (IsUnicode) {
		MoveMemory ((NMTTDISPINFOW)Destination, Source, Length);
	} else {
		MoveMemory ((NMTTDISPINFOA)Destination, Source, Length);
	}
}

public static final void MoveMemory (TEXTMETRIC Destination, long /*int*/ Source, int Length) {
	if (IsUnicode) {
		MoveMemory ((TEXTMETRICW)Destination, Source, Length);
	} else {
		MoveMemory ((TEXTMETRICA)Destination, Source, Length);
	}
}

public static final boolean OpenPrinter (TCHAR pPrinterName, long /*int*/ [] phPrinter, long /*int*/ pDefault) {
	if (IsUnicode) {
		char [] pPrinterName1 = pPrinterName == null ? null : pPrinterName.chars;
		return OpenPrinterW (pPrinterName1, phPrinter, pDefault);
	}
	byte [] pPrinterName1 = pPrinterName == null ? null : pPrinterName.bytes;
	return OpenPrinterA (pPrinterName1, phPrinter, pDefault);
}

public static final boolean PeekMessage (MSG lpMsg, long /*int*/ hWnd, int wMsgFilterMin, int wMsgFilterMax, int wRemoveMsg) {
	if (IsUnicode) return PeekMessageW (lpMsg, hWnd, wMsgFilterMin, wMsgFilterMax, wRemoveMsg);
	return PeekMessageA (lpMsg, hWnd, wMsgFilterMin, wMsgFilterMax, wRemoveMsg);
}

public static final boolean PostMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, long /*int*/ lParam) {
	if (IsUnicode) return PostMessageW (hWnd, Msg, wParam, lParam);
	return PostMessageA (hWnd, Msg, wParam, lParam);
}

public static final boolean PostThreadMessage (int idThread, int Msg, long /*int*/ wParam, long /*int*/ lParam) {
	if (IsUnicode) return PostThreadMessageW (idThread, Msg, wParam, lParam);
	return PostThreadMessageA (idThread, Msg, wParam, lParam);
}

public static final boolean PrintDlg (PRINTDLG lppd) {
	if (IsUnicode) return PrintDlgW (lppd);
	return PrintDlgA (lppd);
}

public static final int RegCreateKeyEx (long /*int*/ hKey, TCHAR lpSubKey, int Reserved, TCHAR lpClass, int dwOptions, int samDesired, long /*int*/ lpSecurityAttributes, long /*int*/[] phkResult, long /*int*/[] lpdwDisposition) {
	if (IsUnicode) {
		char [] lpClass1 = lpClass == null ? null : lpClass.chars;
		char [] lpSubKey1 = lpSubKey == null ? null : lpSubKey.chars;
		return RegCreateKeyExW (hKey, lpSubKey1, Reserved, lpClass1, dwOptions, samDesired, lpSecurityAttributes, phkResult, lpdwDisposition);
	}
	byte [] lpClass1 = lpClass == null ? null : lpClass.bytes;
	byte [] lpSubKey1 = lpSubKey == null ? null : lpSubKey.bytes;
	return RegCreateKeyExA (hKey, lpSubKey1, Reserved, lpClass1, dwOptions, samDesired, lpSecurityAttributes, phkResult, lpdwDisposition);
}

public static final int RegDeleteValue (long /*int*/ hKey, TCHAR lpValueName) {
	if (IsUnicode) {
		char [] lpValueName1 = lpValueName == null ? null : lpValueName.chars;
		return RegDeleteValueW (hKey, lpValueName1);
	}
	byte [] lpValueName1 = lpValueName == null ? null : lpValueName.bytes;
	return RegDeleteValueA (hKey, lpValueName1);
}

public static final int RegEnumKeyEx (long /*int*/ hKey, int dwIndex, TCHAR lpName, int [] lpcName, int [] lpReserved, TCHAR lpClass, int [] lpcClass, FILETIME lpftLastWriteTime) {
	if (IsUnicode) {
		char [] lpName1 = lpName == null ? null : lpName.chars;
		char [] lpClass1 = lpClass == null ? null : lpClass.chars;
		return RegEnumKeyExW (hKey, dwIndex, lpName1, lpcName, lpReserved, lpClass1, lpcClass, lpftLastWriteTime);
	}
	byte [] lpName1 = lpName == null ? null : lpName.bytes;
	byte [] lpClass1 = lpClass == null ? null : lpClass.bytes;
	return RegEnumKeyExA (hKey, dwIndex, lpName1, lpcName, lpReserved, lpClass1, lpcClass, lpftLastWriteTime);
}

public static final int RegisterClass (WNDCLASS lpWndClass) {
	if (IsUnicode) return RegisterClassW (lpWndClass);
	return RegisterClassA (lpWndClass);
}

public static final int RegisterClipboardFormat (TCHAR lpszFormat) {
	if (IsUnicode) {
		char [] lpszFormat1 = lpszFormat == null ? null : lpszFormat.chars;
		return RegisterClipboardFormatW (lpszFormat1);
	}
	byte [] lpszFormat1 = lpszFormat == null ? null : lpszFormat.bytes;
	return RegisterClipboardFormatA (lpszFormat1);
}

public static final int RegisterWindowMessage (TCHAR lpString) {
	if (IsUnicode) {
		char [] lpString1 = lpString == null ? null : lpString.chars;
		return RegisterWindowMessageW (lpString1);
	}
	byte [] lpString1 = lpString == null ? null : lpString.bytes;
	return RegisterWindowMessageA (lpString1);
}

public static final int RegOpenKeyEx (long /*int*/ hKey, TCHAR lpSubKey, int ulOptions, int samDesired, long /*int*/[] phkResult) {
	if (IsUnicode) {
		char [] lpSubKey1 = lpSubKey == null ? null : lpSubKey.chars;
		return RegOpenKeyExW (hKey, lpSubKey1, ulOptions, samDesired, phkResult);
	}
	byte [] lpSubKey1 = lpSubKey == null ? null : lpSubKey.bytes;
	return RegOpenKeyExA (hKey, lpSubKey1, ulOptions, samDesired, phkResult);
}

public static final int RegQueryInfoKey (long /*int*/ hKey, long /*int*/ lpClass, int[] lpcbClass, long /*int*/ lpReserved, int[] lpSubKeys, int[] lpcbMaxSubKeyLen, int[] lpcbMaxClassLen, int[] lpcValues, int[] lpcbMaxValueNameLen, int[] lpcbMaxValueLen, int[] lpcbSecurityDescriptor, long /*int*/ lpftLastWriteTime){
	if (IsUnicode) return RegQueryInfoKeyW (hKey, lpClass, lpcbClass, lpReserved, lpSubKeys, lpcbMaxSubKeyLen, lpcbMaxClassLen, lpcValues, lpcbMaxValueNameLen, lpcbMaxValueLen, lpcbSecurityDescriptor, lpftLastWriteTime);
	return RegQueryInfoKeyA (hKey, lpClass, lpcbClass, lpReserved, lpSubKeys, lpcbMaxSubKeyLen, lpcbMaxClassLen, lpcValues, lpcbMaxValueNameLen, lpcbMaxValueLen, lpcbSecurityDescriptor, lpftLastWriteTime);
}

public static final int RegQueryValueEx (long /*int*/ hKey, TCHAR lpValueName, long /*int*/ lpReserved, int[] lpType, TCHAR lpData, int[] lpcbData) {
	if (IsUnicode) {
		char [] lpValueName1 = lpValueName == null ? null : lpValueName.chars;
		char [] lpData1 = lpData == null ? null : lpData.chars;
		return RegQueryValueExW (hKey, lpValueName1, lpReserved, lpType, lpData1, lpcbData);
	}
	byte [] lpValueName1 = lpValueName == null ? null : lpValueName.bytes;
	byte [] lpData1 = lpData == null ? null : lpData.bytes;
	return RegQueryValueExA (hKey, lpValueName1, lpReserved, lpType, lpData1, lpcbData);
}

public static final int RegQueryValueEx (long /*int*/ hKey, TCHAR lpValueName, long /*int*/ lpReserved, int[] lpType, int [] lpData, int[] lpcbData) {
	if (IsUnicode) {
		char [] lpValueName1 = lpValueName == null ? null : lpValueName.chars;
		return RegQueryValueExW (hKey, lpValueName1, lpReserved, lpType, lpData, lpcbData);
	}
	byte [] lpValueName1 = lpValueName == null ? null : lpValueName.bytes;
	return RegQueryValueExA (hKey, lpValueName1, lpReserved, lpType, lpData, lpcbData);
}

public static final int RegSetValueEx (long /*int*/ hKey, TCHAR lpValueName, int Reserved, int dwType, int[] lpData, int cbData) {
	if (IsUnicode) {
		char [] lpValueName1 = lpValueName == null ? null : lpValueName.chars;
		return RegSetValueExW (hKey, lpValueName1, Reserved, dwType, lpData, cbData);
	}
	byte [] lpValueName1 = lpValueName == null ? null : lpValueName.bytes;
	return RegSetValueExA (hKey, lpValueName1, Reserved, dwType, lpData, cbData);
}

public static final long /*int*/ RemoveProp  (long /*int*/ hWnd, long /*int*/ lpString){
	if (IsUnicode) return RemovePropW (hWnd, lpString);
	return RemovePropA (hWnd, lpString);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TCHAR lParam) {
	if (IsUnicode) {
		char [] lParam1 = lParam == null ? null : lParam.chars;
		return SendMessageW (hWnd, Msg, wParam, lParam1);
	}
	byte [] lParam1 = lParam == null ? null : lParam.bytes;
	return SendMessageA (hWnd, Msg, wParam, lParam1);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, int [] wParam, int [] lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, SIZE lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ [] wParam, long /*int*/ lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, int [] lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, char [] lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, short [] lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, long /*int*/ lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, LITEM lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, LVCOLUMN lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, LVHITTESTINFO lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, LVITEM lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, LVINSERTMARK lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, MARGINS lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, POINT lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, MCHITTESTINFO lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, REBARBANDINFO lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, RECT lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, SYSTEMTIME lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, SHDRAGIMAGE lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TBBUTTON lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TBBUTTONINFO lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TCITEM lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TCHITTESTINFO lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TOOLINFO lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TVHITTESTINFO lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TVINSERTSTRUCT lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TVITEM lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TVSORTCB lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, UDACCEL lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, HDHITTESTINFO lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, HDITEM lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, HDLAYOUT lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final long /*int*/ SendMessage (long /*int*/ hWnd, int Msg, long /*int*/ wParam, BUTTON_IMAGELIST lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final boolean SetMenuItemInfo (long /*int*/ hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii) {
	if (IsUnicode) return SetMenuItemInfoW (hMenu, uItem, fByPosition, lpmii);
	return SetMenuItemInfoA (hMenu, uItem, fByPosition, lpmii);
}

public static final boolean SetDllDirectory (TCHAR lpPathName) {
	if (IsUnicode) {
		char [] lpPathName1 = lpPathName == null ? null : lpPathName.chars;
		return SetDllDirectoryW (lpPathName1);
	}
	byte [] lpPathName1 = lpPathName == null ? null : lpPathName.bytes;
	return SetDllDirectoryA (lpPathName1);
}

public static boolean SetProp (long /*int*/ hWnd, long /*int*/ lpString, long /*int*/ hData) {
	if (IsUnicode) return SetPropW (hWnd, lpString, hData);
	return SetPropA (hWnd, lpString, hData);
}

public static final int SetWindowLong (long /*int*/ hWnd, int nIndex, int dwNewLong) {
	if (IsUnicode) return SetWindowLongW (hWnd, nIndex, dwNewLong);
	return SetWindowLongA (hWnd, nIndex, dwNewLong);
}

public static final long /*int*/ SetWindowLongPtr (long /*int*/ hWnd, int nIndex, long /*int*/ dwNewLong) {
	if (IsUnicode) return SetWindowLongPtrW (hWnd, nIndex, dwNewLong);
	return SetWindowLongPtrA (hWnd, nIndex, dwNewLong);
}

public static final long /*int*/ SetWindowsHookEx (int idHook, long /*int*/ lpfn, long /*int*/ hMod, int dwThreadId) {
	if (IsUnicode) return SetWindowsHookExW (idHook, lpfn, hMod, dwThreadId);
	return SetWindowsHookExA (idHook, lpfn, hMod, dwThreadId);
}

public static final boolean SetWindowText (long /*int*/ hWnd, TCHAR lpString) {
	if (IsUnicode) {
		char [] lpString1 = lpString == null ? null : lpString.chars;
		return SetWindowTextW (hWnd, lpString1);
	}
	byte [] lpString1 = lpString == null ? null : lpString.bytes;
	return SetWindowTextA (hWnd, lpString1);
}

public static final long /*int*/ SHBrowseForFolder (BROWSEINFO lpbi) {
	if (IsUnicode) return SHBrowseForFolderW (lpbi);
	return SHBrowseForFolderA (lpbi);
}

public static final boolean ShellExecuteEx (SHELLEXECUTEINFO lpExecInfo) {
	if (IsUnicode) return ShellExecuteExW (lpExecInfo);
	return ShellExecuteExA (lpExecInfo);
}

public static long /*int*/ SHGetFileInfo (TCHAR pszPath, int dwFileAttributes, SHFILEINFO psfi, int cbFileInfo, int uFlags) {
	if (IsUnicode) {
		char [] pszPath1 = pszPath == null ? null : pszPath.chars;
		return SHGetFileInfoW (pszPath1, dwFileAttributes, (SHFILEINFOW) psfi, cbFileInfo, uFlags);
	}
	byte [] pszPath1 = pszPath == null ? null : pszPath.bytes;
	return SHGetFileInfoA (pszPath1, dwFileAttributes, (SHFILEINFOA) psfi, cbFileInfo, uFlags);
}

public static final boolean Shell_NotifyIcon (int dwMessage, NOTIFYICONDATA lpData) {
	if (IsUnicode) return Shell_NotifyIconW (dwMessage, (NOTIFYICONDATAW)lpData);
	return Shell_NotifyIconA (dwMessage, (NOTIFYICONDATAA)lpData);
}

public static final int SHGetFolderPath (long /*int*/ hwndOwner, int nFolder, long /*int*/ hToken, int dwFlags, TCHAR pszPath) {
	if (IsUnicode) {
		char [] pszPath1 = pszPath == null ? null : pszPath.chars;
		return SHGetFolderPathW (hwndOwner, nFolder, hToken, dwFlags, pszPath1);
	}
	byte [] pszPath1 = pszPath == null ? null : pszPath.bytes;
	return SHGetFolderPathA (hwndOwner, nFolder, hToken, dwFlags, pszPath1);
}

public static final boolean SHGetPathFromIDList (long /*int*/ pidl, TCHAR pszPath) {
	if (IsUnicode) {
		char [] pszPath1 = pszPath == null ? null : pszPath.chars;
		return SHGetPathFromIDListW (pidl, pszPath1);
	}
	byte [] pszPath1 = pszPath == null ? null : pszPath.bytes;
	return SHGetPathFromIDListA (pidl, pszPath1);
}

public static final int StartDoc (long /*int*/ hdc, DOCINFO lpdi) {
	if (IsUnicode) return StartDocW (hdc, lpdi);
	return StartDocA (hdc, lpdi);
}

public static final boolean SystemParametersInfo (int uiAction, int uiParam, RECT pvParam, int fWinIni) {
	if (IsUnicode) return SystemParametersInfoW (uiAction, uiParam, pvParam, fWinIni);
	return SystemParametersInfoA (uiAction, uiParam, pvParam, fWinIni);
}

public static final boolean SystemParametersInfo (int uiAction, int uiParam, HIGHCONTRAST pvParam, int fWinIni) {
	if (IsUnicode) return SystemParametersInfoW (uiAction, uiParam, pvParam, fWinIni);
	return SystemParametersInfoA (uiAction, uiParam, pvParam, fWinIni);
}

public static final boolean SystemParametersInfo (int uiAction, int uiParam, NONCLIENTMETRICS pvParam, int fWinIni) {
	if (IsUnicode) return SystemParametersInfoW (uiAction, uiParam, (NONCLIENTMETRICSW)pvParam, fWinIni);
	return SystemParametersInfoA (uiAction, uiParam, (NONCLIENTMETRICSA)pvParam, fWinIni);
}

public static final boolean SystemParametersInfo (int uiAction, int uiParam, int [] pvParam, int fWinIni) {
	if (IsUnicode) return SystemParametersInfoW (uiAction, uiParam, pvParam, fWinIni);
	return SystemParametersInfoA (uiAction, uiParam, pvParam, fWinIni);
}

public static final int TranslateAccelerator (long /*int*/ hWnd, long /*int*/ hAccTable, MSG lpMsg) {
	if (IsUnicode) return TranslateAcceleratorW (hWnd, hAccTable, lpMsg);
	return TranslateAcceleratorA (hWnd, hAccTable, lpMsg);
}

public static final boolean UnregisterClass (TCHAR lpClassName, long /*int*/ hInstance) {
	if (IsUnicode) {
		char [] lpClassName1 = lpClassName == null ? null : lpClassName.chars;
		return UnregisterClassW (lpClassName1, hInstance);
	}
	byte [] lpClassName1 = lpClassName == null ? null : lpClassName.bytes;
	return UnregisterClassA (lpClassName1, hInstance);
}

public static final int UrlCreateFromPath (TCHAR pszPath, TCHAR pszURL, int[] pcchUrl, int flags) {
	if (IsUnicode) {
		char [] path = pszPath == null ? null : pszPath.chars;
		char [] url = pszURL == null ? null : pszURL.chars;
		return UrlCreateFromPathW (path, url, pcchUrl, flags);
	}
	byte [] path = pszPath == null ? null : pszPath.bytes;
	byte [] url = pszURL == null ? null : pszURL.bytes;
	return UrlCreateFromPathA (path, url, pcchUrl, flags);
}

public static final short VkKeyScan (short ch) {
	if (IsUnicode) return VkKeyScanW (ch);
	return VkKeyScanA (ch);
}

/** Natives */

/** @param hdc cast=(HDC) */
public static final native int AbortDoc (long /*int*/ hdc);
/**
 * @method flags=dynamic
 * @param lpCookie cast=(ULONG_PTR*)
 */
public static final native boolean ActivateActCtx (long /*int*/ hActCtx, long /*int*/ [] lpCookie);
/** @param hkl cast=(HKL) */
public static final native long /*int*/ ActivateKeyboardLayout(long /*int*/ hkl, int Flags);
/** @method flags=dynamic */
public static final native int AddFontResourceExW(char[] lpszFilename, int fl, long /*int*/ pdv);
/** @method flags=dynamic */
public static final native int AddFontResourceExA(byte[] lpszFilename, int fl, long /*int*/ pdv);
public static final native boolean AdjustWindowRectEx (RECT lpRect, int dwStyle, boolean bMenu, int dwExStyle);
/** @method flags=dynamic */
public static final native boolean AllowSetForegroundWindow (int dwProcessId);
/**
 * @method flags=dynamic
 * @param blendFunction flags=struct
 */
public static final native boolean AlphaBlend(long /*int*/ hdcDest, int nXOriginDest, int nYOriginDest, int nWidthDest, int nHeightDest, long /*int*/ hdcSrc, int nXOriginSrc, int nYOriginSrc, int nWidthSrc, int nHeightSrc, BLENDFUNCTION blendFunction);
/**
 * @method flags=dynamic
 * @param hwnd cast=(HWND)
 */
public static final native boolean AnimateWindow(long /*int*/ hwnd, int dwTime, int dwFlags);
/** @param hdc cast=(HDC) */
public static final native boolean Arc (long /*int*/ hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect, int nXStartArc, int nYStartArc, int nXEndArc, int nYEndArc);
/** @method flags=dynamic */
public static final native int AssocQueryStringA(int flags, int str, byte[] pszAssoc, byte[] pszExtra, byte[] pszOut, int[] pcchOut);
/** @method flags=dynamic */
public static final native int AssocQueryStringW(int flags, int str, char[] pszAssoc, char[] pszExtra, char[] pszOut, int[] pcchOut);
/**
 * @param idAttach cast=(DWORD)
 * @param idAttachTo cast=(DWORD)
 */
public static final native boolean AttachThreadInput (int idAttach, int idAttachTo, boolean fAttach);
/**
 * @method flags=dynamic
 * @param hdcTarget cast=(HDC)
 * @param phdc cast=(HDC*)
 */
public static final native long /*int*/ BeginBufferedPaint (long /*int*/ hdcTarget, RECT prcTarget, int dwFormat, BP_PAINTPARAMS pPaintParams, long /*int*/ [] phdc);
public static final native long /*int*/ BeginDeferWindowPos (int nNumWindows);
/** @param hWnd cast=(HWND) */
public static final native long /*int*/ BeginPaint (long /*int*/ hWnd, PAINTSTRUCT lpPaint);
/** @param hdc cast=(HDC) */
public static final native boolean BeginPath(long /*int*/ hdc);
/**
 * @param hdcDest cast=(HDC)
 * @param hdcSrc cast=(HDC)
 */
public static final native boolean BitBlt (long /*int*/ hdcDest, int nXDest, int nYDest, int nWidth, int nHeight, long /*int*/ hdcSrc, int nXSrc, int nYSrc, int dwRop);
/** @param hWnd cast=(HWND) */
public static final native boolean BringWindowToTop (long /*int*/ hWnd);
/** @method flags=dynamic */
public static final native int BufferedPaintInit ();
/**
 * @method flags=dynamic
 * @param hBufferedPaint cast=(HPAINTBUFFER)
 */
public static final native int BufferedPaintSetAlpha (long /*int*/ hBufferedPaint, RECT prc, byte alpha);
/** @method flags=dynamic */
public static final native int BufferedPaintUnInit ();
public static final native int Call (long /*int*/ address);
/** @param address cast=(DLLGETVERSIONPROC) */
public static final native int Call (long /*int*/ address, DLLVERSIONINFO arg0);
/**
 * @param hhk cast=(HHOOK)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ CallNextHookEx(long /*int*/ hhk, int nCode, long /*int*/ wParam, long /*int*/ lParam);
/**
 * @param lpPrevWndFunc cast=(WNDPROC)
 * @param hWnd cast=(HWND)
 */
public static final native long /*int*/ CallWindowProcW (long /*int*/ lpPrevWndFunc, long /*int*/ hWnd, int Msg, long /*int*/ wParam, long /*int*/ lParam);
/**
 * @param lpPrevWndFunc cast=(WNDPROC)
 * @param hWnd cast=(HWND)
 */
public static final native long /*int*/ CallWindowProcA (long /*int*/ lpPrevWndFunc, long /*int*/ hWnd, int Msg, long /*int*/ wParam, long /*int*/ lParam);
/**
 * @param pName cast=(PCERT_NAME_BLOB)
 * @param psz cast=(LPWSTR)
 */
public static final native int CertNameToStrW(int dwCertEncodingType, CERT_NAME_BLOB pName, int dwStrType, char[] psz, int csz);
/**
 * @param pName cast=(PCERT_NAME_BLOB)
 * @param psz cast=(LPSTR)
 */
public static final native int CertNameToStrA(int dwCertEncodingType, CERT_NAME_BLOB pName, int dwStrType, byte[] psz, int csz);
/** @param ch cast=(LPWSTR) */
public static final native long /*int*/ CharLowerW (long /*int*/ ch);
/** @param ch cast=(LPSTR) */
public static final native long /*int*/ CharLowerA (long /*int*/ ch);
/** @param ch cast=(LPWSTR) */
public static final native long /*int*/ CharUpperW (long /*int*/ ch);
/** @param ch cast=(LPSTR) */
public static final native long /*int*/ CharUpperA (long /*int*/ ch);
/**
 * @param hmenu cast=(HMENU)
 * @param uIDCheckItem cast=(UINT)
 * @param uCheck cast=(UINT)
 */
public static final native boolean CheckMenuItem (long /*int*/ hmenu, int uIDCheckItem, int uCheck); 
/** @param lpcc cast=(LPCHOOSECOLORW) */
public static final native boolean ChooseColorW (CHOOSECOLOR lpcc);
public static final native boolean ChooseColorA (CHOOSECOLOR lpcc);
/** @param chooseFont cast=(LPCHOOSEFONTW) */
public static final native boolean ChooseFontW (CHOOSEFONT chooseFont);
public static final native boolean ChooseFontA (CHOOSEFONT chooseFont);
/** @param hWnd cast=(HWND) */
public static final native boolean ClientToScreen (long /*int*/ hWnd, POINT lpPoint);
public static final native boolean CloseClipboard ();
/** @param hdc cast=(HDC) */
public static final native long /*int*/ CloseEnhMetaFile (long /*int*/ hdc);
/** 
 * @method flags=dynamic
 * @param hGesture cast=(HGESTUREINFO) 
 */
public static final native long /*int*/ CloseGestureInfoHandle (long /*int*/ hGesture);
/** @param hObject cast=(HANDLE) */
public static final native boolean CloseHandle (long /*int*/ hObject);
/** @param hPrinter cast=(HANDLE) */
public static final native boolean ClosePrinter (long /*int*/ hPrinter);
/**
 * @method flags=dynamic
 * @param hTheme cast=(HTHEME)
 */
public static final native int CloseThemeData (long /*int*/ hTheme);
/**
 * @method flags=dynamic
 * @param hTouchInput cast=(HTOUCHINPUT)
 */
public static final native boolean CloseTouchInputHandle(long /*int*/ hTouchInput);
/**
 * @param rclsid cast=(REFCLSID)
 * @param pUnkOuter cast=(LPUNKNOWN)
 * @param riid cast=(REFIID)
 * @param ppv cast=(LPVOID *)
 */
public static final native int CoCreateInstance (byte[] rclsid, long /*int*/ pUnkOuter, int dwClsContext, byte[] riid, long /*int*/[] ppv);
/** @method flags=dynamic */
public static final native int CoInternetIsFeatureEnabled (int FeatureEntry, int dwFlags);
/**
 * @method flags=dynamic
 * @param fEnable cast=(BOOL)
 */
public static final native int CoInternetSetFeatureEnabled (int FeatureEntry, int dwFlags, boolean fEnable);
/**
 * @param hrgnDest cast=(HRGN)
 * @param hrgnSrc1 cast=(HRGN)
 * @param hrgnSrc2 cast=(HRGN)
 */
public static final native int CombineRgn (long /*int*/ hrgnDest, long /*int*/ hrgnSrc1, long /*int*/ hrgnSrc2, int fnCombineMode);
/** @param hwndCB cast=(HWND) */
public static final native boolean CommandBar_AddAdornments (long /*int*/ hwndCB, int dwFlags, int dwReserved);
/**
 * @param hInst cast=(HINSTANCE)
 * @param hwndParent cast=(HWND)
 */
public static final native long /*int*/ CommandBar_Create (long /*int*/ hInst, long /*int*/ hwndParent, int idCmdBar);
/** @param hwndCB cast=(HWND) */
public static final native void CommandBar_Destroy (long /*int*/ hwndCB);
/**
 * @param hwndCB cast=(HWND)
 * @param iButton cast=(WORD)
 */
public static final native boolean CommandBar_DrawMenuBar (long /*int*/ hwndCB, int iButton);
/** @param hdnwCB cast=(HWND) */
public static final native int CommandBar_Height (long /*int*/ hdnwCB);
/**
 * @param hwndCB cast=(HWND)
 * @param hInst cast=(HINSTANCE)
 * @param pszMenu cast=(LPTSTR)
 * @param iButton cast=(WORD)
 */
public static final native boolean CommandBar_InsertMenubarEx (long /*int*/ hwndCB, long /*int*/ hInst, long /*int*/ pszMenu, int iButton);
/**
 * @param hwndCB cast=(HWND)
 * @param fShow cast=(BOOL)
 */
public static final native boolean CommandBar_Show (long /*int*/ hwndCB, boolean fShow);
public static final native int CommDlgExtendedError ();
/** @param hImage cast=(HANDLE) */
public static final native long /*int*/ CopyImage (long /*int*/ hImage, int uType, int cxDesired, int cyDesired, int fuFlags);
/** @param cb cast=(ULONG) */
public static final native long /*int*/ CoTaskMemAlloc(int cb);
/** @param pv cast=(LPVOID) */
public static final native void CoTaskMemFree(long /*int*/ pv);
/** @param lpaccl cast=(LPACCEL) */
public static final native long /*int*/ CreateAcceleratorTableW (byte [] lpaccl, int cEntries); 
/** @param lpaccl cast=(LPACCEL) */
public static final native long /*int*/ CreateAcceleratorTableA (byte [] lpaccl, int cEntries);
/**
 * @method flags=dynamic
 * @param pActCtx flags=no_out
 */
public static final native long /*int*/ CreateActCtxW (ACTCTX pActCtx);
/**
 * @method flags=dynamic
 * @param pActCtx flags=no_out
 */
public static final native long /*int*/ CreateActCtxA (ACTCTX pActCtx);
/** @param lpvBits cast=(CONST VOID *),flags=no_out critical */
public static final native long /*int*/ CreateBitmap (int nWidth, int nHeight, int cPlanes, int cBitsPerPel, byte [] lpvBits);
/**
 * @param hWnd cast=(HWND)
 * @param hBitmap cast=(HBITMAP)
 */
public static final native boolean CreateCaret (long /*int*/ hWnd, long /*int*/ hBitmap, int nWidth, int nHeight);
/** @param hdc cast=(HDC) */
public static final native long /*int*/ CreateCompatibleBitmap (long /*int*/ hdc, int nWidth, int nHeight);
/** @param hdc cast=(HDC) */
public static final native long /*int*/ CreateCompatibleDC (long /*int*/ hdc);
/**
 * @param hInst cast=(HINSTANCE)
 * @param pvANDPlane cast=(CONST VOID *),flags=no_out critical
 * @param pvXORPlane cast=(CONST VOID *),flags=no_out critical
 */
public static final native long /*int*/ CreateCursor (long /*int*/ hInst, int xHotSpot, int yHotSpot, int nWidth, int nHeight, byte [] pvANDPlane, byte [] pvXORPlane);
/**
 * @param lpszDriver cast=(LPWSTR)
 * @param lpszDevice cast=(LPWSTR)
 * @param lpszOutput cast=(LPWSTR)
 * @param lpInitData cast=(CONST DEVMODEW *)
 */
public static final native long /*int*/ CreateDCW (char [] lpszDriver, char [] lpszDevice, long /*int*/ lpszOutput, long /*int*/ lpInitData);  
/**
 * @param lpszDriver cast=(LPSTR)
 * @param lpszDevice cast=(LPSTR)
 * @param lpszOutput cast=(LPSTR)
 * @param lpInitData cast=(CONST DEVMODE *)
 */
public static final native long /*int*/ CreateDCA (byte [] lpszDriver, byte [] lpszDevice, long /*int*/ lpszOutput, long /*int*/ lpInitData);  
/**
 * @param hdc cast=(HDC)
 * @param pbmi cast=(BITMAPINFO *),flags=no_out critical
 * @param ppvBits cast=(VOID **),flags=no_in critical
 * @param hSection cast=(HANDLE)
 */
public static final native long /*int*/ CreateDIBSection(long /*int*/ hdc, byte[] pbmi, int iUsage, long /*int*/[] ppvBits, long /*int*/ hSection, int dwOffset);
/**
 * @param hdc cast=(HDC)
 * @param pbmi cast=(BITMAPINFO *),flags=no_out critical
 * @param ppvBits cast=(VOID **),flags=no_in critical
 * @param hSection cast=(HANDLE)
 */
public static final native long /*int*/ CreateDIBSection(long /*int*/ hdc, long /*int*/ pbmi, int iUsage, long /*int*/[] ppvBits, long /*int*/ hSection, int dwOffset);
/**
 * @param hdcRef cast=(HDC)
 * @param lpFilename cast=(LPCWSTR)
 * @param lpDescription cast=(LPCWSTR)
 */
public static final native long /*int*/ CreateEnhMetaFileW(long /*int*/ hdcRef, char[] lpFilename, RECT lpRect, char[] lpDescription);
/**
 * @param hdcRef cast=(HDC)
 * @param lpFilename cast=(LPCSTR)
 * @param lpDescription cast=(LPCSTR)
 */
public static final native long /*int*/ CreateEnhMetaFileA(long /*int*/ hdcRef, byte[] lpFilename, RECT lpRect, byte[] lpDescription);
/** @param lplf cast=(LPLOGFONTW) */
public static final native long /*int*/ CreateFontIndirectW (long /*int*/ lplf);
/** @param lplf cast=(LPLOGFONTA) */
public static final native long /*int*/ CreateFontIndirectA (long /*int*/ lplf);
/** @param lplf flags=no_out */
public static final native long /*int*/ CreateFontIndirectW (LOGFONTW lplf);
/** @param lplf flags=no_out */
public static final native long /*int*/ CreateFontIndirectA (LOGFONTA lplf);
/** @param lplf flags=no_out */
public static final native long /*int*/ CreateIconIndirect (ICONINFO lplf);
public static final native long /*int*/ CreateMenu ();
/** @param logPalette cast=(LOGPALETTE *),flags=no_out critical */
public static final native long /*int*/ CreatePalette (byte[] logPalette);
/** @param hbmp cast=(HBITMAP) */
public static final native long /*int*/ CreatePatternBrush (long /*int*/ hbmp);
/** @param crColor cast=(COLORREF) */
public static final native long /*int*/ CreatePen (int fnPenStyle, int nWidth, int crColor);
/** @param lppt cast=(CONST POINT *) */
public static final native long /*int*/ CreatePolygonRgn(int[] lppt, int cPoints, int fnPolyFillMode);
public static final native long /*int*/ CreatePopupMenu ();
/**
 * @param lpApplicationName cast=(LPCWSTR)
 * @param lpCommandLine cast=(LPWSTR)
 * @param lpProcessAttributes cast=(LPSECURITY_ATTRIBUTES)
 * @param lpThreadAttributes cast=(LPSECURITY_ATTRIBUTES)
 * @param lpEnvironment cast=(LPVOID)
 * @param lpCurrentDirectory cast=(LPWSTR)
 * @param lpStartupInfo cast=(LPSTARTUPINFOW)
 * @param lpProcessInformation cast=(LPPROCESS_INFORMATION)
 */
public static final native boolean CreateProcessW (long /*int*/ lpApplicationName, long /*int*/ lpCommandLine, long /*int*/ lpProcessAttributes, long /*int*/ lpThreadAttributes, boolean bInheritHandles, int dwCreationFlags, long /*int*/ lpEnvironment, long /*int*/ lpCurrentDirectory, STARTUPINFO lpStartupInfo, PROCESS_INFORMATION lpProcessInformation);
/**
 * @param lpApplicationName cast=(LPCSTR)
 * @param lpCommandLine cast=(LPSTR)
 * @param lpProcessAttributes cast=(LPSECURITY_ATTRIBUTES)
 * @param lpThreadAttributes cast=(LPSECURITY_ATTRIBUTES)
 * @param lpEnvironment cast=(LPVOID)
 * @param lpCurrentDirectory cast=(LPSTR)
 * @param lpStartupInfo cast=(LPSTARTUPINFOA)
 * @param lpProcessInformation cast=(LPPROCESS_INFORMATION)
 */
public static final native boolean CreateProcessA (long /*int*/ lpApplicationName, long /*int*/ lpCommandLine, long /*int*/ lpProcessAttributes, long /*int*/ lpThreadAttributes, boolean bInheritHandles, int dwCreationFlags, long /*int*/ lpEnvironment, long /*int*/ lpCurrentDirectory, STARTUPINFO lpStartupInfo, PROCESS_INFORMATION lpProcessInformation);
public static final native long /*int*/ CreateRectRgn (int left, int top, int right, int bottom);
/** @param colorRef cast=(COLORREF) */
public static final native long /*int*/ CreateSolidBrush (int colorRef);
/**
 * @param hGlobal cast=(HGLOBAL)
 * @param fDeleteOnRelease cast=(BOOL)
 * @param ppstm cast=(LPSTREAM *)
 */
public static final native int CreateStreamOnHGlobal(long /*int*/ hGlobal, boolean fDeleteOnRelease, long /*int*/[] ppstm);
/**
 * @param lpClassName cast=(LPWSTR)
 * @param lpWindowName cast=(LPWSTR)
 * @param hWndParent cast=(HWND)
 * @param hMenu cast=(HMENU)
 * @param hInstance cast=(HINSTANCE)
 */
public static final native long /*int*/ CreateWindowExW (int dwExStyle, char [] lpClassName, char [] lpWindowName, int dwStyle, int X, int Y, int nWidth, int nHeight, long /*int*/ hWndParent, long /*int*/ hMenu, long /*int*/ hInstance, CREATESTRUCT lpParam);
/**
 * @param lpClassName cast=(LPSTR)
 * @param hWndParent cast=(HWND)
 * @param hMenu cast=(HMENU)
 * @param hInstance cast=(HINSTANCE)
 */
public static final native long /*int*/ CreateWindowExA (int dwExStyle, byte [] lpClassName, byte [] lpWindowName, int dwStyle, int X, int Y, int nWidth, int nHeight, long /*int*/ hWndParent, long /*int*/ hMenu, long /*int*/ hInstance, CREATESTRUCT lpParam);
/**
 * @param hWinPosInfo cast=(HDWP)
 * @param hWnd cast=(HWND)
 * @param hWndInsertAfter cast=(HWND)
 */
public static final native long /*int*/ DeferWindowPos (long /*int*/ hWinPosInfo, long /*int*/ hWnd, long /*int*/ hWndInsertAfter, int X, int Y, int cx, int cy, int uFlags);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ DefMDIChildProcW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, long /*int*/ lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ DefMDIChildProcA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, long /*int*/ lParam);
/**
 * @param hWnd cast=(HWND)
 * @param hWndMDIClient cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ DefFrameProcW (long /*int*/ hWnd, long /*int*/ hWndMDIClient, int Msg, long /*int*/ wParam, long /*int*/ lParam);
/**
 * @param hWnd cast=(HWND)
 * @param hWndMDIClient cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ DefFrameProcA (long /*int*/ hWnd, long /*int*/ hWndMDIClient, int Msg, long /*int*/ wParam, long /*int*/ lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ DefWindowProcW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, long /*int*/ lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ DefWindowProcA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, long /*int*/ lParam);
/** @param hdc cast=(HDC) */
public static final native boolean DeleteDC (long /*int*/ hdc);
/** @param hemf cast=(HENHMETAFILE) */
public static final native boolean DeleteEnhMetaFile (long /*int*/ hemf);
/** @param hMenu cast=(HMENU) */
public static final native boolean DeleteMenu (long /*int*/ hMenu, int uPosition, int uFlags);
/** @param hGdiObj cast=(HGDIOBJ) */
public static final native boolean DeleteObject (long /*int*/ hGdiObj);
/** @param hAccel cast=(HACCEL) */
public static final native boolean DestroyAcceleratorTable (long /*int*/ hAccel);
public static final native boolean DestroyCaret ();
/** @param hCursor cast=(HCURSOR) */
public static final native boolean DestroyCursor (long /*int*/ hCursor);
/** @param hIcon cast=(HICON) */
public static final native boolean DestroyIcon (long /*int*/ hIcon);
/** @param hMenu cast=(HMENU) */
public static final native boolean DestroyMenu (long /*int*/ hMenu);
/** @param hWnd cast=(HWND) */
public static final native boolean DestroyWindow (long /*int*/ hWnd);
public static final native long /*int*/ DispatchMessageW (MSG lpmsg);
public static final native long /*int*/ DispatchMessageA (MSG lpmsg);
/**
 * @param hWnd cast=(HWND)
 * @param hPrinter cast=(HANDLE)
 * @param pDeviceName cast=(LPWSTR)
 * @param pDevModeOutput cast=(PDEVMODEW)
 * @param pDevModeInput cast=(PDEVMODEW)
 */
public static final native int DocumentPropertiesW (long /*int*/ hWnd, long /*int*/ hPrinter, char[] pDeviceName, long /*int*/ pDevModeOutput, long /*int*/ pDevModeInput, int fMode);
/**
 * @param hWnd cast=(HWND)
 * @param hPrinter cast=(HANDLE)
 * @param pDeviceName cast=(LPTSTR)
 * @param pDevModeOutput cast=(PDEVMODE)
 * @param pDevModeInput cast=(PDEVMODE)
 */
public static final native int DocumentPropertiesA (long /*int*/ hWnd, long /*int*/ hPrinter, byte[] pDeviceName, long /*int*/ pDevModeOutput, long /*int*/ pDevModeInput, int fMode);
/** @param hdc cast=(HDC) */
public static final native boolean DPtoLP (long /*int*/ hdc, POINT lpPoints, int nCount);
/**
 * @param hwnd cast=(HWND)
 * @param pt flags=struct
 */
public static final native boolean DragDetect (long /*int*/ hwnd, POINT pt);
/** @param hDrop cast=(HDROP) */
public static final native void DragFinish (long /*int*/ hDrop);
/**
 * @param hDrop cast=(HDROP)
 * @param lpszFile cast=(LPTSTR)
 */
public static final native int DragQueryFileA (long /*int*/ hDrop, int iFile, byte[] lpszFile, int cch);
/**
 * @param hDrop cast=(HDROP)
 * @param lpszFile cast=(LPWSTR)
 */
public static final native int DragQueryFileW (long /*int*/ hDrop, int iFile, char[] lpszFile, int cch);
/** @param hwnd cast=(HWND) */
public static final native boolean DrawAnimatedRects (long /*int*/ hwnd, int idAni, RECT lprcFrom, RECT lprcTo);
/** @param hdc cast=(HDC) */
public static final native boolean DrawEdge (long /*int*/ hdc, RECT qrc, int edge, int grfFlags);
/** @param hDC cast=(HDC) */
public static final native boolean DrawFocusRect (long /*int*/ hDC, RECT lpRect);
/** @param hdc cast=(HDC) */
public static final native boolean DrawFrameControl (long /*int*/ hdc, RECT lprc, int uType, int uState);
/**
 * @param hdc cast=(HDC)
 * @param hIcon cast=(HICON)
 * @param hbrFlickerFreeDraw cast=(HBRUSH)
 */
public static final native boolean DrawIconEx (long /*int*/ hdc, int xLeft, int yTop, long /*int*/ hIcon, int cxWidth, int cyWidth, int istepIfAniCur, long /*int*/ hbrFlickerFreeDraw, int diFlags);
/** @param hWnd cast=(HWND) */
public static final native boolean DrawMenuBar (long /*int*/ hWnd);
/**
 * @param hdc cast=(HDC)
 * @param hbr cast=(HBRUSH)
 * @param lpOutputFunc cast=(DRAWSTATEPROC)
 * @param lData cast=(LPARAM)
 * @param wData cast=(WPARAM)
 */
public static final native boolean DrawStateW (long /*int*/ hdc, long /*int*/ hbr, long /*int*/ lpOutputFunc, long /*int*/ lData, long /*int*/ wData, int x, int y, int cx, int cy, int fuFlags);
/**
 * @param hdc cast=(HDC)
 * @param hbr cast=(HBRUSH)
 * @param lpOutputFunc cast=(DRAWSTATEPROC)
 * @param lData cast=(LPARAM)
 * @param wData cast=(WPARAM)
 */
public static final native boolean DrawStateA (long /*int*/ hdc, long /*int*/ hbr, long /*int*/ lpOutputFunc, long /*int*/ lData, long /*int*/ wData, int x, int y, int cx, int cy, int fuFlags);
/**
 * @param hDC cast=(HDC)
 * @param lpString cast=(LPWSTR),flags=no_out critical
 */
public static final native int DrawTextW (long /*int*/ hDC, char [] lpString, int nCount, RECT lpRect, int uFormat);
/**
 * @param hDC cast=(HDC)
 * @param lpString cast=(LPSTR),flags=no_out critical
 */
public static final native int DrawTextA (long /*int*/ hDC, byte [] lpString, int nCount, RECT lpRect, int uFormat);
/**
 * @method flags=dynamic
 * @param hTheme cast=(HTHEME)
 * @param hdc cast=(HDC)
 * @param pRect cast=(const RECT *)
 * @param pClipRect cast=(const RECT *)
 */
public static final native int DrawThemeBackground (long /*int*/ hTheme, long /*int*/ hdc, int iPartId, int iStateId, RECT pRect, RECT pClipRect);
/** @method flags=dynamic */
public static final native int DrawThemeEdge (long /*int*/ hTheme, long /*int*/ hdc, int iPartId, int iStateId, RECT pDestRect, int uEdge, int uFlags, RECT pContentRect);
/** @method flags=dynamic */
public static final native int DrawThemeIcon (long /*int*/ hTheme, long /*int*/ hdc, int iPartId, int iStateId, RECT pRect, long /*int*/ himl, int iImageIndex);
/** @method flags=dynamic */
public static final native int DrawThemeParentBackground (long /*int*/ hwnd, long /*int*/ hdc, RECT prc);
/** @method flags=dynamic */
public static final native int DrawThemeText (long /*int*/ hTheme, long /*int*/ hdc, int iPartId, int iStateId, char[] pszText, int iCharCount, int dwTextFlags, int dwTextFlags2, RECT pRect);
/** @method flags=dynamic */
public static final native int DrawThemeTextEx (long /*int*/ hTheme, long /*int*/ hdc, int iPartId, int iStateId, char[] pszText, int iCharCount, int dwFlags, RECT pRect, DTTOPTS pOptions);
/**
 * @method flags=dynamic
 * @param hWnd cast=(HWND)
 */
public static final native int DwmEnableBlurBehindWindow (long /*int*/ hWnd, DWM_BLURBEHIND pBlurBehind);
/**
 * @method flags=dynamic
 * @param hWnd cast=(HWND)
 */
public static final native int DwmExtendFrameIntoClientArea (long /*int*/ hWnd, MARGINS pMarInset);
/** @method flags=dynamic */
public static final native int DwmIsCompositionEnabled (boolean[] pfEnabled); 
/** @param hdc cast=(HDC) */
public static final native boolean Ellipse (long /*int*/ hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect);
/** @param hMenu cast=(HMENU) */
public static final native boolean EnableMenuItem (long /*int*/ hMenu, int uIDEnableItem, int uEnable);
/** @param hWnd cast=(HWND) */
public static final native boolean EnableScrollBar (long /*int*/ hWnd, int wSBflags, int wArrows);
/** @param hWnd cast=(HWND) */
public static final native boolean EnableWindow (long /*int*/ hWnd, boolean bEnable);
/**
 * @method flags=dynamic
 * @param pLangGroupEnumProc cast=(LANGUAGEGROUP_ENUMPROCW)
 * @param lParam cast=(LONG_PTR)
 */
public static final native boolean EnumSystemLanguageGroupsW(long /*int*/ pLangGroupEnumProc, int dwFlags, long /*int*/ lParam);
/**
 * @method flags=dynamic
 * @param pLangGroupEnumProc cast=(LANGUAGEGROUP_ENUMPROCA)
 * @param lParam cast=(LONG_PTR)
 */
public static final native boolean EnumSystemLanguageGroupsA(long /*int*/ pLangGroupEnumProc, int dwFlags, long /*int*/ lParam);
/** @param lpLocaleEnumProc cast=(LOCALE_ENUMPROCW) */
public static final native boolean EnumSystemLocalesW (long /*int*/ lpLocaleEnumProc, int dwFlags);
/** @param lpLocaleEnumProc cast=(LOCALE_ENUMPROCA) */
public static final native boolean EnumSystemLocalesA (long /*int*/ lpLocaleEnumProc, int dwFlags);
/** @param hWinPosInfo cast=(HDWP) */
public static final native boolean EndDeferWindowPos (long /*int*/ hWinPosInfo);
/**
 * @method flags=dynamic
 * @param hBufferedPaint cast=(HPAINTBUFFER)
 */
public static final native int EndBufferedPaint (long /*int*/ hBufferedPaint, boolean fUpdateTarget);
/** @param hdc cast=(HDC) */
public static final native int EndDoc (long /*int*/ hdc);
/** @param hdc cast=(HDC) */
public static final native int EndPage (long /*int*/ hdc);
/** @param hWnd cast=(HWND) */
public static final native int EndPaint (long /*int*/ hWnd, PAINTSTRUCT lpPaint);
/** @param hdc cast=(HDC) */
public static final native boolean EndPath(long /*int*/ hdc);
/**
 * @method flags=dynamic
 * @param hdc cast=(HDC)
 * @param lprcClip cast=(LPCRECT)
 * @param lpfnEnum cast=(MONITORENUMPROC)
 * @param dwData cast=(LPARAM)
 */
public static final native boolean EnumDisplayMonitors (long /*int*/ hdc, RECT lprcClip, long /*int*/ lpfnEnum, int dwData);
/**
 * @param hdc cast=(HDC)
 * @param hemf cast=(HENHMETAFILE)
 * @param lpEnhMetaFunc cast=(ENHMFENUMPROC)
 * @param lpData cast=(LPVOID)
 */
public static final native boolean EnumEnhMetaFile(long /*int*/ hdc, long /*int*/ hemf, long /*int*/ lpEnhMetaFunc, long /*int*/ lpData, RECT lpRect);
/**
 * @param hdc cast=(HDC)
 * @param lpszFamily cast=(LPCWSTR)
 * @param lpEnumFontFamProc cast=(FONTENUMPROCW)
 * @param lParam cast=(LPARAM)
 */
public static final native int EnumFontFamiliesW (long /*int*/ hdc, char [] lpszFamily, long /*int*/ lpEnumFontFamProc, long /*int*/ lParam);
/**
 * @param hdc cast=(HDC)
 * @param lpszFamily cast=(LPSTR)
 * @param lpEnumFontFamProc cast=(FONTENUMPROC)
 * @param lParam cast=(LPARAM)
 */
public static final native int EnumFontFamiliesA (long /*int*/ hdc, byte [] lpszFamily, long /*int*/ lpEnumFontFamProc, long /*int*/ lParam);
/**
 * @param hdc cast=(HDC)
 * @param lpLogfont cast=(LPLOGFONTW)
 * @param lpEnumFontFamExProc cast=(FONTENUMPROCW)
 * @param lParam cast=(LPARAM)
 */
public static final native int EnumFontFamiliesExW (long /*int*/ hdc, LOGFONTW lpLogfont, long /*int*/ lpEnumFontFamExProc, long /*int*/ lParam, int dwFlags);
/**
 * @param hdc cast=(HDC)
 * @param lpLogfont cast=(LPLOGFONTA)
 * @param lpEnumFontFamExProc cast=(FONTENUMPROCA)
 * @param lParam cast=(LPARAM)
 */
public static final native int EnumFontFamiliesExA (long /*int*/ hdc, LOGFONTA lpLogfont, long /*int*/ lpEnumFontFamExProc, long /*int*/ lParam, int dwFlags);
/**
 * @param lprc1 cast=(CONST RECT *),flags=no_out
 * @param lprc2 cast=(CONST RECT *),flags=no_out
 */
public static final native boolean EqualRect (RECT lprc1, RECT lprc2);
/**
 * @param hSrcRgn1 cast=(HRGN)
 * @param hSrcRgn2 cast=(HRGN)
 */
public static final native boolean EqualRgn (long /*int*/ hSrcRgn1, long /*int*/ hSrcRgn2);
/** @param hdc cast=(HDC) */
public static final native int ExcludeClipRect (long /*int*/ hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect);
public static final native int ExpandEnvironmentStringsW (char [] lpSrc, char [] lsDst, int nSize);
public static final native int ExpandEnvironmentStringsA (byte [] lpSrc, byte [] lsDst, int nSize);
/**
 * @param lplb cast=(CONST LOGBRUSH *)
 * @param lpStyle cast=(CONST DWORD *)
 */
public static final native long /*int*/ ExtCreatePen (int dwPenStyle, int dwWidth, LOGBRUSH lplb, int dwStyleCount, int[] lpStyle);
/**
 * @param lpXform cast=(XFORM *)
 * @param lpRgnData cast=(CONST RGNDATA *)
 */
public static final native long /*int*/ ExtCreateRegion (float[] lpXform, int nCount, int[] lpRgnData);
/**
 * @param hdc cast=(HDC)
 * @param lprc flags=no_out
 * @param lpString cast=(LPWSTR),flags=no_out critical
 * @param lpDx cast=(CONST INT *),flags=no_out critical
 */
public static final native boolean ExtTextOutW (long /*int*/ hdc, int X, int Y, int fuOptions, RECT lprc, char[] lpString, int cbCount, int[] lpDx);
/**
 * @param hdc cast=(HDC)
 * @param lprc flags=no_out
 * @param lpString cast=(LPSTR),flags=no_out critical
 * @param lpDx cast=(CONST INT *),flags=no_out critical
 */
public static final native boolean ExtTextOutA (long /*int*/ hdc, int X, int Y, int fuOptions, RECT lprc, byte[] lpString, int cbCount, int[] lpDx);
/**
 * @param lpszFile cast=(LPWSTR)
 * @param phiconLarge cast=(HICON FAR *)
 * @param phiconSmall cast=(HICON FAR *)
 */
public static final native int ExtractIconExW (char [] lpszFile, int nIconIndex, long /*int*/ [] phiconLarge, long /*int*/ [] phiconSmall, int nIcons);
/**
 * @param lpszFile cast=(LPSTR)
 * @param phiconLarge cast=(HICON FAR *)
 * @param phiconSmall cast=(HICON FAR *)
 */
public static final native int ExtractIconExA (byte [] lpszFile, int nIconIndex, long /*int*/ [] phiconLarge, long /*int*/ [] phiconSmall, int nIcons);
public static final native boolean FileTimeToSystemTime (FILETIME lpFileTime, SYSTEMTIME lpSystemTime);
/**
 * @param hDC cast=(HDC)
 * @param lprc flags=no_out
 * @param hbr cast=(HBRUSH)
 */
public static final native int FillRect (long /*int*/ hDC, RECT lprc, long /*int*/ hbr);
/** @param hdc cast=(HDC) */
public static final native boolean FillPath (long /*int*/ hdc);
/**
 * @param lpClassName cast=(LPSTR)
 * @param lpWindowName cast=(LPSTR)
 */
public static final native long /*int*/ FindWindowA (byte [] lpClassName, byte [] lpWindowName);
/**
 * @param lpClassName cast=(LPWSTR)
 * @param lpWindowName cast=(LPWSTR)
 */
public static final native long /*int*/ FindWindowW (char [] lpClassName, char [] lpWindowName);
/**
 * @param lpSource cast=(LPCVOID)
 * @param lpBuffer cast=(LPSTR)
 * @param Arguments cast=(va_list*)
 */
public static final native int FormatMessageA (int dwFlags, long /*int*/ lpSource, int dwMessageId, int dwLanguageId, long /*int*/ [] lpBuffer, int nSize, long /*int*/ Arguments);
/**
 * @param lpSource cast=(LPCVOID)
 * @param lpBuffer cast=(LPWSTR)
 * @param Arguments cast=(va_list*)
 */
public static final native int FormatMessageW (int dwFlags, long /*int*/ lpSource, int dwMessageId, int dwLanguageId, long /*int*/ [] lpBuffer, int nSize, long /*int*/ Arguments);
/** @param hLibModule cast=(HMODULE) */
public static final native boolean FreeLibrary (long /*int*/ hLibModule);
/** @param dwLimit cast=(DWORD) */
public static final native int GdiSetBatchLimit (int dwLimit);
public static final native int GET_WHEEL_DELTA_WPARAM(long /*int*/ wParam);
public static final native int GET_X_LPARAM(long /*int*/ lp);
public static final native int GET_Y_LPARAM(long /*int*/ lp);
public static final native int GetACP ();
public static final native short GetAsyncKeyState (int nVirtKey);
public static final native long /*int*/ GetActiveWindow ();
/** @param hDC cast=(HDC) */
public static final native int GetBkColor (long /*int*/ hDC);
public static final native long /*int*/ GetCapture ();
public static final native boolean GetCaretPos (POINT lpPoint);
/**
 * @param hdc cast=(HDC)
 * @param lpabc cast=(LPABC),flags=no_in critical
 */
public static final native boolean GetCharABCWidthsA (long /*int*/ hdc, int iFirstChar, int iLastChar, int [] lpabc);
/**
 * @param hdc cast=(HDC)
 * @param lpabc cast=(LPABC),flags=no_in critical
 */
public static final native boolean GetCharABCWidthsW (long /*int*/ hdc, int iFirstChar, int iLastChar, int [] lpabc);
/**
 * @param hdc cast=(HDC)
 * @param lpString cast=(LPWSTR),flags=no_out critical
 * @param lpResults cast=(LPGCP_RESULTSW)
 */
public static final native int GetCharacterPlacementW (long /*int*/ hdc, char[] lpString, int nCount, int nMaxExtent, GCP_RESULTS lpResults, int dwFlags);
/**
 * @param hdc cast=(HDC)
 * @param lpString cast=(LPSTR),flags=no_out critical
 */
public static final native int GetCharacterPlacementA (long /*int*/ hdc, byte[] lpString, int nCount, int nMaxExtent, GCP_RESULTS lpResults, int dwFlags);
/**
 * @param hdc cast=(HDC)
 * @param lpBuffer cast=(LPINT),flags=no_in critical
 */
public static final native boolean GetCharWidthA (long /*int*/ hdc, int iFirstChar, int iLastChar, int [] lpBuffer);
/**
 * @param hdc cast=(HDC)
 * @param lpBuffer cast=(LPINT),flags=no_in critical
 */
public static final native boolean GetCharWidthW (long /*int*/ hdc, int iFirstChar, int iLastChar, int [] lpBuffer);
/**
 * @param hInstance cast=(HINSTANCE)
 * @param lpClassName cast=(LPWSTR)
 * @param lpWndClass cast=(LPWNDCLASSW)
 */
public static final native boolean GetClassInfoW (long /*int*/ hInstance, char [] lpClassName, WNDCLASS lpWndClass);
/**
 * @param hInstance cast=(HINSTANCE)
 * @param lpClassName cast=(LPSTR)
 */
public static final native boolean GetClassInfoA (long /*int*/ hInstance, byte [] lpClassName, WNDCLASS lpWndClass);
/** @param hWnd cast=(HWND) */
public static final native int GetClassNameW (long /*int*/ hWnd, char [] lpClassName, int nMaxCount);
/** @param hWnd cast=(HWND) */
public static final native int GetClassNameA (long /*int*/ hWnd, byte [] lpClassName, int nMaxCount);
/** @param hWnd cast=(HWND) */
public static final native boolean GetClientRect (long /*int*/ hWnd, RECT lpRect);
public static final native long /*int*/ GetClipboardData (int uFormat);
public static final native int GetClipboardFormatNameA (int format, byte[] lpszFormatName, int cchMaxCount);
/** @param lpszFormatName cast=(LPWSTR) */
public static final native int GetClipboardFormatNameW (int format, char[] lpszFormatName, int cchMaxCount);
/** @param hdc cast=(HDC) */
public static final native int GetClipBox (long /*int*/ hdc, RECT lprc);
/**
 * @param hdc cast=(HDC)
 * @param hrgn cast=(HRGN)
 */
public static final native int GetClipRgn (long /*int*/ hdc, long /*int*/ hrgn);
/**
 * @method flags=dynamic
 * @param hwndCombo cast=(HWND)
 */
public static final native boolean GetComboBoxInfo (long /*int*/ hwndCombo, COMBOBOXINFO pcbi);
/** @param hdc cast=(HDC) */
public static final native long /*int*/ GetCurrentObject (long /*int*/ hdc, int uObjectType);
public static final native int GetCurrentProcessId ();
public static final native int GetCurrentThreadId ();
/** @method flags=dynamic */
public static final native int GetCurrentProcessExplicitAppUserModelID(long /*int*/[] AppID);
public static final native long /*int*/ GetCursor ();
public static final native boolean GetCursorPos (POINT lpPoint);
/**
 * @param Locale cast=(LCID)
 * @param dwFlags cast=(DWORD)
 * @param lpFormat cast=(LPWSTR)
 * @param lpDateStr cast=(LPWSTR)
 */
public static final native int GetDateFormatW(int Locale, int dwFlags, SYSTEMTIME lpDate, char [] lpFormat, char [] lpDateStr, int cchDate);
/**
 * @param Locale cast=(LCID)
 * @param dwFlags cast=(DWORD)
 * @param lpFormat cast=(LPSTR)
 * @param lpDateStr cast=(LPSTR)
 */
public static final native int GetDateFormatA(int Locale, int dwFlags, SYSTEMTIME lpDate, byte [] lpFormat, byte [] lpDateStr, int cchDate);
/** @param hwnd cast=(HWND) */
public static final native long /*int*/ GetDC (long /*int*/ hwnd);
/**
 * @param hWnd cast=(HWND)
 * @param hrgnClip cast=(HRGN)
 */
public static final native long /*int*/ GetDCEx (long /*int*/ hWnd, long /*int*/ hrgnClip, int flags);
public static final native long /*int*/ GetDesktopWindow ();
/** @param hdc cast=(HDC) */
public static final native int GetDeviceCaps (long /*int*/ hdc, int nIndex);
public static final native int GetDialogBaseUnits ();
/**
 * @param hdc cast=(HDC)
 * @param pColors cast=(RGBQUAD *),flags=no_in critical
 */
public static final native int GetDIBColorTable (long /*int*/ hdc, int uStartIndex, int cEntries, byte[] pColors);
/**
 * @param hdc cast=(HDC)
 * @param hbmp cast=(HBITMAP)
 * @param lpvBits cast=(LPVOID),flags=critical
 * @param lpbi cast=(LPBITMAPINFO),flags=critical
 */
public static final native int GetDIBits (long /*int*/ hdc, long /*int*/ hbmp, int uStartScan, int cScanLines, byte[] lpvBits, byte[] lpbi, int uUsage);
/** @param hDlg cast=(HWND) */
public static final native long /*int*/ GetDlgItem (long /*int*/ hDlg, int nIDDlgItem);
public static final native int GetDoubleClickTime ();
public static final native long /*int*/ GetFocus ();
/** @param hdc cast=(HDC) */
public static final native int GetFontLanguageInfo (long /*int*/ hdc);
public static final native long /*int*/ GetForegroundWindow ();
/** 
 * @method flags=dynamic
 * @param hGestureInfo cast=(HGESTUREINFO)
 * @param pGestureInfo cast=(PGESTUREINFO)
 */
public static final native boolean GetGestureInfo(long /*int*/ hGestureInfo, GESTUREINFO pGestureInfo);
/** @param hdc cast=(HDC) */
public static final native int GetGraphicsMode (long /*int*/ hdc);
/**
 * @param hdc cast=(HDC)
 * @param pgi cast=(LPWORD)
 */
public static final native int GetGlyphIndicesW(long /*int*/ hdc, char[] lpstr, int c, short[] pgi, int fl);
/**
 * @param idThread cast=(DWORD)
 * @param lpgui cast=(LPGUITHREADINFO)
 */
public static final native boolean GetGUIThreadInfo (int idThread, GUITHREADINFO lpgui);
/**
 * @param hIcon cast=(HICON)
 * @param piconinfo flags=no_in
 */
public static final native boolean GetIconInfo (long /*int*/ hIcon, ICONINFO piconinfo);
/** @param lpList cast=(HKL FAR *) */
public static final native int GetKeyboardLayoutList (int nBuff, long /*int*/ [] lpList);
public static final native long /*int*/ GetKeyboardLayout (int idThread);
public static final native short GetKeyState (int nVirtKey);
/** @param lpKeyState cast=(PBYTE) */
public static final native boolean GetKeyboardState (byte [] lpKeyState);
/** @param lpString cast=(LPWSTR) */
public static final native int GetKeyNameTextW (int lParam, char [] lpString, int nSize);
/** @param lpString cast=(LPSTR) */
public static final native int GetKeyNameTextA (int lParam, byte [] lpString, int nSize);
/** @param hWnd cast=(HWND) */
public static final native long /*int*/ GetLastActivePopup (long /*int*/ hWnd);
public static final native int GetLastError ();
/**
 * @method flags=dynamic
 * @param hwnd cast=(HWND)
 */
public static final native boolean GetLayeredWindowAttributes (long /*int*/ hwnd, int [] pcrKey, byte [] pbAlpha, int [] pdwFlags);
/**
 * @method flags=dynamic
 * @param hdc cast=(HDC)
 */
public static final native int GetLayout (long /*int*/ hdc);
/* returns the instance handle to the swt library */
/** @method flags=no_gen */
public static final native long /*int*/ GetLibraryHandle ();
/** @param lpLCData cast=(LPWSTR) */
public static final native int GetLocaleInfoW (int Locale, int LCType, char [] lpLCData, int cchData);
/** @param lpLCData cast=(LPSTR) */
public static final native int GetLocaleInfoA (int Locale, int LCType, byte [] lpLCData, int cchData);
/** @param hWnd cast=(HWND) */
public static final native long /*int*/ GetMenu (long /*int*/ hWnd);
/** @method flags=dynamic */
public static final native boolean GetMenuBarInfo (long /*int*/ hWnd, int idObject, int idItem, MENUBARINFO pmbi);
/** @param hMenu cast=(HMENU) */
public static final native int GetMenuDefaultItem (long /*int*/ hMenu, int fByPos, int gmdiFlags);
/**
 * @method flags=dynamic
 * @param hmenu cast=(HMENU)
 */
public static final native boolean GetMenuInfo (long /*int*/ hmenu, MENUINFO lpcmi);
/** @param hMenu cast=(HMENU) */
public static final native int GetMenuItemCount (long /*int*/ hMenu);
/**
 * @param hMenu cast=(HMENU)
 * @param lpmii cast=(LPMENUITEMINFOW)
 */
public static final native boolean GetMenuItemInfoW (long /*int*/ hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii);
/** @param hMenu cast=(HMENU) */
public static final native boolean GetMenuItemInfoA (long /*int*/ hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii);
/**
 * @param hWnd cast=(HWND)
 * @param hMenu cast=(HMENU)
 */
public static final native boolean GetMenuItemRect (long /*int*/ hWnd, long /*int*/ hMenu, int uItem, RECT lprcItem);
/** @param hWnd cast=(HWND) */
public static final native boolean GetMessageW (MSG lpMsg, long /*int*/ hWnd, int wMsgFilterMin, int wMsgFilterMax);
/** @param hWnd cast=(HWND) */
public static final native boolean GetMessageA (MSG lpMsg, long /*int*/ hWnd, int wMsgFilterMin, int wMsgFilterMax);
public static final native int GetMessagePos ();
public static final native int GetMessageTime ();
/**
 * @param hdc cast=(HDC)
 * @param hrgn cast=(HRGN)
 */
public static final native int GetMetaRgn (long /*int*/ hdc, long /*int*/ hrgn);
/** @method flags=dynamic */
public static final native int GetThemeBitmap (long /*int*/ hTheme, int iPartId, int iStateId, int iPropId, int dwFlags, long /*int*/[] hBitmap);
/** @method flags=dynamic */
public static final native int GetThemeColor (long /*int*/ hTheme, int iPartId, int iStateId, int iPropId, int[] pColor);
/** @method flags=dynamic */
public static final native int GetThemeTextExtent (long /*int*/ hTheme, long /*int*/ hdc, int iPartId, int iStateId, char[] pszText, int iCharCount, int dwTextFlags, RECT pBoundingRect, RECT pExtentRect);
/** @param hdc cast=(HDC) */
public static final native int GetTextCharset (long /*int*/ hdc);
public static final native int GetTickCount ();
/** @param hdc cast=(HDC) */
public static final native int GetMapMode (long /*int*/ hdc);
/**
 * @param hModule cast=(HMODULE)
 * @param lpFilename cast=(LPWSTR)
 */
public static final native int GetModuleFileNameW (long /*int*/ hModule, char [] lpFilename, int inSize);
/**
 * @param hModule cast=(HMODULE)
 * @param lpFilename cast=(LPSTR)
 */
public static final native int GetModuleFileNameA (long /*int*/ hModule, byte [] lpFilename, int inSize);
/** @param lpModuleName cast=(LPWSTR) */
public static final native long /*int*/ GetModuleHandleW (char [] lpModuleName);
/** @param lpModuleName cast=(LPSTR) */
public static final native long /*int*/ GetModuleHandleA (byte [] lpModuleName);
/**
 * @method flags=dynamic
 * @param hmonitor cast=(HMONITOR)
 * @param lpmi cast=(LPMONITORINFO)
 */
public static final native boolean GetMonitorInfoW (long /*int*/ hmonitor, MONITORINFO lpmi);
/**
 * @method flags=dynamic
 * @param hmonitor cast=(HMONITOR)
 * @param lpmi cast=(LPMONITORINFO)
 */
public static final native boolean GetMonitorInfoA (long /*int*/ hmonitor, MONITORINFO lpmi);
/**
 * @param hPal cast=(HPALETTE)
 * @param crColor cast=(COLORREF)
 */
public static final native int GetNearestPaletteIndex (long /*int*/ hPal, int crColor);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObjectA (long /*int*/ hgdiobj, int cbBuffer, BITMAP lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObjectW (long /*int*/ hgdiobj, int cbBuffer, BITMAP lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObjectA (long /*int*/ hgdiobj, int cbBuffer, DIBSECTION lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObjectW (long /*int*/ hgdiobj, int cbBuffer, DIBSECTION lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObjectA (long /*int*/ hgdiobj, int cbBuffer, EXTLOGPEN lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObjectW (long /*int*/ hgdiobj, int cbBuffer, EXTLOGPEN lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObjectA (long /*int*/ hgdiobj, int cbBuffer, LOGBRUSH lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObjectW (long /*int*/ hgdiobj, int cbBuffer, LOGBRUSH lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObjectA (long /*int*/ hgdiobj, int cbBuffer, LOGFONTA lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObjectW (long /*int*/ hgdiobj, int cbBuffer, LOGFONTW lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObjectA (long /*int*/ hgdiobj, int cbBuffer, LOGPEN lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObjectW (long /*int*/ hgdiobj, int cbBuffer, LOGPEN lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject cast=(LPVOID),flags=no_in
 */
public static final native int GetObjectA (long /*int*/ hgdiobj, int cbBuffer, long /*int*/ lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject cast=(LPVOID),flags=no_in
 */
public static final native int GetObjectW (long /*int*/ hgdiobj, int cbBuffer, long /*int*/ lpvObject);
/** @param lpofn cast=(LPOPENFILENAMEW) */
public static final native boolean GetOpenFileNameW (OPENFILENAME lpofn);
public static final native boolean GetOpenFileNameA (OPENFILENAME lpofn);
/** @param hdc cast=(HDC) */
public static final native int GetOutlineTextMetricsW (long /*int*/ hdc, int cbData, OUTLINETEXTMETRICW lpOTM);
/** @param hdc cast=(HDC) */
public static final native int GetOutlineTextMetricsA (long /*int*/ hdc, int cbData, OUTLINETEXTMETRICA lpOTM);
/**
 * @param hdc cast=(HDC)
 * @param lpPoints cast=(LPPOINT)
 * @param lpTypes cast=(LPBYTE)
 */
public static final native int GetPath (long /*int*/ hdc, int[] lpPoints, byte[] lpTypes, int nSize);
/**
 * @param hPalette cast=(HPALETTE)
 * @param logPalette cast=(LPPALETTEENTRY),flags=no_in critical
 */
public static final native int GetPaletteEntries (long /*int*/ hPalette, int iStartIndex, int nEntries, byte[] logPalette);
/** @param hWnd cast=(HWND) */
public static final native long /*int*/ GetParent (long /*int*/ hWnd);
/** @param hdc cast=(HDC) */
public static final native int GetPixel (long /*int*/ hdc, int x, int y);
/** @param hdc cast=(HDC) */
public static final native int GetPolyFillMode (long /*int*/ hdc);
/**
 * @param pPrinterName cast=(LPWSTR)
 * @param phPrinter cast=(LPHANDLE)
 * @param pDefault cast=(LPPRINTER_DEFAULTSW)
 */
public static final native boolean OpenPrinterW (char[] pPrinterName, long /*int*/ [] phPrinter, long /*int*/ pDefault);
/**
 * @param pPrinterName cast=(LPTSTR)
 * @param phPrinter cast=(LPHANDLE)
 * @param pDefault cast=(LPPRINTER_DEFAULTS)
 */
public static final native boolean OpenPrinterA (byte[] pPrinterName, long /*int*/ [] phPrinter, long /*int*/ pDefault);
/**
 * @param hModule cast=(HMODULE)
 * @param lpProcName cast=(LPCTSTR)
 */
public static final native long /*int*/ GetProcAddress (long /*int*/ hModule, byte [] lpProcName);
public static final native long /*int*/ GetProcessHeap ();
/** @param ProcessHeaps cast=(PHANDLE) */
public static final native int GetProcessHeaps (int NumberOfHeaps, long /*int*/[] ProcessHeaps);
/**
 * @param lpAppName cast=(LPWSTR)
 * @param lpKeyName cast=(LPWSTR)
 * @param lpDefault cast=(LPWSTR)
 * @param lpReturnedString cast=(LPWSTR)
 */
public static final native int GetProfileStringW (char [] lpAppName, char [] lpKeyName, char [] lpDefault, char [] lpReturnedString, int nSize);
/**
 * @param lpAppName cast=(LPSTR)
 * @param lpKeyName cast=(LPSTR)
 * @param lpDefault cast=(LPSTR)
 * @param lpReturnedString cast=(LPSTR)
 */
public static final native int GetProfileStringA (byte [] lpAppName, byte [] lpKeyName, byte [] lpDefault, byte [] lpReturnedString, int nSize);
/**
 * @param hWnd cast=(HWND)
 * @param lpString cast=(LPCWSTR)
 */
public static final native long /*int*/ GetPropW (long /*int*/ hWnd, long /*int*/ lpString);
/**
 * @param hWnd cast=(HWND)
 * @param lpString cast=(LPCTSTR)
 */
public static final native long /*int*/ GetPropA (long /*int*/ hWnd, long /*int*/ lpString);
/**
 * @param hdc cast=(HDC)
 * @param hrgn cast=(HRGN)
 */
public static final native int GetRandomRgn (long /*int*/ hdc, long /*int*/ hrgn, int iNum);
/**
 * @param hRgn cast=(HRGN)
 * @param lpRgnData cast=(RGNDATA *),flags=no_in critical
 */
public static final native int GetRegionData (long /*int*/ hRgn, int dwCount, int [] lpRgnData);
/**
 * @param hrgn cast=(HRGN)
 * @param lprc flags=no_in
 */
public static final native int GetRgnBox (long /*int*/ hrgn, RECT lprc);
/** @param hdc cast=(HDC) */
public static final native int GetROP2 (long /*int*/ hdc);
/** @param lpofn cast=(LPOPENFILENAMEW) */
public static final native boolean GetSaveFileNameW (OPENFILENAME lpofn);
public static final native boolean GetSaveFileNameA (OPENFILENAME lpofn);
/** @param hwnd cast=(HWND) */
public static final native boolean GetScrollBarInfo (long /*int*/ hwnd, int idObject, SCROLLBARINFO psbi);
/** @param hwnd cast=(HWND) */
public static final native boolean GetScrollInfo (long /*int*/ hwnd, int flags, SCROLLINFO info);
/** @param lpStartupInfo cast=(LPSTARTUPINFOW) */
public static final native void GetStartupInfoW (STARTUPINFO lpStartupInfo);
/** @param lpStartupInfo cast=(LPSTARTUPINFOA) */
public static final native void GetStartupInfoA (STARTUPINFO lpStartupInfo);
public static final native long /*int*/ GetStockObject (int fnObject);
public static final native int GetSysColor (int nIndex);
public static final native long /*int*/ GetSysColorBrush (int nIndex);
/** @method flags=dynamic */
public static final native short GetSystemDefaultUILanguage ();
/** @param hWnd cast=(HWND) */
public static final native long /*int*/ GetSystemMenu (long /*int*/ hWnd, boolean bRevert);
public static final native int GetSystemMetrics (int nIndex);
/**
 * @param hdc cast=(HDC)
 * @param iStartIndex cast=(UINT)
 * @param nEntries cast=(UINT)
 * @param lppe cast=(LPPALETTEENTRY),flags=no_in critical
 */
public static final native int GetSystemPaletteEntries (long /*int*/ hdc, int iStartIndex, int nEntries, byte[] lppe);
/** @param hDC cast=(HDC) */
public static final native int GetTextColor (long /*int*/ hDC);
/**
 * @param hdc cast=(HDC)
 * @param lpString cast=(LPWSTR),flags=no_out critical
 * @param lpSize flags=no_in
 */
public static final native boolean GetTextExtentPoint32W (long /*int*/ hdc, char [] lpString, int cbString, SIZE lpSize);
/**
 * @param hdc cast=(HDC)
 * @param lpString cast=(LPSTR),flags=no_out critical
 * @param lpSize flags=no_in
 */
public static final native boolean GetTextExtentPoint32A (long /*int*/ hdc, byte [] lpString, int cbString, SIZE lpSize);
/**
 * @param hdc cast=(HDC)
 * @param lptm flags=no_in
 */
public static final native boolean GetTextMetricsW (long /*int*/ hdc, TEXTMETRICW lptm);
/**
 * @param hdc cast=(HDC)
 * @param lptm flags=no_in
 */
public static final native boolean GetTextMetricsA (long /*int*/ hdc, TEXTMETRICA lptm);
/** @method flags=dynamic */
public static final native int GetThemeInt (long /*int*/ hTheme, int iPartId, int iStateId, int iPropId, int[] piVal);
/** @method flags=dynamic */
public static final native int GetThemeMargins (long /*int*/ hTheme, long /*int*/ hdc, int iPartId, int iStateId, int iPropId, RECT prc, MARGINS pMargins);
/** @method flags=dynamic */
public static final native int GetThemeBackgroundContentRect (long /*int*/ hTheme, long /*int*/ hdc, int iPartId, int iStateId, RECT pBoundingRect, RECT pContentRect);
/**
 * @method flags=dynamic
 * @param pContentRect flags=no_out
 */
public static final native int GetThemeBackgroundExtent (long /*int*/ hTheme, long /*int*/ hdc, int iPartId, int iStateId, RECT pContentRect, RECT pExtentRect);
/** @method flags=dynamic */
public static final native int GetThemePartSize (long /*int*/ hTheme, long /*int*/ hdc, int iPartId, int iStateId, RECT prc, int eSize, SIZE psz);
/** @method flags=dynamic */
public static final native int GetThemeMetric (long /*int*/ hTheme, long /*int*/ hdc, int iPartId, int iStateId, int iPropId, int[] piVal);
/** @method flags=dynamic */
public static final native int GetThemeRect (long /*int*/ hTheme, int iPartId, int iStateId, int iPropId, RECT pRect);
/** @method flags=dynamic */
public static final native int GetThemeSysSize (long /*int*/ hTheme, int iSizeID);
/**
 * @param Locale cast=(LCID)
 * @param dwFlags cast=(DWORD)
 * @param lpFormat cast=(LPWSTR)
 * @param lpTimeStr cast=(LPWSTR)
 */
public static final native int GetTimeFormatW(int Locale, int dwFlags, SYSTEMTIME lpTime, char [] lpFormat, char [] lpTimeStr, int cchTime);
/**
 * @param Locale cast=(LCID)
 * @param dwFlags cast=(DWORD)
 * @param lpFormat cast=(LPSTR)
 * @param lpTimeStr cast=(LPSTR)
 */
public static final native int GetTimeFormatA(int Locale, int dwFlags, SYSTEMTIME lpTime, byte [] lpFormat, byte [] lpTimeStr, int cchTime);
/**
 * @method flags=dynamic
 * @param hTouchInput cast=(HTOUCHINPUT)
 * @param cInputs cast=(UINT)
 * @param pTouchInputs cast=(PTOUCHINPUT)
 */
public static final native boolean GetTouchInputInfo(long /*int*/ hTouchInput, int cInputs, long /*int*/ pTouchInputs, int cbSize);
/**
 * @param hWnd cast=(HWND)
 * @param lpRect cast=(LPRECT)
 * @param bErase cast=(BOOL)
 */
public static final native boolean GetUpdateRect (long /*int*/ hWnd, RECT lpRect, boolean bErase);
/**
 * @param hWnd cast=(HWND)
 * @param hRgn cast=(HRGN)
 */
public static final native int GetUpdateRgn (long /*int*/ hWnd, long /*int*/ hRgn, boolean bErase);
/** @param lpVersionInfo cast=(LPOSVERSIONINFOW) */
public static final native boolean GetVersionExW (OSVERSIONINFOEXW lpVersionInfo);
/** @param lpVersionInfo cast=(LPOSVERSIONINFOA) */
public static final native boolean GetVersionExA (OSVERSIONINFOEXA lpVersionInfo);
public static final native boolean GetVersionExW (OSVERSIONINFOW lpVersionInfo);
public static final native boolean GetVersionExA (OSVERSIONINFOA lpVersionInfo);
/** @param hWnd cast=(HWND) */
public static final native long /*int*/ GetWindow (long /*int*/ hWnd, int uCmd);
/** @param hWnd cast=(HWND) */
public static final native int GetWindowLongW (long /*int*/ hWnd, int nIndex);
/** @param hWnd cast=(HWND) */
public static final native int GetWindowLongA (long /*int*/ hWnd, int nIndex);
/** @param hWnd cast=(HWND) */
public static final native long /*int*/ GetWindowLongPtrW (long /*int*/ hWnd, int nIndex);
/** @param hWnd cast=(HWND) */
public static final native long /*int*/ GetWindowLongPtrA (long /*int*/ hWnd, int nIndex);
/** @param hWnd cast=(HWND) */
public static final native long /*int*/ GetWindowDC (long /*int*/ hWnd);
/** @param hdc cast=(HDC) */
public static final native boolean GetWindowOrgEx (long /*int*/ hdc, POINT lpPoint);
/** @param hWnd cast=(HWND) */
public static final native boolean GetWindowPlacement (long /*int*/ hWnd, WINDOWPLACEMENT lpwndpl);
/** @param hWnd cast=(HWND) */
public static final native boolean GetWindowRect (long /*int*/ hWnd, RECT lpRect);
/**
 * @param hWnd cast=(HWND)
 * @param hRgn cast=(HRGN)
 */
public static final native int GetWindowRgn (long /*int*/ hWnd, long /*int*/ hRgn);
/**
 * @param hWnd cast=(HWND)
 * @param lpString cast=(LPWSTR)
 */
public static final native int GetWindowTextW (long /*int*/ hWnd, char [] lpString, int nMaxCount);
/**
 * @param hWnd cast=(HWND)
 * @param lpString cast=(LPSTR)
 */
public static final native int GetWindowTextA (long /*int*/ hWnd, byte [] lpString, int nMaxCount);
/** @param hWnd cast=(HWND) */
public static final native int GetWindowTextLengthW (long /*int*/ hWnd);
/** @param hWnd cast=(HWND) */
public static final native int GetWindowTextLengthA (long /*int*/ hWnd);
/**
 * @method flags=dynamic
 * @param hWnd cast=(HWND)
 */
public static final native long /*int*/ GetWindowTheme (long /*int*/ hWnd);
/**
 * @param hWnd cast=(HWND)
 * @param lpdwProcessId cast=(LPDWORD)
 */
public static final native int GetWindowThreadProcessId (long /*int*/ hWnd, int [] lpdwProcessId);
/**
 * @param hdc cast=(HDC)
 * @param lpXform cast=(LPXFORM)
 */
public static final native boolean GetWorldTransform (long /*int*/ hdc, float[] lpXform);
public static final native double GID_ROTATE_ANGLE_FROM_ARGUMENT(long dwArgument); 
/** @param lpString cast=(LPCWSTR) */
public static final native int GlobalAddAtomW (char [] lpString);
/** @param lpString cast=(LPCTSTR) */
public static final native int GlobalAddAtomA (byte [] lpString);
public static final native long /*int*/ GlobalAlloc (int uFlags, int dwBytes);
/** @param hMem cast=(HANDLE) */
public static final native long /*int*/ GlobalFree (long /*int*/ hMem);
/** @param hMem cast=(HANDLE) */
public static final native long /*int*/ GlobalLock (long /*int*/ hMem);
/** @param hMem cast=(HANDLE) */
public static final native int GlobalSize (long /*int*/ hMem);
/** @param hMem cast=(HANDLE) */
public static final native boolean GlobalUnlock (long /*int*/ hMem);
/**
 * @method flags=dynamic
 * @param hdc cast=(HDC)
 * @param pVertex cast=(PTRIVERTEX)
 * @param dwNumVertex cast=(ULONG)
 * @param pMesh cast=(PVOID)
 * @param dwNumMesh cast=(ULONG)
 * @param dwMode cast=(ULONG)
 */
public static final native boolean GradientFill (long /*int*/ hdc, long /*int*/ pVertex, int dwNumVertex, long /*int*/ pMesh, int dwNumMesh, int dwMode);
public static final native int HIWORD(long /*int*/ l);
/** @param hHeap cast=(HANDLE) */
public static final native long /*int*/ HeapAlloc (long /*int*/ hHeap, int dwFlags, int dwBytes);
/**
 * @param hHeap cast=(HANDLE)
 * @param lpMem cast=(LPVOID)
 */
public static final native boolean HeapFree (long /*int*/ hHeap, int dwFlags, long /*int*/ lpMem);
/**
 * @param hHeap cast=(HANDLE)
 * @param lpMem cast=(LPCVOID)
 */
public static final native boolean HeapValidate (long /*int*/ hHeap, int dwFlags, long /*int*/ lpMem);
/** @param hWnd cast=(HWND) */
public static final native boolean HideCaret (long /*int*/ hWnd);
/**
 * @method flags=dynamic
 * @param ptTest flags=struct
 */
public static final native int HitTestThemeBackground (long /*int*/ hTheme, long /*int*/ hdc, int iPartId, int iStateId, int dwOptions, RECT pRect, long /*int*/ hrgn, POINT ptTest, short[] pwHitTestCode);
/**
 * @param lpsz cast=(LPOLESTR)
 * @param lpiid cast=(LPIID)
 */
public static final native int IIDFromString (char[] lpsz, byte[] lpiid);
/**
 * @param himl cast=(HIMAGELIST)
 * @param hbmImage cast=(HBITMAP)
 * @param hbmMask cast=(HBITMAP)
 */
public static final native int ImageList_Add (long /*int*/ himl, long /*int*/ hbmImage, long /*int*/ hbmMask);
/**
 * @param himl cast=(HIMAGELIST)
 * @param hbmImage cast=(HBITMAP)
 * @param crMask cast=(COLORREF)
 */
public static final native int ImageList_AddMasked (long /*int*/ himl, long /*int*/ hbmImage, int crMask);
/** @param himl cast=(HIMAGELIST) */
public static final native boolean ImageList_BeginDrag (long /*int*/ himl, int iTrack, int dxHotspot, int dyHotspot);
public static final native long /*int*/ ImageList_Create (int cx, int cy, int flags, int cInitial, int cGrow);
/** @param himl cast=(HIMAGELIST) */
public static final native boolean ImageList_Destroy (long /*int*/ himl);
/** @param hwndLock cast=(HWND) */
public static final native boolean ImageList_DragEnter (long /*int*/ hwndLock, int x, int y);
/** @param hwndLock cast=(HWND) */
public static final native boolean ImageList_DragLeave (long /*int*/ hwndLock);
public static final native boolean ImageList_DragMove (int x, int y);
/** @param fShow cast=(BOOL) */
public static final native boolean ImageList_DragShowNolock (boolean fShow);
/**
 * @param himl cast=(HIMAGELIST)
 * @param hdcDst cast=(HDC)
 * @param fStyle cast=(UINT)
 */
public static final native boolean ImageList_Draw (long /*int*/ himl, int i, long /*int*/ hdcDst, int x, int y, int fStyle);
public static final native void ImageList_EndDrag ();
/**
 * @param ppt cast=(POINT *)
 * @param pptHotspot cast=(POINT *)
 */
public static final native long /*int*/ ImageList_GetDragImage (POINT ppt, POINT pptHotspot);
/** @param himl cast=(HIMAGELIST) */
public static final native long /*int*/ ImageList_GetIcon (long /*int*/ himl, int i, int flags);
/**
 * @param himl cast=(HIMAGELIST)
 * @param cx cast=(int *)
 * @param cy cast=(int *)
 */
public static final native boolean ImageList_GetIconSize (long /*int*/ himl, int [] cx, int [] cy);   
/** @param himl cast=(HIMAGELIST) */
public static final native int ImageList_GetImageCount (long /*int*/ himl);
/** @param himl cast=(HIMAGELIST) */
public static final native boolean ImageList_Remove (long /*int*/ himl, int i);
/**
 * @param himl cast=(HIMAGELIST)
 * @param hbmImage cast=(HBITMAP)
 * @param hbmMask cast=(HBITMAP)
 */
public static final native boolean ImageList_Replace (long /*int*/ himl, int i, long /*int*/ hbmImage, long /*int*/ hbmMask);
/**
 * @param himl cast=(HIMAGELIST)
 * @param hicon cast=(HICON)
 */
public static final native int ImageList_ReplaceIcon (long /*int*/ himl, int i, long /*int*/ hicon);
/** @param himl cast=(HIMAGELIST) */
public static final native boolean ImageList_SetIconSize (long /*int*/ himl, int cx, int cy);
/**
 * @param hWnd cast=(HWND)
 * @param hIMC cast=(HIMC)
 */
public static final native long /*int*/ ImmAssociateContext (long /*int*/ hWnd, long /*int*/ hIMC);
public static final native long /*int*/ ImmCreateContext ();
/** @param hIMC cast=(HIMC) */
public static final native boolean ImmDestroyContext (long /*int*/ hIMC);
/** @method flags=dynamic */
public static final native boolean ImmDisableTextFrameService (int idThread);
/** @param hIMC cast=(HIMC) */
public static final native boolean ImmGetCompositionFontW (long /*int*/ hIMC, LOGFONTW lplf);
/** @param hIMC cast=(HIMC) */
public static final native boolean ImmGetCompositionFontA (long /*int*/ hIMC, LOGFONTA lplf);
/**
 * @param hIMC cast=(HIMC)
 * @param lpBuf cast=(LPWSTR)
 */
public static final native int ImmGetCompositionStringW (long /*int*/ hIMC, int dwIndex, char [] lpBuf, int dwBufLen);
/**
 * @param hIMC cast=(HIMC)
 * @param lpBuf cast=(LPSTR)
 */
public static final native int ImmGetCompositionStringA (long /*int*/ hIMC, int dwIndex, byte [] lpBuf, int dwBufLen);
/**
 * @param hIMC cast=(HIMC)
 * @param lpBuf cast=(LPWSTR)
 */
public static final native int ImmGetCompositionStringW (long /*int*/ hIMC, int dwIndex, int [] lpBuf, int dwBufLen);
/**
 * @param hIMC cast=(HIMC)
 * @param lpBuf cast=(LPWSTR)
 */
public static final native int ImmGetCompositionStringA (long /*int*/ hIMC, int dwIndex, int [] lpBuf, int dwBufLen);
/**
 * @param hIMC cast=(HIMC)
 * @param lpBuf cast=(LPWSTR)
 */
public static final native int ImmGetCompositionStringW (long /*int*/ hIMC, int dwIndex, byte [] lpBuf, int dwBufLen); 
/** @param hWnd cast=(HWND) */
public static final native long /*int*/ ImmGetContext (long /*int*/ hWnd);
/**
 * @param hIMC cast=(HIMC)
 * @param lpfdwConversion cast=(LPDWORD)
 * @param lpfdwSentence cast=(LPDWORD)
 */
public static final native boolean ImmGetConversionStatus (long /*int*/ hIMC, int [] lpfdwConversion, int [] lpfdwSentence);
/** @param hWnd cast=(HWND) */
public static final native long /*int*/ ImmGetDefaultIMEWnd (long /*int*/ hWnd);
/** @param hIMC cast=(HIMC) */
public static final native boolean ImmGetOpenStatus (long /*int*/ hIMC);
/** @param hIMC cast=(HIMC) */
public static final native boolean ImmNotifyIME (long /*int*/ hIMC, int dwAction, int dwIndex, int dwValue);
/**
 * @param hWnd cast=(HWND)
 * @param hIMC cast=(HIMC)
 */
public static final native boolean ImmReleaseContext (long /*int*/ hWnd, long /*int*/ hIMC);
/** @param hIMC cast=(HIMC) */
public static final native boolean ImmSetCompositionFontW (long /*int*/ hIMC, LOGFONTW lplf);
/** @param hIMC cast=(HIMC) */
public static final native boolean ImmSetCompositionFontA (long /*int*/ hIMC, LOGFONTA lplf);
/** @param hIMC cast=(HIMC) */
public static final native boolean ImmSetCompositionWindow (long /*int*/ hIMC, COMPOSITIONFORM lpCompForm);
/** @param hIMC cast=(HIMC) */
public static final native boolean ImmSetCandidateWindow (long /*int*/ hIMC, CANDIDATEFORM lpCandidate);
/** @param hIMC cast=(HIMC) */
public static final native boolean ImmSetConversionStatus (long /*int*/ hIMC, int fdwConversion, int dwSentence);
/** @param hIMC cast=(HIMC) */
public static final native boolean ImmSetOpenStatus (long /*int*/ hIMC, boolean fOpen);
public static final native void InitCommonControls ();
public static final native boolean InitCommonControlsEx (INITCOMMONCONTROLSEX lpInitCtrls);
public static final native boolean InSendMessage ();
/** @param hMenu cast=(HMENU) */
public static final native boolean InsertMenuW (long /*int*/ hMenu, int uPosition, int uFlags, long /*int*/ uIDNewItem, char [] lpNewItem);
/** @param hMenu cast=(HMENU) */
public static final native boolean InsertMenuA (long /*int*/ hMenu, int uPosition, int uFlags, long /*int*/ uIDNewItem, byte [] lpNewItem);
/**
 * @param hMenu cast=(HMENU)
 * @param lpmii cast=(LPMENUITEMINFOW)
 */
public static final native boolean InsertMenuItemW (long /*int*/ hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii);
/** @param hMenu cast=(HMENU) */
public static final native boolean InsertMenuItemA (long /*int*/ hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii);
/**
 * @param lpszUrl cast=(LPCTSTR)
 * @param lpszCookieName cast=(LPCTSTR)
 * @param lpszCookieData cast=(LPSTR)
 * @param lpdwSize cast=(LPDWORD)
 */
public static final native boolean InternetGetCookieA (byte[] lpszUrl, byte[] lpszCookieName, byte[] lpszCookieData, int[] lpdwSize);
/**
 * @param lpszUrl cast=(LPCWSTR)
 * @param lpszCookieName cast=(LPCWSTR)
 * @param lpszCookieData cast=(LPWSTR)
 * @param lpdwSize cast=(LPDWORD)
 */
public static final native boolean InternetGetCookieW (char[] lpszUrl, char[] lpszCookieName, char[] lpszCookieData, int[] lpdwSize);
/**
 * @param lpszUrl cast=(LPCTSTR)
 * @param lpszCookieName cast=(LPCTSTR)
 * @param lpszCookieData cast=(LPCTSTR)
 */
public static final native boolean InternetSetCookieA (byte[] lpszUrl, byte[] lpszCookieName, byte[] lpszCookieData);
/**
 * @param lpszUrl cast=(LPCWSTR)
 * @param lpszCookieName cast=(LPCWSTR)
 * @param lpszCookieData cast=(LPCWSTR)
 */
public static final native boolean InternetSetCookieW (char[] lpszUrl, char[] lpszCookieName, char[] lpszCookieData);
/**
 * @param hInternet cast=(HINTERNET)
 * @param lpBuffer cast=(LPVOID)
 */
public static final native boolean InternetSetOption (long /*int*/ hInternet, int dwOption, long /*int*/ lpBuffer, int dwBufferLength);
/** @param hdc cast=(HDC) */
public static final native int IntersectClipRect (long /*int*/ hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect);
/**
 * @param lprcDst flags=no_in
 * @param lprcSrc1 flags=no_out
 * @param lprcSrc2 flags=no_out
 */
public static final native boolean IntersectRect (RECT lprcDst, RECT lprcSrc1, RECT lprcSrc2);
/** @param hWnd cast=(HWND) */
public static final native boolean InvalidateRect (long /*int*/ hWnd, RECT lpRect, boolean bErase);
/**
 * @param hWnd cast=(HWND)
 * @param hRgn cast=(HRGN)
 */
public static final native boolean InvalidateRgn (long /*int*/ hWnd, long /*int*/ hRgn, boolean bErase);
/** @method flags=dynamic */
public static final native boolean IsAppThemed ();
/**
 * @param lp cast=(LPVOID)
 * @param ucb cast=(UINT_PTR)
 */
public static final native boolean IsBadReadPtr (long /*int*/ lp, int ucb);
/**
 * @param lp cast=(LPVOID)
 * @param ucb cast=(UINT_PTR)
 */
public static final native boolean IsBadWritePtr (long /*int*/ lp, int ucb);
public static final native boolean IsDBCSLeadByte (byte TestChar);
/**
 * @method flags=dynamic
 * @param hWnd cast=(HWND)
 */
public static final native boolean IsHungAppWindow (long /*int*/ hWnd);
/** @param hWnd cast=(HWND) */
public static final native boolean IsIconic (long /*int*/ hWnd);
/** @method flags=no_gen */
public static final native boolean IsPPC ();
/** @method flags=no_gen */
public static final native boolean IsSP ();
/**
 * @method flags=dynamic
 * @param hWnd cast=(HWND) 
 * @param outFlags cast=(PULONG)
 */
public static final native boolean IsTouchWindow (long /*int*/ hWnd, long[] outFlags);
/** @param hWnd cast=(HWND) */
public static final native boolean IsWindowEnabled (long /*int*/ hWnd);
/** @param hWnd cast=(HWND) */
public static final native boolean IsWindowVisible (long /*int*/ hWnd);
/** @param hWnd cast=(HWND) */
public static final native boolean IsZoomed (long /*int*/ hWnd);
/** @param hWnd cast=(HWND) */
public static final native boolean KillTimer (long /*int*/ hWnd, long /*int*/ uIDEvent);
/** @param hdc cast=(HDC) */
public static final native boolean LineTo (long /*int*/ hdc, int x1, int x2);
/**
 * @param hInstance cast=(HINSTANCE)
 * @param lpBitmapName cast=(LPWSTR)
 */
public static final native long /*int*/ LoadBitmapW (long /*int*/ hInstance, long /*int*/ lpBitmapName);
/**
 * @param hInstance cast=(HINSTANCE)
 * @param lpBitmapName cast=(LPSTR)
 */
public static final native long /*int*/ LoadBitmapA (long /*int*/ hInstance, long /*int*/ lpBitmapName);
/**
 * @param hInstance cast=(HINSTANCE)
 * @param lpCursorName cast=(LPWSTR)
 */
public static final native long /*int*/ LoadCursorW (long /*int*/ hInstance, long /*int*/ lpCursorName);
/**
 * @param hInstance cast=(HINSTANCE)
 * @param lpCursorName cast=(LPSTR)
 */
public static final native long /*int*/ LoadCursorA (long /*int*/ hInstance, long /*int*/ lpCursorName);
/**
 * @param hInstance cast=(HINSTANCE)
 * @param lpIconName cast=(LPWSTR)
 */
public static final native long /*int*/ LoadIconW (long /*int*/ hInstance, long /*int*/ lpIconName);
/**
 * @param hInstance cast=(HINSTANCE)
 * @param lpIconName cast=(LPSTR)
 */
public static final native long /*int*/ LoadIconA (long /*int*/ hInstance, long /*int*/ lpIconName);
/**
 * @param hinst cast=(HINSTANCE)
 * @param lpszName cast=(LPWSTR)
 */
public static final native long /*int*/ LoadImageW (long /*int*/ hinst, char [] lpszName, int uType, int cxDesired, int cyDesired, int fuLoad);
/**
 * @param hinst cast=(HINSTANCE)
 * @param lpszName cast=(LPSTR)
 */
public static final native long /*int*/ LoadImageA (long /*int*/ hinst, byte [] lpszName, int uType, int cxDesired, int cyDesired, int fuLoad);
/**
 * @param hinst cast=(HINSTANCE)
 * @param lpszName cast=(LPWSTR)
 */
public static final native long /*int*/ LoadImageW (long /*int*/ hinst, long /*int*/ lpszName, int uType, int cxDesired, int cyDesired, int fuLoad);
/**
 * @param hinst cast=(HINSTANCE)
 * @param lpszName cast=(LPSTR)
 */
public static final native long /*int*/ LoadImageA (long /*int*/ hinst, long /*int*/ lpszName, int uType, int cxDesired, int cyDesired, int fuLoad);
/**
 * @param hinst cast=(HINSTANCE)
 * @param lpBuffer cast=(LPWSTR)
 */
public static final native int LoadStringW (long /*int*/ hinst, int uID, char [] lpBuffer, int nBufferMax);
/**
 * @param hinst cast=(HINSTANCE)
 * @param lpBuffer cast=(LPSTR)
 */
public static final native int LoadStringA (long /*int*/ hinst, int uID, byte [] lpBuffer, int nBufferMax);
/** @param lpLibFileName cast=(LPWSTR) */
public static final native long /*int*/ LoadLibraryW (char [] lpLibFileName);
/** @param lpLibFileName cast=(LPSTR) */
public static final native long /*int*/ LoadLibraryA (byte [] lpLibFileName);
/** @param hMem cast=(HLOCAL) */
public static final native long /*int*/ LocalFree (long /*int*/ hMem);
/** @param hWndLock cast=(HWND) */
public static final native boolean LockWindowUpdate (long /*int*/ hWndLock);
public static final native int LODWORD (long l);
public static final native int LOWORD (long /*int*/ l);
/** @param hdc cast=(HDC) */
public static final native boolean LPtoDP (long /*int*/ hdc, POINT lpPoints, int nCount);
public static final native int MAKEWORD(int l, int h);
public static final native long /*int*/ MAKEWPARAM(int l, int h);
public static final native long /*int*/ MAKELPARAM(int l, int h);
public static final native long /*int*/ MAKELRESULT(int l, int h);
public static final native int MapVirtualKeyW (int uCode, int uMapType);
public static final native int MapVirtualKeyA (int uCode, int uMapType);
/**
 * @param hWndFrom cast=(HWND)
 * @param hWndTo cast=(HWND)
 * @param lpPoints cast=(LPPOINT)
 */
public static final native int MapWindowPoints (long /*int*/ hWndFrom, long /*int*/ hWndTo, POINT lpPoints, int cPoints);
/**
 * @param hWndFrom cast=(HWND)
 * @param hWndTo cast=(HWND)
 * @param lpPoints cast=(LPPOINT)
 */
public static final native int MapWindowPoints (long /*int*/ hWndFrom, long /*int*/ hWndTo, RECT lpPoints, int cPoints);
/** @method flags=dynamic */
public static final native boolean MCIWndRegisterClass ();
public static final native boolean MessageBeep (int uType);
/**
 * @param hWnd cast=(HWND)
 * @param lpText cast=(LPWSTR)
 * @param lpCaption cast=(LPWSTR)
 */
public static final native int MessageBoxW (long /*int*/ hWnd, char [] lpText, char [] lpCaption, int uType);
/**
 * @param hWnd cast=(HWND)
 * @param lpText cast=(LPSTR)
 * @param lpCaption cast=(LPSTR)
 */
public static final native int MessageBoxA (long /*int*/ hWnd, byte [] lpText, byte [] lpCaption, int uType);
/**
 * @param hdc cast=(HDC)
 * @param lpXform cast=(XFORM *)
 */
public static final native boolean ModifyWorldTransform(long /*int*/ hdc, float [] lpXform, int iMode);
/** @method flags=dynamic */
public static final native long /*int*/ MonitorFromWindow (long /*int*/ hwnd, int dwFlags);
/**
 * @param Destination cast=(PVOID),flags=no_in critical
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (char[] Destination, long /*int*/ SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in critical
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (byte [] Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in critical
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (byte [] Destination, ACCEL Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in critical
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (byte [] Destination, BITMAPINFOHEADER Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in critical
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (int [] Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in critical
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (long [] Destination, long /*int*/ SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in critical
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (double[] Destination, long /*int*/ SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in critical
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (float[] Destination, long /*int*/ SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in critical
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (short[] Destination, long /*int*/ SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (long /*int*/ Destination, byte [] Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (long /*int*/ Destination, char [] Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (long /*int*/ Destination, int [] Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (long /*int*/ Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, DEVMODEW Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, DEVMODEA Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, DOCHOSTUIINFO Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, GRADIENT_RECT Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, LOGFONTW Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, LOGFONTA Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, MEASUREITEMSTRUCT Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, MINMAXINFO Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, MSG Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, UDACCEL Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, NMTTDISPINFOW Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, NMTTDISPINFOA Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (long /*int*/ Destination, OPENFILENAME Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, RECT Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, SAFEARRAY Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (SAFEARRAY Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, TRIVERTEX Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, WINDOWPOS Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (BITMAPINFOHEADER Destination, byte [] Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (BITMAPINFOHEADER Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (CERT_CONTEXT Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (CERT_INFO Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (DEVMODEW Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (DEVMODEA Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (DOCHOSTUIINFO Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (DRAWITEMSTRUCT Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (EXTLOGPEN Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (FLICK_DATA Destination, long /*int*/ [] Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (FLICK_POINT Destination, long /*int*/ [] Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (HDITEM Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (HELPINFO Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (LOGFONTW Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (LOGFONTA Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (MEASUREITEMSTRUCT Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (MINMAXINFO Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (OFNOTIFY Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (OPENFILENAME Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (POINT Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (POINT Destination, long[] Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMHDR Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMRGINFO Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMCUSTOMDRAW Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMLVCUSTOMDRAW Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTBHOTITEM Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTREEVIEW Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTVCUSTOMDRAW Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTVITEMCHANGE Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMUPDOWN Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, NMLVCUSTOMDRAW Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, NMTVCUSTOMDRAW Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, NMTTCUSTOMDRAW Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, NMLVDISPINFO Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, NMTVDISPINFO Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMLVDISPINFO Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTVDISPINFO Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMLVFINDITEM Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMLVODSTATECHANGE Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMHEADER Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMLINK Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMLISTVIEW Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMREBARCHILDSIZE Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMREBARCHEVRON Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTOOLBAR Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTTCUSTOMDRAW Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTTDISPINFOW Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTTDISPINFOA Destination, long /*int*/ Source, int Length);
public static final native void MoveMemory (RECT Destination, long /*int*/[] Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (SHDRAGIMAGE Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (EMR Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (EMREXTCREATEFONTINDIRECTW Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, SHDRAGIMAGE Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (TEXTMETRICW Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (TEXTMETRICA Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (TOUCHINPUT Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (TVITEM Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (WINDOWPOS Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (MSG Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (UDACCEL Destination, long /*int*/ Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, DROPFILES Source, int Length);
/**
 * @param DestinationPtr cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (long /*int*/ DestinationPtr, double[] Source, int Length);
/**
 * @param DestinationPtr cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (long /*int*/ DestinationPtr, float[] Source, int Length);
/**
 * @param DestinationPtr cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (long /*int*/ DestinationPtr, long[] Source, int Length);
/**
 * @param DestinationPtr cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (long /*int*/ DestinationPtr, short[] Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (SCRIPT_ITEM Destination, long /*int*/ SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (SCRIPT_LOGATTR Destination, long /*int*/ SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (SCRIPT_PROPERTIES Destination, long /*int*/ SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, KEYBDINPUT Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, MOUSEINPUT Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long /*int*/ Destination, GESTURECONFIG Source, int Length);
/**
 * @param hdc cast=(HDC)
 * @param lPoint cast=(LPPOINT)
 */
public static final native boolean MoveToEx (long /*int*/ hdc, int x1, int x2, long /*int*/ lPoint);
/**
 * @param nCount cast=(DWORD)
 * @param pHandles cast=(LPHANDLE)
 * @param dwMilliseconds cast=(DWORD)
 * @param dwWakeMask cast=(DWORD)
 * @param dwFlags cast=(DWORD)
 */
public static final native int MsgWaitForMultipleObjectsEx (int nCount, long /*int*/ pHandles, int dwMilliseconds, int dwWakeMask, int dwFlags);
/**
 * @param lpMultiByteStr cast=(LPCSTR),flags=no_out critical
 * @param lpWideCharStr cast=(LPWSTR),flags=no_in critical
 */
public static final native int MultiByteToWideChar (int CodePage, int dwFlags, byte [] lpMultiByteStr, int cchMultiByte, char [] lpWideCharStr, int cchWideChar);
/**
 * @param lpMultiByteStr cast=(LPCSTR)
 * @param lpWideCharStr cast=(LPWSTR),flags=no_in critical
 */
public static final native int MultiByteToWideChar (int CodePage, int dwFlags, long /*int*/ lpMultiByteStr, int cchMultiByte, char [] lpWideCharStr, int cchWideChar);
/**
 * @method flags=dynamic
 * @param event cast=(DWORD)
 * @param hwnd cast=(HWND)
 * @param idObject cast=(LONG)
 * @param idChild cast=(LONG)
 */
public static final native void NotifyWinEvent (int event, long /*int*/ hwnd, int idObject, int idChild);
public static final native boolean OffsetRect (RECT lprc, int dx, int dy);
/** @param hrgn cast=(HRGN) */
public static final native int OffsetRgn (long /*int*/ hrgn, int nXOffset, int nYOffset);
/** @param pvReserved cast=(LPVOID) */
public static final native int OleInitialize (long /*int*/ pvReserved);
public static final native void OleUninitialize ();
/** @param hWndNewOwner cast=(HWND) */
public static final native boolean OpenClipboard (long /*int*/ hWndNewOwner);
/**
 * @method flags=dynamic
 * @param hwnd cast=(HWND)
 * @param pszClassList cast=(LPCWSTR)
 */
public static final native long /*int*/ OpenThemeData (long /*int*/ hwnd, char[] pszClassList);
/** @param hdc cast=(HDC) */
public static final native boolean PatBlt (long /*int*/ hdc, int x1, int x2, int w, int h, int rop);
/** @param szfile cast=(LPCWSTR) */
public static final native boolean PathIsExe (long /*int*/ szfile);
/** @param hWnd cast=(HWND) */
public static final native boolean PeekMessageW (MSG lpMsg, long /*int*/ hWnd, int wMsgFilterMin, int wMsgFilterMax, int wRemoveMsg);
/** @param hWnd cast=(HWND) */
public static final native boolean PeekMessageA (MSG lpMsg, long /*int*/ hWnd, int wMsgFilterMin, int wMsgFilterMax, int wRemoveMsg);
/** @param hdc cast=(HDC) */
public static final native boolean Pie (long /*int*/ hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect, int nXStartArc, int nYStartArc, int nXEndArc, int nYEndArc);
/** @param pt flags=struct */
public static final native void POINTSTOPOINT(POINT pt, long /*int*/ pts);
/**
 * @param hdc cast=(HDC)
 * @param points cast=(CONST POINT *),flags=no_out critical
 */
public static final native boolean Polygon (long /*int*/ hdc, int [] points, int nPoints);
/**
 * @param hdc cast=(HDC)
 * @param points cast=(CONST POINT *),flags=no_out critical
 */
public static final native boolean Polyline (long /*int*/ hdc, int[] points, int nPoints);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native boolean PostMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, long /*int*/ lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native boolean PostMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, long /*int*/ lParam);
/**
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native boolean PostThreadMessageW (int idThread, int Msg, long /*int*/ wParam, long /*int*/ lParam);
/**
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native boolean PostThreadMessageA (int idThread, int Msg, long /*int*/ wParam, long /*int*/ lParam);
public static final native short PRIMARYLANGID (int lgid);
/** @param lppd cast=(LPPRINTDLGW) */
public static final native boolean PrintDlgW (PRINTDLG lppd);
public static final native boolean PrintDlgA (PRINTDLG lppd);
/**
 * @method flags=dynamic
 * @param hwnd cast=(HWND)
 * @param hdcBlt cast=(HDC)
 */
public static final native boolean PrintWindow (long /*int*/ hwnd, long /*int*/ hdcBlt, int nFlags);
/** @method flags=dynamic */
public static final native int PSPropertyKeyFromString (char[] pszString, PROPERTYKEY pkey);
/**
 * @param rect flags=no_out
 * @param pt flags=no_out struct
 */
public static final native boolean PtInRect (RECT rect, POINT pt);
/** @param hrgn cast=(HRGN) */
public static final native boolean PtInRegion (long /*int*/ hrgn, int X, int Y);
/** @param hDC cast=(HDC) */
public static final native int RealizePalette (long /*int*/ hDC);
/** @param hdc cast=(HDC) */
public static final native boolean Rectangle (long /*int*/ hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect);
/**
 * @param hrgn cast=(HRGN)
 * @param lprc flags=no_out
 */
public static final native boolean RectInRegion (long /*int*/ hrgn, RECT lprc);
/**
 * @param hWnd cast=(HWND)
 * @param hrgnUpdate cast=(HRGN)
 */
public static final native boolean RedrawWindow (long /*int*/ hWnd, RECT lprcUpdate, long /*int*/ hrgnUpdate, int flags);
/** @param hKey cast=(HKEY) */
public static final native int RegCloseKey (long /*int*/ hKey);
/**
 * @param hKey cast=(HKEY)
 * @param lpSubKey cast=(LPWSTR)
 * @param lpClass cast=(LPWSTR)
 * @param lpSecurityAttributes cast=(LPSECURITY_ATTRIBUTES)
 * @param phkResult cast=(PHKEY)
 * @param lpdwDisposition cast=(LPDWORD)
 */
public static final native int RegCreateKeyExW (long /*int*/ hKey, char[] lpSubKey, int Reserved, char[] lpClass, int dwOptions, int samDesired, long /*int*/ lpSecurityAttributes, long /*int*/[] phkResult, long /*int*/[] lpdwDisposition);
/**
 * @param hKey cast=(HKEY)
 * @param lpSubKey cast=(LPSTR)
 * @param lpClass cast=(LPTSTR)
 * @param lpSecurityAttributes cast=(LPSECURITY_ATTRIBUTES)
 * @param phkResult cast=(PHKEY)
 * @param lpdwDisposition cast=(LPDWORD)
 */
public static final native int RegCreateKeyExA (long /*int*/ hKey, byte[] lpSubKey, int Reserved, byte[] lpClass, int dwOptions, int samDesired, long /*int*/ lpSecurityAttributes, long /*int*/[] phkResult, long /*int*/[] lpdwDisposition);
/**
 * @param hKey cast=(HKEY)
 * @param lpValueName cast=(LPWSTR)
 */
public static final native int RegDeleteValueW (long /*int*/ hKey, char[] lpValueName);
/**
 * @param hKey cast=(HKEY)
 * @param lpValueName cast=(LPSTR)
 */
public static final native int RegDeleteValueA (long /*int*/ hKey, byte[] lpValueName);
/**
 * @param hKey cast=(HKEY)
 * @param lpName cast=(LPWSTR)
 * @param lpcName cast=(LPDWORD)
 * @param lpReserved cast=(LPDWORD)
 * @param lpClass cast=(LPWSTR)
 * @param lpcClass cast=(LPDWORD)
 */
public static final native int RegEnumKeyExW (long /*int*/ hKey, int dwIndex, char [] lpName, int [] lpcName, int [] lpReserved, char [] lpClass, int [] lpcClass, FILETIME lpftLastWriteTime);
/**
 * @param hKey cast=(HKEY)
 * @param lpName cast=(LPSTR)
 * @param lpcName cast=(LPDWORD)
 * @param lpReserved cast=(LPDWORD)
 * @param lpClass cast=(LPSTR)
 * @param lpcClass cast=(LPDWORD)
 */
public static final native int RegEnumKeyExA (long /*int*/ hKey, int dwIndex, byte [] lpName, int [] lpcName, int [] lpReserved, byte [] lpClass, int [] lpcClass, FILETIME lpftLastWriteTime);
/** @param lpWndClass cast=(LPWNDCLASSW) */
public static final native int RegisterClassW (WNDCLASS lpWndClass);
public static final native int RegisterClassA (WNDCLASS lpWndClass);
/**
 * @method flags=dynamic
 * @param hWnd cast=(HWND)
 * @param ulFlags cast=(ULONG)
 */
public static final native boolean RegisterTouchWindow(long /*int*/ hWnd, int ulFlags);
/** @param lpString cast=(LPWSTR) */
public static final native int RegisterWindowMessageW (char [] lpString);
/** @param lpString cast=(LPTSTR) */
public static final native int RegisterWindowMessageA (byte [] lpString);
/** @param lpszFormat cast=(LPTSTR) */
public static final native int RegisterClipboardFormatA (byte[] lpszFormat); 
/** @param lpszFormat cast=(LPWSTR) */
public static final native int RegisterClipboardFormatW (char[] lpszFormat); 
/**
 * @param hKey cast=(HKEY)
 * @param lpSubKey cast=(LPWSTR)
 * @param phkResult cast=(PHKEY)
 */
public static final native int RegOpenKeyExW (long /*int*/ hKey, char[] lpSubKey, int ulOptions, int samDesired, long /*int*/[] phkResult);
/**
 * @param hKey cast=(HKEY)
 * @param lpSubKey cast=(LPSTR)
 * @param phkResult cast=(PHKEY)
 */
public static final native int RegOpenKeyExA (long /*int*/ hKey, byte[] lpSubKey, int ulOptions, int samDesired, long /*int*/[] phkResult);
/**
 * @param hKey cast=(HKEY)
 * @param lpClass cast=(LPWSTR)
 * @param lpcbClass cast=(LPDWORD)
 * @param lpReserved cast=(LPDWORD)
 * @param lpSubKeys cast=(LPDWORD)
 * @param lpcbMaxSubKeyLen cast=(LPDWORD)
 * @param lpcbMaxClassLen cast=(LPDWORD)
 * @param lpcValues cast=(LPDWORD)
 * @param lpcbMaxValueNameLen cast=(LPDWORD)
 * @param lpcbMaxValueLen cast=(LPDWORD)
 * @param lpcbSecurityDescriptor cast=(LPDWORD)
 * @param lpftLastWriteTime cast=(PFILETIME)
 */
public static final native int RegQueryInfoKeyW (long /*int*/ hKey, long /*int*/ lpClass, int[] lpcbClass, long /*int*/ lpReserved, int[] lpSubKeys, int[] lpcbMaxSubKeyLen, int[] lpcbMaxClassLen, int[] lpcValues, int[] lpcbMaxValueNameLen, int[] lpcbMaxValueLen, int[] lpcbSecurityDescriptor, long /*int*/ lpftLastWriteTime);
/**
 * @param hKey cast=(HKEY)
 * @param lpClass cast=(LPSTR)
 * @param lpcbClass cast=(LPDWORD)
 * @param lpReserved cast=(LPDWORD)
 * @param lpSubKeys cast=(LPDWORD)
 * @param lpcbMaxSubKeyLen cast=(LPDWORD)
 * @param lpcbMaxClassLen cast=(LPDWORD)
 * @param lpcValues cast=(LPDWORD)
 * @param lpcbMaxValueNameLen cast=(LPDWORD)
 * @param lpcbMaxValueLen cast=(LPDWORD)
 * @param lpcbSecurityDescriptor cast=(LPDWORD)
 * @param lpftLastWriteTime cast=(PFILETIME)
 */
public static final native int RegQueryInfoKeyA (long /*int*/ hKey, long /*int*/ lpClass, int[] lpcbClass, long /*int*/ lpReserved, int[] lpSubKeys, int[] lpcbMaxSubKeyLen, int[] lpcbMaxClassLen, int[] lpcValues, int[] lpcbMaxValueNameLen, int[] lpcbMaxValueLen, int[] lpcbSecurityDescriptor, long /*int*/ lpftLastWriteTime);
/**
 * @param hKey cast=(HKEY)
 * @param lpValueName cast=(LPWSTR)
 * @param lpReserved cast=(LPDWORD)
 * @param lpType cast=(LPDWORD)
 * @param lpData cast=(LPBYTE)
 * @param lpcbData cast=(LPDWORD)
 */
public static final native int RegQueryValueExW (long /*int*/ hKey, char[] lpValueName, long /*int*/ lpReserved, int[] lpType, char [] lpData, int[] lpcbData);
/**
 * @param hKey cast=(HKEY)
 * @param lpValueName cast=(LPWSTR)
 * @param lpReserved cast=(LPDWORD)
 * @param lpType cast=(LPDWORD)
 * @param lpData cast=(LPBYTE)
 * @param lpcbData cast=(LPDWORD)
 */
public static final native int RegQueryValueExW (long /*int*/ hKey, char[] lpValueName, long /*int*/ lpReserved, int[] lpType, int [] lpData, int[] lpcbData);
/**
 * @param hKey cast=(HKEY)
 * @param lpValueName cast=(LPSTR)
 * @param lpReserved cast=(LPDWORD)
 * @param lpType cast=(LPDWORD)
 * @param lpData cast=(LPBYTE)
 * @param lpcbData cast=(LPDWORD)
 */
public static final native int RegQueryValueExA (long /*int*/ hKey, byte[] lpValueName, long /*int*/ lpReserved, int[] lpType, byte [] lpData, int[] lpcbData);
/**
 * @param hKey cast=(HKEY)
 * @param lpValueName cast=(LPWSTR)
 * @param lpData cast=(const BYTE*)
 */
public static final native int RegSetValueExW (long /*int*/ hKey, char[] lpValueName, int Reserved, int dwType, int[] lpData, int cbData);
/**
 * @param hKey cast=(HKEY)
 * @param lpValueName cast=(LPSTR)
 * @param lpData cast=(const BYTE*)
 */
public static final native int RegSetValueExA (long /*int*/ hKey, byte[] lpValueName, int Reserved, int dwType, int[] lpData, int cbData);
/**
 * @param hKey cast=(HKEY)
 * @param lpValueName cast=(LPSTR)
 * @param lpReserved cast=(LPDWORD)
 * @param lpType cast=(LPDWORD)
 * @param lpData cast=(LPBYTE)
 * @param lpcbData cast=(LPDWORD)
 */
public static final native int RegQueryValueExA (long /*int*/ hKey, byte[] lpValueName, long /*int*/ lpReserved, int[] lpType, int [] lpData, int[] lpcbData);
public static final native boolean ReleaseCapture ();
/**
 * @param hWnd cast=(HWND)
 * @param hDC cast=(HDC)
 */
public static final native int ReleaseDC (long /*int*/ hWnd, long /*int*/ hDC);
/** @param hMenu cast=(HMENU) */
public static final native boolean RemoveMenu (long /*int*/ hMenu, int uPosition, int uFlags);
/**
 * @param hWnd cast=(HWND)
 * @param lpString cast=(LPCTSTR)
 */
public static final native long /*int*/ RemovePropA (long /*int*/ hWnd, long /*int*/ lpString);
/**
 * @param hWnd cast=(HWND)
 * @param lpString cast=(LPCWSTR)
 */
public static final native long /*int*/ RemovePropW (long /*int*/ hWnd, long /*int*/ lpString);
public static final native boolean ReplyMessage (long /*int*/ lResult);
/**
 * @param hdc cast=(HDC)
 * @param nSavedDC cast=(int)
 */
public static final native boolean RestoreDC (long /*int*/ hdc, int nSavedDC);
/** @param hdc cast=(HDC) */
public static final native boolean RoundRect (long /*int*/ hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect, int nWidth, int nHeight);
/** @param hdc cast=(HDC) */
public static final native int SaveDC (long /*int*/ hdc);
/** @param hWnd cast=(HWND) */
public static final native boolean ScreenToClient (long /*int*/ hWnd, POINT lpPoint);
/**
 * @param psds cast=(const SCRIPT_DIGITSUBSTITUTE*)
 * @param psc cast=(SCRIPT_CONTROL*)
 * @param pss cast=(SCRIPT_STATE*)
 */
public static final native int ScriptApplyDigitSubstitution (SCRIPT_DIGITSUBSTITUTE psds, SCRIPT_CONTROL psc, SCRIPT_STATE pss);
/**
 * @param pwcChars cast=(const WCHAR *)
 * @param psa cast=(const SCRIPT_ANALYSIS *)
 * @param psla cast=(SCRIPT_LOGATTR *)
 */
public static final native int ScriptBreak (char[] pwcChars, int cChars, SCRIPT_ANALYSIS psa, long /*int*/ psla);
/**
 * @param ppSp cast=(const SCRIPT_PROPERTIES ***)
 * @param piNumScripts cast=(int *)
 */
public static final native int ScriptGetProperties (long /*int*/[] ppSp, int[] piNumScripts);
/**
 * @param hdc cast=(HDC)
 * @param psc cast=(SCRIPT_CACHE *)
 * @param tmHeight cast=(long *)
 */
public static final native int ScriptCacheGetHeight (long /*int*/ hdc, long /*int*/ psc, int[] tmHeight);
/**
 * @param pwLogClust cast=(const WORD *)
 * @param psva cast=(const SCRIPT_VISATTR *)
 * @param piAdvance cast=(const int *)
 * @param psa cast=(const SCRIPT_ANALYSIS *)
 * @param piX cast=(int *)
 */
public static final native int ScriptCPtoX (int iCP, boolean fTrailing, int cChars, int cGlyphs, long /*int*/ pwLogClust, long /*int*/ psva, long /*int*/ piAdvance, SCRIPT_ANALYSIS psa, int[] piX);
/** @param psc cast=(SCRIPT_CACHE *) */
public static final native int ScriptFreeCache (long /*int*/ psc);
/**
 * @param hdc cast=(HDC)
 * @param psc cast=(SCRIPT_CACHE *)
 * @param sfp cast=(SCRIPT_FONTPROPERTIES *)
 */
public static final native int ScriptGetFontProperties (long /*int*/ hdc, long /*int*/ psc, SCRIPT_FONTPROPERTIES sfp);
/**
 * @param psa cast=(const SCRIPT_ANALYSIS *)
 * @param piGlyphWidth cast=(const int *)
 * @param pwLogClust cast=(const WORD *)
 * @param psva cast=(const SCRIPT_VISATTR *)
 * @param piDx cast=(int *)
 */
public static final native int ScriptGetLogicalWidths (SCRIPT_ANALYSIS psa, int cChars, int cGlyphs, long /*int*/ piGlyphWidth, long /*int*/ pwLogClust, long /*int*/ psva, int[] piDx);
/**
 * @param pwcInChars cast=(const WCHAR *)
 * @param psControl cast=(const SCRIPT_CONTROL *)
 * @param psState cast=(const SCRIPT_STATE *)
 * @param pItems cast=(SCRIPT_ITEM *)
 * @param pcItems cast=(int *)
 */
public static final native int ScriptItemize (char[] pwcInChars, int cInChars, int cMaxItems, SCRIPT_CONTROL psControl, SCRIPT_STATE psState, long /*int*/ pItems, int[] pcItems);
/**
 * @param psva cast=(SCRIPT_VISATTR *)
 * @param piAdvance cast=(const int *)
 * @param piJustify cast=(int *)
 */
public static final native int ScriptJustify (long /*int*/ psva, long /*int*/ piAdvance, int cGlyphs, int iDx, int iMinKashida, long /*int*/ piJustify);
/**
 * @param pbLevel cast=(const BYTE *)
 * @param piVisualToLogical cast=(int *)
 * @param piLogicalToVisual cast=(int *)
 */
public static final native int ScriptLayout (int cRuns, byte[] pbLevel, int[] piVisualToLogical, int[] piLogicalToVisual);
/**
 * @param hdc cast=(HDC)
 * @param psc cast=(SCRIPT_CACHE *)
 * @param pwGlyphs cast=(const WORD *)
 * @param psva cast=(const SCRIPT_VISATTR *)
 * @param psa cast=(SCRIPT_ANALYSIS *)
 * @param piAdvance cast=(int *)
 * @param pGoffset cast=(GOFFSET *)
 * @param pABC cast=(ABC *)
 */
public static final native int ScriptPlace (long /*int*/ hdc, long /*int*/ psc, long /*int*/ pwGlyphs, int cGlyphs, long /*int*/ psva, SCRIPT_ANALYSIS psa, long /*int*/ piAdvance, long /*int*/ pGoffset, int[] pABC);
/**
 * @param Locale cast=(LCID)
 * @param psds cast=(SCRIPT_DIGITSUBSTITUTE*)
 */
public static final native int ScriptRecordDigitSubstitution (int Locale, SCRIPT_DIGITSUBSTITUTE psds);
/**
 * @param hdc cast=(HDC)
 * @param psc cast=(SCRIPT_CACHE *)
 * @param pwcChars cast=(const WCHAR *)
 * @param pwOutGlyphs cast=(WORD*)
 */
public static final native int ScriptGetCMap (long /*int*/ hdc, long /*int*/ psc, char[] pwcChars, int cChars, int dwFlags, short[] pwOutGlyphs);
/**
 * @param hdc cast=(HDC)
 * @param psc cast=(SCRIPT_CACHE *)
 * @param pwcChars cast=(const WCHAR *)
 * @param psa cast=(SCRIPT_ANALYSIS *)
 * @param pwOutGlyphs cast=(WORD *)
 * @param pwLogClust cast=(WORD *)
 * @param psva cast=(SCRIPT_VISATTR *)
 * @param pcGlyphs cast=(int *)
 */
public static final native int ScriptShape (long /*int*/ hdc, long /*int*/ psc, char[] pwcChars, int cChars, int cMaxGlyphs, SCRIPT_ANALYSIS psa, long /*int*/ pwOutGlyphs, long /*int*/ pwLogClust, long /*int*/ psva, int[] pcGlyphs);
/**
 * @param hdc cast=(HDC)
 * @param pString cast=(const void*)
 * @param piDx cast=(const int*)
 * @param pTabdef cast=(SCRIPT_TABDEF*)
 * @param pbInClass cast=(const BYTE*)
 * @param pssa cast=(SCRIPT_STRING_ANALYSIS*)
 */
public static final native int ScriptStringAnalyse (long /*int*/ hdc, char[] pString, int cString, int cGlyphs, int iCharset, int dwFlags, int iReqWidth, SCRIPT_CONTROL psControl, SCRIPT_STATE psState, long /*int*/ piDx, long /*int*/ pTabdef, long /*int*/ pbInClass, long /*int*/ pssa);
/** @param ssa cast=(SCRIPT_STRING_ANALYSIS*),flags=struct */
public static final native int ScriptStringOut(long /*int*/ ssa, int iX, int iY, int uOptions, RECT prc, int iMinSel, int iMaxSel, boolean fDisabled);
/** @param pssa cast=(SCRIPT_STRING_ANALYSIS*) */
public static final native int ScriptStringFree(long /*int*/ pssa);
/**
 * @param hdc cast=(const HDC)
 * @param psc cast=(SCRIPT_CACHE *)
 * @param lprc cast=(const RECT *)
 * @param psa cast=(const SCRIPT_ANALYSIS *)
 * @param pwcReserved cast=(const WCHAR *)
 * @param pwGlyphs cast=(const WORD *)
 * @param piAdvance cast=(const int *)
 * @param piJustify cast=(const int *)
 * @param pGoffset cast=(const GOFFSET *)
 */
public static final native int ScriptTextOut (long /*int*/ hdc, long /*int*/ psc, int x, int y, int fuOptions, RECT lprc, SCRIPT_ANALYSIS psa, long /*int*/ pwcReserved, int iReserved, long /*int*/ pwGlyphs, int cGlyphs, long /*int*/ piAdvance, long /*int*/ piJustify, long /*int*/ pGoffset);
/**
 * @param pwLogClust cast=(const WORD *)
 * @param psva cast=(const SCRIPT_VISATTR *)
 * @param piAdvance cast=(const int *)
 * @param psa cast=(const SCRIPT_ANALYSIS *)
 * @param piCP cast=(int *)
 * @param piTrailing cast=(int *)
 */
public static final native int ScriptXtoCP (int iX, int cChars, int cGlyphs, long /*int*/ pwLogClust, long /*int*/ psva, long /*int*/ piAdvance, SCRIPT_ANALYSIS psa, int[] piCP, int[] piTrailing);
/**
 * @param hWnd cast=(HWND)
 * @param hrgnUpdate cast=(HRGN)
 */
public static final native int ScrollWindowEx (long /*int*/ hWnd, int dx, int dy, RECT prcScroll, RECT prcClip, long /*int*/ hrgnUpdate, RECT prcUpdate, int flags);
/**
 * @param hdc cast=(HDC)
 * @param hrgn cast=(HRGN)
 */
public static final native int SelectClipRgn (long /*int*/ hdc, long /*int*/ hrgn);
/**
 * @param hDC cast=(HDC)
 * @param HGDIObj cast=(HGDIOBJ)
 */
public static final native long /*int*/ SelectObject (long /*int*/ hDC, long /*int*/ HGDIObj);
/**
 * @param hDC cast=(HDC)
 * @param hpal cast=(HPALETTE)
 */
public static final native long /*int*/ SelectPalette (long /*int*/ hDC, long /*int*/ hpal, boolean bForceBackground);
/** @param pInputs cast=(LPINPUT) */
public static final native int SendInput (int nInputs, long /*int*/ pInputs, int cbSize);
/**
 * @method flags=no_gen
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, int [] wParam, int [] lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ [] wParam, long /*int*/ lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, char [] lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, int [] lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, short [] lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, long /*int*/ lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, LVCOLUMN lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, LVHITTESTINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, LITEM lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, LVITEM lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, LVINSERTMARK lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, MARGINS lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, MCHITTESTINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, POINT lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, REBARBANDINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, RECT lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, SYSTEMTIME lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, SHDRAGIMAGE lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TBBUTTON lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TBBUTTONINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TCITEM lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TCHITTESTINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TOOLINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TVHITTESTINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TVINSERTSTRUCT lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TVITEM lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TVSORTCB lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, UDACCEL lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, HDHITTESTINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, HDITEM lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, HDLAYOUT lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, BUTTON_IMAGELIST lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageW (long /*int*/ hWnd, int Msg, long /*int*/ wParam, SIZE lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, int [] wParam, int [] lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ [] wParam, long /*int*/ lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, byte [] lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, int [] lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, short [] lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, char [] lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, long /*int*/ lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, LVCOLUMN lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, LVHITTESTINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, LITEM lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, LVINSERTMARK lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, LVITEM lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, MARGINS lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, MCHITTESTINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, POINT lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, REBARBANDINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, RECT lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, SYSTEMTIME lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, SHDRAGIMAGE lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TBBUTTON lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TBBUTTONINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TCITEM lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TCHITTESTINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TOOLINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TVHITTESTINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TVINSERTSTRUCT lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TVITEM lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, TVSORTCB lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, UDACCEL lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, HDHITTESTINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, HDITEM lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, HDLAYOUT lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, BUTTON_IMAGELIST lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long /*int*/ SendMessageA (long /*int*/ hWnd, int Msg, long /*int*/ wParam, SIZE lParam);
/** @param hWnd cast=(HWND) */
public static final native long /*int*/ SetActiveWindow (long /*int*/ hWnd);
/**
 * @param hdc cast=(HDC)
 * @param colorRef cast=(COLORREF)
 */
public static final native int SetBkColor (long /*int*/ hdc, int colorRef);
/** @param hdc cast=(HDC) */
public static final native int SetBkMode (long /*int*/ hdc, int mode);
/**
 * @param hdc cast=(HDC)
 * @param lppt cast=(LPPOINT)
 */
public static final native boolean SetBrushOrgEx (long /*int*/ hdc, int nXOrg, int nYOrg, POINT lppt);
/** @param hWnd cast=(HWND) */
public static final native long /*int*/ SetCapture (long /*int*/ hWnd);
public static final native boolean SetCaretPos (int X, int Y);
/** @param hMem cast=(HANDLE) */
public static final native long /*int*/ SetClipboardData (int uFormat, long /*int*/ hMem);
/** @method flags=dynamic */
public static final native int SetCurrentProcessExplicitAppUserModelID (char[] AppID);
/** @param hCursor cast=(HCURSOR) */
public static final native long /*int*/ SetCursor (long /*int*/ hCursor);
public static final native boolean SetCursorPos (int X, int Y);
/**
 * @param hdc cast=(HDC)
 * @param pColors cast=(RGBQUAD *),flags=no_out critical
 */
public static final native int SetDIBColorTable (long /*int*/ hdc, int uStartIndex, int cEntries, byte[] pColors);
/**
 * @method flags=dynamic
 * @param lpString cast=(LPSTR)
 */
public static final native boolean SetDllDirectoryA (byte [] lpString);
/**
 * @method flags=dynamic
 * @param lpString cast=(LPWSTR)
 */
public static final native boolean SetDllDirectoryW (char [] lpString);
public static final native int SetErrorMode (int uMode);
/** @param hWnd cast=(HWND) */
public static final native long /*int*/ SetFocus (long /*int*/ hWnd);
/** @param hWnd cast=(HWND) */
public static final native boolean SetForegroundWindow (long /*int*/ hWnd);
/** 
 * @method flags=dynamic
 * @param hwnd cast=(HWND)
 * @param pGestureConfig cast=(PGESTURECONFIG)
 */
public static final native boolean SetGestureConfig(long /*int*/ hwnd, int dwReserved, int cIDs, long /*int*/ pGestureConfig, int cbSize);
/** @param hdc cast=(HDC) */
public static final native int SetGraphicsMode (long /*int*/ hdc, int iMode);
/**
 * @method flags=dynamic
 * @param hwnd cast=(HWND)
 */
public static final native boolean SetLayeredWindowAttributes(long /*int*/ hwnd, int crKey, byte bAlpha, int dwFlags);
/**
 * @method flags=dynamic
 * @param hdc cast=(HDC)
 * @param dwLayout cast=(DWORD)
 */
public static final native int SetLayout (long /*int*/ hdc, int dwLayout);
/** @param hdc cast=(HDC) */
public static final native int SetMapMode (long /*int*/ hdc, int fnMapMode);
/**
 * @param hdc cast=(HDC)
 * @param dwFlag cast=(DWORD)
 */
public static final native int SetMapperFlags (long /*int*/ hdc, int dwFlag);
/**
 * @param hWnd cast=(HWND)
 * @param hMenu cast=(HMENU)
 */
public static final native boolean SetMenu (long /*int*/ hWnd, long /*int*/ hMenu);
/** @param hMenu cast=(HMENU) */
public static final native boolean SetMenuDefaultItem (long /*int*/ hMenu, int uItem, int fByPos);
/**
 * @method flags=dynamic
 * @param hmenu cast=(HMENU)
 */
public static final native boolean SetMenuInfo (long /*int*/ hmenu, MENUINFO lpcmi);
/**
 * @param hMenu cast=(HMENU)
 * @param lpmii cast=(LPMENUITEMINFOW)
 */
public static final native boolean SetMenuItemInfoW (long /*int*/ hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii);
/** @param hMenu cast=(HMENU) */
public static final native boolean SetMenuItemInfoA (long /*int*/ hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii);
/** @param hdc cast=(HDC) */
public static final native int SetMetaRgn (long /*int*/ hdc);
/**
 * @param hPal cast=(HPALETTE)
 * @param lppe cast=(PALETTEENTRY *),flags=no_out critical
 */
public static final native int SetPaletteEntries (long /*int*/ hPal, int iStart, int cEntries, byte[] lppe);
/**
 * @param hWndChild cast=(HWND)
 * @param hWndNewParent cast=(HWND)
 */
public static final native long /*int*/ SetParent (long /*int*/ hWndChild, long /*int*/ hWndNewParent);
/** @param hdc cast=(HDC) */
public static final native int SetPixel (long /*int*/ hdc, int X, int Y, int crColor);
/** @param hdc cast=(HDC) */
public static final native int SetPolyFillMode (long /*int*/ hdc, int iPolyFillMode);
/** @method flags=dynamic */
public static final native boolean SetProcessDPIAware ();
/** @param lprc flags=no_in */
public static final native boolean SetRect (RECT lprc, int xLeft, int yTop, int xRight, int yBottom);
/** @param hrgn cast=(HRGN) */
public static final native boolean SetRectRgn (long /*int*/ hrgn, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect);
/** @param hdc cast=(HDC) */
public static final native int SetROP2 (long /*int*/ hdc, int fnDrawMode);
/** @param hwnd cast=(HWND) */
public static final native boolean SetScrollInfo (long /*int*/ hwnd, int flags, SCROLLINFO info, boolean fRedraw);
/** @param hdc cast=(HDC) */
public static final native int SetStretchBltMode (long /*int*/ hdc, int iStretchMode);
/**
 * @param hWnd cast=(HWND)
 * @param lpString cast=(LPCWSTR)
 * @param hData cast=(HANDLE)
 */
public static final native boolean SetPropW (long /*int*/ hWnd, long /*int*/ lpString, long /*int*/ hData);
/**
 * @param hWnd cast=(HWND)
 * @param lpString cast=(LPCTSTR)
 * @param hData cast=(HANDLE)
 */
public static final native boolean SetPropA (long /*int*/ hWnd, long /*int*/ lpString, long /*int*/ hData);
/** @param hdc cast=(HDC) */
public static final native int SetTextAlign (long /*int*/ hdc, int fMode);
/**
 * @param hdc cast=(HDC)
 * @param colorRef cast=(COLORREF)
 */
public static final native int SetTextColor (long /*int*/ hdc, int colorRef);
/**
 * @param hWnd cast=(HWND)
 * @param lpTimerFunc cast=(TIMERPROC)
 */
public static final native long /*int*/ SetTimer (long /*int*/ hWnd, long /*int*/ nIDEvent, int Elapse, long /*int*/ lpTimerFunc);
/** @param hdc cast=(HDC) */
public static final native boolean SetViewportExtEx (long /*int*/ hdc, int nXExtent, int nYExtent, SIZE lpSize);
/** @param hdc cast=(HDC) */
public static final native boolean SetViewportOrgEx (long /*int*/ hdc, int X, int Y, POINT lpPoint);
/** @param hWnd cast=(HWND) */
public static final native int SetWindowLongW (long /*int*/ hWnd, int nIndex, int dwNewLong);
/** @param hWnd cast=(HWND) */
public static final native int SetWindowLongA (long /*int*/ hWnd, int nIndex, int dwNewLong);
/**
 * @param hWnd cast=(HWND)
 * @param dwNewLong cast=(LONG_PTR)
 */
public static final native long /*int*/ SetWindowLongPtrW (long /*int*/ hWnd, int nIndex, long /*int*/ dwNewLong);
/**
 * @param hWnd cast=(HWND)
 * @param dwNewLong cast=(LONG_PTR)
 */
public static final native long /*int*/ SetWindowLongPtrA (long /*int*/ hWnd, int nIndex, long /*int*/ dwNewLong);
/** @param hdc cast=(HDC) */
public static final native boolean SetWindowExtEx (long /*int*/ hdc, int nXExtent, int nYExtent, SIZE lpSize);
/** @param hdc cast=(HDC) */
public static final native boolean SetWindowOrgEx (long /*int*/ hdc, int X, int Y, POINT lpPoint);
/** @param hWnd cast=(HWND) */
public static final native boolean SetWindowPlacement (long /*int*/ hWnd, WINDOWPLACEMENT lpwndpl);
/**
 * @param hWnd cast=(HWND)
 * @param hWndInsertAfter cast=(HWND)
 */
public static final native boolean SetWindowPos(long /*int*/ hWnd, long /*int*/ hWndInsertAfter, int X, int Y, int cx, int cy, int uFlags);
/**
 * @param hWnd cast=(HWND)
 * @param hRgn cast=(HRGN)
 */
public static final native int SetWindowRgn (long /*int*/ hWnd, long /*int*/ hRgn, boolean bRedraw);
/**
 * @param hWnd cast=(HWND)
 * @param lpString cast=(LPWSTR)
 */
public static final native boolean SetWindowTextW (long /*int*/ hWnd, char [] lpString);
/**
 * @param hWnd cast=(HWND)
 * @param lpString cast=(LPSTR)
 */
public static final native boolean SetWindowTextA (long /*int*/ hWnd, byte [] lpString);
/**
 * @method flags=dynamic
 * @param hwnd cast=(HWND)
 * @param pszSubAppName cast=(LPCWSTR)
 * @param pszSubIdList cast=(LPCWSTR)
 */
public static final native int SetWindowTheme (long /*int*/ hwnd, char [] pszSubAppName, char [] pszSubIdList);
/**
 * @param lpfn cast=(HOOKPROC)
 * @param hMod cast=(HINSTANCE)
 */
public static final native long /*int*/ SetWindowsHookExW (int idHook, long /*int*/ lpfn,  long /*int*/ hMod,  int dwThreadId);
/**
 * @param lpfn cast=(HOOKPROC)
 * @param hMod cast=(HINSTANCE)
 */
public static final native long /*int*/ SetWindowsHookExA (int idHook, long /*int*/ lpfn,  long /*int*/ hMod,  int dwThreadId);
/**
 * @param hdc cast=(HDC)
 * @param lpXform cast=(XFORM *)
 */
public static final native boolean SetWorldTransform(long /*int*/ hdc, float[] lpXform);
/** @param lpbi cast=(LPBROWSEINFOW) */
public static final native long /*int*/ SHBrowseForFolderW (BROWSEINFO lpbi);
public static final native long /*int*/ SHBrowseForFolderA (BROWSEINFO lpbi);
/** @param pmb cast=(PSHMENUBARINFO) */
public static final native boolean SHCreateMenuBar (SHMENUBARINFO pmb);
/**
 * @param pszPath cast=(LPCWSTR)
 * @param psfi cast=(SHFILEINFOW *)
 */
public static final native long /*int*/ SHGetFileInfoW (char [] pszPath, int dwFileAttributes, SHFILEINFOW psfi, int cbFileInfo, int uFlags);
/**
 * @param pszPath cast=(LPCSTR)
 * @param psfi cast=(SHFILEINFOA *)
 */
public static final native long /*int*/ SHGetFileInfoA (byte [] pszPath, int dwFileAttributes, SHFILEINFOA psfi, int cbFileInfo, int uFlags);
/**
 * @param hwndOwner cast=(HWND)
 * @param hToken cast=(HANDLE)
 * @param pszPath cast=(LPWSTR)
 */
public static final native int SHGetFolderPathW (long /*int*/ hwndOwner, int nFolder, long /*int*/ hToken, int dwFlags, char[] pszPath);
/**
 * @param hwndOwner cast=(HWND)
 * @param hToken cast=(HANDLE)
 * @param pszPath cast=(LPSTR)
 */
public static final native int SHGetFolderPathA (long /*int*/ hwndOwner, int nFolder, long /*int*/ hToken, int dwFlags, byte[] pszPath);
/** @param hwnd cast=(HWND) */
public static final native boolean SHHandleWMSettingChange (long /*int*/ hwnd, long /*int*/ wParam, long /*int*/ lParam, SHACTIVATEINFO psai);
public static final native int SHRecognizeGesture (SHRGINFO shrg);
public static final native void SHSendBackToFocusWindow (int uMsg, long /*int*/ wp, long /*int*/ lp);
/** @param hwnd cast=(HWND) */
public static final native boolean SHSipPreference (long /*int*/ hwnd, int st);
/** @param lpExecInfo cast=(LPSHELLEXECUTEINFOW) */
public static final native boolean ShellExecuteExW (SHELLEXECUTEINFO lpExecInfo);
public static final native boolean ShellExecuteExA (SHELLEXECUTEINFO lpExecInfo);
public static final native boolean Shell_NotifyIconA (int dwMessage, NOTIFYICONDATAA lpData);
public static final native boolean Shell_NotifyIconW (int dwMessage, NOTIFYICONDATAW lpData);
/** @param ppMalloc cast=(LPMALLOC *) */
public static final native int SHGetMalloc (long /*int*/ [] ppMalloc);
/**
 * @param pidl cast=(LPCITEMIDLIST)
 * @param pszPath cast=(LPWSTR)
 */
public static final native boolean SHGetPathFromIDListW (long /*int*/ pidl, char [] pszPath);
/**
 * @param pidl cast=(LPCITEMIDLIST)
 * @param pszPath cast=(LPSTR)
 */
public static final native boolean SHGetPathFromIDListA (long /*int*/ pidl, byte [] pszPath);
/** @method flags=dynamic */
public static final native int SHCreateItemInKnownFolder (byte [] kfid, int dwKFFlags, char [] pszItem, byte [] riid, long /*int*/ [] ppv);
/** @method flags=dynamic */
public static final native int SHCreateItemFromRelativeName (long /*int*/ psiParent, char [] pszName, long /*int*/ pbc, byte [] riid, long /*int*/ [] ppv);
/**
 * @param bVk cast=(BYTE)
 * @param hwnd cast=(HWND)
 */
public static final native boolean SHSetAppKeyWndAssoc (byte bVk, long /*int*/ hwnd);
/** @param hWnd cast=(HWND) */
public static final native boolean ShowCaret (long /*int*/ hWnd);
public static final native int ShowCursor (boolean bShow);
/** @param hWnd cast=(HWND) */
public static final native boolean ShowOwnedPopups (long /*int*/ hWnd, boolean fShow);
/** @param hWnd cast=(HWND) */
public static final native boolean ShowScrollBar (long /*int*/ hWnd, int wBar, boolean bShow);
/** @param hWnd cast=(HWND) */
public static final native boolean ShowWindow (long /*int*/ hWnd, int nCmdShow);
public static final native boolean SipGetInfo (SIPINFO pSipInfo);
/**
 * @param hdc cast=(HDC)
 * @param lpdi cast=(LPDOCINFOW)
 */
public static final native int StartDocW (long /*int*/ hdc, DOCINFO lpdi);
/** @param hdc cast=(HDC) */
public static final native int StartDocA (long /*int*/ hdc, DOCINFO lpdi);
/** @param hdc cast=(HDC) */
public static final native int StartPage (long /*int*/ hdc);
/**
 * @param hdcDest cast=(HDC)
 * @param hdcSrc cast=(HDC)
 */
public static final native boolean StretchBlt (long /*int*/ hdcDest, int nXOriginDest, int nYOriginDest, int nWidthDest, int nHeightDest, long /*int*/ hdcSrc, int nXOriginSrc, int nYOriginSrc, int nWidthSrc, int nHeightSrc, int dwRop);
/** @param hdc cast=(HDC) */
public static final native boolean StrokePath (long /*int*/ hdc);
public static final native boolean SystemParametersInfoW (int uiAction, int uiParam, HIGHCONTRAST pvParam, int fWinIni);
public static final native boolean SystemParametersInfoA (int uiAction, int uiParam, HIGHCONTRAST pvParam, int fWinIni);
public static final native boolean SystemParametersInfoW (int uiAction, int uiParam, RECT pvParam, int fWinIni);
public static final native boolean SystemParametersInfoA (int uiAction, int uiParam, RECT pvParam, int fWinIni);
public static final native boolean SystemParametersInfoW (int uiAction, int uiParam, NONCLIENTMETRICSW pvParam, int fWinIni);
public static final native boolean SystemParametersInfoA (int uiAction, int uiParam, NONCLIENTMETRICSA pvParam, int fWinIni);
public static final native boolean SystemParametersInfoW (int uiAction, int uiParam, int [] pvParam, int fWinIni);
public static final native boolean SystemParametersInfoA (int uiAction, int uiParam, int [] pvParam, int fWinIni);
/**
 * @param lpKeyState cast=(PBYTE)
 * @param lpChar cast=(LPWORD)
 */
public static final native int ToAscii (int uVirtKey, int uScanCode, byte [] lpKeyState, short [] lpChar, int uFlags);
/**
 * @param lpKeyState cast=(PBYTE)
 * @param pwszBuff cast=(LPWSTR)
 */
public static final native int ToUnicode (int wVirtKey, int wScanCode, byte [] lpKeyState, char [] pwszBuff, int cchBuff, int wFlags);
public static final native long TOUCH_COORD_TO_PIXEL(long touchCoord);
/**
 * @param hwndTV cast=(HWND)
 * @param hitem cast=(HTREEITEM)
 */
public static final native boolean TreeView_GetItemRect (long /*int*/ hwndTV, long /*int*/ hitem, RECT prc, boolean fItemRect);
public static final native boolean TrackMouseEvent (TRACKMOUSEEVENT lpEventTrack);
/**
 * @param hMenu cast=(HMENU)
 * @param hWnd cast=(HWND)
 */
public static final native boolean TrackPopupMenu (long /*int*/ hMenu, int uFlags, int x, int y, int nReserved, long /*int*/ hWnd, RECT prcRect);
/**
 * @param hWnd cast=(HWND)
 * @param hAccTable cast=(HACCEL)
 */
public static final native int TranslateAcceleratorW (long /*int*/ hWnd, long /*int*/ hAccTable, MSG lpMsg);
/**
 * @param hWnd cast=(HWND)
 * @param hAccTable cast=(HACCEL)
 */
public static final native int TranslateAcceleratorA (long /*int*/ hWnd, long /*int*/ hAccTable, MSG lpMsg);
/**
 * @param lpSrc cast=(DWORD *)
 * @param lpCs cast=(LPCHARSETINFO)
 */
public static final native boolean TranslateCharsetInfo (long /*int*/ lpSrc, int [] lpCs, int dwFlags);
/**
 * @param hWndClient cast=(HWND)
 * @param lpMsg cast=(LPMSG)
 */
public static final native boolean TranslateMDISysAccel (long /*int*/ hWndClient, MSG lpMsg);
public static final native boolean TranslateMessage (MSG lpmsg);
/** @method flags=dynamic */
public static final native boolean TransparentBlt (long /*int*/ hdcDest, int nXOriginDest, int nYOriginDest, int nWidthDest, int hHeightDest, long /*int*/ hdcSrc, int nXOriginSrc, int nYOriginSrc, int nWidthSrc, int nHeightSrc, int crTransparent);
/**
 * @param hdcDest cast=(HDC)
 * @param hSrc cast=(HANDLE)
 * @param TransparentColor cast=(COLORREF)
 */
public static final native boolean TransparentImage (long /*int*/ hdcDest, int DstX, int DstY, int DstCx, int DstCy,long /*int*/ hSrc, int SrcX, int SrcY, int SrcCx, int SrcCy, int TransparentColor);
/** @param hhk cast=(HHOOK) */
public static final native boolean UnhookWindowsHookEx (long /*int*/ hhk);
/**
 * @param lpClassName cast=(LPWSTR)
 * @param hInstance cast=(HINSTANCE)
 */
public static final native boolean UnregisterClassW (char [] lpClassName, long /*int*/ hInstance);
/**
 * @param lpClassName cast=(LPSTR)
 * @param hInstance cast=(HINSTANCE)
 */
public static final native boolean UnregisterClassA (byte [] lpClassName, long /*int*/ hInstance);
/**
 * @method flags=dynamic
 * @param hwnd cast=(HWND)
 * @param hdcDst cast=(HDC)
 * @param hdcSrc cast=(HDC)
 * @param crKey cast=(COLORREF)
 */
public static final native boolean UpdateLayeredWindow (long /*int*/ hwnd, long /*int*/ hdcDst, POINT pptDst, SIZE psize, long /*int*/ hdcSrc, POINT pptSrc, int crKey, BLENDFUNCTION pblend, int dwFlags);
/**
 * @method flags=dynamic
 * @param hwnd cast=(HWND)
 */
public static final native boolean UnregisterTouchWindow (long /*int*/ hwnd);
/** @param hWnd cast=(HWND) */
public static final native boolean UpdateWindow (long /*int*/ hWnd);
/**
 * @param pszPath cast=(LPCSTR)
 * @param pszURL cast=(LPSTR)
 */
public static final native int UrlCreateFromPathA (byte[] pszPath, byte[] pszURL, int[] pcchUrl, int flags);
/**
 * @param pszPath cast=(LPCWSTR)
 * @param pszURL cast=(LPWSTR)
 */
public static final native int UrlCreateFromPathW (char[] pszPath, char[] pszURL, int[] pcchUrl, int flags);
/** @param hWnd cast=(HWND) */
public static final native boolean ValidateRect (long /*int*/ hWnd, RECT lpRect);
/** @param ch cast=(WCHAR) */
public static final native short VkKeyScanW (short ch);
/** @param ch cast=(TCHAR) */
public static final native short VkKeyScanA (short ch);
/** @method flags=trycatch */
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl);

public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, int arg0);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, long arg0);

public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, int arg0, int arg1, int arg2, int[] arg3);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, long arg0, int arg1, int arg2, int[] arg3);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, long arg0, long arg1, int arg2, long[] arg3);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, int arg0, long arg1, int arg2, long[] arg3);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, long arg0, int arg1, int arg2, long[] arg3);

public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, char[] arg0, int arg1, int arg2, int[] arg3, int[] arg4);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, short arg0, byte[] arg1, byte[] arg2, byte[] arg3);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, int[] arg0);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, long[] arg0);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, byte[] arg0, int[] arg1);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, byte[] arg0, long[] arg1);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, int arg0, long /*int*/[] arg1, int[] arg2);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, long arg0, int[] arg1, int[] arg2);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, long arg0, long[] arg1, long[] arg2);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, TF_DISPLAYATTRIBUTE arg0);

public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, int arg0, long arg1, long arg2);
public static final native int VtblCall(int fnNumber, long /*int*/ ppVtbl, long arg0, long arg1, long arg2);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, int arg0, int arg1, int arg2);

public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, int arg0, int arg1);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, int arg0, long arg1);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, long arg0, long arg1);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, long arg0, int arg1);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, int[] arg0, byte[] arg1, int[] arg2);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, int[] arg0, byte[] arg1, long[] arg2);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, char[] arg0);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, char[] arg0, int arg1);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, char[] arg0, long arg1);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, PROPERTYKEY arg0, int arg1);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, PROPERTYKEY arg0, long arg1);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, int arg0, int arg1, char[] arg2, char[] arg3, int arg4);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, long arg0, int arg1, char[] arg2, char[] arg3, long arg4);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, int arg0, int[] arg1);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, long arg0, int[] arg1);
public static final native int VtblCall (int fnNumber, long /*int*/ ppVtbl, int arg0, long[] arg1);

public static final native boolean WaitMessage ();
/**
 * @param lpWideCharStr cast=(LPCWSTR),flags=no_out critical
 * @param lpMultiByteStr cast=(LPSTR),flags=no_in critical
 * @param lpDefaultChar cast=(LPCSTR)
 * @param lpUsedDefaultChar cast=(LPBOOL)
 */
public static final native int WideCharToMultiByte (int CodePage, int dwFlags, char [] lpWideCharStr, int cchWideChar, byte [] lpMultiByteStr, int cchMultiByte, byte [] lpDefaultChar, boolean [] lpUsedDefaultChar);
/**
 * @param lpWideCharStr cast=(LPCWSTR),flags=no_out critical
 * @param lpMultiByteStr cast=(LPSTR)
 * @param lpDefaultChar cast=(LPCSTR)
 * @param lpUsedDefaultChar cast=(LPBOOL)
 */
public static final native int WideCharToMultiByte (int CodePage, int dwFlags, char [] lpWideCharStr, int cchWideChar, long /*int*/ lpMultiByteStr, int cchMultiByte, byte [] lpDefaultChar, boolean [] lpUsedDefaultChar);
/** @param hDC cast=(HDC) */
public static final native long /*int*/ WindowFromDC (long /*int*/ hDC);
/** @param lpPoint flags=struct */
public static final native long /*int*/ WindowFromPoint (POINT lpPoint);
/** @param string cast=(const wchar_t *) */
public static final native int wcslen (long /*int*/ string);

/** @param hFileMappingObject cast=(HANDLE) 
 *  @param dwDesiredAccess cast=(DWORD)
 *  @param dwFileOffsetHigh cast=(DWORD)
 *  @param dwFileOffsetLow cast=(DWORD)
 */
public static final native long /*int*/ MapViewOfFile(long /*int*/ hFileMappingObject, int dwDesiredAccess, int dwFileOffsetHigh, int dwFileOffsetLow, int dwNumberOfBytesToMap);
/** @param lpBaseAddress cast=(LPCVOID) */
public static final native boolean UnmapViewOfFile(long /*int*/ lpBaseAddress);

public static final int PROCESS_DUP_HANDLE = 0x0040;
public static final int PROCESS_VM_READ = 0x0010;
public static final int DUPLICATE_SAME_ACCESS = 2;

/**
 * @param dwDesiredAccess cast=(DWORD)
 * @param dwProcessId cast=(DWORD)
 */
public static final native long /*int*/ OpenProcess(int dwDesiredAccess, boolean bInheritHandle, int dwProcessId);

public static final native long /*int*/ GetCurrentProcess();

/**
 * @param hSourceProcessHandle cast=(HANDLE) 
 * @param hSourceHandle cast=(HANDLE) 
 * @param hTargetProcessHandle cast=(HANDLE) 
 * @param lpTargetHandle cast=(LPHANDLE)
 * @param dwDesiredAccess cast=(DWORD) 
 * @param dwOptions cast=(DWORD) 
 */
public static final native boolean DuplicateHandle(long /*int*/ hSourceProcessHandle, long /*int*/ hSourceHandle, long /*int*/ hTargetProcessHandle,
		long /*int*/ [] lpTargetHandle, int dwDesiredAccess, boolean b, int dwOptions);

}
