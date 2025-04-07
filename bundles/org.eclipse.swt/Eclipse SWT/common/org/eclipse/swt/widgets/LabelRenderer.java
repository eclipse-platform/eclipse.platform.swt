/*******************************************************************************
 * Copyright (c) 2025 Syntevo GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Thomas Singer (Syntevo) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public abstract class LabelRenderer extends ControlRenderer {

	public abstract Point computeDefaultSize();

	/** a string inserted in the middle of text that has been shortened */
	private static final String ELLIPSIS = "..."; //$NON-NLS-1$ // could use
	// the ellipsis glyph on
	// some platforms "\u2026"

	protected final Label label;

	protected static final int DEFAULT_MARGIN = 3;

	// The tooltip is used for two purposes - the application can set
	// a tooltip or the tooltip can be used to display the full text when the
	// the text has been truncated due to the label being too short.
	// The appToolTip stores the tooltip set by the application.
	// Control.tooltiptext
	// contains whatever tooltip is currently being displayed.
	private String toolTipText;
	/** the alignment. Either CENTER, RIGHT, LEFT. Default is LEFT */
	private int align = SWT.LEFT;

	private Image backgroundImage;
	private Color[] gradientColors;
	private int[] gradientPercents;
	private boolean gradientVertical;
	private Color background;
	private Color foreground;

	private int leftMargin = DEFAULT_MARGIN;
	private int topMargin = DEFAULT_MARGIN;
	private int rightMargin = DEFAULT_MARGIN;
	private int bottomMargin = DEFAULT_MARGIN;

	protected LabelRenderer(Label label) {
		super(label);
		this.label = label;
	}

	public int getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}

	public int getTopMargin() {
		return topMargin;
	}

	public void setTopMargin(int topMargin) {
		this.topMargin = topMargin;
	}

	public int getRightMargin() {
		return rightMargin;
	}

	public void setRightMargin(int rightMargin) {
		this.rightMargin = rightMargin;
	}

	public int getBottomMargin() {
		return bottomMargin;
	}

	public void setBottomMargin(int bottomMargin) {
		this.bottomMargin = bottomMargin;
	}

	public void setMargins(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
		this.leftMargin = Math.max(0, leftMargin);
		this.topMargin = Math.max(0, topMargin);
		this.rightMargin = Math.max(0, rightMargin);
		this.bottomMargin = Math.max(0, bottomMargin);
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public void setToolTipText(String toolTipText) {
		this.toolTipText = toolTipText;
	}

	protected void setDisplayedToolTip(String text) {
		label.setDisplayedToolTip(text);
	}

	public void dispose() {
		gradientColors = null;
		gradientPercents = null;
		backgroundImage = null;
		toolTipText = null;
	}

	public Color getBackground() {
		return background;
	}

	public void setBackground(Color color) {
		// Are these settings the same as before?
		if (backgroundImage == null && gradientColors == null
			&& gradientPercents == null) {
			if (color == null) {
				if (background == null) {
					return;
				}
			} else {
				if (color.equals(background)) {
					return;
				}
			}
		}
		background = color;
		backgroundImage = null;
		gradientColors = null;
		gradientPercents = null;
	}

	public Color getForeground() {
		return foreground;
	}

	public void setForeground(Color foreground) {
		this.foreground = foreground;
	}

	public Color[] getGradientColors() {
		return gradientColors;
	}

	public int[] getGradientPercents() {
		return gradientPercents;
	}

	public boolean isGradientVertical() {
		return gradientVertical;
	}

	public void setBackground(Color[] colors, int[] percents, boolean vertical) {
		if (colors != null) {
			if (percents == null || percents.length != colors.length - 1) {
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (label.getDisplay().getDepth() < 15) {
				// Don't use gradients on low color displays
				colors = new Color[]{colors[colors.length - 1]};
				percents = new int[]{};
			}
			for (int i = 0; i < percents.length; i++) {
				if (percents[i] < 0 || percents[i] > 100) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
				if (i > 0 && percents[i] < percents[i - 1]) {
					SWT.error(SWT.ERROR_INVALID_ARGUMENT);
				}
			}
		}

		// Are these settings the same as before?
		final Color background = getBackground();
		if (backgroundImage == null) {
			if ((gradientColors != null) && (colors != null)
				&& (gradientColors.length == colors.length)) {
				boolean same = false;
				for (int i = 0; i < gradientColors.length; i++) {
					same = (gradientColors[i] == colors[i])
						   || ((gradientColors[i] == null)
							   && (colors[i] == background))
						   || ((gradientColors[i] == background)
							   && (colors[i] == null));
					if (!same) {
						break;
					}
				}
				if (same) {
					for (int i = 0; i < gradientPercents.length; i++) {
						same = gradientPercents[i] == percents[i];
						if (!same) {
							break;
						}
					}
				}
				if (same && this.gradientVertical == vertical)
					return;
			}
		} else {
			backgroundImage = null;
		}
		// Store the new settings
		if (colors == null) {
			gradientColors = null;
			gradientPercents = null;
			gradientVertical = false;
		} else {
			gradientColors = new Color[colors.length];
			for (int i = 0; i < colors.length; ++i)
				gradientColors[i] = (colors[i] != null)
						? colors[i]
						: background;
			gradientPercents = new int[percents.length];
			System.arraycopy(percents, 0, gradientPercents, 0, percents.length);
			gradientVertical = vertical;
		}
	}

	public Image getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(Image image) {
		if (image != null) {
			gradientColors = null;
			gradientPercents = null;
		}
		backgroundImage = image;
	}

	public int getAlign() {
		return align;
	}

	public void setAlign(int align) {
		this.align = align;
	}

	/**
	 * Shorten the given text <code>t</code> so that its length doesn't exceed
	 * the given width. The default implementation replaces characters in the
	 * center of the original string with an ellipsis ("..."). Override if you
	 * need a different strategy.
	 *
	 * @param gc    the gc to use for text measurement
	 * @param t     the text to shorten
	 * @param width the width to shorten the text to, in points
	 * @return the shortened text
	 */
	protected String shortenText(GC gc, String t, int width, int drawFlags) {
		if (t == null) {
			return null;
		}
		int w = gc.textExtent(ELLIPSIS, drawFlags).x;
		if (width <= w) {
			return t;
		}
		int l = t.length();
		int max = l / 2;
		int min = 0;
		int mid = (max + min) / 2 - 1;
		if (mid <= 0) {
			return t;
		}
		TextLayout layout = new TextLayout(label.getDisplay());
		layout.setText(t);
		mid = validateOffset(layout, mid);
		while (min < mid && mid < max) {
			String s1 = t.substring(0, mid);
			String s2 = t.substring(validateOffset(layout, l - mid), l);
			int l1 = gc.textExtent(s1, drawFlags).x;
			int l2 = gc.textExtent(s2, drawFlags).x;
			if (l1 + w + l2 > width) {
				max = mid;
				mid = validateOffset(layout, (max + min) / 2);
			} else if (l1 + w + l2 < width) {
				min = mid;
				mid = validateOffset(layout, (max + min) / 2);
			} else {
				min = max;
			}
		}
		String result = mid == 0
				? t
				: t.substring(0, mid) + ELLIPSIS
				  + t.substring(validateOffset(layout, l - mid), l);
		layout.dispose();
		return result;
	}

	private int validateOffset(TextLayout layout, int offset) {
		int nextOffset = layout.getNextOffset(offset, SWT.MOVEMENT_CLUSTER);
		if (nextOffset != offset) {
			return layout.getPreviousOffset(nextOffset, SWT.MOVEMENT_CLUSTER);
		}
		return offset;
	}
}
