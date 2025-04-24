/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
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
 *     Conrad Groth - Bug 23837 [FEEP] Button, do not respect foreground and background color on Windows
 *******************************************************************************/
package org.eclipse.swt.widgets;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class represent a selectable user interface object that
 * issues notification when pressed and released.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>ARROW, CHECK, PUSH, RADIO, TOGGLE, FLAT, WRAP</dd>
 * <dd>UP, DOWN, LEFT, RIGHT, CENTER</dd>
 * <dt><b>Events:</b></dt>
 * <dd>Selection</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles ARROW, CHECK, PUSH, RADIO, and TOGGLE may be
 * specified.
 * </p>
 * <p>
 * Note: Only one of the styles LEFT, RIGHT, and CENTER may be specified.
 * </p>
 * <p>
 * Note: Only one of the styles UP, DOWN, LEFT, and RIGHT may be specified when
 * the ARROW style is specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#button">Button
 *      snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Button extends CustomControl {
	private String text = "", message = "";
	private Image image;
	private boolean checked;
	private boolean grayed;
	static /* final */ boolean COMMAND_LINK = false;

	private Accessible acc;
	private AccessibleAdapter accAdapter;
	private boolean defaultButton;

	private final ButtonRenderer renderer;

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
	 *            a composite control which will be the parent of the new
	 *            instance (cannot be null)
	 * @param style
	 *            the style of control to construct
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 *
	 * @see SWT#ARROW
	 * @see SWT#CHECK
	 * @see SWT#PUSH
	 * @see SWT#RADIO
	 * @see SWT#TOGGLE
	 * @see SWT#FLAT
	 * @see SWT#UP
	 * @see SWT#DOWN
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#CENTER
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Button(Composite parent, int style) {
		super(parent, checkStyle(style));
		this.style |= SWT.DOUBLE_BUFFERED;

		final RendererFactory rendererFactory = parent.getDisplay().getRendererFactory();
		renderer = (this.style & SWT.CHECK) != 0
				? rendererFactory.createCheckboxRenderer(this)
				: (this.style & SWT.RADIO) != 0
				? rendererFactory.createRadioButtonRenderer(this)
				: (this.style & SWT.ARROW) != 0
				? rendererFactory.createArrowButtonRenderer(this)
				: rendererFactory.createPushToggleButtonRenderer(this);

		Listener listener = event -> {
			switch (event.type) {
			case SWT.MouseEnter -> onMouseEnter();
			case SWT.MouseExit -> onMouseExit();
			case SWT.Dispose -> onDispose(event);
			case SWT.MouseDown -> onMouseDown(event);
			case SWT.MouseUp -> onMouseUp(event);
			case SWT.Paint -> onPaint(event);
			case SWT.Resize -> onResize();
			case SWT.FocusIn -> onFocusIn();
			case SWT.FocusOut -> onFocusOut();
			case SWT.Traverse -> onTraverse(event);
			case SWT.Selection -> onSelection(event);
			case SWT.KeyDown -> onKeyDown(event);
			case SWT.KeyUp -> onKeyUp(event);
			}
		};
		addListener(SWT.Dispose, listener);
		addListener(SWT.MouseDown, listener);
		addListener(SWT.MouseUp, listener);
		addListener(SWT.Paint, listener);
		addListener(SWT.Resize, listener);
		addListener(SWT.KeyUp, listener);
		addListener(SWT.KeyDown, listener);
		addListener(SWT.FocusIn, listener);
		addListener(SWT.FocusOut, listener);
		addListener(SWT.Traverse, listener);
		addListener(SWT.Selection, listener);
		addListener(SWT.MouseEnter, listener);
		addListener(SWT.MouseExit, listener);

		initializeAccessible();
	}

	@Override
	protected ControlRenderer getRenderer() {
		return renderer;
	}

	/**
	 * TODO: improve this support and make it completely similar to native
	 * windows buttons.
	 * ---------------------------------------------------------------------------
	 * Add accessibility support for the widget.
	 */
	void initializeAccessible() {
		acc = getAccessible();

		Button current = this;

		accAdapter = new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				if (current.isRadioButton()) {
					e.result = createRadioButtonText(current);
				} else if (current.isPushButton()) {
					e.result = createPushButtonText(current);
				} else {
					e.result = getText();
				}
			}

			@Override
			public void getHelp(AccessibleEvent e) {
				e.result = getToolTipText();
			}

			@Override
			public void getKeyboardShortcut(AccessibleEvent e) {
				String shortcut = null;
				String text = getText();
				if (text != null) {
					char mnemonic = _findMnemonic(text);
					if (mnemonic != '\0') {
						shortcut = "Alt+" + mnemonic; //$NON-NLS-1$
					}
				}
				e.result = shortcut;
			}
		};
		acc.addAccessibleListener(accAdapter);
		addListener(SWT.FocusIn, event -> acc.setFocus(ACC.CHILDID_SELF));
	}

	private boolean isPushButton() {
		return (style & SWT.PUSH) != 0;
	}

	private String createRadioButtonText(Button button) {
		StringBuilder b = new StringBuilder();
		Button[] radioGroup = getRadioGroup();

		int index = Arrays.asList(radioGroup).indexOf(button) + 1;
		int all = radioGroup.length;

		b.append(button.getText());
		b.append(" radio button checked.\r\n");
		b.append(index);
		b.append( " of ");
		b.append( all );
		b.append(".\r\n ");
		b.append("To change the selection press Up or Down Arrow.");

		return b.toString();
	}

	private String createPushButtonText(Button button) {
		return button.getText() + " button.\r\n To activate press space bar.";
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

	// TODO maybe this can be improved with a cache.
	// But this cache must be handled somehow on the parent element.
	private Button[] getRadioGroup() {
		if ((style & SWT.RADIO) == 0) {
			return null;
		}

		Control[] children = parent._getChildren();

		ArrayList<Button> radioGroup = new ArrayList<>();
		for (int k = 0; k < children.length; k++) {
			if (children[k] instanceof Button b
					&& (children[k].getStyle() & SWT.RADIO) != 0) {
				radioGroup.add(b);
			}
		}

		return radioGroup.toArray(new Button[0]);
	}

	@Override
	void sendSelectionEvent(int type) {
		super.sendSelectionEvent(type);
	}

	private void onSelection(Event event) {
		redraw();
	}

	private void onTraverse(Event event) {
	}

	private void onFocusIn() {
		redraw();
	}

	private void onFocusOut() {
		redraw();
	}

	private void onKeyDown(Event event) {
		if (event.character == SWT.SPACE) {
			renderer.setPressed(true);
			redraw();
		}
	}

	private void onKeyUp(Event event) {
		if (event.character == SWT.SPACE) {
			renderer.setPressed(false);
			handleSelection();
			redraw();
		}
	}

	private void onResize() {
		redraw();
	}

	private void onPaint(Event event) {
		if (!isVisible()) {
			return;
		}
		Drawing.drawWithGC(this, event.gc, renderer::paint);
	}

	private void onDispose(Event event) {
		dispose();
	}

	private void onMouseDown(Event e) {
		if (e.button == 1) {
			renderer.setPressed(true);
			redraw();
		}
	}

	private void handleSelection() {
		if (isRadioButton()) {
// here we have to force the focus, else the focus stays on another button in this group
// TODO this must be improved.
			forceFocus();
			selectRadio();
		} else if ((style & SWT.PUSH) == 0 && (style & (SWT.TOGGLE | SWT.CHECK)) != 0) {
			setSelection(!checked);
		}
		sendSelectionEvent(SWT.Selection);
	}

	private void onMouseUp(Event e) {
		if (e.button == 1) {
			renderer.setPressed(false);
			handleSelection();
		} else {
			redraw();
		}
	}

	private void onMouseEnter() {
		renderer.setHover(true);
		redraw();
	}

	private void onMouseExit() {
		renderer.setHover(false);
		redraw();
	}

	private boolean isRadioButton() {
		return (style & SWT.RADIO) != 0;
	}

	private boolean isCheckButton() {
		return (style & SWT.CHECK) != 0;
	}

	@Override
	protected Point computeDefaultSize() {
		return renderer.computeDefaultSize();
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control is selected by the user, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the control is selected by the
	 * user. <code>widgetDefaultSelected</code> is not called.
	 * </p>
	 * <p>
	 * When the <code>SWT.RADIO</code> style bit is set, the
	 * <code>widgetSelected</code> method is also called when the receiver loses
	 * selection because another item in the same radio group was selected by
	 * the user. During <code>widgetSelected</code> the application can use
	 * <code>getSelection()</code> to determine the current selected state of
	 * the receiver.
	 * </p>
	 *
	 * @param listener
	 *            the listener which should be notified
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @see SelectionListener
	 * @see #removeSelectionListener
	 * @see SelectionEvent
	 */
	public void addSelectionListener(SelectionListener listener) {
		addTypedListener(listener, SWT.Selection, SWT.DefaultSelection);
	}

	static int checkStyle(int style) {
		style = checkBits(style, SWT.PUSH, SWT.ARROW, SWT.CHECK, SWT.RADIO,
				SWT.TOGGLE, COMMAND_LINK ? SWT.COMMAND : 0);
		if ((style & (SWT.PUSH | SWT.TOGGLE)) != 0) {
			return checkBits(style, SWT.CENTER, SWT.LEFT, SWT.RIGHT, 0, 0, 0);
		}
		if ((style & (SWT.CHECK | SWT.RADIO)) != 0) {
			return checkBits(style, SWT.LEFT, SWT.RIGHT, SWT.CENTER, 0, 0, 0);
		}
		if ((style & SWT.ARROW) != 0) {
			style |= SWT.NO_FOCUS;
			return checkBits(style, SWT.UP, SWT.DOWN, SWT.LEFT, SWT.RIGHT, 0,
					0);
		}
		return style;
	}

	void click() {
		handleSelection();
	}

	/**
	 * Returns a value which describes the position of the text or image in the
	 * receiver. The value will be one of <code>LEFT</code>, <code>RIGHT</code>
	 * or <code>CENTER</code> unless the receiver is an <code>ARROW</code>
	 * button, in which case, the alignment will indicate the direction of the
	 * arrow (one of <code>LEFT</code>, <code>RIGHT</code>, <code>UP</code> or
	 * <code>DOWN</code>).
	 *
	 * @return the alignment
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getAlignment() {
		checkWidget();
		if ((style & SWT.ARROW) != 0) {
			if ((style & SWT.UP) != 0) {
				return SWT.UP;
			}
			if ((style & SWT.DOWN) != 0) {
				return SWT.DOWN;
			}
			if ((style & SWT.LEFT) != 0) {
				return SWT.LEFT;
			}
			if ((style & SWT.RIGHT) != 0) {
				return SWT.RIGHT;
			}
			return SWT.UP;
		}
		if ((style & SWT.LEFT) != 0) {
			return SWT.LEFT;
		}
		if ((style & SWT.CENTER) != 0) {
			return SWT.CENTER;
		}
		if ((style & SWT.RIGHT) != 0) {
			return SWT.RIGHT;
		}
		return SWT.LEFT;
	}

	boolean getDefault() {
		if (!isPushButton() || !isEnabled() || isDisposed()) {
			return false;
		}

		return defaultButton;
	}

	/**
	 * Returns <code>true</code> if the receiver is grayed, and false otherwise.
	 * When the widget does not have the <code>CHECK</code> style, return false.
	 *
	 * @return the grayed state of the checkbox
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.4
	 */
	public boolean getGrayed() {
		checkWidget();
		if (!isCheckButton()) {
			return false;
		}
		return grayed;
	}

	/**
	 * Returns the receiver's image if it has one, or null if it does not.
	 *
	 * @return the receiver's image
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Image getImage() {
		checkWidget();
		return image;
	}

	/**
	 * Returns the widget message. When the widget is created with the style
	 * <code>SWT.COMMAND</code>, the message text is displayed to provide
	 * further information for the user.
	 *
	 * @return the widget message
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.3
	 */
	/* public */ String getMessage() {
		checkWidget();
		return message;
	}

	@Override
	String getNameText() {
		return getText();
	}

	/**
	 * Returns <code>true</code> if the receiver is selected, and false
	 * otherwise.
	 * <p>
	 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>, it
	 * is selected when it is checked. When it is of type <code>TOGGLE</code>,
	 * it is selected when it is pushed in. If the receiver is of any other
	 * type, this method returns false.
	 *
	 * @return the selection state
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getSelection() {
		checkWidget();
		if ((style & (SWT.CHECK | SWT.RADIO | SWT.TOGGLE)) == 0) {
			return false;
		}
		return isChecked();
	}

	/**
	 * Returns the receiver's text, which will be an empty string if it has
	 * never been set or if the receiver is an <code>ARROW</code> button.
	 *
	 * @return the receiver's text
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public String getText() {
		checkWidget();
		if ((style & SWT.ARROW) != 0) {
			return "";
		}
		return text;
	}

	@Override
	boolean isTabGroup() {
		if (isPushButton() || isCheckButton()) {
			return true;
		}
		boolean b = super.isTabGroup();
		return b;
	}

//	@Override
//	boolean setTabItemFocus() {
//
//		if (isRadioButton()) {
//
//			for (Button b : getRadioGroup()) {
//				// we only tab on this element, if there is no other radio
//				// button which is selected.
//				// in case of another selected radio button, this other radio
//				// button should be tabbed.
//				// But if the other checked radio button has focus, then the
//				// tabbing should not be blocked.
//				if (!b.hasFocus() && b.isChecked() && b != this) {
//					return false;
//				}
//
//			}
//
//		}
//
//		boolean b = super.setTabItemFocus();
//		return b;
//	}
	//
	@Override
	boolean isTabItem() {
		boolean b = super.isTabItem();
		return b;
	}

	@Override
	boolean traverseItem(boolean next) {
		boolean b = super.traverseItem(next);
		// if the next item is selected, a radio button loses the check.
		if (b && isRadioButton()) {
			setSelection(false);
		}
		redraw();
		return b;
	}

	@Override
	boolean traverseGroup(boolean next) {
		boolean b = super.traverseGroup(next);
		return b;
	}

	// menmonicHis(char ch) does not exist on mac. It seems on mac there is no
	// mnemonic...
	boolean mnemonicISHit(char ch) {
		if ((style & SWT.RADIO) == 0 && !setFocus()) {
			return false;
		}
		click();
		return true;
	}

	// mnemonicMatch(char key) does not exist on mac...
	boolean mnemonicHasMatch(char key) {
		// char mnemonic = findMnemonic (getText ());
		// if (mnemonic == '\0') return false;
		// return Character.toUpperCase (key) == Character.toUpperCase
		// (mnemonic);
		System.out.println("WARN: Not implemented yet: "
				+ new Throwable().getStackTrace()[0]);
		return false;
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		renderer.invalidateImage();
		text = null;
		image = null;
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the control is selected by the user.
	 *
	 * @param listener
	 *            the listener which should no longer be notified
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
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
	 * Controls how text, images and arrows will be displayed in the receiver.
	 * The argument should be one of <code>LEFT</code>, <code>RIGHT</code> or
	 * <code>CENTER</code> unless the receiver is an <code>ARROW</code> button,
	 * in which case, the argument indicates the direction of the arrow (one of
	 * <code>LEFT</code>, <code>RIGHT</code>, <code>UP</code> or
	 * <code>DOWN</code>).
	 *
	 * @param alignment
	 *            the new alignment
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setAlignment(int alignment) {
		checkWidget();

		style &= ~(SWT.UP | SWT.DOWN | SWT.LEFT | SWT.CENTER | SWT.RIGHT);
		style |= alignment
				& (SWT.UP | SWT.DOWN | SWT.LEFT | SWT.CENTER | SWT.RIGHT);

		redraw();

	}

	/**
	 * Sets the button's background color to the color specified by the
	 * argument, or to the default system color for the control if the argument
	 * is null.
	 * <p>
	 * Note: This is custom paint operation and only affects {@link SWT#PUSH}
	 * and {@link SWT#TOGGLE} buttons. If the native button has a 3D look an
	 * feel (e.g. Windows 7), this method will cause the button to look FLAT
	 * irrespective of the state of the {@link SWT#FLAT} style. For
	 * {@link SWT#CHECK} and {@link SWT#RADIO} buttons, this method delegates to
	 * {@link Control#setBackground(Color)}.
	 * </p>
	 *
	 * @param color
	 *            the new color (or null)
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the argument has been
	 *                disposed</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	@Override
	public void setBackground(Color color) {
		// This method only exists in order to provide custom documentation
		super.setBackground(color);
	}

	void setDefault(boolean value) {
		if ((style & SWT.PUSH) == 0) {
			return;
		}
		defaultButton = value;
	}

	@Override
	public boolean setFocus() {
		checkWidget();

		/*
		 * If the button should get focus, use forceFocus().
		 * Here it should be prevented, that focus changes the selection of radion buttons.
		 */
		/*
		 * Feature in Windows. When a radio button gets focus, it selects the
		 * button in WM_SETFOCUS. The fix is to not assign focus to an
		 * unselected radio button.
		 */
		if (isRadioButton() && !isChecked()) {
			return false;
		}
		boolean b = super.setFocus();
		return b;
	}

	@Override
	public boolean forceFocus() {
		boolean b = super.forceFocus();
		if (b && isRadioButton()) { // if a radio button gets focus, then all
									// other radio buttons of the group lose the
									// selection
			selectRadio(/* withFocus = */false);
		}
		return b;
	}

	/**
	 * Sets the receiver's image to the argument, which may be <code>null</code>
	 * indicating that no image should be displayed.
	 * <p>
	 * Note that a Button can display an image and text simultaneously.
	 * </p>
	 *
	 * @param image
	 *            the image to display on the receiver (may be
	 *            <code>null</code>)
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the image has been
	 *                disposed</li>
	 *                </ul>
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
		if (image != null && image.isDisposed()) {
			error(SWT.ERROR_INVALID_ARGUMENT);
		}
		if ((style & SWT.ARROW) != 0) {
			return;
		}
		this.image = image;
		renderer.invalidateImage();
		redraw();
	}

	/**
	 * Sets the grayed state of the receiver. This state change only applies if
	 * the control was created with the SWT.CHECK style.
	 *
	 * @param grayed
	 *            the new grayed state
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.4
	 */
	public void setGrayed(boolean grayed) {
		checkWidget();
		if ((style & SWT.CHECK) == 0) {
			return;
		}
		this.grayed = grayed;
	}

	/**
	 * Sets the widget message. When the widget is created with the style
	 * <code>SWT.COMMAND</code>, the message text is displayed to provide
	 * further information for the user.
	 *
	 * @param message
	 *            the new message
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 *
	 * @since 3.3
	 */
	/* public */ void setMessage(String message) {
		// TODO not yet implemented, never heard of this...
		checkWidget();
		if (message == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		this.message = message;
		if ((style & SWT.COMMAND) != 0) {
			int length = message.length();
			char[] chars = new char[length + 1];
			message.getChars(0, length, chars, 0);
			// OS.SendMessage (handle, OS.BCM_SETNOTE, 0, chars);
			System.out.println("WARN: Not implemented yet: "
					+ new Throwable().getStackTrace()[0]);
		}
	}

	/**
	 * Sets the selection state of the receiver, if it is of type
	 * <code>CHECK</code>, <code>RADIO</code>, or <code>TOGGLE</code>.
	 *
	 * <p>
	 * When the receiver is of type <code>CHECK</code> or <code>RADIO</code>, it
	 * is selected when it is checked. When it is of type <code>TOGGLE</code>,
	 * it is selected when it is pushed in.
	 *
	 * @param selected
	 *            the new selection state
	 *
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(boolean selected) {
		checkWidget();
		this.checked = selected;
		redraw();
	}

	@Override
	boolean setRadioSelection(boolean value) {
		if (!isRadioButton()) {
			return false;
		}
		if (getSelection() != value) {
			setSelection(value);
			sendSelectionEvent(SWT.Selection);
		}
		return true;
	}

	void selectRadio() {
		for (Button b : getRadioGroup()) {
			if (b != this) {
				b.setSelection(false);
				b.redraw();
			}
		}

		setFocus();
		setRadioSelection(true);
		redraw();
	}

	void selectRadio(boolean withFocus) {
		for (Button b : getRadioGroup()) {
			if (b != this) {
				b.setSelection(false);
				b.redraw();
			}
		}

		if (withFocus) {
			setFocus();
		}
		setRadioSelection(true);
		redraw();
	}

	/**
	 * Sets the receiver's text.
	 * <p>
	 * This method sets the button label. The label may include the mnemonic
	 * character but must not contain line delimiters.
	 * </p>
	 * <p>
	 * Mnemonics are indicated by an '&amp;' that causes the next character to
	 * be the mnemonic. When the user presses a key sequence that matches the
	 * mnemonic, a selection event occurs. On most platforms, the mnemonic
	 * appears underlined but may be emphasized in a platform specific manner.
	 * The mnemonic indicator character '&amp;' can be escaped by doubling it in
	 * the string, causing a single '&amp;' to be displayed.
	 * </p>
	 * <p>
	 * Note that a Button can display an image and text simultaneously on
	 * Windows (starting with XP), GTK+ and OSX. On other platforms, a Button
	 * that has an image and text set into it will display the image or text
	 * that was set most recently.
	 * </p>
	 * <p>
	 * Also note, if control characters like '\n', '\t' etc. are used in the
	 * string, then the behavior is platform dependent.
	 * </p>
	 *
	 * @param string
	 *            the new text
	 *
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the text is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setText(String string) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if ((style & SWT.ARROW) != 0) {
			return;
		}
		text = string;
		redraw();
	}

	private boolean isChecked() {
		return checked;
	}
}
