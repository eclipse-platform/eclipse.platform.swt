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
 *     Raghunandana Murthappa(Advantest) - SkiJa Link implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.LinkRenderer.*;

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
public class Link extends CustomControl {

	/** Left and right margins */
	private static final int DEFAULT_MARGIN = 3;

	/** the alignment. Either CENTER, RIGHT, LEFT. Default is LEFT */
	private int align;
	private int leftMargin = DEFAULT_MARGIN;
	private int topMargin = DEFAULT_MARGIN;
	private int rightMargin = DEFAULT_MARGIN;
	private int bottomMargin = DEFAULT_MARGIN;

	private String text = "";

	private boolean ignoreDispose;

	private Set<TextSegment> links;;
	private String displayText = "";

	private Color linkColor;
	private final LinkRenderer renderer;

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
		this.style |= SWT.DOUBLE_BUFFERED;
		if ((style & (SWT.CENTER | SWT.RIGHT)) == 0) {
			style |= SWT.LEFT;
		}

		if ((style & SWT.CENTER) != 0) {
			align = SWT.CENTER;
		} else if ((style & SWT.RIGHT) != 0) {
			align = SWT.RIGHT;
		} else {
			align = SWT.LEFT;
		}

		final RendererFactory rendererFactory = parent.getDisplay().getRendererFactory();
		renderer = rendererFactory.createLinkRenderer(this);

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

	@Override
	protected ControlRenderer getRenderer() {
		return renderer;
	}

	private void onMouseUp(Event e) {
		if (links == null) {
			return;
		}
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
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Selection, listener);
		eventTable.unhook(SWT.DefaultSelection, listener);
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
	protected Point computeDefaultSize() {
		return renderer.computeDefaultSize();
	}

	@Override
	public int getStyle() {
		int style = super.getStyle();
		switch (getAlignment()) {
		case SWT.RIGHT -> style |= SWT.RIGHT;
		case SWT.CENTER -> style |= SWT.CENTER;
		case SWT.LEFT -> style |= SWT.LEFT;
		}
		return style;
	}

	private void initAccessible() {
		Accessible accessible = getAccessible();
		accessible.addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				e.result = displayText;
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
				if (hasFocus()) {
					e.detail |= ACC.STATE_FOCUSED;
				}
			}

			@Override
			public void getDefaultAction(AccessibleControlEvent e) {
				e.result = SWT.getMessage("SWT_Press"); //$NON-NLS-1$
			}

			@Override
			public void getSelection(AccessibleControlEvent e) {
				if (hasFocus()) {
					e.childID = ACC.CHILDID_SELF;
				}
			}

			@Override
			public void getFocus(AccessibleControlEvent e) {
				if (hasFocus()) {
					e.childID = ACC.CHILDID_SELF;
				}
			}
		});
	}

	private void onDispose(Event event) {
		/* make this handler run after other dispose listeners */
		if (ignoreDispose) {
			ignoreDispose = false;
			return;
		}
		ignoreDispose = true;
		notifyListeners(event.type, event);
		event.type = SWT.NONE;

		text = "";
		super.dispose();
	}

	private void onMouseMove(Event event) {
		if (links == null) {
			return;
		}

		if (renderer.isOverLink(event.x, event.y)) {
			setCursor(display.getSystemCursor(SWT.CURSOR_HAND));
		}
		else {
			setCursor(null);
		}
	}

	private void onPaint(Event event) {
		final Point size = getSize();
		if (size.x == 0 || size.y == 0) {
			return;
		}
		Drawing.drawWithGC(this, event.gc, gc -> renderer.paint(gc, size.x, size.y));
		links = renderer.getLinks();
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
		if (text == null) error(SWT.ERROR_NULL_ARGUMENT);
		if (this.text.equals(text)) return;

		this.text = text;

		renderer.parseLinkText(text);
		displayText = renderer.getLinkDisplayText();

		redraw();
	}

	/**
	 * Returns the receiver's text, which will be an empty string if it has never
	 * been set.
	 *
	 * @return the receiver's text
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public String getText() {
		checkWidget();
		return text;
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

		if (color == null) {
			return;
		}
		if (color.equals(linkColor)) {
			return;
		}

		this.linkColor = color;
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
		return linkColor != null ? linkColor : renderer.getDefaultLinkColor();
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
	 * Return the Link's left margin.
	 *
	 * @return the left margin of the link
	 *
	 * @since 3.6
	 */
	public int getLeftMargin() {
		checkWidget();
		return leftMargin;
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
	 * Return the Link's right margin.
	 *
	 * @return the right margin of the link
	 *
	 * @since 3.6
	 */
	public int getRightMargin() {
		checkWidget();
		return rightMargin;
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
		if (this.topMargin == topMargin || topMargin < 0) {
			return;
		}
		this.topMargin = topMargin;
		redraw();
	}

	/**
	 * Return the Link's top margin.
	 *
	 * @return the top margin of the link
	 *
	 * @since 3.6
	 */
	public int getTopMargin() {
		checkWidget();
		return topMargin;
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

	/**
	 * Return the Link's bottom margin.
	 *
	 * @return the bottom margin of the link
	 *
	 * @since 3.6
	 */
	public int getBottomMargin() {
		checkWidget();
		return bottomMargin;
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
		if (align != SWT.LEFT && align != SWT.RIGHT && align != SWT.CENTER) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (this.align != align) {
			this.align = align;
			redraw();
		}
	}

	/**
	 * Returns the horizontal alignment. The alignment style (LEFT, CENTER or RIGHT)
	 * is returned.
	 *
	 * @return SWT.LEFT, SWT.RIGHT or SWT.CENTER
	 */
	public int getAlignment() {
		checkWidget();
		return align;
	}
}
