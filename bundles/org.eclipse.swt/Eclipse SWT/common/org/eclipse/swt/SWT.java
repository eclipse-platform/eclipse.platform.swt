package org.eclipse.swt;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;

/**
 * This class provides access to a small number of SWT system-wide
 * methods, and in addition defines the public constants provided
 * by SWT.
 * <p>
 * By defining constants like UP and DOWN in a single class, SWT
 * can share common names and concepts at the same time minimizing
 * the number of classes, names and constants for the application
 * programmer.
 * </p><p>
 * Note that, some of the constants provided by this class represent
 * optional, appearance related aspects of widgets which are available
 * either only on some window systems, or for a differing set of
 * widgets on each window system. These constants are marked
 * as <em>HINT</em>s. The set of widgets which support a particular
 * <em>HINT</em> may change from release to release, although we typically
 * will not withdraw support for a <em>HINT</em> once it is made available.
 * </p>
 */
 
/* NOTE:
 *   Good javadoc coding style is to put the values of static final 
 *   constants in the comments. This re-inforces the fact that
 *   consumers are allowed to rely on the value (and they must
 *   since the values are compiled inline in their code). We
 *   can <em>not</em> change the values of these constants between
 *   releases.
 */
public class SWT {
	
	/* Widget Event Constants */
	
	/**
	 * key down event type (value is 1)
	 */
	public static final int KeyDown = 1;
	
	/**
	 * key up event type (value is 2)
	 */
	public static final int KeyUp = 2;
	
	/**
	 * mouse down event type (value is 3)
	 */
	public static final int MouseDown = 3;
	
	/**
	 * mouse up event type (value is 4)
	 */
	public static final int MouseUp = 4;
	
	/**
	 * mouse move event type (value is 5)
	 */
	public static final int MouseMove = 5;
	
	/**
	 * mouse enter event type (value is 6)
	 */
	public static final int MouseEnter = 6;		
	
	/**
	 * Mouse exit event type (value is 7)
	 */
	public static final int MouseExit = 7;
	
	/**
	 * mouse double click event type (value is 8)
	 */
	public static final int MouseDoubleClick = 8;	
	
	/**
	 * paint event type (value is 9)
	 */
	public static final int Paint = 9;	
	
	/**
	 * move event type (value is 10)
	 */
	public static final int Move = 10;
	
	/**
	 * resize event type (value is 11)
	 */
	public static final int Resize = 11;
	
	/**
	 * dispose event type (value is 12)
	 */
	public static final int Dispose = 12;
	
	/**
	 * selection event type (value is 13)
	 */
	public static final int Selection = 13;
	
	/**
	 * default selection event type (value is 14)
	 */
	public static final int DefaultSelection = 14;
	
	/**
	 * focus in event type (value is 15)
	 */
	public static final int FocusIn = 15;
	
	/**
	 * focus out event type (value is 16)
	 */
	public static final int FocusOut = 16;
	
	/**
	 * expand event type (value is 17)
	 */
	public static final int Expand = 17;
	
	/**
	 * collapse event type (value is 18)
	 */
	public static final int Collapse = 18;
	
	/**
	 * iconify event type (value is 19)
	 */
	public static final int Iconify = 19;
	
	/**
	 * de-iconify event type (value is 20)
	 */
	public static final int Deiconify = 20;
	
	/**
	 * close event type (value is 21)
	 */
	public static final int Close = 21;
	
	/**
	 * show event type (value is 22)
	 */
	public static final int Show = 22;
	
	/**
	 * hide event type (value is 23)
	 */
	public static final int Hide = 23;
	
	/**
	 * modify event type (value is 24)
	 */
	public static final int Modify = 24;
	
	/**
	 * verify event type (value is 25)
	 */
	public static final int Verify = 25;
	
	/**
	 * activate event type (value is 26)
	 */
	public static final int Activate = 26;
	
	/**
	 * deactivate event type (value is 27)
	 */
	public static final int Deactivate = 27;	
	
	/**
	 * help event type (value is 28)
	 */
	public static final int Help = 28;
	
	/**
	 * drag detect event type (value is 29)
	 */
	public static final int DragDetect = 29;
	
	/**
	 * arm event type (value is 30)
	 */
	public static final int Arm = 30;
	
	/**
	 * traverse event type (value is 31)
	 */
	public static final int Traverse = 31;
	
	/**
	 * mouse hover event type (value is 32)
	 */
	public static final int MouseHover = 32;

	/**
	 * hardware key down event type (value is 33)
	 */
	public static final int HardKeyDown = 33;
	
	/**
	 * hardware key up event type (value is 34)
	 */
	public static final int HardKeyUp = 34;

	/* Event Details */
	
	/**
	 * a constant known to be zero (0), used in operations which
	 * take bit flags to indicate that "no bits are set"
	 */
	public static final int NONE = 0;
	
	/**
	 * indicates that a user-interface component is being dragged,
	 * for example dragging the thumb of a scroll bar (value is 1)
	 */
	public static final int DRAG = 1;
	
	/**
	 * a constant known to be zero (0), used in operations which
	 * take pointers to indicate a null argument
	 */
	public static final int NULL = 0;
	
	/**
	 * indicates that a default should be used (value is -1)
	 */
	public static final int DEFAULT = -1;

	/**
	 * <code>Menu</code> style constant for menu bar behavior (value is 1&lt;&lt;1)
	 */
	public static final int BAR = 1 << 1;

	/**
	 * <code>Menu</code> style constant for drop down menu behavior (value is 1&lt;&lt;2)
	 */
	public static final int DROP_DOWN = 1 << 2;

	/**
	 * <code>Menu</code> style constant for pop up menu behavior (value is 1&lt;&lt;3)
	 */
	public static final int POP_UP = 1 << 3;

	/**
	 * <code>MenuItem</code> style constant for line separator behavior (value is 1&lt;&lt;1)
	 */
	public static final int SEPARATOR = 1 << 1;

	/* Button, MenuItem Constants */

	/**
	 * <code>Button</code> style constant for toggle button behavior (value is 1&lt;&lt;1)
	 */
	public static final int TOGGLE = 1 << 1;

	/**
	 * <code>Button</code> style constant for arrow button behavior (value is 1&lt;&lt;2)
	 */
	public static final int ARROW = 1 << 2;

	/**
	 * <code>Button</code> and <code>MenuItem</code> 
	 * style constant for push button behavior (value is 1&lt;&lt;3)
	 */
	public static final int PUSH = 1 << 3;

	/**
	 * <code>Button</code> and <code>MenuItem</code> 
	 * style constant for radio button behavior (value is 1&lt;&lt;4)
	 */
	public static final int RADIO = 1 << 4;

	/**
	 * <code>Button</code> and <code>MenuItem</code> 
	 * style constant for check box behavior (value is 1&lt;&lt;5)
	 */
	public static final int CHECK = 1 << 5;

	/**
	 * <code>MenuItem</code> style constant for cascade behavior (value is 1&lt;&lt;6)
	 */
	public static final int CASCADE = 1 << 6;

	/**
	 * <code>Text</code> and <code>List</code> 
	 * style constant for multi-selection behavior in lists
	 * and multiple line support on text fields (value is 1&lt;&lt;1)
	 */
	public static final int MULTI = 1 << 1;

	/**
	 * <code>Text</code> and <code>List</code> 
	 * style constant for single selection behavior in lists
	 * and single line support on text fields (value is 1&lt;&lt;2)
	 */
	public static final int SINGLE = 1 << 2;

	/**
	 * <code>Text</code> style constant for read-only behavior (value is 1&lt;&lt;3)
	 */
	public static final int READ_ONLY = 1 << 3;

	/**
	 * <code>Text</code> style constant for auto-line wrap behavior (value is 1&lt;&lt;6)
	 */
	public static final int WRAP = 1 << 6;

	/**
	 * <code>Combo</code> style constant for simple (not drop down) behavior (value is 1&lt;&lt;6)
	 */
	public static final int SIMPLE = 1 << 6;

	/**
	 * <code>Group</code> and <code>Label</code>
	 * style constant for shadow in behavior (value is 1&lt;&lt;2)
	 */
	public static final int SHADOW_IN = 1 << 2;

	/**
	 * <code>Group</code>, <code>Label</code> and <code>CustomLabel</code> 
	 * style constant for shadow out behavior (value is 1&lt;&lt;3)
	 */
	public static final int SHADOW_OUT = 1 << 3;

	/**
	 * <code>Group</code> style constant for shadow etched in behavior (value is 1&lt;&lt;4)
	 * NOTE: This style is ignored on all platforms except Motif
	 */
	public static final int SHADOW_ETCHED_IN = 1 << 4;

	/**
	 * <code>Label</code> style constant for no shadow behavior (value is 1&lt;&lt;5)
	 */
	public static final int SHADOW_NONE = 1 << 5;

	/**
	 * <code>Group</code> style constant for shadow etched out behavior (value is 1&lt;&lt;6)
	 * NOTE: This style is ignored on all platforms except Motif
	 */
	public static final int SHADOW_ETCHED_OUT = 1 << 6;

	/**
	 * <code>Shell</code> style constant for tool window behavior (value is 1&lt;&lt;2)
	 */
	public static final int TOOL = 1 << 2; 

	/**
	 * <code>Shell</code> style constant to ensure no trimmings are used. 
	 * This overrides all other trim styles (value is 1&lt;&lt;3)
	 */
	public static final int NO_TRIM = 1 << 3;
	
	/**
	 * <code>Shell</code> style constant for resize box trim (value is 1&lt;&lt;4)
	 */
	public static final int RESIZE = 1 << 4;

	/**
	 * <code>Shell</code> style constant for title area trim (value is 1&lt;&lt;5)
	 */
	public static final int TITLE = 1 << 5;

	/**
	 * <code>Shell</code> style constant for close box trim (value is 1&lt;&lt;6,
	 * since we do not distinguish between CLOSE style and MENU style)
	 */
	public static final int CLOSE = 1 << 6;

	/**
	 * <code>Shell</code> style constant for shell menu trim (value is 1&lt;&lt;6,
	 * since we do not distinguish between CLOSE style and MENU style)
	 */
	public static final int MENU = CLOSE;

	/**
	 * <code>Shell</code> style constant for minimize box trim (value is 1&lt;&lt;7)
	 */
	public static final int MIN = 1 << 7;

	/* ScrollBar, Composite, Shell Constants */

	/**
	 * <code>ScrollBar</code>, <code>Composite</code> and <code>Shell</code>
	 * style constant for horizontal scrollbar behavior (value is 1&lt;&lt;8)
	 */
	public static final int H_SCROLL = 1 << 8;

	/**
	 * <code>ScrollBar</code>, <code>Composite</code> and <code>Shell</code>
	 * style constant for vertical scrollbar behavior (value is 1&lt;&lt;9)
	 */
	public static final int V_SCROLL = 1 << 9;

	/**
	 * <code>Shell</code> style constant for maximize box trim (value is 1&lt;&lt;10)
	 */
	public static final int MAX = 1 << 10;

	/**
	 * all <code>Widget</code> style constant for bordered behavior (value is 1&lt;&lt;11)
	 */
	public static final int BORDER = 1 << 11;

	/**
	 * all <code>Widget</code> style constant indicating that the window
	 * manager should clip a widget's children with respect to its viewable
	 * area. Note that this is a <em>HINT</em>. (value is 1&lt;&lt;12)
	 */
	public static final int CLIP_CHILDREN = 1 << 12; 

	/**
	 * all <code>Widget</code> style constant indicating that the window
	 * manager should clip a widget's siblings with respect to its viewable
	 * area. Note that this is a <em>HINT</em>. (value is 1&lt;&lt;13)
	 */
	public static final int CLIP_SIBLINGS = 1 << 13;

	/**
	 * <code>Shell</code> style constant for always on top behavior (value is 1&lt;&lt;14)
	 */
	public static final int ON_TOP = 1 << 14;

	/**
	 * <code>Shell</code> trim style convenience constant for the
	 * most common top level shell appearance
	 * (value is CLOSE|TITLE|MIN|MAX|RESIZE)
	 */
	public static final int SHELL_TRIM = CLOSE | TITLE | MIN | MAX | RESIZE;

	/**
	 * <code>Shell</code> trim style convenience constant for the
	 * most common dialog shell appearance
	 * (value is CLOSE|TITLE|BORDER)
	 */
	public static final int DIALOG_TRIM = TITLE | CLOSE | BORDER;

	/**
	 * <code>Shell</code> style constant for modeless behavior (value is 0)
	 */
	public static final int MODELESS = 0;

	/**
	 * <code>Shell</code> style constant for primary modal behavior (value is 1&lt;&lt;15)
	 */
	public static final int PRIMARY_MODAL = 1 << 15;

	/**
	 * <code>Shell</code> style constant for application modal behavior (value is 1&lt;&lt;16)
	 */
	public static final int APPLICATION_MODAL = 1 << 16;

	/**
	 * <code>Shell</code> style constant for system modal behavior (value is 1&lt;&lt;17)
	 */
	public static final int SYSTEM_MODAL = 1 << 17;

	/**
	 * all <code>Widget</code> style constant for selection hiding
	 * behavior. Note that this is a <em>HINT</em>.  (value is 1&lt;&lt;15)
	 */
	public static final int HIDE_SELECTION = 1 << 15;

	/**
	 * all <code>Widget</code> style constant for full row selection
	 * behavior. Note that this is a <em>HINT</em>.  (value is 1&lt;&lt;16 since
	 * FULL_SELECTION and SMOOTH share the same value, but are 
	 * implemented on non-intersecting sets of widgets)
	 */
	public static final int FULL_SELECTION = 1 << 16;

	/**
	 * all <code>Widget</code> style constant for flat appearance.
	 * Note that this is a <em>HINT</em>.  (value is 1&lt;&lt;23)
	 */
	public static final int FLAT = 1 << 23;

	/**
	 * all <code>Widget</code> style constant for flat appearance.
	 * Note that this is a <em>HINT</em>.  (value is 1&lt;&lt;16 since
	 * FULL_SELECTION and SMOOTH share the same value, but are 
	 * implemented on non-intersecting sets of widgets)
	 */
	public static final int SMOOTH = FULL_SELECTION;

	/**
	 * <code>Canvas</code> style constant for no background behavior (value is 1&lt;&lt;18)
	 */
	public static final int NO_BACKGROUND = 1 << 18;

	/**
	 * <code>Canvas</code> style constant for does not take focus behavior (value is 1&lt;&lt;19)
	 */
	public static final int NO_FOCUS = 1 << 19;

	/**
	 * <code>Canvas</code> style constant for no redraw on resize behavior (value is 1&lt;&lt;20)
	 */
	public static final int NO_REDRAW_RESIZE = 1 << 20;

	/**
	 * <code>Canvas</code> style constant for no paint event merging
	 * behavior (value is 1&lt;&lt;21)
	 */
	public static final int NO_MERGE_PAINTS = 1 << 21;

	/**
	 * <code>Canvas</code> style constant for preventing child radio group
	 * behavior (value is 1&lt;&lt;22)
	 */
	public static final int NO_RADIO_GROUP = 1 << 22;

	/**
	 * various <code>Widget</code> and <code>Layout</code>
	 * style constant for align up behavior (value is 1&lt;&lt;7, since
	 * align UP and align TOP are considered the same)
	 */
	public static final int UP = 1 << 7;

	/**
	 * various <code>Widget</code> and <code>Layout</code>
	 * style constant for align top behavior (value is 1&lt;&lt;7, since
	 * align UP and align TOP are considered the same)
	 */
	public static final int TOP = UP;

	/**
	 * various <code>Widget</code> and <code>Layout</code>
	 * style constant for align down behavior (value is 1&lt;&lt;10, since
	 * align DOWN and align BOTTOM are considered the same)
	 */
	public static final int DOWN               = 1 << 10;

	/**
	 * various <code>Widget</code> and <code>Layout</code>
	 * style constant for align bottom behavior (value is 1&lt;&lt;10, since
	 * align DOWN and align BOTTOM are considered the same)
	 */
	public static final int BOTTOM             = DOWN;

	/**
	 * various <code>Widget</code> and <code>Layout</code>
	 * style constant for align left behavior (value is 1&lt;&lt;14)
	 */
	public static final int LEFT               = 1 << 14;

	/**
	 * various <code>Widget</code> and <code>Layout</code>
	 * style constant for align right behavior (value is 1&lt;&lt;17)
	 */
	public static final int RIGHT              = 1 << 17;

	/**
	 * various <code>Widget</code> and <code>Layout</code>
	 * style constant for align center behavior (value is 1&lt;&lt;24)
	 */
	public static final int CENTER             = 1 << 24;

	/**
	 * various <code>Widget</code> and <code>Layout</code>
	 * style constant for align horizontal behavior (value is 1&lt;&lt;8)
	 */
	public static final int HORIZONTAL = H_SCROLL;

	/**
	 * various <code>Widget</code> and <code>Layout</code>
	 * style constant for align vertical behavior (value is 1&lt;&lt;9)
	 */
	public static final int VERTICAL = V_SCROLL;

	/**
	 * Input Method Editor style constant for double byte
	 * input behavior (value is 1&lt;&lt;1)
	 */
	public static final int DBCS = 1 << 1;

	/**
	 * Input Method Editor style constant for alpha
	 * input behavior (value is 1&lt;&lt;2)
	 */
	public static final int ALPHA = 1 << 2;

	/**
	 * Input Method Editor style constant for native
	 * input behavior (value is 1&lt;&lt;3)
	 */
	public static final int NATIVE = 1 << 3;

	/**
	 * Input Method Editor style constant for phonetic
	 * input behavior (value is 1&lt;&lt;4)
	 */
	public static final int PHONETIC = 1 << 4;

	/**
	 * Input Method Editor style constant for romanicized
	 * input behavior (value is 1&lt;&lt;5)
	 */
	public static final int ROMAN = 1 << 5;

	/**
	 * ASCII character convenience constant for the escape character
	 * (value is the <code>char</code> with value 27)
	 */
	public static final char ESC = 27;

	/**
	 * ASCII character convenience constant for the delete character
	 * (value is the <code>char</code> with value 127)
	 */
	public static final char DEL = 0x7F;
 
	/**
	 * ASCII character convenience constant for the backspace character
	 * (value is the <code>char</code> '\b')
	 */
      public static final char BS = '\b';

	/**
	 * ASCII character convenience constant for the carriage return character
	 * (value is the <code>char</code> '\r')
	 */
	public static final char CR = '\r';

	/**
	 * ASCII character convenience constant for the line feed character
	 * (value is the <code>char</code> '\n')
	 */
	public static final char LF = '\n';
					
	/**
	 * keyboard and/or mouse event mask indicating that the ALT key
	 * was pushed on the keyboard when the event was generated
	 * (value is 1&lt;&lt;16)
	 */
	public static final int ALT = 1 << 16;
					
	/**
	 * keyboard and/or mouse event mask indicating that the SHIFT key
	 * was pushed on the keyboard when the event was generated
	 * (value is 1&lt;&lt;17)
	 */
	public static final int SHIFT = 1 << 17;
					
	/**
	 * keyboard and/or mouse event mask indicating that the CTRL key
	 * was pushed on the keyboard when the event was generated
	 * (value is 1&lt;&lt;18)
	 */
	public static final int CTRL = 1 << 18;

	/**
	 * keyboard and/or mouse event mask indicating that the CTRL key
	 * was pushed on the keyboard when the event was generated. This
	 * is a synonym for CTRL (value is 1&lt;&lt;18)
	 */
	public static final int CONTROL = CTRL;

	/**
	 * keyboard and/or mouse event mask indicating that mouse button one
	 * was pushed when the event was generated. (value is 1&lt;&lt;19)
	 */
	public static final int BUTTON1 = 1 << 19;

	/**
	 * keyboard and/or mouse event mask indicating that mouse button two
	 * was pushed when the event was generated. (value is 1&lt;&lt;20)
	 */
	public static final int BUTTON2 = 1 << 20;

	/**
	 * keyboard and/or mouse event mask indicating that mouse button three
	 * was pushed when the event was generated. (value is 1&lt;&lt;21)
	 */
	public static final int BUTTON3 = 1 << 21;

	/**
	 * keyboard event constant representing the UP ARROW key
	 * (value is (1&lt;&lt;24)+1)
	 */
	public static final int ARROW_UP = (1 << 24) + 1;

	/**
	 * keyboard event constant representing the DOWN ARROW key
	 * (value is (1&lt;&lt;24)+2)
	 */
	public static final int ARROW_DOWN = (1 << 24) + 2;

	/**
	 * keyboard event constant representing the LEFT ARROW key
	 * (value is (1&lt;&lt;24)+3)
	 */
	public static final int ARROW_LEFT = (1 << 24) + 3;

	/**
	 * keyboard event constant representing the RIGHT ARROW key
	 * (value is (1&lt;&lt;24)+4)
	 */
	public static final int ARROW_RIGHT = (1 << 24) + 4;

	/**
	 * keyboard event constant representing the PAGE UP key
	 * (value is (1&lt;&lt;24)+5)
	 */
	public static final int PAGE_UP = (1 << 24) + 5;

	/**
	 * keyboard event constant representing the PAGE DOWN key
	 * (value is (1&lt;&lt;24)+6)
	 */
	public static final int PAGE_DOWN = (1 << 24) + 6;

	/**
	 * keyboard event constant representing the HOME key
	 * (value is (1&lt;&lt;24)+7)
	 */
	public static final int HOME = (1 << 24) + 7;

	/**
	 * keyboard event constant representing the END key
	 * (value is (1&lt;&lt;24)+8)
	 */
	public static final int END = (1 << 24) + 8;

	/**
	 * keyboard event constant representing the INSERT key
	 * (value is (1&lt;&lt;24)+9)
	 */
	public static final int INSERT = (1 << 24) + 9;

	/**
	 * keyboard event constant representing the F1 key
	 * (value is (1&lt;&lt;24)+10)
	 */
	public static final int F1 = (1 << 24) + 10;
	
	/**
	 * keyboard event constant representing the F2 key
	 * (value is (1&lt;&lt;24)+11)
	 */
	public static final int F2 = (1 << 24) + 11;
	
	/**
	 * keyboard event constant representing the F3 key
	 * (value is (1&lt;&lt;24)+12)
	 */
	public static final int F3 = (1 << 24) + 12;
	
	/**
	 * keyboard event constant representing the F4 key
	 * (value is (1&lt;&lt;24)+13)
	 */
	public static final int F4 = (1 << 24) + 13;
	
	/**
	 * keyboard event constant representing the F5 key
	 * (value is (1&lt;&lt;24)+14)
	 */
	public static final int F5 = (1 << 24) + 14;
	
	/**
	 * keyboard event constant representing the F6 key
	 * (value is (1&lt;&lt;24)+15)
	 */
	public static final int F6 = (1 << 24) + 15;
	
	/**
	 * keyboard event constant representing the F7 key
	 * (value is (1&lt;&lt;24)+16)
	 */
	public static final int F7 = (1 << 24) + 16;
	
	/**
	 * keyboard event constant representing the F8 key
	 * (value is (1&lt;&lt;24)+17)
	 */
	public static final int F8 = (1 << 24) + 17;
	
	/**
	 * keyboard event constant representing the F9 key
	 * (value is (1&lt;&lt;24)+18)
	 */
	public static final int F9 = (1 << 24) + 18;
	
	/**
	 * keyboard event constant representing the F10 key
	 * (value is (1&lt;&lt;24)+19)
	 */
	public static final int F10 = (1 << 24) + 19;
	
	/**
	 * keyboard event constant representing the F11 key
	 * (value is (1&lt;&lt;24)+20)
	 */
	public static final int F11 = (1 << 24) + 20;
	
	/**
	 * keyboard event constant representing the F12 key
	 * (value is (1&lt;&lt;24)+21)
	 */
	public static final int F12 = (1 << 24) + 21;
	
	/**
	 * <code>MessageBox</code> style constant for error icon
	 * behavior (value is 1)
	 */
	public static final int ICON_ERROR = 1;

	/**
	 * <code>MessageBox</code> style constant for information icon
	 * behavior (value is 1&lt;&lt;1)
	 */
	public static final int ICON_INFORMATION = 1 << 1;

	/**
	 * <code>MessageBox</code> style constant for question icon
	 * behavior (value is 1&lt;&lt;2)
	 */
	public static final int ICON_QUESTION = 1 << 2;

	/**
	 * <code>MessageBox</code> style constant for warning icon
	 * behavior (value is 1&lt;&lt;3)
	 */
	public static final int ICON_WARNING = 1 << 3;

	/**
	 * <code>MessageBox</code> style constant for "working" icon
	 * behavior (value is 1&lt;&lt;4)
	 */
	public static final int ICON_WORKING = 1 << 4;

	/**
	 * <code>MessageBox</code> style constant for an OK button.
	 * Valid combinations are OK, OK|CANCEL
	 * (value is 1&lt;&lt;5)
	 */
	public static final int OK = 1 << 5;

	/**
	 * <code>MessageBox</code> style constant for YES button.
	 * Valid combinations are YES|NO, YES|NO|CANCEL
	 * (value is 1&lt;&lt;6)
	 */
	public static final int YES = 1 << 6;

	/**
	 * <code>MessageBox</code> style constant for NO button.
	 * Valid combinations are YES|NO, YES|NO|CANCEL
	 * (value is 1&lt;&lt;7)
	 */
	public static final int NO = 1 << 7;

	/**
	 * <code>MessageBox</code> style constant for a CANCEL button.
	 * Valid combinations are OK|CANCEL, YES|NO|CANCEL, RETRY|CANCEL
	 * (value is 1&lt;&lt;8)
	 */
	public static final int CANCEL = 1 << 8;

	/**
	 * <code>MessageBox</code> style constant for an ABORT button.
	 * The only valid combination is ABORT|RETRY|IGNORE
	 * (value is 1&lt;&lt;9)
	 */
	public static final int ABORT = 1 << 9;

	/**
	 * <code>MessageBox</code> style constant for a RETRY button.
	 * Valid combinations are ABORT|RETRY|IGNORE, RETRY|CANCEL
	 * (value is 1&lt;&lt;10)
	 */
	public static final int RETRY = 1 << 10;

	/**
	 * <code>MessageBox</code> style constant for an IGNORE button.
	 * The only valid combination is ABORT|RETRY|IGNORE
	 * (value is 1&lt;&lt;11)
	 */
	public static final int	IGNORE = 1 << 11;

	/**
	 * <code>FileDialog</code> style constant for open file dialog behavior
	 * (value is 1&lt;&lt;12)
	 */
	public static final int OPEN = 1 << 12;

	/**
	 * <code>FileDialog</code> style constant for save file dialog behavior
	 * (value is 1&lt;&lt;13)
	 */
	public static final int SAVE = 1 << 13;

	/**
	 * default color white (value is 1)
	 */
	public static final int COLOR_WHITE = 1;

	/**
	 * default color black (value is 2)
	 */
	public static final int COLOR_BLACK = 2;

	/**
	 * default color red (value is 3)
	 */
	public static final int COLOR_RED = 3;

	/**
	 * default color dark red (value is 4)
	 */
	public static final int COLOR_DARK_RED = 4;

	/**
	 * default color green (value is 5)
	 */
	public static final int COLOR_GREEN = 5;

	/**
	 * default color dark green (value is 6)
	 */
	public static final int COLOR_DARK_GREEN = 6;

	/**
	 * default color yellow (value is 7)
	 */
	public static final int COLOR_YELLOW = 7;

	/**
	 * default color dark yello (value is 8)
	 */
	public static final int COLOR_DARK_YELLOW = 8;

	/**
	 * default color blue (value is 9)
	 */
	public static final int COLOR_BLUE = 9;

	/**
	 * default color dark blue (value is 10)
	 */
	public static final int COLOR_DARK_BLUE = 10;

	/**
	 * default color magenta (value is 11)
	 */
	public static final int COLOR_MAGENTA = 11;

	/**
	 * default color dark magenta (value is 12)
	 */
	public static final int COLOR_DARK_MAGENTA = 12;

	/**
	 * default color cyan (value is 13)
	 */
	public static final int COLOR_CYAN = 13;

	/**
	 * default color dark cyan (value is 14)
	 */
	public static final int COLOR_DARK_CYAN = 14;

	/**
	 * default color gray (value is 15)
	 */
	public static final int COLOR_GRAY = 15;

	/**
	 * default color dark gray (value is 16)
	 */
	public static final int COLOR_DARK_GRAY = 16;
	
	/*
	 * System Colors
	 *
	 * Dealing with system colors is an area where there are
	 * many platform differences.  On some platforms, system
	 * colors can change dynamically while the program is
	 * running.  On other platforms, system colors can be
	 * changed for all instances of a particular widget.
	 * Therefore, the only truly portable method to obtain
	 * a widget color query is to query the color from an
	 * instance of the widget.
	 *
	 *	It is expected that the list of supported colors
	 * will grow over time.
	 */
	
	/**
	 * system color used to paint dark shadow areas (value is 17)
	 */
	public static final int COLOR_WIDGET_DARK_SHADOW = 17;

	/**
	 * system color used to paint normal shadow areas (value is 18)
	 */
	public static final int COLOR_WIDGET_NORMAL_SHADOW = 18;

	/**
	 * system color used to paint light shadow areas (value is 19)
	 */
	public static final int COLOR_WIDGET_LIGHT_SHADOW = 19;

	/**
	 * system color used to paint highlight shadow areas (value is 20)
	 */
	public static final int COLOR_WIDGET_HIGHLIGHT_SHADOW = 20;

	/**
	 * system color used to paint foreground areas (value is 21)
	 */
	public static final int COLOR_WIDGET_FOREGROUND = 21;

	/**
	 * system color used to paint background areas (value is 22)
	 */
	public static final int COLOR_WIDGET_BACKGROUND = 22;

	/**
	 * system color used to paint border areas (value is 23)
	 */
	public static final int COLOR_WIDGET_BORDER = 23;

	/**
	 * system color used to paint list foreground areas (value is 24)
	 */
	public static final int COLOR_LIST_FOREGROUND = 24;

	/**
	 * system color used to paint list background areas (value is 25)
	 */
	public static final int COLOR_LIST_BACKGROUND = 25;

	/**
	 * system color used to paint list selection background areas (value is 26)
	 */
	public static final int COLOR_LIST_SELECTION = 26;

	/**
	 * system color used to paint list selected text (value is 27)
	 */
	public static final int COLOR_LIST_SELECTION_TEXT = 27;

	/**
	 * system color used to paint tooltip text (value is 28)
	 */
	public static final int COLOR_INFO_FOREGROUND = 28;

	/**
	 * system color used to paint tooltip background areas (value is 29)
	 */
	public static final int COLOR_INFO_BACKGROUND = 29;
	
	/**
	 * system color used to paint title text (value is 30)
	 */
	public static final int COLOR_TITLE_FOREGROUND = 30;

	/**
	 * system color used to paint title background areas (value is 31)
	 */
	public static final int COLOR_TITLE_BACKGROUND = 31;

	/**
	 * system color used to paint title background gradient (value is 32)
	 */
	public static final int COLOR_TITLE_BACKGROUND_GRADIENT = 32;
	
	/**
	 * system color used to paint inactive title text (value is 33)
	 */
	public static final int COLOR_TITLE_INACTIVE_FOREGROUND = 33;

	/**
	 * system color used to paint inactive title background areas (value is 34)
	 */
	public static final int COLOR_TITLE_INACTIVE_BACKGROUND = 34;

	/**
	 * system color used to paint inactive title background gradient (value is 35)
	 */
	public static final int COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT = 35;
	
	/**
	 * draw constant indicating whether the drawing operation
	 * should fill the background (value is 1&lt;&lt;0)
	 */
	public static final int DRAW_TRANSPARENT = 1 << 0;

	/**
	 * draw constant indicating whether the string drawing operation
	 * should handle line-delimeters (value is 1&lt;&lt;1)
	 */
	public static final int DRAW_DELIMITER = 1 << 1;

	/**
	 * draw constant indicating whether the string drawing operation
	 * should expand TAB characters (value is 1&lt;&lt;2)
	 */
	public static final int DRAW_TAB = 1 << 2;

	/**
	 * draw constant indicating whether the string drawing operation
	 * should handle mnemonics (value is 1&lt;&lt;3)
	 */
	public static final int DRAW_MNEMONIC = 1 << 3;	
	
	/** 
	 * SWT error constant indicating that no error number was specified
	 * (value is 1) 
	 */
	public static final int ERROR_UNSPECIFIED = 1;
	
	/** 
	 * SWT error constant indicating that no more handles for an
	 * operating system resource are available
	 * (value is 2) 
	 */
	public static final int ERROR_NO_HANDLES = 2;
	
	/** 
	 * SWT error constant indicating that no more callback resources are available
	 * (value is 3) 
	 */
	public static final int ERROR_NO_MORE_CALLBACKS = 3;
	
	/** 
	 * SWT error constant indicating that a null argument was passed in
	 * (value is 4) 
	 */
	public static final int ERROR_NULL_ARGUMENT = 4;
	
	/** 
	 * SWT error constant indicating that an invalid argument was passed in
	 * (value is 5) 
	 */
	public static final int ERROR_INVALID_ARGUMENT = 5;
	
	/** 
	 * SWT error constant indicating that a value was found to be
	 * outside the allowable range
	 * (value is 6) 
	 */
	public static final int ERROR_INVALID_RANGE = 6;
	
	/** 
	 * SWT error constant indicating that a value which can not be 
	 * zero was found to be
	 * (value is 7) 
	 */
	public static final int ERROR_CANNOT_BE_ZERO = 7;
	
	/** 
	 * SWT error constant indicating that the underlying operating
	 * system was unable to provide the value of an item
	 * (value is 8) 
	 */
	public static final int ERROR_CANNOT_GET_ITEM = 8;
	
	/** 
	 * SWT error constant indicating that the underlying operating
	 * system was unable to provide the selection
	 * (value is 9) 
	 */
	public static final int ERROR_CANNOT_GET_SELECTION = 9;

	/** 
	 * SWT error constant indicating that the underlying operating
	 * system was unable to provide the height of an item
	 * (value is 11) 
	 */
	public static final int ERROR_CANNOT_GET_ITEM_HEIGHT = 11;

	/** 
	 * SWT error constant indicating that the underlying operating
	 * system was unable to provide the text of a widget
	 * (value is 12) 
	 */
	public static final int ERROR_CANNOT_GET_TEXT = 12;

	/** 
	 * SWT error constant indicating that the underlying operating
	 * system was unable to set the text of a widget
	 * (value is 13) 
	 */
	public static final int ERROR_CANNOT_SET_TEXT = 13;

	/** 
	 * SWT error constant indicating that the underlying operating
	 * system was unable to add an item
	 * (value is 14) 
	 */
	public static final int ERROR_ITEM_NOT_ADDED = 14;

	/** 
	 * SWT error constant indicating that the underlying operating
	 * system was unable to remove an item
	 * (value is 15) 
	 */
	public static final int ERROR_ITEM_NOT_REMOVED = 15;

	/** 
	 * SWT error constant indicating that a particular feature has
	 * not been implemented on this platform
	 * (value is 20) 
	 */
	public static final int ERROR_NOT_IMPLEMENTED = 20;

	/** 
	 * SWT error constant indicating that a menu which needed
	 * to have the drop down style had some other style instead
	 * (value is 21) 
	 */
	public static final int ERROR_MENU_NOT_DROP_DOWN = 21;

	/** 
	 * SWT error constant indicating that an attempt was made to
	 * invoke an SWT operation which can only be executed by the
	 * user-interface thread from some other thread
	 * (value is 22) 
	 */
	public static final int ERROR_THREAD_INVALID_ACCESS = 22;

	/** 
	 * SWT error constant indicating that an attempt was made to
	 * invoke an SWT operation using a widget which had already
	 * been disposed.
	 * (value is 24) 
	 */
	public static final int ERROR_WIDGET_DISPOSED = 24;

	/** 
	 * SWT error constant indicating that a menu item which needed
	 * to have the cascade style had some other style instead
	 * (value is 27) 
	 */
	public static final int ERROR_MENUITEM_NOT_CASCADE = 27;

	/** 
	 * SWT error constant indicating that the underlying operating
	 * system was unable to set the selection of a widget
	 * (value is 28) 
	 */
	public static final int ERROR_CANNOT_SET_SELECTION = 28;

	/** 
	 * SWT error constant indicating that the underlying operating
	 * system was unable to set the menu
	 * (value is 29) 
	 */
	public static final int ERROR_CANNOT_SET_MENU = 29;

	/** 
	 * SWT error constant indicating that the underlying operating
	 * system was unable to set the enabled state
	 * (value is 30) 
	 */
	public static final int ERROR_CANNOT_SET_ENABLED = 30;

	/** 
	 * SWT error constant indicating that the underlying operating
	 * system was unable to provide enabled/disabled state information
	 * (value is 31) 
	 */
	public static final int ERROR_CANNOT_GET_ENABLED = 31;

	/** 
	 * SWT error constant indicating that a provided widget can
	 * not be used as a parent in the current operation
	 * (value is 32) 
	 */
	public static final int ERROR_INVALID_PARENT = 32;
	
	/** 
	 * SWT error constant indicating that a menu which needed
	 * to have the menu bar style had some other style instead
	 * (value is 33) 
	 */
	public static final int ERROR_MENU_NOT_BAR = 33;

	/** 
	 * SWT error constant indicating that the underlying operating
	 * system was unable to provide count information
	 * (value is 36) 
	 */
	public static final int ERROR_CANNOT_GET_COUNT = 36;

	/** 
	 * SWT error constant indicating that a menu which needed
	 * to have the pop up menu style had some other style instead
	 * (value is 37) 
	 */
	public static final int ERROR_MENU_NOT_POP_UP = 37;

	/** 
	 * SWT error constant indicating that a graphics operation
	 * was attempted with an image of an unsupported depth
	 * (value is 38) 
	 */
	public static final int ERROR_UNSUPPORTED_DEPTH = 38;

	/** 
	 * SWT error constant indicating that an input/output operation
	 * failed during the execution of an SWT operation
	 * (value is 39) 
	 */
	public static final int ERROR_IO = 39;

	/** 
	 * SWT error constant indicating that a graphics operation
	 * was attempted with an image having an invalid format
	 * (value is 40) 
	 */
	public static final int ERROR_INVALID_IMAGE = 40;

	/** 
	 * SWT error constant indicating that a graphics operation
	 * was attempted with an image having a valid but unsupported
	 * format
	 * (value is 42) 
	 */
	public static final int ERROR_UNSUPPORTED_FORMAT = 42;

	/** 
	 * SWT error constant indicating that an attempt was made
	 * to subclass an SWT widget class without implementing the
	 * <code>checkSubclass()</code> method. For additional
	 * information see the comment in <code>Widget.checkSubclass()</code>
	 * (value is 43) 
	 *
	 * @see org.eclipse.swt.widgets.Widget#checkSubclass
	 */
	public static final int ERROR_INVALID_SUBCLASS = 43;

	/** 
	 * SWT error constant indicating that an attempt was made to
	 * invoke an SWT operation using a graphics object which had
	 * already been disposed.
	 * (value is 44) 
	 */
	public static final int ERROR_GRAPHIC_DISPOSED = 44;
	
	/** 
	 * SWT error constant indicating that an attempt was made to
	 * invoke an SWT operation using a device which had already
	 * been disposed.
	 * (value is 45) 
	 */
	public static final int ERROR_DEVICE_DISPOSED = 45;
	
	/** 
	 * SWT error constant indicating that an exception happened
	 * when executing a runnable.
	 * (value is 46) 
	 */
	public static final int ERROR_FAILED_EXEC = 46;
	
	/** 
	 * SWT error constant indicating that an unsatisfied link
	 * error occured while attempting to load a library.
	 * (value is 47) 
	 */
	public static final int ERROR_FAILED_LOAD_LIBRARY = 47;
	
	/**
	 * traversal event detail field value indicating that no 
	 * traversal action should be taken.
	 * (value is 0)
	 */
	public static final int TRAVERSE_NONE = 0;
	
	/**
	 * traversal event detail field value indicating that the 
	 * key which designates that a dialog should be cancelled was
	 * pressed; typically, this is the ESC key
	 * (value is 1&lt;&lt;1)
	 */
	public static final int TRAVERSE_ESCAPE = 1 << 1;

	/**
	 * traversal event detail field value indicating that the
	 * key which activates the default button in a dialog was
	 * pressed; typically, this is the ENTER key
	 * (value is 1&lt;&lt;2)
	 */
	public static final int TRAVERSE_RETURN = 1 << 2;

	/**
	 * traversal event detail field value indicating that the 
	 * key which designates that focus should be given to the
	 * previous tab group was pressed; typically, this is the
	 * SHIFT-TAB key sequence
	 * (value is 1&lt;&lt;3)
	 */
	public static final int TRAVERSE_TAB_PREVIOUS = 1 << 3;

	/**
	 * traversal event detail field value indicating that the 
	 * key which designates that focus should be given to the
	 * next tab group was pressed; typically, this is the
	 * TAB key
	 * (value is 1&lt;&lt;4)
	 */
	public static final int TRAVERSE_TAB_NEXT = 1 << 4;

	/**
	 * traversal event detail field value indicating that the 
	 * key which designates that focus should be given to the
	 * previous tab item was pressed; typically, this is either
	 * the LEFT-ARROW or UP-ARROW keys
	 * (value is 1&lt;&lt;5)
	 */
	public static final int TRAVERSE_ARROW_PREVIOUS = 1 << 5;

	/**
	 * traversal event detail field value indicating that the 
	 * key which designates that focus should be given to the
	 * previous tab item was pressed; typically, this is either
	 * the RIGHT-ARROW or DOWN-ARROW keys
	 * (value is 1&lt;&lt;6)
	 */
	public static final int TRAVERSE_ARROW_NEXT = 1 << 6;

	/**
	 * traversal event detail field value indicating that a 
	 * mnemonic key sequence was pressed
	 * (value is 1&lt;&lt;7)
	 */
	public static final int TRAVERSE_MNEMONIC = 1 << 7;

	/**
	 * traversal event detail field value indicating that the 
	 * key which designates that the previous page of a multi-page
	 * window should be shown was pressed; typically, this
	 * is the CTRL-PAGEUP key sequence
	 * (value is 1&lt;&lt;8)
	 */
	public static final int TRAVERSE_PAGE_PREVIOUS = 1 << 8;
	
	/**
	 * traversal event detail field value indicating that the 
	 * key which designates that the next page of a multi-page
	 * window should be shown was pressed; typically, this
	 * is the CTRL-PAGEDOWN key sequence
	 * (value is 1&lt;&lt;9)
	 */
	public static final int TRAVERSE_PAGE_NEXT = 1 << 9;

	/**
	 * constant indicating that an image or operation is of type bitmap (value is 0)
	 */	
	public static final int BITMAP = 0;

	/**
	 * constant indicating that an image or operation is of type icon (value is 1)
	 */	
	public static final int ICON = 1;

	/**
	 * <code>Image</code> constructor argument indicating that
	 * the new image should be a copy of the image provided as
	 * an argument (value is 0)
	 */	
	public static final int IMAGE_COPY = 0;

	/**
	 * <code>Image</code> constructor argument indicating that
	 * the new image should have the appearance of a "disabled"
	 * (using the platform's rules for how this should look)
	 * copy of the image provided as an argument (value is 1)
	 */	
	public static final int IMAGE_DISABLE = 1;
	
	/**
	 * <code>Image</code> constructor argument indicating that
	 * the new image should have the appearance of a "gray scaled"
	 * copy of the image provided as an argument (value is 2)
	 */	
	public static final int IMAGE_GRAY = 2;
	
	/**
	 * font style constant indicating a normal weight, non-italic font
	 * (value is 0)
	 */
	public static final int NORMAL = 0;
	
	/**
	 * font style constant indicating a bold weight font
	 * (value is 1&lt;&lt;0)
	 */
	public static final int BOLD = 1 << 0;
	
	/**
	 * font style constant indicating an italic font
	 * (value is 1&lt;&lt;1)
	 */
	public static final int ITALIC = 1 << 1;
		
	/**
	 * system arrow cursor (value is 0)
	 */
	public static final int CURSOR_ARROW = 0;
		
	/**
	 * system wait cursor (value is 1)
	 */
	public static final int CURSOR_WAIT = 1;
		
	/**
	 * system cross hair cursor (value is 2)
	 */
	public static final int CURSOR_CROSS = 2;
		
	/**
	 * system app startup cursor (value is 3)
	 */
	public static final int CURSOR_APPSTARTING = 3;
		
	/**
	 * system help cursor (value is 4)
	 */
	public static final int CURSOR_HELP = 4;
		
	/**
	 * system resize all directions cursor (value is 5)
	 */
	public static final int CURSOR_SIZEALL = 5;
		
	/**
	 * system resize north-east-south-west cursor (value is 6)
	 */
	public static final int CURSOR_SIZENESW = 6;
		
	/**
	 * system resize north-south cursor (value is 7)
	 */
	public static final int CURSOR_SIZENS = 7;
		
	/**
	 * system resize north-west-south-east cursor (value is 8)
	 */
	public static final int CURSOR_SIZENWSE = 8;
		
	/**
	 * system resize west-east cursor (value is 9)
	 */
	public static final int CURSOR_SIZEWE = 9;
		
	/**
	 * system resize north cursor (value is 10)
	 */
	public static final int CURSOR_SIZEN = 10;
		
	/**
	 * system resize south cursor (value is 11)
	 */
	public static final int CURSOR_SIZES = 11;
		
	/**
	 * system resize east cursor (value is 12)
	 */
	public static final int CURSOR_SIZEE = 12;
		
	/**
	 * system resize west cursor (value is 13)
	 */
	public static final int CURSOR_SIZEW = 13;
		
	/**
	 * system resize north-east cursor (value is 14)
	 */
	public static final int CURSOR_SIZENE = 14;
		
	/**
	 * system resize south-east cursor (value is 15)
	 */
	public static final int CURSOR_SIZESE = 15;
		
	/**
	 * system resize south-west cursor (value is 16)
	 */
	public static final int CURSOR_SIZESW = 16;
		
	/**
	 * system resize north-west cursor (value is 17)
	 */
	public static final int CURSOR_SIZENW = 17;
		
	/**
	 * system up arrow cursor (value is 18)
	 */
	public static final int CURSOR_UPARROW = 18;
		
	/**
	 * system i-beam cursor (value is 19)
	 */
	public static final int CURSOR_IBEAM = 19;
		
	/**
	 * system "not allowed" cursor (value is 20)
	 */
	public static final int CURSOR_NO = 20;
		
	/**
	 * system hand cursor (value is 21)
	 */
	public static final int CURSOR_HAND = 21;
		
	/**
	 * line drawing style for solid lines (value is 1)
	 */
	public static final int LINE_SOLID = 1;
		
	/**
	 * line drawing style for dashed lines (value is 2)
	 */
	public static final int LINE_DASH = 2;
		
	/**
	 * line drawing style for dotted lines (value is 3)
	 */
	public static final int LINE_DOT = 3;
		
	/**
	 * line drawing style for alternating dash-dot lines (value is 4)
	 */
	public static final int LINE_DASHDOT = 4;
		
	/**
	 * line drawing style for dash-dot-dot lines (value is 5)
	 */
	public static final int LINE_DASHDOTDOT = 5;

	/**
	 * image format constant indicating an unknown image type (value is -1)
	 */
	public static final int IMAGE_UNDEFINED = -1;

	/**
	 * image format constant indicating a Windows BMP format image (value is 0)
	 */
	public static final int IMAGE_BMP = 0;

	/**
	 * image format constant indicating a run-length encoded 
	 * Windows BMP format image (value is 1)
	 */
	public static final int IMAGE_BMP_RLE = 1;

	/**
	 * image format constant indicating a GIF format image (value is 2)
	 */
	public static final int IMAGE_GIF = 2;

	/**
	 * image format constant indicating a ICO format image (value is 3)
	 */
	public static final int IMAGE_ICO = 3;

	/**
	 * image format constant indicating a JPEG format image (value is 4)
	 */
	public static final int IMAGE_JPEG = 4;

	/**
	 * image format constant indicating a PNG format image (value is 5)
	 */
	public static final int IMAGE_PNG = 5;

	/**
	 * GIF image disposal method constants indicating that the
	 * disposal method is unspecified (value is 0)
	 */
	public static final int DM_UNSPECIFIED = 0x0;

	/**
	 * GIF image disposal method constants indicating that the
	 * disposal method is to do nothing. That is, to leave the
	 * previous image in place (value is 1)
	 */
	public static final int DM_FILL_NONE = 0x1;

	/**
	 * GIF image disposal method constants indicating that the
	 * the previous images should be covered with the background
	 * color before displaying the next image (value is 2)
	 */
	public static final int DM_FILL_BACKGROUND = 0x2;

	/**
	 * GIF image disposal method constants indicating that the
	 * disposal method is to restore the previous picture
	 * (value is 3)
	 */
	public static final int DM_FILL_PREVIOUS = 0x3;
	
	/**
	 * image transparency constant indicating that the image
	 * contains no transparency information (value is 0)
	 */
	public static final int TRANSPARENCY_NONE = 0x0;
	
	/**
	 * image transparency constant indicating that the image
	 * contains no transparency information (value is 1&lt;&lt;0)
	 */
	public static final int TRANSPARENCY_ALPHA = 1 << 0;
	
	/**
	 * image transparency constant indicating that the image
	 * contains no transparency information (value is 1&lt;&lt;1)
	 */
	public static final int TRANSPARENCY_MASK = 1 << 1;
	
	/**
	 * image transparency constant indicating that the image
	 * contains no transparency information (value is 1&lt;&lt;2)
	 */
	public static final int TRANSPARENCY_PIXEL = 1 << 2;

/**
 * Answers a concise, human readable description of the error code.
 *
 * @param code the SWT error code.
 * @return a description of the error code.
 *
 * @see SWT
 */
static String findErrorText (int code) {
	switch (code) {
		case ERROR_UNSPECIFIED:            return "Unspecified error";
		case ERROR_NO_HANDLES:			   return "No more handles";
		case ERROR_NO_MORE_CALLBACKS:      return "No more callbacks";
		case ERROR_NULL_ARGUMENT:          return "Argument cannot be null";
		case ERROR_INVALID_ARGUMENT:       return "Argument not valid";
		case ERROR_INVALID_RANGE:          return "Index out of bounds";
		case ERROR_CANNOT_BE_ZERO:         return "Argument cannot be zero";
		case ERROR_CANNOT_GET_ITEM:        return "Cannot get item";
		case ERROR_CANNOT_GET_SELECTION:   return "Cannot get selection";
		case ERROR_CANNOT_GET_ITEM_HEIGHT: return "Cannot get item height";
		case ERROR_CANNOT_GET_TEXT:        return "Cannot get text";
		case ERROR_CANNOT_SET_TEXT:        return "Cannot set text";
		case ERROR_ITEM_NOT_ADDED:         return "Item not added";
		case ERROR_ITEM_NOT_REMOVED:       return "Item not removed";
		case ERROR_NOT_IMPLEMENTED:        return "Not implemented";
		case ERROR_MENU_NOT_DROP_DOWN:     return "Menu must be a drop down";
		case ERROR_THREAD_INVALID_ACCESS:  return "Invalid thread access";
		case ERROR_WIDGET_DISPOSED:        return "Widget is disposed";
		case ERROR_MENUITEM_NOT_CASCADE:   return "Menu item is not a CASCADE"; 
		case ERROR_CANNOT_SET_SELECTION:   return "Cannot set selection"; 
		case ERROR_CANNOT_SET_MENU:        return "Cannot set menu"; 
		case ERROR_CANNOT_SET_ENABLED:     return "Cannot set the enabled state"; 
		case ERROR_CANNOT_GET_ENABLED:     return "Cannot get the enabled state"; 
		case ERROR_INVALID_PARENT:         return "Widget has the wrong parent"; 
		case ERROR_MENU_NOT_BAR:           return "Menu is not a BAR"; 
		case ERROR_CANNOT_GET_COUNT:       return "Cannot get count";
		case ERROR_MENU_NOT_POP_UP:        return "Menu is not a POP_UP";
		case ERROR_UNSUPPORTED_DEPTH:      return "Unsupported color depth";
		case ERROR_IO:                     return "i/o error";
		case ERROR_INVALID_IMAGE:          return "Invalid image";
		case ERROR_UNSUPPORTED_FORMAT:     return "Unsupported or unrecognized format";
		case ERROR_INVALID_SUBCLASS:       return "Subclassing not allowed";
		case ERROR_GRAPHIC_DISPOSED:       return "Graphic is disposed";
		case ERROR_DEVICE_DISPOSED:        return "Device is disposed";
		case ERROR_FAILED_EXEC:            return "Failed to execute runnable";
		case ERROR_FAILED_LOAD_LIBRARY:    return "Unable to load library";
	}
	return "Unknown error";
}

/**
 * Returns the NLS'ed message for the given argument.
 * 
 * @param key the key to look up
 * @return the message for the given key
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the key is null</li>
 * </ul>
 */
public static String getMessage(String key) {
	return Compatibility.getMessage(key);
}
	
/**
 * Returns the SWT platform name.
 * Examples: "win32", "motif", "gtk", "photon"
 *
 * @return the SWT version number
 */
public static String getPlatform () {
	return Callback.getPlatform ();
}

/**
 * Returns the SWT version number as an integer.
 * Example: "SWT051" == 51
 *
 * @return the SWT version number
 */
public static int getVersion () {
	return Library.getVersion ();
}

/**
 * Throws an appropriate exception based on the passed in error code.
 *
 * @param code the SWT error code
 */
public static void error (int code) {
	error (code, null);
}

/**
 * Throws an appropriate exception based on the passed in error code.
 * The <code>throwable</code> argument should be either null, or the
 * throwable which caused SWT to throw an exception.
 * <p>
 * In SWT, errors are reported by throwing one of three exceptions:
 * <dl>
 * <dd>java.lang.IllegalArgumentException</dd>
 * <dt>thrown whenever one of the API methods is invoked with an illegal argument</dt>
 * <dd>org.eclipse.swt.SWTException (extends java.lang.RuntimeException)</dd>
 * <dt>thrown whenever a recoverable error happens internally in SWT</dt>
 * <dd>org.eclipse.swt.SWTError (extends java.lang.Error)</dd>
 * <dt>thrown whenever a <b>non-recoverable</b> error happens internally in SWT</dt>
 * </dl>
 * This method provides the logic which maps between error codes
 * and one of the above exceptions.
 * </p>
 *
 * @param code the SWT error code.
 * @param throwable the exception which caused the error to occur.
 *
 * @see SWTError
 * @see SWTException
 * @see IllegalArgumentException
 */
public static void error (int code, Throwable throwable) {

	// This code prevents the creation of "chains" of SWTErrors and
	// SWTExceptions which in turn contain other SWTErrors and 
	// SWTExceptions as their throwable. This can occur when low level
	// code throws an exception past a point where a higher layer is
	// being "safe" and catching all exceptions. (Note that, this is
	// _a_bad_thing_ which we always try to avoid.)
	//
	// On the theory that the low level code is closest to the
	// original problem, we simply re-throw the original exception here.
	if (throwable instanceof SWTError) throw (SWTError) throwable;
	if (throwable instanceof SWTException) throw (SWTException) throwable;
		
	switch (code) {
		
		// Illegal Arguments (non-fatal)
		case ERROR_NULL_ARGUMENT: 
		case ERROR_CANNOT_BE_ZERO:
		case ERROR_INVALID_ARGUMENT:
		case ERROR_MENU_NOT_BAR:
		case ERROR_MENU_NOT_DROP_DOWN:
		case ERROR_MENU_NOT_POP_UP:
		case ERROR_MENUITEM_NOT_CASCADE:
		case ERROR_INVALID_PARENT: 		
		case ERROR_INVALID_RANGE: {
			throw new IllegalArgumentException (findErrorText (code));
		}
		
		// SWT Errors (non-fatal)
		case ERROR_INVALID_SUBCLASS:
		case ERROR_THREAD_INVALID_ACCESS:
		case ERROR_WIDGET_DISPOSED:
		case ERROR_GRAPHIC_DISPOSED:
		case ERROR_DEVICE_DISPOSED:
		case ERROR_INVALID_IMAGE:
		case ERROR_UNSUPPORTED_DEPTH:
		case ERROR_UNSUPPORTED_FORMAT:
		case ERROR_FAILED_EXEC:
		case ERROR_IO: {
			SWTException exception = new SWTException (code);
			exception.throwable = throwable;
			throw exception;
		}
		
		// OS Failure/Limit (fatal, may occur only on some platforms)
		case ERROR_CANNOT_GET_COUNT:
		case ERROR_CANNOT_GET_ENABLED:
		case ERROR_CANNOT_GET_ITEM:
		case ERROR_CANNOT_GET_ITEM_HEIGHT:
		case ERROR_CANNOT_GET_SELECTION:
		case ERROR_CANNOT_GET_TEXT:
		case ERROR_CANNOT_SET_ENABLED:
		case ERROR_CANNOT_SET_MENU:
		case ERROR_CANNOT_SET_SELECTION:
		case ERROR_CANNOT_SET_TEXT:
		case ERROR_ITEM_NOT_ADDED:
		case ERROR_ITEM_NOT_REMOVED:
		case ERROR_NO_HANDLES: // fall through
		
		// SWT Failure/Limit (fatal, may occur only on some platforms)
		case ERROR_FAILED_LOAD_LIBRARY:
		case ERROR_NO_MORE_CALLBACKS:
		case ERROR_NOT_IMPLEMENTED:
		case ERROR_UNSPECIFIED: {
			SWTError error = new SWTError (code);
			error.throwable = throwable;
			throw error;
		}
	}
	
	// Unknown/Undefined Error
	SWTError error = new SWTError (code);
	error.throwable = throwable;
	throw error;
}

}
