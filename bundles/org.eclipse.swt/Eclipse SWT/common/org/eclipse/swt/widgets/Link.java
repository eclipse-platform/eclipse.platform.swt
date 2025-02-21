/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
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
 *     Conrad Groth - Bug 401015 - [CSS] Add support for styling hyperlinks in Links
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;
import java.util.regex.*;
import java.util.regex.Pattern;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

import java.util.List;

/**
 * Instances of this class represent a selectable user interface object that
 * displays a text with links.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#link">Link snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 *
 * @since 3.1
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Link extends Control implements ICustomWidget {

	/** Left and right margins */
	private static final int DEFAULT_MARGIN = 3;

	/** the alignment. Either CENTER, RIGHT, LEFT. Default is LEFT */
	private int align = SWT.LEFT;
	private int leftMargin = DEFAULT_MARGIN;
	private int topMargin = DEFAULT_MARGIN;
	private int rightMargin = DEFAULT_MARGIN;
	private int bottomMargin = DEFAULT_MARGIN;

	private String text = "";

	private boolean ignoreDispose;

	private Image backgroundImage;

	private Color background;

	private Point computedSize;

	Color linkColor;

	private static final int DRAW_FLAGS = SWT.DRAW_MNEMONIC | SWT.DRAW_TAB | SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER;

	Set<TextSegment> links = new HashSet<>();
	TextSegment prevHoverLink;
	Map<String, List<TextSegment>> parsedText = new HashMap<>();

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must be
	 * built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code> style
	 * constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent a composite control which will be the parent of the new
	 *               instance (cannot be null)
	 * @param style  the style of control to construct
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the parent
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     parent</li>
	 *                                     <li>ERROR_INVALID_SUBCLASS - if this
	 *                                     class is not an allowed subclass</li>
	 *                                     </ul>
	 *
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Link(Composite parent, int style) {
		super(parent, checkStyle(style));
		if ((style & (SWT.CENTER | SWT.RIGHT)) == 0) {
			style |= SWT.LEFT;
		}
		if ((style & SWT.CENTER) != 0) {
			align = SWT.CENTER;
		} else if ((style & SWT.RIGHT) != 0) {
			align = SWT.RIGHT;
		} else if ((style & SWT.LEFT) != 0) {
			align = SWT.LEFT;
		}

		final Listener listener = event -> {
			switch (event.type) {
				case SWT.Paint -> onPaint(event);
				case SWT.MouseMove -> onMouseMove(event);
				case SWT.MouseUp -> onMouseUp(event);
				case SWT.Dispose -> onDispose(event);
			}
		};
		addListener(SWT.Paint, listener);
		addListener(SWT.MouseMove, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.Dispose, listener);

		initAccessible();
	}

	private void onMouseUp(Event e) {
		int x = e.x;
		int y = e.y;
		if ((e.stateMask & SWT.BUTTON1) != 0) {
			for (TextSegment link : links) {
				if (link.rect.contains(x, y)) {
					Event event = new Event();
					event.text = link.linkData != null ? link.linkData : link.text;
					sendSelectionEvent(SWT.Selection, event, true);
				}
			}
		}
	}

	public void addSelectionListener(SelectionListener listener) {
		addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
	}

	/**
	 * Check the style bits to ensure that no invalid styles are applied.
	 */
	private static int checkStyle(int style) {
		if ((style & SWT.BORDER) != 0) {
			style |= SWT.SHADOW_IN;
		}
		int mask = SWT.SHADOW_IN | SWT.SHADOW_OUT | SWT.SHADOW_NONE | SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		style = style & mask;
		style |= SWT.NO_FOCUS | SWT.DOUBLE_BUFFERED;
		return style;
	}

	@Override
	boolean isTabItem() {
		return false;
	}

	@Override
	public Point computeSize(int wHint, int hHint) {
		return computeSize(wHint, hHint, true);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();

		if (!changed && computedSize != null) {
			return new Point(Math.max(wHint, computedSize.x), Math.max(hHint, computedSize.y));
		}

		int lineWidth = 0;
		int lineHeight = 0;

		int leftMargin = this.leftMargin;
		int topMargin = this.topMargin;

		if (!text.isEmpty()) {
			String[] lines = text.split("\n");
			for (String line : lines) {
				Point textExtent = Drawing.executeOnGC(this, gc -> {
					gc.setFont(getFont());
					return getLineExtent(gc, parseTextSegments(line));
				});
				lineWidth = textExtent.x > lineWidth ? textExtent.x : lineWidth;
				lineHeight = textExtent.y;
			}
		}

		int width = leftMargin + lineWidth + this.rightMargin;

		// Height must be multiple of lines.
		int newlineCount = (int) text.chars().filter(ch -> ch == '\n').count();
		if (text.contains("\n")) {
			lineHeight *= (newlineCount + 1);
		}

		int height = topMargin + lineHeight + this.bottomMargin;

		computedSize = new Point(width, height);

		return new Point(Math.max(wHint, computedSize.x), Math.max(hHint, computedSize.y));
	}

	/**
	 * Returns the horizontal alignment. The alignment style (LEFT, CENTER or RIGHT)
	 * is returned.
	 *
	 * @return SWT.LEFT, SWT.RIGHT or SWT.CENTER
	 */
	public int getAlignment() {
		/*
		 * This call is intentionally commented out, to allow this getter method to be
		 * called from a thread which is different from one that created the widget.
		 */
		// checkWidget();
		return align;
	}

	/**
	 * Return the Link's bottom margin.
	 *
	 * @return the bottom margin of the link
	 *
	 * @since 3.6
	 */
	public int getBottomMargin() {
		/*
		 * This call is intentionally commented out, to allow this getter method to be
		 * called from a thread which is different from one that created the widget.
		 */
		// checkWidget();
		return bottomMargin;
	}

	/**
	 * Return the Link's left margin.
	 *
	 * @return the left margin of the link
	 *
	 * @since 3.6
	 */
	public int getLeftMargin() {
		/*
		 * This call is intentionally commented out, to allow this getter method to be
		 * called from a thread which is different from one that created the widget.
		 */
		// checkWidget();
		return leftMargin;
	}

	/**
	 * Return the Link's right margin.
	 *
	 * @return the right margin of the link
	 *
	 * @since 3.6
	 */
	public int getRightMargin() {
		/*
		 * This call is intentionally commented out, to allow this getter method to be
		 * called from a thread which is different from one that created the widget.
		 */
		// checkWidget();
		return rightMargin;
	}

	@Override
	public int getStyle() {
		int style = super.getStyle();
		switch (align) {
		case SWT.RIGHT -> style |= SWT.RIGHT;
		case SWT.CENTER -> style |= SWT.CENTER;
		case SWT.LEFT -> style |= SWT.LEFT;
		}
		return style;
	}

	/**
	 * Return the Link's text.
	 *
	 * @return the text of the link or null
	 */
	public String getText() {
		/*
		 * This call is intentionally commented out, to allow this getter method to be
		 * called from a thread which is different from one that created the widget.
		 */
		// checkWidget();
		return text;
	}

	/**
	 * Return the Link's top margin.
	 *
	 * @return the top margin of the link
	 *
	 * @since 3.6
	 */
	public int getTopMargin() {
		/*
		 * This call is intentionally commented out, to allow this getter method to be
		 * called from a thread which is different from one that created the widget.
		 */
		// checkWidget();
		return topMargin;
	}

	private void initAccessible() {
		Accessible accessible = getAccessible();
		accessible.addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				e.result = getParsedText();
			}
		});

		accessible.addAccessibleControlListener(new AccessibleControlAdapter() {
			@Override
			public void getChildAtPoint(AccessibleControlEvent e) {
				e.childID = ACC.CHILDID_SELF;
			}

			@Override
			public void getLocation(AccessibleControlEvent e) {
				Rectangle rect = display.map(getParent(), null, getBounds());
				e.x = rect.x;
				e.y = rect.y;
				e.width = rect.width;
				e.height = rect.height;
			}

			@Override
			public void getChildCount(AccessibleControlEvent e) {
				e.detail = 0;
			}

			@Override
			public void getRole(AccessibleControlEvent e) {
				e.detail = ACC.ROLE_LINK;
			}

			@Override
			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_FOCUSABLE;
				if (hasFocus())
					e.detail |= ACC.STATE_FOCUSED;
			}

			@Override
			public void getDefaultAction(AccessibleControlEvent e) {
				e.result = SWT.getMessage("SWT_Press"); //$NON-NLS-1$
			}

			@Override
			public void getSelection(AccessibleControlEvent e) {
				if (hasFocus())
					e.childID = ACC.CHILDID_SELF;
			}

			@Override
			public void getFocus(AccessibleControlEvent e) {
				if (hasFocus())
					e.childID = ACC.CHILDID_SELF;
			}
		});
	}

	private String getParsedText() {
		StringBuilder sb = new StringBuilder();
		String[] lines = text.split("\n");
		for (String line : lines) {
			List<TextSegment> segments = parseTextSegments(line);
			for (TextSegment segment : segments) {
				sb.append(segment.text);
			}
		}
		return sb.toString();
	}

	void onDispose(Event event) {
		/* make this handler run after other dispose listeners */
		if (ignoreDispose) {
			ignoreDispose = false;
			return;
		}
		ignoreDispose = true;
		notifyListeners(event.type, event);
		event.type = SWT.NONE;

		backgroundImage = null;
		text = "";
	}

	void onMouseMove(Event event) {
		int x = event.x;
		int y = event.y;

		if (prevHoverLink != null && prevHoverLink.rect.contains(x, y)) {
			setCursor(display.getSystemCursor(SWT.CURSOR_HAND));
			return;
		}

		for (TextSegment link : links) {
			if (link.rect.contains(x, y)) {
				setCursor(display.getSystemCursor(SWT.CURSOR_HAND));
				prevHoverLink = link;
				return;
			}
		}
		setCursor(null);
	}

	void onPaint(Event event) {
		Drawing.drawWithGC(this, event.gc, this::doPaint);
	}

	void doPaint(GC gc) {
		Rectangle rect = getBounds();

		if (rect.width == 0 || rect.height == 0)
			return;
		if (text.isEmpty())
			return;

		gc.setFont(font);
		gc.setBackground(getBackground());
		gc.setClipping(new Rectangle(0, 0, rect.width, rect.height));
		gc.setForeground(getForeground());

		drawBackground(gc, rect);

		Color linkColor = this.linkColor != null ? this.linkColor
				: getDisplay().getSystemColor(SWT.COLOR_LINK_FOREGROUND);

		links.clear();

		int x = leftMargin;
		int lineY = topMargin;

		for (String line : parsedText.keySet()) {
			List<TextSegment> segments = parsedText.get(line);
			int lineX = x;
			if (align == SWT.CENTER) {
				int lineWidth = getLineExtent(gc, segments).x;
				if (rect.width > lineWidth) {
					lineX = Math.max(x, (rect.width - lineWidth) / 2);
				}
			}
			if (align == SWT.RIGHT) {
				int lineWidth = getLineExtent(gc, segments).x;
				lineX = Math.max(x, rect.x + rect.width - rightMargin - lineWidth);
			}

			Point baseExtent = gc.textExtent("a", DRAW_FLAGS);

			for (TextSegment segment : segments) {
				Point extent = gc.textExtent(segment.text, DRAW_FLAGS);
				int noOfTrailSpaces = countTrailingSpaces(segment.text);
				if (noOfTrailSpaces > 0) {
					extent.x = extent.x + noOfTrailSpaces * baseExtent.x;
				}

				if (isEnabled()) {
					gc.setForeground(segment.isLink ? linkColor : getForeground());
				} else {
					gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
				}
				gc.drawText(segment.text, lineX, lineY, DRAW_FLAGS);

				if (segment.isLink) {
					int underlineY = lineY + extent.y - 2;
					gc.drawLine(lineX, underlineY, lineX + extent.x, underlineY);
					// remember bounds of links
					segment.rect = new Rectangle(lineX, lineY, extent.x, extent.y);
					links.add(segment);
				}

				lineX += extent.x;
			}
			lineY += gc.getFontMetrics().getHeight();
		}
	}

	private Point getLineExtent(GC gc, List<TextSegment> segments) {
		StringBuilder sb = new StringBuilder();
		for (TextSegment textSegment : segments) {
			sb.append(textSegment.text);
		}
		return gc.textExtent(sb.toString(), DRAW_FLAGS);
	}

	private void drawBackground(GC gc, Rectangle rect) {
		// draw a background image behind the text
		try {
			if (backgroundImage != null) {
				// draw a background image behind the text
				Rectangle imageRect = backgroundImage.getBounds();
				// tile image to fill space
				gc.setBackground(getBackground());
				gc.fillRectangle(rect);
				int xPos = 0;
				while (xPos < rect.width) {
					int yPos = 0;
					while (yPos < rect.height) {
						gc.drawImage(backgroundImage, xPos, yPos);
						yPos += imageRect.height;
					}
					xPos += imageRect.width;
				}
			} else {
				if (background != null && background.getAlpha() > 0) {
					gc.setBackground(getBackground());
					gc.fillRectangle(rect);
				}
			}
		} catch (SWTException e) {
			if ((getStyle() & SWT.DOUBLE_BUFFERED) == 0) {
				gc.setBackground(getBackground());
				gc.fillRectangle(rect);
			}
		}
	}

	private int countTrailingSpaces(String text) {
		int count = 0;
		for (int i = text.length() - 1; i >= 0 && text.charAt(i) == ' '; i--) {
			count++;
		}
		return count;
	}

	private List<TextSegment> parseTextSegments(String input) {
		List<TextSegment> segments = new ArrayList<>();
		Pattern pattern = Pattern.compile("(.*?)<a(?: href=\\\"(.*?)\\\")?>(.*?)</a>([\\s.,]*)");
		Matcher matcher = pattern.matcher(input);

		int lastEnd = 0;

		while (matcher.find()) {
			// Extract normal text before <a> tag
			String normalText = matcher.group(1);
			if (!normalText.isEmpty()) {
				segments.add(new TextSegment(normalText, false, null, null));
			}

			// Extract href attribute (if present) and linked text inside <a> tag
			String href = matcher.group(2); // href="..." value (can be null)
			String linkText = matcher.group(3); // The actual clickable text
			segments.add(new TextSegment(linkText, true, href, null));

			// Capture trailing spaces and punctuation (important for handling ", "
			// correctly)
			String trailingText = matcher.group(4);
			if (!trailingText.isEmpty()) {
				segments.add(new TextSegment(trailingText, false, null, null));
			}

			lastEnd = matcher.end();
		}

		// Add any remaining text after the last <a> tag
		if (lastEnd < input.length()) {
			String remainingText = input.substring(lastEnd);
			segments.add(new TextSegment(remainingText, false, null, null));
		}

		return segments;
	}

	static class TextSegment {
		String text, linkData;
		boolean isLink;
		Rectangle rect;

		TextSegment(String text, boolean isLink, String linkData, Rectangle rect) {
			this.text = text;
			this.isLink = isLink;
			this.linkData = linkData;
			this.rect = rect;
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		redraw();
	}

	/**
	 * Set the horizontal alignment of the Link. Use the values LEFT, CENTER and
	 * RIGHT to align image and text within the available space.
	 *
	 * @param align the alignment style of LEFT, RIGHT or CENTER
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         <li>ERROR_INVALID_ARGUMENT - if the value of align is
	 *                         not one of SWT.LEFT, SWT.RIGHT or SWT.CENTER</li>
	 *                         </ul>
	 */
	public void setAlignment(int align) {
		checkWidget();
		if (align != SWT.LEFT && align != SWT.RIGHT && align != SWT.CENTER) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (this.align != align) {
			this.align = align;
			redraw();
		}
	}

	@Override
	public void setBackground(Color color) {
		super.setBackground(color);
		if (color == null || color.equals(background)) {
			return;
		}

		background = color;
		backgroundImage = null;
		redraw();
	}

	/**
	 * Set the image to be drawn in the background of the Link.
	 *
	 * @param image the image to be drawn in the background
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	@Override
	public void setBackgroundImage(Image image) {
		checkWidget();
		// background color takes the priority.
		if (background != null || image == backgroundImage) {
			return;
		}
		backgroundImage = image;
		redraw();
	}

	/**
	 * Set the Link's bottom margin, in points.
	 *
	 * @param bottomMargin the bottom margin of the link, which must be equal to or
	 *                     greater than zero
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.6
	 */
	public void setBottomMargin(int bottomMargin) {
		checkWidget();
		if (this.bottomMargin == bottomMargin || bottomMargin < 0) {
			return;
		}
		this.bottomMargin = bottomMargin;
		redraw();
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		redraw();
	}

	/**
	 * Set the Link's horizontal left margin, in points.
	 *
	 * @param leftMargin the left margin of the link, which must be equal to or
	 *                   greater than zero
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.6
	 */
	public void setLeftMargin(int leftMargin) {
		checkWidget();
		if (this.leftMargin == leftMargin || leftMargin < 0) {
			return;
		}
		this.leftMargin = leftMargin;
		redraw();
	}

	/**
	 * Set the Link's margins, in points.
	 *
	 * @param leftMargin   the left margin.
	 * @param topMargin    the top margin.
	 * @param rightMargin  the right margin.
	 * @param bottomMargin the bottom margin.
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.6
	 */
	public void setMargins(int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
		checkWidget();
		this.leftMargin = Math.max(0, leftMargin);
		this.topMargin = Math.max(0, topMargin);
		this.rightMargin = Math.max(0, rightMargin);
		this.bottomMargin = Math.max(0, bottomMargin);
		redraw();
	}

	/**
	 * Set the Link's right margin, in points.
	 *
	 * @param rightMargin the right margin of the link, which must be equal to or
	 *                    greater than zero
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.6
	 */
	public void setRightMargin(int rightMargin) {
		checkWidget();
		if (this.rightMargin == rightMargin || rightMargin < 0) {
			return;
		}
		this.rightMargin = rightMargin;
		redraw();
	}

	/**
	 * Sets the receiver's text.
	 * <p>
	 * The string can contain both regular text and hyperlinks. A hyperlink is
	 * delimited by an anchor tag, &lt;a&gt; and &lt;/a&gt;. Within an anchor, a
	 * single HREF attribute is supported. When a hyperlink is selected, the text
	 * field of the selection event contains either the text of the hyperlink or the
	 * value of its HREF, if one was specified. In the rare case of identical
	 * hyperlinks within the same string, the HREF attribute can be used to
	 * distinguish between them. The string may include the mnemonic character and
	 * line delimiters. The only delimiter the HREF attribute supports is the
	 * quotation mark ("). Text containing angle-bracket characters &lt; or &gt; may
	 * be escaped using \\, however this operation is a hint and varies from
	 * platform to platform.
	 * </p>
	 * <p>
	 * Mnemonics are indicated by an '&amp;' that causes the next character to be
	 * the mnemonic. The receiver can have a mnemonic in the text preceding each
	 * link. When the user presses a key sequence that matches the mnemonic, focus
	 * is assigned to the link that follows the text. Mnemonics in links and in the
	 * trailing text are ignored. On most platforms, the mnemonic appears underlined
	 * but may be emphasised in a platform specific manner. The mnemonic indicator
	 * character '&amp;' can be escaped by doubling it in the string, causing a
	 * single '&amp;' to be displayed.
	 * </p>
	 *
	 * @param text the new text
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the text is
	 *                                     null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 */
	public void setText(String text) {
		checkWidget();
		if (text == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		computedSize = null;

		parsedText.clear();
		String[] lines = text.split("\n");
		for (String line : lines) {
			parsedText.put(line, parseTextSegments(line));
		}

		if (!text.equals(this.text)) {
			this.text = text;
			redraw();
		}
	}

	/**
	 * Set the Link's top margin, in points.
	 *
	 * @param topMargin the top margin of the link, which must be equal to or
	 *                  greater than zero
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.6
	 */
	public void setTopMargin(int topMargin) {
		checkWidget();
		if (this.topMargin == topMargin || topMargin < 0) {
			return;
		}
		this.topMargin = topMargin;
		redraw();
	}

	/**
	 * Sets the link foreground color to the color specified by the argument, or to
	 * the default system color for the link if the argument is null.
	 * <p>
	 * Note: This operation is a hint and may be overridden by the platform.
	 * </p>
	 *
	 * @param color the new color (or null)
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_INVALID_ARGUMENT - if the
	 *                                     argument has been disposed</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 * @since 3.105
	 */
	public void setLinkForeground(Color color) {
		checkWidget();
		if (color != null) {
			if (color.isDisposed())
				error(SWT.ERROR_INVALID_ARGUMENT);
			if (color.equals(linkColor))
				return;
		} else if (linkColor == null)
			return;
		linkColor = color;
		if (getEnabled()) {
			redraw();
		}
	}

	/**
	 * Returns the link foreground color.
	 *
	 * @return the receiver's link foreground color.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 * @since 3.105
	 */
	public Color getLinkForeground() {
		checkWidget();
		return linkColor != null ? linkColor : display.getSystemColor(SWT.COLOR_LINK_FOREGROUND);
	}

	/**
	 * Removes the listener from the collection of listeners who will be notified
	 * when the control is selected by the user.
	 *
	 * @param listener the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the listener
	 *                                     is null</li>
	 *                                     </ul>
	 * @exception SWTException
	 *                                     <ul>
	 *                                     <li>ERROR_WIDGET_DISPOSED - if the
	 *                                     receiver has been disposed</li>
	 *                                     <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                                     called from the thread that created the
	 *                                     receiver</li>
	 *                                     </ul>
	 *
	 * @see SelectionListener
	 * @see #addSelectionListener
	 */
	public void removeSelectionListener(SelectionListener listener) {
		checkWidget();
		if (listener == null)
			error(SWT.ERROR_NULL_ARGUMENT);
		if (eventTable == null)
			return;
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
	}
}
