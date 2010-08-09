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

import com.trolltech.qt.gui.QContentsMargins;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.internal.qt.QtSWTConverter;

/**
 * Instances of this class provide an etched border with an optional title.
 * <p>
 * Shadow styles are hints and may not be honoured by the platform. To create a
 * group with the default shadow style for the platform, do not specify a shadow
 * style.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>SHADOW_ETCHED_IN, SHADOW_ETCHED_OUT, SHADOW_IN, SHADOW_OUT, SHADOW_NONE</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * Note: Only one of the above styles may be specified.
 * </p>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example:
 *      ControlExample</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class Group extends Composite {

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
	 * @see SWT#SHADOW_ETCHED_IN
	 * @see SWT#SHADOW_ETCHED_OUT
	 * @see SWT#SHADOW_IN
	 * @see SWT#SHADOW_OUT
	 * @see SWT#SHADOW_NONE
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Group(Composite parent, int style) {
		super(parent, checkStyle(style));
	}

	@Override
	protected QWidget createQWidget(int style) {
		state |= DRAW_BACKGROUND;
		state |= CANVAS;
		//TODO apply SWT.SHADOW_* styles
		return new QGroupBox();
	}

	QGroupBox getQGroupBox() {
		return (QGroupBox) getQWidget();
	}

	static int checkStyle(int style) {
		style |= SWT.NO_FOCUS;
		/*
		 * Even though it is legal to create this widget with scroll bars, they
		 * serve no useful purpose because they do not automatically scroll the
		 * widget's client area. The fix is to clear the SWT style.
		 */
		return style & ~(SWT.H_SCROLL | SWT.V_SCROLL);
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass()) {
			error(SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	@Override
	public Rectangle getClientArea() {
		checkWidget();
		if (!isVisible()) {
			updateQLayouts();
		}

		Rectangle clientArea = QtSWTConverter.convert(getQWidget().contentsRect());
		if (clientArea.width < 0) {
			clientArea.width = 0;
		}
		if (clientArea.height < 0) {
			clientArea.height = 0;
		}

		return clientArea;
	}

	@Override
	public Rectangle computeTrim(int x, int y, int width, int height) {
		QContentsMargins margins = getQGroupBox().getContentsMargins();
		return new Rectangle(x - margins.left, y - margins.top, width + margins.left + margins.right, height
				+ margins.top + margins.bottom);
	}

	@Override
	String getNameText() {
		return getText();
	}

	/**
	 * Returns the receiver's text, which is the string that the is used as the
	 * <em>title</em>. If the text has not previously been set, returns an empty
	 * string.
	 * 
	 * @return the text
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
		return getQGroupBox().title();
	}

	@Override
	boolean mnemonicHit(char key) {
		return setFocus();
	}

	@Override
	boolean mnemonicMatch(char key) {
		char mnemonic = findMnemonic(getText());
		if (mnemonic == '\0') {
			return false;
		}
		return Character.toUpperCase(key) == Character.toUpperCase(mnemonic);
	}

	void printWidget(int /* long */hwnd, int /* long */hdc, GC gc) {
		// TODO
		throw new UnsupportedOperationException("not yet implemented");//$NON-NLS-1$
		// /*
		// * Bug in Windows. For some reason, PrintWindow() returns success but
		// * does nothing when it is called on a printer. The fix is to just go
		// * directly to WM_PRINT in this case.
		// */
		// boolean success = false;
		// if ( !( OS.GetDeviceCaps( gc.handle, OS.TECHNOLOGY ) ==
		// OS.DT_RASPRINTER ) ) {
		// success = OS.PrintWindow( hwnd, hdc, 0 );
		// }
		//
		// /*
		// * Bug in Windows. For some reason, PrintWindow() fails when it is
		// * called on a push button. The fix is to detect the failure and use
		// * WM_PRINT instead. Note that WM_PRINT cannot be used all the time
		// * because it fails for browser controls when the browser has focus.
		// */
		// if ( !success ) {
		// /*
		// * Bug in Windows. For some reason, WM_PRINT when called with
		// * PRF_CHILDREN will not draw the tool bar divider for tool bar
		// * children that do not have CCS_NODIVIDER. The fix is to draw the
		// * group box and iterate through the children, drawing each one.
		// */
		// int flags = OS.PRF_CLIENT | OS.PRF_NONCLIENT | OS.PRF_ERASEBKGND;
		// OS.SendMessage( hwnd, OS.WM_PRINT, hdc, flags );
		// int nSavedDC = OS.SaveDC( hdc );
		// Control[] children = _getChildren();
		// Rectangle rect = getBounds();
		// OS.IntersectClipRect( hdc, 0, 0, rect.width, rect.height );
		// for ( int i = children.length - 1; i >= 0; --i ) {
		// Point location = children[i].getLocation();
		// int graphicsMode = OS.GetGraphicsMode( hdc );
		// if ( graphicsMode == OS.GM_ADVANCED ) {
		// float[] lpXform = { 1, 0, 0, 1, location.x, location.y };
		// OS.ModifyWorldTransform( hdc, lpXform, OS.MWT_LEFTMULTIPLY );
		// } else {
		// OS.SetWindowOrgEx( hdc, -location.x, -location.y, null );
		// }
		// int /* long */topHandle = children[i].topHandle();
		// int bits = OS.GetWindowLong( topHandle, OS.GWL_STYLE );
		// if ( ( bits & OS.WS_VISIBLE ) == 0 ) {
		// OS.DefWindowProc( topHandle, OS.WM_SETREDRAW, 1, 0 );
		// }
		// children[i].printWidget( topHandle, hdc, gc );
		// if ( ( bits & OS.WS_VISIBLE ) == 0 ) {
		// OS.DefWindowProc( topHandle, OS.WM_SETREDRAW, 0, 0 );
		// }
		// if ( graphicsMode == OS.GM_ADVANCED ) {
		// float[] lpXform = { 1, 0, 0, 1, -location.x, -location.y };
		// OS.ModifyWorldTransform( hdc, lpXform, OS.MWT_LEFTMULTIPLY );
		// }
		// }
		// OS.RestoreDC( hdc, nSavedDC );
		// }
	}

	@Override
	public void setFont(Font font) {
		checkWidget();
		Rectangle oldRect = getClientArea();
		super.setFont(font);
		Rectangle newRect = getClientArea();
		if (!oldRect.equals(newRect)) {
			sendResize();
		}
	}

	/**
	 * Sets the receiver's text, which is the string that will be displayed as
	 * the receiver's <em>title</em>, to the argument, which may not be null.
	 * The string may include the mnemonic character. </p> Mnemonics are
	 * indicated by an '&amp;' that causes the next character to be the
	 * mnemonic. When the user presses a key sequence that matches the mnemonic,
	 * focus is assigned to the first child of the group. On most platforms, the
	 * mnemonic appears underlined but may be emphasised in a platform specific
	 * manner. The mnemonic indicator character '&amp;' can be escaped by
	 * doubling it in the string, causing a single '&amp;' to be displayed. </p>
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
		getQGroupBox().setTitle(string);
	}
}
