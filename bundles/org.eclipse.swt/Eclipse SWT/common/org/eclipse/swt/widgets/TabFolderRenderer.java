/*******************************************************************************
 * Copyright (c) 2000, 2020 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 455263
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class provide all of the measuring and drawing functionality
 * required by <code>CTabFolder</code>. This class can be subclassed in order to
 * customize the look of a CTabFolder.
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @since 3.6
 */
public abstract class TabFolderRenderer extends ControlRenderer {

	int curveIndent = 0;

	protected TabFolder parent;

	// TOP_LEFT_CORNER_HILITE is laid out in reverse (ie. top to bottom)
	// so can fade in same direction as right swoop curve
	static final int[] TOP_LEFT_CORNER_HILITE = new int[] { 5, 2, 4, 2, 3, 3, 2, 4, 2, 5, 1, 6 };

	static final int[] TOP_LEFT_CORNER = new int[] { 0, 6, 1, 5, 1, 4, 4, 1, 5, 1, 6, 0 };
	static final int[] TOP_RIGHT_CORNER = new int[] { -6, 0, -5, 1, -4, 1, -1, 4, -1, 5, 0, 6 };
	static final int[] BOTTOM_LEFT_CORNER = new int[] { 0, -6, 1, -5, 1, -4, 4, -1, 5, -1, 6, 0 };
	static final int[] BOTTOM_RIGHT_CORNER = new int[] { -6, 0, -5, -1, -4, -1, -1, -4, -1, -5, 0, -6 };

	static final int[] SIMPLE_TOP_LEFT_CORNER = new int[] { 0, 2, 1, 1, 2, 0 };
	static final int[] SIMPLE_TOP_RIGHT_CORNER = new int[] { -2, 0, -1, 1, 0, 2 };
	static final int[] SIMPLE_BOTTOM_LEFT_CORNER = new int[] { 0, -2, 1, -1, 2, 0 };
	static final int[] SIMPLE_BOTTOM_RIGHT_CORNER = new int[] { -2, 0, -1, -1, 0, -2 };
	static final int[] SIMPLE_UNSELECTED_INNER_CORNER = new int[] { 0, 0 };

	static final int[] TOP_LEFT_CORNER_BORDERLESS = new int[] { 0, 6, 1, 5, 1, 4, 4, 1, 5, 1, 6, 0 };
	static final int[] TOP_RIGHT_CORNER_BORDERLESS = new int[] { -7, 0, -6, 1, -5, 1, -2, 4, -2, 5, -1, 6 };
	static final int[] BOTTOM_LEFT_CORNER_BORDERLESS = new int[] { 0, -6, 1, -6, 1, -5, 2, -4, 4, -2, 5, -1, 6, -1, 6,
			0 };
	static final int[] BOTTOM_RIGHT_CORNER_BORDERLESS = new int[] { -7, 0, -7, -1, -6, -1, -5, -2, -3, -4, -2, -5, -2,
			-6, -1, -6 };

	static final int[] SIMPLE_TOP_LEFT_CORNER_BORDERLESS = new int[] { 0, 2, 1, 1, 2, 0 };
	static final int[] SIMPLE_TOP_RIGHT_CORNER_BORDERLESS = new int[] { -3, 0, -2, 1, -1, 2 };
	static final int[] SIMPLE_BOTTOM_LEFT_CORNER_BORDERLESS = new int[] { 0, -3, 1, -2, 2, -1, 3, 0 };
	static final int[] SIMPLE_BOTTOM_RIGHT_CORNER_BORDERLESS = new int[] { -4, 0, -3, -1, -2, -2, -1, -3 };

	static final RGB CLOSE_FILL = new RGB(240, 64, 64);

	static final int BUTTON_SIZE = 16;
	static final int BUTTON_TRIM = 1;

	static final int BUTTON_BORDER = SWT.COLOR_WIDGET_DARK_SHADOW;
	static final int BUTTON_FILL = SWT.COLOR_LIST_BACKGROUND;
	static final int BORDER1_COLOR = SWT.COLOR_WIDGET_NORMAL_SHADOW;

	static final int ITEM_TOP_MARGIN = 2;
	static final int ITEM_BOTTOM_MARGIN = 2;
	static final int ITEM_LEFT_MARGIN = 4;
	static final int ITEM_RIGHT_MARGIN = 4;
	static final int INTERNAL_SPACING = 4;
	static final int TABS_WITHOUT_ICONS_PADDING = 14;
	static final int FLAGS = SWT.DRAW_TRANSPARENT | SWT.DRAW_MNEMONIC | SWT.DRAW_DELIMITER;
	static final String ELLIPSIS = "..."; //$NON-NLS-1$
	protected static final String CHEVRON_ELLIPSIS = "99+"; //$NON-NLS-1$
	protected static final float CHEVRON_FONT_SIZE_FACTOR = 0.7f;
	protected static final int CHEVRON_BOTTOM_INDENT = 4;

	// Part constants
	/**
	 * Part constant indicating the body of the tab folder. The body is the
	 * underlying container for all of the tab folder and all other parts are drawn
	 * on top of it. (value is -1).
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 */
	public static final int PART_BODY = -1;
	/**
	 * Part constant indicating the tab header of the folder (value is -2). The
	 * header is drawn on top of the body and provides an area for the tabs and
	 * other tab folder buttons to be rendered.
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 */
	public static final int PART_HEADER = -2;
	/**
	 * Part constant indicating the border of the tab folder. (value is -3). The
	 * border is drawn around the body and is part of the body trim.
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 */
	public static final int PART_BORDER = -3;
	/**
	 * Part constant indicating the background of the tab folder. (value is -4).
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 */
	public static final int PART_BACKGROUND = -4;
	/**
	 * Part constant indicating the maximize button of the tab folder. (value is
	 * -5).
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 */
	public static final int PART_MAX_BUTTON = -5;
	/**
	 * Part constant indicating the minimize button of the tab folder. (value is
	 * -6).
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 */
	public static final int PART_MIN_BUTTON = -6;
	/**
	 * Part constant indicating the chevron button of the tab folder. (value is -7).
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 */
	public static final int PART_CHEVRON_BUTTON = -7;
	/**
	 * Part constant indicating the close button of a tab item. (value is -8).
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 */
	public static final int PART_CLOSE_BUTTON = -8;

	public static final int MINIMUM_SIZE = 1 << 24; // TODO: Should this be a state?

	/**
	 * Constructs a new instance of this class given its parent.
	 *
	 * @param parent CTabFolder
	 *
	 * @exception IllegalArgumentException <ul>
	 *    <li>ERROR_INVALID_ARGUMENT - if the parent is disposed</li>
	 * </ul>
	 *
	 * @see Widget#getStyle
	 */
	protected TabFolderRenderer(TabFolder parent) {
		super(parent);
		if (parent == null) return;
		if (parent.isDisposed ()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		this.parent = parent;
	}

	/**
	 * Given a desired <em>client area</em> for the part (as described by the
	 * arguments), returns the bounding rectangle which would be required to produce
	 * that client area.
	 * <p>
	 * In other words, it returns a rectangle such that, if the part's bounds were
	 * set to that rectangle, the area of the part which is capable of displaying
	 * data (that is, not covered by the "trimmings") would be the rectangle
	 * described by the arguments (relative to the receiver's parent).
	 * </p>
	 *
	 * @param part   one of the part constants
	 * @param state  the state of the part
	 * @param x      the desired x coordinate of the client area
	 * @param y      the desired y coordinate of the client area
	 * @param width  the desired width of the client area
	 * @param height the desired height of the client area
	 * @return the required bounds to produce the given client area
	 *
	 * @see TabFolderRenderer#computeSize(int, int, GC, int, int) valid part and
	 *      state values
	 *
	 * @since 3.6
	 */
	protected abstract Rectangle computeTrim(int part, int state, int x, int y, int width, int height);

	/**
	 * Returns the preferred size of a part.
	 * <p>
	 * The <em>preferred size</em> of a part is the size that it would best be
	 * displayed at. The width hint and height hint arguments allow the caller to
	 * ask a control questions such as "Given a particular width, how high does the
	 * part need to be to show all of the contents?" To indicate that the caller
	 * does not wish to constrain a particular dimension, the constant
	 * <code>SWT.DEFAULT</code> is passed for the hint.
	 * </p>
	 * <p>
	 * The <code>part</code> value indicated what component the preferred size is to
	 * be calculated for. Valid values are any of the part constants:
	 * </p>
	 * <ul>
	 * <li>PART_BODY</li>
	 * <li>PART_HEADER</li>
	 * <li>PART_BORDER</li>
	 * <li>PART_BACKGROUND</li>
	 * <li>PART_MAX_BUTTON</li>
	 * <li>PART_MIN_BUTTON</li>
	 * <li>PART_CHEVRON_BUTTON</li>
	 * <li>PART_CLOSE_BUTTON</li>
	 * <li>A positive integer which is the index of an item in the CTabFolder.</li>
	 * </ul>
	 * <p>
	 * The <code>state</code> parameter may be one of the following:
	 * </p>
	 * <ul>
	 * <li>SWT.NONE</li>
	 * <li>SWT.SELECTED - whether the part is selected</li>
	 * </ul>
	 *
	 * @param part  a part constant
	 * @param state current state
	 * @param gc    the gc to use for measuring
	 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
	 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
	 * @return the preferred size of the part
	 *
	 * @since 3.6
	 */
	protected abstract Point computeSize(int part, int state, GC gc, int wHint, int hHint);

	/**
	 * Draw a specified <code>part</code> of the CTabFolder using the provided
	 * <code>bounds</code> and <code>GC</code>.
	 * <p>
	 * The valid CTabFolder <code>part</code> constants are:
	 * </p>
	 * <ul>
	 * <li>PART_BODY - the entire body of the CTabFolder</li>
	 * <li>PART_HEADER - the upper tab area of the CTabFolder</li>
	 * <li>PART_BORDER - the border of the CTabFolder</li>
	 * <li>PART_BACKGROUND - the background of the CTabFolder</li>
	 * <li>PART_MAX_BUTTON</li>
	 * <li>PART_MIN_BUTTON</li>
	 * <li>PART_CHEVRON_BUTTON</li>
	 * <li>PART_CLOSE_BUTTON</li>
	 * <li>A positive integer which is the index of an item in the CTabFolder.</li>
	 * </ul>
	 * <p>
	 * The <code>state</code> parameter may be a combination of:
	 * </p>
	 * <ul>
	 * <li>SWT.BACKGROUND - whether the background should be drawn</li>
	 * <li>SWT.FOREGROUND - whether the foreground should be drawn</li>
	 * <li>SWT.SELECTED - whether the part is selected</li>
	 * <li>SWT.HOT - whether the part is hot (i.e. mouse is over the part)</li>
	 * </ul>
	 *
	 * @param part   part to draw
	 * @param state  state of the part
	 * @param bounds the bounds of the part
	 * @param gc     the gc to draw the part on
	 *
	 * @since 3.6
	 */
	protected abstract void draw(int part, int state, Rectangle bounds, GC gc);

	/**
	 * Dispose of any operating system resources associated with the renderer.
	 * Called by the CTabFolder parent upon receiving the dispose event or when
	 * changing the renderer.
	 *
	 * @since 3.6
	 */
	protected abstract void dispose();

	protected abstract void createAntialiasColors();

	protected abstract void setSelectionHighlightGradientColor(Color start);

	protected abstract void disposeSelectionHighlightGradientColors();

	protected abstract void resetChevronFont();

}
