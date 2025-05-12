package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;

public class DefaultTabFolderRenderer extends TabFolderRenderer {
	int[] curve;
	int[] topCurveHighlightStart;
	int[] topCurveHighlightEnd;
	int curveWidth = 0;
	int lastTabHeight = -1;

	Color fillColor;
	/* Selected item appearance */
	Color selectionHighlightGradientBegin = null; // null == no highlight
	// Although we are given new colours all the time to show different states
	// (active, etc),
	// some of which may have a highlight and some not, we'd like to retain the
	// highlight colours
	// as a cache so that we can reuse them if we're again told to show the
	// highlight.
	// We are relying on the fact that only one tab state usually gets a highlight,
	// so only
	// a single cache is required. If that happens to not be true, cache simply
	// becomes less effective,
	// but we don't leak colours.
	Color[] selectionHighlightGradientColorsCache = null; // null is a legal value, check on access
	/* Colors for anti-aliasing */
	Color selectedOuterColor = null;
	Color selectedInnerColor = null;
	Color tabAreaColor = null;
	/*
	 * Border color that was used in computing the cached anti-alias Colors. We have
	 * to recompute the colors if the border color changes
	 */
	Color lastBorderColor = null;

	private Font chevronFont = null;

	protected DefaultTabFolderRenderer(TabFolder parent) {
		super(parent);
	}

	void antialias(int[] shape, Color innerColor, Color outerColor, GC gc) {
		// Don't perform anti-aliasing on Mac because the platform
		// already does it. The simple style also does not require anti-aliasing.
		if (parent.simple) return;
		String platform = SWT.getPlatform();
		if ("cocoa".equals(platform)) { //$NON-NLS-1$
			return;
		}
		// Don't perform anti-aliasing on low resolution displays
		if (parent.getDisplay().getDepth() < 15) return;
		if (outerColor != null) {
			int index = 0;
			boolean left = true;
			int oldY = parent.onBottom ? 0 : parent.getSize().y;
			int[] outer = new int[shape.length];
			for (int i = 0; i < shape.length / 2; i++) {
				if (left && (index + 3 < shape.length)) {
					left = parent.onBottom ? oldY <= shape[index + 3] : oldY >= shape[index + 3];
					oldY = shape[index + 1];
				}
				outer[index] = shape[index++] + (left ? -1 : +1);
				outer[index] = shape[index++];
			}
			gc.setForeground(outerColor);
			gc.drawPolyline(outer);
		}
		if (innerColor != null) {
			int[] inner = new int[shape.length];
			int index = 0;
			boolean left = true;
			int oldY = parent.onBottom ? 0 : parent.getSize().y;
			for (int i = 0; i < shape.length / 2; i++) {
				if (left && (index + 3 < shape.length)) {
					left = parent.onBottom ? oldY <= shape[index + 3] : oldY >= shape[index + 3];
					oldY = shape[index + 1];
				}
				inner[index] = shape[index++] + (left ? +1 : -1);
				inner[index] = shape[index++];
			}
			gc.setForeground(innerColor);
			gc.drawPolyline(inner);
		}
	}

	@Override
	protected Point computeSize(int part, int state, GC gc, int wHint, int hHint) {
		int width = 0;
		int height = 0;
		switch (part) {
		case PART_HEADER:
			if (parent.fixedTabHeight != SWT.DEFAULT) {
				height = parent.fixedTabHeight == 0 ? 0 : parent.fixedTabHeight + 1; // +1 for line drawn across top of
																						// tab
			} else {
				TabItem[] items = parent.items;
				if (items.length == 0) {
					height = gc.textExtent("Default", FLAGS).y + ITEM_TOP_MARGIN + ITEM_BOTTOM_MARGIN; //$NON-NLS-1$
				} else {
					for (int i = 0; i < items.length; i++) {
						height = Math.max(height, computeSize(i, SWT.NONE, gc, wHint, hHint).y);
					}
				}
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
				updateCurves();
				TabItem item = parent.items[part];
				if (item.isDisposed()) {
					return new Point(0, 0);
				}
				Image image = item.getImage();
				if (image != null && !image.isDisposed()) {
					Rectangle bounds = image.getBounds();
					if (((state & SWT.SELECTED) != 0 && parent.showSelectedImage)
							|| ((state & SWT.SELECTED) == 0 && parent.showUnselectedImage)) {
						width += bounds.width;
					}
					height = bounds.height;
				}
				String text;
				if ((state & MINIMUM_SIZE) != 0) {
					int minChars = parent.minChars;
					text = minChars == 0 ? null : item.getText();
					if (text != null && text.length() > minChars) {
						if (useEllipses()) {
							int end = minChars < ELLIPSIS.length() + 1 ? minChars : minChars - ELLIPSIS.length();
							text = text.substring(0, end);
							if (minChars > ELLIPSIS.length() + 1) {
								text += ELLIPSIS;
							}
						} else {
							int end = minChars;
							text = text.substring(0, end);
						}
					}
				} else {
					text = item.getText();
				}
				if (text != null) {
					if (width > 0) {
						width += INTERNAL_SPACING;
					}
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
					if (width > 0) {
						width += INTERNAL_SPACING;
					}
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

	private boolean shouldDrawCloseIcon(TabItem item) {
		TabFolder folder = item.getParent();
		boolean showClose = folder.showClose || item.showClose;
		boolean isSelectedOrShowCloseForUnselected = (item.state & SWT.SELECTED) != 0 || folder.showUnselectedClose;
		return showClose && isSelectedOrShowCloseForUnselected;
	}

	/**
	 * Returns padding for the text of a tab item when showing images is disabled
	 * for the tab folder.
	 */
	private int getLargeTextPadding(TabItem item) {
		TabFolder parent = item.getParent();
		String text = item.getText();

		if (text != null && parent.getMinimumCharacters() != 0) {
			return TABS_WITHOUT_ICONS_PADDING;
		}
		return 0;
	}

	private boolean shouldApplyLargeTextPadding(TabFolder tabFolder) {
		return !tabFolder.showSelectedImage && !tabFolder.showUnselectedImage;
	}

	@Override
	protected Rectangle computeTrim(int part, int state, int x, int y, int width, int height) {
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
			width = width + borderLeft + borderRight + 2 * marginWidth + 2 * highlight_margin;
			if (parent.minimized) {
				y = parent.onBottom ? y - borderTop : y - highlight_header - tabHeight - borderTop;
				height = borderTop + borderBottom + tabHeight + highlight_header;
			} else {
				y = parent.onBottom ? y - marginHeight - highlight_margin - borderTop
						: y - marginHeight - highlight_header - tabHeight - borderTop;
				height = height + borderTop + borderBottom + 2 * marginHeight + tabHeight + highlight_header
						+ highlight_margin;
			}
			break;
		case PART_HEADER:
			// no trim
			break;
		case PART_MAX_BUTTON:
		case PART_MIN_BUTTON:
		case PART_CLOSE_BUTTON:
		case PART_CHEVRON_BUTTON:
			x -= BUTTON_TRIM;
			y -= BUTTON_TRIM;
			width += BUTTON_TRIM * 2;
			height += BUTTON_TRIM * 2;
			break;
		case PART_BORDER:
			x = x - borderLeft;
			width = width + borderLeft + borderRight;
			if (!parent.simple) {
				width += 2; // TOP_RIGHT_CORNER needs more space
			}
			y = y - borderTop;
			height = height + borderTop + borderBottom;
			break;
		default:
			if (0 <= part && part < parent.getItemCount()) {
				updateCurves();
				x = x - ITEM_LEFT_MARGIN;
				width = width + ITEM_LEFT_MARGIN + ITEM_RIGHT_MARGIN;
				if (!parent.simple && !parent.single && (state & SWT.SELECTED) != 0) {
					width += curveWidth - curveIndent;
				}
				y = y - ITEM_TOP_MARGIN;
				height = height + ITEM_TOP_MARGIN + ITEM_BOTTOM_MARGIN;
			}
			break;
		}
		return new Rectangle(x, y, width, height);
	}

	@Override
	protected
	void createAntialiasColors() {
		disposeAntialiasColors();
		lastBorderColor = parent.getDisplay().getSystemColor(BORDER1_COLOR);
		RGB lineRGB = lastBorderColor.getRGB();
		/* compute the selected color */
		RGB innerRGB = parent.selectionBackground.getRGB();
		if (parent.selectionBgImage != null
				|| (parent.selectionGradientColors != null && parent.selectionGradientColors.length > 1)) {
			innerRGB = null;
		}
		RGB outerRGB = parent.getBackground().getRGB();
		if (parent.gradientColors != null && parent.gradientColors.length > 1) {
			outerRGB = null;
		}
		if (outerRGB != null) {
			RGB from = lineRGB;
			RGB to = outerRGB;
			int red = from.red + 2 * (to.red - from.red) / 3;
			int green = from.green + 2 * (to.green - from.green) / 3;
			int blue = from.blue + 2 * (to.blue - from.blue) / 3;
			selectedOuterColor = new Color(red, green, blue);
		}
		if (innerRGB != null) {
			RGB from = lineRGB;
			RGB to = innerRGB;
			int red = from.red + 2 * (to.red - from.red) / 3;
			int green = from.green + 2 * (to.green - from.green) / 3;
			int blue = from.blue + 2 * (to.blue - from.blue) / 3;
			selectedInnerColor = new Color(red, green, blue);
		}
		/* compute the tabArea color */
		outerRGB = parent.getParent().getBackground().getRGB();
		RGB from = lineRGB;
		RGB to = outerRGB;
		int red = from.red + 2 * (to.red - from.red) / 3;
		int green = from.green + 2 * (to.green - from.green) / 3;
		int blue = from.blue + 2 * (to.blue - from.blue) / 3;
		tabAreaColor = new Color(red, green, blue);
	}

	/*
	 * Allocate colors for the highlight line. Colours will be a gradual blend
	 * ranging from to. Blend length will be tab height. Recompute this if tab
	 * height changes. Could remain null if there'd be no gradient (start=end or low
	 * colour display)
	 */
	void createSelectionHighlightGradientColors(Color start) {
		disposeSelectionHighlightGradientColors(); // dispose if existing
		// shouldn't happen but just to be safe
		if (start == null) return;

		// alloc colours for entire height to ensure it matches wherever we stop drawing
		int fadeGradientSize = parent.tabHeight;

		RGB from = start.getRGB();
		RGB to = parent.selectionBackground.getRGB();

		selectionHighlightGradientColorsCache = new Color[fadeGradientSize];
		int denom = fadeGradientSize - 1;

		for (int i = 0; i < fadeGradientSize; i++) {
			int propFrom = denom - i;
			int propTo = i;
			int red = (to.red * propTo + from.red * propFrom) / denom;
			int green = (to.green * propTo + from.green * propFrom) / denom;
			int blue = (to.blue * propTo + from.blue * propFrom) / denom;
			selectionHighlightGradientColorsCache[i] = new Color(red, green, blue);
		}
	}

	@Override
	protected void dispose() {
		disposeAntialiasColors();
		disposeSelectionHighlightGradientColors();

		fillColor = null;

		if (chevronFont != null) {
			chevronFont.dispose();
			chevronFont = null;
		}
	}

	void disposeAntialiasColors() {
		tabAreaColor = selectedInnerColor = selectedOuterColor = null;
	}

	@Override
	protected void disposeSelectionHighlightGradientColors() {
		selectionHighlightGradientColorsCache = null;
	}

	@Override
	protected void draw(int part, int state, Rectangle bounds, GC gc) {
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
				if ((state & SWT.SELECTED) != 0) {
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
		Color defaultBackground = selected && parent.shouldHighlight() ? parent.selectionBackground
				: parent.getBackground();
		Image image = selected ? parent.selectionBgImage : null;
		Color[] colors = selected & parent.shouldHighlight() ? parent.selectionGradientColors : parent.gradientColors;
		int[] percents = selected ? parent.selectionGradientPercents : parent.gradientPercents;
		boolean vertical = selected ? parent.selectionGradientVertical : parent.gradientVertical;

		drawBackground(gc, null, bounds.x, bounds.y, bounds.width, bounds.height, defaultBackground, image, colors,
				percents, vertical);
	}

	void drawBackground(GC gc, int[] shape, boolean selected) {
		Color defaultBackground = selected && parent.shouldHighlight() ? parent.selectionBackground
				: parent.getBackground();
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
			x += 1;
			width -= 2;
		}
		int y = parent.onBottom ? size.y - borderBottom - height : borderTop;
		drawBackground(gc, shape, x, y, width, height, defaultBackground, image, colors, percents, vertical);
	}

	void drawBackground(GC gc, int[] shape, int x, int y, int width, int height, Color defaultBackground, Image image,
			Color[] colors, int[] percents, boolean vertical) {
		Region clipping = null;
		Region region = null;
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
			Rectangle imageRect = image.getBounds();
			gc.drawImage(image, imageRect.x, imageRect.y, imageRect.width, imageRect.height, x, y, width, height);
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
						Color lastColor = colors[colors.length - 1];
						if (lastColor == null) {
							lastColor = defaultBackground;
						}
						for (int i = percents.length - 1; i >= 0; i--) {
							gc.setForeground(lastColor);
							lastColor = colors[i];
							if (lastColor == null) {
								lastColor = defaultBackground;
							}
							gc.setBackground(lastColor);
							int percentage = i > 0 ? percents[i] - percents[i - 1] : percents[i];
							int gradientHeight = percentage * height / 100;
							gc.fillGradientRectangle(x, y + pos, width, gradientHeight, true);
							pos += gradientHeight;
						}
					} else {
						Color lastColor = colors[0];
						if (lastColor == null) {
							lastColor = defaultBackground;
						}
						int pos = 0;
						for (int i = 0; i < percents.length; i++) {
							gc.setForeground(lastColor);
							lastColor = colors[i + 1];
							if (lastColor == null) {
								lastColor = defaultBackground;
							}
							gc.setBackground(lastColor);
							int percentage = i > 0 ? percents[i] - percents[i - 1] : percents[i];
							int gradientHeight = percentage * height / 100;
							gc.fillGradientRectangle(x, y + pos, width, gradientHeight, true);
							pos += gradientHeight;
						}
						if (pos < height) {
							gc.setBackground(defaultBackground);
							gc.fillRectangle(x, pos, width, height - pos + 1);
						}
					}
				} else { // horizontal gradient
					y = 0;
					height = parent.getSize().y;
					Color lastColor = colors[0];
					if (lastColor == null) {
						lastColor = defaultBackground;
					}
					int pos = 0;
					for (int i = 0; i < percents.length; ++i) {
						gc.setForeground(lastColor);
						lastColor = colors[i + 1];
						if (lastColor == null) {
							lastColor = defaultBackground;
						}
						gc.setBackground(lastColor);
						int gradientWidth = (percents[i] * width / 100) - pos;
						gc.fillGradientRectangle(x + pos, y, gradientWidth, height, false);
						pos += gradientWidth;
					}
					if (pos < width) {
						gc.setBackground(defaultBackground);
						gc.fillRectangle(x + pos, y, width - pos, height);
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
		if (!parent.minimized) {
			int width = size.x - borderLeft - borderRight - 2 * highlight_margin;
			int height = size.y - borderTop - borderBottom - tabHeight - highlight_header - highlight_margin;
			// Draw highlight margin
			if (highlight_margin > 0) {
				int[] shape = null;
				if (parent.onBottom) {
					int x1 = borderLeft;
					int y1 = borderTop;
					int x2 = size.x - borderRight;
					int y2 = size.y - borderBottom - tabHeight - highlight_header;
					shape = new int[] { x1, y1, x2, y1, x2, y2, x2 - highlight_margin, y2, x2 - highlight_margin,
							y1 + highlight_margin, x1 + highlight_margin, y1 + highlight_margin, x1 + highlight_margin,
							y2, x1, y2 };
				} else {
					int x1 = borderLeft;
					int y1 = borderTop + tabHeight + highlight_header;
					int x2 = size.x - borderRight;
					int y2 = size.y - borderBottom;
					shape = new int[] { x1, y1, x1 + highlight_margin, y1, x1 + highlight_margin, y2 - highlight_margin,
							x2 - highlight_margin, y2 - highlight_margin, x2 - highlight_margin, y1, x2, y1, x2, y2, x1,
							y2 };
				}
				// If horizontal gradient, show gradient across the whole area
				if (selectedIndex != -1 && parent.selectionGradientColors != null
						&& parent.selectionGradientColors.length > 1 && !parent.selectionGradientVertical) {
					drawBackground(gc, shape, true);
				} else if (selectedIndex == -1 && parent.gradientColors != null && parent.gradientColors.length > 1
						&& !parent.gradientVertical) {
					drawBackground(gc, shape, false);
				} else {
					gc.setBackground(selectedIndex != -1 && parent.shouldHighlight() ? parent.selectionBackground
							: parent.getBackground());
					gc.fillPolygon(shape);
				}
			}
			// Draw client area
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

		// draw 1 pixel border around outside
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
		int x = closeRect.x + Math.max(1, (closeRect.width - lineLength) / 2);
		int y = closeRect.y + Math.max(1, (closeRect.height - lineLength) / 2);
		y += parent.onBottom ? -1 : 1;
		int originalLineWidth = gc.getLineWidth();
		Color originalForeground = gc.getForeground();
		switch (closeImageState & (SWT.HOT | SWT.SELECTED | SWT.BACKGROUND)) {
		case SWT.NONE: {
			drawCloseLines(gc, x, y, lineLength, false);
			break;
		}
		case SWT.HOT: {
			drawCloseLines(gc, x, y, lineLength, true);
			break;
		}
		case SWT.SELECTED: {
			drawCloseLines(gc, x, y, lineLength, true);
			break;
		}
		case SWT.BACKGROUND: {
			int[] shape = new int[] { x, y, x + 10, y, x + 10, y + 10, x, y + 10 };
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
		int indent = Math.max(2, (chevronRect.height - fontHeight - CHEVRON_BOTTOM_INDENT) / 2);
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
			drawChevronContent(gc, x + 1, y + 1, chevronString);
			break;
		}
		}
	}

	private void drawRoundRectangle(GC gc, Rectangle chevronRect) {
		gc.fillRoundRectangle(chevronRect.x, chevronRect.y, chevronRect.width, chevronRect.height, 6, 6);
		gc.drawRoundRectangle(chevronRect.x, chevronRect.y, chevronRect.width - 1, chevronRect.height - 1, 6, 6);
	}

	private void drawChevronContent(GC gc, int x, int y, String chevronString) {
		gc.drawLine(x, y, x + 2, y + 2);
		gc.drawLine(x + 2, y + 2, x, y + 4);
		gc.drawLine(x + 1, y, x + 3, y + 2);
		gc.drawLine(x + 3, y + 2, x + 1, y + 4);
		gc.drawLine(x + 4, y, x + 6, y + 2);
		gc.drawLine(x + 6, y + 2, x + 4, y + 4);
		gc.drawLine(x + 5, y, x + 7, y + 2);
		gc.drawLine(x + 7, y + 2, x + 5, y + 4);
		gc.drawString(chevronString, x + 7, y + 3, true);
	}

	/*
	 * Draw a highlight effect along the left, top, and right edges of the tab. Only
	 * for curved tabs, on top. Do not draw if insufficient colors.
	 */
	void drawHighlight(GC gc, Rectangle bounds, int state, int rightEdge) {
		// only draw for curvy tabs and only draw for top tabs
		if (parent.simple || parent.onBottom) return;
		if (selectionHighlightGradientBegin == null) return;

		Color[] gradients = selectionHighlightGradientColorsCache;
		if (gradients == null) return;
		int gradientsSize = gradients.length;
		if (gradientsSize == 0) return;

		int x = bounds.x;
		int y = bounds.y;

		gc.setForeground(gradients[0]);

		// draw top horizontal line
		gc.drawLine(TOP_LEFT_CORNER_HILITE[0] + x + 1, // rely on fact that first pair is top/right of curve
				1 + y, rightEdge - curveIndent, 1 + y);

		int[] leftHighlightCurve = TOP_LEFT_CORNER_HILITE;

		int d = parent.tabHeight - topCurveHighlightEnd.length / 2;

		int lastX = 0;
		int lastY = 0;
		int lastColorIndex = 0;

		// draw upper left curve highlight
		for (int i = 0; i < leftHighlightCurve.length / 2; i++) {
			int rawX = leftHighlightCurve[i * 2];
			int rawY = leftHighlightCurve[i * 2 + 1];
			lastX = rawX + x;
			lastY = rawY + y;
			lastColorIndex = rawY - 1;
			gc.setForeground(gradients[lastColorIndex]);
			gc.drawPoint(lastX, lastY);
		}
		// draw left vertical line highlight
		for (int i = lastColorIndex; i < gradientsSize; i++) {
			gc.setForeground(gradients[i]);
			gc.drawPoint(lastX, 1 + lastY++);
		}

		int rightEdgeOffset = rightEdge - curveIndent;

		// draw right swoop highlight up to diagonal portion
		for (int i = 0; i < topCurveHighlightStart.length / 2; i++) {
			int rawX = topCurveHighlightStart[i * 2];
			int rawY = topCurveHighlightStart[i * 2 + 1];
			lastX = rawX + rightEdgeOffset;
			lastY = rawY + y;
			lastColorIndex = rawY - 1;
			// can happen if tabs are unusually short and cut off the curve
			if (lastColorIndex >= gradientsSize) {
				break;
			}
			gc.setForeground(gradients[lastColorIndex]);
			gc.drawPoint(lastX, lastY);
		}
		// draw right diagonal line highlight
		for (int i = lastColorIndex; i < lastColorIndex + d; i++) {
			// can happen if tabs are unusually short and cut off the curve
			if (i >= gradientsSize) {
				break;
			}
			gc.setForeground(gradients[i]);
			gc.drawPoint(1 + lastX++, 1 + lastY++);
		}

		// draw right swoop highlight from diagonal portion to end
		for (int i = 0; i < topCurveHighlightEnd.length / 2; i++) {
			int rawX = topCurveHighlightEnd[i * 2]; // d is already encoded in this value
			int rawY = topCurveHighlightEnd[i * 2 + 1]; // d already encoded
			lastX = rawX + rightEdgeOffset;
			lastY = rawY + y;
			lastColorIndex = rawY - 1;
			// can happen if tabs are unusually short and cut off the curve
			if (lastColorIndex >= gradientsSize) {
				break;
			}
			gc.setForeground(gradients[lastColorIndex]);
			gc.drawPoint(lastX, lastY);
		}
	}

	/*
	 * Draw the unselected border for the receiver on the left.
	 */
	void drawLeftUnselectedBorder(GC gc, Rectangle bounds, int state) {
		int x = bounds.x;
		int y = bounds.y;
		int height = bounds.height;

		int[] shape = null;
		if (parent.onBottom) {
			int[] left = parent.simple ? SIMPLE_UNSELECTED_INNER_CORNER : BOTTOM_LEFT_CORNER;

			shape = new int[left.length + 2];
			int index = 0;
			shape[index++] = x;
			shape[index++] = y - 1;
			for (int i = 0; i < left.length / 2; i++) {
				shape[index++] = x + left[2 * i];
				shape[index++] = y + height + left[2 * i + 1] - 1;
			}
		} else {
			int[] left = parent.simple ? SIMPLE_UNSELECTED_INNER_CORNER : TOP_LEFT_CORNER;

			shape = new int[left.length + 2];
			int index = 0;
			shape[index++] = x;
			shape[index++] = y + height;
			for (int i = 0; i < left.length / 2; i++) {
				shape[index++] = x + left[2 * i];
				shape[index++] = y + left[2 * i + 1];
			}
		}

		drawBorder(gc, shape);
	}

	void drawMaximize(GC gc, Rectangle maxRect, int maxImageState) {
		if (maxRect.width == 0 || maxRect.height == 0) return;
		// 5x4 or 7x9
		int x = maxRect.x + (maxRect.width - 10) / 2;
		int y = maxRect.y + 3;

		gc.setForeground(parent.getForeground());

		switch (maxImageState & (SWT.HOT | SWT.SELECTED)) {
		case SWT.NONE: {
			if (!parent.getMaximized()) {
				gc.drawRectangle(x, y, 9, 9);
				gc.drawLine(x, y + 2, x + 9, y + 2);
			} else {
				gc.drawRectangle(x, y + 3, 5, 4);
				gc.drawRectangle(x + 2, y, 5, 4);
				gc.drawLine(x + 2, y + 1, x + 7, y + 1);
				gc.drawLine(x, y + 4, x + 5, y + 4);
			}
			break;
		}
		case SWT.HOT: {
			drawRoundRectangle(gc, maxRect);
			if (!parent.getMaximized()) {
				gc.drawRectangle(x, y, 9, 9);
				gc.drawLine(x, y + 2, x + 9, y + 2);
			} else {
				gc.drawRectangle(x, y + 3, 5, 4);
				gc.drawRectangle(x + 2, y, 5, 4);
				gc.drawLine(x + 2, y + 1, x + 7, y + 1);
				gc.drawLine(x, y + 4, x + 5, y + 4);
			}
			break;
		}
		case SWT.SELECTED: {
			drawRoundRectangle(gc, maxRect);
			if (!parent.getMaximized()) {
				gc.drawRectangle(x + 1, y + 1, 9, 9);
				gc.drawLine(x + 1, y + 3, x + 10, y + 3);
			} else {
				gc.drawRectangle(x + 1, y + 4, 5, 4);
				gc.drawRectangle(x + 3, y + 1, 5, 4);
				gc.drawLine(x + 3, y + 2, x + 8, y + 2);
				gc.drawLine(x + 1, y + 5, x + 6, y + 5);
			}
			break;
		}
		}
	}

	void drawMinimize(GC gc, Rectangle minRect, int minImageState) {
		if (minRect.width == 0 || minRect.height == 0) return;
		// 5x4 or 9x3
		int x = minRect.x + (minRect.width - 10) / 2;
		int y = minRect.y + 3;

		gc.setForeground(parent.getForeground());

		switch (minImageState & (SWT.HOT | SWT.SELECTED)) {
		case SWT.NONE: {
			if (!parent.getMinimized()) {
				gc.drawRectangle(x, y, 9, 3);
			} else {
				gc.drawRectangle(x, y + 3, 5, 4);
				gc.drawRectangle(x + 2, y, 5, 4);
				gc.drawLine(x + 3, y + 1, x + 6, y + 1);
				gc.drawLine(x + 1, y + 4, x + 4, y + 4);
			}
			break;
		}
		case SWT.HOT: {
			drawRoundRectangle(gc, minRect);
			if (!parent.getMinimized()) {
				gc.drawRectangle(x, y, 9, 3);
			} else {
				gc.drawRectangle(x, y + 3, 5, 4);
				gc.drawRectangle(x + 2, y, 5, 4);
				gc.drawLine(x + 3, y + 1, x + 6, y + 1);
				gc.drawLine(x + 1, y + 4, x + 4, y + 4);
			}
			break;
		}
		case SWT.SELECTED: {
			drawRoundRectangle(gc, minRect);
			if (!parent.getMinimized()) {
				gc.drawRectangle(x + 1, y + 1, 9, 3);
			} else {
				gc.drawRectangle(x + 1, y + 4, 5, 4);
				gc.drawRectangle(x + 3, y + 1, 5, 4);
				gc.drawLine(x + 4, y + 2, x + 7, y + 2);
				gc.drawLine(x + 2, y + 5, x + 5, y + 5);
			}
			break;
		}
		}
	}

	/*
	 * Draw the unselected border for the receiver on the right.
	 */
	void drawRightUnselectedBorder(GC gc, Rectangle bounds, int state) {
		int x = bounds.x;
		int y = bounds.y;
		int width = bounds.width;
		int height = bounds.height;

		int[] shape = null;
		int startX = x + width - 1;

		if (parent.onBottom) {
			int[] right = parent.simple ? SIMPLE_UNSELECTED_INNER_CORNER : BOTTOM_RIGHT_CORNER;

			shape = new int[right.length + 2];
			int index = 0;

			for (int i = 0; i < right.length / 2; i++) {
				shape[index++] = startX + right[2 * i];
				shape[index++] = y + height + right[2 * i + 1] - 1;
			}
			shape[index++] = startX;
			shape[index++] = y - 1;
		} else {
			int[] right = parent.simple ? SIMPLE_UNSELECTED_INNER_CORNER : TOP_RIGHT_CORNER;

			shape = new int[right.length + 2];
			int index = 0;

			for (int i = 0; i < right.length / 2; i++) {
				shape[index++] = startX + right[2 * i];
				shape[index++] = y + right[2 * i + 1];
			}

			shape[index++] = startX;
			shape[index++] = y + height;
		}

		drawBorder(gc, shape);
	}

	void drawSelected(int itemIndex, GC gc, Rectangle bounds, int state) {
		TabItem item = parent.items[itemIndex];
		int x = bounds.x;
		int y = bounds.y;
		int height = bounds.height;
		int width = bounds.width;
		if (!parent.simple && !parent.single) {
			width -= (curveWidth - curveIndent);
		}
		int borderLeft = parent.borderVisible ? 1 : 0;
		int borderRight = borderLeft;
		int borderTop = parent.onBottom ? borderLeft : 0;
		int borderBottom = parent.onBottom ? 0 : borderLeft;

		Point size = parent.getSize();

		int rightEdge = Math.min(x + width, parent.getRightItemEdge(gc));
		// Draw selection border across all tabs

		if ((state & SWT.BACKGROUND) != 0) {
			int highlight_header = (parent.getStyle() & SWT.FLAT) != 0 ? 1 : 3;
			int xx = borderLeft;
			int yy = parent.onBottom ? size.y - borderBottom - parent.tabHeight - highlight_header
					: borderTop + parent.tabHeight + 1;
			int ww = size.x - borderLeft - borderRight;
			int hh = highlight_header - 1;
			int[] shape = new int[] { xx, yy, xx + ww, yy, xx + ww, yy + hh, xx, yy + hh };
			if (parent.selectionGradientColors != null && !parent.selectionGradientVertical) {
				drawBackground(gc, shape, parent.shouldHighlight());
			} else {
				gc.setBackground(parent.shouldHighlight() ? parent.selectionBackground : parent.getBackground());
				gc.fillRectangle(xx, yy, ww, hh);
			}

			if (parent.single) {
				if (!item.showing) {
					return;
				}
			} else {
				// if selected tab scrolled out of view or partially out of view
				// just draw bottom line
				if (!item.showing) {
					int x1 = Math.max(0, borderLeft - 1);
					int y1 = (parent.onBottom) ? y - 1 : y + height;
					int x2 = size.x - borderRight;
					gc.setForeground(parent.getDisplay().getSystemColor(BORDER1_COLOR));
					gc.drawLine(x1, y1, x2, y1);
					return;
				}

				// draw selected tab background and outline
				shape = null;
				if (parent.onBottom) {
					int[] left = parent.simple ? SIMPLE_BOTTOM_LEFT_CORNER : BOTTOM_LEFT_CORNER;
					int[] right = parent.simple ? SIMPLE_BOTTOM_RIGHT_CORNER : curve;
					if (borderLeft == 0 && itemIndex == parent.firstIndex) {
						left = new int[] { x, y + height };
					}
					shape = new int[left.length + right.length + 8];
					int index = 0;
					shape[index++] = x; // first point repeated here because below we reuse shape to draw outline
					shape[index++] = y - 1;
					shape[index++] = x;
					shape[index++] = y - 1;
					for (int i = 0; i < left.length / 2; i++) {
						shape[index++] = x + left[2 * i];
						shape[index++] = y + height + left[2 * i + 1] - 1;
					}
					for (int i = 0; i < right.length / 2; i++) {
						shape[index++] = parent.simple ? rightEdge - 1 + right[2 * i]
								: rightEdge - curveIndent + right[2 * i];
						shape[index++] = parent.simple ? y + height + right[2 * i + 1] - 1 : y + right[2 * i + 1] - 2;
					}
					shape[index++] = parent.simple ? rightEdge - 1 : rightEdge + curveWidth - curveIndent;
					shape[index++] = y - 1;
					shape[index++] = parent.simple ? rightEdge - 1 : rightEdge + curveWidth - curveIndent;
					shape[index++] = y - 1;
				} else {
					int[] left = parent.simple ? SIMPLE_TOP_LEFT_CORNER : TOP_LEFT_CORNER;
					int[] right = parent.simple ? SIMPLE_TOP_RIGHT_CORNER : curve;
					if (borderLeft == 0 && itemIndex == parent.firstIndex) {
						left = new int[] { x, y };
					}
					shape = new int[left.length + right.length + 8];
					int index = 0;
					shape[index++] = x; // first point repeated here because below we reuse shape to draw outline
					shape[index++] = y + height + 1;
					shape[index++] = x;
					shape[index++] = y + height + 1;
					for (int i = 0; i < left.length / 2; i++) {
						shape[index++] = x + left[2 * i];
						shape[index++] = y + left[2 * i + 1];
					}
					for (int i = 0; i < right.length / 2; i++) {
						shape[index++] = parent.simple ? rightEdge - 1 + right[2 * i]
								: rightEdge - curveIndent + right[2 * i];
						shape[index++] = y + right[2 * i + 1];
					}
					shape[index++] = parent.simple ? rightEdge - 1 : rightEdge + curveWidth - curveIndent;
					shape[index++] = y + height + 1;
					shape[index++] = parent.simple ? rightEdge - 1 : rightEdge + curveWidth - curveIndent;
					shape[index++] = y + height + 1;
				}

				Rectangle clipping = gc.getClipping();
				Rectangle clipBounds = item.getBounds();
				clipBounds.height += 1;
				if (parent.onBottom) {
					clipBounds.y -= 1;
				}
				boolean tabInPaint = clipping.intersects(clipBounds);

				if (tabInPaint) {
					// fill in tab background
					if (parent.selectionGradientColors != null && !parent.selectionGradientVertical) {
						drawBackground(gc, shape, true);
					} else {
						Color defaultBackground = parent.shouldHighlight() ? parent.selectionBackground
								: parent.getBackground();
						Image image = parent.selectionBgImage;
						Color[] colors = parent.selectionGradientColors;
						int[] percents = parent.selectionGradientPercents;
						boolean vertical = parent.selectionGradientVertical;
						xx = x;
						yy = parent.onBottom ? y - 1 : y + 1;
						ww = width;
						hh = height;
						if (!parent.single && !parent.simple) {
							ww += curveWidth - curveIndent;
						}
						drawBackground(gc, shape, xx, yy, ww, hh, defaultBackground, image, colors, percents, vertical);
					}
				}

				// Highlight colors MUST be drawn before the outline so that outline can cover
				// it in the right spots (start of swoop)
				// otherwise the curve looks jagged
				drawHighlight(gc, bounds, state, rightEdge);

				// draw highlight marker of selected tab
				if (parent.selectionHighlightBarThickness > 0 && parent.simple) {
					Color previousColor = gc.getBackground();
					gc.setBackground(
							item.getDisplay().getSystemColor(parent.shouldHighlight() ? SWT.COLOR_LIST_SELECTION
									: SWT.COLOR_WIDGET_DISABLED_FOREGROUND));
					gc.fillRectangle(x + 1 /* outline */,
							parent.onBottom ? y + height - 1 - parent.selectionHighlightBarThickness : y + 1,
							width - 2 /* outline */, parent.selectionHighlightBarThickness);
					gc.setBackground(previousColor);
				}

				// draw outline
				shape[0] = Math.max(0, borderLeft - 1);
				if (borderLeft == 0 && itemIndex == parent.firstIndex) {
					shape[1] = parent.onBottom ? y + height - 1 : y;
					shape[5] = shape[3] = shape[1];
				}
				shape[shape.length - 2] = size.x - borderRight + 1;
				for (int i = 0; i < shape.length / 2; i++) {
					if (shape[2 * i + 1] == y + height + 1)
						shape[2 * i + 1] -= 1;
				}
				Color borderColor = parent.getDisplay().getSystemColor(BORDER1_COLOR);
				if (!borderColor.equals(lastBorderColor)) {
					createAntialiasColors();
				}
				antialias(shape, selectedInnerColor, selectedOuterColor, gc);
				gc.setForeground(borderColor);
				gc.drawPolyline(shape);

				if (!tabInPaint) {
					return;
				}
			}
		}

		if ((state & SWT.FOREGROUND) != 0) {
			// draw Image
			Rectangle trim = computeTrim(itemIndex, SWT.NONE, 0, 0, 0, 0);
			int xDraw = x - trim.x;
			if (parent.single && shouldDrawCloseIcon(item)) {
				xDraw += item.closeRect.width;
			}
			Image image = item.getImage();
			if (image != null && !image.isDisposed() && parent.showSelectedImage) {
				Rectangle imageBounds = image.getBounds();
				// only draw image if it won't overlap with close button
				int maxImageWidth = rightEdge - xDraw - (trim.width + trim.x);
				if (!parent.single && item.closeRect.width > 0) {
					maxImageWidth -= item.closeRect.width + INTERNAL_SPACING;
				}
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
			if (!parent.single && item.closeRect.width > 0) {
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

				gc.setForeground(
						item.selectionForeground == null ? parent.getSelectionForeground() : item.selectionForeground);
				gc.drawText(item.shortenedText, xDraw, textY, FLAGS);
				gc.setFont(gcFont);

				// draw a Focus rectangle
				if (parent.isFocusControl()) {
					Color orginalForeground = gc.getForeground();
					Color orginalBackground = gc.getBackground();
					Display display = parent.getDisplay();
					if (parent.simple || parent.single) {
						gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
						gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
						gc.drawFocus(xDraw - 1, textY - 1, extent.x + 2, extent.y + 2);
					} else {
						gc.setForeground(display.getSystemColor(BUTTON_BORDER));
						gc.drawLine(xDraw, textY + extent.y + 1, xDraw + extent.x + 1, textY + extent.y + 1);
					}
					gc.setForeground(orginalForeground);
					gc.setBackground(orginalBackground);
				}
			}
			if (shouldDrawCloseIcon(item)) {
				drawClose(gc, item.closeRect, item.closeImageState);
			}
		}
	}

	private int getLeftTextMargin(TabItem item) {
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
		int[] shape = null;
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
			if ((style & SWT.FLAT) != 0 && (style & SWT.BORDER) == 0) {
				return;
			}
			int x1 = borderLeft - 1;
			int x2 = size.x - borderRight;
			int y1 = parent.onBottom ? size.y - borderBottom - highlight_header - 1 : borderTop + highlight_header;
			int y2 = parent.onBottom ? size.y - borderBottom : borderTop;
			if (borderLeft > 0 && parent.onBottom) {
				y2 -= 1;
			}

			shape = new int[] { x1, y1, x1, y2, x2, y2, x2, y1 };

			// If horizontal gradient, show gradient across the whole area
			if (selectedIndex != -1 && parent.selectionGradientColors != null
					&& parent.selectionGradientColors.length > 1 && !parent.selectionGradientVertical) {
				drawBackground(gc, shape, true);
			} else if (selectedIndex == -1 && parent.gradientColors != null && parent.gradientColors.length > 1
					&& !parent.gradientVertical) {
				drawBackground(gc, shape, false);
			} else {
				gc.setBackground(selectedIndex != -1 && parent.shouldHighlight() ? parent.selectionBackground
						: parent.getBackground());
				gc.fillPolygon(shape);
			}

			// draw 1 pixel border
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
		boolean simple = parent.simple;
		// Draw Tab Header
		if (parent.onBottom) {
			int[] left, right;
			if ((style & SWT.BORDER) != 0) {
				left = simple ? SIMPLE_BOTTOM_LEFT_CORNER : BOTTOM_LEFT_CORNER;
				right = simple ? SIMPLE_BOTTOM_RIGHT_CORNER : BOTTOM_RIGHT_CORNER;
			} else {
				left = simple ? SIMPLE_BOTTOM_LEFT_CORNER_BORDERLESS : BOTTOM_LEFT_CORNER_BORDERLESS;
				right = simple ? SIMPLE_BOTTOM_RIGHT_CORNER_BORDERLESS : BOTTOM_RIGHT_CORNER_BORDERLESS;
			}
			shape = new int[left.length + right.length + 4];
			int index = 0;
			shape[index++] = x;
			shape[index++] = y - highlight_header;
			for (int i = 0; i < left.length / 2; i++) {
				shape[index++] = x + left[2 * i];
				shape[index++] = y + height + left[2 * i + 1];
				if (borderLeft == 0) {
					shape[index - 1] += 1;
				}
			}
			for (int i = 0; i < right.length / 2; i++) {
				shape[index++] = x + width + right[2 * i];
				shape[index++] = y + height + right[2 * i + 1];
				if (borderLeft == 0) {
					shape[index - 1] += 1;
				}
			}
			shape[index++] = x + width;
			shape[index++] = y - highlight_header;
		} else {
			int[] left, right;
			if ((style & SWT.BORDER) != 0) {
				left = simple ? SIMPLE_TOP_LEFT_CORNER : TOP_LEFT_CORNER;
				right = simple ? SIMPLE_TOP_RIGHT_CORNER : TOP_RIGHT_CORNER;
			} else {
				left = simple ? SIMPLE_TOP_LEFT_CORNER_BORDERLESS : TOP_LEFT_CORNER_BORDERLESS;
				right = simple ? SIMPLE_TOP_RIGHT_CORNER_BORDERLESS : TOP_RIGHT_CORNER_BORDERLESS;
			}
			shape = new int[left.length + right.length + 4];
			int index = 0;
			shape[index++] = x;
			shape[index++] = y + height + highlight_header + 1;
			for (int i = 0; i < left.length / 2; i++) {
				shape[index++] = x + left[2 * i];
				shape[index++] = y + left[2 * i + 1];
			}
			for (int i = 0; i < right.length / 2; i++) {
				shape[index++] = x + width + right[2 * i];
				shape[index++] = y + right[2 * i + 1];
			}
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
			if (!borderColor.equals(lastBorderColor)) {
				createAntialiasColors();
			}
			antialias(shape, null, tabAreaColor, gc);
			gc.setForeground(borderColor);
			gc.drawPolyline(shape);
		}
	}

	void drawUnselected(int index, GC gc, Rectangle bounds, int state) {
		TabItem item = parent.items[index];
		int x = bounds.x;
		int y = bounds.y;
		int height = bounds.height;
		int width = bounds.width;

		// Do not draw partial items
		if (!item.showing) return;

		Rectangle clipping = gc.getClipping();
		if (!clipping.intersects(bounds)) return;

		if ((state & SWT.BACKGROUND) != 0) {
			if (index > 0 && index < parent.selectedIndex) {
				drawLeftUnselectedBorder(gc, bounds, state);
			}
			// If it is the last one then draw a line
			if (index > parent.selectedIndex) {
				drawRightUnselectedBorder(gc, bounds, state);
			}
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
					gc.drawImage(image, imageBounds.x, imageBounds.y, imageBounds.width, imageBounds.height, imageX,
							imageY, imageWidth, imageHeight);
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
			if (shouldDrawCloseIcon(item)) {
				drawClose(gc, item.closeRect, item.closeImageState);
			}
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

	/*
	 * Return true if given start color, the cache of highlight colors we have would
	 * match the highlight colors we'd compute.
	 */
	boolean isSelectionHighlightColorsCacheHit(Color start) {
		if (selectionHighlightGradientColorsCache == null) return false;

		// this case should never happen but check to be safe before accessing array
		// indexes
		if (selectionHighlightGradientColorsCache.length < 2) return false;

		Color highlightBegin = selectionHighlightGradientColorsCache[0];
		Color highlightEnd = selectionHighlightGradientColorsCache[selectionHighlightGradientColorsCache.length - 1];

		if (!highlightBegin.equals(start)) {
			return false;
		}

		// Compare number of colours we have vs. we'd compute
		if (selectionHighlightGradientColorsCache.length != parent.tabHeight) {
			return false;
		}

		// Compare existing highlight end to what it would be (selectionBackground)
		return highlightEnd.equals(parent.selectionBackground);
	}

	@Override
	protected void resetChevronFont() {
		if (chevronFont != null) {
			chevronFont.dispose();
			chevronFont = null;
		}
	}

	@Override
	protected void setSelectionHighlightGradientColor(Color start) {
		//
		// Set to null to match all the early return cases.
		// For early returns, don't realloc the cache, we may get a cache hit next time
		// we're given the highlight
		selectionHighlightGradientBegin = null;

		if (start == null) return;

		// don't bother on low colour
		if (parent.getDisplay().getDepth() < 15) return;

		// don't bother if we don't have a background gradient
		if (parent.selectionGradientColors.length < 2) return;

		// OK we know its a valid gradient now
		selectionHighlightGradientBegin = start;

		if (!isSelectionHighlightColorsCacheHit(start)) {
			createSelectionHighlightGradientColors(start); // if no cache hit then compute new ones
		}
	}

	String shortenText(GC gc, String text, int width) {
		return useEllipses() ? shortenText(gc, text, width, ELLIPSIS) : shortenText(gc, text, width, ""); //$NON-NLS-1$
	}

	String shortenText(GC gc, String text, int width, String ellipses) {
		if (gc.textExtent(text, FLAGS).x <= width) {
			return text;
		}
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

	void updateCurves() {
		// Temp fix for Bug 384743
		if (this.getClass().getName().equals("org.eclipse.e4.ui.workbench.renderers.swt.CTabRendering")) return;
		int tabHeight = parent.tabHeight;
		if (tabHeight == lastTabHeight) return;
		lastTabHeight = tabHeight;
		if (parent.onBottom) {
			int d = tabHeight - 12;
			curve = new int[] { 0, 13 + d, 0, 12 + d, 2, 12 + d, 3, 11 + d, 5, 11 + d, 6, 10 + d, 7, 10 + d, 9, 8 + d,
					10, 8 + d, 11, 7 + d, 11 + d, 7, 12 + d, 6, 13 + d, 6, 15 + d, 4, 16 + d, 4, 17 + d, 3, 19 + d, 3,
					20 + d, 2, 22 + d, 2, 23 + d, 1 };
			curveWidth = 26 + d;
			curveIndent = curveWidth / 3;
		} else {
			int d = tabHeight - 12;
			curve = new int[] { 0, 0, 0, 1, 2, 1, 3, 2, 5, 2, 6, 3, 7, 3, 9, 5, 10, 5, 11, 6, 11 + d, 6 + d, 12 + d,
					7 + d, 13 + d, 7 + d, 15 + d, 9 + d, 16 + d, 9 + d, 17 + d, 10 + d, 19 + d, 10 + d, 20 + d, 11 + d,
					22 + d, 11 + d, 23 + d, 12 + d };
			curveWidth = 26 + d;
			curveIndent = curveWidth / 3;

			// this could be static but since values depend on curve, better to keep in one
			// place
			topCurveHighlightStart = new int[] { 0, 2, 1, 2, 2, 2, 3, 3, 4, 3, 5, 3, 6, 4, 7, 4, 8, 5, 9, 6, 10, 6 };

			// also, by adding in 'd' here we save some math cost when drawing the curve
			topCurveHighlightEnd = new int[] { 10 + d, 6 + d, 11 + d, 7 + d, 12 + d, 8 + d, 13 + d, 8 + d, 14 + d,
					9 + d, 15 + d, 10 + d, 16 + d, 10 + d, 17 + d, 11 + d, 18 + d, 11 + d, 19 + d, 11 + d, 20 + d,
					12 + d, 21 + d, 12 + d, 22 + d, 12 + d };
		}
	}

	/*
	 * Return whether to use ellipses or just truncate labels
	 */
	boolean useEllipses() {
		return parent.simple;
	}

	@Override
	protected void paint(GC gc, int width, int height) {
		Font font = parent.getFont();
		Font oldFont = parent.getOldFont();
		if (oldFont == null || !oldFont.equals(font)) {
			// handle case where default font changes
			oldFont = font;
			if (!parent.updateTabHeight(false)) {
				parent.updateItems();
				parent.redraw();
				return;
			}
		}

		Font gcFont = gc.getFont();
		Color gcBackground = gc.getBackground();
		Color gcForeground = gc.getForeground();

		// Useful for debugging paint problems
		// {
		// Point size = getSize();
		// gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_GREEN));
		// gc.fillRectangle(-10, -10, size.x + 20, size.y+20);
		// }

		Rectangle bodyRect = new Rectangle(0, 0, width, height);
		draw(TabFolderRenderer.PART_BODY, SWT.BACKGROUND | SWT.FOREGROUND, bodyRect, gc);

		gc.setFont(gcFont);
		gc.setForeground(gcForeground);
		gc.setBackground(gcBackground);

		draw(TabFolderRenderer.PART_HEADER, SWT.BACKGROUND | SWT.FOREGROUND, bodyRect, gc);

		gc.setFont(gcFont);
		gc.setForeground(gcForeground);
		gc.setBackground(gcBackground);

		TabItem[] items = parent.getItems();
		if (!parent.getSingle()) {
			for (int i = 0; i < items.length; i++) {
				Rectangle itemBounds = items[i].getBounds();
				if (i != parent.getSelectionIndex() && parent.getBounds().intersects(itemBounds)) {
					draw(i, SWT.BACKGROUND | SWT.FOREGROUND | items[i].state, itemBounds, gc);
				}
			}
		}

		gc.setFont(gcFont);
		gc.setForeground(gcForeground);
		gc.setBackground(gcBackground);

		int selectedIndex = parent.getSelectionIndex();
		if (selectedIndex != -1) {
			draw(selectedIndex, items[selectedIndex].state | SWT.BACKGROUND | SWT.FOREGROUND,
					items[selectedIndex].getBounds(), gc);
		}

		gc.setFont(gcFont);
		gc.setForeground(gcForeground);
		gc.setBackground(gcBackground);

		if (parent.getHoverTb()) {
			Rectangle trim = computeTrim(TabFolderRenderer.PART_BORDER, SWT.NONE, 0, 0, 0, 0);
			int x = parent.getSize().x - (trim.width + trim.x);
			Rectangle hoverRect = new Rectangle(x - 16 - parent.getSpacing(), 2, 16, parent.getTabHeight() - 2);
			parent.setHoverRect(hoverRect);
			gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
			x = hoverRect.x;
			int y = hoverRect.y;
			gc.setBackground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
			gc.fillRectangle(x + hoverRect.width - 6, y, 5, 5);
			gc.drawRectangle(x + hoverRect.width - 6, y, 5, 5);
			gc.drawLine(x + hoverRect.width - 6, y + 2, x + hoverRect.width - 6 + 5, y + 2);
			gc.fillRectangle(x, y, 5, 2);
			gc.drawRectangle(x, y, 5, 2);
		}
		gc.setFont(gcFont);
		gc.setForeground(gcForeground);
		gc.setBackground(gcBackground);
	}
}
