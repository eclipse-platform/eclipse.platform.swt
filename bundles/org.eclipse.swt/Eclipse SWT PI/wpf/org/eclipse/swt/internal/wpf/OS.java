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
package org.eclipse.swt.internal.wpf;


import org.eclipse.swt.internal.*;

/** @jniclass flags=cpp */
public class OS extends C {
	
	/** Constants */
	public static final int BindingFlags_Instance = 4;
	public static final int BindingFlags_NonPublic = 32;
	
	public static final int DragAction_Cancel = 2;
	public static final int DragAction_Continue = 0;
	public static final int DragAction_Drop = 1;

	public static final int DragDropEffects_Copy = 1;
	public static final int DragDropEffects_Link = 4;
	public static final int DragDropEffects_Move = 2;
	public static final int DragDropEffects_None = 0;

	public static final int DragDropKeyStates_AltKey = 32;
	public static final int DragDropKeyStates_ControlKey = 8;
	public static final int DragDropKeyStates_None = 0;
	public static final int DragDropKeyStates_ShiftKey = 4;

	public static final int FontStyle_Bold = 1;
	public static final int FontStyle_Italic = 2;
	public static final int FontStyle_Regular = 0;
	public static final int FontStyle_Strikeout = 8;
	public static final int FontStyle_Underline = 4;

	public static final int Key_LeftAlt = 120;
	public static final int Key_RightAlt = 121;
	public static final int Key_LeftCtrl = 118;
	public static final int Key_RightCtrl = 119;
	public static final int Key_LeftShift = 116;
	public static final int Key_RightShift = 117;
	public static final int Key_Left = 23;
	public static final int Key_Up = 24;
	public static final int Key_Right = 25;
	public static final int Key_Down = 26;
	public static final int Key_PageUp = 19;
	public static final int Key_PageDown = 20;
	public static final int Key_End = 21;
	public static final int Key_Home = 22;
	public static final int Key_Insert = 31;
	public static final int Key_ImeProcessed = 155;
	public static final int Key_F1 = 90;
	public static final int Key_F2 = 91;
	public static final int Key_F3 = 92;
	public static final int Key_F4 = 93;
	public static final int Key_F5 = 94;
	public static final int Key_F6 = 95;
	public static final int Key_F7 = 96;
	public static final int Key_F8 = 97;
	public static final int Key_F9 = 98;
	public static final int Key_F10 = 99;
	public static final int Key_F11 = 100;
	public static final int Key_F12 = 101;
	public static final int Key_F13 = 102;
	public static final int Key_F14 = 103;
	public static final int Key_F15 = 104;
	public static final int Key_F16 = 105;
	public static final int Key_F17 = 106;
	public static final int Key_F18 = 107;
	public static final int Key_F19 = 108;
	public static final int Key_F20 = 109;
	public static final int Key_Back = 2;
	public static final int Key_Return = 6;
	public static final int Key_Delete = 32;
	public static final int Key_Escape = 13;
	public static final int Key_Tab = 3;
	public static final int Key_LineFeed = 4;
	public static final int Key_NumPad0 = 74;
	public static final int Key_NumPad1 = 75;
	public static final int Key_NumPad2 = 76;
	public static final int Key_NumPad3 = 77;
	public static final int Key_NumPad4 = 78;
	public static final int Key_NumPad5 = 79;
	public static final int Key_NumPad6 = 80;
	public static final int Key_NumPad7 = 81;
	public static final int Key_NumPad8 = 82;
	public static final int Key_NumPad9 = 83;
	public static final int Key_Multiply = 84;
	public static final int Key_Add = 85;
	public static final int Key_Separator = 86;
	public static final int Key_Subtract = 87;
	public static final int Key_System = 156;
	public static final int Key_Decimal = 88;
	public static final int Key_Divide = 89;
	public static final int Key_CapsLock = 8;
	public static final int Key_PrintScreen = 30;
	public static final int Key_Pause = 7;
	public static final int Key_Cancel = 1;
	public static final int Key_NumLock = 114;
	public static final int Key_Scroll = 115;
	public static final int Key_D0 = 34;
	public static final int Key_D1 = 35;
	public static final int Key_D2 = 36;
	public static final int Key_D3 = 37;
	public static final int Key_D4 = 38;
	public static final int Key_D5 = 39;
	public static final int Key_D6 = 40;
	public static final int Key_D7 = 41;
	public static final int Key_D8 = 42;
	public static final int Key_D9 = 43;
	public static final int Key_A = 44;
	public static final int Key_B = 45;
	public static final int Key_C = 46;
	public static final int Key_D = 47;
	public static final int Key_E = 48;
	public static final int Key_F = 49;
	public static final int Key_G = 50;
	public static final int Key_H = 51;
	public static final int Key_I = 52;
	public static final int Key_J = 53;
	public static final int Key_K = 54;
	public static final int Key_L = 55;
	public static final int Key_M = 56;
	public static final int Key_N = 57;
	public static final int Key_O = 58;
	public static final int Key_P = 59;
	public static final int Key_Q = 60;
	public static final int Key_R = 61;
	public static final int Key_S = 62;
	public static final int Key_T = 63;
	public static final int Key_U = 64;
	public static final int Key_V = 65;
	public static final int Key_W = 66;
	public static final int Key_X = 67;
	public static final int Key_Y = 68;
	public static final int Key_Z = 69;
	public static final int Key_OemTilde = 146;
	public static final int Key_Oem2 = 145;
	public static final int Key_Oem4 = 149;
	public static final int Key_Oem6 = 151;
	public static final int Key_Oem7 = 152;
	public static final int Key_OemPipe = 150;
	public static final int Key_OemMinus = 143;
	public static final int Key_OemPlus = 141;
	public static final int Key_OemSemicolon = 140;
	public static final int Key_OemComma = 142;
	public static final int Key_OemPeriod = 144;
	
	public static final byte Visibility_Visible = 0;
	public static final byte Visibility_Hidden = 1;
	public static final byte Visibility_Collapsed = 2;
	
	public static final int GridUnitType_Auto = 0;
	public static final int GridUnitType_Pixel = 1;
	public static final int GridUnitType_Star = 2;
	
	public static final int Orientation_Horizontal = 0; 
	public static final int Orientation_Vertical = 1;
	
	public static final int OverflowMode_Never = 2;
	
	public static final int NavigationUIVisibility_Hidden = 2;
	
	public static final int ScrollEventType_EndScroll = 0;
	public static final int ScrollEventType_First = 1;
	public static final int ScrollEventType_LargeDecrement = 2;
	public static final int ScrollEventType_LargeIncrement = 3;
	public static final int ScrollEventType_Last = 4;
	public static final int ScrollEventType_SmallDecrement = 5;
	public static final int ScrollEventType_SmallIncrement = 6;
	public static final int ScrollEventType_ThumbPosition = 7;
	public static final int ScrollEventType_ThumbTrack = 8;
	
	public static final int ShutdownMode_OnExplicitShutdown = 2;
	
	public static final int HorizontalAlignment_Left = 0;
	public static final int HorizontalAlignment_Center = 1;
	public static final int HorizontalAlignment_Right = 2;
	public static final int HorizontalAlignment_Stretch = 3;
	
	public static final int VerticalAlignment_Top = 0;
	public static final int VerticalAlignment_Center = 1;
	public static final int VerticalAlignment_Bottom = 2;
	public static final int VerticalAlignment_Stretch = 3;
	
	public static final int UriKind_RelativeOrAbsolute = 0;
	public static final int UriKind_Absolute = 1;
	public static final int UriKind_Relative = 2;
	
	public static final int Stretch_None = 0;
	public static final int Stretch_Fill = 1;
	public static final int Stretch_Uniform = 2;
	public static final int Stretch_UniformToFill = 3;
	
	public static final int PenLineCap_Flat = 0;
	public static final int PenLineCap_Round = 1;
	public static final int PenLineCap_Square = 2;
	
	public static final int PenLineJoin_Miter = 0;
	public static final int PenLineJoin_Bevel = 1;
	public static final int PenLineJoin_Round = 2;
	
	public static final int SweepDirection_Clockwise = 0;
	public static final int SweepDirection_CounterClockwise = 1;
	
	public static final int FillRule_EvenOdd = 0;
	public static final int FillRule_Nonzero = 1;
	
	public static final int BitmapScalingMode_Unspecified = 0;
	public static final int BitmapScalingMode_LowQuality = 1;
	public static final int BitmapScalingMode_HighQuality = 2;

	public static final int EdgeMode_Unspecified = 0;
	public static final int EdgeMode_Aliased = 1;
	
	public static final int FlowDirection_LeftToRight = 0;
	public static final int FlowDirection_RightToLeft = 1;

	public static final int TileMode_Tile = 4;
	
	public static final int AlignmentX_Left = 0;
	public static final int AlignmentX_Center = 1;
	public static final int AlignmentX_Right = 2;
	public static final int AlignmentY_Top = 0;
	public static final int AlignmentY_Center = 1;
	public static final int Alignmenty_Bottom = 2;
	
	public static final int BrushMappingMode_Absolute = 0;
	public static final int BrushMappingMode_RelativeToBoundingBox = 1;

	public static final int GradientSpreadMethod_Pad = 0;
	public static final int GradientSpreadMethod_Reflect = 1;
	public static final int GradientSpreadMethod_Repeat = 2;
	
	public static final int GeometryCombineMode_Union = 0;
	public static final int GeometryCombineMode_Intersect = 1;
	public static final int GeometryCombineMode_Xor = 2;
	public static final int GeometryCombineMode_Exclude = 3;
	
	public static final int TextAlignment_Left = 0;
	public static final int TextAlignment_Right = 1;
	public static final int TextAlignment_Center = 2;
	public static final int TextAlignment_Justify = 3;
	
	public static final int BaselineAlignment_Baseline = 3;
	
	public static final int TextWrapping_WrapWithOverflow = 0;
	public static final int TextWrapping_NoWrap = 1;
	public static final int TextWrapping_Wrap = 2;
		
	public static final int IntersectionDetail_Empty = 1;
	
	public static final int TextTabAlignment_Left = 0;
	
	public static final int MouseButtonState_Released = 0;
	public static final int MouseButtonState_Pressed = 1;
	
	public static final int MouseButton_Left = 0;
	public static final int MouseButton_Middle = 1;
	public static final int MouseButton_Right = 2;
	public static final int MouseButton_XButton1 = 3;
	public static final int MouseButton_XButton2 = 4;
	
	public static final int MouseButtons_None = 0;
	public static final int MouseButtons_Left = 1048576;
	public static final int MouseButtons_Right = 2097152;
	public static final int MouseButtons_Middle = 4194304;
	public static final int MouseButtons_XButton1 = 8388608;
	public static final int MouseButtons_XButton2 = 16777216;
	
	public static final int ModifierKeys_Alt = 1;
	public static final int ModifierKeys_Control = 2;
	public static final int ModifierKeys_Shift = 4;
	
	public static final int ResizeMode_NoResize = 0;
	public static final int ResizeMode_CanMinimize = 1;
	public static final int ResizeMode_CanResize = 2;
	public static final int ResizeMode_CanResizeWithGrip = 3;
	
	public static final int WindowStyle_None = 0;
	public static final int WindowStyle_SingleBorderWindow = 1;
	public static final int WindowStyle_ThreeDBorderWindow = 2;
	public static final int WindowStyle_ToolWindow = 3;
	

	public static final int WebBrowserReadyState_Uninitialized = 0;
	public static final int WebBrowserReadyState_Loading = 1;
	public static final int WebBrowserReadyState_Loaded = 2;
	public static final int WebBrowserReadyState_Interactive = 3;
	public static final int WebBrowserReadyState_Complete = 4;
	
	public static final int Dock_Top = 1;
	public static final int Dock_Bottom = 3;

	public static final int SelectionMode_Single = 0;
	public static final int SelectionMode_Multiple = 1;
	public static final int SelectionMode_Extended = 2;
	
	public static final int TickPlacement_None = 0;
    public static final int TickPlacement_TopLeft = 1;
    public static final int TickPlacement_BottomRight = 2;
	public static final int TickPlacement_Both = 3;

	public static final int WindowState_Normal = 0;
    public static final int WindowState_Minimized = 1;
    public static final int WindowState_Maximized = 2;
    
    public static final int BitmapCreateOptions_None = 0;
    public static final int BitmapCreateOptions_PreservePixelFormat = 1;
    public static final int BitmapCacheOption_Default = 0;
    
    public static final int MessageBoxButton_OK = 0;
    public static final int MessageBoxButton_OKCancel = 1;
    public static final int MessageBoxButton_YesNoCancel = 3;
    public static final int MessageBoxButton_YesNo = 4;
    
    public static final int MessageBoxImage_None = 0;
    public static final int MessageBoxImage_Error = 16;
    public static final int MessageBoxImage_Hand = 16;
    public static final int MessageBoxImage_Stop = 16;
    public static final int MessageBoxImage_Question = 32;
    public static final int MessageBoxImage_Exclamation = 48;
    public static final int MessageBoxImage_Warning = 48;
    public static final int MessageBoxImage_Information = 64;
    public static final int MessageBoxImage_Asterisk = 64;
    
    public static final int MessageBoxResult_None = 0;
    public static final int MessageBoxResult_OK = 1;
    public static final int MessageBoxResult_Cancel = 2;
    public static final int MessageBoxResult_Yes = 6;
    public static final int MessageBoxResult_No = 7;
    
    public static final int KeyboardNavigationMode_None = 3;
    
    public static final int PlacementMode_AbsolutePoint = 5;
    public static final int PlacementMode_MousePoint = 8;
    
    public static final int DispatcherPriority_Inactive = 0;
    public static final int DispatcherPriority_Normal = 9;
    public static final int DispatcherPriority_Send = 10;
    
    public static final int ScrollBarVisibility_Disabled = 0;
    public static final int ScrollBarVisibility_Auto = 1;
    public static final int ScrollBarVisibility_Hidden = 2;
    public static final int ScrollBarVisibility_Visible = 3;
 
 	public static final int FocusNavigationDirection_Next = 0;
	public static final int FocusNavigationDirection_Previous = 1;
	public static final int FocusNavigationDirection_First = 2;
	public static final int FocusNavigationDirection_Last = 3;
	
	public static final int RelativeSourceMode_FindAncestor = 3;

	public static final int DialogResult_OK = 1;
	
	public static final int TextDecorationUnit_FontRecommended = 0;
	public static final int TextDecorationLocation_Underline = 0;
	public static final int TextDecorationLocation_Strikethrough = 2;
	
	public static final int ToleranceType_Absolute = 0;
	

	/*
	* Note that these GCHandles are leaked.
	*/
	public static final int FontStyles_Italic = FontStyles_Italic();
	public static final int FontStyles_Normal = FontStyles_Normal();	
	public static final int FontStyles_Oblique = FontStyles_Oblique();	
	public static final int FontWeights_Bold = FontWeights_Bold();
	public static final int FontWeights_Normal = FontWeights_Normal();	
	public static final int FontStretches_Normal = FontStretches_Normal();
	public static final int Colors_White = Colors_White();
	public static final int Colors_Black = Colors_Black();
	public static final int Colors_Transparent = Colors_Transparent();
	public static final int SystemColors_ControlColor = SystemColors_ControlColor();
	
/** Handlers */

/** @method flags=no_gen */
public static final native int gcnew_CancelEventHandler(int jniRef, String string);
/** @method flags=gcnew no_gen */
public static final native int gcnew_ContextMenuEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_RoutedPropertyChangedEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_RoutedPropertyChangedEventHandlerObject(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_RoutedEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_DragDeltaEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_EventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_ExecutedRoutedEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_FormsMouseEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_DispatcherHookEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_SelectionChangedEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_SizeChangedEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_ScrollEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_KeyEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_KeyboardFocusChangedEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_TextChangedEventHandler(int jniRef, String string);
/** @method flags=no_gen gcnew */
public static final native int gcnew_TextCompositionEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_TimerHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_MouseEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_MouseButtonEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_MouseWheelEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_NoArgsDelegate(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_GiveFeedbackEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_QueryContinueDragEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_DragEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_WebBrowserNavigatingEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_WebBrowserNavigatedEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_WebBrowserProgressChangedEventHandler(int jniRef, String string);
/** @method flags=no_gen */
public static final native int gcnew_WebBrowserDocumentCompletedEventHandler(int jniRef, String string);

/** JNI natives */

/** @method flags=jni */
public static final native int NewGlobalRef(Object object);
/**
 * @method flags=jni
 * @param globalRef cast=(jobject)
 */
public static final native void DeleteGlobalRef(int globalRef);
/** @method flags=no_gen */
public static final native Object JNIGetObject(int globalRef);

/** Natives */

/**
 * @method flags=getter
 * @param sender cast=(AccessText^),flags=gcobject
 */
public static final native char AccessText_AccessKey(int sender);
/**
 * @method flags=setter
 * @param sender cast=(AccessText^),flags=gcobject
 * @param value cast=(String^),flags=gcobject
 */
public static final native void AccessText_Text(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(AccessText^),flags=gcobject
 * @param value cast=(TextWrapping)
 */
public static final native void AccessText_TextWrapping(int sender, int value);
/** @method accessor=ApplicationCommands::Cut,flags=const gcobject */
public static final native int ApplicationCommands_Cut();
/** @method accessor=ApplicationCommands::Paste,flags=const gcobject */
public static final native int ApplicationCommands_Paste();
/** @method accessor=ApplicationCommands::Redo,flags=const gcobject */
public static final native int ApplicationCommands_Redo();
/** @method accessor=ApplicationCommands::Undo,flags=const gcobject */
public static final native int ApplicationCommands_Undo();
/** @method accessor=Application::Current,flags=const gcobject */
public static final native int Application_Current();
/**
 * @method flags=gcobject getter
 * @param sender cast=(Application ^),flags=gcobject
 */
public static final native int Application_Dispatcher(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Application^),flags=gcobject
 */
public static final native int Application_Resources(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Application^),flags=gcobject
 * @param value cast=(ResourceDictionary^),flags=gcobject
 */
public static final native void Application_Resources(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(Application^),flags=gcobject
 */
public static final native void Application_Run(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(Application^),flags=gcobject
 */
public static final native void Application_Shutdown(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Application^),flags=gcobject
 * @param value cast=(ShutdownMode)
 */
public static final native void Application_ShutdownMode(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Application^),flags=gcobject
 */
public static final native int Application_Windows(int sender);
/**
 * @method accessor=Array::CreateInstance,flags=gcobject
 * @param elementType cast=(Type^),flags=gcobject
 */
public static final native int Array_CreateInstance(int elementType, int length);
/**
 * @method flags=cpp
 * @param sender cast=(Array^),flags=gcobject
 */
public static final native int Array_GetLength(int sender, int dimension);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(Array^),flags=gcobject
 */
public static final native int Array_GetValue (int sender, int index);
/**
 * @method flags=cpp
 * @param sender cast=(Array^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void Array_SetValue (int sender, int value, int index);
/**
 * @method flags=cpp
 * @param sender cast=(ArrayList^),flags=gcobject
 */
public static final native void ArrayList_Clear(int sender);
/**
 * @method flags=getter
 * @param sender cast=(ArrayList^),flags=gcobject
 */
public static final native int ArrayList_Count(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(ArrayList^),flags=gcobject
 */
public static final native int ArrayList_default(int sender, int index);
/**
 * @method flags=setter
 * @param sender cast=(ArrayList^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void ArrayList_default(int sender, int index, int value);
/**
 * @method flags=cpp
 * @param sender cast=(ArrayList^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void ArrayList_Insert(int sender, int index, int value);
/**
 * @method flags=cpp
 * @param sender cast=(ArrayList^),flags=gcobject
 */
public static final native void ArrayList_RemoveAt(int sender, int index);
/**
 * @method flags=setter
 * @param sender cast=(Binding^),flags=gcobject
 * @param value cast=(RelativeSource^),flags=gcobject
 */
public static final native void Binding_RelativeSource(int sender, int value);
/**
 * @method flags=no_gen cpp
 * @param sender cast=(System::Drawing::Bitmap^),flags=gcobject
 */
public static final native int Bitmap_GetHicon(int sender);
/**
 * @method accessor=BitmapDecoder::Create,flags=gcobject
 * @param stream cast=(System::IO::Stream^),flags=gcobject
 * @param createOptions cast=(BitmapCreateOptions)
 * @param cacheOption cast=(BitmapCacheOption)
 */
public static final native int BitmapDecoder_Create(int stream, int createOptions, int cacheOption);
/**
 * @method flags=gcobject getter
 * @param sender cast=(BitmapDecoder^),flags=gcobject
 */
public static final native int BitmapDecoder_Frames(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(BitmapEncoder^),flags=gcobject
 */
public static final native int BitmapEncoder_Frames(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(BitmapEncoder^),flags=gcobject
 * @param stream cast=(System::IO::Stream^),flags=gcobject
 */
public static final native void BitmapEncoder_Save(int sender, int stream);
/**
 * @method accessor=BitmapFrame::Create,flags=gcobject
 * @param source cast=(BitmapSource^),flags=gcobject
 */
public static final native int BitmapFrame_Create(int source);
/**
 * @method flags=cpp
 * @param sender cast=(System::Collections::Generic::IList<BitmapFrame^>^),flags=gcobject
 * @param frame cast=(BitmapFrame^),flags=gcobject
 */
public static final native void BitmapFrameCollection_Add(int sender, int frame);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Collections::Generic::IList<BitmapFrame^>^),flags=gcobject
 */
public static final native int BitmapFrameCollection_default(int sender, int index);
/**
 * @method flags=cpp
 * @param sender cast=(BitmapImage^),flags=gcobject
 */
public static final native void BitmapImage_BeginInit(int sender);
/**
 * @method flags=setter
 * @param sender cast=(BitmapImage^),flags=gcobject
 * @param value cast=(BitmapCreateOptions)
 */
public static final native void BitmapImage_CreateOptions(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(BitmapImage^),flags=gcobject
 */
public static final native void BitmapImage_EndInit(int sender);
/**
 * @method flags=setter
 * @param sender cast=(BitmapImage^),flags=gcobject
 * @param uri cast=(Uri^),flags=gcobject
 */
public static final native void BitmapImage_UriSource(int sender, int uri);
/**
 * @method flags=gcobject getter
 * @param sender cast=(BitmapPalette^),flags=gcobject
 */
public static final native int BitmapPalette_Colors(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(BitmapSource^),flags=gcobject
 */
public static final native int BitmapSource_Clone(int sender);
/**
 * @method accessor=BitmapSource::Create,flags=gcobject
 * @param pixelFormat cast=(PixelFormat),flags=gcobject
 * @param palette cast=(BitmapPalette^),flags=gcobject
 * @param buffer cast=(IntPtr)
 */
public static final native int BitmapSource_Create(int pixelWidth, int pixelHeight, double dpiX, double dpiY, int pixelFormat, int palette, byte[] buffer, int bufferSize, int stride);
/**
 * @method flags=cpp
 * @param sender cast=(BitmapSource^),flags=gcobject
 * @param sourceRect cast=(Int32Rect),flags=gcobject
 * @param buffer cast=(IntPtr)
 */
public static final native void BitmapSource_CopyPixels(int sender, int sourceRect, byte[] buffer, int bufferSize, int stride);
/**
 * @method flags=gcobject getter
 * @param sender cast=(BitmapSource^),flags=gcobject
 */
public static final native int BitmapSource_Format(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(BitmapSource^),flags=gcobject
 */
public static final native int BitmapSource_Palette(int sender);
/**
 * @method flags=getter
 * @param sender cast=(BitmapSource^),flags=gcobject
 */
public static final native int BitmapSource_PixelHeight(int sender);
/**
 * @method flags=getter
 * @param sender cast=(BitmapSource^),flags=gcobject
 */
public static final native int BitmapSource_PixelWidth(int sender);
/** @method accessor=Border::typeid,flags=const gcobject */
public static final native int Border_typeid();
/** @method accessor=Brushes::White,flags=const gcobject */
public static final native int Brushes_White();
/** @method accessor=Brushes::Black,flags=const gcobject */
public static final native int Brushes_Black();
/** @method accessor=Brushes::Navy,flags=const gcobject */
public static final native int Brushes_Navy();
/** @method accessor=Brushes::Red,flags=const gcobject */
public static final native int Brushes_Red();
/** @method accessor=Brushes::Transparent,flags=const gcobject */
public static final native int Brushes_Transparent();
/** @method accessor=Brushes::LightSkyBlue,flags=const gcobject */
public static final native int Brushes_LightSkyBlue();
/**
 * @method flags=setter
 * @param sender cast=(Brush^),flags=gcobject
 */
public static final native void Brush_Opacity(int sender, double opacity);
/**
 * @method flags=getter
 * @param sender cast=(Button^),flags=gcobject
 */
public static final native boolean Button_IsDefault(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Button^),flags=gcobject
 */
public static final native void Button_IsDefault(int sender, boolean value);
/**
 * @method flags=adder
 * @param sender cast=(ButtonBase^),flags=gcobject
 * @param handler cast=(RoutedEventHandler^),flags=gcobject
 */
public static final native void ButtonBase_Click(int sender, int handler);
/** @method accessor=ButtonBase::ClickEvent,flags=const gcobject */
public static final native int ButtonBase_ClickEvent();
/** @method accessor=Byte::typeid,flags=const gcobject */
public static final native int Byte_typeid();
/**
 * @method flags=setter
 * @param sender cast=(CancelEventArgs^),flags=gcobject
 */
public static final native void CancelEventArgs_Cancel(int sender, boolean value);
/**
 * @method accessor=Canvas::GetLeft
 * @param element cast=(UIElement^),flags=gcobject
 */
public static final native double Canvas_GetLeft(int element);
/**
 * @method accessor=Canvas::GetTop
 * @param element cast=(UIElement^),flags=gcobject
 */
public static final native double Canvas_GetTop(int element);
/**
 * @method accessor=Canvas::SetLeft
 * @param element cast=(UIElement^),flags=gcobject
 */
public static final native void Canvas_SetLeft(int element, double length);
/**
 * @method accessor=Canvas::SetTop
 * @param element cast=(UIElement^),flags=gcobject
 */
public static final native void Canvas_SetTop(int element, double length);
/** @method accessor=Canvas::typeid,flags=const gcobject */
public static final native int Canvas_typeid();
/**
 * @method flags=getter
 * @param sender cast=(CharacterHit^),flags=gcobject
 */
public static final native int CharacterHit_FirstCharacterIndex(int sender);
/**
 * @method flags=getter
 * @param sender cast=(CharacterHit^),flags=gcobject
 */
public static final native int CharacterHit_TrailingLength(int sender);
/** @method accessor=CheckBox::typeid,flags=const gcobject */
public static final native int CheckBox_typeid();
/** @method accessor=Clipboard::Clear */
public static final native void Clipboard_Clear();
/**
 * @method accessor=Clipboard::ContainsData
 * @param format cast=(String^),flags=gcobject
 */
public static final native boolean Clipboard_ContainsData(int format);
/**
 * @method accessor=Clipboard::GetData,flags=gcobject
 * @param format cast=(String^),flags=gcobject
 */
public static final native int Clipboard_GetData(int format);
/** @method accessor=Clipboard::GetDataObject,flags=gcobject */
public static final native int Clipboard_GetDataObject();
/** @method accessor=Clipboard::GetText,flags=gcobject */
public static final native int Clipboard_GetText();
/**
 * @method accessor=Clipboard::SetData
 * @param format cast=(String^),flags=gcobject
 * @param data cast=(Object^),flags=gcobject
 */
public static final native void Clipboard_SetData(int format, int data);
/**
 * @method accessor=Clipboard::SetDataObject
 * @param data cast=(Object^),flags=gcobject
 */
public static final native void Clipboard_SetDataObject(int data, boolean copy);
/** @method accessor=Color::FromArgb,flags=struct gcobject */
public static final native int Color_FromArgb(byte a, byte r, byte g, byte b);
/**
 * @method flags=getter
 * @param sender cast=(Color^),flags=gcobject
 */
public static final native byte Color_A(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Color^),flags=gcobject
 */
public static final native byte Color_B(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Color^),flags=gcobject
 */
public static final native byte Color_G(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Color^),flags=gcobject
 */
public static final native byte Color_R(int sender);
/**
 * @method flags=setter
 * @param sender cast=(System::Windows::Forms::ColorDialog^),flags=gcobject
 */
public static final native void ColorDialog_AnyColor(int sender, boolean value);
/**
 * @method flags=setter
 * @param sender cast=(System::Windows::Forms::ColorDialog^),flags=gcobject
 * @param color cast=(System::Drawing::Color),flags=gcobject
 */
public static final native void ColorDialog_Color(int sender, int color);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Windows::Forms::ColorDialog^),flags=gcobject
 */
public static final native int ColorDialog_Color(int sender);
/**
 * @method flags=setter
 * @param sender cast=(System::Windows::Forms::ColorDialog^),flags=gcobject
 * @param colors cast=(array<int>^),flags=gcobject
 */
public static final native void ColorDialog_CustomColors(int sender, int colors);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Windows::Forms::ColorDialog^),flags=gcobject
 */
public static final native int ColorDialog_CustomColors(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(System::Collections::Generic::List<Color>^),flags=gcobject
 * @param color cast=(Color),flags=gcobject
 */
public static final native void ColorList_Add(int sender, int color);
/**
 * @method flags=getter
 * @param sender cast=(System::Collections::Generic::IList<Color>^),flags=gcobject
 */
public static final native int ColorList_Count(int sender);
/**
 * @method flags=getter gcobject
 * @param sender cast=(System::Collections::Generic::IEnumerator<Color>^),flags=gcobject
 */
public static final native int ColorList_Current(int sender);
/**
 * @method flags=gcobject cpp
 * @param sender cast=(System::Collections::Generic::IEnumerable<Color>^),flags=gcobject
 */
public static final native int ColorList_GetEnumerator(int sender);
/** @method accessor=Colors::White,flags=const struct gcobject */
public static final native int Colors_White();
/** @method accessor=Colors::Black,flags=const struct gcobject */
public static final native int Colors_Black();
/** @method accessor=Colors::Red,flags=const struct gcobject */
public static final native int Colors_Red();
/** @method accessor=Colors::Maroon,flags=const struct gcobject */
public static final native int Colors_Maroon();
/** @method accessor=Colors::Lime,flags=const struct gcobject */
public static final native int Colors_Lime();
/** @method accessor=Colors::Green,flags=const struct gcobject */
public static final native int Colors_Green();
/** @method accessor=Colors::Olive,flags=const struct gcobject */
public static final native int Colors_Olive();
/** @method accessor=Colors::Blue,flags=const struct gcobject */
public static final native int Colors_Blue();
/** @method accessor=Colors::Navy,flags=const struct gcobject */
public static final native int Colors_Navy();
/** @method accessor=Colors::LightSkyBlue,flags=const struct gcobject */
public static final native int Colors_LightSkyBlue();
/** @method accessor=Colors::Magenta,flags=const struct gcobject */
public static final native int Colors_Magenta();
/** @method accessor=Colors::Purple,flags=const struct gcobject */
public static final native int Colors_Purple();
/** @method accessor=Colors::Cyan,flags=const struct gcobject */
public static final native int Colors_Cyan();
/** @method accessor=Colors::Teal,flags=const struct gcobject */
public static final native int Colors_Teal();
/** @method accessor=Colors::Transparent,flags=const struct gcobject */
public static final native int Colors_Transparent();
/** @method accessor=Colors::Silver,flags=const struct gcobject */
public static final native int Colors_Silver();
/** @method accessor=Colors::DarkGray,flags=const struct gcobject */
public static final native int Colors_DarkGray();
/** @method accessor=Colors::Yellow,flags=const struct gcobject */
public static final native int Colors_Yellow();
/**
 * @method flags=setter
 * @param sender cast=(ColumnDefinition^),flags=gcobject
 * @param width cast=(GridLength),flags=gcobject
 */
public static final native void ColumnDefinition_Width(int sender, int width);
/**
 * @method flags=cpp
 * @param sender cast=(ColumnDefinitionCollection^),flags=gcobject
 * @param column cast=(ColumnDefinition^),flags=gcobject
 */
public static final native void ColumnDefinitionCollection_Add(int sender, int column);
/**
 * @method flags=getter
 * @param sender cast=(ComboBox^),flags=gcobject
 */
public static final native boolean ComboBox_IsDropDownOpen(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ComboBox^),flags=gcobject
 */
public static final native void ComboBox_IsDropDownOpen(int sender, boolean value);
/**
 * @method flags=setter
 * @param sender cast=(ComboBox^),flags=gcobject
 */
public static final native void ComboBox_IsEditable(int sender, boolean value);
/**
 * @method flags=gcobject getter
 * @param handle cast=(ComboBox^),flags=gcobject
 */
public static final native int ComboBox_SelectionBoxItem(int handle);
/**
 * @method accessor=CommandManager::AddPreviewExecutedHandler,flags=struct
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(ExecutedRoutedEventHandler^),flags=gcobject
 */
public static final native void CommandManager_AddPreviewExecutedHandler(int sender, int handler);
/**
 * @method flags=cpp
 * @param sender cast=(CommonDialog^),flags=gcobject
 * @param parent cast=(Window^),flags=gcobject
 */
public static final native boolean CommonDialog_ShowDialog(int sender, int parent);
/**
 * @method flags=cpp
 * @param sender cast=(CompositeCollection^),flags=gcobject
 * @param object cast=(Object^),flags=gcobject
 */
public static final native int CompositeCollection_IndexOf(int sender, int object);
/**
 * @method flags=cpp
 * @param sender cast=(CompositeCollection^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void CompositeCollection_Insert(int sender, int index, int value);
/**
 * @method flags=cpp
 * @param sender cast=(CompositeCollection^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void CompositeCollection_Remove(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(CompositeCollection^),flags=gcobject
 */
public static final native void CompositeCollection_RemoveAt(int sender, int value);
/** @method accessor=Console::Beep */
public static final native void Console_Beep();
/**
 * @method flags=gcobject getter
 * @param sender cast=(Control^),flags=gcobject
 */
public static final native int Control_Padding(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Control^),flags=gcobject
 * @param value cast=(Thickness),flags=gcobject
 */
public static final native void Control_Padding(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(ContainerVisual^),flags=gcobject
 */
public static final native int ContainerVisual_Clip(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ContainerVisual^),flags=gcobject
 * @param clip cast=(Geometry^),flags=gcobject
 */
public static final native void ContainerVisual_Clip(int sender, int clip);
/**
 * @method flags=gcobject getter
 * @param sender cast=(ContentControl^),flags=gcobject
 */
public static final native int ContentControl_Content(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ContentControl^),flags=gcobject
 * @param content cast=(Object^),flags=gcobject
 */
public static final native void ContentControl_Content(int sender, int content);
/**
 * @method flags=gcobject getter
 * @param sender cast=(ContentPresenter^),flags=gcobject
 */
public static final native int ContentPresenter_Content(int sender);
/** @method accessor=ContentPresenter::typeid,flags=const gcobject */
public static final native int ContentPresenter_typeid();
/**
 * @method flags=setter
 * @param sender cast=(ContextMenu^),flags=gcobject
 */
public static final native void ContextMenu_IsOpen(int sender, boolean value);
/**
 * @method flags=setter
 * @param sender cast=(ContextMenu^),flags=gcobject
 * @param mode cast=(PlacementMode)
 */
public static final native void ContextMenu_Placement(int sender, int mode);
/**
 * @method flags=setter
 * @param sender cast=(ContextMenu^),flags=gcobject
 */
public static final native void ContextMenu_HorizontalOffset(int sender, int offset);
/**
 * @method flags=setter
 * @param sender cast=(ContextMenu^),flags=gcobject
 */
public static final native void ContextMenu_VerticalOffset(int sender, int offset);
/**
 * @method flags=adder
 * @param sender cast=(ContextMenu^),flags=gcobject
 * @param handler cast=(RoutedEventHandler^),flags=gcobject
 */
public static final native void ContextMenu_Opened(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(ContextMenu^),flags=gcobject
 * @param handler cast=(RoutedEventHandler^),flags=gcobject
 */
public static final native void ContextMenu_Closed(int sender, int handler);
/**
 * @method flags=getter
 * @param sender cast=(ContextMenuEventArgs^),flags=gcobject
 */
public static final native double ContextMenuEventArgs_CursorLeft(int sender);
/**
 * @method flags=getter
 * @param sender cast=(ContextMenuEventArgs^),flags=gcobject
 */
public static final native double ContextMenuEventArgs_CursorTop(int sender);
/** @method accessor=Control::BackgroundProperty,flags=const gcobject */
public static final native int Control_BackgroundProperty();
/** @method accessor=Control::BorderBrushProperty,flags=const gcobject */
public static final native int Control_BorderBrushProperty();
/** @method accessor=Control::BorderThicknessProperty,flags=const gcobject */
public static final native int Control_BorderThicknessProperty();
/** @method accessor=Control::ForegroundProperty,flags=const gcobject */
public static final native int Control_ForegroundProperty();
/** @method accessor=Control::FontFamilyProperty,flags=const gcobject */
public static final native int Control_FontFamilyProperty();
/** @method accessor=Control::FontStyleProperty,flags=const gcobject */
public static final native int Control_FontStyleProperty();
/** @method accessor=Control::FontWeightProperty,flags=const gcobject */
public static final native int Control_FontWeightProperty();
/** @method accessor=Control::FontStretchProperty,flags=const gcobject */
public static final native int Control_FontStretchProperty();
/** @method accessor=Control::FontSizeProperty,flags=const gcobject */
public static final native int Control_FontSizeProperty();
/**
 * @method flags=setter
 * @param sender cast=(Control^),flags=gcobject
 * @param value cast=(Thickness),flags=gcobject
 */
public static final native void Control_BorderThickness(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(Control^),flags=gcobject
 * @param value cast=(Brush^),flags=gcobject
 */
public static final native void Control_Background(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(Control^),flags=gcobject
 * @param value cast=(Brush^),flags=gcobject
 */
public static final native void Control_Foreground(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Control^),flags=gcobject
 */
public static final native int Control_FontFamily(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Control^),flags=gcobject
 * @param value cast=(FontFamily^),flags=gcobject
 */
public static final native void Control_FontFamily(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(Control^),flags=gcobject
 * @param value cast=(FontStyle),flags=gcobject
 */
public static final native void Control_FontStyle(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(Control^),flags=gcobject
 * @param value cast=(FontWeight),flags=gcobject
 */
public static final native void Control_FontWeight(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(Control^),flags=gcobject
 * @param value cast=(FontStretch),flags=gcobject
 */
public static final native void Control_FontStretch(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(Control^),flags=gcobject
 */
public static final native double Control_FontSize(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Control^),flags=gcobject
 */
public static final native void Control_FontSize(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(Control ^),flags=gcobject
 */
public static final native int Control_HorizontalContentAlignment(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Control ^),flags=gcobject
 * @param value cast=(HorizontalAlignment)
 */
public static final native void Control_HorizontalContentAlignment(int sender, int value);
/**
 * @method flags=adder
 * @param sender cast=(Control^),flags=gcobject
 * @param handler cast=(MouseButtonEventHandler^),flags=gcobject
 */
public static final native void Control_MouseDoubleClick(int sender, int handler);
/** @method accessor=Control::MouseDoubleClickEvent,flags=const gcobject */
public static final native int Control_MouseDoubleClickEvent();
/**
 * @method flags=adder
 * @param sender cast=(Control^),flags=gcobject
 * @param handler cast=(MouseButtonEventHandler^),flags=gcobject
 */
public static final native void Control_PreviewMouseDoubleClick(int sender, int handler);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Control^),flags=gcobject
 */
public static final native int Control_Template(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Control^),flags=gcobject
 * @param value cast=(ControlTemplate^),flags=gcobject
 */
public static final native void Control_Template(int sender, int value);
/** @method accessor=Control::TemplateProperty,flags=const gcobject */
public static final native int Control_TemplateProperty();
/**
 * @method flags=setter
 * @param sender cast=(Control^),flags=gcobject
 * @param value cast=(VerticalAlignment)
 */
public static final native void Control_VerticalContentAlignment(int sender, int value);
/** @method accessor=CultureInfo::CurrentUICulture,flags=gcobject const */
public static final native int CultureInfo_CurrentUICulture();
/** @method accessor=Cursors::AppStarting,flags=const gcobject */
public static final native int Cursors_AppStarting();
/** @method accessor=Cursors::Arrow,flags=const gcobject */
public static final native int Cursors_Arrow();
/** @method accessor=Cursors::Hand,flags=const gcobject */
public static final native int Cursors_Hand();
/** @method accessor=Cursors::Wait,flags=const gcobject */
public static final native int Cursors_Wait();
/** @method accessor=Cursors::Cross,flags=const gcobject */
public static final native int Cursors_Cross();
/** @method accessor=Cursors::Help,flags=const gcobject */
public static final native int Cursors_Help();
/** @method accessor=Cursors::SizeAll,flags=const gcobject */
public static final native int Cursors_SizeAll();
/** @method accessor=Cursors::SizeNS,flags=const gcobject */
public static final native int Cursors_SizeNS();
/** @method accessor=Cursors::SizeNWSE,flags=const gcobject */
public static final native int Cursors_SizeNWSE();
/** @method accessor=Cursors::SizeNESW,flags=const gcobject */
public static final native int Cursors_SizeNESW();
/** @method accessor=Cursors::SizeWE,flags=const gcobject */
public static final native int Cursors_SizeWE();
/** @method accessor=Cursors::ScrollE,flags=const gcobject */
public static final native int Cursors_ScrollE();
/** @method accessor=Cursors::ScrollN,flags=const gcobject */
public static final native int Cursors_ScrollN();
/** @method accessor=Cursors::ScrollNE,flags=const gcobject */
public static final native int Cursors_ScrollNE();
/** @method accessor=Cursors::ScrollNW,flags=const gcobject */
public static final native int Cursors_ScrollNW();
/** @method accessor=Cursors::ScrollS,flags=const gcobject */
public static final native int Cursors_ScrollS();
/** @method accessor=Cursors::ScrollSE,flags=const gcobject */
public static final native int Cursors_ScrollSE();
/** @method accessor=Cursors::ScrollSW,flags=const gcobject */
public static final native int Cursors_ScrollSW();
/** @method accessor=Cursors::ScrollW,flags=const gcobject */
public static final native int Cursors_ScrollW();
/** @method accessor=Cursors::IBeam,flags=const gcobject */
public static final native int Cursors_IBeam();
/** @method accessor=Cursors::UpArrow,flags=const gcobject */
public static final native int Cursors_UpArrow();
/** @method accessor=Cursors::No,flags=const gcobject */
public static final native int Cursors_No();
/**
 * @method accessor=System::Windows::Interop::CursorInteropHelper::Create,flags=gcobject
 * @param safeHandle cast=(SafeHandle^),flags=gcobject
 */
public static final native int CursorInteropHelper_Create(int safeHandle);
/** @method accessor=DashStyles::Dash,flags=const gcobject */
public static final native int DashStyles_Dash();
/** @method accessor=DashStyles::DashDot,flags=const gcobject */
public static final native int DashStyles_DashDot();
/** @method accessor=DashStyles::DashDotDot,flags=const gcobject */
public static final native int DashStyles_DashDotDot();
/** @method accessor=DashStyles::Dot,flags=const gcobject */
public static final native int DashStyles_Dot();
/** @method accessor=DashStyles::Solid,flags=const gcobject */
public static final native int DashStyles_Solid();
/** @method accessor=DataFormats::Bitmap,flags=const gcobject */
public static final native int DataFormats_Bitmap();
/** @method accessor=DataFormats::FileDrop,flags=const gcobject */
public static final native int DataFormats_FileDrop();
/** @method accessor=DataFormats::Html,flags=const gcobject */
public static final native int DataFormats_Html();
/** @method accessor=DataFormats::Rtf,flags=const gcobject */
public static final native int DataFormats_Rtf();
/** @method accessor=DataFormats::UnicodeText,flags=const gcobject */
public static final native int DataFormats_UnicodeText();
/**
 * @method flags=cpp gcobject
 * @param sender cast=(DataObject^),flags=gcobject
 * @param format cast=(String^),flags=gcobject
 */
public static final native int DataObject_GetData(int sender, int format, boolean autoConvert);
/**
 * @method flags=cpp
 * @param sender cast=(DataObject^),flags=gcobject
 * @param format cast=(String^),flags=gcobject
 */
public static final native boolean DataObject_GetDataPresent(int sender, int format, boolean autoConvert);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(DataObject^),flags=gcobject
 */
public static final native int DataObject_GetFormats(int sender, boolean autoConvert);
/**
 * @method flags=cpp
 * @param sender cast=(DataObject^),flags=gcobject
 * @param format cast=(String^),flags=gcobject
 * @param data cast=(Object^),flags=gcobject
 */
public static final native void DataObject_SetData(int sender, int format, int data, boolean autoConvert);
/**
 * @method flags=cpp
 * @param sender cast=(DependencyObject^),flags=gcobject
 * @param property cast=(DependencyProperty^),flags=gcobject
 */
public static final native void DependencyObject_ClearValue(int sender, int property);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(DependencyObject^),flags=gcobject
 * @param property cast=(DependencyProperty^),flags=gcobject
 */
public static final native int DependencyObject_GetValue(int sender, int property);
/**
 * @method accessor=GetValue,flags=cpp
 * @param sender cast=(DependencyObject^),flags=gcobject
 * @param property cast=(DependencyProperty^),flags=gcobject
 */
public static final native double DependencyObject_GetValueDouble(int sender, int property);
/**
 * @method accessor=GetValue,flags=cpp
 * @param sender cast=(DependencyObject^),flags=gcobject
 * @param property cast=(DependencyProperty^),flags=gcobject
 */
public static final native int DependencyObject_GetValueInt(int sender, int property);
/**
 * @method flags=cpp
 * @param sender cast=(DependencyObject^),flags=gcobject
 * @param property cast=(DependencyProperty^),flags=gcobject
 * @param object cast=(Object^),flags=gcobject
 */
public static final native void DependencyObject_SetValue(int sender, int property, int object);
/** @method accessor=DependencyProperty::UnsetValue,flags=const gcobject */
public static final native int DependencyProperty_UnsetValue();
/**
 * @method flags=cpp
 * @param sender cast=(DependencyPropertyDescriptor^),flags=gcobject
 * @param object cast=(Object^),flags=gcobject
 * @param handler cast=(EventHandler^),flags=gcobject
 */
public static final native void DependencyPropertyDescriptor_AddValueChanged(int sender, int object, int handler);
/**
 * @method accessor=DependencyPropertyDescriptor::FromProperty,flags=gcobject
 * @param dp cast=(DependencyProperty^),flags=gcobject
 * @param type cast=(Type^),flags=gcobject
 */
public static final native int DependencyPropertyDescriptor_FromProperty(int dp, int type);
/**
 * @method accessor=Dispatcher::PushFrame
 * @param frame cast=(DispatcherFrame ^),flags=gcobject
 */
public static final native void Dispatcher_PushFrame(int frame);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Dispatcher ^),flags=gcobject
 */
public static final native int Dispatcher_Hooks(int sender);
/**
 * @method flags=adder
 * @param sender cast=(DispatcherHooks ^),flags=gcobject
 * @param handler cast=(EventHandler ^),flags=gcobject
 */
public static final native void DispatcherHooks_DispatcherInactive(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(DispatcherHooks ^),flags=gcobject
 * @param handler cast=(DispatcherHookEventHandler ^),flags=gcobject
 */
public static final native void DispatcherHooks_OperationAborted(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(DispatcherHooks ^),flags=gcobject
 * @param handler cast=(DispatcherHookEventHandler ^),flags=gcobject
 */
public static final native void DispatcherHooks_OperationCompleted(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(DispatcherHooks ^),flags=gcobject
 * @param handler cast=(DispatcherHookEventHandler ^),flags=gcobject
 */
public static final native void DispatcherHooks_OperationPosted(int sender, int handler);
/**
 * @method flags=getter
 * @param sender cast=(DispatcherFrame^),flags=gcobject
 */
public static final native boolean DispatcherFrame_Continue(int sender);
/**
 * @method flags=setter
 * @param sender cast=(DispatcherFrame^),flags=gcobject
 */
public static final native void DispatcherFrame_Continue(int sender, boolean value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(DispatcherHookEventArgs ^),flags=gcobject
 */
public static final native int DispatcherHookEventArgs_Operation(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(DispatcherOperation^),flags=gcobject
 */
public static final native boolean DispatcherOperation_Abort(int sender);
/**
 * @method flags=getter
 * @param sender cast=(DispatcherOperation ^),flags=gcobject
 */
public static final native int DispatcherOperation_Priority(int sender);
/**
 * @method flags=setter
 * @param sender cast=(DispatcherOperation ^),flags=gcobject
 * @param value cast=(DispatcherPriority)
 */
public static final native void DispatcherOperation_Priority(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(DispatcherOperation^),flags=gcobject
 */
public static final native int DispatcherOperation_Wait(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(Dispatcher ^),flags=gcobject
 * @param priority cast=(DispatcherPriority)
 * @param method cast=(Delegate ^),flags=gcobject
 */
public static final native int Dispatcher_BeginInvoke(int sender, int priority, int method);
/**
 * @method flags=setter
 * @param sender cast=(DispatcherTimer^),flags=gcobject
 * @param value cast=(TimeSpan),flags=gcobject
 */
public static final native void DispatcherTimer_Interval(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(DispatcherTimer^),flags=gcobject
 */
public static final native void DispatcherTimer_Start(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(DispatcherTimer^),flags=gcobject
 */
public static final native void DispatcherTimer_Stop(int sender);
/**
 * @method flags=setter
 * @param sender cast=(DispatcherTimer^),flags=gcobject
 */
public static final native void DispatcherTimer_Tag(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(DispatcherTimer^),flags=gcobject
 */
public static final native int DispatcherTimer_Tag(int sender);
/**
 * @method flags=adder
 * @param sender cast=(DispatcherTimer^),flags=gcobject
 * @param handler cast=(EventHandler^),flags=gcobject
 */
public static final native void DispatcherTimer_Tick(int sender, int handler);
/** @method accessor=DockPanel::DockProperty,flags=const gcobject */
public static final native int DockPanel_DockProperty();
/** @method accessor=DockPanel::typeid,flags=const gcobject */
public static final native int DockPanel_typeid();
/**
 * @method flags=cpp
 * @param sender cast=(DoubleCollection^),flags=gcobject
 */
public static final native void DoubleCollection_Add(int sender, double value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(DoubleAnimationUsingKeyFrames^),flags=gcobject
 */
public static final native int DoubleAnimationUsingKeyFrames_KeyFrames(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(DoubleKeyFrameCollection^),flags=gcobject
 * @param keyFrame cast=(DoubleKeyFrame^),flags=gcobject
 */
public static final native int DoubleKeyFrameCollection_Add(int sender, int keyFrame);
/**
 * @method flags=getter
 * @param e cast=(DragDeltaEventArgs^),flags=gcobject
 */
public static final native int DragDeltaEventArgs_VerticalChange(int e);
/**
 * @method flags=getter
 * @param e cast=(DragDeltaEventArgs^),flags=gcobject
 */
public static final native int DragDeltaEventArgs_HorizontalChange(int e);
/**
 * @method accessor=DragDrop::DoDragDrop
 * @param dragSource cast=(DependencyObject^),flags=gcobject
 * @param data cast=(Object^),flags=gcobject
 * @param allowedEffects cast=(DragDropEffects)
 */
public static final native int DragDrop_DoDragDrop(int dragSource, int data, int allowedEffects);
/**
 * @method flags=getter
 * @param sender cast=(DragEventArgs^),flags=gcobject
 */
public static final native int DragEventArgs_AllowedEffects (int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(DragEventArgs^),flags=gcobject
 */
public static final native int DragEventArgs_Data (int sender);
/**
 * @method flags=getter
 * @param sender cast=(DragEventArgs^),flags=gcobject
 */
public static final native int DragEventArgs_Effects (int sender);
/**
 * @method flags=setter
 * @param sender cast=(DragEventArgs^),flags=gcobject
 * @param effects cast=(DragDropEffects)
 */
public static final native void DragEventArgs_Effects (int sender, int effects);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(DragEventArgs^),flags=gcobject
 * @param relativeTo cast=(IInputElement^),flags=gcobject
 */
public static final native int DragEventArgs_GetPosition (int sender, int relativeTo);
/**
 * @method flags=getter
 * @param sender cast=(DragEventArgs^),flags=gcobject
 */
public static final native int DragEventArgs_KeyStates (int sender);
/** @method accessor=System::Drawing::Color::FromArgb,flags=gcobject */
public static final native int DrawingColor_FromArgb(int a, int r, int g, int b);
/**
 * @method flags=cpp
 * @param sender cast=(System::Drawing::Color^),flags=gcobject
 */
public static final native int DrawingColor_ToArgb(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(DrawingContext^),flags=gcobject
 */
public static final native void DrawingContext_Close(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(DrawingContext^),flags=gcobject
 * @param Drawing cast=(System::Windows::Media::Drawing^),flags=gcobject
 */
public static final native void DrawingContext_DrawDrawing(int sender, int Drawing);
/**
 * @method flags=cpp
 * @param sender cast=(DrawingContext^),flags=gcobject
 * @param brush cast=(Brush^),flags=gcobject
 * @param pen cast=(Pen^),flags=gcobject
 * @param center cast=(Point),flags=gcobject
 */
public static final native void DrawingContext_DrawEllipse(int sender, int brush, int pen, int center, double radiusX, double radiusY);
/**
 * @method flags=cpp
 * @param sender cast=(DrawingContext^),flags=gcobject
 * @param imagesource cast=(ImageSource^),flags=gcobject
 * @param rect cast=(Rect),flags=gcobject
 */
public static final native void DrawingContext_DrawImage(int sender, int imagesource, int rect);
/**
 * @method flags=cpp
 * @param sender cast=(DrawingContext^),flags=gcobject
 * @param pen cast=(Pen^),flags=gcobject
 * @param point0 cast=(Point),flags=gcobject
 * @param point1 cast=(Point),flags=gcobject
 */
public static final native void DrawingContext_DrawLine(int sender, int pen, int point0, int point1);
/**
 * @method flags=cpp
 * @param sender cast=(DrawingContext^),flags=gcobject
 * @param brush cast=(Brush^),flags=gcobject
 * @param pen cast=(Pen^),flags=gcobject
 * @param geometry cast=(Geometry^),flags=gcobject
 */
public static final native void DrawingContext_DrawGeometry(int sender, int brush, int pen, int geometry);
/**
 * @method flags=cpp
 * @param sender cast=(DrawingContext^),flags=gcobject
 * @param brush cast=(Brush^),flags=gcobject
 * @param pen cast=(Pen^),flags=gcobject
 * @param rect cast=(Rect),flags=gcobject
 */
public static final native void DrawingContext_DrawRectangle(int sender, int brush, int pen, int rect);
/**
 * @method flags=cpp
 * @param sender cast=(DrawingContext^),flags=gcobject
 * @param brush cast=(Brush^),flags=gcobject
 * @param pen cast=(Pen^),flags=gcobject
 * @param rect cast=(Rect),flags=gcobject
 */
public static final native void DrawingContext_DrawRoundedRectangle(int sender, int brush, int pen, int rect, double radiusX, double radiusY);
/**
 * @method flags=cpp
 * @param sender cast=(DrawingContext^),flags=gcobject
 * @param formattedText cast=(FormattedText^),flags=gcobject
 * @param point cast=(Point),flags=gcobject
 */
public static final native void DrawingContext_DrawText(int sender, int formattedText, int point);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Drawing::FontFamily^),flags=gcobject
 */
public static final native int DrawingFontFamily_Name(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(DrawingContext^),flags=gcobject
 * @param transform cast=(Transform^),flags=gcobject
 */
public static final native void DrawingContext_PushTransform(int sender, int transform);
/**
 * @method flags=cpp
 * @param sender cast=(DrawingContext^),flags=gcobject
 * @param clipGeometry cast=(Geometry^),flags=gcobject
 */
public static final native void DrawingContext_PushClip(int sender, int clipGeometry);
/**
 * @method flags=cpp
 * @param sender cast=(DrawingContext^),flags=gcobject
 */
public static final native void DrawingContext_PushOpacity(int sender, double opacity);
/**
 * @method flags=cpp
 * @param sender cast=(DrawingContext^),flags=gcobject
 */
public static final native void DrawingContext_Pop(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(DrawingVisual^),flags=gcobject
 */
public static final native int DrawingVisual_Drawing(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(DrawingVisual^),flags=gcobject
 */
public static final native int DrawingVisual_RenderOpen(int sender);
/** @method accessor=DrawingVisual::typeid,flags=const gcobject */
public static final native int DrawingVisual_typeid();
/** @method accessor=EditingCommands::Backspace,flags=const gcobject */
public static final native int EditingCommands_Backspace();
/** @method accessor=EditingCommands::Delete,flags=const gcobject */
public static final native int EditingCommands_Delete();
/** @method accessor=EditingCommands::DeleteNextWord,flags=const gcobject */
public static final native int EditingCommands_DeleteNextWord();
/** @method accessor=EditingCommands::DeletePreviousWord,flags=const gcobject */
public static final native int EditingCommands_DeletePreviousWord();
/**
 * @method accessor=Environment::ExpandEnvironmentVariables,flags=gcobject
 * @param string cast=(String^),flags=gcobject
 */
public static final native int Environment_ExpandEnvironmentVariables(int string);
/**
 * @method flags=gcobject getter
 * @param sender cast=(ExecutedRoutedEventArgs^),flags=gcobject
 */
public static final native int ExecutedRoutedEventArgs_Command(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ExecutedRoutedEventArgs^),flags=gcobject
 */
public static final native void ExecutedRoutedEventArgs_Handled(int sender, boolean handled);
/**
 * @method flags=adder
 * @param sender cast=(Expander^),flags=gcobject
 * @param handler cast=(RoutedEventHandler^),flags=gcobject
 */
public static final native void Expander_Collapsed(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(Expander^),flags=gcobject
 * @param handler cast=(RoutedEventHandler^),flags=gcobject
 */
public static final native void Expander_Expanded(int sender, int handler);
/**
 * @method flags=getter
 * @param sender cast=(Expander^),flags=gcobject
 */
public static final native boolean Expander_IsExpanded(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Expander^),flags=gcobject
 */
public static final native void Expander_IsExpanded(int sender, boolean value);
/**
 * @method flags=setter
 * @param sender cast=(FileDialog^),flags=gcobject
 * @param value cast=(String^),flags=gcobject
 */
public static final native void FileDialog_FileName(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(FileDialog^),flags=gcobject
 */
public static final native int FileDialog_FileNames(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FileDialog^),flags=gcobject
 * @param value cast=(String^),flags=gcobject
 */
public static final native void FileDialog_Filter(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(FileDialog^),flags=gcobject
 */
public static final native int FileDialog_FilterIndex(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FileDialog^),flags=gcobject
 */
public static final native void FileDialog_FilterIndex(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(FileDialog^),flags=gcobject
 * @param value cast=(String^),flags=gcobject
 */
public static final native void FileDialog_InitialDirectory(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(FileDialog^),flags=gcobject
 * @param value cast=(String^),flags=gcobject
 */
public static final native void FileDialog_Title(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::IO::FileInfo^),flags=gcobject
 */
public static final native int FileInfo_DirectoryName(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::IO::FileInfo^),flags=gcobject
 */
public static final native int FileInfo_Name(int sender);
/**
 * @method accessor=System::IO::File::Exists
 * @param sender cast=(String^),flags=gcobject
 */
public static final native boolean File_Exists(int sender);
/**
 * @method accessor=System::IO::File::ReadAllText,flags=gcobject
 * @param sender cast=(String^),flags=gcobject
 */
public static final native int File_ReadAllText(int sender);
/**
 * @method accessor=FocusManager::GetFocusScope,flags=gcobject
 * @param element cast=(DependencyObject^),flags=gcobject
 */
public static final native int FocusManager_GetFocusScope(int element);
/**
 * @method accessor=FocusManager::GetFocusedElement,flags=gcobject
 * @param element cast=(DependencyObject^),flags=gcobject
 */
public static final native int FocusManager_GetFocusedElement(int element);
/**
 * @method flags=setter
 * @param sender cast=(System::Windows::Forms::FolderBrowserDialog^),flags=gcobject
 * @param value cast=(String^),flags=gcobject
 */
public static final native void FolderBrowserDialog_Description(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Windows::Forms::FolderBrowserDialog^),flags=gcobject
 */
public static final native int FolderBrowserDialog_SelectedPath(int sender);
/**
 * @method flags=setter
 * @param sender cast=(System::Windows::Forms::FolderBrowserDialog^),flags=gcobject
 * @param value cast=(String^),flags=gcobject
 */
public static final native void FolderBrowserDialog_SelectedPath(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Drawing::Font^),flags=gcobject
 */
public static final native int Font_FontFamily(int sender);
/**
 * @method flags=getter
 * @param sender cast=(System::Drawing::Font^),flags=gcobject
 */
public static final native int Font_Size(int sender);
/**
 * @method flags=getter
 * @param sender cast=(System::Drawing::Font^),flags=gcobject
 */
public static final native int Font_Style(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Windows::Forms::FontDialog^),flags=gcobject
 */
public static final native int FontDialog_Color(int sender);
/**
 * @method flags=setter
 * @param sender cast=(System::Windows::Forms::FontDialog^),flags=gcobject
 * @param value cast=(System::Drawing::Color),flags=gcobject
 */
public static final native void FontDialog_Color(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Windows::Forms::FontDialog^),flags=gcobject
 */
public static final native int FontDialog_Font(int sender);
/**
 * @method flags=setter
 * @param sender cast=(System::Windows::Forms::FontDialog^),flags=gcobject
 * @param value cast=(System::Drawing::Font^),flags=gcobject
 */
public static final native void FontDialog_Font(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(System::Windows::Forms::FontDialog^),flags=gcobject
 */
public static final native void FontDialog_ShowColor (int sender, boolean value);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(FontFamily^),flags=gcobject
 */
public static final native int FontFamily_GetTypefaces(int sender);
/**
 * @method flags=getter
 * @param sender cast=(FontFamily^),flags=gcobject
 */
public static final native double FontFamily_LineSpacing(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(FontFamily^),flags=gcobject
 */
public static final native int FontFamily_Source(int sender);
/** @method accessor=FontStyles::Italic,flags=const gcobject */
public static final native int FontStyles_Italic();
/** @method accessor=FontStyles::Normal,flags=const gcobject */
public static final native int FontStyles_Normal();
/** @method accessor=FontStyles::Oblique,flags=const gcobject */
public static final native int FontStyles_Oblique();
/** @method accessor=FontWeight::FromOpenTypeWeight,flags=gcobject */
public static final native int FontWeight_FromOpenTypeWeight(int weight);
/**
 * @method flags=cpp
 * @param sender cast=(FontWeight^),flags=gcobject
 */
public static final native int FontWeight_ToOpenTypeWeight(int sender);
/** @method accessor=FontWeights::Bold,flags=const gcobject */
public static final native int FontWeights_Bold();
/** @method accessor=FontWeights::Normal,flags=const gcobject */
public static final native int FontWeights_Normal();
/** @method accessor=FontStretches::Normal,flags=const gcobject */
public static final native int FontStretches_Normal();
/** @method accessor=FontStretch::FromOpenTypeStretch,flags=gcobject */
public static final native int FontStretch_FromOpenTypeStretch(int stretch);
/**
 * @method flags=cpp
 * @param sender cast=(FontStretch^),flags=gcobject
 */
public static final native int FontStretch_ToOpenTypeStretch(int sender);
/**
 * @method accessor=Fonts::GetTypefaces,flags=gcobject
 * @param uri cast=(String^),flags=gcobject
 */
public static final native int Fonts_GetTypefaces(int uri);
/** @method accessor=Fonts::SystemTypefaces,flags=const gcobject */
public static final native int Fonts_SystemTypefaces();
/**
 * @method flags=getter
 * @param sender cast=(FormattedText^),flags=gcobject
 */
public static final native double FormattedText_Baseline(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(FormattedText^),flags=gcobject
 * @param origin cast=(Point),flags=gcobject
 */
public static final native int FormattedText_BuildGeometry(int sender, int origin);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(FormattedText^),flags=gcobject
 * @param origin cast=(Point),flags=gcobject
 */
public static final native int FormattedText_BuildHighlightGeometry(int sender, int origin);
/**
 * @method flags=getter
 * @param sender cast=(FormattedText^),flags=gcobject
 */
public static final native double FormattedText_Height(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(FormattedText^),flags=gcobject
 * @param decorations cast=(TextDecorationCollection^),flags=gcobject
 */
public static final native void FormattedText_SetTextDecorations(int sender, int decorations, int startIndex, int count);
/**
 * @method flags=getter
 * @param sender cast=(FormattedText^),flags=gcobject
 */
public static final native double FormattedText_WidthIncludingTrailingWhitespace(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(System::Windows::Forms::CommonDialog^),flags=gcobject
 */
public static final native int FormsCommonDialog_ShowDialog(int sender);
/**
 * @method flags=getter
 * @param sender cast=(System::Windows::Forms::MouseEventArgs^),flags=gcobject
 */
public static final native int FormsMouseEventArgs_Button(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Frame^),flags=gcobject
 */
public static final native boolean Frame_CanGoBack(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Frame^),flags=gcobject
 */
public static final native boolean Frame_CanGoForward(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Frame^),flags=gcobject
 */
public static final native int Frame_CurrentSource(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Frame^),flags=gcobject
 */
public static final native int Frame_Source(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Frame^),flags=gcobject
 * @param uri cast=(Uri^),flags=gcobject
 */
public static final native void Frame_Source(int sender, int uri);
/**
 * @method flags=cpp
 * @param sender cast=(Frame^),flags=gcobject
 * @param uri cast=(Uri^),flags=gcobject
 */
public static final native boolean Frame_Navigate(int sender, int uri);
/**
 * @method flags=setter
 * @param sender cast=(Frame^),flags=gcobject
 * @param visiblity cast=(System::Windows::Navigation::NavigationUIVisibility)
 */
public static final native void Frame_NavigationUIVisibility(int sender, int visiblity);
/**
 * @method flags=cpp
 * @param sender cast=(Frame^),flags=gcobject
 */
public static final native void Frame_GoBack(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(Frame^),flags=gcobject
 */
public static final native void Frame_GoForward(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(Frame^),flags=gcobject
 */
public static final native void Frame_Refresh(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(Frame^),flags=gcobject
 */
public static final native void Frame_StopLoading(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(FrameworkContentElement^),flags=gcobject
 */
public static final native int FrameworkContentElement_Parent(int sender);
/**
 * @method flags=getter
 * @param sender cast=(FrameworkContentElement^),flags=gcobject
 */
public static final native int FrameworkContentElement_Tag(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkContentElement^),flags=gcobject
 */
public static final native void FrameworkContentElement_Tag(int sender, int value);
/** @method accessor=FrameworkContentElement::typeid,flags=const gcobject */
public static final native int FrameworkContentElement_typeid();
/**
 * @method flags=cpp
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native void FrameworkElement_BeginInit(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native void FrameworkElement_BringIntoView(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param value cast=(ContextMenu^),flags=gcobject
 */
public static final native void FrameworkElement_ContextMenu(int sender, int value);
/**
 * @method flags=adder
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param handler cast=(ContextMenuEventHandler^),flags=gcobject
 */
public static final native void FrameworkElement_ContextMenuClosing(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param handler cast=(ContextMenuEventHandler^),flags=gcobject
 */
public static final native void FrameworkElement_ContextMenuOpening(int sender, int handler);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param cursor cast=(Cursor^),flags=gcobject
 */
public static final native void FrameworkElement_Cursor(int sender, int cursor);
/** @method accessor=FrameworkElement::CursorProperty,flags=const gcobject */
public static final native int FrameworkElement_CursorProperty();
/** @method accessor=FrameworkElement::ActualHeightProperty,flags=const gcobject */
public static final native int FrameworkElement_ActualHeightProperty();
/** @method accessor=FrameworkElement::ActualWidthProperty,flags=const gcobject */
public static final native int FrameworkElement_ActualWidthProperty();
/**
 * @method flags=cpp gcobject
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param key cast=(Object^),flags=gcobject
 */
public static final native int FrameworkElement_FindResource(int sender, int key);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param style cast=(Style^),flags=gcobject
 */
public static final native void FrameworkElement_FocusVisualStyle(int sender, int style);
/**
 * @method flags=getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native int FrameworkElement_FlowDirection(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param value cast=(FlowDirection)
 */
public static final native void FrameworkElement_FlowDirection(int sender, int value);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param dp cast=(DependencyProperty^),flags=gcobject
 */
public static final native int FrameworkElement_GetBindingExpression(int sender, int dp);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param value cast=(HorizontalAlignment)
 */
public static final native void FrameworkElement_HorizontalAlignment(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native boolean FrameworkElement_IsLoaded(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param value cast=(Transform^),flags=gcobject
 */
public static final native void FrameworkElement_LayoutTransform(int sender, int value);
/**
 * @method flags=adder
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param handler cast=(RoutedEventHandler^),flags=gcobject
 */
public static final native void FrameworkElement_Loaded(int sender, int handler);
/**
 * @method flags=gcobject getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native int FrameworkElement_Margin(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param value cast=(Thickness),flags=gcobject
 */
public static final native void FrameworkElement_Margin(int sender, int value);
/** @method accessor=FrameworkElement::MarginProperty,flags=const gcobject */
public static final native int FrameworkElement_MarginProperty();
/**
 * @method flags=gcobject getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native int FrameworkElement_Name(int sender);
/** @method accessor=FrameworkElement::NameProperty,flags=const gcobject */
public static final native int FrameworkElement_NameProperty();
/**
 * @method flags=gcobject getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native int FrameworkElement_Parent(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native int FrameworkElement_RenderTransform(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param value cast=(Transform^),flags=gcobject
 */
public static final native void FrameworkElement_RenderTransform(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native int FrameworkElement_Style(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param value cast=(Style^),flags=gcobject
 */
public static final native void FrameworkElement_Style(int sender, int value);
/** @method accessor=FrameworkElement::StyleProperty,flags=const gcobject */
public static final native int FrameworkElement_StyleProperty();
/**
 * @method flags=gcobject getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native int FrameworkElement_Tag(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param tag cast=(Object^),flags=gcobject
 */
public static final native void FrameworkElement_Tag(int sender, int tag);
/** @method accessor=FrameworkElement::TagProperty,flags=const gcobject */
public static final native int FrameworkElement_TagProperty();
/** @method accessor=FrameworkElement::typeid,flags=const gcobject */
public static final native int FrameworkElement_typeid();
/**
 * @method flags=gcobject getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native int FrameworkElement_ToolTip(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void FrameworkElement_ToolTip(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native double FrameworkElement_MaxHeight(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native void FrameworkElement_MaxHeight(int sender, double height);
/**
 * @method flags=getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native double FrameworkElement_MaxWidth(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native void FrameworkElement_MaxWidth(int sender, double width);
/**
 * @method flags=getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native double FrameworkElement_MinHeight(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native void FrameworkElement_MinHeight(int sender, double height);
/**
 * @method flags=getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native double FrameworkElement_MinWidth(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native void FrameworkElement_MinWidth(int sender, double width);
/**
 * @method flags=getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native double FrameworkElement_Height(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native void FrameworkElement_Height(int sender, double height);
/**
 * @method flags=getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native double FrameworkElement_Width(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native void FrameworkElement_Width(int sender, double width);
/**
 * @method flags=getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native double FrameworkElement_ActualWidth(int sender);
/**
 * @method flags=getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native double FrameworkElement_ActualHeight(int sender);
/** @method accessor=FrameworkElement::WidthProperty,flags=const gcobject */
public static final native int FrameworkElement_WidthProperty();
/** @method accessor=FrameworkElement::HeightProperty,flags=const gcobject */
public static final native int FrameworkElement_HeightProperty();
/**
 * @method flags=adder
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param handler cast=(SizeChangedEventHandler^),flags=gcobject
 */
public static final native void FrameworkElement_SizeChanged(int sender, int handler);
/** @method accessor=FrameworkElement::SizeChangedEvent,flags=const gcobject */
public static final native int FrameworkElement_SizeChangedEvent();
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param str cast=(VerticalAlignment)
 */
public static final native void FrameworkElement_VerticalAlignment(int sender, int str);
/** @method accessor=FrameworkElement::VerticalAlignmentProperty,flags=const gcobject */
public static final native int FrameworkElement_VerticalAlignmentProperty();
/**
 * @method flags=cpp
 * @param sender cast=(FrameworkElementFactory^),flags=gcobject
 * @param value cast=(FrameworkElementFactory^),flags=gcobject
 */
public static final native void FrameworkElementFactory_AppendChild(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(FrameworkElementFactory^),flags=gcobject
 * @param dp cast=(DependencyProperty^),flags=gcobject
 * @param binding cast=(BindingBase^),flags=gcobject
 */
public static final native void FrameworkElementFactory_SetBinding(int sender, int dp, int binding);
/**
 * @method flags=cpp
 * @param sender cast=(FrameworkElementFactory^),flags=gcobject
 * @param dp cast=(DependencyProperty^),flags=gcobject
 * @param value cast=(Boolean)
 */
public static final native void FrameworkElementFactory_SetValue(int sender, int dp, boolean value);
/**
 * @method flags=cpp
 * @param sender cast=(FrameworkElementFactory^),flags=gcobject
 * @param dp cast=(DependencyProperty^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void FrameworkElementFactory_SetValue(int sender, int dp, int value);
/**
 * @method accessor=SetValue,flags=cpp
 * @param sender cast=(FrameworkElementFactory^),flags=gcobject
 * @param dp cast=(DependencyProperty^),flags=gcobject
 * @param value cast=(Dock)
 */
public static final native void FrameworkElementFactory_SetValueDock(int sender, int dp, int value);
/**
 * @method accessor=SetValue,flags=cpp
 * @param sender cast=(FrameworkElementFactory^),flags=gcobject
 * @param dp cast=(DependencyProperty^),flags=gcobject
 */
public static final native void FrameworkElementFactory_SetValueInt(int sender, int dp, int value);
/**
 * @method accessor=SetValue,flags=cpp
 * @param sender cast=(FrameworkElementFactory^),flags=gcobject
 * @param dp cast=(DependencyProperty^),flags=gcobject
 * @param value cast=(Stretch)
 */
public static final native void FrameworkElementFactory_SetValueStretch(int sender, int dp, int value);
/**
 * @method accessor=SetValue,flags=cpp
 * @param sender cast=(FrameworkElementFactory^),flags=gcobject
 * @param dp cast=(DependencyProperty^),flags=gcobject
 * @param value cast=(Orientation)
 */
public static final native void FrameworkElementFactory_SetValueOrientation(int sender, int dp, int value);
/**
 * @method accessor=SetValue,flags=cpp
 * @param sender cast=(FrameworkElementFactory^),flags=gcobject
 * @param dp cast=(DependencyProperty^),flags=gcobject
 * @param value cast=(VerticalAlignment)
 */
public static final native void FrameworkElementFactory_SetValueVerticalAlignment(int sender, int dp, int value);
/**
 * @method accessor=SetValue,flags=cpp
 * @param sender cast=(FrameworkElementFactory^),flags=gcobject
 * @param dp cast=(DependencyProperty^),flags=gcobject
 * @param value cast=(Visibility)
 */
public static final native void FrameworkElementFactory_SetValueVisibility(int sender, int dp, byte value);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(FrameworkTemplate^),flags=gcobject
 * @param name cast=(String^),flags=gcobject
 * @param parent cast=(FrameworkElement^),flags=gcobject
 */
public static final native int FrameworkTemplate_FindName(int sender, int name, int parent);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkTemplate^),flags=gcobject
 * @param value cast=(FrameworkElementFactory^),flags=gcobject
 */
public static final native void FrameworkTemplate_VisualTree(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(Freezable^),flags=gcobject
 */
public static final native boolean Freezable_CanFreeze(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(Freezable^),flags=gcobject
 */
public static final native int Freezable_Clone(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(Freezable^),flags=gcobject
 */
public static final native void Freezable_Freeze(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(GeometryCollection^),flags=gcobject
 */
public static final native void GeometryCollection_Clear(int sender);
/**
 * @method flags=getter
 * @param sender cast=(GeometryCollection^),flags=gcobject
 */
public static final native int GeometryCollection_Count(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(GeometryCollection^),flags=gcobject
 * @param geometry cast=(Geometry^),flags=gcobject
 */
public static final native void GeometryCollection_Add(int sender, int geometry);
/**
 * @method flags=cpp
 * @param sender cast=(GeometryCollection^),flags=gcobject
 * @param geometry cast=(Geometry^),flags=gcobject
 */
public static final native void GeometryCollection_Remove(int sender, int geometry);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(Geometry^),flags=gcobject
 */
public static final native int Geometry_Clone(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Geometry^),flags=gcobject
 */
public static final native int Geometry_Bounds(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(Geometry^),flags=gcobject
 */
public static final native int Geometry_GetFlattenedPathGeometry(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(Geometry^),flags=gcobject
 * @param type cast=(ToleranceType)
 */
public static final native int Geometry_GetFlattenedPathGeometry(int sender, double tolerance, int type);
/**
 * @method flags=cpp
 * @param sender cast=(Geometry^),flags=gcobject
 */
public static final native boolean Geometry_IsEmpty(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(Geometry^),flags=gcobject
 * @param point cast=(Point),flags=gcobject
 */
public static final native boolean Geometry_FillContains(int sender, int point);
/**
 * @method flags=cpp
 * @param sender cast=(Geometry^),flags=gcobject
 * @param geometry cast=(Geometry^),flags=gcobject
 */
public static final native int Geometry_FillContainsWithDetail(int sender, int geometry);
/**
 * @method flags=cpp
 * @param sender cast=(Geometry^),flags=gcobject
 * @param pen cast=(Pen^),flags=gcobject
 * @param hitPoint cast=(Point),flags=gcobject
 */
public static final native boolean Geometry_StrokeContains(int sender, int pen, int hitPoint);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Geometry^),flags=gcobject
 */
public static final native int Geometry_Transform(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Geometry^),flags=gcobject
 * @param transform cast=(Transform^),flags=gcobject
 */
public static final native void Geometry_Transform(int sender, int transform);
/**
 * @method flags=gcobject getter
 * @param sender cast=(GeometryGroup^),flags=gcobject
 */
public static final native int GeometryGroup_Children(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(GeometryGroup^),flags=gcobject
 */
public static final native int GeometryGroup_Children(int sender, int index);
/**
 * @method flags=getter
 * @param sender cast=(GiveFeedbackEventArgs^),flags=gcobject
 */
public static final native int GiveFeedbackEventArgs_Effects (int sender);
/**
 * @method flags=getter
 * @param sender cast=(GlyphRun^),flags=gcobject
 */
public static final native int GlyphRun_BidiLevel(int sender);
/**
 * @method flags=setter
 * @param sender cast=(GradientBrush^),flags=gcobject
 * @param mode cast=(BrushMappingMode)
 */
public static final native void GradientBrush_MappingMode(int sender, int mode);
/**
 * @method flags=setter
 * @param sender cast=(GradientBrush^),flags=gcobject
 * @param method cast=(GradientSpreadMethod)
 */
public static final native void GradientBrush_SpreadMethod(int sender, int method);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Grid^),flags=gcobject
 */
public static final native int Grid_ColumnDefinitions(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Grid^),flags=gcobject
 */
public static final native int Grid_RowDefinitions(int sender);
/**
 * @method accessor=Grid::SetColumn
 * @param element cast=(UIElement^),flags=gcobject
 */
public static final native void Grid_SetColumn(int element, int index);
/**
 * @method accessor=Grid::SetColumnSpan
 * @param element cast=(UIElement^),flags=gcobject
 */
public static final native void Grid_SetColumnSpan(int element, int value);
/**
 * @method accessor=Grid::SetRow
 * @param element cast=(UIElement^),flags=gcobject
 */
public static final native void Grid_SetRow(int element, int index);
/**
 * @method accessor=Grid::SetRowSpan
 * @param element cast=(UIElement^),flags=gcobject
 */
public static final native void Grid_SetRowSpan(int element, int value);
/**
 * @method flags=setter
 * @param sender cast=(GridView^),flags=gcobject
 * @param style cast=(Style^),flags=gcobject
 */
public static final native void GridView_ColumnHeaderContainerStyle(int sender, int style);
/**
 * @method flags=gcobject getter
 * @param sender cast=(GridView^),flags=gcobject
 */
public static final native int GridView_Columns(int sender);
/**
 * @method flags=setter
 * @param sender cast=(GridView^),flags=gcobject
 */
public static final native void GridView_AllowsColumnReorder(int sender, boolean value);
/**
 * @method flags=getter
 * @param sender cast=(GridViewColumn^),flags=gcobject
 */
public static final native double GridViewColumn_ActualWidth(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(GridViewColumn^),flags=gcobject
 */
public static final native int GridViewColumn_CellTemplate(int sender);
/**
 * @method flags=setter
 * @param sender cast=(GridViewColumn^),flags=gcobject
 * @param value cast=(DataTemplate^),flags=gcobject
 */
public static final native void GridViewColumn_CellTemplate(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(GridViewColumn^),flags=gcobject
 */
public static final native int GridViewColumn_Header(int sender);
/**
 * @method flags=setter
 * @param sender cast=(GridViewColumn^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void GridViewColumn_Header(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(GridViewColumn^),flags=gcobject
 */
public static final native int GridViewColumn_HeaderTemplate(int sender);
/**
 * @method flags=setter
 * @param sender cast=(GridViewColumn^),flags=gcobject
 * @param value cast=(DataTemplate^),flags=gcobject
 */
public static final native void GridViewColumn_HeaderTemplate(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(GridViewColumn^),flags=gcobject
 */
public static final native double GridViewColumn_Width(int sender);
/**
 * @method flags=setter
 * @param sender cast=(GridViewColumn^),flags=gcobject
 */
public static final native void GridViewColumn_Width(int sender, double value);
/** @method accessor=GridViewColumn::WidthProperty,flags=const gcobject */
public static final native int GridViewColumn_WidthProperty();
/**
 * @method flags=gcobject getter
 * @param sender cast=(GridViewColumnCollection^),flags=gcobject
 */
public static final native int GridViewColumnCollection_default(int sender, int index);
/**
 * @method flags=cpp
 * @param sender cast=(GridViewColumnCollection^),flags=gcobject
 */
public static final native void GridViewColumnCollection_Clear(int sender);
/**
 * @method flags=getter
 * @param sender cast=(GridViewColumnCollection^),flags=gcobject
 */
public static final native int GridViewColumnCollection_Count(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(GridViewColumnCollection ^),flags=gcobject
 * @param item cast=(GridViewColumn^),flags=gcobject
 */
public static final native int GridViewColumnCollection_IndexOf(int sender, int item);
/**
 * @method flags=cpp
 * @param sender cast=(GridViewColumnCollection^),flags=gcobject
 * @param value cast=(GridViewColumn^),flags=gcobject
 */
public static final native void GridViewColumnCollection_Insert(int sender, int index, int value);
/**
 * @method flags=cpp
 * @param sender cast=(GridViewColumnCollection^),flags=gcobject
 * @param value cast=(GridViewColumn^),flags=gcobject
 */
public static final native boolean GridViewColumnCollection_Remove(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(GridViewColumnHeader^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void GridViewColumnHeader_Content(int sender, int value);
/** @method accessor=GridViewRowPresenterBase::ColumnsProperty,flags=const gcobject */
public static final native int GridViewRowPresenterBase_ColumnsProperty();
/** @method accessor=GridViewHeaderRowPresenter::typeid,flags=const gcobject */
public static final native int GridViewHeaderRowPresenter_typeid();
/**
 * @method flags=setter
 * @param sender cast=(GridViewRowPresenter^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void GridViewRowPresenter_Content(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(GridViewRowPresenter^),flags=gcobject
 */
public static final native int GridViewRowPresenter_Content(int sender);
/** @method accessor=GridViewRowPresenter::typeid,flags=const gcobject */
public static final native int GridViewRowPresenter_typeid();
/**
 * @method flags=setter
 * @param sender cast=(GridViewRowPresenterBase^),flags=gcobject
 * @param value cast=(GridViewColumnCollection^),flags=gcobject
 */
public static final native void GridViewRowPresenterBase_Columns(int sender, int value);
/** @method flags=no_gen */
public static final native int GCHandle_Alloc(int sender);
/** @method flags=no_gen */
public static final native void GCHandle_Free(int sender);
/** @method flags=no_gen */
public static final native void GCHandle_Dump();
/** @method flags=no_gen */
public static final native int GCHandle_ToHandle(int gchandle);
/**
 * @method flags=gcobject getter
 * @param sender cast=(HeaderedContentControl^),flags=gcobject
 */
public static final native int HeaderedContentControl_Header(int sender);
/**
 * @method flags=setter
 * @param sender cast=(HeaderedContentControl^),flags=gcobject
 * @param header cast=(Object^),flags=gcobject
 */
public static final native void HeaderedContentControl_Header(int sender, int header);
/**
 * @method flags=gcobject getter
 * @param sender cast=(HeaderedItemsControl^),flags=gcobject
 */
public static final native int HeaderedItemsControl_Header(int sender);
/**
 * @method flags=setter
 * @param sender cast=(HeaderedItemsControl^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void HeaderedItemsControl_Header(int sender, int value);  
/** @method accessor=HeaderedItemsControl::HeaderTemplateProperty,flags=const gcobject */
public static final native int HeaderedItemsControl_HeaderTemplateProperty();
/**
 * @method flags=cpp gcobject
 * @param sender cast=(System::Windows::Forms::HtmlDocument^),flags=gcobject
 * @param string cast=(String^),flags=gcobject
 */
public static final native int HtmlDocument_InvokeScript(int sender, int string);
/**
 * @method flags=gcobject getter
 * @param sender cast=(HwndSource^),flags=gcobject
 */
public static final native int HwndSource_Handle(int sender);
/**
 * @method flags=adder
 * @param sender cast=(Hyperlink^),flags=gcobject
 * @param handler cast=(RoutedEventHandler^),flags=gcobject
 */
public static final native void Hyperlink_Click(int sender, int handler);
/**
 * @method flags=getter
 * @param sender cast=(ICollection^),flags=gcobject
 */
public static final native int ICollection_Count(int sender);
/**
 * @method accessor=System::Drawing::Icon::FromHandle,flags=no_gen gcobject
 * @param hIcon cast=(IntPtr),flags=gcobject
 */
public static final native int Icon_FromHandle(int hIcon);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(IEnumerable ^),flags=gcobject
 */
public static final native int IEnumerable_GetEnumerator(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(IEnumerator^),flags=gcobject
 */
public static final native int IEnumerator_Current(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(IEnumerator^),flags=gcobject
 */
public static final native boolean IEnumerator_MoveNext(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(IList^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void IList_Add(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(IList^),flags=gcobject
 */
public static final native void IList_Clear(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(IList^),flags=gcobject
 */
public static final native int IList_default(int sender, int index);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(IList^),flags=gcobject
 */
public static final native int IList_GetEnumerator(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(IList^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native int IList_IndexOf(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(IList^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void IList_Insert(int sender, int index, int value);
/**
 * @method flags=cpp
 * @param sender cast=(IList^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void IList_Remove(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(IndexedGlyphRun^),flags=gcobject
 */
public static final native int IndexedGlyphRun_TextSourceCharacterIndex(int sender);
/**
 * @method flags=getter
 * @param sender cast=(IndexedGlyphRun^),flags=gcobject
 */
public static final native int IndexedGlyphRun_TextSourceLength(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(IndexedGlyphRun^),flags=gcobject
 */
public static final native int IndexedGlyphRun_GlyphRun(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(IEnumerable^),flags=gcobject
 */
public static final native int IndexedGlyphRunCollection_GetEnumerator(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(IEnumerator^),flags=gcobject
 */
public static final native int IndexedGlyphRunCollection_Current(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(InlineCollection^),flags=gcobject
 * @param value cast=(Inline^),flags=gcobject
 */
public static final native void InlineCollection_Add(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(InlineCollection^),flags=gcobject
 */
public static final native void InlineCollection_Clear(int sender);
/**
 * @method flags=getter
 * @param sender cast=(InputEventArgs^),flags=gcobject
 */
public static final native int InputEventArgs_Timestamp(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Image^),flags=gcobject
 */
public static final native int Image_Source(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Image^),flags=gcobject
 * @param imageSource cast=(ImageSource^),flags=gcobject
 */
public static final native void Image_Source(int sender, int imageSource);
/** @method accessor=Image::SourceProperty,flags=const gcobject */
public static final native int Image_SourceProperty();
/** @method accessor=Image::StretchProperty,flags=const gcobject */
public static final native int Image_StretchProperty();
/**
 * @method flags=setter
 * @param sender cast=(Image^),flags=gcobject
 * @param stretch cast=(Stretch)
 */
public static final native void Image_Stretch(int sender, int stretch);
/** @method accessor=Image::typeid,flags=const gcobject */
public static final native int Image_typeid();
/**
 * @method accessor=System::Windows::Interop::Imaging::CreateBitmapSourceFromHIcon,flags=gcobject
 * @param hIcon cast=(IntPtr)
 * @param sourceRect cast=(Int32Rect),flags=gcobject
 * @param sizeOptions cast=(BitmapSizeOptions^),flags=gcobject
 */
public static final native int Imaging_CreateBitmapSourceFromHIcon(int hIcon, int sourceRect, int sizeOptions);
/** @method accessor=ImageSource::typeid,flags=const gcobject */
public static final native int ImageSource_typeid();
/**
 * @method flags=cpp
 * @param value cast=(IntPtr^),flags=gcobject
 */
public static final native int IntPtr_ToInt32 (int value);
/**
 * @method flags=cpp
 * @param sender cast=(ItemCollection^),flags=gcobject
 * @param item cast=(Object^),flags=gcobject
 */
public static final native void ItemCollection_Add(int sender, int item);
/**
 * @method flags=cpp
 * @param sender cast=(ItemCollection^),flags=gcobject
 */
public static final native void ItemCollection_Clear(int sender);
/**
 * @method flags=getter
 * @param sender cast=(ItemCollection^),flags=gcobject
 */
public static final native int ItemCollection_Count(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(ItemCollection^),flags=gcobject
 */
public static final native int ItemCollection_CurrentItem(int sender);
/**
 * @method flags=getter
 * @param sender cast=(ItemCollection^),flags=gcobject
 */
public static final native int ItemCollection_CurrentPosition(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(ItemCollection^),flags=gcobject
 */
public static final native int ItemCollection_GetItemAt(int sender, int index);
/**
 * @method flags=cpp
 * @param sender cast=(ItemCollection^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native int ItemCollection_IndexOf(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(ItemCollection^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void ItemCollection_Insert(int sender, int index, int value);
/**
 * @method flags=cpp
 * @param sender cast=(ItemCollection^),flags=gcobject
 * @param item cast=(Object^),flags=gcobject
 */
public static final native void ItemCollection_Remove(int sender, int item);
/**
 * @method flags=cpp
 * @param sender cast=(ItemCollection^),flags=gcobject
 */
public static final native void ItemCollection_RemoveAt(int sender, int index);
/**
 * @method flags=getter
 * @param sender cast=(ItemsControl^),flags=gcobject
 */
public static final native boolean ItemsControl_HasItems(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(ItemsControl^),flags=gcobject
 */
public static final native int ItemsControl_Items(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ItemsControl^),flags=gcobject
 * @param value cast=(IEnumerable^),flags=gcobject
 */
public static final native void ItemsControl_ItemsSource(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(ItemsControl^),flags=gcobject
 */
public static final native int ItemsControl_ItemTemplate(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ItemsControl^),flags=gcobject
 * @param value cast=(DataTemplate^),flags=gcobject
 */
public static final native void ItemsControl_ItemTemplate(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(ItemsControl^),flags=gcobject
 */
public static final native void ItemsControl_IsTextSearchEnabled(int sender, boolean value);
/** @method accessor=ItemsPresenter::typeid,flags=const gcobject */
public static final native int ItemsPresenter_typeid();
/**
 * @method accessor=KeyInterop::VirtualKeyFromKey
 * @param key cast=(Key)
 */
public static final native int KeyInterop_VirtualKeyFromKey(int key);
/** @method accessor=Keyboard::FocusedElement,flags=const gcobject */
public static final native int Keyboard_FocusedElement();
/**
 * @method accessor=Keyboard::Focus,flags=gcobject
 * @param element cast=(IInputElement^),flags=gcobject
 */
public static final native int Keyboard_Focus(int element);
/** @method accessor=Keyboard::Modifiers,flags=const */
public static final native int Keyboard_Modifiers();
/**
 * @method accessor=KeyboardNavigation::GetIsTabStop
 * @param element cast=(DependencyObject^),flags=gcobject
 */
public static final native boolean KeyboardNavigation_GetIsTabStop(int element);
/**
 * @method accessor=KeyboardNavigation::SetIsTabStop
 * @param element cast=(DependencyObject^),flags=gcobject
 */
public static final native void KeyboardNavigation_SetIsTabStop(int element, boolean istabstop);
/**
 * @method accessor=KeyboardNavigation::SetDirectionalNavigation
 * @param element cast=(DependencyObject^),flags=gcobject
 * @param mode cast=(KeyboardNavigationMode)
 */
public static final native void KeyboardNavigation_SetDirectionalNavigation(int element, int mode);
/**
 * @method accessor=KeyboardNavigation::SetTabNavigation
 * @param element cast=(DependencyObject^),flags=gcobject
 * @param mode cast=(KeyboardNavigationMode)
 */
public static final native void KeyboardNavigation_SetTabNavigation(int element, int mode);
/**
 * @method accessor=KeyboardNavigation::SetControlTabNavigation
 * @param element cast=(DependencyObject^),flags=gcobject
 * @param mode cast=(KeyboardNavigationMode)
 */
public static final native void KeyboardNavigation_SetControlTabNavigation(int element, int mode);
/**
 * @method flags=getter
 * @param sender cast=(KeyboardDevice^),flags=gcobject
 */
public static final native int KeyboardDevice_Modifiers(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(KeyboardEventArgs^),flags=gcobject
 */
public static final native int KeyboardEventArgs_KeyboardDevice(int sender);
/**
 * @method flags=getter
 * @param sender cast=(KeyEventArgs^),flags=gcobject
 */
public static final native boolean KeyEventArgs_IsDown(int sender);
/**
 * @method flags=getter
 * @param sender cast=(KeyEventArgs^),flags=gcobject
 */
public static final native boolean KeyEventArgs_IsRepeat(int sender);
/**
 * @method flags=getter
 * @param sender cast=(KeyEventArgs^),flags=gcobject
 */
public static final native boolean KeyEventArgs_IsToggled(int sender);
/**
 * @method flags=getter
 * @param sender cast=(KeyEventArgs^),flags=gcobject
 */
public static final native int KeyEventArgs_Key(int sender);
/**
 * @method flags=getter
 * @param sender cast=(KeyEventArgs^),flags=gcobject
 */
public static final native int KeyEventArgs_SystemKey(int sender);
/** @method accessor=KeyTime::Uniform,flags=const gcobject */
public static final native int KeyTime_Uniform();
/**
 * @method flags=getter
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native boolean Matrix_IsIdentity(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native void Matrix_Invert(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native double Matrix_M11(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native double Matrix_M12(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native double Matrix_M21(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native double Matrix_M22(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native double Matrix_OffsetX(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native double Matrix_OffsetY(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native void Matrix_M11(int sender, double value);
/**
 * @method flags=setter
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native void Matrix_M12(int sender, double value);
/**
 * @method flags=setter
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native void Matrix_M21(int sender, double value);
/**
 * @method flags=setter
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native void Matrix_M22(int sender, double value);
/**
 * @method flags=setter
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native void Matrix_OffsetX(int sender, double value);
/**
 * @method flags=setter
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native void Matrix_OffsetY(int sender, double value);
/**
 * @method accessor=Matrix::Multiply,flags=gcobject
 * @param m1 cast=(Matrix),flags=gcobject
 * @param m2 cast=(Matrix),flags=gcobject
 */
public static final native int Matrix_Multiply(int m1, int m2);
/**
 * @method flags=cpp
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native void Matrix_RotatePrepend(int sender, double angle);
/**
 * @method flags=cpp
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native void Matrix_ScalePrepend(int sender, double scaleX, double scaleY);
/**
 * @method flags=cpp
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native void Matrix_SetIdentity(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native void Matrix_SkewPrepend(int sender, double skewX, double skewY);
/**
 * @method flags=cpp
 * @param sender cast=(Matrix^),flags=gcobject
 */
public static final native void Matrix_TranslatePrepend(int sender, double tx, double ty);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(Matrix^),flags=gcobject
 * @param point cast=(Point),flags=gcobject
 */
public static final native int Matrix_Transform(int sender, int point);
/**
 * @method accessor=MessageBox::Show
 * @param messageBoxText cast=(String^),flags=gcobject
 * @param caption cast=(String^),flags=gcobject
 * @param button cast=(MessageBoxButton)
 * @param icon cast=(MessageBoxImage)
 * @param defaultResult cast=(MessageBoxResult)
 */
public static final native int MessageBox_Show (int messageBoxText, int caption, int button, int icon,	int defaultResult);
/** @method accessor=Mouse::Captured,flags=const gcobject */
public static final native int Mouse_Captured();
/** @method accessor=Mouse::DirectlyOver,flags=const gcobject */
public static final native int Mouse_DirectlyOver();
/**
 * @method accessor=Mouse::GetPosition,flags=gcobject
 * @param relativeTo cast=(IInputElement^),flags=gcobject
 */
public static final native int Mouse_GetPosition(int relativeTo);
/**
 * @method accessor=Mouse::SetCursor
 * @param cursor cast=(Cursor^),flags=gcobject
 */
public static final native boolean Mouse_SetCursor(int cursor);
/** @method accessor=Mouse::LeftButton,flags=const */
public static final native int Mouse_LeftButton();
/** @method accessor=Mouse::RightButton,flags=const */
public static final native int Mouse_RightButton();
/** @method accessor=Mouse::MiddleButton,flags=const */
public static final native int Mouse_MiddleButton();
/** @method accessor=Mouse::XButton1,flags=const */
public static final native int Mouse_XButton1();
/** @method accessor=Mouse::XButton2,flags=const */
public static final native int Mouse_XButton2();
/**
 * @method flags=getter
 * @param sender cast=(MouseButtonEventArgs^),flags=gcobject
 */
public static final native int MouseButtonEventArgs_ButtonState(int sender);
/**
 * @method flags=getter
 * @param sender cast=(MouseButtonEventArgs^),flags=gcobject
 */
public static final native int MouseButtonEventArgs_ClickCount(int sender);
/**
 * @method flags=getter
 * @param sender cast=(MouseButtonEventArgs^),flags=gcobject
 */
public static final native int MouseButtonEventArgs_ChangedButton(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(MouseEventArgs^),flags=gcobject
 * @param relativeTo cast=(IInputElement^),flags=gcobject
 */
public static final native int MouseEventArgs_GetPosition(int sender, int relativeTo);
/**
 * @method flags=getter
 * @param sender cast=(MouseEventArgs^),flags=gcobject
 */
public static final native int MouseEventArgs_LeftButton(int sender);
/**
 * @method flags=getter
 * @param sender cast=(MouseEventArgs^),flags=gcobject
 */
public static final native int MouseEventArgs_MiddleButton(int sender);
/**
 * @method flags=getter
 * @param sender cast=(MouseEventArgs^),flags=gcobject
 */
public static final native int MouseEventArgs_RightButton(int sender);
/**
 * @method flags=getter
 * @param sender cast=(MouseEventArgs^),flags=gcobject
 */
public static final native int MouseEventArgs_XButton1(int sender);
/**
 * @method flags=getter
 * @param sender cast=(MouseEventArgs^),flags=gcobject
 */
public static final native int MouseEventArgs_XButton2(int sender);
/**
 * @method flags=getter
 * @param sender cast=(MouseWheelEventArgs^),flags=gcobject
 */
public static final native int MouseWheelEventArgs_Delta(int sender);
/**
 * @method flags=getter
 * @param sender cast=(ListBoxItem^),flags=gcobject
 */
public static final native boolean ListBoxItem_IsSelected(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ListBoxItem^),flags=gcobject
 */
public static final native void ListBoxItem_IsSelected(int sender, boolean value);
/**
 * @method flags=cpp
 * @param sender cast=(ListBox^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native void ListBox_ScrollIntoView(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(ListBox^),flags=gcobject
 */
public static final native void ListBox_SelectAll(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(ListBox^),flags=gcobject
 */
public static final native int ListBox_SelectedItems(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ListBox^),flags=gcobject
 * @param value cast=(SelectionMode)
 */
public static final native void ListBox_SelectionMode(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(ListBox^),flags=gcobject
 */
public static final native void ListBox_UnselectAll(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ListView^),flags=gcobject
 * @param value cast=(ViewBase^),flags=gcobject
 */
public static final native void ListView_View(int sender, int value);
/** @method accessor=ListViewItem::typeid,flags=const gcobject */
public static final native int ListViewItem_typeid();
/**
 * @method flags=cpp gcobject
 * @param sender cast=(System::IO::MemoryStream^),flags=gcobject
 */
public static final native int MemoryStream_ToArray(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(System::IO::MemoryStream^),flags=gcobject
 * @param buffer cast=(array<Byte>^),flags=gcobject
 */
public static final native void MemoryStream_Write(int sender, int buffer, int offset, int count);
/**
 * @method flags=setter
 * @param sender cast=(Menu^),flags=gcobject
 */
public static final native void Menu_IsMainMenu(int sender, boolean value);
/**
 * @method flags=adder
 * @param sender cast=(MenuItem^),flags=gcobject
 * @param handler cast=(RoutedEventHandler^),flags=gcobject
 */
public static final native void MenuItem_Click(int sender, int handler);
/**
 * @method flags=setter
 * @param sender cast=(MenuItem^),flags=gcobject
 * @param value cast=(Image^),flags=gcobject
 */
public static final native void MenuItem_Icon(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(MenuItem^),flags=gcobject
 * @param value cast=(String^),flags=gcobject
 */
public static final native void MenuItem_InputGestureText(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(MenuItem^),flags=gcobject
 */
public static final native void MenuItem_IsCheckable(int sender, boolean value);
/**
 * @method flags=getter
 * @param sender cast=(MenuItem^),flags=gcobject
 */
public static final native boolean MenuItem_IsChecked(int sender);
/**
 * @method flags=setter
 * @param sender cast=(MenuItem^),flags=gcobject
 */
public static final native void MenuItem_IsChecked(int sender, boolean value);
/**
 * @method flags=adder
 * @param sender cast=(MenuItem^),flags=gcobject
 * @param handler cast=(RoutedEventHandler^),flags=gcobject
 */
public static final native void MenuItem_SubmenuClosed(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(MenuItem^),flags=gcobject
 * @param handler cast=(RoutedEventHandler^),flags=gcobject
 */
public static final native void MenuItem_SubmenuOpened(int sender, int handler);
/** @method accessor=Int32Rect::Empty,flags=const gcobject */
public static final native int Int32Rect_Empty();
/**
 * @method flags=gcobject getter
 * @param sender cast=(MatrixTransform^),flags=gcobject
 */
public static final native int MatrixTransform_Matrix(int sender);
/**
 * @method flags=setter
 * @param sender cast=(MatrixTransform^),flags=gcobject
 * @param value cast=(Matrix),flags=gcobject
 */
public static final native void MatrixTransform_Matrix(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(System::Windows::Forms::NotifyIcon^),flags=gcobject
 * @param value cast=(System::Drawing::Icon^),flags=gcobject
 */
public static final native void NotifyIcon_Icon(int sender, int value);
/**
 * @method flags=adder
 * @param sender cast=(System::Windows::Forms::NotifyIcon^),flags=gcobject
 * @param handler cast=(System::Windows::Forms::MouseEventHandler^),flags=gcobject
 */
public static final native void NotifyIcon_MouseDown(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(System::Windows::Forms::NotifyIcon^),flags=gcobject
 * @param handler cast=(System::Windows::Forms::MouseEventHandler^),flags=gcobject
 */
public static final native void NotifyIcon_MouseUp(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(System::Windows::Forms::NotifyIcon^),flags=gcobject
 * @param handler cast=(EventHandler^),flags=gcobject
 */
public static final native void NotifyIcon_DoubleClick(int sender, int handler);
/**
 * @method flags=setter
 * @param sender cast=(System::Windows::Forms::NotifyIcon^),flags=gcobject
 * @param value cast=(String^),flags=gcobject
 */
public static final native void NotifyIcon_Text(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(System::Windows::Forms::NotifyIcon^),flags=gcobject
 */
public static final native void NotifyIcon_Visible(int sender, boolean value);
/**
 * @method flags=cpp
 * @param sender cast=(Object ^),flags=gcobject
 * @param o cast=(Object ^),flags=gcobject
 */
public static final native boolean Object_Equals(int sender, int o);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(Object ^),flags=gcobject
 */
public static final native int Object_GetType(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(Object ^),flags=gcobject
 */
public static final native int Object_ToString(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(ObservableCollection<GridViewColumn^>^),flags=gcobject
 */
public static final native void ObservableCollectionGridViewColumn_Move(int sender, int oldIndex, int newIndex);
/**
 * @method flags=setter
 * @param sender cast=(OpenFileDialog^),flags=gcobject
 */
public static final native void OpenFileDialog_Multiselect (int sender, boolean value);
/**
 * @method flags=setter
 * @param sender cast=(RowDefinition^),flags=gcobject
 * @param height cast=(GridLength),flags=gcobject
 */
public static final native void RowDefinition_Height(int sender, int height);
/**
 * @method flags=cpp
 * @param sender cast=(RowDefinitionCollection^),flags=gcobject
 * @param row cast=(RowDefinition^),flags=gcobject
 */
public static final native void RowDefinitionCollection_Add(int sender, int row);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Panel^),flags=gcobject
 */
public static final native int Panel_Background(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Panel^),flags=gcobject
 * @param brush cast=(Brush^),flags=gcobject
 */
public static final native void Panel_Background(int sender, int brush);
/** @method accessor=Panel::BackgroundProperty,flags=const gcobject */
public static final native int Panel_BackgroundProperty();
/**
 * @method flags=gcobject getter
 * @param sender cast=(Panel^),flags=gcobject
 */
public static final native int Panel_Children(int sender);
/**
 * @method accessor=Panel::GetZIndex
 * @param element cast=(UIElement^),flags=gcobject
 */
public static final native int Panel_GetZIndex(int element);
/**
 * @method accessor=Panel::SetZIndex
 * @param element cast=(UIElement ^),flags=gcobject
 */
public static final native void Panel_SetZIndex(int element, int index);
/**
 * @method flags=gcobject getter
 * @param sender cast=(PasswordBox^),flags=gcobject
 */
public static final native int PasswordBox_Password(int sender);
/**
 * @method flags=setter
 * @param sender cast=(PasswordBox^),flags=gcobject
 * @param value cast=(String^),flags=gcobject
 */
public static final native void PasswordBox_Password(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(PasswordBox^),flags=gcobject
 */
public static final native char PasswordBox_PasswordChar(int sender);
/**
 * @method flags=setter
 * @param sender cast=(PasswordBox^),flags=gcobject
 */
public static final native void PasswordBox_PasswordChar(int sender, char value);
/**
 * @method flags=getter
 * @param sender cast=(PasswordBox^),flags=gcobject
 */
public static final native int PasswordBox_MaxLength(int sender);
/**
 * @method flags=setter
 * @param sender cast=(PasswordBox^),flags=gcobject
 */
public static final native void PasswordBox_MaxLength(int sender, int value);
/**
 * @method flags=adder
 * @param sender cast=(PasswordBox^),flags=gcobject
 * @param handler cast=(RoutedEventHandler^),flags=gcobject
 */
public static final native void PasswordBox_PasswordChanged(int sender, int handler);
/**
 * @method flags=cpp
 * @param sender cast=(PasswordBox^),flags=gcobject
 */
public static final native void PasswordBox_Paste(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Path^),flags=gcobject
 * @param geometry cast=(Geometry^),flags=gcobject
 */
public static final native void Path_Data(int sender, int geometry);
/**
 * @method flags=setter
 * @param sender cast=(Path^),flags=gcobject
 * @param brush cast=(Brush^),flags=gcobject
 */
public static final native void Path_Fill(int sender, int brush);
/**
 * @method flags=setter
 * @param sender cast=(Path^),flags=gcobject
 * @param value cast=(Stretch)
 */
public static final native void Path_Stretch(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(PathFigureCollection^),flags=gcobject
 * @param element cast=(PathFigure^),flags=gcobject
 */
public static final native void PathFigureCollection_Add(int sender, int element);
/**
 * @method flags=getter
 * @param sender cast=(PathFigureCollection^),flags=gcobject
 */
public static final native int PathFigureCollection_Count(int sender);
/**
 * @method flags=setter
 * @param sender cast=(PathFigure^),flags=gcobject
 * @param point cast=(Point),flags=gcobject
 */
public static final native void PathFigure_StartPoint(int sender, int point);
/**
 * @method flags=setter
 * @param sender cast=(PathFigure^),flags=gcobject
 */
public static final native void PathFigure_IsClosed(int sender, boolean closed);
/**
 * @method flags=getter
 * @param sender cast=(PathFigure^),flags=gcobject
 */
public static final native boolean PathFigure_IsClosed(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(PathFigure^),flags=gcobject
 */
public static final native int PathFigure_Segments(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(PathFigure^),flags=gcobject
 */
public static final native int PathFigure_Segments(int sender, int index);
/**
 * @method flags=cpp
 * @param sender cast=(PathGeometry^),flags=gcobject
 * @param geometry cast=(Geometry^),flags=gcobject
 */
public static final native void PathGeometry_AddGeometry(int sender, int geometry);
/**
 * @method flags=gcobject getter
 * @param sender cast=(PathGeometry^),flags=gcobject
 */
public static final native int PathGeometry_Bounds(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(PathGeometry^),flags=gcobject
 */
public static final native int PathGeometry_Clone(int sender);
/**
 * @method flags=setter
 * @param sender cast=(PathGeometry^),flags=gcobject
 * @param value cast=(FillRule)
 */
public static final native void PathGeometry_FillRule(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(PathGeometry^),flags=gcobject
 */
public static final native int PathGeometry_Figures(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(PathGeometry^),flags=gcobject
 */
public static final native int PathGeometry_Figures(int sender, int index);
/**
 * @method flags=cpp
 * @param sender cast=(PathSegmentCollection^),flags=gcobject
 * @param element cast=(PathSegment^),flags=gcobject
 */
public static final native void PathSegmentCollection_Add(int sender, int element);
/**
 * @method flags=getter
 * @param sender cast=(PathSegmentCollection^),flags=gcobject
 */
public static final native int PathSegmentCollection_Count(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Pen^),flags=gcobject
 */
public static final native int Pen_Brush(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Pen^),flags=gcobject
 * @param value cast=(Brush^),flags=gcobject
 */
public static final native void Pen_Brush(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(Pen^),flags=gcobject
 * @param value cast=(PenLineCap)
 */
public static final native void Pen_DashCap(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(Pen^),flags=gcobject
 * @param value cast=(DashStyle^),flags=gcobject
 */
public static final native void Pen_DashStyle(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(Pen^),flags=gcobject
 * @param value cast=(PenLineCap)
 */
public static final native void Pen_EndLineCap(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(Pen^),flags=gcobject
 * @param value cast=(PenLineCap)
 */
public static final native void Pen_StartLineCap(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(Pen^),flags=gcobject
 * @param value cast=(PenLineJoin)
 */
public static final native void Pen_LineJoin(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(Pen^),flags=gcobject
 */
public static final native void Pen_MiterLimit(int sender, double value);
/**
 * @method flags=setter
 * @param sender cast=(Pen^),flags=gcobject
 */
public static final native void Pen_Thickness(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(PixelFormat^),flags=gcobject
 */
public static final native int PixelFormat_BitsPerPixel(int sender);
/** @method accessor=PixelFormats::Bgr101010,flags=const gcobject */
public static final native int PixelFormats_Bgr101010();
/** @method accessor=PixelFormats::Bgr24,flags=const gcobject */
public static final native int PixelFormats_Bgr24();
/** @method accessor=PixelFormats::Bgr32,flags=const gcobject */
public static final native int PixelFormats_Bgr32();
/** @method accessor=PixelFormats::Bgr555,flags=const gcobject */
public static final native int PixelFormats_Bgr555();
/** @method accessor=PixelFormats::Bgr565,flags=const gcobject */
public static final native int PixelFormats_Bgr565();
/** @method accessor=PixelFormats::Bgra32,flags=const gcobject */
public static final native int PixelFormats_Bgra32();
/** @method accessor=PixelFormats::BlackWhite,flags=const gcobject */
public static final native int PixelFormats_BlackWhite();
/** @method accessor=PixelFormats::Default,flags=const gcobject */
public static final native int PixelFormats_Default();
/** @method accessor=PixelFormats::Indexed1,flags=const gcobject */
public static final native int PixelFormats_Indexed1();
/** @method accessor=PixelFormats::Indexed2,flags=const gcobject */
public static final native int PixelFormats_Indexed2();
/** @method accessor=PixelFormats::Indexed4,flags=const gcobject */
public static final native int PixelFormats_Indexed4();
/** @method accessor=PixelFormats::Indexed8,flags=const gcobject */
public static final native int PixelFormats_Indexed8();
/** @method accessor=PixelFormats::Pbgra32,flags=const gcobject */
public static final native int PixelFormats_Pbgra32();
/** @method accessor=PixelFormats::Rgb24,flags=const gcobject */
public static final native int PixelFormats_Rgb24();
/**
 * @method flags=cpp
 * @param sender cast=(PointCollection^),flags=gcobject
 * @param point cast=(Point),flags=gcobject
 */
public static final native void PointCollection_Add(int sender, int point);
/**
 * @method flags=getter
 * @param sender cast=(Point^),flags=gcobject
 */
public static final native double Point_X(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Point^),flags=gcobject
 */
public static final native double Point_Y(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Popup^),flags=gcobject
 */
public static final native void Popup_AllowsTransparency(int sender, boolean value);
/**
 * @method flags=setter
 * @param sender cast=(Popup^),flags=gcobject
 * @param child cast=(UIElement^),flags=gcobject
 */
public static final native void Popup_Child(int sender, int child);
/**
 * @method flags=getter gcobject
 * @param sender cast=(Popup^),flags=gcobject
 */
public static final native int Popup_Child(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Popup^),flags=gcobject
 */
public static final native void Popup_HorizontalOffset(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(Popup^),flags=gcobject
 */
public static final native double Popup_HorizontalOffset(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Popup^),flags=gcobject
 */
public static final native void Popup_IsOpen(int sender, boolean value);
/**
 * @method flags=getter
 * @param sender cast=(Popup^),flags=gcobject
 */
public static final native boolean Popup_IsOpen(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Popup^),flags=gcobject
 */
public static final native void Popup_VerticalOffset(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(Popup^),flags=gcobject
 */
public static final native double Popup_VerticalOffset(int sender);
/**
 * @method flags=adder
 * @param sender cast=(Popup^),flags=gcobject
 * @param handler cast=(EventHandler^),flags=gcobject
 */
public static final native void Popup_Closed(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(Popup^),flags=gcobject
 * @param handler cast=(EventHandler^),flags=gcobject
 */
public static final native void Popup_Opened(int sender, int handler);
/** @method accessor=PresentationSource::CurrentSources,flags=const gcobject */
public static final native int PresentationSource_CurrentSources();
/**
 * @method accessor=PresentationSource::FromVisual,flags=gcobject
 * @param visual cast=(Visual^),flags=gcobject
 */
public static final native int PresentationSource_FromVisual(int visual);
/**
 * @method flags=gcobject getter
 * @param sender cast=(PresentationSource^),flags=gcobject
 */
public static final native int PresentationSource_RootVisual(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ProgressBar ^),flags=gcobject
 */
public static final native void ProgressBar_IsIndeterminate(int sender, boolean value);
/**
 * @method flags=getter
 * @param sender cast=(ProgressBar ^),flags=gcobject
 */
public static final native void ProgressBar_IsIndeterminate(int sender);
/**
 * @method flags=setter
 * @param handle cast=(ProgressBar ^),flags=gcobject
 * @param value cast=(Orientation)
 */
public static final native void ProgressBar_Orientation(int handle, int value);
/**
 * @method flags=cpp
 * @param sender cast=(PropertyInfo^),flags=gcobject
 * @param obj cast=(Object^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 * @param indexArray cast=(array<Object^>^),flags=gcobject
 */
public static final native void PropertyInfo_SetValue(int sender, int obj, int value, int indexArray);
/**
 * @method accessor=PropertyInfo::SetValue,flags=cpp
 * @param sender cast=(PropertyInfo^),flags=gcobject
 * @param obj cast=(Object^),flags=gcobject
 * @param value cast=(bool)
 * @param indexArray cast=(array<Object^>^),flags=gcobject
 */
public static final native void PropertyInfo_SetValueBoolean(int sender, int obj, boolean value, int indexArray);
/**
 * @method flags=getter
 * @param sender cast=(QueryContinueDragEventArgs^),flags=gcobject
 */
public static final native boolean QueryContinueDragEventArgs_EscapePressed(int sender);
/**
 * @method flags=setter
 * @param sender cast=(QueryContinueDragEventArgs^),flags=gcobject
 * @param dragAction cast=(DragAction)
 */
public static final native void QueryContinueDragEventArgs_Action(int sender, int dragAction);
/**
 * @method flags=getter
 * @param sender cast=(RangeBase ^),flags=gcobject
 */
public static final native double RangeBase_LargeChange(int sender);
/**
 * @method flags=setter
 * @param sender cast=(RangeBase ^),flags=gcobject
 */
public static final native void RangeBase_LargeChange(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(RangeBase ^),flags=gcobject
 */
public static final native double RangeBase_Maximum(int sender);
/**
 * @method flags=setter
 * @param sender cast=(RangeBase ^),flags=gcobject
 */
public static final native void  RangeBase_Maximum(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(RangeBase ^),flags=gcobject
 */
public static final native double RangeBase_Minimum(int sender);
/**
 * @method flags=setter
 * @param sender cast=(RangeBase ^),flags=gcobject
 */
public static final native void  RangeBase_Minimum(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(RangeBase ^),flags=gcobject
 */
public static final native double RangeBase_SmallChange(int sender);
/**
 * @method flags=setter
 * @param sender cast=(RangeBase ^),flags=gcobject
 */
public static final native void RangeBase_SmallChange(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(RangeBase ^),flags=gcobject
 */
public static final native double RangeBase_Value(int sender);
/**
 * @method flags=setter
 * @param sender cast=(RangeBase ^),flags=gcobject
 */
public static final native void RangeBase_Value(int sender, double value);
/**
 * @method flags=adder
 * @param sender cast=(RangeBase ^),flags=gcobject
 * @param handler cast=(RoutedPropertyChangedEventHandler<double> ^),flags=gcobject
 */
public static final native void RangeBase_ValueChanged(int sender, int handler);
/**
 * @method flags=cpp
 * @param sender cast=(Rect^),flags=gcobject
 * @param point cast=(Point),flags=gcobject
 */
public static final native boolean Rect_Contains(int sender, int point);
/**
 * @method flags=cpp
 * @param sender cast=(Rect^),flags=gcobject
 * @param rect cast=(Rect),flags=gcobject
 */
public static final native void Rect_Intersect(int sender, int rect);
/**
 * @method flags=cpp
 * @param sender cast=(Rect^),flags=gcobject
 * @param rect cast=(Rect),flags=gcobject
 */
public static final native void Rect_Union(int sender, int rect);
/**
 * @method flags=getter
 * @param sender cast=(Rect^),flags=gcobject
 */
public static final native double Rect_X(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Rect^),flags=gcobject
 */
public static final native void Rect_X(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(Rect^),flags=gcobject
 */
public static final native double Rect_Y(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Rect^),flags=gcobject
 */
public static final native void Rect_Y(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(Rect^),flags=gcobject
 */
public static final native double Rect_Width(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Rect^),flags=gcobject
 */
public static final native void Rect_Width(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(Rect^),flags=gcobject
 */
public static final native double Rect_Height(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Rect^),flags=gcobject
 */
public static final native void Rect_Height(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(System::Drawing::Rectangle^),flags=gcobject
 */
public static final native int Rectangle_X(int sender);
/**
 * @method flags=getter
 * @param sender cast=(System::Drawing::Rectangle^),flags=gcobject
 */
public static final native int Rectangle_Y(int sender);
/**
 * @method flags=getter
 * @param sender cast=(System::Drawing::Rectangle^),flags=gcobject
 */
public static final native int Rectangle_Width(int sender);
/**
 * @method flags=getter
 * @param sender cast=(System::Drawing::Rectangle^),flags=gcobject
 */
public static final native int Rectangle_Height(int sender);
/** @method accessor=Registry::ClassesRoot,flags=const gcobject */
public static final native int Registry_ClassesRoot();
/**
 * @method flags=cpp gcobject
 * @param sender cast=(RegistryKey^),flags=gcobject
 * @param key cast=(String^),flags=gcobject
 */
public static final native int RegistryKey_OpenSubKey(int sender, int key);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(RegistryKey^),flags=gcobject
 */
public static final native int RegistryKey_GetSubKeyNames(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(RegistryKey^),flags=gcobject
 * @param name cast=(String^),flags=gcobject
 */
public static final native int RegistryKey_GetValue(int sender, int name);
/** @method accessor=RepeatBehavior::Forever,flags=const gcobject */
public static final native int RepeatBehavior_Forever();
/**
 * @method flags=setter
 * @param sender cast=(RelativeSource^),flags=gcobject
 * @param type cast=(Type^),flags=gcobject
 */
public static final native void RelativeSource_AncestorType(int sender, int type);
/**
 * @method accessor=RenderOptions::GetBitmapScalingMode
 * @param target cast=(DependencyObject^),flags=gcobject
 */
public static final native int RenderOptions_GetBitmapScalingMode(int target);
/**
 * @method accessor=RenderOptions::SetBitmapScalingMode
 * @param target cast=(DependencyObject^),flags=gcobject
 * @param mode cast=(BitmapScalingMode)
 */
public static final native void RenderOptions_SetBitmapScalingMode(int target, int mode);
/**
 * @method accessor=RenderOptions::SetEdgeMode
 * @param target cast=(DependencyObject^),flags=gcobject
 * @param edgeMode cast=(EdgeMode)
 */
public static final native void RenderOptions_SetEdgeMode(int target, int edgeMode);
/**
 * @method flags=cpp
 * @param sender cast=(RenderTargetBitmap^),flags=gcobject
 * @param visual cast=(Visual^),flags=gcobject
 */
public static final native void RenderTargetBitmap_Render(int sender, int visual);
/**
 * @method flags=setter
 * @param sender cast=(RoutedEventArgs^),flags=gcobject
 */
public static final native void RoutedEventArgs_Handled(int sender, boolean handled);
/** @method accessor=RoutedEventArgs::typeid,flags=const gcobject */
public static final native int RoutedEventArgs_typeid();
/**
 * @method flags=gcobject getter
 * @param sender cast=(RoutedEventArgs^),flags=gcobject
 */
public static final native int RoutedEventArgs_OriginalSource(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(RoutedEventArgs^),flags=gcobject
 */
public static final native int RoutedEventArgs_Source(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(RoutedPropertyChangedEventArgs<Object^>^),flags=gcobject
 */
public static final native int RoutedPropertyChangedEventArgs_NewValue(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(RoutedPropertyChangedEventArgs<Object^>^),flags=gcobject
 */
public static final native int RoutedPropertyChangedEventArgs_OldValue(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Run^),flags=gcobject
 * @param value cast=(String^),flags=gcobject
 */
public static final native void Run_Text(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(SaveFileDialog^),flags=gcobject
 */
public static final native void SaveFileDialog_OverwritePrompt(int sender, boolean value);
/** @method accessor=System::Windows::Forms::Screen::AllScreens,flags=const gcobject */
public static final native int Screen_AllScreens();
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Windows::Forms::Screen^),flags=gcobject
 */
public static final native int Screen_Bounds(int sender);
/** @method accessor=System::Windows::Forms::Screen::PrimaryScreen,flags=const gcobject */
public static final native int Screen_PrimaryScreen();
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Windows::Forms::Screen^),flags=gcobject
 */
public static final native int Screen_WorkingArea(int sender);
/**
 * @method flags=getter
 * @param sender cast=(ScrollBar^),flags=gcobject
 */
public static final native int ScrollBar_Orientation(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ScrollBar^),flags=gcobject
 * @param orientation cast=(Orientation)
 */
public static final native void ScrollBar_Orientation(int sender, int orientation);
/**
 * @method flags=adder
 * @param sender cast=(ScrollBar^),flags=gcobject
 * @param handler cast=(ScrollEventHandler^),flags=gcobject
 */
public static final native void ScrollBar_Scroll(int sender, int handler);
/**
 * @method flags=getter
 * @param sender cast=(ScrollBar^),flags=gcobject
 */
public static final native double ScrollBar_ViewportSize(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ScrollBar^),flags=gcobject
 */
public static final native void ScrollBar_ViewportSize(int sender, double value);
/** @method accessor=ScrollBar::typeid,flags=const gcobject */
public static final native int ScrollBar_typeid();
/**
 * @method flags=getter gcobject
 * @param sender cast=(ScrollBar^),flags=gcobject
 */
public static final native int ScrollBar_Track(int sender);
/**
 * @method flags=getter gcobject
 * @param sender cast=(Track^),flags=gcobject
 */
public static final native int Track_Thumb(int sender);
/**
 * @method flags=getter
 * @param sender cast=(ScrollEventArgs^),flags=gcobject
 */
public static final native int ScrollEventArgs_ScrollEventType(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(ScrollViewer^),flags=gcobject
 */
public static final native void ScrollViewer_ScrollToVerticalOffset(int sender, double offset);
/**
 * @method accessor=ScrollViewer::SetVerticalScrollBarVisibility
 * @param sender cast=(DependencyObject^),flags=gcobject
 * @param visibility cast=(ScrollBarVisibility)
 */
public static final native void ScrollViewer_SetVerticalScrollBarVisibility(int sender, int visibility);
/**
 * @method accessor=ScrollViewer::SetHorizontalScrollBarVisibility
 * @param sender cast=(DependencyObject^),flags=gcobject
 * @param visibility cast=(ScrollBarVisibility)
 */
public static final native void ScrollViewer_SetHorizontalScrollBarVisibility(int sender, int visibility);
/** @method accessor=ScrollViewer::typeid,flags=const gcobject */
public static final native int ScrollViewer_typeid();
/**
 * @method flags=getter
 * @param sender cast=(ScrollViewer^),flags=gcobject
 */
public static final native double ScrollViewer_VerticalOffset(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Selector^),flags=gcobject
 */
public static final native void Selector_IsSynchronizedWithCurrentItem(int sender, boolean value);
/**
 * @method flags=getter
 * @param sender cast=(Selector^),flags=gcobject
 */
public static final native int Selector_SelectedIndex(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Selector^),flags=gcobject
 */
public static final native void Selector_SelectedIndex(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Selector^),flags=gcobject
 */
public static final native int Selector_SelectedItem(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Selector^),flags=gcobject
 */
public static final native int Selector_SelectedValue(int sender);
/**
 * @method flags=adder
 * @param sender cast=(Selector^),flags=gcobject
 * @param handler cast=(SelectionChangedEventHandler^),flags=gcobject
 */
public static final native void Selector_SelectionChanged(int sender, int handler);
/**
 * @method flags=gcobject getter
 * @param sender cast=(SelectionChangedEventArgs^),flags=gcobject
 */
public static final native int SelectionChangedEventArgs_AddedItems(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(SelectionChangedEventArgs^),flags=gcobject
 */
public static final native int SelectionChangedEventArgs_RemovedItems(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(SetterBaseCollection^),flags=gcobject
 * @param setter cast=(SetterBase^),flags=gcobject
 */
public static final native void SetterBaseCollection_Add(int sender, int setter);
/**
 * @method flags=setter
 * @param sender cast=(Shape^),flags=gcobject
 * @param brush cast=(Brush^),flags=gcobject
 */
public static final native void Shape_Fill(int sender, int brush);
/**
 * @method flags=setter
 * @param sender cast=(Shape^),flags=gcobject
 * @param brush cast=(Brush^),flags=gcobject
 */
public static final native void Shape_Stroke(int sender, int brush);
/**
 * @method flags=setter
 * @param sender cast=(Shape^),flags=gcobject
 */
public static final native void Shape_StrokeThickness(int sender, double strokethickness);
/**
 * @method flags=getter
 * @param sender cast=(Size ^),flags=gcobject
 */
public static final native double Size_Width(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Size ^),flags=gcobject
 */
public static final native double Size_Height(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Size ^),flags=gcobject
 */
public static final native void Size_Width(int sender, double width);
/**
 * @method flags=setter
 * @param sender cast=(Size ^),flags=gcobject
 */
public static final native void Size_Height(int sender, double height);
/**
 * @method flags=gcobject getter
 * @param sender cast=(SizeChangedEventArgs^),flags=gcobject
 */
public static final native int SizeChangedEventArgs_NewSize(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(SizeChangedEventArgs^),flags=gcobject
 */
public static final native int SizeChangedEventArgs_PreviousSize(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Slider ^),flags=gcobject
 * @param value cast=(Orientation)
 */
public static final native void Slider_Orientation(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(Slider^),flags=gcobject
 */
public static final native void Slider_TickFrequency(int sender, double value);
/**
 * @method flags=setter
 * @param sender cast=(Slider^),flags=gcobject
 * @param value cast=(TickPlacement)
 */
public static final native void Slider_TickPlacement(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(StackPanel^),flags=gcobject
 * @param orientation cast=(Orientation)
 */
public static final native void StackPanel_Orientation(int sender, int orientation);
/** @method accessor=StackPanel::OrientationProperty,flags=const gcobject */
public static final native int StackPanel_OrientationProperty();
/** @method accessor=StackPanel::typeid,flags=const gcobject */
public static final native int StackPanel_typeid();
/**
 * @method flags=cpp gcobject
 * @param sender cast=(StreamGeometry^),flags=gcobject
 */
public static final native int StreamGeometry_Open(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(StreamGeometryContext^),flags=gcobject
 * @param startPoint cast=(Point),flags=gcobject
 */
public static final native void StreamGeometryContext_BeginFigure(int sender, int startPoint, boolean isFilled, boolean isClosed);
/**
 * @method flags=cpp
 * @param sender cast=(StreamGeometryContext^),flags=gcobject
 */
public static final native void StreamGeometryContext_Close(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(StreamGeometryContext^),flags=gcobject
 * @param startPoint cast=(Point),flags=gcobject
 */
public static final native void StreamGeometryContext_LineTo(int sender, int startPoint, boolean isStroked, boolean isSmoothJoin);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(String^),flags=gcobject
 */
public static final native int String_ToCharArray(int sender);
/** @method accessor=String::typeid,flags=const gcobject */
public static final native int String_typeid();
/**
 * @method flags=getter
 * @param sender cast=(String^),flags=gcobject
 */
public static final native int String_Length(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Style^),flags=gcobject
 */
public static final native int Style_Setters(int sender);
/**
 * @method flags=getter no_gen gcobject
 * @param sender cast=(SWTCanvas^),flags=gcobject
 */
public static final native int SWTCanvas_Visual(int sender);
/**
 * @method flags=no_gen setter
 * @param sender cast=(SWTCanvas^),flags=gcobject
 * @param visual cast=(DrawingVisual^),flags=gcobject
 */
public static final native void SWTCanvas_Visual(int sender, int visual);
/** @method flags=no_gen */
public static final native int SWTDockPanel_JNIRefProperty();
/** @method flags=no_gen */
public static final native int SWTDockPanel_typeid();
/**
 * @method flags=no_gen setter
 * @param sender cast=(SWTTextRunProperties^),flags=gcobject
 * @param brush cast=(Brush^),flags=gcobject
 */
public static final native void SWTTextRunProperties_ForegroundBrush(int sender, int brush);
/** @method accessor=SystemColors::ControlBrush,flags=const gcobject */
public static final native int SystemColors_ControlBrush();
/** @method accessor=SystemColors::ControlColor,flags=const struct gcobject */
public static final native int SystemColors_ControlColor();
/** @method accessor=SystemColors::ControlTextBrush,flags=const gcobject */
public static final native int SystemColors_ControlTextBrush();
/** @method accessor=SystemColors::ControlTextColor,flags=const struct gcobject */
public static final native int SystemColors_ControlTextColor();
/** @method accessor=SystemColors::ControlDarkColor,flags=const struct gcobject */
public static final native int SystemColors_ControlDarkColor();
/** @method accessor=SystemColors::ControlLightColor,flags=const struct gcobject */
public static final native int SystemColors_ControlLightColor();
/** @method accessor=SystemColors::ControlLightLightColor,flags=const struct gcobject */
public static final native int SystemColors_ControlLightLightColor();
/** @method accessor=SystemColors::ControlDarkDarkColor,flags=const struct gcobject */
public static final native int SystemColors_ControlDarkDarkColor();
/** @method accessor=SystemColors::InfoColor,flags=const struct gcobject */
public static final native int SystemColors_InfoColor();
/** @method accessor=SystemColors::InfoTextColor,flags=const struct gcobject */
public static final native int SystemColors_InfoTextColor();
/** @method accessor=SystemColors::ActiveBorderColor,flags=const struct gcobject */
public static final native int SystemColors_ActiveBorderColor();
/** @method accessor=SystemColors::ActiveBorderBrush,flags=const gcobject */
public static final native int SystemColors_ActiveBorderBrush();
/** @method accessor=SystemColors::ActiveCaptionColor,flags=const struct gcobject */
public static final native int SystemColors_ActiveCaptionColor();
/** @method accessor=SystemColors::ActiveCaptionTextColor,flags=const struct gcobject */
public static final native int SystemColors_ActiveCaptionTextColor();
/** @method accessor=SystemColors::GradientActiveCaptionColor,flags=const struct gcobject */
public static final native int SystemColors_GradientActiveCaptionColor();
/** @method accessor=SystemColors::InactiveCaptionColor,flags=const struct gcobject */
public static final native int SystemColors_InactiveCaptionColor();
/** @method accessor=SystemColors::InactiveCaptionTextColor,flags=const struct gcobject */
public static final native int SystemColors_InactiveCaptionTextColor();
/** @method accessor=SystemColors::GradientInactiveCaptionColor,flags=const struct gcobject */
public static final native int SystemColors_GradientInactiveCaptionColor();
/** @method accessor=SystemColors::WindowColor,flags=const struct gcobject */
public static final native int SystemColors_WindowColor();
/** @method accessor=SystemColors::WindowTextColor,flags=const struct gcobject */
public static final native int SystemColors_WindowTextColor();
/** @method accessor=SystemColors::HighlightBrush,flags=const gcobject */
public static final native int SystemColors_HighlightBrush();
/** @method accessor=SystemColors::HighlightColor,flags=const struct gcobject */
public static final native int SystemColors_HighlightColor();
/** @method accessor=SystemColors::HighlightTextColor,flags=const struct gcobject */
public static final native int SystemColors_HighlightTextColor();
/** @method accessor=SystemFonts::MessageFontFamily,flags=const gcobject */
public static final native int SystemFonts_MessageFontFamily();
/** @method accessor=SystemFonts::MessageFontStyle,flags=const gcobject */
public static final native int SystemFonts_MessageFontStyle();
/** @method accessor=SystemParameters::MinimumHorizontalDragDistance,flags=const */
public static final native double SystemParameters_MinimumHorizontalDragDistance();
/** @method accessor=SystemParameters::MinimumVerticalDragDistance,flags=const */
public static final native double SystemParameters_MinimumVerticalDragDistance();
/** @method accessor=SystemParameters::PrimaryScreenHeight,flags=const */
public static final native double SystemParameters_PrimaryScreenHeight();
/** @method accessor=SystemParameters::PrimaryScreenWidth,flags=const */
public static final native double SystemParameters_PrimaryScreenWidth();
/** @method accessor=SystemParameters::VirtualScreenLeft,flags=const */
public static final native double SystemParameters_VirtualScreenLeft();
/** @method accessor=SystemParameters::VirtualScreenTop,flags=const */
public static final native double SystemParameters_VirtualScreenTop();
/** @method accessor=SystemParameters::VirtualScreenWidth,flags=const */
public static final native double SystemParameters_VirtualScreenWidth();
/** @method accessor=SystemParameters::VirtualScreenHeight,flags=const */
public static final native double SystemParameters_VirtualScreenHeight();
/** @method accessor=SystemParameters::VerticalScrollBarWidth,flags=const */
public static final native double SystemParameters_VerticalScrollBarWidth();
/** @method accessor=SystemParameters::VerticalScrollBarButtonHeight,flags=const */
public static final native double SystemParameters_VerticalScrollBarButtonHeight();
/** @method accessor=SystemParameters::HighContrast,flags=const */
public static final native boolean SystemParameters_HighContrast();
/** @method accessor=SystemParameters::HorizontalScrollBarHeight,flags=const */
public static final native double SystemParameters_HorizontalScrollBarHeight();
/** @method accessor=SystemParameters::HorizontalScrollBarButtonWidth,flags=const */
public static final native double SystemParameters_HorizontalScrollBarButtonWidth();
/** @method accessor=SystemParameters::WheelScrollLines,flags=const */
public static final native int SystemParameters_WheelScrollLines();
/** @method accessor=SystemParameters::WorkArea,flags=const gcobject */
public static final native int SystemParameters_WorkArea();
/** @method accessor=SystemParameters::ThinHorizontalBorderHeight,flags=const */
public static final native double SystemParameters_ThinHorizontalBorderHeight();
/** @method accessor=SystemParameters::ThinVerticalBorderWidth,flags=const */
public static final native double SystemParameters_ThinVerticalBorderWidth();
/** @method accessor=SystemFonts::MessageFontSize,flags=const */
public static final native double SystemFonts_MessageFontSize();
/** @method accessor=SystemFonts::MessageFontWeight,flags=const gcobject */
public static final native int SystemFonts_MessageFontWeight();
/**
 * @method flags=setter
 * @param sender cast=(TextBlock^),flags=gcobject
 * @param brush cast=(Brush^),flags=gcobject
 */
public static final native void TextBlock_Background(int sender, int brush);
/** @method accessor=TextBlock::BackgroundProperty,flags=const gcobject */
public static final native int TextBlock_BackgroundProperty();
/** @method accessor=TextBlock::FontFamilyProperty,flags=const gcobject */
public static final native int TextBlock_FontFamilyProperty();
/** @method accessor=TextBlock::FontStyleProperty,flags=const gcobject */
public static final native int TextBlock_FontStyleProperty();
/** @method accessor=TextBlock::FontWeightProperty,flags=const gcobject */
public static final native int TextBlock_FontWeightProperty();
/** @method accessor=TextBlock::FontStretchProperty,flags=const gcobject */
public static final native int TextBlock_FontStretchProperty();
/** @method accessor=TextBlock::FontSizeProperty,flags=const gcobject */
public static final native int TextBlock_FontSizeProperty();
/**
 * @method flags=setter
 * @param sender cast=(TextBlock^),flags=gcobject
 * @param value cast=(FontFamily^),flags=gcobject
 */
public static final native void TextBlock_FontFamily(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(TextBlock^),flags=gcobject
 * @param value cast=(FontStyle),flags=gcobject
 */
public static final native void TextBlock_FontStyle(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(TextBlock^),flags=gcobject
 * @param value cast=(FontWeight),flags=gcobject
 */
public static final native void TextBlock_FontWeight(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(TextBlock^),flags=gcobject
 * @param value cast=(FontStretch),flags=gcobject
 */
public static final native void TextBlock_FontStretch(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(TextBlock^),flags=gcobject
 */
public static final native void TextBlock_FontSize(int sender, double value);
/**
 * @method flags=setter
 * @param sender cast=(TextBlock^),flags=gcobject
 * @param brush cast=(Brush^),flags=gcobject
 */
public static final native void TextBlock_Foreground(int sender, int brush);
/** @method accessor=TextBlock::ForegroundProperty,flags=const gcobject */
public static final native int TextBlock_ForegroundProperty();
/**
 * @method flags=gcobject getter
 * @param sender cast=(TextBounds^),flags=gcobject
 */
public static final native int TextBounds_Rectangle(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(IEnumerable^),flags=gcobject
 */
public static final native int TextBoundsCollection_GetEnumerator(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(IEnumerator^),flags=gcobject
 */
public static final native int TextBoundsCollection_Current(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(System::Collections::Generic::IList<TextTabProperties^>^),flags=gcobject
 * @param tab cast=(TextTabProperties^),flags=gcobject
 */
public static final native void TextTabPropertiesCollection_Add(int sender, int tab);
/**
 * @method flags=setter
 * @param sender cast=(TabControl^),flags=gcobject
 * @param value cast=(Dock)
 */
public static final native void TabControl_TabStripPlacement(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(TabItem^),flags=gcobject
 */
public static final native boolean TabItem_IsSelected(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(TextBlock^),flags=gcobject
 */
public static final native int TextBlock_Inlines(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(TextBlock^),flags=gcobject
 */
public static final native int TextBlock_Text(int sender);
/**
 * @method flags=setter
 * @param sender cast=(TextBlock^),flags=gcobject
 * @param str cast=(String^),flags=gcobject
 */
public static final native void TextBlock_Text(int sender, int str);
/** @method accessor=TextBlock::TextProperty,flags=const gcobject */
public static final native int TextBlock_TextProperty();
/** @method accessor=TextBlock::typeid,flags=const gcobject */
public static final native int TextBlock_typeid();
/**
 * @method flags=getter
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native int TextBox_CaretIndex(int sender);
/**
 * @method flags=setter
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native void TextBox_CaretIndex(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native int TextBox_GetFirstVisibleLineIndex(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native int TextBox_GetLineIndexFromCharacterIndex(int sender, int value);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native int TextBox_GetRectFromCharacterIndex(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native int TextBox_LineCount(int sender);
/**
 * @method flags=getter
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native int TextBox_MaxLength(int sender);
/**
 * @method flags=setter
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native void TextBox_MaxLength(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native void TextBox_ScrollToLine(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native void TextBox_Select(int sender, int start, int length);
/**
 * @method flags=gcobject getter
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native int TextBox_SelectedText(int sender);
/**
 * @method flags=setter
 * @param sender cast=(TextBox^),flags=gcobject
 * @param value cast=(String^),flags=gcobject
 */
public static final native void TextBox_SelectedText(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native int TextBox_SelectionLength(int sender);
/**
 * @method flags=setter
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native void TextBox_SelectionLength(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native int TextBox_SelectionStart(int sender);
/**
 * @method flags=setter
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native void TextBox_SelectionStart(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(TextBox^),flags=gcobject
 */
public static final native int TextBox_Text(int sender);
/**
 * @method flags=setter
 * @param sender cast=(TextBox^),flags=gcobject
 * @param value cast=(String^),flags=gcobject
 */
public static final native void TextBox_Text(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(TextBox^),flags=gcobject
 * @param value cast=(TextWrapping)
 */
public static final native void TextBox_TextWrapping(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(TextBoxBase^),flags=gcobject
 */
public static final native void TextBoxBase_AcceptsReturn(int sender, boolean value);
/**
 * @method flags=setter
 * @param sender cast=(TextBoxBase^),flags=gcobject
 */
public static final native void TextBoxBase_AcceptsTab(int sender, boolean value);
/**
 * @method flags=cpp
 * @param sender cast=(TextBoxBase^),flags=gcobject
 * @param value cast=(String^),flags=gcobject
 */
public static final native void TextBoxBase_AppendText(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(TextBoxBase^),flags=gcobject
 */
public static final native void TextBoxBase_Copy(int sender);
/**
 * @method flags=setter
 * @param sender cast=(TextBoxBase^),flags=gcobject
 * @param value cast=(ScrollBarVisibility)
 */
public static final native void TextBoxBase_HorizontalScrollBarVisibility(int sender, int value);
/**
 * @method flags=cpp
 * @param sender cast=(TextBoxBase^),flags=gcobject
 */
public static final native void TextBoxBase_Cut(int sender);
/**
 * @method flags=getter
 * @param sender cast=(TextBoxBase^),flags=gcobject
 */
public static final native boolean TextBoxBase_IsReadOnly(int sender);
/**
 * @method flags=setter
 * @param sender cast=(TextBoxBase^),flags=gcobject
 */
public static final native void TextBoxBase_IsReadOnly(int sender, boolean value);
/**
 * @method flags=cpp
 * @param sender cast=(TextBoxBase^),flags=gcobject
 */
public static final native void TextBoxBase_Paste(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(TextBoxBase^),flags=gcobject
 */
public static final native void TextBoxBase_ScrollToEnd(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(TextBoxBase^),flags=gcobject
 */
public static final native void TextBoxBase_ScrollToVerticalOffset(int sender, double value);
/**
 * @method flags=cpp
 * @param sender cast=(TextBoxBase^),flags=gcobject
 */
public static final native void TextBoxBase_SelectAll(int sender);
/**
 * @method flags=adder
 * @param sender cast=(TextBoxBase^),flags=gcobject
 * @param handler cast=(TextChangedEventHandler^),flags=gcobject
 */
public static final native void TextBoxBase_TextChanged(int sender, int handler);
/**
 * @method flags=getter
 * @param sender cast=(TextBoxBase^),flags=gcobject
 */
public static final native double TextBoxBase_VerticalOffset(int sender);
/**
 * @method flags=setter
 * @param sender cast=(TextBoxBase^),flags=gcobject
 * @param value cast=(ScrollBarVisibility)
 */
public static final native void TextBoxBase_VerticalScrollBarVisibility(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(TextCompositionEventArgs^),flags=gcobject
 */
public static final native int TextCompositionEventArgs_ControlText(int sender);
/**
 * @method flags=setter
 * @param sender cast=(TextCompositionEventArgs^),flags=gcobject
 */
public static final native void TextCompositionEventArgs_Handled(int sender, boolean value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(TextCompositionEventArgs^),flags=gcobject
 */
public static final native int TextCompositionEventArgs_SystemText(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(TextCompositionEventArgs^),flags=gcobject
 */
public static final native int TextCompositionEventArgs_Text(int sender);
/** @method accessor=TextDecorations::Underline,flags=const gcobject */
public static final native int TextDecorations_Underline();
/** @method accessor=TextDecorations::Strikethrough,flags=const gcobject */
public static final native int TextDecorations_Strikethrough();
/**
 * @method flags=cpp
 * @param sender cast=(TextDecorationCollection^),flags=gcobject
 * @param decoration cast=(TextDecoration^),flags=gcobject
 */
public static final native void TextDecorationCollection_Add(int sender, int decoration); 
/** @method accessor=TextFormatter::Create,flags=gcobject */
public static final native int TextFormatter_Create();
/**
 * @method flags=cpp gcobject
 * @param sender cast=(TextFormatter^),flags=gcobject
 * @param textSource cast=(TextSource^),flags=gcobject
 * @param paragraphProperties cast=(TextParagraphProperties^),flags=gcobject
 * @param previousLineBreak cast=(TextLineBreak^),flags=gcobject
 */
public static final native int TextFormatter_FormatLine(int sender, int textSource, int firstCharIndex, double paragraphWidth, int paragraphProperties, int previousLineBreak);
/**
 * @method flags=getter
 * @param sender cast=(TextLine^),flags=gcobject
 */
public static final native double TextLine_Baseline(int sender);
/**
 * @method flags=getter
 * @param sender cast=(TextLine^),flags=gcobject
 */
public static final native double TextLine_Height(int sender);
/**
 * @method flags=getter
 * @param sender cast=(TextLine^),flags=gcobject
 */
public static final native int TextLine_NewlineLength(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(TextLine^),flags=gcobject
 * @param characterHit cast=(CharacterHit),flags=gcobject
 */
public static final native int TextLine_GetNextCaretCharacterHit(int sender, int characterHit);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(TextLine^),flags=gcobject
 * @param characterHit cast=(CharacterHit),flags=gcobject
 */
public static final native int TextLine_GetPreviousCaretCharacterHit(int sender, int characterHit);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(TextLine^),flags=gcobject
 */
public static final native int TextLine_GetTextLineBreak(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(TextLine^),flags=gcobject
 */
public static final native int TextLine_GetTextBounds(int sender, int firstTextSourceCharacterIndex, int textLength);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(TextLine^),flags=gcobject
 */
public static final native int TextLine_GetCharacterHitFromDistance(int sender, double distance);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(TextLine^),flags=gcobject
 */
public static final native int TextLine_GetIndexedGlyphRuns(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(TextLine^),flags=gcobject
 * @param characterHit cast=(CharacterHit),flags=gcobject
 */
public static final native double TextLine_GetDistanceFromCharacterHit(int sender, int characterHit);
/**
 * @method flags=getter
 * @param sender cast=(TextLine^),flags=gcobject
 */
public static final native int TextLine_Length(int sender);
/**
 * @method flags=getter
 * @param sender cast=(TextLine^),flags=gcobject
 */
public static final native double TextLine_Start(int sender);
/**
 * @method flags=getter
 * @param sender cast=(TextLine^),flags=gcobject
 */
public static final native double TextLine_Width(int sender);
/**
 * @method flags=getter
 * @param sender cast=(TextLine^),flags=gcobject
 */
public static final native double TextLine_WidthIncludingTrailingWhitespace(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(TextLine^),flags=gcobject
 * @param drawContext cast=(DrawingContext^),flags=gcobject
 * @param origin cast=(Point),flags=gcobject
 * @param invertAxes cast=(InvertAxes)
 */
public static final native void TextLine_Draw(int sender, int drawContext, int origin, int invertAxes);
/**
 * @method flags=getter
 * @param sender cast=(Thickness^),flags=gcobject
 */
public static final native double Thickness_Left(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Thickness^),flags=gcobject
 */
public static final native double Thickness_Right(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Thickness^),flags=gcobject
 */
public static final native double Thickness_Top(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Thickness^),flags=gcobject
 */
public static final native double Thickness_Bottom(int sender);
/** @method accessor=Thumb::DragDeltaEvent,flags=const gcobject */
public static final native int Thumb_DragDeltaEvent();
/**
 * @method flags=setter
 * @param sender cast=(TileBrush^),flags=gcobject
 * @param mode cast=(TileMode)
 */
public static final native void TileBrush_TileMode(int sender, int mode);
/**
 * @method flags=setter
 * @param sender cast=(TileBrush^),flags=gcobject
 * @param stretch cast=(Stretch)
 */
public static final native void TileBrush_Stretch(int sender, int stretch);
/**
 * @method flags=setter
 * @param sender cast=(TileBrush^),flags=gcobject
 * @param viewport cast=(Rect),flags=gcobject
 */
public static final native void TileBrush_Viewport(int sender, int viewport);
/**
 * @method flags=setter
 * @param sender cast=(TileBrush^),flags=gcobject
 * @param mode cast=(BrushMappingMode)
 */
public static final native void TileBrush_ViewportUnits(int sender, int mode);
/**
 * @method flags=setter
 * @param sender cast=(TileBrush^),flags=gcobject
 * @param value cast=(AlignmentX)
 */
public static final native void TileBrush_AlignmentX(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(TileBrush^),flags=gcobject
 * @param value cast=(AlignmentY)
 */
public static final native void TileBrush_AlignmentY(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(Timeline^),flags=gcobject
 */
public static final native void Timeline_AutoReverse(int sender, boolean autoReverse);
/**
 * @method flags=setter
 * @param sender cast=(Timeline^),flags=gcobject
 * @param duration cast=(Duration),flags=gcobject
 */
public static final native void Timeline_Duration(int sender, int duration);
/**
 * @method flags=setter
 * @param sender cast=(Timeline^),flags=gcobject
 * @param behavior cast=(RepeatBehavior),flags=gcobject
 */
public static final native void Timeline_RepeatBehavior(int sender, int behavior);
/** @method accessor=TimeSpan::FromMilliseconds(arg0),flags=const gcobject */
public static final native int TimeSpan_FromMilliseconds(double ms);
/**
 * @method flags=adder
 * @param sender cast=(ToggleButton^),flags=gcobject
 * @param handler cast=(RoutedEventHandler^),flags=gcobject
 */
public static final native void ToggleButton_Checked(int sender, int handler);
/** @method accessor=ToggleButton::CheckedEvent,flags=const gcobject */
public static final native int ToggleButton_CheckedEvent();
/** @method accessor=ToggleButton::IndeterminateEvent,flags=const gcobject */
public static final native int ToggleButton_IndeterminateEvent ();
/**
 * @method flags=getter
 * @param sender cast=(ToggleButton ^),flags=gcobject
 */
public static final native boolean ToggleButton_IsChecked(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ToggleButton ^),flags=gcobject
 */
public static final native void ToggleButton_IsChecked(int sender, boolean value);
/**
 * @method flags=no_gen setter
 * @param sender cast=(ToggleButton^),flags=gcobject
 */
public static final native void ToggleButton_IsCheckedNullSetter(int sender);
/** @method accessor=ToggleButton::IsCheckedProperty,flags=const gcobject */
public static final native int ToggleButton_IsCheckedProperty();
/** @method accessor=ToggleButton::IsThreeStateProperty,flags=const gcobject */
public static final native int ToggleButton_IsThreeStateProperty();
/**
 * @method flags=adder
 * @param sender cast=(ToggleButton^),flags=gcobject
 * @param handler cast=(RoutedEventHandler^),flags=gcobject
 */
public static final native void ToggleButton_Unchecked(int sender, int handler);
/** @method accessor=ToggleButton::UncheckedEvent,flags=const gcobject */
public static final native int ToggleButton_UncheckedEvent();
/**
 * @method flags=getter
 * @param sender cast=(ToolBar^),flags=gcobject
 */
public static final native int ToolBar_Band(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ToolBar^),flags=gcobject
 */
public static final native void ToolBar_Band(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(ToolBar^),flags=gcobject
 */
public static final native int ToolBar_BandIndex(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ToolBar^),flags=gcobject
 */
public static final native void ToolBar_BandIndex(int sender, int value);
/** @method accessor=ToolBar::BandProperty,flags=const gcobject */
public static final native int ToolBar_BandProperty();
/** @method accessor=ToolBar::ButtonStyleKey,flags=const gcobject */
public static final native int ToolBar_ButtonStyleKey();
/** @method accessor=ToolBar::CheckBoxStyleKey,flags=const gcobject */
public static final native int ToolBar_CheckBoxStyleKey();
/** @method accessor=ToolBar::RadioButtonStyleKey,flags=const gcobject */
public static final native int ToolBar_RadioButtonStyleKey();
/** @method accessor=ToolBar::SeparatorStyleKey,flags=const gcobject */
public static final native int ToolBar_SeparatorStyleKey();
/**
 * @method flags=getter
 * @param sender cast=(ToolBar^),flags=gcobject
 */
public static final native boolean ToolBar_HasOverflowItems(int sender);
/**
 * @method accessor=ToolBar::SetOverflowMode
 * @param element cast=(DependencyObject^),flags=gcobject
 * @param mode cast=(OverflowMode)
 */
public static final native void ToolBar_SetOverflowMode(int element, int mode);
/** @method accessor=ToolBar::typeid,flags=const gcobject */
public static final native int ToolBar_typeid();
/**
 * @method flags=setter
 * @param sender cast=(ToolBarTray^),flags=gcobject
 * @param value cast=(Brush^),flags=gcobject
 */
public static final native void ToolBarTray_Background(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(ToolBarTray^),flags=gcobject
 */
public static final native boolean ToolBarTray_IsLocked(int sender);
/**
 * @method flags=setter
 * @param sender cast=(ToolBarTray^),flags=gcobject
 */
public static final native void ToolBarTray_IsLocked(int sender, boolean value);
/**
 * @method flags=setter
 * @param sender cast=(ToolBarTray^),flags=gcobject
 * @param value cast=(Orientation)
 */
public static final native void ToolBarTray_Orientation(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(ToolBarTray^),flags=gcobject
 */
public static final native int ToolBarTray_ToolBars(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(TransformCollection^),flags=gcobject
 * @param transform cast=(Transform^),flags=gcobject
 */
public static final native void TransformCollection_Add(int sender, int transform);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(Transform^),flags=gcobject
 */
public static final native int Transform_Clone(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(TransformGroup^),flags=gcobject
 */
public static final native int TransformGroup_Children(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(TreeView^),flags=gcobject
 */
public static final native int TreeView_SelectedItem(int sender);
/**
 * @method flags=adder
 * @param sender cast=(TreeView^),flags=gcobject
 * @param handler cast=(RoutedPropertyChangedEventHandler<Object^>^),flags=gcobject
 */
public static final native void TreeView_SelectedItemChanged(int sender, int handler);
/** @method accessor=TreeView::typeid,flags=const gcobject */
public static final native int TreeView_typeid();
/** @method accessor=TreeViewItem::CollapsedEvent,flags=const gcobject */
public static final native int TreeViewItem_CollapsedEvent();
/** @method accessor=TreeViewItem::ExpandedEvent,flags=const gcobject */
public static final native int TreeViewItem_ExpandedEvent();
/**
 * @method flags=getter
 * @param sender cast=(TreeViewItem^),flags=gcobject
 */
public static final native boolean TreeViewItem_IsExpanded(int sender);
/**
 * @method flags=setter
 * @param sender cast=(TreeViewItem^),flags=gcobject
 */
public static final native void TreeViewItem_IsExpanded(int sender, boolean value);
/**
 * @method flags=getter
 * @param sender cast=(TreeViewItem^),flags=gcobject
 */
public static final native boolean TreeViewItem_IsSelected(int sender);
/**
 * @method flags=setter
 * @param sender cast=(TreeViewItem^),flags=gcobject
 */
public static final native void TreeViewItem_IsSelected(int sender, boolean value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(TreeViewItem^),flags=gcobject
 */
public static final native int TreeViewItem_HeaderTemplate(int sender);
/**
 * @method flags=setter
 * @param sender cast=(TreeViewItem^),flags=gcobject
 * @param value cast=(DataTemplate^),flags=gcobject
 */
public static final native void TreeViewItem_HeaderTemplate(int sender, int value);
/** @method accessor=TreeViewItem::HeaderTemplateProperty,flags=const gcobject */
public static final native int TreeViewItem_HeaderTemplateProperty();
/** @method accessor=TreeViewItem::typeid,flags=const gcobject */
public static final native int TreeViewItem_typeid();
/**
 * @method flags=cpp gcobject
 * @param sender cast=(Type^),flags=gcobject
 * @param name cast=(String^),flags=gcobject
 * @param bindingFlags cast=(BindingFlags)
 */
public static final native int Type_GetProperty(int sender, int name, int bindingFlags);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(TypeConverter^),flags=gcobject
 * @param string cast=(String^),flags=gcobject
 */
public static final native int TypeConverter_ConvertFromString(int sender, int string);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(TypeConverter^),flags=gcobject
 * @param object cast=(Object^),flags=gcobject
 */
public static final native int TypeConverter_ConvertToString(int sender, int object);
/**
 * @method accessor=TypeDescriptor::GetConverter,flags=gcobject
 * @param object cast=(Object^),flags=gcobject
 */
public static final native int TypeDescriptor_GetConverter(int object);
/**
 * @method flags=struct gcobject getter
 * @param sender cast=(Typeface^),flags=gcobject
 */
public static final native int Typeface_FontFamily(int sender);
/**
 * @method flags=struct gcobject getter
 * @param sender cast=(Typeface^),flags=gcobject
 */
public static final native int Typeface_Style(int sender);
/**
 * @method flags=struct gcobject getter
 * @param sender cast=(Typeface^),flags=gcobject
 */
public static final native int Typeface_Weight(int sender);
/**
 * @method flags=struct gcobject getter
 * @param sender cast=(Typeface^),flags=gcobject
 */
public static final native int Typeface_Stretch(int sender);
/**
 * @method flags=getter
 * @param sender cast=(System::Collections::Generic::ICollection<Typeface^>^),flags=gcobject
 */
public static final native int TypefaceCollection_Count(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Collections::Generic::IEnumerator<Typeface^>^),flags=gcobject
 */
public static final native int TypefaceCollection_Current(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(System::Collections::Generic::IEnumerable<Typeface^>^),flags=gcobject
 */
public static final native int TypefaceCollection_GetEnumerator(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(Type^),flags=gcobject
 * @param object cast=(Object^),flags=gcobject
 */
public static final native boolean Type_IsInstanceOfType(int sender, int object);
/**
 * @method flags=struct gcobject getter
 * @param sender cast=(Type^),flags=gcobject
 */
public static final native int Type_FullName(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(UIElementCollection^),flags=gcobject
 */
public static final native int UIElementCollection_default(int sender, int index);
/**
 * @method flags=cpp
 * @param sender cast=(UIElementCollection^),flags=gcobject
 * @param child cast=(UIElement^),flags=gcobject
 */
public static final native void UIElementCollection_Add(int sender, int child);
/**
 * @method flags=cpp
 * @param sender cast=(UIElementCollection^),flags=gcobject
 * @param child cast=(UIElement^),flags=gcobject
 */
public static final native void UIElementCollection_Insert(int sender, int index, int child);
/**
 * @method flags=cpp
 * @param sender cast=(UIElementCollection^),flags=gcobject
 * @param child cast=(UIElement^),flags=gcobject
 */
public static final native int UIElementCollection_IndexOf(int sender, int child);
/**
 * @method flags=cpp
 * @param sender cast=(UIElementCollection^),flags=gcobject
 * @param child cast=(UIElement^),flags=gcobject
 */
public static final native boolean UIElementCollection_Contains(int sender, int child);
/**
 * @method flags=cpp
 * @param sender cast=(UIElementCollection^),flags=gcobject
 */
public static final native void UIElementCollection_Clear(int sender);
/**
 * @method flags=getter
 * @param sender cast=(UIElementCollection^),flags=gcobject
 */
public static final native int UIElementCollection_Count(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(IEnumerator^),flags=gcobject
 */
public static final native int UIElementCollection_Current(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(IEnumerable^),flags=gcobject
 */
public static final native int UIElementCollection_GetEnumerator(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(UIElementCollection^),flags=gcobject
 * @param child cast=(UIElement^),flags=gcobject
 */
public static final native void UIElementCollection_Remove(int sender, int child);
/**
 * @method flags=cpp
 * @param sender cast=(UIElement^),flags=gcobject
 * @param event cast=(RoutedEvent^),flags=gcobject
 * @param handler cast=(Delegate^),flags=gcobject
 */
public static final native void UIElement_AddHandler(int sender, int event, int handler, boolean handledEventsToo);
/**
 * @method flags=setter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native void UIElement_AllowDrop(int sender, boolean value);
/**
 * @method flags=cpp
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native boolean UIElement_CaptureMouse(int sender);
/**
 * @method flags=setter
 * @param sender cast=(UIElement^),flags=gcobject
 * @param geometry cast=(Geometry^),flags=gcobject
 */
public static final native void UIElement_Clip(int sender, int geometry);
/**
 * @method flags=setter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native void UIElement_ClipToBounds(int sender, boolean value);
/** @method accessor=UIElement::ClipToBoundsProperty,flags=const gcobject */
public static final native int UIElement_ClipToBoundsProperty();
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(DragEventHandler^),flags=gcobject
 */
public static final native void UIElement_DragEnter(int sender, int handler);
/** @method accessor=UIElement::DragEnterEvent,flags=const gcobject */
public static final native int UIElement_DragEnterEvent();
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(DragEventHandler^),flags=gcobject
 */
public static final native void UIElement_DragLeave(int sender, int handler);
/** @method accessor=UIElement::DragLeaveEvent,flags=const gcobject */
public static final native int UIElement_DragLeaveEvent();
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(DragEventHandler^),flags=gcobject
 */
public static final native void UIElement_DragOver(int sender, int handler);
/** @method accessor=UIElement::DragOverEvent,flags=const gcobject */
public static final native int UIElement_DragOverEvent();
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(DragEventHandler^),flags=gcobject
 */
public static final native void UIElement_Drop(int sender, int handler);
/** @method accessor=UIElement::DropEvent,flags=const gcobject */
public static final native int UIElement_DropEvent();
/**
 * @method flags=cpp
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native boolean UIElement_Focus(int sender);
/**
 * @method flags=setter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native void UIElement_Focusable (int sender, boolean value);
/**
 * @method flags=cpp
 * @param sender cast=(UIElement^),flags=gcobject
 * @param dp cast=(DependencyProperty^),flags=gcobject
 * @param animation cast=(AnimationTimeline^),flags=gcobject
 */
public static final native void UIElement_BeginAnimation(int sender, int dp, int animation);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(UIElement^),flags=gcobject
 * @param point cast=(Point),flags=gcobject
 */
public static final native int UIElement_InputHitTest(int sender, int point);
/**
 * @method flags=cpp
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native void UIElement_InvalidateVisual(int sender);
/**
 * @method flags=getter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native boolean UIElement_IsEnabled(int sender);
/**
 * @method flags=setter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native void UIElement_IsEnabled(int sender, boolean enable);
/**
 * @method flags=getter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native boolean UIElement_IsFocused(int sender);
/**
 * @method flags=setter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native void UIElement_IsHitTestVisible(int sender, boolean value);
/**
 * @method flags=getter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native boolean UIElement_IsKeyboardFocused(int sender); 
/**
 * @method flags=getter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native boolean UIElement_IsKeyboardFocusWithin(int sender);
/**
 * @method flags=getter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native boolean UIElement_IsMeasureValid(int sender);
/**
 * @method flags=getter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native boolean UIElement_IsMouseOver(int sender);
/**
 * @method flags=getter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native boolean UIElement_IsVisible(int sender);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(KeyEventHandler^),flags=gcobject
 */
public static final native void UIElement_KeyUp(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(KeyEventHandler^),flags=gcobject
 */
public static final native void UIElement_KeyDown(int sender, int handler);
/**
 * @method flags=struct gcobject getter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native int UIElement_DesiredSize(int sender);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(GiveFeedbackEventHandler^),flags=gcobject
 */
public static final native void UIElement_GiveFeedback(int sender, int handler);
/** @method accessor=UIElement::GiveFeedbackEvent,flags=const gcobject */
public static final native int UIElement_GiveFeedbackEvent();
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(EventHandler^),flags=gcobject
 */
public static final native void UIElement_LayoutUpdated(int sender, int handler);
/**
 * @method flags=cpp
 * @param sender cast=(UIElement ^),flags=gcobject
 * @param availableSize cast=(Size),flags=gcobject
 */
public static final native void UIElement_Measure(int sender, int availableSize);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(MouseButtonEventHandler^),flags=gcobject
 */
public static final native void UIElement_MouseDown(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(MouseEventHandler^),flags=gcobject
 */
public static final native void UIElement_MouseEnter(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(MouseEventHandler^),flags=gcobject
 */
public static final native void UIElement_MouseLeave(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(MouseEventHandler^),flags=gcobject
 */
public static final native void UIElement_MouseMove(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(MouseWheelEventHandler^),flags=gcobject
 */
public static final native void UIElement_MouseWheel(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(MouseButtonEventHandler^),flags=gcobject
 */
public static final native void UIElement_MouseUp(int sender, int handler);
/**
 * @method flags=cpp
 * @param sender cast=(UIElement^),flags=gcobject
 * @param request cast=(TraversalRequest^),flags=gcobject
 */
public static final native void UIElement_MoveFocus(int sender, int request);
/**
 * @method flags=getter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native double UIElement_Opacity(int sender);
/**
 * @method flags=setter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native void UIElement_Opacity(int sender, double value);
/** @method accessor=UIElement::OpacityProperty,flags=const gcobject */
public static final native int UIElement_OpacityProperty();
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(KeyEventHandler^),flags=gcobject
 */
public static final native void UIElement_PreviewKeyDown(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(KeyEventHandler^),flags=gcobject
 */
public static final native void UIElement_PreviewKeyUp(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement ^),flags=gcobject
 * @param handler cast=(MouseButtonEventHandler^),flags=gcobject
 */
public static final native void UIElement_PreviewMouseDown(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement ^),flags=gcobject
 * @param handler cast=(MouseEventHandler^),flags=gcobject
 */
public static final native void UIElement_PreviewMouseMove(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement ^),flags=gcobject
 * @param handler cast=(MouseWheelEventHandler^),flags=gcobject
 */
public static final native void UIElement_PreviewMouseWheel(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement ^),flags=gcobject
 * @param handler cast=(MouseButtonEventHandler^),flags=gcobject
 */
public static final native void UIElement_PreviewMouseUp(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(KeyboardFocusChangedEventHandler^),flags=gcobject
 */
public static final native void UIElement_PreviewGotKeyboardFocus(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(KeyboardFocusChangedEventHandler^),flags=gcobject
 */
public static final native void UIElement_PreviewLostKeyboardFocus(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(KeyboardFocusChangedEventHandler^),flags=gcobject
 */
public static final native void UIElement_LostKeyboardFocus(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(TextCompositionEventHandler^),flags=gcobject
 */
public static final native void UIElement_PreviewTextInput(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(QueryContinueDragEventHandler^),flags=gcobject
 */
public static final native void UIElement_QueryContinueDrag(int sender, int handler);
/** @method accessor=UIElement::QueryContinueDragEvent,flags=const gcobject */
public static final native int UIElement_QueryContinueDragEvent();
/**
 * @method flags=gcobject getter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native int UIElement_RenderSize(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native void UIElement_ReleaseMouseCapture(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(UIElement^),flags=gcobject
 * @param routedEvent cast=(RoutedEvent^),flags=gcobject
 * @param handler cast=(Delegate^),flags=gcobject
 */
public static final native void UIElement_RemoveHandler(int sender, int routedEvent, int handler);
/**
 * @method flags=setter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native void UIElement_SnapsToDevicePixels(int sender, boolean value);
/**
 * @method flags=adder
 * @param sender cast=(UIElement^),flags=gcobject
 * @param handler cast=(TextCompositionEventHandler^),flags=gcobject
 */
public static final native void UIElement_TextInput(int sender, int handler);
/**
 * @method flags=struct cpp gcobject
 * @param sender cast=(UIElement^),flags=gcobject
 * @param point cast=(Point),flags=gcobject
 * @param relativeTo cast=(UIElement^),flags=gcobject
 */
public static final native int UIElement_TranslatePoint(int sender, int point, int relativeTo);
/**
 * @method flags=cpp
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native void UIElement_UpdateLayout(int sender);
/**
 * @method flags=getter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native byte UIElement_Visibility(int sender);
/**
 * @method flags=setter
 * @param sender cast=(UIElement^),flags=gcobject
 * @param visible cast=(Visibility)
 */
public static final native void UIElement_Visibility(int sender, byte visible);
/** @method accessor=UIElement::VisibilityProperty,flags=const gcobject */
public static final native int UIElement_VisibilityProperty();
/**
 * @method flags=getter
 * @param sender cast=(VirtualizingStackPanel^),flags=gcobject
 */
public static final native double VirtualizingStackPanel_VerticalOffset(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(Visual^),flags=gcobject
 * @param descendant cast=(DependencyObject^),flags=gcobject
 */
public static final native boolean Visual_IsAncestorOf(int sender, int descendant);
/**
 * @method flags=cpp
 * @param sender cast=(Visual^),flags=gcobject
 * @param ancestor cast=(DependencyObject^),flags=gcobject
 */
public static final native boolean Visual_IsDescendantOf(int sender, int ancestor);
/**
 * @method flags=struct cpp gcobject
 * @param sender cast=(Visual^),flags=gcobject
 * @param point cast=(Point),flags=gcobject
 */
public static final native int Visual_PointToScreen(int sender, int point);
/**
 * @method flags=struct cpp gcobject
 * @param sender cast=(Visual^),flags=gcobject
 * @param point cast=(Point),flags=gcobject
 */
public static final native int Visual_PointFromScreen(int sender, int point);
/**
 * @method accessor=VisualTreeHelper::GetChild,flags=gcobject
 * @param sender cast=(DependencyObject^),flags=gcobject
 */
public static final native int VisualTreeHelper_GetChild(int sender, int value);
/**
 * @method accessor=VisualTreeHelper::GetChildrenCount
 * @param sender cast=(DependencyObject^),flags=gcobject
 */
public static final native int VisualTreeHelper_GetChildrenCount(int sender);
/**
 * @method accessor=VisualTreeHelper::GetParent,flags=gcobject
 * @param sender cast=(DependencyObject^),flags=gcobject
 */
public static final native int VisualTreeHelper_GetParent(int sender);
/**
 * @method flags=getter
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 */
public static final native boolean WebBrowser_CanGoBack(int sender);
/**
 * @method flags=getter
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 */
public static final native boolean WebBrowser_CanGoForward(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 */
public static final native int WebBrowser_Document(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 */
public static final native int WebBrowser_DocumentText(int sender);
/**
 * @method flags=setter
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 * @param string cast=(String^),flags=gcobject
 */
public static final native void WebBrowser_DocumentText(int sender, int string);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 */
public static final native int WebBrowser_DocumentTitle(int sender);
/**
 * @method flags=adder
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 * @param handler cast=(System::Windows::Forms::WebBrowserDocumentCompletedEventHandler^),flags=gcobject
 */
public static final native void WebBrowser_DocumentCompleted(int sender, int handler);
/**
 * @method flags=cpp
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 */
public static final native boolean WebBrowser_GoBack(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 */
public static final native boolean WebBrowser_GoForward(int sender);
/**
 * @method flags=adder
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 * @param handler cast=(System::Windows::Forms::WebBrowserNavigatingEventHandler^),flags=gcobject
 */
public static final native void WebBrowser_Navigating(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 * @param handler cast=(System::Windows::Forms::WebBrowserNavigatedEventHandler^),flags=gcobject
 */
public static final native void WebBrowser_Navigated(int sender, int handler);
/**
 * @method flags=cpp
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 * @param urlString cast=(String^),flags=gcobject
 */
public static final native void WebBrowser_Navigate(int sender, int urlString);
/**
 * @method flags=adder
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 * @param handler cast=(System::Windows::Forms::WebBrowserProgressChangedEventHandler^),flags=gcobject
 */
public static final native void WebBrowser_ProgressChanged(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 * @param handler cast=(EventHandler^),flags=gcobject
 */
public static final native void WebBrowser_DocumentTitleChanged(int sender, int handler);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 */
public static final native int WebBrowser_StatusText(int sender);
/**
 * @method flags=adder
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 * @param handler cast=(EventHandler^),flags=gcobject
 */
public static final native void WebBrowser_StatusTextChanged(int sender, int handler);
/**
 * @method flags=getter
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 */
public static final native int WebBrowser_ReadyState(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 */
public static final native void WebBrowser_Refresh(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 */
public static final native void WebBrowser_Stop(int sender);
/**
 * @method flags=setter
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 */
public static final native void WebBrowser_ScriptErrorsSuppressed(int sender, boolean value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Windows::Forms::WebBrowser^),flags=gcobject
 */
public static final native int WebBrowser_Url(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Windows::Forms::WebBrowserNavigatingEventArgs^),flags=gcobject
 */
public static final native int WebBrowserNavigatingEventArgs_Url(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Windows::Forms::WebBrowserNavigatedEventArgs^),flags=gcobject
 */
public static final native int WebBrowserNavigatedEventArgs_Url(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(System::Windows::Forms::WebBrowserDocumentCompletedEventArgs^),flags=gcobject
 */
public static final native int WebBrowserDocumentCompletedEventArgs_Url(int sender);
/**
 * @method flags=getter
 * @param sender cast=(System::Windows::Forms::WebBrowserProgressChangedEventArgs^),flags=gcobject
 */
public static final native long WebBrowserProgressChangedEventArgs_CurrentProgress(int sender);
/**
 * @method flags=getter
 * @param sender cast=(System::Windows::Forms::WebBrowserProgressChangedEventArgs^),flags=gcobject
 */
public static final native long WebBrowserProgressChangedEventArgs_MaximumProgress(int sender);
/**
 * @method flags=getter
 * @param sender cast=(WindowCollection^),flags=gcobject
 */
public static final native int WindowCollection_Count(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(IEnumerator^),flags=gcobject
 */
public static final native int WindowCollection_Current(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(IEnumerable^),flags=gcobject
 */
public static final native int WindowCollection_GetEnumerator(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(Window^),flags=gcobject
 */
public static final native void Window_Activate(int sender);
/**
 * @method flags=cpp
 * @param sender cast=(Window^),flags=gcobject
 */
public static final native void Window_Close(int sender);
/**
 * @method flags=adder
 * @param sender cast=(Window^),flags=gcobject
 * @param handler cast=(CancelEventHandler^),flags=gcobject
 */
public static final native void Window_Closing(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(Window^),flags=gcobject
 * @param handler cast=(EventHandler^),flags=gcobject
 */
public static final native void Window_Activated(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(Window^),flags=gcobject
 * @param handler cast=(EventHandler^),flags=gcobject
 */
public static final native void Window_Deactivated(int sender, int handler);
/**
 * @method flags=adder
 * @param sender cast=(Window^),flags=gcobject
 * @param handler cast=(EventHandler^),flags=gcobject
 */
public static final native void Window_LocationChanged(int sender, int handler);
/**
 * @method accessor=Window::GetWindow,flags=gcobject
 * @param dependencyObject cast=(DependencyObject^),flags=gcobject
 */
public static final native int Window_GetWindow(int dependencyObject);
/**
 * @method flags=cpp
 * @param sender cast=(Window^),flags=gcobject
 */
public static final native void Window_Hide(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Window^),flags=gcobject
 * @param owner cast=(Window^),flags=gcobject
 */
public static final native void Window_Owner(int sender, int owner);
/**
 * @method flags=cpp
 * @param sender cast=(Window^),flags=gcobject
 */
public static final native void Window_Show(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Window^),flags=gcobject
 */
public static final native double Window_Left(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Window ^),flags=gcobject
 */
public static final native double Window_Top(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Window^),flags=gcobject
 */
public static final native void Window_Left(int sender, double left);
/**
 * @method flags=setter
 * @param sender cast=(Window ^),flags=gcobject
 */
public static final native void Window_Top(int sender, double top);
/**
 * @method flags=setter
 * @param sender cast=(Window^),flags=gcobject
 * @param icon cast=(ImageSource^),flags=gcobject
 */
public static final native void Window_Icon(int sender, int icon);
/**
 * @method flags=getter
 * @param sender cast=(Window^),flags=gcobject
 */
public static final native boolean Window_IsActive(int sender);
/**
 * @method flags=getter
 * @param sender cast=(Window^),flags=gcobject
 */
public static final native int Window_WindowState (int sender);
/**
 * @method flags=setter
 * @param sender cast=(Window^),flags=gcobject
 * @param windowState cast=(WindowState)
 */
public static final native void Window_WindowState (int sender, int windowState);
/**
 * @method flags=setter
 * @param sender cast=(Window^),flags=gcobject
 */
public static final native void Window_AllowsTransparency(int sender, boolean value);
/**
 * @method flags=getter
 * @param sender cast=(Window^),flags=gcobject
 */
public static final native int Window_WindowStyle(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Window^),flags=gcobject
 * @param value cast=(WindowStyle)
 */
public static final native void Window_WindowStyle(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(Window^),flags=gcobject
 */
public static final native void Window_ShowInTaskbar(int sender, boolean value);
/**
 * @method flags=setter
 * @param sender cast=(Window^),flags=gcobject
 * @param value cast=(ResizeMode)
 */
public static final native void Window_ResizeMode(int sender, int value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Window^),flags=gcobject
 */
public static final native int Window_Title(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Window^),flags=gcobject
 * @param string cast=(String^),flags=gcobject
 */
public static final native void Window_Title(int sender, int string);
/**
 * @method flags=setter
 * @param sender cast=(System::Windows::Forms::Integration::WindowsFormsHost^),flags=gcobject
 * @param child cast=(System::Windows::Forms::Control^),flags=gcobject
 */
public static final native void WindowsFormsHost_Child(int sender, int child);
/**
 * @method flags=cpp
 * @param sender cast=(WriteableBitmap^),flags=gcobject
 * @param sourceRect cast=(Int32Rect),flags=gcobject
 * @param buffer cast=(IntPtr)
 */
public static final native void WriteableBitmap_WritePixels(int sender, int sourceRect, byte[] buffer, int bufferSize, int stride);

/** @method flags=gcnew */
public static final native int gcnew_AccessText();
/** @method flags=gcnew */
public static final native int gcnew_Application();
/** @method flags=gcnew */
public static final native int gcnew_ArrayList(int count);
/**
 * @method flags=gcnew
 * @param point cast=(Point),flags=gcobject
 * @param size cast=(Size),flags=gcobject
 * @param sweepDirection cast=(SweepDirection)
 */
public static final native int gcnew_ArcSegment(int point, int size,  double rotationAngle, boolean isLargeArc, int sweepDirection, boolean isStroked);
/** @method flags=gcnew */
public static final native int gcnew_BitmapImage();
/**
 * @method flags=gcnew
 * @param point1 cast=(Point),flags=gcobject
 * @param point2 cast=(Point),flags=gcobject
 * @param point3 cast=(Point),flags=gcobject
 */
public static final native int gcnew_BezierSegment(int point1, int point2, int point3, boolean isScrolled);
/**
 * @method flags=gcnew
 * @param propertyPath cast=(String^),flags=gcobject
 */
public static final native int gcnew_Binding(int propertyPath);
/**
 * @method flags=gcnew
 * @param colors cast=(System::Collections::Generic::IList<Color>^),flags=gcobject
 */
public static final native int gcnew_BitmapPalette(int colors);
/**
 * @method accessor=System::Drawing::Bitmap,flags=gcnew
 * @param format cast=(System::Drawing::Imaging::PixelFormat)
 * @param scan0 cast=(IntPtr)
 */
public static final native int gcnew_Bitmap(int width, int height, int stride, int format, byte[] scan0);
/** @method flags=gcnew */
public static final native int gcnew_Button();
/** @method flags=gcnew */
public static final native int gcnew_Canvas();
/** @method flags=gcnew */
public static final native int gcnew_CheckBox();
/** @method accessor=System::Windows::Forms::ColorDialog,flags=gcnew */
public static final native int gcnew_ColorDialog();
/** @method accessor=System::Collections::Generic::List<Color>,flags=gcnew */
public static final native int gcnew_ColorList(int count);
/** @method flags=gcnew */
public static final native int gcnew_ComboBox();
/** @method flags=gcnew */
public static final native int gcnew_ComboBoxItem();
/** @method flags=gcnew */
public static final native int gcnew_ControlTemplate();
/**
 * @method flags=gcnew
 * @param source cast=(BitmapSource^),flags=gcobject
 * @param sourceRect cast=(Int32Rect),flags=gcobject
 */
public static final native int gcnew_CroppedBitmap(int source, int sourceRect);
/** @method flags=gcnew */
public static final native int gcnew_CharacterHit(int firstCharacterIndex, int trailingLength);
/**
 * @method flags=gcnew
 * @param geometryCombineMode cast=(GeometryCombineMode)
 * @param geometry1 cast=(Geometry^),flags=gcobject
 * @param geometry2 cast=(Geometry^),flags=gcobject
 */
public static final native int gcnew_CombinedGeometry(int geometryCombineMode, int geometry1, int geometry2); 
/** @method flags=gcnew */
public static final native int gcnew_CompositeCollection();
/** @method flags=gcnew */
public static final native int gcnew_ContextMenu();
/** @method flags=gcnew */
public static final native int gcnew_ContentControl(); 
/** @method flags=gcnew */
public static final native int gcnew_ColumnDefinition();
/** @method flags=gcnew */
public static final native int gcnew_DrawingVisual();
/** @method flags=gcnew */
public static final native int gcnew_DoubleAnimationUsingKeyFrames();
/**
 * @method flags=gcnew
 * @param dashes cast=(DoubleCollection^),flags=gcobject
 */
public static final native int gcnew_DashStyle(int dashes, double offset);
/** @method flags=gcnew */
public static final native int gcnew_DataObject();
/** @method flags=gcnew */
public static final native int gcnew_DataTemplate();
/** @method flags=gcnew */
public static final native int gcnew_DispatcherFrame();
/** @method flags=gcnew */
public static final native int gcnew_DispatcherTimer();
/**
 * @method flags=gcnew
 * @param keytime cast=(KeyTime),flags=gcobject
 */
public static final native int gcnew_DiscreteDoubleKeyFrame(double value, int keytime);
/** @method flags=gcnew */
public static final native int gcnew_DoubleCollection(int capacity);
/**
 * @method flags=gcnew
 * @param timespan cast=(TimeSpan),flags=gcobject
 */
public static final native int gcnew_Duration(int timespan);
/** @method flags=gcnew */
public static final native int gcnew_Expander();
/**
 * @method flags=gcnew
 * @param rect cast=(Rect),flags=gcobject
 */
public static final native int gcnew_EllipseGeometry(int rect);
/**
 * @method accessor=System::IO::FileInfo,flags=gcnew
 * @param path cast=(String^),flags=gcobject
 */
public static final native int gcnew_FileInfo(int path);
/** @method accessor=System::Windows::Forms::FolderBrowserDialog,flags=gcnew */
public static final native int gcnew_FolderBrowserDialog();
/**
 * @method accessor=System::Drawing::Font,flags=gcnew
 * @param fontFamily cast=(String^),flags=gcobject
 * @param fontStyle cast=(System::Drawing::FontStyle)
 */
public static final native int gcnew_Font(int fontFamily, float size, int fontStyle);
/** @method accessor=System::Windows::Forms::FontDialog,flags=gcnew */
public static final native int gcnew_FontDialog();
/**
 * @method flags=gcnew
 * @param str cast=(String^),flags=gcobject
 */
public static final native int gcnew_FontFamily(int str);
/**
 * @method flags=gcnew
 * @param source cast=(BitmapSource^),flags=gcobject
 * @param destinationFormat cast=(PixelFormat),flags=gcobject
 * @param destinationPalette cast=(BitmapPalette^),flags=gcobject
 */
public static final native int gcnew_FormatConvertedBitmap(int source, int destinationFormat, int destinationPalette, double alphaThreshold);
/**
 * @method flags=gcnew
 * @param string cast=(String^),flags=gcobject
 * @param culture cast=(CultureInfo^),flags=gcobject
 * @param flowDirection cast=(FlowDirection)
 * @param typeface cast=(Typeface^),flags=gcobject
 * @param brush cast=(Brush^),flags=gcobject
 */
public static final native int gcnew_FormattedText(int string, int culture, int flowDirection, int typeface, double emSize, int brush);
/** @method flags=gcnew */
public static final native int gcnew_Frame();
/**
 * @method flags=gcnew
 * @param type cast=(Type^),flags=gcobject
 */
public static final native int gcnew_FrameworkElementFactory(int type);
/**
 * @method flags=gcnew
 * @param type cast=(Type^),flags=gcobject
 * @param name cast=(String^),flags=gcobject
 */
public static final native int gcnew_FrameworkElementFactory(int type, int name);
/** @method flags=gcnew */
public static final native int gcnew_GeometryGroup();
/** @method flags=gcnew */
public static final native int gcnew_Grid();
/**
 * @method flags=gcnew
 * @param type cast=(GridUnitType)
 */
public static final native int gcnew_GridLength(double value, int type);
/** @method flags=gcnew */
public static final native int gcnew_GridView();
/** @method flags=gcnew */
public static final native int gcnew_GridViewColumn();
/** @method flags=gcnew */
public static final native int gcnew_GridViewColumnCollection();
/** @method flags=gcnew */
public static final native int gcnew_GridViewColumnHeader();
/** @method flags=gcnew */
public static final native int gcnew_GroupBox();
/**
 * @method flags=gcnew
 * @param inline cast=(Inline^),flags=gcobject
 */
public static final native int gcnew_Hyperlink(int inline);
//public static final native int gcnew_Icon(int stream);
/**
 * @method flags=gcnew
 * @param imageSource cast=(ImageSource^),flags=gcobject
 */
public static final native int gcnew_ImageBrush(int imageSource);
/** @method flags=gcnew */
public static final native int gcnew_Image();
/** @method flags=gcnew */
public static final native int gcnew_Int32(int value);
/** @method flags=gcnew */
public static final native int gcnew_Int32Rect(int x, int y, int width, int height);
/** @method flags=gcnew */
public static final native int gcnew_IntPtr(int value);
/** @method flags=gcnew */
public static final native int gcnew_Label();
/**
 * @method flags=gcnew
 * @param startColor cast=(Color),flags=gcobject
 * @param endColor cast=(Color),flags=gcobject
 */
public static final native int gcnew_LinearGradientBrush(int startColor, int endColor, double angle);
/**
 * @method flags=gcnew
 * @param startColor cast=(Color),flags=gcobject
 * @param endColor cast=(Color),flags=gcobject
 * @param startPoint cast=(Point),flags=gcobject
 * @param endPonit cast=(Point),flags=gcobject
 */
public static final native int gcnew_LinearGradientBrush(int startColor, int endColor, int startPoint, int endPonit);
/**
 * @method flags=gcnew
 * @param point cast=(Point),flags=gcobject
 */
public static final native int gcnew_LineSegment(int point, boolean isStroked);
/** @method flags=gcnew */
public static final native int gcnew_ListBox();
/** @method flags=gcnew */
public static final native int gcnew_ListBoxItem();
/** @method flags=gcnew */
public static final native int gcnew_ListView();
/** @method flags=gcnew */
public static final native int gcnew_ListViewItem();
/** @method flags=gcnew */
public static final native int gcnew_Matrix(double m11, double m12, double m21, double m22, double offsetX, double offsetY);
/**
 * @method flags=gcnew
 * @param matrix cast=(Matrix),flags=gcobject
 */
public static final native int gcnew_MatrixTransform(int matrix);
/** @method accessor=System::IO::MemoryStream,flags=gcnew */
public static final native int gcnew_MemoryStream();
/** @method flags=gcnew */
public static final native int gcnew_Menu();
/** @method flags=gcnew */
public static final native int gcnew_MenuItem();
/** @method accessor=System::Windows::Forms::NotifyIcon,flags=gcnew */
public static final native int gcnew_NotifyIcon();
/** @method flags=gcnew */
public static final native int gcnew_OpenFileDialog();
/** @method flags=gcnew */
public static final native int gcnew_PasswordBox();
/** @method flags=gcnew */
public static final native int gcnew_Path();
/** @method flags=gcnew */
public static final native int gcnew_PathFigure();
/** @method flags=gcnew */
public static final native int gcnew_PathGeometry();
/** @method flags=gcnew */
public static final native int gcnew_Pen();
/**
 * @method flags=gcnew
 * @param brush cast=(Brush^),flags=gcobject
 */
public static final native int gcnew_Pen(int brush, double thickness);
/**
 * @method flags=gcnew
 * @param points cast=(PointCollection^),flags=gcobject
 */
public static final native int gcnew_PolyLineSegment(int points, boolean isStroked);
/** @method flags=gcnew */
public static final native int gcnew_PointCollection(int capacity);
/** @method flags=gcnew */
public static final native int gcnew_Point(double x, double y);
/** @method flags=gcnew */
public static final native int gcnew_Popup();
/** @method flags=gcnew */
public static final native int gcnew_ProgressBar();
/**
 * @method flags=gcnew
 * @param point1 cast=(Point),flags=gcobject
 * @param point2 cast=(Point),flags=gcobject
 */
public static final native int gcnew_QuadraticBezierSegment(int point1, int point2, boolean isScrolled);
/** @method flags=gcnew */
public static final native int gcnew_RadioButton();
/** @method flags=gcnew */
public static final native int gcnew_Rect(double x, double y, double width, double height);
/**
 * @method flags=gcnew
 * @param rect cast=(Rect),flags=gcobject
 */
public static final native int gcnew_RectangleGeometry(int rect);
/**
 * @method flags=gcnew
 * @param relativeSourceMode cast=(RelativeSourceMode)
 */
public static final native int gcnew_RelativeSource(int relativeSourceMode);
/**
 * @method flags=gcnew
 * @param pixelFormat cast=(PixelFormat),flags=gcobject
 */
public static final native int gcnew_RenderTargetBitmap(int pixelWidth, int pixelHeight, double dpiX, double dpiY, int pixelFormat);
/** @method flags=gcnew */
public static final native int gcnew_RepeatBehavior(int repeatCount);
/** @method flags=gcnew */
public static final native int gcnew_RepeatButton();
/** @method accessor=System::Windows::Shapes::Rectangle,flags=gcnew */
public static final native int gcnew_Rectangle();
/** @method flags=gcnew */
public static final native int gcnew_RowDefinition();
/** @method flags=gcnew */
public static final native int gcnew_Run();
/** @method flags=gcnew */
public static final native int gcnew_SaveFileDialog();
/** @method flags=gcnew */
public static final native int gcnew_ScrollBar();
/** @method flags=gcnew */
public static final native int gcnew_ScrollViewer();
/** @method flags=gcnew */
public static final native int gcnew_Separator();
/**
 * @method flags=gcnew
 * @param dp cast=(DependencyProperty^),flags=gcobject
 * @param value cast=(Object^),flags=gcobject
 */
public static final native int gcnew_Setter(int dp, int value);
/**
 * @method accessor=System::Windows::Setter,flags=gcnew
 * @param dp cast=(DependencyProperty^),flags=gcobject
 * @param value cast=(Visibility)
 */
public static final native int gcnew_SetterVisibility(int dp, int value);
/** @method flags=gcnew */
public static final native int gcnew_Size();
/** @method flags=gcnew */
public static final native int gcnew_Size(double width, double height);
/** @method flags=gcnew */
public static final native int gcnew_Slider();
/** @method flags=gcnew */
public static final native int gcnew_ScaleTransform(double scaleX, double scaleY);
/**
 * @method flags=gcnew
 * @param color cast=(Color),flags=gcobject
 */
public static final native int gcnew_SolidColorBrush(int color);
/** @method flags=gcnew */
public static final native int gcnew_StackPanel();
/** @method flags=gcnew */
public static final native int gcnew_StreamGeometry();
/**
 * @method flags=gcnew
 * @param value cast=(const wchar_t *)
 */
public static final native int gcnew_String(char[] value);
/**
 * @method flags=gcnew
 * @param value cast=(const wchar_t *)
 */
public static final native int gcnew_String(char[] value, int startIndex, int length);
/** @method flags=gcnew */
public static final native int gcnew_Style();
/** @method flags=no_gen */
public static final native int gcnew_SWTCanvas(int jniRef);
/**
 * @method flags=no_gen gcnew
 * @param handle cast=(IntPtr)
 */
public static final native int gcnew_SWTSafeHandle(int handle, boolean isIcon);
//public static final native int gcnew_SWTDockPanel(int jniRef);
/** @method flags=no_gen gcnew */
public static final native int gcnew_SWTTextSource(int jniRef);
/**
 * @method flags=no_gen gcnew
 * @param properties cast=(TextRunProperties^),flags=gcobject
 */
public static final native int gcnew_SWTTextEmbeddedObject(int properties, int lenght, double width, double height, double baseline);
/**
 * @method flags=no_gen gcnew
 * @param typeface cast=(Typeface^),flags=gcobject
 * @param textDecorations cast=(TextDecorationCollection^),flags=gcobject
 * @param foregroundBrush cast=(Brush^),flags=gcobject
 * @param backgroundBrush cast=(Brush^),flags=gcobject
 * @param baselineAlignment cast=(BaselineAlignment)
 * @param culture cast=(CultureInfo^),flags=gcobject
 */
public static final native int gcnew_SWTTextRunProperties(int typeface, double size, double hittingSize, int textDecorations, int foregroundBrush, int backgroundBrush, int baselineAlignment, int culture);
/** @method flags=no_gen */
public static final native int gcnew_SWTTextParagraphProperties(int flowDirection, int textAlignment, boolean firstLineInParagraph, int defaultTextRunProperties, int textWrap, double lineHeight, double indent, int tabs);
/** @method flags=no_gen gcnew */
public static final native int gcnew_SWTTreeView(int jniRef);
/**
 * @method flags=no_gen gcnew
 * @param treeView cast=(TreeView^),flags=gcobject
 */
public static final native int gcnew_SWTTreeViewRowPresenter(int treeView);
/** @method flags=gcnew */
public static final native int gcnew_TabControl();
/** @method flags=gcnew */
public static final native int gcnew_TabItem();
/**
 * @method flags=gcnew
 * @param dp cast=(DependencyProperty^),flags=gcobject
 */
public static final native int gcnew_TemplateBindingExtension(int dp);
/** @method flags=gcnew */
public static final native int gcnew_TextDecorationCollection(int capacity); 
/**
 * @method flags=gcnew
 * @param location cast=(TextDecorationLocation)
 * @param pen cast=(Pen^),flags=gcobject
 * @param penOffsetUnit cast=(TextDecorationUnit)
 * @param penThicknessUnit cast=(TextDecorationUnit)
 */
public static final native int gcnew_TextDecoration(int location, int pen, double penOffset, int penOffsetUnit, int penThicknessUnit);
/**
 * @method flags=gcnew
 * @param alignment cast=(TextTabAlignment)
 */
public static final native int gcnew_TextTabProperties(int alignment, double location, int tabLeader, int aligningChar);
/** @method accessor=System::Collections::Generic::List<TextTabProperties^>,flags=gcnew */
public static final native int gcnew_TextTabPropertiesCollection(int capacity);
/** @method flags=gcnew */
public static final native int gcnew_TextBlock();
/** @method flags=gcnew */
public static final native int gcnew_TextBox();
/**
 * @method flags=gcnew
 * @param string cast=(String^),flags=gcobject
 * @param textRunProperties cast=(TextRunProperties^),flags=gcobject
 */
public static final native int gcnew_TextCharacters(int string, int offsetToFirstChar, int length, int textRunProperties);
/**
 * @method flags=gcnew
 * @param textRunProperties cast=(TextRunProperties^),flags=gcobject
 */
public static final native int gcnew_TextEndOfLine(int length, int textRunProperties);
/**
 * @method flags=gcnew
 * @param textRunProperties cast=(TextRunProperties^),flags=gcobject
 */
public static final native int gcnew_TextEndOfParagraph(int length, int textRunProperties);
/** @method flags=gcnew */
public static final native int gcnew_TiffBitmapEncoder();
/** @method flags=gcnew */
public static final native int gcnew_TimeSpan(long ticks);
/** @method flags=gcnew */
public static final native int gcnew_Thickness(double left, double top, double right, double bottom);
/** @method flags=gcnew */
public static final native int gcnew_ToggleButton();
/** @method flags=gcnew */
public static final native int gcnew_ToolBar();
/** @method flags=gcnew */
public static final native int gcnew_ToolBarTray();
/** @method flags=gcnew */
public static final native int gcnew_TransformGroup();
/** @method flags=gcnew */
public static final native int gcnew_TranslateTransform(double offsetX, double offsetY);
/**
 * @method flags=gcnew
 * @param direction cast=(FocusNavigationDirection)
 */
public static final native int gcnew_TraversalRequest(int direction);
/** @method flags=gcnew */
public static final native int gcnew_TreeView();
/** @method flags=gcnew */
public static final native int gcnew_TreeViewItem();
/**
 * @method flags=gcnew
 * @param fontFamily cast=(FontFamily^),flags=gcobject
 * @param style cast=(FontStyle),flags=gcobject
 * @param weight cast=(FontWeight),flags=gcobject
 * @param stretch cast=(FontStretch),flags=gcobject
 */
public static final native int gcnew_Typeface(int fontFamily, int style, int weight, int stretch);
/** @method flags=gcnew */
public static final native int gcnew_UserControl();
/**
 * @method flags=gcnew
 * @param str cast=(String^),flags=gcobject
 * @param type cast=(UriKind)
 */
public static final native int gcnew_Uri(int str, int type);
/** @method accessor=System::Windows::Forms::WebBrowser,flags=gcnew */
public static final native int gcnew_WebBrowser();
/** @method accessor=System::Windows::Forms::Integration::WindowsFormsHost,flags=gcnew */
public static final native int gcnew_WindowsFormsHost();
/** @method flags=gcnew */
public static final native int gcnew_Window();
/**
 * @method flags=gcnew
 * @param source cast=(BitmapSource^),flags=gcobject
 */
public static final native int gcnew_WriteableBitmap (int source);
/**
 * @method flags=gcnew
 * @param pixelFormat cast=(PixelFormat),flags=gcobject
 * @param palette cast=(BitmapPalette^),flags=gcobject
 */
public static final native int gcnew_WriteableBitmap (int pixelWidth, int pixelHeight, double dpiX, double dpiY, int pixelFormat, int palette);
/** @method flags=no_gen */
public static final native void memcpy(char[] dest, int src, int size);
/**
 * @method flags=no_gen
 * @param dest flags=no_in critical
 * @param src cast=(array<Byte>^),flags=gcobject
 */
public static final native void memcpy(byte[] dest, int src, int size);
/**
 * @method flags=no_gen
 * @param src cast=(array<Byte>^),flags=gcobject
 * @param dest flags=no_out critical
 */
public static final native void memcpy(int src, byte[] dest, int size);


//Demo
/**
 * @method flags=gcobject getter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 */
public static final native int FrameworkElement_Resources(int sender);
/**
 * @method flags=setter
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param value cast=(ResourceDictionary^),flags=gcobject
 */
public static final native void FrameworkElement_Resources(int sender, int value);
/**
 * @method accessor=System::IO::StringReader,flags=gcnew
 * @param string cast=(String^),flags=gcobject
 */
public static final native int gcnew_StringReader(int string);
/**
 * @method accessor=XmlReader::Create,flags=gcobject
 * @param stream cast=(System::IO::TextReader^),flags=gcobject
 */
public static final native int XmlReader_Create(int stream);
/**
 * @method accessor=XamlReader::Load,flags=gcobject
 * @param stream cast=(XmlReader^),flags=gcobject
 */
public static final native int XamlReader_Load(int stream);
/** @method flags=gcnew */
public static final native int gcnew_ResourceDictionary();
/**
 * @method flags=setter
 * @param sender cast=(ResourceDictionary^),flags=gcobject
 * @param uri cast=(Uri^),flags=gcobject
 */
public static final native void ResourceDictionary_Source(int sender, int uri);



/** @method flags=gcnew */
public static final native int gcnew_DiscreteDoubleKeyFrame();
/** @method flags=gcnew */
public static final native int gcnew_LinearDoubleKeyFrame();
/**
 * @method flags=gcnew
 * @param parameter cast=(Object^),flags=gcobject
 */
public static final native int gcnew_PropertyPath(int parameter);
/** @method flags=gcnew */
public static final native int gcnew_SplineDoubleKeyFrame();
/** @method flags=gcnew */
public static final native int gcnew_Storyboard();
/** @method flags=no_gen gcnew */
public static final native int gcnew_SWTAnimator(int jniRef);
/**
 * @method flags=setter
 * @param sender cast=(DoubleKeyFrame^),flags=gcobject
 */
public static final native void DoubleKeyFrame_Value(int sender, double value);
/**
 * @method flags=setter
 * @param sender cast=(DoubleKeyFrame^),flags=gcobject
 * @param value cast=(KeyTime),flags=gcobject
 */
public static final native void DoubleKeyFrame_KeyTime(int sender, int value);
/**
 * @method accessor=KeyTime::FromTimeSpan,flags=gcobject
 * @param timeSpan cast=(TimeSpan),flags=gcobject
 */
public static final native int KeyTime_FromTimeSpan(int timeSpan);
/**
 * @method flags=cpp
 * @param sender cast=(Storyboard^),flags=gcobject
 * @param containingObject cast=(FrameworkElement^),flags=gcobject
 */
public static final native void Storyboard_Begin(int sender, int containingObject, boolean isControllable);
/**
 * @method flags=cpp
 * @param sender cast=(Storyboard^),flags=gcobject
 * @param containingObject cast=(FrameworkElement^),flags=gcobject
 */
public static final native void Storyboard_Pause(int sender, int containingObject);
/**
 * @method flags=cpp
 * @param sender cast=(Storyboard^),flags=gcobject
 * @param containingObject cast=(FrameworkElement^),flags=gcobject
 */
public static final native void Storyboard_Resume(int sender, int containingObject);
/**
 * @method flags=cpp
 * @param sender cast=(Storyboard^),flags=gcobject
 * @param containingObject cast=(FrameworkElement^),flags=gcobject
 */
public static final native void Storyboard_Stop(int sender, int containingObject);
/**
 * @method flags=gcobject getter
 * @param sender cast=(TimelineGroup^),flags=gcobject
 */
public static final native int TimelineGroup_Children(int sender);
/** @method accessor=SWTAnimator::DoubleValueProperty,flags=no_gen gcobject const */
public static final native int SWTAnimator_DoubleValueProperty();
/** @method accessor=SWTAnimator::IntValueProperty,flags=no_gen gcobject const */
public static final native int SWTAnimator_IntValueProperty();
/**
 * @method flags=cpp
 * @param sender cast=(FrameworkElement^),flags=gcobject
 * @param name cast=(String^),flags=gcobject
 * @param scopedElement cast=(Object^),flags=gcobject
 */
public static final native void FrameworkElement_RegisterName(int sender, int name, int scopedElement);
/**
 * @method accessor=NewValue,flags=getter
 * @param sender cast=(DependencyPropertyChangedEventArgs^),flags=gcobject
 */
public static final native double DependencyPropertyChangedEventArgs_NewValueDouble(int sender);
/**
 * @method accessor=OldValue,flags=getter
 * @param sender cast=(DependencyPropertyChangedEventArgs^),flags=gcobject
 */
public static final native double DependencyPropertyChangedEventArgs_OldValueDouble(int sender);
/**
 * @method accessor=NewValue,flags=getter
 * @param sender cast=(DependencyPropertyChangedEventArgs^),flags=gcobject
 */
public static final native int DependencyPropertyChangedEventArgs_NewValueInt(int sender);
/**
 * @method accessor=OldValue,flags=getter
 * @param sender cast=(DependencyPropertyChangedEventArgs^),flags=gcobject
 */
public static final native int DependencyPropertyChangedEventArgs_OldValueInt(int sender);
/**
 * @method accessor=Storyboard::SetTargetName
 * @param element cast=(DependencyObject^),flags=gcobject
 * @param name cast=(String^),flags=gcobject
 */
public static final native void Storyboard_SetTargetName(int element, int name);
/**
 * @method accessor=Storyboard::SetTargetProperty
 * @param element cast=(DependencyObject^),flags=gcobject
 * @param propertyPath cast=(PropertyPath^),flags=gcobject
 */
public static final native void Storyboard_SetTargetProperty(int element, int propertyPath);
/** @method flags=gcnew */
public static final native int gcnew_NameScope();
/**
 * @method accessor=NameScope::GetNameScope,flags=gcobject
 * @param dependencyObject cast=(DependencyObject^),flags=gcobject
 */
public static final native int NameScope_GetNameScope(int dependencyObject);
/**
 * @method accessor=NameScope::SetNameScope
 * @param dependencyObject cast=(DependencyObject^),flags=gcobject
 * @param nameScope cast=(INameScope^),flags=gcobject
 */
public static final native void NameScope_SetNameScope(int dependencyObject, int nameScope);
/** @method flags=gcnew */
public static final native int gcnew_KeySpline(double x1, double y1, double x2, double y2);
/**
 * @method flags=setter
 * @param sender cast=(SplineDoubleKeyFrame^),flags=gcobject
 * @param value cast=(KeySpline^),flags=gcobject
 */
public static final native void SplineDoubleKeyFrame_KeySpline(int sender, int value);
/**
 * @method flags=adder
 * @param sender cast=(Timeline^),flags=gcobject
 * @param handler cast=(EventHandler^),flags=gcobject
 */
public static final native void Timeline_Completed(int sender, int handler);
/** @method flags=gcnew */
public static final native int gcnew_Int32AnimationUsingKeyFrames();
/** @method flags=gcnew */
public static final native int gcnew_DiscreteInt32KeyFrame();
/** @method flags=gcnew */
public static final native int gcnew_LinearInt32KeyFrame();
/** @method flags=gcnew */
public static final native int gcnew_SplineInt32KeyFrame();
/**
 * @method flags=gcobject getter
 * @param sender cast=(Int32AnimationUsingKeyFrames^),flags=gcobject
 */
public static final native int Int32AnimationUsingKeyFrames_KeyFrames(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Int32KeyFrame^),flags=gcobject
 */
public static final native void Int32KeyFrame_Value(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(SplineInt32KeyFrame^),flags=gcobject
 * @param value cast=(KeySpline^),flags=gcobject
 */
public static final native void SplineInt32KeyFrame_KeySpline(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(Int32KeyFrame^),flags=gcobject
 * @param value cast=(KeyTime),flags=gcobject
 */
public static final native void Int32KeyFrame_KeyTime(int sender, int value);

/**
 * @method flags=gcobject getter
 * @param sender cast=(Timeline^),flags=gcobject
 */
public static final native int Timeline_Duration(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Duration^),flags=gcobject
 */
public static final native int Duration_TimeSpan(int sender);
/**
 * @method flags=getter
 * @param sender cast=(TimeSpan^),flags=gcobject
 */
public static final native double TimeSpan_TotalMilliseconds(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(Timeline^),flags=gcobject
 */
public static final native int Timeline_BeginTime(int sender);

/** @method flags=gcnew */
public static final native int gcnew_OuterGlowBitmapEffect();
/**
 * @method flags=gcobject getter
 * @param sender cast=(OuterGlowBitmapEffect^),flags=gcobject
 */
public static final native int OuterGlowBitmapEffect_GlowColor(int sender);
/**
 * @method flags=setter
 * @param sender cast=(OuterGlowBitmapEffect^),flags=gcobject
 * @param value cast=(Color),flags=gcobject
 */
public static final native void OuterGlowBitmapEffect_GlowColor(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(OuterGlowBitmapEffect^),flags=gcobject
 */
public static final native double OuterGlowBitmapEffect_GlowSize(int sender);
/**
 * @method flags=setter
 * @param sender cast=(OuterGlowBitmapEffect^),flags=gcobject
 */
public static final native void OuterGlowBitmapEffect_GlowSize(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(OuterGlowBitmapEffect^),flags=gcobject
 */
public static final native double OuterGlowBitmapEffect_Opacity(int sender);
/**
 * @method flags=setter
 * @param sender cast=(OuterGlowBitmapEffect^),flags=gcobject
 */
public static final native void OuterGlowBitmapEffect_Opacity(int sender, double value);
/**
 * @method flags=gcobject getter
 * @param sender cast=(UIElement^),flags=gcobject
 */
public static final native int UIElement_BitmapEffect(int sender);
/**
 * @method flags=setter
 * @param sender cast=(UIElement^),flags=gcobject
 * @param value cast=(BitmapEffect^),flags=gcobject
 */
public static final native void UIElement_BitmapEffect(int sender, int value);
/** @method flags=gcnew */
public static final native int gcnew_DropShadowBitmapEffect();
/**
 * @method flags=gcobject getter
 * @param sender cast=(DropShadowBitmapEffect^),flags=gcobject
 */
public static final native int DropShadowBitmapEffect_Color(int sender);
/**
 * @method flags=setter
 * @param sender cast=(DropShadowBitmapEffect^),flags=gcobject
 * @param value cast=(Color),flags=gcobject
 */
public static final native void DropShadowBitmapEffect_Color(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(DropShadowBitmapEffect^),flags=gcobject
 */
public static final native double DropShadowBitmapEffect_Direction(int sender);
/**
 * @method flags=setter
 * @param sender cast=(DropShadowBitmapEffect^),flags=gcobject
 */
public static final native void DropShadowBitmapEffect_Direction(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(DropShadowBitmapEffect^),flags=gcobject
 */
public static final native double DropShadowBitmapEffect_Opacity(int sender);
/**
 * @method flags=setter
 * @param sender cast=(DropShadowBitmapEffect^),flags=gcobject
 */
public static final native void DropShadowBitmapEffect_Opacity(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(DropShadowBitmapEffect^),flags=gcobject
 */
public static final native double DropShadowBitmapEffect_ShadowDepth(int sender);
/**
 * @method flags=setter
 * @param sender cast=(DropShadowBitmapEffect^),flags=gcobject
 */
public static final native void DropShadowBitmapEffect_ShadowDepth(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(DropShadowBitmapEffect^),flags=gcobject
 */
public static final native double DropShadowBitmapEffect_Softness(int sender);
/**
 * @method flags=setter
 * @param sender cast=(DropShadowBitmapEffect^),flags=gcobject
 */
public static final native void DropShadowBitmapEffect_Softness(int sender, double value);
/** @method flags=gcnew */
public static final native int gcnew_BlurBitmapEffect();
/**
 * @method flags=setter
 * @param sender cast=(BlurBitmapEffect^),flags=gcobject
 */
public static final native void BlurBitmapEffect_Radius(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(BlurBitmapEffect^),flags=gcobject
 */
public static final native double BlurBitmapEffect_Radius(int sender);
/** @method flags=gcnew */
public static final native int gcnew_BevelBitmapEffect();
/**
 * @method flags=getter
 * @param handle cast=(BevelBitmapEffect^),flags=gcobject
 */
public static final native double BevelBitmapEffect_LightAngle(int handle);
/**
 * @method flags=setter
 * @param handle cast=(BevelBitmapEffect^),flags=gcobject
 */
public static final native void BevelBitmapEffect_LightAngle(int handle, double value);
/**
 * @method flags=getter
 * @param handle cast=(BevelBitmapEffect^),flags=gcobject
 */
public static final native double BevelBitmapEffect_BevelWidth(int handle);
/**
 * @method flags=setter
 * @param handle cast=(BevelBitmapEffect^),flags=gcobject
 */
public static final native void BevelBitmapEffect_BevelWidth(int handle, double value);
/**
 * @method flags=getter
 * @param handle cast=(BevelBitmapEffect^),flags=gcobject
 */
public static final native double BevelBitmapEffect_Smoothness(int handle);
/**
 * @method flags=setter
 * @param handle cast=(BevelBitmapEffect^),flags=gcobject
 */
public static final native void BevelBitmapEffect_Smoothness(int handle, double value);
/** @method flags=gcnew */
public static final native int gcnew_BitmapEffectGroup();
/**
 * @method flags=gcobject getter
 * @param sender cast=(BitmapEffectGroup^),flags=gcobject
 */
public static final native int BitmapEffectGroup_Children(int sender);

/**
 * @method flags=gcobject getter
 * @param sender cast=(HwndSource^),flags=gcobject
 */
public static final native int HwndSource_CompositionTarget(int sender);
/**
 * @method flags=setter
 * @param sender cast=(HwndTarget^),flags=gcobject
 * @param value cast=(Color),flags=gcobject
 */
public static final native void HwndTarget_BackgroundColor(int sender, int value);

/**
 * @method accessor=TypeDescriptor::GetProperties,flags=gcobject
 * @param sender cast=(Object^),flags=gcobject
 */
public static final native int TypeDescriptor_GetProperties(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(MemberDescriptor^),flags=gcobject
 */
public static final native int MemberDescriptor_Name(int sender);
/**
 * @method flags=gcobject getter
 * @param sender cast=(DependencyPropertyDescriptor^),flags=gcobject
 */
public static final native int DependencyPropertyDescriptor_DependencyProperty(int sender);
/** @method accessor=DependencyPropertyDescriptor::typeid,flags=const gcobject */
public static final native int DependencyPropertyDescriptor_typeid();
/**
 * @method accessor=DependencyPropertyDescriptor::FromProperty,flags=gcobject
 * @param propertyDescriptor cast=(PropertyDescriptor^),flags=gcobject
 */
public static final native int DependencyPropertyDescriptor_FromProperty(int propertyDescriptor);
/**
 * @method accessor=Type::GetType,flags=gcobject
 * @param typeName cast=(String^),flags=gcobject
 */
public static final native int Type_GetType(int typeName, boolean throwOnError, boolean ignoreCase);
/**
 * @method flags=cpp
 * @param sender cast=(NameScope^),flags=gcobject
 * @param name cast=(String^),flags=gcobject
 * @param scopedElement cast=(Object^),flags=gcobject
 */
public static final native void NameScope_RegisterName(int sender, int name, int scopedElement);

/** @method accessor=Panel::HeightProperty,flags=const gcobject */
public static final native int Panel_HeightProperty();
/** @method accessor=Panel::WidthProperty,flags=const gcobject */
public static final native int Panel_WidthProperty();
/** @method accessor=Canvas::TopProperty,flags=const gcobject */
public static final native int Canvas_TopProperty();
/** @method accessor=Canvas::LeftProperty,flags=const gcobject */
public static final native int Canvas_LeftProperty();
/** @method accessor=OuterGlowBitmapEffect::GlowSizeProperty,flags=const gcobject */
public static final native int OuterGlowBitmapEffect_GlowSizeProperty();
/** @method flags=gcnew */
public static final native int gcnew_Int32Animation();
/** @method flags=gcnew */
public static final native int gcnew_DoubleAnimation();
/**
 * @method flags=no_gen setter gcobject
 * @param sender cast=(Timeline^),flags=gcobject
 * @param value cast=(TimeSpan^),flags=gcobject
 */
public static final native void Timeline_BeginTime(int sender, int value);
/**
 * @method flags=setter
 * @param sender cast=(DoubleAnimation^),flags=gcobject
 */
public static final native void DoubleAnimation_To(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(DoubleAnimation^),flags=gcobject
 */
public static final native double DoubleAnimation_To(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Int32Animation^),flags=gcobject
 */
public static final native void Int32Animation_To(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(Int32Animation^),flags=gcobject
 */
public static final native int Int32Animation_To(int sender);
/**
 * @method flags=setter
 * @param sender cast=(DoubleAnimation^),flags=gcobject
 */
public static final native void DoubleAnimation_From(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(DoubleAnimation^),flags=gcobject
 */
public static final native double DoubleAnimation_From(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Int32Animation^),flags=gcobject
 */
public static final native void Int32Animation_From(int sender, int value);
/**
 * @method flags=getter
 * @param sender cast=(Int32Animation^),flags=gcobject
 */
public static final native int Int32Animation_From(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Timeline^),flags=gcobject
 */
public static final native void Timeline_DecelerationRatio(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(Timeline^),flags=gcobject
 */
public static final native double Timeline_DecelerationRatio(int sender);
/**
 * @method flags=setter
 * @param sender cast=(Timeline^),flags=gcobject
 */
public static final native void Timeline_AccelerationRatio(int sender, double value);
/**
 * @method flags=getter
 * @param sender cast=(Timeline^),flags=gcobject
 */
public static final native double Timeline_AccelerationRatio(int sender);

/** @method flags=no_gen gcnew */
public static final native int gcnew_SWTAnimation(int jniRef);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(Type^),flags=gcobject
 * @param name cast=(String^),flags=gcobject
 * @param bindingFlags cast=(BindingFlags)
 */
public static final native int Type_GetMethod(int sender, int name, int bindingFlags);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(ArrayList^),flags=gcobject
 */
public static final native int ArrayList_ToArray(int sender);
/**
 * @method flags=cpp gcobject
 * @param sender cast=(MethodInfo^),flags=gcobject
 * @param obj cast=(Object^),flags=gcobject
 * @param parameters cast=(array<Object^>^),flags=gcobject
 */
public static final native int MethodInfo_Invoke(int sender, int obj, int parameters);
}