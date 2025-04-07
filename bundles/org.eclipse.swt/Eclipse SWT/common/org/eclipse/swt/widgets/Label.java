/*******************************************************************************
 * Copyright (c) 2000, 2021 IBM Corporation and others.
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
 *     Paul Pazderski - Bug 205199: setImage(null) on Label overrides text
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.graphics.*;

/**
 * A Label which supports aligned text and/or an image and different border
 * styles.
 * <p>
 * If there is not enough space a Label uses the following strategy to fit the
 * information into the available space:
 *
 * <pre>
 * 		ignores the indent in left align mode
 * 		ignores the image and the gap
 * 		shortens the text by replacing the center portion of the label with an ellipsis
 * 		shortens the text by removing the center portion of the label
 * </pre>
 * <dl>
 * <dt><b>Styles:</b>
 * <dd>LEFT, RIGHT, CENTER, SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dt><b>Events:</b>
 * <dd>(NONE)</dd>
 * </dl>
 *
 * <p>
 * This class may be subclassed for the purpose of overriding the default string
 * shortening algorithm that is implemented in method
 * <code>shortenText()</code>.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      CustomControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @see LabelRenderer#shortenText(GC, String, int, int)
 */
public class Label extends CustomControl {

	private final LabelRenderer renderer;

	private String text;
	private Image image;

	private boolean ignoreDispose;

	/**
	 * Constructs a new instance of this class given its parent and a style
	 * value describing its behavior and appearance.
	 * <p>
	 * The style value is either one of the style constants defined in class
	 * <code>SWT</code> which is applicable to instances of this class, or must
	 * be built by <em>bitwise OR</em>'ing together (that is, using the
	 * <code>int</code> "|" operator) two or more of those <code>SWT</code>
	 * style constants. The class description lists the style constants that are
	 * applicable to the class. Style bits are also inherited from superclasses.
	 * </p>
	 *
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                </ul>
	 *
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#CENTER
	 * @see SWT#SHADOW_IN
	 * @see SWT#SHADOW_OUT
	 * @see SWT#SHADOW_NONE
	 * @see #getStyle()
	 */
	public Label(Composite parent, int style) {
		super(parent, checkStyle(style));
		this.style |= SWT.DOUBLE_BUFFERED;
		if ((style & (SWT.CENTER | SWT.RIGHT)) == 0) {
			style |= SWT.LEFT;
		}
		final int align;
		if ((style & SWT.CENTER) != 0) {
			align = SWT.CENTER;
		} else if ((style & SWT.RIGHT) != 0) {
			align = SWT.RIGHT;
		} else {
			align = SWT.LEFT;
		}

		final RendererFactory rendererFactory = parent.getDisplay().getRendererFactory();
		renderer = rendererFactory.createLabelRenderer(this);
		renderer.setAlign(align);
		renderer.setForeground(getForeground());

		final Listener listener = event -> {
			switch (event.type) {
			case SWT.Paint -> onPaint(event);
			case SWT.Traverse -> {
				if (event.detail == SWT.TRAVERSE_MNEMONIC) {
					onMnemonic(event);
				}
			}
			case SWT.Dispose -> onDispose(event);
			}
		};
		addListener(SWT.Paint, listener);
		addListener(SWT.Traverse, listener);
		addListener(SWT.Dispose, listener);

		initAccessible();
	}

	@Override
	protected ControlRenderer getRenderer() {
		return renderer;
	}

	/**
	 * Check the style bits to ensure that no invalid styles are applied.
	 */
	private static int checkStyle(int style) {
		if ((style & SWT.BORDER) != 0) {
			style |= SWT.SHADOW_IN;
		}
		int mask = SWT.SHADOW_IN | SWT.SHADOW_OUT | SWT.SHADOW_NONE
				| SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT;
		style = style & mask;
		style |= SWT.NO_FOCUS | SWT.DOUBLE_BUFFERED;
		return style;
	}

	@Override
	boolean isTabItem() {
		return false;
	}

	@Override
	protected Point computeDefaultSize() {
		return renderer.computeDefaultSize();
	}

	/*
	 * Return the lowercase of the first non-'&' character following an '&'
	 * character in the given string. If there are no '&' characters in the
	 * given string, return '\0'.
	 */
	char _findMnemonic(String string) {
		if (string == null) {
			return '\0';
		}

		int index = 0;
		int length = string.length();
		do {
			while (index < length && string.charAt(index) != '&') {
				index++;
			}
			if (++index >= length) {
				return '\0';
			}
			if (string.charAt(index) != '&') {
				return Character.toLowerCase(string.charAt(index));
			}
			index++;
		} while (index < length);
		return '\0';
	}

	/**
	 * Returns the horizontal alignment. The alignment style (LEFT, CENTER or
	 * RIGHT) is returned.
	 *
	 * @return SWT.LEFT, SWT.RIGHT or SWT.CENTER
	 */
	public int getAlignment() {
		/*
		 * This call is intentionally commented out, to allow this getter method
		 * to be called from a thread which is different from one that created
		 * the widget.
		 */
		// checkWidget();
		return renderer.getAlign();
	}

	/**
	 * Return the Label's bottom margin.
	 *
	 * @return the bottom margin of the label
	 *
	 * @since 3.6
	 */
	public int getBottomMargin() {
		/*
		 * This call is intentionally commented out, to allow this getter method
		 * to be called from a thread which is different from one that created
		 * the widget.
		 */
		// checkWidget();
		return renderer.getBottomMargin();
	}

	/**
	 * Return the Label's image or <code>null</code>.
	 *
	 * @return the image of the label or null
	 */
	public Image getImage() {
		/*
		 * This call is intentionally commented out, to allow this getter method
		 * to be called from a thread which is different from one that created
		 * the widget.
		 */
		// checkWidget();
		return image;
	}

	/**
	 * Return the Label's left margin.
	 *
	 * @return the left margin of the label
	 *
	 * @since 3.6
	 */
	public int getLeftMargin() {
		/*
		 * This call is intentionally commented out, to allow this getter method
		 * to be called from a thread which is different from one that created
		 * the widget.
		 */
		// checkWidget();
		return renderer.getLeftMargin();
	}

	/**
	 * Return the Label's right margin.
	 *
	 * @return the right margin of the label
	 *
	 * @since 3.6
	 */
	public int getRightMargin() {
		/*
		 * This call is intentionally commented out, to allow this getter method
		 * to be called from a thread which is different from one that created
		 * the widget.
		 */
		// checkWidget();
		return renderer.getRightMargin();
	}

	@Override
	public int getStyle() {
		int style = super.getStyle();
		switch (renderer.getAlign()) {
		case SWT.RIGHT -> style |= SWT.RIGHT;
		case SWT.CENTER -> style |= SWT.CENTER;
		case SWT.LEFT -> style |= SWT.LEFT;
		}
		return style;
	}

	/**
	 * Return the Label's text.
	 *
	 * @return the text of the label or null
	 */
	public String getText() {
		/*
		 * This call is intentionally commented out, to allow this getter method
		 * to be called from a thread which is different from one that created
		 * the widget.
		 */
		// checkWidget();
		return text != null ? text : "";
	}

	@Override
	public String getToolTipText() {
		checkWidget();
		return renderer.getToolTipText();
	}

	/**
	 * Return the Label's top margin.
	 *
	 * @return the top margin of the label
	 *
	 * @since 3.6
	 */
	public int getTopMargin() {
		/*
		 * This call is intentionally commented out, to allow this getter method
		 * to be called from a thread which is different from one that created
		 * the widget.
		 */
		// checkWidget();
		return renderer.getTopMargin();
	}

	private void initAccessible() {
		Accessible accessible = getAccessible();
		accessible.addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				e.result = getText();
			}

			@Override
			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}

			@Override
			public void getKeyboardShortcut(AccessibleEvent e) {
				char mnemonic = _findMnemonic(text);
				if (mnemonic != '\0') {
					e.result = "Alt+" + mnemonic; //$NON-NLS-1$
				}
			}
		});

		accessible.addAccessibleControlListener(new AccessibleControlAdapter() {
			@Override
			public void getChildAtPoint(AccessibleControlEvent e) {
				e.childID = ACC.CHILDID_SELF;
			}

			@Override
			public void getLocation(AccessibleControlEvent e) {
				Rectangle rect = getDisplay().map(getParent(), null,
						getBounds());
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
				e.detail = ACC.ROLE_LABEL;
			}

			@Override
			public void getState(AccessibleControlEvent e) {
				e.detail = ACC.STATE_READONLY;
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

		text = null;
		image = null;
		renderer.dispose();
	}

	private void onMnemonic(Event event) {
		char mnemonic = _findMnemonic(text);
		if (mnemonic == '\0') {
			return;
		}
		if (Character.toLowerCase(event.character) != mnemonic) {
			return;
		}
		Composite control = this.getParent();
		while (control != null) {
			Control[] children = control.getChildren();
			int index = 0;
			while (index < children.length) {
				if (children[index] == this) {
					break;
				}
				index++;
			}
			index++;
			if (index < children.length) {
				if (children[index].setFocus()) {
					event.doit = true;
					event.detail = SWT.TRAVERSE_NONE;
				}
			}
			control = control.getParent();
		}
	}

	private void onPaint(Event event) {
		Drawing.drawWithGC(this, event.gc, renderer::paint);
	}


	/**
	 * Set the horizontal alignment of the Label. Use the values LEFT, CENTER
	 * and RIGHT to align image and text within the available space.
	 *
	 * @param align
	 *            the alignment style of LEFT, RIGHT or CENTER
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the value of align is not
	 *                one of SWT.LEFT, SWT.RIGHT or SWT.CENTER</li>
	 *                </ul>
	 */
	public void setAlignment(int align) {
		checkWidget();
		if (align != SWT.LEFT && align != SWT.RIGHT && align != SWT.CENTER) {
			SWT.error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if (renderer.getAlign() != align) {
			renderer.setAlign(align);
			redraw();
		}
	}

	/**
	 * Specify a gradient of colours to be drawn in the background of the Label.
	 * <p>
	 * For example, to draw a gradient that varies from dark blue to blue and
	 * then to white and stays white for the right half of the label, use the
	 * following call to setBackground:
	 * </p>
	 *
	 * <pre>
	 * Label.setBackground(
	 * 		new Color[]{display.getSystemColor(SWT.COLOR_DARK_BLUE),
	 * 				display.getSystemColor(SWT.COLOR_BLUE),
	 * 				display.getSystemColor(SWT.COLOR_WHITE),
	 * 				display.getSystemColor(SWT.COLOR_WHITE)},
	 * 		new int[]{25, 50, 100});
	 * </pre>
	 *
	 * @param colors
	 *            an array of Color that specifies the colors to appear in the
	 *            gradient in order of appearance from left to right; The value
	 *            <code>null</code> clears the background gradient; the value
	 *            <code>null</code> can be used inside the array of Color to
	 *            specify the background color.
	 * @param percents
	 *            an array of integers between 0 and 100 specifying the percent
	 *            of the width of the widget at which the color should change;
	 *            the size of the percents array must be one less than the size
	 *            of the colors array.
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the values of colors and
	 *                percents are not consistent</li>
	 *                </ul>
	 */
	public void setBackground(Color[] colors, int[] percents) {
		setBackground(colors, percents, false);
	}

	/**
	 * Specify a gradient of colours to be drawn in the background of the Label.
	 * <p>
	 * For example, to draw a gradient that varies from dark blue to white in
	 * the vertical, direction use the following call to setBackground:
	 * </p>
	 *
	 * <pre>
	 * Label.setBackground(
	 * 		new Color[]{display.getSystemColor(SWT.COLOR_DARK_BLUE),
	 * 				display.getSystemColor(SWT.COLOR_WHITE)},
	 * 		new int[]{100}, true);
	 * </pre>
	 *
	 * @param colors
	 *            an array of Color that specifies the colors to appear in the
	 *            gradient in order of appearance from left/top to right/bottom;
	 *            The value <code>null</code> clears the background gradient;
	 *            the value <code>null</code> can be used inside the array of
	 *            Color to specify the background color.
	 * @param percents
	 *            an array of integers between 0 and 100 specifying the percent
	 *            of the width/height of the widget at which the color should
	 *            change; the size of the percents array must be one less than
	 *            the size of the colors array.
	 * @param vertical
	 *            indicate the direction of the gradient. True is vertical and
	 *            false is horizontal.
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                <li>ERROR_INVALID_ARGUMENT - if the values of colors and
	 *                percents are not consistent</li>
	 *                </ul>
	 *
	 * @since 3.0
	 */
	public void setBackground(Color[] colors, int[] percents,
			boolean vertical) {
		checkWidget();
		renderer.setBackground(colors, percents, vertical);
		redraw();
	}

	/**
	 * Set the image to be drawn in the background of the label.
	 *
	 * @param image
	 *            the image to be drawn in the background
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public void setBackgroundImage(Image image) {
		checkWidget();
		if (image == renderer.getBackgroundImage()) {
			return;
		}
		renderer.setBackgroundImage(image);
		redraw();

	}

	/**
	 * Set the label's bottom margin, in points.
	 *
	 * @param bottomMargin
	 *            the bottom margin of the label, which must be equal to or
	 *            greater than zero
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.6
	 */
	public void setBottomMargin(int bottomMargin) {
		checkWidget();
		if (renderer.getBottomMargin() == bottomMargin || bottomMargin < 0) {
			return;
		}
		renderer.setBottomMargin(bottomMargin);
		redraw();
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font);
		redraw();
	}

	/**
	 * Set the label's Image. The value <code>null</code> clears it.
	 *
	 * @param image
	 *            the image to be displayed in the label or null
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setImage(Image image) {
		checkWidget();
		if (image != this.image) {
			this.image = image;
			redraw();
		}
	}

	/**
	 * Set the label's horizontal left margin, in points.
	 *
	 * @param leftMargin
	 *            the left margin of the label, which must be equal to or
	 *            greater than zero
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.6
	 */
	public void setLeftMargin(int leftMargin) {
		checkWidget();
		if (renderer.getLeftMargin() == leftMargin || leftMargin < 0) {
			return;
		}
		renderer.setLeftMargin(leftMargin);
		redraw();
	}

	/**
	 * Set the label's margins, in points.
	 *
	 * @param leftMargin
	 *            the left margin.
	 * @param topMargin
	 *            the top margin.
	 * @param rightMargin
	 *            the right margin.
	 * @param bottomMargin
	 *            the bottom margin.
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.6
	 */
	public void setMargins(int leftMargin, int topMargin, int rightMargin,
			int bottomMargin) {
		checkWidget();
		renderer.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
		redraw();
	}

	/**
	 * Set the label's right margin, in points.
	 *
	 * @param rightMargin
	 *            the right margin of the label, which must be equal to or
	 *            greater than zero
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.6
	 */
	public void setRightMargin(int rightMargin) {
		checkWidget();
		if (renderer.getRightMargin() == rightMargin || rightMargin < 0) {
			return;
		}

		renderer.setRightMargin(rightMargin);
		redraw();
	}

	/**
	 * Set the label's text. The value <code>null</code> clears it.
	 * <p>
	 * Mnemonics are indicated by an '&amp;' that causes the next character to
	 * be the mnemonic. When the user presses a key sequence that matches the
	 * mnemonic, focus is assigned to the control that follows the label. On
	 * most platforms, the mnemonic appears underlined but may be emphasised in
	 * a platform specific manner. The mnemonic indicator character '&amp;' can
	 * be escaped by doubling it in the string, causing a single '&amp;' to be
	 * displayed.
	 * </p>
	 * <p>
	 * Note: If control characters like '\n', '\t' etc. are used in the string,
	 * then the behavior is platform dependent.
	 * </p>
	 *
	 * @param text
	 *            the text to be displayed in the label or null
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setText(String text) {
		checkWidget();
		if (text == null) {
			text = ""; //$NON-NLS-1$
		}
		if (!text.equals(this.text)) {
			this.text = text;
			redraw();
		}
	}

	@Override
	public void setToolTipText(String string) {
		setDisplayedToolTip(string);
		renderer.setToolTipText(string);
	}

	void setDisplayedToolTip(String string) {
		super.setToolTipText(string);
	}

	/**
	 * Set the label's top margin, in points.
	 *
	 * @param topMargin
	 *            the top margin of the label, which must be equal to or greater
	 *            than zero
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.6
	 */
	public void setTopMargin(int topMargin) {
		checkWidget();
		if (renderer.getTopMargin() == topMargin || topMargin < 0) {
			return;
		}

		renderer.setTopMargin(topMargin);
		redraw();
	}
}
