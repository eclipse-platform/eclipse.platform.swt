package org.eclipse.swt.custom;
/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;

/**
 * This class provides access to the public constants provided by <code>StyledText</code>.
 */
public class ST {
	
	/* StyledText key action constants */
	/* Navigation */	
	public static final int LINE_UP = SWT.ARROW_UP;
	public static final int LINE_DOWN = SWT.ARROW_DOWN;
	public static final int LINE_START = SWT.HOME;
	public static final int LINE_END = SWT.END;
	public static final int COLUMN_PREVIOUS = SWT.ARROW_LEFT;
	public static final int COLUMN_NEXT = SWT.ARROW_RIGHT;
	public static final int PAGE_UP = SWT.PAGE_UP;
	public static final int PAGE_DOWN = SWT.PAGE_DOWN;
	public static final int WORD_PREVIOUS = SWT.ARROW_LEFT | SWT.CTRL;
	public static final int WORD_NEXT = SWT.ARROW_RIGHT | SWT.CTRL;
	public static final int TEXT_START = SWT.HOME | SWT.CTRL;
	public static final int TEXT_END = SWT.END | SWT.CTRL;
	public static final int WINDOW_START = SWT.PAGE_UP | SWT.CTRL;
	public static final int WINDOW_END = SWT.PAGE_DOWN | SWT.CTRL;

	/* Selection */
	public static final int SELECT_LINE_UP = SWT.ARROW_UP | SWT.SHIFT;
	public static final int SELECT_LINE_DOWN = SWT.ARROW_DOWN | SWT.SHIFT;
	public static final int SELECT_LINE_START = SWT.HOME | SWT.SHIFT;
	public static final int SELECT_LINE_END = SWT.END | SWT.SHIFT;
	public static final int SELECT_COLUMN_PREVIOUS = SWT.ARROW_LEFT | SWT.SHIFT;
	public static final int SELECT_COLUMN_NEXT = SWT.ARROW_RIGHT | SWT.SHIFT;
	public static final int SELECT_PAGE_UP = SWT.PAGE_UP | SWT.SHIFT;
	public static final int SELECT_PAGE_DOWN = SWT.PAGE_DOWN | SWT.SHIFT;
	public static final int SELECT_WORD_PREVIOUS = SWT.ARROW_LEFT | SWT.CTRL | SWT.SHIFT;
	public static final int SELECT_WORD_NEXT = SWT.ARROW_RIGHT | SWT.CTRL | SWT.SHIFT;
	public static final int SELECT_TEXT_START = SWT.HOME | SWT.CTRL | SWT.SHIFT;
	public static final int SELECT_TEXT_END = SWT.END | SWT.CTRL | SWT.SHIFT;
	public static final int SELECT_WINDOW_START = SWT.PAGE_UP | SWT.CTRL| SWT.SHIFT;
	public static final int SELECT_WINDOW_END = SWT.PAGE_DOWN | SWT.CTRL | SWT.SHIFT;

	/* Modification */
	public static final int CUT = SWT.DEL | SWT.SHIFT;
	public static final int COPY = SWT.INSERT | SWT.CTRL;
	public static final int PASTE = SWT.INSERT | SWT.SHIFT;
	public static final int DELETE_PREVIOUS = SWT.BS;
	public static final int DELETE_NEXT = SWT.DEL;

	/* Miscellaneous */
	public static final int TOGGLE_OVERWRITE = SWT.INSERT;
	
	/*
	 * Print options used in StyledTextPrintOptions 	 */
	/**
	 * Print the text foreground color defined in style ranges
	 * (value is 1). 	 */		
	public static final int TEXT_FOREGROUND = 1;
	/**
	 * Print the text background color defined in style ranges
	 * (value is 1&lt;&lt;1). 
	 */		
  	public static final int TEXT_BACKGROUND = 1 << 1;
	/**
	 * Print the font style defined in style ranges
	 * (value is 1&lt;&lt;2). 
	 */		
  	public static final int TEXT_FONT_STYLE = 1 << 2;
	/**
	 * Print the line background color (value is 1&lt;&lt;3). 
	 */
  	public static final int LINE_BACKGROUND = 1 << 3;
}
