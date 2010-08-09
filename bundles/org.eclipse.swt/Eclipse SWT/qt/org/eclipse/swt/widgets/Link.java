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

import com.trolltech.qt.core.Qt.TextInteractionFlag;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Instances of this class represent a selectable user interface object that
 * displays a text with links.
 * <p>
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
public class Link extends Control {
	private Point[] offsets;
	private String[] ids;
	private int[] mnemonics;

	private static final String LINK_START = "<a>";//$NON-NLS-1$
	private static final String LINK_END = "</a>";//$NON-NLS-1$

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
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public Link(Composite parent, int style) {
		super(parent, checkBits(style, SWT.LEFT, SWT.CENTER, SWT.RIGHT, 0, 0, 0));
	}

	@Override
	protected QWidget createQWidget(int style) {
		state |= THEME_BACKGROUND;
		QLabel link = new QLabel();
		link.setTextInteractionFlags(TextInteractionFlag.TextBrowserInteraction);
		link.setOpenExternalLinks(false);
		return link;
	}

	protected QLabel getQLabel() {
		return (QLabel) getQWidget();
	}

	@Override
	protected void connectSignals() {
		getQLabel().linkActivated.connect(this, "linkActivated(java.lang.String)");//$NON-NLS-1$
	}

	protected void linkActivated(String link) {
		Event event = new Event();
		event.text = link;
		sendEvent(SWT.Selection, event);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the control is selected by the user, by sending it one of the
	 * messages defined in the <code>SelectionListener</code> interface.
	 * <p>
	 * <code>widgetSelected</code> is called when the control is selected by the
	 * user. <code>widgetDefaultSelected</code> is not called.
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
		checkWidget();
		if (listener == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Selection, typedListener);
		addListener(SWT.DefaultSelection, typedListener);
	}

	void initAccessible() {
		Accessible accessible = getAccessible();
		accessible.addAccessibleListener(new AccessibleAdapter() {
			@Override
			public void getName(AccessibleEvent e) {
				e.result = parse(_getText());
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

	@Override
	String getNameText() {
		return getText();
	}

	/**
	 * Returns the receiver's text, which will be an empty string if it has
	 * never been set.
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
		return _getText();
	}

	private String _getText() {
		return getQLabel().text();
	}

	private String parse(String string) {
		int length = string.length();
		offsets = new Point[length / 4];
		ids = new String[length / 4];
		mnemonics = new int[length / 4 + 1];
		StringBuffer result = new StringBuffer();
		char[] buffer = new char[length];
		string.getChars(0, string.length(), buffer, 0);
		int index = 0, state = 0, linkIndex = 0;
		int start = 0, tagStart = 0, linkStart = 0, endtagStart = 0, refStart = 0;
		while (index < length) {
			char c = Character.toLowerCase(buffer[index]);
			switch (state) {
			case 0:
				if (c == '<') {
					tagStart = index;
					state++;
				}
				break;
			case 1:
				if (c == 'a') {
					state++;
				}
				break;
			case 2:
				switch (c) {
				case 'h':
					state = 7;
					break;
				case '>':
					linkStart = index + 1;
					state++;
					break;
				default:
					if (Character.isWhitespace(c)) {
						break;
					}
					state = 13;
				}
				break;
			case 3:
				if (c == '<') {
					endtagStart = index;
					state++;
				}
				break;
			case 4:
				state = c == '/' ? state + 1 : 3;
				break;
			case 5:
				state = c == 'a' ? state + 1 : 3;
				break;
			case 6:
				if (c == '>') {
					mnemonics[linkIndex] = parseMnemonics(buffer, start, tagStart, result);
					int offset = result.length();
					parseMnemonics(buffer, linkStart, endtagStart, result);
					offsets[linkIndex] = new Point(offset, result.length() - 1);
					if (ids[linkIndex] == null) {
						ids[linkIndex] = new String(buffer, linkStart, endtagStart - linkStart);
					}
					linkIndex++;
					start = tagStart = linkStart = endtagStart = refStart = index + 1;
					state = 0;
				} else {
					state = 3;
				}
				break;
			case 7:
				state = c == 'r' ? state + 1 : 0;
				break;
			case 8:
				state = c == 'e' ? state + 1 : 0;
				break;
			case 9:
				state = c == 'f' ? state + 1 : 0;
				break;
			case 10:
				state = c == '=' ? state + 1 : 0;
				break;
			case 11:
				if (c == '"') {
					state++;
					refStart = index + 1;
				} else {
					state = 0;
				}
				break;
			case 12:
				if (c == '"') {
					ids[linkIndex] = new String(buffer, refStart, index - refStart);
					state = 2;
				}
				break;
			case 13:
				if (Character.isWhitespace(c)) {
					state = 0;
				} else if (c == '=') {
					state++;
				}
				break;
			case 14:
				state = c == '"' ? state + 1 : 0;
				break;
			case 15:
				if (c == '"') {
					state = 2;
				}
				break;
			default:
				state = 0;
				break;
			}
			index++;
		}
		if (start < length) {
			int tmp = parseMnemonics(buffer, start, tagStart, result);
			int mnemonic = parseMnemonics(buffer, Math.max(tagStart, linkStart), length, result);
			if (mnemonic == -1) {
				mnemonic = tmp;
			}
			mnemonics[linkIndex] = mnemonic;
		} else {
			mnemonics[linkIndex] = -1;
		}
		if (offsets.length != linkIndex) {
			Point[] newOffsets = new Point[linkIndex];
			System.arraycopy(offsets, 0, newOffsets, 0, linkIndex);
			offsets = newOffsets;
			String[] newIDs = new String[linkIndex];
			System.arraycopy(ids, 0, newIDs, 0, linkIndex);
			ids = newIDs;
			int[] newMnemonics = new int[linkIndex + 1];
			System.arraycopy(mnemonics, 0, newMnemonics, 0, linkIndex + 1);
			mnemonics = newMnemonics;
		}
		return result.toString();
	}

	private int parseMnemonics(char[] buffer, int start, int end, StringBuffer result) {
		int mnemonic = -1, index = start;
		while (index < end) {
			if (buffer[index] == '&') {
				if (index + 1 < end && buffer[index + 1] == '&') {
					result.append(buffer[index]);
					index++;
				} else {
					mnemonic = result.length();
				}
			} else {
				result.append(buffer[index]);
			}
			index++;
		}
		return mnemonic;
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		offsets = null;
		ids = null;
		mnemonics = null;
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
	 * Sets the receiver's text.
	 * <p>
	 * The string can contain both regular text and hyperlinks. A hyperlink is
	 * delimited by an anchor tag, &lt;A&gt; and &lt;/A&gt;. Within an anchor, a
	 * single HREF attribute is supported. When a hyperlink is selected, the
	 * text field of the selection event contains either the text of the
	 * hyperlink or the value of its HREF, if one was specified. In the rare
	 * case of identical hyperlinks within the same string, the HREF tag can be
	 * used to distinguish between them. The string may include the mnemonic
	 * character and line delimiters.
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
		if (string.equals(_getText())) {
			return;
		}
		String text = string.replace("<A>", "<a>");//$NON-NLS-1$ //$NON-NLS-2$
		text = text.replace("</A>", "</a>");//$NON-NLS-1$ //$NON-NLS-2$
		getQLabel().setText(adjustTags(text));
	}

	/*
	 * QLabel does not render a link as a link if it is created like this: Text
	 * and <a>link</a> and another <a>link</a> QLabel requires the href part to
	 * be there also. This function changes the string above to this: Text and
	 * <a href="link">link</a> and another <a href="link">link</a>
	 */
	static String adjustTags(String str) {
		int pos = str.indexOf(LINK_START);

		if (pos == -1) {
			return str;
		}

		int start = 0;
		int endPos = pos;
		StringBuffer sb = new StringBuffer();
		String clean = "";//$NON-NLS-1$

		while (pos != -1) {
			sb.append(str.substring(start, pos)).append("<a href=\"");//$NON-NLS-1$
			start = pos + LINK_START.length();
			endPos = str.indexOf(LINK_END, start);
			if (endPos != -1) {
				clean = clean(str.substring(start, endPos));
				sb.append(clean);
			} else {
				// If the link format is invalid in any way, just use
				// original string and try not to be too clever.
				return str;
			}

			sb.append("\">" + clean + LINK_END);//$NON-NLS-1$
			start = endPos + LINK_END.length();
			pos = str.indexOf(LINK_START, start);
		}

		return sb.toString();
	}

	static String clean(String strToClean) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strToClean.length(); i++) {
			char charAt = strToClean.charAt(i);
			if (charAt == '"') {
				sb.append("&quot;"); //$NON-NLS-1$
			} else {
				if (charAt == '<') {
					sb.append("&lt;"); //$NON-NLS-1$
				} else {
					if (charAt == '>') {
						sb.append("&gt;"); //$NON-NLS-1$
					} else {
						sb.append(charAt);
					}
				}
			}
		}
		return sb.toString();
	}
}
