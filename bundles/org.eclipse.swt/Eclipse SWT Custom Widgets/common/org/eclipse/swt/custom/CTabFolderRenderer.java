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
package org.eclipse.swt.custom;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * Instances of this class provide all of the measuring and drawing functionality
 * required by <code>CTabFolder</code>. This class can be subclassed in order to
 * customize the look of a CTabFolder.
 *
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @since 3.6
 */
public class CTabFolderRenderer {

	protected CTabFolder parent;

	Color fillColor;

	private Font chevronFont = null;

	static final RGB CLOSE_FILL = new RGB(240, 64, 64);

	static final int BUTTON_SIZE = 16;
	static final int BUTTON_TRIM = 1;

	static final int BUTTON_BORDER = SWT.COLOR_WIDGET_DARK_SHADOW;
	static final int BUTTON_FILL = SWT.COLOR_LIST_BACKGROUND;
	static final int BORDER1_COLOR = SWT.COLOR_WIDGET_NORMAL_SHADOW;

	static final int ITEM_TOP_MARGIN = 3;
	static final int ITEM_BOTTOM_MARGIN = 3;
	static final int ITEM_LEFT_MARGIN = 4;
	static final int ITEM_RIGHT_MARGIN = 4;
	static final int INTERNAL_SPACING = 4;
	static final int TABS_WITHOUT_ICONS_PADDING = 14;
	static final int FLAGS = SWT.DRAW_TRANSPARENT | SWT.DRAW_MNEMONIC | SWT.DRAW_DELIMITER;
	static final String ELLIPSIS = "..."; //$NON-NLS-1$
	private static final String CHEVRON_ELLIPSIS = "99+"; //$NON-NLS-1$
	private static final float CHEVRON_FONT_SIZE_FACTOR = 0.7f;
	private static final int CHEVRON_BOTTOM_INDENT = 4;

	//Part constants
	/**
	 * Part constant indicating the body of the tab folder. The body is the
	 * underlying container for all of the tab folder and all other parts are
	 * drawn on top of it. (value is -1).
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 * @see #draw(int, int, Rectangle, GC)
	 */
	public static final int PART_BODY = -1;
	/**
	 * Part constant indicating the tab header of the folder (value is -2). The
	 * header is drawn on top of the body and provides an area for the tabs and
	 * other tab folder buttons to be rendered.
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 * @see #draw(int, int, Rectangle, GC)
	 */
	public static final int PART_HEADER = -2;
	/**
	 * Part constant indicating the border of the tab folder. (value is -3). The
	 * border is drawn around the body and is part of the body trim.
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 * @see #draw(int, int, Rectangle, GC)
	 */
	public static final int PART_BORDER = -3;
	/**
	 * Part constant indicating the background of the tab folder. (value is -4).
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 * @see #draw(int, int, Rectangle, GC)
	 */
	public static final int PART_BACKGROUND = -4;
	/**
	 * Part constant indicating the maximize button of the tab folder. (value is
	 * -5).
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 * @see #draw(int, int, Rectangle, GC)
	 */
	public static final int PART_MAX_BUTTON = -5;
	/**
	 * Part constant indicating the minimize button of the tab folder. (value is
	 * -6).
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 * @see #draw(int, int, Rectangle, GC)
	 */
	public static final int PART_MIN_BUTTON = -6;
	/**
	 * Part constant indicating the chevron button of the tab folder. (value is
	 * -7).
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 * @see #draw(int, int, Rectangle, GC)
	 */
	public static final int PART_CHEVRON_BUTTON = -7;
	/**
	 * Part constant indicating the close button of a tab item. (value is -8).
	 *
	 * @see #computeSize(int, int, GC, int, int)
	 * @see #computeTrim(int, int, int, int, int, int)
	 * @see #draw(int, int, Rectangle, GC)
	 */
	public static final int PART_CLOSE_BUTTON = -8;

	public static final int MINIMUM_SIZE = 1 << 24; //TODO: Should this be a state?


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
	protected CTabFolderRenderer(CTabFolder parent) {
		if (parent == null) return;
		if (parent.isDisposed ()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
		this.parent = parent;
	}

	/**
	 * Returns the preferred size of a part.
	 * <p>
	 * The <em>preferred size</em> of a part is the size that it would
	 * best be displayed at. The width hint and height hint arguments
	 * allow the caller to ask a control questions such as "Given a particular
	 * width, how high does the part need to be to show all of the contents?"
	 * To indicate that the caller does not wish to constrain a particular
	 * dimension, the constant <code>SWT.DEFAULT</code> is passed for the hint.
	 * </p><p>
	 * The <code>part</code> value indicated what component the preferred size is
	 * to be calculated for. Valid values are any of the part constants:
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
	 * @param part a part constant
	 * @param state current state
	 * @param gc the gc to use for measuring
	 * @param wHint the width hint (can be <code>SWT.DEFAULT</code>)
	 * @param hHint the height hint (can be <code>SWT.DEFAULT</code>)
	 * @return the preferred size of the part
	 *
	 * @since 3.6
	 */
	protected Point computeSize (int part, int state, GC gc, int wHint, int hHint) {
		int width = 0, height = 0;
		switch (part) {
			case PART_HEADER:
				if (parent.fixedTabHeight != SWT.DEFAULT) {
					height = parent.fixedTabHeight == 0 ? 0 : parent.fixedTabHeight + 1; // +1 for line drawn across top of tab
				} else {
					CTabItem[] items = parent.items;
					if (items.length == 0) {
						height = gc.textExtent("Default", FLAGS).y + ITEM_TOP_MARGIN + ITEM_BOTTOM_MARGIN; //$NON-NLS-1$
					} else {
						for (int i=0; i < items.length; i++) {
							height = Math.max(height, computeSize(i, SWT.NONE, gc, wHint, hHint).y);
						}
					}
					gc.dispose();
				}
				break;
			case PART_MAX_BUTTON:
			case PART_MIN_BUTTON:
			case PART_CLOSE_BUTTON:
				width = height = BUTTON_SIZE;
				break;
			case PART_CHEVRON_BUTTON:
				Font prevFont = gc.getFont();
				gc.setFont(getChevronFont(parent.getDisplay()));
				int widthOfDoubleArrows = 8; // see drawChevron method
				width = gc.textExtent(CHEVRON_ELLIPSIS).x + widthOfDoubleArrows;
				height = BUTTON_SIZE;
				gc.setFont(prevFont);
				break;
			default:
				if (0 <= part && part < parent.getItemCount()) {
					CTabItem item = parent.items[part];
					if (item.isDisposed()) return new Point(0,0);
					Image image = item.getImage();
					if (image != null && !image.isDisposed()) {
						Rectangle bounds = image.getBounds();
						if (((state & SWT.SELECTED) != 0 && parent.showSelectedImage)
								|| ((state & SWT.SELECTED) == 0 && parent.showUnselectedImage)) {
							width += bounds.width;
						}
						height = bounds.height;
					}
					String text = null;
					if ((state & MINIMUM_SIZE) != 0) {
						int minChars = parent.minChars;
						text = minChars == 0 ? null : item.getText();
						if (text != null && text.length() > minChars) {
							int end = minChars < ELLIPSIS.length() + 1 ? minChars : minChars - ELLIPSIS.length();
							text = text.substring(0, end);
							if (minChars > ELLIPSIS.length() + 1) text += ELLIPSIS;
						}
					} else {
						text = item.getText();
					}
					if (text != null) {
						if (width > 0) width += INTERNAL_SPACING;
						if (item.font == null) {
							Point size = gc.textExtent(text, FLAGS);
							width += size.x;
							height = Math.max(height, size.y);
						} else {
							Font gcFont = gc.getFont();
							gc.setFont(item.font);
							Point size = gc.textExtent(text, FLAGS);
							width += size.x;
							height = Math.max(height, size.y);
							gc.setFont(gcFont);
						}
					}

					if (shouldApplyLargeTextPadding(parent)) {
						width += getLargeTextPadding(item) * 2;
					} else if (shouldDrawCloseIcon(item)) {
						if (width > 0) width += INTERNAL_SPACING;
						width += computeSize(PART_CLOSE_BUTTON, SWT.NONE, gc, SWT.DEFAULT, SWT.DEFAULT).x;
					}
				}
				break;
		}
		Rectangle trim = computeTrim(part, state, 0, 0, width, height);
		width = trim.width;
		height = trim.height;
		return new Point(width, height);
	}

	private boolean shouldDrawCloseIcon(CTabItem item) {
		CTabFolder folder = item.getParent();
		boolean showClose = folder.showClose || item.showClose;
		boolean isSelectedOrShowCloseForUnselected = (item.state & SWT.SELECTED) != 0 || folder.showUnselectedClose;
		return showClose && isSelectedOrShowCloseForUnselected;
	}

	/**
	 * Returns padding for the text of a tab item when showing images is disabled for the tab folder.
	 */
	private int getLargeTextPadding(CTabItem item) {
		CTabFolder parent = item.getParent();
		String text = item.getText();

		if (text != null && parent.getMinimumCharacters() != 0) {
			return TABS_WITHOUT_ICONS_PADDING;
		}
		return 0;
	}

	private boolean shouldApplyLargeTextPadding(CTabFolder tabFolder) {
		return !tabFolder.showSelectedImage && !tabFolder.showUnselectedImage;
	}

	/**
	 * Given a desired <em>client area</em> for the part
	 * (as described by the arguments), returns the bounding
	 * rectangle which would be required to produce that client
	 * area.
	 * <p>
	 * In other words, it returns a rectangle such that, if the
	 * part's bounds were set to that rectangle, the area
	 * of the part which is capable of displaying data
	 * (that is, not covered by the "trimmings") would be the
	 * rectangle described by the arguments (relative to the
	 * receiver's parent).
	 * </p>
	 *
	 * @param part one of the part constants
	 * @param state the state of the part
	 * @param x the desired x coordinate of the client area
	 * @param y the desired y coordinate of the client area
	 * @param width the desired width of the client area
	 * @param height the desired height of the client area
	 * @return the required bounds to produce the given client area
	 *
	 * @see CTabFolderRenderer#computeSize(int, int, GC, int, int) valid part and state values
	 *
	 * @since 3.6
	 */
	protected Rectangle computeTrim (int part, int state, int x, int y, int width, int height) {
		int borderLeft = parent.borderVisible ? 1 : 0;
		int borderRight = borderLeft;
		int borderTop = parent.onBottom ? borderLeft : 0;
		int borderBottom = parent.onBottom ? 0 : borderLeft;
		int tabHeight = parent.tabHeight;
		switch (part) {
			case PART_BODY:
				int style = parent.getStyle();
				int highlight_header = (style & SWT.FLAT) != 0 ? 1 : 3;
				int highlight_margin = (style & SWT.FLAT) != 0 ? 0 : 2;
				if (parent.fixedTabHeight == 0 && (style & SWT.FLAT) != 0 && (style & SWT.BORDER) == 0) {
					highlight_header = 0;
				}
				int marginWidth = parent.marginWidth;
				int marginHeight = parent.marginHeight;
				x = x - marginWidth - highlight_margin - borderLeft;
				width = width + borderLeft + borderRight + 2*marginWidth + 2*highlight_margin;
				if (parent.minimized) {
					y = parent.onBottom ? y - borderTop : y - highlight_header - tabHeight - borderTop;
					height = borderTop + borderBottom + tabHeight + highlight_header;
				} else {
					y = parent.onBottom ? y - marginHeight - highlight_margin - borderTop : y - marginHeight - highlight_header - tabHeight - borderTop;
					height = height + borderTop + borderBottom + 2*marginHeight + tabHeight + highlight_header + highlight_margin;
				}
				break;
			case PART_HEADER:
				//no trim
				break;
			case PART_MAX_BUTTON:
			case PART_MIN_BUTTON:
			case PART_CLOSE_BUTTON:
			case PART_CHEVRON_BUTTON:
				x -= BUTTON_TRIM;
				y -= BUTTON_TRIM;
				width += BUTTON_TRIM*2;
				height += BUTTON_TRIM*2;
				break;
			case PART_BORDER:
				x = x - borderLeft;
				width = width + borderLeft + borderRight;
				y = y - borderTop;
				height = height + borderTop + borderBottom;
				break;
			default:
				if (0 <= part && part < parent.getItemCount()) {
					x = x - ITEM_LEFT_MARGIN;
					width = width + ITEM_LEFT_MARGIN + ITEM_RIGHT_MARGIN;
					y = y - ITEM_TOP_MARGIN;
					height = height + ITEM_TOP_MARGIN + ITEM_BOTTOM_MARGIN;
				}
				break;
		}
		return new Rectangle(x, y, width, height);
	}

	/**
	 * Dispose of any operating system resources associated with
	 * the renderer. Called by the CTabFolder parent upon receiving
	 * the dispose event or when changing the renderer.
	 *
	 * @since 3.6
	 */
	protected void dispose() {
		fillColor = null;

		if (chevronFont != null) {
			chevronFont.dispose();
			chevronFont = null;
		}
	}

	/**
	 * Draw a specified <code>part</code> of the CTabFolder using the provided <code>bounds</code> and <code>GC</code>.
	 * <p>The valid CTabFolder <code>part</code> constants are:
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
	 * @param part part to draw
	 * @param state state of the part
	 * @param bounds the bounds of the part
	 * @param gc the gc to draw the part on
	 *
	 * @since 3.6
	 */
	protected void draw (int part, int state, Rectangle bounds, GC gc) {
		switch (part) {
			case PART_BACKGROUND:
				this.drawBackground(gc, bounds, state);
				break;
			case PART_BODY:
				drawBody(gc, bounds, state);
				break;
			case PART_HEADER:
				drawTabArea(gc, bounds, state);
				break;
			case PART_MAX_BUTTON:
				drawMaximize(gc, bounds, state);
				break;
			case PART_MIN_BUTTON:
				drawMinimize(gc, bounds, state);
				break;
			case PART_CHEVRON_BUTTON:
				drawChevron(gc, bounds, state);
				break;
			default:
				if (0 <= part && part < parent.getItemCount()) {
					if (bounds.width == 0 || bounds.height == 0) return;
					if ((state & SWT.SELECTED) != 0 ) {
						drawSelected(part, gc, bounds, state);
					} else {
						drawUnselected(part, gc, bounds, state);
					}
				}
				break;
		}
	}

	void drawBackground(GC gc, Rectangle bounds, int state) {
		boolean selected = (state & SWT.SELECTED) != 0;
		Color defaultBackground = selected && parent.shouldHighlight() ? parent.selectionBackground : parent.getBackground();
		Image image = selected ? parent.selectionBgImage : null;
		Color[] colors = selected & parent.shouldHighlight() ? parent.selectionGradientColors : parent.gradientColors;
		int[] percents = selected ? parent.selectionGradientPercents : parent.gradientPercents;
		boolean vertical = selected ? parent.selectionGradientVertical : parent.gradientVertical;

		drawBackground(gc, null, bounds.x, bounds.y, bounds.width, bounds.height, defaultBackground, image, colors, percents, vertical);
	}

	void drawBackground(GC gc, int[] shape, boolean selected) {
		Color defaultBackground = selected && parent.shouldHighlight() ? parent.selectionBackground : parent.getBackground();
		Image image = selected ? parent.selectionBgImage : null;
		Color[] colors = selected && parent.shouldHighlight() ? parent.selectionGradientColors : parent.gradientColors;
		int[] percents = selected ? parent.selectionGradientPercents : parent.gradientPercents;
		boolean vertical = selected ? parent.selectionGradientVertical : parent.gradientVertical;
		Point size = parent.getSize();
		int width = size.x;
		int height = parent.tabHeight + ((parent.getStyle() & SWT.FLAT) != 0 ? 1 : 3);
		int x = 0;

		int borderLeft = parent.borderVisible ? 1 : 0;
		int borderTop = parent.onBottom ? borderLeft : 0;
		int borderBottom = parent.onBottom ? 0 : borderLeft;

		if (borderLeft > 0) {
			x += 1; width -= 2;
		}
		int y = parent.onBottom ? size.y - borderBottom - height : borderTop;
		drawBackground(gc, shape, x, y, width, height, defaultBackground, image, colors, percents, vertical);
	}

	void drawBackground(GC gc, int[] shape, int x, int y, int width, int height, Color defaultBackground, Image image, Color[] colors, int[] percents, boolean vertical) {
		Region clipping = null, region = null;
		if (shape != null) {
			clipping = new Region();
			gc.getClipping(clipping);
			region = new Region();
			region.add(shape);
			region.intersect(clipping);
			gc.setClipping(region);
		}
		if (image != null) {
			// draw the background image in shape
			gc.setBackground(defaultBackground);
			gc.fillRectangle(x, y, width, height);
			gc.drawImage(image, x, y, width, height);
		} else if (colors != null) {
			// draw gradient
			if (colors.length == 1) {
				Color background = colors[0] != null ? colors[0] : defaultBackground;
				gc.setBackground(background);
				gc.fillRectangle(x, y, width, height);
			} else {
				if (vertical) {
					if (parent.onBottom) {
						int pos = 0;
						if (percents[percents.length - 1] < 100) {
							pos = (100 - percents[percents.length - 1]) * height / 100;
							gc.setBackground(defaultBackground);
							gc.fillRectangle(x, y, width, pos);
						}
						Color lastColor = colors[colors.length-1];
						if (lastColor == null) lastColor = defaultBackground;
						for (int i = percents.length-1; i >= 0; i--) {
							gc.setForeground(lastColor);
							lastColor = colors[i];
							if (lastColor == null) lastColor = defaultBackground;
							gc.setBackground(lastColor);
							int percentage = i > 0 ? percents[i] - percents[i-1] : percents[i];
							int gradientHeight = percentage * height / 100;
							gc.fillGradientRectangle(x, y+pos, width, gradientHeight, true);
							pos += gradientHeight;
						}
					} else {
						Color lastColor = colors[0];
						if (lastColor == null) lastColor = defaultBackground;
						int pos = 0;
						for (int i = 0; i < percents.length; i++) {
							gc.setForeground(lastColor);
							lastColor = colors[i + 1];
							if (lastColor == null) lastColor = defaultBackground;
							gc.setBackground(lastColor);
							int percentage = i > 0 ? percents[i] - percents[i-1] : percents[i];
							int gradientHeight = percentage * height / 100;
							gc.fillGradientRectangle(x, y+pos, width, gradientHeight, true);
							pos += gradientHeight;
						}
						if (pos < height) {
							gc.setBackground(defaultBackground);
							gc.fillRectangle(x, pos, width, height-pos+1);
						}
					}
				} else { //horizontal gradient
					y = 0;
					height = parent.getSize().y;
					Color lastColor = colors[0];
					if (lastColor == null) lastColor = defaultBackground;
					int pos = 0;
					for (int i = 0; i < percents.length; ++i) {
						gc.setForeground(lastColor);
						lastColor = colors[i + 1];
						if (lastColor == null) lastColor = defaultBackground;
						gc.setBackground(lastColor);
						int gradientWidth = (percents[i] * width / 100) - pos;
						gc.fillGradientRectangle(x+pos, y, gradientWidth, height, false);
						pos += gradientWidth;
					}
					if (pos < width) {
						gc.setBackground(defaultBackground);
						gc.fillRectangle(x+pos, y, width-pos, height);
					}
				}
			}
		} else {
			// draw a solid background using default background in shape
			if ((parent.getStyle() & SWT.NO_BACKGROUND) != 0 || !defaultBackground.equals(parent.getBackground())) {
				gc.setBackground(defaultBackground);
				gc.fillRectangle(x, y, width, height);
			}
		}
		if (shape != null) {
			gc.setClipping(clipping);
			clipping.dispose();
			region.dispose();
		}
	}

	/*
	 * Draw the border of the tab
	 */
	void drawBorder(GC gc, int[] shape) {

		gc.setForeground(parent.getDisplay().getSystemColor(BORDER1_COLOR));
		gc.drawPolyline(shape);
	}

	void drawBody(GC gc, Rectangle bounds, int state) {
		Point size = new Point(bounds.width, bounds.height);
		int selectedIndex = parent.selectedIndex;
		int tabHeight = parent.tabHeight;

		int borderLeft = parent.borderVisible ? 1 : 0;
		int borderRight = borderLeft;
		int borderTop = parent.onBottom ? borderLeft : 0;
		int borderBottom = parent.onBottom ? 0 : borderLeft;

		int style = parent.getStyle();
		int highlight_header = (style & SWT.FLAT) != 0 ? 1 : 3;
		int highlight_margin = (style & SWT.FLAT) != 0 ? 0 : 2;

		// fill in body
		if (!parent.minimized){
			int width = size.x  - borderLeft - borderRight - 2*highlight_margin;
			int height = size.y - borderTop - borderBottom - tabHeight - highlight_header - highlight_margin;
			// Draw highlight margin
			if (highlight_margin > 0) {
				int[] shape = null;
				if (parent.onBottom) {
					int x1 = borderLeft;
					int y1 = borderTop;
					int x2 = size.x - borderRight;
					int y2 = size.y - borderBottom - tabHeight - highlight_header;
					shape = new int[] {x1,y1, x2,y1, x2,y2, x2-highlight_margin,y2,
									   x2-highlight_margin, y1+highlight_margin, x1+highlight_margin,y1+highlight_margin,
									   x1+highlight_margin,y2, x1,y2};
				} else {
					int x1 = borderLeft;
					int y1 = borderTop + tabHeight + highlight_header;
					int x2 = size.x - borderRight;
					int y2 = size.y - borderBottom;
					shape = new int[] {x1,y1, x1+highlight_margin,y1, x1+highlight_margin,y2-highlight_margin,
									   x2-highlight_margin,y2-highlight_margin, x2-highlight_margin,y1,
									   x2,y1, x2,y2, x1,y2};
				}
				// If horizontal gradient, show gradient across the whole area
				if (selectedIndex != -1 && parent.selectionGradientColors != null && parent.selectionGradientColors.length > 1 && !parent.selectionGradientVertical) {
					drawBackground(gc, shape, true);
				} else if (selectedIndex == -1 && parent.gradientColors != null && parent.gradientColors.length > 1 && !parent.gradientVertical) {
					drawBackground(gc, shape, false);
				} else {
					gc.setBackground(selectedIndex != -1 && parent.shouldHighlight() ? parent.selectionBackground : parent.getBackground());
					gc.fillPolygon(shape);
				}
			}
			//Draw client area
			if ((parent.getStyle() & SWT.NO_BACKGROUND) != 0) {
				gc.setBackground(parent.getBackground());
				int marginWidth = parent.marginWidth;
				int marginHeight = parent.marginHeight;
				int xClient = borderLeft + marginWidth + highlight_margin, yClient;
				if (parent.onBottom) {
					yClient = borderTop + highlight_margin + marginHeight;
				} else {
					yClient = borderTop + tabHeight + highlight_header + marginHeight;
				}
				gc.fillRectangle(xClient - marginWidth, yClient - marginHeight, width, height);
			}
		} else {
			if ((parent.getStyle() & SWT.NO_BACKGROUND) != 0) {
				int height = borderTop + tabHeight + highlight_header + borderBottom;
				if (size.y > height) {
					gc.setBackground(parent.getParent().getBackground());
					gc.fillRectangle(0, height, size.x, size.y - height);
				}
			}
		}

		//draw 1 pixel border around outside
		if (borderLeft > 0) {
			gc.setForeground(parent.getDisplay().getSystemColor(BORDER1_COLOR));
			int x1 = borderLeft - 1;
			int x2 = size.x - borderRight;
			int y1 = parent.onBottom ? borderTop - 1 : borderTop + tabHeight;
			int y2 = parent.onBottom ? size.y - tabHeight - borderBottom - 1 : size.y - borderBottom;
			gc.drawLine(x1, y1, x1, y2); // left
			gc.drawLine(x2, y1, x2, y2); // right
			if (parent.onBottom) {
				gc.drawLine(x1, y1, x2, y1); // top
			} else {
				gc.drawLine(x1, y2, x2, y2); // bottom
			}
		}
	}

	void drawClose(GC gc, Rectangle closeRect, int closeImageState) {
		if (closeRect.width == 0 || closeRect.height == 0) return;

		// draw X with length of this constant
		final int lineLength = 8;
		int x = closeRect.x + Math.max(1, (closeRect.width-lineLength)/2);
		int y = closeRect.y + Math.max(1, (closeRect.height-lineLength)/2);
		y += parent.onBottom ? -1 : 1;
		int originalLineWidth = gc.getLineWidth();
		Color originalForeground = gc.getForeground();
		switch (closeImageState & (SWT.HOT | SWT.SELECTED | SWT.BACKGROUND)) {
			case SWT.NONE: {
				drawCloseLines(gc, x, y , lineLength, false);
				break;
			}
			case SWT.HOT: {
				drawCloseLines(gc, x, y , lineLength, true);
				break;
			}
			case SWT.SELECTED: {
				drawCloseLines(gc, x, y , lineLength, true);
				break;
			}
			case SWT.BACKGROUND: {
				int[] shape = new int[] {x,y, x+10,y, x+10,y+10, x,y+10};
				drawBackground(gc, shape, false);
				break;
			}
		}
		gc.setLineWidth(originalLineWidth);
		gc.setForeground(originalForeground);
	}

	private void drawCloseLines(GC gc, int x, int y, int lineLength, boolean hot) {
		if (hot) {
			gc.setLineWidth(gc.getLineWidth() + 2);
			gc.setForeground(getFillColor());
		}
		gc.setLineCap(SWT.CAP_ROUND);
		gc.drawLine(x, y, x + lineLength, y + lineLength);
		gc.drawLine(x, y + lineLength, x + lineLength, y);
	}

	void drawChevron(GC gc, Rectangle chevronRect, int chevronImageState) {
		if (chevronRect.width == 0 || chevronRect.height == 0) return;
		// draw chevron (10x7)
		Display display = parent.getDisplay();
		Font font = getChevronFont(display);
		int fontHeight = font.getFontData()[0].getHeight();
		int indent = Math.max(2, (chevronRect.height - fontHeight - CHEVRON_BOTTOM_INDENT) /2);
		int x = chevronRect.x + 2;
		int y = chevronRect.y + indent;
		int count = parent.getChevronCount();
		String chevronString = count > 99 ? CHEVRON_ELLIPSIS : String.valueOf(count);
		switch (chevronImageState & (SWT.HOT | SWT.SELECTED)) {
			case SWT.NONE: {
				Color chevronBorder = parent.single ? parent.getSelectionForeground() : parent.getForeground();
				gc.setForeground(chevronBorder);
				gc.setFont(font);
				drawChevronContent(gc, x, y, chevronString);
				break;
			}
			case SWT.HOT: {
				gc.setForeground(display.getSystemColor(BUTTON_BORDER));
				gc.setBackground(display.getSystemColor(BUTTON_FILL));
				gc.setFont(font);
				drawRoundRectangle(gc, chevronRect);
				drawChevronContent(gc, x, y, chevronString);
				break;
			}
			case SWT.SELECTED: {
				gc.setForeground(display.getSystemColor(BUTTON_BORDER));
				gc.setBackground(display.getSystemColor(BUTTON_FILL));
				gc.setFont(font);
				drawRoundRectangle(gc, chevronRect);
				drawChevronContent(gc, x+1, y+1, chevronString);
				break;
			}
		}
	}

	private void drawRoundRectangle(GC gc, Rectangle chevronRect) {
		gc.fillRoundRectangle(chevronRect.x, chevronRect.y, chevronRect.width,     chevronRect.height,     6, 6);
		gc.drawRoundRectangle(chevronRect.x, chevronRect.y, chevronRect.width - 1, chevronRect.height - 1, 6, 6);
	}

	private void drawChevronContent(GC gc, int x, int y, String chevronString) {
		gc.drawLine(x,y,     x+2,y+2);
		gc.drawLine(x+2,y+2, x,y+4);
		gc.drawLine(x+1,y,   x+3,y+2);
		gc.drawLine(x+3,y+2, x+1,y+4);
		gc.drawLine(x+4,y,   x+6,y+2);
		gc.drawLine(x+6,y+2, x+4,y+4);
		gc.drawLine(x+5,y,   x+7,y+2);
		gc.drawLine(x+7,y+2, x+5,y+4);
		gc.drawString(chevronString, x+7, y+3, true);
	}

	/*
	 * Draw the unselected border for the receiver on the left.
	 */
	void drawLeftUnselectedBorder(GC gc, Rectangle bounds, int state) {
		int x = bounds.x;
		int y = bounds.y;
		int height = bounds.height;

		gc.setForeground(parent.getDisplay().getSystemColor(BORDER1_COLOR));
		if (parent.onBottom) {
			gc.drawLine(x, y - 1, x, y + height - 1);
		} else {
			gc.drawLine(x, y + height, x, y);
		}
	}

	void drawMaximize(GC gc, Rectangle maxRect, int maxImageState) {
		if (maxRect.width == 0 || maxRect.height == 0) return;
		// 5x4 or 7x9
		int x = maxRect.x + (maxRect.width - 10)/2;
		int y = maxRect.y + 3;

		gc.setForeground(parent.getForeground());

		switch (maxImageState & (SWT.HOT | SWT.SELECTED)) {
			case SWT.NONE: {
				if (!parent.getMaximized()) {
					gc.drawRectangle(x, y, 9, 9);
					gc.drawLine(x, y+2, x+9, y+2);
				} else {
					gc.drawRectangle(x, y+3, 5, 4);
					gc.drawRectangle(x+2, y, 5, 4);
					gc.drawLine(x+2, y+1, x+7, y+1);
					gc.drawLine(x, y+4, x+5, y+4);
				}
				break;
			}
			case SWT.HOT: {
				drawRoundRectangle(gc, maxRect);
				if (!parent.getMaximized()) {
					gc.drawRectangle(x, y, 9, 9);
					gc.drawLine(x, y+2, x+9, y+2);
				} else {
					gc.drawRectangle(x, y+3, 5, 4);
					gc.drawRectangle(x+2, y, 5, 4);
					gc.drawLine(x+2, y+1, x+7, y+1);
					gc.drawLine(x, y+4, x+5, y+4);
				}
				break;
			}
			case SWT.SELECTED: {
				drawRoundRectangle(gc, maxRect);
				if (!parent.getMaximized()) {
					gc.drawRectangle(x+1, y+1, 9, 9);
					gc.drawLine(x+1, y+3, x+10, y+3);
				} else {
					gc.drawRectangle(x+1, y+4, 5, 4);
					gc.drawRectangle(x+3, y+1, 5, 4);
					gc.drawLine(x+3, y+2, x+8, y+2);
					gc.drawLine(x+1, y+5, x+6, y+5);
				}
				break;
			}
		}
	}
	void drawMinimize(GC gc, Rectangle minRect, int minImageState) {
		if (minRect.width == 0 || minRect.height == 0) return;
		// 5x4 or 9x3
		int x = minRect.x + (minRect.width - 10)/2;
		int y = minRect.y + 3;

		gc.setForeground(parent.getForeground());

		switch (minImageState & (SWT.HOT | SWT.SELECTED)) {
			case SWT.NONE: {
				if (!parent.getMinimized()) {
					gc.drawRectangle(x, y, 9, 3);
				} else {
					gc.drawRectangle(x, y+3, 5, 4);
					gc.drawRectangle(x+2, y, 5, 4);
					gc.drawLine(x+3, y+1, x+6, y+1);
					gc.drawLine(x+1, y+4, x+4, y+4);
				}
				break;
			}
			case SWT.HOT: {
				drawRoundRectangle(gc, minRect);
				if (!parent.getMinimized()) {
					gc.drawRectangle(x, y, 9, 3);
				} else {
					gc.drawRectangle(x, y+3, 5, 4);
					gc.drawRectangle(x+2, y, 5, 4);
					gc.drawLine(x+3, y+1, x+6, y+1);
					gc.drawLine(x+1, y+4, x+4, y+4);
				}
				break;
			}
			case SWT.SELECTED: {
				drawRoundRectangle(gc, minRect);
				if (!parent.getMinimized()) {
					gc.drawRectangle(x+1, y+1, 9, 3);
				} else {
					gc.drawRectangle(x+1, y+4, 5, 4);
					gc.drawRectangle(x+3, y+1, 5, 4);
					gc.drawLine(x+4, y+2, x+7, y+2);
					gc.drawLine(x+2, y+5, x+5, y+5);
				}
				break;
			}
		}
	}

	void drawHighlight(GC gc, Rectangle bounds, int state, int rightEdge) {
	}

	/*
	 * Draw the unselected border for the receiver on the right.
	 */
	void drawRightUnselectedBorder(GC gc, Rectangle bounds, int state) {
		int x = bounds.x + bounds.width - 1;
		int y = bounds.y;
		int height = bounds.height;

		gc.setForeground(parent.getDisplay().getSystemColor(BORDER1_COLOR));
		if (parent.onBottom) {
			gc.drawLine(x, y + height - 1, x, y - 1);
		} else {
			gc.drawLine(x, y, x, y + height);
		}
	}

	void drawSelected(int itemIndex, GC gc, Rectangle bounds, int state ) {
		CTabItem item = parent.items[itemIndex];
		int x = bounds.x;
		int y = bounds.y;
		int height = bounds.height;
		int width = bounds.width;
		int borderLeft = parent.borderVisible ? 1 : 0;
		int borderRight = borderLeft;
		int borderTop = parent.onBottom ? borderLeft : 0;
		int borderBottom = parent.onBottom ? 0 : borderLeft;

		Point size = parent.getSize();

		int rightEdge = Math.min (x + width, parent.getRightItemEdge(gc));
		//	 Draw selection border across all tabs

		if ((state & SWT.BACKGROUND) != 0) {
			int highlight_header = (parent.getStyle() & SWT.FLAT) != 0 ? 1 : 3;
			int xx = borderLeft;
			int yy = parent.onBottom ? size.y - borderBottom - parent.tabHeight - highlight_header : borderTop + parent.tabHeight + 1;
			int ww = size.x - borderLeft - borderRight;
			int hh = highlight_header - 1;
			int[] shape = new int[] {xx,yy, xx+ww,yy, xx+ww,yy+hh, xx,yy+hh};
			if (parent.selectionGradientColors != null && !parent.selectionGradientVertical) {
				drawBackground(gc, shape, parent.shouldHighlight());
			} else {
				gc.setBackground(parent.shouldHighlight() ? parent.selectionBackground : parent.getBackground());
				gc.fillRectangle(xx, yy, ww, hh);
			}

			if (parent.single) {
				if (!item.showing) return;
			} else {
				// if selected tab scrolled out of view or partially out of view
				// just draw bottom line
				if (!item.showing){
					int x1 = Math.max(0, borderLeft - 1);
					int y1 = (parent.onBottom) ? y - 1 : y + height;
					int x2 = size.x - borderRight;
					gc.setForeground(parent.getDisplay().getSystemColor(BORDER1_COLOR));
					gc.drawLine(x1, y1, x2, y1);
					return;
				}

				// draw selected tab background and outline
				shape = new int[12];
				if (parent.onBottom) {
					int index = 0;
					shape[index++] = x; // first point repeated here because below we reuse shape to draw outline
					shape[index++] = y - 1;
					shape[index++] = x;
					shape[index++] = y - 1;
					shape[index++] = x;
					shape[index++] = y + height;
					if (borderLeft == 0 && itemIndex == parent.firstIndex) {
						shape[index - 2] += x;
						shape[index - 1] += y + height;
					}
					shape[index++] = rightEdge - 1;
					shape[index++] = y + height - 1;
					shape[index++] = rightEdge - 1;
					shape[index++] = y - 1;
					shape[index++] = rightEdge - 1;
					shape[index++] = y - 1;
				} else {
					int index = 0;
					shape[index++] = x; // first point repeated here because below we reuse shape to draw outline
					shape[index++] = y + height + 1;
					shape[index++] = x;
					shape[index++] = y + height + 1;
					shape[index++] = x;
					shape[index++] = y;
					if (borderLeft == 0 && itemIndex == parent.firstIndex) {
						shape[index - 2] += x;
						shape[index - 1] += y;
					}
					shape[index++] = rightEdge - 1;
					shape[index++] = y;
					shape[index++] = rightEdge - 1;
					shape[index++] = y + height + 1;
					shape[index++] = rightEdge - 1;
					shape[index++] = y + height + 1;
				}

				Rectangle clipping = gc.getClipping();
				Rectangle clipBounds = item.getBounds();
				clipBounds.height += 1;
				if (parent.onBottom) clipBounds.y -= 1;
				boolean tabInPaint = clipping.intersects(clipBounds);

				if (tabInPaint) {
					// fill in tab background
					if (parent.selectionGradientColors != null && !parent.selectionGradientVertical) {
						drawBackground(gc, shape, true);
					} else {
						Color defaultBackground = parent.shouldHighlight() ? parent.selectionBackground : parent.getBackground();
						Image image = parent.selectionBgImage;
						Color[] colors = parent.selectionGradientColors;
						int[] percents = parent.selectionGradientPercents;
						boolean vertical = parent.selectionGradientVertical;
						xx = x;
						yy = parent.onBottom ? y -1 : y + 1;
						ww = width;
						hh = height;
						drawBackground(gc, shape, xx, yy, ww, hh, defaultBackground, image, colors, percents, vertical);
					}
				}

				//Highlight colors MUST be drawn before the outline so that outline can cover it in the right spots (start of swoop)
				//otherwise the curve looks jagged
				drawHighlight(gc, bounds, state, rightEdge);

				// draw highlight marker of selected tab
				if (parent.selectionHighlightBarThickness > 0) {
					Color previousColor = gc.getBackground();
					gc.setBackground(item.getDisplay().getSystemColor(parent.shouldHighlight() ? SWT.COLOR_LIST_SELECTION : SWT.COLOR_WIDGET_DISABLED_FOREGROUND));

					int[] highlightShape = Arrays.copyOf(shape, shape.length);
					// Update Y coordinates in shape to apply highlight thickness
					int thickness = parent.selectionHighlightBarThickness;
					boolean onBottom = parent.onBottom;
					int bottomY = y + height - 1;
					int highlightY = onBottom ? bottomY - thickness : thickness + 1;
					highlightShape[1] = highlightShape[3] = highlightShape[highlightShape.length - 1] = highlightShape[highlightShape.length - 3] = highlightY;
					gc.fillPolygon(highlightShape);
					gc.setBackground(previousColor);
				}

				// draw outline
				shape[0] = Math.max(0, borderLeft - 1);
				if (borderLeft == 0 && itemIndex == parent.firstIndex) {
					shape[1] = parent.onBottom ? y + height - 1 : y;
					shape[5] = shape[3] = shape[1];
				}
				shape[shape.length - 2] = size.x - borderRight + 1;
				for (int i = 0; i < shape.length/2; i++) {
					if (shape[2*i + 1] == y + height + 1) shape[2*i + 1] -= 1;
				}
				Color borderColor = parent.getDisplay().getSystemColor(BORDER1_COLOR);
				gc.setForeground(borderColor);
				gc.drawPolyline(shape);

				if (!tabInPaint) return;
			}
		}

		if ((state & SWT.FOREGROUND) != 0) {
			// draw Image
			Rectangle trim = computeTrim(itemIndex, SWT.NONE, 0, 0, 0, 0);
			int xDraw = x - trim.x;
			if (parent.single && shouldDrawCloseIcon(item)) xDraw += item.closeRect.width;
			Image image = item.getImage();
			if (image != null && !image.isDisposed() && parent.showSelectedImage) {
				Rectangle imageBounds = image.getBounds();
				// only draw image if it won't overlap with close button
				int maxImageWidth = rightEdge - xDraw - (trim.width + trim.x);
				if (!parent.single && item.closeRect.width > 0) maxImageWidth -= item.closeRect.width + INTERNAL_SPACING;
				if (imageBounds.width < maxImageWidth) {
					int imageX = xDraw;
					int imageY = y + (height - imageBounds.height) / 2;
					imageY += parent.onBottom ? -1 : 1;
					gc.drawImage(image, imageX, imageY);
					xDraw += imageBounds.width + INTERNAL_SPACING;
				}
			}

			// draw Text
			xDraw += getLeftTextMargin(item);
			int textWidth = rightEdge - xDraw - (trim.width + trim.x);
			if (!parent.single && item.closeRect.width > 0) textWidth -= item.closeRect.width + INTERNAL_SPACING;
			if (textWidth > 0) {
				Font gcFont = gc.getFont();
				gc.setFont(item.font == null ? parent.getFont() : item.font);

				if (item.shortenedText == null || item.shortenedTextWidth != textWidth) {
					item.shortenedText = shortenText(gc, item.getText(), textWidth);
					item.shortenedTextWidth = textWidth;
				}
				Point extent = gc.textExtent(item.shortenedText, FLAGS);
				int textY = y + (height - extent.y) / 2;
				textY += parent.onBottom ? -1 : 1;

				gc.setForeground(item.selectionForeground == null ? parent.getSelectionForeground() : item.selectionForeground);
				gc.drawText(item.shortenedText, xDraw, textY, FLAGS);
				gc.setFont(gcFont);

				// draw a Focus rectangle
				if (parent.isFocusControl()) {
					Color orginalForeground = gc.getForeground();
					Color orginalBackground = gc.getBackground();
					Display display = parent.getDisplay();
					gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
					gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
					gc.drawFocus(xDraw-1, textY-1, extent.x+2, extent.y+2);
					gc.setForeground(orginalForeground);
					gc.setBackground(orginalBackground);
				}
			}
			if (shouldDrawCloseIcon(item)) drawClose(gc, item.closeRect, item.closeImageState);
		}
	}

	private int getLeftTextMargin(CTabItem item) {
		int margin = 0;
		if (shouldApplyLargeTextPadding(parent)) {
			margin += getLargeTextPadding(item);
			if (shouldDrawCloseIcon(item)) {
				 margin -= item.closeRect.width / 2;
			}
		}
		return margin;
	}

	void drawTabArea(GC gc, Rectangle bounds, int state) {
		Point size = parent.getSize();
		Color borderColor = parent.getDisplay().getSystemColor(BORDER1_COLOR);
		int tabHeight = parent.tabHeight;
		int style = parent.getStyle();

		int borderLeft = parent.borderVisible ? 1 : 0;
		int borderRight = borderLeft;
		int borderTop = parent.onBottom ? borderLeft : 0;
		int borderBottom = parent.onBottom ? 0 : borderLeft;

		int selectedIndex = parent.selectedIndex;
		int highlight_header = (style & SWT.FLAT) != 0 ? 1 : 3;
		if (tabHeight == 0) {
			if ((style & SWT.FLAT) != 0 && (style & SWT.BORDER) == 0) return;
			int x1 = borderLeft - 1;
			int x2 = size.x - borderRight;
			int y1 = parent.onBottom ? size.y - borderBottom - highlight_header - 1 : borderTop + highlight_header;
			int y2 = parent.onBottom ? size.y - borderBottom : borderTop;
			if (borderLeft > 0 && parent.onBottom) y2 -= 1;

			int[] shape = new int[] {x1, y1, x1,y2, x2,y2, x2,y1};

			// If horizontal gradient, show gradient across the whole area
			if (selectedIndex != -1 && parent.selectionGradientColors != null && parent.selectionGradientColors.length > 1 && !parent.selectionGradientVertical) {
				drawBackground(gc, shape, true);
			} else if (selectedIndex == -1 && parent.gradientColors != null && parent.gradientColors.length > 1 && !parent.gradientVertical) {
				drawBackground(gc, shape, false);
			} else {
				gc.setBackground(selectedIndex != -1 && parent.shouldHighlight() ? parent.selectionBackground : parent.getBackground());
				gc.fillPolygon(shape);
			}

			//draw 1 pixel border
			if (borderLeft > 0) {
				gc.setForeground(borderColor);
				gc.drawPolyline(shape);
			}
			return;
		}

		int x = Math.max(0, borderLeft - 1);
		int y = parent.onBottom ? size.y - borderBottom - tabHeight : borderTop;
		int width = size.x - borderLeft - borderRight + 1;
		int height = tabHeight - 1;
		int[] shape = new int[8];
		// Draw Tab Header
		if (parent.onBottom) {
			int index = 0;
			shape[index++] = x;
			shape[index++] = y - highlight_header;
			shape[index++] = x;
			shape[index++] = y + height;
			if (borderLeft == 0)
				shape[index - 1] += 1;
			shape[index++] = x + width;
			shape[index++] = y + height;
			if (borderLeft == 0)
				shape[index - 1] += 1;
			shape[index++] = x + width;
			shape[index++] = y - highlight_header;
		} else {
			int index = 0;
			shape[index++] = x;
			shape[index++] = y + height + highlight_header + 1;
			shape[index++] = x;
			shape[index++] = y;
			shape[index++] = x + width;
			shape[index++] = y;
			shape[index++] = x + width;
			shape[index++] = y + height + highlight_header + 1;
		}
		// Fill in background
		boolean single = parent.single;
		boolean bkSelected = single && selectedIndex != -1;
		drawBackground(gc, shape, bkSelected);
		// Fill in parent background for non-rectangular shape
		Region r = new Region();
		r.add(new Rectangle(x, y, width + 1, height + 1));
		r.subtract(shape);
		gc.setBackground(parent.getParent().getBackground());
		fillRegion(gc, r);
		r.dispose();

		// Draw selected tab
		if (selectedIndex == -1) {
			// if no selected tab - draw line across bottom of all tabs
			int x1 = borderLeft;
			int y1 = (parent.onBottom) ? size.y - borderBottom - tabHeight - 1 : borderTop + tabHeight;
			int x2 = size.x - borderRight;
			gc.setForeground(borderColor);
			gc.drawLine(x1, y1, x2, y1);
		}

		// Draw border line
		if (borderLeft > 0) {
			gc.setForeground(borderColor);
			gc.drawPolyline(shape);
		}
	}

	void drawUnselected(int index, GC gc, Rectangle bounds, int state) {
		CTabItem item = parent.items[index];
		int x = bounds.x;
		int y = bounds.y;
		int height = bounds.height;
		int width = bounds.width;

		// Do not draw partial items
		if (!item.showing) return;

		Rectangle clipping = gc.getClipping();
		if (!clipping.intersects(bounds)) return;

		if ((state & SWT.BACKGROUND) != 0) {
			if (index > 0 && index < parent.selectedIndex)
				drawLeftUnselectedBorder(gc, bounds, state);
			// If it is the last one then draw a line
			if (index > parent.selectedIndex)
				drawRightUnselectedBorder(gc, bounds, state);
		}

		if ((state & SWT.FOREGROUND) != 0) {
			// draw Image
			Rectangle trim = computeTrim(index, SWT.NONE, 0, 0, 0, 0);
			int xDraw = x - trim.x;
			Image image = item.getImage();
			if (image != null && !image.isDisposed() && parent.showUnselectedImage) {
				Rectangle imageBounds = image.getBounds();
				// only draw image if it won't overlap with close button
				int maxImageWidth = x + width - xDraw - (trim.width + trim.x);
				if (shouldDrawCloseIcon(item)) {
					maxImageWidth -= item.closeRect.width + INTERNAL_SPACING;
				}
				if (imageBounds.width < maxImageWidth) {
					int imageX = xDraw;
					int imageHeight = imageBounds.height;
					int imageY = y + (height - imageHeight) / 2;
					imageY += parent.onBottom ? -1 : 1;
					int imageWidth = imageBounds.width * imageHeight / imageBounds.height;
					gc.drawImage(image, imageX, imageY, imageWidth, imageHeight);
					xDraw += imageWidth + INTERNAL_SPACING;
				}
			}
			// draw Text
			xDraw += getLeftTextMargin(item);
			int textWidth = x + width - xDraw - (trim.width + trim.x);
			if (shouldDrawCloseIcon(item)) {
				textWidth -= item.closeRect.width + INTERNAL_SPACING;
			}
			if (textWidth > 0) {
				Font gcFont = gc.getFont();
				gc.setFont(item.font == null ? parent.getFont() : item.font);
				if (item.shortenedText == null || item.shortenedTextWidth != textWidth) {
					item.shortenedText = shortenText(gc, item.getText(), textWidth);
					item.shortenedTextWidth = textWidth;
				}
				Point extent = gc.textExtent(item.shortenedText, FLAGS);
				int textY = y + (height - extent.y) / 2;
				textY += parent.onBottom ? -1 : 1;
				gc.setForeground(item.foreground == null ? parent.getForeground() : item.foreground);
				gc.drawText(item.shortenedText, xDraw, textY, FLAGS);
				gc.setFont(gcFont);
			}
			// draw close
			if (shouldDrawCloseIcon(item)) drawClose(gc, item.closeRect, item.closeImageState);
		}
	}

	void fillRegion(GC gc, Region region) {
		// NOTE: region passed in to this function will be modified
		Region clipping = new Region();
		gc.getClipping(clipping);
		region.intersect(clipping);
		gc.setClipping(region);
		gc.fillRectangle(region.getBounds());
		gc.setClipping(clipping);
		clipping.dispose();
	}

	Color getFillColor() {
		if (fillColor == null) {
			fillColor = new Color(CLOSE_FILL);
		}
		return fillColor;
	}

	private Font getChevronFont(Display display) {
		if (chevronFont == null) {
			FontData fd = parent.getFont().getFontData()[0];
			int fontHeight = (int) (fd.height * CHEVRON_FONT_SIZE_FACTOR);
			fd.setHeight(fontHeight);
			chevronFont = new Font(display, fd);
		}
		return chevronFont;
	}

	void resetChevronFont() {
		if (chevronFont != null) {
			chevronFont.dispose();
			chevronFont = null;
		}
	}

	String shortenText(GC gc, String text, int width) {
		return shortenText(gc, text, width, ELLIPSIS);
	}

	String shortenText(GC gc, String text, int width, String ellipses) {
		if (gc.textExtent(text, FLAGS).x <= width) return text;
		int ellipseWidth = gc.textExtent(ellipses, FLAGS).x;
		int length = text.length();
		TextLayout layout = new TextLayout(parent.getDisplay());
		layout.setText(text);
		int end = layout.getPreviousOffset(length, SWT.MOVEMENT_CLUSTER);
		while (end > 0) {
			text = text.substring(0, end);
			int l = gc.textExtent(text, FLAGS).x;
			if (l + ellipseWidth <= width) {
				break;
			}
			end = layout.getPreviousOffset(end, SWT.MOVEMENT_CLUSTER);
		}
		layout.dispose();
		return end == 0 ? text.substring(0, 1) : text + ellipses;
	}

}
