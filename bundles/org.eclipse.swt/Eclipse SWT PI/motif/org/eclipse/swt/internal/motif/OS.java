/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.motif;

import org.eclipse.swt.internal.*;

 
public class OS extends C {
	public static final boolean IsAIX, IsSunOS, IsLinux, IsHPUX;
	static {
		/* Initialize the OS flags and locale constants */
		String osName = System.getProperty ("os.name");
		boolean isAIX = false, isSunOS = false, isLinux = false, isHPUX = false;
		if (osName.equals ("Linux")) isLinux = true;
		if (osName.equals ("AIX")) isAIX = true;
		if (osName.equals ("Solaris")) isSunOS = true;
		if (osName.equals ("SunOS")) isSunOS = true;
		if (osName.equals ("HP-UX")) isHPUX = true;
		IsAIX = isAIX;  IsSunOS = isSunOS;  IsLinux = isLinux;  IsHPUX = isHPUX;
	}

	public static final boolean IsDBLocale = OS.MB_CUR_MAX () != 1;
	public static final int CODESET = CODESET ();
	public static final int LC_CTYPE = LC_CTYPE ();

	static final int RESOURCE_LENGTH = 1024 * 3;
	static final int RESOURCE_START = OS.XtMalloc (RESOURCE_LENGTH);
	static int NextResourceStart = RESOURCE_START;
	static {
		OS.setResourceMem (RESOURCE_START, RESOURCE_START + RESOURCE_LENGTH);
	}
	
	/** Constants */
	public static final int Above = 0;
	public static final int AllPlanes = 0xFFFFFFFF;
	public static final int AllocNone = 0;
	public static final int Below = 1;
	public static final int Button1Mask = (1<<8);
	public static final int Button2Mask = (1<<9);
	public static final int Button3Mask = (1<<10);
	public static final int ButtonPress = 4;
	public static final int ButtonPressMask = 1 << 2;
	public static final int ButtonRelease = 5;
	public static final int ButtonReleaseMask = 1 << 3;
	public static final int ButtonMotionMask = 1 << 13;
	public static final int Button1MotionMask = 1 << 8;
	public static final int Button2MotionMask = 1 << 9;
	public static final int Button3MotionMask = 1 << 10;
	public static final int Button4MotionMask = 1 << 11;
	public static final int Button5MotionMask = 1 << 12;
	public static final int CWBackPixmap = 0x1;
	public static final int CWBitGravity = 0x10;
	public static final int CWColormap = 1 << 13;
	public static final int CWCursor = 0x4000;
	public static final int CWDontPropagate = 0x1000;
	public static final int CWEventMask = 0x800;
	public static final int CWHeight = 0x8;
	public static final int CWSibling = 0x20;
	public static final int CWStackMode = 0x40;
	public static final int CWWidth = 0x4;
	public static final int CapButt = 0x1;
	public static final int CapProjecting = 3;
	public static final int CapRound = 0x2;
	public static final int ClientMessage = 33;
	public static final int Complex = 0;
	public static final int ConfigureNotify = 22;
	public static final int ControlMask = (1<<2);
	public static final int CoordModeOrigin = 0x0;
	public static final int CopyFromParent = 0;
	public static final int CreateNotify = 16;
	public static final int CurrentTime = 0;
	public static final int DestroyNotify = 17;
	public static final int EnterNotify = 7;
	public static final int EnterWindowMask = 1 << 4;
	public static final int EvenOddRule = 0;
	public static final int Expose = 12;
	public static final int ExposureMask = 1 << 15;
	public static final int FillStippled = 0x2;
	public static final int FillTiled = 0x1;
	public static final int FocusChangeMask = 1 << 21;
	public static final int FocusIn = 9;
	public static final int FocusOut = 10;
	public static final int ForgetGravity = 0;
	public static final int GCBackground = 0x8;
	public static final int GCCapStyle = 1 << 6;
	public static final int GCFillRule = 1 << 9;
	public static final int GCForeground = 0x4;
	public static final int GCFunction = 0x1;
	public static final int GCJoinStyle = 1 << 7;
	public static final int GCLineWidth = 0x10;
	public static final int GCFillStyle = 1<<8;
	public static final int GCTile = 1<<10;
	public static final int GCTileStipXOrigin	= 1<<12;
	public static final int GCTileStipYOrigin	= 1<<13;
	public static final int GXand = 0x1;
	public static final int GXcopy = 0x3;
	public static final int GXxor = 0x6;
	public static final int GrabModeAsync = 1;
	public static final int GrabSuccess = 0;
	public static final int GraphicsExpose = 13;
	public static final int hpXK_BackTab = 0x1000FF74;
	public static final int IconicState = 0x3;
	public static final int IncludeInferiors = 0x1;
	public static final int IsUnviewable = 0x1;
	public static final int IsViewable = 0x2;
	public static final int JoinMiter = 0x0;
	public static final int JoinRound = 0x1;
	public static final int JoinBevel = 0x2;
	public static final int KeyPress = 2;
	public static final int KeyPressMask = 1 << 0;
	public static final int KeyRelease = 3;
	public static final int KeyReleaseMask = 1 << 1;
	public static final int LSBFirst = 0;
	public static final int LeaveNotify = 8;
	public static final int LeaveWindowMask = 1 << 5;
	public static final int LineOnOffDash = 0x1;
	public static final int LineSolid = 0x0;
	public static final int MSBFirst = 1;
	public static final int MWM_DECOR_BORDER = 0x2;
	public static final int MWM_DECOR_MAXIMIZE = 0x40;
	public static final int MWM_DECOR_MENU = 0x10;
	public static final int MWM_DECOR_MINIMIZE = 0x20;
	public static final int MWM_DECOR_RESIZEH = 0x4;
	public static final int MWM_DECOR_TITLE = 0x8;
	public static final int MWM_INPUT_FULL_APPLICATION_MODAL = 0x3;
	public static final int MWM_INPUT_MODELESS = 0x0;
	public static final int MWM_INPUT_PRIMARY_APPLICATION_MODAL = 0x1;
	public static final int MWM_INPUT_SYSTEM_MODAL = 0x2;
	public static final int MapNotify = 19;
	public static final int Mod1Mask = (1<<3);
	public static final int Mod1MapIndex = 3;
	public static final int Mod2MapIndex = 4;
	public static final int Mod3MapIndex = 5;
	public static final int Mod4MapIndex = 6;
	public static final int Mod5MapIndex = 7;
	public static final int MotionNotify = 6;
	public static final int None = 0;
	public static final int NormalState = 0x1;
	public static final int NorthWestGravity = 0x1;
	public static final int NotifyAncestor = 0x0;
	public static final int NotifyInferior = 0x2;
	public static final int NotifyNonlinear = 0x3;
	public static final int NotifyNonlinearVirtual = 0x4;
	public static final int NotifyNormal = 0x0;
	public static final int NotifyGrab = 0x1;
	public static final int NotifyUngrab = 0x2;
	public static final int ParentRelative = 1;
	public static final int PMinSize = 1 << 4;
	public static final int PMaxSize = 1 << 5;
	public static final int PPosition = 1 << 2;
	public static final int PointerMotionMask = 1 << 6;
	public static final int PointerMotionHintMask = 1 << 7;
	public static final int PropertyChangeMask = 1 << 22;
	public static final int PropertyNotify = 28;
	public static final int PropModeReplace = 0;
	public static final int QueuedAfterReading = 1;
	public static final int RectangleOut = 0x0;
	public static final int ReparentNotify = 21;
	public static final int ResizeRedirectMask = 1 << 18;
	public static final int RevertToParent = 0x2;
	public static final int RTLD_LAZY = 1; 
	public static final int SelectionClear = 29;
	public static final int SelectionNotify = 31;
	public static final int SelectionRequest = 30;
	public static final int ShapeBounding = 0;
	public static final int ShapeClip = 1;
	public static final int ShapeSet = 0;
	public static final int ShiftMask = (1<<0);
	public static final int StructureNotifyMask = 1 << 17;
	public static final int SubstructureNotifyMask = 1 << 19;
	public static final int SubstructureRedirectMask = 1 << 20;
	public static final int SunXK_F36 = 0x1005FF10;
	public static final int SunXK_F37 = 0x1005FF11;
	public static final int UnmapNotify = 18;
	public static final int Unsorted = 0x0;
	public static final int X_OK = 0x01;
	public static final int XA_ATOM = 4;
	public static final int XA_FONT = 18;
	public static final int XBufferOverflow = 0xFFFFFFFF;
	public static final int XC_X_cursor = 0;
	public static final int XC_bottom_left_corner = 12;
	public static final int XC_bottom_right_corner = 14;
	public static final int XC_bottom_side = 16;
	public static final int XC_cross = 30;
	public static final int XC_double_arrow = 42;
	public static final int XC_fleur = 52;
	public static final int XC_hand2 = 60;
	public static final int XC_left_ptr = 68;
	public static final int XC_left_side = 70;
	public static final int XC_question_arrow = 92;
	public static final int XC_right_side = 96;
	public static final int XC_sb_h_double_arrow = 108;
	public static final int XC_sb_up_arrow = 114;
	public static final int XC_sb_v_double_arrow = 116;
	public static final int XC_sizing = 120;
	public static final int XC_top_left_corner = 134;
	public static final int XC_top_right_corner = 136;
	public static final int XC_top_side = 138;
	public static final int XC_watch = 150;
	public static final int XC_xterm = 152;
	public static final int XCompoundTextStyle = 1;
	public static final int XEMBED_EMBEDDED_NOTIFY = 0;
	public static final int XEMBED_WINDOW_ACTIVATE = 1;
	public static final int XEMBED_WINDOW_DEACTIVATE = 2;
	public static final int XEMBED_REQUEST_FOCUS = 3;
	public static final int XEMBED_FOCUS_IN = 4;
	public static final int XEMBED_FOCUS_OUT = 5;
	public static final int XEMBED_FOCUS_NEXT = 6;
	public static final int XEMBED_FOCUS_PREV = 7;
	public static final int XEMBED_MODALITY_ON = 10;
	public static final int XEMBED_MODALITY_OFF = 11;
	public static final int XEMBED_REGISTER_ACCELERATOR = 12;
	public static final int XEMBED_UNREGISTER_ACCELERATOR = 13;
	public static final int XEMBED_ACTIVATE_ACCELERATOR = 14;
	public static final int XEMBED_FOCUS_CURRENT = 0;
	public static final int XEMBED_FOCUS_FIRST = 1;
	public static final int XEMBED_FOCUS_LAST = 2;
	public static final int XEMBED_MAPPED = 1 << 0;
	public static final int XK_Alt_L = 0xFFE9;
	public static final int XK_Alt_R = 0xFFEA;
	public static final int XK_BackSpace = 0xFF08;
	public static final int XK_Break = 0xFF6B;
	public static final int XK_Cancel = 0xFF69;
	public static final int XK_Caps_Lock = 0xFFE5;
	public static final int XK_Control_L = 0xFFE3;
	public static final int XK_Control_R = 0xFFE4;
	public static final int XK_Delete = 0xFFFF;
	public static final int XK_Down = 0xFF54;
	public static final int XK_End = 0xFF57;
	public static final int XK_Escape = 0xFF1B;
	public static final int XK_F1 = 0xFFBE;
	public static final int XK_F10 = 0xFFC7;
	public static final int XK_F11 = 0xFFC8;
	public static final int XK_F12 = 0xFFC9;
	public static final int XK_F13 = 0xFFCA;
	public static final int XK_F14 = 0xFFCB;
	public static final int XK_F15 = 0xFFCC;
	public static final int XK_F16 = 0xFFCD;
	public static final int XK_F17 = 0xFFCE;
	public static final int XK_F18 = 0xFFCF;
	public static final int XK_F19 = 0xFFD0;
	public static final int XK_F20 = 0xFFD1;
	public static final int XK_F2 = 0xFFBF;
	public static final int XK_F3 = 0xFFC0;
	public static final int XK_F4 = 0xFFC1;
	public static final int XK_F5 = 0xFFC2;
	public static final int XK_F6 = 0xFFC3;
	public static final int XK_F7 = 0xFFC4;
	public static final int XK_F8 = 0xFFC5;
	public static final int XK_F9 = 0xFFC6;
	public static final int XK_Home = 0xFF50;
	public static final int XK_Help = 0xFF6A;
	public static final int XK_ISO_Left_Tab = 0xFE20;
	public static final int XK_Insert = 0xFF63;
	public static final int XK_KP_Enter = 0xFF8D;
	public static final int XK_KP_F1 = 0xFF91;
	public static final int XK_KP_F2 = 0xFF92;
	public static final int XK_KP_F3 = 0xFF93;
	public static final int XK_KP_F4 = 0xFF94;
	public static final int XK_KP_Home = 0xFF95;
	public static final int XK_KP_Left = 0xFF96;
	public static final int XK_KP_Up = 0xFF97;
	public static final int XK_KP_Right = 0xFF98;
	public static final int XK_KP_Down = 0xFF99;
	public static final int XK_KP_Prior = 0xFF9A;
	public static final int XK_KP_Page_Up = 0xFF9A;
	public static final int XK_KP_Next = 0xFF9B;
	public static final int XK_KP_Page_Down = 0xFF9B;
	public static final int XK_KP_End = 0xFF9C;
	public static final int XK_KP_Insert = 0xFF9E;
	public static final int XK_KP_Delete = 0xFF9F;
	public static final int XK_KP_Equal = 0xFFBD;
	public static final int XK_KP_Multiply = 0xFFAA;
	public static final int XK_KP_Add = 0xFFAB;
	public static final int XK_KP_Subtract = 0xFFAD;
	public static final int XK_KP_Decimal = 0xFFAE;
	public static final int XK_KP_Divide = 0xFFAF;
	public static final int XK_KP_0 = 0xFFB0;
	public static final int XK_KP_1 = 0xFFB1;
	public static final int XK_KP_2 = 0xFFB2;
	public static final int XK_KP_3 = 0xFFB3;
	public static final int XK_KP_4 = 0xFFB4;
	public static final int XK_KP_5 = 0xFFB5;
	public static final int XK_KP_6 = 0xFFB6;
	public static final int XK_KP_7 = 0xFFB7;
	public static final int XK_KP_8 = 0xFFB8;
	public static final int XK_KP_9 = 0xFFB9;
	public static final int XK_Left = 0xFF51;
	public static final int XK_Linefeed = 0xFF0A;
	public static final int XK_Meta_L = 0xFFE7;
	public static final int XK_Meta_R = 0xFFE8;
	public static final int XK_Num_Lock= 0xFF7F;
	public static final int XK_Page_Down = 0xFF56;
	public static final int XK_Page_Up = 0xFF55;
	public static final int XK_Pause = 0xFF13;
	public static final int XK_Print = 0xFF61;
	public static final int XK_R1 = 0xFFD2;
	public static final int XK_R2 = 0xFFD3;
	public static final int XK_R3 = 0xFFD4;
	public static final int XK_R4 = 0xFFD5;
	public static final int XK_R5 = 0xFFD6;
	public static final int XK_R6 = 0xFFD7;
	public static final int XK_R7 = 0xFFD8;
	public static final int XK_R9 = 0xFFDA;
	public static final int XK_R13 = 0xFFDE;
	public static final int XK_R15 = 0xFFE0;
	public static final int XK_Return = 0xFF0D;
	public static final int XK_Right = 0xFF53;
	public static final int XK_Scroll_Lock = 0xFF14;
	public static final int XK_Shift_L = 0xFFE1;
	public static final int XK_Shift_R = 0xFFE2;
	public static final int XK_Tab = 0xFF09;
	public static final int XK_Up = 0xFF52;
	public static final int XK_VoidSymbol = 0xFFFFFF;
	public static final int XK_space = 0x20;
	public static final byte XPAttrMerge = 2;
	public static final byte XPDocAttr = 2;
	public static final byte XPJobAttr = 1;
	public static final byte XPPrinterAttr = 4;
	public static final byte XPSpool = 1;
	public static final int XYBitmap = 0;
	public static final int XmALIGNMENT_BEGINNING = 0x0;
	public static final int XmALIGNMENT_CENTER = 0x1;
	public static final int XmALIGNMENT_END = 0x2;
	public static final int XmARROW_DOWN = 0x1;
	public static final int XmARROW_LEFT = 0x2;
	public static final int XmARROW_RIGHT = 0x3;
	public static final int XmARROW_UP = 0x0;
	public static final int XmATTACH_FORM = 0x1;
	public static final int XmBLEND_ALL = 0x0;
	public static final int XmBROWSE_SELECT = 0x3;
	public static final int XmCHARSET_TEXT = 0x0;
	public static final int XmCOMBO_BOX = 0x0;
	public static final int XmCONSTANT = 0x1;
	public static final int XmCOPY_FAILED = 0x0;
	public static final int XmCOPY_TRUNCATED = 0x2;
	public static final int XmCR_DROP_SITE_ENTER_MESSAGE = 0x2;
	public static final int XmCR_DROP_SITE_LEAVE_MESSAGE = 0x1;
	public static final int XmCR_DROP_SITE_MOTION_MESSAGE = 0x3;
	public static final int XmCR_OK = 0x1F;
	public static final int XmCR_OPERATION_CHANGED = 0x8;
	public static final int XmClipboardSuccess = 0x1;
	public static final int XmDEFAULT_SELECT_COLOR = -1;
	public static final int XmDIALOG_CANCEL_BUTTON = 0x2;
	public static final int XmDIALOG_FULL_APPLICATION_MODAL = 0x2;
	public static final int XmDIALOG_HELP_BUTTON = 0x7;
	public static final int XmDIALOG_LIST = 0x8;
	public static final int XmDIALOG_LIST_LABEL = 0x9;
	public static final int XmDIALOG_MESSAGE_LABEL = 0xA;
	public static final int XmDIALOG_MODELESS = 0x0;
	public static final int XmDIALOG_OK_BUTTON = 0x4;
	public static final int XmDIALOG_PRIMARY_APPLICATION_MODAL = 0x1;
	public static final int XmDIALOG_SELECTION_LABEL = 0xB;
	public static final int XmDIALOG_SYSTEM_MODAL = 0x3;
	public static final int XmDIALOG_TEXT = 0xD;
	public static final int XmDO_NOTHING = 0x2;
	public static final int XmDRAG_DYNAMIC = 5;
	public static final int XmDRAG_UNDER_NONE = 0;
	public static final byte XmDROP = 0;
	public static final byte XmDROP_COPY = (1 << 1);
	public static final int XmDROP_DOWN_COMBO_BOX = 0x1;
	public static final int XmDROP_DOWN_LIST = 0x2;
	public static final byte XmDROP_LINK = (1 << 2);
	public static final byte XmDROP_MOVE = (1 << 0);
	public static final byte XmDROP_NOOP = 0;
	public static final int XmDROP_SITE_ACTIVE = 0;
	public static final int XmDROP_SITE_COMPOSITE = 1;
	public static final int XmDROP_SITE_INACTIVE = 1;
	public static final int XmDROP_SITE_INVALID = 2;
	public static final int XmDROP_SITE_VALID = 3;
	public static final int XmEXTENDED_SELECT = 0x2;
	public static final byte[] XmFONTLIST_DEFAULT_TAG = {0x46, 0x4F, 0x4E, 0x54, 0x4C, 0x49, 0x53, 0x54, 0x5F, 0x44, 0x45, 0x46, 0x41, 0x55, 0x4C, 0x54, 0x5F, 0x54, 0x41, 0x47, 0x5F, 0x53, 0x54, 0x52, 0x49, 0x4E, 0x47, 0x0};
	public static final int XmFONT_IS_FONT = 0x0;
	public static final int XmFONT_IS_FONTSET = 0x1;
	public static final int XmFOREGROUND_COLOR = 0x1;
	public static final int XmFRAME_TITLE_CHILD = 0x2;
	public static final int XmHIGHLIGHT_COLOR = -3;
	public static final int XmHIGHLIGHT_NORMAL = 0x0;
	public static final int XmHORIZONTAL = 0x2;
	public static final int XmLAST_POSITION = -1;
	public static final int XmMAX_ON_BOTTOM = 0x1;
	public static final int XmMAX_ON_RIGHT = 0x3;
	public static final int XmMAX_ON_TOP = 0x0;
	public static final int XmMERGE_REPLACE = 0x1;
	public static final int XmMULTIPLE_SELECT = 0x1;
	public static final int XmMULTI_LINE_EDIT = 0x0;
	public static final int XmNO_LINE = 0x0;
	public static final int XmNONE = 0x0;
	public static final int XmN_OF_MANY = 0x1;
	public static final int XmNaccelerator = malloc ("accelerator", 4);
	public static final int XmNacceleratorText = malloc ("acceleratorText", 4);
	public static final int XmNactivateCallback = malloc ("activateCallback", 4);
	public static final int XmNalignment = malloc ("alignment", 1);
	public static final int XmNallowShellResize = malloc ("allowShellResize", 1);
	public static final int XmNancestorSensitive = malloc ("ancestorSensitive", 1);
	public static final int XmNanimationStyle = malloc ("animationStyle", 1);
	public static final int XmNarea = malloc ("area", 4);
	public static final int XmNarmCallback = malloc ("armCallback", 4);
	public static final int XmNarrowDirection = malloc ("arrowDirection", 1);
	public static final int XmNarrowSize = malloc ("arrowSize", 2);
	public static final int XmNarrowSpacing = malloc ("arrowSpacing", 2);
	public static final int XmNbackground = malloc ("background", 4);
	public static final int XmNbackgroundPixmap = malloc ("backgroundPixmap", 4);
	public static final int XmNblendModel = malloc ("blendModel", 1);
	public static final int XmNblinkRate = malloc ("blinkRate", 4);
	public static final int XmNborderColor = malloc ("borderColor", 4);
	public static final int XmNborderWidth = malloc ("borderWidth", 2);
	public static final int XmNbottomAttachment = malloc ("bottomAttachment", 1);
	public static final int XmNbottomShadowColor = malloc ("bottomShadowColor", 4);
	public static final int XmNbrowseSelectionCallback = malloc ("browseSelectionCallback", 4);
	public static final int XmNcancelCallback = malloc ("cancelCallback", 4);
	public static final int XmNcancelLabelString = malloc ("cancelLabelString", 4);
	public static final int XmNcascadingCallback = malloc ("cascadingCallback", 4);
	public static final int XmNchildHorizontalSpacing = malloc ("childHorizontalSpacing", 2);
	public static final int XmNchildren = malloc ("children", 4);
	public static final int XmNclientData = malloc ("clientData", 4);
	public static final int XmNcolormap = malloc ("colormap", 4);
	public static final int XmNcolumns = malloc ("columns", 2);
	public static final int XmNcomboBoxType = malloc ("comboBoxType", 1);
	public static final int XmNconvertProc = malloc ("convertProc", 4);
	public static final int XmNcursorPositionVisible = malloc ("cursorPositionVisible", 1);
	public static final int XmNdecimalPoints = malloc ("decimalPoints", 2);
	public static final int XmNdecrementCallback = malloc ("decrementCallback", 4);
	public static final int XmNdefaultActionCallback = malloc ("defaultActionCallback", 4);
	public static final int XmNdefaultButtonShadowThickness = malloc ("defaultButtonShadowThickness", 4);
	public static final int XmNdefaultPosition = malloc ("defaultPosition", 1);
	public static final int XmNdeleteResponse = malloc ("deleteResponse", 1);
	public static final int XmNdialogStyle = malloc ("dialogStyle", 1);
	public static final int XmNdialogTitle = malloc ("dialogTitle", 4);
	public static final int XmNdirMask = malloc ("dirMask", 4);
	public static final int XmNdirSpec = malloc ("dirSpec", 4);
	public static final int XmNdirectory = malloc ("directory", 4);
	public static final int XmNdragCallback = malloc ("dragCallback", 4);
	public static final int XmNdragDropFinishCallback = malloc ("dragDropFinishCallback", 4);
	public static final int XmNdragInitiatorProtocolStyle = malloc ("dragInitiatorProtocolStyle", 1);
	public static final int XmNdragOperations = malloc ("dragOperations", 1);
	public static final int XmNdragProc = malloc ("dragProc", 4);
	public static final int XmNdragReceiverProtocolStyle = malloc ("dragReceiverProtocolStyle", 1);
	public static final int XmNdropFinishCallback = malloc ("dropFinishCallback", 4);
	public static final int XmNdropProc = malloc("dropProc", 4);
	public static final int XmNdropSiteActivity = malloc ("dropSiteActivity", 1);
	public static final int XmNdropSiteOperations = malloc ("dropSiteOperations", 1);
	public static final int XmNdropSiteType = malloc ("dropSiteType", 1);
	public static final int XmNdropTransfers = malloc ("dropTransfers", 4);
	public static final int XmNeditMode = malloc ("editMode", 4);
	public static final int XmNeditable = malloc ("editable", 1);
	public static final int XmNenableThinThickness = malloc ("enableThinThickness", 1);
	public static final int XmNexportTargets = malloc ("exportTargets", 4);
	public static final int XmNexposeCallback = malloc ("exposeCallback", 4);
	public static final int XmNextendedSelectionCallback = malloc ("extendedSelectionCallback", 4);
	public static final int XmNfilterLabelString = malloc ("filterLabelString", 4);
	public static final int XmNfont = malloc ("font", 4);
	public static final int XmNfontList = malloc ("fontList", 4);
	public static final int XmNfontType = malloc ("fontType", 4);
	public static final int XmNforeground = malloc ("foreground", 4);
	public static final int XmNframeChildType = malloc ("frameChildType", 1);
	public static final int XmNheight = malloc ("height", 2);
	public static final int XmNhelpCallback = malloc ("helpCallback", 4);
	public static final int XmNhelpLabelString = malloc ("helpLabelString", 4);
	public static final int XmNhighlightColor = malloc ("highlightColor", 4);
	public static final int XmNhighlightThickness = malloc ("highlightThickness", 2);
	public static final int XmNhorizontalScrollBar = malloc ("horizontalScrollBar", 4);
	public static final int XmNiconMask = malloc ("iconMask", 4);
	public static final int XmNiconName = malloc ("iconName", 4);
	public static final int XmNiconPixmap = malloc ("iconPixmap", 4);
	public static final int XmNiconic = malloc ("iconic", 1);
	public static final int XmNimportTargets = malloc("importTargets", 4);
	public static final int XmNincrement = malloc ("increment", 4);
	public static final int XmNincrementValue = malloc ("incrementValue", 4);
	public static final int XmNincrementCallback = malloc ("incrementCallback", 4);
	public static final int XmNindicatorOn = malloc ("indicatorOn", 1);
	public static final int XmNindicatorType = malloc ("indicatorType", 1);
	public static final int XmNinitialState = malloc ("initialState", 4);
	public static final int XmNitemCount = malloc ("itemCount", 4);
	public static final int XmNitems = malloc ("items", 4);
	public static final int XmNlabelInsensitivePixmap = malloc ("labelInsensitivePixmap", 4);
	public static final int XmNlabelPixmap = malloc ("labelPixmap", 4);
	public static final int XmNlabelString = malloc ("labelString", 4);
	public static final int XmNlabelType = malloc ("labelType", 1);
	public static final int XmNleftAttachment = malloc ("leftAttachment", 1);
	public static final int XmNlist = malloc ("list", 4);
	public static final int XmNlistMarginHeight = malloc ("listMarginHeight", 2);
	public static final int XmNlistMarginWidth = malloc ("listMarginWidth", 2);
	public static final int XmNlistSizePolicy = malloc ("listSizePolicy", 1);
	public static final int XmNlistSpacing = malloc ("listSpacing", 2);
	public static final int XmNmainWindowMarginWidth  = malloc ("mainWindowMarginWidth", 2);
	public static final int XmNmainWindowMarginHeight  = malloc ("mainWindowMarginHeight", 2);
	public static final int XmNmapCallback = malloc ("mapCallback", 4);
	public static final int XmNmappedWhenManaged = malloc ("mappedWhenManaged", 4);
	public static final int XmNmarginBottom = malloc ("marginBottom", 2);
	public static final int XmNmarginHeight = malloc ("marginHeight", 2);
	public static final int XmNmarginLeft = malloc ("marginLeft", 2);
	public static final int XmNmarginRight = malloc ("marginRight", 2);
	public static final int XmNmarginTop = malloc ("marginTop", 2);
	public static final int XmNmarginWidth = malloc ("marginWidth", 2);
	public static final int XmNmaximum = malloc ("maximum", 4);
	public static final int XmNmaximumValue = malloc ("maximumValue", 4);	
	public static final int XmNmenuBar = malloc ("menuBar", 4);
	public static final int XmNmessageString = malloc ("messageString", 4);
	public static final int XmNminimum = malloc ("minimum", 4);
	public static final int XmNminimumValue = malloc ("minimumValue", 4);
	public static final int XmNminHeight = malloc ("minHeight", 4);
	public static final int XmNminWidth = malloc ("minWidth", 4);
	public static final int XmNmnemonic = malloc ("mnemonic", 4);
	public static final int XmNmodifyVerifyCallback = malloc ("modifyVerifyCallback", 4);
	public static final int XmNmultipleSelectionCallback = malloc ("multipleSelectionCallback", 4);
	public static final int XmNmwmDecorations = malloc ("mwmDecorations", 4);
	public static final int XmNmwmInputMode = malloc ("mwmInputMode", 4);
	public static final int XmNnavigationType = malloc ("navigationType", 1);
	public static final int XmNnoResize = malloc ("noResize", 1);
	public static final int XmNnumChildren = malloc ("numChildren", 4);
	public static final int XmNnumDropTransfers = malloc ("numDropTransfers", 4);
	public static final int XmNnumExportTargets = malloc ("numExportTargets", 4);
	public static final int XmNnumImportTargets = malloc("numImportTargets", 4);
	public static final int XmNokCallback = malloc ("okCallback", 4);
	public static final int XmNokLabelString = malloc ("okLabelString", 4);
	public static final int XmNoperationCursorIcon = malloc ("operationCursorIcon", 4);
	public static final int XmNorientation = malloc ("orientation", 1);
	public static final int XmNoverrideRedirect = malloc ("overrideRedirect", 1);
	public static final int XmNpageDecrementCallback = malloc ("pageDecrementCallback", 4);
	public static final int XmNpageIncrement = malloc ("pageIncrement", 4);
	public static final int XmNpageIncrementCallback = malloc ("pageIncrementCallback", 4);
	public static final int XmNpathMode = malloc ("pathMode", 4);
	public static final int XmNpattern = malloc ("pattern", 4);
	public static final int XmNposition = malloc ("position", 4);
	public static final int XmNpositionIndex = malloc ("positionIndex", 2);
	public static final int XmNprocessingDirection = malloc ("processingDirection", 1);
	public static final int XmNrecomputeSize = malloc ("recomputeSize", 1);
	public static final int XmNresizable = malloc ("resizable", 1);
	public static final int XmNresizePolicy = malloc ("resizePolicy", 1);
	public static final int XmNrightAttachment = malloc ("rightAttachment", 1);
	public static final int XmNscaleMultiple = malloc ("scaleMultiple", 4);
	public static final int XmNscrollHorizontal = malloc ("scrollHorizontal", 1);
	public static final int XmNscrollVertical = malloc ("scrollVertical", 1);
	public static final int XmNselectColor = malloc ("selectColor", 1);
	public static final int XmNselectedItemCount = malloc ("selectedItemCount", 4);
	public static final int XmNselectedItems = malloc ("selectedItems", 4);
	public static final int XmNselectedPosition = malloc ("selectedPosition", 4);
	public static final int XmNselectionArrayCount = malloc ("selectionArrayCount", 4);
	public static final int XmNselectionCallback = malloc ("selectionCallback", 4);
	public static final int XmNselectionPolicy = malloc ("selectionPolicy", 1);
	public static final int XmNsensitive = malloc ("sensitive", 1);
	public static final int XmNseparatorType = malloc ("separatorType", 1);
	public static final int XmNset = malloc ("set", 1);
	public static final int XmNshadowThickness = malloc ("shadowThickness", 2);
	public static final int XmNshadowType = malloc ("shadowType", 1);
	public static final int XmNshowArrows = malloc ("showArrows", 4);
	public static final int XmNshowAsDefault = malloc ("showAsDefault", 2);
	public static final int XmNsliderSize = malloc ("sliderSize", 4);
	public static final int XmNsliderVisual = malloc ("sliderVisual", 4);
	public static final int XmNsourceCursorIcon = malloc ("sourceCursorIcon", 4);
	public static final int XmNspinBoxChildType = malloc ("spinBoxChildType", 2);
	public static final int XmNspotLocation = malloc ("spotLocation", 4);
	public static final int XmNstateCursorIcon = malloc ("stateCursorIcon", 4);
	public static final int XmNsubMenuId = malloc ("subMenuId", 4);
	public static final int XmNsubstitute = malloc ("substitute", 4);
	public static final int XmNtabList = malloc ("tabList", 4);
	public static final int XmNtextField = malloc ("textField", 4);
	public static final int XmNtitle = malloc ("title", 4);
	public static final int XmNtitleString = malloc ("titleString", 4);
	public static final int XmNtoBottomCallback = malloc ("toBottomCallback", 4);
	public static final int XmNtoTopCallback = malloc ("toTopCallback", 4);
	public static final int XmNtopAttachment = malloc ("topAttachment", 1);
	public static final int XmNtopItemPosition = malloc ("topItemPosition", 4);
	public static final int XmNtopShadowColor = malloc ("topShadowColor", 4);
	public static final int XmNtransferProc = malloc ("transferProc", 4);
	public static final int XmNtransferStatus = malloc ("transferStatus", 4);
	public static final int XmNtraversalOn = malloc ("traversalOn", 1);
	public static final int XmNtroughColor = malloc ("troughColor", 4);
	public static final int XmNunmapCallback = malloc ("unmapCallback", 4);
	public static final int XmNuserData = malloc ("userData", 4);
	public static final int XmNvalue = malloc ("value", 4);
	public static final int XmNvalueChangedCallback = malloc ("valueChangedCallback", 4);
	public static final int XmNverifyBell = malloc ("verifyBell", 1);
	public static final int XmNverticalScrollBar = malloc ("verticalScrollBar", 4);
	public static final int XmNvisibleItemCount = malloc ("visibleItemCount", 4);
	public static final int XmNvisual = malloc ("visual", 4);
	public static final int XmNUMERIC = 0x3;
	public static final int XmNwidth = malloc ("width", 2);
	public static final int XmNwordWrap = malloc ("wordWrap", 1);
	public static final int XmNx = malloc ("x", 2);
	public static final int XmNy = malloc ("y", 2);
	public static final int XmONE_OF_MANY = 0x2;
	public static final int XmOUTPUT_ALL = 0x0;
	public static final int XmPATH_MODE_FULL = 0x0;
	public static final int XmPIXELS = 0x0;
	public static final int XmPIXMAP = 0x1;
	public static final int XmRELATIVE = 0x1;
	public static final int XmRESIZE_NONE = 0x0;
	public static final int XmREVERSED_GROUND_COLORS = -2;
	public static final int XmSET = 0x1;
	public static final int XmSHADOW_ETCHED_IN = 0x5;
	public static final int XmSHADOW_ETCHED_OUT = 0x6;
	public static final int XmSHADOW_IN = 0x7;
	public static final int XmSHADOW_OUT = 0x8;
	public static final int XmSINGLE_LINE = 0x1;
	public static final int XmSINGLE_LINE_EDIT = 0x1;
	public static final int XmSTRING = 0x2;
	public static final int XmSTRING_COMPONENT_SEPARATOR = 0x4;
	public static final int XmSTRING_COMPONENT_TAB = 0xC;
	public static final int XmTRANSFER_FAILURE = 0;
	public static final int XmTRAVERSE_CURRENT = 0x0;
	public static final int XmUNSET = 0x0;
	public static final int XmUNSPECIFIED_PIXMAP = 0x2;
	public static final int XmVARIABLE = 0x0;
	public static final int XmVERTICAL = 0x1;
	public static final int XtGrabNone = 0x0;
	public static final int XtIMAlternateInput = 0x4;
	public static final int XtIMTimer = 0x2;
	public static final int XtIMXEvent = 0x1;
	public static final int XtInputReadMask = 1;
	public static final int XtListTail = 0x1;
	public static final int WindingRule = 1;
	public static final int ZPixmap = 2;
	
	
	static int malloc (String name, int length) {
		int strLen = name.length ();
		if (NextResourceStart + strLen + 2 > RESOURCE_START + RESOURCE_LENGTH) {
			System.out.println ("*** Warning : SWT - Resource overrun.  Increase OS.RESOURCE_LENGTH.");
			System.out.println ("*** Warning : Exiting ...");
			System.exit (0);
		}
		char [] unicode = new char [strLen];
		name.getChars (0, strLen, unicode, 0);
		byte[] buffer = new byte [strLen + 2];
		buffer [0] = (byte) length;
		for (int i = 0; i < strLen; i++) {
			buffer [i+1] = (byte) unicode[i];
		}
		OS.memmove (NextResourceStart, buffer, strLen + 2);
		int result = NextResourceStart + 1;
		NextResourceStart += strLen + 2;
		return result;
	}
	/** @method flags=no_gen */
static final native int setResourceMem (int start, int end);

/** X render natives and constants */
public static final int PictStandardARGB32 = 0;
public static final int PictStandardRGB24 = 1;
public static final int PictStandardA8 = 2;
public static final int PictStandardA4 = 3;
public static final int PictStandardA1 = 4;
public static final int PictOpSrc = 1;
public static final int PictOpOver = 3;

public static final native int XRenderPictureAttributes_sizeof();
/** @method flags=dynamic */
public static final native boolean _XRenderQueryExtension(int /*long*/ display, int[] event_basep, int[] error_basep);
public static final boolean XRenderQueryExtension(int /*long*/ display, int[] event_basep, int[] error_basep) {
	lock.lock();
	try {
		return _XRenderQueryExtension(display, event_basep, error_basep);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int _XRenderQueryVersion(int /*long*/ display, int[] major_versionp, int[] minor_versionp);
public static final int XRenderQueryVersion(int /*long*/ display, int[] major_versionp, int[] minor_versionp) {
	lock.lock();
	try {
		return _XRenderQueryVersion(display, major_versionp, minor_versionp);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _XRenderCreatePicture(int /*long*/ display, int /*long*/ drawable, int /*long*/ format, int /*long*/ valuemask, XRenderPictureAttributes attributes);
public static final int /*long*/ XRenderCreatePicture(int /*long*/ display, int /*long*/ drawable, int /*long*/ format, int /*long*/ valuemask, XRenderPictureAttributes attributes) {
	lock.lock();
	try {
		return _XRenderCreatePicture(display, drawable, format, valuemask, attributes);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _XRenderSetPictureClipRegion(int /*long*/ display, int /*long*/ picture, int /*long*/ region);
public static final void XRenderSetPictureClipRegion(int /*long*/ display, int /*long*/ picture, int /*long*/ region) {
	lock.lock();
	try {
		_XRenderSetPictureClipRegion(display, picture, region);
	} finally {
		lock.unlock();
	}
}

/** @method flags=dynamic */
public static final native void _XRenderSetPictureClipRectangles(int /*long*/ display, int /*long*/ picture, int xOrigin, int yOrigin, short[] rects, int count);
public static final void XRenderSetPictureClipRectangles(int /*long*/ display, int /*long*/ picture, int xOrigin, int yOrigin, short[] rects, int count) {
	lock.lock();
	try {
		_XRenderSetPictureClipRectangles(display, picture, xOrigin, yOrigin, rects, count);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _XRenderSetPictureTransform(int /*long*/ display, int /*long*/ picture, int[] transform);
public static final void XRenderSetPictureTransform(int /*long*/ display, int /*long*/ picture, int[] transform) {
	lock.lock();
	try {
		_XRenderSetPictureTransform(display, picture, transform);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _XRenderFreePicture(int /*long*/ display, int /*long*/ picture);
public static final void XRenderFreePicture(int /*long*/ display, int /*long*/ picture) {
	lock.lock();
	try {
		_XRenderFreePicture(display, picture);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native void _XRenderComposite(int /*long*/ display, int op, int /*long*/ src, int /*long*/ mask, int /*long*/ dst, int src_x, int src_y, int mask_x, int mask_y, int dst_x, int dst_y, int width, int height);
public static final void XRenderComposite(int /*long*/ display, int op, int /*long*/ src, int /*long*/ mask, int /*long*/ dst, int src_x, int src_y, int mask_x, int mask_y, int dst_x, int dst_y, int width, int height) {
	lock.lock();
	try {
		_XRenderComposite(display, op, src, mask, dst, src_x, src_y, mask_x, mask_y, dst_x, dst_y, width, height);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _XRenderFindStandardFormat(int /*long*/ display, int format);
public static final int /*long*/ XRenderFindStandardFormat(int /*long*/ display, int format) {
	lock.lock();
	try {
		return _XRenderFindStandardFormat(display, format);
	} finally {
		lock.unlock();
	}
}
/** @method flags=dynamic */
public static final native int /*long*/ _XRenderFindVisualFormat(int /*long*/ display, int /*long*/ visual);
public static final int /*long*/ XRenderFindVisualFormat(int /*long*/ display, int /*long*/ visual) {
	lock.lock();
	try {
		return _XRenderFindVisualFormat(display, visual);
	} finally {
		lock.unlock();
	}
}

/** @param handle cast=(void *) */
public static final native int _dlclose(int /*long*/ handle);
public static final int dlclose(int /*long*/ handle) {
	lock.lock();
	try {
		return _dlclose(handle);
	} finally {
		lock.unlock();
	}
}
/** @param filename cast=(const char *) */
public static final native int /*long*/ _dlopen(byte[] filename, int flag);
public static final int /*long*/ dlopen(byte[] filename, int flag) {
	lock.lock();
	try {
		return _dlopen(filename, flag);
	} finally {
		lock.unlock();
	}
}
/**
 * @param handle cast=(void *)
 * @param symbol cast=(const char *)
 */
public static final native int /*long*/ _dlsym(int /*long*/ handle, byte[] symbol);
public static final int /*long*/ dlsym(int /*long*/ handle, byte[] symbol) {
	lock.lock();
	try {
		return _dlsym(handle, symbol);
	} finally {
		lock.unlock();
	}
}

/** JNI native methods */

/** @method flags=no_gen */
public static final native int MonitorEnter(Object object);
/** @method flags=no_gen */
public static final native int MonitorExit(Object object);

/** Natives */
public static final native int _Call(int proc, int arg1, int arg2);
public static final int Call(int proc, int arg1, int arg2) {
	lock.lock();
	try {
		return _Call(proc, arg1, arg2);
	} finally {
		lock.unlock();
	}
}
public static final native int _ConnectionNumber(int display);
public static final int ConnectionNumber(int display) {
	lock.lock();
	try {
		return _ConnectionNumber(display);
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int CODESET();
/** @param fd_set cast=(fd_set *) */
public static final native boolean FD_ISSET(int fd, byte[] fd_set);
/** @param fd_set cast=(fd_set *) */
public static final native void FD_SET(int fd, byte[] fd_set);
/** @param fd_set cast=(fd_set *) */
public static final native void FD_ZERO(byte[] fd_set);
/** @method flags=const */
public static final native int LC_CTYPE();
/** @method flags=const */
public static final native int MB_CUR_MAX();
/**
 * @param path cast=(const char*)
 */
public static final native int _access (byte [] path, int amode);
public static final int access (byte [] path, int amode) {
	lock.lock();
	try {
		return _access(path, amode);
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int _applicationShellWidgetClass();
public static final int applicationShellWidgetClass() {
	lock.lock();
	try {
		return _applicationShellWidgetClass();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int _overrideShellWidgetClass();
public static final int overrideShellWidgetClass() {
	lock.lock();
	try {
		return _overrideShellWidgetClass();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int _shellWidgetClass();
public static final int shellWidgetClass() {
	lock.lock();
	try {
		return _shellWidgetClass();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int _topLevelShellWidgetClass();
public static final int topLevelShellWidgetClass() {
	lock.lock();
	try {
		return _topLevelShellWidgetClass();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int _transientShellWidgetClass();
public static final int transientShellWidgetClass() {
	lock.lock();
	try {
		return _transientShellWidgetClass();
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int _xmMenuShellWidgetClass();
public static final int xmMenuShellWidgetClass() {
	lock.lock();
	try {
		return _xmMenuShellWidgetClass();
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XAllocColor(int display, int colormap, XColor color);
public static final int XAllocColor(int display, int colormap, XColor color) {
	lock.lock();
	try {
		return _XAllocColor(display, colormap, color);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native void _XBell(int display, int ms);
public static final void XBell(int display, int ms) {
	lock.lock();
	try {
		_XBell(display, ms);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XBlackPixel(int display, int screenNum);
public static final int XBlackPixel(int display, int screenNum) {
	lock.lock();
	try {
		return _XBlackPixel(display, screenNum);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param cursor cast=(Cursor)
 * @param time cast=(Time)
 */
public static final native int _XChangeActivePointerGrab(int display, int eventMask, int cursor, int time);
public static final int XChangeActivePointerGrab(int display, int eventMask, int cursor, int time) {
	lock.lock();
	try {
		return _XChangeActivePointerGrab(display, eventMask, cursor, time);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param w cast=(Window)
 * @param property cast=(Atom)
 * @param type cast=(Atom)
 * @param data cast=(unsigned char *)
 */
public static final native void _XChangeProperty(int display, int w, int property, int type, int format, int mode, int[] data, int nelements);
public static final void XChangeProperty(int display, int w, int property, int type, int format, int mode, int[] data, int nelements) {
	lock.lock();
	try {
		_XChangeProperty(display, w, property, type, format, mode, data, nelements);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native void _XChangeWindowAttributes(int display, int window, int mask, XSetWindowAttributes attributes);
public static final void XChangeWindowAttributes(int display, int window, int mask, XSetWindowAttributes attributes) {
	lock.lock();
	try {
		_XChangeWindowAttributes(display, window, mask, attributes);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param event_return cast=(XEvent *)
 * @param predicate cast=(Bool (*)())
 * @param arg cast=(XPointer)
 */
public static final native int _XCheckIfEvent(int display, int event_return, int predicate, int arg);
public static final int XCheckIfEvent(int display, int event_return, int predicate, int arg) {
	lock.lock();
	try {
		return _XCheckIfEvent(display, event_return, predicate, arg);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param event cast=(XEvent *)
 */
public static final native boolean _XCheckMaskEvent(int display, int mask, int event);
public static final boolean XCheckMaskEvent(int display, int mask, int event) {
	lock.lock();
	try {
		return _XCheckMaskEvent(display, mask, event);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param event cast=(XEvent *)
 */
public static final native boolean _XCheckWindowEvent(int display, int window, int mask, int event);
public static final boolean XCheckWindowEvent(int display, int window, int mask, int event) {
	lock.lock();
	try {
		return _XCheckWindowEvent(display, window, mask, event);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native void _XClearArea(int display, int window, int x, int y, int width, int height, boolean exposures);
public static final void XClearArea(int display, int window, int x, int y, int width, int height, boolean exposures) {
	lock.lock();
	try {
		_XClearArea(display, window, x, y, width, height, exposures);
	} finally {
		lock.unlock();
	}
}
/**
 * @param region cast=(Region)
 * @param rectangle cast=(XRectangle *)
 */
public static final native void _XClipBox(int region, XRectangle rectangle);
public static final void XClipBox(int region, XRectangle rectangle) {
	lock.lock();
	try {
		_XClipBox(region, rectangle);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native void _XCloseDisplay(int display);
public static final void XCloseDisplay(int display) {
	lock.lock();
	try {
		_XCloseDisplay(display);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 */
public static final native void _XCopyArea(int display, int src, int dest, int gc, int src_x, int src_y, int width, int height, int dest_x, int dest_y);
public static final void XCopyArea(int display, int src, int dest, int gc, int src_x, int src_y, int width, int height, int dest_x, int dest_y) {
	lock.lock();
	try {
		_XCopyArea(display, src, dest, gc, src_x, src_y, width, height, dest_x, dest_y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 */
public static final native void _XCopyPlane(int display, int src, int dest, int gc, int src_x, int src_y, int width, int height, int dest_x, int dest_y, int plane);
public static final void XCopyPlane(int display, int src, int dest, int gc, int src_x, int src_y, int width, int height, int dest_x, int dest_y, int plane) {
	lock.lock();
	try {
		_XCopyPlane(display, src, dest, gc, src_x, src_y, width, height, dest_x, dest_y, plane);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param data cast=(char *)
 */
public static final native int _XCreateBitmapFromData(int display, int drawable, byte[] data, int width, int height);
public static final int XCreateBitmapFromData(int display, int drawable, byte[] data, int width, int height) {
	lock.lock();
	try {
		return _XCreateBitmapFromData(display, drawable, data, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param visual cast=(Visual *)
 */
public static final native int _XCreateColormap(int display, int window, int visual, int alloc);
public static final int XCreateColormap(int display, int window, int visual, int alloc) {
	lock.lock();
	try {
		return _XCreateColormap(display, window, visual, alloc);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XCreateFontCursor(int display, int shape);
public static final int XCreateFontCursor(int display, int shape) {
	lock.lock();
	try {
		return _XCreateFontCursor(display, shape);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XCreateGC(int display, int window, int mask, XGCValues values);
public static final int XCreateGC(int display, int window, int mask, XGCValues values) {
	lock.lock();
	try {
		return _XCreateGC(display, window, mask, values);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param visual cast=(Visual *)
 * @param data cast=(char *)
 */
public static final native int _XCreateImage(int display, int visual, int depth, int format, int offset, int data, int width, int height, int bitmap_pad, int bytes_per_line);
public static final int XCreateImage(int display, int visual, int depth, int format, int offset, int data, int width, int height, int bitmap_pad, int bytes_per_line) {
	lock.lock();
	try {
		return _XCreateImage(display, visual, depth, format, offset, data, width, height, bitmap_pad, bytes_per_line);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XCreatePixmap(int display, int drawable, int width, int height, int depth);
public static final int XCreatePixmap(int display, int drawable, int width, int height, int depth) {
	lock.lock();
	try {
		return _XCreatePixmap(display, drawable, width, height, depth);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param source cast=(Pixmap)
 * @param mask cast=(Pixmap)
 */
public static final native int _XCreatePixmapCursor(int display, int source, int mask, XColor foreground_color, XColor background_color, int x, int y);
public static final int XCreatePixmapCursor(int display, int source, int mask, XColor foreground_color, XColor background_color, int x, int y) {
	lock.lock();
	try {
		return _XCreatePixmapCursor(display, source, mask, foreground_color, background_color, x, y);
	} finally {
		lock.unlock();
	}
}
public static final native int _XCreateRegion();
public static final int XCreateRegion() {
	lock.lock();
	try {
		return _XCreateRegion();
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param parent cast=(Window)
 * @param visual cast=(Visual *)
 * @param attributes cast=(XSetWindowAttributes *)
 */
public static final native int _XCreateWindow(int display, int parent, int x, int y, int width, int height, int border_width, int depth, int clazz, int visual, long value_mask, XSetWindowAttributes attributes);
public static final int XCreateWindow(int display, int parent, int x, int y, int width, int height, int border_width, int depth, int clazz, int visual, long value_mask, XSetWindowAttributes attributes) {
	lock.lock();
	try {
		return _XCreateWindow(display, parent, x, y, width, height, border_width, depth, clazz, visual, value_mask, attributes);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XDefaultColormap(int display, int screen_number);
public static final int XDefaultColormap(int display, int screen_number) {
	lock.lock();
	try {
		return _XDefaultColormap(display, screen_number);
	} finally {
		lock.unlock();
	}
}
/** @param screen cast=(Screen *) */
public static final native int _XDefaultColormapOfScreen(int screen);
public static final int XDefaultColormapOfScreen(int screen) {
	lock.lock();
	try {
		return _XDefaultColormapOfScreen(screen);
	} finally {
		lock.unlock();
	}
}
/** @param screen cast=(Screen *) */
public static final native int _XDefaultDepthOfScreen(int screen);
public static final int XDefaultDepthOfScreen(int screen) {
	lock.lock();
	try {
		return _XDefaultDepthOfScreen(screen);
	} finally {
		lock.unlock();
	}
}
/** @param screen cast=(Screen *) */
public static final native int _XDefaultGCOfScreen(int screen);
public static final int XDefaultGCOfScreen(int screen) {
	lock.lock();
	try {
		return _XDefaultGCOfScreen(screen);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XDefaultRootWindow(int display);
public static final int XDefaultRootWindow(int display) {
	lock.lock();
	try {
		return _XDefaultRootWindow(display);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XDefaultScreen(int display);
public static final int XDefaultScreen(int display) {
	lock.lock();
	try {
		return _XDefaultScreen(display);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XDefaultScreenOfDisplay(int display);
public static final int XDefaultScreenOfDisplay(int display) {
	lock.lock();
	try {
		return _XDefaultScreenOfDisplay(display);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XDefaultVisual(int display, int screen_number);
public static final int XDefaultVisual(int display, int screen_number) {
	lock.lock();
	try {
		return _XDefaultVisual(display, screen_number);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native void _XDefineCursor(int display, int window, int cursor);
public static final void XDefineCursor(int display, int window, int cursor) {
	lock.lock();
	try {
		_XDefineCursor(display, window, cursor);
	} finally {
		lock.unlock();
	}
}
/** @param ximage cast=(XImage *) */
public static final native int _XDestroyImage(int ximage);
public static final int XDestroyImage(int ximage) {
	lock.lock();
	try {
		return _XDestroyImage(ximage);
	} finally {
		lock.unlock();
	}
}
/** @param region cast=(Region) */
public static final native void _XDestroyRegion(int region);
public static final void XDestroyRegion(int region) {
	lock.lock();
	try {
		_XDestroyRegion(region);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param w cast=(Window)
 */
public static final native void _XDestroyWindow(int display, int w);
public static final void XDestroyWindow(int display, int w) {
	lock.lock();
	try {
		_XDestroyWindow(display, w);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XDisplayHeight(int display, int screen);
public static final int XDisplayHeight(int display, int screen) {
	lock.lock();
	try {
		return _XDisplayHeight(display, screen);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XDisplayHeightMM(int display, int screen);
public static final int XDisplayHeightMM(int display, int screen) {
	lock.lock();
	try {
		return _XDisplayHeightMM(display, screen);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XDisplayWidth(int display, int screen);
public static final int XDisplayWidth(int display, int screen) {
	lock.lock();
	try {
		return _XDisplayWidth(display, screen);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XDisplayWidthMM(int display, int screen);
public static final int XDisplayWidthMM(int display, int screen) {
	lock.lock();
	try {
		return _XDisplayWidthMM(display, screen);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param drawable cast=(Drawable)
 * @param gc cast=(GC)
 */
public static final native void _XDrawArc(int display, int drawable, int gc, int x1, int y1, int x2, int y2, int a1, int a2);
public static final void XDrawArc(int display, int drawable, int gc, int x1, int y1, int x2, int y2, int a1, int a2) {
	lock.lock();
	try {
		_XDrawArc(display, drawable, gc, x1, y1, x2, y2, a1, a2);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param drawable cast=(Drawable)
 * @param gc cast=(GC)
 */
public static final native void _XDrawLine(int display, int drawable, int gc, int x1, int y1, int x2, int y2);
public static final void XDrawLine(int display, int drawable, int gc, int x1, int y1, int x2, int y2) {
	lock.lock();
	try {
		_XDrawLine(display, drawable, gc, x1, y1, x2, y2);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param drawable cast=(Drawable)
 * @param gc cast=(GC)
 * @param xPoints cast=(XPoint *)
 */
public static final native void _XDrawLines(int display, int drawable, int gc, short[] xPoints, int nPoints, int mode);
public static final void XDrawLines(int display, int drawable, int gc, short[] xPoints, int nPoints, int mode) {
	lock.lock();
	try {
		_XDrawLines(display, drawable, gc, xPoints, nPoints, mode);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param drawable cast=(Drawable)
 * @param gc cast=(GC)
 */
public static final native void _XDrawRectangle(int display, int drawable, int gc, int x, int y, int width, int height);
public static final void XDrawRectangle(int display, int drawable, int gc, int x, int y, int width, int height) {
	lock.lock();
	try {
		_XDrawRectangle(display, drawable, gc, x, y, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param drawable cast=(Drawable)
 * @param gc cast=(GC)
 */
public static final native void _XDrawPoint(int display, int drawable, int gc, int x, int y);
public static final void XDrawPoint(int display, int drawable, int gc, int x, int y) {
	lock.lock();
	try {
		_XDrawPoint(display, drawable, gc, x, y);
	} finally {
		lock.unlock();
	}
}
/** @param region cast=(Region) */
public static final native boolean _XEmptyRegion(int region);
public static final boolean XEmptyRegion(int region) {
	lock.lock();
	try {
		return _XEmptyRegion(region);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XEventsQueued(int display, int mode);
public static final int XEventsQueued(int display, int mode) {
	lock.lock();
	try {
		return _XEventsQueued(display, mode);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param drawable cast=(Drawable)
 * @param gc cast=(GC)
 */
public static final native void _XFillArc(int display, int drawable, int gc, int x1, int y1, int x2, int y2, int a1, int a2);
public static final void XFillArc(int display, int drawable, int gc, int x1, int y1, int x2, int y2, int a1, int a2) {
	lock.lock();
	try {
		_XFillArc(display, drawable, gc, x1, y1, x2, y2, a1, a2);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param drawable cast=(Drawable)
 * @param gc cast=(GC)
 * @param xPoints cast=(XPoint *)
 */
public static final native int _XFillPolygon(int display, int drawable, int gc, short[] xPoints, int nPoints, int mode, int style);
public static final int XFillPolygon(int display, int drawable, int gc, short[] xPoints, int nPoints, int mode, int style) {
	lock.lock();
	try {
		return _XFillPolygon(display, drawable, gc, xPoints, nPoints, mode, style);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param drawable cast=(Drawable)
 * @param gc cast=(GC)
 */
public static final native void _XFillRectangle(int display, int drawable, int gc, int x, int y, int width, int height);
public static final void XFillRectangle(int display, int drawable, int gc, int x, int y, int width, int height) {
	lock.lock();
	try {
		_XFillRectangle(display, drawable, gc, x, y, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param event cast=(XEvent *)
 * @param window cast=(Window)
 */
public static final native boolean _XFilterEvent(int event, int window);
public static final boolean XFilterEvent(int event, int window) {
	lock.lock();
	try {
		return _XFilterEvent(event, window);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native void _XFlush(int display);
public static final void XFlush(int display) {
	lock.lock();
	try {
		_XFlush(display);
	} finally {
		lock.unlock();
	}
}
/**
 * @param fontSet cast=(XFontSet)
 * @param fontStructs cast=(XFontStruct ***)
 * @param fontNames cast=(char ***)
 */
public static final native int _XFontsOfFontSet(int fontSet, int[] fontStructs, int[] fontNames);
public static final int XFontsOfFontSet(int fontSet, int[] fontStructs, int[] fontNames) {
	lock.lock();
	try {
		return _XFontsOfFontSet(fontSet, fontStructs, fontNames);
	} finally {
		lock.unlock();
	}
}
/** @param address cast=(char *) */
public static final native int _XFree(int address);
public static final int XFree(int address) {
	lock.lock();
	try {
		return _XFree(address);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param colormap cast=(Colormap)
 */
public static final native int _XFreeColormap(int display, int colormap);
public static final int XFreeColormap(int display, int colormap) {
	lock.lock();
	try {
		return _XFreeColormap(display, colormap);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param pixels cast=(unsigned long *)
 */
public static final native int _XFreeColors(int display, int colormap, int[] pixels, int npixels, int planes);
public static final int XFreeColors(int display, int colormap, int[] pixels, int npixels, int planes) {
	lock.lock();
	try {
		return _XFreeColors(display, colormap, pixels, npixels, planes);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param pixmap cast=(Cursor)
 */
public static final native void _XFreeCursor(int display, int pixmap);
public static final void XFreeCursor(int display, int pixmap) {
	lock.lock();
	try {
		_XFreeCursor(display, pixmap);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param font_struct cast=(XFontStruct *)
 */
public static final native void _XFreeFont(int display, int font_struct);
public static final void XFreeFont(int display, int font_struct) {
	lock.lock();
	try {
		_XFreeFont(display, font_struct);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(char **) */
public static final native void _XFreeFontNames(int list);
public static final void XFreeFontNames(int list) {
	lock.lock();
	try {
		_XFreeFontNames(list);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 */
public static final native void _XFreeGC(int display, int gc);
public static final void XFreeGC(int display, int gc) {
	lock.lock();
	try {
		_XFreeGC(display, gc);
	} finally {
		lock.unlock();
	}
}
/** @param modmap cast=(XModifierKeymap *) */
public static final native void _XFreeModifiermap(int modmap);
public static final void XFreeModifiermap(int modmap) {
	lock.lock();
	try {
		_XFreeModifiermap(modmap);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param pixmap cast=(Pixmap)
 */
public static final native void _XFreePixmap(int display, int pixmap);
public static final void XFreePixmap(int display, int pixmap) {
	lock.lock();
	try {
		_XFreePixmap(display, pixmap);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(char **) */
public static final native void _XFreeStringList(int list);
public static final void XFreeStringList(int list) {
	lock.lock();
	try {
		_XFreeStringList(list);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 */
public static final native int _XGetGCValues(int display, int gc, int valuemask, XGCValues values);
public static final int XGetGCValues(int display, int gc, int valuemask, XGCValues values) {
	lock.lock();
	try {
		return _XGetGCValues(display, gc, valuemask, values);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param drawable cast=(Drawable)
 * @param root_return cast=(Window *)
 * @param x_return cast=(int *)
 * @param y_return cast=(int *)
 * @param width_return cast=(unsigned int *)
 * @param height_return cast=(unsigned int *)
 * @param border_width_return cast=(unsigned int *)
 * @param depth_return cast=(unsigned int *)
 */
public static final native int _XGetGeometry(int display, int drawable, int[] root_return, int[] x_return, int[] y_return, int[] width_return, int[] height_return, int[] border_width_return, int[] depth_return);
public static final int XGetGeometry(int display, int drawable, int[] root_return, int[] x_return, int[] y_return, int[] width_return, int[] height_return, int[] border_width_return, int[] depth_return) {
	lock.lock();
	try {
		return _XGetGeometry(display, drawable, root_return, x_return, y_return, width_return, height_return, border_width_return, depth_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param drawable cast=(Drawable)
 */
public static final native int _XGetImage(int display, int drawable, int x, int y, int width, int height, int plane_mask, int format);
public static final int XGetImage(int display, int drawable, int x, int y, int width, int height, int plane_mask, int format) {
	lock.lock();
	try {
		return _XGetImage(display, drawable, x, y, width, height, plane_mask, format);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window *)
 * @param revert cast=(int *)
 */
public static final native int _XGetInputFocus(int display, int[] window, int[] revert);
public static final int XGetInputFocus(int display, int[] window, int[] revert) {
	lock.lock();
	try {
		return _XGetInputFocus(display, window, revert);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native boolean _XGetWindowAttributes(int display, int window, XWindowAttributes attributes);
public static final boolean XGetWindowAttributes(int display, int window, XWindowAttributes attributes) {
	lock.lock();
	try {
		return _XGetWindowAttributes(display, window, attributes);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param property cast=(Atom)
 * @param delete cast=(Bool)
 * @param req_type cast=(Atom)
 * @param actual_type_return cast=(Atom *)
 * @param actual_format_return cast=(int *)
 * @param nitems_return cast=(unsigned long *)
 * @param bytes_after_return cast=(unsigned long *)
 * @param prop_return cast=(unsigned char **)
 */
public static final native int _XGetWindowProperty(int display, int window, int property, int offset, int length, boolean delete, int req_type, int[] actual_type_return, int[] actual_format_return, int[] nitems_return, int[] bytes_after_return, int[] prop_return);
public static final int XGetWindowProperty(int display, int window, int property, int offset, int length, boolean delete, int req_type, int[] actual_type_return, int[] actual_format_return, int[] nitems_return, int[] bytes_after_return, int[] prop_return) {
	lock.lock();
	try {
		return _XGetWindowProperty(display, window, property, offset, length, delete, req_type, actual_type_return, actual_format_return, nitems_return, bytes_after_return, prop_return);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XGrabKeyboard(int display, int grabWindow, int ownerEvents, int pointerMode, int keyboardMode, int time);
public static final int XGrabKeyboard(int display, int grabWindow, int ownerEvents, int pointerMode, int keyboardMode, int time) {
	lock.lock();
	try {
		return _XGrabKeyboard(display, grabWindow, ownerEvents, pointerMode, keyboardMode, time);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XGrabPointer(int display, int grabWindow, int ownerEvents, int eventMask, int pointerMode, int keyboardMode, int confineToWindow, int cursor, int time);
public static final int XGrabPointer(int display, int grabWindow, int ownerEvents, int eventMask, int pointerMode, int keyboardMode, int confineToWindow, int cursor, int time) {
	lock.lock();
	try {
		return _XGrabPointer(display, grabWindow, ownerEvents, eventMask, pointerMode, keyboardMode, confineToWindow, cursor, time);
	} finally {
		lock.unlock();
	}
}
public static final native int _XInitThreads();
public static final int XInitThreads() {
	lock.lock();
	try {
		return _XInitThreads();
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param name cast=(char *)
 */
public static final native int _XInternAtom(int display, byte[] name, boolean ifExists);
public static final int XInternAtom(int display, byte[] name, boolean ifExists) {
	lock.lock();
	try {
		return _XInternAtom(display, name, ifExists);
	} finally {
		lock.unlock();
	}
}
/**
 * @param sra cast=(Region)
 * @param srb cast=(Region)
 * @param dr_return cast=(Region)
 */
public static final native void _XIntersectRegion(int sra, int srb, int dr_return);
public static final void XIntersectRegion(int sra, int srb, int dr_return) {
	lock.lock();
	try {
		_XIntersectRegion(sra, srb, dr_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param keysym cast=(KeySym)
 */
public static final native int _XKeysymToKeycode(int display, int keysym);
public static final int XKeysymToKeycode(int display, int keysym) {
	lock.lock();
	try {
		return _XKeysymToKeycode(display, keysym);
	} finally {
		lock.unlock();
	}
}
public static final native int _XKeysymToString(int keysym);
public static final int XKeysymToString(int keysym) {
	lock.lock();
	try {
		return _XKeysymToString(keysym);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param pattern cast=(char *)
 * @param actual_count_return cast=(int *)
 */
public static final native int _XListFonts(int display, byte[] pattern, int maxnames, int[] actual_count_return);
public static final int XListFonts(int display, byte[] pattern, int maxnames, int[] actual_count_return) {
	lock.lock();
	try {
		return _XListFonts(display, pattern, maxnames, actual_count_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param num_prop_return cast=(int *)
 */
public static final native int _XListProperties(int display, int window, int[] num_prop_return);
public static final int XListProperties(int display, int window, int[] num_prop_return) {
	lock.lock();
	try {
		return _XListProperties(display, window, num_prop_return);
	} finally {
		lock.unlock();
	}
}
/** @param fontSet cast=(XFontSet) */
public static final native int _XLocaleOfFontSet(int fontSet);
public static final int XLocaleOfFontSet(int fontSet) {
	lock.lock();
	try {
		return _XLocaleOfFontSet(fontSet);
	} finally {
		lock.unlock();
	}
}
/**
 * @param event cast=(XKeyEvent *)
 * @param string cast=(char *)
 * @param keysym cast=(KeySym *)
 * @param status cast=(XComposeStatus *)
 */
public static final native int _XLookupString(XKeyEvent event, byte[] string, int size, int[] keysym, int[] status);
public static final int XLookupString(XKeyEvent event, byte[] string, int size, int[] keysym, int[] status) {
	lock.lock();
	try {
		return _XLookupString(event, string, size, keysym, status);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 */
public static final native int _XLowerWindow(int display, int window);
public static final int XLowerWindow(int display, int window) {
	lock.lock();
	try {
		return _XLowerWindow(display, window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param w cast=(Window)
 */
public static final native void _XMapWindow(int display, int w);
public static final void XMapWindow(int display, int w) {
	lock.lock();
	try {
		_XMapWindow(display, w);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XGetModifierMapping(int display);
public static final int XGetModifierMapping(int display) {
	lock.lock();
	try {
		return _XGetModifierMapping(display);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param w cast=(Window)
 * @param size_list_return cast=(XIconSize **)
 * @param count_return cast=(int *)
 */
public static final native int _XGetIconSizes(int display, int w, int[] size_list_return, int[] count_return);
public static final int XGetIconSizes(int display, int w, int[] size_list_return, int[] count_return) {
	lock.lock();
	try {
		return _XGetIconSizes(display, w, size_list_return, count_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param w cast=(Window)
 */
public static final native void _XMoveResizeWindow(int display, int w, int x, int y, int width, int height);
public static final void XMoveResizeWindow(int display, int w, int x, int y, int width, int height) {
	lock.lock();
	try {
		_XMoveResizeWindow(display, w, x, y, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param r cast=(Region) */
public static final native int _XOffsetRegion(int r, int dx, int dy);
public static final int XOffsetRegion(int r, int dx, int dy) {
	lock.lock();
	try {
		return _XOffsetRegion(r, dx, dy);
	} finally {
		lock.unlock();
	}
}
/** @param display_name cast=(char *) */
public static final native int _XOpenDisplay(byte[] display_name);
public static final int XOpenDisplay(byte[] display_name) {
	lock.lock();
	try {
		return _XOpenDisplay(display_name);
	} finally {
		lock.unlock();
	}
}
/** @param region cast=(Region) */
public static final native boolean _XPointInRegion(int region, int x, int y);
public static final boolean XPointInRegion(int region, int x, int y) {
	lock.lock();
	try {
		return _XPointInRegion(region, x, y);
	} finally {
		lock.unlock();
	}
}
/** @param points cast=(XPoint *) */
public static final native int _XPolygonRegion(short[] points, int n, int fill_rule);
public static final int XPolygonRegion(short[] points, int n, int fill_rule) {
	lock.lock();
	try {
		return _XPolygonRegion(points, n, fill_rule);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param drawable cast=(Drawable)
 * @param gc cast=(GC)
 * @param image cast=(XImage *)
 */
public static final native int _XPutImage(int display, int drawable, int gc, int image, int srcX, int srcY, int destX, int destY, int width, int height);
public static final int XPutImage(int display, int drawable, int gc, int image, int srcX, int srcY, int destX, int destY, int width, int height) {
	lock.lock();
	try {
		return _XPutImage(display, drawable, gc, image, srcX, srcY, destX, destY, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XQueryColor(int display, int colormap, XColor color);
public static final int XQueryColor(int display, int colormap, XColor color) {
	lock.lock();
	try {
		return _XQueryColor(display, colormap, color);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param width_return cast=(unsigned int *)
 * @param height_return cast=(unsigned int *)
 */
public static final native int _XQueryBestCursor(int display, int d, int width, int height, int[] width_return, int[] height_return);
public static final int XQueryBestCursor(int display, int d, int width, int height, int[] width_return, int[] height_return) {
	lock.lock();
	try {
		return _XQueryBestCursor(display, d, width, height, width_return, height_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param root cast=(Window *)
 * @param child cast=(Window *)
 * @param rootX cast=(int *)
 * @param rootY cast=(int *)
 * @param windowX cast=(int *)
 * @param windowY cast=(int *)
 * @param mask cast=(unsigned int *)
 */
public static final native int _XQueryPointer(int display, int window, int[] root, int[] child, int[] rootX, int[] rootY, int[] windowX, int[] windowY, int[] mask);
public static final int XQueryPointer(int display, int window, int[] root, int[] child, int[] rootX, int[] rootY, int[] windowX, int[] windowY, int[] mask) {
	lock.lock();
	try {
		return _XQueryPointer(display, window, root, child, rootX, rootY, windowX, windowY, mask);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param root_return cast=(Window *)
 * @param parent_return cast=(Window *)
 * @param children_return cast=(Window **)
 * @param nChildren_return cast=(unsigned int *)
 */
public static final native int _XQueryTree(int display, int window, int[] root_return, int[] parent_return, int[] children_return, int[] nChildren_return);
public static final int XQueryTree(int display, int window, int[] root_return, int[] parent_return, int[] children_return, int[] nChildren_return) {
	lock.lock();
	try {
		return _XQueryTree(display, window, root_return, parent_return, children_return, nChildren_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 */
public static final native int _XRaiseWindow(int display, int window);
public static final int XRaiseWindow(int display, int window) {
	lock.lock();
	try {
		return _XRaiseWindow(display, window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 */
public static final native int _XReconfigureWMWindow(int display, int window, int screen, int valueMask, XWindowChanges values);
public static final int XReconfigureWMWindow(int display, int window, int screen, int valueMask, XWindowChanges values) {
	lock.lock();
	try {
		return _XReconfigureWMWindow(display, window, screen, valueMask, values);
	} finally {
		lock.unlock();
	}
}
/** @param region cast=(Region) */
public static final native int _XRectInRegion(int region, int x, int y, int width, int height);
public static final int XRectInRegion(int region, int x, int y, int width, int height) {
	lock.lock();
	try {
		return _XRectInRegion(region, x, y, width, height);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param win cast=(Window)
 * @param parent cast=(Window)
 */
public static final native int _XReparentWindow(int display, int win, int parent, int x, int y);
public static final int XReparentWindow(int display, int win, int parent, int x, int y) {
	lock.lock();
	try {
		return _XReparentWindow(display, win, parent, x, y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param w cast=(Window)
 */
public static final native void _XResizeWindow(int display, int w, int width, int height);
public static final void XResizeWindow(int display, int w, int width, int height) {
	lock.lock();
	try {
		_XResizeWindow(display, w, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param screen cast=(Screen *) */
public static final native int _XRootWindowOfScreen(int screen);
public static final int XRootWindowOfScreen(int screen) {
	lock.lock();
	try {
		return _XRootWindowOfScreen(screen);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 */
public static final native void _XSelectInput(int display, int window, int mask);
public static final void XSelectInput(int display, int window, int mask) {
	lock.lock();
	try {
		_XSelectInput(display, window, mask);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param propagate cast=(Bool)
 * @param event_mask cast=(long)
 * @param event cast=(XEvent *)
 */
public static final native int _XSendEvent(int display, int window, boolean propagate, int event_mask, int event);
public static final int XSendEvent(int display, int window, boolean propagate, int event_mask, int event) {
	lock.lock();
	try {
		return _XSendEvent(display, window, propagate, event_mask, event);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 */
public static final native void _XSetBackground(int display, int gc, int background);
public static final void XSetBackground(int display, int gc, int background) {
	lock.lock();
	try {
		_XSetBackground(display, gc, background);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 * @param pixmap cast=(Pixmap)
 */
public static final native void _XSetClipMask(int display, int gc, int pixmap);
public static final void XSetClipMask(int display, int gc, int pixmap) {
	lock.lock();
	try {
		_XSetClipMask(display, gc, pixmap);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 * @param rectangles cast=(XRectangle *)
 */
public static final native void _XSetClipRectangles(int display, int gc, int clip_x_origin, int clip_y_origin, XRectangle rectangles, int n, int ordering);
public static final void XSetClipRectangles(int display, int gc, int clip_x_origin, int clip_y_origin, XRectangle rectangles, int n, int ordering) {
	lock.lock();
	try {
		_XSetClipRectangles(display, gc, clip_x_origin, clip_y_origin, rectangles, n, ordering);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 * @param dash_list cast=(char *)
 */
public static final native int _XSetDashes(int display, int gc, int dash_offset, byte[] dash_list, int n);
public static final int XSetDashes(int display, int gc, int dash_offset, byte[] dash_list, int n) {
	lock.lock();
	try {
		return _XSetDashes(display, gc, dash_offset, dash_list, n);
	} finally {
		lock.unlock();
	}
}
/** @param handler cast=(XErrorHandler) */
public static final native int _XSetErrorHandler(int handler);
public static final int XSetErrorHandler(int handler) {
	lock.lock();
	try {
		return _XSetErrorHandler(handler);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 */
public static final native void _XSetFillRule(int display, int gc, int fill_rule);
public static final void XSetFillRule(int display, int gc, int fill_rule) {
	lock.lock();
	try {
		_XSetFillRule(display, gc, fill_rule);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 */
public static final native void _XSetFillStyle(int display, int gc, int fill_style);
public static final void XSetFillStyle(int display, int gc, int fill_style) {
	lock.lock();
	try {
		_XSetFillStyle(display, gc, fill_style);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param directories cast=(char **)
 */
public static final native int _XSetFontPath(int display, int directories, int ndirs);
public static final int XSetFontPath(int display, int directories, int ndirs) {
	lock.lock();
	try {
		return _XSetFontPath(display, directories, ndirs);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XGetFontPath(int display, int[] ndirs);
public static final int XGetFontPath(int display, int[] ndirs) {
	lock.lock();
	try {
		return _XGetFontPath(display,  ndirs);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(char **) */
public static final native int _XFreeFontPath(int list);
public static final int XFreeFontPath(int list) {
	lock.lock();
	try {
		return _XFreeFontPath(list);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 */
public static final native void _XSetForeground(int display, int gc, int foreground);
public static final void XSetForeground(int display, int gc, int foreground) {
	lock.lock();
	try {
		_XSetForeground(display, gc, foreground);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 */
public static final native void _XSetFunction(int display, int gc, int function);
public static final void XSetFunction(int display, int gc, int function) {
	lock.lock();
	try {
		_XSetFunction(display, gc, function);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 * @param graphics_exposures cast=(Bool)
 */
public static final native void _XSetGraphicsExposures(int display, int gc, boolean graphics_exposures);
public static final void XSetGraphicsExposures(int display, int gc, boolean graphics_exposures) {
	lock.lock();
	try {
		_XSetGraphicsExposures(display, gc, graphics_exposures);
	} finally {
		lock.unlock();
	}
}
/** @param handler cast=(XIOErrorHandler) */
public static final native int _XSetIOErrorHandler(int handler);
public static final int XSetIOErrorHandler(int handler) {
	lock.lock();
	try {
		return _XSetIOErrorHandler(handler);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 */
public static final native int _XSetInputFocus(int display, int window, int revert, int time);
public static final int XSetInputFocus(int display, int window, int revert, int time) {
	lock.lock();
	try {
		return _XSetInputFocus(display, window, revert, time);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 */
public static final native int _XSetLineAttributes(int display, int gc, int lineWidth, int lineStyle, int capStyle, int joinStyle);
public static final int XSetLineAttributes(int display, int gc, int lineWidth, int lineStyle, int capStyle, int joinStyle) {
	lock.lock();
	try {
		return _XSetLineAttributes(display, gc, lineWidth, lineStyle, capStyle, joinStyle);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 * @param region cast=(Region)
 */
public static final native void _XSetRegion(int display, int gc, int region);
public static final void XSetRegion(int display, int gc, int region) {
	lock.lock();
	try {
		_XSetRegion(display, gc, region);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 * @param pixmap cast=(Pixmap)
 */
public static final native void _XSetStipple(int display, int gc, int pixmap);
public static final void XSetStipple(int display, int gc, int pixmap) {
	lock.lock();
	try {
		_XSetStipple(display, gc, pixmap);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 */
public static final native void _XSetSubwindowMode(int display, int gc, int subwindow_mode);
public static final void XSetSubwindowMode(int display, int gc, int subwindow_mode) {
	lock.lock();
	try {
		_XSetSubwindowMode(display, gc, subwindow_mode);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 * @param pixmap cast=(Pixmap)
 */
public static final native void _XSetTile(int display, int gc, int pixmap);
public static final void XSetTile(int display, int gc, int pixmap) {
	lock.lock();
	try {
		_XSetTile(display, gc, pixmap);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param w cast=(Window)
 * @param prop_window cast=(Window)
 */
public static final native int _XSetTransientForHint(int display, int w, int prop_window);
public static final int XSetTransientForHint(int display, int w, int prop_window) {
	lock.lock();
	try {
		return _XSetTransientForHint(display, w, prop_window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param gc cast=(GC)
 */
public static final native void _XSetTSOrigin(int display, int gc, int ts_x_origin, int ts_y_origin);
public static final void XSetTSOrigin(int display, int gc, int ts_x_origin, int ts_y_origin) {
	lock.lock();
	try {
		_XSetTSOrigin(display, gc, ts_x_origin, ts_y_origin);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param w cast=(Window)
 * @param pixmap cast=(Pixmap)
 */
public static final native void _XSetWindowBackgroundPixmap(int display, int w, int pixmap);
public static final void XSetWindowBackgroundPixmap(int display, int w, int pixmap) {
	lock.lock();
	try {
		_XSetWindowBackgroundPixmap(display, w, pixmap);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param w cast=(Window)
 */
public static final native void _XSetWMNormalHints(int display, int w, XSizeHints hints);
public static final void XSetWMNormalHints(int display, int w, XSizeHints hints) {
	lock.lock();
	try {
		_XSetWMNormalHints(display, w, hints);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param dest cast=(Window)
 * @param src cast=(Pixmap)
 */
public static final native void _XShapeCombineMask(int display, int dest, int dest_kind, int x_off, int y_off, int src, int op);
public static final void XShapeCombineMask(int display, int dest, int dest_kind, int x_off, int y_off, int src, int op) {
	lock.lock();
	try {
		_XShapeCombineMask(display, dest, dest_kind, x_off, y_off, src, op);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param dest cast=(Window)
 * @param region cast=(Region)
 */
public static final native void _XShapeCombineRegion(int display, int dest, int dest_kind, int x_off, int y_off, int region, int op);
public static final void XShapeCombineRegion(int display, int dest, int dest_kind, int x_off, int y_off, int region, int op) {
	lock.lock();
	try {
		_XShapeCombineRegion(display, dest, dest_kind, x_off, y_off, region, op);
	} finally {
		lock.unlock();
	}
}
/**
 * @param sra cast=(Region)
 * @param srb cast=(Region)
 * @param dr_return cast=(Region)
 */
public static final native void _XSubtractRegion(int sra, int srb, int dr_return);
public static final void XSubtractRegion(int sra, int srb, int dr_return) {
	lock.lock();
	try {
		_XSubtractRegion(sra, srb, dr_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param discard cast=(Bool)
 */
public static final native void _XSync(int display, boolean discard);
public static final void XSync(int display, boolean discard) {
	lock.lock();
	try {
		_XSync(display, discard);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param onoff cast=(Bool)
 */
public static final native int _XSynchronize(int display, boolean onoff);
public static final int XSynchronize(int display, boolean onoff) {
	lock.lock();
	try {
		return _XSynchronize(display, onoff);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param is_press cast=(Bool)
 * @param delay cast=(unsigned long)
 */
public static final native void _XTestFakeButtonEvent(int display, int button, boolean is_press, int delay);
public static final void XTestFakeButtonEvent(int display, int button, boolean is_press, int delay) {
	lock.lock();
	try {
		_XTestFakeButtonEvent(display, button, is_press, delay);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param is_press cast=(Bool)
 * @param delay cast=(unsigned long)
 */
public static final native void _XTestFakeKeyEvent(int display, int keycode, boolean is_press, int delay);
public static final void XTestFakeKeyEvent(int display, int keycode, boolean is_press, int delay) {
	lock.lock();
	try {
		_XTestFakeKeyEvent(display, keycode, is_press, delay);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param delay cast=(unsigned long)
 */
public static final native void _XTestFakeMotionEvent(int display, int screen_number, int x, int y, int delay);
public static final void XTestFakeMotionEvent(int display, int screen_number, int x, int y, int delay) {
	lock.lock();
	try {
		_XTestFakeMotionEvent(display, screen_number, x, y, delay);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param src_w cast=(Window)
 * @param dest_w cast=(Window)
 * @param child_return cast=(Window *)
 */
public static final native boolean _XTranslateCoordinates(int display, int src_w, int dest_w, int src_x, int src_y, int[] dest_x_return, int[] dest_y_return, int[] child_return);
public static final boolean XTranslateCoordinates(int display, int src_w, int dest_w, int src_x, int src_y, int[] dest_x_return, int[] dest_y_return, int[] child_return) {
	lock.lock();
	try {
		return _XTranslateCoordinates(display, src_w, dest_w, src_x, src_y, dest_x_return, dest_y_return, child_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 */
public static final native void _XUndefineCursor(int display, int window);
public static final void XUndefineCursor(int display, int window) {
	lock.lock();
	try {
		_XUndefineCursor(display, window);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XUngrabKeyboard(int display, int time);
public static final int XUngrabKeyboard(int display, int time) {
	lock.lock();
	try {
		return _XUngrabKeyboard(display, time);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XUngrabPointer(int display, int time);
public static final int XUngrabPointer(int display, int time) {
	lock.lock();
	try {
		return _XUngrabPointer(display, time);
	} finally {
		lock.unlock();
	}
}
/**
 * @param rectangle cast=(XRectangle *)
 * @param src_region cast=(Region)
 * @param dest_region_return cast=(Region)
 */
public static final native void _XUnionRectWithRegion(XRectangle rectangle, int src_region, int dest_region_return);
public static final void XUnionRectWithRegion(XRectangle rectangle, int src_region, int dest_region_return) {
	lock.lock();
	try {
		_XUnionRectWithRegion(rectangle, src_region, dest_region_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param sra cast=(Region)
 * @param srb cast=(Region)
 * @param dr_return cast=(Region)
 */
public static final native void _XUnionRegion(int sra, int srb, int dr_return);
public static final void XUnionRegion(int sra, int srb, int dr_return) {
	lock.lock();
	try {
		_XUnionRegion(sra, srb, dr_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 */
public static final native void _XUnmapWindow(int display, int window);
public static final void XUnmapWindow(int display, int window) {
	lock.lock();
	try {
		_XUnmapWindow(display, window);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XWarpPointer(int display, int sourceWindow, int destWindow, int sourceX, int sourceY, int sourceWidth, int sourceHeight, int destX, int destY);
public static final int XWarpPointer(int display, int sourceWindow, int destWindow, int sourceX, int sourceY, int sourceWidth, int sourceHeight, int destX, int destY) {
	lock.lock();
	try {
		return _XWarpPointer(display, sourceWindow, destWindow, sourceX, sourceY, sourceWidth, sourceHeight, destX, destY);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XWhitePixel(int display, int screenNum);
public static final int XWhitePixel(int display, int screenNum) {
	lock.lock();
	try {
		return _XWhitePixel(display, screenNum);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 */
public static final native void _XWithdrawWindow(int display, int window, int screen);
public static final void XWithdrawWindow(int display, int window, int screen) {
	lock.lock();
	try {
		_XWithdrawWindow(display, window, screen);
	} finally {
		lock.unlock();
	}
}
/** @param dpy cast=(Display *) */
public static final native boolean _XineramaIsActive(int dpy);
public static final boolean XineramaIsActive(int dpy) {
	lock.lock();
	try {
		return _XineramaIsActive(dpy);
	} finally {
		lock.unlock();
	}
}
/** @param dpy cast=(Display *) */
public static final native int _XineramaQueryScreens(int dpy, int[] number);
public static final int XineramaQueryScreens(int dpy, int[] number) {
	lock.lock();
	try {
		return _XineramaQueryScreens(dpy, number);
	} finally {
		lock.unlock();
	}
}
/**
 * @param shell cast=(Widget)
 * @param protocol cast=(Atom)
 * @param callback cast=(XtCallbackProc)
 * @param closure cast=(XtPointer)
 */
public static final native void _XmAddWMProtocolCallback(int shell, int protocol, int callback, int closure);
public static final void XmAddWMProtocolCallback(int shell, int protocol, int callback, int closure) {
	lock.lock();
	try {
		_XmAddWMProtocolCallback(shell, protocol, callback, closure);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmChangeColor(int widget, int pixel);
public static final void XmChangeColor(int widget, int pixel) {
	lock.lock();
	try {
		_XmChangeColor(widget, pixel);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param format_name cast=(char *)
 * @param buffer cast=(char *)
 * @param data_id cast=(void *)
 */
public static final native int _XmClipboardCopy(int display, int window, int item_id, byte[] format_name, byte[] buffer, int length, int private_id, int[] data_id);
public static final int XmClipboardCopy(int display, int window, int item_id, byte[] format_name, byte[] buffer, int length, int private_id, int[] data_id) {
	lock.lock();
	try {
		return _XmClipboardCopy(display, window, item_id, format_name, buffer, length, private_id, data_id);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 */
public static final native int _XmClipboardEndCopy(int display, int window, int item_id);
public static final int XmClipboardEndCopy(int display, int window, int item_id) {
	lock.lock();
	try {
		return _XmClipboardEndCopy(display, window, item_id);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 */
public static final native int _XmClipboardEndRetrieve(int display, int window);
public static final int XmClipboardEndRetrieve(int display, int window) {
	lock.lock();
	try {
		return _XmClipboardEndRetrieve(display, window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param count cast=(int *)
 * @param max_format_name_length cast=(unsigned long *)
 */
public static final native int _XmClipboardInquireCount(int display, int window, int[] count, int[] max_format_name_length);
public static final int XmClipboardInquireCount(int display, int window, int[] count, int[] max_format_name_length) {
	lock.lock();
	try {
		return _XmClipboardInquireCount(display, window, count, max_format_name_length);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param format_name_buf cast=(char *)
 * @param copied_len cast=(unsigned long *)
 */
public static final native int _XmClipboardInquireFormat(int display, int window, int index, byte[] format_name_buf, int buffer_len, int[] copied_len);
public static final int XmClipboardInquireFormat(int display, int window, int index, byte[] format_name_buf, int buffer_len, int[] copied_len) {
	lock.lock();
	try {
		return _XmClipboardInquireFormat(display, window, index, format_name_buf, buffer_len, copied_len);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param widget cast=(Window)
 * @param format_name cast=(char *)
 * @param length cast=(unsigned long *)
 */
public static final native int _XmClipboardInquireLength(int display, int widget, byte[] format_name, int[] length);
public static final int XmClipboardInquireLength(int display, int widget, byte[] format_name, int[] length) {
	lock.lock();
	try {
		return _XmClipboardInquireLength(display, widget, format_name, length);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param format_name cast=(char *)
 * @param buffer cast=(char *)
 * @param num_bytes cast=(unsigned long *)
 * @param private_id cast=(long *)
 */
public static final native int _XmClipboardRetrieve(int display, int window, byte[] format_name, byte[] buffer, int length, int[] num_bytes, int[] private_id);
public static final int XmClipboardRetrieve(int display, int window, byte[] format_name, byte[] buffer, int length, int[] num_bytes, int[] private_id) {
	lock.lock();
	try {
		return _XmClipboardRetrieve(display, window, format_name, buffer, length, num_bytes, private_id);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param clip_label cast=(XmString)
 * @param widget cast=(Widget)
 * @param callback cast=(XmCutPasteProc)
 * @param item_id cast=(long *)
 */
public static final native int _XmClipboardStartCopy(int display, int window, int clip_label, int timestamp, int widget, int callback, int[] item_id);
public static final int XmClipboardStartCopy(int display, int window, int clip_label, int timestamp, int widget, int callback, int[] item_id) {
	lock.lock();
	try {
		return _XmClipboardStartCopy(display, window, clip_label, timestamp, widget, callback, item_id);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 */
public static final native int _XmClipboardStartRetrieve(int display, int window, int timestamp);
public static final int XmClipboardStartRetrieve(int display, int window, int timestamp) {
	lock.lock();
	try {
		return _XmClipboardStartRetrieve(display, window, timestamp);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param xmString cast=(XmString)
 */
public static final native void _XmComboBoxAddItem(int widget, int xmString, int position, boolean unique);
public static final void XmComboBoxAddItem(int widget, int xmString, int position, boolean unique) {
	lock.lock();
	try {
		_XmComboBoxAddItem(widget, xmString, position, unique);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmComboBoxDeletePos(int widget, int position);
public static final void XmComboBoxDeletePos(int widget, int position) {
	lock.lock();
	try {
		_XmComboBoxDeletePos(widget, position);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param xmString cast=(XmString)
 */
public static final native void _XmComboBoxSelectItem(int widget, int xmString);
public static final void XmComboBoxSelectItem(int widget, int xmString) {
	lock.lock();
	try {
		_XmComboBoxSelectItem(widget, xmString);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateArrowButton(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateArrowButton(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateArrowButton(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateCascadeButtonGadget(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateCascadeButtonGadget(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateCascadeButtonGadget(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateComboBox(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateComboBox(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateComboBox(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateDialogShell(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateDialogShell(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateDialogShell(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateDrawingArea(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateDrawingArea(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateDrawingArea(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateDrawnButton(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateDrawnButton(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateDrawnButton(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateErrorDialog(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateErrorDialog(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateErrorDialog(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateFileSelectionDialog(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateFileSelectionDialog(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateFileSelectionDialog(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateForm(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateForm(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateForm(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateFrame(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateFrame(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateFrame(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateInformationDialog(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateInformationDialog(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateInformationDialog(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateLabel(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateLabel(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateLabel(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateList(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateList(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateList(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateMainWindow(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateMainWindow(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateMainWindow(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateMenuBar(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateMenuBar(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateMenuBar(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateMessageDialog(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateMessageDialog(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateMessageDialog(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreatePopupMenu(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreatePopupMenu(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreatePopupMenu(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreatePulldownMenu(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreatePulldownMenu(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreatePulldownMenu(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreatePushButton(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreatePushButton(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreatePushButton(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreatePushButtonGadget(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreatePushButtonGadget(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreatePushButtonGadget(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateQuestionDialog(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateQuestionDialog(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateQuestionDialog(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateScale(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateScale(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateScale(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateScrollBar(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateScrollBar(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateScrollBar(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateScrolledList(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateScrolledList(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateScrolledList(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateScrolledText(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateScrolledText(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateScrolledText(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateSeparator(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateSeparator(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateSeparator(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateSeparatorGadget(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateSeparatorGadget(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateSeparatorGadget(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateSimpleSpinBox(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateSimpleSpinBox(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateSimpleSpinBox(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateTextField(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateTextField(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateTextField(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateToggleButton(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateToggleButton(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateToggleButton(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateToggleButtonGadget(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateToggleButtonGadget(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateToggleButtonGadget(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateWarningDialog(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateWarningDialog(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateWarningDialog(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param parent cast=(Widget)
 * @param name cast=(String)
 * @param arglist cast=(ArgList)
 */
public static final native int _XmCreateWorkingDialog(int parent, byte[] name, int[] arglist, int argcount);
public static final int XmCreateWorkingDialog(int parent, byte[] name, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmCreateWorkingDialog(parent, name, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param screen cast=(Screen *)
 * @param pixmap cast=(Pixmap)
 */
public static final native boolean _XmDestroyPixmap(int screen, int pixmap);
public static final boolean XmDestroyPixmap(int screen, int pixmap) {
	lock.lock();
	try {
		return _XmDestroyPixmap(screen, pixmap);
	} finally {
		lock.unlock();
	}
}
/** @param dragcontext cast=(Widget) */
public static final native void _XmDragCancel(int dragcontext);
public static final void XmDragCancel(int dragcontext) {
	lock.lock();
	try {
		_XmDragCancel(dragcontext);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param event cast=(XEvent *)
 * @param arglist cast=(ArgList)
 * @param argcount cast=(Cardinal)
 */
public static final native int _XmDragStart(int widget, int event, int[] arglist, int argcount);
public static final int XmDragStart(int widget, int event, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmDragStart(widget, event, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param arglist cast=(ArgList)
 * @param argcount cast=(Cardinal)
 */
public static final native void _XmDropSiteRegister(int widget, int[] arglist, int argcount);
public static final void XmDropSiteRegister(int widget, int[] arglist, int argcount) {
	lock.lock();
	try {
		_XmDropSiteRegister(widget, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmDropSiteUnregister(int widget);
public static final void XmDropSiteUnregister(int widget) {
	lock.lock();
	try {
		_XmDropSiteUnregister(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param arglist cast=(ArgList)
 * @param argcount cast=(Cardinal)
 */
public static final native void _XmDropSiteUpdate(int widget, int[] arglist, int argcount);
public static final void XmDropSiteUpdate(int widget, int[] arglist, int argcount) {
	lock.lock();
	try {
		_XmDropSiteUpdate(widget, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/**
 * @param drop_transfer cast=(Widget)
 * @param transfers cast=(XmDropTransferEntryRec *)
 * @param num_transfers cast=(Cardinal)
 */
public static final native void _XmDropTransferAdd(int drop_transfer, int[] transfers, int num_transfers);
public static final void XmDropTransferAdd(int drop_transfer, int[] transfers, int num_transfers) {
	lock.lock();
	try {
		_XmDropTransferAdd(drop_transfer, transfers, num_transfers);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param arglist cast=(ArgList)
 * @param argcount cast=(Cardinal)
 */
public static final native int _XmDropTransferStart(int widget, int[] arglist, int argcount);
public static final int XmDropTransferStart(int widget, int[] arglist, int argcount) {
	lock.lock();
	try {
		return _XmDropTransferStart(widget, arglist, argcount);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native int _XmFileSelectionBoxGetChild(int widget, int child);
public static final int XmFileSelectionBoxGetChild(int widget, int child) {
	lock.lock();
	try {
		return _XmFileSelectionBoxGetChild(widget, child);
	} finally {
		lock.unlock();
	}
}
/**
 * @param oldList cast=(XmFontList)
 * @param entry cast=(XmFontListEntry)
 */
public static final native int _XmFontListAppendEntry(int oldList, int entry);
public static final int XmFontListAppendEntry(int oldList, int entry) {
	lock.lock();
	try {
		return _XmFontListAppendEntry(oldList, entry);
	} finally {
		lock.unlock();
	}
}
/** @param fontlist cast=(XmFontList) */
public static final native int _XmFontListCopy(int fontlist);
public static final int XmFontListCopy(int fontlist) {
	lock.lock();
	try {
		return _XmFontListCopy(fontlist);
	} finally {
		lock.unlock();
	}
}
/** @param entry cast=(XmFontListEntry *) */
public static final native void _XmFontListEntryFree(int[] entry);
public static final void XmFontListEntryFree(int[] entry) {
	lock.lock();
	try {
		_XmFontListEntryFree(entry);
	} finally {
		lock.unlock();
	}
}
/**
 * @param entry cast=(XmFontListEntry)
 * @param type_return cast=(XmFontType *)
 */
public static final native int _XmFontListEntryGetFont(int entry, int[] type_return);
public static final int XmFontListEntryGetFont(int entry, int[] type_return) {
	lock.lock();
	try {
		return _XmFontListEntryGetFont(entry, type_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param fontName cast=(char *)
 * @param tag cast=(char *)
 */
public static final native int _XmFontListEntryLoad(int display, byte[] fontName, int type, byte[] tag);
public static final int XmFontListEntryLoad(int display, byte[] fontName, int type, byte[] tag) {
	lock.lock();
	try {
		return _XmFontListEntryLoad(display, fontName, type, tag);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(XmFontList) */
public static final native void _XmFontListFree(int list);
public static final void XmFontListFree(int list) {
	lock.lock();
	try {
		_XmFontListFree(list);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(XmFontContext) */
public static final native void _XmFontListFreeFontContext(int context);
public static final void XmFontListFreeFontContext(int context) {
	lock.lock();
	try {
		_XmFontListFreeFontContext(context);
	} finally {
		lock.unlock();
	}
}
/**
 * @param context cast=(XmFontContext *)
 * @param fontList cast=(XmFontList)
 */
public static final native boolean _XmFontListInitFontContext(int[] context, int fontList);
public static final boolean XmFontListInitFontContext(int[] context, int fontList) {
	lock.lock();
	try {
		return _XmFontListInitFontContext(context, fontList);
	} finally {
		lock.unlock();
	}
}
/** @param context cast=(XmFontContext) */
public static final native int _XmFontListNextEntry(int context);
public static final int XmFontListNextEntry(int context) {
	lock.lock();
	try {
		return _XmFontListNextEntry(context);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param atom cast=(Atom)
 */
public static final native int _XmGetAtomName(int display, int atom);
public static final int XmGetAtomName(int display, int atom) {
	lock.lock();
	try {
		return _XmGetAtomName(display, atom);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param timestamp cast=(Time)
 */
public static final native int _XmGetDragContext(int widget, int timestamp);
public static final int XmGetDragContext(int widget, int timestamp) {
	lock.lock();
	try {
		return _XmGetDragContext(widget, timestamp);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native int _XmGetFocusWidget(int widget);
public static final int XmGetFocusWidget(int widget) {
	lock.lock();
	try {
		return _XmGetFocusWidget(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param screen cast=(Screen *)
 * @param name cast=(char *)
 * @param fgPixel cast=(Pixel)
 * @param bgPixel cast=(Pixel)
 */
public static final native int _XmGetPixmap(int screen, byte[] name, int fgPixel, int bgPixel);
public static final int XmGetPixmap(int screen, byte[] name, int fgPixel, int bgPixel) {
	lock.lock();
	try {
		return _XmGetPixmap(screen, name, fgPixel, bgPixel);
	} finally {
		lock.unlock();
	}
}
/**
 * @param screen cast=(Screen *)
 * @param image_name cast=(char *)
 */
public static final native int _XmGetPixmapByDepth(int screen, byte[] image_name, int foreground, int background, int depth);
public static final int XmGetPixmapByDepth(int screen, byte[] image_name, int foreground, int background, int depth) {
	lock.lock();
	try {
		return _XmGetPixmapByDepth(screen, image_name, foreground, background, depth);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XmGetXmDisplay(int display);
public static final int XmGetXmDisplay(int display) {
	lock.lock();
	try {
		return _XmGetXmDisplay(display);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param event cast=(XKeyPressedEvent *)
 * @param string cast=(char *)
 * @param keysym cast=(KeySym *)
 * @param status cast=(int *)
 */
public static final native int _XmImMbLookupString(int widget, XKeyEvent event, byte[] string, int size, int[] keysym, int[] status);
public static final int XmImMbLookupString(int widget, XKeyEvent event, byte[] string, int size, int[] keysym, int[] status) {
	lock.lock();
	try {
		return _XmImMbLookupString(widget, event, string, size, keysym, status);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmImRegister(int widget, int reserved);
public static final void XmImRegister(int widget, int reserved) {
	lock.lock();
	try {
		_XmImRegister(widget, reserved);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param args cast=(ArgList)
 */
public static final native void _XmImSetFocusValues(int widget, int[] args, int num_args);
public static final void XmImSetFocusValues(int widget, int[] args, int num_args) {
	lock.lock();
	try {
		_XmImSetFocusValues(widget, args, num_args);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param args cast=(ArgList)
 */
public static final native void _XmImSetValues(int widget, int[] args, int num_args);
public static final void XmImSetValues(int widget, int[] args, int num_args) {
	lock.lock();
	try {
		_XmImSetValues(widget, args, num_args);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmImUnregister(int widget);
public static final void XmImUnregister(int widget) {
	lock.lock();
	try {
		_XmImUnregister(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmImUnsetFocus(int widget);
public static final void XmImUnsetFocus(int widget) {
	lock.lock();
	try {
		_XmImUnsetFocus(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param name cast=(String)
 */
public static final native int _XmInternAtom(int display, byte[] name, boolean only_if_exists);
public static final int XmInternAtom(int display, byte[] name, boolean only_if_exists) {
	lock.lock();
	try {
		return _XmInternAtom(display, name, only_if_exists);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(Widget)
 * @param xmString cast=(XmString)
 */
public static final native void _XmListAddItemUnselected(int list, int xmString, int position);
public static final void XmListAddItemUnselected(int list, int xmString, int position) {
	lock.lock();
	try {
		_XmListAddItemUnselected(list, xmString, position);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(Widget) */
public static final native void _XmListDeleteAllItems(int list);
public static final void XmListDeleteAllItems(int list) {
	lock.lock();
	try {
		_XmListDeleteAllItems(list);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(Widget) */
public static final native void _XmListDeleteItemsPos(int list, int item_count, int position);
public static final void XmListDeleteItemsPos(int list, int item_count, int position) {
	lock.lock();
	try {
		_XmListDeleteItemsPos(list, item_count, position);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(Widget) */
public static final native void _XmListDeletePos(int list, int position);
public static final void XmListDeletePos(int list, int position) {
	lock.lock();
	try {
		_XmListDeletePos(list, position);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(Widget)
 * @param position_list cast=(int *)
 */
public static final native void _XmListDeletePositions(int list, int[] position_list, int position_count);
public static final void XmListDeletePositions(int list, int[] position_list, int position_count) {
	lock.lock();
	try {
		_XmListDeletePositions(list, position_list, position_count);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(Widget) */
public static final native void _XmListDeselectAllItems(int list);
public static final void XmListDeselectAllItems(int list) {
	lock.lock();
	try {
		_XmListDeselectAllItems(list);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(Widget) */
public static final native void _XmListDeselectPos(int list, int position);
public static final void XmListDeselectPos(int list, int position) {
	lock.lock();
	try {
		_XmListDeselectPos(list, position);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(Widget) */
public static final native int _XmListGetKbdItemPos(int list);
public static final int XmListGetKbdItemPos(int list) {
	lock.lock();
	try {
		return _XmListGetKbdItemPos(list);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(Widget)
 * @param positions cast=(int **)
 * @param count cast=(int *)
 */
public static final native boolean _XmListGetSelectedPos(int list, int[] positions, int[] count);
public static final boolean XmListGetSelectedPos(int list, int[] positions, int[] count) {
	lock.lock();
	try {
		return _XmListGetSelectedPos(list, positions, count);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(Widget)
 * @param xmString cast=(XmString)
 */
public static final native int _XmListItemPos(int list, int xmString);
public static final int XmListItemPos(int list, int xmString) {
	lock.lock();
	try {
		return _XmListItemPos(list, xmString);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(Widget) */
public static final native boolean _XmListPosSelected(int list, int position);
public static final boolean XmListPosSelected(int list, int position) {
	lock.lock();
	try {
		return _XmListPosSelected(list, position);
	} finally {
		lock.unlock();
	}
}
/**
 * @param list cast=(Widget)
 * @param new_items cast=(XmString *)
 */
public static final native void _XmListReplaceItemsPosUnselected(int list, int[] new_items, int item_count, int position);
public static final void XmListReplaceItemsPosUnselected(int list, int[] new_items, int item_count, int position) {
	lock.lock();
	try {
		_XmListReplaceItemsPosUnselected(list, new_items, item_count, position);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(Widget) */
public static final native void _XmListSelectPos(int list, int position, boolean notify);
public static final void XmListSelectPos(int list, int position, boolean notify) {
	lock.lock();
	try {
		_XmListSelectPos(list, position, notify);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(Widget) */
public static final native boolean _XmListSetKbdItemPos(int list, int position);
public static final boolean XmListSetKbdItemPos(int list, int position) {
	lock.lock();
	try {
		return _XmListSetKbdItemPos(list, position);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(Widget) */
public static final native void _XmListSetPos(int list, int position);
public static final void XmListSetPos(int list, int position) {
	lock.lock();
	try {
		_XmListSetPos(list, position);
	} finally {
		lock.unlock();
	}
}
/** @param list cast=(Widget) */
public static final native void _XmListUpdateSelectedList(int list);
public static final void XmListUpdateSelectedList(int list) {
	lock.lock();
	try {
		_XmListUpdateSelectedList(list);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param menu cast=(Widget)
 * @param command cast=(Widget)
 * @param hscroll cast=(Widget)
 * @param vscroll cast=(Widget)
 * @param wregion cast=(Widget)
 */
public static final native void _XmMainWindowSetAreas(int widget, int menu, int command, int hscroll, int vscroll, int wregion);
public static final void XmMainWindowSetAreas(int widget, int menu, int command, int hscroll, int vscroll, int wregion) {
	lock.lock();
	try {
		_XmMainWindowSetAreas(widget, menu, command, hscroll, vscroll, wregion);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native int _XmMessageBoxGetChild(int widget, int child);
public static final int XmMessageBoxGetChild(int widget, int child) {
	lock.lock();
	try {
		return _XmMessageBoxGetChild(widget, child);
	} finally {
		lock.unlock();
	}
}
/** @param argList cast=(ArgList) */
public static final native int _XmParseMappingCreate(int[] argList, int argCount);
public static final int XmParseMappingCreate(int[] argList, int argCount) {
	lock.lock();
	try {
		return _XmParseMappingCreate(argList, argCount);
	} finally {
		lock.unlock();
	}
}
/** @param parseMapping cast=(XmParseMapping) */
public static final native void _XmParseMappingFree(int parseMapping);
public static final void XmParseMappingFree(int parseMapping) {
	lock.lock();
	try {
		_XmParseMappingFree(parseMapping);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native boolean _XmProcessTraversal(int widget, int dir);
public static final boolean XmProcessTraversal(int widget, int dir) {
	lock.lock();
	try {
		return _XmProcessTraversal(widget, dir);
	} finally {
		lock.unlock();
	}
}
/**
 * @param oldTable cast=(XmRenderTable)
 * @param renditions cast=(XmRendition *)
 */
public static final native int _XmRenderTableAddRenditions(int oldTable, int[] renditions, int renditionCount, int mergeMode);
public static final int XmRenderTableAddRenditions(int oldTable, int[] renditions, int renditionCount, int mergeMode) {
	lock.lock();
	try {
		return _XmRenderTableAddRenditions(oldTable, renditions, renditionCount, mergeMode);
	} finally {
		lock.unlock();
	}
}
/** @param renderTable cast=(XmRenderTable) */
public static final native void _XmRenderTableFree(int renderTable);
public static final void XmRenderTableFree(int renderTable) {
	lock.lock();
	try {
		_XmRenderTableFree(renderTable);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param tag cast=(XmStringTag)
 * @param argList cast=(ArgList)
 */
public static final native int _XmRenditionCreate(int widget, byte[] tag, int[] argList, int argCount);
public static final int XmRenditionCreate(int widget, byte[] tag, int[] argList, int argCount) {
	lock.lock();
	try {
		return _XmRenditionCreate(widget, tag, argList, argCount);
	} finally {
		lock.unlock();
	}
}
/** @param rendition cast=(XmRendition) */
public static final native void _XmRenditionFree(int rendition);
public static final void XmRenditionFree(int rendition) {
	lock.lock();
	try {
		_XmRenditionFree(rendition);
	} finally {
		lock.unlock();
	}
}
/**
 * @param fontList cast=(XmRenderTable)
 * @param xmString cast=(XmString)
 */
public static final native int _XmStringBaseline(int fontList, int xmString);
public static final int XmStringBaseline(int fontList, int xmString) {
	lock.lock();
	try {
		return _XmStringBaseline(fontList, xmString);
	} finally {
		lock.unlock();
	}
}
/**
 * @param xmString1 cast=(XmString)
 * @param xmString2 cast=(XmString)
 */
public static final native boolean _XmStringCompare(int xmString1, int xmString2);
public static final boolean XmStringCompare(int xmString1, int xmString2) {
	lock.lock();
	try {
		return _XmStringCompare(xmString1, xmString2);
	} finally {
		lock.unlock();
	}
}
/** @param value cast=(XtPointer) */
public static final native int _XmStringComponentCreate(int type, int length, byte[] value);
public static final int XmStringComponentCreate(int type, int length, byte[] value) {
	lock.lock();
	try {
		return _XmStringComponentCreate(type, length, value);
	} finally {
		lock.unlock();
	}
}
/**
 * @param xmString1 cast=(XmString)
 * @param xmString2 cast=(XmString)
 */
public static final native int _XmStringConcat(int xmString1, int xmString2);
public static final int XmStringConcat(int xmString1, int xmString2) {
	lock.lock();
	try {
		return _XmStringConcat(xmString1, xmString2);
	} finally {
		lock.unlock();
	}
}
/**
 * @param string cast=(char *)
 * @param charset cast=(char *)
 */
public static final native int _XmStringCreate(byte[] string, byte[] charset);
public static final int XmStringCreate(byte[] string, byte[] charset) {
	lock.lock();
	try {
		return _XmStringCreate(string, charset);
	} finally {
		lock.unlock();
	}
}
/** @param string cast=(char *) */
public static final native int _XmStringCreateLocalized(byte[] string);
public static final int XmStringCreateLocalized(byte[] string) {
	lock.lock();
	try {
		return _XmStringCreateLocalized(string);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param renderTable cast=(XmFontList)
 * @param xmString cast=(XmString)
 * @param gc cast=(GC)
 */
public static final native void _XmStringDraw(int display, int window, int renderTable, int xmString, int gc, int x, int y, int width, int align, int lay_dir, XRectangle clip);
public static final void XmStringDraw(int display, int window, int renderTable, int xmString, int gc, int x, int y, int width, int align, int lay_dir, XRectangle clip) {
	lock.lock();
	try {
		_XmStringDraw(display, window, renderTable, xmString, gc, x, y, width, align, lay_dir, clip);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param renderTable cast=(XmFontList)
 * @param xmString cast=(XmString)
 * @param gc cast=(GC)
 */
public static final native void _XmStringDrawImage(int display, int window, int renderTable, int xmString, int gc, int x, int y, int width, int align, int lay_dir, XRectangle clip);
public static final void XmStringDrawImage(int display, int window, int renderTable, int xmString, int gc, int x, int y, int width, int align, int lay_dir, XRectangle clip) {
	lock.lock();
	try {
		_XmStringDrawImage(display, window, renderTable, xmString, gc, x, y, width, align, lay_dir, clip);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 * @param fontlist cast=(XmFontList)
 * @param xmString cast=(XmString)
 * @param gc cast=(GC)
 * @param xmStringUnderline cast=(XmString)
 */
public static final native void _XmStringDrawUnderline(int display, int window, int fontlist, int xmString, int gc, int x, int y, int width, int align, int lay_dir, XRectangle clip, int xmStringUnderline);
public static final void XmStringDrawUnderline(int display, int window, int fontlist, int xmString, int gc, int x, int y, int width, int align, int lay_dir, XRectangle clip, int xmStringUnderline) {
	lock.lock();
	try {
		_XmStringDrawUnderline(display, window, fontlist, xmString, gc, x, y, width, align, lay_dir, clip, xmStringUnderline);
	} finally {
		lock.unlock();
	}
}
/** @param s1 cast=(XmString) */
public static final native boolean _XmStringEmpty(int s1);
public static final boolean XmStringEmpty(int s1) {
	lock.lock();
	try {
		return _XmStringEmpty(s1);
	} finally {
		lock.unlock();
	}
}
/**
 * @param fontList cast=(XmRenderTable)
 * @param xmString cast=(XmString)
 * @param width cast=(Dimension *)
 * @param height cast=(Dimension *)
 */
public static final native void _XmStringExtent(int fontList, int xmString, short[] width, short[] height);
public static final void XmStringExtent(int fontList, int xmString, short[] width, short[] height) {
	lock.lock();
	try {
		_XmStringExtent(fontList, xmString, width, height);
	} finally {
		lock.unlock();
	}
}
/** @param xmString cast=(XmString) */
public static final native void _XmStringFree(int xmString);
public static final void XmStringFree(int xmString) {
	lock.lock();
	try {
		_XmStringFree(xmString);
	} finally {
		lock.unlock();
	}
}
/**
 * @param text cast=(XtPointer)
 * @param tag cast=(XmStringTag)
 * @param rendition cast=(XmStringTag)
 */
public static final native int _XmStringGenerate(byte[] text, byte[] tag, int type, byte[] rendition);
public static final int XmStringGenerate(byte[] text, byte[] tag, int type, byte[] rendition) {
	lock.lock();
	try {
		return _XmStringGenerate(text, tag, type, rendition);
	} finally {
		lock.unlock();
	}
}
/**
 * @param fontList cast=(XmFontList)
 * @param xmString cast=(XmString)
 */
public static final native int _XmStringHeight(int fontList, int xmString);
public static final int XmStringHeight(int fontList, int xmString) {
	lock.lock();
	try {
		return _XmStringHeight(fontList, xmString);
	} finally {
		lock.unlock();
	}
}
/**
 * @param text cast=(XtPointer)
 * @param textEnd cast=(XtPointer *)
 * @param tag cast=(XmStringTag)
 * @param parseTable cast=(XmParseTable)
 * @param callData cast=(XtPointer)
 */
public static final native int _XmStringParseText(byte[] text, int textEnd, byte[] tag, int tagType, int[] parseTable, int parseCount, int callData);
public static final int XmStringParseText(byte[] text, int textEnd, byte[] tag, int tagType, int[] parseTable, int parseCount, int callData) {
	lock.lock();
	try {
		return _XmStringParseText(text, textEnd, tag, tagType, parseTable, parseCount, callData);
	} finally {
		lock.unlock();
	}
}
/**
 * @param xmString cast=(XmString)
 * @param tag cast=(XmStringTag)
 * @param parseTable cast=(XmParseTable)
 */
public static final native int _XmStringUnparse(int xmString, byte[] tag, int tagType, int outputType, int[] parseTable, int parseCount, int parseModel);
public static final int XmStringUnparse(int xmString, byte[] tag, int tagType, int outputType, int[] parseTable, int parseCount, int parseModel) {
	lock.lock();
	try {
		return _XmStringUnparse(xmString, tag, tagType, outputType, parseTable, parseCount, parseModel);
	} finally {
		lock.unlock();
	}
}
/**
 * @param fontList cast=(XmFontList)
 * @param xmString cast=(XmString)
 */
public static final native int _XmStringWidth(int fontList, int xmString);
public static final int XmStringWidth(int fontList, int xmString) {
	lock.lock();
	try {
		return _XmStringWidth(fontList, xmString);
	} finally {
		lock.unlock();
	}
}
/** @param decimal cast=(char *) */
public static final native int _XmTabCreate(int value, byte units, byte offsetModel, byte alignment, byte[] decimal);
public static final int XmTabCreate(int value, byte units, byte offsetModel, byte alignment, byte[] decimal) {
	lock.lock();
	try {
		return _XmTabCreate(value, units, offsetModel, alignment, decimal);
	} finally {
		lock.unlock();
	}
}
/** @param tab cast=(XmTab) */
public static final native void _XmTabFree(int tab);
public static final void XmTabFree(int tab) {
	lock.lock();
	try {
		_XmTabFree(tab);
	} finally {
		lock.unlock();
	}
}
/** @param tabList cast=(XmTabList) */
public static final native void _XmTabListFree(int tabList);
public static final void XmTabListFree(int tabList) {
	lock.lock();
	try {
		_XmTabListFree(tabList);
	} finally {
		lock.unlock();
	}
}
/**
 * @param oldList cast=(XmTabList)
 * @param tabs cast=(XmTab *)
 */
public static final native int _XmTabListInsertTabs(int oldList, int[] tabs, int tab_count, int position);
public static final int XmTabListInsertTabs(int oldList, int[] tabs, int tab_count, int position) {
	lock.lock();
	try {
		return _XmTabListInsertTabs(oldList, tabs, tab_count, position);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmTextClearSelection(int widget, int time);
public static final void XmTextClearSelection(int widget, int time) {
	lock.lock();
	try {
		_XmTextClearSelection(widget, time);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native boolean _XmTextCopy(int widget, int time);
public static final boolean XmTextCopy(int widget, int time) {
	lock.lock();
	try {
		return _XmTextCopy(widget, time);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native boolean _XmTextCut(int widget, int time);
public static final boolean XmTextCut(int widget, int time) {
	lock.lock();
	try {
		return _XmTextCut(widget, time);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmTextDisableRedisplay(int widget);
public static final void XmTextDisableRedisplay(int widget) {
	lock.lock();
	try {
		_XmTextDisableRedisplay(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmTextEnableRedisplay(int widget);
public static final void XmTextEnableRedisplay(int widget) {
	lock.lock();
	try {
		_XmTextEnableRedisplay(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native boolean _XmTextFieldPaste(int widget);
public static final boolean XmTextFieldPaste(int widget) {
	lock.lock();
	try {
		return _XmTextFieldPaste(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native int _XmTextGetInsertionPosition(int widget);
public static final int XmTextGetInsertionPosition(int widget) {
	lock.lock();
	try {
		return _XmTextGetInsertionPosition(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native int _XmTextGetLastPosition(int widget);
public static final int XmTextGetLastPosition(int widget) {
	lock.lock();
	try {
		return _XmTextGetLastPosition(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native int _XmTextGetMaxLength(int widget);
public static final int XmTextGetMaxLength(int widget) {
	lock.lock();
	try {
		return _XmTextGetMaxLength(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native int _XmTextGetSelection(int widget);
public static final int XmTextGetSelection(int widget) {
	lock.lock();
	try {
		return _XmTextGetSelection(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param left cast=(XmTextPosition *)
 * @param right cast=(XmTextPosition *)
 */
public static final native boolean _XmTextGetSelectionPosition(int widget, int[] left, int[] right);
public static final boolean XmTextGetSelectionPosition(int widget, int[] left, int[] right) {
	lock.lock();
	try {
		return _XmTextGetSelectionPosition(widget, left, right);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native int _XmTextGetString(int widget);
public static final int XmTextGetString(int widget) {
	lock.lock();
	try {
		return _XmTextGetString(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param buffer cast=(char *)
 */
public static final native int _XmTextGetSubstring(int widget, int start, int num_chars, int buffer_size, byte[] buffer);
public static final int XmTextGetSubstring(int widget, int start, int num_chars, int buffer_size, byte[] buffer) {
	lock.lock();
	try {
		return _XmTextGetSubstring(widget, start, num_chars, buffer_size, buffer);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param start cast=(XmTextPosition)
 * @param buffer cast=(wchar_t *)
 */
public static final native int _XmTextGetSubstringWcs(int widget, int start, int num_chars, int buffer_size, char[] buffer);
public static final int XmTextGetSubstringWcs(int widget, int start, int num_chars, int buffer_size, char[] buffer) {
	lock.lock();
	try {
		return _XmTextGetSubstringWcs(widget, start, num_chars, buffer_size, buffer);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param value cast=(char *)
 */
public static final native void _XmTextInsert(int widget, int position, byte[] value);
public static final void XmTextInsert(int widget, int position, byte[] value) {
	lock.lock();
	try {
		_XmTextInsert(widget, position, value);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native boolean _XmTextPaste(int widget);
public static final boolean XmTextPaste(int widget) {
	lock.lock();
	try {
		return _XmTextPaste(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param position cast=(XmTextPosition)
 * @param x cast=(Position *)
 * @param y cast=(Position *)
 */
public static final native boolean _XmTextPosToXY(int widget, int position, short[] x, short[] y);
public static final boolean XmTextPosToXY(int widget, int position, short[] x, short[] y) {
	lock.lock();
	try {
		return _XmTextPosToXY(widget, position, x, y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param value cast=(char *)
 */
public static final native void _XmTextReplace(int widget, int from_pos, int to_pos, byte[] value);
public static final void XmTextReplace(int widget, int from_pos, int to_pos, byte[] value) {
	lock.lock();
	try {
		_XmTextReplace(widget, from_pos, to_pos, value);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmTextScroll(int widget, int lines);
public static final void XmTextScroll(int widget, int lines) {
	lock.lock();
	try {
		_XmTextScroll(widget, lines);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmTextSetEditable(int widget, boolean editable);
public static final void XmTextSetEditable(int widget, boolean editable) {
	lock.lock();
	try {
		_XmTextSetEditable(widget, editable);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmTextSetHighlight(int widget, int left, int right, int mode);
public static final void XmTextSetHighlight(int widget, int left, int right, int mode) {
	lock.lock();
	try {
		_XmTextSetHighlight(widget, left, right, mode);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmTextSetInsertionPosition(int widget, int position);
public static final void XmTextSetInsertionPosition(int widget, int position) {
	lock.lock();
	try {
		_XmTextSetInsertionPosition(widget, position);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmTextSetMaxLength(int widget, int max_length);
public static final void XmTextSetMaxLength(int widget, int max_length) {
	lock.lock();
	try {
		_XmTextSetMaxLength(widget, max_length);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmTextSetSelection(int widget, int first, int last, int time);
public static final void XmTextSetSelection(int widget, int first, int last, int time) {
	lock.lock();
	try {
		_XmTextSetSelection(widget, first, last, time);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param value cast=(char *)
 */
public static final native void _XmTextSetString(int widget, byte[] value);
public static final void XmTextSetString(int widget, byte[] value) {
	lock.lock();
	try {
		_XmTextSetString(widget, value);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmTextShowPosition(int widget, int position);
public static final void XmTextShowPosition(int widget, int position) {
	lock.lock();
	try {
		_XmTextShowPosition(widget, position);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param x cast=(Position)
 * @param y cast=(Position)
 */
public static final native int _XmTextXYToPos(int widget, short x, short y);
public static final int XmTextXYToPos(int widget, short x, short y) {
	lock.lock();
	try {
		return _XmTextXYToPos(widget, x, y);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XmUpdateDisplay(int widget);
public static final void XmUpdateDisplay(int widget) {
	lock.lock();
	try {
		_XmUpdateDisplay(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param region cast=(Widget)
 * @param rectangle cast=(XRectangle *)
 */
public static final native boolean _XmWidgetGetDisplayRect(int region, XRectangle rectangle);
public static final boolean XmWidgetGetDisplayRect(int region, XRectangle rectangle) {
	lock.lock();
	try {
		return _XmWidgetGetDisplayRect(region, rectangle);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param list cast=(char **)
 * @param style cast=(XICCEncodingStyle)
 */
public static final native int _XmbTextListToTextProperty(int display, int list, int count, int style, XTextProperty text_prop_return);
public static final int XmbTextListToTextProperty(int display, int list, int count, int style, XTextProperty text_prop_return) {
	lock.lock();
	try {
		return _XmbTextListToTextProperty(display, list, count, style, text_prop_return);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param list_return cast=(char ***)
 * @param count_return cast=(int *)
 */
public static final native int _XmbTextPropertyToTextList(int display, XTextProperty text_prop, int[] list_return, int[] count_return);
public static final int XmbTextPropertyToTextList(int display, XTextProperty text_prop, int[] list_return, int[] count_return) {
	lock.lock();
	try {
		return _XmbTextPropertyToTextList(display, text_prop, list_return, count_return);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native void _XpCancelJob(int display, boolean discard);
public static final void XpCancelJob(int display, boolean discard) {
	lock.lock();
	try {
		_XpCancelJob(display, discard);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param printer_name cast=(char *)
 */
public static final native int _XpCreateContext(int display, byte[] printer_name);
public static final int XpCreateContext(int display, byte[] printer_name) {
	lock.lock();
	try {
		return _XpCreateContext(display, printer_name);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param print_context cast=(XPContext)
 */
public static final native void _XpDestroyContext(int display, int print_context);
public static final void XpDestroyContext(int display, int print_context) {
	lock.lock();
	try {
		_XpDestroyContext(display, print_context);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native void _XpEndJob(int display);
public static final void XpEndJob(int display) {
	lock.lock();
	try {
		_XpEndJob(display);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native void _XpEndPage(int display);
public static final void XpEndPage(int display) {
	lock.lock();
	try {
		_XpEndPage(display);
	} finally {
		lock.unlock();
	}
}
/** @param printer_list cast=(XPPrinterList) */
public static final native void _XpFreePrinterList(int printer_list);
public static final void XpFreePrinterList(int printer_list) {
	lock.lock();
	try {
		_XpFreePrinterList(printer_list);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param print_context cast=(XPContext)
 * @param type cast=(XPAttributes)
 * @param attribute_name cast=(char *)
 */
public static final native int _XpGetOneAttribute(int display, int print_context, byte type, byte[] attribute_name);
public static final int XpGetOneAttribute(int display, int print_context, byte type, byte[] attribute_name) {
	lock.lock();
	try {
		return _XpGetOneAttribute(display, print_context, type, attribute_name);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param print_context cast=(XPContext)
 * @param width cast=(unsigned short *)
 * @param height cast=(unsigned short *)
 * @param reproducible_area cast=(XRectangle *)
 */
public static final native int _XpGetPageDimensions(int display, int print_context, short[] width, short[] height, XRectangle reproducible_area);
public static final int XpGetPageDimensions(int display, int print_context, short[] width, short[] height, XRectangle reproducible_area) {
	lock.lock();
	try {
		return _XpGetPageDimensions(display, print_context, width, height, reproducible_area);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param printer_name cast=(char *)
 * @param list_count cast=(int *)
 */
public static final native int _XpGetPrinterList(int display, byte[] printer_name, int[] list_count);
public static final int XpGetPrinterList(int display, byte[] printer_name, int[] list_count) {
	lock.lock();
	try {
		return _XpGetPrinterList(display, printer_name, list_count);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param print_context cast=(XPContext)
 */
public static final native int _XpGetScreenOfContext(int display, int print_context);
public static final int XpGetScreenOfContext(int display, int print_context) {
	lock.lock();
	try {
		return _XpGetScreenOfContext(display, print_context);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param print_context cast=(XPContext)
 * @param type cast=(XPAttributes)
 * @param pool cast=(char *)
 * @param replacement_rule cast=(XPAttrReplacement)
 */
public static final native void _XpSetAttributes(int display, int print_context, byte type, byte[] pool, byte replacement_rule);
public static final void XpSetAttributes(int display, int print_context, byte type, byte[] pool, byte replacement_rule) {
	lock.lock();
	try {
		_XpSetAttributes(display, print_context, type, pool, replacement_rule);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param print_context cast=(XPContext)
 */
public static final native void _XpSetContext(int display, int print_context);
public static final void XpSetContext(int display, int print_context) {
	lock.lock();
	try {
		_XpSetContext(display, print_context);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param save_data cast=(XPSaveData)
 */
public static final native void _XpStartJob(int display, byte save_data);
public static final void XpStartJob(int display, byte save_data) {
	lock.lock();
	try {
		_XpStartJob(display, save_data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param window cast=(Window)
 */
public static final native void _XpStartPage(int display, int window);
public static final void XpStartPage(int display, int window) {
	lock.lock();
	try {
		_XpStartPage(display, window);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param callback_name cast=(String)
 * @param callback cast=(XtCallbackProc)
 * @param client_data cast=(XtPointer)
 */
public static final native void _XtAddCallback(int widget, int callback_name, int callback, int client_data);
public static final void XtAddCallback(int widget, int callback_name, int callback, int client_data) {
	lock.lock();
	try {
		_XtAddCallback(widget, callback_name, callback, client_data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param proc cast=(XtEventHandler)
 * @param client_data cast=(XtPointer)
 */
public static final native void _XtAddEventHandler(int widget, int event_mask, boolean nonmaskable, int proc, int client_data);
public static final void XtAddEventHandler(int widget, int event_mask, boolean nonmaskable, int proc, int client_data) {
	lock.lock();
	try {
		_XtAddEventHandler(widget, event_mask, nonmaskable, proc, client_data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param event cast=(XEvent *)
 * @param region cast=(Region)
 */
public static final native void _XtAddExposureToRegion(int event, int region);
public static final void XtAddExposureToRegion(int event, int region) {
	lock.lock();
	try {
		_XtAddExposureToRegion(event, region);
	} finally {
		lock.unlock();
	}
}
/**
 * @param app_context cast=(XtAppContext)
 * @param condition cast=(XtPointer)
 * @param proc cast=(XtInputCallbackProc)
 * @param client_data cast=(XtPointer)
 */
public static final native int _XtAppAddInput(int app_context, int source, int condition, int proc, int client_data);
public static final int XtAppAddInput(int app_context, int source, int condition, int proc, int client_data) {
	lock.lock();
	try {
		return _XtAppAddInput(app_context, source, condition, proc, client_data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param app_context cast=(XtAppContext)
 * @param proc cast=(XtTimerCallbackProc)
 * @param client_data cast=(XtPointer)
 */
public static final native int _XtAppAddTimeOut(int app_context, int interval, int proc, int client_data);
public static final int XtAppAddTimeOut(int app_context, int interval, int proc, int client_data) {
	lock.lock();
	try {
		return _XtAppAddTimeOut(app_context, interval, proc, client_data);
	} finally {
		lock.unlock();
	}
}
/**
 * @param appName cast=(String)
 * @param appClass cast=(String)
 * @param widgetClass cast=(WidgetClass)
 * @param display cast=(Display *)
 * @param argList cast=(ArgList)
 */
public static final native int _XtAppCreateShell(byte[] appName, byte[] appClass, int widgetClass, int display, int[] argList, int argCount);
public static final int XtAppCreateShell(byte[] appName, byte[] appClass, int widgetClass, int display, int[] argList, int argCount) {
	lock.lock();
	try {
		return _XtAppCreateShell(appName, appClass, widgetClass, display, argList, argCount);
	} finally {
		lock.unlock();
	}
}
/** @param appContext cast=(XtAppContext) */
public static final native int _XtAppGetSelectionTimeout(int appContext);
public static final int XtAppGetSelectionTimeout(int appContext) {
	lock.lock();
	try {
		return _XtAppGetSelectionTimeout(appContext);
	} finally {
		lock.unlock();
	}
}
/**
 * @param appContext cast=(XtAppContext)
 * @param event cast=(XEvent *)
 */
public static final native void _XtAppNextEvent(int appContext, int event);
public static final void XtAppNextEvent(int appContext, int event) {
	lock.lock();
	try {
		_XtAppNextEvent(appContext, event);
	} finally {
		lock.unlock();
	}
}
/**
 * @param appContext cast=(XtAppContext)
 * @param event cast=(XEvent *)
 */
public static final native boolean _XtAppPeekEvent(int appContext, int event);
public static final boolean XtAppPeekEvent(int appContext, int event) {
	lock.lock();
	try {
		return _XtAppPeekEvent(appContext, event);
	} finally {
		lock.unlock();
	}
}
/** @param appContext cast=(XtAppContext) */
public static final native int _XtAppPending(int appContext);
public static final int XtAppPending(int appContext) {
	lock.lock();
	try {
		return _XtAppPending(appContext);
	} finally {
		lock.unlock();
	}
}
/** @param appContext cast=(XtAppContext) */
public static final native void _XtAppProcessEvent(int appContext, int inputMask);
public static final void XtAppProcessEvent(int appContext, int inputMask) {
	lock.lock();
	try {
		_XtAppProcessEvent(appContext, inputMask);
	} finally {
		lock.unlock();
	}
}
/**
 * @param app_context cast=(XtAppContext)
 * @param handler cast=(XtErrorHandler)
 */
public static final native int _XtAppSetErrorHandler(int app_context, int handler);
public static final int XtAppSetErrorHandler(int app_context, int handler) {
	lock.lock();
	try {
		return _XtAppSetErrorHandler(app_context, handler);
	} finally {
		lock.unlock();
	}
}
/**
 * @param app_context cast=(XtAppContext)
 * @param specification_list cast=(String *)
 */
public static final native void _XtAppSetFallbackResources(int app_context, int specification_list);
public static final void XtAppSetFallbackResources(int app_context, int specification_list) {
	lock.lock();
	try {
		_XtAppSetFallbackResources(app_context, specification_list);
	} finally {
		lock.unlock();
	}
}
/** @param appContext cast=(XtAppContext) */
public static final native void _XtAppSetSelectionTimeout(int appContext, int timeout);
public static final void XtAppSetSelectionTimeout(int appContext, int timeout) {
	lock.lock();
	try {
		_XtAppSetSelectionTimeout(appContext, timeout);
	} finally {
		lock.unlock();
	}
}
/**
 * @param app_context cast=(XtAppContext)
 * @param handler cast=(XtErrorHandler)
 */
public static final native int _XtAppSetWarningHandler(int app_context, int handler);
public static final int XtAppSetWarningHandler(int app_context, int handler) {
	lock.lock();
	try {
		return _XtAppSetWarningHandler(app_context, handler);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native int _XtBuildEventMask(int widget);
public static final int XtBuildEventMask(int widget) {
	lock.lock();
	try {
		return _XtBuildEventMask(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param action cast=(String)
 * @param event cast=(XEvent *)
 * @param params cast=(String *)
 */
public static final native void _XtCallActionProc(int widget, byte[] action, int event, int[] params, int num_params);
public static final void XtCallActionProc(int widget, byte[] action, int event, int[] params, int num_params) {
	lock.lock();
	try {
		_XtCallActionProc(widget, action, event, params, num_params);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native int _XtClass(int widget);
public static final int XtClass(int widget) {
	lock.lock();
	try {
		return _XtClass(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtConfigureWidget(int widget, int x, int y, int width, int height, int borderWidth);
public static final void XtConfigureWidget(int widget, int x, int y, int width, int height, int borderWidth) {
	lock.lock();
	try {
		_XtConfigureWidget(widget, x, y, width, height, borderWidth);
	} finally {
		lock.unlock();
	}
}
public static final native int _XtCreateApplicationContext();
public static final int XtCreateApplicationContext() {
	lock.lock();
	try {
		return _XtCreateApplicationContext();
	} finally {
		lock.unlock();
	}
}
/**
 * @param name cast=(String)
 * @param widgetClass cast=(WidgetClass)
 * @param parent cast=(Widget)
 * @param argList cast=(ArgList)
 */
public static final native int _XtCreatePopupShell(byte[] name, int widgetClass, int parent, int[] argList, int argCount);
public static final int XtCreatePopupShell(byte[] name, int widgetClass, int parent, int[] argList, int argCount) {
	lock.lock();
	try {
		return _XtCreatePopupShell(name, widgetClass, parent, argList, argCount);
	} finally {
		lock.unlock();
	}
}
public static final native int __XtDefaultAppContext();
public static final int _XtDefaultAppContext() {
	lock.lock();
	try {
		return __XtDefaultAppContext();
	} finally {
		lock.unlock();
	}
}
/** @param appContext cast=(XtAppContext) */
public static final native void _XtDestroyApplicationContext(int appContext);
public static final void XtDestroyApplicationContext(int appContext) {
	lock.lock();
	try {
		_XtDestroyApplicationContext(appContext);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtDestroyWidget(int widget);
public static final void XtDestroyWidget(int widget) {
	lock.lock();
	try {
		_XtDestroyWidget(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param w cast=(Widget)
 * @param selection cast=(Atom)
 * @param time cast=(Time)
 */
public static final native void _XtDisownSelection(int w, int selection, int time);
public static final void XtDisownSelection(int w, int selection, int time) {
	lock.lock();
	try {
		_XtDisownSelection(w, selection, time);
	} finally {
		lock.unlock();
	}
}
/** @param event cast=(XEvent *) */
public static final native boolean _XtDispatchEvent(int event);
public static final boolean XtDispatchEvent(int event) {
	lock.lock();
	try {
		return _XtDispatchEvent(event);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native int _XtDisplay(int widget);
public static final int XtDisplay(int widget) {
	lock.lock();
	try {
		return _XtDisplay(widget);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XtDisplayToApplicationContext(int display);
public static final int XtDisplayToApplicationContext(int display) {
	lock.lock();
	try {
		return _XtDisplayToApplicationContext(display);
	} finally {
		lock.unlock();
	}
}
/** @param ptr cast=(char *) */
public static final native void _XtFree(int ptr);
public static final void XtFree(int ptr) {
	lock.lock();
	try {
		_XtFree(ptr);
	} finally {
		lock.unlock();
	}
}
/**
 * @param app_context cast=(XtAppContext)
 * @param dpy_return cast=(Display ***)
 * @param num_dpy_return cast=(Cardinal *)
 */
public static final native void _XtGetDisplays(int app_context, int[] dpy_return, int[] num_dpy_return);
public static final void XtGetDisplays(int app_context, int[] dpy_return, int[] num_dpy_return) {
	lock.lock();
	try {
		_XtGetDisplays(app_context, dpy_return, num_dpy_return);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XtGetMultiClickTime(int display);
public static final int XtGetMultiClickTime(int display) {
	lock.lock();
	try {
		return _XtGetMultiClickTime(display);
	} finally {
		lock.unlock();
	}
}
/**
 * @param w cast=(Widget)
 * @param selection cast=(Atom)
 * @param target cast=(Atom)
 * @param callback cast=(XtSelectionCallbackProc)
 * @param client_data cast=(XtPointer)
 * @param time cast=(Time)
 */
public static final native void _XtGetSelectionValue(int w, int selection, int target, int callback, int client_data, int time);
public static final void XtGetSelectionValue(int w, int selection, int target, int callback, int client_data, int time) {
	lock.lock();
	try {
		_XtGetSelectionValue(w, selection, target, callback, client_data, time);
	} finally {
		lock.unlock();
	}
}
/**
 * @method flags=no_gen
 * @param widget cast=(Widget)
 * @param argList cast=(ArgList)
 */
public static final native void _XtGetValues(int widget, int[] argList, int numArgs);
public static final void XtGetValues(int widget, int[] argList, int numArgs) {
	lock.lock();
	try {
		_XtGetValues(widget, argList, numArgs);
	} finally {
		lock.unlock();
	}
}
/**
 * @param w cast=(Widget)
 * @param event_mask cast=(EventMask)
 * @param nonmaskable cast=(Boolean)
 * @param proc cast=(XtEventHandler)
 * @param client_data cast=(XtPointer)
 * @param position cast=(XtListPosition)
 */
public static final native void _XtInsertEventHandler(int w, int event_mask, boolean nonmaskable, int proc, int client_data, int position);
public static final void XtInsertEventHandler(int w, int event_mask, boolean nonmaskable, int proc, int client_data, int position) {
	lock.lock();
	try {
		_XtInsertEventHandler(w, event_mask, nonmaskable, proc, client_data, position);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native boolean _XtIsManaged(int widget);
public static final boolean XtIsManaged(int widget) {
	lock.lock();
	try {
		return _XtIsManaged(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native boolean _XtIsRealized(int widget);
public static final boolean XtIsRealized(int widget) {
	lock.lock();
	try {
		return _XtIsRealized(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param widgetClass cast=(WidgetClass)
 */
public static final native boolean _XtIsSubclass(int widget, int widgetClass);
public static final boolean XtIsSubclass(int widget, int widgetClass) {
	lock.lock();
	try {
		return _XtIsSubclass(widget, widgetClass);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native boolean _XtIsTopLevelShell(int widget);
public static final boolean XtIsTopLevelShell(int widget) {
	lock.lock();
	try {
		return _XtIsTopLevelShell(widget);
	} finally {
		lock.unlock();
	}
}
/** @param display cast=(Display *) */
public static final native int _XtLastTimestampProcessed(int display);
public static final int XtLastTimestampProcessed(int display) {
	lock.lock();
	try {
		return _XtLastTimestampProcessed(display);
	} finally {
		lock.unlock();
	}
}
public static final native int _XtMalloc(int size);
public static final int XtMalloc(int size) {
	lock.lock();
	try {
		return _XtMalloc(size);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtManageChild(int widget);
public static final void XtManageChild(int widget) {
	lock.lock();
	try {
		_XtManageChild(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtMapWidget(int widget);
public static final void XtMapWidget(int widget) {
	lock.lock();
	try {
		_XtMapWidget(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtMoveWidget(int widget, int x, int y);
public static final void XtMoveWidget(int widget, int x, int y) {
	lock.lock();
	try {
		_XtMoveWidget(widget, x, y);
	} finally {
		lock.unlock();
	}
}
/**
 * @param reference cast=(Widget)
 * @param names cast=(String)
 */
public static final native int _XtNameToWidget(int reference, byte[] names);
public static final int XtNameToWidget(int reference, byte[] names) {
	lock.lock();
	try {
		return _XtNameToWidget(reference, names);
	} finally {
		lock.unlock();
	}
}
/**
 * @param xtAppContext cast=(XtAppContext)
 * @param displayName cast=(String)
 * @param applicationName cast=(String)
 * @param applicationClass cast=(String)
 * @param options cast=(XrmOptionDescRec *)
 * @param argc cast=(int *)
 * @param argv cast=(char **)
 */
public static final native int _XtOpenDisplay(int xtAppContext, byte[] displayName, byte[] applicationName, byte[] applicationClass, int options, int numOptions, int[] argc, int argv);
public static final int XtOpenDisplay(int xtAppContext, byte[] displayName, byte[] applicationName, byte[] applicationClass, int options, int numOptions, int[] argc, int argv) {
	lock.lock();
	try {
		return _XtOpenDisplay(xtAppContext, displayName, applicationName, applicationClass, options, numOptions, argc, argv);
	} finally {
		lock.unlock();
	}
}
/**
 * @param w cast=(Widget)
 * @param translations cast=(XtTranslations)
 */
public static final native void _XtOverrideTranslations(int w, int translations);
public static final void XtOverrideTranslations(int w, int translations) {
	lock.lock();
	try {
		_XtOverrideTranslations(w, translations);
	} finally {
		lock.unlock();
	}
}
/**
 * @param w cast=(Widget)
 * @param selection cast=(Atom)
 * @param time cast=(Time)
 * @param convert_proc cast=(XtConvertSelectionProc)
 * @param lose_selection cast=(XtLoseSelectionProc)
 * @param done_proc cast=(XtSelectionDoneProc)
 */
public static final native boolean _XtOwnSelection(int w, int selection, int time, int convert_proc, int lose_selection, int done_proc);
public static final boolean XtOwnSelection(int w, int selection, int time, int convert_proc, int lose_selection, int done_proc) {
	lock.lock();
	try {
		return _XtOwnSelection(w, selection, time, convert_proc, lose_selection, done_proc);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native int _XtParent(int widget);
public static final int XtParent(int widget) {
	lock.lock();
	try {
		return _XtParent(widget);
	} finally {
		lock.unlock();
	}
}
/** @param string cast=(String) */
public static final native int _XtParseTranslationTable(byte[] string);
public static final int XtParseTranslationTable(byte[] string) {
	lock.lock();
	try {
		return _XtParseTranslationTable(string);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtPopdown(int widget);
public static final void XtPopdown(int widget) {
	lock.lock();
	try {
		_XtPopdown(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtPopup(int widget, int flags);
public static final void XtPopup(int widget, int flags) {
	lock.lock();
	try {
		_XtPopup(widget, flags);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param intended cast=(XtWidgetGeometry *)
 * @param preferred_return cast=(XtWidgetGeometry *)
 */
public static final native int _XtQueryGeometry(int widget, XtWidgetGeometry intended, XtWidgetGeometry preferred_return);
public static final int XtQueryGeometry(int widget, XtWidgetGeometry intended, XtWidgetGeometry preferred_return) {
	lock.lock();
	try {
		return _XtQueryGeometry(widget, intended, preferred_return);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtRealizeWidget(int widget);
public static final void XtRealizeWidget(int widget) {
	lock.lock();
	try {
		_XtRealizeWidget(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param drawable cast=(Drawable)
 * @param widget cast=(Widget)
 */
public static final native void _XtRegisterDrawable(int display, int drawable, int widget);
public static final void XtRegisterDrawable(int display, int drawable, int widget) {
	lock.lock();
	try {
		_XtRegisterDrawable(display, drawable, widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param proc cast=(XtEventHandler)
 * @param client_data cast=(XtPointer)
 */
public static final native void _XtRemoveEventHandler(int widget, int event_mask, boolean nonmaskable, int proc, int client_data);
public static final void XtRemoveEventHandler(int widget, int event_mask, boolean nonmaskable, int proc, int client_data) {
	lock.lock();
	try {
		_XtRemoveEventHandler(widget, event_mask, nonmaskable, proc, client_data);
	} finally {
		lock.unlock();
	}
}
/** @param id cast=(XtInputId) */
public static final native void _XtRemoveInput(int id);
public static final void XtRemoveInput(int id) {
	lock.lock();
	try {
		_XtRemoveInput(id);
	} finally {
		lock.unlock();
	}
}
public static final native void _XtRemoveTimeOut(int id);
public static final void XtRemoveTimeOut(int id) {
	lock.lock();
	try {
		_XtRemoveTimeOut(id);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtResizeWidget(int widget, int width, int height, int borderWidth);
public static final void XtResizeWidget(int widget, int width, int height, int borderWidth) {
	lock.lock();
	try {
		_XtResizeWidget(widget, width, height, borderWidth);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtResizeWindow(int widget);
public static final void XtResizeWindow(int widget) {
	lock.lock();
	try {
		_XtResizeWindow(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param appContext cast=(XtAppContext)
 * @param languageProc cast=(XtLanguageProc)
 * @param pointer cast=(XtPointer)
 */
public static final native int _XtSetLanguageProc(int appContext, int languageProc, int pointer);
public static final int XtSetLanguageProc(int appContext, int languageProc, int pointer) {
	lock.lock();
	try {
		return _XtSetLanguageProc(appContext, languageProc, pointer);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtSetMappedWhenManaged(int widget, boolean flag);
public static final void XtSetMappedWhenManaged(int widget, boolean flag) {
	lock.lock();
	try {
		_XtSetMappedWhenManaged(widget, flag);
	} finally {
		lock.unlock();
	}
}
/**
 * @param widget cast=(Widget)
 * @param argList cast=(ArgList)
 */
public static final native void _XtSetValues(int widget, int[] argList, int numArgs);
public static final void XtSetValues(int widget, int[] argList, int numArgs) {
	lock.lock();
	try {
		_XtSetValues(widget, argList, numArgs);
	} finally {
		lock.unlock();
	}
}
public static final native void _XtToolkitInitialize();
public static final void XtToolkitInitialize() {
	lock.lock();
	try {
		_XtToolkitInitialize();
	} finally {
		lock.unlock();
	}
}
public static final native boolean _XtToolkitThreadInitialize();
public static final boolean XtToolkitThreadInitialize() {
	lock.lock();
	try {
		return _XtToolkitThreadInitialize();
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtTranslateCoords(int widget, short x, short y, short[] root_x, short[] root_y);
public static final void XtTranslateCoords(int widget, short x, short y, short[] root_x, short[] root_y) {
	lock.lock();
	try {
		_XtTranslateCoords(widget, x, y, root_x, root_y);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtUnmanageChild(int widget);
public static final void XtUnmanageChild(int widget) {
	lock.lock();
	try {
		_XtUnmanageChild(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtUnmapWidget(int widget);
public static final void XtUnmapWidget(int widget) {
	lock.lock();
	try {
		_XtUnmapWidget(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param drawable cast=(Drawable)
 */
public static final native void _XtUnregisterDrawable(int display, int drawable);
public static final void XtUnregisterDrawable(int display, int drawable) {
	lock.lock();
	try {
		_XtUnregisterDrawable(display, drawable);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native int _XtWindow(int widget);
public static final int XtWindow(int widget) {
	lock.lock();
	try {
		return _XtWindow(widget);
	} finally {
		lock.unlock();
	}
}
/**
 * @param display cast=(Display *)
 * @param widget cast=(Window)
 */
public static final native int _XtWindowToWidget(int display, int widget);
public static final int XtWindowToWidget(int display, int widget) {
	lock.lock();
	try {
		return _XtWindowToWidget(display, widget);
	} finally {
		lock.unlock();
	}
}
/** @param menu cast=(Widget) */
public static final native void __XmSetMenuTraversal(int menu, boolean traversal);
public static final void _XmSetMenuTraversal(int menu, boolean traversal) {
	lock.lock();
	try {
		__XmSetMenuTraversal(menu, traversal);
	} finally {
		lock.unlock();
	}
}
public static final native int close(int filedes);
public static final native int fd_set_sizeof();
/**
 * @param cd cast=(iconv_t)
 * @param inBuf cast=(void *)
 * @param inBytesLeft cast=(size_t *)
 * @param outBuf cast=(char **)
 * @param outBytesLeft cast=(size_t *)
 */
public static final native int iconv(int cd, int[] inBuf, int[] inBytesLeft, int[] outBuf, int[] outBytesLeft);
/** @param cd cast=(iconv_t) */
public static final native int iconv_close(int cd);
/**
 * @param tocode cast=(const char *)
 * @param fromcode cast=(const char *)
 */
public static final native int iconv_open(byte[] tocode, byte[] fromcode);
public static final native int localeconv_decimal_point();
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param count cast=(size_t)
 */
public static final native void memmove(int dest, XImage src, int count);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param count cast=(size_t)
 */
public static final native void memmove(int dest, XmDragProcCallbackStruct src, int count);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param count cast=(size_t)
 */
public static final native void memmove(int dest, XmSpinBoxCallbackStruct src, int count);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param count cast=(size_t)
 */
public static final native void memmove(int dest, XmTextBlockRec src, int count);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param count cast=(size_t)
 */
public static final native void memmove(int dest, XmTextVerifyCallbackStruct src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(Visual dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XAnyEvent dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XButtonEvent dest, int src, int count);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param count cast=(size_t)
 */
public static final native void memmove(int dest, XButtonEvent src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XCharStruct dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XClientMessageEvent dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XConfigureEvent dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XCreateWindowEvent dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XCrossingEvent dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XDestroyWindowEvent dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XExposeEvent dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XFocusChangeEvent dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XFontStruct dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XImage dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XineramaScreenInfo dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XKeyEvent dest, int src, int count);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XEvent dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XModifierKeymap dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XMotionEvent dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XPropertyEvent dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XReparentEvent dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XmAnyCallbackStruct dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XmDragProcCallbackStruct dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XmDropFinishCallbackStruct dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XmDropProcCallbackStruct dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XmSpinBoxCallbackStruct dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XmTextBlockRec dest, int src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XmTextVerifyCallbackStruct dest, int src, int count);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param count cast=(size_t)
 */
public static final native void memmove(int dest, XExposeEvent src, int count);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param count cast=(size_t)
 */
public static final native void memmove(int dest, XClientMessageEvent src, int count);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param count cast=(size_t)
 */
public static final native void memmove(int dest, XConfigureEvent src, int count);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param count cast=(size_t)
 */
public static final native void memmove(int dest, XKeyEvent src, int count);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param count cast=(size_t)
 */
public static final native void memmove(XIconSize dest, int src, int count);
public static final native int nl_langinfo(int item);
/** @param filedes cast=(int *) */
public static final native int pipe(int[] filedes);
/** @param buf cast=(char *) */
public static final native int read(int filedes, byte[] buf, int nbyte);
/**
 * @param readfds cast=(fd_set *)
 * @param writefds cast=(fd_set *)
 * @param exceptfds cast=(fd_set *)
 * @param timeout cast=(struct timeval *)
 */
public static final native int select(int n, byte[] readfds, byte[] writefds, byte[] exceptfds, int[] timeout);
/** @param locale cast=(char *) */
public static final native int setlocale(int category, byte[] locale);
/** @param buf cast=(char *) */
public static final native int write(int filedes, byte[] buf, int nbyte);

}
