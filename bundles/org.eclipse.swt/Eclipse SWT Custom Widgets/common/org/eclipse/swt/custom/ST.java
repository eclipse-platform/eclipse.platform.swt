package org.eclipse.swt.custom;
/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */


/**
 * This class provides access to the public constants provided by <code>StyledText</code>.
 */
public class ST {
	
	/* StyledText key action constants.  Key bindings for the actions are set
	 * by the StyledText widget.  @see StyledText#createKeyBindings()
	 */
	 
	/* Navigation Key Actions */	
	public static final int LINE_UP = (1 << 24) + 1; 				// binding = SWT.ARROW_UP
	public static final int LINE_DOWN = (1 << 24) + 2; 				// binding = SWT.ARROW_DOWN
	public static final int LINE_START = (1 << 24) + 7; 				// binding = SWT.HOME
	public static final int LINE_END = (1 << 24) + 8; 				// binding = SWT.END
	public static final int COLUMN_PREVIOUS = (1 << 24) + 3; 		// binding = SWT.ARROW_LEFT
	public static final int COLUMN_NEXT = (1 << 24) + 4; 			// binding = SWT.ARROW_RIGHT
	public static final int PAGE_UP = (1 << 24) + 5; 				// binding = SWT.PAGE_UP
	public static final int PAGE_DOWN = (1 << 24) + 6; 				// binding = SWT.PAGE_DOWN
	public static final int WORD_PREVIOUS = (1 << 24) + 3 | 1 << 18;	// binding = SWT.MOD1 + SWT.ARROW_LEFT
	public static final int WORD_NEXT = (1 << 24) + 4 | 1 << 18; 	// binding = SWT.MOD1 + SWT.ARROW_RIGHT
	public static final int TEXT_START = (1 << 24) + 7 | 1 << 18; 	// binding = SWT.MOD1 + SWT.HOME
	public static final int TEXT_END = (1 << 24) + 8 | 1 << 18; 		// binding = SWT.MOD1 + SWT.END
	public static final int WINDOW_START = (1 << 24) + 5 | 1 << 18; 	// binding = SWT.MOD1 + SWT.PAGE_UP
	public static final int WINDOW_END = (1 << 24) + 6 | 1 << 18; 	// binding = SWT.MOD1 + SWT.PAGE_DOWN

	/* Selection Key Actions */
	public static final int SELECT_LINE_UP = (1 << 24) + 1 | 1 << 17; 					// binding = SWT.MOD2 + SWT.ARROW_UP
	public static final int SELECT_LINE_DOWN = (1 << 24) + 2 | 1 << 17; 					// binding = SWT.MOD2 + SWT.ARROW_DOWN
	public static final int SELECT_LINE_START = (1 << 24) + 7 | 1 << 17; 				// binding = SWT.MOD2 + SWT.HOME
	public static final int SELECT_LINE_END = (1 << 24) + 8 | 1 << 17; 					// binding = SWT.MOD2 + SWT.END
	public static final int SELECT_COLUMN_PREVIOUS = (1 << 24) + 3 | 1 << 17; 			// binding = SWT.MOD2 + SWT.ARROW_LEFT
	public static final int SELECT_COLUMN_NEXT = (1 << 24) + 4 | 1 << 17; 				// binding = SWT.MOD2 + SWT.ARROW_RIGHT
	public static final int SELECT_PAGE_UP = (1 << 24) + 5 | 1 << 17; 					// binding = SWT.MOD2 + SWT.PAGE_UP
	public static final int SELECT_PAGE_DOWN = (1 << 24) + 6 | 1 << 17; 					// binding = SWT.MOD2 + SWT.PAGE_DOWN
	public static final int SELECT_WORD_PREVIOUS = (1 << 24) + 3 | 1 << 18 | 1 << 17;	// binding = SWT.MOD1 + SWT.MOD2 + SWT.ARROW_LEFT
	public static final int SELECT_WORD_NEXT = (1 << 24) + 4 | 1 << 18 | 1 << 17; 		// binding = SWT.MOD1 + SWT.MOD2 + SWT.ARROW_RIGHT
	public static final int SELECT_TEXT_START = (1 << 24) + 7 | 1 << 18 | 1 << 17; 		// binding = SWT.MOD1 + SWT.MOD2 + SWT.HOME
	public static final int SELECT_TEXT_END = (1 << 24) + 8 | 1 << 18 | 1 << 17; 		// binding = SWT.MOD1 + SWT.MOD2 + SWT.END
	public static final int SELECT_WINDOW_START = (1 << 24) + 5 | 1 << 18 | 1 << 17; 	// binding = SWT.MOD1 + SWT.MOD2 + SWT.PAGE_UP
	public static final int SELECT_WINDOW_END = (1 << 24) + 6 | 1 << 18 | 1 << 17; 		// binding = SWT.MOD1 + SWT.MOD2 + SWT.PAGE_DOWN

	/* Modification Key Actions */
	public static final int CUT = 0x7F| 1 << 17; 			// binding = SWT.MOD2 + SWT.DEL
	public static final int COPY = (1 << 24) + 9 | 1 << 18; 	// binding = SWT.MOD1 + SWT.INSERT;
	public static final int PASTE = (1 << 24) + 9 | 1 << 17;	// binding = SWT.MOD2 + SWT.INSERT ;
	public static final int DELETE_PREVIOUS = '\b'; 			// binding = SWT.BS;
	public static final int DELETE_NEXT = 0x7F; 				// binding = SWT.DEL;

	/* Miscellaneous Key Actions */
	public static final int TOGGLE_OVERWRITE = (1 << 24) + 9; // binding = SWT.INSERT;
}
