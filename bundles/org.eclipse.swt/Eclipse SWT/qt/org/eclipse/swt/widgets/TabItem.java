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

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Instances of this class represent a selectable user interface object
 * corresponding to a tab for a page in a tab folder.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is <em>not</em> intended to be subclassed.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/snippets/#tabfolder">TabFolder,
 *      TabItem snippets</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */

public class TabItem extends Item {
	TabFolder parent;
	Control control;
	private String toolTipText;

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>TabFolder</code>) and a style value describing its behavior and
	 * appearance. The item is added to the end of the items maintained by its
	 * parent.
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
	 * @see SWT
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public TabItem(TabFolder parent, int style) {
		super(parent, style);
		this.parent = parent;
		parent.createItem(this, parent.getItemCount());
	}

	/**
	 * Constructs a new instance of this class given its parent (which must be a
	 * <code>TabFolder</code>), a style value describing its behavior and
	 * appearance, and the index at which to place it in the items maintained by
	 * its parent.
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
	 * @param index
	 *            the zero-relative index to store the receiver in its parent
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
	 *                <li>ERROR_INVALID_RANGE - if the index is not between 0
	 *                and the number of elements in the parent (inclusive)</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the parent</li>
	 *                <li>ERROR_INVALID_SUBCLASS - if this class is not an
	 *                allowed subclass</li>
	 *                </ul>
	 * 
	 * @see SWT
	 * @see Widget#checkSubclass
	 * @see Widget#getStyle
	 */
	public TabItem(TabFolder parent, int style, int index) {
		super(parent, style);
		this.parent = parent;
		parent.createItem(this, index);
	}

	void _setText(int index, String string) {
		parent.getQTabWidget().setTabText(index, string);
	}

	@Override
	protected void checkSubclass() {
		if (!isValidSubclass()) {
			error(SWT.ERROR_INVALID_SUBCLASS);
		}
	}

	@Override
	void destroyWidget() {
		parent.destroyItem(this);
		releaseQWidget();
	}

	/**
	 * Returns the control that is used to fill the client area of the tab
	 * folder when the user selects the tab item. If no control has been set,
	 * return <code>null</code>.
	 * <p>
	 * 
	 * @return the control
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public Control getControl() {
		checkWidget();
		return control;
	}

	/**
	 * Returns a rectangle describing the receiver's size and location relative
	 * to its parent.
	 * 
	 * @return the receiver's bounding rectangle
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
	public Rectangle getBounds() {
		checkWidget();
		int index = parent.indexOf(this);
		if (index == -1 || control == null) {
			return new Rectangle(0, 0, 0, 0);
		}
		return control.getBounds();
	}

	/**
	 * Returns the receiver's parent, which must be a <code>TabFolder</code>.
	 * 
	 * @return the receiver's parent
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public TabFolder getParent() {
		checkWidget();
		return parent;
	}

	/**
	 * Returns the receiver's tool tip text, or null if it has not been set.
	 * 
	 * @return the receiver's tool tip text
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public String getToolTipText() {
		checkWidget();
		return toolTipText;
	}

	@Override
	void releaseQWidget() {
		super.releaseQWidget();
		parent = null;
	}

	@Override
	void releaseParent() {
		super.releaseParent();
		int index = parent.indexOf(this);
		if (index == parent.getSelectionIndex()) {
			if (control != null) {
				control.setVisible(false);
			}
		}
	}

	@Override
	void releaseWidget() {
		super.releaseWidget();
		control = null;
	}

	/**
	 * Sets the control that is used to fill the client area of the tab folder
	 * when the user selects the tab item.
	 * <p>
	 * 
	 * @param control
	 *            the new control (or null)
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_INVALID_ARGUMENT - if the control has been
	 *                disposed</li> <li>ERROR_INVALID_PARENT - if the control is
	 *                not in the same widget tree</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li> <li>ERROR_THREAD_INVALID_ACCESS - if not
	 *                called from the thread that created the receiver</li>
	 *                </ul>
	 */
	public void setControl(Control control) {
		checkWidget();
		if (control != null) {
			if (control.isDisposed()) {
				error(SWT.ERROR_INVALID_ARGUMENT);
			}
			if (control.parent != parent) {
				error(SWT.ERROR_INVALID_PARENT);
			}
		}
		if (this.control != null && this.control.isDisposed()) {
			this.control = null;
		}
		Control oldControl = this.control, newControl = control;
		this.control = control;
		parent.updateItem(this, oldControl);
		int index = parent.indexOf(this), selectionIndex = parent.getSelectionIndex();
		if (index != selectionIndex) {
			if (newControl != null) {
				if (selectionIndex != -1) {
					Control selectedControl = parent.getItem(selectionIndex).getControl();
					if (selectedControl == newControl) {
						return;
					}
				}
				return;
			}
		}
	}

	@Override
	public void setImage(Image image) {
		checkWidget();
		int index = parent.indexOf(this);
		if (index == -1) {
			return;
		}
		super.setImage(image);
		// TODO
	}

	/**
	 * Sets the receiver's text. The string may include the mnemonic character.
	 * </p>
	 * <p>
	 * Mnemonics are indicated by an '&amp;' that causes the next character to
	 * be the mnemonic. When the user presses a key sequence that matches the
	 * mnemonic, a selection event occurs. On most platforms, the mnemonic
	 * appears underlined but may be emphasised in a platform specific manner.
	 * The mnemonic indicator character '&amp;' can be escaped by doubling it in
	 * the string, causing a single '&amp;' to be displayed.
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
	 * 
	 */
	@Override
	public void setText(String string) {
		checkWidget();
		if (string == null) {
			error(SWT.ERROR_NULL_ARGUMENT);
		}
		if (string.equals(text)) {
			return;
		}
		int index = parent.indexOf(this);
		if (index == -1) {
			return;
		}
		super.setText(string);
		_setText(index, string);
	}

	/**
	 * Sets the receiver's tool tip text to the argument, which may be null
	 * indicating that the default tool tip for the control will be shown. For a
	 * control that has a default tool tip, such as the Tree control on Windows,
	 * setting the tool tip text to an empty string replaces the default,
	 * causing no tool tip text to be shown.
	 * <p>
	 * The mnemonic indicator (character '&amp;') is not displayed in a tool
	 * tip. To display a single '&amp;' in the tool tip, the character '&amp;'
	 * can be escaped by doubling it in the string.
	 * </p>
	 * 
	 * @param string
	 *            the new tool tip text (or null)
	 * 
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setToolTipText(String string) {
		checkWidget();
		toolTipText = string;
	}

}
