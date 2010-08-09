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

import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QDragEnterEvent;
import com.trolltech.qt.gui.QDragLeaveEvent;
import com.trolltech.qt.gui.QDragMoveEvent;
import com.trolltech.qt.gui.QDropEvent;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

/**
 * Instances of this class represent a non-selectable user interface object that
 * displays a string or image. When SEPARATOR is specified, displays a single
 * vertical or horizontal line.
 * <p>
 * Shadow styles are hints and may not be honored by the platform. To create a
 * separator label with the default shadow style for the platform, do not
 * specify a shadow style.
 * </p>
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SEPARATOR, HORIZONTAL, VERTICAL</dd>
 * <dd>SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dd>CENTER, LEFT, RIGHT, WRAP</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of SHADOW_IN, SHADOW_OUT and SHADOW_NONE may be specified.
 * SHADOW_NONE is a HINT. Only one of HORIZONTAL and VERTICAL may be specified.
 * Only one of CENTER, LEFT and RIGHT may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em> within the
 * SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#label">Label snippets</a>
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class Label extends Control {
	private String text = ""; //$NON-NLS-1$
	private Image image;

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
	 * @see SWT#SEPARATOR
	 * @see SWT#HORIZONTAL
	 * @see SWT#VERTICAL
	 * @see SWT#SHADOW_IN
	 * @see SWT#SHADOW_OUT
	 * @see SWT#SHADOW_NONE
	 * @see SWT#CENTER
	 * @see SWT#LEFT
	 * @see SWT#RIGHT
	 * @see SWT#WRAP
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Label(Composite parent, int style) {
		super(parent, checkStyle(style));
	}

	@Override
	protected QWidget createQWidget(int style) {
		state |= THEME_BACKGROUND;
		QLabel label = new MyQLabel();
		if ((style & SWT.SEPARATOR) != 0) {
			setSeparatorType(label, style);
		}
		label.setContentsMargins(0, 0, 0, 0);
		setAlignment(label, style);
		label.setMargin(0);
		// TODO make this work with the right size.
		//label.setWordWrap((style & SWT.WRAP) != 0);
		return label;
	}

	@Override
	protected void checkAndUpdateBorder() {
		// nothing on purpose
	}

	private void setSeparatorType(QLabel label, int style) {
		int frameStyle = 0;
		if ((style & SWT.VERTICAL) != 0) {
			frameStyle |= QFrame.Shape.VLine.value();
			label.setFixedWidth(2);
		} else {
			frameStyle |= QFrame.Shape.HLine.value();
			label.setMaximumHeight(2);
		}
		if ((style & SWT.SHADOW_IN) != 0) {
			frameStyle |= QFrame.Shadow.Raised.value();
		} else if ((style & SWT.SHADOW_OUT) != 0) {
			frameStyle |= QFrame.Shadow.Sunken.value();
		} else if ((style & SWT.SHADOW_NONE) != 0) {
			frameStyle |= QFrame.Shadow.Plain.value();
		}
		label.setFrameStyle(frameStyle);
	}

	private QLabel getQLabel() {
		return (QLabel) getQWidget();
	}

	static int checkStyle(int style) {
		style |= SWT.NO_FOCUS;
		if ((style & SWT.SEPARATOR) != 0) {
			style = checkBits(style, SWT.VERTICAL, SWT.HORIZONTAL, 0, 0, 0, 0);
			return checkBits(style, SWT.SHADOW_OUT, SWT.SHADOW_IN, SWT.SHADOW_NONE, 0, 0, 0);
		}
		return checkBits(style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0);
	}

	@Override
	public boolean qtMouseEnterEvent(Object source) {
		if (source == getQWidget()) {
			sendEvent(SWT.MouseEnter);
		}
		return false;
	}

	@Override
	public boolean qtMouseLeaveEvent(Object source) {
		if (source == getQWidget()) {
			sendEvent(SWT.MouseExit);
		}
		return false;
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		Point p = super.computeSize(wHint, hHint, changed);
		if ((style & SWT.SEPARATOR) != 0) {
			int width = p.x;
			int height = p.y;
			if ((style & SWT.VERTICAL) != 0) {
				width = 2;
			} else {
				height = Math.min(height, 3);
			}
			p.x = width;
			p.y = height;
		}
		return p;
	}

	@Override
	String getNameText() {
		return getText();
	}

	/**
	 * Returns the receiver's text, which will be an empty string if it has
	 * never been set or if the receiver is a <code>SEPARATOR</code> label.
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
		if ((style & SWT.SEPARATOR) != 0) {
			return "";//$NON-NLS-1$
		}
		return text;
	}

	/**
	 * Sets the receiver's text.
	 * <p>
	 * This method sets the widget label. The label may include the mnemonic
	 * character and line delimiters.
	 * </p>
	 * <p>
	 * Mnemonics are indicated by an '&amp;' that causes the next character to
	 * be the mnemonic. When the user presses a key sequence that matches the
	 * mnemonic, focus is assigned to the control that follows the label. On
	 * most platforms, the mnemonic appears underlined but may be emphasised in
	 * a platform specific manner. The mnemonic indicator character '&amp;' can
	 * be escaped by doubling it in the string, causing a single '&amp;' to be
	 * displayed.
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
		if ((style & SWT.SEPARATOR) != 0) {
			return;
		}
		if (string.equals(text)) {
			return;
		}
		text = string;
		string = Display.withCrLf(string);
		getQLabel().setText(string);
	}

	@Override
	boolean mnemonicHit(char key) {
		Composite control = this.parent;
		while (control != null) {
			Control[] children = control._getChildren();
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
					return true;
				}
			}
			control = control.parent;
		}
		return false;
	}

	@Override
	boolean mnemonicMatch(char key) {
		char mnemonic = findMnemonic(getText());
		if (mnemonic == '\0') {
			return false;
		}
		return Character.toUpperCase(key) == Character.toUpperCase(mnemonic);
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		text = null;
		image = null;
	}

	/**
	 * Returns a value which describes the position of the text or image in the
	 * receiver. The value will be one of <code>LEFT</code>, <code>RIGHT</code>
	 * or <code>CENTER</code> unless the receiver is a <code>SEPARATOR</code>
	 * label, in which case, <code>NONE</code> is returned.
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
		if ((style & SWT.SEPARATOR) != 0) {
			return 0;
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

	/**
	 * Controls how text and images will be displayed in the receiver. The
	 * argument should be one of <code>LEFT</code>, <code>RIGHT</code> or
	 * <code>CENTER</code>. If the receiver is a <code>SEPARATOR</code> label,
	 * the argument is ignored and the alignment is not changed.
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
		setAlignment(getQLabel(), alignment);
	}

	void setAlignment(QLabel label, int alignment) {
		if ((getStyle() & SWT.SEPARATOR) != 0) {
			return;
		}
		if ((alignment & SWT.RIGHT) != 0) {
			label.setAlignment(AlignmentFlag.AlignRight);
		} else if ((alignment & SWT.CENTER) != 0) {
			label.setAlignment(AlignmentFlag.AlignCenter);
		}
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
	 * Sets the receiver's image to the argument, which may be null indicating
	 * that no image should be displayed.
	 * 
	 * @param image
	 *            the image to display on the receiver (may be null)
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
		// dont crash if the image is null
		if (image == null) {
			return;
		}
		this.image = image;
		getQLabel().setPixmap(image.getQPixmap());
	}

	private final class MyQLabel extends QLabel {
		@Override
		protected void mousePressEvent(QMouseEvent e) {
			super.mousePressEvent(e);
			e.setAccepted(false);
		}

		@Override
		protected void dropEvent(QDropEvent event) {
			sendDropEvent(event);
		}

		@Override
		protected void dragMoveEvent(QDragMoveEvent event) {
			sendDragMoveEvent(event);
		}

		@Override
		protected void dragEnterEvent(QDragEnterEvent event) {
			sendDragEnterEvent(event);
		}

		@Override
		protected void dragLeaveEvent(QDragLeaveEvent event) {
			sendDragLeaveEvent(event);
		}

	}

}
