/*
 * Copyright (c) 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Andre Weinand, OTI - Initial version
 */
package org.eclipse.swt.internal.carbon;

import org.eclipse.swt.internal.Library;

public class OS {

    static {
		Library.loadLibrary("swt");
	}	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	// Carbon Toolbox native API
	//////////////////////////////////////////////////////////////////////////////////////////////////

	// status
	public static final int kNoErr = 0;
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Appearance Manager
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final short kThemeBrushDialogBackgroundActive = 1; /* Dialogs */
	public static final short kThemeBrushDialogBackgroundInactive = 2; /* Dialogs */
	public static final short kThemeBrushAlertBackgroundActive = 3;
	public static final short kThemeBrushAlertBackgroundInactive = 4;
	public static final short kThemeBrushModelessDialogBackgroundActive = 5;
	public static final short kThemeBrushModelessDialogBackgroundInactive = 6;
	public static final short kThemeBrushUtilityWindowBackgroundActive = 7; /* Miscellaneous */
	public static final short kThemeBrushUtilityWindowBackgroundInactive = 8; /* Miscellaneous */
	public static final short kThemeBrushListViewSortColumnBackground = 9; /* Finder */
	public static final short kThemeBrushListViewBackground = 10;
	public static final short kThemeBrushIconLabelBackground = 11;
	public static final short kThemeBrushListViewSeparator  = 12;
	public static final short kThemeBrushChasingArrows      = 13;
	public static final short kThemeBrushDragHilite         = 14;
	public static final short kThemeBrushDocumentWindowBackground = 15;
	public static final short kThemeBrushFinderWindowBackground = 16;

	public static final short kThemeSystemFont              = 0;
	public static final short kThemeSmallSystemFont         = 1;
	public static final short kThemeSmallEmphasizedSystemFont = 2;
	public static final short kThemeViewsFont               = 3;    /* The following ID's are only available with MacOS X or CarbonLib 1.3 and later*/
	public static final short kThemeEmphasizedSystemFont    = 4;
	public static final short kThemeApplicationFont         = 5;
	public static final short kThemeLabelFont               = 6;
	public static final short kThemeMenuTitleFont           = 100;
	public static final short kThemeMenuItemFont            = 101;
	public static final short kThemeMenuItemMarkFont        = 102;
	public static final short kThemeMenuItemCmdKeyFont      = 103;
	public static final short kThemeWindowTitleFont         = 104;
	public static final short kThemePushButtonFont          = 105;
	public static final short kThemeUtilityWindowTitleFont  = 106;
	public static final short kThemeAlertHeaderFont         = 107;
	public static final short kThemeCurrentPortFont         = 200;
	
	public static final short kThemeStateInactive = 0;
	public static final short kThemeStateActive = 1;
	public static final short kThemeStatePressed = 2;
	public static final short kThemeStateRollover = 6;
	public static final short kThemeStateUnavailable = 7;
	public static final short kThemeStateUnavailableInactive = 8;
	
	public static final short kThemeSmallBevelButton	= 8;    /* small-shadow bevel button */

	public static final short kThemeButtonOff	= 0;
	public static final short kThemeButtonOn	= 1;

	public static final short smSystemScript = -1; 	/* designates system script.*/

	public static native int RegisterAppearanceClient();

	public static native int SetThemeWindowBackground(int wHandle, short brush, boolean update);
	
	public static native int DrawThemeTextBox(int sHandle, short fontID, int state, boolean wrapToWidth,
			short[] bounds, short just, int context);

	public static native int GetThemeTextDimensions(int sHandle, short fontID, int state, boolean wrapToWidth,
			short[] ioBounds, short[] baseLine);
		
	public static native int DrawThemeEditTextFrame(short[] bounds, int state);
	public static native int DrawThemeFocusRect(short[] bounds, boolean hasFocus);
	public static native int DrawThemeGenericWell(short[] bounds, int state, boolean fillCenter);
	public static native int DrawThemeSeparator(short[] bounds, int state);
	
	public static native int GetThemeFont(short themeFontId, short scriptCode,
				byte[] fontName, short[] fontSize, byte[] style);
	
	public static native int DrawThemeButton(short[] bounds, short kind, short[] newInfo, short[] prevInfo,
				int eraseProc, int labelProc, int userData);
				
	public static native int SetThemeBackground(short inBrush, short depth, boolean isColorDevice);
	public static native int GetThemeDrawingState(int[] state);
	public static native int SetThemeDrawingState(int state, boolean disposeNow);
				
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Event Manager
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// event what
	public static final short nullEvent	= 0;
	public static final short mouseDown	= 1;
	public static final short mouseUp	= 2;
	public static final short keyDown	= 3;
	public static final short keyUp		= 4;
	public static final short autoKey	= 5;
	public static final short updateEvt	= 6;
	public static final short diskEvt	= 7;
	public static final short activateEvt	= 8;
	public static final short osEvt		= 15;
	public static final short kHighLevelEvent	= 23;

	// masks
	public static final short updateMask	= 1 << updateEvt;
	public static final short everyEvent	= (short) 0xFFFF;
	
	// masks
	public static final int charCodeMask	= 0x000000FF;
	public static final int keyCodeMask		= 0x0000FF00;

	// EventModifiers
	public static final int activeFlag		= 1;	/* activate? (activateEvt and mouseDown)*/
	public static final int btnState		= 1 << 7;	/* state of button?*/
	public static final int cmdKey			= 1 << 8;	/* command key down?*/
	public static final int shiftKey		= 1 << 9;	/* shift key down?*/
	public static final int alphaLock		= 1 << 10;	/* alpha lock down?*/
	public static final int optionKey		= 1 << 11;	/* option key down?*/
	public static final int controlKey		= 1 << 12;	/* control key down?*/
	public static final int rightShiftKey	= 1 << 13;	/* right shift key down?*/
	public static final int rightOptionKey	= 1 << 14;	/* right Option key down?*/
	public static final int rightControlKey	= 1 << 15;	/* right Control key down?*/
	
	public static native boolean GetNextEvent(short eventMask, int[] eventData);
    public static native boolean WaitNextEvent(short eventMask, int[] eventData, int sleepTime);
	public static native boolean StillDown();
	public static native void GetMouse(short[] where);
	public static native void AEProcessAppleEvent(int[] eventData);
	public static native int MenuEvent(int[] eventData);
	public static native int PostEvent(short eventNum, int eventMsg);
	public static native int GetKeyboardFocus(int wHandle, int[] cHandle);
	public static native int SetKeyboardFocus(int wHandle, int cHandle, short inPart);
	public static native boolean IsShowContextualMenuClick(int[] eventData);
	public static native int ContextualMenuSelect(int mHandle, short[] location, short[] menuId, short[] index);

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Carbon Event Manager
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final double kEventDurationForever	= -1.0;
	public static final double kEventDurationNoWait		= 0.0;
	
	public static final int eventNotHandledErr	= -9874;
	public static final int eventLoopTimedOutErr= -9875;

	public static final int kEventAttributeNone			= 0;
	public static final int kEventAttributeUserEvent	= 1 << 0;

	public static final int kEventClassMouse		= ('m'<<24) + ('o'<<16) + ('u'<<8) + 's';
	public static final int kEventClassKeyboard		= ('k'<<24) + ('e'<<16) + ('y'<<8) + 'b';
	public static final int kEventClassTextInput	= ('t'<<24) + ('e'<<16) + ('x'<<8) + 't';
	public static final int kEventClassApplication	= ('a'<<24) + ('p'<<16) + ('p'<<8) + 'l';
	public static final int kEventClassAppleEvent	= ('e'<<24) + ('p'<<16) + ('p'<<8) + 'c';
		public static final int kEventAppleEvent	= 1;
	public static final int kEventClassMenu			= ('m'<<24) + ('e'<<16) + ('n'<<8) + 'u';
	public static final int kEventClassWindow		= ('w'<<24) + ('i'<<16) + ('n'<<8) + 'd';
	public static final int kEventClassControl		= ('c'<<24) + ('n'<<16) + ('t'<<8) + 'l';
	public static final int kEventClassTablet		= ('t'<<24) + ('b'<<16) + ('l'<<8) + 't';
	public static final int kEventClassVolume		= ('v'<<24) + ('o'<<16) + ('l'<<8) + ' ';
	public static final int kEventClassAppearance	= ('a'<<24) + ('p'<<16) + ('p'<<8) + 'm';
	public static final int kEventClassService		= ('s'<<24) + ('e'<<16) + ('r'<<8) + 'v';
	public static final int kEventClassCommand		= ('c'<<24) + ('m'<<16) + ('d'<<8) + 's';
		public static final int kEventProcessCommand	= 1;

	
	public static final int typeUInt32= ('m'<<24) + ('a'<<16) + ('g'<<8) + 'n';
	public static final int typeChar= ('T'<<24) + ('E'<<16) + ('X'<<8) + 'T';
	public static final int typeUnicodeText= ('u'<<24) + ('t'<<16) + ('x'<<8) + 't';
	public static final int typeWindowRef= ('w'<<24) + ('i'<<16) + ('n'<<8) + 'd';
	public static final int typeWindowDefPartCode= ('w'<<24) + ('d'<<16) + ('p'<<8) + 't';
	public static final int typeControlRef= ('c'<<24) + ('t'<<16) + ('r'<<8) + 'l';
	public static final int typeMouseButton= ('m'<<24) + ('b'<<16) + ('t'<<8) + 'n';
	public static final int typeQDPoint= ('Q'<<24) + ('D'<<16) + ('p'<<8) + 't';
	public static final int typeType= ('t'<<24) + ('y'<<16) + ('p'<<8) + 'e';
	
	public static final int kEventParamAEEventID= ('e'<<24) + ('v'<<16) + ('t'<<8) + 'i';
	public static final int kEventParamAEEventClass= ('e'<<24) + ('v'<<16) + ('c'<<8) + 'l';
	
	public static final int kEventParamWindowDefPart= ('w'<<24) + ('d'<<16) + ('p'<<8) + 'c';
	public static final int kEventParamControlRef= ('c'<<24) + ('t'<<16) + ('r'<<8) + 'l';
	public static final int kEventParamMouseButton= ('m'<<24) + ('b'<<16) + ('t'<<8) + 'n';
	public static final int kEventParamMouseLocation= ('m'<<24) + ('l'<<16) + ('o'<<8) + 'c';
	public static final int kEventParamMouseChord= ('c'<<24) + ('h'<<16) + ('o'<<8) + 'r';

	public static final int kEventParamTextInputSendText= ('t'<<24) + ('s'<<16) + ('t'<<8) + 'x';

	public static final int kEventParamKeyCode= ('k'<<24) + ('c'<<16) + ('o'<<8) + 'd';
	public static final int kEventParamKeyMacCharCodes= ('k'<<24) + ('c'<<16) + ('h'<<8) + 'r';
	public static final int kEventParamKeyModifiers= ('k'<<24) + ('m'<<16) + ('o'<<8) + 'd';
	public static final int kEventParamKeyUnicodes= ('k'<<24) + ('u'<<16) + ('n'<<8) + 'i';
	
	public static final short kEventMouseButtonPrimary= 1;		// the left mouse button
	public static final short kEventMouseButtonSecondary= 2;	// the right mouse button
	public static final short kEventMouseButtonTertiary= 3;		// the middle mouse button

	public static final int kEventMouseDown		= 1;
	public static final int kEventMouseUp		= 2;
	public static final int kEventMouseMoved	= 5;
	public static final int kEventMouseDragged	= 6;
	public static final int kEventMouseWheelMoved	= 10;
	
	public static final int kEventRawKeyDown	= 1;    // A key was pressed
	public static final int kEventRawKeyRepeat	= 2;	// Sent periodically as a key is held down by the user
	public static final int kEventRawKeyUp		= 3;	// A key was released
	public static final int kEventRawKeyModifiersChanged= 4;	// The keyboard modifiers (bucky bits) have changed.
	public static final int kEventHotKeyPressed	= 5;	// A registered Hot Key was pressed.
	public static final int kEventHotKeyReleased= 6;	// A registered Hot Key was released (this is only sent on Mac OS X).
		
	public static final int kEventWindowDrawContent	= 2;
	public static final int kEventWindowActivated	= 5;
	public static final int kEventWindowDeactivated	= 6;
	public static final int kEventWindowBoundsChanged = 27;
	public static final int kEventWindowClose		= 72;
	
	public static final int kWindowBoundsChangeUserDrag   = (1 << 0);
	public static final int kWindowBoundsChangeUserResize = (1 << 1);
	public static final int kWindowBoundsChangeSizeChanged = (1 << 2);
	public static final int kWindowBoundsChangeOriginChanged = (1 << 3);

	public static final int kEventMenuBeginTracking = 1;
	public static final int kEventMenuEndTracking = 2;
		
	public static final int kEventParamDirectObject	= ('-'<<24) + ('-'<<16) + ('-'<<8) + '-'; /* type varies depending on event*/
	public static final int kEventParamAttributes	= ('a'<<24) + ('t'<<16) + ('t'<<8) + 'r'; /* typeUInt32*/

	public static final int kEventTextInputUnicodeForKeyEvent = 2;
	
	public static final int kAEQuitApplication = ('q'<<24) + ('u'<<16) + ('i'<<8) + 't';

	public static native int CallNextEventHandler(int nextHandler, int eventRefHandle); 
	
	public static native int InstallEventHandler(int eventTargetRef, int controlHandlerUPP, int[] eventTypes, int clientData);
	
	public static native int GetEventHICommand(int eRefHandle, int[] outParamType);
			
	public static native int GetEventParameter(int eRefHandle, int paramName, int paramType, int[] outParamType,
			int[] outActualSize, byte[] data);
	public static native int GetEventParameter(int eRefHandle, int paramName, int paramType, int[] outParamType,
			int[] outActualSize, char[] data);
	public static native int GetEventParameter(int eRefHandle, int paramName, int paramType, int[] outParamType,
			int[] outActualSize, short[] data);
	public static native int GetEventParameter(int eRefHandle, int paramName, int paramType, int[] outParamType,
			int[] outActualSize, int[] data);
	public static native int SetEventParameter(int eRefHandle, int paramName, int paramType, char[] data);

	public static native int GetControlEventTarget(int cHandle);
	public static native int GetMenuEventTarget(int cHandle);
	public static native int GetUserFocusEventTarget();
	public static native int GetApplicationEventTarget();
			
	public static native int GetUserFocusWindow();
	
	public static native int GetCurrentEventLoop();
	public static native int NewEventLoopTimerUPP(Object target, String method);
	public static native int NewEventLoopTimerUPP2(Object target, String method);
	public static native int NewEventLoopTimerUPP3(Object target, String method);
	public static native int NewTextCallbackUPP(Object target, String method);
	public static native int NewMouseMovedCallbackUPP(Object target, String method);
	public static native int NewWindowCallbackUPP(Object target, String method);
	public static native int NewApplicationCallbackUPP(Object target, String method);

	public static native int InstallEventLoopTimer(int inEventLoop, double inFireDelay, 
			double inInterval, int inTimerProc, int inTimerData, int[] outTimer);
	public static native int RemoveEventLoopTimer(int inTimer);
	public static native double GetLastUserEventTime();
	public static native int ReceiveNextEvent(int[] eventTypeSpecList, double inTimeout, boolean inPullEvent, int[] outEvent);
	public static native int GetEventDispatcherTarget();
	public static native int SendEventToEventTarget(int theEvent, int theTarget);
	public static native void ReleaseEvent(int theEvent);
	public static native boolean ConvertEventRefToEventRecord(int eHandle, int[] outEvent);
	public static native int InstallStandardEventHandler(int inTarget);
	public static native int GetWindowEventTarget(int wHandle);
	public static native int GetEventClass(int eHandle);
	public static native int GetEventKind(int eHandle);
	public static native double GetEventTime(int eHandle);
	public static native int GetMouseLocation(int eHandle, short[] location);
	public static native int TrackMouseLocation(int portHandle, short[] outPt, short[] outResult);
	public static native void GetGlobalMouse(short[] where);
	public static native int CreateEvent(int allocator, int inClassID, int kind, double when, int flags, int[] outEventRef);
	public static native int PostEventToQueue(int inQueue, int inEvent, short inPriority);
	public static native int GetMainEventQueue();

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Font manager
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static final short kInvalidFontFamily	= -1;

	public static native short FMGetFontFamilyFromName(byte[] name);
	public static native int FMGetFontFamilyName(short id, byte[] name);
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Cursors
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static final int kThemeArrowCursor = 0;
    public static final int kThemeCopyArrowCursor = 1;
    public static final int kThemeAliasArrowCursor = 2;
    public static final int kThemeContextualMenuArrowCursor = 3;
    public static final int kThemeIBeamCursor = 4;
    public static final int kThemeCrossCursor = 5;
    public static final int kThemePlusCursor = 6;
    public static final int kThemeWatchCursor = 7;
    public static final int kThemeClosedHandCursor = 8;
    public static final int kThemeOpenHandCursor = 9;
    public static final int kThemePointingHandCursor = 10;
    public static final int kThemeCountingUpHandCursor = 11;
    public static final int kThemeCountingDownHandCursor = 12;
    public static final int kThemeCountingUpAndDownHandCursor = 13;
    public static final int kThemeSpinningCursor = 14;
    public static final int kThemeResizeLeftCursor = 15;
    public static final int kThemeResizeRightCursor = 16;
    public static final int kThemeResizeLeftRightCursor = 17;
 
    public static final short iBeamCursor = 1;
    public static final short crossCursor = 2;
    public static final short plusCursor = 3;
    public static final short watchCursor = 4;

	public static native void InitCursor();
	public static native int NewCursor(short hotX, short hotY, short[] data, short[] mask);
	public static native int GetCursor(short id);
	public static native void SetCursor(int cursor);
	public static native int SetThemeCursor(int themeCursor);

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	// QuickDraw
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// transfer modes
	public static final short srcCopy	= 0;
	public static final short srcOr		= 1;
	
	// text faces
	public static final short normal	= 0;
	public static final short bold		= 1;
	public static final short italic	= 2;
	
	public static final int kQDUseDefaultTextRendering = 0;
	public static final int kQDUseTrueTypeScalerGlyphs = (1 << 0);
	public static final int kQDUseCGTextRendering = (1 << 1);
	public static final int kQDUseCGTextMetrics = (1 << 2);
	public static final int kQDSupportedFlags =
				kQDUseTrueTypeScalerGlyphs | kQDUseCGTextRendering | kQDUseCGTextMetrics;
	public static final int kQDDontChangeFlags = 0xFFFFFFFF;
	
	public static native int QDSwapTextFlags(int flags);
	public static native void QDSetPatternOrigin(short[] point);
	public static native int GetQDGlobalsScreenBits(int bitmap);

	public static native int GetPort();
	public static native int GetWindowFromPort(int pHandle);
	public static native void SetPort(int portHandle);
	public static native void NormalizeThemeDrawingState();
	public static native void RGBForeColor(int packed);
	public static native void RGBBackColor(int packed);
	public static native void GlobalToLocal(short[] point);
	public static native void LocalToGlobal(short[] point);
	public static native void ScrollRect(short[] rect, short dh, short dv, int updateRgn);
	public static native int GetPortVisibleRegion(int portHandle, int rgnHandle);
	public static native void SetPortVisibleRegion(int portHandle, int rgnHandle);
	public static native void QDFlushPortBuffer(int port, int rgnHandle);
	public static native int QDGetDirtyRegion(int portHandle, int rgnHandle);
	public static native int QDSetDirtyRegion(int portHandle, int rgnHandle);
	public static native int LockPortBits(int portHandle);
	public static native int UnlockPortBits(int portHandle);

	// clipping
	public static native void ClipRect(short[] clipRect);
	public static native void GetClip(int rgnHandle);
	public static native void SetClip(int rgnHandle);	
	public static native void SetOrigin(short h, short v);	
	
	// Text
	public static native void TextFont(short fontID);
	public static native void TextSize(short size);
	public static native void TextFace(short face);
	public static native void TextMode(short mode);
	public static native void DrawText(String s, short font, short size, short face);
	public static native short TextWidth(String s, short font, short size, short face);
	public static native short CharWidth(byte c);
	public static native void GetFontInfo(short[] info);	// FontInfo: short[4]
	public static native void SetFractEnable(boolean enable);
	
	// Lines & Polygons
	public static native void PenSize(short h, short v);
	public static native void MoveTo(short h, short v);
	public static native void LineTo(short h, short v);
	
	// Rectangles
	public static native void EraseRect(short[] bounds);	// rect: short[4]
	public static native void FrameRect(short[] bounds);
	public static native void PaintRect(short[] bounds);
	public static native void InvertRect(short x, short y, short w, short h);
	
	// Ovals
	public static native void FrameOval(short[] bounds);
	public static native void PaintOval(short[] bounds);
	
	// Round Rectangle
	public static native void FrameRoundRect(short[] bounds, short ovalWidth, short ovalHeight);
	public static native void PaintRoundRect(short[] bounds, short ovalWidth, short ovalHeight);
	
	// Regions
	public static native int NewRgn();
	public static native void SetEmptyRgn(int rgnHandle);
	public static native void RectRgn(int rgnHandle, short[] rect);
	public static native void SetRectRgn(int rgnHandle, short left, short top, short right, short bottom);
	public static native void DisposeRgn(int rgnHandle);
	public static native boolean EmptyRgn(int rgnHandle);
	public static native void GetRegionBounds(int rgnHandle, short[] bounds);
	public static native void SectRgn(int srcRgnA, int srcRgnB, int dstRgn);
	public static native void UnionRgn(int srcRgnA, int srcRgnB, int dstRgn);
	public static native void DiffRgn(int srcRgnA, int srcRgnB, int dstRgn);
	public static native boolean PtInRgn(short[] pt, int rgnHandle);
	public static native boolean RectInRgn(short[] rect, int rgnHandle);
	public static native void CopyRgn(int srcRgnHandle, int dstRgnHandle);
	public static native void OffsetRgn(int rgnHandle, short dh, short dv);
	
	public static native void EraseRgn(int rgnHandle);
	public static native void InvertRgn(int rgnHandle);
	
	// Polygons
	public static native int OpenPoly();
	public static native void ClosePoly();
	public static native void OffsetPoly(int polyHandle, short dx, short dy);
	public static native void FramePoly(int polyHandle);
	public static native void PaintPoly(int polyHandle);
	public static native void KillPoly(int polyHandle);
	

    // BitMaps & PixMaps
	public static final short Indexed= 0;
	public static final short RGBDirect= 16;
	
	public static native int NewPixMap(short w, short h, short rowBytes,
			short pixelType, short pixelSize, short cmpSize, short cmpCount, short pixelFormat);
	public static native void DisposePixMap(int pHandle);
	public static native int duplicatePixMap(int srcPixmap);
	
	public static native int getRowBytes(int pHandle);
	public static native void setRowBytes(int pHandle, short rowBytes);
	public static native int GetPixRowBytes(int pHandle);
	public static native void GetPixBounds(int pHandle, short[] bounds);
	public static native void setPixBounds(int pHandle, short top, short left, short bottom, short right);
	public static native short GetPixDepth(int pHandle);
	public static native int getPixHRes(int pHandle);
	public static native int getPixVRes(int pHandle);
	public static native int getBaseAddr(int pHandle);
	public static native void setBaseAddr(int pHandle, int data);
	public static native int getColorTableSize(int pHandle);	// returns number of slots
	public static native void getColorTable(int pHandle, short[] colorSpec);
	public static native void setColorTable(int pHandle, short[] colorSpec);

	public static native void CopyBits(int srcPixMapHandle, int dstPixMapHandle, short[] srcRect, short[] dstRect,
											short mode, int maskRgn);
	public static native void CopyMask(int srcPixMapHandle, int maskPixMapHandle, int dstPixMapHandle,
											short[] srcRect, short[] maskRect, short[] dstRect);
	public static native void CopyDeepMask(int srcPixMapHandle, int maskPixMapHandle, int dstPixMapHandle,
											short[] srcRect, short[] maskRect, short[] dstRect, short mode, int maskRgn);

	public static native int GetPortBitMapForCopyBits(int portHandle);
	
	// CIcon
	public static native int NewCIcon(int pixmapHandle, int maskHandle);
	public static native int getCIconIconData(int iconHandle);
	public static native int getCIconColorTable(int iconHandle);

	// GWorlds
	public static native int NewGWorldFromPtr(int[] offscreenGWorld, int pHandle);
	public static native void DisposeGWorld(int offscreenGWorld);
	public static native void SetGWorld(int portHandle, int gdHandle);
	public static native void GetGWorld(int[] portHandle, int[] gdHandle);
	public static native int GetGDevice();
	public static native int GetMainDevice();
	public static native int getgdPMap(int gdHandle);

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Window Manager
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	// window class
	public static final int kAlertWindowClass             = 1;   /* I need your attention now.*/
	public static final int kMovableAlertWindowClass      = 2;   /* I need your attention now, but I'm kind enough to let you switch out of this app to do other things.*/
	public static final int kModalWindowClass             = 3;   /* system modal, not draggable*/
	public static final int kMovableModalWindowClass      = 4;   /* application modal, draggable*/
	public static final int kFloatingWindowClass          = 5;   /* floats above all other application windows*/
	public static final int kDocumentWindowClass          = 6;   /* document windows*/
	public static final int kUtilityWindowClass           = 8;   /* system-wide floating windows (TSM, AppleGuide) (available in CarbonLib 1.1)*/
	public static final int kHelpWindowClass              = 10;  /* help window (no frame; coachmarks, help tags ) (available in CarbonLib 1.1)*/
	public static final int kSheetWindowClass             = 11;  /* sheet windows for dialogs (available in Mac OS X and CarbonLib 1.3)*/
	public static final int kToolbarWindowClass           = 12;  /* toolbar windows (above documents, below floating windows) (available in CarbonLib 1.1)*/
	public static final int kPlainWindowClass             = 13;  /* plain window (in document layer)*/
	public static final int kOverlayWindowClass           = 14;  /* transparent window which allows 'screen' drawing via CoreGraphics (Mac OS X only)*/
	public static final int kSheetAlertWindowClass        = 15;  /* sheet windows for alerts (available in Mac OS X after 10.0.x and CarbonLib 1.3)*/
	public static final int kAltPlainWindowClass          = 16;  /* alternate plain window (in document layer) (available in Mac OS X after 10.0.x and CarbonLib 1.3)*/
	public static final int kAllWindowClasses             = 0xFFFFFFFF; /* for use with GetFrontWindowOfClass, FindWindowOfClass, GetNextWindowOfClass*/

	// window attributes
	public static final int kWindowNoAttributes           = 0;
	/* This window has a close box.
	 * Available for windows of kDocumentWindowClass, kFloatingWindowClass, and kUtilityWindowClass. */
	public static final int kWindowCloseBoxAttribute      = (1 << 0);
	/* This window changes width when zooming.
	 * Available for windows of kDocumentWindowClass, kFloatingWindowClass, and kUtilityWindowClass. */
	public static final int kWindowHorizontalZoomAttribute = (1 << 1);
	/* This window changes height when zooming.
	 * Available for windows of kDocumentWindowClass, kFloatingWindowClass, and kUtilityWindowClass. */
	public static final int kWindowVerticalZoomAttribute  = (1 << 2);
	/* This window changes both width and height when zooming.
	 * Available for windows of kDocumentWindowClass, kFloatingWindowClass, and kUtilityWindowClass. */
	public static final int kWindowFullZoomAttribute = (kWindowVerticalZoomAttribute | kWindowHorizontalZoomAttribute);
	/* This window has a collapse box.
	 * Available for windows of kDocumentWindowClass and, on Mac OS 9, kFloatingWindowClass and
	 * kUtilityWindowClass; not available for windows of kFloatingWindowClass or kUtilityWindowClass on Mac OS X. */
	public static final int kWindowCollapseBoxAttribute   = (1 << 3);
	/* This window can be resized.
	 * Available for windows of kDocumentWindowClass, kMovableModalWindowClass,
	 * kFloatingWindowClass, kUtilityWindowClass, and kSheetWindowClass. */
	public static final int kWindowResizableAttribute     = (1 << 4);
	/* This window has a vertical titlebar on the side of the window.
	 * Available for windows of kFloatingWindowClass and kUtilityWindowClass. */
	public static final int kWindowSideTitlebarAttribute  = (1 << 5);
	/* This window has a toolbar button.
	 * Available for windows of kDocumentWindowClass on Mac OS X. */
	public static final int kWindowToolbarButtonAttribute = (1 << 6);
	/* This window receives no update events.
	 * Available for all windows. */
	public static final int kWindowNoUpdatesAttribute     = (1 << 16);
	/* This window receives no activate events.
	 * Available for all windows.*/
	public static final int kWindowNoActivatesAttribute   = (1 << 17);
	/* This window receives mouse events even for areas of the window
	 * that are transparent (have an alpha channel component of zero).
	 * Available for windows of kOverlayWindowClass on Mac OS X.*/
	public static final int kWindowOpaqueForEventsAttribute = (1 << 18);	
	/* This window has no shadow.
	 * Available for all windows on Mac OS X.
	 * This attribute is automatically given to windows of kOverlayWindowClass. */
	public static final int kWindowNoShadowAttribute      = (1 << 21);
	/* This window is automatically hidden on suspend and shown on resume.
	 * Available for all windows. This attribute is automatically
	 * given to windows of kFloatingWindowClass, kHelpWindowClass, and
	 * kToolbarWindowClass. */
	public static final int kWindowHideOnSuspendAttribute = (1 << 24);	
	/* This window has the standard Carbon window event handler installed.
	 * Available for all windows. */
	public static final int kWindowStandardHandlerAttribute = (1 << 25);
	/* This window is automatically hidden during fullscreen mode (when the menubar is invisible) and shown afterwards.
	 * Available for all windows.
	 * This attribute is automatically given to windows of kUtilityWindowClass. */	 
	public static final int kWindowHideOnFullScreenAttribute = (1 << 26);	
	/* This window is added to the standard Window menu.
	 * Available for windows of kDocumentWindowClass.
	 * This attribute is automatically given to windows of kDocumentWindowClass. */
	public static final int kWindowInWindowMenuAttribute  = (1 << 27);
	/* This window supports live resizing.
	 * Available for all windows on Mac OS X. */
	public static final int kWindowLiveResizeAttribute    = (1 << 28);
	/* This window will not be repositioned by the default kEventWindowConstrain
	 * handler in response to changes in monitor size, Dock position, and so on.
	 * Available for all windows on Mac OS X after 10.0.x. */
	public static final int kWindowNoConstrainAttribute   = (1 << 31);
	/* The minimum set of window attributes commonly used by document windows. */
	public static final int kWindowStandardDocumentAttributes = (kWindowCloseBoxAttribute | kWindowFullZoomAttribute | kWindowCollapseBoxAttribute | kWindowResizableAttribute);
	/* The minimum set of window attributes commonly used by floating windows. */
	public static final int kWindowStandardFloatingAttributes = (kWindowCloseBoxAttribute | kWindowCollapseBoxAttribute);
	
	// window modality
	public static final int kWindowModalityNone = 0;
	public static final int kWindowModalitySystemModal = 1;
	public static final int kWindowModalityAppModal = 2;
	public static final int kWindowModalityWindowModal = 3;
	
	// ScrollWindowOptions
	public static final int kScrollWindowNoOptions= 0;
    public static final int kScrollWindowInvalidate= 1;
    public static final int kScrollWindowEraseToPortBackground= 2;
	
	// Region values to pass into GetWindowRegion & GetWindowBounds
	//public static final short kWindowTitleBarRgn            = 0;
	//public static final short kWindowTitleTextRgn           = 1;
	//public static final short kWindowCloseBoxRgn            = 2;
	//public static final short kWindowZoomBoxRgn             = 3;
	//public static final short kWindowDragRgn                = 5;
	//public static final short kWindowGrowRgn                = 6;
	//public static final short kWindowCollapseBoxRgn         = 7;
	//public static final short kWindowTitleProxyIconRgn      = 8;
	public static final short kWindowStructureRgn           = 32;
	public static final short kWindowContentRgn             = 33;   /* Content area of the window; empty when the window is collapsed*/
	//public static final short kWindowUpdateRgn              = 34;   /* Carbon forward*/
	//public static final short kWindowOpaqueRgn              = 35;   /* Mac OS X: Area of window considered to be opaque. Only valid for windows with alpha channels.*/
	//public static final short kWindowGlobalPortRgn          = 40;    /* Carbon forward - bounds of the windowÕs port in global coordinates; not affected by CollapseWindow*/

		
	public static native int CreateNewWindow(int windowClass, int attributes, short[] bounds, int[] wHandle);
	public static native int GetWindowPort(int wHandle);
	public static native void BeginUpdate(int wHandle);
	public static native void EndUpdate(int wHandle);
	public static native void DrawControls(int wHandle);
	public static native void UpdateControls(int wHandle, int rgnHandle);
	//public static native void DrawGrowIcon(int wHandle);
	public static native void SetPortWindowPort(int wHandle);
	public static native int FrontWindow();
	public static native int FrontNonFloatingWindow();
	public static native void SelectWindow(int wHandle);
	public static native void ActivateWindow(int wHandle, boolean activate);
	public static native void BringToFront(int wHandle);
	public static native short FindWindow(short[] where, int[] wHandle);
	//public static native boolean ResizeWindow(int wHandle, short[] startPt, short[] sizeConstraints, short[] newContentRect);
	//public static native void DragWindow(int wHandle, short[] startPt, short[] boundsRect);
	//public static native void GetWindowPortBounds(int wHandle, short[] bounds);
	//public static native boolean TrackGoAway(int wHandle, short[] startPt);
	//public static native boolean TrackBox(int wHandle, short[] startPt, short part);
	//public static native void ZoomWindow(int wHandle, short part, boolean toFront);
	public static native void DisposeWindow(int wHandle);
	public static native void InvalWindowRect(int wHandle, short[] bounds);
	public static native void InvalWindowRgn(int wHandle, int rgnHandle);
	public static native void ShowWindow(int wHandle);
	public static native void HideWindow(int wHandle);
	public static native int ShowSheetWindow(int wHandle, int parenthandle);
	public static native int HideSheetWindow(int wHandle);
	public static native void SetWindowBounds(int wHandle, short windowRegion, short[] bounds);
	public static native void GetWindowBounds(int wHandle, short windowRegion, short[] bounds);
	public static native boolean IsValidWindowPtr(int grafPort);
	public static native int GetWRefCon(int wHandle);
	public static native void SetWRefCon(int wHandle, int data);
	public static native void SizeWindow(int wHandle, short w, short h, boolean update);
	public static native void MoveWindow(int wHandle, short h, short v, boolean toFront);
	public static native void ScrollWindowRect(int wHandle, short[] rect, short dx, short dy, int options, int exposedRgn);
	public static native int CopyWindowTitleAsCFString(int wHandle, int[] sHandle);
	public static native int SetWindowTitleWithCFString(int wHandle, int sHandle);
	public static native boolean IsWindowVisible(int wHandle);
	public static native int SetWindowDefaultButton(int wHandle, int cHandle);
	public static native int GetWindowDefaultButton(int wHandle, int[] cHandle);
	public static native int GetWindowModality(int wHandle, int[] modalityKind, int[] unavailableWindowHandle);
	public static native int SetWindowModality(int wHandle, int modalityKind, int unavailableWindowHandle);
	public static native int CollapseWindow(int wHandle, boolean collapse);
	public static native boolean IsWindowActive(int wHandle);
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Menu Manager
	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	
    public static final int kMenuItemAttrDisabled = 1;
    public static final int kMenuItemAttrSeparator = 64;
	
	// menu item mark characters
	public static final char checkMark= (char)18;	// for SWT.CHECK
	public static final char diamondMark= (char)19;	// for SWT.RADIO
	
	// menu glyphs
	public static final short kMenuNullGlyph = 0;
	public static final short kMenuTabRightGlyph = 2;
	public static final short kMenuTabLeftGlyph = 3;
	public static final short kMenuEnterGlyph = 4;
	public static final short kMenuShiftGlyph = 5;
	public static final short kMenuControlGlyph = 6;
	public static final short kMenuOptionGlyph = 7;
	public static final short kMenuSpaceGlyph = 9;
	public static final short kMenuDeleteRightGlyph = 10;
	public static final short kMenuReturnGlyph = 11;
	public static final short kMenuReturnR2LGlyph = 12;
	public static final short kMenuNonmarkingReturnGlyph = 13;
	public static final short kMenuPencilGlyph = 15;
	public static final short kMenuDownwardArrowDashedGlyph = 16;
	public static final short kMenuCommandGlyph = 17;
	public static final short kMenuCheckmarkGlyph = 18;
	public static final short kMenuDiamondGlyph = 19;
	public static final short kMenuAppleLogoFilledGlyph = 20;
	public static final short kMenuParagraphKoreanGlyph = 21;
	public static final short kMenuDeleteLeftGlyph = 23;
	public static final short kMenuLeftArrowDashedGlyph = 24;
	public static final short kMenuUpArrowDashedGlyph = 25;
	public static final short kMenuRightArrowDashedGlyph = 26;
	public static final short kMenuEscapeGlyph = 27;
	public static final short kMenuClearGlyph = 28;
	public static final short kMenuLeftDoubleQuotesJapaneseGlyph = 29;
	public static final short kMenuRightDoubleQuotesJapaneseGlyph = 30;
	public static final short kMenuTrademarkJapaneseGlyph = 31;
	public static final short kMenuBlankGlyph = 97;
	public static final short kMenuPageUpGlyph = 98;
	public static final short kMenuCapsLockGlyph = 99;
	public static final short kMenuLeftArrowGlyph = 100;
	public static final short kMenuRightArrowGlyph = 101;
	public static final short kMenuNorthwestArrowGlyph = 102;
	public static final short kMenuHelpGlyph = 103;
	public static final short kMenuUpArrowGlyph = 104;
	public static final short kMenuSoutheastArrowGlyph = 105;
	public static final short kMenuDownArrowGlyph = 106;
	public static final short kMenuPageDownGlyph = 107;
	public static final short kMenuAppleLogoOutlineGlyph = 108;
	public static final short kMenuContextualMenuGlyph = 109;
	public static final short kMenuPowerGlyph = 110;
	public static final short kMenuF1Glyph = 111;
	public static final short kMenuF2Glyph = 112;
	public static final short kMenuF3Glyph = 113;
	public static final short kMenuF4Glyph = 114;
	public static final short kMenuF5Glyph = 115;
	public static final short kMenuF6Glyph = 116;
	public static final short kMenuF7Glyph = 117;
	public static final short kMenuF8Glyph = 118;
	public static final short kMenuF9Glyph = 119;
	public static final short kMenuF10Glyph = 120;
	public static final short kMenuF11Glyph = 121;
	public static final short kMenuF12Glyph = 122;
	public static final short kMenuF13Glyph = 135;
	public static final short kMenuF14Glyph = 136;
	public static final short kMenuF15Glyph = 137;
	public static final short kMenuControlISOGlyph = 138;

	// menu event types
	public static final int kEventMenuOpening=	4;
	public static final int kEventMenuClosed=	5;
	public static final int kEventMenuPopulate=	9;
	
	// For use with Get/SetMenuItemModifiers
	public static final byte kMenuNoModifiers		= 0;		/* Mask for no modifiers*/
	public static final byte kMenuShiftModifier		= (1 << 0); /* Mask for shift key modifier*/
	public static final byte kMenuOptionModifier	= (1 << 1); /* Mask for option key modifier*/
	public static final byte kMenuControlModifier	= (1 << 2); /* Mask for control key modifier*/
	public static final byte kMenuNoCommandModifier	= (1 << 3); /* Mask for no command key modifier*/

	public static native int MenuSelect(short[] where);
	public static native void HiliteMenu(short menuID);
	public static native void DrawMenuBar();
	public static native void InvalMenuBar();

	public static native int CreateNewMenu(int menuID, int menuAttributes, int[] menuRef);
	public static native void DisposeMenu(int mHandle);
	public static native int NewMenuCallbackUPP(Object target, String method);
	public static native int InitContextualMenus();

	public static native void InsertMenu(int mHandle, short beforeID);
	public static native void DeleteMenu(short menuID);
	public static native void ClearMenuBar();
	
	public static native short CountMenuItems(int mHandle);
	public static native int DeleteMenuItems(int mHandle, short firstItem, int numItems);

	public static native int GetMenuItemRefCon(int mHandle, short index, int[] refCon);
	public static native int SetMenuItemRefCon(int mHandle, short index, int refCon);
	public static native int SetMenuItemCommandKey(int mHandle, short index, boolean virtualKey, char key);
	public static native int SetMenuItemModifiers(int mHandle, short index, byte modifiers);
	public static native int SetMenuItemKeyGlyph(int mHandle, short index, short glyph);
	public static native int InvalidateMenuItems(int mHandle, short index, int numItems);

	public static native int AppendMenuItemTextWithCFString(int mHandle, int sHandle, int attributes, int commandID, short[] outItemIndex);
	public static native int InsertMenuItemTextWithCFString(int mHandle, int sHandle, short index, int attributes, int commandID);
	public static native int SetMenuItemTextWithCFString(int mHandle, short index, int sHandle);
	public static native int CopyMenuItemTextAsCFString(int mHandle, short index, int[] sHandle);

	//public static native int SetMenuItemCommandID(int mHandle, short index, int commandId);
	public static native void EnableMenuCommand(int mHandle, int commandId);
	public static native void DisableMenuCommand(int mHandle, int commandId);
	public static native boolean IsMenuCommandEnabled (int mHandle, int commandId);
	public static native int GetIndMenuItemWithCommandID(int mHandle, int commandId, int index, int[] outMenu, short[] outIndex);
	public static native void DeleteMenuItem(int mHandle, short index);
	public static native int GetMenuItemCommandID(int mHandle, short index, int[] outCommandID);
	public static native short GetMenuID(int mHandle);
	public static native int GetMenuHandle(short menuID);
	public static native int PopUpMenuSelect(int mHandle, short top, short left, short popUpItem);
	public static native int SetRootMenu(int mHandle);
	public static native int RetainMenu(int mHandle);
	public static native int ReleaseMenu(int mHandle);
	public static native int SetMenuTitleWithCFString(int mHandle, int sHandle);
	public static native int SetMenuItemHierarchicalMenu(int mHandle, short index, int hierMenuHandle);
	public static native int GetMenuItemHierarchicalMenu(int mHandle, short index, int[] outHierMenuHandle);
	//public static native void InsertMenuItem(int mHandle, byte[] text, short index);
	//public static native void AppendMenu(int mHandle, byte[] text);
	public static native int ChangeMenuItemAttributes(int mHandle, short index, int setAttributes, int clearAttributes);
	public static native void CheckMenuItem(int mHandle, short index, boolean checked);	
	public static native int GetMenuCommandMark(int mHandle, int commandId, char[] outMark);
	public static native int SetMenuCommandMark(int mHandle, int commandId, char mark);
	public static native boolean IsValidMenu(int mHandle);
	public static native void SetMenuID(int mHandle, short id);	
	public static native boolean IsMenuItemEnabled(int mHandle, short index);
	public static native void DisableMenuItem(int mHandle, short index);
	public static native void EnableMenuItem(int mHandle, short index);
	public static native int SetMenuFont(int mHandle, short fontID, short size);
	public static native int GetMenuFont(int mHandle, short[] fontID, short[] size);
	public static native short GetMenuWidth(int mHandle);
	public static native void CalcMenuSize(int mHandle);
	public static native int SetMenuItemIconHandle(int mHandle, short item, byte iconType, int iconHandle);
	public static native int SetMenuItemCommandID(int mHandle, short item, int commandID);

	/////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Control Manager
	/////////////////////////////////////////////////////////////////////////////////////////////////////////

	// err codes
	public static final int errCouldntSetFocus            	= -30585;
	public static final int errControlIsNotEmbedder			= -30590;
	public static final int errCantEmbedRoot				= -30595;

	// control proc IDs
	public static final short kControlBevelButtonSmallBevelProc= 32;
	public static final short kControlBevelButtonNormalBevelProc= 33;
	public static final short kControlBevelButtonLargeBevelProc= 34;
	public static final short kControlSliderProc            = 48;
	public static final short kControlProgressBarProc       = 80;
	public static final short kControlTabSmallProc			= 129;
	public static final short kControlSeparatorLineProc		= 144;
	public static final short kControlGroupBoxTextTitleProc = 160;
	public static final short kControlPopupArrowEastProc	= 192;
	public static final short kControlUserPaneProc			= 256;
	public static final short kControlEditTextProc			= 272;
	public static final short kControlStaticTextProc        = 288;
	public static final short kControlListBoxProc   		= 352;
	public static final short kControlListBoxAutoSizeProc   = 353;
	public static final short kControlPushButtonProc   		= 368;
	public static final short kControlCheckBoxProc   		= 369;
	public static final short kControlRadioButtonProc   	= 370;
	public static final short kControlCheckBoxAutoToggleProc= 371;
	public static final short kControlRadioButtonAutoToggleProc= 372;
	public static final short kControlPushButLeftIconProc   = 374;
	public static final short kControlScrollBarLiveProc     = 386;
	public static final short kControlPopupButtonProc       = 400;
	public static final short kControlEditUnicodeTextProc   = 912;
	public static final short popupMenuProc                 = 1008;

	// meta part codes for GetControlRegion etc.
	public static final short kControlEntireControl			= 0;
	public static final short kControlStructureMetaPart     = (short) -1;
	public static final short kControlContentMetaPart       = (short) -2;

	// part codes
	public static final short inDesk		= 0;
	public static final short inNoWindow	= 0;
	public static final short inMenuBar		= 1;
	public static final short inSysWindow	= 2;
	public static final short inContent		= 3;
	public static final short inDrag		= 4;
	public static final short inGrow		= 5;
	public static final short inGoAway		= 6;
	public static final short inZoomIn 		= 7;
	public static final short inZoomOut		= 8;
	public static final short inCollapseBox	= 11;
	public static final short inProxyIcon		= 12;
	public static final short inToolbarButton	= 13;
	public static final short inStructure		= 15;
	
	// other part codes
	public static final short kControlUpButtonPart	= 20;
	public static final short kControlDownButtonPart= 21;
	public static final short kControlPageUpPart	= 22;
	public static final short kControlPageDownPart	= 23;
	public static final short kControlIndicatorPart	= 129;
	public static final short thumbDrag				= 999;
	

	// BevelButton control types
	public static final short kControlBehaviorPushbutton    = 0;
	public static final short kControlBehaviorToggles       = 0x0100;
	public static final short kControlBehaviorSticky        = 0x0200;
	public static final short kControlBehaviorSingleValueMenu = 0;
	public static final short kControlBehaviorCommandMenu   = 0x2000; /* menu holds commands, not choices. Overrides multi-value bit.*/
	public static final short kControlBehaviorMultiValueMenu = 0x4000; /* only makes sense when a menu is attached.*/
	public static final short kControlBehaviorOffsetContents = (short) 0x8000;

	public static final short kControlBevelButtonMenuOnBottom = 0;
	public static final short kControlBevelButtonMenuOnRight = (1 << 2);

	
	// control event types
	public static final int kEventControlBoundsChanged	= 154;
	public static native void SetControlAction(int cHandle, int actionProc);

	public static native int NewControlCallbackUPP(Object target, String method);
	public static native int NewControl(int windowHandle, boolean initiallyVisible, short initial, short min, short max, short procID);
	public static native void DisposeControl(int cHandle);
	
	//public static native int CreatePushButtonWithIconControl(int wHandle, int ciconHandle, int sHandle, int[] outControl);
	
	public static native int GetRootControl(int windowHandle, int[] cHandle);
	public static native int CreateRootControl(int windowHandle, int[] cHandle);
	public static native int EmbedControl(int cHandle, int parentControlHandle);
	public static native int CountSubControls(int cHandle, short[] count);
	public static native int GetIndexedSubControl(int cHandle, short index, int[] outHandle);
	public static native int GetSuperControl(int cHandle, int[] parentHandle);

	public static native int GetControlOwner(int cHandle);
	//public static native int FindControlUnderMouse(short[] where, int windowHandle, short[] cpart);
	public static native short TestControl(int cHandle, short[] where);
	public static native short HandleControlClick(int cHandle, short[] where, int modifiers, int actionUPP);
	public static native void MoveControl(int cHandle, short x, short y);
	public static native void SizeControl(int cHandle, short w, short h);
	public static native void ShowControl(int cHandle);
	public static native void HideControl(int cHandle);
	public static native boolean IsValidControlHandle(int cHandle);
	public static native void SetControlReference(int cHandle, int data);
	public static native int GetControlReference(int cHandle);
	public static native int SetControlTitleWithCFString(int cHandle, int sHandle);
	public static native int GetControlTitleAsCFString(int cHandle, int[] sHandle);
	//public static native int setControlToolTipText(int cHandle, short[] bounds, int sHandle);
	public static native void GetControlBounds(int cHandle, short[] bounds);
	public static native void SetControlBounds(int cHandle, short[] bounds);
	public static native int CreateUserPaneControl(int windowHandle, short[] bounds, int features, int[] cHandle);
	public static native boolean IsControlVisible(int cHandle);
	public static native int SetControlVisibility(int cHandle, boolean inIsVisible, boolean inDoDraw);
	public static native boolean IsControlActive(int cHandle);
	public static native int EnableControl(int cHandle);
	public static native int DisableControl(int cHandle);
	public static native boolean IsControlEnabled(int cHandle);
	public static native int GetControl32BitMinimum(int cHandle);
	public static native void SetControl32BitMinimum(int cHandle, int minimum);
	public static native void SetControlMinimum(int cHandle, short minimum);
	public static native int GetControl32BitMaximum(int cHandle);
	public static native void SetControl32BitMaximum(int cHandle, int maximum);
	public static native int GetControl32BitValue(int cHandle);
	public static native short GetControlValue(int cHandle);
	public static native void SetControl32BitValue(int cHandle, int value);
	public static native int GetControlViewSize(int cHandle);
	public static native void SetControlViewSize(int cHandle, int viewSize);
	//public static native void IdleControls(int wHandle);
	public static native int GetBestControlRect(int cHandle, short[] outRect, short[] outBaseLineOffset);
	public static native int GetControlKind(int cHandle, int[] outControlKind);
	public static native int GetControlData(int cHandle, short part, int tag, short[] data);
	public static native int GetControlData(int cHandle, short part, int tag, int[] data);
	public static native int SetControlData(int cHandle, short part, int tag, int data);
	public static native int SetControlData(int cHandle, short part, int tag, short[] data);
	public static native int NewControlActionUPP(Object target, String method);
	public static native int NewControlUserPaneDrawUPP(Object target, String method);
	public static native int NewUserPaneHitTestUPP(Object target, String method);
	public static native short HandleControlKey(int cHandle, short keyCode, char charCode, int modifiers);
	public static native int SetControlFontStyle(int cHandle, short font, short size, short style);
	public static native int SetUpControlBackground(int cHandle, short depth, boolean isColorDevice);

	public static native int GetControlRegion(int cHandle, short inPart, int rgnHandle);
	
	public static short kControlContentCIconHandle= 130;

	public static final int kControlBevelButtonOwnedMenuRefTag = ('o'<<24) + ('m'<<16) + ('r'<<8) + 'f'; /* MenuRef (control will dispose)*/
	public static final int kControlBevelButtonCenterPopupGlyphTag = ('p'<<24) + ('g'<<16) + ('l'<<8) + 'c'; /* Boolean: true = center, false = bottom right*/

	public static native int SetBevelButtonContentInfo(int cHandle, short controlContentType, int controlContentHandle);	

	
	// Slider variants
	public static final short kControlSliderLiveFeedback	= (1 << 0);
	public static final short kControlSliderHasTickMarks	= (1 << 1);
	public static final short kControlSliderReverseDirection	= (1 << 2);
	public static final short kControlSliderNonDirectional	= (1 << 3);

	// Data Browser
	public static final int kDataBrowserNoItem= 0;
	public static final int kDataBrowserDefaultPropertyFlags = 0;
	public static final int kDataBrowserTextType= ('t'<<24) + ('e'<<16) + ('x'<<8) + 't';	/* CFStringRef */
	public static final int kDataBrowserItemNoProperty= 0;	/* The anti-property (no associated data) */
	public static final int kDataBrowserListViewLatestHeaderDesc = 0;
	
	public static final int kDataBrowserDragSelect        = 1 << 0;
	public static final int kDataBrowserSelectOnlyOne     = 1 << 1;
	public static final int kDataBrowserResetSelection    = 1 << 2;
	public static final int kDataBrowserCmdTogglesSelection = 1 << 3;
	public static final int kDataBrowserNoDisjointSelection = 1 << 4;
	public static final int kDataBrowserAlwaysExtendSelection = 1 << 5;
	public static final int kDataBrowserNeverEmptySelectionSet = 1 << 6;

	public static final int kDataBrowserViewSpecificFlagsOffset = 16;
	public static final int kDataBrowserListViewSelectionColumn= 1 << kDataBrowserViewSpecificFlagsOffset;
 
 	// data browser item states
 	public static final int kDataBrowserItemIsSelected = 1 << 0;
 
	public static native int newColumnDesc(int propertyID, int propertyType, int propertyFlags,
			short minimumWidth, short maximumWidth);
			
	public static native int AddDataBrowserListViewColumn(int cHandle, int handle, int index);
	
	public static native int createDataBrowserControl(int wHandle);
	
	public static native int NewDataBrowserDataCallbackUPP(Object target, String method);
	public static native void setDataBrowserItemDataCallback(int cHandle, int dataBrowserDataCallbackUPP);

	public static native int NewDataBrowserItemNotificationCallbackUPP(Object target, String method);
	public static native void setDataBrowserItemNotificationCallback(int cHandle, int dataBrowserItemNotificationCallbackUPP);
	
	public static native int SetDataBrowserActiveItems(int cHandle, boolean active);
	public static native int AddDataBrowserItems(int cHandle, int containerID, int numItems, int[] itemIDs, int preSortProperty);
	public static native int RemoveDataBrowserItems(int cHandle, int containerID, int numItems, int[] itemIDs, int preSortProperty);
	public static native int SetDataBrowserItemDataText(int itemID, int sHandle);
	public static native int SetDataBrowserHasScrollBars(int cHandle, boolean hScroll, boolean vScroll);
	public static native int SetDataBrowserListViewHeaderBtnHeight(int cHandle, short height);
	public static native int UpdateDataBrowserItems(int cHandle, int container, int numItems, int[] items, int preSortProperty, int propertyID);
	public static native int GetDataBrowserItemCount(int cHandle, int container, boolean recurse, int state, int[] numItems);
	public static native int GetDataBrowserItems(int cHandle, int container, boolean recurse, int state, int handle);

	/* Set operations for use with SetDataBrowserSelectedItems */
	public static final int kDataBrowserItemsAdd          = 0;    /* add specified items to existing set */
	public static final int kDataBrowserItemsAssign       = 1;    /* assign destination set to specified items */
	public static final int kDataBrowserItemsToggle       = 2;    /* toggle membership state of specified items */
	public static final int kDataBrowserItemsRemove       = 3;	  /* remove specified items from existing set */
	
	public static native int SetDataBrowserSelectionFlags(int cHandle, int selectionFlags);
	public static native int SetDataBrowserSelectedItems(int cHandle, int numItems, int[] items, int operation);
	
	//---- User Pane
	
	// feature bits
	//public static final int kControlSupportsGhosting      = 1 << 0;
	public static final int kControlSupportsEmbedding     = 1 << 1;
	public static final int kControlSupportsFocus         = 1 << 2;
	//public static final int kControlWantsIdle             = 1 << 3;
	//public static final int kControlWantsActivate         = 1 << 4;
	//public static final int kControlHandlesTracking       = 1 << 5;
	//public static final int kControlSupportsDataAccess    = 1 << 6;
	//public static final int kControlHasSpecialBackground  = 1 << 7;
	public static final int kControlGetsFocusOnClick      = 1 << 8;
	//public static final int kControlSupportsCalcBestRect  = 1 << 9;
	//public static final int kControlSupportsLiveFeedback  = 1 << 10;
	//public static final int kControlHasRadioBehavior      = 1 << 11;
	//public static final int kControlSupportsDragAndDrop   = 1 << 12;
	//public static final int kControlAutoToggles           = 1 << 14;
	//public static final int kControlSupportsGetRegion     = 1 << 17;
	//public static final int kControlSupportsFlattening    = 1 << 19;
	//public static final int kControlSupportsSetCursor     = 1 << 20;
	//public static final int kControlSupportsContextualMenus = 1 << 21;
	//public static final int kControlSupportsClickActivation = 1 << 22;
	//public static final int kControlIdlesWithTimer        = 1 << 23;
	
	public static final int kControlUserPaneDrawProcTag= ('d'<<24) + ('r'<<16) + ('a'<<8) + 'w';
	public static final int kControlUserPaneHitTestProcTag= ('h'<<24) + ('i'<<16) + ('t'<<8) + 't';
	
	// StaticText
	public static final int kControlStaticTextCFStringTag= ('c'<<24) + ('f'<<16) + ('s'<<8) + 't';
	
	// TextEdit
	public static final int kControlEditTextTextTag= ('t'<<24) + ('e'<<16) + ('x'<<8) + 't';
	public static final int kControlEditTextSelectionTag= ('s'<<24) + ('e'<<16) + ('l'<<8) + 'e';
	public static final int kControlEditTextCFStringTag= ('c'<<24) + ('f'<<16) + ('s'<<8) + 't';
	public static final int kControlEditTextLockedTag= ('l'<<24) + ('o'<<16) + ('c'<<8) + 'k';
	
	/*
	public static native int CreateEditUnicodeTextControl(int wHandle, short[] bounds, int sHandle,
		boolean isPassword, int styleHandle, int[] outControl);
	*/
	
	///// MLTE Text
	public static final int kTXNWantHScrollBarMask        = 1 << 2;
	public static final int kTXNWantVScrollBarMask        = 1 << 3;
	public static final int kTXNReadOnlyMask              = 1 << 5;
	public static final int kTXNAlwaysWrapAtViewEdgeMask  = 1 << 11;
	public static final int kTXNDontDrawCaretWhenInactiveMask = 1 << 12;
	public static final int kTXNSingleLineOnlyMask        = 1 << 14;

	public static final int kTXNTextEditStyleFrameType    = 1;
	
	public static final int kTXNUnicodeTextFile           = ('u'<<24) + ('t'<<16) + ('x'<<8) + 't';
	
	public static final int kTXNSystemDefaultEncoding     = 0;
	
	public static final int kTXNUnicodeTextData           = ('u'<<24) + ('t'<<16) + ('x'<<8) + 't';
	
	public static final int kTXNWordWrapStateTag          = ('w'<<24) + ('w'<<16) + ('r'<<8) + 's';
	public static final int kTXNTabSettingsTag            = ('t'<<24) + ('a'<<16) + ('b'<<8) + 's';
	public static final int kTXNDoFontSubstitution        = ('f'<<24) + ('s'<<16) + ('u'<<8) + 'b';
	
	/* kTXNWordWrapStateTag */
	public static final boolean kTXNAutoWrap                  = false;
	public static final boolean kTXNNoAutoWrap                = true;
	
	/* TXNScrollBarState */
	public static final boolean kScrollBarsAlwaysActive       = true;
	public static final boolean kScrollBarsSyncWithFocus      = false;

	// Offsets
    public static final int kTXNUseCurrentSelection = -1;
    public static final int kTXNStartOffset = 0;
    public static final int kTXNEndOffset = 2147483647;
	
		
	public static native int TXNInitTextension();
	public static native int TXNNewObject(int fileSpec, int wHandle, short[] bounds, int frameOptions,
		int frameType, int fileType, int iPermanentEncoding, int[] handle, int[] frameID, int refcon);
	public static native void TXNDeleteObject(int txHandle);
	public static native void TXNSetFrameBounds(int txHandle, int top, int left, int bottom, int right, int frameID);
	public static native void TXNDraw(int txHandle, int gDevice);
	public static native int TXNGetData(int txHandle, int startOffset, int endOffset, int[] dataHandle);
	public static native int TXNSetData(int txHandle, char[] data, int startOffset, int endOffset);
	public static native int TXNGetLineCount(int txHandle, int[] lineTotal);
	public static native int TXNDataSize(int txHandle);
	public static native void TXNGetSelection(int txHandle, int[] startOffset, int[] endOffset);
	public static native int TXNSetSelection(int txHandle, int startOffset, int endOffset);
	public static native void TXNSelectAll(int txHandle);
	public static native void TXNShowSelection(int txHandle, boolean showEnd);
	public static native void TXNKeyDown(int txHandle, int[] eventData);
	public static native void TXNClick(int txHandle, int[] eventData);
	public static native void TXNFocus(int txHandle, boolean becomingFocused);
	public static native int TXNCut(int txHandle);
	public static native int TXNCopy(int txHandle);
	public static native int TXNPaste(int txHandle);
	public static native int TXNGetRectBounds(int txHandle, short[] viewRect, int[] destinationRect, int[] textRect);
	public static native void TXNSetRectBounds(int txHandle, short[] viewRect, int[] destRect, boolean update);
	public static native int TXNActivate(int txHandle, int frameID, boolean scrollBarState);
	public static native int TXNEchoMode(int txHandle, char echoCharacter, int encoding, boolean on);
	public static native int TXNOffsetToPoint(int txHandle, int offset, short[] point);
	public static native void TXNResizeFrame(int txHandle, int width, int height, int frameID);
	public static native void TXNGetViewRect(int txHandle, short[] viewRect);
	public static native int TXNGetLineMetrics(int txHandle, int lineNumber, int[] lineWidth, int[] lineHeight);
	public static native void TXNForceUpdate(int txHandle);
	public static native int TXNSetTXNObjectControls(int txHandle, boolean clearAll, int controlCount, int[] controlTags, int[] controlData);
	//public static native int TXNSetBackground(int txHandle, TXNBackground *iBackgroundInfo);

	// TabFolder
	public static final int kControlTabInfoTag= ('t'<<24) + ('a'<<16) + ('b'<<8) + 'i';	/* ControlTabInfoRec*/
	public static final int kControlTabContentRectTag= ('r'<<24) + ('e'<<16) + ('c'<<8) + 't';	/* Rect*/

	public static native int CreateTabFolderControl(int wHandle, int[] cHandle);
	public static native int setTabText(int cHandle, int index, int sHandle);
	public static native int setTabIcon(int cHandle, int index, int iconHandle);
	
	// Popup menus
	/* 
	public static final int kControlPopupButtonMenuRefTag = OSType("mhan"); // MenuRef
	public static final int kControlPopupButtonExtraHeightTag = OSType("exht"); // SInt16 - extra vertical whitespace within the button
	public static final int kControlPopupButtonOwnedMenuRefTag = OSType("omrf"); // MenuRef
	public static final int kControlPopupButtonCheckCurrentTag = OSType("chck"); // Boolean - whether the popup puts a checkmark next to the current item (defaults to true)
	public static native int CreatePopupButtonControl(int wHandle, short[] bounds, int sHandle, short menuID,
			boolean variableWidth, short titleWidth, short titleJustification, byte titleStyle, int[] outControl);
	*/
	public static native void SetControlPopupMenuHandle(int cHandle, int popupMenuHandle);
	
	//---- Alerts and Dialogs
	// Alert types
    public static final short kAlertStopAlert = 0;
    public static final short kAlertNoteAlert = 1;
    public static final short kAlertCautionAlert = 2;
    public static final short kAlertPlainAlert = 3;
	
	public static native int CreateStandardAlert(short alertType, int errorSHandle, int explanationSHandle,
				int alertParamHandle, int[] dialogHandle);

	public static native int RunStandardAlert(int dialogHandle, int modalFilterUPP, short[] itemHit);
	
	public static native int PickColor(short[] rgb, short[] where, byte[] title, boolean[] success);
	
	// File dialog
	public static final int kNavAllowMultipleFiles= 0x00000080; /* allow multiple items to be selected */
  
	public static final int kNavUserActionNone = 0;
	public static final int kNavUserActionCancel = 1;	/* The user cancelled the dialog. */
	public static final int kNavUserActionOpen = 2;	/* Open button in the GetFile dialog. */
	public static final int kNavUserActionSaveAs = 3; /* Save button in the PutFile dialog. */
	public static final int kNavUserActionChoose = 4;	/* Choose button in the ChooseFile, ChooseFolder, ChooseVolume or ChooseObject dialogs.*/
	public static final int kNavUserActionNewFolder = 5; /* New Folder button in the New Folder dialog. */
	public static final int kNavUserActionSaveChanges = 6; /* Save button in an AskSaveChanges dialog. */
	public static final int kNavUserActionDontSaveChanges = 7; /* Don't Save button in an AskSaveChanges dialog. */
  	public static final int kNavUserActionDiscardChanges = 8; /* Discard button in the AskDiscardChanges dialog. */
  	public static final int kNavUserActionReviewDocuments = 9; /* Review Unsaved button in the AskReviewDocuments dialog (Mac OS X only). */
  	public static final int kNavUserActionDiscardDocuments = 10; /* The user clicked the Discard Changes button in the AskReviewDocuments dialog (Mac OS X only). */
		
	public static native int NavCreateGetFileDialog(int options, int titleHandle, int parentHandle, int[] dialogHandle);
	public static native int NavCreatePutFileDialog(int options, int titleHandle, int parentHandle, int[] dialogHandle,
									int fileType, int fileCreator);
	public static native int NavCreateChooseFolderDialog(int options, int windowTitle, int messageHandle,
									int parentWindowHandle, int[] dialogHandle);
								
	public static native int NavDialogSetSaveFileName(int dialogHandle, int fileNameHandle);
	public static native int NavDialogGetSaveFileName(int dialogHandle);

	public static native int NavDialogRun(int dialogHandle);
	public static native int NavDialogGetUserAction(int dialogHandle);
	
	public static native int NavDialogGetReply(int dialogHandle, int[] replyHandle);
	public static native int NavReplyRecordGetSelection(int replyHandle);	// returns AEDescList
	public static native void NavDialogDisposeReply(int replyHandle);
	
	public static native int AECountItems(int aeDescList, int[] count);
	public static native int AEGetNthPtr(int aeDescList, int oneBasedIndex, int[] sHandle);
	
	public static native int getFiles(int dialogHandle);
	public static native void NavDialogDispose(int dialogHandle);
	
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	// CFStrings
	//////////////////////////////////////////////////////////////////////////////////////////////////
	public static native int CFStringCreateWithCharacters(String s);
	public static native void CFRelease(int sHandle);
	public static native int CFStringGetLength(int sHandle);
	public static native void CFStringGetCharacters(int sHandle, int start, int length, char[] buffer);
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	// Handles
	//////////////////////////////////////////////////////////////////////////////////////////////////
	public static native int NewHandle(int size);
	public static native int NewHandleClear(int size);
	public static native void DisposeHandle(int handle);
	public static native int GetHandleSize(int handle);
	public static native void getHandleData(int handle, char[] data);
	public static native void getHandleData(int handle, int[] data);
	public static native int DerefHandle(int handle);
	
	//////////////////////////////////////////////////////////////////////////////////////////////////
	// Ptrs
	//////////////////////////////////////////////////////////////////////////////////////////////////	
	public static native int NewPtr(int size);
	public static native int NewPtrClear(int size);
	public static native void DisposePtr(int ptr);
	public static native int GetPtrSize(int ptr);
	//public static native int MemError();

	//////////////////////////////////////////////////////////////////////////////////////////////////
	// Unix memory utilities
	//////////////////////////////////////////////////////////////////////////////////////////////////	
	public static native void memcpy(int dest, int src, int n);
	public static native void memcpy(int dest, byte[] src, int n);
	public static native void memcpy(byte[] dest, int src, int n);
	public static native void memset(int dest, int value, int size);

	//////////////////////////////////////////////////////////////////////////////////////////////////
	// Scrap
	//////////////////////////////////////////////////////////////////////////////////////////////////

	public static native int GetCurrentScrap(int[] scrapHandle);
	public static native int GetScrapFlavorCount(int scrapHandle, int[] flavorCount);
	public static native int GetScrapFlavorInfoList(int scrapHandle, int[] flavorCount, int[] info);
	public static native int GetScrapFlavorSize(int scrapHandle, int flavorType, int[] size);
	public static native int GetScrapFlavorData(int scrapHandle, int flavorType, int[] size, byte[] data);
	public static native int PutScrapFlavor(int scrapHandle, int flavorType, int flavorFlags, byte[] data); 
	public static native int ClearCurrentScrap();
	 
	//////////////////////////////////////////////////////////////////////////////////////////////////
	// Misc
	//////////////////////////////////////////////////////////////////////////////////////////////////
	//public static native void ExitToShell();
 	public static native short HiWord(int doubleWord);
	public static native short LoWord(int doubleWord);
	public static native void installQuitHandler(Object target, String method);
	public static native void SysBeep(short duration);
	public static native int GetDblTime();
	public static native int GetCaretTime();
	public static native int GetAvailableWindowPositioningBounds(int gHandle, short[] mainScreenRect);	

}
