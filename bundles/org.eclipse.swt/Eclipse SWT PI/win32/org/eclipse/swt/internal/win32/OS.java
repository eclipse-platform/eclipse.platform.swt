/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;


import org.eclipse.swt.internal.*;

public class OS {
	
	/*
	* SWT Windows flags.
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
	public static final int WIN32_MAJOR, WIN32_MINOR;

	/*
	* Flags for Window API GetVersionEx
	*/
	public static final int VER_PLATFORM_WIN32s = 0;
	public static final int VER_PLATFORM_WIN32_WINDOWS = 1;
	public static final int VER_PLATFORM_WIN32_NT = 2;
	public static final int VER_PLATFORM_WIN32_CE = 3;
	
	/*
	* Initialize the Windows flags
	*/
	static {
		
		/* Load the SWT library */
		Library.loadLibrary ("swt"); //$NON-NLS-1$

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
		OSVERSIONINFO info;
		
		// TEMPORARY CODE
		String MBCS = System.getProperty ("MBCS"); //$NON-NLS-1$
		if (MBCS != null) {
			info = new OSVERSIONINFOA ();
			info.dwOSVersionInfoSize = OSVERSIONINFOA.sizeof;
			OS.GetVersionExA ((OSVERSIONINFOA)info);
		} else {
			info = new OSVERSIONINFOW ();
			info.dwOSVersionInfoSize = OSVERSIONINFOW.sizeof;
			if (!OS.GetVersionExW ((OSVERSIONINFOW)info)) {
				info = new OSVERSIONINFOA ();
				info.dwOSVersionInfoSize = OSVERSIONINFOA.sizeof;
				OS.GetVersionExA ((OSVERSIONINFOA)info);
			}
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
		
		// TEMPORARY CODE
		if (MBCS != null) {
			IsUnicode = false;
			System.out.println ("*** SWT - Warning: Unicode disabled"); //$NON-NLS-1$
		} else {
			IsUnicode = !IsWin32s && !IsWin95;
		}

		/* Get the DBCS flag */
		int index = 0;
		while (index <= 0xFF) {
			if (OS.IsDBCSLeadByte ((byte) index)) break;
			index++;
		}
		IsDBLocale = index <= 0xFF;
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
	public static final int ALTERNATE = 1;
	public static final int BFFM_INITIALIZED = 0x1;
	public static final int BFFM_SETSELECTION = IsUnicode ? 0x467 : 0x466;
	public static final int BFFM_VALIDATEFAILED = IsUnicode ? 0x4 : 0x3;
	public static final int BFFM_VALIDATEFAILEDW = 0x4;
	public static final int BFFM_VALIDATEFAILEDA = 0x3;
	public static final int BF_BOTTOM = 0x8;
	public static final int BF_RIGHT = 0x4;
	public static final int BIF_EDITBOX = 0x10;
	public static final int BIF_NEWDIALOGSTYLE = 0x40;
	public static final int BIF_RETURNONLYFSDIRS = 0x1;
	public static final int BIF_VALIDATE = 0x20;
	public static final int BITSPIXEL = 0xc;
	public static final int BI_BITFIELDS = 3;
	public static final int BI_RGB = 0;
	public static final int BLACKNESS = 0x42;
	public static final int BM_CLICK = 0xf5;
	public static final int BM_GETCHECK = 0xf0;
	public static final int BM_SETCHECK = 0xf1;
	public static final int BM_SETIMAGE = 0xf7;
	public static final int BM_SETSTYLE = 0xf4;
	public static final int BN_CLICKED = 0x0;
	public static final int BN_DOUBLECLICKED = 0x5;
	public static final int BST_CHECKED = 0x1;
	public static final int BST_UNCHECKED = 0x0;
	public static final int BS_BITMAP = 0x80;
	public static final int BS_CENTER = 0x300;
	public static final int BS_CHECKBOX = 0x2;
	public static final int BS_DEFPUSHBUTTON = 0x1;
	public static final int BS_FLAT = 0x8000;
	public static final int BS_GROUPBOX = 0x7;
	public static final int BS_ICON = 0x40;
	public static final int BS_LEFT = 0x100;
	public static final int BS_NOTIFY = 0x4000;
	public static final int BS_OWNERDRAW = 0xb;
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
	public static final int CBN_EDITCHANGE = 0x5;
	public static final int CBN_KILLFOCUS = 0x4;
	public static final int CBN_SELCHANGE = 0x1;
	public static final int CBN_SETFOCUS = 0x3;
	public static final int CBS_AUTOHSCROLL = 0x40;
	public static final int CBS_DROPDOWN = 0x2;
	public static final int CBS_DROPDOWNLIST = 0x3;
	public static final int CBS_NOINTEGRALHEIGHT = 0x400;
	public static final int CBS_SIMPLE = 0x1;
	public static final int CB_ADDSTRING = 0x143;
	public static final int CB_DELETESTRING = 0x144;
	public static final int CB_ERR = 0xffffffff;
	public static final int CB_ERRSPACE = 0xfffffffe;
	public static final int CB_FINDSTRINGEXACT = 0x158;
	public static final int CB_GETCOUNT = 0x146;
	public static final int CB_GETCURSEL = 0x147;
	public static final int CB_GETDROPPEDCONTROLRECT = 0x152;
	public static final int CB_GETDROPPEDSTATE = 0x157;
	public static final int CB_GETEDITSEL = 0x140;
	public static final int CB_GETITEMHEIGHT = 0x154;
	public static final int CB_GETLBTEXT = 0x148;
	public static final int CB_GETLBTEXTLEN = 0x149;
	public static final int CB_INSERTSTRING = 0x14a;
	public static final int CB_LIMITTEXT = 0x141;
	public static final int CB_RESETCONTENT = 0x14b;
	public static final int CB_SELECTSTRING = 0x14d;
	public static final int CB_SETCURSEL = 0x14e;
	public static final int CB_SETEDITSEL = 0x142;
	public static final int CB_SHOWDROPDOWN = 0x14f;
	public static final int CCM_FIRST = 0x2000;
	public static final int CCM_SETBKCOLOR = 0x2001;
	public static final int CCS_NODIVIDER = 0x40;
	public static final int CCS_NORESIZE = 0x4;
	public static final int CC_ANYCOLOR = 0x100;
	public static final int CC_ENABLEHOOK = 0x10;
	public static final int CC_RGBINIT = 0x1;
	public static final int CDDS_POSTERASE = 0x00000004;
	public static final int CDDS_PREERASE = 0x00000003;
	public static final int CDDS_PREPAINT = 0x00000001;
	public static final int CDDS_ITEM = 0x00010000;
	public static final int CDDS_ITEMPREPAINT = CDDS_ITEM | CDDS_PREPAINT;
	public static final int CDDS_SUBITEM = 0x00020000;
	public static final int CDRF_DODEFAULT = 0x00000000;
	public static final int CDRF_NEWFONT = 0x00000002;
	public static final int CDRF_NOTIFYITEMDRAW = 0x00000020;
	public static final int CDRF_NOTIFYPOSTERASE = 0x00000040;
	public static final int CDRF_NOTIFYSUBITEMDRAW = 0x00000020;
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
	public static final int CF_EFFECTS = 0x100;
	public static final int CF_INITTOLOGFONTSTRUCT = 0x40;
	public static final int CF_SCREENFONTS = 0x1;
	public static final int CF_TEXT = 0x1;
	public static final int CF_UNICODETEXT = 13;
	public static final int CF_USESTYLE = 0x80;
	public static final int CLR_DEFAULT = 0xff000000;
	public static final int CLR_INVALID = 0xffffffff;
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
	public static final int COLOR_HIGHLIGHT = 0xd | SYS_COLOR_INDEX_FLAG;
	public static final int COLOR_HIGHLIGHTTEXT = 0xe | SYS_COLOR_INDEX_FLAG;
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
	public static final int CP_INSTALLED = 0x1;
	public static final int CS_BYTEALIGNWINDOW = 0x2000;
	public static final int CS_DBLCLKS = 0x8;
	public static final int CS_HREDRAW = 0x2;
	public static final int CS_VREDRAW = 0x1;
	public static final int CW_USEDEFAULT = 0x80000000;
	public static final int DCX_CACHE = 0x2;
	public static final int DCX_CLIPCHILDREN = 0x8;
	public static final int DCX_CLIPSIBLINGS = 0x10;
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
	public static final int DLGC_BUTTON = 0x2000;
	public static final int DLGC_HASSETSEL = 0x8;
	public static final int DLGC_STATIC = 0x100;
	public static final int DLGC_WANTALLKEYS = 0x4;
	public static final int DLGC_WANTARROWS = 0x1;
	public static final int DLGC_WANTCHARS = 0x80;
	public static final int DLGC_WANTTAB = 0x2;
	public static final int DM_SETDEFID = 0x401;
	public static final int DSS_DISABLED = 0x20;
	public static final int DSTINVERT = 0x550009;
	public static final int DST_BITMAP = 0x4;
	public static final int DST_ICON = 0x3;
	public static final int DT_CALCRECT = 0x400;
	public static final int DT_EDITCONTROL = 0x2000;
	public static final int DT_EXPANDTABS = 0x40;
	public static final int DT_LEFT = 0x0;
	public static final int DT_NOPREFIX = 0x800;
	public static final int DT_SINGLELINE = 0x20;
	public static final int DT_WORDBREAK = 0x10;
	public static final int ECOOP_AND = 0x3;
	public static final int ECOOP_OR = 0x2;
	public static final int ECO_AUTOHSCROLL = 0x80;
	public static final int EDGE_ETCHED = 0x6;
	public static final int EDGE_SUNKEN = 0xa;
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
	public static final int EM_SETOPTIONS = 0x44d;
	public static final int EM_SETPARAFORMAT = 0x447;
	public static final int EM_SETPASSWORDCHAR = 0xcc;
	public static final int EM_SETREADONLY = 0xcf;
	public static final int EM_SETSEL = 0xb1;
	public static final int EM_SETTABSTOPS = 0xcb;
	public static final int EN_ALIGN_LTR_EC = 0x0700;
	public static final int EN_ALIGN_RTL_EC = 0x0701;
	public static final int EN_CHANGE = 0x300;
	public static final int ERROR_NO_MORE_ITEMS = 0x103;
	public static final int ESB_DISABLE_BOTH = 0x3;
	public static final int ESB_ENABLE_BOTH = 0x0;
	public static final int ES_AUTOHSCROLL = 0x80;
	public static final int ES_CENTER = 0x1;
	public static final int ES_MULTILINE = 0x4;
	public static final int ES_NOHIDESEL = 0x100;
	public static final int ES_PASSWORD = 0x20;
	public static final int ES_READONLY = 0x800;
	public static final int ES_RIGHT = 0x2;
	public static final int FALT = 0x10;
	public static final int FCONTROL = 0x8;
	public static final int FNERR_INVALIDFILENAME = 0x3002;
	public static final int FSHIFT = 0x4;
	public static final int FVIRTKEY = 0x1;
	public static final int GCS_COMPSTR = 0x8;
	public static final int GCS_RESULTSTR = 0x800;
	public static final int GMDI_USEDISABLED = 0x1;
	public static final int GMEM_FIXED = 0x0;
	public static final int GMEM_ZEROINIT = 0x40;
	public static final int GN_CONTEXTMENU = 1000;
	public static final int GPTR = 0x40;
	public static final int GRADIENT_FILL_RECT_H = 0x0;
	public static final int GRADIENT_FILL_RECT_V = 0x1;
	public static final int GTL_NUMBYTES = 0x10;
	public static final int GTL_NUMCHARS = 0x8;
	public static final int GTL_PRECISE = 0x2;
	public static final int GT_DEFAULT = 0x0;
	public static final int GWL_EXSTYLE = 0xffffffec;
	public static final int GWL_ID = -12;
	public static final int GWL_STYLE = 0xfffffff0;
	public static final int GWL_USERDATA = 0xffffffeb;
	public static final int GWL_WNDPROC = 0xfffffffc;
	public static final int GW_CHILD = 0x5;
	public static final int GW_HWNDFIRST = 0x0;
	public static final int GW_HWNDLAST = 0x1;
	public static final int GW_HWNDNEXT = 0x2;
	public static final int GW_HWNDPREV = 0x3;
	public static final int HBMMENU_CALLBACK = 0xffffffff;
	public static final int HDI_WIDTH = 0x1;
	public static final int HDM_FIRST = 0x1200;
	public static final int HDM_GETBITMAPMARGIN = HDM_FIRST + 21;
	public static final int HDM_GETITEMCOUNT = 0x1200;
	public static final int HDN_FIRST = 0xfffffed4;
	public static final int HDN_BEGINTRACK = IsUnicode ? 0xfffffeba : 0xfffffece;
	public static final int HDN_BEGINTRACKW = 0xfffffeba;
	public static final int HDN_BEGINTRACKA = 0xfffffece;
	public static final int HDN_DIVIDERDBLCLICKA = HDN_FIRST - 5;
	public static final int HDN_DIVIDERDBLCLICKW = HDN_FIRST - 25;
	public static final int HDN_DIVIDERDBLCLICK = IsUnicode ? HDN_DIVIDERDBLCLICKW : HDN_DIVIDERDBLCLICKA;
	public static final int HDN_ITEMCHANGED = IsUnicode ? 0xfffffebf : 0xfffffed3;
	public static final int HDN_ITEMCHANGEDW = 0xfffffebf;
	public static final int HDN_ITEMCHANGEDA = 0xfffffed3;
	public static final int HDN_ITEMDBLCLICKW = HDN_FIRST - 23;
	public static final int HDN_ITEMDBLCLICKA = HDN_FIRST - 3;
	public static final int HDN_ITEMDBLCLICK = IsUnicode ? HDN_ITEMDBLCLICKW : HDN_ITEMDBLCLICKA;
	public static final int HEAP_ZERO_MEMORY = 0x8;
	public static final int HELPINFO_MENUITEM = 0x2;
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
	public static final int ICM_NOTOPEN = 0x0;
	public static final int ICON_BIG = 0x1;
	public static final int ICON_SMALL = 0x0;
	public static final int I_IMAGENONE = -2;
	public static final int IDABORT = 0x3;
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
	public static final int IDI_WINLOGO = 0x7f05;
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
	public static final int ILD_NORMAL = 0x0;
	public static final int IMAGE_BITMAP = 0x0;
	public static final int IMAGE_CURSOR = 0x2;
	public static final int IMAGE_ICON = 0x1;
	public static final int IME_CMODE_FULLSHAPE = 0x8;
	public static final int IME_CMODE_KATAKANA = 0x2;
	public static final int IME_CMODE_NATIVE = 0x1;
	public static final int IME_CMODE_ROMAN = 0x10;
	public static final int INFINITE = 0xffffffff;
	public static final int KEY_ENUMERATE_SUB_KEYS = 0x8;
	public static final int KEY_NOTIFY = 0x10;
	public static final int KEY_QUERY_VALUE = 0x1;
	public static final int KEY_READ = 0x20019;
	public static final int LAYOUT_RTL = 0x1;
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
	public static final int LB_SETCARETINDEX = 0x19e;
	public static final int LB_SETCURSEL = 0x186;
	public static final int LB_SETHORIZONTALEXTENT = 0x194;
	public static final int LB_SETSEL = 0x185;
	public static final int LB_SETTOPINDEX = 0x197;
	public static final int LF_FACESIZE = 32;
	public static final int LGRPID_ARABIC = 0xd;
	public static final int LGRPID_HEBREW = 0xc;
	public static final int LGRPID_INSTALLED = 1;
	public static final int LCID_SUPPORTED = 0x2;
	public static final int LOCALE_IDEFAULTANSICODEPAGE = 0x1004;
	public static final int LOCALE_SISO3166CTRYNAME = 0x5a;
	public static final int LOCALE_SISO639LANGNAME = 0x59;
	public static final int LOGPIXELSX = 0x58;
	public static final int LOGPIXELSY = 0x5a;
	public static final int LPSTR_TEXTCALLBACK = 0xffffffff;
	public static final int LR_DEFAULTCOLOR = 0x0;
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
	public static final int LVIR_BOUNDS = 0x0;
	public static final int LVIR_ICON = 0x1;
	public static final int LVIR_LABEL = 0x2;
	public static final int LVIS_DROPHILITED = 0x8;
	public static final int LVIS_FOCUSED = 0x1;
	public static final int LVIS_SELECTED = 0x2;
	public static final int LVIS_STATEIMAGEMASK = 0xf000;
	public static final int LVM_APPROXIMATEVIEWRECT = 0x1040;
	public static final int LVM_DELETEALLITEMS = 0x1009;
	public static final int LVM_DELETECOLUMN = 0x101c;
	public static final int LVM_DELETEITEM = 0x1008;
	public static final int LVM_ENSUREVISIBLE = 0x1013;
	public static final int LVM_FIRST = 0x1000;
	public static final int LVM_GETBKCOLOR = 0x1000;
	public static final int LVM_GETCOLUMN = IsUnicode ? 0x105f : 0x1019;
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
	public static final int LVM_GETSELECTEDCOUNT = 0x1032;
	public static final int LVM_GETSTRINGWIDTH = IsUnicode ? 0x1057 : 0x1011;
	public static final int LVM_GETSUBITEMRECT = 0x1038;
	public static final int LVM_GETTEXTCOLOR = 0x1023;
	public static final int LVM_GETTOOLTIPS = 0x104e;
	public static final int LVM_GETTOPINDEX = 0x1027;
	public static final int LVM_HITTEST = 0x1012;
	public static final int LVM_INSERTCOLUMN = IsUnicode ? 0x1061 : 0x101b;
	public static final int LVM_INSERTITEM = IsUnicode ? 0x104d : 0x1007;
	public static final int LVM_SCROLL = 0x1014;
	public static final int LVM_SETBKCOLOR = 0x1001;
	public static final int LVM_SETCALLBACKMASK = LVM_FIRST + 11;
	public static final int LVM_SETCOLUMN = IsUnicode ? 0x1060 : 0x101a;
	public static final int LVM_SETCOLUMNWIDTH = 0x101e;
	public static final int LVM_SETEXTENDEDLISTVIEWSTYLE = 0x1036;
	public static final int LVM_SETIMAGELIST = 0x1003;
	public static final int LVM_SETITEM = IsUnicode ? 0x104c : 0x1006;
	public static final int LVM_SETITEMCOUNT = LVM_FIRST + 47;
	public static final int LVM_SETITEMSTATE = 0x102b;
	public static final int LVM_SETTEXTBKCOLOR = 0x1026;
	public static final int LVM_SETTEXTCOLOR = 0x1024;
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
	public static final int LVSCW_AUTOSIZE = 0xffffffff;
	public static final int LVSCW_AUTOSIZE_USEHEADER = 0xfffffffe;
	public static final int LVSICF_NOINVALIDATEALL = 0x1;
	public static final int LVSICF_NOSCROLL = 0x2;
	public static final int LVSIL_SMALL = 0x1;
	public static final int LVSIL_STATE = 0x2;
	public static final int LVS_EX_FULLROWSELECT = 0x20;
	public static final int LVS_EX_GRIDLINES = 0x1;
	public static final int LVS_EX_LABELTIP = 0x4000;
	public static final int LVS_EX_ONECLICKACTIVATE = 0x40;
	public static final int LVS_EX_SUBITEMIMAGES = 0x2;
	public static final int LVS_EX_TRACKSELECT = 0x8;
	public static final int LVS_EX_TWOCLICKACTIVATE = 0x80;
	public static final int LVS_NOCOLUMNHEADER = 0x4000;
	public static final int LVS_NOSCROLL = 0x2000;
	public static final int LVS_OWNERDATA = 0x1000;
	public static final int LVS_REPORT = 0x1;
	public static final int LVS_SHAREIMAGELISTS = 0x40;
	public static final int LVS_SHOWSELALWAYS = 0x8;
	public static final int LVS_SINGLESEL = 0x4;
	public static final int MAX_PATH = 260;
	public static final int MA_NOACTIVATE = 0x3;
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
	public static final int MB_RTLREADING = 0x100000;
	public static final int MB_SYSTEMMODAL = 0x1000;
	public static final int MB_TASKMODAL = 0x2000;
	public static final int MB_YESNO = 0x4;
	public static final int MB_YESNOCANCEL = 0x3;
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
	public static final int MIIM_ID = 0x2;
	public static final int MIIM_STATE = 0x1;
	public static final int MIIM_SUBMENU = 0x4;
	public static final int MIIM_TYPE = 0x10;
	public static final int MIM_STYLE = 0x10;
	public static final int MK_CONTROL = 0x8;
	public static final int MK_LBUTTON = 0x1;
	public static final int MK_MBUTTON = 0x10;
	public static final int MK_RBUTTON = 0x2;
	public static final int MK_SHIFT = 0x4;
	public static final int MM_TEXT = 0x1;
	public static final int MNC_CLOSE = 0x1;
	public static final int MNS_CHECKORBMP = 0x4000000;
	public static final int MONITOR_DEFAULTTONEAREST = 0x2;
	public static final int MONITORINFOF_PRIMARY = 0x1;
	public static final int MWMO_INPUTAVAILABLE = 0x4;
	public static final int NM_FIRST = 0x0;
	public static final int NM_CLICK = 0xfffffffe;
	public static final int NM_CUSTOMDRAW = NM_FIRST - 12;
	public static final int NM_DBLCLK = 0xfffffffd;
	public static final int NM_RECOGNIZEGESTURE = NM_FIRST - 16;
	public static final int NM_RETURN = 0xfffffffc;
	public static final int NOTSRCCOPY = 0x330008;
	public static final int NULLREGION = 0x1;
	public static final int NULL_BRUSH = 0x5;
	public static final int NULL_PEN = 0x8;
	public static final int NUMRESERVED = 106;
	public static final int OBJID_CLIENT = 0xFFFFFFFC;
	public static final int OBJ_FONT = 0x6;
	public static final int OBJ_PEN = 0x1;
	public static final int OBM_CHECKBOXES = 0x7ff7;
	public static final int ODS_SELECTED = 0x1;
	public static final int ODT_MENU = 0x1;
	public static final int OFN_ALLOWMULTISELECT = 0x200;
	public static final int OFN_EXPLORER = 0x80000;
	public static final int OFN_HIDEREADONLY = 0x4;
	public static final int OFN_NOCHANGEDIR = 0x8;
	public static final int OPAQUE = 0x2;
	public static final int PATCOPY = 0xf00021;
	public static final int PATINVERT = 0x5a0049;
	public static final int PBM_GETPOS = 0x408;
	public static final int PBM_GETRANGE = 0x407;
	public static final int PBM_SETBARCOLOR = 0x409;
	public static final int PBM_SETBKCOLOR = 0x2001;
	public static final int PBM_SETPOS = 0x402;
	public static final int PBM_SETRANGE32 = 0x406;
	public static final int PBM_STEPIT = 0x405;
	public static final int PBS_SMOOTH = 0x1;
	public static final int PBS_VERTICAL = 0x4;
	public static final int PD_ALLPAGES = 0x0;
	public static final int PD_COLLATE = 0x10;
	public static final int PD_PAGENUMS = 0x2;
	public static final int PD_PRINTTOFILE = 0x20;
	public static final int PD_RETURNDC = 0x100;
	public static final int PD_SELECTION = 0x1;
	public static final int PD_USEDEVMODECOPIESANDCOLLATE = 0x40000;
	public static final int PFM_TABSTOPS = 0x10;
	public static final int PHYSICALHEIGHT = 0x6f;
	public static final int PHYSICALOFFSETX = 0x70;
	public static final int PHYSICALOFFSETY = 0x71;
	public static final int PHYSICALWIDTH = 0x6e;
	public static final int PLANES = 0xe;
	public static final int PM_NOREMOVE = 0x0;
	public static final int PM_REMOVE = 0x1;
	public static final String PROGRESS_CLASS = "msctls_progress32"; //$NON-NLS-1$
	public static final int PS_DASH = 0x1;
	public static final int PS_DASHDOT = 0x3;
	public static final int PS_DASHDOTDOT = 0x4;
	public static final int PS_DOT = 0x2;
	public static final int PS_ENDCAP_FLAT = 0x200;
	public static final int PS_GEOMETRIC = 0x10000;
	public static final int PS_JOIN_MITER = 0x2000;
	public static final int PS_SOLID = 0x0;
	public static final int PS_STYLE_MASK = 0xf;
	public static final int QS_KEY = 0x0001;
	public static final int QS_MOUSEMOVE = 0x0002;
	public static final int QS_MOUSEBUTTON = 0x0004;
	public static final int QS_POSTMESSAGE = 0x0008;
	public static final int QS_TIMER = 0x0010;
	public static final int QS_PAINT = 0x0020;
	public static final int QS_SENDMESSAGE = 0x0040;
	public static final int QS_ALLINPUT = QS_MOUSEMOVE | QS_MOUSEBUTTON | QS_KEY | QS_POSTMESSAGE | QS_TIMER | QS_PAINT | QS_SENDMESSAGE;
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
	public static final int RBN_CHEVRONPUSHED = RBN_FIRST - 10;
	public static final int RBN_HEIGHTCHANGE = 0xfffffcc1;
	public static final int RBS_DBLCLKTOGGLE = 0x8000;
	public static final int RBS_BANDBORDERS = 0x400;
	public static final int RBS_VARHEIGHT = 0x200;
	public static final int RB_DELETEBAND = 0x402;
	public static final int RB_GETBANDBORDERS = 0x422;
	public static final int RB_GETBANDCOUNT = 0x40c;
	public static final int RB_GETBANDINFO = IsUnicode ? 0x41c : 0x41d;
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
	public static final int RGN_AND = 0x1;
	public static final int RGN_DIFF = 0x4;
	public static final int RGN_ERROR = 0;
	public static final int RGN_OR = 0x2;
	public static final int SBS_HORZ = 0x0;
	public static final int SBS_VERT = 0x1;
	public static final int SB_BOTTOM = 0x7;
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
	public static final int SEM_FAILCRITICALERRORS = 0x1;
	public static final int SF_RTF = 0x2;
	public static final int SHCMBF_HIDDEN = 0x2;
	public static final int SHCMBM_OVERRIDEKEY = 0x400 + 403;
	public static final int SHCMBM_SETSUBMENU = 0x590;
	public static final int SHCMBM_GETSUBMENU = 0x591;
	public static final int SHMBOF_NODEFAULT = 0x1;
	public static final int SHMBOF_NOTIFY = 0x2;
	public static final int SHRG_RETURNCMD = 0x1;
	public static final int SIF_ALL = 0x17;
	public static final int SIF_DISABLENOSCROLL = 0x8;
	public static final int SIF_PAGE = 0x2;
	public static final int SIF_POS = 0x4;
	public static final int SIF_RANGE = 0x1;
	public static final int SIF_TRACKPOS = 0x10;
	public static final int SIP_DOWN = 1;
	public static final int SIP_UP = 0;
	public static final int SIPF_ON = 0x1;
	public static final int SIZE_MINIMIZED = 0x1;
	public static final int SIZEPALETTE = 104;
	public static final int SM_CMONITORS = 80;
	public static final int SM_CXBORDER = 0x5;
	public static final int SM_CXCURSOR = 0xd;
	public static final int SM_CXEDGE = 0x2d;
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
	public static final int SM_CYHSCROLL = 0x3;
	public static final int SM_CYMENU = 0xf;
	public static final int SM_CYSCREEN = 0x1;
	public static final int SM_CYVSCROLL = 0x14;
	public static final int SPI_GETWORKAREA = 0x30;
	public static final int SPI_GETNONCLIENTMETRICS = 41;
	public static final int SPI_GETWHEELSCROLLLINES = 104;
	public static final int SPI_SETSIPINFO = 224;
	public static final int SRCAND = 0x8800c6;
	public static final int SRCCOPY = 0xcc0020;
	public static final int SRCINVERT = 0x660046;
	public static final int SRCPAINT = 0xee0086;
	public static final int SS_BITMAP = 0xe;
	public static final int SS_CENTER = 0x1;
	public static final int SS_CENTERIMAGE = 0x200;
	public static final int SS_ICON = 0x3;
	public static final int SS_LEFT = 0x0;
	public static final int SS_LEFTNOWORDWRAP = 0xc;
	public static final int SS_NOTIFY = 0x100;
	public static final int SS_OWNERDRAW = 0xd;
	public static final int SS_REALSIZEIMAGE = 0x800;
	public static final int SS_RIGHT = 0x2;
	public static final int STANDARD_RIGHTS_READ = 0x20000;
	public static final int STD_COPY = 0x1;
	public static final int STD_CUT = 0x0;
	public static final int STD_FILENEW = 0x6;
	public static final int STD_FILEOPEN = 0x7;
	public static final int STD_FILESAVE = 0x8;
	public static final int STD_PASTE = 0x2;
	public static final int STM_SETIMAGE = 0x172;
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
	public static final int SYSTEM_FONT = 0xd;
	public static final int S_OK = 0x0;
	public static final int TBIF_COMMAND = 0x20;
	public static final int TBIF_STATE = 0x4;
	public static final int TBIF_IMAGE = 0x1;
	public static final int TBIF_LPARAM = 0x10;
	public static final int TBIF_SIZE = 0x40;
	public static final int TBIF_STYLE = 0x8;
	public static final int TBIF_TEXT = 0x2;
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
	public static final int TBSTATE_CHECKED = 0x1;
	public static final int TBSTYLE_CUSTOMERASE = 0x2000;
	public static final int TBSTYLE_DROPDOWN = 0x8;
	public static final int TBSTATE_ENABLED = 0x4;
	public static final int TBSTYLE_AUTOSIZE = 0x10;
	public static final int TBSTYLE_EX_DRAWDDARROWS = 0x1;
	public static final int TBSTYLE_FLAT = 0x800;
	public static final int TBSTYLE_LIST = 0x1000;
	public static final int TBSTYLE_TOOLTIPS = 0x100;
	public static final int TBSTYLE_TRANSPARENT = 0x8000;
	public static final int TBSTYLE_WRAPABLE = 0x200;
	public static final int TBS_AUTOTICKS = 0x1;
	public static final int TBS_BOTH = 0x8;
	public static final int TBS_HORZ = 0x0;
	public static final int TBS_VERT = 0x2;
	public static final int TB_ADDSTRING = IsUnicode ? 0x44d : 0x41c;
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
	public static final int TB_SETROWS = 0x427;
	public static final int TB_SETSTATE = 0x411;
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
	public static final int TME_HOVER = 0x1;
	public static final int TME_LEAVE = 0x2;
	public static final int TME_QUERY = 0x40000000;
	public static final int TMPF_VECTOR = 0x2;
	public static final String TOOLBARCLASSNAME = "ToolbarWindow32"; //$NON-NLS-1$
	public static final String TOOLTIPS_CLASS = "tooltips_class32"; //$NON-NLS-1$
	public static final int TPM_LEFTALIGN = 0x0;
	public static final int TPM_LEFTBUTTON = 0x0;
	public static final int TPM_RIGHTBUTTON = 0x2;
	public static final int TPM_RIGHTALIGN = 0x8;
	public static final String TRACKBAR_CLASS = "msctls_trackbar32"; //$NON-NLS-1$
	public static final int TRANSPARENT = 0x1;
	public static final int TTF_IDISHWND = 0x1;
	public static final int TTF_SUBCLASS = 0x10;
	public static final int TTF_RTLREADING = 0x4;
	public static final int TTM_ADDTOOL = IsUnicode ? 0x432 : 0x404;
	public static final int TTM_DELTOOL = IsUnicode ? 0x433 : 0x405;
	public static final int TTM_SETMAXTIPWIDTH = 0x418;
	public static final int TTM_UPDATE = 0x41D;
	public static final int TTN_FIRST = 0xfffffdf8;
	public static final int TTN_GETDISPINFO = IsUnicode ? 0xfffffdee : 0xfffffdf8;
	public static final int TTN_GETDISPINFOW = 0xfffffdee;
	public static final int TTN_GETDISPINFOA = 0xfffffdf8;
	public static final int TTN_POP = TTN_FIRST - 2;
	public static final int TTN_SHOW = TTN_FIRST - 1;
	public static final int TTS_ALWAYSTIP = 0x1;
	public static final int TV_FIRST = 0x1100;
	public static final int TVE_COLLAPSE = 0x1;
	public static final int TVE_EXPAND = 0x2;
	public static final int TVGN_CARET = 0x9;
	public static final int TVGN_CHILD = 0x4;
	public static final int TVGN_DROPHILITED = 0x8;
	public static final int TVGN_FIRSTVISIBLE = 0x5;
	public static final int TVGN_LASTVISIBLE = 0xa;
	public static final int TVGN_NEXT = 0x1;
	public static final int TVGN_NEXTVISIBLE = 0x6;
	public static final int TVGN_PARENT = 0x3;
	public static final int TVGN_PREVIOUSVISIBLE = 0x7;
	public static final int TVGN_ROOT = 0x0;
	public static final int TVHT_ONITEM = 0x46;
	public static final int TVHT_ONITEMICON = 0x2;
	public static final int TVHT_ONITEMLABEL = 0x4;
	public static final int TVHT_ONITEMSTATEICON = 0x40;
	public static final int TVIF_HANDLE = 0x10;
	public static final int TVIF_IMAGE = 0x2;
	public static final int TVIF_PARAM = 0x4;
	public static final int TVIF_SELECTEDIMAGE = 0x20;
	public static final int TVIF_STATE = 0x8;
	public static final int TVIF_TEXT = 0x1;
	public static final int TVIS_EXPANDED = 0x20;
	public static final int TVIS_SELECTED = 0x2;
	public static final int TVIS_STATEIMAGEMASK = 0xf000;
	public static final int TVI_FIRST = 0xffff0001;
	public static final int TVI_LAST = 0xffff0002;
	public static final int TVI_ROOT = 0xffff0000;
	public static final int TVM_DELETEITEM = 0x1101;
	public static final int TVM_ENSUREVISIBLE = 0x1114;
	public static final int TVM_EXPAND = 0x1102;
	public static final int TVM_GETBKCOLOR = 0x111f;
	public static final int TVM_GETCOUNT = 0x1105;
	public static final int TVM_GETIMAGELIST = 0x1108;
	public static final int TVM_GETITEM = IsUnicode ? 0x113e : 0x110c;
	public static final int TVM_GETITEMHEIGHT = 0x111c;
	public static final int TVM_GETITEMRECT = 0x1104;
	public static final int TVM_GETNEXTITEM = 0x110a;
	public static final int TVM_GETTEXTCOLOR = 0x1120;
	public static final int TVM_GETTOOLTIPS = TV_FIRST + 25;
	public static final int TVM_GETVISIBLECOUNT = TV_FIRST + 16;
	public static final int TVM_HITTEST = 0x1111;
	public static final int TVM_INSERTITEM = IsUnicode ? 0x1132 : 0x1100;
	public static final int TVM_SELECTITEM = 0x110b;
	public static final int TVM_SETBKCOLOR = 0x111d;
	public static final int TVM_SETIMAGELIST = 0x1109;
	public static final int TVM_SETINSERTMARK = 0x111a;
	public static final int TVM_SETITEM = IsUnicode ? 0x113f : 0x110d;
	public static final int TVM_SETTEXTCOLOR = 0x111e;
	public static final int TVN_BEGINDRAG = IsUnicode ? 0xfffffe38 : 0xfffffe69;
	public static final int TVN_BEGINDRAGW = 0xfffffe38;
	public static final int TVN_BEGINDRAGA = 0xfffffe69;
	public static final int TVN_BEGINRDRAG = IsUnicode ? 0xfffffe37 : 0xfffffe68;
	public static final int TVN_BEGINRDRAGW = 0xfffffe37;
	public static final int TVN_BEGINRDRAGA = 0xfffffe68;
	public static final int TVN_FIRST = 0xfffffe70;
	public static final int TVN_ITEMEXPANDING = IsUnicode ? 0xfffffe3a : 0xfffffe6b;
	public static final int TVN_ITEMEXPANDINGW = 0xfffffe3a;
	public static final int TVN_ITEMEXPANDINGA = 0xfffffe6b;
	public static final int TVN_SELCHANGED = IsUnicode ? 0xfffffe3d : 0xfffffe6e;
	public static final int TVN_SELCHANGEDW = 0xfffffe3d;
	public static final int TVN_SELCHANGEDA = 0xfffffe6e;
	public static final int TVN_SELCHANGING = IsUnicode ? 0xfffffe3e : 0xfffffe6f;
	public static final int TVN_SELCHANGINGW = 0xfffffe3e;
	public static final int TVN_SELCHANGINGA = 0xfffffe6f;
	public static final int TVSIL_NORMAL = 0x0;
	public static final int TVSIL_STATE = 0x2;
	public static final int TVS_DISABLEDRAGDROP = 0x10;
	public static final int TVS_FULLROWSELECT = 0x1000;
	public static final int TVS_HASBUTTONS = 0x1;
	public static final int TVS_HASLINES = 0x2;
	public static final int TVS_LINESATROOT = 0x4;
	public static final int TVS_NOTOOLTIPS = 0x80;
	public static final int TVS_SHOWSELALWAYS = 0x20;
	public static final int UIS_INITIALIZE = 3;
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
	public static final int VK_LBUTTON = 0x1;
	public static final int VK_LEFT = 0x25;
	public static final int VK_MBUTTON = 0x4;
	public static final int VK_MENU = 0x12;
	public static final int VK_MULTIPLY = 0x6A;
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
	public static final int VK_SCROLL = 0x91;
	public static final int VK_SEPARATOR = 0x6C;
	public static final int VK_SHIFT = 0x10;
	public static final int VK_SNAPSHOT = 0x2C;
	public static final int VK_SPACE = 0x20;
	public static final int VK_SUBTRACT = 0x6D;
	public static final int VK_TAB = 0x9;
	public static final int VK_UP = 0x26;
	public static final int VK_ADD = 0x6B;
	public static final int VK_APP1 = 0xc1;
	public static final int VK_APP2 = 0xc2;
	public static final int VK_APP3 = 0xc3;
	public static final int VK_APP4 = 0xc4;
	public static final int VK_APP5 = 0xc5;
	public static final int VK_APP6 = 0xc6;
	public static final String WC_LISTVIEW = "SysListView32"; //$NON-NLS-1$
	public static final String WC_TABCONTROL = "SysTabControl32"; //$NON-NLS-1$
	public static final String WC_TREEVIEW = "SysTreeView32"; //$NON-NLS-1$
	public static final int WH_GETMESSAGE = 0x3;
	public static final int WH_MSGFILTER = 0xFFFFFFFF;
	public static final int WHEEL_DELTA = 120;
	public static final int WHEEL_PAGESCROLL = 0xFFFFFFFF;
	public static final int WM_ACTIVATE = 0x6;
	public static final int WM_ACTIVATEAPP = 0x1c;
	public static final int WM_APP = 0x8000;
	public static final int WM_CANCELMODE = 0x1f;
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
	public static final int WM_GETDLGCODE = 0x87;
	public static final int WM_GETFONT = 0x31;
//	public static final int WM_GETICON = 0x7f;
	public static final int WM_GETOBJECT = 0x003D;
	public static final int WM_HELP = 0x53;
	public static final int WM_HOTKEY = 0x0312;
	public static final int WM_HSCROLL = 0x114;
	public static final int WM_IME_CHAR = 0x286;
	public static final int WM_IME_COMPOSITION = 0x10f;
	public static final int WM_INITDIALOG = 0x110;
	public static final int WM_INITMENUPOPUP = 0x117;
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
	public static final int WM_MOUSELAST = 0x20d;
	public static final int WM_MOVE = 0x3;
	public static final int WM_NCACTIVATE = 0x86;
	public static final int WM_NCCALCSIZE = 0x83;
	public static final int WM_NCHITTEST = 0x84;
	public static final int WM_NCLBUTTONDOWN = 0x00A1;
	public static final int WM_NOTIFY = 0x4e;
	public static final int WM_NULL = 0x0;
	public static final int WM_PAINT = 0xf;
	public static final int WM_PALETTECHANGED = 0x311;
	public static final int WM_PARENTNOTIFY = 0x0210;
	public static final int WM_PASTE = 0x302;
	public static final int WM_PRINTCLIENT = 0x0318;
	public static final int WM_QUERYENDSESSION = 0x11;
	public static final int WM_QUERYNEWPALETTE = 0x30f;
	public static final int WM_QUERYOPEN = 0x13;
	public static final int WM_RBUTTONDBLCLK = 0x206;
	public static final int WM_RBUTTONDOWN = 0x204;
	public static final int WM_RBUTTONUP = 0x205;
	public static final int WM_SETCURSOR = 0x20;
	public static final int WM_SETFOCUS = 0x7;
	public static final int WM_SETFONT = 0x30;
	public static final int WM_SETICON = 0x80;
	public static final int WM_SETREDRAW = 0xb;
	public static final int WM_SETTINGCHANGE = 0x1A;
	public static final int WM_SHOWWINDOW = 0x18;
	public static final int WM_SIZE = 0x5;
	public static final int WM_SYSCHAR = 0x106;
	public static final int WM_SYSCOLORCHANGE = 0x15;
	public static final int WM_SYSCOMMAND = 0x112;
	public static final int WM_SYSKEYDOWN = 0x104;
	public static final int WM_SYSKEYUP = 0x105;
	public static final int WM_TIMER = 0x113;
	public static final int WM_UNDO = 0x304;
	public static final int WM_USER = 0x400;
	public static final int WM_VSCROLL = 0x115;
	public static final int WM_WINDOWPOSCHANGED = 0x47;
	public static final int WM_WINDOWPOSCHANGING = 0x46;
	public static final int WS_BORDER = 0x800000;
	public static final int WS_CAPTION = 0xc00000;
	public static final int WS_CHILD = 0x40000000;
	public static final int WS_CLIPCHILDREN = 0x2000000;
	public static final int WS_CLIPSIBLINGS = 0x4000000;
	public static final int WS_EX_CAPTIONOKBTN = 0x80000000;
	public static final int WS_EX_CLIENTEDGE = 0x200;
	public static final int WS_EX_DLGMODALFRAME = 0x1;
	public static final int WS_EX_LAYOUTRTL = 0x00400000;
	public static final int WS_EX_LEFTSCROLLBAR = 0x00004000;
	public static final int WS_EX_MDICHILD = 0x00000040;
	public static final int WS_EX_NOINHERITLAYOUT = 0x00100000;
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
	
/** Ansi/Unicode wrappers */

public static final int CallWindowProc (int lpPrevWndFunc, int hWnd, int Msg, int wParam, int lParam) {
	if (IsUnicode) return CallWindowProcW (lpPrevWndFunc, hWnd, Msg, wParam, lParam);
	return CallWindowProcA (lpPrevWndFunc, hWnd, Msg, wParam, lParam);
}

public static final short CharUpper (short ch) {
	if (IsUnicode) return CharUpperW (ch);
	return CharUpperA (ch);
}

public static final short CharLower (short ch) {
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

public static final int CreateAcceleratorTable (byte [] lpaccl, int cEntries) {
	if (IsUnicode) return CreateAcceleratorTableW (lpaccl, cEntries);
	return CreateAcceleratorTableA (lpaccl, cEntries);
}

public static final int CreateDC (TCHAR lpszDriver, TCHAR lpszDevice, int lpszOutput, int lpInitData) {
	if (IsUnicode) {
		char [] lpszDriver1 = lpszDriver == null ? null : lpszDriver.chars;
		char [] lpszDevice1 = lpszDevice == null ? null : lpszDevice.chars;
		return CreateDCW (lpszDriver1, lpszDevice1, lpszOutput, lpInitData);
	}
	byte [] lpszDriver1 = lpszDriver == null ? null : lpszDriver.bytes;
	byte [] lpszDevice1 = lpszDevice == null ? null : lpszDevice.bytes;
	return CreateDCA (lpszDriver1, lpszDevice1, lpszOutput, lpInitData);
}

public static final int CreateFontIndirect (int lplf) {
	if (IsUnicode) return CreateFontIndirectW (lplf);
	return CreateFontIndirectA (lplf);
}

public static final int CreateFontIndirect (LOGFONT lplf) {
	if (IsUnicode) return CreateFontIndirectW ((LOGFONTW)lplf);
	return CreateFontIndirectA ((LOGFONTA)lplf);
}

public static final int CreateWindowEx (int dwExStyle, TCHAR lpClassName, TCHAR lpWindowName, int dwStyle, int X, int Y, int nWidth, int nHeight, int hWndParent, int hMenu, int hInstance, CREATESTRUCT lpParam) {
	if (IsUnicode) {
		char [] lpClassName1 = lpClassName == null ? null : lpClassName.chars;
		char [] lpWindowName1 = lpWindowName == null ? null : lpWindowName.chars;
		return CreateWindowExW (dwExStyle, lpClassName1, lpWindowName1, dwStyle, X, Y, nWidth, nHeight, hWndParent, hMenu, hInstance, lpParam);
	}
	byte [] lpClassName1 = lpClassName == null ? null : lpClassName.bytes;
	byte [] lpWindowName1 = lpWindowName == null ? null : lpWindowName.bytes;
	return CreateWindowExA (dwExStyle, lpClassName1, lpWindowName1, dwStyle, X, Y, nWidth, nHeight, hWndParent, hMenu, hInstance, lpParam);
}

public static final int DefMDIChildProc (int hWnd, int Msg, int wParam, int lParam) {
	if (IsUnicode) return DefMDIChildProcW (hWnd, Msg, wParam, lParam);
	return DefMDIChildProcA (hWnd, Msg, wParam, lParam);
}

public static final int DefFrameProc (int hWnd, int hWndMDIClient, int Msg, int wParam, int lParam) {
	if (IsUnicode) return DefFrameProcW (hWnd, hWndMDIClient, Msg, wParam, lParam);
	return DefFrameProcA (hWnd, hWndMDIClient, Msg, wParam, lParam);
}
public static final int DefWindowProc (int hWnd, int Msg, int wParam, int lParam) {
	if (IsUnicode) return DefWindowProcW (hWnd, Msg, wParam, lParam);
	return DefWindowProcA (hWnd, Msg, wParam, lParam);
}

public static final int DispatchMessage (MSG lpmsg) {
	if (IsUnicode) return DispatchMessageW (lpmsg);
	return DispatchMessageA (lpmsg);
}

public static final int DragQueryFile (int hDrop, int iFile, TCHAR lpszFile, int cch) {
	if (IsUnicode) {
		char [] lpszFile1 = lpszFile == null ? null : lpszFile.chars;
		return DragQueryFileW (hDrop, iFile, lpszFile1, cch);
	}
	byte [] lpszFile1 = lpszFile == null ? null : lpszFile.bytes;
	return DragQueryFileA (hDrop, iFile, lpszFile1, cch);
}

public static final boolean DrawState (int hdc, int hbr, int lpOutputFunc, int lData, int wData, int x, int y, int cx, int cy, int fuFlags) {
	if (IsUnicode) return DrawStateW (hdc, hbr, lpOutputFunc, lData, wData, x, y, cx, cy, fuFlags);
	return DrawStateA (hdc, hbr, lpOutputFunc, lData, wData, x, y, cx, cy, fuFlags);
}

public static final int DrawText (int hDC, TCHAR lpString, int nCount, RECT lpRect, int uFormat) {
	if (IsUnicode) {
		char [] lpString1 = lpString == null ? null : lpString.chars;
		return DrawTextW (hDC, lpString1, nCount, lpRect, uFormat);
	}
	byte [] lpString1 = lpString == null ? null : lpString.bytes;
	return DrawTextA (hDC, lpString1, nCount, lpRect, uFormat);
}

public static final int EnumFontFamilies (int hdc, TCHAR lpszFamily, int lpEnumFontFamProc, int lParam) {
	if (IsUnicode) {
		char [] lpszFamily1 = lpszFamily == null ? null : lpszFamily.chars;
		return EnumFontFamiliesW (hdc, lpszFamily1, lpEnumFontFamProc, lParam);
	}
	byte [] lpszFamily1 = lpszFamily == null ? null : lpszFamily.bytes;
	return EnumFontFamiliesA (hdc, lpszFamily1, lpEnumFontFamProc, lParam);
}

public static final boolean EnumSystemLocales (int lpLocaleEnumProc, int dwFlags) {
	if (IsUnicode) return EnumSystemLocalesW (lpLocaleEnumProc, dwFlags);
	return EnumSystemLocalesA (lpLocaleEnumProc, dwFlags);
}

public static final boolean EnumSystemLanguageGroups (int pLangGroupEnumProc, int dwFlags, int lParam) {
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

public static final int ExtractIconEx (TCHAR lpszFile, int nIconIndex, int [] phiconLarge, int [] phiconSmall, int nIcons) {
	if (IsUnicode) {
		char [] lpszFile1 = lpszFile == null ? null : lpszFile.chars;
		return ExtractIconExW (lpszFile1, nIconIndex, phiconLarge, phiconSmall, nIcons);
	}
	byte [] lpszFile1 = lpszFile == null ? null : lpszFile.bytes;
	return ExtractIconExA (lpszFile1, nIconIndex, phiconLarge, phiconSmall, nIcons);
}

public static final boolean ExtTextOut(int hdc, int X, int Y, int fuOptions, RECT lprc, TCHAR lpString, int cbCount, int[] lpDx) {
	if (IsUnicode) {
		char [] lpString1 = lpString == null ? null : lpString.chars;
		return ExtTextOutW (hdc, X, Y, fuOptions, lprc, lpString1, cbCount, lpDx);
	}
	byte [] lpString1 = lpString == null ? null : lpString.bytes;
	return ExtTextOutA (hdc, X, Y, fuOptions, lprc, lpString1, cbCount, lpDx);
}

public static final int FindWindow (TCHAR lpClassName, TCHAR lpWindowName) {
	if (IsUnicode) {
		char [] lpClassName1 = lpClassName == null ? null : lpClassName.chars;
		char [] lpWindowName1 = lpWindowName == null ? null : lpWindowName.chars;
		return FindWindowW (lpClassName1, lpWindowName1); 
	}
	byte [] lpClassName1 = lpClassName == null ? null : lpClassName.bytes;
	byte [] lpWindowName1 = lpWindowName == null ? null : lpWindowName.bytes;
	return FindWindowA (lpClassName1, lpWindowName1);
}

public static final boolean GetCharABCWidths (int hdc, int iFirstChar, int iLastChar, int [] lpabc) {
	if (IsUnicode) return GetCharABCWidthsW (hdc,iFirstChar, iLastChar, lpabc);
	return GetCharABCWidthsA (hdc,iFirstChar, iLastChar, lpabc);
}

public static final int GetCharacterPlacement (int hdc, TCHAR lpString, int nCount, int nMaxExtent, GCP_RESULTS lpResults, int dwFlags) {
	if (IsUnicode) {
		char [] lpString1 = lpString == null ? null : lpString.chars;
		return GetCharacterPlacementW (hdc, lpString1, nCount, nMaxExtent, lpResults, dwFlags);
	}
	byte [] lpString1 = lpString == null ? null : lpString.bytes;
	return GetCharacterPlacementA (hdc, lpString1, nCount, nMaxExtent, lpResults, dwFlags);	
}

public static final boolean GetCharWidth (int hdc, int iFirstChar, int iLastChar, int [] lpabc) {
	if (IsUnicode) return GetCharWidthW (hdc,iFirstChar, iLastChar, lpabc);
	return GetCharWidthA (hdc,iFirstChar, iLastChar, lpabc);
}

public static final boolean GetClassInfo (int hInstance, TCHAR lpClassName, WNDCLASS lpWndClass) {
	if (IsUnicode) {
		char [] lpClassName1 = lpClassName == null ? null : lpClassName.chars;
		return GetClassInfoW (hInstance, lpClassName1, lpWndClass);
	}
	byte [] lpClassName1 = lpClassName == null ? null : lpClassName.bytes;
	return GetClassInfoA (hInstance, lpClassName1, lpWndClass);
}

public static final int GetClipboardFormatName (int format, TCHAR lpszFormatName, int cchMaxCount) {
	if (IsUnicode) {
		char [] lpszFormatName1 = lpszFormatName == null ? null : lpszFormatName.chars;
		return GetClipboardFormatNameW (format, lpszFormatName1, cchMaxCount);
	}
	byte [] lpszFormatName1 = lpszFormatName == null ? null : lpszFormatName.bytes;
	return GetClipboardFormatNameA (format, lpszFormatName1, cchMaxCount);
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

public static final boolean GetMenuItemInfo (int hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii) {
	if (IsUnicode) return GetMenuItemInfoW (hMenu, uItem, fByPosition, lpmii);
	return GetMenuItemInfoA (hMenu, uItem, fByPosition, lpmii);
}

public static final boolean GetMessage (MSG lpMsg, int hWnd, int wMsgFilterMin, int wMsgFilterMax) {	
	if (IsUnicode) return GetMessageW (lpMsg, hWnd, wMsgFilterMin, wMsgFilterMax);
	return GetMessageA (lpMsg, hWnd, wMsgFilterMin, wMsgFilterMax);
}

public static final int GetModuleHandle (TCHAR lpModuleName) {
	if (IsUnicode) {
		char [] lpModuleName1 = lpModuleName == null ? null : lpModuleName.chars;
		return GetModuleHandleW (lpModuleName1);
	}
	byte [] lpModuleName1 = lpModuleName == null ? null : lpModuleName.bytes;
	return GetModuleHandleA (lpModuleName1);
}

public static final boolean GetMonitorInfo (int hmonitor, MONITORINFO lpmi) {
	if (IsUnicode) return GetMonitorInfoW (hmonitor, lpmi);
	return GetMonitorInfoA (hmonitor, lpmi);
}

public static final int GetObject (int hgdiobj, int cbBuffer, BITMAP lpvObject) {
	if (IsUnicode) return GetObjectW (hgdiobj, cbBuffer, lpvObject);
	return GetObjectA (hgdiobj, cbBuffer, lpvObject);
}

public static final int GetObject (int hgdiobj, int cbBuffer, DIBSECTION lpvObject) {
	if (IsUnicode) return GetObjectW (hgdiobj, cbBuffer, lpvObject);
	return GetObjectA (hgdiobj, cbBuffer, lpvObject);
}

public static final int GetObject (int hgdiobj, int cbBuffer, LOGBRUSH lpvObject) {
	if (IsUnicode) return GetObjectW (hgdiobj, cbBuffer, lpvObject);
	return GetObjectA (hgdiobj, cbBuffer, lpvObject);
}

public static final int GetObject (int hgdiobj, int cbBuffer, LOGFONT lpvObject) {
	if (IsUnicode) return GetObjectW (hgdiobj, cbBuffer, (LOGFONTW)lpvObject);
	return GetObjectA (hgdiobj, cbBuffer, (LOGFONTA)lpvObject);
}

public static final int GetObject (int hgdiobj, int cbBuffer, LOGPEN lpvObject) {
	if (IsUnicode) return GetObjectW (hgdiobj, cbBuffer, lpvObject);
	return GetObjectA (hgdiobj, cbBuffer, lpvObject);
}

public static final boolean GetOpenFileName (OPENFILENAME lpofn) {
	if (IsUnicode) return GetOpenFileNameW (lpofn);
	return GetOpenFileNameA (lpofn);
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

public static final boolean GetSaveFileName (OPENFILENAME lpofn) {
	if (IsUnicode) return GetSaveFileNameW (lpofn);
	return GetSaveFileNameA (lpofn);
}

public static final boolean GetTextExtentPoint32 (int hdc, TCHAR lpString, int cbString, SIZE lpSize) {
	if (IsUnicode) {
		char [] lpString1 = lpString == null ? null : lpString.chars;
		return GetTextExtentPoint32W (hdc, lpString1, cbString, lpSize);
	}
	byte [] lpString1 = lpString == null ? null : lpString.bytes;
	return GetTextExtentPoint32A (hdc, lpString1, cbString, lpSize);	
}

public static final boolean GetTextMetrics (int hdc, TEXTMETRIC lptm) {
	if (IsUnicode) return GetTextMetricsW (hdc, (TEXTMETRICW)lptm);
	return GetTextMetricsA (hdc, (TEXTMETRICA)lptm);
}

public static final boolean GetVersionEx (OSVERSIONINFO lpVersionInfo) {
	if (IsUnicode) return GetVersionExW ((OSVERSIONINFOW)lpVersionInfo);
	return GetVersionExA ((OSVERSIONINFOA)lpVersionInfo);
}

public static final int GetWindowLong (int hWnd, int nIndex) {
	if (IsUnicode) return GetWindowLongW (hWnd, nIndex);
	return GetWindowLongA (hWnd, nIndex);
}

public static final int GetWindowText (int hWnd, TCHAR lpString, int nMaxCount) {
	if (IsUnicode) {
		char [] lpString1 = lpString == null ? null : lpString.chars;
		return GetWindowTextW (hWnd, lpString1, nMaxCount);
	}
	byte [] lpString1 = lpString == null ? null : lpString.bytes;
	return GetWindowTextA (hWnd, lpString1, nMaxCount);
}

public static final int GetWindowTextLength (int hWnd) {
	if (IsUnicode) return GetWindowTextLengthW (hWnd);
	return GetWindowTextLengthA (hWnd);
}

public static final boolean ImmGetCompositionFont (int hIMC, LOGFONT lplf) {
	if (IsUnicode) return ImmGetCompositionFontW (hIMC, (LOGFONTW)lplf);
	return ImmGetCompositionFontA (hIMC, (LOGFONTA)lplf);
}

public static final boolean ImmSetCompositionFont (int hIMC, LOGFONT lplf) {
	if (IsUnicode) return ImmSetCompositionFontW (hIMC, (LOGFONTW)lplf);
	return ImmSetCompositionFontA (hIMC, (LOGFONTA)lplf);
}

public static final int ImmGetCompositionString (int hIMC, int dwIndex, TCHAR lpBuf, int dwBufLen) {
	if (IsUnicode) {
		char [] lpBuf1 = lpBuf == null ? null : lpBuf.chars;
		return ImmGetCompositionStringW (hIMC, dwIndex, lpBuf1, dwBufLen);
	}
	byte [] lpBuf1 = lpBuf == null ? null : lpBuf.bytes;
	return ImmGetCompositionStringA (hIMC, dwIndex, lpBuf1, dwBufLen);
}

public static final boolean InsertMenu (int hMenu, int uPosition, int uFlags, int uIDNewItem, TCHAR lpNewItem) {
	if (IsUnicode) {
		char [] lpNewItem1 = lpNewItem == null ? null : lpNewItem.chars;
		return InsertMenuW (hMenu, uPosition, uFlags, uIDNewItem, lpNewItem1);
	}
	byte [] lpNewItem1 = lpNewItem == null ? null : lpNewItem.bytes;
	return InsertMenuA (hMenu, uPosition, uFlags, uIDNewItem, lpNewItem1);	
}

public static final boolean InsertMenuItem (int hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii) {
	if (IsUnicode) return InsertMenuItemW (hMenu, uItem, fByPosition, lpmii);
	return InsertMenuItemA (hMenu, uItem, fByPosition, lpmii);
}

public static final int LoadBitmap (int hInstance, int lpBitmapName) {
	if (IsUnicode) return LoadBitmapW (hInstance, lpBitmapName);
	return LoadBitmapA (hInstance, lpBitmapName);
}

public static final int LoadCursor (int hInstance, int lpCursorName) {
	if (IsUnicode) return LoadCursorW (hInstance, lpCursorName);
	return LoadCursorA (hInstance, lpCursorName);
}

public static final int LoadIcon (int hInstance, int lpIconName) {
	if (IsUnicode) return LoadIconW (hInstance, lpIconName);
	return LoadIconA (hInstance, lpIconName);
}

public static final int LoadImage (int hinst, TCHAR lpszName, int uType, int cxDesired, int cyDesired, int fuLoad) {
	if (IsUnicode) {
		char [] lpszName1 = lpszName == null ? null : lpszName.chars;
		return LoadImageW (hinst, lpszName1, uType, cxDesired, cyDesired, fuLoad);
	}
	byte [] lpszName1 = lpszName == null ? null : lpszName.bytes;
	return LoadImageA (hinst, lpszName1, uType, cxDesired, cyDesired, fuLoad);
}

public static final int LoadLibrary (TCHAR lpLibFileName) {
	if (IsUnicode) {
		char [] lpLibFileName1 = lpLibFileName == null ? null : lpLibFileName.chars;
		return LoadLibraryW (lpLibFileName1);
	}
	byte [] lpLibFileName1 = lpLibFileName == null ? null : lpLibFileName.bytes;
	return LoadLibraryA (lpLibFileName1);
}

public static final int MapVirtualKey (int uCode, int uMapType) {
	if (IsUnicode) return MapVirtualKeyW (uCode, uMapType);
	return MapVirtualKeyA (uCode, uMapType);
}

public static final int MessageBox (int hWnd, TCHAR lpText, TCHAR lpCaption, int uType) {
	if (IsUnicode) {
		char [] lpText1 = lpText == null ? null : lpText.chars;
		char [] lpCaption1 = lpCaption == null ? null : lpCaption.chars;
		return MessageBoxW (hWnd, lpText1, lpCaption1, uType);
	}
	byte [] lpText1 = lpText == null ? null : lpText.bytes;
	byte [] lpCaption1 = lpCaption == null ? null : lpCaption.bytes;
	return MessageBoxA (hWnd, lpText1, lpCaption1, uType);
}

public static final void MoveMemory (int Destination, TCHAR Source, int Length) {
	if (IsUnicode) {
		char [] Source1 = Source == null ? null : Source.chars;
		MoveMemory (Destination, Source1, Length);
	} else {
		byte [] Source1 = Source == null ? null : Source.bytes;
		MoveMemory (Destination, Source1, Length);
	}
}

public static final void MoveMemory (TCHAR Destination, int Source, int Length) {
	if (IsUnicode) {
		char [] Destination1 = Destination == null ? null : Destination.chars;
		MoveMemory (Destination1, Source, Length);
	} else {
		byte [] Destination1 = Destination == null ? null : Destination.bytes;
		MoveMemory (Destination1, Source, Length);
	}
}

public static final void MoveMemory (int Destination, LOGFONT Source, int Length) {
	if (IsUnicode) {
		MoveMemory (Destination, (LOGFONTW)Source, Length);
	} else {
		MoveMemory (Destination, (LOGFONTA)Source, Length);
	}
}

public static final void MoveMemory (LOGFONT Destination, int Source, int Length) {
	if (IsUnicode) {
		MoveMemory ((LOGFONTW)Destination, Source, Length);
	} else {
		MoveMemory ((LOGFONTA)Destination, Source, Length);
	}
}

public static final void MoveMemory (int Destination, NMTTDISPINFO Source, int Length) {
	if (IsUnicode) {
		MoveMemory (Destination, (NMTTDISPINFOW)Source, Length);
	} else {
		MoveMemory (Destination, (NMTTDISPINFOA)Source, Length);
	}
}

public static final void MoveMemory (NMTTDISPINFO Destination, int Source, int Length) {
	if (IsUnicode) {
		MoveMemory ((NMTTDISPINFOW)Destination, Source, Length);
	} else {
		MoveMemory ((NMTTDISPINFOA)Destination, Source, Length);
	}
}

public static final boolean PeekMessage (MSG lpMsg, int hWnd, int wMsgFilterMin, int wMsgFilterMax, int wRemoveMsg) {
	if (IsUnicode) return PeekMessageW (lpMsg, hWnd, wMsgFilterMin, wMsgFilterMax, wRemoveMsg);
	return PeekMessageA (lpMsg, hWnd, wMsgFilterMin, wMsgFilterMax, wRemoveMsg);
}

public static final boolean PostMessage (int hWnd, int Msg, int wParam, int lParam) {
	if (IsUnicode) return PostMessageW (hWnd, Msg, wParam, lParam);
	return PostMessageA (hWnd, Msg, wParam, lParam);
}

public static final boolean PostThreadMessage (int idThread, int Msg, int wParam, int lParam) {
	if (IsUnicode) return PostThreadMessageW (idThread, Msg, wParam, lParam);
	return PostThreadMessageA (idThread, Msg, wParam, lParam);
}

public static final boolean PrintDlg (PRINTDLG lppd) {
	if (IsUnicode) return PrintDlgW (lppd);
	return PrintDlgA (lppd);
}

public static final int RegEnumKeyEx (int hKey, int dwIndex, TCHAR lpName, int [] lpcName, int [] lpReserved, TCHAR lpClass, int [] lpcClass, FILETIME lpftLastWriteTime) {
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

public static final int RegOpenKeyEx (int hKey, TCHAR lpSubKey, int ulOptions, int samDesired, int[] phkResult) {
	if (IsUnicode) {
		char [] lpSubKey1 = lpSubKey == null ? null : lpSubKey.chars;
		return RegOpenKeyExW (hKey, lpSubKey1, ulOptions, samDesired, phkResult);
	}
	byte [] lpSubKey1 = lpSubKey == null ? null : lpSubKey.bytes;
	return RegOpenKeyExA (hKey, lpSubKey1, ulOptions, samDesired, phkResult);
}

public static final int RegQueryInfoKey (int hKey, int lpClass, int[] lpcbClass, int lpReserved, int[] lpSubKeys, int[] lpcbMaxSubKeyLen, int[] lpcbMaxClassLen, int[] lpcValues, int[] lpcbMaxValueNameLen, int[] lpcbMaxValueLen, int[] lpcbSecurityDescriptor, int lpftLastWriteTime){
	if (IsUnicode) return RegQueryInfoKeyW (hKey, lpClass, lpcbClass, lpReserved, lpSubKeys, lpcbMaxSubKeyLen, lpcbMaxClassLen, lpcValues, lpcbMaxValueNameLen, lpcbMaxValueLen, lpcbSecurityDescriptor, lpftLastWriteTime);
	return RegQueryInfoKeyA (hKey, lpClass, lpcbClass, lpReserved, lpSubKeys, lpcbMaxSubKeyLen, lpcbMaxClassLen, lpcValues, lpcbMaxValueNameLen, lpcbMaxValueLen, lpcbSecurityDescriptor, lpftLastWriteTime);
}

public static final int RegQueryValueEx (int hKey, TCHAR lpValueName, int lpReserved, int[] lpType, TCHAR lpData, int[] lpcbData) {
	if (IsUnicode) {
		char [] lpValueName1 = lpValueName == null ? null : lpValueName.chars;
		char [] lpData1 = lpData == null ? null : lpData.chars;
		return RegQueryValueExW (hKey, lpValueName1, lpReserved, lpType, lpData1, lpcbData);
	}
	byte [] lpValueName1 = lpValueName == null ? null : lpValueName.bytes;
	byte [] lpData1 = lpData == null ? null : lpData.bytes;
	return RegQueryValueExA (hKey, lpValueName1, lpReserved, lpType, lpData1, lpcbData);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, TCHAR lParam) {
	if (IsUnicode) {
		char [] lParam1 = lParam == null ? null : lParam.chars;
		return SendMessageW (hWnd, Msg, wParam, lParam1);
	}
	byte [] lParam1 = lParam == null ? null : lParam.bytes;
	return SendMessageA (hWnd, Msg, wParam, lParam1);
}

public static final int SendMessage (int hWnd, int Msg, int [] wParam, int [] lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int [] wParam, int lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, int [] lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, short [] lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, int lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, LVCOLUMN lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, LVHITTESTINFO lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, LVITEM lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, REBARBANDINFO lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, RECT lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, TBBUTTON lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, TBBUTTONINFO lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, TCITEM lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, TOOLINFO lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, TVHITTESTINFO lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, TVINSERTSTRUCT lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final int SendMessage (int hWnd, int Msg, int wParam, TVITEM lParam) {
	if (IsUnicode) return SendMessageW (hWnd, Msg, wParam, lParam);
	return SendMessageA (hWnd, Msg, wParam, lParam);
}

public static final boolean SetMenuItemInfo (int hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii) {
	if (IsUnicode) return SetMenuItemInfoW (hMenu, uItem, fByPosition, lpmii);
	return SetMenuItemInfoA (hMenu, uItem, fByPosition, lpmii);
}

public static final int SetWindowLong (int hWnd, int nIndex, int dwNewLong) {
	if (IsUnicode) return SetWindowLongW (hWnd, nIndex, dwNewLong);
	return SetWindowLongA (hWnd, nIndex, dwNewLong);
}

public static final int SetWindowsHookEx (int idHook, int lpfn, int hMod, int dwThreadId) {
	if (IsUnicode) return SetWindowsHookExW (idHook, lpfn, hMod, dwThreadId);
	return SetWindowsHookExA (idHook, lpfn, hMod, dwThreadId);
}

public static final boolean SetWindowText (int hWnd, TCHAR lpString) {
	if (IsUnicode) {
		char [] lpString1 = lpString == null ? null : lpString.chars;
		return SetWindowTextW (hWnd, lpString1);
	}
	byte [] lpString1 = lpString == null ? null : lpString.bytes;
	return SetWindowTextA (hWnd, lpString1);
}

public static final int SHBrowseForFolder (BROWSEINFO lpbi) {
	if (IsUnicode) return SHBrowseForFolderW (lpbi);
	return SHBrowseForFolderA (lpbi);
}

public static final boolean ShellExecuteEx (SHELLEXECUTEINFO lpExecInfo) {
	if (IsUnicode) return ShellExecuteExW (lpExecInfo);
	return ShellExecuteExA (lpExecInfo);
}

public static final boolean SHGetPathFromIDList (int pidl, TCHAR pszPath) {
	if (IsUnicode) {
		char [] pszPath1 = pszPath == null ? null : pszPath.chars;
		return SHGetPathFromIDListW (pidl, pszPath1);
	}
	byte [] pszPath1 = pszPath == null ? null : pszPath.bytes;
	return SHGetPathFromIDListA (pidl, pszPath1);
}

public static final int StartDoc (int hdc, DOCINFO lpdi) {
	if (IsUnicode) return StartDocW (hdc, lpdi);
	return StartDocA (hdc, lpdi);
}

public static final boolean SystemParametersInfo (int uiAction, int uiParam, RECT pvParam, int fWinIni) {
	if (IsUnicode) return SystemParametersInfoW (uiAction, uiParam, pvParam, fWinIni);
	return SystemParametersInfoA (uiAction, uiParam, pvParam, fWinIni);
}

public static final boolean SystemParametersInfo (int uiAction, int uiParam, NONCLIENTMETRICS pvParam, int fWinIni) {
	if (IsUnicode) return SystemParametersInfoW (uiAction, uiParam, (NONCLIENTMETRICSW)pvParam, fWinIni);
	return SystemParametersInfoA (uiAction, uiParam, (NONCLIENTMETRICSA)pvParam, fWinIni);
}

public static final boolean SystemParametersInfo (int uiAction, int uiParam, int[] pvParam, int fWinIni) {
	if (IsUnicode) return SystemParametersInfoW (uiAction, uiParam, pvParam, fWinIni);
	return SystemParametersInfoA (uiAction, uiParam, pvParam, fWinIni);
}

public static final int TranslateAccelerator (int hWnd, int hAccTable, MSG lpMsg) {
	if (IsUnicode) return TranslateAcceleratorW (hWnd, hAccTable, lpMsg);
	return TranslateAcceleratorA (hWnd, hAccTable, lpMsg);
}

public static final boolean UnregisterClass (TCHAR lpClassName, int hInstance) {
	if (IsUnicode) {
		char [] lpClassName1 = lpClassName == null ? null : lpClassName.chars;
		return UnregisterClassW (lpClassName1, hInstance);
	}
	byte [] lpClassName1 = lpClassName == null ? null : lpClassName.bytes;
	return UnregisterClassA (lpClassName1, hInstance);
}

public static final short VkKeyScan (short ch) {
	if (IsUnicode) return VkKeyScanW (ch);
	return VkKeyScanA (ch);
}

/** Natives */
public static final native int AbortDoc (int hdc);
public static final native int ActivateKeyboardLayout(int hkl, int Flags);
public static final native boolean AdjustWindowRectEx (RECT lpRect, int dwStyle, boolean bMenu, int dwExStyle);
public static final native boolean Arc (int hdc,int nLeftRect,int nTopRect,int nRightRect,int nBottomRect,int nXStartArc,int nYStartArc,int nXEndArc,int nYEndArc);
public static final native int BeginDeferWindowPos (int nNumWindows);
public static final native int BeginPaint (int hWnd, PAINTSTRUCT lpPaint);
public static final native boolean BitBlt (int hdcDest,int nXDest,int nYDest,int nWidth,int nHeight,int hdcSrc,int nXSrc,int nYSrc,int dwRop);
public static final native boolean BringWindowToTop (int hWnd);
public static final native int Call (int address, DLLVERSIONINFO arg0);
public static final native int CallNextHookEx(int hhk, int nCode, int wParam, int lParam);
public static final native int CallWindowProcW (int lpPrevWndFunc, int hWnd, int Msg, int wParam, int lParam);
public static final native int CallWindowProcA (int lpPrevWndFunc, int hWnd, int Msg, int wParam, int lParam);
public static final native short CharLowerW (short ch);
public static final native short CharLowerA (short ch);
public static final native short CharUpperW (short ch);
public static final native short CharUpperA (short ch);
public static final native boolean CheckMenuItem (int hmenu, int uIDCheckItem, int uCheck); 
public static final native boolean ChooseColorW (CHOOSECOLOR lpcc);
public static final native boolean ChooseColorA (CHOOSECOLOR lpcc);
public static final native boolean ChooseFontW (CHOOSEFONT chooseFont);
public static final native boolean ChooseFontA (CHOOSEFONT chooseFont);
public static final native boolean ClientToScreen (int hWnd, POINT lpPoint);
public static final native boolean CloseClipboard ();
public static final native int CombineRgn (int hrgnDest, int hrgnSrc1, int hrgnSrc2, int fnCombineMode);
public static final native boolean CommandBar_AddAdornments (int hwndCB, int dwFlags, int dwReserved);
public static final native int CommandBar_Create (int hInst, int hwndParent, int idCmdBar);
public static final native void CommandBar_Destroy (int hwndCB);
public static final native boolean CommandBar_DrawMenuBar (int hwndCB, int iButton);
public static final native int CommandBar_Height (int hdnwCB);
public static final native boolean CommandBar_InsertMenubarEx (int hwndCB, int hInst, int pszMenu, int iButton);
public static final native boolean CommandBar_Show (int hwndCB, boolean fShow);
public static final native int CommDlgExtendedError ();
public static final native int CopyImage (int hImage, int uType, int cxDesired, int cyDesired, int fuFlags);
public static final native int CreateAcceleratorTableW (byte [] lpaccl, int cEntries); 
public static final native int CreateAcceleratorTableA (byte [] lpaccl, int cEntries);
public static final native int CreateBitmap (int nWidth, int nHeight, int cPlanes, int cBitsPerPel, byte [] lpvBits);
public static final native boolean CreateCaret (int hWnd, int hBitmap, int nWidth, int nHeight);
public static final native int CreateCompatibleBitmap (int hdc, int nWidth, int nHeight);
public static final native int CreateCompatibleDC (int hdc);
public static final native int CreateCursor (int hInst, int xHotSpot, int yHotSpot, int nWidth, int nHeight, byte [] pvANDPlane, byte [] pvXORPlane);
public static final native int CreateDCW (char [] lpszDriver, char [] lpszDevice, int lpszOutput, int lpInitData);  
public static final native int CreateDCA (byte [] lpszDriver, byte [] lpszDevice, int lpszOutput, int lpInitData);  
public static final native int CreateDIBSection(int hdc, byte[] pbmi, int iUsage, int[] ppvBits, int hSection, int dwOffset);
public static final native int CreateFontIndirectW (int lplf);
public static final native int CreateFontIndirectA (int lplf);
public static final native int CreateFontIndirectW (LOGFONTW lplf);
public static final native int CreateFontIndirectA (LOGFONTA lplf);
public static final native int CreateIconIndirect (ICONINFO lplf);
public static final native int CreateMenu ();
public static final native int CreatePalette (byte[] logPalette);
public static final native int CreatePatternBrush (int colorRef);
public static final native int CreatePen (int fnPenStyle, int nWidth, int crColor);
public static final native int CreatePolygonRgn(int[] lppt, int cPoints, int fnPolyFillMode);
public static final native int CreatePopupMenu ();
public static final native int CreateRectRgn (int left, int top, int right, int bottom);
public static final native int CreateSolidBrush (int colorRef);
public static final native int CreateStreamOnHGlobal(int hGlobal, boolean fDeleteOnRelease, int[] ppstm);
public static final native int CreateWindowExW (int dwExStyle, char [] lpClassName, char [] lpWindowName, int dwStyle, int X, int Y, int nWidth, int nHeight, int hWndParent, int hMenu, int hInstance, CREATESTRUCT lpParam);
public static final native int CreateWindowExA (int dwExStyle, byte [] lpClassName, byte [] lpWindowName, int dwStyle, int X, int Y, int nWidth, int nHeight, int hWndParent, int hMenu, int hInstance, CREATESTRUCT lpParam);
public static final native int DeferWindowPos (int hWinPosInfo, int hWnd, int hWndInsertAfter, int X, int Y, int cx, int cy, int uFlags);
public static final native int DefMDIChildProcW (int hWnd, int Msg, int wParam, int lParam);
public static final native int DefMDIChildProcA (int hWnd, int Msg, int wParam, int lParam);
public static final native int DefFrameProcW (int hWnd, int hWndMDIClient, int Msg, int wParam, int lParam);
public static final native int DefFrameProcA (int hWnd, int hWndMDIClient, int Msg, int wParam, int lParam);
public static final native int DefWindowProcW (int hWnd, int Msg, int wParam, int lParam);
public static final native int DefWindowProcA (int hWnd, int Msg, int wParam, int lParam);
public static final native boolean DeleteDC (int hdc);
public static final native boolean DeleteMenu (int hMenu, int uPosition, int uFlags);
public static final native boolean DeleteObject (int hGdiObj);
public static final native boolean DestroyAcceleratorTable (int hAccel);
public static final native boolean DestroyCaret ();
public static final native boolean DestroyCursor (int hCursor);
public static final native boolean DestroyIcon (int hIcon);
public static final native boolean DestroyMenu (int hMenu);
public static final native boolean DestroyWindow (int hWnd);
public static final native int DispatchMessageW (MSG lpmsg);
public static final native int DispatchMessageA (MSG lpmsg);
public static final native boolean DragDetect (int hwnd, POINT pt);
public static final native void DragFinish (int hDrop);
public static final native int DragQueryFileA (int hDrop, int iFile, byte[] lpszFile, int cch);
public static final native int DragQueryFileW (int hDrop, int iFile, char[] lpszFile, int cch);
public static final native boolean DrawEdge (int hdc, RECT qrc, int edge, int grfFlags);
public static final native boolean DrawFocusRect (int hDC, RECT lpRect);
public static final native boolean DrawFrameControl (int hdc, RECT lprc, int uType, int uState);
public static final native boolean DrawIconEx (int hdc, int xLeft, int yTop, int hIcon, int cxWidth, int cyWidth, int istepIfAniCur, int hbrFlickerFreeDraw, int diFlags);
public static final native boolean DrawMenuBar (int hWnd);
public static final native boolean DrawStateW (int hdc, int hbr, int lpOutputFunc, int lData, int wData, int x, int y, int cx, int cy, int fuFlags);
public static final native boolean DrawStateA (int hdc, int hbr, int lpOutputFunc, int lData, int wData, int x, int y, int cx, int cy, int fuFlags);
public static final native int DrawTextW (int hDC, char [] lpString, int nCount, RECT lpRect, int uFormat);
public static final native int DrawTextA (int hDC, byte [] lpString, int nCount, RECT lpRect, int uFormat);
public static final native boolean Ellipse (int hdc,int nLeftRect,int nTopRect,int nRightRect,int nBottomRect);
public static final native boolean EnableMenuItem (int hMenu, int uIDEnableItem, int uEnable);
public static final native boolean EnableScrollBar (int hWnd, int wSBflags, int wArrows);
public static final native boolean EnableWindow (int hWnd, boolean bEnable);
public static final native boolean EnumSystemLanguageGroupsW(int pLangGroupEnumProc, int dwFlags, int lParam);
public static final native boolean EnumSystemLanguageGroupsA(int pLangGroupEnumProc, int dwFlags, int lParam);
public static final native boolean EnumSystemLocalesW (int lpLocaleEnumProc, int dwFlags);
public static final native boolean EnumSystemLocalesA (int lpLocaleEnumProc, int dwFlags);
public static final native boolean EndDeferWindowPos (int hWinPosInfo);
public static final native int EndDoc (int hdc);
public static final native int EndPage (int hdc);
public static final native int EndPaint (int hWnd, PAINTSTRUCT lpPaint);
public static final native boolean EnumDisplayMonitors (int hdc, RECT lprcClip, int lpfnEnum, int dwData);
public static final native int EnumFontFamiliesW (int hdc, char [] lpszFamily, int lpEnumFontFamProc, int lParam);
public static final native int EnumFontFamiliesA (int hdc, byte [] lpszFamily, int lpEnumFontFamProc, int lParam);
public static final native boolean EqualRect (RECT lprc1, RECT lprc2);
public static final native boolean EqualRgn (int hSrcRgn1, int hSrcRgn2);
public static final native int ExpandEnvironmentStringsW (char [] lpSrc, char [] lsDst, int nSize);
public static final native int ExpandEnvironmentStringsA (byte [] lpSrc, byte [] lsDst, int nSize);
public static final native boolean ExtTextOutW(int hdc, int X, int Y, int fuOptions, RECT lprc, char[] lpString, int cbCount, int[] lpDx);
public static final native boolean ExtTextOutA(int hdc, int X, int Y, int fuOptions, RECT lprc, byte[] lpString, int cbCount, int[] lpDx);
public static final native int ExtractIconExW (char [] lpszFile, int nIconIndex, int [] phiconLarge, int [] phiconSmall, int nIcons);
public static final native int ExtractIconExA (byte [] lpszFile, int nIconIndex, int [] phiconLarge, int [] phiconSmall, int nIcons);
public static final native int FillRect(int hDC, RECT lprc, int hbr);
public static final native int FindWindowA (byte [] lpClassName, byte [] lpWindowName);
public static final native int FindWindowW (char [] lpClassName, char [] lpWindowName); 
public static final native boolean FreeLibrary (int hLibModule);
public static final native int GetACP ();
public static final native int GetActiveWindow ();
public static final native int GetBkColor (int hDC);
public static final native int GetCapture ();
public static final native boolean GetCaretPos (POINT lpPoint);
public static final native boolean GetCharABCWidthsA (int hdc, int iFirstChar, int iLastChar, int [] lpabc);
public static final native boolean GetCharABCWidthsW (int hdc, int iFirstChar, int iLastChar, int [] lpabc);
public static final native int GetCharacterPlacementW(int hdc, char[] lpString, int nCount, int nMaxExtent, GCP_RESULTS lpResults, int dwFlags);
public static final native int GetCharacterPlacementA(int hdc, byte[] lpString, int nCount, int nMaxExtent, GCP_RESULTS lpResults, int dwFlags);
public static final native boolean GetCharWidthA (int hdc, int iFirstChar, int iLastChar, int [] lpBuffer);
public static final native boolean GetCharWidthW (int hdc, int iFirstChar, int iLastChar, int [] lpBuffer);
public static final native boolean GetClassInfoW (int hInstance, char [] lpClassName, WNDCLASS lpWndClass);
public static final native boolean GetClassInfoA (int hInstance, byte [] lpClassName, WNDCLASS lpWndClass);
public static final native boolean GetClientRect (int hWnd, RECT lpRect);
public static final native int GetClipboardData (int uFormat);
public static final native int GetClipboardFormatNameA (int format, byte[] lpszFormatName, int cchMaxCount);
public static final native int GetClipboardFormatNameW (int format, char[] lpszFormatName, int cchMaxCount);
public static final native int GetClipBox (int hdc, RECT lprc);
public static final native int GetClipRgn (int hdc, int hrgn);
public static final native boolean GetComboBoxInfo (int hwndCombo, COMBOBOXINFO pcbi);
public static final native int GetCurrentObject (int hdc, int uObjectType);
public static final native int GetCurrentProcessId ();
public static final native int GetCurrentThreadId ();
public static final native int GetCursor ();
public static final native boolean GetCursorPos (POINT lpPoint);
public static final native int GetDC (int hwnd);
public static final native int GetDCEx (int hWnd, int hrgnClip, int flags);
public static final native int GetDesktopWindow ();
public static final native int GetDeviceCaps (int hdc, int nIndex);
public static final native int GetDialogBaseUnits ();
public static final native int GetDIBColorTable (int hdc, int uStartIndex, int cEntries, byte[] pColors);
public static final native int GetDIBits (int hdc, int hbmp, int uStartScan, int cScanLines, int lpvBits, byte[] lpbi, int uUsage);
public static final native int GetDlgItem (int hDlg, int nIDDlgItem);
public static final native int GetDoubleClickTime ();
public static final native int GetFocus ();
public static final native int GetFontLanguageInfo(int hdc);
public static final native boolean GetIconInfo (int hIcon, ICONINFO piconinfo);
public static final native int GetKeyboardLayoutList(int nBuff, int[] lpList);
public static final native int GetKeyboardLayout(int idThread);public static final native short GetKeyState (int nVirtKey);
public static final native boolean GetKeyboardState (byte [] lpKeyState);
public static final native int GetKeyNameTextW(int lParam, char [] lpString, int nSize);
public static final native int GetKeyNameTextA(int lParam, byte [] lpString, int nSize);
public static final native int GetLastActivePopup (int hWnd);
public static final native int GetLastError ();
public static final native int GetLayout (int hdc);
/* returns the instance handle to the swt library */
public static final native int GetLibraryHandle ();
public static final native int GetLocaleInfoW (int Locale, int LCType, char [] lpLCData, int cchData);
public static final native int GetLocaleInfoA (int Locale, int LCType, byte [] lpLCData, int cchData);
public static final native int GetMenu (int hWnd);
public static final native int GetMenuDefaultItem (int hMenu, int fByPos, int gmdiFlags);
public static final native boolean GetMenuInfo (int hmenu, MENUINFO lpcmi);
public static final native int GetMenuItemCount (int hMenu);
public static final native boolean GetMenuItemInfoW (int hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii);
public static final native boolean GetMenuItemInfoA (int hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii);
public static final native boolean GetMessageW (MSG lpMsg, int hWnd, int wMsgFilterMin, int wMsgFilterMax);
public static final native boolean GetMessageA (MSG lpMsg, int hWnd, int wMsgFilterMin, int wMsgFilterMax);
public static final native int GetMessagePos ();
public static final native int GetMessageTime ();
public static final native int GetTextCharset(int hdc);
public static final native int GetTickCount ();
public static final native int GetModuleHandleW (char [] lpModuleName);
public static final native int GetModuleHandleA (byte [] lpModuleName);
public static final native boolean GetMonitorInfoW (int hmonitor, MONITORINFO lpmi);
public static final native boolean GetMonitorInfoA (int hmonitor, MONITORINFO lpmi);
public static final native int GetNearestPaletteIndex(int hPal, int crColor);
public static final native int GetObjectA (int hgdiobj, int cbBuffer, BITMAP lpvObject);
public static final native int GetObjectW (int hgdiobj, int cbBuffer, BITMAP lpvObject);
public static final native int GetObjectA (int hgdiobj, int cbBuffer, DIBSECTION lpvObject);
public static final native int GetObjectW (int hgdiobj, int cbBuffer, DIBSECTION lpvObject);
public static final native int GetObjectA (int hgdiobj, int cbBuffer, LOGBRUSH lpvObject);
public static final native int GetObjectW (int hgdiobj, int cbBuffer, LOGBRUSH lpvObject);
public static final native int GetObjectA (int hgdiobj, int cbBuffer, LOGFONTA lpvObject);
public static final native int GetObjectW (int hgdiobj, int cbBuffer, LOGFONTW lpvObject);
public static final native int GetObjectA (int hgdiobj, int cbBuffer, LOGPEN lpvObject);
public static final native int GetObjectW (int hgdiobj, int cbBuffer, LOGPEN lpvObject);
public static final native boolean GetOpenFileNameW (OPENFILENAME lpofn);
public static final native boolean GetOpenFileNameA (OPENFILENAME lpofn);
public static final native int GetPaletteEntries (int hPalette, int iStartIndex, int nEntries, byte[] logPalette);
public static final native int GetParent (int hWnd);
public static final native int GetPixel (int hdc, int x, int y);
public static final native int GetProcAddress (int hModule, byte [] lpProcName);
public static final native int GetProcessHeap ();
public static final native int GetProfileStringW (char [] lpAppName, char [] lpKeyName, char [] lpDefault, char [] lpReturnedString, int nSize);
public static final native int GetProfileStringA (byte [] lpAppName, byte [] lpKeyName, byte [] lpDefault, byte [] lpReturnedString, int nSize);
public static final native int GetRegionData (int hRgn, int dwCount, int [] lpRgnData);
public static final native int GetRgnBox (int hrgn, RECT lprc);
public static final native int GetROP2 (int hdc);
public static final native boolean GetSaveFileNameW (OPENFILENAME lpofn);
public static final native boolean GetSaveFileNameA (OPENFILENAME lpofn);
public static final native boolean GetScrollInfo (int hwnd, int flags, SCROLLINFO info);
public static final native int GetStockObject (int fnObject);
public static final native int GetSysColor (int nIndex);
public static final native int GetSysColorBrush (int nIndex);
public static final native int GetSystemMenu (int hWnd, boolean bRevert);
public static final native int GetSystemMetrics (int nIndex);
public static final native int GetSystemPaletteEntries(int hdc, int iStartIndex, int nEntries, byte[] lppe);
public static final native int GetTextColor (int hDC);
public static final native boolean GetTextExtentPoint32W (int hdc, char [] lpString, int cbString, SIZE lpSize);
public static final native boolean GetTextExtentPoint32A (int hdc, byte [] lpString, int cbString, SIZE lpSize);
public static final native boolean GetTextMetricsW (int hdc, TEXTMETRICW lptm);
public static final native boolean GetTextMetricsA (int hdc, TEXTMETRICA lptm);
public static final native boolean GetUpdateRect (int hWnd, RECT lpRect, boolean bErase);
public static final native int GetUpdateRgn (int hWnd, int hRgn, boolean bErase);
public static final native boolean GetVersionExW (OSVERSIONINFOW lpVersionInfo);
public static final native boolean GetVersionExA (OSVERSIONINFOA lpVersionInfo);
public static final native int GetWindow (int hWnd, int uCmd);
public static final native int GetWindowLongW (int hWnd, int nIndex);
public static final native int GetWindowLongA (int hWnd, int nIndex);
public static final native boolean GetWindowPlacement (int hWnd, WINDOWPLACEMENT lpwndpl);
public static final native boolean GetWindowRect (int hWnd, RECT lpRect);
public static final native int GetWindowRgn(int hWnd, int hRgn);
public static final native int GetWindowTextW (int hWnd, char [] lpString, int nMaxCount);
public static final native int GetWindowTextA (int hWnd, byte [] lpString, int nMaxCount);
public static final native int GetWindowTextLengthW (int hWnd);
public static final native int GetWindowTextLengthA (int hWnd);
public static final native int GetWindowThreadProcessId (int hWnd, int [] lpdwProcessId);
public static final native int GlobalAlloc (int uFlags, int dwBytes);
public static final native int GlobalFree (int hMem);
public static final native int GlobalLock (int hMem);
public static final native int GlobalSize (int hMem);
public static final native boolean GlobalUnlock (int hMem);
public static final native boolean GradientFill(int hdc, int pVertex, int dwNumVertex, int pMesh, int dwNumMesh, int dwMode);
public static final native int HeapAlloc (int hHeap, int dwFlags, int dwBytes);
public static final native boolean HeapFree (int hHeap, int dwFlags, int lpMem);
public static final native boolean HideCaret (int hWnd);
public static final native int ImageList_Add (int himl, int hbmImage, int hbmMask);
public static final native int ImageList_AddMasked (int himl, int hbmImage, int crMask);
public static final native int ImageList_Create (int cx, int cy, int flags, int cInitial, int cGrow);
public static final native boolean ImageList_Destroy (int himl);
public static final native int ImageList_GetIcon (int himl, int i, int flags);
public static final native boolean ImageList_GetIconSize (int himl, int [] cx, int [] cy);   
public static final native int ImageList_GetImageCount (int himl);
public static final native boolean ImageList_Remove (int himl, int i);
public static final native boolean ImageList_Replace (int himl, int i, int hbmImage, int hbmMask);
public static final native int ImageList_ReplaceIcon (int himl, int i, int hicon);
public static final native boolean ImageList_SetIconSize (int himl, int cx, int cy);
public static final native int ImmAssociateContext (int hWnd, int hIMC);
public static final native int ImmCreateContext ();
public static final native boolean ImmDestroyContext (int hIMC);
public static final native boolean ImmGetCompositionFontW (int hIMC, LOGFONTW lplf);
public static final native boolean ImmGetCompositionFontA (int hIMC, LOGFONTA lplf);
public static final native int ImmGetCompositionStringW (int hIMC, int dwIndex, char [] lpBuf, int dwBufLen);
public static final native int ImmGetCompositionStringA (int hIMC, int dwIndex, byte [] lpBuf, int dwBufLen);
public static final native int ImmGetContext (int hWnd);
public static final native boolean ImmGetConversionStatus (int hIMC, int [] lpfdwConversion, int [] lpfdwSentence);
public static final native int ImmGetDefaultIMEWnd (int hWnd);
public static final native boolean ImmGetOpenStatus (int hIMC);
public static final native boolean ImmReleaseContext (int hWnd, int hIMC);
public static final native boolean ImmSetCompositionFontW (int hIMC, LOGFONTW lplf);
public static final native boolean ImmSetCompositionFontA (int hIMC, LOGFONTA lplf);
public static final native boolean ImmSetCompositionWindow (int hIMC, COMPOSITIONFORM lpCompForm);
public static final native boolean ImmSetConversionStatus (int hIMC, int fdwConversion, int dwSentence);
public static final native boolean ImmSetOpenStatus (int hIMC, boolean fOpen);
public static final native void InitCommonControls ();
public static final native boolean InitCommonControlsEx (INITCOMMONCONTROLSEX lpInitCtrls);
public static final native boolean InsertMenuW (int hMenu, int uPosition, int uFlags, int uIDNewItem, char [] lpNewItem);
public static final native boolean InsertMenuA (int hMenu, int uPosition, int uFlags, int uIDNewItem, byte [] lpNewItem);
public static final native boolean InsertMenuItemW (int hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii);
public static final native boolean InsertMenuItemA (int hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii);
public static final native boolean InvalidateRect(int hWnd, RECT lpRect, boolean bErase);
public static final native boolean InvalidateRgn (int hWnd, int hRgn, boolean bErase);
public static final native boolean IsDBCSLeadByte (byte TestChar);
public static final native boolean IsIconic (int hWnd);
public static final native boolean IsPPC();
public static final native boolean IsSP();
public static final native boolean IsWindowEnabled (int hWnd);
public static final native boolean IsWindowVisible (int hWnd);
public static final native boolean IsZoomed (int hWnd);
public static final native boolean KillTimer (int hWnd, int uIDEvent);
public static final native boolean LineTo (int hdc,int x1, int x2);
public static final native int LoadBitmapW (int hInstance, int lpBitmapName);
public static final native int LoadBitmapA (int hInstance, int lpBitmapName);
public static final native int LoadCursorW (int hInstance, int lpCursorName);
public static final native int LoadCursorA (int hInstance, int lpCursorName);
public static final native int LoadIconW (int hInstance, int lpIconName);
public static final native int LoadIconA (int hInstance, int lpIconName);
public static final native int LoadImageW (int hinst, char [] lpszName, int uType, int cxDesired, int cyDesired, int fuLoad);
public static final native int LoadImageA (int hinst, byte [] lpszName, int uType, int cxDesired, int cyDesired, int fuLoad);
public static final native int LoadLibraryW (char [] lpLibFileName);
public static final native int LoadLibraryA (byte [] lpLibFileName);
public static final native int MapVirtualKeyW (int uCode, int uMapType);
public static final native int MapVirtualKeyA (int uCode, int uMapType);
public static final native int MapWindowPoints (int hWndFrom, int hWndTo, POINT lpPoints, int cPoints);
public static final native int MapWindowPoints (int hWndFrom, int hWndTo, RECT lpPoints, int cPoints);
public static final native boolean MessageBeep (int uType);
public static final native int MessageBoxW (int hWnd, char [] lpText, char [] lpCaption, int uType);
public static final native int MessageBoxA (int hWnd, byte [] lpText, byte [] lpCaption, int uType);
public static final native int MonitorFromWindow (int hwnd, int dwFlags);
public static final native void MoveMemory (char[] Destination, int SourcePtr, int Length);
public static final native void MoveMemory (byte [] Destination, int Source, int Length);
public static final native void MoveMemory (byte [] Destination, ACCEL Source, int Length);
public static final native void MoveMemory (byte [] Destination, BITMAPINFOHEADER Source, int Length);
public static final native void MoveMemory (int [] Destination, int Source, int Length);
public static final native void MoveMemory (int Destination, byte [] Source, int Length);
public static final native void MoveMemory (int Destination, char [] Source, int Length);
public static final native void MoveMemory (int Destination, int [] Source, int Length);
public static final native void MoveMemory (int Destination, GRADIENT_RECT Source, int Length);
public static final native void MoveMemory (int Destination, LOGFONTW Source, int Length);
public static final native void MoveMemory (int Destination, LOGFONTA Source, int Length);
public static final native void MoveMemory (int Destination, MEASUREITEMSTRUCT Source, int Length);
public static final native void MoveMemory (int Destination, MSG Source, int Length);
public static final native void MoveMemory (int Destination, NMTTDISPINFOW Source, int Length);
public static final native void MoveMemory (int Destination, NMTTDISPINFOA Source, int Length);
public static final native void MoveMemory (int Destination, RECT Source, int Length);
public static final native void MoveMemory (int Destination, TRIVERTEX Source, int Length);
public static final native void MoveMemory (int Destination, WINDOWPOS Source, int Length);
public static final native void MoveMemory (BITMAPINFOHEADER Destination, byte [] Source, int Length);
public static final native void MoveMemory (DRAWITEMSTRUCT Destination, int Source, int Length);
public static final native void MoveMemory (HDITEM Destination, int Source, int Length);
public static final native void MoveMemory (HELPINFO Destination, int Source, int Length);
public static final native void MoveMemory (LOGFONTW Destination, int Source, int Length);
public static final native void MoveMemory (LOGFONTA Destination, int Source, int Length);
public static final native void MoveMemory (MEASUREITEMSTRUCT Destination, int Source, int Length);
public static final native void MoveMemory (NMHDR Destination, int Source, int Length);
public static final native void MoveMemory (NMRGINFO Destination, int Source, int Length);
public static final native void MoveMemory (NMCUSTOMDRAW Destination, int Source, int Length);
public static final native void MoveMemory (NMLVCUSTOMDRAW Destination, int Source, int Length);
public static final native void MoveMemory (NMTVCUSTOMDRAW Destination, int Source, int Length);
public static final native void MoveMemory (int Destination, NMLVCUSTOMDRAW Source, int Length);
public static final native void MoveMemory (int Destination, NMTVCUSTOMDRAW Source, int Length);
public static final native void MoveMemory (int Destination, NMLVDISPINFO Source, int Length);
public static final native void MoveMemory (NMLVDISPINFO Destination, int Source, int Length);
public static final native void MoveMemory (NMHEADER Destination, int Source, int Length);
public static final native void MoveMemory (NMLISTVIEW Destination, int Source, int Length);
public static final native void MoveMemory (NMREBARCHEVRON Destination, int Source, int Length);
public static final native void MoveMemory (NMTOOLBAR Destination, int Source, int Length);
public static final native void MoveMemory (NMTTDISPINFOW Destination, int Source, int Length);
public static final native void MoveMemory (NMTTDISPINFOA Destination, int Source, int Length);
public static final native void MoveMemory (TVITEM Destination, int Source, int Length);
public static final native void MoveMemory (WINDOWPOS Destination, int Source, int Length);
public static final native void MoveMemory (MSG Destination, int Source, int Length);
public static final native void MoveMemory(int Destination, DROPFILES Source, int Length);
public static final native void MoveMemory(double[] Destination, int SourcePtr, int Length);
public static final native void MoveMemory(float[] Destination, int SourcePtr, int Length);
public static final native void MoveMemory(short[] Destination, int SourcePtr, int Length);
public static final native void MoveMemory(int DestinationPtr, double[] Source, int Length);
public static final native void MoveMemory(int DestinationPtr, float[] Source, int Length);
public static final native void MoveMemory(int DestinationPtr, short[] Source, int Length);
public static final native boolean MoveToEx (int hdc,int x1, int x2, int lPoint);
public static final native int MsgWaitForMultipleObjectsEx (int nCount, int pHandles, int dwMilliseconds, int dwWakeMask, int dwFlags);
public static final native int MultiByteToWideChar (int CodePage, int dwFlags, byte [] lpMultiByteStr, int cchMultiByte, char [] lpWideCharStr, int cchWideChar);
public static final native int MultiByteToWideChar (int CodePage, int dwFlags, int lpMultiByteStr, int cchMultiByte, char [] lpWideCharStr, int cchWideChar);
public static final native int OleInitialize (int pvReserved);
public static final native void OleUninitialize ();
public static final native boolean OpenClipboard (int hWndNewOwner);
public static final native boolean PatBlt (int hdc,int x1, int x2,int w, int h, int rop);
public static final native boolean PeekMessageW (MSG lpMsg, int hWnd, int wMsgFilterMin, int wMsgFilterMax, int wRemoveMsg);
public static final native boolean PeekMessageA (MSG lpMsg, int hWnd, int wMsgFilterMin, int wMsgFilterMax, int wRemoveMsg);
public static final native boolean Pie (int hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect, int nXStartArc, int nYStartArc, int nXEndArc, int nYEndArc);
public static final native boolean Polygon (int hdc, int [] points, int nPoints);
public static final native boolean Polyline (int hdc,int[] points, int nPoints);
public static final native boolean PostMessageW (int hWnd, int Msg, int wParam, int lParam);
public static final native boolean PostMessageA (int hWnd, int Msg, int wParam, int lParam);
public static final native boolean PostThreadMessageW (int idThread, int Msg, int wParam, int lParam);
public static final native boolean PostThreadMessageA (int idThread, int Msg, int wParam, int lParam);
public static final native boolean PrintDlgW (PRINTDLG lppd);
public static final native boolean PrintDlgA (PRINTDLG lppd);
public static final native boolean PtInRect (RECT rect, POINT pt);
public static final native boolean PtInRegion (int hrgn, int X, int Y);
public static final native int RealizePalette(int hDC);
public static final native boolean Rectangle (int hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect);
public static final native boolean RectInRegion (int hrgn, RECT lprc);
public static final native boolean RedrawWindow (int hWnd, RECT lprcUpdate, int hrgnUpdate, int flags);
public static final native int RegCloseKey (int hKey);
public static final native int RegisterClassW (WNDCLASS lpWndClass);
public static final native int RegisterClassA (WNDCLASS lpWndClass);
public static final native int RegEnumKeyExW (int hKey, int dwIndex, char [] lpName, int [] lpcName, int [] lpReserved, char [] lpClass, int [] lpcClass, FILETIME lpftLastWriteTime);
public static final native int RegisterClipboardFormatA (byte[] lpszFormat); 
public static final native int RegisterClipboardFormatW (char[] lpszFormat); 
public static final native int RegOpenKeyExW (int hKey, char[] lpSubKey, int ulOptions, int samDesired, int[] phkResult);
public static final native int RegQueryInfoKeyW (int hKey, int lpClass, int[] lpcbClass, int lpReserved, int[] lpSubKeys, int[] lpcbMaxSubKeyLen, int[] lpcbMaxClassLen, int[] lpcValues, int[] lpcbMaxValueNameLen, int[] lpcbMaxValueLen, int[] lpcbSecurityDescriptor, int lpftLastWriteTime);
public static final native int RegQueryValueExW (int hKey, char[] lpValueName, int lpReserved, int[] lpType, char [] lpData, int[] lpcbData);
public static final native int RegEnumKeyExA (int hKey, int dwIndex, byte [] lpName, int [] lpcName, int [] lpReserved, byte [] lpClass, int [] lpcClass, FILETIME lpftLastWriteTime);
public static final native int RegOpenKeyExA (int hKey, byte[] lpSubKey, int ulOptions, int samDesired, int[] phkResult);
public static final native int RegQueryInfoKeyA (int hKey, int lpClass, int[] lpcbClass, int lpReserved, int[] lpSubKeys, int[] lpcbMaxSubKeyLen, int[] lpcbMaxClassLen, int[] lpcValues, int[] lpcbMaxValueNameLen, int[] lpcbMaxValueLen, int[] lpcbSecurityDescriptor, int lpftLastWriteTime);
public static final native int RegQueryValueExA (int hKey, byte[] lpValueName, int lpReserved, int[] lpType, byte [] lpData, int[] lpcbData);
public static final native boolean ReleaseCapture ();
public static final native int ReleaseDC (int hWnd, int hDC);
public static final native boolean RemoveMenu (int hMenu, int uPosition, int uFlags);
public static final native boolean RoundRect (int hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect, int nWidth, int nHeight);
public static final native boolean ScreenToClient (int hWnd, POINT lpPoint);
public static final native int ScrollWindowEx (int hWnd, int dx, int dy, RECT prcScroll, RECT prcClip, int hrgnUpdate, RECT prcUpdate, int flags);
public static final native  int SelectClipRgn (int hdc, int hrgn);
public static final native int SelectObject(int hDC, int HGDIObj);
public static final native int SelectPalette(int hDC, int hpal, boolean bForceBackground);
public static final native int SendMessageW (int hWnd, int Msg, int [] wParam, int [] lParam);
public static final native int SendMessageW (int hWnd, int Msg, int [] wParam, int lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, char [] lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, int [] lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, short [] lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, int lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, LVCOLUMN lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, LVHITTESTINFO lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, LVITEM lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, REBARBANDINFO lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, RECT lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, TBBUTTON lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, TBBUTTONINFO lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, TCITEM lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, TOOLINFO lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, TVHITTESTINFO lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, TVINSERTSTRUCT lParam);
public static final native int SendMessageW (int hWnd, int Msg, int wParam, TVITEM lParam);
public static final native int SendMessageA (int hWnd, int Msg, int [] wParam, int [] lParam);
public static final native int SendMessageA (int hWnd, int Msg, int [] wParam, int lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, byte [] lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, int [] lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, short [] lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, int lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, LVCOLUMN lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, LVHITTESTINFO lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, LVITEM lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, REBARBANDINFO lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, RECT lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, TBBUTTON lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, TBBUTTONINFO lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, TCITEM lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, TOOLINFO lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, TVHITTESTINFO lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, TVINSERTSTRUCT lParam);
public static final native int SendMessageA (int hWnd, int Msg, int wParam, TVITEM lParam);
public static final native int SetActiveWindow (int hWnd);
public static final native int SetBkColor (int hdc, int colorRef);
public static final native int SetBkMode (int hdc, int mode);
public static final native int SetCapture (int hWnd);
public static final native boolean SetCaretPos (int X, int Y);
public static final native int SetClipboardData (int uFormat, int hMem);
public static final native int SetCursor (int hCursor);
public static final native boolean SetCursorPos (int X, int Y);
public static final native int SetDIBColorTable (int hdc, int uStartIndex, int cEntries, byte[] pColors);
public static final native int SetErrorMode (int uMode);
public static final native int SetFocus (int hWnd);
public static final native boolean SetForegroundWindow (int hWnd);
public static final native int SetLayout (int hdc, int dwLayout);
public static final native boolean SetMenu (int hWnd, int hMenu);
public static final native boolean SetMenuDefaultItem (int hMenu, int uItem, int fByPos);
public static final native boolean SetMenuInfo (int hmenu, MENUINFO lpcmi);
public static final native boolean SetMenuItemInfoW (int hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii);
public static final native boolean SetMenuItemInfoA (int hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii);
public static final native int SetPaletteEntries (int hPal, int iStart, int cEntries, byte[] lppe);
public static final native int SetParent (int hWndChild, int hWndNewParent);
public static final native int SetPixel (int hdc, int X, int Y, int crColor);
public static final native boolean SetRect(RECT lprc, int xLeft, int yTop, int xRight, int yBottom);
public static final native boolean SetRectRgn (int hrgn, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect);
public static final native int SetROP2 (int hdc, int fnDrawMode);
public static final native boolean SetScrollInfo (int hwnd, int flags, SCROLLINFO info, boolean fRedraw);
public static final native int SetStretchBltMode(int hdc, int iStretchMode);
public static final native int SetTextAlign(int hdc, int fMode);
public static final native int SetTextColor (int hdc, int colorRef);
public static final native int SetTimer (int hWnd, int nIDEvent, int Elapse, int lpTimerFunc);
public static final native int SetWindowLongW (int hWnd, int nIndex, int dwNewLong);
public static final native int SetWindowLongA (int hWnd, int nIndex, int dwNewLong);
public static final native boolean SetWindowPlacement (int hWnd, WINDOWPLACEMENT lpwndpl);
public static final native boolean SetWindowPos(int hWnd, int hWndInsertAfter, int X, int Y, int cx, int cy, int uFlags);
public static final native int SetWindowRgn(int hWnd, int hRgn, boolean bRedraw);
public static final native boolean SetWindowTextW (int hWnd, char [] lpString);
public static final native boolean SetWindowTextA (int hWnd, byte [] lpString);
public static final native int SetWindowsHookExW (int idHook, int lpfn,  int hMod,  int dwThreadId);
public static final native int SetWindowsHookExA (int idHook, int lpfn,  int hMod,  int dwThreadId);
public static final native int SHBrowseForFolderW (BROWSEINFO lpbi);
public static final native int SHBrowseForFolderA (BROWSEINFO lpbi);
public static final native boolean SHCreateMenuBar(SHMENUBARINFO pmb);
public static final native boolean SHHandleWMSettingChange (int hwnd, int wParam, int lParam, SHACTIVATEINFO psai);
public static final native int SHRecognizeGesture(SHRGINFO shrg);
public static final native void SHSendBackToFocusWindow (int uMsg, int wp, int lp);
public static final native boolean SHSipPreference (int hwnd, int st);
public static final native boolean ShellExecuteExW (SHELLEXECUTEINFO lpExecInfo);
public static final native boolean ShellExecuteExA (SHELLEXECUTEINFO lpExecInfo);
public static final native int SHGetMalloc (int [] ppMalloc);
public static final native boolean SHGetPathFromIDListW (int pidl, char [] pszPath);
public static final native boolean SHGetPathFromIDListA (int pidl, byte [] pszPath);
public static final native boolean SHSetAppKeyWndAssoc(byte bVk, int hwnd);
public static final native boolean ShowCaret (int hWnd);
public static final native boolean ShowOwnedPopups (int hWnd, boolean fShow);
public static final native boolean ShowScrollBar (int hWnd, int wBar, boolean bShow);
public static final native boolean ShowWindow (int hWnd, int nCmdShow);
public static final native boolean SipGetInfo (SIPINFO pSipInfo);
public static final native int StartDocW (int hdc, DOCINFO lpdi);
public static final native int StartDocA (int hdc, DOCINFO lpdi);
public static final native int StartPage (int hdc);
public static final native boolean StretchBlt (int hdcDest, int nXOriginDest, int nYOriginDest, int nWidthDest, int nHeightDest, int hdcSrc, int nXOriginSrc, int nYOriginSrc, int nWidthSrc, int nHeightSrc, int dwRop);
public static final native boolean SystemParametersInfoW (int uiAction, int uiParam, RECT pvParam, int fWinIni);
public static final native boolean SystemParametersInfoA (int uiAction, int uiParam, RECT pvParam, int fWinIni);
public static final native boolean SystemParametersInfoW (int uiAction, int uiParam, NONCLIENTMETRICSW pvParam, int fWinIni);
public static final native boolean SystemParametersInfoA (int uiAction, int uiParam, NONCLIENTMETRICSA pvParam, int fWinIni);
public static final native boolean SystemParametersInfoW (int uiAction, int uiParam, int[] pvParam, int fWinIni);
public static final native boolean SystemParametersInfoA (int uiAction, int uiParam, int[] pvParam, int fWinIni);
public static final native int ToAscii (int uVirtKey, int uScanCode, byte [] lpKeyState, short [] lpChar, int uFlags);
public static final native int ToUnicode(int wVirtKey, int wScanCode, byte [] lpKeyState, char [] pwszBuff, int cchBuff, int wFlags);
public static final native boolean TrackMouseEvent(TRACKMOUSEEVENT lpEventTrack);
public static final native boolean TrackPopupMenu (int hMenu, int uFlags, int x, int y, int nReserved, int hWnd, RECT prcRect);
public static final native int TranslateAcceleratorW (int hWnd, int hAccTable, MSG lpMsg);
public static final native int TranslateAcceleratorA (int hWnd, int hAccTable, MSG lpMsg);
public static final native boolean TranslateCharsetInfo(int lpSrc, int [] lpCs, int dwFlags);
public static final native boolean TranslateMDISysAccel (int hWndClient, MSG lpMsg);
public static final native boolean TranslateMessage (MSG lpmsg);
public static final native boolean TransparentImage (int hdcDest, int DstX, int DstY, int DstCx, int DstCy,int hSrc, int SrcX, int SrcY, int SrcCx, int SrcCy, int TransparentColor);public static final native boolean UnhookWindowsHookEx(int hhk);
public static final native boolean UnregisterClassW (char [] lpClassName, int hInstance);
public static final native boolean UnregisterClassA (byte [] lpClassName, int hInstance);
public static final native boolean UpdateWindow (int hWnd);
public static final native boolean ValidateRect (int hWnd, RECT lpRect);
public static final native short VkKeyScanW (short ch);
public static final native short VkKeyScanA (short ch);
public static final native int VtblCall (int ppVtbl, int fnNumber, int arg0);
public static final native boolean WaitMessage ();
public static final native int WideCharToMultiByte (int CodePage, int dwFlags, char [] lpWideCharStr, int cchWideChar, byte [] lpMultiByteStr, int cchMultiByte, byte [] lpDefaultChar, boolean [] lpUsedDefaultChar);
public static final native int WideCharToMultiByte (int CodePage, int dwFlags, char [] lpWideCharStr, int cchWideChar, int lpMultiByteStr, int cchMultiByte, byte [] lpDefaultChar, boolean [] lpUsedDefaultChar);
public static final native int WindowFromDC (int lpPoint);
public static final native int WindowFromPoint (POINT lpPoint);
}
