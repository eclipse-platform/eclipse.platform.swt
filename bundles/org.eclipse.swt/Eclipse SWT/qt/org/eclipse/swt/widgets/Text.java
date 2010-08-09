/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.widgets;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.LayoutDirection;
import com.trolltech.qt.gui.QAbstractScrollArea;
import com.trolltech.qt.gui.QContentsMargins;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QScrollBar;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QTextBlock;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QTextDocument;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QTextLayout;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QFrame.Shape;
import com.trolltech.qt.gui.QLineEdit.EchoMode;
import com.trolltech.qt.gui.QTextCursor.MoveMode;
import com.trolltech.qt.gui.QTextCursor.MoveOperation;
import com.trolltech.qt.gui.QTextEdit.LineWrapMode;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.qt.QtSWTConverter;

/**
 * Instances of this class are selectable user interface objects that allow the
 * user to enter and modify text. Text controls can be either single or
 * multi-line. When a text control is created with a border, the operating
 * system includes a platform specific inset around the contents of the control.
 * When created without a border, an effort is made to remove the inset such
 * that the preferred size of the control is the same size as the contents.
 * <p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>CENTER, ICON_CANCEL, ICON_SEARCH, LEFT, MULTI, PASSWORD, SEARCH, SINGLE,
 * RIGHT, READ_ONLY, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>DefaultSelection, Modify, Verify</dd>
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
public class Text extends Scrollable {
	private static final int minimumLineSpacing = 14;
	private static final int verticalMargin = 1;
	private static final int horizontalMargin = 2;

	int tabs = 8;
	int oldStart, oldEnd;
	boolean doubleClick, ignoreModify, ignoreVerify, ignoreCharacter;
	private int textLimit;
	/**
	 * The maximum number of characters that can be entered into a text widget.
	 * <p>
	 * Note that this value is platform dependent, based upon the native widget
	 * implementation.
	 * </p>
	 */
	public static final int LIMIT;

	/**
	 * The delimiter used by multi-line text widgets. When text is queried and
	 * from the widget, it will be delimited using this delimiter.
	 */
	public static final String DELIMITER;

	/*
	 * These values can be different on different platforms. Therefore they are
	 * not initialized in the declaration to stop the compiler from inlining.
	 */
	static {
		// LIMIT is the default maxlength of the QLineEdit
		LIMIT = new QLineEdit().maxLength();
		DELIMITER = "\n";//$NON-NLS-1$
	}

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
	 * @see SWT#SINGLE
	 * @see SWT#MULTI
	 * @see SWT#READ_ONLY
	 * @see SWT#WRAP
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Text(Composite parent, int style) {
		super(parent, checkStyle(style));
		setTabStops(tabs);
	}

	boolean isSingleLineEdit() {
		return (style & SWT.MULTI) == 0;
	}

	@Override
	protected QWidget createQWidget(int style) {
		doubleClick = true;
		if (isSingleLineEdit()) {
			return createQLineEdit(style);
		}
		return createQTextEdit(style);
	}

	QLineEdit createQLineEdit(int style) {
		QLineEdit lineEdit = new QLineEdit();
		if ((style & SWT.READ_ONLY) != 0) {
			lineEdit.setReadOnly(true);
		}
		if ((style & SWT.PASSWORD) != 0) {
			lineEdit.setEchoMode(EchoMode.Password);
		}
		lineEdit.setSizePolicy(QSizePolicy.Policy.Minimum, QSizePolicy.Policy.Minimum);
		lineEdit.setContentsMargins(0, 0, 0, 0);

		connectQLineEditSignals(lineEdit);

		lineEdit.setFrame(false);

		return lineEdit;
	}

	QWidget createQTextEdit(int style) {
		QTextEdit textEdit = new MyQTextEdit();
		if ((style & SWT.READ_ONLY) != 0) {
			textEdit.setReadOnly(true);
		}
		if ((style & SWT.WRAP) != 0) {
			textEdit.setLineWrapMode(LineWrapMode.WidgetWidth);
		} else {
			textEdit.setLineWrapMode(LineWrapMode.NoWrap);
		}
		connectQTextEditSignals(textEdit);

		textEdit.setFrameShape(Shape.NoFrame);
		textEdit.setLineWidth(0);

		return textEdit;
	}

	@Override
	protected void checkAndUpdateBorder(QWidget control) {
		if (isSingleLineEdit()) {
			if ((style & SWT.BORDER) != 0) {
				getQLineEdit().setFrame(true);
			}
		} else {
			super.checkAndUpdateBorder(control);
		}
	}

	protected void connectQLineEditSignals(QLineEdit lineEdit) {
		lineEdit.textChanged.connect(this, "textChangeEvent(String)"); //$NON-NLS-1$
	}

	protected void connectQTextEditSignals(QTextEdit textEdit) {
		textEdit.textChanged.connect(this, "textChangeEvent()"); //$NON-NLS-1$
	}

	protected void textChangeEvent() {
		textChangeEvent(null);
	}

	protected void textChangeEvent(String text) {
		Event event = new Event();
		event.data = text;
		sendEvent(SWT.Modify, event);
	}

	@Override
	void registerQWidget() {
		super.registerQWidget();
		if (!isSingleLineEdit()) {
			display.addControl(getQTextEdit().viewport(), this);
		}
	}

	@Override
	void deregisterQWidget() {
		if (!isSingleLineEdit()) {
			display.removeControl(getQTextEdit().viewport());
		}
		super.deregisterQWidget();
	}

	private QLineEdit getQLineEdit() {
		return (QLineEdit) getQWidget();
	}

	private QTextEdit getQTextEdit() {
		return (QTextEdit) getQWidget();
	}

	@Override
	QAbstractScrollArea getQScrollArea() {
		if (!isSingleLineEdit()) {
			return getQTextEdit();
		}
		return null;
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the receiver's text is modified, by sending it one of the messages
	 * defined in the <code>ModifyListener</code> interface.
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
	 * @see ModifyListener
	 * @see #removeModifyListener
	 */
	public void addModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Modify, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control is selected by the user, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is not called for texts.
	 * <code>widgetDefaultSelected</code> is typically called when ENTER is
	 * pressed in a single-line text, or when ENTER is pressed in a search text.
	 * If the receiver has the <code>SWT.SEARCH | SWT.CANCEL</code> style and
	 * the user cancels the search, the event object detail field contains the
	 * value <code>SWT.CANCEL</code>.
	 * </p>
	 * 
	 * @param listener
	 *            the listener which should be notified when the control is
	 *            selected by the user
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
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Selection, typedListener);
		addListener(SWT.DefaultSelection, typedListener);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the receiver's text is verified, by sending it one of the messages
	 * defined in the <code>VerifyListener</code> interface.
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
	 * @see VerifyListener
	 * @see #removeVerifyListener
	 */
	public void addVerifyListener(VerifyListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Verify, typedListener);
	}

	/**
	 * Appends a string.
	 * <p>
	 * The new text is appended to the text at the end of the widget.
	 * </p>
	 * 
	 * @param string
	 *            the string to be appended
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
	 */
	public void append(String string) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		string = Display.withCrLf(string);
		if (isSingleLineEdit()) {
			getQLineEdit().setText(getQLineEdit().text() + string);
		} else {
			getQTextEdit().append(string);
		}
	}

	static int checkStyle(int style) {
		if ((style & SWT.SEARCH) != 0) {
			style |= SWT.SINGLE | SWT.BORDER;
			style &= ~SWT.PASSWORD;
			style &= ~SWT.ICON_CANCEL;
			/*
			 * NOTE: ICON_CANCEL has the same value as H_SCROLL and ICON_SEARCH
			 * has the same value as V_SCROLL so they are cleared because
			 * SWT.SINGLE is set.
			 */
		}
		if ((style & SWT.SINGLE) != 0 && (style & SWT.MULTI) != 0) {
			style &= ~SWT.MULTI;
		}
		style = checkBits(style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
		if ((style & SWT.SINGLE) != 0) {
			style &= ~(SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		}
		if ((style & SWT.WRAP) != 0) {
			style |= SWT.MULTI;
			style &= ~SWT.H_SCROLL;
		}
		if ((style & SWT.MULTI) != 0) {
			style &= ~SWT.PASSWORD;
		}
		if ((style & (SWT.SINGLE | SWT.MULTI)) != 0) {
			return style;
		}
		if ((style & (SWT.H_SCROLL | SWT.V_SCROLL)) != 0) {
			return style | SWT.MULTI;
		}
		return style | SWT.SINGLE;
	}

	/**
	 * Clears the selection.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void clearSelection() {
		checkWidget();
		if (isSingleLineEdit()) {
			getQLineEdit().setCursorPosition(getQLineEdit().cursorPosition());
		} else {
			getQTextEdit().textCursor().clearSelection();
		}
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();

		if (wHint != SWT.DEFAULT && wHint < 0) {
			wHint = SWT.DEFAULT;
		}
		if (hHint != SWT.DEFAULT && hHint < 0) {
			hHint = SWT.DEFAULT;
		}

		Point preferredSize;
		if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
			preferredSize = new Point(wHint, hHint);
		} else if (isSingleLineEdit()) {
			if (wHint == SWT.DEFAULT && hHint == SWT.DEFAULT) {
				preferredSize = getPreferredSingleLineClientAreaSize();
			} else if (hHint == SWT.DEFAULT) {
				preferredSize = new Point(wHint, getPreferredSingleLineClientAreaSize().y);
			} else {
				preferredSize = new Point(getPreferredSingleLineClientAreaSize().x, hHint);
			}
		} else {
			if (wHint == SWT.DEFAULT && hHint == SWT.DEFAULT) {
				preferredSize = getPreferredClientAreaSize(-1);
			} else if (hHint == SWT.DEFAULT) {
				preferredSize = new Point(wHint, getPreferredClientAreaSize(wHint).y);
			} else {
				preferredSize = new Point(getPreferredClientAreaSize(hHint).y, hHint);
			}
		}

		Rectangle trim = computeTrim(0, 0, preferredSize.x, preferredSize.y);

		return new Point(trim.width, trim.height);
	}

	private Point getPreferredSingleLineClientAreaSize() {
		QFontMetrics fm = new QFontMetrics(getQLineEdit().font());
		QContentsMargins margins = getQLineEdit().getContentsMargins();
		int left = margins.left;
		int top = margins.top;
		int right = margins.right;
		int bottom = margins.bottom;
		int h = Math.max(fm.lineSpacing(), minimumLineSpacing) + 2 * verticalMargin + top + bottom;
		int w = fm.width(getQLineEdit().text()) + 2 * horizontalMargin + left + right;
		Point size = new Point(w, h);
		if (size == null) {
			return new Point(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		}
		if (size.x < 0) {
			size.x = DEFAULT_WIDTH;
		}
		if (size.y < 0) {
			size.y = DEFAULT_HEIGHT;
		}
		return size;
	}

	private Point getPreferredClientAreaSize(int wHint) {
		QTextDocument doc = getQTextEdit().document();
		Point size = null;
		if (doc != null) {
			double oldTextWidth = doc.textWidth();
			if (wHint >= 0) {
				doc.setTextWidth(wHint);
			} else {
				doc.adjustSize();
			}
			QSize preferredSize = doc.size().toSize();
			doc.setTextWidth(oldTextWidth);
			size = new Point(preferredSize.width(), preferredSize.height());
		}

		if (size == null) {
			return new Point(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		}
		if (size.x < 0) {
			size.x = DEFAULT_WIDTH;
		}
		if (size.y < 0) {
			size.y = DEFAULT_HEIGHT;
		}
		return size;
	}

	@Override
	public Rectangle computeTrim(int x, int y, int width, int height) {
		checkWidget();
		if (isSingleLineEdit()) {
			if ((style & SWT.BORDER) != 0) {
				int border = 0;
				QStyle style = getQWidget().style();
				if (style != null) {
					border = style.pixelMetric(QStyle.PixelMetric.PM_DefaultFrameWidth);
				}
				x -= border;
				y -= border;
				width += 2 * border;
				height += 2 * border;
			}
			return new Rectangle(x, y, width, height);
		}
		return super.computeTrim(x, y, width, height);
	}

	/**
	 * Copies the selected text.
	 * <p>
	 * The current selection is copied to the clipboard.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void copy() {
		checkWidget();
		if (isSingleLineEdit()) {
			getQLineEdit().copy();
		} else {
			getQTextEdit().copy();
		}
	}

	/**
	 * Cuts the selected text.
	 * <p>
	 * The current selection is first copied to the clipboard and then deleted
	 * from the widget.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void cut() {
		checkWidget();
		if (isSingleLineEdit()) {
			getQLineEdit().cut();
		} else {
			getQTextEdit().cut();
		}
	}

	/**
	 * Returns the line number of the caret.
	 * <p>
	 * The line number of the caret is returned.
	 * </p>
	 * 
	 * @return the line number
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getCaretLineNumber() {
		checkWidget();
		if (!isSingleLineEdit()) {
			int lineNumber = 0;
			QTextCursor textCursor = getQTextEdit().textCursor();
			QTextBlock textBlock = textCursor.block();
			QTextLayout textLayout = textBlock.layout();
			if (textLayout != null) {
				lineNumber = textLayout.lineForTextPosition(textCursor.position() - textBlock.position()).lineNumber();
			}
			lineNumber += getNumberOfPrecedingTextLines(textBlock);
			return lineNumber;
		}
		return 0;
	}

	private int getNumberOfPrecedingTextLines(QTextBlock textBlock) {
		int lineCount = 0;
		while (textBlock.isValid()) {
			QTextLayout textLayout = textBlock.layout();
			if (textLayout != null) {
				int lines = textLayout.lineCount();
				lineCount += lines > 0 ? lines : 1;
			}
			textBlock = textBlock.previous();
		}
		return lineCount;
	}

	/**
	 * Returns a point describing the receiver's location relative to its parent
	 * (or its display if its parent is null).
	 * <p>
	 * The location of the caret is returned.
	 * </p>
	 * 
	 * @return a point, the location of the caret
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Point getCaretLocation() {
		checkWidget();
		if (isSingleLineEdit()) {
			return new Point(0, 0);
		}
		return QtSWTConverter.convertPosition(getQTextEdit().cursorRect());
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
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getCaretPosition() {
		checkWidget();
		if (isSingleLineEdit()) {
			return getQLineEdit().cursorPosition();
		}
		return getQTextEdit().textCursor().position();
	}

	/**
	 * Returns the number of characters.
	 * 
	 * @return number of characters in the widget
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getCharCount() {
		checkWidget();
		if (isSingleLineEdit()) {
			return getText().length();

		}
		QTextCursor cursor = getQTextEdit().textCursor();
		int oldPosition = cursor.position();
		cursor.movePosition(MoveOperation.End);
		int count = cursor.position();
		cursor.setPosition(oldPosition);
		return count;
	}

	/**
	 * Returns the double click enabled flag.
	 * <p>
	 * The double click flag enables or disables the default action of the text
	 * widget when the user double clicks.
	 * </p>
	 * 
	 * @return whether or not double click is enabled
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getDoubleClickEnabled() {
		checkWidget();
		return doubleClick;
	}

	/**
	 * Returns the echo character.
	 * <p>
	 * The echo character is the character that is displayed when the user
	 * enters text or the text is changed by the programmer.
	 * </p>
	 * 
	 * @return the echo character
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #setEchoChar
	 */
	public char getEchoChar() {
		checkWidget();
		if (isSingleLineEdit()) {
			if ((style & SWT.PASSWORD) != 0) {
				return '*';
			}
		}
		return '\0';
	}

	/**
	 * Returns the editable state.
	 * 
	 * @return whether or not the receiver is editable
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public boolean getEditable() {
		checkWidget();
		if (isSingleLineEdit()) {
			return !getQLineEdit().isReadOnly();
		}
		return !getQTextEdit().isReadOnly();
	}

	/**
	 * Returns the number of lines.
	 * 
	 * @return the number of lines in the widget
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getLineCount() {
		checkWidget();
		if (isSingleLineEdit()) {
			return 1;
		}
		QTextDocument textDocument = getQTextEdit().document();
		if (textDocument != null) {
			return getNumberOfPrecedingTextLines(textDocument.end());
		}
		return 0;
	}

	/**
	 * Returns the line delimiter.
	 * 
	 * @return a string that is the line delimiter
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #DELIMITER
	 */
	public String getLineDelimiter() {
		checkWidget();
		return DELIMITER;
	}

	/**
	 * Returns the height of a line.
	 * 
	 * @return the height of a row of text
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getLineHeight() {
		checkWidget();
		if (isSingleLineEdit()) {
			return getQLineEdit().fontMetrics().lineSpacing();
		}
		QTextLayout textLayout = getQTextEdit().textCursor().block().layout();
		if (textLayout != null) {
			return (int) Math.round(textLayout.lineAt(0).height());
		}
		return 0;
	}

	/**
	 * Returns the orientation of the receiver, which will be one of the
	 * constants <code>SWT.LEFT_TO_RIGHT</code> or
	 * <code>SWT.RIGHT_TO_LEFT</code>.
	 * 
	 * @return the orientation style
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 2.1.2
	 */
	public int getOrientation() {
		checkWidget();
		return style & (SWT.LEFT_TO_RIGHT | SWT.RIGHT_TO_LEFT);
	}

	/**
	 * Returns the widget message. The message text is displayed as a hint for
	 * the user, indicating the purpose of the field.
	 * <p>
	 * Typically this is used in conjunction with <code>SWT.SEARCH</code>.
	 * </p>
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
	public String getMessage() {
		checkWidget();
		return getQWidget().whatsThis();
	}

	/**
	 * Returns the character position at the given point in the receiver or -1
	 * if no such position exists. The point is in the coordinate system of the
	 * receiver.
	 * <p>
	 * Indexing is zero based.
	 * </p>
	 * 
	 * @return the position of the caret
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
	// TODO - Javadoc
	// /* public */int getPosition( Point point ) {
	// checkWidget();
	// if ( point == null )
	// error( SWT.ERROR_NULL_ARGUMENT );
	// int /* long */lParam = OS.MAKELPARAM( point.x, point.y );
	// int position = OS.LOWORD( OS.SendMessage( handle, OS.EM_CHARFROMPOS, 0,
	// lParam ) );
	// if ( !OS.IsUnicode && OS.IsDBLocale )
	// position = mbcsToWcsPos( position );
	// return position;
	// }

	/**
	 * Returns a <code>Point</code> whose x coordinate is the character position
	 * representing the start of the selected text, and whose y coordinate is
	 * the character position representing the end of the selection. An "empty"
	 * selection is indicated by the x and y coordinates having the same value.
	 * <p>
	 * Indexing is zero based. The range of a selection is from 0..N where N is
	 * the number of characters in the widget.
	 * </p>
	 * 
	 * @return a point representing the selection start and end
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public Point getSelection() {
		checkWidget();
		if (isSingleLineEdit()) {
			int selectionStart = getQLineEdit().selectionStart();
			if (selectionStart == -1) {
				int cursorPos = getQLineEdit().cursorPosition();
				return new Point(cursorPos, cursorPos);
			}
			return new Point(selectionStart, selectionStart + getQLineEdit().selectedText().length());
		}
		int start = getQTextEdit().textCursor().selectionStart();
		int end = getQTextEdit().textCursor().selectionStart();
		return new Point(Math.min(start, end), Math.max(start, end));
	}

	/**
	 * Returns the number of selected characters.
	 * 
	 * @return the number of selected characters.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getSelectionCount() {
		checkWidget();
		Point selection = getSelection();
		return Math.abs(selection.y - selection.x);
	}

	/**
	 * Gets the selected text, or an empty string if there is no current
	 * selection.
	 * 
	 * @return the selected text
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public String getSelectionText() {
		checkWidget();
		if (isSingleLineEdit()) {
			return getQLineEdit().selectedText();
		}
		return getQTextEdit().textCursor().selectedText();
	}

	/**
	 * Returns the number of tabs.
	 * <p>
	 * Tab stop spacing is specified in terms of the space (' ') character. The
	 * width of a single tab stop is the pixel width of the spaces.
	 * </p>
	 * 
	 * @return the number of tab characters
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getTabs() {
		checkWidget();
		return tabs;
	}

	/**
	 * Returns the widget text.
	 * <p>
	 * The text for a text widget is the characters in the widget, or an empty
	 * string if this has never been set.
	 * </p>
	 * 
	 * @return the widget text
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
		if (isSingleLineEdit()) {
			return getQLineEdit().text();
		}
		return getQTextEdit().toPlainText();
	}

	/**
	 * Returns a range of text. Returns an empty string if the start of the
	 * range is greater than the end.
	 * <p>
	 * Indexing is zero based. The range of a selection is from 0..N-1 where N
	 * is the number of characters in the widget.
	 * </p>
	 * 
	 * @param start
	 *            the start of the range
	 * @param end
	 *            the end of the range
	 * @return the range of text
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public String getText(int start, int end) {
		checkWidget();
		if (start > end || end < 0) {
			return "";//$NON-NLS-1$
		}

		String text = getText();
		if (text != null) {
			if (start >= text.length()) {
				text = "";//$NON-NLS-1$
			} else {
				start = Math.max(0, start);
				end = Math.min(end, text.length() - 1);
				text = text.substring(start, end + 1);
			}
		}
		return text;
	}

	/**
	 * Returns the maximum number of characters that the receiver is capable of
	 * holding.
	 * <p>
	 * If this has not been changed by <code>setTextLimit()</code>, it will be
	 * the constant <code>Text.LIMIT</code>.
	 * </p>
	 * 
	 * @return the text limit
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #LIMIT
	 */
	public int getTextLimit() {
		checkWidget();
		if (isSingleLineEdit()) {
			return getQLineEdit().maxLength();
		}
		return textLimit > 0 ? textLimit : LIMIT;
	}

	/**
	 * Returns the zero-relative index of the line which is currently at the top
	 * of the receiver.
	 * <p>
	 * This index can change when lines are scrolled or new lines are added or
	 * removed.
	 * </p>
	 * 
	 * @return the index of the top line
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getTopIndex() {
		checkWidget();
		if (isSingleLineEdit()) {
			return 0;
		}
		QScrollBar scrollBar = getQTextEdit().verticalScrollBar();
		int index = 0;
		if (scrollBar != null) {
			int top = scrollBar.value();
			QTextCursor textCursor = getQTextEdit().cursorForPosition(new QPoint(0, 0));
			QTextBlock topBlock = textCursor.block();
			QTextLayout layout = topBlock.layout();
			if (layout != null && layout.position().y() < top) {
				int layoutPos = (int) Math.round(layout.position().y());
				for (int i = 0; i < layout.lineCount()
						&& layoutPos + layout.lineAt(i).rect().bottom() <= top + layout.lineAt(i).rect().height() / 2; ++i) {
					++index;
				}
			}
			index += getNumberOfPrecedingTextLines(topBlock);
		}
		return 0;
	}

	/**
	 * Returns the top pixel.
	 * <p>
	 * The top pixel is the pixel position of the line that is currently at the
	 * top of the widget. On some platforms, a text widget can be scrolled by
	 * pixels instead of lines so that a partial line is displayed at the top of
	 * the widget.
	 * </p>
	 * <p>
	 * The top pixel changes when the widget is scrolled. The top pixel does not
	 * include the widget trimming.
	 * </p>
	 * 
	 * @return the pixel position of the top line
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getTopPixel() {
		checkWidget();
		if (isSingleLineEdit()) {
			return 0;
		}
		return getQTextEdit().verticalScrollBar().value();
	}

	/**
	 * Inserts a string.
	 * <p>
	 * The old selection is replaced with the new text.
	 * </p>
	 * 
	 * @param string
	 *            the string
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the string is
	 *                <code>null</code></li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void insert(String string) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		string = Display.withCrLf(string);
		getQLineEdit().insert(string);
	}

	/**
	 * Pastes text from clipboard.
	 * <p>
	 * The selected text is deleted from the widget and new text inserted from
	 * the clipboard.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void paste() {
		checkWidget();
		if (isSingleLineEdit()) {
			getQLineEdit().paste();
		} else {
			getQTextEdit().paste();
		}
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the receiver's text is modified.
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
	 * @see ModifyListener
	 * @see #addModifyListener
	 */
	public void removeModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Modify, listener);
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
	 * Removes the listener from the collection of listeners who will be
	 * notified when the control is verified.
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
	 * @see VerifyListener
	 * @see #addVerifyListener
	 */
	public void removeVerifyListener(VerifyListener listener) {
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (eventTable == null) {
			return;
		}
		eventTable.unhook(SWT.Verify, listener);
	}

	/**
	 * Selects all the text in the receiver.
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void selectAll() {
		checkWidget();
		if (isSingleLineEdit()) {
			getQLineEdit().selectAll();
		} else {
			QTextCursor textCursor = getQTextEdit().textCursor();
			textCursor.select(QTextCursor.SelectionType.Document);
			getQTextEdit().setTextCursor(textCursor);
		}
	}

	/**
	 * Sets the double click enabled flag.
	 * <p>
	 * The double click flag enables or disables the default action of the text
	 * widget when the user double clicks.
	 * </p>
	 * <p>
	 * Note: This operation is a hint and is not supported on platforms that do
	 * not have this concept.
	 * </p>
	 * 
	 * @param doubleClick
	 *            the new double click flag
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setDoubleClickEnabled(boolean doubleClick) {
		checkWidget();
		this.doubleClick = doubleClick;
	}

	/**
	 * Sets the echo character.
	 * <p>
	 * The echo character is the character that is displayed when the user
	 * enters text or the text is changed by the programmer. Setting the echo
	 * character to '\0' clears the echo character and redraws the original
	 * text. If for any reason the echo character is invalid, or if the platform
	 * does not allow modification of the echo character, the default echo
	 * character for the platform is used.
	 * </p>
	 * 
	 * @param echo
	 *            the new echo character
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setEchoChar(char echo) {
		checkWidget();
		if (isSingleLineEdit()) {
			if (echo == '\0') {
				getQLineEdit().setEchoMode(EchoMode.Normal);
			} else {
				getQLineEdit().setEchoMode(EchoMode.Password);
			}
		}
	}

	/**
	 * Sets the editable state.
	 * 
	 * @param editable
	 *            the new editable state
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setEditable(boolean editable) {
		checkWidget();
		if (isSingleLineEdit()) {
			getQLineEdit().setReadOnly(!editable);
		} else {
			getQTextEdit().setReadOnly(!editable);
		}
	}

	@Override
	public void setFont(Font font) {
		checkWidget();
		super.setFont(font);
		setTabStops(tabs);
	}

	/**
	 * Sets the widget message. The message text is displayed as a hint for the
	 * user, indicating the purpose of the field.
	 * <p>
	 * Typically this is used in conjunction with <code>SWT.SEARCH</code>.
	 * </p>
	 * 
	 * @param message
	 *            the new message
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the message is null</li>
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
	public void setMessage(String message) {
		checkWidget();
		getQWidget().setWhatsThis(message);
	}

	/**
	 * Sets the orientation of the receiver, which must be one of the constants
	 * <code>SWT.LEFT_TO_RIGHT</code> or <code>SWT.RIGHT_TO_LEFT</code>.
	 * <p>
	 * Note: This operation is a hint and is not supported on platforms that do
	 * not have this concept.
	 * </p>
	 * 
	 * @param orientation
	 *            new orientation style
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @since 2.1.2
	 */
	public void setOrientation(int orientation) {
		checkWidget();
		int flags = SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT;
		if ((orientation & flags) == 0 || (orientation & flags) == flags) {
			return;
		}

		style &= ~flags;
		style |= orientation & flags;

		LayoutDirection direction = LayoutDirection.LeftToRight;
		if (orientation == SWT.RIGHT_TO_LEFT) {
			direction = LayoutDirection.RightToLeft;
		}
		getQWidget().setLayoutDirection(direction);
	}

	/**
	 * Sets the selection.
	 * <p>
	 * Indexing is zero based. The range of a selection is from 0..N where N is
	 * the number of characters in the widget.
	 * </p>
	 * <p>
	 * Text selections are specified in terms of caret positions. In a text
	 * widget that contains N characters, there are N+1 caret positions, ranging
	 * from 0..N. This differs from other functions that address character
	 * position such as getText () that use the regular array indexing rules.
	 * </p>
	 * 
	 * @param start
	 *            new caret position
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(int start) {
		checkWidget();
		setSelection(start, start);
	}

	/**
	 * Sets the selection to the range specified by the given start and end
	 * indices.
	 * <p>
	 * Indexing is zero based. The range of a selection is from 0..N where N is
	 * the number of characters in the widget.
	 * </p>
	 * <p>
	 * Text selections are specified in terms of caret positions. In a text
	 * widget that contains N characters, there are N+1 caret positions, ranging
	 * from 0..N. This differs from other functions that address character
	 * position such as getText () that use the usual array indexing rules.
	 * </p>
	 * 
	 * @param start
	 *            the start of the range
	 * @param end
	 *            the end of the range
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(int start, int end) {
		checkWidget();
		int length = getCharCount();
		start = Math.min(Math.max(0, start), length);
		end = Math.min(Math.max(0, end), length);

		if (isSingleLineEdit()) {
			getQLineEdit().setSelection(start, end - start);
		} else {
			setCursorPosition(start, true);
			setCursorPosition(end, false);
		}

	}

	private void setCursorPosition(int pos, boolean moveAnchor) {
		QTextCursor textCursor = getQTextEdit().textCursor();
		textCursor.setPosition(pos, moveAnchor ? MoveMode.MoveAnchor : MoveMode.KeepAnchor);
		getQTextEdit().setTextCursor(textCursor);
	}

	/**
	 * Sets the selection to the range specified by the given point, where the x
	 * coordinate represents the start index and the y coordinate represents the
	 * end index.
	 * <p>
	 * Indexing is zero based. The range of a selection is from 0..N where N is
	 * the number of characters in the widget.
	 * </p>
	 * <p>
	 * Text selections are specified in terms of caret positions. In a text
	 * widget that contains N characters, there are N+1 caret positions, ranging
	 * from 0..N. This differs from other functions that address character
	 * position such as getText () that use the usual array indexing rules.
	 * </p>
	 * 
	 * @param selection
	 *            the point
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the point is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setSelection(Point selection) {
		checkWidget();
		if (selection == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		setSelection(selection.x, selection.y);
	}

	/**
	 * Sets the number of tabs.
	 * <p>
	 * Tab stop spacing is specified in terms of the space (' ') character. The
	 * width of a single tab stop is the pixel width of the spaces.
	 * </p>
	 * 
	 * @param tabs
	 *            the number of tabs
	 * 
	 *            </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setTabs(int tabs) {
		checkWidget();
		if (tabs < 0) {
			return;
		}
		this.tabs = tabs;
		setTabStops(this.tabs);
	}

	void setTabStops(int tabs) {
		if (!isSingleLineEdit()) {
			getQTextEdit().setTabStopWidth(getQTextEdit().fontMetrics().width(" ") * tabs);//$NON-NLS-1$
		}
	}

	/**
	 * Sets the contents of the receiver to the given string. If the receiver
	 * has style SINGLE and the argument contains multiple lines of text, the
	 * result of this operation is undefined and may vary from platform to
	 * platform.
	 * 
	 * @param string
	 *            the new text
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
	 */
	public void setText(String string) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		string = Display.withCrLf(string);
		if (isSingleLineEdit()) {
			getQLineEdit().setText(string);
		} else {
			getQTextEdit().setText(string);
		}
	}

	/**
	 * Sets the maximum number of characters that the receiver is capable of
	 * holding to be the argument.
	 * <p>
	 * Instead of trying to set the text limit to zero, consider creating a
	 * read-only text widget.
	 * </p>
	 * <p>
	 * To reset this value to the default, use
	 * <code>setTextLimit(Text.LIMIT)</code>. Specifying a limit value larger
	 * than <code>Text.LIMIT</code> sets the receiver's limit to
	 * <code>Text.LIMIT</code>.
	 * </p>
	 * 
	 * @param limit
	 *            new text limit
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_CANNOT_BE_ZERO - if the limit is zero</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see #LIMIT
	 */
	public void setTextLimit(int limit) {
		checkWidget();
		if (limit == 0) {
			error(SWT.ERROR_CANNOT_BE_ZERO);
		}

		if (limit < 0 || limit > LIMIT) {
			limit = LIMIT;
		}

		if (isSingleLineEdit()) {
			getQLineEdit().setMaxLength(limit);
		} else {
			textLimit = limit;
			if (getCharCount() > limit) {
				getQTextEdit().setPlainText(getText().substring(0, limit));
			}
		}
	}

	/**
	 * Sets the zero-relative index of the line which is currently at the top of
	 * the receiver. This index can change when lines are scrolled or new lines
	 * are added and removed.
	 * 
	 * @param index
	 *            the index of the top item
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setTopIndex(int index) {
		checkWidget();
		if (!isSingleLineEdit()) {
			index = Math.min(Math.max(index, 0), getLineCount() - 1);
			QScrollBar scrollBar = getQTextEdit().verticalScrollBar();
			QTextDocument textDocument = getQTextEdit().document();
			if (scrollBar != null && textDocument != null) {
				if (index == 0) {
					scrollBar.setValue(0);
				} else {
					QTextBlock textBlock = textDocument.begin();
					int lineCount = 0;
					while (textBlock.isValid()) {
						QTextLayout layout = textBlock.layout();
						if (layout != null) {
							int oldLineCount = lineCount;
							int lines = layout.lineCount();
							lineCount += lines > 0 ? lines : 1;
							if (index < lineCount) {
								int linePosition = (int) Math.round(layout.position().y()
										+ layout.lineAt(index - oldLineCount).y());
								scrollBar.setValue(linePosition);
								break;
							}
						}
						textBlock = textBlock.next();
					}
				}
			}
		}
	}

	/**
	 * Shows the selection.
	 * <p>
	 * If the selection is already showing in the receiver, this method simply
	 * returns. Otherwise, lines are scrolled until the selection is visible.
	 * </p>
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void showSelection() {
		checkWidget();
		if (!isSingleLineEdit()) {
			getQTextEdit().ensureCursorVisible();
		}
	}

	private final class MyQTextEdit extends QTextEdit {

		@Override
		protected void mousePressEvent(QMouseEvent e) {
			super.mousePressEvent(e);
			e.setAccepted(false);
		}
	}
}
