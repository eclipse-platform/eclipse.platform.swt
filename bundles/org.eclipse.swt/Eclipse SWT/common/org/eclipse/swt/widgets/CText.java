/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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
 *******************************************************************************/
package org.eclipse.swt.widgets;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;

/**
 * Instances of this class are selectable user interface objects that allow the
 * user to enter and modify text. Text controls can be either single or
 * multi-line. When a text control is created with a border, the operating
 * system includes a platform specific inset around the contents of the control.
 * When created without a border, an effort is made to remove the inset such
 * that the preferred size of the control is the same size as the contents.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CENTER, ICON_CANCEL, ICON_SEARCH, LEFT, MULTI, PASSWORD, SEARCH, SINGLE,
 * RIGHT, READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Verify, OrientationChange</dd>
 * </dl>
 * <p>
 * Note: Only one of the styles MULTI and SINGLE may be specified, and only one
 * of the styles LEFT, CENTER, and RIGHT may be specified.
 * </p>
 * <p>
 * Note: The styles ICON_CANCEL and ICON_SEARCH are hints used in combination
 * with SEARCH. When the platform supports the hint, the text control shows
 * these icons. When an icon is selected, a default selection event is sent with
 * the detail field set to one of ICON_CANCEL or ICON_SEARCH. Normally,
 * application code does not need to check the detail. In the case of
 * ICON_CANCEL, the text is cleared before the default selection event is sent
 * causing the application to search for an empty string.
 * </p>
 * <p>
 * Note: Some text actions such as Undo are not natively supported on all
 * platforms.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 *
 * @see <a href="http://www.eclipse.org/swt/snippets/#text">Text snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class CText extends StyledText {
	/**
	 * The maximum number of characters that can be entered into a text widget.
	 * <p>
	 * Note that this value is platform dependent, based upon the native widget
	 * implementation.
	 * </p>
	 */
	public static final int LIMIT;

	/**
	 * The delimiter used by multi-line text widgets. When text is queried and from
	 * the widget, it will be delimited using this delimiter.
	 */
	public static final String DELIMITER;
	static final char PASSWORD = '\u2022';

	private char echoCharacter;
	char[] hiddenText;
	int textLimit = LIMIT;

	/*
	 * These values can be different on different platforms. Therefore they are not
	 * initialized in the declaration to stop the compiler from inlining.
	 */
	static {
		LIMIT = 0x7FFFFFFF;
		DELIMITER = "\r";
	}

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
	 * @see SWT#SINGLE
	 * @see SWT#MULTI
	 * @see SWT#READ_ONLY
	 * @see SWT#WRAP
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#CENTER
	 * @see SWT#PASSWORD
	 * @see SWT#SEARCH
	 * @see SWT#ICON_SEARCH
	 * @see SWT#ICON_CANCEL
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public CText(Composite parent, int style) {
		super(parent, style);
//		if ((style & SWT.SEARCH) != 0) {
//			/*
//			 * Ensure that SWT.ICON_CANCEL and ICON_SEARCH are set. NOTE: ICON_CANCEL has
//			 * the same value as H_SCROLL and ICON_SEARCH has the same value as V_SCROLL so
//			 * it is necessary to first clear these bits to avoid a scroll bar and then
//			 * reset the bit using the original style supplied by the programmer.
//			 */
//			NSSearchFieldCell cell = new NSSearchFieldCell(((NSSearchField) view).cell());
//			if ((style & SWT.ICON_CANCEL) != 0) {
//				this.style |= SWT.ICON_CANCEL;
//				NSButtonCell cancelCell = cell.cancelButtonCell();
//				targetCancel = cancelCell.target();
//				actionCancel = cancelCell.action();
//				cancelCell.setTarget(view);
//				cancelCell.setAction(OS.sel_sendCancelSelection);
//			} else {
//				cell.setCancelButtonCell(null);
//			}
//			if ((style & SWT.ICON_SEARCH) != 0) {
//				this.style |= SWT.ICON_SEARCH;
//				NSButtonCell searchCell = cell.searchButtonCell();
//				targetSearch = searchCell.target();
//				actionSearch = searchCell.action();
//				searchCell.setTarget(view);
//				searchCell.setAction(OS.sel_sendSearchSelection);
//			} else {
//				cell.setSearchButtonCell(null);
//			}
//		}
	}

	/**
	 * Clears the selection.
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void clearSelection() {
		checkWidget();
		Point selection = getSelection();
		setSelection(selection.x);
	}

	/**
	 * Returns the character position of the caret.
	 * <p>
	 * Indexing is zero based.
	 * </p>
	 *
	 * @return the position of the caret
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public int getCaretPosition() {
		return getCaretOffset();
	}

	/**
	 * Returns a point describing the location of the caret relative to the
	 * receiver.
	 *
	 * @return a point, the location of the caret
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public Point getCaretLocation() {
		return getCarets()[0].getLocation();
	}

	/**
	 * Returns the widget message. The message text is displayed as a hint for the
	 * user, indicating the purpose of the field.
	 * <p>
	 * Typically this is used in conjunction with <code>SWT.SEARCH</code>.
	 * </p>
	 *
	 * @return the widget message
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @since 3.3
	 */
	public String getMessage() {
		return getText();
	}

	/**
	 * Sets the widget message. The message text is displayed as a hint for the
	 * user, indicating the purpose of the field.
	 * <p>
	 * Typically this is used in conjunction with <code>SWT.SEARCH</code>.
	 * </p>
	 *
	 * @param message the new message
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the message
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
	 * @since 3.3
	 */
	public void setMessage(String message) {
		setText(message);
	}

	/**
	 * Sets the echo character.
	 * <p>
	 * The echo character is the character that is displayed when the user enters
	 * text or the text is changed by the programmer. Setting the echo character to
	 * '\0' clears the echo character and redraws the original text. If for any
	 * reason the echo character is invalid, or if the platform does not allow
	 * modification of the echo character, the default echo character for the
	 * platform is used.
	 * </p>
	 *
	 * @param echo the new echo character
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 */
	public void setEchoChar(char echo) {
		checkWidget();
		if ((style & SWT.MULTI) != 0)
			return;
		if ((style & SWT.PASSWORD) == 0) {
			Point selection = getSelection();
			char[] text = getTextChars();
			echoCharacter = echo;
			setEditText(text);
			setSelection(selection);
		}
		echoCharacter = echo;
	}

	char[] getEditText() {
		if (hiddenText != null) {
			char[] text = new char[hiddenText.length];
			System.arraycopy(hiddenText, 0, text, 0, text.length);
			return text;
		}
		return getTextChars();
	}

	void setEditText(String string) {
		char[] text = new char[string.length()];
		string.getChars(0, text.length, text, 0);
		setEditText(text);
	}

	void setEditText(char[] text) {
		char[] buffer;
		int length = Math.min(text.length, textLimit);
		if ((style & SWT.PASSWORD) == 0 && echoCharacter != '\0') {
			hiddenText = new char[length];
			buffer = new char[length];
			for (int i = 0; i < length; i++) {
				hiddenText[i] = text[i];
				buffer[i] = echoCharacter;
			}
		} else {
			hiddenText = null;
			buffer = text;
		}
		getContent().setText(new String(buffer));
		setSelection(new Point(0, 0));
	}

	/**
	 * Returns the widget's text as a character array.
	 * <p>
	 * The text for a text widget is the characters in the widget, or a zero-length
	 * array if this has never been set.
	 * </p>
	 * <p>
	 * Note: Use this API to prevent the text from being written into a String
	 * object whose lifecycle is outside of your control. This can help protect the
	 * text, for example, when the widget is used as a password field. However, the
	 * text can't be protected if an {@link SWT#Segments} or {@link SWT#Verify}
	 * listener has been added to the widget.
	 * </p>
	 *
	 * @return a character array that contains the widget's text
	 *
	 * @exception SWTException
	 *                         <ul>
	 *                         <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                         disposed</li>
	 *                         <li>ERROR_THREAD_INVALID_ACCESS - if not called from
	 *                         the thread that created the receiver</li>
	 *                         </ul>
	 *
	 * @see #setTextChars(char[])
	 *
	 * @since 3.7
	 */
	public char[] getTextChars() {
		return getText().toCharArray();
	}

	/**
	 * Sets the contents of the receiver to the characters in the array. If the
	 * receiver has style <code>SWT.SINGLE</code> and the argument contains multiple
	 * lines of text then the result of this operation is undefined and may vary
	 * between platforms.
	 * <p>
	 * Note: Use this API to prevent the text from being written into a String
	 * object whose lifecycle is outside of your control. This can help protect the
	 * text, for example, when the widget is used as a password field. However, the
	 * text can't be protected if an {@link SWT#Segments} or {@link SWT#Verify}
	 * listener has been added to the widget.
	 * </p>
	 *
	 * @param text a character array that contains the new text
	 *
	 * @exception IllegalArgumentException
	 *                                     <ul>
	 *                                     <li>ERROR_NULL_ARGUMENT - if the array is
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
	 *
	 * @see #getTextChars()
	 *
	 * @since 3.7
	 */
	public void setTextChars(char[] text) {
		checkWidget();
		if (text == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		if (hooks(SWT.Verify) || filters(SWT.Verify)) {
			String string = verifyText(new String(text), 0, getCharCount());
			if (string == null)
				return;
			text = new char[string.length()];
			string.getChars(0, text.length, text, 0);
		}
		getContent().setText(new String(text));
		sendEvent(SWT.Modify);
	}

	String verifyText(String string, int start, int end) {
		Event event = new Event();
		event.text = string;
		event.start = start;
		event.end = end;
		/*
		 * It is possible (but unlikely), that application code could have disposed the
		 * widget in the verify event. If this happens, answer null to cancel the
		 * operation.
		 */
		sendEvent(SWT.Verify, event);
		if (!event.doit || isDisposed())
			return null;
		return event.text;
	}

	@Override
	public Color getForeground() {
		return new Color(0, 0, 255);
	}

}
