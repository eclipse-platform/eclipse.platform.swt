/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Martin Karpisek <martin.karpisek@gmail.com> - Bug 443250
 *******************************************************************************/
package org.eclipse.swt.internal.win32;


import java.util.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.widgets.*;

public class OS extends C {
	static {
		Library.loadLibrary ("swt"); //$NON-NLS-1$
	}

	/*
	* SWT Windows flags
	*/
	public static final boolean IsDBLocale;
	/**
	 * Always reports the correct build number, regardless of manifest and
	 * compatibility GUIDs. Note that build number alone is sufficient to
	 * identify Windows version.
	 */
	public static final int WIN32_BUILD;
	/**
	 * Values taken from https://en.wikipedia.org/wiki/List_of_Microsoft_Windows_versions
	 */
	public static final int WIN32_BUILD_WIN10_1809 = 17763; // "Windows 10 October 2018 Update"
	public static final int WIN32_BUILD_WIN10_2004 = 19041; // "Windows 10 May 2020 Update"
	public static final int WIN32_BUILD_WIN11_21H2 = 22000; // Initial Windows 11 release

	public static final String NO_MANIFEST = "org.eclipse.swt.internal.win32.OS.NO_MANIFEST";

	/* Forward references */
	public static final int ACTCTX_FLAG_RESOURCE_NAME_VALID = 0x00000008;
	public static final int ACTCTX_FLAG_SET_PROCESS_DEFAULT = 0x00000010;
	public static final int ACTCTX_FLAG_HMODULE_VALID = 0x00000080;
	public static final int MANIFEST_RESOURCE_ID = 2;
	public static final int SM_IMMENABLED = 0x52;

	static {
		/*
		 * Starting with Windows 10, GetVersionEx() lies about version unless
		 * application manifest has a proper entry. RtlGetVersion() always
		 * reports true version.
		 */
		OSVERSIONINFOEX osVersionInfoEx = new OSVERSIONINFOEX ();
		osVersionInfoEx.dwOSVersionInfoSize = OSVERSIONINFOEX.sizeof;
		if (0 == OS.RtlGetVersion (osVersionInfoEx)) {
			WIN32_BUILD = osVersionInfoEx.dwBuildNumber;
		} else {
			System.err.println ("SWT: OS: Failed to detect Windows build number");
			WIN32_BUILD = 0;
		}

		/* Load the manifest to force the XP Theme */
		if (System.getProperty (NO_MANIFEST) == null) {
			ACTCTX pActCtx = new ACTCTX ();
			pActCtx.cbSize = ACTCTX.sizeof;
			pActCtx.dwFlags = ACTCTX_FLAG_RESOURCE_NAME_VALID | ACTCTX_FLAG_HMODULE_VALID | ACTCTX_FLAG_SET_PROCESS_DEFAULT;
			pActCtx.hModule = OS.GetLibraryHandle ();
			pActCtx.lpResourceName = MANIFEST_RESOURCE_ID;
			long hActCtx = OS.CreateActCtx (pActCtx);
			long [] lpCookie = new long [1];
			OS.ActivateActCtx (hActCtx, lpCookie);
			/*
			* NOTE:  A single activation context is created and activated
			* for the entire lifetime of the program.  It is deactivated
			* and released by Windows when the program exits.
			*/
		}

		/* Make the process DPI aware for Windows Vista */
		OS.SetProcessDPIAware ();

		/* Get the DBCS flag */
		IsDBLocale = OS.GetSystemMetrics (SM_IMMENABLED) != 0;
	}

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
	public static final int ABS_RIGHTNORMAL = 13;
	public static final int ABS_RIGHTPRESSED = 15;
	public static final int ABS_UPDISABLED = 4;
	public static final int ABS_UPHOT = 2;
	public static final int ABS_UPNORMAL = 1;
	public static final int ABS_UPPRESSED = 3;
	public static final int AC_SRC_OVER = 0;
	public static final int AC_SRC_ALPHA = 1;
	public static final int ALTERNATE = 1;
	public static final int ASSOCF_NOTRUNCATE = 0x00000020;
	public static final int ASSOCF_INIT_IGNOREUNKNOWN = 0x400;
	public static final int ASSOCSTR_COMMAND = 1;
	public static final int ASSOCSTR_DEFAULTICON = 15;
	public static final int ASSOCSTR_FRIENDLYAPPNAME = 4;
	public static final int ASSOCSTR_FRIENDLYDOCNAME = 3;
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
	public static final int BDR_SUNKENINNER = 0x0008;
	public static final int BF_LEFT = 0x0001;
	public static final int BF_TOP = 0x0002;
	public static final int BF_RIGHT = 0x0004;
	public static final int BF_BOTTOM = 0x0008;
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
	public static final int CCHDEVICENAME = 32;
	public static final int CCHFORMNAME = 32;
	public static final int CCHILDREN_SCROLLBAR = 5;
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
	public static final int CDIS_HOT = 0x0040;
	public static final int CDIS_MARKED = 0x0080;
	public static final int CDIS_INDETERMINATE = 0x0100;
	public static final int CDIS_SHOWKEYBOARDCUES = 0x0200;
	public static final int CDIS_DROPHILITED = 0x1000;
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
	public static final int CFS_RECT = 0x1;
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
	public static final int COLORONCOLOR = 0x3;
	public static final int COLOR_3DDKSHADOW = 0x15;
	public static final int COLOR_3DFACE = 0xf;
	public static final int COLOR_3DHIGHLIGHT = 0x14;
	public static final int COLOR_3DHILIGHT = 0x14;
	public static final int COLOR_3DLIGHT = 0x16;
	public static final int COLOR_3DSHADOW = 0x10;
	public static final int COLOR_ACTIVECAPTION = 0x2;
	public static final int COLOR_BTNFACE = 0xf;
	public static final int COLOR_BTNHIGHLIGHT = 0x14;
	public static final int COLOR_BTNSHADOW = 0x10;
	public static final int COLOR_BTNTEXT = 0x12;
	public static final int COLOR_CAPTIONTEXT = 0x9;
	public static final int COLOR_GRADIENTACTIVECAPTION = 0x1b;
	public static final int COLOR_GRADIENTINACTIVECAPTION = 0x1c;
	public static final int COLOR_GRAYTEXT = 0x11;
	public static final int COLOR_HIGHLIGHT = 0xd;
	public static final int COLOR_HIGHLIGHTTEXT = 0xe;
	public static final int COLOR_HOTLIGHT = 26;
	public static final int COLOR_INACTIVECAPTION = 0x3;
	public static final int COLOR_INACTIVECAPTIONTEXT = 0x13;
	public static final int COLOR_INFOBK = 0x18;
	public static final int COLOR_INFOTEXT = 0x17;
	public static final int COLOR_MENU = 0x4;
	public static final int COLOR_MENUTEXT = 0x7;
	public static final int COLOR_SCROLLBAR = 0x0;
	public static final int COLOR_WINDOW = 0x5;
	public static final int COLOR_WINDOWFRAME = 0x6;
	public static final int COLOR_WINDOWTEXT = 0x8;
	public static final int COMPLEXREGION = 0x3;
	public static final int CP_ACP = 0x0;
	public static final int CP_UTF8 = 65001;
	public static final int CP_DROPDOWNBUTTON = 1;
	public static final int CPS_COMPLETE = 0x1;
	public static final int CS_DBLCLKS = 0x8;
	public static final int CS_DROPSHADOW = 0x20000;
	public static final int CS_GLOBALCLASS = 0x4000;
	public static final int CS_HREDRAW = 0x2;
	public static final int CS_VREDRAW = 0x1;
	public static final int CS_OWNDC = 0x20;
	public static final int CW_USEDEFAULT = 0x80000000;
	public static final int CWP_SKIPINVISIBLE = 0x0001;
	public static final String DATETIMEPICK_CLASS = "SysDateTimePick32"; //$NON-NLS-1$
	public static final int DC_BRUSH = 18;
	public static final int DCX_CACHE = 0x2;
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
	public static final int DSTINVERT = 0x550009;
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
	public static final int DTM_SETMCSTYLE = DTM_FIRST + 11;
	public static final int DTM_GETIDEALSIZE = DTM_FIRST + 15;
	public static final int DTM_SETFORMAT = DTM_FIRST + 50;
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
	public static final int EDGE_SUNKEN = 10;
	public static final int EDGE_ETCHED = 6;
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
	public static final short FADF_FIXEDSIZE = 0x10;
	public static final int FALT = 0x10;
	public static final int FCONTROL = 0x8;
	public static final int FE_FONTSMOOTHINGCLEARTYPE = 0x0002;
	public static final int FEATURE_DISABLE_NAVIGATION_SOUNDS = 21;
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
	public static final int FOS_OVERWRITEPROMPT = 0x2;
	public static final int FOS_NOCHANGEDIR = 0x8;
	public static final int FOS_PICKFOLDERS = 0x20;
	public static final int FOS_FORCEFILESYSTEM = 0x40;
	public static final int FOS_ALLOWMULTISELECT = 0x200;
	public static final int FOS_FILEMUSTEXIST = 0x1000;
	public static final int FR_PRIVATE = 0x10;
	public static final int FSHIFT = 0x4;
	public static final int FVIRTKEY = 0x1;
	public static final int GCP_REORDER = 0x0002;
	public static final int GCP_GLYPHSHAPE = 0x0010;
	public static final int GCP_CLASSIN = 0x00080000;
	public static final int GCP_LIGATE = 0x0020;
	public static final int GCS_COMPSTR = 0x8;
	public static final int GCS_RESULTSTR = 0x800;
	public static final int GCS_COMPATTR = 0x0010;
	public static final int GCS_COMPCLAUSE = 0x0020;
	public static final int GCS_CURSORPOS = 0x0080;
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
	public static final int GM_ADVANCED = 2;
	public static final int GMDI_USEDISABLED = 0x1;
	public static final int GMEM_FIXED = 0x0;
	public static final int GMEM_MOVEABLE = 0x2;
	public static final int GMEM_ZEROINIT = 0x40;
	public static final int GRADIENT_FILL_RECT_H = 0x0;
	public static final int GRADIENT_FILL_RECT_V = 0x1;
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
	public static final long HBMMENU_CALLBACK = -1;
	public static final int HCBT_ACTIVATE = 5;
	public static final int HCBT_CREATEWND = 3;
	public static final int HCF_HIGHCONTRASTON = 0x1;
	public static final int HDF_BITMAP = 0x2000;
	public static final int HDF_BITMAP_ON_RIGHT = 0x1000;
	public static final int HDF_CENTER = 2;
	public static final int HDF_JUSTIFYMASK = 0x3;
	public static final int HDF_IMAGE = 0x0800;
	public static final int HDF_LEFT = 0;
	public static final int HDF_OWNERDRAW = 0x8000;
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
	public static final int HDM_GETITEM = HDM_FIRST + 11;
	public static final int HDM_GETITEMRECT = HDM_FIRST + 7;
	public static final int HDM_GETORDERARRAY = HDM_FIRST + 17;
	public static final int HDM_HITTEST = HDM_FIRST + 6;
	public static final int HDM_INSERTITEM = HDM_FIRST + 10;
	public static final int HDM_LAYOUT = HDM_FIRST + 5;
	public static final int HDM_ORDERTOINDEX = HDM_FIRST + 15;
	public static final int HDM_SETIMAGELIST = HDM_FIRST + 8;
	public static final int HDM_SETITEM = HDM_FIRST + 12;
	public static final int HDM_SETORDERARRAY = HDM_FIRST + 18;
	public static final int HDN_FIRST = 0xfffffed4;
	public static final int HDN_BEGINDRAG = HDN_FIRST - 10;
	public static final int HDN_BEGINTRACK = 0xfffffeba;
	public static final int HDN_DIVIDERDBLCLICK = HDN_FIRST - 25;
	public static final int HDN_ENDDRAG = HDN_FIRST - 11;
	public static final int HDN_ITEMCHANGED = 0xfffffebf;
	public static final int HDN_ITEMCHANGING = HDN_FIRST - 20;
	public static final int HDN_ITEMCLICK = HDN_FIRST - 22;
	public static final int HDN_ITEMDBLCLICK = HDN_FIRST - 23;
	public static final int HDS_BUTTONS = 0x2;
	public static final int HDS_DRAGDROP = 0x0040;
	public static final int HDS_FULLDRAG = 0x80;
	public static final int HDS_HIDDEN = 0x8;
	public static final int HEAP_ZERO_MEMORY = 0x8;
	public static final int HELPINFO_MENUITEM = 0x2;
	public static final int HHT_ONDIVIDER = 0x4;
	public static final int HHT_ONDIVOPEN = 0x8;
	public static final int HICF_ARROWKEYS = 0x2;
	public static final int HICF_LEAVING = 0x20;
	public static final int HICF_MOUSE = 0x1;
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
	public static final int ICON_BIG = 0x1;
	public static final int ICON_SMALL = 0x0;
	public static final int I_IMAGECALLBACK = -1;
	public static final int I_IMAGENONE = -2;
	public static final int IDABORT = 0x3;
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
	public static final int IDCANCEL = 0x2;
	public static final int IDI_APPLICATION = 32512;
	public static final int IDIGNORE = 0x5;
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
	public static final int IMAGE_ICON = 0x1;
	public static final int IME_CMODE_FULLSHAPE = 0x8;
	public static final int IME_CMODE_KATAKANA = 0x2;
	public static final int IME_CMODE_NATIVE = 0x1;
	public static final int IME_CMODE_ROMAN = 0x10;
	public static final int IME_ESC_HANJA_MODE = 0x1008;
	public static final int IMEMOUSE_LDOWN = 1;
	public static final int INPUT_KEYBOARD = 1;
	public static final int INPUT_MOUSE = 0;
	public static final int INTERNET_MAX_URL_LENGTH = 2084;
	public static final int INTERNET_OPTION_END_BROWSER_SESSION = 42;
	public static final int KEY_QUERY_VALUE = 0x1;
	public static final int KEY_READ = 0x20019;
	public static final int KEY_WRITE = 0x20006;
	public static final int KEYEVENTF_EXTENDEDKEY = 0x0001;
	public static final int KEYEVENTF_KEYUP = 0x0002;
	public static final int KEYEVENTF_SCANCODE = 0x0008;
	public static final int L_MAX_URL_LENGTH = 2084;
	public static final int LANG_JAPANESE = 0x11;
	public static final int LANG_KOREAN = 0x12;
	public static final int LANG_NEUTRAL = 0x0;
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
	public static final int LB_SETANCHORINDEX = 0xf19c;
	public static final int LB_SETCARETINDEX = 0x19e;
	public static final int LB_SETCURSEL = 0x186;
	public static final int LB_SETHORIZONTALEXTENT = 0x194;
	public static final int LB_SETSEL = 0x185;
	public static final int LB_SETTOPINDEX = 0x197;
	public static final int LF_FACESIZE = 32;
	public static final int LGRPID_ARABIC = 0xd;
	public static final int LGRPID_HEBREW = 0xc;
	public static final int LGRPID_INSTALLED = 1;
	public static final int LIF_ITEMINDEX = 0x1;
	public static final int LIF_STATE = 0x2;
	public static final int LIM_SMALL = 0;
	public static final int LIS_FOCUSED = 0x1;
	public static final int LIS_ENABLED = 0x2;
	public static final int LISS_HOT = 0x2;
	public static final int LISS_SELECTED = 0x3;
	public static final int LISS_SELECTEDNOTFOCUS = 0x5;
	public static final int LM_GETIDEALSIZE = 0x701;
	public static final int LM_SETITEM = 0x702;
	public static final int LM_GETITEM = 0x703;
	public static final int LCID_SUPPORTED = 0x2;
	public static final int LOCALE_IDEFAULTANSICODEPAGE = 0x1004;
	public static final int LOCALE_SDECIMAL = 14;
	public static final int LOCALE_SISO3166CTRYNAME = 0x5a;
	public static final int LOCALE_SISO639LANGNAME = 0x59;
	public static final int LOCALE_STIMEFORMAT = 0x00001003;
	public static final int LOCALE_SYEARMONTH = 0x00001006;
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
	public static final int LVM_GETCOLUMN = 0x105f;
	public static final int LVM_GETCOLUMNORDERARRAY = LVM_FIRST + 59;
	public static final int LVM_GETCOLUMNWIDTH = 0x101d;
	public static final int LVM_GETCOUNTPERPAGE = 0x1028;
	public static final int LVM_GETEXTENDEDLISTVIEWSTYLE = 0x1037;
	public static final int LVM_GETHEADER = 0x101f;
	public static final int LVM_GETIMAGELIST = 0x1002;
	public static final int LVM_GETITEM = 0x104b;
	public static final int LVM_GETITEMCOUNT = 0x1004;
	public static final int LVM_GETITEMRECT = 0x100e;
	public static final int LVM_GETITEMSTATE = 0x102c;
	public static final int LVM_GETNEXTITEM = 0x100c;
	public static final int LVM_GETSELECTEDCOLUMN = LVM_FIRST + 174;
	public static final int LVM_GETSELECTEDCOUNT = 0x1032;
	public static final int LVM_GETSTRINGWIDTH = 0x1057;
	public static final int LVM_GETSUBITEMRECT = 0x1038;
	public static final int LVM_GETTEXTCOLOR = 0x1023;
	public static final int LVM_GETTOOLTIPS = 0x104e;
	public static final int LVM_GETTOPINDEX = 0x1027;
	public static final int LVM_HITTEST = 0x1012;
	public static final int LVM_INSERTCOLUMN = 0x1061;
	public static final int LVM_INSERTITEM = 0x104d;
	public static final int LVM_REDRAWITEMS = LVM_FIRST + 21;
	public static final int LVM_SCROLL = 0x1014;
	public static final int LVM_SETBKCOLOR = 0x1001;
	public static final int LVM_SETCALLBACKMASK = LVM_FIRST + 11;
	public static final int LVM_SETCOLUMN = 0x1060;
	public static final int LVM_SETCOLUMNORDERARRAY = LVM_FIRST + 58;
	public static final int LVM_SETCOLUMNWIDTH = 0x101e;
	public static final int LVM_SETEXTENDEDLISTVIEWSTYLE = 0x1036;
	public static final int LVM_SETIMAGELIST = 0x1003;
	public static final int LVM_SETINSERTMARK = LVM_FIRST + 166;
	public static final int LVM_SETITEM = 0x104c;
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
	public static final int LVN_GETDISPINFO = LVN_FIRST - 77;
	public static final int LVN_ITEMACTIVATE = 0xffffff8e;
	public static final int LVN_ITEMCHANGED = 0xffffff9b;
	public static final int LVN_MARQUEEBEGIN = 0xffffff64;
	public static final int LVN_ODFINDITEM = LVN_FIRST - 79;
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
	public static final int MAX_PATH = 260;
	public static final int MA_NOACTIVATE = 0x3;
	public static final int MAPVK_VSC_TO_VK = 1;
	public static final int MAPVK_VK_TO_CHAR = 2;
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
	public static final int MCS_WEEKNUMBERS = 0x0004;
	public static final int MDIS_ALLCHILDSTYLES = 0x0001;
	public static final int MDT_EFFECTIVE_DPI = 0;
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
	public static final int MOD_ALT     = 0x0001;
	public static final int MOD_CONTROL = 0x0002;
	public static final int MOD_SHIFT   = 0x0004;
	public static final int MONITOR_DEFAULTTOPRIMARY = 0x1;
	public static final int MONITOR_DEFAULTTONEAREST = 0x2;
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
	public static final int NOTIFYICONDATA_V2_SIZE = NOTIFYICONDATA_V2_SIZE ();
	public static final int NULLREGION = 0x1;
	public static final int NULL_BRUSH = 0x5;
	public static final int NULL_PEN = 0x8;
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
	public static final int PD_RETURNDEFAULT = 0x00000400;
	public static final int PD_SELECTION = 0x1;
	public static final int PD_USEDEVMODECOPIESANDCOLLATE = 0x40000;
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
	public static final int RB_GETBANDINFO = 0x41c;
	public static final int RB_GETBANDMARGINS = 0x428;
	public static final int RB_GETBARHEIGHT = 0x41b;
	public static final int RB_GETBKCOLOR = 0x414;
	public static final int RB_GETRECT = 0x409;
	public static final int RB_GETTEXTCOLOR = 0x416;
	public static final int RB_IDTOINDEX = 0x410;
	public static final int RB_INSERTBAND = 0x40a;
	public static final int RB_MOVEBAND = 0x427;
	public static final int RB_SETBANDINFO = 0x40b;
	public static final int RB_SETBKCOLOR = 0x413;
	public static final int RB_SETTEXTCOLOR = 0x415;
	public static final int RDW_ALLCHILDREN = 0x80;
	public static final int RDW_ERASE = 0x4;
	public static final int RDW_FRAME = 0x400;
	public static final int RDW_INVALIDATE = 0x1;
	public static final int RDW_UPDATENOW = 0x100;
	public static final String REBARCLASSNAME = "ReBarWindow32"; //$NON-NLS-1$
	public static final int REG_DWORD = 4;
	public static final int REG_OPTION_VOLATILE = 0x1;
	public static final int RGN_AND = 0x1;
	public static final int RGN_COPY = 5;
	public static final int RGN_DIFF = 0x4;
	public static final int RGN_ERROR = 0;
	public static final int RGN_OR = 0x2;
	public static final int SBP_ARROWBTN = 0x1;
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
	public static final int SC_CLOSE = 0xf060;
	public static final int SC_MOVE = 0xf010;
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
	public static final int SET_FEATURE_ON_PROCESS = 0x2;
	public static final int SHADEBLENDCAPS = 120;
	public static final int SHGFI_ICON = 0x000000100;
	public static final int SHGFI_SMALLICON= 0x1;
	public static final int SHGFI_USEFILEATTRIBUTES = 0x000000010;
	public static final int SIGDN_FILESYSPATH = 0x80058000;
	public static final int SIF_ALL = 0x17;
	public static final int SIF_DISABLENOSCROLL = 0x8;
	public static final int SIF_PAGE = 0x2;
	public static final int SIF_POS = 0x4;
	public static final int SIF_RANGE = 0x1;
	public static final int SIF_TRACKPOS = 0x10;
	public static final int SIZE_RESTORED = 0;
	public static final int SIZE_MINIMIZED = 1;
	public static final int SIZE_MAXIMIZED = 2;
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
	public static final int SM_CYEDGE = 0x2e;
	public static final int SM_CYFOCUSBORDER = 84;
	public static final int SM_CYHSCROLL = 0x3;
	public static final int SM_CYMENU = 0xf;
	public static final int SM_CXMINTRACK = 34;
	public static final int SM_CYMINTRACK = 35;
	public static final int SM_CXMAXTRACK = 59;
	public static final int SM_CYMAXTRACK = 60;
	public static final int SM_CMOUSEBUTTONS = 43;
	public static final int SM_CYSCREEN = 0x1;
	public static final int SM_CYVSCROLL = 0x14;
	public static final int SM_DIGITIZER = 94;
	public static final int SM_MAXIMUMTOUCHES= 95;
	public static final int SPI_GETFONTSMOOTHINGTYPE = 0x200A;
	public static final int SPI_GETHIGHCONTRAST = 66;
	public static final int SPI_GETWORKAREA = 0x30;
	public static final int SPI_GETMOUSEVANISH = 0x1020;
	public static final int SPI_GETNONCLIENTMETRICS = 41;
	public static final int SPI_GETWHEELSCROLLCHARS = 108;
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
	public static final int SWP_FRAMECHANGED = 0x0020;
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
	public static final int SW_RESTORE = 0x9;
	public static final int SW_SCROLLCHILDREN = 0x1;
	public static final int SW_SHOW = 0x5;
	public static final int SW_SHOWMAXIMIZED = 0x3;
	public static final int SW_SHOWMINIMIZED = 0x2;
	public static final int SW_SHOWMINNOACTIVE = 0x7;
	public static final int SW_SHOWNA = 0x8;
	public static final int SW_SHOWNOACTIVATE = 0x4;
	public static final int SYSRGN = 0x4;
	public static final int SYSTEM_FONT = 0xd;
	public static final int S_OK = 0x0;
	public static final int TABP_BODY = 10;
	public static final int TBCDRF_USECDCOLORS = 0x800000;
	public static final int TBCDRF_NOBACKGROUND = 0x00400000;
	public static final int TBIF_COMMAND = 0x20;
	public static final int TBIF_STATE = 0x4;
	public static final int TBIF_IMAGE = 0x1;
	public static final int TBIF_LPARAM = 0x10;
	public static final int TBIF_SIZE = 0x40;
	public static final int TBIF_STYLE = 0x8;
	public static final int TBIF_TEXT = 0x2;
	public static final int TB_GETEXTENDEDSTYLE = 0x400 + 85;
	public static final int TB_GETRECT = 0x400 + 51;
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
	public static final int TB_ADDSTRING = 0x44d;
	public static final int TB_AUTOSIZE = 0x421;
	public static final int TB_BUTTONCOUNT = 0x418;
	public static final int TB_BUTTONSTRUCTSIZE = 0x41e;
	public static final int TB_COMMANDTOINDEX = 0x419;
	public static final int TB_DELETEBUTTON = 0x416;
	public static final int TB_ENDTRACK = 0x8;
	public static final int TB_GETBUTTON = 0x417;
	public static final int TB_GETBUTTONINFO = 0x43f;
	public static final int TB_GETBUTTONSIZE = 0x43a;
	public static final int TB_GETBUTTONTEXT = 0x44b;
	public static final int TB_GETDISABLEDIMAGELIST = 0x437;
	public static final int TB_GETHOTIMAGELIST = 0x435;
	public static final int TB_GETHOTITEM = 0x0400 + 71;
	public static final int TB_GETIMAGELIST = 0x431;
	public static final int TB_GETITEMRECT = 0x41d;
	public static final int TB_GETPADDING = 0x0400 + 86;
	public static final int TB_GETROWS = 0x428;
	public static final int TB_GETSTATE = 0x412;
	public static final int TB_GETTOOLTIPS = 0x423;
	public static final int TB_INSERTBUTTON = 0x443;
	public static final int TB_LOADIMAGES = 0x432;
	public static final int TB_MAPACCELERATOR = 0x0400 + 90;
	public static final int TB_SETBITMAPSIZE = 0x420;
	public static final int TB_SETBUTTONINFO = 0x440;
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
	public static final int TCM_INSERTITEM = 0x133e;
	public static final int TCM_SETCURSEL = 0x130c;
	public static final int TCM_SETIMAGELIST = 0x1303;
	public static final int TCM_SETITEM = 0x133d;
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
	public static final int TS_TRUE = 1;
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
	public static final int TTM_ADDTOOL = 0x432;
	public static final int TTM_ADJUSTRECT = 0x400 + 31;
	public static final int TTM_GETCURRENTTOOL = 0x400 + 59;
	public static final int TTM_GETDELAYTIME = 0x400 + 21;
	public static final int TTM_DELTOOL = 0x433;
	public static final int TTM_GETTOOLINFO = 0x400 + 53;
	public static final int TTM_GETTOOLCOUNT = 0x40D;
	public static final int TTM_NEWTOOLRECT = 0x400 + 52;
	public static final int TTM_POP = 0x400 + 28;
	public static final int TTM_SETDELAYTIME = 0x400 + 3;
	public static final int TTM_SETMAXTIPWIDTH = 0x418;
	public static final int TTM_SETTITLE = 0x400 + 33;
	public static final int TTM_TRACKPOSITION = 1042;
	public static final int TTM_TRACKACTIVATE = 1041;
	public static final int TTM_UPDATE = 0x41D;
	public static final int TTM_UPDATETIPTEXT = 0x400 + 57;
	public static final int TTN_FIRST = 0xfffffdf8;
	public static final int TTN_GETDISPINFO = 0xfffffdee;
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
	public static final long TVI_FIRST = -0x0FFFF;
	public static final long TVI_LAST = -0x0FFFE;
	public static final long TVI_ROOT = -0x10000;
	public static final long TVI_SORT = -0x0FFFD;
	public static final int TVM_CREATEDRAGIMAGE = TV_FIRST + 18;
	public static final int TVM_DELETEITEM = 0x1101;
	public static final int TVM_ENSUREVISIBLE = 0x1114;
	public static final int TVM_EXPAND = 0x1102;
	public static final int TVM_GETBKCOLOR = 0x111f;
	public static final int TVM_GETCOUNT = 0x1105;
	public static final int TVM_GETEXTENDEDSTYLE = TV_FIRST + 45;
	public static final int TVM_GETIMAGELIST = 0x1108;
	public static final int TVM_GETITEM = 0x113e;
	public static final int TVM_GETITEMHEIGHT = 0x111c;
	public static final int TVM_GETITEMRECT = 0x1104;
	public static final int TVM_GETITEMSTATE = TV_FIRST + 39;
	public static final int TVM_GETNEXTITEM = 0x110a;
	public static final int TVM_GETTEXTCOLOR = 0x1120;
	public static final int TVM_GETTOOLTIPS = TV_FIRST + 25;
	public static final int TVM_GETVISIBLECOUNT = TV_FIRST + 16;
	public static final int TVM_HITTEST = 0x1111;
	public static final int TVM_INSERTITEM = 0x1132;
	public static final int TVM_MAPACCIDTOHTREEITEM = TV_FIRST + 42;
	public static final int TVM_MAPHTREEITEMTOACCID = TV_FIRST + 43;
	public static final int TVM_SELECTITEM = 0x110b;
	public static final int TVM_SETBKCOLOR = 0x111d;
	public static final int TVM_SETEXTENDEDSTYLE = TV_FIRST + 44;
	public static final int TVM_SETIMAGELIST = 0x1109;
	public static final int TVM_SETINDENT = TV_FIRST + 7;
	public static final int TVM_SETINSERTMARK = 0x111a;
	public static final int TVM_SETITEM = 0x113f;
	public static final int TVM_SETITEMHEIGHT = TV_FIRST + 27;
	public static final int TVM_SETSCROLLTIME = TV_FIRST + 33;
	public static final int TVM_SETTEXTCOLOR = 0x111e;
	public static final int TVM_SORTCHILDREN = TV_FIRST + 19;
	public static final int TVM_SORTCHILDRENCB = TV_FIRST + 21;
	public static final int TVN_BEGINDRAG = 0xfffffe38;
	public static final int TVN_BEGINRDRAG = 0xfffffe37;
	public static final int TVN_FIRST = 0xfffffe70;
	public static final int TVN_GETDISPINFO = TVN_FIRST - 52;
	public static final int TVN_ITEMCHANGING = TVN_FIRST - 17;
	public static final int TVN_ITEMEXPANDED = TVN_FIRST - 55;
	public static final int TVN_ITEMEXPANDING = 0xfffffe3a;
	public static final int TVN_SELCHANGED = 0xfffffe3d;
	public static final int TVN_SELCHANGING = 0xfffffe3e;
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
	public static final int UDM_GETPOS32 = 0x0472;
	public static final int UDM_SETACCEL = 0x046B;
	public static final int UDM_SETRANGE32 = 0x046f;
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
	public static final int VK_HANJA = 0x19;
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
	public static final int VT_BOOL = 11;
	public static final int VT_LPWSTR = 31;
	public static final short VARIANT_TRUE = -1;
	public static final short VARIANT_FALSE = 0;
	public static final short WA_CLICKACTIVE = 2;
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
	public static final int WM_DPICHANGED = 0x02E0;
	public static final int WM_DRAWITEM = 0x2b;
	public static final int WM_ENDSESSION = 0x16;
	public static final int WM_ENTERIDLE = 0x121;
	public static final int WM_ERASEBKGND = 0x14;
	public static final int WM_GESTURE = 0x0119;
	public static final int WM_GETDLGCODE = 0x87;
	public static final int WM_GETFONT = 0x31;
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
	public static final int WM_PARENTNOTIFY = 0x0210;
	public static final int WM_ENTERMENULOOP = 0x0211;
	public static final int WM_EXITMENULOOP = 0x0212;
	public static final int WM_ENTERSIZEMOVE = 0x0231;
	public static final int WM_EXITSIZEMOVE = 0x0232;
	public static final int WM_PASTE = 0x302;
	public static final int WM_PRINT = 0x0317;
	public static final int WM_PRINTCLIENT = 0x0318;
	public static final int WM_QUERYENDSESSION = 0x11;
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
	public static final int WM_SYSDEADCHAR = 0x0107;
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
	public static final int WS_MAXIMIZEBOX = 0x10000;
	public static final int WS_MINIMIZEBOX = 0x20000;
	public static final int WS_OVERLAPPED = 0x0;
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
public static final native int BUTTON_IMAGELIST_sizeof ();
public static final native int CANDIDATEFORM_sizeof ();
public static final native int CHOOSECOLOR_sizeof ();
public static final native int CHOOSEFONT_sizeof ();
public static final native int COMBOBOXINFO_sizeof ();
public static final native int COMPOSITIONFORM_sizeof ();
public static final native int CREATESTRUCT_sizeof ();
public static final native int DEVMODE_sizeof ();
public static final native int DIBSECTION_sizeof ();
public static final native int DOCHOSTUIINFO_sizeof ();
public static final native int DOCINFO_sizeof ();
public static final native int DRAWITEMSTRUCT_sizeof ();
public static final native int DROPFILES_sizeof ();
public static final native int EMR_sizeof ();
public static final native int EMREXTCREATEFONTINDIRECTW_sizeof ();
public static final native int EXTLOGFONTW_sizeof ();
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
public static final native int CIDA_sizeof ();
public static final native int INITCOMMONCONTROLSEX_sizeof ();
public static final native int INPUT_sizeof ();
public static final native int KEYBDINPUT_sizeof ();
public static final native int LITEM_sizeof ();
public static final native int LOGBRUSH_sizeof ();
public static final native int LOGFONT_sizeof ();
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
public static final native int NMTBHOTITEM_sizeof ();
public static final native int NMTREEVIEW_sizeof ();
public static final native int NMTOOLBAR_sizeof ();
public static final native int NMTTDISPINFO_sizeof ();
public static final native int NMTTCUSTOMDRAW_sizeof ();
public static final native int NMTBCUSTOMDRAW_sizeof ();
public static final native int NMTVCUSTOMDRAW_sizeof ();
public static final native int NMTVDISPINFO_sizeof ();
public static final native int NMTVITEMCHANGE_sizeof ();
public static final native int NMUPDOWN_sizeof ();
public static final native int NONCLIENTMETRICS_sizeof ();
/** @method flags=const */
public static final native int NOTIFYICONDATA_V2_SIZE ();
public static final native int OUTLINETEXTMETRIC_sizeof ();
public static final native int OSVERSIONINFOEX_sizeof ();
public static final native int PAINTSTRUCT_sizeof ();
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
public static final native int SCRIPT_FONTPROPERTIES_sizeof ();
public static final native int SCRIPT_ITEM_sizeof ();
public static final native int SCRIPT_LOGATTR_sizeof ();
public static final native int SCRIPT_PROPERTIES_sizeof ();
public static final native int SCRIPT_STATE_sizeof ();
public static final native int SCRIPT_STRING_ANALYSIS_sizeof ();
public static final native int SCROLLBARINFO_sizeof ();
public static final native int SCROLLINFO_sizeof ();
public static final native int SHDRAGIMAGE_sizeof();
public static final native int SHELLEXECUTEINFO_sizeof ();
public static final native int SHFILEINFO_sizeof ();
public static final native int SIZE_sizeof ();
public static final native int STARTUPINFO_sizeof ();
public static final native int SYSTEMTIME_sizeof ();
public static final native int TBBUTTON_sizeof ();
public static final native int TBBUTTONINFO_sizeof ();
public static final native int TCITEM_sizeof ();
public static final native int TCHITTESTINFO_sizeof ();
public static final native int TEXTMETRIC_sizeof ();
public static final native int TF_DA_COLOR_sizeof ();
public static final native int TF_DISPLAYATTRIBUTE_sizeof ();
public static final native int TOOLINFO_sizeof ();
public static final native int TOUCHINPUT_sizeof();
public static final native int TRACKMOUSEEVENT_sizeof ();
public static final native int TRIVERTEX_sizeof ();
public static final native int TVHITTESTINFO_sizeof ();
public static final native int TVINSERTSTRUCT_sizeof ();
public static final native int TVITEM_sizeof ();
public static final native int TVSORTCB_sizeof ();
public static final native int UDACCEL_sizeof ();
public static final native int WINDOWPLACEMENT_sizeof ();
public static final native int WINDOWPOS_sizeof ();
public static final native int WNDCLASS_sizeof ();

/** Ansi/Unicode wrappers */

public static final long AddFontResourceEx (TCHAR lpszFilename, int fl, long pdv) {
	char [] lpszFilename1 = lpszFilename == null ? null : lpszFilename.chars;
	return AddFontResourceEx (lpszFilename1, fl, pdv);
}

public static final int AssocQueryString(int flags, int str, TCHAR pszAssoc, TCHAR pszExtra, TCHAR pszOut, int[] pcchOut) {
	char [] pszAssoc1 = pszAssoc == null ? null : pszAssoc.chars;
	char [] pszExtra1 = pszExtra == null ? null : pszExtra.chars;
	char [] pszOut1 = pszOut == null ? null : pszOut.chars;
	return AssocQueryString (flags, str, pszAssoc1, pszExtra1, pszOut1, pcchOut);
}

public static final long CreateDC (TCHAR lpszDriver, TCHAR lpszDevice, long lpszOutput, long lpInitData) {
	char [] lpszDriver1 = lpszDriver == null ? null : lpszDriver.chars;
	char [] lpszDevice1 = lpszDevice == null ? null : lpszDevice.chars;
	return CreateDC (lpszDriver1, lpszDevice1, lpszOutput, lpInitData);
}

public static final long CreateWindowEx (int dwExStyle, TCHAR lpClassName, TCHAR lpWindowName, int dwStyle, int X, int Y, int nWidth, int nHeight, long hWndParent, long hMenu, long hInstance, CREATESTRUCT lpParam) {
	char [] lpClassName1 = lpClassName == null ? null : lpClassName.chars;
	char [] lpWindowName1 = lpWindowName == null ? null : lpWindowName.chars;
	return CreateWindowEx (dwExStyle, lpClassName1, lpWindowName1, dwStyle, X, Y, nWidth, nHeight, hWndParent, hMenu, hInstance, lpParam);
}

public static final int DocumentProperties (long hWnd, long hPrinter, TCHAR pDeviceName, long pDevModeOutput, long pDevModeInput, int fMode) {
	char [] pDeviceName1 = pDeviceName == null ? null : pDeviceName.chars;
	return DocumentProperties (hWnd, hPrinter, pDeviceName1, pDevModeOutput, pDevModeInput, fMode);
}

public static final int DrawText (long hDC, TCHAR lpString, int nCount, RECT lpRect, int uFormat) {
	char [] lpString1 = lpString == null ? null : lpString.chars;
	return DrawText (hDC, lpString1, nCount, lpRect, uFormat);
}

public static final int ExpandEnvironmentStrings (TCHAR lpSrc, TCHAR lpDst, int nSize) {
	char [] lpSrc1 = lpSrc == null ? null : lpSrc.chars;
	char [] lpDst1 = lpDst == null ? null : lpDst.chars;
	return ExpandEnvironmentStrings (lpSrc1, lpDst1, nSize);
}

public static final int ExtractIconEx (TCHAR lpszFile, int nIconIndex, long [] phiconLarge, long [] phiconSmall, int nIcons) {
	char [] lpszFile1 = lpszFile == null ? null : lpszFile.chars;
	return ExtractIconEx (lpszFile1, nIconIndex, phiconLarge, phiconSmall, nIcons);
}

public static final boolean GetClassInfo (long hInstance, TCHAR lpClassName, WNDCLASS lpWndClass) {
	boolean result;

	char [] lpClassName1 = lpClassName == null ? null : lpClassName.chars;
	result = GetClassInfo (hInstance, lpClassName1, lpWndClass);

	/*
	* WINAPI GetClassInfo copies lpClassName1 pointer to WNDCLASS.lpszClassName.
	* But because JNI code copies java's TCHAR to temporary native string, temporary pointer gets copied.
	* Upon return from JNI GetClassInfo, WNDCLASS contains pointer to already freed memory.
	* Usually the memory stays untouched for a short while, and code seems to work just fine.
	* To prevent this subtle error, field is zeroed to draw attention.
	*/
	lpWndClass.lpszClassName = 0;

	return result;
}

public static final int GetLocaleInfo (int Locale, int LCType, TCHAR lpLCData, int cchData) {
	char [] lpLCData1 = lpLCData == null ? null : lpLCData.chars;
	return GetLocaleInfo (Locale, LCType, lpLCData1, cchData);
}

public static final int GetModuleFileName (long hModule, TCHAR lpFilename, int inSize) {
	char [] lpFilename1 = lpFilename == null ? null : lpFilename.chars;
	return GetModuleFileName (hModule, lpFilename1, inSize);
}

public static final int GetProfileString (TCHAR lpAppName, TCHAR lpKeyName, TCHAR lpDefault, TCHAR lpReturnedString, int nSize) {
	char [] lpAppName1 = lpAppName == null ? null : lpAppName.chars;
	char [] lpKeyName1 = lpKeyName == null ? null : lpKeyName.chars;
	char [] lpDefault1 = lpDefault == null ? null : lpDefault.chars;
	char [] lpReturnedString1 = lpReturnedString == null ? null : lpReturnedString.chars;
	return GetProfileString (lpAppName1, lpKeyName1, lpDefault1, lpReturnedString1, nSize);
}

public static final int GetWindowText (long hWnd, TCHAR lpString, int nMaxCount) {
	char [] lpString1 = lpString == null ? null : lpString.chars;
	return GetWindowText (hWnd, lpString1, nMaxCount);
}

public static final int GlobalAddAtom (TCHAR lpString) {
	char [] lpString1 = lpString == null ? null : lpString.chars;
	return GlobalAddAtom (lpString1);
}

public static final long ImmEscape (long hKL,long hIMC, int uEscape, TCHAR lpData) {
	char [] lpData1 = lpData == null ? null : lpData.chars;
	return ImmEscape (hKL, hIMC, uEscape, lpData1);
}

public static final boolean InternetGetCookie (TCHAR lpszUrl, TCHAR lpszCookieName, TCHAR lpszCookieData, int[] lpdwSize) {
	char [] url = lpszUrl == null ? null : lpszUrl.chars;
	char [] cookieName = lpszCookieName == null ? null : lpszCookieName.chars;
	char [] cookieData = lpszCookieData == null ? null : lpszCookieData.chars;
	return InternetGetCookie (url, cookieName, cookieData, lpdwSize);
}

public static final boolean InternetSetCookie (TCHAR lpszUrl, TCHAR lpszCookieName, TCHAR lpszCookieData) {
	char [] url = lpszUrl == null ? null : lpszUrl.chars;
	char [] cookieName = lpszCookieName == null ? null : lpszCookieName.chars;
	char [] cookieData = lpszCookieData == null ? null : lpszCookieData.chars;
	return InternetSetCookie (url, cookieName, cookieData);
}

public static final int MessageBox (long hWnd, TCHAR lpText, TCHAR lpCaption, int uType) {
	char [] lpText1 = lpText == null ? null : lpText.chars;
	char [] lpCaption1 = lpCaption == null ? null : lpCaption.chars;
	return MessageBox (hWnd, lpText1, lpCaption1, uType);
}

public static final void MoveMemory (long Destination, TCHAR Source, int Length) {
	char [] Source1 = Source == null ? null : Source.chars;
	MoveMemory (Destination, Source1, Length);
}

public static final void MoveMemory (TCHAR Destination, long Source, int Length) {
	char [] Destination1 = Destination == null ? null : Destination.chars;
	MoveMemory (Destination1, Source, Length);
}

public static final boolean OpenPrinter (TCHAR pPrinterName, long [] phPrinter, long pDefault) {
	char [] pPrinterName1 = pPrinterName == null ? null : pPrinterName.chars;
	return OpenPrinter (pPrinterName1, phPrinter, pDefault);
}

public static final int[] readRegistryDwords(int hkeyLocation, String key, String valueName) {
	final int ERROR_MORE_DATA = 234;
	Objects.requireNonNull("key", key);
	Objects.requireNonNull("valueName", valueName);
	long[] phkResult = new long[1];
	TCHAR regKey = new TCHAR(0, key, true);
	TCHAR lpValueName = new TCHAR(0, valueName, true);
	if (OS.RegOpenKeyEx(hkeyLocation, regKey, 0, OS.KEY_READ, phkResult) != 0) {
		return null; // Registry entry not found
	}
	int size = 2;
	int result;
	do {
		int[] lpcbData = new int[] { 4 * size }; // 4 bytes per int
		int[] lpData = new int[size];
		result = OS.RegQueryValueEx(phkResult[0], lpValueName, 0, null, lpData, lpcbData);
		OS.RegCloseKey(phkResult[0]);
		if (result == 0) {
			return lpData;
		}
		size *= 2;
	} while (result == ERROR_MORE_DATA);
	return null; // other error
}
public static final int RegCreateKeyEx (long hKey, TCHAR lpSubKey, int Reserved, TCHAR lpClass, int dwOptions, int samDesired, long lpSecurityAttributes, long[] phkResult, long[] lpdwDisposition) {
	char [] lpClass1 = lpClass == null ? null : lpClass.chars;
	char [] lpSubKey1 = lpSubKey == null ? null : lpSubKey.chars;
	return RegCreateKeyEx (hKey, lpSubKey1, Reserved, lpClass1, dwOptions, samDesired, lpSecurityAttributes, phkResult, lpdwDisposition);
}

public static final int RegDeleteValue (long hKey, TCHAR lpValueName) {
	char [] lpValueName1 = lpValueName == null ? null : lpValueName.chars;
	return RegDeleteValue (hKey, lpValueName1);
}

public static final int RegEnumKeyEx (long hKey, int dwIndex, TCHAR lpName, int [] lpcName, int [] lpReserved, TCHAR lpClass, int [] lpcClass, long lpftLastWriteTime) {
	char [] lpName1 = lpName == null ? null : lpName.chars;
	char [] lpClass1 = lpClass == null ? null : lpClass.chars;
	return RegEnumKeyEx (hKey, dwIndex, lpName1, lpcName, lpReserved, lpClass1, lpcClass, lpftLastWriteTime);
}

public static final int RegisterClass (TCHAR lpszClassName, WNDCLASS lpWndClass) {
	/* Allocate a native string */
	long hHeap = OS.GetProcessHeap ();
	int byteCount = lpszClassName.length () * TCHAR.sizeof;
	lpWndClass.lpszClassName = OS.HeapAlloc (hHeap, OS.HEAP_ZERO_MEMORY, byteCount);
	OS.MoveMemory (lpWndClass.lpszClassName, lpszClassName, byteCount);

	int result = RegisterClass (lpWndClass);

	/* Release and forget native string */
	OS.HeapFree (hHeap, 0, lpWndClass.lpszClassName);
	lpWndClass.lpszClassName = 0;

	return result;
}

public static final int RegisterClipboardFormat (TCHAR lpszFormat) {
	char [] lpszFormat1 = lpszFormat == null ? null : lpszFormat.chars;
	return RegisterClipboardFormat (lpszFormat1);
}

public static final int RegisterWindowMessage (TCHAR lpString) {
	char [] lpString1 = lpString == null ? null : lpString.chars;
	return RegisterWindowMessage (lpString1);
}

public static final int RegOpenKeyEx (long hKey, TCHAR lpSubKey, int ulOptions, int samDesired, long[] phkResult) {
	char [] lpSubKey1 = lpSubKey == null ? null : lpSubKey.chars;
	return RegOpenKeyEx (hKey, lpSubKey1, ulOptions, samDesired, phkResult);
}

public static final int RegQueryValueEx (long hKey, TCHAR lpValueName, long lpReserved, int[] lpType, TCHAR lpData, int[] lpcbData) {
	char [] lpValueName1 = lpValueName == null ? null : lpValueName.chars;
	char [] lpData1 = lpData == null ? null : lpData.chars;
	return RegQueryValueEx (hKey, lpValueName1, lpReserved, lpType, lpData1, lpcbData);
}

public static final int RegQueryValueEx (long hKey, TCHAR lpValueName, long lpReserved, int[] lpType, int [] lpData, int[] lpcbData) {
	char [] lpValueName1 = lpValueName == null ? null : lpValueName.chars;
	return RegQueryValueEx (hKey, lpValueName1, lpReserved, lpType, lpData, lpcbData);
}

public static final int RegSetValueEx (long hKey, TCHAR lpValueName, int Reserved, int dwType, int[] lpData, int cbData) {
	char [] lpValueName1 = lpValueName == null ? null : lpValueName.chars;
	return RegSetValueEx (hKey, lpValueName1, Reserved, dwType, lpData, cbData);
}

public static final long SendMessage (long hWnd, int Msg, long wParam, TCHAR lParam) {
	char [] lParam1 = lParam == null ? null : lParam.chars;
	return SendMessage (hWnd, Msg, wParam, lParam1);
}

/**
 * Experimental API for dark theme.
 * <p>
 * On Windows, there is no OS API for dark theme yet, and this method only
 * configures various tweaks. Some of these tweaks have drawbacks. The tweaks
 * are configured with defaults that fit Eclipse. Non-Eclipse applications are
 * expected to configure individual tweaks instead of calling this method.
 * Please see <code>Display#setData()</code> and documentation for string keys
 * used there.
 * </p>
 * <p>
 * On GTK, behavior may be different as the boolean flag doesn't force dark
 * theme instead it specify that dark theme is preferred.
 * </p>
 *
 * @param isDarkTheme <code>true</code> for dark theme
 */
public static final void setTheme(boolean isDarkTheme) {
	/*
	 * On macOS and GTK, setting dark theme is supported by system API.
	 * Probably this is why it was chosen to have 'OS.setTheme()' SWT API
	 * in 'OS' rather then 'Display'. However, on Windows, there is no
	 * official API yet, just some tweaks to tailor things that SWT can't
	 * color properly. These use settings in Display to allow applications
	 * to configure individual tweaks.
	 */

	Display display = Display.getCurrent();
	if (display == null)
		throw new NullPointerException("Display must be already created before you call OS.setTheme()");

	display.setData("org.eclipse.swt.internal.win32.useDarkModeExplorerTheme", isDarkTheme);
	display.setData("org.eclipse.swt.internal.win32.useShellTitleColoring",    isDarkTheme);
	display.setData("org.eclipse.swt.internal.win32.menuBarForegroundColor",   isDarkTheme ? new Color(display, 0xD0, 0xD0, 0xD0) : null);
	display.setData("org.eclipse.swt.internal.win32.menuBarBackgroundColor",   isDarkTheme ? new Color(display, 0x30, 0x30, 0x30) : null);
	display.setData("org.eclipse.swt.internal.win32.menuBarBorderColor",       isDarkTheme ? new Color(display, 0x50, 0x50, 0x50) : null);
	display.setData("org.eclipse.swt.internal.win32.Canvas.use_WS_BORDER",     isDarkTheme);
	display.setData("org.eclipse.swt.internal.win32.List.use_WS_BORDER",       isDarkTheme);
	display.setData("org.eclipse.swt.internal.win32.Table.use_WS_BORDER",      isDarkTheme);
	display.setData("org.eclipse.swt.internal.win32.Text.use_WS_BORDER",       isDarkTheme);
	display.setData("org.eclipse.swt.internal.win32.Tree.use_WS_BORDER",       isDarkTheme);
	display.setData("org.eclipse.swt.internal.win32.Table.headerLineColor",    isDarkTheme ? new Color(display, 0x50, 0x50, 0x50) : null);
	display.setData("org.eclipse.swt.internal.win32.Label.disabledForegroundColor", isDarkTheme ? new Color(display, 0x80, 0x80, 0x80) : null);
	display.setData("org.eclipse.swt.internal.win32.Combo.useDarkTheme",       isDarkTheme);
	display.setData("org.eclipse.swt.internal.win32.ProgressBar.useColors",    isDarkTheme);
	display.setData("org.eclipse.swt.internal.win32.Text.useDarkThemeIcons",   isDarkTheme);
}

public static final boolean SetWindowText (long hWnd, TCHAR lpString) {
	char [] lpString1 = lpString == null ? null : lpString.chars;
	return SetWindowText (hWnd, lpString1);
}

public static final boolean UnregisterClass (TCHAR lpClassName, long hInstance) {
	char [] lpClassName1 = lpClassName == null ? null : lpClassName.chars;
	return UnregisterClass (lpClassName1, hInstance);
}

public static final int UrlCreateFromPath (TCHAR pszPath, TCHAR pszURL, int[] pcchUrl, int flags) {
	char [] path = pszPath == null ? null : pszPath.chars;
	char [] url = pszURL == null ? null : pszURL.chars;
	return UrlCreateFromPath (path, url, pcchUrl, flags);
}

/* Macros */

public static final int GET_WHEEL_DELTA_WPARAM (long wParam) { return (short)HIWORD (wParam); }
public static final int GET_X_LPARAM (long lp) { return (short)LOWORD (lp); }
public static final int GET_Y_LPARAM (long lp) { return (short)HIWORD (lp); }
public static final int HIWORD (long l) { return (int)l >>> 16; }
public static final int LOWORD (long l) { return (int)l & 0xffff; }
public static final int MAKEWORD (int l, int h) { return (l & 0xff) | ((h & 0xff) << 8); }
public static final long MAKELPARAM (int l, int h) { return ((l & 0xffff) | (h << 16)) & 0xffffffffL; }
public static final long MAKELRESULT (int l, int h) { return MAKELPARAM (l, h); }
public static final long MAKEWPARAM (int l, int h) { return MAKELPARAM (l, h); }
public static final void POINTSTOPOINT (POINT pt, long pts) { pt.x = (short)LOWORD (pts); pt.y = (short)HIWORD (pts); }
public static final int PRIMARYLANGID (int lgid) { return lgid & 0x3ff; }
public static final int TOUCH_COORD_TO_PIXEL (int touchCoord) { return touchCoord / 100; }
public static int HRESULT_FROM_WIN32(int x) {
	return x <= 0 ? x : ((x & 0x0000FFFF) | 0x80070000);
}

/** Natives */

/** @param hdc cast=(HDC) */
public static final native int AbortDoc (long hdc);
/**
 * @param hActCtx cast=(HANDLE)
 * @param lpCookie cast=(ULONG_PTR*)
 */
public static final native boolean ActivateActCtx (long hActCtx, long [] lpCookie);
/** @param hkl cast=(HKL) */
public static final native long ActivateKeyboardLayout(long hkl, int Flags);
/**
 * @param lpszFilename flags=no_out
 * @param pdv cast=(PVOID)
 */
public static final native int AddFontResourceEx(char[] lpszFilename, int fl, long pdv);
public static final native boolean AdjustWindowRectEx (RECT lpRect, int dwStyle, boolean bMenu, int dwExStyle);
/** @method flags=no_gen */
public static final native boolean AllowDarkModeForWindow(long hWnd, boolean allow);
public static final native boolean AllowSetForegroundWindow (int dwProcessId);
/**
 * @param hdcDest cast=(HDC)
 * @param hdcSrc cast=(HDC)
 * @param blendFunction flags=struct
 */
public static final native boolean AlphaBlend(long hdcDest, int nXOriginDest, int nYOriginDest, int nWidthDest, int nHeightDest, long hdcSrc, int nXOriginSrc, int nYOriginSrc, int nWidthSrc, int nHeightSrc, BLENDFUNCTION blendFunction);
/** @param hdc cast=(HDC) */
public static final native boolean Arc (long hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect, int nXStartArc, int nYStartArc, int nXEndArc, int nYEndArc);
/**
 * @param pszAssoc flags=no_out
 * @param pszExtra flags=no_out
 * @param pcchOut cast=(DWORD *)
 */
public static final native int AssocQueryString (int flags, int str, char[] pszAssoc, char[] pszExtra, char[] pszOut, int[] pcchOut);
/**
 * @param hdcTarget cast=(HDC)
 * @param prcTarget flags=no_out
 * @param phdc cast=(HDC*)
 */
public static final native long BeginBufferedPaint (long hdcTarget, RECT prcTarget, int dwFormat, BP_PAINTPARAMS pPaintParams, long [] phdc);
public static final native long BeginDeferWindowPos (int nNumWindows);
/**
 * @param hWnd cast=(HWND)
 * @param lpPaint flags=no_in
 */
public static final native long BeginPaint (long hWnd, PAINTSTRUCT lpPaint);
/**
 * @param hdcDest cast=(HDC)
 * @param hdcSrc cast=(HDC)
 */
public static final native boolean BitBlt (long hdcDest, int nXDest, int nYDest, int nWidth, int nHeight, long hdcSrc, int nXSrc, int nYSrc, int dwRop);
/** @param hWnd cast=(HWND) */
public static final native boolean BringWindowToTop (long hWnd);
public static final native int BufferedPaintInit ();
public static final native int BufferedPaintUnInit ();
/**
 * @param hhk cast=(HHOOK)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long CallNextHookEx(long hhk, int nCode, long wParam, long lParam);
/**
 * @param lpPrevWndFunc cast=(WNDPROC)
 * @param hWnd cast=(HWND)
 */
public static final native long CallWindowProc (long lpPrevWndFunc, long hWnd, int Msg, long wParam, long lParam);
/** @param ch cast=(LPWSTR) */
public static final native long CharLower (long ch);
/** @param ch cast=(LPWSTR) */
public static final native long CharUpper (long ch);
/**
 * @param hWndParent cast=(HWND)
 * @param pt flags=struct
 * @param flags cast=(UINT)
 */
public static final native long ChildWindowFromPointEx (long hWndParent, POINT pt, int flags);
public static final native boolean ChooseColor (CHOOSECOLOR lpcc);
public static final native boolean ChooseFont (CHOOSEFONT chooseFont);
/** @param hWnd cast=(HWND) */
public static final native boolean ClientToScreen (long hWnd, POINT lpPoint);
public static final native boolean CloseClipboard ();
/** @param hdc cast=(HDC) */
public static final native long CloseEnhMetaFile (long hdc);
/** @param hGesture cast=(HGESTUREINFO) */
public static final native long CloseGestureInfoHandle (long hGesture);
/** @param hObject cast=(HANDLE) */
public static final native boolean CloseHandle (long hObject);
/** @param hPrinter cast=(HANDLE) */
public static final native boolean ClosePrinter (long hPrinter);
/** @param hTheme cast=(HTHEME) */
public static final native int CloseThemeData (long hTheme);
/** @param hTouchInput cast=(HTOUCHINPUT) */
public static final native boolean CloseTouchInputHandle(long hTouchInput);
public static final native int CoInternetIsFeatureEnabled (int FeatureEntry, int dwFlags);
/** @param fEnable cast=(BOOL) */
public static final native int CoInternetSetFeatureEnabled (int FeatureEntry, int dwFlags, boolean fEnable);
/**
 * @param hrgnDest cast=(HRGN)
 * @param hrgnSrc1 cast=(HRGN)
 * @param hrgnSrc2 cast=(HRGN)
 */
public static final native int CombineRgn (long hrgnDest, long hrgnSrc1, long hrgnSrc2, int fnCombineMode);
/** @param hImage cast=(HANDLE) */
public static final native long CopyImage (long hImage, int uType, int cxDesired, int cyDesired, int fuFlags);
/** @param cb cast=(ULONG) */
public static final native long CoTaskMemAlloc(int cb);
/** @param pv cast=(LPVOID) */
public static final native void CoTaskMemFree(long pv);
/** @param lpaccl cast=(LPACCEL),flags=no_out */
public static final native long CreateAcceleratorTable (byte [] lpaccl, int cEntries);
/** @param pActCtx flags=no_out */
public static final native long CreateActCtx (ACTCTX pActCtx);
/** @param lpvBits cast=(CONST VOID *),flags=no_out critical */
public static final native long CreateBitmap (int nWidth, int nHeight, int cPlanes, int cBitsPerPel, byte [] lpvBits);
/**
 * @param hWnd cast=(HWND)
 * @param hBitmap cast=(HBITMAP)
 */
public static final native boolean CreateCaret (long hWnd, long hBitmap, int nWidth, int nHeight);
/** @param hdc cast=(HDC) */
public static final native long CreateCompatibleBitmap (long hdc, int nWidth, int nHeight);
/** @param hdc cast=(HDC) */
public static final native long CreateCompatibleDC (long hdc);
/**
 * @param hInst cast=(HINSTANCE)
 * @param pvANDPlane cast=(CONST VOID *),flags=no_out critical
 * @param pvXORPlane cast=(CONST VOID *),flags=no_out critical
 */
public static final native long CreateCursor (long hInst, int xHotSpot, int yHotSpot, int nWidth, int nHeight, byte [] pvANDPlane, byte [] pvXORPlane);
/**
 * @param lpszDriver cast=(LPWSTR)
 * @param lpszDevice cast=(LPWSTR)
 * @param lpszOutput cast=(LPWSTR)
 * @param lpInitData cast=(CONST DEVMODEW *)
 */
public static final native long CreateDC (char [] lpszDriver, char [] lpszDevice, long lpszOutput, long lpInitData);
/**
 * @param hdc cast=(HDC)
 * @param pbmi cast=(BITMAPINFO *),flags=no_out critical
 * @param ppvBits cast=(VOID **),flags=no_in critical
 * @param hSection cast=(HANDLE)
 */
public static final native long CreateDIBSection(long hdc, byte[] pbmi, int iUsage, long[] ppvBits, long hSection, int dwOffset);
/**
 * @param hdc cast=(HDC)
 * @param pbmi cast=(BITMAPINFO *),flags=no_out critical
 * @param ppvBits cast=(VOID **),flags=no_in critical
 * @param hSection cast=(HANDLE)
 */
public static final native long CreateDIBSection(long hdc, long pbmi, int iUsage, long[] ppvBits, long hSection, int dwOffset);
/**
 * @param hdcRef cast=(HDC)
 * @param lpFilename cast=(LPCWSTR),flags=no_out
 * @param lpRect flags=no_out
 * @param lpDescription cast=(LPCWSTR),flags=no_out
 */
public static final native long CreateEnhMetaFile (long hdcRef, char[] lpFilename, RECT lpRect, char[] lpDescription);
/** @param lplf cast=(LPLOGFONTW) */
public static final native long CreateFontIndirect (long lplf);
/** @param lplf flags=no_out */
public static final native long CreateFontIndirect (LOGFONT lplf);
/** @param lplf flags=no_out */
public static final native long CreateIconIndirect (ICONINFO lplf);
public static final native long CreateMenu ();
/** @param hbmp cast=(HBITMAP) */
public static final native long CreatePatternBrush (long hbmp);
/** @param crColor cast=(COLORREF) */
public static final native long CreatePen (int fnPenStyle, int nWidth, int crColor);
/** @param lppt cast=(CONST POINT *) */
public static final native long CreatePolygonRgn(int[] lppt, int cPoints, int fnPolyFillMode);
public static final native long CreatePopupMenu ();
/**
 * @param lpApplicationName cast=(LPCWSTR)
 * @param lpCommandLine cast=(LPWSTR)
 * @param lpProcessAttributes cast=(LPSECURITY_ATTRIBUTES)
 * @param lpThreadAttributes cast=(LPSECURITY_ATTRIBUTES)
 * @param lpEnvironment cast=(LPVOID)
 * @param lpCurrentDirectory cast=(LPWSTR)
 * @param lpStartupInfo flags=no_out
 * @param lpProcessInformation flags=no_in
 */
public static final native boolean CreateProcess (long lpApplicationName, long lpCommandLine, long lpProcessAttributes, long lpThreadAttributes, boolean bInheritHandles, int dwCreationFlags, long lpEnvironment, long lpCurrentDirectory, STARTUPINFO lpStartupInfo, PROCESS_INFORMATION lpProcessInformation);
public static final native long CreateRectRgn (int left, int top, int right, int bottom);
/** @param colorRef cast=(COLORREF) */
public static final native long CreateSolidBrush (int colorRef);
/**
 * @param hGlobal cast=(HGLOBAL)
 * @param fDeleteOnRelease cast=(BOOL)
 * @param ppstm cast=(LPSTREAM *)
 */
public static final native int CreateStreamOnHGlobal(long hGlobal, boolean fDeleteOnRelease, long[] ppstm);
/**
 * @param lpClassName cast=(LPWSTR),flags=no_out
 * @param lpWindowName cast=(LPWSTR),flags=no_out
 * @param hWndParent cast=(HWND)
 * @param hMenu cast=(HMENU)
 * @param hInstance cast=(HINSTANCE)
 */
public static final native long CreateWindowEx (int dwExStyle, char [] lpClassName, char [] lpWindowName, int dwStyle, int X, int Y, int nWidth, int nHeight, long hWndParent, long hMenu, long hInstance, CREATESTRUCT lpParam);
/**
 * @param hWinPosInfo cast=(HDWP)
 * @param hWnd cast=(HWND)
 * @param hWndInsertAfter cast=(HWND)
 */
public static final native long DeferWindowPos (long hWinPosInfo, long hWnd, long hWndInsertAfter, int X, int Y, int cx, int cy, int uFlags);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long DefMDIChildProc (long hWnd, int Msg, long wParam, long lParam);
/**
 * @param hWnd cast=(HWND)
 * @param hWndMDIClient cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long DefFrameProc (long hWnd, long hWndMDIClient, int Msg, long wParam, long lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long DefWindowProc (long hWnd, int Msg, long wParam, long lParam);
/** @param hdc cast=(HDC) */
public static final native boolean DeleteDC (long hdc);
/** @param hemf cast=(HENHMETAFILE) */
public static final native boolean DeleteEnhMetaFile (long hemf);
/** @param hMenu cast=(HMENU) */
public static final native boolean DeleteMenu (long hMenu, int uPosition, int uFlags);
/** @param hGdiObj cast=(HGDIOBJ) */
public static final native boolean DeleteObject (long hGdiObj);
/** @param hAccel cast=(HACCEL) */
public static final native boolean DestroyAcceleratorTable (long hAccel);
public static final native boolean DestroyCaret ();
/** @param hCursor cast=(HCURSOR) */
public static final native boolean DestroyCursor (long hCursor);
/** @param hIcon cast=(HICON) */
public static final native boolean DestroyIcon (long hIcon);
/** @param hMenu cast=(HMENU) */
public static final native boolean DestroyMenu (long hMenu);
/** @param hWnd cast=(HWND) */
public static final native boolean DestroyWindow (long hWnd);
public static final native long DispatchMessage (MSG lpmsg);
/**
 * @param hWnd cast=(HWND)
 * @param hPrinter cast=(HANDLE)
 * @param pDeviceName cast=(LPWSTR)
 * @param pDevModeOutput cast=(PDEVMODEW)
 * @param pDevModeInput cast=(PDEVMODEW)
 */
public static final native int DocumentProperties (long hWnd, long hPrinter, char[] pDeviceName, long pDevModeOutput, long pDevModeInput, int fMode);
/**
 * @param hwnd cast=(HWND)
 * @param pt flags=struct
 */
public static final native boolean DragDetect (long hwnd, POINT pt);
/** @param hDrop cast=(HDROP) */
public static final native void DragFinish (long hDrop);
/**
 * @param hDrop cast=(HDROP)
 * @param lpszFile cast=(LPWSTR)
 */
public static final native int DragQueryFile (long hDrop, int iFile, char[] lpszFile, int cch);
/** @param hdc cast=(HDC) */
public static final native boolean DrawEdge (long hdc, RECT qrc, int edge, int grfFlags);
/**
 * @param hDC cast=(HDC)
 * @param lpRect flags=no_out
 */
public static final native boolean DrawFocusRect (long hDC, RECT lpRect);
/**
 * @param hdc cast=(HDC)
 * @param lprc flags=no_out
 */
public static final native boolean DrawFrameControl (long hdc, RECT lprc, int uType, int uState);
/**
 * @param hdc cast=(HDC)
 * @param hIcon cast=(HICON)
 * @param hbrFlickerFreeDraw cast=(HBRUSH)
 */
public static final native boolean DrawIconEx (long hdc, int xLeft, int yTop, long hIcon, int cxWidth, int cyWidth, int istepIfAniCur, long hbrFlickerFreeDraw, int diFlags);
/** @param hWnd cast=(HWND) */
public static final native boolean DrawMenuBar (long hWnd);
/**
 * @param hDC cast=(HDC)
 * @param lpString cast=(LPWSTR),flags=no_out critical
 */
public static final native int DrawText (long hDC, char [] lpString, int nCount, RECT lpRect, int uFormat);
/**
 * @param hTheme cast=(HTHEME)
 * @param hdc cast=(HDC)
 * @param pRect flags=no_out
 * @param pClipRect flags=no_out
 */
public static final native int DrawThemeBackground (long hTheme, long hdc, int iPartId, int iStateId, RECT pRect, RECT pClipRect);
/**
 * @param hTheme cast=(HTHEME)
 * @param hdc cast=(HDC)
 * @param pszText flags=no_out
 * @param pRect flags=no_out
 */
public static final native int DrawThemeText (long hTheme, long hdc, int iPartId, int iStateId, char[] pszText, int iCharCount, int dwTextFlags, int dwTextFlags2, RECT pRect);
/** @param hwnd cast=(HWND) */
public static final native boolean DwmSetWindowAttribute (long hwnd, int dwAttribute, int[] pvAttribute, int cbAttribute);
/** @param hdc cast=(HDC) */
public static final native boolean Ellipse (long hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect);
/** @param hMenu cast=(HMENU) */
public static final native boolean EnableMenuItem (long hMenu, int uIDEnableItem, int uEnable);
/** @param hWnd cast=(HWND) */
public static final native boolean EnableScrollBar (long hWnd, int wSBflags, int wArrows);
/** @param hWnd cast=(HWND) */
public static final native boolean EnableWindow (long hWnd, boolean bEnable);
/**
 * @param pLangGroupEnumProc cast=(LANGUAGEGROUP_ENUMPROCW)
 * @param lParam cast=(LONG_PTR)
 */
public static final native boolean EnumSystemLanguageGroups (long pLangGroupEnumProc, int dwFlags, long lParam);
/** @param lpLocaleEnumProc cast=(LOCALE_ENUMPROCW) */
public static final native boolean EnumSystemLocales (long lpLocaleEnumProc, int dwFlags);
/** @param hWinPosInfo cast=(HDWP) */
public static final native boolean EndDeferWindowPos (long hWinPosInfo);
/** @param hBufferedPaint cast=(HPAINTBUFFER) */
public static final native int EndBufferedPaint (long hBufferedPaint, boolean fUpdateTarget);
/** @param hdc cast=(HDC) */
public static final native int EndDoc (long hdc);
/** @param hdc cast=(HDC) */
public static final native int EndPage (long hdc);
/**
 * @param hWnd cast=(HWND)
 * @param lpPaint flags=no_out
 */
public static final native int EndPaint (long hWnd, PAINTSTRUCT lpPaint);
/**
 * @param hdc cast=(HDC)
 * @param lprcClip flags=no_out
 * @param lpfnEnum cast=(MONITORENUMPROC)
 * @param dwData cast=(LPARAM)
 */
public static final native boolean EnumDisplayMonitors (long hdc, RECT lprcClip, long lpfnEnum, int dwData);
/**
 * @param hdc cast=(HDC)
 * @param hemf cast=(HENHMETAFILE)
 * @param lpEnhMetaFunc cast=(ENHMFENUMPROC)
 * @param lpData cast=(LPVOID)
 */
public static final native boolean EnumEnhMetaFile(long hdc, long hemf, long lpEnhMetaFunc, long lpData, RECT lpRect);
/**
 * @param hdc cast=(HDC)
 * @param lpszFamily cast=(LPCWSTR),flags=no_out
 * @param lpEnumFontFamProc cast=(FONTENUMPROCW)
 * @param lParam cast=(LPARAM)
 */
public static final native int EnumFontFamilies (long hdc, char [] lpszFamily, long lpEnumFontFamProc, long lParam);
/**
 * @param lprc1 flags=no_out
 * @param lprc2 flags=no_out
 */
public static final native boolean EqualRect (RECT lprc1, RECT lprc2);
/** @param hdc cast=(HDC) */
public static final native int ExcludeClipRect (long hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect);
/** @param lpSrc flags=no_out */
public static final native int ExpandEnvironmentStrings (char [] lpSrc, char [] lsDst, int nSize);
/**
 * @param lplb flags=no_out
 * @param lpStyle cast=(CONST DWORD *)
 */
public static final native long ExtCreatePen (int dwPenStyle, int dwWidth, LOGBRUSH lplb, int dwStyleCount, int[] lpStyle);
/**
 * @param lpXform cast=(XFORM *)
 * @param lpRgnData cast=(CONST RGNDATA *)
 */
public static final native long ExtCreateRegion (float[] lpXform, int nCount, int[] lpRgnData);
/**
 * @param hdc cast=(HDC)
 * @param lprc flags=no_out
 * @param lpString cast=(LPWSTR),flags=no_out critical
 * @param lpDx cast=(CONST INT *),flags=no_out critical
 */
public static final native boolean ExtTextOut (long hdc, int X, int Y, int fuOptions, RECT lprc, char[] lpString, int cbCount, int[] lpDx);
/**
 * @param lpszFile cast=(LPWSTR)
 * @param phiconLarge cast=(HICON FAR *)
 * @param phiconSmall cast=(HICON FAR *)
 */
public static final native int ExtractIconEx (char [] lpszFile, int nIconIndex, long [] phiconLarge, long [] phiconSmall, int nIcons);
/**
 * @param hDC cast=(HDC)
 * @param lprc flags=no_out
 * @param hbr cast=(HBRUSH)
 */
public static final native int FillRect (long hDC, RECT lprc, long hbr);
/** @param dwLimit cast=(DWORD) */
public static final native int GdiSetBatchLimit (int dwLimit);
public static final native int GetACP ();
public static final native long GetActiveWindow ();
/** @param hDC cast=(HDC) */
public static final native int GetBkColor (long hDC);
public static final native long GetCapture ();
/** @param lpPoint flags=no_in */
public static final native boolean GetCaretPos (POINT lpPoint);
/**
 * @param hdc cast=(HDC)
 * @param lpabc cast=(LPABC),flags=no_in critical
 */
public static final native boolean GetCharABCWidths (long hdc, int iFirstChar, int iLastChar, int [] lpabc);
/**
 * @param hdc cast=(HDC)
 * @param lpString cast=(LPWSTR),flags=no_out critical
 */
public static final native int GetCharacterPlacement (long hdc, char[] lpString, int nCount, int nMaxExtent, GCP_RESULTS lpResults, int dwFlags);
/**
 * @param hdc cast=(HDC)
 * @param lpBuffer cast=(LPINT),flags=no_in critical
 */
public static final native boolean GetCharWidth (long hdc, int iFirstChar, int iLastChar, int [] lpBuffer);
/**
 * @param hInstance cast=(HINSTANCE)
 * @param lpClassName cast=(LPWSTR),flags=no_out
 */
public static final native boolean GetClassInfo (long hInstance, char [] lpClassName, WNDCLASS lpWndClass);
/** @param hWnd cast=(HWND) */
public static final native int GetClassName (long hWnd, char [] lpClassName, int nMaxCount);
/**
 * @param hWnd cast=(HWND)
 * @param lpRect flags=no_in
 */
public static final native boolean GetClientRect (long hWnd, RECT lpRect);
public static final native long GetClipboardData (int uFormat);
/** @param lpszFormatName cast=(LPWSTR) */
public static final native int GetClipboardFormatName (int format, char[] lpszFormatName, int cchMaxCount);
/**
 * @param hdc cast=(HDC)
 * @param lprc flags=no_in
 */
public static final native int GetClipBox (long hdc, RECT lprc);
/**
 * @param hdc cast=(HDC)
 * @param hrgn cast=(HRGN)
 */
public static final native int GetClipRgn (long hdc, long hrgn);
/** @param hwndCombo cast=(HWND) */
public static final native boolean GetComboBoxInfo (long hwndCombo, COMBOBOXINFO pcbi);
/** @param hdc cast=(HDC) */
public static final native long GetCurrentObject (long hdc, int uObjectType);
public static final native int GetCurrentProcessId ();
public static final native int GetCurrentThreadId ();
/** @param AppID cast=(PWSTR *) */
public static final native int GetCurrentProcessExplicitAppUserModelID(long[] AppID);
public static final native long GetCursor ();
public static final native boolean GetCursorPos (POINT lpPoint);
/** @param hwnd cast=(HWND) */
public static final native long GetDC (long hwnd);
/**
 * @param hWnd cast=(HWND)
 * @param hrgnClip cast=(HRGN)
 */
public static final native long GetDCEx (long hWnd, long hrgnClip, int flags);
public static final native long GetDesktopWindow ();
/** @param hdc cast=(HDC) */
public static final native int GetDeviceCaps (long hdc, int nIndex);
public static final native int GetDialogBaseUnits ();
/**
 * @param hdc cast=(HDC)
 * @param pColors cast=(RGBQUAD *),flags=no_in critical
 */
public static final native int GetDIBColorTable (long hdc, int uStartIndex, int cEntries, byte[] pColors);
/**
 * @param hdc cast=(HDC)
 * @param hbmp cast=(HBITMAP)
 * @param lpvBits cast=(LPVOID),flags=critical
 * @param lpbi cast=(LPBITMAPINFO),flags=critical
 */
public static final native int GetDIBits (long hdc, long hbmp, int uStartScan, int cScanLines, byte[] lpvBits, byte[] lpbi, int uUsage);
/** @param hDlg cast=(HWND) */
public static final native long GetDlgItem (long hDlg, int nIDDlgItem);
public static final native int GetDoubleClickTime ();
/** @method flags=dynamic */
public static final native int GetDpiForMonitor (long hmonitor, int dpiType, int [] dpiX, int [] dpiY);
public static final native long GetFocus ();
/** @param hdc cast=(HDC) */
public static final native int GetFontLanguageInfo (long hdc);
public static final native long GetForegroundWindow ();
/** @param hGestureInfo cast=(HGESTUREINFO) */
public static final native boolean GetGestureInfo(long hGestureInfo, GESTUREINFO pGestureInfo);
/** @param hdc cast=(HDC) */
public static final native int GetGraphicsMode (long hdc);
/**
 * @param hdc cast=(HDC)
 * @param lpstr flags=no_out
 * @param pgi cast=(LPWORD)
 */
public static final native int GetGlyphIndices (long hdc, char[] lpstr, int c, short[] pgi, int fl);
/**
 * @param idThread cast=(DWORD)
 * @param lpgui cast=(LPGUITHREADINFO)
 */
public static final native boolean GetGUIThreadInfo (int idThread, GUITHREADINFO lpgui);
/**
 * @param hIcon cast=(HICON)
 * @param piconinfo flags=no_in
 */
public static final native boolean GetIconInfo (long hIcon, ICONINFO piconinfo);
/** @param lpList cast=(HKL FAR *) */
public static final native int GetKeyboardLayoutList (int nBuff, long [] lpList);
public static final native long GetKeyboardLayout (int idThread);
public static final native short GetKeyState (int nVirtKey);
/** @param lpKeyState cast=(PBYTE) */
public static final native boolean GetKeyboardState (byte [] lpKeyState);
/** @param hWnd cast=(HWND) */
public static final native long GetLastActivePopup (long hWnd);
public static final native int GetLastError ();
/**
 * @param hwnd cast=(HWND)
 * @param pcrKey cast=(COLORREF *)
 * @param pbAlpha cast=(BYTE *)
 * @param pdwFlags cast=(DWORD *)
 */
public static final native boolean GetLayeredWindowAttributes (long hwnd, int [] pcrKey, byte [] pbAlpha, int [] pdwFlags);
/** @param hdc cast=(HDC) */
public static final native int GetLayout (long hdc);
/* returns the instance handle to the swt library */
/** @method flags=no_gen */
public static final native long GetLibraryHandle ();
/** @param lpLCData cast=(LPWSTR) */
public static final native int GetLocaleInfo (int Locale, int LCType, char [] lpLCData, int cchData);
/** @param hWnd cast=(HWND) */
public static final native long GetMenu (long hWnd);
/** @param hWnd cast=(HWND) */
public static final native boolean GetMenuBarInfo (long hWnd, int idObject, int idItem, MENUBARINFO pmbi);
/** @param hMenu cast=(HMENU) */
public static final native int GetMenuDefaultItem (long hMenu, int fByPos, int gmdiFlags);
/** @param hmenu cast=(HMENU) */
public static final native boolean GetMenuInfo (long hmenu, MENUINFO lpcmi);
/** @param hMenu cast=(HMENU) */
public static final native int GetMenuItemCount (long hMenu);
/**
 * @param hMenu cast=(HMENU)
 * @param lpmii cast=(LPMENUITEMINFOW)
 */
public static final native boolean GetMenuItemInfo (long hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii);
/**
 * @param hWnd cast=(HWND)
 * @param hMenu cast=(HMENU)
 * @param lprcItem flags=no_in
 */
public static final native boolean GetMenuItemRect (long hWnd, long hMenu, int uItem, RECT lprcItem);
/**
 * @param lpMsg flags=no_in
 * @param hWnd cast=(HWND)
 */
public static final native boolean GetMessage (MSG lpMsg, long hWnd, int wMsgFilterMin, int wMsgFilterMax);
public static final native int GetMessagePos ();
public static final native int GetMessageTime ();
/**
 * @param hdc cast=(HDC)
 * @param hrgn cast=(HRGN)
 */
public static final native int GetMetaRgn (long hdc, long hrgn);
/**
 * @param hTheme cast=(HTHEME)
 * @param hdc cast=(HDC)
 * @param prc flags=no_out
 * @param psz flags=no_in
 */
public static final native int GetThemePartSize(long hTheme, long hdc, int iPartId, int iStateId, RECT prc, int eSize, SIZE psz);
/**
 * @param hTheme cast=(HTHEME)
 * @param hdc cast=(HDC)
 * @param pszText flags=no_out
 * @param pBoundingRect flags=no_out
 * @param pExtentRect flags=no_in
 */
public static final native int GetThemeTextExtent (long hTheme, long hdc, int iPartId, int iStateId, char[] pszText, int iCharCount, int dwTextFlags, RECT pBoundingRect, RECT pExtentRect);
/**
 * @param hModule cast=(HMODULE)
 * @param lpFilename cast=(LPWSTR)
 */
public static final native int GetModuleFileName (long hModule, char [] lpFilename, int inSize);
/** @param lpModuleName cast=(LPWSTR),flags=no_out */
public static final native long GetModuleHandle (char [] lpModuleName);
/** @param hmonitor cast=(HMONITOR) */
public static final native boolean GetMonitorInfo (long hmonitor, MONITORINFO lpmi);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObject (long hgdiobj, int cbBuffer, BITMAP lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObject (long hgdiobj, int cbBuffer, DIBSECTION lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObject (long hgdiobj, int cbBuffer, LOGBRUSH lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject flags=no_in
 */
public static final native int GetObject (long hgdiobj, int cbBuffer, LOGFONT lpvObject);
/**
 * @param hgdiobj cast=(HGDIOBJ)
 * @param lpvObject cast=(LPVOID),flags=no_in
 */
public static final native int GetObject (long hgdiobj, int cbBuffer, long lpvObject);
/** @param hdc cast=(HDC) */
public static final native int GetOutlineTextMetrics (long hdc, int cbData, OUTLINETEXTMETRIC lpOTM);
/** @param hWnd cast=(HWND) */
public static final native long GetParent (long hWnd);
/** @param hdc cast=(HDC) */
public static final native int GetPixel (long hdc, int x, int y);
/** @param hdc cast=(HDC) */
public static final native int GetPolyFillMode (long hdc);
/**
 * @param pPrinterName cast=(LPWSTR),flags=no_out
 * @param phPrinter cast=(LPHANDLE)
 * @param pDefault cast=(LPPRINTER_DEFAULTSW)
 */
public static final native boolean OpenPrinter (char[] pPrinterName, long [] phPrinter, long pDefault);
public static final native long GetProcessHeap ();
/**
 * @param lpAppName cast=(LPWSTR),flags=no_out
 * @param lpDefault cast=(LPWSTR),flags=no_out
 * @param lpKeyName cast=(LPWSTR),flags=no_out
 * @param lpReturnedString cast=(LPWSTR)
 */
public static final native int GetProfileString (char [] lpAppName, char [] lpKeyName, char [] lpDefault, char [] lpReturnedString, int nSize);
/**
 * @param hWnd cast=(HWND)
 * @param lpString cast=(LPCWSTR)
 */
public static final native long GetProp (long hWnd, long lpString);
/**
 * @param hdc cast=(HDC)
 * @param hrgn cast=(HRGN)
 */
public static final native int GetRandomRgn (long hdc, long hrgn, int iNum);
/**
 * @param hRgn cast=(HRGN)
 * @param lpRgnData cast=(RGNDATA *),flags=no_in critical
 */
public static final native int GetRegionData (long hRgn, int dwCount, int [] lpRgnData);
/**
 * @param hrgn cast=(HRGN)
 * @param lprc flags=no_in
 */
public static final native int GetRgnBox (long hrgn, RECT lprc);
/** @param hdc cast=(HDC) */
public static final native int GetROP2 (long hdc);
/** @param hwnd cast=(HWND) */
public static final native boolean GetScrollBarInfo (long hwnd, int idObject, SCROLLBARINFO psbi);
/** @param hwnd cast=(HWND) */
public static final native boolean GetScrollInfo (long hwnd, int flags, SCROLLINFO info);
public static final native void GetStartupInfo (STARTUPINFO lpStartupInfo);
public static final native long GetStockObject (int fnObject);
public static final native int GetSysColor (int nIndex);
public static final native long GetSysColorBrush (int nIndex);
/** @param hWnd cast=(HWND) */
public static final native long GetSystemMenu (long hWnd, boolean bRevert);
public static final native int GetSystemMetrics (int nIndex);
/** @param hDC cast=(HDC) */
public static final native int GetTextColor (long hDC);
/**
 * @param hdc cast=(HDC)
 * @param lpString cast=(LPWSTR),flags=no_out critical
 * @param lpSize flags=no_in
 */
public static final native boolean GetTextExtentPoint32 (long hdc, char [] lpString, int cbString, SIZE lpSize);
/**
 * @param hdc cast=(HDC)
 * @param lptm flags=no_in
 */
public static final native boolean GetTextMetrics (long hdc, TEXTMETRIC lptm);
/**
 * @param hTouchInput cast=(HTOUCHINPUT)
 * @param cInputs cast=(UINT)
 * @param pTouchInputs cast=(PTOUCHINPUT)
 */
public static final native boolean GetTouchInputInfo(long hTouchInput, int cInputs, long pTouchInputs, int cbSize);
/**
 * @param hWnd cast=(HWND)
 * @param lpRect flags=no_in
 * @param bErase cast=(BOOL)
 */
public static final native boolean GetUpdateRect (long hWnd, RECT lpRect, boolean bErase);
/**
 * @param hWnd cast=(HWND)
 * @param hRgn cast=(HRGN)
 */
public static final native int GetUpdateRgn (long hWnd, long hRgn, boolean bErase);
/** @param hWnd cast=(HWND) */
public static final native long GetWindow (long hWnd, int uCmd);
/** @param hWnd cast=(HWND) */
public static final native int GetWindowLong (long hWnd, int nIndex);
/** @param hWnd cast=(HWND) */
public static final native long GetWindowLongPtr (long hWnd, int nIndex);
/** @param hWnd cast=(HWND) */
public static final native long GetWindowDC (long hWnd);
/** @param hdc cast=(HDC) */
public static final native boolean GetWindowOrgEx (long hdc, POINT lpPoint);
/** @param hWnd cast=(HWND) */
public static final native boolean GetWindowPlacement (long hWnd, WINDOWPLACEMENT lpwndpl);
/** @param hWnd cast=(HWND) */
public static final native boolean GetWindowRect (long hWnd, RECT lpRect);
/**
 * @param hWnd cast=(HWND)
 * @param hRgn cast=(HRGN)
 */
public static final native int GetWindowRgn (long hWnd, long hRgn);
/**
 * @param hWnd cast=(HWND)
 * @param lpString cast=(LPWSTR)
 */
public static final native int GetWindowText (long hWnd, char [] lpString, int nMaxCount);
/** @param hWnd cast=(HWND) */
public static final native int GetWindowTextLength (long hWnd);
/**
 * @param hWnd cast=(HWND)
 * @param lpdwProcessId cast=(LPDWORD)
 */
public static final native int GetWindowThreadProcessId (long hWnd, int [] lpdwProcessId);
public static final native double GID_ROTATE_ANGLE_FROM_ARGUMENT(long dwArgument);
/** @param lpString cast=(LPCWSTR),flags=no_out */
public static final native int GlobalAddAtom (char [] lpString);
public static final native long GlobalAlloc (int uFlags, int dwBytes);
/** @param hMem cast=(HANDLE) */
public static final native long GlobalFree (long hMem);
/** @param hMem cast=(HANDLE) */
public static final native long GlobalLock (long hMem);
/** @param hMem cast=(HANDLE) */
public static final native int GlobalSize (long hMem);
/** @param hMem cast=(HANDLE) */
public static final native boolean GlobalUnlock (long hMem);
/**
 * @param hdc cast=(HDC)
 * @param pVertex cast=(PTRIVERTEX)
 * @param dwNumVertex cast=(ULONG)
 * @param pMesh cast=(PVOID)
 * @param dwNumMesh cast=(ULONG)
 * @param dwMode cast=(ULONG)
 */
public static final native boolean GradientFill (long hdc, long pVertex, int dwNumVertex, long pMesh, int dwNumMesh, int dwMode);
/** @param hHeap cast=(HANDLE) */
public static final native long HeapAlloc (long hHeap, int dwFlags, int dwBytes);
/**
 * @param hHeap cast=(HANDLE)
 * @param lpMem cast=(LPVOID)
 */
public static final native boolean HeapFree (long hHeap, int dwFlags, long lpMem);
/** @param hWnd cast=(HWND) */
public static final native boolean HideCaret (long hWnd);
/**
 * @param lpsz cast=(LPOLESTR),flags=no_out
 * @param lpiid cast=(LPIID)
 */
public static final native int IIDFromString (char[] lpsz, byte[] lpiid);
/**
 * @param pidl cast=(PCIDLIST_ABSOLUTE)
 */
public static final native int ILGetSize(long pidl);
/**
 * @param himl cast=(HIMAGELIST)
 * @param hbmImage cast=(HBITMAP)
 * @param hbmMask cast=(HBITMAP)
 */
public static final native int ImageList_Add (long himl, long hbmImage, long hbmMask);
/**
 * @param himl cast=(HIMAGELIST)
 * @param hbmImage cast=(HBITMAP)
 * @param crMask cast=(COLORREF)
 */
public static final native int ImageList_AddMasked (long himl, long hbmImage, int crMask);
/** @param himl cast=(HIMAGELIST) */
public static final native boolean ImageList_BeginDrag (long himl, int iTrack, int dxHotspot, int dyHotspot);
public static final native long ImageList_Create (int cx, int cy, int flags, int cInitial, int cGrow);
/** @param himl cast=(HIMAGELIST) */
public static final native boolean ImageList_Destroy (long himl);
/** @param hwndLock cast=(HWND) */
public static final native boolean ImageList_DragEnter (long hwndLock, int x, int y);
/** @param hwndLock cast=(HWND) */
public static final native boolean ImageList_DragLeave (long hwndLock);
public static final native boolean ImageList_DragMove (int x, int y);
/** @param fShow cast=(BOOL) */
public static final native boolean ImageList_DragShowNolock (boolean fShow);
public static final native void ImageList_EndDrag ();
/**
 * @param himl cast=(HIMAGELIST)
 * @param cx cast=(int *)
 * @param cy cast=(int *)
 */
public static final native boolean ImageList_GetIconSize (long himl, int [] cx, int [] cy);
/** @param himl cast=(HIMAGELIST) */
public static final native int ImageList_GetImageCount (long himl);
/** @param himl cast=(HIMAGELIST) */
public static final native boolean ImageList_Remove (long himl, int i);
/**
 * @param himl cast=(HIMAGELIST)
 * @param hbmImage cast=(HBITMAP)
 * @param hbmMask cast=(HBITMAP)
 */
public static final native boolean ImageList_Replace (long himl, int i, long hbmImage, long hbmMask);
/**
 * @param himl cast=(HIMAGELIST)
 * @param hicon cast=(HICON)
 */
public static final native int ImageList_ReplaceIcon (long himl, int i, long hicon);
/** @param himl cast=(HIMAGELIST) */
public static final native boolean ImageList_SetIconSize (long himl, int cx, int cy);
/**
 * @param hKL cast=(HKL)
 * @param hIMC cast=(HIMC)
 * @param lpData cast=(LPVOID)
 */
public static final native long ImmEscape (long hKL, long hIMC, int uEscape, char[] lpData);
/**
 * @param hIMC cast=(HIMC)
 * @param lplf flags=no_in
 */
public static final native boolean ImmGetCompositionFont (long hIMC, LOGFONT lplf);
/**
 * @param hIMC cast=(HIMC)
 * @param lpBuf cast=(LPWSTR)
 */
public static final native int ImmGetCompositionString (long hIMC, int dwIndex, char [] lpBuf, int dwBufLen);
/**
 * @param hIMC cast=(HIMC)
 * @param lpBuf cast=(LPWSTR)
 */
public static final native int ImmGetCompositionString (long hIMC, int dwIndex, int [] lpBuf, int dwBufLen);
/**
 * @param hIMC cast=(HIMC)
 * @param lpBuf cast=(LPWSTR)
 */
public static final native int ImmGetCompositionString (long hIMC, int dwIndex, byte [] lpBuf, int dwBufLen);
/** @param hWnd cast=(HWND) */
public static final native long ImmGetContext (long hWnd);
/**
 * @param hIMC cast=(HIMC)
 * @param lpfdwConversion cast=(LPDWORD)
 * @param lpfdwSentence cast=(LPDWORD)
 */
public static final native boolean ImmGetConversionStatus (long hIMC, int [] lpfdwConversion, int [] lpfdwSentence);
/** @param hWnd cast=(HWND) */
public static final native long ImmGetDefaultIMEWnd (long hWnd);
/** @param hIMC cast=(HIMC) */
public static final native boolean ImmGetOpenStatus (long hIMC);
/** @param hIMC cast=(HIMC) */
public static final native boolean ImmNotifyIME (long hIMC, int dwAction, int dwIndex, int dwValue);
/**
 * @param hWnd cast=(HWND)
 * @param hIMC cast=(HIMC)
 */
public static final native boolean ImmReleaseContext (long hWnd, long hIMC);
/**
 * @param hIMC cast=(HIMC)
 * @param lplf flags=no_out
 */
public static final native boolean ImmSetCompositionFont (long hIMC, LOGFONT lplf);
/**
 * @param hIMC cast=(HIMC)
 * @param lpCompForm flags=no_out
 */
public static final native boolean ImmSetCompositionWindow (long hIMC, COMPOSITIONFORM lpCompForm);
/**
 * @param hIMC cast=(HIMC)
 * @param lpCandidate flags=no_out
 */
public static final native boolean ImmSetCandidateWindow (long hIMC, CANDIDATEFORM lpCandidate);
/** @param hIMC cast=(HIMC) */
public static final native boolean ImmSetConversionStatus (long hIMC, int fdwConversion, int dwSentence);
/** @param hIMC cast=(HIMC) */
public static final native boolean ImmSetOpenStatus (long hIMC, boolean fOpen);
/** @param lpInitCtrls flags=no_out */
public static final native boolean InitCommonControlsEx (INITCOMMONCONTROLSEX lpInitCtrls);
/**
 * @param hMenu cast=(HMENU)
 * @param lpmii flags=no_out
 */
public static final native boolean InsertMenuItem (long hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii);
/**
 * @param lpszUrl cast=(LPCWSTR),flags=no_out
 * @param lpszCookieName cast=(LPCWSTR),flags=no_out
 * @param lpszCookieData cast=(LPWSTR)
 * @param lpdwSize cast=(LPDWORD)
 */
public static final native boolean InternetGetCookie (char[] lpszUrl, char[] lpszCookieName, char[] lpszCookieData, int[] lpdwSize);
/**
 * @param lpszUrl cast=(LPCWSTR),flags=no_out
 * @param lpszCookieName cast=(LPCWSTR),flags=no_out
 * @param lpszCookieData cast=(LPCWSTR),flags=no_out
 */
public static final native boolean InternetSetCookie (char[] lpszUrl, char[] lpszCookieName, char[] lpszCookieData);
/**
 * @param hInternet cast=(HINTERNET)
 * @param lpBuffer cast=(LPVOID)
 */
public static final native boolean InternetSetOption (long hInternet, int dwOption, long lpBuffer, int dwBufferLength);
/** @param hdc cast=(HDC) */
public static final native int IntersectClipRect (long hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect);
/**
 * @param lprcDst flags=no_in
 * @param lprcSrc1 flags=no_out
 * @param lprcSrc2 flags=no_out
 */
public static final native boolean IntersectRect (RECT lprcDst, RECT lprcSrc1, RECT lprcSrc2);
/** @param hWnd cast=(HWND) */
public static final native boolean InvalidateRect (long hWnd, RECT lpRect, boolean bErase);
/**
 * @param hWnd cast=(HWND)
 * @param hRgn cast=(HRGN)
 */
public static final native boolean InvalidateRgn (long hWnd, long hRgn, boolean bErase);
public static final native boolean IsAppThemed ();
/** @method flags=no_gen */
public static final native boolean IsDarkModeAvailable();
/** @param hWnd cast=(HWND) */
public static final native boolean IsHungAppWindow (long hWnd);
/** @param hWnd cast=(HWND) */
public static final native boolean IsIconic (long hWnd);
/**
 * @param hWnd cast=(HWND)
 * @param outFlags cast=(PULONG)
 */
public static final native boolean IsTouchWindow (long hWnd, long[] outFlags);
/** @param hWnd cast=(HWND) */
public static final native boolean IsWindowEnabled (long hWnd);
/** @param hWnd cast=(HWND) */
public static final native boolean IsWindowVisible (long hWnd);
/** @param hWnd cast=(HWND) */
public static final native boolean IsZoomed (long hWnd);
/** @param hWnd cast=(HWND) */
public static final native boolean KillTimer (long hWnd, long uIDEvent);
/** @param hdc cast=(HDC) */
public static final native boolean LineTo (long hdc, int x1, int x2);
/**
 * @param hInstance cast=(HINSTANCE)
 * @param lpBitmapName cast=(LPWSTR)
 */
public static final native long LoadBitmap (long hInstance, long lpBitmapName);
/**
 * @param hInstance cast=(HINSTANCE)
 * @param lpCursorName cast=(LPWSTR)
 */
public static final native long LoadCursor (long hInstance, long lpCursorName);
/**
 * @param hInstance cast=(HINSTANCE)
 * @param lpIconName cast=(LPWSTR)
 */
public static final native long LoadIcon (long hInstance, long lpIconName);
/**
 * @param hinst cast=(HINSTANCE)
 * @param pszName cast=(PCWSTR)
 * @param phico cast=(HICON *)
 */
public static final native int LoadIconMetric (long hinst, long pszName, int lims, long [] phico);
/**
 * @param hinst cast=(HINSTANCE)
 * @param lpszName cast=(LPWSTR)
 */
public static final native long LoadImage (long hinst, long lpszName, int uType, int cxDesired, int cyDesired, int fuLoad);
/**
 * @param pwszKLID cast=(LPCWSTR)
 * @param Flags cast=(UINT)
 */
public static final native long LoadKeyboardLayout(char [] pwszKLID, long Flags);
/** @param hMem cast=(HLOCAL) */
public static final native long LocalFree (long hMem);
/** @param hdc cast=(HDC) */
public static final native boolean LPtoDP (long hdc, POINT lpPoints, int nCount);
public static final native int MapVirtualKey (int uCode, int uMapType);
/**
 * @param hWndFrom cast=(HWND)
 * @param hWndTo cast=(HWND)
 */
public static final native int MapWindowPoints (long hWndFrom, long hWndTo, POINT lpPoints, int cPoints);
/**
 * @param hWndFrom cast=(HWND)
 * @param hWndTo cast=(HWND)
 * @param lpPoints cast=(LPPOINT)
 */
public static final native int MapWindowPoints (long hWndFrom, long hWndTo, RECT lpPoints, int cPoints);
public static final native boolean MessageBeep (int uType);
/**
 * @param hWnd cast=(HWND)
 * @param lpText cast=(LPWSTR),flags=no_out
 * @param lpCaption cast=(LPWSTR),flags=no_out
 */
public static final native int MessageBox (long hWnd, char [] lpText, char [] lpCaption, int uType);
/**
 * @param hdc cast=(HDC)
 * @param lpXform cast=(XFORM *)
 */
public static final native boolean ModifyWorldTransform(long hdc, float [] lpXform, int iMode);
/** @param hwnd cast=(HWND) */
public static final native long MonitorFromWindow (long hwnd, int dwFlags);
/**
 * @param Destination cast=(PVOID),flags=no_in critical
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (char[] Destination, long SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in critical
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (byte [] Destination, long Source, int Length);
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
public static final native void MoveMemory (int [] Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in critical
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (long [] Destination, long SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in critical
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (double[] Destination, long SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in critical
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (float[] Destination, long SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in critical
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (short[] Destination, long SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (long Destination, byte [] Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (long Destination, char [] Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (long Destination, int [] Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (long Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, DEVMODE Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, DOCHOSTUIINFO Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, GRADIENT_RECT Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, LOGFONT Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, MEASUREITEMSTRUCT Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, MINMAXINFO Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, MSG Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, UDACCEL Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, NMTTDISPINFO Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, RECT Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, SAFEARRAY Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (SAFEARRAY Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, TRIVERTEX Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, WINDOWPOS Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (BITMAPINFOHEADER Destination, byte [] Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (BITMAPINFOHEADER Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (DEVMODE Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (DOCHOSTUIINFO Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (DRAWITEMSTRUCT Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (FLICK_DATA Destination, long [] Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (FLICK_POINT Destination, long [] Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (HDITEM Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (HELPINFO Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (LOGFONT Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (MEASUREITEMSTRUCT Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (MINMAXINFO Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (POINT Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (POINT Destination, long[] Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMHDR Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMCUSTOMDRAW Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMLVCUSTOMDRAW Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTBCUSTOMDRAW Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTBHOTITEM Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTREEVIEW Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTVCUSTOMDRAW Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTVITEMCHANGE Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMUPDOWN Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, NMLVCUSTOMDRAW Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, NMTBCUSTOMDRAW Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, NMTVCUSTOMDRAW Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, NMLVDISPINFO Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, NMTVDISPINFO Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMLVDISPINFO Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTVDISPINFO Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMLVODSTATECHANGE Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMHEADER Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMLINK Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMLISTVIEW Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMREBARCHILDSIZE Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMREBARCHEVRON Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTOOLBAR Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTTCUSTOMDRAW Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (NMTTDISPINFO Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (EMR Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (EMREXTCREATEFONTINDIRECTW Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, SHDRAGIMAGE Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (TEXTMETRIC Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (TOUCHINPUT Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (WINDOWPOS Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (MSG Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (UDACCEL Destination, long Source, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, DROPFILES Source, int Length);
/**
 * @param DestinationPtr cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (long DestinationPtr, double[] Source, int Length);
/**
 * @param DestinationPtr cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (long DestinationPtr, float[] Source, int Length);
/**
 * @param DestinationPtr cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (long DestinationPtr, long[] Source, int Length);
/**
 * @param DestinationPtr cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out critical
 */
public static final native void MoveMemory (long DestinationPtr, short[] Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (SCRIPT_ITEM Destination, long SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (SCRIPT_LOGATTR Destination, long SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param SourcePtr cast=(CONST VOID *)
 */
public static final native void MoveMemory (SCRIPT_PROPERTIES Destination, long SourcePtr, int Length);
/**
 * @param Destination cast=(PVOID)
 * @param Source cast=(CONST VOID *),flags=no_out
 */
public static final native void MoveMemory (long Destination, CIDA Source, int Length);
/**
 * @param Destination cast=(PVOID),flags=no_in
 * @param Source cast=(CONST VOID *)
 */
public static final native void MoveMemory (CIDA Destination, long Source, int Length);
/**
 * @param hdc cast=(HDC)
 * @param lPoint cast=(LPPOINT)
 */
public static final native boolean MoveToEx (long hdc, int x1, int x2, long lPoint);
/**
 * @param lpMultiByteStr cast=(LPCSTR),flags=no_out critical
 * @param lpWideCharStr cast=(LPWSTR),flags=no_in critical
 */
public static final native int MultiByteToWideChar (int CodePage, int dwFlags, byte [] lpMultiByteStr, int cchMultiByte, char [] lpWideCharStr, int cchWideChar);
/**
 * @param lpMultiByteStr cast=(LPCSTR)
 * @param lpWideCharStr cast=(LPWSTR),flags=no_in critical
 */
public static final native int MultiByteToWideChar (int CodePage, int dwFlags, long lpMultiByteStr, int cchMultiByte, char [] lpWideCharStr, int cchWideChar);
/**
 * @param event cast=(DWORD)
 * @param hwnd cast=(HWND)
 * @param idObject cast=(LONG)
 * @param idChild cast=(LONG)
 */
public static final native void NotifyWinEvent (int event, long hwnd, int idObject, int idChild);
public static final native boolean OffsetRect (RECT lprc, int dx, int dy);
/** @param hrgn cast=(HRGN) */
public static final native int OffsetRgn (long hrgn, int nXOffset, int nYOffset);
/** @param pvReserved cast=(LPVOID) */
public static final native int OleInitialize (long pvReserved);
public static final native void OleUninitialize ();
/** @param hWndNewOwner cast=(HWND) */
public static final native boolean OpenClipboard (long hWndNewOwner);
/**
 * @param hwnd cast=(HWND)
 * @param pszClassList cast=(LPCWSTR),flags=no_out
 */
public static final native long OpenThemeData (long hwnd, char[] pszClassList);
/** @param hdc cast=(HDC) */
public static final native boolean PatBlt (long hdc, int x1, int x2, int w, int h, int rop);
/** @param szfile cast=(LPCWSTR) */
public static final native boolean PathIsExe (long szfile);
/**
 * @param lpMsg flags=no_in
 * @param hWnd cast=(HWND)
 */
public static final native boolean PeekMessage (MSG lpMsg, long hWnd, int wMsgFilterMin, int wMsgFilterMax, int wRemoveMsg);
/** @param hdc cast=(HDC) */
public static final native boolean Pie (long hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect, int nXStartArc, int nYStartArc, int nXEndArc, int nYEndArc);
/**
 * @param hdc cast=(HDC)
 * @param points cast=(CONST POINT *),flags=no_out critical
 */
public static final native boolean Polygon (long hdc, int [] points, int nPoints);
/**
 * @param hdc cast=(HDC)
 * @param points cast=(CONST POINT *),flags=no_out critical
 */
public static final native boolean Polyline (long hdc, int[] points, int nPoints);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native boolean PostMessage (long hWnd, int Msg, long wParam, long lParam);
/**
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native boolean PostThreadMessage (int idThread, int Msg, long wParam, long lParam);
public static final native boolean PrintDlg (PRINTDLG lppd);
/**
 * @param hwnd cast=(HWND)
 * @param hdcBlt cast=(HDC)
 */
public static final native boolean PrintWindow (long hwnd, long hdcBlt, int nFlags);
/**
 * @param pszString cast=(LPCWSTR),flags=no_out
 * @param pkey flags=no_in
 */
public static final native int PSPropertyKeyFromString (char[] pszString, PROPERTYKEY pkey);
/**
 * @param rect flags=no_out
 * @param pt flags=no_out struct
 */
public static final native boolean PtInRect (RECT rect, POINT pt);
/** @param hrgn cast=(HRGN) */
public static final native boolean PtInRegion (long hrgn, int X, int Y);
/** @param hdc cast=(HDC) */
public static final native boolean Rectangle (long hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect);
/**
 * @param hrgn cast=(HRGN)
 * @param lprc flags=no_out
 */
public static final native boolean RectInRegion (long hrgn, RECT lprc);
/**
 * @param hWnd cast=(HWND)
 * @param lprcUpdate flags=no_out
 * @param hrgnUpdate cast=(HRGN)
 */
public static final native boolean RedrawWindow (long hWnd, RECT lprcUpdate, long hrgnUpdate, int flags);
/** @param hKey cast=(HKEY) */
public static final native int RegCloseKey (long hKey);
/**
 * @param hKey cast=(HKEY)
 * @param lpSubKey cast=(LPCWSTR),flags=no_out
 * @param lpClass cast=(LPWSTR),flags=no_out
 * @param lpSecurityAttributes cast=(LPSECURITY_ATTRIBUTES)
 * @param phkResult cast=(PHKEY)
 * @param lpdwDisposition cast=(LPDWORD)
 */
public static final native int RegCreateKeyEx (long hKey, char[] lpSubKey, int Reserved, char[] lpClass, int dwOptions, int samDesired, long lpSecurityAttributes, long[] phkResult, long[] lpdwDisposition);
/**
 * @param hKey cast=(HKEY)
 * @param lpValueName cast=(LPCWSTR),flags=no_out
 */
public static final native int RegDeleteValue (long hKey, char[] lpValueName);
/**
 * @param hKey cast=(HKEY)
 * @param lpName cast=(LPWSTR)
 * @param lpcName cast=(LPDWORD)
 * @param lpReserved cast=(LPDWORD)
 * @param lpClass cast=(LPWSTR)
 * @param lpcClass cast=(LPDWORD)
 * @param lpftLastWriteTime cast=(PFILETIME)
 */
public static final native int RegEnumKeyEx (long hKey, int dwIndex, char [] lpName, int [] lpcName, int [] lpReserved, char [] lpClass, int [] lpcClass, long lpftLastWriteTime);
/** @param lpWndClass flags=no_out */
public static final native int RegisterClass (WNDCLASS lpWndClass);
/**
 * @param hWnd cast=(HWND)
 * @param fsModifiers cast=(UINT)
 * @param vk cast=(UINT)
 */
public static final native boolean RegisterHotKey(long hWnd, int id, int fsModifiers, int vk);
/**
 * @param hWnd cast=(HWND)
 * @param ulFlags cast=(ULONG)
 */
public static final native boolean RegisterTouchWindow(long hWnd, int ulFlags);
/** @param lpString cast=(LPCWSTR),flags=no_out */
public static final native int RegisterWindowMessage (char [] lpString);
/** @param lpszFormat cast=(LPCWSTR),flags=no_out */
public static final native int RegisterClipboardFormat (char[] lpszFormat);
/**
 * @param hKey cast=(HKEY)
 * @param lpSubKey cast=(LPCWSTR),flags=no_out
 * @param phkResult cast=(PHKEY)
 */
public static final native int RegOpenKeyEx (long hKey, char[] lpSubKey, int ulOptions, int samDesired, long[] phkResult);
/**
 * @param hKey cast=(HKEY)
 * @param lpValueName cast=(LPCWSTR),flags=no_out
 * @param lpReserved cast=(LPDWORD)
 * @param lpType cast=(LPDWORD)
 * @param lpData cast=(LPBYTE)
 * @param lpcbData cast=(LPDWORD)
 */
public static final native int RegQueryValueEx (long hKey, char[] lpValueName, long lpReserved, int[] lpType, char [] lpData, int[] lpcbData);
/**
 * @param hKey cast=(HKEY)
 * @param lpValueName cast=(LPCWSTR),flags=no_out
 * @param lpReserved cast=(LPDWORD)
 * @param lpType cast=(LPDWORD)
 * @param lpData cast=(LPBYTE)
 * @param lpcbData cast=(LPDWORD)
 */
public static final native int RegQueryValueEx (long hKey, char[] lpValueName, long lpReserved, int[] lpType, int [] lpData, int[] lpcbData);
/**
 * @param hKey cast=(HKEY)
 * @param lpValueName cast=(LPCWSTR),flags=no_out
 * @param lpData cast=(const BYTE*)
 */
public static final native int RegSetValueEx (long hKey, char[] lpValueName, int Reserved, int dwType, int[] lpData, int cbData);
public static final native boolean ReleaseCapture ();
/**
 * @param hWnd cast=(HWND)
 * @param hDC cast=(HDC)
 */
public static final native int ReleaseDC (long hWnd, long hDC);
/** @param hMenu cast=(HMENU) */
public static final native boolean RemoveMenu (long hMenu, int uPosition, int uFlags);
/**
 * @param hWnd cast=(HWND)
 * @param lpString cast=(LPCWSTR)
 */
public static final native long RemoveProp (long hWnd, long lpString);
public static final native boolean ReplyMessage (long lResult);
/**
 * @param hdc cast=(HDC)
 * @param nSavedDC cast=(int)
 */
public static final native boolean RestoreDC (long hdc, int nSavedDC);
/** @param hdc cast=(HDC) */
public static final native boolean RoundRect (long hdc, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect, int nWidth, int nHeight);
/** @method flags=dynamic */
public static final native int RtlGetVersion (OSVERSIONINFOEX lpVersionInformation);
/** @param hdc cast=(HDC) */
public static final native int SaveDC (long hdc);
/** @param hWnd cast=(HWND) */
public static final native boolean ScreenToClient (long hWnd, POINT lpPoint);
/**
 * @param psds cast=(const SCRIPT_DIGITSUBSTITUTE*)
 * @param psc cast=(SCRIPT_CONTROL*)
 * @param pss cast=(SCRIPT_STATE*)
 */
public static final native int ScriptApplyDigitSubstitution (long psds, SCRIPT_CONTROL psc, SCRIPT_STATE pss);
/**
 * @param pwcChars cast=(const WCHAR *),flags=no_out
 * @param psa cast=(const SCRIPT_ANALYSIS *),flags=no_out
 * @param psla cast=(SCRIPT_LOGATTR *)
 */
public static final native int ScriptBreak (char[] pwcChars, int cChars, SCRIPT_ANALYSIS psa, long psla);
/**
 * @param ppSp cast=(const SCRIPT_PROPERTIES ***)
 * @param piNumScripts cast=(int *)
 */
public static final native int ScriptGetProperties (long[] ppSp, int[] piNumScripts);
/**
 * @param hdc cast=(HDC)
 * @param psc cast=(SCRIPT_CACHE *)
 * @param tmHeight cast=(long *)
 */
public static final native int ScriptCacheGetHeight (long hdc, long psc, int[] tmHeight);
/**
 * @param pwLogClust cast=(const WORD *)
 * @param psva cast=(const SCRIPT_VISATTR *)
 * @param piAdvance cast=(const int *)
 * @param psa cast=(const SCRIPT_ANALYSIS *),flags=no_out
 * @param piX cast=(int *)
 */
public static final native int ScriptCPtoX (int iCP, boolean fTrailing, int cChars, int cGlyphs, long pwLogClust, long psva, long piAdvance, SCRIPT_ANALYSIS psa, int[] piX);
/** @param psc cast=(SCRIPT_CACHE *) */
public static final native int ScriptFreeCache (long psc);
/**
 * @param hdc cast=(HDC)
 * @param psc cast=(SCRIPT_CACHE *)
 * @param sfp cast=(SCRIPT_FONTPROPERTIES *)
 */
public static final native int ScriptGetFontProperties (long hdc, long psc, SCRIPT_FONTPROPERTIES sfp);
/**
 * @param psa cast=(const SCRIPT_ANALYSIS *),flags=no_out
 * @param piGlyphWidth cast=(const int *)
 * @param pwLogClust cast=(const WORD *)
 * @param psva cast=(const SCRIPT_VISATTR *)
 * @param piDx cast=(int *)
 */
public static final native int ScriptGetLogicalWidths (SCRIPT_ANALYSIS psa, int cChars, int cGlyphs, long piGlyphWidth, long pwLogClust, long psva, int[] piDx);
/**
 * @param pwcInChars cast=(const WCHAR *),flags=no_out
 * @param psControl cast=(const SCRIPT_CONTROL *),flags=no_out
 * @param psState cast=(const SCRIPT_STATE *),flags=no_out
 * @param pItems cast=(SCRIPT_ITEM *)
 * @param pcItems cast=(int *)
 */
public static final native int ScriptItemize (char[] pwcInChars, int cInChars, int cMaxItems, SCRIPT_CONTROL psControl, SCRIPT_STATE psState, long pItems, int[] pcItems);
/**
 * @param psva cast=(SCRIPT_VISATTR *)
 * @param piAdvance cast=(const int *)
 * @param piJustify cast=(int *)
 */
public static final native int ScriptJustify (long psva, long piAdvance, int cGlyphs, int iDx, int iMinKashida, long piJustify);
/**
 * @param pbLevel cast=(const BYTE *),flags=no_out
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
public static final native int ScriptPlace (long hdc, long psc, long pwGlyphs, int cGlyphs, long psva, SCRIPT_ANALYSIS psa, long piAdvance, long pGoffset, int[] pABC);
/**
 * @param hdc cast=(HDC)
 * @param psc cast=(SCRIPT_CACHE *)
 * @param pwcChars cast=(const WCHAR *),flags=no_out
 * @param pwOutGlyphs cast=(WORD*)
 */
public static final native int ScriptGetCMap (long hdc, long psc, char[] pwcChars, int cChars, int dwFlags, short[] pwOutGlyphs);
/**
 * @param hdc cast=(HDC)
 * @param psc cast=(SCRIPT_CACHE *)
 * @param pwcChars cast=(const WCHAR *),flags=no_out
 * @param psa cast=(SCRIPT_ANALYSIS *)
 * @param pwOutGlyphs cast=(WORD *)
 * @param pwLogClust cast=(WORD *)
 * @param psva cast=(SCRIPT_VISATTR *)
 * @param pcGlyphs cast=(int *)
 */
public static final native int ScriptShape (long hdc, long psc, char[] pwcChars, int cChars, int cMaxGlyphs, SCRIPT_ANALYSIS psa, long pwOutGlyphs, long pwLogClust, long psva, int[] pcGlyphs);
/**
 * @param hdc cast=(HDC)
 * @param pString cast=(const void*)
 * @param piDx cast=(const int*)
 * @param pTabdef cast=(SCRIPT_TABDEF*)
 * @param pbInClass cast=(const BYTE*)
 * @param pssa cast=(SCRIPT_STRING_ANALYSIS*)
 */
public static final native int ScriptStringAnalyse (long hdc, long pString, int cString, int cGlyphs, int iCharset, int dwFlags, int iReqWidth, SCRIPT_CONTROL psControl, SCRIPT_STATE psState, long piDx, long pTabdef, long pbInClass, long pssa);
/** @param ssa cast=(SCRIPT_STRING_ANALYSIS*),flags=struct */
public static final native int ScriptStringOut(long ssa, int iX, int iY, int uOptions, RECT prc, int iMinSel, int iMaxSel, boolean fDisabled);
/** @param pssa cast=(SCRIPT_STRING_ANALYSIS*) */
public static final native int ScriptStringFree(long pssa);
/**
 * @param hdc cast=(const HDC)
 * @param psc cast=(SCRIPT_CACHE *)
 * @param lprc cast=(const RECT *),flags=no_out
 * @param psa cast=(const SCRIPT_ANALYSIS *),flags=no_out
 * @param pwcReserved cast=(const WCHAR *)
 * @param pwGlyphs cast=(const WORD *)
 * @param piAdvance cast=(const int *)
 * @param piJustify cast=(const int *)
 * @param pGoffset cast=(const GOFFSET *)
 */
public static final native int ScriptTextOut (long hdc, long psc, int x, int y, int fuOptions, RECT lprc, SCRIPT_ANALYSIS psa, long pwcReserved, int iReserved, long pwGlyphs, int cGlyphs, long piAdvance, long piJustify, long pGoffset);
/**
 * @param pwLogClust cast=(const WORD *)
 * @param psva cast=(const SCRIPT_VISATTR *)
 * @param piAdvance cast=(const int *)
 * @param psa cast=(const SCRIPT_ANALYSIS *),flags=no_out
 * @param piCP cast=(int *)
 * @param piTrailing cast=(int *)
 */
public static final native int ScriptXtoCP (int iX, int cChars, int cGlyphs, long pwLogClust, long psva, long piAdvance, SCRIPT_ANALYSIS psa, int[] piCP, int[] piTrailing);
/**
 * @param hWnd cast=(HWND)
 * @param hrgnUpdate cast=(HRGN)
 */
public static final native int ScrollWindowEx (long hWnd, int dx, int dy, RECT prcScroll, RECT prcClip, long hrgnUpdate, RECT prcUpdate, int flags);
/**
 * @param hdc cast=(HDC)
 * @param hrgn cast=(HRGN)
 */
public static final native int SelectClipRgn (long hdc, long hrgn);
/**
 * @param hDC cast=(HDC)
 * @param HGDIObj cast=(HGDIOBJ)
 */
public static final native long SelectObject (long hDC, long HGDIObj);
/** @param pInputs flags=no_out */
public static final native int SendInput (int nInputs, INPUT pInputs, int cbSize);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, int [] wParam, int [] lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, char [] lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, int [] lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, long lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, LVCOLUMN lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, LVHITTESTINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, LITEM lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, LVITEM lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, LVINSERTMARK lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, MARGINS lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, MCHITTESTINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, REBARBANDINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, RECT lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, SYSTEMTIME lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, SHDRAGIMAGE lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, TBBUTTON lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, TBBUTTONINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, TCITEM lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, TCHITTESTINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, TOOLINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, TVHITTESTINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, TVINSERTSTRUCT lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, TVITEM lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, TVSORTCB lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, UDACCEL lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, HDHITTESTINFO lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, HDITEM lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, HDLAYOUT lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, BUTTON_IMAGELIST lParam);
/**
 * @param hWnd cast=(HWND)
 * @param wParam cast=(WPARAM)
 * @param lParam cast=(LPARAM)
 */
public static final native long SendMessage (long hWnd, int Msg, long wParam, SIZE lParam);
/** @param hWnd cast=(HWND) */
public static final native long SetActiveWindow (long hWnd);
/**
 * @param hdc cast=(HDC)
 * @param colorRef cast=(COLORREF)
 */
public static final native int SetBkColor (long hdc, int colorRef);
/** @param hdc cast=(HDC) */
public static final native int SetBkMode (long hdc, int mode);
/**
 * @param hdc cast=(HDC)
 * @param lppt flags=no_in
 */
public static final native boolean SetBrushOrgEx (long hdc, int nXOrg, int nYOrg, POINT lppt);
/** @param hWnd cast=(HWND) */
public static final native long SetCapture (long hWnd);
public static final native boolean SetCaretPos (int X, int Y);
/** @param AppID flags=no_out */
public static final native int SetCurrentProcessExplicitAppUserModelID (char[] AppID);
/** @param hCursor cast=(HCURSOR) */
public static final native long SetCursor (long hCursor);
public static final native boolean SetCursorPos (int X, int Y);
/** @param hdc cast=(HDC) */
public static final native int SetDCBrushColor (long hdc, int color);
/**
 * @param hdc cast=(HDC)
 * @param pColors cast=(RGBQUAD *),flags=no_out critical
 */
public static final native int SetDIBColorTable (long hdc, int uStartIndex, int cEntries, byte[] pColors);
/** @param hWnd cast=(HWND) */
public static final native long SetFocus (long hWnd);
/** @param hWnd cast=(HWND) */
public static final native boolean SetForegroundWindow (long hWnd);
/**
 * @param hwnd cast=(HWND)
 * @param pGestureConfig flags=no_out
 */
public static final native boolean SetGestureConfig(long hwnd, int dwReserved, int cIDs, GESTURECONFIG pGestureConfig, int cbSize);
/** @param hdc cast=(HDC) */
public static final native int SetGraphicsMode (long hdc, int iMode);
/** @param hwnd cast=(HWND) */
public static final native boolean SetLayeredWindowAttributes(long hwnd, int crKey, byte bAlpha, int dwFlags);
/**
 * @param hdc cast=(HDC)
 * @param dwLayout cast=(DWORD)
 */
public static final native int SetLayout (long hdc, int dwLayout);
/**
 * @param hWnd cast=(HWND)
 * @param hMenu cast=(HMENU)
 */
public static final native boolean SetMenu (long hWnd, long hMenu);
/** @param hMenu cast=(HMENU) */
public static final native boolean SetMenuDefaultItem (long hMenu, int uItem, int fByPos);
/**
 * @param hmenu cast=(HMENU)
 * @param lpcmi flags=no_out
 */
public static final native boolean SetMenuInfo (long hmenu, MENUINFO lpcmi);
/**
 * @param hMenu cast=(HMENU)
 * @param lpmii flags=no_out
 */
public static final native boolean SetMenuItemInfo (long hMenu, int uItem, boolean fByPosition, MENUITEMINFO lpmii);
/** @param hdc cast=(HDC) */
public static final native int SetMetaRgn (long hdc);
/**
 * @param hWndChild cast=(HWND)
 * @param hWndNewParent cast=(HWND)
 */
public static final native long SetParent (long hWndChild, long hWndNewParent);
/** @param hdc cast=(HDC) */
public static final native int SetPixel (long hdc, int X, int Y, int crColor);
/** @param hdc cast=(HDC) */
public static final native int SetPolyFillMode (long hdc, int iPolyFillMode);
public static final native boolean SetProcessDPIAware ();
/** @method flags=no_gen */
public static final native int SetPreferredAppMode(int mode);
/** @param lprc flags=no_in */
public static final native boolean SetRect (RECT lprc, int xLeft, int yTop, int xRight, int yBottom);
/** @param hrgn cast=(HRGN) */
public static final native boolean SetRectRgn (long hrgn, int nLeftRect, int nTopRect, int nRightRect, int nBottomRect);
/** @param hdc cast=(HDC) */
public static final native int SetROP2 (long hdc, int fnDrawMode);
/**
 * @param hwnd cast=(HWND)
 * @param info flags=no_out
 */
public static final native boolean SetScrollInfo (long hwnd, int flags, SCROLLINFO info, boolean fRedraw);
/** @param hdc cast=(HDC) */
public static final native int SetStretchBltMode (long hdc, int iStretchMode);
/**
 * @param hWnd cast=(HWND)
 * @param lpString cast=(LPCWSTR)
 * @param hData cast=(HANDLE)
 */
public static final native boolean SetProp (long hWnd, long lpString, long hData);
/**
 * @param hdc cast=(HDC)
 * @param colorRef cast=(COLORREF)
 */
public static final native int SetTextColor (long hdc, int colorRef);
/**
 * @param hWnd cast=(HWND)
 * @param lpTimerFunc cast=(TIMERPROC)
 */
public static final native long SetTimer (long hWnd, long nIDEvent, int Elapse, long lpTimerFunc);
/** @param hWnd cast=(HWND) */
public static final native int SetWindowLong (long hWnd, int nIndex, int dwNewLong);
/**
 * @param hWnd cast=(HWND)
 * @param dwNewLong cast=(LONG_PTR)
 */
public static final native long SetWindowLongPtr (long hWnd, int nIndex, long dwNewLong);
/** @param hdc cast=(HDC) */
public static final native boolean SetWindowOrgEx (long hdc, int X, int Y, POINT lpPoint);
/**
 * @param hWnd cast=(HWND)
 * @param lpwndpl flags=no_out
 */
public static final native boolean SetWindowPlacement (long hWnd, WINDOWPLACEMENT lpwndpl);
/**
 * @param hWnd cast=(HWND)
 * @param hWndInsertAfter cast=(HWND)
 */
public static final native boolean SetWindowPos(long hWnd, long hWndInsertAfter, int X, int Y, int cx, int cy, int uFlags);
/**
 * @param hWnd cast=(HWND)
 * @param hRgn cast=(HRGN)
 */
public static final native int SetWindowRgn (long hWnd, long hRgn, boolean bRedraw);
/**
 * @param hWnd cast=(HWND)
 * @param lpString cast=(LPCWSTR),flags=no_out
 */
public static final native boolean SetWindowText (long hWnd, char [] lpString);
/**
 * @param hwnd cast=(HWND)
 * @param pszSubAppName cast=(LPCWSTR),flags=no_out
 * @param pszSubIdList cast=(LPCWSTR),flags=no_out
 */
public static final native int SetWindowTheme (long hwnd, char [] pszSubAppName, char [] pszSubIdList);
/**
 * @param lpfn cast=(HOOKPROC)
 * @param hMod cast=(HINSTANCE)
 */
public static final native long SetWindowsHookEx (int idHook, long lpfn,  long hMod,  int dwThreadId);
/**
 * @param hdc cast=(HDC)
 * @param lpXform cast=(XFORM *),flags=no_out
 */
public static final native boolean SetWorldTransform(long hdc, float[] lpXform);
/** @param pszPath cast=(LPCWSTR),flags=no_out */
public static final native long SHGetFileInfo (char [] pszPath, int dwFileAttributes, SHFILEINFO psfi, int cbFileInfo, int uFlags);
public static final native boolean ShellExecuteEx (SHELLEXECUTEINFO lpExecInfo);
/** @param lpData flags=no_out */
public static final native boolean Shell_NotifyIcon (int dwMessage, NOTIFYICONDATA lpData);
/** @param hWnd cast=(HWND) */
public static final native boolean ShowCaret (long hWnd);
/** @param hWnd cast=(HWND) */
public static final native boolean ShowOwnedPopups (long hWnd, boolean fShow);
/** @param hWnd cast=(HWND) */
public static final native boolean ShowScrollBar (long hWnd, int wBar, boolean bShow);
/** @param hWnd cast=(HWND) */
public static final native boolean ShowWindow (long hWnd, int nCmdShow);
/**
 * @param hdc cast=(HDC)
 * @param lpdi flags=no_out
 */
public static final native int StartDoc (long hdc, DOCINFO lpdi);
/** @param hdc cast=(HDC) */
public static final native int StartPage (long hdc);
/**
 * @param hdcDest cast=(HDC)
 * @param hdcSrc cast=(HDC)
 */
public static final native boolean StretchBlt (long hdcDest, int nXOriginDest, int nYOriginDest, int nWidthDest, int nHeightDest, long hdcSrc, int nXOriginSrc, int nYOriginSrc, int nWidthSrc, int nHeightSrc, int dwRop);
public static final native boolean SystemParametersInfo (int uiAction, int uiParam, HIGHCONTRAST pvParam, int fWinIni);
public static final native boolean SystemParametersInfo (int uiAction, int uiParam, RECT pvParam, int fWinIni);
public static final native boolean SystemParametersInfo (int uiAction, int uiParam, NONCLIENTMETRICS pvParam, int fWinIni);
public static final native boolean SystemParametersInfo (int uiAction, int uiParam, int [] pvParam, int fWinIni);
/**
 * @param lpKeyState cast=(PBYTE)
 * @param pwszBuff cast=(LPWSTR)
 */
public static final native int ToUnicode (int wVirtKey, int wScanCode, byte [] lpKeyState, char [] pwszBuff, int cchBuff, int wFlags);
/**
 * @param hwndTV cast=(HWND)
 * @param hitem cast=(HTREEITEM)
 */
public static final native boolean TreeView_GetItemRect (long hwndTV, long hitem, RECT prc, boolean fItemRect);
public static final native boolean TrackMouseEvent (TRACKMOUSEEVENT lpEventTrack);
/**
 * @param hMenu cast=(HMENU)
 * @param hWnd cast=(HWND)
 */
public static final native boolean TrackPopupMenu (long hMenu, int uFlags, int x, int y, int nReserved, long hWnd, RECT prcRect);
/**
 * @param hWnd cast=(HWND)
 * @param hAccTable cast=(HACCEL)
 */
public static final native int TranslateAccelerator (long hWnd, long hAccTable, MSG lpMsg);
/**
 * @param lpSrc cast=(DWORD *)
 * @param lpCs cast=(LPCHARSETINFO)
 */
public static final native boolean TranslateCharsetInfo (long lpSrc, int [] lpCs, int dwFlags);
/**
 * @param hWndClient cast=(HWND)
 * @param lpMsg cast=(LPMSG)
 */
public static final native boolean TranslateMDISysAccel (long hWndClient, MSG lpMsg);
public static final native boolean TranslateMessage (MSG lpmsg);
/**
 * @param hdcDest cast=(HDC)
 * @param hdcSrc cast=(HDC)
 */
public static final native boolean TransparentBlt (long hdcDest, int nXOriginDest, int nYOriginDest, int nWidthDest, int hHeightDest, long hdcSrc, int nXOriginSrc, int nYOriginSrc, int nWidthSrc, int nHeightSrc, int crTransparent);
/** @param hkl cast=(HKL) */
public static final native boolean UnloadKeyboardLayout (long hkl);
/** @param hhk cast=(HHOOK) */
public static final native boolean UnhookWindowsHookEx (long hhk);
/**
 * @param lpClassName cast=(LPCWSTR),flags=no_out
 * @param hInstance cast=(HINSTANCE)
 */
public static final native boolean UnregisterClass (char [] lpClassName, long hInstance);
/**
 * @param hWnd cast=(HWND)
 */
public static final native boolean UnregisterHotKey(long hWnd, int id);
/** @param hwnd cast=(HWND) */
public static final native boolean UnregisterTouchWindow (long hwnd);
/** @param hWnd cast=(HWND) */
public static final native boolean UpdateWindow (long hWnd);
/**
 * @param pszPath cast=(LPCWSTR),flags=no_out
 * @param pszURL cast=(LPWSTR)
 * @param pcchUrl cast=(DWORD *)
 */
public static final native int UrlCreateFromPath (char[] pszPath, char[] pszURL, int[] pcchUrl, int flags);
/**
 * @param hWnd cast=(HWND)
 * @param lpRect flags=no_out
 */
public static final native boolean ValidateRect (long hWnd, RECT lpRect);
/** @param ch cast=(WCHAR) */
public static final native short VkKeyScan (short ch);

public static final native boolean WaitMessage ();
/**
 * @param lpWideCharStr cast=(LPCWSTR),flags=no_out critical
 * @param lpMultiByteStr cast=(LPSTR),flags=no_in critical
 * @param lpDefaultChar cast=(LPCSTR)
 * @param lpUsedDefaultChar cast=(LPBOOL)
 */
public static final native int WideCharToMultiByte (int CodePage, int dwFlags, char [] lpWideCharStr, int cchWideChar, byte [] lpMultiByteStr, int cchMultiByte, byte [] lpDefaultChar, int [] lpUsedDefaultChar);
/**
 * @param lpWideCharStr cast=(LPCWSTR),flags=no_out critical
 * @param lpMultiByteStr cast=(LPSTR)
 * @param lpDefaultChar cast=(LPCSTR)
 * @param lpUsedDefaultChar cast=(LPBOOL)
 */
public static final native int WideCharToMultiByte (int CodePage, int dwFlags, char [] lpWideCharStr, int cchWideChar, long lpMultiByteStr, int cchMultiByte, byte [] lpDefaultChar, int [] lpUsedDefaultChar);
/** @param hDC cast=(HDC) */
public static final native long WindowFromDC (long hDC);
/** @param lpPoint flags=struct */
public static final native long WindowFromPoint (POINT lpPoint);
/** @param string cast=(const wchar_t *) */
public static final native int wcslen (long string);

/** @param hFileMappingObject cast=(HANDLE)
 *  @param dwDesiredAccess cast=(DWORD)
 *  @param dwFileOffsetHigh cast=(DWORD)
 *  @param dwFileOffsetLow cast=(DWORD)
 */
public static final native long MapViewOfFile(long hFileMappingObject, int dwDesiredAccess, int dwFileOffsetHigh, int dwFileOffsetLow, int dwNumberOfBytesToMap);
/** @param lpBaseAddress cast=(LPCVOID) */
public static final native boolean UnmapViewOfFile(long lpBaseAddress);

public static final int PROCESS_DUP_HANDLE = 0x0040;
public static final int PROCESS_VM_READ = 0x0010;
public static final int DUPLICATE_SAME_ACCESS = 2;

/**
 * @param dwDesiredAccess cast=(DWORD)
 * @param dwProcessId cast=(DWORD)
 */
public static final native long OpenProcess(int dwDesiredAccess, boolean bInheritHandle, int dwProcessId);

public static final native long GetCurrentProcess();

/**
 * @param hSourceProcessHandle cast=(HANDLE)
 * @param hSourceHandle cast=(HANDLE)
 * @param hTargetProcessHandle cast=(HANDLE)
 * @param lpTargetHandle cast=(LPHANDLE)
 * @param dwDesiredAccess cast=(DWORD)
 * @param dwOptions cast=(DWORD)
 */
public static final native boolean DuplicateHandle(long hSourceProcessHandle, long hSourceHandle, long hTargetProcessHandle,
		long [] lpTargetHandle, int dwDesiredAccess, boolean b, int dwOptions);

}
