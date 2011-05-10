/**********************************************************************
 * Copyright (c) 2003, 2011 IBM Corp.
 * Portions Copyright (c) 1983-2002, Apple Computer, Inc.
 *
 * All rights reserved.  This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 **********************************************************************/
package org.eclipse.swt.internal.carbon;

 
import org.eclipse.swt.internal.*;

public class OS extends C {
	static {
		Library.loadLibrary ("swt-pi");
	}

	public static final int VERSION;
	static {
		int [] response = new int [1];
		OS.Gestalt (OS.gestaltSystemVersion, response);
		VERSION = response [0] & 0xffff;		
	}

	/** Constants */
	public static final int APPL = ('A'<<24) + ('P'<<16) + ('P'<<8) + 'L';
	public static final int RGBDirect = 16;
	public static final int alphaLock = 0x0400;
	public static final int badDragFlavorErr = -1852;
	public static final int bold = 1;
	public static final int cantGetFlavorErr = -1854;
	public static final int checkMark = 18;
	public static final int cmdKey = 1 << 8;
	public static final int controlKey = 1 << 12;
	public static final int CSSM_CERT_X_509v3 = 0x3;
	public static final int diamondMark = 19;
	public static final int dragNotAcceptedErr = -1857;
	public static final int errControlIsNotEmbedder = -30590;
	public static final int errDataBrowserItemNotFound = -4971;
	public static final int errUnknownControl = -30584;
	public static final int eventLoopTimedOutErr = -9875;
	public static final int eventNotHandledErr = -9874;
	public static final int eventParameterNotFoundErr = -9870;
	public static final int gestaltSystemVersion = ('s'<<24) + ('y'<<16) + ('s'<<8) + 'v';
	public static final int inContent = 3;
	public static final int inMenuBar = 1;
	public static final int inStructure = 15;
	public static final int inZoomIn = 7;
	public static final int inZoomOut = 8;
	public static final int inToolbarButton = 13;	
	public static final int italic = 2;
	public static final int k24RGBPixelFormat = 0x00000018;
	public static final int k32ARGBPixelFormat = 0x00000020;
	public static final int kActivateAndHandleClick = 3;
	public static final int kAppearanceEventClass = ('a'<<24) + ('p'<<16) + ('p'<<8) + 'r';
	public static final int kAEAppearanceChanged = ('t'<<24) + ('h'<<16) + ('m'<<8) + 'e';
	public static final int kAESystemFontChanged = ('s'<<24) + ('y'<<16) + ('s'<<8) + 'f';
	public static final int kAESmallSystemFontChanged = ('s'<<24) + ('s'<<16) + ('f'<<8) + 'n';
	public static final int kAEViewsFontChanged = ('v'<<24) + ('f'<<16) + ('n'<<8) + 't';
	public static final int kAEQuitApplication = ('q'<<24) + ('u'<<16) + ('i'<<8) + 't';
	public static final int kAEOpenDocuments = ('o' << 24) + ('d' << 16) + ('o' << 8) + 'c';
	public static final int keyDirectObject = ('-' << 24) + ('-' << 16) + ('-' << 8) + '-';
	public static final int typeAEList = ('l' << 24) + ('i' << 16) + ('s' << 8) + 't';
	public static final int kCoreEventClass = ('a'<<24) + ('e'<<16) + ('v'<<8) + 't';
	public static final int kAlertCautionAlert = 2;
	public static final int kAlertCautionIcon = ('c'<<24) + ('a'<<16) + ('u'<<8) + 't';
	public static final int kAlertNoteAlert = 1;
	public static final int kAlertNoteIcon = ('n'<<24) + ('o'<<16) + ('t'<<8) + 'e';
	public static final int kAlertPlainAlert = 3;
	public static final int kAlertStopAlert = 0;
	public static final int kAlertStopIcon = ('s'<<24) + ('t'<<16) + ('o'<<8) + 'p';
	public static final int kAlertDefaultOKText           = -1;
	public static final int kAlertDefaultCancelText       = -1;
	public static final int kAlertStdAlertOKButton        = 1;
	public static final int kAlertStdAlertCancelButton    = 2;
	public static final int kAlertStdAlertOtherButton     = 3;
	public static final int kAtSpecifiedOrigin = 0;
	public static final int kATSFontContextGlobal = 1;
	public static final int kATSFontContextLocal = 2;
	public static final int kATSOptionFlagsDefault = 0;
	public static final int kATSOptionFlagsDefaultScope = 0x00000000 << 12;
	public static final int kATSFontFormatUnspecified = 0;
	public static final int kATSDeletedGlyphcode = 0xFFFF;
	public static final int kATSLineLastNoJustification = 0x00000020;
	public static final int kATSUAscentTag = 284;
	public static final int kATSUCrossStreamShiftTag = 269;
	public static final int kATSUDescentTag = 285;
	public static final int kATSUDirectDataBaselineDeltaFixedArray = 1;
	public static final int kATSUDirectDataLayoutRecordATSLayoutRecordVersion1 = 100;
	public static final int kATSUDirectDataLayoutRecordATSLayoutRecordCurrent = kATSUDirectDataLayoutRecordATSLayoutRecordVersion1;
	public static final int kATSUFullJustification = 0x40000000;
	public static final int kATSUNoJustification = 0x00000000;
	public static final int kATSUImposeWidthTag = 266;
	public static final int kATSULineAscentTag = 8;
	public static final int kATSULineDescentTag = 9;
	public static final int kATSULineDirectionTag = 3;
	public static final int kATSLineUseDeviceMetrics = 0x01000000;
	public static final int kATSLineKeepSpacesOutOfMargin = 0x00000008;
	public static final int kATSUStartAlignment = 0x00000000;
	public static final int kATSUEndAlignment = 0x40000000;
	public static final int kATSUCenterAlignment = 0x20000000;
	public static final int kATSUByCharacter = 0;
	public static final int kATSUByTypographicCluster = 1;
	public static final int kATSUByWord = 2;
	public static final int kATSUByCharacterCluster = 3;
	public static final int kATSUColorTag = 263;
	public static final int kATSUCGContextTag = 32767;
	public static final int kATSUFontTag = 261;
	public static final int kATSULineFlushFactorTag = 5;
	public static final int kATSULineHighlightCGColorTag = 17;
	public static final int kATSULineJustificationFactorTag = 4;
	public static final int kATSULineLayoutOptionsTag = 7;
	public static final int kATSULineWidthTag = 1;
	public static final int kATSULeftToRightBaseDirection = 0;
	public static final int kATSURightToLeftBaseDirection = 1;
	public static final int kATSURGBAlphaColorTag = 288;
	public static final int kATSUQDBoldfaceTag = 256;
	public static final int kATSUQDItalicTag = 257;
	public static final int kATSUQDUnderlineTag = 258;
	public static final int kATSUStyleStrikeThroughTag = 292;
	public static final int kATSUStyleUnderlineColorOptionTag = 291;
	public static final int kATSUStyleUnderlineCountOptionTag = 290;
	public static final int kATSUStyleStrikeThroughColorOptionTag = 294;
	public static final int kATSUStyleSingleLineCount = 1;
	public static final int kATSUStyleDoubleLineCount = 2;
	public static final int kATSULeftTab = 0;
	public static final int kATSUseDeviceOrigins = 1;
	public static final int kATSUseLineHeight = 0x7FFFFFFF;
	public static final int kATSUSizeTag = 262;
	public static final int kATSUToTextEnd = 0xFFFFFFFF;
	public static final int kCFRunLoopBeforeWaiting = 1 << 5;
	public static final int kCFRunLoopAfterWaiting = 1 << 6;
	public static final int kCFRunLoopRunFinished = 1;
	public static final int kCFRunLoopRunStopped = 2;
	public static final int kCFRunLoopRunTimedOut = 3;
	public static final int kCFRunLoopRunHandledSource = 4;
	public static final int kAvailBoundsChangedForDock = 1 << 0;
	public static final int kCGBlendModeNormal = 0;
	public static final int kCGBlendModeDifference = 10;
	public static final int kCGBitmapByteOrderDefault = 0 << 12;
	public static final int kCGBitmapByteOrder16Little = 1 << 12;
	public static final int kCGBitmapByteOrder32Little = 2 << 12;
	public static final int kCGBitmapByteOrder16Big = 3 << 12;
	public static final int kCGBitmapByteOrder32Big = 4 << 12;
	public static final int kCGBitmapByteOrder16Host = __BIG_ENDIAN__() ? kCGBitmapByteOrder16Big : kCGBitmapByteOrder16Little;
	public static final int kCGBitmapByteOrder32Host = __BIG_ENDIAN__() ? kCGBitmapByteOrder32Big : kCGBitmapByteOrder32Little;
	public static final int kCFAllocatorDefault = 0;
	public static final int kCFNumberFormatterDecimalStyle = 1;
	public static final int kCFURLPOSIXPathStyle = 0;
	public static final int kCFStringEncodingASCII = 0x0600;
	public static final int kCFStringEncodingMacRoman = 0;
	public static final int kCFStringEncodingUTF8 = 0x08000100;
	public static final int kCFStringEncodingUnicode = 0x0100;
	public static final int kCGEncodingMacRoman = 1;
	public static final int kCGImageAlphaNone = 0;
	public static final int kCGImageAlphaPremultipliedLast = 1;
	public static final int kCGImageAlphaPremultipliedFirst = 2;
	public static final int kCGImageAlphaLast = 3;
	public static final int kCGImageAlphaFirst = 4;
	public static final int kCGImageAlphaNoneSkipLast = 5;
	public static final int kCGImageAlphaNoneSkipFirst = 6;
	public static final int kCGInterpolationDefault = 0;
	public static final int kCGInterpolationNone = 1;
	public static final int kCGInterpolationLow = 2;
	public static final int kCGInterpolationHigh = 3;
	public static final int kCGLineCapButt = 0;
	public static final int kCGLineCapRound = 1;
	public static final int kCGLineCapSquare = 2;
	public static final int kCGLineJoinMiter = 0;
	public static final int kCGLineJoinRound = 1;
	public static final int kCGLineJoinBevel = 2;
	public static final int kCGPathElementMoveToPoint = 0;
	public static final int kCGPathElementAddLineToPoint = 1;
	public static final int kCGPathElementAddQuadCurveToPoint = 2;
	public static final int kCGPathElementAddCurveToPoint = 3;
	public static final int kCGPathElementCloseSubpath = 4; 
	public static final int kCGPatternTilingNoDistortion = 0;
	public static final int kCGPatternTilingConstantSpacingMinimalDistortion = 1;
	public static final int kCGPatternTilingConstantSpacing = 2;
	public static final int kCGRenderingIntentDefault = 0;
	public static final int kCGTextFill = 0;
	public static final int kCGTextInvisible = 3;
	public static final int kCMHelpItemRemoveHelp = 3;
	public static final int kColorPickerDialogIsMoveable =  1;
	public static final int kColorPickerDialogIsModal = 2;
	public static final int kControlBehaviorPushbutton = 0;
	public static final int kControlBehaviorToggles = 0x0100;
	public static final int kControlBevelButtonAlignCenter = 0;
	public static final int kControlBevelButtonAlignLeft  = 1;
	public static final int kControlBevelButtonAlignRight = 2;
	public static final int kControlBevelButtonAlignTextCenter = 1;
	public static final int kControlBevelButtonAlignTextFlushRight = -1;
	public static final int kControlBevelButtonAlignTextFlushLeft = -2;
	public static final int kControlBevelButtonNormalBevelProc = 33;
	public static final int kControlBevelButtonSmallBevel = 0;
	public static final int kControlBevelButtonLargeBevel = 2;
	public static final int kControlBevelButtonMenuRefTag = ('m'<<24) + ('h'<<16) + ('n'<<8) + 'd';
	public static final int kControlBevelButtonNormalBevel = 1;
	public static final int kControlBevelButtonPlaceBelowGraphic = 3;
	public static final int kControlBevelButtonPlaceToRightOfGraphic = 1;
	public static final int kControlBevelButtonKindTag = ('b'<<24) + ('e'<<16) + ('b'<<8) + 'k';
	public static final int kControlBevelButtonTextAlignTag = ('t'<<24) + ('a'<<16) + ('l'<<8) + 'i';
	public static final int kControlBevelButtonTextPlaceTag = ('t'<<24) + ('p'<<16) + ('l'<<8) + 'c';
	public static final int kControlBevelButtonGraphicAlignTag = ('g'<<24) + ('a'<<16) + ('l'<<8) + 'i';
	public static final int kControlBoundsChangeSizeChanged = 1 << 2;
	public static final int kControlBoundsChangePositionChanged = 1 << 3;
	public static final int kControlClockTypeHourMinute = 0;
	public static final int kControlClockTypeHourMinuteSecond = 1;
	public static final int kControlClockTypeMonthDayYear = 2;
	public static final int kControlClockTypeMonthYear = 3;
	public static final int kControlClockFlagStandard = 0;
	public static final int kControlClockFlagDisplayOnly = 1;
	public static final int kControlClockFlagLive = 2;
	public static final int kControlClockLongDateTag = ('d'<<24) + ('a'<<16) + ('t'<<8) + 'e';
	public static final int kControlCheckBoxAutoToggleProc = 371;
	public static final int kControlContentCGImageRef = 134;
	public static final int kControlContentCIconHandle = 130;
	public static final int kControlContentIconRef = 132;
	public static final int kControlContentMetaPart = -2;
	public static final int kControlContentTextOnly = 0;
	public static final int kControlDataBrowserIncludesFrameAndFocusTag = ('b'<<24) + ('r'<<16) + ('d'<<8) + 'r';
	public static final int kControlDownButtonPart = 21;
	public static final int kControlEditTextCFStringTag = ('c'<<24) + ('f'<<16) + ('s'<<8) + 't';
	public static final int kControlEditTextLockedTag = ('l'<<24) + ('o'<<16) + ('c'<<8) + 'k';
	public static final int kControlEditTextSingleLineTag = ('s'<<24) + ('g'<<16) + ('l'<<8) + 'c';
	public static final int kControlEditTextSelectionTag = ('s'<<24) + ('e'<<16) + ('l'<<8) + 'e';
	public static final int kControlEditTextTextTag = ('t'<<24) + ('e'<<16) + ('x'<<8) + 't';
	public static final int kControlEditTextInsertCFStringRefTag = ('i'<<24) + ('n'<<16) + ('c'<<8) + 'f';
	public static final int kControlEditTextPasswordCFStringTag = ('p'<<24) + ('w'<<16) + ('c'<<8) + 'f';
	public static final int kControlEditTextPart = 5;
	public static final int kControlEntireControl = 0;
	public static final int kControlFocusNoPart = 0;
	public static final int kControlFontStyleTag = ('f'<<24) + ('o'<<16) + ('n'<<8) + 't';
	public static final int kControlGetsFocusOnClick = 1 << 8;
	public static final int kControlGroupBoxTextTitleProc = 160;
	public static final int kControlHandlesTracking = 1 << 5;
	public static final int kControlIconTransformTag = ('t'<<24) + ('r'<<16) + ('f'<<8) + 'm';
	public static final int kControlIndicatorPart = 129;
	public static final int kControlKindScrollBar = ('s'<<24) + ('b'<<16) + ('a'<<8) + 'r';
	public static final int kControlMsgApplyTextColor = 30;
	public static final int kControlMsgSetUpBackground = 23;
	public static final int kControlPageDownPart = 23;
	public static final int kControlPageUpPart = 22;
	public static final int kControlPopupArrowEastProc = 192;
	public static final int kControlPopupArrowOrientationEast = 0;
	public static final int kControlPopupArrowOrientationWest = 1;
	public static final int kControlPopupArrowOrientationNorth = 2;
	public static final int kControlPopupArrowOrientationSouth = 3;
	public static final int kControlPopupArrowSizeNormal  = 0;
	public static final int kControlPopupArrowSizeSmall   = 1;
	public static final int kControlPopupButtonProc = 400;
	public static final int kControlProgressBarIndeterminateTag = ('i'<<24) + ('n'<<16) + ('d'<<8) + 'e';
	public static final int kControlProgressBarProc = 80;
	public static final int kControlPushButtonProc = 368;
	public static final int kControlRadioButtonAutoToggleProc = 372;
	public static final int kControlScrollBarLiveProc = 386;
	public static final int kControlSearchFieldCancelPart = 30;
	public static final int kControlSearchFieldMenuPart = 31;
	public static final int kControlSeparatorLineProc = 144;
	public static final int kControlSizeTag = ('s'<<24) + ('i'<<16) + ('z'<<8) + 'e';
	public static final int kControlSizeSmall = 1;
	public static final int kControlSliderLiveFeedback = (1 << 0);
	public static final int kControlSliderNonDirectional = (1 << 3);
	public static final int kControlSliderProc = 48;
	public static final int kControlClickableMetaPart = -4;
	public static final int kControlStructureMetaPart = -1;
	public static final int kControlSupportsEmbedding = 1 << 1;
	public static final int kControlSupportsFocus = 1 << 2;
	public static final int kControlStaticTextIsMultilineTag = ('s'<<24) + ('t'<<16) + ('i'<<8) + 'm';
	public static final int kControlStaticTextCFStringTag = ('c'<<24) + ('f'<<16) + ('s'<<8) + 't';
	public static final int kControlTabContentRectTag = ('r'<<24) + ('e'<<16) + ('c'<<8) + 't';
	public static final int kControlTabDirectionNorth = 0;
	public static final int kControlTabDirectionSouth = 1;
	public static final int kControlTabImageContentTag = ('c'<<24) + ('o'<<16) + ('n'<<8) + 't';
	public static final int kControlTabInfoVersionOne = 1;
	public static final int kControlTabInfoTag = ('t'<<24) + ('a'<<16) + ('b'<<8) + 'i';
	public static final int kControlTabSizeLarge = 0;
	public static final int kControlTabSmallProc = 129;
	public static final int kControlUpButtonPart = 20;
	public static final int kControlUserPaneDrawProcTag = ('d'<<24) + ('r'<<16) + ('a'<<8) + 'w';
	public static final int kControlUserPaneHitTestProcTag = ('h'<<24) + ('i'<<16) + ('t'<<8) + 't';
	public static final int kControlUserPaneProc = 256;
	public static final int kControlUserPaneTrackingProcTag = ('t'<<24) + ('r'<<16) + ('a'<<8) + 'k';
	public static final int kControlUseBackColorMask = 16;
	public static final int kControlUseFontMask = 0x1;
	public static final int kControlUseForeColorMask = 8;
	public static final int kControlUseJustMask = 0x0040;
	public static final int kControlUseSizeMask = 0x4;
	public static final int kControlUseThemeFontIDMask = 0x80;
	public static final int kControlUseFaceMask = 0x2;
	public static final int kControlWantsActivate = 1 << 4;
	public static final int kCurrentProcess = 2;
	public static final int kDataBrowserAttributeListViewAlternatingRowColors = (1 << 1);
	public static final int kDataBrowserAttributeListViewDrawColumnDividers = (1 << 2);
	public static final int kDataBrowserCheckboxType = ('c'<<24) + ('h'<<16) + ('b'<<8) + 'x';
	public static final int kDataBrowserCmdTogglesSelection = 1 << 3;
	public static final int kDataBrowserContainerClosed = 10;
	public static final int kDataBrowserContainerClosing = 9;
	public static final int kDataBrowserContainerIsClosableProperty = 6;
	public static final int kDataBrowserContainerIsOpen = 1 << 1;
	public static final int kDataBrowserContainerIsOpenableProperty = 5;
	public static final int kDataBrowserContainerIsSortableProperty = 7;
	public static final int kDataBrowserContainerOpened = 8;
	public static final int kDataBrowserCustomType = 0x3F3F3F3F;
	public static final int kDataBrowserDefaultPropertyFlags = 0;
	public static final int kDataBrowserDragSelect = 1 << 0;
	public static final int kDataBrowserIconAndTextType = ('t'<<24) + ('i'<<16) + ('c'<<8) + 'n';
	public static final int kDataBrowserItemAnyState = -1;
	public static final int kDataBrowserItemIsActiveProperty = 1;
	public static final int kDataBrowserItemIsContainerProperty = 4;
	public static final int kDataBrowserItemIsDragTarget  = 1 << 2;
	public static final int kDataBrowserItemIsEditableProperty = 3;
	public static final int kDataBrowserItemIsSelectableProperty = 2;
	public static final int kDataBrowserItemIsSelected = 1 << 0;
	public static final int kDataBrowserItemNoProperty = 0;
	public static final int kDataBrowserItemParentContainerProperty = 11;
	public static final int kDataBrowserItemsAdd = 0;
	public static final int kDataBrowserItemsAssign = 1;
	public static final int kDataBrowserItemsRemove = 3;
	public static final int kDataBrowserItemRemoved = 2;
	public static final int kDataBrowserItemSelected = 5;
	public static final int kDataBrowserItemDeselected = 6;
	public static final int kDataBrowserItemDoubleClicked = 7;
	public static final int kDataBrowserLatestCallbacks = 0;
	public static final int kDataBrowserLatestCustomCallbacks = 0;
	public static final int kDataBrowserListView = ('l'<<24) + ('s'<<16) + ('t'<<8) + 'v';
	public static final int kDataBrowserListViewLatestHeaderDesc = 0;
	public static final int kDataBrowserListViewMovableColumn = 1 << OS.kDataBrowserViewSpecificFlagsOffset + 1;
	public static final int kDataBrowserListViewSelectionColumn = 1 << OS.kDataBrowserViewSpecificFlagsOffset;
	public static final int kDataBrowserListViewSortableColumn = 1 << 18;
	public static final int kDataBrowserNeverEmptySelectionSet = 1 << 6;
	public static final int kDataBrowserMetricCellContentInset = 1;
	public static final int kDataBrowserMetricIconAndTextGap = 2;
	public static final int kDataBrowserMetricDisclosureColumnEdgeInset = 3;
	public static final int kDataBrowserMetricDisclosureTriangleAndContentGap = 4;
	public static final int kDataBrowserMetricDisclosureColumnPerDepthGap = 5;
	public static final int kDataBrowserNoItem = 0;
	public static final int kDataBrowserOrderUndefined = 0;
	public static final int kDataBrowserOrderIncreasing = 1;
	public static final int kDataBrowserOrderDecreasing = 2;
	public static final int kDataBrowserPropertyEnclosingPart = 0;
	public static final int kDataBrowserPropertyContentPart =  ('-'<<24) + ('-'<<16) + ('-'<<8) + '-';
	public static final int kDataBrowserPropertyDisclosurePart =  ('d'<<24) + ('i'<<16) + ('s'<<8) + 'c';
	public static final int kDataBrowserPropertyIsMutable = 1 << 0;
	public static final int kDataBrowserRevealOnly = 0;
	public static final int kDataBrowserRevealAndCenterInView = 1 << 0;
	public static final int kDataBrowserRevealWithoutSelecting = 1 << 1;
	public static final int kDataBrowserSelectOnlyOne = 1 << 1;
	public static final int kDataBrowserUserStateChanged = 13;
	public static final int kDataBrowserUserToggledContainer = 16;
	public static final int kDataBrowserTextType = ('t'<<24) + ('e'<<16) + ('x'<<8) + 't';
	public static final int kDataBrowserTableViewFillHilite = 1;
	public static final int kDataBrowserAttributeAutoHideScrollBars = 1 << 3;
	public static final int kDataBrowserViewSpecificFlagsOffset = 16;
	public static final int kDocumentWindowClass = 6;
	public static final int kDragActionNothing = 0;
	public static final int kDragActionCopy = 1;
	public static final int kDragActionAlias = 1 << 1;
	public static final int kDragActionGeneric = 1 << 2;
	public static final int kDragActionPrivate = 1 << 3;
	public static final int kDragActionMove = 1 << 4;
	public static final int kDragActionDelete = 1 << 5;
	public static final int kDragActionAll = 0xFFFFFFFF;
	public static final int kDragStandardTranslucency = 0;
	public static final int kDragTrackingEnterHandler = 1;
	public static final int kDragTrackingEnterWindow = 2;
	public static final int kDragTrackingInWindow = 3;
	public static final int kDragTrackingLeaveWindow = 4;
	public static final int kDragTrackingLeaveHandler = 5;
	public static final int kEventAppleEvent = 1;
	public static final int kEventAppDeactivated = 2;
	public static final int kEventAppAvailableWindowBoundsChanged = 110;
	public static final int kEventAppFocusMenuBar = 8;
	public static final int kEventAttributeUserEvent = 1 << 0;
	public static final int kEventClassAppleEvent = ('e'<<24) + ('p'<<16) + ('p'<<8) + 'c';
	public static final int kEventClassApplication = ('a'<<24) + ('p'<<16) + ('p'<<8) + 'l';
	public static final int kEventClassCommand = ('c'<<24) + ('m'<<16) + ('d'<<8) + 's';
	public static final int kEventClassClockView = ('c'<<24) + ('l'<<16) + ('o'<<8) + 'c';
	public static final int kEventClassControl = ('c'<<24) + ('n'<<16) + ('t'<<8) + 'l';
	public static final int kEventClassFont= ('f'<<24) + ('o'<<16) + ('n'<<8) + 't';
	public static final int kEventClassHIObject = ('h'<<24) + ('i'<<16) + ('o'<<8) + 'b';
	public static final int kEventClassKeyboard = ('k'<<24) + ('e'<<16) + ('y'<<8) + 'b';
	public static final int kEventClassMenu = ('m'<<24) + ('e'<<16) + ('n'<<8) + 'u';
	public static final int kEventClassMouse = ('m'<<24) + ('o'<<16) + ('u'<<8) + 's';
	public static final int kEventClassScrollable = ('s'<<24) + ('c'<<16) + ('r'<<8) + 'l';
	public static final int kEventClassSearchField = ('s'<<24) + ('r'<<16) + ('f'<<8) + 'd';
	public static final int kEventClassTextInput = ('t'<<24) + ('e'<<16) + ('x'<<8) + 't';
	public static final int kEventClassWindow = ('w'<<24) + ('i'<<16) + ('n'<<8) + 'd';
	public static final int kEventClockDateOrTimeChanged = 1;
	public static final int kEventControlApplyBackground = 5;
	public static final int kEventControlActivate = 9;
  	public static final int kEventControlAddedSubControl = 152;
	public static final int kEventControlBoundsChanged = 154;
	public static final int kEventControlClick = 13;
	public static final int kEventControlContextualMenuClick = 12;
  	public static final int kEventControlDeactivate = 10;
	public static final int kEventControlDraw = 4;
	public static final int kControlFocusNextPart = -1;
	public static final int kEventControlGetClickActivation = 17;
	public static final int kEventControlGetFocusPart = 8;
	public static final int kEventControlGetPartRegion = 101;
	public static final int kEventControlHit = 1;
	public static final int kEventControlHitTest = 3;
	public static final int kEventControlSetCursor = 11;
	public static final int kEventControlSetFocusPart = 7;
	public static final int kEventParamControlRegion = ('c'<<24) + ('r'<<16) + ('g'<<8) + 'n';
	public static final int kEventParamControlSubControl = ('c'<<24) + ('s'<<16) + ('u'<<8) + 'b';
	public static final int kEventControlRemovingSubControl = 153;
	public static final int kEventControlOwningWindowChanged = 159;
	public static final int kEventControlVisibilityChanged = 157;	
	public static final int kEventControlTrack = 51;
	public static final int kEventPriorityStandard = 1;
	public static final double kEventDurationForever = -1.0;
	public static final double kEventDurationNoWait = 0.0;
	public static final int kEventParamEventRef = ('e'<<24) + ('v'<<16) + ('n'<<8) + 't';
	public static final int kEventFontSelection = 2;
	public static final int kEventFontPanelClosed = 1;
	public static final int kEventParamGrafPort =  ('g'<<24) + ('r'<<16) + ('a'<<8) + 'f';
	public static final int kEventHIObjectConstruct = 1;
	public static final int kEventHIObjectDestruct = 3;
	public static final int kEventMenuCalculateSize = 1004;
	public static final int kEventMenuClosed = 5;
	public static final int kEventMenuCreateFrameView = 1005;
	public static final int kEventMenuDrawItem = 102;
	public static final int kEventMenuDrawItemContent = 103;
	public static final int kEventMenuGetFrameBounds = 1006;
	public static final int kEventMenuMatchKey = 7;
	public static final int kEventMenuMeasureItemWidth = 100;
	public static final int kEventMenuOpening = 4;
	public static final int kEventMenuPopulate = 9;
	public static final int kEventMenuTargetItem = 6;
	public static final int kEventMouseButtonPrimary = 1;
	public static final int kEventMouseButtonSecondary = 2;
	public static final int kEventMouseButtonTertiary = 3;
	public static final int kEventMouseDown = 1;
	public static final int kEventMouseDragged = 6;
	public static final int kEventMouseEntered = 8;
	public static final int kEventMouseExited = 9;
	public static final int kEventMouseMoved = 5;
	public static final int kEventMouseUp = 2;
	public static final int kEventMouseWheelAxisX = 0;
	public static final int kEventMouseWheelAxisY = 1;
	public static final int kEventMouseWheelMoved = 10;
	public static final int kEventParamAEEventClass = ('e'<<24) + ('v'<<16) + ('c'<<8) + 'l';
	public static final int kEventParamAEEventID = ('e'<<24) + ('v'<<16) + ('t'<<8) + 'i';
	public static final int kEventParamATSUFontID = ('a'<<24) + ('u'<<16) + ('i'<<8) + 'd';
	public static final int kEventParamATSUFontSize = ('a'<<24) + ('u'<<16) + ('s'<<8) + 'z';
	public static final int kEventParamAttributes = ('a'<<24) + ('t'<<16) + ('t'<<8) + 'r';
	public static final int kEventParamBounds =  ('b'<<24) + ('o'<<16) + ('u'<<8) + 'n';
	public static final int kEventParamCGContextRef = ('c'<<24) + ('n'<<16) + ('t'<<8) + 'x';
	public static final int kEventParamClickActivation = ('c'<<24) + ('l'<<16) + ('a'<<8) + 'c';
	public static final int kEventParamClickCount = ('c'<<24) + ('c'<<16) + ('n'<<8) + 't';
	public static final int kEventParamControlPart= ('c'<<24) + ('p'<<16) + ('r'<<8) + 't';
	public static final int kEventParamControlRef = ('c'<<24) + ('t'<<16) + ('r'<<8) + 'l';
	public static final int kEventParamCurrentBounds = ('c'<<24) + ('r'<<16) + ('c'<<8) + 't';
	public static final int kEventParamDirectObject = ('-'<<24) + ('-'<<16) + ('-'<<8) + '-';
	public static final int kEventParamDictionary = ('d'<<24) + ('i'<<16) + ('c'<<8) + 't';
	public static final int kEventParamFMFontFamily = ('f'<<24) + ('m'<<16) + ('f'<<8) + 'm';
	public static final int kEventParamFMFontStyle = ('f'<<24) + ('m'<<16) + ('s'<<8) + 't';
	public static final int kEventParamFMFontSize = ('f'<<24) + ('m'<<16) + ('s'<<8) + 'z';
	public static final int kEventParamFontColor = ('f'<<24) + ('c'<<16) + ('l'<<8) + 'r';
	public static final int kEventParamKeyCode = ('k'<<24) + ('c'<<16) + ('o'<<8) + 'd';
	public static final int kEventParamKeyMacCharCodes = ('k'<<24) + ('c'<<16) + ('h'<<8) + 'r';
	public static final int kEventParamKeyModifiers = ('k'<<24) + ('m'<<16) + ('o'<<8) + 'd';
	public static final int kEventParamMenuCommand = ('m'<<24) + ('c'<<16) + ('m'<<8) + 'd';
	public static final int kEventParamMenuItemIndex = ('i'<<24) + ('t'<<16) + ('e'<<8) + 'm';
	public static final int kEventParamMenuItemBounds = ('m'<<24) + ('i'<<16) + ('t'<<8) + 'b';
	public static final int kEventParamMenuItemWidth = ('m'<<24) + ('i'<<16) + ('t'<<8) + 'w';
	public static final int kEventParamModalClickResult = ('w'<<24) + ('m'<<16) + ('c'<<8) + 'r';
	public static final int kEventParamModalWindow = ('m'<<24) + ('w'<<16) + ('i'<<8) + 'n';
	public static final int kEventParamWindowModality = ('w'<<24) + ('m'<<16) + ('o'<<8) + 'd';
	public static final int kEventParamMouseButton = ('m'<<24) + ('b'<<16) + ('t'<<8) + 'n';
	public static final int kEventParamMouseChord = ('c'<<24) + ('h'<<16) + ('o'<<8) + 'r';
	public static final int kEventParamMouseLocation = ('m'<<24) + ('l'<<16) + ('o'<<8) + 'c';
	public static final int kEventParamMouseWheelAxis = ('m'<<24) + ('w'<<16) + ('a'<<8) + 'x';
	public static final int kEventParamMouseWheelDelta = ('m'<<24) + ('w'<<16) + ('d'<<8) + 'l';
	public static final int kEventParamPreviousBounds = ('p'<<24) + ('r'<<16) + ('c'<<8) + 't';
	public static final int kEventParamOrigin = ('o'<<24) + ('r'<<16) + ('g'<<8) + 'n';
	public static final int kEventParamOriginalBounds = ('o'<<24) + ('r'<<16) + ('c'<<8) + 't';
	public static final int kEventParamReason =  ('w'<<24) + ('h'<<16) + ('y'<<8) + '?';
	public static final int kEventParamRgnHandle =  ('r'<<24) + ('g'<<16) + ('n'<<8) + 'h';
	public static final int kEventParamTextInputReplyLeadingEdge = ('t'<<24) + ('r'<<16) + ('l'<<8) + 'e';
	public static final int kEventParamTextInputReplyPoint = ('t'<<24) + ('r'<<16) + ('p'<<8) + 't';
	public static final int kEventParamTextInputReplyRegionClass = ('t'<<24) + ('r'<<16) + ('r'<<8) + 'g';
	public static final int kEventParamTextInputReplyText = ('t'<<24) + ('r'<<16) + ('t'<<8) + 'x';
	public static final int kEventParamTextInputReplyTextOffset = ('t'<<24) + ('r'<<16) + ('t'<<8) + 'o';
	public static final int kEventParamTextInputSendCurrentPoint = ('t'<<24) + ('s'<<16) + ('c'<<8) + 'p';
	public static final int kEventParamTextInputSendFixLen = ('t'<<24) + ('s'<<16) + ('f'<<8) + 'x';
	public static final int kEventParamTextInputSendHiliteRng = ('t'<<24) + ('s'<<16) + ('h'<<8) + 'i';
	public static final int kEventParamTextInputSendKeyboardEvent = ('t'<<24) + ('s'<<16) + ('k'<<8) + 'e';
	public static final int kEventParamTextInputSendText = ('t'<<24) + ('s'<<16) + ('t'<<8) + 'x';
	public static final int kEventParamKeyUnicodes= ('k'<<24) + ('u'<<16) + ('n'<<8) + 'i';
	public static final int kEventParamWindowDefPart = ('w'<<24) + ('d'<<16) + ('p'<<8) + 'c';
	public static final int kEventParamWindowMouseLocation = ('w'<<24) + ('m'<<16) + ('o'<<8) + 'u';
	public static final int kEventParamWindowRef = ('w'<<24) + ('i'<<16) + ('n'<<8) + 'd';
	public static final int kEventParamWindowRegionCode   = ('w'<<24) + ('s'<<16) + ('h'<<8) + 'p';
	public static final int kEventProcessCommand = 1;
	public static final int kEventQueueOptionsNone = 0;
	public static final int kEventRawKeyDown = 1;
	public static final int kEventRawKeyRepeat = 2;
	public static final int kEventRawKeyUp = 3;
	public static final int kEventRawKeyModifiersChanged = 4;
	public static final int kEventSearchFieldCancelClicked = 1;
	public static final int kEventSearchFieldSearchClicked = 2;
	public static final int kEventScrollableScrollTo = 10;
	public static final int kEventTextInputUpdateActiveInputArea = 1;
	public static final int kEventTextInputUnicodeForKeyEvent = 2;
	public static final int kEventTextInputOffsetToPos = 3;
	public static final int kEventTextInputPosToOffset = 4;
	public static final int kEventTextInputGetSelectedText = 6;
	public static final int kEventWindowActivated = 5;
	public static final int kEventWindowBoundsChanged = 27;
	public static final int kEventWindowClose = 72;
	public static final int kEventWindowCollapsing = 86;
	public static final int kEventWindowCollapsed = 67;
	public static final int kEventWindowDeactivated = 6;
	public static final int kEventWindowDrawContent = 2;
	public static final int kEventWindowExpanded = 70;
	public static final int kEventWindowFocusAcquired = 200;
	public static final int kEventWindowFocusRelinquish = 201;
	public static final int kEventWindowGetClickModality = 8;
	public static final int kEventWindowGetRegion = 1002;
	public static final int kEventWindowHidden = 25;
	public static final int kEventWindowHitTest = 1003;
	public static final int kEventWindowShown = 24;
	public static final int kEventWindowToolbarSwitchMode = 150;
	public static final int kEventWindowUpdate = 1;
	public static final int kEventWindowZoom = 75;
	public static final int kFMIterationCompleted = -980;
	public static final int kFloatingWindowClass = 5;
	public static final int kFontFamilyName = 1;
	public static final int kFontNoPlatformCode = -1;
	public static final int kFontNoScriptCode = -1;
	public static final int kFontNoLanguageCode = -1;
	public static final int kFontUnicodePlatform = 0;
	public static final int kFontSelectionQDStyleVersionZero = 0;
	public static final int kFontSelectionATSUIType = ('a'<<24) + ('s'<<16) + ('t'<<8) + 'l';
	public static final int kFontSelectionQDType = ('q'<<24) + ('s'<<16) + ('t'<<8) + 'l';
	public static final int kHIComboBoxAutoCompletionAttribute = (1 << 0);
	public static final int kHIComboBoxAutoDisclosureAttribute = (1 << 1);
	public static final int kHIComboBoxAutoSizeListAttribute = (1 << 3);
	public static final int kHIComboBoxEditTextPart = 5;
	public static final int kHIComboBoxNumVisibleItemsTag = ('c'<<24) + ('b'<<16) + ('n'<<8) + 'i';
	public static final int kHICommandFromMenu = 1 << 0;
	public static final int kHICommandQuit = ('q'<<24) + ('u'<<16) + ('i'<<8) + 't';
	public static final int kHILayoutBindMin = 1;
	public static final int kHILayoutBindMax= 2;
	public static final int kHILayoutPositionCenter = 1;
	public static final int kHIModalClickIsModal = 1 << 0;
	public static final int kHIModalClickAllowEvent = 1 << 1;
	public static final int kHIModalClickAnnounce = 1 << 2;	
	public static final int kHIScrollViewOptionsVertScroll = (1 << 0);
	public static final int kHIScrollViewOptionsHorizScroll = (1 << 1);
	public static final int kHIScrollViewOptionsAllowGrow = (1 << 2);
	public static final int kHIThemeFrameTextFieldSquare = 0;
	public static final int kHIThemeGroupBoxKindPrimary = 0;
	public static final int kKLKCHRuchrKind = 0;
	public static final int kKLKCHRKind = 1;
	public static final int kKLuchrKind = 2;
	public static final int kKLKind = 7;
	public static final int kKLuchrData = 1;
	public static final int kUCKeyActionDown = 0;
	public static final int kHIThemeFrameListBox = 1;
	public static final int kRedrawHighlighting = 1;
	public static final int kTSMOutsideOfBody = 1;
	public static final int kTSMInsideOfBody = 2;
	public static final int kTSMInsideOfActiveInputArea = 3;
	public static final int kThemeComboBox = 16;
	public static final int kThemeLeftOutsideArrowPressed = 0x01;
	public static final int kThemeLeftInsideArrowPressed = 0x02;
	public static final int kThemeLeftTrackPressed = 0x04;
	public static final int kThemePopupButton = 5;
	public static final int kThemeThumbPressed = 0x08;
	public static final int kThemeRightTrackPressed = 0x10;
	public static final int kThemeRightInsideArrowPressed = 0x20;
	public static final int kThemeRightOutsideArrowPressed = 0x40;
	public static final int kThemeScrollBarMedium = 0;
	public static final int kThemeSliderMedium = 2;
	public static final int kThemeProgressBarMedium = 3;
	public static final int kThemeTrackActive = 0;
	public static final int kThemeTrackDisabled = 1;
	public static final int kThemeTrackNothingToScroll = 2;
	public static final int kThemeTrackInactive = 3;
	public static final int kThemeTrackHorizontal = (1 << 0);
	public static final int kThemeTrackRightToLeft = (1 << 1);
	public static final int kThemeTrackShowThumb = (1 << 2);
	public static final int kThemeTrackThumbRgnIsNotGhost = (1 << 3);
	public static final int kThemeTrackNoScrollBarArrows = (1 << 4);
	public static final int kThemeTrackHasFocus = (1 << 5);
	public static final int kThemeTabNonFront = 0;
	public static final int kThemeTabNonFrontPressed = 1;
	public static final int kThemeTabNonFrontInactive = 2;
	public static final int kThemeTabFront = 3;
	public static final int kThemeTabFrontInactive = 4;
	public static final int kThemeTabNonFrontUnavailable = 5;
	public static final int kThemeTabFrontUnavailable = 6;
	public static final int kThemeTabNorth = 0;
	public static final int kThemeTabSouth = 1;
	public static final int kThemeTabEast = 2;
	public static final int kThemeTabWest = 3;
	public static final int kThemeTextColorMenuItemDisabled = 36;
	public static final int kHIThemeOrientationNormal = 0;
	public static final int kHIThemeOrientationInverted = 1;
	public static final int kHIThemeTextHorizontalFlushLeft = 0;
	public static final int kHIThemeTextHorizontalFlushCenter = 1;
	public static final int kHIThemeTextHorizontalFlushRight = 2;
	public static final int kHIThemeTextVerticalFlushTop  = 0;
	public static final int kHIThemeTextVerticalFlushCenter = 1;
	public static final int kHIThemeTextVerticalFlushBottom = 2;
	public static final int kHIThemeTabPositionFirst = 0;
	public static final int kHIThemeTabPositionMiddle = 1;
	public static final int kHIThemeTabPositionLast = 2;
	public static final int kHIThemeTabPositionOnly = 3;
	public static final int kHIThemeTabAdornmentFocus = 1 << 2;
	public static final int kHIThemeTabAdornmentTrailingSeparator = 1 << 4;
	public static final int kHIViewFeatureIsOpaque = 1 << 25;
	public static final int kHIViewZOrderAbove = 1;
	public static final int kHIViewZOrderBelow = 2;
	public static final int kHITransformNone = 0x00;
	public static final int kHITransformDisabled = 0x01;
	public static final int kHITransformSelected = 0x4000;
	public static final int kHMCFStringContent = ('c'<<24) + ('f'<<16) + ('s'<<8) + 't';
	public static final int kHMOutsideBottomRightAligned = 10;
	public static final int kHMAbsoluteCenterAligned = 23;
	public static final int kHMContentProvided = 0;
	public static final int kHMContentNotProvided = -1;
	public static final int kHMContentNotProvidedDontPropagate = -2;
	public static final int kHMDefaultSide = 0;
	public static final int kHMDisposeContent = 1;
	public static final int kHMSupplyContent = 0;
	public static final int kHelpWindowClass = 10;
	public static final int kInvalidFontFamily = -1;
	public static final int kLarge1BitMask = ('I'<<24) + ('C'<<16) + ('N'<<8) + '#';
	public static final int kLarge4BitData = ('i'<<24) + ('c'<<16) + ('l'<<8) + '4';
	public static final int kLarge8BitData = ('i'<<24) + ('c'<<16) + ('l'<<8) + '8';
	public static final int kLarge32BitData = ('i'<<24) + ('l'<<16) + ('3'<<8) + '2';
	public static final int kLarge8BitMask = ('l'<<24) + ('8'<<16) + ('m'<<8) + 'k';
	public static final int kSmall1BitMask = ('i'<<24) + ('c'<<16) + ('s'<<8) + '#';
	public static final int kSmall4BitData = ('i'<<24) + ('c'<<16) + ('s'<<8) + '4';
	public static final int kSmall8BitData = ('i'<<24) + ('c'<<16) + ('s'<<8) + '8';
	public static final int kSmall32BitData = ('i'<<24) + ('s'<<16) + ('3'<<8) + '2';
	public static final int kSmall8BitMask = ('s'<<24) + ('8'<<16) + ('m'<<8) + 'k';
	public static final int kMini1BitMask = ('i'<<24) + ('c'<<16) + ('m'<<8) + '#';
	public static final int kMini4BitData = ('i'<<24) + ('c'<<16) + ('m'<<8) + '4';
	public static final int kMini8BitData = ('i'<<24) + ('c'<<16) + ('m'<<8) + '8';
	public static final int kThumbnail32BitData = ('i'<<24) + ('t'<<16) + ('3'<<8) + '2';
	public static final int kThumbnail8BitMask = ('t'<<24) + ('8'<<16) + ('m'<<8) + 'k';
	public static final int kHuge1BitMask = ('i'<<24) + ('c'<<16) + ('h'<<8) + '#';
	public static final int kHuge4BitData = ('i'<<24) + ('c'<<16) + ('h'<<8) + '4';
	public static final int kHuge8BitData = ('i'<<24) + ('c'<<16) + ('h'<<8) + '8';
	public static final int kHuge32BitData = ('i'<<24) + ('h'<<16) + ('3'<<8) + '2';
	public static final int kHuge8BitMask = ('h'<<24) + ('8'<<16) + ('m'<<8) + 'k';
	public static final int kLigaturesType = 1;
	public static final int kQDParseRegionFromTop = (1 << 0);
	public static final int kQDParseRegionFromBottom = (1 << 1);
	public static final int kQDParseRegionFromLeft = (1 << 2);
	public static final int kQDParseRegionFromRight = (1 << 3);
	public static final int kQDParseRegionFromTopLeft = kQDParseRegionFromTop | kQDParseRegionFromLeft;
	public static final int kQDRegionToRectsMsgInit = 1;
	public static final int kQDRegionToRectsMsgParse = 2;
	public static final int kQDRegionToRectsMsgTerminate = 3;
	public static final int kRequiredLigaturesOffSelector = 1;
	public static final int kCommonLigaturesOffSelector = 3;
	public static final int kRareLigaturesOffSelector = 5;
	public static final int kLogosOffSelector = 7;
	public static final int kLSLaunchDefaults = 0x00000001;
	public static final int kLSUnknownType = 0;
	public static final int kLSUnknownCreator = 0;
	public static final int kLSRolesAll = 0xFFFFFFFF;
	public static final int kRebusPicturesOffSelector = 9;
	public static final int kDiphthongLigaturesOffSelector = 11;
	public static final int kSquaredLigaturesOffSelector = 13;
	public static final int kAbbrevSquaredLigaturesOffSelector = 15;
	public static final int kMacHelpVersion = 3;
	public static final int kMenuBlankGlyph = 97;
	public static final int kMenuCapsLockGlyph = 99;
	public static final int kMenuCGImageRefType = 7;
	public static final int kMenuCheckmarkGlyph = 18;
	public static final int kMenuClearGlyph = 28;
	public static final int kMenuCommandGlyph = 17;
	public static final int kMenuContextualMenuGlyph = 109;
	public static final int kMenuControlGlyph = 6;
	public static final int kMenuControlISOGlyph = 138;
	public static final int kMenuControlModifier = 4;
	public static final int kMenuDeleteLeftGlyph = 23;
	public static final int kMenuDeleteRightGlyph = 10;
	public static final int kMenuDiamondGlyph = 19;
	public static final int kMenuDownArrowGlyph = 106;
	public static final int kMenuDownwardArrowDashedGlyph = 16;
	public static final int kMenuEnterGlyph = 4;
	public static final int kMenuEscapeGlyph = 27;
	public static final int kMenuEventIncludeDisabledItems = 0x0001;
	public static final int kMenuEventQueryOnly = 0x0002;
	public static final int kMenuEventDontCheckSubmenus = 0x0004;
	public static final int kMenuF10Glyph = 120;
	public static final int kMenuF11Glyph = 121;
	public static final int kMenuF12Glyph = 122;
	public static final int kMenuF13Glyph = 135;
	public static final int kMenuF14Glyph = 136;
	public static final int kMenuF15Glyph = 137;
	public static final int kMenuF1Glyph = 111;
	public static final int kMenuF2Glyph = 112;
	public static final int kMenuF3Glyph = 113;
	public static final int kMenuF4Glyph = 114;
	public static final int kMenuF5Glyph = 115;
	public static final int kMenuF6Glyph = 116;
	public static final int kMenuF7Glyph = 117;
	public static final int kMenuF8Glyph = 118;
	public static final int kMenuF9Glyph = 119;
	public static final int kMenuHelpGlyph = 103;
	public static final int kMenuItemAttrCustomDraw = 1 << 11;
	public static final int kMenuItemAttrAutoRepeat = 1 << 9;
	public static final int kMenuItemAttrSeparator = 64;
	public static final int kMenuLeftArrowDashedGlyph = 24;
	public static final int kMenuLeftArrowGlyph = 100;
	public static final int kMenuNoCommandModifier = (1 << 3);
	public static final int kMenuNoIcon = 0;
	public static final int kMenuNoModifiers = 0;
	public static final int kMenuNonmarkingReturnGlyph = 13;
	public static final int kMenuNorthwestArrowGlyph = 0x66;
	public static final int kMenuNullGlyph = 0;
	public static final int kMenuOptionGlyph = 7;
	public static final int kMenuOptionModifier = (1 << 1);
	public static final int kMenuPageDownGlyph = 107;
	public static final int kMenuPageUpGlyph = 98;
	public static final int kMenuPencilGlyph = 15;
	public static final int kMenuPowerGlyph = 110;
	public static final int kMenuReturnGlyph = 11;
	public static final int kMenuReturnR2LGlyph = 12;
	public static final int kMenuRightArrowDashedGlyph = 26;
	public static final int kMenuRightArrowGlyph = 101;
	public static final int kMenuShiftGlyph = 5;
	public static final int kMenuShiftModifier = (1 << 0);
	public static final int kMenuSoutheastArrowGlyph = 0x69;
	public static final int kMenuSpaceGlyph = 9;
	public static final int kMenuTabRightGlyph = 2;
	public static final int kMenuUpArrowDashedGlyph = 25;
	public static final int kMenuUpArrowGlyph = 104;
	public static final int kMouseTrackingMouseDown= 1;
	public static final int kMouseTrackingMouseUp= 2;
	public static final int kMouseTrackingMouseExited  = 3;
	public static final int kMouseTrackingMouseEntered = 4;
	public static final int kMouseTrackingMouseDragged= 5;
	public static final int kMouseTrackingMouseKeyModifiersChanged= 6;
	public static final int kMouseTrackingUserCancelled= 7;
	public static final int kMouseTrackingTimedOut= 8;
	public static final int kMouseTrackingMouseMoved= 9;
	public static final int kModalWindowClass = 3;
	public static final int kMovableModalWindowClass = 4;
	public static final int kNavAllowInvisibleFiles = 0x00000100;
	public static final int kNavAllowMultipleFiles = 0x00000080;
	public static final int kNavAllowOpenPackages = 0x00002000;
	public static final int kNavCBNewLocation = 5;
	public static final int kNavCBPopupMenuSelect = 8;
	public static final int kNavCtlSelectCustomType = 21;
	public static final int kNavCtlSetLocation = 8;
	public static final int kNavFilteringBrowserList = 0;
	public static final int kNavGenericSignature = ('*'<<24) + ('*'<<16) + ('*'<<8) + '*';
	public static final int kNavSupportPackages = 0x00001000;
	public static final int kNavDontConfirmReplacement = 0x00010000;
	public static final int kNavUserActionCancel = 1;
	public static final int kNavUserActionChoose = 4;
	public static final int kNavUserActionOpen = 2;
	public static final int kNavUserActionSaveAs = 3;
	public static final short kOnSystemDisk = -32768;
	public static final int kOverlayWindowClass = 14;
	public static final int kPMCancel = 0x0080;
	public static final int kPMDestinationFax = 3;
	public static final int kPMDestinationFile = 2;
	public static final int kPMDestinationPreview = 4;
	public static final int kPMDestinationPrinter = 1;
	public static final short kPMLandscape = 2;
	public static final short kPMPortrait = 1;
	public static final int kPMPrintAllPages = 2147483647;
	public static final int kPMShowDefaultInlineItems = 1 << 15;
	public static final int kPMShowPageAttributesPDE = 1 << 8;
	public static final int kQDUseCGTextMetrics = (1 << 2);
	public static final int kQDUseCGTextRendering = (1 << 1);
	public static final int kScrapFlavorTypeUnicode = ('u'<<24) + ('t'<<16) + ('x'<<8) + 't';
	public static final int kScrapFlavorTypeText = ('T'<<24) + ('E'<<16) + ('X'<<8) + 'T';
	public static final boolean kScrollBarsSyncAlwaysActive = true;
	public static final boolean kScrollBarsSyncWithFocus = false;
	public static final int kHISearchFieldNoAttributes = 0;
	public static final int kHISearchFieldAttributesCancel = 1 << 0;
	public static final int kHISearchFieldAttributesSearchIcon = 1 << 1;
	public static final int kSelectorAlLAvailableData = 0xFFFFFFFF;
	public static final int kSetFrontProcessFrontWindowOnly = 1 << 0;
	public static final int kSheetWindowClass = 11;
	public static final int kStdCFStringAlertVersionOne = 1;
	public static final int kSystemIconsCreator = ('m'<<24) + ('a'<<16) + ('c'<<8) + 's';
	public static final int kSymbolLigaturesOffSelector = 17;
	public static final int kControlSliderDoesNotPoint = 2;
	public static final int kTXNViewRectKey = 0;
	public static final int kTXNDestinationRectKey = 1;
	public static final int kTXNTextRectKey = 2;
	public static final int kTXNVerticalScrollBarRectKey = 3;
	public static final int kTXNHorizontalScrollBarRectKey = 4;
	public static final int kTXNAlwaysWrapAtViewEdgeMask = 1 << 11;
	public static final int kTXNBackgroundTypeRGB = 1;
	public static final int kTXNDefaultFontSize = 0x000C0000;
	public static final int kTXNDefaultFontStyle = 0;
	public static final int kTXNDefaultFontName = 0;
	public static final int kTXNFlushLeft = 1;
	public static final int kTXNFlushRight = 2;
	public static final int kTXNCenter = 4;
	public static final int kTXNLeftToRight = 0;
	public static final int kTXNRightToLeft = 1;
	public static final int kTXNDisableDragAndDropTag = ('d'<<24) + ('r'<<16) + ('a'<<8) + 'g';
	public static final int kTXNDoFontSubstitution = ('f'<<24) + ('S'<<16) + ('u'<<8) + 'b';
	public static final int kTXNDontDrawCaretWhenInactiveMask = 1 << 12;
	public static final int kTXNDrawCaretWhenInactiveTag = ('d'<<24)+('c'<<16)+('r'<<8)+'t';
	public static final int kTXNEndOffset = 2147483647;
	public static final int kTXNIOPrivilegesTag = ('i'<<24) + ('o'<<16) + ('p'<<8) + 'v';
	public static final int kTXNJustificationTag = ('j'<<24) + ('u'<<16) + ('s'<<8) + 't';
	public static final int kTXNLineDirectionTag = ('l'<<24) + ('n'<<16) + ('d'<<8) + 'r';
	public static final int kTXNMarginsTag = ('m'<<24) + ('a'<<16) + ('r'<<8) + 'g';
	public static final int kTXNMonostyledTextMask = 1 << 17;
	public static final int kTXNQDFontFamilyIDAttribute = ('f'<<24) + ('o'<<16) + ('n'<<8) + 't';
	public static final int kTXNQDFontSizeAttribute = ('s'<<24) + ('i'<<16) + ('z'<<8) + 'e';
	public static final int kTXNQDFontStyleAttribute = ('f'<<24) + ('a'<<16) + ('c'<<8) + 'e';
	public static final int kTXNQDFontColorAttribute = ('k'<<24) + ('l'<<16) + ('o'<<8) + 'r';
	public static final int kTXNQDFontFamilyIDAttributeSize = 2;
	public static final int kTXNQDFontSizeAttributeSize = 2;
	public static final int kTXNQDFontStyleAttributeSize = 2;
	public static final int kTXNQDFontColorAttributeSize = 6;
	public static final int kTXNReadOnlyMask = 1 << 5;
	public static final int kTXNSingleLineOnlyMask = 1 << 14;
	public static final int kTXNStartOffset = 0;
	public static final int kTXNSystemDefaultEncoding = 0;
	public static final int kTXNTabSettingsTag = ('t'<<24) + ('a'<<16) + ('b'<<8) + 's';
	public static final int kTXNTextEditStyleFrameType = 1;
	public static final int kTXNUnicodeTextData = ('u'<<24) + ('t'<<16) + ('x'<<8) + 't';
	public static final int kTXNUnicodeTextFile = ('u'<<24) + ('t'<<16) + ('x'<<8) + 't';
	public static final int kTXNUseCurrentSelection = -1;
	public static final int kTXNVisibilityTag = ('v'<<24) + ('i'<<16) + ('s'<<8) + 'b';
	public static final int kTXNWordWrapStateTag = ('w'<<24) + ('w'<<16) + ('r'<<8) + 's';
	public static final int kTXNAutoScrollBehaviorTag = ('s'<<24) + ('b'<<16) + ('e'<<8) + 'v';
	public static final int kTXNWantHScrollBarMask = 1 << 2;
	public static final int kTXNWantVScrollBarMask = 1 << 3;
	public static final int kTextEncodingMacUnicode = 0x7E;
	public static final int kTextEncodingMacRoman = 0;
	public static final int kTextLanguageDontCare = -128;
	public static final int kTextRegionDontCare = -128;
	public static final int kThemeAdornmentDefault = 1 << 0;
	public static final int kThemeAdornmentFocus = 1 << 2;
	public static final int kThemeAliasArrowCursor = 2;
	public static final int kThemeArrowButton = 4;
	public static final int kThemeArrowCursor = 0;
	public static final int kThemeArrowLeft = 0;
	public static final int kThemeArrowDown = 1;
	public static final int kThemeArrowRight = 2;
	public static final int kThemeArrowUp = 3;
	public static final int kThemeArrow5pt = 1;
	public static final int kThemeArrow9pt = 3;
	public static final int kThemeBevelButtonSmall = 8;
	public static final int kThemeBrushDialogBackgroundActive = 1;
	public static final int kThemeBrushDocumentWindowBackground = 15;
	public static final int kThemeBrushPrimaryHighlightColor = -3;
	public static final int kThemeBrushSecondaryHighlightColor = -4;
	public static final int kThemeBrushButtonFaceActive = 29;
	public static final int kThemeBrushButtonInactiveDarkShadow = 36;
	public static final int kThemeBrushFocusHighlight = 19;
	public static final int kThemeBrushListViewBackground = 10; 
	public static final int kThemeButtonOff = 0;
	public static final int kThemeButtonOn = 1;
	public static final int kThemeButtonMixed = 2;
	public static final int kThemeCheckBox = 1;
	public static final int kThemeCopyArrowCursor = 1;
	public static final int kThemeCrossCursor = 5;
	public static final int kThemeCurrentPortFont = 200;
	public static final int kThemeDisclosureButton = 6;
	public static final int kThemeDisclosureTriangle = 6;
	public static final int kThemeDisclosureRight = 0;
	public static final int kThemeDisclosureDown = 1;
	public static final int kThemeDisclosureLeft = 2;
	public static final int kThemeEmphasizedSystemFont = 4;
	public static final int kThemeIBeamCursor = 4;
	public static final int kThemeMenuItemCmdKeyFont = 103;
	public static final int kThemeMenuItemFont = 101;
	public static final int kThemeMenuItemHierarchical = 1;
	public static final int kThemeMetricDisclosureButtonWidth = 22;
	public static final int kThemeMetricDisclosureTriangleHeight = 25;
	public static final int kThemeMetricDisclosureTriangleWidth = 26;
	public static final int kThemeMetricCheckBoxWidth = 50;
	public static final int kThemeMetricComboBoxLargeDisclosureWidth = 74;
	public static final int kThemeMetricRadioButtonWidth = 52;
	public static final int kThemeMetricEditTextFrameOutset = 5;
	public static final int kThemeMetricEditTextWhitespace = 4;
	public static final int kThemeMetricFocusRectOutset = 7;
	public static final int kThemeMetricHSliderHeight = 41;
	public static final int kThemeMetricLargeTabHeight = 10;
	public static final int kThemeMetricLargeTabCapsWidth = 11;
	public static final int kThemeMetricLittleArrowsHeight = 27;
	public static final int kThemeMetricLittleArrowsWidth = 28;
	public static final int kThemeMetricMenuTextTrailingEdgeMargin = 67;
	public static final int kThemeMetricMenuIconTrailingEdgeMargin = 69;
	public static final int kThemeMetricNormalProgressBarThickness = 58;
	public static final int kThemeMetricTabFrameOverlap = 12;
	public static final int kThemeMetricPrimaryGroupBoxContentInset = 61;
	public static final int kThemeMetricPushButtonHeight = 19;
	public static final int kThemeMetricRoundTextFieldContentHeight = 80;
	public static final int kThemeMetricRoundTextFieldContentInsetLeft = 76;
	public static final int kThemeMetricRoundTextFieldContentInsetRight = 77;
	public static final int kThemeMetricRoundTextFieldContentInsetBottom = 78;
	public static final int kThemeMetricRoundTextFieldContentInsetTop = 79;
	public static final int kThemeMetricRoundTextFieldContentInsetWithIconLeft = 109;
	public static final int kThemeMetricRoundTextFieldContentInsetWithIconRight = 110;
	public static final int kThemeMetricRoundTextFieldSmallContentInsetLeft = 120;
	public static final int kThemeMetricRoundTextFieldSmallContentInsetRight = 121;
	public static final int kThemeMetricRoundTextFieldSmallContentInsetWithIconLeft = 123;
	public static final int kThemeMetricRoundTextFieldSmallContentInsetWithIconRight = 124;
	public static final int kThemeMetricScrollBarWidth = 0;
	public static final int kThemeMetricVSliderWidth = 45;
	public static final int kThemeNotAllowedCursor = 18;
	public static final int kThemePointingHandCursor = 10;
	public static final int kThemePushButton = 0;
	public static final int kThemePushButtonFont = 105;
	public static final int kThemeRadioButton = 2;
	public static final int kThemeResizeLeftRightCursor = 17;
	public static final int kThemeResizeUpDownCursor = 21;
	public static final int kThemeResizeUpCursor = 19;
	public static final int kThemeResizeDownCursor = 20;
	public static final int kThemeResizeLeftCursor = 15;
	public static final int kThemeResizeRightCursor = 16;
	public static final int kThemeRoundedBevelButton = 15;
	public static final int kThemeSmallBevelButton = 8;
	public static final int kThemeSmallEmphasizedSystemFont = 2;
	public static final int kThemeSmallSystemFont = 1;
	public static final int kThemeSpinningCursor = 14;
	public static final int kThemeStateActive = 1;
	public static final int kThemeStateInactive = 0;
	public static final int kThemeStatePressed = 2;
	public static final int kThemeStateRollover = 6;
	public static final int kThemeStateUnavailable = 7;
	public static final int kThemeStateUnavailableInactive = 8;
	public static final int kThemeSystemFont = 0;
	public static final int kThemeTextColorDialogActive = 1;
	public static final int kThemeTextColorPushButtonInactive = 13;
	public static final int kThemeTextColorDocumentWindowTitleActive = 23;
	public static final int kThemeTextColorDocumentWindowTitleInactive = 24;
	public static final int kThemeTextColorListView = 22;
	public static final int kThemeTextColorPushButtonActive = 12;
	public static final int kThemeToolbarFont = 108;
	public static final int kThemeViewsFont = 3;
	public static final int kThemeWatchCursor = 7;
	public static final int kTrackMouseLocationOptionDontConsumeMouseUp = 1;
	public static final int kTransformSelected = 0x4000;
	public static final int kUIModeNormal = 0;
	public static final int kUIModeContentSuppressed = 1;
	public static final int kUIModeContentHidden = 2;
	public static final int kUIModeAllHidden = 3;
	public static final int kUIModeAllSuppressed = 4;
	public static final int kUnicodeDocument = ('u'<<24) + ('d'<<16) + ('o'<<8) + 'c';
	public static final int kUtilityWindowClass = 8;
    public static final int kWindowActivationScopeNone = 0;
    public static final int kWindowActivationScopeIndependent = 1;
    public static final int kWindowActivationScopeAll = 2;
	public static final int kWindowAlertPositionParentWindowScreen = 0x700A;
    public static final int kWindowBoundsChangeOriginChanged = 1<<3;
    public static final int kWindowBoundsChangeSizeChanged = 1<<2;
    public static final int kWindowCascadeOnMainScreen = 4;
	public static final int kWindowCloseBoxAttribute = (1 << 0);
	public static final int kWindowCollapseBoxAttribute = (1 << 3);
	public static final int kWindowCompositingAttribute = (1 << 19);
	public static final int kWindowContentRgn = 33;
	public static final int kWindowGroupAttrHideOnCollapse = (1 << 4);
	public static final int kWindowGroupAttrSelectAsLayer = (1 << 0);
	public static final int kWindowHorizontalZoomAttribute = 1 << 1;
	public static final int kWindowVerticalZoomAttribute  = 1 << 2;
	public static final int kWindowFullZoomAttribute = (OS.kWindowVerticalZoomAttribute | OS.kWindowHorizontalZoomAttribute);
	public static final int kWindowLiveResizeAttribute = (1 << 28);
	public static final int kWindowModalityAppModal = 2;
	public static final int kWindowModalityNone = 0;
	public static final int kWindowModalitySystemModal = 1;
	public static final int kWindowModalityWindowModal = 3;
	public static final int kWindowNoShadowAttribute = (1 << 21);
	public static final int kWindowResizableAttribute = (1 << 4);
	public static final int kWindowStandardHandlerAttribute = (1 << 25);
	public static final int kWindowStructureRgn = 32;
	public static final int kWindowToolbarButtonAttribute = (1 << 6);
	public static final int kWindowUpdateRgn= 34;
	public static final int kWindowNoTitleBarAttribute = (1 << 9);
	public static final int kCaretPosition = 1;
	public static final int kRawText = 2;
	public static final int kSelectedRawText = 3;
	public static final int kConvertedText = 4;
	public static final int kSelectedConvertedText = 5;
	public static final int kBlockFillText = 6;
	public static final int kOutlineText = 7;
	public static final int kSelectedText = 8;
	public static final int menuItemNotFoundError = -5622;
	public static final int mouseDown = 1;
	public static final int noErr = 0;
	public static final int normal = 0;
	public static final int optionKey = 1 << 11;
	public static final int osEvt = 15;
	public static final int paramErr = -50;
	public static final int shiftKey = 1 << 9;
	public static final int smKCHRCache = 38;	
	public static final int smKeyScript = 22;
	public static final int smRegionCode = 40;
	public static final int smSystemScript = -1;
	public static final int srcCopy = 0;
	public static final int srcOr = 1;
	public static final int srcXor = 2;
	public static final int notSrcXor = 6;
	public static final int teFlushDefault = 0;
	public static final int teCenter = 1;
	public static final int teFlushRight = -1;
	public static final int teFlushLeft = -2;
	public static final int teJustLeft = 0;
	public static final int teJustCenter = 1;
	public static final int teJustRight = -1;
	public static final int typeBoolean = ('b'<<24) + ('o'<<16) + ('o'<<8) + 'l';
	public static final int typeCFDictionaryRef = ('c'<<24) + ('f'<<16) + ('d'<<8) + 'c';
	public static final int typeCFMutableArrayRef = ('c'<<24) + ('f'<<16) + ('m'<<8) + 'a';
	public static final int typeCFStringRef = ('c'<<24) + ('f'<<16) + ('s'<<8) + 't';
	public static final int typeCFTypeRef = ('c'<<24) + ('f'<<16) + ('t'<<8) + 'y';
	public static final int typeCGContextRef = ('c'<<24) + ('n'<<16) + ('t'<<8) + 'x';
	public static final int typeChar = ('T'<<24) + ('E'<<16) + ('X'<<8) + 'T';
	public static final int typeClickActivationResult = ('c'<<24) + ('l'<<16) + ('a'<<8) + 'c';
	public static final int typeControlPartCode = ('c'<<24) + ('p'<<16) + ('r'<<8) + 't';
	public static final int typeControlRef = ('c'<<24) + ('t'<<16) + ('r'<<8) + 'l';
	public static final int typeEventRef = ('e'<<24) + ('v'<<16) + ('r'<<8) + 'f';
	public static final int typeFileURL = ('f'<<24) + ('u'<<16) + ('r'<<8) + 'l';
	public static final int typeFixed  = ('f'<<24) + ('i'<<16) + ('x'<<8) + 'd';
	public static final int typeFSRef = ('f'<<24) + ('s'<<16) + ('r'<<8) + 'f';
	public static final int typeGrafPtr =  ('g'<<24) + ('r'<<16) + ('a'<<8) + 'f';
	public static final int typeHICommand = ('h'<<24) + ('c'<<16) + ('m'<<8) + 'd';
	public static final int typeHIPoint = ('h'<<24) + ('i'<<16) + ('p'<<8) + 't';
	public static final int typeHIRect = ('h'<<24) + ('i'<<16) + ('r'<<8) + 'c';
	public static final int typeHISize = ('h'<<24) + ('i'<<16) + ('s'<<8) + 'z';
	public static final int typeLongInteger = ('l'<<24) + ('o'<<16) + ('n'<<8) + 'g';
	public static final int typeMenuCommand = ('m'<<24) + ('c'<<16) + ('m'<<8) + 'd';
	public static final int typeMenuItemIndex = ('m'<<24) + ('i'<<16) + ('d'<<8) + 'x';        
	public static final int typeMenuRef = ('m'<<24) + ('e'<<16) + ('n'<<8) + 'u';
	public static final int typeModalClickResult = ('w'<<24) + ('m'<<16) + ('c'<<8) + 'r';
	public static final int typeMouseButton = ('m'<<24) + ('b'<<16) + ('t'<<8) + 'n';
	public static final int typeMouseWheelAxis = ('m'<<24) + ('w'<<16) + ('a'<<8) + 'x';
	public static final int typeQDPoint = ('Q'<<24) + ('D'<<16) + ('p'<<8) + 't';
	public static final int typeQDRectangle = ('q'<<24) + ('d'<<16) + ('r'<<8) + 't';
	public static final int typeQDRgnHandle = ('r'<<24) + ('g'<<16) + ('n'<<8) + 'h';
	public static final int typeRGBColor = ('c'<<24) + ('R'<<16) + ('G'<<8) + 'B';
	public static final int typeSInt16 = ('s'<<24) + ('h'<<16) + ('o'<<8) + 'r';
	public static final int typeSInt32 = ('l'<<24) + ('o'<<16) + ('n'<<8) + 'g';
	public static final int typeTextRangeArray = ('t'<<24) + ('r'<<16) + ('a'<<8) + 'y';
	public static final int typeType = ('t'<<24) + ('y'<<16) + ('p'<<8) + 'e';
	public static final int typeUInt32 = ('m'<<24) + ('a'<<16) + ('g'<<8) + 'n';
	public static final int typeUnicodeText = ('u'<<24) + ('t'<<16) + ('x'<<8) + 't';
	public static final int typeWildCard = ('w'<<24) + ('i'<<16) + ('l'<<8) + 'd';
	public static final int typeWindowDefPartCode = ('w'<<24) + ('d'<<16) + ('p'<<8) + 't';
	public static final int typeWindowPartCode = ('w'<<24) + ('p'<<16) + ('a'<<8) + 'r';
	public static final int typeWindowModality = ('w'<<24) + ('m'<<16) + ('o'<<8) + 'd';
	public static final int kEventParamWindowPartCode = ('w'<<24) + ('p'<<16) + ('a'<<8) + 'r';
	public static final int typeWindowRef = ('w'<<24) + ('i'<<16) + ('n'<<8) + 'd';
	public static final int typeWindowRegionCode = ('w'<<24) + ('s'<<16) + ('h'<<8) + 'p';
	public static final int updateEvt = 6;
	public static final int updateMask = 1 << updateEvt;
	public static final int userCanceledErr = -128;
	public static final short wInContent = 1;
	public static final short wNoHit = 0;

/** JNI natives */
	
/** @method flags=no_gen */
public static final native int NewGlobalRef(Object object);
/** @method flags=no_gen */
public static final native void DeleteGlobalRef(int globalRef);
/** @method flags=no_gen */
public static final native Object JNIGetObject(int globalRef);

/** Natives */

/** @method flags=no_gen */
public static final native boolean __BIG_ENDIAN__();
/** @method flags=const */
public static final native int kCFRunLoopCommonModes();
/** @method flags=const */
public static final native int kCFRunLoopDefaultMode();
/** @method flags=const address */
public static final native int kCFTypeArrayCallBacks();
/** @method flags=const address */
public static final native int kCFTypeSetCallBacks();
/** @method flags=const */
public static final native int kFontPanelAttributesKey();
/** @method flags=const */
public static final native int kFontPanelAttributeTagsKey();
/** @method flags=const */
public static final native int kFontPanelAttributeSizesKey();
/** @method flags=const */
public static final native int kFontPanelAttributeValuesKey();	
/** @method flags=const */
public static final native int kLSItemContentType();
/** @method flags=const */
public static final native int kUTTagClassFilenameExtension();
/** @method flags=const address */
public static final native int kHIViewWindowContentID();
/** @method flags=const address */
public static final native int kHIViewWindowGrowBoxID();
/** @method flags=const */
public static final native int kPMDocumentFormatPDF();
/** @method flags=const */
public static final native int kPMGraphicsContextCoreGraphics();
public static final native int ActiveNonFloatingWindow();
/** @param idocID cast=(TSMDocumentID) */
public static final native int ActivateTSMDocument(int idocID);
/**
 * @param theAEDescList cast=(const AEDescList *)
 * @param theCount cast=(long *)
 */
public static final native int AECountItems(AEDesc theAEDescList, int[] theCount);
/**
 * @param theAEDesc cast=(AEDesc *)
 * @param result cast=(AEDesc *)
 */
public static final native int AECoerceDesc(int theAEDesc, int toType, AEDesc result);
/**
 * @param typeCode cast=(DescType)
 * @param dataPtr cast=(const void *)
 * @param dataSize cast=(Size)
 */
public static final native int AECreateDesc(int typeCode, byte[] dataPtr, int dataSize, AEDesc result);
public static final native int AEDisposeDesc(AEDesc theAEDesc);
/**
 * @param theAEDesc cast=(AEDesc *)
 * @param dataPtr cast=(void *)
 */
public static final native int AEGetDescData(AEDesc theAEDesc, byte[] dataPtr, int maximumSize);
/**
 * @param theAEDescList cast=(const AEDescList *)
 * @param desiredType cast=(DescType)
 * @param theAEKeyword cast=(AEKeyword *)
 * @param typeCode cast=(DescType *)
 * @param dataPtr cast=(void *)
 * @param maximumSize cast=(Size)
 * @param actualSize cast=(Size *)
 */
public static final native int AEGetNthPtr(AEDesc theAEDescList, int index, int desiredType, int[] theAEKeyword, int[] typeCode, int dataPtr, int maximumSize, int[] actualSize);
/**
 * @param theAppleEvent cast=(const AppleEvent *)
 * @param theAEKeyword cast=(AEKeyword)
 * @param desiredType cast=(DescType)
 * @param result cast=(AEDesc *)
 */
public static final native int AEGetParamDesc (int theAppleEvent, int theAEKeyword, int desiredType, AEDesc result);
/**
 * @param theAEEventClass cast=(AEEventClass)
 * @param theAEEventID cast=(AEEventID)
 * @param handler cast=(AEEventHandlerUPP)
 * @param handlerRefcon cast=(long)
 */
public static final native int AEInstallEventHandler(int theAEEventClass, int theAEEventID, int handler, int handlerRefcon, boolean isSysHandler);  
/**
 * @param theAEEventClass cast=(AEEventClass)
 * @param theAEEventID cast=(AEEventID)
 * @param handler cast=(AEEventHandlerUPP)
 */
public static final native int AERemoveEventHandler (int theAEEventClass, int theAEEventID, int handler, boolean isSysHandler);
/** @param theEventRecord cast=(const EventRecord *) */
public static final native int AEProcessAppleEvent(EventRecord theEventRecord);
/**
 * @param iFile cast=(const FSSpec *)
 * @param iContext cast=(ATSFontContext)
 * @param iFormat cast=(ATSFontFormat)
 * @param iReserved cast=(void *)
 * @param iOptions cast=(ATSOptionFlags)
 * @param oContainer cast=(ATSFontContainerRef *)
 */
public static final native int ATSFontActivateFromFileSpecification(byte[] iFile, int iContext, int iFormat, int iReserved, int iOptions, int[] oContainer);
/**
 * @param iContainer cast=(ATSFontContainerRef)
 * @param iRefCon cast=(void *)
 * @param iOptions cast=(ATSOptionFlags)
 */
public static final native int ATSFontDeactivate(int iContainer, int iRefCon, int iOptions);
/**
 * @param iName cast=(CFStringRef)
 * @param iOptions cast=(ATSOptionFlags)
 */
public static final native int ATSFontFindFromName(int iName, int iOptions);
/**
 * @param iFont cast=(ATSFontRef)
 * @param iOptions cast=(ATSOptionFlags)
 * @param oName cast=(CFStringRef*)
 */
public static final native int ATSFontGetName(int iFont, int iOptions, int[] oName);
/**
 * @param iFont cast=(ATSFontRef)
 * @param iOptions cast=(ATSOptionFlags)
 * @param oName cast=(CFStringRef *)
 */
public static final native int ATSFontGetPostScriptName(int iFont, int iOptions, int[] oName);
/**
 * @param iIterator cast=(ATSFontIterator)
 * @param Font cast=(ATSFontRef *)
 */
public static final native int ATSFontIteratorNext(int iIterator, int[] Font);
/** @param ioIterator cast=(ATSFontIterator *) */
public static final native int ATSFontIteratorRelease(int[] ioIterator);
/**
 * @param iContext cast=(ATSFontContext)
 * @param iFilter cast=(const ATSFontFilter *)
 * @param iRefCon cast=(void *)
 * @param iOptions cast=(ATSOptionFlags)
 * @param ioIterator cast=(ATSFontIterator *)
 */
public static final native int ATSFontIteratorCreate(int iContext, int iFilter, int iRefCon, int iOptions, int[] ioIterator);
/**
 * @param iFont cast=(ATSFontRef)
 * @param iOptions cast=(ATSOptionFlags)
 */
public static final native int ATSFontGetVerticalMetrics(int iFont, int iOptions, ATSFontMetrics oMetrics);
/** @param iFont cast=(ATSFontRef) */
public static final native int ATSFontGetHorizontalMetrics(int iFont, int iOptions, ATSFontMetrics oMetrics);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param oBreakCount cast=(ItemCount *)
 */
public static final native int ATSUBatchBreakLines(int iTextLayout, int iRangeStart, int iRangeLength, int iLineWidth, int[] oBreakCount);
/** @param oStyle cast=(ATSUStyle *) */
public static final native int ATSUCreateStyle(int[] oStyle);
/** @param oTextLayout cast=(ATSUTextLayout *) */
public static final native int ATSUCreateTextLayout(int[] oTextLayout);
/**
 * @param iText cast=(ConstUniCharArrayPtr)
 * @param iRunLengths cast=(const UniCharCount *)
 * @param iStyles cast=(ATSUStyle *)
 * @param oTextLayout cast=(ATSUTextLayout *)
 */
public static final native int ATSUCreateTextLayoutWithTextPtr(int iText, int iTextOffset, int iTextLength, int iTextTotalLength, int iNumberOfRuns, int[] iRunLengths, int[] iStyles, int[] oTextLayout);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param iLineOffset cast=(UniCharArrayOffset)
 * @param iDataSelector cast=(ATSUDirectDataSelector)
 * @param oLayoutDataArrayPtr cast=(void *)
 * @param oLayoutDataCount cast=(ItemCount *)
 */
public static final native int ATSUDirectGetLayoutDataArrayPtrFromTextLayout(int iTextLayout, int iLineOffset, int iDataSelector, int[] oLayoutDataArrayPtr, int[] oLayoutDataCount);
/**
 * @param iLineRef cast=(ATSULineRef)
 * @param iDataSelector cast=(ATSUDirectDataSelector)
 * @param iLayoutDataArrayPtr cast=(void *)
 */
public static final native int ATSUDirectReleaseLayoutDataArrayPtr(int iLineRef, int iDataSelector, int iLayoutDataArrayPtr);
/** @param iStyle cast=(ATSUStyle) */
public static final native int ATSUDisposeStyle(int iStyle);
/** @param iTextLayout cast=(ATSUTextLayout) */
public static final native int ATSUDisposeTextLayout(int iTextLayout);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param iLineOffset cast=(UniCharArrayOffset)
 * @param iLineLength cast=(UniCharCount)
 * @param iLocationX cast=(ATSUTextMeasurement)
 * @param iLocationY cast=(ATSUTextMeasurement)
 */
public static final native int ATSUDrawText(int iTextLayout, int iLineOffset, int iLineLength, int iLocationX, int iLocationY);
/**
 * @param iName cast=(const void *)
 * @param oFontID cast=(ATSUFontID *)
 */
public static final native int ATSUFindFontFromName(byte[] iName, int iNameLength, int iFontNameCode, int iFontNamePlatform, int iFontNameScript, int iFontNameLanguage, int[] oFontID);
/**
 * @param iFontID cast=(ATSUFontID)
 * @param oName cast=(Ptr)
 * @param oActualNameLength cast=(ByteCount *)
 * @param oFontNameIndex cast=(ItemCount *)
 */
public static final native int ATSUFindFontName(int iFontID, int iFontNameCode, int iFontNamePlatform, int iFontNameScript, int iFontNameLanguage, int iMaximumNameLength, byte[] oName, int[] oActualNameLength, int[] oFontNameIndex);
/**
 * @param oFontIDs cast=(ATSUFontID *)
 * @param oFontCount cast=(ItemCount *)
 */
public static final native int ATSUGetFontIDs(int[] oFontIDs, int iArraySize, int[] oFontCount);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param iTextBasePointX cast=(ATSUTextMeasurement)
 * @param iTextBasePointY cast=(ATSUTextMeasurement)
 * @param iBoundsCharStart cast=(UniCharArrayOffset)
 * @param oGlyphBounds cast=(ATSTrapezoid *)
 * @param oActualNumberOfBounds cast=(ItemCount *)
 */
public static final native int ATSUGetGlyphBounds(int iTextLayout, int iTextBasePointX, int iTextBasePointY, int iBoundsCharStart, int iBoundsCharLength, short iTypeOfBounds, int iMaxNumberOfBounds, int oGlyphBounds, int[] oActualNumberOfBounds);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param iTextBasePointX cast=(ATSUTextMeasurement)
 * @param iTextBasePointY cast=(ATSUTextMeasurement)
 * @param iBoundsCharStart cast=(UniCharArrayOffset)
 * @param oGlyphBounds cast=(ATSTrapezoid *)
 * @param oActualNumberOfBounds cast=(ItemCount *)
 */
public static final native int ATSUGetGlyphBounds(int iTextLayout, int iTextBasePointX, int iTextBasePointY, int iBoundsCharStart, int iBoundsCharLength, short iTypeOfBounds, int iMaxNumberOfBounds, ATSTrapezoid oGlyphBounds, int[] oActualNumberOfBounds);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param iTag cast=(ATSUAttributeTag)
 * @param oValue cast=(ATSUAttributeValuePtr)
 * @param oActualValueSize cast=(ByteCount *)
 */
public static final native int ATSUGetLayoutControl(int iTextLayout, int iTag, int iExpectedValueSize, int[] oValue, int[] oActualValueSize);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param iLineStart cast=(UniCharArrayOffset)
 * @param iTag cast=(ATSUAttributeTag)
 * @param iExpectedValueSize cast=(ByteCount)
 * @param oValue cast=(ATSUAttributeValuePtr)
 * @param oActualValueSize cast=(ByteCount *)
 */
public static final native int ATSUGetLineControl(int iTextLayout, int iLineStart, int iTag, int iExpectedValueSize, int[] oValue, int[] oActualValueSize);
/**
 * @param iATSUStyle cast=(ATSUStyle)
 * @param iGlyphID cast=(GlyphID)
 * @param iNewPathProc cast=(ATSQuadraticNewPathUPP)
 * @param iLineProc cast=(ATSQuadraticLineUPP)
 * @param iCurveProc cast=(ATSQuadraticCurveUPP)
 * @param iClosePathProc cast=(ATSQuadraticClosePathUPP)
 * @param iCallbackDataPtr cast=(void *)
 * @param oCallbackResult cast=(OSStatus *)
 */
public static final native int ATSUGlyphGetQuadraticPaths(int iATSUStyle, short iGlyphID, int iNewPathProc, int iLineProc, int iCurveProc, int iClosePathProc, int iCallbackDataPtr, int[] oCallbackResult);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param iRangeStart cast=(UniCharArrayOffset)
 * @param iRangeLength cast=(UniCharCount)
 * @param iMaximumBreaks cast=(ItemCount)
 * @param oBreaks cast=(UniCharArrayOffset *)
 * @param oBreakCount cast=(ItemCount *)
 */
public static final native int ATSUGetSoftLineBreaks(int iTextLayout, int iRangeStart, int iRangeLength, int iMaximumBreaks, int[] oBreaks, int[] oBreakCount);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param oHighlightRegion cast=(RgnHandle)
 */
public static final native int ATSUGetTextHighlight (int iTextLayout, int iTextBasePointX, int iTextBasePointY, int iHighlightStart, int iHighlightLength, int oHighlightRegion);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param oTextBefore cast=(ATSUTextMeasurement *)
 * @param oTextAfter cast=(ATSUTextMeasurement *)
 * @param oAscent cast=(ATSUTextMeasurement *)
 * @param oDescent cast=(ATSUTextMeasurement *)
 */
public static final native int ATSUGetUnjustifiedBounds(int iTextLayout, int iLineStart, int iLineLength,  int[] oTextBefore, int[] oTextAfter, int[] oAscent, int[] oDescent);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param iTextBasePointX cast=(ATSUTextMeasurement)
 * @param iTextBasePointY cast=(ATSUTextMeasurement)
 */
public static final native int ATSUHighlightText(int iTextLayout, int iTextBasePointX, int iTextBasePointY, int iHighlightStart, int iHighlightLength);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param iOldOffset cast=(UniCharArrayOffset)
 * @param iMovementType cast=(ATSUCursorMovementType)
 * @param oNewOffset cast=(UniCharArrayOffset *)
 */
public static final native int ATSUNextCursorPosition(int iTextLayout, int iOldOffset, int iMovementType, int[] oNewOffset);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param oCaretIsSplit cast=(Boolean *)
 */
public static final native int ATSUOffsetToPosition(int iTextLayout, int iOffset, boolean iIsLeading, ATSUCaret oMainCaret, ATSUCaret oSecondCaret, boolean[] oCaretIsSplit);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param ioPrimaryOffset cast=(UniCharArrayOffset *)
 * @param oIsLeading cast=(Boolean *)
 * @param oSecondaryOffset cast=(UniCharArrayOffset *)
 */
public static final native int ATSUPositionToOffset(int iTextLayout, int iLocationX, int iLocationY, int[] ioPrimaryOffset,  boolean[] oIsLeading,  int[] oSecondaryOffset);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param iOldOffset cast=(UniCharArrayOffset)
 * @param iMovementType cast=(ATSUCursorMovementType)
 * @param oNewOffset cast=(UniCharArrayOffset *)
 */
public static final native int ATSUPreviousCursorPosition(int iTextLayout, int iOldOffset, int iMovementType,  int[] oNewOffset);
/**
 * @param iStyle cast=(ATSUStyle)
 * @param iAttributeCount cast=(ItemCount)
 * @param iTag cast=(ATSUAttributeTag *)
 * @param iValueSize cast=(ByteCount *)
 * @param iValue cast=(ATSUAttributeValuePtr *)
 */
public static final native int ATSUSetAttributes(int iStyle, int iAttributeCount, int[] iTag, int[] iValueSize, int[] iValue); 
/**
 * @param iStyle cast=(ATSUStyle)
 * @param iFeatureCount cast=(ItemCount)
 * @param iType cast=(const ATSUFontFeatureType *)
 * @param iSelector cast=(const ATSUFontFeatureSelector *)
 */
public static final native int ATSUSetFontFeatures(int iStyle, int iFeatureCount,  short[] iType, short[] iSelector);
/** @param iTextLayout cast=(ATSUTextLayout) */
public static final native int ATSUSetHighlightingMethod(int iTextLayout, int iMethod, ATSUUnhighlightData iUnhighlightData);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param iAttributeCount cast=(ItemCount)
 * @param iTag cast=(ATSUAttributeTag *)
 * @param iValueSize cast=(ByteCount *)
 * @param iValue cast=(ATSUAttributeValuePtr *)
 */
public static final native int ATSUSetLayoutControls(int iTextLayout, int iAttributeCount, int[] iTag, int[] iValueSize, int[] iValue);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param iLineStart cast=(UniCharArrayOffset)
 * @param iAttributeCount cast=(ItemCount)
 * @param iTag cast=(const ATSUAttributeTag *)
 * @param iValueSize cast=(const ByteCount *)
 * @param iValue cast=(const ATSUAttributeValuePtr *)
 */
public static final native int ATSUSetLineControls(int iTextLayout, int iLineStart, int iAttributeCount, int[] iTag, int[] iValueSize, int[] iValue);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param iStyle cast=(ATSUStyle)
 * @param iRunStart cast=(UniCharArrayOffset)
 * @param iRunLength cast=(UniCharCount)
 */
public static final native int ATSUSetRunStyle(int iTextLayout, int iStyle, int iRunStart, int iRunLength);
/** @param iTextLayout cast=(ATSUTextLayout) */
public static final native int ATSUSetSoftLineBreak(int iTextLayout, int iLineBreak);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param iTabs cast=(const ATSUTab *)
 */
public static final native int ATSUSetTabArray(int iTextLayout, int iTabs, int iTabCount);
/**
 * @param iTextLayout cast=(ATSUTextLayout)
 * @param iText cast=(ConstUniCharArrayPtr)
 * @param iTextOffset cast=(UniCharArrayOffset)
 * @param iTextLength cast=(UniCharCount)
 * @param iTextTotalLength cast=(UniCharCount)
 */
public static final native int ATSUSetTextPointerLocation(int iTextLayout, int iText, int iTextOffset, int iTextLength, int iTextTotalLength);
/** @param iTextLayout cast=(ATSUTextLayout) */
public static final native int ATSUSetTransientFontMatching(int iTextLayout, boolean iTransientFontMatching);
/** @param iTextLayout cast=(ATSUTextLayout) */
public static final native int ATSUTextInserted(int iTextLayout, int iInsertionLocation, int iInsertionLength);
/** @param iTextLayout cast=(ATSUTextLayout) */
public static final native int ATSUTextDeleted(int iTextLayout, int iInsertionLocation, int iInsertionLength);
/**
 * @param cHandle cast=(ControlRef)
 * @param containerID cast=(DataBrowserItemID)
 * @param numItems cast=(UInt32)
 * @param itemIDs cast=(const DataBrowserItemID *)
 * @param preSortProperty cast=(DataBrowserPropertyID)
 */
public static final native int AddDataBrowserItems(int cHandle, int containerID, int numItems, int[] itemIDs, int preSortProperty);
/**
 * @param browser cast=(ControlRef)
 * @param columnDesc cast=(DataBrowserListViewColumnDesc *)
 * @param position cast=(DataBrowserTableViewColumnIndex)
 */
public static final native int AddDataBrowserListViewColumn(int browser, DataBrowserListViewColumnDesc columnDesc, int position);
/**
 * @param theDrag cast=(DragRef)
 * @param theItemRef cast=(DragItemRef)
 * @param theType cast=(FlavorType)
 * @param dataPtr cast=(const void *)
 * @param dataSize cast=(Size)
 * @param theFlags cast=(FlavorFlags)
 */
public static final native int AddDragItemFlavor(int theDrag, int theItemRef, int theType, byte[] dataPtr, int dataSize, int theFlags);  
/**
 * @param mHandle cast=(MenuRef)
 * @param sHandle cast=(CFStringRef)
 * @param attributes cast=(MenuItemAttributes)
 * @param commandID cast=(MenuCommand)
 * @param outItemIndex cast=(MenuItemIndex *)
 */
public static final native int AppendMenuItemTextWithCFString(int mHandle, int sHandle, int attributes, int commandID, short[] outItemIndex);
/**
 * @param inQueue cast=(EventQueueRef)
 * @param inList cast=(const EventTypeSpec *)
 */
public static final native int AcquireFirstMatchingEventInQueue(int inQueue, int inNumTypes, int[] inList, int inOptions);
/** @param cHandle cast=(ControlRef) */
public static final native int AutoSizeDataBrowserListViewColumns(int cHandle);
/** @param wHandle cast=(WindowRef) */
public static final native void BringToFront(int wHandle);
/** @param sHandle cast=(CFTypeRef) */
public static final native void CFRelease(int sHandle);
/** @param sHandle cast=(CFTypeRef) */
public static final native void CFRetain(int sHandle);
/** @param inEventLoop cast=(EventLoopRef) */
public static final native int GetCFRunLoopFromEventLoop(int inEventLoop);
/**
 * @param rl cast=(CFRunLoopRef)
 * @param observer cast=(CFRunLoopObserverRef)
 * @param mode cast=(CFStringRef)
 */
public static final native void CFRunLoopAddObserver(int rl, int observer, int mode);
/**
 * @param rl cast=(CFRunLoopRef)
 * @param source cast=(CFRunLoopSourceRef)
 * @param mode cast=(CFStringRef)
 */
public static final native void CFRunLoopAddSource(int rl, int source, int mode);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param activities cast=(CFOptionFlags)
 * @param order cast=(CFIndex)
 * @param callout cast=(CFRunLoopObserverCallBack)
 * @param context cast=(CFRunLoopObserverContext *)
 */
public static final native int CFRunLoopObserverCreate(int allocator, int activities, boolean repeats, int order, int callout, int context);
/** @param observer cast=(CFRunLoopObserverRef) */
public static final native void CFRunLoopObserverInvalidate(int observer);
/**
 * @param mode cast=(CFStringRef)
 * @param seconds cast=(CFTimeInterval)
 */
public static final native int CFRunLoopRunInMode(int mode, double seconds, boolean returnAfterSourceHandled);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param order cast=(CFIndex)
 */
public static final native int CFRunLoopSourceCreate(int allocator, int order, CFRunLoopSourceContext context);
/** @param source cast=(CFRunLoopSourceRef) */
public static final native void CFRunLoopSourceInvalidate(int source);
/** @param source cast=(CFRunLoopSourceRef) */
public static final native void CFRunLoopSourceSignal(int source);
/** @param rl cast=(CFRunLoopRef) */
public static final native void CFRunLoopStop(int rl);
/** @param rl cast=(CFRunLoopRef) */
public static final native void CFRunLoopWakeUp(int rl);
/**
 * @param theArray cast=(CFMutableArrayRef)
 * @param value cast=(const void *)
 */
public static final native void CFArrayAppendValue(int theArray, int value);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param capacity cast=(CFIndex)
 * @param callBacks cast=(const CFArrayCallBacks *)
 */
public static final native int CFArrayCreateMutable(int allocator, int capacity, int callBacks);
/** @param theArray cast=(CFArrayRef) */
public static final native int CFArrayGetCount(int theArray);
/** @param theArray cast=(CFArrayRef) */
public static final native int CFArrayGetValueAtIndex(int theArray, int idx);
/**
 * @param theSet cast=(CFMutableSetRef)
 * @param value cast=(const void *)
 */
public static final native void CFSetAddValue(int theSet, int value);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param capacity cast=(CFIndex)
 * @param callBacks cast=(const CFSetCallBacks *)
 */
public static final native int CFSetCreateMutable(int allocator, int capacity, int callBacks);
/** @param theSet cast=(CFMutableSetRef) */
public static final native int CFSetGetCount(int theSet);
/**
 * @param theSet cast=(CFMutableSetRef)
 * @param values cast=(const void **)
 */
public static final native void CFSetGetValues(int theSet, int[] values);
/**
 * @param theSet cast=(CFMutableSetRef)
 * @param value cast=(const void *)
 */
public static final native void CFSetRemoveValue(int theSet, int value);
/** 
 * @param allocater cast=(CFAllocatorRef)
 * @param directoryURL cast=(CFURLRef)
 * @param bundleType cast=(CFStringRef)
 */
public static final native int CFBundleCreateBundlesFromDirectory(int allocater, int directoryURL, int bundleType);
/** 
 * @param bundle cast=(CFBundleRef)
 * @param cFBundleDocumentTypes cast=(CFStringRef)
 */
public static final native int CFBundleGetValueForInfoDictionaryKey(int bundle, int cFBundleDocumentTypes);
public static final native int CFBundleGetMainBundle();
/** @param bundle cast=(CFBundleRef) */
public static final native int CFBundleGetIdentifier(int bundle);
/**
 * @param bundle cast=(CFBundleRef)
 * @param packageType cast=(UInt32 *)
 * @param packageCreator cast=(UInt32 *)
 */
public static final native void CFBundleGetPackageInfo(int bundle, int[] packageType, int[] packageCreator);
/**
 * @param theData cast=(CFDataRef)
 * @param range flags=struct
 * @param buffer cast=(UInt8 *)
 */
public static final native void CFDataGetBytes(int theData, CFRange range,  byte[] buffer); 
/** @param theData cast=(CFDataRef) */
public static final native int CFDataGetBytePtr(int theData);
/** @param theData cast=(CFDataRef) */
public static final native int CFDataGetLength(int theData);
/**
 * @param theDict cast=(CFDictionaryRef)
 * @param key cast=(const void *)
 * @param value cast=(const void **)
 */
public static final native boolean CFDictionaryGetValueIfPresent(int theDict, int key, int[] value);
/**
 * @param theString cast=(CFStringRef)
 * @param theOtherString cast=(CFStringRef)
 */
public static final native boolean CFEqual(int theString, int theOtherString);
public static final native int CFLocaleCopyCurrent();
/**
 * @param formatter cast=(CFNumberFormatterRef)
 * @param key cast=(CFStringRef)
 */
public static final native int CFNumberFormatterCopyProperty(int formatter, int key);
/**
 * @param alloc cast=(CFAllocatorRef)
 * @param locale cast=(CFLocaleRef)
 * @param style cast=(CFNumberFormatterStyle)
 */
public static final native int CFNumberFormatterCreate(int alloc, int locale, int style);
/**
 * @param alloc cast=(CFAllocatorRef)
 * @param bytes cast=(const UInt8 *)
 * @param numBytes cast=(CFIndex)
 * @param encoding cast=(CFStringEncoding)
 */
public static final native int CFStringCreateWithBytes(int alloc, byte[] bytes, int numBytes, int encoding, boolean isExternalRepresentation);
/**
 * @param alloc cast=(CFAllocatorRef)
 * @param chars cast=(const UniChar *)
 * @param numChars cast=(CFIndex)
 */
public static final native int CFStringCreateWithCharacters(int alloc, char[] chars, int numChars);
/**
 * @param alloc cast=(CFAllocatorRef)
 * @param chars cast=(const UniChar *)
 * @param numChars cast=(CFIndex)
 */
public static final native int CFStringCreateWithCharacters(int alloc, int chars, int numChars);
/**
 * @param theString cast=(CFStringRef)
 * @param range cast=(CFRange *),flags=struct
 * @param encoding cast=(CFStringEncoding)
 * @param lossByte cast=(UInt8)
 * @param isExternalRepresentation cast=(Boolean)
 * @param buffer cast=(UInt8 *)
 * @param maxBufLen cast=(CFIndex)
 * @param usedBufLen cast=(CFIndex *)
 */
public static final native int CFStringGetBytes(int theString, CFRange range, int encoding, byte lossByte, boolean isExternalRepresentation, byte[] buffer, int maxBufLen, int[] usedBufLen);
/**
 * @param theString cast=(CFStringRef)
 * @param range cast=(CFRange *),flags=struct
 * @param buffer cast=(UniChar *)
 */
public static final native void CFStringGetCharacters(int theString, CFRange range, char[] buffer);
/** @param theString cast=(CFStringRef) */
public static final native int CFStringGetLength(int theString);
public static final native int CFStringGetSystemEncoding();
/**
 * @param anURL cast=(CFURLRef)
 * @param pathStyle cast=(CFURLPathStyle)
 */
public static final native int CFURLCopyFileSystemPath(int anURL, int pathStyle);
/** @param url cast=(CFURLRef) */
public static final native int CFURLCopyLastPathComponent(int url);
/** @param url cast=(CFURLRef) */
public static final native int CFURLCopyPathExtension(int url);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param url cast=(CFURLRef)
 * @param encoding cast=(CFStringEncoding)
 * @param escapeWhitespace cast=(Boolean)
 */
public static final native int CFURLCreateData(int allocator, int url, int encoding, boolean escapeWhitespace);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param url cast=(CFURLRef)
 * @param pathComponent cast=(CFStringRef)
 * @param isDirectory cast=(Boolean)
 */
public static final native int CFURLCreateCopyAppendingPathComponent(int allocator, int url, int pathComponent, boolean isDirectory);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param url cast=(CFURLRef)
 * @param extension cast=(CFStringRef)
 */
public static final native int CFURLCreateCopyAppendingPathExtension(int allocator, int url, int extension);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param url cast=(CFURLRef)
 */
public static final native int CFURLCreateCopyDeletingLastPathComponent(int allocator, int url);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param fsRef cast=(const struct FSRef *)
 */
public static final native int CFURLCreateFromFSRef(int allocator, byte[] fsRef);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param URLBytes cast=(const UInt8 *)
 * @param length cast=(CFIndex)
 * @param encoding cast=(CFStringEncoding)
 * @param baseURL cast=(CFURLRef)
 */
public static final native int CFURLCreateWithBytes(int allocator, byte[] URLBytes, int length, int encoding, int baseURL);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param filePath cast=(CFStringRef)
 * @param pathStyle cast=(CFURLPathStyle)
 */
public static final native int CFURLCreateWithFileSystemPath (int allocator, int filePath, int pathStyle, boolean isDirectory);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param buffer cast=(const UInt8 *)
 */
public static final native int CFURLCreateFromFileSystemRepresentation(int allocator, int buffer, int bufLen, boolean isDirectory);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param URLString cast=(CFStringRef)
 * @param baseURL cast=(CFURLRef)
 */
public static final native int CFURLCreateWithString(int allocator, int URLString, int baseURL);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param originalString cast=(CFStringRef)
 * @param charactersToLeaveUnescaped cast=(CFStringRef)
 * @param legalURLCharactersToBeEscaped cast=(CFStringRef)
 */
public static final native int CFURLCreateStringByAddingPercentEscapes(int allocator, int originalString, int charactersToLeaveUnescaped, int legalURLCharactersToBeEscaped, int encoding);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param originalString cast=(CFStringRef)
 * @param charactersToLeaveUnescaped cast=(CFStringRef)
 */
public static final native int CFURLCreateStringByReplacingPercentEscapes(int allocator, int originalString, int charactersToLeaveUnescaped);
/**
 * @param url cast=(CFURLRef)
 * @param fsRef cast=(struct FSRef *)
 */
public static final native boolean CFURLGetFSRef(int url, byte[] fsRef);
/** @method flags=no_gen */
public static final native void CGAffineTransformConcat (float[] t1, float[] t2, float[] result);
/** @method flags=no_gen */
public static final native void CGAffineTransformMake (float a, float b, float c, float d, float tx, float ty, float[] result);
/** @method flags=no_gen */
public static final native void CGAffineTransformTranslate (float[] t, float tx, float ty, float[] result);
/** @method flags=no_gen */
public static final native void CGAffineTransformRotate (float[] t, float angle, float[] result);
/** @method flags=no_gen */
public static final native void CGAffineTransformScale (float[] t, float sx, float sy, float[] result);
/** @method flags=no_gen */
public static final native void CGAffineTransformInvert (float[] t, float[] result);
/**
 * @param inContext cast=(CGContextRef)
 * @param sx cast=(float)
 * @param sy cast=(float)
 */
public static final native void CGContextScaleCTM(int inContext, float sx, float sy);
/**
 * @param inContext cast=(CGContextRef)
 * @param tx cast=(float)
 * @param ty cast=(float)
 */
public static final native void CGContextTranslateCTM(int inContext, float tx, float ty);
/**
 * @param data cast=(void *)
 * @param width cast=(size_t)
 * @param height cast=(size_t)
 * @param bitsPerComponent cast=(size_t)
 * @param bytesPerRow cast=(size_t)
 * @param colorspace cast=(CGColorSpaceRef)
 * @param alphaInfo cast=(CGImageAlphaInfo)
 */
public static final native int CGBitmapContextCreate(int data, int width, int height, int bitsPerComponent, int bytesPerRow, int colorspace, int alphaInfo);
/** @param colorspace cast=(CGColorSpaceRef) */
public static final native int CGColorCreate(int colorspace, float[] components);
/** @param color cast=(CGColorRef) */
public static final native void CGColorRelease(int color);
/** @param baseSpace cast=(CGColorSpaceRef) */
public static final native int CGColorSpaceCreatePattern(int baseSpace);
public static final native int CGColorSpaceCreateDeviceRGB ();
/** @method flags=dynamic */
public static final native int CGBitmapContextCreateImage(int context);
/** @param cs cast=(CGColorSpaceRef) */
public static final native void CGColorSpaceRelease (int cs);
/**
 * @param ctx cast=(CGContextRef)
 * @param x cast=(float)
 * @param y cast=(float)
 * @param radius cast=(float)
 * @param startAngle cast=(float)
 * @param endAngle cast=(float)
 * @param clockwise cast=(Boolean)
 */
public static final native void CGContextAddArc (int ctx, float x, float y, float radius, float startAngle, float endAngle, boolean clockwise);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextAddArcToPoint (int ctx, float x1, float y1, float x2, float y2, float radius);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextAddLineToPoint (int ctx, float x, float y);
/**
 * @param ctx cast=(CGContextRef)
 * @param points cast=(const CGPoint *)
 * @param count cast=(size_t)
 */
public static final native void CGContextAddLines (int ctx, float[] points, int count);
/**
 * @param context cast=(CGContextRef)
 * @param rect flags=struct
 */
public static final native void CGContextAddRect (int context, CGRect rect);
/**
 * @param context cast=(CGContextRef)
 * @param path cast=(CGPathRef)
 */
public static final native void CGContextAddPath (int context, int path);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextBeginPath (int ctx);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextClip (int ctx);
/**
 * @param ctx cast=(CGContextRef)
 * @param rect cast=(CGRect *),flags=struct
 */
public static final native void CGContextClearRect (int ctx, CGRect rect);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextClosePath (int ctx);
/**
 * @param context cast=(CGContextRef)
 * @param transform cast=(CGAffineTransform *),flags=struct
 */
public static final native void CGContextConcatCTM (int context, float[] transform);
/**
 * @param ctx cast=(CGContextRef)
 * @param rect cast=(CGRect *),flags=struct
 * @param image cast=(CGImageRef)
 */
public static final native void CGContextDrawImage (int ctx, CGRect rect, int image);
/**
 * @param context cast=(CGContextRef)
 * @param shading cast=(CGShadingRef)
 */
public static final native void CGContextDrawShading (int context, int shading);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextEOClip (int ctx);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextEOFillPath (int ctx);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextFillPath (int ctx);
/**
 * @param ctx cast=(CGContextRef)
 * @param rect cast=(CGRect *),flags=struct
 */
public static final native void CGContextStrokeRect (int ctx, CGRect rect);
/**
 * @param ctx cast=(CGContextRef)
 * @param rect cast=(CGRect *),flags=struct
 */
public static final native void CGContextFillRect (int ctx, CGRect rect);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextFlush (int ctx);
/** @method flags=no_gen */
public static final native void CGContextGetCTM (int context, float[] result);
/** @param context cast=(CGContextRef) */
public static final native int CGContextGetInterpolationQuality (int context	);
/**
 * @method flags=no_gen
 * @param ctx cast=(CGContextRef)
 */
public static final native void CGContextGetPathBoundingBox(int ctx, CGRect rect);
/**
 * @method flags=no_gen
 * @param ctx cast=(CGContextRef)
 * @param point cast=(CGPoint)
 */
public static final native void CGContextGetTextPosition (int ctx, CGPoint point);
/**
 * @param ctx cast=(CGContextRef)
 * @param x cast=(float)
 * @param y cast=(float)
 */
public static final native void CGContextMoveToPoint (int ctx, float x, float y);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextRelease(int ctx);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextRotateCTM(int ctx, float angle);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextRestoreGState(int ctx);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextSaveGState(int ctx);
/**
 * @param ctx cast=(CGContextRef)
 * @param name cast=(const char *)
 * @param size cast=(float)
 * @param textEncoding cast=(CGTextEncoding)
 */
public static final native void CGContextSelectFont (int ctx, byte[] name, float size, int textEncoding);
/**
 * @param ctx cast=(CGContextRef)
 * @param colorspace cast=(CGColorSpaceRef)
 */
public static final native void CGContextSetFillColorSpace (int ctx, int colorspace);
/**
 * @param context cast=(CGContextRef)
 * @param pattern cast=(CGPatternRef)
 */
public static final native void CGContextSetFillPattern (int context, int pattern, float[] components);
/** @param context cast=(CGContextRef) */
public static final native void CGContextSetAlpha (int context, float alpha);
/**
 * @method flags=dynamic
 * @param context cast=(CGContextRef)
 */
public static final native void CGContextSetBlendMode(int context, int mode);
/**
 * @param ctx cast=(CGContextRef)
 * @param value cast=(const float *)
 */
public static final native void CGContextSetFillColor (int ctx, float[] value);
/**
 * @param ctx cast=(CGContextRef)
 * @param font cast=(CGFontRef)
 */
public static final native void CGContextSetFont (int ctx, int font);
/**
 * @param ctx cast=(CGContextRef)
 * @param size cast=(float)
 */
public static final native void CGContextSetFontSize (int ctx, float size);
/** @param context cast=(CGContextRef) */
public static final native void CGContextSetInterpolationQuality (int context, int quality);
/** @param context cast=(CGContextRef) */
public static final native void CGContextSetLineCap (int context, int cap); 
/**
 * @param ctx cast=(CGContextRef)
 * @param phase cast=(float)
 * @param lengths cast=(const float *)
 * @param count cast=(size_t)
 */
public static final native void CGContextSetLineDash (int ctx, float phase, float[] lengths, int count);
/** @param context cast=(CGContextRef) */
public static final native void CGContextSetLineJoin (int context, int join); 
/**
 * @param ctx cast=(CGContextRef)
 * @param width cast=(float)
 */
public static final native void CGContextSetLineWidth (int ctx, float width);
/** @param context cast=(CGContextRef) */
public static final native void CGContextSetMiterLimit (int context, float limit);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextSetShouldAntialias (int ctx, boolean shouldAntialias);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextSetShouldSmoothFonts (int ctx, boolean shouldSmooth);
/**
 * @param ctx cast=(CGContextRef)
 * @param colorspace cast=(CGColorSpaceRef)
 */
public static final native void CGContextSetStrokeColorSpace (int ctx, int colorspace);
/**
 * @param ctx cast=(CGContextRef)
 * @param value cast=(const float *)
 */
public static final native void CGContextSetStrokeColor (int ctx, float[] value);
/**
 * @param context cast=(CGContextRef)
 * @param pattern cast=(CGPatternRef)
 */
public static final native void CGContextSetStrokePattern (int context, int pattern, float[] components);
/** @param context cast=(CGContextRef) */
public static final native void CGContextSetRenderingIntent (int context, int intent);
/**
 * @param ctx cast=(CGContextRef)
 * @param r cast=(float)
 * @param g cast=(float)
 * @param b cast=(float)
 * @param alpha cast=(float)
 */
public static final native void CGContextSetRGBFillColor (int ctx, float r, float g, float b, float alpha);
/**
 * @param ctx cast=(CGContextRef)
 * @param r cast=(float)
 * @param g cast=(float)
 * @param b cast=(float)
 * @param alpha cast=(float)
 */
public static final native void CGContextSetRGBStrokeColor (int ctx, float r, float g, float b, float alpha);
/**
 * @param ctx cast=(CGContextRef)
 * @param mode cast=(CGTextDrawingMode)
 */
public static final native void CGContextSetTextDrawingMode (int ctx, int mode);
/**
 * @param ctx cast=(CGContextRef)
 * @param x cast=(float)
 * @param y cast=(float)
 */
public static final native void CGContextSetTextPosition (int ctx, float x, float y);
/**
 * @param ctx cast=(CGContextRef)
 * @param cstring cast=(const char *)
 * @param length cast=(size_t)
 */
public static final native void CGContextShowText (int ctx, byte[] cstring, int length);
/**
 * @param ctx cast=(CGContextRef)
 * @param x cast=(float)
 * @param y cast=(float)
 * @param cstring cast=(const char *)
 * @param length cast=(size_t)
 */
public static final native void CGContextShowTextAtPoint (int ctx, float x, float y, byte[] cstring, int length);
/**
 * @param ctx cast=(CGContextRef)
 * @param transform cast=(CGAffineTransform *),flags=struct
 */
public static final native void CGContextSetTextMatrix (int ctx, float[] transform);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextStrokePath (int ctx);
/** @param ctx cast=(CGContextRef) */
public static final native void CGContextSynchronize (int ctx);
public static final native boolean CGCursorIsVisible ();
/**
 * @param info cast=(void *)
 * @param domainDimension cast=(size_t)
 * @param domain cast=(const float *)
 * @param rangeDimension cast=(size_t)
 * @param range cast=(const float *)
 * @param callbacks cast=(const CGFunctionCallbacks *)
 */
public static final native int CGFunctionCreate (int info, int domainDimension, float[] domain, int rangeDimension, float[] range, CGFunctionCallbacks callbacks);
/** @param function cast=(CGFunctionRef) */
public static final native void CGFunctionRelease (int function);
/**
 * @param info cast=(void *)
 * @param data cast=(const void *)
 * @param size cast=(size_t)
 * @param releaseData cast=(void *)
 */
public static final native int CGDataProviderCreateWithData (int info, int data, int size, int releaseData);
/** @param url cast=(CFURLRef) */
public static final native int CGDataProviderCreateWithURL (int url);
/** @param provider cast=(CGDataProviderRef) */
public static final native void CGDataProviderRelease (int provider);
/** @param display cast=(CGDirectDisplayID) */
public static final native int CGDisplayBaseAddress (int display);
/** @param display cast=(CGDirectDisplayID) */
public static final native int CGDisplayBitsPerPixel (int display);
/** @param display cast=(CGDirectDisplayID) */
public static final native int CGDisplayBitsPerSample (int display);
/** @param display cast=(CGDirectDisplayID) */
public static final native int CGDisplayBytesPerRow (int display);
/** @param display cast=(CGDirectDisplayID) */
public static final native int CGDisplayPixelsHigh (int display);
/** @param display cast=(CGDirectDisplayID) */
public static final native int CGDisplayPixelsWide (int display);
/** @param display cast=(CGDirectDisplayID) */
public static final native int CGDisplayHideCursor(int display);
/** @param display cast=(CGDirectDisplayID) */
public static final native int CGDisplayShowCursor(int display);
/**
 * @method flags=no_gen
 * @param display cast=(CGDirectDisplayID)
 * @param result flags=struct
 */
public static final native void CGDisplayBounds(int display, CGRect result);
public static final native int CGMainDisplayID();
public static final native int CGFontCreateWithPlatformFont (int[] platformFontReference);
/** @param font cast=(CGFontRef) */
public static final native void CGFontRelease (int font);
/**
 * @param rect flags=struct
 * @param maxDisplays cast=(CGDisplayCount)
 * @param dspys cast=(CGDirectDisplayID *)
 * @param dspyCnt cast=(CGDisplayCount *)
 */
public static final native int CGGetDisplaysWithRect (CGRect rect, int maxDisplays, int[] dspys, int[] dspyCnt);
/**
 * @param width cast=(size_t)
 * @param height cast=(size_t)
 * @param bitsPerComponent cast=(size_t)
 * @param bitsPerPixel cast=(size_t)
 * @param bytesPerRow cast=(size_t)
 * @param colorspace cast=(CGColorSpaceRef)
 * @param alphaInfo cast=(CGImageAlphaInfo)
 * @param provider cast=(CGDataProviderRef)
 * @param decode cast=(const float *)
 * @param shouldInterpolate cast=(Boolean)
 * @param intent cast=(CGColorRenderingIntent)
 */
public static final native int CGImageCreate (int width, int height, int bitsPerComponent, int bitsPerPixel, int bytesPerRow, int colorspace, int alphaInfo, int provider, float[] decode, boolean shouldInterpolate, int intent);
/**
 * @method flags=dynamic
 */
public static final native int CGImageCreateCopy (int image);
/**
 * @method flags=dynamic
 * @param rect flags=struct
 */
public static final native int CGImageCreateWithImageInRect(int image, CGRect rect);
/** @param source cast=(CGDataProviderRef) */
public static final native int CGImageCreateWithPNGDataProvider (int source, float[] decode, boolean shouldInterpolate, int intent);
/** @param source cast=(CGDataProviderRef) */
public static final native int CGImageCreateWithJPEGDataProvider (int source, float[] decode, boolean shouldInterpolate, int intent);
/** @param image cast=(CGImageRef) */
public static final native int CGImageGetAlphaInfo (int image);
/** @param image cast=(CGImageRef) */
public static final native int CGImageGetBitsPerComponent (int image);
/** @param image cast=(CGImageRef) */
public static final native int CGImageGetBitsPerPixel (int image);
/** @param image cast=(CGImageRef) */
public static final native int CGImageGetBytesPerRow (int image);
/** @param image cast=(CGImageRef) */
public static final native int CGImageGetDataProvider (int image);
/** @param image cast=(CGImageRef) */
public static final native int CGImageGetColorSpace (int image);
/** @param image cast=(CGImageRef) */
public static final native int CGImageGetHeight (int image);
/** @param image cast=(CGImageRef) */
public static final native int CGImageGetWidth (int image);
/** @param image cast=(CGImageRef) */
public static final native void CGImageRelease (int image);
/**
 * @param path cast=(CGMutablePathRef)
 * @param m cast=(const CGAffineTransform *)
 */
public static final native void CGPathAddArc (int path, float[] m, float x, float y, float r, float startAngle, float endAngle, boolean clockwise);
/**
 * @param path cast=(CGMutablePathRef)
 * @param m cast=(const CGAffineTransform *)
 */
public static final native void CGPathAddCurveToPoint (int path, float[] m, float cx1, float cy1, float cx2, float cy2, float x, float y);
/**
 * @param path cast=(CGMutablePathRef)
 * @param m cast=(const CGAffineTransform *)
 */
public static final native void CGPathAddLineToPoint (int path, float[] m, float x, float y);
/**
 * @param path1 cast=(CGMutablePathRef)
 * @param m cast=(const CGAffineTransform *)
 * @param path2 cast=(CGPathRef)
 */
public static final native void CGPathAddPath (int path1, float[] m, int path2);
/**
 * @param path cast=(CGMutablePathRef)
 * @param m cast=(const CGAffineTransform *)
 */
public static final native void CGPathAddQuadCurveToPoint (int path, float[] m, float cx, float cy, float x, float y);
/**
 * @param path cast=(CGMutablePathRef)
 * @param m cast=(const CGAffineTransform *)
 * @param rect flags=struct
 */
public static final native void CGPathAddRect (int path, float[] m, CGRect rect);
/**
 * @param path cast=(CGPathRef)
 * @param info cast=(void *)
 * @param function cast=(CGPathApplierFunction)
 */
public static final native void CGPathApply (int path, int info, int function);
/** @param path cast=(CGMutablePathRef) */
public static final native void CGPathCloseSubpath (int path);
public static final native int CGPathCreateMutable ();
/** @param path cast=(CGPathRef) */
public static final native int CGPathCreateMutableCopy (int path);
/**
 * @method flags=no_gen
 * @param path cast=(CGPathRef)
 */
public static final native void CGPathGetBoundingBox (int path, CGRect rect);
/**
 * @method flags=no_gen
 * @param path cast=(CGPathRef)
 */
public static final native void CGPathGetCurrentPoint (int path, CGPoint point);
/** @param path cast=(CGPathRef) */
public static final native boolean CGPathIsEmpty (int path);
/**
 * @param path cast=(CGMutablePathRef)
 * @param m cast=(const CGAffineTransform *)
 */
public static final native void CGPathMoveToPoint (int path, float[] m, float x, float y);
/** @param path cast=(CGPathRef) */
public static final native void CGPathRelease (int path);
/**
 * @param info cast=(void *)
 * @param bounds flags=struct
 * @param matrix cast=(CGAffineTransform *),flags=struct
 * @param tiling cast=(CGPatternTiling)
 * @param callbacks cast=(const CGPatternCallbacks *)
 */
public static final native int CGPatternCreate (int info, CGRect bounds, float[] matrix, float xStep, float yStep, int tiling, int isColored, CGPatternCallbacks callbacks);
/** @param pattern cast=(CGPatternRef) */
public static final native void CGPatternRelease (int pattern);
/** @method flags=no_gen */
public static final native void CGPointApplyAffineTransform (CGPoint point, float[] t, CGPoint result);
/**
 * @param mouseCursorPosition flags=struct
 * @param updateMouseCursorPosition cast=(boolean_t)
 * @param mouseButtonDown cast=(boolean_t)
 * @param mouseButtonDown2 cast=(boolean_t)
 * @param mouseButtonDown3 cast=(boolean_t)
 * @param mouseButtonDown4 cast=(boolean_t)
 * @param mouseButtonDown5 cast=(boolean_t)
 */
public static final native int CGPostMouseEvent(CGPoint mouseCursorPosition, boolean updateMouseCursorPosition, int buttonCount, boolean mouseButtonDown, boolean mouseButtonDown2, boolean mouseButtonDown3, boolean mouseButtonDown4, boolean mouseButtonDown5);
/**
 * @param keyChar cast=(CGCharCode)
 * @param virtualKey cast=(CGKeyCode)
 * @param keyDown cast=(boolean_t)
 */
public static final native int CGPostKeyboardEvent(int keyChar, int virtualKey, boolean keyDown);
public static final native int CGPostScrollWheelEvent(int wheelCount, int wheel1);
/**
 * @param rect flags=struct
 * @param point flags=struct
 */
public static final native int CGRectContainsPoint(CGRect rect, CGPoint point);
/**
 * @method flags=no_gen
 * @param r1 flags=struct no_out
 * @param r2 flags=struct no_out
 * @param result flags=struct no_in
 */
public static final native void CGRectUnion (CGRect r1, CGRect r2, CGRect result);
/**
 * @param colorspace cast=(CGColorSpaceRef)
 * @param start flags=struct
 * @param end flags=struct
 * @param function cast=(CGFunctionRef)
 */
public static final native int CGShadingCreateAxial (int colorspace, CGPoint start, CGPoint end, int function, boolean extendStart, boolean extendEnd);
/**
 * @param colorspace cast=(CGColorSpaceRef)
 * @param start flags=struct
 * @param end flags=struct
 * @param function cast=(CGFunctionRef)
 */
public static final native int CGShadingCreateRadial (int colorspace, CGPoint start, float startRadius, CGPoint end, float endRadius, int function, boolean extendStart, boolean extendEnd);
/** @param shading cast=(CGShadingRef) */
public static final native void CGShadingRelease (int shading);
/** @method flags=no_gen */
public static final native void CGSizeApplyAffineTransform (CGSize size, float[] t, CGSize result);
/** @param newCursorPosition flags=struct */
public static final native int CGWarpMouseCursorPosition (CGPoint newCursorPosition);
/**
 * @param menu cast=(MenuRef)
 * @param item cast=(MenuItemIndex)
 * @param setTheseAttributes cast=(MenuItemAttributes)
 * @param clearTheseAttributes cast=(MenuItemAttributes)
 */
public static final native int ChangeMenuItemAttributes(int menu, int item, int setTheseAttributes, int clearTheseAttributes);
/**
 * @param windowHandle cast=(WindowRef)
 * @param setAttributes cast=(WindowAttributes)
 * @param clearAttributes cast=(WindowAttributes)
 */
public static final native int ChangeWindowAttributes(int windowHandle, int setAttributes, int clearAttributes);
public static final native int CPSEnableForegroundOperation(int[] psn, int arg2, int arg3, int arg4, int arg5);
public static final native int CPSSetProcessName(int[] psn, byte[] name);
/**
 * @param nextHandler cast=(EventHandlerCallRef)
 * @param eventRefHandle cast=(EventRef)
 */
public static final native int CallNextEventHandler(int nextHandler, int eventRefHandle);
public static final native void Call(int proc, int arg1, int arg2);
/** @param theMenu cast=(MenuRef) */
public static final native void CalcMenuSize(int theMenu);
/** @param inRootMenu cast=(MenuRef) */
public static final native int CancelMenuTracking(int inRootMenu, boolean inImmediate, int inDismissalReason); 
public static final native int ClearCurrentScrap();
/** @param inWindow cast=(WindowRef) */
public static final native int ClearKeyboardFocus(int inWindow);
public static final native void ClearMenuBar();
/**
 * @param inContext cast=(CGContextRef)
 * @param portRect cast=(const Rect *)
 * @param rgnHandle cast=(RgnHandle)
 */
public static final native int ClipCGContextToRegion(int inContext, Rect portRect, int rgnHandle);
/**
 * @param cHandle cast=(ControlRef)
 * @param container cast=(DataBrowserItemID)
 */
public static final native int CloseDataBrowserContainer(int cHandle, int container);
public static final native void ClosePicture();
/** @param dstRgn cast=(RgnHandle) */
public static final native void CloseRgn(int dstRgn);
/**
 * @param wHandle cast=(WindowRef)
 * @param collapse cast=(Boolean)
 */
public static final native int CollapseWindow(int wHandle, boolean collapse);
/**
 * @param inMenu cast=(MenuRef)
 * @param inGlobalLocation flags=struct
 * @param inHelpItemString cast=(ConstStr255Param)
 * @param outUserSelectionType cast=(UInt32 *)
 * @param outMenuID cast=(SInt16 *)
 * @param outMenuItem cast=(MenuItemIndex *)
 */
public static final native int ContextualMenuSelect (int inMenu, Point inGlobalLocation, boolean inReserved, int  inHelpType, byte[] inHelpItemString, AEDesc inSelection, int[] outUserSelectionType, short[] outMenuID, short[] outMenuItem);
/**
 * @param inEvent cast=(EventRef)
 * @param outEvent cast=(EventRecord *)
 */
public static final native boolean ConvertEventRefToEventRecord(int inEvent, EventRecord outEvent);
/**
 * @param iTextToUnicodeInfo cast=(TextToUnicodeInfo)
 * @param iPascalStr cast=(ConstStr255Param)
 * @param oUnicodeLen cast=(ByteCount *)
 */
public static final native int ConvertFromPStringToUnicode(int iTextToUnicodeInfo, byte[] iPascalStr, int iOutputBufLen, int[] oUnicodeLen, char[] oUnicodeStr);
/**
 * @param iUnicodeToTextInfo cast=(UnicodeToTextInfo)
 * @param iUnicodeStr cast=(ConstUniCharArrayPtr)
 * @param oPascalStr cast=(unsigned char *)
 */
public static final native int ConvertFromUnicodeToPString (int iUnicodeToTextInfo, int iUnicodeLen, char[] iUnicodeStr, byte[] oPascalStr); 
/**
 * @param srcPixMapHandle cast=(const BitMap *)
 * @param dstPixMapHandle cast=(const BitMap *)
 * @param srcRect cast=(const Rect *)
 * @param dstRect cast=(const Rect *)
 * @param mode cast=(short)
 * @param maskRgn cast=(RgnHandle)
 */
public static final native void CopyBits(int srcPixMapHandle, int dstPixMapHandle, Rect srcRect, Rect dstRect, short mode, int maskRgn);
/**
 * @param cHandle cast=(ControlRef)
 * @param sHandle cast=(CFStringRef *)
 */
public static final native int CopyControlTitleAsCFString(int cHandle, int[] sHandle);
/**
 * @param mHandle cast=(MenuRef)
 * @param index cast=(MenuItemIndex)
 * @param sHandle cast=(CFStringRef *)
 */
public static final native int CopyMenuItemTextAsCFString(int mHandle, short index, int[] sHandle);
/**
 * @param srcRgnHandle cast=(RgnHandle)
 * @param dstRgnHandle cast=(RgnHandle)
 */
public static final native void CopyRgn(int srcRgnHandle, int dstRgnHandle);
/**
 * @param theDrag cast=(DragRef)
 * @param numItems cast=(UInt16 *)
 */
public static final native int CountDragItems(int theDrag, short[] numItems);
/**
 * @param theDrag cast=(DragRef)
 * @param theItemRef cast=(DragItemRef)
 * @param numFlavors cast=(UInt16 *)
 */
public static final native int CountDragItemFlavors(int theDrag, int theItemRef, short[] numFlavors);
/** @param mHandle cast=(MenuRef) */
public static final native short CountMenuItems(int mHandle);
/**
 * @param cHandle cast=(ControlRef)
 * @param count cast=(UInt16 *)
 */
public static final native int CountSubControls(int cHandle, short[] count);
/**
 * @param window cast=(WindowRef)
 * @param boundsRect cast=(const Rect *)
 * @param title cast=(CFStringRef)
 * @param thickness cast=(ControlBevelThickness)
 * @param behavior cast=(ControlBevelButtonBehavior)
 * @param info cast=(ControlButtonContentInfoPtr)
 * @param menuID cast=(SInt16)
 * @param menuBehavior cast=(ControlBevelButtonMenuBehavior)
 * @param menuPlacement cast=(ControlBevelButtonMenuPlacement)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateBevelButtonControl(int window, Rect boundsRect, int title, short thickness, short behavior, int info, short menuID, short menuBehavior, short menuPlacement, int[] outControl);
/**
 * @param window cast=(WindowRef)
 * @param boundsRect cast=(const Rect *)
 * @param clockType cast=(ControlClockType)
 * @param clockFlags cast=(ControlClockFlags)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateClockControl(int window, Rect boundsRect, int clockType, int clockFlags, int[] outControl);
/**
 * @param window cast=(WindowRef)
 * @param boundsRect cast=(const Rect *)
 * @param title cast=(CFStringRef)
 * @param initialValue cast=(SInt32)
 * @param autoToggle cast=(Boolean)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateCheckBoxControl(int window, Rect boundsRect, int title, int initialValue, boolean autoToggle, int[] outControl);
/**
 * @param inPort cast=(CGrafPtr)
 * @param outContext cast=(CGContextRef *)
 */
public static final native int CreateCGContextForPort(int inPort, int[] outContext);
/**
 * @param window cast=(WindowRef)
 * @param boundsRect cast=(const Rect *)
 * @param style cast=(DataBrowserViewStyle)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateDataBrowserControl(int window, Rect boundsRect, int style,int[] outControl);
/**
 * @param allocator cast=(CFAllocatorRef)
 * @param inClassID cast=(UInt32)
 * @param kind cast=(UInt32)
 * @param when cast=(EventTime)
 * @param flags cast=(EventAttributes)
 * @param outEventRef cast=(EventRef *)
 */
public static final native int CreateEvent(int allocator, int inClassID, int kind, double when, int flags, int[] outEventRef);
/**
 * @param window cast=(WindowRef)
 * @param boundsRect cast=(const Rect *)
 * @param title cast=(CFStringRef)
 * @param primary cast=(Boolean)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateGroupBoxControl(int window, Rect boundsRect, int title, boolean primary, int[] outControl);
/**
 * @param window cast=(WindowRef)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateIconControl(int window, Rect boundsRect, ControlButtonContentInfo icon, boolean dontTrack, int[] outControl);
/**
 * @param window cast=(WindowRef)
 * @param boundsRect cast=(const Rect *)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateLittleArrowsControl(int window, Rect boundsRect, int value, int minimum, int maximum, int increment, int[] outControl);
/**
 * @param menuID cast=(MenuID)
 * @param menuAttributes cast=(MenuAttributes)
 * @param outMenuRef cast=(MenuRef *)
 */
public static final native int CreateNewMenu(short menuID, int menuAttributes, int[] outMenuRef);
/**
 * @param windowClass cast=(WindowClass)
 * @param attributes cast=(WindowAttributes)
 * @param bounds cast=(const Rect *)
 * @param wHandle cast=(WindowRef *)
 */
public static final native int CreateNewWindow(int windowClass, int attributes, Rect bounds, int[] wHandle);
/**
 * @param window cast=(WindowRef)
 * @param boundsRect cast=(const Rect *)
 * @param orientation cast=(ControlPopupArrowOrientation)
 * @param size cast=(ControlPopupArrowSize)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreatePopupArrowControl(int window, Rect boundsRect, short orientation, short size, int[] outControl);
/**
 * @param window cast=(WindowRef)
 * @param title cast=(CFStringRef)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreatePopupButtonControl(int window, Rect boundsRect, int title, short menuID, boolean variableWidth, short titleWidth, short titleJustification, int titleStyle, int[] outControl);
/**
 * @param window cast=(WindowRef)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateProgressBarControl(int window, Rect boundsRect, int value, int minimim, int maximum, boolean indeterminate, int [] outControl);
/**
 * @param window cast=(WindowRef)
 * @param boundsRect cast=(const Rect *)
 * @param title cast=(CFStringRef)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreatePushButtonControl(int window, Rect boundsRect, int title, int[] outControl);
/**
 * @param window cast=(WindowRef)
 * @param title cast=(CFStringRef)
 * @param icon cast=(ControlButtonContentInfo *)
 * @param iconAlignment cast=(ControlPushButtonIconAlignment)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreatePushButtonWithIconControl(int window, Rect boundsRect, int title, ControlButtonContentInfo icon, short iconAlignment, int[] outControl);
/**
 * @param window cast=(WindowRef)
 * @param boundsRect cast=(const Rect *)
 * @param title cast=(CFStringRef)
 * @param initialValue cast=(SInt32)
 * @param autoToggle cast=(Boolean)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateRadioButtonControl(int window, Rect boundsRect, int title, int initialValue, boolean autoToggle, int[] outControl);
/**
 * @param windowHandle cast=(WindowRef)
 * @param cHandle cast=(ControlRef *)
 */
public static final native int CreateRootControl(int windowHandle, int[] cHandle);
/**
 * @param window cast=(WindowRef)
 * @param boundsRect cast=(const Rect *)
 * @param value cast=(SInt32)
 * @param minimum cast=(SInt32)
 * @param maximum cast=(SInt32)
 * @param orientation cast=(ControlSliderOrientation)
 * @param numTickMarks cast=(UInt16)
 * @param liveTracking cast=(Boolean)
 * @param liveTrackingProc cast=(ControlActionUPP)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateSliderControl(int window, Rect boundsRect, int value, int minimum, int maximum, int orientation, short numTickMarks, boolean liveTracking, int liveTrackingProc, int [] outControl);
/**
 * @param window cast=(WindowRef)
 * @param liveTrackingProc cast=(ControlActionUPP)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateScrollBarControl(int window, Rect boundsRect, int value, int minimum, int maximum, int viewSize, boolean liveTracking, int liveTrackingProc, int [] outControl);
/**
 * @param window cast=(WindowRef)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateSeparatorControl(int window, Rect boundsRect, int [] outControl);
/**
 * @param alertType cast=(AlertType)
 * @param errorSHandle cast=(CFStringRef)
 * @param explanationSHandle cast=(CFStringRef)
 * @param alertParamHandle cast=(const AlertStdCFStringAlertParamRec *)
 * @param dialogHandle cast=(DialogRef *)
 */
public static final native int CreateStandardAlert(short alertType, int errorSHandle, int explanationSHandle, AlertStdCFStringAlertParamRec alertParamHandle, int[] dialogHandle);
/**
 * @param window cast=(WindowRef)
 * @param text cast=(CFStringRef)
 * @param style cast=(const ControlFontStyleRec *)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateStaticTextControl(int window, Rect boundsRect, int text, ControlFontStyleRec style, int [] outControl);    
/**
 * @param window cast=(WindowRef)
 * @param boundsRect cast=(const Rect *)
 * @param size cast=(ControlTabSize)
 * @param direction cast=(ControlTabDirection)
 * @param numTabs cast=(UInt16)
 * @param tabArray cast=(const ControlTabEntry *)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateTabsControl(int window, Rect boundsRect, short size, short direction, short numTabs, int tabArray, int[] outControl);
/**
 * @param iEncoding cast=(TextEncoding)
 * @param oTextToUnicodeInfo cast=(TextToUnicodeInfo *)
 */
public static final native int CreateTextToUnicodeInfoByEncoding(int iEncoding, int[] oTextToUnicodeInfo);
/**
 * @param iEncoding cast=(TextEncoding)
 * @param oUnicodeToTextInfo cast=(UnicodeToTextInfo *)
 */
public static final native int CreateUnicodeToTextInfoByEncoding (int iEncoding, int[] oUnicodeToTextInfo);
/**
 * @param window cast=(WindowRef)
 * @param text cast=(CFStringRef)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateEditUnicodeTextControl(int window, Rect boundsRect, int text, boolean isPassword, ControlFontStyleRec style, int [] outControl);
/**
 * @param window cast=(WindowRef)
 * @param outControl cast=(ControlRef *)
 */
public static final native int CreateUserPaneControl(int window, Rect boundsRect, int features, int [] outControl);
/**
 * @param inAttributes cast=(WindowGroupAttributes)
 * @param outGroup cast=(WindowGroupRef *)
 */
public static final native int CreateWindowGroup (int inAttributes, int [] outGroup);
/** @method flags=dynamic */
public static final native int DataBrowserChangeAttributes(int inDataBrowser, int inAttributesToSet, int inAttributesToClear);
/** @method flags=dynamic */
public static final native int DataBrowserGetMetric(int inDataBrowser, int inMetric, boolean[] outUsingDefaultValue, float[] outValue);
/** @method flags=dynamic */
public static final native int DataBrowserSetMetric(int inDataBrowser, int inMetric, boolean inUseDefaultValue, float inValue);
/** @method flags=dynamic */
public static final native int DataBrowserGetAttributes(int inDataBrowser, int[] outAttributes); 
/** @param idocID cast=(TSMDocumentID) */
public static final native int DeactivateTSMDocument(int idocID);
/** @param menuID cast=(MenuID) */
public static final native void DeleteMenu(short menuID);
/**
 * @param mHandle cast=(MenuRef)
 * @param index cast=(short)
 */
public static final native void DeleteMenuItem(int mHandle, short index);
/**
 * @param mHandle cast=(MenuRef)
 * @param firstItem cast=(MenuItemIndex)
 * @param numItems cast=(ItemCount)
 */
public static final native int DeleteMenuItems(int mHandle, short firstItem, int numItems);
/** @param idocID cast=(TSMDocumentID) */
public static final native int DeleteTSMDocument(int idocID);
/**
 * @param srcRgnA cast=(RgnHandle)
 * @param srcRgnB cast=(RgnHandle)
 * @param dstRgn cast=(RgnHandle)
 */
public static final native void DiffRgn(int srcRgnA, int srcRgnB, int dstRgn);
/** @param cHandle cast=(ControlRef) */
public static final native int DisableControl(int cHandle);
/**
 * @param mHandle cast=(MenuRef)
 * @param commandId cast=(MenuCommand)
 */
public static final native void DisableMenuCommand(int mHandle, int commandId);
/**
 * @param mHandle cast=(MenuRef)
 * @param index cast=(MenuItemIndex)
 */
public static final native void DisableMenuItem(int mHandle, short index);
/** @param cHandle cast=(ControlRef) */
public static final native void DisposeControl(int cHandle);
/** @param theDrag cast=(DragRef) */
public static final native int DisposeDrag(int theDrag);
/** @param offscreenGWorld cast=(GWorldPtr) */
public static final native void DisposeGWorld(int offscreenGWorld);
/** @param handle cast=(Handle) */
public static final native void DisposeHandle(int handle);
/** @param mHandle cast=(MenuRef) */
public static final native void DisposeMenu(int mHandle);
/** @param ptr cast=(Ptr) */
public static final native void DisposePtr(int ptr);
/** @param rgnHandle cast=(RgnHandle) */
public static final native void DisposeRgn(int rgnHandle);
/** @param ioTextToUnicodeInfo cast=(TextToUnicodeInfo *) */
public static final native int DisposeTextToUnicodeInfo(int[] ioTextToUnicodeInfo);
/** @param ioUnicodeToTextInfo cast=(UnicodeToTextInfo *) */
public static final native int DisposeUnicodeToTextInfo(int[] ioUnicodeToTextInfo);
/** @param wHandle cast=(WindowRef) */
public static final native void DisposeWindow(int wHandle);
/** @param inControl cast=(ControlRef) */
public static final native void DrawControlInCurrentPort(int inControl);
public static final native void DrawMenuBar();
/**
 * @param picHandle cast=(PicHandle)
 * @param rect cast=(const Rect *)
 */
public static final native void DrawPicture(int picHandle, Rect rect);
/**
 * @param inBounds cast=(Rect *)
 * @param inKind cast=(ThemeButtonKind)
 * @param inNewInfo cast=(const ThemeButtonDrawInfo *)
 * @param inPrevInfo cast=(const ThemeButtonDrawInfo *)
 * @param inEraseProc cast=(ThemeEraseUPP)
 * @param inLabelProc cast=(ThemeButtonDrawUPP)
 * @param inUserData cast=(UInt32)
 */
public static final native int DrawThemeButton(Rect inBounds, short inKind, ThemeButtonDrawInfo inNewInfo, ThemeButtonDrawInfo inPrevInfo, int inEraseProc, int inLabelProc, int inUserData);
/**
 * @param bounds cast=(const Rect *)
 * @param state cast=(ThemeDrawState)
 */
public static final native int DrawThemeEditTextFrame(Rect bounds, int state);
/**
 * @param bounds cast=(const Rect *)
 * @param hasFocus cast=(Boolean)
 */
public static final native int DrawThemeFocusRect(Rect bounds, boolean hasFocus);
/**
 * @param orientation cast=(ThemeArrowOrientation)
 * @param size cast=(ThemePopupArrowSize)
 * @param state cast=(ThemeDrawState)
 * @param eraseProc cast=(ThemeEraseUPP)
 * @param eraseData cast=(UInt32)
 */
public static final native int DrawThemePopupArrow(Rect bounds,short orientation, short size, int state, int eraseProc, int eraseData); 
/**
 * @param bounds cast=(const Rect *)
 * @param state cast=(ThemeDrawState)
 */
public static final native int DrawThemeSeparator(Rect bounds, int state);
/**
 * @param sHandle cast=(CFStringRef)
 * @param fontID cast=(ThemeFontID)
 * @param state cast=(ThemeDrawState)
 * @param wrapToWidth cast=(Boolean)
 * @param bounds cast=(const Rect *)
 * @param just cast=(SInt16)
 * @param context cast=(void *)
 */
public static final native int DrawThemeTextBox(int sHandle, short fontID, int state, boolean wrapToWidth, Rect bounds, short just, int context);
/**
 * @param inControl cast=(ControlRef)
 * @param inContainer cast=(ControlRef)
 */
public static final native int EmbedControl(int inControl, int inContainer);
/** @param r cast=(const Rect *) */
public static final native boolean EmptyRect(Rect r);
/** @param rgnHandle cast=(RgnHandle) */
public static final native boolean EmptyRgn(int rgnHandle);
/** @param cHandle cast=(ControlRef) */
public static final native int EnableControl(int cHandle);
/**
 * @param mHandle cast=(MenuRef)
 * @param commandId cast=(MenuCommand)
 */
public static final native void EnableMenuCommand(int mHandle, int commandId);
/**
 * @param mHandle cast=(MenuRef)
 * @param index cast=(MenuItemIndex)
 */
public static final native void EnableMenuItem(int mHandle, short index);
/** @param bounds cast=(const Rect *) */
public static final native void EraseRect(Rect bounds);
public static final native int Fix2Long(int x);
/**
 * @param inQueue cast=(EventQueueRef)
 * @param inComparator cast=(EventComparatorUPP)
 * @param inCompareData cast=(void *)
 */
public static final native int FindSpecificEventInQueue(int inQueue, int inComparator, int inCompareData);
/** @param x cast=(Fixed) */
public static final native double Fix2X(int x);
/** @param idocID cast=(TSMDocumentID) */
public static final native int FixTSMDocument(int idocID);
public static final native int FMGetATSFontRefFromFont(int iFont);
/** @param name cast=(ConstStr255Param) */
public static final native short FMGetFontFamilyFromName(byte[] name);
/**
 * @param iFont cast=(FMFont)
 * @param oFontFamily cast=(FMFontFamily *)
 * @param oStyle cast=(FMFontStyle *)
 */
public static final native int FMGetFontFamilyInstanceFromFont(int iFont, short[] oFontFamily, short[] oStyle);
public static final native int FMGetFontFromATSFontRef(int iFont);
/**
 * @param iFontFamily cast=(FMFontFamily)
 * @param iStyle cast=(FMFontStyle)
 * @param oFont cast=(FMFont *)
 * @param oIntrinsicStyle cast=(FMFontStyle *)
 */
public static final native int FMGetFontFromFontFamilyInstance(short iFontFamily, short iStyle, int[] oFont, short[] oIntrinsicStyle);
public static final native boolean FPIsFontPanelVisible();
public static final native int FPShowHideFontPanel();
/**
 * @param spec cast=(FSSpec *)
 * @param fndrInfo cast=(FInfo *)
 */
public static final native int FSpGetFInfo(byte[] spec, byte[] fndrInfo);
/**
 * @param source cast=(const FSSpec *)
 * @param newRef cast=(FSRef *)
 */
public static final native int FSpMakeFSRef(byte[] source, byte[] newRef);
/**
 * @param ref cast=(FSRef *)
 * @param whichInfo cast=(FSCatalogInfoBitmap)
 * @param catalogInfo cast=(FSCatalogInfo *)
 * @param outName cast=(HFSUniStr255 *)
 * @param fsSpec cast=(FSSpec *)
 * @param parentRef cast=(FSRef *)
 */
public static final native int FSGetCatalogInfo(byte[] ref, int whichInfo, byte[] catalogInfo, byte[] outName, byte[] fsSpec, byte[] parentRef); 
/**
 * @param where cast=(Point *),flags=struct
 * @param wHandle cast=(WindowRef *)
 */
public static final native short FindWindow(Point where, int[] wHandle);
public static final native int FrontWindow();
/**
 * @param selector cast=(OSType)
 * @param response cast=(long *)
 */
public static final native int Gestalt(int selector, int[] response);
public static final native int GetApplicationEventTarget();
/** @param windowClass cast=(WindowClass) */
public static final native int GetAvailableWindowAttributes(int windowClass);
/**
 * @param inDevice cast=(GDHandle)
 * @param outAvailableRect cast=(Rect *)
 */
public static final native int GetAvailableWindowPositioningBounds(int inDevice, Rect outAvailableRect);
/**
 * @param inControl cast=(ControlRef)
 * @param outRect cast=(Rect *)
 * @param outBaseLineOffset cast=(SInt16 *)
 */
public static final native int GetBestControlRect(int inControl, Rect outRect, short[] outBaseLineOffset);
public static final native int GetCaretTime();
/** @param rgnHandle cast=(RgnHandle) */
public static final native void GetClip(int rgnHandle);
/** @param theControl cast=(ControlRef) */
public static final native int GetControlAction(int theControl	);
/** @param cHandle cast=(ControlRef) */
public static final native int GetControl32BitMaximum(int cHandle);
/** @param cHandle cast=(ControlRef) */
public static final native int GetControl32BitMinimum(int cHandle);
/** @param cHandle cast=(ControlRef) */
public static final native int GetControl32BitValue(int cHandle);
/**
 * @param cHandle cast=(ControlRef)
 * @param bounds cast=(Rect *)
 */
public static final native void GetControlBounds(int cHandle, Rect bounds);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inBufferSize cast=(Size)
 * @param inBuffer cast=(void *)
 * @param outActualSize cast=(Size *)
 */
public static final native int GetControlData(int inControl, short inPart, int inTagName, int inBufferSize, ControlFontStyleRec inBuffer, int[] outActualSize);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inBufferSize cast=(Size)
 * @param inBuffer cast=(void *)
 * @param outActualSize cast=(Size *)
 */
public static final native int GetControlData(int inControl, short inPart, int inTagName, int inBufferSize, Rect inBuffer, int[] outActualSize);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inBufferSize cast=(Size)
 * @param inBuffer cast=(void *)
 * @param outActualSize cast=(Size *)
 */
public static final native int GetControlData(int inControl, short inPart, int inTagName, int inBufferSize, int[] inBuffer, int[] outActualSize);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inBufferSize cast=(Size)
 * @param inBuffer cast=(void *)
 * @param outActualSize cast=(Size *)
 */
public static final native int GetControlData(int inControl, short inPart, int inTagName, int inBufferSize, short[] inBuffer, int[] outActualSize);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inBufferSize cast=(Size)
 * @param inBuffer cast=(void *)
 * @param outActualSize cast=(Size *)
 */
public static final native int GetControlData(int inControl, short inPart, int inTagName, int inBufferSize, byte[] inBuffer, int[] outActualSize);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inBufferSize cast=(Size)
 * @param inBuffer cast=(void *)
 */
public static final native int GetControlData(int inControl, short inPart, int inTagName, int inBufferSize, ControlEditTextSelectionRec inBuffer, int[] outActualSize);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inBufferSize cast=(Size)
 * @param inBuffer cast=(void *)
 * @param outActualSize cast=(Size *)
 */
public static final native int GetControlData(int inControl, short inPart, int inTagName, int inBufferSize, LongDateRec inBuffer, int[] outActualSize);
/** @param cHandle cast=(ControlRef) */
public static final native int GetControlEventTarget(int cHandle);
/**
 * @param inControl cast=(ControlRef)
 * @param outFeatures cast=(UInt32 *)
 */
public static final native int GetControlFeatures(int inControl, int[] outFeatures);
/** @param inControl cast=(ControlRef) */
public static final native int GetControlKind(int inControl, ControlKind kind);
/** @param cHandle cast=(ControlRef) */
public static final native int GetControlOwner(int cHandle);
/**
 * @param control cast=(ControlRef)
 * @param actualSize cast=(UInt32 *)
 * @param propertyBuffer cast=(void *)
 */
public static final native int GetControlProperty(int control, int  propertyCreator, int propertyTag, int bufferSize, int[] actualSize,  int[] propertyBuffer);
/** @param cHandle cast=(ControlRef) */
public static final native int GetControlReference(int cHandle);
/**
 * @param cHandle cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param rgnHandle cast=(RgnHandle)
 */
public static final native int GetControlRegion(int cHandle, short inPart, int rgnHandle);
/** @param cHandle cast=(ControlRef) */
public static final native short GetControlValue(int cHandle);
/** @param cHandle cast=(ControlRef) */
public static final native int GetControlViewSize(int cHandle);
public static final native int GetCurrentEventButtonState();
public static final native int GetCurrentEventLoop();
public static final native int GetCurrentEventKeyModifiers();
public static final native int GetCurrentEventQueue();
/** @param psn cast=(ProcessSerialNumber *) */
public static final native int GetCurrentProcess(int[] psn);
/** @param scrap cast=(ScrapRef *) */
public static final native int GetCurrentScrap(int[] scrap);
/**
 * @param browser cast=(ControlRef)
 * @param callbacks cast=(DataBrowserCallbacks *)
 */
public static final native int GetDataBrowserCallbacks(int browser, DataBrowserCallbacks  callbacks);
/** @param browser cast=(ControlRef) */
public static final native int GetDataBrowserHasScrollBars(int browser, boolean [] horiz, boolean [] vert);
/**
 * @param cHandle cast=(ControlRef)
 * @param container cast=(DataBrowserItemID)
 * @param recurse cast=(Boolean)
 * @param state cast=(DataBrowserItemState)
 * @param numItems cast=(UInt32 *)
 */
public static final native int GetDataBrowserItemCount(int cHandle, int container, boolean recurse, int state, int[] numItems);
/**
 * @param itemData cast=(ControlRef)
 * @param theData cast=(ThemeButtonValue *)
 */
public static final native int GetDataBrowserItemDataButtonValue(int itemData, short [] theData);
/**
 * @param cHandle cast=(ControlRef)
 * @param item cast=(DataBrowserItemID)
 * @param property cast=(DataBrowserPropertyID)
 * @param part cast=(DataBrowserPropertyPart)
 * @param bounds cast=(Rect *)
 */
public static final native int GetDataBrowserItemPartBounds(int cHandle, int item, int property, int part, Rect bounds);
/**
 * @param browser cast=(ControlRef)
 * @param container cast=(DataBrowserItemID)
 * @param recurse cast=(Boolean)
 * @param state cast=(DataBrowserItemState)
 * @param items cast=(Handle)
 */
public static final native int GetDataBrowserItems(int browser, int container, boolean recurse, int state, int items);
/**
 * @param browser cast=(ControlRef)
 * @param state cast=(DataBrowserItemState *)
 */
public static final native int GetDataBrowserItemState(int browser, int item, int [] state);
/**
 * @param browser cast=(ControlRef)
 * @param column cast=(DataBrowserTableViewColumnID *)
 * @param expandableRows cast=(Boolean *)
 */
public static final native int GetDataBrowserListViewDisclosureColumn(int browser, int [] column, boolean [] expandableRows);
/**
 * @param browser cast=(ControlRef)
 * @param height cast=(UInt16 *)
 */
public static final native int GetDataBrowserListViewHeaderBtnHeight(int browser, short [] height);
/**
 * @param browser cast=(ControlRef)
 * @param column cast=(DataBrowserTableViewColumnID)
 * @param desc cast=(DataBrowserListViewHeaderDesc *)
 */
public static final native int GetDataBrowserListViewHeaderDesc(int browser, int column, DataBrowserListViewHeaderDesc desc);
/**
 * @param browser cast=(ControlRef)
 * @param property cast=(DataBrowserPropertyID)
 * @param flags cast=(DataBrowserPropertyFlags *)
 */
public static final native int GetDataBrowserPropertyFlags(int browser, int property, int [] flags);
/**
 * @param browser cast=(ControlRef)
 * @param column cast=(DataBrowserTableViewColumnID)
 * @param position cast=(DataBrowserTableViewColumnIndex *)
 */
public static final native int GetDataBrowserTableViewColumnPosition(int browser,int column,int[] position);
/**
 * @param browser cast=(ControlRef)
 * @param row cast=(DataBrowserTableViewRowIndex)
 * @param item cast=(DataBrowserItemID *)
 */
public static final native int GetDataBrowserTableViewItemID(int browser, int row, int [] item);
/**
 * @param browser cast=(ControlRef)
 * @param item cast=(DataBrowserTableViewRowIndex)
 * @param row cast=(DataBrowserItemID *)
 */
public static final native int GetDataBrowserTableViewItemRow(int browser, int item, int [] row);                         
/**
 * @param browser cast=(ControlRef)
 * @param column cast=(DataBrowserTableViewColumnID)
 * @param width cast=(UInt16 *)
 */
public static final native int GetDataBrowserTableViewNamedColumnWidth(int browser, int column, short [] width);
/**
 * @param browser cast=(ControlRef)
 * @param height cast=(UInt16 *)
 */
public static final native int GetDataBrowserTableViewRowHeight(int browser, short [] height);
/** @param browser cast=(ControlRef) */
public static final native int GetDataBrowserScrollBarInset(int browser, Rect insetRect);
/**
 * @param cHandle cast=(ControlRef)
 * @param top cast=(UInt32 *)
 * @param left cast=(UInt32 *)
 */
public static final native int GetDataBrowserScrollPosition(int cHandle, int[] top, int[] left);
/**
 * @param browser cast=(ControlRef)
 * @param first cast=(UInt32 *)
 * @param last cast=(UInt32 *)
 */
public static final native int GetDataBrowserSelectionAnchor(int browser, int [] first, int [] last);
/**
 * @param browser cast=(ControlRef)
 * @param selectionFlags cast=(DataBrowserSelectionFlags *)
 */
public static final native int GetDataBrowserSelectionFlags(int browser, int [] selectionFlags);
/**
 * @param browser cast=(ControlRef)
 * @param property cast=(DataBrowserPropertyID *)
 */
public static final native int GetDataBrowserSortProperty(int browser, int[] property);
public static final native int GetDblTime();
public static final native int GetDeviceList();
/**
 * @param theDrag cast=(DragRef)
 * @param outActions cast=(DragActions *)
 */
public static final native int GetDragAllowableActions(int theDrag, int[] outActions); 
/**
 * @param theDrag cast=(DragRef)
 * @param outAction cast=(DragActions *)
 */
public static final native int GetDragDropAction(int theDrag, int[] outAction);
/**
 * @param theDrag cast=(DragRef)
 * @param theItemRef cast=(DragItemRef *)
 */
public static final native int GetDragItemReferenceNumber(int theDrag, short index, int[] theItemRef);
/**
 * @param theDrag cast=(DragRef)
 * @param modifiers cast=(SInt16 *)
 * @param mouseDownModifiers cast=(SInt16 *)
 * @param mouseUpModifiers cast=(SInt16 *)
 */
public static final native int GetDragModifiers(int theDrag, short[] modifiers, short[] mouseDownModifiers, short[] mouseUpModifiers);
/**
 * @param theDrag cast=(DragRef)
 * @param mouse cast=(Point *)
 * @param globalPinnedMouse cast=(Point *)
 */
public static final native int GetDragMouse(int theDrag, Point mouse, Point globalPinnedMouse); 
/** @param eHandle cast=(EventRef) */
public static final native int GetEventClass(int eHandle);
public static final native int GetEventDispatcherTarget();
/** @param eHandle cast=(EventRef) */
public static final native int GetEventKind(int eHandle);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inDesiredType cast=(EventParamType)
 * @param outActualType cast=(EventParamType *)
 * @param inBufferSize cast=(UInt32)
 * @param outActualSize cast=(UInt32 *)
 * @param outData cast=(void *)
 */
public static final native int GetEventParameter(int inEvent, int inName, int inDesiredType, int[] outActualType, int inBufferSize, int[] outActualSize, int outData);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inDesiredType cast=(EventParamType)
 * @param outActualType cast=(EventParamType *)
 * @param inBufferSize cast=(UInt32)
 * @param outActualSize cast=(UInt32 *)
 * @param outData cast=(void *)
 */
public static final native int GetEventParameter(int inEvent, int inName, int inDesiredType, int[] outActualType, int inBufferSize, int[] outActualSize, int[] outData);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inDesiredType cast=(EventParamType)
 * @param outActualType cast=(EventParamType *)
 * @param inBufferSize cast=(UInt32)
 * @param outActualSize cast=(UInt32 *)
 * @param outData cast=(void *)
 */
public static final native int GetEventParameter(int inEvent, int inName, int inDesiredType, int[] outActualType, int inBufferSize, int[] outActualSize, char[] outData);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inDesiredType cast=(EventParamType)
 * @param outActualType cast=(EventParamType *)
 * @param inBufferSize cast=(UInt32)
 * @param outActualSize cast=(UInt32 *)
 * @param outData cast=(void *)
 */
public static final native int GetEventParameter(int inEvent, int inName, int inDesiredType, int[] outActualType, int inBufferSize, int[] outActualSize, short[] outData);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inDesiredType cast=(EventParamType)
 * @param outActualType cast=(EventParamType *)
 * @param inBufferSize cast=(UInt32)
 * @param outActualSize cast=(UInt32 *)
 * @param outData cast=(void *)
 */
public static final native int GetEventParameter(int inEvent, int inName, int inDesiredType, int[] outActualType, int inBufferSize, int[] outActualSize, byte[] outData);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inDesiredType cast=(EventParamType)
 * @param outActualType cast=(EventParamType *)
 * @param inBufferSize cast=(UInt32)
 * @param outActualSize cast=(UInt32 *)
 * @param outData cast=(void *)
 */
public static final native int GetEventParameter(int inEvent, int inName, int inDesiredType, int[] outActualType, int inBufferSize, int[] outActualSize, boolean[] outData);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inDesiredType cast=(EventParamType)
 * @param outActualType cast=(EventParamType *)
 * @param inBufferSize cast=(UInt32)
 * @param outActualSize cast=(UInt32 *)
 * @param outData cast=(void *)
 */
public static final native int GetEventParameter(int inEvent, int inName, int inDesiredType, int[] outActualType, int inBufferSize, int[] outActualSize, HICommand outData);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inDesiredType cast=(EventParamType)
 * @param outActualType cast=(EventParamType *)
 * @param inBufferSize cast=(UInt32)
 * @param outActualSize cast=(UInt32 *)
 * @param outData cast=(void *)
 */
public static final native int GetEventParameter(int inEvent, int inName, int inDesiredType, int[] outActualType, int inBufferSize, int[] outActualSize, Point outData);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inDesiredType cast=(EventParamType)
 * @param outActualType cast=(EventParamType *)
 * @param inBufferSize cast=(UInt32)
 * @param outActualSize cast=(UInt32 *)
 * @param outData cast=(void *)
 */
public static final native int GetEventParameter(int inEvent, int inName, int inDesiredType, int[] outActualType, int inBufferSize, int[] outActualSize, CGPoint outData);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inDesiredType cast=(EventParamType)
 * @param outActualType cast=(EventParamType *)
 * @param inBufferSize cast=(UInt32)
 * @param outActualSize cast=(UInt32 *)
 * @param outData cast=(void *)
 */
public static final native int GetEventParameter(int inEvent, int inName, int inDesiredType, int[] outActualType, int inBufferSize, int[] outActualSize, CGRect outData);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inDesiredType cast=(EventParamType)
 * @param outActualType cast=(EventParamType *)
 * @param inBufferSize cast=(UInt32)
 * @param outActualSize cast=(UInt32 *)
 * @param outData cast=(void *)
 */
public static final native int GetEventParameter(int inEvent, int inName, int inDesiredType, int[] outActualType, int inBufferSize, int[] outActualSize, RGBColor outData);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inDesiredType cast=(EventParamType)
 * @param outActualType cast=(EventParamType *)
 * @param inBufferSize cast=(UInt32)
 * @param outActualSize cast=(UInt32 *)
 * @param outData cast=(void *)
 */
public static final native int GetEventParameter(int inEvent, int inName, int inDesiredType, int[] outActualType, int inBufferSize, int[] outActualSize, Rect outData);
/** @param eHandle cast=(EventRef) */
public static final native double GetEventTime(int eHandle);
/**
 * @param theDrag cast=(DragRef)
 * @param theItemRef cast=(DragItemRef)
 * @param theType cast=(FlavorType)
 * @param dataPtr cast=(void *)
 * @param dataSize cast=(Size *)
 */
public static final native int GetFlavorData(int theDrag, int theItemRef, int theType, byte[] dataPtr, int[] dataSize, int dataOffset);
/**
 * @param theDrag cast=(DragRef)
 * @param theItemRef cast=(DragItemRef)
 * @param theType cast=(FlavorType)
 * @param dataSize cast=(Size *)
 */
public static final native int GetFlavorDataSize(int theDrag, int theItemRef, int theType, int[] dataSize);
/**
 * @param theDrag cast=(DragRef)
 * @param theItemRef cast=(DragItemRef)
 * @param theType cast=(FlavorType *)
 */
public static final native int GetFlavorType(int theDrag,int theItemRef, short index, int[] theType);
/** @param psn cast=(ProcessSerialNumber *) */
public static final native int GetFrontProcess(int [] psn);
/**
 * @param portHandle cast=(CGrafPtr *)
 * @param gdHandle cast=(GDHandle *)
 */
public static final native void GetGWorld(int[] portHandle, int[] gdHandle);
/** @param where cast=(Point *) */
public static final native void GetGlobalMouse(Point where);
/** @param handle cast=(Handle) */
public static final native int GetHandleSize(int handle);
/**
 * @param iconFamily cast=(IconFamilyHandle)
 * @param iconType cast=(OSType)
 * @param h cast=(Handle)
 */
public static final native int GetIconFamilyData(int iconFamily, int iconType, int h);
/** @method flags=dynamic */
public static final native int GetIconRefFromIconFamilyPtr(int inIconFamilyPtr, int inSize, int[] outIconRef);
/**
 * @param inRef cast=(const FSRef *)
 * @param inFileName cast=(const UniChar *)
 * @param inWhichInfo cast=(FSCatalogInfoBitmap)
 * @param inCatalogInfo cast=(const FSCatalogInfo *)
 * @param outIconRef cast=(IconRef *)
 * @param outLabel cast=(SInt16 *)
 */
public static final native int GetIconRefFromFileInfo(byte[] inRef, int inFileNameLength, char[] inFileName, int inWhichInfo, int inCatalogInfo, int inUsageFlags, int[] outIconRef, int[] outLabel);
/**
 * @param vRefNum cast=(SInt16)
 * @param creator cast=(OSType)
 * @param iconType cast=(OSType)
 * @param theIconRef cast=(IconRef *)
 */
public static final native int GetIconRef(short vRefNum, int creator, int iconType, int[] theIconRef);
/**
 * @param mHandle cast=(MenuRef)
 * @param commandId cast=(MenuCommand)
 * @param index cast=(UInt32)
 * @param outMenu cast=(MenuRef *)
 * @param outIndex cast=(MenuItemIndex *)
 */
public static final native int GetIndMenuItemWithCommandID(int mHandle, int commandId, int index, int[] outMenu, short[] outIndex);
/**
 * @param cHandle cast=(ControlRef)
 * @param index cast=(UInt16)
 * @param outHandle cast=(ControlRef *)
 */
public static final native int GetIndexedSubControl(int cHandle, short index, int[] outHandle);
/** @param theMenu cast=(MenuRef) */
public static final native void GetItemMark (int theMenu, short item, short[] markChar);
/**
 * @param wHandle cast=(WindowRef)
 * @param cHandle cast=(ControlRef *)
 */
public static final native int GetKeyboardFocus(int wHandle, int[] cHandle);
public static final native double GetLastUserEventTime();
public static final native int GetMainDevice();
public static final native int GetMainEventQueue();
public static final native int GetMBarHeight();
/**
 * @param theMenu cast=(MenuRef)
 * @param commandId cast=(MenuCommand)
 * @param outMark cast=(UniChar *)
 */
public static final native int GetMenuCommandMark(int theMenu, int commandId, char[] outMark);
/** @param cHandle cast=(MenuRef) */
public static final native int GetMenuEventTarget(int cHandle);
/**
 * @param inMenu cast=(MenuRef)
 * @param outFontID cast=(SInt16 *)
 * @param outFontSize cast=(UInt16 *)
 */
public static final native int GetMenuFont(int inMenu, short[] outFontID, short[] outFontSize);
/** @param inMenu cast=(MenuRef) */
public static final native short GetMenuHeight(int inMenu);
/** @param menu cast=(MenuRef) */
public static final native short GetMenuID(int menu);
/**
 * @param inMenu cast=(MenuRef)
 * @param inItem cast=(SInt16)
 * @param outCommandID cast=(MenuCommand *)
 */
public static final native int GetMenuItemCommandID(int inMenu, short inItem, int[] outCommandID);
/**
 * @param inMenu cast=(MenuRef)
 * @param inItem cast=(SInt16)
 * @param outHierMenu cast=(MenuRef *)
 */
public static final native int GetMenuItemHierarchicalMenu(int inMenu, short inItem, int []outHierMenu);
/**
 * @param inMenu cast=(MenuRef)
 * @param intItem cast=(SInt16)
 * @param outRefCon cast=(UInt32 *)
 */
public static final native int GetMenuItemRefCon(int inMenu, short intItem, int[] outRefCon);
/** @param menu cast=(MenuRef) */
public static final native int GetMenuTrackingData(int menu, MenuTrackingData outData);
/** @param inMenu cast=(MenuRef) */
public static final native short GetMenuWidth(int inMenu);
/** @param where cast=(Point *) */
public static final native void GetMouse(Point where);
/** @param curDevice cast=(GDHandle) */
public static final native int GetNextDevice(int curDevice);
/** @param pHandle cast=(PixMapHandle) */
public static final native short GetPixDepth(int pHandle);
/** @param port cast=(GrafPtr *) */
public static final native void GetPort(int[] port);
/** @param portHandle cast=(CGrafPtr) */
public static final native int GetPortBitMapForCopyBits(int portHandle);
/**
 * @param pHandle cast=(CGrafPtr)
 * @param rect cast=(Rect *)
 */
public static final native void GetPortBounds(int pHandle, Rect rect);
/** @param inWindow cast=(WindowRef) */
public static final native int GetPreviousWindow(int inWindow);
/** @param ptr cast=(Ptr) */
public static final native int GetPtrSize(int ptr);
/**
 * @param rgnHandle cast=(RgnHandle)
 * @param bounds cast=(Rect *)
 */
public static final native void GetRegionBounds(int rgnHandle, Rect bounds);
/**
 * @param windowHandle cast=(WindowRef)
 * @param cHandle cast=(ControlRef *)
 */
public static final native int GetRootControl(int windowHandle, int[] cHandle);
/**
 * @param scrap cast=(ScrapRef)
 * @param infoCount cast=(UInt32 *)
 */
public static final native int GetScrapFlavorCount(int scrap, int[] infoCount);
/**
 * @param scrap cast=(ScrapRef)
 * @param flavorType cast=(ScrapFlavorType)
 * @param byteCount cast=(Size *)
 * @param destination cast=(void *)
 */
public static final native int GetScrapFlavorData(int scrap, int flavorType, int[] byteCount, byte[] destination);
/**
 * @param scrap cast=(ScrapRef)
 * @param flavorType cast=(ScrapFlavorType)
 * @param byteCount cast=(Size *)
 * @param destination cast=(void *)
 */
public static final native int GetScrapFlavorData(int scrap, int flavorType, int[] byteCount, char[] destination);
/**
 * @param scrap cast=(ScrapRef)
 * @param infoCount cast=(UInt32 *)
 * @param info cast=(ScrapFlavorInfo *)
 */
public static final native int GetScrapFlavorInfoList(int scrap, int[] infoCount, int[] info);
/**
 * @param scrap cast=(ScrapRef)
 * @param flavorType cast=(ScrapFlavorType)
 * @param byteCount cast=(Size *)
 */
public static final native int GetScrapFlavorSize(int scrap, int flavorType, int[] byteCount);
public static final native int GetScriptManagerVariable(short selector);
/**
 * @param cHandle cast=(ControlRef)
 * @param parentHandle cast=(ControlRef *)
 */
public static final native int GetSuperControl(int cHandle, int[] parentHandle);
/**
 * @param outMode cast=(SystemUIMode *)
 * @param outOptions cast=(SystemUIOptions *)
 */
public static final native void GetSystemUIMode(int[] outMode, int[] outOptions);
/** @param theControl cast=(ControlRef) */
public static final native int GetTabContentRect(int theControl, Rect rect);
public static final native int GetThemeBrushAsColor(short inBrush, short inDepth, boolean inColorDev, RGBColor outColor);
public static final native int GetThemeButtonContentBounds(Rect inBounds, int inKind, ThemeButtonDrawInfo inDrawInfo, Rect outBounds);
/** @param outRegion cast=(RgnHandle) */
public static final native int GetThemeButtonRegion(Rect inBounds, int inKind, ThemeButtonDrawInfo inNewInfo, int outRegion);
/** @param state cast=(ThemeDrawingState *) */
public static final native int GetThemeDrawingState(int[] state);
/**
 * @param themeFontId cast=(ThemeFontID)
 * @param scriptCode cast=(ScriptCode)
 * @param fontName cast=(unsigned char *)
 * @param fontSize cast=(SInt16 *)
 * @param style cast=(Style *)
 */
public static final native int GetThemeFont(short themeFontId, short scriptCode, byte[] fontName, short[] fontSize, byte[] style);
public static final native int GetThemeMenuItemExtra(short inItemType, short[] outHeight, short[] outWidth); 
public static final native int GetThemeMetric(int inMetric, int [] outMetric);
public static final native int GetThemeTextColor(short inColor, short inDepth, boolean inColorDev, RGBColor outColor);
/**
 * @param sHandle cast=(CFStringRef)
 * @param fontID cast=(ThemeFontID)
 * @param state cast=(ThemeDrawState)
 * @param wrapToWidth cast=(Boolean)
 * @param ioBounds cast=(Point *)
 * @param baseLine cast=(SInt16 *)
 */
public static final native int GetThemeTextDimensions(int sHandle, short fontID, int state, boolean wrapToWidth, Point ioBounds, short[] baseLine);
public static final native int GetUserFocusEventTarget();
public static final native int GetUserFocusWindow();
/** @param inWindow cast=(WindowRef) */
public static final native int SetUserFocusWindow(int inWindow);
/**
 * @param inWindow cast=(WindowRef)
 * @param outScope cast=(WindowActivationScope *)
 */
public static final native int GetWindowActivationScope(int inWindow, int[] outScope);
/** @param inWindow cast=(WindowRef) */
public static final native int GetWindowAlpha(int inWindow, float [] outAlpha);
/**
 * @param wHandle cast=(WindowRef)
 * @param windowRegion cast=(WindowRegionCode)
 * @param bounds cast=(Rect *)
 */
public static final native void GetWindowBounds(int wHandle, short windowRegion, Rect bounds);
/**
 * @param inWindow cast=(WindowRef)
 * @param outClass cast=(WindowClass *)
 */
public static final native int GetWindowClass (int inWindow, int[] outClass);
/**
 * @param wHandle cast=(WindowRef)
 * @param cHandle cast=(ControlRef *)
 */
public static final native int GetWindowDefaultButton(int wHandle, int[] cHandle);
/** @param wHandle cast=(WindowRef) */
public static final native int GetWindowEventTarget(int wHandle);
/** @param pHandle cast=(CGrafPtr) */
public static final native int GetWindowFromPort(int pHandle);
public static final native int GetWindowGroupOfClass (int windowClass);
public static final native int GetWindowList();
/** @param inWindow cast=(WindowRef) */
public static final native int GetNextWindow(int inWindow);
/**
 * @param inWindow cast=(WindowRef)
 * @param outModalKind cast=(WindowModality *)
 * @param outUnavailableWindow cast=(WindowRef *)
 */
public static final native int GetWindowModality(int inWindow, int[] outModalKind, int[] outUnavailableWindow);
/** @param wHandle cast=(WindowRef) */
public static final native int GetWindowPort(int wHandle);
/**
 * @param window cast=(WindowRef)
 * @param inRegionCode cast=(WindowRegionCode)
 * @param ioWinRgn cast=(RgnHandle)
 */
public static final native int GetWindowRegion(int window, short inRegionCode, int ioWinRgn);
/**
 * @param inWindow cast=(WindowRef)
 * @param inMinLimits cast=(HISize *)
 * @param inMaxLimits cast=(HISize *)
 */
public static final native int GetWindowResizeLimits (int inWindow, CGPoint inMinLimits, CGPoint inMaxLimits);
/**
 * @param intWindow cast=(WindowRef)
 * @param outRect cast=(Rect *)
 */
public static final native void GetWindowStructureWidths(int intWindow, Rect outRect);
/**
 * @method flags=dynamic
 * @param inImage cast=(CGImageRef)
 * @param outImage cast=(CGImageRef *)
 */
public static final native int HICreateTransformedCGImage(int inImage, int inTransform, int[] outImage); 
/**
 * @param control cast=(ControlRef)
 * @param localPoint flags=struct
 * @param modifiers cast=(EventModifiers)
 * @param cursorWasSet cast=(Boolean *)
 */
public static final native int HandleControlSetCursor(int control, Point localPoint, int modifiers, boolean[] cursorWasSet);  
/**
 * @param inComboBox cast=(HIViewRef)
 * @param inText cast=(CFStringRef)
 * @param outIndex cast=(CFIndex *)
 */
public static final native int HIComboBoxAppendTextItem(int inComboBox, int inText, int[] outIndex);
/**
 * @param inComboBox cast=(HIViewRef)
 * @param inIndex cast=(CFIndex)
 * @param outString cast=(CFStringRef *)
 */
public static final native int HIComboBoxCopyTextItemAtIndex(int inComboBox, int inIndex, int[] outString);
/**
 * @param boundsRect cast=(const HIRect *)
 * @param text cast=(CFStringRef)
 * @param style cast=(const ControlFontStyleRec *)
 * @param list cast=(CFArrayRef)
 * @param inAttributes cast=(OptionBits)
 * @param outComboBox cast=(HIViewRef *)
 */
public static final native int HIComboBoxCreate(CGRect boundsRect, int text, ControlFontStyleRec style, int list, int inAttributes, int[] outComboBox);
/** @param inComboBox cast=(HIViewRef) */
public static final native int HIComboBoxGetItemCount(int inComboBox);
/**
 * @param inComboBox cast=(HIViewRef)
 * @param inIndex cast=(CFIndex)
 * @param inText cast=(CFStringRef)
 */
public static final native int HIComboBoxInsertTextItemAtIndex(int inComboBox, int inIndex, int inText);
/**
 * @method flags=dynamic
 * @param inComboBox cast=(HIViewRef)
 */
public static final native boolean HIComboBoxIsListVisible(int inComboBox);
/**
 * @param inComboBox cast=(HIViewRef)
 * @param inIndex cast=(CFIndex)
 */
public static final native int HIComboBoxRemoveItemAtIndex(int inComboBox, int inIndex);
/**
 * @method flags=dynamic
 * @param inComboBox cast=(HIViewRef)
 */
public static final native int HIComboBoxSetListVisible (int inComboBox, boolean inVisible);
/**
 * @param inRole cast=(CFStringRef)
 * @param inSubrole cast=(CFStringRef)
 */
public static final native int HICopyAccessibilityRoleDescription(int inRole, int inSubrole);
/** @param inObject cast=(HIObjectRef) */
public static final native int HIObjectCopyClassID(int inObject);
/**
 * @param inClassID cast=(CFStringRef)
 * @param inConstructData cast=(EventRef)
 * @param outObject cast=(HIObjectRef *)
 */
public static final native int HIObjectCreate(int inClassID, int inConstructData, int[] outObject);
/**
 * @param inClassID cast=(CFStringRef)
 * @param inBaseClassID cast=(CFStringRef)
 * @param inOptions cast=(OptionBits)
 * @param inConstructProc cast=(EventHandlerUPP)
 * @param inNumEvents cast=(UInt32)
 * @param inEventList cast=(const EventTypeSpec *)
 * @param inConstructData cast=(void *)
 * @param outClassRef cast=(HIObjectClassRef *)
 */
public static final native int HIObjectRegisterSubclass(int inClassID, int inBaseClassID, int inOptions, int inConstructProc, int inNumEvents, int[] inEventList, int inConstructData, int[] outClassRef);
/** @param inView cast=(HIViewRef) */
public static final native int HIViewScrollRect(int inView, CGRect inRect, float inDX, float inDY);
/** @param inView cast=(HIViewRef) */
public static final native int HIViewRegionChanged(int inView, int inRegionCode);
/** @method flags=dynamic */
public static final native int HIShapeCreateWithQDRgn(int inRgn);
/** @method flags=dynamic */
public static final native int HIShapeReplacePathInCGContext(int inShape, int inContext);
/** @param outView cast=(HIViewRef*) */
public static final native int HIScrollViewCreate(int inOptions, int[] outView);
/** @param inView cast=(HIViewRef) */
public static final native int HIScrollViewSetScrollBarAutoHide(int inView, boolean inAutoHide);    
/**
 * @param inSearchMenu cast=(MenuRef)
 * @param inDescriptiveText cast=(CFStringRef)
 * @param outRef cast=(HIViewRef*)
 */
public static final native int HISearchFieldCreate(CGRect inBounds, int inAttributes, int inSearchMenu, int inDescriptiveText, int [] outRef);
/**
 * @param inSearchField cast=(HIViewRef)
 * @param inAttributesToSet cast=(OptionBits)
 * @param inAttributesToClear cast=(OptionBits)
 */
public static final native int HISearchFieldChangeAttributes(int inSearchField, int inAttributesToSet, int inAttributesToClear);
/**
 * @param inSearchField cast=(HIViewRef)
 * @param outDescription cast=(CFStringRef *)
 */
public static final native int HISearchFieldCopyDescriptiveText(int inSearchField, int [] outDescription);
/**
 * @param inSearchField cast=(HIViewRef)
 * @param outAttributes cast=(OptionBits*)
 */
public static final native int HISearchFieldGetAttributes(int inSearchField, int [] outAttributes);
/**
 * @param inSearchField cast=(HIViewRef)
 * @param inDescription cast=(CFStringRef)
 */
public static final native int HISearchFieldSetDescriptiveText(int inSearchField, int inDescription);
/** @param outTextView cast=(HIViewRef *) */
public static final native int HITextViewCreate(CGRect inBoundsRect, int inOptions, int inTXNFrameOptions, int[] outTextView);
/** @param inTextView cast=(HIViewRef) */
public static final native int HITextViewGetTXNObject(int inTextView);
/**
 * @param inTextView cast=(HIViewRef)
 * @param inColor cast=(CGColorRef)
 */
public static final native int HITextViewSetBackgroundColor(int inTextView, int inColor); 
/** @param inContext cast=(CGContextRef) */
public static final native int HIThemeDrawBackground(CGRect inBounds, HIThemeBackgroundDrawInfo inDrawInfo, int inContext, int inOrientation); 
/**
 * @param inBounds cast=(const HIRect *),flags=no_out
 * @param inDrawInfo cast=(const HIThemeButtonDrawInfo *)
 * @param inContext cast=(CGContextRef)
 * @param inOrientation cast=(HIThemeOrientation)
 * @param outLabelRect cast=(HIRect *),flags=no_in
 */
public static final native int HIThemeDrawButton(CGRect inBounds, HIThemeButtonDrawInfo inDrawInfo, int inContext, int inOrientation, CGRect outLabelRect);
/** @param inContext cast=(CGContextRef) */
public static final native int HIThemeDrawFocusRect(CGRect inRect, boolean inHasFocus, int inContext, int inOrientation); 
/** @param inContext cast=(CGContextRef) */
public static final native int HIThemeDrawFrame(CGRect inRect, HIThemeFrameDrawInfo inDrawInfo, int inContext, int inOrientation);   
/**
 * @param inRect cast=(const HIRect *)
 * @param info cast=(const HIThemeButtonDrawInfo *)
 * @param inContext cast=(CGContextRef)
 * @param inOrientation cast=(HIThemeOrientation)
 */
public static final native int HIThemeDrawGenericWell(CGRect inRect, HIThemeButtonDrawInfo info, int inContext, int inOrientation);
/** @param inContext cast=(CGContextRef) */
public static final native int HIThemeDrawGroupBox(CGRect inRect, HIThemeGroupBoxDrawInfo inDrawInfo, int inContext, int inOrientation);
/** @param inContext cast=(CGContextRef) */
public static final native int HIThemeDrawGrowBox(CGPoint inOrigin, HIThemeGrowBoxDrawInfo inDrawInfo, int inContext, int inOrientation);
/** @param inContext cast=(CGContextRef) */
public static final native int HIThemeDrawPopupArrow(CGRect inBounds, HIThemePopupArrowDrawInfo inDrawInfo, int inContext, int inOrientation); 
/** @param inContext cast=(CGContextRef) */
public static final native int HIThemeDrawSeparator(CGRect inRect, HIThemeSeparatorDrawInfo inDrawInfo, int inContext, int inOrientation);  
/**
 * @param inRect cast=(const HIRect *),flags=no_out
 * @param inDrawInfo cast=(const HIThemeTabDrawInfo *)
 * @param inContext cast=(CGContextRef)
 * @param inOrientation cast=(HIThemeOrientation)
 * @param outLabelRect cast=(HIRect *),flags=no_in
 */
public static final native int HIThemeDrawTab(CGRect inRect, HIThemeTabDrawInfo inDrawInfo, int inContext, int inOrientation, CGRect outLabelRect);
/**
 * @param inRect cast=(const HIRect *)
 * @param inDrawInfo cast=(const HIThemeTabPaneDrawInfo *)
 * @param inContext cast=(CGContextRef)
 * @param inOrientation cast=(HIThemeOrientation)
 */
public static final native int HIThemeDrawTabPane(CGRect inRect, HIThemeTabPaneDrawInfo inDrawInfo, int inContext, int inOrientation);
/**
 * @param inString cast=(CFStringRef)
 * @param inContext cast=(CGContextRef)
 */
public static final native int HIThemeDrawTextBox(int inString, CGRect inBounds, HIThemeTextInfo inTextInfo, int inContext, int inOrientation); 
/** @param inContext cast=(CGContextRef) */
public static final native int HIThemeDrawTrack(HIThemeTrackDrawInfo inDrawInfo, CGRect inGhostRect, int inContext, int inOrientation);
public static final native int HIThemeGetButtonBackgroundBounds(CGRect inBounds, HIThemeButtonDrawInfo inDrawInfo, CGRect outBounds);
public static final native int HIThemeGetButtonContentBounds(CGRect inBounds, HIThemeButtonDrawInfo inDrawInfo, CGRect outBounds);
public static final native int HIThemeGetScrollBarTrackRect(CGRect inBounds, HIScrollBarTrackInfo inTrackInfo, boolean inIsHoriz, CGRect outTrackBounds);     
/** @param inString cast=(CFStringRef) */
public static final native int HIThemeGetTextDimensions(int inString, float inWidth, HIThemeTextInfo inTextInfo, float[] outWidth, float[] outHeight, float[] outBaseline);
public static final native int HIThemeGetTrackBounds(HIThemeTrackDrawInfo inDrawInfo, CGRect outBounds);
public static final native int HIThemeGetTrackPartBounds(HIThemeTrackDrawInfo inDrawInfo, short inPartCode, CGRect outPartBounds);
public static final native int HIThemeGetTrackThumbPositionFromBounds(HIThemeTrackDrawInfo inDrawInfo, CGRect inThumbBounds, float[] outRelativePosition);  
/**
 * @param inDrawInfo cast=(HIThemeTrackDrawInfo *)
 * @param inThumbOffset cast=(HIPoint *)
 */
public static final native int HIThemeGetTrackThumbPositionFromOffset(HIThemeTrackDrawInfo inDrawInfo, CGPoint inThumbOffset, float[] outRelativePosition);
public static final native int HIThemeGetTrackLiveValue(HIThemeTrackDrawInfo inDrawInfo, float inRelativePosition, int[] outValue);
public static final native boolean HIThemeHitTestScrollBarArrows(CGRect inScrollBarBounds, HIScrollBarTrackInfo inTrackInfo, boolean inIsHoriz, CGPoint inPtHit, CGRect outTrackBounds, short[] outPartCode);
public static final native boolean HIThemeHitTestTrack(HIThemeTrackDrawInfo inDrawInfo, CGPoint inMousePoint, short[] outPartHit);
/**
 * @method flags=dynamic
 * @param inBrush cast=(ThemeBrush)
 * @param inInfo cast=(void *)
 * @param inContext cast=(CGContextRef)
 * @param inOrientation cast=(HIThemeOrientation)
 */
public static final native int HIThemeSetFill(int inBrush, int inInfo, int inContext, int inOrientation);
/** @method flags=dynamic */
public static final native int HIThemeSetTextFill(int inColor, int inInfo, int inContext, int inOrientation);
/**
 * @param parent cast=(HIViewRef)
 * @param child cast=(HIViewRef)
 */
public static final native int HIViewAddSubview(int parent, int child);
/** @param inView cast=(HIViewRef) */
public static final native int HIViewChangeAttributes(int inView, int inAttrsToSet, int inAttrsToClear);
/** @param inView cast=(HIViewRef) */
public static final native int HIViewChangeFeatures(int inView, int inFeaturesToSet, int inFeaturesToClear);
/**
 * @param inView cast=(HIViewRef)
 * @param inEvent cast=(EventRef)
 */
public static final native int HIViewClick(int inView, int inEvent);
/**
 * @param ioPoint cast=(HIPoint *)
 * @param inSourceView cast=(HIViewRef)
 * @param inDestView cast=(HIViewRef)
 */
public static final native int HIViewConvertPoint(CGPoint ioPoint, int inSourceView, int inDestView);
/**
 * @param inSourceView cast=(HIViewRef)
 * @param inDestView cast=(HIViewRef)
 */
public static final native int HIViewConvertRect(CGRect ioRect, int inSourceView, int inDestView);  
/**
 * @param ioRgn cast=(RgnHandle)
 * @param inSourceView cast=(HIViewRef)
 * @param inDestView cast=(HIViewRef)
 */
public static final native int HIViewConvertRegion(int ioRgn, int inSourceView, int inDestView); 
/**
 * @param inView cast=(HIViewRef)
 * @param inOptions cast=(OptionBits)
 * @param outFrame cast=(HIRect *)
 * @param outImage cast=(CGImageRef *)
 */
public static final native int HIViewCreateOffscreenImage(int inView, int inOptions, CGRect outFrame, int[] outImage);
/**
 * @param inContext cast=(CGContextRef)
 * @param inImage cast=(CGImageRef)
 */
public static final native int HIViewDrawCGImage(int inContext, CGRect inBounds, int inImage); 
/**
 * @param inStartView cast=(HIViewRef)
 * @param inID cast=(HIViewID *),flags=struct
 * @param outControl cast=(HIViewRef *)
 */
public static final native int HIViewFindByID(int inStartView, int inID, int[] outControl);
/** @param inView cast=(HIViewRef) */
public static final native int HIViewGetBounds(int inView, CGRect outRect);
/**
 * @param inView cast=(HIViewRef)
 * @param outFeatures cast=(HIViewFeatures *)
 */
public static final native int HIViewGetFeatures(int inView, int[] outFeatures);
/** @param inView cast=(HIViewRef) */
public static final native int HIViewGetFirstSubview(int inView);
/** @param inView cast=(HIViewRef) */
public static final native int HIViewGetLastSubview(int inView);
/** @param inView cast=(HIViewRef) */
public static final native int HIViewGetLayoutInfo (int inView, HILayoutInfo outLayoutInfo);
/** @param inView cast=(HIViewRef) */
public static final native int HIViewGetNextView(int inView);
/**
 * @param inView cast=(HIViewRef)
 * @param outRect cast=(HIRect *)
 */
public static final native int HIViewGetFrame(int inView, CGRect outRect);
/** @param inView cast=(HIViewRef) */
public static final native boolean HIViewGetNeedsDisplay(int inView);
/** @param wHandle cast=(WindowRef) */
public static final native int HIViewGetRoot(int wHandle);
/**
 * @param inView cast=(HIViewRef)
 * @param outMinSize cast=(HISize *)
 * @param outMaxSize cast=(HISize *)
 */
public static final native int HIViewGetSizeConstraints(int inView, CGRect outMinSize, CGRect outMaxSize);
/**
 * @param inView cast=(HIViewRef)
 * @param inPoint cast=(CGPoint *)
 * @param inDeep cast=(Boolean)
 * @param outView cast=(HIViewRef *)
 */
public static final native int HIViewGetSubviewHit(int inView, CGPoint inPoint, boolean inDeep, int[] outView);
/** @param inView cast=(HIViewRef) */
public static final native int HIViewGetSuperview(int inView);
/**
 * @param inView cast=(HIViewRef)
 * @param inEvent cast=(EventRef)
 * @param outView cast=(HIViewRef *)
 */
public static final native int HIViewGetViewForMouseEvent(int inView, int inEvent, int[] outView);
/** 
 * @param inGrowBoxView cast=(HIViewRef) 
 * @param inTransparent cast=(Boolean)
 */
public static final native int HIGrowBoxViewSetTransparent(int inGrowBoxView, boolean inTransparent);
/** @param inView cast=(HIViewRef) */
public static final native boolean HIViewIsDrawingEnabled (int inView);
/** @param inView cast=(HIViewRef) */
public static final native boolean HIViewIsVisible(int inView);
/** @param inView cast=(HIViewRef) */
public static final native int HIViewRemoveFromSuperview(int inView);
/** @param inView cast=(HIViewRef) */
public static final native int HIViewRender(int inView);
/** @param inView cast=(HIViewRef) */
public static final native int HIViewSetBoundsOrigin(int inView, float inX, float inY); 
/**
 * @param inView cast=(HIViewRef)
 * @param isEnabled cast=(Boolean)
 */
public static final native int HIViewSetDrawingEnabled(int inView, boolean isEnabled);
/**
 * @param inView cast=(HIViewRef)
 * @param inRect cast=(const HIRect *)
 */
public static final native int HIViewSetFrame(int inView, CGRect inRect);
/** @param inView cast=(HIViewRef) */
public static final native int HIViewSetLayoutInfo (int inView, HILayoutInfo outLayoutInfo);
/**
 * @param inView cast=(HIViewRef)
 * @param inNeedsDisplay cast=(Boolean)
 */
public static final native int HIViewSetNeedsDisplay(int inView, boolean inNeedsDisplay);
/**
 * @param inView cast=(HIViewRef)
 * @param inRgn cast=(RgnHandle)
 * @param inNeedsDisplay cast=(Boolean)
 */
public static final native int HIViewSetNeedsDisplayInRegion(int inView, int inRgn, boolean inNeedsDisplay);
/**
 * @param inView cast=(HIViewRef)
 * @param inVisible cast=(Boolean)
 */
public static final native int HIViewSetVisible(int inView, boolean inVisible);
/**
 * @param inView cast=(HIViewRef)
 * @param inOp cast=(HIViewZOrderOp)
 * @param inOther cast=(HIViewRef)
 */
public static final native int HIViewSetZOrder(int inView, int inOp, int inOther);
/**
 * @param inView cast=(HIViewRef)
 * @param inPartToClick cast=(HIViewPartCode)
 * @param modifiers cast=(UInt32)
 * @param outPartClicked cast=(ControlPartCode *)
 */
public static final native int HIViewSimulateClick(int inView, short inPartToClick, int modifiers, short[] outPartClicked);
/**
 * @param cHandle cast=(ControlRef)
 * @param where flags=struct
 * @param modifiers cast=(EventModifiers)
 * @param actionUPP cast=(ControlActionUPP)
 */
public static final native short HandleControlClick(int cHandle, Point where, int modifiers, int actionUPP);
public static final native short HiWord(int doubleWord);
/** @param wHandle cast=(WindowRef) */
public static final native void HideWindow(int wHandle);
/** @param menuID cast=(MenuID) */
public static final native void HiliteMenu(short menuID);
/** @param h cast=(Handle) */
public static final native void HLock(int h);
/** @param inContent cast=(const HMHelpContentRec *) */
public static final native int HMDisplayTag(HMHelpContentRec inContent);
public static final native int HMGetTagDelay (int [] outDelay);
public static final native int HMHideTag ();
public static final native int HMSetTagDelay (int inDelay);
/**
 * @param inControl cast=(ControlRef)
 * @param inContentUPP cast=(HMControlContentUPP)
 */
public static final native void HMInstallControlContentCallback(int inControl, int inContentUPP);  
/** @param h cast=(Handle) */
public static final native void HUnlock(int h);
/**
 * @param theIconRef cast=(IconRef)
 * @param whichIcons cast=(IconSelectorValue)
 * @param iconFamily cast=(IconFamilyHandle *)
 */
public static final native int IconRefToIconFamily(int theIconRef, int whichIcons, int[] iconFamily);
public static final native int InitContextualMenus();
public static final native void InitCursor();
/** @param window cast=(WindowRef) */
public static final native void HIWindowFlush(int window);
/**
 * @param inWindow cast=(WindowRef)
 * @param outOwner cast=(WindowRef*)
 */
public static final native boolean HIWindowIsDocumentModalTarget(int inWindow, int[] outOwner);  
/** @param callbacks cast=(DataBrowserCallbacks *),flags=init */
public static final native int InitDataBrowserCallbacks(DataBrowserCallbacks callbacks);
public static final native int InitDataBrowserCustomCallbacks(DataBrowserCustomCallbacks callbacks); 
/**
 * @param mHandle cast=(MenuRef)
 * @param beforeID cast=(MenuID)
 */
public static final native void InsertMenu(int mHandle, short beforeID);
/**
 * @param mHandle cast=(MenuRef)
 * @param sHandle cast=(CFStringRef)
 * @param index cast=(MenuItemIndex)
 * @param attributes cast=(MenuItemAttributes)
 * @param commandID cast=(MenuCommand)
 */
public static final native int InsertMenuItemTextWithCFString(int mHandle, int sHandle, short index, int attributes, int commandID);
/**
 * @param inTarget cast=(EventTargetRef)
 * @param inHandler cast=(EventHandlerUPP)
 * @param inNumTypes cast=(UInt32)
 * @param inList cast=(const EventTypeSpec *)
 * @param inUserData cast=(void *)
 * @param outRef cast=(EventHandlerRef *)
 */
public static final native int InstallEventHandler(int inTarget, int inHandler, int inNumTypes, int[] inList, int inUserData, int[] outRef);
/**
 * @param inEventLoop cast=(EventLoopRef)
 * @param inFireDelay cast=(EventTimerInterval)
 * @param inInterval cast=(EventTimerInterval)
 * @param inTimerProc cast=(EventLoopIdleTimerUPP)
 * @param inTimerData cast=(void *)
 * @param outTimer cast=(EventLoopTimerRef *)
 */
public static final native int InstallEventLoopIdleTimer(int inEventLoop, double inFireDelay, double inInterval, int inTimerProc, int inTimerData, int[] outTimer);
/**
 * @param inEventLoop cast=(EventLoopRef)
 * @param inFireDelay cast=(EventTimerInterval)
 * @param inInterval cast=(EventTimerInterval)
 * @param inTimerProc cast=(EventLoopTimerUPP)
 * @param inTimerData cast=(void *)
 * @param outTimer cast=(EventLoopTimerRef *)
 */
public static final native int InstallEventLoopTimer(int inEventLoop, double inFireDelay, double inInterval, int inTimerProc, int inTimerData, int[] outTimer);
/**
 * @param receiveHandler cast=(DragReceiveHandlerUPP)
 * @param theWindow cast=(WindowRef)
 * @param handlerRefCon cast=(void *)
 */
public static final native int InstallReceiveHandler(int receiveHandler,int theWindow, int[] handlerRefCon);
/**
 * @param trackingHandler cast=(DragTrackingHandlerUPP)
 * @param theWindow cast=(WindowRef)
 * @param handlerRefCon cast=(void *)
 */
public static final native int InstallTrackingHandler(int trackingHandler,int theWindow, int[] handlerRefCon);
/**
 * @param wHandle cast=(WindowRef)
 * @param bounds cast=(const Rect *)
 */
public static final native void InvalWindowRect(int wHandle, Rect bounds);
/**
 * @param wHandle cast=(WindowRef)
 * @param rgnHandle cast=(RgnHandle)
 */
public static final native void InvalWindowRgn(int wHandle, int rgnHandle);
/** @param r cast=(const Rect *) */
public static final native void InvertRect(Rect r);
/** @param inControl cast=(ControlRef) */
public static final native boolean IsControlActive(int inControl);
/** @param cHandle cast=(ControlRef) */
public static final native boolean IsControlEnabled(int cHandle);
/** @param cHandle cast=(ControlRef) */
public static final native boolean IsControlVisible(int cHandle);
/**
 * @param cHandle cast=(ControlRef)
 * @param itemID cast=(DataBrowserItemID)
 */
public static final native boolean IsDataBrowserItemSelected(int cHandle, int itemID);
/**
 * @param inQueue cast=(EventQueueRef)
 * @param inEvent cast=(EventRef)
 */
public static final native boolean IsEventInQueue(int inQueue, int inEvent);
/**
 * @param mHandle cast=(MenuRef)
 * @param commandId cast=(MenuCommand)
 */
public static final native boolean IsMenuCommandEnabled(int mHandle, int commandId);
/**
 * @param mHandle cast=(MenuRef)
 * @param index cast=(MenuItemIndex)
 */
public static final native boolean IsMenuItemEnabled(int mHandle, short index);
/**
 * @param inStartMenu cast=(MenuRef)
 * @param inEvent cast=(EventRef)
 * @param inOptions cast=(MenuEventOptions)
 * @param outMenu cast=(MenuRef *)
 * @param outMenuItem cast=(MenuItemIndex *)
 */
public static final native boolean IsMenuKeyEvent(int inStartMenu, int inEvent, int inOptions, int[] outMenu, short[] outMenuItem);
/** @param cHandle cast=(ControlRef) */
public static final native boolean IsValidControlHandle(int cHandle);
/** @param mHandle cast=(MenuRef) */
public static final native boolean IsValidMenu(int mHandle);
/** @param grafPort cast=(WindowRef) */
public static final native boolean IsValidWindowPtr(int grafPort);
/** @param window cast=(WindowRef) */
public static final native boolean IsWindowActive(int window);
/** @param window cast=(WindowRef) */
public static final native boolean IsWindowCollapsed(int window);
/** @param window cast=(WindowRef) */
public static final native boolean IsWindowModified(int window);
/** @param window cast=(WindowRef) */
public static final native boolean IsWindowVisible(int window);
/**
 * @param transData cast=(const void *)
 * @param state cast=(UInt32 *)
 */
public static final native int KeyTranslate(int transData, short keycode, int[] state);
/** @param oKeyboardLayout cast=(KeyboardLayoutRef *) */
public static final native int KLGetCurrentKeyboardLayout(int[] oKeyboardLayout);
/**
 * @param iKeyboardLayout cast=(KeyboardLayoutRef)
 * @param iPropertyTag cast=(KeyboardLayoutPropertyTag)
 * @param oValue cast=(const void **)
 */
public static final native int KLGetKeyboardLayoutProperty(int iKeyboardLayout, int iPropertyTag, int[] oValue);
public static final native byte LMGetKbdType();
/**
 * @param keyLayoutPtr cast=(const UCKeyboardLayout *)
 * @param virtualKeyCode cast=(UInt16)
 * @param keyAction cast=(UInt16)
 * @param modifierKeyState cast=(UInt32)
 * @param keyboardType cast=(UInt32)
 * @param keyTranslateOptions cast=(OptionBits)
 * @param deadKeyState cast=(UInt32 *)
 * @param maxStringLength cast=(UniCharCount)
 * @param actualStringLength cast=(UniCharCount *)
 * @param unicodeString cast=(UniChar *)
 */
public static final native int UCKeyTranslate (int keyLayoutPtr, short virtualKeyCode, short keyAction, int modifierKeyState, int keyboardType, int keyTranslateOptions, int[] deadKeyState, int maxStringLength, int[] actualStringLength, char[] unicodeString);
/** @param pictHandle cast=(PicHandle) */
public static final native void KillPicture(int pictHandle);
/**
 * @param h cast=(short)
 * @param v cast=(short)
 */
public static final native void LineTo(short h, short v);
public static final native int Long2Fix(int x);
public static final native short LoWord(int doubleWord);
/**
 * @param inCreator cast=(OSType)
 * @param inBundleID cast=(CFStringRef)
 * @param inName cast=(CFStringRef)
 * @param outAppRef cast=(FSRef *)
 * @param outAppURL cast=(CFURLRef *)
 */
public static final native int LSFindApplicationForInfo(int inCreator, int inBundleID, int inName, byte[] outAppRef, int[] outAppURL);
/**
 * @method flags=dynamic
 * @param inContentType cast=(CFStringRef)
 */
public static final native int LSCopyAllRoleHandlersForContentType(int inContentType, int inRoleMask);
/**
 * @param inItem cast=(const FSRef *)
 * @param inRoles cast=(LSRolesMask)
 * @param inAttributeName cast=(CFStringRef)
 * @param outValue cast=(CFTypeRef *)
 */
public static final native int  LSCopyItemAttribute (byte[] inItem, int inRoles, int inAttributeName, int[] outValue);
/**
 * @param inType cast=(OSType)
 * @param inCreator cast=(OSType)
 * @param inExtension cast=(CFStringRef)
 * @param inRoleMask cast=(LSRolesMask)
 * @param outAppRef cast=(FSRef *)
 * @param outAppURL cast=(CFURLRef *)
 */
public static final native int LSGetApplicationForInfo(int inType, int inCreator,int inExtension, int inRoleMask, byte[] outAppRef, int[] outAppURL);
/**
 * @param inURLs cast=(CFArrayRef)
 * @param inAEParam cast=(const AEKeyDesc *)
 * @param inAppParams cast=(const LSApplicationParameters *)
 * @param outPSNs cast=(ProcessSerialNumber *)
 */
public static final native int LSOpenURLsWithRole(int inURLs, int inRole, int inAEParam, LSApplicationParameters inAppParams, int[] outPSNs, int inMaxPSNCount);
/**
 * @param inURL cast=(CFURLRef)
 * @param outLaunchedURL cast=(CFURLRef *)
 */
public static final native int LSOpenCFURLRef(int inURL, int[] outLaunchedURL);
/** @param outPSN cast=(ProcessSerialNumber *) */
public static final native int LSOpenApplication (LSApplicationParameters inAppParams, int[] outPSN);
/**
 * @param inRef cast=(const FSRef *)
 * @param outDisplayName cast=(CFStringRef *)
 */
public static final native int LSCopyDisplayNameForRef(byte[] inRef, int[] outDisplayName);
/** @param mHandle cast=(Point *),flags=struct */
public static final native int MenuSelect(Point mHandle);
/**
 * @param theControl cast=(ControlRef)
 * @param h cast=(SInt16)
 * @param v cast=(SInt16)
 */
public static final native void MoveControl(int theControl, short h, short v);
/**
 * @param h cast=(short)
 * @param v cast=(short)
 */
public static final native void MoveTo(short h, short v);
/**
 * @param wHandle cast=(WindowRef)
 * @param h cast=(short)
 * @param v cast=(short)
 * @param toFront cast=(Boolean)
 */
public static final native void MoveWindow(int wHandle, short h, short v, boolean toFront);
/**
 * @param inOptions cast=(const NavDialogCreationOptions *)
 * @param inEventProc cast=(NavEventUPP)
 * @param inFilterProc cast=(NavObjectFilterUPP)
 * @param inClientData cast=(void *)
 * @param outDialog cast=(NavDialogRef *)
 */
public static final native int NavCreateChooseFolderDialog(NavDialogCreationOptions inOptions, int inEventProc, int inFilterProc, int inClientData, int[] outDialog);
/**
 * @param inOptions cast=(const NavDialogCreationOptions *)
 * @param inTypeList cast=(NavTypeListHandle)
 * @param inEventProc cast=(NavEventUPP)
 * @param inPreviewProc cast=(NavPreviewUPP)
 * @param inFilterProc cast=(NavObjectFilterUPP)
 * @param inClientData cast=(void *)
 * @param outDialog cast=(NavDialogRef *)
 */
public static final native int NavCreateGetFileDialog(NavDialogCreationOptions inOptions, int inTypeList, int inEventProc, int inPreviewProc, int inFilterProc, int inClientData, int[] outDialog);
/**
 * @param inOptions cast=(const NavDialogCreationOptions *)
 * @param inFileType cast=(OSType)
 * @param inFileCreator cast=(OSType)
 * @param inEventProc cast=(NavEventUPP)
 * @param inClientData cast=(void *)
 * @param outDialog cast=(NavDialogRef *)
 */
public static final native int NavCreatePutFileDialog(NavDialogCreationOptions inOptions, int inFileType, int inFileCreator, int inEventProc, int inClientData, int[] outDialog);
/**
 * @param dialog cast=(NavDialogRef)
 * @param selector cast=(NavCustomControlMessage)
 */
public static final native int NavCustomControl(int dialog, int selector, AEDesc parms);
/**
 * @param dialog cast=(NavDialogRef)
 * @param selector cast=(NavCustomControlMessage)
 */
public static final native int NavCustomControl(int dialog, int selector, NavMenuItemSpec parms);
/** @param dialogHandle cast=(NavDialogRef) */
public static final native void NavDialogDispose(int dialogHandle);
/** @param dialogHandle cast=(NavDialogRef) */
public static final native int NavDialogGetSaveFileName(int dialogHandle);
/** @param dialogHandle cast=(NavDialogRef) */
public static final native int NavDialogGetUserAction(int dialogHandle);
/** @param dialog cast=(NavDialogRef) */
public static final native int NavDialogGetWindow(int dialog);
/** @param dialogHandle cast=(NavDialogRef) */
public static final native int NavDialogRun(int dialogHandle);
/**
 * @param dialogHandle cast=(NavDialogRef)
 * @param fileNameHandle cast=(CFStringRef)
 */
public static final native int NavDialogSetSaveFileName(int dialogHandle, int fileNameHandle);
/** @method flags=dynamic */
public static final native int NavDialogSetFilterTypeIdentifiers(int inGetFileDialog, int inTypeIdentifiers);  
/** @param outOptions cast=(NavDialogCreationOptions *) */
public static final native int NavGetDefaultDialogCreationOptions(NavDialogCreationOptions outOptions);
/**
 * @param inDialog cast=(NavDialogRef)
 * @param outReply cast=(NavReplyRecord *)
 */
public static final native int NavDialogGetReply(int inDialog, NavReplyRecord outReply);
public static final native int NavDisposeReply(NavReplyRecord reply); 
/**
 * @param owningWindow cast=(WindowRef)
 * @param boundsRect cast=(const Rect *)
 * @param controlTitle cast=(ConstStr255Param)
 * @param initiallyVisible cast=(Boolean)
 * @param initialValue cast=(SInt16)
 * @param minimumValue cast=(SInt16)
 * @param maximumValue cast=(SInt16)
 * @param procID cast=(SInt16)
 * @param controlReference cast=(SInt32)
 */
public static final native int NewControl(int owningWindow, Rect boundsRect, byte[] controlTitle, boolean initiallyVisible, short initialValue, short minimumValue, short maximumValue, short procID, int controlReference);
/** @param theDrag cast=(DragRef *) */
public static final native int NewDrag(int[] theDrag); 
/**
 * @param offscreenGWorld cast=(GWorldPtr *)
 * @param PixelFormat cast=(unsigned long)
 * @param boundsRect cast=(const Rect *)
 * @param cTable cast=(CTabHandle)
 * @param aGDevice cast=(GDHandle)
 * @param flags cast=(GWorldFlags)
 * @param newBuffer cast=(Ptr)
 * @param rowBytes cast=(long)
 */
public static final native int NewGWorldFromPtr(int[] offscreenGWorld, int PixelFormat, Rect boundsRect, int cTable, int aGDevice, int flags, int newBuffer, int rowBytes);
/** @param size cast=(Size) */
public static final native int NewHandle(int size);
/** @param size cast=(Size) */
public static final native int NewHandleClear(int size);
/** @param size cast=(Size) */
public static final native int NewPtr(int size);
/** @param size cast=(Size) */
public static final native int NewPtrClear(int size);
public static final native int NewRgn();
/**
 * @param supportedInterfaceTypes cast=(OSType *)
 * @param idocID cast=(TSMDocumentID *)
 */
public static final native int NewTSMDocument(short numOfInterface, int[] supportedInterfaceTypes, int[] idocID, long refcon);
public static final native void OffsetRect(Rect rect, short dh, short dv);
/**
 * @param rgnHandle cast=(RgnHandle)
 * @param dh cast=(short)
 * @param dv cast=(short)
 */
public static final native void OffsetRgn(int rgnHandle, short dh, short dv);
/**
 * @param cHandle cast=(ControlRef)
 * @param container cast=(DataBrowserItemID)
 */
public static final native int OpenDataBrowserContainer(int cHandle, int container);
public static final native void OpenRgn();
/** @param rect cast=(const Rect *) */
public static final native int OpenPicture(Rect rect);
/** @param theColorInfo cast=(ColorPickerInfo *) */
public static final native int PickColor(ColorPickerInfo theColorInfo);
/**
 * @param mHandle cast=(MenuRef)
 * @param top cast=(short)
 * @param left cast=(short)
 * @param popUpItem cast=(short)
 */
public static final native int PopUpMenuSelect(int mHandle, short top, short left, short popUpItem);
/**
 * @param eventNum cast=(EventKind)
 * @param eventMsg cast=(UInt32)
 */
public static final native int PostEvent(short eventNum, int eventMsg);
/**
 * @param inQueue cast=(EventQueueRef)
 * @param inEvent cast=(EventRef)
 * @param inPriority cast=(EventPriority)
 */
public static final native int PostEventToQueue(int inQueue, int inEvent, short inPriority);
/** @param pageFormat cast=(PMPageFormat *) */
public static final native int PMCreatePageFormat(int[] pageFormat);
/** @param printSettings cast=(PMPrintSettings *) */
public static final native int PMCreatePrintSettings(int[] printSettings);
/** @param printSession cast=(PMPrintSession *) */
public static final native int PMCreateSession(int[] printSession);
/**
 * @param pageFormat cast=(PMPageFormat)
 * @param flatFormat cast=(Handle *)
 */
public static final native int PMFlattenPageFormat(int pageFormat, int[] flatFormat);
/**
 * @param printSettings cast=(PMPrintSettings)
 * @param flatSettings cast=(Handle *)
 */
public static final native int PMFlattenPrintSettings(int printSettings, int[] flatSettings);
/**
 * @param pageFormat cast=(PMPageFormat)
 * @param pageRect cast=(PMRect *)
 */
public static final native int PMGetAdjustedPageRect(int pageFormat, PMRect pageRect);
/**
 * @param pageFormat cast=(PMPageFormat)
 * @param paperRect cast=(PMRect *)
 */
public static final native int PMGetAdjustedPaperRect(int pageFormat, PMRect paperRect);
/** @param printSettings cast=(PMPrintSettings) */
public static final native int PMGetCollate(int printSettings, boolean[] collate);
/**
 * @param printSettings cast=(PMPrintSettings)
 * @param copies cast=(UInt32 *)
 */
public static final native int PMGetCopies(int printSettings, int[] copies);
/**
 * @param printSettings cast=(PMPrintSettings)
 * @param first cast=(UInt32 *)
 */
public static final native int PMGetFirstPage(int printSettings, int[] first);
/**
 * @param printSettings cast=(PMPrintSettings)
 * @param name cast=(CFStringRef *)
 */
public static final native int PMGetJobNameCFString(int printSettings, int[] name);
/**
 * @param printSettings cast=(PMPrintSettings)
 * @param last cast=(UInt32 *)
 */
public static final native int PMGetLastPage(int printSettings, int[] last);
/**
 * @param pageFormat cast=(PMPageFormat)
 * @param orientation cast=(PMOrientation *)
 */
public static final native int PMGetOrientation(int pageFormat, short[] orientation);
/**
 * @param printSettings cast=(PMPrintSettings)
 * @param minPage cast=(UInt32 *)
 * @param maxPage cast=(UInt32 *)
 */
public static final native int PMGetPageRange(int printSettings, int[] minPage, int[] maxPage);
/**
 * @method flags=dynamic
 * @param i cast=(PMPrinter)
 * @param printSettings cast=(PMPrintSettings)
 * @param resolution cast=(PMResolution *)
 */
public static final native int PMPrinterGetOutputResolution(int i, int printSettings, PMResolution resolution);
/** @param printManagerObject cast=(PMObject) */
public static final native int PMRelease(int printManagerObject);
/**
 * @param pageFormat cast=(PMPageFormat)
 * @param resolution cast=(PMResolution *)
 */
public static final native int PMGetResolution(int pageFormat, PMResolution resolution);
/**
 * @param printSession cast=(PMPrintSession)
 * @param printSettings cast=(PMPrintSettings)
 * @param pageFormat cast=(PMPageFormat)
 */
public static final native int PMSessionBeginDocumentNoDialog(int printSession, int printSettings, int pageFormat);
/**
 * @param printSession cast=(PMPrintSession)
 * @param pageFormat cast=(PMPageFormat)
 * @param pageFrame cast=(const PMRect *)
 */
public static final native int PMSessionBeginPageNoDialog(int printSession, int pageFormat, PMRect pageFrame);
/**
 * @param printSession cast=(PMPrintSession)
 * @param printSettings cast=(PMPrintSettings)
 * @param destLocationP cast=(CFURLRef *)
 */
public static final native int PMSessionCopyDestinationLocation(int printSession, int printSettings, int[] destLocationP);
/**
 * @param printSession cast=(PMPrintSession)
 * @param printerList cast=(CFArrayRef *)
 * @param currentIndex cast=(CFIndex *)
 * @param currentPrinter cast=(PMPrinter *)
 */
public static final native int PMSessionCreatePrinterList(int printSession, int[] printerList, int[] currentIndex, int[] currentPrinter);
/**
 * @param printSession cast=(PMPrintSession)
 * @param pageFormat cast=(PMPageFormat)
 */
public static final native int PMSessionDefaultPageFormat(int printSession, int pageFormat);
/**
 * @param printSession cast=(PMPrintSession)
 * @param printSettings cast=(PMPrintSettings)
 */
public static final native int PMSessionDefaultPrintSettings(int printSession, int printSettings);
/** @param printSession cast=(PMPrintSession) */
public static final native int PMSessionEndDocumentNoDialog(int printSession);
/** @param printSession cast=(PMPrintSession) */
public static final native int PMSessionEndPageNoDialog(int printSession);
/** @param printSession cast=(PMPrintSession) */
public static final native int PMSessionError(int printSession);
/**
 * @param printSession cast=(PMPrintSession)
 * @param printer cast=(PMPrinter *)
 */
public static final native int PMSessionGetCurrentPrinter(int printSession, int[] printer);
/**
 * @param printSession cast=(PMPrintSession)
 * @param printSettings cast=(PMPrintSettings)
 * @param destTypeP cast=(PMDestinationType *)
 */
public static final native int PMSessionGetDestinationType(int printSession, int printSettings, short[] destTypeP); 
/**
 * @param printSession cast=(PMPrintSession)
 * @param graphicsType cast=(CFStringRef)
 * @param graphicsContext cast=(void **)
 */
public static final native int PMSessionGetGraphicsContext(int printSession, int graphicsType, int[] graphicsContext);
/**
 * @param printSession cast=(PMPrintSession)
 * @param pageFormat cast=(PMPageFormat)
 * @param result cast=(Boolean *)
 */
public static final native int PMSessionPageSetupDialog(int printSession, int pageFormat, boolean[] result);	
/**
 * @param printSession cast=(PMPrintSession)
 * @param settings cast=(PMPrintSettings)
 * @param pageFormat cast=(PMPageFormat)
 * @param accepted cast=(Boolean *)
 */
public static final native int PMSessionPrintDialog(int printSession, int settings, int pageFormat, boolean[] accepted);
/**
 * @param session cast=(PMPrintSession)
 * @param printerName cast=(CFStringRef)
 */
public static final native int PMSessionSetCurrentPrinter(int session, int printerName);
/**
 * @param printSession cast=(PMPrintSession)
 * @param printSettings cast=(PMPrintSettings)
 * @param destType cast=(PMDestinationType)
 * @param destFormat cast=(CFStringRef)
 * @param destLocation cast=(CFURLRef)
 */
public static final native int PMSessionSetDestination(int printSession, int printSettings, short destType, int destFormat, int destLocation);
/** @param printSession cast=(PMPrintSession) */
public static final native int PMSessionSetError(int printSession, int printError);
/**
 * @param printSession cast=(PMPrintSession)
 * @param docFormat cast=(CFStringRef)
 * @param graphicsContexts cast=(CFArrayRef)
 * @param options cast=(CFTypeRef)
 */
public static final native int PMSessionSetDocumentFormatGeneration(int printSession, int docFormat, int graphicsContexts, int options);
/**
 * @param printSession cast=(PMPrintSession)
 * @param documentWindow cast=(WindowRef)
 * @param sheetDoneProc cast=(PMSheetDoneUPP)
 */
public static final native int PMSessionUseSheets(int printSession, int documentWindow, int sheetDoneProc); 
/**
 * @param printSession cast=(PMPrintSession)
 * @param pageFormat cast=(PMPageFormat)
 * @param result cast=(Boolean *)
 */
public static final native int PMSessionValidatePageFormat(int printSession, int pageFormat, boolean[] result);
/**
 * @param printSession cast=(PMPrintSession)
 * @param printSettings cast=(PMPrintSettings)
 * @param result cast=(Boolean *)
 */
public static final native int PMSessionValidatePrintSettings(int printSession, int printSettings, boolean[] result);
/** @param printSettings cast=(PMPrintSettings) */
public static final native int PMSetCollate(int printSettings, boolean collate);
/**
 * @param printSettings cast=(PMPrintSettings)
 * @param copies cast=(UInt32)
 * @param lock cast=(Boolean)
 */
public static final native int PMSetCopies(int printSettings, int copies, boolean lock);
/**
 * @param printSettings cast=(PMPrintSettings)
 * @param first cast=(UInt32)
 * @param lock cast=(Boolean)
 */
public static final native int PMSetFirstPage(int printSettings, int first, boolean lock);
/**
 * @param printSettings cast=(PMPrintSettings)
 * @param name cast=(CFStringRef)
 */
public static final native int PMSetJobNameCFString(int printSettings, int name); 
/**
 * @param printSettings cast=(PMPrintSettings)
 * @param last cast=(UInt32)
 * @param lock cast=(Boolean)
 */
public static final native int PMSetLastPage(int printSettings, int last, boolean lock);
/**
 * @param pageFormat cast=(PMPageFormat)
 * @param orientation cast=(PMOrientation)
 * @param lock cast=(Boolean)
 */
public static final native void PMSetOrientation(int pageFormat, short orientation, boolean lock);
/**
 * @param printSettings cast=(PMPrintSettings)
 * @param minPage cast=(UInt32)
 * @param maxPage cast=(UInt32)
 */
public static final native int PMSetPageRange(int printSettings, int minPage, int maxPage);
/** @method flags=dynamic */
public static final native int PMShowPrintDialogWithOptions(int printSession, int printSettings, int pageFormat, int printDialogOptions, boolean[] accepted);
/**
 * @param flatFormat cast=(Handle)
 * @param pageFormat cast=(PMPageFormat *)
 */
public static final native int PMUnflattenPageFormat(int flatFormat, int[] pageFormat); 
/**
 * @param flatSettings cast=(Handle)
 * @param printSettings cast=(PMPrintSettings *)
 */
public static final native int PMUnflattenPrintSettings(int flatSettings, int[] printSettings); 
/**
 * @param pt cast=(Point *),flags=struct
 * @param r cast=(const Rect *)
 */
public static final native boolean PtInRect(Point pt, Rect r);
/**
 * @param pt cast=(Point *),flags=struct
 * @param rgnHandle cast=(RgnHandle)
 */
public static final native boolean PtInRgn(Point pt, int rgnHandle);
/**
 * @param scrap cast=(ScrapRef)
 * @param flavorType cast=(ScrapFlavorType)
 * @param flavorFlags cast=(ScrapFlavorFlags)
 * @param flavorSize cast=(Size)
 * @param flavorData cast=(const void *)
 */
public static final native int PutScrapFlavor(int scrap, int flavorType, int flavorFlags, int flavorSize, byte[] flavorData);
/**
 * @param scrap cast=(ScrapRef)
 * @param flavorType cast=(ScrapFlavorType)
 * @param flavorFlags cast=(ScrapFlavorFlags)
 * @param flavorSize cast=(Size)
 * @param flavorData cast=(const void *)
 */
public static final native int PutScrapFlavor(int scrap, int flavorType, int flavorFlags, int flavorSize, char[] flavorData);
/**
 * @param inPort cast=(CGrafPtr)
 * @param outContext cast=(CGContextRef *)
 */
public static final native int QDBeginCGContext(int inPort, int[] outContext);
/**
 * @param inPort cast=(CGrafPtr)
 * @param inoutContext cast=(CGContextRef *)
 */
public static final native int QDEndCGContext(int inPort, int[] inoutContext);
/**
 * @param port cast=(CGrafPtr)
 * @param rgnHandle cast=(RgnHandle)
 */
public static final native void QDFlushPortBuffer(int port, int rgnHandle);
/** @param provider cast=(CGDataProviderRef) */
public static final native int QDPictCreateWithProvider(int provider);
/**
 * @param ctx cast=(CGContextRef)
 * @param rect cast=(CGRect *),flags=struct
 * @param picRef cast=(QDPictRef)
 */
public static final native int QDPictDrawToCGContext(int ctx, CGRect rect, int picRef);
/** @param pictRef cast=(QDPictRef) */
public static final native void QDPictRelease(int pictRef);
/**
 * @method flags=no_gen
 * @param picture cast=(QDPictRef)
 * @param rect cast=(CGRect *)
 */
public static final native void QDPictGetBounds(int picture, CGRect rect);
/**
 * @param rgn cast=(RgnHandle)
 * @param dir cast=(QDRegionParseDirection)
 * @param proc cast=(RegionToRectsUPP)
 * @param userData cast=(void *)
 */
public static final native int QDRegionToRects(int rgn, int dir, int proc, int userData);
/** @param color cast=(const RGBColor *) */
public static final native void RGBBackColor(RGBColor color);
/** @param color cast=(const RGBColor *) */
public static final native void RGBForeColor(RGBColor color);
/**
 * @param iconFile cast=(const FSSpec *)
 * @param iconFamily cast=(IconFamilyHandle *)
 */
public static final native int ReadIconFile (byte[] iconFile, int[] iconFamily);
/**
 * @param inNumTypes cast=(UInt32)
 * @param inList cast=(const EventTypeSpec *)
 * @param inTimeout cast=(EventTimeout)
 * @param inPullEvent cast=(Boolean)
 * @param outEvent cast=(EventRef *)
 */
public static final native int ReceiveNextEvent(int inNumTypes, int[] inList, double inTimeout, boolean inPullEvent, int[] outEvent);
/**
 * @param rect cast=(const Rect *)
 * @param rgnHandle cast=(RgnHandle)
 */
public static final native boolean RectInRgn(Rect rect, int rgnHandle);
/**
 * @param rgnHandle cast=(RgnHandle)
 * @param left cast=(const Rect *)
 */
public static final native void RectRgn(int rgnHandle, Rect left);
public static final native int RegisterAppearanceClient();
/** @param theEvent cast=(EventRef) */
public static final native void ReleaseEvent(int theEvent);
/** @param theIconRef cast=(IconRef) */
public static final native void ReleaseIconRef(int theIconRef);
/** @param mHandle cast=(MenuRef) */
public static final native int ReleaseMenu(int mHandle);
/** @param inGroup cast=(WindowGroupRef) */
public static final native int ReleaseWindowGroup (int inGroup);
/** @param inWindow cast=(WindowRef) */
public static final native int ReleaseWindow(int inWindow); 
/** @param control cast=(ControlRef) */
public static final native int RemoveControlProperty(int control, int propertyCreator, int propertyTag);
/**
 * @param cHandle cast=(ControlRef)
 * @param containerID cast=(DataBrowserItemID)
 * @param numItems cast=(UInt32)
 * @param itemIDs cast=(const DataBrowserItemID *)
 * @param preSortProperty cast=(DataBrowserPropertyID)
 */
public static final native int RemoveDataBrowserItems(int cHandle, int containerID, int numItems, int[] itemIDs, int preSortProperty);
/**
 * @param browser cast=(ControlRef)
 * @param column cast=(DataBrowserTableViewColumnID)
 */
public static final native int RemoveDataBrowserTableViewColumn(int browser, int column);
/**
 * @param inQueue cast=(EventQueueRef)
 * @param inEvent cast=(EventRef)
 */
public static final native int RemoveEventFromQueue(int inQueue, int inEvent);
/** @param inHandlerRef cast=(EventHandlerRef) */
public static final native int RemoveEventHandler(int inHandlerRef);
/** @param inTimer cast=(EventLoopTimerRef) */
public static final native int RemoveEventLoopTimer(int inTimer);
/**
 * @param receiveHandler cast=(DragReceiveHandlerUPP)
 * @param theWindow cast=(WindowRef)
 */
public static final native int RemoveReceiveHandler(int receiveHandler,int theWindow);
/**
 * @param trackingHandler cast=(DragTrackingHandlerUPP)
 * @param theWindow cast=(WindowRef)
 */
public static final native int RemoveTrackingHandler(int trackingHandler,int theWindow);
/**
 * @param window cast=(WindowRef)
 * @param parentWindow cast=(WindowRef)
 */
public static final native int RepositionWindow(int window, int parentWindow, int method);
/** @param window cast=(WindowRef) */
public static final native int ReshapeCustomWindow(int window);
public static final native int RestoreApplicationDockTileImage();
/** @param inEvent cast=(EventRef) */
public static final native int RetainEvent(int inEvent);
/** @param mHandle cast=(MenuRef) */
public static final native int RetainMenu(int mHandle);
/** @param inWindow cast=(WindowRef) */
public static final native int RetainWindow(int inWindow); 
/**
 * @param browser cast=(ControlRef)
 * @param item cast=(DataBrowserItemID)
 * @param property cast=(DataBrowserPropertyID)
 * @param options cast=(DataBrowserRevealOptions)
 */
public static final native int RevealDataBrowserItem(int browser, int item, int property, byte options);
/**
 * @param dialogHandle cast=(DialogRef)
 * @param modalFilterUPP cast=(ModalFilterUPP)
 * @param itemHit cast=(DialogItemIndex *)
 */
public static final native int RunStandardAlert(int dialogHandle, int modalFilterUPP, short[] itemHit);
/**
 * @param psn1 cast=(ProcessSerialNumber *)
 * @param psn2 cast=(ProcessSerialNumber *)
 */
public static final native int SameProcess(int [] psn1, int[] psn2, boolean[] result); 
/**
 * @param rect cast=(const Rect *)
 * @param dh cast=(short)
 * @param dv cast=(short)
 * @param updateRgn cast=(RgnHandle)
 */
public static final native void ScrollRect(Rect rect, short dh, short dv, int updateRgn);

/**
 * @param certType cast=(CSSM_CERT_TYPE)
 * @param policyOID cast=(CSSM_OID *)
 * @param value cast=(CSSM_DATA *)
 * @param policySearch cast=(SecPolicySearchRef *) 
 */
public static final native int SecPolicySearchCreate(int certType, int policyOID, int value, int[] policySearch);

/**
 * @param searchRef cast=(SecPolicySearchRef)
 * @param policyRef cast=(SecPolicyRef *)
 */
public static final native int SecPolicySearchCopyNext(int searchRef, int[] policyRef);

/** 
 * @param certificates cast=(CFArrayRef)
 * @param policies cast=(CFTypeRef)
 * @param trustRef cast=(SecTrustRef *) 
 */
public static final native int SecTrustCreateWithCertificates(int certificates, int policies, int[] trustRef);

/**
 * @param src1 flags=no_out
 * @param src2 flags=no_out
 * @param dstRect flags=no_in
 */
public static final native boolean SectRect(Rect src1, Rect src2, Rect dstRect);
/**
 * @param srcRgnA cast=(RgnHandle)
 * @param srcRgnB cast=(RgnHandle)
 * @param dstRgn cast=(RgnHandle)
 */
public static final native void SectRgn(int srcRgnA, int srcRgnB, int dstRgn);
/** @param wHandle cast=(WindowRef) */
public static final native void SelectWindow(int wHandle);
/**
 * @param window cast=(WindowRef)
 * @param behindWindow cast=(WindowRef)
 */
public static final native void SendBehind(int window, int behindWindow);
/**
 * @param theEvent cast=(EventRef)
 * @param theTarget cast=(EventTargetRef)
 */
public static final native int SendEventToEventTarget(int theEvent, int theTarget);
/**
 * @param theEvent cast=(EventRef)
 * @param theTarget cast=(EventTargetRef)
 */
public static final native int SendEventToEventTargetWithOptions(int theEvent, int theTarget, int options);
/** @param inImage cast=(CGImageRef) */
public static final native int SetApplicationDockTileImage(int inImage);
/**
 * @param inWindow cast=(WindowRef)
 * @param inTracks cast=(Boolean)
 */
public static final native int SetAutomaticControlDragTrackingEnabledForWindow (int inWindow, boolean inTracks);
/**
 * @param inButton cast=(ControlRef)
 * @param inContent cast=(ControlButtonContentInfoPtr)
 */
public static final native int SetBevelButtonContentInfo(int inButton, ControlButtonContentInfo inContent);
/** @param rgnHandle cast=(RgnHandle) */
public static final native void SetClip(int rgnHandle);
/**
 * @param cHandle cast=(ControlRef)
 * @param maximum cast=(SInt32)
 */
public static final native void SetControl32BitMaximum(int cHandle, int maximum);
/**
 * @param cHandle cast=(ControlRef)
 * @param minimum cast=(SInt32)
 */
public static final native void SetControl32BitMinimum(int cHandle, int minimum);
/**
 * @param cHandle cast=(ControlRef)
 * @param value cast=(SInt32)
 */
public static final native void SetControl32BitValue(int cHandle, int value);
/**
 * @param cHandle cast=(ControlRef)
 * @param actionProc cast=(ControlActionUPP)
 */
public static final native void SetControlAction(int cHandle, int actionProc);
/**
 * @param cHandle cast=(ControlRef)
 * @param bounds cast=(const Rect *)
 */
public static final native void SetControlBounds(int cHandle, Rect bounds);
/**
 * @param inControl cast=(ControlRef)
 * @param inProc cast=(ControlColorUPP)
 */
public static final native int SetControlColorProc(int inControl, int inProc);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inSize cast=(Size)
 * @param inData cast=(const void *)
 */
public static final native int SetControlData(int inControl, int inPart, int inTagName, int inSize, ControlButtonContentInfo inData);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inSize cast=(Size)
 * @param inData cast=(const void *)
 */
public static final native int SetControlData(int inControl, int inPart, int inTagName, int inSize, LongDateRec inData);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inSize cast=(Size)
 * @param inData cast=(const void *)
 */
public static final native int SetControlData(int inControl, int inPart, int inTagName, int inSize, ControlTabInfoRecV1 inData);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inSize cast=(Size)
 * @param inData cast=(const void *)
 */
public static final native int SetControlData(int inControl, int inPart, int inTagName, int inSize, Rect inData);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inSize cast=(Size)
 * @param inData cast=(const void *)
 */
public static final native int SetControlData(int inControl, int inPart, int inTagName, int inSize, short[] inData);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inSize cast=(Size)
 * @param inData cast=(const void *)
 */
public static final native int SetControlData(int inControl, int inPart, int inTagName, int inSize, int[] inData);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inSize cast=(Size)
 * @param inData cast=(const void *)
 */
public static final native int SetControlData(int inControl, int inPart, int inTagName, int inSize, int inData);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inSize cast=(Size)
 * @param inData cast=(const void *)
 */
public static final native int SetControlData(int inControl, int inPart, int inTagName, int inSize, byte[] inData);
/**
 * @param inControl cast=(ControlRef)
 * @param inPart cast=(ControlPartCode)
 * @param inTagName cast=(ResType)
 * @param inSize cast=(Size)
 * @param inData cast=(const void *)
 */
public static final native int SetControlData(int inControl, int inPart, int inTagName, int inSize, ControlEditTextSelectionRec inData);
/**
 * @param inControl cast=(ControlRef)
 * @param inStyle cast=(const ControlFontStyleRec *)
 */
public static final native int SetControlFontStyle(int inControl, ControlFontStyleRec inStyle);
/**
 * @param cHandle cast=(ControlRef)
 * @param popupMenuHandle cast=(MenuRef)
 */
public static final native void SetControlPopupMenuHandle(int cHandle, int popupMenuHandle);
/**
 * @param control cast=(ControlRef)
 * @param propertyData cast=(const void *)
 */
public static final native int SetControlProperty(int control, int propertyCreator, int propertyTag, int propertySize, int[] propertyData);
/**
 * @param cHandle cast=(ControlRef)
 * @param data cast=(SInt32)
 */
public static final native void SetControlReference(int cHandle, int data);
/**
 * @param cHandle cast=(ControlRef)
 * @param sHandle cast=(CFStringRef)
 */
public static final native int SetControlTitleWithCFString(int cHandle, int sHandle);
/**
 * @param cHandle cast=(ControlRef)
 * @param viewSize cast=(SInt32)
 */
public static final native void SetControlViewSize(int cHandle, int viewSize);
/** @param inControl cast=(ControlRef) */
public static final native int SetControlVisibility(int inControl, boolean inIsVisible, boolean inDoDraw);
/** @param cursor cast=(const Cursor *) */
public static final native void SetCursor(int cursor);
/**
 * @param browser cast=(ControlRef)
 * @param callbacks cast=(const DataBrowserCallbacks *),flags=init
 */
public static final native int SetDataBrowserCallbacks(int browser, DataBrowserCallbacks  callbacks);
/** @param browser cast=(ControlRef) */
public static final native int SetDataBrowserCustomCallbacks(int browser, DataBrowserCustomCallbacks  callbacks);
/**
 * @param cHandle cast=(ControlRef)
 * @param hScroll cast=(Boolean)
 * @param vScroll cast=(Boolean)
 */
public static final native int SetDataBrowserHasScrollBars(int cHandle, boolean hScroll, boolean vScroll);
/**
 * @param itemRef cast=(DataBrowserItemDataRef)
 * @param data cast=(Boolean)
 */
public static final native int SetDataBrowserItemDataBooleanValue(int itemRef, boolean data);
/**
 * @param itemRef cast=(DataBrowserItemDataRef)
 * @param themeButtonValue cast=(ThemeButtonValue)
 */
public static final native int SetDataBrowserItemDataButtonValue(int itemRef, short themeButtonValue);
/**
 * @param itemRef cast=(DataBrowserItemDataRef)
 * @param iconRef cast=(IconRef)
 */
public static final native int SetDataBrowserItemDataIcon(int itemRef, int iconRef);
/**
 * @param itemRef cast=(DataBrowserItemDataRef)
 * @param itemID cast=(DataBrowserItemID)
 */
public static final native int SetDataBrowserItemDataItemID(int itemRef, int itemID);
/**
 * @param itemRef cast=(DataBrowserItemDataRef)
 * @param sHandle cast=(CFStringRef)
 */
public static final native int SetDataBrowserItemDataText(int itemRef, int sHandle);
/**
 * @param cHandle cast=(ControlRef)
 * @param colID cast=(DataBrowserTableViewColumnID)
 * @param b cast=(Boolean)
 */
public static final native int SetDataBrowserListViewDisclosureColumn(int cHandle, int colID, boolean b);
/**
 * @param cHandle cast=(ControlRef)
 * @param height cast=(UInt16)
 */
public static final native int SetDataBrowserListViewHeaderBtnHeight(int cHandle, short height);
/** @param browser cast=(ControlRef) */
public static final native int SetDataBrowserListViewHeaderDesc(int browser, int column, DataBrowserListViewHeaderDesc desc);
/**
 * @param browser cast=(ControlRef)
 * @param property cast=(DataBrowserPropertyID)
 * @param flags cast=(DataBrowserPropertyFlags)
 */
public static final native int SetDataBrowserPropertyFlags(int browser, int property, int flags);
/**
 * @param cHandle cast=(ControlRef)
 * @param top cast=(UInt32)
 * @param left cast=(UInt32)
 */
public static final native int SetDataBrowserScrollPosition(int cHandle, int top, int left);
/**
 * @param cHandle cast=(ControlRef)
 * @param numItems cast=(UInt32)
 * @param items cast=(const DataBrowserItemID *)
 * @param operation cast=(DataBrowserSetOption)
 */
public static final native int SetDataBrowserSelectedItems(int cHandle, int numItems, int[] items, int operation);
/**
 * @param cHandle cast=(ControlRef)
 * @param selectionFlags cast=(DataBrowserSelectionFlags)
 */
public static final native int SetDataBrowserSelectionFlags(int cHandle, int selectionFlags);
/** @param browser cast=(ControlRef) */
public static final native int SetDataBrowserSortOrder(int browser, short order);
/**
 * @param browser cast=(ControlRef)
 * @param property cast=(DataBrowserPropertyID)
 */
public static final native int SetDataBrowserSortProperty(int browser, int property);
/** @param browser cast=(ControlRef) */
public static final native int SetDataBrowserTableViewColumnPosition(int browser, int column, int position);
/** @param browser cast=(ControlRef) */
public static final native int SetDataBrowserTableViewHiliteStyle(int browser, int hiliteStyle);  
/** @param browser cast=(ControlRef) */
public static final native int SetDataBrowserTableViewItemRow(int browser, int item, int row);
/**
 * @param browser cast=(ControlRef)
 * @param column cast=(DataBrowserTableViewColumnID)
 * @param width cast=(UInt16)
 */
public static final native int SetDataBrowserTableViewNamedColumnWidth(int browser, int column, short width);
/** @param browser cast=(ControlRef) */
public static final native int SetDataBrowserTableViewRowHeight(int browser, short height	);
/**
 * @param cHandle cast=(ControlRef)
 * @param rootID cast=(DataBrowserItemID)
 */
public static final native int SetDataBrowserTarget(int cHandle, int rootID);
/**
 * @param theDrag cast=(DragRef)
 * @param inActions cast=(DragActions)
 * @param isLocal cast=(Boolean)
 */
public static final native int SetDragAllowableActions(int theDrag, int inActions, boolean isLocal);
/**
 * @param theDrag cast=(DragRef)
 * @param inAction cast=(DragActions)
 */
public static final native int SetDragDropAction(int theDrag, int inAction);
/**
 * @param inDrag cast=(DragRef)
 * @param inCGImage cast=(CGImageRef)
 * @param inImageOffsetPt cast=(HIPoint *)
 * @param inImageFlags cast=(DragImageFlags)
 */
public static final native int SetDragImageWithCGImage(int inDrag, int inCGImage, CGPoint inImageOffsetPt, int inImageFlags);
/**
 * @param theDrag cast=(DragRef)
 * @param inputProc cast=(DragInputUPP)
 * @param dragInputRefCon cast=(void *)
 */
public static final native int SetDragInputProc(int theDrag, int inputProc, int dragInputRefCon);
/**
 * @param theDrag cast=(DragRef)
 * @param theItemRef cast=(DragItemRef)
 * @param theType cast=(FlavorType)
 * @param dataPtr cast=(const void *)
 * @param dataSize cast=(Size)
 * @param dataOffset cast=(UInt32)
 */
public static final native int SetDragItemFlavorData (int theDrag, int theItemRef, int theType, byte[] dataPtr, int dataSize, int dataOffset);
/**
 * @param theDrag cast=(DragRef)
 * @param sendProc cast=(DragSendDataUPP)
 * @param dragSendRefCon cast=(void *)
 */
public static final native int SetDragSendProc(int theDrag, int sendProc, int dragSendRefCon);
/**
 * @param inTimer cast=(EventLoopTimerRef)
 * @param inNextFire cast=(EventTimerInterval)
 */
public static final native int SetEventLoopTimerNextFireTime(int inTimer, double inNextFire);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inType cast=(EventParamType)
 * @param inSize cast=(UInt32)
 * @param inDataPtr cast=(const void *)
 */
public static final native int SetEventParameter(int inEvent, int inName, int inType, int inSize, Point inDataPtr);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inType cast=(EventParamType)
 * @param inSize cast=(UInt32)
 * @param inDataPtr cast=(const void *)
 */
public static final native int SetEventParameter(int inEvent, int inName, int inType, int inSize, HICommand inDataPtr);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inType cast=(EventParamType)
 * @param inSize cast=(UInt32)
 * @param inDataPtr cast=(const void *)
 */
public static final native int SetEventParameter(int inEvent, int inName, int inType, int inSize, char[] inDataPtr);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inType cast=(EventParamType)
 * @param inSize cast=(UInt32)
 * @param inDataPtr cast=(const void *)
 */
public static final native int SetEventParameter(int inEvent, int inName, int inType, int inSize, short[] inDataPtr);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inType cast=(EventParamType)
 * @param inSize cast=(UInt32)
 * @param inDataPtr cast=(const void *)
 */
public static final native int SetEventParameter(int inEvent, int inName, int inType, int inSize, int[] inDataPtr);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inType cast=(EventParamType)
 * @param inSize cast=(UInt32)
 * @param inDataPtr cast=(const void *)
 */
public static final native int SetEventParameter(int inEvent, int inName, int inType, int inSize, boolean[] inDataPtr);
/**
 * @param inEvent cast=(EventRef)
 * @param inName cast=(EventParamName)
 * @param inType cast=(EventParamType)
 * @param inSize cast=(UInt32)
 * @param inDataPtr cast=(const void *)
 */
public static final native int SetEventParameter(int inEvent, int inName, int inType, int inSize, CGPoint inDataPtr);
/**
 * @param iStyleType cast=(OSType)
 * @param iNumStyles cast=(UInt32)
 * @param iStyles cast=(void *)
 * @param iFPEventTarget cast=(void *)
 */
public static final native int SetFontInfoForSelection(int iStyleType, int iNumStyles, int iStyles, int iFPEventTarget);
/** @param psn cast=(const ProcessSerialNumber *) */
public static final native int SetFrontProcess(int[] psn);
/** @param psn cast=(const ProcessSerialNumber *) */
public static final native int SetFrontProcessWithOptions(int[] psn, int inOptions);
/** @param handle cast=(Handle) */
public static final native void SetHandleSize(int handle, int size);
/**
 * @param portHandle cast=(CGrafPtr)
 * @param gdHandle cast=(GDHandle)
 */
public static final native void SetGWorld(int portHandle, int gdHandle);
/**
 * @param iconFamily cast=(IconFamilyHandle)
 * @param iconType cast=(OSType)
 * @param h cast=(Handle)
 */
public static final native int SetIconFamilyData(int iconFamily, int iconType, int h);
/** @param theMenu cast=(MenuRef) */
public static final native void SetItemMark(int theMenu, short item, short markChar);
/**
 * @param wHandle cast=(WindowRef)
 * @param cHandle cast=(ControlRef)
 * @param inPart cast=(ControlFocusPart)
 */
public static final native int SetKeyboardFocus(int wHandle, int cHandle, short inPart);
/**
 * @param mHandle cast=(MenuRef)
 * @param commandId cast=(MenuCommand)
 * @param mark cast=(UniChar)
 */
public static final native int SetMenuCommandMark(int mHandle, int commandId, char mark);
/**
 * @param mHandle cast=(MenuRef)
 * @param fontID cast=(SInt16)
 * @param size cast=(UInt16)
 */
public static final native int SetMenuFont(int mHandle, short fontID, short size);
/**
 * @param mHandle cast=(MenuRef)
 * @param index cast=(MenuItemIndex)
 * @param virtualKey cast=(Boolean)
 * @param key cast=(UInt16)
 */
public static final native int SetMenuItemCommandKey(int mHandle, short index, boolean virtualKey, char key);
/**
 * @param mHandle cast=(MenuRef)
 * @param index cast=(MenuItemIndex)
 * @param hierMenuHandle cast=(MenuRef)
 */
public static final native int SetMenuItemHierarchicalMenu(int mHandle, short index, int hierMenuHandle);
/**
 * @param mHandle cast=(MenuRef)
 * @param item cast=(SInt16)
 * @param iconType cast=(UInt8)
 * @param iconHandle cast=(Handle)
 */
public static final native int SetMenuItemIconHandle(int mHandle, short item, byte iconType, int iconHandle);
/**
 * @param mHandle cast=(MenuRef)
 * @param index cast=(SInt16)
 * @param glyph cast=(SInt16)
 */
public static final native int SetMenuItemKeyGlyph(int mHandle, short index, short glyph);
/**
 * @param mHandle cast=(MenuRef)
 * @param index cast=(SInt16)
 * @param modifiers cast=(UInt8)
 */
public static final native int SetMenuItemModifiers(int mHandle, short index, byte modifiers);
/**
 * @param mHandle cast=(MenuRef)
 * @param index cast=(SInt16)
 * @param refCon cast=(UInt32)
 */
public static final native int SetMenuItemRefCon(int mHandle, short index, int refCon);
/**
 * @param mHandle cast=(MenuRef)
 * @param index cast=(MenuItemIndex)
 * @param sHandle cast=(CFStringRef)
 */
public static final native int SetMenuItemTextWithCFString(int mHandle, short index, int sHandle);
/**
 * @param mHandle cast=(MenuRef)
 * @param sHandle cast=(CFStringRef)
 */
public static final native int SetMenuTitleWithCFString(int mHandle, int sHandle);
/** @param pHandle cast=(GrafPtr) */
public static final native void SetPort(int pHandle);
/**
 * @param p cast=(Point *)
 * @param h cast=(short)
 * @param v cast=(short)
 */
public static final native void SetPt(Point p, short h, short v);
/**
 * @param r cast=(Rect *)
 * @param left cast=(short)
 * @param top cast=(short)
 * @param right cast=(short)
 * @param bottom cast=(short)
 */
public static final native void SetRect(Rect r, short left, short top, short right, short bottom);
/**
 * @param rgnHandle cast=(RgnHandle)
 * @param left cast=(short)
 * @param top cast=(short)
 * @param right cast=(short)
 * @param bottom cast=(short)
 */
public static final native void SetRectRgn(int rgnHandle, short left, short top, short right, short bottom);
/** @param mHandle cast=(MenuRef) */
public static final native int SetRootMenu(int mHandle);
/**
 * @param inMode cast=(SystemUIMode)
 * @param inOptions cast=(SystemUIOptions)
 */
public static final native int SetSystemUIMode(int inMode, int inOptions);
/**
 * @param inBrush cast=(ThemeBrush)
 * @param depth cast=(SInt16)
 * @param isColorDevice cast=(Boolean)
 */
public static final native int SetThemeBackground(short inBrush, short depth, boolean isColorDevice);
/** @param themeCursor cast=(ThemeCursor) */
public static final native int SetThemeCursor(int themeCursor);
/**
 * @param state cast=(ThemeDrawingState)
 * @param disposeNow cast=(Boolean)
 */
public static final native int SetThemeDrawingState(int state, boolean disposeNow);
public static final native int SetThemeTextColor(short inBrush, short depth, boolean isColorDevice);
/**
 * @param wHandle cast=(WindowRef)
 * @param brush cast=(ThemeBrush)
 * @param update cast=(Boolean)
 */
public static final native int SetThemeWindowBackground(int wHandle, short brush, boolean update);
/**
 * @param cHandle cast=(ControlRef)
 * @param depth cast=(SInt16)
 * @param isColorDevice cast=(Boolean)
 */
public static final native int SetUpControlBackground(int cHandle, short depth, boolean isColorDevice);
/**
 * @param wHandle cast=(WindowRef)
 * @param scope cast=(WindowActivationScope)
 */
public static final native int SetWindowActivationScope(int wHandle, int scope);
/** @param inWindow cast=(WindowRef) */
public static final native int SetWindowAlpha(int inWindow, float inAlpha);
/**
 * @param window cast=(WindowRef)
 * @param regionCode cast=(WindowRegionCode)
 * @param globalBounds cast=(Rect *)
 */
public static final native void SetWindowBounds(int window, int regionCode, Rect globalBounds);
/**
 * @param wHandle cast=(WindowRef)
 * @param cHandle cast=(ControlRef)
 */
public static final native int SetWindowDefaultButton(int wHandle, int cHandle);
/**
 * @param inWindow cast=(WindowRef)
 * @param inNewGroup cast=(WindowGroupRef)
 */
public static final native int SetWindowGroup(int inWindow, int inNewGroup);
/**
 * @param inGroup cast=(WindowGroupRef)
 * @param inWindow cast=(WindowRef)
 */
public static final native int SetWindowGroupOwner(int inGroup, int inWindow);
/**
 * @param inGroup cast=(WindowGroupRef)
 * @param inNewGroup cast=(WindowGroupRef)
 */
public static final native int SetWindowGroupParent(int inGroup, int inNewGroup);
/**
 * @param inWindow cast=(WindowRef)
 * @param inModalKind cast=(WindowModality)
 * @param inUnavailableWindow cast=(WindowRef)
 */
public static final native int SetWindowModality(int inWindow, int inModalKind, int inUnavailableWindow);
/**
 * @param inWindow cast=(WindowRef)
 */
public static final native int SetWindowModified(int inWindow, boolean modified);
/**
 * @param inWindow cast=(WindowRef)
 * @param inMinLimits cast=(HISize *)
 * @param inMaxLimits cast=(HISize *)
 */
public static final native int SetWindowResizeLimits (int inWindow, CGPoint inMinLimits, CGPoint inMaxLimits);
/**
 * @param wHandle cast=(WindowRef)
 * @param sHandle cast=(CFStringRef)
 */
public static final native int SetWindowTitleWithCFString(int wHandle, int sHandle);
/** @param wHandle cast=(WindowRef) */
public static final native void ShowWindow(int wHandle);
/**
 * @param cHandle cast=(ControlRef)
 * @param w cast=(SInt16)
 * @param h cast=(SInt16)
 */
public static final native void SizeControl(int cHandle, short w, short h);
/**
 * @param wHandle cast=(WindowRef)
 * @param w cast=(short)
 * @param h cast=(short)
 * @param update cast=(Boolean)
 */
public static final native void SizeWindow(int wHandle, short w, short h, boolean update);
public static final native boolean StillDown();
/** @param duration cast=(short) */
public static final native void SysBeep(short duration);
/** @param txHandle cast=(TXNObject) */
public static final native int TXNCopy(int txHandle);
/** @param txHandle cast=(TXNObject) */
public static final native int TXNCut(int txHandle);
/** @param txHandle cast=(TXNObject) */
public static final native int TXNDataSize(int txHandle);
/** @param txHandle cast=(TXNObject) */
public static final native void TXNDeleteObject(int txHandle);
/**
 * @param txHandle cast=(TXNObject)
 * @param echoCharacter cast=(UniChar)
 * @param encoding cast=(TextEncoding)
 * @param on cast=(Boolean)
 */
public static final native int TXNEchoMode(int txHandle, char echoCharacter, int encoding, boolean on);
/**
 * @param txHandle cast=(TXNObject)
 * @param startOffset cast=(TXNOffset)
 * @param endOffset cast=(TXNOffset)
 * @param dataHandle cast=(Handle *)
 */
public static final native int TXNGetData(int txHandle, int startOffset, int endOffset, int[] dataHandle);
/**
 * @param txHandle cast=(TXNObject)
 * @param lineTotal cast=(ItemCount *)
 */
public static final native int TXNGetLineCount(int txHandle, int[] lineTotal);
/**
 * @param iTXNObject cast=(TXNObject)
 * @param iLineNumber cast=(UInt32)
 * @param oLineWidth cast=(Fixed *)
 * @param oLineHeight cast=(Fixed *)
 */
public static final native int TXNGetLineMetrics(int iTXNObject, int iLineNumber, int [] oLineWidth, int [] oLineHeight);
/**
 * @param iTXNObject cast=(TXNObject)
 * @param iControlCount cast=(ItemCount)
 * @param iControlTags cast=(const TXNControlTag *)
 * @param oControlData cast=(TXNControlData *)
 */
public static final native int TXNGetTXNObjectControls(int iTXNObject, int iControlCount, int [] iControlTags, int [] oControlData);
/** @param iTXNObject cast=(TXNObject) */
public static final native int TXNGetHIRect(int iTXNObject, int iTXNRectKey, CGRect oRectangle);
/**
 * @param txHandle cast=(TXNObject)
 * @param startOffset cast=(TXNOffset *)
 * @param endOffset cast=(TXNOffset *)
 */
public static final native void TXNGetSelection(int txHandle, int[] startOffset, int[] endOffset);
/** @param iTXNObject cast=(TXNObject) */
public static final native void TXNGetViewRect (int iTXNObject, Rect oViewRect);
/**
 * @param iDefaultFonts cast=(const TXNMacOSPreferredFontDescription *)
 * @param iCountDefaultFonts cast=(ItemCount)
 * @param iUsageFlags cast=(TXNInitOptions)
 */
public static final native int TXNInitTextension(int iDefaultFonts, int iCountDefaultFonts, int iUsageFlags);
/**
 * @param txHandle cast=(TXNObject)
 * @param offset cast=(TXNOffset)
 * @param point cast=(HIPoint *)
 */
public static final native int TXNOffsetToHIPoint(int txHandle, int offset, CGPoint point);
/** @param txHandle cast=(TXNObject) */
public static final native int TXNPaste(int txHandle);
/**
 * @param iTXNObject cast=(TXNObject)
 * @param iPoint cast=(HIPoint *)
 * @param oOffset cast=(TXNOffset *)
 */
public static final native int TXNHIPointToOffset (int iTXNObject, CGPoint iPoint, int [] oOffset);
/** @param txHandle cast=(TXNObject) */
public static final native void TXNSelectAll(int txHandle);
/**
 * @param iTXNObject cast=(TXNObject)
 * @param iBackgroundInfo cast=(const TXNBackground *)
 */
public static final native int TXNSetBackground(int iTXNObject, TXNBackground iBackgroundInfo);
/**
 * @param iTXNObject cast=(TXNObject)
 * @param iDataType cast=(TXNDataType)
 * @param iDataPtr cast=(const void *)
 * @param iDataSize cast=(ByteCount)
 * @param iStartOffset cast=(TXNOffset)
 * @param iEndOffset cast=(TXNOffset)
 */
public static final native int TXNSetData(int iTXNObject, int iDataType, char[] iDataPtr, int iDataSize, int iStartOffset, int iEndOffset);
/**
 * @param txHandle cast=(TXNObject)
 * @param top cast=(SInt32)
 * @param left cast=(SInt32)
 * @param bottom cast=(SInt32)
 * @param right cast=(SInt32)
 * @param frameID cast=(TXNFrameID)
 */
public static final native void TXNSetFrameBounds(int txHandle, int top, int left, int bottom, int right, int frameID);
/**
 * @param txHandle cast=(TXNObject)
 * @param startOffset cast=(TXNOffset)
 * @param endOffset cast=(TXNOffset)
 */
public static final native int TXNSetSelection(int txHandle, int startOffset, int endOffset);
/**
 * @param iTXNObject cast=(TXNObject)
 * @param iAttrCount cast=(ItemCount)
 * @param iAttributes cast=(const TXNTypeAttributes *)
 * @param iStartOffset cast=(TXNOffset)
 * @param iEndOffset cast=(TXNOffset)
 */
public static final native int TXNSetTypeAttributes(int iTXNObject, int iAttrCount, int iAttributes, int iStartOffset, int iEndOffset);
/**
 * @param iTXNObject cast=(TXNObject)
 * @param iClearAll cast=(Boolean)
 * @param iControlCount cast=(ItemCount)
 * @param iControlTags cast=(const TXNControlTag *)
 * @param iControlData cast=(const TXNControlData *)
 */
public static final native int TXNSetTXNObjectControls(int iTXNObject, boolean iClearAll, int iControlCount, int[] iControlTags, int[] iControlData);
/**
 * @param txHandle cast=(TXNObject)
 * @param showEnd cast=(Boolean)
 */
public static final native void TXNShowSelection(int txHandle, boolean showEnd);
/** @param face cast=(StyleParameter) */
public static final native void TextFace(short face);
/** @param fontID cast=(short) */
public static final native void TextFont(short fontID);
/** @param size cast=(short) */
public static final native void TextSize(short size);
/**
 * @param theDrag cast=(DragRef)
 * @param theEvent cast=(const EventRecord *)
 * @param theRegion cast=(RgnHandle)
 */
public static final native int TrackDrag(int theDrag, EventRecord theEvent, int theRegion);
/**
 * @param inPort cast=(GrafPtr)
 * @param inOptions cast=(OptionBits)
 * @param inTime cast=(EventTimeout)
 * @param outPt cast=(Point *)
 * @param outModifiers cast=(UInt32 *)
 * @param outResult cast=(MouseTrackingResult *)
 */
public static final native int TrackMouseLocationWithOptions(int inPort, int inOptions, double inTime, Point outPt, int [] outModifiers, short[] outResult);
/**
 * @param inUTI cast=(CFStringRef)
 * @param inConformsToUTI cast=(CFStringRef)
 */
public static final native boolean UTTypeConformsTo(int inUTI, int inConformsToUTI);
/**
 * @param inTagClass cast=(CFStringRef)
 * @param inTag cast=(CFStringRef)
 * @param inConformingToUTI cast=(CFStringRef)
 */
public static final native int UTTypeCreatePreferredIdentifierForTag(int inTagClass, int inTag, int inConformingToUTI);
/**
 * @param inTagClass cast=(CFStringRef)
 * @param inTag cast=(CFStringRef)
 * @param inConformingToUTI cast=(CFStringRef)
 */
public static final native int UTTypeCreateAllIdentifiersForTag(int inTagClass, int inTag, int inConformingToUTI);
/**
 * @param inUTI1 cast=(CFStringRef)
 * @param inUTI2 cast=(CFStringRef)
 */
public static final native boolean UTTypeEqual(int inUTI1, int inUTI2);
/**
 * @param srcA flags=no_out
 * @param srcB flags=no_out
 * @param dst flags=no_in
 */
public static final native void UnionRect(Rect srcA, Rect srcB, Rect dst);
/**
 * @param srcRgnA cast=(RgnHandle)
 * @param srcRgnB cast=(RgnHandle)
 * @param dstRgn cast=(RgnHandle)
 */
public static final native void UnionRgn(int srcRgnA, int srcRgnB, int dstRgn);
/**
 * @param cHandle cast=(ControlRef)
 * @param container cast=(DataBrowserItemID)
 * @param numItems cast=(UInt32)
 * @param items cast=(const DataBrowserItemID *)
 * @param preSortProperty cast=(DataBrowserPropertyID)
 * @param propertyID cast=(DataBrowserPropertyID)
 */
public static final native int UpdateDataBrowserItems(int cHandle, int container, int numItems, int[] items, int preSortProperty, int propertyID);
/**
 * @param iTextScriptID cast=(ScriptCode)
 * @param iTextLanguageID cast=(LangCode)
 * @param iRegionID cast=(RegionCode)
 * @param iTextFontname cast=(ConstStr255Param)
 * @param oEncoding cast=(TextEncoding *)
 */
public static final native int UpgradeScriptInfoToTextEncoding(short iTextScriptID, short iTextLanguageID, short iRegionID, byte[] iTextFontname, int[] oEncoding); 
/** @param idocID cast=(TSMDocumentID) */
public static final native int UseInputWindow(int idocID, boolean useWindow);
/** @param initialGlobalMouse flags=struct */
public static final native boolean WaitMouseMoved(Point initialGlobalMouse);
public static final native int X2Fix(double x);
/**
 * @param inWindow cast=(WindowRef)
 * @param inPartCode cast=(WindowPartCode)
 * @param ioIdealSize cast=(Point *)
 */
public static final native int ZoomWindowIdeal(int inWindow, short inPartCode, Point ioIdealSize);
/** @method flags=const */
public static final native int kCFNumberFormatterDecimalSeparator();
public static final native int getpid();
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *)
 * @param n cast=(size_t)
 */
public static final native void memmove(NavCBRec dest, int src, int n);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *)
 * @param n cast=(size_t)
 */
public static final native void memmove(NavFileOrFolderInfo dest, int src, int n);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *)
 * @param n cast=(size_t)
 */
public static final native void memmove(NavMenuItemSpec dest, int src, int n);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param n cast=(size_t)
 */
public static final native void memmove(ATSTrapezoid dest, int src, int n);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *)
 * @param n cast=(size_t)
 */
public static final native void memmove(RGBColor dest, int src, int n);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param n cast=(size_t)
 */
public static final native void memmove(CGPathElement dest, int src, int n);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param n cast=(size_t)
 */
public static final native void memmove(TextRange dest, int src, int n);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param n cast=(size_t)
 */
public static final native void memmove(int dest, PixMap src, int n);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param n cast=(size_t)
 */
public static final native void memmove(int dest, Cursor src, int n);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param n cast=(size_t)
 */
public static final native void memmove(GDevice dest, int src, int n);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param n cast=(size_t)
 */
public static final native void memmove(PixMap dest, int src, int n);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param n cast=(size_t)
 */
public static final native void memmove(HMHelpContentRec dest, int src, int n);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param n cast=(size_t)
 */
public static final native void memmove(ATSLayoutRecord dest, int src, int n);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *),flags=critical no_out
 * @param n cast=(size_t)
 */
public static final native void memmove(org.eclipse.swt.internal.carbon.Point dest, int[] src, int n);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param n cast=(size_t)
 */
public static final native void memmove(int dest, HMHelpContentRec src, int n);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param n cast=(size_t)
 */
public static final native void memmove(int dest, BitMap src, int n);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param n cast=(size_t)
 */
public static final native void memmove(int dest, RGBColor src, int n);
/**
 * @param dest cast=(void *),flags=no_in
 * @param src cast=(const void *)
 * @param n cast=(size_t)
 */
public static final native void memmove(Rect dest, int src, int n);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param n cast=(size_t)
 */
public static final native void memmove(int dest, Rect src, int n);
/**
 * @param dest cast=(void *),flags=critical no_in
 * @param src cast=(const void *),flags=critical no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(char[] dest, byte[] src, int size);
/**
 * @param dest cast=(void *)
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(int dest, ATSUTab src, int size);
/**
 * @param dest cast=(void *),flags=critical no_in
 * @param src cast=(const void *),flags=no_out
 * @param size cast=(size_t)
 */
public static final native void memmove(int[] dest, TXNTab src, int size);

/**
 * @param inHIObject cast=(HIObjectRef)
 * @param inIdentifier cast=(UInt64)
 */
public static final native int AXUIElementCreateWithHIObjectAndIdentifier(int inHIObject, long inIdentifier);
/** @method flags=dynamic */
public static final native int AXUIElementCreateWithDataBrowserAndItemInfo(int inDataBrowser, DataBrowserAccessibilityItemInfo inInfo);
/**
 * @param inNotification cast=(CFStringRef)
 * @param inHIObject cast=(HIObjectRef)
 * @param inIdentifier cast=(UInt64)
 */
public static final native void AXNotificationHIObjectNotify(int inNotification, int inHIObject, long inIdentifier);
/**
 * @param inUIElement cast=(AXUIElementRef)
 * @param outIdentifier cast=(UInt64 *)
 */
public static final native void AXUIElementGetIdentifier(int inUIElement, long[] outIdentifier);
/** @param inUIElement cast=(AXUIElementRef) */
public static final native int AXUIElementGetHIObject(int inUIElement);
/** @method flags=dynamic */
public static final native int AXUIElementGetDataBrowserItemInfo(int inElement, int inDataBrowser, int inDesiredInfoVersion, DataBrowserAccessibilityItemInfo outInfo);
/**
 * @param theType cast=(AXValueType)
 * @param range cast=(CFRange *)
 */
public static final native int AXValueCreate (int theType, CFRange range);
/**
 * @param value cast=(AXValueRef)
 * @param theType cast=(AXValueType)
 * @param range cast=(CFRange *)
 */
public static final native boolean AXValueGetValue (int value, int theType, CFRange range);
/** @param inObject cast=(HIObjectRef) */
public static final native int HIObjectSetAccessibilityIgnored(int inObject, boolean inIgnored);
/**
 * @param inHIObject cast=(HIObjectRef)
 * @param inIdentifier cast=(UInt64)
 * @param inAttributeName cast=(CFStringRef)
 * @param inAttributeData cast=(CFTypeRef)
 */
public static final native int HIObjectSetAuxiliaryAccessibilityAttribute(int inHIObject, long inIdentifier, int inAttributeName, int inAttributeData);
/**
 * @param element cast=(AXUIElementRef)
 * @param attribute cast=(CFStringRef)
 * @param value cast=(CFTypeRef *)
 */
public static final native int AXUIElementCopyAttributeValue (int element, int attribute, int [] value);


/**
 * @param ctx cast=(JSContextRef)
 * @param script cast=(JSStringRef)
 * @param thisObject cast=(JSObjectRef)
 * @param sourceURL cast=(JSStringRef)
 * @param exception cast=(JSValueRef *)
 */
public static final native int JSEvaluateScript (int ctx, int script, int thisObject, int sourceURL, int startingLineNumber, int[] exception);

/**
 * @param string cast=(const char *)
 */
public static final native int JSStringCreateWithUTF8CString (byte[] string);

/**
 * @param string cast=(JSStringRef)
 */
public static final native void JSStringRelease (int string);


public static final int kEventClassAccessibility = ('a'<<24) + ('c'<<16) + ('c'<<8) + 'e';

public static final int kEventAccessibleGetChildAtPoint = 1;
public static final int kEventAccessibleGetFocusedChild = 2;
public static final int kEventAccessibleGetAllAttributeNames = 21;
public static final int kEventAccessibleGetAllParameterizedAttributeNames = 25;
public static final int kEventAccessibleGetNamedAttribute = 22;
public static final int kEventAccessibleSetNamedAttribute = 23;
public static final int kEventAccessibleIsNamedAttributeSettable = 24;
public static final int kEventAccessibleGetAllActionNames = 41;
public static final int kEventAccessiblePerformNamedAction = 42;
public static final int kEventAccessibleGetNamedActionDescription = 44;

public static final int kEventParamAccessibleObject = ('a'<<24) + ('o'<<16) + ('b'<<8) + 'j';
public static final int kEventParamAccessibleChild = ('a'<<24) + ('c'<<16) + ('h'<<8) + 'l';
public static final int kEventParamAccessibleAttributeName = ('a'<<24) + ('t'<<16) + ('n'<<8) + 'm';
public static final int kEventParamAccessibleAttributeNames = ('a'<<24) + ('t'<<16) + ('n'<<8) + 's';
public static final int kEventParamAccessibleAttributeValue = ('a'<<24) + ('t'<<16) + ('v'<<8) + 'l';
public static final int kEventParamAccessibleAttributeSettable = ('a'<<24) + ('t'<<16) + ('s'<<8) + 't';
public static final int kEventParamAccessibleAttributeParameter = ('a'<<24) + ('t'<<16) + ('p'<<8) + 'a';
public static final int kEventParamAccessibleActionName = ('a'<<24) + ('c'<<16) + ('n'<<8) + 'm';
public static final int kEventParamAccessibleActionNames = ('a'<<24) + ('c'<<16) + ('n'<<8) + 's';
public static final int kEventParamAccessibleActionDescription = ('a'<<24) + ('c'<<16) + ('d'<<8) + 's';
public static final int kEventParamAccessibleEventQueued = ('a'<<24) + ('e'<<16) + ('q'<<8) + 'u';

public static final String kAXApplicationRole  = "AXApplication";
public static final String kAXSystemWideRole   = "AXSystemWide";
public static final String kAXWindowRole       = "AXWindow";
public static final String kAXSheetRole        = "AXSheet";
public static final String kAXDrawerRole       = "AXDrawer";
public static final String kAXGrowAreaRole     = "AXGrowArea";
public static final String kAXImageRole        = "AXImage";
public static final String kAXUnknownRole      = "AXUnknown";
public static final String kAXButtonRole       = "AXButton";
public static final String kAXRadioButtonRole  = "AXRadioButton";
public static final String kAXCheckBoxRole     = "AXCheckBox";
public static final String kAXPopUpButtonRole  = "AXPopUpButton";
public static final String kAXMenuButtonRole   = "AXMenuButton";
public static final String kAXTabGroupRole     = "AXTabGroup";
public static final String kAXTableRole        = "AXTable";
public static final String kAXColumnRole       = "AXColumn";
public static final String kAXRowRole          = "AXRow";
public static final String kAXOutlineRole      = "AXOutline";
public static final String kAXBrowserRole      = "AXBrowser";
public static final String kAXScrollAreaRole   = "AXScrollArea";
public static final String kAXScrollBarRole    = "AXScrollBar";
public static final String kAXRadioGroupRole   = "AXRadioGroup";
public static final String kAXListRole         = "AXList";
public static final String kAXGroupRole        = "AXGroup";
public static final String kAXValueIndicatorRole = "AXValueIndicator";
public static final String kAXComboBoxRole     = "AXComboBox";
public static final String kAXSliderRole       = "AXSlider";
public static final String kAXIncrementorRole  = "AXIncrementor";
public static final String kAXBusyIndicatorRole = "AXBusyIndicator";
public static final String kAXProgressIndicatorRole = "AXProgressIndicator";
public static final String kAXRelevanceIndicatorRole = "AXRelevanceIndicator";
public static final String kAXToolbarRole      = "AXToolbar";
public static final String kAXDisclosureTriangleRole = "AXDisclosureTriangle";
public static final String kAXTextFieldRole = "AXTextField";
public static final String kAXTextAreaRole     = "AXTextArea";
public static final String kAXStaticTextRole   = "AXStaticText";
public static final String kAXMenuBarRole      = "AXMenuBar";
public static final String kAXMenuBarItemRole  = "AXMenuBarItem";
public static final String kAXMenuRole         = "AXMenu";
public static final String kAXMenuItemRole     = "AXMenuItem";
public static final String kAXSplitGroupRole   = "AXSplitGroup";
public static final String kAXSplitterRole     = "AXSplitter";
public static final String kAXColorWellRole    = "AXColorWell";
public static final String kAXTimeFieldRole    = "AXTimeField";
public static final String kAXDateFieldRole    = "AXDateField";
public static final String kAXHelpTagRole      = "AXHelpTag";
public static final String kAXMatteRole        = "AXMatteRole";
public static final String kAXDockItemRole     = "AXDockItem";
public static final String kAXLinkRole         = "AXLink";  // as seen in WebKit

public static final String kAXCloseButtonSubrole       = "AXCloseButton";
public static final String kAXMinimizeButtonSubrole    = "AXMinimizeButton";
public static final String kAXZoomButtonSubrole        = "AXZoomButton";
public static final String kAXToolbarButtonSubrole     = "AXToolbarButton";
public static final String kAXSecureTextFieldSubrole   = "AXSecureTextField";
public static final String kAXTableRowSubrole          = "AXTableRow";
public static final String kAXOutlineRowSubrole        = "AXOutlineRow";
public static final String kAXUnknownSubrole           = "AXUnknown";
public static final String kAXStandardWindowSubrole    = "AXStandardWindow";
public static final String kAXDialogSubrole            = "AXDialog";
public static final String kAXSystemDialogSubrole      = "AXSystemDialog";
public static final String kAXFloatingWindowSubrole    = "AXFloatingWindow";
public static final String kAXSystemFloatingWindowSubrole = "AXSystemFloatingWindow";
public static final String kAXIncrementArrowSubrole    = "AXIncrementArrow";
public static final String kAXDecrementArrowSubrole    = "AXDecrementArrow";
public static final String kAXIncrementPageSubrole     = "AXIncrementPage";
public static final String kAXDecrementPageSubrole     = "AXDecrementPage";
public static final String kAXSortButtonSubrole        = "AXSortButton";
public static final String kAXSearchFieldSubrole       = "AXSearchField";
public static final String kAXApplicationDockItemSubrole = "AXApplicationDockItem";
public static final String kAXDocumentDockItemSubrole  = "AXDocumentDockItem";
public static final String kAXFolderDockItemSubrole    = "AXFolderDockItem";
public static final String kAXMinimizedWindowDockItemSubrole= "AXMinimizedWindowDockItem";
public static final String kAXURLDockItemSubrole       = "AXURLDockItem";
public static final String kAXDockExtraDockItemSubrole = "AXDockExtraDockItem";
public static final String kAXTrashDockItemSubrole     = "AXTrashDockItem";
public static final String kAXProcessSwitcherListSubrole = "AXProcessSwitcherList";

//General attributes
public static final String kAXRoleAttribute                    = "AXRole";
public static final String kAXSubroleAttribute                 = "AXSubrole";
public static final String kAXRoleDescriptionAttribute         = "AXRoleDescription";
public static final String kAXHelpAttribute                    = "AXHelp";
public static final String kAXTitleAttribute                   = "AXTitle";
public static final String kAXValueAttribute                   = "AXValue";
public static final String kAXMinValueAttribute                = "AXMinValue";
public static final String kAXMaxValueAttribute                = "AXMaxValue";
public static final String kAXValueIncrementAttribute          = "AXValueIncrement";
public static final String kAXAllowedValuesAttribute           = "AXAllowedValues";
public static final String kAXEnabledAttribute                 = "AXEnabled";
public static final String kAXFocusedAttribute                 = "AXFocused";
public static final String kAXParentAttribute                  = "AXParent";
public static final String kAXChildrenAttribute                = "AXChildren";
public static final String kAXSelectedChildrenAttribute        = "AXSelectedChildren";
public static final String kAXVisibleChildrenAttribute         = "AXVisibleChildren";
public static final String kAXWindowAttribute                  = "AXWindow";
public static final String kAXTopLevelUIElementAttribute       = "AXTopLevelUIElement";
public static final String kAXPositionAttribute                = "AXPosition";
public static final String kAXSizeAttribute                    = "AXSize";
public static final String kAXOrientationAttribute             = "AXOrientation";
public static final String kAXDescriptionAttribute             = "AXDescription";

//   Text-specific attributes
public static final String kAXSelectedTextAttribute            = "AXSelectedText";
public static final String kAXVisibleCharacterRangeAttribute   = "AXVisibleCharacterRange";
public static final String kAXSelectedTextRangeAttribute       = "AXSelectedTextRange";
public static final String kAXNumberOfCharactersAttribute      = "AXNumberOfCharacters";
public static final String kAXSharedTextUIElementsAttribute    = "AXSharedTextUIElements";
public static final String kAXSharedCharacterRangeAttribute    = "AXSharedCharacterRange";
     
//   Window-specific attributes
public static final String kAXMainAttribute                    = "AXMain";
public static final String kAXMinimizedAttribute               = "AXMinimized";
public static final String kAXCloseButtonAttribute             = "AXCloseButton";
public static final String kAXZoomButtonAttribute              = "AXZoomButton";
public static final String kAXMinimizeButtonAttribute          = "AXMinimizeButton";
public static final String kAXToolbarButtonAttribute           = "AXToolbarButton";
public static final String kAXGrowAreaAttribute                = "AXGrowArea";
public static final String kAXProxyAttribute                   = "AXProxy";
public static final String kAXModalAttribute                   = "AXModal";
public static final String kAXDefaultButtonAttribute           = "AXDefaultButton";
public static final String kAXCancelButtonAttribute            = "AXCancelButton";
     
//   Menu-specific attributes
public static final String kAXMenuItemCmdCharAttribute         = "AXMenuItemCmdChar";
public static final String kAXMenuItemCmdVirtualKeyAttribute   = "AXMenuItemCmdVirtualKey";
public static final String kAXMenuItemCmdGlyphAttribute        = "AXMenuItemCmdGlyph";
public static final String kAXMenuItemCmdModifiersAttribute    = "AXMenuItemCmdModifiers";
public static final String kAXMenuItemMarkCharAttribute        = "AXMenuItemMarkChar";
public static final String kAXMenuItemPrimaryUIElementAttribute = "AXMenuItemPrimaryUIElement";
     
//   Application-specific attributes
public static final String kAXMenuBarAttribute                 = "AXMenuBar";
public static final String kAXWindowsAttribute                 = "AXWindows";
public static final String kAXFrontmostAttribute               = "AXFrontmost";
public static final String kAXHiddenAttribute                  = "AXHidden";
public static final String kAXMainWindowAttribute              = "AXMainWindow";
public static final String kAXFocusedWindowAttribute           = "AXFocusedWindow";
public static final String kAXFocusedUIElementAttribute        = "AXFocusedUIElement"; 
     
//   Miscellaneous attributes
public static final String kAXHeaderAttribute                  = "AXHeader";
public static final String kAXEditedAttribute                  = "AXEdited";
public static final String kAXValueWrapsAttribute              = "AXValueWraps";
public static final String kAXTabsAttribute                    = "AXTabs";
public static final String kAXTitleUIElementAttribute          = "AXTitleUIElement";
public static final String kAXHorizontalScrollBarAttribute     = "AXHorizontalScrollBar";
public static final String kAXVerticalScrollBarAttribute       = "AXVerticalScrollBar";
public static final String kAXOverflowButtonAttribute          = "AXOverflowButton";
public static final String kAXFilenameAttribute                = "AXFilename";
public static final String kAXExpandedAttribute                = "AXExpanded";
public static final String kAXSelectedAttribute                = "AXSelected";
public static final String kAXSplittersAttribute               = "AXSplitters";
public static final String kAXNextContentsAttribute            = "AXNextContents";
public static final String kAXDocumentAttribute                = "AXDocument";
public static final String kAXDecrementButtonAttribute         = "AXDecrementButton";
public static final String kAXIncrementButtonAttribute         = "AXIncrementButton";
public static final String kAXPreviousContentsAttribute        = "AXPreviousContents";
public static final String kAXContentsAttribute                = "AXContents";
public static final String kAXIncrementorAttribute             = "AXIncrementor";
public static final String kAXHourFieldAttribute               = "AXHourField";
public static final String kAXMinuteFieldAttribute             = "AXMinuteField";
public static final String kAXSecondFieldAttribute             = "AXSecondField";
public static final String kAXAMPMFieldAttribute               = "AXAMPMField";
public static final String kAXDayFieldAttribute                = "AXDayField";
public static final String kAXMonthFieldAttribute              = "AXMonthField";
public static final String kAXYearFieldAttribute               = "AXYearField";
public static final String kAXColumnTitleAttribute             = "AXColumnTitles";
public static final String kAXURLAttribute                     = "AXURL";
public static final String kAXLabelUIElementsAttribute         = "AXLabelUIElements";
public static final String kAXLabelValueAttribute              = "AXLabelValue";
public static final String kAXShownMenuUIElementAttribute      = "AXShownMenuUIElement";
public static final String kAXServesAsTitleForUIElementsAttribute = "AXServesAsTitleForUIElements";
public static final String kAXLinkedUIElementsAttribute        = "AXLinkedUIElements";
     
//   Table and outline view attributes
public static final String kAXRowsAttribute                    = "AXRows";
public static final String kAXVisibleRowsAttribute             = "AXVisibleRows";
public static final String kAXSelectedRowsAttribute            = "AXSelectedRows";
public static final String kAXColumnsAttribute                 = "AXColumns";
public static final String kAXVisibleColumnsAttribute          = "AXVisibleColumns";
public static final String kAXSelectedColumnsAttribute         = "AXSelectedColumns";
public static final String kAXSortDirectionAttribute           = "AXSortDirection";
public static final String kAXColumnHeaderUIElementsAttribute  = "AXColumnHeaderUIElements";
public static final String kAXIndexAttribute                   = "AXIndex";
public static final String kAXDisclosingAttribute              = "AXDisclosing";
public static final String kAXDisclosedRowsAttribute           = "AXDisclosedRows";
public static final String kAXDisclosedByRowAttribute          = "AXDisclosedByRow";
     
//   Matte attributes
public static final String kAXMatteHoleAttribute               = "AXMatteHole";
public static final String kAXMatteContentUIElementAttribute   = "AXMatteContentUIElement";
     
//   Dock attributes
public static final String kAXIsApplicationRunningAttribute    = "AXIsApplicationRunning";
     
//   System-wide attributes
public static final String kAXFocusedApplicationAttribute      = "AXFocusedApplication";

// Text-suite parameterized attributes
public static final String kAXLineForIndexParameterizedAttribute = "AXLineForIndex";
public static final String kAXRangeForLineParameterizedAttribute = "AXRangeForLine";
public static final String kAXStringForRangeParameterizedAttribute = "AXStringForRange";
public static final String kAXRangeForPositionParameterizedAttribute = "AXRangeForPosition";
public static final String kAXRangeForIndexParameterizedAttribute = "AXRangeForIndex";
public static final String kAXBoundsForRangeParameterizedAttribute = "AXBoundsForRange";
public static final String kAXRTFForRangeParameterizedAttribute = "AXRTFForRange";
public static final String kAXAttributedStringForRangeParameterizedAttribute = "AXAttributedStringForRange";
public static final String kAXStyleRangeForIndexParameterizedAttribute = "AXStyleRangeForIndex";
public static final String kAXInsertionPointLineNumberAttribute = "AXInsertionPointLineNumber";

// Accessibility actions.
public static final String kAXPressAction          = "AXPress";
public static final String kAXIncrementAction      = "AXIncrement";
public static final String kAXDecrementAction      = "AXDecrement";
public static final String kAXConfirmAction        = "AXConfirm";
public static final String kAXCancelAction         = "AXCancel";
public static final String kAXRaiseAction          = "AXRaise";
public static final String kAXShowMenuAction       = "AXShowMenu";

// Focus notifications
public static final String kAXMainWindowChangedNotification = "AXMainWindowChanged";
public static final String kAXFocusedWindowChangedNotification = "AXFocusedWindowChanged";
public static final String kAXFocusedUIElementChangedNotification = "AXFocusedUIElementChanged";
     
//   Application notifications
public static final String kAXApplicationActivatedNotification = "AXApplicationActivated";
public static final String kAXApplicationDeactivatedNotification = "AXApplicationDeactivated";
public static final String kAXApplicationHiddenNotification = "AXApplicationHidden";
public static final String kAXApplicationShownNotification = "AXApplicationShown";
     
//   Window notifications
public static final String kAXWindowCreatedNotification    = "AXWindowCreated";
public static final String kAXWindowMovedNotification      = "AXWindowMoved";
public static final String kAXWindowResizedNotification    = "AXWindowResized";
public static final String kAXWindowMiniaturizedNotification = "AXWindowMiniaturized";
public static final String kAXWindowDeminiaturizedNotification = "AXWindowDeminiaturized";
     
//   New drawer, sheet, and help tag notifications
public static final String kAXDrawerCreatedNotification    = "AXDrawerCreated";
public static final String kAXSheetCreatedNotification     = "AXSheetCreated";
public static final String kAXHelpTagCreatedNotification   = "AXHelpTagCreated";
     
//   Element notifications
public static final String kAXValueChangedNotification     = "AXValueChanged";
public static final String kAXUIElementDestroyedNotification = "AXUIElementDestroyed";
     
//   Menu notifications
public static final String kAXMenuOpenedNotification       = "AXMenuOpened";
public static final String kAXMenuClosedNotification       = "AXMenuClosed";
public static final String kAXMenuItemSelectedNotification = "AXMenuItemSelected";
     
//   Table and outline view notifications
public static final String kAXRowCountChangedNotification  = "AXRowCountChanged";
     
//   Miscellaneous notifications
public static final String kAXSelectedChildrenChangedNotification = "AXSelectedChildrenChanged";
public static final String kAXSelectedTextChangedNotification = "AXSelectedTextChanged";
public static final String kAXResizedNotification          = "AXResized";
public static final String kAXMovedNotification            = "AXMoved";
public static final String kAXCreatedNotification          = "AXCreated";

// AXValue types
public static final int kAXValueCFRangeType = 4;

// AXValue Constants
public static final String kAXAscendingSortDirectionValue  = "AXAscendingSortDirection";
public static final String kAXDescendingSortDirectionValue = "AXDescendingSortDirection";
public static final String kAXHorizontalOrientationValue   = "AXHorizontalOrientation";
public static final String kAXUnknownOrientationValue      = "AXUnknownOrientation";
public static final String kAXUnknownSortDirectionValue    = "AXUnknownSortDirection";
public static final String kAXVerticalOrientationValue     = "AXVerticalOrientation";

// Error codes
public static final int kAXErrorIllegalArgument = -25201;
public static final int kAXErrorInvalidUIElement = -25202;
public static final int kAXErrorInvalidUIElementObserver = -25203;
public static final int kAXErrorCannotComplete = -25204;
public static final int kAXErrorAttributeUnsupported = -25205;
public static final int kAXErrorActionUnsupported = -25206;
public static final int kAXErrorAPIDisabled = -25211;
public static final int kAXErrorParameterizedAttributeUnsupported = -25213;


}
